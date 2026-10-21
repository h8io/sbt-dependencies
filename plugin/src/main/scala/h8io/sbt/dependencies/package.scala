package h8io.sbt

import sbt.*
import sbt.librarymanagement.DependencyBuilders.OrganizationArtifactName
import sbt.librarymanagement.{Artifact, CrossVersion, ModuleID}

import scala.annotation.nowarn

package object dependencies {
  implicit class OrganizationArtifactsMixin(artifacts: Seq[OrganizationArtifactName]) {
    def %(version: String): Seq[ModuleID] = artifacts map (_ % version)
  }

  implicit class OrganizationMixin(organization: String) {
    def %(artifacts: Seq[String]): Seq[OrganizationArtifactName] =
      artifacts map (stringToOrganization(organization) % _)

    def %%(artifacts: Seq[String]): Seq[OrganizationArtifactName] =
      artifacts map (stringToOrganization(organization) %% _)
  }

  implicit class ModuleIDsMixin(modules: Seq[ModuleID]) {
    def %(configuration: Configuration): Seq[ModuleID] = apply(_ % configuration)

    def cross(v: CrossVersion): Seq[ModuleID] = apply(_.cross(v))

    def notTransitive: Seq[ModuleID] = apply(_.notTransitive())

    def intransitive: Seq[ModuleID] = apply(_.intransitive())

    def changing: Seq[ModuleID] = apply(_.changing())

    def force(): Seq[ModuleID] = apply(_.force())

    def artifacts(newArtifacts: Artifact*): Seq[ModuleID] = apply(_.artifacts(newArtifacts*))

    def excludeAll(rules: ExclusionRule*): Seq[ModuleID] = apply(_.excludeAll(rules*))

    // ModuleID.exclude is deprecated since sbt 2.1.0, which wants the cross-versioned name spelled out. This mirrors
    // it on both rows, and sbt 1 has no replacement to switch to, so the deprecation stays sbt's to announce
    @nowarn("cat=deprecation")
    def exclude(org: String, name: String): Seq[ModuleID] = apply(_.exclude(org, name))

    def extra(attributes: (String, String)*): Seq[ModuleID] = apply(_.extra(attributes*))

    def pomOnly: Seq[ModuleID] = apply(_.pomOnly())

    def jar: Seq[ModuleID] = apply(_.jar())

    @inline private def apply(f: ModuleID => ModuleID): Seq[ModuleID] = modules map f
  }
}
