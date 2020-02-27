package org.novelfs.taglessmetrics.kamon

import kamon.tag.TagSet

final case class RangeSampler(name : String, tags : TagSet)

object RangeSampler {
  def apply(name: String): RangeSampler = new RangeSampler(name, TagSet.Empty)

  implicit class RangeSamplerOps(val rangeSampler: RangeSampler) extends AnyVal {
    def withTag(key: String, value: String) : RangeSampler =
      rangeSampler.copy(tags = rangeSampler.tags.withTag(key, value))

    def withTag(key: String, value: Boolean) : RangeSampler =
      rangeSampler.copy(tags = rangeSampler.tags.withTag(key, value))

    def withTag(key: String, value: Long) : RangeSampler =
      rangeSampler.copy(tags = rangeSampler.tags.withTag(key, value))
  }
}
