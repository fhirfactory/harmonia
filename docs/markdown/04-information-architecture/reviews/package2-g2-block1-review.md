# Domain04 Package2 — G2 Block 1 Semantic Review

**Status: G2 CANDIDATE ARCHITECTURE — G2-D01–D04 APPROVED; G2-Q01 RESOLVED; remaining propositions await review. G2 is not CLOSED.**

**Date:** 2026-10-08. **Scope:** Referral; Appointment / Scheduling; Episode / Encounter; Order only.

## 1. Governing Authority & Derivation Basis

The [central Architectural Axioms](../../governance/architectural-axioms.md) govern. Materially relevant are AX-01/AX-04 (information meaning before implementation machinery), AX-02/AX-03 (standards/representation boundaries), AX-06 (explicit information authority), AX-08 (meaningful provenance/evidence), AX-13 (managed-information boundary), AX-14 (semantic distinctions), AX-15 (operational uncertainty) and [AX-17](../../governance/architectural-axioms.md#ax-17) (architectural authority and explicit absence). The candidates describe attributable business information and bounded Harmonia responsibility; none acquires authority from custody or derives its meaning from representation. Existing security/access governance is retained without designing a new access model.

[G1 §16](package2-g1-review.md#16-controlled-g1-reconciliation-and-closure) is the current closure record: **G1 remains CLOSED; K1–K13 remain RESOLVED — APPROVED with their qualifications.** Its historical statements that G2 had not commenced describe the closure-time boundary. The initial separately authorised Block 1 task commenced bounded G2 derivation without rewriting G1 history or approving G2; §§10–11 record the subsequent bounded decision approvals.

Current reconciled sources, not pre-reconciliation quotations, supply the derivation:

- [Strategy catalogue](../../02-strategy/capabilities/business-enabling-capabilities.md#view-2-service-administration): the four established owners and FEAT-SA-03–14; ancestry/Tier uncertainty retained. Strategy overview, capability index/taxonomy and derivation rules constrain the trace without assigning a new Business Capability ancestor.
- [Service Administration behaviour §§3–6](../../03-business-architecture/behaviours/02-service-administration.md#3-referral-administration) and [Processes §§3.3–3.5](../../03-business-architecture/processes/business-processes.md#33-referral-progression-process): exact Functions, Services, locally scoped progression and explicit qualifications. The Clinic / Operational Queue Process supplies a bounded example of operational use of scheduling information, not a scheduling engine.
- [Information responsibility matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix), [dependency matrix](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix), [Roles](../../03-business-architecture/actors-roles/roles.md), [Actors](../../03-business-architecture/actors-roles/actors.md), [Interactions](../../03-business-architecture/collaborations-interactions/interactions.md) and [Collaborations](../../03-business-architecture/collaborations-interactions/collaborations.md): ownership, participation, exposure and qualified interactions.
- [Service Delivery](../../03-business-architecture/behaviours/03-service-delivery.md) and [Patient Clinical Record](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#21-patient-clinical-record): performance-enablement boundary and longitudinal contextual use. Capability-scoped Functions with unestablished Feature associations retain that standing.
- Domain04 [Metamodel](../metamodel/information-architecture-metamodel.md), [Relationships](../patterns/information-relationships.md), [Collections/containment](../patterns/containment-and-collections.md), [Definition-to-accountability](../patterns/definition-to-accountability.md), [Authority/provenance](../governance/authority-custody-provenance.md), [Lifecycle](../governance/information-lifecycle.md), [Assemblies/Views](../assemblies-views/assemblies-and-views.md), [Guardrails](../guardrails/modelling-guardrails.md), [Traceability](../traceability/domain03-traceability.md) and all six [Package1 families](../information-families/README.md).

Each family records the forward derivation: established owner/Feature → Function/Service/Process → established Interaction or qualified collaboration context → local Information Requirement → proposed/reused Concept → qualified relationship. Canonical ownership-matrix assets anchor responsibility. An applicable interaction vocabulary does not by itself establish a new Function-to-Interaction allocation, Service consumption or collaboration instance. Local requirement/question keys are document review references, not structural Canonical IDs.

## 2. Review Standing

| Category | Meaning in this review | Authority |
| :--- | :--- | :--- |
| **A — Directly Derived** | Information meaning/need strongly follows current Business Architecture or existing Domain04 semantics | Candidate proposition in this package; existing upstream meaning remains authoritative. A is not package approval. |
| **B — Derived but Requires Review** | Evidenced need supports the derivation, but Concept boundary, classification or relationship/composition judgement remains | Candidate only; architect acceptance required before authoritative adoption. |
| **C — Unresolved** | Architecture does not establish enough meaning, responsibility or relationship | Absence is explicitly retained. No candidate decision resolves it. |
| **D — Suggested** | Potentially useful idea lacks sufficient derivation for candidate establishment | Explicitly non-authoritative; excluded from the principal Concept inventory and required relationships. |

Category is separate from applicability: required, conditional, contextual or optional describes when a relationship matters. A strongly derived conditional relationship is still not universally required. A relationship not established is neither prohibited nor presumed available.

Approval standing is also separate from derivation category. The [explicit approved decisions G2-D01–D03](#10-approved-architectural-decisions) now establish Episode's meaning and the specified Appointment/Encounter/delivery distinctions and relationships. They supersede only the prior absence or candidate standing they expressly address; other B/C questions remain unresolved and the package is not approved as a whole.

[G2-D04](#g2-d04) subsequently rejects the independent Referral Disposition candidate and establishes Referral Outcome for assurance, governance and traceability. G2-Q01 is resolved; approval does not settle the other Referral questions or confer clinical authority.

## 3. Concept & Requirement Propositions

Names below are semantic names; spacing/style does not allocate implementation classes. Existing Concepts and existing candidate Assemblies are identified to avoid duplication.

| Family / local requirements | A — directly derived Concept propositions | B — proposed Concept/composition boundary | C — significant absence |
| :--- | :--- | :--- | :--- |
| [Referral](../information-families/referral.md), RF-IR1–5 | Referral (Assertion / Request); Referral Progression Event (Event). Subject, originating participation, intent/reason, supporting context and recipient-decision meanings remain required. Referral Outcome (Outcome, with source-Assertion characteristics) is separately APPROVED by G2-D04 for assurance/governance; G2-Q01 is resolved. | Referral Context Assembly (Assembly: G2-Q06). The former independent Referral Disposition candidate is rejected/superseded by G2-D04, not retained here. | Acceptance commitment/effective responsibility/transfer; recipient Role/Actor eligibility; exact assessment-Referral/bounded-Order boundary; detailed governance scope, evidence sufficiency and contextual outcome qualification. |
| [Appointment / Scheduling](../information-families/appointment-scheduling.md), AP-IR1–3 | Scheduling Change Event (Event); Schedule Synchronisation State (Assertion / Derived Knowledge State). Source authority and planned versus known state distinction are required. | Appointment metamodel classification and source/context qualification only (G2-Q02; scheduling meaning approved by G2-D02); Patient Appointment Schedule View (View: G2-Q06) | Standalone requested/proposed scheduling Concepts; detailed participant/service/temporal contract; retrospective-establishment authority; Appointment → Encounter cardinality; source-conflict/freshness criteria. |
| [Episode / Encounter](../information-families/episode-encounter.md), EC-IR1–5 | Encounter Progression Event (Event); Encounter Care-Place Association (Relationship, existing pattern). Identifiable encounter context, subject, attending participation and temporal movements are required. | Encounter metamodel classification and administrative tracking/Assembly boundary only (G2-Q03; actual care-interaction meaning approved by G2-D02/D03); Encounter Context Assembly (existing candidate, reused: G2-Q06) | Episode originating authority, information-responsibility allocation, detailed identity and Encounter-association rules/evidence; ON_LEAVE correspondence; discharge publication timing. Episode meaning itself is approved by G2-D01. |
| [Order](../information-families/order.md), OR-IR1–6 | Order (existing Assertion / Direction); Order Routing Directive (existing Assertion / Instruction); Order Progression Event (Event); Order Change Request (Assertion); Order-Result Correlation Link (existing Relationship) | Order Context Assembly (existing candidate, reused: G2-Q06) | Change authority/response/effect criteria (G2-Q07); universal acceptance/finality/legal qualification; exact broader outcome kinds/owner traces. |

The inventory remains **19 named principal candidate/reused Concepts or compositions**: **11 A and 6 B** for derivation/remaining boundary review, plus **Episode — APPROVED by G2-D01** and **Referral Outcome — APPROVED by G2-D04**. The original 18-Concept inventory contained 11 A and 7 B; G2-D01 added Episode from the former D suggestion, and G2-D04 replaces the B Referral Disposition candidate with approved Referral Outcome without adding another Concept. Appointment and Encounter retain residual classification/qualification review; their specified meanings and relationships are approved by G2-D02/D03. Approval does not manufacture an A upstream derivation or settle Episode ownership. No Concepts are introduced for each identifier, participant role, status, date, acknowledgement qualifier, clinical context item or destination. Existing Concepts and Source/Target/Type/Qualification/Properties remain the relationship machinery.

Direct derivation establishes the need to distinguish request, source decision and reported milestone. G2-D04 now establishes Referral Outcome's assurance/governance boundary while rejecting a separate Referral Disposition Concept; significant progression occurrences remain historical evidence rather than all becoming outcomes. Remaining B propositions concern only their recorded unreviewed boundaries. No Concept is justified solely by a standard, conventional EMR practice or familiar software design.

## 4. Cross-Family Semantic Analysis

Each relationship uses the common relationship machinery. Properties below state semantic evidence/qualification, not a payload design. Only multiplicities explicitly approved by G2-D01/D03 are recorded; no other cardinality or automatic counterpart creation is inferred.

| Source → Target / relationship | Applicability / category | Evidence and qualification | What remains unestablished |
| :--- | :--- | :--- | :--- |
| Referral → DeliverableHealthcareService, intended/resolved service context | **Conditional / contextual — A** | Referral consumes Service Provision Resolution to resolve target clinic/provider/catchment; RF-IR4 reuses Package1 contextual binding. Properties: requested purpose, intended recipient and resolution evidence. | Every Referral need not have a fully resolved service binding; binding does not establish acceptance or delivery. |
| Referral → Appointment, associated allocation | **Conditional — A** | Referral Process Scheduled allocates an outpatient appointment. Preserve source correlation and external scheduler authority. | No universal consequence of Referral; no mandated booking sequence. |
| Referral → Encounter, direct association | **Not established — C** | Consultation Attended reports specialist assessment in a scoped pathway, but supplies no precise Referral–Encounter identity/association rule. | Appointment/consultation context does not fill this missing link. No relation is inferred transitively through Appointment. |
| Referral → Order, conversion/causation/association | **Not established — C** | Approved K1 preserves distinct requests and explicitly rejects a universal Order consequence. | Specific causal or correlation rule and the assessment/bounded-order boundary. |
| Referral → HealthcareServiceDelivery / ServiceOutcome, reported fulfilment/outcome context | **Contextual candidate — B** | Scoped consultation attendance/assessment and separate Service Outcome Interaction support relating actual delivery/outcome information to referral context when separately evidenced. G2-Q05 reviews precise meaning; disposition alone is insufficient. | Universal fulfilment criterion, complete authority trace, and automatic care-responsibility assumption/Transfer of Care. |
| Appointment → Encounter, scheduling/administrative context | **Contextual association — APPROVED G2-D02** | Appointment may provide prospective context or be created retrospectively for an unscheduled Encounter, such as emergency/walk-in. Proposed scheduling-origin/context qualification is retained for later review; retrospective creation does not prove prospective scheduling. | Appointment → Encounter cardinality remains unestablished, including cancellation, rescheduling, recurring, group and other contexts. Appointment existence does not imply Encounter occurred; Encounter does not prove prospective Appointment existed. |
| Appointment → HealthcareServiceDelivery, actual fulfilment | **Not established — C** | Booking notifications establish planned context; actual delivery requires independent evidence. | No direct universal or transitive appointment-fulfilment rule. |
| Encounter → HealthcareServiceDelivery, care-interaction context | **Optional contextual association — APPROVED G2-D03** | Encounter may contextualise zero or more deliveries [0..*]. Delivery may occur without an associated Encounter. This is neither semantic identity, containment nor mandatory lifecycle progression. | Further authority/correlation qualifications and universal Delivery owner remain unestablished. Encounter existence does not prove any service delivery; no unapproved reverse cardinality is assigned. |
| Order → DeliverableHealthcareService, requested service context | **Conditional / contextual — A** | Package1 chain, Service Provision Resolution and routing responsibility. Properties: bounded request scope and resolved service/provider context. | Every incoming Order need not have a resolved binding; Order is not mandatory for delivery. |
| Order → HealthcareServiceDelivery, request versus evidenced fulfilment association | **Conditional — A** | Package1 establishes direction and separate execution information; Order Process supplies execution evidence in its local scope. Properties distinguish requested performance from confirmed occurrence. | Existence of Order does not prove delivery, and unfulfilled Order remains legitimate information. |
| Order → ServiceOutcome / returned result, outcome association | **Required responsibility; conditional instance — A** | Associate Order Outcome / Order Outcome Notification and existing Order-Result Correlation Link; source qualification and correlation evidence retained. | No universal outcome authority, diagnostic verification/finality definition, successful-outcome guarantee or conflict adjudication. |
| Order → Appointment, imaging scheduling context | **Conditional — A** | Local Specimen Collected / Scheduled checkpoint includes imaging appointment booking. | Does not establish scheduling as a stage for all Orders or Order Administration as booking authority. |
| Order → Encounter, particular care context | **Contextual occurrence evidenced; precise relationship not established — C** | Encounter Process Active In-Progress includes clinical orders underway. | No universal Order–Encounter identity/correlation rule, compulsory encounter context or cardinality is established. |
| Episode → Problem / Condition, longitudinal treatment context | **APPROVED G2-D01 — [1..*] Problems/Conditions** | Episode represents their treatment across related Encounters with assurance/workflow/financial perspective. Problems/Conditions are semantic boundary references; their later family is not derived here. | Episode authority/responsibility and detailed relationship qualifications remain unresolved. No new clinical taxonomy, authority or implementation containment is established. |
| Episode → Encounter, related care interaction in longitudinal treatment context | **APPROVED G2-D01 — related Encounters [0..*]** | Episode is independently meaningful, not merely an Encounter Collection. Encounter association contributes to its treatment context without defining its complete meaning; not every subject Encounter belongs to a particular Episode. | Mechanism/rules for association, detailed identity, authority/responsibility, reverse multiplicity and temporal/lifecycle semantics remain unresolved under G2-Q04. No Encounter ownership or containment. |
| Encounter Care-Place Association → operational place/readiness context | **Conditional — A** | Explicit location/readiness Service dependencies before confirmed moves. Location Administration owns definition; Bed & Care-Place Management owns readiness/state. | Encounter location does not establish bed cleaning/readiness or Transfer of Care. No operational family is derived. |

Explicit equivalence distinctions are **A — mandatory constraints**: Referral ≠ Order; Referral intent ≠ disposition ≠ responsibility actually assumed ≠ Transfer of Care; Appointment ≠ Encounter; Encounter ≠ HealthcareServiceDelivery; Order ≠ delivery/outcome; dispatch ≠ receipt ≠ acknowledgement ≠ acceptance. These forbid collapse, not evidenced optional/contextual associations.

**G2-D04 — approved distinctions:** Referral ≠ Referral Progression Event ≠ Referral Outcome; Referral Outcome ≠ Responsibility Assumption ≠ Transfer of Care ≠ HealthcareServiceDelivery ≠ ServiceOutcome. Outcome may reference relevant resulting activity/information where appropriate, but its existence establishes none of those phenomena. The precise Referral–delivery/outcome contextual relationships under G2-Q05 remain unreviewed; this permission supplies no universal fulfilment or transfer relationship.

## 5. Authority, Temporal Meaning & Composition Findings

Originating authority and information responsibility are independent. Referral originators assert intent; triage/recipient decision-makers assert dispositions. Scheduling sources retain authoritative booking state; Harmonia governs knowledge/synchronisation and projection. Encounter Administration governs the tracking context and associations; administrative/clinical participants retain authority over their particular source assertions. Order Administration governs intake/routing/progression/correlation/change coordination, while requesters, performers and outcome originators retain substantive request/performance/outcome authority. Exhaustive eligibility and clinical/legal qualification are not invented.

Referral Administration governs Referral Outcome information for accountability within the applicable assurance/governance scope. The source/originator and substantive authority of what became of a Referral remain contextual. Recording acceptance, progression or an outcome does not prove that care responsibility was actually assumed or transferred; Harmonia's information governance confers no such authority. RF-IR5 requires sufficient progression/outcome evidence to account for the scoped Referral population. Unknown outcome is preserved as absent evidence and an assurance gap, not filled to manufacture coverage.

Source, relay, originator and responsible participant are separately identifiable where the business evidence requires them. Harmonia may originate its own intake, routing, association, synchronisation and composition facts; it does not thereby originate the clinical phenomena represented. Acknowledgement, review, credential validation, storage, aggregation and presentation confer no new originating authority.

Temporal needs follow particular responsibilities: disposition/progression occurrence versus assertion/recording; scheduled allocation versus actual care; effective care-place association; requested change versus established effect; outcome association time versus outcome occurrence. No universal date bundle, common lifecycle or universal signature state is created. Information correction, real-world progression and Process checkpoints remain distinct. Source evidence and derivation/correction lineage remain conceptual provenance, not a database design.

Referral Outcome may require provenance of what was understood, source/originator, relevant authority, established/effective time and rationale/qualification where supported; these are not universally mandatory attributes. Referral Administration workflow closure/finalisation remains Business Process progression, not intrinsic Referral disposition, a terminal Referral state or an Outcome acting as workflow state. No Referral Closure or Referral Finalisation Concept is introduced.

Referral Context Assembly and Patient Appointment Schedule View have directly evidenced assembly/query purposes but proposed composition boundaries. Existing Encounter Context Assembly and Order Context Assembly candidates are reused. **Assemblies/Views acquire no originating authority over constituent information.** No additional Collection is required. Episode's longitudinal treatment meaning and related-Encounter association are now approved, while its information responsibility, originating authority and association rules remain unresolved. Collection ≠ Membership ≠ Containment remains intact; assurance/workflow/financial perspective confers no additional authority.

## 6. Architectural Review Questions

The seven identifiers are retained for review continuity. G2-D01–D03 resolve only their expressly approved portions of G2-Q02–05. G2-D04 now resolves G2-Q01 only. G2-Q02–07 retain their existing residual questions without alteration or further review in this step. A review answer may retain an absence; this persistence step modifies no frozen upstream architecture.

<a id="g2-q01"></a>

### G2-Q01 — Referral disposition boundary (RESOLVED — G2-D04; G1 U1 retained)

**Original B review question (retained as history):**

> Should Referral Disposition have an independently identifiable Assertion boundary, as proposed, or should the recipient/triage decision remain qualified information on Referral Progression Event? The decision must retain its own source/reason and meaning apart from request intent, even if no separate Concept is accepted. This question does not define acceptance's clinical responsibility effect.

**Approved resolution:** [G2-D04](#g2-d04) rejects Referral Disposition as an independent Concept and reframes its assurance/governance requirement as Referral Outcome. Referral Progression Event remains significant historical progression evidence, distinct from Outcome. Workflow closure/finalisation remains Referral Administration activity/Business Process progression. G1 K1 and U1 are preserved; no responsibility-assumption/transfer effect is established. Contextual authority/provenance/time qualification and detailed assurance scope/evidence sufficiency remain unestablished without reopening Q01 or answering another question.

<a id="g2-q02"></a>

### G2-Q02 — Appointment classification and source/context qualification (PARTIALLY RESOLVED — G2-D02; residual B/C)

Appointment's distinction from Encounter and prospective/retrospective scheduling-context meaning are approved by G2-D02; the former prospective-only meaning question is superseded. Remaining unreviewed matters: exact metamodel classification, minimum service/participant/time/source qualification, who originates or has authority for retrospective establishment, and Appointment → Encounter cardinality across cancellation, rescheduling, recurring, group and other contexts. Scheduling-origin/context is identified as a qualification/property for later review, not a separate Concept. Requested/proposed arrangements remain unresolved; external booking authority is preserved.

<a id="g2-q03"></a>

### G2-Q03 — Encounter administrative tracking/context assembly boundary (PARTIALLY RESOLVED — G2-D02/D03; residual B; G1 U4/U8 retained)

Encounter's actual care-interaction/context meaning and distinction from Appointment and HealthcareServiceDelivery are established by G2-D02/D03; the former alternatives for that primary meaning are superseded. Remaining unreviewed matters: precise metamodel classification and how administrative planning/tracking information relates to the actual interaction and existing Encounter Context Assembly. Planned tracking does not prove actual occurrence. ON_LEAVE correspondence and Process checkpoint applicability remain unresolved.

<a id="g2-q04"></a>

### G2-Q04 — Episode association rules and information responsibility (PARTIALLY RESOLVED — G2-D01; residual C)

Episode's independent longitudinal treatment meaning is approved by G2-D01, including one or more Problems/Conditions [1..*] and related Encounters [0..*]; whether Episode exists is no longer a question. Remaining unreviewed matters: originating authority and Harmonia information responsibility, detailed identity, mechanism/rules and evidence for Encounter association, reverse multiplicity and temporal/lifecycle semantics. The encounter-specific upstream owner name does not supply these details. No Encounter ownership, Episode Collection or implementation containment is implied.

<a id="g2-q05"></a>

### G2-Q05 — Referral and Encounter delivery/outcome contextualisation (PARTIALLY RESOLVED — G2-D03; residual B/C)

G2-D03 approves optional Encounter care-interaction context for zero or more HealthcareServiceDelivery instances [0..*], with delivery permitted without Encounter. The former question about that association's existence, basic meaning and multiplicity is resolved; further authority/evidence qualifications remain unreviewed. The Referral portion remains unreviewed: are the proposed evidence-qualified Referral–delivery/outcome contextual associations sufficient, and what does each association assert? No automatic fulfilment, responsibility transfer, successful outcome or universal Delivery/Outcome owner is established. The missing direct Referral–Encounter link remains C.

<a id="g2-q06"></a>

### G2-Q06 — Justified composition boundaries (B)

Are Referral Context Assembly, Patient Appointment Schedule View, and the reused Encounter Context Assembly / Order Context Assembly the appropriate semantic compositions for their evidenced Functions/Queries? What inclusion and temporal composition boundary is actually required? No decision is implied about stored snapshots, dynamic materialisation, clinical completeness or authority over constituents.

<a id="g2-q07"></a>

### G2-Q07 — Order change responsibility and effect (C)

What establishes authority to request/authorise a modification or cancellation, what does the performing-system response commit to, and when may Harmonia report the change as effected? Coordination is established, but exact effect/eligibility criteria are not. No response, timeout, acknowledgement or altered representation is taken as proof of effect without its governing evidence.

## 7. Non-Authoritative Suggestions

| Suggestion / history | Potential purpose | Standing and preserved review history |
| :--- | :--- | :--- |
| **Episode Context — formerly D; SUPERSEDED by G2-D01** | Original suggestion: distinguish a sustained care/responsibility context from a bounded interaction and longitudinal record | Original review rationale retained as history: EC-IR5 showed contextual need but no precise Episode identity, boundary, owner or membership semantics; G2-Q04 sought meaning/responsibility because the owner name was insufficient. G2-D01 now promotes this suggestion into the independently meaningful Episode Information Concept with its approved definition. It is no longer a non-authoritative D suggestion. Responsibility and association-rule uncertainties remain. |
| **Responsibility Assumption Assertion** | If later required, distinguish an attributable account of responsibility actually assumed from referral intent and intake acceptance | G1 U1 retains extent, duration, effectiveness, relinquishment and transfer effects. G2 does not establish an information responsibility or lifecycle for this suggestion. Separate acceptance and traceable responsibility would be needed. |

Episode is now in the 19-Concept principal inventory as approved by G2-D01; the superseded D row is retained solely as review history. Responsibility Assumption Assertion alone remains a current non-authoritative D suggestion, outside the principal inventory and without a backlog or continuation instruction. At the G2-D01–D03 persistence boundary no Referral Disposition review had commenced; subsequent G2-D04 now resolves Q01 without promoting this responsibility suggestion.

## 8. Preserved G1 Uncertainty & Upstream Findings

| G1 uncertainty | Block 1 treatment |
| :--- | :--- |
| U1 — Referral responsibility effects / assessment-Order boundary | Retained in Referral/Order; no automatic care consequence, narrowed taxonomy or converted request. |
| U2 — Signing, finality, verification, authority and legal qualification | Local Order Process requirements preserved; no universal signature/finality Concept or consumer authority. |
| U3 — Medication/procedure information kinds and detailed relationships | Broader Order scope retained; later families not derived. |
| U4 — Delivery/Outcome representation, owner traces and kind/qualification authority | Package1 Concepts reused; no universal owner or asserted complete kind model. G2-D03 approves optional Encounter context and [0..*] delivery multiplicity only; further authority/evidence and the Referral association remain unreviewed. |
| U5 — Business acceptance/filing, acknowledgement dispositions and clinical incorporation | Dispatch/receipt/ACK/acceptance distinct; no incorporation model or invented acceptance criteria. |
| U6 — Referrer Actor eligibility, target qualifier/Role and authority effects | Practitioner fulfilment and ReferralSource context preserved; no exhaustive eligibility matrix or new receiving Role. |
| U7 — Capability Tier/ancestry/root/structural IDs | Established owners/Features used; no invented ancestry, IDs or aliases. |
| U8 — Behaviour/Process correspondence including ON_LEAVE | Independent scope/states preserved; no stage-equivalence mapping or universal sequence. |
| U9 — Discharge publication timing and unrelated task orderings | Before/upon departure remains unresolved; no alternative event model. To Do/Synthetic Task uncertainties untouched and unused. |
| U10 — Five unestablished Feature associations | Preserved; no replacement Feature association. Order Outcome remains Capability-scoped as already recorded. |
| U11 — Collaboration summary source Service/consumer dependency | Untouched/unused; no new dependency invented from collaborative use. |
| U12 — Detailed downstream representation allocation | No realised mapping or implementation assignment made. |
| U13 — AX-16 central-register standing | Untouched; no Concept or relationship derived from presumed central adoption. |

The G1-recorded AX-12 central/Domain01 discrepancy likewise remains unchanged and unused. No upstream source is reopened. Episode's prior semantic absence is superseded only by explicit G2-D01 approval; remaining responsibility/association details and unrelated gaps are not completed by inference. Domain01–03, central axioms, Foundation, G1 and the six Package1 family bodies remain unchanged.

## 9. Gate & Continuation Boundary

The four families remain a candidate package. This record persists G2-D01–D03 in §10 and subsequent G2-D04 in §11, resolving G2-Q01 only in this step; it approves no unrelated proposition or the package as a whole. No other G2 review question is commenced or further resolved in this persistence step. **G2 is not CLOSED; G3 is not commenced.**

Diagnostics, Medication, Procedure, Clinical Document, Problem / Condition / Care Need, Care Plan and Clinical Communication families are not derived. Existing references/examples are boundary evidence only. No FHIR resource/profile mapping, FSH, StructureDefinition, class, entity, schema/table, payload/API, integration message/topic, cache model, scheduling engine, order-entry design or application/implementation allocation is created.

Initial Block 1 derivation verification and diff are recorded in the [execution report](../../../../.junie/reports/2026-10-08-domain04-package2-g2-semantic-derivation-block1.md). That report and its diff remain unchanged historical execution evidence; they do not describe this subsequent decision-persistence step and are not architectural authority.

## 10. Approved Architectural Decisions

**Decision/persistence date:** 2026-10-08. **Authority:** explicit architectural review approval of G2-D01–D03. This is persistence of decisions already made, not a new derivation or an answer to unrelated questions. AX-17 governs every detail not established by the decisions.

<a id="g2-d01"></a>

### G2-D01 — Episode

**Status: RESOLVED — APPROVED, with explicit unresolved association/responsibility details.**

> **Episode is a longitudinal information context representing the treatment of one or more Problems or Conditions across a related series of Encounters, providing a coherent perspective for assurance, workflow and financial purposes.**

Episode is now an independently meaningful Information Concept. The former non-authoritative Episode Context suggestion is promoted to Episode with this definition; its original rationale and standing remain historical in §7.

- Episode is not merely a Collection of Encounters. Encounter association contributes to longitudinal treatment context but does not define its complete meaning.
- Episode relates one or more Problems/Conditions [1..*] to a longitudinal course of treatment and may relate to multiple Encounters [0..*]. Not every Encounter for a healthcare subject necessarily belongs to a particular Episode.
- Encounter-association mechanisms/rules, detailed identity, originating authority, Harmonia information-responsibility allocation and temporal/lifecycle semantics remain unresolved. The decision allocates no reverse cardinality, upstream owner, Feature or Service.
- No Episode ownership of related Encounters is inferred; the association is not implementation containment. Assurance/workflow/financial perspective is not an allocation of authority for those activities.
- This architecturally approved meaning stands independently of representation. No FHIR EpisodeOfCare derivation, mapping or equivalence is established.

**Review consequence:** G2-Q04 no longer asks whether Episode exists or what its basic meaning is; only genuinely unresolved details remain. No Problems/Conditions family is derived.

<a id="g2-d02"></a>

### G2-D02 — Appointment and Encounter

**Status: RESOLVED — APPROVED, with explicit unresolved cardinality/qualification details.**

> **An Appointment may provide the prospective scheduling context for an Encounter. Where an Encounter occurs without a pre-existing Appointment, such as an emergency or walk-in interaction, an Appointment may be created retrospectively to represent the scheduling/administrative context associated with that Encounter.**

Appointment and Encounter are distinct Information Concepts. Their relationship is principally scheduling context, not mandatory lifecycle progression.

```text
Appointment ≠ Encounter
Appointment existence does not imply Encounter occurred
Encounter does not prove prospective Appointment existed
Retrospective Appointment establishment is not evidence of prospective scheduling
```

- No Appointment → Encounter cardinality is established. Cancellation, rescheduling, recurring appointments, group appointments and other scheduling contexts remain unreviewed for multiplicity.
- Prospective and retrospective context do not create separate Appointment Concepts. Scheduling-origin/context qualification is identified as a property/qualification for later review, without a defined representation or mandatory property structure.
- The decision does not assign the originator/authority for retrospective creation or transfer external booking authority to Harmonia.

**Review consequence:** G2-Q02 reflects approved prospective/retrospective meaning and retains only unreviewed qualification/classification/authority/cardinality details. G2-Q03 preserves actual care-interaction meaning while retaining administrative tracking/Assembly questions.

<a id="g2-d03"></a>

### G2-D03 — Encounter and HealthcareServiceDelivery

**Status: RESOLVED — APPROVED, with explicit unresolved further authority/evidence details.**

> **An Encounter may provide the care-interaction context within which zero or more Healthcare Service Deliveries occur. A Healthcare Service Delivery may occur without an associated Encounter.**

Encounter and HealthcareServiceDelivery are distinct Information Concepts. A single Encounter may contextualise multiple HealthcareServiceDelivery instances [0..*], and delivery may exist independently of Encounter context.

- Encounter ≠ HealthcareServiceDelivery. Encounter existence does not establish that any service was delivered.
- The relationship is optional contextual association, not mandatory lifecycle progression, identity, containment or a component/state of Encounter.
- The approved [0..*] multiplicity concerns deliveries contextualised by an Encounter; no further reverse cardinality is inferred.
- Package1's DeliverableHealthcareService → optional Order → HealthcareServiceDelivery → ServiceOutcome semantics remain unchanged. Encounter is not made mandatory anywhere in that chain.
- Further source/correlation qualification and universal delivery/outcome ownership are not established by this decision.

**Review consequence:** G2-Q05's Encounter/delivery existence, basic meaning and multiplicity questions are resolved. Further qualifications and the Referral portion remain unreviewed; no Referral Disposition review is commenced.

### 10.1 Combined Semantic View and Persistence Boundary

```text
Appointment — scheduling / planned interaction context; may be established retrospectively for an unscheduled Encounter
Encounter — actual care interaction/context
HealthcareServiceDelivery — actual delivery/performance of a Healthcare Service, as represented by Package1 delivery information
Episode — longitudinal treatment context across Problems/Conditions and related Encounters for assurance, workflow and financial perspective

Problem / Condition [1..*] → contextualised through Episode → related Encounter [0..*]
Appointment → may provide scheduling context for Encounter
Encounter → may contextualise HealthcareServiceDelivery [0..*]
HealthcareServiceDelivery → may exist without Encounter
```

These expressions communicate semantic associations, not implementation containment. The Concepts remain distinct even if downstream representations later correlate or combine them. Information about delivery remains distinct from the represented real-world performance; no Harmonia clinical-performance responsibility is inferred.

Only the review record, affected Appointment/Episode–Encounter candidate family text and the stale Episode description in the family index are reconciled. G2-Q01, G2-Q06 and G2-Q07 are untouched; the Referral part of G2-Q05 is not answered. No upstream, Foundation, Package1, historical execution report/diff, later family or downstream representation is modified. G1 remains CLOSED; G2 remains open.

## 11. Subsequent Approved Architectural Decision

**Decision/persistence date:** 2026-10-08. **Authority:** explicit architectural review approval of G2-D04 resolving G2-Q01. Section 10 remains the unchanged historical G2-D01–D03 record; its then-unreviewed Q01 statements describe that earlier boundary. AX-17 governs every detail not established by this decision.

<a id="g2-d04"></a>

### G2-D04 — Referral Outcome

**Status: RESOLVED — APPROVED. G2-Q01 is resolved; G2 remains open.**

Referral Disposition is **not retained as an independent Information Concept**. The assurance and governance requirement previously represented by that B candidate is instead represented by **Referral Outcome**.

> **Referral Outcome is the governed information concerning what ultimately became of a Referral, sufficient to support assurance, governance and traceability over the Referral and its progression.**

**Semantic classification:** Outcome; provenance-bearing information with Assertion characteristics where a source reports what became of the Referral. This is not ServiceOutcome equivalence or a downstream representation category.

Referral Outcome exists principally to support the ability to account for Referrals within the applicable governance/assurance scope. Where established and appropriate, it may include the resulting outcome, basis/reason, source/originator, authority, temporal context and relationships to relevant resulting activity or information. No canonical Referral Outcome value set is established.

```text
Referral ≠ Referral Progression Event ≠ Referral Outcome
Referral Outcome ≠ Responsibility Assumption ≠ Transfer of Care ≠ HealthcareServiceDelivery ≠ ServiceOutcome
Referral information semantics ≠ Referral Administration workflow progression
```

Referral Outcome may reference or relate to those Concepts where appropriate; its existence establishes none of them. Recording that a Referral was accepted, progressed or otherwise reached an outcome does not itself prove that care responsibility was actually assumed or transferred. G1 K1's intent/disposition/responsibility/Transfer of Care distinctions remain intact. Outcome is not equated with acceptance.

**Referral Progression Event:** Retained as historical information concerning significant occurrences in the administrative/business progression of a Referral. Not every Event represents a Referral Outcome, and the historical progression record is not collapsed into the final/current Outcome.

**Assurance / governance coverage — RF-IR5:** Harmonia must support assurance that Referrals within the applicable governance scope can be accounted for through sufficient progression and outcome information to establish what became of them. This requirement is applied to established Referral Administration / Manage Referral Progression / Referral Status & Outcome Service responsibility without allocating a new owner, Feature, Service or Collection.

```text
Referral population within governance scope → progression/outcome evidence → assurance coverage
```

An inability to establish what became of a Referral is itself an assurance gap. No Referral Outcome is manufactured to make coverage appear complete. Where the outcome is not known, its absence is preserved explicitly.

**Workflow closure / finalisation:** This belongs to the progression of the Business Process / Referral Administration activity, not intrinsic Referral disposition information. The workflow may reach a state in which no further Referral Administration activity is required. This does not require a Referral Finalisation or Referral Closure Information Concept, a terminal state intrinsic to Referral, or Referral Outcome to double as workflow state. No workflow terminal states are defined and the Business Process state machine is not reproduced as the Information Concept lifecycle.

**Authority, temporal meaning and provenance:** Harmonia governs the outcome information for assurance and traceability; it does not infer substantive authority from recording or possessing it. Outcome may require provenance sufficient to establish what outcome was understood, source/originator, authority where relevant, when established/effective, and rationale/qualification where relevant. These are not all universally mandatory attributes. Detailed governance-scope criteria, evidence sufficiency, authority eligibility and contextual provenance/temporal qualification remain unestablished where existing architecture supplies no detail.

**Review history and consequence:** Q01 retains its original B question. The former Referral Disposition candidate proposed an independent Assertion for an attributable decision/reported disposition, with acceptance, decline, redirection and context-specific waitlisting examples. Architectural review rejects that separate Concept and reframes its assurance/governance purpose as Referral Outcome. Recipient-decision meanings and the canonical upstream Referral Disposition Log label are not erased. Q01 is resolved, without answering acceptance's clinical responsibility effects or the precise Referral–delivery/outcome relationships under Q05. Responsibility Assumption Assertion remains a non-authoritative D suggestion.

**Persistence boundary:** Only the Referral family, this review record and the necessary Referral navigation text are reconciled. Q02–Q07 and the complete G2-D01–D03 record remain unchanged. Frozen upstream architecture, Foundation, Package1 and initial historical report/diff are not modified. No other G2 question, G3, later Package2 family, FHIR mapping or downstream representation is commenced. G1 remains CLOSED; G2 is not CLOSED.
