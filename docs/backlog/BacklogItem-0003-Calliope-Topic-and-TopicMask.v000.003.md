<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Backlog Item: 0003 — Calliope Naming foundation

**Backlog Item ID:** 0003

**Backlog Item Revision:** 000.003

**Standing:** Accepted with qualifications by Mark Hunter, Solution Architect.
A separately issued bounded assignment to Junie supplies implementation authority.

## Overview

### High-Level Synopsis of Effort

Establish the immutable Naming models for the first Software 0.1.0 increment
using the qualified accepted Topic and TopicMask runtime design.

### High-Level Synopsis of Activities

Implement the accepted value objects, builders, canonical parsers, matching
and logical reconstruction in Calliope. Verify behaviour, limits, conformance
and documentation with focused tests and evidence for the integrated candidate.

### High-Level Synopsis of Outcome

Calliope exposes deterministic names, reusable selectors and lossless
representations of the applicable ordered data. Consumers can use Naming
primitives while retaining responsibility for their own feature and policy rules.

### High-Level Synopsis of Acceptance Criteria

Demonstrate identity, construction, matching, interchange, defensive behaviour,
bounded evaluation and safe concurrent reuse. Establish object and documentation
quality and record the Solution Architect's disposition of open prerequisites.

## Context

Start on `feature/BL-0003-Calliope-Topic-and-TopicMask` from accepted Architecture
**1.1.26** and the Software **`0.1.0-SNAPSHOT`** foundation at
`7d5c195ad7782ab0b39032308a2eb8f72c678da5`. The accepted reset supersedes the
exploratory Naming implementation and its compatibility obligations.
Revision [000.002][prior] retains its historical assignment and review standing.
Governed architectural references and applicable persisted data retain their
own standing. [Software HCRL 009, DR-3][reset] · [Software 0.1.0 roadmap][roadmap]

Production scope is exclusively
`calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/`, including
justified subordinate packages. Place corresponding tests and fixtures under
`calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/` and
Calliope test resources, as applicable. The Calliope-only dependency allowance
specified below is approved for the subsequent bounded assignment.

Excluded from this increment are InformationUnit, InformationSecurityContext,
OperationalMetadataMask, Pragma, Pragmatosis, PetasosParcel, Mneme, Mnemosyne,
Ponos, Ergon and all other downstream consumers. Historical Java compatibility
adapters, obsolete constructors and serializers, and migration functionality
are excluded. Logging collection, buffering and delivery belong to the logging
framework. Feature-level subscription execution and policy evaluation belong
to their respective owners. [Final.001.001 §§6–7][design]

## Associated Requirements

| Authority | Applicable obligation and standing |
| --- | --- |
| [Final.001.001][design] · [Software HCRL 004, DR-10][design-acceptance] | Qualified acceptance covers the runtime model, matching, construction, canonical representations and §8 Java API. Apply the Naming-specific §10 behaviours and track §9 qualifications. |
| [Calliope-Models][calliope] · [Topic][topic] · [TopicMask][mask] · [Architecture 1.1.28/001][naming-hcrl] | Calliope owns the shared model and neutral matching semantics. Final.001.001 governs standalone implementation under DR-6. DR-1–DR-5 resolve the proposed Continuum correspondence, with formal incorporation still governed separately. |
| [Canonical identity and naming governance][naming-governance] · [AX-04, AX-07, AX-10, AX-11, AX-14 and AX-17][axioms] | Preserve distinct identity and version domains, independent authorisation, bounded evaluation and explicit treatment of discrepancies. Keep regex and serialization machinery subordinate to Harmonia semantics. |
| [NFR-099][nfr] · [Final.001.001 §5][interchange] | Prefer XML where practical for structured interchange. Preserve the complete logical contract with any supported encoding. Justify any JSON encoding by an applicable interoperability contract. |
| [Implementation Guide][guide] · [Software Development Praxis][software] | Demonstrate cohesive object design, the 95/5 engineering approach, educational documentation, and applicable integrated verification. |

### Architectural correspondence and retained qualifications

