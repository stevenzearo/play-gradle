package util

import scala.collection.immutable.HashMap

/**
 * @author steve
 */
class RowMap[T] {
    private var fieldMap: HashMap[Int, Class[Object]] = new HashMap[Int, Class[Object]]
}
