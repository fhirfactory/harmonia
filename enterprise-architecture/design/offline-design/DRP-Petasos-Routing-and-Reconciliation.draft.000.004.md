<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal — Petasos Routing and Reconciliation

**Status** · Draft.000.004 · Pending Solution Architect review  
**Basis** · Architecture 1.1.26 · Software 0.1.0 · G03 / Iteration 005 / DR-12  
**Solution Architect** · Mark Hunter. [PF-07 adjudications][pf07-task] · [Predecessor Draft.000.003][previous-draft] · [Accepted Processing Framework][framework] · [Revision 003 instruction][revision-task] · [Reviewed Draft.000.002][predecessor] · [Cumulative HCRL.004 / DR-12][hcrl] · [G03 adjudications][prior]

## 1. Common strategy

At H11 Petasos durably accepts routing responsibility for the complete originating Pragma list from Ergon Egress. The applicable routers use that accepted input and establish their own downstream decisions and obligations. The PF-07 boundary in §§2.1–2.2 precedes and remains distinct from router decision capture and fulfilment. `Petasos.Subscription-Router` fulfils subscription matches determined by Petasos, requesting a Pragmatosis for each matching subscription. `Petasos.Praxis-Router` routes the existing Pragmas selected by Ponos for the applicable Praxis progression. Both use governed persistence and the existing Petasos durable handoff and recovery capabilities. The common strategy is a contract for staged outcomes and reconciliation. It does not require a shared routing-event superclass, a new workflow or routing engine, or another HA/DR framework. The router and record names are candidate design names. [Petasos][petasos] · [PragmaFactory][factory] · [Work recorded before acknowledgement][recorded] · [Recovery preserves lineage][lineage]

1. Recover the existing durable decision for the applicable trigger if one has been established. Establish a decision through its owning mechanism only when the applicable contract establishes that a new decision is required. Preserve the triggering occurrence, decision basis and complete obligation set, including an empty set where applicable.
2. Establish the decision and complete set durably through Mneme/Mnemosyne **before attempting any individual obligation**. A transient or partially established record is insufficient. If creation of the decision record is indeterminate, reconcile against the authoritative record or trigger correspondence before proceeding.
3. Coordinate the RoutingSetList through Mneme and reconcile before routing when required. Exclude confirmed completions from the working list. For each eligible outstanding obligation, resolve the applicable governed work and lineage through Mneme/Mnemosyne. Subscription-Router requests or recovers the subscriber-specific Pragmatosis and establishes its authoritative persistence before injection. Praxis-Router resolves and routes the existing originating Pragma and preserves its identity.
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

### 1.1 Pre-routing reconciliation

Both routers build or recover the working list from the durable decision and complete obligation set. Use the established decision on repeat handover or recovery. When reconciliation is required, determine the status of each affected obligation using evidence from the owning boundaries. Confirmed routing completion excludes that obligation from the working list and preserves its authoritative history. Incomplete obligations are eligible only under applicable ownership, authorisation and recovery rules. Indeterminate obligations remain held until sufficient authoritative evidence resolves the outcome or the applicable contract establishes a safe further action. An indeterminate queue-injection result never triggers automatic reinjection. [Accepted Work Recorded Before Ack][recorded] · [AX-15][ax15] · [Revision 003 instruction][revision-task]

### 1.2 Startup and successful routing activity

```text
moduleStartupInstant() : Instant
lastSuccessfulRoutingActivity() : Instant
```

`moduleStartupInstant()` identifies the startup of the applicable Petasos module instance. `lastSuccessfulRoutingActivity()` identifies the latest confirmed successful routing activity in the applicable routing engine's operational context. Advance it only when the entire activity in its applicable scope is confirmed successful. A failed, partial or indeterminate activity retains its own outcome. Per-obligation success can be recorded for completed siblings without marking the overall activity successful.

An engine with no previous successful activity has **no value** for last success. Preserve the `Instant` type when a value exists. There is no historical placeholder. Where either value is absent, lost, incomparable or otherwise untrustworthy, the timestamp comparison supplies no continuity evidence. Establish the routing-set state using the authoritative basis when needed, including on first use if prior effects may exist. A newly established decision with all obligations authoritatively known to be new does not require an unrelated historical scan.

```java
if (lastSuccessfulRoutingActivity()
        .isBefore(moduleStartupInstant())) {

    reconcileRoutingList();
}
```

The comparison illustrates the case with both values present and comparable. An older last success signals possible interrupted continuity and triggers reconciliation. A timestamp at or after startup supplies no evidence of durable injection or completeness. Use the applicable engine and module-instance context, and qualify any shortcut against the **current RoutingSetList** and its established obligation state. Success in one set cannot suppress reconciliation required in another. Storage, representation of absence and exact scope bindings remain design matters.

Reconcile on indeterminate operations, loss of ownership or relevant continuity, and detected conflicts or uncertainty even without a restart. The normal path may reuse established, current obligation state under valid ownership and trusted continuity. It avoids an expensive historical reconciliation for every activity while still excluding confirmed completions. [AX-09][ax09] · [AX-15][ax15] · [Revision 003 instruction][revision-task]

