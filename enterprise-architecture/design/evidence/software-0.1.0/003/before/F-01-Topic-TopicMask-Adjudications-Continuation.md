<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# F-01 Topic / TopicMask — Continuation Adjudication Record

**Programme:** Harmonia — Calliope Core Model  
**Software baseline / evidence iteration:** `software-0.1.0/003`  
**Standing:** Solution Architect decisions accepted in discussion; incorporation into a revised DRP and Architecture Continuum remains pending.  
**Scope:** F-01.45–F-01.73 (with F-01.74 indicating consolidation rather than a new object-model requirement).  
**Predecessor evidence:** `enterprise-architecture/design/evidence/software-0.1.0/002/before/F-01-Topic-TopicMask-Adjudications.md`  
**Predecessor design candidate:** `enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.draft.001.001.md`  
**Next proposed design candidate:** `enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.draft.001.002.md`

## Authority and interpretation

This record captures decisions explicitly accepted during the subsequent Solution Architect review. It is an **input** to the next controlled design consolidation, not itself an accepted Architecture Continuum publication or an implementation authorisation. Where these decisions correct earlier discussion or the iteration-002 evidence, the later decision prevails **for the draft under review**; the existing governed Continuum remains authoritative until formally reconciled. Preserve all iteration-002 evidence as historical material. Do not silently update earlier evidence.

**Critical correction:** The `N<position>` generation rule applies to a **missing or empty qualifier**, never to a value. A value is mandatory and non-empty. Earlier F-01.24/F-01.48 interpretation and portions of Draft.001.001 and iteration-002 evidence reversed those fields. This continuation expressly supersedes that incorrect interpretation.

## Accepted continuation adjudications

### F-01.45 — Explicit universal TopicMask

**ACCEPTED.** A universal mask is an explicit logical kind/property, not a reserved combination of regular expressions. `TopicMask.all()` constructs a universal mask; it matches every non-null Topic and never grants authorisation by itself. Ordinary masks require at least one criterion; an empty ordinary mask is invalid. The explicit universal kind carries no criteria; `includeContainedElements` is inapplicable. All universal masks compare equal to one another, and their equality/hashing and logical serialization preserve explicit universality. Reject ambiguous deserialized representations. This supersedes Draft.001.001's proposed special RE2/J tuple and reserved `@all` encoding. The exact Java field representation (e.g., `boolean universal`) is an implementation detail of the explicit logical kind.

### F-01.46 — Two-layer TopicMask canonical-expression escaping

