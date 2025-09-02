package h8io.sbt.dependencies

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sbt.*

class PackageTest extends AnyFlatSpec with Matchers {
  "Mixin" should "provide a correct modules sequence from organization artifacts" in {
    Seq(
      "com.fasterxml.jackson.core" % "jackson-databind",
      "com.fasterxml.jackson.core" % "jackson-core",
      "com.fasterxml.jackson.module" %% "jackson-module-scala"
    ) % "2.14.1" should contain theSameElementsInOrderAs List(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.1",
      "com.fasterxml.jackson.core" % "jackson-core" % "2.14.1",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.1"
    )
  }

  it should "provide a correct modules sequence from artifacts" in {
    "org.apache.spark" %% Seq(
      "spark-core",
      "spark-sql",
      "spark-streaming"
    ) % "3.3.1" should contain theSameElementsInOrderAs List(
      "org.apache.spark" %% "spark-core" % "3.3.1",
      "org.apache.spark" %% "spark-sql" % "3.3.1",
      "org.apache.spark" %% "spark-streaming" % "3.3.1"
    )
  }

  it should "provide a correctly configured modules sequence" in {
    "org.junit.jupiter" % Seq(
      "junit-jupiter-api",
      "junit-jupiter-engine",
      "junit-jupiter-params"
    ) % "5.9.1" % Test should contain theSameElementsInOrderAs List(
      "org.junit.jupiter" % "junit-jupiter-api" % "5.9.1" % Test,
      "org.junit.jupiter" % "junit-jupiter-engine" % "5.9.1" % Test,
      "org.junit.jupiter" % "junit-jupiter-params" % "5.9.1" % Test
    )
  }
}
