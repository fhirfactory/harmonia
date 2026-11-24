<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal — Petasos Routing and Reconciliation

**Status** · Draft.000.002 · Pending Solution Architect review  
**Basis** · Architecture 1.1.26 · Software 0.1.0 · G03 / Iteration 005 / DR-12  
**Solution Architect** · Mark Hunter. [Revision 002 instruction][task] · [Reviewed Draft.000.001][predecessor] · [Cumulative HCRL.004 / DR-12][hcrl] · [G03 adjudications][prior]

## 1. Common strategy

Both routers receive the governed originating Pragmas created and handed over by Ergon Egress. `Petasos.Subscription-Router` fulfils subscription matches determined by Petasos, requesting a Pragmatosis for each matching subscription. `Petasos.Praxis-Router` routes the existing Pragmas selected by Ponos for the applicable Praxis progression. Both use governed persistence and the existing Petasos durable handoff and recovery capabilities. The common strategy is a contract for staged outcomes and reconciliation. It does not require a shared routing-event superclass, a new workflow or routing engine, or another HA/DR framework. The router and record names are candidate design names. [Petasos][petasos] · [PragmaFactory][factory] · [Work recorded before acknowledgement][recorded] · [Recovery preserves lineage][lineage]

1. Obtain the applicable decision from its owning mechanism. Include the triggering occurrence, the decision basis and its complete obligation set, including an empty set where applicable.
2. Establish the decision and complete set durably through Mneme/Mnemosyne **before attempting any individual obligation**. A transient or partially established record is insufficient. If creation of the decision record is indeterminate, reconcile against the authoritative record or trigger correspondence before proceeding.
3. For each obligation, resolve the applicable governed work and lineage through Mneme/Mnemosyne. Subscription-Router requests or recovers the subscriber-specific Pragmatosis and establishes its authoritative persistence before injection. Praxis-Router resolves and routes the existing originating Pragma and preserves its identity.
4. Petasos injects work for the obligation into its appropriate purpose-defined queue. Use the existing by-value PetasosParcel and Artemis routing mandate. Record evidence of durable acceptance under the applicable Petasos contract before any acknowledgement that claims it.
5. Through Mneme, record queue-injection outcomes against the matching obligation. Record successful Pragmatosis construction for Subscription-Router and the resolved existing Pragma identity for Praxis-Router. Preserve separate evidence of durable work, routing-decision persistence and queue acceptance.
6. Following interruption, recover the recorded decision and obligations. Reconcile each incomplete or uncertain obligation with the owning boundary's evidence. Retain successful siblings and earlier acknowledged effects. A permitted retry continues the same obligation and work identity. A new evaluation or deliberate replay requires its own governed decision.

The routing decision and complete obligation set are the durable basis of recovery. Later revisions to subscription, Praxis or other selection inputs do not change that basis. Any necessary new decision must be authorised and linked to the original. Applicable current security policy still governs each protected operation on recovery. Neither a stored decision nor a record update grants authority to disclose, forward or execute. [Canonical Principles][axioms] · [Themis authorisation][g01] · [Petasos–Mneme durability and recovery][handoff]

| Concern | Subscription-Router | Praxis-Router |
| --- | --- | --- |
| Input | Existing originating Pragmas | Existing originating Pragmas |
| Decision | Petasos subscription matching | Ponos Praxis evaluation service |
| Selection basis | Subscription definitions and applicable masks | Praxis progression rules and potential output filters |
| Originating Pragma construction | None | None |
| Subscriber-specific Pragmatosis construction | Through Mneme, one per applicable subscription | Not established for direct Praxis routing |
| Routed work | Subscriber-specific Pragmatosis | Selected existing originating Pragma |
| Durable routing record | SubscriptionIdEvent | PraxisRoutingEvent, provisional name |
| Queue injection and reconciliation | Petasos | Petasos |

## 2. Component responsibilities

| Responsibility | Owner | Worker | Involved |
| --- | --- | --- | --- |
| Subscription evaluation and matching | Petasos | Petasos | Mneme |
| Praxis evaluation service and progression rules | Ponos | Ponos | Petasos |
| Praxis routing orchestration | Petasos | Petasos | Ponos |
| Originating Pragma list creation and handover | Ergon Egress | Ergon Egress with Mneme PragmaFactory for construction | Petasos receives the list |
| Subscriber-specific Pragmatosis construction | Petasos requests construction | Mneme PragmaFactory | — |
| Queue routing, injection, management and delivery | Petasos | Petasos | — |
| Reconciliation of routing obligations | Petasos | Petasos | Mneme |

