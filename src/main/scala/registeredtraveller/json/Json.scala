package registeredtraveller.json

import java.net.URL
import scala.io.Source
import scala.util.Try
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import registeredtraveller.io.IO

trait Json extends IO {
  val jsonFromUrlContent: URL => Try[JValue] =
    url => urlContentToString(url) map { parse(_) }

  val jsonFromClasspath: String => Try[JValue] =
    classpath => fromClasspath(classpath) flatMap jsonFromUrlContent

  val jsonFromFilepath: String => Try[JValue] =
    filepath => Try { parse(Source.fromFile(filepath).getLines().mkString) }

  implicit class JFieldOps(jfield: (String, JValue)) {
    def merge(json: JValue) = (jfield: JValue) merge json
  }
}