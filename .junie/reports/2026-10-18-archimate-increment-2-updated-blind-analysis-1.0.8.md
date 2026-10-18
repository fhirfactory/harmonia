<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Increment 2 — blind analysis of the updated Archi 1.0.8 export

**2026-10-18 — observation only; no reconciliation or architectural acceptance.**

Compared with the first blind export, the updated XML adds **six explicit semantic
Composition relationships**, six relationship-organisation references, and edits
**one existing View**, adding one node and six visual connections. **All 1,304
element records remain unchanged**, including the five additions previously
observed. Those five still lack governed metadata. The relationships exist in the
semantic model regardless of that absence; their acceptance remains unresolved.

## 1. Artefacts, evidence and authority

Repository-relative paths are used below. `P`, `O`, `I` and `C` designate XML
inputs; `I:line` locators use physical LF-delimited lines in the unchanged current
incoming file, checked independently of XML-parser source-line annotations.

| Input | Artefact | Bytes | SHA-256 |
| --- | --- | ---: | --- |
| P — governed Release 1.0.8 publication | `docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml` | 20,800,334 | `504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a` |
| I — updated incoming export | `docs/archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml` | 21,841,727 | `b869ac466340e204077c6a733d648153594563be134c386fc7a17af609428f41` |
| O — previous blind-test incoming XML, preserved in Git | Same ingest path at commit `1294fbfbec2ec8d42465d20867082883721382cc`, blob `4c616a90f880698dd8c8155f2f0e3c88e8b88a70` | 21,837,077 | `4d178c7625744f34cfc34213468bc2c6e9ee833c570bfbbfcf65916ab9fe1af2` |
| C — real no-edit Archi transport control | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-exports/release-1.0.7.xml` | 21,835,649 | `38e49fa348d015aba6b19e21b4f96dc00fddf3c4d7671b3e352b4c07748917ea` |

O was read using `git show COMMIT:PATH` and copied byte-for-byte to
`/tmp/harmonia-108-previous-preserved-root.xml`. Its SHA-256 matches the
[first report](2026-10-18-archimate-increment-2-blind-analysis-1.0.8.md), whose own
SHA-256 remains `f31aebb5b3175165d1a7013ba26d614cbd8ec04c51094ab8bca69748e867ceb1`.
**The previous XML is preserved; no reconstruction from the report was needed.**
The first report and O supply historical observations, never architectural authority.

Read the current root/docs `AGENTS.md`,
[axioms](../../docs/markdown/governance/architectural-axioms.md),
[identity/naming contract](../../docs/markdown/governance/canonical-identity-and-naming.md),
[navigation/View convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md),
[mapping profile](../../docs/markdown/governance/rad-archimate-model-mapping.md),
[release policy](../../docs/markdown/governance/architecture-release-policy.md),
ingest/framework guidance, [1.0.8 publication evidence](../../docs/archimate/processing/publishing/1.0.8/publication-1.0.8.md)
and [real no-edit transport evidence](2026-10-18-archimate-canonical-identity-establishment.md#real-archi-proof-and-increment-2-gate).
Independently confirmed that published 1.0.8 differs from published 1.0.7 only in
one model release value, justifying C as a transport control. P remains the
architectural comparison baseline.

AX-14 separates semantics, organisation, presentation, names and identity;
AX-15/17 preserve uncertain outcomes and unaccepted architecture; AX-18 prevents
integration, coordination or naming from conferring domain authority. No inferred
intent, implementation structure or conversational explanation was used as evidence.

## 2. Comparison method

Executed the existing, unchanged `diagnose_roundtrip.py`:

- `compare --published P --incoming I`: full baseline comparison.
- `compare --published O --incoming I`: direct previous-export comparison.
- `diagnose --model I --previous P --baseline-context`: identity, naming and source-binding audit.

All used `python3 -B` and a 60-second timeout. Existing offline XSD, duplicate-ID,
Property-definition and semantic/visual/reference checks ran through these tools.
No comparator equivalence rule changed. Compressed raw outputs and validation
are retained in the [evidence directory](2026-10-18-archimate-increment-2-updated-blind-analysis-1.0.8/validation.json).

Independent read-only XML queries compared exact Exchange IDs and native types,
resolved Property definitions by unique exact keys, and compared retained records
without name-based identity matching. Organisation paths/reference memberships
were compared as multisets, independently of relationship endpoints. View-local
IDs, geometry, styles, nesting, connections and bendpoints were inspected separately.
Parsing/signatures ignore formatting indentation, without rewriting or normalising
the incoming XML or treating substantive text/geometry differences as equivalent.
No reusable ingestion/reconciliation code was implemented.

## 3. Summary counts

| Collection/finding | P | O | I | Classification / observed delta |
| --- | ---: | ---: | ---: | --- |
| Semantic elements | 1,299 | 1,304 | 1,304 | Five retained **Architectural change candidates** against P; no additional element records against O |
| Removed / substantively edited retained elements | — | — | 0 / 0 | No Name, native type, Documentation or governed metadata edits |
| Semantic relationships | 852 | 852 | 858 | Six **Semantic relationship changes**, all added Composition; no removed/edited retained relationships |
| Composition / Realization / Association / Influence | 670 / 157 / 15 / 10 | Same | 676 / 157 / 15 / 10 | No relationship retyping or retained endpoint changes |
| Organisation folders | 3,749 | 3,751 | 3,751 | Two retained **Navigation/organisation changes** against P; no further folders |
| Organisation references | 3,347 | 3,352 | 3,358 | Five retained element references plus six additional relationship references |
| All organisation items / empty folders | 7,096 / 636 | 7,103 / 636 | 7,109 / 636 | No existing path/reference membership lost or moved |
| Views | 1,196 | 1,196 | 1,196 | One additional **View/presentation change**; no View addition/removal/rename/move |
| Visual nodes / connections / bendpoints | 3,915 / 1,449 / 2,922 | Same | 3,916 / 1,455 / 2,922 | One added node, six added connections; six retained nodes change geometry, five change style |
| Property definitions | 61 | 61 | 61 | Same keys/datatypes; no additional change against O |
| Valid unique Canonical IDs | 1,299 | 1,299 | 1,299 | Same owners/values; five additions remain without IDs |
| Three governed names / `archimateType` | Each on 1,299 | Same | Same | Five retained missing sets; zero new claims/conflicts |
| `harmoniaType=Feature` / EC bindings | 178 / 14 | Same | Same | No added or altered claims |

Model identifier `id-80b065eaa4c550eba09c0dbd41b1d631`, Name, Documentation,
model Properties and release value **1.0.8** are unchanged against P and O.
Counts are observations, not accepted architecture; overlapping metadata,
organisation and presentation findings must not be counted as extra semantic elements.

## 4. Findings retained from the first export

All five element additions below **remain present with exactly the same records**.
They are new relative to P, not newly introduced by this update. Every record has
only its native Name child: no Properties or Documentation. For each, `canonicalId`,
`displayName`, `friendlyName`, `canonicalName` and `archimateType` are absent;
source/standing metadata is absent. A1 additionally lacks `harmoniaType=Feature`.

Define element organisation context **E** as the exact path:

```text
Strategy / Business Layer / Business Enabling Capabilities /
[CT1] Entity Information Enablement / [CT2] Client Administration /
[CT3] Person Identity Administration
```

| Finding | Exchange identifier | Exact native Name | Native type | Element organisation | Current semantic participation / View appearances |
| --- | --- | --- | --- | --- | --- |
| A1, I:34747 | `id-727a264026584f80bc3304ff8383dc92` | `[Feature] Identifier Capture` | Capability | E / `[Feature] Identifier Capture` | Incoming R6; no outgoing edge; one appearance in V1, newly added since O |
| A2, I:87198 | `id-234b080d0ea54d57bb0001a2cb861080` | `[Product] Provider Registry - Satellite` | Product | `Business / Products` | No semantic relationships; no View appearances |
| A3, I:87201 | `id-bfdd958364d1466a914d474dce8baab9` | `[Product] Provider Registry - Central` | Product | `Business / Products` | No semantic relationships; no View appearances |
| A4, I:87204 | `id-3c5ae26723224eb0a6e6a70d70fe2c83` | `[Product] Practitioner Collaboration Service` | Product | `Business / Products` | No semantic relationships; no View appearances |
| A5, I:87207 | `id-d84d8e1f988c4bc998b18834aabfcd74` | `[Product] Health Information Exchange Service` | Product | `Business / Products` | No semantic relationships; no View appearances |

The two folders and five element references recorded by the first report remain.
The existing 1,299 Canonical IDs/names, 14 EC aliases and known transport changes
remain. A1's former lack of incident relationships and visual appearances describes
O only: it now has R6 and V1. The first report's historical observations are preserved.

## 5. Additional element changes in the updated export

**None.** O → I retains all 1,304 element Exchange IDs and exact element records,
including Properties, Documentation and Names. P → I retains all original 1,299
IDs, native types, Names and Documentation; their transport-affected Properties
match C exactly. Native populations remain Capability 842, Product 4 and the other
previously published types. No textual qualifier was interpreted as a native retype.

## 6. Semantic relationship changes

**R1–R6 — Semantic relationship change.** Each is an explicit
`/model/relationships/relationship`, absent from both P and O. For **every row**,
the exact source is:

`id-29e0bf94882559a7b6ed9d092303a560` — **Person Identity Administration**,
native Capability, existing in P and O, with governed metadata and CT3 classification.

| Finding / I semantic line | Relationship Exchange identifier | Native type | Exact target Exchange identifier — native Name | Endpoint new relative to P? | Organisation reference |
| --- | --- | --- | --- | --- | --- |
| R1 / 95218 | `id-73ecd7a39407404ba8e2bb663efbdc19` | Composition | `id-d9272acdc675539cb441d2db9c5ad6e6` — Cross-Authority Identifier Correlation | Neither | Directly under `Relations`, I:132430 |
| R2 / 95219 | `id-36b2aa8ad58149fdb20d44c1c16dd64c` | Composition | `id-6f0bfe5e814c5734a7c530f2b7515666` — Governed Identity Correction | Neither | Directly under `Relations`, I:132431 |
| R3 / 95220 | `id-3c3d723683c147a4a902b4d35a2af018` | Composition | `id-bdf18b5e580b5b85a34357a3b21f36db` — Identity Source & Correction Lineage | Neither | Directly under `Relations`, I:132432 |
| R4 / 95221 | `id-daf437bb32114daa8a6b9462cb9b4fb2` | Composition | `id-f1e1928516765880955f638a7fec8c35` — Identifier Resolution | Neither | Directly under `Relations`, I:132433 |
| R5 / 95222 | `id-f9b270950f0241108b68f1c3daa50019` | Composition | `id-28d7815137a05a2aab6d1684011dbbde` — Identity Alias Association | Neither | Directly under `Relations`, I:132434 |
| R6 / 95223 | `id-bff76286f8994d20aa1b8aef166acebd` | Composition | `id-727a264026584f80bc3304ff8383dc92` — [Feature] Identifier Capture | Target A1 only; already present in O | Directly under `Relations`, I:132435 |

All targets are native **Capability**. R1–R5 targets also carry the established
`harmoniaType=Feature` Property; A1 does not. All six relationships have **no Name,
Documentation, Properties or other attributes beyond identifier, source, target
and `xsi:type`**. Each has one organisation reference and one visual connection
in V1. Missing endpoint metadata does not invalidate the observation of R6.

All 852 existing relationships are structurally exact against O and C, including
their Properties and endpoints; no pre-existing relationship between these exact
source/target pairs was found in P. These six are additional semantic records,
not inferred from geometry and not merely six diagram lines.

## 7. Element organisation changes

**Navigation/organisation change — retained from O:** relative to P, E gains the
`[Feature] Identifier Capture` folder/reference (I:124846/124848), and
`Business / Products` gains A2–A5 references (folder I:129488; references
I:129490–129493). These are exactly the earlier two folders and five references.

**No additional element organisation change against O.** All element homes,
folder paths and relative retained-item order remain. E's existing context-element
reference remains at E (I:124850). Folder containment does not establish R6;
the independently observed semantic record at I:95223 establishes its existence.
No Product container element or Product relationship was inferred.

## 8. Relationship organisation changes

**Navigation/organisation change — additional since O:** six reference items are
appended as direct children of the Exchange organisation item labelled `Relations`
(I:129773); exact IDs/reference lines appear in §6. There are no new relationship
folders, duplicate homes, lost references or moves of existing relationship homes.

The [source-home convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md#relationship-homes-follow-the-source)
requires relationship navigation to follow its source's semantic resource home.
These root-level references do not mirror source home E. The current tree has a
`Relations / Strategy / Business Layer / Business Enabling Capabilities / [CT1] Entity Information Enablement / [CT2] Client Administration`
branch (I:131760), but no further source-specific Person Identity Administration
folder on that branch. A separate existing `Relations / Business / ... / [CT3] Person Identity Administration`
branch concerns Business behaviour and does not change the source's Strategy home.

This is an observable **organisation discrepancy** requiring separate disposition.
It changes none of R1–R6's types, sources or targets and supplies no architectural
ownership. No folder or reference was repaired or generated.

## 9. View/presentation changes

**V1 — View/presentation change, additional since O.** Exactly one existing View
changes: `id-e2a326c7d36751af9076a32dabe0e9cd`,
**0-Person Identity Administration — Feature Landscape**, I:256574.
Its unchanged organisation path is:

```text
Views / 2-BusinessLayer / 2-Business Enabling Capabilities /
[CT1] Entity Information Enablement / [CT2] Client Administration /
[CT3] Person Identity Administration
```

V1 keeps its ID, type, Name and Documentation. It changes from six nodes/no
connections to seven nodes/six connections. All nodes are direct View children;
no XML node nesting/containment was added. The table gives `(x, y, w, h)`:

| Element appearance | Visual node Exchange identifier | O geometry | I geometry |
| --- | --- | --- | --- |
| Person Identity Administration | `id-a17cde2ab18f527f9103f8c50e32958f` | (40, 40, 360, 148) | (24, 216, 360, 493) |
| Cross-Authority Identifier Correlation | `id-ce6796499a7e5e128aef2c2b31b44df9` | (40, 230, 360, 148) | (421, 552, 240, 73) |
| Governed Identity Correction | `id-af6828d7edce5c05b5819b597293a981` | (450, 230, 360, 148) | (421, 384, 240, 73) |
| Identifier Resolution | `id-74781db5bf4e5fe38af2da1bf742f003` | (860, 230, 360, 148) | (421, 300, 240, 76) |
| Identity Alias Association | `id-8459b669dc085cabbcfca4ede19c444e` | (1270, 230, 360, 148) | (421, 633, 240, 76) |
| Identity Source & Correction Lineage | `id-0369c1bef15255da8cf693f89b7295e7` | (40, 420, 360, 148) | (421, 468, 240, 73) |
| [Feature] Identifier Capture | `id-073fae7e65554293bed1a47ff6e535ee` | Absent | (421, 216, 240, 73), I:256631 |

The five existing target nodes change font size **12 → 9** and fill to RGB
**(245,222,170)**, alpha 100. Their previous fills were (255,255,181), except
Identity Source & Correction Lineage, previously (255,222,150). The new A1 node
uses the same new target style. The source node retains its style, including
font size 12. Line colour, font name and text colour on retained nodes are unchanged.

Each new connection uses native visual `xsi:type=Relationship` and source node
`id-a17cde2ab18f527f9103f8c50e32958f`. Targets are the matching node IDs above:

| Semantic relationship | New visual connection Exchange identifier | Target appearance | I line |
| --- | --- | --- | ---: |
| R1 | `id-27509aa6cf6b40f6b3bdfd76f9ac949c` | Cross-Authority Identifier Correlation | 256680 |
| R2 | `id-66c57297bd4f4d898a53fe3d4bcc6b13` | Governed Identity Correction | 256640 |
| R3 | `id-3242d1af0d884c9ab36adbe7f0feb936` | Identity Source & Correction Lineage | 256648 |
| R4 | `id-fa56f00e1759495d82b8c40c00792ab9` | Identifier Resolution | 256656 |
| R5 | `id-df01f77a7fa245d89bc3a6992ee3d829` | Identity Alias Association | 256664 |
| R6 | `id-7239b62978b141519ddde72feb8b40fa` | [Feature] Identifier Capture | 256672 |

Each `relationshipRef` is the full semantic Exchange ID listed in §6; visual
endpoints correctly represent semantic endpoints. All six have black lines,
Sans 9 black text and no explicit bendpoints. Existing node order is retained;
the new node is appended, followed by connections serialized R2, R3, R4, R5, R6, R1.
All other **1,195 Views**, all original visual IDs and all existing connections/
bendpoints match O exactly. A2–A5 still appear in no View.

## 10. Metadata/identity issues and transport distinctions

**Metadata/identity reconciliation issue — retained:** A1–A5 still lack the
metadata listed in §4. Their semantic additions remain candidates against P;
their identity as genuinely new architectural instances is unresolved. R6 does
not provide A1 with a Canonical ID, governed names or Feature specialisation.

All 1,299 prior Canonical IDs, three names and `archimateType` Properties retain
exact values, multiplicity and owner association. No malformed/duplicate/multiple
ID claims or continuity/source-binding/naming conflicts were found. Existing
`harmoniaType=Feature` coverage is 178; all 14 EC bindings retain their owners.
Relationships/Views/visual objects do not require Canonical IDs under this contract;
their absence on R1–R6 is not a missing-element-ID finding. The bare relationships
do not carry acceptance/source-assertion/standing evidence of their own.

**Archi/Open Exchange round-trip transformation — retained, not additional edits:**

| Transformation against P | Evidence in current comparison |
| --- | --- |
| 61 Property-definition IDs rekeyed; references redirected | Same unique keys and `string` datatypes; definitions exact against O/C; e.g. `governed-canonical-id` → `propid-18`, `governed-harmonia-type` → `propid-34` |
| 61 definition-name `xml:lang="en"` attributes lost | Same loss as O/C; definition-name text unchanged |
| Original organisation IDs lost, known ordering changes | All 7,096 original item IDs absent; existing folder/reference memberships retained; added items also have no organisation IDs |
| 1,669 embedded evidence-Property whitespace changes | 615 Snapshot Record JSON, 409 Source Locators, 291 Deferred Relationship Assertions, 269 Source Assertion JSON, 85 Derivation Provenance; retained elements/relationships exact against C |
| Style/default and View serialization behaviour | Previously characterised 3,910 absent-alpha → 100 materialisations, 34 final-LF losses in View Documentation, and connection reorderings in the same 15 Views remain; other 1,195 Views exact against C |

The new V1 geometry, colour/font changes, node and connections are **additional
presentation edits**, not explained away by that transport evidence. No JSON,
whitespace or geometry equivalence was broadened. Historical transport losses and
unproved paint-order equivalence remain visible rather than being reported lossless.

## 11. Candidate Inferences — explicitly non-authoritative

- **CI1:** A1's `[Feature]` Name, Capability type, R6 and V1 context are compatible
  with a possible Harmonia Feature interpretation. They do **not** establish the
  required `harmoniaType=Feature`, canonical identity, bounded definition, governed
  names or approved framing. No such fields were generated.
- **CI2:** R1–R6 may propose native Composition as the representation of the
  source CT3's Feature framing/decomposition. This is not an accepted profile rule.
  [Mapping §5](../../docs/markdown/governance/rad-archimate-model-mapping.md#5-relationship-profile-and-direction)
  explicitly leaves the native edge for BEC CT3 framing subject to adjudication
  and forbids automatically equating framing with Composition. The XML still
  unambiguously contains six Composition relationships.

No Product-to-service/component equivalence, deployment topology from
“Central”/“Satellite”, domain authority, or intended test objective is inferred.

## 12. Findings requiring Solution Architect adjudication

**Unresolved / insufficient evidence:**

1. **Architectural acceptance/meaning of R1–R6:** their types/endpoints are known;
   source-governed whole/part meaning and the native framing profile are not
   established by incoming authorship. The mapping-profile question must remain
   explicit; neither dropping the relationships nor silently adopting a new rule
   is justified.
2. **A1–A5 identity, definitions and standing:** whether each represents a new
   architectural instance or another representation of an existing resource remains
   unestablished. A1's specialisation and A2–A5's governed Product meanings remain open.
3. **Relationship organisation disposition:** all six root-level references depart
   from source-home navigation. This is independent of semantic relationship acceptance.
4. **V1 standing/readability:** its unchanged Documentation says “Context and
   immediate governed children only. Standing remains in element Properties.”
   A1 has no such Properties; the existing Identity Source & Correction Lineage
   retains `v0.2 / Architectural Status=Proposed` despite the changed common fill.
   Neither placement, the unchanged View text nor the new colour approves either
   element. Semantic standing remains distinct from visual styling; no GUI rendering
   or human visual acceptance is claimed.
5. **Concurrent native-file drift:** the native `.archimate` file and its `.bak`
   changed bytes during analysis. Neither is an XML comparison input. No analysis
   command writes either file; the actor, cause and any architectural meaning of
   those changes are unestablished. No reconstruction or interpretation of later
   native working state was attempted. I's hash remains the pinned value in §1.

Exact native edit sequence, unexported intent and missing canonical crosswalks
cannot be established from these snapshots. No ambiguity was silently resolved.

## 13. What the current machinery recognises

Existing tools positively enumerate the five additional element IDs, six
additional relationship IDs, exact endpoint references and visual additions, while
detecting the five missing metadata sets. Offline schema/reference checks accept
R6 despite its target's metadata gaps. Read-only inspection independently exposes
the root-level relation homes and the separate V1 changes.

The unchanged Increment 1 comparator reports:

| Comparison | Result | Raw events / classifications |
| --- | --- | --- |
| P → I | `DIFFERENCES`, exit 2 | 10,761: 1,669 semantic differences; 4,269 information losses; 4,760 unexplained differences; 63 equivalent normalisations |
| O → I | `DIFFERENCES`, exit 2 | 64 unexplained differences; zero events in the other categories |
| Identity/source audit | Execution `PASS`, exit 0 | Identity/naming readiness both `NONCONFORMANT`: the same five incomplete additions |

O → I's 64 events comprise six semantic relationship subtrees and relationship
collection ordering (7), six organisation references and organisation ordering
(7), and V1's 42 attribute changes, seven added visual subtrees and child ordering
(50). The comparator calls added semantic relationships **unexplained subtrees**;
its zero `SEMANTIC DIFFERENCE` events for O → I does not mean no semantic change.
Its changed-*retained*-record lists correctly show zero element/relationship edits
and one View edit, separately from the six additional relationship IDs.

P → I's raw lists still flag 625 elements, 269 relationships and 1,195 Views because
transport findings remain. These are not architectural edit counts. Organisation
items are paired positionally, not reconciled by recovered identity. Source-home
compliance and Harmonia interpretations require independent examination.
**The current framework provides useful detection and evidence, but does not yet
implement reliable automatic Increment 2 classification, acceptance or reconciliation.**
No new reconciliation rules are established by this report.

## 14. Validation and stop point

| Check | Result / limit |
| --- | --- |
| Existing offline schemas, identifier uniqueness, Property references, semantic/visual endpoints and organisation references | **PASS**, no input issues on P, O or I; not proof of architectural approval |
| Existing required architecture suite, JDK 21 | **PASS**, 90 tests, 0 failures/errors/skips; Maven 26.779 seconds, 180-second timeout |
| Release validator | **PASS**, 1.0.8 / previous 1.0.7; 30-second timeout |
| Git whitespace check | Exit 2: 85 changed CRLF lines in the supplied incoming XML flagged as trailing whitespace. Preserved unchanged; report whitespace check passes |
| Required input/authority preservation | **PASS**, incoming XML, governed baseline/publications, RADS, framework, first report and release authority unchanged; no new files in RADS/ArchiMate trees |
| Wider 440-file observation | 438 unchanged; native `HarmoniaArchictecture-1.0.8.archimate` and `.archimate.bak` changed during analysis. Cause/actor **UNRESOLVED**; neither file is a comparison input, neither was written by an analysis command |
| Reconciliation/allocation/publication | None executed; no reconciliation metadata, Canonical IDs, governed names or semantic relationships added by this task |

Architecture command:

```bash
timeout --signal=TERM --kill-after=5s 180s env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

The [validation record](2026-10-18-archimate-increment-2-updated-blind-analysis-1.0.8/validation.json)
pins the protected-file hashes, raw comparison summaries and compressed
comparison/audit/test/release evidence and observed native-file hash drift.
No command timed out or stalled. A broad all-file-equality assertion detected
the two native-file changes; the narrower required XML/authority checks pass.
Only this new report and its observation/validation evidence were created;
no task write targeted pre-existing inputs or staging. The first report was not
overwritten. **STOP: no reconciliation, ingestion implementation, governance
update, release increment or publication follows this analysis.**