Energeia retains management of Praxis, Pathway and Journey. Ponos owns execution and interpretation of the applicable Praxis for progression, including the evaluation method/API service invoked by Petasos. Praxis provides the rules and operational envelope. Petasos orchestrates fulfilment of the resulting routing obligations. It requests subscriber-specific construction on the subscription path and resolves existing work on the Praxis path. It leaves Praxis interpretation, Ergon execution eligibility, and execution retry/scheduling at their existing boundaries. [Ponos][ponos] · [Praxis][praxis] · [Scheduling authority][scheduler]

Mneme owns the PragmaFactory construction operation and applies the required model invariants. It provides governed information access and manages the routing records, originating Pragmas and any subscriber-specific undertakings through Mnemosyne, which establishes authoritative durability. Mneme neither interprets SubscriptionMask or Praxis nor makes queue-routing decisions. Themis provides applicable PEP/PDP capabilities and governs each protected boundary, including Ponos evaluation invocation, Mneme reads/writes and construction, Petasos FORWARD and Ergon admissions. The governing policy and trusted context remain distinct from functional ownership and correlation. [PragmaFactory][factory] · [G01 design][g01] · [AX-07][ax07] · [Accepted persistence contracts][persistence]

Ergon Egress, Petasos, Ponos and Mneme supply semantically meaningful auditable occurrences for their respective operations as applicable policy requires. Kleio owns Audit Capture and construction of authoritative FHIR AuditEvents and uses its governed capture authority. If mandatory durable capture cannot be established, the affected operation fails closed. Routing records, queue metrics and observations serve their own purposes. The pending Petasos–Kleio staging proposal supplies no authority to change the capture gate. [Kleio][kleio] · [Pending audit candidate][audit-draft]

### 2.1 Ergon Egress creates and hands over originating Pragmas

Ergon Egress produces the originating Pragma list from eligible outputs of completed processing. Apply dead-task blocking, then the applicable outbound Themis PEP. Ergon Egress requests construction through Mneme's PragmaFactory, establishes governed persistence of the resulting Pragmas through Mneme/Mnemosyne, and hands the created list to Petasos. Petasos then performs the applicable routing. The illustration is an architectural sequence. Method names, structure and placement of implementation code remain open. [Ergon][ergon] · [Ergon endpoint boundary][ergon-endpoint] · [Themis outbound enforcement][g01] · [PragmaFactory][factory] · [Revision 002 instruction][task]

```java
List<Pragma> createdList =
    Ergon.Egress
        .PostBlockingOfDeadTasks()
        .PostPEP
        .CreatePragmaFromOutputList();

Ergon.Egress.HandoverToPetasos(createdList);

Petasos.doStuff();
```

Dead-task blocking and the release decision are distinct from the Ergon-instance TaskOutcome and any undertaking or originating-work disposition. Preserve the prior G03 PublishingMask decision for publication: Ponos owns eligibility and Mneme evaluates the producing Ergon's mask before downstream publication Pragma creation. Governed save, output mediation and Praxis continuation retain their own contracts. Inclusion in the list supplies neither publication, disclosure or execution authority nor evidence of Petasos acceptance. Handover receipt, subsequent queue acceptance and later Ergon execution are distinct. [Publication and subscription][publication] · [Pragma outcomes][outcomes] · [Themis outbound enforcement][g01]

## 3. Petasos.Subscription-Router

Subscription-Router receives already-created originating Pragmas from Ergon Egress. Petasos evaluates applicable subscriptions against an eligible published IU and originating Pragma using its subscription view. Mneme provides governed information. R1/R2 matching uses Pragma and IU summary/metadata. R3+ may additionally evaluate authorised content under the owning access/disclosure contract. Publication eligibility, matching and Themis authorisation are separate. Preserve same-context information and conveyance criteria under the conceptual Parcel/ParcelMask design. Pre-parcel criteria and unavailable-input treatment remain under PF-06 and related DR-10 obligations. `SubscriptionCache` describes the illustrative input view without prescribing another cache allocation. [Publication and subscription §§4.1–4.2][publication] · [Conceptual Parcel/ParcelMask][parcel-mask]

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

This illustrates the confirmed path. Durable creation of the event includes originating publication identity and the full SubscriberSet. Work must be established through the governed persistence path before injection. Confirmations and exceptions follow §§5–6. Pseudocode names and signatures do not allocate canonical types or interfaces. [Original routing instruction][prior-task]

