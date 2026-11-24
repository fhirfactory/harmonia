<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Archi round-trip Increment 1 — diagnostics and empirical fidelity analysis

Date: 2026-10-18. Branch: `Harmonia-GovernedAccess`.
Accepted architecture baseline: **Release 1.0.6**, inspected and accepted by Mark in
**Archi 5.10.0**, committed at `d060702d6055bc42ef4e0b64917703636f466faa`.
Status: **real Mark-attested Archi 5.10.0 exports analysed; TRANSPORT FINDINGS**.
The UUID32 and three-name Property carriers pass the controlled transport probe.
The Release 1.0.6 export is not lossless: evidence text, organisation item identity
and some presentation metadata change. The root-cause analysis below accounts for
every raw finding without changing the comparator or adding normalisation rules.

## Implementation and authority

Implemented one opt-in read-only tool,
[`diagnose_roundtrip.py`](../../docs/archimate/capability-architecture/tools/diagnose_roundtrip.py),
with `diagnose`, `compare` and `verify-exports` commands. It inventories the future
identity/three-name contract, probes exact no-edit Exchange transport, and runs both
hash-pinned fixture comparisons after real exports arrive. It does not reconcile
architecture or match architectural instances by names. Outputs are restricted to
`.junie/reports` or `/tmp`, and cannot overwrite input files.

The final [assessment](2026-10-18-archimate-roundtrip-least-step-plan.md), explicit
task adjudications, [mapping](../../docs/markdown/governance/rad-archimate-model-mapping.md),
[navigation convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md)
and [axioms](../../docs/markdown/governance/architectural-axioms.md) were consulted.
AX-04 supports reuse of existing machinery; AX-14 preserves identity, names, types,
semantic/presentation state and legacy roles separately; AX-15/AX-17 preserve
unresolved transport outcomes and absent naming authority. No semantic architectural
change is proposed. Older storage terminology and UUID-URN recommendations remain
historical/current unmigrated contracts, rather than being silently rewritten to
match this task's future UUID32 contract. The explicit task prohibition on a release
increment takes precedence for this bounded implementation: the release authority
remains **1.0.6 / previous 1.0.5**.

The user's pre-existing staged/unstaged assessment-report changes were left intact.
Historical pending-inspection statements were preserved; they do not override Mark's
subsequent acceptance of Release 1.0.6.

## Files added

- Tool: `docs/archimate/capability-architecture/tools/diagnose_roundtrip.py`.
- Tests: `docs/archimate/capability-architecture/tests/test_roundtrip.py`.
- Synthetic input and pinning manifest:
  `docs/archimate/capability-architecture/tests/fixtures/roundtrip/future-contract.xml`
  and `manifest.json`.
- [Operating procedure](../../docs/archimate/capability-architecture/roundtrip-increment-1.md).
- This report and evidence in
  [`2026-10-18-archimate-roundtrip-increment-1/`](2026-10-18-archimate-roundtrip-increment-1/):
  `active-diagnostic-summary.json`, `specimen-diagnostic.json`, `archi-fidelity.json`,
  `baseline-self-comparison.json`, validation logs, the supplied raw `archi-exports/`
  files, and the follow-up [`empirical-analysis.json`](2026-10-18-archimate-roundtrip-increment-1/empirical-analysis.json).

This follow-up changes this report and adds analytical evidence only. The diagnostic
implementation, tests, manifest, raw exports and raw `archi-fidelity.json` findings
remain unchanged. Earlier implementation files listed above remain part of Increment 1.

No existing publication/parser/identity/recovery implementation, test, protected
fixture, RAD Markdown, architecture-release authority or generated architecture
file was modified.

## Mechanisms reused

`generate_views` supplies the secure DTD-rejecting XML parser, namespaces, offline
XSD resolver, global identifier validation, serialization and subtree signatures.
`synchronise_baseline.load_inputs` and `Baseline` validate existing source/crosswalk
authority; `Baseline.chain` supplies established source context. Existing persisted
source paths are compared exactly with their standing retained. No competing path
derivation or friendly-name tokenisation was added. `architecture_release.load_release`
reads release authority. Existing identity/adoption/binding checks, publication
preservation tests, frozen recovery replay and independent deterministic reproduction
remain independent verification mechanisms.

