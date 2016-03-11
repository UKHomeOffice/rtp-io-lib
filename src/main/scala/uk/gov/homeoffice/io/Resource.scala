package uk.gov.homeoffice.io

case class Resource[R](r: R) {
  def to[T](implicit ev: Readable[R, T]) = implicitly[Readable[R, T]].read(r)
}