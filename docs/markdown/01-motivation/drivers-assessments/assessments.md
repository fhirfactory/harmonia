# Architectural & Operational Assessments

## Overview

Assessments represent the formal architectural and operational evaluations of systemic hazards, structural risks, and operating conditions arising from platform drivers. In enterprise architecture, an assessment diagnoses *what happens or what fails* if architectural guardrails are absent.

Harmonia recognizes strictly **five agreed assessments**. These assessments identify concrete failure modes and performance hazards observed in distributed healthcare integration environments. Assessments are not manufactured to force artificial symmetry; they exist only where an explicit systemic risk has been analyzed.

---

## 1. Silent Data Loss via False Acceptance

### Assessment Summary
Prematurely acknowledging receipt of an incoming clinical event before responsibility for that event has crossed an authoritative durable boundary creates a critical risk of unrecoverable data loss.

### Operational Hazard
When an ingress component positively acknowledges an incoming operational or clinical event before responsibility for that event has crossed an authoritative durable boundary, upstream senders legitimately consider their delivery obligation satisfied and release local responsibility.

If an unrecoverable failure occurs (such as process termination, node failure, network partition, or power loss) after positive acknowledgement but before durable persistence or guaranteed downstream delivery:
- The upstream sender marks the event as delivered and will not retransmit.
- The receiving platform loses the event representation without having persisted or processed it.
- The clinical event is silently dropped without any error indication or notification.

In clinical practice, this leads directly to missing critical diagnostic results, unrecorded medication orders, or undetected admission events, creating severe risks for patient safety.

#### Downstream Integration Architecture Example
In traditional HL7 v2 and MLLP transport middleware, inbound gateways often return an immediate positive acknowledgement (`MSA-1 = AA`, Application Accept) upon successful memory parsing or volatile buffering. If the gateway process crashes or hardware fails before the message is committed to durable storage, silent data loss occurs because the sender received an `AA` and will never retry. Downstream integration adapters (Domain 06) must enforce durable acceptance boundaries and emit negative acknowledgements (`AE`) when durability is not established.

### Architectural Implication & Response
- Positive acceptance must be strictly coupled to crossing an authoritative durable boundary.
- Ingress boundaries must guarantee that incoming event responsibility is durably accepted before emitting a positive acceptance response to the sender. If durable acceptance cannot be established, an explicit negative or indeterminate response must be emitted so the sender retains retry responsibility.
- **Related Driver**: [Continuous Clinical Service Availability](drivers.md)
- **Target Goal**: [Durable Acceptance & Preservation of Clinical Events](../goals-outcomes/strategic-goals.md)
- **Governing Axioms**: [AX-10 (Failure as Normal Condition)](../principles/architectural-axioms.md), [AX-05 (State Separation)](../principles/architectural-axioms.md), [AX-15 (Explicit Representation of Uncertainty)](../principles/architectural-axioms.md)
- **Foundational Requirement**: [`REQ-FND-001` Durable Ingress Acceptance Boundary](../requirements-constraints/foundational-requirements.md)

---

## 2. Unmonitored Destination Failure / Fan-Out Divergence

### Assessment Summary
Broadcasting clinical events across multiple downstream destination systems without tracking granular, per-destination delivery status leads to silent divergence between destination systems and fractured clinical state.

### Operational Hazard
A single clinical event often requires distribution to multiple disparate destinations (fan-out). For example, an HL7 `ADT^A08` (Update Patient Information) or `ADT^A02` (Transfer Patient) may need to update:
1. The regional clinical data repository.
2. The local pharmacy dispensing system.
3. The radiology information system (RIS).
4. The hospital billing and claims engine.
5. An external primary health network notifications feed.

If the integration platform considers the parent activity "completed" merely because the message was posted to an internal dispatch mechanism, failures at individual destination endpoints go unmonitored:
- The clinical repository is updated, but the pharmacy feed times out.
- The parent workflow reports success.
- The pharmacy continues operating on obsolete patient medication allergy data.
- Systemic clinical state diverges silently across the healthcare network.

### Architectural Implication & Response
- Operational workflows must track explicit progression state per destination.
- Parent activities must maintain fine-grained sub-status across the entire fan-out lifecycle until every individual destination delivery is deterministically confirmed or explicitly flagged as failed/indeterminate.
- **Related Driver**: [Coordinated Operational Activity and Entity State](drivers.md)
- **Target Goal**: [Coordinated Progression of Operational Activities & Associated Entity State](../goals-outcomes/strategic-goals.md)
- **Governing Axiom**: [AX-16 (Operational Activity & Entity State Progress Together)](../principles/architectural-axioms.md)
- **Foundational Requirement**: [`REQ-FND-002` Operational Activity Progression State](../requirements-constraints/foundational-requirements.md)

---

## 3. PHI Leakage through Operational Logging

### Assessment Summary
Emitting unmasked Protected Health Information (PHI) into operational diagnostic logs, monitoring systems, and tracing tools creates severe data breach exposures and violates statutory privacy obligations.

