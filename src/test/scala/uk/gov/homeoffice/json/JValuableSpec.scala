package uk.gov.homeoffice.json

import org.json4s.JsonAST.JNothing
import org.specs2.mutable.Specification
import org.json4s.jackson.JsonMethods._
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

    "remove a specific field" in {
      json \ "person" \ "givenName" must not be equalTo(JNothing)
      val jsonAfterRemoval = json.remove { _ == json \ "person" \ "givenName" }
      jsonAfterRemoval \ "person" \ "givenName" mustEqual JNothing
    }
  }
}