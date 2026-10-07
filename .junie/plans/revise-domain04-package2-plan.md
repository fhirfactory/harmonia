---
sessionId: session-261007-092143-1d0s
---

# Domain04 — Package 2 Delivery Plan and Technical Design Proposal

**Package:** Clinical & Service Administration Information Families

**Status:** REVISED PROPOSAL — awaiting architectural review; execution is not authorised.

**Revision date:** 2026-10-07

**Repository reference:** `808e73a1d871050f29c64c6780032ec29d4b7834`; observations refer to working files inspected on the revision date.

**Supersession:** This proposal replaces the execution instructions and predetermined models in `domain04-package2-clinical-information-families.md` and the earlier version of this revision draft. The former remains a historical proposal, not an executable instruction.

## 1. Purpose, scope and review boundary

Package 2 will derive abstract Information Architecture forward from the frozen Business Architecture. Its governing question is: **What information must be in play for the enterprise responsibilities and behaviours established in Domain03 to occur, and what does that information mean?**

Business Architecture tells us what the enterprise does. Information Architecture identifies, quantifies and qualifies the information that must therefore be understood, managed, referenced, contextualised, assembled, exchanged or governed. Subsequent architectural layers determine concrete representation, application behaviour, integration contracts, persistence and technological realisation.

The documentation serves three purposes:

1. **Engineering input:** provide precise semantic boundaries, responsibilities and guardrails against which subsequent design and development can be assessed, without prescribing representations.
2. **Context for AI-assisted development:** supply authoritative terminology, evidence, constraints and decision history so that an AI peer reasons from the architecture, identifies gaps and challenges contradictions openly, rather than substituting a familiar model.
3. **Education and demonstration:** preserve the derivation through Motivation → Strategy → Business Architecture → Information Architecture → Application Architecture → Integration Architecture → Technology Architecture → Implementation. TOGAF supports separation and progression; ArchiMate supports concepts and relationships. Neither requires artefacts without engineering value. Visible derivation supports assurance, maintainability, consistency, development efficiency, reduced rework and cost optimisation.

### 1.1 Agreed investigation scope

Retain eleven subject areas and their proposed documentation destinations:

| Subject area | Proposed future file under `docs/markdown/04-information-architecture/information-families/` |
| :--- | :--- |
| Referral | `referral.md` |
| Appointment / Scheduling Context | `appointment-scheduling.md` |
| Episode / Encounter | `episode-encounter.md` |
| Order | `order.md` |
| Diagnostic Information | `diagnostics.md` |
| Medication Information | `medication.md` |
| Procedure | `procedure.md` |
| Clinical Document | `clinical-document.md` |
| Problem / Condition / Care Need | `problem-condition-care-need.md` |
| Goal / Care Plan | `care-plan.md` |
| Clinical Communication | `clinical-communication.md` |

These are investigation boundaries, not an established taxonomy or a promise that every candidate becomes canonical. A proposed regrouping must be explained and returned for review before changing these document boundaries.

### 1.2 Planning-only boundary

This task produces only this revised plan and documentation technical design. Reading frozen sources to establish an investigation agenda does not execute the semantic derivation. No Package 2 concept definitions, family documents, semantic diagrams or architectural decisions are established here.

Domains01–03, Domain04 Foundation and Package 1 remain frozen. No upstream traceability changes, FHIR mappings, application Data Objects, classes, schemas, APIs or persistence models are permitted. Operational work, facility logistics, collaboration-space models, detailed alerting, security-policy design and full longitudinal-record assembly remain outside Package 2; their frozen behaviours may be examined only for a relevant semantic dependency or boundary.

This is not a convergence/runtime implementation step. The master runtime plan has been consulted for scope separation; its milestones, accepted outcomes and next step are unchanged. The attached Kleio persistence report is a historical implementation artefact, not derivation evidence for clinical Information Concepts. Reports under `.junie/reports/` are execution records, not architectural authority.

**Gate G0:** return this proposal for architectural review and stop. Review acceptance must precede any Package 2 execution; completion of this planning task does not imply acceptance.

## 2. Authority and inspected source register

Repository axioms and `AGENTS.md` govern all work. Frozen Domains01–03 constrain the investigation; Domain04 Foundation supplies the metamodel, relationship model and governance rules; Package 1 supplies reusable concepts with unchanged meanings. The user's revision instructions supply additional explicit non-equivalence constraints and the planning-only boundary. Plans and historical reports cannot repair or override frozen sources.

The following codes are navigation shorthand, not new architectural identifiers. Each subject brief specifies exact sections to examine.

| Code | Frozen source and role |
| :--- | :--- |
| AX | [Architectural Axioms](../../docs/architectural-axioms.md) |
| T | [Domain02 Business Enabling Capabilities](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md): exact Feature identifiers/names; missing hierarchy tiers must not be reconstructed |
| M | [Domain03 metamodel](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md), §§2–5: capability, function, service, process and responsibility boundaries |
| B | [Domain03 Service Administration](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md) |
| D | [Domain03 Service Delivery](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md) |
| H | [Domain03 Health Service Operations](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md) |
| E | [Domain03 Intrinsic / Shared Enablement](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md) |
| P | [Principal Business Processes](../../docs/markdown/03-business-architecture/processes/business-processes.md) |
| C | [Business Collaborations](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md) |
| I | [Business Interactions](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md) |
| O | [Business Information Responsibility & Ownership Model](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md), §2: authoritative ownership matrix |
| X | [Cross-Capability Dependencies](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md) |
| R | [Business Roles](../../docs/markdown/03-business-architecture/actors-roles/roles.md), with [Actors](../../docs/markdown/03-business-architecture/actors-roles/actors.md) where participant kind matters |
| F-M | [Domain04 metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md) |
| F-R | [Information Relationships](../../docs/markdown/04-information-architecture/patterns/information-relationships.md) |
| F-C | [Containment and Collections](../../docs/markdown/04-information-architecture/patterns/containment-and-collections.md) |
| F-P | [Definition-to-Accountability](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md) |
| F-A | [Authority, Custody and Provenance](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md) |
| F-L | [Information Lifecycle](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md) |
| F-V | [Assemblies and Views](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md) |
| F-G | [Sixteen Modelling Guardrails](../../docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md) |
| F-T | [Domain03 Traceability Framework](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md): governing method; representative names require checking against Domain03 |

### 2.1 Material axioms and consistency of the method

| Axiom | Consequence for this proposal |
| :--- | :--- |
| AX-01 — Health-Information Centric | Derive information required by health enterprise behaviour, without turning Package 2 into a generic data catalogue. |
| AX-02, AX-03 — Boundary standards; native models | Separate meaning from representation. Abstract concepts do not prescribe replacement representations for standards-defined objects. Native-model decisions remain for subsequent layers; no parallel implementation model is proposed. |
| AX-04 — Harmonia Owns the Semantics | Business meaning and explicit responsibility constrain later machinery; engines and schemas cannot determine the investigation's answers. |
| AX-05 — Active and Authoritative Durable State | Projection, indexing, synchronisation and custody do not establish durable truth or originating clinical authority. No runtime design is introduced. |
| AX-06 — Explicit Information Authority | Distinguish authority, credibility and qualification from management responsibility, delivery success and persistence success. |
| AX-07, AX-08 — Governed Operations and Meaningful Evidence | Investigate significant qualification, correction, acceptance and acknowledgement evidence without designing security mechanisms or treating technical activity as clinical attestation. |
| AX-13 — Management Boundary | Communication evidence may describe an external recipient's response; it does not extend Harmonia control into the recipient system. |
| AX-14 — Semantic Distinctions | Retain business-significant distinctions regardless of shared downstream representations. |
| AX-15 — Preserve Uncertainty | Preserve unknown business outcomes. Use the same evidence discipline in modelling: unanswered questions remain open rather than acquiring invented definitions. |

No axiom change is proposed. Material tensions in frozen sources are recorded in §8, with consequences and review gates.

## 3. Four categories of architectural knowledge

### 3.1 Frozen semantic invariants

The revision instruction explicitly freezes the following non-equivalences:

- Business Information Concept ≠ Application Data Object ≠ FHIR Resource ≠ Persistence Entity ≠ API Payload; the Foundation also distinguishes Java Class and Database Table.
- Referral ≠ Order; Appointment ≠ Encounter; Encounter ≠ `HealthcareServiceDelivery`; Order ≠ Result.
- Medication Order ≠ Dispense ≠ Administration; Procedure Activity ≠ Procedure Documentation.
- Clinical Document ≠ Clinical Fact; Condition ≠ Problem ≠ Care Need.
- Goal ≠ Outcome; Care Plan ≠ Activity; Clinical Communication ≠ Clinical Record.

These guardrails do not supply complete definitions or make every term canonical. The Clinical Document distinction does not establish Clinical Fact as a concept. This plan does not attribute the complete list to a frozen file where it is absent.

The Foundation also establishes business meaning before representation, bounded ownership, granular assertion/relationship authority, concept-specific lifecycles, non-transfer of authority through aggregation, forward relationships, role separation, containment/membership separation and meaningful n-ary associations. Generic examples and candidate assemblies are not complete Package 2 models.

Reuse Package 1 concepts by reference to their definitions, without semantic redefinition:

