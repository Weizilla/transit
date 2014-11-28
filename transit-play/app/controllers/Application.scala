package controllers

import java.nio.file.Files

import com.typesafe.config.ConfigFactory
import com.weizilla.transit.BusController
import com.weizilla.transit.data.{Direction, Prediction}
import com.weizilla.transit.favorites.sqlite.JdbcSqliteFavoritesStore
import com.weizilla.transit.groups.sqlite.JdbcSqliteGroupsStore
import com.weizilla.transit.cache.sqlite.JdbcSqliteCacheStore
import com.weizilla.transit.source.stream.StreamingDataSource
import com.weizilla.transit.source.stream.http.{HttpInputStreamProvider, HttpReader}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

import scala.collection.JavaConversions._

object Application extends Controller {
  val apiKey = ConfigFactory.load("api-key.conf").getString("apiKey")

  val controller = {
    val reader: HttpReader = new HttpReader(new HttpReader.HttpURLConnectionFactory)
    val provider: HttpInputStreamProvider = new HttpInputStreamProvider(reader, apiKey)
    val source: StreamingDataSource = new StreamingDataSource(provider)

    try {
      val dbPath = Files.createTempFile("transit-play-", ".db")
      dbPath.toFile.deleteOnExit()
      val favStore: JdbcSqliteFavoritesStore = JdbcSqliteFavoritesStore.createStore(dbPath)
      val groupsStore: JdbcSqliteGroupsStore = JdbcSqliteGroupsStore.createStore(dbPath)
      val cacheStore: JdbcSqliteCacheStore = JdbcSqliteCacheStore.createStore(dbPath)
      new BusController(source, favStore, groupsStore, cacheStore)
    } catch {
      case e: Exception =>
        throw new RuntimeException("Unable to create bus controller", e)
    }
  }

  val createGroupForm = Form(single("name" -> nonEmptyText))

  val addStopToGroupForm = Form(tuple(
    "stopId" -> number,
    "groupId" -> number,
    "routeId" -> nonEmptyText,
    "direction" -> nonEmptyText
  ))

  val saveFavoriteRouteForm = Form(single("routeId" -> nonEmptyText))
  val saveFavoriteStopForm = Form(tuple(
    "stopId" -> number,
    "routeId" -> nonEmptyText,
    "direction" -> nonEmptyText
  ))

  def index = Action {
    val groups = controller.getAllGroups
    val routes = controller.getRoutes
    val routeMap = routes.map(r => r.getId -> r).toMap
    val favRoutes = controller.getFavoriteRouteIds.map(routeMap(_))
    Ok(views.html.index(groups, favRoutes, routes))
  }

  def directions(routeId: String) = Action {
    val directions = controller.getDirections(routeId)
    Ok(views.html.directions(routeId, directions))
  }

  def stops(routeId: String, direction: String) = Action {
    val dir = Direction.valueOf(direction)
    val stops = controller.getStops(routeId, dir)
    val stopsMap = stops.map(s => s.getId -> s).toMap
    val favStops = controller.getFavoriteStopIds.map(stopsMap(_))
    val groups = controller.getAllGroups
    Ok(views.html.stops(routeId, direction, stops, favStops, groups))
  }

  def predictions(stopIdsStr: String, routeIdsStr: Option[String]) = Action {
    val stopIds = stopIdsStr.split(",").map(_.toInt).toList
    val routeIds = routeIdsStr.map(_.split(",").toList).getOrElse(List())

    var predictions: Iterable[Prediction] = List()
    var msg: Option[String] = None

    try {
      predictions = controller.getPredictions(stopIds.map(i => i:java.lang.Integer), routeIds)
      msg = None
    } catch {
      case t: Throwable =>
        msg = Some(t.getMessage)
    }

    val stopNames = predictions.map(_.getStopName).toSet

    Ok(views.html.predictions(stopNames, routeIds, predictions, msg))
  }

  def saveFavoriteRoute() = Action { implicit request =>
    val routeId = saveFavoriteRouteForm.bindFromRequest.get
    controller.saveFavoriteRoute(routeId)
    Redirect(routes.Application.index())
  }

  def saveFavoriteStop() = Action { implicit  request =>
    val (stopId, routeId, direction) = saveFavoriteStopForm.bindFromRequest().get
    controller.saveFavoriteStop(stopId)
    Redirect(routes.Application.stops(routeId, direction))
  }

  def createGroup() = Action { implicit request =>
    val groupName = createGroupForm.bindFromRequest.get
    controller.createGroup(groupName)
    Redirect(routes.Application.index())
  }

  def addStopToGroup() = Action { implicit request =>
    val (stopId, groupId, routeId, direction) = addStopToGroupForm.bindFromRequest.get
    controller.addStopToGroup(groupId, stopId)
    Redirect(routes.Application.stops(routeId, direction))
  }

  def getGroup(groupId: Int) = Action {
    val stops = controller.getStopIdsForGroup(groupId)
    Ok(views.html.group(groupId, stops))
  }
}
