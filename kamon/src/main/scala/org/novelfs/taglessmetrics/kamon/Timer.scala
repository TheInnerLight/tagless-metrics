package org.novelfs.taglessmetrics.kamon

final case class Timer(name : String, tags : Map[String, String])

object Timer {
  def apply(name: String): Timer = new Timer(name, Map.empty)

  implicit class TimerOps(val timer: Timer) extends AnyVal {
    def refine(tag : (String, String)) : Timer =
      timer.copy(tags = timer.tags + tag)
  }
}
