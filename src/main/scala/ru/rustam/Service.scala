package ru.rustam

import akka.http.scaladsl.server.Directives._
import org.json4s._
import org.json4s.jackson.Serialization.read
import org.log4s._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Rustam on 26.03.2016.
  */
trait Service {
  private[this] val logger  = getLogger
  implicit      val formats = DefaultFormats
  val route = logRequestResult("akka-http-microservice") {
    path("sendStat") {
      post {
        entity(as[String]) { string =>
          val body = read[WotData](string)
          val query = "INSERT INTO stats (" +
            "timeAdded," +
            "xp, " +
            "cap, " +
            "defBase, " +
            "map, " +
            "frag, " +
            "gold, " +
            "hits, " +
            "spot, " +
            "tier, " +
            "idNum, " +
            "place, " +
            "shots, " +
            "assist, " +
            "damage, " +
            "freeXP, " +
            "result, " +
            "credits, " +
            "pierced, " +
            "vehicle, " +
            "battleTier, " +
            "battleTime, " +
            "battleType, " +
            "originalXP, " +
            "playerName, " +
            "assistRadio, " +
            "assistTrack, " +
            "playerAccount) VALUES (" +
            System.currentTimeMillis / 1000 + "," +
            body.xp + "," +
            body.cap + "," +
            body.defBase + ",'" +
            body.map + "'," +
            body.frag + "," +
            body.gold + "," +
            body.hits + "," +
            body.spot + "," +
            body.tier + "," +
            body.idNum + "," +
            body.place + "," +
            body.shots + "," +
            body.assist + "," +
            body.damage + "," +
            body.freeXP + "," +
            body.result + "," +
            body.credits + "," +
            body.pierced + ",'" +
            body.vehicle + "'," +
            body.battleTier + "," +
            body.epochTime + "," +
            body.battleType + "," +
            body.originalXP + ",'" +
            body.playerName + "'," +
            body.assistRadio + "," +
            body.assistTrack + ",'" +
            body.playerAccount + "' );"
          complete {
            Db.con.sendQuery(query).map(_ => s"Got $string and saved to db.").recover {
              case th: Throwable =>
                logger.error(th)(string)
                th.toString
            }
          }
        }
      }
    }
  }
}

