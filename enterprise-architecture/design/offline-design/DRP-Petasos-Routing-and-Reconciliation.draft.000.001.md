<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal — Petasos Routing and Reconciliation

**Status** · Draft.000.001 · Pending Solution Architect review  
**Basis** · Architecture 1.1.26 · Software 0.1.0 · G03 / Iteration 005 / DR-12  
**Solution Architect** · Mark Hunter. [Operative instruction][task] · [Cumulative HCRL.004 / DR-12][hcrl] · [G03 adjudications][prior]

## 1. Common strategy

`Petasos.Subscription-Router` fulfils subscription matches determined by Petasos. `Petasos.Praxis-Router` fulfils successor-work obligations determined by Ponos's evaluation of the applicable Praxis. Both use the existing PragmaFactory, governed persistence and Petasos durable handoff and recovery capabilities. The common strategy is a contract for staged outcomes and reconciliation. It does not require a shared routing-event superclass, a new workflow or routing engine, or another HA/DR framework. The router and record names are candidate design names. [Petasos][petasos] · [PragmaFactory][factory] · [Work recorded before acknowledgement][recorded] · [Recovery preserves lineage][lineage]

1. Obtain the applicable decision from its owning mechanism. Include the triggering occurrence, the decision basis and its complete obligation set, including an empty set where applicable.
2. Establish the decision and complete set durably through Mneme/Mnemosyne **before attempting any individual obligation**. A transient or partially established record is insufficient. If creation of the decision record is indeterminate, reconcile against the authoritative record or trigger correspondence before proceeding.
3. For each applicable obligation, request the appropriate Pragma-family construction from Mneme's PragmaFactory, or recover the existing work with the same governed identity. Establish authoritative work and resolvable lineage through Mneme/Mnemosyne for handoff.
4. Petasos injects work for the obligation into its appropriate purpose-defined queue. Use the existing by-value PetasosParcel and Artemis routing mandate. Record evidence of durable acceptance under the applicable Petasos contract before any acknowledgement that claims it.
5. Through Mneme, record successful construction and queue injection against the matching obligation. Preserve distinct evidence for authoritative work and queue acceptance, including when only one effect is established.
6. Following interruption, recover the recorded decision and obligations. Reconcile each incomplete or uncertain obligation with the owning boundary's evidence. Retain successful siblings and earlier acknowledged effects. A permitted retry continues the same obligation and work identity. A new evaluation or deliberate replay requires its own governed decision.

The routing decision and complete obligation set are the durable basis of recovery. Later revisions to subscription, Praxis or other selection inputs do not change that basis. Any necessary new decision must be authorised and linked to the original. Applicable current security policy still governs each protected operation on recovery. Neither a stored decision nor a record update grants authority to disclose, forward or execute. [Canonical Principles][axioms] · [Themis authorisation][g01] · [Petasos–Mneme durability and recovery][handoff]

## 2. Component responsibilities

| Responsibility | Owner | Worker | Involved |
| --- | --- | --- | --- |
| Subscription evaluation and matching | Petasos | Petasos | Mneme |
| Praxis evaluation service and progression rules | Ponos | Ponos | Petasos |
| Praxis routing orchestration | Petasos | Petasos | Ponos |
| Pragma-family construction for an obligation | Requesting component, here Petasos | Mneme PragmaFactory | — |
| Queue routing, injection, management and delivery | Petasos | Petasos | — |
| Reconciliation of routing obligations | Petasos | Petasos | Mneme |

Energeia retains management of Praxis, Pathway and Journey. Ponos owns execution and interpretation of the applicable Praxis for progression, including the evaluation method/API service invoked by Petasos. Praxis provides the rules and operational envelope. Petasos orchestrates construction requests and fulfilment of the resulting routing obligations. It leaves Praxis interpretation, Ergon execution eligibility, and execution retry/scheduling at their existing boundaries. [Ponos][ponos] · [Praxis][praxis] · [Scheduling authority][scheduler]

Mneme owns the PragmaFactory construction operation and applies the required model invariants. It provides governed information access and manages the routing records and constructed work through Mnemosyne, which establishes authoritative durability. Mneme neither interprets SubscriptionMask or Praxis nor makes queue-routing decisions. Themis provides applicable PEP/PDP capabilities and governs each protected boundary, including Ponos evaluation invocation, Mneme reads/writes and construction, Petasos FORWARD and Ergon admissions. The governing policy and trusted context remain distinct from functional ownership and correlation. [PragmaFactory][factory] · [G01 design][g01] · [AX-07][ax07] · [Accepted persistence contracts][persistence]

