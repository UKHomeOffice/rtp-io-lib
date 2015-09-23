package uk.gov.homeoffice.logging

import org.specs2.mutable.Specification

class ConsoleLoggingSpec extends Specification with ConsoleLogging {
  "Console logging" should {
    "be available" in {
      withConsoleLog { consoleLog =>
        System.out.println("Hi")
        consoleLog() must contain("Hi")
      }
    }
  }
}