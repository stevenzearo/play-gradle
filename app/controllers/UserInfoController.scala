package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsNumber, JsString, JsValue}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.UserInfoService

/**
 * @author steve
 */
@Singleton
class UserInfoController@Inject() (protected val userInfoService: UserInfoService, protected val cc: ControllerComponents) extends AbstractController(cc) {
    def get = Action(parse.json) { request: Request[JsValue] =>
        val strings = request.path.split("/")
        val id = strings(strings.length-1).toLong
        request match {
            case request: JsString => Ok(views.html.user(userInfoService.get(id))(request.body.toString()))
            case request: _ => Ok(views.html.user(null)(request.toString()))
        }

    }
}
