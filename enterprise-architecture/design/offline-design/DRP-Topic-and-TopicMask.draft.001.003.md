<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Package — Topic and TopicMask

- **Standing** · Draft.001.003, pending Solution Architect review
- **Evidence boundary** · software-0.1.0, iteration 004
- **Scope** · Common Topic and TopicMask model, matching and diagnostics
- **Design lineage** · [Accepted Final.001.000](DRP-Topic-and-TopicMask.Final.001.000.md), [Draft.001.001](DRP-Topic-and-TopicMask.draft.001.001.md), [immediate predecessor Draft.001.002](DRP-Topic-and-TopicMask.draft.001.002.md)

This candidate consolidates F-01.1–F-01.81 with their explicit supersessions. The accepted Architecture Continuum remains authoritative for existing contracts. Architectural differences and implementation obligations remain visible in the [reconciliation][reconciliation]. The [decision delta][delta] records incorporation, and [DR-10][review] requests review of this exact draft. [F-01.1–F-01.44][initial] · [F-01.45–F-01.74][continuation] · [F-01.75–F-01.81][latest]

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

## 4. TopicMask kinds and matching

A TopicMask is an immutable structural matching specification with two logical kinds.

| Kind | Logical data and behaviour |
| --- | --- |
| Ordinary | At least one ordered criterion, plus `includeContainedElements` (default false). Each criterion has independent qualifier and value RegEx expressions and an optional version RegEx expression. Equality and hashing use the ordered original expression strings, version-constraint presence and containment flag. |
| Universal | Explicit universality, constructed through `TopicMask.all()`. Matches every non-null Topic. It has no criteria and containment is inapplicable. All universal masks compare equal to each other and hash consistently. The logical kind survives reconstruction. |

The kinds remain distinct even when an ordinary criterion happens to cover every valid Topic. No RegEx tuple, wildcard or canonical-expression token carries universal meaning. TopicMask carries no UUID and defines no natural ordering.

`isUniversal()` identifies the explicit kind, and its entry view is empty. `getCanonicalExpression()` and `isIncludeContainedElements()` are ordinary-only and reject universal masks. Universal interchange uses the separate logical kind in §5.

The public operation is `TopicMask.matches(Topic)`. A null candidate always returns false. For an ordinary mask, check depth before evaluating any RegEx. False containment requires equal depth, while true containment allows extra **trailing** entries. At each position, evaluate qualifier, value and the supplied version constraint in that order. Every criterion must match the candidate entry at its own position. Return false on the first mismatch. Return true when every required field succeeds. Topic supplies the ordered entry data and has no mask-evaluation API.

Use RE2/J's full-field `matches()` on decoded field text. For example, `ADR` fails against `xADRx`, while `.*ADR.*` can match it. Matching is case-sensitive by default. A supported expression flag such as `(?i)` can change that for a field. Document the supported RE2/J dialect and exclusions, including backreferences and look-around. Java RegEx fallback is prohibited. Validate and compile original expressions when constructing immutable entries, including during parsing and logical reconstruction. Retain the original strings and share immutable compiled patterns. Matchers and other evaluation state are local to the invocation.

### RegEx and literal helpers

The `of(qRegex, vRegex)` RegEx factory leaves version unconstrained. The three-argument overload behaves as follows.

| Third RegEx argument | Candidate with absent version | Candidate with present version |
| --- | --- | --- |
| `null` | Accepted by this field | Accepted by this field |
| `""` | Fails the presence check | Empty expression fails to match a valid nonempty version |
| `".*"` | Fails the presence check | Full-field wildcard according to RE2/J |
| Any other valid expression | Fails the presence check | Evaluate the full field against the expression |

Absent, empty and wildcard constraints are structurally distinct through equality, hashing, canonical parsing and logical serialization. Null qualifier or value expressions are invalid. Empty qualifier and value expressions are valid RE2/J expressions and cannot match valid nonempty fields. `forValue(vRegex)` supplies `.*` for the qualifier, `forQualifier(qRegex)` for the value, and `any()` for both. These helpers leave version unconstrained. Their entries participate in the ordinary depth and position checks.

