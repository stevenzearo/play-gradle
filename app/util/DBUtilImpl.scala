package util

import java.lang.reflect.Field
import java.sql.{PreparedStatement, ResultSet}

import javax.inject.Inject
import play.api.db.Database

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
// todo
class DBUtilImpl @Inject()(implicit database: Database) extends DBUtil {

    override def get[T >: AnyRef](aClass: Class[T], id: Object): Option[T] = {
        val tableClass = TableClass[T](aClass)
        val entities: ListBuffer[T] = executeQuery[T](tableClass, s"select * from ${tableClass.tableName} where ${tableClass.primaryKeyColumn} = $id")
        var result: T = null
        if (entities.size > 1) throw new Exception("duplicated primary key")
        if (entities.nonEmpty) result = entities.head
        Option(result)
    }

    override def select[T](sql: String, entitiesClass: Class[T], params: Object*): mutable.ListBuffer[T] = {
        val entityClass = EntityClass[T](entitiesClass)
        executeQuery(entityClass, sql, params)
    }

    override def create[T](aClass: Class[T], t: T): Boolean = {
        val tableClass = TableClass[T](aClass)
        val tableName = tableClass.tableName
        val fieldValMap = tableClass.fieldMap.map(entry => entry._1 -> entry._2.get(t))
        if (fieldValMap.isEmpty) throw new Exception("table columns can not be empty")
        val columnsStr: String = fieldValMap.keys.reduce((k1, k2) => k1 + ", " + k2)
        val paramsStr = fieldValMap.values.map(v => s"\'$v\'").reduce((v1, v2) => s"$v1, $v2")
        val sql: String = s"insert into $tableName ($columnsStr) values ($paramsStr)"
        execute(tableClass, sql, fieldValMap.values)
    }

    override def delete(id: Object): Boolean = false

    private def execute[T](tableClass: TableClass[T], sql: String, params: Object*): Boolean = {
        var result: Boolean = false
        database.withConnection(connection => {
            val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
            val paramPairs = getParamPair(params)
            paramPairs.foreach(paramPair => preparedStatement.setObject(paramPair._1, paramPair._2))
            result = preparedStatement.execute()
        })
        result
    }

    private def executeQuery[T](entityClass: AbstractEntity[T], sql: String, params: Object*): mutable.ListBuffer[T] = {
        database.withConnection(connection => {
            val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
            val paramPairs: List[(Int, Object)] = getParamPair(params)
            paramPairs.foreach(paramPair => preparedStatement.setObject(paramPair._1, paramPair._2))
            val resultSet = preparedStatement.executeQuery()
            val fieldMap: Map[String, Field] = entityClass.getFieldMap
            val listBuffer = new mutable.ListBuffer[T]
            while (resultSet.next()) {
                listBuffer.append(constructEntity(entityClass, fieldMap, resultSet))
            }
            listBuffer
        })
    }

    /*
      private def executeUpdate(): Unit = {

      }*/

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

    private def getParamPair(params: Object*): List[(Int, Object)] = {
        val paramPairs: ListBuffer[(Int, Object)] = ListBuffer.apply()
        if (params != null && params.nonEmpty) {
            for (elem <- params; index <- 1 until params.length) {
                paramPairs.append((index, elem))
            }
        }
        paramPairs.toList
    }
}
