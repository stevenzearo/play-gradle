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

  override def get[T >: AnyRef](id: Object): Option[T] = {
    val entityClass = classOf[T]
    val tableClass = new TableClass[T](entityClass)
    val entities: ListBuffer[T] = executeQuery[T](tableClass, s"select * from ${tableClass.tableName} where ${tableClass.primaryKeyColumn} = $id")
    var result: T = null
    if (entities.size > 1) throw new Exception("duplicated primary key")
    if (entities.nonEmpty) result = entities.head
    Option(result)
  }

  override def select[T >: AnyRef](sql: String, entitiesClass: Class[T], params: Object*): mutable.ListBuffer[T] = {
    val entityClass = new EntityClass[T](entitiesClass)
    executeQuery(entityClass, sql, params)
  }

  override def create[T >: AnyRef](t: T): Boolean = {
    val tableClass = new TableClass[T](classOf[T])


    null
  }

  override def delete(id: Object): Boolean = ???

  private def execute[T >: AnyRef](tableClass: TableClass[T], sql: String, params: Object*): Boolean = {
    database.withConnection(connection => {
      val preparedStatement: PreparedStatement = connection.prepareStatement(sql)

    })
    false
  }

  private def executeQuery[T >: AnyRef](entityClass: AbstractEntity[T], sql: String, params: Object*): mutable.ListBuffer[T] = {
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

  private def getEntityFieldMap[T >: AnyRef](entityClass: Class[T]): mutable.HashMap[String, Class[_]] = {
    val fieldMap: mutable.HashMap[String, Class[_]] = new mutable.HashMap[String, Class[_]]
    entityClass.getDeclaredFields.foreach(field => {
      field.setAccessible(true)
      val columnAnnotation = field.getAnnotation(classOf[Column])
      if (columnAnnotation != null) {
        fieldMap.put(field.getAnnotation(classOf[Column]).name(), field.getDeclaringClass)
      }
    })
    fieldMap
  }

  private def constructEntity[T >: AnyRef](entityClass: AbstractEntity[T], fieldMap: Map[String, Field], resultSet: ResultSet): T = {
    val fieldMapVal = fieldMap.map(entry => (entry._1, resultSet.getObject(entry._1)))
    val entity: T = entityClass.entityClass.getDeclaredConstructor().newInstance()
    entityClass.entityClass.getDeclaredFields.foreach(field => {
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
