package uk.gov.homeoffice.io

import java.io.InputStream
import org.specs2.mutable.Specification

class InterpolationSpec extends Specification {
  import Interpolation._

  "String" should {
    "be interpolated to input stream" in {
      in"/test.json" must beAnInstanceOf[InputStream]
    }
  }
}