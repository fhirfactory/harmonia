# Canonical Business Capability Model (18 CT1 Domains)

## Overview & Purpose

Business Capabilities describe **what the health enterprise/business must be
capable of doing**. They are organisational abilities independent of technology,
products, departments, professions or automation. This catalogue is the canonical
R1/R2 reconciliation of the adjudicated Business Capability decisions recorded in
DN-BC-001 through DN-BC-037. The scratchpad retains working reasoning only;
the definitions, explicit tier/parent assertions and boundaries here govern the
reconciled model. Provisional and unresolved matters are not promoted as approved
hierarchy.

Business Enabling Capabilities describe **reusable enabling abilities Harmonia
provides or supports**. The two hierarchies are distinct:

```text
Business Capability:          CT1 → CT2 → CT3
                                  enabled-by / supported-by
Business Enabling Capability: CT1 → CT2 → CT3 → Feature
```

The relationship is enablement/support, never decomposition or implementation
equivalence. **Business Capabilities do not decompose into Harmonia Features.**
Features are ultimately framed by Business Enabling CT3s. This catalogue
establishes no new BC↔BEC mapping and does not reframe existing Features.
Previously approved B2.1 and assurance support relationships retain their
bounded authority without being extended to the new BC descendants.

Technology-sounding names such as Health Information Exchange, Digital
Communication Channel, Identity Assurance and Access Control Enforcement
describe organisational abilities. Physical, procedural and digital mechanisms
illustrate exercise/enablement, not software-component identities. An interpretation
requiring a particular software product to explain a BC is suspect. Where BC and
BEC concern the same subject, their names must distinguish organisational ability
from system enablement where reuse would imply equivalence or ownership. The
existing Exchange name collision is explicitly unresolved below.

## R1/R2 Scope and Decomposition Rules

> Capability decomposition SHALL be sufficient to establish Harmonia's architectural context and derivation requirements. It SHALL NOT pursue exhaustive decomposition of healthcare-enterprise capabilities where additional detail does not materially affect Harmonia responsibility, information semantics, interoperability, security, workflow enablement, traceability or another Harmonia architectural concern.

> Scope may determine decomposition depth. Scope must not distort decomposition semantics.

The stopping test asks whether distinguishing another ability would materially
change how Harmonia behaves, governs, represents, exchanges, secures, coordinates
or traces something. Once a branch is decomposed, its materially distinct peers
must be coherent from the business perspective, independently of Harmonia's
implementation interest. This does not require identical depth, a Feature for
every peer or a complete healthcare-enterprise taxonomy. An undecomposed CT1/CT2
does not authorise a skipped tier or an invented CT3.

