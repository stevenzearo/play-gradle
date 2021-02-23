package lib.db.async

import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Sink, Source}
import demo.UnitSpec
import domain.UserInfo
import lib.db.DBUtil
import org.junit.jupiter.api.Test

import scala.concurrent.ExecutionContextExecutor

/**
 * @author steve
 */
class AsyncDBUtilImplTest extends UnitSpec {
    val dBUtil: DBUtil = app.injector.instanceOf(classOf[DBUtil])
    val asyncDBUtil: AsyncDBUtil = app.injector.instanceOf(classOf[AsyncDBUtil])
    implicit val system: ActorSystem = app.actorSystem
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    @Test
    override def registerTest(): Unit = {
        val source = Source.actorRef[UserInfo](100, OverflowStrategy.dropTail)
        val actorRef = source.to(Sink.foreach(println)).run()
        asyncDBUtil.select("select * from user_infos", classOf[UserInfo], null)(actorRef)
    }

    "Search UserInfo" must {
        val userInfos = dBUtil.select("select * from user_infos", classOf[UserInfo], null)
        "userInfo result" in {
            userInfos.size must not be 0
        }
    }
}