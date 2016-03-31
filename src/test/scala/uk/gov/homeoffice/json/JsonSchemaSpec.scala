package uk.gov.homeoffice.json

import java.net.{MalformedURLException, URL}
import org.json4s.JsonAST.{JObject, JString}
import org.json4s.jackson.JsonMethods._
import org.scalactic.{Bad, Good}
import org.specs2.mutable.Specification
import grizzled.slf4j.Logging
import uk.gov.homeoffice.json.JsonSchema.BadSchemaException
import uk.gov.homeoffice.json.JsonSchemaSpec._

class JsonSchemaSpec extends Specification with Logging {
  "JSON schema" should {
    "be invalidated when providing an invalid URL" in {
      JsonSchema(new URL("")) must throwA[MalformedURLException]
    }

    "be invalidated when providing a bad schema location" in {
      JsonSchema(new URL("file:///does-not-exist.json")) must throwA[BadSchemaException].like {
        case t => t.getMessage mustEqual "Failed to parse file:/does-not-exist.json into a JSON schema because: /does-not-exist.json (No such file or directory)"
      }
    }

    "be invalidated when providing JSON that is not a schema" in {
      JsonSchema(JObject("bad" -> JString("schema"))) must throwA[BadSchemaException].like {
        case t => t.getMessage mustEqual "Given JSON schema is invalid, missing mandatory fields: $schema, id, type, properties"
      }
    }

    "be invalidated when providing JSON that does not conform to the JSON schema specification" in {
      val schema = parse("""
      {
        "id": "http://www.bad.com/schema",
        "$schema": "http://json-schema.org/draft-04/schema",
        "type": "object",
        "properties": {
          "address": {
            "type": "INVALID TYPE"
          }
        }
      }""")

      JsonSchema(schema) must throwA[BadSchemaException]
    }

    "be invalidated when providing JSON that does not conform to the JSON schema specification and print to console the generated error message" in {
      val schema = parse("""
      {
        "id": "http://www.bad.com/schema",
        "$schema": "http://json-schema.org/draft-04/schema",
        "type": "object",
        "properties": {
          "address": {
            "type": "INVALID TYPE"
          }
        }
      }""")

      try {
        JsonSchema(schema)
        ko
      } catch {
        case e: BadSchemaException =>
          info("===   Example of 'bad JSON schema' exception message   ===")
          info(e)
          info("=== End Example of 'bad JSON schema' exception message ===")
          ok
      }
    }

    "be valid" in {
      JsonSchema(schema) must beAnInstanceOf[JsonSchema]
    }
  }

  "JSON" should {
    "always be validated against an empty JSON schema" in {
      val whateverJson = parse("""
      {
        "blah": "blah"
      }""")

      EmptyJsonSchema.validate(whateverJson) mustEqual Good(whateverJson)
    }

    "highlight errors when failing to validate against schema" in {
      val json = parse("""
      {
        "bad": "data"
      }""")

      val Bad(JsonError(j, Some(error), _)) = JsonSchema(schema).validate(json)
      error must contain("""missing: ["address","phoneNumbers"]""")
      j mustEqual json
    }

    "validate against schema" in {
      val json = parse("""
      {
        "address": {
          "street": "The Street",
          "city": "Edinburgh"
        },
        "phoneNumbers": [
          {
            "location": "home",
            "code": 131
          },
          {
            "location": "work",
            "code": 131
          }
        ]
      }""")

      JsonSchema(schema) validate json mustEqual Good(json)
    }
  }
}

object JsonSchemaSpec {
  val schema = parse("""
  {
    "$schema": "http://json-schema.org/draft-04/schema#",
    "id": "http://www.bad.com/schema",
    "type": "object",
    "properties": {
      "address": {
        "type": "object",
        "properties": {
          "street": {
            "type": "string"
          },
          "city": {
            "type": "string"
          }
        },
        "required": [
          "street",
          "city"
        ]
      },
      "phoneNumbers": {
        "type": "array",
        "items": {
          "type": "object",
          "properties": {
            "location": {
              "type": "string",
              "minLength": 4
            },
            "code": {
              "type": "integer",
              "minimum": 131
            }
          }
        },
        "minItems": 2,
        "uniqueItems": true
      }
    },
    "required": [
      "address",
      "phoneNumbers"
    ]
  }""")
}