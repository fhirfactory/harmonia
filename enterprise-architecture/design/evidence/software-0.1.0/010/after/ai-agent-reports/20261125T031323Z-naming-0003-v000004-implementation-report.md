<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Calliope Naming Implementation Report — Backlog Item 0003 v000.004

Report-Produced-At: 2026-11-25T03:13:23Z
Task-Issued-At: 2026-11-25T01:43:36.190Z
**Agent / role** · Junie · Software implementation and verification  
**Software target** · `0.1.0-SNAPSHOT` · review target `software-0.1.0-010`  
**Branch** · `feature/BL-0003-Calliope-Topic-and-TopicMask`

**Disposition** · Implementation candidate submitted for independent review.
This report does not accept the implementation, close Software HCRL 010, or
establish a released software baseline.

## 1. Authority, scope and architecture review

The implementation follows accepted Backlog Item 0003 revision `000.004` and
`DRP-Topic-and-TopicMask.Final.003.md`, covering AC-1 through AC-13. The
Solution Architect's standalone implementation authorization and the five
prerequisites recorded under Architecture HCRL 1.1.28/001 DR-6 were applied.
The implementation does not reopen those decisions or make an architecture
change. [Task Detail][task] · [Accepted Backlog Item][backlog] · [Final.003][design] ·
[Architecture DR-6][dr6]

The architecture review for this bounded change found no new architectural
decision or conflict requiring implementation escalation. The change remains
inside Calliope's pure model/library boundary: no higher Harmonia module,
authorization, persistence, audit, routing, or logging dependency was added.
TopicMask matching returns a Boolean only and does not grant authorization.
The accepted Final.003 contract remains the detailed design authority; the
incorporated Architecture baseline remains 1.1.26.

## 2. Starting source and treatment of the RE2/J candidate

