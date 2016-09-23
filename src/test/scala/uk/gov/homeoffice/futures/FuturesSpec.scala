package uk.gov.homeoffice.futures

import java.util.concurrent.{Callable, Executors}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification

class FuturesSpec(implicit ev: ExecutionEnv) extends Specification {
  "Java future" should {
    "be converted to a Scala future" in {
      val value = 99

      val result = toFuture {
        val executor = Executors.newFixedThreadPool(1)

        executor.submit(new Callable[Int]() {
          override def call(): Int = value
        })
      }

      result must beEqualTo(value).await
    }
  }
}