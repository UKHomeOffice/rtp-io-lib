package uk.gov.homeoffice.condition

trait Condition {
  /**
    * Example usage:
    *
    * def batmanNeedsHelp(fighting: Int) = fighting >= 5
    *
    * val sidekick: Option[String] = "Robin" when batmanNeedsHelp(fighting = 25)
    */
  implicit class When[A](a: => A) {
    def when(check: => Boolean) = if (check) Some(a) else None
  }

  /**
    * Example usage:
    *
    * def batmanNeedsHelp(fighting: Int) = fighting >= 5
    *
    * val sidekick: Option[String] = when(batmanNeedsHelp(fighting = 25)) therefore "Robin"
    */
  def when(check: => Boolean) = new {
    def therefore[R](result: => R) = if (check) Some(result) else None
  }
}