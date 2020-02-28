package org.novelfs.taglessmetrics.kamon.instances

import cats.implicits._
import org.novelfs.taglessmetrics.TimerMetric
import org.novelfs.taglessmetrics.kamon.Timer
import _root_.kamon.Kamon
import cats.effect.Sync

trait TimerMetricInstances {
  implicit def timerTimer[F[_] : Sync] : TimerMetric[F, Timer]= new TimerMetric[F, Timer] {
    override def measure[A](op: F[A])(metric: Timer): F[A] =
      for {
        started <- Sync[F].delay{ Kamon.timer(metric.name).withTags(metric.tags).start() }
        result <- op
        _ <- Sync[F].delay { started.stop() }
      } yield result
  }
}
