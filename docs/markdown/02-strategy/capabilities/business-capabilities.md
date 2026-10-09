# Canonical Business Capability Model (18 L1 Capabilities)

## Overview & Purpose

The Business Capability Tier represents **what the healthcare enterprise must be capable of doing** to fulfill its clinical mission, provide health services to the population, govern quality and safety, and sustain healthcare operations.

A Business Capability is:
- **Independent of Technology**: It exists regardless of whether it is supported by pen-and-paper, legacy systems, modern electronic health records, or integration middleware.
- **Independent of Organizational Hierarchy**: It describes an operational ability, not a department, committee, or job title.
- **Enterprise-Wide in Scope**: It describes the entire healthcare delivery and governance environment within which Harmonia operates.

The Business Capability model does **not** describe Harmonia's software architecture. Instead, it defines the healthcare business landscape. By establishing this landscape, Harmonia can explicitly articulate where it provides core platform enablement, where it connects or routes operational information, and where it remains purely adjacent or reference.

---

## Capability Modeling Quality Rules

The 18 L1 Business Capabilities are governed by five foundational quality rules:

| Rule ID | Rule Name | Description & Architectural Intent |
| :--- | :--- | :--- |
| **CM-R01** | **Coverage** | The catalogue must encompass the complete healthcare enterprise operating scope across care and health delivery, health information and digital health, research and innovation, health-service management, and governance and independent assurance. |
| **CM-R02** | **Orthogonality** | Each capability represents a distinct, non-overlapping operational domain. Boundaries between capabilities are clear and mutually exclusive. |
| **CM-R03** | **Semantic Clarity** | Capability definitions use unambiguous clinical and healthcare operational terminology, avoiding vague IT jargon or vendor marketing language. |
| **CM-R04** | **No Junk Drawers** | Every capability has a cohesive, singular focus. Catch-all categories ("General Administration", "Miscellaneous Services") are strictly prohibited. |
| **CM-R05** | **Global Name Uniqueness** | Every capability name across all tiers and levels is globally unique within the Harmonia architecture. |

---

## Harmonia Relevance Taxonomy

An established Business Capability relevance classification assigns exactly one of the following tiers. Existing classifications for 01–16 are preserved. Classification for 17 and 18 and enabling roles for 17 remain **unresolved**; the approved Business Capability decision does not establish them. The separately approved [Health Service Assurance Strategy derivation](../capability-maps/health-service-assurance-derivation.md) establishes the bounded enabling role for 18 without assigning its relevance classification. “Unresolved” is a status, not an additional relevance tier.

| Relevance Tier | Definition & Architectural Meaning |
| :--- | :--- |
| **Reference** | Exists in the healthcare enterprise environment but does not materially affect Harmonia's platform responsibilities or processing obligations. |
| **Adjacent** | Harmonia consumes, represents, or supplies information around the capability but does not materially participate in its execution or coordination. |
| **Harmonia-Relevant** | Harmonia materially enables, connects, routes, coordinates, or supplies information used in performing the capability. |
| **Harmonia-Core** | Harmonia itself provides substantive, direct platform responsibility for the capability. |

### Foundational Principle: Enablement vs. Ownership
> **A Harmonia-Core capability may enable a Harmonia-Relevant business capability without Harmonia owning that business capability.**

Harmonia provides core integration, identity resolution, semantic transformation, and reliable event distribution to support clinical care. Harmonia does **not** practice medicine, make diagnostic judgements, or own clinical care delivery.

---

## The 18 L1 Business Capabilities

The 18 L1 Business Capabilities are organized into five natural operational regions:

1. **Care & Health Delivery** (Capabilities 01–08)
2. **Health Information & Digital Health** (Capabilities 09–12)
3. **Research & Innovation** (Capability 13)
4. **Health Service Management** (Capabilities 14–16)
5. **Governance & Assurance** (Capabilities 17–18)

These regions are natural contextual groupings of Business Capabilities. They are not capability tiers, organisational structures, application boundaries or ownership hierarchies. They are distinct from the five Business Enabling contextual views. The approved catalogue's L1 labels and numbers do not supply missing structural Canonical IDs or downstream Capability ancestry.

