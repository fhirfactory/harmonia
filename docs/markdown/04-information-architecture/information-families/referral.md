# Referral Information Family

**Status: G2 CANDIDATE — Block 1; Referral Outcome semantics APPROVED by G2-D04; family/package not approved as a whole, CLOSED or FROZEN.**

Review standing uses [G2 categories A–D](../reviews/package2-g2-block1-review.md#2-review-standing). Even A identifies a directly derived proposition in this candidate package, not approval of the package. [G1 remains CLOSED](../reviews/package2-g1-review.md#16-controlled-g1-reconciliation-and-closure).

## 1. Purpose & Semantic Boundary

This family describes the information needed to receive a formal request for another care participant or service provider to assess, manage or assume some degree of clinical care responsibility, assemble its supporting context, preserve historical progression evidence, and account for what became of it through governed Referral Outcome information where known.

The request's substantive meaning originates with the referring participant. Harmonia governs referral information and coordination; it does not thereby perform assessment, make triage judgements or assume clinical responsibility.

```text
Referral intent ≠ Referral disposition ≠ responsibility actually assumed ≠ Transfer of Care
Referral ≠ Referral Progression Event ≠ Referral Outcome
Referral Outcome ≠ Responsibility Assumption ≠ Transfer of Care ≠ HealthcareServiceDelivery ≠ ServiceOutcome
Referral information semantics ≠ Referral Administration workflow progression
Referrer ≠ Requester ≠ Service Provider
```

Assessment, management and assumption are potentially overlapping requested purposes, not stages or an exhaustive subtype taxonomy. Referral does not require an Appointment, Encounter, Order, successful delivery or Transfer of Care.

## 2. Domain 03 Responsibility & Traceability

The derivation begins with [Strategy Referral Administration and FEAT-SA-03–05](../../02-strategy/capabilities/business-enabling-capabilities.md#2-referral-administration-harmonia-relevant), [reconciled Referral behaviour](../../03-business-architecture/behaviours/02-service-administration.md#3-referral-administration), the [Referral Progression Process](../../03-business-architecture/processes/business-processes.md#33-referral-progression-process), and the authoritative [Referral Master & Triage Ledger responsibility](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix). Supporting Referral Master Record, Triage Decision Ledger and Referral Disposition Log wording does not create three independent assets or Concepts.

The [Service Delivery Interactions](../../03-business-architecture/collaborations-interactions/interactions.md#22-service-delivery-interactions) distinguish Service Referral, Service Response and Service Outcome. [Collaborations](../../03-business-architecture/collaborations-interactions/collaborations.md#4-collaboration-governance-guardrails-and-invariants) permit discrete interactions without an enduring collaboration; none is mandatory for an individual referral. [Roles](../../03-business-architecture/actors-roles/roles.md#32-referral-direction-is-contextual) distinguish Referrer participation from a local relationship role or ReferralSource qualifier.

The following keys are local review references, not allocated architectural Canonical IDs. Capability Tier, full ancestry and structural IDs remain unresolved under G1 K9.

| Requirement | Established owner / Feature → Function / exposed Service / Process | Interaction → information need → candidate semantics |
| :--- | :--- | :--- |
| RF-IR1 | Referral Administration / FEAT-SA-03 Referral Document Ingestion → Receive Referral / Referral Submission Service | Service Referral → retain what is requested, by whom, for which subject, with indications, purpose and supplied priority → Referral and qualified participant/subject relationships. This meaning is necessary for intake validation, not inferred from a referral document format. |
| RF-IR2 | Referral Administration / FEAT-SA-04 Referral Supporting Information → Assemble Referral Context / Referral Context Query | Clinical Information Supply/Query/Provision in referral triage → understand which supplied history or supporting information bears on this referral and its source → supporting-context relationships; proposed Referral Context Assembly. No new clinical kinds are derived here. |
| RF-IR3 | Referral Administration / FEAT-SA-05 Referral Status & Outcome Tracking → Manage Referral Progression / Referral Status & Outcome Service / Referral Progression Process | Service Response → preserve significant intake/triage and recipient-decision progression evidence, distinct from what ultimately became of the Referral → Referral Progression Event; Referral Outcome as approved by G2-D04. Acceptance, decline and redirection retain their contextual meanings; no canonical outcome value set is established. |
| RF-IR4 | Referral Administration → consumed Person Identifier Resolution and Service Provision Resolution; [established dependency matrix](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix) | Identity Query and service-context resolution → correlate the referred subject and identify a receiving service/provider/catchment where applicable → reuse Package1 identity and DeliverableHealthcareService. These dependencies confer no acceptance or care-transfer authority. |
| RF-IR5 | G2-D04 approval applied to the established Referral Administration responsibility / FEAT-SA-05 → Manage Referral Progression / Referral Status & Outcome Service | Assurance/governance → support assurance that Referrals within the applicable governance scope can be accounted for through sufficient progression and outcome information to establish what became of them → Referral Progression Event and Referral Outcome where known; absent outcome evidence remains an assurance gap. No new owner, Feature or Service is allocated. |

RF-IR5 expresses the approved assurance requirement:

```text
Referral population within governance scope → progression/outcome evidence → assurance coverage
```

The population identifies the scope of accountability; it does not establish a new Collection Concept. An inability to establish what became of a Referral is itself an assurance gap. Unknown outcome remains unknown; no Referral Outcome is manufactured to make coverage appear complete. Detailed governance-scope criteria and evidence sufficiency remain unestablished by this decision.

## 3. Principal Information Concepts

### 3.1 Referral — A

- **Semantic Classification**: Assertion / Request.
- **Definition**: An identifiable formal request, concerning a subject of care, for another care participant or provider to assess, manage or assume a stated care responsibility.
- **Necessity**: RF-IR1 requires the request to remain recognisable as its progression and understood outcome develop. RF-IR4 requires its subject and intended service/recipient to be understood. This is a business request, not a document, generic Order, receiving decision or clinical relationship.
- **Characteristics**: Requested purpose/responsibility, clinical indication/reason, referring participation, intended recipient and supplied clinical priority/context. Priority supplied with a request is distinct from a subsequent clinician's triage judgement. Particular referring and intended-recipient associations use existing relationships, not new Referrer or Referee entities.

### 3.2 Referral Outcome — APPROVED (G2-D04)

- **Semantic Classification**: Outcome; provenance-bearing information with Assertion characteristics where a source reports what became of the Referral. This classification does not equate it with ServiceOutcome.

> **Referral Outcome is the governed information concerning what ultimately became of a Referral, sufficient to support assurance, governance and traceability over the Referral and its progression.**

- **Necessity**: RF-IR3 and RF-IR5 require an account of what became of the Referral, distinct from its formal request and historical progression. Its principal purpose is accountability within the applicable governance/assurance scope, as approved by [G2-D04](../reviews/package2-g2-block1-review.md#g2-d04).
- **Characteristics**: Where established and appropriate, this information may include the resulting outcome, basis/reason, source/originator, authority, temporal context and relationships to relevant resulting activity or information. These are contextual semantic qualifications, not universally mandatory attributes or a canonical outcome value set.
- **Boundary**: Referral Outcome may reference Responsibility Assumption, Transfer of Care, HealthcareServiceDelivery or ServiceOutcome where appropriate; its existence establishes none of them. Recording that a Referral was accepted, progressed or otherwise reached an outcome does not itself prove that care responsibility was actually assumed or transferred. Acceptance and waitlisting retain distinct contextual meanings. Outcome is neither an acceptance synonym nor Referral Administration workflow state.
- **Review history**: The original B candidate Referral Disposition proposed an independent Assertion for an attributable recipient/triage decision. [G2-Q01](../reviews/package2-g2-block1-review.md#g2-q01) is now resolved: G2-D04 rejects that independent Concept and reframes its assurance/governance requirement as Referral Outcome. The original proposal remains in the review history; disposition wording in G1 K1 and upstream source labels remains valid without creating a separate Concept.

### 3.3 Referral Progression Event — A

- **Semantic Classification**: Event.
- **Definition**: Historical information concerning a significant occurrence or reported milestone in the administrative/business progression of a Referral, qualified by its source, business context and occurrence/assertion time.
- **Necessity**: RF-IR3 and the disposition-history responsibility require knowing how the referral progressed, rather than overwriting its intent with the latest process label. Intake validation, clinical triage and scheduling are examples within their evidenced scopes.
- **Boundary**: A Referral Progression Event and Referral Outcome may correlate without becoming identical. Not every progression event represents an outcome; the historical progression record is not collapsed into the final/current Referral Outcome. A recorded event neither proves a later event nor turns an illustrative specialist pathway into every referral's lifecycle.

### 3.4 Referral Context Assembly — B

- **Semantic Classification**: Assembly.
- **Definition**: A governed composition of a Referral and the independently meaningful supporting information selected for its intake/triage context.
- **Necessity**: RF-IR2 explicitly requires assembly and a comprehensive referral dossier. A named assembly is proposed for that business purpose; its precise inclusion and snapshot/dynamic semantics require [G2-Q06](../reviews/package2-g2-block1-review.md#g2-q06).
- **Boundary**: Constituent information retains its own authority, responsibility, identity and qualification. This is not a new clinical record, Collection ownership claim, mandatory stored package or universal longitudinal view.

## 4. Key Information Relationships

All rows apply the [canonical relationship pattern](../patterns/information-relationships.md): Source, Target, Type, with Qualification and Properties where meaningful. Named participant roles are local Relationship Roles; they do not create Business Roles. No cardinalities are allocated from workflow convention.

| Source → Target | Relationship Type / Qualification | Properties needed for meaning | Standing / applicability |
| :--- | :--- | :--- | :--- |
| Referral → Healthcare Subject Context / Person identity reference | Concerns subject | Source subject identification and identity-correlation evidence; reuse [Person family](person-healthcare-subject.md) | A — required to understand the referred subject; no ownership of identity. |
| Referral → Practitioner / Practitioner Role Binding; qualified source organisation where supplied | Originated through referring participation | Actual participant, source authority and context; Referring Clinician is an existing local Relationship Role | A — originating participation required; Practitioner fulfilment of Referrer supported. Exhaustive Actor eligibility and the recipient Business Role remain unresolved (G1 U6). |
| Referral → intended participant/provider; DeliverableHealthcareService when resolved | Addresses / requests assessment or care; resolved service context | Intended recipient, requested purpose, resolution evidence and eligibility/catchment qualification where used | A — contextual/conditional service association under RF-IR4; it does not establish recipient acceptance. Reuse [service contextual binding](healthcare-service.md#stage-2-deliverablehealthcareservice-contextualisation--binding); no ServiceProvisionMapping. |
| Referral Outcome → Referral | Accounts for what became of the referral | Source/originator, authority, basis/reason and established/effective temporal context where relevant | APPROVED G2-D04 — assurance/governance meaning under RF-IR3/RF-IR5; unknown outcome is preserved, not fabricated. No universal attribute bundle or cardinality is established. |
| Referral Progression Event → Referral | Reports administrative progression | Milestone meaning, source, occurrence versus recorded time; decision reference when supplied | A — required for evidenced progression; not a mandatory stage list. |
| Referral Context Assembly → Referral / supporting information | Assembles for triage | Selection purpose, source references and composition provenance | B — proposed composition under G2-Q06. Association does not mean containment or transfer of authority. |
| Referral → Appointment | Associated scheduling | Source evidence identifying the referral and allocated appointment | A — conditional specialist booking pathway, not every referral; see [cross-family analysis](../reviews/package2-g2-block1-review.md#4-cross-family-semantic-analysis). |

## 5. Authority, Responsibility & Provenance

| Concept | Originating authority / source | Harmonia responsibility | Provenance required conceptually |
| :--- | :--- | :--- | :--- |
| Referral | Referring participant for the requested meaning and indications; sending system may only relay it | Referral Administration manages a governed request representation, validates intake, correlates subject/target, preserves and exposes progression | Originator versus supplying source, referral identity/context, indication/priority attribution, received versus authored time where available; correction source and evidence. |
| Referral Outcome | The source/originator of the reported outcome and relevant resulting information; substantive authority is contextual, not conferred by Harmonia custody | Referral Administration manages, preserves, correlates and exposes governed outcome information for assurance and traceability; recording it does not confer authority over clinical judgement, assumed responsibility, transfer or delivery | What outcome was understood, source/originator, authority where relevant, when established/effective, rationale/qualification and correction/derivation history where supported. No universal mandatory attribute bundle or exhaustive authority eligibility is established. |
| Referral Progression Event | Participant/source reporting the occurrence; Harmonia may originate its own intake/coordination facts | Manage administrative progression and its evidence; consume reported clinical/scheduling facts without substituting its own authority | Source event/assertion context, event-to-referral correlation, material transition rationale and correction/derivation history. |
| Referral Context Assembly | Constituent originators; Referral Administration for the composition only | Govern contextual assembly and authorised provision | Inclusion basis and generating context/time; lineage to constituent assertions and their qualifications. Composition acquires none of their originating authority. |

## 6. Temporal & Lifecycle Characteristics

RF-IR3 requires progression occurrence and recorded/asserted time to remain distinguishable: a late triage notification need not mean late triage. Referral Outcome may require knowing when an outcome was established or effective, distinct from when it was recorded or reported, where supported by its context. No universal temporal attribute bundle is established. The request's originating/asserted time and reception time matter when explaining intake and progression. Any supplied requested urgency or timing belongs to the referring request; a triage urgency/category is attributable to its decision-maker, not an inferred appointment deadline.

The Process's Submitted → Intake Validated → Clinically Triaged → Accepted / Waitlisted → Scheduled → Consultation Attended → Discharged / Rejected presentation remains a scoped specialist pathway. It establishes neither a universal sequence nor equivalence between its compound alternatives. No universal responsibility effective period is derivable. Information correction/history follows [Foundation lifecycle governance](../governance/information-lifecycle.md); this is distinct from the real-world care lifecycle and from process progression. No exhaustive retention period or universal Referral transition map is introduced.

Referral workflow closure/finalisation belongs to the progression of the Referral Administration activity / Business Process. The workflow may reach a state in which no further administrative activity is required; that does not establish intrinsic Referral disposition or a terminal Referral state. No Referral Closure or Referral Finalisation Concept is created, and Referral Outcome does not double as workflow state. Business Process progression is not reproduced as the Information Concept lifecycle.

## 7. Assembly Participation & Other Families

Reuse Healthcare Subject Context, Practitioner Role Binding, Healthcare Organisation and DeliverableHealthcareService for identity/participation/service context. Supporting clinical information is referenced under its existing responsibility; its later Package2 families are not derived here. A scheduling allocation can be conditionally associated with this referral. Direct Referral–Encounter and Referral–Order associations are **not established**. A subsequently evidenced delivery/outcome may contextualise the referral, but neither the precise fulfilment relationship nor responsibility transfer is established by referral intake alone; see cross-family analysis.

## 8. Explicit Boundaries, Unresolved Semantics & Downstream Realisation

G1 U1 retains acceptance commitments, extent/duration/effectiveness of assumed responsibility, referring-party relinquishment and the assessment-Referral/bounded-Order boundary. G1 U6 retains Actor eligibility and target-role uncertainty. There is no partial/total responsibility taxonomy, automatic enduring collaboration, clinical incorporation rule or accepted-implies-transfer rule.

Referral Outcome's meaning and independent boundary are approved by G2-D04; G2-Q01 is resolved. Detailed governance scope, evidence sufficiency, source-authority eligibility and contextual provenance/temporal qualification remain unestablished; no canonical outcome values or universal mandatory attribute bundle is defined. The Referral Context Assembly boundary remains a B review proposition. A possible separately attributable account of responsibility actually assumed is [D — suggested](../reviews/package2-g2-block1-review.md#7-non-authoritative-suggestions), not a Concept established in this family. G2-Q05's precise Referral–delivery/outcome associations remain unreviewed. The absence of a precise Referral–Encounter/Order relationship is preserved rather than completed.

This family specifies information meaning and responsibility only. No FHIR mapping, profile, application object, persistence model, API, integration message, component allocation or EMR workflow is designed. [AX-17](../../../architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty) governs all candidate and absent relationships.
