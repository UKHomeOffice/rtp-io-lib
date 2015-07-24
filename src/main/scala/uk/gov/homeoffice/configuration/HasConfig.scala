package uk.gov.homeoffice.configuration

import com.typesafe.config.ConfigFactory

trait HasConfig extends ConfigFactorySupport {
  val config = ConfigFactory.load
}