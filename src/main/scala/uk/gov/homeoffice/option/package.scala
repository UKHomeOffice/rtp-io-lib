package uk.gov.homeoffice

package object option {
  implicit class OptionOps[A : Ordering](o: Option[A]) {
    def <(other: Option[A]) = (o, other) match {
      case (None, None) => false
      case (None, _) => true
      case (_, None) => false
      case (Some(oItem), Some(otherItem)) => implicitly[Ordering[A]].lt(oItem, otherItem)
    }
  }
}