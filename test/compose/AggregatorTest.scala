package compose

import akka.actor.ActorSystem
import akka.stream.Materializer
import exceptions.{FailureDecodingJson, InvalidQueryParamException}
import models._
import org.mockito.Mockito._
import org.scalatest.concurrent.{Eventually, Futures}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, Matchers}
import play.api.libs.ws.WSClient
import services.{
  ArtistInfoService,
  MovieInfoService,
  MovieSearchService,
  RatingService
}

import scala.concurrent.{ExecutionContext, Future}

class AggregatorTest
    extends AsyncWordSpec
    with Matchers
    with MockitoSugar
    with Eventually
    with Futures {
  implicit val matMock: Materializer = mock[Materializer]
  implicit val systemMock: ActorSystem = mock[ActorSystem]
  implicit val wsClientMock: WSClient = mock[WSClient]

  val moviesInfo = MoviesInfo(Metadata(0, 5, 10),
                              Seq(
                                MovieInfo(1,
                                          "SW",
                                          "test",
                                          "overview",
                                          1F,
                                          1,
                                          "2011-10-11",
                                          1,
                                          1,
                                          "poster",
                                          "en",
                                          Seq(1, 2),
                                          Seq(1, 2))))

  "The Aggregator" when {
    implicit val executionContext: ExecutionContext =
      ExecutionContext.Implicits.global

    "when calling successfully downstream services" should {
      "return the movie details JSON" in {
        val movieSearchServiceMock = mock[MovieSearchService]
        val movieInfoServiceMock = mock[MovieInfoService]
        val artistInfoServiceMock = mock[ArtistInfoService]
        val ratingServiceMock = mock[RatingService]

        val moviesSearchRequest = MoviesSearchRequest("Action", 500000001, 0, 5)
        when(movieSearchServiceMock.searchMovies(moviesSearchRequest))
          .thenReturn(
            Future.successful(MovieSearch(Metadata(0, 5, 10), Seq(1, 2))))

        when(
          movieInfoServiceMock.moviesInfo(MoviesInfoRequest(Seq(1, 2), 5, 0)))
          .thenReturn(Future.successful(moviesInfo))

        when(artistInfoServiceMock.artistInfos(Seq(1, 2))).thenReturn(
          Future.successful(
            Seq(ArtistInfos(Metadata(0, 5, 10),
                            Seq(ArtistInfo("Neil", 1, "path", Seq(1, 2), 1),
                                ArtistInfo("Mary", 1, "path", Seq(2), 12))))))
        when(ratingServiceMock.ratingAverageInfos(moviesInfo)).thenReturn(
          Seq(RatingAverageInfo(Rating(1, 4.5f), isBellowMedianAverage = false),
              RatingAverageInfo(Rating(2, 3.4f), isBellowMedianAverage = true)))
        when(movieSearchServiceMock.getAllGenres).thenReturn(
          Future.successful(Seq(Genre(1, "Action"), Genre(2, "SF"))))

        val aggregator = new Aggregator(movieSearchServiceMock,
                                        movieInfoServiceMock,
                                        artistInfoServiceMock,
                                        ratingServiceMock)
        val moviesDetailJsonFuture = aggregator.aggregate(moviesSearchRequest)
        val expectedMoviesDetailJson =
          """{
            |  "data" : {
            |    "movieDetail" : [
            |      {
            |        "id" : 1,
            |        "title" : "SW",
            |        "averageRating" : 4.5,
            |        "ratedBelowMedian" : false,
            |        "runtime" : 1,
            |        "releaseDate" : "2011-10-11",
            |        "revenue" : 1,
            |        "posterPath" : "poster",
            |        "originalLanguage" : "en",
            |        "genres" : [
            |          "Action",
            |          "SF"
            |        ],
            |        "cast" : [
            |          {
            |            "id" : 1,
            |            "gender" : 1,
            |            "name" : "Neil",
            |            "profilePath" : "path"
            |          }
            |        ]
            |      }
            |    ]
            |  }
            |}""".stripMargin

        moviesDetailJsonFuture.map { json =>
          assert(json == expectedMoviesDetailJson)
        }
      }
    }

    "when calling successfully downstream services with 1 failure" should {
      "return a generic failure" in {
        val movieSearchServiceMock = mock[MovieSearchService]
        val movieInfoServiceMock = mock[MovieInfoService]
        val artistInfoServiceMock = mock[ArtistInfoService]
        val ratingServiceMock = mock[RatingService]

        val moviesSearchRequest = MoviesSearchRequest("Action", 500000001, 0, 5)
        when(movieSearchServiceMock.searchMovies(moviesSearchRequest))
          .thenReturn(
            Future.successful(MovieSearch(Metadata(0, 5, 10), Seq(1, 2))))

        when(
          movieInfoServiceMock.moviesInfo(MoviesInfoRequest(Seq(1, 2), 5, 0)))
          .thenReturn(Future.successful(moviesInfo))

        when(artistInfoServiceMock.artistInfos(Seq(1, 2))).thenReturn(
          Future.successful(
            Seq(ArtistInfos(Metadata(0, 5, 10),
                            Seq(ArtistInfo("Neil", 1, "path", Seq(1, 2), 1),
                                ArtistInfo("Mary", 1, "path", Seq(2), 12))))))
        when(ratingServiceMock.ratingAverageInfos(moviesInfo)).thenReturn(
          Seq(RatingAverageInfo(Rating(1, 4.5f), isBellowMedianAverage = false),
              RatingAverageInfo(Rating(2, 3.4f), isBellowMedianAverage = true)))
        when(movieSearchServiceMock.getAllGenres)
          .thenReturn(Future.failed(new Exception("bam!")))

        val aggregator = new Aggregator(movieSearchServiceMock,
                                        movieInfoServiceMock,
                                        artistInfoServiceMock,
                                        ratingServiceMock)
        val moviesDetailJsonFuture = aggregator.aggregate(moviesSearchRequest)

        moviesDetailJsonFuture.map { json =>
          assert(
            json == "Generic failure calling downstream service: Exception: bam!")
        }
      }
    }

    "when calling successfully downstream services with 1 failure due to json decoding" should {
      "return a generic failure" in {
        val movieSearchServiceMock = mock[MovieSearchService]
        val movieInfoServiceMock = mock[MovieInfoService]
        val artistInfoServiceMock = mock[ArtistInfoService]
        val ratingServiceMock = mock[RatingService]

        val moviesSearchRequest = MoviesSearchRequest("Action", 500000001, 0, 5)
        when(movieSearchServiceMock.searchMovies(moviesSearchRequest))
          .thenReturn(
            Future.successful(MovieSearch(Metadata(0, 5, 10), Seq(1, 2))))

        when(
          movieInfoServiceMock.moviesInfo(MoviesInfoRequest(Seq(1, 2), 5, 0)))
          .thenReturn(Future.successful(moviesInfo))

        when(artistInfoServiceMock.artistInfos(Seq(1, 2))).thenReturn(
          Future.successful(
            Seq(ArtistInfos(Metadata(0, 5, 10),
                            Seq(ArtistInfo("Neil", 1, "path", Seq(1, 2), 1),
                                ArtistInfo("Mary", 1, "path", Seq(2), 12))))))
        when(ratingServiceMock.ratingAverageInfos(moviesInfo)).thenReturn(
          Seq(RatingAverageInfo(Rating(1, 4.5f), isBellowMedianAverage = false),
              RatingAverageInfo(Rating(2, 3.4f), isBellowMedianAverage = true)))

        when(movieSearchServiceMock.getAllGenres)
          .thenReturn(Future.failed(new FailureDecodingJson("bam!")))

        val aggregator = new Aggregator(movieSearchServiceMock,
                                        movieInfoServiceMock,
                                        artistInfoServiceMock,
                                        ratingServiceMock)
        val moviesDetailJsonFuture = aggregator.aggregate(moviesSearchRequest)

        moviesDetailJsonFuture.map { json =>
          assert(json == "Failure decoding json: FailureDecodingJson: null")
        }
      }
    }

    "when calling successfully downstream services with 1 failure due to invalid query params" should {
      "return a generic failure suggesting invalid params" in {
        val movieSearchServiceMock = mock[MovieSearchService]
        val movieInfoServiceMock = mock[MovieInfoService]
        val artistInfoServiceMock = mock[ArtistInfoService]
        val ratingServiceMock = mock[RatingService]

        val moviesSearchRequest = MoviesSearchRequest("Action", 500000001, 0, 5)
        when(movieSearchServiceMock.searchMovies(moviesSearchRequest))
          .thenReturn(
            Future.successful(MovieSearch(Metadata(0, 5, 10), Seq(1, 2))))

        when(
          movieInfoServiceMock.moviesInfo(MoviesInfoRequest(Seq(1, 2), 5, 0)))
          .thenReturn(Future.successful(moviesInfo))

        when(artistInfoServiceMock.artistInfos(Seq(1, 2))).thenReturn(
          Future.successful(
            Seq(ArtistInfos(Metadata(0, 5, 10),
                            Seq(ArtistInfo("Neil", 1, "path", Seq(1, 2), 1),
                                ArtistInfo("Mary", 1, "path", Seq(2), 12))))))
        when(ratingServiceMock.ratingAverageInfos(moviesInfo)).thenReturn(
          Seq(RatingAverageInfo(Rating(1, 4.5f), isBellowMedianAverage = false),
              RatingAverageInfo(Rating(2, 3.4f), isBellowMedianAverage = true)))

        when(movieSearchServiceMock.getAllGenres)
          .thenReturn(Future.failed(new InvalidQueryParamException("bam!")))

        val aggregator = new Aggregator(movieSearchServiceMock,
                                        movieInfoServiceMock,
                                        artistInfoServiceMock,
                                        ratingServiceMock)
        val moviesDetailJsonFuture = aggregator.aggregate(moviesSearchRequest)

        moviesDetailJsonFuture.map { json =>
          assert(
            json == "Failure calling downstream service due to invalid query parameters supplied: InvalidQueryParamException: null")
        }
      }
    }

    "when calling successfully downstream movie info service with 1 failure due to invalid query params" should {
      "return a generic failure suggesting invalid params" in {
        val movieSearchServiceMock = mock[MovieSearchService]
        val movieInfoServiceMock = mock[MovieInfoService]
        val artistInfoServiceMock = mock[ArtistInfoService]
        val ratingServiceMock = mock[RatingService]

        val moviesSearchRequest = MoviesSearchRequest("Action", 500000001, 0, 5)
        when(movieSearchServiceMock.searchMovies(moviesSearchRequest))
          .thenReturn(
            Future.successful(MovieSearch(Metadata(0, 5, 10), Seq(1, 2))))

        when(artistInfoServiceMock.artistInfos(Seq(1, 2))).thenReturn(
          Future.successful(
            Seq(ArtistInfos(Metadata(0, 5, 10),
                            Seq(ArtistInfo("Neil", 1, "path", Seq(1, 2), 1),
                                ArtistInfo("Mary", 1, "path", Seq(2), 12))))))
        when(ratingServiceMock.ratingAverageInfos(moviesInfo)).thenReturn(
          Seq(RatingAverageInfo(Rating(1, 4.5f), isBellowMedianAverage = false),
              RatingAverageInfo(Rating(2, 3.4f), isBellowMedianAverage = true)))

        when(movieSearchServiceMock.getAllGenres).thenReturn(
          Future.successful(Seq(Genre(1, "Action"), Genre(2, "SF"))))
        when(
          movieInfoServiceMock.moviesInfo(MoviesInfoRequest(Seq(1, 2), 5, 0)))
          .thenReturn(Future.failed(new InvalidQueryParamException("bam!")))

        val aggregator = new Aggregator(movieSearchServiceMock,
                                        movieInfoServiceMock,
                                        artistInfoServiceMock,
                                        ratingServiceMock)
        val moviesDetailJsonFuture = aggregator.aggregate(moviesSearchRequest)

        moviesDetailJsonFuture.map { json =>
          assert(
            json == "Failure calling downstream service due to invalid query parameters supplied: InvalidQueryParamException: null")
        }
      }
    }

    "when calling successfully downstream artist info service with 1 generic failure" should {
      "return a generic failure suggesting invalid params" in {
        val movieSearchServiceMock = mock[MovieSearchService]
        val movieInfoServiceMock = mock[MovieInfoService]
        val artistInfoServiceMock = mock[ArtistInfoService]
        val ratingServiceMock = mock[RatingService]

        val moviesSearchRequest = MoviesSearchRequest("Action", 500000001, 0, 5)
        when(movieSearchServiceMock.searchMovies(moviesSearchRequest))
          .thenReturn(
            Future.successful(MovieSearch(Metadata(0, 5, 10), Seq(1, 2))))

        when(
          movieInfoServiceMock.moviesInfo(MoviesInfoRequest(Seq(1, 2), 5, 0)))
          .thenReturn(Future.successful(moviesInfo))

        when(ratingServiceMock.ratingAverageInfos(moviesInfo)).thenReturn(
          Seq(RatingAverageInfo(Rating(1, 4.5f), isBellowMedianAverage = false),
              RatingAverageInfo(Rating(2, 3.4f), isBellowMedianAverage = true)))

        when(movieSearchServiceMock.getAllGenres).thenReturn(
          Future.successful(Seq(Genre(1, "Action"), Genre(2, "SF"))))
        when(artistInfoServiceMock.artistInfos(Seq(1, 2)))
          .thenReturn(Future.failed(new Exception("bang!")))

        val aggregator = new Aggregator(movieSearchServiceMock,
                                        movieInfoServiceMock,
                                        artistInfoServiceMock,
                                        ratingServiceMock)
        val moviesDetailJsonFuture = aggregator.aggregate(moviesSearchRequest)

        moviesDetailJsonFuture.map { json =>
          assert(
            json == "Generic failure calling downstream service: Exception: bang!")
        }
      }
    }
  }
}
