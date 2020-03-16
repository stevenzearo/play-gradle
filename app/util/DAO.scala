package util

import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
trait DAO[T >: Null] {
    def get(id: Object): Option[T]

    def select(sql: String, params: Object*): ListBuffer[T]

    def create(t: T): Boolean

    def delete(id: Object): Boolean
}
