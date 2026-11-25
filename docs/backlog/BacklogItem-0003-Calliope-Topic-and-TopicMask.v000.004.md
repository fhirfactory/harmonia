<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Backlog Item: 0003 — Calliope Naming foundation

**Backlog Item ID:** 0003

**Backlog Item Revision:** 000.004

**Standing:** Accepted by Solution Architect. Implementation requires a
separately authorised assignment against the accepted revision.

## Overview

### High-Level Synopsis of Effort

Bring Calliope's Topic and TopicMask model and its affected consumers into
conformance with the accepted Final.003 Naming design.

### High-Level Synopsis of Activities

Preserve the Topic value and builder contracts. Implement the six wildcard
forms, version constraints, strict canonical and XML interchange, and the
supported Java and diagnostic boundaries. Refactor affected dependencies and
verify the exact integrated candidate.

### High-Level Synopsis of Outcome

Calliope provides immutable names and bounded, reusable selectors. A single
interchange contract preserves original field text, ordering, mask kind and
constraint presence. Consumers retain their feature and policy responsibilities.

### High-Level Synopsis of Acceptance Criteria

Demonstrate identity, construction and recovery, matching, strict and lossless
interchange, safe bounded evaluation, and API and dependency conformance.
Provide attributable component, affected-consumer and solution-wide evidence
for independent review.

## Context

The development target is Software **`0.1.0-SNAPSHOT`** on
`feature/BL-0003-Calliope-Topic-and-TopicMask`. Architecture **1.1.26** is the
current incorporated baseline. The accepted software foundation is the reset
checkpoint `7d5c195ad7782ab0b39032308a2eb8f72c678da5`. The worktree's RE2/J
candidate remains unaccepted and has its own exact review record. Revision
[000.003][prior] retains its accepted standing and governs that historical
assignment. [Software HCRL 009][foundation] · [Software HCRL 010][software-review]

Final.003 is accepted design authority for the R1/R2 Naming scope. Architecture
1.1.28/001 DR-8 accepts the replacement Continuum ADD, DR-9 accepts the
pre-1.0.0 Software Development Praxis amendment, and DR-10 qualifies their
transition. The live Praxis and Continuum are at the incorporated baseline.
Application and verification of 1.1.28 await the separate accepted 1.1.27
predecessor. Earlier live Continuum wording about runtime Topic UUIDs or regex
selection is a recorded discrepancy covered by the accepted reconciliation.
[Final.003][design] · [DR-8][dr8] · [DR-9][dr9] · [DR-10][dr10]

The Calliope production boundary is
`calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/`, with
corresponding tests and fixtures. The eventual assignment covers coherent
refactoring of evidenced direct and indirect consumers of changed Naming
contracts. New IU, Pragma, Petasos, subscription, routing, registry, persistence
and service functionality are outside this scope. So are R3 representation-content
matching and operational logging infrastructure. Preserve independently governed
IU identity, registry, alias, persistence and reference obligations where applicable.
[Final.003 §7][consumers] · [DR-4][dr4]

### Implementation and acceptance prerequisites

DR-6 permits a standalone Naming start before the Architecture releases when
the accepted design supplies an unambiguous contract, the Solution Architect
has accepted this Backlog Item revision, applicable prerequisites and outstanding
decisions have explicit dispositions, architectural discrepancies stay recorded
and governed, and software acceptance claims only conformance established by
evidence. A bounded assignment must identify the accepted revision, exact
starting source and candidate treatment, applicable scope and verification.
Dependent software acceptance requires the relevant Architecture reconciliation
to be incorporated and verified, plus Solution Architect disposition of the
exact software candidate and any required evidence gaps. [DR-6][dr6] · [Software Development Praxis][praxis]

Retain the DR-5 input-locator correction and its DR-10 placement decision in
the Architecture reviews. The named exploratory ArchUnit suite is absent.
Carry forward the standalone interim structural qualification within its
recorded scope while 1.1.27/002 is pending. Escalate any changed or unsatisfied
applicable gate. Qualified IU naming and reference mappings, the separate
Themis API module matter and shared logging delivery remain in their own
governed scopes. [DR-5][dr5] · [DR-10][dr10] · [ArchUnit amendment][archunit] · [Preparation report][report]

## Associated Requirements

