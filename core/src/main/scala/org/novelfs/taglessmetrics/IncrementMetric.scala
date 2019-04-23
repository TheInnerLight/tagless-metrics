package org.novelfs.taglessmetrics

trait IncrementMetric[F[_], TMetric] {
  def increment(metric : TMetric) : F[Unit]
  def incrementTimes(n : Long)(metric: TMetric) : F[Unit]
}

object IncrementMetric {
  def apply[F[_], TMetric](implicit incrementMetric: IncrementMetric[F, TMetric]): IncrementMetric[F, TMetric] = incrementMetric
}
