package demo


import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite

/**
 * @author steve
 */
abstract class UnitSpec extends PlaySpec with GuiceOneServerPerSuite {
    def registerTest(): Unit // using @Test for this implementation to register test suits
}