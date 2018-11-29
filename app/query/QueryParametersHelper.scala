package query

import exceptions.InvalidQueryParamException
import models.{IInfoRequest, IRequest, MoviesSearchRequest}

import scala.util.{Failure, Success, Try}

trait IQueryParametersBuilder {
  def createQueryParams[IRequest](request: IRequest): String
}

object QueryParametersHelper {
  def apply(query: Map[String, Seq[String]]): Option[MoviesSearchRequest] = {
    val maybeGenre = getStringParameter(query, "genre")
    val maybeRevenue = getIntParameter(query, "revenue")
    val maybeOffset = getIntParameter(query, "offset")
    val maybeLimit = getIntParameter(query, "limit")

    for {
      genre <- maybeGenre
      revenue <- maybeRevenue
      offset <- maybeOffset
      limit <- maybeLimit
    } yield MoviesSearchRequest(genre, revenue, offset, limit)
  }

  def createQueryParams(request: IRequest): String = {
    request match {
      case infoRequest: IInfoRequest => createQueryParams(infoRequest)
      case moviesSearchRequest: MoviesSearchRequest =>
        createQueryParams(moviesSearchRequest)
    }
  }

  private def getStringParameter(query: Map[String, Seq[String]],
                                 paramName: String): Option[String] = {
    query.get(paramName).flatMap(_.headOption)
  }

  private def getIntParameter(query: Map[String, Seq[String]],
                              paramName: String): Option[Int] = {
    Try(query.get(paramName).flatMap(_.headOption).map(_.toInt)) match {
      case Success(param) => param
      case Failure(_) =>
        throw new InvalidQueryParamException(
          s"Invalid query param supplied: $paramName. It must be an int.")
    }
  }

  private def createQueryParams(infoRequest: IInfoRequest): String = {
    import infoRequest._
    if (ids.isEmpty) ""
    else s"?ids=${ids.mkString(",")}&limit=$limit&offset=$offset"
  }

  private def createQueryParams(
      moviesSearchParameters: MoviesSearchRequest): String = {
    import moviesSearchParameters._
    s"?genre=$genre&revenue=$revenue&limit=$limit&offset=$offset"
  }
}
