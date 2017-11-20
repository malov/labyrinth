package org.matruss.labyrinth.harvest

import scala.xml.XML
import scala.util.{Failure, Success, Try}

import org.matruss.labyrinth.harvest.BagOfWords.Tag

/**
  * Service class to process content of the web page and extract outgoing links
  *
  * @param content  raw content of the web page
  */
class BagOfWords(private val content:String) {
  private val anchorPattern = "<a href=(\"[^\"]*\")[^<]".r

  /**
    * Extracts links from the web page.
    * First assumes that page is valid XML, and extracts anchor nodes and attributes
    * However, since most of the pages are not valid XML, if first method fails, it tries extract
    * external links based on regex pattern
    *
    * @return outgoing links as strings
    */
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
/** Companion object */
object BagOfWords {
  object Tag {
    // todo ? what if tag in upper case
    val Anchor = "a"
    val Link = "href"
  }
  def apply(content:String) = new BagOfWords(content)
}
