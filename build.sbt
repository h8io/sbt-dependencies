import Dependencies.*

ThisBuild / organization := "io.h8.sbt"
ThisBuild / organizationName := "H8IO"
ThisBuild / organizationHomepage := Some(url("https://github.com/h8io/"))

ThisBuild / description := "SBT dependencies helper"
ThisBuild / licenses := List("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/h8io/sbt-dependencies"))
ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / scalaVersion := "2.12.21"
ThisBuild / crossScalaVersions += "3.8.4"
ThisBuild / scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, 12)) =>
    Seq("-Xfatal-warnings", "-Xlint:_", "-Ywarn-unused", "-Ywarn-dead-code", "-Ywarn-unused:-nowarn", "-Xsource:3")
  case Some((3, _)) => Seq("-Werror", "-Wunused:all", "-Wvalue-discard")
  case _            => Nil
})
ThisBuild / javacOptions ++= Seq("--release", "11")

ThisBuild / developers := List(
  Developer(
    id = "eshu",
    name = "Pavel",
    email = "tjano.xibalba@gmail.com",
    url = url("https://github.com/h8io/")
  )
)

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/h8io/sbt-dependencies"),
    "scm:git@github.com:h8io/sbt-dependencies.git"
  )
)

ThisBuild / dynverSonatypeSnapshots := true

val plugin = project
  .enablePlugins(SbtPlugin, ScoverageSummaryPlugin)
  .settings(
    name := "sbt-dependencies",
    sbtPlugin := true,
    sbtPluginPublishLegacyMavenStyle := false,
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.12" => "1.8.0"
        case _      => "2.0.0"
      }
    },
    libraryDependencies += scalaTest % Test
  )
