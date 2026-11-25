<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Implementation Report — Backlog Item 0003

**Standing** · Implementation candidate completed; not submitted for acceptance.

Report-Produced-At: UNRESOLVED. The connected file-emission interface
did not expose an attributable system timestamp for this completed Report. No
terminal clock value, filesystem timestamp, commit time or inferred time has
been substituted. The filename prefix is correspondingly unresolved for
Solution Architect treatment before governed closure.

**Corresponding Task** ·
[`20261124T082329Z-naming-0003-implementation-task.md`](../../before/ai-agent-tasks/20261124T082329Z-naming-0003-implementation-task.md),
issued `2026-11-24T08:23:29.194Z` under Software Development Praxis. Its
approved Task source SHA-256 is
`af6fb43230877c0ff49a2fd1cb969c9c060759807e88ebbbe435becc9a8d1578`.

**Originating work** · Backlog Item 0003, revision `000.003`,
[`source`](../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md),
SHA-256 `6c5b7bf1612105d8043e62becadb8923d66d74cbf9151130567dd030dba5947b`.

**Accepted design** ·
[`DRP-Topic-and-TopicMask.Final.001.001.md`](../../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md),
accepted with qualifications under Software HCRL `004`, DR-10; standalone
prerequisite authority under Architecture HCRL `1.1.28/001`, DR-6.

**Target** · `software-0.1.0`, iteration `010`; source module `calliope`.

## Source and candidate identity

- Starting branch: `feature/BL-0003-Calliope-Topic-and-TopicMask`, tracking
  `origin/feature/BL-0003-Calliope-Topic-and-TopicMask`.
- Starting commit: `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`.
- Starting Git tree: `1846199b36bbfb07c87746c1367c059698b7120d`.
- The Task records the accepted development foundation tree, approved Task
  source hash, complete pre-existing working-tree inventory and status capture
  time. The index was clean at the source baseline.
- The previously modified convergence plan and pre-existing `.junie`, backlog,
  and Architecture `1.1.28/001` artifacts were not edited by this implementation.
  No unrelated source, Continuum, HCRL, release or review record was changed.
- The implementation remains uncommitted and unstaged. The retained patch is
  [`naming-0003-candidate.patch`](../naming-0003-candidate.patch), SHA-256
  `70ad92b2503e89b15ff150e89562723b23ca80c1f7cc94648863e7f5a553f97a`.
  It applies cleanly to the source index with `git apply --check --cached
  --whitespace=error` and contains the exact scoped Calliope candidate, including
  new source, tests and fixtures. It does not include pre-existing workspace
  changes, this Report or the Task record.

## Implemented result

The candidate adds the approved immutable `Topic`, `TopicEntry`, `TopicBuilder`,
`TopicMask`, `TopicMaskEntry` and `TopicMaskBuilder` API in Calliope Naming. It
includes strict canonical Topic and mask parsers, shared Unicode/field
validation, safe categorized `NamingValidationException` failures, RE2/J
construction-time compilation, bounded lexical safeguards, local fail-closed
match diagnostics and a deterministic UTF-8 XML reader/writer. The XML codec
preserves ordered entries, explicit mask kind, containment and present-empty
regex fields; it uses Base64 for regex text, caps the complete record before
decoding, disables DTD/external entity processing and rejects unknown structure.

Only the two authorized Calliope dependency declarations were added:
`com.google.re2j:re2j:1.8` and test-scoped
`org.junit.jupiter:junit-jupiter:5.10.2`. The inherited Java 21 compiler and
Surefire configuration, parent POM, module graph, plugins and logging
dependencies remain unchanged.

The accepted limits are applied: depth 16; Topic qualifier/value/version 64/256/64
Unicode code points; each mask expression 512 code points; canonical Topic and
mask ceilings 32 KiB and 128 KiB UTF-8; XML record 256 KiB UTF-8; active group
depth 16; counted repetition bound 256; saturating expansion estimate 4,096;
native RE2/J `programSize()` at most 1,024; and at most 48 full-field checks per
ordinary comparison. Patterns are compiled once and matchers remain
invocation-local. No Java RegEx fallback, second regex parser, global match
cache, or logger infrastructure was introduced.

The two accepted implementation qualifications are preserved:

1. **RE2/J safeguards** — safeguards are limited to bounded inputs, native
   program size and simple lexical group/repetition/expansion checks; they do
   not parse or estimate a second regex language.
2. **Diagnostics** — validation and comparison metadata remain synchronous,
   bounded and local to Naming. Evaluation errors return false with safe ERROR
   intent; no application LogEvent emission, global mutable state, asynchronous
   processing, buffering, queues or delivery guarantees were added.

The separate interim verification qualification is also preserved. No files
were found under the governed exploratory ArchUnit source location, so that
suite is reported as unexecuted; the separate Architecture `1.1.27/002`
amendment retains its standing. The applicable `NamingStructureTest` verifies
the per-file 500-line maximum and the Naming dependency boundary.

## Acceptance-criteria correspondence