| Authority | Applicable obligation and standing |
| --- | --- |
| [Final.003][design] · [Architecture 1.1.28/001 DR-8][dr8] | Accepted R1/R2 runtime Topic identity, TopicMask selection, Java API, strict interchange and diagnostic boundary. Use the accepted design for standalone Naming under DR-6. |
| [Calliope-Models][calliope] · [Topic][topic] · [TopicMask][mask] · [TopicIdentityStable correspondence][dr3] | Preserve the common library responsibility and existing architectural identities. A qualified runtime path carries Topic identity. The corresponding element and relationship refinements await incorporation. |
| [Architecture naming governance][naming-governance] · [Final.003 §§1 and 7][identity] · [IU identity contract][iu] | Keep architectural element identifiers, runtime Topic identity and IU identity distinct. Where authority requires a mapping, use the owning contract and adjudicate missing vocabulary or mappings. |
| [Architectural Axioms][axioms] · [Final.003 §9][architecture] | AX-04 and AX-12 govern bounded platform operations and internal mechanics. AX-06–AX-08 govern neutral selection and safe evidence. AX-09–AX-11 govern bounded transient work and failure. AX-14, AX-15 and AX-17 govern semantic distinctions, uncertainty and authority. |
| [NFR-099][nfr] · [Final.003 §5][xml] | Use the accepted strict XML codec for structured records, preserving original data and mask kind. |
| [Final.003 §7][coherence] · [DR-9][dr9] · [Software Development Praxis §6][praxis-evidence] · [Implementation Guide][guide] | Apply the accepted pre-1.0.0 coherence principle, defensive engineering, educational documentation and integrated verification. |

## Expected / Associated Activity

### Topic construction and presentation

Preserve immutable ordered TopicEntry records and version-independent Topic
identity, equality, hashing and ordering. The non-versioned canonical name
supplies runtime Topic identity. TopicEntry equality and hashing retain the
complete record, including its optional version. Validate completed qualifiers
and present versions as nonempty printable ASCII without leading or trailing
whitespace, allowing internal spaces. At Topic assembly, generate
`N<position>` for null or empty qualifier input. Treat null and empty Topic
version input as absence.
Require a nonempty, well-formed Unicode value with letters, numbers,
punctuation, symbols, combining marks and internal U+0020. Reject other space
separators, line/paragraph separators, controls, format characters, surrogates,
private-use and unassigned code points. Preserve original valid text and
opaque versions. Strict completed-record reconstruction always requires explicit
valid qualifiers and rejects explicit empty Topic version segments or elements.
[Final.003 §§1–3][identity] · [Final.003 §5][xml]

Support sequential append and explicit zero-based indexed assembly. The first
successful add selects the mode. Indexed input may arrive out of order, can
replace an occupied position without changing depth and must be contiguous at
build. Validate additions and replacements before changing state. Failed
operations preserve builder state and published snapshots. Parent and ancestor
navigation preserve retained versions and compare literal whole-entry paths for
strict ancestry. [Final.003 §§2–3][construction]

### TopicMask selection

Validate and classify each qualifier and value pattern once and retain its
original text and an immutable internal matching description. Match the complete
decoded field case-sensitively, preserving original Unicode and treating
ordinary punctuation literally. Each pattern fits one of six forms.

| Raw pattern | Meaning |
| --- | --- |
| `Patient` | Exact text |
| `Patient*` | Prefix |
| `*Patient` | Suffix |
| `*Patient*` | Contains a contiguous literal |
| `Patient*Record` | Prefix and suffix with no overlap |
| `*` | Unrestricted valid field text |

Decode literal asterisks and backslashes from `\*` and `\\` in the raw pattern
from left to right. A backslash before any other character or at the end is
literal. Reject null and unsupported unescaped wildcard placements, including
adjacent wildcards and two wildcards outside the contains form. An explicitly
empty pattern is valid exact text and cannot match a completed field. Literal
helpers validate completed fields before escaping them. Unrestricted helpers
use raw `*` and `any()` leaves version unconstrained. Use bounded equality,
prefix, suffix and substring operations. Exclude RE2/J, Java regex,
general-purpose regex parsers, wildcard-to-regex translation, recursive matching
and backtracking. [Final.003 §4][patterns]

