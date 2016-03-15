package uk.gov.homeoffice

package object io {
  implicit def toResource[R](r: R): Resource[R] = Resource(r)
}