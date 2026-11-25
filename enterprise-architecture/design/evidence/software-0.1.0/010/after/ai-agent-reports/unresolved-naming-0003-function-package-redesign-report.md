<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Junie Implementation Report — Calliope Naming Function-Package Redesign

Report-Produced-At: UNRESOLVED. The interface exposes no attributable
timestamp for completed report emission. No command-completion time,
filesystem metadata or unrelated event time has been substituted. The
filename prefix remains unresolved for Solution Architect treatment.

**Task** · [Object model and function-package redesign][task]
(Task-Issued-At 2026-11-25T04:35:58.919Z).

**Repository / branch** · `fhirfactory/harmonia` ·
`feature/BL-0003-Calliope-Topic-and-TopicMask`.

**Software target** · `0.1.0-SNAPSHOT`, review target `software-0.1.0-010`.

**Starting source** · HEAD `46a08484e5c824b870fecda59b943ba37a241e2b`; Calliope
tree `d186b400cf2ee01c0fc91aa10421ff036105613d` (the previous package-refactor
candidate). The real Git index was clean at start and finish.

**Resulting candidate** · Tree `cf059f48f17cc9dc0f301945ba80feef7dccf9cd`,
obtained by staging `calliope` over HEAD in a temporary index. The patch is
[`naming-0003-function-package-redesign-candidate.patch`][patch], SHA-256
`0c394b60913bc4a1d4451c80751accbb993bc567259d1502356a4714ece8ca19`. It covers
63 files (+2790/−902) against HEAD; relative to `d186b400` the delta is 58
files (+2596/−829). Replaying the patch onto HEAD reproduced the tree exactly.
The patch excludes this Report and its Task. An earlier draft of the patch
from this same assignment, captured before serialization was added, was
overwritten and is not evidence.

**Standing** · Unaccepted candidate for independent review and Solution
Architect adjudication. No commit, push, design, backlog, HCRL or release
change was made.

## 1. Outcome

The Naming implementation is reorganised into six standard Java packages
named by function. The supported surface is the root package and
`naming.valuesets`. Behaviour and supported member signatures of Final.003 §8
are preserved. Per the 07:26Z disposition, the four Naming models and the
validation exception are now serializable.

```text
net.fhirfactory.harmonia.model.core.naming          (supported)
  Topic, TopicMask                     principal values (identity / selector)
  TopicEntry, TopicMaskEntry           dependent values, owned by their model
  TopicBuilder, TopicMaskBuilder       fluent construction
  NamingXmlCodec                       interchange facade
  NamingValidationException            public failure contract
  NamingBoundary         (pkg-private) violation -> public exception
  NamingSerialization    (pkg-private) shared proxy checks
naming.valuesets                                    (supported)
  NamingFailureCategory, NamingOperation, NamingFieldKind
naming.validation                                   (implementation)
  TopicFieldRules, NamingLimits, NamingRuleViolation
naming.canonical                                    (implementation)
  CanonicalPathSyntax, TopicCanonicalParser, MaskExpressionParser,
  CanonicalEntryFields
naming.matching                                     (implementation)
  FieldPattern, VersionConstraint, NumericVersion (pkg-private),
  EntryCriterion, MatchCandidate, MaskEvaluator, MatchProbe,
  MatchDiagnosticSink, NamingDiagnostic, DiagnosticSeverity, MatchExitCategory
naming.interchange                                  (implementation)
  NamingXmlFormat, XmlEntryFields, XmlMaskRecord,
  XmlRecordParser (pkg-private)
```

The layering is checked by tests and by `jdeps -verbose:package`:
`valuesets` depends on nothing; `validation` depends only on `valuesets`; and
`canonical`, `matching` and `interchange` depend only on `validation` and
`valuesets`. Only the root package depends on the implementation packages, and
no implementation package refers to a model type.

## 2. Design rationale

**Classification.** Following the three-tick test in the Granularity note:

- `Topic` and `TopicMask` carry distinct domain meaning and own their
  constituents. They have no independent managed lifecycle and are immutable
  values.
- `TopicEntry` and `TopicMaskEntry` are dependent data types. They are composed
  into, and created only through, their owning model or builder.

No architectural identity or relationship was derived from Java class
boundaries.

**Construction boundary inverted.** Previously, the parsers and the codec
constructed models directly, which was the main reason all machinery had to
share one package. Now:

- the canonical parsers return `CanonicalEntryFields`;
- `NamingXmlFormat` returns `XmlEntryFields` and `XmlMaskRecord`; and
- the evaluator reads a `MatchCandidate` view.

The models alone assemble completed entries. So every construction route —
builders, canonical text, XML and now deserialization — passes through the
same package-private `create` paths with the actual operation and entry index.

**Failure boundary.** Implementation packages cannot reach the hidden factory
of `NamingValidationException`. They raise a stackless, payload-free
`NamingRuleViolation`. The package-private `NamingBoundary` translates it
field for field at each delegation point, so callers see only the public
exception, and it carries no cause. Other runtime exceptions are treated as
defects and propagate unchanged.

**Matching.** `TopicMaskEntry` keeps its raw text as identity and owns one
compiled `EntryCriterion`. `TopicMask` passes the compiled criteria and a
private `TopicView` adapter to `MaskEvaluator`. The ordering, 48-check bound,
fail-closed handling and one-shot diagnostics are unchanged. The test probe now
operates on `EntryCriterion`.

**XML.** As authorised, the public `NamingXmlCodec` is a thin facade that maps
models to neutral records. `NamingXmlFormat` owns the element vocabulary,
deterministic output, Base64, bounds and hardened parsing. `XmlRecordParser`
is package-private to `interchange`.

**Visibility.** Implementation types are public only where another Naming
package calls them; each is documented as *not part of the supported Naming
API*. `NumericVersion`, `XmlRecordParser`, `CanonicalPathSyntax`'s scanning
methods, `NamingBoundary` and `NamingSerialization` remain package-private.
Supported types still have no public or protected constructors, and their
public or protected signatures expose only `java.*`, `naming` or
`naming.valuesets` types.

**Serialization.** `Topic`, `TopicEntry`, `TopicMask` and `TopicMaskEntry` use
the serialization-proxy pattern:

- **Write.** `writeReplace` emits a nested `SerialForm` holding raw text only;
  every model instance field is `transient`.
- **Read.** `readResolve` checks array presence, length and depth, then
  rebuilds the model through the strict `create` path.
- **Universal mask.** It resolves to the shared `TopicMask.all()` instance.
- **Rejection.** A direct `readObject` always throws `InvalidObjectException`,
  as does any invalid proxy content; the message names only the type.
- **Compiled criteria.** They are recomputed, never deserialized.
- **Exception.** `NamingValidationException` previously held a
  non-serializable `OptionalInt`. It now stores a primitive index that is
  re-range-checked on read.

Builders are deliberately not serializable.

## 3. Conflicts with Final.003 §8 and proposed resolutions

| # | Final.003 / Backlog prescription | Candidate | Status / proposal |
| --- | --- | --- | --- |
| C1 | Keep `TopicCanonicalParser`, `MaskExpressionParser`, `CanonicalPathSyntax`, `MaskEvaluator` package-private; keep `TopicFieldRules`, `NamingLimits` internal; co-locate diagnostic types with evaluation. | Function packages. These types are public but documented as unsupported and confined by structural tests. Diagnostics remain co-located with evaluation in `matching`. | Applied under the 07:02Z disposition (function packages, appropriate visibility, no JPMS). **Proposal:** amend §8 to read “not part of the supported API; confined to the Naming function packages”, plus a repository ArchUnit rule when that suite exists. |
| C2 | “Keep XML concerns inside the codec.” | Codec facade in `naming`; XML mechanics in `naming.interchange`. | Authorised by the 07:02Z disposition. Proposal: record it as the §8 interpretation. |
| C3 | Supported surface lists no serialization capability. | Four models and the exception implement `Serializable`, using validating proxies. | Applied under the 07:26Z disposition. **Proposal:** record the extension, its proxy and validation semantics, and the non-serializable builders in Final.003 §8 or its successor through HCRL. |
| C4 | Granularity / Guide §7 suggest `datatypes`, `factory` subpackages. | Entries and builders stay in the root package. | Recommended as is. Moving them would require public `create` paths or public model assembly, which would let callers bypass strict validation. The function packages already achieve the intended separation. |

