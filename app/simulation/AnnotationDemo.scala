package simulation

import domain.UserInfo
import util.{Column, Entity, Repository, TableClass}

import scala.annotation.{ClassfileAnnotation, StaticAnnotation}
import scala.annotation.meta.field
import scala.beans.BeanProperty

/**
 * @author steve
 */
object AnnotationDemo {
  @Entity
  @TableClass(name = "employees")
  class Employee {
    @Column(name = "id") var id: String = _
  }

  def main(args: Array[String]): Unit = {
    val employee = new Employee
        employee.id = "id-0001"
    val aClass = classOf[UserInfo]
    val repository = aClass.getAnnotation(classOf[Repository])
    println(repository)
    val annotations = aClass.getAnnotations
    println("-------")
    annotations.foreach(println)
    println("-------")
    aClass.getDeclaredFields.foreach(field => field.getAnnotations.foreach(println))
  }
}
