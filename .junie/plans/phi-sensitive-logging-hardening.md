---
sessionId: session-260923-074721-1epq
---

# Requirements

### Goal / Outcome
Harden Harmonia logging across all subprojects so that operational logs (INFO, WARN, ERROR) and standard output streams strictly contain safe operational metadata without Protected Health Information (PHI) or raw payload content. Retain controlled, diagnostically justified PHI payload logging exclusively through the established `PhiLogger` mechanism at DEBUG/TRACE under an explicit runtime gate (`PhiLoggingConfig`, disabled by default). Ensure credentials, secrets, and authorization headers are never logged at any level.

### Scope
- **In scope**:
  - `energeia/ponos`: Remediate `TaskMessageProcessor` and `TaskEventMessageProcessor` by removing `System.out.println` and raw FHIR/Ergon payload logging at INFO/ERROR; route diagnostic payload inspection to `PhiLogger` at DEBUG/TRACE; preserve operational metadata.
  - `hestia/mneme-persistence`: Remediate `OperationsRestClient` by removing raw HTTP response bodies from ERROR/WARN logs and logging safe HTTP/service metadata and failure categories.
  - `pylai/pylai-mllp-in`: Remove uncontrolled SLF4J DEBUG raw HL7 message logging from inbound wrappers (`IncomingAdtMessageProcessorWrapper`, `IncomingMfnMessageProcessorWrapper`); sanitize exception logging in inner HL7 processors (`IncomingAdtMessageProcessor`, `IncomingMfnMessageProcessor`, `IncomingOrmMessageProcessor`, `IncomingOruMessageProcessor`).
  - `themis/themis-api`: Harden `ThemisPrincipal` and `ThemisSecurityContext` string representation (`toString()`) to prevent accidental credential, attribute map, or authority leakage while preserving immutability, accessors, and serialization contracts.
  - `petasos/petasos-artemis` and `energeia/ponos`: Sanitize exception logging in `ArtemisPetasosProducer`, `ArtemisPetasosSubscription`, `PetasosQueueToExchangeConduit`, and `PragmaWorkflowDispatcher` to avoid dumping raw payloads or untrusted exception messages.
  - `agora/agora-matrix` and `agora/agora-core`: Prevent upstream HTTP error response bodies from becoming exception messages in `MatrixRestException` and `SynapseAdminException`; sanitize retry and deactivation logging in `AgoraIdentityService`.
  - Production logging config: Verify `PhiLoggingConfig` default-off behavior and ensure no production configuration enables unsafe wire/framework payload logging.
  - `paradeigma/paradeigma-test`: Add architecture tests in `SecurityEnforcementArchitectureTest` enforcing anti-regression rules for sensitive logging.
- **Out of scope**:
  - Broad framework reconfigurations where no unsafe production setting exists.
  - Redesigning Themis authorization, policy engines, or security context propagation (Task 04).
  - Modifying Task 05 Step 01 CORS architecture.
  - Task 06 (AuditEvent immutability), Task 07 (volatile persistence fallback), or Tasks 08–10 (clinical workflow).
  - Generic regex-based post-hoc log sanitization.

### Done When
- No production message processor invokes `System.out.println` or `System.err.println`.
- No operational log statement (INFO, WARN, ERROR) emits raw FHIR resources, HL7 messages, HTTP response bodies, or Ergon event payloads.
- PHI diagnostic logging is exclusively routed via `PhiLogger` at DEBUG/TRACE and remains disabled by default (`harmonia.logging.phi-enabled=false`).
- Credentials, bearer tokens, and Authorization headers are never emitted at any log level.
- `ThemisPrincipal.toString()` and `ThemisSecurityContext.toString()` emit safe structural summaries without dumping raw attributes or authorities.
- Exception messages across REST clients, HL7 gateways, Artemis transports, and Agora services do not re-emit raw payloads into operational logs or stack traces.
- Architecture guardrails in `SecurityEnforcementArchitectureTest` and all existing unit/integration test suites pass green.

# Technical Design

