package uk.gov.homeoffice.io

import java.io.IOException
import java.net.URL
import scala.io.Codec
import scala.util.Success
import org.json4s.JValue
import org.specs2.mutable.Specification
import org.json4s.JsonDSL._

class URLToJValueSpec extends Specification {
  "URL resource" should {
    "fail to be read" in {
      Resource(new URL("file:./src/test/resources/non-existing.json")).to[JValue] must beFailedTry.withThrowable[IOException]
    }

    "give JSON" in {
      Resource(new URL("file:./src/test/resources/test.json")).to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }

    "give JSON for a specified encoding" in {
      Resource(new URL("file:./src/test/resources/test.json"), Codec.ISO8859).to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }
  }
}