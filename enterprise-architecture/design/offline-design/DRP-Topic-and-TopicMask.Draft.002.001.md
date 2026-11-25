<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Package — Topic and TopicMask

**Standing** · Draft.002.001 · Proposed for Solution Architect review. This is
the first draft based on accepted DRP release 002, with possible successor
Final.003 after adjudication. Architecture 1.1.26 is the accepted Continuum
baseline. Software 0.1.0-SNAPSHOT is an unaccepted implementation candidate.

**Design baseline** · [Exact qualified accepted release 002 content][baseline],
currently held at the original Final.001.001 path. The [filename reconciliation
inventory][filenames] records the proposed Final.002 locator. [Software HCRL
004 DR-10][review] and [007 finalisation][acceptance-boundary] establish the
source standing and qualifications.

Sections 1–3 and 7 carry forward the qualified design's Topic and consumer
contracts. Sections 4–6, 8 and 10 propose the R1/R2 matching changes and their
verification. Section 9 records the choices needing adjudication. R1/R2 refer
to the common Topic identity and TopicMask matching scope. The existing R3
decision on IU Topic designation remains in §7. Potential matching of IU
representation type or content is a separate downstream concern.

## 1. Topic identity and structure

A Topic identifies or classifies a named concept by an ordered root-to-leaf path of typed entries. A completed Topic has at least one entry. Each completed TopicEntry has a qualifier, a value and an optional version. The qualifier identifies the role of that entry at its position. Qualifiers may repeat at distinct positions, as in `System=Harmonia.Application=Mneme.Component=IngresServices.Component=PetasosReceiver.Component=HL7v2MessageReciever`. Zero-based positions establish order. A list or a sorted map can represent that order, with any sequence keys serving as an implementation mechanism. Reconstruction preserves the same complete ordering.

Topic and TopicEntry are immutable. Construction defensively copies mutable inputs, and read access preserves immutability. Changing a path or its version data creates a new object. A Topic carries no runtime UUID. Its non-versioned canonical name supplies identity, equality and hashing. Versions do not affect Topic equality. The full typed path determines identity, so distinct paths can have the same unqualified name.

Changing the non-versioned qualified path changes runtime Topic identity. Sequence indices support deterministic construction and reconstruction. Identity depends on the ordered qualifier/value pairs, regardless of the assembly mechanism. The runtime model does not require global uniqueness of the unqualified name. Identify any existing registry-scoped uniqueness or alias rules and retain their effects until an explicit compatibility or migration decision covers them. Preserve or explicitly migrate historical UUID-backed references outside `Topic`, including persisted references. Map generated and explicit `N<position>` forms where legacy users distinguished them. Ambiguous references require disposition before migration. [R1][reconciliation]

An architectural element retains its existing element identifiers and governed `canonicalId` through rename or containment changes under the [architectural naming contract][architecture-naming]. Its governed naming and display properties keep their own semantics. Runtime naming accessors follow §3. Matching text establishes neither shared identity nor correspondence. Record an explicit mapping when an architectural element describes or governs a runtime Topic. Preserve historical architectural references and use ADD/HCRL for any architectural update. The runtime model has no architecture UUID, `canonicalId` field or accessor. The IU UUID remains the identity of the IU. [R4][reconciliation]

Topic natural ordering compares qualifier and then value at each position, case-sensitively and lexicographically, with versions excluded. The first difference determines the order. If one path is a proper prefix of the other, the shorter path precedes the longer one. The comparison returns zero exactly for equal Topics. Hash-based and naturally ordered collections can therefore treat version variants as one key. A version-sensitive comparison uses the complete ordered entry data or the versioned canonical name.

### Completed TopicEntry fields