### 1.3 PetasosRoutingSharedStatus and RoutingSetList concurrency

**Option A applies.** `PetasosRoutingSharedStatus` is a logical, ephemeral shared operational-state concept for routing coordination. It expresses ownership and consistency information without choosing a concurrency mechanism. Petasos owns its meaning and use, determines obligation and completion semantics, and requests coordination through Mneme. Mneme manages shared operational state and provides the applicable ownership, concurrency and consistency controls across the relevant Petasos instances. Mnemosyne retains authoritative persistence for the durable decisions and governed work. Mneme does not interpret subscription, Praxis or routing rules. [Mneme][mneme] · [AX-05][ax05] · [AX-09][ax09]

After state loss or restart, reconstruct the necessary operational view from `SubscriptionIdEvent`, `PraxisRoutingEvent` (provisional name), applicable authoritative Pragma-family information, Provenance and owning evidence. Establish current ownership through Mneme before acting. Recovery preserves all durable obligations and previously established outcomes. An operational view cannot create or override a durable decision, confer completion authority or establish Artemis acceptance.

The coordination unit is the applicable **RoutingSetList**. Conflicting reconciliation and fulfilment for the same set are mutually exclusive across applicable Petasos instances. Independent sets can proceed concurrently. The identity design must resolve the same contested routing set consistently, including before decision creation where necessary. Two attempts must not obtain independent ownership by assigning separate identities to the same obligation set. Distinct subscription and Praxis obligations retain their separate identities even where they reference an originating Pragma in common.

**Ownership-validity invariant.** After a legitimate takeover, an expired, failed or superseded owner cannot initiate or continue conflicting fulfilment, including queue injection and completion updates, or release the successor's ownership. The effective side-effect boundaries must prevent or reject actions by a stale owner. A check performed only when acquiring ownership is insufficient. An operation already handed off before ownership loss may still have an indeterminate effect. Reconcile it as the original attempt before enabling a conflicting action. If ownership validity or the safety of a further effect cannot be established, hold the affected obligation. Ephemeral status alone supplies no durable fencing guarantee. Exact acquisition, release, renewal, ownership evidence, fencing and post-failure recovery remain for solution design. [Accepted Mneme concurrency allocation][mneme-123] · [Two-cache qualifications][mneme-124] · [Concurrency Policy Conformance][concurrency-policy]

### 1.4 Combined operation

The illustration uses the issued names. Its routing set can be a provisional view until the applicable decision and current state are established under the ownership and persistence contracts.

```java
RoutingSetList routingSet = buildOrRecoverRoutingSet();
PetasosRoutingSharedStatus sharedStatus =
    mneme.acquireRoutingSharedStatus(routingSet.id());

try {
    // Establish or validate the durable decision and refresh its state under current ownership.
    if (requiresReconciliation(
            sharedStatus,
            moduleStartupInstant(),
            lastSuccessfulRoutingActivity())) {
        routingSet = reconcileAndRemoveConfirmedRouted(routingSet);
    }

    performRouting(routingSet);

    if (routingActivitySuccessfullyCompleted()) {
        recordLastSuccessfulRoutingActivity();
    }
} finally {
    mneme.releaseRoutingSharedStatus(sharedStatus);
}
```

Enter the protected sequence only if applicable ownership is established. Discovery may precede ownership. Any decision create before acquisition needs its own governed protection against competing creation for the same trigger. After acquisition, establish, refresh or validate the owning decision, its revision and relevant obligation evidence before using the working view. The skip path applies only where that validated view already establishes the outstanding eligible obligations. Valid ownership must protect each reconciliation and fulfilment effect, including during ownership loss or takeover. The exact placement of decision creation, refresh, revalidation and effect enforcement remains open. The pseudocode defines neither canonical interfaces nor a lock, lease, separate coordination platform or cross-system transaction. [Revision 003 instruction][revision-task] · [Accepted persistence contracts][persistence] · [AX-15][ax15]

## 2. Component responsibilities

| Responsibility | Owner | Worker | Involved |
| --- | --- | --- | --- |
| Subscription evaluation and matching | Petasos | Petasos | Mneme |
| Praxis evaluation service and progression rules | Ponos | Ponos | Petasos |
| Praxis routing orchestration | Petasos | Petasos | Ponos |
| H10 expected set, creation and completeness | Ergon Egress | Ergon Egress requests governed construction from Mneme PragmaFactory. Mneme coordinates persistence and Mnemosyne establishes authoritative durability. | Petasos receives the complete established list at H11. |
| H11 durable acceptance of complete-list routing responsibility | Petasos | Petasos applies the existing governed handover and durability contracts. | Ergon Egress supplies the established list and receives the applicable acknowledgement. |
| Subscriber-specific Pragmatosis construction | Petasos requests construction | Mneme PragmaFactory | — |
| Queue routing, injection, management and delivery | Petasos | Petasos | — |
| Reconciliation of routing obligations | Petasos | Petasos | Mneme |
| Routing-set coordination | Petasos owns routing-state meaning and use | Mneme manages shared state and cross-instance ownership | Petasos requests coordination |

