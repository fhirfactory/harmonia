# Harmonia 1.1.20 — Business-to-Enterprise Capability Reconciliation
**Candidate 011 — architecture analysis, not an accepted target model or migration instruction**  
**Source:** `fhirfactory/harmonia` / `Capability-Map-Consolidation` / commit `fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374`  
**Design baseline:** Candidate 006 (Business and Enterprise); Candidate 003 (Stakeholders/Journeys); accepted Candidate 009 Scope; Candidate 010 (existing Service inventory and DRP register).  
**Authority:** Existing governed Continuum and agreed DRPs are equally substantive architecture evidence; neither is to be silently displaced. Explicit unresolved matters stay unresolved.  
**No repository changes, identity allocation, service-home allocation, acceptance, or Codex authorisation.**

## Navigation
1. [Executive architectural assessment](#1-executive-architectural-assessment)
2. [Method, authority and meaning of mapping](#2-method-authority-and-meaning-of-mapping)
3. [Business CT1 and CT2 to Enterprise CT2](#3-business-ct1-and-ct2-to-enterprise-ct2)
4. [Enterprise CT2 reverse demand](#4-enterprise-ct2-reverse-demand)
5. [Selected CT3-level semantic stress tests](#5-selected-ct3-level-semantic-stress-tests)
6. [24 reconciliation findings](#6-reconciliation-findings)
7. [Stakeholder and Journey stage integrity](#7-stakeholder-and-journey-stage-integrity)
8. [16 end-to-end integrity test proposals](#8-end-to-end-integrity-test-proposals)
9. [Candidate 006 depth refinements](#9-candidate-006-depth-refinements)
10. [Decision register and next design boundary](#10-decision-register-and-next-design-boundary)
11. [Sources, validation and explicit limitations](#11-sources-validation-and-explicit-limitations)

## 1. Executive architectural assessment

The current evidence-backed working baseline contains **15 Business CT1s / 86 CT2s / 258 CT3s / 1,299 Features** and **9 Enterprise CT1s / 47 CT2s / 139 CT3s / 700 Features**. This pass establishes **353 candidate Business CT2 → Enterprise CT2 contribution links**, and context-indexes all 258 Business CT3 demands. All 47 Enterprise CT2s appear as possible contributors to at least one Business CT2. This is **candidate scoping completeness**, not proof of design completeness or delivery.

The retained 29 Journeys have **179 value stages**, each provisionally anchored to Business CT2 and candidate Enterprise CT2 context. A further **20 deliberately reasoned CT3-to-CT3 spot checks**, **24 findings** and **16 integrity test proposals** challenge key boundaries.

### Judgements
- **The existing four-tier capability skeleton is broadly serviceable**. This analysis has not demonstrated a need for another Enterprise CT1. Targeted depth changes are more compelling than new top-level taxonomy.
- **E05 (Themis)** is the clearest under-articulated capability obligation: a policy/Passport lifecycle cannot be inferred from “Contextual Authorisation Determination” plus “Passport Propagation” alone.
- **E08 (Kleio/Dokimasia)** is the second: reporting is not sufficient to describe creation, immutable custody, collection, raw export, dereferenced assembly and controlled evidence release.
- **E01/E06** must separate working-state update, individual work execution, workflow progression, acceptance and authoritative durable commitment.
- **B14 and B15 must remain Business Capabilities** even where their digital support is outside, broader than, or later than Harmonia’s first solution releases.
- **Capabilities are consumer abilities, not wrappers around Solution Facets.** Familiar facets inform the model but do not dictate E CT1/CT2 decomposition; Artemis/Praxis are later structural reconciliation questions.
- **No Service-per-Feature rule.** After capability refinement, independently meaningful granular Service contracts may be discovered without bundling Retrieve, Check, Update, ProcessSomething and Commit.

## 2. Method, authority and meaning of mapping

The sequence is **Stakeholder perspective and Journey stage → Business Capability/Feature → Enterprise Capability/Feature → granular Service intention → later Information Systems home**. It is a design reasoning order, not a one-way runtime chain or an ArchiMate relationship definition. The Continuum and DRPs inform every level with equal respect.

A candidate link means an information-systems ability **may contribute to** the named Business Capability. It does **not** assert: that the Business Capability is performed by Harmonia; that an Enterprise Feature is satisfied; that an application Service exists; that the relation is governed; or that the solution will include a payer, research, training or clinical decision-making system.

Cross-cutting Enterprise CT2s (digital authentication, governed reference resolution, exchange format mediation, parcel routing, operational logging and the like) are associated only where there is a plausible relevant demand. These remain **supporting digital dependencies**, not newly invented Business Capability owners.

Three evidence classes are maintained: **established architectural intent** (current Continuum or agreed DRP, preserving open questions), **architectural inference** (reasoned candidate mapping) and **new proposal** (specific capability or test extension for review). Retained design understanding is used to frame and challenge the proposals; it is not quoted as an exact historical transcript.

### Explicit non-equivalence controls
| Business/domain notion | Not equivalent to | Why |
|---|---|---|
| Physical specimen transport | E06.03 Governed Parcel Routing | PetasosParcel is an internal information/work parcel, not a clinical specimen. |
| Professional clinical privileges | E05.01 Digital Identity & Authentication | Authentication verifies an identity; clinical privilege determines professional authority. |
| Clinical finding interpretation | E04.04 Semantic Validation | Data-semantic validity does not prove a clinical interpretation is correct. |
| Clinical handover acceptance | E03.04 Exchange Receipt Confirmation | A recipient system receipt does not prove receiving clinical responsibility was accepted. |
| Business continuity preparedness | E08.06 Service Continuity & Recovery | ICT recovery enables but is not equivalent to healthcare enterprise continuity. |
| Service claim assessment | E09.03 Business Information Reporting | Reporting is not a claims adjudication service or an implied solution commitment. |
| Information update | E01.01 Durable Information Acceptance | Preparing/updating working information is distinct from authoritative durable commitment. |
| Patient perspective | B14 Participant perspective | Participant and Patient are separate stakeholder perspectives, not superclass/subclass in this design. |

## 3. Business CT1 and CT2 to Enterprise CT2

Each first-listed Enterprise CT2 is a candidate most-immediate information-systems contribution; additional CT2s are supporting or cross-cutting. This ordering is for review and **must not** become automatic service-home selection.
### B01 — Care Participation & Access
People can understand their health and care options, obtain access, and participate on appropriate terms.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B01.01` Health Needs & Service Navigation | Help people recognise needs and find suitable sources of assistance. | `E02.02` Information Discovery & Search | `E07.01` Contextual Information Presentation; `E09.04` Evidence & Insight Presentation | Business outcome, IT contribution only |
| `B01.02` Care Entry & Access Arrangement | Make it possible to request, arrange and enter appropriate care. | `E06.05` Temporal Execution & Scheduling | `E02.01` Governed Information Access; `E07.02` Governed User Interaction; `E02.04` Information Distribution & Notification; `E05.01` Digital Identity & Authentication | Business outcome, IT contribution only |
| `B01.03` Eligibility & Entitlement Determination | Establish a person's basis and conditions for receiving services. | `E05.02` Contextual Authorisation Determination | `E02.01` Governed Information Access; `E09.03` Operational & Business Information Reporting | Business outcome, IT contribution only |
| `B01.04` Informed Participation & Choice | Support people in understanding, expressing preferences and participating in decisions. | `E07.02` Governed User Interaction | `E07.01` Contextual Information Presentation; `E02.01` Governed Information Access | Business outcome, IT contribution only |
| `B01.05` Representation & Carer Participation | Enable duly authorised advocates, carers and related people to assist care. | `E05.02` Contextual Authorisation Determination | `E05.03` Local Access Policy Enforcement; `E07.02` Governed User Interaction; `E02.01` Governed Information Access; `E05.01` Digital Identity & Authentication | Business outcome, IT contribution only |
| `B01.06` Self-Management & Wellbeing Support | Enable ongoing health actions and participation outside formal encounters. | `E07.01` Contextual Information Presentation | `E09.04` Evidence & Insight Presentation; `E02.02` Information Discovery & Search | Business outcome, IT contribution only |

### B02 — Clinical Assessment & Determination
Clinicians and people can establish, assess and revise clinical understanding and appropriate care intent.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B02.01` Clinical Context Establishment | Obtain and interpret what is known about the person's relevant circumstances and history. | `E02.01` Governed Information Access | `E02.02` Information Discovery & Search; `E07.01` Contextual Information Presentation; `E01.02` Information Version & History Management; `E04.03` Semantic Transformation; `E07.04` User-Context Continuity | Real-world business outcome |
| `B02.02` Health & Functional Assessment | Determine current clinical, functional and support needs. | `E07.01` Contextual Information Presentation | `E02.01` Governed Information Access; `E09.02` Information Aggregation & Analysis | Real-world business outcome |
| `B02.03` Clinical Investigation Direction | Decide what further evidence is needed and why. | `E06.01` Task Representation & State Management | `E02.02` Information Discovery & Search; `E03.01` External Information Exchange | Real-world business outcome |
| `B02.04` Diagnostic & Clinical Reasoning | Interpret findings and establish or revise clinical understanding. | `E07.01` Contextual Information Presentation | `E04.04` Semantic Validation; `E09.04` Evidence & Insight Presentation; `E02.01` Governed Information Access | Real-world business outcome |
| `B02.05` Clinical Risk & Priority Determination | Determine urgency, risk and priority of care. | `E07.01` Contextual Information Presentation | `E06.02` Workflow & Work Progression; `E09.04` Evidence & Insight Presentation; `E08.03` Audit & Provenance Reporting | Real-world business outcome |
| `B02.06` Care Decision & Planning | Decide, agree, communicate and revise intended care. | `E06.01` Task Representation & State Management | `E07.02` Governed User Interaction; `E02.04` Information Distribution & Notification; `E01.01` Governed Information Persistence | Real-world business outcome |

### B03 — Clinical Care & Response
Indicated clinical care and direct assistance can be provided, observed, adapted and continued safely.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B03.01` Direct Clinical Intervention | Provide indicated clinical treatments or procedures. | `E07.01` Contextual Information Presentation | `E02.01` Governed Information Access; `E06.01` Task Representation & State Management; `E08.03` Audit & Provenance Reporting | Real-world business outcome |
| `B03.02` Nursing & Direct Care | Provide ongoing nursing, observation and care assistance. | `E06.02` Workflow & Work Progression | `E07.01` Contextual Information Presentation; `E02.01` Governed Information Access; `E07.04` User-Context Continuity | Real-world business outcome |
| `B03.03` Clinical Observation & Response | Observe condition, detect changes and respond appropriately. | `E02.04` Information Distribution & Notification | `E06.02` Workflow & Work Progression; `E07.01` Contextual Information Presentation; `E09.04` Evidence & Insight Presentation | Real-world business outcome |
| `B03.04` Urgent & Emergency Response | Provide timely action when health is seriously threatened. | `E06.02` Workflow & Work Progression | `E02.03` Distributed Information Availability; `E07.01` Contextual Information Presentation; `E08.06` Service Continuity & Recovery | Real-world business outcome |
| `B03.05` Procedural & Peri-Procedural Care | Establish readiness, deliver and follow up clinical procedures. | `E06.01` Task Representation & State Management | `E02.01` Governed Information Access; `E07.01` Contextual Information Presentation; `E08.03` Audit & Provenance Reporting | Real-world business outcome |
| `B03.06` Supportive, Palliative & End-of-Life Care | Provide appropriate comfort, support and care in serious or life-limiting illness. | `E07.01` Contextual Information Presentation | `E02.01` Governed Information Access; `E06.01` Task Representation & State Management | Real-world business outcome |

### B04 — Diagnostic Investigation
Reliable diagnostic evidence can be requested, generated, interpreted, verified and communicated.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B04.01` Diagnostic Investigation Commissioning | Make an appropriate investigation available against a justified and valid request, including preparation and allocation of responsibility. | `E06.01` Task Representation & State Management | `E03.02` Service Contract Exposure & Consumption; `E02.02` Information Discovery & Search; `E04.04` Semantic Validation | Real-world business outcome |
| `B04.02` Diagnostic Evidence Production | Establish observations, measurements, images or other evidence suitable for the intended investigation. | `E03.01` External Information Exchange | `E04.04` Semantic Validation; `E01.03` Information Integrity Management; `E06.01` Task Representation & State Management; `E03.03` Protocol & Format Mediation | Real-world business outcome |
| `B04.03` Diagnostic Findings Reporting | Produce, communicate and appropriately escalate clinically usable findings within relevant professional authority. | `E03.01` External Information Exchange | `E02.04` Information Distribution & Notification; `E07.01` Contextual Information Presentation; `E08.03` Audit & Provenance Reporting; `E03.04` Interaction Acknowledgement & Error Handling; `E04.03` Semantic Transformation | Real-world business outcome |
| `B04.04` Diagnostic Quality & Safety Assurance | Establish appropriate identification, traceability, technical validity and safety of investigations and findings. | `E04.04` Semantic Validation | `E08.03` Audit & Provenance Reporting; `E01.03` Information Integrity Management; `E03.05` Exchange Compatibility & Conformance | Real-world business outcome |

### B05 — Care Coordination & Continuity
Care intentions, contributions and responsibilities can remain coherent over time and across boundaries.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B05.01` Multidisciplinary Care Coordination | Enable several parties to plan and contribute coherently. | `E06.02` Workflow & Work Progression | `E02.04` Information Distribution & Notification; `E07.01` Contextual Information Presentation; `E05.04` Security Context & Passport Propagation | Real-world business outcome |
| `B05.02` Referral & Transfer Coordination | Arrange movement to an appropriate provider, service or care setting. | `E06.02` Workflow & Work Progression | `E03.01` External Information Exchange; `E02.04` Information Distribution & Notification; `E07.02` Governed User Interaction; `E06.03` Event & Parcel Routing; `E06.04` Delivery Reliability & Recovery | Real-world business outcome |
| `B05.03` Care Responsibility Transfer | Establish and communicate who is responsible for continuing care. | `E06.01` Task Representation & State Management | `E02.04` Information Distribution & Notification; `E08.03` Audit & Provenance Reporting; `E07.01` Contextual Information Presentation; `E06.06` Work Context & Checkpoint Preservation | Real-world business outcome |
| `B05.04` Longitudinal Care Coordination | Maintain coherent care intentions and activities over time. | `E01.02` Information Version & History Management | `E02.03` Distributed Information Availability; `E07.03` Longitudinal Information Viewing; `E02.05` Information Access Continuity | Real-world business outcome |
| `B05.05` Follow-Up & Ongoing Care Arrangement | Ensure required follow-up is organised and not lost. | `E06.05` Temporal Execution & Scheduling | `E06.02` Workflow & Work Progression; `E02.04` Information Distribution & Notification; `E08.03` Audit & Provenance Reporting; `E06.04` Delivery Reliability & Recovery | Real-world business outcome |
| `B05.06` Community & Cross-Organisation Continuity | Support continuity where care crosses organisational, geographic or sector boundaries. | `E03.01` External Information Exchange | `E02.03` Distributed Information Availability; `E07.03` Longitudinal Information Viewing; `E02.04` Information Distribution & Notification; `E03.03` Protocol & Format Mediation; `E03.04` Interaction Acknowledgement & Error Handling | Real-world business outcome |

### B06 — Therapies, Medicines & Functional Support
Medicines, rehabilitation and therapeutic or psychosocial support can be appropriately provided and reviewed.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B06.01` Medicines Therapy Management | Prescribe, reconcile, administer, review and optimise medicines as appropriate. | `E02.01` Governed Information Access | `E07.03` Longitudinal Information Viewing; `E06.01` Task Representation & State Management; `E04.04` Semantic Validation | Real-world business outcome |
| `B06.02` Dispensing & Medication Supply | Dispense and make appropriate medicines available. | `E03.01` External Information Exchange | `E06.01` Task Representation & State Management; `E02.04` Information Distribution & Notification; `E07.01` Contextual Information Presentation | Real-world business outcome |
| `B06.03` Functional Rehabilitation & Restoration | Improve or maintain function, participation and independence. | `E07.01` Contextual Information Presentation | `E09.04` Evidence & Insight Presentation; `E06.01` Task Representation & State Management | Real-world business outcome |
| `B06.04` Assistive & Therapeutic Support | Provide equipment, devices and tailored assistance needed by people. | `E07.01` Contextual Information Presentation | `E06.01` Task Representation & State Management; `E02.02` Information Discovery & Search | Real-world business outcome |
| `B06.05` Psychosocial & Behavioural Support | Provide appropriate mental, behavioural and social intervention/support. | `E07.01` Contextual Information Presentation | `E02.02` Information Discovery & Search; `E06.02` Workflow & Work Progression; `E09.04` Evidence & Insight Presentation | Real-world business outcome |

### B07 — Health Service Provision
Healthcare services can be defined, made accessible, resourced, delivered and accounted for.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B07.01` Health Service Portfolio Management | Define the services available and the needs they address. | `E09.03` Operational & Business Information Reporting | `E07.01` Contextual Information Presentation; `E04.01` Information Model Definition & Resolution | Business outcome, IT contribution only |
| `B07.02` Service Provider & Network Participation | Establish appropriate provider relationships and service contributions. | `E02.02` Information Discovery & Search | `E03.02` Service Contract Exposure & Consumption; `E09.03` Operational & Business Information Reporting; `E05.02` Contextual Authorisation Determination; `E03.04` Interaction Acknowledgement & Error Handling | Business outcome, IT contribution only |
| `B07.03` Demand, Capacity & Scheduling | Match service demand with available capacity, including urgent demand. | `E06.05` Temporal Execution & Scheduling | `E09.03` Operational & Business Information Reporting; `E08.01` Operational Observability; `E07.01` Contextual Information Presentation | Business outcome, IT contribution only |
| `B07.04` Service Delivery Coordination | Keep service delivery organised across participants and resources. | `E06.02` Workflow & Work Progression | `E08.01` Operational Observability; `E02.04` Information Distribution & Notification; `E07.01` Contextual Information Presentation; `E06.03` Event & Parcel Routing; `E06.04` Delivery Reliability & Recovery; `E06.06` Work Context & Checkpoint Preservation; `E08.02` Operational Logging & Evidence Collection | Business outcome, IT contribution only |
| `B07.05` Service Access Administration | Maintain the administrative arrangements through which care is accessed. | `E06.05` Temporal Execution & Scheduling | `E07.02` Governed User Interaction; `E02.01` Governed Information Access; `E08.03` Audit & Provenance Reporting; `E05.01` Digital Identity & Authentication; `E07.04` User-Context Continuity | Business outcome, IT contribution only |
| `B07.06` Service Performance & Reporting | Determine and report whether services are being delivered as required. | `E09.03` Operational & Business Information Reporting | `E09.02` Information Aggregation & Analysis; `E08.04` Conformance & Assurance Support; `E09.01` Information Set & Result Organisation | Business outcome, IT contribution only |

### B08 — Workforce & Professional Practice
An appropriately qualified, available and supported workforce can practise safely and effectively.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B08.01` Workforce Supply & Deployment | Obtain and allocate required workforce capacity. | `E06.05` Temporal Execution & Scheduling | `E07.01` Contextual Information Presentation; `E09.03` Operational & Business Information Reporting; `E08.01` Operational Observability | Real-world business outcome |
| `B08.02` Professional Qualification & Credential Assurance | Establish eligibility, qualifications and conditions for practice. | `E02.02` Information Discovery & Search | `E04.04` Semantic Validation; `E05.02` Contextual Authorisation Determination; `E08.03` Audit & Provenance Reporting | Real-world business outcome |
| `B08.03` Scope of Practice & Clinical Privileges | Determine what a practitioner may undertake in the service context. | `E05.02` Contextual Authorisation Determination | `E05.03` Local Access Policy Enforcement; `E07.01` Contextual Information Presentation; `E08.03` Audit & Provenance Reporting | Real-world business outcome |
| `B08.04` Professional Development & Competence | Sustain necessary skills and professional readiness. | `E07.01` Contextual Information Presentation | `E04.01` Information Model Definition & Resolution; `E09.04` Evidence & Insight Presentation; `E09.03` Operational & Business Information Reporting | Real-world business outcome |
| `B08.05` Clinical Supervision & Practice Support | Provide appropriate supervision, consultation and professional support. | `E07.02` Governed User Interaction | `E02.04` Information Distribution & Notification; `E06.02` Workflow & Work Progression | Real-world business outcome |
| `B08.06` Workforce Health, Wellbeing & Readiness | Support workforce health and ability to meet the service mission. | `E07.01` Contextual Information Presentation | `E09.03` Operational & Business Information Reporting; `E05.05` Information Protection & Security Evidence | Real-world business outcome |

### B09 — Clinical Facilities & Supply
Facilities, equipment, goods, logistics and transport can be ready where healthcare requires them.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B09.01` Clinical Facility Availability | Provide suitable places and physical conditions for care. | `E02.02` Information Discovery & Search | `E08.01` Operational Observability; `E06.05` Temporal Execution & Scheduling; `E07.01` Contextual Information Presentation | Real-world business outcome |
| `B09.02` Clinical Equipment Readiness | Make necessary equipment available and safe to use. | `E02.02` Information Discovery & Search | `E08.01` Operational Observability; `E06.05` Temporal Execution & Scheduling | Real-world business outcome |
| `B09.03` Medicines & Therapeutic Goods Supply | Maintain appropriate supply, availability and integrity of regulated goods. | `E02.02` Information Discovery & Search | `E06.01` Task Representation & State Management; `E03.01` External Information Exchange; `E08.03` Audit & Provenance Reporting | Real-world business outcome |
| `B09.04` Consumables & Clinical Logistics | Acquire, store, position and replenish materials used in care. | `E06.02` Workflow & Work Progression | `E02.02` Information Discovery & Search; `E09.03` Operational & Business Information Reporting | Real-world business outcome |
| `B09.05` Patient & Clinical Transport | Move patients and necessary clinical resources where required. | `E06.02` Workflow & Work Progression | `E06.05` Temporal Execution & Scheduling; `E03.01` External Information Exchange; `E08.03` Audit & Provenance Reporting | Real-world business outcome |
| `B09.06` Resource Safety & Availability Assurance | Assess and address gaps affecting resource readiness. | `E08.01` Operational Observability | `E08.05` Operational Fault Management; `E08.06` Service Continuity & Recovery; `E09.03` Operational & Business Information Reporting | Real-world business outcome |

### B10 — Health Information Stewardship
The healthcare enterprise can meet its responsibilities for health records, knowledge, information use and accountability.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B10.01` Health Record Stewardship | Establish and maintain clinically and legally meaningful records. | `E01.01` Governed Information Persistence | `E01.02` Information Version & History Management; `E02.01` Governed Information Access; `E07.03` Longitudinal Information Viewing; `E01.05` Governed Information Reference Management | Business outcome, IT contribution only |
| `B10.02` Information Completeness & Provenance Accountability | Know whether the record has the evidence, source and history necessary for use. | `E01.03` Information Integrity Management | `E08.03` Audit & Provenance Reporting; `E02.02` Information Discovery & Search; `E07.05` Information Presentation Accountability; `E09.01` Information Set & Result Organisation | Business outcome, IT contribution only |
| `B10.03` Appropriate Information Access & Sharing | Make information available to authorised participants for valid business purposes. | `E05.02` Contextual Authorisation Determination | `E05.03` Local Access Policy Enforcement; `E02.01` Governed Information Access; `E02.04` Information Distribution & Notification; `E01.05` Governed Information Reference Management; `E03.03` Protocol & Format Mediation | Business outcome, IT contribution only |
| `B10.04` Consent, Preference & Representation Stewardship | Maintain and respect consent, decision authority and participation preferences. | `E05.02` Contextual Authorisation Determination | `E05.05` Information Protection & Security Evidence; `E07.02` Governed User Interaction; `E01.02` Information Version & History Management | Business outcome, IT contribution only |
| `B10.05` Health Knowledge Stewardship | Curate trusted clinical guidance and domain knowledge for professional use. | `E04.01` Information Model Definition & Resolution | `E04.02` Terminology & Value Set Management; `E04.05` Model Change & Release Governance; `E07.01` Contextual Information Presentation; `E04.03` Semantic Transformation | Business outcome, IT contribution only |
| `B10.06` Retention, Disclosure & Record Accountability | Meet obligations for retention, disclosure, correction and traceability. | `E01.04` Information Retention & Recovery | `E05.03` Local Access Policy Enforcement; `E08.03` Audit & Provenance Reporting; `E09.03` Operational & Business Information Reporting | Business outcome, IT contribution only |
| `B10.07` Information Correction & Reconciliation | Resolve inaccurate, inconsistent or disputed health information without obscuring history. | `E01.02` Information Version & History Management | `E01.03` Information Integrity Management; `E02.01` Governed Information Access; `E04.04` Semantic Validation; `E04.03` Semantic Transformation | Business outcome, IT contribution only |

### B11 — Safety, Quality & Accountability
Healthcare quality, safety, conformance and concerns can be examined, assured, improved and accounted for.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B11.01` Clinical Safety & Risk Management | Identify, reduce and respond to risks arising from care. | `E08.04` Conformance & Assurance Support | `E07.01` Contextual Information Presentation; `E09.04` Evidence & Insight Presentation; `E06.02` Workflow & Work Progression | Real-world business outcome |
| `B11.02` Incident Reporting & Learning | Capture, investigate, respond to and learn from incidents. | `E06.01` Task Representation & State Management | `E08.03` Audit & Provenance Reporting; `E09.04` Evidence & Insight Presentation; `E07.02` Governed User Interaction | Real-world business outcome |
| `B11.03` Clinical Governance & Practice Assurance | Establish accountability for the appropriateness and safety of clinical practice. | `E08.04` Conformance & Assurance Support | `E08.03` Audit & Provenance Reporting; `E04.05` Model Change & Release Governance; `E09.03` Operational & Business Information Reporting | Real-world business outcome |
| `B11.04` Service Quality & Outcome Evaluation | Assess whether service delivery produces acceptable outcomes and experience. | `E09.03` Operational & Business Information Reporting | `E09.04` Evidence & Insight Presentation; `E08.04` Conformance & Assurance Support | Real-world business outcome |
| `B11.05` Regulatory Conformance & Accreditation | Demonstrate and manage conformance with external obligations. | `E08.04` Conformance & Assurance Support | `E08.03` Audit & Provenance Reporting; `E09.03` Operational & Business Information Reporting; `E01.03` Information Integrity Management | Real-world business outcome |
| `B11.06` Complaints, Advocacy & Redress | Receive and address concerns and remedy service shortcomings. | `E07.02` Governed User Interaction | `E06.01` Task Representation & State Management; `E08.03` Audit & Provenance Reporting; `E05.05` Information Protection & Security Evidence | Real-world business outcome |

### B12 — Enterprise Direction & Readiness
The healthcare enterprise can set direction, steward resources, manage risk and demonstrate operational readiness.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B12.01` Strategic Direction & Service Investment | Determine desired outcomes, strategic priorities and investments. | `E09.03` Operational & Business Information Reporting | `E09.04` Evidence & Insight Presentation; `E08.04` Conformance & Assurance Support | Business outcome, IT contribution only |
| `B12.02` Finance, Funding & Commercial Stewardship | Obtain, account for and manage financial and contractual resources. | `E09.03` Operational & Business Information Reporting | `E02.01` Governed Information Access; `E08.04` Conformance & Assurance Support | **Scope to agree** |
| `B12.03` Enterprise Performance & Accountability | Explain enterprise position, performance and delivery of commitments. | `E09.03` Operational & Business Information Reporting | `E08.04` Conformance & Assurance Support; `E07.01` Contextual Information Presentation; `E05.01` Digital Identity & Authentication | Business outcome, IT contribution only |
| `B12.04` Enterprise Risk & Resilience | Understand material risks, prepare for disruption and sustain essential activity. | `E08.06` Service Continuity & Recovery | `E08.01` Operational Observability; `E08.05` Operational Fault Management; `E09.03` Operational & Business Information Reporting; `E06.04` Delivery Reliability & Recovery; `E06.06` Work Context & Checkpoint Preservation; `E08.02` Operational Logging & Evidence Collection | Business outcome, IT contribution only |
| `B12.05` Health Service Readiness & Mobilisation | Demonstrate and restore preparedness to provide required health services. | `E08.01` Operational Observability | `E08.06` Service Continuity & Recovery; `E06.05` Temporal Execution & Scheduling; `E09.03` Operational & Business Information Reporting | Business outcome, IT contribution only |

### B13 — Population Health & Learning
Population health can be assessed and improved, and trustworthy research and learning can influence practice.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B13.01` Population Health Surveillance | Understand the health patterns, threats and needs of populations. | `E09.02` Information Aggregation & Analysis | `E09.03` Operational & Business Information Reporting; `E03.01` External Information Exchange; `E02.02` Information Discovery & Search | Real-world business outcome |
| `B13.02` Health Promotion & Prevention | Prevent avoidable illness and support healthier participation. | `E07.01` Contextual Information Presentation | `E09.03` Operational & Business Information Reporting; `E09.04` Evidence & Insight Presentation | Real-world business outcome |
| `B13.03` Public Health Protection & Response | Coordinate action for population-scale risks and events. | `E06.02` Workflow & Work Progression | `E03.01` External Information Exchange; `E09.03` Operational & Business Information Reporting; `E07.01` Contextual Information Presentation | Real-world business outcome |
| `B13.04` Research Governance & Participant Protection | Establish ethical, legal and methodological conditions for health research. | `E05.02` Contextual Authorisation Determination | `E05.03` Local Access Policy Enforcement; `E08.03` Audit & Provenance Reporting; `E09.05` Governed Analytical Access | **Scope to agree** |
| `B13.05` Health Research Execution | Conduct valid investigations and produce defensible findings. | `E09.05` Governed Analytical Access | `E09.02` Information Aggregation & Analysis; `E01.03` Information Integrity Management; `E08.03` Audit & Provenance Reporting; `E01.05` Governed Information Reference Management; `E04.03` Semantic Transformation; `E09.01` Information Set & Result Organisation | **Scope to agree** |
| `B13.06` Evidence Translation & Practice Improvement | Incorporate trustworthy findings into policy and healthcare practice. | `E04.05` Model Change & Release Governance | `E07.01` Contextual Information Presentation; `E09.04` Evidence & Insight Presentation | Real-world business outcome |

### B14 — Participation, Independence & Support
People can pursue personally meaningful goals, independence and participation with appropriate assistance.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B14.01` Personal Goal & Preference Support | Help a participant articulate, review and change personally meaningful goals and choices. | `E07.01` Contextual Information Presentation | `E07.02` Governed User Interaction; `E02.01` Governed Information Access | Real-world business outcome |
| `B14.02` Support Needs & Circumstances Assessment | Determine what assistance or accommodation would help achieve intended participation outcomes. | `E07.01` Contextual Information Presentation | `E02.01` Governed Information Access; `E09.02` Information Aggregation & Analysis | Real-world business outcome |
| `B14.03` Choice & Support Arrangement | Enable selection, initiation, modification or cessation of appropriate supports. | `E07.02` Governed User Interaction | `E06.05` Temporal Execution & Scheduling; `E02.02` Information Discovery & Search; `E05.02` Contextual Authorisation Determination | Real-world business outcome |
| `B14.04` Ongoing Support Coordination | Maintain coherence of supports across providers, settings and changes in circumstances. | `E06.02` Workflow & Work Progression | `E02.04` Information Distribution & Notification; `E07.01` Contextual Information Presentation; `E08.03` Audit & Provenance Reporting; `E07.04` User-Context Continuity | Real-world business outcome |
| `B14.05` Independent Living & Community Participation | Enable access to activities, environments, relationships and resources needed for everyday participation. | `E07.01` Contextual Information Presentation | `E02.02` Information Discovery & Search; `E06.05` Temporal Execution & Scheduling | **Scope to agree** |
| `B14.06` Participant Safeguarding & Supported Decision Participation | Respect wishes, prevent avoidable harm, and support decisions or representation within appropriate authority. | `E05.02` Contextual Authorisation Determination | `E05.03` Local Access Policy Enforcement; `E07.02` Governed User Interaction; `E08.03` Audit & Provenance Reporting; `E05.01` Digital Identity & Authentication | Real-world business outcome |

### B15 — Health System Funding & Commissioning
Services and supports can be commissioned, purchased, financed and evaluated against population need.

| Business CT2 | Meaning | First candidate IT contribution | Other contribution contexts | Delivery qualification |
|---|---|---|---|---|
| `B15.01` Health Needs-Based Commissioning | Identify the services/outcomes a population or covered group requires and arrange service provision. | `E09.03` Operational & Business Information Reporting | `E09.02` Information Aggregation & Analysis; `E02.02` Information Discovery & Search | **Scope to agree** |
| `B15.02` Coverage & Entitlement Administration | Establish and maintain the funding terms under which people may access services or supports. | `E05.02` Contextual Authorisation Determination | `E02.01` Governed Information Access; `E09.03` Operational & Business Information Reporting; `E07.02` Governed User Interaction | **Scope to agree** |
| `B15.03` Health Service Purchasing & Contract Stewardship | Arrange and oversee the purchase of services from providers. | `E09.03` Operational & Business Information Reporting | `E08.04` Conformance & Assurance Support; `E02.02` Information Discovery & Search | **Scope to agree** |
| `B15.04` Claims & Funding Accountability | Account for qualifying service activity and applicable payment arrangements. | `E02.01` Governed Information Access | `E09.03` Operational & Business Information Reporting; `E08.03` Audit & Provenance Reporting; `E01.01` Governed Information Persistence | **Scope to agree** |
| `B15.05` Funding Outcomes & Equity Review | Evaluate whether funding arrangements meet intended access, quality and fairness goals. | `E09.02` Information Aggregation & Analysis | `E09.04` Evidence & Insight Presentation; `E08.04` Conformance & Assurance Support | **Scope to agree** |

## 4. Enterprise CT2 reverse demand

The reverse view is essential: a coherent Enterprise Capability may support multiple, quite different Business Capabilities. Listed business consumers are candidate evidence of need—not instances, home assignments or implementation confirmation.
### E01 — Governed Information Custody
| Enterprise CT2 | Candidate Business CT2 consumers (count) | Example consumers |
|---|---:|---|
| `E01.01` Governed Information Persistence | 3 | `B02.06` Care Decision & Planning; `B10.01` Health Record Stewardship; `B15.04` Claims & Funding Accountability |
| `E01.02` Information Version & History Management | 5 | `B02.01` Clinical Context Establishment; `B05.04` Longitudinal Care Coordination; `B10.01` Health Record Stewardship; `B10.04` Consent, Preference & Representation Stewardship; `B10.07` Information Correction & Reconciliation |
| `E01.03` Information Integrity Management | 6 | `B04.02` Diagnostic Evidence Production; `B04.04` Diagnostic Quality & Safety Assurance; `B10.02` Information Completeness & Provenance Accountability; `B10.07` Information Correction & Reconciliation; `B11.05` Regulatory Conformance & Accreditation |
| `E01.04` Information Retention & Recovery | 1 | `B10.06` Retention, Disclosure & Record Accountability |
| `E01.05` Governed Information Reference Management | 3 | `B10.01` Health Record Stewardship; `B10.03` Appropriate Information Access & Sharing; `B13.05` Health Research Execution |

### E02 — Information Access, Discovery & Distribution
| Enterprise CT2 | Candidate Business CT2 consumers (count) | Example consumers |
|---|---:|---|
| `E02.01` Governed Information Access | 21 | `B01.02` Care Entry & Access Arrangement; `B01.03` Eligibility & Entitlement Determination; `B01.04` Informed Participation & Choice; `B01.05` Representation & Carer Participation; `B02.01` Clinical Context Establishment |
| `E02.02` Information Discovery & Search | 19 | `B01.01` Health Needs & Service Navigation; `B01.06` Self-Management & Wellbeing Support; `B02.01` Clinical Context Establishment; `B02.03` Clinical Investigation Direction; `B04.01` Diagnostic Investigation Commissioning |
| `E02.03` Distributed Information Availability | 3 | `B03.04` Urgent & Emergency Response; `B05.04` Longitudinal Care Coordination; `B05.06` Community & Cross-Organisation Continuity |
| `E02.04` Information Distribution & Notification | 14 | `B01.02` Care Entry & Access Arrangement; `B02.06` Care Decision & Planning; `B03.03` Clinical Observation & Response; `B04.03` Diagnostic Findings Reporting; `B05.01` Multidisciplinary Care Coordination |
| `E02.05` Information Access Continuity | 1 | `B05.04` Longitudinal Care Coordination |

### E03 — Digital Interoperability & Service Interaction
| Enterprise CT2 | Candidate Business CT2 consumers (count) | Example consumers |
|---|---:|---|
| `E03.01` External Information Exchange | 10 | `B02.03` Clinical Investigation Direction; `B04.02` Diagnostic Evidence Production; `B04.03` Diagnostic Findings Reporting; `B05.02` Referral & Transfer Coordination; `B05.06` Community & Cross-Organisation Continuity |
| `E03.02` Service Contract Exposure & Consumption | 2 | `B04.01` Diagnostic Investigation Commissioning; `B07.02` Service Provider & Network Participation |
| `E03.03` Protocol & Format Mediation | 3 | `B04.02` Diagnostic Evidence Production; `B05.06` Community & Cross-Organisation Continuity; `B10.03` Appropriate Information Access & Sharing |
| `E03.04` Interaction Acknowledgement & Error Handling | 3 | `B04.03` Diagnostic Findings Reporting; `B05.06` Community & Cross-Organisation Continuity; `B07.02` Service Provider & Network Participation |
| `E03.05` Exchange Compatibility & Conformance | 1 | `B04.04` Diagnostic Quality & Safety Assurance |

### E04 — Semantic Meaning & Model Governance
| Enterprise CT2 | Candidate Business CT2 consumers (count) | Example consumers |
|---|---:|---|
| `E04.01` Information Model Definition & Resolution | 3 | `B07.01` Health Service Portfolio Management; `B08.04` Professional Development & Competence; `B10.05` Health Knowledge Stewardship |
| `E04.02` Terminology & Value Set Management | 1 | `B10.05` Health Knowledge Stewardship |
| `E04.03` Semantic Transformation | 5 | `B02.01` Clinical Context Establishment; `B04.03` Diagnostic Findings Reporting; `B10.05` Health Knowledge Stewardship; `B10.07` Information Correction & Reconciliation; `B13.05` Health Research Execution |
| `E04.04` Semantic Validation | 7 | `B02.04` Diagnostic & Clinical Reasoning; `B04.01` Diagnostic Investigation Commissioning; `B04.02` Diagnostic Evidence Production; `B04.04` Diagnostic Quality & Safety Assurance; `B06.01` Medicines Therapy Management |
| `E04.05` Model Change & Release Governance | 3 | `B10.05` Health Knowledge Stewardship; `B11.03` Clinical Governance & Practice Assurance; `B13.06` Evidence Translation & Practice Improvement |

### E05 — Identity, Trust & Information Protection
| Enterprise CT2 | Candidate Business CT2 consumers (count) | Example consumers |
|---|---:|---|
| `E05.01` Digital Identity & Authentication | 5 | `B01.02` Care Entry & Access Arrangement; `B01.05` Representation & Carer Participation; `B07.05` Service Access Administration; `B12.03` Enterprise Performance & Accountability; `B14.06` Participant Safeguarding & Supported Decision Participation |
| `E05.02` Contextual Authorisation Determination | 11 | `B01.03` Eligibility & Entitlement Determination; `B01.05` Representation & Carer Participation; `B07.02` Service Provider & Network Participation; `B08.02` Professional Qualification & Credential Assurance; `B08.03` Scope of Practice & Clinical Privileges |
| `E05.03` Local Access Policy Enforcement | 6 | `B01.05` Representation & Carer Participation; `B08.03` Scope of Practice & Clinical Privileges; `B10.03` Appropriate Information Access & Sharing; `B10.06` Retention, Disclosure & Record Accountability; `B13.04` Research Governance & Participant Protection |
| `E05.04` Security Context & Passport Propagation | 1 | `B05.01` Multidisciplinary Care Coordination |
| `E05.05` Information Protection & Security Evidence | 3 | `B08.06` Workforce Health, Wellbeing & Readiness; `B10.04` Consent, Preference & Representation Stewardship; `B11.06` Complaints, Advocacy & Redress |

### E06 — Digital Work, Events & Temporal Coordination
| Enterprise CT2 | Candidate Business CT2 consumers (count) | Example consumers |
|---|---:|---|
| `E06.01` Task Representation & State Management | 15 | `B02.03` Clinical Investigation Direction; `B02.06` Care Decision & Planning; `B03.01` Direct Clinical Intervention; `B03.05` Procedural & Peri-Procedural Care; `B03.06` Supportive, Palliative & End-of-Life Care |
| `E06.02` Workflow & Work Progression | 15 | `B02.05` Clinical Risk & Priority Determination; `B03.02` Nursing & Direct Care; `B03.03` Clinical Observation & Response; `B03.04` Urgent & Emergency Response; `B05.01` Multidisciplinary Care Coordination |
| `E06.03` Event & Parcel Routing | 2 | `B05.02` Referral & Transfer Coordination; `B07.04` Service Delivery Coordination |
| `E06.04` Delivery Reliability & Recovery | 4 | `B05.02` Referral & Transfer Coordination; `B05.05` Follow-Up & Ongoing Care Arrangement; `B07.04` Service Delivery Coordination; `B12.04` Enterprise Risk & Resilience |
| `E06.05` Temporal Execution & Scheduling | 11 | `B01.02` Care Entry & Access Arrangement; `B05.05` Follow-Up & Ongoing Care Arrangement; `B07.03` Demand, Capacity & Scheduling; `B07.05` Service Access Administration; `B08.01` Workforce Supply & Deployment |
| `E06.06` Work Context & Checkpoint Preservation | 3 | `B05.03` Care Responsibility Transfer; `B07.04` Service Delivery Coordination; `B12.04` Enterprise Risk & Resilience |

### E07 — Information Presentation & Human Interaction
| Enterprise CT2 | Candidate Business CT2 consumers (count) | Example consumers |
|---|---:|---|
| `E07.01` Contextual Information Presentation | 38 | `B01.01` Health Needs & Service Navigation; `B01.04` Informed Participation & Choice; `B01.06` Self-Management & Wellbeing Support; `B02.01` Clinical Context Establishment; `B02.02` Health & Functional Assessment |
| `E07.02` Governed User Interaction | 14 | `B01.02` Care Entry & Access Arrangement; `B01.04` Informed Participation & Choice; `B01.05` Representation & Carer Participation; `B02.06` Care Decision & Planning; `B05.02` Referral & Transfer Coordination |
| `E07.03` Longitudinal Information Viewing | 4 | `B05.04` Longitudinal Care Coordination; `B05.06` Community & Cross-Organisation Continuity; `B06.01` Medicines Therapy Management; `B10.01` Health Record Stewardship |
| `E07.04` User-Context Continuity | 4 | `B02.01` Clinical Context Establishment; `B03.02` Nursing & Direct Care; `B07.05` Service Access Administration; `B14.04` Ongoing Support Coordination |
| `E07.05` Information Presentation Accountability | 1 | `B10.02` Information Completeness & Provenance Accountability |

### E08 — Operational Observability, Assurance & Resilience
| Enterprise CT2 | Candidate Business CT2 consumers (count) | Example consumers |
|---|---:|---|
| `E08.01` Operational Observability | 8 | `B07.03` Demand, Capacity & Scheduling; `B07.04` Service Delivery Coordination; `B08.01` Workforce Supply & Deployment; `B09.01` Clinical Facility Availability; `B09.02` Clinical Equipment Readiness |
| `E08.02` Operational Logging & Evidence Collection | 2 | `B07.04` Service Delivery Coordination; `B12.04` Enterprise Risk & Resilience |
| `E08.03` Audit & Provenance Reporting | 23 | `B02.05` Clinical Risk & Priority Determination; `B03.01` Direct Clinical Intervention; `B03.05` Procedural & Peri-Procedural Care; `B04.03` Diagnostic Findings Reporting; `B04.04` Diagnostic Quality & Safety Assurance |
| `E08.04` Conformance & Assurance Support | 10 | `B07.06` Service Performance & Reporting; `B11.01` Clinical Safety & Risk Management; `B11.03` Clinical Governance & Practice Assurance; `B11.04` Service Quality & Outcome Evaluation; `B11.05` Regulatory Conformance & Accreditation |
| `E08.05` Operational Fault Management | 2 | `B09.06` Resource Safety & Availability Assurance; `B12.04` Enterprise Risk & Resilience |
| `E08.06` Service Continuity & Recovery | 4 | `B03.04` Urgent & Emergency Response; `B09.06` Resource Safety & Availability Assurance; `B12.04` Enterprise Risk & Resilience; `B12.05` Health Service Readiness & Mobilisation |

### E09 — Information Insight & Evidence Use
| Enterprise CT2 | Candidate Business CT2 consumers (count) | Example consumers |
|---|---:|---|
| `E09.01` Information Set & Result Organisation | 3 | `B07.06` Service Performance & Reporting; `B10.02` Information Completeness & Provenance Accountability; `B13.05` Health Research Execution |
| `E09.02` Information Aggregation & Analysis | 7 | `B02.02` Health & Functional Assessment; `B07.06` Service Performance & Reporting; `B13.01` Population Health Surveillance; `B13.05` Health Research Execution; `B14.02` Support Needs & Circumstances Assessment |
| `E09.03` Operational & Business Information Reporting | 26 | `B01.03` Eligibility & Entitlement Determination; `B07.01` Health Service Portfolio Management; `B07.02` Service Provider & Network Participation; `B07.03` Demand, Capacity & Scheduling; `B07.06` Service Performance & Reporting |
| `E09.04` Evidence & Insight Presentation | 15 | `B01.01` Health Needs & Service Navigation; `B01.06` Self-Management & Wellbeing Support; `B02.04` Diagnostic & Clinical Reasoning; `B02.05` Clinical Risk & Priority Determination; `B03.03` Clinical Observation & Response |
| `E09.05` Governed Analytical Access | 2 | `B13.04` Research Governance & Participant Protection; `B13.05` Health Research Execution |

## 5. Selected CT3-level semantic stress tests

These are the **only** CT3-to-CT3 associations in this iteration subject to individual semantic reasoning. The remaining 238 Business CT3s are indexed by their parent CT2 context, **not** asserted to have proven CT3/Feature correspondence. Every Feature remains an explicit unsatisfied demand pending contract and test evidence.

| Test | Business CT3 | Candidate Enterprise CT3 | Important distinction |
|---|---|---|---|
| SC01 | `B05.03.01` Clinical Handover Acceptance | `E06.01.03` Work Lifecycle Visibility; `E02.04.01` Selective Information Distribution; `E08.03.02` Auditable Activity History | Handover acceptance requires a receiving business actor; dispatching a notification is not evidence of acceptance. |
| SC02 | `B05.03.03` Outstanding Obligation Transfer | `E06.01.01` Actionable Work Representation; `E06.04.03` Delivery Outcome Visibility; `E08.03.02` Auditable Activity History | Outstanding obligations must retain ownership across a handover even if transport is delivered. |
| SC03 | `B10.03.02` Purpose-Appropriate Information Disclosure | `E05.02.02` Purpose-Scoped Permission; `E05.03.01` Boundary-Enforced Access; `E02.01.01` Authorised Information Retrieval | Purpose-appropriate disclosure requires contextual authorisation and local enforcement before information retrieval. |
| SC04 | `B10.02.02` Clinical Information Source Accountability | `E01.03.02` Information Origin Preservation; `E08.03.03` Information Provenance Visibility; `E07.05.01` Source-Attributed Information Display | Information source accountability requires origin, provenance and display attribution, not merely having records. |
| SC05 | `B10.07.03` Cross-Source Information Reconciliation | `E04.03.01` Governed Representation Translation; `E01.02.01` Historic Version Retrieval; `E02.02.01` Version-Referenced Information Search | Cross-source reconciliation requires representation mediation and versioned traceability; transformation does not silently replace source truth. |
| SC06 | `B08.03.01` Service-Specific Clinical Privileging | `E05.02.01` Context-Sensitive Access Decision; `E05.03.01` Boundary-Enforced Access; `E02.02.02` Contextual Information Discovery | Clinical privileging is business authority; digital authentication alone is not clinical authority to act. |
| SC07 | `B04.03.03` Critical Finding Communication | `E02.04.02` Information Availability Notification; `E06.04.03` Delivery Outcome Visibility; `E03.04.03` Bounded Interaction Outcome | Critical result communication requires escalated receipt/outcome; exchange receipt cannot alone prove clinical recipient action. |
| SC08 | `B04.04.02` Verified Diagnostic Result Release | `E04.04.01` Information Model Conformance Checking; `E01.01.01` Durable Information Acceptance; `E08.03.02` Auditable Activity History | Verification, release and authoritative information custody are distinguishable from diagnosis. |
| SC09 | `B14.06.02` Supported Decision Participation | `E07.02.02` Review & Confirmation Interaction; `E05.02.02` Purpose-Scoped Permission; `E07.01.03` Participant Support Context Presentation | Supported decision participation must respect the participant perspective and legitimate authority; no assumed Patient inheritance. |
| SC10 | `B15.04.01` Service Claim Assessment | `E09.03.02` Business Information Reporting; `E05.02.01` Context-Sensitive Access Decision; `E02.01.01` Authorised Information Retrieval | Digital access, decision evidence and reporting do not automatically mean Harmonia contains a claim adjudication engine. |
| SC11 | `B13.05.02` Governed Research Evidence Collection | `E09.05.01` Purpose-Bound Analytical Access; `E01.03.02` Information Origin Preservation; `E09.02.02` Repeatable Information Analysis | Research evidence collection requires purpose-bounded access and source integrity, not unrestricted analytical copying. |
| SC12 | `B12.04.02` Business Continuity Preparedness | `E08.06.03` Recovery Readiness Evidence; `E08.01.03` Dependency Status Visibility; `E01.04.03` Custody Recovery | Business continuity preparedness can be evidenced by ICT recovery readiness but is not identical to ICT recovery. |
| SC13 | `B10.05.02` Knowledge Change Review | `E04.05.02` Model Compatibility Assessment; `E04.05.01` Controlled Semantic Publication; `E03.05.01` Versioned Contract Compatibility | Knowledge change review requires model compatibility and controlled publication; no automatic live value-set change. |
| SC14 | `B05.04.02` Cross-Episode Care Context | `E07.03.01` Longitudinal Health Record Viewing; `E02.03.01` Distributed Access; `E01.02.01` Historic Version Retrieval | Cross-episode context needs continuity and historical versions, but aggregated presentation is not the authoritative record. |
| SC15 | `B11.05.02` Accreditation Evidence Preparation | `E08.04.01` Release Conformance Evidence; `E08.03.02` Auditable Activity History; `E09.03.03` Controlled Information Export | Accreditation evidence requires conformance and attributable records, not a mere dashboard. |
| SC16 | `B03.03.02` Deterioration Recognition & Escalation | `E02.04.02` Information Availability Notification; `E07.01.01` Clinical Context Presentation; `E06.02.01` Coordinated Work Progression | Digital alerts can assist deterioration escalation; clinical assessment and intervention remain real-world professional abilities. |
| SC17 | `B01.05.01` Authorised Care Representation | `E05.02.01` Context-Sensitive Access Decision; `E07.02.04` Controlled Interaction Scope; `E08.03.02` Auditable Activity History | A representative may act only within established authority and scope; identity alone is insufficient. |
| SC18 | `B02.04.01` Clinical Finding Interpretation | `E04.04.03` Contextual Semantic Compatibility; `E07.01.01` Clinical Context Presentation; `E02.01.03` Scoped Information Selection | Semantic validation and information presentation assist clinical interpretation; they do not establish clinical diagnostic correctness. |
| SC19 | `B09.05.03` Clinical Specimen and Resource Transport | `E06.02.01` Coordinated Work Progression; `E06.05.01` Scheduled Work Execution | Physical movement of specimens is NOT the same concept as Artemis routing a PetasosParcel. Digital work coordination may support physical transport. |
| SC20 | `B07.03.02` Service Capacity Allocation | `E06.05.01` Scheduled Work Execution; `E08.01.02` Operational Performance Visibility; `E07.01.04` Service Operations Context Presentation | Service capacity allocation is a business decision; scheduling software and monitoring are contributing information services. |

## 6. Reconciliation findings

Findings are architectural questions or design extensions, **not** accepted additional Capabilities. “High” denotes impact on correctness/scope, not automatic approval.

### R01 — Policy definition, Passport construction and authoritative maintenance · HIGH
**Context:** `E05.02`, `E05.04` · Business `B10.03`, `B10.04`, `B12.03`, `B01.05`  
**Finding:** The DRP distinguishes PR policy authority, preconstructed Passports, PD distribution and local PEP. Candidate 006 emphasises determination/propagation but not the authoritative policy-and-Passport lifecycle as an independently assessable capability.  
**Proposed disposition:** Depth-first CT3/Feature expansion; assess separate CT2 only if this responsibility cannot sit coherently within current E05.02/E05.04.  
**Evidence:** [DRP-Themis-Policy-Passport-Enforcement.draft.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Themis-Policy-Passport-Enforcement.draft.001.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — CANDIDATE_EXTENSION

### R02 — Invalidated or stale Passport must never authorise · HIGH
**Context:** `E05.03`, `E05.04` · Business `B10.03`, `B10.04`, `B14.06`  
**Finding:** Explicit revalidation, withdrawal, currency and fail-closed decisions are required across distributed PEPs, not merely passing security context.  
**Proposed disposition:** Add measurable currency/withdrawal/invalid-token scenario Features; preserve Themis model/operational authority distinction.  
**Evidence:** [DRP-Themis-Policy-Passport-Enforcement.draft.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Themis-Policy-Passport-Enforcement.draft.001.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — CANDIDATE_EXTENSION

### R03 — AuditEvidence and Provenance capture are distinct from reporting · HIGH
**Context:** `E08.02`, `E08.03` · Business `B10.02`, `B11.02`, `B11.03`, `B11.05`  
**Finding:** E08.03 is titled Reporting; immutable record origination, near-source contribution and governed management are different abilities.  
**Proposed disposition:** Expand E08.03 CT3/Features for capture, immutable append, lineage correlation; keep operational logs separate.  
**Evidence:** [DRP-Mneme-Audit.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Audit.draft.002.md); [DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — CANDIDATE_EXTENSION

### R04 — Raw record export versus complete dereferenced evidence export · HIGH
**Context:** `E08.03`, `E09.03`, `E05.03` · Business `B10.06`, `B11.05`, `B13.04`  
**Finding:** Bulk/time-window raw export is not equivalent to privileged, all-or-nothing Kleio/Dokimasia dereferenced export; the latter must fail without disclosure on failed authorisation.  
**Proposed disposition:** Distinct Service and Feature obligations, with complete-set authorisation, traceable failure and no partial release.  
**Evidence:** [DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — CANDIDATE_EXTENSION

### R05 — InformationUnit structure and semantics independent of storage/technology · HIGH
**Context:** `E01.01`, `E01.02`, `E01.05`, `E04.01` · Business `B10.01`, `B10.02`, `B10.07`  
**Finding:** One InformationRepresentation per IU, identity distinct from external entity, container/content metadata separation, and version distinct from active generation need explicit coverage tests.  
**Proposed disposition:** Expand/qualify Features across existing E01/E04 without turning IU into a Mneme or Mnemosyne-owned object.  
**Evidence:** [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R06 — Governed information state and work outcome require distinct acceptance · HIGH
**Context:** `E01.01`, `E02.01`, `E06.01`, `E06.06` · Business `B05.03`, `B10.01`, `B11.02`  
**Finding:** ProcessSomething may succeed while Commit has failed or returned indeterminate; receipt, cache and provider acknowledgement are not authoritative acceptance.  
**Proposed disposition:** Require cross-domain failure tests and outcome vocabulary; not a combined ProcessAndCommit service.  
**Evidence:** [DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md); [DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R07 — Pragma, Pragmaton, Petasos custody and IU version are separate states · HIGH
**Context:** `E06.01`, `E06.04`, `E06.06`, `E01.01` · Business `B05.03`, `B07.04`, `B10.02`  
**Finding:** Task envelope progression, working context, durable work custody and authoritative information commitment carry different identity and state obligations.  
**Proposed disposition:** Introduce stage-specific integrity assertions and distinct consumer-facing service intents.  
**Evidence:** [DRP-Mneme-Pragma-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragma-Model.draft.002.md); [DRP-Mneme-Pragmaton-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Model.draft.002.md); [DRP-Interaction-Petasos-to-Mneme.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R08 — Exclusive parcel routing and non-DT workflow need semantic coverage regardless of component inventory · HIGH
**Context:** `E06.02`, `E06.03`, `E06.04` · Business `B05.02`, `B07.04`, `B12.04`  
**Finding:** Artemis routing and Praxis non-DT workflow meanings are established but named native components were absent from Candidate 010 inventory. This is not grounds to change EC topology.  
**Proposed disposition:** Keep E06 service obligations solution-independent; refer Facet structure reconciliation to later Information Systems pass.  
**Evidence:** [DRP-Interaction-Petasos-to-Mneme.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — SYSTEMS_RECONCILIATION

### R09 — FHIR/external representations and IU references do not collapse into one identifier · HIGH
**Context:** `E02.01`, `E02.02`, `E03.03`, `E04.03` · Business `B02.01`, `B04.03`, `B10.01`  
**Finding:** Managed identity, information coordinates, content representation and external identifiers remain distinguishable, even where a technology handles them together.  
**Proposed disposition:** Test cross-format retrieval and identity/coordinate resolution; preserve standards-native representation by explicit design where required.  
**Evidence:** [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md); [DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R10 — Information received by HIE remains available as governed information · MEDIUM
**Context:** `E02.03`, `E02.05`, `E01.04` · Business `B05.04`, `B05.06`, `B10.03`  
**Finding:** Distributed access, access continuity and durable custody should be jointly assessed for information received into Harmonia.  
**Proposed disposition:** Add acceptance/availability/adverse isolation tests and determine solution release scope.  
**Evidence:** [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R11 — Clinical and nonclinical participant interaction must remain distinguishable · HIGH
**Context:** `E07.01`, `E07.02`, `E07.04` · Business `B01.04`, `B14.01`, `B14.03`, `B14.06`  
**Finding:** Clinical UI examples do not exhaust disability/independent-living/support coordination demands; supported decision-making is not ordinary clinician consent.  
**Proposed disposition:** Retain B14 as a distinct business domain; develop separate Journey scenarios and avoid inventing R1 accessibility conformance.  
**Evidence:** Candidate 006;  Candidate 003 stakeholder Journeys  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R12 — Commissioning, claims, and entitlement may exceed Harmonia delivery scope · MEDIUM
**Context:** `E09.02`, `E09.03`, `E05.02`, `E02.01` · Business `B15.01`, `B15.02`, `B15.03`, `B15.04`, `B15.05`  
**Finding:** B15 is a valid Business Capability regardless of whether Harmonia delivers payer functions. A map is not a build commitment.  
**Proposed disposition:** Record digital contribution and solution-boundary qualifications; no automatic claims/payment engine.  
**Evidence:** Candidate 003/006 Journeys J26/J27  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — SCOPE_BOUNDARY

### R13 — Calliope release and semantic compatibility need gated rather than dynamic changes · HIGH
**Context:** `E04.02`, `E04.05`, `E03.05` · Business `B10.05`, `B11.03`, `B13.06`  
**Finding:** Value sets/models are release-governed; independent acceptance and dependent-system gating are unresolved in design history.  
**Proposed disposition:** Confirm regression/compatibility test obligations; do not assume unreviewed dynamic ontology or terminology publication.  
**Evidence:** Candidate 006;  DRP-Information-Unit-Framework.draft.003.md  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R14 — Rsyslog operational log transport must not subsume Audit/Provenance · MEDIUM
**Context:** `E08.02`, `E08.03`, `E08.06` · Business `B10.02`, `B11.02`, `B12.04`  
**Finding:** In-JVM satellite to rsyslog-based server and independent Log-Persistence have distinct failure semantics and custody from immutable AuditEvent/Provenance.  
**Proposed disposition:** Keep logging assurance distinct; transport technology choices inform later solution, not EC decomposition.  
**Evidence:** [DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R15 — AI-based assurance and insight remain observational or advisory at R1/R2 · MEDIUM
**Context:** `E08.04`, `E09.04`, `E09.05` · Business `B11.03`, `B12.03`, `B13.06`  
**Finding:** Recommended action may be presented, but not treated as authority to commit protected work or information.  
**Proposed disposition:** Strengthen advisory status and no-authority test; confirm release applicability.  
**Evidence:** Candidate 006;  established design intent  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R16 — Exchange receipt versus authoritative clinical information acceptance · MEDIUM
**Context:** `E03.04`, `E01.01`, `E02.03` · Business `B04.03`, `B05.06`, `B10.01`  
**Finding:** MLLP/API acknowledgement and authoritative durable persistence are different outcomes; external standards contract precedence survives.  
**Proposed disposition:** Test sender retries and failure between external response and durable acceptance; no ungrounded ACK promise.  
**Evidence:** [DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R17 — Central scheduler/calendar and temporal consistency requirements · MEDIUM
**Context:** `E06.05`, `E06.06`, `E01.03` · Business `B05.05`, `B07.03`, `B12.05`  
**Finding:** Single central scheduler/calendar direction and multi-timezone temporal evidence should remain cross-cutting, not duplicated for each workflow.  
**Proposed disposition:** Preserve central temporal responsibility and assess deadlines/late deliveries/recovery.  
**Evidence:** Candidate 006;  Continuum NFR-derived elements  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R18 — Journey stages must preserve expected value, exceptions and independent evidence · HIGH
**Context:** `E02.01`, `E05.03`, `E06.01`, `E07.01`, `E08.03`, `E09.04` · Business `B01.02`, `B05.03`, `B10.02`, `B11.05`, `B14.04`  
**Finding:** Current Journeys are proposed and only coarsely linked by CT1; stage-specific obligations should not claim implementation satisfaction.  
**Proposed disposition:** Use the 29 Journey stage rows and independent failure probes; review stage anchors and acceptance criteria.  
**Evidence:** Candidate 003/006;  Candidate 009 accepted Scope  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — INTEGRITY_TEST

### R19 — Retain existing Continuum identities and classify correspondence by semantics · HIGH
**Context:** `E01.01`, `E02.01`, `E03.02`, `E05.02`, `E06.01`, `E08.03` · Business `B10.01`, `B07.04`, `B11.05`  
**Finding:** 142 Business Services and five Application Services currently have distinct canonical identities; equivalent names do not prove equivalent semantics.  
**Proposed disposition:** Carry all source identities into later matching register; no new canonical IDs or retirements in this study.  
**Evidence:** [Continuum](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/);  HCRL 1.1.20/001  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — GOVERNANCE

### R20 — Large binary payloads are referenced; generic relational custody is not the media store · HIGH
**Context:** `E01.05`, `E02.01`, `E01.01` · Business `B10.01`, `B10.06`  
**Finding:** Large-content governance belongs to IU references and fit-for-purpose media custody; not PostgreSQL large-blob storage.  
**Proposed disposition:** Test reference validity, permissions and media retrieval separately from ordinary IU payload persistence.  
**Evidence:** [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md);  established NFR-087  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R21 — Concurrency and capacity admission must not silently lose accepted work · MEDIUM
**Context:** `E06.02`, `E06.04`, `E08.06` · Business `B07.03`, `B12.04`, `B05.03`  
**Finding:** Concurrent work obeys defined policies; overload may reject with explicit reporting but accepted work remains recoverable.  
**Proposed disposition:** Test accept/reject boundary and failure at retry/checkpoint before claim of performance satisfaction.  
**Evidence:** [Motivation / NFRs](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/motivation/)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R22 — Local PEP, inherited/forwarded enforcement scope and masked obligations · MEDIUM
**Context:** `E05.02`, `E05.03`, `E05.04` · Business `B10.03`, `B14.06`, `B08.03`  
**Finding:** PEP placement and scoped forwarding/inheritance are not equal to centralised authorisation decisions; Themis masks and required criteria remain policy constraints.  
**Proposed disposition:** Expose independent enforcement/scoping assessments; do not confuse component identity with capability.  
**Evidence:** [DRP-Themis-Policy-Passport-Enforcement.draft.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Themis-Policy-Passport-Enforcement.draft.001.md);  established design intent  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — CANDIDATE_EXTENSION

### R23 — Research governed access and reversible qualification separate from uncontrolled data export · MEDIUM
**Context:** `E09.05`, `E08.03`, `E05.02`, `E01.03` · Business `B13.04`, `B13.05`, `B10.03`  
**Finding:** Request-specific authorisation, provenance and purpose binding remain applicable to analytic/result reuse.  
**Proposed disposition:** Test purpose, authorisation, source version and release qualification; not general population research system commitment.  
**Evidence:** [DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

### R24 — Service resilience and disaster recovery preserve governing constraints · MEDIUM
**Context:** `E08.06`, `E01.04`, `E06.06` · Business `B12.04`, `B12.05`, `B07.04`  
**Finding:** Recovery cannot silently turn uncertain outcomes into success or alter IU identity/version; work does not resume inside failed Ergon.  
**Proposed disposition:** Add explicit failure-recovery truth table; distinguish recoverable work and information persistence.  
**Evidence:** [DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md); [DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md)  
**Status:** UNRESOLVED_REQUIRES_ARCHITECTURAL_REVIEW — COVERAGE_TEST

## 7. Stakeholder and Journey stage integrity

### Stakeholder perspective register
| Ref | Stakeholder perspective | Participation context |
|---|---|---|
| `S01` | Patient | Emergency patient, person receiving ongoing clinical care, surgical patient |
| `S02` | Practitioner (including specialists) | GP, intensivist, surgeon, visiting specialist |
| `S03` | Allied Health | Occupational therapist, physiotherapist, speech pathologist, psychologist, social worker where relevant |
| `S04` | Nurse & Support | Registered/enrolled nurse, midwife, nurse practitioner, care assistant |
| `S05` | Diagnostics People | Pathology scientist, diagnostic technician, sonographer, radiologist |
| `S06` | Operations — healthcare | Ward operations, health-service manager, clinic operations, deployment support |
| `S07` | Administrators — healthcare | Scheduling, referrals, admissions, medical records, service billing |
| `S08` | Executive | Portfolio executive, CEO, governing body, Defence command |
| `S09` | Regulatory / Government | Public-health authority, accreditation body, policy authority, pharmaceutical regulator; potential commissioning context |
| `S10` | Researchers | Clinical trial team, data custodian in research context, evidence synthesis specialist |
| `S11` | Operations — systems | ICT operations, reliability, incident management, service recovery |
| `S12` | Administrators — systems | Access administration, controlled configuration, semantic-release administration |
| `S13` | Participant | Disability support participant, aged-care resident, wellbeing programme participant, community support recipient |

### Reusable Journey definitions (Value Stream specialisations)
| Ref | Journey pattern | Value stages |
|---|---|---|
| `RJ01` | Access & Participate in Care | Need or intent → find/access → engage → understand/participate → continue |
| `RJ02` | Determine & Direct Clinical Care | Establish concern/context → assess → decide/plan → direct care → evaluate/handover |
| `RJ03` | Deliver & Observe Care | Accept care context → provide care/support → observe → respond/escalate → continue |
| `RJ04` | Produce Diagnostic Evidence | Qualify request → investigate → check validity → report → close loop |
| `RJ05` | Provide a Health Service | Establish service demand → organise access/capacity → provide → assess → adapt |
| `RJ06` | Steward & Assure | Understand obligations → set direction → authorise/commit → evaluate → respond/improve |
| `RJ07` | Produce & Apply Knowledge | Define purpose → establish authority → investigate → verify → disseminate/apply |
| `RJ08` | Operate an Information Service | Establish readiness → provide/observe → manage incident → recover → improve |
| `RJ09` | Administer Governed Change | Establish authority → specify change → assess → apply → verify → revise/revoke |
| `RJ10` | Pursue Participation & Support | Express aspirations/needs → establish choices → arrange support → participate → evaluate/adapt |
| `RJ11` | Commission & Fund Health Services | Identify population/service need → select/fund services → establish arrangements → account for activity/outcomes → review investment |

All **13 original stakeholder perspectives**, including Patient and Participant as distinct perspectives, and all **29 candidate Journeys / 11 reusable Journeys**, remain unchanged as source candidates. J25–J27 contain provisional stakeholder categories; this work does not convert them into canonical Stakeholder elements or infer inheritance. The table below indexes value stages to Business CT2s and potential Enterprise CT2 support, without claiming satisfaction.

### J01 — Maintain Health & Wellbeing
**Stakeholder:** Patient  
**Intended value:** Informed and supported ongoing health; RJ01, RJ07 feedback  
**Negative probe:** Unexpected service advice or stale guidance  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Understand health | `B01.06` | `E07.01`, `E09.04`, `E02.02` |
| 2. identify risk/need | `B01.01` | `E02.02`, `E07.01`, `E09.04` |
| 3. adopt protective action | `B13.02` | `E07.01`, `E09.03`, `E09.04` |
| 4. access support | `B01.02` | `E06.05`, `E02.01`, `E07.02`, `E02.04`, `E05.01` |
| 5. review wellbeing | `B01.06`, `B13.02` | `E07.01`, `E09.04`, `E02.02`, `E09.03` |

### J02 — Seek, Receive & Continue Care
**Stakeholder:** Patient  
**Intended value:** Care accessed and continued with informed participation; RJ01 + contributions from RJ02/3  
**Negative probe:** Care transition loses source or responsibility  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Recognise need | `B01.01` | `E02.02`, `E07.01`, `E09.04` |
| 2. find/access care | `B01.02` | `E06.05`, `E02.01`, `E07.02`, `E02.04`, `E05.01` |
| 3. establish care context | `B02.01` | `E02.01`, `E02.02`, `E07.01`, `E01.02`, `E04.03`, `E07.04` |
| 4. receive assessment/plan | `B02.02`, `B02.06` | `E07.01`, `E02.01`, `E09.02`, `E06.01`, `E07.02`, `E02.04` |
| 5. participate in intervention | `B03.01` | `E07.01`, `E02.01`, `E06.01`, `E08.03` |
| 6. understand outcome | `B05.03` | `E06.01`, `E02.04`, `E08.03`, `E07.01`, `E06.06` |
| 7. continue/transition | `B05.05` | `E06.05`, `E06.02`, `E02.04`, `E08.03`, `E06.04` |

### J03 — Represent & Support a Patient
**Stakeholder:** Representing/supporting stakeholder  
**Intended value:** Patient supported appropriately within representative's authority; shares RJ01, not all Patient stages  
**Negative probe:** Representative authority invalid or expires mid-journey  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Establish relationship/authority | `B01.05` | `E05.02`, `E05.03`, `E07.02`, `E02.01`, `E05.01` |
| 2. understand need/preferences | `B01.04` | `E07.02`, `E07.01`, `E02.01` |
| 3. help access | `B01.02` | `E06.05`, `E02.01`, `E07.02`, `E02.04`, `E05.01` |
| 4. support decisions/communication | `B01.04` | `E07.02`, `E07.01`, `E02.01` |
| 5. coordinate follow-up | `B05.05` | `E06.05`, `E06.02`, `E02.04`, `E08.03`, `E06.04` |

### J04 — Evaluate & Direct Clinical Care
**Stakeholder:** Practitioner  
**Intended value:** Appropriate clinical decisions and continuity; RJ02  
**Negative probe:** Clinical decision based on an unqualified historical version  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Accept concern | `B02.01` | `E02.01`, `E02.02`, `E07.01`, `E01.02`, `E04.03`, `E07.04` |
| 2. obtain context | `B02.01` | `E02.01`, `E02.02`, `E07.01`, `E01.02`, `E04.03`, `E07.04` |
| 3. assess/determine | `B02.02`, `B02.04` | `E07.01`, `E02.01`, `E09.02`, `E04.04`, `E09.04` |
| 4. decide/plan | `B02.06` | `E06.01`, `E07.02`, `E02.04`, `E01.01` |
| 5. direct/provide care | `B03.01` | `E07.01`, `E02.01`, `E06.01`, `E08.03` |
| 6. evaluate | `B03.03` | `E02.04`, `E06.02`, `E07.01`, `E09.04` |
| 7. communicate/transition | `B05.03` | `E06.01`, `E02.04`, `E08.03`, `E07.01`, `E06.06` |

### J05 — Manage Complex / Critical Clinical Care
**Stakeholder:** Specialist practitioner  
**Intended value:** Specialised clinical decision/response; RJ02 + RJ03 with specialty constraints  
**Negative probe:** Critical deterioration during node failure  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Recognise deterioration/complexity | `B03.03` | `E02.04`, `E06.02`, `E07.01`, `E09.04` |
| 2. synthesise evidence | `B02.01` | `E02.01`, `E02.02`, `E07.01`, `E01.02`, `E04.03`, `E07.04` |
| 3. prioritise decision | `B02.05` | `E07.01`, `E06.02`, `E09.04`, `E08.03` |
| 4. coordinate intervention | `B03.04`, `B05.01` | `E06.02`, `E02.03`, `E07.01`, `E08.06`, `E02.04`, `E05.04` |
| 5. monitor response | `B03.03` | `E02.04`, `E06.02`, `E07.01`, `E09.04` |
| 6. revise plan | `B02.06` | `E06.01`, `E07.02`, `E02.04`, `E01.01` |
| 7. transition | `B05.03` | `E06.01`, `E02.04`, `E08.03`, `E07.01`, `E06.06` |

### J06 — Restore Function & Participation
**Stakeholder:** Allied Health  
**Intended value:** Functional and psychosocial improvement; shared RJ02/3 elements  
**Negative probe:** Support outcome is mistaken for a medical treatment order  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Understand functional need | `B06.03` | `E07.01`, `E09.04`, `E06.01` |
| 2. assess capacity/environment | `B02.02` | `E07.01`, `E02.01`, `E09.02` |
| 3. agree goals | `B14.01`, `B06.03` | `E07.01`, `E07.02`, `E02.01`, `E09.04`, `E06.01` |
| 4. provide interventions/support | `B06.03` | `E07.01`, `E09.04`, `E06.01` |
| 5. evaluate progress | `B06.03` | `E07.01`, `E09.04`, `E06.01` |
| 6. enable ongoing participation | `B14.05` | `E07.01`, `E02.02`, `E06.05` |

### J07 — Provide Continuous Nursing & Support
**Stakeholder:** Nurse and Support  
**Intended value:** Safe, continuous direct care; RJ03  
**Negative probe:** Handover accepted while outstanding obligation is lost  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Accept responsibility | `B05.03` | `E06.01`, `E02.04`, `E08.03`, `E07.01`, `E06.06` |
| 2. establish care needs | `B03.02` | `E06.02`, `E07.01`, `E02.01`, `E07.04` |
| 3. organise care | `B03.02` | `E06.02`, `E07.01`, `E02.01`, `E07.04` |
| 4. deliver/assist | `B03.02` | `E06.02`, `E07.01`, `E02.01`, `E07.04` |
| 5. monitor and escalate | `B03.03` | `E02.04`, `E06.02`, `E07.01`, `E09.04` |
| 6. hand over | `B05.03` | `E06.01`, `E02.04`, `E08.03`, `E07.01`, `E06.06` |

### J08 — Investigate & Report Clinical Evidence
**Stakeholder:** Diagnostics People  
**Intended value:** Reliable, timely diagnostic evidence; RJ04  
**Negative probe:** Critical finding received but not communicated and committed  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Receive/qualify request | `B04.01` | `E06.01`, `E03.02`, `E02.02`, `E04.04` |
| 2. prepare subject/materials | `B04.02` | `E03.01`, `E04.04`, `E01.03`, `E06.01`, `E03.03` |
| 3. acquire evidence | `B04.02` | `E03.01`, `E04.04`, `E01.03`, `E06.01`, `E03.03` |
| 4. analyse/verify | `B04.03`, `B04.04` | `E03.01`, `E02.04`, `E07.01`, `E08.03`, `E03.04`, `E04.03` |
| 5. release findings | `B04.03` | `E03.01`, `E02.04`, `E07.01`, `E08.03`, `E03.04`, `E04.03` |
| 6. escalate critical results | `B04.03` | `E03.01`, `E02.04`, `E07.01`, `E08.03`, `E03.04`, `E04.03` |
| 7. reconcile completion | `B04.04` | `E04.04`, `E08.03`, `E01.03`, `E03.05` |

### J09 — Deliver Reliable Healthcare Services
**Stakeholder:** Healthcare Operations  
**Intended value:** Required service available and delivered; RJ05  
**Negative probe:** Resource reported available though required dependency is down  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Understand demand | `B07.03` | `E06.05`, `E09.03`, `E08.01`, `E07.01` |
| 2. determine service capacity | `B07.03` | `E06.05`, `E09.03`, `E08.01`, `E07.01` |
| 3. deploy workforce/resources | `B08.01`, `B09.02` | `E06.05`, `E07.01`, `E09.03`, `E08.01`, `E02.02` |
| 4. coordinate delivery | `B07.04` | `E06.02`, `E08.01`, `E02.04`, `E07.01`, `E06.03`, `E06.04` |
| 5. manage exceptions | `B07.04` | `E06.02`, `E08.01`, `E02.04`, `E07.01`, `E06.03`, `E06.04` |
| 6. review results | `B07.06` | `E09.03`, `E09.02`, `E08.04`, `E09.01` |
| 7. adapt | `B07.01` | `E09.03`, `E07.01`, `E04.01` |

### J10 — Maintain Healthcare Readiness
**Stakeholder:** Healthcare Operations  
**Intended value:** Clinically and operationally ready service; RJ05/RJ06  
**Negative probe:** Declared readiness lacks recovered-state evidence  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Establish required capability | `B12.05` | `E08.01`, `E08.06`, `E06.05`, `E09.03` |
| 2. evaluate readiness | `B09.06` | `E08.01`, `E08.05`, `E08.06`, `E09.03` |
| 3. identify shortfalls | `B09.06` | `E08.01`, `E08.05`, `E08.06`, `E09.03` |
| 4. mobilise resources | `B12.05` | `E08.01`, `E08.06`, `E06.05`, `E09.03` |
| 5. test readiness | `B12.05` | `E08.01`, `E08.06`, `E06.05`, `E09.03` |
| 6. sustain/restore | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |

### J11 — Arrange & Administer Healthcare Access
**Stakeholder:** Healthcare Administrators  
**Intended value:** Access coordinated and administration accurate; RJ01/RJ05  
**Negative probe:** Appointment notification precedes actual booking acceptance  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Receive request | `B07.05` | `E06.05`, `E07.02`, `E02.01`, `E08.03`, `E05.01`, `E07.04` |
| 2. establish person/service context | `B07.05` | `E06.05`, `E07.02`, `E02.01`, `E08.03`, `E05.01`, `E07.04` |
| 3. validate authority/eligibility | `B01.03`, `B01.05` | `E05.02`, `E02.01`, `E09.03`, `E05.03`, `E07.02`, `E05.01` |
| 4. book/route | `B07.03` | `E06.05`, `E09.03`, `E08.01`, `E07.01` |
| 5. communicate arrangements | `B07.05` | `E06.05`, `E07.02`, `E02.01`, `E08.03`, `E05.01`, `E07.04` |
| 6. record attendance/outcome | `B07.05` | `E06.05`, `E07.02`, `E02.01`, `E08.03`, `E05.01`, `E07.04` |
| 7. complete administration | `B07.05` | `E06.05`, `E07.02`, `E02.01`, `E08.03`, `E05.01`, `E07.04` |

### J12 — Administer Healthcare Records & Obligations
**Stakeholder:** Healthcare Administrators  
**Intended value:** Reliable business stewardship of records and entitlements; RJ05/RJ06  
**Negative probe:** Disclosure is authorised at request but not at retrieval  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Receive information/obligation | `B10.01` | `E01.01`, `E01.02`, `E02.01`, `E07.03`, `E01.05` |
| 2. verify scope/authority | `B10.03` | `E05.02`, `E05.03`, `E02.01`, `E02.04`, `E01.05`, `E03.03` |
| 3. register/update | `B10.01`, `B10.07` | `E01.01`, `E01.02`, `E02.01`, `E07.03`, `E01.05`, `E01.03` |
| 4. make appropriate access available | `B10.03` | `E05.02`, `E05.03`, `E02.01`, `E02.04`, `E01.05`, `E03.03` |
| 5. resolve discrepancy | `B10.07` | `E01.02`, `E01.03`, `E02.01`, `E04.04`, `E04.03` |
| 6. retain/disclose/close | `B10.06` | `E01.04`, `E05.03`, `E08.03`, `E09.03` |

### J13 — Direct & Govern the Enterprise
**Stakeholder:** Executive  
**Intended value:** Enterprise achieves sustainable outcomes; RJ06  
**Negative probe:** Executive summary cannot be traced to authoritative evidence  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Understand position | `B12.03` | `E09.03`, `E08.04`, `E07.01`, `E05.01` |
| 2. determine strategic outcomes | `B12.01` | `E09.03`, `E09.04`, `E08.04` |
| 3. decide priorities | `B12.01` | `E09.03`, `E09.04`, `E08.04` |
| 4. allocate authority/resources | `B12.02` | `E09.03`, `E02.01`, `E08.04` |
| 5. monitor service and risk | `B12.03` | `E09.03`, `E08.04`, `E07.01`, `E05.01` |
| 6. intervene | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 7. evaluate direction | `B12.03` | `E09.03`, `E08.04`, `E07.01`, `E05.01` |

### J14 — Regulate & Oversee Healthcare
**Stakeholder:** Regulatory / Government  
**Intended value:** Accountable, safe and equitable health system; RJ06  
**Negative probe:** Audit export dereferences evidence the requester cannot access  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Identify public interest/obligation | `B11.05` | `E08.04`, `E08.03`, `E09.03`, `E01.03` |
| 2. establish policy/standard | `B11.05` | `E08.04`, `E08.03`, `E09.03`, `E01.03` |
| 3. obtain evidence | `B11.05` | `E08.04`, `E08.03`, `E09.03`, `E01.03` |
| 4. assess conformance and outcomes | `B11.03` | `E08.04`, `E08.03`, `E04.05`, `E09.03` |
| 5. intervene/authorise | `B11.05` | `E08.04`, `E08.03`, `E09.03`, `E01.03` |
| 6. learn/revise | `B11.04` | `E09.03`, `E09.04`, `E08.04` |

### J15 — Protect & Improve Population Health
**Stakeholder:** Regulatory / Government  
**Intended value:** Population health protected and improved; RJ06/RJ07  
**Negative probe:** Time-limited surveillance input omitted without qualification  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Observe population | `B13.01` | `E09.02`, `E09.03`, `E03.01`, `E02.02` |
| 2. identify threat/inequity | `B13.01` | `E09.02`, `E09.03`, `E03.01`, `E02.02` |
| 3. determine response | `B13.03` | `E06.02`, `E03.01`, `E09.03`, `E07.01` |
| 4. coordinate intervention | `B13.03` | `E06.02`, `E03.01`, `E09.03`, `E07.01` |
| 5. monitor impact | `B13.03` | `E06.02`, `E03.01`, `E09.03`, `E07.01` |
| 6. adjust public action | `B13.03` | `E06.02`, `E03.01`, `E09.03`, `E07.01` |

### J16 — Produce Trustworthy Health Research
**Stakeholder:** Researchers  
**Intended value:** Valid, ethically produced knowledge; RJ07  
**Negative probe:** Research purpose changes after data access granted  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Define question | `B13.05` | `E09.05`, `E09.02`, `E01.03`, `E08.03`, `E01.05`, `E04.03` |
| 2. obtain ethics/authority | `B13.04` | `E05.02`, `E05.03`, `E08.03`, `E09.05` |
| 3. design method | `B13.05` | `E09.05`, `E09.02`, `E01.03`, `E08.03`, `E01.05`, `E04.03` |
| 4. obtain appropriate data/evidence | `B13.05`, `B10.03` | `E09.05`, `E09.02`, `E01.03`, `E08.03`, `E01.05`, `E04.03` |
| 5. analyse/verify | `B13.05` | `E09.05`, `E09.02`, `E01.03`, `E08.03`, `E01.05`, `E04.03` |
| 6. report/share | `B13.05` | `E09.05`, `E09.02`, `E01.03`, `E08.03`, `E01.05`, `E04.03` |
| 7. translate into practice | `B13.06` | `E04.05`, `E07.01`, `E09.04` |

### J17 — Translate Evidence into Practice
**Stakeholder:** Researchers + practitioners  
**Intended value:** Knowledge changes practice appropriately; RJ07 + RJ02  
**Negative probe:** Updated guidance published without model-version governance  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Appraise findings | `B13.06` | `E04.05`, `E07.01`, `E09.04` |
| 2. assess applicability | `B13.06` | `E04.05`, `E07.01`, `E09.04` |
| 3. agree change | `B08.04` | `E07.01`, `E04.01`, `E09.04`, `E09.03` |
| 4. update practice guidance | `B10.05` | `E04.01`, `E04.02`, `E04.05`, `E07.01`, `E04.03` |
| 5. apply | `B08.04` | `E07.01`, `E04.01`, `E09.04`, `E09.03` |
| 6. measure effect | `B11.04` | `E09.03`, `E09.04`, `E08.04` |
| 7. refine | `B13.06` | `E04.05`, `E07.01`, `E09.04` |

### J18 — Operate & Restore Information Services
**Stakeholder:** Systems Operations  
**Intended value:** Reliable, recoverable Information Systems services; RJ08  
**Negative probe:** Processing succeeds but commit outcome is uncertain during failure  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Establish service readiness | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 2. operate | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 3. monitor | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 4. investigate degradation | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 5. recover/restore | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 6. verify integrity/availability | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 7. learn/improve | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |

### J19 — Sustain Information Services
**Stakeholder:** Systems Operations  
**Intended value:** Sustainable information-service delivery; RJ08  
**Negative probe:** Planned restart loses accepted work  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Understand demand/health | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 2. plan capacity and continuity | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 3. maintain service | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 4. exercise recovery | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |
| 5. verify service level | `B12.03` | `E09.03`, `E08.04`, `E07.01`, `E05.01` |
| 6. improve | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |

### J20 — Govern Systems Access & Configuration
**Stakeholder:** Systems Administrators  
**Intended value:** Controlled and accountable access/configuration; RJ09  
**Negative probe:** Revoked Passport still used to authorise a change  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Establish authorised request | `B12.03`, `B10.03` | `E09.03`, `E08.04`, `E07.01`, `E05.01`, `E05.02`, `E05.03` |
| 2. determine scope | `B10.03` | `E05.02`, `E05.03`, `E02.01`, `E02.04`, `E01.05`, `E03.03` |
| 3. provision/configure | `B10.03` | `E05.02`, `E05.03`, `E02.01`, `E02.04`, `E01.05`, `E03.03` |
| 4. validate | `B10.03` | `E05.02`, `E05.03`, `E02.01`, `E02.04`, `E01.05`, `E03.03` |
| 5. monitor/review | `B10.03` | `E05.02`, `E05.03`, `E02.01`, `E02.04`, `E01.05`, `E03.03` |
| 6. amend/revoke | `B10.03` | `E05.02`, `E05.03`, `E02.01`, `E02.04`, `E01.05`, `E03.03` |
| 7. preserve evidence | `B10.06` | `E01.04`, `E05.03`, `E08.03`, `E09.03` |

### J21 — Govern Models, Releases & Information Changes
**Stakeholder:** Systems Administrators  
**Intended value:** Governed information-system evolution; RJ09  
**Negative probe:** Model release breaks a dependent consumer without recorded qualification  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Identify controlled change | `B10.05` | `E04.01`, `E04.02`, `E04.05`, `E07.01`, `E04.03` |
| 2. assess compatibility/impact | `B10.05` | `E04.01`, `E04.02`, `E04.05`, `E07.01`, `E04.03` |
| 3. authorise release | `B12.03` | `E09.03`, `E08.04`, `E07.01`, `E05.01` |
| 4. publish/deploy | `B10.05` | `E04.01`, `E04.02`, `E04.05`, `E07.01`, `E04.03` |
| 5. verify consumers | `B10.05` | `E04.01`, `E04.02`, `E04.05`, `E07.01`, `E04.03` |
| 6. monitor/recover | `B12.04` | `E08.06`, `E08.01`, `E08.05`, `E09.03`, `E06.04`, `E06.06` |

### J22 — Pursue Wellbeing & Chosen Life Goals
**Stakeholder:** S13 Participant  
**Intended value:** Self-directed wellbeing and participation; RJ10  
**Negative probe:** Participant goals inferred as clinical problems  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Express aspirations | `B14.01` | `E07.01`, `E07.02`, `E02.01` |
| 2. explore choices | `B14.03` | `E07.02`, `E06.05`, `E02.02`, `E05.02` |
| 3. agree desired support | `B14.03` | `E07.02`, `E06.05`, `E02.02`, `E05.02` |
| 4. participate in activities | `B14.05` | `E07.01`, `E02.02`, `E06.05` |
| 5. review outcomes | `B14.01` | `E07.01`, `E07.02`, `E02.01` |
| 6. adapt preferences | `B14.01` | `E07.01`, `E07.02`, `E02.01` |

### J23 — Obtain & Sustain Everyday Supports
**Stakeholder:** S13 Participant + Healthcare Administrators/Operations where appropriate  
**Intended value:** Support is available and appropriate without assuming treatment; RJ10/RJ05  
**Negative probe:** Support provider handover leaves unmet obligation  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Understand support need | `B14.02` | `E07.01`, `E02.01`, `E09.02` |
| 2. choose/arrange service | `B14.03` | `E07.02`, `E06.05`, `E02.02`, `E05.02` |
| 3. obtain support | `B14.04` | `E06.02`, `E02.04`, `E07.01`, `E08.03`, `E07.04` |
| 4. assess fit/continuity | `B14.04` | `E06.02`, `E02.04`, `E07.01`, `E08.03`, `E07.04` |
| 5. adjust/renew/finish | `B14.03` | `E07.02`, `E06.05`, `E02.02`, `E05.02` |

### J24 — Maintain Independence & Community Participation
**Stakeholder:** S13 Participant, Allied Health where appropriate  
**Intended value:** Agency, access, independence and inclusion; RJ10  
**Negative probe:** Assistive support unavailable or inconsistent with chosen participation  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Establish desired participation | `B14.05` | `E07.01`, `E02.02`, `E06.05` |
| 2. understand barriers | `B14.02` | `E07.01`, `E02.01`, `E09.02` |
| 3. arrange aids/assistance | `B14.03`, `B06.04` | `E07.02`, `E06.05`, `E02.02`, `E05.02`, `E07.01`, `E06.01` |
| 4. take part | `B14.05` | `E07.01`, `E02.02`, `E06.05` |
| 5. evaluate independence/outcomes | `B14.01` | `E07.01`, `E07.02`, `E02.01` |
| 6. adjust | `B14.03` | `E07.02`, `E06.05`, `E02.02`, `E05.02` |

### J25 — Act for Another Within a Recognised Responsibility
**Stakeholder:** Provisional ResponsiblePerson cross-context perspective  
**Intended value:** Supported person or enterprise is properly represented/cared for; shared RJ01/RJ06 parts; no parent-role ontology  
**Negative probe:** ResponsiblePerson lacks authority for requested scope  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Establish scope of responsibility | `B14.06`, `B01.05` | `E05.02`, `E05.03`, `E07.02`, `E08.03`, `E05.01`, `E02.01` |
| 2. understand wishes/obligations | `B14.01` | `E07.01`, `E07.02`, `E02.01` |
| 3. arrange or contribute | `B14.03` | `E07.02`, `E06.05`, `E02.02`, `E05.02` |
| 4. communicate decisions/actions | `B14.06` | `E05.02`, `E05.03`, `E07.02`, `E08.03`, `E05.01` |
| 5. review/hand over | `B14.04` | `E06.02`, `E02.04`, `E07.01`, `E08.03`, `E07.04` |

### J26 — Fund & Arrange Eligible Care or Support
**Stakeholder:** Payer/insurer/funder — specialisation/participating perspective TBD  
**Intended value:** Funding supports eligible access; RJ11  
**Negative probe:** Coverage decision based on obsolete entitlement  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Identify entitlement or service need | `B15.02` | `E05.02`, `E02.01`, `E09.03`, `E07.02` |
| 2. assess coverage/conditions | `B15.02` | `E05.02`, `E02.01`, `E09.03`, `E07.02` |
| 3. authorise or arrange payment | `B15.04` | `E02.01`, `E09.03`, `E08.03`, `E01.01` |
| 4. reconcile provision/claims | `B15.04` | `E02.01`, `E09.03`, `E08.03`, `E01.01` |
| 5. review fairness/outcome | `B15.05` | `E09.02`, `E09.04`, `E08.04` |

### J27 — Commission & Review a Provider Network
**Stakeholder:** Commissioner/purchaser — specialisation/participating perspective TBD  
**Intended value:** Accessible, accountable service supply; RJ11/RJ05  
**Negative probe:** Provider contract outcome cannot be traced to evidence  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Assess population need | `B15.01` | `E09.03`, `E09.02`, `E02.02` |
| 2. specify intended outcomes | `B15.01` | `E09.03`, `E09.02`, `E02.02` |
| 3. select/contract providers | `B15.03` | `E09.03`, `E08.04`, `E02.02` |
| 4. monitor service performance | `B15.03` | `E09.03`, `E08.04`, `E02.02` |
| 5. renew/change arrangements | `B15.03` | `E09.03`, `E08.04`, `E02.02` |

### J28 — Educate & Maintain Professional Practice
**Stakeholder:** Educator/trainer and Practitioner/Allied/Nursing perspectives  
**Intended value:** Safe and competent practice; RJ07/RJ06  
**Negative probe:** Competence assessment is mistaken for clinical privilege approval  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Identify learning need | `B08.04` | `E07.01`, `E04.01`, `E09.04`, `E09.03` |
| 2. design teaching/supervision | `B08.04` | `E07.01`, `E04.01`, `E09.04`, `E09.03` |
| 3. provide learning | `B08.04` | `E07.01`, `E04.01`, `E09.04`, `E09.03` |
| 4. assess competence | `B08.04` | `E07.01`, `E04.01`, `E09.04`, `E09.03` |
| 5. maintain credentials | `B08.02` | `E02.02`, `E04.04`, `E05.02`, `E08.03` |
| 6. renew | `B08.04` | `E07.01`, `E04.01`, `E09.04`, `E09.03` |

### J29 — Oversee Therapeutic Goods and Public Protection
**Stakeholder:** S09 Regulatory/Government, with provider/diagnostics contribution  
**Intended value:** Regulated health products and associated activity are appropriately controlled; RJ06  
**Negative probe:** Product safety finding not connected to a corrective action  
| Value stage | Business CT2 candidate | Enterprise CT2 context (not validated) |
|---|---|---|
| 1. Set standards | `B11.05` | `E08.04`, `E08.03`, `E09.03`, `E01.03` |
| 2. assess product/service evidence | `B11.05` | `E08.04`, `E08.03`, `E09.03`, `E01.03` |
| 3. authorise/monitor | `B11.05` | `E08.04`, `E08.03`, `E09.03`, `E01.03` |
| 4. identify risk | `B13.03` | `E06.02`, `E03.01`, `E09.03`, `E07.01` |
| 5. issue intervention | `B13.03` | `E06.02`, `E03.01`, `E09.03`, `E07.01` |
| 6. review protection | `B11.05` | `E08.04`, `E08.03`, `E09.03`, `E01.03` |

## 8. End-to-end integrity test proposals

The **Retrieve → Check → Update → ProcessSomething → Commit** model is a reference for separating consumer intentions, **not** a mandatory sequence. `ProcessSomething` is not ArchiMate::Process. Successful execution does not imply authoritative commitment.
### IT01 — Clinical handover with outstanding obligations
**Journey(s):** `J02`, `J04`, `J07` · **Business context:** `B05.03`, `B10.02` · **Enterprise context:** `E06.01`, `E02.04`, `E08.03`, `E01.01`  
**Intended scenario:** Retrieve the current attributed patient context; check responsible party and outstanding task list; update handover intentions; perform clinical handover; commit accepted responsibility and evidence.  
**Adverse/integrity assertion:** Delivering a handover message is not receiving clinician acceptance. After ProcessSomething success but Commit uncertainty, preserve indeterminate status, allow authoritative outcome query, and never assert handover complete.  
**Required evidence:** Handover accepted/rejected identity, open obligations, authoritative acknowledgement, retry/correlation evidence.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT02 — Diagnostic critical result release and escalation
**Journey(s):** `J08` · **Business context:** `B04.03`, `B04.04` · **Enterprise context:** `E04.04`, `E02.04`, `E03.04`, `E06.04`, `E08.03`  
**Intended scenario:** Retrieve request/specimen identity; check analytical/semantic validity; update finding; ProcessSomething release/escalation; commit verified result and accountability.  
**Adverse/integrity assertion:** Transport receipt, result release, clinician awareness and authoritative persistence are distinct. On missing acknowledgement, notify/escalate without silently claiming delivery or treatment.  
**Required evidence:** Specimen chain, result version, validation disposition, critical dispatch/reception evidence.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT03 — Representative authority expiry during a disclosure
**Journey(s):** `J03`, `J25` · **Business context:** `B01.05`, `B10.03`, `B14.06` · **Enterprise context:** `E05.02`, `E05.03`, `E05.04`, `E07.02`  
**Intended scenario:** Check legitimate representation scope and current Passport at the point of disclosure; re-evaluate if circumstances change.  
**Adverse/integrity assertion:** A revoked Passport cannot authorise release. Missing/unavailable authority fails closed; no partial disclosure of restricted information.  
**Required evidence:** Policy/Passport version, RequiredCriteria, PEP decision, denied-access audit correlation.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT04 — Participant support change with supported choice
**Journey(s):** `J22`, `J23`, `J24` · **Business context:** `B14.01`, `B14.03`, `B14.06` · **Enterprise context:** `E07.01`, `E07.02`, `E05.02`, `E06.01`  
**Intended scenario:** Present the participant perspective and chosen support; capture changed preferences and supported decision evidence.  
**Adverse/integrity assertion:** Do not treat the participant as a derived Patient or assume clinician consent describes all legitimate support decisions. Update of preference is distinct from provider delivery and funding commit.  
**Required evidence:** Preference version, decision authority, review/confirmation, change notification.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT05 — Clinical information update with failed authoritative commit
**Journey(s):** `J04`, `J05` · **Business context:** `B02.06`, `B10.01` · **Enterprise context:** `E02.01`, `E01.01`, `E01.02`, `E08.03`  
**Intended scenario:** Retrieve immutable version; Check governing conditions; Update working state; ProcessSomething; attempt Commit with supplied version precondition.  
**Adverse/integrity assertion:** After a successful operation but interrupted persistence acknowledgement, do not report committed state; distinguish conflict, failure and indeterminate outcome.  
**Required evidence:** Input version, ProcessSomething outcome, commit correlation, authoritative version readback, immutable audit link.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT06 — Governed model and value-set release
**Journey(s):** `J21` · **Business context:** `B10.05`, `B12.03` · **Enterprise context:** `E04.01`, `E04.02`, `E04.05`, `E03.05`  
**Intended scenario:** Check semantic diff and dependent consumers; seek release authority; publish controlled model/version and conformance evidence.  
**Adverse/integrity assertion:** An accepted model change must not silently mutate active value sets or bypass scheduled release controls; dependent compatibility remains separately assessed.  
**Required evidence:** Model/version refs, review outcome, release provenance, regression evidence, consumer compatibility.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT07 — Privileged audit/provenance evidence export
**Journey(s):** `J12`, `J14` · **Business context:** `B10.06`, `B11.05` · **Enterprise context:** `E08.03`, `E05.02`, `E05.03`, `E09.03`  
**Intended scenario:** Select immutable AuditEvents/Provenance, Check request authority and all referenced information, assemble fully, then release once complete.  
**Adverse/integrity assertion:** If any required reference fails authorisation, no partial dereferenced export is released. Raw record export remains a separate service promise.  
**Required evidence:** Request scope, reference list, authorisation outcomes, atomic release disposition, original record versions.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT08 — Single-node loss during accepted work
**Journey(s):** `J18`, `J19` · **Business context:** `B12.04`, `B12.05` · **Enterprise context:** `E06.04`, `E06.06`, `E08.06`, `E01.04`  
**Intended scenario:** Accept and durably record work, checkpoint transition, fail a worker, reconstruct eligible work and resume from a governed boundary.  
**Adverse/integrity assertion:** Do not resume within a failed Ergon, lose accepted work, repeat a committed side effect without controlled disposition or rewrite authoritative IU version.  
**Required evidence:** Accepted work ledger, processing transition, checkpoint reference, recovery verdict, authoritative information identity.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT09 — Clinical privilege withdrawn during an active session
**Journey(s):** `J20`, `J28` · **Business context:** `B08.02`, `B08.03` · **Enterprise context:** `E05.02`, `E05.03`, `E05.04`, `E08.03`  
**Intended scenario:** Update source professional privilege authority; propagate invalidation/revalidation; request a protected action.  
**Adverse/integrity assertion:** A previously valid login is insufficient to authorise a now-disallowed clinical operation; action must fail closed even where Passport distribution is delayed.  
**Required evidence:** Effective policy and privilege revisions, Passport currency, PEP decision, event timing.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT10 — Purpose-bounded research extraction
**Journey(s):** `J16` · **Business context:** `B13.04`, `B13.05` · **Enterprise context:** `E09.05`, `E05.02`, `E01.03`, `E08.03`  
**Intended scenario:** Check research authority/purpose and versioned data source; select permitted records; retain provenance on analysis or export.  
**Adverse/integrity assertion:** Analytical capability is not unrestricted access or approval of the research programme. Withhold unauthorised data and avoid source mutation.  
**Required evidence:** Authorisation grant, requested fields, consent/ethics scope where applicable, source version and lineage.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT11 — AI assurance or insight recommendation
**Journey(s):** `J13`, `J17` · **Business context:** `B11.03`, `B12.03`, `B13.06` · **Enterprise context:** `E08.04`, `E09.04`, `E09.05`  
**Intended scenario:** Generate evidence-linked observation/recommendation and present provenance and qualification.  
**Adverse/integrity assertion:** R1/R2 AI advice has no authority to release protected information, direct clinical care or Commit changes; human or governed authority remains separate.  
**Required evidence:** Recommendation status, input evidence references, restriction markers and distinct subsequent authorised action.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT12 — External exchange acknowledgement versus durable information acceptance
**Journey(s):** `J02`, `J08` · **Business context:** `B04.03`, `B05.06`, `B10.01` · **Enterprise context:** `E03.01`, `E03.04`, `E01.01`, `E02.03`  
**Intended scenario:** Validate external contract, receive exchange, apply governance and persist accepted InformationUnit before the applicable authoritative acceptance outcome.  
**Adverse/integrity assertion:** HL7 v2/MLLP ACK semantics follow the external contract; a transport receipt may not be reported as an authoritative committed information version without durable acceptance.  
**Required evidence:** External message ID, exchange ACK semantics, IU version identity, durable disposition, retry disposition.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT13 — Admission control during throughput pressure
**Journey(s):** `J09`, `J18` · **Business context:** `B07.03`, `B12.04` · **Enterprise context:** `E06.04`, `E08.01`, `E08.06`  
**Intended scenario:** Increase incoming activity to capacity threshold; distinguish accepted from rejected work; monitor capacity and accepted-work durability.  
**Adverse/integrity assertion:** Explicitly reject or defer beyond capacity rather than acknowledging unretained work; accepted work is not silently dropped after failure.  
**Required evidence:** Admission decision, buffer/capacity counters, durable acceptance evidence, after-failure work reconciliation.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT14 — Funding/commissioning digital-scope honesty
**Journey(s):** `J26`, `J27` · **Business context:** `B15.01`, `B15.04`, `B15.05` · **Enterprise context:** `E09.03`, `E05.02`, `E02.01`  
**Intended scenario:** Test business need for entitlement, claims review, provider oversight and outcome evidence against the actual Harmonia solution boundary.  
**Adverse/integrity assertion:** A conceptual Business-to-Enterprise map does not authorise a payer engine, procurement system or contract management implementation.  
**Required evidence:** In-scope/out-of-scope service commitment and correspondence to business outcomes; gap qualification.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT15 — Clinical emergency cross-facility availability
**Journey(s):** `J05`, `J07` · **Business context:** `B03.04`, `B05.06` · **Enterprise context:** `E02.03`, `E02.05`, `E03.01`, `E07.03`  
**Intended scenario:** Retrieve a previously accepted cross-facility clinical record during loss of a node or source endpoint.  
**Adverse/integrity assertion:** Received information must remain available according to its governed acceptance contract; no substitution of an unrelated patient or unverified cached version.  
**Required evidence:** Patient/record correlation, authoritative version ref, staleness qualification, failure and retrieval timeline.  
**Status:** Proposed test; no solution result or acceptance claimed.

### IT16 — InformationUnit version, content and media-reference integrity
**Journey(s):** `J12` · **Business context:** `B10.01`, `B10.07` · **Enterprise context:** `E01.02`, `E01.05`, `E02.01`, `E04.03`  
**Intended scenario:** Retrieve a historical IU with one governed representation and externally stored large-content reference; check origin and mapping lineage.  
**Adverse/integrity assertion:** Do not replace governed identity with provider ID, silently rewrite an immutable version, or treat large binary content as a PostgreSQL general payload.  
**Required evidence:** IU identity/version, reference validation, metadata separation, source representation, historical dereference evidence.  
**Status:** Proposed test; no solution result or acceptance claimed.

## 9. Candidate 006 depth refinements

### Reviewable proposals — do not incorporate automatically
| Current home to examine | Potential additional CT3/Feature meaning | Architectural reason | Standing |
|---|---|---|---|
| `E05.02` | Authoritative policy definition / policy change decision | Themis PR owns policy definitions, not merely a single request decision. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E05.02` | Authoritative Passport construction and revalidation | Preconstructed Passport authority distinct from operational distribution. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E05.04` | Operational Passport currency, invalidation and withdrawal | A propagated snapshot must not outlive governing authorisation changes. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E05.03` | Scoped local PEP enforcement and approved forwarding/inheritance | Enforcement ownership and local scope are independently assessable. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E08.03` | Near-source immutable AuditEvidence contribution / capture | Reporting an AuditEvent is not the same as creating the mandatory evidence. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E08.03` | Immutable Provenance record custody and chronological Evidence order | Proof of derivation and record immutability extend beyond display. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E08.03` | Raw non-dereferenced Audit/Provenance record export | An authorised record export may preserve references without following them. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E08.03` | Complete dereferenced AuditEvent export with all-or-nothing release | Kleio export cannot leak a partially authorised evidence assembly. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E08.03` | Complete dereferenced Provenance export with all-or-nothing release | Dokimasia dereferences the selected Provenance evidence in its own time. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E01.01 / E01.02` | Distinct InformationUnit authoritative identity, representation and generation semantics | IU cardinality and provider-independent identity are stable information semantics. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E06.01 / E06.06` | Separate work acceptance, execution outcome, checkpoint and authoritative IU commit | Task and work progression must not masquerade as a persisted InformationUnit. | **PROPOSED — CT3 vs Feature and naming unresolved** |
| `E07.01 / E07.02` | Participant-specific supported decisions and representative authority presentation | Participant is not a Patient specialisation and supported participation is not merely clinical consent. | **PROPOSED — CT3 vs Feature and naming unresolved** |

**Depth-first preference:** preserve E01–E09 and the 47 Enterprise CT2s unless review demonstrates a distinct responsibility that cannot coherently reside there. The above are proposals to test that premise, not predetermined additions.

## 10. Decision register and next design boundary

| Priority | Architectural decision to discuss | Recommendation from this iteration |
|---|---|---|
| 1 | E05 policy authority and Passport lifecycle | Approve the missing *meaning*; select CT3/Feature depth or a dedicated CT2 only if necessary. |
| 2 | E08 evidence lifecycle and two dereferenced exports | Approve distinctions between capture, immutable management, non-dereferenced and dereferenced release; identify the correct E08 homes. |
| 3 | Business ↔ Enterprise semantic link treatment | Confirm contribution/support as the link intent; do not conflate with implementation, realisation or architectural Home. |
| 4 | B14 participant scope and UI obligations | Keep Participant and Patient independent; accept only supported-decision and support-service requirements within solution scope. |
| 5 | B15 funding/commissioning digital boundary | Preserve Business Capabilities but avoid presuming Harmonia owns claims, purchasing or payment engines. |
| 6 | Journey stage coverage and independent integrity evidence | Use 179 stage hypotheses and 16 scenario probes to challenge gaps; do not auto-accept staged traces. |
| 7 | Remaining CT3 and Feature-level reconciliation | Use each of 1,999 Features as an independent demand: 20 manually reasoned spot checks are illustrative, not full satisfaction. |
| 8 | Service Architecture rebuild after capability reconciliation | Use granular consumer intentions and existing Service identities; avoid facet topology as a prerequisite. |

**Next design boundary:** after the Solution Architect reviews the major findings, refine the Enterprise CT3/Features and progressively validate Business CT3 and Feature-level demand. Only then update the granular Service target and later reconcile Information Systems organisation. Neither this document nor the structured package is a Codex task.

## 11. Sources, validation and explicit limitations

### Branch and accepted predecessor
- [Current Continuum](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/continuum/) at pinned commit `fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374`.
- [HCRL 1.1.20/001](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/review/1.1.20/hcrl-1.1.20-001.pending.md) preserves 1.1.19 immutable publication and 1.1.20 carry-forward qualification.
- Candidate 006 Business / Enterprise hierarchy supplied in this conversation. Candidate 003 supplied Stakeholders and Journeys. Candidate 009 Scope accepted. Candidate 010 supplied existing Service identities, DRP register and initial obligation inventory.

### DRP source register
- [DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Interaction-Petasos-to-Mneme.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Mneme-Audit.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Audit.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Mneme-Pragma-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragma-Model.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Mneme-Pragmaton-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Model.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Mneme-Security.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Security.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Themis-Policy-Passport-Enforcement.draft.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Themis-Policy-Passport-Enforcement.draft.001.md) — design-intent evidence; unresolved points not silently accepted.
- [DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md) — design-intent evidence; unresolved points not silently accepted.

### Validation counts
| Item | Count / status |
|---|---:|
| BusinessCT1 | 15 |
| BusinessCT2 | 86 |
| BusinessCT3 | 258 |
| BusinessFeatures | 1,299 |
| EnterpriseCT1 | 9 |
| EnterpriseCT2 | 47 |
| EnterpriseCT3 | 139 |
| EnterpriseFeatures | 700 |
| BusinessCT2ToEnterpriseCT2CandidateLinks | 353 |
| StakeholderPerspectives | 13 |
| ReusableJourneyDefinitions | 11 |
| JourneyCount | 29 |
| JourneyStageCount | 179 |
| DRPSourceCount | 19 |
| existingServices | 147 |
| ReconciliationFindings | 24 |
| CT3 individually analysed spot checks | 20 |
| End-to-end integrity tests | 16 |
| Feature implementation/contract satisfaction | **0 asserted; not tested** |

### Explicit qualifications
1. No Change to Candidate 006 hierarchy or Candidate 010 Service identities has been authorised.
2. All Business-to-Enterprise links are candidate support/contribution relationships, not ArchiMate relationships and not implementation coverage.
3. The 258 Business CT3 demands are context-indexed under their B CT2, not directly validated against all 139 Enterprise CT3s.
4. Every one of 1,999 Features remains unverified by specific test evidence; they are preserved without fabricated satisfaction.
5. Journey stage anchors are informed hypotheses; none are accepted end-to-end integrity tests yet.
6. 19 DRP inputs are known to Candidate 010; DRPs are canonical design intent for this exercise, subject to explicit unresolved matters.
7. Existing Continuum elements and DRPs have equal architectural consideration; neither can be silently rewritten.
8. Services and architecture homes are not allocated by this pass; no Solution Facet component topology is prescribed.
9. Candidate 010 existing Service inventory covers 142 Business and 5 Application Services, not all older Functions/Processes/relationships.
10. The model is a coverage/reconciliation baseline and a list of design questions, not yet a Codex-ready migration authority.

### Read the files together
This document is the architecture judgement narrative. The JSON contains full candidate links, all Feature demand records, every existing Service identity, all Journey-stage contexts and the findings. The accompanying workbook is the sortable review interface, **not** governed ArchiMate and **not** a substitute for independent meaning review.

