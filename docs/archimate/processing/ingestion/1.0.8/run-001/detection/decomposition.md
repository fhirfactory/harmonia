# Change Request decomposition — 1.0.8/run-001

Revision 1. Stable review-local IDs allocated once, CR-001 through CR-036. These process IDs are not Canonical IDs. Evidence labels identify records/appearances only.

The 36 CRs comprise 9 element-instance additions, 13 native relationship additions, 6 instance-specific CT3-framing interpretation questions, 4 logical Feature-context interpretation questions and 4 authored View changes. Deterministic proposed Feature recognition belongs with its element proposal; a separate classification CR would add no unresolved independent decision. Repeated native-type qualifiers establish no extra subtype. No missing-metadata, transport, folder-replay or pre-existing migration CRs are created.

Each relationship can be decided independently of its endpoint addition and its optional additional Harmonia context. The six CT3-framing questions do not recreate existing source-qualified context: five targets already have Proposed placement, and the question concerns whether/how this native encoding carries it. The four Realization context questions remain separate from the native edges. Neither family proposes a universal rule.

Each View CR covers one coherent authored presentation delta—membership, connections, layout and declared scope—because those observations together express its review question. Native element/edge decisions remain separate. A Solution Architect may request a bounded amendment/split without losing these original IDs/evidence. V2 is independently reviewable against existing architecture; the others depend on the exact reconciled displayed elements/edges. Logical-context CRs are not silently prerequisites of a native-edge-only presentation. A stronger scope claim needs an explicit dependency revision before authorisation.

