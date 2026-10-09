# Master Requirements Navigation & Traceability Catalogue

## Overview

The **Master Requirements Navigation & Traceability Catalogue** serves as the repository-wide index connecting platform motivation, architectural axioms, and downstream requirements across all architecture domains.

### Scope & Architectural Authority
- **Domain 01 Ownership**: Domain 01 owns the five cross-cutting [Foundational Platform Requirements](foundational-requirements.md) (`REQ-FND-001` through `REQ-FND-005`), the three generalized external constraint categories (`CST-EXT-001` through `CST-EXT-003`), and this master navigation catalogue. The original four requirements retain Accepted status; `REQ-FND-005` is **APPROVED**, the authorised Independent Assurance reconciliation is **APPROVED / CLOSED**, and Domain01 is again **CLOSED / FROZEN**, as recorded in the [reconciliation record](../reviews/independent-assurance-reconciliation.md).
- **Downstream Ownership**: Detailed, subsystem-specific requirements remain strictly owned by their respective downstream architecture domains (Domain 04 Information, Domain 05 Application, Domain 06 Integration, Domain 08 Security, Domain 11 Solution Packs, Domain 13 Governance). Downstream requirements are marked `Candidate` until their owning architecture domains undergo formal canonical reconciliation.
- **Navigation Index, Not Duplicate Database**: This catalogue provides global traceability and lifecycle status tracking. It does not duplicate detailed functional specifications owned downstream.
- **Realistic Traceability**: Traceability captures meaningful, architecturally established relationships without forcing an artificial, fully connected graph across all elements; absence of a relationship between unrelated items is architecturally valid.

---

## 1. Master Requirements & Constraints Catalogue

The following table indexes platform requirements, foundational axioms, owning domains, and current governance status:

