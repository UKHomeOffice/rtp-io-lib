package uk.gov.homeoffice.json

import org.json4s.JsonAST.{JInt, JString}
import org.json4s._

object JValuable {
  def transform[V : JValuable](transformation: (JValue, V))(implicit jvalue: JValue) = jvalue transformField {
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