package uk.gov.homeoffice

import com.typesafe.config.ConfigFactory

trait HasConfig {
  val config = ConfigFactory.load
}