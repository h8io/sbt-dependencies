import Dependencies.*

ThisBuild / organization := "io.h8.sbt"
ThisBuild / organizationName := "H8IO"
ThisBuild / organizationHomepage := Some(url("https://github.com/h8io/"))

ThisBuild / description := "SBT dependencies helper"
ThisBuild / licenses := List("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/h8io/sbt-dependencies"))
ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / scalaVersion := "2.12.20"
ThisBuild / scalacOptions ++= Seq("-Xsource:3")
ThisBuild / javacOptions ++= Seq("-target", "8")

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/h8io/sbt-dependencies"),
    "scm:git@github.com:h8io/sbt-dependencies.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "eshu",
    name = "Pavel",
    email = "tjano.xibalba@gmail.com",
    url = url("https://github.com/h8io/")
  )
)

pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
  else localStaging.value
}
credentials += Credentials(Path.userHome / ".sbt" / "sonatype_central_credentials")

import ReleaseTransformations.*
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
    libraryDependencies ++= Seq("org.scala-sbt" % "sbt" % "1.11.5" % Provided, scalaTest % Test)
  ).enablePlugins(SbtPlugin)