| Matter | Finding and required treatment |
| --- | --- |
| **Common Naming model** | DR-1 and DR-2 accept the qualified non-versioned Topic path and structural TopicMask with positional and version-aware matching and explicit universality. Architecture 1.1.26 still carries the earlier UUID, sequencing and selector wording. The accepted correspondence remains governed until incorporated. The standalone model follows Final.001.001, with legacy data obligations preserved at applicable boundaries. [Architecture 1.1.28/001][naming-hcrl] · [Final.001.001][design] |
| **Identity and downstream integration** | DR-3 preserves architectural element identity separately from runtime Topic path identity. DR-4 preserves the IU UUID, its unqualified Topic designation and shared selector semantics. Defer exact IU qualifier vocabulary, specialised reference mappings and downstream integration to their appropriate backlog items. Governed registry, alias, persisted-reference and selector migration follow their own authority and require selection-equivalence evidence where applicable. [Architecture 1.1.28/001][naming-hcrl] · [IU identity contract][iu] |
| **Retained input and release sequence** | DR-5 approves the digest-matched two-path locator correction for the Architecture transition that incorporates it. Live canonical validation retains the known missing-locator failure until it is treated. Under DR-6, standalone Naming can start before completion and publication of Architecture 1.1.27 and 1.1.28 once this item and its applicable prerequisites are accepted and the discrepancies remain governed. Dependent acceptance still requires incorporated and verified architecture. Keep the 1.1.27/002 amendment and 1.1.28/001 reconciliation separate. [DR-6][naming-start] · [Incorporation readiness][naming-readiness] · [Pending amendment][archunit-amendment] |
| **Implementation qualifications** | Final.001.001 §6 and §9 retain the dependency, limit, logging and evidence qualifications. Apply the accepted measures below within the standalone assignment. The separate Themis API concern and shared logging integration retain their existing standing. [Final.001.001 §6][limits] · [F-01 reconciliation][reconciliation] |

## Expected / Associated Activity

### Approved dependency scope and resource limits

Include only two dependency declarations in the authorised Calliope build
change. Add direct `com.google.re2j:re2j:1.8` for matching and test-scoped
`org.junit.jupiter:junit-jupiter:5.10.2` for the Naming tests in `calliope/pom.xml`.
Keep the inherited Java 21 compiler and Surefire configuration. RE2/J 1.8 is
available in the local Maven cache and exposes `Pattern.programSize()` as a
public API. Use RE2/J directly. The assignment includes no parent POM,
module graph, plugin or logging dependency change. [Calliope POM][pom] · [RE2/J 1.8][re2j]

| Limit | Approved maximum |
| --- | --- |
| Topic or ordinary mask depth | **16** entries. Indexed Topic assembly accepts positions 0–15. The universal mask has no entries. |
| Decoded TopicEntry fields | **64** qualifier, **256** value and **64** present version Unicode code points per entry. |
| Decoded ordinary mask expressions | **512** Unicode code points per qualifier, value or version expression. Apply this separately to the actual quoted expression from a literal helper. |
| Canonical text | **32 KiB** for either Topic canonical form and **128 KiB** for an ordinary mask expression, counted as UTF-8 bytes including escapes and separators. |
| Structured interchange | **256 KiB** in UTF-8 for one Topic or TopicMask XML record, including markup and encoding overhead. |
| Pre-compilation complexity | **16** nested regex groups, **256** as the largest explicit repetition bound, and **4,096** code-point units of conservative estimated expansion per expression. Apply the qualified safeguard below. |
| Compiled expression | **1,024** RE2/J program instructions per expression, checked with `Pattern.programSize()` before retaining it. |

Validate Unicode well-formedness and count decoded code points before outer
escaping. Enforce bounds before growing builder storage, materialising oversized
input or compiling expressions. A sparse index above 15 fails before allocation.
Prefer bounded input and the native `Pattern.programSize()` check. Keep any
additional pre-compilation validation to simple, justified and independently
testable checks. Use lexical limits on active group depth and explicit counted
repetitions and a conservative expansion bound over the bounded expression.
A simple upper bound can multiply the source length by the counted-repeat
multipliers using saturating arithmetic. Recognise escaped, class and quoted
literal text so it remains literal. Use RE2/J as the sole syntax authority and
matching engine. The approved qualification excludes constructing a second
regex parser, expression tree or expanded pattern to estimate operand costs.
The native post-compilation instruction count limits evaluation work and
retained state.

