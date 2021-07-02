package simulation.json

import domain.UserInfo
import play.api.libs.json.Json

/**
 * @author steve
 */
object JsonTest {

    def main(args: Array[String]): Unit = {
        println(Json.toJson(UserInfo(1, "steve", 23))(Json.writes[UserInfo]))
    }
}
