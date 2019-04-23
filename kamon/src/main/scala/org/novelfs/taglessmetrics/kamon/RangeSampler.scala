package org.novelfs.taglessmetrics.kamon

final case class RangeSampler(name : String, tags : Map[String, String])

object RangeSampler {
  def apply(name: String): RangeSampler = new RangeSampler(name, Map.empty)

  implicit class RangeSamplerOps(val rangeSampler: RangeSampler) extends AnyVal {
    def refine(tag : (String, String)) : RangeSampler =
      rangeSampler.copy(tags = rangeSampler.tags + tag)
  }
}
