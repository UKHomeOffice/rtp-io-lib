package uk.gov.homeoffice.io

import scala.io.Source._
import scala.util.Try

case class Resource[R](r: R) {
  def to[T](implicit ev: Readable[R, T]) = implicitly[Readable[R, T]].read(r)
}

trait Readable[R, T] {
  def read(r: R): Try[T]
}

object Readable {
  implicit object ClasspathToString extends Readable[Classpath, String] {
    override def read(cp: Classpath): Try[String] = Try {
      val inputStream = getClass.getResourceAsStream(cp)
      fromInputStream(inputStream).mkString
    }
  }
}