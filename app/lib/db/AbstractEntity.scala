package lib.db

import java.lang.reflect.Field

import scala.collection.mutable

/**
 * @author steve
 */
trait AbstractEntity[T] {
    val entityClass: Class[T]

    def getFieldMap: Map[String, Field] = {
        val fieldMap: mutable.Map[String, Field] = new mutable.HashMap[String, Field]
        entityClass.getDeclaredFields.foreach(field => {
            field.setAccessible(true)
            val columnAnnotation = field.getAnnotation(classOf[Column])
            if (columnAnnotation != null) fieldMap.put(columnAnnotation.name(), field)
        })
        fieldMap.toMap
    }
}

object AbstractEntity {
    def validateEntityAnnotation[T](entityClass: Class[T]): Unit = {
        val entityAnnotation = entityClass.getAnnotation(classOf[Entity])
        if (entityAnnotation == null) throw new Exception("table class must have @Entity annotation")
    }
}