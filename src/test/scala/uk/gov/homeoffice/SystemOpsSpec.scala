package uk.gov.homeoffice

import org.specs2.mutable.Specification

class SystemOpsSpec extends Specification with SystemOps {
  "System operations" should {
    "provide OS new line" in {
      val text = s"blah${newLine}blah"

      text mustEqual
        """blah
          |blah""".stripMargin
    }
  }
}