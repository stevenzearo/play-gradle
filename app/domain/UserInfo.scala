package domain

import simulation.{Entity, Table}
import util.{Column, Entity, TableClass}

/**
 * @author steve
 */
@Entity
@TableClass(name = "user_infos")
class UserInfo() {
  @Column(name = "id") var id: String = _
  @Column(name = "name") var name: String = _
  @Column(name = "age") var age: Int = _

  override def toString: String = s"UserInfo: {id:$id, name:$name, age:$age}"
}
