package uk.gov.homeoffice.pagination

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Pagination {
  def paginate[T](iterable: Future[Iterable[T]])(pageSize: Int)(page: Int): Future[Page[T]] = {
    val start = {
      val start = (page - 1) * pageSize
      if (start <= 0) 0 else start
    }

    iterable map { _.slice(start, start + pageSize) } flatMap { it =>
      if (it.isEmpty && page > 1) {
        paginate(iterable)(pageSize)(page - 1)
      } else {
        iterable map { originalIterable =>
          Page(if (page <= 0) 1 else page,
            pageSize,
            totalItems = if (originalIterable.isInstanceOf[Stream[T]]) Int.MaxValue else originalIterable.size,
            hasPrevious = page > 1,
            hasNext = if (originalIterable.isInstanceOf[Stream[T]]) true else (start + pageSize) < originalIterable.size,
            it)
        }
      }
    }
  }
}