| Package 1 source | Reuse constraint |
| :--- | :--- |
| [Person and Healthcare Subject](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md) | Reuse `Person`, qualified identity/identifiers and `Healthcare Subject Context`; no patient identity recreated within referral or encounter. |
| [Practitioner](../../docs/markdown/04-information-architecture/information-families/practitioner.md) | Reuse identity, registration, privileges, role bindings and `Endpoint`; participation/job title does not establish clinical authority. |
| [Organisation](../../docs/markdown/04-information-architecture/information-families/organisation.md) | Reuse organisation identity/units; `Service Provider` is a business capacity, not a replacement entity. |
| [Healthcare Location](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md) | Reuse location/care-place definitions; association is distinct from operational occupancy/turnover. |
| [Healthcare Service](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md) | Reuse `OfferedHealthcareService`, `DeliverableHealthcareService`, `HealthcareServiceDelivery`, `ServiceOutcome` and reference/contextual `AssuredHealthcareService`; Order is an associated direction mechanism, not a mandatory service stage. |
| [Device](../../docs/markdown/04-information-architecture/information-families/device.md) | Reuse definition/instance/association semantics; device association does not confer ownership of physiological information. |

### 3.2 Candidate Information Concepts

Candidates are investigative hypotheses prompted by exact behaviour or the agreed scope. The future candidate register must record prompting source, tentative label, required meaning, neighbouring concepts, alternatives and missing evidence.

Permitted dispositions: retain as justified; distinguish several meanings; express through an existing concept, qualification or relationship; retain as reference/contextual with demonstrated necessity; defer; reject as unsupported; or record an architectural gap. Consolidation requires semantic evidence and cannot override a frozen non-equivalence. No number of concepts is prescribed.

### 3.3 Explicit modelling questions

Each question records the uncertainty, frozen constraints, evidence to examine, alternative interpretations, counterexamples and what would justify a conclusion. The briefs in §7 set an agenda without answering it.

### 3.4 Derived architectural decisions

No new Package 2 decision is made here. During authorised execution, a decision may be proposed only after showing:

**Business Responsibility / Behaviour → Information Requirement → Semantic Distinction → Information Concept / Relationship → Qualification / Responsibility / Authority / Provenance / Temporal and Lifecycle Semantics.**

A supported conventional interpretation, a supported Harmonia-specific interpretation and an unresolved question are legitimate results. Familiarity with healthcare models is not evidence for any of them.

## 4. Forward derivation method and documentation technical design

### 4.1 Business evidence before concept selection

1. Read scoped capability, Feature, function, service, process, collaboration and interaction sources. Record exact wording, path, section, element type, boundaries and negative responsibility statements.
2. Write information requirements before selecting concept labels: what must participants know, distinguish, understand, reference, relate, manage or govern? Explain the consequence if that information is absent or indistinguishable.
3. Quantify independently meaningful information. Test distinctions among intentions, occurrences, assertions, associations, outputs and compositions. One requirement can support several concepts; several behaviours can require one meaning. Count conceptual distinctions, not records or physical multiplicities.
4. Investigate candidate meanings and alternatives against requirements and frozen constraints. Test identity, authority, qualification, temporal context and lifecycle where meaningful; no universal set of characteristics is required.
5. Test relationships and responsibility separately. Acting on information does not establish ownership. Confirm contextual necessity before retaining externally authoritative information; absent ownership and necessity, record a gap or reject the candidate.
6. Propose justified semantic decisions, rejected alternatives and remaining uncertainty. Preserve the reasoning in future family documents. Only then draw semantic diagrams.

The reading direction is Capability / Feature → Function / Process / Service → Collaboration / Interaction → Information Requirement → Concept / Relationship. It is not a compulsory chain through every element: a discrete interaction need not belong to a collaboration, and a stateless function need not have a process. Mark absent/inapplicable links instead of manufacturing them.

### 4.2 Reasoning records, not data schemas

These proposed documentation formats organise reasoning; their headings are not application attributes or a realised information schema. Planning-local references such as `P2-IR-…`, `P2-Q-…` and `P2-DEC-…` must never be presented as upstream identifiers.

| Record | Required reasoning |
| :--- | :--- |
| Business evidence | Exact quotation/bounded excerpt, file/section, canonical label/type, scope, responsibility and non-ownership statements. |
| Information requirement | Business action/participant need; what must be known/distinguished/related; why; evidence; management/reference need; uncertainty. |
| Candidate analysis | Requirement, tentative meaning, independent necessity, neighbours, Package 1 reuse, alternative decomposition, evidence and disposition. |
| Qualification | Meaning/category; responsibility; originating/attesting authority; stewardship/custody/consumption separately; provenance; qualification; effective/occurrence/assertion context; lifecycle where meaningful. |
| Relationship | Business reason/meaning; source/target/type; meaningful roles/qualifications; evidence/provenance; time; n-ary needs; cardinality evidence or explicitly unspecified cardinality. |
| Decision | Evidence/requirement references; reasoning; alternatives; boundary; responsibility/authority; qualifications/limits; exclusions; unresolved dependencies; review status. |
| Gap / conflict | Competing sources; missing evidence; consequence; affected decisions; review question; no frozen-source repair. |

An eventual Information Set must justify its grouping purpose, membership/assembly criteria, composition governance, responsibility and constituent provenance. An upstream dossier, ledger or register does not mechanically require a new canonical collection or assembly.

### 4.3 Governance, relationships and pattern tests

Keep **Architectural Responsibility ≠ Originating Information Authority ≠ Information Stewardship ≠ Information Custody ≠ Information Consumption**. Dimensions may combine without becoming synonyms: Harmonia can govern correlation/history while preserving external assertion authority. Authority may attach to assertions/relationships rather than a whole composite. Do not infer it from receipt, indexing or persistence.

Use F-R without redesign. Source, target and type are fundamental; roles, qualification, effective period, authority/evidence, primacy, validity/status and provenance are available where meaningful. Investigate contextual, temporal, authoritative, evidentiary, fulfilment-oriented, structural and classificatory meaning. Author forward; avoid duplicate inverse authority. Preserve meaningful n-ary associations. Cardinality requires business evidence or a frozen rule; otherwise leave it unspecified. Connectivity is not an acceptance criterion.

`Requester`, `Performer` and `Referrer` occur in R. They must not be declared universally to be only relationship roles. A similarly named local relationship role neither creates nor proves a Business Role. Check context and record ambiguities (§8).

Test **Definition → Contextualisation / Binding → Fulfilment → Outcome → Accountability** after requirements are recorded. Classify applicability as full, partial, concept-specific, not applicable, or unresolved pending evidence, with reasons. Elements are semantic classifications, not mandatory lifecycle states or boxes. Do not create Medication Definition, Procedure Definition, outcome or accountability concepts to complete the pattern. The Foundation's phrase “stages may be collapsed” does not permit erasing independently meaningful distinctions.

### 4.4 Standards discipline

FHIR, HL7, LOINC, SNOMED CT, ICD, MBS, DRG, EMR/PAS/LIS/RIS/PACS models, MLLP, HTTP, Java, Spring, JPA, PostgreSQL, schemas and payloads cannot supply canonical semantics. Frozen sources mention some of these: quote faithfully where relevant, identify contextual/representation language, and derive requirements independently. Do not erase source text or import a standard's model.

Later comparison may validate convergence or reveal differences; it cannot retrospectively replace derivation. This proposal contains no mappings. Any change suggested by comparison requires renewed business evidence and semantic review.

## 5. Revalidated Domain03 responsibility terminology

All ten questioned administration capability names occur in B; most questioned asset names occur verbatim in O. They cannot be treated as invented simply because B uses other labels. Combining names into an uncited upstream concept would invent traceability.

| Subject | Exact capability / asset in O §2 | Exact responsibility labels in B |
| :--- | :--- | :--- |
| Referral | `Referral Administration` / `Referral Master & Triage Ledger` | §3: `Referral Master Record`, `Triage Decision Ledger`, `Referral Disposition Log` |
| Appointment / Scheduling | `Scheduling Administration` / `Appointment Synchronisation State` | §4: `Appointment Event History`, `Schedule Synchronisation State` |
| Episode / Encounter | `Episode & Encounter Admin` / `Encounter Master & Movement Ledger` | §5 capability `Episode & Encounter Administration`; `Encounter Master Record`, `Encounter State Transition Log`, `Encounter Care-Place Movement Ledger` |
| Order | `Order Administration` / `Clinical Order & Closed-Loop Matrix` | §6: `Clinical Order Master Record`, `Closed-Loop Tracking Ledger`, `Order-Result Correlation Matrix` |
| Diagnostic | `Diagnostic Administration` / `Diagnostic Correlation & Linkage Registry` | §7: `Diagnostic Request-Report Linkage Registry`, `Result Distribution Ledger` |
| Medication | `Medication Administration` / `Medication Event Timeline Matrix` | §8: `Medication Event Timeline`, `Prescription-Dispense-Administration Correlation Matrix` |
| Procedure | `Procedure Administration` / `Procedure Request & Documentation Index` | §9: `Procedure Request Register`, `Procedural Documentation Index` |
| Clinical Document | `Clinical Record Administration` / `Clinical Document Registry & Lifecycle State` | §10: `Clinical Document Registry`, `Document Version History`, `Document Lifecycle State Register` |
| Problem / Condition / Care Need | `Patient Clinical Record` / `Governed Longitudinal Health Record (LHR)`; `Care Plan Administration` / `Shared Care Plan & Clinical Goal Registry` for relevant associations | E §2.1: `Governed Longitudinal Health Record (LHR)`, `Canonical Clinical Timeline`, `Active Problem List`, `Immutable Historical Record Store`; B §11 for care-plan associations |
| Goal / Care Plan | `Care Plan Administration` / `Shared Care Plan & Clinical Goal Registry` | §11: `Shared Care Plan Registry`, `Clinical Goal Ledger`, `Care Plan Revision History` |
| Clinical Communication | `Clinical Communication Admin` / `Clinical Message Dispatch & Audit Register` | §12 capability `Clinical Communication Administration`; `Clinical Message Dispatch Log`, `Business Delivery Acknowledgement Ledger`, `Medico-Legal Transmission Audit Register` |

