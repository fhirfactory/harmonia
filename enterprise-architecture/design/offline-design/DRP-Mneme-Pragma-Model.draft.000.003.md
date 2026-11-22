<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal (DRP)

**DRP:** DRP-Mneme-Pragma-Model-001\
**Title:** Mneme Pragma Model — Concept and Requirements\
**Status:** Draft.000.003 · Pending Solution Architect review\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Basis:** Architecture 1.1.26 · Software 0.1.0 / Iteration 005 / G03  
**Lineage:** [Pragma Model Draft.002][previous] · [Publication/subscription Draft.000.002][publication] · [Pragmaton Draft.000.003][pragmaton] · [DR-12][review]

## 1. Feature Realisation

This DRP defines the work information through which Harmonia receives
governed input, progresses a subscriber-specific undertaking and expresses
its outputs.

### 1.1 New or Updated Feature Details

Use the existing Features **EligibleTaskPublication**, **RecoveryPreservesTaskLineage**
and **SingleSchedulingAuthority** for the publication, work progression and
scheduling concerns. The preceding draft's Information Unit access and
execution/handoff context remains relevant. Review correspondence to the existing
Mneme **Pragma Management** application function before allocating any new
Feature or architectural element. [Publication][eligible] · [Work lineage][lineage] · [Scheduling][scheduler-feature] · [Pragma Management][management]

### 1.2 Associated Requirements

- Harmonia SHALL distinguish the reusable work definition, originating Pragma,
  subscriber-specific Pragmatosis and Pragmaton working information.
- Mneme SHALL manage both Pragma and Pragmatosis. Ponos, the Ergon and Petasos
  exercise their assigned execution and handoff responsibilities.
- Each Pragma SHALL represent exactly one input InformationUnit and zero or
  more generated output InformationUnits. The input, outputs, origin and
  subscriber realisation SHALL remain governed and correlatable.
- Each realisation SHALL retain independent identity, lifecycle, outcome and
  lineage through handoff, execution, completion and retry.
- Embedded IU summaries SHALL be immutable, content-free and non-authoritative
  and SHALL preserve source identity and relevant version correspondence.
- Progression SHALL preserve any applicable `DoNotBefore` scheduling constraint
  and remain distinguishable from authoritative IU version progression and
  Petasos transport state.
- Pragma management SHALL preserve applicable security, provenance and audit lineage.

### 1.3 Associated Strategy and Motivation

AX-04 assigns the semantics of work and scheduling to Harmonia. AX-05 and
AX-06 distinguish authoritative state from working data, embedded summaries and
transport. AX-07 governs access, execution and release. AX-08 and AX-16 require
evidence and observable progression. AX-14 preserves separate work, undertaking
and outcome meanings. AX-15 preserves uncertain handoff and execution outcomes,
and AX-17 reserves unconfirmed mappings for review. CORE-004 and PROV-001
support correlation, causation and provenance across boundaries. [Canonical Principles][axioms] · [CORE-004][core004] · [PROV-001][prov001]

## 2. Proposal

Retain Pragma as the Mneme-managed activity information envelope and distinguish
its originating work role from the **Pragmatosis** role for a particular
subscriber's undertaking. Align input, output, lifecycle and working-information
semantics with the two companion candidates. [Publication/subscription][publication] · [Pragmaton][pragmaton]

## 3. Design Context

The Continuum's work definition, work instance, undertaking and outcome Topics
record bounded adjudicated meanings. The Pragma Topic and Mneme Pragma
Management retain their qualified Domain05 standing. The completed IU and
Mneme–Mnemosyne reviews govern information authority and persistence.
The current publication/subscription and Pragmaton drafts supply the agreed
design direction for this proposal. Continuum incorporation remains pending. [Pragma Topic][pragma] · [Pragma Management][management] · [IU authority][iu] · [Mneme–Mnemosyne interaction][persistence]

## 4. Architectural Definition

### 4.1 Work identity and realisation

The **reusable work definition** specifies work of a kind, its expectations
and constraints. **Pragma** identifies a particular originating archetypal
work instance available for subscription. **Pragmatosis** identifies a
particular subscriber-specific undertaking of that work. Each undertaking
has independent identity, lifecycle, outcome, subscriber association and
lineage to the originating Pragma and its input. `Stimulus` → `Noesis` is
the Digital Twin specialisation. Dokimasia-specific names for the Pragma and
its realisation remain unresolved. [Publication/subscription realisation][publication-selection] · [Pragmaton work and IU distinctions][pragmaton-work]

