Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

PLAN THE TASK

**Task description**  
Plan the whole task.

**Feedback**  
Focus the plan on implementing only Task 05 / Step 01 CORS hardening across the identified modules. The plan should cover: BEFE application-layer trusted-origin allow-list with externalized configuration and fail-closed behavior; alignment of the raw BEFE operations server with the same policy; removal of browser CORS support from service-only APIs (Pylai inbound, Pylai outbound, Ponos, Mnemosyne Clinical); focused automated tests for trusted/untrusted/no-Origin/preflight/multiple origins/empty config/non-reflection/security boundary; and the smallest reliable verification sequence. Exclude all Step 02 logging remediation and avoid architecture redesign.



Checking recent IDE activity

**Requirements**

**Goal / Outcome**  
Eliminate wildcard CORS, arbitrary origin reflection, and wildcard-plus-credentials vulnerabilities across Harmonia. Restrict browser CORS strictly to the browser-facing Iris BEFE presentation API using externalized, fail-closed trusted-origin allow-listing with `Vary: Origin` caching safety. Completely remove browser CORS mechanisms from internal service-only integration APIs (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).

**Scope**
- **In scope**:
  - Centralized BEFE CORS configuration manager (`BefeCorsConfig`) supporting system properties and environment variables.
  - Hardening BEFE JAX-RS `CorsFilter` with preflight short-circuiting, exact allowed-origin matching, `Vary: Origin`, method/header restrictions, and credentials removal.
  - Hardening BEFE raw operations server (`OperationsServerManager`) on port 8090 to share `BefeCorsConfig` and eliminate wildcard CORS.
  - Complete removal of unused CORS filters and interceptors from service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).
  - Deployment configuration update in `deployment/kubernetes/base/iris/iris-befe.yaml`.
  - Architecture test enforcement in `SecurityEnforcementArchitectureTest`.
  - Comprehensive automated unit and integration tests covering all origin and preflight scenarios.
- **Out of scope**:
  - Task 05 Step 02 PHI / sensitive logging remediation (stdout removal, TaskMessageProcessor, OperationsRestClient, etc.).
  - Subsequent tasks (Task 06 AuditEvent immutability, Task 07 volatile fallback removal, Tasks 08-10 authoritative clinical workflows).
  - Adding CORS to other service-facing APIs (Mnemosyne Operations, Agora, Actuator endpoints).

**Done When**
- No API in Harmonia responds with `Access-Control-Allow-Origin: *` or combines wildcard origins with credentials.
- Iris BEFE protected endpoints emit `Access-Control-Allow-Origin` matching only explicitly configured, normalized trusted origins, accompanied by `Vary: Origin`.
- Untrusted origins and non-CORS requests (no `Origin` header) receive no `Access-Control-Allow-Origin` header, and untrusted preflight OPTIONS requests are rejected with 403 Forbidden.
- OPTIONS preflight requests remain strictly isolated from clinical business logic and authentication context creation.
- Service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`) contain no browser CORS filters or interceptors.
- All existing BEFE tests (104 tests) and architecture invariant tests pass green.

**Technical Design**

**Decisions**
- **Chose combined PreMatching JAX-RS request and response filter for BEFE / not response-only or servlet filter**: Intercepts preflight `OPTIONS` before resource matching to guarantee zero business logic execution, returns 403 Forbidden for untrusted preflight origins, and decorates all outbound responses (including Themis 401/403 aborts) with `Vary: Origin` and exact allowed origins for trusted clients.
- **Chose complete removal of CORS from service-only APIs / not disabled or restrictive CORS filters**: Pure machine-to-machine integration pipelines have zero browser callers; removing filters and interceptors entirely eliminates attack surface and dead code without altering service-to-service HTTP semantics.
- **Chose unified `BefeCorsConfig` shared between JAX-RS and raw Operations server / not independent configuration**: Prevents policy drift between port 8080 (clinical) and port 8090 (operations) while keeping trusted-origin management in a single authoritative class.
- **Chose omitting `Access-Control-Allow-Credentials: true` / not enabling credentials**: Iris BEFE relies on bearer tokens in `Authorization` headers (via Elytron OIDC), not ambient browser cookies; omitting credentials prevents browser credential leakage and CSRF attack vectors.

**Approach & Touches**
- **Reference Analog**: Follows `calliope/.../PhiLoggingConfig.java` for externalized system property with environment variable fallback, fail-closed default, and thread-safe in-memory test overrides.
- **Key Touches**:
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/BefeCorsConfig.java`: Authoritative configuration manager.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/CorsFilter.java`: Hardened JAX-RS filter.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/server/OperationsServerManager.java`: Operations socket server CORS alignment.
  - `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/CorsFilter.java`: File removal.
  - `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/CorsFilter.java`: File removal.
  - `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/CorsFilter.java`: File removal.
  - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/config/JpaRestfulServer.java`: Strip `CorsInterceptor` registration and imports.
  - `deployment/kubernetes/base/iris/iris-befe.yaml`: Add `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` container environment variable.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/BefeCorsFilterTest.java`: New comprehensive BEFE CORS test suite.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/OperationsServerManagerTest.java`: Update test 3 and add hardened operations CORS tests.
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`: Add CORS isolation architecture rule.

**Nuances, Risks & Corners**
- **Exact Origin Normalization**: Origins must be matched as structured tuples `(scheme, host, port)` via `java.net.URI` normalization (lowercase scheme and host, explicit port if non-default, no path/query/fragment). Subdomain suffixes (e.g. `clinical.harmonia.local.attacker.com`) and path prefixes must be strictly rejected.
- **Fail-Closed Default**: If no origins are configured, `BefeCorsConfig.getAllowedOrigins()` returns an empty set; all cross-origin requests are denied without throwing runtime initialization exceptions.
- **Cache Integrity**: `Vary: Origin` must always be included whenever `Access-Control-Allow-Origin` is returned dynamically to prevent proxy and browser cache poisoning between different origins.
- **Preflight vs Business Logic**: Preflight requests must never trigger Themis policy evaluation or instantiate `ThemisSecurityContext`.

**Contracts**  
`BefeCorsConfig` authoritative contracts:
```java
public final class BefeCorsConfig {
    public static final String PROPERTY_ALLOWED_ORIGINS = "harmonia.befe.cors.allowed-origins";
    public static final String ENV_ALLOWED_ORIGINS = "HARMONIA_BEFE_CORS_ALLOWED_ORIGINS";
    public static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS";
    public static final String ALLOWED_HEADERS = "Authorization, Content-Type, Accept, X-Correlation-Id";
    public static final String MAX_AGE_SECONDS = "86400";

    public static Set<String> getAllowedOrigins();
    public static String normalizeOrigin(String rawOrigin);
    public static boolean isOriginAllowed(String origin);
    public static void setAllowedOriginsOverrideForTesting(String origins);
    public static void resetAllowedOriginsForTesting();
}
```

**Testing**

