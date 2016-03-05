package uk.gov.homeoffice.io

import java.io.{File, IOException}
import java.net.URL
import scala.io.Codec
import scala.util.Success
import org.specs2.mutable.Specification

class IOSpec extends Specification with IO {
  "Classpath resource" should {
    "be found from a given string representing the path" in {
      fromClasspath(path("/test-1.txt")) must beLike {
        case Success(u: URL) => u.getFile must endWith(path("test-classes/test-1.txt"))
      }
    }

    "not be found from a given string representing the path" in {
      fromClasspath(path("/blah.txt")) must beFailedTry.withThrowable[IOException]
    }

    "give its content" in {
      fromClasspath(path("/test-1.txt")) flatMap { urlContentToString(_) } must beSuccessfulTry("Hello World!")
    }
  }

  "Resource content" should {
    val resourceURL = new File("src/test/resources/test.json").toURI.toURL

    "be captured from URL" in {
      urlContentToString(resourceURL) must beLike {
        case Success(content) => content must contain("blah")
      }
    }

    "be captured from URL and adapted" in {
      urlContentToString(resourceURL)(_.replaceAll("blah", "BLAH")) must beLike {
        case Success(content) => content must contain("BLAH")
      }
    }

    "be captured from URL for a requested encoding" in {
      urlContentToString(resourceURL, Codec.ISO8859) must beLike {
        case Success(content) => content must contain("blah")
      }
    }

    "be captured from URL and adapted with requested encoding" in {
      urlContentToString(resourceURL, Codec.ISO8859)(adapt = _.replaceAll("blah", "BLAH")) must beLike {
        case Success(content) => content must contain("BLAH")
      }
    }
  }
}