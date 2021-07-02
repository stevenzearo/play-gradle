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
        Logger.logger.info("hello, world!")
        val stack = new mutable.Stack[Int]
        stack.push(1)
        stack.push(2)
        stack.pop() mustBe 2
        stack.pop() mustBe 1
        Logger.logger.info(">>>>>>>>>>>>>")
        Logger.logger.warn("test>>>>>>>>>>>")
    }

    "Pop values in last-in-first-out order" must {
        val stack = new mutable.Stack[Int]
        stack.push(1)
        stack.push(2)
        stack.pop() mustBe 2
        stack.pop() mustBe 1
        Logger.logger.warn(">>>>>>>>>>>stack pop")

        "A Test" can {
            Logger.logger.info("can do something")
        }

        "A should Test" should {
            Logger.logger.info("should do something")
        }
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
