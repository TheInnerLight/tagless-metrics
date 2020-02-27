package org.novelfs.taglessmetrics.kamon

import kamon.tag.TagSet

final case class Timer(name : String, tags : TagSet)

object Timer {
  def apply(name: String): Timer = new Timer(name, TagSet.Empty)

  implicit class TimerOps(val timer: Timer) extends AnyVal {
    def withTag(key: String, value: String) : Timer =
      timer.copy(tags = timer.tags.withTag(key, value))

    def withTag(key: String, value: Boolean) : Timer =
      timer.copy(tags = timer.tags.withTag(key, value))

    def withTag(key: String, value: Long) : Timer =
      timer.copy(tags = timer.tags.withTag(key, value))
  }
}
