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
    "should fetch links from the valid web page" in {
      val harvester = WebHarvester( conf.httpSettings )
    }
  }
}
