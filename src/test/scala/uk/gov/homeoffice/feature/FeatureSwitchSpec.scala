package uk.gov.homeoffice.feature

import com.typesafe.config.ConfigFactory
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification

class FeatureSwitchSpec extends Specification {
  "Feature switch that is not configured" should {
    "not run functionality" in new Scope with FeatureSwitch {
      val result = withFeature("my.feature") {
        "Done!"
      }

      result must beNone
    }

    "run when defaulted to do so" in new Scope with FeatureSwitch {
      val result = withFeature("my.feature", default = true) {
        "Done!"
      }

      result must beSome("Done!")
    }
  }

  "Feature switch that is configured to false" should {
    "not run functionality" in new Scope with FeatureSwitch {
      override implicit val config = ConfigFactory.parseString("my.feature = false")

      val result = withFeature("my.feature") {
        "Done!"
      }

      result must beNone
    }
  }

  "Feature switch that is configured to off" should {
    "not run functionality" in new Scope with FeatureSwitch {
      override implicit val config = ConfigFactory.parseString("my.feature = off")

      val result = withFeature("my.feature") {
        "Done!"
      }

      result must beNone
    }
  }

  "Feature switched (configured to be) on" should {
    "run functionality" in new Scope with FeatureSwitch {
      override implicit val config = ConfigFactory.parseString("my.feature = on")

      val result = withFeature("my.feature") {
        "Done!"
      }

      result must beSome("Done!")
    }
  }

  "Feature switched (configured to be) on" should {
    "run functionality" in new Scope with FeatureSwitch {
      override implicit val config = ConfigFactory.parseString("my.feature = true")

      val result = withFeature("my.feature") {
        "Done!"
      }

      result must beSome("Done!")
    }
  }
}