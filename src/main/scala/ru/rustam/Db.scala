package ru.rustam

import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.postgresql.util.URLParser

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by Rustam on 26.03.2016.
  */
object Db {

  val configuration = URLParser.parse(
    "jdbc:postgresql://178.62.140.149:5432/wot_battle_stat?user=wot-battle-stat&password=wot-battle-stat")
  lazy val con = {
    val con = new PostgreSQLConnection(configuration)
    Await.result(con.connect, 5 seconds)
    con
  }

}
