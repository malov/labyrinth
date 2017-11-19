package org.matruss.labyrinth.model

import org.matruss.labyrinth.config.WebSite
import org.matruss.labyrinth.harvest.WebHarvester

import scala.xml.Elem

class WebPage(cfg:WebSite, url:String, service:WebHarvester) {

  // private[this] val content
  private[this] val links:Iterable[WebLink] = {
    service
      .fetch(url)
      .links
      .map( WebLink(_,cfg) )
  }
  private[this] val pages:Iterable[WebPage] = {
    links
      .filter(_.toFollow)
      .map( l => WebPage(cfg, l.url, service) )
  }
  def toXml:Elem = ???
}

object WebPage{
  def apply(cfg:WebSite, url:String, service:WebHarvester):WebPage =
    new WebPage(cfg, url, service)
}

