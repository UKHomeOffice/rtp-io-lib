package uk.gov.homeoffice.configuration

import com.typesafe.config.ConfigFactory

trait HasConfig extends ConfigFactorySupport {
  implicit val config = ConfigFactory.load
}