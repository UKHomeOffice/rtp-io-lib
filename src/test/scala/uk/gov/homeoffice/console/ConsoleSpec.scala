package uk.gov.homeoffice.console

import org.specs2.mutable.Specification

class ConsoleSpec extends Specification with Console {
  "Console" should {
    "present some text" in {
      println(present("Hello World"))
      ok
    }
  }
}