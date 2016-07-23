package services

import javax.inject.{Inject, Singleton}

import play.api.libs.ws.{WSClient, WSRequest}
import utils.GPSOAuthHelper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait GPSOAuthService {
  def doMasterLogin(email: String, password: String): Future[String]
}

@Singleton
class GPSOAuthServiceImpl @Inject()(remoteRequestService: RemoteRequestService) extends GPSOAuthService {
  override def doMasterLogin(email: String, password: String): Future[String] = {
    val authParameters: Map[String, String] = GPSOAuthHelper.createMasterAuthParameters(email, password)
    remoteRequestService.post(GPSOAuthHelper.googleAuthUrl, authParameters).map(response => {
      response.body
    })
  }
}