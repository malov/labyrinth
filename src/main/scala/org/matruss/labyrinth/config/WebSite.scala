package org.matruss.labyrinth.config

import com.typesafe.config.Config

case class WebSite(setting:String)

object WebSite {

  def apply(cfg:Config):WebSite = WebSite(
    setting = cfg.getString(ConfigNames.Site.Setting)
  )
}