| Criterion | Candidate and evidence |
| --- | --- |
| AC-1 — Identity, ordering, hierarchy | `TopicTest` covers version-independent Topic identity, hash/order, proper prefixes, ancestry and navigation; `TopicMaskTest` covers logical mask identity and version presence. |
| AC-2 — Names and canonical parsing | `TopicTest` and `TopicCanonicalParserTest` cover projections, escaped delimiters, strict grammar and malformed separators. |
| AC-3 — Versions | `TopicTest`, `TopicMaskTest` and XML fixture tests distinguish Topic absence, mask absence/present-empty/wildcard and constrained matching. |
| AC-4 — Mask evaluation | `TopicMaskTest` covers positional full-field matching, inline flags, exact/trailing depth, literal helpers, ordinary versus universal kind, null and fail-closed behavior. |
| AC-5 — Builders and validation | `TopicBuilderTest` and `TopicMaskTest` cover mode selection, gaps/recovery, failed replacements, builder reuse and immutable snapshots. |
| AC-6 — Serialization | `NamingXmlCodecTest` exercises actual reader/writer fixtures for Topic, ordinary mask, universal kind, versions and Base64 regex preservation. |
| AC-7 — Lossless reconstruction | Topic canonical round trips, mask canonical round trips, scoped regex flags, structural escapes, empty expressions and XML fixtures compare logical data after decode. |
| AC-8 — Malformed/adversarial input | Tests cover missing/invalid fields, malformed Unicode, unsupported regex syntax, `@all`, invalid canonical syntax, DTD/entity input, unknown XML structure, malformed Base64 and injected matcher/diagnostic failures. |
| AC-9 — Bounds/resource use | `TopicBuilderTest`, `TopicCanonicalParserTest`, `ExpressionSafeguardTest`, `NamingXmlCodecTest` and the deterministic `TopicMaskTest` workload exercise field/depth/canonical/XML limits, safeguards, native program-size cap and 48-check maximum. |
| AC-10 — Concurrency | `TopicMaskTest` reuses one immutable compiled mask across concurrent match calls; builder snapshot isolation is tested separately. |
| AC-11 — Structural coherence | `NamingStructureTest` checks all Naming production files are at most 500 lines and rejects Themis, logging and Java RegEx imports; it checks the exact RE2/J/JUnit coordinates. The candidate patch confirms the bounded POM/source scope. |
| AC-12 — Implementation Guide | Defensive failures, preserved builder state, safe diagnostics, concurrency and documented test limitations are covered. Integrated readiness remains unclaimed pending the review and verification treatment below. |
| AC-13 — Educational documentation | Naming classes, public operations and significant parsers/evaluation helpers contain Javadocs covering responsibilities, constraints and failure semantics. |

## Verification

Environment: OpenJDK `21.0.13.1`, Apache Maven `3.9.12`, Linux, UTF-8. Both
commands were bounded with `timeout`; neither timed out or stalled.

| Command | Scope and outcome |
| --- | --- |
| `timeout --signal=TERM 150s env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:$PATH mvn -B -ntp -pl calliope -am test` | PASS. Parent and Calliope (2 reactor modules); all 36 Calliope JUnit tests passed, 0 failures/errors/skips. |
| `timeout --signal=TERM 360s env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:$PATH mvn -B -ntp verify` | PASS. All 50 reactor modules succeeded; 72 tests across 8 test summaries, 0 failures/errors/skips. Calliope ran 36 tests. Maven emitted non-fatal warnings for empty JAR artifacts. |

The deterministic maximum-depth workload recorded `iterations=1000`,
`depth=16`, `fieldChecks=48`, and `elapsedNs=39029317` in the full reactor
run. The focused run independently reported `elapsedNs=39799364` for the same
fixed workload. Structural checks passed, and the candidate patch passed
`git apply --check --cached --whitespace=error` against the clean source index.

The full Maven reactor exercised its configured module builds and tests; it did
not establish live service/deployment behavior or, by itself, prove every
solution-wide regression, integration and conformance scenario required for an
acceptance candidate. No production ingress/egress, container, distributed
failure/recovery or deployment workload was run. The exploratory ArchUnit suite
was absent and unexecuted under the accepted qualification. These are explicit
verification limits, not passes. Per Software Development Praxis, required
missing solution-wide coverage needs explicit Solution Architect treatment in
the eventual candidate HCRL before readiness or acceptance can be claimed.

## Boundaries and pending governed activity

- No independent Codex implementation review or additional Architecture Review
  was initiated in this bounded Task. Both the independent implementation
  review and the required proportionate Architecture Review remain separate
  prerequisites to later acceptance; this Report is not evidence that either
  occurred.
- No software HCRL was created or advanced, no acceptance or closure is claimed,
  and no Architecture release identity or publication was changed. The accepted
  Architecture `1.1.26` baseline and separate `1.1.27/002` amendment and
  `1.1.28/001` reconciliation retain their existing standing.
- No commit, push, merge, tag, deployment or publication was performed or
  authorized by this Task. The source candidate and patch remain uncommitted.
- Shared application logging, operational delivery guarantees, IU vocabulary,
  specialised reference mappings, downstream integration, compatibility and
  migration remain outside this assignment and retain their existing authority.

## Exact candidate paths

```text
calliope/pom.xml
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/CanonicalPathSyntax.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/ExpressionSafeguard.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/FieldEvaluator.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/MaskEvaluator.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/MaskExpressionParser.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/MatchDiagnosticSink.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/NamingValidationException.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/Topic.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicBuilder.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicCanonicalParser.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicEntry.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicFieldRules.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicMask.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicMaskBuilder.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicMaskEntry.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/datatypes/NamingDiagnostic.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/interchange/NamingXmlCodec.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/valuesets/DiagnosticSeverity.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/valuesets/MatchExitCategory.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/valuesets/NamingFailureCategory.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/valuesets/NamingFieldKind.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/valuesets/NamingLimits.java
calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/valuesets/NamingOperation.java
calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/ExpressionSafeguardTest.java
calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/NamingStructureTest.java
calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/NamingXmlCodecTest.java
calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/TopicBuilderTest.java
calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/TopicCanonicalParserTest.java
calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/TopicMaskTest.java
calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/TopicTest.java
calliope/src/test/resources/net/fhirfactory/harmonia/model/core/naming/interchange/mask-present-empty-version.xml
calliope/src/test/resources/net/fhirfactory/harmonia/model/core/naming/interchange/topic.xml
```