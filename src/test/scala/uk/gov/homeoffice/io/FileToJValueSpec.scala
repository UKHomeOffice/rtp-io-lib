package uk.gov.homeoffice.io

import java.io.{File, IOException}
import scala.io.Codec
import scala.util.Success
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.specs2.mutable.Specification

class FileToJValueSpec extends Specification {
  "File resource" should {
    "fail to be read" in {
      Resource(new File("src/test/resources/non-existing.json")).to[JValue] must beFailedTry.withThrowable[IOException]
    }

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
}