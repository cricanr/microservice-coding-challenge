package controllers

import compose.Aggregator
import mocks.AggregatorSuccessMock
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class MoviesControllerSuccessTest extends WordSpec with Matchers with MockitoSugar with GuiceOneAppPerSuite {

  override def fakeApplication(): Application = new GuiceApplicationBuilder()
    .overrides(bind[Aggregator].to[AggregatorSuccessMock])
    .build()

  "The MoviesController" when {
    "calling aggregate with a correct query" should {
      "respond the movies det" in {
        val controller: MoviesController = app.injector.instanceOf[MoviesController]
        val fakeRequest = FakeRequest(method = "movies", "?genre=Science%20Fiction&revenue=500000000&offset=0&limit=10")
        val result: Future[Result] = controller.movies()(fakeRequest.withHeaders())

        status(result) shouldBe OK
        val content = contentAsString(result)
        content should include(AggregatorSuccessMock.successfulResponse)
      }
    }
  }
}