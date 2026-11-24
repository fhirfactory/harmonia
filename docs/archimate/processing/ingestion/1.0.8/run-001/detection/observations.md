# Fresh detection evidence — 1.0.8/run-001

Observation only. A/R/V labels below are evidence locators, not architectural identities. All physical lines refer to the immutable snapshot [submission.xml](submission.xml).

## Method and complete difference summary

The unchanged Increment 1 comparator ran P → submission and transport-control C → submission; its identity/source-context audit also ran. [Raw diagnostic commands](diagnostic-commands.json) and compressed JSON preserve all events. [detect.py](detect.py) independently indexes the final submission against P and compares retained records against the justified no-edit control. It resolves Property references by exact keys, retains values/languages/multiplicity and compares native records, visual nodes/connections and path/reference multisets separately. Indentation is ignored only as XML syntax; evidence-text changes remain visible. No label-based identity matching or new comparator normalisation is used.

| Collection | Baseline | Submission | Fresh finding |
| --- | ---: | ---: | --- |
| Elements | 1,299 | 1,308 | 9 additions; no removals or retained semantic edits beyond control |
| Relationships | 852 | 865 | 6 Composition, 4 Realization, 2 Flow, 1 Serving added |
| Views | 1,196 | 1,198 | 2 added, 2 existing modified; 1,194 unchanged beyond transport |
| Visual nodes | 3,915 | 3,923 | 8 added; 6 retained nodes edited beyond transport |
| Visual connections | 1,449 | 1,469 | 20 added: 13 new-edge appearances and 7 existing-edge appearances |
| Explicit bendpoints | 2,922 | 2,928 | 6 added in V3 |
| Folder paths / references | 3,749 / 3,347 | 3,752 / 3,371 | +3 folders, +24 references; no lost/moved existing membership |
| Governed canonical owners | 1,299 | 1,299 | Exact continuity; no mutated, malformed or duplicate claims |

No semantic element/relationship/View removals, retained retypes, native Name mutations, semantic Documentation edits, new Property keys/datatypes, or unresolved XML/visual endpoint references were detected. Model identity/Name/Documentation/resolved model Properties retain the 1.0.8 baseline. All 1,299 retained element records and all 852 retained relationship records match C exactly. Raw P → submission changed-record counts include transport, not additional architectural changes.

## Elements

| Evidence | Native type | Exact submitted Name | Exchange identity / physical line | Recognition |
| --- | --- | --- | --- | --- |
| A1 | Capability | `[Feature] Identifier Capture` | `id-727a264026584f80bc3304ff8383dc92` / 34747 | Feature proposed classification recognised deterministically; native Capability retained |
| A2 | Product | `[Product] Provider Registry - Satellite` | `id-234b080d0ea54d57bb0001a2cb861080` / 87210 | Qualifier repeats native type; no additional Harmonia subtype recognised |
| A3 | Product | `[Product] Provider Registry - Central` | `id-bfdd958364d1466a914d474dce8baab9` / 87213 | Qualifier repeats native type; no additional Harmonia subtype recognised |
| A4 | Product | `[Product] Practitioner Collaboration Service` | `id-3c5ae26723224eb0a6e6a70d70fe2c83` / 87216 | Qualifier repeats native type; no additional Harmonia subtype recognised |
| A5 | Product | `[Product] Health Information Exchange Service` | `id-d84d8e1f988c4bc998b18834aabfcd74` / 87219 | Qualifier repeats native type; no additional Harmonia subtype recognised |
| A6 | BusinessService | `[Business Service] Identifier Create` | `id-fddda503b8f0438e995465cd93204326` / 62338 | Qualifier repeats native type; no additional Harmonia subtype recognised |
| A7 | BusinessService | `[Business Service] Identifier Update` | `id-0088d61ccfcf4962b3aab8bc3b96021d` / 62341 | Qualifier repeats native type; no additional Harmonia subtype recognised |
| A8 | BusinessProcess | `[Business Process] Identifier Verification` | `id-c645a859ac2c4447ad5ae850cf384bf3` / 62344 | Qualifier repeats native type; no additional Harmonia subtype recognised |
| A9 | BusinessService | `[Business Service] Verify Identity Source` | `id-9bfedbfd5b404694a2224c039fa5d4c4` / 62347 | Qualifier repeats native type; no additional Harmonia subtype recognised |

