package org.matruss.labyrinth

sealed trait ExitStatus {
  def code:Int
}

case object Successful extends ExitStatus {
  override val code:Int = 0
}

case object Failed extends ExitStatus {
  override val code:Int = 1
}
