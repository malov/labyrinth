package org.matruss.labyrinth.model

import java.net.URI

import scala.xml.Elem

import org.matruss.labyrinth.config.WebSite
import org.matruss.labyrinth.harvest.Harvester
import org.matruss.labyrinth.URIUtils._

/**
  * Main building block of the site, class describing web page (or graph node)
  * Has links to previously seen URI's/edges, and outgoing links/edges to other pages
  *
  * @todo modify for asynchronisity: since links would be followed as futures,
  *       generrate(...) should return a set of futures, which should be eventually
  *       flatmapped
  * @todo add attributes (brought from WebLink class) to show in the final map
  *       for broken or otherwise not followed links
  * @todo add configurable parameter for depth, to break out from sites with too many
  *       levels to follow
  *
  * @param cfg            configuration
  * @param base           URI of the page
  * @param urlSeenBefore  all URIs seen before reaching this page
  * @param service        handler for web service object, to fetch underlying pages
  */
class WebPage(cfg:WebSite, base:URI, urlSeenBefore:Set[WebLink], service:Harvester ) {

  private[model] val links:Set[WebLink] = {
    service
      .fetch(base)
      .links
      .map( WebLink(base, _ ,cfg) )
      .filterNot(_.isExternal)
      .filterNot( l => urlSeenBefore.map(_.comboURI).contains(l.comboURI) )
      .filter(_.toFollow)
      .toSet
  }
  private[model] def generate:Set[WebPage] = {
    links.map( l => WebPage(cfg, buildURI(base,l.relative), urlSeenBefore ++ links, service) )
  }
  def toXml:Elem =
    <page>
      <uri>{base}</uri>
      { generate.map(_.toXml) }
    </page>
}

/** Companion object */
object WebPage{

  def apply( cfg:WebSite, base:URI, urlSeenBefore:Set[WebLink], service:Harvester):WebPage =
    new WebPage(cfg, base, urlSeenBefore, service)
}

