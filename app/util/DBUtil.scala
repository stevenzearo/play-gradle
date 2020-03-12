package util

import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
trait DBUtil {
    def get[T >: AnyRef](id: Object): Option[T]

    def select[T >: AnyRef](sql: String, entitiesClass: Class[T], params: Object*): ListBuffer[T]

    def create[T >: AnyRef](t: T): Boolean

    def delete(id: Object): Boolean
}