### Decisions
- **Chose explicit safe `toString()` overrides on `ThemisSecurityContext` and `ThemisPrincipal` records / not converting records to classes or altering record components**: Preserves Java record semantics, deconstruction, equals/hashCode, accessors, and serialization contracts while preventing accidental attribute map and authority dumping during string formatting or logging.
- **Chose removing raw HL7 message body logging from MLLP wrappers / not adding a second `PhiLogger` instance in wrappers**: The inner processors (`Incoming*MessageProcessor`) already route deliberate diagnostics through `PhiLogger` at DEBUG with dual gates; removing raw SLF4J DEBUG logging from the wrappers eliminates uncontrolled payload emission without creating duplicate diagnostic events.
- **Chose constraining `MatrixRestException` and `SynapseAdminException` message construction to HTTP status, errcode, and error description / not passing raw response bodies into exception messages or using regex redaction**: Treating exception messages as data at the source prevents raw upstream HTML/JSON bodies from leaking into `getMessage()`, operational logs, or stack traces.
- **Chose removing raw HTTP response bodies from `OperationsRestClient` ERROR logs and logging target, operation, status code, and failure category / not attempting post-hoc JSON scrubbing**: Fails closed and guarantees zero clinical payload or OperationOutcome leakage into operational logs.
- **Chose targeted Logback `ListAppender` logging assertions in each module's unit tests / not importing Paradeigma into production modules**: Respects Invariant 1 (`Production Code -> Paradeigma` is strictly forbidden) while achieving high-fidelity log capture and assertion.

### Approach & Touches
- **Reference Analog**: `calliope/src/main/java/net/fhirfactory/harmonia/logging/PhiLoggingConfig.java` and `calliope/src/test/java/net/fhirfactory/harmonia/logging/PhiLoggingGateTest.java` serve as the design template for dual-gate PHI control and log capture testing via Logback `ListAppender`.
- **Target Touches**:
  - `themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisPrincipal.java`: Override `toString()` to summarize principal identity and attribute count.
  - `themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisSecurityContext.java`: Override `toString()` to summarize principals, correlationId, causationId, tenantId, authority count, and attribute count.
  - `themis/themis-api/src/test/java/net/fhirfactory/harmonia/themis/api/model/ThemisSecurityContextTest.java`: Add tests verifying safe string representation.
  - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/OperationsRestClient.java`: Replace `response.body()` and raw `e.getMessage()` logging with structured metadata (target, operation, status, URI, safe error category).
  - `hestia/mneme-persistence/src/test/java/net/fhirfactory/harmonia/persistence/client/OperationsRestClientLoggingTest.java`: New unit test asserting zero response body or PHI marker leakage at ERROR/WARN.
  - `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/camel/TaskMessageProcessor.java`: Strip `System.out.println`, replace INFO/ERROR payload dumps with safe Task metadata, route payload diagnostics to `PhiLogger.debug(...)`.
  - `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/camel/TaskEventMessageProcessor.java`: Strip `System.out.println`, replace INFO/ERROR payload dumps with safe ErgonEvent metadata, route payload diagnostics to `PhiLogger.debug(...)`.
  - `energeia/ponos/src/test/java/net/fhirfactory/harmonia/praxis/camel/TaskMessageProcessorTest.java`: Add logging-capture assertions for safe metadata and PHI marker suppression.
  - `energeia/ponos/src/test/java/net/fhirfactory/harmonia/praxis/camel/TaskEventMessageProcessorLoggingTest.java`: New test verifying payload suppression and safe metadata.
  - `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/camel/IncomingAdtMessageProcessorWrapper.java` and `IncomingMfnMessageProcessorWrapper.java`: Remove raw HL7 body DEBUG logging.
  - `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/hl7/Incoming*MessageProcessor.java`: Replace raw `e.getMessage()` in error log statements with safe exception class and error code.
  - `petasos/petasos-artemis/src/main/java/net/fhirfactory/harmonia/petasos/artemis/producer/ArtemisPetasosProducer.java` and `consumer/ArtemisPetasosSubscription.java`: Sanitize exception logging at WARN/ERROR.
  - `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/conduit/PetasosQueueToExchangeConduit.java` and `PragmaWorkflowDispatcher.java`: Sanitize exception logging.
  - `agora/agora-matrix/src/main/java/net/fhirfactory/harmonia/agora/matrix/client/MatrixClientAdapter.java` and `admin/SynapseAdministrationGateway.java`: Format `MatrixRestException` and `SynapseAdminException` with status, errcode, and error description instead of raw body.
  - `agora/agora-matrix/src/main/java/net/fhirfactory/harmonia/agora/matrix/client/MatrixRestException.java` and `admin/SynapseAdminException.java`: Update constructors/formatting to safeguard exception messages.
  - `agora/agora-core/src/main/java/net/fhirfactory/harmonia/agora/core/identity/AgoraIdentityService.java`: Log status and errcode in retry warning.
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`: Add architecture checks preventing `System.out.println` in message processors, raw response-body logging in `OperationsRestClient`, and verifying safe security context `toString()`.

