package org.matruss.labyrinth

import java.net.{URI, URL}

import scala.util.{Failure, Success, Try}

object URIUtils {

  private def addProtocol(uri:String):String = {
    if(uri.split("http:|https:").length > 1) uri
    else if(uri.startsWith("/") ) uri
    else s"http://${uri}"
  }

  def buildURI(base:String, relative:String = ""):URI = {
    Try {
      if(relative.isEmpty) (new URL( addProtocol(base) )).toURI
      else {
        new URL( new URL( addProtocol(base) ), relative).toURI
      }
    }
    match {
      case Success(uri) => uri
      case Failure(error) => (new URL( addProtocol(base) )).toURI
    }
  }

  def buildURI(base:URI, relative:String):URI = {
    Try {
      if(relative.isEmpty) base
      else {
        new URL( base.toURL, addProtocol(relative) ).toURI
      }
    }
    match {
      case Success(uri) => uri
      case Failure(error) => base
    }
  }
}
