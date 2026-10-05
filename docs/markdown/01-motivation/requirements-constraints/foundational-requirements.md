# Foundational Platform Requirements

## Overview

Foundational Platform Requirements represent the enduring, non-negotiable architectural obligations owned directly by Domain 01 Motivation. Unlike detailed subsystem requirements (which are owned downstream by Business, Application, Integration, and Security domains), foundational requirements express cross-cutting platform invariants that apply across all Harmonia capabilities.

Harmonia recognizes strictly **four foundational requirements**. Each requirement is stated using a protocol-neutral normative formulation, ensuring that implementation choices and wire protocols are recognized as downstream realisations rather than foundational definitions.

---

## REQ-FND-001: Durable Ingress Acceptance Boundary

### Normative Requirement Statement
> **Harmonia shall not positively acknowledge acceptance of an incoming event until responsibility for that event has crossed the required durable acceptance boundary.**

### Architectural Intent & Rationale
This requirement eliminates the critical vulnerability identified in [Silent Data Loss via False Acceptance](../drivers-assessments/assessments.md). When an upstream external system transmits a clinical event (such as an emergency admission, critical lab result, or medication order), it remains the responsibility of that upstream sender until Harmonia has durably secured the event.

Releasing an upstream sender from responsibility (by returning a positive transport acknowledgement) before the event is committed to durable storage creates a vulnerable window where gateway crashes, memory exhaustions, or hardware reboots cause silent, unrecoverable data loss.

### Protocol-Neutral Delineation & Downstream Realisations
- **Normative Rule**: The requirement mandates that the durable acceptance boundary must be crossed before positive acknowledgement. It is completely protocol-neutral.
- **Downstream Realisations**:
  - In HL7 v2 over MLLP integration (owned by Domain 06 Integration Architecture), this rule is realised by forbidding an `MSA-1 = AA` (Application Accept) acknowledgement until downstream persistence or queue publication succeeds. If persistence fails, the gateway MUST return `MSA-1 = AE` (Application Error) to trigger upstream sender retry.
  - In FHIR RESTful APIs, this rule is realised by returning `201 Created` or `200 OK` only after transactional database commitment, returning `202 Accepted` only when durable asynchronous queueing is verified, and returning `500 Internal Server Error` or `503 Service Unavailable` on durable failure.
  - In Ingress Dual-Write safety (`REC-001`), this rule enforces that in-memory active caching alone is insufficient to satisfy positive acceptance.

- **Primary Motivation**: [Silent Data Loss via False Acceptance](../drivers-assessments/assessments.md), [Continuous Clinical Service Availability](../drivers-assessments/drivers.md)
- **Governing Principles**: [AX-05 (State Separation)](../principles/architectural-axioms.md), [AX-10 (Failure as Normal Condition)](../principles/architectural-axioms.md), [AX-15 (Explicit Representation of Uncertainty)](../principles/architectural-axioms.md)
- **Historical Identifier Reference**: `REQ-INGRESS-001`

---

## REQ-FND-002: Operational Activity Progression State

### Normative Requirement Statement
> **Harmonia shall maintain sufficient explicit and observable state to determine the progression and outcome of managed multi-stage operational activities.**

### Architectural Intent & Rationale
Complex health integration involves distributed, multi-stage activities—such as routing ADT events, dispatching laboratory orders, and executing multi-destination fan-out deliveries. If integration middleware treats these operations as disconnected fire-and-forget messages, failures at individual destination endpoints cause unmonitored divergence between hospital systems (as diagnosed in [Unmonitored Destination Failure](../drivers-assessments/assessments.md)).

Harmonia mandates that every multi-stage operational activity must maintain explicit, queryable, and observable progression state across its entire lifecycle. Operational state must capture checkpoints, destination delivery statuses, error codes, and completion outcomes, enabling operational engineers to monitor, audit, and reconcile distributed activities.

