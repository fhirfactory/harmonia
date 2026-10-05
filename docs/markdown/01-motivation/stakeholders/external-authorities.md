# External Authorities & Regulatory Bodies

## Overview

In enterprise architecture, it is essential to maintain clear demarcation between internal enterprise participants and external governing entities. External authorities and regulatory bodies are **not** enterprise stakeholders of Harmonia; they do not operate the platform, consume its clinical interfaces, or maintain its codebase.

Instead, these external bodies act as **external regulatory actors and sources of architectural constraints**. They establish the legal, statutory, identifier, and interoperability rules that Harmonia must satisfy. Representing them as external authorities preserves conceptual hygiene and clarifies where requirements originate.

---

## Key External Authorities & Constraint Sources

### 1. Australian Digital Health Agency (ADHA)
- **Role & Scope**: The national statutory agency responsible for coordinating the Australian national digital health strategy, operating the My Health Record infrastructure, and publishing national technical specifications.
- **Architectural Influence**:
  - Imposes national interoperability specifications (e.g., Secure Message Delivery, National Clinical Terminology Service, Australian Core FHIR profiles).
  - Shapes platform driver: [Clinical Interoperability Across Heterogeneous Standards](../drivers-assessments/drivers.md).
  - Source of constraint: [Mandated External Interoperability Contracts](../requirements-constraints/external-constraints.md).

### 2. Healthcare Identifiers (HI) Service
- **Role & Scope**: The national statutory service operated by Services Australia under the *Healthcare Identifiers Act 2010 (Cth)*.
- **Architectural Influence**:
  - Authoritative national provider of foundational healthcare identifiers:
    - **IHI** (Individual Healthcare Identifier) for patients/care recipients.
    - **HPI-I** (Healthcare Provider Identifier - Individual) for registered practitioners.
    - **HPI-O** (Healthcare Provider Identifier - Organisation) for healthcare delivery entities.
  - Establishes statutory obligations governing the handling, validation, access, and usage of national healthcare identifiers.
  - Shapes platform driver: [Patient Safety & Identity Integrity](../drivers-assessments/drivers.md).
  - Source of constraint: [Applicable National / Jurisdictional Healthcare Identifier Obligations](../requirements-constraints/external-constraints.md).

### 3. Australian Health Practitioner Regulation Agency (AHPRA)
- **Role & Scope**: The national statutory agency responsible for the registration and accreditation of health practitioners across Australia under the National Registration and Accreditation Scheme.
- **Architectural Influence**:
  - Establishes authoritative national registers of practitioners, registration types, endorsements, and disciplinary conditions.
  - Informs provider directory validation and practitioner role verification.
  - Shapes platform driver: [National Healthcare Directory / Endpoint Governance](../drivers-assessments/drivers.md).

### 4. Office of the Australian Information Commissioner (OAIC)
- **Role & Scope**: The independent statutory agency responsible for privacy and freedom of information regulation in Australia.
- **Architectural Influence**:
  - Enforces the *Privacy Act 1988 (Cth)*, the Australian Privacy Principles (APPs), and the Notifiable Data Breaches (NDB) scheme.
  - Establishes statutory obligations regarding the collection, use, disclosure, and protection of sensitive health information.
  - Defines regulatory compliance expectations for preventing unauthorized disclosures and reporting qualifying data breaches.
  - Shapes platform driver: [Statutory Health Information Privacy & Protection](../drivers-assessments/drivers.md).
  - Source of constraint: [Applicable Health-Information Privacy and Data-Protection Obligations](../requirements-constraints/external-constraints.md).

### 5. Standards Development Organizations (SDOs)
- **Bodies**: Health Level Seven International (HL7), FHIR Management Board, ISO Technical Committee 215 (Health Informatics), and Integrating the Healthcare Enterprise (IHE).
- **Role & Scope**: International consensus organizations that develop, publish, and maintain formal specifications for health information exchange.
- **Architectural Influence**:
  - Define wire protocols, serialization formats (HL7 v2 ER7, FHIR JSON/XML), and messaging semantics.
  - Source of constraint: [Mandated External Interoperability Contracts](../requirements-constraints/external-constraints.md).

---

## Architectural Delineation

| Entity Category | System Role | Relationship to Harmonia | Architectural Artefact |
| :--- | :--- | :--- | :--- |
| **Enterprise Stakeholders** | Active Participants | Directly operate, use, or rely on Harmonia services (e.g., Clinicians, Hospitals, Operators). | [Enterprise Stakeholders](enterprise-stakeholders.md) |
| **External Authorities** | Governing Bodies | External statutory, legal, or standards entities that mandate boundaries and rules. | [External Constraints](../requirements-constraints/external-constraints.md) |

This explicit separation ensures that regulatory directives are addressed as non-negotiable boundaries, without confusing external statutory agencies with internal system users.
