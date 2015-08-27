package uk.gov.homeoffice.pickle

import scala.pickling.Defaults._
import scala.pickling.json._
import org.json4s.jackson.JsonMethods._
import org.specs2.mutable.Specification
import uk.gov.homeoffice.json.{JsonFormats => Json4sFormats}

class PickleSpec extends Specification with Json4sFormats {
  "Class" should {
    class Person(val name: String, val age: Int)

    "be pickled" in {
      val person = new Person("Batman", 20)
      val pickledPerson = person.pickle
      val json = parse(pickledPerson.value)

      (json \ "name").extract[String] mustEqual "Batman"

      val unpickledPerson = pickledPerson.unpickle[Person]

      unpickledPerson.name mustEqual person.name
      unpickledPerson.age mustEqual person.age
    }
  }
}