package lib.db

/**
 * @author steve
 */
class EntityClass[T] private(val entityClass: Class[T]) extends AbstractEntity[T] {
}

object EntityClass {
    def apply[T](entityClass: Class[T]): EntityClass[T] = {
        AbstractEntity.validateEntityAnnotation(entityClass)
        new EntityClass[T](entityClass)
    }
}