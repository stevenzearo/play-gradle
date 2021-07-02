package lib.db.async

import scala.concurrent.Future
import scala.reflect.ClassTag


/**
 * @author steve
 */
trait AsyncDBUtil {
    def get[T: ClassTag, A](aClass: Class[T], id: A): Future[Option[T]]

    def select[T: ClassTag](sql: String, entitiesClass: Class[T], params: Array[Object]): Future[Array[T]]

    def create[T](aClass: Class[T], t: T): Future[Boolean]

    def delete[T, A](aClass: Class[T], id: A): Future[Boolean]
}
