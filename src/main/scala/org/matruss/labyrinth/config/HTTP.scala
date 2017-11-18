package org.matruss.labyrinth.config

import com.typesafe.config.Config

case class HTTP(totalMax:Int, totalRoute:Int)

object HTTP {
  def apply(cfg:Config):HTTP = HTTP(
    cfg.getInt(ConfigNames.HTTP.MaxTotal),
    cfg.getInt(ConfigNames.HTTP.MaxPerRoute)
  )
}
