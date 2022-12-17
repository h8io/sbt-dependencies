# sbt-dependencies
SBT dependencies helpers

## How to use
1. Add the plugin to the file `project/plugins.sbt`:
   ```scala
   addSbtPlugin("io.h8.sbt" %% "sbt-dependencies" % "1.0.0")
   ```
2. Import the package `io.h8.sbt.dependencies._` (in the file `build.sbt`
   you should use `_root_.io.h8.sbt.dependencies._` because there is automatically imported `sbt._`
   which contains `io` package)
3. Add group dependencies:
   ```scala
   libraryDependencies ++= (
    Seq(
      "com.fasterxml.jackson.core" % "jackson-databind",
      "com.fasterxml.jackson.core" % "jackson-core",
      "com.fasterxml.jackson.module" %% "jackson-module-scala"
    ) % "2.14.1") ++ (
    "org.apache.spark" %% Seq(
      "spark-core",
      "spark-sql",
      "spark-streaming"
    ) % "3.3.1") ++ (
      "org.junit.jupiter" % Seq(
        "junit-jupiter-api",
        "junit-jupiter-engine",
        "junit-jupiter-params"
    ) % "5.9.1" % Test)
   ```