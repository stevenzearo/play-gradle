package lib.db.async

import akka.actor.{Actor, ActorRef}

/**
 * @author steve
 */
class EntitySenderActor[T](ref: ActorRef) extends Actor{
    override def receive: Receive = {
        case t: T =>
            ref ! t
        case _ =>
            ref ! new Exception("unknown")
    }
}
