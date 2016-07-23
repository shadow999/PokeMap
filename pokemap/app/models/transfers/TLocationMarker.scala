package models.transfers

import play.api.libs.json.Json

case class TLocationMarker(lat: Double,
                           lng: Double,
                           title: String,
                           content: String,
                           iconUri: String)

object TLocationMarker {
  implicit val format = Json.format[TLocationMarker]
}