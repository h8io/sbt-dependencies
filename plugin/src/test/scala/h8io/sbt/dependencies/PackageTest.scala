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

  /** Every modifier on the sequence is the same modifier applied to each module, so that is what these state. The
    * function is written out on the right rather than reused from the left, which is what makes a modifier delegating
    * to the wrong one of its neighbours fail here.
    */
  private def group = "org.apache.spark" %% Seq("spark-core", "spark-sql") % "3.3.1"

  private def modules =
    List("org.apache.spark" %% "spark-core" % "3.3.1", "org.apache.spark" %% "spark-sql" % "3.3.1")

  private def behavesLike(actual: Seq[ModuleID])(expected: ModuleID => ModuleID) =
    actual should contain theSameElementsInOrderAs modules.map(expected)

  it should "cross-version every module" in
    behavesLike(group.cross(CrossVersion.full))(_.cross(CrossVersion.full))

  it should "make every module non-transitive" in
    behavesLike(group.notTransitive)(_.notTransitive())

  it should "make every module intransitive" in
    behavesLike(group.intransitive)(_.intransitive())

  it should "mark every module as changing" in
    behavesLike(group.changing)(_.changing())

  it should "force the version of every module" in
    behavesLike(group.force())(_.force())

  it should "give every module the same artifacts" in {
    val artifact = Artifact("spark", "sources")
    behavesLike(group.artifacts(artifact))(_.artifacts(artifact))
  }

  it should "apply the same exclusion rules to every module" in {
    val rule = ExclusionRule("org.slf4j")
    behavesLike(group.excludeAll(rule))(_.excludeAll(rule))
  }

  it should "exclude the same dependency from every module" in
    behavesLike(group.exclude("org.slf4j", "slf4j-api"))(_.exclude("org.slf4j", "slf4j-api"))

  it should "attach the same extra attributes to every module" in
    behavesLike(group.extra("key" -> "value"))(_.extra("key" -> "value"))

  it should "reduce every module to its pom" in
    behavesLike(group.pomOnly)(_.pomOnly())

  it should "reduce every module to its jar" in
    behavesLike(group.jar)(_.jar())
}
