package uk.gov.homeoffice.io

import java.io.{File, IOException, InputStream}
import java.net.URL
import scala.io.Codec
import scala.io.Source._
import scala.util.{Failure, Success, Try}
import org.json4s.JValue
import org.json4s.jackson.JsonMethods._

/**
  * A typeclass representing a readable resource
  *
  * @tparam R The type of Resource to read, such as a classpath or File or URL etc.
  * @tparam T The result of reading a resource
  */
trait Readable[R, T] {
  /**
    * Read in a Resource of type R as a T
    * e.g. read a classpath resource resulting in a String
    *
    * @param r Resource of type R
    * @param encoding Codec of resource
    * @return Success[T] when resource is successfully read to produce a T, otherwise a Failure with associated Exception
    */
  def read(r: R)(implicit encoding: Codec): Try[T]
}

/**
  * This companion object brings into scope, default implicits for reading resources.
  */
object Readable {
  /**
    * Read in Classpath Resource -> String
    */
  implicit object ClasspathToString extends Readable[Classpath, String] {
    override def read(cp: Classpath)(implicit encoding: Codec): Try[String] = Try {
      val inputStream = getClass.getResourceAsStream(cp)
      fromInputStream(inputStream).mkString
    } recoverWith {
      case _: NullPointerException => Failure(new IOException(s"Could not read resource for given: $cp"))
    }
  }

  /**
    * Read in Classpath Resource -> InputStream
    */
  implicit object ClasspathToInputStream extends Readable[Classpath, InputStream] {
    override def read(cp: Classpath)(implicit encoding: Codec): Try[InputStream] = Try {
      val inputStream = getClass.getResourceAsStream(cp)

      if (inputStream == null) throw new IOException(s"Could not read resource for given: $cp")
      else inputStream
    }
  }

  /**
    * Read in Classpath Resource -> URL
    */
  implicit object ClasspathToURL extends Readable[Classpath, URL] {
    override def read(cp: Classpath)(implicit encoding: Codec): Try[URL] = Try {
      getClass.getResource(cp)
    } flatMap { url =>
      if (url == null) Failure(new IOException("Resource not found"))
      else Success(url)
    }
  }

  /**
    * Read in Classpath Resource -> JValue
    */
  implicit object ClasspathToJValue extends Readable[Classpath, JValue] {
    override def read(cp: Classpath)(implicit encoding: Codec): Try[JValue] =
      ClasspathToString.read(cp) map { parse(_) }
  }

  /**
    * Read in URL Resource -> String
    */
  implicit object URLToString extends Readable[URL, String] {
    override def read(u: URL)(implicit encoding: Codec): Try[String] = Try {
      fromURL(u).mkString
    }
  }

  /**
    * Read in URL Resource -> JValue
    */
  implicit object URLToJValue extends Readable[URL, JValue] {
    override def read(u: URL)(implicit encoding: Codec): Try[JValue] =
      URLToString.read(u) map { parse(_) }
  }

  /**
    * Read in File Resource -> JValue
    */
  implicit object FileToJValue extends Readable[File, JValue] {
    override def read(f: File)(implicit encoding: Codec): Try[JValue] = Try {
      parse(fromFile(f).mkString)
    }
  }
}