package org.novelfs.taglessmetrics.kamon

import _root_.kamon.Kamon
import _root_.kamon.testkit.InstrumentInspection
import cats.effect.IO
import cats.implicits._
import org.novelfs.taglessmetrics.{DecrementMetric, IncrementMetric, GaugeMetric}
import org.novelfs.taglessmetrics.kamon.instances.all._
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import cats.effect.unsafe.implicits.global

class GaugeSpec extends AnyFlatSpec with Matchers with InstrumentInspection.Syntax with ScalaCheckDrivenPropertyChecks  {
  val positiveInt = Gen.posNum[Int]
  val positiveDouble = Gen.posNum[Double]

  "increment" should "increment the metric the number of times it is called" in {
    forAll(positiveInt) { expectedGaugeValue: Int =>
      val kamonGauge = Kamon.gauge("test-gauge").withoutTags()
      kamonGauge.update(0)
      val testGauge = Gauge("test-gauge")
      val incrMetric = IncrementMetric[IO, Gauge].increment(testGauge)
      List.fill(expectedGaugeValue)(incrMetric).sequence_.unsafeRunSync()
      val actualGaugeValue = kamonGauge.value()
      actualGaugeValue shouldBe expectedGaugeValue
    }
  }

  "incrementTimes" should "increment the metric by n" in {
    forAll(positiveInt) { expectedGaugeValue: Int =>
      val kamonGauge = Kamon.gauge("test-gauge-2").withoutTags()
      kamonGauge.update(0)
      val testGauge = Gauge("test-gauge-2")
      IncrementMetric[IO, Gauge].incrementTimes(expectedGaugeValue.toLong)(testGauge).unsafeRunSync()
      val actualGaugeValue = kamonGauge.value()
      actualGaugeValue shouldBe expectedGaugeValue
    }
  }

  "decrement" should "decrement the metric the number of times it is called" in {
    forAll(positiveInt) { n: Int =>
      val kamonGauge = Kamon.gauge("test-gauge-3").withoutTags()
      kamonGauge.update(Long.MaxValue)
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
      val kamonGauge = Kamon.gauge("test-gauge-4").withoutTags()
      kamonGauge.update(Long.MaxValue)
      val testGauge = Gauge("test-gauge-4")
      DecrementMetric[IO, Gauge].decrementTimes(n.toLong)(testGauge).unsafeRunSync()
      val expectedGaugeValue = Long.MaxValue - n
      val actualGaugeValue = kamonGauge.value()
      actualGaugeValue shouldBe expectedGaugeValue
    }
  }

  "raise" should "raise the metric by n" in {
    forAll(positiveDouble) { expectedGaugeValue: Double =>
      val kamonGauge = Kamon.gauge("test-gauge-2").withoutTags()
      kamonGauge.update(0)
      val testGauge = Gauge("test-gauge-2")
      GaugeMetric[IO, Gauge].raise(expectedGaugeValue)(testGauge).unsafeRunSync()
      val actualGaugeValue = kamonGauge.value()
      actualGaugeValue shouldBe expectedGaugeValue
    }
  }

  "lower" should "lower the metric by n" in {
    forAll(positiveDouble) { n: Double =>
      val kamonGauge = Kamon.gauge("test-gauge-4").withoutTags()
      kamonGauge.update(Double.MaxValue)
      val testGauge = Gauge("test-gauge-4")
      GaugeMetric[IO, Gauge].lower(n)(testGauge).unsafeRunSync()
      val expectedGaugeValue = Double.MaxValue - n
      val actualGaugeValue = kamonGauge.value()
      actualGaugeValue shouldBe expectedGaugeValue
    }
  }

  "set" should "set the metric to n" in {
    forAll(positiveDouble) { n: Double =>
      val kamonGauge = Kamon.gauge("test-gauge-4").withoutTags()
      kamonGauge.update(Double.MaxValue)
      val testGauge = Gauge("test-gauge-4")
      GaugeMetric[IO, Gauge].set(n)(testGauge).unsafeRunSync()
      val expectedGaugeValue = n
      val actualGaugeValue = kamonGauge.value()
      actualGaugeValue shouldBe expectedGaugeValue
    }
  }

}
