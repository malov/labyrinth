package org.matruss.labyrinth.config

import com.typesafe.config.Config

case class LabyrinthConfiguration(global:Global, httpSettings:HTTP)

object LabyrinthConfiguration {
  def apply(cfg:Config):LabyrinthConfiguration = LabyrinthConfiguration(
    global = Global( cfg.getConfig(ConfigNames.Labyrinth.Globals) ),
    httpSettings = HTTP( cfg.getConfig(ConfigNames.Labyrinth.Http) )
  )
}