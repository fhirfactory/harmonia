<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# MVP Business Capability Decomposition — Architectural Review

**Date:** 2026-10-13

**Repository / branch:** `fhirfactory/harmonia` / `Harmonia-GovernedAccess`

**Reviewed baseline:** `809f713e3c49d1d4700c84a240a6a86ba1439b94`

**Standing:** Analysis and candidate recommendation only; awaiting Mark/ChatGPT architectural adjudication. `ACCEPT` means acceptable within this review, not architectural approval. No identifiers, canonical relationships, Business Enabling responsibilities or implementation are allocated or changed.

## 1. Executive Assessment

**The candidate is broadly viable, but is not yet a sufficient, coherent MVP without boundary corrections.** Its eighteen CT1 frames preserve the current healthcare landscape, and most proposed abilities supply credible business context. The material weaknesses are operational care flow/capacity/logistics, ambiguous consent and device associations, and incomplete assurance criteria responsibility. Several proposed leaves describe objects, outputs or steps rather than enduring business abilities; others duplicate established Business Enabling names or subdivide reference context without a material Harmonia consequence.

The recommended approach is to retain the eighteen CT1 frames, make a small number of operational and assurance distinctions explicit, rename ambiguous leaves, and prune subordinate detail where a definition or example suffices. This leaves a model complete enough to justify Harmonia while deliberately incomplete as healthcare Enterprise Architecture. It does not assert that Harmonia owns the clinical, workforce, financial, public-health or research activities it informs.

**Coverage sufficiency is conditional on the decisions in §5.** The original candidate has credible broad justification for almost all established enabling areas; some important responsibilities cannot safely be explained by its current labels alone. The revised candidate in §7 addresses those problems. Neither this review nor acceptance of a Business Capability tree would resolve the separate Business Enabling CT ancestry/Feature-framing work.

### Authority and evidence used

Report source labels below are citation aids, not architectural identifiers. Catalogue ordinals are navigation references, not structural identity.

| Source | Role in this review |
| :--- | :--- |
| [S1 — Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md) | Highest architectural authority, including AX-18 as well as the requested AX-14/15/17. |
| [S2 — Business Capability catalogue](../../docs/markdown/02-strategy/capabilities/business-capabilities.md) | Eighteen frames, six established Individual Care Delivery children, quality rules, relevance and responsibility boundaries. |
| [S3 — Business Enabling catalogue](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) | Established enabling landscape, 137-Feature baseline, scope and assurance responsibilities; used as evidence of required business context. |
| [S4 — Capability decomposition / derivation model](../../docs/markdown/02-strategy/capability-maps/capability-tier-model.md) and [fixed-decomposition decision](../../docs/markdown/02-strategy/reviews/fixed-capability-decomposition-decision.md) | Fixed ancestry, bounded CT3 scope, decomposition versus derivation, representative rather than exhaustive traceability. |
| [S5 — Business Architecture metamodel](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md) | Capability/Feature/Function/Service/Process distinctions; global Capability-name uniqueness; clinical-work, information-ownership and sufficiency rules. |
| [S6 — Approved K9 reconciliation](../../docs/markdown/04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing) | Established identities/responsibilities remain valid while numerical tiers, full ancestry and structural IDs remain unresolved. |
| [S7 — Assurance Strategy derivation](../../docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md), [REQ-FND-005](../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity) and [assurance Business derivation](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md) | Independence, governed criteria, contextual evidence, assessment/adjudication, outcome communication, non-recursion and exclusion of subject remediation. |
| [S8 — Entity](../../docs/markdown/03-business-architecture/behaviours/01-entity-management.md), [Administration](../../docs/markdown/03-business-architecture/behaviours/02-service-administration.md), [Delivery](../../docs/markdown/03-business-architecture/behaviours/03-service-delivery.md), [Operations](../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md) and [Shared Enablement](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md) | Semantic coverage evidence, particularly identity, encounter/order progression, capacity, physical logistics, work integration, access and collaboration. |
| [S9 — Reference candidate capture](../../docs/markdown/03-business-architecture/reference-model/business-architecture.json) and [capture documentation](../../docs/markdown/03-business-architecture/reference-model/README.md) | Navigation and corroboration only. Candidate inclusion supplies no approval, ancestry or missing Capability identity. Source Markdown governs. |

The capture contains 24 Business Capabilities, 76 established Business Enabling contexts and 137 FT Features; all captured Capability tiers are unresolved. Its 69 enabling descriptions with unresolved standalone identity are not a supply of CT3s. The user explicitly supplies the eighteen CT1 frames and six agreed CT2 children as review input. This report uses those candidate tier positions without treating deprecated catalogue labels or captured `null` tiers as allocated canonical structure. The difference between task input and the repository's unallocated representation remains explicit.

### Material axiom consistency

| Axioms | Consequence for the recommendation |
| :--- | :--- |
| AX-01 / AX-04 | Business justification remains health-information centred and independent of software components, products and engines. |
| AX-05 / AX-06 / AX-11 / AX-12 | Record management, timely access and historical preservation have business purpose; current state, history, active availability and originating authority remain distinct. No cache/persistence CT3 is invented. |
| AX-02 / AX-13 | Information sharing has an explicit interoperability boundary; retained evidence does not confer control over an external recipient or emitted copy. |
| AX-07 / AX-08 / AX-09 | Rights, consent, security enforcement, attributable activity and meaningful evidence remain distinct. Operational context and diagnostics do not automatically become persistent records. |
| AX-14 / AX-16 / AX-18 | Clinical intent, performed care, reported facts, operational progression, resource readiness, service capacity and independent conclusions retain their different meanings and authorities. Integration does not confer clinical-work management. |
| AX-15 / AX-17 | Unknown operational effects remain unknown; unapproved decomposition and relationships remain candidates. Assurance evidence insufficiency remains distinct from execution uncertainty. |

Merging Capability identities below does not merge information states, authorities, outcomes or contracts. AX-14 requires preservation of meaningful distinctions; it does not require one Capability for every distinction. No axiom conflict requires amendment. Ambiguous candidate meanings and the existing collaboration-source conflict are preserved for decision, rather than resolved through implementation evidence.

## 2. Candidate Review

Every supplied candidate CT2 and CT3 is classified below: **44 CT2 and 100 CT3 items**. Names in the first column reproduce the supplied model. Proposed names are suggestions only. `MERGE` removes a separate candidate identity while retaining the stated business concern in its destination. `REMOVE` removes subordinate MVP modelling, not the enterprise activity or its existing CT1 context. `RELOCATE` proposes a different business responsibility frame, not a repository edit. Material `MISSING` proposals are consolidated in §3.

### 2.1 CT1 — Individual Care Delivery

