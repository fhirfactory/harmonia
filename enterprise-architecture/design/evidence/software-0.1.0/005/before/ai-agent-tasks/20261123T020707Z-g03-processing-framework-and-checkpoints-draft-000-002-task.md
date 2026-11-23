Task-Issued-At: 2026-11-23T02:07:07.445Z

Event source: Codex interaction rollout-2026-11-22T22-04-06-01a202ed-72a2-75e3-a092-1706fc2be364.jsonl, record 307, timestamp of the user-role response_item containing the operative instruction entering the session.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Detail — G03 Harmonia Processing Framework and Checkpoints · Draft.000.002

- **Direction** — The user issued the bounded revision below and identified its sequence and functional decisions as Solution Architect direction for the candidate.
- **Target** — Software 0.1.0, G03 evidence Iteration 005, cumulative [HCRL.004 DR-12](../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12), against Architecture 1.1.26.
- **Source state** — `feature/BL-CALLIOPE-OO-01` at `3588f5f8df1fbe65d64e8635a3a6a378bf19360c`, matching the local tracking ref. The index was clean. The worktree held the prior DR-12 amendment, untracked Draft.000.001 and both earlier Task/Report pairs.

## Operative issued instruction

````
# Codex Assignment — Harmonia Processing Framework and Checkpoints · Draft.000.002

## 1. Objective

Revise:

`enterprise-architecture/design/offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.001.md`

Create:

`enterprise-architecture/design/offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.002.md`

The revision must incorporate the Solution Architect's agreed **Ponos / Ergon processing sequence** and its functional responsibilities.

This is a **bounded architectural documentation revision**, not a new design investigation.

Preserve Draft.000.001 unchanged as historical evidence.

## 2. Architectural and review context

- Accepted Architecture baseline: **1.1.26**
- Software version: **0.1.0**
- Review: **G03 / Iteration 005**
- Cumulative HCRL: `enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`
- Review activity: **DR-12**

Use the existing terminology reconciliation:

`enterprise-architecture/design/evidence/software-0.1.0/005/after/ai-agent-reports/20261122T111009Z-g03-ergon-route-checkpoint-reconciliation-report.md`

And the preceding processing-framework reconciliation report.

Confirm the current repository branch, HEAD and working-tree state.

Preserve all existing work.

Do not repeat the earlier comprehensive architectural investigation.

## 3. Agreed Ergon processing route

Replace the previous illustrative route with the following **Solution Architect-directed candidate processing sequence**.

Preserve the sequence and functional separation.

```java
from(ergon.ingress.queueId)
    .checkpoint()
    .process(ergon.ingress.pep)                    // Ingress authorisation
    .checkpoint()
    .process(ergon.context-load)                   // WorkSpace creation
    .checkpoint()
    .process(ergon.processor)                      // Business logic within WorkSpace
    .checkpoint()
    .process(ergon.egress.context-unloader)        // WorkSpace close-off
    .checkpoint()
    .process(ergon.egress.output-unload/validate)  // Discrete IU creation
    .checkpoint()
    .process(ergon.egress.outcome-decision)        // Execution outcome generation
    .checkpoint()
    .process(ergon.egress.filter)                  // PublishingMask evaluation
    .checkpoint()
    .process(ergon.egress.pep)                     // Themis outbound enforcement
    .checkpoint()
    .process(ergon.egress.pragma-create)           // Originating Pragma creation
    .checkpoint()
    .process(petasos.route-in)                     // Petasos routing handover
    .to(checkpoint());
```

**This is architectural pseudocode.**

It is not necessarily valid Apache Camel Java DSL.

The processing sequence is the agreed candidate direction. Exact Java class names, Camel endpoint configurations and implementation interfaces remain detailed design matters.

Do not introduce a coordinating component or additional processing engine.

## 4. Functional stage responsibilities

Update the DRP's processing-stage correspondence to identify the following responsibilities.

