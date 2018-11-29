package services

import java.io.{File, OutputStream}

import akka.stream.Materializer
import akka.stream.scaladsl.{RestartSource, Sink}
import akka.util.ByteString
import com.google.inject.Inject
import io.circe.generic.auto._
import io.circe.parser.decode
import models.{MoviesInfo, Rating, RatingAverageInfo}
import play.api.libs.ws.WSClient

import scala.collection.immutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Try

class RatingService @Inject()(ws: WSClient)(implicit ec: ExecutionContext,
                                            mat: Materializer) {
  private lazy val ratingLinesUnfiltered = ratingLines
  private val ratingAvgList = ratingsAverage(ratingLinesUnfiltered)

  private def ratingLines: immutable.Seq[String] = {
    val source = Source.fromFile("ratings.txt")
    val ratingLines = Source.fromFile("ratings.txt").getLines.toList
    source.close()

    ratingLines
  }

  private[services] def createOutputFile(
      ratingsFilePath: String): Try[OutputStream] = {
    Try {
      val file = new File(ratingsFilePath)
      if (!file.exists()) file.createNewFile
      java.nio.file.Files.newOutputStream(file.toPath)
    }
  }

  def streamRatingsToFile(): Unit = {
    ws.url("http://localhost:3020/ratings").withMethod("GET").stream().foreach {
      response =>
        val tryOutputStream = createOutputFile("ratings.txt")
        tryOutputStream
          .map { outputStream =>
            val sink = Sink.foreach[ByteString] { bytes =>
              decodeRatingMessage(bytes)
              println(bytes.map(_.toChar).mkString.substring(6))
              outputStream.write(bytes.toArray)
            }

            response.bodyAsSource.runWith(sink).andThen {
              case result =>
                outputStream.close()
                result.get
            }
          }
          .recover {
            case failure: Throwable =>
              RestartSource.withBackoff(
                minBackoff = 3 seconds,
                maxBackoff = 30 seconds,
                randomFactor = 0.2 // adds 20% "noise" to vary the intervals slightly
              ) { () =>
                println("Stream source has been restarted.")
                response.bodyAsSource
              }

              println(
                s"Failure reading streaming data from file: ${failure.getClass.getSimpleName}: ${failure.getMessage}")
          }
    }
  }

  private[services] def decodeRatingMessage(
      bytes: ByteString): Option[Rating] = {
    val maybeMessage = Try(bytes.map(_.toChar).mkString.substring(6)).toOption
    maybeMessage.flatMap(message => decode[Rating](message).toOption)
  }

  private def rating(
      movieId: Int,
      ratingsAverageList: Seq[Rating] = ratingAvgList): Option[Rating] = {
    ratingsAverageList.find(rating => rating.id == movieId)
  }

  private def decodeRating(message: String): Option[Rating] = {
    decode[Rating](message).toOption
  }

  def ratingsAverage(ratingLinesUnfiltered: Seq[String]): Seq[Rating] = {
    val maybeRatingsPruned = ratingLinesUnfiltered
      .collect { case line if line nonEmpty => line.trim }
      .collect { case line if line.length > 6 => line.substring(6) }

    val ratings = maybeRatingsPruned.map(line => decodeRating(line)).collect {
      case decodedRating if decodedRating nonEmpty => decodedRating.get
    }

    val distinctRatingIds = ratings.map(_.id).distinct
    distinctRatingIds.map { distinctRatingId =>
      val rates = ratings.filter(_.id == distinctRatingId)
      val size = rates.size
      val average = if (size == 0) -1 else rates.map(_.rating).sum / size
      Rating(distinctRatingId, average)
    }
  }

  def isBellowMedianRating(movieId: Int,
                           ratingsAverageList: Seq[Rating]): Boolean = {
    val size = ratingsAverageList.size
    val averageRating =
      if (size == 0) -1 else ratingsAverageList.map(_.rating).sum / size

    val maybeRating = rating(movieId, ratingsAverageList)

    maybeRating.forall(rating =>
      if (rating.rating < averageRating) true else false)
  }

  def ratingAverageInfo(movieId: Int,
                        ratingsAverageList: Seq[Rating] = ratingAvgList)
    : Option[RatingAverageInfo] = {
    val maybeRating = rating(movieId, ratingsAverageList)
    maybeRating.map(
      rating =>
        RatingAverageInfo(rating,
                          isBellowMedianRating(movieId, ratingsAverageList)))
  }

  def ratingAverageInfos(moviesInfo: MoviesInfo): Seq[RatingAverageInfo] = {
    moviesInfo.data.map(movieInfo => ratingAverageInfo(movieInfo.id)).collect {
      case ratingAverageInfo if ratingAverageInfo.nonEmpty =>
        ratingAverageInfo.get
    }
  }
}