All nine records have no Properties or Documentation. Their missing machine metadata is expected authoring evidence under 2B, not an architectural defect. Final identity/new-instance binding and responsibility remain adjudication questions; no IDs or final names have been supplied. A2–A5 have no semantic relationships or visual appearances. A1 has R6–R9 and appearances in V1/V3. A6–A9 participation is explicit in [elements.json](elements.json).

## Semantic relationships

| Evidence | Native type | Source → target (exact native Names) | Exchange identity / physical line | Duplicate finding |
| --- | --- | --- | --- | --- |
| R1 | Composition | `Person Identity Administration` → `Cross-Authority Identifier Correlation` | `id-73ecd7a39407404ba8e2bb663efbdc19` / 95230 | No baseline duplicate against resolved governed endpoints |
| R2 | Composition | `Person Identity Administration` → `Governed Identity Correction` | `id-36b2aa8ad58149fdb20d44c1c16dd64c` / 95231 | No baseline duplicate against resolved governed endpoints |
| R3 | Composition | `Person Identity Administration` → `Identity Source & Correction Lineage` | `id-3c3d723683c147a4a902b4d35a2af018` / 95232 | No baseline duplicate against resolved governed endpoints |
| R4 | Composition | `Person Identity Administration` → `Identifier Resolution` | `id-daf437bb32114daa8a6b9462cb9b4fb2` / 95233 | No baseline duplicate against resolved governed endpoints |
| R5 | Composition | `Person Identity Administration` → `Identity Alias Association` | `id-f9b270950f0241108b68f1c3daa50019` / 95234 | No baseline duplicate against resolved governed endpoints |
| R6 | Composition | `Person Identity Administration` → `[Feature] Identifier Capture` | `id-bff76286f8994d20aa1b8aef166acebd` / 95235 | UNRESOLVED: new endpoint has no governed architectural identity |
| R7 | Realization | `[Business Service] Identifier Create` → `[Feature] Identifier Capture` | `id-e84f2e9df66a48d795d604d75afc72cb` / 95236 | UNRESOLVED: new endpoint has no governed architectural identity |
| R8 | Realization | `[Business Service] Identifier Update` → `[Feature] Identifier Capture` | `id-69eb864f56df4d928f8abc3f7ab4e834` / 95237 | UNRESOLVED: new endpoint has no governed architectural identity |
| R9 | Realization | `[Business Process] Identifier Verification` → `[Feature] Identifier Capture` | `id-c0f84a8e4c8d496580a68cac4823c58c` / 95238 | UNRESOLVED: new endpoint has no governed architectural identity |
| R10 | Flow | `[Business Service] Identifier Create` → `[Business Process] Identifier Verification` | `id-bef77e2eb7de4691b4fbe9a0c84e8232` / 95239 | UNRESOLVED: new endpoint has no governed architectural identity |
| R11 | Flow | `[Business Service] Identifier Update` → `[Business Process] Identifier Verification` | `id-0849f31d6d7d4f7eb1e484d7437e5cd7` / 95240 | UNRESOLVED: new endpoint has no governed architectural identity |
| R12 | Realization | `[Business Service] Verify Identity Source` → `Identity Source & Correction Lineage` | `id-37f6913198e2434db5309c1c8e36fbc0` / 95241 | UNRESOLVED: new endpoint has no governed architectural identity |
| R13 | Serving | `[Business Service] Verify Identity Source` → `[Business Process] Identifier Verification` | `id-c53c42ba0a4f437f9cb76a4c1a701816` / 95242 | UNRESOLVED: new endpoint has no governed architectural identity |

Each is an explicit semantic record, with no Name, Properties or Documentation. Every one is referenced once directly under `Relations`. [relationships.json](relationships.json) retains exact semantic endpoint IDs, existing Canonical IDs, target standing, comparison keys and discriminator evidence. XSD/reference success does not adjudicate native legality or Harmonia meaning.

### Relationship reuse and duplicate analysis

R1–R5 have both endpoint Canonical IDs resolved and have no baseline native-type/source/target match. No resolved-canonical-key duplicates or exact Exchange-triple duplicates occur anywhere in the final model. R6–R13 contain at least one new endpoint without governed identity: their final semantic duplicate status remains **UNRESOLVED**, despite unique observed transport triples. No attempted duplicate against fully resolved governed endpoints is positively identified. This is more precise than the historical exact-Exchange-triple “none” result. Do not allocate, merge or collapse an edge to resolve that uncertainty.

