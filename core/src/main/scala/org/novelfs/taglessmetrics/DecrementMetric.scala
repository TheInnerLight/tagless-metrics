package org.novelfs.taglessmetrics

trait DecrementMetric[F[_], TMetric] {
  def decrement(metric : TMetric) : F[Unit]
  def decrementTimes(n : Long)(metric: TMetric) : F[Unit]
}

object DecrementMetric {
  def apply[F[_], TMetric](implicit decrementMetric: DecrementMetric[F, TMetric]): DecrementMetric[F, TMetric] = decrementMetric
}
