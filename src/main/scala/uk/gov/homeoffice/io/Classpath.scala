package uk.gov.homeoffice.io

import java.io.File

case class Classpath(private val path: String)

object Classpath {
  /**
    * Makes sure that a given string representing a class/file path is operating system agnostic.
    */
  val path: String => String =
    p => {
      val osAgnosticPath = p.replaceAll("""\\""", "/").replaceAll("/", File.separator)

      if (osAgnosticPath.startsWith("/")) osAgnosticPath
      else s"/$osAgnosticPath"
    }

  implicit val classpath2String: Classpath => String =
    cp => path(cp.path)
}