The seven V2 connections reuse established governed relationship bindings. Their exact baseline Properties/assertion discriminators are retained in [views.json](views.json); none is replaced by tuple matching or a new semantic relationship. Multiple appearances do not create multiple edges.

<a id="existing-feature-context"></a>

### Existing Feature context

The current RADS [Feature records](../../../../../../markdown/governance/baseline/features.md) already retain source-qualified Person Identity Administration responsibility/framing context for the five existing targets, each with **Proposed placement**. This existing contextual evidence is not a baseline native Composition. The proposed native encoding and any change in interpretation/standing require separate adjudication. Existing names/contexts are not regenerated here.

| R | RADS Feature source anchor | Existing canonicalId | Architectural standing / placement |
| --- | --- | --- | --- |
| R1 | [features.md#feat-em-02](../../../../../../markdown/governance/baseline/features.md#feat-em-02) | `6d74d143c7454fa0bf30743ddf7ba094` | Canonical / Proposed |
| R2 | [features.md#feat-em-04](../../../../../../markdown/governance/baseline/features.md#feat-em-04) | `f7ab7a24e8234129b2ab02e9abd4fba2` | Canonical / Proposed |
| R3 | [features.md#hcm-ft-identity-source-correction-lineage](../../../../../../markdown/governance/baseline/features.md#hcm-ft-identity-source-correction-lineage) | `d72134e75001449a9e7ffae53f61458f` | Proposed / Proposed |
| R4 | [features.md#feat-em-01](../../../../../../markdown/governance/baseline/features.md#feat-em-01) | `ae034169e1394ccdb3a2ce9651eaf305` | Canonical / Proposed |
| R5 | [features.md#feat-em-03](../../../../../../markdown/governance/baseline/features.md#feat-em-03) | `97721dbd017e4c2daa73e1e87a8499b9` | Canonical / Proposed |

The baseline source record `proposal:feature-identity-source-correction-lineage` retains its boundary: no probabilistic matching, golden-record selection or autonomous merge authority. R3/R12 and V1/V4 do not promote its standing or alter this boundary. Context-interpretation CRs ask about the native encoding and bounded effects, not silent promotion of existing placement.

## Views

<a id="v1"></a>

### V1 — 0-Person Identity Administration — Feature Landscape

`modified`; Exchange `id-e2a326c7d36751af9076a32dabe0e9cd`, physical line 256740. Baseline 6 nodes / 0 connections; submitted 7 nodes / 6 connections. No removed nodes/connections. All nodes are direct children of the View: no XML semantic containment is asserted.

Submitted Documentation: `Context and immediate governed children only. Standing remains in element Properties. Navigation and proximity create no semantic relationships.`

| Node ID | Exact represented Name | Appearance | Submitted (x, y, w, h) |
| --- | --- | --- | --- |
| `id-a17cde2ab18f527f9103f8c50e32958f` | `Person Identity Administration` | modified beyond control | ('12', '12', '192', '493') |
| `id-ce6796499a7e5e128aef2c2b31b44df9` | `Cross-Authority Identifier Correlation` | modified beyond control | ('241', '348', '240', '73') |
| `id-af6828d7edce5c05b5819b597293a981` | `Governed Identity Correction` | modified beyond control | ('241', '180', '240', '73') |
| `id-74781db5bf4e5fe38af2da1bf742f003` | `Identifier Resolution` | modified beyond control | ('241', '96', '240', '76') |
| `id-8459b669dc085cabbcfca4ede19c444e` | `Identity Alias Association` | modified beyond control | ('241', '429', '240', '76') |
| `id-0369c1bef15255da8cf693f89b7295e7` | `Identity Source & Correction Lineage` | modified beyond control | ('240', '264', '240', '73') |
| `id-073fae7e65554293bed1a47ff6e535ee` | `[Feature] Identifier Capture` | added | ('241', '12', '240', '73') |

| Connection ID | relationshipRef | Semantic source → target | Relationship origin |
| --- | --- | --- | --- |
| `id-66c57297bd4f4d898a53fe3d4bcc6b13` | `id-36b2aa8ad58149fdb20d44c1c16dd64c` | `Person Identity Administration` → `Governed Identity Correction` | new semantic relationship proposal |
| `id-3242d1af0d884c9ab36adbe7f0feb936` | `id-3c3d723683c147a4a902b4d35a2af018` | `Person Identity Administration` → `Identity Source & Correction Lineage` | new semantic relationship proposal |
| `id-fa56f00e1759495d82b8c40c00792ab9` | `id-daf437bb32114daa8a6b9462cb9b4fb2` | `Person Identity Administration` → `Identifier Resolution` | new semantic relationship proposal |
| `id-df01f77a7fa245d89bc3a6992ee3d829` | `id-f9b270950f0241108b68f1c3daa50019` | `Person Identity Administration` → `Identity Alias Association` | new semantic relationship proposal |
| `id-7239b62978b141519ddde72feb8b40fa` | `id-bff76286f8994d20aa1b8aef166acebd` | `Person Identity Administration` → `[Feature] Identifier Capture` | new semantic relationship proposal |
| `id-27509aa6cf6b40f6b3bdfd76f9ac949c` | `id-73ecd7a39407404ba8e2bb663efbdc19` | `Person Identity Administration` → `Cross-Authority Identifier Correlation` | new semantic relationship proposal |

Exact View-local source/target IDs, native semantic endpoints, styles, ordered bendpoints, baseline node XML and submitted XML are retained in [views.json](views.json). Every visual endpoint agrees with its referenced semantic edge; reference resolution does not approve architecture or visual readability.

<a id="v2"></a>

### V2 — 0-Entity Information Enablement — CT2 Landscape

`modified`; Exchange `id-c8ec850977565a17869af357ee28d917`, physical line 259158. Baseline 8 nodes / 0 connections; submitted 8 nodes / 7 connections. No removed nodes/connections. All nodes are direct children of the View: no XML semantic containment is asserted.

Submitted Documentation: `Context and immediate governed children only. Standing remains in element Properties. Navigation and proximity create no semantic relationships.`

| Node ID | Exact represented Name | Appearance | Submitted (x, y, w, h) |
| --- | --- | --- | --- |
| `id-84aa3ddd9d375b48981e4d7e4b8d2d7e` | `Entity Information Enablement` | retained | ('40', '40', '360', '148') |
| `id-92c2544bb75954fd8dfac150373bf8b5` | `Client Administration` | retained | ('40', '230', '360', '148') |
| `id-1ae6d706a2d85b9eac70a31e969c76d8` | `Clinical Device Administration` | retained | ('450', '230', '360', '148') |
| `id-4b3c4cbc947a555b914c6a0bc31f4b93` | `Health Materiel Administration` | retained | ('860', '230', '360', '148') |
| `id-5bfc1365cdde59799f741698323c415b` | `Health Service Administration` | retained | ('1270', '230', '360', '148') |
| `id-82338ac3935152f0866281add52432b0` | `Location Administration` | retained | ('40', '420', '360', '148') |
| `id-a2eee58a65e25eb0b7dfaa0186c6d11c` | `Organisation Administration` | retained | ('450', '420', '360', '148') |
| `id-302c6b450f875419b7ea4ea3dee8d0d0` | `Provider Administration` | retained | ('860', '420', '360', '148') |

| Connection ID | relationshipRef | Semantic source → target | Relationship origin |
| --- | --- | --- | --- |
| `id-62f38f2147d54605be108818ddb65a25` | `id-f7deddb1eb2751aa96ba6a490b18003e` | `Entity Information Enablement` → `Clinical Device Administration` | existing governed relationship |
| `id-3709d706394f4695b9886cb76038762a` | `id-be1babf8397d51b3878b271f8775d711` | `Entity Information Enablement` → `Health Materiel Administration` | existing governed relationship |
| `id-39efc4286edf40609e42ded6b79737c6` | `id-c099301d131c594580c840a6363997a3` | `Entity Information Enablement` → `Health Service Administration` | existing governed relationship |
| `id-0d6cbbef60984cad9624700a2b53c2da` | `id-c97120325e3e5a1b99a11eeec1e5c89e` | `Entity Information Enablement` → `Organisation Administration` | existing governed relationship |
| `id-6ec0b7e2d8964fe287b08e19e3af6cd7` | `id-3fc7839fceda5f4193e58dd14415a0ab` | `Entity Information Enablement` → `Client Administration` | existing governed relationship |
| `id-587eff9341f64a99b0581ecc74030c17` | `id-b297858a4ab352cb942b88df159109ed` | `Entity Information Enablement` → `Location Administration` | existing governed relationship |
| `id-b45f6090065040c5b2cf8a39ac28a9dc` | `id-05fb0b3f26d8545a8caf8fcb8a92b2e3` | `Entity Information Enablement` → `Provider Administration` | existing governed relationship |

Exact View-local source/target IDs, native semantic endpoints, styles, ordered bendpoints, baseline node XML and submitted XML are retained in [views.json](views.json). Every visual endpoint agrees with its referenced semantic edge; reference resolution does not approve architecture or visual readability.

<a id="v3"></a>

### V3 — Identifier Capture - Feature realisation

`added`; Exchange `id-216c4ab0757b45b3894e7e0c2594b9f9`, physical line 256638. Baseline 0 nodes / 0 connections; submitted 5 nodes / 6 connections. No removed nodes/connections. All nodes are direct children of the View: no XML semantic containment is asserted.

Submitted Documentation: absent; purpose inferred only as a candidate from membership and Name.

| Node ID | Exact represented Name | Appearance | Submitted (x, y, w, h) |
| --- | --- | --- | --- |
| `id-4f78c588c80d42e0b4f52e765dc76ff2` | `[Business Service] Identifier Create` | added | ('168', '36', '253', '55') |
| `id-3fd6725d68f4476884cf44f9fbf09794` | `[Feature] Identifier Capture` | added | ('12', '36', '120', '397') |
| `id-d0a1079102c14d26901ace75bd379281` | `[Business Service] Identifier Update` | added | ('168', '108', '253', '55') |
| `id-a222a0f6a6804944b188e32ee5caa9a5` | `[Business Process] Identifier Verification` | added | ('168', '180', '253', '55') |
| `id-8e77730d5e7147bea761ee18da71358c` | `[Business Service] Verify Identity Source` | added | ('636', '180', '253', '55') |

| Connection ID | relationshipRef | Semantic source → target | Relationship origin |
| --- | --- | --- | --- |
| `id-007507b4daa748ee8df77d2dbf8a496d` | `id-e84f2e9df66a48d795d604d75afc72cb` | `[Business Service] Identifier Create` → `[Feature] Identifier Capture` | new semantic relationship proposal |
| `id-95c171f829a849a3ac544a931b4d6a5f` | `id-bef77e2eb7de4691b4fbe9a0c84e8232` | `[Business Service] Identifier Create` → `[Business Process] Identifier Verification` | new semantic relationship proposal |
| `id-f64ce7c8aac347b2856dbceba42237d6` | `id-69eb864f56df4d928f8abc3f7ab4e834` | `[Business Service] Identifier Update` → `[Feature] Identifier Capture` | new semantic relationship proposal |
| `id-851cf4ffa1eb436983a9e47a6f5157b5` | `id-0849f31d6d7d4f7eb1e484d7437e5cd7` | `[Business Service] Identifier Update` → `[Business Process] Identifier Verification` | new semantic relationship proposal |
| `id-b1f2f750233f4717b5bbedcf413e3c61` | `id-c0f84a8e4c8d496580a68cac4823c58c` | `[Business Process] Identifier Verification` → `[Feature] Identifier Capture` | new semantic relationship proposal |
| `id-e11b8af91143439bbb54ff0be650fb79` | `id-c53c42ba0a4f437f9cb76a4c1a701816` | `[Business Service] Verify Identity Source` → `[Business Process] Identifier Verification` | new semantic relationship proposal |

Exact View-local source/target IDs, native semantic endpoints, styles, ordered bendpoints, baseline node XML and submitted XML are retained in [views.json](views.json). Every visual endpoint agrees with its referenced semantic edge; reference resolution does not approve architecture or visual readability.

<a id="v4"></a>

### V4 — Identity Source & Correction Lineage - Feature realisation

`added`; Exchange `id-8fb2f8b1776f4ebf969e9b5b638e4ed3`, physical line 256609. Baseline 0 nodes / 0 connections; submitted 2 nodes / 1 connections. No removed nodes/connections. All nodes are direct children of the View: no XML semantic containment is asserted.

Submitted Documentation: absent; purpose inferred only as a candidate from membership and Name.

| Node ID | Exact represented Name | Appearance | Submitted (x, y, w, h) |
| --- | --- | --- | --- |
| `id-030fc162b19b41c8930dac9f9c901bf0` | `Identity Source & Correction Lineage` | added | ('12', '24', '240', '409') |
| `id-5a05920f7422487c930faff16187327b` | `[Business Service] Verify Identity Source` | added | ('312', '24', '253', '55') |

| Connection ID | relationshipRef | Semantic source → target | Relationship origin |
| --- | --- | --- | --- |
| `id-d4301bf1848e4e7e9a1d5267dfa3594e` | `id-37f6913198e2434db5309c1c8e36fbc0` | `[Business Service] Verify Identity Source` → `Identity Source & Correction Lineage` | new semantic relationship proposal |

Exact View-local source/target IDs, native semantic endpoints, styles, ordered bendpoints, baseline node XML and submitted XML are retained in [views.json](views.json). Every visual endpoint agrees with its referenced semantic edge; reference resolution does not approve architecture or visual readability.

V1 changes six retained node geometries/styles and adds A1 plus six new Composition appearances. Five retained child fonts change 12 → 9; all six existing nodes end with fill (245,222,170); the source retains Sans 12. V2 retains every node/geometry/style beyond transport and adds seven existing-edge appearances. V3 has five new nodes, six new connections, six explicit bendpoints. V4 has two new nodes and one new connection. No View was regenerated or laid out by this run.

## Navigation

New element folders: `Business / Products` and the `[Feature] Identifier Capture` folder under Strategy / Business Layer / Business Enabling Capabilities / [CT1] Entity Information Enablement / [CT2] Client Administration / [CT3] Person Identity Administration. A6–A9 references are directly under `Business`. New View folder: the corresponding Identifier Capture context beneath Views / 2-BusinessLayer / 2-Business Enabling Capabilities / the same CT1–CT3 path. V3 is referenced there; V4 is added to the existing Lineage Feature folder. Existing View homes do not move.

All thirteen relationship references are directly under `Relations`; this does not establish relationship meaning or an authoritative home. 2B governs later source-home reprojection from resolved governed source context. No CR exists merely to replay arbitrary incoming relationship folders. [navigation.json](navigation.json) preserves every added path/reference and confirms no existing membership loss/move. The authored new View homes remain explicit presentation questions in CR-035/036; their nesting supplies no semantic framing.

## Transport and evidence-only findings

The preserved real no-edit 1.0.7 Archi export is a transport control, justified by exact in-memory restoration of 1.0.8’s single model release value to the 1.0.7 published byte stream. It is not the semantic baseline. [transport.json](transport.json) and the raw diagnostics preserve the following:

- All 61 Property-definition IDs are rekeyed with references redirected; unique keys/string datatypes remain and all 61 definition-name language attributes are lost.
- 1,669 evidence-Property whitespace changes match C exactly: 615 Snapshot Record JSON, 409 Source Locators, 291 Deferred Relationship Assertions, 269 Source Assertion JSON, 85 Derivation Provenance.
- All 7,096 original organisation item identities are absent after transport; path/reference membership is checked independently. Original folder identity/edit sequence remains unknown.
- 3,910 original node alpha defaults materialise as 100; 34 View Documentation strings lose a final LF; 15 Views have known connection-order transformations. No whole-model fidelity or paint-order equivalence is claimed.
- Raw P → submission: 10,806 events (1,669 semantic, 4,270 loss, 4,805 unexplained, 62 equivalent); C → submission: 125 events (123 unexplained, 2 positional loss, including the governed release transition). The two positional losses do not establish semantic/reference deletion; path/reference multisets demonstrate retention.
- The identity diagnostic executes successfully but reports NONCONFORMANT readiness for nine missing metadata sets. Current 2B treats absence on new authoring as evidence, not nine metadata-defect CRs.
- The baseline has 841 existing native Name/displayName discrepancies, unchanged in the submission. The pre-existing migration population remains external/deferred; there are no submitted Name/displayName mutation CRs.

Known transport differences, missing generated metadata and existing migration debt receive no architectural acceptance or reconciliation through this record. No new publication, universal profile, ID allocation, relationship home or generated naming is established.
