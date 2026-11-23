Task-Issued-At: 2026-11-23T02:42:04.971Z

Event source: Codex interaction rollout-2026-11-22T22-04-06-01a202ed-72a2-75e3-a092-1706fc2be364.jsonl, record 451, timestamp of the user-role response_item containing the operative instruction entering the session.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Detail — G03 Harmonia Processing Framework and Checkpoints · Draft.000.003

- **Direction** — The user issued the bounded consolidation below and identified its adjudications as Solution Architect direction for Draft.000.003.
- **Target** — Software 0.1.0, G03 evidence Iteration 005, cumulative [HCRL.004 DR-12](../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12), against Architecture 1.1.26.
- **Source state** — `feature/BL-CALLIOPE-OO-01` at `3588f5f8df1fbe65d64e8635a3a6a378bf19360c`, matching the local tracking ref. The index was clean. The worktree held the earlier DR-12 continuations, both preceding drafts and three prior Task/Report pairs.

## Operative issued instruction

````
# Codex Assignment — Processing Framework and Checkpoints · Draft.000.003

## 1. Objective

Create:

`enterprise-architecture/design/offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.003.md`

Using:

`enterprise-architecture/design/offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.002.md`

Incorporate the Solution Architect's subsequent adjudications concerning:

- WorkSpace information categories and immutability.
- Ergon-owned processing status.
- Context Unloader responsibilities.
- The lifecycle of processing status.
- Ponos-owned Praxis processing and concurrency status.
- Praxis.Server / Praxis.Agent interactions.
- Pragma metadata status.
- Egress filtering and dead-task blocking.

**This is a bounded consolidation of agreed architectural decisions.**

Do not initiate another broad architectural investigation.

Preserve Draft.000.002 unchanged.

## 2. Review context

Accepted Architecture baseline: **1.1.26**

Software version: **0.1.0**

Review: **G03 / Iteration 005 / DR-12**

Cumulative HCRL:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

Use the preceding processing-framework DRP and reconciliation evidence as the primary sources.

Consult additional governed sources only where necessary to identify a genuine architectural conflict.

Confirm the repository branch, HEAD and worktree state before editing.

Preserve existing uncommitted work.

## 3. WorkSpace information model

Explicitly document three distinct categories of information within the Ergon WorkSpace.

### Category 1 — Context-loaded information

InformationUnits loaded by `ergon.context-load` to establish the Ergon's initial working context.

These InformationUnits are **immutable within the Ergon Processor**.

### Category 2 — Explicitly retrieved information

InformationUnits retrieved explicitly by the Ergon's business logic during processing.

These InformationUnits are also **immutable within the Ergon Processor**.

### Category 3 — Processor-generated information

Information generated during execution comprises:

1. Newly created InformationUnits placed into the WorkSpace's `.output()` collection.
2. `Workspace.Processor.Status`.

These are distinct kinds of generated information and must not be conflated.

The first two categories are existing governed information used by the Processor.

The third contains information produced by the Processor.

**Do not describe Context Unloader as persisting modifications to Category 1 or Category 2 InformationUnits.**

Do not introduce mutable copies, implicit updates, or replacement semantics without explicit architectural authority.

## 4. Three status domains

Document three distinct status domains.

| Status domain | Semantic owner | Persistence and availability |
|---|---|---|
| `Workspace.Processor.Status` | Ergon | Mneme |
| `Pathway.Processing.Status` | Ponos | Mneme |
| `Pragma.Metadata.Status` | Ergon or Ponos — detailed allocation unresolved | Mneme |

Mneme manages the persistence and availability of the status information.

That responsibility does not confer semantic ownership of the status or the decisions that change it.

Preserve the distinct meanings, responsibilities and lifecycles of the three domains.

Do not introduce one universal status model or assume that the domains can be merged.

## 5. Workspace.Processor.Status

`Workspace.Processor.Status` is **owned by the Ergon**.

It represents processing information produced during execution.

It is not merely diagnostic information, and it must not be reduced to a simple execution outcome.

It may contain substantial transitive processing details.

These details are particularly significant for the eventual DigitalTwin-Behaviour design.

### 5.1 Business-defined branch selection

The branch-selection rules are defined within the **business logic of the Ergon itself**.

The common Ergon processing framework does not define those rules.

The Context Unloader uses `Workspace.Processor.Status` and the Ergon-defined branch-selection information to determine the applicable content-release branches.

The status can therefore influence:

- Which generated content is released for further processing.
- How generated information is prepared.
- Which references are incorporated into generated InformationUnits.

### 5.2 Reference-based information representation

Context Unloader uses the status and the applicable branch rules to ensure that the generated InformationUnits incorporate appropriate **References rather than embedding actual data**, where that is the intended information representation.

Preserve the distinction between preparing content and establishing a governed InformationUnit.

Do not prescribe a new information representation model or change the accepted InformationUnit framework.

