package uk.gov.homeoffice.json

import org.specs2.mutable.Specification
import org.json4s.native.JsonMethods._
import uk.gov.homeoffice.json.JValuable._

class JValuableSpec extends Specification with JsonFormats {
  implicit val json = parse("""
  {
    "person": {
      "givenName": "Bruce",
      "familyName": "Wayne"
    }
  }""")

  "JSON" should {
    "tranform a field" in {
      (replace(json \ "person" \ "givenName" -> "Batman") \ "person" \ "givenName").extract[String] mustEqual "Batman"
    }

    "tranform a field by amending it" in {
      (transform(json \ "person" \ "givenName" -> { (n: String) => n.toLowerCase }) \ "person" \ "givenName").extract[String] mustEqual "bruce"
    }
  }
}