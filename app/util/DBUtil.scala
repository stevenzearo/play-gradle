package util

import scala.collection.mutable.ListBuffer

/**
 * @author steve
 */
trait DBUtil {
//  def get[T](id: Object): T
  def select[T](sql: String, entitiesClass: Class[T], params: Object*): ListBuffer[T]
//  def create[T](t: T): Boolean
//  def delete(id: Object): Boolean
}
