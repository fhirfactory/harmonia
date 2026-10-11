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

**Standing: Harmonia Candidate Information Systems Architecture, reconstructed
and corrected through bounded Step-A adjudication, 2026-10-11.** This is the
canonical RADS record of the candidate,
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

Initial reconstruction (A1, recorded here as Step 1) is complete. Step A remains
open: architectural review/adjudication (A2) and bounded iterative correction
(A3) are in progress; acceptance as sufficiently faithful (A4) is pending.
The Energeia / DigitalTwin / Dokimasia and Pathway / Journey / Publish/Subscribe /
handoff and Energeia / Praxis / Journey / persistence adjudications below correct
the candidate within that boundary. The Kleio / Themis / Petasos adjudication
below further corrects the candidate and R1.x/R2.x Observe → Report scope.
The [Mneme / Mnemosyne correction](#mneme-mnemosyne-adjudication) incorporates
Context/Pragma Management and Persistence/Search/Research Metadata Management.
It retains the same A2/A3 boundary; **Step B has NOT started**.
Capability mapping, Information Architecture coverage, upstream requirements
gap analysis, excess-feature analysis, MVP
determination, final baselining and implementation planning remain future work.
The [documentation gap register](gaps.md) records insufficiencies within the
documented candidate, not unsupported upstream requirements. This initial
record does not declare the Domain Completion Gate satisfied.

## Read the candidate

| View | Contents |
| :--- | :--- |
| [System map](candidate-system-map.md) | Responsibility-based software hierarchy; separate collaboration map; construct standing. |
| [Component register](component-register.md) | Roles, features, significant internal constructs, information, persistence, collaborators and exclusions. |
| [Candidate feature map](candidate-feature-map.md) | Documented solution features and allocations, including retained proposals and unclear owners. |
| [Context and persistence management](context-and-persistence-management.md) | Mneme services, value-based Context lifecycle, Pragma Output, exceptional harmonisation, Mnemosyne oversight, Search and Research Metadata. |
| [Data and persistence](data-and-persistence.md) | Coupled logical/application Data Objects; information-management and persistence responsibilities; Clinical / C&G / O&A responsibility domains and retained historical structure variants. |
| [Pathway, Journey and handoff](pathways-journeys-and-handoff.md) | Adjudicated Praxis/Journey ownership, control, selection, handoff, observation and assurance meanings; retained detailed uncertainty. |
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
  are supported; only Twins with pending work become active and are serviced by
  bounded execution resources. No thread per Twin is required. **Behaviour**
  specialises Ergon. **Stimulus** and **Effector** are candidate Pragma
  specialisations, with detailed information semantics unresolved.
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
  PDP-derived **PEP-Context** projects applicable Criteria for authenticated Identity;
  Policy, Criteria and local enforcement decision remain distinct. Distributed
  Themis-owned PEP capability enforces at hosts controlling the operation; no central
  enforcement component or independent host reinterpretation is introduced.
- **Themis availability/delegation:** valid sufficient PEP-Context permits in-flight
  local enforcement during temporary PDP loss; new/renewed/replacement context
  requires PDP establishment and activity must wait at that boundary. Unavailability
  is not denial. Context may be delegated within Themis with source provenance;
  detailed delegation/validity mechanics remain deferred. PRP durability may use
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
establish a general O&A topology. APIs, schemas, policy language, transaction/
retry/failover, queue technology and detailed protocols remain unresolved under
[CDG-42–44](gaps.md#themis-detailed-contracts). The Themis note's proposed audit
authorisation exception conflicts with AX-07 and is explicitly retained for
adjudication; it is not adopted as an authorisation bypass.

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
technology, UI or AI implementation is selected. The Themis/AX-07 audit-authorisation
conflict is retained unchanged; no exception is adopted.

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
