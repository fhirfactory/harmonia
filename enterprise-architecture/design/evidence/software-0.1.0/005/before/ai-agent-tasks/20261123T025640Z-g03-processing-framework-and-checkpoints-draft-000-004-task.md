Task-Issued-At: 2026-11-23T02:56:40.877Z

Event source: Codex interaction rollout-2026-11-22T22-04-06-01a202ed-72a2-75e3-a092-1706fc2be364.jsonl, record 549, timestamp of the user-role response_item containing the operative instruction entering the session.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Detail — G03 Harmonia Processing Framework and Checkpoints · Draft.000.004

- **Direction** — The user issued the bounded final-review preparation below and identified its adjudications as Solution Architect direction for Draft.000.004.
- **Target** — Software candidate 0.1.0, G03 evidence Iteration 005, cumulative [HCRL.004 DR-12](../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12), against Architecture 1.1.26.
- **Source state** — `feature/BL-CALLIOPE-OO-01` at `3588f5f8df1fbe65d64e8635a3a6a378bf19360c`, matching the local tracking ref. The index was clean. The worktree held the prior DR-12 continuations, three earlier drafts and four preceding Task/Report pairs.

## Operative issued instruction

````
# Codex Assignment — Processing Framework and Checkpoints · Draft.000.004

## 1. Objective

Create:

`enterprise-architecture/design/offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.004.md`

Using:

`enterprise-architecture/design/offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.003.md`

Incorporate the Solution Architect's final adjudications concerning:

1. The WorkSpace-to-Egress information-transfer boundary.
2. Pass-by-value semantics for the entire Pragma.
3. The distinction between the Egress Pragma and newly created originating Pragmas.
4. The final AuditEvent disposition of `Workspace.Processor.Status`.

**This is a bounded revision intended to prepare the DRP for final architectural review.**

Preserve Draft.000.003 unchanged.

Do not conduct another broad repository investigation.

## 2. Architectural and review context

- Accepted Architecture baseline: **1.1.26**
- Software candidate: **0.1.0**
- Review: **G03 / Iteration 005 / DR-12**
- Existing HCRL: `enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

Use Draft.000.003 and its existing reconciliation evidence as the primary source material.

Consult additional governed sources only where a specific inconsistency requires verification.

Confirm the current repository branch, HEAD and working-tree state before editing.

Preserve all existing work.

## 3. WorkSpace-to-Egress boundary

The Solution Architect has established that **the entire Pragma is passed by value out of the WorkSpace and through the Camel Exchange on the Egress side**.

This supersedes the earlier narrower interpretation that only the output collection was transferred.

The logical progression is:

```text
Ergon.Process
     │
     │  WorkSpace
     │
     ▼
Context Unloader
     │
     ├── Uses Workspace.Processor.Status
     ├── Applies Ergon-defined branch-selection rules
     ├── Prepares appropriate IU References
     ├── Releases the Pragma by value
     └── Closes the WorkSpace
     │
     ▼
Camel Exchange
     │
     │  Pragma (by value)
     │
     ▼
Output Unload/Validate
     │
     ▼
Outcome Decision
     │
     ▼
Egress Filter
     │
     ▼
Egress PEP
     │
     ▼
Pragma Create
     │
     ▼
