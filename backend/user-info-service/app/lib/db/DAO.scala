package lib.db

import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
trait DAO[T >: Null] {
    def get[A >: Null](id: A): Option[T]

    def select(sql: String, params: Object*): ListBuffer[T]

    def create(t: T): Boolean

    def delete[A >: Null](id: A): Boolean
}
