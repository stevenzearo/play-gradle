package demo

import org.junit.jupiter.api.Test
import play.api.Logger

import scala.collection.mutable

/**
 * @author steve
 */
class ScalaTestDemo2 extends UnitSpec {

    @Test
    override def registerTest(): Unit = {
        Logger.logger.info("testing...")
    }

    "A must test" must {
        println("hello, world!")
        val stack = new mutable.Stack[Int]
        stack.push(1)
        stack.push(2)
        stack.pop() mustBe 2
        stack.pop() mustBe 1
        println(">>>>>>>>>>>>>")
        Logger.logger.warn("test>>>>>>>>>>>")
    }

    "A should Test" should {
        println("should do something")
        "A Test" can {
            println("can do something")
        }
    }

    "Pop values in last-in-first-out order" must {
        val stack = new mutable.Stack[Int]
        stack.push(1)
        stack.push(2)
        stack.pop() mustBe 2
        stack.pop() mustBe 1
        Logger.logger.warn(">>>>>>>>>>>stack pop")
    }

    "throw NoSuchElementException if an empty stack is popped" must {
        val emptyStack = new mutable.Stack[Int]
        a[NoSuchElementException] must be thrownBy {
            emptyStack.pop()
        }
        Logger.logger.warn("empty stack")
    }

    "A Stack" must {
        Logger.logger.warn(">>>>>>>>>>>word spec")
        "and" must {
            Logger.logger.warn(">>>>>>>>>>>must inner test")
        }
        "inner test" in {
            Logger.logger.warn(">>>>>>>>>>>in inner test")
        }
    }

    "ignore" ignore {
        Logger.logger.warn(">>>>>>>>>>>word ignored")

        "but" must {
            Logger.logger.warn(">>>>>>>>>>>must in ignored")
        }
    }

}