| Identifier | Short Title | Owning Architecture Domain | Governing Principles | Primary Motivation / Driver | Classification | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **REQ-FND-001** | [Durable Ingress Acceptance Boundary](foundational-requirements.md) | Domain 01 (Motivation) | [AX-05](../../governance/architectural-axioms.md#ax-05), [AX-10](../../governance/architectural-axioms.md#ax-10), [AX-15](../../governance/architectural-axioms.md#ax-15) | [Silent Data Loss via False Acceptance](../drivers-assessments/assessments.md) | Foundational Platform Requirement | **Accepted** |
| **REQ-FND-002** | [Operational Activity Progression State](foundational-requirements.md) | Domain 01 (Motivation) | [AX-16](../../governance/architectural-axioms.md#ax-16) | [Unmonitored Destination Failure](../drivers-assessments/assessments.md) | Foundational Platform Requirement | **Accepted** |
| **REQ-FND-003** | [Subject Referential Integrity](foundational-requirements.md) | Domain 01 (Motivation) | [AX-06](../../governance/architectural-axioms.md#ax-06), [AX-14](../../governance/architectural-axioms.md#ax-14) | [Patient Safety & Identity Integrity](../drivers-assessments/drivers.md) | Foundational Platform Requirement | **Accepted** |
| **REQ-FND-004** | [Explicit Indeterminate Outcome](foundational-requirements.md) | Domain 01 (Motivation) | [AX-15](../../governance/architectural-axioms.md#ax-15) | [Silent Data Loss via False Acceptance](../drivers-assessments/assessments.md) | Foundational Platform Requirement | **Accepted** |
| **REQ-FND-005** | [Independent Assurance of Governed Activity](foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity) | Domain 01 (Motivation) | [AX-06](../../governance/architectural-axioms.md#ax-06), [AX-07](../../governance/architectural-axioms.md#ax-07), [AX-08](../../governance/architectural-axioms.md#ax-08), [AX-14](../../governance/architectural-axioms.md#ax-14) | [Operator compliance/forensic review and engineer verification concerns; privacy obligations](foundational-requirements.md#motivation-relationships) | Foundational Platform Requirement | **APPROVED** |
| **CST-EXT-001** | [Applicable Privacy & Data-Protection](external-constraints.md) | Domain 01 (Motivation) | [AX-07](../../governance/architectural-axioms.md#ax-07), [AX-08](../../governance/architectural-axioms.md#ax-08) | [Statutory Health Privacy](../drivers-assessments/drivers.md) | External Constraint Category | **Accepted** |
| **CST-EXT-002** | [Applicable Healthcare Identifiers](external-constraints.md) | Domain 01 (Motivation) | [AX-06](../../governance/architectural-axioms.md#ax-06), [AX-14](../../governance/architectural-axioms.md#ax-14) | [Patient Safety & Identity Integrity](../drivers-assessments/drivers.md) | External Constraint Category | **Accepted** |
| **CST-EXT-003** | [Mandated Interoperability Contracts](external-constraints.md) | Domain 01 (Motivation) | [AX-02](../../governance/architectural-axioms.md#ax-02), [AX-03](../../governance/architectural-axioms.md#ax-03) | [Clinical Interoperability](../drivers-assessments/drivers.md) | External Constraint Category | **Accepted** |
| **CORE-001** | Authority Classification | Domain 04 (Information) | [AX-06](../../governance/architectural-axioms.md#ax-06) | [Clinical Information Independence](../drivers-assessments/drivers.md) | Core Semantic Requirement | **Candidate** |
| **CORE-003** | Governed Canonical Semantics | Domain 04 (Information) | [AX-02](../../governance/architectural-axioms.md#ax-02), [AX-03](../../governance/architectural-axioms.md#ax-03) | [Clinical Interoperability](../drivers-assessments/drivers.md) | Core Semantic Requirement | **Candidate** |
| **SEC-001** | Trusted Identity & Authentication | Domain 08 (Security) | [AX-07](../../governance/architectural-axioms.md#ax-07) | [Implicit Perimeter Trust](../drivers-assessments/assessments.md) | Security Architecture Requirement | **Candidate** |
| **SEC-002** | Default-Deny Authorization | Domain 08 (Security) | [AX-07](../../governance/architectural-axioms.md#ax-07) | [Implicit Perimeter Trust](../drivers-assessments/assessments.md) | Security Architecture Requirement | **Candidate** |
| **SEC-010** | PHI-Safe Operational Logging | Domain 08 (Security) / Domain 13 (Governance) | [AX-07](../../governance/architectural-axioms.md#ax-07) | [PHI Leakage through Operational Logging](../drivers-assessments/assessments.md) | Security Architecture Requirement | **Candidate** |
| **PROV-001** | End-to-End Accountable Provenance | Domain 08 (Security) / Domain 04 (Info) | [AX-08](../../governance/architectural-axioms.md#ax-08) | [Statutory Health Privacy](../drivers-assessments/drivers.md) | Security & Provenance Requirement | **Candidate** |
| **INT-TRANS-001**| At-Least-Once Transport & Idempotency | Domain 06 (Integration) | [AX-10](../../governance/architectural-axioms.md#ax-10) | [Continuous Clinical Availability](../drivers-assessments/drivers.md) | Integration Architecture Requirement | **Candidate** |
| **APP-SEP-001** | Transport / Business Separation | Domain 05 (App) / Domain 06 (Integration) | [AX-02](../../governance/architectural-axioms.md#ax-02), [AX-04](../../governance/architectural-axioms.md#ax-04) | [Clinical Interoperability](../drivers-assessments/drivers.md) | Application Layering Pattern | **Candidate** |
| **DIR-001..006** | Provider Directory Workflow Pragmas | Domain 11 (Solution Pack: Provider Directory) | [AX-14](../../governance/architectural-axioms.md#ax-14), [AX-16](../../governance/architectural-axioms.md#ax-16) | [Directory & Endpoint Governance](../drivers-assessments/drivers.md) | Solution-Specific Requirement | **Candidate** |

---

## 2. Reclassification & Supersession Registry

The following table records historical patterns, legacy requirements, and design formulations that have been formally superseded, retired, or reclassified during the canonical consolidation of Domain 01:

| Historical / Legacy Element | Previous Status | Current Canonical Status | Target Allocation | Architectural Rationale for Supersession / Reclassification |
| :--- | :--- | :--- | :--- | :--- |
| **Write-Behind Caching Persistence** | Proposed Principle | **Retired / Superseded** | None | Conflated active in-memory cache state with authoritative durable state; violated AX-05 by risking silent data loss if volatile cache crashed prior to database flush. |
| **Pre-Persistence Positive ACK** | Implementation Practice | **Retired / Superseded** | None | Violated Invariant 4 (`REC-001`) and `REQ-FND-001`; returning an HL7 `AA` ACK before durable persistence creates severe vulnerability to silent message loss. |
| **AX-12 ("Hide Plumbing")** | Earlier reclassification claim | **Current cross-domain architectural axiom** | [Canonical governance register](../../governance/architectural-axioms.md#ax-12) | The 2026-10-09 human decision retains AX-12 and supersedes the earlier transfer claim. No Domain05 transfer is made. |
| **At-Least-Once Delivery & Idempotency** | Architecture Pattern | **Reclassified** | Domain 06 (Integration Architecture) | Wire transport and message broker delivery contract rather than universal enterprise motivation axiom. |
| **Transport / Business Separation** | Architecture Rule | **Reclassified** | Domain 05 / 06 (App & Integration) | Internal layering pattern governing Petasos messaging and Erga activity processing boundaries. |
| **Canonical FHIR R5 Schemas** | Architecture Rule | **Reclassified** | Domain 04 / 06 (Info & Integration) | Specific interface and serialization contract selection rather than universal motivation principle. |
| **Zero-PHI Diagnostic Logging** | Principle Definition | **Reclassified** | Domain 08 (Security) / Domain 13 (Governance) | Downstream Security Architecture operational requirement/constraint derived from AX-07; formal architectural guardrails cleanly deferred to Domain 13. |
| **DIR-001 through DIR-006** | Architecture Requirements | **Reclassified** | Domain 11 (Solution Pack: Provider Directory) | Solution-specific workflow rules and pragma definitions for Provider Directory change events. |

---

## 3. Downstream Domain Traceability Handoff

Domain 01 Motivation establishes the bedrock upon which subsequent architecture domains build:

`REQ-FND-005` supplies solution-independent motivation for subsequent Strategy responsibility, Business assurance behaviour and Information Architecture derivation, followed by Application Architecture and an eventual solution. These downstream relationships are **not yet established**. Human review and refreezing of Domain01 are complete; subsequent reconciliation requires separate instruction. The requirement does not allocate itself to an existing capability, component or workflow.

- **Domain 02 (Strategy)**: Consumes Strategic Goals and Platform Drivers to define enterprise capabilities, resource allocations, and clinical value streams.
- **Domain 04 (Information Architecture)**: Realizes `CORE-001` (Authority Classification), `CORE-003` (Canonical Semantics), and `REQ-FND-003` (Subject Referential Integrity) through formal data models and FHIR profiles.
- **Domain 05 (Application Architecture)**: Realizes `REQ-FND-002` (Activity Progression State), remains subject to current cross-domain `AX-12` (Hide Plumbing, Not Information), and enforces the separation between active state and authoritative persistence (`AX-05`).
- **Domain 06 (Integration Architecture)**: Realizes `REQ-FND-001` (Durable Ingress Acceptance Boundary via `REC-001`), `INT-TRANS-001` (At-Least-Once Transport), and external wire protocol gateways (`CST-EXT-003`).
- **Domain 08 (Security Architecture)**: Realizes `SEC-001`, `SEC-002`, `SEC-010` (PHI-Safe Operational Logging), and `PROV-001` derived from `AX-07`, `AX-08`, and `CST-EXT-001`.
- **Domain 11 (Solution Packs)**: Implements specialized solution workflows (such as Provider Directory `DIR-001..DIR-006`).
- **Domain 13 (Governance & Decisions)**: Owns formal architectural guardrails, Architecture Decision Records (ADRs), and compliance enforcement.
