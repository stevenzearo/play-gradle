package lib.db.async

import java.lang.reflect.Field
import java.sql.{PreparedStatement, ResultSet}
import java.util.concurrent.SubmissionPublisher

import akka.NotUsed
import akka.stream.scaladsl.{JavaFlowSupport, Source}
import javax.inject.Inject
import lib.db.{AbstractEntity, Column, EntityClass, TableClass}
import models.DataBaseExecutionContext
import play.api.db.Database

import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */

class AsyncDBUtilImpl @Inject()(implicit database: Database, ec: DataBaseExecutionContext) extends AsyncDBUtil {
    private val logger = org.slf4j.LoggerFactory.getLogger(classOf[AsyncDBUtilImpl])

    override def get[T >: Null, A](aClass: Class[T], id: A): Source[T, NotUsed] = {
        val tableClass = TableClass[T](aClass)
        val sql = s"select * from ${tableClass.tableName} where ${tableClass.primaryKeyColumn} = \'$id\'"
        executeQuery[T](tableClass, sql, null)
    }

    override def select[T](sql: String, entitiesClass: Class[T], params: Array[Object]): Source[T, NotUsed] = {
        val entityClass = EntityClass[T](entitiesClass)
        executeQuery(entityClass, sql, params)
    }

    override def create[T](aClass: Class[T], t: T): Source[Boolean, NotUsed] = {
        val tableClass = TableClass[T](aClass)
        val tableName = tableClass.tableName
        val fieldValMap = tableClass.fieldMap.map(entry => entry._1 -> entry._2.get(t))
        if (fieldValMap.isEmpty) throw new Exception("table columns can not be empty")
        val columnsStr: String = fieldValMap.keys.reduce((k1, k2) => k1 + ", " + k2)
        val paramsStr = fieldValMap.values.map(v => s"\'$v\'").reduce((v1, v2) => s"$v1, $v2")
        val sql: String = s"insert into $tableName ($columnsStr) values ($paramsStr)"
        execute(tableClass, sql, fieldValMap.values.toArray)
    }

    override def delete[T, A](aClass: Class[T], id: A): Source[Boolean, NotUsed] = {
        val tableClass = TableClass[T](aClass)
        val tableName = tableClass.tableName
        val primaryKey: String = tableClass.primaryKeyColumn
        val sql: String = s"delete from $tableName where $primaryKey = '$id'"
        execute(tableClass, sql, null)
    }

    private def execute[T](tableClass: TableClass[T], sql: String, params: Array[Object]): Source[Boolean, NotUsed] = {
        logger.info(sql + ", " + params)
        ec -> {
            database.withConnection(connection => {
                val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                setParam(preparedStatement, params)
                Source.single(preparedStatement.execute())
            })
        }
    }._2

    private def executeQuery[T](entityClass: AbstractEntity[T], sql: String, params: Array[Object]) = {
        logger.info(sql + ", " + params)
        ec -> {
            val publisher = new SubmissionPublisher[T]()
            database.withConnection(connection => {
                val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                setParam(preparedStatement, params)
                val resultSet = preparedStatement.executeQuery()
                val fieldMap: Map[String, Field] = entityClass.getFieldMap
                while (resultSet.next()) {
                    publisher.submit(constructEntity(entityClass, fieldMap, resultSet)
                    )
                }
            })
            JavaFlowSupport.Source.fromPublisher(publisher)
        }
    }._2

    private def executeUpdate[T](entityClass: AbstractEntity[T], sql: String, params: Array[Object]): Int = {
        logger.info(sql)
        database.withConnection(connection => {
            val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
            setParam(preparedStatement, params)
            preparedStatement.executeUpdate()
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
