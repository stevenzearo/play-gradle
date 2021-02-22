package lib.db.async

import akka.NotUsed
import akka.stream.scaladsl.Source

/**
 * @author steve
 */
trait AsyncDBUtil {
    def get[T >: Null, A](aClass: Class[T], id: A): Source[T, NotUsed]

    def select[T](sql: String, entitiesClass: Class[T], params: Array[Object]): Source[T, NotUsed]

    def create[T](aClass: Class[T], t: T): Source[Boolean, NotUsed]

    def delete[T, A](aClass: Class[T], id: A): Source[Boolean, NotUsed]
}
