package uk.gov.homeoffice.io

import java.io.File

case class Classpath(private val path: String)

object Classpath {
  /** Makes sure that a given string representing a class path is operating system agnostic. */
  val path: String => String =
    p => {
      val osAgnosticPath = p.replaceAll("""\\""", "/").replaceAll("/", File.separator)

      if (osAgnosticPath.startsWith("/")) osAgnosticPath
      else s"/$osAgnosticPath"
    }

  /** Implicitly convert a Classpath to a String - Use this as the path of a Classpath is private and not accessible by all other client code. */
  implicit val classpath2String: Classpath => String =
    cp => path(cp.path)

  implicit val classpath2Resource: Classpath => Resource[Classpath] =
    cp => Resource(cp)
}