package services

import java.sql.PreparedStatement

import domain.UserInfo
import javax.inject.Inject
import play.api.db.Database
import util.DBUtil

import scala.collection.mutable

/**
 * @author steve
 */
class ApplicationDatabase @Inject()(protected val dbUtil: DBUtil, db: Database) {
    def userInfos(): mutable.ListBuffer[UserInfo] = {
        val userInfoList = new mutable.ListBuffer[UserInfo]
        db.withConnection { connection =>
            val statement: PreparedStatement = connection.prepareStatement("select * from user_infos")
            statement.execute()
            val resultSet = statement.getResultSet
            while (resultSet.next()) {
                val info = new UserInfo
                info.id = resultSet.getString("id")
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

    def userInfos2(): mutable.ListBuffer[UserInfo] = {
        dbUtil.select("select * from user_infos", classOf[UserInfo])
    }

    def getUserInfo(id: String): UserInfo = {
        dbUtil.get(classOf[UserInfo], id).getOrElse(new UserInfo)
    }
}
