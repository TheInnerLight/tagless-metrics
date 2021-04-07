package org.novelfs.taglessmetrics.kamon

import _root_.kamon.Kamon
import _root_.kamon.testkit.InstrumentInspection
import cats.effect.IO
import cats.implicits._
import org.novelfs.taglessmetrics.kamon.instances.all._
import org.novelfs.taglessmetrics.IncrementMetric
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import cats.effect.unsafe.implicits.global

import scala.util.Random

class RangeSamplerSpec extends AnyFlatSpec with Matchers with InstrumentInspection.Syntax with ScalaCheckDrivenPropertyChecks  {
  val positiveInt = Gen.posNum[Int]

    "increment" should "increment the metric the number of times it is called" in {
    forAll(positiveInt) { expectedRangeSamplerValue: Int =>
      val suffix = Random.alphanumeric.take(10).mkString
      val kamonRangeSampler = Kamon.rangeSampler("test-range-sampler-" + suffix).withoutTags()
      val testRangeSampler = RangeSampler("test-range-sampler-" + suffix)
      val incrMetric = IncrementMetric[IO, RangeSampler].increment(testRangeSampler)
      List.fill(expectedRangeSamplerValue)(incrMetric).sequence_.unsafeRunSync()
      kamonRangeSampler.sample()
      val actualRangeSamplerValue = kamonRangeSampler.distribution().max
      actualRangeSamplerValue shouldBe expectedRangeSamplerValue
    }
  }

  "incrementTimes" should "increment the metric by n" in {
    forAll(positiveInt) { expectedRangeSamplerValue: Int =>
      val suffix = Random.alphanumeric.take(10).mkString
      val kamonRangeSampler = Kamon.rangeSampler("test-range-sampler-" + suffix).withoutTags()
      val testRangeSampler = RangeSampler("test-range-sampler-" + suffix)
      IncrementMetric[IO, RangeSampler].incrementTimes(expectedRangeSamplerValue.toLong)(testRangeSampler).unsafeRunSync()
      kamonRangeSampler.sample()
      val actualRangeSamplerValue = kamonRangeSampler.distribution().max
      actualRangeSamplerValue shouldBe expectedRangeSamplerValue
    }
  }

}
