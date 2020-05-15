package lib.db

import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
trait DBUtil {
    def get[T >: Null, A](aClass: Class[T], id: A): Option[T]

    def select[T](sql: String, entitiesClass: Class[T], params: Array[Object]): ListBuffer[T]

    def create[T](aClass: Class[T], t: T): Boolean

    def delete[T, A](aClass: Class[T], id: A): Boolean
}
