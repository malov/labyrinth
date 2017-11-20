package org.matruss.labyrinth.model

import java.net.URI

import org.matruss.labyrinth.config.WebSite
import org.matruss.labyrinth.URIUtils._

case class WebLink(base:URI, relative:String, private val cfg:WebSite) {
  private[this] val comboURI:URI = buildURI(base, relative)

  def isExternal:Boolean =
    base.getHost != comboURI.getHost

  def toFollow:Boolean =
    !cfg.exclude.exists( comboURI.getPath.contains )
}
