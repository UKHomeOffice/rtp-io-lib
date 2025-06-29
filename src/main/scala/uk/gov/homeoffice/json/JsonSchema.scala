package uk.gov.homeoffice.json

import java.net.URL
import collection.JavaConverters._
import scala.util.{Failure, Success}
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.exceptions.ProcessingException
import com.github.fge.jsonschema.main.JsonSchemaFactory
import org.json4s._
import org.json4s.JsonAST.JNothing
import org.json4s.jackson.JsonMethods._
import uk.gov.homeoffice.json.JsonSchema.Validator

class JsonSchema(validator: Validator) {
  def validate(json: JValue): Either[JsonError, JValue] = try {
    val processingReport = validator.validate(JsonLoader.fromString(compact(render(json))))

    if (processingReport.isSuccess) {
      Right(json)
    } else {
      val errorMessages = for {
        processingMessage <- processingReport.iterator().asScala.toList
        message = processingMessage.toString if !message.contains("the following keywords are unknown and will be ignored")
      } yield message

      Left(JsonError(json, Some(errorMessages.mkString(", "))))
    }
  } catch {
    case e: ProcessingException => Left(JsonError(json, Some(e.getProcessingMessage.getMessage), Some(e)))
    case t: Throwable => Left(JsonError(json, Some(t.getMessage), Some(t)))
  }
}

object JsonSchema extends Json with JsonFormats {
  type Validator = com.github.fge.jsonschema.main.JsonSchema

  def apply(schema: URL): JsonSchema = jsonFromUrlContent(schema) match {
    case Success(j) => apply(j)
    case Failure(t) => throw new BadSchemaException(s"Failed to parse $schema into a JSON schema because: ${t.getMessage}")
  }

  def apply(schema: JValue): JsonSchema = {
    val missingRequiredProperties = Seq("$schema", "id", "type").foldLeft(Seq.empty[String]) {
      case (seq, p) if schema \ p == JNothing => seq :+ p
      case (seq, _) => seq
    }

    if (missingRequiredProperties.nonEmpty)
      throw new BadSchemaException(s"Given JSON schema is invalid, missing mandatory fields: ${missingRequiredProperties.mkString(", ")}")

    val jsonSchemaNode = JsonLoader fromString compact(render(schema))

    val validator = {
      val syntaxValidator = JsonSchemaFactory.byDefault().getSyntaxValidator()
      val processingReport = syntaxValidator.validateSchema(jsonSchemaNode)

      if (processingReport.isSuccess)
        JsonSchemaFactory.byDefault().getJsonSchema(jsonSchemaNode)
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
object EmptyJsonSchema extends JsonSchema(JsonSchemaFactory.byDefault() getJsonSchema JsonLoader.fromString("{}"))
