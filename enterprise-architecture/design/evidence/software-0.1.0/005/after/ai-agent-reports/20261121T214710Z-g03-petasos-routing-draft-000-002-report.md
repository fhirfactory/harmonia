Report-Produced-At: 2026-11-21T21:47:10.509857Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Reconciliation Report — Petasos Routing and Reconciliation · Draft.000.002

The [Revision 002 Task][task] directed a bounded follow-up to [Draft.000.001][previous] in [G03 / Iteration 005 / HCRL.004 DR-12][hcrl]. The starting worktree was at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`) on `feature/BL-CALLIOPE-OO-01`, with Architecture 1.1.26 and pending prior decisions. [Original routing reconciliation][original-report]

## Exact changes

- [Draft.000.002 §2.1][egress] places originating Pragma creation and handover at Ergon Egress. Dead-task blocking and the applicable Themis outbound PEP precede construction through Mneme's PragmaFactory. Ergon Egress obtains governed durability through Mneme/Mnemosyne before handing the list to Petasos. The sequence preserves Ergon-instance TaskOutcome, work disposition, output publication, handover and routing as separate facts. The pseudocode illustrates the stages and establishes no signatures.
- [Draft.000.002 §3][subscription] retains the Subscription-Router design and exact pseudocode. Its only substantive scope clarification is that its input is the existing originating Pragma handed over by Ergon Egress. Petasos still matches subscriptions, durably records each publication decision and complete SubscriberSet before fulfilment, requests subscriber-specific Pragmatosis through Mneme and recovers existing obligations.
- [Draft.000.002 §4][praxis] removes the prior illustrative Praxis-side Pragma construction. Ponos evaluates the context and the existing list using Praxis progression rules and any applicable output filters. Its result identifies existing Pragma identities and destination Ergons. Petasos durably records the decision and complete result before resolving and routing the selected identities. Several rules may select the same Pragma while retaining distinct obligations. The candidate establishes no Praxis-side Pragmatosis construction requirement.
- [Draft.000.002 §5.1][recovery] adds recovery of the original list from authoritative Provenance when the complete set can be established. The candidate uses execution and egress occurrence correlation to recover the original work and checks existing decisions for each applicable router before any new routing obligation. It needs no additional durable record solely to preserve the list. Existing acknowledgement and durable routing obligations still apply.
- The common strategy, responsibilities, failure analysis and evidence sections now distinguish originating work construction, subscription undertaking construction and direct Praxis resolution. Preserve known sibling outcomes and explicit uncertainty throughout. A broker-local transaction establishes no cross-resource atomicity with Mneme/Mnemosyne. The separate Kleio audit and Mnemosyne candidates retain pending standing. [Revised DRP][candidate]

## Continuum correspondence and residual differences

| Surface | Existing correspondence and remaining conflict or gap |
| --- | --- |
| Ergon Egress, PragmaFactory and publication | The C29 endpoint preserves execution encapsulation and the Ergon model says outputs may yield multiple Pragma. Mneme Pragma Management and PragmaFactory supply governed construction and management. Revision 002 places the request at Ergon Egress under the outbound Themis PEP. Map the execution-side caller and durable handover without allocating a new endpoint/API or moving construction into Petasos. Prior qualified Pragma Management describes filtering egress Pragma and Mneme semantic selection. Preserve the existing G03 candidate's PublishingMask eligibility and Petasos matching decisions and the separate continuation role while their canonical correspondence remains pending. [Ergon][ergon-model] · [C29][endpoint] · [Pragma Management][pragma-management] · [Factory][factory] · [Themis G01][g01] · [Publication candidate][publication] |
| Praxis and TaskOutcome | Qualified Ponos/Praxis/Pragma contributions assign output processing and routing to Ponos/Ergo. U18 describes continuation as a role and leaves open whether an existing output Pragma is designated or another representation is used. Revision 002 selects the existing-output approach for the bounded direct Praxis path, with Ponos evaluating and Petasos effecting routing. Canonical representation of that seam, filter/criteria and evaluation semantics, and the exact trigger and result contract remain open. Energeia retains Praxis management. The existing TaskOutcome Topic describes parent resolution. Preserve its standing and the open correspondence to actual Ergon-instance processing and disposition. [Ponos][ponos] · [Praxis][praxis-model] · [Pragma / U18][pragma-topic] · [TaskOutcome Topic][task-outcome] |
| PetasosParcel and work-realisation | Retain the approved exclusive Artemis routing mandate and by-value Parcel semantics. The qualified ActionableTask handoff describes a parcel referencing originating work and consumer-side undertaking establishment. The G03 subscription candidate instead establishes a Pragmatosis before handoff and parcels that undertaking identity. The new direct Praxis path routes existing originating Pragma identities while leaving the actual parcel referent, work-realisation/receiving role and relationship to the older handoff contract open. A similarity of referent does not settle the receiving role or establish a new undertaking. [PetasosParcel Topic][parcel] · [Routing mandate][mandate] · [Conceptual Parcel/ParcelMask][mask] · [Petasos–Mneme draft][handoff] |
| Authoritative Provenance | Accepted Provenance Management supports durable, governed references to Ergon activity and Pragma through Dokimasia, Mneme and Mnemosyne. The current contract does not establish an execution-attempt identity, a complete egress-set proof, or a search endpoint with that result. The candidate requires adequate evidence and recovery through the existing authority. It preserves the five-contract boundary and R1/R2 Summary-only governed search. [Provenance][provenance] · [Accepted Provenance Management][prov-review] · [Accepted persistence contracts][persistence] |

AX-04, AX-05 and AX-06 preserve reuse of existing machinery and authoritative work/provenance boundaries. AX-07 and AX-08 preserve protected operations and meaningful lineage evidence. AX-14/15/16 preserve the egress, handover, decision and routing outcomes and their uncertainty. AX-17/18 preserve qualified responsibilities and explicit unresolved correspondence. This revision captures the issued decisions at candidate level.

## Completeness and PF-07/PF-08

**Provenance completeness.** Require attributable identity for the specific Ergon execution and egress occurrence, distinguishing repeats of the same Ergon or input. Evidence must establish exact membership, completion, sufficient durable persistence and original identity/version/lineage correspondence. Include a known empty set where applicable and retain ordering only where the governing contract makes it meaningful. Distinguish a partial set and indeterminate construction from a complete set with a lost handover reply. Treat one router's decision as evidence for that router's own processing. Query results or missing acknowledgements alone supply no proof of complete output or of handover non-occurrence. Exact proof representation, access, retention and authoritative recovery rules remain open. [Candidate recovery][recovery]

| Exception | Progress and remaining contract |
| --- | --- |
| **PF-07 · Partially resolved** | The candidate settles the guarded egress-to-Petasos sequence and provenance-based recovery of a complete list. Still establish trigger/ACK ownership and what each acknowledgement establishes, authoritative egress completion and applicable identity/version evidence, safe reconciliation when list creation or handover is indeterminate, and the evidence and ownership needed to prove per-router receipt, durable decisions, queue acceptance and safe re-injection. Preserve audit and external ACK qualifications. |
| **PF-08 · Unresolved at contract level** | The candidate identifies the same egress occurrence, the same existing work identity and the same recorded decision as the recovery basis. Still decide exact execution/egress/decision/obligation and delivery-attempt correspondence across repeats and replay, multiple selections of the same Pragma and concurrent recovery, re-injection conditions and evidence, and execution eligibility, timing and `DoNotBefore` at its owning boundary. A new authorised decision or deliberate replay retains a separate identity and lineage contract. |

The previous PF register and both router evidence sets retain their standing. Later verification must exercise blocked tasks and denied/unavailable PEP decisions, complete including empty, partial and indeterminate egress, lost handover ACK, zero/one/both routers processed, repeated executions and repeated selection of a single work identity. Confirm existing effects and durable decisions survive restart, matching is preserved, and no new originating work or unauthorised execution follows from recovery. [Prior register][register] · [Revised conformance expectations][conformance]

## Checks and disposition

- **PASS** · Bounded offline Java 21 ArchUnit reactor exited 0. Maven reported **90 tests**, no failures, errors or skips, in 27.145 seconds. This is existing-software evidence. Neither the candidate routing behavior nor authoritative-provenance recovery was exercised.
- **PASS** · Checked 116 local link targets/fragments before Report emission. Verified Draft.000.001 SHA-256 `c0930af415c09c63af4fd637e58118a1a2bcdc976fe2dc043b98ded9061a35dc`, the bounded Subscription-Router change and unchanged pseudocode, the Praxis correction, the recovery cases and prior HCRL text and evidence references. Verified the worktree scope, pre-existing changes, HEAD, refs, staged content and Architecture 1.1.26. Draft.000.002 SHA-256 `46db01f8321aa710e7618b71b0c694421a6f36b526eea270fc5a8bf6d647d71e`.
- **FAIL** · `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 005` exited 1 with `No corresponding HCRL for software-0.1.0-005`. The task places the evidence in 005 and records review in cumulative HCRL.004. Whole-iteration validation still requires the matching record or explicit governance disposition.
- **UNRESOLVED** · The attributable original instruction-entry time is unavailable. The Task preserves the instruction with unresolved timestamp and filename prefix for Solution Architect treatment before closure.

Worktree changes are confined to Draft.000.002, its Task/Report pair and the bounded [DR-12 addition][hcrl]. HCRL.004 and DR-12 remain pending.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-g03-petasos-routing-draft-000-002-task.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[previous]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.001.md
[candidate]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md
[egress]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md#21-ergon-egress-creates-and-hands-over-originating-pragmas
[subscription]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md#3-petasossubscription-router
[praxis]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md#4-petasospraxis-router
[recovery]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md#51-ergon-egress-handover-recovery-from-authoritative-provenance
[conformance]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md#8-conformance-expectations
[original-report]: 20261121T213340Z-g03-petasos-routing-and-reconciliation-report.md
[register]: ../G03-Pragma-Family-Adjudications-005.md#pf-01pf-12-disposition
[ergon-model]: ../../../../../../continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md#architectural-content
[endpoint]: ../../../../../../continuum/application/application-interface/800789f54ba24f53a354600d5cd43f6f.md#architectural-content
[pragma-management]: ../../../../../../continuum/application/application-function/ab0cdaa7ed4149ee856216efa5a265c4.md#architectural-content
[factory]: ../../../../../../continuum/application/application-component/75e77d8dfd1d4870aa6151485d28d1dd.md#architectural-content
[ponos]: ../../../../../../continuum/application/application-component/b521762c1ea444b48cd5f66be3752d30.md#architectural-content
[praxis-model]: ../../../../../../continuum/application/data-object/0f961462340348b290fd844357d54d67.md#architectural-content
[pragma-topic]: ../../../../../../continuum/application/data-object/27ad143e7c784e378556af8a57c438f7.md#architectural-content
[task-outcome]: ../../../../../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md#architectural-content
[parcel]: ../../../../../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md#architectural-content
[mandate]: ../../../../../../continuum/motivation/constraint/mandate/e024703b32c5437481ce290e8e50b079.md#approved-definition
[provenance]: ../../../../../../continuum/application/data-object/e282811b51274649816566aac7edeb8a.md#architectural-content
[prov-review]: ../../../../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-18
[persistence]: ../../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[publication]: ../../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.003.md
[mask]: ../../../../../offline-design/DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md#4-subscription-and-publication-relationship
[handoff]: ../../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.000.004.md
[g01]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#4-architectural-definition
