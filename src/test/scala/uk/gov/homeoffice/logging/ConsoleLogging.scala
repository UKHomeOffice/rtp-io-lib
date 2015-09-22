package uk.gov.homeoffice.logging

import java.io.{ByteArrayOutputStream, PrintStream}
import org.specs2.mutable.Specification

trait ConsoleLogging {
  this: Specification =>

  isolated

  val sysOutOriginal = System.out
  val sysErrorOriginal = System.err

  def withConsoleLog[R](block: (() => String) => R) = {
    val baos = new ByteArrayOutputStream
    val ps = new PrintStream(baos)

    System.setOut(ps)
    System.setErr(ps)

    val consoleLog: () => String = baos.toString

    val result = block(consoleLog)

    System.setOut(sysOutOriginal)
    System.setErr(sysErrorOriginal)

    result
  }
}