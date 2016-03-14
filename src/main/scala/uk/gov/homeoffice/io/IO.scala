package uk.gov.homeoffice.io

import java.io.{FileNotFoundException, InputStream, IOException, File}
import java.net.URL
import scala.io.Codec
import scala.io.Source._
import scala.util.{Success, Failure, Try}

/**
  * Read resource
  */
trait IO {
  /**
    * Acquire resource as input stream from class path
    * @param classpath of required resource
    * @throws FileNotFoundException when file is not found
    * @return InputStream to required resource
    */
  @deprecated(message = "Use uk.gov.homeoffice.io.Resource", since = "11th March 2016")
  def streamFromClasspath(classpath: String): InputStream = {
    val stream = getClass.getResourceAsStream(classpath)
    if (stream == null) throw new FileNotFoundException(s"Could not load resource from $classpath")
    else stream
  }

  /**
    * Acquire resource as URL from class path e.g.
    * fromClasspath(path("/test.txt"))
    * @param classpath String of class path to required resource
    * @return Try[URL] Success of URL when found or Failure of IOException
    */
  @deprecated(message = "Use uk.gov.homeoffice.io.Resource", since = "11th March 2016")
  def urlFromClasspath(classpath: String): Try[URL] = Try {
    getClass.getResource(classpath)
  } flatMap { url =>
    if (url == null) Failure(new IOException("Resource not found"))
    else Success(url)
  }

  /**
    * Acquire content as String of a given URL and optional encoding e.g.
    * fromClasspath(path("/test.txt"))
    * @param url URL of required resource
    * @param encoding Codec that defaults to UTF-8
    * @param adapt Optional function to adapt the content
    * @return Try[String] Success of String when content has been read (and optionally adapted) or Failure
    */
  @deprecated(message = "Use uk.gov.homeoffice.io.Resource", since = "11th March 2016")
  def urlContentToString(url: URL, encoding: Codec = Codec.UTF8)(implicit adapt: String => String = s => s): Try[String] = Try {
    adapt(fromURL(url)(encoding).getLines().mkString)
  }

  /**
    * Makes sure that a given string representing a file path is operating system agnostic.
    */
  def path(p: String): String = p.replaceAll("""\\""", "/").replaceAll("/", File.separator)
}