| Field | Accepted construction and validation |
| --- | --- |
| Qualifier | A completed qualifier is nonblank printable ASCII (U+0020–U+007E), with no leading or trailing whitespace. Ordinary internal spaces and printable punctuation are allowed. At Topic assembly only, `null` or `""` becomes `N<position>` using the final zero-based position in decimal, such as `N0` or `N1`. Validate the generated qualifier exactly as a supplied literal. A whitespace-only nonempty input is invalid. |
| Value | A value is mandatory and non-null/nonempty. Validate by Unicode code point. Allow letters (L*), numbers (N*), punctuation (P*), symbols (S*) and combining marks (M*). Allow only U+0020 as a space, internally. Reject controls (Cc), format characters (Cf), surrogate code points (Cs), private-use (Co), unassigned (Cn), line and paragraph separators (Zl/Zp), other space separators (Zs) and malformed Unicode sequences. |
| Version | An optional String with `null` and `""` representing the same absence. A present version is nonempty printable ASCII with no leading or trailing whitespace. Embedded ordinary spaces are allowed. No version scheme, compatibility interpretation or version order is prescribed. |

Reject invalid input without trimming, case conversion or Unicode normalisation. Preserve allowed code points as supplied. Verify Unicode well-formedness before measuring release-defined qualifier, value and version lengths in code points. Measure decoded fields before canonical escaping. A supplementary-plane code point counts once. Transport or storage UTF-8 byte limits apply independently.

A generated qualifier is indistinguishable from an equal explicit literal for identity, matching, ordering and interchange. For example, assembling `(System, Harmonia)` followed by `(null, Mneme)` yields `System=Harmonia.N1=Mneme`. The same result follows with an explicit `N1` qualifier. A supplied value of `N1` is ordinary literal text. Missing and empty values always fail validation.

## 2. Topic construction and navigation

A TopicBuilder can append entries sequentially or add entries at explicit indices. The first successful addition fixes the mode for the instance. An attempt to use the other mode fails. In indexed mode, negative indices fail, and `build()` requires every index from 0 to depth minus one. Adds can arrive out of order. A successful addition to an occupied index replaces the prior input without changing depth. Sequential and indexed inputs that assemble to the same ordered records produce equivalent Topics. Generated qualifiers use the final positions in that ordered path.

Validate supplied fields at addition, including mandatory values, nonempty supplied qualifiers and present versions. Validate before storing a new or replacement input or setting the mode. At `build()`, check depth, index continuity, generated qualifiers and all completed-object invariants. A failed add, replacement or build preserves the previous builder state and all published Topics. For example, a builder with indices 0 and 2 can fail the continuity check, add index 1 and then build successfully. Each successful build returns an independent immutable snapshot. Builders are reusable and need not be thread-safe. Assemble a different path explicitly. Required builder operations exclude entry removal, renumbering and a builder-from-Topic editing convenience.

Hierarchy is implicit in complete entries. Literal ancestry compares a proper prefix of qualifier/value pairs in order, case-sensitively and ignoring versions. For example, `Country=Australia.StateTerritory=ACT` is an ancestor of `Country=Australia.StateTerritory=ACT.City=Canberra`. The same path with `StateTerritory=AC` fails the ancestry test. Equal paths, including differing version variants, have no strict ancestry relationship. Mask matching has its own positional criteria.

- `getDepth()` counts entries. A root has depth 1.
- `getParent()` returns the path without the final entry, or `null` at a root.
- `getAncestor(int depth)` retains the first `depth` entries. It accepts `1 <= depth <= getDepth()` and rejects arguments outside that range. The current depth denotes the full Topic. Both navigation methods preserve versions on all retained entries.
- `isAncestorOf(Topic)` tests strict version-independent literal ancestry and returns false for a null candidate. Parent and child links need not be stored.

## 3. Topic names and strict canonical parsing

For the following three-entry example the first two entries have versions.

| API | Representation |
| --- | --- |
| `getCanonicalName()` | `qualifier0=value0.qualifier1=value1.qualifier2=value2` |
| `getCanonicalNameWithVersion()` | `qualifier0:version0=value0.qualifier1:version1=value1.qualifier2=value2` |
| `getName()` | `value0.value1.value2` |
| `getRelativeName()` | `qualifier2=value2` |
| `getFriendlyName()` | `value2` |
| `getDisplayName()` | `value1-value2` |
| `toString()` | The versioned canonical name |