### Operational Hazard
Software developers and integration engineers require diagnostic logging to troubleshoot routing errors, transform bugs, and network timeouts. A common developer practice is logging raw message payloads (e.g., logging an entire raw HL7 message string or complete FHIR resource JSON) at `DEBUG` or `INFO` levels.

In a healthcare environment, these logs are ingested into centralized search clusters, log forwarders, monitoring dashboards, and cloud observability tools:
- Patient names, home addresses, phone numbers, Medicare numbers, and intimate clinical diagnoses become stored in unencrypted, broadly accessible log repositories.
- Operational support personnel and external vendors gain unauthorized access to sensitive clinical data without clinical need-to-know authorization.
- Routine log retention and aggregation triggers substantial statutory data breach notifications under national privacy legislation (e.g., the Australian *Privacy Act 1988* and Notifiable Data Breaches scheme).

### Architectural Implication & Response
- Operational log streams must be structurally segregated from clinical data payloads.
- Diagnostic logs must emit only non-sensitive operational metadata (transaction UUIDs, message control IDs, timestamps, masked identifiers) and strictly prohibit unmasked clinical observation values, demographic text, or free-text clinical notes.
- **Related Driver**: [Statutory Health Information Privacy & Protection](drivers.md)
- **Governing Axiom**: [AX-07 (Security Is Intrinsic to Managed Operations)](../principles/architectural-axioms.md)
- **Derived Guardrail & Requirement**: Classifies downstream as a mandatory Security Architecture requirement (`SEC-010` PHI-Safe Operational Logging), with formal governance guardrails owned under Domain 13.

---

## 4. Implicit Perimeter Trust

### Assessment Summary
Assuming that internal network segments, container fabrics, or service-to-service calls are trustworthy by default leaves the platform vulnerable to lateral movement, credential abuse, and unauthorized clinical data manipulation.

### Operational Hazard
Traditional enterprise architectures frequently rely on perimeter firewalls: once a request crosses the external network boundary, all internal service-to-service communication is unauthenticated, unencrypted, and fully trusted.

In modern distributed and containerized integration environments, perimeter-only trust is hazardous:
- Compromise of a single peripheral container or gateway allows an attacker to make unhindered calls to core clinical repositories.
- Internal integration routes can bypass clinical consent rules and authorization checks because the caller is presumed to be "internal."
- Audit logs cannot prove who authorized a specific data retrieval or state mutation because individual operations lack verifiable caller security context.

### Architectural Implication & Response
- Security must be intrinsic to every governed operation rather than dependent upon voluntary caller behaviour or perimeter assumptions.
- Every internal request, service invocation, and state mutation must execute within an established, verifiable security context subject to platform-enforced security policy.
- Downstream security architecture (Domain 08) derives specific enforcement patterns, such as default-deny authorization and authenticated service communication, from governing axiom AX-07.
- **Related Driver**: [Statutory Health Information Privacy & Protection](drivers.md)
- **Governing Axiom**: [AX-07 (Security Is Intrinsic to Managed Operations)](../principles/architectural-axioms.md)
- **Downstream Requirements**: `SEC-001 / SEC-002` (Trusted Authentication & Default-Deny Authorization).

---

## 5. Centralised Synchronous Persistence Can Constrain Concurrent Processing

### Assessment Summary
Requiring central synchronous persistence interaction for every unit of active processing can introduce contention, latency, unnecessary I/O and coordination bottlenecks that limit concurrent throughput and scalability.

### Operational Hazard
In a high-volume regional integration hub, thousands of clinical messages arrive concurrently from emergency rooms, pathology analyzers, and patient portals. If the architecture forces every active query, routing step, and transient coordination check to execute synchronous disk I/O and transactional row locking against a single central database:
- Transaction lock contention and wait states escalate as concurrency increases.
- Increased I/O wait times can cause ingress queues to fill and upstream connections to time out.
- High-priority emergency point-of-care clinical queries become stalled behind long-running background batch imports.
- Scalability is constrained to the physical I/O limits of the central persistence appliance.

*Note on Technology Neutrality*: This assessment diagnoses the fundamental hazards of synchronous persistence contention, latency, and coordination bottlenecks. It is an architectural evaluation of concurrency dynamics; it does not mandate any specific product, cache, database, or queueing technology.

### Architectural Implication & Response
- The architecture must decouple active operational coordination from authoritative durable persistence.
- High-frequency operational checks and client-facing queries must be served through responsive, concurrent access paths without imposing transactional lock contention on authoritative storage.
- **Related Driver**: [Continuous Clinical Service Availability](drivers.md)
- **Target Goal**: [Responsive Access to Managed Information](../goals-outcomes/strategic-goals.md)
- **Governing Axioms**: [AX-11 (Responsive & Resilient Information Access)](../principles/architectural-axioms.md), [AX-05 (Active Coordination ≠ Authoritative State)](../principles/architectural-axioms.md), [AX-09 (Default Ephemerality of Operational State)](../principles/architectural-axioms.md)
