<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Package — Topic and TopicMask

- **Standing** · Draft.001.001, pending Solution Architect review
- **Evidence boundary** · software-0.1.0, iteration 002
- **Scope** · Common Topic and TopicMask model, matching and diagnostic behaviour
- **Authority for this draft** · [F-01 adjudications](../evidence/software-0.1.0/002/before/F-01-Topic-TopicMask-Adjudications.md)

This package consolidates the accepted discussion decisions for the common model. Sections 1–7 describe the requirements for review. Paragraphs marked as recommendations and the Java signatures are proposals. Adoption into the Architecture Continuum and software implementation require separate authorisation. [Final.001](DRP-Topic-and-TopicMask.Final.001.000.md) is the historical predecessor. The current Continuum contracts and their relationship to this draft are recorded in the [architecture reconciliation](../evidence/software-0.1.0/002/after/F-01-Architecture-Reconciliation.md).

## 1. Topic and TopicEntry

A Topic identifies or classifies a named concept by a root-to-leaf path of typed entries. Each completed `TopicEntry` holds a required qualifier, a value, and an optional version. A qualifier identifies the role of its entry at that position. It can recur at other positions, as in `System=Harmonia.Application=Mneme.Component=IngresServices.Component=PetasosReceiver.Component=HL7v2MessageReciever`. A Topic has at least one entry. Positions are zero-based and deterministic. Sequence keys are an internal way to implement order. An immutable ordered list is sufficient, and a defensively copied `SortedMap<Integer, TopicEntry>` is also suitable if reconstruction preserves the same order. [F-01.2–F-01.6, F-01.32][adjudications]

At Topic assembly, replace a null or empty value at position `i` with `N<i>`, using its zero-based position. The resulting literal is indistinguishable from the same explicitly supplied value in naming, equality, ordering, matching and interchange. Qualifiers must be supplied and valid. Versions are optional strings. Both `null` and `""` represent absence. Preserve all nonempty version text exactly, including whitespace, with no required version scheme or ordering. Preserve case and literal field content, including whitespace. Topic and TopicEntry are strictly immutable. Copy mutable input containers, expose only immutable state, and construct a new object for any change. [F-01.2, F-01.21–F-01.25][adjudications]

Topic identity is its **non-versioned canonical name**. There is no instance UUID. `equals()` and `hashCode()` use that name. The ordered, escaped qualifier/value path defines identity, so a path change changes identity. `Topic.compareTo` compares qualifier then value at each position, case-sensitively and with versions excluded. At the first differing position the field comparison determines order. For a shared prefix, the shorter path precedes the longer one. A zero comparison is equivalent to Topic equality. Topics that differ only by version can collapse to a single key in hash-based or naturally ordered collections. Use the ordered entries including versions, or their versioned canonical representation, where a caller needs version-sensitive comparison. [F-01.1, F-01.4, F-01.21–F-01.23, F-01.29][adjudications]

## 2. Names and strict Topic parsing

For three entries with versions at positions 0 and 1, the name conventions are as follows. Field strings in these examples require no escaping. [F-01 name and hierarchy conventions][adjudications]

| API | Result |
| --- | --- |
| `getCanonicalName()` | `qualifier0=value0.qualifier1=value1.qualifier2=value2` |
| `getCanonicalNameWithVersion()` | `qualifier0:version0=value0.qualifier1:version1=value1.qualifier2=value2` |
| `getName()` | `value0.value1.value2` |
| `getRelativeName()` | `qualifier2=value2` |
| `getFriendlyName()` | `value2` |
| `getDisplayName()` | `value1-value2` |
| `toString()` | The result of `getCanonicalNameWithVersion()` |

