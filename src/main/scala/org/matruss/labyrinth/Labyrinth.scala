package org.matruss.labyrinth

import com.typesafe.config.ConfigFactory
import org.matruss.labyrinth.Labyrinth.LabyrinthParams
import scopt.OptionParser
import org.matruss.labyrinth.config.LabyrinthConfiguration

class Labyrinth(cfg:LabyrinthConfiguration) {

  // def validate:Boolean = true
  def run:Unit = {}
  // def init:Unit = {}
  def parser:OptionParser[LabyrinthParams] = new OptionParser[LabyrinthParams]("Labyrinth") {
    arg[String]("<URL>").minOccurs(1).maxOccurs(1).action( (x,c) =>
      c.copy(url = x)
    ).text("url to map")
  }
}

object Labyrinth {
  case class LabyrinthParams(url:String = "")

  object ExitStatus {
    val Success = 0
    val Failure = 1
  }

  def main(args: Array[String]): Unit = {
    val cfg = LabyrinthConfiguration ( ConfigFactory.load() )
    val mapper = new Labyrinth( cfg )

    mapper.parser.parse(args, LabyrinthParams() ) match {
      case Some(p) =>
      case None => lastRites(ExitStatus.Success, s"Failure: arguments parsing")
    }
  }

  private def lastRites(code:Int, message:String) {
    // todo log message
    System.exit(code)
  }
}
