<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Package — Topic and TopicMask

**Standing** · Draft.002.003 · Proposed for Solution Architect review. This is
the third draft based on accepted DRP release 002, with possible successor
Final.003. Architecture 1.1.26 governs. The software 0.1.0-SNAPSHOT candidate
remains unaccepted.

**Design lineage** · [Draft.002.002][previous-draft] is the immediate baseline.
The [qualified accepted release 002 content][baseline] remains at the original
Final.001.001 locator under [Software HCRL 004 DR-10][review] and
[007 finalisation][acceptance-boundary]. The [filename inventory][filenames]
records the pending reconciliation.

The [Solution Architect's revision instruction][revision-task] sets the range
validation, version-constraint spelling and successor XML fields for this
draft. Section 9 records the remaining decisions. The accepted Topic, builder,
canonical naming and consumer contracts carry forward in §§1–3 and 7.
R3 representation-content matching remains outside the Naming scope.

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

### Version matching

A TopicEntry retains its original optional opaque string version under §1.
Construction, navigation, storage and interchange of a Topic preserve the
string without numeric interpretation. Only TopicMask evaluates numeric versions,
and only when it reaches a numeric constraint in the ordered comparison.
Version matching leaves Topic identity, equality, hashing and ordering intact.

| Constraint after outer decoding | Meaning |
| --- | --- |
| Omitted | Unconstrained for either absent or present candidate versions. |
| Present and empty | Valid, always a non-match. |
| `1.2` | Exact case-sensitive string equality, including the spelling. |
| `=1.2` | Numeric equality. |
| `<1.2` or `<=1.2` | Numeric less than or less than or equal. |
| `>1.2` or `>=1.2` | Numeric greater than or greater than or equal. |
| `>=1.2,<2.0` | Bounded numeric range. Both comparisons must succeed. |

An absent candidate version matches only an omitted constraint. A present
nonnumeric version can match exact text and fails numeric comparisons and
ranges. A present-empty constraint fails for every candidate. A literal
asterisk in an exact version constraint matches a literal asterisk in the
stored version. The six wildcard forms apply exclusively to qualifier and
value. No semantic-versioning, prerelease or compatibility ordering is implied.

The original constraint text and its presence participate in structural
TopicMask equality and hashing. Masks carrying `=01.002` and `=1.2` can
select the same versions while remaining distinct masks. Omission and
present-empty are distinct through construction, rendering and interchange.
A missing candidate version never enters the numeric comparator.

#### Numeric syntax and ordering

A numeric operand or candidate consists of one or more nonempty ASCII digit
components separated by single periods. Components represent nonnegative
integers. Permit leading zeroes. Reject numeric signs, leading or trailing
periods, empty components, spaces, exponents, non-ASCII digits and alphabetic
suffixes. A period separates components. The text `1.10` therefore has the
components one and ten. Topic version strings that fail this grammar retain
their ordinary exact-text use.

Compare corresponding components from left to right. Ignore leading zeroes
for comparison. Compare each component by its significant digit length, then
lexicographically by its ASCII digits if the lengths agree. An all-zero
component has value zero. After one version runs out of components, compare
any remaining components against zero. The first unequal component decides
the result. Compare bounded text spans without numeric conversion.
Keep the original spellings for access, storage and round trips.

| A | B | Numeric relationship |
| --- | --- | --- |
| `1.2` | `1.2.0` | Equal |
| `1.2.0` | `1.2.0.0` | Equal |
| `01.002` | `1.2` | Equal |
| `1.10` | `1.9` | Greater |
| `1.2.1` | `1.2` | Greater |
| `2` | `2.0.0` | Equal |

| Stored candidate version | Constraint | Result |
| --- | --- | --- |
| `1.2-beta` | `1.2-beta` | Match |
| `1.2-beta` | `>=1.2` | No match |
| `1.2.0` | `1.2` | No match |
| `1.2.0` | `=1.2` | Match |
| `1.2.0` | `>=1.2,<2.0` | Match |
| `2.0.0` | `>=1.2,<2.0` | No match |

#### Bounded ranges and validation

A range contains exactly two comparisons. Both endpoints must satisfy the
numeric syntax and bounds. The first comparison is a lower bound (`>` or
`>=`) and the second is an upper bound (`<` or `<=`). A range is valid when
the lower endpoint is numerically less than the upper endpoint, or when the
two endpoints are numerically equal and both comparisons are inclusive.

Reject malformed endpoints, missing or duplicate bounds, reversed direction
or ordering, and equal endpoints with either comparison exclusive.
Construction ends after these endpoint checks. Correctly ordered, distinct
endpoints are valid even when the version field limit leaves no candidate
that can satisfy both bounds. Matching tests the actual candidate version
against both comparisons and requires both to succeed.

| Range | Construction result |
| --- | --- |
| `>=1.2,<2.0` | Valid. |
| `>1.2,<2.0` | Valid. |
| `>=1.2,<=1.2.0` | Valid numeric singleton. |
| `>1.2,<=1.2.0` | Invalid. Equal endpoints with an exclusive lower bound. |
| `>=1.2,<1.2.0` | Invalid. Equal endpoints with an exclusive upper bound. |
| `>=2.0,<1.2` | Invalid. Reversed endpoint ordering. |
| `>1.2,<1.2.0` | Invalid. Equal endpoints with both bounds exclusive. |
| `>=1.2,` | Invalid. Missing upper bound. |
| `>=1.2,>2.0` | Invalid. Duplicate lower bound. |
| `>=1.x,<2.0` | Invalid. Malformed lower endpoint. |

Validate and classify supplied constraints at mask construction, canonical
reconstruction and XML reconstruction. Keep the original constraint text
alongside the internal validated description. Reject invalid or unsupported
constraints before publishing an entry or mutating builder state. A failed
constraint validation never produces an unrestricted entry. During matching,
a syntactically nonnumeric candidate under a numeric constraint is an
ordinary mismatch. An unexpected evaluator failure also returns false
with bounded diagnostic treatment under §6.

## 5. Canonical mask expressions and logical interchange

Preserve the ordered ordinary mask envelope
`qualifierPattern[:versionConstraint]=valuePattern` and the separately
supplied containment flag. A missing colon is an omitted constraint.
A colon followed immediately by the entry's equals delimiter is
present-empty. Write that colon whenever the constraint is present.

Construct canonical text from the original qualifier, value and optional
raw constraint strings. Escape every literal `.`, `=`, `:` and `\` in each
field with one backslash. Separate entries using unescaped periods. The
parser requires one unescaped equals delimiter and at most one unescaped
colon before it within each entry. Reject empty paths and entry segments,
improper delimiters and dangling or unknown canonical escapes. The outer
scan strips exactly one canonical escaping layer before validating the
decoded fields. The qualifier and value then use the §4 wildcard rules.
The version constraint uses the raw lexical rules below. The Topic
parser in §3 continues to use the completed literal-field grammar.

### Version constraint spelling

On the raw constraint text recovered after outer canonical decoding, use
constraint presence and the first character to choose the interpretation.

- Omitted and present-empty have the distinct behaviours in §4.
- An initial `=`, `<` or `>` selects the numeric grammar. Read `<=` and `>=`
  as complete operators before testing the one-character forms. A numeric
  expression contains one comparison or exactly two comparisons with a
  single comma between them. In a range, the lower comparison comes first and
  the upper comparison second. Numeric expressions contain no whitespace.
- An initial backslash is an exact-text escape only before one of
  `=`, `<`, `>` or a backslash. Remove that one leading backslash and match
  the remaining version literally. Reject any other use of the reserved
  leading escape. Interior and trailing backslashes are ordinary text.
- Otherwise, the entire nonempty string is exact version text. A comma
  within exact text is literal. The comma separates endpoints only after a
  numeric operator has selected the numeric grammar.

A malformed numeric form such as `>=1.x`, `>= 1.2`, `>=1.2,>2` or
`=1.2,<2.0` is a validation error. It cannot fall through to exact text.
To match a stored literal beginning with an operator or backslash, apply
the leading exact-text escape. Validate exact version text under the completed
version rules in §1. Preserve the original raw constraint, including any
leading escape. The outer canonical layer escapes that raw text independently.
Decode the outer layer first and interpret the raw leading escape second.

Examples below use raw and canonical text outside Java string literal syntax.

| Value pattern supplied | Entry with qualifier `Role` | Meaning |
| --- | --- | --- |
| `Patient*` | `Role=Patient*` | Starts with `Patient`. |
| `Patient\*` | `Role=Patient\\*` | Exactly `Patient*`. |
| `A.B:C=D` | `Role=A\.B\:C\=D` | Exactly `A.B:C=D`. |
| `A\\B` | `Role=A\\\\B` | Exactly `A\B`. |

| Raw version constraint | Canonical entry for qualifier `Role` and value `LAB` | Meaning |
| --- | --- | --- |
| Omitted | `Role=LAB` | Unconstrained. |
| Present empty | `Role:=LAB` | Always a non-match for the version check. |
| `1.2` | `Role:1\.2=LAB` | Exact text `1.2`. |
| `=1.2` | `Role:\=1\.2=LAB` | Numeric equality with `1.2`. |
| `>=1.2,<2.0` | `Role:>\=1\.2,<2\.0=LAB` | Bounded numeric range. |
| `release,hotfix` | `Role:release,hotfix=LAB` | Exact text containing a comma. |
| `\=1.2` | `Role:\\\=1\.2=LAB` | Exact text `=1.2`. |
| `\<ci` | `Role:\\<ci=LAB` | Exact text `<ci`. |
| `\>ops` | `Role:\\>ops=LAB` | Exact text `>ops`. |
| `\\build` | `Role:\\\\build=LAB` | Exact text `\build`. |

Canonical reconstruction with the containment flag preserves structural
equality and re-emits the same expression. For example, `Role=` remains
a valid nonmatching criterion with an empty value pattern. Ordinary
`toString()` renders the canonical expression. Universal masks use a
distinct logical record and may use distinct descriptive display text.
Canonical renderings may expose sensitive data and are unsuitable for
diagnostics.

### Logical records and XML encoding

Logical Topic interchange preserves completed entries in order and the
original optional versions under §1. An ordinary mask record preserves
mask kind, containment, entry order and the original qualifier and value
patterns. Each entry has an optional original raw constraint string which
can be present and empty. The decoded raw string determines the constraint
category. Derived comparison state stays internal. A universal record
contains neither criteria nor containment.

Use the existing strict UTF-8 XML envelope and reversible UTF-8 Base64 for
raw ordinary fields. Each ordinary successor mask entry carries
`qualifierPattern` and `valuePattern` elements, followed by an optional
`versionConstraint` element. Each has `encoding="base64"`. Omit the constraint
element for no constraint. For present-empty, emit
`<versionConstraint encoding="base64"></versionConstraint>`. For numeric
equality `=1.2` and exact text `=1.2` respectively, emit the following
different raw payloads.

~~~xml
<versionConstraint encoding="base64">PTEuMg==</versionConstraint>
<versionConstraint encoding="base64">XD0xLjI=</versionConstraint>
~~~

The corresponding bounded range `>=1.2,<2.0` encodes as
`Pj0xLjIsPDIuMA==`. The writer carries the original raw text directly,
using canonical padded Base64 with no line breaks. It emits the same XML
bytes for the same logical record. The reader validates the selected XML
format and structure first, then Base64 and strict UTF-8 before applying
§4 pattern validation or the version lexical rule. XML applies no canonical
backslash escaping. A present element with zero decoded characters remains
present and empty. A completed Topic version element with empty data is
invalid under the separate Topic record contract.

Enforce ordered required fields and a single optional constraint. Reject
duplicate or unexpected structure, conflicting XML encoding declarations,
non-XML whitespace in element-only content and malformed payloads. Reject
DTDs and disallow external resolution, including external entities.
Reconstruct strict completed records, rejecting missing or invalid completed
Topic qualifiers before any builder default can apply. An omitted constraint
remains absent. Explicit format recognition for these successor records remains
open under §9.

### Compatibility with existing selectors

Use explicit, governed format or source correspondence to distinguish persisted
regex selectors from the successor wildcard selectors. The same text can change
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

Carry forward the release limits from accepted Backlog Item 0003
revision 000.003. Depth is 16 for a Topic or ordinary mask. Decoded Topic
qualifier, value and present version lengths are 64, 256 and 64 code points.
Each original qualifier or value pattern, including literal-helper output,
has a 512-code-point cap. Topic canonical text is capped at 32 KiB, ordinary
mask canonical text at 128 KiB and a single XML record at 256 KiB, measured
in UTF-8 bytes. Check decoded fields and pattern text after Unicode
well-formedness validation and before outer escaping. Byte limits on
serialized representations apply independently. [Accepted size limits][limits]

The version limits derive from the current 64-code-point Topic version
cap. An exact literal has at most 64 ASCII characters, or 65 in raw
constraint text with the leading escape. Each numeric operand has at most
64 ASCII characters including dots. This permits at most 32 components with
at least one digit each, and at most 64 digits in any one component. A single
comparison adds at most two characters. A range adds at most four operator
characters and one comma to two operands. Its raw constraint text is
therefore at most 133 ASCII characters, within the previous 512-code-point
per-expression cap. Apply the raw cap before building comparison state.
Check the individual operand bounds as well as the aggregate. A numeric
candidate is already bounded by the Topic field rule.

Bound temporary allocations during canonical scanning, XML decoding and
construction. Stop before materialising a seventeenth entry. Reject oversized
records and field encodings before allocating oversized decode buffers.
Keep the wildcard grammar's operator and fragment bounds. Scan each numeric
operand once, using bounded component spans if needed, with at most two
operand descriptions per constraint. For a range, compare the two validated
endpoints directly during construction. Use bounded string operations without
recursive parsing, numeric conversion, expansion or general expression
interpretation.

At depth 16, ordinary matching performs at most 32 qualifier/value checks
and 16 version checks. A version check scans a numeric candidate at most
once and compares against at most two operands. A first mismatch ends
evaluation. Match invocations use local state and can share immutable
descriptions. The model uses no global match-result cache. Return false
on unexpected evaluation failure and preserve the outcome if diagnostics fail.

The proposed model uses bounded string operations for text and version
matching. It removes the need for RE2/J and its lexical scanner, group
nesting limits, counted-repetition limits, expansion estimates and program
instruction limits. See the independent review treatment in §10.

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

Propose pattern terminology for the existing text factories and builders.
The following signatures illustrate the minimal change for ordinary entries.
The Solution Architect reviews the exact names, null-handling convention and
caller transition.

~~~java
// TopicMaskEntry
static TopicMaskEntry of(String qualifierPattern, String valuePattern);
static TopicMaskEntry of(String qualifierPattern, String valuePattern, String versionConstraint);
String getQualifierPattern();
String getValuePattern();
Optional<String> getVersionConstraint();

// TopicMaskBuilder
TopicMaskBuilder addPatterns(String qualifierPattern, String valuePattern);
TopicMaskBuilder addPatterns(String qualifierPattern, String valuePattern, String versionConstraint);
~~~

The two-argument overload omits the constraint. The three-argument overload
would retain `null` as another way to omit it and `""` as present-empty.
Every other argument is the raw constraint string from §5. The accessor
returns the original raw string, including any leading escape, using an
empty Optional for omission and a present empty string for present-empty.
The existing literal helpers retain their roles. A three-argument literal
helper would validate a supplied nonempty version as a Topic version literal
and apply the leading escape when required, so a version like `=1.2` is
matched as text. Keep the qualifier/value-only helper overloads and
unrestricted helpers.

`TopicMask.fromCanonicalExpression(String, boolean)` retains its ordinary
mask role and validates the decoded raw constraint. The mask XML reader
validates the same raw contract under the selected record format. Factories,
builders and both reconstruction paths should share validation semantics
and supply the actual operation, version-constraint field category and
available valid entry index to typed failures. An internal constraint
description and evaluator suffice. Callers need no public comparison type,
parser, operator enum or version framework.

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

This register records the decisions that remain open after the
[Solution Architect's revision instruction][revision-task].

| Matter | Decision required |
| --- | --- |
| Format recognition and migration | Establish explicit recognition of the successor XML format and the dialect of bare canonical expressions where source context is absent. Decide how to identify legacy regex selectors, which selectors and version spellings to migrate, and what evidence establishes equivalent selection. Preserve persisted selector, UUID and registry references as applicable. |
| Supported API and diagnostics | Confirm exact factory, builder, helper and accessor signatures, omission conventions and caller transition. Decide codec placement, Java visibility, exception metadata, exit-index convention and the optional local handler's scope. Review any public diagnostic addition explicitly. Operational logging reliability and latency remain separately governed. |
| Architecture and software sequence | Decide the applicable Architecture transition and exact ADD correspondence for the TopicMask definition, selection association and direct dependencies. Reconcile the 1.1.25 incorporation and the separately reviewed 1.1.28/001 DR-2/DR-6 candidate. Preserve architectural IDs and relationships. Govern the filename patches, next Backlog Item 0003 and Software HCRL 010 treatment through their own dispositions. Retain release-specific verification and evidence qualifications, IU qualifier vocabulary and boundary mappings, and the pre-existing module dependency question in their established scopes. |

### Architectural correspondence

| Axiom | Draft treatment |
| --- | --- |
| [AX-04][ax04] | Harmonia defines six text-matching shapes, exact version text, numeric component ordering and endpoint validity. Bounded platform string operations supply the machinery. |
| [AX-06][ax06] · [AX-07][ax07] · [AX-08][ax08] | Matching supplies only a selector outcome. Themis retains policy evaluation and Kleio authoritative audit evidence. Diagnostics carry safe operational context. |
| [AX-10][ax10] · [AX-11][ax11] | Bound parsing, endpoint validation, candidate comparison, transient work and optional diagnostics, and preserve fail-closed outcomes under failure and load. |
| [AX-12][ax12] | Keep domain access fluent and keep mechanics inside the Naming boundary, with deliberate adapter and validation surfaces. |
| [AX-14][ax14] · [AX-15][ax15] · [AX-17][ax17] | Preserve Topic identity, version and constraint presence, selector kind and failure distinctions. Preserve uncertainty about legacy references, source authority, format recognition and API choices for Solution Architect disposition. |

The current Continuum retains the historical common-model identity and selector
contracts incorporated by [Architecture 1.1.25 DR-3][rad]. The qualified runtime
design and the separately reviewed [1.1.28/001 candidate][alignment] record the
differences. An accepted design revision needs its own disposition and exact
Continuum correspondence before dependent acceptance.

## 10. Behavioural verification for a future implementation

| Area | Required coverage |
| --- | --- |
| Preserved Topic and builder contracts | Retain the Topic-focused checks in [Final release 002 §10][baseline-tests]. Compare actual ordered qualifier, value and version data, generated and explicit qualifier equivalence, navigation, character validation, identity, hashing, ordering, strict names, builder modes, recovery and snapshot isolation. Exercise opaque version text, including numeric-looking and nonnumeric versions. |
| Ordinary and universal selectors | Exercise the six shapes, case and Unicode preservation, literal punctuation, empty patterns, escaped stars and backslashes, rejected wildcard placements and non-overlapping prefix/suffix. Assert depth-first and field-order short circuits, exact and inclusive containment, null handling and explicit universality. |
| Version evaluation | Exercise all single operators, leading zeroes, unequal component widths and counts, absent and nonnumeric candidates, exact versus numeric equality and exclusive/inclusive boundaries. Include correctly ordered distinct endpoints, closed singletons, reversed ordering, equal endpoints with either bound exclusive, and malformed, missing or duplicate bounds. Check rollback after rejected constraints. |
| Canonical strings and XML | Use independent expected canonical and XML fixtures covering omission, present-empty, operators, escaped leading text, backslashes and literal/range commas. Compare exact raw payload and structural equality after reconstruction, including different spellings with equal numeric effect. Verify deterministic records and rejection of malformed fields, encoding conflicts, improper XML whitespace, duplicates, DTDs and external resolution. |
| Bounded and concurrent evaluation | Check below, at and beyond all relevant depth, field, operand, component, raw and encoded bounds. Verify early depth rejection, maximum-field and maximum-depth late mismatch, mixed-outcome concurrency with time-bounded tests, and both disabled and failing diagnostics. |
| Compatibility and conformance | Replay governed legacy references and selector scopes under their identified formats. Verify dependencies, public surface, documentation, runnable coverage and exact test accounting. Preserve applicable integration, architectural and operational logging evidence and qualifications. |

### Independent software review obligations

The [Software 010 independent review][independent-findings] remains
applicable to the exact existing software candidate. Carry the following
obligations into the next separately authorised implementation assignment.

- Findings 2–3 require strict completed-record reconstruction, rejection of
  invalid qualifiers without builder defaults, XML encoding consistency and
  structural whitespace and kind validation.
- Findings 4–5 require early depth enforcement in both canonical parsers
  and safe, correct failure metadata at XML, canonical and builder
  boundaries, including contextual version-constraint failures.
- Findings 6–8 require deliberate API and package exposure, accurate
  execution counts and coverage, and documentation aligned with supported
  behaviour. Preserve the distinction between the 36 independently verified
  tests and the unsupported aggregate count of 72. Carry forward the
  missing integrated verification and logging dispositions.
- Finding 1 concerns the pinned RE2/J implementation and its lexical
  safeguards. Preserve the finding in the retained candidate's review.
  Retire these obligations for a successor candidate only after acceptance
  of both the replacement architecture and the revised Backlog Item.

[previous-draft]: DRP-Topic-and-TopicMask.Draft.002.002.md
[revision-task]: ../evidence/1.1.28/001/before/ai-agent-tasks/20261124T220017Z-topic-drp-draft-002-003.md
[independent-findings]: ../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#findings-and-required-treatment
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
