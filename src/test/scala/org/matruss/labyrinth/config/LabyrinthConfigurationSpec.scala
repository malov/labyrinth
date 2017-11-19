package org.matruss.labyrinth.config

import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.junit.JUnitRunner

import scala.util.{Try, Success}

@RunWith(classOf[JUnitRunner])
class LabyrinthConfigurationSpec extends FreeSpec with Matchers {

  "Labyrinth configuration" - {
    "should be parsable if valid" in {
      val conf = Try { LabyrinthConfiguration(ConfigFactory.load("application.test.conf") ) }
      conf shouldBe a [Success[LabyrinthConfiguration]]

      val httpConf = conf.get.httpSettings
      httpConf.totalMax shouldEqual 20
      httpConf.totalRoute shouldEqual 5
    }
  }
}
