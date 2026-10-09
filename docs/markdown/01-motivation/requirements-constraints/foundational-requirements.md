# Foundational Platform Requirements

## Overview

Foundational Platform Requirements represent the enduring, non-negotiable architectural obligations owned directly by Domain 01 Motivation. Unlike detailed subsystem requirements (which are owned downstream by Business, Application, Integration, and Security domains), foundational requirements express cross-cutting platform invariants that apply across all Harmonia capabilities.

This catalogue contains **five foundational requirements**: `REQ-FND-001` through `REQ-FND-004` retain their Accepted status; `REQ-FND-005` is **APPROVED**, the authorised Independent Assurance reconciliation is **APPROVED / CLOSED**, and Domain01 is again **CLOSED / FROZEN**. Each requirement is stated using a protocol-neutral normative formulation, ensuring that implementation choices and wire protocols are recognized as downstream realisations rather than foundational definitions. The [reconciliation record](../reviews/independent-assurance-reconciliation.md) records the deliberate reopening and human approval/refreeze decision.

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
- **Governing Principles**: [AX-05 (State Separation)](../../governance/architectural-axioms.md#ax-05), [AX-10 (Failure as Normal Condition)](../../governance/architectural-axioms.md#ax-10), [AX-15 (Explicit Representation of Uncertainty)](../../governance/architectural-axioms.md#ax-15)
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
- **Governing Principle**: [AX-16 (Operational Activity & Entity State Progress Together)](../../governance/architectural-axioms.md#ax-16)
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
- **Governing Principles**: [AX-06 (Explicit Information Authority)](../../governance/architectural-axioms.md#ax-06), [AX-14 (Preservation of Semantic Distinctions)](../../governance/architectural-axioms.md#ax-14)
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
- **Governing Principle**: [AX-15 (Uncertainty Is Preserved Until Resolved)](../../governance/architectural-axioms.md#ax-15)
- **Historical Identifier Reference**: `REQ-UNCERT-001`

---

## REQ-FND-005: Independent Assurance of Governed Activity

**Classification:** Foundational Platform Requirement. **Status:** APPROVED — human architectural approval recorded on 2026-10-08; normative wording and relationships unchanged. The authorised Independent Assurance reconciliation is APPROVED / CLOSED; Domain01 Motivation Architecture is CLOSED / FROZEN.

### Normative Requirement Statement

> **Harmonia shall support independent governed assurance of activities, information and outcomes for which assurance is required, such that assurance conclusions are established from sufficient and trustworthy evidence against applicable obligations, expectations or controls.**
>
> **The activity or mechanism responsible for performing or managing the subject being assured shall not be solely responsible for determining, suppressing, manufacturing or retrospectively altering its assurance outcome. Harmonia shall establish sufficient separation of assurance responsibility and authority from responsibility for performance or control of the subject so that assurance is not merely self-attestation by the subject being assured.**
>
> **Assurance may identify findings, exceptions and the need for operational response, but shall not thereby assume responsibility for management, assignment, delegation, escalation or remediation of the activity being assured.**
>
> **Assurance may manage its own assurance activity without thereby assuming management of the subject activity.**
>
> **Where available evidence is insufficient to establish an assurance conclusion with the required degree of confidence, that insufficiency shall remain explicit and shall not be interpreted as either assurance or non-assurance.**

### Architectural Intent & Rationale

Those accountable for health-information handling and operational activity need credible grounds for demonstrating that applicable obligations, expectations or controls have been met. The [Regional Health Network Operator's compliance and forensic-review concerns](../stakeholders/enterprise-stakeholders.md#1-regional-health-network-operator), [engineers' incident review and deterministic verification concerns](../stakeholders/enterprise-stakeholders.md#6-platform-operations--integration-engineers), and [O3's demonstrable protection and accountable handling](../goals-outcomes/business-outcomes.md#3-demonstrable-protection-and-accountable-handling-of-health-information-o3) already establish the value of substantive review and trustworthy evidence.

Those existing concerns do not themselves establish performer independence. The approved reconciliation adds that obligation explicitly: a conclusion controlled solely by the activity or mechanism being assured can conceal findings or manufacture confidence and cannot provide the required independent assurance. Evidence availability and operational completion alone cannot establish whether the applicable obligations, expectations or controls have been satisfied.

### Applicability & Responsibility Boundary

The requirement applies where assurance is required. It does not require assurance of every activity, information item or outcome. The criteria establishing applicability, applicable obligations and the required degree of confidence remain for subsequent governed derivation; this requirement does not define an applicability model.

Independence concerns responsibility and authority. It does not prescribe physical, technical or organisational separation, nor require external assurance. It does not allocate an assessor or prohibit the subject's performer from contributing evidence. Assurance manages its own work and may inform an operational response; responsibility for managing that response remains distinct. The explanatory shorthand is: **the assurance capability is the QA service, not the Job Foreman.**

### Semantic & Uncertainty Boundaries

The following distinctions constrain this requirement; they do not define new information types or an assurance outcome taxonomy:

```text
Provenance ≠ Assurance
Audit information ≠ Assurance
Evidence ≠ Assurance conclusion
Operational progression ≠ Assurance
Workflow completion ≠ Assurance
Business Outcome ≠ Assurance Outcome
Operational uncertainty ≠ Insufficient assurance evidence
Independent assurance ≠ External assurance
Assurance finding ≠ Operational response
Assurance responsibility ≠ Operational management responsibility
```

[`REQ-FND-004`](#req-fnd-004-explicit-indeterminate-outcome) and [AX-15](../../governance/architectural-axioms.md#ax-15) continue to govern uncertainty about an operation's state, effect or outcome, including an assurance activity's own execution where applicable. Separately, an assurance activity may execute correctly yet lack sufficient evidence to establish a conclusion about its subject. This requirement preserves that insufficiency without treating it as an uncertain execution outcome, execution failure, assurance or non-assurance. Neither existing operational rule is rewritten or extended into an assurance-evidence sufficiency rule.

### Motivation Relationships

These are branching relationships, not a sequential motivation pipeline. The independence, management boundary and evidence-insufficiency obligations are newly approved intent for reconciliation, not meanings retrospectively attributed to the reused elements.

| Existing element | Relationship to `REQ-FND-005` | Semantic limit |
| :--- | :--- | :--- |
| [Regional Health Network Operator](../stakeholders/enterprise-stakeholders.md#1-regional-health-network-operator) | Compliance demonstration and forensic-review concerns motivate the requirement directly. | No new Operator-to-Privacy-Driver relationship or statutory independence mandate is inferred. |
| [Platform Operations & Integration Engineers](../stakeholders/enterprise-stakeholders.md#6-platform-operations--integration-engineers) | Incident review and deterministic verification concerns motivate the requirement directly. | Operational diagnosis and monitoring remain distinct from independent assurance. |
| [Statutory Health Information Privacy & Protection Driver](../drivers-assessments/drivers.md#5-statutory-health-information-privacy--protection) | Supports the requirement's need to evaluate handling against applicable obligations and controls. | Privacy supplies one valid motivation, not the entire assurance scope or independence obligation. |
| [O3](../goals-outcomes/business-outcomes.md#3-demonstrable-protection-and-accountable-handling-of-health-information-o3) | The requirement supports O3's existing demonstrability and accountability purpose. | Textual contribution only; no new causal handover, intermediate Goal or assurance-outcome equivalence. |
| [CST-EXT-001](external-constraints.md#cst-ext-001-applicable-health-information-privacy-and-data-protection-obligations) | Bounds assurance concerning health-information privacy and protection by applicable obligations. | No universal external requirement for independent assurance is claimed. |
| [AX-06](../../governance/architectural-axioms.md#ax-06) | Co-governs interpretation of evidence authority and credibility independently of technical state. | Information authority does not establish assessor independence. |
| [AX-07](../../governance/architectural-axioms.md#ax-07) | Co-governs security context and policy for assurance activity. | Security enforcement does not establish independent conclusion authority. |
| [AX-08](../../governance/architectural-axioms.md#ax-08) | Co-governs meaningful provenance and audit evidence used in assurance. | Preserving evidence does not perform assurance or establish a conclusion. |
| [AX-14](../../governance/architectural-axioms.md#ax-14) | Co-governs preservation of the requirement's semantic distinctions. | Distinct meanings do not prescribe separate technologies or information types. |

[AX-09](../../governance/architectural-axioms.md#ax-09) remains an evidence-selection and retention boundary: observations and progression state are not automatically provenance or audit evidence. [`REQ-FND-002`](#req-fnd-002-operational-activity-progression-state) can supply observable operational facts; it is a complementary evidence relationship, not an independence parent or assurance conclusion. `REQ-FND-004` / AX-15 have the complementary uncertainty relationship described above. [AX-17](../../governance/architectural-axioms.md#ax-17) governs this explicit reconciliation and preservation of unresolved downstream derivation; it is not a source of the real-world assurance need.

### Downstream Derivation Boundary

The requirement supplies upstream motivation from which Strategy responsibility, Business assurance behaviour and Information Architecture may subsequently be derived, followed by Application Architecture and an eventual solution. Those relationships remain unestablished here. No capability allocation, workflow specification, solution construct, outcome values, evidence model, execution mechanism or deployment boundary is defined by this requirement.
