package org.matruss.labyrinth.harvest

import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.matruss.labyrinth.config.LabyrinthConfiguration
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WebHarvestSpec extends FreeSpec with Matchers {
  private val conf = LabyrinthConfiguration( ConfigFactory.load("application.test.conf") )

  "WebHarvester" - {
    val harvester = WebHarvester( conf.httpSettings )
    "should fetch links from the valid web page" in {
      val response = harvester.fetch("http://www.google.com")
      response.responseCode shouldEqual 200
    }
    "should add protocol to URL if necessary" in {
      val uri = harvester.createURI("www.google.com","")
      uri.toString shouldEqual "http://www.google.com"
    }
    "should keep protocol of URL if supplied" in {
      val uri = harvester.createURI("https://www.google.com","")
      uri.toString shouldEqual "https://www.google.com"
    }
    "should build correct relative URI" in {
      val uri = harvester.createURI("http://www.google.com","/policy")
      uri.toString shouldEqual "http://www.google.com/policy"
    }
  }
}
