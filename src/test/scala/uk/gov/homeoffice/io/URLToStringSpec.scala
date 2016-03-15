package uk.gov.homeoffice.io

import java.io.IOException
import java.net.URL
import scala.io.Codec
import scala.util.Success
import org.specs2.mutable.Specification

class URLToStringSpec extends Specification {
  "URL resource" should {
    "give a string" in {
      Resource(new URL("file:./src/test/resources/test.json")).to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }

    "give a string for a specified encoding" in {
      Resource(new URL("file:./src/test/resources/test.json"), Codec.ISO8859).to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }
  }

  "URL" should {
    "fail to be read" in {
      new URL("file:./src/test/resources/non-existing.json").to[String] must beFailedTry.withThrowable[IOException]
    }

    "give a string" in {
      new URL("file:./src/test/resources/test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }
  }
}