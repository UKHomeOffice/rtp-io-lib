package uk.gov.homeoffice.json

import org.specs2.mutable.Specification
import org.json4s.native.JsonMethods._


class JsonErrorSpec extends Specification {

  "Exception" should {

    "be converted to JSON" in {

      val exception = new Exception("There was an error")
      exception.setStackTrace(Array(
        new StackTraceElement("class1", "method1", "file1", 23),
        new StackTraceElement("class2", "method2", "file2", 165),
        new StackTraceElement("class3", "method3", "file3", 12)
      ))
      val jsonError = JsonError(throwable = Some(exception))

      val expectedJSON = parse( """
          {
           "errorMessage": "There was an error",
           "stackTrace": [
             {
              "file": "file1"
              "class": "class1"
              "method": "method1"
              "line": 23
             },
            {
             "file": "file2"
             "class": "class2"
             "method": "method2"
             "line": 165
            },
            {
             "file": "file3"
             "class": "class3"
             "method": "method3"
             "line": 12
            },
           ]
          }
                                """
      )

      jsonError.throwableAsJson must beSome(expectedJSON)
    }
  }

}