The canonical byte ceilings cover the maximum legal escaped shapes. Each decoded
code point can require up to four UTF-8 bytes, and the four escaped ASCII
characters require two bytes each. At depth 16 the largest Topic is at most
20,528 bytes with separators and the largest mask expression at most 98,352 bytes.
For the accepted UTF-8 XML codec, use ordered entries, explicit mask kind and
presence, and reversible UTF-8 Base64 for present regex fields to preserve raw
expression code points, including line endings. Bound the record before decoding
and reject external resolution, DTDs and unknown structure. The XML envelope cap
leaves room for the encoded fields and markup. This encoding applies at the
Naming interchange boundary. [Final.001.001 §5][interchange] · [NFR-099][nfr]

Compile once during immutable entry construction. Check depth before matching,
then short circuit on the first failed field. Share compiled patterns and use
invocation-local matcher state. Limit an ordinary evaluation to at most 48
full-field checks over bounded fields. Exclude Java RegEx fallback and global
match-result caching. Use a small deterministic JUnit workload for valid
boundary construction, over-budget rejection and maximum-depth match and late
mismatch. Record JDK, sample counts and observed elapsed times. Use bounded
test execution and the structural limits as the reproducible gates. [Final.001.001 §6][limits]

### Accepted standalone diagnostics qualification

- At construction, parsing and reconstruction boundaries, provide a documented
  `IllegalArgumentException` subtype with a stable category and safe context.
  Distinguish missing or invalid fields, malformed text or records, rejected
  expressions and exceeded limits. Supply the operation, field kind and zero-based
  entry index when available. Validate before mutation and preserve the existing
  builder state and snapshots on failure. Callers can handle the category directly.
- Keep `TopicMask.matches(Topic)` synchronous and Boolean. An unexpected evaluation
  error yields `false` with a distinct internal evaluation-error category.
  Keep the diagnostic handoff synchronous, bounded and local to Naming, with
  package-local test capture at the comparison exit. Cover depth, qualifier,
  value, required-version absence, version mismatch, evaluation failure and
  successful match. Leave the index
  absent before field evaluation and use the last checked position for ordinary
  success. Diagnostic failure must preserve the fail-closed outcome. Callers
  handle `false` as a non-match. The diagnostic handoff distinguishes evaluation
  failure for the future application logger.
- Restrict diagnostics to categories and bounded metadata such as the operation,
  depth, index and field kind. Exclude raw field values, paths, regex text and
  engine messages or causes that expose them. Verify safe handling-boundary ERROR
  intent and conditional TRACE eligibility using local capture. The reset
  checkpoint lacks an application logger implementation.
- The accepted standalone deferral covers actual ERROR and TRACE LogEvent
  emission through the Harmonia application logger, live console independence,
  buffering, backpressure, transport, receipt acknowledgement, reliable ERROR
  delivery, recovery and latency evidence. Carry these obligations to the
  operational logging backlog. Global mutable diagnostic state, asynchronous
  processing, queues, buffering and logging infrastructure are outside Naming.
  [Final.001.001 §6][limits] · [Implementation Guide][guide]

### Accepted verification scope and qualification

- Run JUnit functional and structural tests for the approved API, immutable
  snapshots and the Naming ownership boundary. Cover malformed and adverse
  input, every applicable limit, logical reconstruction and round trips through
  the actual XML reader/writer, error and diagnostic failure, and concurrent
  reuse of a shared mask. Map results to AC-1–AC-13 and Final.001.001 §10.
- Run `mvn -B -ntp -pl calliope -am test` and `mvn -B -ntp verify` on JDK 21
  against the exact candidate. Record module and test counts, commands,
  environment and results. Show that Naming tests actually ran. Record the
  applicable solution-wide regression, integration and conformance scenarios
  exercised and request explicit disposition for required missing coverage under
  the Software Development Praxis. A successful reactor build establishes its
  recorded build and test scope. [Software Development Praxis §6][software-evidence]
- The Solution Architect accepts structural JUnit checks, production and test
  diff review, and dependency-boundary verification as interim evidence for this
  standalone increment. The exploratory ArchUnit suite is absent. Report it as unexecuted
  and retain this scoped qualification while 1.1.27/002 is pending.
  Keep the obsolete suite retired and preserve the separate amendment review.
  [Software HCRL 009, DR-2][reset-verification] · [Pending amendment][archunit-amendment]
