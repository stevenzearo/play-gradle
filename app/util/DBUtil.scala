package util

/**
 * @author steve
 */
trait DBUtil {
  def get[T](id: Object): T
  def select[T](sql: String)(objects: Object*)(entitiesClass: Class[T]): BufferedIterator[T]
  def create[T](t: T): Boolean
  def delete(id: Object): Boolean
}