Display at depth 1 is the sole value. At greater depth, join the final two values with `-`. The display preserves case and valid whitespace, omits qualifiers and versions, and applies no canonical escaping. Its role is presentation. `getName()` joins the raw values with periods and `getFriendlyName()` returns the raw final value. Neither uses canonical escaping. `getRelativeName()` returns the final qualified, non-versioned entry with canonical escaping. Raw projections can be ambiguous and do not supply qualified identity or a canonical parse input.

In canonical text, prefix every literal `.`, `=`, `:` and `\` in each field with one backslash. Join entries with an unescaped period. A non-versioned entry is `qualifier=value`. In the versioned projection, use `qualifier:version=value` when a version is present. Omit the entire version segment for absence. For example, `System=Harmonia.Component:2\.4=Petasos\.Receiver` has two entries, with version `2.4` and value `Petasos.Receiver` at position 1. Its non-versioned canonical name is `System=Harmonia.Component=Petasos\.Receiver`.

`Topic.fromCanonicalName(String)` reads only the non-versioned syntax and reconstructs all versions as absent. `Topic.fromCanonicalNameWithVersion(String)` reads the version-capable syntax, including unversioned entries. Both require explicit, valid nonempty qualifiers and values at every position. The parser decodes the field escapes, applies the completed-field and depth rules, and preserves the exact allowed field text. It rejects empty paths or segments, dangling or unknown escapes, missing field separators and extra or misplaced unescaped delimiters. `System=Harmonia.N1=Mneme` is valid. A segment spelled `=Mneme` or `N1=` is invalid. The parser never supplies defaults. Strict versioned parsing rejects a textual empty version segment such as `Role:=LAB`. The canonical spelling for an absent version is `Role=LAB`. At builder/API input, `null` and `""` still represent absence.

The non-versioned output must round-trip the identity projection. The versioned output must round-trip all ordered field data and re-emit the same canonical name with versions. Versioned round-trip assertions compare the actual stored versions because Topic equality ignores them.

## 4. TopicMask kinds and proposed matching

| Kind | Logical data and behaviour |
| --- | --- |
| Ordinary | At least one ordered criterion and `includeContainedElements` (default false). Each criterion has an independent qualifier pattern, value pattern and optional version constraint. Equality and hashing retain the logical kind, ordered original texts and version-constraint presence, and the containment flag. |
| Universal | Constructed explicitly through `TopicMask.all()`. Matches every non-null Topic. It has no criteria and containment is inapplicable. All universal masks compare equal and hash consistently. Reconstruction preserves the explicit kind. |

An ordinary mask with unrestricted text criteria remains ordinary. A wildcard
or canonical expression never denotes universality. TopicMask carries no UUID
and defines no natural ordering. `isUniversal()` identifies the explicit kind.
Its entry view is empty and both ordinary-only accessors,
`getCanonicalExpression()` and `isIncludeContainedElements()`, reject it.

`TopicMask.matches(Topic)` returns false for null. Ordinary matching checks
depth before the first field. Exact containment requires the same depth.
Inclusive containment permits additional trailing entries. At each position,
check qualifier, value, then any version constraint, with the first failure
ending the comparison. A Topic supplies ordered entries and has no
mask-evaluation API. Version-sensitive matching may distinguish equal Topics.

### Qualifier and value patterns

Evaluate against the complete decoded field, case-sensitively, preserving
literal Unicode text without normalisation or case folding. An unescaped
`*` consumes zero or more characters. A scan of the original pattern recognizes
`\*` as a literal asterisk and `\\` as one literal backslash. A backslash before
any other character, or at the end, is literal. Process those pairs from left
to right. All remaining characters are literal, including `.`, `?`, `+`, braces,
parentheses, brackets, pipes and characters resembling flags or backreferences.
The wildcard limit counts unescaped operators. There is no regex evaluation,
translation to regex, RE2/J dependency or alternate matching engine.

| Raw pattern | Matching rule |
| --- | --- |
| `Patient` | Text equality. |
| `Patient*` | Text starts with `Patient`. |
| `*Patient` | Text ends with `Patient`. |
| `*Patient*` | Text contains `Patient` as a contiguous substring. |
| `Patient*Record` | Text starts with `Patient` and ends with `Record`, with enough text for both literal parts without overlap. |
| `*` | Any valid text in this field. |

These are the supported operator placements. All literal parts shown in the
table are nonempty after wildcard decoding. Reject adjacent operators such as
`**`, split two-operator forms such as `Pa*ti*ent`, and forms such as
`*Patient*Record` and `Patient*Record*`. Thus two operators are admitted only
around a nonempty literal. A one-operator prefix/suffix pair can meet with an
empty middle, as in `PatientRecord`, and `AB*BC` fails against `ABC`. Null
qualifier/value patterns are invalid. An explicitly empty text pattern has the
exact-match interpretation and cannot match a valid nonempty Topic field.
Pattern validation requires well-formed Unicode, the operator grammar and the
applicable size bounds. Completed Topic fields remain subject to §1 regardless
of which pattern is used.

Validate each pattern once during construction or reconstruction. Retain its
original text for equality and serialization, together with an immutable
internal description of its shape and decoded literals. Use equality,
prefix/suffix checks and bounded substring search. A straightforward substring
search may compare at most the field length times the literal length in character
units. Enforce field and pattern limits before those operations. Perform no
recursive matching, backtracking or repeated wildcard expansion.

Unrestricted qualifier or value helpers supply raw `*`. The `any()` helper
supplies `*` for both and leaves version unconstrained, producing an ordinary
criterion. Literal helpers first validate completed field values and escape
literal backslashes and asterisks in the raw pattern. For example, literal helper
input `Patient*` produces `Patient\*`, which matches a literal final asterisk.
The actual escaped pattern must fit the pattern limit before serialization.
Mask builders append immutable entries, validate before mutation, preserve
prior state on failed operations and return independent immutable snapshots.
They generate no qualifiers, and callers confine builder use to one thread or
coordinate access.

### Pending version matching

Topic versions remain optional opaque strings with the §1 rules. The proposed
direction is numeric comparisons (`=`, `<`, `<=`, `>`, `>=`) and bounded ranges.
The operators are candidates. Decisions about the numeric domain, compatibility
and interchange are in §9. Omitting a mask's version constraint leaves that
position unconstrained. The structural distinction between omission and a
supplied empty constraint must be carried into the decision, including explicit
treatment if empty constraints cease to be valid. An unresolved or unsupported
constraint must never be interpreted as unconstrained. Constrained matching and
its representations require adjudication before implementation. The proposal
offers no regex fallback for version matching.

## 5. Canonical mask expressions and logical interchange

The ordinary text-mask envelope retains the ordered
`qualifierPattern[:versionConstraint]=valuePattern` syntax and the separately
supplied containment flag. The optional version segment shows the existing
structural slot. Its accepted successor syntax and encoding remain open under
§9. In this draft the fully specified text cases omit it.

Construct canonical text from the **original raw pattern**, before interpreting
its wildcard operators. For the canonical layer, escape each `.`, `=`, `:` and
`\` with one backslash. Separate entries with unescaped periods. The parser
checks the envelope while scanning, requires one unescaped equals sign and at
most one unescaped colon before it, and rejects empty path/entry segments, extra
or misplaced delimiters, dangling canonical escapes and unknown canonical
escapes. Strip exactly one canonical escaping layer to recover the original
pattern. Only then scan the raw wildcard escapes and classify operators. The
canonical layer leaves `*` alone. The Topic parser in §3 uses its completed
literal-field grammar independently.

The examples below show raw text and canonical text, outside Java string
literal syntax.

| Value pattern supplied | Entry with qualifier `Role` | Meaning |
| --- | --- | --- |
| `Patient*` | `Role=Patient*` | Starts with `Patient`. |
| `Patient\*` | `Role=Patient\\*` | Exactly `Patient*`. |
| `A.B:C=D` | `Role=A\.B\:C\=D` | Exactly `A.B:C=D`. |
| `A\\B` | `Role=A\\\\B` | Exactly `A\B`. |

An ordinary mask reconstructed from its proposed canonical expression and
containment flag must be structurally equal, preserve the original pattern
strings and re-emit the same canonical expression. For example, `Role=` is a
valid nonmatching criterion with an empty value pattern. `toString()` renders
the ordinary canonical expression. A universal mask has a distinct logical
record and may use separate descriptive display text. Canonical strings may
carry sensitive field data and are unsuitable for diagnostics.

Logical interchange preserves ordered completed TopicEntry records and their
versions. For masks it preserves kind, ordered original ordinary pattern data,
containment and agreed version presence. Universal interchange contains neither
criteria nor containment. Reconstruction validates the completed data and
rebuilds immutable matching descriptions. It does not apply defaults to
completed qualifiers. XML preserves the current strict UTF-8 record boundary,
deterministic writing, ordered fields, and reversible UTF-8 Base64 for the
original text pattern payloads. Decode XML structure, then Base64 and strict
UTF-8, then the raw wildcard layer. XML carries the raw pattern and does not
apply the canonical backslash layer. The version payload and manner of
identifying the successor mask format require review. Reject conflicting
encoding declarations, non-XML whitespace in element-only content, unknown
structure, DTDs and external resolution. Keep a present-empty legacy constraint
distinguishable from omission in any authorised legacy reader or migration.

### Compatibility with existing selectors

Use explicit, governed format or source correspondence to distinguish persisted
regex selectors from the proposed wildcard selectors. The same text can change
meaning, for example `A.B` or `A*`, and a successful read cannot identify its
original dialect. Decide the format discriminator and handling of bare
canonical expressions before deploying a new reader. Exclude heuristic fallback
and automatic reinterpretation of legacy selector text or XML.

Map historical Exact and Inclusive depth flags only with evidence of equivalent
criteria and selection scope. Retain registry and historical UUID-backed
references or use an authorised migration. Reconcile name-only selectors with
the ordered qualifier/value/version structure. Use `TopicMask.all()` only when
the unrestricted intent was established. Prove equivalent selection for any
migrated selector with persisted fixture or consumer replay, particularly
security-sensitive and routing cases. Reject unresolved or ambiguous inputs
without widening selection. [F-01 reconciliation][reconciliation]

## 6. Resource limits, concurrency and diagnostics

Carry forward the applicable release limits from accepted Backlog Item 0003
revision 000.003 as the draft's verification baseline. These include depth 16,
decoded Topic fields of 64/256/64 code points for qualifier/value/present
version, a 512-code-point limit per original raw pattern (including literal
helper output), 32 KiB for Topic canonical text, 128 KiB for ordinary mask
canonical text and 256 KiB for a single XML record, measured in UTF-8 bytes.
Confirm any new version-constraint limits when that model is settled. Measure
well-formed decoded field and pattern code points before canonical escaping.
Treat serialized-byte ceilings separately. [Accepted size limits][limits]

Enforce depth before growing storage or materialising a seventeenth entry.
Bound temporary allocations during outer scanning, Base64 decoding and
construction. Keep at most two operators and a bounded number of literal
fragments per pattern. For a 16-entry ordinary mask perform at most 32
qualifier/value field evaluations, with at most one additional bounded version
evaluation per entry if approved. Match invocations keep their own state and
can share immutable descriptions. Apply short-circuit evaluation and avoid a
global match-result cache. Return false on unexpected evaluation failure and
preserve the outcome if diagnostics fail.

The proposed model removes RE2/J and the regex-specific lexical scanner,
group nesting limit, counted-repetition limit, expansion estimates and
compiled-program instruction limit. The existing candidate and its review
remain governed by the approved RE2/J contract until a different design is
accepted.

Keep a small typed validation outcome for construction, parsing and
reconstruction errors. A supported caller receives a stable category with
bounded context such as operation, field kind and a valid zero-based index
when known. Report the actual boundary and use an absent index when a
position is unavailable or invalid. Distinguish invalid input, limits,
mismatch and unexpected evaluation failure. Use safe context such as depth
and last checked position and omit raw fields, paths, patterns and payload-bearing
exception text. Validation fails before mutation or publication of a result.

Preserve the qualified standalone diagnostics scope. A narrowly scoped
package-local handler could receive safe comparison-exit metadata synchronously.
With diagnostics disabled, skip diagnostic data construction and dispatch.
The invocation owns its state and a handler failure cannot change the
matching outcome. Any production handler contract, injection scope, severity
mapping or supported diagnostic API needs Solution Architect disposition.
Calliope adds no logging framework, shared mutable sink, worker, queue,
buffer or remote delivery mechanism. Actual application TRACE and ERROR
integration, console independence, recovery, bounded caller latency and
reliable delivery remain governed operational logging obligations.
[Standalone qualification][diagnostics] · [Logging strategy][logging]

Ordinary diagnostics reflect comparison and validation outcomes. Themis
governs security policy and applicable mandatory auditing, with authoritative
capture under Kleio. Calliope comparison diagnostics supply no authority or
audit substitute.

## 7. Consumer boundaries

TopicMask provides a neutral selector. A successful ordinary or universal match conveys no authority to access, disclose, publish, route, execute or persist. Themis evaluates authorisation with the applicable Passport, action and other policy criteria. InformationUnitMask, OperationalMetadataMask and PetasosParcelMask can compose TopicMasks for Topic-bearing criteria. Subscription, publication and filter statements compose criteria and own their feature-level rules. Routing and capability discovery retain their distinct eligibility, availability and coverage rules.

### Information Unit Topic designation

The governed IU Topic designation `harmonia.mneme.iu.<hex-string-of-compressed-uuid>` is the unqualified `Topic.getName()` projection of a structured runtime Topic. Define the distinct fully qualified canonical representation using explicitly governed qualifiers. The exact sequence and its boundary mappings require a separate design decision. Existing implementation spellings and illustrative qualifier sequences supply no accepted vocabulary.

The IU UUID remains the authoritative IU identity. TopicEntry versions carry their own meaning independently of the authoritative IU revision, Mneme operational revision and active generation, Content Structure Version and content-native versions. Preserve the established designation across persistence, transport, messaging, matching and references, or prove an explicit migration with approved boundary mappings and legacy Topic UUID treatment. [IU identity and revision contract][iu] · [R3][reconciliation]

### R3 representation-content matching

Potential richer matching against
`InformationUnit.InformationRepresentation.type` or
`InformationUnit.InformationRepresentation.content.data` is outside the R1/R2
TopicMask selector. Neither a content evaluator nor a general expression
abstraction belongs in this Naming change. If an owning feature later establishes
a need, review a separate matching contract with its own semantics, input bounds,
authority, recovery and conformance evidence.

## 8. Supported surface and package containment

Carry forward the six primary model types, all Topic, TopicEntry and
TopicBuilder contracts in [Final release 002 §8][baseline-api], and the stable
TopicMask operations for builders, explicit universal construction, ordinary
canonical reconstruction, entries, containment and Boolean matching. Retain
immutable values, factory-led construction and the existing accessor exception
contracts. Retain the necessary validation exception and small, safe metadata
contract. Preserve four XML conversion operations, with the codec responsible
for interchange and the completed value types responsible for domain invariants.

Propose replacing regex-named text operations and accessors with pattern
terminology at the existing factory and builder boundaries. Keep the two-field
construction and literal/unrestricted helper roles. Approve exact public
signatures and any caller-transition plan alongside the version-constraint
contract. The published type and signatures for a version constraint, including
possible text and literal helpers, remain open. Preserve independent qualifiers
and values, optional version-constraint presence and contextual failure semantics.

Review each package alongside Java visibility. Prefer package-private
constructors behind accepted factories, and package-private parsing, matching,
diagnostic and limit machinery. Place match-only records and enums near the
evaluator. Expose only the metadata deliberately required by supported callers
and keep exception construction helpers internal. Co-locating the codec with
strict completed-entry construction is a candidate that preserves separate
class responsibilities. Keeping an interchange subpackage needs a justified
minimal cross-package contract. Avoid public access façades introduced solely
to bridge internal packages. Require an exact inventory of supported types and
members, package/visibility decisions and consumer uses before freezing the
API. [Independent finding and API inventory][independent-api]

## 9. Decisions for Solution Architect adjudication

| Matter | Decision required |
| --- | --- |
| Text language | Confirm six shapes, non-overlapping prefix/suffix, empty qualifier/value pattern behaviour, the two backslash pairs and literal treatment of other backslashes. Confirm the outer grammar and examples before assigning implementation. |
| Numeric version domain | Choose integers, dot-separated numeric components or another representation. Define accepted syntax and bounded digits/components. Decide signs, leading zeroes and whether spellings with the same numeric value retain distinct original text. Topic itself keeps its opaque stored version. |
| Version comparisons and ranges | Define how components compare, including differing component counts, trailing zeroes and the comparison type for `=`. Set overflow-safe bounds and specify range syntax, endpoints, inclusivity, reversed/empty ranges and any candidate operators to exclude. No ordering is established by this draft. |
| Existing nonnumeric versions | Decide which constraint kinds can evaluate them, and what a numeric constraint does when the field is nonnumeric. Require explicit compatibility and migration treatment for governed existing versions. |
| Absent version | Decide how absent candidate versions interact with each constraint kind and whether any constraint explicitly selects absence. Omission of the constraint remains unconstrained. A failed conversion or unsupported constraint must never broaden selection. |
| Text and wildcard versions | Decide whether exact-text or wildcard version constraints remain available and how a reader distinguishes them from numeric constraints. If either form is retired, specify the treatment of established selectors and helpers. |
| Version interchange and empty constraint | Choose the canonical grammar and lossless XML representation, format discrimination, raw text preservation and API representation. Explicitly distinguish absent and empty constraints. Decide whether present-empty persists as an accepted nonmatching constraint or is rejected in the new format with explicit legacy treatment. |
| Legacy selector format and migration | Decide how to identify old canonical and XML mask formats, which selectors are safely convertible and what to do with unresolved ones. Demonstrate equivalent selection and delivery of required registry and reference migrations before dependent use. |
| Supported API and diagnostics | Decide exact model/codec surface, placement and Java visibility, exception metadata and exit-index convention. Decide whether to approve a local handler and its scope and whether any new public diagnostic contract is warranted. Resolve operational logger compatibility and any reliability/latency conflict separately. |
| Continuum and software correspondence | Decide the applicable Architecture transition and exact ADD scope for the TopicMask definition, selection association and direct dependencies. Reconcile the proposed mechanism with the existing qualified design, 1.1.25 incorporation and separately reviewed 1.1.28/001 DR-2/DR-6 exact candidate. Preserve accepted element and relationship identities. Decide the next Backlog Item 0003 and Software HCRL 010 treatment only against adjudicated architecture. Resolve release-specific conformance, legacy references, IU vocabulary/boundary mappings and the pre-existing module dependency question in their governed scopes. |

### Architectural correspondence

| Axiom | Draft treatment |
| --- | --- |
| [AX-04][ax04] | Harmonia defines six matching shapes and uses bounded platform string operations. This proposes a different matcher contract from the earlier RE2/J design. |
| [AX-06][ax06] · [AX-07][ax07] · [AX-08][ax08] | Matching supplies only a selector outcome. Themis retains policy evaluation and Kleio authoritative audit evidence. Diagnostics carry safe operational context. |
| [AX-10][ax10] · [AX-11][ax11] | Bound parsing, matching, transient work and optional diagnostics, and preserve fail-closed outcomes under failure and load. |
| [AX-12][ax12] | Keep domain access fluent and keep mechanics inside the Naming boundary, with deliberate adapter and validation surfaces. |
| [AX-14][ax14] · [AX-15][ax15] · [AX-17][ax17] | Preserve Topic identity, version and constraint presence, selector kind and failure distinctions. Put uncertain legacy references, source authority, variant formats and open version/diagnostics choices before the Solution Architect. |

The current Continuum retains the historical common-model identity and selector
contracts incorporated by [Architecture 1.1.25 DR-3][rad]. The qualified runtime
design and the separately reviewed [1.1.28/001 candidate][alignment] record the
differences. An accepted design revision needs its own disposition and exact
Continuum correspondence before dependent acceptance.

## 10. Behavioural verification for a future implementation

| Area | Required coverage |
| --- | --- |
| Preserved Topic and builder contracts | Retain the Topic-focused tests in [Final release 002 §10][baseline-tests]. Compare actual ordered qualifier/value/version data, generated/explicit qualifier equivalence, navigation, character validation, equality/hash/order, strict names, both modes, recovery and snapshot isolation. |
| Ordinary and universal selectors | Exercise all six shapes, case and code-point preservation, literal punctuation and flags, empty patterns, escaped stars and backslashes, rejected operator counts/placements and insufficient non-overlapping text. Assert positional order, depth-first and first-field short circuits, exact and inclusive containment, null handling, explicit universal round-trip and ordinary-only accessors. |
| Canonical strings and XML | Use independent expected raw, canonical and XML fixtures and validate matching after reconstruction. Test both escaping layers and separately test XML raw pattern Base64 and UTF-8. Check deterministic bytes, unknown or misplaced fields, invalid completed qualifier, contradictory declarations, XML whitespace, DTD and external resolution, limits and stable boundary diagnostics. |
| Version contract | Once adjudicated, exercise every operator, endpoints and syntax boundaries, nonnumeric and absent inputs, omitted versus empty constraints, equality independent of stored Topic versions and round trips through both canonical and XML forms. |
| Bounded and concurrent evaluation | Check below/at/above depth, field, pattern, byte and allocation bounds. Verify early depth rejection in both parsers, maximum-field and maximum-depth late mismatch, bounded mixed-outcome concurrency, no global cache, and fail-closed evaluation/handler failures with safe metadata. |
| Compatibility and conformance | Replay governed references, supported legacy formats and consumer scopes without widening selection. Verify approved dependency scope, public API and package visibility, implementation documentation and exact candidate test accounting. Record applicable integrated and architecture verification, operational logging obligations and dispositions for any required missing coverage. [Independent review][independent] |

[baseline]: DRP-Topic-and-TopicMask.Final.001.001.md
[baseline-api]: DRP-Topic-and-TopicMask.Final.001.001.md#8-accepted-java-surface
[baseline-tests]: DRP-Topic-and-TopicMask.Final.001.001.md#10-mandatory-behavioural-acceptance
[filenames]: ../evidence/1.1.28/001/after/topic-drp-filename-inventory.md
[review]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[acceptance-boundary]: ../review/software-0.1.0/hcrl-software-0.1.0-007.pending.md
[reconciliation]: ../evidence/software-0.1.0/004/after/F-01-Architecture-Reconciliation.md
[architecture-naming]: ../../governance/canonical-identity-and-naming.md#architectural-identity
[iu]: DRP-Information-Unit-Framework.Final.002.md#2-identity-and-distinct-revisions
[logging]: DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md#2-architectural-decisions-and-constraints
[limits]: ../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md#approved-dependency-scope-and-resource-limits
[diagnostics]: ../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md#accepted-standalone-diagnostics-qualification
[independent]: ../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md
[independent-api]: ../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#package-containment-and-api-review
[rad]: ../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3
[alignment]: ../review/1.1.28/hcrl-1.1.28-001.pending.md#dr-2
[ax04]: ../../governance/architectural-axioms.md#ax-04
[ax06]: ../../governance/architectural-axioms.md#ax-06
[ax07]: ../../governance/architectural-axioms.md#ax-07
[ax08]: ../../governance/architectural-axioms.md#ax-08
[ax10]: ../../governance/architectural-axioms.md#ax-10
[ax11]: ../../governance/architectural-axioms.md#ax-11
[ax12]: ../../governance/architectural-axioms.md#ax-12
[ax14]: ../../governance/architectural-axioms.md#ax-14
[ax15]: ../../governance/architectural-axioms.md#ax-15
[ax17]: ../../governance/architectural-axioms.md#ax-17
