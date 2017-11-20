package org.matruss.labyrinth.model

import java.net.URI

import org.matruss.labyrinth.config.WebSite

case class WebLink(base:URI, relative:String, private val cfg:WebSite) {

  def isExternal:Boolean = ???
  def toFollow:Boolean = ???
}
