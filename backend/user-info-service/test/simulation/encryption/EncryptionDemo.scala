package simulation.encryption

import java.security.MessageDigest

import org.apache.commons.codec.digest.MessageDigestAlgorithms

/**
 * @author steve
 */
object EncryptionDemo {
  def main(args: Array[String]): Unit = {
    val SHA256 = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256)
    val encrypted = SHA256.digest("hello".getBytes()).mkString
    println(encrypted)
  }
}
