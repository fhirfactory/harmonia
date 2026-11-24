Report-Produced-At: 2026-11-21T22:36:55.045883Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Reconciliation Report — Petasos Routing and Reconciliation · Draft.000.003

The [Revision 003 Task][task] directed a bounded follow-up to [Draft.000.002][previous] under [G03 / Iteration 005 / HCRL.004 DR-12][hcrl]. The worktree started at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`) on `feature/BL-CALLIOPE-OO-01` against Architecture 1.1.26. [Revision 002 reconciliation][prior-report]

## Exact changes

- [Draft.000.003 §§1–1.1][strategy] prioritises recovery of the durable decision and complete obligation set. Required pre-routing reconciliation excludes confirmed completions from the working list while retaining their authoritative history. Incomplete work remains subject to ownership, authorisation and recovery. Indeterminate injection is reconciled or held, with no automatic reinjection. A current, validated set under trusted continuity can avoid a historical reconciliation.
- [§1.2][timestamps] defines the two `Instant` properties. Startup identifies the applicable module instance. Last success advances only on confirmed success of the whole applicable routing activity. A failed, partial or indeterminate activity retains its own outcome. A prior success before startup triggers reconciliation. No previous success means no value. Unavailable or incomparable values provide no continuity evidence. Respect the current engine/module context and applicable RoutingSetList, so an unrelated success cannot suppress necessary recovery. Indeterminate results, ownership or continuity loss and detected conflict also trigger reconciliation. Timestamps provide no acceptance or completion proof.
- [§§1.3–1.4][coordination] records Option A, ephemeral `PetasosRoutingSharedStatus`. Petasos owns routing meaning and use. Mneme manages shared coordination and ownership for a RoutingSetList across relevant Petasos instances, while independent sets can proceed. Durable decision records, governed work and Provenance preserve authoritative facts. Operational status can be reconstructed after loss and establishes no durable completion or fencing by itself.
- The combined operation takes ownership before using the working view for reconciliation and effects. It requires protected decision establishment and authoritative refresh or validation after acquisition, so discovery before acquisition cannot confer permission to act on a stale view. The precise sequencing and ownership mechanism remain open. After takeover, stale owners must be prevented from injecting, updating completion or releasing a successor's ownership. Prior in-flight effects may be indeterminate and need reconciliation before a conflicting action. [Candidate combined operation][combined]
- The component, failure, evidence and conformance sections incorporate these invariants. The Subscription-Router and Praxis-Router descriptions, Ergon Egress sequence and authoritative-provenance recovery subsection retain their Draft.000.002 text. Existing work identity and decision-specific recovery continue to apply. [Exact revised candidate][candidate]

## Architectural correspondence

| Surface | Baseline, correspondence and required treatment |
| --- | --- |
| Mneme coordination | AX-05 assigns distributed active state and coordination to Mneme. AX-09 permits ephemeral operational state used for routing and concurrency. The accepted 1.1.23 persistence collaboration locates concurrency of an individual IU in Mneme. RoutingSetList is a different scope, with ownership and effects crossing applicable Petasos instances. This is a candidate extension of the use of existing coordination, with explicit scope mapping and proof needed. It changes neither existing IU concurrency semantics nor Petasos routing authority. [AX-05][ax05] · [AX-09][ax09] · [Mneme][mneme] · [1.1.23 DR-1][mneme-123] |
| Cache and operational-state placement | The accepted Standard Cache has distributed reference/operational-coordination responsibility, with qualified topology and concurrency guarantees. The Workflow Cache remains per-JVM LOCAL. A LOCAL-only mechanism would conflict with the cross-instance requirement. Neither synchronous distribution nor peer IU OperationalMetadata establishes safe RoutingSetList ownership, placement, consistency or fencing. Retain MC-01–MC-07, especially MC-01/02 for partition and owner/failover behaviour, and use governed Mneme access. No new cache or coordination technology is allocated. [Standard Cache][standard-cache] · [1.1.24 DR-3][mneme-124] · [1.1.25 DR-2][operational-metadata] |
| Persistence, acceptance and failed ownership | Mnemosyne retains authoritative decisions/work and existing version and Success requirements. Operational timestamps, cache visibility, acquired ownership or missing status cannot prove durable acceptance. Artemis acceptance is a separately evidenced effect. Persisted routing outcomes and the broker boundary still require owning evidence. AX-15 governs unknown effects and safe retry. A cross-instance ownership guarantee must address effects after stale ownership without presuming atomicity between Mneme and Artemis. The pending DurableInformation/DurableData candidate has no new authority in this revision. [Accepted persistence][persistence] · [Concurrency Policy Conformance][concurrency-policy] · [AX-15][ax15] · [Mnemosyne candidate][durable-data] |
| Adjacent design and authority | The earlier qualified allocations for Mneme subscription selection, consumer-side undertaking creation, Ponos/Ergo continuation routing and parent TaskOutcome remain explicit. The direct Praxis parcel/work-realisation boundary, Ponos filters and egress Provenance completeness retain their detailed review requirements. Themis governs protected operations and Kleio retains mandatory authoritative capture. G03 staged audit supplies no change to those requirements. [Revision 002 reconciliation][prior-report] · [G03 adjudications][register] · [Candidate open decisions][open] |

The reviewed sources support Mneme's responsibility allocation. They supply no accepted contract for routing-set concurrency or stale-owner exclusion. Both canonical correspondence and verified suitability of the existing coordination capabilities are required before a dependent design can claim those guarantees. AX-04/05/09 favour reuse of existing active-state machinery, AX-07/08 preserve enforcement and evidence, and AX-10/14/15/16 distinguish distributed failure, operational observations and authoritative effects. AX-17/18 preserve uncertainty and responsibility boundaries.

## PF-07 and PF-08

**PF-07 remains partially resolved.** Preserve egress output completeness, trigger and acknowledgement custody, per-router handover recovery, separate evidence for decision/work persistence and Artemis acceptance, and audit/external ACK obligations. Further evidence must establish each acknowledgement's issuer, meaning and sufficient proof, including after indeterminate commit or missing updates.

**PF-08 — Architectural strategy resolved; detailed transport, concurrency and recovery contracts outstanding.**

Agree the pre-routing and conditional recovery strategy recorded in the candidate. Retain decisions on exact decision/set/obligation identities and equivalence across repeated execution, publication, repeated selection and replay. Establish authoritative proof of Artemis acceptance after indeterminate effects, safe reinjection and duplicate suppression, ownership validity and fencing, cross-instance failed-owner recovery, acquisition/renewal/release and protected refresh, precise shared status lifecycle and consistency, exact timestamp scope and initialisation, handoff ACK/retry equivalence, execution admission/redelivery and `DoNotBefore`. The existing PF register and earlier reconciliation records retain their historical standing. [PF-08 in the candidate][pf08] · [Existing register][register]

Later verification must exercise no previous success, restart, stale or unavailable timestamps, activity on separate sets, loss of ownership or continuity without restart, simultaneous and competing same-set recovery, independently progressing sets, failed-owner takeover and resumed stale owners, and in-flight broker acceptance. Include partial progress and lost ACK/update, confirm no premature success timestamp or lost completed history, and verify the actual configured Mneme ownership and Artemis evidence boundaries with security, audit and recovery controls. [Candidate conformance expectations][conformance]

## Checks and disposition

- **PASS** · Bounded offline Java 21 ArchUnit reactor exited 0. Maven reported **90 tests** with zero failures, errors or skips in 27.883 seconds. This is existing-software evidence. No implementation or cross-instance failure-injection evidence exists for this candidate.
- **PASS** · Verified both earlier routing draft hashes, exact retained router/Egress/provenance passages, issued coordination decisions and exact PF-08 wording, 128 local link targets/fragments before Report emission, earlier DR-12 text/links and worktree scope. Draft.000.002 SHA-256 `46db01f8321aa710e7618b71b0c694421a6f36b526eea270fc5a8bf6d647d71e`. Draft.000.003 SHA-256 `a69081ddbec30110e37301100a25601f072c6301a435289f6ab478b4434e2e18`. Prior evidence, pre-existing changes, HEAD, refs, staged content and Architecture 1.1.26 were preserved.
- **FAIL** · The iteration checker `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 005` exited 1 with `No corresponding HCRL for software-0.1.0-005`. The task retains evidence in 005 and the work in cumulative HCRL.004. Whole-iteration validation still requires the matching record or explicit governance disposition.
- **UNRESOLVED** · The original instruction-entry system time is unavailable. The Task preserves the issued wording with unresolved timestamp and filename prefix for Solution Architect treatment before closure.

The [DR-12 addition][hcrl] records Draft.000.003 and the qualified PF-08 result. HCRL.004 and DR-12 remain pending Solution Architect review. Repository changes are confined to the candidate, the Task/Report pair and the bounded HCRL addition.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-g03-petasos-routing-draft-000-003-task.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[previous]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md
[prior-report]: 20261121T214710Z-g03-petasos-routing-draft-000-002-report.md
[candidate]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md
[strategy]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#11-pre-routing-reconciliation
[timestamps]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#12-startup-and-successful-routing-activity
[coordination]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#13-petasosroutingsharedstatus-and-routingsetlist-concurrency
[combined]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#14-combined-operation
[open]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#7-architecture-correspondence-and-open-decisions
[pf08]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#71-pf-08-qualified-disposition
[conformance]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md#8-conformance-expectations
[register]: ../G03-Pragma-Family-Adjudications-005.md#pf-01pf-12-disposition
[ax05]: ../../../../../../continuum/motivation/principle/0412a2084f0145babc4f022c71fea65f.md
[ax09]: ../../../../../../continuum/motivation/principle/9a5cb9e8876844e58e660f909f7df9fc.md
[ax15]: ../../../../../../continuum/motivation/principle/ef8abbaec37e4890ad78b9f263e67906.md
[mneme]: ../../../../../../continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md#architectural-content
[standard-cache]: ../../../../../../continuum/application/application-function/cfe4e0dbe6894a14841452b731ab9cb8.md
[mneme-123]: ../../../../../register/1.1.23/hcrl-1.1.23-001.complete.md#dr-1
[mneme-124]: ../../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[operational-metadata]: ../../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-2
[persistence]: ../../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[concurrency-policy]: ../../../../../../continuum/motivation/principle/architectural-axiom/7e3b05a2e3e547cbbd26b1e181af0d10.md
[durable-data]: ../../../../../offline-design/DRP-Mnemosyne-DurableInformation-and-DurableData.draft.000.001.md
