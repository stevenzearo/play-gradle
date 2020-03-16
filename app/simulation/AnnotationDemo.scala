package simulation

import domain.UserInfo
import lib.db.{Column, Entity, PrimaryKey, Table}

import util._

/**
 * @author steve
 */
object AnnotationDemo {

    @Entity
    @Table(name = "employees")
    class Employee {
        @PrimaryKey
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


        val userInfo: UserInfo = new UserInfo
        userInfo.id = "id-0001"
        userInfo.name = "user-0001"
        userInfo.age = 19

        println("********************")
        val tableClass = TableClass(classOf[UserInfo])
        val tableName = tableClass.tableName
        val fieldValMap = tableClass.fieldMap.map(entry => entry._1 -> entry._2.get(userInfo))
        if (fieldValMap.isEmpty) throw new Exception("table columns can not be empty")
        val columnsStr: String = fieldValMap.keys.reduce((k1, k2) => k1 + ", " + k2)
        val paramsStr = fieldValMap.values.map(v => s"\'$v\'").reduce((v1, v2) => s"$v1, $v2")
        val sql: String = s"insert into $tableName ($columnsStr) values ($paramsStr)"
        println(sql)
    }
}