**SubscriptionIdEvent** records the originating publication and its applicable identity, decision basis or version correspondence, the resolved SubscriberSet and subscriber/subscription associations, each intended Ergon queue from its subscription, references to any established Pragmatosis, and distinct construction and queue-injection outcomes and evidence. The initial `Successful-Pragmatosis-Creation+RouteInjection` value is `NULL`: success of the combined obligation has yet to be established. Both effects require evidence before combined success can be recorded. A NULL may be present after an actual effect or an indeterminate call. It supplies no proof of absence or failure. The exact representation of intermediate states, uncertainty and evidence remains open.

Each matching subscription has its own obligation even when two subscriptions identify the same subscriber. Within this G03 subscription-work pattern the parcel carries the established Pragmatosis identity and permitted handoff metadata. The IU reference and payload remain with governed access, and the receiving Ergon resolves the undertaking through Mneme. An empty resolved SubscriberSet establishes the no-match disposition and creates no undertaking or Ergon TaskOutcome. Recovery uses the existing SubscriptionIdEvent and its obligations without evaluating the current SubscriptionCache again. [Pragma work and outcomes][pragma] · [Petasos–Mneme handoff][handoff] · [Qualified prior Parcel account][parcel]

## 4. Petasos.Praxis-Router

**Ponos owns the Praxis evaluation method/API service. Petasos.Praxis-Router invokes it with the applicable Praxis context and the list of already-created originating Pragmas.** Ponos evaluates progression rules, including any applicable selection criteria or filters, to select existing Pragma identities for identified subsequent Ergons. Petasos fulfils those obligations without interpreting the Praxis. The filter model, applicable inputs and exact evaluation semantics remain open. A processing outcome or progression occurrence may trigger a decision. Actual Ergon TaskOutcome, undertaking and parent disposition, output mediation and Assurance remain independently attributable. [Praxis][praxis] · [Ponos][ponos] · [Pragma outcomes][outcomes] · [Existing TaskOutcome Topic][task-outcome]

```java
PraxisRoutingResult result =
    ponos.evaluatePraxis(praxisContext, createdList);

PraxisRoutingEvent event =
    mneme.createPraxisRoutingEvent(result);

for (PraxisRoutingEntry entry : result.entries()) {
    Pragma existingPragma = mneme.getPragma(entry.pragmaId());
    queueExistingPragma(existingPragma, entry.destinationErgon());
    mneme.updatePraxisRoutingEvent(event, entry, existingPragma);
}
```

This illustrates the confirmed path. Establish the complete Ponos result and obligation set durably before attempting fulfilment. Resolve each selected originating Pragma under the applicable Mneme contract. Praxis-Router routes the same identity to the selected destination. Several rules may select the same Pragma, including for different destinations. Preserve distinct obligations with enough rule/decision and destination correspondence to recover them without duplicating originating work. Identifiers, interfaces and the treatment of repeated selections for one destination require review. Continue to use by-value PetasosParcel and Artemis. The direct Praxis parcel/work-realisation contract remains open, including the exact referent and any receiving work role. This path establishes no new Pragmatosis construction requirement. Preserve specialised execution constraints. [Revision 002 instruction][task] · [Artemis routing mandate][routing-mandate] · [Qualified Parcel account][parcel]

The provisional **PraxisRoutingEvent** identifies the triggering execution or progression occurrence, the applicable Praxis identity and revision, the Ponos evaluation result, the selected existing originating Pragma identities and their lineage, destination Ergon identities, distinct obligations, queue-injection outcomes and recovery evidence. Recovery uses the durable result. A later Ponos invocation to re-evaluate would require a separately authorised new decision. [Pragma continuation][pragma-topic] · [Recovery preserves lineage][lineage]

## 5. Failure and recovery analysis

The rules apply to both router designs. An operation may have a confirmed success, confirmed failure or indeterminate result. An unrecorded or unattempted stage, where knowable, remains distinguishable from those results. This draft leaves the encoding and lifecycle of those facts open. [Explicit Indeterminate Outcome][indeterminate] · [Persistence contracts][persistence]

