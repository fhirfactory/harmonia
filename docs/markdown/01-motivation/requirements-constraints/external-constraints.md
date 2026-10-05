# External Regulatory & Standards Constraints

## Overview

External Constraints represent the non-negotiable boundaries, legal obligations, and technical standards imposed on the Harmonia Health Integration Environment by external entities. In enterprise architecture, a constraint limits architectural and implementation choices.

To prevent deployment-specific statutes or regional protocols from being mistakenly universalized into core platform axioms, Harmonia categorizes external constraints into strictly **three generalized external constraint categories**. Specific regional statutes (such as Australian privacy acts or national identifier formats) and wire protocols (such as HL7 v2 over MLLP) serve as concrete jurisdictional instances of these categories rather than universal platform laws.

---

## CST-EXT-001: Applicable Health-Information Privacy and Data-Protection Obligations

### Categorical Definition
> **Harmonia must operate within the health-information privacy, confidentiality, disclosure, retention, and data-protection obligations applicable to the jurisdiction and deployment context.**

### Context & Architectural Boundary
Health data is categorized as hypersensitive personal data across all legal jurisdictions. Wherever Harmonia is deployed, it is bound by the statutory data protection regime governing that region. The platform must be architecturally designed to satisfy these obligations without requiring bespoke rewrites of core domain logic.

### Representative Instances & Examples
- **Australia**: The *Privacy Act 1988 (Cth)*, the Australian Privacy Principles (APPs), the *My Health Records Act 2012*, and state-based health records legislation (e.g., *Health Records Act 2001 (Vic)*).
- **United States**: The Health Insurance Portability and Accountability Act (HIPAA) Security and Privacy Rules.
- **European Union**: The General Data Protection Regulation (GDPR) and European Health Data Space (EHDS) regulations.

### Direct Architectural Impacts
- Requires downstream security architecture to govern access to protected health information and enforce applicable access controls ([`SEC-001 / SEC-002`](master-requirements-catalogue.md)).
- Requires downstream operational logging policies and guardrails to protect health information from unauthorized exposure in diagnostic telemetry ([`SEC-010`](master-requirements-catalogue.md)).
- Requires platform communications and stored data representations to apply appropriate protection mechanisms according to deployment risk and statutory requirements.
- Informs audit and provenance requirements for demonstrating authorized handling of health information ([`PROV-001`](master-requirements-catalogue.md)).
- Bounds Platform Driver: [Statutory Health Information Privacy & Protection](../drivers-assessments/drivers.md).

---

## CST-EXT-002: Applicable National / Jurisdictional Healthcare Identifier Obligations

### Categorical Definition
> **Where a deployment participates in a national or jurisdictional healthcare identifier ecosystem, Harmonia must conform to applicable identifier representation, validation, authority, and usage obligations.**

### Context & Architectural Boundary
Many modern nations operate statutory healthcare identifier services to uniquely identify patients, clinicians, and health organizations. Participation in these ecosystems is legally regulated: the law dictates how identifiers may be retrieved, verified, stored, cross-referenced, and disclosed. Harmonia must conform strictly to these statutory rules while maintaining internal referential integrity.

### Representative Instances & Examples
- **Australia**: The *Healthcare Identifiers Act 2010 (Cth)* and HI Service Operating Rules:
  - **IHI** (Individual Healthcare Identifier): Strict prohibitions against using the IHI as a general-purpose identity token or publishing it outside authorized clinical communication channels.
  - **HPI-I** (Healthcare Provider Identifier - Individual): Validation against national registers and credentialing rules.
  - **HPI-O** (Healthcare Provider Identifier - Organisation): Association with authorized electronic service delivery endpoints.
- **United Kingdom**: NHS Number validation and Spine integration rules.
- **United States**: National Provider Identifier (NPI) specifications.

### Direct Architectural Impacts
- Requires strict validation of identifier check digits, namespaces, and authority types before accepting clinical records ([`REQ-FND-003`](foundational-requirements.md)).
- Dictates that external national identifiers must not be conflated with internal synthetic database keys or process identifiers.
- Requires integration boundaries interacting with national directory or identifier services to adhere to governing access, query, and communication rules.
- Bounds Platform Driver: [Patient Safety & Identity Integrity](../drivers-assessments/drivers.md).

---

## CST-EXT-003: Mandated External Interoperability Contracts

### Categorical Definition
> **Where an external system or ecosystem mandates a protocol, information standard, transport contract, or serialization format, Harmonia must conform to that contract at the applicable interoperability boundary.**

### Context & Architectural Boundary
Harmonia operates as an integration hub connecting hundreds of disparate health systems. The platform cannot dictate the internal wire protocols of external hospitals, laboratory analyzers, or national repositories. At its ingress and egress boundaries, Harmonia must conform strictly to the wire formats, message structures, and transport handshakes mandated by external systems.

However, in accordance with [AX-02 (Standards at Boundary; Sovereignty Within)](../principles/architectural-axioms.md), external contracts govern only the external membrane. Once an event crosses into Harmonia, the platform enforces its own canonical domain semantics.

### Representative Instances & Examples
- **HL7 v2.x over MLLP**: Minimal Lower Layer Protocol (MLLP) framing over raw TCP, utilizing pipe-and-hat (`ER7`) segment syntax for admissions (ADT), orders (ORM), and observations (ORU).
- **HL7 FHIR RESTful APIs**: HTTP/JSON or HTTP/XML operations conforming to specific regional profiles (e.g., Australian Core AU-Base / AU-Core, US Core).
- **Secure Messaging Delivery (SMD)**: Encrypted Web Services (WS-*) protocols mandated for inter-clinic electronic correspondence.
- **DICOM**: Digital Imaging and Communications in Medicine protocol for radiological studies.

### Direct Architectural Impacts
- Demarcates the boundary between Pylai (the protocol membrane gateways) and core internal platform engines.
- Governs wire-level error signaling (e.g., mapping internal durable persistence failures to protocol-appropriate error responses).
- Prevents external wire formats from polluting internal domain models.
- Imposes on Platform Driver: [Clinical Interoperability Across Heterogeneous Standards](../drivers-assessments/drivers.md).
