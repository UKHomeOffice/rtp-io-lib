package uk.gov.homeoffice

import scala.concurrent.duration.Duration

package object duration {
  implicit class DurationOps(d: Duration) {
    val toPrettyString = "%02d:%02d:%06.3f".format(d.toHours, d.toMinutes % 60, d.toMillis / 1000d % 60)
  }
}