package uk.gov.homeoffice.json

import org.json4s.JValue
import org.json4s.JsonAST.{JObject, JNothing}
import org.json4s.JsonDSL._

case class JsonError(json: JValue = JNothing, error: Option[String] = None, throwable: Option[Throwable] = None) {
  @deprecated(message = "Use toJson instead which renders any JSON under a 'json' property", since = "30th March 2015")
  def asJson = JObject() merge json merge {
    error.fold(JObject()) { error => "error" -> error }
  } merge {
    throwable map { Json.toJson } getOrElse JObject()
  }

  def toJson = (if (json == JNothing) JObject() else JObject("json" -> json)) merge {
    error.fold(JObject()) { error => "error" -> error }
  } merge {
    throwable map { Json.toJson } getOrElse JObject()
  }

  def toException = new JsonErrorException(this)
}

case class JsonErrorException(jsonError: JsonError) extends Exception(JsonErrorException.toString(jsonError), jsonError.throwable orNull)

object JsonErrorException {
  def toString(jsonError: JsonError) = jsonError.json match {
    case JNothing => jsonError.error getOrElse ""
    case json => jsonError.error map { _ + s", json: $json"} getOrElse s", JSON: $json"
  }
}