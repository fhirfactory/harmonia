<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Junie Implementation Review Report — Calliope Naming Package Structure

Report-Produced-At: UNRESOLVED. The connected interface does not expose an
attributable timestamp for completed report emission. No command-completion time,
filesystem metadata, or unrelated event time has been substituted; the filename
prefix remains explicitly unresolved for Solution Architect treatment.

**Task** · [Calliope Naming package-structure review][task].

**Repository / branch** · `fhirfactory/harmonia` ·
`feature/BL-0003-Calliope-Topic-and-TopicMask`.

**Software target** · `0.1.0-SNAPSHOT`, review target `software-0.1.0-010`.

**Observed base** · HEAD `46a08484e5c824b870fecda59b943ba37a241e2b`. The exact
pre-edit worktree snapshot for this bounded refactor was not captured; this
Report does not assert that the pre-edit worktree was clean.

**Resulting Calliope candidate** · Tree
`d186b400cf2ee01c0fc91aa10421ff036105613d`, reconstructed by applying the
candidate patch to the observed base. Candidate patch:
`enterprise-architecture/design/evidence/software-0.1.0/010/after/naming-0003-package-refactor-candidate.patch`.
SHA-256:
`002cd5cb7ec6ad3837baf185af01fe85f49cc6d44e1e4c02e67135af738127f3`.
The patch contains 32 Calliope files and excludes this Task and Report.

## Outcome

The package refactor keeps the principal Naming models and all implementation
that relies on package-private access in `net.fhirfactory.harmonia.model.core.naming`.
It places only the public validation vocabulary in the cohesive
`net.fhirfactory.harmonia.model.core.naming.valuesets` subpackage. The supported
model method names, visibility and behavior remain covered by the structural and
behavioral test suites. No compatibility alias was added.

### Resulting package structure

```text
net.fhirfactory.harmonia.model.core.naming
├── Topic, TopicMask
├── TopicEntry, TopicMaskEntry
├── TopicBuilder, TopicMaskBuilder
├── NamingXmlCodec, NamingValidationException
├── package-private field/canonical/XML parsers and validators
├── package-private mask/version evaluators and descriptions
├── package-private limits and diagnostic types
└── package-info.java

net.fhirfactory.harmonia.model.core.naming.valuesets
├── NamingFailureCategory
├── NamingOperation
├── NamingFieldKind
└── package-info.java
```

### Design decisions

- `Topic` and `TopicMask` remain the principal Naming abstractions in the
  accepted `naming` domain package. They are immutable values/selectors, not
  independently managed first-class entities; no runtime identity, registry,
  or lifecycle was introduced.
- `TopicEntry` and `TopicMaskEntry` remain immutable entry/criterion data types
  in the same package as their accepted constructors and strict assembly paths.
  They gain no independent identity or persistence semantics.
- `TopicBuilder` and `TopicMaskBuilder` remain in `naming` because their
  package-private construction and assembly boundary must remain available to
  `Topic` and `TopicMask` without public bridge APIs.
- `NamingXmlCodec` remains co-located as explicitly required by Final.003 §8.
  Canonical/XML reconstruction, field validation, matching, version handling,
  limits and internal diagnostics also remain package-private in this package.
- The three public validation enums are now grouped in `valuesets`. This is a
  function-oriented subpackage with no dependency back on the domain model.
  `NamingValidationException.of(...)` is the package-private construction point
  for safe failures; `TopicFieldRules` now performs field checks rather than
  acting as an exception factory.
- Package-level Javadoc and `calliope/README.md` document the classification and
  the reason further package splitting was not applied.

The Object-and-Class-Granularity note is Draft — Architectural Review and is
guidance, not accepted architecture. Its three-tick assessment concerns domain
meaning and lifecycle, not whether a Java class exists. The Initial Model
Architecture note allows `datatypes`, `valuesets`, `factory` and other
subpackages where responsibilities warrant them; it does not prescribe that
every category must receive a subpackage. No architectural identity or
relationship decision was derived from existing Java class boundaries.

### Encapsulation conflict and proposed resolution

