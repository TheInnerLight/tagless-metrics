package org.novelfs.taglessmetrics.kamon.instances

import cats.implicits._
import cats.effect.Sync
import org.novelfs.taglessmetrics.DecrementMetric
import org.novelfs.taglessmetrics.kamon.{Gauge, RangeSampler}
import _root_.kamon.Kamon

trait DecrementMetricInstances {
  implicit def decrementGauge[F[_] : Sync]: DecrementMetric[F, Gauge] = new DecrementMetric[F, Gauge] {
    override def decrement(metric: Gauge): F[Unit] =
      Sync[F].delay {
        Kamon.gauge(metric.name).withTags(metric.tags).decrement()
      }.void

    override def decrementTimes(n: Long)(metric: Gauge): F[Unit] =
      Sync[F].delay {
        Kamon.gauge(metric.name).withTags(metric.tags).decrement(n.toDouble)
      }.void
  }

  implicit def decrementRangeSampler[F[_] : Sync]: DecrementMetric[F, RangeSampler] = new DecrementMetric[F, RangeSampler] {
    override def decrement(metric: RangeSampler): F[Unit] =
      Sync[F].delay {
        Kamon.rangeSampler(metric.name).withTags(metric.tags).decrement()
      }.void

    override def decrementTimes(n: Long)(metric: RangeSampler): F[Unit] =
      Sync[F].delay {
        Kamon.rangeSampler(metric.name).withTags(metric.tags).decrement(n)
      }.void
  }
}
