package uk.gov.homeoffice.logging

import java.io.{ByteArrayOutputStream, PrintStream}
import scala.collection.mutable

trait ConsoleLogging {
  val outputStreams = mutable.Map[Thread, ByteArrayOutputStream]()

  def consoleLog = outputStreams(Thread.currentThread()).toString

  def withConsoleRedirect[T](block: => T) = {
    val baos = new ByteArrayOutputStream
    outputStreams.update(Thread.currentThread(), baos)

    val ps = new PrintStream(baos)

    val sysOutOriginal = System.out
    val sysErrorOriginal = System.err

    System.setOut(ps)
    System.setErr(ps)

    val result = block

    System.setOut(sysOutOriginal)
    System.setErr(sysErrorOriginal)
    outputStreams.remove(Thread.currentThread())

    result
  }
}