Java package-private access does not cross a subpackage boundary. Moving
`TopicEntry`/`TopicMaskEntry` construction, builder assembly, strict
reconstruction, parser/evaluator collaborators, or diagnostics into
function-named child packages would therefore require public/protected exposure,
reflection, or a bridge API. That would weaken or widen the package-private
boundary mandated by Final.003 §8 and Backlog Item 0003 v000.004. Final.003 also
explicitly co-locates `NamingXmlCodec` and the internal validation machinery.

**Proposed resolution:** retain these internals in `naming` and keep the
supported surface narrow, as this candidate does. If future work needs physical
subpackage separation, first obtain an accepted design change defining an
enforceable cross-package encapsulation mechanism; do not silently make
implementation types public. No such design change is proposed or implemented
here.

Moving the three public enum FQNs from `...naming` to `...naming.valuesets` is a
package-level source/binary identity change, even though enum member names and
the simple typed accessor contracts are unchanged. Final.003 §8 specifies the
public validation vocabulary and says to export it separately from match
metadata, but does not state the old FQNs. Repository search found no Java
imports of Naming types outside Calliope and no downstream Maven dependency on
the `calliope` artifact. Compatibility with consumers outside this checkout is
not verified; no old-package aliases were retained, as instructed.

## Changes and scope

- Relocated `NamingFailureCategory`, `NamingOperation`, and `NamingFieldKind`
  to `naming.valuesets`; updated their production and test imports.
- Added `package-info.java` for the core package and `valuesets`.
- Added the package classification and encapsulation rationale to
  `calliope/README.md`.
- Centralized the hidden validation-exception factory in
  `NamingValidationException`; removed the corresponding exception-construction
  responsibility from `TopicFieldRules`.
- Strengthened `NamingStructureTest` to assert the exact public types in each
  package, the allowed package set, and the absence of reverse model imports
  from `valuesets`; retained API signature, visibility, size and dependency
  assertions.
- No accepted Backlog Item, design document, HCRL, Maven dependency, module
  boundary, or runtime behavior was intentionally changed. No commit or push was
  made.

## Consumer and dependency assessment

Repository search found no Java import of `net.fhirfactory.harmonia.model.core.naming`
outside Calliope. The only `calliope` artifact declaration found in Maven POMs
is Calliope's own module declaration; no dependent module was identified.
Accordingly, no external consumer refactor or integration scenario was in scope.
This evidence is limited to the checked-out repository and does not establish
absence of consumers outside it.

No Maven dependency was added or removed by this package-structure change.

## Verification

Environment: Linux, JDK 21 (`javac --release 21`), Maven 3.9.12.

| Command | Result |
| --- | --- |
| `mvn -B -ntp -pl calliope -am test` | **PASS** — 67 tests in 12 Calliope test classes; 0 failures, 0 errors, 0 skips. |
| `mvn -B -ntp verify` | **PASS** — all 50 reactor modules succeeded; Calliope reported 67 tests with 0 failures, errors or skips. Some modules emitted non-fatal empty-JAR warnings. |
| `mvn -B -ntp -pl calliope javadoc:javadoc` | **PASS** — Javadoc generation succeeded. |
| `mvn -B -ntp -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false test` | **UNEXECUTED** — build succeeded, but the selected module had no sources or tests to compile or run; this is not an architecture-test pass. |
| `git diff --check` | **PASS** — no whitespace errors. |
| Candidate patch replay from observed HEAD | **PASS** — apply check succeeded and the reconstructed tree matched `d186b400cf2ee01c0fc91aa10421ff036105613d`. |

The 67 is the observed test count for the Calliope suite, not a sum across
separate Maven runs. Full-reactor unique test totals were not calculated.

## Qualifications and review boundary

- The exact pre-edit worktree status for this refactor was not captured; the
  candidate tree and patch hash above identify the resulting Calliope state.
- The prescribed ArchUnit selector ran no tests because the module contains no
  source/tests. No ArchUnit coverage is claimed.
- Binary compatibility with unobserved external clients of the previous enum
  package is not verified. Independent review should confirm that the explicit
  `valuesets` allocation is consistent with the accepted public API boundary.
- This Report does not accept the implementation, close Software HCRL 010,
  change Architecture release state, or supersede any existing review finding.
  Independent review and Solution Architect disposition remain required.
- The report-emission timestamp remains unresolved for Solution Architect
  treatment under the provenance contract.

[task]: ../../before/ai-agent-tasks/20261125T033725Z-naming-0003-package-structure-refactor-task.md