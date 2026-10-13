# Harmonia Architecture Design Notes

> **Standing:** Architectural scratchpad / working memory only.
>
> This document captures design discussion, emerging decisions, semantic
> distinctions, rationale, interpretive warnings, and unresolved
> questions that may otherwise be lost between working sessions. It is
> **not architectural authority**.
>
> Canonical architecture remains in the applicable authoritative
> Markdown documents under `/docs/markdown`. Material recorded here
> becomes authoritative only when it is explicitly adjudicated and
> incorporated into the appropriate canonical architecture source.
>
> Scratchpad material MUST NOT be used to silently resolve uncertainty,
> establish identifiers, allocate ancestry, create traceability
> relationships, or override canonical architecture.

## Purpose

This scratchpad exists to preserve the architectural reasoning that sits
between conversational discussion and formal architectural
documentation.

In particular, capture material where retaining only the final decision
would lose an important semantic boundary or make a later
misinterpretation likely.

Useful scratchpad material includes:

-   candidate architectural decisions;
-   intended meanings of ambiguous terms;
-   semantic distinctions;
-   responsibility boundaries;
-   rationale for placement or decomposition;
-   interpretive warnings;
-   deliberately rejected interpretations;
-   unresolved questions;
-   assumptions that still require adjudication; and
-   observations that should influence later canonical documentation.

A successful build, implementation pattern, catalogue shape, or
convenient structural mapping does not promote scratchpad material into
architectural authority.

## Working Practice

During Harmonia design discussions, record a note when a decision,
distinction, boundary, rationale, warning, or unresolved issue could
reasonably be lost through conversation compaction or a later session
restart.

Where useful, notes should distinguish:

-   **Context** --- what part of the architecture is being discussed.
-   **Working Decision** --- what has presently been agreed.
-   **Intended Meaning** --- what the term or construct is intended to
    mean.
-   **Boundary** --- what it explicitly does not mean or own.
-   **Reasoning** --- why the decision was made.
-   **Interpretive Warning** --- likely future misreadings that should
    be prevented.
-   **Open Issue** --- anything deliberately left unresolved.
-   **Promotion Target** --- likely canonical document(s), if known.

Scratchpad notes do not need every heading where the meaning is obvious.

------------------------------------------------------------------------

# Business Capability MVP Reconciliation

## DN-BC-001 --- MVP Business Capability Modelling Scope

**Context:** Business Capability CT1 → CT2 → CT3 reconciliation.

**Working Decision:**

The Business Capability model is being developed as the minimum coherent
business architecture necessary to establish Harmonia's architectural
context and justify its Business Enabling Capabilities.

It is not intended to become an exhaustive Enterprise Health Capability
Model.

Capability decomposition should therefore be established where a
distinction is necessary to explain, derive, constrain, or govern
Harmonia responsibilities, or where sufficient adjacent business context
is required to make those responsibilities intelligible.

**Working test:**

> Would distinguishing these business abilities cause Harmonia to
> behave, govern, represent, exchange, secure, coordinate, or trace
> something materially differently?

If yes, further Capability distinction may be justified.

If no, additional decomposition is probably unnecessary explanatory
enterprise detail.

**Boundary:**

The established Business Enabling Capability landscape may be used as
evidence that business context is required. It MUST NOT be used to
mechanically reverse-engineer Business Capability identities or create
one-to-one Business Capability ↔ Business Enabling Capability mappings.

------------------------------------------------------------------------

## DN-BC-002 --- Fixed Capability Decomposition and Incomplete Branches

**Context:** Capability metamodel.

**Working Decision:**

Harmonia uses the fixed decomposition:

`CT1 → CT2 → CT3 → Feature (FT)`

A Feature must be framed by a meaningful CT3 Capability. CT3 must have a
CT2 parent and CT2 must have a CT1 parent.

A Feature cannot compensate for incomplete Capability decomposition.

If Harmonia requires a Feature and a meaningful CT3 cannot yet be
identified, this indicates incomplete upstream Capability modelling. It
is not permission to skip CT3 or manufacture a meaningless CT3 for
structural symmetry.

**Interpretive Warning:**

A CT1 or CT2 branch may remain intentionally undecomposed in the MVP
where no Feature currently requires a lower frame. This does not relax
the fixed hierarchy for future Features.

