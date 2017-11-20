package org.matruss.labyrinth.harvest

import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.junit.JUnitRunner

import org.matruss.labyrinth.URIUtils._
import org.matruss.labyrinth.config.LabyrinthConfiguration

@RunWith(classOf[JUnitRunner])
class WebHarvestSpec extends FreeSpec with Matchers {
  private val conf = LabyrinthConfiguration( ConfigFactory.load("application.test.conf") )

  "WebHarvester" - {
    val harvester = WebHarvester( conf.httpSettings )
    "should fetch links from the valid web page" in {
      val response = harvester.fetch( buildURI("http://www.google.com") )
      response.responseCode shouldEqual 200
    }
  }
}
