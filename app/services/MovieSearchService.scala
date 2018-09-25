package services

import com.google.inject.Inject
import io.circe
import io.circe.generic.auto._
import io.circe.parser.decode
import models.{Genre, MovieSearch, MoviesSearchRequest}
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

trait IMovieSearchService {
  def getAllGenres(implicit ec: ExecutionContext): Future[Seq[Genre]]

  def searchMovies(moviesSearchParameters: MoviesSearchRequest)(implicit ec: ExecutionContext): Future[MovieSearch]
}

class MovieSearchService @Inject()(requestHandler: IRequestHandler)
                                  (implicit ec: ExecutionContext,
                                   wSClient: WSClient)
  extends IMovieSearchService {

  import MovieSearchService._
  import requestHandler._

  val baseUri = "http://localhost:3040"

  override def searchMovies(moviesSearchRequest: MoviesSearchRequest)(implicit ec: ExecutionContext): Future[MovieSearch] = {
    import query.QueryParametersHelper._

    val method = "/movies"
    val queryParams = createQueryParams(moviesSearchRequest)
    val urlSearchMovies = s"$baseUri$method$queryParams"
    callEndpointWithRetry[MovieSearch](urlSearchMovies)
  }

  override def getAllGenres(implicit ec: ExecutionContext): Future[Seq[Genre]] = {
    val method = "/genres"
    val urlGetAllGenres = s"$baseUri$method"
    callEndpointWithRetry[Seq[Genre]](urlGetAllGenres)
  }
}

object MovieSearchService {
  implicit def decodeMovieSearch(body: String): Either[circe.Error, MovieSearch] = {
    decode[MovieSearch](body)
  }

  implicit def decodeGenres(body: String): Either[circe.Error, Seq[Genre]] = {
    decode[Seq[Genre]](body)
  }
}