## 6. Context Unloader — Revised responsibility

The agreed processing stage remains:

`ergon.egress.context-unloader`

Its responsibilities include:

1. Interpreting the Ergon-owned `Workspace.Processor.Status`.
2. Applying the branch-selection rules defined by that Ergon's business logic.
3. Selecting the applicable generated-content release branches.
4. Preparing appropriate reference relationships for generated outputs.
5. Closing off the Ergon's processing WorkSpace.

The Context Unloader performs these responsibilities within the existing Ergon execution framework.

It must not independently invent business-specific branch rules.

It must not assume ownership of Ponos's Praxis processing state.

It must not modify existing context-loaded or explicitly retrieved InformationUnits.

### Distinction from Output Unload/Validate

Retain the following two separate stages:

**`ergon.egress.context-unloader`**

Prepares the generated information for release using Ergon-defined processing status and branch-selection semantics.

**`ergon.egress.output-unload/validate`**

Validates and establishes discrete output InformationUnits through the applicable governed Mneme contracts.

Do not merge these functions.

Do not reintroduce an obligation to persist modifications to immutable input IUs.

## 7. Workspace.Processor.Status lifecycle

Record the following lifecycle.

| Phase | Responsibility |
|---|---|
| Creation and maintenance | Ergon business logic produces and maintains `Workspace.Processor.Status` during processing. |
| Operational persistence and availability | Mneme provides the required persistence and availability of status information. |
| Context unloading | Context Unloader consumes the status for branch selection and release preparation. |
| Processing-instance closure | The status completes its operational lifecycle following closure of the processing instance for a particular Pragma. |
| Final disposition | The status information ends its life in an AuditEvent associated with that processing-instance closure. |

The AuditEvent captures the relevant processing status following closure of the processing instance for the applicable Pragma.

Preserve **Kleio's responsibility for authoritative AuditEvent construction and preservation**.

Do not equate the status object itself with the AuditEvent.

Do not presume that the complete WorkSpace, input InformationUnits or generated output content must be copied into the AuditEvent.

The exact audit representation, capture interaction and timing are matters for the applicable audit integration design.

The lifecycle must preserve the Solution Architect's direction that the status information ultimately resides in an AuditEvent.

## 8. Pathway.Processing.Status

`Pathway.Processing.Status` is **owned by Ponos**, as the owner of Praxis processing.

It incorporates:

- Praxis processing and progression status.
- **Concurrency information and coordination details** associated with Praxis processing.

Mneme manages its persistence and availability.

This is a functional orchestration responsibility.

**Do not recast Praxis concurrency as an HA/DR design question.**

Do not introduce distributed locking, fencing, replication strategies, recovery algorithms or additional coordination mechanisms into this DRP.

## 9. Praxis.Server / Praxis.Agent

Record the Solution Architect's proposed Ponos architecture.

### Praxis.Server (Manager)

A Ponos-owned Praxis management component responsible for managing Praxis processing.

Its responsibilities include the applicable processing coordination and concurrency semantics.

### Praxis.Agent

A Ponos-owned component embedded in each **Ergon-Execution-Module**.

The Ergon execution framework interacts with its local Praxis.Agent for the relevant Praxis processing activities.

The Agent provides the local interaction boundary to the Ponos-owned Praxis management functionality.

The exact interface and detailed division of responsibility between Praxis.Server and Praxis.Agent remain to be defined.

**Do not introduce a new independent orchestration engine.**

Do not move Praxis ownership from Ponos to Ergon.

Do not prescribe a transport, concurrency algorithm or deployment topology.

This is a logical architectural responsibility model.

## 10. Pragma.Metadata.Status

Preserve the existence of:

`Pragma.Metadata.Status`

Its semantic ownership is **Ergon or Ponos**, depending on the relevant status responsibility.

The exact allocation of status transitions remains unresolved.

Mneme manages persistence and availability.

Do not assign all Pragma metadata transitions to:

- Context Unloader.
- Outcome Decision.
- Praxis.Agent.
- Praxis.Server.
- Mneme.

Do not invent a Pragma lifecycle state machine.

Record the allocation of individual transitions as a subsequent design question.

## 11. Egress Filter — Explicit adjudication

The Solution Architect has confirmed:

**Dead-task blocking belongs within `ergon.egress.filter`, alongside PublishingMask evaluation.**

The filter therefore performs two distinct checks:

1. **Dead-task blocking:** Exclude content or work that must not proceed because of the applicable task state.
2. **PublishingMask evaluation:** Determine the publication eligibility of the remaining output InformationUnits.

Preserve the separate semantics and reasons for filtering.

A PublishingMask non-match does not invalidate the existence or governance of the underlying IU.

The exact status consequences of dead-task blocking must follow the applicable Pragma and Praxis contracts.

Do not invent new status transitions.

