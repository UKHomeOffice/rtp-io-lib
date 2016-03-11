package uk.gov.homeoffice.io

import scala.util.Success
import org.specs2.mutable.Specification

class ResourceToStringSpec extends Specification {
  "Classpath resource" should {
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

    /*"give a string from classpath of /'s" in {
      Resource("/subfolder/test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }*/

    /*"give a string from classpath of /'s even when not providing the mandatory / at the start of the path" in {
      Resource("subfolder/test.json").to[String] mustEqual Success {
        """{
          |    "blah": "whatever"
          |}""".stripMargin
      }
    }*/
  }
}