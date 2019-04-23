package org.novelfs.taglessmetrics.kamon

final case class Counter(name : String, tags : Map[String, String])

object Counter {
  def apply(name: String): Counter = new Counter(name, Map.empty)

  implicit class CounterOps(val counter: Counter) extends AnyVal {
    def refine(tag : (String, String)) : Counter =
      counter.copy(tags = counter.tags + tag)
  }
}