The subsequent outbound PEP remains responsible for **Themis authorisation**, not publication eligibility or dead-task blocking.

This adjudication resolves the previously outstanding placement question.

## 12. Preserve the agreed processing route

Do not change the sequence or add processing stages.

```java id="s5hjdj"
from(ergon.ingress.queueId)
    .checkpoint()
    .process(ergon.ingress.pep)
    .checkpoint()
    .process(ergon.context-load)
    .checkpoint()
    .process(ergon.processor)
    .checkpoint()
    .process(ergon.egress.context-unloader)
    .checkpoint()
    .process(ergon.egress.output-unload/validate)
    .checkpoint()
    .process(ergon.egress.outcome-decision)
    .checkpoint()
    .process(ergon.egress.filter)
    .checkpoint()
    .process(ergon.egress.pep)
    .checkpoint()
    .process(ergon.egress.pragma-create)
    .checkpoint()
    .process(petasos.route-in)
    .to(checkpoint());
```

This remains architectural pseudocode.

Retain the horizontal H01–H11 Checkpoint representation.

Update the functional descriptions and supporting interactions, not the route structure.

The following decisions remain unchanged:

- Ingress and egress PEP ownership.
- Mneme's PragmaFactory is invoked internally by `ergon.egress.pragma-create`.
- Only successfully established originating Pragmas are handed to Petasos.
- Checkpoint execution is synchronous.
- Checkpoint persistence is independently configurable within mandatory governance requirements.
- Independent Assurance remains with Dokimasia.

## 13. Preserve the three-tier architecture

Retain the existing model:

1. **Intra-Module Orchestration**
2. **Inter-Module Orchestration**
3. **OA&M Oversight**

The first tier remains the primary subject of this DRP.

Tier 2 incorporates Praxis/Ponos, Petasos and publication/subscription-driven progression.

Tier 3 incorporates operational observation, audit and independent Assurance.

Do not broaden this revision into detailed designs for DigitalTwin-Behaviour, Pylai, Iris or Dokimasia.

## 14. Reconciliation and open matters

Update the DRP's reconciliation and decision registers to reflect the new adjudications.

Explicitly identify as resolved:

- WorkSpace information categories.
- Immutability of context-loaded and explicitly retrieved IUs.
- Ergon ownership of `Workspace.Processor.Status`.
- Context Unloader's branch-selection and reference-preparation responsibilities.
- The AuditEvent final disposition of processing-instance status.
- Ponos ownership of `Pathway.Processing.Status`, including concurrency information.
- The Praxis.Server / embedded Praxis.Agent conceptual model.
- Dead-task blocking within Egress Filter.

Explicitly retain as unresolved:

- Detailed allocation of `Pragma.Metadata.Status` transitions.
- Exact Praxis.Server / Praxis.Agent interfaces.
- Detailed processing-status audit representation and capture interaction.
- Existing PF-07/PF-08 interaction obligations.
- Detailed checkpoint configuration, evidence and implementation contracts.
- Other genuinely unresolved G03 matters carried forward from Draft.000.002.

Do not reclassify implementation questions as new architectural blockers.

## 15. HCRL and evidence

Make a **bounded additive update** to DR-12 in:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

Record Draft.000.003 and the specific Solution Architect adjudications.

Preserve all previous review history and outstanding obligations.

Create the corresponding Task and Report evidence under:

`enterprise-architecture/design/evidence/software-0.1.0/005/`

Follow existing repository evidence conventions.

## 16. Verification and constraints

Verify that:

- Draft.000.002 remains unchanged.
- Draft.000.003 preserves the agreed processing sequence.
- H01–H11 remain correctly positioned.
- WorkSpace categories and immutability are explicit.
- All three status domains remain distinct.
- Semantic ownership and Mneme persistence responsibility are separate.
- Context Unloader consumes Ergon-defined branch-selection information.
- The processing-status lifecycle ends in an AuditEvent.
- Praxis concurrency remains a Ponos-owned functional responsibility.
- Dead-task blocking is allocated to Egress Filter.
- Existing security, audit, durability and Assurance responsibilities remain intact.
- No new framework or unnecessary architectural construct has been introduced.

Perform bounded documentation checks appropriate to the changes.

Report unrelated pre-existing validation failures without attempting corrective work.

**Do not:**

- Implement software.
- Modify the Architecture Continuum.
- Modify accepted baselines.
- Create additional DRPs.
- Invent canonical interfaces or status data types.
- Reopen previously accepted decisions.
- Commit, tag or push.
- Mark the DRP or HCRL accepted.

## 17. Deliverables

Return:

1. `DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.003.md`
2. A concise reconciliation/execution report.
3. The bounded HCRL.004 / DR-12 amendment.

**Primary objective:** Consolidate the agreed processing architecture, WorkSpace information model, status ownership and lifecycle, and Egress Filter responsibilities into Draft.000.003 without expanding the design scope.
````
