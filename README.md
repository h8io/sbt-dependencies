# sbt-dependencies
SBT dependencies helpers.

⚠️ SBT 1.8.0 or newer required.

## How to use
1. Add the plugin to the file `project/plugins.sbt`:
   ```scala
   addSbtPlugin("io.h8.sbt" %% "sbt-dependencies" % "1.0.0")
   ```
2. Import the package `h8io.sbt.dependencies.*`
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
   Other modules modifiers (like `exclude`, `excludeAll` or `force`) are applicable too.