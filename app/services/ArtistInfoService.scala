package services

import com.google.inject.Inject
import io.circe
import io.circe.generic.auto._
import io.circe.parser.decode
import models.{ArtistInfoRequest, ArtistInfos, Metadata}
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import scala.util.control.NonFatal

trait IArtistInfoService {
  def artistInfo(artistInfoRequest: ArtistInfoRequest)(implicit ec: ExecutionContext): Future[ArtistInfos]
}

class ArtistInfoService @Inject()(requestHandler: IRequestHandler)
                                 (implicit ec: ExecutionContext, wSClient: WSClient) extends IArtistInfoService {

  import ArtistInfoService._
  import requestHandler._

  val baseUri = "http://localhost:3050"

  def artistInfo(artistInfoRequest: ArtistInfoRequest)(implicit ec: ExecutionContext): Future[ArtistInfos] = {
    import query.QueryParametersHelper._

    val method = "/artists"
    val queryParams = createQueryParams(artistInfoRequest)
    val urlArtistInfo = s"$baseUri$method$queryParams"

    implicit val retryConfig: RetryConfig = RetryConfig(noTries = 3, factor = 1.7f, 1, 0)
    callEndpointWithRetry[ArtistInfos](urlArtistInfo)
  }

  def artistInfos(artistIds: Seq[Int]): Future[Seq[ArtistInfos]] = {
    val artistInfoRequests = artistIds.grouped(5).toSeq.map(artistIdBatch => ArtistInfoRequest(artistIdBatch, 5, 0))

    val artistInfoFutureResponses = artistInfoRequests.map(artistInfoRequest => artistInfo(artistInfoRequest))
    val handleFailedFutures = artistInfoFutureResponses.map(_.recover {
      case NonFatal(_) => ArtistInfos(Metadata(5, 5, 0), Seq.empty)
    })
    val successfulFutureArtistInfo = Future.sequence(handleFailedFutures)
    successfulFutureArtistInfo
  }
}

object ArtistInfoService {
  implicit def decoder(body: String): Either[circe.Error, ArtistInfos] = {
    decode[ArtistInfos](body)
  }
}
