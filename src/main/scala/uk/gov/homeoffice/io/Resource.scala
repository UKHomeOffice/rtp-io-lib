package uk.gov.homeoffice.io

import scala.io.Codec
import scala.util.Try

/**
  * Resource that could be a URL, from classpath, or File for example, where the contents is given by stipulating what "to" generate.
  * @param r The actual resource by a specific type such as URL
  * @param encoding Codec i.e. the encoding of the resource
  * @tparam R This is the actual type of the resource
  */
case class Resource[R](r: R, encoding: Codec = Codec.UTF8) {
  /**
    * What "to" generate from said resource
    * @param ev Evidence that manages the reading of the resource in"to" the require format
    * @tparam T The format of what "to" read this resource in"to" such as generating JSON (JValue) from resource
    * @return Success of the required format read in"to" or Failure encapsulting the exception
    */
  def to[T](implicit ev: Readable[R, T]): Try[T] = implicitly[Readable[R, T]].read(r)(encoding)
}