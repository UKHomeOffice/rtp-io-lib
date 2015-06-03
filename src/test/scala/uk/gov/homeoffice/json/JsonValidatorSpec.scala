package uk.gov.homeoffice.json

import java.net.{MalformedURLException, URL}
import scala.util.Success
import org.scalactic.Good
import org.specs2.mutable.Specification
import uk.gov.homeoffice.json.JsonSchema.BadSchemaException

class JsonValidatorSpec extends Specification with Json with JsonFormats {
  "Json validation" should {
    "have a valid schema" in {
      val validator = new JsonValidator {
        val jsonSchema = JsonSchema(getClass.getResource("/schema-test.json"))
      }

      validator.jsonSchema must beAnInstanceOf[JsonSchema]
    }

    "not happen because of invalid schema URL" in {
      new JsonValidator {
        val jsonSchema = JsonSchema(new URL(""))
      } must throwA[MalformedURLException]
    }

    "not happen because of invalid schema" in {
      new JsonValidator {
        val jsonSchema = JsonSchema(getClass.getResource("/test-1.txt"))
      } must throwA[BadSchemaException]
    }

    "pass" in {
      val validator = new JsonValidator {
        val jsonSchema = JsonSchema(getClass.getResource("/schema-test.json"))
      }

      jsonFromClasspath("/test.json").map(validator.validate) must beLike {
        case Success(Good(j)) => (j \ "blah").extract[String] mustEqual "whatever"
      }
    }
  }
}