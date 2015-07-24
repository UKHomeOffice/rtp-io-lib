package uk.gov.homeoffice.configuration

import java.time.{Duration => JavaDuration}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.util.Try
import com.typesafe.config.{Config, ConfigValue, ConfigValueFactory}

trait ConfigFactorySupport {
  implicit val anyToConfigValue: Any => ConfigValue =
    a => ConfigValueFactory.fromAnyRef(a)

  implicit val javaDurationToFiniteDuration: JavaDuration => FiniteDuration =
    d => new FiniteDuration(d.toNanos, TimeUnit.NANOSECONDS)

  implicit val finiteDurationToJavaDuration: FiniteDuration => JavaDuration =
    f => f.asInstanceOf[JavaDuration]

  implicit class ConfiguredFiniteDuration(path: String)(implicit config: Config) {
    def seconds = new FiniteDuration(config.getDuration(path, TimeUnit.SECONDS), TimeUnit.SECONDS)
  }

  implicit class ConfigOps(config: Config) {
    def duration(path: String, default: Duration): Duration = {
      val d = Try {
        config.getDuration(path)
      } getOrElse JavaDuration.ofNanos(default.toNanos)

      d
    }

    def int(path: String, default: Int): Int = Try {
      config.getInt(path)
    } getOrElse default

    def boolean(path: String, default: Boolean): Boolean = Try {
      config.getBoolean(path)
    } getOrElse default
  }
}