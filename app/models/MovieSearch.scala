package models

case class MoviesSearchRequest(genre: String,
                               revenue: Int,
                               offset: Int,
                               limit: Int)
    extends IRequest {
  require(revenue >= 500000000)
}

case class MovieSearch(metadata: Metadata, data: Seq[Int])

case class Genre(id: Int, name: String)
