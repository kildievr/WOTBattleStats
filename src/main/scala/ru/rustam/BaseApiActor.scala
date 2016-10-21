package ru.rustam

import akka.actor.Actor
import spray.routing.{RequestContext, HttpService}
import org.json4s.jackson.Serialization.read
import org.json4s._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Created by Rustam on 26.03.2016.
  */
class BaseApiActor extends Actor with MyService {
  override implicit val execContext = context.dispatcher

  def actorRefFactory = context

  def receive = runRoute(route)
}

trait MyService extends HttpService {



  implicit val execContext: ExecutionContext
  val route = path("sendStat") {
    post {
      ctx => {
//        val body = ctx.request.entity.asString
        implicit val formats = DefaultFormats
        val body = read[WotData](ctx.request.entity.asString)
        Db.con.sendQuery(s"INSERT INTO stat (json) VALUES ('$body');").onComplete {
          case Success(res) =>
            ctx.complete(s"Got $body and saved to db.")
          case Failure(err) =>
            ctx.complete(s"Body should be a valid json.")
        }
      }
    }
  }
}