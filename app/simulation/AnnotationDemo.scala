package simulation

import scala.annotation.Annotation
import scala.annotation.meta.{beanGetter, beanSetter, getter}

/**
 * @author steve
 */
object AnnotationDemo {
  @beanSetter
  class BeanSetterDemo extends Annotation
  @beanGetter
  class BeanGetterDemo extends Annotation
  @getter
  class GetterDemo extends Annotation

  @BeanGetterDemo
  class Employee {

  }

}