```text
┌──────────────────────────────────────────────────────────────────────────────────────────────┐
│                                    CARE & HEALTH DELIVERY                                    │
│  01. Individual Care Delivery                               [Harmonia-Relevant]              │
│  02. Care Access & Coordination                             [Harmonia-Relevant]              │
│  03. Health Rights, Advocacy & Participation                [Adjacent]                       │
│  04. Diagnostic, Therapeutic & Clinical Support Services    [Harmonia-Relevant]              │
│  05. Health Products & Clinical Technology                  [Reference]                      │
│  06. Clinical Quality, Safety & Improvement                 [Harmonia-Relevant]              │
│  07. Community Health & Wellbeing                           [Reference]                      │
│  08. Population Health & Health-System Planning             [Adjacent]                       │
├──────────────────────────────────────────────────────────────────────────────────────────────┤
│                             HEALTH INFORMATION & DIGITAL HEALTH                              │
│  09. Health Information & Knowledge Management              [Harmonia-Core]                  │
│  10. Standards, Semantics & Reference Governance            [Harmonia-Core]                  │
│  11. Connected Health Services                              [Harmonia-Core]                  │
│  12. Security, Privacy & Digital Trust                      [Harmonia-Core]                  │
├──────────────────────────────────────────────────────────────────────────────────────────────┤
│                                    RESEARCH & INNOVATION                                     │
│  13. Health Research & Innovation                           [Adjacent]                       │
├──────────────────────────────────────────────────────────────────────────────────────────────┤
│                                  HEALTH SERVICE MANAGEMENT                                   │
│  14. Health Service Direction & Stewardship                 [Reference]                      │
│  15. Workforce & Organisational Capability                  [Harmonia-Relevant]              │
│  16. Corporate Resources & Enterprise Services              [Reference]                      │
├──────────────────────────────────────────────────────────────────────────────────────────────┤
│                                    GOVERNANCE & ASSURANCE                                    │
│  17. Health Service Governance                              [Unresolved]                     │
│  18. Health Service Assurance                               [Unresolved]                     │
└──────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### Region 1: Care & Health Delivery

#### 01. Individual Care Delivery
- **Classification**: **Harmonia-Relevant**
- **Operational Scope**: The direct clinical assessment, diagnosis, treatment planning, intervention, monitoring, and ongoing care provided to an individual patient across acute, ambulatory, community, and home care settings.
- **Harmonia Enabling Role**: Harmonia provides clinical notification dispatch, point-of-care longitudinal health record assembly, and reliable transmission of clinical orders and results, ensuring treating clinicians possess complete, accurate patient context.

#### 02. Care Access & Coordination
- **Classification**: **Harmonia-Relevant**
- **Operational Scope**: Managing patient intake, referral triage, appointment scheduling, multidisciplinary care team coordination, cross-facility patient transfers, and care continuity across organizational boundaries.
- **Harmonia Enabling Role**: Harmonia routes structured referrals, correlates multidisciplinary care team assignments, tracks closed-loop referral and order progressions, and delivers cross-boundary notifications via secure collaboration channels.

#### 03. Health Rights, Advocacy & Participation
- **Classification**: **Adjacent**
- **Operational Scope**: Protecting patient rights, facilitating informed consent, managing advance care directives, supporting patient advocacy, and enabling consumer participation in health decisions.
- **Harmonia Enabling Role**: Harmonia evaluates patient consent directives at integration boundaries and provides tamper-evident audit trails of record access for consumer transparency, but does not manage consumer advocacy programs.

#### 04. Diagnostic, Therapeutic & Clinical Support Services
- **Classification**: **Harmonia-Relevant**
- **Operational Scope**: The specialized clinical services that support direct patient care, including pathology and laboratory testing, diagnostic imaging (radiology), pharmacy and medication dispensing, and allied health therapies.
- **Harmonia Enabling Role**: Harmonia integrates diverse diagnostic and therapeutic data streams with standards-based information models, correlates diagnostic reports with patient encounters, and enables reliable closed-loop coordination of diagnostic orders and results.

#### 05. Health Products & Clinical Technology
- **Classification**: **Reference**
- **Operational Scope**: The procurement, inventory control, maintenance, tracking, and biomedical governance of medical devices, implants, surgical supplies, and pharmaceuticals.
- **Harmonia Enabling Role**: Harmonia is not responsible for biomedical engineering, supply chain logistics, or equipment maintenance. Device telemetry or inventory changes are consumed only where they generate clinical observations or affect service capacity.

#### 06. Clinical Quality, Safety & Improvement
- **Classification**: **Harmonia-Relevant**
- **Operational Scope**: Operational clinical quality, safety and improvement: monitoring clinical outcomes; identifying quality and safety concerns; adverse event and incident management, reporting and response; infection control; operational quality and safety monitoring; continuous quality improvement; and coordination of operational responses to identified concerns.
- **Boundary**: Operational monitoring evaluates activity or service state for management, response and improvement purposes. Independent clinical audit or other independent evaluation of clinical activity against established governance, compliance, quality or safety criteria belongs to **18 Health Service Assurance**. A clinical audit label alone does not establish identical semantics for every audit activity. Ordinary quality monitoring remains here even when it supplies assurance evidence.
- **Harmonia Enabling Role**: Harmonia supports clinical safety by providing patient identifier correlation and association, eliminating duplicate event processing, ensuring non-destructive data preservation, and supplying tamper-evident clinical provenance trails.

#### 07. Community Health & Wellbeing
- **Classification**: **Reference**
- **Operational Scope**: Population-wide health promotion, disease prevention initiatives, environmental health management, community outreach, and social determinants of health programs.
- **Harmonia Enabling Role**: Harmonia focuses on regional health integration across clinical and operational healthcare providers; broad public health community outreach is outside platform scope.

#### 08. Population Health & Health-System Planning
- **Classification**: **Adjacent**
- **Operational Scope**: Aggregating epidemiologic data, monitoring public health registries (e.g., cancer, communicable diseases), health service demand modeling, and regional health resource planning.
- **Harmonia Enabling Role**: Harmonia provides governed longitudinal data extraction interfaces, syndicates statutory disease notifications, and provides aggregated directory endpoints to regional planning systems.

---

### Region 2: Health Information & Digital Health

#### 09. Health Information & Knowledge Management
- **Classification**: **Harmonia-Core**
- **Operational Scope**: Governing the lifecycle, structure, indexing, durable preservation, and retrieval of clinical health records, medical documentation, and clinical knowledge assets.
- **Harmonia Enabling Role**: Harmonia provides vendor-neutral longitudinal clinical record assembly, durable historical state preservation, and active distributed access to managed clinical information.

#### 10. Standards, Semantics & Reference Governance
- **Classification**: **Harmonia-Core**
- **Operational Scope**: Defining, governing, and publishing healthcare data models, exchange schemas, clinical terminologies (SNOMED CT, LOINC), classification systems (ICD-10/11), and semantic mapping rules.
- **Harmonia Enabling Role**: Harmonia maintains canonical data models, bidirectional healthcare format transformations, and directory profile conformance rules.

#### 11. Connected Health Services
- **Classification**: **Harmonia-Core**
- **Operational Scope**: Establishing resilient digital connectivity, multi-protocol integration boundaries, reliable message delivery, publish-subscribe event routing, and secure cross-enterprise exchange.
- **Harmonia Enabling Role**: Harmonia delivers multi-protocol boundary adaptation, resilient message distribution, and asynchronous activity coordination across enterprise boundaries.

#### 12. Security, Privacy & Digital Trust
- **Classification**: **Harmonia-Core**
- **Operational Scope**: Enforcing digital identity governance, authentication, attribute- and role-based access control (ABAC/RBAC), verifiable data protection, compliance auditing, and privacy preservation.
- **Harmonia Enabling Role**: Harmonia enforces default-deny policy evaluation, propagates tamper-evident, attributable security context, generates verifiable compliance audit records, and ensures isolation of protected health information.

---

### Region 3: Research & Innovation

#### 13. Health Research & Innovation
- **Classification**: **Adjacent**
- **Operational Scope**: Clinical trials management, translational research, genomic analysis, biomedical innovation, and health outcomes research.
- **Harmonia Enabling Role**: Harmonia supplies governed, de-identified clinical extracts and isolated synthetic testbed environments without hosting clinical trials.

---

### Region 4: Health Service Management

#### 14. Health Service Direction & Stewardship
- **Classification**: **Reference**
- **Operational Scope**: Health-service direction, executive leadership, strategic direction and planning, organisational priorities, health-service stewardship, organisational decision-making, and supporting legal advice where applicable.
- **Fundamental Question**: Where is the health service going, what are its priorities, and how should it be directed and sustained?
- **Boundary**: Governance authority, health-service board governance, organisational policy governance, establishment of regulatory compliance requirements and reporting requirements, and governance checkpoints belong to **17 Health Service Governance**. Independent assurance belongs to **18 Health Service Assurance**. Legal advice may support management, governance or other activities without itself becoming a Governance & Assurance capability.
- **Harmonia Enabling Role**: Executive leadership, health-service direction and stewardship are human organisational functions outside platform automation.

#### 15. Workforce & Organisational Capability
- **Classification**: **Harmonia-Relevant**
- **Operational Scope**: Healthcare provider credentialing, practitioner role management, organizational hierarchy governance, clinical rostering, and directory management.
- **Harmonia Enabling Role**: Harmonia delivers master Healthcare Provider Directory management (Practitioners, Roles, Organizations, Locations, Endpoints), federating authoritative national and regional directory feeds.

#### 16. Corporate Resources & Enterprise Services
- **Classification**: **Reference**
- **Operational Scope**: General enterprise resource planning (ERP), corporate finance, commercial payroll, billing/invoicing systems, facilities management, and corporate IT helpdesk services.
- **Harmonia Enabling Role**: Harmonia integrates with patient administration and financial systems to exchange clinical activity summaries and charging records without managing enterprise ERP functions.

---

### Region 5: Governance & Assurance

This region concerns establishing the obligations, authorities, requirements, constraints, reporting requirements and checkpoints under which health-service activities are expected to operate, and independently evaluating governed subjects against applicable requirements and criteria using sufficient trustworthy evidence.

Governance and independent assurance are its primary operational responsibilities. It is not a general-purpose location for every activity containing governance, control, monitoring, compliance, risk, quality or audit terminology.

#### 17. Health Service Governance
- **Definition**: **Health Service Governance is the capability to establish and maintain the requirements, obligations, authorities, constraints, policies, controls, reporting requirements and compliance checkpoints under which the health service and its activities are expected to operate.**
- **Fundamental Question**: **What must be true, under what authority and constraints, and what reporting and checkpoints are required?**
- **Operational Scope**: Governance authority; health-service governance, including board governance; organisational policy governance; obligations and constraints; governance controls; compliance requirements and compliance reporting requirements; governance checkpoints; and governed risk information used to inform governance decisions.
- **Boundary**: Governance establishes the normative operating envelope. It does not thereby determine the operational means by which governed activity achieves its required outcome or own that activity's management and progression. Subject-specific governance remains with its subject capability where intrinsic to managing that subject.
- **Classification / Harmonia Enabling Role**: **Unresolved**; no platform enablement or solution responsibility is allocated by this Business Capability reconciliation.

##### Risk Boundary

Risk is relevant context, not a separate L1 Business Capability or a newly authorised Harmonia Risk Management product scope. Governance, Management and Assurance activities may consider risk where relevant.

A governed Risk Register may exist within the Health Service Governance information context. It may inform governance decisions and management activity, be referenced by Incident Management and Incident Response, be informed by incident observations and outcomes, and provide context relevant to assurance. This contextual allowance does not derive an information concept or implementation.

This reconciliation establishes no comprehensive Risk Management lifecycle, Risk Appetite management, Risk Treatment workflows, Risk Ownership model, GRC platform or other conventional risk-management architecture. Any such scope requires separately established requirements.

#### 18. Health Service Assurance
- **Definition**: **Health Service Assurance is the capability to independently evaluate governed subjects against applicable requirements, obligations, constraints, controls, performance expectations, quality criteria and compliance checkpoints, using sufficient trustworthy evidence to establish assurance findings, exceptions and conclusions.**
- **Fundamental Question**: **Can we independently establish, from sufficient trustworthy evidence, that what was or is required was or is true?**
- **Operational Scope**: Independent evaluation of governed subjects against applicable criteria, including independent clinical audit/evaluation; establishment of assurance findings, exceptions and conclusions from sufficient trustworthy evidence.
- **Boundary**: Assurance may assess completion, state or outcome where relevant as evidence or criteria. It does not assess completion for the purpose of progressing the subject workflow. Where evidence cannot establish a conclusion with the required confidence, insufficiency remains explicit under [REQ-FND-005](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity).
- **Motivation Traceability**: [REQ-FND-005 — Independent Assurance of Governed Activity](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity) motivates BC-18 and its independence, management and evidence-insufficiency boundaries.
- **Classification**: **Unresolved**; the approved Strategy derivation does not assign a relevance tier.
- **Harmonia Enabling Role**: The approved [Assurance Design, Assurance Criteria Management and Governed Assurance](business-enabling-capabilities.md#health-service-assurance-approved-business-enabling-capabilities) support BC-18 through [collaborative Enterprise Capability realisation](../capability-maps/health-service-assurance-derivation.md#collaborative-enterprise-capability-contribution-matrix), including **EC-14 Service Guardian**. No strategic logical or application component is allocated this responsibility.

##### Clinical Services Delivery Assurance Boundary

Health Service Assurance SHALL NOT be represented as Clinical Services Delivery Assurance. The enterprise scope's reference to independent clinical audit/evaluation does not make Harmonia responsible for assuring clinical judgement, professional practice, clinical adequacy of care, or delivery of clinical services by healthcare practitioners or healthcare organisations.

Information concerning clinical service delivery MAY be the subject of governed assurance where an explicit applicable requirement establishes that assurance concern. This does not transfer responsibility for Clinical Services Delivery Assurance to Harmonia. [Capability 06 — Clinical Quality, Safety & Improvement](#06-clinical-quality-safety--improvement) retains its established clinical quality/safety responsibilities.

> **Harmonia may assure facts and behaviour concerning clinical activity where explicitly required; it does not thereby assure the clinical adequacy of care.**

##### Meaning of Independent

Independent Assurance requires an independently governed assurance activity/workflow whose progression and assurance conclusion are not controlled by the activity or mechanism responsible for performing or managing the subject being assured.

Independence does not inherently require an external auditor, a different organisation, department, human actor, application, deployment, database or Kubernetes cluster, or physical infrastructure separation. This is a responsibility and authority boundary consistent with REQ-FND-005, not an Application or Technology Architecture decision about physical execution separation.

> **Evidence dependency does not compromise assurance independence; control dependency does.**

The subject being assured may supply evidence. It shall not thereby be solely responsible for determining, suppressing, manufacturing or retrospectively altering its assurance conclusion.

##### Assurance / Operational Management Boundary

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

## Summary Matrix: Relevance & Enabling Roles

| # | L1 Business Capability | Region | Harmonia Relevance | Primary Enabling Responsibility |
| :- | :--- | :--- | :--- | :--- |
| **01** | Individual Care Delivery | Care & Health Delivery | **Harmonia-Relevant** | Longitudinal record assembly and presentation, clinical notification dispatch. |
| **02** | Care Access & Coordination | Care & Health Delivery | **Harmonia-Relevant** | Referral routing, care team correlation, secure collaboration dispatch. |
| **03** | Health Rights, Advocacy & Participation | Care & Health Delivery | **Adjacent** | Ingress consent directive evaluation, access audit evidence. |
| **04** | Diagnostic, Therapeutic & Clinical Support Services | Care & Health Delivery | **Harmonia-Relevant** | Diagnostic data integration, closed-loop order and result coordination. |
| **05** | Health Products & Clinical Technology | Care & Health Delivery | **Reference** | External biomedical and pharmacy supply chain outside platform scope. |
| **06** | Clinical Quality, Safety & Improvement | Care & Health Delivery | **Harmonia-Relevant** | Patient identifier correlation, duplicate suppression, tamper-evident provenance. |
| **07** | Community Health & Wellbeing | Care & Health Delivery | **Reference** | Public health outreach outside core platform integration scope. |
| **08** | Population Health & Health-System Planning | Care & Health Delivery | **Adjacent** | Governed longitudinal data extraction, statutory disease notification feeds. |
| **09** | Health Information & Knowledge Management | Health Information & Digital Health | **Harmonia-Core** | Vendor-neutral longitudinal clinical record assembly, durable state preservation. |
| **10** | Standards, Semantics & Reference Governance | Health Information & Digital Health | **Harmonia-Core** | Canonical data models, bidirectional healthcare format transformations, terminology governance. |
| **11** | Connected Health Services | Health Information & Digital Health | **Harmonia-Core** | Multi-protocol boundary adaptation, resilient message distribution, asynchronous activity coordination. |
| **12** | Security, Privacy & Digital Trust | Health Information & Digital Health | **Harmonia-Core** | Default-deny policy evaluation, attributable security context propagation, compliance auditing. |
| **13** | Health Research & Innovation | Research & Innovation | **Adjacent** | Governed de-identified clinical extract feeds, isolated synthetic testbed environments. |
| **14** | Health Service Direction & Stewardship | Health Service Management | **Reference** | Executive leadership, health-service direction and stewardship outside automation. |
| **15** | Workforce & Organisational Capability | Health Service Management | **Harmonia-Relevant** | Master Healthcare Provider Directory (Practitioner, Role, Org, Location). |
| **16** | Corporate Resources & Enterprise Services | Health Service Management | **Reference** | Integration with patient administration and financial systems for activity and charging records. |
| **17** | Health Service Governance | Governance & Assurance | **Unresolved** | Not derived; governance establishes obligations, authority, constraints, reporting requirements and checkpoints. |
| **18** | Health Service Assurance | Governance & Assurance | **Unresolved** | Assurance Design, Assurance Criteria Management and Governed Assurance, collaboratively realised by approved existing EC contributions and EC-14 Service Guardian; no component allocation. |

---

## Quality-Rule Conformance of the Reconciled Model

| Rule | Reconciliation evidence |
| :--- | :--- |
| **CM-R01 Coverage** | Five regions and eighteen capabilities now make health-service governance and independent assurance explicit while preserving the established care, information, research and management landscape. |
| **CM-R02 Orthogonality** | 06 monitors/responds/improves operational clinical quality and safety; 14 directs and sustains the service; 17 establishes the normative envelope; 18 independently evaluates against that envelope. Different evaluation purposes and conclusion authority distinguish monitoring from assurance, even when they share evidence. |
| **CM-R03 Semantic Clarity** | Explicit definitions separate Governance, Management, Management Monitoring and Independent Assurance. Evidence, business outcomes, incidents and risk are not assurance conclusions. |
| **CM-R04 No Junk Drawers** | Region 5 admits governance and independent assurance as primary responsibilities. Subject-specific governance, operational management, incident response and risk context do not become an unrestricted governance/assurance/risk category. |
| **CM-R05 Global Capability Name Uniqueness** | Existing capability names are retained except the approved 14 rename; 17 and 18 have distinct names. Neither new name duplicates another established capability name in the current Strategy catalogues. No alias or downstream capability is invented. |

---

## Downstream Progression

The 18 Business Capabilities define what the healthcare enterprise must do. The approved five-region/eighteen-capability reconciliation established the Business Capability model without enabling derivation for 17 or 18. The separately approved [Health Service Assurance Strategy derivation](../capability-maps/health-service-assurance-derivation.md) now establishes REQ-FND-005 → BC-18 → Assurance Design, Assurance Criteria Management and Governed Assurance → collaborative Enterprise Capability realisation, including EC-14. BC-17 enabling derivation and other unestablished relationships remain unresolved under [AX-17](../../../architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty).

- [Business Enabling Capabilities](business-enabling-capabilities.md): Five authentic healthcare contextual views with established Capability responsibilities and atomic Features; affected Capability Tier and complete ancestry remain unresolved.
- [Enterprise Capabilities](enterprise-capabilities.md): Reusable ICT capabilities (EC-01 .. EC-14), preserving the established EC-01 through EC-13 semantics.
- [Health Service Assurance Derivation](../capability-maps/health-service-assurance-derivation.md): Approved traceability, bounded contribution matrix and deliberately unresolved downstream relationships.
- [Capability Tier Progression Model](../capability-maps/capability-tier-model.md): Detailed vertical derivation rules and composition dynamics.
- [Five-Region Business Capability Reconciliation](../reviews/business-capability-five-region-reconciliation.md): Approved decision, direct documentary consequences, preserved semantics and unresolved downstream matters; a review record, not authority over this canonical model.

The assurance derivation adds only the explicitly approved Business Enabling responsibilities, principles, EC-14 and collaborative contribution relationships. EC-12's existing operational semantics remain unchanged. No Course of Action, Value Stream, strategic logical component, downstream Business/Information Architecture or solution allocation is inferred from 17 or 18.
