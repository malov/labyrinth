package org.matruss.labyrinth.harvest


import scala.util.{Failure, Success, Try}
import scala.xml.XML
import org.matruss.labyrinth.harvest.Bag.Tag

class Bag(private val content:String) {

  def extract:Iterable[String] = {
    def extractAsXML:Iterable[String] = {
      val page = XML.loadString(content)
      page  \\ Tag.Anchor flatMap { _.attribute(Tag.Link) } map { _.toString }
    }
    def extractAsPattern:Iterable[String] = ???

    Try { extractAsXML }
    match {
      case Success(seq) => seq
      case Failure(_) => { extractAsPattern }
    }
  }
}

object Bag {
  object Tag {
    // todo ? what if tag in upper case
    val Anchor = "a"
    val Link = "href"
  }
  def apply(content:String) = new Bag(content)
}