The six agreed CT2s are retained. Assessment is decomposed by how knowledge of health state is established; setting, profession, specialty and information format are not decomposition axes. No displayed sequence is an obligatory care workflow. S2's definitions remain controlling.

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Individual Health Assessment | ACCEPT | |
| CT3 | Health History Assessment | ACCEPT | |
| CT3 | Clinical Examination | ACCEPT | |
| CT3 | Health Status Measurement | ACCEPT | |
| CT2 | Clinical Determination | ACCEPT | |
| CT3 | Clinical Finding Determination | ACCEPT WITH RENAME | **Clinical Finding Interpretation**: establishes clinical significance; assessment already establishes assessed findings. |
| CT3 | Diagnosis & Problem Determination | ACCEPT | |
| CT3 | Health Risk & Prognostic Determination | ACCEPT | |
| CT2 | Individual Care Planning | ACCEPT | |
| CT3 | Care Goal Definition | ACCEPT | |
| CT3 | Care Intervention Planning | ACCEPT WITH RENAME | **Care Response Planning**: includes intended intervention and monitoring, not just intervention selection. Prescribing expresses intent; it is not dose administration. |
| CT3 | Care Plan Coordination | MERGE | Clinical alignment of intentions belongs within Care Response Planning; coordination of fulfilment belongs within Care-Team Coordination in 2.2. No third undifferentiated owner. The identically named enabling catalogue description supplies no independent BC identity under K9. |
| CT2 | Clinical Intervention | ACCEPT | |
| CT3 | Therapeutic Intervention | REVIEW | Medication and procedures are also therapeutic. Preferred bounded label: **Therapy & Counselling Intervention**, covering performed therapeutic actions outside the medication/procedural leaves. Partition and residual coverage need adjudication; specialty-specific therapy trees are unnecessary. |
| CT3 | Medication Intervention | ACCEPT | |
| CT3 | Procedural Intervention | ACCEPT | |
| CT2 | Individual Health Monitoring | ACCEPT | |
| CT3 | Health State Monitoring | MERGE | Restates the agreed CT2 rather than demonstrating another business ability. Retain its meaning in Individual Health Monitoring; no replacement CT3 is manufactured. |
| CT3 | Treatment Response Monitoring | MERGE | A monitoring purpose/trajectory within Individual Health Monitoring. Interpreting response remains Clinical Determination; evaluating care effectiveness remains Care Review. |
| CT3 | Clinical Deterioration Monitoring | MERGE | A monitored change/threshold concern within Individual Health Monitoring. Distinct deterioration, stability and improvement semantics remain; event capture alone is not clinical interpretation. |
| CT2 | Individual Care Review | ACCEPT | |
| CT3 | Care Progress Review | MERGE | Interim progress is evidence for **Care Effectiveness Review**, proposed below. Timepoint alone does not justify a separate ability. |
| CT3 | Care Plan Review | ACCEPT WITH RENAME | **Care Intent Appropriateness Review**: evaluates whether existing intent remains appropriate; establishing revised intent belongs to Planning. |
| CT3 | Care Outcome Review | ACCEPT WITH RENAME | **Care Effectiveness Review**: evaluates achieved/interim care against objectives, distinct from health-state reassessment and from independent assurance. |

History establishes reported/recalled health context; examination establishes observed or elicited clinical signs; measurement establishes quantified health state. Examination may use measurements, but quantitative observations and examination findings retain their meaning and attribution. These are means of establishing knowledge, not laboratory specialties or record-access mechanisms. Risk/prognosis is interpretation under Clinical Determination.

The intervention recommendation is conditional: medicinal administration and a performed procedure must remain distinguishable from a prescription, dispense event, booking or request. A single episode may contain several abilities; this does not equate them. No assertion is made that the three CT3s form an exhaustive taxonomy of clinical practice.

Monitoring is deliberately left at its established CT2 in §7. No presently required **Business Capability-owned** Feature has been demonstrated that forces one of the proposed monitoring leaves to become an independently justified CT3. Existing Service Delivery Features retain their Business Enabling owners and require separate ancestry reconciliation. This is not permission to define a Feature directly under the monitoring CT2; a material future requirement would reopen the CT3 question.

### 2.2 CT1 — Care Access & Coordination

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Care Access Management | ACCEPT | |
| CT3 | Intake | ACCEPT WITH RENAME | **Care Intake Management**: identifies/registers the care recipient and establishes entry context. Intake is an ability, not one submission step; it does not acquire master-person reconciliation authority. |
| CT3 | Referral | ACCEPT WITH RENAME | **Care Referral Management**: manages the request for another participant's care responsibility and its disposition; referral submission does not establish responsibility assumed or Transfer of Care. |
| CT3 | Eligibility | ACCEPT WITH RENAME | **Care Eligibility Determination**: determines eligibility for service access, including applicable funding entitlement where relevant. It is distinct from information-access authorisation and from defining a service's eligibility rules. |
| CT2 | Care Scheduling & Allocation | ACCEPT WITH RENAME | **Care Scheduling & Capacity Management**: allocation alone fails to frame operational flow, resource readiness and available/committed/utilised service capacity. The proposed broader boundary requires decision. |
| CT3 | Appointment | ACCEPT WITH RENAME | **Care Appointment Management**: business scheduling/attendance context, not the Appointment information object or a claim that Harmonia owns source bookings. |
| CT3 | Care-Place Allocation | ACCEPT | |
| CT3 | Resource Allocation | REVIEW | Preferred **Care Resource Deployment**: operational availability and deployment of staff/equipment within authorised work. Exclude workforce establishment, clinical-work determination and unbounded resource/ERP management. Resource readiness does not equal service capacity. |
| CT2 | Care Coordination & Transition | ACCEPT | |
| CT3 | Care-Team Coordination | ACCEPT | |
| CT3 | Transfer | REVIEW | Physical movement and transfer of clinical responsibility have materially different outcomes. Replace the ambiguous leaf with **Care Responsibility Transition** here and **Patient Movement Coordination** in the proposed logistics branch. Arrival does not establish responsibility accepted. |
| CT3 | Discharge / Transition | ACCEPT WITH RENAME | **Care Discharge Coordination**: manages discharge readiness and operational exit coordination. Clinical clearance, discharge authorisation, information publication, physical departure and continuing care responsibility remain distinct. |

The additional business needs in §3 are operational care flow, care-place readiness, operational service capacity and support logistics. They justify the established session/queue, encounter, bed turnover, capacity, work dispatch, patient transport and specimen-custody responsibilities. Neither generic workflow coordination nor allocation covers all these abilities.

Care-Team Coordination aligns business care responsibilities and commitments. Digital collaboration provides the interaction environment. Workflow & Activity Coordination enables progression of operational Work Orders, human To Dos and synthetic Tasks. Those enabling mechanisms neither define the clinical plan nor acquire clinical-work/EMR worklist authority (S5 §§3.5–3.7; S8 Operations §§2.9–2.11).

### 2.3 CT1 — Health Rights, Advocacy & Participation

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Health Rights Management | ACCEPT | |
| CT3 | Consent & Directive Management | REVIEW | Clinical care consent/advance care intent and permission for information disclosure are different rights and decisions. Prefer **Care Consent & Advance Directive Management** plus the materially missing disclosure-consent frame in §3. A recorded preference is not automatically legal authority or security authorisation. |
| CT3 | Information Access Rights | ACCEPT WITH RENAME | **Health Information Rights Management**: establishes/exercises consumer rights concerning access and correction; it does not perform searches or own access-policy enforcement. |
| CT2 | Consumer Participation | ACCEPT | |
| CT3 | Care Participation | ACCEPT WITH RENAME | **Consumer Care Participation**: makes the participant and business ability explicit. |
| CT3 | Representative / Carer Participation | ACCEPT WITH RENAME | **Care Representation & Support Participation**: covers participation through valid representation and support relationships. Carer contact, nominated representative and legal substitute decision authority are not equivalent. |

Advocacy remains CT1 context; no advocacy-programme CT2/CT3 is materially necessary for the established enabling baseline. Rights/preferences originate here; privacy/security controls apply them within their own responsibilities.

### 2.4 CT1 — Diagnostic, Therapeutic & Clinical Support Services

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Diagnostic Services | ACCEPT WITH RENAME | **Diagnostic Support Provision**: avoid duplicating the established Business Enabling Capability name Diagnostic Services. This concerns the enterprise's diagnostic ability, not integration of its outputs. |
| CT3 | Diagnostic Request Fulfilment | ACCEPT WITH RENAME | **Diagnostic Investigation Performance**: performance of the requested investigation, not transport/queue acceptance or a closed-loop integration mechanism. |
| CT3 | Diagnostic Result Provision | ACCEPT WITH RENAME | **Diagnostic Reporting**: establishes and issues an attributable diagnostic result/report, including governed corrections. Transmission belongs to sharing/communication; treating-clinician interpretation belongs to Clinical Determination. |
| CT2 | Medication & Pharmacy Services | ACCEPT | |
| CT3 | Medication Supply / Dispensing | ACCEPT WITH RENAME | **Medication Dispensing**: supply to an individual under an applicable request. Procurement/inventory supply is Product Management context; prescribing and administering remain different abilities. |
| CT3 | Medication Therapy Support | MERGE | No distinct pharmacy responsibility is specified beyond the existing care abilities. Clinical medication review/advice belongs to Planning/Review or performed Intervention according to purpose; supply belongs to Dispensing. Medication-history integration does not justify a duplicate clinical ability. |
| CT2 | Therapeutic Support Services | MERGE | The candidate provides no orthogonal boundary beyond performed therapeutic care and operational support. Retain clinical content under Clinical Intervention and support preparation/logistics under Care Access & Coordination. CT1 still supplies specialist support-service context. |
| CT3 | Procedure Support | MERGE | Clinical performance belongs to Procedural Intervention; preparation, equipment readiness and material/support work belong to the operational branches in 2.2/§3. An undefined support label is not an additional capability owner. |
| CT3 | Allied / Therapeutic Service Provision | RELOCATE | Performed therapies belong to Clinical Intervention, principally the proposed Therapy & Counselling Intervention. “Allied” is a professional grouping, not a business-ability decomposition. Retain specialist-provider context as explanation of this CT1. |

