# Pragma Security Context & Asynchronous Transport Security

## 1. Immutable Originating Context

A `Pragma` represents an asynchronous, state-tracked unit of work within Harmonia. It carries immutable security context established at the boundary where the request entered the system:

```java
public class Pragma {
    private ThemisPrincipal originatingPrincipal;
    private final Set<ThemisAuthority> originatingAuthorities;
    private ThemisSecurityContext originatingSecurityContext;
    private String policyVersion;
}
```

---

## 2. Serialization to FHIR R5 Task Extensions & JSON

When `Pragma` is persisted or transmitted as a FHIR R5 `Task`, its security context is mapped to standardized extensions:

```
http://harmonia.net/fhir/StructureDefinition/security-principal-id
http://harmonia.net/fhir/StructureDefinition/security-principal-type
http://harmonia.net/fhir/StructureDefinition/security-source-domain
http://harmonia.net/fhir/StructureDefinition/security-authority
http://harmonia.net/fhir/StructureDefinition/security-policy-version
```

When transmitted across Petasos messaging channels, `Pragma` is serialized as JSON in the message body (`PetasosMessage.getPayload()`), preserving complete provenance, originating security context, authorities, and correlation metadata.

---

## 3. Hybrid Dual-Layer Asynchronous Security Propagation Model

Harmonia messaging across Petasos and ActiveMQ Artemis enforces the **Hybrid Dual-Layer Security Model**:

```
┌────────────────────────────────────────────────────────────────────────┐
│ Layer 1: Authoritative Domain Payload (Opaque Body)                   │
│  - Serialized Pragma JSON or AgoraCollaborationEvent JSON             │
│  - Establishes originatingPrincipal, originatingAuthorities,         │
│    originatingSecurityContext, securityDomain, correlationId,         │
│    causationId, and requestedAt timestamp.                            │
│  - Strictly authoritative; governs downstream Themis policy gates.    │
└──────────────────────────────────┬─────────────────────────────────────┘
                                   │
┌──────────────────────────────────▼─────────────────────────────────────┐
│ Layer 2: Non-Authoritative Transport Metadata (JMS Headers/Properties)│
│  - harmonia_initiating_principal, harmonia_security_domain             │
│  - petasos_message_id, petasos_correlation_id, petasos_causation_id   │
│  - Used strictly for operational routing, broker tracing, diagnostics │
│  - CANNOT instantiate a trusted ThemisPrincipal or ThemisSecurityCtx  │
└────────────────────────────────────────────────────────────────────────┘
```

### Key Principles & Invariants

1. **Payload Primacy**: The domain/work payload inside the message body is the single source of truth for security context and provenance. Transport-layer metadata cannot override or mutate domain context.
2. **No Trusted Identity from Transport**: In `ArtemisMessageConverter`, deserializing JMS properties (such as `harmonia_initiating_principal` or `harmonia_security_domain`) never instantiates a `ThemisPrincipal` or populates `PetasosMessage.originatingPrincipal`. Transport headers are treated strictly as non-authoritative diagnostics metadata.
3. **Fail-Closed Missing Context**: If a message body lacks security context (e.g., non-Pragma payloads or missing originating principal), downstream processors do not synthesize internal service identities (`service:petasos`, `service:internal`) or default authorities (`provider.change.submit`). Missing context evaluates against Themis default-deny policy and fails closed (`DENY`).

---

## 4. Duplicate-Detection & Broker Redelivery Lifecycle

Petasos subscriptions (`ArtemisPetasosSubscription`) and deduplication (`DuplicateDetector`) implement a **commit-on-success** lifecycle:

1. **Inspection Prior to Handling**: `DuplicateDetector.isDuplicate(dedupId)` checks whether a message was already successfully processed without prematurely marking it as processed in the LRU cache.
2. **Record on Success**: `DuplicateDetector.record(dedupId)` (or `markProcessed`) is invoked only upon successful handler completion and message acknowledgment.
3. **Broker Redelivery on Failure**: When a message handler throws an exception or calls `session.recover()`, the message ID is evicted or left unrecorded. The Artemis broker redelivers the message with identical `messageId`, `correlationId`, `causationId`, payload, and an incremented `JMSXDeliveryCount`, allowing processing to retry rather than being dropped by client-side deduplication.
4. **Duplicate Suppression**: Genuine duplicates arriving after successful acknowledgment are identified and skipped without duplicate handler execution.

---

## 5. Non-Canonical Legacy Transport Paths

The following transport paths remain unmigrated in Step 3 and are explicitly classified as **non-canonical legacy transports**:

| Legacy Path | Component | Transport Mechanism | Security & Operational Limitations |
| :--- | :--- | :--- | :--- |
| **Ponos Direct JMS Fallback** | `TaskQueueProducerService` | Raw JMS `TextMessage` when `ArtemisPonosProducer` is unavailable | Bypasses Petasos envelope abstraction; carries no `ThemisSecurityContext` or originating authorities; lacks Petasos duplicate detection. |
| **MLLP Inbound TaskEvent** | `TaskEventProducerService` | Raw JMS `TextMessage` containing `ErgonEvent` JSON + HIE headers | Bypasses Petasos converter; `ErgonEvent` lacks principal, authority, and security context fields; correlation/causation metadata is incomplete. |
| **MLLP Outbound Consumer** | `OutboundTaskQueueConsumer` | Direct JMS consumer with `AUTO_ACKNOWLEDGE` | Consumes raw `TextMessage`/`BytesMessage`; bypasses Petasos subscription lifecycle, redelivery counting, and DLQ handling; ignores transport security headers. |

Any workflows traversing these legacy paths operate outside the governed Hybrid Dual-Layer security model and must undergo downstream authentication/authorization before accessing clinical or administrative resources.

---

## 6. Security Invariants & Deferred Scope

### Core Security Invariants
1. **Unforgeable at Boundary**: Gateway submission logic populates originating security fields strictly from authenticated HTTP request context, preventing clients from overriding credentials.
2. **Tamper Detection**: If serialized security claims are corrupted or stripped in transit, downstream evaluators fail closed (`DENY`). Tampering with JMS properties cannot escalate privileges or create trusted principals.
3. **No Credential Leakage**: Tokens, sessions, and passwords are never serialized into `Pragma` extensions or transport headers.

### Deferred Scope
- **Executing-Principal Transition & Child-Task Inheritance**: Transitioning executing identity to activity-level process principals (`process:ponos-engine`) while preserving originating human provenance is deferred to Step 4 of the architectural roadmap.
- **Durable Persistence & Recovery Redesign**: Long-term state recovery and Mneme/Mnemosyne persistence resilience are deferred to Step 5.
- **Audit Lineage**: Non-PHI immutable `AuditEvent` generation is managed under Task 06.
- **Transport Hardening**: ActiveMQ Artemis TLS transport encryption and Kubernetes network-level mutual TLS are deferred to platform infrastructure hardening.
