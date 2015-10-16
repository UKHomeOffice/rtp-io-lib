package uk.gov.homeoffice.json

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.scalactic.Or

@deprecated(message = "This trait no longer adds any benefits and really should not required/used", since = "16th October 2015")
trait JsonValidator {
  def jsonSchema: JsonSchema

  def validate(json: JValue): JValue Or JsonError = jsonSchema validate json
}

/**
 * The use of this JSON Validator is not recommended.
 * However, there may be a situation where you do not have a JSON schema but still want to use the JSON functionality provided by this library e.g. for tests.
 */
trait NoJsonValidator extends JsonValidator {
  val jsonSchema = JsonSchema(parse("""
  {
    "$schema": "http://json-schema.org/draft-04/schema#",
    "id": "http://www.gov.uk/no-json-validator",
    "type": "object",
    "properties": {
    }
  }"""))
}