A small shared `Exchange` index resolves Properties once for both diagnostics and
fidelity comparison. The historical `generate_views.Model` is intentionally limited
to Capability/Composition and single values keyed by definition ID; extending it for
mixed-type, multiplicity-preserving intake would change protected machinery. The
new index uses the existing parser/XSDs and retains invalid records as unresolved
diagnostic locators, without allocating identity or weakening historical checks.

## Active-model identity and naming results

The [diagnostic summary](2026-10-18-archimate-roundtrip-increment-1/active-diagnostic-summary.json)
is derived from the accepted XML, its definitions and validated source bindings.
The complete per-element inventory can be reproduced using the procedure's
`diagnose --baseline-context` command. Two independent command processes produced
byte-identical full inventories. Audit execution is **PASS**; future identity and
naming readiness are **NONCONFORMANT**, as expected for the accepted unmigrated model.

| Canonical ID measurement | Result |
| --- | ---: |
| Total ArchiMate elements | 1,299 |
| Single valid UUID32 `canonicalId` | 0 |
| Missing future `canonicalId` | 1,299 |
| Malformed future claims | 0 |
| Multiple future claims | 0 |
| Duplicate future UUID32 values | 0 |
| Historical EC aliases | 14 |
| Duplicate EC aliases / conflicting frozen bindings | 0 / 0 |

The absence of malformed/duplicate future claims does not establish identity
readiness: there are no future claims. All 14 EC values remain on their exact
registered Exchange elements. The frozen register SHA-256 remains
`aadfaa9c1c6d9d35908036683fff4837537de9fe0683c192e2fd0959fced6073`.
No EC value was converted, removed, replaced or counted as a Canonical ID.

UUID recognition uses exact lowercase `[0-9a-f]{32}` from the final assessment;
it rejects separators, prefixes/URNs, uppercase, whitespace, wrong length and
nonhexadecimal text. It does not impose type prefixes or infer UUIDs from XML IDs.
Uniqueness, single-value multiplicity and optional previous-snapshot binding
conflicts are separate checks. Lifetime immutability/non-reuse cannot be proved by
this snapshot: no active future UUID allocation/tombstone history exists. Possible
reuse versus representation-binding drift is exposed for adjudication, not inferred.

| Naming evidence | Available | Missing / qualification |
| --- | ---: | --- |
| Native element Name, existing display carrier | 1,299 | 0 missing/ambiguous |
| Future `displayName` Property | 0 | 1,299; optional duplication of native carrier |
| Future `friendlyName` Property | 0 | 1,299; derivation grammar unresolved |
| Future `canonicalName` Property | 0 | 1,299 |
| Legacy qualified structural path | 946 | 353 lack this source-path evidence |
| Structural-path standing | 190 Established | 756 Proposed; no promotion |
| Legacy short-name Property | 719 | 580 absent |
| Legacy reviewed-name Property | 162 | Kept distinct from display-name authority |
| Legacy name aliases | 44 | Separate from identifier aliases |
| Legacy historical structural-ID aliases | 430 | Preserved in their existing channel |

There are **34** native-Name/legacy-short-name differences and **three duplicate
legacy short-name pairs**. Exact owners and strings are in the summary. These are
legacy naming observations, not identity merges or conflicts with nonexistent
future Properties. No persisted future-name conflict was found; none is populated.
All 946 projected legacy qualified paths agree with their bound source paths.
Their deterministic context was inspected through existing ancestry machinery;
no derived values were written. Qualified naming coverage outside that source
population and a governed friendly-name grammar remain unresolved.

The diagnostic separately inventories source/capture keys, Snapshot Reference IDs,
Reference IDs, native definition IDs, construct types and both alias channels.
`legacy canonical_id` corresponds conceptually to future `canonicalName`;
legacy `canonical_name` remains a short label, with no assumption that it equals
future `displayName`.

## Controlled future-contract specimen

The [synthetic specimen](../../docs/archimate/capability-architecture/tests/fixtures/roundtrip/future-contract.xml)
contains **3 elements, 1 Realization, 1 View, 4 visual objects and 1 visual
connection**, including an annotation and two ordered bendpoints. All three elements
carry separate literal `canonicalId`, `displayName`, `friendlyName` and `canonicalName`
Properties, plus native type metadata. The Feature uses native Capability,
`harmoniaType=Feature` and the existing `v0.2 / Construct Type=feature` pattern.

