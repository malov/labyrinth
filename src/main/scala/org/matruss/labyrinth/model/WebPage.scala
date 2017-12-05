package org.matruss.labyrinth.model

import java.net.URI

import scala.xml.Elem
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import org.matruss.labyrinth.config.WebSite
import org.matruss.labyrinth.harvest.Harvester
import org.matruss.labyrinth.URIUtils._
import org.matruss.labyrinth.harvest.WebHarvester.WebResponse


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
class WebPage(cfg:WebSite, base:URI, urlSeenBefore:Set[WebLink], service:Harvester )(implicit ec:ExecutionContext) {

  private[model] val links:Set[WebLink] = {
    // blocking because we need all links materialized before proceeding
    val content = Await.result(service.fetch(base), 1 second)
    content
      .links
      .map( WebLink(base, _ ,cfg) )
      .filterNot(_.isExternal)
      .filterNot( l => urlSeenBefore.map(_.comboURI).contains(l.comboURI) )
      .filter(_.toFollow)
      .toSet
  }

  private[model] def generate:Set[Future[WebPage]] = {
    links.map(l => Future( WebPage(cfg, buildURI(base,l.relative), urlSeenBefore ++ links, service) ) )
  }

  def toXml:Future[Elem] =
    Future {
      <page>
        <uri>{base}</uri>
        { Future.traverse(generate)(ws => ws.map(_.toXml) ) }
      </page>
    }
}

/** Companion object */
object WebPage{

  def apply( cfg:WebSite, base:URI, urlSeenBefore:Set[WebLink], service:Harvester)(implicit ec:ExecutionContext):WebPage =
    new WebPage(cfg, base, urlSeenBefore, service)
}

