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
handoff adjudications below correct the candidate within that boundary.
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
| [Data and persistence](data-and-persistence.md) | Coupled logical/application Data Objects; information-management and persistence responsibilities; competing persistence structures. |
| [Pathway, Journey and handoff](pathways-journeys-and-handoff.md) | Adjudicated control, selection, handoff, execution, observation and assurance meanings; retained detailed uncertainty. |
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
- Praxis definition/composition/instance questions remain open wherever source
  meanings conflict. `ReportedTask` remains deferred under Domain04.

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

- **Energeia** is the activity/execution architectural subsystem. **Ponos** is
  its reusable execution framework/component pattern; **Ergo** is the reusable
  conceptual activity pattern and platform-integration contract; **Ergon** is
  a bounded fine-grained task-completion software component; **Erga** provides
  meaningful collections for executable assembly, concurrent activity management/
  observation and coherent libraries/change management. These are distinct.
  Ponos and Ergo/Ergon are tightly coupled peer responsibilities, with execution
  and contract reuse relationships rather than Ponos containing Ergo or Ergon.
- **Mneme / Pragma Management** manages Pragma information, containing the
  candidate logical application embodiment **PragmaFactory** and Application
  Data Object **Pragma**. PragmaFactory creates/initialises appropriate Pragma
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
**Praxis requires separate architectural adjudication**; this correction supplies
no definition-to-instance, workflow or composition model for it.

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
  **Praxis → Pathway [0..1]** is established; broader Praxis remains unresolved.
- **Dynamic selection:** Publish/Subscribe is distinct from Pathway routing.
  Mneme / Pragma Management owns Publishing, Subscription and Pattern semantic
  management, eligibility, matching/selection and eligible consumer/destination
  relationships. Publishing blocks provider egress on a matching filter;
  Subscription selects available Pragma for a consumer on a matching Pattern.
  Topic is governed classification/metadata, distinct from destination.
- **Handoff and execution:** Petasos persistently manages reliable handoff
  obligations. For Pragma processing it manages identity/reference and handoff
  metadata while the managed payload remains through Mneme. Consumers resolve
  information through Mneme; ActionableTask undertakings establish distinct
  FulfillmentTasks. Ponos owns execution/progression and execution re-attempts;
  Petasos owns handoff redelivery/recovery/dead-letter state. Petasos also
  supports Communication/Notification Events and other messaging forms; no
  universal reference-only format is established.
- **Separate state:** managed Pragma state, handoff state, undertaking state and
  runtime execution state are distinct. Delivery information in Pragma records
  governed observations, without owning Petasos's operational obligation.
- **Common observation:** Journey is the canonical governed representation of
  observed progression for Pathway, Pub/Sub and mixed activity, potentially a
  graph and with no Pathway required. Reporting, monitoring, analysis and
  Dokimasia assurance can operate against it. Observed evidence remains distinct
  from inferred interpretation and findings; observations are not automatically
  permanent AuditEvents. Dokimasia terminal rules remain controlling.

The Publish/Subscribe note partially settles fan-out: common Mneme-managed
Pragma/payload, potentially multiple Petasos handoffs and distinct consumer
FulfillmentTasks for ActionableTask undertakings. Detailed reference/copy/version,
identity, targetDestination, processing/concurrency, lineage and Journey contracts
remain unresolved ([CDG-37](gaps.md#pubsub-fanout)). Pattern language, definition
lifecycles, handoff representation, Journey construction/management ownership,
evidence/inference schema and technology remain unresolved. No Step B, MVP,
implementation plan or final Solution Architecture baseline is established.
