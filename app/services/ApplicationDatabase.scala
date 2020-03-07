package services

import domain.UserInfo
import javax.inject.Inject
import play.api.db.Database

import scala.collection.mutable

/**
 * @author steve
 */
class ApplicationDatabase @Inject()(implicit db: Database) {
  def userInfos(): mutable.ListBuffer[UserInfo] = {
    val userInfoList = new mutable.ListBuffer[UserInfo]
    db.getConnection()
    db.withConnection { connection =>
      val statement = connection.prepareStatement("select * from user_infos")
      val map = "user_infos" -> [UserInfo]
      statement.execute()
      val resultSet = statement.getResultSet
      while (resultSet.next()) {
        val info = new UserInfo
        info.id = resultSet.getInt("id")
        info.name = resultSet.getString("name")
        info.age = resultSet.getInt("age")
        userInfoList.append(info)
      }
      resultSet.close()
      statement.close()
      connection.close()
    }
    userInfoList
  }
}