Use each label exactly with its source/type. Shortened capability names in O are source variants, not silently normalised. Determine whether differences represent summary/detail granularity or competing claims; unresolved differences constrain decisions. The ownership matrix does not make every supplied assertion Harmonia-originated.

O has no dedicated Condition, Care Need, Episode, Medication Definition, Procedure Definition or Clinical Fact responsibility row. This does not prove the information unnecessary. Examine behaviour, reference/contextual necessity and governance before deciding.

## 6. Common acceptance criteria for every subject

J1–J8 apply to all briefs and proposed concepts/significant relationships:

1. **J1 — Necessity:** exact business evidence supports a requirement and explains what would be lost without the meaning.
2. **J2 — Distinction:** justify independent meaning, reuse or rejection against neighbours/alternatives; no standards-based splitting or representation-based merging.
3. **J3 — Responsibility:** support management through O and relevant functions/processes, or demonstrate reference/contextual necessity for a traced responsibility. Consumption alone is insufficient.
4. **J4 — Governance:** distinguish authority/stewardship/custody/consumption; preserve meaningful provenance, qualifications and uncertainty.
5. **J5 — Time/lifecycle:** justify effective/occurrence/assertion context and lifecycle where meaningful; process states are not universal concept states.
6. **J6 — Relationships:** justify semantic purpose, direction, qualification, authority, cardinality, containment and n-ary associations.
7. **J7 — Conformance:** preserve frozen semantics, Package 1 reuse, sixteen guardrails, pattern non-compulsion and abstraction boundaries; report conflicts.
8. **J8 — Reviewability:** retain reasoning, candidate dispositions, open questions and exclusions; diagrams follow decisions and make no stronger claim.

## 7. Subject-area investigation briefs

These are planning agendas, not executed derivations. “Requirements to derive” identifies questions to turn into evidenced requirements. Candidate labels imply no definition, ownership, category or lifecycle. Collaboration/interaction names are verified entries; family applicability remains to be investigated unless directly established. Missing links are stated instead of invented.

### 7.1 Referral

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | B §3; O `Referral Administration` row; P §3.3; X §2 referral dependencies; I §2.2; C §§3.1, 3.4; R §§2.2, 3.2; T Service Administration §2. |
| 2. Capability / Features | `Business Enabling Capability: Referral Administration`; `Referral Document Ingestion` (`FEAT-SA-03`), `Referral Supporting Information` (`FEAT-SA-04`), `Referral Status & Outcome Tracking` (`FEAT-SA-05`). |
| 3. Functions / Services / Processes | `Receive Referral` / `Referral Submission Service`; `Assemble Referral Context` / `Referral Context Query`; `Manage Referral Progression` / `Referral Status & Outcome Service`; `Referral Progression Process`. |
| 4. Collaborations / Interactions | Examine `Service Referral`, `Service Response`, `Transfer of Care`, `Service Coordination`; `Patient Collaboration`, `Care-Team Collaboration`. The catalogue does not make every referral an instance of Transfer of Care. |
| 5. Requirements to derive | What must be known to receive, assess, triage, accept/decline/redirect, progress and report a referral? What supporting information must be related without acquiring its authority? |
| 6. Frozen constraints | Referral ≠ Order; Package 1 identity/provider/service reuse; F-A, F-R and F-V authority preservation. Narrow frozen Referral wording is a review issue (§8, K1), not a complete definition to copy. |
| 7. Candidates | Referral; triage decision; referral disposition; supporting referral context. Test whether decisions/dispositions are independent concepts, qualifications or relationships, and context an Information Set/assembly. |
| 8. Modelling questions | What information constitutes a Referral? How do assessment, requested care, acceptance and any ensuing responsibility differ? Which distinction from Order follows from the behaviour, and what does the narrow wording leave unresolved? |
| 9. Relationships to investigate | Referred subject; referring/receiving participants; requested service; supporting assertions/documents; disposition and scheduling context. Investigate meaning/direction, not a prebuilt referral graph. |
| 10. Responsibility / authority | Trace progression/context responsibility to O and B. Who originates the request, supporting assertions and triage decision? Who governs their qualification? Assembly or intake must not re-author them. |
| 11. Provenance / qualification / time | Request origin, triage basis, supporting-source lineage, priority qualification and changing dispositions; distinguish requested/effective timing from assertion/receipt timing where required. |
| 12. Lifecycle | Examine P §3.3 alongside B's shorter progression. Determine which information progresses and which assertions retain meaning independently; do not copy the process onto every candidate. |
| 13. Patterns to test | F-P applicability; F-V supporting-context assembly; F-R qualified participation/correlation. No manufactured referral definition or accountability concept. |
| 14. Leakage risk | Referral-as-standard-resource, mandatory dossier composition, or assumed longitudinal care transfer. Preserve exact upstream wording while exposing its disputed scope. |
| 15. Decision justification | J1–J8 plus an evidenced Referral/Order distinction and disposition of K1. No final definition dependent on unresolved frozen wording. |

### 7.2 Appointment / Scheduling Context

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | B §4; O `Scheduling Administration` and `Calendar Management` rows; E §2.10; P §§3.4, 4.8 as boundary evidence; H §2.1; I §2.2; C §§3.4, 3.6; T Service Administration §3 and Intrinsic Enablement §10. |
| 2. Capability / Features | `Scheduling Administration`: `Appointment Notification Ingestion` (`FEAT-SA-06`), `Appointment Status Synchronization` (`FEAT-SA-07`); `Calendar Management`: `Schedule Projection Integration` (`FEAT-ISE-24`), `Temporal Event Correlation` (`FEAT-ISE-25`). |
| 3. Functions / Services / Processes | `Receive Appointment Notification` / `Appointment Notification Ingress`; `Synchronise Appointment Status` / `Appointment Schedule Query`; `Project Operational Schedule` / `Unified Calendar Projection Query`; `Correlate Activity with Temporal Context` / `Temporal Context Resolution`. No dedicated Appointment process is named in P. |
| 4. Collaborations / Interactions | Test `Service Response`, `Service Coordination`, `Service Outcome`; `Care-Team Collaboration`, `Operational Collaboration`. Clinic queue/encounter progression is boundary evidence, not an invented scheduling process. |
| 5. Requirements to derive | What must be understood to receive changed booking milestones, synchronise status and expose consolidated schedules? What must be retained to distinguish source booking information from Harmonia projection/correlation? |
| 6. Frozen constraints | Appointment ≠ Encounter; B §4 and E §2.10 leave booking/slot authority external. Package 1 availability/service bindings do not automatically become patient appointment bookings. |
| 7. Candidates | Appointment as externally authoritative information; booking milestone; synchronised scheduling context; temporal correlation; schedule projection. Candidate management status remains open. |
| 8. Modelling questions | Is Appointment independently required to be understood or referenced? Which information does Harmonia govern? Is there any necessary relationship to Encounter or delivery? Does cancellation affect only a booking, correlation or another meaning? |
| 9. Relationships to investigate | Subject/service/participant/time associations; source booking to synchronised context; optional encounter/activity correlation. No necessary “becomes”, “creates” or “realised as” relationship to Encounter. |
| 10. Responsibility / authority | Separate source booking authority from Scheduling Administration synchronisation and Calendar Management projection responsibilities; ask who may assert or correct correlations. |
| 11. Provenance / qualification / time | Booking-source lineage; notification and projection currency; requested/planned time versus received/asserted time; cancellation/rescheduling and conflicting source reports. |
| 12. Lifecycle | Examine source milestones and projection/correlation progression independently. No imported Appointment lifecycle or guaranteed Encounter outcome. |
| 13. Patterns to test | F-P applicability is open; F-V projection, F-R temporal correlation and F-C grouping versus containment. |
| 14. Leakage risk | Host scheduler slots, standard Appointment resources or familiar appointment-to-encounter workflows driving semantics or Harmonia ownership. |
| 15. Decision justification | J1–J8 plus explicit scope of external booking authority and Harmonia-managed projection/correlation. Any encounter relationship requires specific evidence and justified cardinality. |