------------------------------------------------------------------------

## DN-BC-003 --- Individual Health Assessment CT3 Decomposition

**Context:**

`Individual Care Delivery → Individual Health Assessment`

**Working Decision:**

Individual Health Assessment decomposes according to the distinct means
by which knowledge about the individual's health state is established,
not according to care setting, profession, clinical specialty, workflow,
or information type.

Current agreed CT3s:

-   Health History Assessment
-   Clinical Examination
-   Health Status Measurement

**Boundary:**

Functional, nutritional, cognitive, developmental, psychosocial,
mental-health, frailty and similar assessment labels are generally
assessment domains rather than distinct means of establishing knowledge.

Health Risk Assessment crosses into Clinical Determination where
evidence is interpreted into a clinically meaningful conclusion.

------------------------------------------------------------------------

## DN-BC-004 --- Care Access & Coordination MVP Decomposition

**Context:** Review of the candidate Business Capability decomposition
against established Harmonia Business Enabling responsibilities.

**Working Decision:**

The expanded Care Access & Coordination structure proposed during review
is accepted as a useful MVP business frame, subject to the definitions
and boundaries recorded below.

Current working structure:

``` text
Care Access & Coordination

  Care Access Management
    Care Intake Management
    Care Referral Management
    Care Eligibility Determination

  Care Scheduling & Capacity Management
    Care Appointment Management
    Care-Place Allocation
    Care-Place Readiness Management
    Care Resource Deployment
    Operational Care Flow Coordination
    Operational Care Capacity Management

  Care Coordination & Transition
    Care-Team Coordination
    Care Responsibility Transition
    Care Discharge Coordination

  Care Support Logistics Management
    Patient Movement Coordination
    Clinical Material Logistics Coordination
    Care Support Work Coordination
```

**Reasoning:**

The original scheduling/allocation model was too thin to explain
established Harmonia-relevant business context around operational flow,
care-place readiness, service capacity, roster/resource deployment, work
coordination, patient transport, and clinical-material logistics.

The additional structure is intended to provide sufficient business
context without turning Harmonia into a PAS, workforce-management
system, bed-management product, or hospital logistics platform.

------------------------------------------------------------------------

## DN-BC-005 --- Care Resource Deployment Means Roster-Supported Operational Deployment

**Context:**

`Care Access & Coordination → Care Scheduling & Capacity Management → Care Resource Deployment`

**Working Decision:**

Retain **Care Resource Deployment** as a CT3 beneath **Care Scheduling &
Capacity Management**.

**Intended Meaning:**

Care Resource Deployment concerns the operational deployment of
available and appropriately authorised resources to meet care demand.

A particularly important use is **roster support**: the intersection of
roster/on-call context, availability, role/qualification, resource
suitability, and the operational care requirement.

It answers, in business terms:

> Of the resources legitimately available to provide coverage, which
> resources are operationally deployed to meet this care requirement?

**Boundary:**

Care Resource Deployment does **not** establish Harmonia as:

-   the authoritative rostering system;
-   the workforce-management system;
-   the authority that establishes workforce positions or employment;
-   the authority that determines clinical work;
-   a hospital-wide resource-logistics system; or
-   the authoritative source of all resource availability.

**Related distinction:**

-   **Workforce Service Coverage Management** establishes
    roster/on-call/service-coverage context: *who is rostered, on-call,
    or otherwise available to provide coverage?*
-   **Care Resource Deployment** applies available and authorised
    resource context to operational care demand: *who or what is
    actually deployed to support this care requirement?*
-   **Care Support Work Coordination** coordinates the resulting
    operational support work: *what work needs doing, by whom, and how
    is its progression/completion coordinated?*

These responsibilities may consume overlapping information but are not
interchangeable.

**Interpretive Warning:**

The name **Care Resource Deployment** can reasonably be read more
broadly than its intended Harmonia meaning.

Future architecture MUST NOT infer responsibility for hospital-wide
resource logistics merely from the Capability name. Its roster-support
and care-demand context must be retained explicitly in the canonical
Capability definition when this working decision is promoted.

------------------------------------------------------------------------

## DN-BC-006 --- Care Capacity, Resource Readiness and Resource Deployment Are Distinct