The three fixed UUID32 strings exercise leading zero, digit and letter values:
`00000000000040008000000000000001`, `1234567890ab4def8123456789abcde0`, and
`abcdef0123454abc8def0123456789ab`. These are synthetic transport strings, not
allocated Harmonia identities. Names/responsibilities are explicitly synthetic;
no governed element was cloned and assigned an identity. Literal qualified paths
establish no new naming derivation or semantic specialisation.

The [specimen diagnostic](2026-10-18-archimate-roundtrip-increment-1/specimen-diagnostic.json)
passes offline structural checks and reports three unique valid UUID32 values,
all three distinct naming Properties on every element and no naming/type conflict.
Its pinned SHA-256 is
`0f0c0d4711ee8db12b2c6103958f88f4bde190802ced1793ead2052c73cc7cfb`.
That diagnostic is input-fixture validation. The subsequently supplied real export
now provides separate empirical transport evidence, described below; neither accepts
governed architecture changes.

## Real export provenance and results

At initial preparation, autonomous GUI execution was unavailable and both probes
correctly stopped at **AWAITING ARCHI EXPORT**. Mark has since supplied both real
exports. The [fidelity report](2026-10-18-archimate-roundtrip-increment-1/archi-fidelity.json)
records `operator=Mark` and `archi_5_10_0_no_edit_full_model=true`: Archi 5.10.0,
no intentional edits, entire model/all Views, organisation included, English
import/export. This is an operator declaration tied to the hashes, not independent
authentication of GUI execution. Both published inputs and exports have **zero
XSD/identifier/reference validation issues**.

| Probe | Empirical result |
| --- | --- |
| Accepted Release 1.0.6 → real unchanged export | **DIFFERENCES**, fully decomposed below; not an architecture change set |
| Synthetic future contract → real unchanged export | **Contract Properties preserved exactly**; organisation identity loss and added presentation styles remain findings |
| Baseline self-comparison, initial harness control only | EQUIVALENT; does not constitute Archi evidence |

SHA-256 pins, verified again during this analysis:

| File | SHA-256 |
| --- | --- |
| Accepted Release 1.0.6 input | `d57a5adeeef6b0906b30a02c3ea80cc6ff6308988775e7e3a619db01564ca52a` |
| [Release export](2026-10-18-archimate-roundtrip-increment-1/archi-exports/release-1.0.6.xml) | `1d38cdf8680d1e55868a741be3cfaafc05cc0ac54e40403afe03989748485ab3` |
| Controlled specimen input | `0f0c0d4711ee8db12b2c6103958f88f4bde190802ced1793ead2052c73cc7cfb` |
| [Specimen export](2026-10-18-archimate-roundtrip-increment-1/archi-exports/future-contract.xml) | `05883bf536101594044c7aaf4c3223e1a750fb5ed54714f3214c40bff8823499` |
| Raw `archi-fidelity.json`, retained unchanged | `f615bf51bc25cb616663e53ecae7ae3c5915bd58e0ecfd63d7a606d978bf6c2c` |

The [procedure](../../docs/archimate/capability-architecture/roundtrip-increment-1.md)
remains the reproducible import/export and `verify-exports` procedure. The new
[analytical evidence](2026-10-18-archimate-roundtrip-increment-1/empirical-analysis.json)
records path-pattern counts, content invariants, changed JSON leaf paths and the
affected View IDs/names. A read-only probe reused `Exchange`, the secure parser,
subtree signatures and `publish_navigation.navigation_homes`; it asserted these
observations against the actual XML and pinned raw findings. It adds no parser,
identity matcher, reconciliation policy or comparator normalisation.

## Release 1.0.6: root causes rather than occurrence counts

The raw classifications are retained: **1,669 SEMANTIC DIFFERENCE**, **4,263
INFORMATION LOSS**, **4,688 UNEXPLAINED DIFFERENCE**, and **59 SEMANTICALLY
EQUIVALENT NORMALISATION**. There are no raw presentation-preserving entries.
These are XML comparison events, not counts of independent architectural changes.
The following decomposition accounts for every event. Analytical explanations do
not rewrite the JSON classifications or make the whole-model comparison pass.

### Property text normalisation: all 1,669 semantic findings

All paths end in `element|relationship[id=…]/properties/property[key=…]/value/text()`.
There is one actual behaviour: Archi collapses repeated literal spaces in Property
text, including spaces **inside quoted JSON strings**.

