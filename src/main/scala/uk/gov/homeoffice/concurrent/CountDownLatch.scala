package uk.gov.homeoffice.concurrent

import java.util.concurrent.{TimeUnit, TimeoutException}
import scala.concurrent.duration.Duration

object CountDownLatch {
  def apply(initialCount: Int) = new CountDownLatch(initialCount)
}

class CountDownLatch(val initialCount: Int) {
  val underlying = new java.util.concurrent.CountDownLatch(initialCount)

  def count = underlying.getCount

  def isZero = count == 0

  def countDown() = underlying.countDown()

  def await() = underlying.await()

  def await(timeout: Duration) = underlying.await(timeout.toNanos, TimeUnit.NANOSECONDS)

  def within(timeout: Duration) = await(timeout) || {
    throw new TimeoutException(timeout.toString)
  }
}