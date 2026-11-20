<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Harmonia — F-01 Topic and TopicMask Adjudication Record

**Intended repository path:** `enterprise-architecture/design/evidence/software-0.1.0/002/before/F-01-Topic-TopicMask-Adjudications.md`  
**Status:** Solution Architect discussion decisions accepted; DRP consolidation, Architecture Continuum incorporation and software implementation remain pending.  
**Source:** Calliope Design Review and Adjudication conversation (F-01.1 through F-01.44).  
**Historical input:** `../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.000.md` (retain unchanged).  
**Target design:** `../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.001.md` (not yet accepted).

## Purpose and governance

This is the **before** evidence supplied to Codex for a bounded reconciliation of the existing Topic/TopicMask DRP. It records decisions agreed with the Solution Architect, including corrections made during the discussion. It is not an accepted Architecture Continuum baseline, an implementation authorisation, or permission to publish, commit or push. The historical Final.001 document contains superseded assumptions, especially UUID identity and the absence of TopicEntry versions. Never use those historical statements to override this record silently.

## Accepted F-01 adjudications

| ID | Accepted decision |
|---|---|
| **F-01.1 — Identity** | Topic has **no UUID**. Its identity, `equals()` and `hashCode()` are based on its **non-versioned canonical name**. Different per-entry versions do not alter Topic identity. Version-sensitive comparison is distinct from equality. |
| **F-01.2 — Immutability** | `Topic` and `TopicEntry` are strictly immutable; defensive copies; changes create new objects. Derived names are deterministic. |
| **F-01.3 — Ordering** | Sequence keys are an implementation mechanism for guaranteed deterministic order, **not independent architectural meaning**. Retaining a `SortedMap<Integer,TopicEntry>` is acceptable. The implementation must prevent external mutation and preserve order on reconstruction. |
| **F-01.4 — Repeated qualifiers** | Repeated qualifiers are permitted at distinct hierarchical positions (e.g. several successive `Component` entries). Full ordered path determines identity. |
| **F-01.5 — Hierarchy** | Hierarchy is implicit in ordered entries. Exact Topic equality is distinct from ancestor/descendant relationships; masks handle prefix/containment matching. No stored parent/child links required. |
| **F-01.6 — Structural containment** | Containment uses **whole TopicEntry boundaries**, not raw string-prefix comparisons. Regex handles pattern matching separately. |
| **F-01.7 — Positional masks** | Mask entry *i* matches Topic entry *i*; no implicit skipped levels. With containment enabled, additional **trailing** Topic entries are permitted. |
| **F-01.8 — Field regex** | Mask qualifier and value are independently evaluated using RegEx; both criteria must match at their position. Version constraint, if present, is separate. |
| **F-01.9 — Construction helpers** | Support immutable `TopicMaskEntry` convenience factories, such as `of(q,v)`, `forValue(v)` (qualifier `.*`), `forQualifier(q)` (value `.*`), and `any()`; and fluent `TopicMask.builder()` with corresponding helpers, optional versions and containment. These are construction conveniences, not new matching semantics. |
| **F-01.10 — Version matching** | Optional version constraints support RegEx. **Unspecified** version constraint matches any version **including absent**; explicitly supplied regex requires a present version (`.*` does not match absent). |
| **F-01.11 — Mask equality** | TopicMask has **no UUID**. Equality/hash are structural: ordered qualifier, value and version expressions plus `includeContainedElements`. Compare expression strings, not language equivalence of different regexes. Immutable. |
| **F-01.12 — Matching API** | **Corrected decision: Option A.** Authoritative public API is `TopicMask.matches(Topic)`, **not** `Topic.matches(TopicMask)`. Topic remains independent of mask evaluation. |
| **F-01.13 — Full-field matching** | Regex uses full-field `matches()`, not substring `find()`. Caller supplies `.*` when substring matching is intended. |
| **F-01.14 — Compiled patterns** | Compile regex during mask construction; reject malformed expressions immediately; reuse compiled immutable patterns; create per-invocation matchers. Keep original expression strings for representation/equality. |
| **F-01.15 — Logical serialisation** | Serialized mask authority consists of ordered qualifier/value/version expression strings plus containment flag. Compiled patterns are reconstructed from these values, not part of interchange contract. |
| **F-01.16 — Format independence** | Calliope defines the logical representation, **not** a mandatory JSON/Java/transport format. Adapters preserve order, identity, version semantics, and immutability. No transport-specific annotations required in core model. |
| **F-01.17 — Empty/universal masks** | Empty mask is **invalid**. Universal match must be explicitly constructed as `TopicMask.all()`. `includeContainedElements` defaults **false**. Matching all is **not** permission to access all; Themis policy remains separate. |
| **F-01.18 — Regex engine** | Adopt **RE2/J** as TopicMask regex engine for predictable matching and no catastrophic backtracking; document supported dialect and exclusions; **no automatic `java.util.regex` fallback**. Input/resource limits still needed. |
| **F-01.19 — Literal helpers** | Distinguish regex builders from literal builders; e.g. `forValue` vs `forLiteralValue`, `forQualifier` vs `forLiteralQualifier`, `of` vs `ofLiterals`. Literal arguments are escaped for RE2/J. Equivalent builder helpers. |
| **F-01.20 — Null candidate** | `TopicMask.matches(null)` returns **false**, including `TopicMask.all()`. Null/malformed definitions are still invalid at construction. |
| **F-01.21 — Case sensitivity** | Topic identity and default regex matching are **case-sensitive**. No automatic case normalization. Explicit regex flags (e.g. `(?i)`) may request case-insensitivity. |
| **F-01.22 — Entry version type** | Version is optional **String**, not a semver object. Calliope does not prescribe version schemes, ordering or compatibility. It does not change Topic equality. |
| **F-01.23 — Absent/empty version** | `null` and `""` both mean **absent version** and normalize to same internal state. Nonempty versions retain exact text, including whitespace. Absent versions are omitted in versioned canonical strings. |
| **F-01.24 — Generated missing values** | An absent or empty **value** is replaced at Topic assembly with `N<zero-based-position>` (`N0`, `N1`, …); it thereafter behaves exactly like an explicit identical literal. This **supersedes** earlier willingness to store empty TopicEntry values. **Qualifier remains required**; Final.001's generated-*qualifier* rule is superseded. |
| **F-01.25 — Escaping** | Canonical text uses backslash escapes for structural delimiters `.` `=` `:` and literal `\`; fields internally retain original unescaped values. Escaping/parsing must be deterministic and reversible, including periods in versions/values. |
| **F-01.26 — Topic parsing** | Strict factories `Topic.fromCanonicalName(String)` and `Topic.fromCanonicalNameWithVersion(String)`. Non-versioned input cannot restore omitted versions; versioned input retains versions. Reject malformed syntax/escaping/qualifiers; preserve case/whitespace. |
| **F-01.27 — Mask expression parsing** | Provide `TopicMask.fromCanonicalExpression(String, boolean includeContainedElements)`. Use a dedicated regex-aware parser that correctly separates structural escaping and regex syntax; do not naively reuse Topic parser. Builder preferred for programmatic creation. |
| **F-01.28 — Display** | `Topic.toString()` gives `getCanonicalNameWithVersion()`; `TopicMask.toString()` gives `getCanonicalExpression()`. Mask `toString()` need not encode containment and is **not** a complete serialization contract. |
| **F-01.29 — Natural ordering** | `Topic implements Comparable<Topic>`, comparing ordered qualifier/value pairs **without versions**; order must be consistent with `equals()`, case-sensitive. `TopicMask` is **not Comparable**. |
| **F-01.30 — Navigation** | `getParent()` drops last entry (null for one-entry root); `getAncestor(int depth)` retains first `depth` entries; `isAncestorOf(Topic)` tests **strict**, structural, literal qualifier/value ancestry, ignoring versions. No parent links. |
| **F-01.31 — Navigation versions** | Parent/ancestor Topics preserve versions on all retained entries. |
| **F-01.32 — Depth** | `getDepth()` returns entry count; root depth 1; empty Topics invalid; versions do not affect depth. |
| **F-01.33 — Depth limit** | Maximum Topic hierarchy depth is a **software-release-defined** parameter (proposed `MAX_TOPIC_DEPTH`), enforced on Topic and TopicMask construction/parsing, not dynamically configurable at runtime. Numerical value remains TBD. |
| **F-01.34 — Field limits** | Independently release-defined maximum qualifier, value, version, and mask-regex lengths. Enforced on construction/parsing. Numerical values remain TBD. |
| **F-01.35 — Regex complexity** | In addition to expression length, enforce a release-defined compile/evaluation complexity safeguard where technically feasible. Exact mechanisms and thresholds require **verification against RE2/J** and implementation evidence; do not claim a capability unverified. |
| **F-01.36 — Short circuit** | Check depth/containment structure before regex. Iterate positions; check qualifier, value, version (if constrained); return false on first mismatch, true only on complete success. `all()` is explicit special case, excluding null. |
| **F-01.37 — Diagnostics** | Retain Boolean API rather than a public `TopicMatchResult`; record comparison exit point (zero-based), failure category and suitable diagnostic information in **log or appropriate audit trail**. Never automatically expose sensitive matching details. |
| **F-01.38 — Evaluation failures** | Unexpected runtime matching failure returns **false** and emits an **ERROR LogEvent**; do not propagate as a normal API exception. Logging failure must not turn a mismatch/error into `true`. Invalid mask construction remains exceptional. |
| **F-01.39 — Logging architecture** | Calliope uses Harmonia's application logging framework, following parallel Local-Logger/console and in-JVM Log-Satellite strategy; no direct rsyslog/Log-Server/Log-Persistence dependency. Diagnostic `LogEvent` is distinct from security `AuditEvent`: Themis makes audit decisions; Calliope does not independently audit each mask. Avoid indiscriminate sensitive data logging. **Qualification:** logging backpressure/failure interaction requires verification under existing DRP. |
| **F-01.40 — Log levels** | Match, normal mismatch and depth mismatch: conditional **TRACE**. Invalid mask at handling boundary: **ERROR**. Unexpected evaluation error: **ERROR**. No INFO/DEBUG-per-comparison requirement. Include exit point when logged. |
| **F-01.41 — Concurrency** | Single immutable TopicMask safely matches concurrently without external synchronisation; compiled patterns shared, per-call state local, deterministic output. |
| **F-01.42 — Cache** | **No global TopicMask×Topic Boolean-result cache** in Calliope. Consumers may reuse immutable compiled masks. Future result caches require measured evidence and explicit rationale. |
| **F-01.43 — Acceptance tests** | Mandatory Maven/JUnit tests across identity, hierarchy, regex, versions, construction, escaping/parsing, immutability, serialization, validation, security/fail-closed, diagnostics, concurrency and performance, especially negative/overbroad-mask scenarios. |
| **F-01.44 — Consolidation** | Preserve historical `Final.001`; consolidate these decisions into `DRP-Topic-and-TopicMask.draft.002.md`, then review before Final.002 and separately governed Architecture Continuum reconciliation. **No Java implementation, publication, ADD/HCRL, commit or push authorised here.** |

## Name and hierarchy conventions

For three entries (positions `0..2`):

| API | Expected representation |
|---|---|
| `getCanonicalName()` | `qualifier0=value0.qualifier1=value1.qualifier2=value2` |
| `getCanonicalNameWithVersion()` | `qualifier0:version0=value0.qualifier1:version1=value1.qualifier2=value2` (only show `:version` where present) |
| `getName()` | `value0.value1.value2` |
| `getRelativeName()` | `qualifier2=value2` |
| `getFriendlyName()` | `value2` |
| `getDisplayName()` | `value1-value2` (presentation convention given for the three-entry case; behaviour for depth 1 requires specification) |

Example legitimate repeated qualifiers: `System=Harmonia.Application=Mneme.Component=IngresServices.Component=PetasosReceiver.Component=HL7v2MessageReciever`. The example spelling is retained as originally provided, not a normalization rule.

## Critical precedence / corrections

1. **F-01.12:** The user initially chose B and immediately corrected to **A**. Authoritative API is `TopicMask.matches(Topic)`.
2. **F-01.24:** Placeholder `N<position>` applies to missing **values**, **not** qualifiers, contrary to historical Final.001.
3. **F-01.1 / F-01.11:** No UUID for either Topic or TopicMask, contrary to Final.001.
4. **F-01.22:** Optional version is at `TopicEntry`, contrary to historical statement that Topic/TopicMask have no versions. This does **not** equate TopicEntry version with IU/content revision.
5. **F-01.17:** An empty mask does **not** implicitly match all; universal matching requires explicit construction.
6. **F-01.39:** Follow the existing logging middleware DRP (`DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md`); rsyslog is the Log-Server strategic foundation, while application framework/transport/persistence technology details remain unselected in that DRP.

## Outstanding qualifications requiring reconciliation, not invention

- **Literal vs regex escaping and parse grammar:** A textual mask expression containing regex metacharacters and Topic structural delimiter escapes needs a rigorously reversible grammar; Java code must not be generated from a hand-wavy split on `.` or `:`. Establish behaviour for regex backslashes, groups, class expressions, and separators.
- **`getDisplayName()` at depths other than three** and escaping of noncanonical human-facing names require explicit implementation definition without changing the accepted three-entry example.
- **`TopicMask.all()` encoding:** Needs a deterministic nonempty logical expression / tagged universal representation compatible with rejection of empty masks; ensure serialization retains the explicit universal state.
- **Regex limits:** Exact RE2/J available compile/complexity controls, thresholds and error mapping must be evidenced. Do not assume a configurable complexity API.
- **Logging backpressure:** Reconcile fail-closed matching with the satellite's bounded buffer/backpressure. Do not silently introduce independent infrastructure or drop required events.
- **Versioned name vs equality:** Ensure serialization, `compareTo` and collections are consistent where two Topic objects have identical names but different versioned entries.
- **Resource limits:** Numerical release values remain undecided and require software-release governance.
- **Historical architecture references:** Reconcile accepted Information Unit framework, earlier Topic identity DRP and Continuum identities without automatically modifying them; list contradictions as exceptions for architect adjudication.

## Intended Codex delivery after receiving this evidence

1. Create `../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.001.md` as a complete coherent design based on the decisions above.
2. Preserve Final.001 as unmodified historical evidence.
3. Produce **after** evidence: concise decision delta, bounded cross-source conflicts, software implementation gap analysis, and unresolved qualifications.
4. Do not alter Java, tests, accepted Continuum, releases, or HCRL; do not commit or push absent separate authorisation.
