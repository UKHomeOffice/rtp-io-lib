package uk.gov.homeoffice.io

import java.net.BindException
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification

class NetworkSpec extends Specification {
  trait Context extends Scope with Network

  "Network" should {
    "get a free port" in new Context {
      val result = freeport(1) { port =>
        "success"
      }

      result mustEqual "success"
    }

    "get a free port after 1 bind exception" in new Context {
      var retries = 0

      val result = freeport(1) { port =>
        if (retries == 0) {
          retries = retries + 1
          throw new BindException
        } else {
          "success"
        }
      }

      result mustEqual "success"
      retries mustEqual 1
    }
  }
}