Energeia retains management of Praxis, Pathway and Journey. Ponos owns execution and interpretation of the applicable Praxis for progression, including the evaluation method/API service invoked by Petasos. Praxis provides the rules and operational envelope. Petasos orchestrates fulfilment of the resulting routing obligations. It requests subscriber-specific construction on the subscription path and resolves existing work on the Praxis path. It leaves Praxis interpretation, Ergon execution eligibility, and execution retry/scheduling at their existing boundaries. [Ponos][ponos] · [Praxis][praxis] · [Scheduling authority][scheduler]

Mneme owns the PragmaFactory construction operation and applies the required model invariants. It provides governed information access and manages the routing records, originating Pragmas and any subscriber-specific undertakings through Mnemosyne, which establishes authoritative durability. Mneme neither interprets SubscriptionMask or Praxis nor makes queue-routing decisions. Themis provides applicable PEP/PDP capabilities and governs each protected boundary, including Ponos evaluation invocation, Mneme reads/writes and construction, Petasos FORWARD and Ergon admissions. The governing policy and trusted context remain distinct from functional ownership and correlation. [PragmaFactory][factory] · [G01 design][g01] · [AX-07][ax07] · [Accepted persistence contracts][persistence]

Ergon Egress, Petasos, Ponos and Mneme supply semantically meaningful auditable occurrences for their respective operations as applicable policy requires. Kleio owns Audit Capture and construction of authoritative FHIR AuditEvents and uses its governed capture authority. If mandatory durable capture cannot be established, the affected operation fails closed. Routing records, queue metrics and observations serve their own purposes. The pending Petasos–Kleio staging proposal supplies no authority to change the capture gate. [Kleio][kleio] · [Pending audit candidate][audit-draft]

### 2.1 H10 — Originating Pragma Creation

The accepted Processing Framework places Originating Pragma Creation at `ergon.egress.pragma-create` and Petasos Route-In at `petasos.route-in`. The functional sequence and Checkpoints carry forward. The Egress Pragma carries the execution context and retains its own identity. Ergon Egress uses the eligible and authorised output IU identities to request distinct originating Pragmas through Mneme's PragmaFactory. Dead-task blocking, PublishingMask evaluation and outbound Themis enforcement occur at their accepted boundaries. [Processing Framework Final.001 §4.2][framework] · [PragmaFactory][factory] · [PF-07 adjudications][pf07-task]

The existing originating Pragma list has the following **conceptual information contract**. `ErgonCreatedPragmaList`, `ErgonCreatedPragmaToken` and the field labels express the issued semantics. Java representation, package placement, serialisation and framework bindings remain detailed design matters.

```java
public class ErgonCreatedPragmaList {

    public class ErgonCreatedPragmaToken {
        String pragmaId;
        Status status;
    }

    List<ErgonCreatedPragmaToken> createdList;
    Integer expectedListSize;
}
```

- `expectedListSize` is the number of originating Pragmas expected for this Ergon processing instance and Egress occurrence. Ergon Egress owns determination of the expected set and its completeness.
- `createdList` contains the individual tokens from the governed PragmaFactory operation. Each `pragmaId` identifies an originating Pragma and its `status` records its creation and persistence outcome. Mneme owns construction and persistence coordination. Mnemosyne establishes the required authoritative durability.
- H10 completes successfully when both checks hold. The number of entries corresponds to `expectedListSize`, and every required member of the expected set is accounted for with confirmed successful authoritative establishment. Equality of counts supplies only the cardinality check. A duplicate or unexpected entry cannot account for a missing required member. Creation, persistence and completeness evidence must be attributable to the producing Ergon processing instance and Egress occurrence.
- H10 establishes creation and persistence outcomes. Keep those outcomes distinct from Petasos routing acceptance, later queue or execution results, the Ergon-instance TaskOutcome and any undertaking or originating-work disposition. Failed or indeterminate creation and incomplete sets stay visible under §5.

The token's status describes the H10 operation within the existing handover contract. The established Pragma, Ergon execution and routing status domains retain their own meanings. Encoding and evidence of complete membership remain for design. [Pragma work and outcomes][outcomes] · [Accepted persistence contracts][persistence] · [PF-07 adjudications][pf07-task]

### 2.2 H11 — Petasos Route-In and complete-list acceptance

Ergon Egress hands over the complete `ErgonCreatedPragmaList` established at H10. **Petasos accepts the complete list on an all-or-nothing basis and assumes routing responsibility for the entire accepted set.** A successful H11 requires durable establishment of that responsibility for the complete list. An incomplete or indeterminate set supplies no basis for confirmed successful acceptance. [Processing Framework Final.001 §4.2][framework] · [PF-07 adjudications][pf07-task] · [Work recorded before acknowledgement][recorded]

The following acknowledgement is conceptual. At this invocation boundary, `createdList` denotes the complete `ErgonCreatedPragmaList`. The signature, acknowledgement encoding, value set and field spellings remain detailed design matters.

