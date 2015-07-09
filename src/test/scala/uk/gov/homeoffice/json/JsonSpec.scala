package uk.gov.homeoffice.json

import java.io.{IOException, FileNotFoundException}
import scala.util.control.NonFatal
import org.json4s.{JValue, JObject, JString}
import org.json4s.JsonDSL._
import org.scalactic.{Good, Or, Bad}
import org.specs2.mutable.Specification

class JsonSpec extends Specification with Json {
  "Classpath JSON resource" should {
    "give its content" in {
      jsonFromClasspath(path("/test-2.json")) must beSuccessfulTry { JObject("hello" -> JString("world!")) }
    }
  }

  "URL JSON resource" should {
    "give its content" in {
      fromClasspath(path("/test-2.json")) flatMap jsonFromUrlContent must beSuccessfulTry { JObject("hello" -> JString("world!")) }
    }
  }

  "File JSON resource" should {
    "give its content" in {
      jsonFromFilepath(path("src/test/resources/test-2.json")) must beSuccessfulTry { JObject("hello" -> JString("world!")) }
    }

    "not be found" in {
      jsonFromFilepath(path("src/test/resources/blah.json")) must beFailedTry.withThrowable[FileNotFoundException]
    }
  }

  /*"JSON" should {
    "give non fatal JSON error" in {
      val json = "hello" -> "world"

      error(json)(new IllegalArgumentException) must beLike {
        case Bad(j) => j.fatalException must beFalse
      }
    }

    "give fatal JSON error" in {
      def errorX(pfs: (JValue => PartialFunction[Throwable, JValue Or JsonError])*)(json: JValue) = {
        pfs.foldRight {
          PartialFunction[Throwable, JValue Or JsonError] {
            case e: IOException => Bad(JsonError(json, e.getMessage, Some(e), fatalException = true))
            case NonFatal(n) => Bad(JsonError(json, n.getMessage, Some(n)))
            case t => Bad(JsonError(json, t.getMessage, Some(t), fatalException = true))
          }
        } { (pf, acc) => pf(json).applyOrElse(acc) }
      }

      val v = errorX("asdf" -> "asdf")()

      v

      def error(pfs: PartialFunction[Throwable, JValue Or JsonError]*) = (json: JValue) => {
        pfs.foldRight {
          PartialFunction[Throwable, JValue Or JsonError] {
            case e: IOException => Bad(JsonError(json, e.getMessage, Some(e), fatalException = true))
            case NonFatal(n) => Bad(JsonError(json, n.getMessage, Some(n)))
            case t => Bad(JsonError(json, t.getMessage, Some(t), fatalException = true))
          }
        } { (pf, acc) => pf.orElse(acc) }
      }


      //val json: JObject = "hello" -> "world"

      error(PartialFunction[Throwable, JValue Or JsonError] {
        case i: IllegalArgumentException => Bad(JsonError(json, i.getMessage, Some(i), fatalException = true))
      })("hello" -> "world")


      val fatal = (json: JValue) => PartialFunction[Throwable, JValue Or JsonError] {
        case i: IllegalArgumentException => Bad(JsonError(json, i.getMessage, Some(i), fatalException = true))
      }



      error(json, fatal(json)) must beLike {
        case Bad(j) => j.fatalException must beTrue
      }


      /*val json: JObject = "hello" -> "world"

      val fatal = (json: JValue) => PartialFunction[Throwable, JValue Or JsonError] {
        case i: IllegalArgumentException => Bad(JsonError(json, i.getMessage, Some(i), fatalException = true))
      }

      val v = fatal(json).orElse(error(json))
      v

      fatal(json).  .applyOrElse(error(json)) must beLike {
        case Bad(j) => j.fatalException must beTrue
      }*/
    }
  }*/
}