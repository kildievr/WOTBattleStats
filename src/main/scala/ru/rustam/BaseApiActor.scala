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
        implicit val formats = DefaultFormats
        val body = read[WotData](ctx.request.entity.asString)
          Db.con.sendQuery(s"INSERT INTO stats (" +
            s"timeadded," +
            s"xp, " +
            s"cap, " +
            s"defBase, " +
            s"map, " +
            s"frag, " +
            s"gold, " +
            s"hits, " +
            s"spot, " +
            s"tier, " +
            s"idNum, " +
            s"place, " +
            s"shots, " +
            s"assist, " +
            s"damage, " +
            s"freeXP, " +
            s"result, " +
            s"credits, " +
            s"pierced, " +
            s"vehicle, " +
            s"battleTier, " +
            s"battleTime, " +
            s"battleType, " +
            s"originalXP, " +
            s"playerName, " +
            s"assistRadio, " +
            s"assistTrack, " +
            s"playerAccount" +
            s") VALUES (" +
            s"${System.currentTimeMillis / 1000},"+
            s"${body.xp}," +
            s"${body.cap}," +
            s"${body.defBase}," +
            s"'${body.map}'," +
            s"${body.frag}," +
            s"${body.gold}," +
            s"${body.hits}," +
            s"${body.spot}," +
            s"${body.tier}," +
            s"${body.idNum}," +
            s"${body.place}," +
            s"${body.shots}," +
            s"${body.assist}," +
            s"${body.damage}," +
            s"${body.freeXP}," +
            s"${body.result}," +
            s"${body.credits}," +
            s"${body.pierced}," +
            s"'${body.vehicle}'," +
            s"${body.battleTier}," +
            s"${body.epochTime}," +
            s"${body.battleType}," +
            s"${body.originalXP}," +
            s"'${body.playerName}'," +
            s"${body.assistRadio}," +
            s"${body.assistTrack}," +
            s"'${body.playerAccount}' );  ").onComplete {
          case Success(res) =>
            ctx.complete(s"Got $body and saved to db.")
          case Failure(err) =>
            ctx.complete(s"Body should be a valid json.")
        }
      }
    }
  }
}

