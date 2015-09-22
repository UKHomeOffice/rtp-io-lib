package uk.gov.homeoffice.logging

import java.io.{ByteArrayOutputStream, PrintStream}
import org.specs2.matcher.Scope

trait ConsoleLogging {
  this: Scope =>

  val baos = new ByteArrayOutputStream

  val ps = new PrintStream(baos)

  def consoleLog = baos.toString

  def withConsoleRedirect[T](block: => T) = {
    val sysOutOriginal = System.out
    val sysErrorOriginal = System.err

    System.setOut(ps)
    System.setErr(ps)

    val result = block

    System.setOut(sysOutOriginal)
    System.setErr(sysErrorOriginal)

    result
  }
}