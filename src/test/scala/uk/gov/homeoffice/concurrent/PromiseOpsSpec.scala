package uk.gov.homeoffice.concurrent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification

class PromiseOpsSpec(implicit ev: ExecutionEnv) extends Specification with PromiseOps {
  "Promise" should {
    "be realized and still give back the result" in {
      val promise = Promise[String]()

      promise <~ Future.successful("Well done!")

      promise.future must beEqualTo("Well done!").await
    }

    """be realized (using named "realized") and still give back the result""" in {
      val promise = Promise[String]()

      promise realized Future.successful("Well done!")

      promise.future must beEqualTo("Well done!").await
    }

    "be not be realized because of an exception" in {
      val promise = Promise[String]()

      promise <~> Future.failed(new Exception("Whoops!"))

      promise.future must throwAn[Exception](message = "Whoops!").await
    }

    """be not be realized (using named "unrealized") because of an exception""" in {
      val promise = Promise[String]()

      promise unrealized Future.failed(new Exception("Whoops!"))

      promise.future must throwAn[Exception](message = "Whoops!").await
    }
  }
}
