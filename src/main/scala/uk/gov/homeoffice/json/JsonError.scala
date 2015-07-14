package uk.gov.homeoffice.json

import org.json4s.JValue
import org.json4s.JsonAST.JNothing

case class JsonError(json: JValue = JNothing, error: String, throwable: Option[Throwable] = None)