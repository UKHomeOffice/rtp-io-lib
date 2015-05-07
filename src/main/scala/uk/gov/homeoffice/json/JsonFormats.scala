package uk.gov.homeoffice.json

import org.json4s.DefaultFormats

trait JsonFormats {
  implicit val json4sFormats = DefaultFormats
}