### 7.3 Episode / Encounter

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | B §5; O `Episode & Encounter Admin` row; P §3.4; X encounter dependencies; D §§2.1, 2.4, 2.8, 2.10; I §2.2; C §3.4; F-V candidate Encounter Context; T Service Administration §4. |
| 2. Capability / Features | `Business Enabling Capability: Episode & Encounter Administration`: `Encounter Context Creation` (`FEAT-SA-08`), `Encounter State Progression` (`FEAT-SA-09`), `Encounter Bed/Location Association` (`FEAT-SA-10`). No distinct Episode Feature is established in these sections. |
| 3. Functions / Services / Processes | `Establish Encounter Context` / `Encounter Context Resolution`; `Progress Encounter State` / `Encounter Lifecycle Event Notification`; `Maintain Encounter Care-Place Association` / `Encounter Location History Query`; `Encounter Lifecycle Process`. |
| 4. Collaborations / Interactions | Examine `Care-Team Collaboration`, `Service Coordination`, `Transfer of Care`, `Service Outcome`; contextual enablement functions in D. No explicit Episode-specific interaction/process has been identified. |
| 5. Requirements to derive | What must be distinguished to establish context, correlate participants, progress an encounter and preserve care-place movements? Do episode references require meaning beyond encounter context? |
| 6. Frozen constraints | Appointment ≠ Encounter ≠ `HealthcareServiceDelivery`; reuse subject/practitioner/location/service meanings. Candidate assembly examples do not decide Episode status or ownership of constituent clinical information. |
| 7. Candidates | Encounter; Encounter Context; encounter progression information; care-place association; Episode. Test independent concept, contextual grouping, relationship or unresolved status for Episode. |
| 8. Modelling questions | What is Episode? How does it differ from Encounter? Is it independently governed? What business evidence supports its responsibility, authority, lifecycle and any Episode/Encounter cardinality? |
| 9. Relationships to investigate | Subject/provider/participant associations; qualified temporal care-place binding; contextual linkage to delivery; possible Episode/Encounter association. Do not assume containment, one-to-many or a shared lifecycle. |
| 10. Responsibility / authority | Establish what O assigns to encounter tracking; distinguish supplied clinical assertions from Harmonia context/correlation. O has no separate Episode responsibility; reference/contextual necessity must be demonstrated or a gap recorded. |
| 11. Provenance / qualification / time | Context establishment, source encounter assertions, movement interval, state effective time, reporting time and correction lineage; assess community/virtual contexts without requiring a physical bed. |
| 12. Lifecycle | Compare P §3.4, B §5 and T `FEAT-SA-09` (including `ON_LEAVE`). Record differences; do not infer an Episode lifecycle from Encounter or transfer closure to linked information. |
| 13. Patterns to test | F-P; F-V context assembly versus independently governed information; F-R n-ary participation and temporal binding. |
| 14. Leakage risk | Conventional admission/episode aggregation, standard Encounter/Episode resources, bed-based definitions or preselected cardinality. |
| 15. Decision justification | J1–J8 plus explicit Episode disposition, evidence for its relationship to Encounter and unresolved-state treatment. Absence of evidence is not proof that Episode is only contextual. |

### 7.4 Order

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | B §6; O `Order Administration` row; P §3.5; X order dependencies; I §2.2; C §3.5; T Service Administration §5; Package 1 Healthcare Service §4. F-T §2.3 terminology discrepancy is K3 (§8). |
| 2. Capability / Features | `Business Enabling Capability: Order Administration`; `Order Request Ingestion` (`FEAT-SA-11`), `Order Destination Resolution & Routing` (`FEAT-SA-12`), `Order Closed-Loop Progression Tracking` (`FEAT-SA-13`), `Order Cancellation & Modification Coordination` (`FEAT-SA-14`); B also names `Order Result Association`, without a corresponding identifier in this T section. |
| 3. Functions / Services / Processes | `Receive Order Request` / `Order Requisition Ingress`; `Resolve Order Destination` / `Order Dispatch Service`; `Manage Order Progression` / `Order Status & Tracking Query`; `Coordinate Order Modification / Cancellation` / `Order Cancellation Service`; `Associate Order Outcome` / `Order Outcome Notification`; `Closed-Loop Order Progression Process`. |
| 4. Collaborations / Interactions | `Service-Delivery Collaboration`; `Service Request`, `Service Response`, `Service Outcome`, `Service Coordination`. Test applicability rather than inventing an Order Collaboration. |
| 5. Requirements to derive | What must be known to receive a direction, establish destination, distinguish acknowledgement/progress/outcome, coordinate modification/cancellation and correlate the outcome to its originating request? |
| 6. Frozen constraints | Referral ≠ Order ≠ Result; Package 1 Order direction semantics and delivery without mandatory Order; acknowledgement distinctions; no result ownership transferred by correlation. |
| 7. Candidates | Order; routing direction; order progression information; modification/cancellation request; order-outcome correlation. Test independent meaning versus qualifications/relationships. |
| 8. Modelling questions | Which distinctions belong to direction, dispatch, execution, outcome and acknowledgement? Is Medication Order a family-specific qualification or separate meaning? What does closure establish and what remains externally authoritative? |
| 9. Relationships to investigate | Requester/subject/intended service/destination; directed delivery; outcome correlation; amendments and cancellations. Meaningful n-ary scope and cardinality require business evidence. |
| 10. Responsibility / authority | Separate Order Administration progression/correlation responsibility from requesting authority, performing activity and result assertions. Do not turn source systems or consuming functions into new owners. |
| 11. Provenance / qualification / time | Origin and authority of direction/amendment; acknowledgement source/meaning; performance versus tracking time; outcome-binding evidence and contested correlation. |
| 12. Lifecycle | Examine B §6 and P §3.5 with T's broader closed-loop scope. Signature and specimen-specific states must not become universal Order/Result semantics without review (K2). |
| 13. Patterns to test | Order remains an associated direction mechanism, not a mandatory F-P stage; test qualified relationship, progression and evidence patterns without manufacturing Order Definition. |
| 14. Leakage risk | Standard service-request resources, generic task state machines, laboratory-only progression or invented names copied from F-T. |
| 15. Decision justification | J1–J8 plus exact Domain03 traceability, preserved request/execution/result distinctions, and disposition of K2/K3 where consequential. |

### 7.5 Diagnostic Information

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | B §7; O `Diagnostic Administration` row; D §2.5 and §2.10 for non-laboratory measurement context; P §3.5; I §§2.1, 2.2, 2.4; C §§3.2, 3.5; X diagnostic dependencies; T Service Administration §6. |
| 2. Capability / Features | `Diagnostic Administration`: `Diagnostic Request Correlation` (`FEAT-SA-15`), `Diagnostic Report Ingestion & Binding` (`FEAT-SA-16`), `Diagnostic Report Distribution` (`FEAT-SA-17`); `Diagnostic Services Enablement` in D: `Laboratory & Imaging Result Distribution`, `Diagnostic History Consolidation` (T `FEAT-SD-09`, `FEAT-SD-10`, under `Diagnostic Services`). Preserve these source capability-name variants. |
| 3. Functions / Services / Processes | `Correlate Diagnostic Request` / `Diagnostic Correlation Resolution`; `Bind Diagnostic Report` / `Diagnostic Report Ingress`; `Distribute Diagnostic Report` / `Diagnostic Result Delivery Service`; `Receive Diagnostic Requisition Feed` / `Modality Worklist Distribution`; `Publish Governed Diagnostic Report` / `Diagnostic Publication Service`. No dedicated Diagnostic process is named in P; inspect Order outcome binding as a boundary. |
| 4. Collaborations / Interactions | `Practitioner Collaboration`, `Service-Delivery Collaboration`; `Clinical Information Supply`, `Clinical Information Review`, `Service Outcome`, `Information Qualification`, `Information Correction`. No universal diagnostic-to-collaboration mapping is established. |
| 5. Requirements to derive | What must be distinguished to correlate a diagnostic request, understand supplied results, bind and distribute preliminary/final/corrected information, and preserve clinical meaning independently of delivery state? |
| 6. Frozen constraints | Order ≠ Result; Package 1 delivery/outcome meanings; B §7 disclaims interpretation/specimen-analysis ownership; F-A granular authority and F-L correction history. No frozen Observation/Finding equivalence. |
| 7. Candidates | Diagnostic request context; diagnostic activity information; Observation; Finding; Diagnostic Report; result correlation; report revision/correction. Specimen/study context is investigated only where behaviour requires it. |
| 8. Modelling questions | What is Observation? What is Finding? Are they independent, overlapping or differently qualified meanings? What distinguishes diagnostic activity, recorded measurement/assertion, interpretation and report? What report information must be understood, and does signature have any universal necessity? |
| 9. Relationships to investigate | Request/result correlation; report and conveyed/asserted information; observation/finding connection if justified; source/subject/context; correction/supersession; performed activity and outcome. No assumed report hierarchy or specimen cardinality. |
| 10. Responsibility / authority | Separate correlation/distribution responsibility from performing-provider assertion authority. Examine responsibility for qualification/publication; O's linkage registry is not evidence of Harmonia-originated interpretation. |
| 11. Provenance / qualification / time | Source activity/evidence; supplied versus interpreted assertion; reference/abnormality qualification; preliminary/final/corrected standing; observation/effective, assertion and distribution times where meaningful. |
| 12. Lifecycle | Test report revision and assertion qualification separately from activity and delivery progression. P's signed final report wording requires K2 scope review; no mandatory signed Diagnostic Report definition. |
| 13. Patterns to test | F-P where independently required; F-A assertion qualifications; F-R correlation and evidentiary associations; F-V supplied report versus assembled context. |
| 14. Leakage risk | LIS/RIS/PACS decompositions, accession/specimen structures, Observation resources, LOINC/SNOMED taxonomies or mandatory signature deciding semantics. |
| 15. Decision justification | J1–J8 plus an explicit disposition of Observation and Finding individually, justified report boundaries and preserved source authority. Unsupported distinctions stay unresolved. |

