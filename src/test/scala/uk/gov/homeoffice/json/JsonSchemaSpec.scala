package uk.gov.homeoffice.json

import org.json4s.JsonAST.{JObject, JString}
import org.json4s.native.JsonMethods._
import org.scalautils.{Bad, Good}
import org.specs2.mutable.Specification
import JsonSchema.BadSchemaException
import JsonSchemaSpec._
import uk.gov.homeoffice.json.JsonSchema.BadSchemaException

class JsonSchemaSpec extends Specification {
  "JSON schema" should {
    "be invalidated when providing JSON that is not a schema" in {
      JsonSchema(JObject("bad" -> JString("schema"))) must throwA[BadSchemaException](message = "Given JSON schema is invalid, missing mandatory fields: \\$schema, id, type, properties")
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
          println("===   Example of 'bad JSON schema' exception message   ===")
          println(e.getMessage)
          println("=== End Example of 'bad JSON schema' exception message ===")
          ok
      }
    }

    "be valid" in {
      JsonSchema(schema) must beAnInstanceOf[JsonSchema]
    }
  }

  "JSON" should {
    "highlight errors when failing to validate against schema" in {
      val json = parse("""
      {
        "bad": "data"
      }""")

      val Bad(JsonError(j, error, _, _)) = JsonSchema(schema).validate(json)
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

      JsonSchema(schema).validate(json) mustEqual Good(json)
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
              "type": "string"
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
  }
  """)
}