An ordinary mask has one or more criteria, with exact depth by default and
optional inclusion of trailing levels. Check depth before testing ordered
positions. At each position evaluate qualifier, value and then version, stopping
at the first mismatch. Explicit universal masks match every non-null Topic,
have an empty entry view and reject the ordinary-only canonical and containment
accessors. A broad ordinary mask retains its kind. Both kinds return false for
a null candidate. Mask equality and hashing use logical kind and, for ordinary
masks, containment, ordered original patterns and raw constraint text and
presence. Selection conveys only a Boolean result. Themis owns policy
evaluation and Kleio owns authoritative audit capture.
[Final.003 §§4 and 7][matching] · [Consumer boundaries][consumers]

### Version constraints

An omitted constraint accepts any candidate version including absence. A
present-empty constraint always fails. Exact text is case-sensitive and
spelling-sensitive. A numeric comparison uses `=`, `<`, `<=`, `>` or `>=`.
A range such as `>=1.2,<2.0` requires exactly a lower comparison followed by
an upper comparison. All explicit constraints fail for absent candidates.
Nonnumeric versions can match exact text and fail numeric constraints.
[Final.003 §4][versions]

Interpret numerics as bounded dot-separated nonempty ASCII decimal components.
Compare significant digit lengths and digits component by component, ignoring
leading zeros and treating missing trailing components as zero. Preserve the
original spellings and avoid fixed-width conversion or other numeric conversion.
Validate syntax, bounds, endpoint order and inclusivity during construction and
reconstruction. Numerically equal endpoints require both comparisons to be
inclusive. Accept correctly ordered distinct endpoints without searching for a
representable witness. [Final.003 §4][ranges]

After outer canonical decoding when applicable, a leading `=`, `<` or `>`
selects numeric grammar, with `<=` and `>=` recognised first. An initial
backslash escapes only a leading operator character or backslash for exact
text. An invalid reserved leading escape fails validation. Interior and trailing
backslashes are literal. A comma is literal in exact text and a separator only
in numeric grammar. Validate decoded exact text as a present Topic version.
Reject malformed operator-led constraints without fallback. Preserve the raw
constraint text and presence, including escaped spelling, for equality, hashing
and interchange. [Final.003 §5][spelling]

### Canonical and XML interchange

Implement the Topic projections and strict parsers specified in Final.003 §3,
including the non-versioned canonical identity, versioned canonical name and
Topic rendering, raw period-joined name, escaped final relative entry, raw
friendly name and display name (sole value or last two values joined with a
hyphen). An ordinary mask canonical entry is
`qualifierPattern[:versionConstraint]=valuePattern`, with containment supplied
separately. Escape literal `.`, `=`, `:` and backslash in fields and join
entries with unescaped periods. Decode exactly one outer escape layer before
validating completed fields, patterns or constraints. Preserve a colon before
the equals delimiter for a present-empty constraint, and omit it for absence.
Ordinary masks render their canonical expression. Universal masks have a
distinct logical record and may render descriptive text. [Final.003 §§3 and 5][names] · [Canonical masks][canonical]

Implement the stateless `NamingXmlCodec` in Naming. Its four operations are
`readTopic(byte[])`, `writeTopic(Topic)`, `readTopicMask(byte[])` and
`writeTopicMask(TopicMask)`. Use the strict UTF-8 XML envelope and deterministic
bytes. Preserve ordered completed Topic fields and optional nonempty versions.
An ordinary mask carries explicit kind, containment and ordered entries with
`qualifierPattern`, `valuePattern` and optional `versionConstraint`, in that
order. Encode raw mask fields as canonical padded UTF-8 Base64 with
`encoding="base64"` and no line breaks or canonical escaping. Preserve the
present-empty constraint as `<versionConstraint encoding="base64"></versionConstraint>`.
A universal record has explicit kind with neither containment nor criteria.
Validate record structure before Base64 and strict UTF-8 decoding and then
validate the recovered raw text. Reject conflicting encoding, unexpected or
duplicate content, non-XML structural whitespace, DTDs and external resolution.
Publish only the accepted format. [Final.003 §§5 and 8][xml] · [Supported API][api]

### Public API, validation and diagnostics

