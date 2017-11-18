package org.matruss.labyrinth.config

import com.typesafe.config.Config

case class Global(setting:String)

object Global {

  def apply(cfg:Config):Global = Global(
    setting = cfg.getString(ConfigNames.Global.Setting)
  )
}