- Must-hold: Trusted configured origin (`https://clinical.harmonia.local`) receives exact `Access-Control-Allow-Origin: https://clinical.harmonia.local` and `Vary: Origin`.
- Must-hold: Untrusted origin (`https://attacker.example`) receives no `Access-Control-Allow-Origin` header; untrusted preflight OPTIONS receives 403 Forbidden.
- Must-hold: Non-CORS request without `Origin` header processes normally with zero `Access-Control-*` headers emitted.
- Must-hold: Empty or absent configuration fails closed: no origins are granted cross-origin access.
- Must-hold: No response from BEFE JAX-RS or raw Operations server emits `Access-Control-Allow-Credentials: true` or `Access-Control-Allow-Origin: *`.
- Must-hold: Raw Operations server (port 8090) validates requesting origins against `BefeCorsConfig` and handles preflight OPTIONS without executing operations logic.
- Must-hold: Architecture test asserts zero CORS filters or interceptors registered in `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, and `mnemosyne-clinical`.
- Regression target: All existing 104 tests in `iris/iris-befe` remain green.

**Assumptions & Open Questions**

- **Significant Assumption (Bearer Authentication vs CORS Credentials)**: BEFE uses bearer-token authentication (`Authorization: Bearer <token>`) managed by WildFly Elytron OIDC, not ambient browser session cookies. Rationale: Omitting `Access-Control-Allow-Credentials: true` eliminates cross-origin credentialed leakage risks. Alternative: Externalize a credential toggle if cookie sessions are introduced in the future. Impact: Substantially hardened browser security posture.
- **Significant Assumption (Preflight Response Status)**: Successful preflight OPTIONS requests return HTTP 200 OK. Rationale: Preserves existing Harmonia behavior (`CorsFilter.java:40` and `OperationsServerManager.java:206`). Alternative: HTTP 204 No Content. Impact: Consistent with current Axios client handling across Iris SPAs.

**Delivery Steps**

**Step 1: Remove CORS from Service-Only APIs and Add Architecture Guardrails**  
Goal: Eliminate browser CORS attack surface from service-only modules and enforce anti-regression architecture invariants.  
Scope: `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, `hestia/mnemosyne-clinical`, and `paradeigma/paradeigma-test`.  
Acceptance Criteria:
- [ ] `CorsFilter.java` removed from `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/`
- [ ] `CorsFilter.java` removed from `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/`
- [ ] `CorsFilter.java` removed from `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/`
- [ ] `CorsInterceptor` and `CorsConfiguration` registration removed from `hestia/mnemosyne-clinical/.../JpaRestfulServer.java`
- [ ] Architecture rule added to `SecurityEnforcementArchitectureTest` asserting service-only modules register no CORS providers/interceptors and BEFE contains no wildcard CORS
- [ ] Modules compile cleanly and existing service-module unit/integration tests pass
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="SecurityEnforcementArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 2: Harden BEFE CORS, Align Operations Server, and Update Deployment Configuration**  
Goal: Implement externalized trusted-origin allow-listing, preflight short-circuiting, and fail-closed validation for BEFE JAX-RS and raw Operations server.  
Scope: `iris/iris-befe` (`BefeCorsConfig.java`, `CorsFilter.java`, `OperationsServerManager.java`, `BefeCorsFilterTest.java`, `OperationsServerManagerTest.java`) and `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `BefeCorsConfig` created with system property (`harmonia.befe.cors.allowed-origins`), environment variable (`HARMONIA_BEFE_CORS_ALLOWED_ORIGINS`), origin normalization, and fail-closed evaluation
- [ ] `CorsFilter` updated to implement combined `ContainerRequestFilter` and `ContainerResponseFilter` with `@PreMatching`
- [ ] Preflight OPTIONS for trusted origins short-circuits with 200 OK, exact origin, `Vary: Origin`, restricted methods, and restricted headers
- [ ] Preflight OPTIONS for untrusted origins aborts with 403 Forbidden and no CORS headers
- [ ] Outbound responses for trusted origins include exact `Access-Control-Allow-Origin` and `Vary: Origin`; untrusted and non-CORS requests receive no `Access-Control-Allow-Origin`
- [ ] `Access-Control-Allow-Credentials: true` and wildcard origins are never emitted
- [ ] `OperationsServerManager` aligned with `BefeCorsConfig` on port 8090, replacing wildcard CORS with exact matching and preflight validation
- [ ] `deployment/kubernetes/base/iris/iris-befe.yaml` updated to inject `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` with deployed Iris origins (`clinical`, `console`, `admin`)
- [ ] Automated tests in `BefeCorsFilterTest` verify: trusted origin, untrusted origin, no-Origin, multiple origins, empty config, credentials absence, preflight isolation, and non-reflection
- [ ] `OperationsServerManagerTest` updated and passing for hardened preflight and origin verification
Verification: `mvn test -pl iris/iris-befe` → green



REVIEW THE PLAN

**Task description**  
Review the current plan for Task 05 / Step 01 CORS hardening.

**Feedback**  
Check that the plan is tightly scoped to Step 01 only, preserves existing authentication/authorization boundaries, covers fail-closed externalized BEFE trusted-origin handling plus alignment of the raw operations server, removes rather than restricts CORS on service-only modules, and includes focused tests for non-reflection, empty config, untrusted origins, and preflight isolation. Flag any missing verification on changed module boundaries or any hidden Step 02/logging scope creep.



Plan Approved

**Verification**
- **All referenced files exist and behave as described**: verified BEFE `CorsFilter` (wildcard `*` + `Access-Control-Allow-Credentials: true` as a `ContainerResponseFilter`, OPTIONS→200); `OperationsServerManager.sendResponse` hardcodes `Access-Control-Allow-Origin: *` at line 527 and short-circuits OPTIONS at 205-207; `JpaRestfulServer` registers a wildcard HAPI `CorsInterceptor` (lines 62-68); `ThemisClinicalAuthorizationFilter` intentionally bypasses OPTIONS at lines 113-115. All four `CorsFilter.java` locations confirmed.
- **Reference analog is accurate**: `PhiLoggingConfig` genuinely implements the externalized system-property + env-var fallback, fail-closed default, and in-memory test-override pattern the plan proposes to mirror for `BefeCorsConfig`.
- **Breaking-test awareness confirmed**: existing `OperationsServerManagerTest` test 3 (`testCorsPreflight`) currently asserts `Access-Control-Allow-Origin == "*"`; the plan explicitly schedules updating it — no silent regression.
- **Service-only deletions are provably safe**: grep for `CorsFilter` returns only the 4 self-defining `@Provider` classes with zero external references, so removal cannot break compilation.
- **Step 1 verification adequacy**: `paradeigma-test` pom directly depends on `mnemosyne-clinical` (line 77), so `mvn test -pl paradeigma/paradeigma-test -am` compiles the only non-deletion edit (`JpaRestfulServer`), catching any compile break there.
- **Architecture-test convention fits**: `SecurityEnforcementArchitectureTest` already uses file-string inspection for similar guardrails (e.g., `workflowDispatcherAndErgonBaseReferencePonosProcessIdentity`), so the proposed CORS-isolation rule matches existing style.
- **Scope discipline**: no Step 02 logging remediation creep; auth/authz boundaries explicitly preserved (Themis filter untouched); service-only modules removed rather than restricted; fail-closed externalized BEFE origin allow-list; raw operations server aligned to shared `BefeCorsConfig`; tests cover trusted/untrusted/no-Origin/multiple/empty-config/credentials-absence/preflight-isolation/non-reflection.
- Verification commands use real project tooling from AGENTS.md and target the changed boundaries.

**Issues**  
None blocking.

**Notes**
- Minor (non-blocking): Step 1 modifies four service modules but its `Verification` runs only `SecurityEnforcementArchitectureTest`. This is acceptable because (a) the pylai/ponos/pylai-out changes are pure deletions of standalone unreferenced `@Provider` classes, and (b) the only edited-in-place file, `JpaRestfulServer` in `mnemosyne-clinical`, is transitively compiled via `-am`. If the executor wants belt-and-suspenders coverage, adding the changed service modules to the `-pl` list is a cheap enhancement, but it is not required for correctness.
- The plan returns 403 for untrusted preflight OPTIONS; the spec only strictly requires "no CORS grant." Returning 403 is a defensible, stricter choice and is explicitly listed as intended behavior — not a deviation.

**Discoveries for future steps**
- `paradeigma-test` depends on `mnemosyne-clinical`, `pylai-fhir-registry`, `pylai-mllp-base`, `erga`, `praxis` but NOT directly on `pylai-mllp-in`, `pylai-mllp-out`, or `ponos` — relevant when choosing verification `-pl` scopes.
- `SecurityEnforcementArchitectureTest` is file-content based (not ArchUnit `classes()` rules) for repo-layout guardrails; new guardrails should follow that string-inspection pattern.
- Existing `OperationsServerManagerTest` and `OperationsServerManagerIntegrationTest` both exercise the raw port-8090 server over real sockets — useful harness for the hardened operations CORS tests.



**Requirements**

**Goal / Outcome**  
Eliminate wildcard CORS, arbitrary origin reflection, and wildcard-plus-credentials vulnerabilities across Harmonia. Restrict browser CORS strictly to the browser-facing Iris BEFE presentation API using externalized, fail-closed trusted-origin allow-listing with `Vary: Origin` caching safety. Completely remove browser CORS mechanisms from internal service-only integration APIs (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).

**Scope**
- **In scope**:
  - Centralized BEFE CORS configuration manager (`BefeCorsConfig`) supporting system properties and environment variables.
  - Hardening BEFE JAX-RS `CorsFilter` with preflight short-circuiting, exact allowed-origin matching, `Vary: Origin`, method/header restrictions, and credentials removal.
  - Hardening BEFE raw operations server (`OperationsServerManager`) on port 8090 to share `BefeCorsConfig` and eliminate wildcard CORS.
  - Complete removal of unused CORS filters and interceptors from service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).
  - Deployment configuration update in `deployment/kubernetes/base/iris/iris-befe.yaml`.
  - Architecture test enforcement in `SecurityEnforcementArchitectureTest`.
  - Comprehensive automated unit and integration tests covering all origin and preflight scenarios.
- **Out of scope**:
  - Task 05 Step 02 PHI / sensitive logging remediation (stdout removal, TaskMessageProcessor, OperationsRestClient, etc.).
  - Subsequent tasks (Task 06 AuditEvent immutability, Task 07 volatile fallback removal, Tasks 08-10 authoritative clinical workflows).
  - Adding CORS to other service-facing APIs (Mnemosyne Operations, Agora, Actuator endpoints).

**Done When**
- No API in Harmonia responds with `Access-Control-Allow-Origin: *` or combines wildcard origins with credentials.
- Iris BEFE protected endpoints emit `Access-Control-Allow-Origin` matching only explicitly configured, normalized trusted origins, accompanied by `Vary: Origin`.
- Untrusted origins and non-CORS requests (no `Origin` header) receive no `Access-Control-Allow-Origin` header, and untrusted preflight OPTIONS requests are rejected with 403 Forbidden.
- OPTIONS preflight requests remain strictly isolated from clinical business logic and authentication context creation.
- Service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`) contain no browser CORS filters or interceptors.
- All existing BEFE tests (104 tests) and architecture invariant tests pass green.

**Technical Design**

**Decisions**
- **Chose combined PreMatching JAX-RS request and response filter for BEFE / not response-only or servlet filter**: Intercepts preflight `OPTIONS` before resource matching to guarantee zero business logic execution, returns 403 Forbidden for untrusted preflight origins, and decorates all outbound responses (including Themis 401/403 aborts) with `Vary: Origin` and exact allowed origins for trusted clients.
- **Chose complete removal of CORS from service-only APIs / not disabled or restrictive CORS filters**: Pure machine-to-machine integration pipelines have zero browser callers; removing filters and interceptors entirely eliminates attack surface and dead code without altering service-to-service HTTP semantics.
- **Chose unified `BefeCorsConfig` shared between JAX-RS and raw Operations server / not independent configuration**: Prevents policy drift between port 8080 (clinical) and port 8090 (operations) while keeping trusted-origin management in a single authoritative class.
- **Chose omitting `Access-Control-Allow-Credentials: true` / not enabling credentials**: Iris BEFE relies on bearer tokens in `Authorization` headers (via Elytron OIDC), not ambient browser cookies; omitting credentials prevents browser credential leakage and CSRF attack vectors.

