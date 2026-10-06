# Domain 04 Foundation Semantic Hygiene Completion Report

**Date**: 2026-10-06  
**Status**: Completed  
**Scope**: Domain 04 — Information Architecture Foundation Semantic Hygiene Pass

---

### 1. Executive Summary

A bounded semantic hygiene pass was executed across the newly created **Domain 04 — Information Architecture** foundation documentation. The pass removed semantic drift, eliminated non-canonical upstream terminology, refined responsibility derivation rules to accommodate external and contextual concepts without unintended ownership acquisition, clarified the boundary between architectural responsibility and information stewardship, established semantically scoped Person containment constraints, and generalized reference service models and candidate assemblies.

The core Information Architecture metamodel, foundational design patterns, and governing architectural question remain unchanged. Authoritative upstream baselines (**Domain 01 Motivation**, **Domain 02 Strategy**, and **Domain 03 Business Architecture**) were strictly preserved without modification.

---

### 2. Files Reviewed and Modified

#### Documents Modified in `docs/markdown/04-information-architecture/`:
1. `README.md`
   - Updated domain summaries to reflect generalized multi-domain `ServiceOutcome` concepts, candidate illustrative assembly framing, and refined containment/anchoring rules.
2. `metamodel/information-architecture-metamodel.md`
   - Refined Information Responsibility characteristics and governance principles to distinguish Harmonia-owned concepts from referenced/external concepts without ownership transfer.
3. `patterns/information-relationships.md`
   - Replaced invented/non-canonical role examples (*Registered Clinician*, *Triage Officer*, *System Administrator*, *Patient / Care Recipient*) with canonical Domain 03 Business Roles (*Clinician*, *Care Coordinator*, *Information Steward*, *Patient*, *Representative*, *Performer*, *Service Provider*).
   - Clarified that *Healthcare Subject* is an entity/subject context orthogonal to the *Patient* Business Role and is NOT itself a Business Role.
   - Updated the Relationship-to-Business-Role boundary diagram and illustrative table to use canonical roles (*Representative*, *Support Person*).
4. `patterns/containment-and-collections.md`
   - Replaced absolute Person containment bans with the semantically bounded constraint: *Person-oriented concepts SHALL NOT use recursive containment merely to represent familial, social, care, representation or authority relationships. Those semantics SHALL use appropriate Information Relationships or Collections.*
5. `patterns/definition-to-accountability.md`
   - Broadened `ServiceOutcome` across clinical, operational, and administrative outcomes according to service context.
   - Clarified that `HealthcareServiceDelivery` does not universally mandate a Healthcare Subject when broader service semantics permit otherwise (e.g. facility disinfection, environmental testing, population calibration).
6. `governance/authority-custody-provenance.md`
   - Disentangled architectural Information Responsibility from "data stewardship" and the `Information Steward` governance role.
7. `assemblies-views/assemblies-and-views.md`
   - Explicitly framed context assemblies as illustrative candidate assemblies, removing premature lifetime/episode/regional assertions.
   - Replaced invented concept labels (e.g. *Person Master Record*) with canonical Domain 03/04 terms (*Person Identity Record*).
8. `guardrails/modelling-guardrails.md`
   - Updated Guardrail 6 (Responsibility Derivation & Bounded Ownership) to reflect the refined ownership derivation rule and stewardship distinction.
   - Updated Guardrail 9 (Containment vs Membership) to incorporate the semantically bounded Person containment constraint.
9. `traceability/domain03-traceability.md`
   - Replaced non-canonical Capability, Feature, Function, Process, and Information Responsibility names with exact verbatim Domain 03 terms (*Person Identity*, *Cross-Authority Identifier Correlation*, *Correlate Person Identifiers*, *Person Identity Record*, *Identifier Namespace Bindings*, *Cross-Authority Correlation Graph*, *Service-Location-Provider Binding*, *Service / Location / Provider Mapping Matrix*, *Closed-Loop Order Status Tracking*, *Track Order Fulfilment Status*, *Clinical Order Record*, *Order Status History*, *Order-Result Reconciliation Binding*).
   - Replaced the absolute "No Unanchored Information Concepts" rule with the refined bounded ownership derivation rule.
   - Disentangled architectural Information Responsibility from "data stewardship" or the `Information Steward` role.

---

### 3. Terminology Corrections Against Domain 03 Baselines

| Document Location | Prior / Non-Canonical Terminology | Corrected Canonical Domain 03 Terminology |
| :--- | :--- | :--- |
| `patterns/information-relationships.md` | *Registered Clinician*, *Triage Officer*, *System Administrator*, *Patient / Care Recipient* | *Clinician*, *Care Coordinator*, *Information Steward*, *Patient*, *Representative*, *Performer*, *Service Provider* |
| `patterns/information-relationships.md` | *Healthcare Subject* (represented as a Business Role) | Explicitly clarified as an Entity / Subject context; diagram updated to use *Representative* |
| `traceability/domain03-traceability.md` | *Establish / Link Person Master Identity* (invented Function) | `Correlate Person Identifiers` (under Feature: *Cross-Authority Identifier Correlation*) |
| `traceability/domain03-traceability.md` | *Person Identity & Identifier Correlation Graph* | `Person Identity Record`, `Identifier Namespace Bindings`, `Cross-Authority Correlation Graph` |
| `traceability/domain03-traceability.md` | *Process & Route Closed-Loop Order* / *Clinical Order & Closed-Loop Matrix* | `Track Order Fulfilment Status` / `Clinical Order Record, Order Status History, Order-Result Reconciliation Binding` |
| `traceability/domain03-traceability.md` | *Data Stewardship* (as synonym for Information Responsibility) | Architectural Information Responsibility (capability property distinct from `Information Steward` role) |
| `assemblies-views/assemblies-and-views.md` | *Person Master Record* | `Person Identity Record` |

---

### 4. Refined Rules and Guardrails

1. **Bounded Responsibility Derivation**:
   > *Information Concepts for which Harmonia claims architectural responsibility SHALL trace to an owning Domain 03 Information Responsibility and the Functions/Processes through which it is discharged. Referenced, consumed, externally authoritative or contextual Information Concepts may be represented where required to discharge a traced Harmonia responsibility, but SHALL NOT thereby acquire Harmonia ownership or authority.*
2. **Architectural Responsibility vs. Stewardship**:
   > *Architectural information responsibility is a capability property and is distinct from the governance role of `Information Steward`.*
3. **Semantically Scoped Person Containment**:
   > *Person-oriented concepts SHALL NOT use recursive containment merely to represent familial, social, care, representation or authority relationships. Those semantics SHALL use appropriate Information Relationships or Collections.*
4. **Multi-Domain Service Outcomes**:
   > *`ServiceOutcome` captures clinical findings, diagnostic observations, therapeutic changes, operational outputs, or administrative artifacts according to the service context.*

---

### 5. Upstream Immutability & Downstream Boundary Confirmation

- **Domains 01, 02, and 03**: Confirmed **zero modifications** across `docs/markdown/01-motivation/`, `docs/markdown/02-strategy/`, and `docs/markdown/03-business-architecture/`.
- **Application Architecture (Domain 05) / Integration Architecture (Domain 06)**: Confirmed zero introduction of software classes, Spring beans, database schemas, JPA entities, FHIR profiles, or REST endpoints.
- **Detailed Information Family Population**: Full domain information catalogues across all 110 business enabling capabilities remain reserved for subsequent dedicated modelling tasks.
