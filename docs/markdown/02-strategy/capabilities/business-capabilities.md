# Canonical Business Capability Model (16 L1 Capabilities)

## Overview & Purpose

The Business Capability Tier represents **what the healthcare enterprise must be capable of doing** to fulfill its clinical mission, provide health services to the population, govern quality and safety, and sustain healthcare operations.

A Business Capability is:
- **Independent of Technology**: It exists regardless of whether it is supported by pen-and-paper, legacy systems, modern electronic health records, or integration middleware.
- **Independent of Organizational Hierarchy**: It describes an operational ability, not a department, committee, or job title.
- **Enterprise-Wide in Scope**: It describes the entire healthcare delivery and governance environment within which Harmonia operates.

The Business Capability model does **not** describe Harmonia's software architecture. Instead, it defines the healthcare business landscape. By establishing this landscape, Harmonia can explicitly articulate where it provides core platform enablement, where it connects or routes operational information, and where it remains purely adjacent or reference.

---

## Capability Modeling Quality Rules

The 16 L1 Business Capabilities are governed by five foundational quality rules:

| Rule ID | Rule Name | Description & Architectural Intent |
| :--- | :--- | :--- |
| **CM-R01** | **Coverage** | The catalogue must encompass the complete healthcare enterprise operating scope across individual care delivery, digital health, research, and corporate stewardship. |
| **CM-R02** | **Orthogonality** | Each capability represents a distinct, non-overlapping operational domain. Boundaries between capabilities are clear and mutually exclusive. |
| **CM-R03** | **Semantic Clarity** | Capability definitions use unambiguous clinical and healthcare operational terminology, avoiding vague IT jargon or vendor marketing language. |
| **CM-R04** | **No Junk Drawers** | Every capability has a cohesive, singular focus. Catch-all categories ("General Administration", "Miscellaneous Services") are strictly prohibited. |
| **CM-R05** | **Global Name Uniqueness** | Every capability name across all tiers and levels is globally unique within the Harmonia architecture. |

---

## Harmonia Relevance Taxonomy

Each Business Capability is assigned to exactly one Harmonia relevance tier:

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

## The 16 L1 Business Capabilities

The 16 L1 Business Capabilities are organized into four natural operational regions:
1. **Care & Health Delivery** (Capabilities 01–08)
2. **Health Information & Digital Health** (Capabilities 09–12)
3. **Research & Innovation** (Capability 13)
4. **Enterprise Management** (Capabilities 14–16)

