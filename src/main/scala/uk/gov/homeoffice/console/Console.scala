package uk.gov.homeoffice.console

trait Console {
  def present(s: String) = {
    val border = "***" + ("*" * s.length) + "***"
    val subBorder = "*" + (" " * (s.length + 4)) + "*"

    println(
      s"""$border
         |$subBorder
         |*  $s  *
         |$subBorder
         |$border
       """.stripMargin)
  }
}