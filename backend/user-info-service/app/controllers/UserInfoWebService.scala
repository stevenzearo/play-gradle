package controllers

import controllers.user.{SearchRequest, SearchResponse}
import domain.UserInfo
import models.ServiceExecutionContext
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.UserInfoService

import javax.inject.{Inject, Singleton}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * @author steve
 */
@Singleton
class UserInfoWebService @Inject()(protected val userInfoService: UserInfoService, protected val cc: ControllerComponents, implicit val ec: ServiceExecutionContext) extends AbstractController(cc) {
    private val logger = org.slf4j.LoggerFactory.getLogger(classOf[UserInfoWebService])

    def get(id: String): Action[AnyContent] = Action(parse.json) {
        logger.info(s"get user info $id")
        var value = Json.parse("{}")
        val res = Await.result(userInfoService.get(id.toLong), 2 seconds)
        if (res.nonEmpty) value = Json.toJson[UserInfo](res.get)(Json.writes[UserInfo])
        Ok(value)
    }

    def getByName: Action[JsValue] = Action(parse.json) {
        request: Request[JsValue] => {
            val searchRequest: SearchRequest = Json.fromJson(request.body)(Json.reads[SearchRequest]).get
            logger.info(s"get user info by name: ${searchRequest.name}")
            var userInfos: Array[UserInfo] = Array.empty
            userInfoService.select("select * from user_infos where name like ?", searchRequest.name)
                .onComplete(result => {
                    if (result.isSuccess) {
                        val res = result.get
                        userInfos = res
                    }
                })
            val response = Json.toJson(new SearchResponse(userInfos.toArray))(Json.writes[SearchResponse])
            logger.info(response.toString())
            Ok(response)
        }
    }
}
