package uk.gov.homeoffice.logging

import java.io.{PrintStream, ByteArrayOutputStream}
import grizzled.slf4j.Logging

trait ConsoleLogging {
  this: Logging =>

  val baos = new ByteArrayOutputStream
  val ps = new PrintStream(baos)

  System.setOut(ps)
  System.setErr(ps)

  def consoleLog: String = baos.toString
}