Use the complete [Final.003 §8 API table][api] as the signature and visibility
contract for `Topic`, `TopicEntry`, `TopicBuilder`, `TopicMask`,
`TopicMaskEntry` and `TopicMaskBuilder`. The `Topic.builder()` and
`TopicMask.builder()` factories supply builders with package-private
constructors. Use `TopicMaskEntry.of` and `TopicMaskBuilder.addPatterns` for
raw inputs. Expose `getQualifierPattern()`, `getValuePattern()` and
`getVersionConstraint()` returning `Optional<String>` for the raw constraint.
Keep literal helpers, fluent builders and factory overloads as specified. The
two-field raw overload omits the constraint. The three-field raw overload
accepts null for omission, empty for present-empty and nonempty raw text under
the lexical rules. The exact-version literal helper validates its supplied
nonempty version and escapes a reserved leading character.

Expose only the supported model and XML surfaces, `NamingValidationException`
and the typed vocabulary `NamingFailureCategory`, `NamingOperation` and
`NamingFieldKind`. Keep parsers, evaluators, parsed descriptions, resource
limits, diagnostics and exception construction internal. Remove the public
exception factory and the comparison-only `MASK_MATCH` from the validation
surface. Avoid public bridges between packages. [Final.003 §8][api]

Report typed construction and reconstruction failures with a stable category,
actual operation, field kind and a valid zero-based entry index when known.
Use an absent index before a position is known or for an invalid position.
Preserve builder state and publish no partial result. Keep diagnostic metadata
and exception details free of raw text, paths and payload-bearing messages.
[Final.003 §§6 and 8][limits] · [Supported API][api]

Public `matches(Topic)` uses an absent handler. It constructs no diagnostic
record and dispatches no callback. A package-private caller can supply a
synchronous, invocation-local handler. Determine the Boolean outcome before
emitting at most one bounded exit record with category, candidate depth, field
kind and optional valid index. Keep the index absent before any field check
and use the last checked position for ordinary success. Keep the handler outside
mask state and interchange. An unexpected evaluation failure returns false.
Handler failure preserves the established outcome without retry. Verify local
TRACE eligibility and handling-boundary ERROR intent. Actual application logger
emission, console independence, buffering, backpressure, delivery,
acknowledgement, recovery and caller latency evidence remain deferred to the
governed operational logging work.
[Final.003 §6][diagnostics] · [Standalone logging qualification][logging-qualification]

### Resource bounds

| Limit | Accepted maximum |
| --- | --- |
| Topic and ordinary mask depth | 16 |
| Decoded Topic qualifier, value and present version | 64, 256 and 64 code points respectively |
| Raw qualifier or value pattern, including literal-helper output | 512 code points each |
| Topic canonical text, ordinary mask canonical text and one XML record | 32 KiB, 128 KiB and 256 KiB respectively, measured in UTF-8 bytes |
| Exact version constraint text | 64 ASCII characters decoded, 65 raw with a leading escape |
| Numeric version operand | 64 ASCII characters including dots, at most 32 components |
| Raw single numeric comparison | 66 ASCII characters including its operator |
| Raw numeric range constraint | 133 ASCII characters, including operators and comma |

Validate Unicode well-formedness before counting code points and measure
decoded fields before outer escaping. Enforce byte, field and encoding limits
before oversized temporary allocation and stop before materialising a
seventeenth entry. Bound parsed descriptions and scan a numeric candidate at
most once per version check, comparing it with at most two operands. Match
with invocation-local state and at most 32 qualifier/value checks plus 16
version checks. Builders are caller-confined or coordinated. Completed models
support concurrent immutable reuse without shared mutable evaluation state or
global match-result caches. [Final.003 §6][limits]

### Dependency coherence and verification

Start from the [affected-dependency assessment][dependency-evidence] and repeat
it against the exact authorised starting source. Check direct and indirect
production and test consumers, interfaces, adapters, representations,
configuration, integrations, fixtures, documentation and the effective build
graph. Refactor affected consumers only to the current contract. Remove
obsolete RE2/J machinery and dependencies from Naming. Keep JUnit test-scoped
and retain Calliope's independence from higher layers. Exclude compatibility
APIs, dual-format readers and writers and a selector migration framework.
Escalate any affected independently governed boundary whose contract or
mapping lacks authority. [Final.003 §§7 and 8][coherence] · [DR-9][dr9]