- Obtain an independent Codex implementation review of Junie's exact candidate
  before submitting for implementation acceptance. Review the accepted API and
  Naming boundary, defensive paths, test evidence, POM scope and retained
  qualifications. Record review findings and evidence for their disposition or
  correction. [Implementation Guide][guide] · [Software Development Praxis §6][software-evidence]

### Implementation handoff

1. Reference this accepted revision and exact starting commit in the formal
   implementation assignment. Carry the approved POM allowance, dependency and
   limit profile, XML representation, diagnostics deferral and interim verification
   qualification into that assignment.
   Retain the accepted R1–R6 dispositions and identify dependent work requiring
   separate authority. [Final.001.001 §§6, 9–10][limits] · [DR-6][naming-start]
2. Implement `Topic`, `TopicEntry`, `TopicBuilder`, `TopicMask`, `TopicMaskEntry`
   and `TopicMaskBuilder` with the accepted Java API. Add parsing, validation,
   matching and interchange collaborators where their responsibilities justify
   them. Preserve the immutable model boundary and the ordered, format-independent
   logical records. Implement the approved XML codec and document its
   deterministic reconstruction and failures.
   [Final.001.001 §§5, 8][design]
3. Execute the approved verification scope and submit the exact candidate for
   independent review. Record the commands, environment, outcomes, limitations,
   dispositions and AC-1–AC-13 correspondence in the Implementation Report and
   applicable software HCRL.
   [Implementation Guide][guide] · [Software Development Praxis §6][software-evidence]

## Expected Outcomes

- Immutable, documented Calliope Naming objects with deterministic construction,
  canonical forms and neutral positional matching.
- Validated interchange that preserves complete ordered Topic data and the
  logical kinds and constraints of TopicMask.
- Reproducible component and solution-wide evidence for the exact candidate,
  including relevant failures, limits and architectural qualifications.

## Acceptance Criteria