| Property key | Changed values | Owner |
| --- | ---: | --- |
| `v0.2 / Snapshot Record JSON` | 615 | Element |
| `v0.2 / Source Locators` | 409 | Element |
| `v0.2 / Deferred Relationship Assertions` | 291 | Element |
| `v0.2 / Derivation Provenance` | 85 | Element |
| `v0.2 / Source Assertion JSON` | 269 | Relationship |
| **Total** | **1,669** | **1,400 element / 269 relationship values** |

Every before/after value parses as JSON, and **every parsed JSON value differs**.
This is not JSON pretty-printing equivalence. Recursive comparison finds exactly
**2,328 changed string leaves**, with no changed keys, object/list structure, list
lengths or non-string scalars. Every changed leaf is reproduced by collapsing runs
of ASCII spaces. Of those leaves:

- **2,317** are source/evidence `text` fields whose leading line indentation is
  reduced. Paths include `sources/*/text`, `provenance/evidence/*/text`, review,
  placement and responsibility-relocation evidence, and deferred source assertions.
- **11** are `governed_definition_latex` strings: eight current records and three
  `pre_review_record` copies. Six lose indentation only; five also lose internal
  repeated spaces. For example, `Encoded   (Scoped illustration; …)` becomes
  `Encoded (Scoped illustration; …)` within a retained definition.

The installed same-hash Exchange importer corroborates the mechanism:
`parseProperties` calls `getChildElementText(..., "value", true)`, which uses JDOM
`getTextNormalize()`. Escaped JSON line breaks remain escapes at that stage, while
literal indentation spaces within JSON string values collapse. No domain type,
identity, name, status, owner/parent, qualified path or endpoint JSON field changes.
No graph/responsibility change is detected, but **semantic-element and relationship
Property content does change**. Verbatim source/provenance, indentation-sensitive
Markdown and definition evidence cannot be declared semantically equivalent.
Retain these findings as real exact-content loss/change; do not normalise JSON or
whitespace to hide them. The accepted input and frozen evidence remain intact.

### Organisation identity loss and ordering: most apparent losses are positional

| Raw INFORMATION LOSS path pattern | Events |
| --- | ---: |
| `organizations/…/item[position=…]/@identifier` | 3,890 |
| Organisation item subtree absent at compared position | 268 |
| Organisation `@identifierRef` absent at compared position | 25 |
| Organisation label subtree absent at compared position | 25 |
| Property-definition `name/@xml:lang` absent | 55 |
| **Total** | **4,263** |

Archi drops organisation item IDs and reorders the organisation tree. The comparator
deliberately pairs anonymous items by position rather than inventing folder identity.
After reordering, it consequently compares some different branches and reports
subtree/reference/label removals and additions. A removed subtree is one finding;
its descendants are not separately walked. **3,890 is therefore not the actual
number of lost item identities.** Direct XML enumeration establishes:

| Organisation invariant | Published → exported |
| --- | --- |
| Items | 7,096 → 7,096 |
| Items carrying `identifier` | **7,096 → 0** |
| Labelled folders | 3,749 → 3,749 |
| Reference items | 3,347 → 3,347: 1,299 elements, 852 relationships, 1,196 Views |
| Empty folders | 636 → 636 |
| Multiset of exact full labelled folder paths, including label language | Equal |
| Multiset of `(full labelled path, identifierRef)` with multiplicity | Equal |
| Multiset of empty-folder paths | Equal |
| Existing `navigation_homes` result | Equal |

Top roots change from `Application, Business, Motivation, Relations, Strategy, Views`
to `Strategy, Business, Application, Motivation, Relations, Views`; descendant
ordering also changes. **No folder, reference target, membership or empty context is
lost in these exports.** The apparent label/ref changes at positional paths are
comparison correspondence artefacts, not renamed folders or changed memberships.
For this no-edit specimen, the observed reordering preserves labelled navigation
structure and semantic membership. It does not prove persistent folder/reference
identity, which is genuinely lost for **all 7,096 items**. It also does not prove
unchanged navigation sibling order. Path/membership equality is a no-edit structural
observation, never an architectural identity rule or a matcher for future edits.

The 55 remaining loss findings have a different cause: all Property-definition
names lose `xml:lang="en"`. Their **name text and datatype survive**, and the
language attributes on owner Property values survive. This is real definition-label
language metadata loss, not loss of a Property key or its values. It is not suppressed
by definition-ID rekeying equivalence.

### All 4,688 unexplained findings: four observed patterns

