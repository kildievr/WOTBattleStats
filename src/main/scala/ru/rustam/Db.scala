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
    "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres")
  val con = {
    val con = new PostgreSQLConnection(configuration)
    Await.result(con.connect, 5 seconds)
    con
  }

}
