package services

import exceptions.GenericFailureCalling
import models.{Genre, Metadata, MovieSearch}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}
import services.MovieSearchService._

import scala.concurrent.Future

class MovieSearchServiceTest
    extends WordSpec
    with PrivateMethodTester
    with Matchers
    with MockitoSugar {
  private val retryHandlerMock = mock[RetryHandler]
  private val requestHandler = new RequestHandler(retryHandlerMock)

  import requestHandler._

  "The MovieSearchService" when {
    "calling validateAllGenresResponseStatus with an OK status" should {
      "return Genres results successfully" in {
        val result = validateResult[Seq[Genre]](200,
                                                """[
            {
            "id": 28,
            "name": "Action"
            }]""".stripMargin,
                                                decodeGenres)

        result.toString shouldEqual Future
          .successful(Seq(Genre(28, "Action")))
          .toString
      }
    }

    "calling validateAllGenresResponseStatus with an 500 status" should {
      "return a validation error" in {
        val resultFailure = validateResult[MovieSearch](500,
                                                        """[
            {
            "id": 28,
            "name": "Action"
            }]""",
                                                        decodeMovieSearch)
        resultFailure.toString shouldEqual Future
          .failed(new GenericFailureCalling(
            "Exception calling get all genres endpoint"))
          .toString
      }
    }

    "calling validateMovieSearchResult with an OK status" should {
      "return MovieSearch results successfully" in {
        val result = validateResult[MovieSearch](
          200,
          """{
            "metadata": {
            "offset": 0,
            "limit": 5,
            "total": 220
            },
            "data": [
            1893,
            680]
            }""",
          decodeMovieSearch
        )
        result.toString shouldEqual Future
          .successful(MovieSearch(Metadata(0, 5, 220), List(1893, 680)))
          .toString
      }
    }

    "calling validateMovieSearchResult with an 500 status" should {
      "return a validation error" in {
        val resultFailure = validateResult[MovieSearch](500,
                                                        """[
            {
            "id": 28,
            "name": "Action"
            }]""",
                                                        decodeMovieSearch)
        resultFailure.toString shouldEqual Future
          .failed(new GenericFailureCalling("Exception calling endpoint"))
          .toString
      }
    }
  }
}
