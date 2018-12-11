package services

import exceptions.GenericFailureCalling
import models._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}
import services.ArtistInfoService._

import scala.concurrent.Future

class ArtistInfoServiceTest
    extends WordSpec
    with PrivateMethodTester
    with Matchers
    with MockitoSugar {

  private val body: String =
    """{
      |    "metadata": {
      |        "offset": 0,
      |        "limit": 2,
      |        "total": 2
      |    },
      |    "data": [
      |        {
      |            "name": "Mark Hamill",
      |            "gender": 2,
      |            "profilePath": "https://image.tmdb.org/t/p/w185/fk8OfdReNltKZqOk2TZgkofCUFq.jpg",
      |            "movies": [
      |                11,
      |                181808
      |            ],
      |            "id": "2"
      |        }
      |        ]
      |        }""".stripMargin

  private val retryHandlerMock = mock[RetryHandler]
  private val requestHandler = new RequestHandler(retryHandlerMock)

  import requestHandler._

  "calling validateArtistInfoResponse with an OK status" should {
    "return ArtistInfo results successfully" in {
      val result = validateResult[ArtistInfos](200, body, decoder)
      result.toString shouldBe Future
        .successful(
          ArtistInfos(
            Metadata(0, 2, 2),
            Seq(ArtistInfo(
              "Mark Hamill",
              2,
              "https://image.tmdb.org/t/p/w185/fk8OfdReNltKZqOk2TZgkofCUFq.jpg",
              Seq(11, 181808),
              2))))
        .toString
    }
  }

  "calling validateArtistInfoResponse with an 500 status" should {
    "return a validation error" in {
      val resultFailure =
        validateResult[ArtistInfos](500, """{"bla"}""", decoder)
      resultFailure.toString shouldEqual Future
        .failed(new GenericFailureCalling("Exception calling artists endpoint"))
        .toString
    }
  }
}
