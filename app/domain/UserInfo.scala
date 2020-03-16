package domain

import lib.db.{Column, Entity, PrimaryKey, Table}

import util.{Entity, PrimaryKey, Table}

/**
 * @author steve
 */
@Entity
@Table(name = "user_infos")
class UserInfo() {
    @PrimaryKey
    @Column(name = "id") var id: String = _
    @Column(name = "name") var name: String = _
    @Column(name = "age") var age: Int = _

    override def toString: String = s"UserInfo: {id:$id, name:$name, age:$age}"
}
