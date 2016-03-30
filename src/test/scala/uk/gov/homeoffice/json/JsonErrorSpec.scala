package uk.gov.homeoffice.json

import org.json4s._
import org.json4s.JsonDSL._
import org.specs2.mutable.Specification
import org.json4s.jackson.JsonMethods._

class JsonErrorSpec extends Specification {
  "JsonError" should {
    "be converted to JSON" in {
      val jsonError = JsonError(json = JObject("data" -> JString("blah")), error = Some("Problem with my app"))

      jsonError.toJson mustEqual
        ("json" ->
          ("data" -> "blah")) ~
        ("error" -> "Problem with my app")
    }

    "be converted to JSON even without a JSON property" in {
      val exception = new Exception("There was an error")

      exception.setStackTrace(
        Array(
          new StackTraceElement("class1", "method1", "file1", 23),
          new StackTraceElement("class2", "method2", "file2", 165),
          new StackTraceElement("class3", "method3", "file3", 12)
        )
      )

      val jsonError = JsonError(error = Some("Problem with my app"), throwable = Some(exception))

      val expectedJSON = parse("""
      {
       "error": "Problem with my app",
       "errorStackTrace": {
         "errorMessage": "There was an error",
         "stackTrace": [
           {
            "file": "file1",
            "class": "class1",
            "method": "method1",
            "line": 23
           },
          {
           "file": "file2",
           "class": "class2",
           "method": "method2",
           "line": 165
          },
          {
           "file": "file3",
           "class": "class3",
           "method": "method3",
           "line": 12
          }
         ]
       }
      }""")

      jsonError.toJson mustEqual expectedJSON
    }
  }
}