Diagnostic performance and reporting are retained because investigation activity/outcome and an attributable report are not interchangeable facts for Harmonia's order/result lifecycle. They need not occur as two compulsory workflow steps, nor be provided by different organisations. Clinical-service provision can be a Business Capability; an actual exposed Business Service is a different element type. The word “service” alone neither validates nor invalidates a capability.

### 2.5 CT1 — Health Products & Clinical Technology

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Health Product Management | ACCEPT | |
| CT3 | Product Definition / Catalogue | ACCEPT WITH RENAME | **Health Product Catalogue Management**: ability to govern identifiable product/formulary context; definitions/catalogues are its information, not separate capabilities. |
| CT3 | Product Availability | REMOVE | No distinct supply-chain execution responsibility is required for this MVP. Availability facts can accompany product context or care-resource readiness; do not derive inventory/replenishment architecture. |
| CT2 | Clinical Technology Management | ACCEPT | |
| CT3 | Device Definition | ACCEPT WITH RENAME | **Clinical Device Identification & Specification**: type/model specification and identifiable physical instances remain distinct within the frame. Device Definition is also a named enabling catalogue description; its standalone Capability identity remains unresolved under K9. Similar wording establishes no equivalence. |
| CT3 | Device Availability / Association | REVIEW | Split business meanings. Preferred leaf **Clinical Device Use Attribution** identifies which device is associated with the subject/encounter/location relevant to information. Readiness/deployment belongs in Care Resource Deployment. Association does not prove availability, allocation authority or observation credibility. |

This small device distinction is material to telemetry attribution and safe patient/location binding (S3 Entity Management; S8 Entity §7). It does not make Harmonia a biomedical engineering or asset-maintenance system. S2 classifies this CT1 as Reference while S3 has Harmonia-Relevant device responsibilities; these classifications describe different capability responsibilities. They do not authorise automatic CT1 reclassification, nor prove that all of its enterprise scope needs decomposition.

### 2.6 CT1 — Clinical Quality, Safety & Improvement

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Clinical Safety Management | ACCEPT | |
| CT3 | Safety Concern / Incident Management | ACCEPT WITH RENAME | **Clinical Safety Concern & Response Management**: operational concern/incident response; an incident is not an assurance finding. |
| CT3 | Infection / Clinical Risk Monitoring | ACCEPT WITH RENAME | **Clinical Safety Threat Monitoring**: service-level operational infection/safety threats informing response. Individual prognosis belongs to Determination; population surveillance belongs to 2.8. No comprehensive risk lifecycle is implied. |
| CT2 | Clinical Quality Management | ACCEPT | |
| CT3 | Outcome / Quality Monitoring | ACCEPT WITH RENAME | **Clinical Quality Performance Monitoring**: service quality/outcome observation for management and improvement, not individual care-effectiveness review or independent conclusion authority. |
| CT3 | Continuous Improvement | ACCEPT | |

Monitoring to manage or improve remains here; independent evaluation against governing criteria belongs to Health Service Assurance. Shared measurements, audit records or clinical data do not erase the different purposes and authorities. Harmonia's information-safety contribution does not confer responsibility for clinical adequacy or an enterprise incident-management product.

### 2.7 CT1 — Community Health & Wellbeing

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Preventive Health | ACCEPT | |
| CT3 | Screening | ACCEPT WITH RENAME | **Preventive Screening Management**: programme eligibility, participation, recall and follow-up context. Individual investigation/performance and interpretation retain their clinical owners. |
| CT3 | Immunisation | ACCEPT WITH RENAME | **Immunisation Programme Management**: programme coverage, participation and recording/reporting context. A vaccine dose administered is Medication Intervention, not proof of programme fulfilment. |
| CT2 | Community Health Promotion | REMOVE | Existing CT1 reference context is sufficient; broad public-health promotion programmes do not explain additional Harmonia responsibilities. |
| CT3 | Health Promotion | REMOVE | No material lower-tier consequence beyond that reference context. |
| CT3 | Community Outreach | REMOVE | Promotion outreach is outside current required decomposition. Clinical outreach is a delivery context for the established care abilities, not a distinct business ability by setting. |

Preventive distinctions are retained because screening recall/eligibility and immunisation event/register obligations have different business meanings in the established enabling baseline. This does not change the CT1's Reference classification, move all individual preventive care out of Individual Care Delivery, or model complete public-health delivery.

### 2.8 CT1 — Population Health & Health-System Planning

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Population Health Surveillance | ACCEPT | |
| CT3 | Registry / Notifiable Condition Monitoring | ACCEPT WITH RENAME | **Notifiable Health Event Reporting**: explicit statutory notification responsibility justifies a separate ability. Registry membership is an information mechanism; no enterprise registry-management product is derived. |
| CT3 | Population Health Monitoring | ACCEPT | |
| CT2 | Health-System Planning | ACCEPT | Retain bounded adjacent context. |
| CT3 | Service Demand Analysis | MERGE | Illustrative analytical concern within Health-System Planning; no separately justified Harmonia ability. |
| CT3 | Regional Capacity Planning | MERGE | Illustrative planning concern within Health-System Planning. Strategic capacity planning remains different from managing current operational service capacity. |

Notification of an individual notifiable event and population pattern observation are different obligations/outcomes. Harmonia supplies governed information to the responsible public-health actor; transmission success does not establish that actor's surveillance conclusion. Demand forecasting, epidemiological models and regional planning algorithms are not derived.

### 2.9 CT1 — Health Information & Knowledge Management

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Health Information Management | ACCEPT | |
| CT3 | Clinical Record Management | ACCEPT | |
| CT3 | Health Information Access | ACCEPT WITH RENAME | **Health Information Discovery & Retrieval**: business ability to find/obtain information. The enabling Capability Health Information Access remains a separate established identity; rights and policy decisions belong elsewhere. |
| CT3 | Health Information Exchange | RELOCATE | Consolidate with the proposed **Health Information Sharing** under Connected Health Services. A second exchange owner is not justified by the candidate. The established identically named BEC is not reparented or renamed. |
| CT2 | Clinical Knowledge Management | ACCEPT | |
| CT3 | Clinical Knowledge Curation | ACCEPT | |
| CT3 | Clinical Knowledge Access | ACCEPT | |

Clinical Record Management includes longitudinal organisation, governed correction/supersession and durable historical preservation. It does not originate every clinical assertion or equate current information authority with storage success. Active information availability and authoritative durable progression remain different under AX-05; those implementation responsibilities do not become BC leaves.

Clinical Knowledge Management concerns usable clinical knowledge assets. Coding-system/model definitions and mapping-rule governance belong to Standards, Semantics & Reference Governance. Consuming governed knowledge/concept services does not transfer their ownership. The candidate does not require separate decision-support, guideline-authoring or knowledge-engine products.

### 2.10 CT1 — Standards, Semantics & Reference Governance

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Health Information Standards Governance | ACCEPT | |
| CT3 | Information Model Governance | ACCEPT | |
| CT3 | Exchange Specification Governance | ACCEPT | |
| CT2 | Clinical Semantics Governance | ACCEPT | |
| CT3 | Terminology Governance | ACCEPT | |
| CT3 | Classification Governance | ACCEPT | |
| CT3 | Semantic Mapping Governance | ACCEPT | |

