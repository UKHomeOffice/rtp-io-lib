package uk.gov.homeoffice.console

import grizzled.slf4j.Logging

trait Console extends Logging {
  val present = (s: String) => info(s"***** $s *****")
}