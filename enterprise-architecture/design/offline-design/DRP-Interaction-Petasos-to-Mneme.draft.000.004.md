<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Petasos-to-Mneme-001\
**Title:** Interaction::Petasos-to-Mneme\
**Status:** Draft.000.004 · Pending Solution Architect review\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Basis:** Architecture 1.1.26 · Software 0.1.0 / Iteration 005 / G03  
**Lineage:** [Reviewed predecessor][g03-predecessor] · [Earlier Draft.002][previous] · [G03 adjudications][g03-adjudications] · [Pragma Model][pragma] · [Publication/subscription][publication] · [DR-12][review]

## 1. Feature Realisation

This interaction transfers durable responsibility for an existing
subscriber-specific undertaking between independently recoverable activities.
Mneme manages the undertaking and Petasos manages its handoff.

### 1.1 New or Updated Feature Details

Reuse **Durable Work Acceptance** and the **AcceptedWorkRecordedBeforeAck**,
**Duplicate Delivery Recognition** and **RecoveryPreservesTaskLineage**
Features. The Pragma Model retains the Mneme information-management and
InformationUnit responsibilities. [Durable Work Acceptance][acceptance] · [Work recorded before acknowledgement][recorded] · [Duplicate recognition][duplicates] · [Recovery lineage][lineage] · [Pragma Model][pragma]

### 1.2 Associated Requirements

- Petasos SHALL request a distinct undertaking per applicable matching
  subscription under the applicable identity and retry rules. Mneme constructs
  the requested objects and governs the originating Pragma and the realisation.
- Acknowledged durable handoff SHALL require an authoritative, resolvable
  undertaking, recoverable lineage and evidence of Petasos's durable
  commitment. The parcel SHALL identify the established undertaking.
- Identity, subscriber and subscription association, IU version correspondence,
  and appropriate security and provenance context SHALL survive handoff.
- Duplicate delivery and retry SHALL preserve the identity of the same
  undertaking. Recognition of duplicate delivery SHALL NOT assume execution
  idempotence. Recovery SHALL NOT resume inside a failed Ergon.
- Undertaking persistence, durable acceptance, delivery/acknowledgement,
  execution, Mneme save and authoritative IU persistence SHALL retain their
  separate meanings and evidence.

### 1.3 Associated Strategy and Motivation

AX-04 keeps handoff meaning under Harmonia's governed semantics. AX-05 and
AX-06 preserve the state and authority boundaries in ADR-018. AX-07 governs
forwarding, resolution and execution. AX-08 and AX-16 preserve evidence and
observable progression. AX-10 covers failure and distributed recovery.
AX-14 keeps work and delivery outcomes distinct. AX-15 and REQ-FND-004 preserve
indeterminate results. AX-17 governs the qualified historical differences.
[Canonical Principles][axioms] · [ADR-018][adr] · [Explicit Indeterminate Outcome][indeterminate]

## 2. Proposal

Petasos durably delivers a parcel that refers to an already-established
subscriber-specific realisation. The receiver resolves the existing
undertaking through Mneme. The Ergon execution boundary establishes or
reconstructs its working context when required. [Publication/subscription][publication] · [Ergon–Mneme interaction][ergon]

## 3. Design Context

The accepted Mneme–Mnemosyne, IU and cache contracts govern authoritative
state and working context. The approved conceptual Parcel/ParcelMask DRP
distinguishes a conveyance from the information it conveys. The
PetasosParcel Topic retains its qualified Domain05 standing, including
the prior handoff account.
The Pragma-family candidates and the G03 instruction supply the sequence in
this draft. Its incorporation and the differences in §7 require adjudication.
[Persistence interaction][persistence] · [IU authority][iu] · [Two-cache architecture][caches] · [Conceptual Parcel/ParcelMask][parcel-final] · [PetasosParcel Topic][parcel] · [G03 Task][task]

## 4. Architectural Definition

