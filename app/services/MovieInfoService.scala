package services

import com.google.inject.Inject
import io.circe
import io.circe.generic.auto._
import io.circe.parser.decode
import models.{MoviesInfo, MoviesInfoRequest}
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

class MovieInfoService @Inject()(retryHandler: RetryHandler,
                                 requestHandler: IRequestHandler)(
    implicit ec: ExecutionContext,
    wSClient: WSClient) {

  import MovieInfoService._
  import requestHandler._

  private val baseUri = "http://localhost:3030"
  private val method = "/movies"

  def moviesInfo(moviesInfoRequest: MoviesInfoRequest): Future[MoviesInfo] = {
    implicit val retryConfig: RetryConfig =
      RetryConfig(noTries = 2, factor = 1.3f, 1, 0)

    import query.QueryParametersHelper._
    val queryParams = createQueryParams(moviesInfoRequest)
    val urlMoviesInfo = s"$baseUri$method$queryParams"

    callEndpointWithRetry[MoviesInfo](urlMoviesInfo)
  }

  def movieInfo(movieId: Int): Future[MoviesInfo] = {
    val queryParams = s"id=$movieId"
    val urlMoviesInfo = s"$baseUri$method$queryParams"
    callEndpointWithRetry[MoviesInfo](urlMoviesInfo)
  }
}

object MovieInfoService {
  implicit def decoder(body: String): Either[circe.Error, MoviesInfo] =
    decode[MoviesInfo](body)
}
