package uk.gov.homeoffice.io

import java.io.IOException
import scala.util.{Failure, Success}
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.specs2.mutable.Specification

class ClasspathToJValueSpec extends Specification {
  "Classpath resource" should {
    "give JSON from root of classpath" in {
      Resource(Classpath("/test.json")).to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }
  }

  "Classpath" should {
    "fail to be read" in {
      Classpath("/non-existing.json").to[JValue] must beLike {
        case Failure(e: IOException) => e.getMessage mustEqual "Could not read resource for given: Classpath(/non-existing.json)"
      }
    }

    "give JSON from root of classpath" in {
      Classpath("/test.json").to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }

    "give JSON from root of classpath even when not providing the mandatory / at the start of the path" in {
      Classpath("test.json").to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }

    "give JSON from classpath of /'s" in {
      Classpath("/subfolder/test.json").to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }

    "give JSON from classpath of /'s even when not providing the mandatory / at the start of the path" in {
      Classpath("subfolder/test.json").to[JValue] mustEqual Success {
        "blah" -> "whatever": JValue
      }
    }
  }
}