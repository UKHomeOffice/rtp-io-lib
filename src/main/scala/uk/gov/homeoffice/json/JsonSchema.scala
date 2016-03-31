package uk.gov.homeoffice.json

import java.net.URL
import scala.collection.JavaConversions._
import scala.util.{Failure, Success}
import org.json4s.JValue
import org.json4s.JsonAST.JNothing
import org.json4s.jackson.JsonMethods._
import org.scalactic.{Bad, Good, Or}
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.exceptions.ProcessingException
import com.github.fge.jsonschema.main.JsonSchemaFactory
import uk.gov.homeoffice.json.JsonSchema.Validator

/**
 * JSON Schema that conforms to http://json-schema.org/
 * Upon given JSON that is a valid JSON schema, this class represents said schema
  *
  * @param validator Validator with underlying type of com.github.fge.jsonschema.main.JsonSchema
 */
class JsonSchema(validator: Validator) {
  def validate(json: JValue): JValue Or JsonError = try {
    val processingReport = validator.validate(JsonLoader.fromString(compact(render(json))))

    if (processingReport.isSuccess) {
      Good(json)
    } else {
      val errorMessages = for {
        processingMessage <- processingReport.iterator().toList
        message = processingMessage.toString if !message.contains("the following keywords are unknown and will be ignored")
      } yield message

      Bad(JsonError(json, Some(errorMessages.mkString(", "))))
    }
  } catch {
    case e: ProcessingException => Bad(JsonError(json, Some(e.getProcessingMessage.getMessage), Some(e)))
    case t: Throwable => Bad(JsonError(json, Some(t.getMessage), Some(t)))
  }
}

/**
 * JSON Schema factory
 */
object JsonSchema extends Json {
  type Validator = com.github.fge.jsonschema.main.JsonSchema

  def apply(schema: URL): JsonSchema = jsonFromUrlContent(schema) match {
    case Success(j) => apply(j)
    case Failure(t) => throw new BadSchemaException(s"Failed to parse $schema into a JSON schema because: ${t.getMessage}")
  }

  def apply(schema: JValue): JsonSchema = {
    // TODO Not sure I like this "val" followed by "if" - Think validation "options" can be given to the underlying validator, but don't know how.
    val missingRequiredProperties = Seq("$schema", "id", "type", "properties").foldLeft(Seq.empty[String]) { (seq, p) =>
      if (schema \ p == JNothing) seq :+ p
      else seq
    }

    if (missingRequiredProperties.nonEmpty)
      throw new BadSchemaException(s"Given JSON schema is invalid, missing mandatory fields: ${missingRequiredProperties.mkString(", ")}")

    val jsonSchemaNode = JsonLoader fromString compact(render(schema))

    val validator = {
      val syntaxValidator = JsonSchemaFactory byDefault() getSyntaxValidator
      val processingReport = syntaxValidator validateSchema jsonSchemaNode

      if (processingReport.isSuccess)
        JsonSchemaFactory byDefault() getJsonSchema jsonSchemaNode
      else
        throw new BadSchemaException(s"Given JSON schema is invalid: $processingReport")
    }

    new JsonSchema(validator)
  }

  class BadSchemaException(message: String) extends Exception(message)
}

/**
  * Even though JSON should always be validated against an associated schema, there could still be a good reason to allow any JSON, hence this schema with no rules (empty) can be used.
  */
object EmptyJsonSchema extends JsonSchema(JsonSchemaFactory byDefault() getJsonSchema JsonLoader.fromString("{}"))