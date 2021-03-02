package controllers

import akka.stream.scaladsl.Sink
import controllers.user.{SearchRequest, SearchResponse}
import domain.UserInfo
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.UserInfoService

import scala.collection.mutable

/**
 * @author steve
 */
@Singleton
class UserInfoWebService @Inject()(protected val userInfoService: UserInfoService, protected val cc: ControllerComponents) extends AbstractController(cc) {
    private val logger = org.slf4j.LoggerFactory.getLogger(classOf[UserInfoWebService])

    def get(id: String): Action[AnyContent] = Action(parse.json) {
        logger.info(s"get user info $id")
        val userInfos: mutable.Buffer[UserInfo] = mutable.Buffer.empty[UserInfo]
        userInfoService.get(id.toLong)(Sink.foreach[UserInfo](t => {
            userInfos.appended(t)
        }))
        val value = Json.toJson[UserInfo](userInfos.head)(Json.writes[UserInfo])
        Ok(value)
    }

    def getByName: Action[JsValue] = Action(parse.json) {
        request: Request[JsValue] => {
            val searchRequest: SearchRequest = Json.fromJson(request.body)(Json.reads[SearchRequest]).get
            logger.info(s"get user info by name: ${searchRequest.name}")
            val userInfos: mutable.Buffer[UserInfo] = mutable.Buffer.empty[UserInfo]
            userInfoService.search(searchRequest)(Sink.foreach[UserInfo](t => {
                userInfos.appended(t)
            }))
            val response = Json.toJson(new SearchResponse(userInfos.toArray))(Json.writes[SearchResponse])
            logger.info(response.toString())
            Ok(response)
        }
    }
}
