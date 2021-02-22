package demo

import org.junit.jupiter.api.Test
import org.scalatest.{MustMatchers, OptionValues}
import play.api.Logger

import scala.collection.mutable

/**
 * @author steve
 */
@Test
class ScalaTestDemo extends OptionValues with MustMatchers {

    @Test
    def test(): Unit = {
        println("hello, world!")
        val stack = new mutable.Stack[Int]
        stack.push(1)
        stack.push(2)
        stack.pop() mustBe 2
        stack.pop() mustBe 1
        println(">>>>>>>>>>>>>")
        Logger.logger.warn("test>>>>>>>>>>>")

        val stack2 = new mutable.Stack[Int]
        a[NoSuchElementException] must be thrownBy {
            stack2.pop()
        }
    }
}