```java
PragmaListHandoffStatus status =
    Petasos.RoutingEngine.Route(createdList);
```

A successful acknowledgement conceptually means:

```text
received = GOOD
commited = TRUE
```

`received = GOOD` expresses receipt at Petasos. `commited = TRUE` expresses established durable acceptance of complete-list routing responsibility. Subscriber matching, Ponos Praxis evaluation, router decisions, queue acceptance, delivery and execution are separate outcomes. Petasos can have accepted the list while neither, one or both applicable routers have established their downstream decisions. Recovery reconciles that downstream progress independently of the list-level acceptance. [PF-07 adjudications][pf07-task] · [Petasos–Mneme durability and evidence][handoff-ack]

Petasos may accept an originating Pragma and subsequently determine that it has no subscribers. The proven no-subscriber disposition is valid and preserves the earlier complete-list acceptance. Petasos owns matching and routing. For each applicable subscription, Petasos requests the subscriber-specific Pragmatosis through Mneme's PragmaFactory. A zero-match disposition yields zero subscriber-specific undertakings and no execution outcome on that subscription path.

| Originating Pragma | Subscriber matches | Disposition |
| --- | ---: | --- |
| Pragma A | 2 | Route to two subscribers |
| Pragma B | 0 | Valid no-subscriber disposition |
| Pragma C | 1 | Route to one subscriber |

The list acceptance and the later per-router work and queue outcomes each require evidence at their owning boundary. §5 retains confirmed effects, failures and uncertainty independently, without changing PF-08 reconciliation or existing Themis and Kleio responsibilities. [G03 publication and subscription][publication] · [PF-07 adjudications][pf07-task]

## 3. Petasos.Subscription-Router

Subscription-Router uses the applicable already-created originating Pragmas from the complete list accepted by Petasos at H11. Petasos evaluates applicable subscriptions against an eligible published IU and originating Pragma using its subscription view. Mneme provides governed information. R1/R2 matching uses Pragma and IU summary/metadata. R3+ may additionally evaluate authorised content under the owning access/disclosure contract. Publication eligibility, matching and Themis authorisation are separate. Preserve same-context information and conveyance criteria under the conceptual Parcel/ParcelMask design. Pre-parcel criteria and unavailable-input treatment remain under PF-06 and related DR-10 obligations. `SubscriptionCache` describes the illustrative input view without prescribing another cache allocation. [Publication and subscription §§4.1–4.2][publication] · [Conceptual Parcel/ParcelMask][parcel-mask]

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

Each matching subscription has its own obligation even when two subscriptions identify the same subscriber. Within this G03 subscription-work pattern the parcel carries the established Pragmatosis identity and permitted handoff metadata. The IU reference and payload remain with governed access, and the receiving Ergon resolves the undertaking through Mneme. An empty resolved SubscriberSet establishes a valid no-subscriber disposition after acceptance. The list acceptance remains in force. The subscription disposition creates no undertaking or Ergon TaskOutcome. Recovery uses the existing SubscriptionIdEvent and its obligations without evaluating the current SubscriptionCache again. [Pragma work and outcomes][pragma] · [Petasos–Mneme handoff][handoff] · [Qualified prior Parcel account][parcel]

## 4. Petasos.Praxis-Router

