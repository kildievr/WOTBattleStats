package ru.rustam

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by Rustam on 25.03.2016.
  */
object Main extends App {
  implicit val system = ActorSystem("wot-battle-stats-actor-system")
  val baseApiActor = system.actorOf(Props[BaseApiActor], "base-api-actor")
  implicit val timeout = Timeout(5 seconds)
  IO(Http) ? Http.Bind(baseApiActor, interface = "0.0.0.0", port = 9000)
}
