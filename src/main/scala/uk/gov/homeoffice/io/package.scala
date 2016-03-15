package uk.gov.homeoffice

import java.io.File
import java.net.URL

package object io {
  implicit val file2Resource: File => Resource[File] = toResource

  implicit val url2Resource: URL => Resource[URL] = toResource

  private def toResource[R](r: R) = Resource(r)
}