Petasos, Ponos and Mneme supply semantically meaningful auditable occurrences for their respective operations as applicable policy requires. Kleio owns Audit Capture and construction of authoritative FHIR AuditEvents and uses its governed capture authority. If mandatory durable capture cannot be established, the affected operation fails closed. Routing records, queue metrics and observations serve their own purposes. The pending Petasos–Kleio staging proposal supplies no authority to change the capture gate. [Kleio][kleio] · [Pending audit candidate][audit-draft]

## 3. Petasos.Subscription-Router

Petasos evaluates applicable subscriptions against an eligible published IU and originating Pragma using its subscription view. Mneme provides governed information. R1/R2 matching uses Pragma and IU summary/metadata. R3+ may additionally evaluate authorised content under the owning access/disclosure contract. Publication eligibility, matching and Themis authorisation are separate. Preserve same-context information and conveyance criteria under the conceptual Parcel/ParcelMask design. Pre-parcel criteria and unavailable-input treatment remain under PF-06 and related DR-10 obligations. `SubscriptionCache` describes the illustrative input view without prescribing another cache allocation. [Publication and subscription §§4.1–4.2][publication] · [Conceptual Parcel/ParcelMask][parcel-mask]

```java
SubscriptionCache subscriptionCache = parseSubscriptionCache();
SubscriberSet subscriberSet =
    determineSubscribers(subscriptionCache, publishedIU, pragma);

SubscriptionIdEvent event = mneme.createSubscriptionIdEvent(subscriberSet);

for (SubscriberEntry entry : subscriberSet) {
    Pragmatosis undertaking = mneme.createNewPragmatosis(entry);
    queueNewPragmatosis(undertaking);
    mneme.updateSubscriptionIdEvent(event, entry, undertaking);
}
```

This illustrates the confirmed path. Durable creation of the event includes originating publication identity and the full SubscriberSet. Work must be established through the governed persistence path before injection. Confirmations and exceptions follow §§5–6. Pseudocode names and signatures do not allocate canonical types or interfaces. [Operative instruction][task]

**SubscriptionIdEvent** records the originating publication and its applicable identity, decision basis or version correspondence, the resolved SubscriberSet and subscriber/subscription associations, each intended Ergon queue from its subscription, references to any established Pragmatosis, and distinct construction and queue-injection outcomes and evidence. The initial `Successful-Pragmatosis-Creation+RouteInjection` value is `NULL`: success of the combined obligation has yet to be established. Both effects require evidence before combined success can be recorded. A NULL may be present after an actual effect or an indeterminate call. It supplies no proof of absence or failure. The exact representation of intermediate states, uncertainty and evidence remains open.

Each matching subscription has its own obligation even when two subscriptions identify the same subscriber. Within this G03 subscription-work pattern the parcel carries the established Pragmatosis identity and permitted handoff metadata. The IU reference and payload remain with governed access, and the receiving Ergon resolves the undertaking through Mneme. An empty resolved SubscriberSet establishes the no-match disposition and creates no undertaking or Ergon TaskOutcome. Recovery uses the existing SubscriptionIdEvent and its obligations without evaluating the current SubscriptionCache again. [Pragma work and outcomes][pragma] · [Petasos–Mneme handoff][handoff] · [Qualified prior Parcel account][parcel]

## 4. Petasos.Praxis-Router

**Ponos owns the Praxis evaluation method/API service. Petasos.Praxis-Router invokes it.** Ponos interprets the applicable Praxis and evaluates workflow progression. Petasos arranges the resulting handoffs within the durable decision. It does not derive new successors or reinterpret Praxis progression rules. The result can specify an appropriate specialised Pragma-family object for an obligation. The base type used below demonstrates the sequence and implies no universal successor representation. A processing outcome or progression event can trigger the decision. The decision, its selected successors, actual Ergon TaskOutcome, undertaking/parent disposition, output mediation and Assurance remain distinct. [Praxis][praxis] · [Ponos][ponos] · [Pragma model §4.8][outcomes] · [Existing TaskOutcome Topic][task-outcome]

```java
PraxisRoutingResult result = ponos.evaluatePraxis(praxisContext);
PraxisRoutingEvent event = mneme.createPraxisRoutingEvent(result);

for (PraxisRoutingEntry entry : result.entries()) {
    Pragma work = mneme.createPragma(entry);
    queueNewWork(work);
    mneme.updatePraxisRoutingEvent(event, entry, work);
}
```

