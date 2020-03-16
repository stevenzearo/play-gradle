package domain

import javax.inject.Inject
import lib.db.{DAO, DBUtil}

import util.DBUtil
import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
class UserInfoDAO @Inject()(val dbUtil: DBUtil) extends DAO[UserInfo] {
    val aClass: Class[UserInfo] = classOf[UserInfo]

    override def get(id: Object): Option[UserInfo] = dbUtil.get(aClass, id)

    override def select(sql: String, params: Object*): ListBuffer[UserInfo] = dbUtil.select(sql, aClass, params)

    override def create(t: UserInfo): Boolean = dbUtil.create(aClass, t)

    override def delete(id: Object): Boolean = dbUtil.delete(aClass, id)

}