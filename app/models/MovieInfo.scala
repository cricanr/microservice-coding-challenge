package models

case class MoviesInfoRequest(ids: Seq[Int], limit: Int, offset: Int) extends IInfoRequest

case class MovieInfo(id: Int, title: String, tagline: String, overview: String, popularity: Float, runtime: Int, releaseDate: String, revenue: Int, budget: Int, posterPath: String,
                     originalLanguage: String, genres: Seq[Int], cast: Seq[Int])
case class MoviesInfo(metadata: Metadata, data: Seq[MovieInfo])