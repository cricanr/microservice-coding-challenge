package mocks

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import compose.Aggregator
import models.MoviesSearchRequest
import play.api.libs.ws.WSClient
import services._

import scala.concurrent.{ExecutionContext, Future}

class AggregatorSuccessMock @Inject()(movieSearchService: MovieSearchService,
                                      movieInfoService: MovieInfoService,
                                      artistInfoService: ArtistInfoService,
                                      ratingService: RatingService)(
    implicit ec: ExecutionContext,
    mat: Materializer,
    system: ActorSystem,
    wsClient: WSClient)
    extends Aggregator(movieSearchService,
                       movieInfoService,
                       artistInfoService,
                       ratingService)(ec, mat, system, wsClient) {
  override def aggregate(request: MoviesSearchRequest): Future[String] = {
    Future.successful(AggregatorSuccessMock.successfulResponse)
  }
}

class AggregatorFailedMock @Inject()(movieSearchService: MovieSearchService,
                                     movieInfoService: MovieInfoService,
                                     artistInfoService: ArtistInfoService,
                                     ratingService: RatingService)(
    implicit ec: ExecutionContext,
    mat: Materializer,
    system: ActorSystem,
    wsClient: WSClient)
    extends Aggregator(movieSearchService,
                       movieInfoService,
                       artistInfoService,
                       ratingService)(ec, mat, system, wsClient) {
  override def aggregate(request: MoviesSearchRequest): Future[String] = {
    Future.failed(new Exception("Invalid search parameters supplied."))
  }
}

