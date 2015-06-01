package uk.gov.homeoffice.json

import org.specs2.mutable.Specification
import uk.gov.homeoffice.json.JsonSchema.BadSchemaException

class JsonValidatorSpec extends Specification {
  "Json schema" should {
    "be valid" in {
      JsonValidator(getClass.getResource("/schema-test.json")).jsonSchema must beAnInstanceOf[JsonSchema]
    }

    "be invalid" in {
      JsonValidator(getClass.getResource("/io-test-1.txt")) must throwA[BadSchemaException]
    }
  }
}