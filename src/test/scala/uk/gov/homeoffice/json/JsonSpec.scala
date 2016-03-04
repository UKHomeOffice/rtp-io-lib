package uk.gov.homeoffice.json

import java.io.{File, FileNotFoundException}
import scala.io.Codec
import scala.util.Success
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

    val resourceClasspath = path("/test.json")

    "be captured from class path" in {
      jsonFromClasspath(resourceClasspath) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("whatever")
      }
    }

    "be captured from class path and adapted" in {
      jsonFromClasspath(resourceClasspath)(_.replaceAll("whatever", "WHATEVER")) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("WHATEVER")
      }
    }

    "be captured from class path for a requested encoding" in {
      jsonFromClasspath(resourceClasspath)(encoding = Codec.ISO8859) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("whatever")
      }
    }

    "be captured from class path and adapted with requested encoding" in {
      jsonFromClasspath(resourceClasspath)(adapt = _.replaceAll("whatever", "WHATEVER"), encoding = Codec.ISO8859) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("WHATEVER")
      }
    }

    "be captured from class path for a requested encoding and adapted" in {
      jsonFromClasspath(resourceClasspath)(encoding = Codec.ISO8859, adapt = _.replaceAll("whatever", "WHATEVER")) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("WHATEVER")
      }
    }
  }

  "URL JSON resource" should {
    "give its content" in {
      fromClasspath(path("/test-2.json")) flatMap jsonFromUrlContent must beSuccessfulTry { JObject("hello" -> JString("world!")) }
    }

    val resourceURL = new File("src/test/resources/test.json").toURI.toURL

    "be captured from URL" in {
      jsonFromUrlContent(resourceURL) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("whatever")
      }
    }

    "be captured from URL and adapted" in {
      jsonFromUrlContent(resourceURL)(_.replaceAll("whatever", "WHATEVER")) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("WHATEVER")
      }
    }

    "be captured from URL for a requested encoding" in {
      jsonFromUrlContent(resourceURL)(encoding = Codec.ISO8859) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("whatever")
      }
    }

    "be captured from URL and adapted with requested encoding" in {
      jsonFromUrlContent(resourceURL)(adapt = _.replaceAll("whatever", "WHATEVER"), encoding = Codec.ISO8859) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("WHATEVER")
      }
    }

    "be captured from URL for a requested encoding and adapted" in {
      jsonFromUrlContent(resourceURL)(encoding = Codec.ISO8859, adapt = _.replaceAll("whatever", "WHATEVER")) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("WHATEVER")
      }
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

    val resourceFilepath = path("src/test/resources/test.json")

    "be captured from file path" in {
      jsonFromFilepath(resourceFilepath) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("whatever")
      }
    }

    "be captured from file path and adapted" in {
      jsonFromFilepath(resourceFilepath)(_.replaceAll("whatever", "WHATEVER")) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("WHATEVER")
      }
    }

    "be captured from file path for a requested encoding" in {
      jsonFromFilepath(resourceFilepath)(encoding = Codec.ISO8859) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("whatever")
      }
    }

    "be captured from file path and adapted with requested encoding" in {
      jsonFromFilepath(resourceFilepath)(adapt = _.replaceAll("whatever", "WHATEVER"), encoding = Codec.ISO8859) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("WHATEVER")
      }
    }

    "be captured from file path for a requested encoding and adapted" in {
      jsonFromFilepath(resourceFilepath)(encoding = Codec.ISO8859, adapt = _.replaceAll("whatever", "WHATEVER")) must beLike {
        case Success(content) => content \ "blah" mustEqual JString("WHATEVER")
      }
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