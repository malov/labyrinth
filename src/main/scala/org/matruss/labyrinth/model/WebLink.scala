package org.matruss.labyrinth.model

import java.net.URI

import org.matruss.labyrinth.config.WebSite
import org.matruss.labyrinth.URIUtils._

/**
  * Class describing page links/graph edges and their properties:
  *  - is outgoing link is external or not
  *  - is it restricted by some other rules (defined in configuration) to be followed
  *
  * @param base     URI of the page this link was found on
  * @param relative link to the outgoing page
  * @param cfg      configuration
  */
case class WebLink(base:URI, relative:String, private val cfg:WebSite) {
  val comboURI:URI = buildURI(base, relative)

  def isExternal:Boolean =
    base.getHost != comboURI.getHost

  def toFollow:Boolean =
    !cfg.exclude.exists( comboURI.getPath.contains )

  override def equals(that: Any): Boolean = {
    if (that.isInstanceOf[WebLink])
      that.asInstanceOf[WebLink].comboURI == comboURI
    else false
  }
}
