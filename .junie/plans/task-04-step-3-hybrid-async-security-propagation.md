---
sessionId: session-260922-124236-13st
---

# Requirements

- **Goal / outcome**: Implement the confirmed Hybrid Dual-Layer asynchronous security propagation model across Petasos and Artemis, ensuring authoritative domain payloads (e.g., serialized `Pragma`) govern security context and provenance, eliminating trusted identity manufacture from JMS transport properties, removing unsafe missing-context defaults in Ponos, and resolving broker redelivery duplicate-detection defects.
- **Scope**:
  - In scope: `ArtemisMessageConverter`, `PetasosMessage` envelope semantics, `PetasosQueueToExchangeConduit`, `PragmaWorkflowDispatcher`, `ArtemisPetasosSubscription`, `DuplicateDetector`, focused security/transport/tampering unit tests, and minimal security documentation alignment.
  - Out of scope: Ponos/Ergon Step 4 executing-principal transition or child-task inheritance, Step 5 persistence/recovery redesign, Task 06 immutable AuditEvent behavior, raw legacy JMS path migration (MLLP TaskEvent, Ponos direct JMS fallback), manual/administrative replay implementation, and Artemis TLS/Kubernetes network redesign.
- **Done when**:
  - JMS properties (`harmonia_initiating_principal`, `harmonia_security_domain`) are strictly non-authoritative transport metadata and cannot instantiate a trusted `ThemisPrincipal` or `ThemisSecurityContext`.
  - Serialized domain payloads (Pragma JSON) authoritatively establish originating principal, authorities, and security domain without transport metadata overriding them.
  - Missing security context remains unauthenticated/missing and fails closed at Ponos/Themis authorization boundaries rather than synthesizing default internal service identities.
  - Broker redeliveries following handler failures are not prematurely dropped or acknowledged by client-side duplicate detection.
  - All targeted unit, integration, and architecture tests pass cleanly.

# Technical Design

- **Decisions**:
  - Chose Hybrid Dual-Layer model / not JMS-property-based security — Authoritative domain envelope preserves complete provenance across async boundaries, while JMS properties provide lightweight routing/correlation/diagnostics without acting as an authorization credential.
  - Chose fail-closed missing context / not synthetic service defaults in Ponos — Unauthenticated messages must not silently receive `PROVIDER_CHANGE_SUBMIT` or `SYSTEM_INTEGRATION` authorities; absence must evaluate against Themis default-deny policy.
  - Chose commit-on-success duplicate detection / not record-on-ingress — Duplicate detector must record message IDs only upon successful handling/acknowledgement so broker redelivery on transient failure is not skipped.
  - Chose retaining `harmonia_initiating_principal` as diagnostic transport metadata / not removing it entirely — Preserves operational tracing in broker logs while stripping reverse-conversion identity instantiation.
- **Approach & touches**:
  - `petasos/petasos-artemis/.../converter/ArtemisMessageConverter.java`: Retain outgoing scalar JMS properties; in `toPetasosMessage`, do NOT create `ThemisPrincipal` or set `originatingPrincipal` from `HDR_HARMONIA_INITIATING_PRINCIPAL` or `HDR_HARMONIA_SECURITY_DOMAIN`.
  - `petasos/petasos-api/.../message/PetasosMessage.java`: Ensure builder/constructor does not synthesize trusted security context from untrusted strings; retain existing convenience accessors.
  - `energeia/ponos/.../praxis/conduit/PetasosQueueToExchangeConduit.java`: In `convertToPragma`, preserve deserialized Pragma security fields; remove default service principal (`service:petasos`) and authority synthesis block when principal is null.
  - `energeia/ponos/.../praxis/conduit/PragmaWorkflowDispatcher.java`: Remove fallback synthesis of `service:internal` and submit authorities when `pragma.getOriginatingPrincipal() == null`; pass actual context to Themis authorization (defaulting to DENY).
  - `petasos/petasos-core/.../dedup/DuplicateDetector.java` & `petasos/petasos-artemis/.../consumer/ArtemisPetasosSubscription.java`: Refactor deduplication lifecycle so `seenIds` records messages upon successful handler completion/acknowledgement rather than pre-execution, ensuring `session.recover()` redelivery delivers to handler.
  - `docs/security/pragma-security.md` & `docs/security/ponos-security.md`: Update documentation to describe the Hybrid Dual-Layer model and classify raw JMS paths as legacy non-canonical.
