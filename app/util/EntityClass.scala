package util

/**
 * @author steve
 */
class EntityClass[T >: AnyRef](val entityClass: Class[T]) extends AbstractEntity[T] {
}
object EntityClass {
  def apply[T >: AnyRef](entityClass: Class[T]): EntityClass[T] = {
    val entityClass = new EntityClass[T](entityClass)
    entityClass.validateEntityAnnotation()
    entityClass
  }
}
