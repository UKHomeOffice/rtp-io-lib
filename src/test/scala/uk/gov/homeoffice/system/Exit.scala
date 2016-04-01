package uk.gov.homeoffice.system

trait Exit {
  def exitAfter[R](f: => R) = {
    val result = f
    sys.exit()
    result
  }
}