package org.matruss.labyrinth.harvest


import scala.util.{Failure, Success, Try}
import scala.xml.XML
import org.matruss.labyrinth.harvest.Bag.Tag

class Bag(private val content:String) {

  def extract:Iterable[String] = {
    Try {
      val page = XML.loadString(content)
      val attributes = page  \\ Tag.Anchor flatMap { node =>
        node.attribute(Tag.Link)
      }
      attributes.map(_.toString)
    }
    match {
      case Success(seq) => seq
      case Failure(e) =>
        val x = e
        Seq.empty[String] // todo log errors as well
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