**Approach & Touches**
- **Reference Analog**: Follows `calliope/.../PhiLoggingConfig.java` for externalized system property with environment variable fallback, fail-closed default, and thread-safe in-memory test overrides.
- **Key Touches**:
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/BefeCorsConfig.java`: Authoritative configuration manager.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/CorsFilter.java`: Hardened JAX-RS filter.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/server/OperationsServerManager.java`: Operations socket server CORS alignment.
  - `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/CorsFilter.java`: File removal.
  - `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/CorsFilter.java`: File removal.
  - `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/CorsFilter.java`: File removal.
  - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/config/JpaRestfulServer.java`: Strip `CorsInterceptor` registration and imports.
  - `deployment/kubernetes/base/iris/iris-befe.yaml`: Add `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` container environment variable.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/BefeCorsFilterTest.java`: New comprehensive BEFE CORS test suite.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/OperationsServerManagerTest.java`: Update test 3 and add hardened operations CORS tests.
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`: Add CORS isolation architecture rule.

**Nuances, Risks & Corners**
- **Exact Origin Normalization**: Origins must be matched as structured tuples `(scheme, host, port)` via `java.net.URI` normalization (lowercase scheme and host, explicit port if non-default, no path/query/fragment). Subdomain suffixes (e.g. `clinical.harmonia.local.attacker.com`) and path prefixes must be strictly rejected.
- **Fail-Closed Default**: If no origins are configured, `BefeCorsConfig.getAllowedOrigins()` returns an empty set; all cross-origin requests are denied without throwing runtime initialization exceptions.
- **Cache Integrity**: `Vary: Origin` must always be included whenever `Access-Control-Allow-Origin` is returned dynamically to prevent proxy and browser cache poisoning between different origins.
- **Preflight vs Business Logic**: Preflight requests must never trigger Themis policy evaluation or instantiate `ThemisSecurityContext`.

**Contracts**  
`BefeCorsConfig` authoritative contracts:
```java
public final class BefeCorsConfig {
    public static final String PROPERTY_ALLOWED_ORIGINS = "harmonia.befe.cors.allowed-origins";
    public static final String ENV_ALLOWED_ORIGINS = "HARMONIA_BEFE_CORS_ALLOWED_ORIGINS";
    public static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS";
    public static final String ALLOWED_HEADERS = "Authorization, Content-Type, Accept, X-Correlation-Id";
    public static final String MAX_AGE_SECONDS = "86400";

    public static Set<String> getAllowedOrigins();
    public static String normalizeOrigin(String rawOrigin);
    public static boolean isOriginAllowed(String origin);
    public static void setAllowedOriginsOverrideForTesting(String origins);
    public static void resetAllowedOriginsForTesting();
}
```

**Testing**

- Must-hold: Trusted configured origin (`https://clinical.harmonia.local`) receives exact `Access-Control-Allow-Origin: https://clinical.harmonia.local` and `Vary: Origin`.
- Must-hold: Untrusted origin (`https://attacker.example`) receives no `Access-Control-Allow-Origin` header; untrusted preflight OPTIONS receives 403 Forbidden.
- Must-hold: Non-CORS request without `Origin` header processes normally with zero `Access-Control-*` headers emitted.
- Must-hold: Empty or absent configuration fails closed: no origins are granted cross-origin access.
- Must-hold: No response from BEFE JAX-RS or raw Operations server emits `Access-Control-Allow-Credentials: true` or `Access-Control-Allow-Origin: *`.
- Must-hold: Raw Operations server (port 8090) validates requesting origins against `BefeCorsConfig` and handles preflight OPTIONS without executing operations logic.
- Must-hold: Architecture test asserts zero CORS filters or interceptors registered in `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, and `mnemosyne-clinical`.
- Regression target: All existing 104 tests in `iris/iris-befe` remain green.

**Assumptions & Open Questions**

- **Significant Assumption (Bearer Authentication vs CORS Credentials)**: BEFE uses bearer-token authentication (`Authorization: Bearer <token>`) managed by WildFly Elytron OIDC, not ambient browser session cookies. Rationale: Omitting `Access-Control-Allow-Credentials: true` eliminates cross-origin credentialed leakage risks. Alternative: Externalize a credential toggle if cookie sessions are introduced in the future. Impact: Substantially hardened browser security posture.
- **Significant Assumption (Preflight Response Status)**: Successful preflight OPTIONS requests return HTTP 200 OK. Rationale: Preserves existing Harmonia behavior (`CorsFilter.java:40` and `OperationsServerManager.java:206`). Alternative: HTTP 204 No Content. Impact: Consistent with current Axios client handling across Iris SPAs.

**Delivery Steps**

*** Step 1: Remove CORS from Service-Only APIs and Add Architecture Guardrails**  
Goal: Eliminate browser CORS attack surface from service-only modules and enforce anti-regression architecture invariants.  
Scope: `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, `hestia/mnemosyne-clinical`, and `paradeigma/paradeigma-test`.  
Acceptance Criteria:
- [ ] `CorsFilter.java` removed from `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/`
- [ ] `CorsFilter.java` removed from `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/`
- [ ] `CorsFilter.java` removed from `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/`
- [ ] `CorsInterceptor` and `CorsConfiguration` registration removed from `hestia/mnemosyne-clinical/.../JpaRestfulServer.java`
- [ ] Architecture rule added to `SecurityEnforcementArchitectureTest` asserting service-only modules register no CORS providers/interceptors and BEFE contains no wildcard CORS
- [ ] Modules compile cleanly and existing service-module unit/integration tests pass
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="SecurityEnforcementArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 2: Harden BEFE CORS, Align Operations Server, and Update Deployment Configuration**  
Goal: Implement externalized trusted-origin allow-listing, preflight short-circuiting, and fail-closed validation for BEFE JAX-RS and raw Operations server.  
Scope: `iris/iris-befe` (`BefeCorsConfig.java`, `CorsFilter.java`, `OperationsServerManager.java`, `BefeCorsFilterTest.java`, `OperationsServerManagerTest.java`) and `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `BefeCorsConfig` created with system property (`harmonia.befe.cors.allowed-origins`), environment variable (`HARMONIA_BEFE_CORS_ALLOWED_ORIGINS`), origin normalization, and fail-closed evaluation
- [ ] `CorsFilter` updated to implement combined `ContainerRequestFilter` and `ContainerResponseFilter` with `@PreMatching`
- [ ] Preflight OPTIONS for trusted origins short-circuits with 200 OK, exact origin, `Vary: Origin`, restricted methods, and restricted headers
- [ ] Preflight OPTIONS for untrusted origins aborts with 403 Forbidden and no CORS headers
- [ ] Outbound responses for trusted origins include exact `Access-Control-Allow-Origin` and `Vary: Origin`; untrusted and non-CORS requests receive no `Access-Control-Allow-Origin`
- [ ] `Access-Control-Allow-Credentials: true` and wildcard origins are never emitted
- [ ] `OperationsServerManager` aligned with `BefeCorsConfig` on port 8090, replacing wildcard CORS with exact matching and preflight validation
- [ ] `deployment/kubernetes/base/iris/iris-befe.yaml` updated to inject `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` with deployed Iris origins (`clinical`, `console`, `admin`)
- [ ] Automated tests in `BefeCorsFilterTest` verify: trusted origin, untrusted origin, no-Origin, multiple origins, empty config, credentials absence, preflight isolation, and non-reflection
- [ ] `OperationsServerManagerTest` updated and passing for hardened preflight and origin verification
Verification: `mvn test -pl iris/iris-befe` → green



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Priorities: remove browser CORS support from service-only modules exactly as scoped; keep all auth/authz/runtime behavior otherwise unchanged; add architecture guardrails that assert these service-only modules no longer register CORS providers/interceptors and that BEFE does not use wildcard CORS. Known from previous steps: the three service CORS filters are standalone `@Provider` classes with no external references, so deletion should be safe; `JpaRestfulServer` currently registers HAPI `CorsInterceptor`; `SecurityEnforcementArchitectureTest` already uses file-content inspection patterns suitable for this guardrail. Optimize execution speed: prefer narrow compile/test scope, short command timeouts first (60-120s), and report exact commands used. If tests are run, keep them targeted to this step only.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Step 1 Accomplishments: Remove CORS from Service-Only APIs and Add Architecture Guardrails**

**1. Removed Browser CORS from Service-Only Modules**  
Browser CORS mechanisms were completely eliminated from internal integration and persistence services that have no browser callers:
- **`pylai-mllp-in`**: Deleted `CorsFilter.java` (`pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/CorsFilter.java`).
- **`pylai-mllp-out`**: Deleted `CorsFilter.java` (`pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/CorsFilter.java`).
- **`energeia-ponos`**: Deleted `CorsFilter.java` (`energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/CorsFilter.java`).
- **`mnemosyne-clinical`**: Removed HAPI `CorsInterceptor` and Spring `CorsConfiguration` registration as well as unused imports (`CorsInterceptor`, `CorsConfiguration`, `Arrays`) from `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/config/JpaRestfulServer.java`.

**2. Added Architecture Guardrails**  
Added `serviceOnlyModulesMustNotRegisterCorsAndBefeMustNotUseWildcardCors` to `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`:
- Asserts that `CorsFilter.java` files do not exist in `pylai-mllp-in`, `pylai-mllp-out`, and `energeia-ponos`.
- Scans all Java source files across `pylai-mllp-in`, `pylai-mllp-out`, and `energeia-ponos` to guarantee zero CORS filters or `Access-Control-Allow-Origin` logic exist in those service modules.
- Asserts that `JpaRestfulServer.java` contains neither `CorsInterceptor` nor `CorsConfiguration`.
- Asserts that once BEFE CORS configuration is established, BEFE `CorsFilter` and `OperationsServerManager` emit no wildcard origins (`"*"`) or wildcard-plus-credentials.

**3. Verification & Test Execution**
- Verification command passed cleanly:
  ```bash
  mvn test -pl paradeigma/paradeigma-test -am -Dtest="SecurityEnforcementArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
  ```
  Result: 8 tests run, 0 failures, 0 errors, 0 skipped.
- Full architecture suite verified:
  ```bash
  mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
  ```
  Result: 32 tests run across 7 test classes, 0 failures, 0 errors, 0 skipped.
- Service module unit/integration tests verified:
  - `hestia/mnemosyne-clinical`: 25 tests run, 0 failures.
  - `pylai/pylai-mllp-in`: All tests passed.
  - `pylai/pylai-mllp-out`: All tests passed.
  - `energeia/ponos`: 59 tests run across affected reactor modules, 0 failures.

---

**Discoveries for future steps**
- **Execution commands & speed**: Always include `-am` when targeting submodules like `pylai-mllp-in` or `paradeigma-test` (e.g. `mvn test -pl paradeigma/paradeigma-test -am -Dtest="..."`) so upstream modules (`pylai-mllp-base`, `calliope`, `themis-core`) are compiled in the reactor; builds complete in ~5–12s.
- **Service modules**: Standalone `@Provider` classes in WildFly JAX-RS modules (`CorsFilter`) had zero references from code or XML configurations, allowing clean deletion without configuration adjustments.



IMPLEMENTATION REVIEW

**Task description**  
Fully complete step 1.

**Feedback**  
Please verify only step 1: removal of browser CORS support from service-only modules and the added architecture guardrails. Focus on whether the deletions and `JpaRestfulServer` edit are complete and safe, whether the new `SecurityEnforcementArchitectureTest` meaningfully prevents regression without overfitting, and whether any service-only CORS registration or wildcard CORS traces remain that step 1 should have addressed. Also confirm the reported targeted test execution is sufficient evidence for this step.



Implementation Approved

**Verification**
- **Service CorsFilter deletions**: Confirmed via filesystem check that `CorsFilter.java` is actually MISSING from `pylai/pylai-mllp-in/.../mllpgateway/config/`, `pylai/pylai-mllp-out/.../mllpout/config/`, and `energeia/ponos/.../praxis/rest/`. (Note: `glob_search` returned stale git-tracked paths for these; the direct filesystem check confirmed deletion.)
- **JpaRestfulServer edit**: Verified `hestia/mnemosyne-clinical/.../JpaRestfulServer.java` no longer references `CorsInterceptor`, `CorsConfiguration`, or the now-unused `java.util.Arrays` import; only `LoggingInterceptor` and `ResponseHighlighterInterceptor` remain registered. Auth/runtime behavior otherwise unchanged.
- **No stray CORS traces in service modules**: A repo-wide grep for `Access-Control|CrossOrigin|CorsInterceptor|CorsConfiguration|allowedOrigins` returned matches only in `iris-befe` (CorsFilter, OperationsServerManager) and the arch test itself — no leftover CORS in the four service-only modules. The `iris-befe` wildcard CORS is Step 2's scope, correctly untouched here.
- **Architecture test quality**: The new rule `serviceOnlyModulesMustNotRegisterCorsAndBefeMustNotUseWildcardCors` (1) asserts absence of the three deleted files, (2) walks all `.java` sources in the three service trees asserting no `Access-Control-Allow-Origin`/`CorsFilter`, (3) asserts `JpaRestfulServer` has no `CorsInterceptor`/`CorsConfiguration`, and (4) has a forward-compatible BEFE wildcard guard gated on `BefeCorsConfig` existence (dormant until Step 2, so it neither falsely fails now nor overfits). Test imports (`Files`, `Path`, `Stream`, `List`) are present.
- **Test execution**: Ran the exact verification command `mvn test -pl paradeigma/paradeigma-test -am -Dtest="SecurityEnforcementArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → `Tests run: 8, Failures: 0, Errors: 0` BUILD SUCCESS.
- **Compile safety**: `mvn compile -pl pylai/pylai-mllp-in,pylai/pylai-mllp-out,energeia/ponos,hestia/mnemosyne-clinical -am` succeeded (exit 0), confirming no dangling references to the removed `@Provider` filters.

