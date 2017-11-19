package org.matruss.labyrinth.model

import org.matruss.labyrinth.config.Site
import org.matruss.labyrinth.harvest.WebHarvester

import scala.xml.Elem

class Page(cfg:Site, url:String, service:WebHarvester) {

  // private[this] val content
  private[this] val links:Iterable[Link] = {
    service
      .fetch(url)
      .links
      .map( Link(_,cfg) )
  }
  private[this] val pages:Iterable[Page] = {
    links
      .filter(_.toFollow)
      .map( l => Page(cfg, l.url, service) )
  }
  def toXml:Elem = ???
}

object Page{
  def apply(cfg:Site, url:String, service:WebHarvester):Page =
    new Page(cfg, url, service)
}

