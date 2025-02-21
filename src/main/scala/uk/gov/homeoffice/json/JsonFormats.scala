package uk.gov.homeoffice.json

import org.json4s.DefaultFormats
import org.json4s.ext.JodaTimeSerializers

object JsonFormats extends JsonFormats

trait JsonFormats {
  implicit val json4sFormats = DefaultFormats ++ JodaTimeSerializers.all
}
