package uk.gov.homeoffice.json

import java.io.FileNotFoundException
import org.json4s.JsonAST.{JObject, JString}
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
}