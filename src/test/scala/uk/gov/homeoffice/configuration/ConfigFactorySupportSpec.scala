package uk.gov.homeoffice.configuration

import java.time.Duration
import scala.concurrent.duration.FiniteDuration
import org.specs2.mutable.Specification

class ConfigFactorySupportSpec extends Specification with ConfigFactorySupport {
  "Configuration support" should {
    "convert a 5 minute duration to a finite duration" in {
      val finitieDuration: FiniteDuration = Duration.parse("PT5M")
      finitieDuration.toMinutes mustEqual 5
    }
  }
}