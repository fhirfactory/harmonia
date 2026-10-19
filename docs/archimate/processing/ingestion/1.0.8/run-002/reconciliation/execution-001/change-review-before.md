<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate Home update Change Review — 1.0.8/run-002

## Review basis

- Recorded by Codex, 2026-10-19 (Australia/Sydney), governance release 1.0.14.
- Exact comparison baseline remains publication 1.0.8, not the governance release.
- The Solution Architect explicitly directed a fresh read of the updated landing file. This is a distinct input under the [run convention](../../README.md#run-layout), linked to predecessor [run-001](../run-001/change-review.md); it does not overwrite or reopen that original input's evidence. No competing architecture branch or Information/Application ingestion is created.
- Updated source: [HarmoniaArchitecture-1.0.8.xml](../../../../exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml).
- Fresh SHA-256 `366bbcf305575b831ea7e8e72e1c2b19f9631518fda8f297a4f7c51a959a5556`; 21,858,672 bytes. [Compressed byte-identical snapshot](detection/submission.xml.gz), SHA-256 of decompressed bytes identical. Prior input SHA-256 `6b973db242a4b26bc6a93e27b482100b6f96655cb40deb4ceb35a93c134c7d3e`; baseline SHA-256 `504703cd1c4a91efe0f598db6a974ffea1a08541ebe81372130f1d647d71d69a`.
- [Read-only detection](../../../../framework/tools/analyse_home_submission.py) and [exact semantic evidence](detection/explicit-homes.json) report four new directed Association «Home» edges, no changed/deleted elements or existing semantic relationships, and four additional connections in two existing Views. The new Archi property definition carries Specialization; it is transport metadata for the authored semantic specialisation, not a separate architecture proposal.
- No previous proposal is silently accepted. Original run-001 instance proposals remain independently governed. The Solution Architect-authored Home intent is authoritative; preserving it does not invent the missing endpoint identities or approve an entire ingestion. This scoped review records only the newly authored assertions and View appearances. The full updated snapshot also retains the original proposals.
- Authority: [modelling strategy](../../../../../markdown/governance/architecture-modelling-strategy.md), [Change Review contract](../../../../../markdown/governance/archimate-ingestion-change-review.md), and AX-04/14/17. All new CRs have initial/default On-Hold Dispositions; no issued Disposition is rewritten. No new Home for any new 1.0.8 element has been manufactured.
- The separate accepted one-time existing-baseline migration is recorded in [business-homes.json](../../../../../markdown/governance/baseline/business-homes.json). It is not reconciliation of the new endpoint proposals here. Mandatory Home cardinality, inheritance, automatic naming/navigation and downstream-layer rules remain undecided.

| ID | Brief Description | Disposition | Outcome |
| --- | --- | --- | --- |
| [CR-001](#cr-001) | Preserve authored Home: [Business Process] Identifier Verification | Accept | Unresolved |
| [CR-002](#cr-002) | Preserve authored Home: [Business Service] Identifier Update | Accept | Unresolved |
| [CR-003](#cr-003) | Preserve authored Home: [Business Service] Identifier Create | Accept | Unresolved |
| [CR-004](#cr-004) | Preserve authored Home: [Business Service] Verify Identity Source | Accept | Unresolved |
| [CR-005](#cr-005) | Preserve added Home connections in CR-035 View | Accept | Unresolved |
| [CR-006](#cr-006) | Preserve added Home connections in CR-036 View | Accept | Unresolved |

<a id="cr-001"></a>

## CR-001 — Preserve authored Home: [Business Process] Identifier Verification

**Disposition:** On-Hold

**Outcome:** Unresolved

**Outcome Reason:** Authored Home intent is explicit and authoritative under the Solution Architect's task decision. Reconciliation remains unexecuted because its newly submitted source endpoint (and, where applicable, target endpoint) has no accepted governed identity/binding. This is not an unresolved Home interpretation or a rejection of the authored assertion.

### Proposed Architectural Change

Preserve the Solution Architect-authored `[Business Process] Identifier Verification → directed Association «Home» → [Feature] Identifier Capture` as its own semantic assertion. Existing native relationships remain independent.

### Detected Evidence

Fresh input SHA-256 `366bbcf305575b831ea7e8e72e1c2b19f9631518fda8f297a4f7c51a959a5556`, relationship `id-ba8d543c3f6a4f15bd41e5c2d7bcf6cf`, line 95244; exact raw relationship and endpoint IDs are in [explicit-homes.json](detection/explicit-homes.json). Source `id-c645a859ac2c4447ad5ae850cf384bf3` is native `BusinessProcess`; target `id-727a264026584f80bc3304ff8383dc92` is native `Capability`, recognised Feature. Native type Association; directed true; specialisation Home; Name absent. Context: Entity Information Enablement → Client Administration → Person Identity Administration → Feature. These authored Composition contexts retain their own standing; no Home is inferred through them.

### Architectural Questions / Adjudication

The Solution Architect has supplied the intended Home explicitly. No further Realization/context interpretation is needed. New endpoint identity, bounded definition and acceptance remain with the corresponding original element proposals. The initial/default On-Hold call records that dependency, not uncertainty about the Home meaning. No new Disposition is issued on behalf of the Solution Architect.

### Related Change Requests

Original [run-001 CR-001 and CR-006–009](../run-001/change-review.md) govern the new endpoint proposals. Their accepted, evidenced governed state is required before reconciliation. Original Realization proposals CR-022/024/026/028 remain independent; interpretation findings CR-023/025/027/029 are Resolved by governing decision.

### Authorised Reconciliation Instruction

Not authorised pending governed endpoint resolution. Preserve the incoming relationship exactly as authored; do not manufacture or replace it.

### Reconciliation / Processing Record

Input preserved; no endpoint allocation or semantic reconciliation executed.

<a id="cr-002"></a>

## CR-002 — Preserve authored Home: [Business Service] Identifier Update

**Disposition:** On-Hold

**Outcome:** Unresolved

**Outcome Reason:** Authored Home intent is explicit and authoritative under the Solution Architect's task decision. Reconciliation remains unexecuted because its newly submitted source endpoint (and, where applicable, target endpoint) has no accepted governed identity/binding. This is not an unresolved Home interpretation or a rejection of the authored assertion.

### Proposed Architectural Change

Preserve the Solution Architect-authored `[Business Service] Identifier Update → directed Association «Home» → [Feature] Identifier Capture` as its own semantic assertion. Existing native relationships remain independent.

### Detected Evidence

Fresh input SHA-256 `366bbcf305575b831ea7e8e72e1c2b19f9631518fda8f297a4f7c51a959a5556`, relationship `id-a21144ee285f467982d13c39b49228dd`, line 95251; exact raw relationship and endpoint IDs are in [explicit-homes.json](detection/explicit-homes.json). Source `id-0088d61ccfcf4962b3aab8bc3b96021d` is native `BusinessService`; target `id-727a264026584f80bc3304ff8383dc92` is native `Capability`, recognised Feature. Native type Association; directed true; specialisation Home; Name absent. Context: Entity Information Enablement → Client Administration → Person Identity Administration → Feature. These authored Composition contexts retain their own standing; no Home is inferred through them.

### Architectural Questions / Adjudication

The Solution Architect has supplied the intended Home explicitly. No further Realization/context interpretation is needed. New endpoint identity, bounded definition and acceptance remain with the corresponding original element proposals. The initial/default On-Hold call records that dependency, not uncertainty about the Home meaning. No new Disposition is issued on behalf of the Solution Architect.

### Related Change Requests

Original [run-001 CR-001 and CR-006–009](../run-001/change-review.md) govern the new endpoint proposals. Their accepted, evidenced governed state is required before reconciliation. Original Realization proposals CR-022/024/026/028 remain independent; interpretation findings CR-023/025/027/029 are Resolved by governing decision.

### Authorised Reconciliation Instruction

Not authorised pending governed endpoint resolution. Preserve the incoming relationship exactly as authored; do not manufacture or replace it.

### Reconciliation / Processing Record

Input preserved; no endpoint allocation or semantic reconciliation executed.

<a id="cr-003"></a>

## CR-003 — Preserve authored Home: [Business Service] Identifier Create

**Disposition:** On-Hold

**Outcome:** Unresolved

**Outcome Reason:** Authored Home intent is explicit and authoritative under the Solution Architect's task decision. Reconciliation remains unexecuted because its newly submitted source endpoint (and, where applicable, target endpoint) has no accepted governed identity/binding. This is not an unresolved Home interpretation or a rejection of the authored assertion.

### Proposed Architectural Change

Preserve the Solution Architect-authored `[Business Service] Identifier Create → directed Association «Home» → [Feature] Identifier Capture` as its own semantic assertion. Existing native relationships remain independent.

### Detected Evidence

Fresh input SHA-256 `366bbcf305575b831ea7e8e72e1c2b19f9631518fda8f297a4f7c51a959a5556`, relationship `id-3f9d4356f4b84ef7b45fe3435d27fde8`, line 95258; exact raw relationship and endpoint IDs are in [explicit-homes.json](detection/explicit-homes.json). Source `id-fddda503b8f0438e995465cd93204326` is native `BusinessService`; target `id-727a264026584f80bc3304ff8383dc92` is native `Capability`, recognised Feature. Native type Association; directed true; specialisation Home; Name absent. Context: Entity Information Enablement → Client Administration → Person Identity Administration → Feature. These authored Composition contexts retain their own standing; no Home is inferred through them.

### Architectural Questions / Adjudication

The Solution Architect has supplied the intended Home explicitly. No further Realization/context interpretation is needed. New endpoint identity, bounded definition and acceptance remain with the corresponding original element proposals. The initial/default On-Hold call records that dependency, not uncertainty about the Home meaning. No new Disposition is issued on behalf of the Solution Architect.

### Related Change Requests

Original [run-001 CR-001 and CR-006–009](../run-001/change-review.md) govern the new endpoint proposals. Their accepted, evidenced governed state is required before reconciliation. Original Realization proposals CR-022/024/026/028 remain independent; interpretation findings CR-023/025/027/029 are Resolved by governing decision.

### Authorised Reconciliation Instruction

Not authorised pending governed endpoint resolution. Preserve the incoming relationship exactly as authored; do not manufacture or replace it.

### Reconciliation / Processing Record

Input preserved; no endpoint allocation or semantic reconciliation executed.

<a id="cr-004"></a>

## CR-004 — Preserve authored Home: [Business Service] Verify Identity Source

**Disposition:** On-Hold

**Outcome:** Unresolved

**Outcome Reason:** Authored Home intent is explicit and authoritative under the Solution Architect's task decision. Reconciliation remains unexecuted because its newly submitted source endpoint (and, where applicable, target endpoint) has no accepted governed identity/binding. This is not an unresolved Home interpretation or a rejection of the authored assertion.

### Proposed Architectural Change

Preserve the Solution Architect-authored `[Business Service] Verify Identity Source → directed Association «Home» → Identity Source & Correction Lineage` as its own semantic assertion. Existing native relationships remain independent.

### Detected Evidence

Fresh input SHA-256 `366bbcf305575b831ea7e8e72e1c2b19f9631518fda8f297a4f7c51a959a5556`, relationship `id-d559bbf53430465e90f47c40593403a5`, line 95265; exact raw relationship and endpoint IDs are in [explicit-homes.json](detection/explicit-homes.json). Source `id-9bfedbfd5b404694a2224c039fa5d4c4` is native `BusinessService`; target `id-bdf18b5e580b5b85a34357a3b21f36db` is native `Capability`, recognised Feature. Native type Association; directed true; specialisation Home; Name absent. Context: Entity Information Enablement → Client Administration → Person Identity Administration → Feature. These authored Composition contexts retain their own standing; no Home is inferred through them.

### Architectural Questions / Adjudication

The Solution Architect has supplied the intended Home explicitly. No further Realization/context interpretation is needed. New endpoint identity, bounded definition and acceptance remain with the corresponding original element proposals. The initial/default On-Hold call records that dependency, not uncertainty about the Home meaning. No new Disposition is issued on behalf of the Solution Architect.

### Related Change Requests

Original [run-001 CR-001 and CR-006–009](../run-001/change-review.md) govern the new endpoint proposals. Their accepted, evidenced governed state is required before reconciliation. Original Realization proposals CR-022/024/026/028 remain independent; interpretation findings CR-023/025/027/029 are Resolved by governing decision.

### Authorised Reconciliation Instruction

Not authorised pending governed endpoint resolution. Preserve the incoming relationship exactly as authored; do not manufacture or replace it.

### Reconciliation / Processing Record

Input preserved; no endpoint allocation or semantic reconciliation executed.

<a id="cr-005"></a>

## CR-005 — Preserve added Home connections in CR-035 View

**Disposition:** On-Hold

**Outcome:** Unresolved

**Outcome Reason:** Added visual appearances are retained authored evidence; acceptance and reconciliation of the related View and endpoint state remain unexecuted.

### Proposed Architectural Change

Preserve only the added Home connection appearances in View `id-216c4ab0757b45b3894e7e0c2594b9f9`; reuse their explicit semantic Home edges. This supplies no additional semantic Home.

### Detected Evidence

The updated input adds three Home connection(s) to this existing View. All previous nodes and connections, coordinates, names and View metadata remain unchanged. [Evidence](detection/explicit-homes.json) records the changed View ID; the byte-identical compressed snapshot preserves every connection ID and endpoint.

### Architectural Questions / Adjudication

No View placement can establish Home. The exact authored Home relationships already exist. View acceptance and endpoint reconciliation require their independently governed state.

### Related Change Requests

[run-001 CR-035](../run-001/change-review.md#cr-035) retains the original View proposal; this run records only its updated-input delta. Related Home proposals above must have evidenced governed endpoints before replay.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed; authored visual evidence preserved.

<a id="cr-006"></a>

## CR-006 — Preserve added Home connections in CR-036 View

**Disposition:** On-Hold

**Outcome:** Unresolved

**Outcome Reason:** Added visual appearances are retained authored evidence; acceptance and reconciliation of the related View and endpoint state remain unexecuted.

### Proposed Architectural Change

Preserve only the added Home connection appearances in View `id-8fb2f8b1776f4ebf969e9b5b638e4ed3`; reuse their explicit semantic Home edges. This supplies no additional semantic Home.

### Detected Evidence

The updated input adds one Home connection(s) to this existing View. All previous nodes and connections, coordinates, names and View metadata remain unchanged. [Evidence](detection/explicit-homes.json) records the changed View ID; the byte-identical compressed snapshot preserves every connection ID and endpoint.

### Architectural Questions / Adjudication

No View placement can establish Home. The exact authored Home relationships already exist. View acceptance and endpoint reconciliation require their independently governed state.

### Related Change Requests

[run-001 CR-036](../run-001/change-review.md#cr-036) retains the original View proposal; this run records only its updated-input delta. Related Home proposals above must have evidenced governed endpoints before replay.

### Authorised Reconciliation Instruction

Not authorised

### Reconciliation / Processing Record

Not yet processed; authored visual evidence preserved.
