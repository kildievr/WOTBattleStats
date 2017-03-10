package ru.rustam

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.datastax.spark.connector.cql.CassandraConnector
import org.json4s.jackson.Serialization.write
import org.log4s.getLogger
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration.DurationInt

class MainTest extends WordSpec with Matchers with ScalatestRouteTest with RouteService {

  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(50.seconds)
  private[this] val logger  = getLogger

  val allRows = Array(
    Sale(1, "2017-03-01 20:01:00+0400", 1, 10, 1, 1, 1),
    Sale(1, "2017-03-01 20:01:00+0400", 1, 15, 1, 1, 1),
    Sale(2, "2017-03-01 20:02:00+0400", 2, 20, 1, 1, 1),
    Sale(3, "2017-03-01 20:03:00+0400", 3, 30, 1, 1, 1),
    Sale(4, "2017-03-01 20:04:00+0400", 4, 40, 1, 1, 1),
    Sale(5, "2017-03-01 20:05:00+0400", 5, 50, 1, 1, 1),
    Sale(6, "2017-03-01 20:06:00+0400", 6, 60, 1, 1, 1)
  )


  "Check test" should {
    "get-sales-by-period" in {
      val dataToSend = write(ByPeriod("2015-03-01 16:00", "2018-03-01 16:07"))
      Post("/test/get-sales-by-period", HttpEntity(`application/json`, dataToSend)) ~> route ~> check {
        val resp = responseAs[ResponseData]
        val diff = resp.data.diff(allRows)
        (resp.data.forall(x => allRows.contains(x)) && resp.data.length == allRows.length) shouldBe true
      }
    }
    "get-sales-by-shop" in {
      val dataToSend = write(ByShop("2015-03-01 16:00", "2018-03-01 16:07", Array(2)))
      Post("/test/get-sales-by-shop", HttpEntity(`application/json`, dataToSend)) ~> route ~> check {
        val resp = responseAs[ResponseData]
        resp.data shouldBe Array(Sale(2, "2017-03-01 20:02:00+0400", 2, 20, 1, 1, 1))
      }
    }
    "get-sales-by-shop-product" in {
      val dataToSend = write(ByProduct("2015-03-01 16:00", "2018-03-01 16:07", Array(2), Array(2, 3)))
      Post("/test/get-sales-by-shop-product", HttpEntity(`application/json`, dataToSend)) ~> route ~> check {
        val resp = responseAs[ResponseData]
        resp.data shouldBe Array(Sale(2, "2017-03-01 20:02:00+0400", 2, 20.0f, 1, 1, 1))
      }
    }
    "get-sales-by-shop-price" in {
      val dataToSend = write(ByPrice("2015-03-01 16:00", "2018-03-01 16:07", Array(1), 11, 100))
      Post("/test/get-sales-by-shop-price", HttpEntity(`application/json`, dataToSend)) ~> route ~> check {
        val resp = responseAs[ResponseData]
        resp.data shouldBe Array(Sale(1, "2017-03-01 20:01:00+0400", 1, 15, 1, 1, 1))
      }
    }
  }

  override protected def beforeAll(): Unit = {
    val commands = List(
      s"CREATE KEYSPACE ${Schema.schema}\n  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };",
      s"""create table if not exists ${Schema.schema}.sales (
        |	shop_id int,
        |	sale_date timestamp,
        |	product_id int,
        |	price float,
        |	product_count int,
        |	category_id int,
        |	vendor_id int,
        |	primary key (shop_id, sale_date, product_id, vendor_id, category_id,  product_count, price)
        |);""".stripMargin,
      s"insert into ${Schema.schema}.sales (sale_date, shop_id, product_id, price, product_count, category_id, vendor_id) values ('2017-03-01 20:01+0400', 1, 1, 10, 1, 1, 1);",
      s"insert into ${Schema.schema}.sales (sale_date, shop_id, product_id, price, product_count, category_id, vendor_id) values ('2017-03-01 20:01+0400', 1, 1, 15, 1, 1, 1);",
      s"insert into ${Schema.schema}.sales (sale_date, shop_id, product_id, price, product_count, category_id, vendor_id) values ('2017-03-01 20:02+0400', 2, 2, 20, 1, 1, 1);",
      s"insert into ${Schema.schema}.sales (sale_date, shop_id, product_id, price, product_count, category_id, vendor_id) values ('2017-03-01 20:03+0400', 3, 3, 30, 1, 1, 1);",
      s"insert into ${Schema.schema}.sales (sale_date, shop_id, product_id, price, product_count, category_id, vendor_id) values ('2017-03-01 20:04+0400', 4, 4, 40, 1, 1, 1);",
      s"insert into ${Schema.schema}.sales (sale_date, shop_id, product_id, price, product_count, category_id, vendor_id) values ('2017-03-01 20:05+0400', 5, 5, 50, 1, 1, 1);",
      s"insert into ${Schema.schema}.sales (sale_date, shop_id, product_id, price, product_count, category_id, vendor_id) values ('2017-03-01 20:06+0400', 6, 6, 60, 1, 1, 1);"
    )

    val cc = CassandraConnector(Spark.sc.getConf)
    cc.withSessionDo { session =>
      commands.foreach(command =>
      try {
        session.execute(command)
      } catch {
        case th: Throwable =>
          logger.error(th)("Error!")
      })
    }
  }
}
