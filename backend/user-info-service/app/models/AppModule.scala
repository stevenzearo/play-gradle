package models

import java.time.Clock
import com.google.inject.AbstractModule
import domain.{UserInfo, UserInfoDAO}
import lib.db.async.{AsyncDBUtil, AsyncDBUtilImpl}
import lib.db.{DAO, DBUtil, DBUtilImpl}
import services.{ApplicationTimer, AtomicCounter, Counter, UserInfoService}

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class AppModule extends AbstractModule {
  override def configure(): Unit = {
    // Use the system clock as the default implementation of Clock
//    bind(classOf[UserInfoExecutionContext]).asEagerSingleton()
    bind(classOf[DBUtil]).to(classOf[DBUtilImpl]).asEagerSingleton()
    bind(classOf[AsyncDBUtil]).to(classOf[AsyncDBUtilImpl]).asEagerSingleton()
    bind(classOf[UserInfoDAO]).asEagerSingleton()
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    bind(classOf[ApplicationTimer]).asEagerSingleton()
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[Counter]).to(classOf[AtomicCounter])
    bind(classOf[UserInfoService]).asEagerSingleton()
    bind(classOf[AsyncDBUtil]).to(classOf[AsyncDBUtilImpl]).asEagerSingleton()
  }
}
