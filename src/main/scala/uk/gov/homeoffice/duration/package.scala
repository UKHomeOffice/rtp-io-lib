package uk.gov.homeoffice

import scala.concurrent.duration.Duration

package object duration {
  implicit class DurationOps(d: Duration) {
    val toPrettyString = {
      def format(period: Long, periodType: String) = period match {
        case 0 => ""
        case 1 => s"1 $periodType, "
        case x => s"$x ${periodType}s, "
      }

      (format(d.toDays, "day") + format(d.toHours % 24, "hour") + format(d.toMinutes % 60, "minute") + format(d.toSeconds % 60, "second")).replaceAll(", $", "")
    }
  }
}