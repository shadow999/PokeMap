import javax.inject._
import play.api._
import play.api.http.HttpFilters
import play.api.mvc._

import filters.RequestFilter

@Singleton
class Filters @Inject() (env: Environment,
                         requestFilter: RequestFilter) extends HttpFilters {

  override val filters = {
    if (env.mode == Mode.Dev) Seq(requestFilter) else Seq.empty
  }

}
