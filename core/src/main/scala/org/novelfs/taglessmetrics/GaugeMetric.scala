package org.novelfs.taglessmetrics

trait GaugeMetric[F[_], TMetric] {
  def raise(n : Double)(metric: TMetric) : F[Unit]
  def lower(n : Double)(metric: TMetric) : F[Unit]
}

object GaugeMetric {
  def apply[F[_], TMetric](implicit gaugeMetric: GaugeMetric[F, TMetric]): GaugeMetric[F, TMetric] = gaugeMetric
}
