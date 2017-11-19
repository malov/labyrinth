package org.matruss.labyrinth.model

import org.matruss.labyrinth.config.WebSite

case class WebLink(url:String, private val cfg:WebSite) {

  def toFollow:Boolean = true // todo must be defined by configuration rules
}
