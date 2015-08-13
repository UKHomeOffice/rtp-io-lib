package uk.gov.homeoffice.resource

import java.util.concurrent.atomic.AtomicBoolean
import org.specs2.mutable.Specification
import uk.gov.homeoffice.resource.CloseableResource._

class CloseableResourceSpec extends Specification {
  "Resource" should {
    "be automatically closed" in {
      val resourceClosed = new AtomicBoolean

      class Resource {
        def close() = resourceClosed.set(true)
      }

      using(new Resource) { _ => }

      resourceClosed.get() must beTrue
    }
  }
}