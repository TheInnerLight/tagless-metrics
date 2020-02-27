package org.novelfs.taglessmetrics.kamon

import kamon.tag.TagSet

final case class Counter(name : String, tags : TagSet)

object Counter {
  def apply(name: String): Counter = new Counter(name, TagSet.Empty)

  implicit class CounterOps(val counter: Counter) extends AnyVal {
    def withTag(key: String, value: String) : Counter =
      counter.copy(tags = counter.tags.withTag(key, value))

    def withTag(key: String, value: Boolean) : Counter =
      counter.copy(tags = counter.tags.withTag(key, value))

    def withTag(key: String, value: Long) : Counter =
      counter.copy(tags = counter.tags.withTag(key, value))
  }
}
