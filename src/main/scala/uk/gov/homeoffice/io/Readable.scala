package uk.gov.homeoffice.io

import java.io.{InputStream, FileNotFoundException}
import scala.io.Source._
import scala.util.{Failure, Try}

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
    * @return Success[T] when resource is successfully read to produce a T, otherwise a Failure with associated Exception
    */
  def read(r: R): Try[T]
}

/**
  * This companion object brings into scope, default implicits for reading resources.
  */
object Readable {
  /**
    * Read in Classpath Resource -> String
    */
  implicit object ClasspathToString extends Readable[Classpath, String] {
    override def read(cp: Classpath): Try[String] = Try {
      val inputStream = getClass.getResourceAsStream(cp)
      fromInputStream(inputStream).mkString
    } recoverWith {
      case _: NullPointerException => Failure(new FileNotFoundException(s"Could not read resource for given: $cp"))
    }
  }

  /**
    * Read in Classpath Resource -> InputStream
    */
  implicit object ClasspathToInputStream extends Readable[Classpath, InputStream] {
    override def read(cp: Classpath): Try[InputStream] = Try {
      val inputStream = getClass.getResourceAsStream(cp)

      if (inputStream == null) throw new FileNotFoundException(s"Could not read resource for given: $cp")
      else inputStream
    }
  }
}