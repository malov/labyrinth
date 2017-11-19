package org.matruss.labyrinth.model

import org.matruss.labyrinth.config.Site

case class Link(url:String, private val cfg:Site) {

  def toFollow:Boolean = true // todo must be defined by configuration rules
}
