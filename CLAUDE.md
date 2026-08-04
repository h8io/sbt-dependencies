# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

`sbt-dependencies` lets a family of artifacts that shares a version state it once. It is one file — three
implicit classes lifting sbt's dependency syntax from a single `ModuleID` to a `Seq[ModuleID]`:

- `String` gains `%` and `%%` taking a `Seq[String]` of artifact names
- `Seq[OrganizationArtifactName]` gains `%` taking a version
- `Seq[ModuleID]` gains everything a single `ModuleID` has — `% configuration`, `cross`, `notTransitive`,
  `intransitive`, `changing`, `force()`, `artifacts`, `excludeAll`, `exclude`, `extra`, `pomOnly`, `jar`

Every one of them is `map` over a sequence. Nothing is deferred, cached or reinterpreted, and the result is
ordinary dependencies. Keeping it that way is the point: a build that uses this should be indistinguishable
from one that spells its dependencies out.

**It is not an `AutoPlugin`.** There are no settings and nothing to enable — `addSbtPlugin` only puts the
package on the build's classpath, and `import h8io.sbt.dependencies.*` brings the syntax into scope. A reader
looking for `projectSettings` will not find any.

`stages`, `cfg`, `xi`, `reflect` and the `h8io.g8` template all declare their dependencies through it, so a
change here reaches every H8IO build.

## Commands

```bash
sbt +test                    # both rows of the matrix
sbt "plugin2_12/test"        # one row: sbt 1 on Scala 2.12
sbt "plugin/test"            # the other: sbt 2 on Scala 3
sbt scalafmtAll scalafmtSbt  # format
./test.sh                    # everything CI runs
```

## The matrix

`projectMatrix` builds two rows: Scala 2.12 published for sbt 1, Scala 3 published for sbt 2. `baseDirectory`
is virtual (`.sbt/matrix/plugin2_12`), so anything deriving paths from it needs checking against both rows.

Unlike `h8io/sbt-testkit`, there is **no version-specific source directory here** — the whole plugin compiles
unchanged on both rows, because it touches only `ModuleID`, `Configuration` and `ExclusionRule`, whose shape did
not change between sbt 1 and sbt 2. That is a property worth keeping: reaching for an API that differs between
the two would mean introducing a compat split for a file this small.

## Coverage, and what is missing

The gate is `coverageSummaryStmtLowThreshold := 30`, high at 90, branch at 100/100. The build measures **34%
of statements**, and the floor sits just under that: it says *do not get worse*, not that 34% is acceptable.

What is untested is specific and easy. `PackageTest` covers the three ways to build a sequence — one
organization with many artifacts, many organizations with one version, and a configuration applied to the
group. It covers **none** of the other eleven modifiers on `Seq[ModuleID]`, which is the whole of the gap. They
are all the same one line, `apply(_.someModuleIdMethod)`, so each is a short test of the same shape as the
existing ones, and writing them would take the number close to 100 in one sitting.

Branch coverage reads 100%, which says less than it looks: there is almost nothing branching in what the tests
reach.

**The check is per module, not on the aggregate.** The build totals 35.29% while the `plugin` module that the
gate actually judges is at 34%. An unset minimum resolves to the low threshold of its own metric in its own
module, so the number to compare against is always the module's own.

## Releasing

The README pins a version in its install snippet and nothing checks it. Bump it as part of the release rather
than after it.

## Style

- Warnings are fatal on both rows, including unused imports — an `import sbt.*` left behind after an edit fails
  the build rather than warning.
- `scalafmt` covers `.sbt` files too (`scalafmtSbtCheck`), and CI checks both.
