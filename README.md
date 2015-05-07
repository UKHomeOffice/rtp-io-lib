IO - Registered Traveller
=========================
General Scala IO functionality (written for Registered Traveller but reusable) such as JSON schema validation

Project built with the following (main) technologies:

- Scala

- SBT

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