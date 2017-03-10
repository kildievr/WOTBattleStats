package ru.rustam

import java.util.function.ToIntFunction

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.datastax.driver.core.Row
import com.datastax.spark.connector.cql.CassandraConnector
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.collection.mutable

trait ShopId {
  def shop: Array[Int] = null

  def filters: mutable.Map[String, Any] = mutable.HashMap.empty[String, Any]

  def clause(s: Sale): Boolean = true
}

trait DateFromTo extends ShopId {
  def from: String = ""

  def to: String = ""

  override def filters: mutable.Map[String, Any] = mutable.HashMap[String, Any]("sale_date >= ?" -> from, "sale_date <= ?" -> to)
}

object ShopIds {
  val cc = CassandraConnector(Spark.sc.getConf)
  val ids: Array[Int] = cc.withSessionDo { session =>
    session.execute(s"select distinct shop_id from ${Schema.schema}.sales;")
  }.all().stream().mapToInt(new ToIntFunction[Row] {
    override def applyAsInt(value: Row): Int = value.getInt(0)
  }).toArray
}

case class ByPeriod(override val from: String, override val to: String) extends DateFromTo {
  override def shop: Array[Int] = ShopIds.ids
}

case class ByShop(override val from: String, override val to: String, override val shop: Array[Int]) extends DateFromTo

case class ByProduct(override val from: String, override val to: String, override val shop: Array[Int], product: Seq[Int]) extends DateFromTo {
  override def clause(s: Sale): Boolean = product.contains(s.productId)
}

case class ByPrice(override val from: String, override val to: String, override val shop: Array[Int], priceFrom: Float, priceTo: Float) extends DateFromTo {
  override def filters: mutable.Map[String, Any] = super.filters += ("price >= ?" -> priceFrom, "price <= ?" -> priceTo)
}

case class ResponseData(data: Array[Sale])

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val byPeriodFormat: RootJsonFormat[ByPeriod] = jsonFormat2(ByPeriod)
  implicit val byShopFormat: RootJsonFormat[ByShop] = jsonFormat3(ByShop)
  implicit val byProductFormat: RootJsonFormat[ByProduct] = jsonFormat4(ByProduct)
  implicit val byPriceFormat: RootJsonFormat[ByPrice] = jsonFormat5(ByPrice)
  implicit val saleFormat: RootJsonFormat[Sale] = jsonFormat7(Sale)
  implicit val responseData: RootJsonFormat[ResponseData] = jsonFormat1(ResponseData)
}
