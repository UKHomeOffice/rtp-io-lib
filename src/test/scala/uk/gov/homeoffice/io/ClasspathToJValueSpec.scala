package uk.gov.homeoffice.io

import java.io.IOException
import scala.util.Success
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.specs2.mutable.Specification

class ClasspathToJValueSpec extends Specification {
  "Classpath resource" should {
    "fail to be read" in {
      Resource(Classpath("/non-existing.json")).to[JValue] must beFailedTry.withThrowable[IOException]
    }

    "give JSON from root of classpath" in {
      Resource(Classpath("/test.json")).to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }

    "give JSON from root of classpath even when not providing the mandatory / at the start of the path" in {
      Resource(Classpath("test.json")).to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }

    "give JSON from classpath of /'s" in {
      Resource(Classpath("/subfolder/test.json")).to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }

    "give JSON from classpath of /'s even when not providing the mandatory / at the start of the path" in {
      Resource(Classpath("subfolder/test.json")).to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }
  }
}