**Issues**  
None blocking.

**Notes**
- The `-am` flag is required for the verification command; omitting it produces a spurious `DependencyResolutionException` for unbuilt reactor artifacts (not a code defect).
- Non-blocking: The executor also created two planning markdown files under `.junie/plans/` (`options-draft.md`, `task-05-step-01-cors-hardening.md`). These are harmless documentation artifacts, not code.
- Non-blocking: Arch-test part 2 scans only the three JAX-RS service dirs; `mnemosyne-clinical` is covered narrowly by the `JpaRestfulServer`-specific assertion in part 3. This matches the plan's stated touch points and is adequate since that file was the only CORS registration site in that module.

**Discoveries for future steps**
- Verification command runs fast: full `-am` reactor build + arch test ≈ well under the 280s timeout; the SecurityEnforcementArchitectureTest itself elapses ~0.08s. Always keep `-am`.
- `glob_search` can return stale git-tracked paths for recently deleted files — confirm deletions with a filesystem check (`[ -e path ]`) rather than glob alone.
- Step 2 will activate arch-test part 4 (BEFE wildcard guard) once `iris/iris-befe/.../config/BefeCorsConfig.java` is created; BEFE `CorsFilter` (line 33) and `OperationsServerManager` (line 527) still emit `Access-Control-Allow-Origin: *` and must be hardened to keep that guard green.



**Requirements**

**Goal / Outcome**  
Eliminate wildcard CORS, arbitrary origin reflection, and wildcard-plus-credentials vulnerabilities across Harmonia. Restrict browser CORS strictly to the browser-facing Iris BEFE presentation API using externalized, fail-closed trusted-origin allow-listing with `Vary: Origin` caching safety. Completely remove browser CORS mechanisms from internal service-only integration APIs (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).

**Scope**
- **In scope**:
  - Centralized BEFE CORS configuration manager (`BefeCorsConfig`) supporting system properties and environment variables.
  - Hardening BEFE JAX-RS `CorsFilter` with preflight short-circuiting, exact allowed-origin matching, `Vary: Origin`, method/header restrictions, and credentials removal.
  - Hardening BEFE raw operations server (`OperationsServerManager`) on port 8090 to share `BefeCorsConfig` and eliminate wildcard CORS.
  - Complete removal of unused CORS filters and interceptors from service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).
  - Deployment configuration update in `deployment/kubernetes/base/iris/iris-befe.yaml`.
  - Architecture test enforcement in `SecurityEnforcementArchitectureTest`.
  - Comprehensive automated unit and integration tests covering all origin and preflight scenarios.
- **Out of scope**:
  - Task 05 Step 02 PHI / sensitive logging remediation (stdout removal, TaskMessageProcessor, OperationsRestClient, etc.).
  - Subsequent tasks (Task 06 AuditEvent immutability, Task 07 volatile fallback removal, Tasks 08-10 authoritative clinical workflows).
  - Adding CORS to other service-facing APIs (Mnemosyne Operations, Agora, Actuator endpoints).

**Done When**
- No API in Harmonia responds with `Access-Control-Allow-Origin: *` or combines wildcard origins with credentials.
- Iris BEFE protected endpoints emit `Access-Control-Allow-Origin` matching only explicitly configured, normalized trusted origins, accompanied by `Vary: Origin`.
- Untrusted origins and non-CORS requests (no `Origin` header) receive no `Access-Control-Allow-Origin` header, and untrusted preflight OPTIONS requests are rejected with 403 Forbidden.
- OPTIONS preflight requests remain strictly isolated from clinical business logic and authentication context creation.
- Service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`) contain no browser CORS filters or interceptors.
- All existing BEFE tests (104 tests) and architecture invariant tests pass green.

**Technical Design**

**Decisions**
- **Chose combined PreMatching JAX-RS request and response filter for BEFE / not response-only or servlet filter**: Intercepts preflight `OPTIONS` before resource matching to guarantee zero business logic execution, returns 403 Forbidden for untrusted preflight origins, and decorates all outbound responses (including Themis 401/403 aborts) with `Vary: Origin` and exact allowed origins for trusted clients.
- **Chose complete removal of CORS from service-only APIs / not disabled or restrictive CORS filters**: Pure machine-to-machine integration pipelines have zero browser callers; removing filters and interceptors entirely eliminates attack surface and dead code without altering service-to-service HTTP semantics.
- **Chose unified `BefeCorsConfig` shared between JAX-RS and raw Operations server / not independent configuration**: Prevents policy drift between port 8080 (clinical) and port 8090 (operations) while keeping trusted-origin management in a single authoritative class.
- **Chose omitting `Access-Control-Allow-Credentials: true` / not enabling credentials**: Iris BEFE relies on bearer tokens in `Authorization` headers (via Elytron OIDC), not ambient browser cookies; omitting credentials prevents browser credential leakage and CSRF attack vectors.

**Approach & Touches**
- **Reference Analog**: Follows `calliope/.../PhiLoggingConfig.java` for externalized system property with environment variable fallback, fail-closed default, and thread-safe in-memory test overrides.
- **Key Touches**:
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/BefeCorsConfig.java`: Authoritative configuration manager.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/CorsFilter.java`: Hardened JAX-RS filter.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/server/OperationsServerManager.java`: Operations socket server CORS alignment.
  - `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/CorsFilter.java`: File removal.
  - `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/CorsFilter.java`: File removal.
  - `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/CorsFilter.java`: File removal.
  - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/config/JpaRestfulServer.java`: Strip `CorsInterceptor` registration and imports.
  - `deployment/kubernetes/base/iris/iris-befe.yaml`: Add `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` container environment variable.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/BefeCorsFilterTest.java`: New comprehensive BEFE CORS test suite.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/OperationsServerManagerTest.java`: Update test 3 and add hardened operations CORS tests.
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`: Add CORS isolation architecture rule.

**Nuances, Risks & Corners**
- **Exact Origin Normalization**: Origins must be matched as structured tuples `(scheme, host, port)` via `java.net.URI` normalization (lowercase scheme and host, explicit port if non-default, no path/query/fragment). Subdomain suffixes (e.g. `clinical.harmonia.local.attacker.com`) and path prefixes must be strictly rejected.
- **Fail-Closed Default**: If no origins are configured, `BefeCorsConfig.getAllowedOrigins()` returns an empty set; all cross-origin requests are denied without throwing runtime initialization exceptions.
- **Cache Integrity**: `Vary: Origin` must always be included whenever `Access-Control-Allow-Origin` is returned dynamically to prevent proxy and browser cache poisoning between different origins.
- **Preflight vs Business Logic**: Preflight requests must never trigger Themis policy evaluation or instantiate `ThemisSecurityContext`.