| Stage | Responsibility |
|---|---|
| `ergon.ingress.pep` | Apply ingress Themis authorisation before admitting processing. |
| `ergon.context-load` | Establish the execution WorkSpace and its initial governed information context. |
| `ergon.processor` | Execute the bounded Ergon business logic using its WorkSpace. |
| `ergon.egress.context-unloader` | Close off the WorkSpace and handle the return of changed working information through the applicable governed context-save arrangements. |
| `ergon.egress.output-unload/validate` | Extract, validate and establish discrete output InformationUnits, including the required governed creation/persistence outcomes. |
| `ergon.egress.outcome-decision` | Determine the attributable outcome of the Ergon execution, distinct from publication and downstream routing outcomes. |
| `ergon.egress.filter` | Apply the producing Ergon's PublishingMask to the appropriate output InformationUnits. Preserve ordinary non-match dispositions without deleting governed information. |
| `ergon.egress.pep` | Apply outbound Themis enforcement before onward release. |
| `ergon.egress.pragma-create` | Request construction and authoritative persistence of originating Pragmas through Mneme's PragmaFactory. |
| `petasos.route-in` | Hand over the established originating Pragma list to Petasos under the applicable routing-acceptance contract. |

Preserve the existing architectural ownership of each responsibility.

Do not infer that a successful business-processing outcome means that subsequent publication, routing or downstream execution succeeded.

## 5. Mneme PragmaFactory interaction — Explicit decision

Record the following agreed decision:

**`ergon.egress.pragma-create` invokes Mneme's PragmaFactory internally.**

The function receives the eligible and authorised output IU identities and requests construction of originating Pragmas.

Mneme applies the governed construction and persistence contracts.

Only successfully established originating Pragma identities may be returned for onward handover.

The resulting list is supplied to `petasos.route-in`.

This interaction is **not** an additional Camel route stage.

Preserve the distinction between:

- Discrete output IU creation/persistence.
- Publication eligibility.
- Outbound authorisation.
- Originating Pragma creation/persistence.
- Petasos acceptance of routing responsibility.

## 6. Context Unloader and Output Unload/Validate

The Solution Architect has explicitly separated these functions.

### Context Unloader

`ergon.egress.context-unloader`

Responsible for WorkSpace close-off and the appropriate return of changed working information.

Preserve the existing Ergon–Mneme context-save responsibilities and their authoritative persistence requirements.

### Output Unload/Validate

`ergon.egress.output-unload/validate`

Responsible for establishing discrete output InformationUnits from the Ergon's generated outputs.

These outputs must be validated and persisted under their applicable governed contracts before being treated as successfully established output IUs.

The exact internal interfaces and implementation mechanics remain open.

**Do not combine these functions into one processing stage.**

## 7. Outcome Decision

Introduce an explicit description of:

`ergon.egress.outcome-decision`

This is a distinct stage responsible for determining the attributable Ergon execution outcome.

It must remain conceptually separate from:

- Context-save outcomes.
- Output IU persistence outcomes.
- PublishingMask dispositions.
- Themis authorisation outcomes.
- Pragma creation outcomes.
- Petasos routing acceptance.
- Downstream Ergon processing outcomes.

Reconcile the stage with the existing G03 distinction between the Ergon-instance TaskOutcome and the applicable undertaking or parent-work disposition.

Do not invent a new canonical outcome data type or change the existing TaskOutcome Topic semantics.

## 8. Egress filtering and authorisation

Preserve the sequence:

```text
Output IU Creation
       ↓
Outcome Decision
       ↓
PublishingMask
       ↓
Outbound Themis PEP
       ↓
Originating Pragma Creation
       ↓
Petasos Route-In
```

The PublishingMask determines publication eligibility.

The outbound PEP determines whether onward release is authorised.

Neither substitutes for the other.

The existing G03 routing design also requires dead-task blocking before outbound release. Preserve that requirement and explicitly identify its correspondence with the revised Egress sequence.

**Do not silently remove dead-task blocking or invent an additional processing stage without Solution Architect adjudication.**

Record any necessary placement decision as outstanding if the current sources do not settle it.

Likewise, retain Praxis selection as a distinct Ponos responsibility invoked through the Petasos Praxis-Router.

## 9. Updated single-row processing diagram

Replace the Draft.000.001 logical processing row with a single horizontal, left-to-right representation of the revised route.

The diagram must:

- Preserve the exact agreed order.
- Show the functional processing stages.
- Show a Checkpoint between each adjacent functional stage.
- Include the initial and terminal Checkpoints.
- Use provisional H-point identifiers for diagram correspondence.
- Keep Mneme/Mnemosyne interactions outside the principal processing row.

Do not assign permanent H-point identities or canonical names.

Do not invent additional components.

## 10. Preserve the checkpoint architecture

Retain the Draft.000.001 checkpoint design.

In particular:

**Every functional handover has a lightweight synchronous Checkpoint.**

Not every Checkpoint requires durable persistence.

