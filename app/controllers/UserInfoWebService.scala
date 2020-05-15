package controllers

import controllers.user.{SearchRequest, SearchResponse}
import domain.UserInfo
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.UserInfoService

/**
 * @author steve
 */
@Singleton
class UserInfoWebService @Inject()(protected val userInfoService: UserInfoService, protected val cc: ControllerComponents) extends AbstractController(cc) {
    private val logger = org.slf4j.LoggerFactory.getLogger(classOf[UserInfoWebService])

    def get(id: String): Action[AnyContent] = Action(parse.json) {
        logger.info(s"get user info $id")
        val value = Json.toJson[UserInfo](userInfoService.get(id.toLong))(Json.writes[UserInfo])
        Ok(value)
    }

    def getByName: Action[JsValue] = Action(parse.json) {
        request: Request[JsValue] => {
            val searchRequest: SearchRequest = Json.fromJson(request.body)(Json.reads[SearchRequest]).get
            val searchResponse = userInfoService.search(searchRequest)
            logger.info(s"get user info by name: ${searchRequest.name}")
            val response = Json.toJson(searchResponse)(Json.writes[SearchResponse])
            logger.info(response.toString())
            Ok(response)
        }
    }
}