| Raw UNEXPLAINED DIFFERENCE path pattern | Events | Empirical explanation / assessment |
| --- | ---: | --- |
| Visual node `style/lineColor/@a` added | 3,910 | Absent alpha becomes `100`; narrowly presentation-preserving in this Archi version |
| View `documentation/text()` | 34 | Exactly one terminal LF removed; real exact-text information loss |
| View `child-order` | 15 | Connection sibling order changes to source-node traversal order; visual equivalence unproved |
| Organisation positional/order effects | 729 | Reordered anonymous branches, explained by the independent structural checks above |
| **Total** | **4,688** | No residual unaccounted-for path pattern |

The 729 organisation events comprise **485 added item subtrees, 117 child-order
events, 70 label-text changes, 25 added label subtrees and 32 identifierRef changes**.
They share the organisation behaviour already described; they are not 729 additional
architectural changes. Together with the positional loss entries above, they must
not be counted as independently lost hierarchy/content.

For all **3,910** alpha entries the sole change is absent `a` → `a="100"`; RGB and
other existing node-style content remain identical. The bundled XSD says alpha is
optional and 100 is opaque, but **declares no default**. The stronger evidence is
the inspected Archi importer/exporter: `getRGBAlphaValue` returns internal 255 for
an absent alpha and for explicit 100; `writeRGBAttributes` exports internal 255 as
100. With the unchanged RGB, this establishes a **PRESENTATION-PRESERVING
TRANSFORMATION for these occurrences in Archi 5.10.0**. It does not establish a
universal XSD/cross-tool default or justify dropping other style differences.
This is a report-level interpretation; all 3,910 raw findings remain visible.

For all **34** Documentation findings, `after == before[:-1]` and the omitted
character is a final LF. All internal line breaks, indentation and other characters
survive. These include frozen source-evidence Views such as `Source evidence —
bec-review`; their complete IDs/names are retained in the analytical evidence.
Terminal newline loss may leave prose rendering unchanged, but exact/verbatim
evidence fidelity fails. It is therefore explained **INFORMATION LOSS**, not a
blanket trim-equivalence rule. The exact internal stage removing this LF has not
been isolated; the transformation itself is established. All element, relationship
and model Documentation survives exactly.

All **15** View order events concern **connection siblings only**. Node order is
unchanged. In every affected View, the exported order equals the original
connections grouped by source node in the existing node traversal order, preserving
order within each source. This agrees with exporter `writeConnections`, which
traverses `diagram.eAllContents()`. Affected Views are five overview models, nine
`Behaviour 1` pages and `Historical Motivation — legacy source assertions`; exact
IDs/names are in the analytical evidence. Connections' identities, endpoints,
styles and ordered routing points survive. This explains serialization order but
does **not** prove unchanged paint/z-order at crossings. Keep it as an explained
presentation transformation with unresolved visual equivalence; no presentation
suppression or replay is implemented.

### Already-proven representational equivalences

The existing **59** equivalent findings are **55 Property-definition ID rekeys**
and **four order changes** in uniquely keyed element, relationship, Property-definition
and View collections. Exact unique Property names resolve the rekeyed definitions;
every owner/value/language/multiplicity association is independently compared.
All 55 definition names and datatypes survive. The language loss just described
remains separately visible. XML syntax/prefix/element-only-indentation equivalences
remain as originally implemented; leaf data is not trimmed. The installed plug-in
SHA-256 is unchanged:
`7442b0a8ee987b26ea53c98ce040890cb7c18945771a5f364f470f03de295e1a`.

## What actually survives: semantic content, hierarchy and relationships

Independent ID-keyed checks remove only Properties from copies of each matched
semantic record, then compare existing subtree signatures. **Every one of the
1,299 elements and 852 relationships is otherwise exactly identical.** This proves
native IDs/types, Names, Documentation, relationship endpoints and residual semantic
attributes/content survive. It is more specific than inferring stability from
unchanged record counts.

| Governed/content observation | Empirical result |
| --- | --- |
| Element IDs/types/native Names/Documentation | 1,299/1,299 exact |
| Element Property instances | 19,658 → 19,658; no missing keys/claims; only the 1,400 text changes above |
| Relationship IDs/types/endpoints/Names/Documentation and other non-Property content | 852/852 exact |
| Relationship Property instances | 3,978 → 3,978; only 269 source-assertion text changes above |
| Native relationship graph | 670 Composition, 157 Realization, 15 Association and 10 Influence edges unchanged |
| Legacy structural qualified path Properties | All 946 exact, on the same elements |
| Legacy short naming Properties | All 719 exact, on the same elements |
| Historical EC identifier aliases | All 14 exact values on their exact original elements |
| Harmonia construct-type Properties | Preserved on the same elements |

