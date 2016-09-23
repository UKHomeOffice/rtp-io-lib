package uk.gov.homeoffice

import java.util.concurrent.{Future => JFuture}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Try

package object futures {
  def toFuture[A](jFuture: JFuture[A])(implicit ec: ExecutionContext): Future[A] = {
    val promise = Promise[A]()

    ec execute new Runnable {
      def run() = promise complete Try {
        jFuture.get
      }
    }

    promise.future
  }
}