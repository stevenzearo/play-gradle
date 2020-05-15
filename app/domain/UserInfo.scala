package domain

import lib.db.{Column, Entity, PrimaryKey, Table}

/**
 * @author steve
 */
@Entity
@Table(name = "user_infos")
class UserInfo() {
    @PrimaryKey
    @Column(name = "id") var id: Long = _
    @Column(name = "name") var name: String = _
    @Column(name = "age") var age: Int = _

    def this(id: Long, name: String, age: Int) {
        this()
        this.id = id
        this.name = name
        this.age = age
    }

    override def toString: String = s"UserInfo: {id:$id, name:$name, age:$age}"
}

object UserInfo {
    def apply(): UserInfo = new UserInfo()

    def apply(id: Long, name: String, age: Int): UserInfo = new UserInfo(id, name, age)

    def unapply(userInfo: UserInfo): Option[(Long, String, Int)] = {
        if (userInfo == null) return None
        Some(userInfo.id, userInfo.name, userInfo.age)
    }
}