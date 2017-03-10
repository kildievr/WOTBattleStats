package ru.rustam

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.datastax.spark.connector._
import com.datastax.spark.connector.rdd.CassandraTableScanRDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.concurrent.duration._
import scala.language.postfixOps

case class Sale(shopId: Int, saleDate: String, productId: Int, price: Float, productCount: Int, categoryId: Int, vendorId: Int)

object Schema {
  val schema = "test2"
}

//sbt test -Dcassandra=127.0.0.1
object Main extends App with RouteService {
  implicit val system = ActorSystem("o-labs-actor-system")
  implicit val timeout = Timeout(5 seconds)
  implicit val materializer = ActorMaterializer()
  Http().bindAndHandle(route, "0.0.0.0", 9000)
}

object Spark {

  //конфиг написал прямо в коде для простоты и потому что спарк здесь builtin
  //а buildin он здесь для просто ты запуска sbt run/test
  val conf = new SparkConf()
    .set("spark.master", "local[4]")
    .set("spark.executor.memory", "1g")
    .set("spark.cassandra.connection.host", System.getProperty("cassandra", "localhost"))
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .setAppName("oLabs")

  val sc = new SparkContext(conf)

  val table: CassandraTableScanRDD[Sale] = sc.cassandraTable[Sale](Schema.schema, "sales")

  def select[T <: Iterable[(String, Any)]](kv: T) = {
    var query = table.select("sale_date", "shop_id", "product_id", "price", "product_count", "category_id", "vendor_id")
    kv.foreach(kv => query = query.where(kv._1, kv._2))
    query.repartitionByCassandraReplica(Schema.schema, "sales")
  }
}