This decomposition is materially justified: clinical terminology, purpose-specific classification and mapping assertions have different meanings, authorities and applicability constraints. Classification is not automatically clinical synonymy; mappings need not assert equivalence. Information-model meaning and interoperability-contract projections are also distinct. Governance of definitions/rules does not mean this BC owns every use, transformation or clinical interpretation. These are intrinsic subject-governance responsibilities, not relocation to Health Service Governance.

### 2.11 CT1 — Connected Health Services

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Health Information Connectivity | ACCEPT | |
| CT3 | Boundary Connectivity | ACCEPT WITH RENAME | **Standards-Based Health Information Connectivity**: establishes interoperable participation and applicable exchange contracts; not ports, adapters or a technology-specific connection. |
| CT3 | Cross-Enterprise Information Exchange | ACCEPT WITH RENAME | **Health Information Sharing**: business sharing across participants, including within an enterprise. Consolidates the duplicate exchange leaf in 2.9; scopes governed sharing and observable disposition without acquiring payload ownership. |
| CT2 | Digital Health Interaction | ACCEPT | |
| CT3 | Clinical Communication | ACCEPT WITH RENAME | **Clinical Message Conveyance**: directed communication of authored clinical intent/content with attributable delivery/disposition. Does not conduct a clinical consultation, interpret the message or perform a handover. |
| CT3 | Event / Notification Distribution | ACCEPT WITH RENAME | **Health Event Notification**: communicates significant clinical/operational change to applicable interested parties. Not a pub/sub implementation or proof that recipients acted. |
| CT3 | Digital Collaboration | ACCEPT WITH RENAME | **Health Service Collaboration**: sustained clinical and operational collaboration context; business ability exceeds a chat transport and covers General Collaboration as well as care-team interaction. |

Health Information Sharing covers submission, retrieval-mediated sharing, addressed distribution and syndication as interaction meanings, not four manufactured BC leaves. Discovery/retrieval in 2.9 serves the information user's need; sharing governs a participant exchange; connectivity establishes standards-compatible interaction. Clinical Message Conveyance concerns directed communication; collaboration concerns a sustained shared interaction context. Their exact contracts must preserve this separation.

S8 Shared Enablement's `Coordinate Care-Team Collaboration` description includes “clinical handovers”, while S5 §3.7 excludes Harmonia management of clinical handover. S9 explicitly preserves this source conflict. This review reports it; the recommended collaboration definition grants no clinical-handover authority and makes no source correction. Clinical discourse is not automatically an authoritative clinical record; read/receipt evidence is not clinical review, acceptance, incorporation or approval.

### 2.12 CT1 — Security, Privacy & Digital Trust

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Digital Identity & Access Governance | ACCEPT | |
| CT3 | Identity Assurance | ACCEPT | |
| CT3 | Access Control | ACCEPT | |
| CT2 | Health Information Protection | ACCEPT | |
| CT3 | Privacy Control | ACCEPT WITH RENAME | **Health Privacy Safeguard Management**: enforces permissible collection/use/disclosure within applicable rights and consent; not ownership of those rights. |
| CT3 | Information Protection | ACCEPT WITH RENAME | **Health Information Safeguarding**: integrity, confidentiality and applicable protection obligations; avoids an unbounded generic enterprise-security leaf. |
| CT2 | Digital Accountability & Trust | ACCEPT | |
| CT3 | Security Context / Attribution | ACCEPT WITH RENAME | **Governed Activity Attribution**: establishes actor, acting authority and purpose/context for accountable action. A context token is information, not a Capability. |
| CT3 | Access & Compliance Audit | ACCEPT WITH RENAME | **Security & Information-Use Accountability**: records/reviews attributable security-significant handling within the security boundary. Independent evaluation/conclusion belongs to 18; audit production alone is not assurance. |

Identity Assurance concerns the trusted acting identity/security context. Care-recipient identity/identifier correlation concerns who the health record or encounter is about, and needs business context in Care Intake Management and Clinical Record Management. The two must not be conflated. Here “assurance” does not by wording establish an independently governed Health Service Assurance activity.

Access Control determines whether an operation is permitted; information discovery/retrieval obtains information within that decision; consumer rights establish entitlement; privacy controls apply permissible-use/disclosure constraints. Patient consent, practitioner qualification, clinical privilege, operational privilege and technical authentication are distinct contributions, not interchangeable authority. Kleio's intrinsic mandatory capture authority remains distinct from the audited actor's authority under AX-07; no new business Capability or authority mechanism is inferred.

### 2.13 CT1 — Health Research & Innovation

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Health Research Enablement | ACCEPT WITH RENAME | **Research Information Support**: business support for governed research information needs, rather than a software-enablement boundary or research delivery product. |
| CT3 | Research Data Provision | ACCEPT WITH RENAME | **Governed Research Information Provision**: authorised, purpose-qualified information provision, including applicable de-identification. |
| CT3 | Research Cohort / Data Preparation | MERGE | Preparation/cohort selection serves Governed Research Information Provision; no separate research cohort-management or analytics responsibility is established. Preparation is not a generic new Capability merely because it is a Function. |
| CT2 | Health Innovation | ACCEPT | Bounded contextual evaluation purpose, not innovation-portfolio ownership. |
| CT3 | Innovation Evaluation | ACCEPT WITH RENAME | **Health Innovation Evaluation**: explains use of governed synthetic/testbed information and evaluation context without deriving a trial or test-management product. |
| CT3 | Clinical Technology Evaluation | MERGE | A subject of Health Innovation Evaluation in this MVP. Regulatory device assessment, procurement qualification and clinical-practice evaluation are not added. |

The limited information-support/evaluation framing explains S2's extracts and isolated synthetic testbed context. It does not derive a research ethics system, trial lifecycle, recruitment capability, genomic platform, or authority to approve clinical innovations. Production isolation remains unchanged.

### 2.14 CT1 — Health Service Direction & Stewardship

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Health Service Direction | MERGE | Retain as explanatory content of the existing CT1. No additional enabling responsibility needs this subordinate identity. |
| CT3 | Strategic Direction | MERGE | CT1 reference context is sufficient; no Harmonia strategy-management product. |
| CT3 | Service Priority Setting | MERGE | CT1 reference context is sufficient; distinct from governance establishing obligatory constraints. |
| CT2 | Health Service Stewardship | MERGE | CT1 reference context is sufficient; executive stewardship is outside platform automation. |
| CT3 | Service Sustainability | MERGE | CT1 reference context is sufficient; no financial/workforce sustainability decomposition is required. |
| CT3 | Organisational Decision Support | REMOVE | A broad support mechanism/benefit, not a defined orthogonal business ability. Supplying operational information does not require a new executive decision-support Capability. |

This leaves the established CT1 intact and its business purpose intelligible. Governance authority stays in 17; independent assurance stays in 18. System Steward's established assurance-status consumption does not imply executive health-service stewardship ownership or require this reference tree to be fully decomposed.

### 2.15 CT1 — Workforce & Organisational Capability

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Health Workforce Management | ACCEPT | |
| CT3 | Practitioner Qualification | ACCEPT WITH RENAME | **Practitioner Qualification Management**: professional registration/qualification and competency standing; distinct from operational badges, role information and clinical privilege. |
| CT3 | Practitioner Role / Engagement | ACCEPT WITH RENAME | **Practitioner Engagement & Privilege Management**: contextual roles, affiliations, scope and approved clinical privilege; role resolution itself confers no privilege. Operational authority remains a separate determination. |
| CT3 | Workforce Availability | REVIEW | Preferred **Workforce Service Coverage Management**: governed roster/on-call and service coverage context. Current staff presence, deployment and mobile-service capacity belong to operational care responsibilities. Do not derive full HR/rostering ownership. |
| CT2 | Health Organisation Management | ACCEPT WITH RENAME | **Healthcare Organisation & Service Structure Management**: the original name is too narrow for the proposed organisation, physical-location and service-offering responsibilities. Retain their different identities and authorities within an enterprise structure frame. |
| CT3 | Organisation Structure | ACCEPT WITH RENAME | **Healthcare Organisation Identity & Structure Management**: includes verified organisational identity/profile and relationships, not hierarchy alone. |
| CT3 | Healthcare Location Structure | ACCEPT WITH RENAME | **Healthcare Location Definition Management**: identity, geography, physical containment and static care-place definition. Not organisational hierarchy, occupancy or turnover. |
| CT3 | Service Organisation | ACCEPT WITH RENAME | **Healthcare Service Offering Management**: defines identifiable service offerings, service/provider/location/role relationships, availability windows and eligibility rules. An Organisation/Location/Practitioner association alone does not establish Healthcare Service context. |

