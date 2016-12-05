package uk.gov.homeoffice.pagination

import scala.concurrent.Future
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification

class PaginationSpec(implicit env: ExecutionEnv) extends Specification with Pagination {
  "Pagination" should {
    "give one empty page" in {
      paginate(Future successful Seq.empty[String])(pageSize = 10)(page = 1) must beEqualTo {
        Page(number = 1, pageSize = 10, totalItems = 0, hasPrevious = false, hasNext = false, iterable = Nil)
      }.await
    }

    "give one empty page, then trying to move to previous page will stay on current empty page" in {
      val page = paginate(Future successful Seq.empty[String])(pageSize = 10) _

      page(1) must beEqualTo(Page(number = 1, pageSize = 10, totalItems = 0, hasPrevious = false, hasNext = false, iterable = Nil)).await
      page(0) must beEqualTo(Page(number = 1, pageSize = 10, totalItems = 0, hasPrevious = false, hasNext = false, iterable = Nil)).await
    }

    "give one empty page, then trying to advance to next page will stay on current empty page" in {
      val page = paginate(Future successful Seq.empty[String])(pageSize = 10) _

      page(1) must beEqualTo(Page(number = 1, pageSize = 10, totalItems = 0, hasPrevious = false, hasNext = false, iterable = Nil)).await
      page(2) must beEqualTo(Page(number = 1, pageSize = 10, totalItems = 0, hasPrevious = false, hasNext = false, iterable = Nil)).await
    }

    "give one page of strings" in {
      val strings = Seq("A", "B", "C")

      paginate(Future successful strings)(pageSize = 10)(page = 1) must beEqualTo {
        Page(1, pageSize = 10, totalItems = 3, hasPrevious = false, hasNext = false, strings)
      }.await
    }

    "give one page of strings, then trying to move to previous page will stay on current page" in {
      val strings = Seq("A", "B", "C")

      val page = paginate(Future successful Seq("A", "B", "C"))(pageSize = 10) _

      val resultingPage = Page(1, pageSize = 10, totalItems = 3, hasPrevious = false, hasNext = false, strings)

      page(1) must beEqualTo(resultingPage).await
      page(0) must beEqualTo(resultingPage).await
    }

    "give one page of strings, then trying to move to non existing, in this case negative page, will stay on current page" in {
      val strings = Seq("A", "B", "C")

      val page = paginate(Future successful Seq("A", "B", "C"))(pageSize = 10) _

      val resultingPage = Page(1, pageSize = 10, totalItems = 3, hasPrevious = false, hasNext = false, strings)

      page(1) must beEqualTo(resultingPage).await
      page(-10) must beEqualTo(resultingPage).await
    }

    "give one page of strings, then trying to advance to next page will stay on current page" in {
      val strings = Seq("A", "B", "C")

      val page = paginate(Future successful strings)(pageSize = 10) _

      page(1) must beEqualTo(Page(1, pageSize = 10, totalItems = 3, hasPrevious = false, hasNext = false, strings)).await
      page(2) must beEqualTo(Page(1, pageSize = 10, totalItems = 3, hasPrevious = false, hasNext = false, strings)).await
    }

    "give one page of 3 strings, followed by another page of 2 strings" in {
      val page = paginate(Future successful Seq("A", "B", "C", "D", "E"))(pageSize = 3) _

      page(1) must beEqualTo(Page(1, pageSize = 3, totalItems = 5, hasPrevious = false, hasNext = true, Seq("A", "B", "C"))).await
      page(2) must beEqualTo(Page(2, pageSize = 3,  totalItems = 5, hasPrevious = true, hasNext = false, Seq("D", "E"))).await
    }

    "give one page of 3 strings, followed by another page of 2 strings, followed by staying on this last page when attempting to advance again" in {
      val page = paginate(Future successful Seq("A", "B", "C", "D", "E"))(pageSize = 3) _

      page(1) must beEqualTo(Page(1, pageSize = 3, totalItems = 5, hasPrevious = false, hasNext = true, Seq("A", "B", "C"))).await
      page(2) must beEqualTo(Page(2, pageSize = 3, totalItems = 5, hasPrevious = true, hasNext = false, Seq("D", "E"))).await
      page(3) must beEqualTo(Page(2, pageSize = 3, totalItems = 5, hasPrevious = true, hasNext = false, Seq("D", "E"))).await
    }

    "give one page of 1 custom object, followed by a page of 2 custom objects when changing page size" in {
      case class Test(id: String)

      val pages = paginate(Future successful Seq(Test("A"), Test("B"), Test("C"), Test("D"))) _

      pages(1)(1) must beEqualTo(Page(1, pageSize = 1, totalItems = 4, hasPrevious = false, hasNext = true, Seq(Test("A")))).await
      pages(2)(2) must beEqualTo(Page(2, pageSize = 2, totalItems = 4, hasPrevious = true, hasNext = false, Seq(Test("C"), Test("D")))).await
    }
  }
}