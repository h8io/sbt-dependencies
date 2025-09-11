import Dependencies.*

ThisBuild / organization := "io.h8.sbt"
ThisBuild / organizationName := "H8IO"
ThisBuild / organizationHomepage := Some(url("https://github.com/h8io/"))

ThisBuild / description := "SBT dependencies helper"
ThisBuild / licenses := List("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/h8io/sbt-dependencies"))
ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / scalaVersion := "2.12.20"
// ThisBuild / crossScalaVersions += "3.7.2"
ThisBuild / scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, 12)) => Seq("-Xsource:3")
  case _             => Nil
})
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

val root = project
  .enablePlugins(SbtPlugin, ScoverageSummaryPlugin)
  .settings(
    name := "sbt-dependencies",
    sbtPlugin := true,
    sbtPluginPublishLegacyMavenStyle := false,
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.12" => "1.11.5"
        case _      => "2.0.0-RC4"
      }
    },
    libraryDependencies ++= Seq("org.scala-sbt" % "sbt" % (pluginCrossBuild / sbtVersion).value, scalaTest % Test)
  )