These are enterprise responsibilities for legitimate providers, places and offerings, not a copy of directory software structure. They justify verified directory information, routing destinations, relationships and service context used across care, access and operations. Offering definitions remain distinct from eligibility decisions and current service capacity. Organisation/location/role and service identities remain distinct even when grouped under one CT2.

Operational support staff are not all Practitioners. Their authorised participation and deployment are contextual responsibilities within Care Support Work Coordination and Care Resource Deployment; general employment/facility support remains reference context. The recommendation does not force every support worker into a clinical Practitioner identity, or collapse Clinical Qualification Management with the independently established badge/access Credential Management (S6 §11.6; S8 Operations §§2.12/2.16).

### 2.16 CT1 — Corporate Resources & Enterprise Services

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Financial & Commercial Services | ACCEPT | Retain reference financial context; only the materially relevant interface purpose needs a CT3. |
| CT3 | Charging / Claims Interface | ACCEPT WITH RENAME | **Clinical Activity Charging Information Provision**: business provision of attributable activity/charging information to responsible financial actors. An interface itself is a Service/contract concern. |
| CT3 | Financial Administration | MERGE | General financial administration is already the CT2's reference context. No ledger, payroll, claims adjudication or commercial lifecycle is required. |
| CT2 | Corporate Support Services | ACCEPT | Retain reference facility/employment support context, including legitimate physical-access arrangements. |
| CT3 | Facilities / Corporate Resource Support | MERGE | Restates a broad support basket without an additional Harmonia distinction. Relevant care-place readiness and care logistics are explicitly framed in 2.2; remaining corporate detail stays CT2 context. |

Financial information exchange does not transfer billing or claims authority. Funding entitlement informs Care Eligibility Determination; it is different from a clinical charging record. Physical badge/access information has credible facility-support and authorised-work context; it does not become patient identity or clinical qualification. No standalone Financial Governance capability is added: S7 explicitly excludes that Harmonia responsibility.

### 2.17 CT1 — Health Service Governance

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Governance Framework Management | ACCEPT | |
| CT3 | Policy / Obligation Governance | ACCEPT | |
| CT3 | Authority & Constraint Governance | ACCEPT | |
| CT2 | Compliance Governance | ACCEPT | |
| CT3 | Compliance Requirement Management | ACCEPT WITH RENAME | **Compliance Obligation Definition**: establishes applicable compliance requirements, not day-to-day enforcement, case handling or evidence evaluation. |
| CT3 | Governance Checkpoint / Reporting Management | REVIEW | Preferred **Governance Reporting & Checkpoint Definition**: establishes what reports/checkpoints are required. Performing reports, progressing activity and independently evaluating evidence remain with their respective capabilities. |

This is a normative-envelope model. Subject-specific governance of products/devices, standards, privacy/security and workforce remains intrinsic to those subjects. Risk may inform governance, management and assurance; a contextual Risk Register does not justify Risk Appetite, Treatment, Ownership or complete GRC architecture. BC-17 relevance classification and Business Enabling derivation remain unestablished despite a plausible candidate BC decomposition.

### 2.18 CT1 — Health Service Assurance

| Tier | Candidate | Classification | Exception / boundary |
| :--- | :--- | :--- | :--- |
| CT2 | Assurance Design | ACCEPT WITH RENAME | **Health Service Assurance Specification**: enterprise ability to define the assurance approach. The established Assurance Design BEC retains its name and identity; this is not an ancestry assignment to it. |
| CT3 | Assurance Scope / Method Definition | ACCEPT WITH RENAME | **Assurance Approach Definition**: scope, method, applicability and explicit assurance disposition, including governed decisions that a concern is not assurable/not worth assuring. |
| CT3 | Assurance Evidence Requirement Definition | MERGE | Include evidence requirements in the missing **Assurance Evaluation Criteria Governance** alongside criterion meaning and temporal applicability. Required evidence remains distinct from the source information later associated as evidence. |
| CT2 | Assurance Evaluation | ACCEPT | |
| CT3 | Evidence Assessment | ACCEPT WITH RENAME | **Assurance Evidence Assessment**: evidence/context association and assessment against applicable criteria, preserving temporal/version specificity and confidence/insufficiency. It does not establish the final conclusion by itself. |
| CT3 | Assurance Finding / Conclusion | ACCEPT WITH RENAME | **Assurance Conclusion Determination**: adjudicates what finding/conclusion follows. A finding/conclusion is an output; the ability is determining it from assessed evidence within independent authority. |
| CT2 | Assurance Oversight | MERGE | No independent oversight mandate is established beyond specifying assurance and managing/evaluating its own assurance activity. Integrate those concerns in Specification/Evaluation; operational oversight/remediation stays outside assurance. |
| CT3 | Assurance Exception / Disposition Management | MERGE | Exceptions are evaluated/adjudicated in Assurance Evaluation; assurance disposition is specified in Assurance Approach Definition. Operational remediation/escalation belongs to the subject's management. Do not create an assurance-led corrective-action lifecycle. |

The additional criteria-governance frame and the outcome-communication definition obligation are recorded in §3. The preferred Business tree groups specification and evaluation responsibilities without imposing a one-to-one structure on the three approved BECs. The five existing assurance Functions, three Processes, three Services and three Interactions retain their owners; none is promoted, renamed or reattached by this report.

Independent assurance may govern its own progression, but the subject performer/manager must not control that progression or conclusion. Source information remains source-owned; contextual evidentiary association belongs to assurance. Insufficient evidence does not mean criterion satisfaction/non-satisfaction, and is not an uncertain execution effect under AX-15. Reporting begins from an established finding/conclusion and does not re-adjudicate it. Health Service Assurance does not assure the clinical adequacy of care, assume financial governance, or recursively assure its own execution (REQ-FND-005; S7).

## 3. Material Gaps

These are the only proposed additional Capability identities. Their names, tiers and placement remain candidate recommendations. Definition-level coverage repairs follow separately; they do not justify new identities by themselves.

