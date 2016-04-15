package uk.gov.homeoffice.condition

import org.specs2.mutable.Specification

class ConditionSpec extends Specification with Condition {
  "Condition" should {
    "be satified" in {
      def batmanNeedsHelp(fighting: Int) = fighting >= 5

      val sidekick = "Robin" when batmanNeedsHelp(fighting = 25)
      sidekick must beSome("Robin")
    }

    "not be satified" in {
      def batmanNeedsHelp(fighting: Int) = fighting >= 5

      val sidekick = "Robin" when batmanNeedsHelp(fighting = 3)
      sidekick must beNone
    }
  }

  "Condition including therfore" should {
    "be satified" in {
      def batmanNeedsHelp(fighting: Int) = fighting >= 5

      val sidekick = when(batmanNeedsHelp(fighting = 25)) therefore "Robin"
      sidekick must beSome("Robin")
    }

    "not be satified" in {
      def batmanNeedsHelp(fighting: Int) = fighting >= 5

      val sidekick = when(batmanNeedsHelp(fighting = 3)) therefore "Robin"
      sidekick must beNone
    }
  }
}