### Nuances, Risks & Corners
- **Operational Utility vs PHI Redaction**: Do not over-redact operational identifiers. Correlation ID, causation ID, Task ID, Pragma ID, message control ID, HTTP status, and principal ID are vital for production observability and triage.
- **Exception Messages as Data**: Upstream errors (e.g. from Synapse, HAPI FHIR, or Artemis) frequently echo back full request bodies or patient names. Formatting exception messages with safe summaries at construction time prevents leakage down call stacks and into log appenders.
- **Dual-Gate PHI Semantics**: `PhiLogger` requires both `PhiLoggingConfig.isPhiEnabled()` to be true AND the underlying logger to be at DEBUG/TRACE. Enabling one without the other results in zero output, preventing accidental leakage.
- **Production Isolation (Invariant 1)**: Production modules (`themis`, `hestia`, `energeia`, `pylai`, `petasos`, `agora`) must NEVER import Paradeigma testing utilities (such as `PhiLogTestProbe`). Tests in production modules must use standard Logback `ListAppender` instances attached programmatically in `@BeforeEach`/`@AfterEach`.

### Contracts
Safe `toString()` contracts for security objects:
```java
// ThemisPrincipal.toString() format
"ThemisPrincipal[principalId=" + principalId + ", type=" + principalType + ", sourceDomain=" + sourceDomain + ", attributeCount=" + attributes.size() + "]"

// ThemisSecurityContext.toString() format
"ThemisSecurityContext[originatingPrincipal=" + originatingPrincipal + ", executingPrincipal=" + executingPrincipal + ", domain=" + securityDomain + ", correlationId=" + correlationId + ", causationId=" + causationId + ", tenantId=" + tenantId + ", authoritiesCount=" + authorities.size() + ", attributeCount=" + attributes.size() + "]"
```

# Testing

- Must-hold: Synthetic PHI marker (`PATIENT-PHI-MARKER-92831`) in Task, HL7 message, Ergon event, or REST response body NEVER appears in captured log events at INFO, WARN, or ERROR across any module.
- Must-hold: Synthetic credential/token marker (`TOKEN-SECRET-MARKER-81742`) NEVER appears in any captured log event at any level, including DEBUG and TRACE.
- Must-hold: `ThemisPrincipal.toString()` and `ThemisSecurityContext.toString()` contain principalId, correlationId, and collection counts, but do not contain attribute map contents or raw authority code sets.
- Must-hold: When `PhiLoggingConfig.isPhiEnabled()` is false (default), `PhiLogger.debug(...)` and `PhiLogger.trace(...)` emit zero log events.
- Must-hold: When `PhiLoggingConfig.isPhiEnabled()` is true and logger level is DEBUG/TRACE, `PhiLogger` emits events to `org.harmonia.phi` with the `PHI` marker attached.
- Must-hold: Upstream synthetic exceptions containing PHI markers in their message do not propagate the marker into operational WARN/ERROR log statements.
- Regression target: All existing unit and integration tests across `themis-api` (3 tests), `mneme-persistence` (125 tests), `energeia-ponos` (59 tests), `pylai-mllp-in` (6 tests), `petasos-artemis` (20 tests), `agora-matrix` (18 tests), and `agora-core` remain green.
- Architecture guardrails: `SecurityEnforcementArchitectureTest` validates zero `System.out` in processing classes, safe `toString()` on security records, and zero response-body logging in `OperationsRestClient`.

# Assumptions & Open Questions

- **Significant Assumption (Inner HL7 Processors Retain PhiLogger Diagnostics)**: Inbound MLLP wrappers (`IncomingAdtMessageProcessorWrapper`, `IncomingMfnMessageProcessorWrapper`) remove raw HL7 message DEBUG logging without adding `PhiLogger`, because the downstream inner processors (`IncomingAdtMessageProcessor`, `IncomingMfnMessageProcessor`) already contain dual-gated `PhiLogger` diagnostic logging. Rationale: Eliminates redundant, duplicate PHI diagnostic logs while maintaining deliberate troubleshooting capability. Alternative: Inject `PhiLogger` into wrappers as well. Impact: Cleaner log streams and lower overhead when PHI diagnostics are enabled.
- **Significant Assumption (Exception Message Sanitization at Source for Matrix/Synapse Clients)**: `MatrixRestException` and `SynapseAdminException` are updated to format their message using HTTP status, errcode, and error description rather than the full raw HTTP response body. Rationale: Exception messages are frequently printed by uninstrumented framework error handlers and stack traces; sanitizing the message at construction time eliminates the root cause of body leakage across all callers. Alternative: Wrap every catch block across Agora with custom logging logic. Impact: Robust, centralized defense against upstream error body leakage.

