package models

case class Cast(id: Int, gender: Int, name: String, profilePath: String)

case class MovieDetail(id: Int, title: String, averageRating: Float, ratedBelowMedian: Boolean, runtime: Int, releaseDate: String, revenue: Int, posterPath: String, originalLanguage: String,
                       genres: Seq[String], cast: Seq[Cast])

case class Movies(movieDetail: Seq[MovieDetail])

case class MoviesDetail(data: Movies)