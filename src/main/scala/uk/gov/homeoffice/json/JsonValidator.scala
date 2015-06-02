package uk.gov.homeoffice.json

import java.net.URL
import scala.util.{Failure, Success}
import org.json4s._
import org.scalactic.Or
import uk.gov.homeoffice.json.JsonSchema.BadSchemaException

trait JsonValidator {
  def jsonSchema: JsonSchema

  def validate(json: JValue): JValue Or JsonError = jsonSchema validate json
}

object JsonValidator {
  def apply(schema: URL) = new JsonValidator with Json {
    val jsonSchema = jsonFromUrlContent(schema) match {
      case Success(s) => JsonSchema(s)
      case Failure(t) => throw new BadSchemaException(s"Failed to create JsonSchema for $path: $t")
    }
  }
}