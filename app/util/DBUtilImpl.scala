package util

import java.sql.{Connection, PreparedStatement}

import javax.inject.Inject
import play.api.db.Database

import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
// todo
class DBUtilImpl @Inject() (implicit database: Database) extends DBUtil {

  override def get[T](id: Object): T = ???
  override def select[T](sql: String)(objects: Object*)(entitiesClass: Class[T]): BufferedIterator[T] = ???
  override def create[T](t: T): Boolean = ???
  override def delete(id: Object): Boolean = ???

  private def execute[T](block: Connection => T): Boolean = {
    false
  }

  private def executeQuery(sql: String, params: Object*): Unit = {
    database.withConnection(connection => {
      val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
      val paramPairs: ListBuffer[(Int, Object)] = ListBuffer.apply()
      for (elem <- params; index <- 1 to params.length) {
        paramPairs.append((index, elem))
      }
      paramPairs.foreach(paramPair => preparedStatement.setObject(paramPair._1, paramPair._2))
      preparedStatement.execute()
      val resultSet = preparedStatement.getResultSet
      while (resultSet.next()) {
        // todo get class information and mapp result
      }
    })
  }

  private def executeUpdate(): Unit = {

  }
}
