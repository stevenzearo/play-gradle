package util

/**
 * @author steve
 */
class EntityClass[T](val entityClass: Class[T]) extends AbstractEntity[T] {
}

object EntityClass {
    def apply[T](entityClass: Class[T]): EntityClass[T] = {
//        validateEntityAnnotation()
//        entityClass
        entityClass
        apply(entityClass)
    }
}