**Ponos owns the Praxis evaluation method/API service. Petasos.Praxis-Router invokes it with the applicable Praxis context and the list of already-created originating Pragmas.** For an Ergon Egress handover, those Pragmas come from the complete list accepted at H11. Ponos evaluates progression rules, including any applicable selection criteria or filters, to select existing Pragma identities for identified subsequent Ergons. Petasos fulfils those obligations without interpreting the Praxis. The filter model, applicable inputs and exact evaluation semantics remain open. A processing outcome or progression occurrence may trigger a decision. Actual Ergon TaskOutcome, undertaking and parent disposition, output mediation and Assurance remain independently attributable. [Praxis][praxis] · [Ponos][ponos] · [Pragma outcomes][outcomes] · [Existing TaskOutcome Topic][task-outcome]

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
| H10 failed, indeterminate or incomplete creation | Preserve each available creation and persistence fact and existing authoritative work. Matching list size alone cannot establish completeness. Reconcile the exact expected set and its attributable outcomes before advancing to successful handover. |
| H11 failed or indeterminate acceptance | Preserve the originating work established at H10. Keep confirmed failure and unknown effects distinct. A missing acknowledgement leaves the outcome unresolved. Reconcile the existing acceptance evidence before a permitted repeat of the same handover. |
| Durable H11 acceptance followed by downstream failure | Preserve acceptance for the complete list. Reconcile the specific routing or delivery effects without recreating originating work. A confirmed no-subscriber disposition remains valid. |
| Before durable decision creation | If no durable decision was established, recover the original trigger through its owning handoff or progression contract. For an Ergon Egress trigger use §5.1 to reconcile H11 acceptance and each applicable router's decision separately. Trigger custody, acknowledgement timing and any uncaptured evaluation result remain open. If decision creation is uncertain, first establish whether the decision already exists. |
| After confirmed durable record creation | Resume from the recorded decisions and full obligation set. A cache view or a fresh Ponos evaluation supplies no replacement. |
| During work establishment or resolution | Preserve indeterminate creation or durable establishment at Ergon Egress or during subscriber-specific construction. Reconcile through governed identity, egress provenance or subscription-obligation correspondence as applicable. Praxis-Router resolves an existing identity and withholds injection if that resolution or its authority cannot be established. |
| After successful work establishment, before queue injection | Recover the existing identity and the outstanding injection obligation. If the crash position is uncertain, reconcile whether the send occurred before repeating it. |
| During Artemis injection | Use the applicable configured broker durability and routing guarantees and Petasos evidence to distinguish confirmed acceptance, confirmed failure and indeterminate commit. Receipt, accepted durable enqueue, delivery and execution are separate. Preserve uncertainty after a lost reply. |
| After successful injection, before routing-record update | Confirm the original acceptance with evidence attributable to the obligation/attempt. Record that outcome without resending. A NULL or missing update, or a queue lookup with no message, does not prove the earlier send failed. |
| During routing-record update | Recover the authoritative record and compare its revision, obligation and evidence. Reconcile the update and its underlying effects separately. Preserve known facts and prevent stale recovery from replacing evidenced progress. |
| Partial fan-out or competing recovery | Use Mneme-managed RoutingSetList ownership to prevent competing reconciliation or fulfilment, then refresh or validate relevant authoritative state. Preserve completed siblings, stable identities and independently attributable attempts. Known success is left out of the working list without deleting history. |
| Restart or loss of operational continuity | Treat the timestamp comparison as a signal. Recover from durable decisions and owning evidence and re-establish valid ownership before acting. A missing shared-status entry or a successful activity in a different set provides no clearance. |
| Owner failure, expiry or takeover | Prevent effects by a stale owner, including stale completion updates or release. Reconcile any in-flight injection that may already have taken effect before a conflicting action. Hold when ownership or a safe further action cannot be established. |
| Subscription or Praxis definition changes | Preserve the recorded decision and the definition/version basis for this obligation set. Authorisation is checked per protected operation at its applicable boundary. An authorised new decision records its relationship to the original with its own basis. |
| Duplicate delivery | Recognise a repeat delivery of the same obligation and preserve identity. Ponos and the execution boundary determine execution eligibility from governed state, including any applicable `DoNotBefore`, authority and prior execution or recovery conditions. Transport redelivery creates no execution authority. |
| Deliberate replay | Use an explicitly designated replayable boundary. Establish the identity of the new processing attempt and link it to the original through correlation and provenance. The replay contract must state whether it reuses or creates a routing decision, obligations or work, and whether it includes an authorised new evaluation. Preserve the original decision and effects. |

Artemis 2.33.0 supports broker-local commit acknowledgement under the applicable persistence settings and finite, per-address duplicate detection, which can affect an entire transaction. Detection depends on configuration, history, persistence and failover. Queue absence is inconclusive because a parcel might already have been delivered, acknowledged or moved. Broker detection can support a permitted retry using the same send identity within its proved scope. It supplies neither permanent exactly-once work execution nor the missing authoritative work/acknowledgement evidence. The configured broker, store and HA behaviour require verification. [Commit guarantees][artemis-commit] · [Duplicate detection][artemis-duplicates] · [Redelivery][artemis-redelivery] · [Artemis HA][artemis-ha]

### 5.1 Ergon Egress handover recovery from authoritative provenance

Reconstruct the original `ErgonCreatedPragmaList` through governed authoritative Provenance when it establishes the exact output set and its completion basis for the particular Ergon execution and Egress occurrence. Recover the expected size, exact membership and per-member establishment results from the applicable evidence. This preserves the earlier recovery approach using existing information rather than a separate durable handover record solely to reconstruct the list. Dokimasia retains Provenance Management. Originating activity generates Provenance through the existing client responsibility, Mneme provides governed information access, and Mnemosyne establishes authoritative durable persistence. The governed lookup and detailed completeness evidence remain open under the accepted R1/R2 Summary-only search boundary. [Provenance][provenance] · [Accepted Provenance Management][prov-review] · [Persistence contracts][persistence] · [Revision 002 instruction][task] · [PF-07 adjudications][pf07-task]

Reconcile H11 durable acceptance and each applicable router's downstream decision separately. Authoritative provenance for the H10 list establishes the creation and completeness facts that it proves. Evidence owned by the Petasos handover boundary must establish the durable acceptance claimed at H11. Preserve confirmed acceptance and each established router decision and its effects, including when only one router has reached a downstream decision. Use recorded results for existing decisions. Apply current policy and preserve original work identities and lineage in any authorised repeat or reconciliation. A missing or inconclusive lookup leaves the affected outcome unresolved.

