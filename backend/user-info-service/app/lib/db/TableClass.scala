package lib.db

import java.lang.reflect.Field

/**
 * @author steve
 */
class TableClass[T] private(val entityClass: Class[T]) extends AbstractEntity[T] {
    val tableName: String = getTableName
    val primaryKeyColumn: String = getPrimaryKey
    val fieldMap: Map[String, Field] = getFieldMap

    private def getPrimaryKey: String = {
        val primaryKeyField = entityClass.getDeclaredFields.filter(field => field.getAnnotation(classOf[PrimaryKey]) != null)(0)
        primaryKeyField.getAnnotation(classOf[Column]).name()
    }

    private def getTableName: String = {
        val tableAnnotation = entityClass.getAnnotation(classOf[Table])
        val tableName: String = tableAnnotation.name()
        if (tableName == null || tableName.strip().isBlank) throw new Exception("table name can not be null or blank")
        tableName
    }
}

object TableClass {
    def apply[T](entityClass: Class[T]): TableClass[T] = {
        AbstractEntity.validateEntityAnnotation(entityClass)
        validateAnnotation(entityClass)
        new TableClass(entityClass)
    }

    private def validateAnnotation[T](entityClass: Class[T]): Unit = {
        val tableAnnotation = entityClass.getAnnotation(classOf[Table])
        if (tableAnnotation == null) throw new Exception("table class must have @Table annotation")
        val primaryKeyNum: Int = entityClass.getDeclaredFields.count(field => field.getAnnotation(classOf[PrimaryKey]) != null)
        if (primaryKeyNum != 1) throw new Exception("table class must have and only have one primary key")
    }
}