- **Nuances / risks / corners**:
  - In `PetasosQueueToExchangeConduit.convertToPragma`, copying Petasos metadata must not overwrite existing authoritative Pragma fields (e.g. `correlationId`, `causationId`, `source`) if already present in the deserialized JSON.
  - In `DuplicateDetector`, ensure thread safety and LRU window bounds are preserved when recording happens post-execution or via explicit status callbacks.
  - Agora collaboration events carry security context within the domain event body (`AgoraCollaborationEvent`) and do not use Pragma; ensure reverse converter changes do not break Agora event consumption.
- **Suspected areas**:
  - `ArtemisMessageConverter.java:258-264`: Instantiates `ThemisPrincipal.of(initiatingPrincipalId, PrincipalType.HUMAN, securityDomain)` and assigns it to PetasosMessage originating principal.
  - `ArtemisPetasosSubscription.java:105-113`: Calls `duplicateDetector.isUnique(dedupId)` before `handler.onMessage()`, polluting the cache on failed attempts.
  - `PetasosQueueToExchangeConduit.java:225-234`: Injects default `service:petasos` principal and submit authorities when `originatingPrincipal` is null.
  - `PragmaWorkflowDispatcher.java:171-178`: Substitutes `service:internal` and submit authorities when `originatingPrincipal` is null.

# Testing

- Must-hold: JMS property tampering (e.g. setting `harmonia_initiating_principal = "mallory"`) does not create a `ThemisPrincipal` or override authoritative Pragma context (`HUMAN:dr.mark`).
- Must-hold: Pragma JSON round-trip through `PetasosMessage` -> `ArtemisMessageConverter` -> JMS -> `PetasosMessage` -> `PetasosQueueToExchangeConduit` preserves initiating principal, authorities, security domain, correlation ID, causation ID, and `requestedAt`.
- Must-hold: Unauthenticated messages lacking domain security context remain null/empty and result in `DENY` from `PragmaWorkflowDispatcher` without receiving synthetic authorities.
- Must-hold: Handler failure triggering `session.recover()` allows subsequent broker redelivery to be processed rather than swallowed by `DuplicateDetector`.
- Must-hold: Genuine duplicate message after successful processing and acknowledgment is detected and skipped.
- Regression target: Petasos Artemis adapter suite (`ArtemisMessageConverterTest`, `ArtemisConnectionManagerTest`).
- Regression target: Ponos conduit and workflow dispatcher suites (`PetasosQueueToExchangeConduitTest`, `PragmaWorkflowDispatcherSecurityTest`).
- Regression target: Architecture tests (`ParadeigmaIsolationArchitectureTest`, `PetasosApiIsolationArchitectureTest`, `SecurityEnforcementArchitectureTest`, `AgoraIsolationArchitectureTest`).

# Assumptions & Open Questions

- **Significant assumptions**:
  - `harmonia_initiating_principal` and `harmonia_security_domain` are retained on outgoing JMS messages for broker-level diagnostic/operational logging, but are classified as non-authoritative transport metadata. (Alternative: strip them completely; chosen option balances operational troubleshooting without creating security risk).
  - Raw JMS paths (`TaskQueueProducerService` direct-JMS fallback, `pylai-mllp-base/TaskEventProducerService`, and `pylai-mllp-out/OutboundTaskQueueConsumer`) remain documented as legacy non-canonical transports without modification in Step 3.

# Delivery Steps

### ✓ Step 1: Petasos & Artemis Transport Hardening
Goal: Remove trusted identity reconstruction from JMS transport properties in Artemis converter and tighten Petasos envelope security semantics.
Scope: `petasos/petasos-artemis/src/main/java/net/fhirfactory/harmonia/petasos/artemis/converter/ArtemisMessageConverter.java`, `petasos/petasos-api/src/main/java/net/fhirfactory/harmonia/petasos/api/message/PetasosMessage.java`, and `petasos/petasos-artemis/src/test/java/net/fhirfactory/harmonia/petasos/artemis/converter/ArtemisMessageConverterTest.java`.
Acceptance Criteria:
- [ ] `ArtemisMessageConverter.toPetasosMessage` does not construct a `ThemisPrincipal` or populate `PetasosMessage.originatingPrincipal` from `HDR_HARMONIA_INITIATING_PRINCIPAL` or `HDR_HARMONIA_SECURITY_DOMAIN`.
- [ ] `ArtemisMessageConverter.toJmsMessage` preserves outgoing scalar operational JMS headers (`messageId`, `correlationId`, `causationId`, `messageType`, `source`, `destination`, `timestamp`, `durable`, `priority`, `duplicateDetectionId`, `harmonia_correlation_id`) and non-authoritative diagnostic headers (`harmonia_initiating_principal`, `harmonia_security_domain`).
- [ ] Unit tests in `ArtemisMessageConverterTest` verify complete transport metadata round-trip and prove that tampering with JMS principal/domain headers cannot manufacture a trusted `ThemisPrincipal` or `ThemisSecurityContext`.
Verification: `mvn test -pl petasos/petasos-artemis,petasos/petasos-api -am` → green

