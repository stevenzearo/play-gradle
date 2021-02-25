package lib.db.async

import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Sink, Source}
import akka.testkit.TestProbe
import demo.UnitSpec
import domain.UserInfo
import lib.db.DBUtil
import org.junit.jupiter.api.Test

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

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
        val probe = TestProbe()
        val source = Source.actorRef[UserInfo](1, OverflowStrategy.fail)
        val actorRef = source.throttle(1, 100.millis).to(Sink.actorRef(probe.ref, onCompleteMessage = None)).run()
        asyncDBUtil.select("select * from user_infos", classOf[UserInfo], null)(actorRef)
        probe.expectMsgAllClassOf(500.millis, classOf[UserInfo])
    }

    @Test
    def testSync(): Unit = {
        val userInfos = dBUtil.select("select * from user_infos", classOf[UserInfo], null)
        userInfos.size must not be 0
    }
}