| Classification / proposed capability | Proposed frame | Material business reason and enabling evidence |
| :--- | :--- | :--- |
| **MISSING — Operational Care Flow Coordination** (CT3) | Care Access & Coordination → Care Scheduling & Capacity Management | Progresses presence, sessions, queues and encounters through operational service access/use. Booking, bed allocation and information sharing alone do not justify the established encounter/session/queue/progression responsibilities. Clinical milestones remain externally authoritative inputs (S3 Administration/Operations; S8 Operations §§2.1–2.5). |
| **MISSING — Operational Care Capacity Management** (CT3) | Same CT2 | Understands/manages available, committed and utilised service capacity in context. A ready resource is not service capacity; current operational capacity is not strategic regional planning. This Business frame does not merge the six established context-specific capacity responsibilities or grant a regional consumer their originating authority (S8 Operations §1). |
| **MISSING — Care-Place Readiness Management** (CT3) | Same CT2 | Establishes readiness/turnover before allocation/use. Allocation and structural definition cannot stand in for vacancy, cleaning, restrictions and preparation. This justifies bed-state/turnover/readiness responsibilities without modelling all environmental-services operations (S3 HSO; S8 Operations §2.6). |
| **MISSING — Care Support Logistics Management** (CT2) | Care Access & Coordination | Cohesive operational support for care access and fulfilment, rather than an IT/workflow abstraction. Contains the three business abilities below. |
| **MISSING — Patient Movement Coordination** (CT3) | Care Support Logistics Management | Coordinates physical patient transit and applicable mobility constraints. Resolves the candidate Transfer ambiguity; a moved patient is not proof of transferred clinical responsibility (S3 HSO; S8 Operations §2.13). |
| **MISSING — Clinical Material Logistics Coordination** (CT3) | Same CT2 | Coordinates physical clinical materials and chain of custody relevant to care/diagnostic fulfilment. Neither patient movement nor digital payload exchange justifies specimen transport custody (S8 Operations §2.14). |
| **MISSING — Care Support Work Coordination** (CT3) | Same CT2 | Allocates/coordinates authorised operational support work and its completion/escalation. Explains established work matching/dispatch/oversight without granting clinical-work allocation authority or replicating Work Order/To Do/Task as BC leaves (S8 Operations §§2.9–2.11; S5 §3.7). |
| **MISSING — Care Responsibility Transition** (CT3) | Care Access & Coordination → Care Coordination & Transition | Establishes/coordinates who has actually assumed applicable care responsibility. A referral request, transport arrival, discharge readiness or message receipt is insufficient. This is the other materially necessary meaning hidden in Transfer (S3 referral/order distinction; S8 Administration §3). |
| **MISSING — Information Disclosure Consent Management** (CT3) | Health Rights, Advocacy & Participation → Health Rights Management | Establishes the person's governed information-sharing choices/directives. Clinical treatment consent/advance care intent is not blanket disclosure permission; subsequent enforcement is a different security/privacy responsibility (S3 Client Privacy/Health Information Control; S8 Entity §2.4). |
| **MISSING — Assurance Evaluation Criteria Governance** (CT3) | Health Service Assurance → Health Service Assurance Specification | Establishes/manages applicable evaluation criteria, evidence requirements, versions and temporal applicability without changing their source obligations. Scope/method and evidence alone do not provide the business justification for approved Assurance Criteria Management (S3/S7). |

**Definition-level repairs, not additional Capability proposals:** explicitly include care-recipient identification/registration in Care Intake Management and record association; clinical/operational privilege distinctions in workforce and operational work definitions; identifiable Healthcare Service offerings and provision relationships in Healthcare Service Offering Management; device type versus instance and patient/location attribution in the device branch; clinical record current state versus preserved historical truth; and clinical versus general operational collaboration in Health Service Collaboration.

Assurance Evaluation must explicitly encompass communicating established findings/conclusions to legitimate authorised recipients. Determining a conclusion does not establish that communication occurred; activity-status observation is also different (S7 Business derivation §§5.2–5.3). Those distinctions require clear business responsibility and contracts, but the current evidence does not require a separate CT3 communication identity. The approved reporting Process and outcome Service therefore remain evidence of required behaviour, not a template for promoting another Capability.

Without those definitions, apparently covered enabling areas remain semantically under-justified. With them, separate patient-identity, directory, on-call, calendar, dashboard, workflow-engine, specimen-analysis specialty, resource-type, or clinical-setting BC trees are unnecessary. No master-person/EMPI, risk-management, Financial Governance or Assurance-of-Assurance gap is asserted.

## 4. Material Overreach

| Direction of overreach | Candidate pressure | MVP limit |
| :--- | :--- | :--- |
| Complete clinical practice / EMR | Overlapping therapeutic categories; separate treatment-response/deterioration monitoring; progress versus outcome by timepoint; care-plan coordination without an authority boundary. | Keep the six agreed clinical abilities, only justified CT3 distinctions, and explicit intention/performance/interpretation boundaries. Do not infer clinical worklists, prescribing authority, specialty pathways or an exhaustive intervention taxonomy. |
| Practice/hospital operations | Resource Allocation, Transfer, Procedure Support and Workforce Availability can absorb all scheduling, staffing, equipment and clinical workflow. | Make flow, readiness, capacity and support logistics explicit but bounded. No room-by-room, ward/theatre/ED BC decomposition, HR roster engine or full hospital logistics model. Preserve established enabling operational contexts independently. |
| Supply-chain / ERP | Product Availability, financial administration and facilities/corporate resources invite procurement, inventory, payroll and maintenance trees. | Catalogue/device attribution and attributable clinical charging information suffice; retain other enterprise context above CT3. |
| Complete public-health architecture | Promotion/outreach programmes and demand/regional capacity analysis. | Retain CT1 reference/adjacent context; detail only screening/immunisation, notifiable reporting and population monitoring needed to explain information obligations. |
| Complete research / innovation architecture | Research cohort preparation as an independent ability, plus separate clinical-technology evaluation. | Governed information provision and bounded evaluation context; no trials, recruitment, ethics workflow, research platform or innovation portfolio. |
| Executive / complete GRC architecture | Strategic priorities/sustainability decomposition; broad reporting/checkpoint management; assurance oversight and exception disposition. | CT1 executive reference context, governance defining the normative envelope, and independent assurance specification/evaluation. No risk-treatment, compliance-case, remediation or recursive-assurance product. |

These limits reduce scope by avoiding identities that have no additional responsibility consequence. They do not delete established catalogue context or deny that the wider enterprise exercises those abilities.

## 5. Boundary Issues Requiring Architectural Decision

This is the bounded adjudication list for Mark/ChatGPT. Decisions concern candidate business semantics, not approval to edit the architecture or implement a solution.

1. **Clinical decomposition:** approve the assessment means and the interpretation/intent/performance/review boundaries; decide whether Therapy & Counselling Intervention is a sufficient residual clinical frame. Accept monitoring's CT3 as unestablished where no independently justified distinction has been demonstrated.
2. **Care plan and care responsibility:** merge plan coordination according to purpose; approve clinical intent alignment in Planning, operational care commitments in Care-Team Coordination, and the physical-movement versus Care Responsibility Transition split. Neither plan dissemination nor recipient acknowledgement determines clinical intent or responsibility assumed.
3. **Operational scope:** approve the broader scheduling/capacity frame and the support-logistics branch. Capacity remains contextual and distinguishable from resource readiness; staff/equipment deployment does not grant clinical-work determination. Workforce Service Coverage Management concerns coverage context, with current presence/deployment under operations.
4. **Medication/support boundary:** distinguish prescribed intent, patient-specific dispensing, performed administration and subsequent monitoring/review. Retire undefined Medication Therapy Support/Procedure Support as independent leaves unless a business responsibility beyond those existing frames is explicitly demonstrated.
5. **Information, rights and exchange:** approve separation of treatment/advance-care consent from disclosure consent; distinguish rights, obtaining information and security permission. Consolidate duplicate exchange under Health Information Sharing, preserving the existing BEC identities and all four interaction meanings.
6. **Communication/collaboration and device attribution:** approve directed-message, event-notification and sustained-collaboration definitions; reconcile the existing clinical-handover source conflict separately. Approve device attribution versus readiness/deployment and structural versus operational location/service definitions.
7. **Governance/quality/assurance:** approve governance as requirement/checkpoint definition, quality/safety as operational monitoring/response, and assurance as independently controlled specification/evaluation. Approve the criteria-governance frame and explicitly require authorised outcome communication within evaluation without a separate communication Capability; exclude assurance-led subject remediation, Financial Governance and recursion. Preserve REQ-FND-005 and established source/criterion authority.
8. **Scope and standing:** approve subordinate pruning while retaining all eighteen CT1 contexts, global name separation from BECs, and explicit unestablished leaves. BC-17/18 relevance, BC-17 enablement, BEC ancestry, Feature framing, identifiers and catalogue relationships are separate decisions; none follows automatically from this review.

Open meanings are architectural uncertainty under AX-17. Unknown operation effects are AX-15 uncertainty. Evidence insufficient for assurance conclusion is the separate REQ-FND-005 condition. None is resolved by a well-formed tree or successful software build.

## 6. Business Enabling Coverage Assessment