Petasos Route-In
```

The existing Checkpoints remain between the individual processing functions.

The diagram above illustrates information flow, not a replacement Camel route.

### Required architectural statements

Record explicitly:

- The Context Unloader is responsible for the WorkSpace-to-Egress handover.
- The entire Pragma is passed by value through the Camel Exchange.
- This handover establishes the Egress processing context without introducing another WorkSpace or information container.
- Subsequent Egress functions operate on the Pragma carried by the Exchange.
- The original WorkSpace need not remain accessible to those functions.
- The Camel Exchange is an intra-module transfer mechanism, not authoritative information persistence.

Do not prescribe serialization, copying strategies or Java object-lifecycle mechanics.

Pass-by-value is the required architectural transfer semantics, irrespective of the eventual implementation technique.

## 4. Context Unloader responsibilities

Retain the responsibilities established in Draft.000.003.

`ergon.egress.context-unloader`:

1. Consumes Ergon-owned `Workspace.Processor.Status`.
2. Applies branch-selection rules defined by the Ergon's business logic.
3. Selects the applicable generated-content release branches.
4. Prepares the generated content and its appropriate IU References.
5. Releases the entire Pragma by value into the Camel Exchange.
6. Closes off the WorkSpace.

The first two WorkSpace information categories remain immutable within the Processor:

- Context-loaded IUs.
- Explicitly retrieved IUs.

The Context Unloader must not be described as persisting modifications to these existing InformationUnits.

Newly generated InformationUnits remain represented within the output information carried by the Egress Pragma until they are established under the applicable governed contracts.

Do not change the accepted InformationUnit representation or reference model.

## 5. Egress Pragma versus originating Pragmas

Preserve a clear distinction between the two roles.

### Egress Pragma

The Pragma transferred from the WorkSpace into the Camel Exchange.

It carries the information required by the remaining Egress processing functions, including the generated output information and relevant execution context.

Its transfer is pass-by-value.

### Newly created originating Pragmas

The originating Pragmas constructed subsequently by:

`ergon.egress.pragma-create`

This function invokes Mneme's PragmaFactory internally.

The eligible and authorised output IU identities provide the basis for originating Pragma construction.

Mneme applies the appropriate governed construction and persistence contracts.

Only successfully established originating Pragma identities proceed to Petasos Route-In.

**The Egress Pragma and newly created originating Pragmas must not be treated as the same work identity merely because they share the Pragma information model.**

Do not create another Camel processing stage to represent the PragmaFactory invocation.

Do not invent new canonical Pragma types or classes.

If existing governed terminology provides a more precise distinction, cite it while preserving the Solution Architect's intended separation.

## 6. Output Unload/Validate

Retain:

`ergon.egress.output-unload/validate`

This function operates on the pass-by-value Pragma supplied through the Camel Exchange.

It:

- Extracts the applicable generated output information.
- Validates the generated InformationUnits.
- Establishes discrete governed IUs through Mneme.
- Obtains the required creation and persistence outcomes.

It must not require access to the closed WorkSpace.

The function remains distinct from Context Unloader.

## 7. Workspace.Processor.Status lifecycle

The Solution Architect has explicitly established:

**`Workspace.Processor.Status` ends its lifecycle in an AuditEvent following closure of the processing instance for the applicable Pragma.**

Record that statement without weakening its meaning.

The lifecycle remains:

| Phase | Responsibility |
|---|---|
| Creation and maintenance | Ergon business logic |
| Operational persistence and availability | Mneme |
| Release preparation | Context Unloader consumes status and Ergon-defined branch-selection rules |
| Processing-instance closure | Operational status lifecycle concludes |
| Final disposition | Status is captured in an AuditEvent associated with closure of that processing instance |

Kleio retains responsibility for authoritative AuditEvent construction and preservation.

Do not silently replace the whole status with an unspecified subset described merely as *relevant status information*.

The exact AuditEvent representation remains an audit integration design matter. Where appropriate, governed references may be used rather than copying substantial underlying content.

Preserve the distinction between:

- Ergon-owned operational status.
- Mneme-managed persistence and availability.
- Kleio-owned authoritative audit capture.
- Dokimasia-owned independent Assurance.

Do not introduce another status repository or audit component.

## 8. Preserve the agreed processing route

The processing route is unchanged.

```java
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

Preserve the single horizontal H01–H11 processing diagram.

Do not add, remove, reorder or rename processing stages.

Preserve the previously agreed internal allocation of:

- Dead-task blocking and PublishingMask evaluation to Egress Filter.
- Themis enforcement to Egress PEP.
- Originating Pragma construction to Pragma Create.
- Petasos routing handover to Petasos Route-In.

## 9. Preserve the three status domains

Retain the distinct status domains:

| Status domain | Semantic owner | Persistence/availability |
|---|---|---|
| `Workspace.Processor.Status` | Ergon | Mneme |
| `Pathway.Processing.Status` | Ponos | Mneme |
| `Pragma.Metadata.Status` | Ergon or Ponos, allocation unresolved | Mneme |

