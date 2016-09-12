package uk.gov.homeoffice.io

import java.net.BindException
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification

class NetworkSpec extends Specification {
  trait Context extends Scope with Network

  "Network" should {
    "get a free port" in new Context {
      val result = freeport() { port =>
        "success"
      }

      result mustEqual "success"
    }

    "get a free port after 1 bind exception" in new Context {
      var retried = -1

      val result = freeport() { port =>
        retried = retried + 1

        if (retried == 0) {
          throw new BindException
        } else {
          "success"
        }
      }

      result mustEqual "success"
      retried mustEqual 1
    }

    "fail to get a free port after 1 retry" in new Context {
      var retried = -1

      freeport(1) { port =>
        retried = retried + 1

        if (retried <= 1) {
          throw new BindException
        } else {
          "success"
        }
      } must throwA[BindException]

      retried mustEqual 1
    }
  }
}