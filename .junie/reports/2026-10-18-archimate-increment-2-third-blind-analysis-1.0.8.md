<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Increment 2 — third blind analysis of the Archi 1.0.8 export

**2026-10-18 — observation only; no reconciliation or architectural acceptance.**

Relative to the **recorded second export**, the current export adds **four element
records, seven semantic relationships and two Views**, and changes **two existing
Views**. No same-native-type/same-endpoint relationship duplication was found.
The five earlier element records and six Composition records remain unchanged;
their acceptance remains unresolved. **The exact second XML is not preserved in
the Git objects or files checked.** Immediate changes below compare preserved
second-pass observations; an exact second-XML comparison remains unavailable.

## 1. Artefacts and exact before/current evidence

Repository-relative paths are used throughout. `I:line` means a physical
LF-delimited line in the current unchanged incoming XML, verified against raw bytes.

| Evidence | Artefact / Git preservation | Bytes | SHA-256 |
| --- | --- | ---: | --- |
| P — governed 1.0.8 | `docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml`; HEAD blob `59cecaa80088a785c7c3a66da1731555624fbcda` | 20,800,334 | `504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a` |
| S — second export, recorded observations only | Same ingest path when second report ran; **no exact XML/commit found** | 21,841,727 recorded | `b869ac466340e204077c6a733d648153594563be134c386fc7a17af609428f41` recorded |
| I — current third export | `docs/archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml`; working-tree input | 21,854,756 | `6b973db242a4b26bc6a93e27b482100b6f96655cb40deb4ceb35a93c134c7d3e` |
| F — first export, supplemental exact before-state | Ingest path at HEAD, blob `4c616a90f880698dd8c8155f2f0e3c88e8b88a70` | 21,837,077 | `4d178c7625744f34cfc34213468bc2c6e9ee833c570bfbbfcf65916ab9fe1af2` |
| C — accepted real no-edit transport control | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-exports/release-1.0.7.xml` | 21,835,649 | `38e49fa348d015aba6b19e21b4f96dc00fddf3c4d7671b3e352b4c07748917ea` |

HEAD is `1294fbfbec2ec8d42465d20867082883721382cc`. Both the Git index and
HEAD contain **F, not S**. Path history across all refs identifies that first
export only; enumerating all Git objects, including unreachable objects, finds
no blob with S's recorded byte length. No file of that length was found under
`docs/`, `.junie/` or `/tmp/`. F's temporary exact copy was hash-verified against
the Git blob. Native `.archimate`/`.bak` files were not used to reconstruct S.

The evidence chain is **P → S (recorded) → I (exact)**. S is evidenced by the
[second report](2026-10-18-archimate-increment-2-updated-blind-analysis-1.0.8.md)
(SHA-256 `1bb429a9e6d6b53ef6c154ba568903ee8468d39da8e7a9ff44237b1fc48557fe`)
and its preserved `baseline-comparison.json.gz` and `previous-comparison.json.gz`.
Their compressed hashes are respectively
`9a162c5f942c125ab5cece65c053a9279d49f6659da2a1933888f3866364a545` and
`1a0ecac7c624c7e6adbd763da852fe72fe4abf0c64f6a287a724865c953e5388`;
both decompress exactly to the historical comparator outputs inspected here.
The [first report](2026-10-18-archimate-increment-2-blind-analysis-1.0.8.md)
remains historical observation evidence, never architectural authority.

Read current root/docs `AGENTS.md`, the
[axioms](../../docs/markdown/governance/architectural-axioms.md),
[identity/naming contract](../../docs/markdown/governance/canonical-identity-and-naming.md),
[navigation/View convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md),
[mapping profile](../../docs/markdown/governance/rad-archimate-model-mapping.md),
[release policy](../../docs/markdown/governance/architecture-release-policy.md),
ingest/framework guidance, [1.0.8 publication evidence](../../docs/archimate/processing/publishing/1.0.8/publication-1.0.8.md),
its publication comparison/inspection guide, and the preserved real no-edit proof.
Independently confirmed P differs from published 1.0.7 in exactly one model-level
release value by restoring it **in memory** and obtaining byte equality. C is
transport evidence; P remains the architectural baseline.

AX-14 keeps semantics, organisation, presentation and identity distinct. AX-15/17
preserve uncertain evidence and unaccepted architecture. AX-18 prohibits inferring
domain authority from participation, coordination, names or presentation.
No conversational explanation, intended test outcome or new convention was used.

## 2. Comparison method

Ran unchanged read-only `diagnose_roundtrip.py` with `python3 -B`, bounded to
60 seconds: `compare` on **P → I**, **F → I**, **C → I**; and
`diagnose --model I --previous P --baseline-context`. Each existing Exchange
reader runs the offline Diagram/Model/View schema, identifier, Property-definition,
reference and visual/semantic endpoint checks. No comparison rule was broadened.
An exact **S → I** comparator run is unavailable because S's XML is missing.

For the primary historical comparison, compared S's recorded ID inventories,
counts, exact added-subtree signatures and field-level before/after observations
with current observations. No prior XML was rebuilt from those records. The
historical baseline-comparison event sets have 63 current-only and 18 historical-only
events; these are differing observations against P, **not** a count of S → I edits.

Independent read-only XML queries indexed exact Exchange IDs; checked native Names,
types, Documentation and Properties; compared all retained semantic records with C;
compared folder-path/reference **multisets**; inspected View-local IDs, styles,
geometry, ordered bendpoints and XML parentage; and grouped semantic relationships
by the exact `(native type, source ID, target ID)` triple. No lexical element
matching or folder-driven edge inference occurred. Indentation is XML syntax;
content text, references and geometry were not repaired or declared equivalent.
Only temporary observation/report queries were written, outside repository tooling.

The [evidence bundle](2026-10-18-archimate-increment-2-third-blind-analysis-1.0.8/validation.json)
retains fresh raw comparisons, identity audit, structured observations, historical
observation deltas, test/release logs and preservation hashes. Original earlier
reports/evidence and pre-existing untracked ingestion work remain untouched.

## 3. Immediate changes since the recorded second export

| Collection | P | S, recorded | I | Immediate observed change |
| --- | ---: | ---: | ---: | --- |
| Semantic elements | 1,299 | 1,304 | 1,308 | +4; no observed removals or retained-record edits |
| Semantic relationships | 852 | 858 | 865 | +7; no observed removals or retained-record edits |
| Composition / Realization | 670 / 157 | 676 / 157 | 676 / 161 | +4 Realization |
| Flow / Serving | 0 / 0 | 0 / 0 | 2 / 1 | +2 Flow, +1 Serving |
| Association / Influence | 15 / 10 | 15 / 10 | 15 / 10 | Unchanged |
| Views | 1,196 | 1,196 | 1,198 | +2; 2 existing Views edited; no removed Views |
| Visual nodes / connections | 3,915 / 1,449 | 3,916 / 1,455 | 3,923 / 1,469 | +7 / +14; no observed visual removals |
| Explicit bendpoints | 2,922 | 2,922 | 2,928 | +6, all in one new View |
| Organisation folders / references | 3,749 / 3,347 | 3,751 / 3,358 | 3,752 / 3,371 | +1 View folder / +13 references |
| All organisation items / empty folders | 7,096 / 636 | 7,109 / 636 | 7,123 / 635 | +14 items; one existing empty folder populated |
| Property definitions | 61 | 61 | 61 | No additional definition change |
| Canonical-ID owners / elements missing metadata | 1,299 / 0 | 1,299 / 5 | 1,299 / 9 | Four additional incomplete records |

These are record counts, not accepted architectural changes. A1 and the existing
Lineage element acquire additional semantic/visual context without changing their
element records. Native semantic Names/types, endpoint IDs and metadata on retained
objects show no new edits. Byte-level completeness against S cannot be certified.

## 4. Cumulative divergence from governed 1.0.8

**Architectural change candidates:** nine additional element records: one
Capability, four Products, three BusinessServices and one BusinessProcess.
**Semantic relationship changes:** thirteen additional records: six Composition,
four Realization, two Flow and one Serving. No governed element/relationship/View
identifier is missing; all retained semantic records exactly match C, including
the transport-affected evidence Properties. No retained native Name/type or
semantic Documentation edit is observed against P.

**Navigation/organisation changes:** two element folders, nine element references,
thirteen relationship references, one View folder and two View references added.
No existing folder-path/reference membership is lost or moved. **View/presentation
changes:** two existing Views edited and two Views added; cumulatively eight nodes,
twenty visual connections and six explicit bendpoints added. The other 1,194
retained Views match C exactly. Known P → C transport effects remain separately
visible (§11). The model ID `id-80b065eaa4c550eba09c0dbd41b1d631`, model Name,
Documentation, resolved model Properties and release value **1.0.8** are retained.

## 5. Element records and previous findings

All A1–A9 have **no Properties and no Documentation**: only a native Name child.
Each lacks `canonicalId`, `displayName`, `friendlyName`, `canonicalName`,
`archimateType`, definition/source/standing metadata. A1 also lacks
`harmoniaType=Feature`. These are metadata findings, not authority to allocate fields.
Names below are exact native Names, independent of governed naming Properties.

Define **E** as:

```text
Strategy / Business Layer / Business Enabling Capabilities /
[CT1] Entity Information Enablement / [CT2] Client Administration /
[CT3] Person Identity Administration
```

| Finding / source | Exchange identifier | Exact native Name | Native type | Element organisation | Incident relationships; Views |
| --- | --- | --- | --- | --- | --- |
| A1, I:34747 | `id-727a264026584f80bc3304ff8383dc92` | [Feature] Identifier Capture | Capability | E / `[Feature] Identifier Capture` | R6, R7, R8, R9; V3, V1 |
| A2, I:87210 | `id-234b080d0ea54d57bb0001a2cb861080` | [Product] Provider Registry - Satellite | Product | `Business / Products` | No relationships; no Views |
| A3, I:87213 | `id-bfdd958364d1466a914d474dce8baab9` | [Product] Provider Registry - Central | Product | `Business / Products` | No relationships; no Views |
| A4, I:87216 | `id-3c5ae26723224eb0a6e6a70d70fe2c83` | [Product] Practitioner Collaboration Service | Product | `Business / Products` | No relationships; no Views |
| A5, I:87219 | `id-d84d8e1f988c4bc998b18834aabfcd74` | [Product] Health Information Exchange Service | Product | `Business / Products` | No relationships; no Views |
| A6, I:62338 | `id-fddda503b8f0438e995465cd93204326` | [Business Service] Identifier Create | BusinessService | `Business` | R7, R10; V3 |
| A7, I:62341 | `id-0088d61ccfcf4962b3aab8bc3b96021d` | [Business Service] Identifier Update | BusinessService | `Business` | R8, R11; V3 |
| A8, I:62344 | `id-c645a859ac2c4447ad5ae850cf384bf3` | [Business Process] Identifier Verification | BusinessProcess | `Business` | R9, R10, R11, R13; V3 |
| A9, I:62347 | `id-9bfedbfd5b404694a2224c039fa5d4c4` | [Business Service] Verify Identity Source | BusinessService | `Business` | R12, R13; V4, V3 |

**Retained from the earlier passes:** A1–A5 have exactly the same records as F,
consistent with S's recorded unchanged state. A2, A3, A4 and A5 remain unaffected:
same `Business / Products` homes and no semantic relationships or View appearances.
A1 retains R6 and its V1 appearance, and is **supplemented/affected** by incoming
R7, R8 and R9 plus a second visual appearance in V3. It has not been retyped or
given governed metadata. Its V1 geometry changes separately (§10).

**Additional this pass — Architectural change candidates:** A6–A9 are semantic
element records absent from P, F and S's recorded complete element-ID inventory.
Their actual `xsi:type` values are shown above; a textual qualifier supplies no
extra subtype or identity rule. Their four organisation references are direct
children of `Business` (I:129514–129517); no new element folders accompany them.
No retained element record was removed, renamed, retyped or given new Properties.

## 6. Semantic relationship changes

**R1–R6 remain unchanged**, verified against the exact added-subtree signatures in
S's preserved comparator outputs. Each is native **Composition**, source
`id-29e0bf94882559a7b6ed9d092303a560` — **Person Identity Administration**,
a governed baseline Capability (`canonicalId=6c3de7750ef1436cad6a9533731782b6`).
All still have one `Relations` root reference and one V1 connection.

| Finding / semantic source | Relationship ID | Exact target ID — native Name | Endpoint origin / effect this pass |
| --- | --- | --- | --- |
| R1, I:95230 | `id-73ecd7a39407404ba8e2bb663efbdc19` | `id-d9272acdc675539cb441d2db9c5ad6e6` — Cross-Authority Identifier Correlation | Governed baseline; no additional incident edge |
| R2, I:95231 | `id-36b2aa8ad58149fdb20d44c1c16dd64c` | `id-6f0bfe5e814c5734a7c530f2b7515666` — Governed Identity Correction | Governed baseline; no additional incident edge |
| R3, I:95232 | `id-3c3d723683c147a4a902b4d35a2af018` | `id-bdf18b5e580b5b85a34357a3b21f36db` — Identity Source & Correction Lineage | Governed baseline; additional R12/V4 context |
| R4, I:95233 | `id-daf437bb32114daa8a6b9462cb9b4fb2` | `id-f1e1928516765880955f638a7fec8c35` — Identifier Resolution | Governed baseline; no additional incident edge |
| R5, I:95234 | `id-f9b270950f0241108b68f1c3daa50019` | `id-28d7815137a05a2aab6d1684011dbbde` — Identity Alias Association | Governed baseline; no additional incident edge |
| R6, I:95235 | `id-bff76286f8994d20aa1b8aef166acebd` | `id-727a264026584f80bc3304ff8383dc92` — [Feature] Identifier Capture | A1 from first pass; gains R7–R9 and V3 |

R1–R5 targets are native Capability with `harmoniaType=Feature`; R6's target
is A1, native Capability without that Property. R1/R2/R4/R5 have no additional
semantic context this pass; R3's target gains R12/V4. None of these effects modifies
the original six records. Their shared source and V1 remain unchanged semantically.

**R7–R13 — additional Semantic relationship changes.** Every row is an explicit
`/model/relationships/relationship`, absent from P/F and S's recorded complete
relationship-ID inventory. Endpoint origins below distinguish baseline architecture
from blind-test additions. Exact A1/A6–A9 Names and IDs are fixed in §5 and repeated
in the endpoint table for independent verification.

| Finding / source | Relationship ID | Native type | Source ID — exact Name (origin) | Target ID — exact Name (origin) |
| --- | --- | --- | --- | --- |
| R7, I:95236 | `id-e84f2e9df66a48d795d604d75afc72cb` | Realization | `id-fddda503b8f0438e995465cd93204326` — [Business Service] Identifier Create (this pass) | `id-727a264026584f80bc3304ff8383dc92` — [Feature] Identifier Capture (first blind pass) |
| R8, I:95237 | `id-69eb864f56df4d928f8abc3f7ab4e834` | Realization | `id-0088d61ccfcf4962b3aab8bc3b96021d` — [Business Service] Identifier Update (this pass) | `id-727a264026584f80bc3304ff8383dc92` — [Feature] Identifier Capture (first blind pass) |
| R9, I:95238 | `id-c0f84a8e4c8d496580a68cac4823c58c` | Realization | `id-c645a859ac2c4447ad5ae850cf384bf3` — [Business Process] Identifier Verification (this pass) | `id-727a264026584f80bc3304ff8383dc92` — [Feature] Identifier Capture (first blind pass) |
| R10, I:95239 | `id-bef77e2eb7de4691b4fbe9a0c84e8232` | Flow | `id-fddda503b8f0438e995465cd93204326` — [Business Service] Identifier Create (this pass) | `id-c645a859ac2c4447ad5ae850cf384bf3` — [Business Process] Identifier Verification (this pass) |
| R11, I:95240 | `id-0849f31d6d7d4f7eb1e484d7437e5cd7` | Flow | `id-0088d61ccfcf4962b3aab8bc3b96021d` — [Business Service] Identifier Update (this pass) | `id-c645a859ac2c4447ad5ae850cf384bf3` — [Business Process] Identifier Verification (this pass) |
| R12, I:95241 | `id-37f6913198e2434db5309c1c8e36fbc0` | Realization | `id-9bfedbfd5b404694a2224c039fa5d4c4` — [Business Service] Verify Identity Source (this pass) | `id-bdf18b5e580b5b85a34357a3b21f36db` — Identity Source & Correction Lineage (governed baseline) |
| R13, I:95242 | `id-c53c42ba0a4f437f9cb76a4c1a701816` | Serving | `id-9bfedbfd5b404694a2224c039fa5d4c4` — [Business Service] Verify Identity Source (this pass) | `id-c645a859ac2c4447ad5ae850cf384bf3` — [Business Process] Identifier Verification (this pass) |

**For every R1–R13:** Name, Properties and Documentation are absent. Each has
exactly one organisation reference directly under **`Relations`**; R1–R6 occur
at I:132453–132458 and R7–R13 at I:132459–132465 in table order. R7–R11 and R13
each appear once in V3; R12 appears once in V4. Their exact visual connection IDs
are in §10. No endpoint metadata gap was used to dismiss an explicit relationship.

All 852 governed relationship records and the six earlier Composition records
are retained with their exact native types and endpoints. No relationship removal,
endpoint mutation, retyping or Property/Documentation edit was observed.
New relationships supply observed assertions, not acceptance/source-standing evidence.

## 7. Possible semantic relationship duplications

**None observed by the requested exact test.** All **865** current semantic
relationships have distinct `(native type, source Exchange ID, target Exchange ID)`
triples. R7–R13 match neither a governed-baseline triple nor any of S's six earlier
addition triples. Every R7–R13 source is itself an A6–A9 addition, absent from both
earlier complete element inventories. No relationship IDs were merged or deleted.

V2's seven new visual connections reference **existing governed Composition
records** (§10); they are additional visual appearances, not seven duplicate
semantic records. Unknown canonical identity on A1–A9 prevents conclusions about
unestablished alternate representations; similar Names were not treated as identity.

## 8. Element organisation changes

**Navigation/organisation change, additional:** four reference items A6–A9 appear
directly under `Business`, in A6/A7/A8/A9 order at I:129514–129517. No element
folder addition/removal/move is observed since S. Previously observed A1's folder
at E / `[Feature] Identifier Capture` and `Business / Products` remain; A1–A5
retain their homes. All baseline element reference-path memberships remain.

These locations establish neither Feature ownership nor Product/service/component
equivalence. V3/V4 appearance does not automatically relocate A6–A9 beneath a
Feature in the model tree. Source-governed context remains unestablished.

## 9. Relationship organisation changes

**Navigation/organisation change, additional:** R7–R13 add seven direct root
`Relations` references, after retained R1–R6, at the lines in §6. No relationship
folder was added, removed or moved; no retained reference-path membership is lost,
moved or duplicated. All thirteen added relationships have exactly one home.

**Organisation discrepancy, independently retained:** R1–R6 still do not mirror
their source's Strategy home E under Relations, as required by the existing
[source-home convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md#relationship-homes-follow-the-source).
**Additional discrepancy:** R7–R13 lie at `Relations`, whereas their actual source
elements are organised at `Business`; their homes do not mirror that observed
source path either. Neither discrepancy changes a semantic source, target or
type. All semantic and visual endpoints resolve and agree. No relationship home
or ownership was inferred from targets, View proximity or folder labels.

## 10. View / presentation changes

The following are **View/presentation changes**, separate from §§5–9.
Define **W** as:

```text
Views / 2-BusinessLayer / 2-Business Enabling Capabilities /
[CT1] Entity Information Enablement / [CT2] Client Administration /
[CT3] Person Identity Administration
```

| View / current source | Exchange ID | Exact native Name | Status against S / organisation |
| --- | --- | --- | --- |
| V1, I:256740 | `id-e2a326c7d36751af9076a32dabe0e9cd` | 0-Person Identity Administration — Feature Landscape | Retained, geometry/style edit; W |
| V2, I:259158 | `id-c8ec850977565a17869af357ee28d917` | 0-Entity Information Enablement — CT2 Landscape | Retained, seven connections added; W ancestry through [CT1] Entity Information Enablement only |
| V3, I:256638 | `id-216c4ab0757b45b3894e7e0c2594b9f9` | Identifier Capture - Feature realisation | New; W / `[Feature] Identifier Capture` |
| V4, I:256609 | `id-8fb2f8b1776f4ebf969e9b5b638e4ed3` | Identity Source & Correction Lineage - Feature realisation | New; W / `[Feature] Identity Source & Correction Lineage` |

**V1:** retains its ID, Name, Diagram type, Documentation, seven node IDs and
six connection IDs. No node/connection/element membership addition or removal
against S. All seven nodes remain **direct XML children of the View**; there is
no new XML node nesting. Geometry `(x, y, w, h)` changes as follows, using S's
recorded geometry, without reconstructing its XML:

| Element appearance | Visual node ID | S geometry, recorded | I geometry |
| --- | --- | --- | --- |
| Person Identity Administration | `id-a17cde2ab18f527f9103f8c50e32958f` | (24, 216, 360, 493) | (12, 12, 192, 493) |
| Cross-Authority Identifier Correlation | `id-ce6796499a7e5e128aef2c2b31b44df9` | (421, 552, 240, 73) | (241, 348, 240, 73) |
| Governed Identity Correction | `id-af6828d7edce5c05b5819b597293a981` | (421, 384, 240, 73) | (241, 180, 240, 73) |
| Identifier Resolution | `id-74781db5bf4e5fe38af2da1bf742f003` | (421, 300, 240, 76) | (241, 96, 240, 76) |
| Identity Alias Association | `id-8459b669dc085cabbcfca4ede19c444e` | (421, 633, 240, 76) | (241, 429, 240, 76) |
| Identity Source & Correction Lineage | `id-0369c1bef15255da8cf693f89b7295e7` | (421, 468, 240, 73) | (240, 264, 240, 73) |
| [Feature] Identifier Capture | `id-073fae7e65554293bed1a47ff6e535ee` | (421, 216, 240, 73) | (241, 12, 240, 73) |

V1's source node changes fill RGB **(255,255,181) → (245,222,170)**, retaining
alpha 100 and Sans 12. Its width becomes 192; height remains 493. The other six
nodes retain RGB (245,222,170), alpha 100, Sans 9, existing lines/text styles and
dimensions; they change coordinates only. All six earlier R1–R6 visual connections
retain exact attributes, style, source/target IDs, `relationshipRef`, order and no
explicit bendpoints. They reference **second-pass relationships**, not new ones.

**V2:** retains all eight nodes, exact node geometry/styles/parentage, Name,
Documentation and organisation; **seven connections added**, none removed.
These reference seven Composition records already present in **P** and retained
in S. All use source visual node `id-84aa3ddd9d375b48981e4d7e4b8d2d7e`, representing
`id-4726f9b8f6015e019238b5a8cc596c7c` — **Entity Information Enablement**.

| Added visual connection ID | Existing governed relationshipRef | Target visual node ID | Target element ID — Name |
| --- | --- | --- | --- |
| `id-62f38f2147d54605be108818ddb65a25` | `id-f7deddb1eb2751aa96ba6a490b18003e` | `id-1ae6d706a2d85b9eac70a31e969c76d8` | `id-a6aaf19f1f225b67b1e3a19740d5f457` — Clinical Device Administration |
| `id-3709d706394f4695b9886cb76038762a` | `id-be1babf8397d51b3878b271f8775d711` | `id-4b3c4cbc947a555b914c6a0bc31f4b93` | `id-c480beb83dfe55e599ff5dc56b4249bb` — Health Materiel Administration |
| `id-39efc4286edf40609e42ded6b79737c6` | `id-c099301d131c594580c840a6363997a3` | `id-5bfc1365cdde59799f741698323c415b` | `id-19d0a7d9080e5758915878db238ec2d8` — Health Service Administration |
| `id-0d6cbbef60984cad9624700a2b53c2da` | `id-c97120325e3e5a1b99a11eeec1e5c89e` | `id-a2eee58a65e25eb0b7dfaa0186c6d11c` | `id-97a226da4bae53e9bb0cb071c6dbf5c2` — Organisation Administration |
| `id-6ec0b7e2d8964fe287b08e19e3af6cd7` | `id-3fc7839fceda5f4193e58dd14415a0ab` | `id-92c2544bb75954fd8dfac150373bf8b5` | `id-c67b67dba5cc512d94eac01eb1524775` — Client Administration |
| `id-587eff9341f64a99b0581ecc74030c17` | `id-b297858a4ab352cb942b88df159109ed` | `id-82338ac3935152f0866281add52432b0` | `id-243b002011da54d5ac721e2a99d456ea` — Location Administration |
| `id-b45f6090065040c5b2cf8a39ac28a9dc` | `id-05fb0b3f26d8545a8caf8fcb8a92b2e3` | `id-302c6b450f875419b7ea4ea3dee8d0d0` | `id-7b438cff4f2d5b6d928cd7ea538f67a4` — Provider Administration |

V2's connections, I:259233–259281, are native visual `Relationship`, with black
lines, Sans 9 black text and zero explicit bendpoints. The existing node order is
retained and the seven connections are appended in table order. This is a change
to a projection of existing semantics; no new semantic Composition was manufactured.

**V3/V4, added Views:** native Diagram; neither has Properties or Documentation.
Every node is native visual `Element`, directly parented by its View. Neither
View adds XML node nesting. All new nodes use Sans 9 black text, alpha 100 and
line RGB (92,92,92); A1 and the governed Lineage appearance use fill (245,222,170),
the four new Business elements use (255,255,181).

| View | New visual node ID | Represented element | Geometry |
| --- | --- | --- | --- |
| V3 | `id-4f78c588c80d42e0b4f52e765dc76ff2` | A6 | (168, 36, 253, 55) |
| V3 | `id-3fd6725d68f4476884cf44f9fbf09794` | A1 | (12, 36, 120, 397) |
| V3 | `id-d0a1079102c14d26901ace75bd379281` | A7 | (168, 108, 253, 55) |
| V3 | `id-a222a0f6a6804944b188e32ee5caa9a5` | A8 | (168, 180, 253, 55) |
| V3 | `id-8e77730d5e7147bea761ee18da71358c` | A9 | (636, 180, 253, 55) |
| V4 | `id-030fc162b19b41c8930dac9f9c901bf0` | `id-bdf18b5e580b5b85a34357a3b21f36db` — Identity Source & Correction Lineage | (12, 24, 240, 409) |
| V4 | `id-5a05920f7422487c930faff16187327b` | A9 | (312, 24, 253, 55) |

Every new connection below references a **semantic relationship introduced in
this pass**, whose exact ID/type/endpoints are in §6. Connections use native visual
`Relationship`, black lines and Sans 9 black text. Table order is XML order within
each View; node order is the table order above. Source/target visual IDs agree with
the semantic endpoints. Bendpoint coordinates are ordered, absolute `(x, y)` values.

| View | New visual connection ID | relationshipRef label | Source visual ID → target visual ID | Explicit bendpoints |
| --- | --- | --- | --- | --- |
| V3 | `id-007507b4daa748ee8df77d2dbf8a496d` | R7 | `id-4f78c588c80d42e0b4f52e765dc76ff2` → `id-3fd6725d68f4476884cf44f9fbf09794` | None |
| V3 | `id-95c171f829a849a3ac544a931b4d6a5f` | R10 | `id-4f78c588c80d42e0b4f52e765dc76ff2` → `id-a222a0f6a6804944b188e32ee5caa9a5` | (516, 48), (516, 204), (468, 204) |
| V3 | `id-f64ce7c8aac347b2856dbceba42237d6` | R8 | `id-d0a1079102c14d26901ace75bd379281` → `id-3fd6725d68f4476884cf44f9fbf09794` | None |
| V3 | `id-851cf4ffa1eb436983a9e47a6f5157b5` | R11 | `id-d0a1079102c14d26901ace75bd379281` → `id-a222a0f6a6804944b188e32ee5caa9a5` | (480, 132), (480, 192) |
| V3 | `id-b1f2f750233f4717b5bbedcf413e3c61` | R9 | `id-a222a0f6a6804944b188e32ee5caa9a5` → `id-3fd6725d68f4476884cf44f9fbf09794` | None |
| V3 | `id-e11b8af91143439bbb54ff0be650fb79` | R13 | `id-8e77730d5e7147bea761ee18da71358c` → `id-a222a0f6a6804944b188e32ee5caa9a5` | (528, 216) |
| V4 | `id-d4301bf1848e4e7e9a1d5267dfa3594e` | R12 | `id-5a05920f7422487c930faff16187327b` → `id-030fc162b19b41c8930dac9f9c901bf0` | None |

**View organisation:** W gains one `[Feature] Identifier Capture` folder with
V3's reference (I:136932–136934). The existing `[Feature] Identity Source & Correction
Lineage` folder gains V4's reference (I:136928–136930), becoming nonempty. V1 remains
directly at W, and V2 at its CT1 context; no prior View home was moved or removed.
No other retained View content edit was observed beyond the accepted transport
effects. No GUI rendering, readability acceptance or View-generation rule is claimed.

## 11. Identity/metadata and round-trip findings

**Metadata/identity reconciliation issue:** all nine additions lack the metadata
listed in §5, four more than S. Incoming Exchange IDs identify observable records;
their identity as genuinely new governed instances remains unestablished. A1's
new Realizations and two appearances supply no `harmoniaType`, Canonical ID or
governed names. No source identity was matched solely by Name.

All **1,299** original valid unique UUID32 Canonical IDs retain exact values,
owners and multiplicity. All three governed names and `archimateType` remain on
those same elements; `harmoniaType=Feature` coverage remains **178**. All **14** EC
bindings retain their owners. The audit finds no malformed/duplicate/multiple ID
claims, continuity, source-binding or naming conflicts. Execution status is PASS;
identity/naming readiness is **NONCONFORMANT**, with nine missing metadata sets.
The contract allocates no Canonical IDs to relationships/Views/visual objects;
their lack of such IDs is not an element-metadata failure.

**Archi/Open Exchange round-trip transformation, retained:**

| P → I observation | Control evidence / distinction |
| --- | --- |
| 61 Property-definition IDs rekeyed; all 61 name-language attributes lost | Same exact definition keys/string datatypes and definition records as C/F; no new change since S |
| All 7,096 original organisation item IDs absent; known ordering effects | Same transport behaviour as C; folder/reference memberships checked independently; no folder identity reconstructed |
| 1,669 evidence-Property whitespace changes | Same C values: 615 Snapshot Record JSON, 409 Source Locators, 291 Deferred Relationship Assertions, 269 Source Assertion JSON, 85 Derivation Provenance |
| 3,910 original node alpha defaults materialised as 100 | Known C behaviour remains; additional authored colours/geometry remain separate |
| 34 View Documentation strings lose one final LF | Known C losses retained; no new semantic Documentation rule inferred |
| Connection ordering differs in 15 transport-affected Views | Known C serialization; paint-order equivalence remains unproved |

No transport transformation is newly attributed to S → I. New Views, semantic
records, V1 styling/geometry and V2 connections are additional observations, not
explained away as transport. Prefix/indentation syntax is not semantic identity;
no comparator normalisation or metadata repair was introduced.

## 12. Candidate Inferences — non-authoritative

- **CI1:** A1's Name, Capability type, earlier Composition and additional
  Realizations/Views are compatible with a possible Harmonia Feature interpretation.
  They do not establish its missing `harmoniaType`, identity, definition or standing.
- **CI2:** R7–R9/R12 may propose realisation of A1/Lineage by the named Business
  behaviours. The native Realization assertions are facts; their governed meaning,
  bounded responsibilities and acceptance are not established by those assertions
  or View Names. Lineage remains `v0.2 / Architectural Status=Proposed` and
  `harmoniaType=Feature`, with Canonical ID `d72134e75001449a9e7ffae53f61458f`.
- **CI3, retained:** R1–R6 may propose Composition for CT3/Feature framing.
  [Mapping §5](../../docs/markdown/governance/rad-archimate-model-mapping.md#5-relationship-profile-and-direction)
  still leaves that native encoding subject to adjudication. Six incoming edges
  do not establish a general Composition or View-layout convention.

No clinical/domain authority, Product deployment model, service ownership,
Function/Process chain, missing relationship or intended test objective is inferred.

## 13. Unresolved findings for Solution Architect adjudication

**Unresolved / insufficient evidence:**

1. The exact second-export XML is unavailable. Recorded observations establish
   the reported immediate deltas, but cannot certify a complete byte-level before/
   after comparison, unrecorded serialization details or the native edit sequence.
2. A1–A9 need decisions on identity, source definitions, governed names, typing
   qualifications and standing before acceptance. Existing Names/native types were
   reported, not normalised. A1's Feature interpretation remains unestablished.
3. R7–R13 require review of their asserted meanings, direction, native legality
   and source authority; XSD/reference PASS does not prove those architectural
   matters. R1–R6's framing interpretation remains open, without retrospective
   reinterpretation. Bare relationships carry no source/acceptance Properties.
4. The thirteen root relationship homes remain separate organisation discrepancies;
   no repair or inferred ownership follows. New elements' governed navigation
   context is not supplied by their View appearances.
5. V1's unchanged Documentation refers to governed children/standing in Properties,
   while A1 lacks those Properties. The same new fill on existing Proposed Lineage
   does not approve it. V3/V4 have no scope Documentation. View acceptance,
   readability and treatment of human presentation state remain for review.

No possible semantic duplication was observed under the specified exact-ID test;
unknown canonical identities were not silently equated or resolved.

## 14. Current machinery assessment

The unchanged Increment 1 machinery detects additional exact IDs, missing metadata,
reference/schema issues and detailed XML/visual differences. Together with direct
queries it exposes all nine added elements, thirteen added relationships, actual
native types/endpoints, independent organisation homes and changed visual records.
It does not implement architectural acceptance or reliable Increment 2 classification.

| Existing comparison | Result | Raw events / classification counts |
| --- | --- | --- |
| P → I | DIFFERENCES, exit 2 | 10,806: 1,669 semantic; 4,270 loss; 4,805 unexplained; 62 equivalent; 0 presentation-preserving |
| F → I, supplemental first-state comparison | DIFFERENCES, exit 2 | 112: 111 unexplained, 1 loss |
| C → I, transport-control comparison | DIFFERENCES, exit 2 | 125: 123 unexplained, 2 loss, including the governed release-value difference |
| S → I exact XML comparison | UNRESOLVED / unavailable | Not run against a reconstructed XML or mislabelled F |

The comparator classifies added semantic relationship subtrees as **unexplained**;
zero `SEMANTIC DIFFERENCE` events in F → I/C → I does not mean zero semantic additions.
Its P → I retained-record lists flag 625 elements, 269 relationships and 1,195
Views because transport findings remain; these are not architectural edit counts.
It retains IDs/counts separately from edited-retained-object lists.

Anonymous organisation is paired positionally. The new View folder shifts V1's
reference position, yielding an extra raw `INFORMATION LOSS` event at
`/model/organizations[1]/item[position=6]/item[position=3]/item[position=2]/item[position=4]/item[position=1]/item[position=4]/item[position=6]/@identifierRef`.
Its old value is V1's ID; the new item is a folder. V1 still has its original path
and reference, so this event does **not** establish deletion or loss of that View.
The analogous earlier A1 insertion accounts for C → I's other positional loss.

Exact triple grouping finds no semantic duplicates here; independently resolving
connection `relationshipRef` values distinguishes V2's existing semantics from new
records. These observations are not a claim of an existing automatic duplicate
classification or organisation reconciler. Missing identity/authority, framing
adjudication and presentation acceptance remain beyond automatic inference. The
absent S snapshot also prevents an exact immediate comparator run. No equivalence,
ingestion, layout or reconciliation behaviour was added to obtain these results.

## 15. Validation and stop point

| Check | Result / limit |
| --- | --- |
| Existing offline schema, global identifiers, Property definitions/references, semantic endpoints, View-local references and visual/semantic endpoint agreement | **PASS** on P/F/C/I; no input issues; not native-semantic or architectural approval |
| Existing identity/naming/source audit | Execution **PASS**; readiness **NONCONFORMANT** for nine metadata-incomplete additions |
| Required architecture suite, JDK 21 / offline Maven | **PASS**: 90 tests; zero failures, errors or skips; 27.553 seconds; bounded to 180 seconds |
| Release validator | **PASS**, version 1.0.8 / previous 1.0.7; bounded to 30 seconds |
| Whole-workspace Git whitespace check | Exit 2; 306 supplied incoming CRLF lines flagged as trailing whitespace; inputs preserved unchanged; new report formatting checked separately |
| Protected input/authority preservation | **PASS**, 441 pre-existing files hash-identical to start, including P/I, all RADS/ArchiMate framework/publication files, release authority, both earlier reports and native/backup workbench files |
| Reconciliation / metadata allocation / release increment / publication | None executed; no reconciliation metadata, Canonical IDs, names, relationships, Views or rules written to an input or governed source |

Executed commands, with logs/results retained in the evidence bundle:

```bash
timeout --signal=TERM --kill-after=5s 60s python3 -B docs/archimate/framework/tools/diagnose_roundtrip.py compare --published <P|F|C> --incoming <I> --output /tmp/harmonia-108-third-<comparison>.json
timeout --signal=TERM --kill-after=5s 60s python3 -B docs/archimate/framework/tools/diagnose_roundtrip.py diagnose --model <I> --previous <P> --baseline-context --output /tmp/harmonia-108-third-diagnostic.json
timeout --signal=TERM --kill-after=5s 180s env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
timeout --signal=TERM --kill-after=5s 30s python3 -B scripts/architecture_release.py check
```

No command timed out or stalled. An initial temporary observation query had an
unsupported ElementPath predicate; switching that query to XPath corrected the
query harness without changing repository machinery or model content. Schema,
reference, architecture and release checks completed independently.

The validation record pins start/end hashes and evidence hashes and records the
existing Git edits/staging. Only this new third-pass report and its analysis/
validation evidence were added in the repository. Existing reports were not
overwritten. No new RADS/ArchiMate files or publication outputs were created.
Release authority remains 1.0.8. **STOP: no reconciliation, repair, implementation,
governance change, release increment or publication follows this analysis.**