Run focused Calliope tests and reactor verification on JDK 21 using bounded
commands, including `mvn -B -ntp -pl calliope -am test` and
`mvn -B -ntp verify`. Exercise the applicable consumer regressions,
solution-wide regression and integration scenarios, and relevant architecture
and implementation conformance. Use the qualified interim structural JUnit,
diff and dependency checks for standalone evidence within their recorded
scope. Report the named absent ArchUnit suite as unexecuted where applicable.
Report exact source, commands, environment, scenarios, test cases actually
executed, failures, errors, skips and unavailable checks. Count actual test
executions from reports without adding duplicate Maven summaries. Seek
explicit Solution Architect treatment of required incomplete checks. Obtain
independent Codex review of the exact successor before software acceptance.
Map outcomes to AC-1–AC-13 in the Implementation Report and applicable
Software HCRL. [Final.003 §10][verification] · [Software Development Praxis §6][praxis-evidence] · [Review reconciliation][review-evidence]

## Expected Outcomes

- Immutable, documented Naming models with qualified Topic identity, bounded
  positional selection and the accepted Java surface.
- A single strict canonical and XML contract that preserves the applicable
  ordered data, original spellings, constraint presence and logical kind.
- A coherent build and affected consumers, with a reproducible evidence set
  for the exact integrated candidate and explicit treatment of applicable
  outstanding gates.

## Acceptance Criteria

