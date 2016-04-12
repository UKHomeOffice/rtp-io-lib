package uk.gov.homeoffice.json

import java.net.URL
import scala.io.{Codec, Source}
import scala.util.Try
import com.google.json.JsonSanitizer._
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import uk.gov.homeoffice.io.IO

/**
  * Singleton to Json resource functionality, if you do not want to mixin the associated trait
  */
object Json extends Json

/**
  * Read JSON resource
  */
trait Json extends IO {
  /**
    * Use this method instead "parse" exposed by JSON4s, as this one will use Google's "sanitize" of a given String before generating a JValue.
    * It is assumed that the given string starts with { and ends with }
    * @param s String The text representing JSON to be generated to a JValue
    * @return Try[JValue] As the parsing may fail a Success of JValue or Failure is generated
    */
  def toJson(s: String): Try[JValue] = Try {
    val string = s.replaceAll("[\n\r]", "").trim

    if (string.startsWith("{") && string.endsWith("}")) parse(sanitize(string))
    else throw new Exception(s"The given string does not start with { and/or end with }, as JSON should be: $string")
  }

  /**
    * Acquire JSON from given URL e.g.
    * fromClasspath(path("/test.json"))
    * @param url URL to resource to read
    * @param encoding Codec of resource which defaults to UTF-8
    * @param adapt Optional function to adapt the content - Note that it is the String that is read in, that is adapted before generating JSON
    * @return Try[JValue] Success of JValue or Failure
    */
  @deprecated(message = "Use uk.gov.homeoffice.io.Resource", since = "11th March 2016")
  def jsonFromUrlContent(url: URL, encoding: Codec = Codec.UTF8)(implicit adapt: String => String = s => s): Try[JValue] =
    urlContentToString(url, encoding)(adapt) map { parse(_) }

  /**
    * Acquire JSON from class path e.g.
    * jsonFromClasspath(path("/test.json"))
    * @param classpath String class path of resource
    * @param encoding Codec of resource which defaults to UTF-8
    * @param adapt Optional function to adapt the content - Note that it is the String that is read in, that is adapted before generating JSON
    * @return Try[JValue] Success of JValue or Failure
    */
  @deprecated(message = "Use uk.gov.homeoffice.io.Resource", since = "11th March 2016")
  def jsonFromClasspath(classpath: String, encoding: Codec = Codec.UTF8)(implicit adapt: String => String = s => s): Try[JValue] =
    urlFromClasspath(classpath) flatMap { jsonFromUrlContent(_, encoding)(adapt) }

  /**
    * Acquire JSON from file path e.g.
    * jsonFromFilepath(path("src/test/test.json"))
    * @param filepath String file path of resource
    * @param encoding Codec of resource which defaults to UTF-8
    * @param adapt Optional function to adapt the content - Note that it is the String that is read in, that is adapted before generating JSON
    * @return Try[JValue] Success of JValue or Failure
    */
  @deprecated(message = "Use uk.gov.homeoffice.io.Resource", since = "11th March 2016")
  def jsonFromFilepath(filepath: String, encoding: Codec = Codec.UTF8)(implicit adapt: String => String = s => s): Try[JValue] = Try {
    parse(adapt(Source.fromFile(filepath)(encoding).getLines().mkString))
  }

  /**
    * Generate JSON representation of a Throwable
    * @param t Throwable to be converted to JSON
    * @return JValue the JSON created from the given Throwable
    */
  @deprecated(message = "Renamed as toJson for consistency with other functionality using toJson", since = "12th April 2016")
  def asJson(t: Throwable): JValue =
    "errorStackTrace" ->
      ("errorMessage" -> t.getMessage) ~
      ("stackTrace" -> t.getStackTrace.toList.map { st =>
        ("file" -> st.getFileName) ~
          ("class" -> st.getClassName) ~
          ("method" -> st.getMethodName) ~
          ("line" -> st.getLineNumber)
      })

  /**
    * Generate JSON representation of a Throwable
    * @param t Throwable to be converted to JSON
    * @return JValue the JSON created from the given Throwable
    */
  def toJson(t: Throwable): JValue =
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

  implicit class JValueOps(json: JValue) {
    /** In scope of the following functions */
    implicit val j = json

    def replace[V : JValuable](transformation: (JValue, V)) = JValuable replace transformation

    def transform[V : JValuable](transformation: (JValue, V => V)) = JValuable transform transformation
  }
}