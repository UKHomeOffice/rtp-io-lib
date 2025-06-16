package uk.gov.homeoffice

import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

package object datetime {
  val `yyyy-MM-dd` = DateTimeFormat forPattern "yyyy-MM-dd"

  val `yyyy-dd-MM` = DateTimeFormat forPattern "yyyy-dd-MM"

  val `yyyy-MM-dd HH:mm:ss` = DateTimeFormat forPattern "yyyy-MM-dd HH:mm:ss"

}
