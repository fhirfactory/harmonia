Report-Produced-At: UNRESOLVED

Event source: The attributable system timestamp for completed Report emission was not exposed at the required precision. The filename prefix and field remain unresolved; no clock reading, filesystem metadata, or commit timestamp has been substituted.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Implementation Report — Calliope Topic/TopicMask

- **Target:** Software candidate `0.1.0`; implementation iteration `008`
- **Task:** [Topic/TopicMask implementation Task](../../before/ai-agent-tasks/UNRESOLVED-topic-topicmask-implementation-task.md)
- **Backlog Item:** [BL-CALLIOPE-OO-01, revision 1](../../../../../../../docs/backlog/calliope-oo-01-topic-models.md)
- **Accepted design:** [DRP-Topic-and-TopicMask.Final.001.001](../../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md)
- **Design acceptance:** [HCRL.004 / DR-10](../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10), with [Iteration 007 provenance](../../../../../review/software-0.1.0/hcrl-software-0.1.0-007.pending.md)
- **HCRL:** [hcrl-software-0.1.0-008.pending.md](../../../../../review/software-0.1.0/hcrl-software-0.1.0-008.pending.md)
- **Branch / HEAD:** `feature/BL-CALLIOPE-OO-01` / `8af315134a1f2e03e75512f66513ef2f7eeb80b8`
- **Runtime:** OpenJDK `21.0.13.1`; Apache Maven `3.9.12`; Linux `amd64`
- **Architecture baseline:** `1.1.26` (unchanged)
- **Resulting repository state:** Uncommitted candidate; no commit, merge, tag, push, deployment, release allocation, or acceptance was performed or inferred.

## 1. Outcome

Implemented the accepted immutable ordered Topic/TopicMask model in Calliope and adapted its directly affected metadata, security-selection, and operational-selection consumers. The candidate uses RE2/J `1.8`, strict canonical parsers, immutable builder snapshots, explicit universal-mask semantics, safe local diagnostics, and executable behavioral, serialization, compatibility, concurrency, and resource-bound tests.

This Report submits the candidate for independent implementation review. It does not claim Solution Architect acceptance, close any DR, establish a first accepted software baseline, or resolve the qualifications inherited from HCRL.004 / DR-10.

## 2. Implementation decisions and boundaries

- `Topic` is an immutable ordered list of completed `TopicEntry` values. Equality, hashing, and natural ordering use the complete ordered qualifier/value path and ignore versions. The accepted versioned representation retains each entry version. Navigation preserves retained versions; the unqualified `getName()` projection remains available but is not a canonical identity.
- Canonical Topic parsing and formatting use the accepted escaped syntax. `TopicBuilder` supports exclusive sequential/indexed modes, generated `N<position>` qualifiers, indexed replacement, continuity checks, reusable state, and independent snapshots. Replacement follows Final.001.001 §2, notwithstanding Backlog AC-3's rejection requirement; see §5.
- `TopicMask` distinguishes ordinary masks from `TopicMask.all()`. Ordinary entries compile qualifier, value, and optional version expressions with RE2/J full-field matching. The evaluator checks depth first, then each position in qualifier/value/version order, short-circuits, and fails closed on matcher exceptions. Canonical parsing and JSON/Java serialization preserve ordinary/universal kind and version-constraint presence.
- Literal helpers quote with RE2/J's `Pattern.quote`; tests cover the actual RE2/J metacharacter-escaping form, including a literal containing `\E`. Topic and mask entry, Topic and mask, and logical JSON reconstruction validate through the same bounded constructors. Ordinary `TopicMask.toString()` returns its accepted canonical expression; evaluator diagnostics never log that expression.
- Candidate-only limits are `MAX_DEPTH=32`, qualifier `64`, value `256`, version `64`, expression `1024`, canonical Topic text `24,704`, and mask text `196,864` Unicode code points. Limits are applied before/after escaping at the relevant boundaries and are tested at/over field, expression, and depth boundaries. These values are proposed software-candidate limits, not an accepted release-specific policy; Solution Architect confirmation remains open under DR-10.
- RE2/J `1.8` is declared directly in `calliope/pom.xml`. No regex fallback, cache, authorization framework, or new logging framework was added. Non-PHI local diagnostics use the existing SLF4J API; they contain only structural categories, indices, and bounded depths. `TopicMask` grants no operational authority; Themis remains independent.
- Directly affected Calliope consumers were adapted: Topic-derived metadata UUID helpers now expose canonical-name projections; metadata diagnostic summaries no longer depend on Topic UUID short IDs; `InformationSecurityContext` builds literal action-value masks and only derives display names from a provably literal path; `OperationalMetadataMask` converts its legacy subject string through `TopicFactory` and fails closed on invalid input. No production source outside `calliope/` was modified by this task.
- `TopicFactory.forInformationUnit(UUID)` preserves the established raw `harmonia.mneme.iu.<hex>` name and leaves the IU UUID independent. Its runtime qualifiers are generated `N<position>` fields, not an asserted fully qualified IU vocabulary. No Hestia/V001 path was edited.
- The current Git status reports `TopicMaskEntryPattern.java` and `TopicMaskType.java` as deleted. Their deletion origin cannot be attributed to this activity because a task-specific pre-edit status was not captured; the candidate has no references to either obsolete basis/type. No restoration or unrelated worktree cleanup was performed.

