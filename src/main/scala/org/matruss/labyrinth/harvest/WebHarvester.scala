package org.matruss.labyrinth.harvest

import java.net.{URL,URI}

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

  /*private[harvest] def createURI(base:String, link:String):URI = {
    val protocol = (base.split(":"), link.split(":") ) match {
      case (Nil,Nil) => "http"
      case (x, Nil) => x.head
      case (Nil,y) => y.head
      case (x,y) => y.head
      case _ => "http"
    }
  }*/

  def fetch(url:String):WebResponse = {
    import WebHarvester.{Encoding, GoodResponse}

    // val uri = createURI(url)
    val request = new HttpGet( url )
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

  def close():Unit = client.close()
}

object WebHarvester {
  case class WebResponse(links:Iterable[String], responseCode:Int, responseMessage:String)

  private val GoodResponse = 200
  private val Encoding = "UTF-8"

  def apply(cfg:HTTP):WebHarvester = new WebHarvester(cfg)
}
