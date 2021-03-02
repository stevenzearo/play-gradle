package domain

import akka.actor.ActorRef
import javax.inject.Inject
import lib.db.async.AsyncDBUtil

/**
 * @author steve
 */
class UserInfoAsyncDAO @Inject()(val dbUtil: AsyncDBUtil) {
    val aClass: Class[UserInfo] = classOf[UserInfo]

    def get[T](id: T)(ref: ActorRef): Unit = dbUtil.get(aClass, id)(ref)

    def select(sql: String, params: Object*)(ref: ActorRef): Unit = dbUtil.select(sql, aClass, params.toArray)(ref)

    def create(t: UserInfo)(ref: ActorRef): Unit = dbUtil.create(aClass, t)(ref)

    def delete[T](id: T)(ref: ActorRef): Unit = dbUtil.delete(aClass, id)(ref)
}