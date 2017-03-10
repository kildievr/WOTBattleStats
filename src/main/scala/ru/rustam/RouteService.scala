package ru.rustam

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import org.json4s._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait RouteService extends JsonSupport {

  implicit val formats = DefaultFormats

  def handle[T <: ShopId](entity: Directive1[T]): Route = entity { request =>
    complete {
      Future {
        ResponseData(Spark.sc.union(request.shop.map(shopId => {
          Spark.select {
            request.filters += ("shop_id = ?" -> shopId)
          }.filter(request.clause)
        }
        )).collect())
      }
    }
  }

  //нет проверки входящих параметров и в случае чего отдаются 500, хотя в большом приложении это всё надо реализовывать
  val route: Route = logRequestResult("akka-http-microservice") {
    post {
      pathPrefix("test") {
        path("get-sales-by-period")(handle(entity(as[ByPeriod]))) ~
          path("get-sales-by-shop")(handle(entity(as[ByShop]))) ~
          path("get-sales-by-shop-product")(handle(entity(as[ByProduct]))) ~
          path("get-sales-by-shop-price")(handle(entity(as[ByPrice])))
      }
    }
  }
}