| Criterion | Observable result | Verification / expected evidence | Final.003 trace |
| --- | --- | --- | --- |
| AC-1 · Identity, ordering and hierarchy | Topic equality, hashing and natural order follow the ordered case-sensitive qualifier/value path, with shorter proper prefixes first. Navigation preserves retained versions. Masks compare by kind and their applicable ordered raw data, constraint presence and containment. | Test repeated qualifiers, distinct paths sharing a name, case, qualifier-then-value order, strict prefixes and equal version variants in hash and sorted collections. Check root, parent, null, equal and strict ancestry, current and invalid ancestor depths and actual retained versions. Compare mask kind, order, containment and equivalent numeric spellings as distinct structures. | [§§1–2][identity] · [§4][matching] · [§10][verification] |
| AC-2 · Names and canonical Topic parsing | All Topic projections and both strict parsers implement the accepted grammar and preserve allowed text. | Assert independently expected projections at depth 1, 2 and greater depth, including escaped delimiters, raw display text, supplementary and composed/decomposed text. Reject empty or omitted completed fields, malformed escapes, segments and delimiters, and version syntax in the unversioned parser. | [§3][names] · [§10][verification] |
| AC-3 · Versions | Topic versions are opaque. Mask constraints distinguish omission, present-empty, exact text, single numeric comparisons and bounded ranges. | Check actual stored Topic versions, including absence and rejection of explicit empty version during strict reads. Exercise all five numeric operators, unequal widths and component counts, long digits, leading and trailing zeroes, exact versus numeric spelling, nonnumeric and absent candidates, escaped leading operators/backslash, exact commas and a literal star. Test lower/upper inclusivity, valid closed singletons and correctly ordered distinct ranges even without a representable interior candidate, plus reversed, exclusive equal-endpoint, malformed and duplicate-bound rejection. Check invalid-add recovery and version-sensitive selection of equal Topics. | [§§1 and 3][identity] · [§4 version matching][versions] · [§5 spelling][spelling] · [§10][verification] |
| AC-4 · Mask evaluation | Match ordered positions after the depth check, using the six wildcard forms and qualifier/value/version short circuit. Universal masks remain explicit. | Test all six forms for qualifier and value, escaped stars/backslashes, ordinary punctuation, case, Unicode, null and empty patterns, rejected wildcard arrangements and non-overlap (for example `AB*BC` against `ABC`). Test reordered criteria, exact and inclusive containment, depth and field-order counters, both kinds against null, universal accessor rejection and an unrestricted ordinary criterion that remains ordinary. | [§4][matching] · [§8][api] · [§10][verification] |
| AC-5 · Builders and validation | Both builders validate before mutation and return independent immutable snapshots. Topic indexed input uses final positions and the first successful add fixes mode. | Test all Topic add overloads, a failed first add, both mixed-mode directions, out-of-order indices, gaps and recovery, negative indices, successful replacement without depth growth and failed replacement rollback. Compare generated and explicit qualifiers in equality, ordering, matching and interchange. Test failed mask additions/builds, helper equivalence, reuse, defensive snapshots and immutable views. | [§§1–2][identity] · [§4][matching] · [§8][api] · [§10][verification] |
| AC-6 · Serialization and deserialization | The four codec operations read strict completed records and write deterministic UTF-8 XML preserving ordered fields, kinds, containment and raw mask data. | Use independently authored expected Topic, ordinary and universal XML fixtures, including exact writer bytes, canonical padded Base64, empty versus omitted constraints, Unicode, line endings in raw patterns and original numeric/exact spellings. Check the actual decoded Topic entries and versions and reconstructed selector outcomes. | [§5 XML][xml] · [§8][api] · [§10][verification] |
| AC-7 · Lossless reconstruction | Unversioned Topic canonical names round-trip identity, versioned names round-trip all entry data, and ordinary mask canonical text plus containment round-trips structure and matching behaviour. | Assert independent canonical strings, field values and re-emission. Include both containment values, empty patterns, omitted and present-empty constraints, numeric operators and ranges, exact commas and both levels of escaping. Verify explicit universal logical reconstruction and that canonical input never creates universality. | [§3][names] · [§5][canonical] · [§10][verification] |
| AC-8 · Malformed input and diagnostics | Strict field, canonical and XML rejection yields safe typed context. Optional diagnostics preserve the match outcome and unexpected evaluation failure returns false. | Check omitted, empty and invalid completed qualifiers, Unicode categories and whitespace, malformed constraints at all input boundaries, XML ordering, duplicates, incorrect kind/containment, conflicting declarations, malformed UTF-8/Base64 and prohibited DTD/resolution. Assert category, actual operation, field and valid or absent index for builder, navigation, canonical and XML failures. Prove disabled record construction and dispatch are absent. Use local capture and fault injection for bounded metadata, one exit, TRACE/ERROR intent and throwing handler behaviour on success, mismatch and evaluation failure. | [§§1 and 3][identity] · [§§4–6][matching] · [§8][api] · [§10][verification] |
| AC-9 · Bounds and resource use | All accepted field, entry, operand, component, raw and encoded bounds hold. Evaluators use prevalidated descriptions and bounded local work. | Exercise below, at and beyond applicable limits, including code points versus UTF-8 bytes, helper-expanded patterns, sparse indices, excessive field encodings and the seventeenth entry before materialisation. Verify maximum-depth/field positive and late-mismatch workloads, depth and field check counts and at most one candidate numeric scan per version check. Establish construction-time validation and absence of recursion, backtracking and global result caches. | [§4][matching] · [§6][limits] · [§10][verification] |
| AC-10 · Concurrency | Completed models and their descriptions are immutable and can be reused concurrently. | Use time-bounded mixed-outcome tests against a shared mask, including depth, text and version mismatches and successful matches. Verify independent snapshots and mutation-resistant entry views after builder reuse. | [§2][construction] · [§6][limits] · [§10][verification] |
| AC-11 · Structural and dependency coherence | The public surface and package boundaries match §8. Naming uses JDK operations and the single current interchange contract. Affected consumers use that contract. | Compare source/bytecode API inventory to §8, including codec co-location and internal diagnostics and limits. Inspect complete POM coordinates/scopes and the effective dependency graph for RE2/J removal, absence of regex machinery and higher-layer imports, and test-only JUnit. Trace affected producer and consumer changes or substantiate their absence. Review cohesive responsibilities and count full production Java files, including documentation, against the Implementation Guide's 500-line threshold and exception process. | [§§4 and 6][patterns] · [§§7–8][coherence] · [§10][verification] |
| AC-12 · Implementation and acceptance evidence | The exact candidate has functional, defensive, architectural and integrated conformance evidence under the Implementation Guide and Praxis. | Map actual AC results to the exact source and independent review. Show component and reactor runs, actual affected-consumer and solution-wide regression/integration exercises, and applicable conformance results. Use unique executions per run, with failures, errors, skips and unavailable checks disclosed. Record required Solution Architect dispositions, DR-6 dependent Architecture treatment and the scoped structural and logging qualifications. | [§§9–10][architecture] · [Software Development Praxis §6][praxis-evidence] |
| AC-13 · Educational documentation | Classes and significant operations explain responsibilities, valid inputs, caller boundaries and unhappy paths in terms of the supported contract. | Compare documentation with actual Java signatures and behaviour. Review factory and builder modes, confinement, ancestry failures, canonical and XML rules, absence and present-empty semantics, universal accessor restrictions, typed metadata, limits and diagnostic handling. Describe renderings as potentially sensitive and identify the neutral-selection and logging boundaries. | [§§1–8][design] · [§10][verification] · [Implementation Guide][guide] |

