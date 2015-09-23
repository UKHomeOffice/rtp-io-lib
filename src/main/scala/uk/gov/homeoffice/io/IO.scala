package uk.gov.homeoffice.io

import java.io.{InputStream, IOException, File}
import java.net.URL
import scala.io.Source
import scala.util.{Success, Failure, Try}

trait IO {
  def classpathResource(classpath: String): InputStream = getClass.getResourceAsStream(classpath)

  def fromClasspath(classpath: String): Try[URL] =
    Try {
      getClass.getResource(classpath)
    } flatMap { url =>
      if (url == null) Failure(new IOException("Resource not found"))
      else Success(url)
    }

  def urlContentToString(url: URL): Try[String] = Try { Source.fromURL(url).getLines().mkString }

  /** Makes sure that a given string representing a file path is operating system agnostic. */
  def path(p: String): String = p.replaceAll("""\\""", "/").replaceAll("/", File.separator)
}