package ru.rustam

import akka.actor.Actor
import spray.routing.HttpService

/**
  * Created by Rustam on 26.03.2016.
  */
class BaseApiActor extends Actor with MyService {

  def actorRefFactory = context

  def receive = runRoute(route)

}

trait MyService extends HttpService {
  val route = pathPrefix("antifraud" / "00.01") {
    post {
      ctx => ctx.complete("OK")
    }
  }
}