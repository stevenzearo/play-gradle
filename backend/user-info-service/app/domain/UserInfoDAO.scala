package domain

import javax.inject.Inject
import lib.db.{DAO, DBUtil}

import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
class UserInfoDAO @Inject()(val dbUtil: DBUtil) extends DAO[UserInfo] {
  val aClass: Class[UserInfo] = classOf[UserInfo]

  override def get[T](id: T): Option[UserInfo] = dbUtil.get(aClass, id)

  override def select(sql: String, params: Object*): ListBuffer[UserInfo] = dbUtil.select(sql, aClass, params.toArray)

  override def create(t: UserInfo): Boolean = dbUtil.create(aClass, t)

  override def delete[T](id: T): Boolean = dbUtil.delete(aClass, id)
}