### 7.6 Medication Information

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | B §8; O `Medication Administration` row; D §§2.6, 2.7 and §1 non-practice boundary; E §2.1; I §§2.1, 2.2; C §§3.1, 3.4; R §2.2; T Service Administration §7 and Service Delivery medication/preventive sections. |
| 2. Capability / Features | `Medication Administration`: `Medication Order Ingestion` (`FEAT-SA-18`), `Dispense Event Tracking` (`FEAT-SA-19`), `Medication Administration Record (MAR) Ingestion` (`FEAT-SA-20`); D `Medication Therapy Enablement`: `Medication History Reconciliation Ingestion`, `Medication Administration Tracking` (T `FEAT-SD-11`, `FEAT-SD-12` under `Medication Therapy`). |
| 3. Functions / Services / Processes | `Receive Medication Order` / `Medication Order Ingress`; `Track Dispense Event` / `Dispense Event Ingress`; `Receive Medication Administration Event` / `Medication Administration Ingress`; `Project Longitudinal Medication History` / `Longitudinal Medication History Query`. Examine `Correlate Adverse Reaction & Allergy Profile` only for a relevant boundary. No dedicated Medication process is named in P. |
| 4. Collaborations / Interactions | `Patient Collaboration`, `Care-Team Collaboration`; `Clinical Information Supply`, `Clinical Information Review`, `Service Request`, `Service Outcome`. Test scope; do not manufacture a medication-practice collaboration. |
| 5. Requirements to derive | What must be distinguished to ingest directions, receive supply and administration reports, correlate them and present medication history? What information about the medication itself is independently required? |
| 6. Frozen constraints | Medication Order ≠ Dispense ≠ Administration; Harmonia tracks/communicates but does not prescribe, dispense or administer. Package 1 subject/practitioner authority; independent source assertion provenance. |
| 7. Candidates | Medication Order; dispense information; administration information; medication history/reconciled assertion; medication reference; Medication Definition as an explicit hypothesis requiring independent evidence. |
| 8. Modelling questions | What does each event/report assert? How does direction differ from supply and actual administration information? Does history carry new assertion authority? Does Medication Definition have independent necessity or is reference sufficient? Derive setting-neutral meanings, without making bedside nursing definitional. |
| 9. Relationships to investigate | Direction/supply/administration correlation; subject, originating authority and substance/medication reference; history source membership/assembly. No mandatory one-order/one-dispense/one-administration chain. |
| 10. Responsibility / authority | Separate timeline/correlation responsibility from prescribing, supplying, administering and reconciliation assertion authorities. No assumption that Harmonia owns external medication definitions. |
| 11. Provenance / qualification / time | Source of each direction/event/assertion; reconciliation lineage and reliability; prescribed/intended versus actual and reported timing; distinguish non-occurrence, unknown occurrence and missing report if business evidence requires it. |
| 12. Lifecycle | Determine whether a candidate represents immutable occurrence information, a revisable assertion, a changing direction or an assembly. Do not copy an Order lifecycle onto all medication information. |
| 13. Patterns to test | F-P; F-A event/assertion qualification; F-R correlation; F-V medication timeline. Incomplete pattern applicability is acceptable. |
| 14. Leakage risk | Pharmaceutical catalogues, standard medication resources, e-MAR conventions, nurse/bedside assumptions or manufactured Medication Definition. |
| 15. Decision justification | J1–J8 plus setting-neutral order/dispense/administration distinctions and an evidenced accept/reject/defer disposition for Medication Definition. Record narrow source examples without silently broadening frozen business scope (K4). |

### 7.7 Procedure

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | B §9; O `Procedure Administration` row; D §1; H §2.3 and P §4.7 for theatre boundary only; I §2.2; C §3.5; R `Performer`; T Service Administration §8, including the undecomposed surgical/procedural care boundary in Service Delivery. |
| 2. Capability / Features | `Business Enabling Capability: Procedure Administration`; `Procedure Booking Ingestion` (`FEAT-SA-21`), `Procedural Documentation Ingestion` (`FEAT-SA-22`). `Theatre Operations` / `Operating Theatre Case Progression` is adjacent operational context, not ownership of all Procedure meanings. |
| 3. Functions / Services / Processes | `Receive Procedure Booking / Request` / `Procedure Booking Ingress`; `Receive Procedural Documentation` / `Procedural Documentation Ingress`. Inspect `Manage Theatre Case Progression` / `Theatre Case Progression Telemetry` and `Theatre Case Progression Process` only to separate surgery-specific operational milestones. No general Procedure process is named in P. |
| 4. Collaborations / Interactions | `Service-Delivery Collaboration`; `Service Request`, `Service Response`, `Service Outcome`, `Service Coordination`. Their necessity for each candidate requires evidence. |
| 5. Requirements to derive | What must be understood to receive procedural requests/bookings and documentation? What performed-activity/outcome information is needed to interpret or correlate them independently of theatre logistics? |
| 6. Frozen constraints | Procedure Activity ≠ Procedure Documentation; Order ≠ Result; Package 1 service definition/delivery/outcome meanings; Harmonia non-practice boundary. |
| 7. Candidates | Procedure request/booking context; Procedure Activity; Procedure Documentation; procedure outcome information; Procedure Definition as an unproven reusable-definition hypothesis. |
| 8. Modelling questions | What is independently meaningful about Procedure in this business scope? Is reuse of service definition/delivery semantics sufficient? Is a distinct Definition required? How do request, actual activity, outcome and documentation differ? |
| 9. Relationships to investigate | Request to activity/outcome/documentation; performed service, subject/performer and context; documentation asserting activity/outcome. No universal surgical sequence or mandatory definition binding. |
| 10. Responsibility / authority | Trace request/index responsibility to O; distinguish performing-provider authority and document authority. Do not assign surgical practice or theatre logistics to Procedure Administration. |
| 11. Provenance / qualification / time | Booking versus performed activity; author/performer distinction; documentary evidence; requested/performed/asserted timing; qualification of changed or corrected documentation. |
| 12. Lifecycle | Test request progression, activity occurrence/progression and documentation versioning separately. Theatre Case Progression does not establish a universal Procedure lifecycle. |
| 13. Patterns to test | F-P and Package 1 service reuse; F-R request/evidence associations; F-A provenance. Require independent business necessity for Procedure Definition. |
| 14. Leakage risk | Surgery/theatre language defining all procedures, procedure resources/codes, operative-document structures or pattern-driven Definition. |
| 15. Decision justification | J1–J8 plus setting-neutral semantics, bounded operational dependencies and an explicit Procedure Definition disposition. Unsupported non-surgical scope must be recorded, not invented (K4). |

### 7.8 Clinical Document

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | B §10; O `Clinical Record Administration` row; P §3.6; E §§2.1, 2.2, 2.7; X document dependencies; I §§2.1, 2.4; C §3.7; T Service Administration §9. |
| 2. Capability / Features | `Business Enabling Capability: Clinical Record Administration`; `Clinical Document Ingestion` (`FEAT-SA-23`), `Document Versioning & Supersession` (`FEAT-SA-24`), `Document Metadata Indexing` (`FEAT-SA-25`). |
| 3. Functions / Services / Processes | `Receive Clinical Document` / `Clinical Document Ingress`; `Govern Clinical Document Lifecycle` / `Clinical Document Lifecycle Service`; `Index Clinical Document Metadata` / `Document Metadata Registry Query`; `Clinical Document Lifecycle Process`. E's record assembly/preservation functions establish a separate boundary. |
| 4. Collaborations / Interactions | `Information-Sharing Collaboration`; `Clinical Information Supply`, `Information Submission`, `Information Qualification`, `Information Correction`, `Information Publishing`. Routine supply and formal submission remain distinct. |
| 5. Requirements to derive | What makes supplied information a document for ingestion, attribution, indexing, revision and supersession? What must be distinguished between documentary identity, conveyed assertions, document standing and integrated clinical synthesis? |
| 6. Frozen constraints | Clinical Document ≠ Clinical Fact; communication ≠ record; F-A granular authority, F-L preserved history, F-V constituent authority. Signed process states do not by themselves define every document as signed or immutable. |
| 7. Candidates | Clinical Document; document version/addendum; document attribution/standing; registry/index context; conveyed assertions. Clinical Fact is a question, not an established candidate definition. |
| 8. Modelling questions | What is Clinical Document, and what does it contain or assert? Is documentary composition distinct from the standing of assertions? Does Clinical Fact deserve canonical status or remain descriptive language? Are signature/legal characteristics applicable qualifications or a scope constraint needing review? |
| 9. Relationships to investigate | Document to assertions/subject/author/context; version/addendum/supersession; document contribution to record assembly. A document correction need not automatically change all related assertions. |
| 10. Responsibility / authority | Separate document governance, authorship/attestation, sourced assertion authority and Patient Clinical Record composition responsibility. No signature/receipt/persistence-based transfer of originating authority. |
| 11. Provenance / qualification / time | Author and amendment attribution; source assertion lineage; document standing and qualification; authored/effective/published/received/revised time where required. |
| 12. Lifecycle | Compare B §10, P §3.6 and F-L; investigate versioning, addenda, supersession and entered-in-error meaning. Preserve historical evidence without assuming every document is signed, legally constitutive or immutable once signed. K2 constrains finalisation. |
| 13. Patterns to test | F-P; F-A assertion/document authority; F-V document composition versus record assembly; F-C structural containment versus reference. |
| 14. Leakage risk | Document resource/profile structures, mandatory signature, legal-instrument definitions, or automatic creation of Clinical Fact to populate a diagram. |
| 15. Decision justification | J1–J8 plus a behaviour-derived document boundary, explicit Clinical Fact disposition and documented handling of K2. No universal signature or legal-instrument claim without resolved evidence. |

