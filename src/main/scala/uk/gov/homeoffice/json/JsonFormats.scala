package uk.gov.homeoffice.json

import org.json4s.{DefaultFormats, Formats}
import org.json4s.ext.JodaTimeSerializers

object JsonFormats extends JsonFormats

trait JsonFormats {
  implicit val json4sFormats :Formats = DefaultFormats ++ JodaTimeSerializers.all
}