**ACCEPTED.** Canonical-expression representation has an outer structural escaping layer, distinct from the original RE2/J expression syntax. Escape `.`, `=`, `:` and `\` when encoding each expression as text, and remove exactly one structural layer during parsing before RE2/J compilation. Preserve raw expressions exactly, including groups, character classes, escaped metacharacters and embedded structural delimiters. Strictly reject malformed structural grammar or invalid RE2/J patterns. For an ordinary mask, `fromCanonicalExpression(mask.getCanonicalExpression(), mask.isIncludeContainedElements())` must reconstruct a structurally equal mask. Universal masks use their separate explicit logical representation.

### F-01.47 — Display name at arbitrary depth

**ACCEPTED.** `Topic.getDisplayName()` returns the final two **values** joined with `-` for depth >= 2, or the sole value at depth 1. It excludes qualifiers and versions; preserves case and existing valid whitespace; performs no canonical escaping; and is derived presentation text, never identity.

### F-01.48 — Mandatory Topic values and canonical parsing

**ACCEPTED, as corrected by F-01.55–F-01.56.** A completed TopicEntry value is non-null and non-empty. A Topic builder rejects null or empty values rather than generating `N<position>`. Canonical parsers reject empty values and do not synthesise identity-bearing fields. The earlier statement that **values** are default-generated is superseded. An explicitly supplied literal `N1` remains an ordinary value if provided, but is not produced by the defaulting rule.

### F-01.49 — Qualifier character validation

**ACCEPTED, as corrected by F-01.55.** An explicitly supplied qualifier must be nonblank, printable ASCII, and have no leading or trailing whitespace. Embedded ordinary spaces and printable ASCII punctuation are allowed, subject to release-defined length and canonical escaping. The accepted candidate predicate is `StringUtils.isNotBlank(q) && StringUtils.isAsciiPrintable(q) && q.equals(q.strip())` (or behaviourally equivalent validation). A missing/empty qualifier is an allowed **Topic-assembly input** and resolves to `N<position>` before the completed Topic is exposed; an invalid non-empty qualifier is rejected. The constraints apply to literal TopicEntry qualifiers, not to mask RegEx syntax.

### F-01.50 — Field character policies

**ACCEPTED.** Qualifiers use printable ASCII. Values use printable Unicode, retaining international characters. Versions are optional printable ASCII. No automatic case or Unicode normalisation is permitted. Limits are release-defined for each field. See F-01.51–F-01.54 for additional precise restrictions.

### F-01.51 — Value whitespace

**ACCEPTED.** TopicEntry values must not contain leading or trailing whitespace. Embedded printable spaces are allowed. Invalid values are rejected, not silently trimmed. This applies to explicitly supplied values in builders and canonical parsers. **Values are never default-generated** (F-01.55 correction).

### F-01.52 — Version whitespace

**ACCEPTED.** TopicEntry version is an optional printable ASCII String. `null` and `""` represent absence; a present, non-empty version has no leading/trailing whitespace. Embedded printable spaces are allowed. Invalid versions are rejected rather than trimmed. No particular version scheme is imposed. This supersedes earlier allowance for whitespace-only versions.

### F-01.53 — Printable Unicode values

**ACCEPTED.** Validate values by Unicode code point/category: permit letters (L*), numbers (N*), punctuation (P*), symbols (S*), and combining marks (M*). Permit ordinary U+0020 only internally. Reject controls (Cc), format characters (Cf), malformed surrogates (Cs), private-use (Co), unassigned (Cn), line/paragraph separators (Zl/Zp), and all other space separators (Zs). Reject malformed Unicode sequences. Preserve allowed code points without automatic normalisation. Leading/trailing whitespace remains prohibited.

### F-01.54 — Length measurement

**ACCEPTED.** Release-defined TopicEntry field lengths are measured in Unicode **code points**; values are measured before canonical escaping. ASCII qualifiers/versions have equivalent code-point and character counts. RegEx length limits apply to original expressions before the outer structural-escaping layer. UTF-8 byte constraints at transport/storage boundaries are independent. Reject invalid Unicode sequences before applying length semantics.

### F-01.55 — CRITICAL: Generated qualifiers, not values

**ACCEPTED; corrective/superseding decision.** Only a missing (`null`) or empty (`""`) **qualifier** is replaced with `N<position>` at Topic assembly, based on the final zero-based hierarchical position. Values must always be supplied, non-null and non-empty. Generated qualifiers are validated exactly like explicit qualifiers and are indistinguishable from equal explicit literals in identity, matching, interchange and ordering. Correct F-01.24, F-01.48, Draft.001.001 and all affected comparison/testing documentation accordingly. This restores the intended rule in historical Final.001.

### F-01.56 — Canonical parsing requires normalized qualifiers

**ACCEPTED.** Topic canonical-name parsers require an explicit, non-empty qualifier **and** an explicit, non-empty value in every entry. Parsers never generate `N<position>` or silently normalise identity. Builders may generate a missing qualifier during assembly. Canonical `N1=Mneme` is valid and indistinguishable from a generated `N1` once assembled; canonical `=Mneme` is invalid.

### F-01.57 — Empty RegEx expressions

**ACCEPTED.** An empty string `""` is a permitted RE2/J expression and retains full-field RegEx semantics. It is **not** a wildcard, not an absent constraint, and not universal matching. Empty qualifier/value expressions cannot match valid non-empty TopicEntry qualifier/value fields; an empty version expression requires a present version but cannot match a valid non-empty version. Empty RegEx is distinct from empty literal input, which is invalid under the literal helper rules.

### F-01.58 — Three states of version RegEx constraint

**ACCEPTED.** A mask-entry version RegEx may be (a) `null`: no constraint, matches present or absent Topic versions; (b) `""`: present explicit empty expression; or (c) `".*"`: explicit wildcard matching a **present** version according to RE2/J semantics. These are structurally distinct for equality, hashing, parsing and serialization. Never normalise empty expression to absent constraint. TopicEntry version's separate `null`/empty normalization does not apply to masks.

### F-01.59 — Nullable version argument in RegEx factories

**ACCEPTED.** `TopicMaskEntry.of(qRegex,vRegex,null)` is equivalent to `TopicMaskEntry.of(qRegex,vRegex)` (no version constraint). Passing `""` as the third argument creates a present empty RegEx constraint. The `".*"` third argument creates a present wildcard constraint. Preserve structural distinctions.

### F-01.60 — Mandatory qualifier/value RegEx arguments

**ACCEPTED.** Null qualifier or value RegEx arguments are invalid; they are never implicitly widened to `.*`. Empty RegEx strings are valid. `forValue`, `forQualifier` and `any` intentionally provide the respective `.*` defaults. The optional version field remains governed separately by F-01.58–F-01.59.

### F-01.61 — Literal helper validation

**ACCEPTED.** Literal mask helper methods validate their literal arguments against corresponding TopicEntry field rules **before** quoting/escaping them for RE2/J. Literal qualifier: nonempty printable ASCII with no leading/trailing whitespace. Literal value: nonempty printable Unicode with no leading/trailing whitespace. Literal version when supplied: valid nonempty printable ASCII without leading/trailing whitespace. Empty literal qualifier/value is invalid even though empty RegEx expressions are permitted. Apply subsequent expression size/complexity limits to the resulting RegEx. No implicit wildcards.

### F-01.62 — Literal version overloads

**ACCEPTED.** `ofLiterals(q,v)` leaves version unconstrained. `ofLiterals(q,v,version)` requires a valid non-null/nonempty literal version and matches precisely that version. A null/empty version in the three-argument literal factory is invalid, unlike the optional null version allowed in RegEx factories.

### F-01.63 — No default-qualifier generation in mask factories

**ACCEPTED.** `TopicMaskEntry` factories never synthesise `N<position>`. Null or empty literal qualifiers are invalid. `forLiteralValue(value)` intentionally supplies an unrestricted qualifier RegEx at its positional entry. An explicitly specified `N1` is a normal literal qualifier.

### F-01.64 — No default-qualifier builder helper

**ACCEPTED.** Do not add `TopicMaskBuilder.addDefaultQualifier(...)` or comparable special APIs. Developers may explicitly supply `N0`, `N1`, etc. or use `addForLiteralValue(value)` to accept any qualifier at a position. Topic-assembly defaulting does not occur in the mask builder.

### F-01.65 — Builder reuse and snapshots

**ACCEPTED.** `TopicMaskBuilder` is reusable after `build()`; each result is an independent immutable snapshot, unaffected by later builder operations. Defensive copies are mandatory. Immutable compiled patterns may be shared safely. Builders need not be thread-safe. The same snapshot principle applies to `TopicBuilder`.

### F-01.66 — Validation timing

**ACCEPTED.** Validate supplied fields immediately when added to builders where possible. For Topic, reject null/empty values immediately; permit missing/empty qualifiers as assembly inputs and resolve them to `N<position>` at `build()`. Validate supplied nonempty qualifiers and present versions immediately. Validate literal helper inputs at invocation and RegEx syntax at immutable mask-entry construction. Validate hierarchy depth, index continuity, normalized completed state, and all object-level invariants during `build()`. Failed construction exposes no partially completed object.

### F-01.67 — Failed builder operations leave state unchanged

**ACCEPTED.** Validate before mutating builder state. A failed add/replace or failed `build()` leaves the builder's previous state intact and reusable; previously built objects are unchanged. This applies to Topic and TopicMask builders. No transaction framework is implied.

### F-01.68 — Final position determines generated qualifier

**ACCEPTED.** `N<position>` uses the entry's final, zero-based position in the completed ordered hierarchy, not an arbitrary internal map key or call sequence. Sequential append and final sorted indexed order must yield the same resolved positions for equivalent ordered inputs. Explicit literal qualifiers remain untouched.

### F-01.69 — Indexed construction retained

**ACCEPTED.** Retain explicitly indexed Topic assembly. Valid completed indexed hierarchies use contiguous zero-based indices `0..N-1`; gaps and negative indices fail validation. Entries are ordered by ascending index. Indices have no independent architectural identity. Missing qualifiers use their final positions. A duplicate index is **not** invalid by itself; F-01.71 explicitly authorises replacement.

### F-01.70 — Exclusive builder construction modes

**ACCEPTED.** A TopicBuilder instance uses **either** sequential append **or** indexed additions; mixing is invalid. The first successful addition establishes the mode. Failed additions cannot alter mode or state. Both modes produce equivalent immutable Topics for equivalent ordered entries. Indexed gaps are invalid at final build. Reuse/snapshot semantics remain applicable.

### F-01.71 — Duplicate indexed additions replace

**ACCEPTED.** `addEntry(index,...)` adds where unused and **replaces** the existing entry when the index already exists. Validate replacement fully first; an invalid replacement leaves state unchanged. Built Topic snapshots are unaffected. Final depth/index continuity checks still apply. This specifically supersedes the earlier recommendation to reject duplicate indices.

### F-01.72 — No entry-removal API

**ACCEPTED.** Do not add `TopicBuilder.removeEntry(index)` as part of this design. A different hierarchy is a **new Topic identity** assembled from the desired entries; an existing Topic is never mutated. Indexed replacement is retained, but removal/renumbering semantics are not part of the required builder API.

### F-01.73 — No builder-from-existing-Topic convenience

**ACCEPTED.** Do not introduce `Topic.builder(existingTopic)` merely to facilitate editing an immutable Topic. New Topics are assembled explicitly through existing constructors/builders; parent/ancestor navigation already supports traversal. Do not add speculative API features without demonstrated need.

## Consolidation disposition (F-01.74)

The Solution Architect agreed to consolidate the subsequent accepted decisions as a **new governed iteration** (`software-0.1.0/003`), preserving the `002` evidence. Prepare `DRP-Topic-and-TopicMask.draft.001.002.md` as the next draft evolving the historical Final.001.000 baseline. Do not edit or silently relabel `draft.001.001.md` or the previous accepted Final DRP. The DRP naming convention is `AAA.BBB`: `001.001` is the first draft evolving accepted `001.000`, `001.002` the next draft, and acceptance would create `002.000` separately. No code modification, ADD/HCRL, publication, commit or push is authorised solely by this record.

## Mandatory reconciliation checklist for Codex

1. Explicitly correct **qualifier versus value** defaulting in all successor design text, examples, API descriptions, proposed tests, decision-delta and implementation-gap conclusions. **Do not silently rewrite iteration-002 evidence**; record correction in iteration-003 `after/` evidence.
2. Replace proposed universal-RegEx tuple and `@all` special encoding with the explicit universal kind and independent logical representation (F-01.45).
3. Incorporate the accepted two-layer mask canonical-expression grammar and mandatory reversible round-trip tests (F-01.46).
4. Update arbitrary-depth display-name semantics, mandatory Topic values, qualifier/value/version character and whitespace validation, Unicode category rules, and code-point length accounting (F-01.47–F-01.56).
5. Preserve absent/empty/wildcard **version-mask-expression** distinctions separately from TopicEntry version normalization (F-01.57–F-01.60).
6. Separate literal and RegEx helper validation, including strict literal-version overloads; do not default qualifiers in mask factories/builders (F-01.61–F-01.64).
7. Preserve reusable, snapshot-isolated builders, early validation, atomic failure semantics, final position defaulting, indexed/sequential mode separation, and indexed replacement (F-01.65–F-01.71).
8. Do **not** add speculative removal or builder-from-existing-Topic conveniences (F-01.72–F-01.73).
9. Cross-check existing accepted architecture and consumer compatibility; retain unresolved R1–R6 architecture/dependency/logging issues for separate Solution Architect disposition. Do not imply these changes are already incorporated into the Architecture Continuum.
10. Record what was actually inspected and changed. Do not implement Java, alter Maven/tests, modify approved architectural baseline, commit or push absent separate authorisation.

## Acceptance-test delta

Add or refine mandatory Maven/JUnit acceptance cases covering: generated **qualifiers** at final hierarchical positions; rejection of absent/empty values; strict canonical parsing of absent qualifiers; valid/invalid ASCII qualifiers and versions; Unicode value category checks including supplementary-plane code points; whitespace and code-point length limits; universal mask representation and round-trip without reserved regex; two-layer regex escaping with delimiters/classes/groups; empty qualifier/value regex allowed but null prohibited; version regex absent/empty/wildcard distinctions; strict literal factory validation; versioned literal overload null rejection; builder reuse with immutable snapshots; failure atomicity; indexed and sequential construction mode isolation; duplicate-index replacement; contiguous index enforcement; and absence of removal/copy-edit convenience APIs.