### 7.9 Problem / Condition / Care Need

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | E §2.1; O `Patient Clinical Record` and `Care Plan Administration` rows; B §11; D §§2.1, 2.3, 2.7, 2.8 for assessment/preventive/community contexts; I §§2.1, 2.2, 2.4; C §§3.1, 3.4. No dedicated three-concept taxonomy is established in these Domain03 sections. |
| 2. Capability / Features | `Patient Clinical Record`: `Longitudinal Health Record (LHR) Assembly` (`FEAT-ISE-01`), `Active Clinical Record Access` (`FEAT-ISE-02`), `Durable Clinical Record Preservation` (`FEAT-ISE-03`). `Care Plan Context Association` in B §11 is relevant but has no corresponding individual identifier in T's care-plan subsection. |
| 3. Functions / Services / Processes | `Maintain Active Clinical Record` / `Active Problem & Allergy Summary Query`; `Assemble Longitudinal Clinical Record` / `Longitudinal Clinical Record Query`; `Preserve Durable Clinical Record` / `Historical Clinical Record Access`; `Associate Care Plan Context` / `Care Plan Context Query`. No dedicated Condition/Problem/Care Need process is named in P. |
| 4. Collaborations / Interactions | `Patient Collaboration`, `Care-Team Collaboration`; `Clinical Information Review`, `Information Qualification`, `Information Challenge`, `Information Correction`, `Service Coordination`. Relevance does not establish a new business responsibility. |
| 5. Requirements to derive | What must be distinguished to maintain active problems and associate plans with diagnoses/health problems? Do preventive/community/support behaviours independently require Care Need information? What must be understood beyond a problem-list grouping? |
| 6. Frozen constraints | Condition ≠ Problem ≠ Care Need; semantic categories are non-hierarchical; no default clinical-practice ownership from record assembly; source assertions retain authority. |
| 7. Candidates | Condition; Problem; Care Need; active problem grouping; contextual/qualified clinical assertion. The three labels require investigation individually and no inheritance hierarchy is presumed. |
| 8. Modelling questions | What does each mean and why is it necessary? What distinguishes them? Is Condition limited to pathology? What relationships are justified? Does Care Need have a direct behavioural trace or only contextual necessity? No plausible definition is adopted in this plan. |
| 9. Relationships to investigate | Subject attribution; evidentiary association; care-plan concern; possible Condition/Problem/Care Need relationships; collection membership. No “Condition is a Problem” hierarchy or compulsory mapping among the three. |
| 10. Responsibility / authority | E/O establish active-problem and longitudinal-synthesis responsibility, not blanket origination of diagnoses or needs. A distinct Care Need/Condition responsibility has not been established; trace necessity, reference scope or gap explicitly. |
| 11. Provenance / qualification / time | Who asserted/qualified each meaning, on what evidence? Examine competing assertions, certainty, current relevance, effective period and assertion time without prescribing status enums. |
| 12. Lifecycle | Determine meaningful assertion qualification, resolution/relevance and correction separately from list inclusion or plan revision. No universal clinical-assertion lifecycle automatically applied. |
| 13. Patterns to test | F-P; F-A qualification; F-C active-list membership; F-R evidentiary/contextual associations; F-V composition without re-authoring. |
| 14. Leakage risk | Condition resources, pathology-first definitions, diagnosis code hierarchies, conventional problem-list storage or an invented inheritance tree. |
| 15. Decision justification | J1–J8 plus separate evidence/disposition for all three candidates and each proposed relationship. Weak upstream support is a visible gap, not permission to modify Domain03. |

### 7.10 Goal / Care Plan

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | B §11; O `Care Plan Administration` row; C §§3.1, 3.4; I §§2.2, 2.10; D §§2.1, 2.8; E §2.1 for the record boundary; T Service Administration §10. |
| 2. Capability / Features | `Business Enabling Capability: Care Plan Administration`; `Care Plan Ingestion & Distribution` (`FEAT-SA-26`); B additionally names `Care Plan Context Association`, `Care Plan Update Distribution`, without individual identifiers in T's care-plan subsection. |
| 3. Functions / Services / Processes | `Receive Care Plan` / `Care Plan Ingress`; `Associate Care Plan Context` / `Care Plan Context Query`; `Distribute Care Plan Change` / `Care Plan Change Notification`. Inspect `Coordinate Multi-Agency Chronic Care Plan` / `Chronic Care Timeline Query` for use context. No dedicated Care Plan process is named in P. |
| 4. Collaborations / Interactions | `Patient Collaboration` explicitly includes shared goal setting/outcome tracking; `Care-Team Collaboration` includes shared planning. Examine `Service Coordination`, `Coordination Outcome`, `Participation Registration`, `Participation Withdrawal` without equating participation to caring/access authority. |
| 5. Requirements to derive | What information coordinates shared care intent, goals, patient action steps, context and revisions? What must be known to distribute change to participants? Which concerns, activities or outcomes are independently referenced? |
| 6. Frozen constraints | Goal ≠ Outcome; Care Plan ≠ Activity; Condition ≠ Problem ≠ Care Need; participation/caring/access/consent authorities remain distinct; referenced information retains identity and authority. |
| 7. Candidates | Goal; Care Plan; planned action/activity information; participant enrolment; barrier; plan revision. O's named registry contents prompt questions, not automatic canonical concepts. |
| 8. Modelling questions | What is Goal? What does Care Plan coordinate? Are plan definition, subject-specific intent, commitments and referenced work independently meaningful? Which barriers/participants need independent concepts? How is outcome evaluation justified by behaviour? |
| 9. Relationships to investigate | Plan/goal, plan/concern, plan/activity and participant associations; goal/outcome evaluation if supported; revision and contextual binding. Do not require every activity to be plan-owned. |
| 10. Responsibility / authority | Separate Care Plan Administration registry/revision responsibility, originating planning/goal assertions, patient/practitioner agreement and individual activity/outcome authority. Ask who qualifies coordination and revisions. |
| 11. Provenance / qualification / time | Goal/plan contributors, agreement or asserted intent, revision lineage, effective horizons, changing participant context and outcome evidence where required. |
| 12. Lifecycle | Determine plan revision, goal progression/qualification and membership effective periods independently of action execution. No generic “plan becomes delivered care” lifecycle. |
| 13. Patterns to test | F-P only where definition/binding meanings are independently supported; F-R qualified/n-ary participation; F-C membership; F-V composition. No mandatory Care Plan Definition stage. |
| 14. Leakage risk | Standard CarePlan/Goal resources, conventional care-management templates, predetermined goal/outcome meanings or filling all five pattern elements. |
| 15. Decision justification | J1–J8 plus an evidenced Goal boundary, the actual information coordinated by Care Plan, and preserved authority of referenced concerns, work and outcomes. |

### 7.11 Clinical Communication

| Required analysis | Investigation agenda |
| :--- | :--- |
| 1. Exact sources | B §12; O `Clinical Communication Admin` row; E §§2.1, 2.2, 2.4, 2.7; B §10 for document submission; I §§2.1, 2.4; C §§3.2, 3.4, 3.7; T Service Administration §11 and acknowledgement distinction. |
| 2. Capability / Features | `Business Enabling Capability: Clinical Communication Administration`; `Clinical Communication Distribution` (`FEAT-SA-27`), `Delivery Acknowledgement Tracking` (`FEAT-SA-28`), `Communication Audit Logging` (`FEAT-SA-29`). `Health Information Communication` is a separate mediation responsibility, not a synonym. |
| 3. Functions / Services / Processes | `Distribute Clinical Communication` / `Secure Clinical Message Dispatch`; `Track Business Delivery Acknowledgement` / `Communication Delivery Status Query`; `Record Communication Transaction` / `Communication Audit Query`. Examine `Receive Information Submission`, `Receive Clinical Document`, `Coordinate Care-Team Collaboration`, `Mediate Standards-Based Health Information Communication` at their respective boundaries. No dedicated Clinical Communication process is named in P. |
| 4. Collaborations / Interactions | `Practitioner Collaboration`, `Care-Team Collaboration`, `Information-Sharing Collaboration`; `Clinical Collaboration` (Interaction), `Clinical Information Supply`, `Information Submission`, `Information Qualification`, `Clinical Information Notification`. Preserve metamodel types. |
| 5. Requirements to derive | What must be known to distribute communication, identify its conveyed information, track business receipt/rejection and preserve transaction evidence? Which supplied assertions may require separate qualification or incorporation into governed information? |
| 6. Frozen constraints | Clinical Communication ≠ Clinical Record; technical acknowledgement ≠ business acknowledgement; O excludes communication-content ownership; E §2.7 requires explicit formal document submission for authoritative collaborative entries; AX-13 bounds external outcomes. |
| 7. Candidates | Clinical Communication; conveyed information/assertion; business delivery acknowledgement; communication transaction evidence; governed contribution/qualification relationship, if supported. No automatic communication-to-record transformation. |
| 8. Modelling questions | What is being communicated, and does it remain an assertion distinct from the communication? Who authorises it? What exact Domain03 behaviour governs incorporation/qualification? What does a reported filing acknowledgement prove, and does it establish any Harmonia record standing? |
| 9. Relationships to investigate | Communication/source/recipient/content; acknowledgement/about-communication; evidence of exchange; subsequent submission/qualification linked to conveyed assertions. No “communication becomes clinical record” edge. |
| 10. Responsibility / authority | Separate dispatch/acknowledgement/evidence responsibility, content authority, formal document governance, integrated record composition, exchange and protocol mediation. Receiving/filing does not imply clinical verification or transfer of originating authority. |
| 11. Provenance / qualification / time | Content source; sender/recipient assertion of receipt/rejection/filing; transaction lineage; sent/received/asserted/qualified times. Preserve unknown business delivery or incorporation outcomes. |
| 12. Lifecycle | Investigate communication delivery/evidence progression separately from conveyed assertion qualification and record/document lifecycle. No mandatory incorporation state machine without Domain03 support. |
| 13. Patterns to test | F-P applicability; F-A assertion/evidence distinction; F-R communicated-content and acknowledgement associations; F-V no authority transfer through composition. |
| 14. Leakage risk | Transport success treated as clinical acceptance, standard Communication resources, chat transcript equated with record, or automatic verification/filing semantics. |
| 15. Decision justification | J1–J8 plus distinct communication/content/authority reasoning and an exact behavioural trace for any incorporation rule. Missing governance stays a gap; acknowledgement scope ambiguity K7 must remain visible. |

