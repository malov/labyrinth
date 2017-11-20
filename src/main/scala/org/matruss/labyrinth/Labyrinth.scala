package org.matruss.labyrinth

import scala.util.{Failure, Success, Try}
import scala.xml.Elem

import com.typesafe.config.ConfigFactory
import scopt.OptionParser

import org.matruss.labyrinth.config.LabyrinthConfiguration
import org.matruss.labyrinth.Labyrinth.LabyrinthParams
import org.matruss.labyrinth.harvest.WebHarvester
import org.matruss.labyrinth.model.{WebLink, WebPage}
import URIUtils._

/**
  * Main class for application, has all the method to initialize and run application, and generate output
  *
  * @param cfg  configuration file
  */
class Labyrinth(cfg:LabyrinthConfiguration) {

  private def toXML(page:Elem):Elem = <main>{page}</main>

  def parser:OptionParser[LabyrinthParams] = new OptionParser[LabyrinthParams]("Labyrinth") {
    arg[String]("<URL>").minOccurs(1).maxOccurs(1).action( (x,c) =>
      c.copy(url = x)
    ).text("url to a site for mapping")
  }

  def init:WebHarvester = WebHarvester(cfg.httpSettings)

  def run(startUrl:String, service:WebHarvester):ExitStatus = {
    val x = toXML(
      WebPage( cfg.site, buildURI(startUrl), Set.empty[WebLink], service).toXml
    )
    x
    service.close()
    Successful
  }
}

/**
  * Companion object for main class, where it is constructed, all methods are run,
  * all errors from below are caught and deal with
  */
object Labyrinth {
  case class LabyrinthParams(url:String = "")

  def main(args: Array[String]): Unit = {
    val cfg = LabyrinthConfiguration ( ConfigFactory.load() )
    val maze = new Labyrinth( cfg )

    maze.parser.parse(args, LabyrinthParams() ) match {
      case Some(p) => {
        Try { maze.run( p.url, maze.init ) }
        match {
          case Success(status) => lastRites(status, s"Success: application finished")
          case Failure(ex) =>
            val x = ex
            lastRites(Failed, s"Failure: application failed")
        }
      }
      case None => lastRites(Failed, s"Failure: arguments parsing")
    }
  }

  private def lastRites(status:ExitStatus, message:String) {
    // todo log message
    System.exit(status.code)
  }
}
