package org.novelfs.taglessmetrics.kamon.instances

import _root_.kamon.Kamon
import cats.implicits._
import cats.effect.Sync
import org.novelfs.taglessmetrics.IncrementMetric
import org.novelfs.taglessmetrics.kamon.{Counter, Gauge, RangeSampler}

trait IncrementMetricInstances {
  implicit def incrementCounter[F[_] : Sync]: IncrementMetric[F, Counter] = new IncrementMetric[F, Counter] {
    override def increment(metric: Counter): F[Unit] =
      Sync[F].delay {
        Kamon.counter(metric.name).withTags(metric.tags).increment()
      }.void

    override def incrementTimes(n: Long)(metric: Counter): F[Unit] =
      Sync[F].delay {
        Kamon.counter(metric.name).withTags(metric.tags).increment(n)
      }.void
  }

  implicit def incrementGauge[F[_] : Sync]: IncrementMetric[F, Gauge] = new IncrementMetric[F, Gauge] {
    override def increment(metric: Gauge): F[Unit] =
      Sync[F].delay {
        Kamon.gauge(metric.name).withTags(metric.tags).increment()
      }.void

    override def incrementTimes(n: Long)(metric: Gauge): F[Unit] =
      Sync[F].delay {
        Kamon.gauge(metric.name).withTags(metric.tags).increment(n.toDouble)
      }.void
  }

  implicit def incrementRangeSampler[F[_] : Sync]: IncrementMetric[F, RangeSampler] = new IncrementMetric[F, RangeSampler] {
    override def increment(metric: RangeSampler): F[Unit] =
      Sync[F].delay {
        Kamon.rangeSampler(metric.name).withTags(metric.tags).increment()
      }.void

    override def incrementTimes(n: Long)(metric: RangeSampler): F[Unit] =
      Sync[F].delay {
        Kamon.rangeSampler(metric.name).withTags(metric.tags).increment(n)
      }.void
  }
}
