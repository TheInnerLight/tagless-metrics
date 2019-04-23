package org.novelfs.taglessmetrics.kamon

final case class Histogram(name : String, tags : Map[String, String])

object Histogram {
  def apply(name: String): Histogram = new Histogram(name, Map.empty)

  implicit class THistogramOps(val hist: Histogram) extends AnyVal {
    def refine(tag : (String, String)) : Histogram =
      hist.copy(tags = hist.tags + tag)
  }
}
