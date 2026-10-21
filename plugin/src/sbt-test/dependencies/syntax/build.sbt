import h8io.sbt.dependencies.*

name := "syntax"
scalaVersion := "2.13.18"

libraryDependencies ++= "org.apache.spark" %% Seq("spark-core", "spark-sql") % "3.3.1" exclude
  ("org.typelevel" %% "cats-core")
libraryDependencies ++= Seq("org.scalatest" % "scalatest-core", "org.scalatest" %% "scalatest-flatspec") % "3.2.20" % Test

TaskKey[Unit]("check") := {
  val expected = Seq(
    "org.apache.spark" %% "spark-core" % "3.3.1" excludeAll ("org.typelevel" %% "cats-core"),
    "org.apache.spark" %% "spark-sql" % "3.3.1" excludeAll ("org.typelevel" %% "cats-core"),
    "org.scalatest" % "scalatest-core" % "3.2.20" % Test,
    "org.scalatest" %% "scalatest-flatspec" % "3.2.20" % Test
  )
  val actual = libraryDependencies.value
  assert(expected.forall(actual.contains), s"expected $expected among $actual")
}
