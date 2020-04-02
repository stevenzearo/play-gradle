package services

import java.sql.PreparedStatement

import domain.{UserInfo, UserInfoDAO}
import javax.inject.Inject
import lib.db.DAO
import play.api.db.Database

import scala.collection.mutable

/**
 * @author steve
 */
class ApplicationDatabase @Inject()(protected val dao: UserInfoDAO, db: Database) {
    def userInfos(): mutable.ListBuffer[UserInfo] = {
        val userInfoList = new mutable.ListBuffer[UserInfo]
        db.withConnection { connection =>
            val statement: PreparedStatement = connection.prepareStatement("select * from user_infos")
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

    def userInfos2(): mutable.ListBuffer[UserInfo] = {
        dao.select("select * from user_infos", classOf[UserInfo])
    }

    def getUserInfo(id: Int): UserInfo = {
        dao.get(id).getOrElse(new UserInfo)
    }
}
