package uk.gov.homeoffice.io

import java.io.{IOException, InputStream}
import scala.io.Source.fromInputStream
import scala.util.{Failure, Success, Try}
import org.specs2.mutable.Specification

class ClasspathToInputStreamSpec extends Specification {
  "Classpath resource" should {
    "give an input stream from root of classpath" in {
      Resource(Classpath("/test.json")).to[InputStream] must beLike[Try[InputStream]] {
        case Success(inputStream) => fromInputStream(inputStream).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }
  }

  "Classpath" should {
    "fail to be read" in {
      Classpath("/non-existing.json").to[InputStream] must beLike {
        case Failure(e: IOException) => e.getMessage mustEqual "Could not read resource for given: Classpath(/non-existing.json)"
      }
    }

    "give an input stream from root of classpath" in {
      Classpath("/test.json").to[InputStream] must beLike[Try[InputStream]] {
        case Success(inputStream) => fromInputStream(inputStream).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }

    "give an input stream from root of classpath even when not providing the mandatory / at the start of the path" in {
      Classpath("test.json").to[InputStream] must beLike[Try[InputStream]] {
        case Success(inputStream) => fromInputStream(inputStream).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }

    "give an input stream from classpath of /'s" in {
      Classpath("/subfolder/test.json").to[InputStream] must beLike[Try[InputStream]] {
        case Success(inputStream) => fromInputStream(inputStream).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }

    "give an input stream from classpath of /'s even when not providing the mandatory / at the start of the path" in {
      Classpath("subfolder/test.json").to[InputStream] must beLike[Try[InputStream]] {
        case Success(inputStream) => fromInputStream(inputStream).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }
  }
}