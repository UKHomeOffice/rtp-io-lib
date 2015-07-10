package uk.gov.homeoffice.json

import org.json4s.DefaultFormats
import org.json4s.ext.JodaTimeSerializers

trait JsonFormats {
  implicit val json4sFormats = DefaultFormats ++ JodaTimeSerializers.all
}