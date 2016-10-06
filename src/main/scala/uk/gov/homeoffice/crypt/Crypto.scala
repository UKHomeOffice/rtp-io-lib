package uk.gov.homeoffice.crypt

import java.nio.charset.StandardCharsets._
import javax.crypto.Cipher
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}

import scala.util.Try
import org.apache.commons.codec.binary.Base64._
import org.json4s.JValue
import org.json4s.JsonDSL._
import uk.gov.homeoffice.json.JsonFormats
import org.json4s.jackson.JsonMethods._

/**
  * Encrypt text and decrypt back to text.
  */
trait Crypto extends JsonFormats {
  def encrypt(data: String, iv: String)(implicit secrets: Secrets): JValue = {
    val cipher: Cipher = Cipher.getInstance(secrets.transformation)
    val secretKey = new SecretKeySpec(secrets.encryptionKey.getBytes(UTF_8), secrets.algorithm)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes(UTF_8), 0, cipher.getBlockSize))
    val encryptedData = cipher.doFinal(data.getBytes(UTF_8))

    ("data" -> sign(encodeBase64String(encryptedData), secrets.signingPassword)) ~ ("iv" -> sign(encodeBase64String(iv.getBytes()), secrets.signingPassword))
  }

  def encrypt(data: JValue, iv: String)(implicit secrets: Secrets): JValue = encrypt(pretty(render(data)), iv)

  def decrypt(j: JValue)(implicit secrets: Secrets): Try[String] = Try {
    val signedData = (j \ "data").extract[String]
    val signedIV = (j \ "iv").extract[String]

    if (verifySignatureFor(signedData, secrets.signingPassword) && verifySignatureFor(signedIV, secrets.signingPassword)) {
      val key = new SecretKeySpec(secrets.encryptionKey.getBytes(UTF_8), secrets.algorithm)
      val cipher: Cipher = Cipher.getInstance(secrets.transformation)

      cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(decodeBase64(signedIV.getBytes()), 0, cipher.getBlockSize))
      new String(cipher.doFinal(decodeBase64(signedData.split("----").head.getBytes(UTF_8))))

    } else {
      throw new IllegalAccessException(s"Badly signed data $signedData & signedIV $signedIV")
    }
  }

  private def verifySignatureFor(signedData: String, signingPassword: String): Boolean = {
    require(signedData.nonEmpty)

    val signedDataWithClientPassword = sign(signedData.split("----").head, signingPassword)

    if (signedData == signedDataWithClientPassword) true else false
  }

  private def sign(data: String, signingPassword: String): String = {
    val hmac256 = new HmacSha256
    val signature = encodeBase64String(hmac256.create(signingPassword, data))
    s"$data----$signature"
  }
}