package uk.gov.homeoffice.json

import java.net.URL
import scala.io.Source
import scala.util.Try
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import uk.gov.homeoffice.io.IO

object Json extends Json

trait Json extends IO {
  def jsonFromUrlContent(url: URL)(implicit adapt: String => String = s => s): Try[JValue] = urlContentToString(url)(adapt) map { parse(_) }

  def jsonFromClasspath(classpath: String)(implicit adapt: String => String = s => s): Try[JValue] = fromClasspath(classpath) flatMap { jsonFromUrlContent(_)(adapt) }

  def jsonFromFilepath(filepath: String)(implicit adapt: String => String = s => s): Try[JValue] = Try {
    parse(adapt(Source.fromFile(filepath).getLines().mkString))
  }

  def asJson(t: Throwable): JValue =
    "errorStackTrace" ->
      ("errorMessage" -> t.getMessage) ~
      ("stackTrace" -> t.getStackTrace.toList.map { st =>
        ("file" -> st.getFileName) ~
          ("class" -> st.getClassName) ~
          ("method" -> st.getMethodName) ~
          ("line" -> st.getLineNumber)
      })

  implicit class JFieldOps(jfield: (String, JValue)) {
    def merge(json: JValue) = (jfield: JValue) merge json
  }
}