package uk.gov.homeoffice.concurrent

import scala.concurrent.Promise

trait PromiseOps {
  implicit class PromiseOps[R](p: Promise[R]) {
    /** Complete a Promise as successful with the given result, and give back said result */
    val <~ = (result: R) => {
      p success result
      result
    }

    /** Text named version of <~ */
    val realized = <~
  }
}