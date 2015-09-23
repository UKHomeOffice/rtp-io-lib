package uk.gov.homeoffice.logging

import java.io.{ByteArrayOutputStream, PrintStream}
import org.specs2.mutable.Specification

trait ConsoleLogging {
  this: Specification =>

  isolated

  type ConsoleLog = () => String

  val sysOutOriginal = System.out
  val sysErrorOriginal = System.err

  def withConsoleLog[R](block: ConsoleLog => R) = {
    val baos = new ByteArrayOutputStream
    val consoleLog: ConsoleLog = baos.toString
    val ps = new PrintStream(baos)

    System.setOut(ps)
    System.setErr(ps)

    val result = block(consoleLog)

    System.setOut(sysOutOriginal)
    System.setErr(sysErrorOriginal)

    result
  }
}