### 4.1 Responsibility boundaries

| Responsibility | Owner | Worker | Involved |
| --- | --- | --- | --- |
| Publication eligibility | Ponos | Mneme | — |
| Subscription matching | Petasos | Petasos | Mneme |
| Pragmatosis generation | Petasos | Mneme | — |
| Queue routing | Petasos | Petasos | — |
| Queue management | Petasos | Petasos | — |

Mneme manages Pragma-family information and applies model invariants
through its PragmaFactory on requests from Ponos, DigitalTwinEngine and
Petasos. Mnemosyne establishes authoritative recoverable state through
the governed Mneme interaction. Petasos owns durable transport and
delivery to the intended Ergon queue established by the subscription.
Ponos governs execution, Praxis defines outcome rules, and the Ergon
carries out the bounded work. Functional ownership provides no access,
disclosure, forwarding or execution authorisation.
[Pragma Model][pragma] · [Petasos][petasos] · [Routing mandate][routing]

### 4.2 Work identity and reference handoff

1. Petasos applies SubscriptionMask semantics to eligible originating
   Pragma information. Mneme supplies governed Pragma and IU summary/metadata
   for R1/R2 and may provide authorised IU payload for R3+. Themis governs
   access and disclosure at each applicable boundary.
2. For each applicable match, Petasos requests construction of the
   corresponding undertaking through Mneme's PragmaFactory under the
   applicable identity, retry and authorisation rules. Zero matches
   yield no undertaking or Ergon TaskOutcome.
3. Each **Pragmatosis**, or **Noesis** for a realised Stimulus, has its
   own identity, lifecycle, outcome and lineage to the originating Pragma
   and input InformationUnit.
4. Mneme establishes the authoritative, recoverable state of that
   realisation through the governed Mnemosyne interaction.
5. A by-value **PetasosParcel** carries the established Pragmatosis identity
   with the permitted handoff metadata. The work reference is to the
   undertaking. The IU reference and payload remain outside the parcel,
   within governed information access.
6. Petasos routes the parcel to the intended Ergon queue already determined
   by the subscription and manages the queue and durable delivery.
7. On receipt, the Ergon resolves the existing undertaking through Mneme.
   Redelivery preserves the undertaking identity.
8. When governed execution requires working information, the execution
   boundary establishes or reconstructs the undertaking's Pragmaton.

The by-value parcel represents a conveyance. The referenced work retains
its own authoritative representation. Full InformationUnit Content,
Pragmaton and WorkSpace contents stay within their governed access and
execution boundaries. This handoff rule applies to subscriber-specific work.
Other Petasos communication purposes retain their own payload contracts.
[Publication/subscription handoff][pub-handoff] · [PetasosParcel Topic][parcel] · [Parcel by-value constraint][by-value]

### 4.3 Durability and acknowledgement

| Event | What its evidence establishes |
| --- | --- |
| Authoritative undertaking persistence | Mnemosyne has durably established the realisation and the lineage needed to resolve and recover it under the applicable persistence guarantees. |
| Petasos durable acceptance | Petasos has taken on the durable handoff obligation for the particular reference under a defined transition. |
| Petasos delivery and acknowledgement | Delivery evidence records the outcome of a particular attempt to the intended destination. An acknowledgement attests only to its specified event and outcome. A transport receipt establishes receipt at its own boundary. |
| Ergon execution | The particular Ergon instance was admitted, progressed or completed as established by execution evidence. Actual processing has its own TaskOutcome under Praxis rules. Resolution of the undertaking, undertaking disposition, originating Pragma disposition, output mediation and Assurance remain distinct. |
| Mneme information save | Mneme handled the return of working information and determined its governed disposition. |
| Authoritative InformationUnit persistence | Mnemosyne established or reported the authoritative IU mutation outcome and applicable revision under the owning contract. |