The [metamodel](../../03-business-architecture/metamodel/business-architecture-metamodel.md#11-capability-decomposition-and-terminology)
governs tier meaning. Explicit CT1/CT2/CT3 and parent statements below establish
the stated BC hierarchy; display ordinals, indentation and regional membership
alone do not. CT4 and deeper tiers are outside scope. No ordinal is an allocated
local code or Canonical ID. BEC tiers/ancestry remain separately unresolved under
K9. Absent decomposition does not deny real-world abilities or assign them to
an existing node.

## Capability Modelling Quality Rules

| Rule | Requirement |
| :--- | :--- |
| CM-R01 Coverage | Preserve the eighteen broad enterprise domains and five regions; decomposition is bounded by R1/R2 semantic sufficiency rather than exhaustive enterprise detail. |
| CM-R02 Orthogonality | Preserve distinct responsibilities, outcomes and authority even where capabilities share evidence, context or implementation machinery. |
| CM-R03 Semantic Clarity | Use clear business meanings; examples do not redefine capabilities as technology. |
| CM-R04 No Junk Drawers | Keep cohesive purposes; do not use residual categories to hide materially distinct abilities. |
| CM-R05 Global Name Uniqueness | Capability names are globally unique across the architecture. The unresolved BC/BEC Exchange collision is reported; neither identity is merged or renamed here. |

### Capability decomposition and educational documentation

Decomposition follows architectural distinction, not explanatory detail.
Definition, state change, boundary, illustrative activities, care contexts and
exclusions explain capabilities without establishing more children. History-taking,
diagnosis, prescribing, medication administration, surgery, therapy and counselling
are examples unless an explicit adjudication establishes a Capability identity.

<a id="b2-r05"></a>

### B2-R05 — Capability Is Independent of Care Delivery Context

Setting, delivery mode, temporal pattern and organisational arrangement do not
by themselves establish separate Business Capabilities.

<a id="b2-r06"></a>

### B2-R06 — Context Independence Test

If changing setting, mode or time context does not materially change the business
ability, it remains the same Capability. Acute, Planned/Scheduled and Longitudinal
Care are not Individual Care Delivery's CT2 decomposition. Inpatient, ambulatory,
community, home, virtual, emergency and elective remain illustrative contexts.

## Harmonia Relevance Taxonomy

<a id="foundational-principle-enablement-vs-ownership"></a>

| Classification | Meaning |
| :--- | :--- |
| Reference | Enterprise context without material effect on Harmonia responsibilities or processing obligations. |
| Adjacent | Harmonia consumes, represents or supplies contextual information without material participation in execution or coordination. |
| Harmonia-Relevant | Harmonia materially enables, connects, routes, coordinates or supplies information used in performing the capability. |
| Harmonia-Core | Harmonia provides substantive direct platform responsibility for the capability within its assigned boundary. |

An established classification has exactly one of these values. Unresolved is a
status, not a fifth value. CT1 relevance describes material participation across
its bounded scope; it does not classify every descendant identically or confer
enterprise-domain authority. Descendant relevance remains unallocated unless
explicitly established. Contextual resource/state consumption alone warrants
neither Core nor automatic Relevant classification. AX-18 preserves originating
and domain authority. A Harmonia-Core enabling responsibility may support a
Harmonia-Relevant BC without Harmonia owning that business activity.

## The 18 CT1 Business Capability Domains

Five natural regions group the eighteen CT1s. Regions are contextual views,
not capability ancestors, organisational boundaries or application ownership.
They are distinct from the five BEC contextual views. All eighteen named
catalogue domains below are explicitly **CT1 roots**; each branch statement
explicitly establishes its CT2 parent and each table its CT3 parent.

| Catalogue ordinal | CT1 Business Capability | Region | Relevance |
| :--- | :--- | :--- | :--- |
| 01 | Individual Care Delivery | Care & Health Delivery | Harmonia-Relevant |
| 02 | Care Access & Coordination | Care & Health Delivery | Harmonia-Relevant |
| 03 | Health Rights, Advocacy & Participation | Care & Health Delivery | Adjacent |
| 04 | Diagnostic, Therapeutic & Clinical Support Services | Care & Health Delivery | Harmonia-Relevant |
| 05 | Health Products & Clinical Technology | Care & Health Delivery | Harmonia-Relevant |
| 06 | Clinical Quality, Safety & Improvement | Care & Health Delivery | Harmonia-Relevant |
| 07 | Community Health & Wellbeing | Care & Health Delivery | Harmonia-Relevant |
| 08 | Population Health & Health-System Planning | Care & Health Delivery | Harmonia-Relevant |
| 09 | Health Information & Knowledge Management | Health Information & Digital Health | Harmonia-Core |
| 10 | Standards, Semantics & Reference Governance | Health Information & Digital Health | Harmonia-Core |
| 11 | Connected Health Services | Health Information & Digital Health | Harmonia-Core |
| 12 | Security, Privacy & Digital Trust | Health Information & Digital Health | Harmonia-Core |
| 13 | Health Research & Innovation | Research & Innovation | Harmonia-Relevant |
| 14 | Health Service Direction & Stewardship | Health Service Management | Harmonia-Relevant |
| 15 | Workforce & Organisational Capability | Health Service Management | Harmonia-Relevant |
| 16 | Corporate Resources & Enterprise Services | Health Service Management | Harmonia-Relevant |
| 17 | Health Service Governance | Governance & Assurance | Unresolved |
| 18 | Health Service Assurance | Governance & Assurance | Harmonia-Relevant |

---

### Region 1: Care & Health Delivery

#### 01. Individual Care Delivery

**Tier and ancestry:** CT1 root.

**Definition:** Assess, clinically determine, plan, intervene, monitor and review individual care independently of setting, mode, time pattern or organisational arrangement.

**Classification:** Harmonia-Relevant.

The six CT2s are Individual Health Assessment, Clinical Determination, Individual Care Planning, Clinical Intervention, Individual Health Monitoring and Individual Care Review. Their semantic state changes do not prescribe execution order.

<a id="individual-health-assessment"></a>

##### 01.01 Individual Health Assessment

**Tier and ancestry:** CT2; parent Individual Care Delivery (CT1).

**Definition:** The capability to establish an understanding of an individual's
current health state through the collection, consideration and evaluation of
relevant clinical, health and contextual information.

**State Change:** `Health information → Assessed health state / findings`

**Boundary:** Assessment establishes the assessed health state or findings.
Existing records can contribute, while direct examination, observation,
history-taking or measurement may establish information independently of Health
Information Access.

**Illustrative Activities:** History taking, physical examination, consideration
of available clinical records and measurement of current observations.

**Example Care Contexts:** An emergency presentation, a scheduled consultation
or a home visit may exercise the same assessment ability.

**Explicit Exclusions:** Establishing clinical conclusions, selecting the care
response and performing an intervention are not the assessment itself.

<a id="clinical-determination"></a>

##### 01.02 Clinical Determination

**Tier and ancestry:** CT2; parent Individual Care Delivery (CT1).

**Definition:** The capability to interpret assessed health information and
establish clinically meaningful conclusions about an individual's health state,
condition, risk or prognosis.

**State Change:** `Assessed findings → Clinically interpreted understanding`

**Boundary:** Clinical Determination interprets findings and establishes their
clinical meaning, including the clinical significance of change identified by
monitoring.

**Illustrative Activities:** Diagnosis, clinical risk interpretation and
prognostic judgement.

**Example Care Contexts:** Interpretation during acute care or a longitudinal
follow-up consultation remains the same business ability.

**Explicit Exclusions:** Clinical Determination does not include establishing
care needs or selecting the response to the determination.

<a id="individual-care-planning"></a>

##### 01.03 Individual Care Planning

**Tier and ancestry:** CT2; parent Individual Care Delivery (CT1).

**Definition:** The capability to establish the intended clinical response to an
individual's assessed and clinically interpreted health state, including the
objectives, interventions and monitoring required to progress their care.

**State Change:** `Clinically interpreted understanding → Intended course of care`

**Boundary:** Planning establishes what is intended to happen. It does not imply
that the planned care has occurred.

**Illustrative Activities:** Establishing care objectives, selecting intended
therapy, prescribing as an expression of clinical intent and planning monitoring.

**Example Care Contexts:** An intended course of care may be established during
an inpatient encounter, an ambulatory consultation or a virtual consultation.

**Explicit Exclusions:** Performing planned care is Clinical Intervention.
Ordering, requesting, scheduling and allocating care do not themselves establish
that an intervention has occurred. Operational service/resource availability
information is not Health Information Access merely because it informs planning.

<a id="clinical-intervention"></a>

##### 01.04 Clinical Intervention

**Tier and ancestry:** CT2; parent Individual Care Delivery (CT1).

**Definition:** The capability to perform clinical actions intended to alter,
maintain or otherwise manage an individual's health state in accordance with an
established care intent.

**State Change:** `Intended care → Care action performed`

**Boundary:** Clinical Intervention establishes that a care action was performed.
An intervention need not produce a successful clinical outcome. Access to an
intended intervention does not confer authority to perform it.

**Illustrative Activities:** Medication administration, surgery, therapy and
counselling when performed as clinical actions under an established care intent.

**Example Care Contexts:** An intervention may be performed in inpatient,
community or home care, or through a suitable virtual delivery mode.

**Explicit Exclusions:** Ordering, requesting, scheduling or allocating an
intervention is not itself the Clinical Intervention; selecting the intended
response belongs to planning.

<a id="individual-health-monitoring"></a>

##### 01.05 Individual Health Monitoring

**Tier and ancestry:** CT2; parent Individual Care Delivery (CT1).

**Definition:** The capability to observe and evaluate an individual's health
state over time in order to identify clinically meaningful change, stability or
deviation from an expected state or trajectory.

**State Change:** `Observed health states over time → Change / stability / deviation identified`

**Boundary:** Monitoring establishes change, stability or deviation.
Interpretation of the clinical significance of that change remains Clinical
Determination.

**Illustrative Activities:** Comparing serial observations with a baseline or
expected trajectory and identifying a trend or deviation.

**Example Care Contexts:** Serial inpatient observations and longitudinal home
monitoring exercise the same ability across different time patterns and settings.

**Explicit Exclusions:** Clinical interpretation of an identified change and
selection of the care response are not monitoring. Evaluating care effectiveness
against its objectives belongs to Individual Care Review.

<a id="individual-care-review"></a>

##### 01.06 Individual Care Review

**Tier and ancestry:** CT2; parent Individual Care Delivery (CT1).

**Definition:** The capability to evaluate the progress and effectiveness of an
individual's care against its intended objectives and establish the continuing
appropriateness of the current care intent.

**State Change:** `Care progress / outcomes → Effectiveness and continuing appropriateness established`

**Boundary:** Care Review evaluates the care itself against its intent and
objectives; it is not simply another name for reassessment.

**Illustrative Activities:** Reviewing response to delivered therapy against care
objectives and evaluating whether the current care intent remains appropriate.

**Example Care Contexts:** Review during an admission, a scheduled follow-up or
longitudinal care retains the same evaluation purpose.

**Explicit Exclusions:** Reassessment alone does not establish care effectiveness.
Establishing a revised intended course of care belongs to Individual Care Planning.


Clinical Determination and Individual Care Planning have no adjudicated CT3 set in the supplied decisions. Clinical Intervention’s Therapy & Counselling Intervention / Medication Intervention / Procedural Intervention candidate remains provisional under DN-BC-010; it is not promoted as a final taxonomy. Examples cannot close these gaps.

The previously approved [B2.1 support trace](../../05-information-systems-architecture/traceability/b2-1-individual-care-delivery-health-information-access.md) is preserved without extending it to CT3s or establishing runtime dependencies.

##### CT2 — Individual Health Assessment

**Tier and ancestry:** CT2; parent Individual Care Delivery (CT1).

**Definition:** Establish an understanding of an individual’s current health state.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Individual Health Assessment; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Health History Assessment | Establish relevant health knowledge from the individual’s history and applicable historical/contextual information. |
| Clinical Examination | Establish health findings through clinical examination. |
| Health Status Measurement | Establish individual health observations or measurements. |

**Boundary:** The decomposition distinguishes means of establishing knowledge. Functional, nutritional, cognitive, mental-health and similar labels are assessment domains, not automatically peer abilities. Risk interpretation belongs to Clinical Determination. Care setting, profession, specialty and information type do not define the decomposition.

##### CT2 — Individual Health Monitoring

**Tier and ancestry:** CT2; parent Individual Care Delivery (CT1).

**Definition:** Observe health over time to identify change, stability or deviation.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Individual Health Monitoring; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Health State Monitoring | Establish an aggregate, temporally situated view of the individual’s health from applicable monitored data. |
| Care Response Monitoring | Scope that health-state view to a particular problem/condition and the treatment applied to it. |

**Boundary:** Care Response Monitoring consumes health-state services provided through Health State Monitoring without independently recreating the aggregate view. This semantic consumption establishes no named Service, interface or runtime invocation. Measurement supplies observations; monitoring establishes a view; Clinical Determination interprets its clinical significance. Deterioration, improvement and stability are possible monitored states, not established peer CT3s.

##### CT2 — Individual Care Review

**Tier and ancestry:** CT2; parent Individual Care Delivery (CT1).

**Definition:** Evaluate care against intent and objectives.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Individual Care Review; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Care Appropriateness Review | Evaluate whether the current intended care remains suitable in the individual’s current context. |
| Care Effectiveness Review | Evaluate whether delivered care is achieving or has achieved its intended goals and outcomes. |

**Boundary:** An effectiveness finding may inform appropriateness without replacing it. Care may be appropriate yet ineffective, or effective yet no longer appropriate to continue. Monitoring the response is distinct from judging effectiveness; identifying a need to reconsider care does not establish the replacement plan. Replanning belongs to Individual Care Planning.

#### 02. Care Access & Coordination

**Tier and ancestry:** CT1 root.

**Definition:** Managing patient intake, referral triage, appointment scheduling, multidisciplinary care team coordination, cross-facility patient transfers, and care continuity across organisational boundaries.

**Classification:** Harmonia-Relevant.

##### CT2 — Care Access Management

**Tier and ancestry:** CT2; parent Care Access & Coordination (CT1).

**Definition:** Establish and manage access to applicable care.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Care Access Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Care Intake Management | Establish and manage the intake context through which an individual seeks or enters care. |
| Care Referral Management | Manage a request for another participant/service to assess, manage or assume applicable care responsibility. |
| Care Eligibility Determination | Determine whether applicable eligibility conditions for care access are satisfied. |

**Boundary:** Intake, referral and eligibility are distinct abilities. Referral submission or receipt does not establish acceptance, care delivery or responsibility actually assumed; eligibility does not itself allocate or deliver care.

##### CT2 — Care Scheduling & Capacity Management

**Tier and ancestry:** CT2; parent Care Access & Coordination (CT1).

**Definition:** Manage scheduled opportunities, care places, usable operational capacity and care-demand resourcing.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Care Scheduling & Capacity Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Care Appointment Management | Establish and manage a scheduled opportunity for care. |
| Care-Place Allocation | Allocate an applicable care place to a care requirement. |
| Care-Place Readiness Management | Establish and manage whether a particular care place is ready for allocation or use. |
| Care Resource Deployment | Apply legitimately available and appropriately authorised resources to a current or planned operational care requirement, particularly using roster/on-call and service-coverage context. |
| Operational Care Flow Coordination | Coordinate operational care flow and its progression within the care-delivery environment. |
| Operational Care Capacity Management | Understand and manage the service’s usable capacity to accommodate care in its current operational context. |

**Boundary:** Available staff, equipment or an unoccupied bed does not establish usable service capacity. Available for service coverage ≠ deployed to a care requirement ≠ assigned operational work. Care Resource Deployment consumes availability, role, qualification, engagement and privilege context without changing authoritative rosters, qualifications, employment or privilege records. It establishes no Harmonia authority over clinical work, enterprise assets or hospital-wide logistics. Operational capacity is distinct from strategic/tactical capacity planning and from collective service readiness (OI-BC-002).

##### CT2 — Care Coordination & Transition

**Tier and ancestry:** CT2; parent Care Access & Coordination (CT1).

**Definition:** Coordinate care participation, actual responsibility transition and discharge.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Care Coordination & Transition; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Care-Team Coordination | Coordinate participating care-team context and collaboration around care. |
| Care Responsibility Transition | Establish and coordinate who has actually assumed applicable care responsibility. |
| Care Discharge Coordination | Coordinate applicable discharge requirements and progression. |

**Boundary:** Patient moved ≠ care responsibility transferred. Arrival, referral submission, message receipt, discharge readiness or physical departure does not itself establish acceptance of responsibility. Information assembly for clinical handover does not confer clinical-handover management authority on Harmonia.

##### CT2 — Care Support Logistics Management

**Tier and ancestry:** CT2; parent Care Access & Coordination (CT1).

**Definition:** Coordinate bounded operational support needed for care access and fulfilment.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Care Support Logistics Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Patient Movement Coordination | Coordinate physical patient transit and applicable mobility constraints. |
| Clinical Material Logistics Coordination | Coordinate clinically relevant material movement, custody, availability or disposition needed for care or diagnostic fulfilment. |
| Care Support Work Coordination | Coordinate authorised operational support work: what needs doing, who performs it and how progression, completion or escalation is coordinated. |

**Boundary:** These abilities do not establish a general hospital logistics, supply-chain, ERP, facilities or workforce-management architecture. Support work coordination does not determine clinical work; assignment/dispatch does not establish availability, qualification, privilege or clinical authority.

#### 03. Health Rights, Advocacy & Participation

**Tier and ancestry:** CT1 root.

**Definition:** Protecting patient rights, facilitating informed consent, managing advance care directives, supporting patient advocacy, and enabling consumer participation in health decisions.

**Classification:** Adjacent.

No CT2/CT3 set is established by this reconciliation. Consent and record-access evidence can support rights without assigning advocacy or participation management to Harmonia.

#### 04. Diagnostic, Therapeutic & Clinical Support Services

**Tier and ancestry:** CT1 root.

**Definition:** The specialised clinical services that support direct patient care, including pathology and laboratory testing, diagnostic imaging (radiology), pharmacy and medication dispensing, and allied health therapies.

**Classification:** Harmonia-Relevant.

The following diagnostic and pharmacy branches are established. Other therapeutic/support scope remains enterprise context; no further subordinate identity is manufactured.

##### CT2 — Diagnostic Support Provision

**Tier and ancestry:** CT2; parent Diagnostic, Therapeutic & Clinical Support Services (CT1).

**Definition:** Provide diagnostic services and manage their evidence, interpretation and governed reporting.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Diagnostic Support Provision; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Diagnostic Service Appointment Management | Manage the diagnostic-service-specific scheduled opportunity, including preparation, modality, urgency, duration and other service-specific constraints. |
| Diagnostic Service Delivery | Perform the diagnostic activity that obtains or produces diagnostic evidence. |
| Diagnostic Specimen Management | Maintain a specimen’s identity, integrity, suitability, custody and applicable lifecycle context. |
| Diagnostic Data Management | Manage acquired or produced diagnostic evidence as information, including identity, association, integrity, organisation, availability and lifecycle. |
| Diagnostic Interpretation | Establish what diagnostic evidence demonstrates within the diagnostic service’s professional scope. |
| Diagnostic Report Management | Establish and govern the authoritative expression of diagnostic findings, including applicable creation, review, amendment, verification, finalisation and authorisation/signing. |
| Diagnostic Report Distribution | Provide an established report to applicable authorised recipients or consuming contexts under applicable access, privacy, security and communication controls. |

**Boundary:** Scheduling ≠ performance ≠ evidence management ≠ interpretation ≠ governed report ≠ distribution. Specimen and data responsibilities remain distinct. Diagnostic interpretation does not establish patient-level Clinical Determination from the total clinical context. Generic appointments are distinct from diagnostic-specific constraints. Diagnostic Order Management is not established here as a subordinate capability. Signing/finalisation are report behaviours, not additional CT3s. Services may exercise a subset, concurrently or iteratively; no mandatory diagnostic workflow is established.

##### CT2 — Medication & Pharmacy Services

**Tier and ancestry:** CT2; parent Diagnostic, Therapeutic & Clinical Support Services (CT1).

**Definition:** Provide professional medication/pharmacy services under applicable controls.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Medication & Pharmacy Services; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Prescription Medication Dispensing | Professionally prepare and provide medication according to an applicable authorised prescription/order and dispensing requirements. |
| Non-Prescription Medication Provision | Provide medication not requiring an applicable prescription, subject to professional, regulatory and safety controls. |
| Medication Advice | Provide professional medication-specific advice about appropriate use, precautions, interactions, administration, adherence or safe handling. |
| Medication Dose Administration Aid Provision | Prepare and provide organised medication doses supporting administration according to an already established regimen. |

**Boundary:** Medication prescribed ≠ dispensed ≠ provided ≠ administered. Advice does not establish diagnosis, prescribing or authority to alter treatment. Dose aid provision does not establish the regimen; commercial packaging examples do not define the capability. These are pharmacy responsibilities, distinct from clinical intervention, and do not prescribe a medication sequence. Medication Advice’s detailed scope remains subject to refinement. OI-BC-001 remains unresolved; compounding/preparation is neither established, rejected nor silently subsumed.

#### 05. Health Products & Clinical Technology

**Tier and ancestry:** CT1 root.

**Definition:** Manage health products and clinical technology in the care environment, with distinct device inventory, functional/release, deployment, utilisation, integration and lifecycle responsibilities. Product procurement and supply context is retained without inferring further Health Product Management decomposition.

**Classification:** Harmonia-Relevant.

##### CT2 — Clinical Technology Management

**Tier and ancestry:** CT2; parent Health Products & Clinical Technology (CT1).

**Definition:** Manage clinical technology as a coherent business concern rather than only the device information Harmonia happens to use.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Clinical Technology Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Clinical Device Inventory Management | Establish what devices exist and their managed asset identity, specification, ownership/custody and inventory context. |
| Clinical Device Feature & Release Management | Manage functional/release state, including applicable software, firmware, configuration and enabled device characteristics. |
| Clinical Device Loan & Deployment Management | Manage device loan, issue, assignment, placement, return and deployment. |
| Clinical Device Utilisation Support | Enable and support otherwise appropriately authorised users to use the particular device safely and effectively. |
| Clinical Device Integration Provision | Establish and maintain the device’s capability and configuration to participate in applicable information exchanges. |
| Clinical Device Data Integration | Acquire, exchange or convey device data through established integration capability. |
| Clinical Device Lifecycle Management | Govern introduction, continued service, review, replacement, withdrawal, decommissioning and retirement within the overall device estate. |

**Boundary:** Inventory ≠ release state ≠ deployment ≠ user enablement ≠ use in care. Professional qualification/privilege does not establish device-specific enablement. Integration provisioned ≠ data integrated; neither confers clinical interpretation or downstream information authority. Device monitoring remains externally provided enablement, with provider unestablished. Monitoring, maintenance and lifecycle disposition have distinct meanings. The Maintenance membership discrepancy is preserved under Unresolved Business Capability matters; no eighth CT3 is inferred.

#### 06. Clinical Quality, Safety & Improvement

**Tier and ancestry:** CT1 root.

**Definition:** Operational clinical quality, safety and improvement: monitoring clinical outcomes; identifying quality and safety concerns; adverse event and incident management, reporting and response; infection control; operational quality and safety monitoring; continuous quality improvement; and coordination of operational responses to identified concerns.

**Classification:** Harmonia-Relevant.

##### CT2 — Clinical Safety Management

**Tier and ancestry:** CT2; parent Clinical Quality, Safety & Improvement (CT1).

**Definition:** Manage the clinical-risk distinctions materially required for R1/R2.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Clinical Safety Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Clinical Risk Identification | Recognise and characterise a clinical safety risk from applicable evidence. |
| Clinical Risk Mitigation Management | Manage prospective/preventative measures reducing identified risk likelihood, exposure or consequence. |
| Clinical Risk Response Management | Manage active response, containment and remediation where risk has materialised, is emerging or requires active intervention. |

**Boundary:** Monitoring supplies evidence but does not itself identify/characterise clinical risk. Monitoring providers remain unestablished. Mitigation/response may require action by other capabilities without acquiring those capabilities’ ownership. Response is not a generic enterprise incident-management container. The three CT3s are deliberately non-exhaustive specialist context; identified, mitigated and actively responded-to states are distinct, not a mandatory sequence.

##### CT2 — Clinical Quality Management

**Tier and ancestry:** CT2; parent Clinical Quality, Safety & Improvement (CT1).

**Definition:** Manage the clinical-quality distinctions materially required for R1/R2.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Clinical Quality Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Clinical Quality Criteria Management | Establish and manage measures, indicators, expectations and criteria against which clinical quality is considered. |
| Clinical Quality Evidence Management | Manage the body of applicable evidence used for clinical-quality consideration without owning its originating activity. |
| Clinical Quality Review & Assessment Management | Review evidence against applicable criteria and establish a clinical-quality assessment, finding or conclusion. |
| Clinical Quality Reporting | Communicate established clinical-quality measures, assessments, findings and conclusions to authorised consumers. |

**Boundary:** Criteria ≠ evidence ≠ assessment ≠ reporting ≠ subsequent improvement. Observation alone is not quality judgement and reporting does not originate the reported conclusion. Clinical Quality Performance Monitoring is not established as a separate CT3; the provider of monitoring evidence remains unestablished. Continuous Improvement is not a subordinate CT3 here; generic placement remains unestablished. Health Service Continuous Improvement has the separately established service-wide purpose under 14. The four CT3s do not exhaust specialist Clinical Quality.

#### 07. Community Health & Wellbeing

**Tier and ancestry:** CT1 root.

**Definition:** Support community health and wellbeing, including the bounded preventative-health abilities required for R1/R2. Wider environmental health, outreach and social determinants remain enterprise context without inferred additional decomposition.

**Classification:** Harmonia-Relevant.

##### CT2 — Preventative Health

**Tier and ancestry:** CT2; parent Community Health & Wellbeing (CT1).

**Definition:** Address preventative-health needs through organised activity with sufficient R1/R2 distinctions.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Preventative Health; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Preventative Health Problem Identification | Identify and characterise a preventative-health problem warranting organised attention. |
| Preventative Health Programme Management | Establish and manage an organised response to a preventative-health need/objective without absorbing its delivery capabilities. |
| Preventative Health Education Services | Provide education intended to prevent adverse health outcomes and influence preventative knowledge or behaviour. |
| Preventative Health Programme Monitoring | Observe programme operation, reach, participation, coverage, progress and applicable performance. |
| Preventative Health Reporting | Report established preventative-health problems, programmes, cohorts, activity, performance and outcomes. |
| Preventative Health Cohort Management | Establish and manage the target population, inclusion/exclusion criteria, membership, stratification and applicable entry/exit context. |

**Boundary:** Screening and immunisation are programme types, not automatically separate CT3s. Population at risk ≠ defined cohort ≠ individual clinical problem. Programme management does not own clinical intervention, appointments or other delivery abilities. Education is purpose-specific rather than generic communication. Programme monitoring is distinct from population-health assessment, individual monitoring and assurance/review conclusions. This is deliberate specialist under-decomposition; unmodelled responsibilities are not assigned to existing CT3s.

#### 08. Population Health & Health-System Planning

**Tier and ancestry:** CT1 root.

**Definition:** Understand population health through assessment/surveillance and applicable event/notification management; assess health-service demand and plan required capacity and its distribution.

**Classification:** Harmonia-Relevant.

##### CT2 — Population Health Surveillance

**Tier and ancestry:** CT2; parent Population Health & Health-System Planning (CT1).

**Definition:** Understand population-health state and manage organised surveillance, significant events and applicable notification.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Population Health Surveillance; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Population Health Longitudinal Assessment | Establish population-health state, patterns and change over time from applicable evidence. |
| Population Health Surveillance Management | Establish and manage surveillance purposes, populations, concerns and regimes. |
| Population Health Event Identification | Identify and characterise significant population-health events/patterns from applicable evidence. |
| Notifiable Health Event Management | Manage formal notification applicability, required information, recipient, status, submission, correction/supplementation and applicable follow-up. |
| Population Health Reporting | Communicate established population-health assessments, surveillance, events, trends and findings. |

**Boundary:** Population state ≠ individual state; observed change ≠ identified event ≠ notifiability ≠ obligation discharged. Notification management neither defines the obligation nor owns operational response, and is distinct from general reporting. Surveillance may provide evidence for preventative problems/cohorts, demand planning, clinical quality or safety without establishing those outcomes. Population assessment differs from preventative-programme performance. Individual observations may contribute population evidence without becoming population conclusions. Epidemiology, outbreak response and public-health scope are deliberately not exhaustively modelled.

##### CT2 — Health-System Planning

**Tier and ancestry:** CT2; parent Population Health & Health-System Planning (CT1).

**Definition:** Assess health-service demand and plan the capacity and footprint required to meet it.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Health-System Planning; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Health Service Demand Assessment | Assess current/expected health-service demand across a population, geography, service domain or health-system context. |
| Health Service Capacity Planning | Determine required strategic/tactical capacity to address assessed demand. |
| Health Service Distribution Planning | Determine the distribution of planned capacity across a geographical, organisational, site or service-network footprint. |

**Boundary:** Population-health evidence does not predetermine a demand conclusion. Individual care demand differs from health-system demand; required capacity differs from established or operationally usable capacity. Planning owns neither underlying resources nor operational allocation/deployment. Planned footprint differs from managed locations and identifiable offerings. Detailed commissioning, funding, capital, infrastructure and workforce planning remain intentionally unmodelled.

---

### Region 2: Health Information & Digital Health

#### 09. Health Information & Knowledge Management

**Tier and ancestry:** CT1 root.

**Definition:** Manage governed health-information content, structures, lifecycle, catalogues, compartments, correlation and retrieval, alongside reusable clinical knowledge and its governed standing.

**Classification:** Harmonia-Core.

##### CT2 — Health Information Management

**Tier and ancestry:** CT2; parent Health Information & Knowledge Management (CT1).

**Definition:** Manage health information as governed, semantically meaningful content.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Health Information Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Health Information Data Management | Manage actual health-information content within applicable governed structures. |
| Health Information Structure Management | Manage structures, element/reference definitions, constraints and semantic bindings giving content unambiguous meaning. |
| Health Information Lifecycle Management | Govern state, amendment, correction, supersession, currency, retention and disposition while preserving provenance and historical meaning. |
| Health Information Catalogue and Index Management | Manage descriptive catalogues/indexes through which information existence and location can be established. |
| Health Information Compartmentalisation | Manage contextual association, partitioning and bounded information groupings. |
| Health Information Data Correlation | Establish meaningful relationships between independently managed information items/sets. |
| Health Information Data Retrieval | Obtain identified information under an applicable authorised request/context. |

**Boundary:** Semantic authority ≠ semantic binding ≠ content; a persisted value is not self-describing health information. Structure/binding management does not govern the referenced terminology/value set. Catalogue existence/location ≠ retrieval ≠ communication ≠ presentation; discoverability does not authorise access. Compartment membership may inform but does not determine authorisation. Correlation establishes neither clinical interpretation nor entity identity. Current state and immutable historical operational truth remain distinct; managing information does not originate its facts. Structures are not equated with FHIR resources, physical schemas or storage mechanisms.

##### CT2 — Clinical Knowledge Management

**Tier and ancestry:** CT2; parent Health Information & Knowledge Management (CT1).

**Definition:** Identify, extract, formulate, associate, govern and share reusable clinical knowledge.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Clinical Knowledge Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Clinical Knowledge Context Identification | Identify sources/contexts potentially containing reusable clinical knowledge. |
| Clinical Knowledge Context Extraction | Extract relevant material while preserving its source/context relationship. |
| Clinical Knowledge Information Formulation | Formulate explicit reusable knowledge, including applicability, qualifications, rationale and evidence relationships. |
| Clinical Knowledge Concept Association | Associate knowledge with applicable governed clinical concepts without acquiring concept authority. |
| Clinical Knowledge Review & Approval Management | Manage review, endorsement, approval/rejection, conditions and applicable history for intended governed use. |
| Clinical Knowledge Publication & Sharing | Make approved knowledge available to applicable authorised consumers and use contexts. |

**Boundary:** Source identified ≠ extracted material ≠ formulated knowledge ≠ reviewed/approved knowledge ≠ publication ≠ individual application. Reusable knowledge differs from source health information and individual clinical facts. Formulation is distinct from Health Information Structure Management and does not establish approval. Concept association does not redefine terminology or semantic authority. Publication establishes neither individual applicability nor a clinical conclusion/decision. Clinical knowledge and source provenance/authority remain distinguishable; no mandatory knowledge workflow is prescribed.

#### 10. Standards, Semantics & Reference Governance

**Tier and ancestry:** CT1 root.

**Definition:** Govern organisational adoption, oversight and appropriate use of health-information models, standards, terminology, classifications and semantic mappings.

**Classification:** Harmonia-Core.

##### CT2 — Health Information Standards Governance

**Tier and ancestry:** CT2; parent Standards, Semantics & Reference Governance (CT1).

**Definition:** Govern organisational adoption and use of health-information models and applicable standards.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Health Information Standards Governance; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Information Model Discovery | Identify information models relevant to the organisation’s health-information needs. |
| Information Model Adoption | Adopt applicable information models, including intrinsic assessment of adoption impacts. |
| Information Model Education | Support organisational understanding and appropriate use of adopted information models. |
| Information Model Compliance | Address organisational conformity to applicable information-model requirements. |
| Information Model Governance Oversight | Oversee and evaluate effectiveness, consistency and appropriate operation of information-model governance. |
| Standards Adoption | Adopt applicable standards within the organisation. |
| Standards Impact Identification | Identify standards impacts across clinical/work practice, permitted medications, procedures, policy, safety, obligations and reporting as applicable. |

**Boundary:** Oversight does not perform its discovery, adoption, education or compliance peers. Model-adoption impact is intrinsic to adoption; standards impacts can extend beyond information-system implementation and warrant their distinct established identity. These are the peers explicitly named in DN-BC-025; no additional standards peer is inferred to fill out a conventional taxonomy. Conceptual progression does not establish a workflow.

##### CT2 — Clinical Semantics Governance

**Tier and ancestry:** CT2; parent Standards, Semantics & Reference Governance (CT1).

**Definition:** Govern clinical meaning and appropriate organisational use of semantic artefacts.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Clinical Semantics Governance; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Terminology Governance | Manage governed terminology/reference-set lifecycle and standing, including acceptance, release, update, supersession/retirement and authority/provider coordination. |
| Terminology Utilisation Oversight | Oversee actual terminology/reference-set use and identify inappropriate, inconsistent or drifting usage. |
| Terminology Localisation | Manage controlled local responses where adopted terminology does not meet legitimate organisational semantic requirements. |
| Classification Governance | Govern applicable classification definitions and their organisational standing. |
| Classification Utilisation Oversight | Oversee appropriate organisational application of classifications. |
| Semantic Mapping Governance | Govern mappings between applicable semantic artefacts without collapsing their source meanings or authorities. |

**Boundary:** Compartmentalisation concerns business/context relationships; classification concerns sensitivity and consequences of exposure/mishandling; terminology concerns consistent meaning. They are orthogonal even where metadata/mechanisms are shared. Classification Localisation is not established and SHALL NOT be invented for symmetry. Governance differs from structure binding and knowledge-to-concept association.

#### 11. Connected Health Services

**Tier and ancestry:** CT1 root.

**Definition:** Exchange health information appropriately and provide digitally mediated communication, notifications and collaborative interaction between health-service participants.

**Classification:** Harmonia-Core.

##### CT2 — Health Information Exchange

**Tier and ancestry:** CT2; parent Connected Health Services (CT1).

**Definition:** Exchange health information appropriately between participating parties, systems and services.

No CT3 decomposition is established here.

**Boundary:** This organisational ability is not a FHIR server, interface engine, protocol or interoperability product. The established rename from Health Information Connectivity does not allocate additional CT3s. Health Information Sharing Programme Management is a named working capability in DN-BC-027 whose precise tier/parent is not supplied there; it is retained separately as unresolved. The collision with the existing BEC name remains for explicit adjudication; no BEC identity is merged or renamed.

##### CT2 — Digital Communication Channel

**Tier and ancestry:** CT2; parent Connected Health Services (CT1).

**Definition:** Provide digitally mediated communication, notification and collaborative interaction between patients, practitioners and other health-service participants.

No CT3 decomposition is established here.

**Boundary:** Appointment notifications, pharmacy queries, results and collaboration illustrate participant communication. This is not the general UI/presentation capability: exercising another capability through a UI does not itself exercise this communication ability. General presentation enablement remains parked for BEC reconciliation. No CT3 set is established by DN-BC-027.

#### 12. Security, Privacy & Digital Trust

**Tier and ancestry:** CT1 root.

**Definition:** Establish adequate identity confidence, coherent access controls, privacy/protection safeguards, governed activity attribution and accountability for security-relevant activity and information use.

**Classification:** Harmonia-Core.

##### CT2 — Digital Identity & Access Governance

**Tier and ancestry:** CT2; parent Security, Privacy & Digital Trust (CT1).

**Definition:** Govern sufficient identity confidence and coherent access controls across the organisation.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Digital Identity & Access Governance; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Identity Assurance | Establish sufficient confidence that a person is who they claim to be through appropriate evidence and processes. |
| Identity Modelling & Interoperability | Establish identity models and interoperability arrangements appropriate to organisational identity use. |
| Identity Qualification Control | Determine whether presented identity characteristics, provenance and assurance quality are sufficient for its intended context. |
| Identity Identification & Authentication | Identify an actor and authenticate the asserted identity. |
| Access Control Context Management | Determine what information, functions, activities or other concerns require control, under what circumstances and how requirements are modelled. |
| Access Control Assurance & Reporting | Establish organisational assurance/evidence of whether access-control arrangements are appropriately established and operating. |
| Access Control Collaboration | Coordinate arrangements across independently controlled domains/parties while retaining their control ownership. |
| Access Control Enforcement | Ensure physical, procedural and digital control mechanisms coherently impose intended controls. |

**Boundary:** Who are you? ≠ confidence in the claimed identity ≠ identity quality sufficient here. Federation and Single Sign-On are examples, not equivalent concepts or universal identity-quality guarantees. Context management is not merely runtime attribute assembly; enforcement is not a software authorisation engine. Strong local control does not imply end-to-end control. Identity Assurance is an organisational ability; the possible future BEC name Identity Assurance Representation remains unestablished.

##### CT2 — Health Information Protection

**Tier and ancestry:** CT2; parent Security, Privacy & Digital Trust (CT1).

**Definition:** Govern privacy consequences and adequate protection of the information estate and its environment.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Health Information Protection; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Health Privacy Safeguard Management | Establish organisational safeguards and assurance processes concerning privacy consequences of information supply, exposure or use. |
| Health Information Safeguarding | Ensure information and its operational environment are protected and resilient against loss, compromise, inappropriate access or operational failure. |
| Health Information Access Monitoring | Evaluate actual organisational access/handling practice, including controls, accessibility, compartments, classification and physical/printed handling. |
| Health Information Compartment Assurance | Assure appropriate establishment and application of compartmentalisation without establishing access decisions. |
| Health Information Classification Assurance | Assure appropriate classification according to sensitivity and potential consequences of exposure/mishandling. |

**Boundary:** Privacy consequences differ from estate/environment protection. Storage, backups, disaster recovery, physical controls and testing illustrate organisational safeguards rather than define software components. Access Monitoring evaluates practice using IT, audit, physical-access or other evidence; it is not merely observation of software access events. Evidence supply does not transfer business accountability. These subject-specific protection abilities are not separate macro-level Health Service Assurance trees.

##### CT2 — Digital Accountability & Trust

**Tier and ancestry:** CT2; parent Security, Privacy & Digital Trust (CT1).

**Definition:** Establish governed attribution and accountability for activity and information use.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Digital Accountability & Trust; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Governed Activity Attribution | Preserve who participated, what was done, the attributable person/role/unit/organisation capacity and applicable permission/authority/constraint context. |
| Security & Information-Use Accountability | Evaluate whether attributed security-relevant activity/information use was appropriate within policy, obligation and authority constraints, and establish accountability. |

**Boundary:** Actor identified ≠ activity attributed ≠ activity permitted. Attribution without permission/constraint context cannot establish entitlement and is richer than simple audit logging or provenance. Accountability evaluates appropriateness rather than merely recording participation. Expected consumption by governance/assurance or other enablement establishes no structural BC↔BEC relationship.

---

### Region 3: Research & Innovation

#### 13. Health Research & Innovation

**Tier and ancestry:** CT1 root.

**Definition:** Provide existing health information for authorised research under applicable constraints and evaluate health innovation; deliberately under-decomposed for R1/R2.

**Classification:** Harmonia-Relevant.

##### CT2 — Research Information Support

**Tier and ancestry:** CT2; parent Health Research & Innovation (CT1).

**Definition:** Provide the existing health information needed for authorised research within the deliberately bounded R1/R2 responsibility.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Research Information Support; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Governed Research Information Provision | Provide existing health information for an authorised research purpose under applicable ethics, governance, privacy, consent, security and information-use constraints. |

**Boundary:** This is deliberate under-decomposition, not a claim that information provision exhausts research support. Research Assessment and Governance, Research/Trials Oversight, clinical-trial management, genomic analysis and other specialist research abilities are intentionally unmodelled unless Harmonia requirements make them material. Their absence does not assign them to information provision.

##### CT2 — Health Innovation

**Tier and ancestry:** CT2; parent Health Research & Innovation (CT1).

**Definition:** Consider health-service and clinical innovation within the R1/R2 business context.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Health Innovation; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Health Innovation Evaluation | Evaluate proposed/emerging innovation for suitability, value, implications and potential adoption. |

**Boundary:** Evaluation does not establish adoption or the clinical/operational authority to implement innovation. The single CT3 is deliberately sufficient, not an exhaustive innovation lifecycle.

---

### Region 4: Health Service Management

#### 14. Health Service Direction & Stewardship

**Tier and ancestry:** CT1 root.

**Definition:** Model needed health services and their viability requirements, support establishment/improvement and align services with the wider ecosystem of dependencies, resources, financial arrangements and organisational change.

**Classification:** Harmonia-Relevant.

##### CT2 — Health Service Modelling

**Tier and ancestry:** CT2; parent Health Service Direction & Stewardship (CT1).

**Definition:** Establish what service should exist and what it needs to become viable.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Health Service Modelling; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Health Service Need Identification | Establish an unmet health-service need requiring response. |
| Health Service Capability Identification | Identify the ordinary business abilities needed to satisfy the service need across clinical, workforce, facility, technology, information and support concerns. |
| Health Service Implementation Support | Support establishment of the framed service through applicable organisational preparation and programmes of work. |
| Health Service Continuous Improvement | Evaluate the established service and identify correction/improvement needs that may feed back into modelling and implementation support. |

**Boundary:** Capability Identification uses ordinary business meaning; it is not Harmonia CT-tier allocation. Implementation support may require recruitment, training, facility, equipment or technology change without absorbing HR, facilities, ICT or procurement responsibility. Identification, fulfilment requirements, support and review describe semantic relationships, not a prescribed workflow.

##### CT2 — Health Service Alignment

**Tier and ancestry:** CT2; parent Health Service Direction & Stewardship (CT1).

**Definition:** Position the service coherently in its wider health-service ecosystem.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Health Service Alignment; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Health Service Co-Dependency Management | Identify and manage what the service needs from others and what others need from it. |
| Health Service Resource Identification | Identify required workforce, facilities, equipment, clinical technology, ICT, administration and support resources without owning them. |
| Health Service Financial Management | Establish the service’s fit within funding, financial and charging arrangements without absorbing enterprise finance/accounting. |
| Health Service Implementation Planning | Establish and coordinate surrounding organisational/service changes needed to introduce or materially change the service. |

**Boundary:** Alignment does not establish the authoritative service catalogue; it positions the service within the environment represented by that catalogue. Modelling determines what service is needed; alignment determines how it fits; Offering Management establishes the identifiable offering. Expected future Service Catalogue richness explains architectural relevance, but establishes neither a BEC identity/hierarchy nor a BC↔BEC mapping. Alignment does not resolve operational-readiness ancestry.

#### 15. Workforce & Organisational Capability

**Tier and ancestry:** CT1 root.

**Definition:** Govern practitioner qualification, engagement/privilege and enterprise service coverage, together with organisation identity/structure, recognised locations and identifiable healthcare service offerings.

**Classification:** Harmonia-Relevant.

Workforce & Organisational Capability remains the established CT1. Health Workforce Management and Healthcare Organisation & Service Structure Management are its distinct CT2 frames; directories represent their facts without replacing business governance of those facts.

##### CT2 — Health Workforce Management

**Tier and ancestry:** CT2; parent Workforce & Organisational Capability (CT1).

**Definition:** Govern practitioners’ professional standing, engagement/privilege and whole-enterprise service coverage.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Health Workforce Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Practitioner Qualification Management | Govern professional/clinical standing and qualifications, including applicable review, escalation, complaints, penalties/issues, suspension and revocation. |
| Practitioner Engagement & Privilege Management | Manage practitioners as service participants, their roles/capacities, applicable privileges and qualification context above ordinary HR employment administration. |
| Workforce Service Coverage Management | Observe whether required enterprise work/service obligations can be fulfilled, identify coverage gaps/risks and adjust coverage in response. |

**Boundary:** Qualified ≠ engaged ≠ privileged for a particular activity. Coverage is broader than rostering; it asks whether required work can be covered and how coverage should change. Care Resource Deployment applies available resources to a particular care requirement; Care Support Work Coordination progresses particular support work. Neither consumption of qualification/coverage information nor directory representation transfers the underlying workforce authority to Harmonia.

##### CT2 — Healthcare Organisation & Service Structure Management

**Tier and ancestry:** CT2; parent Workforce & Organisational Capability (CT1).

**Definition:** Manage organisational identity/structure, recognised locations and identifiable organisational service offerings.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Healthcare Organisation & Service Structure Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Healthcare Organisation Identity & Structure Management | Establish and manage identity and structural relationships of participating organisations and organisational units. |
| Healthcare Location Definition Management | Establish and manage recognised locations and their applicable business characteristics and relationships. |
| Healthcare Service Offering Management | Establish/manage what identifiable service is offered, by whom, where, when and under which availability conditions/constraints. |

**Boundary:** Location definition is distinct from Facilities Management. Service exists ≠ offered here ≠ available at this time/context. Temporal/conditional availability is material: a walk-in offering may operate 06:00–22:00 while another pathway covers other hours. Offering availability does not establish collective operational readiness; OI-BC-002 remains unplaced. Organisation, location and practitioner associations do not substitute for material Healthcare Service context.

#### 16. Corporate Resources & Enterprise Services

**Tier and ancestry:** CT1 root.

**Definition:** Provide the bounded clinical-activity charging information and enterprise support context required for R1/R2, without assigning enterprise finance, HR, facilities or ICT operating authority to Harmonia.

**Classification:** Harmonia-Relevant.

##### CT2 — Financial & Commercial Services

**Tier and ancestry:** CT2; parent Corporate Resources & Enterprise Services (CT1).

**Definition:** Provide the financial/commercial business context materially required for R1/R2.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Financial & Commercial Services; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Clinical Activity Charging Information Provision | Identify and provide information about chargeable or cost-relevant clinical/health-service activity where required for applicable funding, financial or charging purposes. |

**Boundary:** The single CT3 is deliberately narrow. Referral forwarding, diagnostic requests, service types, quantities and utilisation context can supply evidence; not every mediated activity is chargeable. Evidence provision does not confer pricing, billing, claims, accounting or revenue-management responsibility. Comprehensive Medicare, insurance, activity-based funding and revenue-cycle architecture is not established.

##### CT2 — Corporate Support Services

**Tier and ancestry:** CT2; parent Corporate Resources & Enterprise Services (CT1).

**Definition:** Provide enterprise support whose resources, availability, state or constraints may materially affect service activity.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Corporate Support Services; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Back-Office Services | Provide administrative/support services on which health-service activity depends. |
| Facilities Management | Manage physical facilities/services and applicable resource, location, availability and facility-state context. |
| IT Infrastructure Management | Operate/manage underlying technology infrastructure and its availability. |
| Communications & Publicity | Manage organisational communication/publication arrangements relevant to information availability/distribution. |
| People Management | Manage people across the organisation, distinct from practitioner-specific Health Workforce Management. |
| Operational Oversight | Observe broader operational conditions affecting the organisation’s ability to work or deliver services. |

**Boundary:** These are relevant enterprise abilities even where Harmonia only consumes their state/resource/constraint context; contextual consumption alone does not make them Harmonia-Core. They are not decomposed further for R1/R2 absent a material requirement. Consumption transfers neither resource ownership nor corporate operating authority and does not resolve OI-BC-002.

---

### Region 5: Governance & Assurance

#### 17. Health Service Governance

**Tier and ancestry:** CT1 root.

**Definition:** Establish and maintain the obligations, authorities, constraints, policies and governance framework under which health-service activity must operate, and define how adherence will be assessed, checked and reported.

**Classification:** Unresolved.

Health Service Governance establishes what the organisation must do and how adherence will be determined and governed. It does not determine operational means or acquire progression/management of governed work. Enabling derivation remains unestablished; consumption of its requirements alone does not settle its relevance classification.

##### CT2 — Governance Framework Management

**Tier and ancestry:** CT2; parent Health Service Governance (CT1).

**Definition:** Establish the authoritative WHAT and legitimate working parameters.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Governance Framework Management; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Policy / Obligation Governance | Identify, interpret, establish and maintain authoritative legislative, regulatory, policy, standards, guideline, contractual and other obligations, attributable and traceable to source authority. |
| Authority & Constraint Governance | Establish who may make, approve or govern organisational decisions/artefacts and the bounds within which authority may be exercised. |

**Boundary:** Obligation = what must be satisfied; authority = who may decide/approve; constraint = within what bounds. Engagement with regulators, standards/compliance bodies or other authorities can establish legitimate parameters without transferring their authority.

##### CT2 — Compliance Governance

**Tier and ancestry:** CT2; parent Health Service Governance (CT1).

**Definition:** Define how adherence to the authoritative WHAT will be determined, checked and reported.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Compliance Governance; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Compliance Assessment Definition | Define the evidence, measures, criteria, controls and assessment arrangements sufficient to evaluate compliance with established obligations, authorities and constraints. |
| Governance Reporting & Checkpoint Definition | Establish where/how adherence is checked and what governance/compliance information must be reported. |

**Boundary:** Obligation definition establishes what must be satisfied; assessment definition establishes the basis for determining satisfaction. This replaces the candidate Compliance Obligation Definition wording. Compliance Governance does not itself perform assurance or assume operational management.

#### 18. Health Service Assurance

**Tier and ancestry:** CT1 root.

**Definition:** Establish and maintain justified confidence that health services can be, and are being, delivered in accordance with applicable expectations, obligations, constraints and required levels of performance.

**Classification:** Harmonia-Relevant.

Health Service Assurance establishes whether sufficient evidence exists to give justified confidence in the organisation’s ability to deliver its health services appropriately. Its macro-level subject is that ability. Security, information, technology, process, clinical and other concerns may contribute criteria/evidence without becoming separate Business Assurance Capability trees. The independently governed evaluation already established in the canonical architecture is a required assurance approach where REQ-FND-005 applies; the reconciliation below preserves its requirements.

##### CT2 — Health Service Assurance Specification

**Tier and ancestry:** CT2; parent Health Service Assurance (CT1).

**Definition:** Specify how macro-level service-delivery assurance will be undertaken and what sufficiently supports confidence.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Health Service Assurance Specification; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Assurance Approach Definition | Define scope, evidence expectations, responsibilities, methods, applicable degree of independence and timing/frequency for service-delivery assurance. |
| Assurance Evaluation Criteria Governance | Establish and maintain criteria against which sufficient assurance can be determined. |

**Boundary:** Criteria may draw on workforce, facilities, clinical practice, information, technology, security, privacy, governance, dependencies and operational state. These contributing concerns do not create separate Business Assurance trees. An assurance approach SHALL preserve every applicable higher-level independence requirement; it cannot designate self-attestation as sufficient where REQ-FND-005 requires independent governed assurance.

##### CT2 — Assurance Evaluation

**Tier and ancestry:** CT2; parent Health Service Assurance (CT1).

**Definition:** Determine what justified confidence the applicable evidence supports.

**Decomposition assertion:** Each named child in this table is a CT3 Business Capability whose immediate CT2 parent is Assurance Evaluation; that CT2 has the CT1 parent stated above.

| CT3 Business Capability | Definition |
| :--- | :--- |
| Assurance Evidence Assessment | Evaluate available evidence against the established assurance approach and criteria. |
| Assurance Conclusion Determination | Establish the legitimately supported conclusion, including insufficiency, qualification and unresolved concerns where applicable. |

**Boundary:** Evidence assessment ≠ conclusion determination ≠ operational response. Insufficient evidence establishes neither assurance nor non-assurance. Assurance manages its own evaluation, not its subject. These BC responsibilities do not remap existing Assurance Design, Assurance Criteria Management or Governed Assurance BEC ownership.

## Assurance Independence Reconciliation

Independence is a required property of applicable assurance approaches/evaluations,
rather than the exhaustive definition of the macro-level Business Capability.
This does **not** make Harmonia's already-required independent governed assurance
optional. [REQ-FND-005](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity)
requires it for activities, information and outcomes **for which assurance is
required**. Its applicability is bounded; it neither defines every enterprise
assurance approach nor requires assurance of every activity.

The [approved Strategy derivation](../capability-maps/health-service-assurance-derivation.md)
and [Business assurance responsibilities](../../03-business-architecture/behaviours/health-service-assurance.md)
implement that obligation through independently governed progression and
conclusion authority. Assurance Approach Definition may determine applicable
methods, responsibilities and degree of independence but SHALL NOT weaken those
requirements. The subject may contribute evidence; it shall not solely determine,
suppress, manufacture or retrospectively alter its assurance outcome. Evidence
dependency is distinct from control dependency. No external assessor or physical
separation is implied.

The broader organisational capability concerns justified confidence in service
delivery. Harmonia's assigned participation remains assurance of explicitly
required facts/behaviour; it does not acquire clinical-adequacy assurance or
management of the service. Thus the old independent-evaluation definition is
preserved as the established governed-assurance obligation within the broader
capability, rather than discarded or universalised to all enterprise evaluations.
Operational uncertainty under AX-15/REQ-FND-004 remains distinct from evidence
insufficiency under REQ-FND-005.

### Clinical Services Delivery Assurance Boundary

Health Service Assurance SHALL NOT be represented as Clinical Services Delivery Assurance. The enterprise scope's reference to independent clinical audit/evaluation does not make Harmonia responsible for assuring clinical judgement, professional practice, clinical adequacy of care, or delivery of clinical services by healthcare practitioners or healthcare organisations.

Information concerning clinical service delivery MAY be the subject of governed assurance where an explicit applicable requirement establishes that assurance concern. This does not transfer responsibility for Clinical Services Delivery Assurance to Harmonia. [Capability 06 — Clinical Quality, Safety & Improvement](#06-clinical-quality-safety--improvement) retains its established clinical quality/safety responsibilities.

> **Harmonia may assure facts and behaviour concerning clinical activity where explicitly required; it does not thereby assure the clinical adequacy of care.**

### Meaning of Independent

Independent Assurance requires an independently governed assurance activity/workflow whose progression and assurance conclusion are not controlled by the activity or mechanism responsible for performing or managing the subject being assured.

Independence does not inherently require an external auditor, a different organisation, department, human actor, application, deployment, database or Kubernetes cluster, or physical infrastructure separation. This is a responsibility and authority boundary consistent with REQ-FND-005, not an Application or Technology Architecture decision about physical execution separation.

> **Evidence dependency does not compromise assurance independence; control dependency does.**

The subject being assured may supply evidence. It shall not thereby be solely responsible for determining, suppressing, manufacturing or retrospectively altering its assurance conclusion.

### Assurance / Operational Management Boundary

Health Service Assurance does not thereby assume operational management of its subject, assignment, delegation, reassignment, operational escalation, operational remediation, workflow progression, Incident Management or Incident Response. Findings, exceptions and conclusions may inform or initiate appropriate operational activity; responsibility for that activity remains with its operational capability.

> **Assurance is the QA service, not the Job Foreman.**

An assurance activity may manage its own internal assurance workflow without thereby assuming management of the subject being assured.

---

## Governance, Management, Monitoring and Assurance Boundaries

| Concern | Purpose and responsibility | Boundary |
| :--- | :--- | :--- |
| **Governance** | Establishes what must be done or must be true; applicable obligations, constraints and authority; reporting required to demonstrate compliance; and checkpoints required to determine whether obligations and constraints are satisfied. | Establishes the normative operating envelope without determining operational means. |
| **Management** | Establishes and coordinates what must be done operationally and how it will be performed within constraints; operational-state monitoring, activity progression, progress reporting, assignment/reassignment, escalation necessary for completion, response and remediation. | Owns progression of the managed activity. Being governed does not relocate activity management to Region 5. |
| **Management Monitoring** | Observation and assessment of the current state of an activity for the purpose of determining progress, completion, fulfilment or impediments, so that the containing management or workflow activity can determine what should happen next. | May cause or inform progression, pause, retry, escalation, reassignment, response or completion. Observation alone is not Independent Assurance. |
| **Independent Assurance** | Evaluation of a governed subject against applicable obligations, constraints, controls, performance expectations, quality criteria or compliance checkpoints, using sufficient trustworthy evidence to establish an assurance conclusion. | Independently governs its evaluation and conclusion; does not progress or manage its subject. |

> **Management Monitoring evaluates activity state in order to progress or manage the activity. Independent Assurance evaluates a governed subject against governing criteria in order to establish an assurance conclusion.**

### Evidence Distinctions

Evidence production does not itself perform assurance. Security, privacy, clinical, operational and information-management capabilities may produce trustworthy evidence used by Health Service Assurance without becoming assurance capabilities.

```text
Provenance ≠ Assurance
Audit information / AuditEvent ≠ Assurance
Operational monitoring ≠ Assurance
Workflow progression ≠ Assurance
Business Outcome ≠ Assurance Outcome
Workflow completion ≠ Assurance
Incident ≠ Assurance Finding
Incident Response ≠ Assurance
Risk ≠ Assurance Finding
Risk ≠ Assurance Outcome
```

### Subject-Specific Governance

> **Subject-specific governance remains with the capability responsible for that subject where governance is intrinsic to managing the subject itself.**

Health Products & Clinical Technology (05), Standards, Semantics & Reference Governance (10), Security, Privacy & Digital Trust (12), and Workforce & Organisational Capability (15) retain their existing governance semantics and regional placement. Capability 12's compliance auditing and audit-record production do not, merely by that wording, establish independent assurance. Region 5 concerns governance and independent assurance as primary responsibilities.

---


## Classification Reconciliation and Rationale

| CT1 | Previous → reconciled classification | Semantic rationale |
| :--- | :--- | :--- |
| 05 Health Products & Clinical Technology | Reference → Harmonia-Relevant | Device integration provision/data integration and service-relevant device context materially affect information exchange and care enablement. Supply-chain and maintenance authority remain external. |
| 07 Community Health & Wellbeing | Reference → Harmonia-Relevant | The established preventative programme/cohort distinctions materially frame information, notification and care enablement. Wider public-health enterprise activity is not assigned to Harmonia. |
| 08 Population Health & Health-System Planning | Adjacent → Harmonia-Relevant | Formal notification and governed population information materially support required health activity; planning remains externally governed and is not Core through context consumption. |
| 13 Health Research & Innovation | Adjacent → Harmonia-Relevant | Governed provision of existing information for an authorised research purpose is material support, not passive context or ownership of trials/research. Innovation evaluation does not acquire a separate platform assignment. |
| 14 Health Service Direction & Stewardship | Reference → Harmonia-Relevant | Service needs, viability, dependencies and alignment facts materially frame Harmonia’s service information and derivation requirements. This does not assign executive/service-management authority or establish a future BEC. |
| 15 Workforce & Organisational Capability | Harmonia-Relevant retained | Professional standing, privilege, coverage and organisational/service-offering facts materially constrain care enablement. Directory support does not make workforce/service governance Core. |
| 16 Corporate Resources & Enterprise Services | Reference → Harmonia-Relevant | Required clinical activity/utilisation evidence is material financial/charging support. Corporate support context also matters, but contextual consumption alone does not classify every support CT3 Relevant or Core. |
| 17 Health Service Governance | Unresolved retained | The normative and compliance-definition responsibilities are established, but material platform participation/enablement is not yet sufficiently assigned to distinguish Adjacent from Relevant/Core. |
| 18 Health Service Assurance | Unresolved → Harmonia-Relevant | Already-approved independent governed assurance materially supports service confidence. Harmonia does not own macro-level clinical/service delivery assurance, so the whole BC is not classified Core. |
| 01–04, 06, 09–12 | Established assignments retained | Their material enabling/direct platform responsibilities remain established; organisational definitions do not turn clinical judgement, consent authority or enterprise decisions into software responsibilities. Core is bounded to assigned platform activity. |

## Unresolved Business Capability Matters

### OI-BC-001 — Medication Compounding and Preparation

**Unresolved.** Whether compounding/specialised preparation warrants a distinct
CT3 beneath Medication & Pharmacy Services is neither established nor rejected.
Absence does not mean subsumption by Prescription Medication Dispensing,
irrelevance, exclusion or representation by another Capability. Adjudication
must preserve peer completeness and the R1/R2 materiality test.

### OI-BC-002 — Health Service Operational Readiness Placement

The identified semantic concern asks: **Can this Health Service Offering actually
be delivered from this operational context?** It establishes collective readiness
from workforce, administration, spaces, equipment/devices, materials, utilities,
ICT/communications and other dependencies without owning them. Ongoing site/service
readiness and readiness for a session are presently exercises of the same concern;
session-level exercise does not establish another Capability.

Its provisional name/identified identity and boundary are retained, but **CT2 parent,
CT1 ancestry and canonical placement remain unresolved**. No complete CT1→CT2→CT3
path is asserted. Offering defined/temporally available ≠ collectively ready;
individual resource ready ≠ service ready; care-place readiness ≠ service readiness;
readiness oversight ≠ clinical execution or supporting-resource ownership.
It SHALL NOT be placed beneath Health Service Alignment, Healthcare Service
Offering Management, Care Scheduling & Capacity Management, Corporate Support
Services, Health Product Management or another convenient parent without explicit
adjudication.

### Other Preserved Uncertainties and Canonical Conflicts

- Clinical Determination and Individual Care Planning have no established CT3
  set in the supplied decisions. Clinical Intervention’s three-child candidate
  remains provisional; DN-BC-010’s taxonomy question is not closed by the queue.
- DN-BC-015 defines **Clinical Device Maintenance Management** as maintaining or
  restoring acceptable device condition through maintenance, testing, calibration,
  inspection, fault remediation and repair, distinct from monitoring and overall
  lifecycle disposition. Its Working Decision and closing guardrail nevertheless
  specify seven CT3s omitting Maintenance. Its semantics are preserved, but its
  inclusion/tier/ancestry requires adjudication; no eighth child is manufactured.
- **Health Information Sharing Programme Management** is named by DN-BC-027’s
  spelling correction, but that note supplies no precise tier/parent assertion.
  Its name is retained without constructing ancestry or an additional definition.
- **Health Information Exchange** is the approved BC CT2 name and an existing
  BEC name. Their identities/meanings remain distinct. This conflicts with CM-R05
  and the BC/BEC naming guardrail; neither a BEC rename nor an identity merger is
  authorised here. The naming point remains stopped pending adjudication.
- Generic Continuous Improvement placement, external device/safety/quality
  monitoring providers, the possible Identity Assurance Representation BEC name
  and general UI/presentation enablement remain unestablished/parked. No standards
  or other peer is added merely for symmetry. Other undecomposed enterprise scope
  remains explicitly unmodelled.

## Semantic Relationships and Downstream Boundary

<a id="downstream-progression"></a>

Business capabilities may provide evidence, context, identified events or
established information to other business capabilities while retaining their own
outcomes and source authority. Unless explicitly required otherwise, such provision
is contextual rather than a universal dependency. **Information consumed ≠ ownership
transferred; context supplied ≠ target outcome established.** These relationships
are not decomposition, workflow order, runtime invocation, Service identity or
application dependency. Any RADS relationship must preserve the exact meaning;
where no governed type fits, preserve it as unresolved rather than substitute a
stronger convenient type.

### Established BC Evidence and Context Relationships

The following relationships preserve the adjudicated business meaning. They
do not require every source instance to produce an input for every target
instance, or make one capability perform another. A conditional relationship
remains conditional. No named Business Service or machine-readable relationship
type is allocated by this table.

| Source | Recipient | Established meaning |
| :--- | :--- | :--- |
| Population Health Surveillance Management | Population Health Longitudinal Assessment; Population Health Event Identification | Supplies the population, concern and purpose of managed surveillance. |
| Population Health Longitudinal Assessment | Population Health Event Identification | Supplies trends/change evidence that may support recognition of a significant event. |
| Population Health Event Identification | Notifiable Health Event Management | Supplies an identified event for consideration under applicable notification obligations. |
| Population Health Longitudinal Assessment; Population Health Surveillance Management; Population Health Event Identification; Notifiable Health Event Management | Population Health Reporting | Supply their established assessments, surveillance state, events and notification information for reporting. |
| Population Health Longitudinal Assessment; Population Health Event Identification | Preventative Health Problem Identification | Supply population-health evidence/identified concerns without determining the preventative response. |
| Population Health Longitudinal Assessment | Preventative Health Cohort Management | Supplies population characteristics relevant to cohort definition, stratification or review. |
| Population Health Event Identification | Preventative Health Cohort Management | May supply event context for identifying the population requiring preventative attention. |
| Individual Care Delivery | Population Health Surveillance | Individual information may contribute population evidence without becoming a population conclusion. |
| Population Health Surveillance | Individual Care Delivery | Population findings may inform individual care without establishing an Individual Clinical Determination. |
| Population Health Surveillance | Clinical Quality Management; Clinical Safety Management | May supply evidence relevant to their distinct assessments or risk identification; their information may also contribute population evidence. |
| Population Health Longitudinal Assessment; Population Health Event Identification | Health Service Demand Assessment | Supply population state/change or identified-event evidence relevant to expected service demand. |
| Health Service Demand Assessment | Health Service Capacity Planning | Supplies assessed demand evidence relevant to required capacity. |
| Health Service Capacity Planning | Health Service Distribution Planning | Supplies planned-capacity requirements relevant to the service footprint. |
| Operational Care Capacity Management | Health Service Demand Assessment; Health Service Capacity Planning | May supply operational pressure/utilisation and capacity experience as planning evidence. |
| Health Service Distribution Planning | Healthcare Service Offering Management | May supply planned-footprint context for identifiable offerings. |
| Standards, Semantics & Reference Governance | Health Information Structure Management | Supplies governed semantic artefacts for reference/binding while retaining their authority. |
| Health Information Structure Management | Health Information Data Management | Supplies the structure, references, constraints and bindings through which content is meaningful. |
| Health Information Data Management | Health Information Lifecycle Management; Health Information Catalogue and Index Management; Health Information Compartmentalisation; Health Information Data Correlation | Supplies the managed information subject/content for these distinct responsibilities. |
| Health Information Catalogue and Index Management | Health Information Data Retrieval | Supplies existence, discovery and location context. |
| Health Information Compartmentalisation | Health Information Data Retrieval | Supplies applicable compartment scope/context without making an access decision. |
| Health Information Data Correlation | Health Information Data Retrieval | May supply relationships relevant to identifying related requested information. |
| Health Information Structure Management; Health Information Lifecycle Management | Health Information Data Retrieval | Supply interpretation and applicable current/historical/lifecycle context. |
| Clinical Knowledge Context Identification | Clinical Knowledge Context Extraction | Supplies the identified source/context for extraction. |
| Clinical Knowledge Context Extraction | Clinical Knowledge Information Formulation | Supplies relevant extracted material for formulation. |
| Clinical Knowledge Information Formulation | Clinical Knowledge Concept Association; Clinical Knowledge Review & Approval Management | Supplies formulated knowledge for association and governed review. |
| Standards, Semantics & Reference Governance | Clinical Knowledge Concept Association | Supplies governed concepts/artefacts without transferring semantic authority. |
| Clinical Knowledge Concept Association | Clinical Knowledge Review & Approval Management | Supplies semantic-association context relevant to review. |
| Clinical Knowledge Review & Approval Management | Clinical Knowledge Publication & Sharing | Supplies knowledge whose applicable approval standing is established. |
| Clinical Knowledge Publication & Sharing | Individual Care Delivery | Supplies reusable knowledge; the individual-care capability establishes its clinical application/conclusion. |
| Health Information Management | Clinical Knowledge Context Identification | May supply source information containing potential reusable knowledge. |

### Material Traceability Requirements

Later derivation SHALL preserve established CT3 identity and the distinction
between the Business purpose it supports and its owning BEC CT3 Feature frame.
It SHALL NOT attach a Harmonia Feature directly to a BC CT3. Where the following
contexts are material, traceability must preserve them without prescribing an
Information Concept, representation, interface or implementation:

| Concern | Context/evidence to preserve |
| :--- | :--- |
| Clinical risk | Evidence potentially indicating risk, the characterised risk, mitigation, active response and resulting state remain distinct. |
| Clinical quality | Underlying criterion authority; evidence source and purpose; criteria/evidence supporting an assessment; assessment authority distinct from its report and subsequent improvement. |
| Preventative health | Evidence supporting problem identification; target cohort; membership reasoning, inclusions/exclusions and applicable entry/exit or stratification. Programme type does not create another BC. |
| Population health | Population/cohort/geographical and temporal scope; evidence supporting assessment/event identification; notification obligation, recipient/authority, status and supporting event evidence. |
| Health-system planning | Demand basis, assumptions and scope; capacity requirements and distribution context; planned capacity distinguishable from operational evidence. |
| Health information | Applicable structures/reference targets/bindings and semantic authority; lifecycle state/history; catalogue versus content; compartment versus access decision; correlations and source provenance; retrieved information context. |
| Clinical knowledge | Source/extraction provenance and supporting formulation material; concept authority; review/approval state, conditions and history; publication preserving standing and limits; individual use distinct from general knowledge authority. |

Later traceability must preserve source provenance, applicable context, temporal
standing and these distinct business outcomes. Criteria do not replace governing
requirements; reported representations do not originate the facts/conclusions
they present. Programme-specific implementation does not establish another BC.
Implementation reuse does not collapse BC peers or reverse-derive their meaning.

The separately approved [assurance support derivation](../capability-maps/health-service-assurance-derivation.md)
and [B2.1 trace](../../05-information-systems-architecture/traceability/b2-1-individual-care-delivery-health-information-access.md)
remain intact, with no extension to the newly reconciled CT3s. This reconciliation
establishes no BEC ancestry, no framing of the 137 Features, no new BC↔BEC mapping,
no allocation of Canonical IDs/local codes and no Domain05 B2.2 work. The 69
unresolved enabling descriptions remain unresolved. Those are subsequent phases.

- [Business Enabling Capabilities](business-enabling-capabilities.md)
- [Capability decomposition and derivation model](../capability-maps/capability-tier-model.md)
- [Historical five-region reconciliation](../reviews/business-capability-five-region-reconciliation.md)
- [Business Architecture reference candidate](../../03-business-architecture/reference-model/README.md)
