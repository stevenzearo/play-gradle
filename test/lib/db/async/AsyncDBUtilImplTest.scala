package lib.db.async

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, Sink}
import demo.UnitSpec
import domain.UserInfo
import lib.db.DBUtil
import org.junit.jupiter.api.Test

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}

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
        /*val source = asyncDBUtil.select("select * from user_infos", classOf[UserInfo], null)
        val graph = source.via(Flow[UserInfo].map(_ => 1)).toMat(Sink.fold(0)(_ + _))(Keep.right)
        val eventualInt = graph.run()
        val value1 = Await.result(eventualInt, 5.second)
        //        val eventualDone = source.runForeach(println)
        //        eventualDone.onComplete(_ => system.terminate())
        println(value1)*/
    }

    "Search UserInfo" must {
        val userInfos = dBUtil.select("select * from user_infos", classOf[UserInfo], null)
        "userInfo result" in {
            userInfos.size must not be 0
        }
    }
/*
    "Async DBUtil" must {
        val source = asyncDBUtil.select("select * from user_infos", classOf[UserInfo], null)
        "source" in {
            val eventualDone = source.runForeach(println)
            eventualDone.onComplete(_ => system.terminate())
            Thread.sleep(3000L)
        }
    }*/
}