# Delivery Steps

### ✓ Step 1: Security Context and Principal Safe String Representations
Goal: Override `toString()` on `ThemisPrincipal` and `ThemisSecurityContext` records to emit safe structural summaries without dumping raw attributes or authorities, preserving record immutability, accessors, and serialization contracts.
Scope: `themis/themis-api` (`ThemisPrincipal.java`, `ThemisSecurityContext.java`, `ThemisSecurityContextTest.java`).
Acceptance Criteria:
- [ ] `ThemisPrincipal` overrides `toString()` to include `principalId`, `principalType`, `sourceDomain`, and `attributeCount`, omitting raw attribute keys and values
- [ ] `ThemisSecurityContext` overrides `toString()` to include `originatingPrincipal`, `executingPrincipal`, `securityDomain`, `correlationId`, `causationId`, `tenantId`, `clientIp`, `requestedAt`, `authoritiesCount`, and `attributeCount`, omitting raw authority lists and attribute maps
- [ ] Record components, accessors, `equals()`, `hashCode()`, and serialization compatibility remain unchanged
- [ ] Unit tests in `ThemisSecurityContextTest` verify that synthetic secret markers in attributes and authorities are absent from `toString()` output
Verification: `mvn test -pl themis/themis-api -am` → green

### ✓ Step 2: OperationsRestClient Response Logging Hardening
Goal: Remove raw HTTP response body logging from `OperationsRestClient` error and warning handlers, replacing with safe operational metadata.
Scope: `hestia/mneme-persistence` (`OperationsRestClient.java`, new `OperationsRestClientLoggingTest.java`).
Acceptance Criteria:
- [ ] Remove `response.body()` from PUT, DELETE, and list-parsing ERROR log statements in `OperationsRestClient`
- [ ] Operational ERROR/WARN logs emit target service name, HTTP operation, HTTP status code, URI, and safe failure category
- [ ] Exception logging replaces raw exception message dumping with exception class name and safe failure category
- [ ] New unit test `OperationsRestClientLoggingTest` uses synthetic PHI markers (`PATIENT-PHI-MARKER-92831`) in mock error response bodies and asserts marker absence from captured operational logs
Verification: `mvn test -pl hestia/mneme-persistence -am` → green

### ✓ Step 3: Ponos Message Processors Logging Hardening
Goal: Remove `System.out.println` and raw FHIR/Ergon payload logging from Ponos message processors, routing deliberate payload diagnostics through `PhiLogger` at DEBUG/TRACE under the dual-gate control.
Scope: `energeia/ponos` (`TaskMessageProcessor.java`, `TaskEventMessageProcessor.java`, `TaskMessageProcessorTest.java`, new `TaskEventMessageProcessorLoggingTest.java`).
Acceptance Criteria:
- [ ] All `System.out.println` and `System.err.println` calls removed from `TaskMessageProcessor` and `TaskEventMessageProcessor`
- [ ] Whole-payload INFO/ERROR log statements replaced with safe operational metadata: Task ID, business status, priority, event type, trigger reason, execution stage
- [ ] Diagnostic payload inspection routes through `PhiLogger` at DEBUG/TRACE only, respecting `PhiLoggingConfig.isPhiEnabled()`
- [ ] Exception handlers log exception class and safe failure category instead of dumping raw payloads or untrusted exception messages
- [ ] Automated logging tests in `TaskMessageProcessorTest` and `TaskEventMessageProcessorLoggingTest` assert synthetic PHI marker absence at INFO/WARN/ERROR and confirm presence under `PhiLogger` only when PHI logging is explicitly enabled
Verification: `mvn test -pl energeia/ponos -am` → green

