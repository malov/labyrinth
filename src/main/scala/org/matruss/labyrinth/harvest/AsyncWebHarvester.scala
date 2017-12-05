package org.matruss.labyrinth.harvest

import java.net.URI

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import org.matruss.labyrinth.harvest.WebHarvester.WebResponse

class AsyncWebHarvester(wsClient:StandaloneAhcWSClient)(implicit ec:ExecutionContext) extends Harvester {

  def fetch(uri:URI):Future[WebResponse] = {
    import Harvester.GoodResponse

    wsClient
      .url(uri.toString)
      .get()
      .map { resp =>
        if(resp.status == GoodResponse)
          WebResponse(
            BagOfWords( resp.body ).extract,
            resp.status,
            resp.statusText
          )
        else
          WebResponse(Seq.empty[String], resp.status, resp.statusText)
      }
  }
}

object AsyncWebHarvester {
  def apply(wsClient:StandaloneAhcWSClient)(implicit ec:ExecutionContext) = new AsyncWebHarvester(wsClient)
}
