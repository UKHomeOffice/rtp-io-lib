package uk.gov.homeoffice.json

import org.json4s._
import org.scalactic.Or

trait JsonValidator {
  def jsonSchema: JsonSchema

  def validate(json: JValue): JValue Or JsonError = jsonSchema validate json
}