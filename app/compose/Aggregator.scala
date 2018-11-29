package compose

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import compose.Composer.artistId
import exceptions.{
  FailureDecodingJson,
  GenericFailureCalling,
  InvalidQueryParamException
}
import io.circe.generic.auto._
import io.circe.syntax._
import models._
import play.api.libs.ws.WSClient
import services._

import scala.concurrent.{ExecutionContext, Future}

class Aggregator @Inject()(movieSearchService: MovieSearchService,
                           movieInfoService: MovieInfoService,
                           artistInfoService: ArtistInfoService,
                           ratingService: RatingService)(
    implicit ec: ExecutionContext,
    mat: Materializer,
    system: ActorSystem,
    ws: WSClient) {

  def aggregate(request: MoviesSearchRequest): Future[String] = {

    ratingService.streamRatingsToFile()
    val composer = new Composer()
    val futureMovieSearch = movieSearchService.searchMovies(request)
    val futureGenresResult = movieSearchService.getAllGenres

    futureMovieSearch
      .flatMap { movieSearch =>
        val moviesDetailJson = for {
          moviesInfoResult <- movieInfoService.moviesInfo(
            MoviesInfoRequest(movieSearch.data,
                              movieSearch.metadata.limit,
                              movieSearch.metadata.offset))
          artistInfosResult <- artistInfoService.artistInfos(
            artistId(moviesInfoResult))
          genresResult <- futureGenresResult
        } yield {
          composer
            .compose(moviesInfoResult,
                     artistInfosResult,
                     genresResult,
                     ratingService.ratingAverageInfos(moviesInfoResult))
            .asJson
            .toString
        }
        moviesDetailJson.recover {
          case failure => handleFailure(failure)
        }
      }
  }

  private def handleFailure(exception: Throwable): String = {
    exception match {
      case failure: InvalidQueryParamException =>
        s"Failure calling downstream service due to invalid query parameters supplied: ${failureInfo(failure)}"
      case failure: GenericFailureCalling =>
        s"Failure calling downstream service: ${failureInfo(failure)}"
      case failure: FailureDecodingJson =>
        s"Failure decoding json: ${failureInfo(failure)}"
      case _ @failure =>
        s"Generic failure calling downstream service: ${failureInfo(failure)}"
    }
  }

  private def failureInfo(failure: Throwable): String =
    s"${failure.getClass.getSimpleName}: ${failure.getMessage}"
}
