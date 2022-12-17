package io.h8.sbt

import sbt._
import sbt.librarymanagement.DependencyBuilders.OrganizationArtifactName

package object dependencies {
  implicit class OrganizationArtifactsMixin(artifacts: Seq[OrganizationArtifactName]) {
    def %(version: String): Seq[ModuleID] = artifacts map (_ % version)
  }

  implicit class OrganizationMixin(organization: String) {
    def %(artifacts: Seq[String]): Seq[OrganizationArtifactName] = artifacts map (organization % _)

    def %%(artifacts: Seq[String]): Seq[OrganizationArtifactName] = artifacts map (organization %% _)
  }

  implicit class ModuleIDsMixin(modules: Seq[ModuleID]) {
    def %(configuration: Configuration): Seq[ModuleID] = modules map (_ % configuration)
  }
}
