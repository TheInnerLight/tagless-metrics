package org.novelfs.taglessmetrics

trait TimerMetric[F[_], TMetric] {
  def measure[A](op : F[A])(metric: TMetric) : F[A]
}

object TimerMetric {
  def apply[F[_], TMetric](implicit timerMetric: TimerMetric[F, TMetric]): TimerMetric[F, TMetric] = timerMetric
}
