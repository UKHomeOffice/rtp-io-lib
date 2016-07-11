package uk.gov.homeoffice.crypt

import scala.util.{Random, Success}
import org.json4s.JsonAST.JObject
import org.json4s.jackson.JsonMethods._
import org.json4s.{JString, JValue}
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification

class CryptoSpec extends Specification {
  trait Context extends Crypto with Scope {
    implicit val secrets = Secrets("4a0783fc-2ca1-11e6-b67b-9e71128c", "A0Suns5tMarked5D1stinct7on")

    val encryptionIV = "abcdadsgdighcige"
  }

  "Data" should {
    val rawData = "test"

    "be encrypted and signed" in new Context {
      encrypt(rawData, encryptionIV) must beLike {
        case j: JValue =>
          j \ "data" must beAnInstanceOf[JString]
          j \ "iv" must beAnInstanceOf[JString]
      }
    }

    "be decrypted with given signing password" in new Context {
      val rawData = "test"
      val r = Random

      decrypt(encrypt(rawData, r.nextString(16))) must beLike {
        case Success(decryptedData) => decryptedData mustEqual rawData
      }
    }

    "fail to be decrypted and empty json data" in new Context {
      decrypt(JObject()) must beFailedTry
    }

    "successfully decrypt the signed data passed as Json" in new Context {
      val signedJSON = parse("""
      {
        "data": "JUxPd7WN3xfPnaIwctMcPQ==----CqKlGQeY16Pp6HOBSdQF+Fw2SodpoKq2tnTn6WpAreg=",
        "iv": "5rm654u0xJ7ir4foo6frgZzll7vmgJvmiKbqhbXovK7ij4vli7fpqqTkiYXqq6g=----aLfxZ02vLn1xO1IwAd7hewkbS2YVcDMasplz4WaUJHY="
      }""")

      decrypt(signedJSON) must beLike {
        case Success(data) => data mustEqual rawData
      }
    }

    "fail to decrypt the badly signed data passed as Json" in new Context {
      val signedJSON = parse("""
      {
        "data": "xGarbled5sXKTdBVO3BSdYkA==----UoRZUwpLDYdUYA6U4tqsYtZqNqR5MZeM6Oau68ZYSd8=",
        "iv": "6IGK7Zau5oS94Zit5qSH7JG74aOU6J6F5KKY7LyP6rC957Cv4pKI4rOW65q04oWR----wMHiQwXjC9ecsYRNVf0shiJYw9uvYZjBxpExWR31yks="
      }""")

      decrypt(signedJSON) must beFailedTry.like {
        case e: IllegalAccessException => e.getMessage must startWith("Badly signed data")
      }
    }

    "decode with base64 decoder" in new Context {
      val decryptedData = decrypt(encrypt(rawData, encryptionIV))

      decryptedData must beLike {
        case Success(data) => data mustEqual rawData
      }
    }
  }

  "JSON" should {
    "be encrypted and decrypted" in new Context {
      val json = parse("""
      {
        "key": "value"
      }""")

      val encryptedJson = encrypt(json, encryptionIV)

      decrypt(encryptedJson) must beLike {
        case Success(j) => parse(j) mustEqual json
      }
    }
  }
}