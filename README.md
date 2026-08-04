[![GitHub release](https://img.shields.io/github/v/release/h8io/sbt-dependencies)](https://github.com/h8io/sbt-dependencies/releases/latest)

# sbt-dependencies

State a version once for a family of artifacts that has to move together.

## The problem

Libraries rarely arrive alone. Spark is three or four artifacts, Jackson is several, JUnit is a handful — and
they share a version not by coincidence but by requirement. Written out one line at a time, the version is
repeated as many times as there are artifacts:

```scala
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.3.1",
  "org.apache.spark" %% "spark-sql" % "3.3.1",
  "org.apache.spark" %% "spark-streaming" % "3.3.1"
)
```

Every bump is then three edits instead of one, and nothing notices when it is two. A family that drifts apart
fails at runtime, on a `NoSuchMethodError` far from the line that caused it.

## Usage

```scala
// project/plugins.sbt
addSbtPlugin("io.h8.sbt" %% "sbt-dependencies" % "1.1.1")
```

```scala
// build.sbt
import h8io.sbt.dependencies.*
```

### One organization, several artifacts

```scala
libraryDependencies ++= "org.apache.spark" %% Seq("spark-core", "spark-sql", "spark-streaming") % "3.3.1"
```

`%` and `%%` both work, and mean what they always mean — whether the Scala version is part of the artifact name.

### Several organizations, one version

Some families are split across organizations while still sharing a version:

```scala
libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind",
  "com.fasterxml.jackson.core" % "jackson-core",
  "com.fasterxml.jackson.module" %% "jackson-module-scala"
) % "2.14.1"
```

Each entry chooses its own `%` or `%%`; the version applies to all of them.

### Modifiers apply to the group

Anything you would write on a single `ModuleID` can be written once for the sequence:

```scala
libraryDependencies ++= "org.junit.jupiter" % Seq(
  "junit-jupiter-api",
  "junit-jupiter-engine",
  "junit-jupiter-params"
) % "5.9.1" % Test

libraryDependencies ++= "org.apache.spark" %% Seq("spark-core", "spark-sql") % "3.3.1" %
  Provided excludeAll ExclusionRule("org.slf4j")
```

Available on the group: `% configuration`, `cross`, `notTransitive`, `intransitive`, `changing`, `force()`,
`artifacts`, `excludeAll`, `exclude`, `extra`, `pomOnly`, `jar`.

## What it is not

An extension of sbt's dependency model. Everything here is `map` over a `Seq[ModuleID]`, so what comes out is
ordinary dependencies that resolve, exclude and cross-build exactly as they would written one per line. Nothing
is deferred, cached or reinterpreted — the only thing that changes is how many times you type the version.

## sbt versions

Published for both sbt 1.x and sbt 2.x; `addSbtPlugin` resolves the right one.

## License

Apache-2.0. See `LICENSE`.
