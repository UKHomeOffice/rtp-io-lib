package uk.gov.homeoffice.io

import java.net.{BindException, ServerSocket}
import grizzled.slf4j.Logging

trait Network extends Logging {
  type Port = Int

  def freeport[R](retries: Int = 5)(f: Port => R): R = {
    require(retries >= 0)

    def freeport(retries: Int = 5, retry: Int)(f: Port => R): R = try {
      f(port)
    } catch {
      case e: BindException if retry < retries => freeport(retries, retry + 1)(f)
    }

    freeport(retries, 0)(f)
  }

  def port: Int = {
    val port = new ServerSocket(0).getLocalPort
    info(s"Attempting to use free port $port")
    port
  }
}