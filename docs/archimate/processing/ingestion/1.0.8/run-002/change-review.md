| ID | Brief Description | Disposition | Outcome |
| --- | --- | --- | --- |
| [CR-001](#cr-001) | Preserve authored Home: [Business Process] Identifier Verification | Accept | Resolved |
| [CR-002](#cr-002) | Preserve authored Home: [Business Service] Identifier Update | Accept | Resolved |
| [CR-003](#cr-003) | Preserve authored Home: [Business Service] Identifier Create | Accept | Resolved |
| [CR-004](#cr-004) | Preserve authored Home: [Business Service] Verify Identity Source | Accept | Resolved |
| [CR-005](#cr-005) | Preserve added Home connections in CR-035 View | Accept | Resolved |
| [CR-006](#cr-006) | Preserve added Home connections in CR-036 View | Accept | Resolved |

## Current execution revision 2 — 2026-10-19, RAD-1.0.9

The Solution Architect's subsequent Accept instructions are executed below. Earlier On-Hold calls, Outcomes, reasons and Not authorised instructions are preserved in the [byte-identical pre-execution review](reconciliation/execution-001/change-review-before.md) and existing prior revisions. Interpretation findings resolved by governing decisions retain their original On-Hold Dispositions and reasons. The [execution audit](reconciliation/execution-001/results.json) records actual resulting states; acceptance alone was not treated as a satisfied dependency.

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


<a id="cr-001"></a>

## CR-001 — Preserve authored Home: [Business Process] Identifier Verification

**Disposition:** Accept

**Outcome:** Resolved

**Outcome Reason:** Resolved by the authorised 2026-10-19 reconciliation: the exact governed resulting state exists in RAD-1.0.9, with dependencies, canonical metadata, references and actual Archi 5.10 transport verified. See [the per-CR execution record](reconciliation/execution-001/results.json) (`CR-001`) and [publication verification](../../../publishing/1.0.9/reconciliation-verification.json). Original Disposition, Outcome and Outcome Reason remain unchanged in the [pre-execution review](reconciliation/execution-001/change-review-before.md). Human rendering inspection remains a separate gate.

### Proposed Architectural Change

Preserve the Solution Architect-authored `[Business Process] Identifier Verification → directed Association «Home» → [Feature] Identifier Capture` as its own semantic assertion. Existing native relationships remain independent.

### Detected Evidence

Fresh input SHA-256 `366bbcf305575b831ea7e8e72e1c2b19f9631518fda8f297a4f7c51a959a5556`, relationship `id-ba8d543c3f6a4f15bd41e5c2d7bcf6cf`, line 95244; exact raw relationship and endpoint IDs are in [explicit-homes.json](detection/explicit-homes.json). Source `id-c645a859ac2c4447ad5ae850cf384bf3` is native `BusinessProcess`; target `id-727a264026584f80bc3304ff8383dc92` is native `Capability`, recognised Feature. Native type Association; directed true; specialisation Home; Name absent. Context: Entity Information Enablement → Client Administration → Person Identity Administration → Feature. These authored Composition contexts retain their own standing; no Home is inferred through them.

### Architectural Questions / Adjudication

The Solution Architect has supplied the intended Home explicitly. No further Realization/context interpretation is needed. New endpoint identity, bounded definition and acceptance remain with the corresponding original element proposals. The initial/default On-Hold call records that dependency, not uncertainty about the Home meaning. No new Disposition is issued on behalf of the Solution Architect.

**Current Solution Architect execution decision — 2026-10-19:** Accept, as recorded in the adjudicated summary and explicitly directed in the reconciliation task. Preserve the submitted bounded change and apply existing governance; the subsequent Product qualifier decision resolves the recorded Product vocabulary stop. The earlier On-Hold instruction and all prior reasons remain recoverable in the byte-identical pre-execution review.

### Related Change Requests

Original [run-001 CR-001 and CR-006–009](../run-001/change-review.md) govern the new endpoint proposals. Their accepted, evidenced governed state is required before reconciliation. Original Realization proposals CR-022/024/026/028 remain independent; interpretation findings CR-023/025/027/029 are Resolved by governing decision.

### Authorised Reconciliation Instruction

Reconcile the accepted native Association `id-ba8d543c3f6a4f15bd41e5c2d7bcf6cf` and its exact submitted scope from the frozen input. Establish the required reconciled elements/edges before dependent relationships or View appearances; preserve native types, endpoints, direction, specialisation and authorised layout. Apply only governed identity/naming and publication metadata. The exact source capture and binding are [accepted-ingestion-1.0.8.json](../../../../../markdown/governance/baseline/accepted-ingestion-1.0.8.json). No Home is inferred and no native Realization is replaced. This subsequent execution instruction is linked to the Solution Architect decision above; the original Not authorised instruction remains in the pre-execution review.

### Reconciliation / Processing Record

**PASS — reconciled.** [Execution result and dependency evidence](reconciliation/execution-001/results.json), record `CR-001`; [RAD-1.0.9 Open Exchange publication](../../../../exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml). Original inputs and frozen detection remain unchanged; prior processing statements remain in retained history.


<a id="cr-002"></a>

## CR-002 — Preserve authored Home: [Business Service] Identifier Update

**Disposition:** Accept

**Outcome:** Resolved

**Outcome Reason:** Resolved by the authorised 2026-10-19 reconciliation: the exact governed resulting state exists in RAD-1.0.9, with dependencies, canonical metadata, references and actual Archi 5.10 transport verified. See [the per-CR execution record](reconciliation/execution-001/results.json) (`CR-002`) and [publication verification](../../../publishing/1.0.9/reconciliation-verification.json). Original Disposition, Outcome and Outcome Reason remain unchanged in the [pre-execution review](reconciliation/execution-001/change-review-before.md). Human rendering inspection remains a separate gate.

### Proposed Architectural Change

Preserve the Solution Architect-authored `[Business Service] Identifier Update → directed Association «Home» → [Feature] Identifier Capture` as its own semantic assertion. Existing native relationships remain independent.

### Detected Evidence

Fresh input SHA-256 `366bbcf305575b831ea7e8e72e1c2b19f9631518fda8f297a4f7c51a959a5556`, relationship `id-a21144ee285f467982d13c39b49228dd`, line 95251; exact raw relationship and endpoint IDs are in [explicit-homes.json](detection/explicit-homes.json). Source `id-0088d61ccfcf4962b3aab8bc3b96021d` is native `BusinessService`; target `id-727a264026584f80bc3304ff8383dc92` is native `Capability`, recognised Feature. Native type Association; directed true; specialisation Home; Name absent. Context: Entity Information Enablement → Client Administration → Person Identity Administration → Feature. These authored Composition contexts retain their own standing; no Home is inferred through them.

### Architectural Questions / Adjudication

The Solution Architect has supplied the intended Home explicitly. No further Realization/context interpretation is needed. New endpoint identity, bounded definition and acceptance remain with the corresponding original element proposals. The initial/default On-Hold call records that dependency, not uncertainty about the Home meaning. No new Disposition is issued on behalf of the Solution Architect.

**Current Solution Architect execution decision — 2026-10-19:** Accept, as recorded in the adjudicated summary and explicitly directed in the reconciliation task. Preserve the submitted bounded change and apply existing governance; the subsequent Product qualifier decision resolves the recorded Product vocabulary stop. The earlier On-Hold instruction and all prior reasons remain recoverable in the byte-identical pre-execution review.

### Related Change Requests

Original [run-001 CR-001 and CR-006–009](../run-001/change-review.md) govern the new endpoint proposals. Their accepted, evidenced governed state is required before reconciliation. Original Realization proposals CR-022/024/026/028 remain independent; interpretation findings CR-023/025/027/029 are Resolved by governing decision.

### Authorised Reconciliation Instruction

Reconcile the accepted native Association `id-a21144ee285f467982d13c39b49228dd` and its exact submitted scope from the frozen input. Establish the required reconciled elements/edges before dependent relationships or View appearances; preserve native types, endpoints, direction, specialisation and authorised layout. Apply only governed identity/naming and publication metadata. The exact source capture and binding are [accepted-ingestion-1.0.8.json](../../../../../markdown/governance/baseline/accepted-ingestion-1.0.8.json). No Home is inferred and no native Realization is replaced. This subsequent execution instruction is linked to the Solution Architect decision above; the original Not authorised instruction remains in the pre-execution review.

### Reconciliation / Processing Record

**PASS — reconciled.** [Execution result and dependency evidence](reconciliation/execution-001/results.json), record `CR-002`; [RAD-1.0.9 Open Exchange publication](../../../../exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml). Original inputs and frozen detection remain unchanged; prior processing statements remain in retained history.


<a id="cr-003"></a>

## CR-003 — Preserve authored Home: [Business Service] Identifier Create

**Disposition:** Accept

**Outcome:** Resolved

**Outcome Reason:** Resolved by the authorised 2026-10-19 reconciliation: the exact governed resulting state exists in RAD-1.0.9, with dependencies, canonical metadata, references and actual Archi 5.10 transport verified. See [the per-CR execution record](reconciliation/execution-001/results.json) (`CR-003`) and [publication verification](../../../publishing/1.0.9/reconciliation-verification.json). Original Disposition, Outcome and Outcome Reason remain unchanged in the [pre-execution review](reconciliation/execution-001/change-review-before.md). Human rendering inspection remains a separate gate.

### Proposed Architectural Change

Preserve the Solution Architect-authored `[Business Service] Identifier Create → directed Association «Home» → [Feature] Identifier Capture` as its own semantic assertion. Existing native relationships remain independent.

### Detected Evidence

Fresh input SHA-256 `366bbcf305575b831ea7e8e72e1c2b19f9631518fda8f297a4f7c51a959a5556`, relationship `id-3f9d4356f4b84ef7b45fe3435d27fde8`, line 95258; exact raw relationship and endpoint IDs are in [explicit-homes.json](detection/explicit-homes.json). Source `id-fddda503b8f0438e995465cd93204326` is native `BusinessService`; target `id-727a264026584f80bc3304ff8383dc92` is native `Capability`, recognised Feature. Native type Association; directed true; specialisation Home; Name absent. Context: Entity Information Enablement → Client Administration → Person Identity Administration → Feature. These authored Composition contexts retain their own standing; no Home is inferred through them.

### Architectural Questions / Adjudication

The Solution Architect has supplied the intended Home explicitly. No further Realization/context interpretation is needed. New endpoint identity, bounded definition and acceptance remain with the corresponding original element proposals. The initial/default On-Hold call records that dependency, not uncertainty about the Home meaning. No new Disposition is issued on behalf of the Solution Architect.

**Current Solution Architect execution decision — 2026-10-19:** Accept, as recorded in the adjudicated summary and explicitly directed in the reconciliation task. Preserve the submitted bounded change and apply existing governance; the subsequent Product qualifier decision resolves the recorded Product vocabulary stop. The earlier On-Hold instruction and all prior reasons remain recoverable in the byte-identical pre-execution review.

### Related Change Requests

Original [run-001 CR-001 and CR-006–009](../run-001/change-review.md) govern the new endpoint proposals. Their accepted, evidenced governed state is required before reconciliation. Original Realization proposals CR-022/024/026/028 remain independent; interpretation findings CR-023/025/027/029 are Resolved by governing decision.

### Authorised Reconciliation Instruction

Reconcile the accepted native Association `id-3f9d4356f4b84ef7b45fe3435d27fde8` and its exact submitted scope from the frozen input. Establish the required reconciled elements/edges before dependent relationships or View appearances; preserve native types, endpoints, direction, specialisation and authorised layout. Apply only governed identity/naming and publication metadata. The exact source capture and binding are [accepted-ingestion-1.0.8.json](../../../../../markdown/governance/baseline/accepted-ingestion-1.0.8.json). No Home is inferred and no native Realization is replaced. This subsequent execution instruction is linked to the Solution Architect decision above; the original Not authorised instruction remains in the pre-execution review.

### Reconciliation / Processing Record

**PASS — reconciled.** [Execution result and dependency evidence](reconciliation/execution-001/results.json), record `CR-003`; [RAD-1.0.9 Open Exchange publication](../../../../exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml). Original inputs and frozen detection remain unchanged; prior processing statements remain in retained history.


<a id="cr-004"></a>

## CR-004 — Preserve authored Home: [Business Service] Verify Identity Source

**Disposition:** Accept

**Outcome:** Resolved

**Outcome Reason:** Resolved by the authorised 2026-10-19 reconciliation: the exact governed resulting state exists in RAD-1.0.9, with dependencies, canonical metadata, references and actual Archi 5.10 transport verified. See [the per-CR execution record](reconciliation/execution-001/results.json) (`CR-004`) and [publication verification](../../../publishing/1.0.9/reconciliation-verification.json). Original Disposition, Outcome and Outcome Reason remain unchanged in the [pre-execution review](reconciliation/execution-001/change-review-before.md). Human rendering inspection remains a separate gate.

### Proposed Architectural Change

Preserve the Solution Architect-authored `[Business Service] Verify Identity Source → directed Association «Home» → Identity Source & Correction Lineage` as its own semantic assertion. Existing native relationships remain independent.

### Detected Evidence

Fresh input SHA-256 `366bbcf305575b831ea7e8e72e1c2b19f9631518fda8f297a4f7c51a959a5556`, relationship `id-d559bbf53430465e90f47c40593403a5`, line 95265; exact raw relationship and endpoint IDs are in [explicit-homes.json](detection/explicit-homes.json). Source `id-9bfedbfd5b404694a2224c039fa5d4c4` is native `BusinessService`; target `id-bdf18b5e580b5b85a34357a3b21f36db` is native `Capability`, recognised Feature. Native type Association; directed true; specialisation Home; Name absent. Context: Entity Information Enablement → Client Administration → Person Identity Administration → Feature. These authored Composition contexts retain their own standing; no Home is inferred through them.

### Architectural Questions / Adjudication

The Solution Architect has supplied the intended Home explicitly. No further Realization/context interpretation is needed. New endpoint identity, bounded definition and acceptance remain with the corresponding original element proposals. The initial/default On-Hold call records that dependency, not uncertainty about the Home meaning. No new Disposition is issued on behalf of the Solution Architect.

**Current Solution Architect execution decision — 2026-10-19:** Accept, as recorded in the adjudicated summary and explicitly directed in the reconciliation task. Preserve the submitted bounded change and apply existing governance; the subsequent Product qualifier decision resolves the recorded Product vocabulary stop. The earlier On-Hold instruction and all prior reasons remain recoverable in the byte-identical pre-execution review.

### Related Change Requests

Original [run-001 CR-001 and CR-006–009](../run-001/change-review.md) govern the new endpoint proposals. Their accepted, evidenced governed state is required before reconciliation. Original Realization proposals CR-022/024/026/028 remain independent; interpretation findings CR-023/025/027/029 are Resolved by governing decision.

### Authorised Reconciliation Instruction

Reconcile the accepted native Association `id-d559bbf53430465e90f47c40593403a5` and its exact submitted scope from the frozen input. Establish the required reconciled elements/edges before dependent relationships or View appearances; preserve native types, endpoints, direction, specialisation and authorised layout. Apply only governed identity/naming and publication metadata. The exact source capture and binding are [accepted-ingestion-1.0.8.json](../../../../../markdown/governance/baseline/accepted-ingestion-1.0.8.json). No Home is inferred and no native Realization is replaced. This subsequent execution instruction is linked to the Solution Architect decision above; the original Not authorised instruction remains in the pre-execution review.

### Reconciliation / Processing Record

**PASS — reconciled.** [Execution result and dependency evidence](reconciliation/execution-001/results.json), record `CR-004`; [RAD-1.0.9 Open Exchange publication](../../../../exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml). Original inputs and frozen detection remain unchanged; prior processing statements remain in retained history.


<a id="cr-005"></a>

## CR-005 — Preserve added Home connections in CR-035 View

**Disposition:** Accept

**Outcome:** Resolved

**Outcome Reason:** Resolved by the authorised 2026-10-19 reconciliation: the exact governed resulting state exists in RAD-1.0.9, with dependencies, canonical metadata, references and actual Archi 5.10 transport verified. See [the per-CR execution record](reconciliation/execution-001/results.json) (`CR-005`) and [publication verification](../../../publishing/1.0.9/reconciliation-verification.json). Original Disposition, Outcome and Outcome Reason remain unchanged in the [pre-execution review](reconciliation/execution-001/change-review-before.md). Human rendering inspection remains a separate gate.

### Proposed Architectural Change

Preserve only the added Home connection appearances in View `id-216c4ab0757b45b3894e7e0c2594b9f9`; reuse their explicit semantic Home edges. This supplies no additional semantic Home.

### Detected Evidence

The updated input adds three Home connection(s) to this existing View. All previous nodes and connections, coordinates, names and View metadata remain unchanged. [Evidence](detection/explicit-homes.json) records the changed View ID; the byte-identical compressed snapshot preserves every connection ID and endpoint.

### Architectural Questions / Adjudication

No View placement can establish Home. The exact authored Home relationships already exist. View acceptance and endpoint reconciliation require their independently governed state.

**Current Solution Architect execution decision — 2026-10-19:** Accept, as recorded in the adjudicated summary and explicitly directed in the reconciliation task. Preserve the submitted bounded change and apply existing governance; the subsequent Product qualifier decision resolves the recorded Product vocabulary stop. The earlier On-Hold instruction and all prior reasons remain recoverable in the byte-identical pre-execution review.

### Related Change Requests

[run-001 CR-035](../run-001/change-review.md#cr-035) retains the original View proposal; this run records only its updated-input delta. Related Home proposals above must have evidenced governed endpoints before replay.

### Authorised Reconciliation Instruction

Reconcile the accepted native Diagram `id-216c4ab0757b45b3894e7e0c2594b9f9` and its exact submitted scope from the frozen input. Establish the required reconciled elements/edges before dependent relationships or View appearances; preserve native types, endpoints, direction, specialisation and authorised layout. Apply only governed identity/naming and publication metadata. The exact source capture and binding are [accepted-ingestion-1.0.8.json](../../../../../markdown/governance/baseline/accepted-ingestion-1.0.8.json). No Home is inferred and no native Realization is replaced. This subsequent execution instruction is linked to the Solution Architect decision above; the original Not authorised instruction remains in the pre-execution review.

### Reconciliation / Processing Record

**PASS — reconciled.** [Execution result and dependency evidence](reconciliation/execution-001/results.json), record `CR-005`; [RAD-1.0.9 Open Exchange publication](../../../../exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml). Original inputs and frozen detection remain unchanged; prior processing statements remain in retained history.


<a id="cr-006"></a>

## CR-006 — Preserve added Home connections in CR-036 View

**Disposition:** Accept

**Outcome:** Resolved

**Outcome Reason:** Resolved by the authorised 2026-10-19 reconciliation: the exact governed resulting state exists in RAD-1.0.9, with dependencies, canonical metadata, references and actual Archi 5.10 transport verified. See [the per-CR execution record](reconciliation/execution-001/results.json) (`CR-006`) and [publication verification](../../../publishing/1.0.9/reconciliation-verification.json). Original Disposition, Outcome and Outcome Reason remain unchanged in the [pre-execution review](reconciliation/execution-001/change-review-before.md). Human rendering inspection remains a separate gate.

### Proposed Architectural Change

Preserve only the added Home connection appearances in View `id-8fb2f8b1776f4ebf969e9b5b638e4ed3`; reuse their explicit semantic Home edges. This supplies no additional semantic Home.

### Detected Evidence

The updated input adds one Home connection(s) to this existing View. All previous nodes and connections, coordinates, names and View metadata remain unchanged. [Evidence](detection/explicit-homes.json) records the changed View ID; the byte-identical compressed snapshot preserves every connection ID and endpoint.

### Architectural Questions / Adjudication

No View placement can establish Home. The exact authored Home relationships already exist. View acceptance and endpoint reconciliation require their independently governed state.

**Current Solution Architect execution decision — 2026-10-19:** Accept, as recorded in the adjudicated summary and explicitly directed in the reconciliation task. Preserve the submitted bounded change and apply existing governance; the subsequent Product qualifier decision resolves the recorded Product vocabulary stop. The earlier On-Hold instruction and all prior reasons remain recoverable in the byte-identical pre-execution review.

### Related Change Requests

[run-001 CR-036](../run-001/change-review.md#cr-036) retains the original View proposal; this run records only its updated-input delta. Related Home proposals above must have evidenced governed endpoints before replay.

### Authorised Reconciliation Instruction

Reconcile the accepted native Diagram `id-8fb2f8b1776f4ebf969e9b5b638e4ed3` and its exact submitted scope from the frozen input. Establish the required reconciled elements/edges before dependent relationships or View appearances; preserve native types, endpoints, direction, specialisation and authorised layout. Apply only governed identity/naming and publication metadata. The exact source capture and binding are [accepted-ingestion-1.0.8.json](../../../../../markdown/governance/baseline/accepted-ingestion-1.0.8.json). No Home is inferred and no native Realization is replaced. This subsequent execution instruction is linked to the Solution Architect decision above; the original Not authorised instruction remains in the pre-execution review.

### Reconciliation / Processing Record

**PASS — reconciled.** [Execution result and dependency evidence](reconciliation/execution-001/results.json), record `CR-006`; [RAD-1.0.9 Open Exchange publication](../../../../exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml). Original inputs and frozen detection remain unchanged; prior processing statements remain in retained history.
