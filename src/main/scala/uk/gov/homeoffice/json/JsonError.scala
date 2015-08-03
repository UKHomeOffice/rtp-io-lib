package uk.gov.homeoffice.json

import org.json4s.JValue
import org.json4s.JsonAST.{JObject, JNothing}
import org.json4s.JsonDSL._

case class JsonError(json: JValue = JNothing, error: Option[String] = None, throwable: Option[Throwable] = None) {
  def asJson = JObject() merge json merge {
    error.fold(JObject()) { error => "error" -> error }
  } merge {
    throwableAsJson.fold(JObject()) { throwableJson => "errorStackTrace" -> throwableJson }
  }

  def throwableAsJson: Option[JValue] = throwable map { t =>
    ("errorMessage" -> t.getMessage) ~
    ("stackTrace" -> t.getStackTrace.toList.map { st =>
      ("file" -> st.getFileName) ~
      ("class" -> st.getClassName) ~
      ("method" -> st.getMethodName) ~
      ("line" -> st.getLineNumber)
    })
  }
}