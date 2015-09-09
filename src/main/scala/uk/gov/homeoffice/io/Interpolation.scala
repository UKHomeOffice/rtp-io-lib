package uk.gov.homeoffice.io

import java.io.InputStream

object Interpolation {
  implicit class Interpolation(val sc: StringContext) extends AnyVal {
    def in(args: Any*): InputStream = getClass().getResourceAsStream(sc.s(args: _*))
  }
}