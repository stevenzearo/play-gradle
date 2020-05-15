package services

import controllers.user.{SearchRequest, SearchResponse}
import domain.{UserInfo, UserInfoDAO}
import javax.inject.{Inject, Singleton}
import lib.db.DBUtilImpl

/**
 * @author steve
 */
@Singleton
class UserInfoService @Inject() (protected val userInfoDAO: UserInfoDAO) {
    private val logger = org.slf4j.LoggerFactory.getLogger(classOf[UserInfoService])

    def get(id: Long): UserInfo = {
        val userInfoOptional = userInfoDAO.get(id)
        if (userInfoOptional.isDefined) userInfoOptional.get else null
    }

    def search(request: SearchRequest): SearchResponse = {
        logger.info(s"search userInfo by name: ${request.name}")
        val userInfos = userInfoDAO.select("select * from user_infos where name like ?", s"%${request.name}%").toList
        new SearchResponse(userInfos)
    }
}
