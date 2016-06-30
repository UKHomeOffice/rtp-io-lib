package uk.gov.homeoffice.crypt

import java.nio.charset.StandardCharsets._
import javax.crypto

class HmacSha256  {
  private val algorithm = "HMacSha256"

  private def newMac = crypto.Mac.getInstance(algorithm)

  def create(key : String, message : String) = hmac(key.getBytes(UTF_8), message.getBytes(UTF_8))

  private def hmac(key : Array[Byte], message : Array[Byte]) : Array[Byte] = {
    val secret = new crypto.spec.SecretKeySpec(key, algorithm)
    val mac = newMac

    mac.init(secret)
    mac.doFinal(message)
  }
}