**The revised candidate provides sufficient business context for the established enabling landscape, subject to the adjudications and definitions above. The original candidate provides broad coverage but is not yet sufficient to establish its most important responsibility boundaries.** This is a semantic sufficiency assessment, not approval of BC↔BEC edges, a claim of Feature ancestry completion, or an exhaustive matrix.

| Material enabling area inspected | Business justification and sufficiency finding |
| :--- | :--- |
| Client/person/subject identity and relationships | Care Intake Management, Clinical Record Management and participation/representation need dependable subject identification and valid relationships. Explicit identifier/subject context definitions repair a gap in the original labels. Patient identity is distinct from digital actor authentication; authoritative upstream correction remains different from originating master-person decisions. |
| Provider, organisation, location, service, product and device information | Legitimate qualified providers, identifiable organisations/places/service offerings, contextual service eligibility/availability, and attributable product/device use explain these responsibilities. The broadened organisation/service/device names remedy narrow “structure/definition” labels. No one BC per directory BEC is required. |
| Care administration: intake, referral, scheduling, encounters, orders, diagnostics, medication, procedures, care plans and communications | Entry, responsibility request/transition, operational flow, care intent, performed intervention, diagnostic reporting and sharing provide coherent context. Referral ≠ Order; booking/request/acceptance ≠ performance; prescription ≠ dispense ≠ administration. An independent generic Order BC is unnecessary: bounded fulfilment across diagnostic/medication/procedure activity explains order enablement. |
| Clinical Service Delivery contexts | Assessment/determination/planning/intervention/monitoring/review and their supporting information/coordination abilities explain the baseline across primary, acute, emergency, inpatient, community, outreach and virtual settings. The established heterogeneous enabling views are not BC CT2s; no setting-specific BCs are added. |
| Operational service flow, care places, resources, capacity, staffing, work, transport, materials and discharge | This is the largest original justification gap. The proposed flow/capacity/readiness/logistics abilities and explicit coverage/deployment definitions cover it without modelling all hospital operations. Context-specific capacity authority and source clinical clearances remain intact. |
| Shared clinical record, access and presentation | Clinical Record Management, Health Information Discovery & Retrieval and legitimate clinical/operational consumers justify longitudinal assembly, preservation, timely access and contextual presentation. Presentation is enablement, not a new business responsibility to practice care or own the displayed information. |
| Standards, clinical knowledge and semantics | Clinical Knowledge Management plus standards/model/terminology/classification/mapping governance provide sufficient, distinguishable context. Their business purposes do not depend on schema validators or terminology-engine boundaries. |
| Exchange, communication, collaboration, calendar and workflow/activity coordination | Sharing, message conveyance, notification, service collaboration and operational care/coverage responsibilities justify these cross-cutting abilities. Calendar projects source schedules; generic workflow supports distinct human and synthetic activity. Neither needs a one-to-one BC mirror or a generic workflow-engine BC. |
| Security, consent, privacy and accountability | Rights/disclosure consent, trusted acting identity, permission, safeguards, attribution and meaningful accountability provide sufficient context. Audit evidence production and operational access/qualification checks remain different from independent assurance and from each other. |
| Three approved assurance BECs | Assurance specification, explicit criteria governance, independent evidence assessment/conclusion and authorised outcome communication explain Assurance Design, Assurance Criteria Management and Governed Assurance. Approved REQ-FND-005 → BC-18 derivation remains intact; no new BEC/EC or owner assignment follows. |
| Adjacent/reference funding, charging, research, innovation and enterprise context | Eligibility/funding context, clinical charging information provision, governed research information and evaluation purpose suffice. Executive, corporate and broad public-health context can remain selectively undecomposed. Their lack of CT3 detail is not proof of missing enablement. |

The most suspicious structural copies are **Health Information Access**, **Health Information Exchange**, **Diagnostic Services**, and **Assurance Design**, which duplicate established enabling Capability names. **Device Definition** and **Care Plan Coordination** also repeat enabling catalogue descriptions whose standalone identities are unestablished. Names alone establish neither semantic identity nor a parent relationship. The recommended renames/merges separate the enterprise purpose from the system-enabling responsibility; they leave established BEC names unchanged.

Candidate areas without a material lower-tier justification are broad promotion/outreach, executive direction/stewardship detail, product availability as supply-chain execution, corporate/financial administration detail, research-cohort management, and unbounded assurance oversight. Context is retained at a sufficient higher frame rather than requiring every enterprise activity to receive a new identity.

Existing source uncertainties remain: the clinical-handover wording conflict, unestablished individual Service/Feature consumption associations, BC-17 enabling derivation, and K9's unresolved BEC ancestry. Coverage can be semantically sufficient while those formal relationships remain unestablished. This task neither repairs them by inference nor resumes Domain05 B2.2.

## 7. Recommended MVP Candidate

**Proposed for adjudication; not approved architecture.** The three indentation levels below mean CT1 → CT2 → CT3. They allocate no codes or canonical structural identity. `†` marks a preferred resolution of a `REVIEW` boundary; selection remains subject to §5. Added items are justified in §3; all other names/dispositions come from §2.

An explicit `[CT3 not established]` or `[no subordinate MVP decomposition]` marker is a boundary/status annotation, never a Capability. Selectively stopping at CT1/CT2 is compatible with the fixed model where no Feature is framed there. Every required Feature must still ultimately have an independently justified CT3 with complete ancestry. In particular, this business-context tree supplies no ancestry for the 137 Business Enabling Features.

```text
Individual Care Delivery
  Individual Health Assessment
    Health History Assessment
    Clinical Examination
    Health Status Measurement
  Clinical Determination
    Clinical Finding Interpretation
    Diagnosis & Problem Determination
    Health Risk & Prognostic Determination
  Individual Care Planning
    Care Goal Definition
    Care Response Planning
  Clinical Intervention
    Therapy & Counselling Intervention †
    Medication Intervention
    Procedural Intervention
  Individual Health Monitoring
    [CT3 not established; response/deterioration are explicit monitoring concerns]
  Individual Care Review
    Care Intent Appropriateness Review
    Care Effectiveness Review

Care Access & Coordination
  Care Access Management
    Care Intake Management
    Care Referral Management
    Care Eligibility Determination
  Care Scheduling & Capacity Management †
    Care Appointment Management
    Care-Place Allocation
    Care-Place Readiness Management
    Care Resource Deployment †
    Operational Care Flow Coordination
    Operational Care Capacity Management
  Care Coordination & Transition
    Care-Team Coordination
    Care Responsibility Transition †
    Care Discharge Coordination
  Care Support Logistics Management
    Patient Movement Coordination †
    Clinical Material Logistics Coordination
    Care Support Work Coordination

Health Rights, Advocacy & Participation
  Health Rights Management
    Care Consent & Advance Directive Management †
    Information Disclosure Consent Management †
    Health Information Rights Management
  Consumer Participation
    Consumer Care Participation
    Care Representation & Support Participation

Diagnostic, Therapeutic & Clinical Support Services
  Diagnostic Support Provision
    Diagnostic Investigation Performance
    Diagnostic Reporting
  Medication & Pharmacy Services
    Medication Dispensing

Health Products & Clinical Technology
  Health Product Management
    Health Product Catalogue Management
  Clinical Technology Management
    Clinical Device Identification & Specification
    Clinical Device Use Attribution †

Clinical Quality, Safety & Improvement
  Clinical Safety Management
    Clinical Safety Concern & Response Management
    Clinical Safety Threat Monitoring
  Clinical Quality Management
    Clinical Quality Performance Monitoring
    Continuous Improvement

Community Health & Wellbeing
  Preventive Health
    Preventive Screening Management
    Immunisation Programme Management
  [Health promotion/outreach remain CT1 reference context]

Population Health & Health-System Planning
  Population Health Surveillance
    Notifiable Health Event Reporting
    Population Health Monitoring
  Health-System Planning
    [CT3 not established; demand and strategic capacity are contextual concerns]

Health Information & Knowledge Management
  Health Information Management
    Clinical Record Management
    Health Information Discovery & Retrieval
  Clinical Knowledge Management
    Clinical Knowledge Curation
    Clinical Knowledge Access

Standards, Semantics & Reference Governance
  Health Information Standards Governance
    Information Model Governance
    Exchange Specification Governance
  Clinical Semantics Governance
    Terminology Governance
    Classification Governance
    Semantic Mapping Governance

Connected Health Services
  Health Information Connectivity
    Standards-Based Health Information Connectivity
    Health Information Sharing
  Digital Health Interaction
    Clinical Message Conveyance
    Health Event Notification
    Health Service Collaboration

Security, Privacy & Digital Trust
  Digital Identity & Access Governance
    Identity Assurance
    Access Control
  Health Information Protection
    Health Privacy Safeguard Management
    Health Information Safeguarding
  Digital Accountability & Trust
    Governed Activity Attribution
    Security & Information-Use Accountability

Health Research & Innovation
  Research Information Support
    Governed Research Information Provision
  Health Innovation
    Health Innovation Evaluation

Health Service Direction & Stewardship
  [No subordinate MVP decomposition; direction/priorities/sustainability remain context]

Workforce & Organisational Capability
  Health Workforce Management
    Practitioner Qualification Management
    Practitioner Engagement & Privilege Management
    Workforce Service Coverage Management †
  Healthcare Organisation & Service Structure Management
    Healthcare Organisation Identity & Structure Management
    Healthcare Location Definition Management
    Healthcare Service Offering Management

Corporate Resources & Enterprise Services
  Financial & Commercial Services
    Clinical Activity Charging Information Provision
  Corporate Support Services
    [CT3 not established; remaining facility/corporate support is reference context]

Health Service Governance
  Governance Framework Management
    Policy / Obligation Governance
    Authority & Constraint Governance
  Compliance Governance
    Compliance Obligation Definition
    Governance Reporting & Checkpoint Definition †

Health Service Assurance
  Health Service Assurance Specification
    Assurance Approach Definition
    Assurance Evaluation Criteria Governance
  Assurance Evaluation
    Assurance Evidence Assessment
    Assurance Conclusion Determination
```

