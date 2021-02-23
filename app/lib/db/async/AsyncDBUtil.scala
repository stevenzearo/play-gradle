package lib.db.async

import akka.actor.ActorRef


/**
 * @author steve
 */
trait AsyncDBUtil {
    def get[T >: Null, A](aClass: Class[T], id: A)(ref: ActorRef): Unit

    def select[T](sql: String, entitiesClass: Class[T], params: Array[Object])(ref: ActorRef): Unit

    def create[T](aClass: Class[T], t: T)(ref: ActorRef): Unit

    def delete[T, A](aClass: Class[T], id: A)(ref: ActorRef): Unit
}
