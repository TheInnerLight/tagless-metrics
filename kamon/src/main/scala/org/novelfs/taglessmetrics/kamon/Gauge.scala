package org.novelfs.taglessmetrics.kamon

final case class Gauge(name : String, tags : Map[String, String])

object Gauge {
  def apply(name: String): Gauge = new Gauge(name, Map.empty)

  implicit class GaugeOps(val gauge: Gauge) extends AnyVal {
    def refine(tag : (String, String)) : Gauge =
      gauge.copy(tags = gauge.tags + tag)
  }
}
