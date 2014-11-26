package controllers

import com.typesafe.config.ConfigFactory
import play.api.mvc.{Action, Controller}

object Application extends Controller {
  val apiKey = ConfigFactory.load("api-key.conf").getString("apiKey")

  def index = Action {
    Ok(views.html.index(apiKey))
  }
}
