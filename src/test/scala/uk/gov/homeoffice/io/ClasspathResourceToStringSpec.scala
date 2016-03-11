package uk.gov.homeoffice.io

import java.io.FileNotFoundException
import scala.util.Success
import org.specs2.mutable.Specification

class ClasspathResourceToStringSpec extends Specification {
  "Classpath resource" should {
    "fail to be read" in {
      Resource(Classpath("/non-existing.json")).to[String] must beFailedTry.withThrowable[FileNotFoundException]
    }

    "give a string from root of classpath" in {
      Resource(Classpath("/test.json")).to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }

    "give a string from root of classpath even when not providing the mandatory / at the start of the path" in {
      Resource(Classpath("test.json")).to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }

    "give a string from classpath of /'s" in {
      Resource(Classpath("/subfolder/test.json")).to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }

    "give a string from classpath of /'s even when not providing the mandatory / at the start of the path" in {
      Resource(Classpath("subfolder/test.json")).to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }
  }
}