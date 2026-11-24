<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->

# Domain05 — Information Systems Layer Architecture

**Standing: A4 — PASS; the current Harmonia Candidate Information Systems
Architecture is accepted as the baseline for Step B, by the explicit 2026-10-13
B1 task decision.** This is the canonical RADS record of that accepted candidate,
not the final Solution Architecture baseline. Inclusion does not establish
that a component or feature is required for R1.x/R2.x. Absence of upstream
traceability at this stage does not invalidate a documented candidate.

## Purpose and completion boundary

Harmonia already has substantial solution design. This first Domain05 activity
reconstructs that design from repository documentation, preserving established
RADS boundaries and explicitly retaining competing or incomplete candidate
meanings. It does not derive another solution from Domains01–04 or use source
code, Maven modules, classes, deployment units or technology products to supply
missing architecture.

The authorised Domain05 approach is:

1. Document the Candidate Architecture — **this initial reconstruction**.
2. Map upstream capability and information architecture and establish traceability.
3. Identify genuine architecture gaps against that upstream architecture.
4. Identify excess candidate features/components and establish the MVP Candidate Architecture.
5. Establish the implementation plan.

**Current result: B2.1 — Individual Care Delivery ↔ Health Information Access: PASS.**
The [B2.1 trace](traceability/b2-1-individual-care-delivery-health-information-access.md)
records the approved L1 and six L2 capability-enablement relationships and their
rationales. B2.2 is not commenced; no other capability mapping or application
allocation is established by this reconciliation.
The [governing method](traceability/step-b-method.md), canonical Domain04
[Metabolē pattern](../04-information-architecture/patterns/metabole.md) and
[Health Information Retrieval Service example](traceability/health-information-access-worked-example.md)
retain the B1 decisions and method demonstration. The earlier B1 reconciliation
was returned for Mark/ChatGPT adjudication; this task does not separately declare
B1 PASS.
Step A is complete at the sufficiently-faithful candidate acceptance boundary.
Retained candidate deferrals remain explicit and upstream coverage is unproven.

The following Step-A reconciliation history records the standing at each review;
its A4-pending/Step-B-not-started statements do not supersede the current
acceptance and bounded B2.1 result above.

