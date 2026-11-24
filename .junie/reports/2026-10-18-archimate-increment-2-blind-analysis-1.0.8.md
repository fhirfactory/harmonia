<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Increment 2 — blind analysis of the Archi 1.0.8 export

**2026-10-18 — observation only; awaiting Solution Architect adjudication.**

The export adds **five semantic element records: four native Products and one
native Capability named `[Feature] Identifier Capture`**. It adds **no semantic
relationships**. Two folders and five model-tree references are added. No existing
semantic Name, type, Documentation, governed identity or governed name changes;
no additional View or visual edit is observable beyond established Archi transport
behaviour. All five added elements lack canonical metadata and Documentation.
Their identity as genuinely new architectural instances remains unresolved.

## 1. Artefacts and authority

Paths below are repository-relative. `P`, `I` and `C` identify the actual XML
inputs used throughout this report; line numbers refer to their original files.

| Input | Artefact | Bytes | SHA-256 |
| --- | --- | ---: | --- |
| P: current published/pre-edit comparison baseline | `docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml` | 20,800,334 | `504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a` |
| I: incoming edited export | `docs/archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml` | 21,837,077 | `4d178c7625744f34cfc34213468bc2c6e9ee833c570bfbbfcf65916ab9fe1af2` |
| C: preserved, real no-edit Archi 1.0.7 export; transport control only | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-exports/release-1.0.7.xml` | 21,835,649 | `38e49fa348d015aba6b19e21b4f96dc00fddf3c4d7671b3e352b4c07748917ea` |

The root `architecture-release.json` identifies 1.0.8, previous 1.0.7. The
[1.0.8 publication evidence](../../docs/archimate/processing/publishing/1.0.8/publication-1.0.8.md),
[comparison](../../docs/archimate/processing/publishing/1.0.8/publication-comparison.json)
and [inspection guide](../../docs/archimate/processing/publishing/1.0.8/archi-inspection-1.0.8.md)
identify P as the unchanged 1.0.7 architecture/presentation with one release-value
update. Independently restoring that value **in memory** reproduces the entire
published 1.0.7 byte stream, SHA-256
`def447de2f522737c8b9c30dd9850a57bfec71eaa01f0820ff49283c40fba4bb`.
Thus C is a justified transport control, not a substitute architecture baseline.
This establishes an appropriate comparison, not the unrecorded native edit history.

Read: root/docs `AGENTS.md`, the ingest README, and the current
[axioms](../../docs/markdown/governance/architectural-axioms.md),
[identity/naming](../../docs/markdown/governance/canonical-identity-and-naming.md),
[navigation/View convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md),
[mapping](../../docs/markdown/governance/rad-archimate-model-mapping.md) and
[release policy](../../docs/markdown/governance/architecture-release-policy.md).
AX-14 requires separate semantic/navigation/presentation and identity roles;
AX-15/17 preserve uncertainty and candidate standing; AX-18 prevents names,
integration or collaboration from supplying domain authority. Publication and
processing evidence establish representation/transport facts, not new architecture.
The explicit observation-only task prohibits a release increment; none was made.

## 2. Comparison method

Used the existing read-only `diagnose_roundtrip.py compare` unchanged on **P → I**
and **C → I**, then `diagnose --model I --previous P --baseline-context`.
Each command used a 60-second timeout and `python3 -B`; outputs are temporary
`/tmp/harmonia-108-blind-{transport,control,diagnostic}-root.json` files.
No operator/version attestation was invented for I.

Independent read-only XML inspection indexed semantic elements, relationships,
Views and visual objects by exact Exchange identifiers. Property definitions
were resolved by their unique exact keys, retaining datatypes, languages,
values, multiplicity and owners. Existing Canonical IDs and source bindings
were checked separately. No objects were matched by names.

Organisation folder-path and `(path, identifierRef)` **multisets** measured observable
navigation without claiming recovered folder identity. Visual structure, geometry,
styles, nesting, connection order and bendpoints were compared independently.
No folder or visual containment generated a semantic edge.

As a completeness check, subtracting only the five added elements and seven added
organisation items from an **in-memory copy** of I, and accounting for C's known
1.0.7 release value, gives an exact whole-tree structural signature match with C.
This check ignores element-only formatting indentation/tails, retaining content
text, attributes, references and child order. No source file was rewritten and no
new comparator equivalence or reusable reconciliation implementation was added.
Pre-existing untracked `processing/ingestion/1.0.8/blind-observation/` files were
neither read for their conclusions nor modified.

## 3. Summary counts

Counts describe records and observable differences, not accepted architecture.
Metadata issues overlap the five element candidates and must not be added to them
as further semantic elements.

| Collection/change | P → I | Classification/result |
| --- | ---: | --- |
| Semantic elements | 1,299 → 1,304 | 5 Architectural change candidates; 0 removed |
| Native types | Capability 841 → 842; Product 0 → 4 | Other type populations unchanged; no retained element retyped |
| Retained semantic Names / Documentation / identity metadata | 1,299 retained | 0 editorial changes; evidence-Property transformations below |
| Semantic relationships | 852 → 852 | 0 added, removed or substantively changed |
| Relationship types/endpoints | 670 Composition, 157 Realization, 15 Association, 10 Influence | All identities, types and endpoints retained |
| Property definitions | 61 → 61 | 0 keys/datatypes added, removed or changed; 61 IDs rekeyed; 61 name-language attributes lost |
| Organisation folders | 3,749 → 3,751 | 2 Navigation/presentation changes |
| Organisation references | 3,347 → 3,352 | 5 Navigation/presentation changes; no existing membership lost/moved |
| All organisation items / empty folders | 7,096 → 7,103 / 636 → 636 | 7 additions; all 7,096 original item IDs absent after transport |
| Views / visual objects / connections / bendpoints | 1,196 / 3,915 / 1,449 / 2,922, unchanged | 0 added/removed; 0 further edits against C |
| Valid unique Canonical IDs | 1,299 → 1,299 | Same owners/values; 5 new elements missing IDs; 0 malformed, duplicate or multiple claims |
| Three governed names / `archimateType` | Each retained on all 1,299 original elements | Each absent on all 5 additions |
| `harmoniaType=Feature` / EC bindings | 178 / 14, unchanged | No new Feature Property; all EC bindings retained |

Model identifier `id-80b065eaa4c550eba09c0dbd41b1d631`, model Name, Documentation,
textual-projection reference and release **1.0.8** are unchanged against P.
The C → I release difference is the already-governed publication transition.

## 4. Architectural change candidates

Each row is an **Architectural change candidate** because an explicit semantic
`/model/elements/element` record exists in I with an Exchange ID absent from both
P and C. These are not merely folder labels or diagram annotations.

| Finding | Exact Exchange identifier | Native `xsi:type` | Exact native Name | I line |
| --- | --- | --- | --- | ---: |
| A1 | `id-727a264026584f80bc3304ff8383dc92` | Capability | `[Feature] Identifier Capture` | 34747 |
| A2 | `id-234b080d0ea54d57bb0001a2cb861080` | Product | `[Product] Provider Registry - Satellite` | 87199 |
| A3 | `id-bfdd958364d1466a914d474dce8baab9` | Product | `[Product] Provider Registry - Central` | 87202 |
| A4 | `id-3c5ae26723224eb0a6e6a70d70fe2c83` | Product | `[Product] Practitioner Collaboration Service` | 87205 |
| A5 | `id-d84d8e1f988c4bc998b18834aabfcd74` | Product | `[Product] Health Information Exchange Service` | 87208 |

Every addition contains only its Name child. Each has **zero incident semantic
relationships and zero visual appearances**. No Composition, Realization,
Association or other added relationship exists to report endpoints for.
The four Products introduce a native type absent from P. Names containing
“Service” do not retype them as BusinessServices, establish component allocations,
or assign operational/domain authority.

## 5. Navigation/presentation-only changes

Both changes below are classified **Navigation/presentation change**:

- **N1:** I:124841 adds `[Feature] Identifier Capture` beneath
  `Strategy / Business Layer / Business Enabling Capabilities / [CT1] Entity Information Enablement / [CT2] Client Administration / [CT3] Person Identity Administration`.
  Its single child, I:124843, references A1. This is one folder plus one reference.
- **N2:** I:129483 adds `Business / Products`, with four direct references at
  I:129485–129488 to A2, A3, A4 and A5 respectively. This is one folder plus four references.

N1 does **not** establish Person Identity Administration → Identifier Capture
Composition or Feature framing. Existing context element
`id-29e0bf94882559a7b6ed9d092303a560` remains referenced at the same folder path
(I:124845); insertion shifts its sibling position. N2 establishes no semantic
Product container or Product relationships. The two folder identities/edit history
cannot be recovered from identifier-free organisation items.

All existing folder-path/reference memberships and relative retained-item order
match C after removing these additions. Existing Views and their navigation are
unchanged against C. None of A1–A5 has been placed in a View.

## 6. Archi/Open Exchange round-trip transformations

The following are each classified **Archi/Open Exchange round-trip transformation**.
They reproduce the preserved
[1.0.7 empirical acceptance](2026-10-18-archimate-canonical-identity-establishment.md#real-archi-proof-and-increment-2-gate)
and its `empirical-acceptance.json`; exact retained-record equality with C supplies
the control evidence. Recognising a transformation does not erase information loss
or establish visual equivalence.

| Observed transformation against P | Extent/evidence |
| --- | --- |
| Definition IDs replaced and property references redirected | All 61 unique keys/datatypes retained, all type `string`. Examples: `governed-canonical-id` → `propid-18`; `governed-display-name` → `propid-19`; `governed-friendly-name` → `propid-20`; `governed-canonical-name` → `propid-21`; `governed-archimate-type` → `propid-22`; `governed-harmonia-type` → `propid-34` |
| Definition-name language loss | All 61 `<name xml:lang="en">` attributes absent in I; no definition-name text change |
| Organisation identity loss and ordering | All 7,096 original item IDs absent; existing structure/memberships retain the characterised Archi ordering. New items also have no organisation IDs |
| Embedded evidence-text whitespace changes | 1,669 Property-value changes: 615 Snapshot Record JSON, 409 Source Locators, 291 Deferred Relationship Assertions, 269 Source Assertion JSON, 85 Derivation Provenance; exactly equal to C, without new JSON/whitespace normalisation |
| Node style defaults materialised | 3,910 previously absent alpha values become `100`, as in C |
| View Documentation newline loss | 34 losses of exactly one final LF, as in C |
| Connection sibling ordering | The same 15 Views reorder by source traversal; paint-order equivalence remains unproved |
| Exchange serialization/collection ordering | Formatting/newlines and retained keyed-collection ordering match the no-edit control; semantic/visual identities do not change |

All 1,299 retained element records, 852 relationship records, 1,196 View subtrees
and 61 property definitions match C structurally, including Properties and
presentation. There is no additional unexplained content delta after accounting
for A1–A5, N1–N2 and the known release value.

## 7. Metadata/identity reconciliation issues

**M1–M5 — Metadata/identity reconciliation issue**, one for each of A1–A5:
`canonicalId`, `displayName`, `friendlyName`, `canonicalName` and `archimateType`
Properties are absent. There is also no Documentation, source/crosswalk metadata,
governance standing or explicit responsibility definition. An Exchange identifier
and native Name are present but do not establish Harmonia canonical identity.

A1 additionally lacks `harmoniaType=Feature`. `[Feature]` is observable Name text;
native type is Capability. The label and folder placement cannot establish the
governed Feature specialisation, framing, ancestry or qualified names.
Relationships, folders, Views and visual objects do not require Canonical IDs
under the current contract; their lack is not added to this issue count.

The existing diagnostic reports `audit_status=PASS` (audit execution), but identity
and naming readiness both **NONCONFORMANT**, identifying exactly these five missing
IDs/name sets. Existing ID continuity and governed naming/type conflict lists are
empty. All 14 EC aliases remain on their original owners. No duplicate Exchange
identifier, unresolved reference or offline XSD issue was found in P, C or I.

## 8. Unresolved findings for Solution Architect adjudication

Each item is classified **Unresolved / insufficient evidence**:

1. **Canonical identity/standing of A1–A5:** new Exchange records are established;
   whether they represent genuinely new architectural instances, another
   representation of existing resources, or another intent cannot be established
   without source/canonical evidence. No name-based crosswalk or acceptance inferred.
2. **A1 meaning and Feature status:** bounded definition, governed type,
   responsibility and any intended relationship to Person Identity Administration
   are unestablished. Navigation supplies no semantic endpoint assertion.
3. **A2–A5 meaning and relationships:** native Product types are observed, but their
   governed Product meaning, authority, scope, realisation and service/component
   relationships are unestablished. In particular, “Central”/“Satellite” does not
   establish deployment topology; collaboration/HIE names confer no domain authority.
4. **Historical transport limits:** original folder IDs remain lost, the established
   214 historical RAD crosswalk gaps remain unresolved, and visual paint-order
   equivalence remains unproved. These are inherited limits, not newly authored changes.

No exported semantic relation is missing merely because navigation suggests one.
The observed absence of relationships remains explicit; intended-but-unexported
relationships and the exact human edit sequence cannot be discovered from this XML.

## 9. Machinery assessment and verification

**The current framework can detect the records and metadata gaps, but does not
yet reliably perform automatic Increment 2 classification/reconciliation.** Its
[README](../../docs/archimate/framework/README.md) explicitly identifies the tool
as an Increment 1 fidelity probe. This analysis used existing machinery plus
bounded, independent inspection; it does not establish an implemented Increment 2 workflow.

P → I returns `DIFFERENCES` (exit 2): **10,698 raw events** — 1,669 semantic
differences, 4,269 information losses, 4,696 unexplained differences and 64
equivalent normalisations. These are comparator events, not architectural-change
counts. Its raw changed-record lists contain 625 elements, 269 relationships and
1,195 Views, despite no further retained-record edits against C.

C → I returns 15 events, 0 changed retained semantic/relationship/View records.
It detects all five additions but classifies them as unexplained subtrees. Its
positional organisation comparison reports apparent loss of the existing Person
Identity Administration `identifierRef` at the insertion position, although that
same reference persists at the next sibling. The independent membership comparison
establishes retention. These behaviours demonstrate why transport controls,
separate structure comparisons and unresolved identity handling matter to reliable
classification. Automatic architectural acceptance is not evidenced.

Verification: existing offline schema/reference checks **PASS**; whole-tree control
accounting **PASS**; required repository architecture suite **PASS**, 90 tests,
zero failures/errors/skips, Maven 26.377 seconds. The suite ran on JDK 21 with
`timeout --signal=TERM --kill-after=5s 180s mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`;
log: `/tmp/harmonia-108-blind-architecture-root.log`. No command timed out or stalled.
Audit/schema/test success does not approve the proposed architecture.

**Stop point reached:** report only. RADS, incoming export, governed publications,
release authority and reconciliation machinery remain unchanged. No IDs or
relationships were allocated, repaired or inferred; no publication was performed.
