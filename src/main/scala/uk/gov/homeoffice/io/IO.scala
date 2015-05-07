package uk.gov.homeoffice.io

import java.io.{IOException, File}
import java.net.URL
import scala.io.Source
import scala.util.{Success, Failure, Try}

trait IO {
  val fromClasspath: String => Try[URL] =
    classpath => Try {
      getClass.getResource(classpath)
    } flatMap { url =>
      if (url == null) Failure(new IOException("Resource not found"))
      else Success(url)
    }

  val urlContentToString: URL => Try[String] =
    url => Try { Source.fromURL(url).getLines().mkString }

  /** Makes sure that a given string representing a file path is operating system agnostic. */
  val path: String => String =
    p => p.replaceAll("""\\""", "/").replaceAll("/", File.separator)
}