## tagless-metrics-core

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.novelfs/tagless-metrics-core_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.novelfs/tagless-metrics-core_2.13)

Defines a core tagless dsl for incrementing/decrementing/etc metrics.

As of version 0.2.0, tagless-metrics supports Kamon 2.x for Scala 2.12 and
2.13.

Kamon 1.x is supported by version 0.1.0 of tagless-metrics and is
availale for Scala 2.11 and 2.12 ... see below for further details.

## tagless-metrics-kamon

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.novelfs/tagless-metrics-kamon_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.novelfs/tagless-metrics-kamon_2.13)

Defines kamon-integration via the dsl.


## Include

Add dependencies to `built.sbt`

```
libraryDependencies += "org.novelfs" %% "tagless-metrics-core" % "0.2.0"
libraryDependencies += "org.novelfs" %% "tagless-metrics-kamon" % "0.2.0"
```

## Usage

You can import the instances individually if you wish, or simply:

```
import org.novelfs.taglessmetrics.kamon.implicits._
```

Import the metrics to be used

```
import org.novelfs.taglessmetrics.kamon.Counter
```

Counter usage:

```
val helloWorldCounter = Counter("hello-world-counter")

def printAndIncrementMetric[F[_] : Sync : IncrementMeric[?[_], Counter]] : F[Unit] =
  for {
    _ <- IncrementMetric[F, Counter].increment(helloWorldCounter.withTag("status", "before"))
    _ <- Sync[F].delay { println("Hello world" }
    _ <- IncrementMetric[F, Counter].increment(helloWorldCounter.withTag("status", "after"))
  } yield ()

```

## Older versions

Kamon 1.x is supported by version 0.1.0 of tagless-metrics and is
availale for Scala 2.11 and 2.12. Inclusion and usage are as above, but note
that syntax follows the earlier Kamon release with `refine` used in place of
`withTag`. Consequently the preceding example would be written,

```
val helloWorldCounter = Counter("hello-world-counter")

def printAndIncrementMetric[F[_] : Sync : IncrementMeric[?[_], Counter]] : F[Unit] =
  for {
    _ <- IncrementMetric[F, Counter].increment(helloWorldCounter.refine("status" -> "before"))
    _ <- Sync[F].delay { println("Hello world" }
    _ <- IncrementMetric[F, Counter].increment(helloWorldCounter.refine("status" -> "after"))
  } yield ()

```
