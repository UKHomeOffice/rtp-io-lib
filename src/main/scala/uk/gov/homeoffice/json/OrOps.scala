package uk.gov.homeoffice.json

import scala.reflect.ClassTag
import org.scalactic.{Bad, Good, Or}

trait OrOps {
  implicit def or2Option[G, B](or: G Or B): Option[G] = or toOption

  def good[R, G, B](implicit ct: ClassTag[R]): G Or B => R = {
    case Good(r) => r.asInstanceOf[R]
    case Bad(JsonError(_, error, Some(t))) => throw t
    case Bad(e) => throw new Exception(s"Unexpected: $e")
  }

  def good[R, G, B](r: => R): G Or B => R = {
    case Good(_) => r
    case Bad(JsonError(_, error, Some(t))) => throw t
    case Bad(e) => throw new Exception(s"Unexpected: $e")
  }
}