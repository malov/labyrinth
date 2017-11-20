package org.matruss.labyrinth.model

import java.net.URI
import scala.xml.Elem

import org.matruss.labyrinth.config.WebSite
import org.matruss.labyrinth.harvest.WebHarvester
import org.matruss.labyrinth.URIUtils._

class WebPage(cfg:WebSite, base:URI, urlSeenBefore:Seq[WebLink], service:WebHarvester ) {

  private[model] val links:Iterable[WebLink] = {
    service
      .fetch(base)
      .links
      .map( WebLink(base, _ ,cfg) )
      .filterNot(_.isExternal)
      .filterNot( urlSeenBefore.contains ) // implies comparison of WebLink objects, which are case classes
  }
  private[model] val pages:Iterable[WebPage] = {
    links
      .filter(_.toFollow)
      .map( l => WebPage(cfg, buildURI(base,l.relative), urlSeenBefore ++ links, service) )
  }
  def toXml:Elem =
    <page>
      <uri>base</uri>
      { pages.map(_.toXml) }
    </page>
}

object WebPage{

  def apply( cfg:WebSite, base:URI, urlSeenBefore:Seq[WebLink], service:WebHarvester):WebPage =
    new WebPage(cfg, base, urlSeenBefore, service)
}

