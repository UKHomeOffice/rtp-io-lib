package uk.gov.homeoffice.json

import java.io.FileNotFoundException
import org.json4s.JsonAST.{JInt, JNothing}
import org.json4s.jackson.JsonMethods._
import org.json4s.{JObject, JString}
import org.specs2.mutable.Specification
import uk.gov.homeoffice.json.stuff.{MoreStuff, MyStuff}

class JsonSpec extends Specification with Json with JsonFormats {
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

    "give its content including applying an adapter" in {
      jsonFromFilepath(path("src/test/resources/test-2.json"))(_.replaceAll("world", "WORLD")) must beSuccessfulTry { JObject("hello" -> JString("WORLD!")) }
    }

    "not be found" in {
      jsonFromFilepath(path("src/test/resources/blah.json")) must beFailedTry.withThrowable[FileNotFoundException]
    }
  }

  "JSON" should {
    "be extracted to a case class" in {
      val json = parse("""
      {
        "id": "Stuff ID",
        "moreStuff": {
          "howMuch": 10
        }
      }""")

      json.extract[MyStuff] must beLike[MyStuff] {
        case MyStuff(_, MoreStuff(howMuch)) => howMuch mustEqual 10
      }
    }

    "not be found" in {
      val json = parse("""
      {
        "id": "Stuff ID",
        "moreStuff": {
          "howMuch": 10
        }
      }""")

      json \ "not-there" mustEqual JNothing
      json \ "moreStuff" \ "howMuch" mustEqual JInt(10)
    }
  }

  /*"JSON" should {
    val json = "hello" -> "world"

    "give non fatal JSON error" in {
      bad(json)(new IllegalArgumentException) must beLike {
        case Bad(j) => j.fatalException must beFalse
      }
    }

    "give fatal JSON error" in {
      val fatal = bad(json, j => { case i: IllegalArgumentException => Bad(JsonError(j, i.getMessage, Some(i), fatalException = true)) })

      fatal(new IllegalArgumentException) must beLike {
        case Bad(j) => j.fatalException must beTrue
      }
    }
  }*/
}