`Pathway.Processing.Status` includes the functional concurrency details of Praxis orchestration.

Preserve the Ponos-owned Praxis.Server (Manager) and embedded Praxis.Agent architecture.

Do not introduce HA/DR mechanisms, distributed coordination algorithms or implementation details.

## 10. Preserve the checkpoint architecture

Retain the Draft.000.003 checkpoint model unchanged except where clarification is necessary to explain pass-by-value Egress handovers.

Specifically:

- Every functional handover has a synchronous Checkpoint.
- Checkpoint execution is mandatory at the defined handover.
- Checkpoint-state persistence depends on the applicable policy and mandatory recovery requirements.
- Logging, audit and persistence granularity remain independently configurable.
- The candidate levels remain `COARSE | MEDIUM | FINE | EXTREME`.
- Mneme manages relevant housekeeping and governed status availability.
- Kleio owns authoritative audit capture.
- Dokimasia owns independent Assurance.

The checkpoint between Context Unloader and Output Unload/Validate receives the pass-by-value Egress Pragma through the Camel Exchange.

No additional intermediate persistent information store is implied.

## 11. Preserve the three-tier orchestration model

Retain the existing:

1. Intra-Module Orchestration.
2. Inter-Module Orchestration.
3. OA&M Oversight.

Do not expand the designs of DigitalTwin-Behaviour, Pylai, Iris or Dokimasia.

Retain the existing responsibility boundaries.

## 12. Reconciliation and review standing

Update only the relevant reconciliation entries.

Record as resolved for this DRP candidate:

- WorkSpace-to-Egress transfer semantics.
- Pass-by-value transfer of the entire Pragma.
- Context Unloader's handover responsibility.
- Independence of Egress processing from the closed WorkSpace.
- Distinction between the Egress Pragma and newly created originating Pragmas.
- Final AuditEvent disposition of `Workspace.Processor.Status`.

Retain outstanding detailed matters in their appropriate owning reviews, including:

- `Pragma.Metadata.Status` transition ownership.
- Petasos acknowledgement and routing responsibility.
- PF-07 and remaining PF-08 detailed contracts.
- TaskOutcome correspondence.
- Detailed Checkpoint implementation and evidence contracts.
- AuditEvent representation and capture interaction.
- Praxis.Server / Praxis.Agent interfaces.
- Other previously recorded G03 qualifications.

Do not treat those outstanding matters as failures of the overall processing framework unless a direct architectural contradiction is identified.

## 13. HCRL and evidence

Make a bounded additive update to:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

Under **DR-12**, record:

- Draft.000.004.
- The pass-by-value Egress Pragma decision.
- Context Unloader's transfer responsibility.
- AuditEvent lifecycle clarification.
- Preserved outstanding obligations.

Preserve all previous review history and dispositions.

Create the corresponding Task and Report evidence under:

`enterprise-architecture/design/evidence/software-0.1.0/005/`

Follow existing evidence conventions.

## 14. Verification

Perform bounded checks confirming:

- Draft.000.003 remains unchanged.
- The Camel route and H01–H11 sequence remain unchanged.
- Pass-by-value is explicitly associated with the entire Egress Pragma.
- The Egress functions do not require the closed WorkSpace.
- The Egress Pragma and newly created originating Pragmas are distinguished.
- No additional component or processing stage has been introduced.
- The status lifecycle ends in an AuditEvent.
- All three status domains remain distinct.
- Previous architectural decisions and G03 qualifications are preserved.
- Updated documentation references resolve.

Report unrelated existing validation failures without attempting corrective changes.

Do not:

- Implement software.
- Modify the Architecture Continuum.
- Change accepted baselines.
- Create additional DRPs.
- Invent canonical Java APIs or information types.
- Commit, tag or push.
- Mark the candidate accepted.

## 15. Deliverables

Return:

1. `DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.004.md`
2. A concise reconciliation/execution report.
3. The bounded HCRL.004 / DR-12 amendment.

**Primary objective:** Produce a final-review-ready processing-framework candidate incorporating the complete pass-by-value Pragma Egress boundary and definitive processing-status AuditEvent disposition, without reopening settled architectural matters.
````
