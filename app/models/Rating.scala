package models

case class Rating(id: Int, rating: Float)

case class RatingAverageInfo(rating: Rating, isBellowMedianAverage: Boolean)
