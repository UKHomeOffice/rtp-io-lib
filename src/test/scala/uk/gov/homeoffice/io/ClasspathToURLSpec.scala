package uk.gov.homeoffice.io

import java.io.IOException
import java.net.URL
import scala.io.Source.fromURL
import scala.util.{Failure, Success, Try}
import org.specs2.mutable.Specification

class ClasspathToURLSpec extends Specification {
  "Classpath resource" should {
    "give a URL from root of classpath" in {
      Resource(Classpath("/test.json")).to[URL] must beLike[Try[URL]] {
        case Success(url) => fromURL(url).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }
  }

  "Classpath" should {
    "fail to be read" in {
      Classpath("/non-existing.json").to[URL] must beLike {
        case Failure(e: IOException) => e.getMessage mustEqual "Could not read resource for given: Classpath(/non-existing.json)"
      }
    }

    "give a URL from root of classpath" in {
      Classpath("/test.json").to[URL] must beLike[Try[URL]] {
        case Success(url) => fromURL(url).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }

    "give a URL from root of classpath even when not providing the mandatory / at the start of the path" in {
      Classpath("test.json").to[URL] must beLike[Try[URL]] {
        case Success(url) => fromURL(url).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }

    "give a URL from classpath of /'s" in {
      Classpath("/subfolder/test.json").to[URL] must beLike[Try[URL]] {
        case Success(url) => fromURL(url).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }

    "give a URL from classpath of /'s even when not providing the mandatory / at the start of the path" in {
      Classpath("subfolder/test.json").to[URL] must beLike[Try[URL]] {
        case Success(url) => fromURL(url).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }
  }
}