package lib.db.async

import java.lang.reflect.Field
import java.sql.{PreparedStatement, ResultSet}

import akka.actor.{ActorRef, ActorSystem}
import javax.inject.Inject
import lib.db.{AbstractEntity, Column, EntityClass, TableClass}
import models.DataBaseExecutionContext
import play.api.db.Database

import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */

class AsyncDBUtilImpl @Inject()(implicit database: Database, system: ActorSystem, ec: DataBaseExecutionContext) extends AsyncDBUtil {
    private val logger = org.slf4j.LoggerFactory.getLogger(classOf[AsyncDBUtilImpl])

    override def get[T >: Null, A](aClass: Class[T], id: A)(ref: ActorRef): Unit = {
        val tableClass = TableClass[T](aClass)
        val sql = s"select * from ${tableClass.tableName} where ${tableClass.primaryKeyColumn} = \'$id\'"
        executeQuery[T](tableClass, sql, null)(ref: ActorRef)
    }

    override def select[T](sql: String, entitiesClass: Class[T], params: Array[Object])(ref: ActorRef): Unit = {
        val entityClass = EntityClass[T](entitiesClass)
        executeQuery(entityClass, sql, params)(ref)
    }

    override def create[T](aClass: Class[T], t: T)(ref: ActorRef): Unit = {
        val tableClass = TableClass[T](aClass)
        val tableName = tableClass.tableName
        val fieldValMap = tableClass.fieldMap.map(entry => entry._1 -> entry._2.get(t))
        if (fieldValMap.isEmpty) throw new Exception("table columns can not be empty")
        val columnsStr: String = fieldValMap.keys.reduce((k1, k2) => k1 + ", " + k2)
        val paramsStr = fieldValMap.values.map(v => s"\'$v\'").reduce((v1, v2) => s"$v1, $v2")
        val sql: String = s"insert into $tableName ($columnsStr) values ($paramsStr)"
        execute(tableClass, sql, fieldValMap.values.toArray)(ref)
    }

    override def delete[T, A](aClass: Class[T], id: A)(ref: ActorRef): Unit = {
        val tableClass = TableClass[T](aClass)
        val tableName = tableClass.tableName
        val primaryKey: String = tableClass.primaryKeyColumn
        val sql: String = s"delete from $tableName where $primaryKey = '$id'"
        execute(tableClass, sql, null)(ref)
    }

    private def execute[T](tableClass: TableClass[T], sql: String, params: Array[Object])(ref: ActorRef): Unit = {
        logger.info(sql + ", " + params)
        ec -> {
            database.withConnection(connection => {
                val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                setParam(preparedStatement, params)
                ref ! preparedStatement.execute()
            })
        }
    }

    private def executeQuery[T](entityClass: AbstractEntity[T], sql: String, params: Array[Object])(ref: ActorRef): Unit = {
        logger.info(sql + ", " + params)
        ec -> {
            database.withConnection(connection => {
                val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                setParam(preparedStatement, params)
                val resultSet = preparedStatement.executeQuery()
                val fieldMap: Map[String, Field] = entityClass.getFieldMap
                while (resultSet.next()) {
                    ref ! constructEntity(entityClass, fieldMap, resultSet)
                }
            })
        }
    }

    private def executeUpdate[T](entityClass: AbstractEntity[T], sql: String, params: Array[Object])(ref: ActorRef): Unit = {
        logger.info(sql)
        database.withConnection(connection => {
            val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
            setParam(preparedStatement, params)
            ref ! preparedStatement.executeUpdate()
        })
    }

    private def setParam(preparedStatement: PreparedStatement, params: Array[Object]): Unit = {
        val paramPairs: List[(Int, Object)] = getParamPair(params)
        paramPairs.foreach(paramPair => preparedStatement.setObject(paramPair._1, paramPair._2))
    }

    private def constructEntity[T](abstractEntity: AbstractEntity[T], fieldMap: Map[String, Field], resultSet: ResultSet): T = {
        val fieldMapVal = fieldMap.map(entry => (entry._1, resultSet.getObject(entry._1)))
        val entity: T = abstractEntity.entityClass.getDeclaredConstructor().newInstance()
        abstractEntity.entityClass.getDeclaredFields.foreach(field => {
            field.setAccessible(true)
            val columnAnnotation = field.getAnnotation(classOf[Column])
            if (columnAnnotation != null) {
                val columnName = field.getAnnotation(classOf[Column]).name()
                val filedVal = fieldMapVal.get(columnName).orNull
                field.set(entity, filedVal)
            }
        })
        entity
    }

    private def getParamPair(params: Array[Object]): List[(Int, Object)] = {
        val paramPairs: ListBuffer[(Int, Object)] = ListBuffer.apply()
        if (params != null && params.nonEmpty) {
            for (elem <- params; index <- 1 to params.length) {
                paramPairs.append((index, elem))
            }
        }
        paramPairs.toList
    }
}
