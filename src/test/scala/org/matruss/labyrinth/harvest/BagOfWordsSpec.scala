package org.matruss.labyrinth.harvest

import scala.xml.XML
import scala.io.Source
import scala.util.{Failure, Success, Try}

import org.junit.runner.RunWith
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BagOfWordsSpec extends FreeSpec with Matchers {

  "Bag of Words class" - {
    "should parse valid html" in {
      val source = Source.fromFile("src/test/resources/sample.html").getLines.toStream
      Try { BagOfWords( source.mkString ) } shouldBe a [Success[_]]
    }
    "should extract links from valid XML page" in {
      val source = Source.fromFile("src/test/resources/sample.html").getLines.toStream
      Try { XML.loadString( source.mkString ) } shouldBe a [Success[_]]

      val bag = BagOfWords( source.mkString )
      bag.extract.size shouldEqual 2
    }
    "should still extract links from invalid XML page" in {
      val source = Source.fromFile("src/test/resources/monzo.html").getLines.toStream
      Try { XML.loadString( source.mkString ) } shouldBe a [Failure[_]]

      val bag = BagOfWords( source.mkString )
      val links = bag.extract
      links.size shouldEqual 24
    }
  }
}