Before acknowledging **durable handoff**, establish both the authoritative,
resolvable undertaking with its necessary lineage and the recoverable
Petasos handoff obligation. Authoritative persistence alone supplies no
evidence of successful delivery. Acknowledge each event according to its
own boundary. An external acknowledgement, where applicable, retains its
own interoperability contract. [Work recorded before acknowledgement][recorded] · [ADR-018][adr] · [Five-contract interaction][persistence] · [Petasos][petasos]

Minimum evidence identifies the undertaking, the handoff obligation and the
intended recipient, and correlates the applicable parcel and delivery
occurrence. It identifies the acknowledgement's issuing boundary, event
meaning, time and outcome, with the supporting evidence for the durability
or delivery that it claims. Recoverable lineage identifies the originating
Pragma, matched subscription, subscriber, source IU and applicable revision.
Carry required handoff metadata or make it resolvable through governed
references. This evidence requirement prescribes neither parcel fields nor
an acknowledgement protocol. [Delivery Correlation][delivery-correlation] · [Conceptual Parcel/ParcelMask][parcel-final] · [Recovery lineage][lineage]

### 4.4 Duplicate delivery, retry and recovery

| Case | Required architectural behaviour |
| --- | --- |
| Multiple matching subscriptions | Preserve a separate realisation and independent handoff, execution and outcome for each match. Two distinct subscriptions associated with the same subscriber still produce distinct undertakings. |
| Partial fan-out | Preserve established undertakings and each handoff's actual outcome. Account separately for obligations still outstanding or uncertain. Recovery of one obligation preserves the completed siblings and their outcomes. |
| Indeterminate persistence | Preserve uncertainty and reconcile against the governed Mneme/Mnemosyne state before establishing any dependent handoff. An unknown result supplies no basis for creating another undertaking for the same obligation. |
| Indeterminate transport or repeated handoff | Correlate the existing undertaking and handoff obligation with authoritative work and Petasos evidence. Preserve unresolved results. Where a repeat is permitted, use the same undertaking for the same work obligation and distinguish individual delivery attempts. |
| Duplicate delivery or redelivery after consumer failure | Recognise duplicate delivery independently of Ergon execution. Resolve the same undertaking and consult its governed progression before deciding whether any further execution is eligible. A redelivery supplies neither execution permission nor evidence that earlier processing did or did not occur. |
| Ergon failure | Recover at the governed execution boundary using durable work state, authoritative information and applicable checkpoints. Keep execution retry distinct from Petasos redelivery. Execution does not resume inside a failed Ergon. |
| Retry subject to `DoNotBefore` | Preserve the applicable earliest execution time across handoff and recovery. Before it, execution remains deferred. At or after it, any permitted retry retains the same work identity and still requires applicable authority and recovery conditions. |

Duplicate delivery recognition is a handoff and correlation requirement.
Execution idempotence is a separate property of the executing activity
and supplies no assumption for recovery. Designated deliberate replay
retains its existing governed meaning and linked attempt evidence.
Indeterminate outcomes stay visible until owning evidence establishes
the result. [Duplicate recognition][duplicates] · [Publication/subscription reliability][pub-reliability] · [Pragma progression and scheduling][pragma-progression] · [Petasos recovery][petasos] · [Explicit Indeterminate Outcome][indeterminate]

### 4.5 Execution working information

When governed execution is to start, `Ergon.ContextLoader` collaborates
with Mneme to establish the realised undertaking's Pragmaton.
`Ergon.WorkSpace` exposes its managed working information to the
ProcessingEngine. Mneme's Workflow Cache supports transient local
materialisation. Reconstruct required working information from governed
sources at the applicable version and durable execution state, subject
to the existing context and recovery contracts. [Pragmaton and WorkSpace][pragmaton] · [Ergon–Mneme establishment][ergon] · [Ergon–Mneme recovery][ergon-recovery] · [Two-cache architecture][caches]

### 4.6 Security, provenance and Assurance

