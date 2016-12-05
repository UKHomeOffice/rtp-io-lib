package uk.gov.homeoffice.pagination

case class Page[T](number: Int, pageSize: Int, totalItems: Int, hasPrevious: Boolean, hasNext: Boolean, iterable: Iterable[T])