| Egress or handover evidence | Recovery treatment |
| --- | --- |
| Complete authoritative originating Pragma set | Recover exact membership, expected size, authoritative establishment of every required member, applicable versions and lineage for that Egress occurrence. Include a proved empty expected set where applicable. Recover order only where the governing contract makes it meaningful. |
| Partially established output set | Preserve confirmed members and their authoritative persistence and lineage. Reconcile the producing occurrence and outstanding construction through their owning contracts before using the list as complete. |
| Indeterminate output construction or persistence | Preserve uncertainty and recover evidence under the owning output and persistence contracts. A lost reply, stale cache or empty lookup leaves the effect unproved and supplies no authority to duplicate construction. |
| Complete set with lost or uncertain H11 acknowledgement | Reconcile whether Petasos durably accepted responsibility for the complete list. Retain authoritative originating work and any confirmed routing effects. A permitted repeat uses the original identity basis and members. A missing acknowledgement leaves acceptance unresolved. |
| Durable H11 acceptance with routing pending, failed or indeterminate | Preserve complete-list acceptance. Recover each existing decision and obligation, and separately reconcile unfinished or uncertain downstream routing or delivery. The re-evaluation and replay rules in §§1–1.4 continue to apply. |
| Confirmed no-subscriber disposition | Retain durable acceptance and the recorded empty SubscriberSet as separate successful facts. The subscription path creates no Pragmatosis or Ergon execution result for that Pragma. |

Minimum proof identifies the **particular execution occurrence**, the **Egress occurrence**, and their attributable relationship to the complete set of resulting Pragma identities. Distinguish legitimate re-execution of the same Ergon or input from recovery of one execution. Individual Provenance references can establish membership facts. The contracts still need to define proof of expected and exact membership, per-member authoritative establishment and closure, including applicable versions, lineage, retention, ordering, unavailable evidence and recovery after a lost acknowledgement. Preserve the distinction between a confirmed empty set, partial construction and indeterminate construction. [Provenance][provenance] · [Explicit Indeterminate Outcome][indeterminate]

## 6. Minimum identity and evidence for reconciliation

| Boundary | Required correspondence and evidence subject |
| --- | --- |
| H10 originating work | Associate the expected set, expected size, exact membership and per-Pragma creation and authoritative persistence outcomes with the producing Ergon processing instance and Egress occurrence. Preserve the completion basis independently of handover evidence. |
| H11 complete-list acceptance | Identify the exact established list and the receipt and durable routing-responsibility outcomes. Attribute any acknowledgement and supporting durability evidence to this handover and its issuing boundary. Keep list acceptance separate from each router decision, broker acceptance, delivery and execution. |
| Decision and trigger | Identify the originating publication or progression occurrence, decision identity, applicable definition identity/revision or governed decision basis, and complete obligation set. Preserve attributable links to the original execution/egress and its Pragma list where applicable. Evidence must support recovery of the same decision after a lost create response. |
| Routing-set coordination | Correlate the scope of operational ownership with the current durable decision or original trigger as applicable. Establish valid ownership across the affected instances and the applicability of the working view before use. Operational status and timestamps supply no proof of work persistence, per-obligation completion or broker acceptance. |
| Obligation and work | Identify the obligation within its decision, its subscription/subscriber or progression rule correspondence, its intended destination and the governed work with applicable revision and lineage. The subscription path needs recoverable association to the existing Pragmatosis after a lost construction reply. The Praxis path preserves the selected existing originating Pragma identity independently of how many obligations reference it. |
| Per-obligation queue handoff | Attribute a parcel and send attempt to the obligation, work identity, intended destination and acknowledgement boundary. Retain enough evidence of actual routing and durable acceptance under Petasos so a later observation can establish successful injection after a lost record update. Deployment and contract determine whether the necessary evidence is available. |
| Subsequent effects | Keep delivery acknowledgement, Ergon admission/execution and outcome, Mneme's governed save and authoritative information mutation separately attributable. Identify who issued each acknowledgement, which fact it attests and its outcome. Evidence of one stage supplies only that stage's conclusion. |

These are semantic minima, not prescribed field names, key formats, retention periods, state encodings, claim/locking mechanisms or queries. Recovery needs sufficient owning evidence to resume, hold or escalate an obligation. Where the result remains indeterminate, withhold any dependent acceptance claim. Exact negative-proof threshold, safe re-injection conditions, and authoritative evidence after delivery or retention expiry require contract review. [Accepted Work Recorded Before Ack][recorded] · [Petasos–Mneme evidence and recovery][handoff] · [G03 PF-07/PF-08][prior]

The authoritative routing record and constructed work use the existing Mneme/Mnemosyne contracts. Artemis acceptance uses a separate Petasos transaction boundary. A broker-local transaction does not persist or update the routing record, authoritative work or Provenance. Preserve the accepted SummaryWriter limits, authoritative version rules and multi-site durable mutation Success. The routing records' representation and mapping to applicable existing write/read contracts require adjudication. The proposed Mnemosyne DurableInformation/DurableData decomposition is a separate pending candidate. [Accepted persistence contracts][persistence] · [Pending Mnemosyne candidate][durable-data]

## 7. Architecture correspondence and open decisions

