## tagless-metrics-core

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.novelfs/tagless-metrics-core_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.novelfs/tagless-metrics-core_2.13)

Defines a core tagless dsl for incrementing/decrementing/etc metrics.

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
    _ <- IncrementMetric[F, Counter].increment(helloWorldCounter.withTag("status" -> "before"))
    _ <- Sync[F].delay { println("Hello world" }
    _ <- IncrementMetric[F, Counter].increment(helloWorldCounter.withTag("status" -> "after"))
  } yield ()

```
