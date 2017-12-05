package org.matruss.labyrinth

import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, PrettyPrinter}
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory
import scopt.OptionParser
import better.files.File
import org.matruss.labyrinth.config.LabyrinthConfiguration
import org.matruss.labyrinth.Labyrinth.LabyrinthParams
import org.matruss.labyrinth.harvest.{AsyncWebHarvester}//, WebHarvester}
import org.matruss.labyrinth.model.{WebLink, WebPage}
import URIUtils._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{ExecutionContext, Future}

/**
  * Main class for application, has all the method to initialize and run application, and generate output
  *
  * @todo make it finish naturally: starting call should be a long-running Future,
  *       which should be flat-mapped, sequence-reversed, then have exit call
  *       in Success call back handler
  * @todo add a REST point to check running status
  *
  * @param cfg  configuration file
  */
class Labyrinth(cfg:LabyrinthConfiguration) {

  private def toXML(page:Elem):Elem = <main>{page}</main>

  private def output(page:Elem):Unit = {
    File(cfg.global.output).overwrite(
      new PrettyPrinter(80,2).format(page)
    )
  }
  def parser:OptionParser[LabyrinthParams] = new OptionParser[LabyrinthParams]("Labyrinth") {
    arg[String]("<URL>").minOccurs(1).maxOccurs(1).action( (x,c) =>
      c.copy(url = x)
    ).text("url to a site for mapping")
  }

  def run(startUrl:String, client:StandaloneAhcWSClient):Future[Elem] = {
    WebPage(
      cfg.site,
      buildURI(startUrl),
      Set.empty[WebLink],
      AsyncWebHarvester(client)
    )
      .toXml
  }
}

/**
  * Companion object for main class, where it is constructed, all methods are run,
  * all errors from below are caught and deal with
  */
object Labyrinth {
  case class LabyrinthParams(url:String = "")

  def main(args: Array[String]): Unit = {
    implicit val system:ActorSystem = ActorSystem()
    implicit val materializer:ActorMaterializer = ActorMaterializer()
    // system.registerOnTermination { System.exit(0) }

    val cfg = LabyrinthConfiguration ( ConfigFactory.load() )
    val maze = new Labyrinth( cfg )

    maze.parser.parse(args, LabyrinthParams() ) match {
      case Some(p) => {
        val client = StandaloneAhcWSClient()
        maze.run(p.url, client)
          .onComplete {
            case Success(elem) => {
              maze.output(elem)
              client.close()
              system.terminate()
              lastRites(Successful, s"Success: completed successfully")
            }
            case Failure(e) => lastRites(Failed, s"Failure: failed building map")
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