At task entry the branch was `feature/BL-0003-Calliope-Topic-and-TopicMask`,
HEAD was `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, and its tree was
`1846199b36bbfb07c87746c1367c059698b7120d`. The real index was clean. The
worktree held the unaccepted Calliope candidate as untracked source: 23
production Java files, seven JUnit classes and two XML fixtures; the modified
`calliope/pom.xml` declared compile-scope `com.google.re2j:re2j:1.8`. The
accepted preparation report recorded the opening worktree status and digests;
pre-existing unrelated work, including the cited `.junie` plans, documentation,
backlog, convergence plan, Architecture review and evidence, was preserved.
[Preparation evidence][prep]

The prior candidate was treated solely as reusable engineering material. Its
exact patch SHA-256 was
`70ad92b2503e89b15ff150e89562723b23ca80c1f7cc94648863e7f5a553f97a`; its
Software HCRL 010 review has no Solution Architect disposition. No foundation
checkpoint reset or broad revert was performed. Existing Topic, TopicBuilder,
TopicEntry and field-rule logic was retained where it met Final.003; the
RE2/J-based evaluator/safeguards, old diagnostic/value-set plumbing, old XML
codec placement, obsolete test and old mask fixture were replaced or removed.
No compatibility API was introduced.

## 3. Implemented scope and changed-file inventory

The final candidate contains 45 Calliope files: 26 production Java files in the
single supported `model.core.naming` package, 12 test classes, five XML fixtures,
`calliope/pom.xml`, and `calliope/README.md`. No application module outside
Calliope was changed.

The production implementation now provides:

- Immutable ordered Topic identity, full TopicEntry equality, version-preserving
  navigation, strict projections and canonical parsing; sequential/indexed
  builders preserve state after unsuccessful operations.
- Classified six-form qualifier/value wildcard patterns with literal escape
  handling, raw-spelling preservation, optional version constraints, bounded
  component-wise decimal comparison and non-overlapping prefix/suffix matching.
- Ordinary and explicit universal TopicMask kinds, structural equality,
  exact-depth default matching, explicit containment, positional short-circuit
  evaluation and null-safe fail-closed results.
- A stateless `NamingXmlCodec` in the Naming package with the exact four
  operations, deterministic UTF-8 output, ordered completed reconstruction,
  padded UTF-8 Base64 mask fields, and hardened bounded JDK XML parsing.
- The approved public model/factory/accessor surface and typed public failure
  vocabulary; canonical parsers, pattern/version evaluators, XML helpers,
  resource limits and diagnostics remain package-private.
- Invocation-local optional diagnostics. Public `matches(Topic)` has no handler;
  no diagnostic record is constructed without a sink; one safe record is
  emitted only after the Boolean result is established.

The final public types are exactly `Topic`, `TopicEntry`, `TopicBuilder`,
`TopicMask`, `TopicMaskEntry`, `TopicMaskBuilder`, `NamingXmlCodec`,
`NamingValidationException`, `NamingFailureCategory`, `NamingOperation`, and
`NamingFieldKind`. All production Java files remain below 500 lines. The
supported contract and caller responsibilities are documented in the JavaDocs
and [Calliope Naming README][readme].

## 4. AC-1 through AC-13 implementation and evidence map

| Criterion | Implementation and evidence | Result |
| --- | --- | --- |
| AC-1 | Topic identity/order excludes optional versions; TopicEntry equality includes them; mask kind, ordered raw criteria, constraint spelling and containment are structural. `TopicTest`, `TopicMaskTest`, `VersionConstraintTest`. | Implemented; tests pass. |
| AC-2 | Strict canonical forms and raw name, relative, friendly and display projections; hierarchy and ancestor navigation preserve versions. `TopicTest`, `TopicCanonicalParserTest`. | Implemented; tests pass. |
| AC-3 | Omitted, present-empty, exact-text, escaped-leading-operator, numeric single comparisons and ordered inclusive/exclusive ranges; original spellings retained. `VersionConstraintTest`, `MaskCanonicalExpressionTest`. | Implemented; tests pass. |
| AC-4 | Six accepted wildcard forms, case sensitivity, Unicode, punctuation literalness, escaped `*`/backslash, unsupported arrangements and non-overlapping prefix/suffix. `FieldPatternTest`, `TopicMaskTest`. | Implemented; tests pass. |
| AC-5 | Sequential/indexed modes, out-of-order indexed additions, contiguous build, replacement, deterministic generated qualifiers, builder failure recovery and immutable snapshots. `TopicBuilderTest`, `TopicMaskTest`. | Implemented; tests pass. |
| AC-6 | Ordered Topic/mask XML, explicit kind and containment, empty versus omitted constraints, padded Base64, deterministic bytes and independent valid/invalid fixtures. `NamingXmlCodecTest`; `topic.xml`, `ordinary-mask.xml`, `ordinary-mask-empty-constraint.xml`, `universal-mask.xml`, `malformed-mask-order.xml`. | Implemented; tests pass. |
| AC-7 | Strict one-layer canonical escaping, raw masks, versions, empty pattern/constraint distinctions and explicit ordinary/universal boundary. `TopicCanonicalParserTest`, `MaskCanonicalExpressionTest`. | Implemented; tests pass. |
| AC-8 | Exact public API allow-list/visibility, safe category-operation-field-index metadata, strict reconstruction and one-record diagnostics with throwing-handler isolation. `NamingStructureTest`, `MatchDiagnosticsTest`, `NamingXmlCodecTest`. | Implemented; tests pass. |
| AC-9 | Fixed field, pattern, operand/component, depth, UTF-8 canonical, XML record and Base64 bounds; 17th-entry guards; maximum 48 positional checks; one numeric candidate parse per version check. `ResourceBoundsTest`, `FieldPatternTest`, `VersionConstraintTest`, `NamingStructureTest`. | Implemented; tests pass. |
| AC-10 | Shared immutable masks and parsed descriptions reused concurrently across mixed outcomes with bounded worker waits. `ConcurrencyTest`. | Implemented; test passes. |
| AC-11 | No RE2/J or regex matching, no replacement engine, no higher-layer import, exact public inventory and single-package containment. `NamingStructureTest`, dependency tree, `jdeps`, POM/source searches. | Implemented; checks pass. |
| AC-12 | Focused tests and full 50-module Maven `verify` ran; no affected external consumer was found. The named ArchUnit module has no source or tests, and no reactor runtime integration tests are available. | **Qualified / acceptance gate remains open**; see §7. |
| AC-13 | Public JavaDocs and implementation README describe input forms, builders, versions, canonical/XML contracts, limits, failures, immutability, diagnostics and caller responsibilities. `mvn -B -ntp -pl calliope javadoc:javadoc`. | Implemented; Javadoc build passes. |

## 5. Test, build and dependency verification

JDK: OpenJDK `21.0.13.1`, Maven `3.9.12`, UTF-8 platform encoding.

| Execution | Scope and result |
| --- | --- |
| `mvn -B -ntp -pl calliope -am test` | **PASS.** 12 Calliope test classes; 67 executed tests; 0 failures, 0 errors, 0 skipped. |
| `mvn -B -ntp verify` | **PASS.** 50 reactor modules; Calliope's same 67 tests passed; the remaining reactor modules reported no tests to run. No other test cases were counted. Maven emitted non-fatal empty-JAR warnings for modules without sources. |
| `mvn -B -ntp -pl calliope javadoc:javadoc` | **PASS.** Javadoc generation completed successfully. |
| `mvn -B -ntp -pl calliope dependency:tree -Dscope=compile` | **PASS.** No compile-scope dependencies are declared for Calliope. |
| `jdeps -s --multi-release 21 calliope/target/calliope-0.1.0-SNAPSHOT.jar` | **PASS.** Required modules are `java.base` and `java.xml` only. |
| `mvn -B -ntp -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false test` | Build **PASS**, but the selector reports **No tests to run**; the named ArchUnit source/test suite is absent and is **UNEXECUTED**, not a pass. |

The 67 unique test methods are accounted once below. The two required Maven
executions each ran this same suite; they are separate executions, not 134
distinct tests.

| Test class | Executed |
| --- | ---: |
| `NamingStructureTest` | 5 |
| `TopicBuilderTest` | 9 |
| `MatchDiagnosticsTest` | 5 |
| `TopicMaskTest` | 6 |
| `VersionConstraintTest` | 6 |
| `ResourceBoundsTest` | 6 |
| `FieldPatternTest` | 5 |
| `MaskCanonicalExpressionTest` | 5 |
| `TopicTest` | 7 |
| `ConcurrencyTest` | 1 |
| `TopicCanonicalParserTest` | 4 |
| `NamingXmlCodecTest` | 8 |
| **Distinct tests total** | **67** |

During development, two assertions failed because test setup expected a
package-private diagnostic overload to be absent and attempted to match a
three-entry Topic with a one-position exact-depth mask. Both test assumptions
were corrected locally; the final required test and verify executions above
report no failures, errors or skips.

## 6. Dependency, consumer and structural assessment

The compile-scope RE2/J declaration was removed; JUnit remains test-scoped.
The source structure test verifies there are no `java.util.regex` or
`com.google.re2j` references, no public adapter/diagnostic bridge, no Naming
subpackages, and no production file at or above 500 lines. The verified
candidate patch applies cleanly to the recorded foundation HEAD.

The repository search for `import net.fhirfactory.harmonia.model.core.naming`
found no Java importers; a POM artifact search found only Calliope's own
`<artifactId>calliope</artifactId>` declaration. The accepted preparation
report's 50-POM reactor survey likewise found no consumer. No direct or indirect
consumer required refactoring. The repository cannot establish behavior for
external or dynamically loaded consumers; none was inferred.

## 7. Resource, concurrency and documentation conclusions

The tests cover the accepted maximum depth, UTF-8 byte sizing, pattern literal
expansion, numeric operand/component/range limits, Base64 field and XML record
limits, known/unknown failure indexes, maximum 48 field checks, and early
canonical rejection before segment substring materialization. Numeric versions
are compared by bounded component spans; source structure checks one candidate
parse per version comparison and no integer, floating-point, BigInteger or
BigDecimal conversion. Completed Topic/TopicMask descriptions are immutable;
no shared mutable evaluation state or global match cache was introduced.

The README and public JavaDocs document the supported contract; Javadoc
generation passes. Canonical and pattern renderings may contain sensitive
values and are explicitly not sanitized diagnostic output. No logging framework
or logging infrastructure was introduced.

## 8. Qualifications, unresolved gates and acceptance boundary

AC-12 remains qualified. The full reactor compiled and verified, but the
repository's named ArchUnit module has no tests and the reactor has no
application/runtime integration tests. No affected in-reactor consumer was
found, so consumer-specific regressions were not applicable. The absent
ArchUnit suite and any required integrated/runtime verification remain explicit
acceptance evidence gaps for independent Codex review and Solution Architect
treatment; this report does not deem them satisfied.

Architecture HCRL 1.1.28/001, Architecture release state `1.1.26`, Software
HCRL 010, its historical findings and its unresolved prior Report timestamp
were not modified. The Software HCRL 010 review still has no Solution Architect
disposition for the earlier RE2/J candidate. The separate operational logging,
InformationUnit, adapter, registry/reference and downstream governed concerns
remain outside this implementation. No new architectural issue was discovered.

No commit, push, merge, tag, publication, deployment, backlog revision,
Architecture change or HCRL closure was performed. The next step is exact-source
Codex review and Solution Architect adjudication, not automatic software
acceptance.

## 9. Exact implementation candidate

- **Base HEAD / tree:** `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb` /
  `1846199b36bbfb07c87746c1367c059698b7120d`.
- **Candidate tree:** `d4b64b4000d5f4bc858368a236691ba6f94e6f64`.
- **Patch:** [verified implementation candidate][candidate].
- **Patch SHA-256:** `489f1cb55ed7951b15001487affb42beae168389aa1a4c6920626b8a38ed063f`.
- **Git state:** real index clean, HEAD unchanged, no commit; the candidate is
  represented by the patch and was checked against the exact base.
- **Compatibility:** no legacy Naming API, bridge layer or backward-compatibility
  mechanism was introduced.

[task]: ../../before/ai-agent-tasks/20261125T014336Z-naming-0003-v000004-implementation-task.md
[backlog]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.004.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.003.md
[dr6]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md#dr-6
[software-hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
[prep]: ../../../../../../../docs/backlog/evidence/0003/000.004-preparation-report.md
[readme]: ../../../../../../../calliope/README.md
[candidate]: ../naming-0003-v000004-implementation-candidate-verified.patch