Apply Themis policy at the applicable handoff, resolution and execution
boundaries. Petasos applies the Themis-owned enforcement capability for its
governed FORWARD operation. Mneme and the Ergon enforce the applicable
access and execution controls at their boundaries. Preserve trusted identity
and security-context continuity using the applicable G01 operation and
delegation rules. Transport metadata supplies correlation and conveys no
independent authority to access or execute governed information.
[Petasos FORWARD enforcement][parcel] · [G01 authorisation][security] · [Trusted transport question][dr7]

Correlate originating work, realised undertaking, subscription, subscriber,
source IU identity/revision, Petasos delivery and acknowledgement, execution
and persistence outcomes. Capture security and information-significant
events under Kleio audit and provenance policy. Dokimasia uses relevant
evidence to compare expected and observed progression independently.
Assurance and observation retain their separate meanings.
[Provenance and Assurance][assurance] · [Publication/subscription reliability][pub-reliability]

### 4.7 Kleio Audit Capture

Petasos submits semantically meaningful auditable occurrences for its own
operations to the Kleio Audit Capture contract as applicable policy requires.
Mneme separately contributes evidence of its governed information operations.
Kleio owns Audit Capture and constructs and manages the authoritative FHIR
AuditEvent. AuditEvent, LogEvent, Provenance and Assurance retain separate
meanings. Mandatory capture uses Kleio's governed platform authority and
fails closed where the required durable evidence cannot be established.
[Canonical AX-07][g03-ax07] · [Kleio][g03-kleio] · [Mneme Audit candidate][g03-mneme-audit]

The Solution Architect's proposed per-Petasos-Engine local integration
corresponds functionally to Kleio's existing client contract. The exact
Audit.Agent/Audit.Satellite identity and a Kleio-governed Artemis staging
boundary remain for review. The linked candidate evaluates durability
parity, a same-broker transaction and recovery. The existing capture
requirement governs this interaction. [Petasos–Kleio investigation][g03-audit]

## 5. Explicitly Not Prescribed

Interface signatures, parcel encodings, persistence schema, coordination
mechanisms, messaging protocols and retry algorithms remain solution design
under the existing architectural constraints. This proposal defines
semantic ordering, recovery obligations and evidence.

## 6. Evidence and Rationale

The reconciliation traces the retained Draft.002 boundaries, candidate
sequence, historical discrepancies and proposed follow-on work. Earlier
design and implementation sources supply lineage and discovery evidence.
[Petasos–Mneme reconciliation][evidence] · [Pragma model reconciliation][pragma-evidence] · [Ergon–Mneme reconciliation][ergon-evidence]

## 7. Relationship to Current Architecture

| Source treatment | Candidate correspondence |
| --- | --- |
| Draft.002 §3.3 allows identification or reconstruction of the relevant Pragma | Keep reference-based identification and correlation. For this handoff the parcel references the already-established subscriber realisation. |
| Draft.002 §3.4 describes Mneme establishing Pragma on receipt | Receipt resolves or recovers the existing realisation through Mneme. The proposed ordering establishes that undertaking and its recoverable state before handoff. |
| Draft.002 §§3.2 and 3.4 allow recovery of processing information | Retain durable work acceptance as its own transition and recover working information when execution requires it. This candidate requires authoritative undertaking state and lineage before acknowledged durable handoff. |
| Draft.002 §3.5 separates redelivery and Ergon failure | Preserve that distinction and make reuse of the same undertaking identity explicit. Execution retry remains a governed decision. |
| The qualified PetasosParcel Topic describes an originating work reference and consumer-side undertaking creation | Petasos requests construction through Mneme before handoff and carries the established undertaking identity. The candidate changes both the reference target and the establishment allocation. Reconcile those differences and the prior qualified allocation of semantic selection before incorporation. |

Preserve ADR-018's general durable-work boundary and the other Petasos
communication purposes. Reuse canonical identities and relationships only
where correspondence has been established. [Source Draft.002][previous] · [PetasosParcel Topic][parcel] · [ADR-018][adr] · [Reconciliation][evidence]