In canonical text, encode literal `.`, `=`, `:` and `\` inside each field by prefixing one backslash. The in-memory fields contain the decoded literals. Unescaped `.` separates entries, unescaped `=` separates the qualifier or qualifier/version pair from the value, and unescaped `:` separates a qualifier from a present version before the equals sign. For example, `System=Harmonia.Component:2\.4=Petasos\.Receiver` decodes to two entries, with version `2.4` and value `Petasos.Receiver` at position 1. Its non-versioned canonical name is `System=Harmonia.Component=Petasos\.Receiver`. [F-01.23, F-01.25–F-01.28][adjudications]

`fromCanonicalName(String)` parses the non-versioned syntax and reconstructs entries with absent versions. It rejects an unescaped version delimiter. `fromCanonicalNameWithVersion(String)` parses the version-capable syntax, including paths whose entries have no versions. Both factories reject malformed syntax or escapes, missing or malformed qualifiers, invalid depth and fields that exceed their release limits. A dangling backslash, an unknown escape, a missing field separator, an extra unescaped field delimiter or an empty path segment is malformed. Parsing preserves case and whitespace. Versioned canonical text omits the entire `:version` segment when the version is absent. The canonical name is reversible for the identity projection. The versioned canonical name is reversible for the full ordered entry data. [F-01.1, F-01.23, F-01.25–F-01.26, F-01.32–F-01.34][adjudications]

**Recommendation for review.** Emit the relative name with the same escaping as the final non-versioned canonical entry. Treat `getName()` as joined raw values, `getFriendlyName()` as the raw leaf value and `getDisplayName()` as presentation text. Use the leaf value at depth 1 and join the final two values with `-` at depth 2 or greater. These suggestions preserve the accepted example. Display at depths other than three and escaping of human-facing names remain open. Also confirm the strict parser's handling of textual empty fields. The proposed canonical spelling rejects `qualifier=` and `qualifier:=value`, with missing-value generation and empty-version normalization handled by assembly inputs and canonical output using the normalized forms. [Open design details](#9-open-decisions-and-evidence)

## 3. Hierarchy and navigation

Hierarchy is implicit in the ordered entries. An ancestor matches a **proper prefix of complete entries** by exact, case-sensitive qualifier and value at each position. Versions are ignored in this comparison. For example, `Country=Australia.StateTerritory=ACT` is an ancestor of `Country=Australia.StateTerritory=ACT.City=Canberra`. `StateTerritory=AC` at the same position would fail, regardless of shared characters. Equality, ancestry and wildcard matching are distinct operations. [F-01.4–F-01.7, F-01.21, F-01.30–F-01.32][adjudications]

- `getDepth()` returns the entry count, starting at 1 for a root.
- `getParent()` returns the path with the last entry removed, or `null` for a root.
- `getAncestor(int depth)` retains exactly the first `depth` entries. The current depth denotes the full path. Parent and ancestor results preserve all versions on retained entries.
- `isAncestorOf(Topic)` tests strict ancestry. Equal-depth paths, including equal Topics with different versions, have no strict ancestry relationship.

**Recommendation for review.** Treat depths outside `1..getDepth()` as invalid arguments and a null ancestry candidate as false. Construct navigation results from immutable prefixes without storing parent or child links.

## 4. TopicMask

A TopicMask holds an immutable, ordered sequence of criteria and `includeContainedElements`, which defaults to false. Each criterion contains a qualifier regex, a value regex and an optional version regex. Equality and hashing use the original exact expression strings, their order, presence of version constraints and the containment flag. Equivalent regex languages can have different expression strings and different structural identities. There is no mask UUID or natural ordering. [F-01.7–F-01.11, F-01.14–F-01.17, F-01.29][adjudications]

The public matching operation is **`TopicMask.matches(Topic)`**. For an ordinary mask, exact depth is required when `includeContainedElements` is false. When it is true, the candidate must have at least the mask depth. Only additional **trailing** entries are permitted. After the depth check, apply the criteria at position `i` to the candidate entry at position `i`. Evaluate qualifier, then value, then the optional version, and return false on the first mismatch. An omitted version expression accepts both present and absent candidate versions. A supplied expression requires a present version, including when the expression is `.*`. Match using the complete decoded field, so a caller requesting substring matching must express it in the regex. Only a candidate that satisfies every criterion matches. [F-01.7–F-01.8, F-01.10, F-01.12–F-01.14, F-01.36][adjudications]

Empty or malformed masks fail construction. `TopicMask.all()` explicitly matches every valid non-null Topic, including roots and Topics with or without versions. `matches(null)` always returns false. `any()` is a convenience for an ordinary entry and follows the depth, position and RE2/J semantics of its expressions. A positional mask that happens to cover all Topics through its expressions still follows ordinary matching. [F-01.9, F-01.17, F-01.20, F-01.36][adjudications]

### Engine, helpers and canonical expressions

Use RE2/J for qualifier, value and version regex, compiled during mask construction, before any mask is returned. Reject invalid or unsupported expressions at that boundary. Default matching is case-sensitive. Explicit RE2/J flags such as `(?i)` may select case-insensitive matching for a field. Use RE2/J's supported dialect and document exclusions, including backreferences and look-around. Java regex fallback is forbidden. `matches()` is full-field matching. Thus `ADR` fails against `xADRx` and `.*ADR.*` can match it. `.*` retains RE2/J's default line-terminator semantics. Share immutable compiled patterns and allocate matchers for the current invocation. [F-01.8, F-01.13–F-01.14, F-01.18, F-01.21, F-01.41][adjudications]

`of(q,v)` accepts regex. `forValue(v)` supplies qualifier regex `.*` and `forQualifier(q)` supplies value regex `.*`. `any()` supplies `.*` for both fields with the version unconstrained. Literal helpers such as `ofLiterals`, `forLiteralValue` and `forLiteralQualifier` quote the supplied input for RE2/J before compiling. Fluent builder equivalents append entries in order and support optional version constraints and containment. A literal `A.B` therefore matches that literal dot, while regex `A.B` can also match `AxB`. [F-01.9, F-01.19][adjudications]

`fromCanonicalExpression(String, boolean)` takes containment explicitly. A dedicated regex-aware parser must recover each exact field expression, distinguish structural escapes from regex syntax, and validate the decoded regex through RE2/J. A dot or colon inside a regex group or character class must survive the round trip without creating a path entry or version criterion. Use the builder for programmatic assembly. `getCanonicalExpression()` presents the expression path and `toString()` returns the same text. Neither supplies the containment flag for general reconstruction. [F-01.14–F-01.16, F-01.25, F-01.27–F-01.28][adjudications]

**Recommendation for review — two-layer expression grammar.** Encode each raw regex independently by escaping `.`, `=`, `:` and `\` with a backslash, then join fields in the form `qualifierRegex[:versionRegex]=valueRegex` and entries with `.`. The mask-specific scanner recognizes only unescaped structural separators, rejects malformed structural escapes and invalid separator order, and removes exactly one escaping layer before passing the original regex to RE2/J. The colon before the equals sign indicates a supplied version regex. Its absence omits the constraint. Keep expression text exact after decoding, including any empty expression the release validation policy allows. Regex grouping, flags, classes and escapes are interpreted only by RE2/J after decoding. For the example below, the first value contains a literal regex dot and character class, and the second qualifier contains a scoped flag. Each line pair shows the transformation, separately from Java string-literal escaping.

~~~text
Raw expressions at position 0: qualifier Role, omitted version, value (LAB|MED)\.[A:=]
Encoded entry:                  Role=(LAB|MED)\\\.[A\:\=]
Raw expressions at position 1: qualifier (?i:Zone), version 2\.4, value .*
Encoded entry:                  (?i\:Zone):2\\\.4=\.*
Joined canonical expression:    Role=(LAB|MED)\\\.[A\:\=].(?i\:Zone):2\\\.4=\.*
~~~

**Recommendation for review — explicit universal representation.** Reserve a single entry with qualifier and value expressions `(?s:.*)`, an omitted version expression and containment true as the logical representation of `all()`. Use a reserved canonical token such as `@all`, accepted only with containment true, and reserve that exact tuple from ordinary construction. Reconstruct the tuple as the universal kind at the logical decoding boundary. This proposal retains structural identity and a nonempty logical payload without adding a hidden identity field. Round-trip and rejection tests must cover the reserved tuple, ordinary masks with similar expressions, and the token with the wrong containment flag. The encoding, reserved tuple and constructor rules are pending disposition.

## 5. Logical serialization and validation

Topic interchange preserves the normalized ordered qualifier/value/version records, including absent versus present versions. Mask interchange preserves ordered exact qualifier/value/version regex text, omitted version constraints, the containment flag and the explicit universal representation once defined. Reconstruct masks with the same validated construction path and rebuild compiled patterns. Core logical data is format-independent. Adapters preserve ordering, identity, version-sensitive information and immutability. The contract prescribes no JSON, Java serialization, transport format or transport annotations in the shared model. Derived displays, mutable builders, matchers and compiled patterns carry no logical authority. [F-01.1–F-01.3, F-01.11, F-01.14–F-01.17, F-01.23, F-01.28, F-01.41][adjudications]

Construction and parsing enforce a software-release-defined maximum hierarchy depth for both Topics and masks and independent limits on qualifier length, value length, version length and regex length. Values and measurement units remain subject to software-release definition. Apply the regex length limit to retained expression text, including expressions produced by literal helpers. Establish a release-defined compile and evaluation complexity safeguard where technically feasible, based on evidence for the pinned RE2/J implementation. Verify available controls and error mapping before selecting the mechanism or setting thresholds. RE2/J avoids catastrophic backtracking. Depth, input size and expression complexity still affect resource use. Validation failures reject the definition. Unexpected evaluation failures use the fail-closed path. [F-01.14, F-01.18, F-01.32–F-01.36, F-01.38][adjudications]

Matching on the same immutable mask is safe concurrently without external synchronization. Compiled patterns are shared and all per-call evaluation state is local. Calliope must not maintain a global mask-by-Topic Boolean-result cache. Consumers may reuse immutable compiled masks. Any later result caching requires measured evidence and explicit rationale. Topic equality alone cannot distinguish candidates whose versions differ and may match differently. [F-01.14, F-01.41–F-01.42][adjudications]

**Recommendation for review.** Validate generated values and decoded fields through the same model rules. Bound the parser's input and allocation before compilation, based on the approved decoded limits and representational overhead. Confirm short-circuiting and construction/matching budgets with reproducible measurements. [F-01.33–F-01.36][adjudications]

## 6. Fail-closed matching and diagnostics

Return false for a null candidate, a failed shape or field criterion, or an unexpected runtime evaluation failure. For the unexpected failure, emit an **ERROR LogEvent**. Keep the matching API Boolean and leave invalid mask definitions exceptional at construction. An invalid definition is logged at **ERROR** by the handling boundary. Normal success, mismatch and depth mismatch are eligible for conditional **TRACE**. Log the comparison exit point (zero-based position when a field was evaluated), failure category and suitable bounded context. Categories include depth, qualifier, value, absent required version, version mismatch and evaluation error. A diagnostics failure must never promote a false result. [F-01.20, F-01.36–F-01.40][adjudications]

Calliope uses Harmonia application logging. A Local-Logger console path runs in parallel with the in-JVM Log-Satellite path. Calliope requires no direct dependency on rsyslog, Log-Server or Log-Persistence. A diagnostic LogEvent and a security AuditEvent have separate purposes. Themis retains policy evaluation ownership. Kleio performs mandatory authoritative audit capture under the established platform authority and policy. Calliope does not independently audit each mask comparison. Omit protected or sensitive field text, reconstructed paths, raw regex and exceptions containing them from default diagnostics. Log safe metadata such as the depth, checked field, exit position and category. [F-01.37–F-01.40][adjudications] · [Logging strategy](DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md) · [AX-07](../../governance/architectural-axioms.md#ax-07) · [Repository logging invariant](../../../AGENTS.md#invariant-7-zero-phi-diagnostic-logging)

**Recommendation for review.** Represent an exit before any field with an explicit absent position and include the mask and candidate depths when applicable. On ordinary success record the last checked position. The universal special case has no checked position. Select a sanctioned application logging integration and verify behaviour under saturated buffer, sink failure and recovery, including console independence, protection against recursive error logging, and preservation of the false outcome. Keep the satellite's agreed backpressure and no-intentional-shedding policy in that review. The effect on caller latency, completion and ERROR delivery is open pending that evidence.

## 7. Consumer boundaries

Topic and TopicMask supply shared naming and discrimination to consuming Features. Higher-level IU, OperationalMetadata and PetasosParcel masks can compose TopicMasks for Topic-valued criteria. Publication, subscription and filter statements retain their own selection rules and compose relevant criteria. A positive TopicMask match supplies one selector result. Themis evaluates authorisation and security evidence under its own criteria, Passport and enforcement contracts. Routing and capability discovery apply their separate eligibility, availability and coverage rules. TopicEntry version is separate from IU identity, authoritative IU revision, Mneme operational revision, Content Structure Version and native Content version. The accepted IU UUID and the IU's Topic designation remain separately governed. Its qualified canonical mapping, historic Topic references and existing mask inputs require Solution Architect disposition before any dependent incorporation or implementation. [IU framework](DRP-Information-Unit-Framework.Final.002.md) · [Architecture reconciliation](../evidence/software-0.1.0/002/after/F-01-Architecture-Reconciliation.md)

## 8. Proposed Java surface

These are candidate signatures. Type packaging, builder allocation, exception types and compatibility treatment are implementation design. An assembly builder can accept missing raw values and publish only resolved immutable entries. `Optional<String>` indicates a proposed read API for optional version fields. Regex overloads with two arguments omit the version constraint. The versioned literal overload quotes all supplied fields.

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

// TopicBuilder and TopicEntry.
TopicBuilder addEntry(String qualifier, String value);
TopicBuilder addEntry(String qualifier, String value, String version);
Topic build();
String getQualifier();
String getValue();
Optional<String> getVersion();

// TopicMask.
static TopicMaskBuilder builder();
static TopicMask all();
static TopicMask fromCanonicalExpression(String expression, boolean includeContainedElements);
List<TopicMaskEntry> getEntries();
boolean isIncludeContainedElements();
String getCanonicalExpression();
boolean matches(Topic candidate);

// TopicMaskEntry.
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

// TopicMaskBuilder.
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
| Canonical grammar and validation | Review the two-layer proposal and prove reversibility for groups, classes, backslashes and delimiters under RE2/J. Confirm how strict Topic parsing treats textual empty fields, define qualifier validity beyond the required qualifier and syntax rules, and confirm any additional mask field constraints such as empty regex policy. |
| Display and helper edges | Define display at depths other than three, escaping for human-facing projections and bounds behaviour for proposed navigation accessors. |
| Universal mask | Confirm the explicit nonempty logical encoding, canonical token and distinction between explicit universality and broad ordinary criteria. |
| Software-release limits | Define depth and independent field limits, units, expression complexity safeguards and expected failure/resource outcomes. Verify the pinned RE2/J capabilities. |
| Logging under pressure | Verify the approved pipeline, backpressure, failure and recovery behaviour against Boolean matching and diagnostic delivery. Confirm the logging dependency boundary. |
| Version-aware representation | Verify ordered round trips and version-sensitive consumers against version-insensitive Topic equality, ordering and collection behaviour. |
| Current accepted architecture | Resolve the identity, names, references, mask defaults and consumer compatibility conflicts in the [architecture reconciliation](../evidence/software-0.1.0/002/after/F-01-Architecture-Reconciliation.md). |

## 10. Mandatory behavioural acceptance

Later implementation acceptance requires Maven/JUnit evidence for the following cases. Assertions involving an open representation or numerical limit use the subsequently approved contract. [F-01.43–F-01.44][adjudications]

| Area | Required coverage |
| --- | --- |
| Identity and ordering | Independently assembled equal paths, equal hashes and zero natural comparison across different or absent versions. Verify ordered qualifiers, repeated qualifiers, case and whitespace, distinguished path lengths, leading-prefix ordering, collision-free canonical escaping, and set/map behaviour. Show version-sensitive comparison of equal Topics. |
| Entry assembly and navigation | Null and empty values generate the correct absolute position, explicit identical literals compare and match identically, missing qualifiers and empty paths fail, retained versions survive parent and ancestor operations, root has no parent, strict ancestry rejects the same path and partial-field prefixes. |
| Names, versions and parsers | Verify every name convention including the accepted three-entry display. Round-trip both Topic factories with dots, equals, colons and backslashes in field text. Reject malformed text, qualifiers and escapes. Confirm case and whitespace preservation, null/empty version normalization and that non-versioned parsing cannot restore versions. |
| Mask definition and helpers | Ordered structural equality/hashes, distinguish expressions with the same regex language, containment changes, and presence of version criteria. Exercise regex and literal helpers and their builder equivalents, invalid/null definitions, default exact depth, empty mask rejection and explicit universal matching/round trip including null exclusion. |
| Positional matching and security | Independent qualifier, value and version checks, full-field behaviour, explicit case folding, pattern dot versus literal dot, shorter candidate, trailing descendants and a would-be match requiring skipped levels. Exercise missing/present versions with omitted constraints, `.*` and mismatching regex. Show that equal Topics with different versions can yield different results for the same mask. Prevent malformed or ambiguous definitions from becoming unrestricted. Verify at the consumer boundary that a positive mask match alone cannot yield Themis permission. |
| Immutability and interchange | Mutate construction inputs and reused builders without affecting prior objects. Verify read views, deterministic reconstruction, round-trip original regex text, order, both containment states, normalized values, versions and universal form. Reconstruction validates and recompiles. Include masks with backslashes, character classes, groups and structural delimiters. |
| Release validation and RE2/J | Test valid limits and over-limit rejection for both depths and every field class, including parsing, logical reconstruction and literal-helper expansion. Test malformed regex, look-around and backreferences rejected at construction, with no fallback. Verify the approved complexity safeguards and their error mapping. |
| Diagnostics and failure | Verify shape checks precede regex, first mismatch stops evaluation, expected conditional TRACE and invalid-definition ERROR, and runtime evaluation ERROR with false. Verify zero-based field exit points, no raw sensitive data, sink failure cannot promote false, and agreed saturation, recovery and LogEvent delivery behaviour. |
| Concurrency and performance | Match one reused mask concurrently with differing candidates and versions. Verify determinism and input isolation, construction-time compilation and per-call matcher state. Measure allowed worst-case depth and expression workloads, including negatives, against the release-defined budgets. Verify no global Boolean-result caching. |

[adjudications]: ../evidence/software-0.1.0/002/before/F-01-Topic-TopicMask-Adjudications.md