```text
┌────────────────────────────────────────────────────────────────────────┐
│                      CARE & HEALTH DELIVERY                            │
│  01. Individual Care Delivery                  [Harmonia-Relevant]     │
│  02. Care Access & Coordination                [Harmonia-Relevant]     │
│  03. Health Rights, Advocacy & Participation   [Adjacent]              │
│  04. Diagnostic, Therapeutic & Clinical Support[Harmonia-Relevant]     │
│  05. Health Products & Clinical Technology     [Reference]             │
│  06. Clinical Quality, Safety & Improvement    [Harmonia-Relevant]     │
│  07. Community Health & Wellbeing              [Reference]             │
│  08. Population Health & Health-System Planning[Adjacent]              │
├────────────────────────────────────────────────────────────────────────┤
│                 HEALTH INFORMATION & DIGITAL HEALTH                    │
│  09. Health Information & Knowledge Management [Harmonia-Core]         │
│  10. Standards, Semantics & Reference Gov.     [Harmonia-Core]         │
│  11. Connected Health Services                 [Harmonia-Core]         │
��  12. Security, Privacy & Digital Trust         [Harmonia-Core]         │
├────────────────────────────────────────────────────────────────────────┤
│                       RESEARCH & INNOVATION                            │
│  13. Health Research & Innovation              [Adjacent]              │
├────────────────────────────────────────────────────────────────────────┤
│                       ENTERPRISE MANAGEMENT                            │
│  14. Enterprise Direction & Stewardship        [Reference]             │
│  15. Workforce & Organisational Capability     [Harmonia-Relevant]     │
│  16. Corporate Resources & Enterprise Services [Reference]             │
└────────────────────────────────────────────────────────────────────────┘
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
- **Operational Scope**: Monitoring clinical outcomes, managing adverse events and incident reporting, clinical audit, infection control, and continuous quality improvement programs.
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

### Region 4: Enterprise Management

#### 14. Enterprise Direction & Stewardship
- **Classification**: **Reference**
- **Operational Scope**: Executive leadership, health service board governance, strategic planning, legal counsel, regulatory compliance reporting, and organizational policy formulation.
- **Harmonia Enabling Role**: Corporate governance and executive stewardship are human organizational functions outside platform automation.

#### 15. Workforce & Organisational Capability
- **Classification**: **Harmonia-Relevant**
- **Operational Scope**: Healthcare provider credentialing, practitioner role management, organizational hierarchy governance, clinical rostering, and directory management.
- **Harmonia Enabling Role**: Harmonia delivers master Healthcare Provider Directory management (Practitioners, Roles, Organizations, Locations, Endpoints), federating authoritative national and regional directory feeds.

#### 16. Corporate Resources & Enterprise Services
- **Classification**: **Reference**
- **Operational Scope**: General enterprise resource planning (ERP), corporate finance, commercial payroll, billing/invoicing systems, facilities management, and corporate IT helpdesk services.
- **Harmonia Enabling Role**: Harmonia integrates with patient administration and financial systems to exchange clinical activity summaries and charging records without managing enterprise ERP functions.

---

## Summary Matrix: Relevance & Enabling Roles

| # | L1 Business Capability | Region | Harmonia Relevance | Primary Enabling Responsibility |
| :- | :--- | :--- | :--- | :--- |
| **01** | Individual Care Delivery | Care & Health Delivery | **Harmonia-Relevant** | Longitudinal record assembly and presentation, clinical notification dispatch. |
| **02** | Care Access & Coordination | Care & Health Delivery | **Harmonia-Relevant** | Referral routing, care team correlation, secure collaboration dispatch. |
| **03** | Health Rights, Advocacy & Participation | Care & Health Delivery | **Adjacent** | Ingress consent directive evaluation, access audit evidence. |
| **04** | Diagnostic, Therapeutic & Clinical Support | Care & Health Delivery | **Harmonia-Relevant** | Diagnostic data integration, closed-loop order and result coordination. |
| **05** | Health Products & Clinical Technology | Care & Health Delivery | **Reference** | External biomedical and pharmacy supply chain outside platform scope. |
| **06** | Clinical Quality, Safety & Improvement | Care & Health Delivery | **Harmonia-Relevant** | Patient identifier correlation, duplicate suppression, tamper-evident provenance. |
| **07** | Community Health & Wellbeing | Care & Health Delivery | **Reference** | Public health outreach outside core platform integration scope. |
| **08** | Population Health & Health-System Planning | Care & Health Delivery | **Adjacent** | Governed longitudinal data extraction, statutory disease notification feeds. |
| **09** | Health Information & Knowledge Management | Health Information & Digital | **Harmonia-Core** | Vendor-neutral longitudinal clinical record assembly, durable state preservation. |
| **10** | Standards, Semantics & Reference Governance | Health Information & Digital | **Harmonia-Core** | Canonical data models, bidirectional healthcare format transformations, terminology governance. |
| **11** | Connected Health Services | Health Information & Digital | **Harmonia-Core** | Multi-protocol boundary adaptation, resilient message distribution, asynchronous activity coordination. |
| **12** | Security, Privacy & Digital Trust | Health Information & Digital | **Harmonia-Core** | Default-deny policy evaluation, attributable security context propagation, compliance auditing. |
| **13** | Health Research & Innovation | Research & Innovation | **Adjacent** | Governed de-identified clinical extract feeds, isolated synthetic testbed environments. |
| **14** | Enterprise Direction & Stewardship | Enterprise Management | **Reference** | Executive leadership and clinical governance outside automation. |
| **15** | Workforce & Organisational Capability | Enterprise Management | **Harmonia-Relevant** | Master Healthcare Provider Directory (Practitioner, Role, Org, Location). |
| **16** | Corporate Resources & Enterprise Services | Enterprise Management | **Reference** | Integration with patient administration and financial systems for activity and charging records. |

---

## Downstream Progression

The 16 Business Capabilities define what the healthcare enterprise must do. The next tier in the Strategy architecture defines what software systems and information infrastructure must enable in support of these business capabilities:
- [Business Enabling Capabilities](business-enabling-capabilities.md): Five authentic healthcare contextual views with established Capability responsibilities and atomic Features; affected Capability Tier and complete ancestry remain unresolved.
- [Enterprise Capabilities](enterprise-capabilities.md): Cross-cutting reusable ICT capabilities (EC-01 .. EC-13).
- [Capability Tier Progression Model](../capability-maps/capability-tier-model.md): Detailed vertical derivation rules and composition dynamics.
