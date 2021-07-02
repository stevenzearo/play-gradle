package services

import domain.UserInfo
import lib.db.async.{AsyncDAO, AsyncDBUtil}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

/**
 * @author steve
 */
@Singleton
class UserInfoService @Inject()(val dbUtil: AsyncDBUtil) extends AsyncDAO[UserInfo] {
    val aClass: Class[UserInfo] = classOf[UserInfo]

    override def get[A >: Null](id: A): Future[Option[UserInfo]] = dbUtil.get[UserInfo, A](aClass, id)

    override def select(sql: String, params: Object*): Future[Array[UserInfo]] = dbUtil.select[UserInfo](sql, aClass, params.toArray)

    override def create(t: UserInfo): Future[Boolean] = dbUtil.create[UserInfo](aClass, t)

    override def delete[A >: Null](id: A): Future[Boolean] = dbUtil.delete[UserInfo, A](aClass, id)
}