package controllers

import java.nio.file.Files

import com.typesafe.config.ConfigFactory
import com.weizilla.transit.BusController
import com.weizilla.transit.data.{Direction, Prediction}
import com.weizilla.transit.favorites.sqlite.JdbcSqliteFavoritesStore
import com.weizilla.transit.groups.sqlite.JdbcSqliteGroupsStore
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
      val favDb = Files.createTempFile("transit-play-favorites-", ".db")
      favDb.toFile.deleteOnExit()
      val groupDb = Files.createTempFile("transit-play-groups-", ".db")
      groupDb.toFile.deleteOnExit()
      val favStore: JdbcSqliteFavoritesStore = JdbcSqliteFavoritesStore.createStore(favDb)
      val groupsStore: JdbcSqliteGroupsStore = JdbcSqliteGroupsStore.createStore(groupDb)
      new BusController(source, favStore, groupsStore)
    } catch {
      case e: Exception =>
        throw new RuntimeException("Unable to create bus controller", e)
    }
  }

  val createGroupForm = Form(mapping("name" -> nonEmptyText)(CreateGroupData.apply)(CreateGroupData.unapply))

  val addStopToGroupForm = Form(mapping(
    "stopId" -> number,
    "groupId" -> number,
    "direction" -> nonEmptyText,
    "routeId" -> nonEmptyText
  )(AddStopToGroupData.apply)(AddStopToGroupData.unapply))

  def index = Action {
    val groups = controller.getAllGroups
    val routes = controller.getRoutes
    val routeMap = routes.map(r => r.getId -> r).toMap
    val favRoutes = controller.getFavoriteRouteIds.map(routeMap(_))
    Ok(views.html.index(createGroupForm, groups, favRoutes, routes))
  }

  def directions(routeId: String) = Action {
    val directions = controller.getDirections(routeId)
    Ok(views.html.directions(routeId, directions))
  }

  def stops(routeId: String, direction: String) = Action {
    val dir = Direction.valueOf(direction)
    val stops = controller.getStops(routeId, dir)
    val stopsMap = stops.map(s => s.getId -> s).toMap
    val favStops = controller.getFavoriteStopIds(routeId, dir).map(stopsMap(_))
    val groups = controller.getAllGroups
    Ok(views.html.stops(routeId, dir, stops, favStops, groups, addStopToGroupForm))
  }

  def predictions(stopIdsStr: String, routeIdsStr: Option[String]) = Action {
    val stopIds = stopIdsStr.split(",").map(_.toInt).toList
    val routeIds = routeIdsStr.getOrElse("").split(",").toList

    var predictions: Iterable[Prediction] = List()
    var msg: Option[String] = None

    try {
      predictions = controller.getPredictions(stopIds.map(i => i:java.lang.Integer), routeIds)
      msg = None
    } catch {
      case t: Throwable =>
        msg = Some(t.getMessage)
    }

    Ok(views.html.predictions(stopIds, routeIds, predictions, msg))
  }

  def saveFavRoute(routeId: String) = Action {
    controller.saveFavorite(routeId)
    Redirect(routes.Application.index())
  }

  def saveFavStop(stopId: Int, routeId: String, direction: String) = Action {
    controller.saveFavorite(stopId, routeId, Direction.valueOf(direction))
    Redirect(routes.Application.stops(routeId, direction))
  }

  def createGroup() = Action { implicit request =>
    val groupName = createGroupForm.bindFromRequest.get.name
    controller.createGroup(groupName)
    Redirect(routes.Application.index())
  }

  def addStopToGroup() = Action { implicit request =>
    val data = addStopToGroupForm.bindFromRequest.get
    controller.addStopToGroup(data.groupId, data.stopId)
    Redirect(routes.Application.stops(data.routeId, data.direction))
  }

  def getGroup(groupId: Int) = Action {
    val stops = controller.getStopIdsForGroup(groupId)
    Ok(views.html.group(groupId, stops))
  }
}
