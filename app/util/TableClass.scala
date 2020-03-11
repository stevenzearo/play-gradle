package util

import scala.collection.immutable.HashMap

/**
 * @author steve
 */
class TableClass[T](val entityClass: Class[T]) {
  val tableName: String = getTableName
  val primaryKeyColumn: String = getPrimaryKey
  val fieldMap: HashMap[String, Class[_]] = getFieldMap
  val fieldValMap: HashMap[String, Object] = getFieldValMap

  def this(entityClass: Class[T]) {
    this(entityClass)
    val entityAnnotation = entityClass.getAnnotation(classOf[Entity])
    if (entityAnnotation == null) throw new Exception("table class must have @Entity annotation")
    val tableAnnotation = entityClass.getAnnotation(classOf[Table])
    if (tableAnnotation == null) throw new Exception("table class must have @Table annotation")
    val primaryKeyNum: Int = entityClass.getDeclaredFields.count(field => field.getAnnotation(classOf[PrimaryKey]) != null)
    if (primaryKeyNum != 1) throw new Exception("table class must have and only have one primary key")
  }
  private def getPrimaryKey: String = {
    val primaryKeyField = entityClass.getDeclaredFields.filter(field => field.getAnnotation(classOf[PrimaryKey]) != null)(0)
    primaryKeyField.getAnnotation(classOf[Column]).name()
  }

  private def getFieldMap: HashMap[String, Class[_]] = {
    null
  }

  private def getFieldValMap: HashMap[String, Object] = {
    null
  }

  private def getTableName: String = {
    val tableAnnotation = entityClass.getAnnotation(classOf[Table])
    val tableName: String = tableAnnotation.name()
    if (tableName == null || tableName.strip().isBlank) throw new Exception("table name can not be null or blank")
    tableName
  }
}
