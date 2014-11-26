package controllers

import java.nio.file.Files

import com.typesafe.config.ConfigFactory
import com.weizilla.transit.BusController
import com.weizilla.transit.data.{Prediction, Direction}
import com.weizilla.transit.source.stream.StreamingDataSource
import com.weizilla.transit.source.stream.http.{HttpInputStreamProvider, HttpReader}
import com.weizilla.transit.favorites.sqlite.JdbcSqliteFavoritesStore
import com.weizilla.transit.groups.sqlite.JdbcSqliteGroupsStore
import play.api.mvc.{Action, Controller}
import scala.collection.JavaConversions._

object Application extends Controller {
  val apiKey = ConfigFactory.load("api-key.conf").getString("apiKey")

  val controller = {
    val reader: HttpReader = new HttpReader(new HttpReader.HttpURLConnectionFactory)
    val provider: HttpInputStreamProvider = new HttpInputStreamProvider(reader, apiKey)
    val source: StreamingDataSource = new StreamingDataSource(provider)

    try {
      val file = Files.createTempFile("transit-play-", ".db")
      file.toFile.deleteOnExit()
      val favStore: JdbcSqliteFavoritesStore = JdbcSqliteFavoritesStore.createStore(file)
      val groupsStore: JdbcSqliteGroupsStore = JdbcSqliteGroupsStore.createStore(file)
      new BusController(source, favStore, groupsStore)
    } catch {
      case e: Exception =>
        throw new RuntimeException("Unable to create bus controller", e)
    }
  }

  def index = Action {
    val groups = controller.getAllGroups
    val favRouteIds = controller.getFavoriteRouteIds
    val routes = controller.getRoutes
    Ok(views.html.index(groups, favRouteIds, routes))
  }

  def directions(routeId: String) = Action {
    val directions = controller.getDirections(routeId)
    Ok(views.html.directions(routeId, directions))
  }

  def stops(routeId: String, direction: String) = Action {
    val dir = Direction.valueOf(direction)
    val stops = controller.getStops(routeId, dir)
    val favStopIds = controller.getFavoriteStopIds(routeId, dir)
    Ok(views.html.stops(routeId, dir, stops, favStopIds))
  }

  def predictions(routeId: String, stopId: Int) = Action {
    var predictions: Iterable[Prediction] = List()
    var msg: Option[String] = None

    try {
      predictions = controller.getPredictions(routeId, stopId)
      msg = None
    } catch {
      case t: Throwable =>
        msg = Some(t.getMessage)
    }

    Ok(views.html.predictions(routeId, stopId, predictions, msg))
  }

  def saveFavRoute(routeId: String) = Action {
    controller.saveFavorite(routeId)
    Redirect(routes.Application.index())
  }

  def saveFavStop(stopId: Int, routeId: String, direction: String) = Action {
    controller.saveFavorite(stopId, routeId, Direction.valueOf(direction))
    Redirect(routes.Application.stops(routeId, direction))
  }
}