**Contracts**  
`BefeCorsConfig` authoritative contracts:
```java
public final class BefeCorsConfig {
    public static final String PROPERTY_ALLOWED_ORIGINS = "harmonia.befe.cors.allowed-origins";
    public static final String ENV_ALLOWED_ORIGINS = "HARMONIA_BEFE_CORS_ALLOWED_ORIGINS";
    public static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS";
    public static final String ALLOWED_HEADERS = "Authorization, Content-Type, Accept, X-Correlation-Id";
    public static final String MAX_AGE_SECONDS = "86400";

    public static Set<String> getAllowedOrigins();
    public static String normalizeOrigin(String rawOrigin);
    public static boolean isOriginAllowed(String origin);
    public static void setAllowedOriginsOverrideForTesting(String origins);
    public static void resetAllowedOriginsForTesting();
}
```

**Testing**

- Must-hold: Trusted configured origin (`https://clinical.harmonia.local`) receives exact `Access-Control-Allow-Origin: https://clinical.harmonia.local` and `Vary: Origin`.
- Must-hold: Untrusted origin (`https://attacker.example`) receives no `Access-Control-Allow-Origin` header; untrusted preflight OPTIONS receives 403 Forbidden.
- Must-hold: Non-CORS request without `Origin` header processes normally with zero `Access-Control-*` headers emitted.
- Must-hold: Empty or absent configuration fails closed: no origins are granted cross-origin access.
- Must-hold: No response from BEFE JAX-RS or raw Operations server emits `Access-Control-Allow-Credentials: true` or `Access-Control-Allow-Origin: *`.
- Must-hold: Raw Operations server (port 8090) validates requesting origins against `BefeCorsConfig` and handles preflight OPTIONS without executing operations logic.
- Must-hold: Architecture test asserts zero CORS filters or interceptors registered in `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, and `mnemosyne-clinical`.
- Regression target: All existing 104 tests in `iris/iris-befe` remain green.

**Assumptions & Open Questions**

- **Significant Assumption (Bearer Authentication vs CORS Credentials)**: BEFE uses bearer-token authentication (`Authorization: Bearer <token>`) managed by WildFly Elytron OIDC, not ambient browser session cookies. Rationale: Omitting `Access-Control-Allow-Credentials: true` eliminates cross-origin credentialed leakage risks. Alternative: Externalize a credential toggle if cookie sessions are introduced in the future. Impact: Substantially hardened browser security posture.
- **Significant Assumption (Preflight Response Status)**: Successful preflight OPTIONS requests return HTTP 200 OK. Rationale: Preserves existing Harmonia behavior (`CorsFilter.java:40` and `OperationsServerManager.java:206`). Alternative: HTTP 204 No Content. Impact: Consistent with current Axios client handling across Iris SPAs.

**Delivery Steps**

**✓ Step 1: Remove CORS from Service-Only APIs and Add Architecture Guardrails**  
Goal: Eliminate browser CORS attack surface from service-only modules and enforce anti-regression architecture invariants.  
Scope: `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, `hestia/mnemosyne-clinical`, and `paradeigma/paradeigma-test`.  
Acceptance Criteria:
- [ ] `CorsFilter.java` removed from `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/`
- [ ] `CorsFilter.java` removed from `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/`
- [ ] `CorsFilter.java` removed from `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/`
- [ ] `CorsInterceptor` and `CorsConfiguration` registration removed from `hestia/mnemosyne-clinical/.../JpaRestfulServer.java`
- [ ] Architecture rule added to `SecurityEnforcementArchitectureTest` asserting service-only modules register no CORS providers/interceptors and BEFE contains no wildcard CORS
- [ ] Modules compile cleanly and existing service-module unit/integration tests pass
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="SecurityEnforcementArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 2: Harden BEFE CORS, Align Operations Server, and Update Deployment Configuration**  
Goal: Implement externalized trusted-origin allow-listing, preflight short-circuiting, and fail-closed validation for BEFE JAX-RS and raw Operations server.  
Scope: `iris/iris-befe` (`BefeCorsConfig.java`, `CorsFilter.java`, `OperationsServerManager.java`, `BefeCorsFilterTest.java`, `OperationsServerManagerTest.java`) and `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `BefeCorsConfig` created with system property (`harmonia.befe.cors.allowed-origins`), environment variable (`HARMONIA_BEFE_CORS_ALLOWED_ORIGINS`), origin normalization, and fail-closed evaluation
- [ ] `CorsFilter` updated to implement combined `ContainerRequestFilter` and `ContainerResponseFilter` with `@PreMatching`
- [ ] Preflight OPTIONS for trusted origins short-circuits with 200 OK, exact origin, `Vary: Origin`, restricted methods, and restricted headers
- [ ] Preflight OPTIONS for untrusted origins aborts with 403 Forbidden and no CORS headers
- [ ] Outbound responses for trusted origins include exact `Access-Control-Allow-Origin` and `Vary: Origin`; untrusted and non-CORS requests receive no `Access-Control-Allow-Origin`
- [ ] `Access-Control-Allow-Credentials: true` and wildcard origins are never emitted
- [ ] `OperationsServerManager` aligned with `BefeCorsConfig` on port 8090, replacing wildcard CORS with exact matching and preflight validation
- [ ] `deployment/kubernetes/base/iris/iris-befe.yaml` updated to inject `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` with deployed Iris origins (`clinical`, `console`, `admin`)
- [ ] Automated tests in `BefeCorsFilterTest` verify: trusted origin, untrusted origin, no-Origin, multiple origins, empty config, credentials absence, preflight isolation, and non-reflection
- [ ] `OperationsServerManagerTest` updated and passing for hardened preflight and origin verification
Verification: `mvn test -pl iris/iris-befe` → green



**Requirements**

**Goal / Outcome**  
Eliminate wildcard CORS, arbitrary origin reflection, and wildcard-plus-credentials vulnerabilities across Harmonia. Restrict browser CORS strictly to the browser-facing Iris BEFE presentation API using externalized, fail-closed trusted-origin allow-listing with `Vary: Origin` caching safety. Completely remove browser CORS mechanisms from internal service-only integration APIs (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).

**Scope**
- **In scope**:
  - Centralized BEFE CORS configuration manager (`BefeCorsConfig`) supporting system properties and environment variables.
  - Hardening BEFE JAX-RS `CorsFilter` with preflight short-circuiting, exact allowed-origin matching, `Vary: Origin`, method/header restrictions, and credentials removal.
  - Hardening BEFE raw operations server (`OperationsServerManager`) on port 8090 to share `BefeCorsConfig` and eliminate wildcard CORS.
  - Complete removal of unused CORS filters and interceptors from service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).
  - Deployment configuration update in `deployment/kubernetes/base/iris/iris-befe.yaml`.
  - Architecture test enforcement in `SecurityEnforcementArchitectureTest`.
  - Comprehensive automated unit and integration tests covering all origin and preflight scenarios.
- **Out of scope**:
  - Task 05 Step 02 PHI / sensitive logging remediation (stdout removal, TaskMessageProcessor, OperationsRestClient, etc.).
  - Subsequent tasks (Task 06 AuditEvent immutability, Task 07 volatile fallback removal, Tasks 08-10 authoritative clinical workflows).
  - Adding CORS to other service-facing APIs (Mnemosyne Operations, Agora, Actuator endpoints).

**Done When**
- No API in Harmonia responds with `Access-Control-Allow-Origin: *` or combines wildcard origins with credentials.
- Iris BEFE protected endpoints emit `Access-Control-Allow-Origin` matching only explicitly configured, normalized trusted origins, accompanied by `Vary: Origin`.
- Untrusted origins and non-CORS requests (no `Origin` header) receive no `Access-Control-Allow-Origin` header, and untrusted preflight OPTIONS requests are rejected with 403 Forbidden.
- OPTIONS preflight requests remain strictly isolated from clinical business logic and authentication context creation.
- Service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`) contain no browser CORS filters or interceptors.
- All existing BEFE tests (104 tests) and architecture invariant tests pass green.

**Technical Design**

**Decisions**
- **Chose combined PreMatching JAX-RS request and response filter for BEFE / not response-only or servlet filter**: Intercepts preflight `OPTIONS` before resource matching to guarantee zero business logic execution, returns 403 Forbidden for untrusted preflight origins, and decorates all outbound responses (including Themis 401/403 aborts) with `Vary: Origin` and exact allowed origins for trusted clients.
- **Chose complete removal of CORS from service-only APIs / not disabled or restrictive CORS filters**: Pure machine-to-machine integration pipelines have zero browser callers; removing filters and interceptors entirely eliminates attack surface and dead code without altering service-to-service HTTP semantics.
- **Chose unified `BefeCorsConfig` shared between JAX-RS and raw Operations server / not independent configuration**: Prevents policy drift between port 8080 (clinical) and port 8090 (operations) while keeping trusted-origin management in a single authoritative class.
- **Chose omitting `Access-Control-Allow-Credentials: true` / not enabling credentials**: Iris BEFE relies on bearer tokens in `Authorization` headers (via Elytron OIDC), not ambient browser cookies; omitting credentials prevents browser credential leakage and CSRF attack vectors.

**Approach & Touches**
- **Reference Analog**: Follows `calliope/.../PhiLoggingConfig.java` for externalized system property with environment variable fallback, fail-closed default, and thread-safe in-memory test overrides.
- **Key Touches**:
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/BefeCorsConfig.java`: Authoritative configuration manager.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/CorsFilter.java`: Hardened JAX-RS filter.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/server/OperationsServerManager.java`: Operations socket server CORS alignment.
  - `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/CorsFilter.java`: File removal.
  - `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/CorsFilter.java`: File removal.
  - `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/CorsFilter.java`: File removal.
  - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/config/JpaRestfulServer.java`: Strip `CorsInterceptor` registration and imports.
  - `deployment/kubernetes/base/iris/iris-befe.yaml`: Add `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` container environment variable.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/BefeCorsFilterTest.java`: New comprehensive BEFE CORS test suite.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/OperationsServerManagerTest.java`: Update test 3 and add hardened operations CORS tests.
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`: Add CORS isolation architecture rule.

**Nuances, Risks & Corners**
- **Exact Origin Normalization**: Origins must be matched as structured tuples `(scheme, host, port)` via `java.net.URI` normalization (lowercase scheme and host, explicit port if non-default, no path/query/fragment). Subdomain suffixes (e.g. `clinical.harmonia.local.attacker.com`) and path prefixes must be strictly rejected.
- **Fail-Closed Default**: If no origins are configured, `BefeCorsConfig.getAllowedOrigins()` returns an empty set; all cross-origin requests are denied without throwing runtime initialization exceptions.
- **Cache Integrity**: `Vary: Origin` must always be included whenever `Access-Control-Allow-Origin` is returned dynamically to prevent proxy and browser cache poisoning between different origins.
- **Preflight vs Business Logic**: Preflight requests must never trigger Themis policy evaluation or instantiate `ThemisSecurityContext`.

**Contracts**  
`BefeCorsConfig` authoritative contracts:
```java
public final class BefeCorsConfig {
    public static final String PROPERTY_ALLOWED_ORIGINS = "harmonia.befe.cors.allowed-origins";
    public static final String ENV_ALLOWED_ORIGINS = "HARMONIA_BEFE_CORS_ALLOWED_ORIGINS";
    public static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS";
    public static final String ALLOWED_HEADERS = "Authorization, Content-Type, Accept, X-Correlation-Id";
    public static final String MAX_AGE_SECONDS = "86400";

