package uk.gov.homeoffice.resource

object CloseableResource {
  def using[A, B <: { def close(): Any }](closeable: B)(f: B => A): A = try f(closeable) finally closeable.close()
}