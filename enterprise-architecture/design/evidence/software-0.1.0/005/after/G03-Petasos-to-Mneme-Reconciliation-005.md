<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# G03 Petasos–Mneme interaction reconciliation · Iteration 005

**Origin** · Architecture 1.1.26 · `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`).  
**Review correspondence** · [G03 Task][task] · [Source Draft.002][previous] · [Petasos–Mneme Draft.000.003][candidate] · [Cumulative HCRL.004 / DR-12][hcrl]

## Preserved architectural semantics

| Source boundary | Retained meaning |
| --- | --- |
| Draft.002 §§3.1–3.2 and ADR-018 | Petasos transfers responsibility for recoverable work. Mneme manages the information and Mnemosyne establishes authoritative durable state. Durable transport and authoritative IU commitment retain separate meanings. The general ADR-018 durable work-acceptance possibility remains effective. [ADR-018][adr] · [Durable Work Acceptance][acceptance] · [Persistence interaction][persistence] |
| Draft.002 §3.3 and PetasosParcel | Petasos conveys a separately identifiable parcel by value. Its occurrence, origin/source, destination and operational state have distinct meanings from those of the referenced work or IU. The approved conceptual Parcel/ParcelMask proposal retains its conceptual standing and qualifications. Other parcel purposes retain their own contracts. [PetasosParcel Topic][parcel] · [By-value constraint][by-value] · [Conceptual Parcel/ParcelMask][parcel-final] |
| Draft.002 §§3.4–3.5 and accepted cache/recovery | Preserve causation, correlation, security and provenance across failure or handoff. Recognise duplicate or delayed delivery and retain the distinction from Ponos/Ergon execution retry. Rebuild working context at the established execution boundary from durable work and governed information. Recovery does not resume inside a failed Ergon. [Petasos][petasos] · [Two-cache architecture][caches] · [Ergon–Mneme candidate][ergon] |

## Decisions incorporated in the candidate

- Mneme's subscription and factory responsibilities establish one Pragmatosis or Noesis per authorised matching subscription. Mneme uses Mnemosyne to establish the realisation and its lineage authoritatively before reference handoff. Separate subscriptions of the same subscriber retain separate undertakings. The by-value parcel identifies the existing undertaking, and the receiving side resolves it under Mneme governance. [Publication/subscription][publication] · [Pragma Model][pragma]
- Durable acceptance of the handoff requires the authoritative, resolvable undertaking and necessary lineage, together with Petasos evidence of its recoverable obligation. Evidence of undertaking persistence, durable acceptance, delivery/acknowledgement, execution, Mneme save and authoritative IU persistence remains separately attributable. Record the acknowledgement's issuing boundary, event meaning, time and result against its work and handoff. Concrete evidence representation and detailed ownership are open. [Acknowledgement Feature][ack] · [Delivery Correlation][correlation] · [Persistence interaction][persistence]
- Preserve each matched subscription's undertaking identity and handoff outcome across partial fan-out, indeterminate persistence or transport, repeated handoff, duplicate delivery and execution failure. Retry the same obligation only under its owning conditions with the same undertaking identity. Maintain any applicable `DoNotBefore` execution constraint and independent evidence for delay or failure. Duplicate recognition grants no assumption of Ergon idempotence. [Publication/subscription][publication] · [Pragma Model][pragma] · [Explicit Indeterminate Outcome][indeterminate]
- Establish or reconstruct Pragmaton through the Ergon–Mneme boundary when execution needs it. WorkSpace and the Workflow Cache remain separate access and transient materialisation responsibilities. Their contents and full IU Content stay within governed information access and execution. [Pragmaton model][pragmaton] · [Ergon–Mneme candidate][ergon] · [Two-cache architecture][caches]
- Apply applicable Themis controls to forwarding, resolution and execution. Preserve trusted identity, version-specific IU lineage and subscription/fan-out association through governed evidence. Transport metadata supplies no authority of its own. Correlate independent outcomes for Kleio audit, provenance and Dokimasia Assurance. [PetasosParcel Topic][parcel] · [G01 authorisation][security] · [Assurance disposition][assurance]

## Historical behaviour replaced in this candidate

| Mapped source | Difference requiring Solution Architect disposition |
| --- | --- |
| Draft.002 §§3.3–3.4 and its by-value/by-reference question | Earlier wording allowed the parcel to identify or help reconstruct Pragma and Mneme to establish Pragma on receipt. Here the by-value parcel references the authoritative subscriber realisation. Receipt resolves that existing work identity. Working information can still be reconstructed when execution needs it. |
| Qualified PetasosParcel Topic and Petasos work-state contribution | The prior pattern references the ActionableTask and has the receiving consumer establish FulfillmentTask. The candidate moves establishment to Mneme before transport and changes the parcel referent. Canonical work/outcome and representation correspondence remains open. The prior source retains its qualified standing. [PetasosParcel Topic][parcel] · [Petasos][petasos] · [Pragma model reconciliation][pragma-evidence] |
| Draft.002 §3.2's broad recoverable-work acceptance | This candidate uses the narrower authoritative-realisation precondition for its subscriber-specific handoff. Keep ADR-018 and other established Petasos handoffs within their own scope. |
| Draft.002 §3.5's generic duplicate/redelivery handling | The candidate makes same-obligation identity reuse explicit. It preserves the distinction between transport redelivery, execution retry and deliberate replay. Detailed cross-boundary decisions and revision correspondence still require adjudication. |

