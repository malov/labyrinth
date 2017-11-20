package org.matruss.labyrinth.config

import scala.collection.JavaConverters._
import com.typesafe.config.Config

case class WebSite(exclude:Seq[String])

object WebSite {

  def apply(cfg:Config):WebSite = WebSite(
    exclude = cfg.getStringList(ConfigNames.Site.Exclude).asScala
  )
}