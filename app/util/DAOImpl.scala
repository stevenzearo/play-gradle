package util

import javax.inject.Inject

import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
class DAOImpl[T >: Null](@Inject val dbUtil: DBUtil) extends DAO[T] {
    val aClass: Class[T] = classOf[T]

    override def get(id: Object): Option[T] = dbUtil.get(aClass, id)

    override def select(sql: String, params: Object*): ListBuffer[T] = dbUtil.select(sql, aClass, params)

    override def create(t: T): Boolean = dbUtil.create(aClass, t)

    override def delete(id: Object): Boolean = dbUtil.delete(aClass, id)
}
