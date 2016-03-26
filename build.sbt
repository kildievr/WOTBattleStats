name := "WOTBattleStats"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val sprayV = "1.3.3"
  Seq(
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-testkit" % sprayV % "test",
    "org.specs2" %% "specs2-core" % "2.5" % "test",
    "com.typesafe.akka" %% "akka-actor" % "2.4.2",
    "com.github.mauricio" %% "postgresql-async" % "0.2.19",
    "org.json4s" %% "json4s-jackson" % "3.3.0"
  )
}

Revolver.settings: Seq[sbt.Def.Setting[_]]
    