| Point or case | Required handling |
| --- | --- |
| Before durable decision creation | If no durable decision was established, recover the original trigger through its owning handoff or progression contract. For an Ergon Egress trigger use the applicable provenance recovery in §5.1 and reconcile existing Petasos decisions first. Trigger custody, acknowledgement timing and any uncaptured evaluation result remain open. If decision creation is uncertain, first establish whether the decision already exists. |
| After confirmed durable record creation | Resume from the recorded decisions and full obligation set. A cache view or a fresh Ponos evaluation supplies no replacement. |
| During work establishment or resolution | Preserve indeterminate creation or durable establishment at Ergon Egress or during subscriber-specific construction. Reconcile through governed identity, egress provenance or subscription-obligation correspondence as applicable. Praxis-Router resolves an existing identity and withholds injection if that resolution or its authority cannot be established. |
| After successful work establishment, before queue injection | Recover the existing identity and the outstanding injection obligation. If the crash position is uncertain, reconcile whether the send occurred before repeating it. |
| During Artemis injection | Use the applicable configured broker durability and routing guarantees and Petasos evidence to distinguish confirmed acceptance, confirmed failure and indeterminate commit. Receipt, accepted durable enqueue, delivery and execution are separate. Preserve uncertainty after a lost reply. |
| After successful injection, before routing-record update | Confirm the original acceptance with evidence attributable to the obligation/attempt. Record that outcome without resending. A NULL or missing update, or a queue lookup with no message, does not prove the earlier send failed. |
| During routing-record update | Recover the authoritative record and compare its revision, obligation and evidence. Reconcile the update and its underlying effects separately. Preserve known facts and prevent stale recovery from replacing evidenced progress. |
| Partial fan-out or competing recovery | Recover individual incomplete or uncertain obligations. Preserve successful siblings, stable identities and independently attributable attempts. Establish control and update rules that prevent a second work identity or loss of progress if two workers recover the same obligation. |
| Subscription or Praxis definition changes | Preserve the recorded decision and the definition/version basis for this obligation set. Authorisation is checked per protected operation at its applicable boundary. An authorised new decision records its relationship to the original with its own basis. |
| Duplicate delivery | Recognise a repeat delivery of the same obligation and preserve identity. Ponos and the execution boundary determine execution eligibility from governed state, including any applicable `DoNotBefore`, authority and prior execution or recovery conditions. Transport redelivery creates no execution authority. |
| Deliberate replay | Use an explicitly designated replayable boundary. Establish the identity of the new processing attempt and link it to the original through correlation and provenance. The replay contract must state whether it reuses or creates a routing decision, obligations or work, and whether it includes an authorised new evaluation. Preserve the original decision and effects. |

Artemis 2.33.0 supports broker-local commit acknowledgement under the applicable persistence settings and finite, per-address duplicate detection, which can affect an entire transaction. Detection depends on configuration, history, persistence and failover. Queue absence is inconclusive because a parcel might already have been delivered, acknowledged or moved. Broker detection can support a permitted retry using the same send identity within its proved scope. It supplies neither permanent exactly-once work execution nor the missing authoritative work/acknowledgement evidence. The configured broker, store and HA behaviour require verification. [Commit guarantees][artemis-commit] · [Duplicate detection][artemis-duplicates] · [Redelivery][artemis-redelivery] · [Artemis HA][artemis-ha]

### 5.1 Ergon Egress handover recovery from authoritative provenance

The original `createdList` can be reconstructed from governed, authoritative Provenance when the evidence establishes the complete output set for the particular Ergon execution and egress occurrence. This avoids a separate durable handover record solely to preserve the list. Retain existing handover outcome and durable router obligations. Dokimasia retains Provenance Management. Originating activity generates Provenance through the existing client responsibility, Mneme provides governed information access, and Mnemosyne establishes authoritative durable persistence. Reuse those contracts. The illustrative lookup does not require a new API, search/index design, durable coordination store or changes to the accepted R1/R2 Summary-only search boundary. [Provenance][provenance] · [Accepted Provenance Management][prov-review] · [Persistence contracts][persistence] · [Revision 002 instruction][task]

```java
List<Pragma> createdList =
    mneme.findPragmasByProvenance(
        originatingErgonExecutionIdentity,
        egressOccurrenceIdentity
    );

Ergon.Egress.HandoverToPetasos(createdList);
```

The illustration assumes a complete, authoritative reconstruction and a permitted handover. Reconciliation must check both applicable routers' existing decisions before new obligations are established. An existing decision uses its recorded result. If one router already handled the list and the other is applicable, preserve the first router's outcome and reconcile the second independently. A missing or inconclusive lookup does not establish absence. Apply current policy and the original identity, lineage and durability requirements when repeating or reconciling a handover. Never create more originating Pragmas merely because handover is uncertain.

