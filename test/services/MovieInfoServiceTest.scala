package services

import exceptions.GenericFailureCalling
import models._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}
import services.MovieInfoService._

import scala.concurrent.Future

class MovieInfoServiceTest
    extends WordSpec
    with PrivateMethodTester
    with Matchers
    with MockitoSugar {

  private val body =
    """{
      |   "metadata":{
      |      "offset":0,
      |      "limit":3,
      |      "total":3
      |   },
      |   "data":[
      |      {
      |         "id":680,
      |         "title":"Pulp Fiction",
      |         "tagline":"Just because you are a character doesn't mean you have character.",
      |         "overview":"A burger-loving hit man, his philosophical partner, a drug-addled gangster's moll and a washed-up boxer converge in this sprawling, comedic crime caper. Their adventures unfurl in three stories that ingeniously trip back and forth in time.",
      |         "popularity":22.685188,
      |         "runtime":154,
      |         "releaseDate":"1994-09-10",
      |         "revenue":213928762,
      |         "budget":8000000,
      |         "posterPath":"https://image.tmdb.org/t/p/w342/dM2w364MScsjFf8pfMbaWUcWrR.jpg",
      |         "originalLanguage":"en",
      |         "genres":[
      |            53,
      |            80
      |         ],
      |         "cast":[
      |            8891,
      |            2231,
      |            139,
      |            62,
      |            10182,
      |            1037,
      |            7036
      |         ]
      |      }
      |   ]
      |}""".stripMargin

  private val retryHandlerMock = mock[RetryHandler]
  private val requestHandler = new RequestHandler(retryHandlerMock)

  import requestHandler._

  "calling validateMovieInfoResponse with an OK status" should {
    "return MovieInfo results successfully" in {
      val result = validateResult[MoviesInfo](200, body, decoder)
      result.toString shouldBe Future
        .successful(MoviesInfo(
          Metadata(0, 3, 3),
          List(MovieInfo(
            680,
            "Pulp Fiction",
            "Just because you are a character doesn't mean you have character.",
            "A burger-loving hit man, his philosophical partner, a drug-addled gangster's moll and a washed-up boxer converge in this sprawling, comedic crime caper. Their adventures unfurl in three stories that ingeniously trip back and forth in time.",
            22.685188F,
            154,
            "1994-09-10",
            213928762,
            8000000,
            "https://image.tmdb.org/t/p/w342/dM2w364MScsjFf8pfMbaWUcWrR.jpg",
            "en",
            List(53, 80),
            List(8891, 2231, 139, 62, 10182, 1037, 7036)
          ))
        ))
        .toString

    }
  }

  "calling validateMovieSearchResult with an 500 status" should {
    "return a validation error" in {
      val resultFailure =
        validateResult[MoviesInfo](500, """{"bla"}""", decoder)
      resultFailure.toString shouldEqual Future
        .failed(
          new GenericFailureCalling("Exception calling movie info endpoint"))
        .toString
    }
  }
}
