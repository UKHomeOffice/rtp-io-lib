package uk.gov.homeoffice.io

import java.io.IOException
import java.net.URL
import scala.io.Codec
import scala.util.{Failure, Success}
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.specs2.mutable.Specification

class URLToJValueSpec extends Specification {
  "URL resource" should {
    "give JSON" in {
      Resource(new URL("file:./src/test/resources/test.json")).to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }

      "give JSON for a specified encoding" in {
        Resource(new URL("file:./src/test/resources/test.json"), Codec.ISO8859).to[JValue] mustEqual Success {
          "blah" -> "whatever": JValue
        }
      }
    }
  }

  "URL" should {
    "fail to be read" in {
      new URL("file:./src/test/resources/non-existing.json").to[JValue] must beLike {
        case Failure(e: IOException) => e.getMessage mustEqual "Could not read URL for given: file:./src/test/resources/non-existing.json"
      }
    }

    "give JSON" in {
      new URL("file:./src/test/resources/test.json").to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }
  }
}