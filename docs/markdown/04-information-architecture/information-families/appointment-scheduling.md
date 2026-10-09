# Appointment / Scheduling Information Family

**Status: G2 CANDIDATE — Block 1; G2-D02 approved; remaining propositions await review. Not CLOSED or FROZEN.**

[G2 review standing A–D](../reviews/package2-g2-block1-review.md#2-review-standing) distinguishes derivation strength from acceptance. [G1 remains CLOSED](../reviews/package2-g1-review.md#16-controlled-g1-reconciliation-and-closure).

## 1. Purpose & Semantic Boundary

Scheduling Administration requires Harmonia to understand appointment notifications, synchronise changes and expose consolidated patient schedules. It does not require Harmonia to originate authoritative bookings. The [approved behaviour](../../03-business-architecture/behaviours/02-service-administration.md#4-scheduling-administration) explicitly retains host PAS/departmental scheduling authority. [Approved G2-D02](../reviews/package2-g2-block1-review.md#g2-d02) additionally establishes that an Appointment may provide prospective scheduling context for an Encounter or be created retrospectively for an Encounter without a pre-existing Appointment.

```text
Scheduling need ≠ proposal ≠ authoritative allocation ≠ received scheduling information
Authoritative booking state ≠ Harmonia's knowledge/synchronisation of that state
Appointment ≠ Encounter ≠ HealthcareServiceDelivery
Appointment existence does not imply Encounter occurred
Encounter does not prove prospective Appointment existed
```

These distinctions identify meanings that must not be conflated when supplied; they do not establish separate prospective and retrospective Appointment Concepts. A retrospective Appointment represents scheduling/administrative context associated with an unscheduled Encounter, such as an emergency or walk-in interaction; it is not evidence of prospective scheduling. Independent scheduling-request/proposal responsibility, booking negotiation and scheduler authority transfer remain unestablished.

## 2. Domain 03 Responsibility & Traceability

Use [Strategy FEAT-SA-06–07](../../02-strategy/capabilities/business-enabling-capabilities.md#3-scheduling-administration-harmonia-relevant), [Scheduling behaviour](../../03-business-architecture/behaviours/02-service-administration.md#4-scheduling-administration), and [Appointment Synchronisation State](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix). Appointment Event History and Schedule Synchronisation State are supporting responsibility wording, not calendar/slot models.

[Service Response and Service Coordination](../../03-business-architecture/collaborations-interactions/interactions.md#22-service-delivery-interactions) supply the interaction vocabulary for scheduling communications. Their applicability to notifications/coordination does not allocate a new Service dependency or require a [Business Collaboration](../../03-business-architecture/collaborations-interactions/collaborations.md#41-collaboration-existence-threshold). Referral and Encounter Processes provide local uses of allocated scheduling context; neither is a standalone scheduling process.

Keys below are local references, not Canonical IDs. Established Scheduling Administration ownership suffices; Tier and ancestry remain unresolved under G1 K9.

| Requirement | Owner / established Feature → Function / exposed Service | Interaction / local context → information need → candidate semantics |
| :--- | :--- | :--- |
| AP-IR1 | Scheduling Administration / FEAT-SA-06 Appointment Notification Ingestion → Receive Appointment Notification / Appointment Notification Ingress | Service Response / supplied notification → recognise the scheduled arrangement, its subject, originating scheduler and reported change → proposed Appointment; Scheduling Change Event. Need arises from booking notifications, not a standard resource shape. |
| AP-IR2 | Scheduling Administration / FEAT-SA-07 Appointment Status Synchronization → Synchronise Appointment Status / Appointment Schedule Query | Clinical Information Provision/Notification and Service Coordination → distinguish received source milestones from the state Harmonia has synchronised and made available → Schedule Synchronisation State; proposed Patient Appointment Schedule View. |
| AP-IR3 | Referral Administration → Referral Progression Process, Scheduled checkpoint; Episode & Encounter Administration → Encounter Lifecycle Process, Planned / Booked checkpoint | Locally allocated appointment and subject notification / planned elective admission or clinic appointment → preserve the particular scheduling association where identified → conditional Referral–Appointment and Appointment–Encounter relationships. This does not make Scheduling Administration owner of either other family. |

AP-IR3 traces to the [Referral](../../03-business-architecture/processes/business-processes.md#33-referral-progression-process) and [Encounter](../../03-business-architecture/processes/business-processes.md#34-encounter-lifecycle-process) Processes. G2-D02 now establishes the additional retrospective scheduling-context possibility; it is not inferred from those prospective Process examples. Neither trace creates an additional Feature association or universal stage correspondence.

## 3. Principal Information Concepts

### 3.1 Appointment — G2-D02 approved meaning; remaining classification/qualification B/C

- **Semantic Classification**: Assertion / Scheduling Contextual Binding — metamodel classification remains proposed for review; prospective/retrospective scheduling meaning and distinction from Encounter are approved under G2-D02.
- **Definition**: An identifiable scheduling/administrative context for a healthcare subject's intended or actual care interaction. An Appointment may provide prospective scheduling context for an Encounter or be created retrospectively to represent that context for an Encounter without a pre-existing Appointment.
- **Necessity**: AP-IR1 requires creation, rescheduling, cancellation and check-in notifications to concern a recognisable arrangement; AP-IR2 requires a schedule of those arrangements. Identity is scoped to the source authority, not presumed globally unique merely from a booking identifier.
- **Boundary**: Appointment and Encounter are distinct. Appointment existence does not prove an Encounter occurred; an Encounter does not prove a prospective Appointment existed. Retrospective creation must not be interpreted as evidence of prospective scheduling. No separate prospective/retrospective Concepts or Appointment → Encounter cardinality are established. Remaining source/context qualification and metamodel classification are [G2-Q02](../reviews/package2-g2-block1-review.md#g2-q02); Harmonia does not become the booking authority by representing this context.

### 3.2 Scheduling Change Event — A

- **Semantic Classification**: Event.
- **Definition**: Information reporting a material appointment milestone/change, such as creation, rescheduling, cancellation, check-in or attendance, with its source and scheduling context.
- **Necessity**: AP-IR1 and AP-IR2 explicitly require notification ingestion, event history and propagation. A source change and Harmonia's receipt of its notification are distinguishable occurrences.
- **Boundary**: Check-in and attendance retain their reported meanings; neither universally establishes Encounter creation, clinical review or successful care delivery. No compulsory booking → check-in → attendance sequence is inferred.

### 3.3 Schedule Synchronisation State — A

- **Semantic Classification**: Assertion / Derived Knowledge State.
- **Definition**: The attributable state of what appointment information Harmonia has received, correlated and synchronised for provision to consumers, relative to identified scheduling sources.
- **Necessity**: AP-IR2 and the ownership matrix explicitly require synchronisation state. It explains what Harmonia knows rather than making its last received notification the scheduler's current truth.
- **Boundary**: This is semantic knowledge/derivation, not a cache model, version token or replication design. No complete synchronisation, latest-source-state claim or universal stale-state policy may be inferred without evidence. Missing information remains unknown rather than an authoritative cancellation or absence.

### 3.4 Patient Appointment Schedule View — B

- **Semantic Classification**: Information View, using the [existing Assembly/View distinction](../assemblies-views/assemblies-and-views.md#1-information-assembly-vs-information-view).
- **Definition**: A subject-oriented projection of appointment information across facilities, with source qualifications and the state of Harmonia's knowledge retained.
- **Necessity**: AP-IR2's Appointment Schedule Query explicitly exposes consolidated patient schedules. Naming a semantic View is proposed; precise inclusion, temporal horizon and composition semantics require [G2-Q06](../reviews/package2-g2-block1-review.md#g2-q06).
- **Boundary**: Consolidation establishes neither one authoritative enterprise calendar nor booking ownership. The View does not acquire the originating authority of its constituent information.

## 4. Key Information Relationships

Use [Source / Target / Type / Qualification / Properties](../patterns/information-relationships.md), preserving source-specific scheduling authority.

| Source → Target | Type / Qualification | Meaning-bearing properties | Standing / applicability |
| :--- | :--- | :--- | :--- |
| Appointment → Healthcare Subject Context | Scheduling/administrative context concerns subject | Source identification, correlation and scheduling context | A subject meaning; scheduling scope approved by G2-D02, remaining qualification under G2-Q02. Reuse [Package1 subject semantics](person-healthcare-subject.md). |
| Appointment → reported service/provider/location context | Scheduling care/service context | Allocated time where supplied and source qualifications; reuse Package1 Concepts only when identifiable | Contextual; no mandatory provider/location/participant tuple is established. Exact required detail is C under G2-Q02, not imported from a scheduling product. |
| Scheduling Change Event → Appointment | Reports scheduling milestone/change | Source booking reference, source assertion/occurrence time and received time; changed scheduled period where supplied | A — required correlation meaning; Appointment's precise boundary remains B. Cancellation is source-reported, not inferred from silence. |
| Schedule Synchronisation State → source appointment information / Scheduling Change Event | Derives knowledge from received evidence | Identified sources, correlation and derivation/recorded time; uncertainty about unsupplied source changes | A — required; never substitutes for externally authoritative scheduling state. |
| Patient Appointment Schedule View → Appointment information / Schedule Synchronisation State | Projects subject schedule | Inclusion purpose, constituent sources and projection time/context | B — G2-Q06. Inclusion is not physical containment or appointment ownership; no separate Collection is required. |
| Appointment → Encounter | Prospective or retrospectively established scheduling/administrative context | Explicit source correlation; proposed qualification for later review identifying prospective versus retrospective establishment | APPROVED — G2-D02: contextual association, not mandatory lifecycle progression. No Appointment → Encounter cardinality established. Appointment existence does not imply Encounter occurred; Encounter does not prove prospective Appointment existed. |

## 5. Authority, Responsibility & Provenance

| Concept | Originating authority | Harmonia responsibility | Provenance characteristics |
| :--- | :--- | :--- | :--- |
| Appointment | Host PAS or departmental scheduler for authoritative allocation/booking state; G2-D02 does not assign an originator or authority for retrospective establishment | Scheduling Administration consumes/references scheduling context and manages a governed representation for synchronisation/provision; retrospective context does not confer booking authority | Source authority, source-scoped identity, origin versus relay, scheduling context and alteration lineage; prospective/retrospective qualification is proposed for later review, not an invented originator or eligibility rule. |
| Scheduling Change Event | Source reporting the milestone; source actor may differ from scheduler for attendance evidence | Ingest, correlate, preserve event history and propagate reported milestones | Reporter, scheduler/source reference, reported occurrence/assertion versus receipt, affected appointment and change/correction context. |
| Schedule Synchronisation State | Harmonia for its own evidenced synchronisation/derivation facts only | Own/govern synchronisation knowledge under Scheduling Administration | Source evidence used, derivation/correlation context and recorded time; uncertainty or conflict remains explicit without an invented resolution rule. |
| Patient Appointment Schedule View | Constituent source authorities; Scheduling Administration for projection only | Assemble/expose the consolidated subject schedule with access qualification | Composition/inclusion basis, constituent references and projection time. Neither presentation nor aggregation transfers booking authority. |

## 6. Temporal & Lifecycle Characteristics

Allocated/scheduled time or period is required to understand a prospective appointment schedule. Source change/occurrence time and received/recorded time are required to distinguish rescheduling from delayed notification. Attendance/check-in occurrence time matters only where that milestone is supplied; scheduled time is never actual encounter or delivery time by assumption.

G2-D02 permits retrospective Appointment establishment after an unscheduled Encounter. The fact of retrospective establishment must remain distinguishable from prospective scheduling. A scheduling-origin/context qualification, potentially supported by context-establishment time and Encounter occurrence evidence, is identified for later review under G2-Q02; this task defines no property structure, mandatory temporal bundle or new Concept. Appointment → Encounter multiplicity remains unresolved, including cancellation, rescheduling, recurring, group and other scheduling contexts.

Requested periods and proposed periods must be distinguished from allocations if reported, but upstream architecture does not establish their detailed semantics or a standalone requested/proposed scheduling Concept. They remain C, not implied steps preceding every booking.

An Appointment's source booking lifecycle, the scheduling-event history, Harmonia synchronisation progression and an Encounter's real-world/admin progression differ. No universal recurrence, missed-appointment disposition, calendar horizon or status transition model is derived. Foundation provenance/correction rules apply to managed information; they do not authorise Harmonia to alter the host scheduler's truth.

## 7. Assembly Participation & Other Families

The proposed View directly supports Appointment Schedule Query. It does not require a persistent appointment Collection or a new scheduler-owned assembly. Existing Service Provision Context may explain an identified service's availability; service availability is not a booked appointment, calendar or clinic slot model.

Referral–Appointment and Order–Appointment associations remain conditional on their evidenced local pathways, including the Order Process's imaging scheduling checkpoint. Appointment–Encounter scheduling-context association is now approved by G2-D02 for both prospective context and retrospective establishment after an unscheduled Encounter; an Appointment is not a real-world prerequisite to every Encounter. An operational clinic queue may consume booking/check-in information ([Clinic / Operational Queue Process](../../03-business-architecture/processes/business-processes.md#48-clinic--operational-queue-progression-process)); its arrival/readiness progression remains operational information, not an Appointment lifecycle. The [cross-family review](../reviews/package2-g2-block1-review.md#4-cross-family-semantic-analysis) qualifies these relationships.

## 8. Explicit Boundaries, Unresolved Semantics & Downstream Realisation

No scheduling engine, slot, calendar, recurrence structure, booking algorithm or negotiation workflow is introduced. An Appointment neither requires Referral nor produces Encounter or delivery by itself. No exact source-to-Harmonia state conflict/freshness rule is established.

G2-Q02 retains metamodel classification, source/context qualification, retrospective-establishment responsibility and unsupported Appointment → Encounter cardinality; it no longer questions the approved scheduling-context meaning or distinction from Encounter. G2-Q06 retains the View boundary. Requested need/proposed context remain unestablished as independently governed Concepts. Roles of schedulers and notification reporters are identified by source evidence; no new Scheduler Business Role or exhaustive participant eligibility is invented.

This is conceptual Information Architecture. No FHIR resource/profile, application object, cache/persistence model, schema, API or integration contract is allocated. [AX-17](../../governance/architectural-axioms.md#ax-17) remains authoritative.
