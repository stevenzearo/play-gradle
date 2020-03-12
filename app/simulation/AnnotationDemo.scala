package simulation

import domain.UserInfo
import util._

/**
 * @author steve
 */
object AnnotationDemo {

    @Entity
    @Table(name = "employees")
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


        println("********************")
        val tableClass = new TableClass(classOf[Employee])
        val tableName = tableClass.tableName
        val fieldValMap = tableClass.fieldMap.map(entry => entry._1 -> entry._2.get(employee))
        if (fieldValMap.isEmpty) throw new Exception("table columns can not be empty")
        val columnsStr: String = fieldValMap.keys.reduce((k1, k2) => k1 + ", " + k2)
        val paramsStr = fieldValMap.values.reduce((v1, v2) => v1 + ", " + v2)
        var sql: String = s"insert into $tableName ($columnsStr) values ($paramsStr)"
        println(sql)
    }
}
