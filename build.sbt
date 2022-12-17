import Dependencies._

ThisBuild / organization := "io.h8"
ThisBuild / organizationName := "H8IO"
ThisBuild / version := "0.0.1"
ThisBuild / organizationHomepage := Some(url("https://github.com/h8io/"))

ThisBuild / description := "SBT dependencies helper"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/h8io/sbt-dependencies"))
ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / scalaVersion := "2.12.17"
ThisBuild / javacOptions := Seq("-target", "8")

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/h8io/borscht"),
    "scm:git@github.com:h8io/borscht.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "eshu",
    name = "Pavel Parkhomenko",
    email = "tjano.xibalba@gmail.com",
    url = url("https://github.com/h8io/")
  )
)

pomIncludeRepository := { _ => false }
sonatypeProfileName := "io.h8"
sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
ThisBuild / publishTo := sonatypePublishToBundle.value

import ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  // For non cross-build projects, use releaseStepCommand("publishSigned")
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

lazy val root = (project in file("."))
  .settings(
    name := "sbt-dependencies",
    libraryDependencies ++= Seq("org.scala-sbt" % "sbt" % "1.8.0" % Provided, scalaTest % Test)
  )
