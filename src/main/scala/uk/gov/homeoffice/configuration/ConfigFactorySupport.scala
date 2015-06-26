package uk.gov.homeoffice.configuration

import java.time.Duration
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import com.typesafe.config.{Config, ConfigValue, ConfigValueFactory}

trait ConfigFactorySupport {
  implicit val anyToConfigValue: Any => ConfigValue =
    a => ConfigValueFactory.fromAnyRef(a)

  implicit val durationToFiniteDuration: Duration => FiniteDuration =
    d => new FiniteDuration(d.toNanos, TimeUnit.NANOSECONDS)

  implicit class ConfiguredFiniteDuration(path: String)(implicit config: Config) {
    def seconds = new FiniteDuration(config.getDuration(path, TimeUnit.SECONDS), TimeUnit.SECONDS)
  }
}