package services

import akka.actor.ActorSystem
import akka.pattern.Patterns.after
import com.google.inject.Inject
import exceptions.GenericFailureCalling
import io.circe
import play.api.libs.ws.WSClient

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

case class RetryConfig(noTries: Int, factor: Float, init: Int, cur: Int)

trait IRetryHandler {
  def callWithRetry[T](callEndpoint: String => Future[T])
                      (uri: String)
                      (implicit ec: ExecutionContext,
                               wsClient: WSClient,
                               decoder: String => Either[circe.Error, T],
                               retryConfig: RetryConfig): Future[T]
}

class RetryHandler @Inject()(implicit system: ActorSystem, ec: ExecutionContext) extends IRetryHandler {
  implicit val defaultRetryConfig: RetryConfig = RetryConfig(noTries = 1, factor = 1.5f, init = 1, cur = 0)

  def callWithRetry[T](callEndpoint: String => Future[T])
                      (uri: String)(implicit ec: ExecutionContext, wsClient: WSClient, decoder: String => Either[circe.Error, T], retryConfig: RetryConfig = defaultRetryConfig)
  : Future[T] = {
    import retryConfig._
    callEndpoint(uri).recoverWith {
      case _ =>
        if (noTries > 0) {
          val next: Int = if (cur == 0) init else Math.ceil(cur * factor).toInt
          println(s"retrying calling: after $next ms")

          after(next.milliseconds, system.scheduler, ec, Future.successful(1)).flatMap { _ =>
            callWithRetry(callEndpoint)(uri)(ec, wsClient, decoder, retryConfig.copy(noTries = noTries - 1))
          }
        } else {
          println("No of retries exhausted")
          throw new GenericFailureCalling("No of retries exhausted")
        }
    }
  }
}
