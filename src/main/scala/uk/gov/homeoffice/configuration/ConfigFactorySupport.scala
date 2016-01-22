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

  implicit val durationToFiniteDuration: Duration => FiniteDuration =
    d => new FiniteDuration(d.toNanos, TimeUnit.NANOSECONDS)

  implicit val finiteDurationToJavaDuration: FiniteDuration => JavaDuration =
    f => f.asInstanceOf[JavaDuration]

  implicit class ConfiguredFiniteDuration(path: String)(implicit config: Config) {
    def seconds = new FiniteDuration(config.getDuration(path, TimeUnit.SECONDS), TimeUnit.SECONDS)
  }

  implicit class ConfigOps(config: Config) {
    val duration = (path: String, default: Duration) => property(path, JavaDuration.ofNanos(default.toNanos), _.getDuration)

    val text = (path: String, default: String) => property(path, default, _.getString)

    val int = (path: String, default: Int) => property(path, default, _.getInt)

    val boolean = (path: String, default: Boolean) => property(path, default, _.getBoolean)

    private def property[P, D](path: P, default: D, f: Config => P => D) = Try {
      f(config)(path)
    } getOrElse default
  }
}