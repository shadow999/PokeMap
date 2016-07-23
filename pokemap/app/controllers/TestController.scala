package controllers

import javax.inject._

import models.transfers.TLocationMarker
import play.api.libs.json.Json
import play.api.mvc._
import services.GPSOAuthService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

@Singleton
class TestController @Inject()(gpsOAuthService: GPSOAuthService) extends Controller {

  val rand = Random
  val originLat = 49.264374
  val originLng = -123.1189124
  val numberLocations = 5

  def getTestLocations: Action[AnyContent] = Action {
    val locations = (1 to numberLocations).map(_ => createRandomLocation())
    Ok(Json.toJson(locations))
  }

  private def getRandomOffset(): Double = {
    val factor = if (rand.nextInt(2) == 0) -1 else 1
    (rand.nextInt(100) + 100 * factor).toDouble / 10000D
  }

  private def createRandomLocation(): TLocationMarker = {
    TLocationMarker(
      originLat + getRandomOffset(),
      originLng + getRandomOffset(),
      "Test 1",
      "This is test location 1.",
      "https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png")
  }

  def test: Action[AnyContent] = Action.async {
    val email = "moc19891@gmail.com"
    val password = "alex1989"
    gpsOAuthService.doMasterLogin(email, password).map(res => Ok(Json.toJson(res)))

  }

}