object AggregatorSuccessMock {
  val successfulResponse =
    """{
      |data: {
      |movieDetail: [
      |{
      |id: 1893,
      |title: "Star Wars: Episode I - The Phantom Menace",
      |averageRating: -1,
      |ratedBelowMedian: true,
      |runtime: 136,
      |releaseDate: "1999-05-19",
      |revenue: 924317558,
      |posterPath: "https://image.tmdb.org/t/p/w342/n8V09dDc02KsSN6Q4hC2BX6hN8X.jpg",
      |originalLanguage: "en",
      |genres: [
      |"Adventure",
      |"Action",
      |"Science Fiction"
      |],
      |cast: [ ]
      |},
      |{
      |id: 335988,
      |title: "Transformers: The Last Knight",
      |averageRating: -1,
      |ratedBelowMedian: true,
      |runtime: 149,
      |releaseDate: "2017-06-16",
      |revenue: 605425157,
      |posterPath: "https://image.tmdb.org/t/p/w342/s5HQf2Gb3lIO2cRcFwNL9sn1o1o.jpg",
      |originalLanguage: "en",
      |genres: [
      |"Action",
      |"Science Fiction",
      |"Thriller",
      |"Adventure"
      |],
      |cast: [
      |{
      |id: 13240,
      |gender: 2,
      |name: "Mark Wahlberg",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/z2wJh5n7qZRUE1y9uB8UrivAV2b.jpg"
      |},
      |{
      |id: 19536,
      |gender: 2,
      |name: "Josh Duhamel",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/hi5OzlZAwf22xRRPLFbKnXNoZ9L.jpg"
      |},
      |{
      |id: 209578,
      |gender: 1,
      |name: "Laura Haddock",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/cc8dV3F1poQ3nZoNfm5rECI2hd9.jpg"
      |},
      |{
      |id: 4173,
      |gender: 2,
      |name: "Anthony Hopkins",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/btweZvSRBiE6jaMME93BSXeTQN6.jpg"
      |},
      |{
      |id: 2283,
      |gender: 2,
      |name: "Stanley Tucci",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/omGlTJF2IW5r3L3c5y0qkCt3hFr.jpg"
      |}
      |]
      |},
      |{
      |id: 286217,
      |title: "The Martian",
      |averageRating: -1,
      |ratedBelowMedian: true,
      |runtime: 141,
      |releaseDate: "2015-09-30",
      |revenue: 630161890,
      |posterPath: "https://image.tmdb.org/t/p/w342/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg",
      |originalLanguage: "en",
      |genres: [
      |"Drama",
      |"Adventure",
      |"Science Fiction"
      |],
      |cast: [
      |{
      |id: 1892,
      |gender: 2,
      |name: "Matt Damon",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/elSlNgV8xVifsbHpFsqrPGxJToZ.jpg"
      |},
      |{
      |id: 83002,
      |gender: 1,
      |name: "Jessica Chastain",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/nkFrkn5NZVGWb4b2X0yIcXezhyt.jpg"
      |},
      |{
      |id: 41091,
      |gender: 1,
      |name: "Kristen Wiig",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/iqRX3PDxA33zFhSfKTo5BE593G4.jpg"
      |},
      |{
      |id: 8447,
      |gender: 2,
      |name: "Jeff Daniels",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/gai03gCu3DxMYxFympt7hUObpI5.jpg"
      |},
      |{
      |id: 454,
      |gender: 2,
      |name: "Michael Peña",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/oqlIKSglghuX7kSTalODn71nlOd.jpg"
      |}
      |]
      |},
      |{
      |id: 330459,
      |title: "Rogue One: A Star Wars Story",
      |averageRating: -1,
      |ratedBelowMedian: true,
      |runtime: 133,
      |releaseDate: "2016-12-14",
      |revenue: 1056057273,
      |posterPath: "https://image.tmdb.org/t/p/w342/qjiskwlV1qQzRCjpV0cL9pEMF9a.jpg",
      |originalLanguage: "en",
      |genres: [
      |"Action",
      |"Adventure",
      |"Science Fiction"
      |],
      |cast: [
      |{
      |id: 72855,
      |gender: 1,
      |name: "Felicity Jones",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/ywXf5fLAYLlwK5JRn6UrMMsBnwb.jpg"
      |},
      |{
      |id: 8688,
      |gender: 2,
      |name: "Diego Luna",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/uH8sMbiMmy9sprKr1vJpoPBB40E.jpg"
      |},
      |{
      |id: 77335,
      |gender: 2,
      |name: "Ben Mendelsohn",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/7wuqoqwDOMi6k1Y4zNzGPPsiQKy.jpg"
      |},
      |{
      |id: 1341,
      |gender: 2,
      |name: "Donnie Yen",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/vlKBbOc0htUsDGvcxeULcFXDMRo.jpg"
      |},
      |{
      |id: 1019,
      |gender: 2,
      |name: "Mads Mikkelsen",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/8F1dY2rjZ1YDEKH0imDs21xdTDX.jpg"
      |}
      |]
      |},
      |{
      |id: 27205,
      |title: "Inception",
      |averageRating: -1,
      |ratedBelowMedian: true,
      |runtime: 148,
      |releaseDate: "2010-07-14",
      |revenue: 825532764,
      |posterPath: "https://image.tmdb.org/t/p/w342/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg",
      |originalLanguage: "en",
      |genres: [
      |"Action",
      |"Thriller",
      |"Science Fiction",
      |"Mystery",
      |"Adventure"
      |],
      |cast: [
      |{
      |id: 6193,
      |gender: 2,
      |name: "Leonardo DiCaprio",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/w7bU92hsW21RgBgQRgvsEBFMA4h.jpg"
      |},
      |{
      |id: 24045,
      |gender: 2,
      |name: "Joseph Gordon-Levitt",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/zSuXCR6xCKIgo0gWLdp8moMlH3I.jpg"
      |},
      |{
      |id: 27578,
      |gender: 1,
      |name: "Ellen Page",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/y3vnDOSEpX2OO6WxSnrAbRE6xLG.jpg"
      |},
      |{
      |id: 2524,
      |gender: 2,
      |name: "Tom Hardy",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/4CR1D9VLWZcmGgh4b6kKuY2NOel.jpg"
      |},
      |{
      |id: 3899,
      |gender: 2,
      |name: "Ken Watanabe",
      |profilePath: "https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185https://image.tmdb.org/t/p/w185/v8WQ5wCIZsnqVZn7jQveaDqurox.jpg"
      |}
      |]
      |}
      |]
      |}
      |}"""

}
