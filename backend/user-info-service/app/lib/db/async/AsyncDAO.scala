package lib.db.async

import scala.concurrent.Future

/**
 * @author steve
 */
trait AsyncDAO[T >: Null] {
    def get[A >: Null](id: A): Future[Option[T]]

    def select(sql: String, params: Object*): Future[Array[T]]

    def create(t: T): Future[Boolean]

    def delete[A >: Null](id: A): Future[Boolean]
}