## Solution Architect acceptance

Mark Hunter accepted Backlog Item 0003 revision 000.004 as the governing
specification for the next authorised Calliope Naming assignment for Software
`0.1.0-SNAPSHOT` on `feature/BL-0003-Calliope-Topic-and-TopicMask`. The acceptance
covers the reviewed scope, technical requirements, AC-1–AC-13, Final.003
correspondence, dependency assessment and verification obligations.
The [reviewed input][reviewed-input] preserves the exact pre-acceptance Markdown
at SHA-256 `ae29876de13d6171dc46c4c5300442e9fa24f419a2fece4dc7b150882968a0a6`.
[Preparation report][report] · [Acceptance record and handover][acceptance-report]

Revision 000.003 retains its historical acceptance. Architecture 1.1.26 is the
incorporated baseline. Final.003 incorporation awaits the governed 1.1.27
predecessor and 1.1.28 transition. Implementation requires the applicable
DR-6 conditions and a separately authorised assignment. The RE2/J candidate
remains unaccepted and its Software HCRL 010 findings retain their standing.
[Revision 000.003][prior] · [DR-6][dr6] · [DR-10][dr10] · [Software HCRL 010][software-review]

[prior]: BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md
[reviewed-input]: evidence/0003/000.004-reviewed.md.gz
[acceptance-report]: evidence/0003/000.004-acceptance-report.md
[logging-qualification]: BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md#accepted-standalone-diagnostics-qualification
[report]: evidence/0003/000.004-preparation-report.md
[dependency-evidence]: evidence/0003/000.004-preparation-report.md#affected-dependency-assessment
[review-evidence]: evidence/0003/000.004-preparation-report.md#independent-review-reconciliation
[design]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md
[identity]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#1-topic-identity-and-structure
[construction]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#2-topic-construction-and-navigation
[names]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#3-topic-names-and-strict-canonical-parsing
[matching]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#4-topicmask-kinds-and-matching
[patterns]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#qualifier-and-value-patterns
[versions]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#version-matching
[ranges]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#bounded-ranges-and-validation
[canonical]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#5-canonical-mask-expressions-and-logical-interchange
[spelling]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#version-constraint-spelling
[xml]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#logical-records-and-xml-encoding
[limits]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#6-resource-limits-concurrency-and-diagnostics
[diagnostics]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#diagnostic-handler
[consumers]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#7-consumer-boundaries
[coherence]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#pre-100-implementation-coherence
[api]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#8-supported-java-api
[architecture]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#9-remaining-incorporation-and-verification
[verification]: ../../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md#10-behavioural-verification-for-a-future-implementation
[dr3]: ../../enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md#dr-3
[dr4]: ../../enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md#dr-4
[dr5]: ../../enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md#dr-5
[dr6]: ../../enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md#dr-6
[dr8]: ../../enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md#dr-8
[dr9]: ../../enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md#dr-9
[dr10]: ../../enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md#dr-10
[archunit]: ../../enterprise-architecture/design/review/1.1.27/hcrl-1.1.27-002.pending.md
[foundation]: ../../enterprise-architecture/design/register/software-0.1.0/hcrl-software-0.1.0-009.complete.md
[software-review]: ../../enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
[naming-governance]: ../../enterprise-architecture/governance/canonical-identity-and-naming.md#architectural-identity
[axioms]: ../../enterprise-architecture/governance/architectural-axioms.md
[praxis]: ../../enterprise-architecture/governance/software-development-praxis.md
[praxis-evidence]: ../../enterprise-architecture/governance/software-development-praxis.md#6-solution-wide-candidate-submission-and-evidence
[guide]: ../implementation/Harmonia-Implementation-Guide-Philosophy.md
[iu]: ../../enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.002.md#2-identity-and-distinct-revisions
[calliope]: ../../enterprise-architecture/continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md#architectural-content
[topic]: ../../enterprise-architecture/continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md#architectural-content
[mask]: ../../enterprise-architecture/continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md#architectural-content
[nfr]: ../../enterprise-architecture/continuum/motivation/principle/6973441f2d62448188af9bfce5ad9d9f.md#architectural-content
