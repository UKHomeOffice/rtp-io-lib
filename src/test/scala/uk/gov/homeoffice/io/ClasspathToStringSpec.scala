package uk.gov.homeoffice.io

import java.io.IOException
import scala.io.Codec
import scala.util.{Failure, Success}
import org.specs2.mutable.Specification

class ClasspathToStringSpec extends Specification {
  "Classpath resource" should {
    "give a string from root of classpath" in {
      Resource(Classpath("/test.json")).to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }

      "give a string from root of classpath for a specified encoding" in {
        Resource(Classpath("/test.json"), Codec.ISO8859).to[String] mustEqual Success {
          """{
            |    "blah": "whatever"
            |}""".stripMargin
        }
      }
    }
  }

  "Classpath" should {
    "fail to be read" in {
      Classpath("/non-existing.json").to[String] must beLike {
        case Failure(e: IOException) => e.getMessage mustEqual "Could not read resource for given: Classpath(/non-existing.json)"
      }
    }

    "give a string from root of classpath" in {
      Classpath("/test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }

    "give a string from root of classpath even when not providing the mandatory / at the start of the path" in {
      Classpath("test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }

    "give a string from classpath of /'s" in {
      Classpath("/subfolder/test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }

    "give a string from classpath of /'s even when not providing the mandatory / at the start of the path" in {
      Classpath("subfolder/test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }
  }
}