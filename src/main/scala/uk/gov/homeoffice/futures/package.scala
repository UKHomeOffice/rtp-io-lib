package uk.gov.homeoffice

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future, Promise}
import scala.util.Try
import java.util.concurrent.{Future => JFuture}

package object futures {
  def toFuture[A](jFuture: JFuture[A])(implicit executor: ExecutionContextExecutor = ExecutionContext.global): Future[A] = {
    val promise = Promise[A]()

    executor execute new Runnable {
      def run() = promise complete Try {
        jFuture.get
      }
    }

    promise.future
  }
}