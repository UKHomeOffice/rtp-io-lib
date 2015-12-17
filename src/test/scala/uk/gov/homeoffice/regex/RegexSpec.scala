package uk.gov.homeoffice.regex

import org.specs2.mutable.Specification

class RegexSpec extends Specification {
  "Extract trailing number" should {
    def trailingInt(s: String): Option[Int] = """\d+$""".r.findFirstIn(s).map(_.toInt)

    "give number" in {
      trailingInt("item_10") must beSome(10)
      trailingInt("item10") must beSome(10)
      trailingInt("it_em_10") must beSome(10)
      trailingInt("item_01") must beSome(1)
      trailingInt("item") must beNone
      trailingInt("item_10_") must beNone
    }
  }

  "Regex group" should {
    "capture the second group" in {
      val text = """ "registeredTravellerNumber" : "RTW6Y7VNW" """

      val pattern = """.*?"registeredTravellerNumber" : "(.*?)".*?""".r
      val pattern(value) = text

      value mustEqual "RTW6Y7VNW"
    }
  }
}