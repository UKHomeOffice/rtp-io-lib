package uk.gov.homeoffice.datetime

import com.github.nscala_time.time.Imports._
import org.specs2.mutable.Specification

class DateTimeSpec extends Specification {
  "Date as a String" should {
    "be converted to DateTime formatted as yyyy-MM-dd" in {
      "1999-12-30" as `yyyy-MM-dd` must beLike {
        case dt: DateTime =>
          dt.getYear mustEqual 1999
          dt.getMonthOfYear mustEqual 12
          dt.getDayOfMonth mustEqual 30
      }
    }
  }
}