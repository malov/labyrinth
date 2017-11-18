package org.matruss.labyrinth.model

import org.matruss.labyrinth.config.Site
import org.matruss.labyrinth.harvest.WebHarvester

class Page(cfg:Site, url:String, service:WebHarvester) {

  private[this] val content = { ???
    //service.client.execute(url)
  }
  private[this] val links:Iterable[Link] = ???
  private[this] val pages:Iterable[Page] = ???
}

