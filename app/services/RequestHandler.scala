package services

import com.google.inject.Inject
import exceptions.{FailureDecodingJson, GenericFailureCalling}
import io.circe
import play.api.libs.ws.{WSClient, WSRequest}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

trait IRequestHandler {
  def callEndpoint[T](uri: String)(implicit ec: ExecutionContext, wsClient: WSClient, decoder: String => Either[circe.Error, T]): Future[T]

  def callEndpointWithRetry[T](uri: String)(implicit ec: ExecutionContext, wsClient: WSClient, decoder: String => Either[circe.Error, T]): Future[T]

  def validateResult[T](statusCode: Int, body: String, decoder: String => Either[circe.Error, T]): Future[T]
}

class RequestHandler @Inject()(retryHandler: RetryHandler) extends IRequestHandler {

  import retryHandler._

  override def callEndpoint[T](uri: String)(implicit ec: ExecutionContext, wsClient: WSClient, decoder: String => Either[circe.Error, T]): Future[T] = {
    val requestWS: WSRequest = wsClient.url(uri)
      .withRequestTimeout(10000 millis)
      .addHttpHeaders("Accept" -> "application/json")

    val futureResponse = requestWS.execute()
    val futureResult = futureResponse.flatMap {
      response =>
        validateResult[T](response.status, response.body, decoder)
    }
    futureResult
  }

  override def callEndpointWithRetry[T](uri: String)
                                       (implicit ec: ExecutionContext,
                                        wsClient: WSClient,
                                        decoder: String => Either[circe.Error, T]): Future[T] = callWithRetry[T](callEndpoint[T])(uri)

  /* TODO: further improvement would be to NOT test a private method. I chose for this challenge this approach to reduce the mocking needed
while still avoiding to test 3rd party library code or servers that need mocking
as our logic is basically testing the status code */
  override def validateResult[T](statusCode: Int, body: String, decoder: String => Either[circe.Error, T]): Future[T] = {
    statusCode match {
      case status if status >= 200 && status < 300 => decoder(body) match {
        case Left(f) => Future.failed(new FailureDecodingJson(s"Exception during json decoding response: ${f.getClass}:${f.getMessage}"))
        case Right(resultDecoded) => Future.successful(resultDecoded)
      }
      case status if status >= 500 => Future.failed(new GenericFailureCalling(s"Exception calling endpoint"))
    }
  }
}