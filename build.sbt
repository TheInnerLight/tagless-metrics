import ReleaseTransformations._

name := "tagless-metrics"

version := "0.3.0"

inThisBuild(Seq(
  organization := "org.novelfs",
  scalaVersion := "2.13.5",
  crossScalaVersions := Seq("2.12.13", scalaVersion.value)
))

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

lazy val commonSettings = Seq(
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full),
  publishTo := sonatypePublishToBundle.value,
  sonatypeSessionName := s"[sbt-sonatype] ${name.value}-${scalaBinaryVersion.value}-${version.value}",
  homepage := Some(url("https://github.com/TheInnerLight/tagless-metrics")),
  licenses := Seq("Apache-2.0" -> url("https://opensource.org/licenses/Apache-2.0")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/TheInnerLight/tagless-metrics"),
      "scm:git@github.com:TheInnerLight/tagless-metrics.git"
    )
  ),
  developers := List(
    Developer(
      id    = "TheInnerLight",
      name  = "Phil Curzon",
      email = "phil@novelfs.org",
      url   = url("https://github.com/TheInnerLight")
    )
  ),
  pomIncludeRepository := { _ => false },
  publishMavenStyle := true,
)

lazy val core =
  (project in file("core"))
    .settings(commonSettings: _*)
    .settings(
      name := "tagless-metrics-core",
    )

lazy val kamon =
  (project in file("kamon"))
    .dependsOn(core)
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        "io.kamon"          %% "kamon-bundle"                  % "2.0.4",
        "org.typelevel"     %% "cats-effect"                   % "3.0.1",
        "io.kamon"          %% "kamon-testkit"                 % "2.0.4"   % Test,
        "org.scalacheck"    %% "scalacheck"                    % "1.15.3"  % Test,
        "org.scalatestplus" %% "scalacheck-1-15"               % "3.2.5.0" % Test,
        "org.scalatest"     %% "scalatest"                     % "3.2.5"   % Test,
      ),
      name := "tagless-metrics-kamon",
    )

lazy val combined =
  (project in file("."))
    .settings(commonSettings: _*)
    .settings(noPublishSettings)
    .aggregate(
      core, 
      kamon
    )

credentials ++= (for {
  username <- sys.env.get("SONATYPE_USERNAME")
  password <- sys.env.get("SONATYPE_PASSWORD")
} yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq

parallelExecution in Test := false

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  tagRelease,
  publishArtifacts,
  releaseStepCommand("sonatypeBundleRelease")
)
