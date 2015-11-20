package uk.gov.homeoffice

import scala.util.{Failure, Success, Try}
import org.specs2.mutable.Specification

class TrySpec extends Specification {
  "Try" should {
    "be failed option" in {
      val result = Try {
        if ("1" == "2") Some("incorrect")
        else throw new Exception
      } match {
        case s @ Success(_) => s
        case Failure(_) => Success(None)
      }

      result must beSuccessfulTry(None)
    }

    "be successful option" in {
      val correct = Some("correct")

      val result = Try {
        if ("1" == "1") correct
        else throw new Exception
      } match {
        case s @ Success(_) => s
        case Failure(_) => Success(None)
      }

      result must beSuccessfulTry(correct)
    }
  }
}