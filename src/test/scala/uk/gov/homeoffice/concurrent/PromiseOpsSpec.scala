package uk.gov.homeoffice.concurrent

import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import org.specs2.mutable.Specification

class PromiseOpsSpec extends Specification with PromiseOps {
  "Promise" should {
    "be realized and still give back the result" in {
      val promise = Promise[String]()
      var result = "Waiting to be done!"

      Future {
        TimeUnit.SECONDS.sleep(1)
        result = promise <~ "Well done!"
      }

      eventually {
        result mustEqual "Well done!"
      }
    }

    """be realized (using named "realized") and still give back the result""" in {
      val promise = Promise[String]()
      var result = "Waiting to be done!"

      Future {
        TimeUnit.SECONDS.sleep(1)
        result = promise realized "Well done!"
      }

      eventually {
        result mustEqual "Well done!"
      }
    }
  }
}