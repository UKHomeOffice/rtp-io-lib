IO - Scala
==========
General Scala IO functionality (originally written for Registered Traveller UK, but reusable) such as JSON schema validation

Project built with the following (main) technologies:

- Scala

- SBT

- Json4s

Introduction
------------
TODO

Build and Deploy
----------------
The project is built with SBT (using Activator on top).

To compile:
> activator compile

To run the specs:
> activator test

To run integration specs:
> activator it:test

The project can be "assembled" into a "one JAR":
> activator assembly

Note that "assembly" will first compile and test.

Publishing
----------
To publish the jar to artifactory you will need to 

1. Copy the .credentials file into your <home directory>/.ivy2/
2. Edit this .credentials file to fill in the artifactory user and password

> activator publish

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
      val (_, newJson) = mapArray("fee" -> "payment.feeInPence", field => JInt(BigInt(field.extract[String])))(json, JNothing)
      Good(newJson)
    }
  }
  
  val flatJson = parse("""
  {
    "fee_1": "12",
    "fee_2": "15",
    "fee_3": 18
  }""")

  val json = parse("""
  {
    "payment": [
      { "feeInPence": 12 },
      { "feeInPence": 15 },
      { "feeInPence": 18 }
    ]
  }""")

  val Good(result) = transform(flatJson) // Assuming successful transformation
```