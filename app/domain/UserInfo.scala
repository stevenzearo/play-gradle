package domain

import javax.inject.Inject

/**
 * @author steve
 */
class UserInfo @Inject()() {
  var id: Int = _
  var name: String = _
  var age: Int = _

  override def toString: String = s"UserInfo: {id:$id, name:$name, age:$age}"
}
