package registeredtraveller.json

import org.json4s.JsonAST.JInt
import org.json4s._
import org.json4s.native.JsonMethods._
import org.scalautils._
import org.specs2.matcher.JsonMatchers
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class JsonTransformerSpec extends Specification with JsonMatchers {
  trait Context extends Scope with JsonTransformer

  "Mapping an array" should {
    "successfully map a number of fields to a JArray" in new Context {
      def transform(json: JValue): JValue Or JsonError = {
        val (_, newJson) = mapArray("fld" -> "flds.newFld")(json, JNothing)
        Good(newJson)
      }

      val flatJson = parse("""
      {
        "fld_1": "something texty",
        "fld_2": 15
      }""")

      val json = parse("""
      {
        "flds": [
          { "newFld": "something texty" },
          { "newFld": 15 }
        ]
      }""")

      val Good(result) = transform(flatJson)

      result mustEqual json
    }

    "successfully map a number of fields to a JArray and apply the corresponding conversion" in new Context {
      def transform(json: JValue): JValue Or JsonError = {
        val (_, newJson) = mapArray("fee" -> "payment.feeInPence", field => JInt(BigInt(field.extract[String])))(json, JNothing)
        Good(newJson)
      }

      val flatJson = parse("""
      {
        "fee_1": "12",
        "fee_2": "15",
        "fee_3": 18
      }""")

      val json = parse("""
      {
        "payment": [
          { "feeInPence": 12 },
          { "feeInPence": 15 },
          { "feeInPence": 18 }
        ]
      }""")

      val Good(result) = transform(flatJson)

      result mustEqual json
    }

    "transform arrays with elements of 2 digit index" in new Context {
      def transform(json: JValue): JValue Or JsonError = {
        val (_, newJson) = mapArray("item" -> "items.newItem")(json, JNothing)
        Good(newJson)
      }

      val flatJson = parse("""
      {
        "item_9": "nine",
        "item_10": "ten"
      }""")

      val json = parse("""
      {
        "items": [
          { "newItem": "nine" },
          { "newItem": "ten" }
        ]
      }""")

      val Good(result) = transform(flatJson)

      result mustEqual json
    }

    "give bad mapping for array of items with no numbers that indicate their index" in new Context {
      def transform(json: JValue): JValue Or JsonError = {
        val (_, newJson) = mapArray("fee" -> "payment.feeInPence")(json, JNothing)
        Good(newJson)
      }

      todo
    }
  }
}