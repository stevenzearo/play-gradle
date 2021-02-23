package lib.db.async

import akka.actor.Actor
import domain.UserInfo

/**
 * @author steve
 */
class EntityReceiver[T] extends Actor{
    override def receive: Receive = {
        case userInfo: UserInfo =>
            println(userInfo.toString)
    }
}