## 8. Frozen-source discrepancies and review questions

The following observations are findings about source text, not resolved Package 2 semantics. No frozen document is changed. Interpretation must preserve the applicable axioms; a concept or relationship dependent on a material unresolved conflict cannot be finalised by this plan.

| Issue | Inspected evidence | Consequence and proposed treatment |
| :--- | :--- | :--- |
| **K1 — Referral scope tension** | B §3 says Referral concerns “assumption or sharing of ongoing clinical care responsibility”; T's Referral/Order distinction also centres responsibility. I §2.2 `Service Referral` and R `Referrer` say “assess, manage, or assume” responsibility. The revision instruction rejects using transfer/sharing as the primary definition. | Preserve Referral ≠ Order and quote all relevant sources. Investigate receipt/assessment/triage/acceptance behaviour before definition. Return whether the narrower text is bounded scope, incomplete explanation or an upstream contradiction requiring separate review. Do not silently replace the frozen meaning or copy it as the complete definition. |
| **K2 — Signature/legal process scope** | P §3.5 includes “Authoritative diagnostic report signed” at Final Result Bound; P §3.6 includes `Final Signed`, legal signature and sign-off. B §10 and F-L use broader document version/lifecycle wording. | Signature is present in frozen process descriptions and cannot be claimed absent. Determine whether it qualifies a particular process/disposition or establishes a wider restriction. Do not infer that every Diagnostic Report/Clinical Document is signed, a legal instrument or immutable once signed. Escalate any irreconcilable scope conflict before dependent decisions. |
| **K3 — Foundation Order example lacks exact Domain03 trace** | F-T §2.3 uses `Closed-Loop Order Status Tracking`, `Track Order Fulfilment Status`, `Clinical Order Record`, `Order Status History`, `Order-Result Reconciliation Binding`; these exact names were not found in the inspected Domain03 source. B §6 supplies the different exact labels listed in §§5 and 7.4. | Retain the frozen derivation principle, but do not use its representative labels as canonical Domain03 evidence. Cite B/O/P directly in Package 2. Record the discrepancy without changing F-T or Domain03; review whether the example requires a separately authorised correction. |
| **K4 — Narrow examples versus setting-neutral scope** | B §8/O use bedside/nurse/e-MAR medication examples; B §9/O and H/P theatre sections emphasise surgical documentation. D §1 covers multiple settings and disclaims clinical practice; D medication and community contexts supply wider use evidence. | Do not define Medication Administration by a nurse/bedside setting or Procedure by surgery. Distinguish examples from normative restrictions through source review. Where broader semantics lack behavioural support, record that limit/gap; do not expand frozen scope merely to obtain setting neutrality. |
| **K5 — Responsibility/capability terminology variants** | O uses `Episode & Encounter Admin` and `Clinical Communication Admin`; B/T use full `… Administration` names. O asset labels often aggregate B responsibilities. D uses enablement capability names where T uses operating-context names. B has Features without individual T identifiers. | Preserve source names, types and sections. Do not invent identifiers, L2/L3 tiers or a silent equivalence catalogue. Test whether each variant is shorthand, grouping or material mismatch; review consequential ambiguity. |
| **K6 — Package 1 delivery responsibility wording** | Healthcare Service §2 refers to “Performing Clinical / Operational Capabilities” and gives `Inpatient Care Enablement` / `Diagnostic Administration` examples for execution. D §1 says Harmonia enables rather than practices care; B §7 excludes interpretation/specimen analysis. | Reuse the frozen `HealthcareServiceDelivery` meaning without treating examples as proof Harmonia performs medicine or originates clinical outcomes. Separate information about external fulfilment from enablement/correlation responsibility. Refer unresolved ownership consequences for review; do not reassign or redefine Package 1. |
| **K7 — Acknowledgement/incorporation scope** | B §12 defines a Business Delivery ACK as received, parsed, accepted and filed into the clinical record. T distinguishes business acknowledgements more broadly; `FEAT-SA-28` discusses delivery/read receipts. E §2.7 requires formal document submission for authoritative collaborative entries; O says communication does not own content. | Investigate whether these are distinct business acknowledgement qualifications, scopes or contradictory claims. An external filing report is evidence from its asserting source, not automatic Harmonia clinical authority or proof communication became a record. Do not invent a verification gate; trace the actual incorporation behaviour or record a gap. |
| **K8 — Referrer role ambiguity** | R §2.2 includes `Referrer` as a Business Role, while §3.2 describes source `Referrer` as contextual qualifier. The earlier proposal also incorrectly treated `Requester`/`Performer` as never Business Roles, despite their presence in R. | Apply the frozen separation between local relationship role and Business Role. Preserve both source statements and seek scope clarification where necessary; do not repair the catalogue or infer authority from matching role labels. |

K1 and K2 particularly require review of frozen-source scope before any affected canonical definition. Other issues may admit a bounded interpretation, but that interpretation needs explicit evidence. Planning does not settle them. Work on an independent subject may proceed only under the later approved execution scope, without concealing or routing around a blocking dependency.

The initial evidence register must also track missing support for Episode independence, Observation/Finding distinction, Condition/Care Need governance, Clinical Fact status, Medication/Procedure Definition necessity and communication incorporation. These are open modelling questions, not proof of an upstream defect.

## 9. Proposed eventual documentation and visual deliverables

### 9.1 Family document structure

Retain the proposed eleven destinations in §1.1, subject to a reviewed grouping change. Each eventual family document should preserve a readable derivation, not merely a finished taxonomy:

1. Purpose, investigation scope and document status.
2. **Frozen semantic invariants**, with source provenance and Package 1 concepts reused by reference.
3. Exact Business Architecture evidence: capability/Feature, function/service/process, relevant collaboration/interaction, information responsibility and negative boundaries.
4. Information requirements and conceptual quantification: what must be distinguished, referenced, related or managed, why, and for which behaviour. Account for Information Sets where a grouping/composition is itself justified.
5. **Candidate Information Concepts**, alternatives tested and dispositions, including rejected/deferred candidates. Do not hide unsuccessful hypotheses.
6. **Explicit modelling questions**, answers supported by evidence and questions that remain open.
7. **Derived architectural decisions**: reasoning chain, semantic boundary, neighbour distinctions, alternatives, responsibility, authority and limits. Approved concepts are clearly separated from unresolved candidates.
8. Qualified concept descriptions: meanings/categories, identity where meaningful, responsibility versus authority/stewardship/custody/consumption, provenance, qualification, temporal/effective context and independent lifecycle where justified.
9. Derived Information Relationships using F-R, with rationale, qualifications, authority and justified or unspecified cardinality.
10. Pattern tests and applicability reasons; assembly/view/collection participation only where justified; no compulsory stages or universal state machine.
11. Mermaid visualisations of supported decisions; explicit negative boundaries; gaps/dependencies and implications for later architectural layers.
12. Review/conformance evidence and decision references, giving future human and AI peers enough context to understand why the model exists.

Each significant decision should include a small derivation example using actual source behaviour and the resulting need. Such examples demonstrate the engineering use of TOGAF layer separation and ArchiMate element semantics without creating new upstream services, processes or roles. They should show how semantic clarity constrains later design and avoids unnecessary duplicate representations, conflicting ownership and rework.

### 9.2 Mermaid output coverage

Retain the proposed ten visual coverage areas while removing their predetermined nodes, arrows, decompositions and cardinalities:

| Visual coverage | Evidence prerequisite |
| :--- | :--- |
| 1. Referral | Derived referral meanings and justified relationships to existing service/subject concepts. |
| 2. Appointment / Scheduling and Episode / Encounter | Independent investigation outcomes; optional correlations only where evidenced; no assumed appointment-to-encounter or episode cardinality. |
| 3. Order | Supported direction, progression and outcome distinctions, without a prescribed workflow-shaped graph. |
| 4. Diagnostic Information | Individual Observation/Finding dispositions and supported activity/report/assertion relationships. |
| 5. Medication Information | Derived order/dispense/administration/history meanings; no forced Definition or event chain. |
| 6. Procedure | Supported activity/documentation distinctions and Definition disposition; no surgery-only model. |
| 7. Problem / Condition / Care Need | Individually justified meanings and relationships; no assumed hierarchy. |
| 8. Goal / Care Plan | Derived coordination and goal meanings; only justified activity/outcome associations. |
| 9. Clinical Document | Supported documentary/assertion distinction; no mandatory Clinical Fact node. |
| 10. Cross-family context, including Clinical Communication | Only independently justified cross-family relationships; communication/content/record distinctions must remain visible. |

