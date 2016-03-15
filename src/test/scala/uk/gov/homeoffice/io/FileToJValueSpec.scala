package uk.gov.homeoffice.io

import java.io.{File, IOException}
import scala.io.Codec
import scala.util.{Failure, Success}
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.specs2.mutable.Specification

class FileToJValueSpec extends Specification {
  "File resource" should {
    "give JSON" in {
      Resource(new File("src/test/resources/test.json")).to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }

    "give JSON for a specified encoding" in {
      Resource(new File("src/test/resources/test.json"), Codec.ISO8859).to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }
  }

  "File" should {
    "fail to be read" in {
      new File("src/test/resources/non-existing.json").to[JValue] must beLike {
        case Failure(e: IOException) => e.getMessage mustEqual "Could not read file for given: src/test/resources/non-existing.json"
      }
    }

    "give JSON" in {
      new File("src/test/resources/test.json").to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }
  }
}