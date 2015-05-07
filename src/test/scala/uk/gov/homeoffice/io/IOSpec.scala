package uk.gov.homeoffice.io

import java.io.IOException
import java.net.URL
import scala.util.Success
import org.specs2.mutable.Specification

class IOSpec extends Specification with IO {
  "Classpath resource" should {
    "be found from a given string representing the path" in {
      fromClasspath(path("/io-test-1.txt")) must beLike {
        case Success(u: URL) => u.getFile must endWith(path("test-classes/io-test-1.txt"))
      }
    }

    "not be found from a given string representing the path" in {
      fromClasspath(path("/io-test-0.txt")) must beFailedTry.withThrowable[IOException]
    }

    "give its content" in {
      fromClasspath(path("/io-test-1.txt")) flatMap urlContentToString must beSuccessfulTry("Hello World!")
    }
  }
}