Classpath consumers can still compile against the unsupported public types; no
compile-time enforcement exists without JPMS, which the Solution Architect
declined. Enforcement is by documentation and `NamingStructureTest`.

## 4. Trade-offs

- **More public types.** There are 20 public implementation types, a deliberate
  cost of cross-package collaboration in standard Java. They are mitigated by
  explicit documentation, per-package allow-lists and a signature-exposure
  test.
- **Small neutral records.** `CanonicalEntryFields` and `XmlEntryFields` are
  structurally similar. They are kept separate so that `canonical` and
  `interchange` do not depend on each other.
- **Allocation.** Each `matches` call allocates one small `TopicView`. The
  translation boundary adds a lambda per delegated call. Neither changes the
  bounds or semantics.
- **Serialization proxies.** They add about 50 lines per model, and Topic is
  now 396 lines, the largest production file. All production files remain
  below 500 lines; the next largest is `XmlRecordParser` at 363.

## 5. Changed-file inventory (relative to the starting tree `d186b400`)

- **Added (main).** `NamingBoundary`, `NamingSerialization`;
  `validation/{NamingRuleViolation, package-info}`;
  `canonical/{CanonicalEntryFields, package-info}`;
  `matching/{EntryCriterion, MatchCandidate, package-info}`;
  `interchange/{NamingXmlFormat, XmlEntryFields, XmlMaskRecord, package-info}`.
- **Moved/reworked (main).** `NamingLimits`, `TopicFieldRules` → `validation`;
  `CanonicalPathSyntax`, `TopicCanonicalParser`, `MaskExpressionParser` →
  `canonical`; `FieldPattern`, `VersionConstraint`, `NumericVersion`,
  `MaskEvaluator`, `MatchProbe`, `MatchDiagnosticSink`, `NamingDiagnostic`,
  `DiagnosticSeverity`, `MatchExitCategory` → `matching`; `XmlRecordParser` →
  `interchange`.
- **Modified (main).** `Topic`, `TopicEntry`, `TopicBuilder`, `TopicMask`,
  `TopicMaskEntry`, `TopicMaskBuilder`, `NamingXmlCodec`,
  `NamingValidationException`, root `package-info`.
- **Tests.** Added `NamingBoundaryTest`, `SerializationTest` and
  `canonical/CanonicalParserTest`. `FieldPatternTest` moved to `matching` and
  gained compiled-criterion coverage. `NamingStructureTest` was rewritten.
  `MatchDiagnosticsTest`, `ResourceBoundsTest`, `TopicMaskTest`,
  `VersionConstraintTest`, `TopicBuilderTest`, `TopicCanonicalParserTest` and
  `NamingXmlCodecTest` changed only imports and probe types; `TopicMaskTest`
  also received the relocated literal-limit case. No behavioural assertion was
  weakened.
- **Docs.** `calliope/README.md`: package organisation, Java serialization and
  diagnostics sections.
- **No change.** `calliope/pom.xml`, XML fixtures, value-set enums, other
  modules, accepted documents, backlog items and HCRLs.

## 6. Verification (JDK 21, Maven 3.9.12, Linux)