This illustrates the confirmed path. The full Ponos result and successor set become durable before construction. Use the applicable work-handoff contract for the selected successor type, with a by-value PetasosParcel on Artemis. The exact trigger/result contract, destination correspondence and parcel referent for specialised successors remain for review. The illustration prescribes no API names or signatures. [Operative instruction][task] · [Artemis routing mandate][routing-mandate]

The provisional **PraxisRoutingEvent** identifies the triggering processing outcome or progression occurrence, the relevant Praxis identity and revision, the decision returned by Ponos, the complete set of successor obligations, identities of any created Pragma-family objects, queue-injection outcomes and the correlation/recovery evidence. Preserve the outcome and source-work lineage and the applicable scheduling and specialised execution constraints. Once the Ponos result is durably captured, recovery uses that result and does not automatically invoke Ponos again. A new evaluation requires a separately governed decision. Route recovery preserves the recorded result while newer Praxis versions govern new decisions through the owning service. [Pragma Topic and continuation][pragma-topic] · [Recovery preserves lineage][lineage]

## 5. Failure and recovery analysis

The rules apply to both router designs. An operation may have a confirmed success, confirmed failure or indeterminate result. An unrecorded or unattempted stage, where knowable, remains distinguishable from those results. This draft leaves the encoding and lifecycle of those facts open. [Explicit Indeterminate Outcome][indeterminate] · [Persistence contracts][persistence]

| Point or case | Required handling |
| --- | --- |
| Before durable decision creation | If no durable decision was established, recover the original trigger through its owning handoff or progression contract. Source, custody, acknowledgement timing and how to recover any evaluation result not yet captured need a contract. If decision creation itself is uncertain, first reconcile whether that decision already exists. |
| After confirmed durable record creation | Resume from the recorded decisions and full obligation set. A cache view or a fresh Ponos evaluation supplies no replacement. |
| During PragmaFactory construction or work persistence | Retain uncertainty when creation or durable establishment is indeterminate. Look up the already established work, if any, through governed identity and obligation correspondence. Proceed only with established work and applicable durable lineage. |
| After successful work establishment, before queue injection | Recover the existing identity and the outstanding injection obligation. If the crash position is uncertain, reconcile whether the send occurred before repeating it. |
| During Artemis injection | Use the applicable configured broker durability and routing guarantees and Petasos evidence to distinguish confirmed acceptance, confirmed failure and indeterminate commit. Receipt, accepted durable enqueue, delivery and execution are separate. Preserve uncertainty after a lost reply. |
| After successful injection, before routing-record update | Confirm the original acceptance with evidence attributable to the obligation/attempt. Record that outcome without resending. A NULL or missing update, or a queue lookup with no message, does not prove the earlier send failed. |
| During routing-record update | Recover the authoritative record and compare its revision, obligation and evidence. Reconcile the update and its underlying effects separately. Preserve known facts and prevent stale recovery from replacing evidenced progress. |
| Partial fan-out or competing recovery | Recover individual incomplete or uncertain obligations. Preserve successful siblings, stable identities and independently attributable attempts. Establish control and update rules that prevent a second work identity or loss of progress if two workers recover the same obligation. |
| Subscription or Praxis definition changes | Preserve the recorded decision and the definition/version basis for this obligation set. Authorisation is checked per protected operation at its applicable boundary. An authorised new decision records its relationship to the original with its own basis. |
| Duplicate delivery | Recognise a repeat delivery of the same obligation and preserve identity. Ponos and the execution boundary determine execution eligibility from governed state, including any applicable `DoNotBefore`, authority and prior execution or recovery conditions. Transport redelivery creates no execution authority. |
| Deliberate replay | Use an explicitly designated replayable boundary. Establish the identity of the new processing attempt and link it to the original through correlation and provenance. The replay contract must state whether it reuses or creates a routing decision, obligations or work, and whether it includes an authorised new evaluation. Preserve the original decision and effects. |

Artemis 2.33.0 supports broker-local commit acknowledgement under the applicable persistence settings and finite, per-address duplicate detection, which can affect an entire transaction. Detection depends on configuration, history, persistence and failover. Queue absence is inconclusive because a parcel might already have been delivered, acknowledged or moved. Broker detection can support a permitted retry using the same send identity within its proved scope. It supplies neither permanent exactly-once work execution nor the missing authoritative work/acknowledgement evidence. The configured broker, store and HA behaviour require verification. [Commit guarantees][artemis-commit] · [Duplicate detection][artemis-duplicates] · [Redelivery][artemis-redelivery] · [Artemis HA][artemis-ha]