Consequently **no governed architectural hierarchy, relationship semantic type,
source/target, semantic Name or native Documentation change is detected**. Composition
and qualified-path evidence are checked separately from navigation folders. This
does not erase the evidence-Property changes: exact provenance and embedded
definition text really are altered. No relationship Canonical ID policy is invented.
Existing relationship identities/endpoints are stable enough to support future
Increment 2 comparison, but a future comparator must distinguish the known
source-assertion transport damage from intentional edits and retain that damage
as evidence. This analysis neither implements that distinction nor authorises
automatic relationship reconciliation.

## Presentation fidelity and controlled-specimen results

All **1,196 View identities/attributes/names**, **3,915 View-local visual object
identities** and **1,449 View-local connection identities** survive, in the same
Views. Membership, node nesting/parent structure, semantic references, visual
endpoints, coordinates, dimensions, labels/annotations and ordered bendpoints are
retained. The complete raw comparison exposes only the alpha, Documentation and
connection-order classes described above for Release 1.0.6 presentation. There is
no observed geometry/routing loss. This supplies useful preservation evidence for
Increment 3, while the connection paint-order question prevents claiming complete
unchanged rendering. No screenshot/pixel comparison or View replay was performed.

The real controlled-specimen export proves the future Properties independently of
the legacy model:

| Property | Exact value, language, multiplicity and correct element association |
| --- | ---: |
| `canonicalId` | **3/3**, all 32 characters unchanged, including leading zeros |
| `displayName` | **3/3** |
| `friendlyName` | **3/3** |
| `canonicalName` | **3/3** |
| `archimateType` | **3/3** |
| `harmoniaType` | **1/1**, Feature on native Capability |

All **17 element Property instances** survive, including the legacy Feature
construct-type Property. Native Names/Documentation/types, the Realization identity,
endpoints/content, View membership/geometry/annotation and both ordered bendpoints
are retained. Seven definition IDs are regenerated, while semantic Property names
and owner/value associations remain exact. This proves the carrier does not depend
on generated definition IDs remaining stable. No specimen UUID is assigned to any
Release 1.0.6 instance; the specimen remains non-governed.

The specimen still has raw **9 INFORMATION LOSS** events (all nine organisation
item IDs), **8 equivalent** events (seven definition rekeys and definition order),
**0 semantic differences** and **10 unexplained** events. The latter comprise five
added style subtrees on four nodes/one connection plus five collateral child-order
entries caused by inserting those styles. Archi materialises omitted styles as
explicit colours/fonts/opacity, including `Sans` size 9. This is one default-style
materialisation behaviour, not ten independent presentation changes. The added
styles reflect this Archi environment; style-absent input does not establish an
environment-independent appearance. **Presentation equivalence remains unproved
for these styles**, and no new style normalisation is introduced.

## Scalability measurements

| Release 1.0.6 diagnostic metric | Result |
| --- | ---: |
| Semantic elements | 1,299 |
| Relationships | 852 |
| Views | 1,196 |
| Visual objects, including annotations | 3,915 |
| Visual connections | 1,449 |
| Largest View | Business Capability Model — v0.2 |
| Largest View ID | `id-9ac38e89ff405925aaa995f4f9ae79e8` |
| Largest View element occurrences / connections | 200 / 182 |
| Published XML size | 19,806,014 bytes / 19.806014 decimal MB |

Metrics are computed for each compared model and are not thresholds. Generation
and validation timing instrumentation is deferred to the existing publication/
verification reporting layer; this diagnostic neither runs publication nor adds
volatile timing to deterministic output.

## Tests and validation

Added **26** tests covering UUID lexical form, missing/multiple/duplicate claims,
EC separation, observed immutable-binding conflict/possible reuse, distinct names,
Feature/native-type compatibility, exact Property rekey/value/owner fidelity,
datatype/key ambiguity, semantic versus visual ordering, folder-ID loss, whitespace/
language/content loss, unknown metadata, relationships, geometry/routes, invalid/
partial inputs, secure parsing, output-write protection, determinism, source pins
and the unauthenticated/self-copy versus actual-execution distinction.