| Historical terminology | Correspondence for review |
| --- | --- |
| `ActionableTaskArchetype` | Defines reusable work. It supplies definition-level semantics for work instantiation. The published Pragma is a particular instance. [TaskDefinition Topic][definition] |
| `ActionableTask` | Identifies particular work, providing a functional comparison to the originating Pragma. Preserve the governed concurrency, outcome participation and output composition semantics for explicit correspondence review. [Task Topic][work] |
| `FulfillmentTask` | Identifies a particular undertaking, providing a functional comparison to Pragmatosis. Canonical identity, claims, representations and handoff correspondence remain for review. [TaskFulfillment Topic][undertaking] |
| `TaskOutcome` | Resolves originating work under its outcome policy. A Pragmatosis has its own outcome and may contribute to work resolution under a separate governed decision. Sibling completion and successful handoff do not independently establish work resolution. [TaskOutcome Topic][outcome] |

These correspondences identify roles to reconcile. Canonical identity and
representation require an explicit decision.

### 4.2 InformationUnit input and output

`Pragma.input()` represents **exactly one** governed InformationUnit.
`Pragma.output()` represents **zero or more** generated InformationUnits.
Generated outputs are attributable to the undertaking that produced them.
Changes to existing Pragmaton working values follow governed Mneme save
semantics. Newly generated InformationUnits are distinct Pragma Outputs.
Outputs for publication follow the applicable PublishingMask before downstream
Pragma creation. Existing Praxis-defined Pathway continuation retains its
separate role and deferred representation. [Publication flow][publication-output] · [Pragma Topic][pragma] · [Pragmaton work and IU distinctions][pragmaton-work]

An embedded IU summary in a Pragma or Pragmatosis is an immutable,
content-free, non-authoritative snapshot. It preserves the source InformationUnit
identity and relevant version correspondence. Authoritative IU versions are
established through the governed Mneme–Mnemosyne interaction. Older snapshots
remain tied to the versions they represent. [IU authority][iu] · [Mneme–Mnemosyne interaction][persistence]

### 4.3 Pragmaton and WorkSpace

**Pragmaton** is the Mneme-managed execution-information environment associated
with a realised undertaking. **`Ergon.WorkSpace`** is the Ergon-facing access
abstraction for that environment. The originating Pragma and Pragmatosis carry
work meaning and identity. Pragmaton carries bounded working information for
execution. The companion Pragmaton candidate defines contextualisation, the
JavaSpaces-inspired information-space semantics and governed acquisition. [Pragmaton model][pragmaton] · [WorkSpace relationship][pragmaton-workspace]

### 4.4 Progression and scheduling

| Concern | Architectural meaning |
| --- | --- |
| Publication | Producer-side eligibility determines whether generated information may yield an originating Pragma available for subscription. |
| Subscription realisation | An eligible match produces its own subscriber-specific undertaking under Mneme management and the applicable authorisation. |
| Durable handoff | Petasos accepts durable transport of a reference to the undertaking under the handoff contract. The authoritative realisation and resolvable lineage must exist before durable acceptance is acknowledged. |
| Execution | Ponos and the responsible Ergon progress the identified undertaking in its governed context, subject to applicable scheduling and authorisation. |
| Completion | Attribute the completion disposition and outputs to the undertaking. The outcome policy governs any effect on originating work. |
| Retry | Preserve the undertaking identity, lineage and applicable scheduling constraint for the same work obligation. Retry and redelivery can have separate outcomes. Preserve uncertainty until sufficient governed evidence establishes the outcome. |

Where applicable, **`DoNotBefore` is the earliest permitted start of execution**
for the undertaking. Before that time, execution remains deferred. Reaching
that time satisfies the timing constraint alone. Other eligibility,
authorisation and recovery conditions continue to apply. The constraint
applies across handoff and retry and uses the existing coherent scheduling
responsibility. Its formal allocation, representation and evidence remain
for review. These distinctions prescribe no implementation lifecycle state
machine. [G03 instruction][task] · [Coherent Workflow Scheduler][scheduler-principle] · [SingleSchedulingAuthority][scheduler-feature] · [Publication and handoff reliability][publication-reliability]

