package controllers

import javax.inject._

import models.TLocationMarker
import play.api.libs.json.Json
import play.api.mvc._

import scala.util.Random

@Singleton
class TestController @Inject() extends Controller {

  val rand = Random
  val originLat = 49.264374
  val originLng = -123.1189124
  val numberLocations = 100

  def getTestLocations = Action {
    val locations = (1 to numberLocations).map(_ => createRandomLocation())
    Ok(Json.toJson(locations))
  }

  private def getRandomOffset(): Double = {
    val factor = if (rand.nextInt(2) == 0) -1 else 1
    (rand.nextInt(500) + 500 * factor).toDouble / 10000D
  }

  private def createRandomLocation(): TLocationMarker = {
    TLocationMarker(
      originLat + getRandomOffset(),
      originLng + getRandomOffset(),
      "Test 1",
      "This is test location 1.",
      "https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png")
  }

}

