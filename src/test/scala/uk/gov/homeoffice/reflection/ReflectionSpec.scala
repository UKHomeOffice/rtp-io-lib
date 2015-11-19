package uk.gov.homeoffice.reflection

import org.specs2.mutable.Specification

class ReflectionSpec extends Specification {
  "A String" should {
    "be instantiated by its default constructor" in {
      val s: String = instantiate("java.lang.String")
      s mustEqual ""
    }

    "be instantiated by one argument constructor" in {
      val s: String = instantiate("java.lang.String", "Hello World")
      s mustEqual "Hello World"
    }
  }
}