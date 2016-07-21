package controllers

import javax.inject._

import play.api.mvc._
import services.Counter

@Singleton
class HomeController @Inject()(counter: Counter) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index(s"Your new application is ready. ${counter.nextCount()}"))
  }

}
