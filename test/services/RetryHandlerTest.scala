package services

import akka.actor.ActorSystem
import akka.testkit.TestKit
import exceptions.GenericFailureCalling
import io.circe
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.language.postfixOps

class RetryHandlerTest
    extends TestKit(ActorSystem("MySpec"))
    with WordSpecLike
    with BeforeAndAfterAll
    with Matchers
    with MockitoSugar
    with ScalaFutures
    with Eventually {
  private implicit val ec: ExecutionContextExecutor = ExecutionContext.global
  private implicit val wsClientMock: WSClient = mock[WSClient]

  private implicit def decoderSuccessMock(
      body: String): Either[circe.Error, String] = Right("success")

  private val uriSuccess = "localhost:192/movies"
  private val uriFailure = "localhost:192/bam"

  def serviceCallFailing(uri: String): Future[String] = {
    println("Meow!")
    Future.failed(new GenericFailureCalling("meow!"))
  }

  def serviceCallSuccess(uri: String): Future[String] = {
    println("Meow!")
    Future.successful("meow!")
  }

  "The RetryHandler" when {
    "calling callWithRetryWithoutArgs for function returning a failed future" should {
      "call function with retry back-off propagation in case of error" in {
        val retryHandler = new RetryHandler

        implicit val retryConfig: RetryConfig =
          RetryConfig(noTries = 2, factor = 1.5f, init = 1, cur = 0)
        val f =
          retryHandler.callWithRetry[String](serviceCallFailing)(uriFailure)

        eventually(
          f.toString shouldEqual Future
            .failed(new GenericFailureCalling("meow!"))
            .toString)
        eventually(
          f.toString shouldEqual Future
            .failed(new GenericFailureCalling("meow!"))
            .toString)
        eventually(
          f.toString shouldEqual Future
            .failed(new GenericFailureCalling("meow!"))
            .toString)

        // TODO: fix verify called 3 times... for now visible only in console... cannot find any()...
        //        org.mockito.Mockito.verify(retryHandler.callWithRetryWithoutArgs(serviceCall)(any . any[Int]()),
        //          org.mockito.Mockito.calls(3))
        eventually(
          f.toString shouldEqual Future
            .failed(new GenericFailureCalling("No of retries exhausted"))
            .toString)
      }
    }

    "calling callWithRetryWithoutArgs for function returning a failed future with noRetries = 0" should {
      "call function with retry back-off propagation in case of error" in {
        val retryHandler = new RetryHandler
        val f =
          retryHandler.callWithRetry[String](serviceCallFailing)(uriFailure)

        eventually(
          f.toString shouldEqual Future
            .failed(new GenericFailureCalling("meow!"))
            .toString)

        eventually(
          f.toString shouldEqual Future
            .failed(new GenericFailureCalling("No of retries exhausted"))
            .toString)

        // TODO: fix verify called 0 times... for now visible only in console... cannot find any()...
      }
    }

    "calling callWithRetryWithoutArgs for function returning a successful future" should {
      "call function successfully without retry" in {
        val retryHandler = new RetryHandler
        val f =
          retryHandler.callWithRetry[String](serviceCallSuccess)(uriSuccess)

        eventually(f.toString shouldEqual Future.successful("meow!").toString)
      }
    }
  }
}
