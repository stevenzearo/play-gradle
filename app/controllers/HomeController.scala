package controllers

import javax.inject._
import play.api.mvc._
import play.twirl.api.Html
import services.ApplicationDatabase

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, database: ApplicationDatabase)(implicit assetsFinder: AssetsFinder)
    extends AbstractController(cc) {

    /**
     * Create an Action to render an HTML page with a welcome message.
     * The configuration in the `routes` file means that this method
     * will be called when the application receives a `GET` request with
     * a path of `/`.
     */
    def index: Action[AnyContent] = Action {
        Ok(views.html.index("Your new application is ready."))
    }

    def testHtml: Action[AnyContent] = Action {
        val userInfoStr: String = database.userInfos2().map(userInfo => s"<p>$userInfo</p>").reduce(_ + _)
        val userInfoStr2 = database.getUserInfo("id-0001").toString
        Ok(views.html.testHtml("steve")(Html(s"<div>$userInfoStr<p>$userInfoStr2</p></div>")))
    }
}
