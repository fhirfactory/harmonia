---
sessionId: session-260922-211811-ju5z
---

# Requirements

### Goal / Outcome
Eliminate wildcard CORS, arbitrary origin reflection, and wildcard-plus-credentials vulnerabilities across Harmonia. Restrict browser CORS strictly to the browser-facing Iris BEFE presentation API using externalized, fail-closed trusted-origin allow-listing with `Vary: Origin` caching safety. Completely remove browser CORS mechanisms from internal service-only integration APIs (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`).

### Scope
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

### Done When
- No API in Harmonia responds with `Access-Control-Allow-Origin: *` or combines wildcard origins with credentials.
- Iris BEFE protected endpoints emit `Access-Control-Allow-Origin` matching only explicitly configured, normalized trusted origins, accompanied by `Vary: Origin`.
- Untrusted origins and non-CORS requests (no `Origin` header) receive no `Access-Control-Allow-Origin` header, and untrusted preflight OPTIONS requests are rejected with 403 Forbidden.
- OPTIONS preflight requests remain strictly isolated from clinical business logic and authentication context creation.
- Service-only modules (`pylai-mllp-in`, `pylai-mllp-out`, `energeia-ponos`, `mnemosyne-clinical`) contain no browser CORS filters or interceptors.
- All existing BEFE tests (104 tests) and architecture invariant tests pass green.

# Technical Design

### Decisions
- **Chose combined PreMatching JAX-RS request and response filter for BEFE / not response-only or servlet filter**: Intercepts preflight `OPTIONS` before resource matching to guarantee zero business logic execution, returns 403 Forbidden for untrusted preflight origins, and decorates all outbound responses (including Themis 401/403 aborts) with `Vary: Origin` and exact allowed origins for trusted clients.
- **Chose complete removal of CORS from service-only APIs / not disabled or restrictive CORS filters**: Pure machine-to-machine integration pipelines have zero browser callers; removing filters and interceptors entirely eliminates attack surface and dead code without altering service-to-service HTTP semantics.
- **Chose unified `BefeCorsConfig` shared between JAX-RS and raw Operations server / not independent configuration**: Prevents policy drift between port 8080 (clinical) and port 8090 (operations) while keeping trusted-origin management in a single authoritative class.
- **Chose omitting `Access-Control-Allow-Credentials: true` / not enabling credentials**: Iris BEFE relies on bearer tokens in `Authorization` headers (via Elytron OIDC), not ambient browser cookies; omitting credentials prevents browser credential leakage and CSRF attack vectors.

### Approach & Touches
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

### Nuances, Risks & Corners
- **Exact Origin Normalization**: Origins must be matched as structured tuples `(scheme, host, port)` via `java.net.URI` normalization (lowercase scheme and host, explicit port if non-default, no path/query/fragment). Subdomain suffixes (e.g. `clinical.harmonia.local.attacker.com`) and path prefixes must be strictly rejected.
- **Fail-Closed Default**: If no origins are configured, `BefeCorsConfig.getAllowedOrigins()` returns an empty set; all cross-origin requests are denied without throwing runtime initialization exceptions.
- **Cache Integrity**: `Vary: Origin` must always be included whenever `Access-Control-Allow-Origin` is returned dynamically to prevent proxy and browser cache poisoning between different origins.
- **Preflight vs Business Logic**: Preflight requests must never trigger Themis policy evaluation or instantiate `ThemisSecurityContext`.

### Contracts
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

# Testing

- Must-hold: Trusted configured origin (`https://clinical.harmonia.local`) receives exact `Access-Control-Allow-Origin: https://clinical.harmonia.local` and `Vary: Origin`.
- Must-hold: Untrusted origin (`https://attacker.example`) receives no `Access-Control-Allow-Origin` header; untrusted preflight OPTIONS receives 403 Forbidden.
- Must-hold: Non-CORS request without `Origin` header processes normally with zero `Access-Control-*` headers emitted.
- Must-hold: Empty or absent configuration fails closed: no origins are granted cross-origin access.
- Must-hold: No response from BEFE JAX-RS or raw Operations server emits `Access-Control-Allow-Credentials: true` or `Access-Control-Allow-Origin: *`.
- Must-hold: Raw Operations server (port 8090) validates requesting origins against `BefeCorsConfig` and handles preflight OPTIONS without executing operations logic.
- Must-hold: Architecture test asserts zero CORS filters or interceptors registered in `pylai-mllp-in`, `pylai-mllp-out`, `energeia/ponos`, and `mnemosyne-clinical`.
- Regression target: All existing 104 tests in `iris/iris-befe` remain green.

# Assumptions & Open Questions

- **Significant Assumption (Bearer Authentication vs CORS Credentials)**: BEFE uses bearer-token authentication (`Authorization: Bearer <token>`) managed by WildFly Elytron OIDC, not ambient browser session cookies. Rationale: Omitting `Access-Control-Allow-Credentials: true` eliminates cross-origin credentialed leakage risks. Alternative: Externalize a credential toggle if cookie sessions are introduced in the future. Impact: Substantially hardened browser security posture.
- **Significant Assumption (Preflight Response Status)**: Successful preflight OPTIONS requests return HTTP 200 OK. Rationale: Preserves existing Harmonia behavior (`CorsFilter.java:40` and `OperationsServerManager.java:206`). Alternative: HTTP 204 No Content. Impact: Consistent with current Axios client handling across Iris SPAs.

# Delivery Steps

### ✓ Step 1: Remove CORS from Service-Only APIs and Add Architecture Guardrails
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

### ✓ Step 2: Harden BEFE CORS, Align Operations Server, and Update Deployment Configuration
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