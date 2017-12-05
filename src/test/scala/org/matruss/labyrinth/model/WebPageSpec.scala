package org.matruss.labyrinth.model

import java.net.URI
import scala.util.{Success, Try}

import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import org.matruss.labyrinth.config.LabyrinthConfiguration
/*import org.matruss.labyrinth.harvest.TestHarvester

@RunWith(classOf[JUnitRunner])
class WebPageSpec extends FreeSpec with Matchers {
  private val conf = LabyrinthConfiguration( ConfigFactory.load("application.test.conf") )
  private val service = new TestHarvester

  "Web Page class" - {
    "should be build from proper content" ignore {
      Try {
        WebPage(
          conf.site,
          new URI("file:///src/test/resources/sample.html"),
          Set.empty[WebLink],
          service
        )
      } shouldBe a [Success[_]]
    }
    "should correctly defined which links to follow" in {}
    "should build correct XML" in {}
  }
}
*/