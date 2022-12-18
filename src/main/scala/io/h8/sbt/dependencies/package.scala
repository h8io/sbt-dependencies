package io.h8.sbt

import sbt._
import sbt.librarymanagement.DependencyBuilders.OrganizationArtifactName
import sbt.librarymanagement.{Artifact, CrossVersion, Disabled, ModuleID}

package object dependencies {
  implicit class OrganizationArtifactsMixin(artifacts: Seq[OrganizationArtifactName]) {
    def %(version: String): Seq[ModuleID] = artifacts map (_ % version)
  }

  implicit class OrganizationMixin(organization: String) {
    def %(artifacts: Seq[String]): Seq[OrganizationArtifactName] = artifacts map (organization % _)

    def %%(artifacts: Seq[String]): Seq[OrganizationArtifactName] = artifacts map (organization %% _)
  }

  implicit class ModuleIDsMixin(modules: Seq[ModuleID]) {
    def %(configuration: Configuration): Seq[ModuleID] = apply(_ % configuration)

    def cross(v: CrossVersion): Seq[ModuleID] = apply(_ cross v)

    def notTransitive: Seq[ModuleID] = apply(_.notTransitive())

    def intransitive: Seq[ModuleID] = apply(_.intransitive())

    def changing: Seq[ModuleID] = apply(_.changing())

    def force: Seq[ModuleID] = apply(_.force)

    def artifacts(newArtifacts: Artifact*): Seq[ModuleID] = apply(_.artifacts(newArtifacts: _*))

    def excludeAll(rules: ExclusionRule*): Seq[ModuleID] = apply(_.excludeAll(rules: _*))

    def exclude(org: String, name: String): Seq[ModuleID] = apply(_.exclude(org, name))

    def extra(attributes: (String, String)*): Seq[ModuleID] = apply(_.extra(attributes: _*))

    def pomOnly: Seq[ModuleID] = apply(_.pomOnly())

    def jar: Seq[ModuleID] = apply(_.jar())

    @inline private def apply(f: ModuleID => ModuleID): Seq[ModuleID] = modules map f
  }
}
