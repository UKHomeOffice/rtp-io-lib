package uk.gov.homeoffice.io

import java.net.{BindException, ServerSocket}
import grizzled.slf4j.Logging

trait Network extends Logging {
  type Port = Int

  /**
    * Get a free port on the machine that this functionality is running on.
    * Because of possible race conditions, there is a retry policy e.g.
    * even when finding a free port, until it is actually used, another thread could come in and steal said port.
    * @param retries Int The maximum number of retries to perform to use a free port.
    * @param f: Port => R Client code's function to be run with an available port.
    * @tparam R Resulting type of the given function f
    * @return R Result of running the given function f
    */
  def freeport[R](retries: Int = 5)(f: Port => R): R = {
    require(retries >= 0)

    def port: Int = {
      val port = new ServerSocket(0).getLocalPort
      info(s"Attempting to use free port $port")
      port
    }

    def freeport(retries: Int = 5, retry: Int)(f: Port => R): R = try {
      f(port)
    } catch {
      case e: BindException if retry < retries => freeport(retries, retry + 1)(f)
    }

    freeport(retries, 0)(f)
  }
}