### 4.5 Responsibility boundaries

| Responsibility | Allocation |
| --- | --- |
| Work information | Mneme owns Pragma and Pragmatosis information management, including governed creation/realisation, identities, lineage and progression information. |
| Durable transport and handoff | Petasos owns transport, concrete handoff and its operational state. |
| Execution | Ponos governs execution and specialised Ergons perform bounded activity. Praxis retains its existing fulfilment and continuation semantics. |
| Authoritative persistence | Mnemosyne establishes durable authoritative state through the governed Mneme interaction. |

Pragma is the governed information envelope. Workflow, execution, transport,
authoritative persistence and audit records have their own responsibilities
and representations. [Pragma Management][management] · [Pragma Topic][pragma] · [Persistence interaction][persistence]

### 4.6 Security, provenance and audit

Themis governs access, execution and release. Correlate originating work,
subscriber undertaking, source and output InformationUnits, handoff and
governed outcomes for applicable provenance, Kleio audit and Dokimasia
Assurance. Operational security context has its own governed treatment
independent of persisted business content. [G01 authorisation][security] · [Publication/subscription evidence and assurance][publication]

### 4.7 Invariants

- Transport, execution and use do not confer ownership of Pragma or Pragmatosis.
- Work progression, transport state, execution outcome and authoritative IU
  version progression retain separate meanings.
- Summaries and working changes cannot establish authoritative versions. New
  outputs remain distinguishable from changes to existing working information.
- Camel Exchange, FHIR Task, PetasosMessage and Java types have their applicable
  implementation or interchange meanings. Any mapping to the architectural
  work model requires explicit correspondence.

## 5. Explicitly Not Prescribed

Java classes and interfaces, DTOs, physical schemas, products, frameworks,
protocols, deployment topology and algorithms remain solution-design choices
under the governed constraints. This includes lifecycle encodings, timer and
retry mechanisms, identifier formats and storage layout.

## 6. Evidence and Rationale

The reconciliation evidence traces retained Draft.002 content, adopted candidate
semantics, discrepancies and proposed follow-on corrections. Historical
implementation and Design Notes supply discovery and lineage. Architectural
standing comes from the applicable Continuum and attributable review decisions. [G03 reconciliation][evidence]

## 7. Relationship to Current Architecture

If accepted, reconcile the draft using existing Continuum elements wherever
adequate. Preserve Pragma's governed information role, including the recorded
realisation of Metabolē, and the existing separation from Pylai.Diadromí.
Escalate any modelling gap or new abstraction with explicit rationale for
review. [Pragma Topic][pragma] · [G03 reconciliation][evidence]

## 8. Conformance Expectations

Verify distinct undertakings for different matches, stable lineage across
retry, no execution before an applicable `DoNotBefore`, and independent
completion and handoff outcomes. Verify exact input cardinality and output
attribution, immutable summaries with source-version correspondence, and
governed persistence of working changes. [G03 reconciliation][evidence]

## 9. Open Questions / Matters for Review

1. Confirm canonical correspondence and minimum identity, lifecycle and
   correlation contracts for originating work, undertakings and resulting
   work resolution. Preserve existing concurrency and outcome policies while
   adjudicating Work Order, To Do, synthetic Task and boundary representations.
2. Confirm the scheduling contract beyond the earliest-start constraint, including
   canonical placement of `DoNotBefore`, governed decision ownership at the
   execution boundary, and evidence after recovery or an uncertain result.
3. Determine the formal association and lifecycle of Pragmaton for the realised
   undertaking and any required durable relationship to the originating Pragma.
   The WorkSpace access relationship is agreed at design level.
4. Reconcile output attribution with any originating-work aggregation and the
   representation of Pathway continuation. Confirm required input/output
   version and correlation semantics across handoff and persistence.
5. Decide Dokimasia-specific names and confirm Twin-specific correspondence,
   including Effector. Carry forward existing publication/subscription and
   Pragmaton review obligations under DR-12. [DR-12][review] · [G03 reconciliation][evidence]


## 10. Continuum Update

The entries below identify candidate Continuum correspondence for adjudication
before any governed incorporation.

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

