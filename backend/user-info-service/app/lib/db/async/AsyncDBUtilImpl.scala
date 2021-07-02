package lib.db.async

import lib.db.{AbstractEntity, Column, EntityClass, TableClass}
import models.DatabaseExecutionContext
import play.api.db.Database

import java.lang.reflect.Field
import java.sql.{Connection, PreparedStatement, ResultSet}
import javax.inject.Inject
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.reflect.ClassTag

/**
 * @author steve
 */

class AsyncDBUtilImpl @Inject()(implicit database: Database, ec: DatabaseExecutionContext) extends AsyncDBUtil {
    private val logger = org.slf4j.LoggerFactory.getLogger(classOf[AsyncDBUtilImpl])

    override def get[T: ClassTag, A](aClass: Class[T], id: A): Future[Option[T]] = {
        val tableClass: TableClass[T] = TableClass[T](aClass)
        val sql: String = s"select * from ${tableClass.tableName} where ${tableClass.primaryKeyColumn} = \'$id\'"
        val tArray: Future[Array[T]] = executeQuery[T](tableClass, sql, null)
        tArray.map[Option[T]](arr => arr.headOption)
    }

    override def select[T: ClassTag](sql: String, entitiesClass: Class[T], params: Array[Object]): Future[Array[T]] = {
        val entityClass: EntityClass[T] = EntityClass[T](entitiesClass)
        executeQuery[T](entityClass, sql, params)
    }

    override def create[T](aClass: Class[T], t: T): Future[Boolean] = {
        val tableClass: TableClass[T] = TableClass[T](aClass)
        val tableName: String = tableClass.tableName
        val fieldValMap: Map[String, AnyRef] = tableClass.fieldMap.map(entry => entry._1 -> entry._2.get(t))
        if (fieldValMap.isEmpty) throw new Exception("table columns can not be empty")
        val columnsStr: String = fieldValMap.keys.reduce((k1, k2) => k1 + ", " + k2)
        val paramsStr: String = fieldValMap.values.map(v => s"\'$v\'").reduce((v1, v2) => s"$v1, $v2")
        val sql: String = s"insert into $tableName ($columnsStr) values ($paramsStr)"
        execute[T](tableClass, sql, fieldValMap.values.toArray)
    }

    override def delete[T, A](aClass: Class[T], id: A): Future[Boolean] = {
        val tableClass: TableClass[T] = TableClass[T](aClass)
        val tableName: String = tableClass.tableName
        val primaryKey: String = tableClass.primaryKeyColumn
        val sql: String = s"delete from $tableName where $primaryKey = '$id'"
        execute[T](tableClass, sql, null)
    }

    private def execute[T](tableClass: TableClass[T], sql: String, params: Array[Object]): Future[Boolean] = {
        logger.info(sql + ", " + params)
        Future {
            var res = false
            database.withConnection(connection => {
                val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                setParam(preparedStatement, params)
                res = preparedStatement.execute()
            })
            res
        }
    }

    private def executeQuery[T: ClassTag](entityClass: AbstractEntity[T], sql: String, params: Array[Object]): Future[Array[T]] = {
        Future[Array[T]] {
            val t: mutable.Buffer[T] = mutable.Buffer.empty
            database.withConnection(connection => {
                val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                setParam(preparedStatement, params)
                logger.info(s"executing: $sql params: ${if (params == null) "" else params.mkString("Array(", ", ", ")")}")
                val resultSet = preparedStatement.executeQuery()
                val fieldMap: Map[String, Field] = entityClass.getFieldMap
                while (resultSet.next()) {
                    t.append(constructEntity(entityClass, fieldMap, resultSet))
                }
            })
            logger.info(s"result size: ${t.length}")
            t.toArray
        }
    }

    private def executeUpdate[T](entityClass: AbstractEntity[T], sql: String, params: Array[Object]): Future[Int] = {
        logger.info(sql)
        Future {
            var res: Int = 0
            database.withConnection(connection => {
                val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                setParam(preparedStatement, params)
                res = preparedStatement.executeUpdate()
            })
            res
        }
    }

    private def getResultSet[T](sql: String, params: Array[Object], connection: Connection): ResultSet = {
        val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
        setParam(preparedStatement, params)
        val resultSet: ResultSet = preparedStatement.executeQuery()
        resultSet
    }

    private def setParam(preparedStatement: PreparedStatement, params: Array[Object]): Unit = {
        if (params != null) {
            val paramPairs: List[(Int, Object)] = getParamPair(params)
            paramPairs.foreach(paramPair => preparedStatement.setObject(paramPair._1, paramPair._2))
        }
    }

    private def constructEntity[T](abstractEntity: AbstractEntity[T], fieldMap: Map[String, Field], resultSet: ResultSet): T = {
        val fieldMapVal: Map[String, AnyRef] = fieldMap.map(entry => (entry._1, resultSet.getObject(entry._1)))
        val entity: T = abstractEntity.entityClass.getDeclaredConstructor().newInstance()
        abstractEntity.entityClass.getDeclaredFields.foreach(field => {
            field.setAccessible(true)
            val columnAnnotation: Column = field.getAnnotation(classOf[Column])
            if (columnAnnotation != null) {
                val columnName: String = field.getAnnotation(classOf[Column]).name()
                val filedVal: AnyRef = fieldMapVal.get(columnName).orNull
                field.set(entity, filedVal)
            }
        })
        entity
    }

    private def getParamPair(params: Array[Object]): List[(Int, Object)] = {
        val paramPairs: ListBuffer[(Int, Object)] = ListBuffer.apply()
        if (params != null && params.nonEmpty) {
            params.zipWithIndex.foreach(elem => paramPairs.append((elem._2, elem._1)))
        }
        paramPairs.toList
    }
}
