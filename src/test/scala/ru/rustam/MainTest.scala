package ru.rustam

import org.specs2.mutable.Specification
import spray.http.HttpEntity
import spray.http.MediaTypes._
import spray.testkit.Specs2RouteTest

/**
  * Created by Rustam on 26.03.2016.
  */
class MainTest extends Specification with Specs2RouteTest with MyService {
  override implicit val execContext = system.dispatcher

  override def actorRefFactory = system

  "Check test" should {
    "pass" in {
      Post("/sendStat", HttpEntity(`application/json`, "{}")) ~> route ~> check {
        handled must beTrue
      }
    }
  }
}
