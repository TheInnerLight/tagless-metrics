package org.novelfs.taglessmetrics.kamon

import kamon.tag.TagSet

final case class Histogram(name : String, tags : TagSet)

object Histogram {
  def apply(name: String): Histogram = new Histogram(name, TagSet.Empty)

  implicit class THistogramOps(val hist: Histogram) extends AnyVal {
    def withTag(key: String, value: String) : Histogram =
      hist.copy(tags = hist.tags.withTag(key, value))

    def withTag(key: String, value: Boolean) : Histogram =
      hist.copy(tags = hist.tags.withTag(key, value))

    def withTag(key: String, value: Long) : Histogram =
      hist.copy(tags = hist.tags.withTag(key, value))
  }
}
