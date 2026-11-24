Task-Issued-At: 2026-11-22T11:04:08.483Z

Event source: Codex interaction rollout-2026-11-22T22-04-06-01a202ed-72a2-75e3-a092-1706fc2be364.jsonl, record 12, timestamp of the user-role response_item containing the operative instruction entering the session.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Detail — G03 Ergon route, processing functions and checkpoint reconciliation

- **Instruction** — The user issued the assignment retained verbatim below, including the illustrative route attributed to the Solution Architect.
- **Target and review correspondence** — Software `0.1.0`, G03 evidence Iteration `005`, against accepted Architecture `1.1.26`. The cumulative review reference is [HCRL software-0.1.0 / 004, DR-12](../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12).
- **Checked-out source at entry** — `feature/BL-CALLIOPE-OO-01` at `3588f5f8df1fbe65d64e8635a3a6a378bf19360c`, clean, aligned with the existing local tracking ref.

## Operative issued instruction

````
# Assignment — Ergon Route, Processing Functions and Checkpoint Reconciliation

## Context

We are continuing the Harmonia G03 / Software 0.1.0 architectural review.

The accepted Architecture baseline is **1.1.26**.

The Solution Architect has outlined a candidate processing model based on Apache Camel routes, CDI-managed processing functions and explicit checkpoints.

Before adopting this model, we need to reconcile its terminology and responsibilities against the existing architectural authority.

**This is an investigation and reconciliation task, not an architecture amendment or implementation assignment.**

## 1. Establish the repository baseline

Confirm the current branch, HEAD and working-tree state.

Use the repository's current checked-out state following the Solution Architect's latest push.

Do not change branches or alter existing work.

## 2. Investigate the existing architecture

Examine the relevant architectural definitions and interaction contracts for:

- Ponos and Ergon execution.
- Ergon Ingress, processing and Egress.
- ContextLoader, Pragmaton and Ergon.WorkSpace.
- Pragma and Pragmatosis creation.
- PublishingMask and PragmaFilter.
- Themis ingress and egress enforcement.
- Petasos routing and handover.
- Checkpoint persistence and recovery.
- Dokimasia and independent Assurance.
- Kleio audit capture.
- Apache Camel routes, processors and endpoints.
- CDI `@Dependent` lifecycle and concurrency arrangements.

**Use bounded searches and excerpts. Do not load entire large RAD or Continuum documents unnecessarily.**

Distinguish accepted architectural authority from pending DRPs and implementation candidates.

## 3. Candidate Ergon processing route

The Solution Architect has supplied the following conceptual route:

```java
from(ergon.ingress.queueId)
    .checkpoint()
    .process(ergon.ingress.pep)
    .checkpoint()
    .process(ergon.context-load)
    .checkpoint()
    .process(ergon.processor)
    .checkpoint()
    .process(ergon.egress.output-extract)
    .checkpoint()
    .process(ergon.egress.filter)
    .checkpoint()
    .process(ergon.egress.pep)
    .checkpoint()
    .process(petasos.route-in)
    .to(checkpoint());
```

This is **illustrative pseudocode**, not an accepted or prescribed implementation.

Its purpose is to express the sequence of functional processing and the interactions between stages.

The candidate model has these characteristics:

1. Apache Camel routes express the processing sequence.
2. Processing functions are generally implemented through CDI `@Dependent` instances exposing Camel `process(Exchange)` behaviour.
3. Checkpoints identify the handover boundaries between processing functions.
4. Checkpoints may invoke other Harmonia components through their established APIs.
5. Mneme interactions generally occur inside processing methods or checkpoints rather than requiring separate Camel route stages.
6. Camel endpoints/adapters may implement handovers, using mechanisms such as `direct:`, `seda:` or Artemis-backed messaging as appropriate.
7. Individual checkpoint/function handovers produce simple Pass/Fail results captured by an independent Assurance service.
8. Checkpoints have separately configurable logging, audit and persistence granularity: `COARSE`, `MEDIUM`, `FINE`, `EXTREME`.

Do not assume that every characteristic is already accepted architectural authority.

## 4. Produce a terminology reconciliation matrix

For each candidate route element, identify:

| Field | Required information |
|---|---|
| Candidate name | Name from the illustrative route |
| Existing governed name | Exact name established in the repository |
| Architectural owner | Component responsible for the behaviour |
| Functional responsibility | What the element actually does |
| Interaction boundaries | Relevant existing interfaces, APIs and handovers |
| Source authority | Precise repository file and section |
| Correspondence | Exact match, synonym, partial match, conflict or new proposal |
| Recommendation | Retain existing name, adopt candidate, or request adjudication |

Do not silently resolve conflicting architectural definitions.

Where the repository does not establish a name, explicitly identify it as unresolved.

## 5. Investigate checkpoints specifically

Determine whether the accepted architecture already establishes:

- Named processing checkpoints.
- Checkpoint lifecycle and identity.
- Configurable logging and audit granularity.
- Checkpoint persistence and recovery.
- Independent Pass/Fail handover evidence.
- Interaction with Dokimasia and Kleio.
- Allocation of checkpoint responsibilities to Ponos or other components.

Distinguish architectural requirements from proposed implementation mechanisms.

In particular, do not conflate a proposed Harmonia checkpoint abstraction with Apache Camel's existing `checkpoint()` functionality.

Identify any existing contracts that would constrain the proposal.

## 6. Reconcile the processing sequence

Produce **one horizontal, single-row logical processing diagram**.

Show the candidate functional stages in their processing order, with named handover points between them.

Use provisional identifiers `H01`, `H02`, etc., solely for diagram correspondence.

Do not assign authoritative H-point identities prematurely.

Keep Mneme/Mnemosyne interactions **out of the main processing row**. Document those separately in the terminology matrix.

Identify any established processing responsibility missing from the proposed sequence, but do not invent additional processing stages.

## 7. Deliverables

Produce one compact reconciliation report containing:

1. Repository baseline.
2. Terminology matrix.
3. Single-row route diagram.
4. Existing checkpoint and Assurance contract findings.
5. Genuine architectural discrepancies.
6. Recommended naming conventions requiring Solution Architect adjudication.

Place Task and Report evidence under the existing G03 / Iteration 005 evidence structure, following repository conventions.

No new DRP is required.

Do not modify the Continuum, software implementation, existing DRPs or cumulative HCRL.

Do not commit, tag or push.

## 8. Completion criteria

The assignment is complete when we can answer:

**What should each stage and checkpoint in the candidate Ergon route actually be called, based on the Harmonia architecture we have already established?**

Report the findings for Solution Architect review.

Do not treat the candidate route as accepted merely because its terminology has been reconciled.
````
