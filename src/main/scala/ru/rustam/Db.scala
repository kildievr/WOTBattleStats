package ru.rustam

import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.postgresql.util.URLParser

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by Rustam on 26.03.2016.
  */
object Db {

  val createTable: String =
    """CREATE TABLE IF NOT EXISTS stats (
  added         SERIAL PRIMARY KEY,
  timeAdded     INTEGER NOT NULL,
  xp            INTEGER,
  cap           INTEGER,
  defBase       INTEGER,
  map           TEXT,
  frag          INTEGER,
  gold          INTEGER,
  hits          INTEGER,
  spot          INTEGER,
  tier          INTEGER,
  idNum         INTEGER,
  place         INTEGER,
  shots         INTEGER,
  assist        INTEGER,
  damage        INTEGER,
  freeXP        INTEGER,
  result        INTEGER,
  credits       INTEGER,
  pierced       INTEGER,
  vehicle       TEXT,
  battleTier    INTEGER,
  battleTime    TEXT,
  battleType    INTEGER,
  originalXP    INTEGER,
  playerName    TEXT,
  assistRadio   INTEGER,
  assistTrack   INTEGER,
  playerAccount TEXT
);"""
  val configuration       = URLParser.parse("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres")
  lazy val con = Await.result(for {
    connection <- new PostgreSQLConnection(configuration).connect
    _ <- connection.sendQuery(createTable)
  } yield connection, 5 seconds)

}