- Reconcile the reusable definition, originating work, undertaking and work
  outcome with the existing business information model, preserving identities
  and outcome meanings where correspondence is established.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates

- Relate Pragma management to applicable work/execution Business/Enabling behaviour only where architecturally meaningful.

### 10.5 Application Layer — Information Architecture Updates

- Reuse the existing application information elements to express Pragma,
  Pragmatosis, input/output cardinality and lineage where sufficient.
  Review any additional boundary contract explicitly.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation

- Establish/refine Mneme Pragma-management behaviour and its relationships to execution/handoff interactions.

### 10.7 Application Layer — Structural / Component Updates

- Reuse Mneme, Mnemosyne, Ponos/Praxis/Ergo and Petasos components according to
  the recorded responsibility boundaries.

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

Principal lineage includes the preceding Pragma Model and the Design Notes on
Mneme Context, Pragma management and output continuation. The G03 Task records
the operative review directions. [Preceding draft][previous] · [Mneme Context and Pragma Management][mneme-note] · [Pragma Management and Energeia Boundary][management-note] · [Outputs and Pathway Continuation][output-note] · [G03 Task][task]

[previous]: DRP-Mneme-Pragma-Model.draft.002.md
[publication]: DRP-Pragma-Publication-and-Subscription.draft.000.002.md
[publication-output]: DRP-Pragma-Publication-and-Subscription.draft.000.002.md#41-output-and-publication
[publication-selection]: DRP-Pragma-Publication-and-Subscription.draft.000.002.md#42-selection-realisation-and-handoff
[publication-reliability]: DRP-Pragma-Publication-and-Subscription.draft.000.002.md#5-reliability-and-consistency-requirements
[pragmaton]: DRP-Mneme-Pragmaton-Model.draft.000.003.md
[pragmaton-work]: DRP-Mneme-Pragmaton-Model.draft.000.003.md#312-pragma-pragmatosis-informationunit-and-output-separation
[pragmaton-workspace]: DRP-Mneme-Pragmaton-Model.draft.000.003.md#37-pragmaton-and-ergonworkspace
[review]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[task]: ../evidence/software-0.1.0/005/before/ai-agent-tasks/unresolved-g03-mneme-pragma-model-reconciliation-task.md
[evidence]: ../evidence/software-0.1.0/005/after/G03-Mneme-Pragma-Model-Reconciliation-005.md
[axioms]: ../../governance/architectural-axioms.md
[core004]: ../../continuum/motivation/requirement/assurance-requirement/f2e504e7d6374e41a81303265adda6de.md
[prov001]: ../../continuum/motivation/requirement/assurance-requirement/3e45e60affb44bf384d54de26d3add6a.md
[eligible]: ../../continuum/strategy/capability/feature/c637e91e3d5e402386ff352d0326c1c5.md
[lineage]: ../../continuum/strategy/capability/feature/157b20d5c544429a8ada854c45ec244f.md
[scheduler-feature]: ../../continuum/strategy/capability/feature/48977be5d3b24f82ba676a0b8a26e0d6.md
[scheduler-principle]: ../../continuum/motivation/principle/architectural-axiom/99adfc9b5af049748df91755d395c247.md
[pragma]: ../../continuum/application/data-object/27ad143e7c784e378556af8a57c438f7.md
[management]: ../../continuum/application/application-function/ab0cdaa7ed4149ee856216efa5a265c4.md
[definition]: ../../continuum/application/data-object/topic/1d6d4c3a2df440be94262855fa6564eb.md
[work]: ../../continuum/application/data-object/topic/c43139289cb649b4836521a96a51604c.md
[undertaking]: ../../continuum/application/data-object/topic/30f4deffcd694f07bf54b2b192695d4f.md
[outcome]: ../../continuum/application/data-object/topic/08d8ee19a30f418f9d5bc497ced0d1ef.md
[persistence]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[iu]: ../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[security]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md
[mneme-note]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Mneme-Context-and-Pragma-Management.md#10-pragma-management
[management-note]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Pragma-Management-Responsibility-and-Energeia-Boundary.md#responsibility-separation
[output-note]: ../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Pragma-Outputs-and-Pathway-Continuation.md#3-outputs-and-continuation
