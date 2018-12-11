package controllers

import compose.Aggregator
import mocks.AggregatorFailedMock
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest

import scala.concurrent.Future

class MoviesControllerFailureTest
    extends WordSpec
    with Matchers
    with MockitoSugar
    with GuiceOneAppPerSuite {

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .overrides(bind[Aggregator].to[AggregatorFailedMock])
      .build()

  "The MoviesController" when {
    "calling aggregate with a correct query" should {
      "respond the movies det" in {
        val controller: MoviesController =
          app.injector.instanceOf[MoviesController]
        val fakeRequest = FakeRequest(
          method = "movies",
          "?genre=bam%20Fiction&revenue=500000000&offset=0&limit=10")
        val result = controller.movies()(fakeRequest.withHeaders())

        result.toString shouldEqual Future
          .failed(new Exception("Invalid search parameters supplied."))
          .toString
      }
    }
  }
}