Single-child branches are not filled with invented siblings. A CT2 may retain wider contextual responsibilities while only a materially needed part has CT3 detail; a child must still have a genuine scoped ability beyond a renamed parent. If a future Feature requires detail in an unestablished branch, establish that CT3 from business meaning first. Existing Feature names, identifiers, owners and partial ancestry remain untouched.

## 8. Repository Impact if Approved

Approval of this recommendation would authorise a subsequent reconciliation only to the extent explicitly instructed. The following inventory identifies likely impact; no document, relationship, JSON record, schema or fingerprint listed here was changed in this task.

| Canonical / RADS artefact | Subsequent reconciliation need |
| :--- | :--- |
| `docs/markdown/02-strategy/capabilities/business-capabilities.md` | Approved ability definitions, CT2/CT3 decomposition, exceptions/status, name uniqueness and boundaries; preserve eighteen frames, six agreed care children and relevance unless separately adjudicated. |
| `docs/markdown/02-strategy/README.md`, `capabilities/index.md`, `capability-maps/index.md` | Navigation/count/decomposition explanations only where the approved model changes them. Natural regions remain contextual views. |
| `docs/markdown/02-strategy/capability-maps/capability-tier-model.md` | Representative business derivation and sufficiency examples where affected; preserve fixed hierarchy, no manufactured ancestry and no exhaustive matrix. |
| `docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md` | Align references to the approved Business decomposition while preserving the already approved three BECs, EC contributions and REQ-FND-005 boundaries. Any additional enabling relation needs its own authority. |
| `docs/markdown/02-strategy/reviews/` decision records | Persist the actual adjudication and scope. Preserve historical five-region/fixed-decomposition/K9 evidence; do not rewrite historical approval into approval of this candidate. |
| `docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md` | Check conformance and, only if needed, reconcile examples. This review requires no metamodel or axiom amendment. |
| `docs/markdown/03-business-architecture/behaviours/` and `information-responsibility/information-responsibility.md` | Check terminology and business-context references, especially capacity, logistics, collaboration and assurance. Existing BEC-owned behaviour does not automatically relocate to a new Business Capability. Resolve the reported handover conflict through a separately approved semantic correction. |
| `docs/markdown/03-business-architecture/processes/business-processes.md`, `dependencies/cross-capability-dependencies.md`, and collaborations/interactions/roles catalogues | Impact review of established references only. No new Process, Function, Service, Role, consumption edge or participant is mechanically derived from the tree. |
| `docs/markdown/03-business-architecture/reference-model/business-architecture.json` | Semantically reconcile approved Business elements/parents, candidate/unresolved standing and source locators/fingerprints. Preserve BECs, 137 local Feature IDs, unresolved dimensions and established behavioural ownership. Internal capture keys and catalogue ordinals are not Canonical IDs. |
| Reference-model `README.md`, `business-architecture.schema.json`, `scripts/rads_business_architecture.py` and its tests | Document changed capture scope/counts; assess separately authorised enforcement of mandatory CT3 Feature framing. The documented validator/schema enforcement gap is pre-existing; a representation PASS alone does not prove fixed-model conformance. Do not refresh fingerprints to conceal source drift. |
| Relevant Domain04 traceability/information-family references and Domain05 B2.1 traceability | Check only genuine effects of approved business semantics/references. Preserve the established six-child `enabled-by Health Information Access` relation and K9 uncertainty. No information family, FHIR representation, component allocation or Domain05 B2.2 follows from this task. |

The Business Enabling catalogue is an input, not a redesign target. Any later reference reconciliation must preserve its established responsibilities/identities and unestablished ancestry; this report creates no BC↔BEC edges. Source-fingerprint and locator updates belong to that later semantic reconciliation, not a mechanical capture refresh. Canonical ID/code allocation remains separate and unperformed.

### Review verification and stop boundary

Initial working tree was clean on the requested branch. Builds do not validate the semantic recommendation. Verification results:

| Check | Result / evidence |
| :--- | :--- |
| Review completeness | **PASS** — all 44 supplied CT2 and 100 supplied CT3 candidates have exactly one classification; all 18 CT1s and all eight required report sections are present. Every non-ACCEPT row has a reason. |
| Recommendation consistency | **PASS** — proposed tree contains 18 CT1 frames, 40 CT2 candidates and 84 CT3 candidates, with explicit status annotations for undecomposed branches. No duplicate recommended name or exact collision with captured established Business Enabling Capability names; local source-link file targets resolve. |
| Required architecture command | **UNRESOLVED at test level** — `timeout --signal=TERM --kill-after=10s 300s mvn -B test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` stopped after 1.809 seconds with Maven exit 1 before the architecture test module. The harness attempted to write `/home/mhunter/.m2/repository/com/sun/mail/jakarta.mail/resolver-status.properties` on a read-only filesystem. This establishes an environment failure, not failed architectural assertions; no timeout or stall occurred. Log: `/tmp/harmonia-mvp-bc-architecture-tests.log`. |
| Corrected bounded verification | **PASS** — the same selection with Maven `-o` and a 120-second timeout used cached dependencies, avoided the resolver write, and completed in 25.170 seconds (exit 0). **90 tests, 0 failures, 0 errors, 0 skipped**, including all eight AGENTS.md-named architecture classes and three additional selected architecture classes. Log: `/tmp/harmonia-mvp-bc-architecture-tests-offline.log`; Surefire XML is under `paradeigma/paradeigma-test/target/surefire-reports/`. No unchanged stalled command was rerun. |
| Repository preservation | **PASS** — final tracked diff is empty; working-tree status shows only this added report. Canonical sources/capture, source fingerprints, identifiers and implementation have no changes. |

Work stops with this report. Canonical Markdown, RADS JSON/schema, identifiers, Features, Functions, Services, Processes, catalogue relationships, applications and implementation remain unchanged. Await architectural adjudication before any subsequent reconciliation.
