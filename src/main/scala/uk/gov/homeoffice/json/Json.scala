package uk.gov.homeoffice.json

import java.io.IOException
import java.net.URL
import scala.io.Source
import scala.util.Try
import scala.util.control.NonFatal
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.scalactic.{Bad, Or}
import uk.gov.homeoffice.io.IO

object Json extends Json

trait Json extends IO {
  val jsonFromUrlContent: URL => Try[JValue] =
    url => urlContentToString(url) map { parse(_) }

  val jsonFromClasspath: String => Try[JValue] =
    classpath => fromClasspath(classpath) flatMap jsonFromUrlContent

  val jsonFromFilepath: String => Try[JValue] =
    filepath => Try { parse(Source.fromFile(filepath).getLines().mkString) }

  def error[G](json: JValue, pf: JValue => PartialFunction[Throwable, G Or JsonError] = (j: JValue) => PartialFunction.empty[Throwable, G Or JsonError]) = {
    val defaultPF = PartialFunction[Throwable, G Or JsonError] {
      case e: IOException => Bad(JsonError(json, e.getMessage, Some(e), fatalException = true))
      case NonFatal(n) => Bad(JsonError(json, n.getMessage, Some(n)))
      case t => Bad(JsonError(json, t.getMessage, Some(t), fatalException = true))
    }

    pf(json) orElse defaultPF
  }

  implicit class JFieldOps(jfield: (String, JValue)) {
    def merge(json: JValue) = (jfield: JValue) merge json
  }
}