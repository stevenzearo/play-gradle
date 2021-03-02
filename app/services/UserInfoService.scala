package services

import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.OverflowStrategy
import akka.stream.actor.ActorSubscriber
import akka.stream.scaladsl.{Sink, Source}
import controllers.user.{SearchRequest, SearchResponse}
import domain.{UserInfo, UserInfoAsyncDAO, UserInfoDAO}
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import scala.concurrent.duration._


/**
 * @author steve
 */
@Singleton
class UserInfoService @Inject()(protected val userInfoDAO: UserInfoAsyncDAO, implicit val system: ActorSystem) {
    private val logger = org.slf4j.LoggerFactory.getLogger(classOf[UserInfoService])

    def get(id: Long)(sink: Sink[UserInfo, Future[Done]]): Unit = {
        Source.actorRef[UserInfo](1, OverflowStrategy.fail)
        val source: Source[UserInfo, ActorRef] = Source.actorRef[UserInfo](1, OverflowStrategy.fail)
        val actorRef: ActorRef = source.throttle(1, 100.millis).to(sink).run()
        userInfoDAO.get(id)(actorRef)
    }

    def search(request: SearchRequest)(sink: Sink[UserInfo, Future[Done]]): Unit = {
        logger.info(s"search userInfo by name: ${request.name}")
        Source.actorRef[UserInfo](1, OverflowStrategy.fail)
        val source: Source[UserInfo, ActorRef] = Source.actorRef[UserInfo](1, OverflowStrategy.fail)
        val actorRef: ActorRef = source.throttle(1, 100.millis).to(sink).run()
        ActorSubscriber[UserInfo](actorRef)
        userInfoDAO.select("select * from user_infos where name like ?", s"%${request.name}%")(actorRef)
    }
}