### ✓ Step 4: HL7 Gateway Inbound Wrapper and Processor Hardening
Goal: Eliminate uncontrolled SLF4J DEBUG raw-HL7 logging from gateway wrappers and prevent exception-message payload leakage in inner HL7 processors.
Scope: `pylai/pylai-mllp-in` (`IncomingAdtMessageProcessorWrapper.java`, `IncomingMfnMessageProcessorWrapper.java`, `IncomingAdtMessageProcessor.java`, `IncomingMfnMessageProcessor.java`, `IncomingOrmMessageProcessor.java`, `IncomingOruMessageProcessor.java`, processor tests).
Acceptance Criteria:
- [ ] Remove raw HL7 message body logging from `IncomingAdtMessageProcessorWrapper` and `IncomingMfnMessageProcessorWrapper`; log message type, control ID, and character length at ordinary DEBUG
- [ ] Inner processors retain existing controlled `phiLog.debug(...)` diagnostic logging under `PhiLoggingConfig`
- [ ] Fallback error logging in inner processors (`IncomingAdtMessageProcessor`, `IncomingMfnMessageProcessor`, `IncomingOrmMessageProcessor`, `IncomingOruMessageProcessor`) replaces raw `e.getMessage()` with exception class, error code, and safe error description
- [ ] Processor tests assert no raw HL7 message bodies or synthetic patient markers appear in operational logs at INFO/WARN/ERROR
Verification: `mvn test -pl pylai/pylai-mllp-in -am` → green

### ✓ Step 5: Transport, Conduit, and Matrix/Synapse Exception Hardening
Goal: Prevent raw HTTP response bodies and payload fragments from leaking via exception messages and stack traces in Artemis, conduit, workflow dispatcher, and Matrix/Synapse layers.
Scope: `petasos/petasos-artemis` (`ArtemisPetasosProducer.java`, `ArtemisPetasosSubscription.java`), `energeia/ponos` (`PetasosQueueToExchangeConduit.java`, `PragmaWorkflowDispatcher.java`), `agora/agora-matrix` (`MatrixClientAdapter.java`, `SynapseAdministrationGateway.java`, `MatrixRestException.java`, `SynapseAdminException.java`), `agora/agora-core` (`AgoraIdentityService.java`), and related tests.
Acceptance Criteria:
- [ ] `MatrixClientAdapter` and `SynapseAdministrationGateway` construct `MatrixRestException` and `SynapseAdminException` using HTTP status code, errcode, and error description rather than the raw HTTP response body
- [ ] `AgoraIdentityService` retry and deactivation handlers log HTTP status, errcode, and user ID without raw exception body dumping
- [ ] `ArtemisPetasosProducer` and `ArtemisPetasosSubscription` log destination, message ID, retry count, and exception class at WARN/ERROR rather than arbitrary message bodies
- [ ] `PetasosQueueToExchangeConduit` and `PragmaWorkflowDispatcher` sanitize error logging to emit workflow ID, Pragma ID, exception class, and safe failure category rather than raw exchange exception objects
- [ ] Focused tests in `agora-matrix`, `agora-core`, `petasos-artemis`, and `ponos` verify synthetic PHI markers in upstream exceptions do not leak into operational logs
Verification: `mvn test -pl petasos/petasos-artemis,energeia/ponos,agora/agora-matrix,agora/agora-core -am` → green

### ✓ Step 6: Architecture Guardrails, Production Config Verification, and Repository Test Suite
Goal: Enforce anti-regression architecture invariants against sensitive logging and verify full cross-module security and architectural test suites.
Scope: `paradeigma/paradeigma-test` (`SecurityEnforcementArchitectureTest.java`), production logback configurations (`pylai-mllp-cli`, `ponos-cli`, `mnemosyne-operations-cli`), and repository test suite.
Acceptance Criteria:
- [ ] Add architecture checks in `SecurityEnforcementArchitectureTest`:
  - Assert zero `System.out.println` and `System.err.println` in production processing classes (`energeia/ponos`, `pylai/pylai-mllp-in`)
  - Assert `ThemisPrincipal.toString()` and `ThemisSecurityContext.toString()` do not include raw attribute map dumps or authority collections
  - Assert `OperationsRestClient` does not log raw response body content
- [ ] Verify production logback configurations maintain `org.harmonia.phi` at DEBUG with `additivity="false"` and ensure no production settings enable verbose wire/payload logging for HAPI FHIR, Hibernate, Infinispan, Artemis, Camel, or Matrix
- [ ] Verify `PhiLoggingConfig.isPhiEnabled()` defaults strictly to `false` in production runtimes
- [ ] Execute full architecture and security test suite
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green