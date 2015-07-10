package uk.gov.homeoffice.console

trait Console {
  def present(s: String) = {
    val border = "***" + s.map(_ => "*").mkString + "***"
    val subBorder = "*" + border.map(_ => " ").drop(2).mkString + "*"

    println(
      s"""$border
         |$subBorder
         |*  $s  *
         |$subBorder
         |$border
       """.stripMargin)
  }
}