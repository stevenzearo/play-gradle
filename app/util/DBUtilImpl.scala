package util

import java.sql.{Connection, PreparedStatement, ResultSet}

import domain.UserInfo
import javax.inject.Inject
import play.api.db.Database
import simulation.{Column, Entity}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
// todo
class DBUtilImpl @Inject()(implicit database: Database) extends DBUtil {


  //  override def get[T](id: Object): T = ???
  override def select[T](sql: String, entitiesClass: Class[T], params: Object*): mutable.ListBuffer[T] = {
    executeQuery(entitiesClass, sql, params)
  }

  //  override def create[T](t: T): Boolean = ???
  //  override def delete(id: Object): Boolean = ???

  private def execute[T](block: Connection => T): Boolean = {
    false
  }

  private def executeQuery[T](entityClass: Class[T], sql: String, params: Object*): mutable.ListBuffer[T] = {
    database.withConnection(connection => {
      val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
      val paramPairs: ListBuffer[(Int, Object)] = ListBuffer.apply()
      if (params != null && params.nonEmpty) {
        for (elem <- params; index <- 1 until params.length) {
          paramPairs.append((index, elem))
        }
      }
      paramPairs.foreach(paramPair => preparedStatement.setObject(paramPair._1, paramPair._2))
      preparedStatement.execute()
      val resultSet: ResultSet = preparedStatement.getResultSet

      val fieldMap: mutable.Map[String, Class[_]] = getEntityFieldMap(entityClass)
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

  private def getEntityFieldMap[T](entityClass: Class[T]): mutable.HashMap[String, Class[_]] = {
    val fieldMap: mutable.HashMap[String, Class[_]] = new mutable.HashMap[String, Class[_]]
    val entity = entityClass.getAnnotation(classOf[Entity])
    if (entity == null) throw new Error("entity class must declared with @Entity annotation")
    entityClass.getDeclaredFields.foreach(field => {
      field.setAccessible(true)
      val columnAnnotation = field.getAnnotation(classOf[Column])
      if (columnAnnotation != null) {
        fieldMap.put(field.getAnnotation(classOf[Column]).name(), field.getDeclaringClass)
      }
    })
    fieldMap
  }

  private def constructEntity[T](entityClass: Class[T], fieldMap: mutable.Map[String, Class[_]], resultSet: ResultSet): T = {
    val fieldMapVal = fieldMap.map(entry => (entry._1, resultSet.getObject(entry._1)))
    val entity: T = entityClass.getDeclaredConstructor().newInstance()
    entityClass.getDeclaredFields.foreach(field => {
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
}
