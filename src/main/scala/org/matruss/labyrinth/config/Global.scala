package org.matruss.labyrinth.config

import com.typesafe.config.Config

case class Global(output:String)

object Global {

  def apply(cfg:Config):Global = Global(
    output = cfg.getString(ConfigNames.Global.Output)
  )
}