- **Subscription allocation and handoff.** G03 assigns Petasos matching and pre-handoff construction requests through Mneme. The qualified Petasos and Parcel descriptions assign semantic selection to Mneme and describe a parcel referencing originating work followed by consumer-side undertaking creation. Preserve the by-value mandate and other parcel purposes. Reconcile the allocation, changed reference/ordering and canonical Pragma/Pragmatosis correspondence before incorporating this candidate. [Petasos][petasos] · [PetasosParcel Topic][parcel] · [G03 adjudications][prior]
- **Ergon Egress and Praxis continuation.** C29 and the Ergon model support output-derived work and a bounded endpoint. Revision 002 locates the guarded construction request and handover in Ergon Egress. Map that request through the existing execution-side PragmaFactory collaboration, whose detailed caller contract remains open. Qualified U18 Ponos/Praxis/Pragma descriptions assign output processing and routing to Ponos/Ergo and defer continuation representation, including whether a resulting output Pragma is selected. This candidate uses Ponos evaluation to select existing output Pragmas and Petasos to route them. Reconcile the contributor wording and the new service/trigger/result and filter contract for this bounded path. The work-realisation/parcel referent and any receiving role for direct Praxis routing remain open. Preserve Energeia management and the TaskOutcome Topic's qualified parent-resolution meaning pending G03 mapping. [Ergon endpoint][ergon-endpoint] · [PragmaFactory][factory] · [Ponos][ponos] · [Praxis][praxis] · [Pragma Topic][pragma-topic] · [Existing TaskOutcome Topic][task-outcome]
- **Provenance and acknowledgement.** The existing Provenance model supports execution and Pragma references and governed durable availability. §§2.1–2.2 resolve PF-07 functional completeness and acceptance. The contracts must still establish exact-set completion and its evidence, trigger and acknowledgement custody, per-router reconciliation following downstream interruption, and cross-boundary evidence and audit integration. The conceptual list specifies required handover information under existing boundaries. [Provenance][provenance] · [Accepted Provenance Management][prov-review] · [G03 adjudications][prior] · [PF-07 adjudications][pf07-task] · [DR-12][hcrl]
- **Mneme coordination correspondence.** AX-05/09 and the accepted active-state/operational-coordination responsibilities support this allocation. The accepted detailed IU concurrency contract covers individual IUs. RoutingSetList adds a distinct coordination scope and needs explicit mapping and proof across the applicable Petasos instances. Neither the accepted Standard-Cache direction nor peer IU OperationalMetadata establishes the model, its placement, cross-site consistency or fencing. The execution-local Workflow Cache cannot supply this cross-instance guarantee. Preserve MC-01–MC-07, the two-cache allocation and governed Mneme access. Exact use of existing capabilities and canonical representation require review. [Mneme][mneme] · [Accepted IU concurrency][mneme-123] · [Two-cache qualifications][mneme-124] · [Peer OperationalMetadata][operational-metadata]
- **Model and contract.** Adjudicate whether existing work/handoff information and feature contracts can express the two records and their lifecycle, including source/definition revision, correlation, current Themis binding, eligibility after disruption, retention and governing persistence. Keep protected access and fail-closed capture. Approval of this candidate's semantics would precede any governed Continuum mapping or software assignment. [PragmaFactory][factory] · [G01 design][g01] · [Architecture Review Praxis][review-praxis]

### 7.1 PF-08 qualified disposition

**PF-08 — Architectural strategy resolved; detailed transport, concurrency and recovery contracts outstanding.**

The issued strategy settles durable decision and complete obligation capture, conditional pre-routing reconciliation and restart detection, working-list exclusion of confirmed completions, explicit incomplete and indeterminate outcomes, and Mneme-managed ephemeral `PetasosRoutingSharedStatus` at RoutingSetList scope. It preserves existing work identities and authoritative-provenance reconstruction, with distinct Subscription-Router and Praxis-Router responsibilities. The previous register and revision evidence retain their historical standing. [Revision 003 instruction][revision-task] · [Earlier PF register][prior] · [DR-12][hcrl]

The detailed contracts still require Solution Architect treatment:

- Exact routing-decision, RoutingSetList and obligation identities. Establish correspondence across repeated executions, renewed publication, multiple selections and deliberate replay.
- Authoritative proof of Artemis acceptance after an indeterminate result, safe reinjection and duplicate suppression, including delayed evidence and acknowledgement loss.
- Ownership validity and fencing at effect boundaries, cross-instance recovery after owner failure, acquisition/renewal/release, ownership and authoritative-refresh ordering, and shared-status lifecycle and consistency guarantees.
- Exact startup and last-success scope, initialisation and availability semantics that preserve the safeguards in §1.2.
- Handoff acknowledgement and retry equivalence, execution eligibility after redelivery and the owning timing/scheduling and `DoNotBefore` contracts.
- Effective implementation and failure-injection evidence for the selected persistence, transport and Mneme coordination behaviour.

### 7.2 PF-07 disposition and companion follow-up

