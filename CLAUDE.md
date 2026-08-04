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

## Coverage

100% of statements and of branches, with the gate at `low = high = 100` on both metrics. Every modifier on
`Seq[ModuleID]` is one line delegating to its `ModuleID` counterpart, so there is no part of this that is
awkward to reach and no reason for the number to be anything else.

The tests for the modifiers state the same thing eleven times: applying it to the group equals mapping it over
the members. The expected side writes the function out rather than reusing the one under test, which is what
makes a modifier delegating to the wrong one of its neighbours — `jar` to `pomOnly()`, say — fail rather than
pass.

**The check is per module, not on the aggregate.** That mattered while this build was under the bar: it totalled
35.29% while the `plugin` module the gate judges stood at 34%, and a floor set from the total failed. An unset
minimum resolves to the low threshold of its own metric in its own module, so the number to compare against is
always the module's own.

Note that `-Wsafe-init` on the Scala 3 row reacts to instance fields in a ScalaTest class: adding a
`private val` to `PackageTest` made the checker report the *first* test in the file, one untouched by the
change. The helpers are `private def` for that reason.

## Releasing

The README pins a version in its install snippet and nothing checks it. Bump it as part of the release rather
than after it.

## Style

- Warnings are fatal on both rows, including unused imports — an `import sbt.*` left behind after an edit fails
  the build rather than warning.
- `scalafmt` covers `.sbt` files too (`scalafmtSbtCheck`), and CI checks both.
