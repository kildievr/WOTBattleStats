name := "WOTBattleStats"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaV = "2.4.14"
  val akkaHttpV = "10.0.0"
  val log4jV = "2.7"
  Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.github.mauricio" %% "postgresql-async" % "0.2.20",
    "org.json4s" %% "json4s-jackson" % "3.5.0",
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "test",
    "org.log4s" %% "log4s" % "1.3.3",
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jV,
    "org.apache.logging.log4j" % "log4j-core" % log4jV,
    "org.apache.logging.log4j" % "log4j-api" % log4jV,
    "com.lmax" % "disruptor" % "3.3.6",
    "com.typesafe.akka" %% "akka-slf4j" % akkaV
  )
}

Revolver.settings: Seq[sbt.Def.Setting[_]]
    