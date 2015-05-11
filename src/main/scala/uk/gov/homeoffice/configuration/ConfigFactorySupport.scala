package uk.gov.homeoffice.configuration

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import com.typesafe.config.{Config, ConfigValue, ConfigValueFactory}

trait ConfigFactorySupport {
  implicit val anyToConfigValue: Any => ConfigValue =
    a => ConfigValueFactory.fromAnyRef(a)

  implicit class ConfiguredFiniteDuration(path: String)(implicit config: Config) {
    def seconds = new FiniteDuration(config.getDuration(path, TimeUnit.SECONDS), TimeUnit.SECONDS)
  }
}