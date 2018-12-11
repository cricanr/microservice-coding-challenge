package services

import com.google.inject.Inject
import io.circe.generic.auto._
import io.circe.parser.decode
import models.RatingAverageInfo
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class RatingInfoService @Inject()(retryHandler: RetryHandler,
                                  requestHandler: IRequestHandler)(
    implicit ec: ExecutionContext,
    wSClient: WSClient)
    extends IRatingService {

  import RatingInfoService._
  import requestHandler._

  private val baseUri = "http://localhost:9000"
  private val method = "/ratingInfo/{id}"

  def ratingInfo(movieId: Int): Future[RatingAverageInfo] = {
    val urlRatingInfo = s"$baseUri$method$movieId"
    callEndpointWithRetry[RatingAverageInfo](urlRatingInfo)
  }
}

object RatingInfoService {

  import io.circe

  implicit def decoder(body: String): Either[circe.Error, RatingAverageInfo] =
    decode[RatingAverageInfo](body)
}
