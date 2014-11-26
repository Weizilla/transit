package controllers

import java.nio.file.Files

import com.typesafe.config.ConfigFactory
import com.weizilla.transit.bus.BusController
import com.weizilla.transit.bus.source.stream.StreamingBusDataSource
import com.weizilla.transit.bus.source.stream.http.{HttpInputStreamProvider, HttpReader}
import com.weizilla.transit.favorites.sqlite.JdbcSqliteFavoritesStore
import com.weizilla.transit.groups.sqlite.JdbcSqliteGroupsStore
import play.api.mvc.{Action, Controller}
import scala.collection.JavaConversions._

object Application extends Controller {
  val apiKey = ConfigFactory.load("api-key.conf").getString("apiKey")

  val controller = {
    val reader: HttpReader = new HttpReader(new HttpReader.HttpURLConnectionFactory)
    val provider: HttpInputStreamProvider = new HttpInputStreamProvider(reader, apiKey)
    val source: StreamingBusDataSource = new StreamingBusDataSource(provider)

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
}