## 6. Minimum identity and evidence for reconciliation

| Boundary | Required correspondence and evidence subject |
| --- | --- |
| Decision and trigger | Identify the originating publication or progression occurrence, decision identity, applicable definition identity/revision or governed decision basis, and complete obligation set. Evidence must make it possible to recover that same decision after a lost create response. |
| Obligation and work | Identify the obligation within its decision, and its subscription/subscriber or successor-work role and intended destination. A recovered obligation must identify or resolve the same governed Pragma-family object with its recoverable authoritative identity, applicable revision and lineage. Choose an owning correlation or recoverable association that survives a lost construction acknowledgement. |
| Handoff | Attribute a parcel and send attempt to the obligation, work identity, intended destination and acknowledgement boundary. Retain enough evidence of actual routing and durable acceptance under Petasos so a later observation can establish successful injection after a lost record update. Deployment and contract determine whether the necessary evidence is available. |
| Subsequent effects | Keep delivery acknowledgement, Ergon admission/execution and outcome, Mneme's governed save and authoritative information mutation separately attributable. Identify who issued each acknowledgement, which fact it attests and its outcome. Evidence of one stage supplies only that stage's conclusion. |

These are semantic minima, not prescribed field names, key formats, retention periods, state encodings, claim/locking mechanisms or queries. Recovery needs sufficient owning evidence to resume, hold or escalate an obligation. Where the result remains indeterminate, withhold any dependent acceptance claim. Exact negative-proof threshold, safe re-injection conditions, and authoritative evidence after delivery or retention expiry require contract review. [Accepted Work Recorded Before Ack][recorded] · [Petasos–Mneme evidence and recovery][handoff] · [G03 PF-07/PF-08][prior]

The authoritative routing record and constructed work use the existing Mneme/Mnemosyne contracts. Artemis acceptance uses a separate Petasos transaction boundary. A broker-local transaction does not persist the routing-record update or authoritative work. Preserve the accepted SummaryWriter limits, authoritative version rules and multi-site durable mutation Success. The routing records' representation and mapping to applicable existing write/read contracts require adjudication. The proposed Mnemosyne DurableInformation/DurableData decomposition is a separate pending candidate. [Accepted persistence contracts][persistence] · [Pending Mnemosyne candidate][durable-data]

## 7. Architecture correspondence and open decisions

- **Subscription allocation and handoff.** G03 assigns Petasos matching and pre-handoff construction requests through Mneme. The qualified Petasos and Parcel descriptions assign semantic selection to Mneme and describe a parcel referencing originating work followed by consumer-side undertaking creation. Preserve the by-value mandate and other parcel purposes. Reconcile the allocation, changed reference/ordering and canonical Pragma/Pragmatosis correspondence before incorporating this candidate. [Petasos][petasos] · [PetasosParcel Topic][parcel] · [G03 adjudications][prior]
- **Praxis continuation.** Qualified Ponos, Praxis and Pragma descriptions assign output processing and routing to the Ponos/Ergo execution framework. G03 identifies Ponos as the evaluator and Petasos as orchestrator of the resulting construction, queue injection and reconciliation. The new Ponos service and exact split of result/continuation and concrete routing need canonical correspondence. Energeia's model ownership and Ponos's progression semantics remain as directed. Determine the owning trigger/outcome and successor-type contracts, preserving the TaskOutcome Topic's qualified parent-resolution meaning pending its G03 correspondence decision. [Ponos][ponos] · [Praxis][praxis] · [Pragma Topic][pragma-topic] · [Existing TaskOutcome Topic][task-outcome]
- **Identity, acknowledgement and retry.** PF-07 now has candidate ordering and decision/obligation evidence subjects. Exact acknowledging boundaries, sufficient recovery proofs, handling before decision capture, distinct cross-boundary update results and audit interaction remain open. PF-08 now distinguishes same-record recovery from new decisions. Exact equivalence and identities for re-publication, changed definitions, scheduled execution and deliberate replay, and safe/concurrent re-injection proofs remain open. [G03 adjudications][prior] · [DR-12][hcrl]
- **Model and contract.** Adjudicate whether existing work/handoff information and feature contracts can express the two records and their lifecycle, including source/definition revision, correlation, current Themis binding, eligibility after disruption, retention and governing persistence. Keep protected access and fail-closed capture. Approval of this candidate's semantics would precede any governed Continuum mapping or software assignment. [PragmaFactory][factory] · [G01 design][g01] · [Architecture Review Praxis][review-praxis]

