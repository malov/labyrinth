package org.matruss.labyrinth.harvest

import java.net.{URI, URL}
import scala.util.{Failure, Success, Try}

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.util.EntityUtils

import org.matruss.labyrinth.config.HTTP
import org.matruss.labyrinth.harvest.WebHarvester.WebResponse

/**
  * Service class to fetch content of the web page based on page URI
  *
  * @param cfg  configuration
  */
class WebHarvester(cfg:HTTP) {
  private[this] val cm = {
    val res = new PoolingHttpClientConnectionManager
    res.setMaxTotal(cfg.totalMax)
    res.setDefaultMaxPerRoute(cfg.totalRoute)
    res
  }
  private[this] lazy val client:CloseableHttpClient =
    HttpClients.custom().setConnectionManager(cm).build()

  def fetch(uri:URI):WebResponse = {
    import WebHarvester.{Encoding, GoodResponse}

    Try {
      val request = new HttpGet( uri )
      val response = client.execute(request)
      response.getStatusLine.getStatusCode match {
        case code if code == GoodResponse => {
          WebResponse(
            BagOfWords( EntityUtils.toString( response.getEntity, Encoding ) ).extract,
            code,
            response.getStatusLine.getReasonPhrase
          )
        }
        case error =>
          WebResponse(Seq.empty[String], error, response.getStatusLine.getReasonPhrase )
      }
    }
    match {
      case Success(resp) => resp
      case Failure(error) => WebResponse(Seq.empty[String], 1, error.getMessage )
    }
  }

  def close():Unit = client.close()
}

object WebHarvester {
  case class WebResponse(links:Iterable[String], responseCode:Int, responseMessage:String)

  private val GoodResponse = 200
  private val Encoding = "UTF-8"

  def apply(cfg:HTTP):WebHarvester = new WebHarvester(cfg)
}