No tests or production code were changed for this empirical follow-up. The initial
implementation validation is retained below as historical execution evidence;
the subsequent follow-up checks are recorded separately. All commands used bounded
waiting. Python verification used
`PYTHONDONTWRITEBYTECODE=1` in final runs. Logs below are preserved under the evidence
directory. No TIMEOUT/STALLED operation occurred.

| Command/check, from repository root unless stated | Bound | Actual outcome / evidence |
| --- | ---: | --- |
| `python3 -m unittest discover -s docs/archimate/capability-architecture/tests -v` | 120 s | **PASS**, 128 tests, 64.894 s; [publication.log](2026-10-18-archimate-roundtrip-increment-1/publication.log) |
| `python3 -m unittest discover -s scripts/tests -p 'test_architecture_*.py' -v` | 60 s | **PASS**, 25 tests, 29.297 s; [identity-tests.log](2026-10-18-archimate-roundtrip-increment-1/identity-tests.log) |
| `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` | 180 s | **PASS**, 90 tests, zero failures/errors/skips, BUILD SUCCESS, 31.572 s; [architecture.log](2026-10-18-archimate-roundtrip-increment-1/architecture.log) |
| `tools/synchronise_baseline.py --check` through the existing Makefile | 90 s | **PASS**, current XML and deterministic publication agree; [checks.log](2026-10-18-archimate-roundtrip-increment-1/checks.log) |
| `tools/report_baseline.py --check` through the existing Makefile | 30 s | **PASS**, current generated report/guide unchanged; same log |
| `tools/verify_baseline_outputs.py` | 120 s | **PASS**, all 14 generated outputs reproduce byte for byte in independent processes; [determinism.log](2026-10-18-archimate-roundtrip-increment-1/determinism.log) |
| `tools/recover_v02.py --check` through the existing Makefile | 90 s | **PASS**, protected 1.0.5A replay unchanged; same checks log; XML hash `f85be36ff6edb3c6491dd526c42d4207db9214154da437941b709de34dc106a1` |
| Additional `tools/report_recovery.py --check` against live release | 30 s | **FAIL**, existing `Stale recovery release evidence` guard; same checks log; explanation below |
| `python3 scripts/architecture_identity.py validate` | 30 s | **PASS**, 14 registered records; [identity-validation.log](2026-10-18-archimate-roundtrip-increment-1/identity-validation.log) |
| `python3 scripts/architecture_identity.py bindings --check` | 60 s | **PASS**, exact 14 retained bindings; [bindings.log](2026-10-18-archimate-roundtrip-increment-1/bindings.log) |
| `python3 scripts/architecture_release.py check` | 30 s | **PASS**, 1.0.6 / previous 1.0.5; [release.log](2026-10-18-archimate-roundtrip-increment-1/release.log) |
| Two independent active diagnostic invocations | 20 s each | **PASS**, byte-identical full JSON and successful audits; summary above |
| Offline synthetic specimen validation/diagnostic | 10 s | **PASS**, 3 unique UUID32 claims, all names and correct Feature metadata |
| Full baseline self-comparison control | 60 s | **EQUIVALENT**, no findings; explicitly not an Archi export |
| Initial `verify-exports`, before Mark supplied exports | 10 s | **AWAITING ARCHI EXPORT** at that time, expected exit 3, both source pins verified; superseded by current empirical results above |
| Git/whitespace/input-preservation review | Bounded local checks | **PASS**, no tracked governed/generated/protected input changes |

The extra historical report command fails because unchanged
`report_recovery.render()` loads the **live** root release and compares it with
frozen **1.0.5** evidence, while the accepted current release is **1.0.6**. This is a
report-context incompatibility, not a model replay or new diagnostic/test failure.
The protected replay, existing recovery tests and current report checks pass. No
historical evidence or implementation was rewritten to conceal this failure.

Follow-up validation after the real exports were supplied:

| Check | Bound | Outcome |
| --- | ---: | --- |
| Same publication/round-trip unittest discovery command above | 150 s | **PASS**, 128/128, 73.078 s; [empirical-publication.log](2026-10-18-archimate-roundtrip-increment-1/empirical-publication.log) |
| Same offline Maven architecture command above | 180 s | **PASS**, 90 tests, zero failures/errors/skips; BUILD SUCCESS, 32.961 s; [empirical-architecture.log](2026-10-18-archimate-roundtrip-increment-1/empirical-architecture.log) |
| `tools/verify_baseline_outputs.py` | 120 s | **PASS**, all 14 outputs reproduce byte for byte in independent processes, release unchanged; [empirical-determinism.log](2026-10-18-archimate-roundtrip-increment-1/empirical-determinism.log) |
| Read-only XML/raw-findings analytical assertions | 60 s | **PASS**, all path counts reconcile, JSON content changes characterised, semantic/organisation/Property-owner preservation checked; [empirical-analysis.json](2026-10-18-archimate-roundtrip-increment-1/empirical-analysis.json) |
| Input/raw evidence hashes and report link/whitespace checks | Bounded local checks | **PASS**; sources, exports, raw report, implementation and protected governed files unchanged |

