package compose

import models._
import org.scalatest.{Matchers, WordSpec}

class ComposerTest extends WordSpec with Matchers {
  "The Composer" when {
    "calling compose with models" should {
      "return a MoviesDetail" in {
        val composer = new Composer()
        val moviesInfo = MoviesInfo(Metadata(5, 5, 10), Seq(MovieInfo(1, "SW", "test", "overview", 1F, 1, "2011-10-11", 1, 1, "poster", "en", Seq(1, 2), Seq(1, 2))))
        val artistsInfo = Seq(ArtistInfos(Metadata(5, 5, 10), Seq(ArtistInfo("elan", 1, "", Seq(1), 1))))
        val genres = Seq(Genre(1, "SF"))
        val ratingsAvg = Seq(RatingAverageInfo(Rating(1, 13f), false))
        composer.compose(moviesInfo, artistsInfo, genres, ratingsAvg) shouldEqual
          MoviesDetail(Movies(Seq(MovieDetail(1, "SW", 13f, false, 1, "2011-10-11", 1, "poster", "en", List("SF"), List(Cast(1, 1, "elan", ""))))))
      }
    }
  }

}
