package org.novelfs.taglessmetrics.kamon.instances

import cats.implicits._
import _root_.kamon.Kamon
import cats.effect.Sync
import org.novelfs.taglessmetrics.GaugeMetric
import org.novelfs.taglessmetrics.kamon.Gauge

trait GaugeMetricInstances {
  implicit def gaugeInstance[F[_] : Sync]: GaugeMetric[F, Gauge] = new GaugeMetric[F, Gauge] {
    override def raise(n: Double)(metric: Gauge): F[Unit] =
      Sync[F].delay {
        Kamon.gauge(metric.name).withTags(metric.tags).increment(n)
      }.void

    override def lower(n: Double)(metric: Gauge): F[Unit] =
      Sync[F].delay {
        Kamon.gauge(metric.name).withTags(metric.tags).decrement(n)
      }.void

    override def update(n: Double)(metric: Gauge): F[Unit] =
      Sync[F].delay {
        Kamon.gauge(metric.name).withTags(metric.tags).update(n)
      }.void
  }
}
