package org.matruss.labyrinth.harvest

import scala.util.{Failure, Success, Try}
import scala.xml.XML

import org.matruss.labyrinth.harvest.BagOfWords.Tag

class BagOfWords(private val content:String) {
  private val anchorPattern = "<a href=(\"[^\"]*\")[^<]".r

  def extract:Iterable[String] = {
    def replace(token:String):String = token.replaceAll("\"|>|<","").trim
    def extractAsXML:Iterable[String] = {
      val page = XML.loadString(content)
      page  \\ Tag.Anchor flatMap { _.attribute(Tag.Link) } map { _.toString }
    }
    def extractAsPattern:Iterable[String] = {
      anchorPattern
        .findAllIn(content)
        .mkString
        .split("<a href=")
        .map(replace)
    }
    Try { extractAsXML }
    match {
      case Success(seq) => seq
      case Failure(_) => { extractAsPattern }
    }
  }
}

object BagOfWords {
  object Tag {
    // todo ? what if tag in upper case
    val Anchor = "a"
    val Link = "href"
  }
  def apply(content:String) = new BagOfWords(content)
}
