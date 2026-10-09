# Episode / Encounter Information Family

**Status: G2 CANDIDATE — Block 1; G2-D01–D03 approved; remaining propositions await review. Not CLOSED or FROZEN.**

[G2 review standing A–D](../reviews/package2-g2-block1-review.md#2-review-standing) qualifies derivation strength separately from the [approved G2 decisions](../reviews/package2-g2-block1-review.md#10-approved-architectural-decisions). [G1 remains CLOSED](../reviews/package2-g1-review.md#16-controlled-g1-reconciliation-and-closure). Episode is now independently established by G2-D01; its authority, information-responsibility allocation and Encounter-association rules remain unresolved.

## 1. Purpose & Semantic Boundary

Episode & Encounter Administration requires identifiable patient/provider encounter contexts, administrative progression and historical care-place associations. Encounter denotes actual care interaction/context; its administrative tracking may include planning without proving an actual interaction occurred. G2-D03 establishes that an Encounter may contextualise zero or more HealthcareServiceDelivery instances, and a delivery may occur without an Encounter. G2-D01 establishes Episode as longitudinal treatment context for one or more Problems/Conditions across related Encounters, providing an assurance, workflow and financial perspective.

```text
Longitudinal / episode context ≠ bounded encounter context
Appointment ≠ Encounter ≠ HealthcareServiceDelivery
Encounter administrative progression ≠ clinical service performance
Care-place association ≠ Care-Place Definition ≠ operational bed state
```

The owner name includes Episode, but the reconciled Functions, Services and ownership-matrix asset are encounter-specific. G1 K5 still prohibits using owner-name correction to infer Episode ownership. G2-D01 supplies Episode's approved meaning directly; it does not allocate an upstream owner, new Feature or Service. Longitudinal record synthesis remains with Patient Clinical Record. Episode is not merely a Collection of Encounters; Encounter association contributes to its longitudinal treatment meaning without establishing ownership or containment.

## 2. Domain 03 Responsibility & Traceability

Use [Strategy FEAT-SA-08–10](../../02-strategy/capabilities/business-enabling-capabilities.md#4-episode--encounter-administration-harmonia-core), [Episode & Encounter behaviour](../../03-business-architecture/behaviours/02-service-administration.md#5-episode--encounter-administration), the [Encounter Lifecycle Process](../../03-business-architecture/processes/business-processes.md#34-encounter-lifecycle-process), and [Encounter Master & Movement Ledger](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix). Supporting Encounter Master Record, Encounter State Transition Log and Encounter Care-Place Movement Ledger labels describe responsibility, not three predetermined Concepts.

The [Service Delivery and Operational Interactions](../../03-business-architecture/collaborations-interactions/interactions.md#22-service-delivery-interactions) distinguish Service Response/Outcome/Coordination, Transfer of Care, Resource Reporting and Resource Allocation. Care-Team and Operational [Collaborations](../../03-business-architecture/collaborations-interactions/collaborations.md#34-care-team-collaboration) supply relevant participation settings; no universal collaboration instance or care-transfer result is inferred. Clinical Information Provision/Notification describes disclosed context and transition notification without inventing a new Interaction.

Local keys below are not Canonical IDs. Capability Tier/ancestry remain unresolved under G1 K9; the established owner and exact Functions provide the trace.

| Requirement | Established owner / Feature → Function / exposed Service / Process | Interaction → information need → candidate semantics |
| :--- | :--- | :--- |
| EC-IR1 | Episode & Encounter Administration / FEAT-SA-08 Encounter Context Creation → Establish Encounter Context / Encounter Context Resolution | Clinical Information Provision; care-context coordination → distinguish the encounter by source-qualified identity, subject, attending participant, clinical class and supplied admitting diagnosis → proposed Encounter; existing subject/practitioner relationships and candidate Encounter Context Assembly. It does not derive a new diagnosis Concept. |
| EC-IR2 | Episode & Encounter Administration / FEAT-SA-09 Encounter State Progression → Progress Encounter State / Encounter Lifecycle Event Notification / Encounter Lifecycle Process | Service Response/Coordination and clinical information notification → retain attributable administrative state/milestone information independently of clinical performance → Encounter Progression Event and context-qualified state assertions. |
| EC-IR3 | Episode & Encounter Administration / FEAT-SA-10 Encounter Bed/Location Association → Maintain Encounter Care-Place Association / Encounter Location History Query | Resource Allocation/Reporting and Operational Coordination → know which ward/room/bed context applied to the encounter over time → Encounter Care-Place Association using the Foundation relationship pattern. |
| EC-IR4 | Episode & Encounter Administration → consumed Care-Place Specification Lookup and Bed Availability Query; [dependency matrix](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix) | Location lookup and resource reporting before confirming a move → distinguish physical care-place characteristics, readiness evidence and the encounter's association → reuse Healthcare Location / Care-Place Definition; reference operational readiness under its existing owner. No operational-state family is derived. |
| EC-IR5 | Care-Team Collaboration's acute/chronic/inpatient episode context; Primary Care's Correlate Primary Care Consultation Context / Primary Care Summary Ingress; Patient Clinical Record's Assemble Longitudinal Clinical Record / Longitudinal Clinical Record Query; additional explicit architectural authority: G2-D01 | Service Coordination / Clinical Information Provision → longitudinal treatment context for one or more Problems/Conditions and related Encounters, with assurance/workflow/financial perspective → Episode, APPROVED by G2-D01. Upstream references provide contextual evidence, not an inferred Episode owner or Feature. Association rules and responsibility remain C. |

EC-IR5 retains the original [Care-Team Collaboration](../../03-business-architecture/collaborations-interactions/collaborations.md#34-care-team-collaboration), [Primary Care](../../03-business-architecture/behaviours/03-service-delivery.md#21-primary-care), and [Patient Clinical Record](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#21-patient-clinical-record) context. The prior absence of an established Episode meaning is superseded specifically by [G2-D01](../reviews/package2-g2-block1-review.md#g2-d01), not completed through inference or a standard. No association mechanism, new upstream responsibility or Problems/Conditions family is derived in this persistence step.

## 3. Principal Information Concepts

### 3.1 Encounter — G2-D02/D03 approved meaning; remaining classification/tracking boundary B

- **Semantic Classification**: Activity / Contextual Binding — exact metamodel classification and administrative tracking/Assembly boundary remain under G2-Q03; actual care-interaction/context meaning is established by G2-D02/D03.
- **Definition**: Information identifying and contextualising an actual bounded care interaction between a healthcare subject and participating healthcare providers, distinct from scheduling context and from actual Healthcare Service Delivery/performance.
- **Necessity**: EC-IR1 and EC-IR2 require continuity of encounter tracking across planning, arrival, active care and discharge information. G2-D02/D03 establish the represented care-interaction meaning; how administrative planning/tracking relates to that actual interaction and the Assembly remains [G2-Q03](../reviews/package2-g2-block1-review.md#g2-q03).
- **Characteristics**: Source/namespace-qualified encounter identity, subject reference, attending participant, clinical class, admitting diagnosis/context as supplied, and attributable administrative state. These are governed characteristics/relationships, not automatically separate Concepts.
- **Boundary**: Planned tracking information does not prove an actual Encounter occurred. Appointment existence is not occurrence evidence, and an actual Encounter does not prove a prospective Appointment existed. An Encounter may contextualise zero or more deliveries, but neither proves delivery nor owns/contains it; delivery may occur without Encounter context. Encounter identity remains distinct from Person, Appointment and Episode identity.

### 3.2 Encounter Progression Event — A

- **Semantic Classification**: Event.
- **Definition**: Information concerning a material encounter administrative transition or reported milestone, with its triggering context, qualified state meaning, source authority and time.
- **Necessity**: EC-IR2 expressly requires state progression, a transition log and notifications. The event carries state assertions where appropriate; the Foundation does not require a new Concept for each state label.
- **Boundary**: Planned, arrived, active, on leave, discharged and completed information retain their source/context meanings. No unestablished equivalence between Behaviour, Strategy and Process labels is added. Discharged does not automatically establish all documentation/coding completion or legal closure.

### 3.3 Encounter Care-Place Association — A

- **Semantic Classification**: Relationship / Temporal Contextual Association.
- **Definition**: The qualified association of an Encounter with a Healthcare Location or Care-Place over the period relevant to that encounter's operational location/context.
- **Necessity**: EC-IR3 requires a movement history and current location context. This applies the existing relationship machinery; it does not add a local movement-record implementation.
- **Boundary**: Planned placement/reservation and actual location must retain their qualification when supplied. A source asserting encounter location does not thereby establish bed readiness, cleaning completion, provider ownership or Transfer of Care. Physical care-place association is applicable where that setting has such locations, not mandatory for every virtual/community contact.

### 3.4 Encounter Context Assembly — B

- **Semantic Classification**: Assembly.
- **Definition**: The existing Foundation candidate for composing independently meaningful encounter, subject, attending participant, admitting context and care-place information for encounter-context resolution.
- **Necessity**: EC-IR1 and EC-IR3 support coordinated provision of these facts. Reuse [Foundation Encounter Context](../assemblies-views/assemblies-and-views.md#2-illustrative-candidate-context-assemblies--views) and Package1 assembly participation rather than create an equivalent local pattern.
- **Boundary**: This is not the Encounter activity, an Episode Collection, a longitudinal record or a mandatory persisted aggregate. It acquires no originating authority over constituents. Exact inclusion/composition scope remains [G2-Q06](../reviews/package2-g2-block1-review.md#g2-q06).

### 3.5 Episode — APPROVED (G2-D01)

- **Semantic Classification**: Longitudinal Information Context. It is not merely a Collection of Encounters; no additional metamodel kind or implementation aggregate is assigned by this decision.
- **Definition**: Episode is a longitudinal information context representing the treatment of one or more Problems or Conditions across a related series of Encounters, providing a coherent perspective for assurance, workflow and financial purposes.
- **Architectural authority**: Explicitly approved in [G2-D01](../reviews/package2-g2-block1-review.md#g2-d01), superseding the former non-authoritative Episode Context suggestion while retaining its review history. This meaning is independent of downstream representation and is not derived from FHIR EpisodeOfCare.
- **Necessity**: EC-IR5 now has this explicit approved longitudinal treatment meaning. Problems/Conditions and related Encounters contribute to the treatment context; Encounter association does not define Episode's complete meaning. These are boundary references, not derivation of a later clinical family.
- **Relationships and boundary**: Episode relates one or more Problems/Conditions [1..*] to a longitudinal course of treatment and may relate to multiple Encounters [0..*]. Not every Encounter for the subject necessarily belongs to a particular Episode. No Encounter-association rules, reverse cardinality, Encounter ownership or implementation containment are established.
- **Remaining uncertainty**: Originating authority, Harmonia information-responsibility allocation, detailed identity, association evidence/rules and temporal/lifecycle details remain unresolved under G2-Q04. Assurance/workflow/financial perspective does not confer performance, billing or assurance authority.

## 4. Key Information Relationships

| Source → Target | Type / Qualification | Properties necessary for meaning | Standing / applicability |
| :--- | :--- | :--- | :--- |
| Encounter → Healthcare Subject Context | Concerns subject | Source identity reference, correlation and care setting | A subject meaning; actual care-interaction meaning approved by G2-D02/D03, remaining tracking/classification under G2-Q03. Reuse [Person family](person-healthcare-subject.md); registration does not originate Person identity. |
| Encounter → Practitioner / Practitioner Role Binding | Attending / care participation | Source-attested participation and effective context/period where supplied | A — attending context required by EC-IR1; broader care-team context supported by FEAT-SA-10. Relationship Roles are not new Business Roles, and participation does not establish consent or decision authority. |
| Encounter Progression Event → Encounter | Reports administrative transition | Source authority, prior/new state where meaningful, rationale and transition/recorded time | A — required EC-IR2; state-label correspondence remains contextual/unresolved. |
| Encounter → Healthcare Location / Care-Place Definition | Encounter Care-Place Association | Location evidence, effective period, planned/actual qualification and change provenance | A — conditional on physical placement, required meaning for that context; reuse [Location family](healthcare-location.md#5-architectural-invariant-static-definition-vs-operational-state). |
| Encounter Context Assembly → Encounter / subject / participants / contextual information | Assembles for encounter-context provision | Inclusion purpose and attributable constituent references | B — proposed composition boundary under G2-Q06, without containment/authority transfer. |
| Appointment → Encounter | Prospective or retrospectively established scheduling/administrative context | Explicit correlation; proposed scheduling-origin/context qualification for later review | APPROVED — G2-D02. No Appointment → Encounter cardinality; retrospective establishment does not prove prospective scheduling. |
| Encounter → HealthcareServiceDelivery | Care-interaction context for delivery | Explicit encounter/delivery correlation, source and temporal qualification | APPROVED — G2-D03: zero or more deliveries [0..*] may occur in an Encounter context; delivery may occur without an associated Encounter. Context is not identity, containment or mandatory progression. Further authority/evidence detail remains unresolved. |
| Episode → Problem / Condition boundary reference | Longitudinal treatment context concerns | Source authority, treatment-context and association evidence remain incompletely established | APPROVED — G2-D01: one or more Problems/Conditions [1..*]. Detailed clinical family and authority allocation are not derived. |
| Episode → Encounter | Related care interaction in longitudinal treatment context | Source authority and association evidence/rules remain unresolved | APPROVED — G2-D01: related Encounters [0..*]. Not every subject Encounter belongs to a particular Episode; rules, ownership and reverse multiplicity are not inferred. G2-Q04. |

## 5. Authority, Responsibility & Provenance

| Concept | Originating authority / sources | Harmonia responsibility | Provenance characteristics |
| :--- | :--- | :--- | :--- |
| Encounter | Health-service administrative participant for registration/class/state; treating participant for clinical context assertions; no universal originator is specified | Episode & Encounter Administration creates/manages the governed tracking context, correlates and exposes it; does not originate every clinical fact or perform the encounter | Namespace/source identity, subject correlation, originating versus supplying participants, admitting-context attribution and correction evidence. Strategy's authoritative tracking-context responsibility does not confer authority over the represented care. |
| Encounter Progression Event | Source empowered to assert the particular administrative/clinical milestone; Harmonia for its own governance/coordination facts | Govern encounter information progression and notification | Triggering interaction, source and authorising context, transition occurrence versus recorded time, rationale and amended/superseding assertion lineage. Exact eligibility for each transition remains context-specific. |
| Encounter Care-Place Association | Placement/movement source; Location Administration retains place definition, Bed & Care-Place Management retains readiness/state | Govern encounter-to-location association/history; consume readiness evidence without taking its ownership | Source association, effective period, actual/planned qualification, move/correction evidence and referenced readiness context where used. |
| Encounter Context Assembly | Each constituent originator; Episode & Encounter Administration for encounter-context composition | Govern assembled contextual provision only | Composition/inclusion context and generating time; constituent authority and source references. No assembly acquires clinical or booking authority. |
| Episode | Originating treatment-context/association authority is not established by G2-D01 | Episode's meaning is approved; a specific Harmonia owning/managing responsibility is not allocated. No authority over Encounters, Problems/Conditions or constituent information is inferred | Source, context and association provenance need further review under G2-Q04; no particular originator, association mechanism or authority transfer is asserted. |

## 6. Temporal & Lifecycle Characteristics

Encounter progression requires transition/occurrence time distinct from asserted/recorded time. Care-place movement requires effective association periods to explain where a subject was accommodated over time. Planned timing and actual arrival/care/discharge timing remain distinguishable where evidenced; neither scheduled time nor a last-known state establishes actual service start/end.

G1 K10 preserves independently established Strategy ON_LEAVE and the Behaviour/Process progressions. Their applicability and transition correspondence remain unresolved (G1 U8). The Process's triage, documentation finalisation, coding and legal-closure conditions remain local; they do not become every Encounter's lifecycle. ON_LEAVE is neither omitted nor mapped to another state.

Information lifecycle governance, real-world care progression and Business Process progression may correlate but are distinct. Information corrections preserve the Foundation's history/provenance rules without changing history of the actual encounter. The [Discharge Management responsibility](../../03-business-architecture/behaviours/04-health-service-operations.md#215-discharge-management) and [Discharge Progression Process](../../03-business-architecture/processes/business-processes.md#413-discharge-progression-process) now establish the preparation versus confirmed-exit distinction: applicable information may be prepared, published or communicated earlier, while FEAT-HSO-29 departure notifications, updated bed-state communication and finalised-summary dispatch are triggered by confirmed physical exit. Earlier preparation does not satisfy that trigger. This supersedes only that portion of G1 U9; detailed applicability and timing, the relation between summary signing and finalisation, discharge-authorisation workflows, universal Encounter closure and downstream transitions remain unestablished. No additional publication event, universal lifecycle, atomic transition linkage or FHIR status mapping is inferred.

Episode is longitudinal treatment context under G2-D01. No start/end criteria, episode closure state, Encounter-association effective rules or common lifecycle with its related Encounters are established. G2-D02's retrospectively established Appointment must not be mistaken for prospective scheduling; G2-D03's contextual association does not create an Encounter-to-delivery lifecycle.

## 7. Assembly Participation & Other Families

Reuse Healthcare Subject Context, Practitioner Role Binding, Healthcare Location / Care-Place Definition, and the existing candidate Encounter Context Assembly. Encounter information can contribute to the established longitudinal assembly owned by Patient Clinical Record; this does not make Episode & Encounter Administration owner of longitudinal synthesis.

An Appointment may provide prospective scheduling context or be established retrospectively for an unscheduled Encounter (G2-D02). An Encounter may contextualise zero or more independently meaningful HealthcareServiceDelivery instances; delivery may occur without Encounter context (G2-D03). Episode relates one or more Problems/Conditions to longitudinal treatment across related Encounters (G2-D01), without deriving that later clinical family. The Package1 DeliverableHealthcareService → optional Order → HealthcareServiceDelivery → ServiceOutcome chain remains intact with no mandatory Encounter.

Clinical orders may occur during the local encounter pathway, but exact universal Order–Encounter correlation and cardinality remain unestablished. A Referral's attended consultation does not by itself supply an explicit Referral–Encounter link. See [cross-family analysis](../reviews/package2-g2-block1-review.md#4-cross-family-semantic-analysis).

## 8. Explicit Boundaries, Unresolved Semantics & Downstream Realisation

G2-Q03 retains metamodel classification and the administrative tracking/Assembly boundary after approval of actual care-interaction meaning. Episode is established; G2-Q04 concerns only unresolved authority/responsibility, detailed identity, association rules/evidence and temporal/lifecycle details. The [prior Episode Context suggestion](../reviews/package2-g2-block1-review.md#7-non-authoritative-suggestions) is historical and superseded by G2-D01, not a current D suggestion. No Episode = Collection of Encounters rule, membership mechanism, hierarchy or universal lifecycle is added.

G2-Q05's Encounter/delivery optional contextual association and [0..*] multiplicity are resolved by G2-D03; additional authority/evidence qualifications and the unrelated Referral portion remain unreviewed. G2-Q06 retains the Assembly boundary. G1 U4's universal delivery/outcome authority, U8's ON_LEAVE correspondence and U9's discharge applicability/timing beyond the established preparation/confirmed-exit distinction remain unresolved. No clinical responsibility transfer is inferred from movement or discharge, and no operational bed model is derived.

No FHIR mapping, application component, class, persistence entity, schema, API, integration message, cache model or implementation allocation is created. [AX-17](../../governance/architectural-axioms.md#ax-17) governs the derivation itself.
