package org.matruss.labyrinth.config

object ConfigNames {
  object Labyrinth {
    val Globals = "labyrinth"
    val Http = "http"
    val Site = "site"
  }
  object Global {
    val Output = "output"
  }
  object HTTP {
    val MaxTotal = "total-overall"
    val MaxPerRoute = "total-route"
  }
  object Site {
    val Exclude = "exclude"
  }
}
