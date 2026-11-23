Task-Issued-At: 2026-11-23T04:00:05.477Z

Event source: Codex interaction rollout-2026-11-22T22-04-06-01a202ed-72a2-75e3-a092-1706fc2be364.jsonl, record 846, timestamp of the user-role response_item containing the operative instruction entering the session.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Detail — G03 / PF-07 Petasos Routing and Reconciliation Final.001

- **Acceptance authority and scope** — The operative instruction records the Solution Architect's qualified acceptance of Routing Draft.000.004, including the PF-07 H10/H11 contract and PF-08 strategy. Authorised work covers Final.001, the bounded DR-12 disposition and Iteration 005 evidence. Implementation, Continuum incorporation and collective G03 closure require separate authority.
- **Exact accepted source** — [Routing Draft.000.004](../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.004.md), SHA-256 `7e71bf7d2fa84cd2d3beae723d0412804416ecab0ebfcf733e0b9b39aa7ece91`, matching the [PF-07 reconciliation report](../../after/ai-agent-reports/20261123T033325Z-g03-pf-07-ergon-egress-petasos-handover-report.md). Its [detailed qualifications](../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.004.md#7-architecture-correspondence-and-open-decisions) and [conformance obligations](../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.004.md#8-conformance-expectations) carry forward. [Processing Framework Final.001](../../../../../offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#42-stage-and-checkpoint-correspondence) governs H10/H11 and the processing sequence.
- **Target and source state** — Architecture 1.1.26, Software candidate 0.1.0, G03 / Iteration 005 / [HCRL.004 DR-12](../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12). Branch `feature/BL-CALLIOPE-OO-01` at `3588f5f8df1fbe65d64e8635a3a6a378bf19360c` matched its existing local tracking ref. The index was clean. The worktree held the prior G03 drafts, accepted framework, evidence and DR-12 continuations.

## Operative issued instruction

# Codex Assignment — G03 / PF-07 Finalisation

## 1. Authority

The Solution Architect has accepted:

`DRP-Petasos-Routing-and-Reconciliation.draft.000.004.md`

**Disposition: Accepted with qualifications.**

This acceptance covers the routing architecture described in Draft.000.004, including the previously established PF-08 strategy and the newly adjudicated PF-07 H10/H11 handover contract.

The acceptance does not authorise implementation, Architecture Continuum incorporation or closure of the collective G03 review.

## 2. Create Final.001

Create:

`enterprise-architecture/design/offline-design/DRP-Petasos-Routing-and-Reconciliation.Final.001.md`

Use Draft.000.004 as the accepted source.

Preserve its architectural content without substantive changes.

Update the document's status, acceptance references and review-standing terminology as necessary.

Preserve Draft.000.004 unchanged.

## 3. Preserve accepted PF-07 decisions

The final document must retain:

- `ErgonCreatedPragmaList` and `ErgonCreatedPragmaToken` as the conceptual H10 information contract.
- `expectedListSize` and the per-Pragma creation/persistence statuses.
- Ergon Egress ownership of expected-set completeness.
- Mneme PragmaFactory construction and authoritative persistence coordination.
- H10's distinction between structural completeness and successful establishment.
- H11's all-or-nothing acceptance of the complete list.
- Petasos's durable assumption of routing responsibility.
- The distinction between list receipt and durable acceptance.
- Zero subscribers as a valid routing disposition.
- Independent downstream routing, delivery and execution outcomes.

Preserve the accepted Processing Framework's H10/H11 boundaries and processing sequence.

## 4. Preserve PF-08 and other qualifications

Do not reopen or modify the accepted PF-08 strategy.

Retain all existing detailed-design and conformance obligations, including:

- Exact originating Pragma membership and completeness evidence.
- Durable handover acknowledgement and correlation.
- Failure and indeterminate-outcome handling.
- Recovery, replay and idempotence.
- RoutingSetList ownership and concurrency controls.
- Authoritative Provenance reconstruction.
- Relevant Pragma Model, publication/subscription and Petasos–Mneme correspondence.
- Trusted security context and mandatory audit evidence.
- Direct Praxis routing and work-realisation correspondence.

Acceptance of the architecture does not imply completion of these contracts.

## 5. HCRL update

Update:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

Under DR-12:

1. Record the Solution Architect's acceptance of Routing Draft.000.004.
2. Identify Routing Final.001 as the accepted design.
3. Preserve the PF-07 architectural resolutions.
4. Carry forward detailed acknowledgement, completeness, recovery and conformance obligations.
5. Retain the existing PF-08 disposition and qualifications.
6. Preserve all previous review history.

**The collective G03 review remains pending.**

Do not change the standing of other candidate DRPs.

## 6. Evidence and verification

Create the corresponding Task and Report under:

`enterprise-architecture/design/evidence/software-0.1.0/005/`

Verify:

- Final.001 matches Draft.000.004 substantively.
- Draft.000.004 remains unchanged.
- Processing Framework Final.001 remains unchanged.
- PF-07 decisions are preserved.
- PF-08 decisions and qualifications remain intact.
- HCRL.004 accurately records the bounded acceptance.
- All previous review dispositions remain traceable.
- Documentation references resolve.

Report the existing HCRL.005 evidence-checker failure separately. Do not attempt to resolve it within this assignment.

## 7. Constraints

Do not:

- Modify software.
- Modify the Architecture Continuum.
- Modify another accepted DRP.
- Introduce new architectural decisions.
- Close the collective G03 review.
- Commit, tag or push.

## 8. Deliverables

Return:

1. `DRP-Petasos-Routing-and-Reconciliation.Final.001.md`
2. Updated HCRL.004 / DR-12.
3. Concise finalisation report.

**Objective:** Formalise the Solution Architect's qualified acceptance of the Petasos routing architecture, including PF-07 and the previously established PF-08 strategy, without expanding the review.