Coverage is not a requirement to populate a particular graph. Optional relationships, disconnected concepts and unspecified cardinality are valid. A diagram may explain a supported boundary and a labelled unresolved question; tentative content must not appear canonical. If ten visuals or these groupings cannot faithfully cover the derived architecture, propose a revised visual/document arrangement for review rather than inventing nodes or edges.

Do not author Package 2 semantic diagrams in this planning revision. They follow the decision evidence during later authorised work, with diagram nodes/edges traceable to decision references.

### 9.3 Future change boundaries

After review and explicit execution authorisation, proposed deliverables are the eleven family documents, navigation/roadmap additions in the two Domain04 README files and a Package 2 completion report. Navigation additions must preserve all frozen Foundation/Package 1 semantics. No change to F-T or Domain03 is included, even where discrepancies are identified.

The previously proposed report destination is `.junie/reports/2026-10-07-domain04-clinical-service-administration-information-families.md`; review the date if execution occurs later. The report records work performed, material evidence/results, decisions, unresolved gaps and completion scope. It is not a new architectural authority. Durable semantic reasoning and accepted decisions belong in the family documentation, not only in the report.

## 10. Proposed delivery sequence after review

All steps below are **NOT STARTED and conditional on G0 acceptance and explicit execution authorisation**. They describe reviewable work boundaries; this revision does not commence any of them.

| Step | Work and outputs | Exit / review boundary |
| :--- | :--- | :--- |
| 1. Establish the evidence baseline | Reconfirm frozen sources/revisions; capture exact source excerpts and types for all eleven areas; rebuild Package 2's own evidence register from B/O/P/C/I and applicable dependencies. Record K1–K8 and missing traces. This does not modify upstream architecture or establish concepts. | **G1 — Evidence review:** return exact traceability, source variants, conflicts and unresolved requirements. Resolve or explicitly bound material frozen-source questions before dependent semantic decisions. |
| 2. Quantify and qualify the initial block | Referral, Appointment / Scheduling Context, Episode / Encounter and Order. Record requirements before labels; investigate alternatives, responsibility, authority, provenance, time, lifecycle and relationships; propose decisions with J1–J8 evidence. | **G2 — Initial semantic review:** stop and return the four-area derivation and gaps before proceeding. No later family may be used to silently settle these questions. |
| 3. Quantify and qualify diagnostic/intervention areas | Diagnostic Information, Medication Information and Procedure, reusing only accepted initial-block and Package 1 meanings. Resolve candidate questions rather than copying the earlier models or pattern table. | **G3 — Semantic review:** assess Observation/Finding, definition necessity, scope neutrality, source authority and clinical/documentary distinctions. Stop at the approved block boundary. |
| 4. Quantify and qualify evaluative/planning/document/communication areas | Clinical Document; Problem / Condition / Care Need; Goal / Care Plan; Clinical Communication. Preserve independently derived assertions, composition, coordination and communication evidence boundaries. | **G4 — Semantic review:** assess individual concept dispositions, communicated-assertion governance, Clinical Fact status and remaining source conflicts. |
| 5. Integrate and prepare publication | Check cross-family consistency without forced connectivity; draft family documentation from reviewed reasoning; produce Mermaid outputs; prepare navigation additions and completion evidence. If integration changes a decision, return it for semantic review. | **G5 — Package review:** assess complete derivation/conformance and documented gaps. Publish canonical documentation only within the approved scope; unresolved candidates remain explicitly unresolved. |

Pattern applicability is considered within each derivation, never a prior family-classification exercise. Early blocks need not depend on unaccepted later definitions: mark downstream dependencies and return for review when they become material. No step may silently repair a prerequisite in frozen architecture or commence later-domain design.

Completion means all agreed subject areas have an evidenced disposition and reviewable reasoning, not that every question has been forced into a concept. A material unresolved dependency prevents finalising the affected decision; a bounded unresolved question can be an accepted limitation only if reviewed and explicitly recorded. Do not claim full semantic closure while concealing gaps.

## 11. Assurance and validation proposal

### 11.1 Architectural review checks

| Check | Evidence required |
| :--- | :--- |
| Forward derivation | Information requirements precede concept decisions in the reasoning record. For each concept, explain business necessity without consulting representations. |
| Exact traceability | Every claimed upstream element matches its cited source/type; absent processes, Feature identifiers, family-specific collaborations and direct links remain absent. O/B differences are visible. |
| Frozen protection | No semantic change to Domains01–03, Foundation or Package 1; check actual file changes against the approved boundary. |
| Quantification | Every candidate has a disposition; justified concepts/sets and distinctions cover behavioural needs without a prescribed concept count or physical record cardinality. |
| Qualification | Responsibility, authority, stewardship, custody, consumption, provenance, qualification, time and lifecycle have evidence or explicit “not meaningful / unresolved” reasons. No attribute/schema design. |
| Responsibility | Owned concepts trace to O and the behaviour discharging responsibility. Reference/contextual concepts demonstrate necessity; merely useful conventional concepts are rejected or deferred. |
| Relationships | Meaning precedes graph structure; direction, roles, authority, temporal qualification, n-ary scope and any cardinality are supported. No forced containment/connectivity. |
| Patterns | Full/partial/concept-specific/not-applicable outcomes are allowed and reasoned; “unresolved” remains unresolved; missing stages create no artificial concepts. |
| Standards leakage | Inspect explanations, examples, tables, diagrams and acceptance criteria, not only keywords. Standards mentioned in frozen evidence remain context, never the reason a concept exists. |
| Decision history | Alternatives, evidence limits and conflicts are preserved. Conventional and Harmonia-specific conclusions face the same acceptance criteria. |
| Visual fidelity | Diagram claims match reviewed decisions; no phantom Definition/Clinical Fact node, implicit lifecycle, cardinality or communication-to-record transformation. |
| Educational utility | A future peer can follow behaviour → requirement → distinction → decision and use it to assess later design without inferring architecture from implementation. |

Apply all sixteen F-G guardrails explicitly in the future conformance record. Architecture tests enforce repository implementation invariants; they cannot establish semantic necessity or validate this derivation method in place of architectural review.

### 11.2 Evidence-oriented review scenarios

- A conventional concept with no behaviour/ownership trace must demonstrate contextual necessity or remain rejected/deferred/gap; a standard resource is insufficient.
- An externally authoritative booking can be correlated and projected without Harmonia claiming appointment/slot authority or inventing an Encounter consequence.
- Episode can be independently meaningful, contextual or unresolved according to evidence; no default classification/cardinality is imposed.
- Observation and Finding are analysed individually, including authority/qualification/provenance; resemblance or different standard representations do not decide their boundary.
- A report/document can have a signed disposition within a cited process without all documents/reports being defined by signing. If frozen scope prevents that interpretation, flag the conflict.
- Superseding a document or closing an encounter must not erase the identity/authority/provenance of related information through aggregation assumptions.
- Medication and Procedure Definition candidates can be rejected while preserving all genuinely required business information and Package 1 meanings.
- A conveyed assertion and the communication carrying it remain distinguishable; an acknowledgement's asserted scope does not manufacture clinical verification or Harmonia governance.
- Pattern stages can be partially applicable or absent without creating missing concepts; a cross-family diagram can remain disconnected.

### 11.3 Verification for this planning revision

Perform bounded document validation: relative links resolve; all eleven briefs cover the fifteen required analyses; quoted canonical names/Feature identifiers match cited sources; no new family files/semantic diagrams are created; and protected source contents remain unchanged. Preserve the incoming original proposal as historical context.

Run the repository-mandated architecture suite as a regression check, without treating it as Package 2 execution or semantic assurance:

```bash
mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
```

Use bounded execution under AGENTS §5.1. Offline mode may be used to avoid blocking dependency resolution, with any missing dependency/build prerequisite reported distinctly. Ordinary focused tests warrant investigation after about two minutes without progress; no unchanged stalled reruns. Report PASS, FAIL, TIMEOUT, STALLED or UNRESOLVED according to observed evidence. A reactor build failure before architecture tests execute must not be reported as failed architecture behaviour. Do not fix code, change frozen sources or broaden this planning task to make the check pass.

### 11.4 Results of this planning revision

- **PASS — document structure and links:** all eleven briefs cover analyses 1–15; all 29 relative document links resolve; explicit Feature name/identifier pairs match T; no Package 2 semantic diagrams or family documents were created.
- **PASS — frozen protection:** checksum comparison confirmed 63 protected/source files unchanged, including Domains01–03, Domain04 Foundation/Package 1, repository axioms and the runtime master plan. The incoming original Package 2 proposal is unchanged; only this revision proposal was edited.
- **PASS — architecture regression:** executed `timeout --signal=TERM --kill-after=10s 180s mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`. Maven reported BUILD SUCCESS in 32.101 seconds on 2026-10-07; 90 tests across 11 ArchitectureTest classes, zero failures/errors/skips. This checks implementation invariants, not acceptance of the proposed information semantics.
- **PASS — whitespace:** comparison with the incoming revision draft produced no whitespace diagnostics after correcting Markdown line breaks.

Architectural review remains pending. These checks do not resolve K1–K8 or confer execution authorisation.

## 12. Review handover

This proposal retains the agreed scope and proposed documentation structure, replaces destination-first models with eleven evidence-led investigations, and exposes source discrepancies rather than inventing upstream architecture. The next action is architectural review of the method, source findings, subject agendas and gates.

**Package 2 has not been executed. G0 remains pending. No semantic decision, final family document, FHIR mapping, realised Data Object or implementation is authorised by this plan's existence.**