## 3. Files and source-size review

Production changes are confined to `calliope/pom.xml`, the `Topic`/`TopicMask` naming model, entry/builder/factory code, the new `naming.support` validation/parser/evaluator classes, and the directly affected Calliope information/operational metadata classes listed in §2. Tests were updated or added for those contracts, including `TopicMaskEvaluatorTest` and `TopicMaskBuilderTest`.

All changed/new production Java sources are below the 500-line limit; the largest changed source is `Topic.java` at 428 lines. The established package hierarchy and unrelated Calliope functionality were preserved.

## 4. Acceptance-criteria evidence

| Accepted behavior / criterion | Result | Evidence and qualification |
| --- | --- | --- |
| Topic identity, equality, hashing, and deterministic order | PASS | Tests cover qualifier/value identity, versions excluded from identity/order, case sensitivity, repeated qualifiers, equal unqualified names with distinct paths, and prefix ordering. |
| Hierarchical navigation and display projections | PASS | Tests cover root/parent/ancestor boundaries, strict ancestry, retained versions, and display behavior at depths 1, 2, and greater than 2. |
| Qualifier generation and construction modes | PASS | Sequential/indexed `N<position>` equivalence, out-of-order indexed additions, replacement, gaps, negative/out-of-range indices, builder recovery/reuse, snapshot immutability, and absence of editing operations are tested. |
| Character and structural validation | PASS | Tests cover Unicode code-point handling, supplementary symbols, combining marks, punctuation, internal ASCII spaces, malformed UTF-16, controls, DEL, non-ASCII qualifiers/versions, disallowed Unicode categories, and strict canonical escapes/separators. |
| Candidate resource limits | PASS, limits pending | Exact and exceeded field/expression/depth boundaries and escaped-length round trips are tested. Numeric candidate limits require Solution Architect disposition before being treated as an accepted release policy. |
| Topic canonical/versioned forms | PASS | Non-versioned and versioned round trips cover escaped `.`, `=`, `:`, backslash, absent versions, malformed syntax, and the required versioned `toString()` projection. |
| Universal and ordinary mask semantics | PASS | Tests cover explicit universal equality/hash, null rejection, exact depth, contained trailing elements, positional/no-skip matching, ordinary broad coverage distinct from universal, and ordinary-only accessor failures. |
| RE2/J expressions, literals, and versions | PASS | Tests cover full-field matching, groups, classes, flags, supported dialect rejection, literal metacharacters, independent qualifier/value/version criteria, absent/empty/wildcard version distinctions, and pattern-length bounds. |
| Canonical mask parsing/reconstruction and logical JSON | PASS | Tests cover empty RegEx fields, empty version expression, escaped syntax, malformed/unknown escapes, expression reconstruction, universal-vs-ordinary JSON shape, ambiguity rejection, and JSON/Java serialization round trips. |
| Legacy selector compatibility and fail-closed behavior | QUALIFIED | Exact/Inclusive name-only and qualifier/value vectors, numeric dotted-version adapter behavior, literal name masks, and invalid/blank/wildcard/ambiguous vectors are tested. Historical selector corpora and security-sensitive equivalence are unavailable; `TopicSubscription` case-insensitive behavior was deliberately not migrated. |
| Concurrent evaluation | PASS | Eight workers each performed 500 evaluations against the same immutable compiled mask (4,000 total); matchers are invocation-local. This is functional concurrency evidence, not a latency benchmark. |
| Error handling and diagnostics | PASS locally; reliability open | Injected matcher failure returns false; ListAppender tests confirm conditional TRACE, ERROR, bounded exit metadata, and no candidate/expression payload. Local SLF4J behavior does not establish Log-Satellite delivery, saturation, outage, recovery, or bounded-latency behavior. |
| Integrated consumer and architectural checks | PASS, qualified skips | Focused and full-reactor results are recorded in §6. Ten live-service integration tests were skipped by their environment gates and remain unproven here. |

## 5. Compatibility, migration, and authority qualifications

