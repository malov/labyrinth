package org.matruss.labyrinth.harvest

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
  lazy val client:CloseableHttpClient = HttpClients.custom().setConnectionManager(cm).build()

  def fetch(url:String):WebResponse = {
    import WebHarvester.{Encoding, GoodResponse}

    val request = new HttpGet(url)
    val response = client.execute(request)
    response.getStatusLine.getStatusCode match {
      case code if code == GoodResponse => {
        EntityUtils.toString( response.getEntity, Encoding )
        ???
      }
      case error =>
        WebResponse(Seq.empty[String], error, response.getStatusLine.getReasonPhrase )
    }
  }
}

object WebHarvester {
  case class WebResponse(links:Iterable[String], responseCode:Int, responseMessage:String)

  private val GoodResponse = 200
  private val Encoding = "UTF-8"

  def apply(cfg:HTTP):WebHarvester = new WebHarvester(cfg)
}