| Command / check | Result |
| --- | --- |
| `mvn -B -ntp -pl calliope -am test` | **PASS** — 84 tests in 15 classes; 0 failures, errors or skips. |
| `mvn -B -ntp verify` | **PASS** — all 50 reactor modules SUCCESS; Calliope 84/0/0/0. There were 32 non-fatal “JAR will be empty” warnings in other modules. |
| `mvn -B -ntp -pl calliope javadoc:javadoc` (clean output) | **PASS** — 0 warnings. |
| `javac --release 21 -Xlint:all` over Calliope main sources | **PASS** — 0 warnings. |
| `jdeps -s` / `-verbose:package` on `calliope/target/classes` | `java.base`, `java.xml` only; package dependencies as in §1. |
| `mvn ... -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" ...` | **Not executed (no tests)** — build succeeded with “No sources to compile / No tests to run”. This is not a PASS. |
| Patch replay and `git diff --check` | **PASS** — replayed tree equals `cf059f48…`; no whitespace errors. |

**Test accounting.** The figure of 84 is the unique test methods reported by
the current Surefire run across 15 classes:

| Class | Tests | Class | Tests | Class | Tests |
| --- | --- | --- | --- | --- | --- |
| `TopicBuilderTest` | 9 | `TopicMaskTest` | 7 | `MaskCanonicalExpressionTest` | 5 |
| `NamingStructureTest` | 8 | `TopicTest` | 7 | `TopicCanonicalParserTest` | 4 |
| `NamingXmlCodecTest` | 8 | `ResourceBoundsTest` | 6 | `NamingBoundaryTest` | 3 |
| `matching.FieldPatternTest` | 5 | `SerializationTest` | 6 | `canonical.CanonicalParserTest` | 3 |
| `MatchDiagnosticsTest` | 6 | `VersionConstraintTest` | 6 | `ConcurrencyTest` | 1 |

Of these, 67 come from the previous candidate's suite, which was carried
forward (one test relocated) with its assertions intact. The remaining 17 are
new.

`calliope/target/surefire-reports` still contains stale XML files from
earlier runs; for example, the old root-package `FieldPatternTest`. Those
files account for the 92 test cases a directory-wide count produces. They are
not evidence for this candidate.

The two `verify` executions of the same tests are not additive.

## 7. Consumer and dependency assessment

No source outside Calliope imports `net.fhirfactory.harmonia.model.core.naming`,
and no module POM depends on `calliope`. The FQNs of the supported types are
unchanged from the previous candidate. Within this checkout, no consumer
refactor is required. Compatibility with consumers outside this checkout is
unverified. No Maven dependency was added or removed.

## 8. Known limitations and unresolved items

1. **Report-Produced-At.** UNRESOLVED, along with the filename prefix.
2. **Questionnaire timestamp.** The time of the Solution Architect's
   questionnaire answers is UNRESOLVED (recorded in the Task).
3. **Final.003 §8 amendments.** C1–C3 are applied under explicit dispositions
   but are not yet recorded in Final.003 or an HCRL; C4 is a recommendation.
   They require Architecture Review and HCRL treatment.
4. **Architecture suite.** The ArchUnit suite named in `AGENTS.md` §4 does not
   exist in `paradeigma-test`. Package confinement is enforced only by
   `NamingStructureTest`.
5. **Untrusted deserialization.** Java deserialization of untrusted input
   remains the caller's responsibility; JDK filters still apply. The proxies
   guarantee model validity, not stream-level resource limits.
6. **Iteration placement.** Placing this evidence in iteration 010 follows the
   previous Tasks, but the iteration has not been confirmed by the Solution
   Architect.
7. **Carried obligations.** Integrated runtime verification, operational
   logging and the earlier Software HCRL 010 qualifications are carried
   forward unchanged.

## 9. Confirmations

- No legacy compatibility alias, bridge or `internal` package was introduced,
  and no JPMS descriptor was added.
- Final.003 behaviour, the supported member signatures and the value-set
  vocabulary are preserved.
- The implementation does not claim acceptance. The next activity is
  independent review and Solution Architect adjudication.

[task]: ../../before/ai-agent-tasks/20261125T043558Z-naming-0003-function-package-redesign-task.md
[patch]: ../naming-0003-function-package-redesign-candidate.patch
