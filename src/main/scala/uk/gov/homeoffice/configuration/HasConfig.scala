package uk.gov.homeoffice.configuration

import com.typesafe.config.{Config, ConfigFactory}

trait HasConfig {
  implicit val config :Config = ConfigFactory.load
}