| Criterion | Observable result | Verification / expected evidence |
| --- | --- | --- |
| AC-1 · Identity, ordering and hierarchy | Topic equality, hashing and natural ordering use the case-sensitive qualified path, excluding versions. Ordering is qualifier then value per position, with proper prefixes first. TopicMask equality and hashing use logical kind and, for ordinary masks, ordered original expressions, version-constraint presence and containment. | Test distinct paths sharing an unqualified name; equal Topics with differing versions in hash and sorted collections; repeated qualifiers, case, spaces and prefix ordering; root, null and strict ancestry; out-of-range depths and version-preserving ancestor/parent. Compare independently built masks of each kind. |
| AC-2 · Names and canonical parsing | Name accessors, relative/friendly/display forms, canonical names and `toString()` follow §3. Strict canonical parsing decodes the four structural escapes and requires each completed field. | Assert all projections across depths, escaped `.`, `=`, `:` and backslash, and significant case and text. Reject omitted fields, empty segments, misplaced or extra separators, invalid escape syntax and version segments in the unversioned parser. |
| AC-3 · Versions | Topic input `null` and empty version mean absence, expressed by omission. Strict Topic parsing rejects an explicit empty version segment. A mask distinguishes absent, present-empty, wildcard and other version expressions and enforces presence when constrained. | Compare the actual stored ordered versions and emitted canonical forms. Exercise two- and three-argument mask factories and match against both absent and present versions, including a mask that distinguishes two equal Topics. Verify an explicit empty mask version expression round-trips. |
| AC-4 · Mask evaluation | Ordinary masks have at least one criterion. Check depth before regex, then qualifier, value and version at each position. Exact depth is the default. Containment permits additional trailing levels. Explicit universality matches non-null Topics and rejects ordinary-only accessors. | Test full-field RE2/J, case sensitivity and supported flags, position and first-mismatch short circuit, exact and trailing-depth cases, and both kinds against null. Verify literal quoting and helper equivalence, empty qualifier/value regex behaviour, explicit universal construction, and a broad ordinary mask that retains its kind. Confirm no Java RegEx fallback. |
| AC-5 · Builders and validation | The first successful Topic add fixes the construction mode. Indexed input can arrive out of order, requires contiguous nonnegative positions at build and replaces an occupied index without growth. Generated `N<position>` qualifiers use final positions. Both builders validate before mutation and publish defensive snapshots. | Test both indexed overloads, failed first adds, mode mixing, gaps and recovery, invalid replacement preserving the prior input, failed builds, builder reuse and immutable entry views. Compare generated and explicit qualifiers in ordering, identity, matching and interchange. Verify that empty ordinary masks fail construction. |
| AC-6 · Serialization and deserialization | The supported Naming codec preserves ordered Topic records, mask kind, containment where applicable and original expressions, including absent versus present-empty constraints. Decoding validates fields and recompiles expressions. The codec's format stays outside domain semantics. | Exercise the actual XML reader and writer in both directions with independent expected fixtures. Verify expression encoding round-trips raw code points and distinguishes absent from present-empty fields. Record any separately authorised additional encoding and test it equivalently. Reject universal payloads carrying ordinary data, missing or ambiguous kind, and empty ordinary criteria. |
| AC-7 · Lossless reconstruction | Unversioned canonical output reconstructs Topic identity. Versioned canonical text and logical interchange reconstruct every ordered field. An ordinary mask's canonical expression plus containment reconstructs its structure and behaviour. | Compare raw field records, re-emitted canonical text and matching outcomes after decoding. Include RegEx groups and classes, scoped flags, empty fields, escaped delimiters and literal backslashes. Check explicit universal logical reconstruction separately. |
| AC-8 · Malformed and adversarial input | Invalid input fails predictably without trimming, normalisation, fallback or accidental universality. Unexpected evaluation errors fail closed and diagnostics preserve the outcome. | Exercise nulls, blank or missing fields, malformed Unicode, disallowed code-point categories and whitespace, malformed canonical or structured input, invalid or unsupported RE2/J syntax and the rejected `@all` token. Check preservation of composed/decomposed and supplementary-plane text. For XML, test rejection of unsafe entity processing and malformed or ambiguous payloads. Assert caller-visible validation categories and safe handling-boundary ERROR intent and conditional TRACE eligibility under the approved diagnostics qualification. Inject evaluation and diagnostic failures with local capture. |
| AC-9 · Bounds and resource use | Construction, parsing and logical decoding enforce the approved depth, field, expression and serialized byte limits. Compilation and evaluation use explicit bounds, native RE2/J program-size checks and only simple, justified pre-compilation safeguards. | Exercise below, at and above applicable limits, including sparse indices, supplementary code points, escaped fields and quoted literal expressions. Test encoded byte ceilings, lexical guards and the compiled instruction cap independently. Verify rejection of excessive counted repetition before compilation and preservation of literal syntax. Verify construction-time compilation, no global match-result cache, and bounded evaluation with deterministic boundary workloads. Record observed timings, safe diagnostic categories and exit indices without raw fields, expressions or protected data. |
| AC-10 · Concurrency | Completed Topics and masks are immutable. Concurrent reuse shares immutable compiled patterns and uses invocation-local evaluation state. Builders are reusable with caller-controlled confinement. | Repeatedly match with one mask from multiple threads using varied depth/version cases and independently known outcomes. Verify immutable read access and isolation of built snapshots after later builder operations. |
| AC-11 · Structural coherence | Implementation and tests follow the Naming boundary. Collaborators have cohesive responsibilities. Dependency choices follow the accepted design and separately approved build scope. | Review the diff and a class/responsibility inventory. Verify direct pinned RE2/J, the test-only JUnit dependency, independence from Themis and higher layers, and absence of compatibility, migration or consumer additions. Count full production Java files including documentation and declarations. Obtain a per-file Solution Architect exception for any file exceeding 500 physical lines. |
| AC-12 · Implementation Guide | The implementation handles realistic unhappy paths with defined outcomes and recoverable builder state. Evidence distinguishes functional, defensive, conformance and integrated verification. | Review the §10 test mapping and 95/5 handling of invalid construction, validation, parser, evaluation and diagnostics failures. Trace AC-1–AC-13 to the exact candidate in the Report and HCRL. Record component and reactor results, independent Codex review and the applicable treatment of required solution-wide and architectural checks. Retain the accepted scoped deferral for shared logger evidence, including bounded caller latency, console independence, outage, saturation, recovery, receipt and reliable ERROR delivery. |
| AC-13 · Educational documentation | Classes, interfaces, public and protected operations, and significant internal logic explain their purpose, constraints and failures. | Review the accepted API, immutability and builder usage, canonical grammar, mask kinds and version semantics, XML encoding, RE2/J features, limit values and failure behaviour. Verify the documentation tracks actual code and states the applicable downstream and diagnostics qualifications. |

