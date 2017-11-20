package org.matruss.labyrinth

import java.net.{URI, URL}

object URIUtils {

  private def addProtocol(uri:String):String = {
    if(uri.split("http:|https:").length > 1) uri
    else if(uri.startsWith("/") ) uri
    else s"http://${uri}"
  }

  def buildURI(base:String, relative:String = ""):URI = {

    if(relative.isEmpty) (new URL( addProtocol(base) )).toURI
    else {
      new URL( new URL( addProtocol(base) ), relative).toURI
    }
  }
  def buildURI(base:URI, relative:String):URI = {
    if(relative.isEmpty) base
    else new URL( base.toURL, addProtocol(relative) ).toURI
  }
}