    public static Set<String> getAllowedOrigins();
    public static String normalizeOrigin(String rawOrigin);
    public static boolean isOriginAllowed(String origin);
    public static void setAllowedOriginsOverrideForTesting(String origins);
    public static void resetAllowedOriginsForTesting();
}
```

**Testing**

- Must-hold: Trusted configured origin (`https://clinical.harmonia.local`) receives exact `Access-Control-Allow-Origin: https://clinical.harmonia.local` and `Vary: Origin`.
- Must-hold: Untrusted origin (`https://attacker.example`) receives no `Access-Control-Allow-Origin` header; untrusted preflight OPTIONS receives 403 Forbidden.
- Must-hold: Non-CORS request without `Origin` header processes normally with zero `Access-Control-*` headers emitted.
- Must-hold: Empty or absent configuration fails closed: no origins are granted cross-origin access.
- Must-hold: No response from BEFE JAX-RS or raw Operations server emits `Access-Control-Allow-Credentials: true` or `Access-Control-Allow-Origin: *`.
- Must-hold: Raw Operations server (port 8090) validates requesting origins against `BefeCorsConfig` and handles preflight OPTIONS without executing operations logic.
- Must-hold: Architecture test asserts zero CORS filters or interceptors registered in `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, and `mnemosyne-clinical`.
- Regression target: All existing 104 tests in `iris/iris-befe` remain green.

**Assumptions & Open Questions**

- **Significant Assumption (Bearer Authentication vs CORS Credentials)**: BEFE uses bearer-token authentication (`Authorization: Bearer <token>`) managed by WildFly Elytron OIDC, not ambient browser session cookies. Rationale: Omitting `Access-Control-Allow-Credentials: true` eliminates cross-origin credentialed leakage risks. Alternative: Externalize a credential toggle if cookie sessions are introduced in the future. Impact: Substantially hardened browser security posture.
- **Significant Assumption (Preflight Response Status)**: Successful preflight OPTIONS requests return HTTP 200 OK. Rationale: Preserves existing Harmonia behavior (`CorsFilter.java:40` and `OperationsServerManager.java:206`). Alternative: HTTP 204 No Content. Impact: Consistent with current Axios client handling across Iris SPAs.

**Delivery Steps**

**✓ Step 1: Remove CORS from Service-Only APIs and Add Architecture Guardrails**  
Goal: Eliminate browser CORS attack surface from service-only modules and enforce anti-regression architecture invariants.  
Scope: `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, `hestia/mnemosyne-clinical`, and `paradeigma/paradeigma-test`.  
Acceptance Criteria:
- [ ] `CorsFilter.java` removed from `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/`
- [ ] `CorsFilter.java` removed from `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/`
- [ ] `CorsFilter.java` removed from `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/`
- [ ] `CorsInterceptor` and `CorsConfiguration` registration removed from `hestia/mnemosyne-clinical/.../JpaRestfulServer.java`
- [ ] Architecture rule added to `SecurityEnforcementArchitectureTest` asserting service-only modules register no CORS providers/interceptors and BEFE contains no wildcard CORS
- [ ] Modules compile cleanly and existing service-module unit/integration tests pass
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="SecurityEnforcementArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

*** Step 2: Harden BEFE CORS, Align Operations Server, and Update Deployment Configuration**  
Goal: Implement externalized trusted-origin allow-listing, preflight short-circuiting, and fail-closed validation for BEFE JAX-RS and raw Operations server.  
Scope: `iris/iris-befe` (`BefeCorsConfig.java`, `CorsFilter.java`, `OperationsServerManager.java`, `BefeCorsFilterTest.java`, `OperationsServerManagerTest.java`) and `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `BefeCorsConfig` created with system property (`harmonia.befe.cors.allowed-origins`), environment variable (`HARMONIA_BEFE_CORS_ALLOWED_ORIGINS`), origin normalization, and fail-closed evaluation
- [ ] `CorsFilter` updated to implement combined `ContainerRequestFilter` and `ContainerResponseFilter` with `@PreMatching`
- [ ] Preflight OPTIONS for trusted origins short-circuits with 200 OK, exact origin, `Vary: Origin`, restricted methods, and restricted headers
- [ ] Preflight OPTIONS for untrusted origins aborts with 403 Forbidden and no CORS headers
- [ ] Outbound responses for trusted origins include exact `Access-Control-Allow-Origin` and `Vary: Origin`; untrusted and non-CORS requests receive no `Access-Control-Allow-Origin`
- [ ] `Access-Control-Allow-Credentials: true` and wildcard origins are never emitted
- [ ] `OperationsServerManager` aligned with `BefeCorsConfig` on port 8090, replacing wildcard CORS with exact matching and preflight validation
- [ ] `deployment/kubernetes/base/iris/iris-befe.yaml` updated to inject `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` with deployed Iris origins (`clinical`, `console`, `admin`)
- [ ] Automated tests in `BefeCorsFilterTest` verify: trusted origin, untrusted origin, no-Origin, multiple origins, empty config, credentials absence, preflight isolation, and non-reflection
- [ ] `OperationsServerManagerTest` updated and passing for hardened preflight and origin verification
Verification: `mvn test -pl iris/iris-befe` → green



IMPLEMENTATION

**Task description**  
Fully complete step 2.

**Feedback**  
Implement only Task 05 / Step 01 BEFE CORS hardening and deployment/test updates. Priorities: add a single shared BEFE trusted-origin configuration with externalized property/env-var and fail-closed exact-origin matching; harden the JAX-RS filter so trusted preflight short-circuits before business logic while normal responses for trusted origins get exact `Access-Control-Allow-Origin` plus `Vary: Origin`; never emit wildcard origin or `Access-Control-Allow-Credentials`; align the raw operations server to the same policy; update deployment config to supply trusted BEFE origins; add focused tests for trusted/untrusted/no-Origin/multiple origins/empty config/preflight/non-reflection and operations-server behavior. Preserve Themis/WildFly authz/authn boundaries and do not touch Step 02 logging findings.

Known from previous steps: `ThemisClinicalAuthorizationFilter` already bypasses OPTIONS intentionally; `OperationsServerManagerTest` currently expects wildcard CORS and needs updating; the architecture test added in step 1 will begin enforcing no BEFE wildcard CORS once `BefeCorsConfig` exists. Optimize execution speed: use narrow tests first, short per-command timeouts (60-120s) unless a broader compile/test truly needs more, and report exact commands used with outcomes.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Open file

Code is Ready

**Step 2 Accomplishments: Harden BEFE CORS, Align Operations Server, and Update Deployment Configuration**

All goals and acceptance criteria for Step 2 have been fully implemented and verified.

**1. Centralized BEFE CORS Configuration (`BefeCorsConfig`)**
- Created `BefeCorsConfig` in `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/BefeCorsConfig.java`.
- Externalized configuration via system property `harmonia.befe.cors.allowed-origins` (`PROPERTY_ALLOWED_ORIGINS`) and environment variable fallback `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` (`ENV_ALLOWED_ORIGINS`).
- Established strict constants:
  - `ALLOWED_METHODS = "GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS"`
  - `ALLOWED_HEADERS = "Authorization, Content-Type, Accept, X-Correlation-Id"`
  - `MAX_AGE_SECONDS = "86400"`
- Structured origin tuple normalization via `java.net.URI`:
  - Lowercases scheme and host.
  - Strips default ports (`80` for `http`, `443` for `https`) while preserving non-default ports.
  - Strips trailing slash (`/`) and strictly rejects paths beyond root (e.g. `/api`), query strings, fragments, user info, non-http/https schemes, wildcard `*`, and string `"null"`.
- Prevents subdomain suffix attacks (e.g. `clinical.harmonia.local.attacker.com`) via exact Set matching.
- Fails closed: if no configuration is present or resolved, returns an empty set and denies all cross-origin requests.
- Implemented thread-safe in-memory test override lifecycle (`setAllowedOriginsOverrideForTesting`, `resetAllowedOriginsForTesting`).

**2. Hardened PreMatching JAX-RS `CorsFilter`**
- Updated `CorsFilter` in `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/CorsFilter.java` to implement both `ContainerRequestFilter` and `ContainerResponseFilter` annotated with `@Provider` and `@PreMatching`.
- **Preflight Isolation**: Intercepts preflight `OPTIONS` before resource matching:
  - Trusted origins short-circuit with HTTP 200 OK, exact allowed origin, `Vary: Origin`, restricted methods, restricted headers, and 86400s max-age. Preflight requests never reach clinical resource matching, Themis policy evaluation, or security context instantiation.
  - Untrusted preflight origins immediately abort with HTTP 403 Forbidden and zero CORS headers.
  - Non-CORS requests (no `Origin` header) process normally without interception.
- **Outbound Response Decoration**:
  - Outbound responses for trusted origins receive exact `Access-Control-Allow-Origin` matching the request origin and `Vary: Origin` (preserving any existing `Vary` values).
  - Outbound responses for untrusted origins receive NO `Access-Control-Allow-Origin` (zero reflection).
  - Outbound responses for non-CORS requests receive NO `Access-Control-*` headers.
  - `Access-Control-Allow-Credentials: true` and wildcard origins (`*`) are strictly eliminated and never emitted.

**3. Aligned Raw Operations Socket Server (`OperationsServerManager`)**
- Updated `OperationsServerManager` in `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/server/OperationsServerManager.java` (port 8090) to share `BefeCorsConfig`.
- Employs request-scoped `CURRENT_ALLOWED_ORIGIN` `ThreadLocal` safely cleared in a `finally` block.
- Preflight `OPTIONS` requests evaluate `Origin`:
  - Trusted origins receive 200 OK with exact allowed origin, `Vary: Origin`, allowed methods, allowed headers, and 86400s max-age.
  - Untrusted preflight origins receive 403 Forbidden with zero CORS headers without executing operations logic.
  - Non-CORS `OPTIONS` requests receive 405 Method Not Allowed with zero CORS headers.
- All HTTP responses emitted via `sendResponse` attach `Access-Control-Allow-Origin: <exact origin>` and `Vary: Origin` strictly when the origin is trusted; untrusted and non-CORS requests receive zero CORS headers.
- Removed unconditional `Access-Control-Allow-Origin: *` and broad header advertisements.

**4. Kubernetes Deployment Configuration**
- Updated `deployment/kubernetes/base/iris/iris-befe.yaml` to inject `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` containing the deployed Iris SPA origins:
  `https://clinical.harmonia.local,https://console.harmonia.local,https://admin.harmonia.local`.

