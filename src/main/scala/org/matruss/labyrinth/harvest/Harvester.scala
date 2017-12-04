package org.matruss.labyrinth.harvest

import java.net.URI

import org.matruss.labyrinth.harvest.WebHarvester.WebResponse

import scala.io.Source

/**
  * Common trait for link content fetching,
  * extended by web harvester and test substitute
  */
trait Harvester {
  def fetch(uri:URI):WebResponse
}

object Harvester {
  val GoodResponse = 200
  val Encoding = "UTF-8"
}

class TestHarvester extends Harvester {

  /**
    * Test implementation of link content fetch,
    * get content of local file on the file system
    *
    * @param uri  path to the file on local file system
    * @return simulated web response
    */
  override def fetch(uri:URI):WebResponse = {
    val content = Source.fromFile(uri.getPath).getLines.toList.mkString
    WebResponse( BagOfWords (content).extract, 200, "" )
  }
}