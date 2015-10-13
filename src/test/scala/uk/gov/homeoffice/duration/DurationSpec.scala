package uk.gov.homeoffice.duration

import scala.concurrent.duration._
import org.specs2.mutable.Specification

class DurationSpec extends Specification {
  "Duration" should {
    "be pretty" in {
      val d = (2 days) + (10 hours) + (61 minutes) + (65 seconds)

      d.toPrettyString mustEqual "2 days, 11 hours, 2 minutes, 5 seconds"
    }
  }
}