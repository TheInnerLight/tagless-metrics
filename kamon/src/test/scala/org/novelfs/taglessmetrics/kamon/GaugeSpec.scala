package org.novelfs.taglessmetrics.kamon

import _root_.kamon.Kamon
import _root_.kamon.testkit.MetricInspection
import cats.effect.IO
import cats.implicits._
import org.novelfs.taglessmetrics.{DecrementMetric, IncrementMetric}
import org.novelfs.taglessmetrics.kamon.instances.all._
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import scala.concurrent.duration._

class GaugeSpec extends FlatSpec with Matchers with MetricInspection with GeneratorDrivenPropertyChecks  {
  val positiveInt = Gen.posNum[Int]

  implicit val ioTimer = IO.timer(scala.concurrent.ExecutionContext.global)

  "increment" should "increment the metric the number of times it is called" in {
    forAll(positiveInt) { expectedGaugeValue: Int =>
      val kamonGauge = Kamon.gauge("test-gauge")
      kamonGauge.set(0)
      val testGauge = Gauge("test-gauge")
      val incrMetric = IncrementMetric[IO, Gauge].increment(testGauge)
      List.fill(expectedGaugeValue)(incrMetric).sequence_.unsafeRunSync()
      val actualGaugeValue = kamonGauge.value()
      actualGaugeValue shouldBe expectedGaugeValue
    }
  }

  "incrementTimes" should "increment the metric by n" in {
    forAll(positiveInt) { expectedGaugeValue: Int =>
      val kamonGauge = Kamon.gauge("test-gauge-2")
      kamonGauge.set(0)
      val testGauge = Gauge("test-gauge-2")
      IncrementMetric[IO, Gauge].incrementTimes(expectedGaugeValue.toLong)(testGauge).unsafeRunSync()
      val actualGaugeValue = kamonGauge.value()
      actualGaugeValue shouldBe expectedGaugeValue
    }
  }

  "decrement" should "decrement the metric the number of times it is called" in {
    forAll(positiveInt) { n: Int =>
      val kamonGauge = Kamon.gauge("test-gauge-3")
      kamonGauge.set(Long.MaxValue)
      val testGauge = Gauge("test-gauge-3")
      val decrMetric = DecrementMetric[IO, Gauge].decrement(testGauge)
      List.fill(n)(decrMetric).sequence_.unsafeRunSync()
      val expectedGaugeValue = Long.MaxValue - n
      val actualGaugeValue = kamonGauge.value()
      actualGaugeValue shouldBe expectedGaugeValue
    }
  }

  "decrementTimes" should "decrement the metric by n" in {
    forAll(positiveInt) { n: Int =>
      val kamonGauge = Kamon.gauge("test-gauge-4")
      kamonGauge.set(Long.MaxValue)
      val testGauge = Gauge("test-gauge-4")
      DecrementMetric[IO, Gauge].decrementTimes(n.toLong)(testGauge).unsafeRunSync()
      val expectedGaugeValue = Long.MaxValue - n
      val actualGaugeValue = kamonGauge.value()
      actualGaugeValue shouldBe expectedGaugeValue
    }
  }
}