Initial reconstruction (A1, recorded here as Step 1) is complete. The
[U17 assessment](#sylloge-adjudication) now records A2 COMPLETE and A3 COMPLETE
WITH NON-BLOCKING DEFERRED ITEMS. At that review Step A remained open and
A4 acceptance was pending, with recommendation **READY FOR A4 ADJUDICATION**.
The Energeia / DigitalTwin / Dokimasia and Pathway / Journey / Publish/Subscribe /
handoff and Energeia / Praxis / Journey / persistence adjudications below correct
the candidate within that boundary. The Kleio / Themis / Petasos adjudication
below further corrects the candidate and R1.x/R2.x Observe → Report scope.
The [Mneme / Mnemosyne correction](#mneme-mnemosyne-adjudication) incorporates
Context/Pragma Management and Persistence/Search/Research Metadata Management.
The [Identity consolidation](#identity-candidate-adjudication) reconciles
externally provided authentication with Themis-owned authorisation semantics.
The [Calliope / Pylai / Praxis.Horos consolidation](#calliope-pylai-horos-adjudication)
incorporates the supplied adjudications with explicit deferrals.
The [three subsequent boundary adjudications](#boundary-reconciliation) resolve
the retained Terma/Strategy, Calliope realisation and Kleio capture-authority
conflicts. The [Agora / Omilia reconciliation](#agora-omilia-adjudication) now
establishes collaboration control, operations, ownership and capture boundaries.
The [Sylloge adjudication](#sylloge-adjudication) closes the remaining F31/D15
Binary Content Management responsibility question. [U18 output/continuation
reconciliation](#pragma-continuation-adjudication) records the subsequently
adjudicated Pragma-role and Journey distinction and an explicit taxonomy wording
conflict. It does not reassess A4 readiness or change acceptance standing.
The [U19 Digital Twin Behaviour reconciliation](#digital-twin-behaviour-adjudication)
captures the agreed A4.7 execution decisions and closes that taxonomy wording
conflict. It prepares documentation for Mark/ChatGPT review; **A4.7 is not declared
PASS**; A4 acceptance and Step-B commencement were still pending at that review.
Remaining capability mapping, Information Architecture coverage, upstream requirements
gap analysis, excess-feature analysis, MVP
determination, final baselining and implementation planning remain future work.
The [documentation gap register](gaps.md) records insufficiencies within the
documented candidate, not unsupported upstream requirements. This initial
record does not declare the Domain Completion Gate satisfied.

## Read the candidate

| View | Contents |
| :--- | :--- |
| [Step B traceability method](traceability/step-b-method.md) | Canonical B1-R01–R12 rules, graph, enablement semantics, B2-R05/R06 references and B2-R07 scope. |
| [B2.1 — Individual Care Delivery ↔ Health Information Access](traceability/b2-1-individual-care-delivery-health-information-access.md) | PASS for L1 and six L2 relationships; no L3 or application allocation; B2.2 not commenced. |
| [Health Information Access worked example](traceability/health-information-access-worked-example.md) | Six Function-level requires/consumes/produces traces and additive Metabolē progression; no component allocation. |
| [B1 reconciliation and validation record](reviews/b1-method-reconciliation.md) | Affected files, terminology conflicts, checks and retained questions for Mark/ChatGPT adjudication. |
| [System map](candidate-system-map.md) | Responsibility-based software hierarchy; separate collaboration map; construct standing. |
| [Component register](component-register.md) | Roles, features, significant internal constructs, information, persistence, collaborators and exclusions; [controlled nomenclature](component-register.md#authorisation-nomenclature) and [Identity/Themis model](component-register.md#c02). |
| [Candidate feature map](candidate-feature-map.md) | Documented solution features and allocations, including retained proposals and unclear owners. |
| [Context and persistence management](context-and-persistence-management.md) | Mneme services, value-based Context lifecycle, Pragma Output, exceptional harmonisation, Mnemosyne oversight, Search and Research Metadata. |
| [Data and persistence](data-and-persistence.md) | Coupled logical/application Data Objects; information-management and persistence responsibilities; Clinical / C&G / O&A responsibility domains and retained historical structure variants. |
| [Pathway, Journey and handoff](pathways-journeys-and-handoff.md) | Adjudicated Praxis/Journey ownership, control, selection, handoff, observation and assurance meanings; retained detailed uncertainty. |
| [Pragma outputs and Pathway continuation DesignNote](design-notes/DesignNote-Pragma-Outputs-and-Pathway-Continuation.md) | U18 adjudication, conceptual branching example, continuation as a role, Journey/provenance distinction and release/representation deferrals. |
| [Digital Twin Behaviour and execution semantics DesignNote](design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md) | U19/A4.7 agreed one-Ergo Behaviour Praxis, entity-relative inputs/outputs, single-input serialisation, fresh per-Behaviour Context, developer sequence policy, failure dispositions and governed administrative resume; detailed deferrals. |
| [Calliope Models and DataSets](calliope-models-and-datasets.md) | Distinct build-time definitions and runtime governed content, developer support, security/assurance nomenclature and Themis reconciliation. |
| [Pylai external interoperability and Praxis.Horos](pylai-external-interoperability.md) | Terma, transient non-recoverable Diadromí, Controller, Pathway/Broker/Server patterns, ingress acceptance and egress handover ordering. |
| [Agora / Omilia collaboration](agora-omilia-collaboration.md) | Collaboration control versus operations, native record versus conduit, roster SEP, managed-Room capture, audit and media boundaries; deliberate deferrals. |
| [Sylloge Binary Content Management](sylloge-binary-content-management.md) | Formally managed binary-content responsibility, Information Unit associations, resolution/retrieval/assembly and custody knowledge; distinct from persistence and native collaboration media. |
| [Internal middleware](middleware.md) | Internal messaging, recoverable handoff, dispatch, access and context collaboration, separated from external integration and products. |
| [Documentation/component-feature gaps](gaps.md) | Evidence, incomplete meaning and possible adjudication for each gap. |
| [Sources and reconciliation](sources.md) | Authority, legacy provenance, reviewed scope, disposition and section-specific supersession. |

The emerging Information Systems Layer model is preserved as follows:

```text
Information Systems Layer
  Enabling Capability Layer + Enterprise Capability Layer — mapping deferred
  Logical Data Layer                         ┐
  Application Feature Map                    ├ analysed together where documented
  Application Component Map                  ┘
    Data Objects / Information Responsibility / Persistence Responsibility
  Middleware Map — internal component collaboration
  Data / Persistence Layer — persistence structures, without selecting platforms
```

## Authority and interpretation

The [Architectural Axioms](../governance/architectural-axioms.md), repository
[AGENTS.md](../../../AGENTS.md), [documentation instructions](../../AGENTS.md)
and [Completion Plan](../architecture-completion-plan.md) govern. Established
RADS decisions constrain reconstruction; legacy material supplies candidate
solution meaning. A historical `[IMPLEMENTED]`, `Accepted` or `authoritative`
label is not a new Domain05 approval. Accepted ADRs retain their governance role
subject to the axioms; recording their meaning here does not revoke a decision.

Material constraints are AX-01–04 (health-information purpose, native models,
private internal semantics and technology independence), AX-05/11/12 (governed
active access, durable establishment and developer support), AX-06–09
(information authority, security, meaningful evidence and transient state),
AX-10/14/15/16 (recoverable collaboration, distinct outcomes, uncertainty and
observable entity/activity progression), AX-13 (non-destructive fail-closed
publication), and AX-17/18 (explicit architectural uncertainty and no inferred
domain authority). Each view identifies the relevant consequences. This is
reconstruction within those boundaries, not a material redesign.

Source identifiers are local documentary keys defined in [Sources](sources.md).
Register identifiers C, F, D, M and CDG support navigation only; they are not
upstream capability, requirement or Information Concept identifiers. Unknown
means the inspected documentation does not establish the detail. It does not
mean the feature cannot exist or has been rejected.

<a id="task-decisions"></a>

## Task-specific candidate decisions

The explicit 2026-10-11 task instruction establishes the following candidate
solution meanings, recorded as **U01** for traceability:

- `ActionableTaskArchetype` is an Information Architecture meta-concept, not
  itself an instantiated Information Asset. Its current candidate
  logical embodiment is `PragmaFactory`; the factory's detailed contract is unknown.
- Both `ActionableTask` and `FulfillmentTask` may be represented through `Pragma`,
  with `WorkOrder`, `ToDo`, `Stimulus` and `Effector` candidate forms. Sharing a
  representation preserves the distinct work-instance and undertaking meanings.
  The forms are not four new Information Resources. U02 below establishes
  candidate Stimulus/Effector specialisation at the application-object level;
  it does not introduce Information Architecture subclasses.
- **Ergon is an Application Architecture behavioural construct. It has no
  discrete Information Architecture representation merely by virtue of being
  an Ergon. Its information significance is expressed through the information
  resources it acts upon or generates.** No `Ergon` Information Resource is created.
- Detailed Praxis representation/lifecycle and historical definition-to-instance
  equivalences remain open. U06 below establishes its higher-order operational
  meaning and Energeia ownership. `ReportedTask` remains deferred under Domain04.

These decisions extend the recorded candidate posture; they do not claim that
legacy sources already specify them or complete Information Architecture
traceability. The [Domain04 Task model](../04-information-architecture/information-families/task-work.md)
and [Dokimasia finding](../04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md)
remain controlling for their established meanings and deferrals.

<a id="energeia-adjudication"></a>

## Energeia / DigitalTwin / Dokimasia candidate adjudication

**U02 — 2026-10-11:** the user-supplied
`DesignNote-Energeia-DigitalTwin-Dokimasia-Specialisation.md`, §§2–14, is the
primary adjudication input for this Step-A correction. Its decisions are
incorporated into these RADS views, subject to the axioms and established
upstream semantics. This records adjudicated candidate responsibilities;
it does not accept the whole Candidate Architecture or finalise detailed design.

- **Energeia** is now a discrete operational-management Software Component under
  U06 below, refining U02's earlier activity/execution subsystem description.
  **Ponos** is its associated reusable execution framework/component pattern;
  **Ergo** is the reusable
  conceptual activity pattern and platform-integration contract; **Ergon** is
  a bounded fine-grained task-completion software component; **Erga** provides
  meaningful collections for executable assembly, concurrent activity management/
  observation and coherent libraries/change management. These are distinct.
  Ponos and Ergo/Ergon are tightly coupled peer responsibilities, with execution
  and contract reuse relationships rather than Ponos containing Ergo or Ergon.
- **Mneme / Pragma Management** manages Pragma information, with logical
  Aggregation of the candidate application embodiment **PragmaFactory** and
  management of Application Data Object **Pragma**. PragmaFactory creates/initialises appropriate Pragma
  representations associated with the ActionableTaskArchetype meta-concept.
  Execution of, behaviour upon, or transport of managed information does not
  confer information-management responsibility for that information. Ponos
  executes, Petasos provides messaging/recoverable handoff, and Ergon supplies
  bounded activity behaviour; durable establishment remains with Mnemosyne.
- **DigitalTwin** is a Ponos-specialised execution component for entity-specific
  operational coordination, including the per-Twin queue pattern. Many Twins
  are supported; bounded resources service runnable pending work, while stopped
  Twins retain queued inputs pending authorised resume (U19). No thread per Twin
  is required. **Behaviour**
  specialises Ergon. **Stimulus** and **Effector** are candidate Pragma
  specialisations. [U19](#digital-twin-behaviour-adjudication) subsequently establishes
  Behaviour-specific IU and execution semantics; detailed schemas remain unresolved.
- **Dokimasia** is a Ponos-specialised execution component for independent
  governed assurance activity. **Dokimasia Ergon** specialises Ergon for bounded
  assurance activity; **AI Assurance Ergon** is a discrete candidate specialisation
  of Dokimasia Ergon where AI processing is itself an explicit governed workflow
  activity. Ordinary Ergon may instead invoke AI internally as an implementation
  choice; not all Harmonia AI use is allocated to AI Assurance Ergon.
- Dokimasia execution SHALL NOT be monitored, assured or assessed by another
  Dokimasia execution and SHALL NOT generate a separate AuditEvent for its own
  execution. Its governed assurance record/result provides authoritative evidence
  of that assurance activity. Governance, execution integrity, reporting and
  controlled observability remain required. The
  [terminal-boundary rules](component-register.md#dokimasia-terminal-boundaries)
  apply to its specialised activity components too.
- Dokimasia requires corresponding candidate Pragma specialisation semantics:
  assurance request/input and assurance result/finding are semantic roles whose
  names, schemas, cardinalities and lifecycles remain unresolved. The existence
  and discrete responsibility of AI-based Dokimasia componentry are established;
  detailed Information Systems architecture requires
  [discovery before implementation modelling is finalised](gaps.md#dokimasia-ai-discovery).
  No AI technology/provider or additional assurance subclass is selected.

<a id="specialisation-principle"></a>

### Common specialisation principle

> Specialised Harmonia execution constructs SHALL reuse the common platform
> management, assurance, reporting and integration capabilities inherited through
> their base architectural constructs. Specialisation introduces only those
> behaviours and information semantics necessary for the specialised role.

“Inherited” expresses architectural specialisation/reuse, not mandatory
programming-language inheritance or runtime containment. DigitalTwin and
Dokimasia reuse Ponos execution, Ergo/Ergon activity contracts, Mneme Pragma
Management, process-management and platform integration. Behaviour reuses
Context Loading and Context Unloading, process-management mechanisms,
Petasos/platform integration, assurance integration, logging/observability,
reporting, checkpoint/platform integration and other common Ergo/Ergon facilities.
Stimulus/Effector reuse common Pragma process-management, assurance, reporting
and management facilities. Neither specialisation establishes a parallel
messaging, context, task/progression, reporting, assurance or persistence framework;
Dokimasia's explicit terminal rules constrain common assurance/evidence hooks.

The [system map](candidate-system-map.md#specialisation-and-reuse) distinguishes
these relationships from containment and collaboration. Domain04 Task meaning,
outcome ownership and the Ergon information boundary remain unchanged.
**U06 below establishes broader Praxis meaning and ownership**; detailed
representation/lifecycle, historical definition-to-instance equivalences and
relationships to execution/activity constructs remain unresolved.

<a id="pathway-journey-adjudication"></a>

## Pathway / Journey / Publish/Subscribe / handoff candidate adjudication

**U03–U05 — 2026-10-11:** the supplied Pragma boundary, Pathway/Journey and
Mneme publication/subscription/handoff DesignNotes are incorporated into the
[shared semantic view](pathways-journeys-and-handoff.md), subject to the axioms
and Domain04 meaning. The duplicate Energeia attachment is byte-identical to
U02. These are human-adjudicated Candidate Architecture inputs for this task;
their working-note labels do not constitute whole-candidate acceptance.

- **Defined control:** every Ergon exposes ingress/egress PathwayEndpoints.
  PathwaySegment is inter-component; PathwayTraversal is intra-component and
  preserves encapsulation. Route is ordered PathwaySegment [1..N]. Pathway
  defines controlled endpoint interaction and available Routes. Only
  **Praxis → Pathway [0..1]** was established by U04; U06 now records its
  Aggregation and broader operational/assurance envelope.
- **Dynamic selection:** Publish/Subscribe is distinct from Pathway routing.
  Mneme / Pragma Management owns Publishing, Subscription and Pattern semantic
  management, eligibility, matching/selection and eligible recipient/consumer
  relationships. Petasos owns concrete destination/handoff mechanics. Publishing
  blocks provider egress on a matching filter;
  Subscription selects available Pragma for a consumer on a matching Pattern.
  Topic is governed classification/metadata, distinct from destination.
- **Handoff and execution:** Petasos persistently manages reliable handoff
  obligations. For ActionableTask processing a by-value PetasosParcel carries
  references and handoff metadata while ActionableTask/Pragma and payload remain
  through Mneme. Consumers resolve information through Mneme; undertakings
  establish distinct FulfillmentTasks. Ponos owns execution/progression and execution re-attempts;
  Petasos owns handoff redelivery/recovery/dead-letter state. U09 below establishes
  the common by-value PetasosParcel for every queue,
  with associated information predominantly by reference. Communication/Notification
  Events retain their distinct purposes; handoff is one Petasos use case.
- **Separate state:** managed Pragma state, handoff state, undertaking state and
  runtime execution state are distinct. Delivery information in Pragma records
  governed observations, without owning Petasos's operational obligation.
- **Common observation:** Journey is the canonical governed representation of
  observed progression for Pathway, Pub/Sub and mixed activity, potentially a
  graph and with no Pathway required. Reporting, analysis and
  Dokimasia assurance can operate against it. Observed evidence remains distinct
  from inferred interpretation and findings; observations are not automatically
  permanent AuditEvents. Dokimasia terminal rules remain controlling.

The Publish/Subscribe note partially settles fan-out: common Mneme-managed
Pragma/payload, potentially multiple Petasos handoffs and distinct consumer
FulfillmentTasks for ActionableTask undertakings. Detailed reference/copy/version,
identity, targetDestination, processing/concurrency, lineage and Journey contracts
remain unresolved ([CDG-37](gaps.md#pubsub-fanout)). Pattern language, definition
lifecycles, handoff representation and Journey construction/evidence/inference
contracts remain unresolved. U06 resolves Journey ownership to Energeia and
establishes semantic persistence domains without selecting technology. No Step B,
MVP,
implementation plan or final Solution Architecture baseline is established.


<a id="operational-management-adjudication"></a>

## Energeia / Praxis / Journey / persistence candidate adjudication

**U06 — 2026-10-11:** the supplied
`DesignNote-Energeia-Operational-Management-and-Praxis-Responsibility.md`, §§1–15,
and explicit bounded task decisions establish the following corrections. The task
also adjudicates Journey ownership and the revised persistence domains, which
extend the new note. Incorporated meaning is self-contained in RADS; the external
note is provenance, not a continuing architecture dependency.

- **Energeia C39** is a discrete Harmonia Software Component responsible for
  Harmonia operational management. It owns **Praxis** and **Journey**, providing
  candidate **Praxis Management Services F11** and **Journey Management Services
  F38**. Detailed Feature/Service/internal-construct/subordinate-Component
  decomposition remains unresolved. Existing execution/activity constructs stay
  associated with Energeia; Specialisation/reuse remains unchanged.
- **Praxis** establishes expected/permitted operational context and the assurance
  envelope; **Journey** represents observed progression. Praxis aggregates
  **Pathway [0..1]** and has a working Aggregation relationship to Journey, with
  cardinalities unresolved. No Composition or common lifecycle is inferred.
  Praxis can govern Publish/Subscribe activity without a Pathway.
- **Praxis is the assurance basis; Journey is the evidence subject.** Applicable
  Pathway expectations contribute through Praxis. Journey remains factual with
  respect to available observations; deterministic/AI inference and conclusions
  remain distinguishable. Dokimasia independence and terminal rules are retained.
- **R1.x/R2.x: Observe → Report.** Energeia is limited to observation and
  reporting. Evaluation, thresholding or stateful supervisory behaviour is not
  established as an intermediate R1.x/R2.x responsibility. Energeia observes Harmonia work and
  progression without autonomous adaptation. External IT/Ops manages technical
  infrastructure/platform operation. **R3 Adaptive Load and Resilience Service**
  is a deferred direction only; no policy, algorithm or control architecture is
  designed. Existing governed execution/recovery authority is not broadened by
  observation.
- **Mneme** determines semantic eligibility and eligible recipient/consumer
  relationships; **Petasos** establishes/manages concrete persistent handoff
  obligations to destinations; **Ponos** manages execution. Four distinct managed
  information, handoff, undertaking and execution states remain preserved.
- **Mnemosyne** has persistence responsibilities for **Clinical Information**,
  **Harmonia Configuration & Governance (C&G)** and **Harmonia Operations &
  Assurance (O&A)**. C&G is a coherent governance information space without
  semantic sharding into divergent authority. O&A permits horizontal partitioning
  while preserving identity, provenance, relationships and governed meaning.
  Neither distinction selects physical topology, technology, schemas or shard keys.
- Energeia application ownership, Mneme Context/Pragma management and
  Mnemosyne durable persistence responsibility remain distinct. Journey aggregates
  or references substantial information/evidence without indiscriminate duplication
  or acquiring ownership of the referenced information.

The [Praxis/Journey semantic view](pathways-journeys-and-handoff.md#praxis-operational-context),
[Energeia register](component-register.md#c39) and
[persistence domain guardrails](data-and-persistence.md#persistence-responsibility-domains)
record these meanings. Praxis lifecycle/representation, Journey construction and
reference contracts, Energeia decomposition, physical persistence/partitioning
and existing detailed fan-out uncertainty remain explicit in [CDG](gaps.md).
Potential upstream omissions require later bounded review; no Business Architecture
or capability requirements are manufactured here.

```text
A1 — Candidate reconstruction                  COMPLETE
A2 — Architectural review/adjudication         IN PROGRESS
A3 — Candidate correction                      IN PROGRESS
A4 — Candidate acceptance                      PENDING
Step B — Capability/Information traceability   NOT STARTED
```

<a id="kleio-themis-petasos-adjudication"></a>

## Kleio / Themis / Petasos candidate adjudication

**U07–U09 — 2026-10-11:** the three supplied Kleio, Themis and Petasos DesignNotes
and explicit bounded task requirements are incorporated as self-contained RADS
candidate meaning, under the axioms and Domains01–04. External notes record
provenance; required architecture does not depend on access to them.

- **[Kleio C03](component-register.md#c03)** owns authoritative Audit Information
  Management and Audit Capture. Clients report significant occurrences through
  Kleio's contract; Kleio constructs immutable FHIR AuditEvents. New linked events
  qualify/correct earlier assertions without altering them. Audit is not Governance
  or Assurance; significant user activity differs from UI telemetry. Journey is an
  audit source, not the boundary; Journey participation is never a prerequisite.
  Authoritative evidence remains distinct from prepared historical views.
- **Longitudinal evidence:** reference authoritative source information by default
  with sufficient identity/version/provenance/temporal detail; replicate only the
  necessary evidence when its audited historical state cannot reliably be retrieved.
  Kleio may consume Dokimasia outputs and supply historical audit context, preserving
  Dokimasia terminal rules and independence.
- **Auditability is essential:** Harmonia SHALL NOT perform auditable activity
  without authoritative durable capture of required audit information. If Kleio
  cannot capture it, **Harmonia is DOWN** and fails closed. R1.x/R2.x permits multiple
  independent authoritative audit persistence targets through Mneme/Mnemosyne O&A;
  one configured target able to establish the required audit information suffices.
  Cross-target audit harmonisation is **SEP**: no identical/complete histories,
  automatic convergence, reconnection reconciliation or unified history guarantee.
- **[Themis C02](component-register.md#c02)** owns the Authorisation Model through
  PAP administration, PRP authoritative repository semantics and PDP reasoning over
  model, authenticated Identity and context. ACCESS, CHANGE and FORWARD are distinct.
  PDP-derived **PEP-Context** supplies precomputed Identity-specific **PEP-Criteria**;
  Policy, Criteria and local enforcement decision remain distinct. Distributed
  Themis-owned PEP capability enforces at hosts controlling the operation; no central
  enforcement component or independent host reinterpretation is introduced.
- **Themis availability/delegation:** valid sufficient PEP-Context permits in-flight
  local enforcement during temporary PDP loss; new/renewed/replacement context
  requires PDP establishment and activity must wait at that boundary. Unavailability
  is not denial. U12 refines delegation as Identity / PEP-Criteria forwarding,
  subject to FORWARD authority and originating/acting Identity provenance;
  detailed forwarding/validity mechanics remain deferred. PRP durability may use
  Mneme/Mnemosyne C&G without transferring Authorisation Model ownership.
- **[Petasos C10](component-register.md#c10)** owns creation, operation and status of
  durable purpose-defined message/event queues. All carry **PetasosParcel by value**;
  associated governed information is predominantly by reference. Communication
  analogy does not establish literal FHIR equivalence. ActionableTask remains
  Mneme-managed; its Parcel may reference it and each undertaking establishes its
  own FulfillmentTask. Queue management stays with Petasos while Energeia observes/
  reports. Petasos hosts governed FORWARD using Themis-owned PEP/Criteria.

C&G remains one coherent governance information space; O&A remains horizontally
partitionable without a prescribed key/topology. Independent audit targets do not
establish a general O&A topology. APIs, schemas, transaction/
retry/failover, queue technology and detailed protocols remain unresolved under
[CDG-42–44](gaps.md#themis-detailed-contracts). U15 subsequently resolves the
former Themis-note audit-authorisation conflict through Kleio-specific governed
intrinsic platform capture authority under AX-07.

<a id="identity-candidate-adjudication"></a>

## Identity.Authentication / Identity.Authorisation candidate consolidation

**U12 — 2026-10-12:** the supplied Identity DesignNote, read in full outside the
repository, and explicit task adjudications are reconciled into the existing
[Calliope](component-register.md#authorisation-nomenclature) and
[Themis](component-register.md#c02) entries. These are the canonical detailed
definitions; this consolidation does not create a parallel Identity authority.

- **Authentication is SEP:** an external OIDC/OAuth IdP establishes WHO an
  Individual or System is. Keycloak + PostgreSQL SHALL be the default FOSS IdP
  for development/test/POC; it is replaceable and introduces no Keycloak-specific
  architectural dependency. Harmonia owns no identity, credential, password or
  MFA framework. Authentication grants no Harmonia authority.
- **Calliope defines/governs controlled nomenclature; Themis owns policy
  association, resolution and enforcement.** Release-bounded Function.Name,
  Function.Scope, Information.Compartments and Information.Classification
  ValueSets constrain governed combinations. ACCESS/CHANGE/FORWARD remain distinct
  existing concepts, not an exhaustive final action ValueSet.
- **Within a Criterion, applicable dimensions are conjunctive; between Criteria,
  grants are additive / UNION.** Nested collections and AuthorisationRoles
  preserve this UNION, as do direct and AuthorisationRole-derived Identity grants.
  No matching effective Criterion means DENY; no explicit negative/DENY Criteria
  or arbitrary Boolean policy language is introduced. The same model serves
  human and system Identities. Temporal validity differs from revocation.
- **PractitionerCapacity is selected operating context, not authority:** one is
  active for the applicable practitioner session/operational or roster context.
  It differs from Practitioner, FHIR PractitionerRole and AuthorisationRole.
  Operational responsibility, including future Agora notification use through Omilia,
  remains distinct from Themis authorisation. Receiving a notification grants
  no information access; following it requires normal PEP enforcement.
- **PEP-Criteria is precomputed and Identity-specific:** Themis resolves governed
  grants/context/policy state and retains derivation provenance. Local PEPs
  enforce deterministically and do not interpret a general-purpose policy language.
  Existing sufficient valid context may support local/in-flight enforcement
  during PDP loss; required new/renewed/replacement context must wait.
  Unavailability is not DENY. Forwarding requires FORWARD authority and preserves
  originating and receiving/acting Identity provenance.
- **Untagged information remains governed:** required effective Compartment and
  Classification values use deterministic governed derivation; inability to
  establish required attributes fails closed. Decision-time derivation differs
  from persistent metadata change. Themis is not assumed to own all derivation.
- **Administration is governed work:** Themis owns PAP/PRP/PDP, the model and PEP
  framework; Ergo/Praxis supplies management/review/approval activity; Iris supplies
  human interaction. Calliope defines security/assurance nomenclature, Themis
  decides/enforces, Dokimasia assures and Kleio audits.

The [deferred-design register](gaps.md#identity-deferred-design) preserves exact
representations, temporal expressions, revocation/invalidation, effective-attribute
ownership/precedence, PractitionerCapacity mapping/UI, Agora/Synapse mechanics,
PAP Praxes/Erga, forwarding lifecycle and caching/refresh/distribution. No
jCasbin/OPA/Cedar dependency, general-purpose IAM framework or detailed Iris
design is introduced. Identity is sufficiently captured for Candidate Architecture
review; A4 and whole-domain acceptance remain pending, and Step B has not started.
U12 did not resolve mandatory Kleio capture authority. The subsequent
[U15 adjudication](#boundary-reconciliation) now resolves it under AX-07.

<a id="mneme-mnemosyne-adjudication"></a>

## Mneme / Mnemosyne candidate adjudication

**U10/U11 — 2026-10-11:** the supplied Context/Pragma and Persistence Management
DesignNotes, together with explicit task decisions for Search/Research Metadata,
are incorporated into the [canonical management view](context-and-persistence-management.md).
These extend accepted candidate meanings within A2/A3; A4 acceptance is pending.

- **Mneme is the Context and Pragma Manager.** It manages the information context
  of Harmonia activity through Context and Pragma, providing controlled concurrent
  access to existing values and management of newly produced Content. Its four
  principal exposed services are **Context Load, Context Save, Adhoc Resource
  Access and Adhoc Search**. Supporting/internal capabilities create no fifth service.
- **Context is a versioned temporal load-snapshot of authoritative resource
  values**, composed into its bucket by **Composition**. Context version and
  constituent authoritative state/version are distinct; no globally transactionally
  consistent distributed snapshot is implied. The Ergo definition declares the
  **likely**, deliberately non-exhaustive resource set. Context Load is preferred;
  bounded runtime Adhoc access/search remains supported and may be more expensive.
- **Mneme Context Load/Save differ from Ergo Context Loader/Unloader.** Load
  materialises values; Loader exposes the loaded Context to business logic;
  Unloader returns bounded results across that boundary and does not persist.
  The originating Ergon SHALL NOT own persistence mechanics, shard/database
  transactions or generic conflict resolution.
- **New Content is Pragma Output**, semantically distinct from changed working
  Context values. Mneme manages both and their subsequent information consequences.
- **Resource Harmonisation Manager is internal Mneme responsibility**, expected
  for approximately **5% or less at worst**, not an SLA. It causes bounded
  Harmonisation Ergon activity as **new governed work**, through the established
  work/execution contract and common Ponos capability. It does not harmonise
  domains itself, resume inside the originating Ergon or duplicate execution.
- **Mnemosyne is the Persistence Manager**: transaction and persistent-service
  oversight/reporting, shard management, authoritative persistence reporting,
  persistence logging, compartment validation/reapplication and hosting Themis-owned
  PEP enforcement. API success alone does not establish the Harmonia outcome.
- **Search Management and Research Metadata Management are first-class Mnemosyne
  responsibilities.** Mneme Adhoc Search uses underlying authoritative persistence
  search, without assuming a complete transient cache. Mnemosyne may manage search
  capability metadata. Research Metadata captures AI-derived meaning/relationships
  based on proximity, remaining distinct from unchanged authoritative source
  Content/metadata. AI derivation executes outside Mnemosyne through an appropriate
  Harmonia behavioural/execution construct; provenance/confidence/schema remain open.
- **Energeia R1.x/R2.x remains Observe → Report.** R3 adaptive direction is deferred.
  Persistence management stays Mnemosyne's responsibility; all prior unaffected
  adjudications, including Kleio's cross-target audit harmonisation SEP, remain.

Detailed Context/concurrency/harmonisation, persistence/search and Research Metadata
contracts remain [CDG-45–48](gaps.md#context-and-pragma-contracts) under AX-17. No
technology, UI or AI implementation is selected. U15 subsequently resolves
Kleio mandatory capture authority under AX-07; no policy exception is established.

| Completion activity | Status |
| :--- | :--- |
| A1 — Candidate reconstruction/documentation | COMPLETE |
| A2 — Architectural review/adjudication | IN PROGRESS |
| A3 — Bounded correction/reconciliation | IN PROGRESS |
| A4 — Candidate acceptance | PENDING |
| Step B — Capability + Information traceability | NOT STARTED |

These corrections accept neither the whole Candidate Architecture nor a
traceability-proven Domain05 baseline. Previously adjudicated execution,
specialisation, Praxis/Pathway, Journey, selection, state and assurance boundaries
remain in force; R3 Adaptive Load and Resilience Service remains future direction.


<a id="calliope-pylai-horos-adjudication"></a>

## Calliope / Pylai / Praxis.Horos candidate consolidation

**U13/U14 — 2026-10-12:** supplied Calliope and Pylai working DesignNotes, together
with explicit current task adjudications, are consolidated into the
[Calliope](calliope-models-and-datasets.md) and
[Pylai/Horos](pylai-external-interoperability.md) views. The Pylai note predates
Broker/Server/Controller, non-recoverability and specialised EgressHoros decisions;
these additions derive from the explicit task, not absent note wording. Required
meaning is self-contained in RADS; external note location confers no authority.

- **Calliope-Models** governs Models, Metadata Types, Criteria Definitions,
  Topics, Filter Types/Elements, core Harmonia element libraries and security/
  assurance definitions through Design → Code → Build → Test → Release → Deploy.
  **Calliope-DataSets** manages mutable governed ValueSets, CodeSets, Structured
  Metadata and applicable ontology content; Mneme/Mnemosyne retains access and
  persistence and existing Ponos/Ergo/Praxis may support governed workflow.
- Definition ownership is not runtime operational ownership. Development
  representations support normal type-safe human/IDE/AI-assisted programming;
  current validity/membership uses runtime authority. Models/build contracts
  establish DataSet access structures; DataSets do not bootstrap them.
- Calliope governs shared Security and Assurance Nomenclature, including the
  U12 four release-bounded authorisation ValueSets. Themis owns policy
  association/resolution/enforcement; Dokimasia assures; Kleio audits. U12
  Identity semantics and all detailed release-content deferrals remain intact.
- **Pylai is the External Interoperability Service**, independent of communication
  style, protocol or technology. Terma acts at the logical external boundary;
  Diadromí carries/represents transient interaction. Neither Diadromí nor its
  Controller is recoverable: lost interaction ends; appropriate client retry
  starts a new Diadromí, preserving any uncertainty about earlier effects.
- **PylaiPathway** connects external interaction to governed Praxis/Ergo activity;
  ingress initial ContextLoader establishes Task.input/Context without semantic
  mapping, paired BusinessLogic produces Task.output, and Ergo.Egress feeds
  Petasos.Queue[TermaName] as Praxis.PathwaySegment. Durable Harmonia acceptance
  requires authoritative Pragma/Task persistence, distinct from protocol ACK.
- **Praxis.Horos / IngressHoros / EgressHoros** name logical Praxis boundaries.
  EgressHoros hands out to Pylai and back with communication state/outcome before
  Ergo.ContextUnloader completes. Genuine technical endpoints, FHIR Endpoint
  and established Ergon PathwayEndpoint interfaces retain their meanings.
- **PylaiBroker** serves already executing BusinessLogic without fabricating
  work/Praxis/Pathway. **PylaiServer** changes use Pathway; READ/SEARCH uses
  Diadromí-Controller and Mneme's existing Adhoc services without invented
  ActionableTasks/Praxes/Erga. Controller is not mandatory for all Diadromí.
- Pylai carries identity; Themis establishes Harmonia authority and enforcement.
  Meaningful transactions are auditable through Kleio; Journey may record
  applicable business progression/interoperability failure; Dokimasia may use
  evidence. Journey, Audit and Assurance remain distinct. U15 subsequently
  resolves the Kleio mandatory capture-authority question under AX-07.

[CDG-49/50](gaps.md#calliope-contracts) preserves detailed ontology, DataSet,
representation, UI/workflow, protocol, token, API and Horos/handover questions.
U15 below resolves the former Strategy/Terma and Calliope realisation conflicts.
These bounded components are represented for Candidate Architecture review;
this establishes neither whole-Domain05 acceptance nor Step-B authority.

<a id="boundary-reconciliation"></a>

## Terma, Calliope and Kleio boundary reconciliation

**U15 — 2026-10-12:** three explicit adjudications close the corresponding
retained conflicts and reconcile the existing candidate views:

- **Terma manages the communication stack; applicable execution manages
  application meaning.** Terma establishes, conducts and completes external
  communication, including connections/sessions, framing and communication-local
  retry. Application interpretation/processing remains with the applicable
  Harmonia execution framework, principally Praxis/Ergo/Ergon, except where an
  explicitly defined pattern provides otherwise. Retry requires the same active
  interaction and Diadromí; termination/loss permits no resumption. A later client
  retry creates both anew. Terma/Diadromí supplies no durable application recovery;
  Petasos and established execution retain durable messaging/recovery. Strategy
  G4, Component 4 and summary now express this distinction; CDG-51 is closed.
- **Calliope-Models is a development/build-time model and library dependency;
  Calliope-DataSets manages mutable governed runtime content and authoritative
  access.** Build-time representation supports type-safe development and normal
  IDE/AI assistance; current governed membership/content uses DataSets where
  runtime resolution is required. These responsibilities are complementary.
  Models' library/dependency semantics remain; DataSets uses Mneme/Mnemosyne and
  does not make Calliope a persistence manager. Strategy Component 5 / Seam 5 and
  AGENTS §2 are reconciled; CDG-49's realisation conflict is closed, its detailed
  deferrals preserved.
- **Kleio mandatory authoritative capture uses intrinsic platform authority
  associated with its audit responsibility, governed by Harmonia security policy.**
  The audited Identity remains evidence, not the authority persisting the
  mandatory AuditEvent; its PEP-Criteria is not borrowed. Themis retains policy
  evaluation. Required capture fails the governed operation closed if Kleio
  cannot sufficiently exercise that authority. AX-07 incorporates this specific
  clarification and CDG-42's audit-authorisation conflict is closed. Authority is
  not generalised to other components or other Kleio activities. Any proposed
  equivalent authority requires separate architecture adjudication.

Canonical detail: [Terma and Diadromí](pylai-external-interoperability.md#terma-and-diadromi),
[Calliope realisation](calliope-models-and-datasets.md#calliope-realisation),
[Kleio capture authority](component-register.md#kleio-capture-authority) and
[U15 provenance](sources.md#u15). Deliberate detailed-design/discovery deferrals
remain. Reconciliation authorises no software, deployment or dependency changes.
The U15 closure assessment recorded four responsibility questions without
changing their architecture. U16 below subsequently reconciles the supplied
Agora/Omilia adjudication and reassesses those questions. A4 remains pending for Mark + ChatGPT; Step B is not started.

### Boundary terminology

| Term | Meaning in this adjudication |
| :--- | :--- |
| Communication-local retry | Terma retry during the same active external interaction and Diadromí; no resumption after termination/loss. |
| Durable messaging/recovery | Established Petasos and execution responsibilities; not supplied by Terma/Diadromí. |
| Models library dependency | Development/build-time structural contracts and representations supplied by Calliope-Models. |
| Runtime dataset authority | Calliope-DataSets authority over current governed mutable content/membership; persistence remains Mneme/Mnemosyne. |
| Identity-derived authority | Authority granted to a human/system Identity and resolved/enforced through Themis. |
| Kleio intrinsic platform authority | Policy-governed authority specifically for Kleio mandatory authoritative audit capture; not inferred for other activities/components. |
| Subject of audit evidence | Originating/acting Identity recorded in evidence; distinct from the authority performing mandatory capture. |


<a id="agora-omilia-adjudication"></a>

## Agora / Omilia collaboration reconciliation and closure assessment

**U16 — 2026-10-12:** the supplied Agora/Omilia DesignNote and explicit task
adjudications are incorporated into the [canonical collaboration view](agora-omilia-collaboration.md).
**Agora controls; Omilia operates.** Agora owns native collaboration and managed
Room/Space lifecycle/membership/publication intent. Omilia.Server, derived from
Matrix Synapse, performs operations and exposes the bidirectional Application
Services API; Omilia.Client, derived from Element, provides the collaboration
client. Harmonia.Omilia is their architectural identity.

Roster Management is SEP; R1.x/R2.x needs only applicable Shift Start/End periods
as operational context. Agora controls membership intent without becoming roster
authority. Source clinical/operational/workflow information merely conveyed through
Agora retains its originating owner. Defined managed-Room interactions may be
captured/derived as new Communication Information Units and/or Pragma for governed
processing; the original Agora record remains Agora's. Not every message becomes
work or clinical record content. Dialogue retention, logging and Kleio authoritative
audit remain distinct. AX-05 and existing security/isolation rules remain unchanged.

The U16 Step-A assessment closed the former roster, Agora record-custody and
generic formal-record handoff blockers. It settles Agora-native media ownership
and the non-transfer of referenced binary ownership. **At that snapshot one A2
question remained:** the application custodian for the candidate F31/D15 formally managed
clinical binary content outside native collaboration, or its explicit deferred-scope
disposition. U17 below now closes [CDG-25](gaps.md#binary-custody-question) through
the explicit Sylloge allocation. Detailed collaboration matters remain
[non-blocking deferrals](gaps.md#agora-omilia-contracts).

| Activity | Assessment at the U16 snapshot |
| :--- | :--- |
| A1 — Candidate reconstruction/documentation | **COMPLETE** |
| A2 — Architectural review/adjudication | **INCOMPLETE** — F31/D15 binary application-custody question only |
| A3 — Candidate correction/reconciliation | **COMPLETE WITH NON-BLOCKING DEFERRED ITEMS** — supplied adjudications reconciled; remaining A2 decision may require a further bounded correction |
| A4 readiness | **NOT READY FOR A4 ADJUDICATION** — recommendation; A4 remains pending for Mark + ChatGPT |
| Step B — Capability + Information traceability | **NOT STARTED** |

This assessment does not declare Step A, Domain05 or A4 complete. It does not
assess upstream coverage or turn detailed deferred design into additional blockers.

<a id="sylloge-adjudication"></a>

## Sylloge reconciliation and repeated closure assessment

**U17 — 2026-10-12:** the explicit architectural adjudication introduces
[Harmonia.Sylloge](sylloge-binary-content-management.md) as the **Binary Content
Management subsystem**. It owns governed binary-content metadata, Information
Unit associations, location/resolution knowledge, retrieval, on-demand assembly
from one or more payloads and knowledge of Harmonia versus external management.
The system/component views include **C49**; **F31/D15** now allocate this
application responsibility. [CDG-25](gaps.md#binary-custody-question) is closed at
responsibility level. The preceding U16 assessment is a historical snapshot,
superseded for current readiness by this assessment.

Sylloge management does not imply physical custody or mandatory copying of every
payload into Harmonia. Mnemosyne remains Persistence Manager and may supply
applicable persistence services; Mneme remains Context and Pragma Manager.
Agora retains native collaboration media and the collaboration record/reference
concerning formal binaries. Sylloge retains Binary Content Management; Omilia
acquires no such responsibility. Logical information, payload, application owner,
physical custodian and persistence provider remain distinct.

The repeated assessment compares current system, component, feature, information,
middleware, source-disposition and CDG-01–52 responsibility boundaries against the
[Step-A sufficiency criterion](../architecture-completion-plan.md#33-domain05-five-stage-completion-method).
U16 already resolved CA-01–03; U17 resolves the remaining CA-04 responsibility
rather than removing binary content from the candidate. No further concrete
unresolved Candidate Architecture responsibility question was established.
Detailed Sylloge [deferrals](sylloge-binary-content-management.md#sylloge-deferred-design),
other detailed contracts, historical contradictions, explicitly unselected
proposals and future traceability questions remain distinguishable; none by
itself prevents a sufficiently faithful candidate from being reviewed at A4.

| Activity | Revised U17 assessment |
| :--- | :--- |
| A1 — Candidate reconstruction/documentation | **COMPLETE** — the adjudicated responsibility and its limits are self-contained in RADS. |
| A2 — Architectural review/adjudication | **COMPLETE** at Candidate responsibility level — the final F31/D15 question is resolved by Sylloge; no further concrete responsibility blocker established. |
| A3 — Candidate correction/reconciliation | **COMPLETE WITH NON-BLOCKING DEFERRED ITEMS** — affected views agree; detailed storage, representation, interaction and lifecycle design remains deferred. |
| A4 readiness | **READY FOR A4 ADJUDICATION** — recommendation for Mark + ChatGPT; A4 acceptance remains **PENDING**, not complete. |
| Step B — Capability + Information traceability | **NOT STARTED**. |

This assessment establishes readiness for architectural review, not A4 acceptance,
whole-Domain05 completion, upstream coverage, MVP or implementation readiness.
Stop here and return the Candidate Architecture to Mark + ChatGPT for A4 review.

<a id="pragma-continuation-adjudication"></a>

## Pragma outputs, Pathway continuation and Journey reconciliation

**U18 — 2026-10-12:** the [DesignNote](design-notes/DesignNote-Pragma-Outputs-and-Pathway-Continuation.md)
records the explicit adjudication; the [shared semantic view](pathways-journeys-and-handoff.md#pathway-continuation)
and [Context/Pragma view](context-and-persistence-management.md#pragma-output)
incorporate its meaning. Pathway defines the conceptual Ergo sequence; Praxis
defines fulfilment, manages applicable Context definitions and defines segment
criteria/outcomes and continuation semantics. The execution framework performs
output processing/routing; Praxis neither orchestrates nor choreographs.

Ergon outputs may yield multiple Pragma. The **Pathway-continuation role**
identifies information for continuing the current Praxis; other resulting Pragma
remain independently available for governed use or no further processing.
Continuation lineage supports meaningful Journey progression; **Journey lineage
is not complete Pragma provenance**. Energeia management, Ponos/Ergo execution,
Mneme Context/Pragma and semantic Pub/Sub, Petasos handoff and Mnemosyne/Kleio
boundaries remain intact. Pub/Sub-only Journeys remain valid.

Context requirements remain concrete in R1.x/R2.x. R3.x dynamic Context and
dynamically configured reusable/generic Ergo remain deferred, without a designed
mechanism or current AI-driven selection/configuration. [CDG-53](gaps.md#continuation-representation)
retains construction/designation/representation; no Pragma subtype is introduced.
[U18 source reconciliation](sources.md#u18) reported the AGENTS.md §2
“Workflow orchestration (Praxis)” wording conflict; U19 below now closes it. This bounded
correction neither accepts A4 nor advances Step B or implementation.

<a id="digital-twin-behaviour-adjudication"></a>

## Digital Twin Behaviour and execution semantics — A4.7 reconciliation

**U19 — 2026-10-12:** the
[DesignNote](design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md)
captures the agreed A4.7 decisions. Digital Twin → Behaviour [1..*] → Praxis →
Pathway → **Ergo [exactly 1]** retains genuine Praxis Context, criteria/outcomes,
fulfilment and housekeeping. Praxis is neither orchestrator nor choreographer;
the explicitly authorised AGENTS §2 correction now says Pathway fulfilment
management. Energeia and Ponos remain peer application components, with Praxis
as semantic/operational envelope rather than structural containment.

One Twin queue contains Stimulus and Effector. Interest matching creates
**BehaviourStimulus / BehaviourEffector per interested Behaviour**, analogous
in meaning to Ergon interest/FulfillmentTask without establishing type inheritance
or a shared implementation. Interested Behaviours process each input
**sequentially**, subject to the failure dispositions below, each loading fresh
Praxis-defined Context after its predecessor completes/saves on the normal path.
Interest fan-out differs from same-Ergon-type instance
ExecutionConcurrency. Concurrency is across independent Twins.

R1.x/R2.x developer configuration supplies sequencing policy through the framework
API; absent it, order is unspecified. The applicable Behaviour sequence remains
stable throughout the current input; configuration changes apply to subsequently
processed inputs. Outputs enter normal governed processing, resolving Twin-relative
references only where required; same-Twin inputs requeue without interruption.
`ContinueBehaviourOnFailure` contains failure to the current Behaviour execution;
the Behaviour remains active, other interested Behaviours continue normally and
normal queue processing continues. `StopBehaviourOnFailure` stops the failing
Behaviour immediately, permits remaining interested Behaviours to complete the
current input, then halts the Twin. `StopDigitalTwinOnFailure` terminates the
failing Behaviour and halts the Twin immediately; no remaining Behaviour processes
the current input and no subsequent queued input is processed.
A stopped Twin retains its queue pending authorised human-initiated Administrative
Function → specialised Administrative Ergon → governed Twin resume. Iris/Iris-BackEnd
and Omilia/Agora convey intent through separate paths; neither directly resumes
the Twin and Iris has no direct Agora/Omilia interaction.

Affected system, component, feature, data, Context, Pathway/handoff, middleware and
collaboration views are reconciled. [U19 provenance](sources.md#u19) records the
review and corrected contradictions. [CDG-54](gaps.md#digital-twin-execution-contracts)
retains detailed runtime/IU/API/reference-resolution and future sequencing/failure
questions, including uncertain-save handling and recovery of a stopped Behaviour.
[CDG-55](gaps.md#administrative-function-finding) flags broader administration
reconciliation without designing a catalogue or recovery console.

**Documentation readiness: ready for Mark/ChatGPT A4.7 architectural adjudication.**
This recommendation is limited to capture/reconciliation of the supplied decisions;
it declares **no A4.7 PASS**, candidate acceptance, Step-B commencement or
implementation readiness. Existing detailed deferrals remain explicit.
