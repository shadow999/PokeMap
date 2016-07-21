package filters

import akka.stream.Materializer
import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RequestFilter @Inject()(
    implicit override val mat: Materializer,
    exec: ExecutionContext) extends Filter {

  override def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {
    nextFilter(requestHeader).map { result =>
      result
    }
  }

}
