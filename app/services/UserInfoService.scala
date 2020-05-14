package services

import domain.{UserInfo, UserInfoDAO}
import javax.inject.{Inject, Singleton}

/**
 * @author steve
 */
@Singleton
class UserInfoService @Inject() (protected val userInfoDAO: UserInfoDAO) {
    def get(id: Long): UserInfo = {
        val userInfoOptional = userInfoDAO.get(id)
        if (userInfoOptional.isDefined) userInfoOptional.get else null
    }

    def search(): List[UserInfo] = {
        userInfoDAO.select("select * from user_infos").toList
    }
}