### Downstream Scope
- Realised in Domain 05 Application Architecture and Domain 06 Integration Architecture through Pragma envelopes, FHIR `Task` resources, and task progression tracking.
- Does not mandate a specific workflow engine; it mandates observable progression state synchronized with real-world entity state.

- **Primary Motivation**: [Unmonitored Destination Failure / Fan-Out Divergence](../drivers-assessments/assessments.md), [Coordinated Operational Activity and Entity State](../drivers-assessments/drivers.md)
- **Governing Principle**: [AX-16 (Operational Activity & Entity State Progress Together)](../principles/architectural-axioms.md)
- **Historical Identifier Reference**: `REQ-COORD-001`

---

## REQ-FND-003: Subject Referential Integrity

### Normative Requirement Statement
> **Harmonia shall preserve and validate the integrity of subject references and associations used within managed information, preventing known-invalid or internally inconsistent associations from being silently treated as valid.**

### Architectural Intent & Rationale
Clinical records derive their entire medical validity from their association with the correct human patient subject. In regional health networks, patient identifiers arrive from disparate sources (national IHIs, regional MRNs, local facility patient numbers). Mismatched or corrupted subject references lead directly to patient harm.

Harmonia mandates that any subject reference attached to a managed clinical event must be structurally validated, referentially verified, and checked for internal consistency. Known-invalid identifiers (e.g., failed check-digits, malformed namespaces, or conflicting patient identifiers within a single clinical bundle) must be rejected or explicitly flagged rather than silently propagated as valid.

### Explicit Boundary: Non-MPI Demarcation
- **Not a Master Patient Index (MPI)**: This requirement does **not** establish Harmonia as an MPI or enterprise demographic matching engine.
- **Not a Golden-Record Determiner**: Harmonia does not execute probabilistic matching or merge patient demographics into a synthetic "golden record."
- **Intent**: Preserving the referential integrity, authenticity, and provenance of subject identifiers without performing lossy or speculative demographic unification.

- **Primary Motivation**: [Patient Safety & Identity Integrity](../drivers-assessments/drivers.md)
- **External Constraint**: [Applicable National / Jurisdictional Healthcare Identifier Obligations](external-constraints.md)
- **Governing Principles**: [AX-06 (Explicit Information Authority)](../principles/architectural-axioms.md), [AX-14 (Preservation of Semantic Distinctions)](../principles/architectural-axioms.md)
- **Historical Identifier Reference**: `REQ-IDENT-001`

---

## REQ-FND-004: Explicit Indeterminate Outcome

### Normative Requirement Statement
> **Where Harmonia cannot establish the outcome of a managed operation with sufficient certainty, the outcome shall be represented explicitly as indeterminate and shall not be interpreted as either success or failure without subsequent reconciliation or another authoritative determination.**

### Architectural Intent & Rationale
In distributed networks, communication timeouts, socket disconnects, and unacknowledged dispatches leave the status of remote operations uncertain. In the presence of a network partition or destination crash, a client cannot know whether an update was processed and the ACK was lost, or whether the update was never received.

A frequent architectural failure mode is "binary collapsing": assuming an unconfirmed operation failed (triggering duplicate side effects on blind retry) or assuming it succeeded (ignoring unperformed clinical orders). Harmonia mandates that uncertainty must be preserved and explicitly represented as `INDETERMINATE`.

### Reconciliation & Retry Semantics
- This requirement does **not** prohibit automated retries or resilient recovery.
- It mandates that retry mechanisms must be safe, idempotent, and reconciliation-aware. An indeterminate outcome must be resolved through explicit reconciliation (such as querying destination state or verifying idempotency tokens) rather than blind, speculative assumption.

- **Primary Motivation**: [Silent Data Loss via False Acceptance](../drivers-assessments/assessments.md), [Continuous Clinical Service Availability](../drivers-assessments/drivers.md)
- **Governing Principle**: [AX-15 (Uncertainty Is Preserved Until Resolved)](../principles/architectural-axioms.md)
- **Historical Identifier Reference**: `REQ-UNCERT-001`