## 8. Conformance Expectations

Review independent fan-out, same-subscriber matches, partial completion,
indeterminate persistence and delivery, duplicate receipt, failure recovery
and retry eligibility. Verify the referenced realisation is authoritative
before durable acceptance is acknowledged, and verify distinct evidence for
delivery, execution, save and IU persistence. [Review cases][evidence]

## 9. Open Questions / Matters for Review

1. Confirm the canonical reference and work identity correspondence and the
   disposition of the qualified work-reference/consumer-creation semantics.
2. Confirm the minimum recoverable state, acknowledgement ownership and
   boundary-specific evidence, including partial fan-out and indeterminate
   persistence or delivery.
3. Confirm equivalence of the same work and handoff obligation across
   subscription changes, repeated delivery, retry and deliberate replay.
   Retain the allocation of duplicate, execution and scheduling decisions
   for review where their detailed boundary remains open.
4. Confirm trusted context and security evidence across transport, including
   the existing DR-7 question. Reconcile parcel metadata and criteria against
   subscription and routing requirements without changing DR-10 or G03
   A06/A07 obligations. [DR-12][review] · [Reconciliation][evidence]


## 10. Continuum Update

The entries below identify candidate Continuum correspondence for
adjudication before any governed incorporation.

### 10.1 Motivation Updates

- Associate the Feature realisation with the Requirements, Axioms, Principles, Constraints, Goals/Outcomes and other Motivation elements identified in Section 1.
- Add/refine any requirement made explicit by this DRP only after architectural review; do not delegate requirement discovery to implementation.
- Preserve existing Motivation elements unchanged where they already express the required semantics.

### 10.2 Strategy Updates

- Establish/refine the Enterprise Capability and Feature context identified in Section 1.
- Map the Enterprise Capability/Features to the applicable Business and Business-Enabling capability context.
- Associate applicable Courses of Action and other Strategy elements.
- If no defensible Feature and Enterprise Capability placement can be established, stop and reconsider the architectural justification for the DRP.

### 10.3 Business Layer — Information Architecture Updates

- Reuse Pragma, subscriber undertaking, InformationUnit and work correlation
  concepts, subject to confirmed canonical correspondence.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates

- Relate durable work handoff and Pragma-management behaviours to their Business/Enabling capability context.

### 10.5 Application Layer — Information Architecture Updates

- Reconcile the PetasosParcel reference target, responsibility for establishing
  the undertaking and stable evidence of the distinct handoff outcomes.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation

- Establish/refine Petasos-to-Mneme Interaction and relate it to durable work and Pragma-management Features.

### 10.7 Application Layer — Structural / Component Updates

- Reuse Petasos and Mneme; transport adapters/serialisers/handlers remain implementation design.

### 10.8 Cross-Layer and Cross-Feature Relationships

- Connect the application realisation to the Feature(s) it realises/supports rather than leaving application constructs isolated.
- Preserve explicit relationships to applicable Requirements, Axioms, Courses of Action and information concepts.
- Establish Access, Realisation, Serving, Assignment, Association, Influence or other native/specialised relationships according to their actual semantics; do not use generic Association where a stronger governed relationship is known.
- Connect related Features where the design depends on another Feature's services or information rather than duplicating that capability locally.

### 10.9 Explicitly Unaffected / No-Change Architecture

- Implementation classes, interfaces, DTOs, algorithms, products, deployment topology and internal module decomposition remain Technical Lead solution-design space unless explicitly elevated by this DRP.
- Existing architecture referenced only as context is not modified unless this section explicitly says so.

## 11. Review Decision

**Decision:** Pending\
**Decision authority:** Solution Architect / Architecture Review\
**Decision notes:** (To be completed during review.)

## 12. Post-Decision Actions

(To be completed after review.)