Clarify explicitly:

> Checkpoint execution is mandatory at the defined handover. Checkpoint-state persistence is determined by the applicable checkpoint policy and mandatory recovery requirements.

Preserve:

- Synchronous checkpoint execution.
- Incoming state validation where applicable.
- Conditional logging.
- Conditional audit.
- Synchronous forwarding.
- Forwarding-outcome verification.
- Interaction-specific outcome semantics.
- Mneme-managed high-speed housekeeping.
- Independently attributable evidence for Dokimasia Assurance.

**Pass/Fail remains a conceptual description**, not a mandatory Boolean or two-value enum.

Retain independently configurable checkpoint granularity:

`COARSE | MEDIUM | FINE | EXTREME`

for:

1. Logging.
2. Audit.
3. Checkpoint persistence.

These settings must never weaken mandatory security, audit, durability or recovery guarantees.

Do not introduce asynchronous endpoints inside Checkpoints.

Do not treat Checkpoint completion as proof of downstream business execution.

## 11. Preserve the three-tier orchestration model

The following sections should remain substantively unchanged:

### Tier 1 — Intra-Module Orchestration

Apache Camel processing routes, reusable scaffolding, checkpoints and operational housekeeping.

### Tier 2 — Inter-Module Orchestration

Praxis-based progression, Petasos routing and publication/subscription-driven progression.

### Tier 3 — OA&M Oversight

Operational observation, logging, metrics, audit and independent Assurance.

The third tier remains an emerging architectural area requiring further exploration.

Preserve candidate applicability to:

- Ponos / common Ergon.
- DigitalTwin-Behaviour.
- Pylai-Message-In.
- Pylai-Message-Out.
- Pylai-API-Server.
- Pylai-API-Client.

Do not broaden this revision into detailed designs of those additional modules.

## 12. Reconciliation and open decisions

Update only those reconciliation entries affected by the new route.

Specifically:

- Context Unloader and Output Unload/Validate are now distinct stages.
- Outcome Decision is explicitly represented.
- PublishingMask and outbound PEP are distinct.
- Originating Pragma creation is explicitly represented after outbound PEP.
- PragmaFactory is invoked internally by the Pragma Create function.
- Petasos Route-In follows successful originating Pragma establishment.
- Dead-task blocking must retain its existing architectural obligation, with exact placement still subject to reconciliation.

Retain outstanding detailed contracts for:

- Checkpoint identity and lifecycle.
- Required durable recovery boundaries.
- Minimum evidence for each handover.
- Exact checkpoint granularity semantics.
- CDI processor lifecycle and measured performance.
- Petasos acceptance, acknowledgement and reconciliation.
- Direct Praxis routing work-realisation correspondence.
- Trusted security-context propagation.
- Independent Assurance integration.

Avoid reopening accepted architectural decisions.

## 13. HCRL and evidence

Create Draft.000.002 and preserve Draft.000.001.

Add a bounded continuation under **DR-12** in:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

The addition must identify the revised route and the Solution Architect's new processing-stage decisions.

Preserve all existing HCRL entries and outstanding obligations.

Record Task and Report evidence under:

`enterprise-architecture/design/evidence/software-0.1.0/005/`

Follow the repository's established evidence conventions.

## 14. Verification and constraints

Verify:

- The processing stages appear in the exact agreed order.
- The new functions are documented with distinct responsibilities.
- The horizontal route has the correct number and position of Checkpoints.
- PragmaFactory remains an internal Mneme interaction.
- No unintended changes to Tier 2 or Tier 3.
- Existing mandatory persistence, security, audit and recovery obligations remain intact.
- Previous draft and review history remain preserved.
- All relevant links resolve.

Run bounded documentation and architecture checks as appropriate.

Report existing validation failures without introducing unrelated corrective changes.

Do not:

- Implement software.
- Change the Architecture Continuum.
- Change accepted baselines.
- Create additional DRPs.
- Invent canonical APIs, interfaces or data types.
- Commit, tag or push.
- Mark the DRP or HCRL accepted.

## 15. Deliverables

Return:

1. `DRP-Harmonia-Processing-Framework-and-Checkpoints.draft.000.002.md`
2. A concise reconciliation/execution report.
3. The additive HCRL DR-12 amendment.

**Primary objective:** Incorporate the agreed Ergon processing sequence into the candidate Harmonia processing architecture, preserving checkpoint governance and the existing component responsibility boundaries.
````