## Remaining review and recommendation

**No remaining observed transformation prevents safe establishment of UUID32
`canonicalId`, `displayName`, `friendlyName` and `canonicalName` in the forms
demonstrated by the controlled specimen.** Their exact values and correct element
associations survive real Archi 5.10.0 transport despite definition-ID regeneration.
The identity/naming carrier prerequisite is therefore met. **Whole-model lossless
round-trip fidelity is disproved**, and the aggregate status correctly remains
TRANSPORT FINDINGS rather than PASS.

| Question | Evidence-based conclusion |
| --- | --- |
| Can UUID32 Canonical IDs survive unchanged? | Yes, all three test values exactly, including leading zeros; no claim of lifetime immutability/non-reuse proof |
| Can `displayName`, `friendlyName`, `canonicalName` survive unchanged? | Yes, each 3/3, independently preserved with exact key/value/owner association |
| Can Harmonia semantic-type metadata survive? | Yes, Feature on Capability, with native and semantic type kept distinct |
| Which structures are normalised? | Definition IDs and keyed collection order; organisation order; explicit opaque line alpha; source-grouped connection order; specimen default styles. Only the proven equivalences above are accepted analytically |
| Which information is lost/changed? | Exact whitespace inside 1,669 evidence Properties, 34 View final LFs, 7,096 organisation item IDs, 55 definition-name language attributes |
| Does unchanged View presentation survive sufficiently for later preservation work? | Identity, geometry, membership, labels and routing survive; connection paint-order equivalence and default-style appearance remain unproved |
| Are relationships stable enough for future comparison? | Yes for IDs, types, endpoints and graph/content outside the 269 source-evidence Property changes; full verbatim evidence stability fails |
| Is there a remaining identity/naming carrier problem? | None observed for UUID32 and the tested names. Arbitrary whitespace-sensitive Property text remains unsafe |

The last qualification is material: the specimen proves the tested naming forms,
not every possible string. The importer normalises Property text. A future approved
name requiring leading/trailing or repeated whitespace would need exact transfer
verification; a value altered by that behaviour would be a transport blocker for
that value. No naming grammar is narrowed here to manufacture a pass. All 1,299
existing native Names and all tested naming Properties survive; no name damage is
observed in these exports.

Mark + ChatGPT review remains necessary for these distinct matters:

- **Exact evidence preservation:** the real Property/source-definition and View
  Documentation damage must remain explicit. It cannot be accepted as an
  architectural edit or fixed by blanket whitespace/JSON normalisation. Any later
  intake must preserve authoritative evidence rather than silently replacing it
  with damaged export text.
- **Organisation and presentation continuity:** folder/reference item identity is
  lost, sibling ordering changes, and connection paint order/default-style visual
  equivalence is not proved. These affect later navigation/presentation handling;
  they do not corrupt the tested element identity/naming Properties.
- **Naming authority:** separately authorised establishment still needs approved
  friendly-name derivation/source rules, treatment of incomplete/proposed qualified
  context and form-specific naming provenance. Literal transport success supplies
  none of that architectural authority. The EC alias/new-UUID decision is settled.

Recommendation: **the measured transport behaviour supports separately authorising
Canonical ID/naming establishment for the demonstrated forms**, subject to those
independent naming decisions and exact-value checks for the approved names. There
is no evidence-based reason to block UUID32 allocation on folder-ID loss or View
ordering alone. This is a readiness recommendation, not allocation/migration
authorisation, acceptance of these exports as governed replacements, or approval
of the genuine evidence losses. Increment 1 stops here.

Release 1.0.6 and all 14 deterministic generated outputs remain unchanged. No
governed architecture was modified; no real Canonical IDs were allocated; no legacy
naming field was migrated. Reconciliation, Markdown write-back, relationship
Canonical IDs, Increment 2 and presentation replay/Increment 3 were not implemented.
