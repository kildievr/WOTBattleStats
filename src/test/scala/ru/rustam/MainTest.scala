package ru.rustam

import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.json4s.jackson.Serialization.write
import org.scalatest._

/**
  * Created by Rustam on 26.03.2016.
  */
class MainTest extends FlatSpec with Matchers with ScalatestRouteTest with Service {

  "Check test" should "pass" in {
    val dataToSend = write(WotData(0, 0, 0, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", 0, "10.10.10 10:10:10", 0, 0, "", 0, 0, ""))
    Post("/sendStat", HttpEntity(`application/json`, dataToSend)) ~> route ~> check {
      val resp = responseAs[String]
      resp shouldBe s"Got $dataToSend and saved to db."
    }
  }
}
