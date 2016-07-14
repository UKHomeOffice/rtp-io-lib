package uk.gov.homeoffice.json

import scala.util.Try
import org.json4s.JsonDSL._
import org.json4s.{JArray, JObject, JValue, _}
import org.scalactic.Or
import grizzled.slf4j.Logging

/**
  * TODO WARNING Work in progress
  * Transform JSON from one format to another.
  * The API provides "map" and "mapArray" which are similar.
  *
  * JSON to map from (full up) ->                                        -> Updated JSON to map from (hopefully empty)
  *                              |                                       |
  *                              -------->                    ---------->
  *                                       |------> map >------|
  *                              -------->                    ---------->
  *                              |                                       |
  * JSON to map to (empty)------>                                        -> Updated JSON to map to (hopefully full up)
  *
  * Picture mapping JSON, as handing your (original) JSON to "map" which does the transformation in the following way:
  * - Take a piece of of JSON from the given JSON to be mapped (at the same time removing said JSON from the given JSON).
  * - Apply the transformation according to the declared mapping.
  * - The newly transformed JSON is added to JSON that accumulates all the transformations.
  * - Upon completion, you should (hopefully) end up with an updated version of the original JSON, now empty, and the new JSON that contains all the transformations.
  */
trait JsonTransformer extends JsonFormats with Logging {
  type FromProperty = String

  type ToProperty = String

  type PropertyMapping = (FromProperty, ToProperty)

  case class JsonTransformation(oldJson: JValue, newJson: JValue)

  implicit def jsonToTransformation(json: JValue): JsonTransformation = JsonTransformation(json, JNothing)

  def transform(json: JValue): JValue Or JsonError

  /**
    * Map a JSON property from one format to another e.g. to map the JSON
    * {
    *   "propertyName": "propertyValue"
    * }
    * to
    * {
    *   "propertyGroup": {
    *     "newPropertyName": "propertyValue"
    *   }
    * }
    * use the following:
    * map("propertyName" -> "propertyGroup.newPropertyName")
    *
    * Note that a conversion can be provided such that the property must be mapped from a String to an Int e.g.
    * mapping the above "propertyValue" from a String to an Int:
    * map("propertyName" -> "propertyGroup.newPropertyName", property => JInt(BigInt(property.extract[String])))
    * @param propertyMapping he property name mapping e.g "propertyName" -> "propertyGroup.newPropertyName"
    * @param conversion an optional function to convert a property value to another type
    * @return PartialFunction[JsonTransformation, JsonTransformation]
    */
  def map(propertyMapping: PropertyMapping, conversion: JValue => JValue = j => j): PartialFunction[JsonTransformation, JsonTransformation] = {
    case jsonTransformation: JsonTransformation =>
      val (fromProperty, toProperty) = propertyMapping

      val oldJsonUpdated = jsonTransformation.oldJson removeField { case (k, v) => k == fromProperty }

      val newJsonUpdated = jsonTransformation.newJson merge {
        def parse(properties: Seq[String]): JValue = properties match {
          case h +: Nil => h -> convert(jsonTransformation.oldJson \ fromProperty, conversion)
          case h +: t => h -> parse(t)
        }

        parse(toProperty.split("\\."))
      }

      JsonTransformation(oldJsonUpdated, newJsonUpdated)
  }

  /**
    * Map a JSON property array from one format to another e.g. to map the JSON
    * {
    *   "propertyName_0": "propertyValue0"
    *   "propertyName_1": "propertyValue1"
    * }
    * to
    * {
    *   "propertyGroup": {[
    *     { "newPropertyName": "propertyValue0" },
    *     { "newPropertyName": "propertyValue1" }
    *   ]}
    * }
    * use the following:
    * mapArray("propertyName" -> "propertyGroup.newPropertyName")
    *
    * Note that a conversion can be provided such that the property must be mapped from a String to an Int e.g.
    * mapping the above "propertyValue" from a String to an Int:
    * mapArray("propertyName" -> "propertyGroup.newPropertyName", property => JInt(BigInt(property.extract[String])))
    * @param propertyMapping the property name mapping e.g "propertyName" -> "propertyGroup.newPropertyName"
    * @param conversion an optional function to convert a property value to another type
    * @return PartialFunction[JsonTransformation, JsonTransformation]
    */
  def mapArray(propertyMapping: PropertyMapping, conversion: JValue => JValue = j => j): PartialFunction[JsonTransformation, JsonTransformation] = {
    case jsonTransformation: JsonTransformation =>
      val (fromProperty, toProperty) = propertyMapping

      val fromArrayFields = jsonTransformation.oldJson.filterField {
        case (key, value) => key.startsWith(fromProperty)
      } sortWith { case ((k1, _), (k2, _)) => trailingInt(k1) < trailingInt(k2) }

      val oldJsonUpdated = jsonTransformation.oldJson removeField { case (k, v) => k.startsWith(fromProperty) }

      val newJsonUpdated = {
        def parse(properties: Seq[String]): JValue = properties match {
          case h1 +: h2 +: Nil =>
            jsonTransformation.newJson \ h1 match {
              case a @ JArray(list) =>
                jsonTransformation.newJson remove (_ == a) merge {
                  val updatedList = fromArrayFields.zipWithIndex.foldLeft(list) { case (l, ((key, value), index)) =>
                    l.zipWithIndex map { case (existing, i) => if (i == index) existing.merge(JObject(h2 -> convert(value, conversion))) else existing }
                  }

                  JObject(h1 -> JArray(updatedList))
                }

              case _ =>
                jsonTransformation.newJson merge JObject(h1 -> JArray(fromArrayFields.map { case (k, v) => JObject(h2 -> convert(v, conversion)) }))
            }

          case h +: t =>
            h -> parse(t)
        }

        parse(toProperty.split("\\."))
      }

      JsonTransformation(oldJsonUpdated, newJsonUpdated)
  }
  
  def convert(json: JValue, conversion: JValue => JValue) = Try {
    conversion(json)
  } getOrElse {
    warn(s"Failed to apply conversion to (and so leaving as is): $json")
    json
  }

  implicit def partialFunctionOps[A, B](pf: PartialFunction[A, B]): Object { def ~[C](k: (B) => C): PartialFunction[A, C] } = new {
    def ~[C](k: B => C) = pf.andThen(k)
  }

  implicit class TransformerOps(json: JValue) {
    /**
      * Transform the JSON that matches the given "properties" and encapsulate those transformations into a JArray named by the given "property".
      * e.g. takes { givenName_1: "Tim", givenName_2: "David", surname_1: "Gent", surname_2: "Ainslie" }
      *   transformArray("previousNames", ("givenName", "firstName"), ("surname","familyName"))
      *   returns { names: [{ firstName: "Tim", surname: "Gent" }, { firstName: "David", surname: "Ainslie" }] }
      */
    def transformArray(property: String, properties: (String, String)*): JValue = {
      val fields = json.filterField {
        case (key, value) if key.startsWith(properties.head._1) => true
        case _ => false
      }

      if (fields.isEmpty)
        JNothing
      else
        property -> JArray(fields map {
          case (key, value) =>
            val transformedTailProperties = properties.tail.map {
              case (tailKeyFrom, tailKeyTo) => JObject(tailKeyTo -> json \ (tailKeyFrom + key.dropWhile(_ != '_')))
            }

            transformedTailProperties.foldLeft(JObject(properties.head._2 -> value)) { (j1, j2) => j1 merge j2 }
        })
    }
  }

  private def trailingInt(s: String) = """\d+$""".r.findFirstIn(s).map(_.toInt).getOrElse(0)
}