`ofLiterals(q,v)` leaves the version unconstrained. `ofLiterals(q,v,version)` requires a valid, nonempty version literal. Validate all supplied literals against the corresponding completed TopicEntry field rules and length limits before quoting for RE2/J. Null and empty literal qualifiers, values and supplied versions are invalid. Next apply the expression limits to the quoted RegEx. `forLiteralValue(value)` intentionally leaves the qualifier unrestricted, and `forLiteralQualifier(qualifier)` leaves the value unrestricted. For example, a literal `A.B` matches the dot itself, while direct RegEx `A.B` can match `AxB`. A literal `N1` is an ordinary qualifier. No mask entry factory or mask builder generates qualifiers or supplies a default-qualifier helper. An explicit literal qualifier or `addForLiteralValue(value)` covers the respective uses.

The fluent TopicMaskBuilder appends immutable entries, provides the corresponding RegEx and literal helpers, and configures containment. Reject invalid input before mutating state. On `build()`, validate depth and ordinary-mask invariants. Failed operations preserve builder state and previous results. Each successful build takes a defensive, immutable snapshot. Builders are reusable and need not be thread-safe. Immutable compiled patterns can be shared with other snapshots.

## 5. Canonical mask expressions and logical interchange

An ordinary mask serializes its expression path with two layers of syntax. Start with each **original** RE2/J expression. For the outer layer, prefix each `.`, `=`, `:` and `\` in that expression with one backslash. Then emit `qualifierRegex[:versionRegex]=valueRegex` at each position, joining entries with an unescaped period. The optional segment is determined by version-constraint presence; an empty supplied version expression emits a colon followed immediately by the equals sign. The containment flag is supplied separately.

A dedicated mask parser scans only for unescaped structural syntax and accepts escapes only for those four characters. Unescaped periods delimit entries. Require just one unescaped equals sign per entry and at most one unescaped colon before it. Reject extra or misplaced separators, empty entry segments, dangling or unknown escapes and an empty expression path. An entry such as `=` has two empty RegEx fields and is structurally valid. An entry such as `Role:=LAB` has a present empty version expression. Remove exactly one structural layer before RE2/J validation and compilation. The outer scanner treats RegEx grouping, flags, classes and escapes as decoded field content. The Topic parser uses completed literal-field rules and cannot parse mask expressions.

The example shows raw RegEx followed by canonical text. Backslashes are displayed as text, outside any Java string literal.

~~~text
Raw at position 0: qualifier Role, no version, value (LAB|MED)\.[A:=]
Encoded entry:     Role=(LAB|MED)\\\.[A\:\=]
Raw at position 1: qualifier (?i:Zone), version 2\.4, value .*
Encoded entry:     (?i\:Zone):2\\\.4=\.*
Canonical path:    Role=(LAB|MED)\\\.[A\:\=].(?i\:Zone):2\\\.4=\.*
~~~

For every ordinary mask, `TopicMask.fromCanonicalExpression(mask.getCanonicalExpression(), mask.isIncludeContainedElements())` must reconstruct a structurally equal mask and re-emit the same canonical expression. This includes original backslashes, groups, character classes, scoped flags, embedded structural delimiters, empty expressions and absent versus present version expressions. `toString()` returns the canonical expression for ordinary masks, with containment supplied separately for reconstruction. A universal mask may render descriptive text outside canonical expression syntax. Interchange uses its explicit logical kind.

Logical interchange preserves ordered completed TopicEntry records, including optional versions. For ordinary masks it carries the ordered raw expression records and containment flag, with empty version RegEx distinct from absence. For universal masks it carries the explicit kind with neither criteria nor containment. Reject ambiguous representations, including a purported universal mask carrying ordinary data. Decode to immutable validated values and rebuild compiled patterns from the original expression strings. The core logical contract remains format-independent. Adapters preserve the relevant distinctions. A transport format, Java serialization and transport-specific annotations are outside the core model contract.

### Compatibility with legacy selectors

Map legacy Exact to `includeContainedElements=false` and Inclusive to `true` only after establishing equivalent criterion basis and selection scope. Explicitly reconcile name-only criteria with positional qualifier/value/version criteria and retain the intended constraints and version-presence behaviour. Use `TopicMask.all()` only for established unrestricted intent. Invalid, empty or ambiguous ordinary input provides no basis for universality. Validate legacy Java RegEx with the pinned RE2/J version, remediate unsupported patterns and demonstrate equivalent selection across migrated inputs. Java RegEx fallback is prohibited.

Account for persisted masks, their former UUID references, and consumer-specific consequences. Invalid or ambiguous inputs fail closed during migration. A conversion must preserve selection scope, including for security-sensitive consumers, with evidence for exact-depth, descendant, deliberately unrestricted and rejected selectors. [R2][reconciliation]

## 6. Resource limits, concurrency and diagnostics

Each software release defines a maximum hierarchy depth for Topics and ordinary masks, separate maximum lengths for qualifier, value, version and RegEx, and feasible safeguards for RegEx compile and evaluation complexity. Enforce the applicable limits on construction, parsing and logical reconstruction. Bound original expressions before outer escaping and include the actual quoted expression when a literal helper is used. Reject malformed Unicode before length measurement. Numeric limits, expression measurement details beyond the pre-escaping rule, pinned RE2/J capabilities, complexity thresholds and error mapping need release-specific evidence. RE2/J avoids catastrophic backtracking. Length, depth and expression complexity still affect resource use.

A single immutable mask can match concurrently without external synchronization. Share immutable patterns and use local per-call matching state. Calliope has no global TopicMask-by-Topic Boolean-result cache. Consumers may reuse compiled masks. Any future result caching requires measured justification. Equal Topics with different versions can produce different match outcomes.

Matching remains a synchronous Boolean operation. An unexpected runtime evaluation error returns false and requires an ERROR LogEvent through the application logger. A diagnostics failure must never turn a mismatch or evaluation error into a positive result. Invalid mask construction fails exceptionally, with ERROR logging at the handling boundary. Normal success, mismatch and depth mismatch are eligible for conditional TRACE, with no requirement for synchronous remote delivery. Record the comparison exit point, including the zero-based index when a field was checked, the category and bounded safe context. Useful categories include depth, qualifier, value, required version absent, version mismatch and evaluation failure. Use safe metadata such as depth, position and checked field. Omit protected field values, raw paths, raw RegEx and exception content that could expose them. For review, use an absent exit index for outcomes before field evaluation and the last checked position for ordinary success.

Use Harmonia's existing application-logging abstraction with parallel Local-Logger console and in-JVM Log-Satellite paths. The logging framework owns buffering, backpressure, delivery acknowledgement and recovery. TopicMask adds no queue, worker thread or delivery subsystem, and the core model has no direct dependency on rsyslog, Log-Server or Log-Persistence. Themis determines security audit requirements, with authoritative capture under Kleio's established policy and platform responsibility. Routine comparison diagnostics are LogEvents.

Preserve the bounded buffer, producer backpressure, no intentional shedding and Log-Server receipt acknowledgement. Receipt transfers delivery responsibility and does not establish durable commit. Bound and measure any caller blocking against applicable performance requirements. Verify saturation, sink outage, recovery, console independence, return latency and reliable ERROR delivery without silent intentional discard. Preserve any uncertain delivery or unresolved reliability/latency conflict for explicit disposition before dependent acceptance. [Logging strategy][logging] · [R6][reconciliation]

### Dependency allocation

Declare RE2/J directly in the Maven module that implements TopicMask matching. Use its patterns directly unless a demonstrated requirement warrants an abstraction. Topic and TopicMask remain neutral models independent of Themis and higher-level governance services. Confirm the implementing module, pinned RE2/J version and features, availability of the Harmonia logging abstraction and compatibility with the governed Calliope-Models boundary when implementation is authorised. Review the existing `themis-api` dependency independently under module governance. F-01 confers no authority to remove or restructure it. [R5][reconciliation]

## 7. Consumer boundaries

TopicMask provides a neutral selector. A successful ordinary or universal match conveys no authority to access, disclose, publish, route, execute or persist. Themis evaluates authorisation with the applicable Passport, action and other policy criteria. InformationUnitMask, OperationalMetadataMask and PetasosParcelMask can compose TopicMasks for Topic-bearing criteria. Subscription, publication and filter statements compose criteria and own their feature-level rules. Routing and capability discovery retain their distinct eligibility, availability and coverage rules.

### Information Unit Topic designation

The governed IU Topic designation `harmonia.mneme.iu.<hex-string-of-compressed-uuid>` is the unqualified `Topic.getName()` projection of a structured runtime Topic. Define the distinct fully qualified canonical representation using explicitly governed qualifiers. The exact sequence and its boundary mappings require a separate design decision. Existing implementation spellings and illustrative qualifier sequences supply no accepted vocabulary.

The IU UUID remains the authoritative IU identity. TopicEntry versions carry their own meaning independently of the authoritative IU revision, Mneme operational revision and active generation, Content Structure Version and content-native versions. Preserve the established designation across persistence, transport, messaging, matching and references, or prove an explicit migration with approved boundary mappings and legacy Topic UUID treatment. [IU identity and revision contract][iu] · [R3][reconciliation]

## 8. Proposed Java surface

These signatures express the accepted semantics. The proposed Java arrangement uses `String.compareTo` for deterministic, locale-independent field ordering and `Optional<String>` to distinguish a present empty version RegEx from absence. The draft proposes `IllegalArgumentException` for out-of-range ancestor depths and `IllegalStateException` for ordinary-only accessor calls on universal masks. An adapter can use `isUniversal()` to choose the explicit logical interchange kind before reading ordinary data. Packaging and exact Java types remain for review.

~~~java
// Topic implements Comparable<Topic>.
static TopicBuilder builder();
static Topic fromCanonicalName(String text);
static Topic fromCanonicalNameWithVersion(String text);
List<TopicEntry> getEntries();
String getCanonicalName();
String getCanonicalNameWithVersion();
String getName();
String getRelativeName();
String getFriendlyName();
String getDisplayName();
int getDepth();
Topic getParent();
Topic getAncestor(int depth);
boolean isAncestorOf(Topic candidate);
int compareTo(Topic other);

// TopicBuilder
TopicBuilder addEntry(String qualifier, String value);
TopicBuilder addEntry(String qualifier, String value, String version);
TopicBuilder addEntry(int index, String qualifier, String value);
TopicBuilder addEntry(int index, String qualifier, String value, String version);
Topic build();

// TopicEntry
String getQualifier();
String getValue();
Optional<String> getVersion();

// TopicMask
static TopicMaskBuilder builder();
static TopicMask all();
static TopicMask fromCanonicalExpression(String expression, boolean includeContainedElements);
boolean isUniversal();
List<TopicMaskEntry> getEntries();
// Ordinary masks only. Both accessors reject a universal mask.
boolean isIncludeContainedElements();
String getCanonicalExpression();
boolean matches(Topic candidate);

// TopicMaskEntry
static TopicMaskEntry of(String qualifierRegex, String valueRegex);
static TopicMaskEntry of(String qualifierRegex, String valueRegex, String versionRegex);
static TopicMaskEntry forValue(String valueRegex);
static TopicMaskEntry forQualifier(String qualifierRegex);
static TopicMaskEntry any();
static TopicMaskEntry ofLiterals(String qualifier, String value);
static TopicMaskEntry ofLiterals(String qualifier, String value, String version);
static TopicMaskEntry forLiteralValue(String value);
static TopicMaskEntry forLiteralQualifier(String qualifier);
String getQualifierRegex();
String getValueRegex();
Optional<String> getVersionRegex();

// TopicMaskBuilder
TopicMaskBuilder add(TopicMaskEntry entry);
TopicMaskBuilder addRegex(String qualifierRegex, String valueRegex);
TopicMaskBuilder addRegex(String qualifierRegex, String valueRegex, String versionRegex);
TopicMaskBuilder addForValue(String valueRegex);
TopicMaskBuilder addForQualifier(String qualifierRegex);
TopicMaskBuilder addAny();
TopicMaskBuilder addLiterals(String qualifier, String value);
TopicMaskBuilder addLiterals(String qualifier, String value, String version);
TopicMaskBuilder addForLiteralValue(String value);
TopicMaskBuilder addForLiteralQualifier(String qualifier);
TopicMaskBuilder includeContainedElements(boolean include);
TopicMask build();
~~~

## 9. Open decisions and evidence

| Matter | Required disposition or evidence |
| --- | --- |
| Architecture alignment | R1–R6 have accepted dispositions for this draft. The current Continuum and historical references retain their governed standing. Obtain any required ADD/HCRL disposition before dependent work. [Architecture reconciliation][reconciliation] |
| Compatibility and IU representation | Prove historical Topic and mask reference handling, registry rules, selector equivalence and persisted/transport adapter behaviour. Establish the IU qualifier vocabulary and boundary mappings separately. [Implementation obligations][obligations] |
| Detailed design | Review exact Java API choices, field comparison, universal descriptive text and diagnostic exit-index convention. Decide adapter-specific encodings separately. |
| Implementation and acceptance | Confirm actual module placement and logging integration, set release limits, verify pinned RE2/J behaviour and collect compatibility, concurrency, performance, failure and delivery evidence. The pre-existing `themis-api` concern and any reliability/latency conflict require separate governance treatment. [Implementation obligations][obligations] |

## 10. Mandatory behavioural acceptance

Collect Maven/JUnit evidence in a separately authorised implementation. Use approved numerical limits and any subsequently dispositioned representation choices when asserting those boundaries.

| Area | Required coverage |
| --- | --- |
| Identity, ordering and hierarchy | Verify equal hashes and zero comparison for independently built Topics with the same qualified path and different versions. Exercise case, internal spaces, repeated qualifiers, field order, shorter-prefix ordering and hash/sorted collection behaviour. Verify different identities with the same unqualified name. Show that a version-sensitive mask can distinguish equal Topics. Verify strict whole-entry ancestry, null ancestry, root depth and absent parent, valid ancestor depths including the current depth, out-of-range rejection and version-preserving parent/ancestor results. |
| Qualification and value validation | With sequential and out-of-order indexed input, verify null and empty qualifiers generate `N0`, `N1` and later positions in the final hierarchy. Compare with explicit literals for identity, ordering, matching and interchange. Check generated qualifier limits. Reject null, empty and whitespace-only values at addition, while accepting supplied literal `N1`. Reject blank nonempty qualifiers. |
| Character and length validation | Check valid ASCII spaces and punctuation plus invalid boundary spaces, controls, DEL and non-ASCII for qualifier/version. Test `null`/empty Topic versions as absence, including versioned output. For value, cover allowed letters, numbers, punctuation, symbols and marks, supplementary code points, preserved composed/decomposed text and internal U+0020. Reject all excluded category groups, leading/trailing space, embedded non-ASCII space and unpaired surrogates. Test exact and exceeded code-point limits with supplementary-plane values and fields whose escaped form is longer. |
| Topic construction and reuse | Verify exclusive modes fixed by the first successful addition, failed first add leaving mode undecided, negative-index rejection, out-of-order continuity and gap failure followed by recovery. Exercise successful replacement without growth, invalid replacement retaining the prior input, failed builds leaving the builder usable, and snapshot isolation after successful later operations. Check defensive copying, immutable read access and the absence of removal and builder-from-Topic editing APIs. |
| Topic canonical forms and display | Round-trip unversioned identity and ordered versioned records with `.`, `=`, `:` and backslashes in fields. Reject omitted/empty qualifiers and values, invalid fields and escapes, extra delimiters, empty segments, any version segment in non-versioned input and textual empty versions in versioned input. Verify absent version round-trips with the colon omitted and distinguish the mask parser's valid empty expression. Check raw and escaped projections and display at depths 1, 2 and beyond 3. |
| Mask kinds and selector security | Test explicit universal construction, equality/hashing, matches across depths and versions, null exclusion, explicit logical round-trip, empty entry view and rejection by the two ordinary-only accessors. Reject empty ordinary masks, universal payloads carrying ordinary fields and the historical proposed `@all` string. Verify that a containment-enabled ordinary mask with `(?s:.*)` on qualifier and value and no version constraint round-trips as ordinary. Verify consumer-side Themis authority and that invalid or ambiguous selectors cannot widen access. |
| RegEx, literals and version presence | Verify independent full-field RegEx, explicit flags, exact default depth, trailing containment and rejection of would-be skipped matches. Reject null qualifier/value expressions and accept empty ones with their actual no-match behaviour. Check two-argument and null-version RegEx equivalence, absent/empty/wildcard structural differences and presence/match outcomes against both version states. Validate literals before quoting, including null/empty qualifiers, values and the supplied version of the three-argument overload. Exercise literal metacharacters, explicit `N1`, no default qualifier helper, and quoted expression length limits. |
| Mask builders and expression round trips | Exercise builder reuse and defensive snapshots, failed adds and builds preserving prior state, and helper equivalence. Round-trip ordinary masks with either containment flag, original expression strings, all version-constraint states, empty qualifier/value RegEx, dots, equals, colons, groups, classes, scoped flags and literal backslashes. Reject malformed escapes, delimiter order, empty paths and invalid RE2/J syntax. Reconstruct and recompile from logical interchange and compare structural equality and matching. |
| Identity and IU migration | Verify deployed identifiers, registry aliases and uniqueness rules, generated/explicit qualifier collisions and historical references. Exercise approved mappings for qualified and unqualified IU names across boundaries while retaining independent IU UUID and revision semantics. |
| Mask migration | Compare selection scope before and after conversion using persisted/reference replay, Exact/Inclusive, name-only, qualifier/value/version, unrestricted and invalid or ambiguous fixtures. Establish RE2/J equivalence or remediate unsupported inputs, including security and routing regressions. |
| Bounded evaluation and diagnostics | Verify construction-time compilation, release depth/field/expression limits and evidenced RE2/J complexity safeguards without fallback, including unsupported constructs. Verify depth and first-field short circuit, conditional TRACE and handling-boundary ERROR, fail-closed ERROR on evaluation failure, safe zero-based exit metadata and no protected payload leakage. Verify concurrent reuse and measured worst-case workloads, including negative cases and absence of global result caching. |
| Dependency and logging conformance | Verify direct RE2/J placement, Themis independence and use of the shared logger. Exercise failure, saturation and recovery with a remote sink outage, console independence and receipt acknowledgement. Measure bounded synchronous return latency for normal and failure outcomes, confirm ERROR delivery and demonstrate routine TRACE without synchronous remote delivery. Record uncertain delivery and any conflict with the agreed reliability or performance requirements. |

[initial]: ../evidence/software-0.1.0/002/before/F-01-Topic-TopicMask-Adjudications.md
[continuation]: ../evidence/software-0.1.0/003/before/F-01-Topic-TopicMask-Adjudications-Continuation.md
[latest]: ../evidence/software-0.1.0/004/before/F-01-Topic-TopicMask-Adjudications-Continuation-004.md
[delta]: ../evidence/software-0.1.0/004/after/F-01-Decision-Delta.md
[reconciliation]: ../evidence/software-0.1.0/004/after/F-01-Architecture-Reconciliation.md
[obligations]: ../evidence/software-0.1.0/004/after/F-01-Implementation-Gap-Analysis.md
[review]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[architecture-naming]: ../../governance/canonical-identity-and-naming.md#architectural-identity
[iu]: DRP-Information-Unit-Framework.Final.002.md#2-identity-and-distinct-revisions
[logging]: DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md#2-architectural-decisions-and-constraints
