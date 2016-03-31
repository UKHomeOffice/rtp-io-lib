Scala library for IO functionality
==================================
General Scala IO functionality such as JSON schema validation

Project built with the following (main) technologies:

- Scala

- SBT

- Json4s

Introduction
------------
TODO

Build and Deploy
----------------
The project is built with SBT. On a Mac (sorry everyone else) do:
> brew install sbt

It is also a good idea to install Typesafe Activator (which sits on top of SBT) for when you need to create new projects - it also has some SBT extras, so running an application with Activator instead of SBT can be useful. On Mac do:
> brew install typesafe-activator

To compile:
> sbt compile

or
> activator compile

To run the specs:
> sbt test

To run integration specs:
> sbt it:test

Publishing
----------
To publish the jar to artifactory you will need to 

1. Copy the .credentials file into your <home directory>/.ivy2/
2. Edit this .credentials file to fill in the artifactory security credentials (amend the realm name and host where necessary)

> sbt publish

Note that initially this project refers to some libraries held within a private Artifactory. However, those libraries have been open sourced under https://github.com/UKHomeOffice.

Example Usage
-------------
- Validate JSON against a JSON schema:
```scala
  val json: JValue = getYourJson()
  val schema: JValue = getYourSchema()
  
  val Good(result) = JsonSchema(schema).validate(json) // Assuming successful validation
```

- Transform JSON from one structure to another:
```scala
  val yourJsonTransformer = new JsonTransformer {
    def transform(json: JValue): JValue Or JsonError = {
      val JsonTransformation(oldJson, newJson) = (
        map("name" -> "superName") ~
        mapArray("fee" -> "payment.feeInPence", field => JInt(BigInt(field.extract[String])))
      )(json)
      
      Good(newJson)
    }
  }
  
  val flatJson = parse("""
  {
    "name": "Batman",
    "fee_1": "12",
    "fee_2": "15",
    "fee_3": 18
  }""")

  val json = parse("""
  {
    "superName": "Batman",
    "payment": [
      { "feeInPence": 12 },
      { "feeInPence": 15 },
      { "feeInPence": 18 }
    ]
  }""")

  // Assuming successful transformation
  transform(flatJson) mustEqual Good(json) 
```

Note - if required (though not advised) the EmptyJsonSchema can be used to all JSON to be validated successfully.

JSON Schema Validation
----------------------
There are several online JSON schema validation tools such as [JSON Schema Validator](http://www.jsonschemavalidator.net/)

Alternatively, a JSON schema can be validated from the Scala REPL by doing the following:

> sbt

> console

> import uk.gov.homeoffice.json._

> import uk.gov.homeoffice.json.Json._

> jsonFromFilepath("src/test/resources/schema-test.json") map { JsonSchema(_) }

If you've given a valid file path and the schema is valid, the result will be something like:

res3: scala.util.Try[uk.gov.homeoffice.json.JsonSchema] = Success(uk.gov.homeoffice.json.JsonSchema@7568db95)