package uk.gov.homeoffice.or

import org.scalactic.{Bad, Good, Or}

trait OrOps {
  def good[R, G, B]: G Or B => R Or B = {
    case Good(r: R) => Good(r)
    case Bad(b) => Bad(b)
  }

  def good[R, G, B](r: => R): G Or B => R Or B = {
    case Good(_) => Good(r)
    case Bad(b) => Bad(b)
  }
}