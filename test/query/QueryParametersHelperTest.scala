package query

import exceptions.InvalidQueryParamException
import models.{ArtistInfoRequest, MoviesInfoRequest, MoviesSearchRequest}
import org.scalatest.{Matchers, WordSpec}

class QueryParametersHelperTest extends WordSpec with Matchers {
  "The MoviesSearchRequest" when {
    "built from a valid query param: `?genre=Science%20Fiction&revenue=500000000&offset=0&limit=10`" should {
      "return Some(MoviesSearchRequest(Science Fiction,500000000,0,10))" in {
        val queryParams: Map[String, Seq[String]] =
          Map("genre" -> Seq("Science Fiction"),
              "revenue" -> Seq("500000000"),
              "offset" -> Seq("0"),
              "limit" -> Seq("10"))
        val moviesSearchRequest = QueryParametersHelper.apply(queryParams)

        moviesSearchRequest shouldBe Some(
          MoviesSearchRequest("Science Fiction", 500000000, 0, 10))
      }
    }

    "built from an invalid query param: `?genre=Science%20Fiction&revenue=45&offset=0&limit=10`" should {
      "return validation error" in {
        val queryParams: Map[String, Seq[String]] =
          Map("genre" -> Seq("Science Fiction"),
              "revenue" -> Seq("45"),
              "offset" -> Seq("0"),
              "limit" -> Seq("10"))

        a[IllegalArgumentException] should be thrownBy {
          QueryParametersHelper.apply(queryParams)
        }
      }
    }

    "built from an invalid query param (revenue not Int): `?genre=Science%20Fiction&revenue=cat&offset=0&limit=10`" should {
      "return validation error" in {
        val queryParams: Map[String, Seq[String]] =
          Map("genre" -> Seq("Science Fiction"),
              "revenue" -> Seq("cat"),
              "offset" -> Seq("0"),
              "limit" -> Seq("10"))

        a[InvalidQueryParamException] should be thrownBy {
          QueryParametersHelper.apply(queryParams)
        }
      }
    }
  }

  "The MovieInfoService" when {
    "creating a query param for movieInfo method" should {
      "return a valid query param" in {
        val moviesInfoRequest = MoviesInfoRequest(Seq(680, 1893, 12), 5, 0)
        val queryParams =
          QueryParametersHelper.createQueryParams(moviesInfoRequest)

        queryParams shouldBe "?ids=680,1893,12&limit=5&offset=0"
      }
    }

    "creating a query param for movieInfo method with no ids" should {
      "return an empty query param" in {
        val moviesInfoRequest = MoviesInfoRequest(Seq.empty, 5, 0)
        val queryParams =
          QueryParametersHelper.createQueryParams(moviesInfoRequest)

        queryParams shouldBe ""
      }
    }
  }

  "The ArtistInfoService" when {
    "creating a query param for artists method" should {
      "return a valid query param" in {
        val artistInfoRequest = ArtistInfoRequest(Seq(2, 3), 5, 0)
        val queryParams =
          QueryParametersHelper.createQueryParams(artistInfoRequest)

        queryParams shouldBe "?ids=2,3&limit=5&offset=0"
      }
    }

    "creating a query param for artists method with no ids" should {
      "return an empty query param" in {
        val artistInfoRequest = ArtistInfoRequest(Seq.empty, 5, 0)
        val queryParams =
          QueryParametersHelper.createQueryParams(artistInfoRequest)

        queryParams shouldBe ""
      }
    }
  }
}