**5. Automated Tests & Invariant Verification**
- Created comprehensive test suite in `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/BefeCorsFilterTest.java` (16 tests):
  - Empty / absent config fails closed.
  - System property and in-memory override lifecycle.
  - Multiple comma-separated origin parsing and normalization.
  - Case-insensitivity, default port stripping, and trailing slash handling.
  - Strict rejection of invalid origins, query parameters, path suffixes, user info, non-http schemes, and subdomain suffix spoofing.
  - Preflight short-circuiting with 200 OK and restricted headers for trusted origins.
  - Preflight rejection with 403 Forbidden and zero CORS headers for untrusted origins and empty configs.
  - Non-CORS request pass-through without abort.
  - Exact origin and `Vary: Origin` response decoration.
  - Non-reflection of untrusted origins.
  - Total absence of wildcard origins and `Access-Control-Allow-Credentials: true`.
- Updated `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/OperationsServerManagerTest.java` (test 3) with hardened preflight, 403 untrusted preflight rejection, 405 non-CORS preflight rejection, and origin validation.
- All 120 tests in `iris/iris-befe` passed green (0 failures, 0 errors).
- All 32 architecture invariant tests in `paradeigma/paradeigma-test` passed green (including `SecurityEnforcementArchitectureTest`).

---

**Discoveries for future steps**
- **Execution commands & speed**:
  - `mvn test -pl iris/iris-befe`: runs all 120 tests in ~4.8s.
  - `mvn test -pl paradeigma/paradeigma-test -am -Dtest="SecurityEnforcementArchitectureTest"`: compiles reactor and runs in ~6.5s.
- **JDK HTTP Client**: `java.net.HttpURLConnection` in Java standard library restricts the `Origin` header by default unless `System.setProperty("sun.net.http.allowRestrictedHeaders", "true")` is set in test setup.
- **JAX-RS PreMatching**: When testing JAX-RS `ContainerRequestFilter` with `abortWith`, testing `abortWith` captured responses validates preflight short-circuiting before resource matching, while `filter(requestContext, responseContext)` tests the response-filtering phase.



IMPLEMENTATION REVIEW

**Task description**  
Fully complete step 2.

**Feedback**  
Please review only Task 05 / Step 01 step 2. Focus on whether BEFE CORS is now fail-closed, exact-match, non-reflective, and free of wildcard origin plus credentials; whether the raw operations server truly aligns with the same trusted-origin policy and keeps OPTIONS preflight isolated from business logic; whether the deployment/configuration and tests are sufficient; and whether any changes accidentally weaken existing auth/authz boundaries or spill into Step 02 logging scope.



Implementation Approved

**Verification**
- **BefeCorsConfig** (`iris/iris-befe/.../config/BefeCorsConfig.java`): Confirmed fail-closed default (empty `Set` when unconfigured), system-property→env-var resolution order, and structured exact-origin normalization via `java.net.URI` (lowercases scheme/host, strips default ports 80/443, preserves non-default ports, rejects paths beyond `/`, query, fragment, userinfo, wildcard `*`, `null`, and non-http(s) schemes). Subdomain-suffix and path-prefix spoofing are rejected by exact Set membership.
- **CorsFilter** (`.../config/CorsFilter.java`): Verified it is a `@Provider @PreMatching` combined `ContainerRequestFilter`+`ContainerResponseFilter`. Trusted preflight OPTIONS short-circuits with 200 + exact origin + `Vary: Origin` + restricted methods/headers + max-age; untrusted preflight aborts 403 with zero CORS headers; non-CORS (no Origin) is never aborted. Response phase adds exact `Access-Control-Allow-Origin` + `Vary: Origin` only for trusted origins (non-reflective), preserving existing `Vary`. No `Access-Control-Allow-Credentials` and no wildcard ever emitted.
- **Preflight isolation / auth boundaries**: `CorsFilter` is `@PreMatching` while `ThemisClinicalAuthorizationFilter` is post-matching, so trusted/untrusted preflight is resolved before Themis evaluation or security-context creation. ThemisClinicalAuthorizationFilter was not modified (its 35 tests still pass). Untrusted non-OPTIONS requests still flow through normal Themis authz (correct — browser blocks due to absent CORS headers).
- **OperationsServerManager** (`.../server/OperationsServerManager.java`): Shares `BefeCorsConfig`; request-scoped `CURRENT_ALLOWED_ORIGIN` ThreadLocal set after origin validation and cleared in a `finally`. Preflight: trusted→200 with exact headers, untrusted-origin→403, no-Origin→405, all without executing operations logic. `sendResponse` emits `Access-Control-Allow-Origin`+`Vary: Origin` only when origin trusted. Wildcard removed.
- **Deployment** (`deployment/kubernetes/base/iris/iris-befe.yaml`): Injects `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS=https://clinical.harmonia.local,https://console.harmonia.local,https://admin.harmonia.local`.
- **Tests**: `BefeCorsFilterTest` (16 tests) covers trusted/untrusted/no-origin/multiple/empty/normalization/rejection/preflight/non-reflection/credentials-absence. `OperationsServerManagerTest` test 3 covers trusted 200 / untrusted 403 / non-CORS 405 / GET origin decoration / untrusted+non-CORS non-reflection.
- **Execution**: `mvn test -pl iris/iris-befe` → 120 tests, 0 failures (~run). `mvn test -pl paradeigma/paradeigma-test -am -Dtest=SecurityEnforcementArchitectureTest` → 8 tests green (BEFE no-wildcard invariant now enforced with BefeCorsConfig present). No Step 02 logging scope touched.

**Issues**  
None.

**Notes**
- Minor, non-blocking: `sendPreflightResponse` hardcodes the status line as "200 OK" (statusCode is always 200 at the only call site, so harmless). 
- The plan mentioned "104 existing BEFE tests" as a regression target; the module now reports 120 tests total (includes the new CORS suite) all green — no regression.

**Discoveries for future steps**
- `mvn test -pl iris/iris-befe` runs in a few seconds; `SecurityEnforcementArchitectureTest` via `-am` reactor build ~ under a minute — 300s/400s timeouts are ample.
- Test harness requires `System.setProperty("sun.net.http.allowRestrictedHeaders","true")` for `Origin` header on `HttpURLConnection` (already set in OperationsServerManagerTest setup).



**Requirements**

**Goal / Outcome**  
Eliminate wildcard CORS, arbitrary origin reflection, and wildcard-plus-credentials vulnerabilities across Harmonia. Restrict browser CORS strictly to the browser-facing Iris BEFE presentation API using externalized, fail-closed trusted-origin allow-listing with `Vary: Origin` caching safety. Completely remove browser CORS mechanisms from internal service-only integration APIs (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).

**Scope**
- **In scope**:
  - Centralized BEFE CORS configuration manager (`BefeCorsConfig`) supporting system properties and environment variables.
  - Hardening BEFE JAX-RS `CorsFilter` with preflight short-circuiting, exact allowed-origin matching, `Vary: Origin`, method/header restrictions, and credentials removal.
  - Hardening BEFE raw operations server (`OperationsServerManager`) on port 8090 to share `BefeCorsConfig` and eliminate wildcard CORS.
  - Complete removal of unused CORS filters and interceptors from service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).
  - Deployment configuration update in `deployment/kubernetes/base/iris/iris-befe.yaml`.
  - Architecture test enforcement in `SecurityEnforcementArchitectureTest`.
  - Comprehensive automated unit and integration tests covering all origin and preflight scenarios.
- **Out of scope**:
  - Task 05 Step 02 PHI / sensitive logging remediation (stdout removal, TaskMessageProcessor, OperationsRestClient, etc.).
  - Subsequent tasks (Task 06 AuditEvent immutability, Task 07 volatile fallback removal, Tasks 08-10 authoritative clinical workflows).
  - Adding CORS to other service-facing APIs (Mnemosyne Operations, Agora, Actuator endpoints).

