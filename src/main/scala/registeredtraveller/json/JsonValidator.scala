package registeredtraveller.json

import org.json4s._
import org.scalautils._

trait JsonValidator {
  def jsonSchema: JsonSchema

  def validate(json: JValue): JValue Or JsonError = jsonSchema validate json
}