## 8. Conformance expectations

For both routers, verify the full decision is durably recoverable before the first construction, the corresponding source trigger can be recovered before that point, and a restart after capture uses the same decisions without rerunning Subscription matching or Ponos evaluation. Verify same-subscriber multiple subscriptions, empty results, specialised successors and unchanged decision versions across definition updates. Exercise timeouts and crash gaps at decision creation, work construction/persistence, broker commit and record update, including lost replies, post-consumption queue absence, duplicate detection expiry, partial and concurrent recovery, independently authorised replay and `DoNotBefore`. Demonstrate that resolved sibling evidence is retained, indeterminate outcomes remain explicit, and retry or redelivery cannot create a second work identity or authorise execution. Verify Themis denial/unavailability, Kleio mandatory capture, lineage, and successful acknowledgement at each owning boundary using the actual selected durability configuration. These are requirements for later design and implementation evidence.

**Review disposition** · Candidate pending Solution Architect review in DR-12. The authorised functional allocation is the design input. Canonical correspondence, the detailed contracts in §7 and runtime verification require their own review. [DR-12][hcrl]

[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-routing-and-reconciliation-task.md
[hcrl]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[prior]: ../evidence/software-0.1.0/005/after/G03-Pragma-Family-Adjudications-005.md#pf-01pf-12-disposition
[axioms]: ../../governance/architectural-axioms.md
[ax07]: ../../continuum/motivation/principle/architectural-axiom/5de2ae2c3b794c2d9f3a14efd51d564f.md
[petasos]: ../../continuum/application/application-component/195e5f10db8e419391354197b5d7a8cc.md#architectural-content
[ponos]: ../../continuum/application/application-component/b521762c1ea444b48cd5f66be3752d30.md#architectural-content
[praxis]: ../../continuum/application/data-object/0f961462340348b290fd844357d54d67.md#architectural-content
[factory]: ../../continuum/application/application-component/75e77d8dfd1d4870aa6151485d28d1dd.md#architectural-content
[pragma-topic]: ../../continuum/application/data-object/27ad143e7c784e378556af8a57c438f7.md#architectural-content
[parcel]: ../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md#architectural-content
[routing-mandate]: ../../continuum/motivation/constraint/mandate/e024703b32c5437481ce290e8e50b079.md#approved-definition
[task-outcome]: ../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md#architectural-content
[recorded]: ../../continuum/strategy/capability/feature/2f0cf045d3914c03adfcb87dea623978.md
[lineage]: ../../continuum/strategy/capability/feature/157b20d5c544429a8ada854c45ec244f.md
[scheduler]: ../../continuum/strategy/capability/feature/48977be5d3b24f82ba676a0b8a26e0d6.md
[indeterminate]: ../../continuum/motivation/requirement/fc732b64fc564127b73174e28d9ccce8.md
[kleio]: ../../continuum/application/application-component/fefda110e2274884ad58906733606efd.md#auditability-and-availability
[persistence]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[pragma]: DRP-Mneme-Pragma-Model.draft.000.004.md#4-architectural-definition
[outcomes]: DRP-Mneme-Pragma-Model.draft.000.004.md#48-execution-outcomes-and-work-disposition
[handoff]: DRP-Interaction-Petasos-to-Mneme.draft.000.004.md#4-architectural-definition
[publication]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#4-architectural-definition
[parcel-mask]: DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md#4-subscription-and-publication-relationship
[g01]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#4-architectural-definition
[audit-draft]: DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md
[durable-data]: DRP-Mnemosyne-DurableInformation-and-DurableData.draft.000.001.md
[review-praxis]: ../../governance/architecture-review-praxis.md
[artemis-commit]: https://github.com/apache/artemis/blob/2.33.0/docs/user-manual/send-guarantees.adoc
[artemis-duplicates]: https://github.com/apache/artemis/blob/2.33.0/docs/user-manual/duplicate-detection.adoc
[artemis-redelivery]: https://github.com/apache/artemis/blob/2.33.0/docs/user-manual/undelivered-messages.adoc
[artemis-ha]: https://github.com/apache/artemis/blob/2.33.0/docs/user-manual/ha.adoc
