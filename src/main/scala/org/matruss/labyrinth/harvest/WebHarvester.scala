package org.matruss.labyrinth.harvest

import java.net.{URI, URL}
import scala.util.{Failure, Success, Try}

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.util.EntityUtils

import org.matruss.labyrinth.config.HTTP
import org.matruss.labyrinth.harvest.WebHarvester.WebResponse

class WebHarvester(cfg:HTTP) {
  private[this] val cm = {
    val res = new PoolingHttpClientConnectionManager
    res.setMaxTotal(cfg.totalMax)
    res.setDefaultMaxPerRoute(cfg.totalRoute)
    res
  }
  private[this] lazy val client:CloseableHttpClient =
    HttpClients.custom().setConnectionManager(cm).build()

  private[harvest] def createURI(base:String, relativePath:String):URI = {
    def addProtocol(url:String):String = {
      if(url.split("http:|https:").length > 1) url
      else s"http://${url}"
    }

    if(relativePath.isEmpty) (new URL( addProtocol(base) )).toURI
    else {
      new URL( new URL( addProtocol(base) ), relativePath).toURI
    }
  }

  def fetch(base:String, path:String = ""):WebResponse = {
    import WebHarvester.{Encoding, GoodResponse}

    Try {
      val request = new HttpGet( createURI( base, path) )
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
