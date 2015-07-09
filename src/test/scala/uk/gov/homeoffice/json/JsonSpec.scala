package uk.gov.homeoffice.json

import java.io.FileNotFoundException
import org.json4s.JsonDSL._
import org.json4s.{JObject, JString, JValue}
import org.scalactic.{Bad, Or}
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

  "JSON" should {
    val json = "hello" -> "world"

    "give non fatal JSON error" in {
      error(json)(new IllegalArgumentException) must beLike {
        case Bad(j) => j.fatalException must beFalse
      }
    }

    "give fatal JSON error" in {
      val fatal = error(json, j => PartialFunction[Throwable, JValue Or JsonError] {
        case i: IllegalArgumentException => Bad(JsonError(j, i.getMessage, Some(i), fatalException = true))
      })

      fatal(new IllegalArgumentException) must beLike {
        case Bad(j) => j.fatalException must beTrue
      }
    }
  }
}