| Egress or handover evidence | Recovery treatment |
| --- | --- |
| Complete authoritative originating Pragma set | Use verified completion evidence attributable to this egress occurrence, and recover its exact governed membership, including a confirmed empty set where applicable. Reconstruct the original list with its identities, applicable versions and lineage, and order only where the governing contract makes order meaningful. |
| Partially established output set | Preserve confirmed members and their authoritative persistence and lineage. An observed subset supplies no completion evidence. Reconcile construction under its owning operation before using the list as complete. |
| Indeterminate output construction | Preserve uncertainty and recover evidence under the owning persistence and output contracts. A lost reply, stale cache or empty lookup cannot establish the outcome or authorise duplicate construction. |
| Complete set with lost handover acknowledgement | Reconcile the original handover and routing decisions using the recovered list. If a repeat is authorised, reuse the original list identity basis and members. Do not reproduce originating work or treat a missing ACK as proof of failure. |
| Handover already handled by one or both routers | Recover each established decision and obligations. Preserve successful effects and avoid re-evaluating recorded decisions. A new decision for an applicable router requires an established need and authority under the owning contract. |

Minimum proof identifies the **particular execution occurrence**, the **egress occurrence**, and their attributable relationship to the complete set of resulting Pragma identities. It must distinguish legitimate re-execution of the same Ergon or input from recovery of one execution. Establish membership and completion with sufficient durable evidence to distinguish zero, partial and indeterminate outputs. The proof must support resolution of the same objects at the applicable versions with their lineage and under governed access. Individual Provenance references can establish membership facts. The contract still needs to specify what establishes complete membership and closure, including the evidence relationship to work persistence, retention, any defined ordering, unavailable evidence and recovery after a lost acknowledgement. A subset or count alone does not prove exact completion. [Provenance][provenance] · [Explicit Indeterminate Outcome][indeterminate]

## 6. Minimum identity and evidence for reconciliation

| Boundary | Required correspondence and evidence subject |
| --- | --- |
| Originating work and handover | Associate the producing Ergon execution and egress occurrence with the exact, durably established Pragma set and its completion evidence. Attribute the handover and any acknowledgement or router decision to that source. Preserve the actual known outcome independently from output construction. |
| Decision and trigger | Identify the originating publication or progression occurrence, decision identity, applicable definition identity/revision or governed decision basis, and complete obligation set. Preserve attributable links to the original execution/egress and its Pragma list where applicable. Evidence must support recovery of the same decision after a lost create response. |
| Obligation and work | Identify the obligation within its decision, its subscription/subscriber or progression rule correspondence, its intended destination and the governed work with applicable revision and lineage. The subscription path needs recoverable association to the existing Pragmatosis after a lost construction reply. The Praxis path preserves the selected existing originating Pragma identity independently of how many obligations reference it. |
| Handoff | Attribute a parcel and send attempt to the obligation, work identity, intended destination and acknowledgement boundary. Retain enough evidence of actual routing and durable acceptance under Petasos so a later observation can establish successful injection after a lost record update. Deployment and contract determine whether the necessary evidence is available. |
| Subsequent effects | Keep delivery acknowledgement, Ergon admission/execution and outcome, Mneme's governed save and authoritative information mutation separately attributable. Identify who issued each acknowledgement, which fact it attests and its outcome. Evidence of one stage supplies only that stage's conclusion. |

These are semantic minima, not prescribed field names, key formats, retention periods, state encodings, claim/locking mechanisms or queries. Recovery needs sufficient owning evidence to resume, hold or escalate an obligation. Where the result remains indeterminate, withhold any dependent acceptance claim. Exact negative-proof threshold, safe re-injection conditions, and authoritative evidence after delivery or retention expiry require contract review. [Accepted Work Recorded Before Ack][recorded] · [Petasos–Mneme evidence and recovery][handoff] · [G03 PF-07/PF-08][prior]

The authoritative routing record and constructed work use the existing Mneme/Mnemosyne contracts. Artemis acceptance uses a separate Petasos transaction boundary. A broker-local transaction does not persist or update the routing record, authoritative work or Provenance. Preserve the accepted SummaryWriter limits, authoritative version rules and multi-site durable mutation Success. The routing records' representation and mapping to applicable existing write/read contracts require adjudication. The proposed Mnemosyne DurableInformation/DurableData decomposition is a separate pending candidate. [Accepted persistence contracts][persistence] · [Pending Mnemosyne candidate][durable-data]

## 7. Architecture correspondence and open decisions