## Remaining architectural questions

Confirm the canonical reference and lineage contract, minimum durable work and acknowledgement evidence, and precise responsibility for acknowledging each boundary. Decide how repeat and replay obligations remain equivalent across subscription changes, where retry decisions and `DoNotBefore` evidence are governed, and how partial or indeterminate outcomes are reconciled. Confirm transport trust and metadata/selector correspondence under the earlier DR-7, DR-10 and G03 A06/A07 obligations. Interface, encoding, persistence schema and retry mechanics remain solution design. [DR-12][hcrl] · [Prior publication/subscription reconciliation][pub-evidence]

## Required follow-on reconciliation

- **Continuum.** Review the PetasosParcel Topic, Petasos qualified work-state contribution and the associated Pragma/Pragma Management and work/outcome references. Establish which canonical elements express the agreed referent and establishment responsibility. Retain other Petasos purposes, the routing mandate, transport-by-value and authority boundaries. Apply any semantic changes through governed incorporation after disposition. [PetasosParcel Topic][parcel] · [Petasos][petasos] · [Pragma model reconciliation][pragma-evidence] · [Routing mandate][routing]
- **Pragma-family review.** Align next revisions of publication/subscription and the conceptual Parcel/ParcelMask sources with the exact acknowledgement semantics and work-reference/metadata correspondence once adjudicated. Carry the approved identity, version, scheduling and recovery boundaries into the Ergon–Mneme reference. Its current candidate already separates resolving durable work from rebuilding Pragmaton. [Publication/subscription][publication] · [Conceptual Parcel/ParcelMask][parcel-final] · [Ergon–Mneme reconciliation][ergon-evidence]

## Review checks

Review a fan-out where one subscriber has two subscriptions and an acknowledgement is lost for one of them. Confirm that the proven sibling outcomes survive recovery, the uncertain outcome stays explicit, and repetition resolves the same realisation when safe. Compare redelivery after consumer failure with a governed Ergon retry. Verify that pre-start `DoNotBefore` still defers execution, transient working context can be rebuilt, and the owning persistence and delivery evidence independently support their claims. These are candidate review and future conformance cases. [Candidate][candidate]

[task]: ../before/ai-agent-tasks/unresolved-g03-petasos-to-mneme-reconciliation-task.md
[previous]: ../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md
[candidate]: ../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.000.003.md
[publication]: ../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.002.md
[pragma]: ../../../../offline-design/DRP-Mneme-Pragma-Model.draft.000.003.md
[pragmaton]: ../../../../offline-design/DRP-Mneme-Pragmaton-Model.draft.000.003.md
[ergon]: ../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.003.md
[parcel-final]: ../../../../offline-design/DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md
[security]: ../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#4-architectural-definition
[hcrl]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[pub-evidence]: Pragma-Publication-and-Subscription-Reconciliation-005.md
[pragma-evidence]: G03-Mneme-Pragma-Model-Reconciliation-005.md
[ergon-evidence]: G03-Ergon-to-Mneme-Pragmaton-Reconciliation-005.md
[persistence]: ../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[caches]: ../../../../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[assurance]: ../../../../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-19
[adr]: ../../../../../../docs/architecture-decisions.md#adr-018-----mnemosyne-defines-the-authoritative-durable-state-boundary
[petasos]: ../../../../../continuum/application/application-component/195e5f10db8e419391354197b5d7a8cc.md
[parcel]: ../../../../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md
[routing]: ../../../../../continuum/motivation/constraint/mandate/e024703b32c5437481ce290e8e50b079.md
[by-value]: ../../../../../continuum/motivation/constraint/b599ebffaf2f4e9e8deaec2ed98f2cd7.md
[indeterminate]: ../../../../../continuum/motivation/requirement/fc732b64fc564127b73174e28d9ccce8.md
[acceptance]: ../../../../../continuum/strategy/capability/0d036c0bb26b47dda1cf5d2c1510a083.md
[ack]: ../../../../../continuum/strategy/capability/feature/2f0cf045d3914c03adfcb87dea623978.md
[correlation]: ../../../../../continuum/strategy/capability/feature/13c97c4f6b7d4bd18da03332eb0fdc8b.md
