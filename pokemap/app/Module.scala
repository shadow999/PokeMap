import com.google.inject.AbstractModule
import java.time.Clock

import services.{ApplicationLoader, AtomicCounter, Counter}

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    bind(classOf[ApplicationLoader]).asEagerSingleton()
    bind(classOf[Counter]).to(classOf[AtomicCounter])
  }

}