- **Subscription allocation and handoff.** G03 assigns Petasos matching and pre-handoff construction requests through Mneme. The qualified Petasos and Parcel descriptions assign semantic selection to Mneme and describe a parcel referencing originating work followed by consumer-side undertaking creation. Preserve the by-value mandate and other parcel purposes. Reconcile the allocation, changed reference/ordering and canonical Pragma/Pragmatosis correspondence before incorporating this candidate. [Petasos][petasos] · [PetasosParcel Topic][parcel] · [G03 adjudications][prior]
- **Ergon Egress and Praxis continuation.** C29 and the Ergon model support output-derived work and a bounded endpoint. Revision 002 locates the guarded construction request and handover in Ergon Egress. Map that request through the existing execution-side PragmaFactory collaboration, whose detailed caller contract remains open. Qualified U18 Ponos/Praxis/Pragma descriptions assign output processing and routing to Ponos/Ergo and defer continuation representation, including whether a resulting output Pragma is selected. This candidate uses Ponos evaluation to select existing output Pragmas and Petasos to route them. Reconcile the contributor wording and the new service/trigger/result and filter contract for this bounded path. The work-realisation/parcel referent and any receiving role for direct Praxis routing remain open. Preserve Energeia management and the TaskOutcome Topic's qualified parent-resolution meaning pending G03 mapping. [Ergon endpoint][ergon-endpoint] · [PragmaFactory][factory] · [Ponos][ponos] · [Praxis][praxis] · [Pragma Topic][pragma-topic] · [Existing TaskOutcome Topic][task-outcome]
- **Provenance, acknowledgement and retry.** The existing Provenance model supports execution and Pragma references and governed durable availability. It establishes no egress-set completion model or new query. PF-07 retains the proof and authority of complete egress output, trigger/ACK custody, per-router reconciliation after partial or uncertain handover, cross-boundary evidence and audit questions. PF-08 retains exact identity/equivalence across repeated executions, renewed publication and deliberate replay, concurrency and permitted re-injection, and execution eligibility and scheduling. The earlier register remains authoritative for the full open-contract treatment. [Provenance][provenance] · [Accepted Provenance Management][prov-review] · [G03 adjudications][prior] · [DR-12][hcrl]
- **Model and contract.** Adjudicate whether existing work/handoff information and feature contracts can express the two records and their lifecycle, including source/definition revision, correlation, current Themis binding, eligibility after disruption, retention and governing persistence. Keep protected access and fail-closed capture. Approval of this candidate's semantics would precede any governed Continuum mapping or software assignment. [PragmaFactory][factory] · [G01 design][g01] · [Architecture Review Praxis][review-praxis]

## 8. Conformance expectations

Verify Ergon Egress blocks dead tasks, applies its outbound Themis PEP, requests originating Pragma construction and establishes authoritative persistence before handover. Verify a complete authoritative list can be recovered for a specific invocation across repeated executions, including an empty set, partial construction, indeterminate persistence, a lost handover reply, and zero, one or both routers having accepted the handover. For each router, verify durable decision capture before fulfilment and recovery without rerunning matching or Ponos evaluation. Verify same-subscriber multiple subscriptions, unchanged versions across definition changes, and one existing originating Pragma selected for several distinct Praxis obligations without new construction. Exercise timeouts and crash gaps at decision creation, work construction/persistence, broker commit and record update, including lost replies, post-consumption queue absence, duplicate detection expiry, partial and concurrent recovery, independently authorised replay and `DoNotBefore`. Demonstrate that resolved sibling evidence is retained, indeterminate outcomes remain explicit, and retry or redelivery cannot create a second work identity or authorise execution. Verify Themis denial/unavailability, Kleio mandatory capture, lineage, and successful acknowledgement at each owning boundary using the actual selected durability configuration. These are requirements for later design and implementation evidence.

**Review disposition** · Candidate pending Solution Architect review in DR-12. The authorised functional allocation is the design input. Canonical correspondence, the detailed contracts in §7 and runtime verification require their own review. [DR-12][hcrl]

[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-routing-draft-000-002-task.md
[prior-task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-routing-and-reconciliation-task.md
[predecessor]: DRP-Petasos-Routing-and-Reconciliation.draft.000.001.md
[ergon]: ../../continuum/application/application-component/49c9eb2bef0c47b8b5328621d5fc5479.md#architectural-content
[ergon-endpoint]: ../../continuum/application/application-interface/800789f54ba24f53a354600d5cd43f6f.md#architectural-content
[provenance]: ../../continuum/application/data-object/e282811b51274649816566aac7edeb8a.md#architectural-content
[prov-review]: ../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-18
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
