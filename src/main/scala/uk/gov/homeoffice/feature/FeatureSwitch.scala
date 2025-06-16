package uk.gov.homeoffice.feature

import grizzled.slf4j.Logging
import uk.gov.homeoffice.configuration.HasConfig
import scala.util.Try

/**
  * Run functionality according to whether a configured "feature" is switched on.
  * The configuration of a feature is switched on by setting it to 'on' or 'true' e.g.
  * application.conf has:
  * my-feature = on
  * OR
  * my-feature = true
  */
trait FeatureSwitch extends HasConfig with Logging {
  /**
    * Functionality is run according to configuration.
    * @param feature String Your feature that is configured to run (or not) your functionality.
    * @param default Boolean Whether the feature is on or off by default when said feature switch is not configured
    * @param functionality R Your functionality that produces some result R.
    * @tparam R Result type that your functionality gives back as a result.
    * @return Option of the outcome of running your functionality.
    *         If the feature is not configured 'on', then a None will be the outcome, otherwise the result will be wrapped in a Some.
    */
  def withFeature[R](feature: String, default: Boolean = false)(functionality: => R): Option[R] = {
    val featureOn = Try(config.getBoolean(feature)).getOrElse(default)

    if(featureOn) {
      info(s"Running Feature: $feature")
      Some(functionality)
    } else {
      None
    }
  }
}