### ✓ Step 2: Ponos Conduit & Dispatcher Missing-Context Hardening
Goal: Ensure authoritative domain payloads govern Pragma security context and eliminate dangerous missing-context fallbacks in Ponos conduit and dispatcher.
Scope: `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/conduit/PetasosQueueToExchangeConduit.java`, `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/conduit/PragmaWorkflowDispatcher.java`, `energeia/ponos/src/test/java/net/fhirfactory/harmonia/praxis/conduit/PetasosQueueToExchangeConduitTest.java`, and `energeia/ponos/src/test/java/net/fhirfactory/harmonia/praxis/conduit/PragmaWorkflowDispatcherSecurityTest.java`.
Acceptance Criteria:
- [ ] `PetasosQueueToExchangeConduit.convertToPragma` deserializes authoritative Pragma/FHIR Task JSON payloads without overwriting domain security context with transport defaults.
- [ ] Unauthenticated messages lacking domain security context are NOT assigned synthetic `service:petasos` identities or `PROVIDER_CHANGE_SUBMIT` / `SYSTEM_INTEGRATION` authorities in `PetasosQueueToExchangeConduit`.
- [ ] `PragmaWorkflowDispatcher.dispatchPragma` does not manufacture `service:internal` identities or default authorities when `originatingPrincipal` is null; unauthenticated requests evaluate with missing context and fail closed (`DENY`).
- [ ] Focused tests in `PetasosQueueToExchangeConduitTest` and `PragmaWorkflowDispatcherSecurityTest` assert domain payload primacy, absence preservation, and fail-closed dispatcher behavior.
Verification: `mvn test -pl energeia/ponos -am` → green

### ✓ Step 3: Broker Redelivery & Duplicate-Detection Lifecycle Fix
Goal: Fix message deduplication lifecycle in Petasos subscription so broker redelivery following processing failure is delivered rather than dropped.
Scope: `petasos/petasos-core/src/main/java/net/fhirfactory/harmonia/petasos/core/dedup/DuplicateDetector.java`, `petasos/petasos-artemis/src/main/java/net/fhirfactory/harmonia/petasos/artemis/consumer/ArtemisPetasosSubscription.java`, `petasos/petasos-core/src/test/java/net/fhirfactory/harmonia/petasos/core/dedup/DuplicateDetectorTest.java`, and new/updated subscription tests.
Acceptance Criteria:
- [ ] `DuplicateDetector` provides distinct inspection (`isDuplicate`) and confirmation (`record` / `markProcessed` / `removeOnFailure`) mechanisms.
- [ ] `ArtemisPetasosSubscription` does not permanently record a message as processed before `handler.onMessage()` successfully executes.
- [ ] Handler failures triggering `session.recover()` allow Artemis redelivery with same `messageId`, `correlationId`, `causationId`, and incremented `deliveryCount` without being skipped as a duplicate.
- [ ] Genuine duplicate messages received after successful acknowledgment are identified and skipped.
- [ ] Unit tests in `DuplicateDetectorTest` and `ArtemisPetasosSubscriptionTest` verify first-delivery success, redelivery after failure, and duplicate suppression after success.
Verification: `mvn test -pl petasos/petasos-core,petasos/petasos-artemis -am` → green

### ✓ Step 4: End-to-End Regression, Architecture Invariants & Documentation
Goal: Validate full system integration, confirm architectural invariants across modules, and document the Hybrid Dual-Layer model and legacy path boundaries.
Scope: `docs/security/pragma-security.md`, `docs/security/ponos-security.md`, and project test suites.
Acceptance Criteria:
- [ ] Documentation in `docs/security/` reflects the Hybrid Dual-Layer model and documents raw JMS paths (`TaskQueueProducerService` fallback, MLLP `TaskEventProducerService`, MLLP `OutboundTaskQueueConsumer`) as non-canonical legacy transports.
- [ ] ArchUnit test suites (`PetasosApiIsolationArchitectureTest`, `SecurityEnforcementArchitectureTest`, `ParadeigmaIsolationArchitectureTest`, `AgoraIsolationArchitectureTest`) pass with zero violations.
- [ ] End-to-end regression suites across Pylai FHIR Registry, Agora, Ponos, and Petasos pass cleanly.
Verification: `mvn test -pl paradeigma/paradeigma-test,agora/agora-service,pylai/pylai-fhir-registry,energeia/ponos,petasos/petasos-artemis -am -Dtest="*ArchitectureTest,Agora*,*Test" -Dsurefire.failIfNoSpecifiedTests=false` → green