The accepted API intentionally removes candidate software behavior that conflicts with Final.001.001. In particular, runtime Topic UUID accessors/identity are removed; `getCanonicalName()` is now a canonical string rather than the former map; UUID-taking `Topic`/`TopicBuilder` identity constructors and the old TopicMask dual-basis/UUID API are not retained. The deprecated five-position Topic constructor and positional projection accessors remain where meaningful. Metadata UUID-derived accessors were renamed to canonical-name projections. Old UUID-backed references, registry uniqueness/aliases, persisted Topic/Mask selectors, and old JSON/Java-serialized forms were not migrated or replayed.

The accepted design and Backlog revision 1 are not silently reconciled by this Report. Backlog AC-2 requires preservation of the old public/serialization/UUID contracts, while the accepted Java API and Task require the new model; Backlog AC-3 requires duplicate-index rejection, while Final.001.001 §2 requires indexed replacement. The Task directs implementation against the accepted design and the Backlog was not edited, but the affected AC statuses and migration conditions remain for Solution Architect review before acceptance.

Other open qualifications carried from HCRL.004 / DR-10 are: historical selector equivalence and no broadened security-sensitive selection; the canonical fully qualified IU qualifier vocabulary and boundary mappings; the approved release-specific resource/RE2/J safeguards; shared logging integration, performance, and normal/failure reliability evidence; and any authorized Architecture Continuum reconciliation. No ADD, Continuum, release authority, deployment policy, or Architecture release increment was created here.

The Task-specific pre-edit `git status` was not captured by this agent. The entry branch/HEAD and 42-file dirty-state reference were taken from the immediately preceding Iteration 007 Report; all edit operations in this session were path-scoped to `calliope/` and Iteration 008, no Git operation changed the index/branch, and the final status confirms the same branch and HEAD. The exact baseline limitation is retained for Solution Architect treatment; the separate V001 paths were not edited.

Task-Issued-At and Report-Produced-At remain `UNRESOLVED` because their attributable event timestamps were not exposed. Their filename prefixes remain unresolved; no filesystem time or unrelated event time has been substituted.

## 6. Verification actually executed

All commands used offline Maven `3.9.12` and OpenJDK `21.0.13.1` from `/usr/lib/jvm/java-21-openjdk-amd64`. Maven invocations were bounded with `timeout`; no command exceeded the repository's 10-minute maximum.

1. `mvn -B -o -ntp -Dstyle.color=never test -pl calliope -am` — **PASS**, 271 tests, 0 failures, 0 errors, 0 skips across four reactor modules.
2. `mvn -B -o -ntp -Dstyle.color=never test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` — **PASS**, 90 architecture tests, 0 failures, errors, or skips.
3. `mvn -B -o -ntp -Dstyle.color=never test -pl agora/agora-service -am` — **PASS**, Agora Service 15 tests, 0 failures, errors, or skips; all 12 reactor modules succeeded.
4. `mvn -B -o -ntp -Dstyle.color=never test -pl energeia/erga,pylai/pylai-mllp-out -am` — **PASS**, Erga 98 tests, 0 failures, errors, or skips; all 14 reactor modules succeeded, including Pylai MLLP Base and Outbound.
5. `mvn -B -o -ntp -Dstyle.color=never test` — **PASS**, all 50 reactor modules succeeded. Class-level Surefire lines aggregate to 336 classes and 1,765 test instances, with 0 failures, 0 errors, and 10 skips. Nine skips are in the Hestia `DistributedAuthoritativeDockerPathTest` M24 (2), mTLS (3), public-FHIR-separation (1), and Path-A (3) groups; one skip is in the `petasos-artemis` module. These live-service/broker cases remain unproven in this run.
6. `git diff --check` — **PASS**; no whitespace errors. No JaCoCo/line-coverage report or application deployment run was produced.

During development, a new strict HL7 composite-trigger test exposed fallback on an incomplete `ADT^` value; the adapter was corrected to reject missing message/trigger components. A compile error from a missing `TopicMaskEntry` import was also corrected. The final Calliope, consumer, architecture, and full-reactor runs above pass; no known final implementation/test failure remains.

The final build emitted non-fatal deprecation warnings for API use in `ErgonPayloadFhirConverter` and `InformationUnitIdentifierTest`; these did not fail compilation or tests and are retained for independent review alongside the explicitly preserved legacy Topic projections.

## 7. Review request and limits

Please review the exact current uncommitted integrated candidate, with the `calliope/` subtree as the Iteration 008 source delta and the V001/other pre-existing changes left untouched. Record per-criterion dispositions, resolve the compatibility/migration and release-specific qualifications above, and do not infer acceptance from these passing tests. No software release, accepted predecessor, or commit/push authority is asserted.
