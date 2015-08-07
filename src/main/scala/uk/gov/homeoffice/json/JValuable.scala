package uk.gov.homeoffice.json

import org.json4s.JsonAST.{JInt, JString}
import org.json4s._

object JValuable {
  /**
   * Transform the value of a given field (first parameter of tuple transformation) for some JSON (an implicit JValue)
   * @param transformation Two parameter tuple where the first parameter is the field to be transformed and the second is the new value
   * @param json JValue of the overall JSON containing the field to be transformed
   * @tparam V The new value of the transformation
   * @return JValue of the (new) JSON containing the field that has been transformed
   */
  def transform[V : JValuable](transformation: (JValue, V))(implicit json: JValue): JValue = json transformField {
    case (k, v) if v == transformation._1 => k -> implicitly[JValuable[V]].asJValue(transformation._2)
  }

  trait JValuable[V] {
    def asJValue(v: V): JValue
  }

  implicit object StringJValuable extends JValuable[String] {
    def asJValue(v: String) = JString(v)
  }

  implicit object IntJValuable extends JValuable[Int] {
    def asJValue(v: Int) = JInt(v)
  }
}