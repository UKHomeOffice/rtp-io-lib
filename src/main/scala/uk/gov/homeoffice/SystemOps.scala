package uk.gov.homeoffice

object SystemOps extends SystemOps

trait SystemOps {
  val newLine = sys.props("line.separator")
}