**Context:** Care Scheduling & Capacity Management.

**Working Decision:**

Operational care capacity, resource readiness/availability, and resource
deployment are related but semantically distinct.

-   **Operational Care Capacity Management** concerns the ability of a
    service to accommodate care in the current operational context.
-   **Care-Place Readiness Management** concerns whether a care place is
    actually ready for allocation or use.
-   **Care Resource Deployment** concerns application of
    available/authorised resources to care demand.

Possession of available staff, equipment, or an unoccupied bed does not
by itself establish service capacity.

Likewise, service capacity does not prove that a particular resource has
been deployed.

**Boundary:**

Operational service capacity is distinct from strategic or regional
capacity planning.

------------------------------------------------------------------------

## DN-BC-007 --- Physical Patient Movement Is Not Transfer of Care Responsibility

**Context:** Care Access & Coordination.

**Working Decision:**

Physical movement of a patient and transition of care responsibility are
separate business meanings.

-   **Patient Movement Coordination** concerns physical transit and
    applicable mobility constraints.
-   **Care Responsibility Transition** concerns
    establishment/coordination of who has actually assumed applicable
    care responsibility.

**Semantic guardrail:**

> Patient moved ≠ care responsibility transferred.

Arrival at a destination, referral submission, message receipt,
discharge readiness, or physical departure does not by itself establish
that another clinician, team, or service has accepted care
responsibility.

This distinction must be preserved in information semantics, workflow
interpretation, traceability, and any later implementation allocation.

------------------------------------------------------------------------

## DN-BC-008 --- Care Support Logistics Is Bounded Operational Context

**Context:**

`Care Access & Coordination → Care Support Logistics Management`

**Working Decision:**

Care Support Logistics Management is retained as a CT2 containing:

-   Patient Movement Coordination
-   Clinical Material Logistics Coordination
-   Care Support Work Coordination

**Intended Meaning:**

The branch supplies business context for operational support necessary
to enable care access and fulfilment, including physical patient
movement, clinically relevant material movement/custody, and authorised
support work.

**Boundary:**

It does not establish a general hospital logistics, supply-chain, ERP,
facilities-management, or workforce-management architecture.

Clinical Material Logistics Coordination should remain concerned with
materials whose movement, custody, availability, or disposition is
materially relevant to care or diagnostic fulfilment.

Care Support Work Coordination must not be interpreted as authority to
determine clinical work.

------------------------------------------------------------------------

# Open Adjudication Queue

The Codex MVP Business Capability decomposition review remains a
candidate recommendation. After Care Access & Coordination, material
review areas still include:

1.  Clinical decomposition and the proposed Therapy & Counselling
    Intervention residual frame.
2.  Care-plan coordination boundaries.
3.  Medication/support boundaries.
4.  Information rights, disclosure consent, discovery/retrieval, sharing
    and exchange.
5.  Communication, notification, collaboration and the existing
    clinical-handover semantic conflict.
6.  Device attribution versus readiness/deployment.
7.  Governance, clinical quality/safety, and independent assurance.
8.  Final scope/pruning and global Capability-name uniqueness.
9.  Reconciliation of the approved Business Capability tree into
    canonical Markdown and RADS.
10. Business Enabling CT ancestry and Feature framing remain separate
    work.
11. Domain05 B2.2 remains parked until Capability/RADS reconciliation is
    sufficiently complete.

------------------------------------------------------------------------

# Promotion / Reconciliation Reminder

Before material from this scratchpad is promoted:

1.  Re-read the applicable canonical Markdown.
2.  Confirm that the working decision has actually been adjudicated.
3.  Preserve unresolved uncertainty rather than silently filling gaps.
4.  Incorporate the decision, definition, boundary and any material
    interpretive warning into the appropriate canonical source.
5.  Update RADS only as part of an explicitly authorised semantic
    reconciliation.
6.  Do not allocate Canonical IDs merely because the conceptual tree has
    been agreed.
7.  Do not infer Business Capability ↔ Business Enabling Capability
    relationships from contextual justification alone.
8.  Do not resume Domain05 traceability merely because a candidate
    Capability name exists.

Once promoted, this scratchpad may retain the note as historical
reasoning, but the canonical Markdown becomes the architectural
authority.
