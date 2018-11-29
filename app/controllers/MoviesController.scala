package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import compose.Aggregator
import javax.inject._
import play.api.libs.ws.WSClient
import play.api.mvc._
import query.QueryParametersHelper
import services._

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

@Singleton
class MoviesController @Inject()(
    cc: ControllerComponents,
    ws: WSClient,
    system: ActorSystem,
    retryHandler: RetryHandler,
    ratingService: RatingService,
    aggregator: Aggregator
)(implicit ec: ExecutionContext, mat: Materializer)
    extends AbstractController(cc) {

  def movies: Action[AnyContent] = Action.async { implicit request =>
    val maybeMoviesSearchParameters = QueryParametersHelper(request.queryString)
    maybeMoviesSearchParameters
      .map(
        moviesSearchParams =>
          aggregator
            .aggregate(moviesSearchParams)
            .map(aggregatedResult => Ok(aggregatedResult)))
      .getOrElse(Future.successful(Ok("Invalid search parameters supplied.")))
  }
}
