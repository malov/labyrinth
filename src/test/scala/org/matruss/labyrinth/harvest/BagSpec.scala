package org.matruss.labyrinth.harvest

import scala.xml.XML
import scala.io.Source
import scala.util.{Failure, Success, Try}

import org.junit.runner.RunWith
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BagSpec extends FreeSpec with Matchers {

  "Bag class" - {
    "should parse valid html" in {
      val source = Source.fromFile("src/test/resources/sample.html").getLines.toStream
      Try { Bag( source.mkString ) } shouldBe a [Success[Bag]]
    }
    "should extract links from valid XML page" in {
      val source = Source.fromFile("src/test/resources/sample.html").getLines.toStream
      Try { XML.loadString( source.mkString ) } shouldBe a [Success[String]]

      val bag = Bag( source.mkString )
      bag.extract.size shouldEqual 2
    }
    "should still extract links from invalid XML page" in {
      val source = Source.fromFile("src/test/resources/monzo.html").getLines.toStream
      Try { XML.loadString( source.mkString ) } shouldBe a [Failure[_]]
    }
  }
}
