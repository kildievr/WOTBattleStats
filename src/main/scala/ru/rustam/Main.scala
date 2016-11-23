package ru.rustam

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by Rustam on 25.03.2016.
  */
object Main extends App with Service {
  implicit val system       = ActorSystem("wot-battle-stats-actor-system")
  implicit val timeout      = Timeout(5 seconds)
  implicit val materializer = ActorMaterializer()
  Http().bindAndHandle(route, "0.0.0.0", 9000)

}
