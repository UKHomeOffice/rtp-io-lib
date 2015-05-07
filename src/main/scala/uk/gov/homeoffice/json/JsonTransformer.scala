package uk.gov.homeoffice.json

import scala.util.Try
import org.json4s.JsonAST.JNothing
import org.json4s.JsonDSL._
import org.json4s.{JArray, JObject, JValue, _}
import org.scalautils.Or

/**
 * Transform JSON from one format to another.
 */
trait JsonTransformer extends JsonFormats {
  type FromProperty = String

  type ToProperty = String

  type PropertyMapping = (FromProperty, ToProperty)

  type JsonTransformation = PartialFunction[(JValue, JValue), (JValue, JValue)]

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
   * @return JsonTransformation
   */
  def map(propertyMapping: PropertyMapping, conversion: JValue => JValue = j => j): JsonTransformation = {
    case (fromJson, toJson) =>
      val (fromProperty, toProperty) = propertyMapping

      val fromJsonUpdated = fromJson removeField { case (k, v) => k == fromProperty }

      val toJsonUpdated = toJson merge {
        def parse(properties: Seq[String]): JValue = properties match {
          case h +: Nil => h -> convert(fromJson \ fromProperty, conversion)
          case h +: t => h -> parse(t)
        }

        parse(toProperty.split("\\."))
      }

      (fromJsonUpdated, toJsonUpdated)
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
   * @return JsonTransformation
   */
  def mapArray(propertyMapping: PropertyMapping, conversion: JValue => JValue = j => j): JsonTransformation = {
    case (fromJson, toJson) =>
      val (fromProperty, toProperty) = propertyMapping

      val fromArrayFields = fromJson.filterField {
        case (key, value) => key.startsWith(fromProperty)
      } sortWith { case ((k1, _), (k2, _)) => trailingInt(k1) < trailingInt(k2) }

      val fromJsonUpdated = fromJson removeField { case (k, v) => k.startsWith(fromProperty) }

      val toJsonUpdated = {
        def parse(properties: Seq[String]): JValue = properties match {
          case h1 +: h2 +: Nil =>
            toJson \ h1 match {
              case a@JArray(list) =>
                toJson remove (_ == a) merge {
                  val updatedList = fromArrayFields.zipWithIndex.foldLeft(list) { case (l, ((key, value), index)) =>
                    l.zipWithIndex.map { case (existing, i) => if (i == index) existing.merge(JObject(h2 -> convert(value, conversion))) else existing }
                  }

                  JObject(h1 -> JArray(updatedList))
                }

              case _ =>
                toJson merge JObject(h1 -> JArray(fromArrayFields.map { case (k, v) => JObject(h2 -> convert(v, conversion)) }))
            }

          case h +: t =>
            h -> parse(t)
        }

        parse(toProperty.split("\\."))
      }

      (fromJsonUpdated, toJsonUpdated)
  }
  
  def convert(json: JValue, conversion: JValue => JValue) = Try {
    conversion(json)
  } getOrElse {
    println(s"Failed to apply conversion to (and so leaving as is): $json")
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