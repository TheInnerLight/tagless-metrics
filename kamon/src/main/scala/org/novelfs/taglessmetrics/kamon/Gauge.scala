package org.novelfs.taglessmetrics.kamon

import kamon.tag.TagSet

final case class Gauge(name : String, tags : TagSet)

object Gauge {
  def apply(name: String): Gauge = new Gauge(name, TagSet.Empty)

  implicit class GaugeOps(val gauge: Gauge) extends AnyVal {
    def withTag(key: String, value: String) : Gauge =
      gauge.copy(tags = gauge.tags.withTag(key, value))

    def withTag(key: String, value: Boolean) : Gauge =
      gauge.copy(tags = gauge.tags.withTag(key, value))

    def withTag(key: String, value: Long) : Gauge =
      gauge.copy(tags = gauge.tags.withTag(key, value))
  }
}