## Solution Architect acceptance

Mark Hunter accepted this work specification for Software `0.1.0-SNAPSHOT`
against Architecture 1.1.26 on `feature/BL-0003-Calliope-Topic-and-TopicMask`.
The [reviewed input][reviewed-input] preserves the exact pre-acceptance Markdown
at SHA-256 `c43b70a93aa6e51c0ae6d2b484e5bee76a0c178ec03716576ecea0321d926e5d`.
The accepted qualifications from the Solution Architect's instruction are as follows.

1. **RE2/J safeguards.** Use the accepted dependency and resource limits.
   Prefer explicit input limits and native compiled-program safeguards. Keep
   additional pre-compilation validation simple, justified and independently
   testable. Exclude a second regex parser or engine for complexity estimation.
   AC-9 records the qualified verification requirement.
2. **Diagnostics.** Keep diagnostics synchronous, bounded and local to Naming.
   Preserve meaningful validation failures, safe diagnostic categories and
   fail-closed matching. Exclude global mutable diagnostic state, asynchronous
   processing, queues, buffering and logging infrastructure. Operational logging
   integration and delivery guarantees remain deferred to their respective items.

The acceptance includes the stated Calliope POM allowance and the interim
architectural-verification qualification for this standalone increment.
The governed Architecture work and dependent acceptance requirements retain
their recorded standing under DR-6. [Software Development Praxis][software] · [DR-6][naming-start]

[prior]: BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md
[reviewed-input]: evidence/0003/000.003-reviewed.md.gz
[roadmap]: ../implementation/harmonia-convergence-runtime-integration-plan.md#software-010-core-model-foundation
[reset]: ../../enterprise-architecture/design/register/software-0.1.0/hcrl-software-0.1.0-009.complete.md#dr-3
[reset-verification]: ../../enterprise-architecture/design/register/software-0.1.0/hcrl-software-0.1.0-009.complete.md#dr-2
[design]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[design-acceptance]: ../../enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[interchange]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md#5-canonical-mask-expressions-and-logical-interchange
[limits]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md#6-resource-limits-concurrency-and-diagnostics
[reconciliation]: ../../enterprise-architecture/design/evidence/software-0.1.0/004/after/F-01-Architecture-Reconciliation.md
[naming-hcrl]: ../../enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md
[naming-start]: ../../enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md#dr-6
[naming-readiness]: ../../enterprise-architecture/design/evidence/1.1.28/001/after/ai-agent-reports/20261124T062949Z-final-governance-readiness.md
[naming-governance]: ../../enterprise-architecture/governance/canonical-identity-and-naming.md#architectural-identity
[iu]: ../../enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.002.md#2-identity-and-distinct-revisions
[calliope]: ../../enterprise-architecture/continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md#architectural-content
[topic]: ../../enterprise-architecture/continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md#architectural-content
[mask]: ../../enterprise-architecture/continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md#architectural-content
[axioms]: ../../enterprise-architecture/governance/architectural-axioms.md
[nfr]: ../../enterprise-architecture/continuum/motivation/principle/6973441f2d62448188af9bfce5ad9d9f.md#architectural-content
[pom]: ../../calliope/pom.xml
[re2j]: https://github.com/google/re2j/blob/re2j-1.8/README.md
[archunit-amendment]: ../../enterprise-architecture/design/review/1.1.27/hcrl-1.1.27-002.pending.md
[guide]: ../implementation/Harmonia-Implementation-Guide-Philosophy.md
[software]: ../../enterprise-architecture/governance/software-development-praxis.md
[software-evidence]: ../../enterprise-architecture/governance/software-development-praxis.md#6-solution-wide-candidate-submission-and-evidence
