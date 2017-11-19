package org.matruss.labyrinth.harvest

import scala.io.Source
import org.scalatest.{FreeSpec, Matchers}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.{Success, Try}

@RunWith(classOf[JUnitRunner])
class BagSpec extends FreeSpec with Matchers {

  private val source = Source.fromFile("src/test/resources/sample.html").getLines.toStream

  "Bag class" - {
    "should parse valid html" in {
      Try { Bag( source.mkString ) } shouldBe a [Success[Bag]]
    }
    "should extract links from page source" in {
      val bag = Bag( source.mkString )
      bag.extract.size shouldEqual 2
    }
  }
}
