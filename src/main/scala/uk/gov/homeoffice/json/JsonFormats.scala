package uk.gov.homeoffice.json

import org.json4s.DefaultFormats
import org.json4s.ext.JodaTimeSerializers
import org.json4s.mongo.ObjectIdSerializer

object JsonFormats extends JsonFormats

trait JsonFormats {
  implicit val json4sFormats = DefaultFormats + new ObjectIdSerializer ++ JodaTimeSerializers.all
}