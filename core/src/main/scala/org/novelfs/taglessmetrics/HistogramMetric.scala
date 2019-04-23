package org.novelfs.taglessmetrics

trait HistogramMetric[F[_], TMetric] {
  def record(value : Long)(metric : TMetric) : F[Unit]
  def recordTimes(times : Long)(value : Long)(metric : TMetric) : F[Unit]
}

object HistogramMetric {
  def apply[F[_], TMetric](implicit histogramMetric: HistogramMetric[F, TMetric]): HistogramMetric[F, TMetric] = histogramMetric
}
