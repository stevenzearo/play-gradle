package simulation.reflection

/**
 * @author steve
 */
object ReflectionDemo {

    class Student {
        var name: String = _
        var id: String = _
        var age: Int = _

        override def toString: String = s"Student: {id: $id, name: $name, age: $age}"
    }

    def main(args: Array[String]): Unit = {
        val aClass = classOf[Student]
        aClass.getFields.foreach(println)
    }
}
