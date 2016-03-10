package uk.gov.homeoffice.io

import scala.util.Success
import org.specs2.mutable.Specification

class ClasspathResourceToStringSpec extends Specification {
  "Classpath resource" should {
    "give a string from root of classpath" in {
      ClasspathResource("/test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }

    "give a string from root of classpath even when not providing the mandatory / at the start of the path" in {
      ClasspathResource("test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }

    "give a string from classpath of /'s" in {
      todo

      ClasspathResource("/subfolder/test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }

    "give a string from classpath of /'s even when not providing the mandatory / at the start of the path" in {
      todo

      ClasspathResource("subfolder/test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }
  }
}