package uk.gov.homeoffice.io

import java.io.{FileNotFoundException, InputStream}
import scala.io.Source.fromInputStream
import scala.util.{Success, Try}
import org.specs2.mutable.Specification

class ClasspathResourceToInputStreamSpec extends Specification {
  "Classpath resource" should {
    "fail to be read" in {
      Resource(Classpath("/non-existing.json")).to[InputStream] must beFailedTry.withThrowable[FileNotFoundException]
    }

    "give an input stream from root of classpath" in {
      Resource(Classpath("/test.json")).to[InputStream] must beLike[Try[InputStream]] {
        case Success(inputStream) => fromInputStream(inputStream).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }

    "give an input stream from root of classpath even when not providing the mandatory / at the start of the path" in {
      Resource(Classpath("test.json")).to[InputStream] must beLike[Try[InputStream]] {
        case Success(inputStream) => fromInputStream(inputStream).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }

    "give an input stream from classpath of /'s" in {
      Resource(Classpath("/subfolder/test.json")).to[InputStream] must beLike[Try[InputStream]] {
        case Success(inputStream) => fromInputStream(inputStream).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }

    "give an input stream from classpath of /'s even when not providing the mandatory / at the start of the path" in {
      Resource(Classpath("subfolder/test.json")).to[InputStream] must beLike[Try[InputStream]] {
        case Success(inputStream) => fromInputStream(inputStream).mkString mustEqual
          """{
            |    "blah": "whatever"
            |}""".stripMargin
      }
    }
  }
}