**PF-07 — Architectural responsibilities and normal-path behaviour resolved. Detailed contracts and conformance evidence remain outstanding.** The Solution Architect settled the producing Ergon's H10 completeness responsibility and Petasos's all-or-nothing durable H11 acceptance. Valid no-subscriber results belong to subsequent subscription routing. The conceptual terms name the existing H10/H11 interaction and its outcome evidence. Canonical representation, identity and correlation, authoritative proof of exact membership and establishment, durable acceptance and acknowledgement encoding, custody, idempotence, recovery and evidence remain in their owning contracts. [PF-07 adjudications][pf07-task] · [Processing Framework Final.001 §4.2][framework] · [DR-12][hcrl]

The following candidate correspondence belongs in separately authorised companion work. Each retains its recorded standing.

| Candidate | Follow-up |
| --- | --- |
| [Mneme Pragma Model §§4.4–4.5][pragma-progression] | Distinguish H10 originating Pragma establishment and H11 originating-list acceptance from the later subscriber-specific Pragmatosis construction and handoff. Map the expected set, member identity, lineage and creation/persistence outcomes to the existing model. |
| [Petasos–Mneme interaction §§4.3–4.4][handoff-ack] | Reconcile the evidence and acknowledgement scope for durable responsibility for a complete originating list with the separately recorded undertaking/parcel effects. Define authoritative completion and acceptance proof, correlation after missing acknowledgement, and repeat/recovery equivalence under the existing contracts. |
| [Pragma Publication and Subscription §§4.1–4.2][publication-output] | Trace originating-list acceptance before the subsequent subscription dispositions, including valid zero matches. Preserve eligibility, matching and later undertaking delivery as distinct operations. |

## 8. Conformance expectations

Verify Ergon Egress blocks dead tasks, applies its outbound Themis PEP and uses H10 to establish the exact expected set authoritatively before H11. Exercise a valid full set, a proved empty expected set where applicable, equal counts with a missing or duplicate member, failed creation and indeterminate persistence. Verify H11 withholds success for an incomplete list or unproved acceptance and attests durable responsibility only for the entire accepted list. Test a lost acknowledgement before or after the acceptance effect, stable identity on permitted repeat, and independent recovery of zero, one or both downstream router decisions after list acceptance. Verify a mixed accepted list with a valid no-subscriber disposition, and preserve acceptance independently of later routing, delivery and execution failures. For each router, verify durable decision capture before fulfilment and recovery without rerunning matching or Ponos evaluation. Verify same-subscriber multiple subscriptions, unchanged versions across definition changes, and one existing originating Pragma selected for several distinct Praxis obligations without new construction. Exercise timeouts and crash gaps at decision creation, work construction/persistence, broker commit and record update, including lost replies, post-consumption queue absence, duplicate detection expiry, partial and concurrent recovery, independently authorised replay and `DoNotBefore`. Demonstrate that resolved sibling evidence is retained, indeterminate outcomes remain explicit, and retry or redelivery cannot create a second work identity or authorise execution. Verify Themis denial/unavailability, Kleio mandatory capture, lineage, and successful acknowledgement at each owning boundary using the actual selected durability configuration. These are requirements for later design and implementation evidence. Also exercise first use with no success timestamp, a restart with last success before startup, later timestamps following a different routing set, and loss of continuity or ownership without restart. Verify the established-state path can avoid historical reconciliation while excluding confirmed completed obligations. Exercise simultaneous attempts by different Petasos instances on the same set and parallel work on independent sets. Test stale views during acquisition, concurrent decision establishment, owner failure followed by takeover and stale-owner recovery, and prior in-flight Artemis acceptance. Verify no duplicate effects or loss of known outcomes after loss of shared status, and that partial or indeterminate activity cannot advance last success.

**Review disposition** · Candidate pending Solution Architect review in DR-12. The authorised functional allocation and the PF-07 adjudications are the design input. Canonical correspondence, the detailed contracts in §7 and runtime verification require their own review. [DR-12][hcrl]

[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-routing-draft-000-002-task.md
[prior-task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-routing-and-reconciliation-task.md
[pf07-task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/20261123T033042Z-g03-pf-07-ergon-egress-petasos-handover-task.md
[previous-draft]: DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md
[framework]: DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#42-stage-and-checkpoint-correspondence
[pragma-progression]: DRP-Mneme-Pragma-Model.draft.000.004.md#44-progression-and-scheduling
[handoff-ack]: DRP-Interaction-Petasos-to-Mneme.draft.000.004.md#43-durability-and-acknowledgement
[publication-output]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#41-output-and-publication
[predecessor]: DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md
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
[revision-task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-routing-draft-000-003-task.md
[ax05]: ../../continuum/motivation/principle/0412a2084f0145babc4f022c71fea65f.md
[ax09]: ../../continuum/motivation/principle/9a5cb9e8876844e58e660f909f7df9fc.md
[ax15]: ../../continuum/motivation/principle/ef8abbaec37e4890ad78b9f263e67906.md
[mneme]: ../../continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md#architectural-content
[mneme-123]: ../register/1.1.23/hcrl-1.1.23-001.complete.md#dr-1
[mneme-124]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[operational-metadata]: ../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-2
[concurrency-policy]: ../../continuum/motivation/principle/architectural-axiom/7e3b05a2e3e547cbbd26b1e181af0d10.md
