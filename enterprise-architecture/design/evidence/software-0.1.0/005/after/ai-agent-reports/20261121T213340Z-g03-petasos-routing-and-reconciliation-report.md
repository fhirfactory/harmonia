Report-Produced-At: 2026-11-21T21:33:40.365346Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Reconciliation Report — Petasos Routing and Reconciliation · G03 / Iteration 005

The [operative Task][task] authorised a candidate design and bounded reconciliation of both Petasos routers under [HCRL.004 / DR-12][hcrl]. The worktree started at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`) on `feature/BL-CALLIOPE-OO-01`, with Architecture 1.1.26 and the existing pending G03 evidence and candidates. [Prior G03 adjudications][prior]

## Candidate and architectural correspondence

The [new Draft.000.001][candidate] records the Solution Architect's common strategy. Both routers first establish the decision and its complete obligation set durably through Mneme/Mnemosyne. Petasos then requests appropriate Pragma-family construction or recovers established work, injects the work into Petasos queues and correlates the effects by obligation. Recovery uses the recorded obligations and preserves known sibling outcomes without rerunning selection or Praxis evaluation against newer definitions.

| Surface | Existing authority and reconciliation |
| --- | --- |
| Subscription versus queue responsibility | The qualified Petasos and Parcel elements allocate semantic selection to Mneme and concrete handoff to Petasos. Parcel/ParcelMask Final §4 also retains the earlier Mneme allocation. G03 assigns subscription evaluation/matching to Petasos, with Mneme providing governed information. Reconcile the earlier allocation and affected elements explicitly. Retain the conceptual same-context mask composition and open pre-parcel selector requirements. [Petasos][petasos] · [Parcel Topic][parcel] · [Parcel/ParcelMask][mask] · [Publication draft][publication] |
| Praxis interpretation versus handoff | The qualified Ponos, Praxis and Pragma contributions say Ponos/Ergo performs output processing and routing using Praxis semantics. G03 specifies a Ponos-owned evaluation service invoked by Petasos, and Petasos orchestration of construction, injection and reconciliation. This is an explicit assignment at the continuation boundary. Preserve Energeia's management of Praxis, Pathway and Journey, Ponos's execution and progression responsibilities and the distinction between continuation and independent publication. Reconcile the new service boundary, trigger/result and eligible successor parcel referents. [Ponos][ponos] · [Praxis][praxis] · [Pragma Topic][pragma-topic] |
| Constructed work and parcel | The PragmaFactory logical role supports construction, with caller and detailed contracts open. The qualified prior ActionableTask handoff parcels originating work and establishes a FulfillmentTask at the consumer. The G03 subscription design requests authoritative Pragmatosis establishment before handoff, parcels its identity and resolves it at the Ergon. Preserve the by-value Artemis mandate. Other communication patterns and unresolved specialised successor shapes retain their standing. [PragmaFactory][factory] · [Parcel Topic][parcel] · [Exclusive routing mandate][mandate] · [Petasos–Mneme draft][handoff] |
| Decision record and Outcome | Petasos owns fulfilment/reconciliation. Mneme manages durable decision and work information through the existing Mnemosyne contracts. The candidate record names express this new information requirement without allocating a new canonical type or universal event superclass. Governed persistence, broker acceptance, delivery, actual Ergon processing, save and authoritative mutation have different evidence. TaskOutcome's existing Topic describes parent resolution, while the G03 design uses TaskOutcome for Ergon-instance processing, with the association to progression and parent disposition still to be mapped. No result is invented where execution did not occur. [Accepted Mneme–Mnemosyne contracts][persistence] · [TaskOutcome Topic][outcome] · [G03 Pragma draft][pragma] |
| Themis and Kleio | Themis remains the authorisation decision and enforcement authority. Applicable local PEPs gate protected operations, including Ponos evaluation, Mneme operations, forwarding and execution. Petasos, Ponos and Mneme submit required auditable occurrences from their owning boundaries. Kleio retains authoritative capture and fail-closed requirements. Audit staging and the Mnemosyne DurableInformation/DurableData proposal are separate pending candidates. [G01][g01] · [AX-07][ax07] · [Kleio][kleio] · [Audit candidate][audit] · [Mnemosyne candidate][mnemosyne] |

The candidate uses AX-04 and the native Artemis mandate for queue operations, AX-05/06 for authority and persistence separation, AX-07/08 for policy and semantically meaningful evidence, AX-10/14/15/16 for replayable work, preserved uncertainty and observable progression, and AX-17/18 for explicit gaps and ownership. This is candidate correspondence to 1.1.26. The qualified prior assertions continue to apply at their recorded standing.

## PF-07 and PF-08 disposition

| Exception | Settled by the issued strategy at candidate level | Open contract and adjudication |
| --- | --- | --- |
| **PF-07 — Authority, acceptance and acknowledgement evidence** · Prior status Partially resolved | The decision and complete set are durable before acting. The constructed work and resolvable lineage precede reference handoff. Keep construction, durable injection and the record update separate. An absent update or initially NULL combined marker cannot establish that injection failed. Each obligation has its own outcome. | Define original trigger custody and ACK timing before decision capture, lookup/correlation after an uncertain decision create, exact recoverable work version/lineage and sufficiency of each ACK issuer's evidence. Determine reliable recovery evidence after delivery/retention or a lost reply, conditions for safe re-injection, external ACK correspondence and mandatory audit integration. Preserve configured multi-site mutation guarantees and existing Kleio treatment. |
| **PF-08 — Retry equivalence and execution eligibility** · Prior status Unresolved | Once captured, recovery uses the same result/obligation set and preserves established work identity and completed siblings. Changed subscription or Praxis definitions supply no basis to re-evaluate in-flight obligations. A deliberate replay is separately governed and attributable. Redelivery does not establish entitlement to execute. | Define exact trigger, decision, obligation, work and delivery-attempt correspondence across renewed publication, changes to definitions and replay. Set concurrency controls and lifecycle rules that preserve known progress, conditions for permitted or indeterminate retry, who establishes timing and recovery eligibility under `DoNotBefore`, and when deliberate replay reuses or creates records, work or a new evaluation. |

The earlier PF register remains intact. These additions resolve parts of the candidate strategy without approving a runtime contract, canonical design or specific persistence encoding. [Prior PF register][prior] · [Candidate identity and evidence][candidate-evidence]

## Subsequent verification requirements

1. Show that both full decision sets and their trigger identities are recoverable, that no individual fulfilment precedes durable capture, and that restart/duplicate trigger handling does not create a second decision or re-evaluate an existing one. Exercise zero matches, multiple matches to the same subscriber, specialised successors and definition changes.
2. Inject interruptions before/after each persistence and broker boundary. Demonstrate evidence for already-created work and previously accepted parcels, including loss of replies and record updates, queue lookup after consumption, duplicate-cache expiry, broker failover and partial or concurrent recovery. Validate the selected journal/store and route guarantees. The cited Artemis 2.33.0 manual supports finite duplicate detection and configuration-dependent commit assurance. [Duplicate detection][artemis-duplicates] · [Commit guarantees][artemis-commit]
3. Verify separate durable acceptance, delivery, execution, save and authoritative mutation evidence. Test policy denial or unavailability on repeat access, `DoNotBefore`, execution admission after redelivery, explicitly authorised replay and fail-closed mandatory audit. Treat open safety/evidence contracts as open when test setup cannot establish the owning guarantee.

## Checks and result

- **PASS** · The bounded offline ArchUnit reactor completed in 29.071 seconds as reported by Maven, exit 0. All **90 tests** passed with zero failures, errors or skips. This verifies existing software conformance. Router behaviour and actual broker/persistence recovery remain for future evidence.
- **PASS** · Checked 107 local targets/fragments before Report emission, the required design and failure cases, DR-12 historical body and links, pre-existing changed files and bounded worktree scope. Candidate SHA-256 `c0930af415c09c63af4fd637e58118a1a2bcdc976fe2dc043b98ded9061a35dc`. HEAD, refs, staged content and Architecture 1.1.26 were preserved.
- **FAIL** · `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 005` exited 1 with `No corresponding HCRL for software-0.1.0-005`. The assigned evidence stays in 005 and the cumulative record stays in HCRL.004. Formal whole-iteration layout verification requires the matching HCRL or an explicit governance disposition.
- **UNRESOLVED** · The original instruction-entry system timestamp is unavailable. The Task retains the issued instruction with unresolved time and filename prefix for Solution Architect treatment before closure.

The [DR-12 extension][hcrl] records the candidate and reconciliation. HCRL.004 and DR-12 remain pending. Worktree changes are confined to the new candidate, this Task/Report pair and the bounded HCRL addition.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-g03-petasos-routing-and-reconciliation-task.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[prior]: ../G03-Pragma-Family-Adjudications-005.md#pf-01pf-12-disposition
[candidate]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.001.md
[candidate-evidence]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.001.md#6-minimum-identity-and-evidence-for-reconciliation
[petasos]: ../../../../../../continuum/application/application-component/195e5f10db8e419391354197b5d7a8cc.md#architectural-content
[ponos]: ../../../../../../continuum/application/application-component/b521762c1ea444b48cd5f66be3752d30.md#architectural-content
[praxis]: ../../../../../../continuum/application/data-object/0f961462340348b290fd844357d54d67.md#architectural-content
[pragma-topic]: ../../../../../../continuum/application/data-object/27ad143e7c784e378556af8a57c438f7.md#architectural-content
[parcel]: ../../../../../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md#architectural-content
[factory]: ../../../../../../continuum/application/application-component/75e77d8dfd1d4870aa6151485d28d1dd.md#architectural-content
[mandate]: ../../../../../../continuum/motivation/constraint/mandate/e024703b32c5437481ce290e8e50b079.md#approved-definition
[outcome]: ../../../../../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md#architectural-content
[persistence]: ../../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[mask]: ../../../../../offline-design/DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md#4-subscription-and-publication-relationship
[publication]: ../../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.003.md
[handoff]: ../../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.000.004.md
[pragma]: ../../../../../offline-design/DRP-Mneme-Pragma-Model.draft.000.004.md#48-execution-outcomes-and-work-disposition
[g01]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#4-architectural-definition
[ax07]: ../../../../../../continuum/motivation/principle/architectural-axiom/5de2ae2c3b794c2d9f3a14efd51d564f.md
[kleio]: ../../../../../../continuum/application/application-component/fefda110e2274884ad58906733606efd.md#auditability-and-availability
[audit]: ../../../../../offline-design/DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md
[mnemosyne]: ../../../../../offline-design/DRP-Mnemosyne-DurableInformation-and-DurableData.draft.000.001.md
[artemis-duplicates]: https://github.com/apache/artemis/blob/2.33.0/docs/user-manual/duplicate-detection.adoc
[artemis-commit]: https://github.com/apache/artemis/blob/2.33.0/docs/user-manual/send-guarantees.adoc
