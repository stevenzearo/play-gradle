package controllers

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
    def get(id: String): Action[AnyContent] = Action(parse.json) {
        val value = Json.toJson[UserInfo](userInfoService.get(id.toLong))(Json.writes[UserInfo])
        Ok(value)
    }

    def getByName: Action[JsValue] = Action(parse.json) {
        request: Request[JsValue] => {
            val getRequest: GetByNameRequest = Json.fromJson(request.body)(Json.reads[GetByNameRequest]).get
            val response = Json.toJson(getRequest)(Json.writes[GetByNameRequest])
            Ok(response)
        }
    }
}
