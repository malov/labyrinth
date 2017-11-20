package org.matruss.labyrinth

import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.junit.JUnitRunner

import org.matruss.labyrinth.config.LabyrinthConfiguration

@RunWith(classOf[JUnitRunner])
class LabyrinthSpec extends FreeSpec with Matchers {
  private val conf = LabyrinthConfiguration( ConfigFactory.load("application.test.conf") )

  "Labyrinth" -{
    "should be built" in {}
  }
}