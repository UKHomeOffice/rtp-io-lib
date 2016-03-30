package uk.gov.homeoffice.json

import org.json4s.JValue
import org.json4s.JsonAST.{JObject, JNothing}
import org.json4s.JsonDSL._

case class JsonError(json: JValue = JNothing, error: Option[String] = None, throwable: Option[Throwable] = None) {
  @deprecated(message = "Use toJson instead which renders any JSON under a 'json' property", since = "30th March 2015")
  def asJson = JObject() merge json merge {
    error.fold(JObject()) { error => "error" -> error }
  } merge {
    throwable map { Json.asJson } getOrElse JObject()
  }

  def toJson = (if (json == JNothing) JObject() else JObject("json" -> json)) merge {
    error.fold(JObject()) { error => "error" -> error }
  } merge {
    throwable map { Json.asJson } getOrElse JObject()
  }
}