**Done When**
- No API in Harmonia responds with `Access-Control-Allow-Origin: *` or combines wildcard origins with credentials.
- Iris BEFE protected endpoints emit `Access-Control-Allow-Origin` matching only explicitly configured, normalized trusted origins, accompanied by `Vary: Origin`.
- Untrusted origins and non-CORS requests (no `Origin` header) receive no `Access-Control-Allow-Origin` header, and untrusted preflight OPTIONS requests are rejected with 403 Forbidden.
- OPTIONS preflight requests remain strictly isolated from clinical business logic and authentication context creation.
- Service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`) contain no browser CORS filters or interceptors.
- All existing BEFE tests (104 tests) and architecture invariant tests pass green.

**Technical Design**

**Decisions**
- **Chose combined PreMatching JAX-RS request and response filter for BEFE / not response-only or servlet filter**: Intercepts preflight `OPTIONS` before resource matching to guarantee zero business logic execution, returns 403 Forbidden for untrusted preflight origins, and decorates all outbound responses (including Themis 401/403 aborts) with `Vary: Origin` and exact allowed origins for trusted clients.
- **Chose complete removal of CORS from service-only APIs / not disabled or restrictive CORS filters**: Pure machine-to-machine integration pipelines have zero browser callers; removing filters and interceptors entirely eliminates attack surface and dead code without altering service-to-service HTTP semantics.
- **Chose unified `BefeCorsConfig` shared between JAX-RS and raw Operations server / not independent configuration**: Prevents policy drift between port 8080 (clinical) and port 8090 (operations) while keeping trusted-origin management in a single authoritative class.
- **Chose omitting `Access-Control-Allow-Credentials: true` / not enabling credentials**: Iris BEFE relies on bearer tokens in `Authorization` headers (via Elytron OIDC), not ambient browser cookies; omitting credentials prevents browser credential leakage and CSRF attack vectors.

**Approach & Touches**
- **Reference Analog**: Follows `calliope/.../PhiLoggingConfig.java` for externalized system property with environment variable fallback, fail-closed default, and thread-safe in-memory test overrides.
- **Key Touches**:
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/BefeCorsConfig.java`: Authoritative configuration manager.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/CorsFilter.java`: Hardened JAX-RS filter.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/server/OperationsServerManager.java`: Operations socket server CORS alignment.
  - `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/CorsFilter.java`: File removal.
  - `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/CorsFilter.java`: File removal.
  - `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/CorsFilter.java`: File removal.
  - `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/config/JpaRestfulServer.java`: Strip `CorsInterceptor` registration and imports.
  - `deployment/kubernetes/base/iris/iris-befe.yaml`: Add `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` container environment variable.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/BefeCorsFilterTest.java`: New comprehensive BEFE CORS test suite.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/OperationsServerManagerTest.java`: Update test 3 and add hardened operations CORS tests.
  - `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java`: Add CORS isolation architecture rule.

**Nuances, Risks & Corners**
- **Exact Origin Normalization**: Origins must be matched as structured tuples `(scheme, host, port)` via `java.net.URI` normalization (lowercase scheme and host, explicit port if non-default, no path/query/fragment). Subdomain suffixes (e.g. `clinical.harmonia.local.attacker.com`) and path prefixes must be strictly rejected.
- **Fail-Closed Default**: If no origins are configured, `BefeCorsConfig.getAllowedOrigins()` returns an empty set; all cross-origin requests are denied without throwing runtime initialization exceptions.
- **Cache Integrity**: `Vary: Origin` must always be included whenever `Access-Control-Allow-Origin` is returned dynamically to prevent proxy and browser cache poisoning between different origins.
- **Preflight vs Business Logic**: Preflight requests must never trigger Themis policy evaluation or instantiate `ThemisSecurityContext`.

**Contracts**  
`BefeCorsConfig` authoritative contracts:
```java
public final class BefeCorsConfig {
    public static final String PROPERTY_ALLOWED_ORIGINS = "harmonia.befe.cors.allowed-origins";
    public static final String ENV_ALLOWED_ORIGINS = "HARMONIA_BEFE_CORS_ALLOWED_ORIGINS";
    public static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS";
    public static final String ALLOWED_HEADERS = "Authorization, Content-Type, Accept, X-Correlation-Id";
    public static final String MAX_AGE_SECONDS = "86400";

    public static Set<String> getAllowedOrigins();
    public static String normalizeOrigin(String rawOrigin);
    public static boolean isOriginAllowed(String origin);
    public static void setAllowedOriginsOverrideForTesting(String origins);
    public static void resetAllowedOriginsForTesting();
}
```

**Testing**

- Must-hold: Trusted configured origin (`https://clinical.harmonia.local`) receives exact `Access-Control-Allow-Origin: https://clinical.harmonia.local` and `Vary: Origin`.
- Must-hold: Untrusted origin (`https://attacker.example`) receives no `Access-Control-Allow-Origin` header; untrusted preflight OPTIONS receives 403 Forbidden.
- Must-hold: Non-CORS request without `Origin` header processes normally with zero `Access-Control-*` headers emitted.
- Must-hold: Empty or absent configuration fails closed: no origins are granted cross-origin access.
- Must-hold: No response from BEFE JAX-RS or raw Operations server emits `Access-Control-Allow-Credentials: true` or `Access-Control-Allow-Origin: *`.
- Must-hold: Raw Operations server (port 8090) validates requesting origins against `BefeCorsConfig` and handles preflight OPTIONS without executing operations logic.
- Must-hold: Architecture test asserts zero CORS filters or interceptors registered in `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, and `mnemosyne-clinical`.
- Regression target: All existing 104 tests in `iris/iris-befe` remain green.

**Assumptions & Open Questions**

- **Significant Assumption (Bearer Authentication vs CORS Credentials)**: BEFE uses bearer-token authentication (`Authorization: Bearer <token>`) managed by WildFly Elytron OIDC, not ambient browser session cookies. Rationale: Omitting `Access-Control-Allow-Credentials: true` eliminates cross-origin credentialed leakage risks. Alternative: Externalize a credential toggle if cookie sessions are introduced in the future. Impact: Substantially hardened browser security posture.
- **Significant Assumption (Preflight Response Status)**: Successful preflight OPTIONS requests return HTTP 200 OK. Rationale: Preserves existing Harmonia behavior (`CorsFilter.java:40` and `OperationsServerManager.java:206`). Alternative: HTTP 204 No Content. Impact: Consistent with current Axios client handling across Iris SPAs.

**Delivery Steps**

**✓ Step 1: Remove CORS from Service-Only APIs and Add Architecture Guardrails**  
Goal: Eliminate browser CORS attack surface from service-only modules and enforce anti-regression architecture invariants.  
Scope: `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, `hestia/mnemosyne-clinical`, and `paradeigma/paradeigma-test`.  
Acceptance Criteria:
- [ ] `CorsFilter.java` removed from `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/`
- [ ] `CorsFilter.java` removed from `pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/`
- [ ] `CorsFilter.java` removed from `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/`
- [ ] `CorsInterceptor` and `CorsConfiguration` registration removed from `hestia/mnemosyne-clinical/.../JpaRestfulServer.java`
- [ ] Architecture rule added to `SecurityEnforcementArchitectureTest` asserting service-only modules register no CORS providers/interceptors and BEFE contains no wildcard CORS
- [ ] Modules compile cleanly and existing service-module unit/integration tests pass
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="SecurityEnforcementArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**✓ Step 2: Harden BEFE CORS, Align Operations Server, and Update Deployment Configuration**  
Goal: Implement externalized trusted-origin allow-listing, preflight short-circuiting, and fail-closed validation for BEFE JAX-RS and raw Operations server.  
Scope: `iris/iris-befe` (`BefeCorsConfig.java`, `CorsFilter.java`, `OperationsServerManager.java`, `BefeCorsFilterTest.java`, `OperationsServerManagerTest.java`) and `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `BefeCorsConfig` created with system property (`harmonia.befe.cors.allowed-origins`), environment variable (`HARMONIA_BEFE_CORS_ALLOWED_ORIGINS`), origin normalization, and fail-closed evaluation
- [ ] `CorsFilter` updated to implement combined `ContainerRequestFilter` and `ContainerResponseFilter` with `@PreMatching`
- [ ] Preflight OPTIONS for trusted origins short-circuits with 200 OK, exact origin, `Vary: Origin`, restricted methods, and restricted headers
- [ ] Preflight OPTIONS for untrusted origins aborts with 403 Forbidden and no CORS headers
- [ ] Outbound responses for trusted origins include exact `Access-Control-Allow-Origin` and `Vary: Origin`; untrusted and non-CORS requests receive no `Access-Control-Allow-Origin`
- [ ] `Access-Control-Allow-Credentials: true` and wildcard origins are never emitted
- [ ] `OperationsServerManager` aligned with `BefeCorsConfig` on port 8090, replacing wildcard CORS with exact matching and preflight validation
- [ ] `deployment/kubernetes/base/iris/iris-befe.yaml` updated to inject `HARMONIA_BEFE_CORS_ALLOWED_ORIGINS` with deployed Iris origins (`clinical`, `console`, `admin`)
- [ ] Automated tests in `BefeCorsFilterTest` verify: trusted origin, untrusted origin, no-Origin, multiple origins, empty config, credentials absence, preflight isolation, and non-reflection
- [ ] `OperationsServerManagerTest` updated and passing for hardened preflight and origin verification
Verification: `mvn test -pl iris/iris-befe` → green



**Summary**
- Implemented Task 05 / Step 01 CORS hardening: BEFE now uses an externalized, fail-closed trusted-origin allow-list, while service-only APIs no longer advertise browser CORS.
- Preserved existing authentication/authorization boundaries and stopped at Step 01; no Step 02 logging remediation was performed.

**Changes**
- Added shared BEFE CORS configuration and hardened both the JAX-RS filter and raw Operations server to use exact trusted-origin matching, `Vary: Origin`, restricted methods/headers, trusted preflight handling, and no wildcard or credentials headers.
- Removed CORS support from `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, and `mnemosyne-clinical`, and added architecture guardrails to prevent regression.
- Updated BEFE deployment config with externalized trusted origins and added/updated focused BEFE and operations-server CORS tests.

**Verification**
- Ran `mvn test -pl iris/iris-befe` and `mvn test -pl paradeigma/paradeigma-test -am -Dtest="SecurityEnforcementArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`; both passed.

**Notes**
- Deferred Step 02 findings unchanged: payload logging/stdout, response-body logging, raw HL7 DEBUG logging, Matrix/Synapse exception leakage, `ThemisSecurityContext`/`ThemisPrincipal` string-safety, PHI logger routing, and framework wire-logging controls were intentionally left untouched.

