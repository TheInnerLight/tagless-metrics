package org.novelfs.taglessmetrics.kamon.instances

import cats.implicits._
import cats.effect.Sync
import org.novelfs.taglessmetrics.HistogramMetric
import org.novelfs.taglessmetrics.kamon.Histogram
import _root_.kamon.Kamon

trait HistogramMetricInstances {
  implicit def histogramHistogram[F[_] : Sync] = new HistogramMetric[F, Histogram] {
    override def record(value: Long)(metric: Histogram): F[Unit] =
      Sync[F].delay {
        Kamon.histogram(metric.name).withTags(metric.tags).record(value)
      }.void

    override def recordTimes(times: Long)(value: Long)(metric: Histogram): F[Unit] =
      Sync[F].delay {
        Kamon.histogram(metric.name).withTags(metric.tags).record(value, times)
      }.void
  }
}
