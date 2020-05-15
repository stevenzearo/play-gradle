package simulation.param

/**
 * @author steve
 */
object ParamTest {
    def test(params: Object*): Unit = {
        println(params)
    }

    def main(args: Array[String]): Unit = {
        test("steve")
    }
}
