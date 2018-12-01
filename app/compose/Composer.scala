package compose

import models._

class Composer {
  def compose(moviesInfoResult: MoviesInfo,
              artistInfoResult: Seq[ArtistInfos],
              genresResult: Seq[Genre],
              ratingAverageInfos: Seq[RatingAverageInfo]): MoviesDetail = {
    val nonEmptyArtistInfoResult =
      artistInfoResult.filter(res => res.data.nonEmpty)
    val artistInfosFlatten = nonEmptyArtistInfoResult.flatMap(_.data).distinct
    val metadata = nonEmptyArtistInfoResult.headOption
      .map(_.metadata)
      .getOrElse(Metadata(0, 5, 100))
    val artistInfos = ArtistInfos(metadata, artistInfosFlatten)
    val movieDetails = moviesInfoResult.data.map { movieInfoResult =>
      val maybeRatingAverageInfo = ratingAverageInfos.find(ratingAvgInfo =>
        ratingAvgInfo.rating.id == movieInfoResult.id)
      val avgRating: Float =
        maybeRatingAverageInfo.map(_.rating.rating).getOrElse(-1)
      val ratedBellowMedian =
        maybeRatingAverageInfo.forall(_.isBellowMedianAverage)

      val genres = movieInfoResult.genres.flatMap { id =>
        genresResult.collect { case genre if genre.id == id => genre.name }
      }
      val casts = movieInfoResult.cast.flatMap { id =>
        artistInfos.data.collect {
          case artistInfo if artistInfo.id == id =>
            Cast(artistInfo.id,
                 artistInfo.gender,
                 artistInfo.name,
                 artistInfo.profilePath)
        }
      }

      MovieDetail(
        movieInfoResult.id,
        movieInfoResult.title,
        averageRating = avgRating,
        ratedBelowMedian = ratedBellowMedian,
        movieInfoResult.runtime,
        movieInfoResult.releaseDate,
        movieInfoResult.revenue,
        movieInfoResult.posterPath,
        movieInfoResult.originalLanguage,
        genres,
        casts
      )
    }

    MoviesDetail(Movies(movieDetails))
  }
}

object Composer {
  def artistId(moviesInfo: MoviesInfo): Seq[Int] = {
    moviesInfo.data.flatMap(movieInfo => movieInfo.cast)
  }
}
