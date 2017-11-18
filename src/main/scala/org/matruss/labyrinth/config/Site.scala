package org.matruss.labyrinth.config

import com.typesafe.config.Config

case class Site(setting:String)

object Site {

  def apply(cfg:Config):Site = Site(
    setting = cfg.getString(ConfigNames.Site.Setting)
  )
}