| Evidence | CR | Independently adjudicable architectural matter |
| --- | --- | --- |
| A1 | [CR-001](../change-review.md#cr-001) | Add Identifier Capture Feature |
| A2 | [CR-002](../change-review.md#cr-002) | Add Provider Registry - Satellite Product |
| A3 | [CR-003](../change-review.md#cr-003) | Add Provider Registry - Central Product |
| A4 | [CR-004](../change-review.md#cr-004) | Add Practitioner Collaboration Service Product |
| A5 | [CR-005](../change-review.md#cr-005) | Add Health Information Exchange Service Product |
| A6 | [CR-006](../change-review.md#cr-006) | Add Identifier Create Business Service |
| A7 | [CR-007](../change-review.md#cr-007) | Add Identifier Update Business Service |
| A8 | [CR-008](../change-review.md#cr-008) | Add Identifier Verification Business Process |
| A9 | [CR-009](../change-review.md#cr-009) | Add Verify Identity Source Business Service |
| R1, V1 | [CR-010](../change-review.md#cr-010) | Add Composition to Cross-Authority Identifier Correlation |
| R2, V1 | [CR-011](../change-review.md#cr-011) | Add Composition to Governed Identity Correction |
| R3, V1 | [CR-012](../change-review.md#cr-012) | Add Composition to Identity Source & Correction Lineage |
| R4, V1 | [CR-013](../change-review.md#cr-013) | Add Composition to Identifier Resolution |
| R5, V1 | [CR-014](../change-review.md#cr-014) | Add Composition to Identity Alias Association |
| R6, V1 | [CR-015](../change-review.md#cr-015) | Add Composition to Identifier Capture |
| R1, V1 | [CR-016](../change-review.md#cr-016) | Interpret CT3 framing for Cross-Authority Identifier Correlation |
| R2, V1 | [CR-017](../change-review.md#cr-017) | Interpret CT3 framing for Governed Identity Correction |
| R3, V1 | [CR-018](../change-review.md#cr-018) | Interpret CT3 framing for Identity Source & Correction Lineage |
| R4, V1 | [CR-019](../change-review.md#cr-019) | Interpret CT3 framing for Identifier Resolution |
| R5, V1 | [CR-020](../change-review.md#cr-020) | Interpret CT3 framing for Identity Alias Association |
| R6, V1 | [CR-021](../change-review.md#cr-021) | Interpret CT3 framing for Identifier Capture |
| R7, V3 | [CR-022](../change-review.md#cr-022) | Add Identifier Create Realization |
| R7, V3 | [CR-023](../change-review.md#cr-023) | Interpret Feature context for Identifier Create |
| R8, V3 | [CR-024](../change-review.md#cr-024) | Add Identifier Update Realization |
| R8, V3 | [CR-025](../change-review.md#cr-025) | Interpret Feature context for Identifier Update |
| R9, V3 | [CR-026](../change-review.md#cr-026) | Add Identifier Verification Realization |
| R9, V3 | [CR-027](../change-review.md#cr-027) | Interpret Feature context for Identifier Verification |
| R12, V4 | [CR-028](../change-review.md#cr-028) | Add Verify Identity Source Realization |
| R12, V4 | [CR-029](../change-review.md#cr-029) | Interpret Feature context for Verify Identity Source |
| R10, V3 | [CR-030](../change-review.md#cr-030) | Add Identifier Create Flow to Identifier Verification |
| R11, V3 | [CR-031](../change-review.md#cr-031) | Add Identifier Update Flow to Identifier Verification |
| R13, V3 | [CR-032](../change-review.md#cr-032) | Add Verify Identity Source Serving to Identifier Verification |
| V1 | [CR-033](../change-review.md#cr-033) | Revise Person Identity Administration Feature Landscape |
| V2 | [CR-034](../change-review.md#cr-034) | Expose existing Compositions in Entity Information Enablement View |
| V3 | [CR-035](../change-review.md#cr-035) | Add Identifier Capture Feature realisation View |
| V4 | [CR-036](../change-review.md#cr-036) | Add Identity Source and Correction Lineage realisation View |

## Evidence-only observations

| Evidence | Treatment |
| --- | --- |
| 61 definition rekeys/language losses; 1,669 evidence-Property changes; organisation-ID/style/newline/order transport | Retained transport evidence, no architectural CR |
| A1–A9 absent canonical identity/derived metadata | Expected authoring boundary; questions about new-instance binding/meaning stay with element CRs; no metadata-population CR |
| 13 root Relations references | Preserve incoming navigation evidence; governed publication homes follow resolved source, no semantic CR for replay |
| 841 existing Name/displayName discrepancies | Existing 2B migration debt, unchanged; no submission CR |
| Seven V2 existing-edge appearances | Shared presentation evidence for CR-034; zero new semantic-edge CRs |
| Incomplete governed endpoint identity for R6–R13 | Duplicate resolution remains unresolved pending endpoint resulting states; no guessed merge/new allocation |

## Dependencies

The following are required resulting states for later processing, not execution scheduling or an instruction to reconcile. Dispositions alone establish no endpoint/relationship. No dependency logic was executed.

| Dependent CR | Prerequisite CR | Required governed resulting state |
| --- | --- | --- |
| CR-015 | CR-001 | Identifier Capture exists as the resolved governed architectural endpoint for submitted A1, with its accepted native type/classification and evidenced reconciliation; identity has not been allocated here. |
| CR-016 | CR-010 | The exact native R1 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient. |
| CR-017 | CR-011 | The exact native R2 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient. |
| CR-018 | CR-012 | The exact native R3 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient. |
| CR-019 | CR-013 | The exact native R4 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient. |
| CR-020 | CR-014 | The exact native R5 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient. |
| CR-021 | CR-015 | The exact native R6 Composition exists as an evidenced reconciled relationship between resolved governed endpoints, at its authorised standing. Acceptance alone is insufficient. |
| CR-022 | CR-006 | `[Business Service] Identifier Create` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing. |
| CR-022 | CR-001 | `[Feature] Identifier Capture` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing. |
| CR-023 | CR-022 | The native R7 Realization has been reconciled and validated with the exact resolved governed source/target and authorised standing; an Accept disposition by itself supplies no resulting state. |
| CR-024 | CR-007 | `[Business Service] Identifier Update` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing. |
| CR-024 | CR-001 | `[Feature] Identifier Capture` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing. |
| CR-025 | CR-024 | The native R8 Realization has been reconciled and validated with the exact resolved governed source/target and authorised standing; an Accept disposition by itself supplies no resulting state. |
| CR-026 | CR-008 | `[Business Process] Identifier Verification` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing. |
| CR-026 | CR-001 | `[Feature] Identifier Capture` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing. |
| CR-027 | CR-026 | The native R9 Realization has been reconciled and validated with the exact resolved governed source/target and authorised standing; an Accept disposition by itself supplies no resulting state. |
| CR-028 | CR-009 | `[Business Service] Verify Identity Source` exists as the evidenced reconciled governed endpoint for its submitted record, at its authorised native type/classification and standing. |
| CR-029 | CR-028 | The native R12 Realization has been reconciled and validated with the exact resolved governed source/target and authorised standing; an Accept disposition by itself supplies no resulting state. |
| CR-030 | CR-006 | `[Business Service] Identifier Create` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing. |
| CR-030 | CR-008 | `[Business Process] Identifier Verification` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing. |
| CR-031 | CR-007 | `[Business Service] Identifier Update` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing. |
| CR-031 | CR-008 | `[Business Process] Identifier Verification` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing. |
| CR-032 | CR-009 | `[Business Service] Verify Identity Source` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing. |
| CR-032 | CR-008 | `[Business Process] Identifier Verification` exists as its resolved, reconciled governed endpoint, with authorised responsibility/type and evidenced standing. |
| CR-033 | CR-001 | The governed element corresponding to submitted `id-727a264026584f80bc3304ff8383dc92` exists with evidenced reconciled identity, native type/classification and standing for this View appearance. |
| CR-033 | CR-011 | The exact submitted relationship `id-36b2aa8ad58149fdb20d44c1c16dd64c` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-033 | CR-012 | The exact submitted relationship `id-3c3d723683c147a4a902b4d35a2af018` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-033 | CR-010 | The exact submitted relationship `id-73ecd7a39407404ba8e2bb663efbdc19` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-033 | CR-015 | The exact submitted relationship `id-bff76286f8994d20aa1b8aef166acebd` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-033 | CR-013 | The exact submitted relationship `id-daf437bb32114daa8a6b9462cb9b4fb2` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-033 | CR-014 | The exact submitted relationship `id-f9b270950f0241108b68f1c3daa50019` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-035 | CR-007 | The governed element corresponding to submitted `id-0088d61ccfcf4962b3aab8bc3b96021d` exists with evidenced reconciled identity, native type/classification and standing for this View appearance. |
| CR-035 | CR-001 | The governed element corresponding to submitted `id-727a264026584f80bc3304ff8383dc92` exists with evidenced reconciled identity, native type/classification and standing for this View appearance. |
| CR-035 | CR-009 | The governed element corresponding to submitted `id-9bfedbfd5b404694a2224c039fa5d4c4` exists with evidenced reconciled identity, native type/classification and standing for this View appearance. |
| CR-035 | CR-008 | The governed element corresponding to submitted `id-c645a859ac2c4447ad5ae850cf384bf3` exists with evidenced reconciled identity, native type/classification and standing for this View appearance. |
| CR-035 | CR-006 | The governed element corresponding to submitted `id-fddda503b8f0438e995465cd93204326` exists with evidenced reconciled identity, native type/classification and standing for this View appearance. |
| CR-035 | CR-031 | The exact submitted relationship `id-0849f31d6d7d4f7eb1e484d7437e5cd7` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-035 | CR-024 | The exact submitted relationship `id-69eb864f56df4d928f8abc3f7ab4e834` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-035 | CR-030 | The exact submitted relationship `id-bef77e2eb7de4691b4fbe9a0c84e8232` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-035 | CR-026 | The exact submitted relationship `id-c0f84a8e4c8d496580a68cac4823c58c` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-035 | CR-032 | The exact submitted relationship `id-c53c42ba0a4f437f9cb76a4c1a701816` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-035 | CR-022 | The exact submitted relationship `id-e84f2e9df66a48d795d604d75afc72cb` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |
| CR-036 | CR-009 | The governed element corresponding to submitted `id-9bfedbfd5b404694a2224c039fa5d4c4` exists with evidenced reconciled identity, native type/classification and standing for this View appearance. |
| CR-036 | CR-028 | The exact submitted relationship `id-37f6913198e2434db5309c1c8e36fbc0` exists as a reconciled governed edge, with correct resolved endpoints, direction and standing; the View connection must reference that edge. |

Existing-endpoint prerequisites additionally require the exact canonical bindings and source standing recorded in relationships.json, with current-target applicability checked before any later instruction. Target Lineage retains Proposed standing. Optional stronger View scope prerequisites are explicit questions in the relevant View CR; they are not assumed executed or silently added here.
