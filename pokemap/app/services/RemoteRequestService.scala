package services

import javax.inject.{Inject, Singleton}

import play.api.libs.ws.{WSClient, WSRequest, WSResponse}

import scala.concurrent.Future

trait RemoteRequestService {
  def post(url: String, parameters: Map[String, String] = Map()): Future[WSResponse]
}

@Singleton
class RemoteRequestServiceImpl @Inject()(ws: WSClient) extends RemoteRequestService {

  override def post(url: String, parameters: Map[String, String] = Map()): Future[WSResponse] = {
    addQueryParameters(ws.url(url), parameters).post("")
  }

  private def addQueryParameters(request: WSRequest, parameters: Map[String, String]): WSRequest = {
    parameters.foldLeft(request)((r, s) =>
      r.withQueryString(s)
    )
  }
}
