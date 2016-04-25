package uk.gov.homeoffice.concurrent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * Extra implicit functionality added to Promise.
  * Examples shown in uk.gov.homeoffice.concurrent.PromiseOpsSpec
  */
trait PromiseOps {
  implicit class PromiseOps[R](p: Promise[R]) {
    /**
      * Complete a Promise as successful with the given future result, and give back said future result.
      * E.g. a function may result in a Future such as def myFunction = Future { ... }
      * You can complete a Promise and keep the "flow" of the function be prefixing with the promise to be "realized":
      * def myFunction = myPromise <~ Future { ... }
      */
    def <~(result: Future[R]): Future[R] = result map { r =>
      p success r
      r
    }

    /**
      * Text named version of future of <~
      */
    def realized(result: Future[R]): Future[R] = <~(result)

    /**
      * Complete a Promise as a failure that encapsulates an exception, and give back said future result.
      * E.g. a function may result in a Future such as def myFunction = Future { ... }
      * You can complete a Promise and keep the "flow" of the function be prefixing with the promise to be "unrealized":
      * def myFunction = myPromise <~> Future { ... }
      */
    def <~>(result: Future[R]): Future[R] = result recoverWith {
      case t =>
        p failure t
        result
    }

    /**
      * Text named version of future of <~>
      */
    def unrealized(result: Future[R]): Future[R] = <~>(result)
  }
}