package uk.gov.homeoffice

import com.github.nscala_time.time.Imports._
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

package object datetime {
  val `yyyy-MM-dd` = DateTimeFormat forPattern "yyyy-MM-dd"

  val `yyyy-dd-MM` = DateTimeFormat forPattern "yyyy-dd-MM"

  val `yyyy-MM-dd HH:mm:ss` = DateTimeFormat forPattern "yyyy-MM-dd HH:mm:ss"

  implicit class StringWithDateOps(s: String) {
    def as(formatter: DateTimeFormatter): DateTime = formatter parseDateTime s
  }
}