## Appendix A --- Design Provenance

This appendix is informative, not normative.

Principal lineage includes Draft.002 and the ADR-018 durable work-acceptance
distinction. The companion Pragma-family drafts supply the current design
correspondence. [Source draft][previous] · [ADR-018][adr] · [G03 Task][task]

[previous]: DRP-Interaction-Petasos-to-Mneme.draft.002.md
[pragma]: DRP-Mneme-Pragma-Model.draft.000.004.md
[pragma-progression]: DRP-Mneme-Pragma-Model.draft.000.004.md#44-progression-and-scheduling
[pragmaton]: DRP-Mneme-Pragmaton-Model.draft.000.004.md#37-pragmaton-and-ergonworkspace
[publication]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md
[pub-handoff]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#42-selection-realisation-and-handoff
[pub-reliability]: DRP-Pragma-Publication-and-Subscription.draft.000.003.md#5-reliability-and-consistency-requirements
[ergon]: DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md#41-establishment
[ergon-recovery]: DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md#47-failure-and-recovery
[parcel-final]: DRP-PetasosParcel-and-PetasosParcelMask.Final.001.md
[security]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#4-architectural-definition
[review]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[dr7]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-7-pragma-fhir-security-context-authority
[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-petasos-to-mneme-reconciliation-task.md
[evidence]: ../evidence/software-0.1.0/005/after/G03-Petasos-to-Mneme-Reconciliation-005.md
[pragma-evidence]: ../evidence/software-0.1.0/005/after/G03-Mneme-Pragma-Model-Reconciliation-005.md
[ergon-evidence]: ../evidence/software-0.1.0/005/after/G03-Ergon-to-Mneme-Pragmaton-Reconciliation-005.md
[persistence]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[caches]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[iu]: ../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[assurance]: ../register/1.1.19/hcrl-1.1.19-002.complete.md#dr-19
[axioms]: ../../governance/architectural-axioms.md
[adr]: ../../../docs/architecture-decisions.md#adr-018-----mnemosyne-defines-the-authoritative-durable-state-boundary
[petasos]: ../../continuum/application/application-component/195e5f10db8e419391354197b5d7a8cc.md
[parcel]: ../../continuum/application/data-object/f0a88f05a0cb438cb42572ec07fb8513.md
[routing]: ../../continuum/motivation/constraint/mandate/e024703b32c5437481ce290e8e50b079.md
[by-value]: ../../continuum/motivation/constraint/b599ebffaf2f4e9e8deaec2ed98f2cd7.md
[indeterminate]: ../../continuum/motivation/requirement/fc732b64fc564127b73174e28d9ccce8.md
[acceptance]: ../../continuum/strategy/capability/0d036c0bb26b47dda1cf5d2c1510a083.md
[recorded]: ../../continuum/strategy/capability/feature/2f0cf045d3914c03adfcb87dea623978.md
[duplicates]: ../../continuum/strategy/capability/feature/c4ba2198e8a2460d802b0845c938b608.md
[lineage]: ../../continuum/strategy/capability/feature/157b20d5c544429a8ada854c45ec244f.md
[delivery-correlation]: ../../continuum/strategy/capability/feature/13c97c4f6b7d4bd18da03332eb0fdc8b.md

[g03-predecessor]: DRP-Interaction-Petasos-to-Mneme.draft.000.003.md
[g03-adjudications]: ../evidence/software-0.1.0/005/after/G03-Pragma-Family-Adjudications-005.md
[g03-audit]: DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md
[g03-ax07]: ../../continuum/motivation/principle/architectural-axiom/5de2ae2c3b794c2d9f3a14efd51d564f.md#kleio-mandatory-capture-authority
[g03-kleio]: ../../continuum/application/application-component/fefda110e2274884ad58906733606efd.md#auditability-and-availability
[g03-mneme-audit]: DRP-Mneme-Audit.draft.002.md#4-architectural-definition
