package uk.gov.homeoffice.configuration

import java.time.Duration
import scala.concurrent.duration.FiniteDuration
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification

class ConfigFactorySupportSpec extends Specification {
  trait Context extends Scope with HasConfig with ConfigFactorySupport

  "Configuration support" should {
    "convert a 5 minute duration to a finite duration" in new Context {
      val finitieDuration: FiniteDuration = Duration.parse("PT5M")
      finitieDuration.toMinutes mustEqual 5
    }

    "default text" in new Context {
      config.text("does-not-exist", "default") mustEqual "default"
    }

    "default int" in new Context {
      config.int("does-not-exist", 99) mustEqual 99
    }

    "default boolean" in new Context {
      config.boolean("does-not-exist", true) must beTrue
    }
  }
}