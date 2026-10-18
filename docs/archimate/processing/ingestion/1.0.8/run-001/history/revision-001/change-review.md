| ID | Brief Description | Status |
| --- | --- | --- |
| [CR-001](#cr-001) | Add Identifier Capture Feature | On-Hold |
| [CR-002](#cr-002) | Add Provider Registry - Satellite Product | On-Hold |
| [CR-003](#cr-003) | Add Provider Registry - Central Product | On-Hold |
| [CR-004](#cr-004) | Add Practitioner Collaboration Service Product | On-Hold |
| [CR-005](#cr-005) | Add Health Information Exchange Service Product | On-Hold |
| [CR-006](#cr-006) | Add Identifier Create Business Service | On-Hold |
| [CR-007](#cr-007) | Add Identifier Update Business Service | On-Hold |
| [CR-008](#cr-008) | Add Identifier Verification Business Process | On-Hold |
| [CR-009](#cr-009) | Add Verify Identity Source Business Service | On-Hold |
| [CR-010](#cr-010) | Add Composition to Cross-Authority Identifier Correlation | On-Hold |
| [CR-011](#cr-011) | Add Composition to Governed Identity Correction | On-Hold |
| [CR-012](#cr-012) | Add Composition to Identity Source & Correction Lineage | On-Hold |
| [CR-013](#cr-013) | Add Composition to Identifier Resolution | On-Hold |
| [CR-014](#cr-014) | Add Composition to Identity Alias Association | On-Hold |
| [CR-015](#cr-015) | Add Composition to Identifier Capture | On-Hold |
| [CR-016](#cr-016) | Interpret CT3 framing for Cross-Authority Identifier Correlation | On-Hold |
| [CR-017](#cr-017) | Interpret CT3 framing for Governed Identity Correction | On-Hold |
| [CR-018](#cr-018) | Interpret CT3 framing for Identity Source & Correction Lineage | On-Hold |
| [CR-019](#cr-019) | Interpret CT3 framing for Identifier Resolution | On-Hold |
| [CR-020](#cr-020) | Interpret CT3 framing for Identity Alias Association | On-Hold |
| [CR-021](#cr-021) | Interpret CT3 framing for Identifier Capture | On-Hold |
| [CR-022](#cr-022) | Add Identifier Create Realization | On-Hold |
| [CR-023](#cr-023) | Interpret Feature context for Identifier Create | On-Hold |
| [CR-024](#cr-024) | Add Identifier Update Realization | On-Hold |
| [CR-025](#cr-025) | Interpret Feature context for Identifier Update | On-Hold |
| [CR-026](#cr-026) | Add Identifier Verification Realization | On-Hold |
| [CR-027](#cr-027) | Interpret Feature context for Identifier Verification | On-Hold |
| [CR-028](#cr-028) | Add Verify Identity Source Realization | On-Hold |
| [CR-029](#cr-029) | Interpret Feature context for Verify Identity Source | On-Hold |
| [CR-030](#cr-030) | Add Identifier Create Flow to Identifier Verification | On-Hold |
| [CR-031](#cr-031) | Add Identifier Update Flow to Identifier Verification | On-Hold |
| [CR-032](#cr-032) | Add Verify Identity Source Serving to Identifier Verification | On-Hold |
| [CR-033](#cr-033) | Revise Person Identity Administration Feature Landscape | On-Hold |
| [CR-034](#cr-034) | Expose existing Compositions in Entity Information Enablement View | On-Hold |
| [CR-035](#cr-035) | Add Identifier Capture Feature realisation View | On-Hold |
| [CR-036](#cr-036) | Add Identity Source and Correction Lineage realisation View | On-Hold |

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate Ingestion Change Review — 1.0.8/run-001

## Review basis

- Review/run: **1.0.8/run-001**; revision **1**; prepared by **Codex / 2026-10-18**.
- Submitted incoming evidence: [docs/archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml](../../../../exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml); SHA-256 **`6b973db242a4b26bc6a93e27b482100b6f96655cb40deb4ceb35a93c134c7d3e`**; 21,854,756 bytes.
- Byte-identical retained input: [detection/submission.xml](detection/submission.xml), same SHA-256. The Authoring Iteration is **closed** by this deliberate Submission. Earlier evolving exports are historical working-state evidence, not separate Submissions.
- Exact originating Published Baseline: **Release/Publication 1.0.8**, [docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml](../../../../exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml); SHA-256 **`504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a`**; 20,800,334 bytes.
- Publication provenance: [docs/archimate/exchange/publish/README.md](../../../../exchange/publish/README.md), [docs/archimate/processing/publishing/1.0.8/publication-1.0.8.md](../../../publishing/1.0.8/publication-1.0.8.md), [docs/archimate/processing/publishing/1.0.8/publication-comparison.json](../../../publishing/1.0.8/publication-comparison.json), [docs/archimate/processing/publishing/1.0.8/archi-inspection-1.0.8.md](../../../publishing/1.0.8/archi-inspection-1.0.8.md), [docs/archimate/processing/publishing/1.0.8/publication-validation-1.0.8.json](../../../publishing/1.0.8/publication-validation-1.0.8.json). The index identifies this published model; the publication/inspection/comparison pin its hash. Exact one-value restoration reproduces the published 1.0.7 byte stream. Submission retains the 1.0.8 model ID/release Property and all established bindings. Native edit/operator history beyond that evidence is not invented.
- Current repository governance release: **1.0.11**, previous 1.0.10; target source HEAD **`6bf6767ce7449560d2e34401ff260fd7ae3cfeef`**, with pre-existing working-tree changes captured in [start-preservation](detection/start-preservation.json). Working-tree authority hashes, provenance and tool versions: [provenance.json](detection/provenance.json). **1.0.11 is not this semantic comparison baseline.**
- Transport control: [.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-exports/release-1.0.7.xml](../../../../../../.junie/reports/2026-10-18-archimate-canonical-identity-establishment/archi-exports/release-1.0.7.xml); SHA-256 `38e49fa348d015aba6b19e21b4f96dc00fddf3c4d7671b3e352b4c07748917ea`. Used solely to recognise characterised Archi transformations, not to select architecture.
- Detection: unchanged Increment 1 P→submission and C→submission comparisons, identity/source audit, plus independent run-specific [query](detection/detect.py). [Observations](detection/observations.md), [raw diagnostics](detection/diagnostic-commands.json), [summary](detection/summary.json), [exact visual records](detection/views.json). All model inputs were read only.
- Governed template: [docs/markdown/governance/templates/archimate-ingestion-change-review-template.md](../../../../../markdown/governance/templates/archimate-ingestion-change-review-template.md). Authority: [docs/markdown/governance/archimate-ingestion-change-review.md](../../../../../markdown/governance/archimate-ingestion-change-review.md), [docs/markdown/governance/archimate-ingestion-publication-semantics.md](../../../../../markdown/governance/archimate-ingestion-publication-semantics.md), [docs/markdown/governance/architecture-authoring-praxis.md](../../../../../markdown/governance/architecture-authoring-praxis.md). AX-04 preserves meaning independent of tools; AX-14 separates evidence/interpretation/decision and semantics/navigation/presentation/identity; AX-15 preserves uncertainty; AX-17 leaves missing meaning for adjudication; AX-18 forbids inferred domain authority.
- Historical supporting evidence: [prior-analysis comparison](detection/historical-evidence.md). The exact second intermediate XML remains unavailable; no reconstruction or new Submission was inferred. Current P→final detection needs neither that snapshot nor historical conclusions.
- Run evidence: `detection/` contains input, baseline reference, observations and decomposition; `validation/` contains review/preservation/test evidence. `reconciliation/` is reserved and not materialised because it has no evidence, as required by the run convention. No reconciliation/validation-of-reconciled-state/publication is claimed.
- Release handling: this is preparation of an unadjudicated review, not acceptance of an architectural repository change set. Under release policy, preparation/generation does not increment; **architecture-release.json is untouched**. Any later accepted change set remains subject to the patch rule.

### Decomposition record

Fresh observations yield **36 independently adjudicable CRs**, all On-Hold: 9 element additions, 13 explicit semantic edges, 6 CT3-framing interpretation questions, 4 Realization/logical-context questions and 4 View changes. The full evidence-to-CR rationale and resulting-state dependency matrix are in [decomposition.md](detection/decomposition.md); the exact structured entries are [change-requests.json](detection/change-requests.json).

Known transport transformations, expected absent machine metadata, arbitrary relationship folders and unchanged migration debt generate no architectural CRs. Seven V2 connections expose governed edges and support CR-034 only. Five new Compositions have fully resolved endpoints and no baseline duplicate; the remaining eight added edges retain unresolved final semantic duplicate status because new endpoint identities are unresolved. No positive attempted duplicate is established.

The existing five Feature targets already retain source-qualified CT3 context at Proposed placement standing. The interpretation CRs review the additional native encoding/effect without silently promoting that context. Element/edge/context/View decisions remain independent. All dependencies state the governed result required; none has been evaluated or executed.

| Observation/evidence locator | CR(s) supported | Decomposition reason / evidence-only treatment |
| --- | --- | --- |
| A1–A9; elements.json | CR-001–009 | One proposed architectural instance each; recognised classification stays with its instance |
| R1–R6; relationships.json | CR-010–015 | Six independently reviewable native Composition edges |
| R1–R6 plus current RADS Feature context | CR-016–021 | Six instance framing/native-encoding questions; no universal rule |
| R7–R9/R12 | CR-022/024/026/028 and CR-023/025/027/029 | Native Realizations and optional logical contexts can receive different decisions |
| R10/R11/R13 | CR-030–032 | Two transfer proposals and one service-use proposal |
| V1/V2/V3/V4; views.json | CR-033–036 | One coherent authored View proposal each, independent of semantic approvals |
| Transport/navigation/absent metadata/pre-existing migration | None except View-home questions | Evidence retained; no inferred semantic architecture or machinery-only CR |

<a id="cr-001"></a>

## CR-001 — Add Identifier Capture Feature

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the single architectural instance `[Feature] Identifier Capture`, proposed native `Capability` and recognised Harmonia classification `Feature`.

### Detected Evidence

**Observed Evidence:** [submission.xml:34747](detection/submission.xml#L34747) — `id-727a264026584f80bc3304ff8383dc92`; XPath `/*/*[4]/*[404]`. New semantic element, absent from the 1.0.8 baseline. Native Name: `[Feature] Identifier Capture`. The record has only its Name: no Properties or Documentation. Recognition: Feature proposed classification recognised deterministically; native Capability retained. See [element observations](detection/elements.json) (`A1` mapping in [inventory](detection/observations.md)). Absent canonical identity and derived metadata are evidence only, not a defect CR or allocation instruction.

### Architectural Questions / Adjudication

1. Accept `[Feature] Identifier Capture` as a new Harmonia Feature with native type `Capability`? Confirm whether it is a new architectural instance or identify an established governed binding if reuse was intended.
2. What bounded responsibility/definition and architectural standing are authorised? The submission contains no Documentation or standing assertion.
3. The governed `[Feature]` + Capability rule recognises the proposed Feature classification deterministically. Does the Solution Architect accept this architectural instance? Its CT3 relationship/context is adjudicated separately; recognition supplies no identity or acceptance.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Increment 2A granularity/adjudication; 2B recognition and metadata; AX-04/14/17/18.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-002"></a>

## CR-002 — Add Provider Registry - Satellite Product

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the single architectural instance `[Product] Provider Registry - Satellite`, proposed native `Product`. No additional Harmonia classification is inferred.

### Detected Evidence

**Observed Evidence:** [submission.xml:87210](detection/submission.xml#L87210) — `id-234b080d0ea54d57bb0001a2cb861080`; XPath `/*/*[4]/*[1166]`. New semantic element, absent from the 1.0.8 baseline. Native Name: `[Product] Provider Registry - Satellite`. The record has only its Name: no Properties or Documentation. Recognition: Qualifier repeats native type; no additional Harmonia subtype recognised. See [element observations](detection/elements.json) (`A2` mapping in [inventory](detection/observations.md)). Absent canonical identity and derived metadata are evidence only, not a defect CR or allocation instruction.

### Architectural Questions / Adjudication

1. Accept `[Product] Provider Registry - Satellite` as a new Harmonia Product with native type `Product`? Confirm whether it is a new architectural instance or identify an established governed binding if reuse was intended.
2. What bounded responsibility/definition and architectural standing are authorised? The submission contains no Documentation or standing assertion.
3. `[Product]` repeats the native type and establishes no extra Harmonia subtype. Confirm the intended local display wording under governed naming; no raw Name has been normalised in this review.
4. What Product meaning/scope is intended? The Name supplies no deployment, component allocation, service ownership or domain-decision authority; none is proposed by inference.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Increment 2A granularity/adjudication; 2B recognition and metadata; AX-04/14/17/18.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-003"></a>

## CR-003 — Add Provider Registry - Central Product

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the single architectural instance `[Product] Provider Registry - Central`, proposed native `Product`. No additional Harmonia classification is inferred.

### Detected Evidence

**Observed Evidence:** [submission.xml:87213](detection/submission.xml#L87213) — `id-bfdd958364d1466a914d474dce8baab9`; XPath `/*/*[4]/*[1167]`. New semantic element, absent from the 1.0.8 baseline. Native Name: `[Product] Provider Registry - Central`. The record has only its Name: no Properties or Documentation. Recognition: Qualifier repeats native type; no additional Harmonia subtype recognised. See [element observations](detection/elements.json) (`A3` mapping in [inventory](detection/observations.md)). Absent canonical identity and derived metadata are evidence only, not a defect CR or allocation instruction.

### Architectural Questions / Adjudication

1. Accept `[Product] Provider Registry - Central` as a new Harmonia Product with native type `Product`? Confirm whether it is a new architectural instance or identify an established governed binding if reuse was intended.
2. What bounded responsibility/definition and architectural standing are authorised? The submission contains no Documentation or standing assertion.
3. `[Product]` repeats the native type and establishes no extra Harmonia subtype. Confirm the intended local display wording under governed naming; no raw Name has been normalised in this review.
4. What Product meaning/scope is intended? The Name supplies no deployment, component allocation, service ownership or domain-decision authority; none is proposed by inference.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Increment 2A granularity/adjudication; 2B recognition and metadata; AX-04/14/17/18.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-004"></a>

## CR-004 — Add Practitioner Collaboration Service Product

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the single architectural instance `[Product] Practitioner Collaboration Service`, proposed native `Product`. No additional Harmonia classification is inferred.

### Detected Evidence

**Observed Evidence:** [submission.xml:87216](detection/submission.xml#L87216) — `id-3c5ae26723224eb0a6e6a70d70fe2c83`; XPath `/*/*[4]/*[1168]`. New semantic element, absent from the 1.0.8 baseline. Native Name: `[Product] Practitioner Collaboration Service`. The record has only its Name: no Properties or Documentation. Recognition: Qualifier repeats native type; no additional Harmonia subtype recognised. See [element observations](detection/elements.json) (`A4` mapping in [inventory](detection/observations.md)). Absent canonical identity and derived metadata are evidence only, not a defect CR or allocation instruction.

### Architectural Questions / Adjudication

1. Accept `[Product] Practitioner Collaboration Service` as a new Harmonia Product with native type `Product`? Confirm whether it is a new architectural instance or identify an established governed binding if reuse was intended.
2. What bounded responsibility/definition and architectural standing are authorised? The submission contains no Documentation or standing assertion.
3. `[Product]` repeats the native type and establishes no extra Harmonia subtype. Confirm the intended local display wording under governed naming; no raw Name has been normalised in this review.
4. What Product meaning/scope is intended? The Name supplies no deployment, component allocation, service ownership or domain-decision authority; none is proposed by inference.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Increment 2A granularity/adjudication; 2B recognition and metadata; AX-04/14/17/18.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-005"></a>

## CR-005 — Add Health Information Exchange Service Product

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the single architectural instance `[Product] Health Information Exchange Service`, proposed native `Product`. No additional Harmonia classification is inferred.

### Detected Evidence

**Observed Evidence:** [submission.xml:87219](detection/submission.xml#L87219) — `id-d84d8e1f988c4bc998b18834aabfcd74`; XPath `/*/*[4]/*[1169]`. New semantic element, absent from the 1.0.8 baseline. Native Name: `[Product] Health Information Exchange Service`. The record has only its Name: no Properties or Documentation. Recognition: Qualifier repeats native type; no additional Harmonia subtype recognised. See [element observations](detection/elements.json) (`A5` mapping in [inventory](detection/observations.md)). Absent canonical identity and derived metadata are evidence only, not a defect CR or allocation instruction.

### Architectural Questions / Adjudication

1. Accept `[Product] Health Information Exchange Service` as a new Harmonia Product with native type `Product`? Confirm whether it is a new architectural instance or identify an established governed binding if reuse was intended.
2. What bounded responsibility/definition and architectural standing are authorised? The submission contains no Documentation or standing assertion.
3. `[Product]` repeats the native type and establishes no extra Harmonia subtype. Confirm the intended local display wording under governed naming; no raw Name has been normalised in this review.
4. What Product meaning/scope is intended? The Name supplies no deployment, component allocation, service ownership or domain-decision authority; none is proposed by inference.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Increment 2A granularity/adjudication; 2B recognition and metadata; AX-04/14/17/18.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-006"></a>

## CR-006 — Add Identifier Create Business Service

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the single architectural instance `[Business Service] Identifier Create`, proposed native `BusinessService`. No additional Harmonia classification is inferred.

### Detected Evidence

**Observed Evidence:** [submission.xml:62338](detection/submission.xml#L62338) — `id-fddda503b8f0438e995465cd93204326`; XPath `/*/*[4]/*[843]`. New semantic element, absent from the 1.0.8 baseline. Native Name: `[Business Service] Identifier Create`. The record has only its Name: no Properties or Documentation. Recognition: Qualifier repeats native type; no additional Harmonia subtype recognised. See [element observations](detection/elements.json) (`A6` mapping in [inventory](detection/observations.md)). Absent canonical identity and derived metadata are evidence only, not a defect CR or allocation instruction.

### Architectural Questions / Adjudication

1. Accept `[Business Service] Identifier Create` as a new Harmonia Business Service with native type `BusinessService`? Confirm whether it is a new architectural instance or identify an established governed binding if reuse was intended.
2. What bounded responsibility/definition and architectural standing are authorised? The submission contains no Documentation or standing assertion.
3. `[Business Service]` repeats the native type and establishes no extra Harmonia subtype. Confirm the intended local display wording under governed naming; no raw Name has been normalised in this review.
4. Confirm the responsibility boundary, including whether identity verification acts on source-attributed information without assuming master-person matching, golden-record or autonomous merge authority. Any different domain authority requires explicit architecture review under AX-18.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Increment 2A granularity/adjudication; 2B recognition and metadata; AX-04/14/17/18.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-007"></a>

## CR-007 — Add Identifier Update Business Service

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the single architectural instance `[Business Service] Identifier Update`, proposed native `BusinessService`. No additional Harmonia classification is inferred.

### Detected Evidence

**Observed Evidence:** [submission.xml:62341](detection/submission.xml#L62341) — `id-0088d61ccfcf4962b3aab8bc3b96021d`; XPath `/*/*[4]/*[844]`. New semantic element, absent from the 1.0.8 baseline. Native Name: `[Business Service] Identifier Update`. The record has only its Name: no Properties or Documentation. Recognition: Qualifier repeats native type; no additional Harmonia subtype recognised. See [element observations](detection/elements.json) (`A7` mapping in [inventory](detection/observations.md)). Absent canonical identity and derived metadata are evidence only, not a defect CR or allocation instruction.

### Architectural Questions / Adjudication

1. Accept `[Business Service] Identifier Update` as a new Harmonia Business Service with native type `BusinessService`? Confirm whether it is a new architectural instance or identify an established governed binding if reuse was intended.
2. What bounded responsibility/definition and architectural standing are authorised? The submission contains no Documentation or standing assertion.
3. `[Business Service]` repeats the native type and establishes no extra Harmonia subtype. Confirm the intended local display wording under governed naming; no raw Name has been normalised in this review.
4. Confirm the responsibility boundary, including whether identity verification acts on source-attributed information without assuming master-person matching, golden-record or autonomous merge authority. Any different domain authority requires explicit architecture review under AX-18.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Increment 2A granularity/adjudication; 2B recognition and metadata; AX-04/14/17/18.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-008"></a>

## CR-008 — Add Identifier Verification Business Process

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the single architectural instance `[Business Process] Identifier Verification`, proposed native `BusinessProcess`. No additional Harmonia classification is inferred.

### Detected Evidence

**Observed Evidence:** [submission.xml:62344](detection/submission.xml#L62344) — `id-c645a859ac2c4447ad5ae850cf384bf3`; XPath `/*/*[4]/*[845]`. New semantic element, absent from the 1.0.8 baseline. Native Name: `[Business Process] Identifier Verification`. The record has only its Name: no Properties or Documentation. Recognition: Qualifier repeats native type; no additional Harmonia subtype recognised. See [element observations](detection/elements.json) (`A8` mapping in [inventory](detection/observations.md)). Absent canonical identity and derived metadata are evidence only, not a defect CR or allocation instruction.

### Architectural Questions / Adjudication

1. Accept `[Business Process] Identifier Verification` as a new Harmonia Business Process with native type `BusinessProcess`? Confirm whether it is a new architectural instance or identify an established governed binding if reuse was intended.
2. What bounded responsibility/definition and architectural standing are authorised? The submission contains no Documentation or standing assertion.
3. `[Business Process]` repeats the native type and establishes no extra Harmonia subtype. Confirm the intended local display wording under governed naming; no raw Name has been normalised in this review.
4. Confirm the responsibility boundary, including whether identity verification acts on source-attributed information without assuming master-person matching, golden-record or autonomous merge authority. Any different domain authority requires explicit architecture review under AX-18.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Increment 2A granularity/adjudication; 2B recognition and metadata; AX-04/14/17/18.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-009"></a>

## CR-009 — Add Verify Identity Source Business Service

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the single architectural instance `[Business Service] Verify Identity Source`, proposed native `BusinessService`. No additional Harmonia classification is inferred.

### Detected Evidence

**Observed Evidence:** [submission.xml:62347](detection/submission.xml#L62347) — `id-9bfedbfd5b404694a2224c039fa5d4c4`; XPath `/*/*[4]/*[846]`. New semantic element, absent from the 1.0.8 baseline. Native Name: `[Business Service] Verify Identity Source`. The record has only its Name: no Properties or Documentation. Recognition: Qualifier repeats native type; no additional Harmonia subtype recognised. See [element observations](detection/elements.json) (`A9` mapping in [inventory](detection/observations.md)). Absent canonical identity and derived metadata are evidence only, not a defect CR or allocation instruction.

### Architectural Questions / Adjudication

1. Accept `[Business Service] Verify Identity Source` as a new Harmonia Business Service with native type `BusinessService`? Confirm whether it is a new architectural instance or identify an established governed binding if reuse was intended.
2. What bounded responsibility/definition and architectural standing are authorised? The submission contains no Documentation or standing assertion.
3. `[Business Service]` repeats the native type and establishes no extra Harmonia subtype. Confirm the intended local display wording under governed naming; no raw Name has been normalised in this review.
4. Confirm the responsibility boundary, including whether identity verification acts on source-attributed information without assuming master-person matching, golden-record or autonomous merge authority. Any different domain authority requires explicit architecture review under AX-18.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Increment 2A granularity/adjudication; 2B recognition and metadata; AX-04/14/17/18.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-010"></a>

## CR-010 — Add Composition to Cross-Authority Identifier Correlation

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add only the native Composition from `Person Identity Administration` to `Cross-Authority Identifier Correlation`, preserving whole/source → part/target direction. This CR reviews the explicit edge and its bounded whole/part meaning; the CT3-framing interpretation is separately adjudicable.

### Detected Evidence

**Observed Evidence:** [submission.xml:95230](detection/submission.xml#L95230) — `id-73ecd7a39407404ba8e2bb663efbdc19`; XPath `/*/*[5]/*[1]`. Source: `Person Identity Administration` (Capability, Exchange `id-29e0bf94882559a7b6ed9d092303a560`, existing canonicalId `6c3de7750ef1436cad6a9533731782b6`). Target: `Cross-Authority Identifier Correlation` (Capability, Exchange `id-d9272acdc675539cb441d2db9c5ad6e6`, existing canonicalId `6d74d143c7454fa0bf30743ddf7ba094`). No Name, Properties or Documentation. It has one `Relations` root reference and one V1 visual appearance. No baseline duplicate against resolved governed endpoints. See [relationship observations](detection/relationships.json); endpoint resolution is independent of folder placement.

### Architectural Questions / Adjudication

1. Accept the submitted native Composition from `[CT3] Person Identity Administration` to `Cross-Authority Identifier Correlation` for this instance, with the stated endpoints and direction?
2. Confirm native endpoint legality, intended whole/part meaning and relationship standing. The unresolved CT3/Feature profile is not resolved by native legality or deliberate authorship.
3. The separate framing CR may receive a different disposition. No universal CT3 → Composition → Feature rule follows from this instance.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B semantic containment and unresolved CT3/Feature matter; mapping §5; 2A separate interpretations; AX-14/17.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

Informationally related (not automatic prerequisites): [CR-016](#cr-016), [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-011"></a>

## CR-011 — Add Composition to Governed Identity Correction

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add only the native Composition from `Person Identity Administration` to `Governed Identity Correction`, preserving whole/source → part/target direction. This CR reviews the explicit edge and its bounded whole/part meaning; the CT3-framing interpretation is separately adjudicable.

### Detected Evidence

**Observed Evidence:** [submission.xml:95231](detection/submission.xml#L95231) — `id-36b2aa8ad58149fdb20d44c1c16dd64c`; XPath `/*/*[5]/*[2]`. Source: `Person Identity Administration` (Capability, Exchange `id-29e0bf94882559a7b6ed9d092303a560`, existing canonicalId `6c3de7750ef1436cad6a9533731782b6`). Target: `Governed Identity Correction` (Capability, Exchange `id-6f0bfe5e814c5734a7c530f2b7515666`, existing canonicalId `f7ab7a24e8234129b2ab02e9abd4fba2`). No Name, Properties or Documentation. It has one `Relations` root reference and one V1 visual appearance. No baseline duplicate against resolved governed endpoints. See [relationship observations](detection/relationships.json); endpoint resolution is independent of folder placement.

### Architectural Questions / Adjudication

1. Accept the submitted native Composition from `[CT3] Person Identity Administration` to `Governed Identity Correction` for this instance, with the stated endpoints and direction?
2. Confirm native endpoint legality, intended whole/part meaning and relationship standing. The unresolved CT3/Feature profile is not resolved by native legality or deliberate authorship.
3. The separate framing CR may receive a different disposition. No universal CT3 → Composition → Feature rule follows from this instance.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B semantic containment and unresolved CT3/Feature matter; mapping §5; 2A separate interpretations; AX-14/17.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

Informationally related (not automatic prerequisites): [CR-017](#cr-017), [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-012"></a>

## CR-012 — Add Composition to Identity Source & Correction Lineage

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add only the native Composition from `Person Identity Administration` to `Identity Source & Correction Lineage`, preserving whole/source → part/target direction. This CR reviews the explicit edge and its bounded whole/part meaning; the CT3-framing interpretation is separately adjudicable.

### Detected Evidence

**Observed Evidence:** [submission.xml:95232](detection/submission.xml#L95232) — `id-3c3d723683c147a4a902b4d35a2af018`; XPath `/*/*[5]/*[3]`. Source: `Person Identity Administration` (Capability, Exchange `id-29e0bf94882559a7b6ed9d092303a560`, existing canonicalId `6c3de7750ef1436cad6a9533731782b6`). Target: `Identity Source & Correction Lineage` (Capability, Exchange `id-bdf18b5e580b5b85a34357a3b21f36db`, existing canonicalId `d72134e75001449a9e7ffae53f61458f`). No Name, Properties or Documentation. It has one `Relations` root reference and one V1 visual appearance. No baseline duplicate against resolved governed endpoints. See [relationship observations](detection/relationships.json); endpoint resolution is independent of folder placement.

### Architectural Questions / Adjudication

1. Accept the submitted native Composition from `[CT3] Person Identity Administration` to `Identity Source & Correction Lineage` for this instance, with the stated endpoints and direction?
2. Confirm native endpoint legality, intended whole/part meaning and relationship standing. The unresolved CT3/Feature profile is not resolved by native legality or deliberate authorship.
3. The separate framing CR may receive a different disposition. No universal CT3 → Composition → Feature rule follows from this instance.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B semantic containment and unresolved CT3/Feature matter; mapping §5; 2A separate interpretations; AX-14/17.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

Informationally related (not automatic prerequisites): [CR-018](#cr-018), [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-013"></a>

## CR-013 — Add Composition to Identifier Resolution

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add only the native Composition from `Person Identity Administration` to `Identifier Resolution`, preserving whole/source → part/target direction. This CR reviews the explicit edge and its bounded whole/part meaning; the CT3-framing interpretation is separately adjudicable.

### Detected Evidence

**Observed Evidence:** [submission.xml:95233](detection/submission.xml#L95233) — `id-daf437bb32114daa8a6b9462cb9b4fb2`; XPath `/*/*[5]/*[4]`. Source: `Person Identity Administration` (Capability, Exchange `id-29e0bf94882559a7b6ed9d092303a560`, existing canonicalId `6c3de7750ef1436cad6a9533731782b6`). Target: `Identifier Resolution` (Capability, Exchange `id-f1e1928516765880955f638a7fec8c35`, existing canonicalId `ae034169e1394ccdb3a2ce9651eaf305`). No Name, Properties or Documentation. It has one `Relations` root reference and one V1 visual appearance. No baseline duplicate against resolved governed endpoints. See [relationship observations](detection/relationships.json); endpoint resolution is independent of folder placement.

### Architectural Questions / Adjudication

1. Accept the submitted native Composition from `[CT3] Person Identity Administration` to `Identifier Resolution` for this instance, with the stated endpoints and direction?
2. Confirm native endpoint legality, intended whole/part meaning and relationship standing. The unresolved CT3/Feature profile is not resolved by native legality or deliberate authorship.
3. The separate framing CR may receive a different disposition. No universal CT3 → Composition → Feature rule follows from this instance.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B semantic containment and unresolved CT3/Feature matter; mapping §5; 2A separate interpretations; AX-14/17.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

Informationally related (not automatic prerequisites): [CR-019](#cr-019), [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-014"></a>

## CR-014 — Add Composition to Identity Alias Association

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add only the native Composition from `Person Identity Administration` to `Identity Alias Association`, preserving whole/source → part/target direction. This CR reviews the explicit edge and its bounded whole/part meaning; the CT3-framing interpretation is separately adjudicable.

### Detected Evidence

**Observed Evidence:** [submission.xml:95234](detection/submission.xml#L95234) — `id-f9b270950f0241108b68f1c3daa50019`; XPath `/*/*[5]/*[5]`. Source: `Person Identity Administration` (Capability, Exchange `id-29e0bf94882559a7b6ed9d092303a560`, existing canonicalId `6c3de7750ef1436cad6a9533731782b6`). Target: `Identity Alias Association` (Capability, Exchange `id-28d7815137a05a2aab6d1684011dbbde`, existing canonicalId `97721dbd017e4c2daa73e1e87a8499b9`). No Name, Properties or Documentation. It has one `Relations` root reference and one V1 visual appearance. No baseline duplicate against resolved governed endpoints. See [relationship observations](detection/relationships.json); endpoint resolution is independent of folder placement.

### Architectural Questions / Adjudication

1. Accept the submitted native Composition from `[CT3] Person Identity Administration` to `Identity Alias Association` for this instance, with the stated endpoints and direction?
2. Confirm native endpoint legality, intended whole/part meaning and relationship standing. The unresolved CT3/Feature profile is not resolved by native legality or deliberate authorship.
3. The separate framing CR may receive a different disposition. No universal CT3 → Composition → Feature rule follows from this instance.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B semantic containment and unresolved CT3/Feature matter; mapping §5; 2A separate interpretations; AX-14/17.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

Informationally related (not automatic prerequisites): [CR-020](#cr-020), [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-015"></a>

## CR-015 — Add Composition to Identifier Capture

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add only the native Composition from `Person Identity Administration` to `[Feature] Identifier Capture`, preserving whole/source → part/target direction. This CR reviews the explicit edge and its bounded whole/part meaning; the CT3-framing interpretation is separately adjudicable.

### Detected Evidence

**Observed Evidence:** [submission.xml:95235](detection/submission.xml#L95235) — `id-bff76286f8994d20aa1b8aef166acebd`; XPath `/*/*[5]/*[6]`. Source: `Person Identity Administration` (Capability, Exchange `id-29e0bf94882559a7b6ed9d092303a560`, existing canonicalId `6c3de7750ef1436cad6a9533731782b6`). Target: `[Feature] Identifier Capture` (Capability, Exchange `id-727a264026584f80bc3304ff8383dc92`, governed identity unresolved; submitted record only). No Name, Properties or Documentation. It has one `Relations` root reference and one V1 visual appearance. UNRESOLVED: new endpoint has no governed architectural identity. See [relationship observations](detection/relationships.json); endpoint resolution is independent of folder placement.

### Architectural Questions / Adjudication

1. Accept the submitted native Composition from `[CT3] Person Identity Administration` to `[Feature] Identifier Capture` for this instance, with the stated endpoints and direction?
2. Confirm native endpoint legality, intended whole/part meaning and relationship standing. The unresolved CT3/Feature profile is not resolved by native legality or deliberate authorship.
3. The separate framing CR may receive a different disposition. No universal CT3 → Composition → Feature rule follows from this instance.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B semantic containment and unresolved CT3/Feature matter; mapping §5; 2A separate interpretations; AX-14/17.

### Related Change Requests

- Required resulting state from [CR-001](#cr-001): Identifier Capture exists as the resolved governed architectural endpoint for submitted A1, with its accepted native type/classification and evidenced reconciliation; identity has not been allocated here.

Informationally related (not automatic prerequisites): [CR-021](#cr-021), [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-016"></a>

## CR-016 — Interpret CT3 framing for Cross-Authority Identifier Correlation

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** For this instance only, determine whether the submitted Composition encodes CT3 framing/context of `Cross-Authority Identifier Correlation` by Person Identity Administration, and what bounded standing/context effect is authorised. The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. This is a candidate Harmonia interpretation, not another semantic-edge addition or a universal profile decision.

### Detected Evidence

**Observed Evidence:** R1, [submission.xml:95230](detection/submission.xml#L95230) — `id-73ecd7a39407404ba8e2bb663efbdc19`; XPath `/*/*[5]/*[1]`. The XML contains a bare native Composition only; it contains no assertion that establishes a universal framing rule. The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. See [framing evidence and RADS locators](detection/observations.md#existing-feature-context), [relationships](detection/relationships.json).

### Architectural Questions / Adjudication

1. Does this Composition, if accepted and reconciled, encode the governed CT3 framing/context for `Cross-Authority Identifier Correlation` for this instance?
2. State the exact semantic/naming-context effect and standing. Existing Proposed placement and, for Lineage, Proposed architectural standing remain unchanged unless explicitly adjudicated; acceptance of an edge alone does not approve those matters.
3. If the native edge is accepted without this extra interpretation, identify any unresolved context that must remain blocked. No general metamodel rule is submitted or established here.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B bounded unresolved CT3/Feature matter and existing parent_key/owner_key context; mapping §5; 2A; AX-17.

### Related Change Requests

- Required resulting state from [CR-010](#cr-010): The exact native R1 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient.

Informationally related (not automatic prerequisites): [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-017"></a>

## CR-017 — Interpret CT3 framing for Governed Identity Correction

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** For this instance only, determine whether the submitted Composition encodes CT3 framing/context of `Governed Identity Correction` by Person Identity Administration, and what bounded standing/context effect is authorised. The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. This is a candidate Harmonia interpretation, not another semantic-edge addition or a universal profile decision.

### Detected Evidence

**Observed Evidence:** R2, [submission.xml:95231](detection/submission.xml#L95231) — `id-36b2aa8ad58149fdb20d44c1c16dd64c`; XPath `/*/*[5]/*[2]`. The XML contains a bare native Composition only; it contains no assertion that establishes a universal framing rule. The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. See [framing evidence and RADS locators](detection/observations.md#existing-feature-context), [relationships](detection/relationships.json).

### Architectural Questions / Adjudication

1. Does this Composition, if accepted and reconciled, encode the governed CT3 framing/context for `Governed Identity Correction` for this instance?
2. State the exact semantic/naming-context effect and standing. Existing Proposed placement and, for Lineage, Proposed architectural standing remain unchanged unless explicitly adjudicated; acceptance of an edge alone does not approve those matters.
3. If the native edge is accepted without this extra interpretation, identify any unresolved context that must remain blocked. No general metamodel rule is submitted or established here.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B bounded unresolved CT3/Feature matter and existing parent_key/owner_key context; mapping §5; 2A; AX-17.

### Related Change Requests

- Required resulting state from [CR-011](#cr-011): The exact native R2 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient.

Informationally related (not automatic prerequisites): [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-018"></a>

## CR-018 — Interpret CT3 framing for Identity Source & Correction Lineage

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** For this instance only, determine whether the submitted Composition encodes CT3 framing/context of `Identity Source & Correction Lineage` by Person Identity Administration, and what bounded standing/context effect is authorised. The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. This is a candidate Harmonia interpretation, not another semantic-edge addition or a universal profile decision.

### Detected Evidence

**Observed Evidence:** R3, [submission.xml:95232](detection/submission.xml#L95232) — `id-3c3d723683c147a4a902b4d35a2af018`; XPath `/*/*[5]/*[3]`. The XML contains a bare native Composition only; it contains no assertion that establishes a universal framing rule. The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. See [framing evidence and RADS locators](detection/observations.md#existing-feature-context), [relationships](detection/relationships.json).

### Architectural Questions / Adjudication

1. Does this Composition, if accepted and reconciled, encode the governed CT3 framing/context for `Identity Source & Correction Lineage` for this instance?
2. State the exact semantic/naming-context effect and standing. Existing Proposed placement and, for Lineage, Proposed architectural standing remain unchanged unless explicitly adjudicated; acceptance of an edge alone does not approve those matters.
3. If the native edge is accepted without this extra interpretation, identify any unresolved context that must remain blocked. No general metamodel rule is submitted or established here.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B bounded unresolved CT3/Feature matter and existing parent_key/owner_key context; mapping §5; 2A; AX-17.

### Related Change Requests

- Required resulting state from [CR-012](#cr-012): The exact native R3 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient.

Informationally related (not automatic prerequisites): [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-019"></a>

## CR-019 — Interpret CT3 framing for Identifier Resolution

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** For this instance only, determine whether the submitted Composition encodes CT3 framing/context of `Identifier Resolution` by Person Identity Administration, and what bounded standing/context effect is authorised. The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. This is a candidate Harmonia interpretation, not another semantic-edge addition or a universal profile decision.

### Detected Evidence

**Observed Evidence:** R4, [submission.xml:95233](detection/submission.xml#L95233) — `id-daf437bb32114daa8a6b9462cb9b4fb2`; XPath `/*/*[5]/*[4]`. The XML contains a bare native Composition only; it contains no assertion that establishes a universal framing rule. The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. See [framing evidence and RADS locators](detection/observations.md#existing-feature-context), [relationships](detection/relationships.json).

### Architectural Questions / Adjudication

1. Does this Composition, if accepted and reconciled, encode the governed CT3 framing/context for `Identifier Resolution` for this instance?
2. State the exact semantic/naming-context effect and standing. Existing Proposed placement and, for Lineage, Proposed architectural standing remain unchanged unless explicitly adjudicated; acceptance of an edge alone does not approve those matters.
3. If the native edge is accepted without this extra interpretation, identify any unresolved context that must remain blocked. No general metamodel rule is submitted or established here.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B bounded unresolved CT3/Feature matter and existing parent_key/owner_key context; mapping §5; 2A; AX-17.

### Related Change Requests

- Required resulting state from [CR-013](#cr-013): The exact native R4 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient.

Informationally related (not automatic prerequisites): [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-020"></a>

## CR-020 — Interpret CT3 framing for Identity Alias Association

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** For this instance only, determine whether the submitted Composition encodes CT3 framing/context of `Identity Alias Association` by Person Identity Administration, and what bounded standing/context effect is authorised. The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. This is a candidate Harmonia interpretation, not another semantic-edge addition or a universal profile decision.

### Detected Evidence

**Observed Evidence:** R5, [submission.xml:95234](detection/submission.xml#L95234) — `id-f9b270950f0241108b68f1c3daa50019`; XPath `/*/*[5]/*[5]`. The XML contains a bare native Composition only; it contains no assertion that establishes a universal framing rule. The current RADS Feature record already retains Person Identity Administration as source-qualified responsibility/framing context with **Proposed placement**. This CR does not recreate that context or promote its standing. See [framing evidence and RADS locators](detection/observations.md#existing-feature-context), [relationships](detection/relationships.json).

### Architectural Questions / Adjudication

1. Does this Composition, if accepted and reconciled, encode the governed CT3 framing/context for `Identity Alias Association` for this instance?
2. State the exact semantic/naming-context effect and standing. Existing Proposed placement and, for Lineage, Proposed architectural standing remain unchanged unless explicitly adjudicated; acceptance of an edge alone does not approve those matters.
3. If the native edge is accepted without this extra interpretation, identify any unresolved context that must remain blocked. No general metamodel rule is submitted or established here.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B bounded unresolved CT3/Feature matter and existing parent_key/owner_key context; mapping §5; 2A; AX-17.

### Related Change Requests

- Required resulting state from [CR-014](#cr-014): The exact native R5 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient.

Informationally related (not automatic prerequisites): [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-021"></a>

## CR-021 — Interpret CT3 framing for Identifier Capture

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** For this instance only, determine whether the submitted Composition encodes CT3 framing/context of `[Feature] Identifier Capture` by Person Identity Administration, and what bounded standing/context effect is authorised. There is no established source record for A1; its CT3 context is a candidate requiring explicit adjudication. This is a candidate Harmonia interpretation, not another semantic-edge addition or a universal profile decision.

### Detected Evidence

**Observed Evidence:** R6, [submission.xml:95235](detection/submission.xml#L95235) — `id-bff76286f8994d20aa1b8aef166acebd`; XPath `/*/*[5]/*[6]`. The XML contains a bare native Composition only; it contains no assertion that establishes a universal framing rule. There is no established source record for A1; its CT3 context is a candidate requiring explicit adjudication. See [framing evidence and RADS locators](detection/observations.md#existing-feature-context), [relationships](detection/relationships.json).

### Architectural Questions / Adjudication

1. Does this Composition, if accepted and reconciled, encode the governed CT3 framing/context for `[Feature] Identifier Capture` for this instance?
2. State the exact semantic/naming-context effect and standing. Existing Proposed placement and, for Lineage, Proposed architectural standing remain unchanged unless explicitly adjudicated; acceptance of an edge alone does not approve those matters.
3. If the native edge is accepted without this extra interpretation, identify any unresolved context that must remain blocked. No general metamodel rule is submitted or established here.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B bounded unresolved CT3/Feature matter and existing parent_key/owner_key context; mapping §5; 2A; AX-17.

### Related Change Requests

- Required resulting state from [CR-015](#cr-015): The exact native R6 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient.

Informationally related (not automatic prerequisites): [CR-033](#cr-033).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-022"></a>

## CR-022 — Add Identifier Create Realization

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the explicit native Realization from `[Business Service] Identifier Create` to `[Feature] Identifier Capture`, preserving direction and bounded realization meaning. This alone establishes no Harmonia logical Feature containment/context.

### Detected Evidence

**Observed Evidence:** [submission.xml:95236](detection/submission.xml#L95236) — `id-e84f2e9df66a48d795d604d75afc72cb`; XPath `/*/*[5]/*[7]`. Source: `[Business Service] Identifier Create` (BusinessService, Exchange `id-fddda503b8f0438e995465cd93204326`, governed identity unresolved; submitted record only). Target: `[Feature] Identifier Capture` (Capability, Exchange `id-727a264026584f80bc3304ff8383dc92`, governed identity unresolved; submitted record only). No Name, Properties or Documentation. UNRESOLVED: new endpoint has no governed architectural identity. See [relationship evidence](detection/relationships.json).

### Architectural Questions / Adjudication

1. Accept the submitted Realization from `[Business Service] Identifier Create` to `[Feature] Identifier Capture`?
2. Confirm native legality, intended realization responsibility and standing. Does the stated scope respect the target Feature boundary? Additional logical context is a separate question.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Mapping §5 behaviour → Feature Realization; 2B explicit relationships and unresolved logical context; 2A; AX-14/17/18.

### Related Change Requests

- Required resulting state from [CR-006](#cr-006): `[Business Service] Identifier Create` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing.
- Required resulting state from [CR-001](#cr-001): `[Feature] Identifier Capture` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing.

Informationally related (not automatic prerequisites): [CR-023](#cr-023).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-023"></a>

## CR-023 — Interpret Feature context for Identifier Create

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** For this instance only, consider treating R7 Realization as establishing Harmonia logical Feature context of `[Business Service] Identifier Create` in `[Feature] Identifier Capture`. This additional interpretation is a question, not observed XML containment. Preserve the native Realization; do not substitute or add Composition.

### Detected Evidence

**Observed Evidence:** R7, [submission.xml:95236](detection/submission.xml#L95236) — `id-e84f2e9df66a48d795d604d75afc72cb`; XPath `/*/*[5]/*[7]`. Source: `[Business Service] Identifier Create` (BusinessService, Exchange `id-fddda503b8f0438e995465cd93204326`, governed identity unresolved; submitted record only). Target: `[Feature] Identifier Capture` (Capability, Exchange `id-727a264026584f80bc3304ff8383dc92`, governed identity unresolved; submitted record only). The View presents these endpoints but contains no independent semantic containment assertion. The native Realization and View placement do not themselves answer the Harmonia context question.

### Architectural Questions / Adjudication

1. Does this Realization also establish Harmonia logical Feature context for `[Business Service] Identifier Create` in `[Feature] Identifier Capture`?
2. If so, specify the bounded context, standing and any naming consequences for this instance. If not, retain the native Realization without invented context. No universal Realization inference or Composition is authorised.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B unresolved Realization → Feature logical-context matter; 2A separate interpretations; AX-14/17.

### Related Change Requests

- Required resulting state from [CR-022](#cr-022): The native R7 Realization has been reconciled and validated with the exact resolved governed source/target and authorised standing; an Accept disposition by itself supplies no resulting state.

Informationally related (not automatic prerequisites): [CR-035](#cr-035).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-024"></a>

## CR-024 — Add Identifier Update Realization

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the explicit native Realization from `[Business Service] Identifier Update` to `[Feature] Identifier Capture`, preserving direction and bounded realization meaning. This alone establishes no Harmonia logical Feature containment/context.

### Detected Evidence

**Observed Evidence:** [submission.xml:95237](detection/submission.xml#L95237) — `id-69eb864f56df4d928f8abc3f7ab4e834`; XPath `/*/*[5]/*[8]`. Source: `[Business Service] Identifier Update` (BusinessService, Exchange `id-0088d61ccfcf4962b3aab8bc3b96021d`, governed identity unresolved; submitted record only). Target: `[Feature] Identifier Capture` (Capability, Exchange `id-727a264026584f80bc3304ff8383dc92`, governed identity unresolved; submitted record only). No Name, Properties or Documentation. UNRESOLVED: new endpoint has no governed architectural identity. See [relationship evidence](detection/relationships.json).

### Architectural Questions / Adjudication

1. Accept the submitted Realization from `[Business Service] Identifier Update` to `[Feature] Identifier Capture`?
2. Confirm native legality, intended realization responsibility and standing. Does the stated scope respect the target Feature boundary? Additional logical context is a separate question.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Mapping §5 behaviour → Feature Realization; 2B explicit relationships and unresolved logical context; 2A; AX-14/17/18.

### Related Change Requests

- Required resulting state from [CR-007](#cr-007): `[Business Service] Identifier Update` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing.
- Required resulting state from [CR-001](#cr-001): `[Feature] Identifier Capture` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing.

Informationally related (not automatic prerequisites): [CR-025](#cr-025).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-025"></a>

## CR-025 — Interpret Feature context for Identifier Update

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** For this instance only, consider treating R8 Realization as establishing Harmonia logical Feature context of `[Business Service] Identifier Update` in `[Feature] Identifier Capture`. This additional interpretation is a question, not observed XML containment. Preserve the native Realization; do not substitute or add Composition.

### Detected Evidence

**Observed Evidence:** R8, [submission.xml:95237](detection/submission.xml#L95237) — `id-69eb864f56df4d928f8abc3f7ab4e834`; XPath `/*/*[5]/*[8]`. Source: `[Business Service] Identifier Update` (BusinessService, Exchange `id-0088d61ccfcf4962b3aab8bc3b96021d`, governed identity unresolved; submitted record only). Target: `[Feature] Identifier Capture` (Capability, Exchange `id-727a264026584f80bc3304ff8383dc92`, governed identity unresolved; submitted record only). The View presents these endpoints but contains no independent semantic containment assertion. The native Realization and View placement do not themselves answer the Harmonia context question.

### Architectural Questions / Adjudication

1. Does this Realization also establish Harmonia logical Feature context for `[Business Service] Identifier Update` in `[Feature] Identifier Capture`?
2. If so, specify the bounded context, standing and any naming consequences for this instance. If not, retain the native Realization without invented context. No universal Realization inference or Composition is authorised.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B unresolved Realization → Feature logical-context matter; 2A separate interpretations; AX-14/17.

### Related Change Requests

- Required resulting state from [CR-024](#cr-024): The native R8 Realization has been reconciled and validated with the exact resolved governed source/target and authorised standing; an Accept disposition by itself supplies no resulting state.

Informationally related (not automatic prerequisites): [CR-035](#cr-035).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-026"></a>

## CR-026 — Add Identifier Verification Realization

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the explicit native Realization from `[Business Process] Identifier Verification` to `[Feature] Identifier Capture`, preserving direction and bounded realization meaning. This alone establishes no Harmonia logical Feature containment/context.

### Detected Evidence

**Observed Evidence:** [submission.xml:95238](detection/submission.xml#L95238) — `id-c0f84a8e4c8d496580a68cac4823c58c`; XPath `/*/*[5]/*[9]`. Source: `[Business Process] Identifier Verification` (BusinessProcess, Exchange `id-c645a859ac2c4447ad5ae850cf384bf3`, governed identity unresolved; submitted record only). Target: `[Feature] Identifier Capture` (Capability, Exchange `id-727a264026584f80bc3304ff8383dc92`, governed identity unresolved; submitted record only). No Name, Properties or Documentation. UNRESOLVED: new endpoint has no governed architectural identity. See [relationship evidence](detection/relationships.json).

### Architectural Questions / Adjudication

1. Accept the submitted Realization from `[Business Process] Identifier Verification` to `[Feature] Identifier Capture`?
2. Confirm native legality, intended realization responsibility and standing. Does the stated scope respect the target Feature boundary? Additional logical context is a separate question.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Mapping §5 behaviour → Feature Realization; 2B explicit relationships and unresolved logical context; 2A; AX-14/17/18.

### Related Change Requests

- Required resulting state from [CR-008](#cr-008): `[Business Process] Identifier Verification` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing.
- Required resulting state from [CR-001](#cr-001): `[Feature] Identifier Capture` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing.

Informationally related (not automatic prerequisites): [CR-027](#cr-027).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-027"></a>

## CR-027 — Interpret Feature context for Identifier Verification

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** For this instance only, consider treating R9 Realization as establishing Harmonia logical Feature context of `[Business Process] Identifier Verification` in `[Feature] Identifier Capture`. This additional interpretation is a question, not observed XML containment. Preserve the native Realization; do not substitute or add Composition.

### Detected Evidence

**Observed Evidence:** R9, [submission.xml:95238](detection/submission.xml#L95238) — `id-c0f84a8e4c8d496580a68cac4823c58c`; XPath `/*/*[5]/*[9]`. Source: `[Business Process] Identifier Verification` (BusinessProcess, Exchange `id-c645a859ac2c4447ad5ae850cf384bf3`, governed identity unresolved; submitted record only). Target: `[Feature] Identifier Capture` (Capability, Exchange `id-727a264026584f80bc3304ff8383dc92`, governed identity unresolved; submitted record only). The View presents these endpoints but contains no independent semantic containment assertion. The native Realization and View placement do not themselves answer the Harmonia context question.

### Architectural Questions / Adjudication

1. Does this Realization also establish Harmonia logical Feature context for `[Business Process] Identifier Verification` in `[Feature] Identifier Capture`?
2. If so, specify the bounded context, standing and any naming consequences for this instance. If not, retain the native Realization without invented context. No universal Realization inference or Composition is authorised.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B unresolved Realization → Feature logical-context matter; 2A separate interpretations; AX-14/17.

### Related Change Requests

- Required resulting state from [CR-026](#cr-026): The native R9 Realization has been reconciled and validated with the exact resolved governed source/target and authorised standing; an Accept disposition by itself supplies no resulting state.

Informationally related (not automatic prerequisites): [CR-035](#cr-035).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-028"></a>

## CR-028 — Add Verify Identity Source Realization

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add the explicit native Realization from `[Business Service] Verify Identity Source` to `Identity Source & Correction Lineage`, preserving direction and bounded realization meaning. This alone establishes no Harmonia logical Feature containment/context.

### Detected Evidence

**Observed Evidence:** [submission.xml:95241](detection/submission.xml#L95241) — `id-37f6913198e2434db5309c1c8e36fbc0`; XPath `/*/*[5]/*[12]`. Source: `[Business Service] Verify Identity Source` (BusinessService, Exchange `id-9bfedbfd5b404694a2224c039fa5d4c4`, governed identity unresolved; submitted record only). Target: `Identity Source & Correction Lineage` (Capability, Exchange `id-bdf18b5e580b5b85a34357a3b21f36db`, existing canonicalId `d72134e75001449a9e7ffae53f61458f`). No Name, Properties or Documentation. UNRESOLVED: new endpoint has no governed architectural identity. See [relationship evidence](detection/relationships.json). The target Lineage has existing Proposed architectural and placement standing, which this edge does not promote.

### Architectural Questions / Adjudication

1. Accept the submitted Realization from `[Business Service] Verify Identity Source` to `Identity Source & Correction Lineage`?
2. Confirm native legality, intended realization responsibility and standing. Does the stated scope respect the target Feature boundary? Additional logical context is a separate question.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Mapping §5 behaviour → Feature Realization; 2B explicit relationships and unresolved logical context; 2A; AX-14/17/18.

### Related Change Requests

- Required resulting state from [CR-009](#cr-009): `[Business Service] Verify Identity Source` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing.

Informationally related (not automatic prerequisites): [CR-029](#cr-029).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-029"></a>

## CR-029 — Interpret Feature context for Verify Identity Source

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** For this instance only, consider treating R12 Realization as establishing Harmonia logical Feature context of `[Business Service] Verify Identity Source` in `Identity Source & Correction Lineage`. This additional interpretation is a question, not observed XML containment. Preserve the native Realization; do not substitute or add Composition.

### Detected Evidence

**Observed Evidence:** R12, [submission.xml:95241](detection/submission.xml#L95241) — `id-37f6913198e2434db5309c1c8e36fbc0`; XPath `/*/*[5]/*[12]`. Source: `[Business Service] Verify Identity Source` (BusinessService, Exchange `id-9bfedbfd5b404694a2224c039fa5d4c4`, governed identity unresolved; submitted record only). Target: `Identity Source & Correction Lineage` (Capability, Exchange `id-bdf18b5e580b5b85a34357a3b21f36db`, existing canonicalId `d72134e75001449a9e7ffae53f61458f`). The View presents these endpoints but contains no independent semantic containment assertion. The native Realization and View placement do not themselves answer the Harmonia context question.

### Architectural Questions / Adjudication

1. Does this Realization also establish Harmonia logical Feature context for `[Business Service] Verify Identity Source` in `Identity Source & Correction Lineage`?
2. If so, specify the bounded context, standing and any naming consequences for this instance. If not, retain the native Realization without invented context. No universal Realization inference or Composition is authorised.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B unresolved Realization → Feature logical-context matter; 2A separate interpretations; AX-14/17.

### Related Change Requests

- Required resulting state from [CR-028](#cr-028): The native R12 Realization has been reconciled and validated with the exact resolved governed source/target and authorised standing; an Accept disposition by itself supplies no resulting state.

Informationally related (not automatic prerequisites): [CR-036](#cr-036).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-030"></a>

## CR-030 — Add Identifier Create Flow to Identifier Verification

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add only native `Flow` from `[Business Service] Identifier Create` to `[Business Process] Identifier Verification` with the submitted direction and explicitly adjudicated bounded meaning.

### Detected Evidence

**Observed Evidence:** [submission.xml:95239](detection/submission.xml#L95239) — `id-bef77e2eb7de4691b4fbe9a0c84e8232`; XPath `/*/*[5]/*[10]`. Source: `[Business Service] Identifier Create` (BusinessService, Exchange `id-fddda503b8f0438e995465cd93204326`, governed identity unresolved; submitted record only). Target: `[Business Process] Identifier Verification` (BusinessProcess, Exchange `id-c645a859ac2c4447ad5ae850cf384bf3`, governed identity unresolved; submitted record only). No Name, Properties or Documentation. UNRESOLVED: new endpoint has no governed architectural identity. One visual connection in V3; see [relationships](detection/relationships.json) and [Views](detection/views.json).

### Architectural Questions / Adjudication

1. Accept this submitted Flow from `[Business Service] Identifier Create` to `[Business Process] Identifier Verification`?
2. What actual information/value transfer does this Flow assert, with what bounded direction and standing? A Flow is not automatically Triggering, execution order or orchestration.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Mapping §5 Flow/Serving direction and meaning; 2B; 2A; AX-14/17/18.

### Related Change Requests

- Required resulting state from [CR-006](#cr-006): `[Business Service] Identifier Create` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing.
- Required resulting state from [CR-008](#cr-008): `[Business Process] Identifier Verification` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing.

Informationally related (not automatic prerequisites): [CR-035](#cr-035).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-031"></a>

## CR-031 — Add Identifier Update Flow to Identifier Verification

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add only native `Flow` from `[Business Service] Identifier Update` to `[Business Process] Identifier Verification` with the submitted direction and explicitly adjudicated bounded meaning.

### Detected Evidence

**Observed Evidence:** [submission.xml:95240](detection/submission.xml#L95240) — `id-0849f31d6d7d4f7eb1e484d7437e5cd7`; XPath `/*/*[5]/*[11]`. Source: `[Business Service] Identifier Update` (BusinessService, Exchange `id-0088d61ccfcf4962b3aab8bc3b96021d`, governed identity unresolved; submitted record only). Target: `[Business Process] Identifier Verification` (BusinessProcess, Exchange `id-c645a859ac2c4447ad5ae850cf384bf3`, governed identity unresolved; submitted record only). No Name, Properties or Documentation. UNRESOLVED: new endpoint has no governed architectural identity. One visual connection in V3; see [relationships](detection/relationships.json) and [Views](detection/views.json).

### Architectural Questions / Adjudication

1. Accept this submitted Flow from `[Business Service] Identifier Update` to `[Business Process] Identifier Verification`?
2. What actual information/value transfer does this Flow assert, with what bounded direction and standing? A Flow is not automatically Triggering, execution order or orchestration.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Mapping §5 Flow/Serving direction and meaning; 2B; 2A; AX-14/17/18.

### Related Change Requests

- Required resulting state from [CR-007](#cr-007): `[Business Service] Identifier Update` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing.
- Required resulting state from [CR-008](#cr-008): `[Business Process] Identifier Verification` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing.

Informationally related (not automatic prerequisites): [CR-035](#cr-035).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-032"></a>

## CR-032 — Add Verify Identity Source Serving to Identifier Verification

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Add only native `Serving` from `[Business Service] Verify Identity Source` to `[Business Process] Identifier Verification` with the submitted direction and explicitly adjudicated bounded meaning.

### Detected Evidence

**Observed Evidence:** [submission.xml:95242](detection/submission.xml#L95242) — `id-c53c42ba0a4f437f9cb76a4c1a701816`; XPath `/*/*[5]/*[13]`. Source: `[Business Service] Verify Identity Source` (BusinessService, Exchange `id-9bfedbfd5b404694a2224c039fa5d4c4`, governed identity unresolved; submitted record only). Target: `[Business Process] Identifier Verification` (BusinessProcess, Exchange `id-c645a859ac2c4447ad5ae850cf384bf3`, governed identity unresolved; submitted record only). No Name, Properties or Documentation. UNRESOLVED: new endpoint has no governed architectural identity. One visual connection in V3; see [relationships](detection/relationships.json) and [Views](detection/views.json).

### Architectural Questions / Adjudication

1. Accept this submitted Serving from `[Business Service] Verify Identity Source` to `[Business Process] Identifier Verification`?
2. What service consumption does Serving assert? Confirm native legality, consuming behaviour and standing; this does not create ownership or domain authority.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** Mapping §5 Flow/Serving direction and meaning; 2B; 2A; AX-14/17/18.

### Related Change Requests

- Required resulting state from [CR-009](#cr-009): `[Business Service] Verify Identity Source` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing.
- Required resulting state from [CR-008](#cr-008): `[Business Process] Identifier Verification` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing.

Informationally related (not automatic prerequisites): [CR-035](#cr-035).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-033"></a>

## CR-033 — Revise Person Identity Administration Feature Landscape

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Show Person Identity Administration and six Feature appearances with six explicit Composition connections, in the submitted left-source/right-Feature arrangement. This is one independently reviewable authored View delta; it accepts no element, semantic edge or additional logical interpretation.

### Detected Evidence

**Observed Evidence:** V1, [submission.xml:256740](detection/submission.xml#L256740) — `id-e2a326c7d36751af9076a32dabe0e9cd`; XPath `/*/*[8]/*/*[972]`. `modified` relative to the exact baseline; 6 → 7 nodes, 0 → 6 connections. See [complete per-View inventory](detection/observations.md#v1) and [exact visual records, before/after geometry/styles and references](detection/views.json). Every displayed connection references a separately proposed semantic edge; presentation acceptance cannot create it.

### Architectural Questions / Adjudication

1. Accept this modified authored presentation of `0-Person Identity Administration — Feature Landscape` with the scope, membership and visual connections listed below and in the View evidence?
2. Confirm its bounded purpose, scope/standing filter, View home and submitted layout/readability for later authorised preservation. Geometry and folder placement establish no semantic containment.
3. The unchanged View Documentation says “Context and immediate governed children only. Standing remains in element Properties.” A1 has no standing Properties and Lineage remains Proposed. What precise scope/filter wording and standing presentation should the accepted View use?
4. Should the View communicate native Composition alone, or the additional instance CT3-framing interpretation? If it claims the latter, the applicable CR-016 through CR-021 resulting context states are additional prerequisites; record that choice and exact dependency revision before any instruction. The base dependency list below supplies presentation of native edges only.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B legitimate authored Views / existing-edge reuse; navigation/View convention; 2A dependencies; AX-14/17.

### Related Change Requests

- Required resulting state from [CR-001](#cr-001): The governed element corresponding to submitted `id-727a264026584f80bc3304ff8383dc92` exists with evidenced reconciled identity, native type/classification and standing for this View appearance.
- Required resulting state from [CR-011](#cr-011): The exact submitted relationship `id-36b2aa8ad58149fdb20d44c1c16dd64c` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.
- Required resulting state from [CR-012](#cr-012): The exact submitted relationship `id-3c3d723683c147a4a902b4d35a2af018` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.
- Required resulting state from [CR-010](#cr-010): The exact submitted relationship `id-73ecd7a39407404ba8e2bb663efbdc19` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.
- Required resulting state from [CR-015](#cr-015): The exact submitted relationship `id-bff76286f8994d20aa1b8aef166acebd` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.
- Required resulting state from [CR-013](#cr-013): The exact submitted relationship `id-daf437bb32114daa8a6b9462cb9b4fb2` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.
- Required resulting state from [CR-014](#cr-014): The exact submitted relationship `id-f9b270950f0241108b68f1c3daa50019` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.

Informationally related (not automatic prerequisites): [CR-016](#cr-016), [CR-017](#cr-017), [CR-018](#cr-018), [CR-019](#cr-019), [CR-020](#cr-020), [CR-021](#cr-021).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-034"></a>

## CR-034 — Expose existing Compositions in Entity Information Enablement View

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Expose seven already-governed Composition relationships from Entity Information Enablement to its CT2 appearances; preserve the existing eight nodes and geometry. This is one independently reviewable authored View delta; it accepts no element, semantic edge or additional logical interpretation.

### Detected Evidence

**Observed Evidence:** V2, [submission.xml:259158](detection/submission.xml#L259158) — `id-c8ec850977565a17869af357ee28d917`; XPath `/*/*[8]/*/*[1017]`. `modified` relative to the exact baseline; 8 → 8 nodes, 0 → 7 connections. See [complete per-View inventory](detection/observations.md#v2) and [exact visual records, before/after geometry/styles and references](detection/views.json). Every added connection resolves to an existing baseline relationship with unchanged identity, endpoints and assertion Properties. There is no new semantic edge for this CR.

### Architectural Questions / Adjudication

1. Accept this modified authored presentation of `0-Entity Information Enablement — CT2 Landscape` with the scope, membership and visual connections listed below and in the View evidence?
2. Confirm its bounded purpose, scope/standing filter, View home and submitted layout/readability for later authorised preservation. Geometry and folder placement establish no semantic containment.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B legitimate authored Views / existing-edge reuse; navigation/View convention; 2A dependencies; AX-14/17.

### Related Change Requests

No prerequisite CR. Established baseline endpoints/references and their current standing must remain applicable; there is no inferred new semantic dependency.

Required existing governed state: the seven exact Composition bindings, endpoint Canonical IDs and assertion Properties listed under V2 remain intact; no relationship-addition CR is required.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-035"></a>

## CR-035 — Add Identifier Capture Feature realisation View

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Present Identifier Capture, Identifier Create, Identifier Update, Identifier Verification and Verify Identity Source with the six submitted Realization/Flow/Serving appearances. This is one independently reviewable authored View delta; it accepts no element, semantic edge or additional logical interpretation.

### Detected Evidence

**Observed Evidence:** V3, [submission.xml:256638](detection/submission.xml#L256638) — `id-216c4ab0757b45b3894e7e0c2594b9f9`; XPath `/*/*[8]/*/*[971]`. `added` relative to the exact baseline; 0 → 5 nodes, 0 → 6 connections. See [complete per-View inventory](detection/observations.md#v3) and [exact visual records, before/after geometry/styles and references](detection/views.json). Every displayed connection references a separately proposed semantic edge; presentation acceptance cannot create it.

### Architectural Questions / Adjudication

1. Accept this added authored presentation of `Identifier Capture - Feature realisation` with the scope, membership and visual connections listed below and in the View evidence?
2. Confirm its bounded purpose, scope/standing filter, View home and submitted layout/readability for later authorised preservation. Geometry and folder placement establish no semantic containment.
3. There is no submitted Documentation or Properties for this new View. Confirm the proposed purpose above; its Feature-realisation Name is evidence of presentation intent, not proof of logical Feature containment.
4. Does the accepted View require the separately reviewed logical Feature-context interpretations? Its explicit native-edge presentation can stand without them. Record any stronger context claim and corresponding resulting-state prerequisites before authorisation.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B legitimate authored Views / existing-edge reuse; navigation/View convention; 2A dependencies; AX-14/17.

### Related Change Requests

- Required resulting state from [CR-007](#cr-007): The governed element corresponding to submitted `id-0088d61ccfcf4962b3aab8bc3b96021d` exists with evidenced reconciled identity, native type/classification and standing for this View appearance.
- Required resulting state from [CR-001](#cr-001): The governed element corresponding to submitted `id-727a264026584f80bc3304ff8383dc92` exists with evidenced reconciled identity, native type/classification and standing for this View appearance.
- Required resulting state from [CR-009](#cr-009): The governed element corresponding to submitted `id-9bfedbfd5b404694a2224c039fa5d4c4` exists with evidenced reconciled identity, native type/classification and standing for this View appearance.
- Required resulting state from [CR-008](#cr-008): The governed element corresponding to submitted `id-c645a859ac2c4447ad5ae850cf384bf3` exists with evidenced reconciled identity, native type/classification and standing for this View appearance.
- Required resulting state from [CR-006](#cr-006): The governed element corresponding to submitted `id-fddda503b8f0438e995465cd93204326` exists with evidenced reconciled identity, native type/classification and standing for this View appearance.
- Required resulting state from [CR-031](#cr-031): The exact submitted relationship `id-0849f31d6d7d4f7eb1e484d7437e5cd7` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.
- Required resulting state from [CR-024](#cr-024): The exact submitted relationship `id-69eb864f56df4d928f8abc3f7ab4e834` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.
- Required resulting state from [CR-030](#cr-030): The exact submitted relationship `id-bef77e2eb7de4691b4fbe9a0c84e8232` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.
- Required resulting state from [CR-026](#cr-026): The exact submitted relationship `id-c0f84a8e4c8d496580a68cac4823c58c` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.
- Required resulting state from [CR-032](#cr-032): The exact submitted relationship `id-c53c42ba0a4f437f9cb76a4c1a701816` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.
- Required resulting state from [CR-022](#cr-022): The exact submitted relationship `id-e84f2e9df66a48d795d604d75afc72cb` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.

Informationally related (not automatic prerequisites): [CR-023](#cr-023), [CR-025](#cr-025), [CR-027](#cr-027), [CR-029](#cr-029).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed

<a id="cr-036"></a>

## CR-036 — Add Identity Source and Correction Lineage realisation View

**Status:** On-Hold

### Proposed Architectural Change

**Candidate Architectural Interpretation:** Present Verify Identity Source realizing Identity Source & Correction Lineage, with two element appearances and one Realization connection. This is one independently reviewable authored View delta; it accepts no element, semantic edge or additional logical interpretation.

### Detected Evidence

**Observed Evidence:** V4, [submission.xml:256609](detection/submission.xml#L256609) — `id-8fb2f8b1776f4ebf969e9b5b638e4ed3`; XPath `/*/*[8]/*/*[970]`. `added` relative to the exact baseline; 0 → 2 nodes, 0 → 1 connections. See [complete per-View inventory](detection/observations.md#v4) and [exact visual records, before/after geometry/styles and references](detection/views.json). Every displayed connection references a separately proposed semantic edge; presentation acceptance cannot create it.

### Architectural Questions / Adjudication

1. Accept this added authored presentation of `Identity Source & Correction Lineage - Feature realisation` with the scope, membership and visual connections listed below and in the View evidence?
2. Confirm its bounded purpose, scope/standing filter, View home and submitted layout/readability for later authorised preservation. Geometry and folder placement establish no semantic containment.
3. There is no submitted Documentation or Properties for this new View. Confirm the proposed purpose above; its Feature-realisation Name is evidence of presentation intent, not proof of logical Feature containment.
4. Does the accepted View require the separately reviewed logical Feature-context interpretations? Its explicit native-edge presentation can stand without them. Record any stronger context claim and corresponding resulting-state prerequisites before authorisation.

**Solution Architect Decision:** No final disposition. Rationale, authority reference, adjudicator and date: not yet recorded.

**Applicable authority / identified gap:** 2B legitimate authored Views / existing-edge reuse; navigation/View convention; 2A dependencies; AX-14/17.

### Related Change Requests

- Required resulting state from [CR-009](#cr-009): The governed element corresponding to submitted `id-9bfedbfd5b404694a2224c039fa5d4c4` exists with evidenced reconciled identity, native type/classification and standing for this View appearance.
- Required resulting state from [CR-028](#cr-028): The exact submitted relationship `id-37f6913198e2434db5309c1c8e36fbc0` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge.

Informationally related (not automatic prerequisites): [CR-029](#cr-029).

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed
