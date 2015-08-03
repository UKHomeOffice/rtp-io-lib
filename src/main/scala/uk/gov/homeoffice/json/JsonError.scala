package uk.gov.homeoffice.json

import org.json4s.JValue
import org.json4s.JsonAST.JNothing

case class JsonError(json: JValue = JNothing, error: Option[String] = None, throwable: Option[Throwable] = None)