import com.google.inject.AbstractModule
import java.time.Clock

import services._

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    bind(classOf[ApplicationLoader]).asEagerSingleton()
    bind(classOf[RemoteRequestService]).to(classOf[RemoteRequestServiceImpl])
    bind(classOf[GPSOAuthService]).to(classOf[GPSOAuthServiceImpl])
  }

}
