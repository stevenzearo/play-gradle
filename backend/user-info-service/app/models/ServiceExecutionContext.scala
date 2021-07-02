package models

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext

import javax.inject.{Inject, Singleton}

@Singleton
class ServiceExecutionContext @Inject()(system: ActorSystem) extends CustomExecutionContext(system, "service.dispatcher")
