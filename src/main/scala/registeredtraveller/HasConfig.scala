package registeredtraveller

import com.typesafe.config.ConfigFactory

trait HasConfig {
  val config = ConfigFactory.load
}