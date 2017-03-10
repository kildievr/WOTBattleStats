name := "O-labs"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaV = "2.4.17"
  val akkaHttpV = "10.0.4"
  val log4jV = "2.8.1"
  val sparkV = "2.1.0"
  Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" % "akka-remote_2.11" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "org.log4s" %% "log4s" % "1.3.4",
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jV,
    "org.apache.logging.log4j" % "log4j-core" % log4jV,
    "org.apache.logging.log4j" % "log4j-api" % log4jV,
    "org.apache.logging.log4j" % "log4j-1.2-api" % log4jV,
    "com.dorkbox" % "MinLog-SLF4J" % "1.9",
    "com.lmax" % "disruptor" % "3.3.6",
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" % "akka-http-spray-json_2.11" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "test",
    "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0",
    "org.apache.spark" %% "spark-core" % sparkV,
    "org.apache.spark" %% "spark-sql" % sparkV
  )
}

excludeDependencies ++= Seq(
  "org.slf4j" % "slf4j-log4j12",
  "log4j" % "log4j",
  "com.esotericsoftware.minlog" % "minlog")

Revolver.settings: Seq[sbt.Def.Setting[_]]
    