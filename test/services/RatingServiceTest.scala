package services

import akka.stream.Materializer
import akka.util.ByteString
import models._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

class RatingServiceTest extends WordSpec with Matchers with MockitoSugar {
  "The RatingService" when {
    implicit val ecMock: ExecutionContext = mock[ExecutionContext]
    implicit val materializerMock: Materializer = mock[Materializer]
    val wsClient = mock[WSClient]

    "decoding streamed bytes to a Rating successfully" should {
      "return a valid Rating" in {
        val bytes = ByteString("data: {\"id\":510819,\"rating\":7.4184895}")
        val ratingService = new RatingService(wsClient)
        ratingService.decodeRatingMessage(bytes) shouldEqual Some(Rating(510819, 7.4184895f))
      }
    }

    "decoding streamed bytes in incorrect format to a Rating" should {
      "return no rating" in {
        val bytes = ByteString("cat: {\"id\":510819,\"rating\":7.4184895}")
        val ratingService = new RatingService(wsClient)
        ratingService.decodeRatingMessage(bytes) shouldEqual None
      }
    }

    "decoding no streamed bytes to a Rating" should {
      "return no rating" in {
        val bytes = ByteString("")
        val ratingService = new RatingService(wsClient)
        ratingService.decodeRatingMessage(bytes) shouldEqual None
      }
    }

    "calling ratingsAverage for some valid messages with movie ratings" should {
      "return a list of distinct Ratings average" in {
        val ratingService = new RatingService(wsClient)

        val ratingLinesUnfiltered: Seq[String] = Seq(
          """data: {"id":300668,"rating":1.7326623}""",
          """data: {"id":300668,"rating":2.7850761}""",
          """data: {"id":483877,"rating":2.535405}""")

        ratingService.ratingsAverage(ratingLinesUnfiltered) shouldEqual Seq(Rating(300668, 2.2588692f), Rating(483877, 2.535405f))
      }
    }

    "calling ratingsAverage for some valid messages with movie ratings with no duplicate ids" should {
      "return a list of Ratings not aggregated" in {
        val ratingService = new RatingService(wsClient)

        val ratingLinesUnfiltered: Seq[String] = Seq(
          """data: {"id":300668,"rating":1.7326623}""",
          """data: {"id":300665,"rating":2.8850761}""",
          """data: {"id":483877,"rating":2.535405}""")

        ratingService.ratingsAverage(ratingLinesUnfiltered) shouldEqual Seq(Rating(300668, 1.7326623f), Rating(300665, 2.8850761f), Rating(483877, 2.535405f))
      }
    }

    "calling ratingsAverage for some invalid messages and a valid message" should {
      "return a list with only the valid Rating" in {
        val ratingService = new RatingService(wsClient)

        val ratingLinesUnfiltered: Seq[String] = Seq(
          """data: {"cat":300668,"rating":1.7326623}""",
          """data1: {"id":300665,"rating":2.8850761}""",
          """data: {"id":483877,"ratingDog":1.535405}""")

        ratingService.ratingsAverage(ratingLinesUnfiltered) shouldEqual Seq(Rating(300665, 2.885076f))
      }
    }

    "calling ratingsAverage with valid message and an empty line" should {
      "return a list with the valid Rating" in {
        val ratingService = new RatingService(wsClient)

        val ratingLinesUnfiltered: Seq[String] = Seq(
          """\n""",
          """data: {"id":483877,"rating":1.535405}""")

        ratingService.ratingsAverage(ratingLinesUnfiltered) shouldEqual Seq(Rating(483877, 1.535405f))
      }
    }

    "calling isBellowMedianRating for a movie id" should {
      "return true if movie is rated bellow the average movie ratings" in {
        val ratingService = new RatingService(wsClient)
        val ratings = Seq(Rating(300668, 1.7326623f), Rating(300665, 2.8850761f), Rating(483877, 2.535405f))

        ratingService.isBellowMedianRating(300668, ratings) shouldEqual true
      }
    }

    "calling isBellowMedianRating for a movie id" should {
      "return false if movie is rated above the average movie ratings" in {
        val ratingService = new RatingService(wsClient)
        val ratings = Seq(Rating(300668, 1.7326623f), Rating(300665, 2.8850761f), Rating(483877, 2.535405f))

        ratingService.isBellowMedianRating(300665, ratings) shouldEqual false
      }
    }

    "calling isBellowMedianRating for a movie id not existing in the collection" should {
      "return false" in {
        val ratingService = new RatingService(wsClient)
        val ratings = Seq(Rating(300668, 1.7326623f), Rating(300665, 2.8850761f), Rating(483877, 2.535405f))

        ratingService.isBellowMedianRating(1, ratings) shouldEqual true
      }
    }

    "calling isBellowMedianRating for a movie id if no ratings available" should {
      "return false" in {
        val ratingService = new RatingService(wsClient)
        val ratings = Seq.empty

        ratingService.isBellowMedianRating(1, ratings) shouldEqual true
      }
    }

    "calling ratingAverageInfo for a movie id" should {
      "return a RatingAverageInfo" in {
        val ratingService = new RatingService(wsClient)
        val ratings = Seq(Rating(300668, 1.7326623f), Rating(300665, 2.8850761f), Rating(483877, 2.535405f))

        ratingService.ratingAverageInfo(300668, ratings) shouldEqual Some(RatingAverageInfo(Rating(300668, 1.7326623f), isBellowMedianAverage = true))
      }
    }

    "calling ratingAverageInfo for a movie id not existing in collection" should {
      "return no RatingAverageInfo" in {
        val ratingService = new RatingService(wsClient)
        val ratings = Seq(Rating(300668, 1.7326623f), Rating(300665, 2.8850761f), Rating(483877, 2.535405f))

        ratingService.ratingAverageInfo(2, ratings) shouldEqual None
      }
    }

    "calling ratingAverageInfo for a movie id with an empty movie rating collection" should {
      "return no RatingAverageInfo" in {
        val ratingService = new RatingService(wsClient)
        val ratings = Seq.empty

        ratingService.ratingAverageInfo(2, ratings) shouldEqual None
      }
    }
  }
}