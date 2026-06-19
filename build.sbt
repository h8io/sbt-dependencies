dynverSonatypeSnapshots := true
dynverSeparator := "-"

val plugin = project
  .in(file("plugin"))
  .enablePlugins(SbtPlugin, ScoverageSummaryPlugin)
  .settings(
    name := "sbt-dependencies",
    organization := "io.h8.sbt",
    organizationName := "H8IO",
    organizationHomepage := Some(url("https://github.com/h8io/")),
    description := "SBT dependencies helper",
    licenses := List("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    homepage := Some(url("https://github.com/h8io/sbt-dependencies")),
    versionScheme := Some("semver-spec"),
    javacOptions ++= Seq("--release", "11"),
    developers := List(
      Developer(
        id = "eshu",
        name = "Pavel",
        email = "tjano.xibalba@gmail.com",
        url = url("https://github.com/h8io/")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/h8io/sbt-dependencies"),
        "scm:git@github.com:h8io/sbt-dependencies.git"
      )
    ),
    sbtPlugin := true,
    sbtPluginPublishLegacyMavenStyle := false,
    scalacOptions ++=
      (scalaBinaryVersion.value match {
        case "2.12" =>
          Seq(
            "-Xsource:3",
            "-language:higherKinds",
            "--deprecation",
            "--feature",
            "--unchecked",
            "-Xlint:_",
            "-Xfatal-warnings",
            "-opt:l:inline",
            "-opt-warnings",
            "-Ywarn-unused",
            "-Ywarn-dead-code",
            "-Ywarn-unused:-nowarn",
            "-Ywarn-value-discard",
            "-Ywarn-numeric-widen",
            "-Ywarn-extra-implicit",
            "-Ypartial-unification"
          )
        case _ =>
          Seq(
            "-deprecation",
            "-feature",
            "-unchecked",
            "-Werror",
            "-Wshadow:all",
            "-Wunused:all",
            "-Wvalue-discard",
            "-Wsafe-init"
          )
      }),
    scalaVersion := "2.12.21",
    crossScalaVersions := Seq(scalaVersion.value, "3.8.4"),
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % Test
  )
