package uk.gov.homeoffice.io

import scala.io.Source._
import scala.util.Try

case class ClasspathResource(path: String) {
  def to[T : Resource] = implicitly[Resource[T]].read(path)
}

trait Resource[A] {
  def read(path: String): Try[A]
}

object Resource {
  implicit object ToString extends Resource[String] {
    override def read(path: String): Try[String] = Try {
      val inputStream = getClass.getResourceAsStream(path)
      fromInputStream(inputStream).mkString
    }
  }
}