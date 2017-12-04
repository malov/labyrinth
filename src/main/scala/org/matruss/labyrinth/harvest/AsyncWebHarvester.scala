package org.matruss.labyrinth.harvest

import java.net.URI

import scala.concurrent.Future

import play.api.libs.ws.ahc.{StandaloneAhcWSClient, StandaloneAhcWSRequest, StandaloneAhcWSResponse}
import org.matruss.labyrinth.harvest.WebHarvester.WebResponse

class AsyncWebHarvester(wsClient:StandaloneAhcWSClient) extends Harvester {

  def fetch(uri:URI):Future[WebResponse] = {
    import Harvester.GoodResponse

    wsClient
      .url(uri.toString)
      .get()
      .map { resp =>
        if(resp.status == GoodResponse)
          WebResponse(
            BagOfWords( resp.body[String] ).extract,
            resp.status,
            resp.statusText
          )
        else
          WebResponse(Seq.empty[String], resp.status, resp.statusText)
      }
  }
}
