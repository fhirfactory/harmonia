---
sessionId: session-260922-094137-1lj3
---

# Requirements

Establish a trusted, identity-provider-neutral authentication boundary for Iris BEFE clinical APIs (`/api/fhir/*`) at the WildFly container layer, ensuring authenticated callers are exposed via Jakarta/JAX-RS `SecurityContext.getUserPrincipal()` to `ThemisClinicalAuthorizationFilter` as `PrincipalType.HUMAN` using the validated `sub` claim.

### Scope
- **In Scope:**
  - Standard WildFly container-managed OIDC bearer-only authentication descriptors (`iris/iris-befe/src/main/webapp/WEB-INF/web.xml`, `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`).
  - Authoritative packaging resolution: standardize on the deployed WAR-on-WildFly container runtime, pinning `Dockerfile` to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` (aligning with `pom.xml` WildFly 31.0.1.Final).
  - Externalized deployment configuration in `docker-compose.yml` and Kubernetes manifests (`iris-befe.yaml`) with standard OIDC variables (`OIDC_ISSUER_URL`, `OIDC_CLIENT_ID`, etc.) and secure-by-default fail-closed behavior.
  - Test-scoped deterministic JWT signing helper for generating test tokens (valid, expired, wrong issuer/audience, corrupt signature) without introducing vendor SDKs.
  - Comprehensive unit and filter test coverage verifying missing/invalid principal handling (401), anti-spoofing header immunity, and authenticated-but-denied clinical access (403 Forbidden).
  - Explicit documentation of the container-level verification boundary, noting runtime test tooling limitations without creating prohibited application-level JWT validators.
- **Out of Scope:**
  - Clinical authorization policies or RBAC roles (`CLINICAL_ADMIN`, `CLINICAL_DOCTOR`, etc.) — Task 03 scope; valid authenticated clinical callers receive 403 Forbidden under current default-deny rules.
  - Application-level or filter-level JWT parsing/validation logic.
  - Operations API (`ThemisOperationsAuthorizer`) and Pylai FHIR gateway (`FhirSecurityInterceptor`) auth remediation (logged as discrepancies).
  - Ingress TLS termination architecture or non-clinical endpoint changes.

### Done When
- Iris BEFE clinical API endpoints (`/api/fhir/*`) are secured at the WildFly container layer using `elytron-oidc-client` bearer-only authentication.
- Unauthenticated requests or requests with invalid/expired/untrusted tokens are rejected at the container boundary with HTTP 401 Unauthorized (RFC 6750 `WWW-Authenticate: Bearer`).
- Valid cryptographically signed tokens establish a container `SecurityContext` principal with the validated `sub` claim, which `ThemisClinicalAuthorizationFilter` consumes as `PrincipalType.HUMAN`.
- Valid authenticated clinical requests reach Themis and return HTTP 403 Forbidden with a FHIR `OperationOutcome` due to absent clinical authorization policy (Task 03).
- Caller-controlled headers (`X-Harmonia-User`, `X-Harmonia-Role`, etc.) and arbitrary bearer text cannot establish or elevate identity.
- Full repository architecture tests (`*ArchitectureTest`) and BEFE test suites pass cleanly.

# Technical Design

### Decisions
- **Chose authoritative WAR-on-WildFly packaging with pinned base image / not bootable JAR or floating `latest` image**: The active deployment topology across Docker Compose and Kubernetes runs `iris-befe.war` on WildFly. Pinning `Dockerfile` to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` resolves runtime ambiguity, matches the project's WildFly 31 target, and leverages WildFly 31's native `elytron-oidc-client` subsystem without divergent Galleon bootable-JAR layering.
- **Chose WildFly container-managed OIDC bearer-only deployment configuration / not application-level JWT parsing or vendor SDKs**: Descriptors `WEB-INF/web.xml` and `WEB-INF/oidc.json` configure WildFly's native OIDC subsystem. Application code and `ThemisClinicalAuthorizationFilter` remain strictly decoupled from cryptographic validation and identity provider specifics (Keycloak, Entra, Auth0).
- **Chose container 401 for unauthenticated requests and filter 403 OperationOutcome for authenticated requests / not filter-level token parsing**: Servlet container authentication runs before JAX-RS `@PreMatching` filters. Missing or invalid bearer tokens are rejected by the container with HTTP 401. Requests passing container authentication reach `ThemisClinicalAuthorizationFilter`, where the trusted principal is evaluated by Themis and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (until Task 03).
- **Chose `sub` claim as default stable principal identifier / not email or display name**: OIDC RFC 7519 defines `sub` as the immutable, unique subject identifier. Using `sub` avoids identity instability, protects privacy, and satisfies Task 02 constraints.
- **Chose honest reporting of test runtime limitations / not synthetic in-filter JWT validators or disguised mocked tests**: The repository's test dependencies (`resteasy-undertow`) lack WildFly's Elytron OIDC subsystem. Unit and filter tests verify JAX-RS principal consumption and anti-spoofing; test JWT signing utilities provide deterministic tokens for container smoke testing; container integration limitations are documented transparently per parent task instructions.

### Approach & Touches
- **Packaging & Descriptors**:
  - `iris/iris-befe/Dockerfile`: Pin base image to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
  - `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`: Add `<login-config><auth-method>OIDC</auth-method></login-config>` and `<security-constraint>` protecting `/api/fhir/*` and `/fhir/*` with `<auth-constraint>`.
  - `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`: Add provider URL (`${env.OIDC_ISSUER_URL}`), client ID (`${env.OIDC_CLIENT_ID}`), `bearer-only: true`, `principal-attribute: sub`, `verify-token-audience: true`, and fail-closed defaults.
- **Deployment Manifests**:
  - `docker-compose.yml`: Wire externalized OIDC environment variables (`OIDC_ISSUER_URL`, `OIDC_CLIENT_ID`, `OIDC_AUDIENCE`) for `befe` service.
  - `deployment/kubernetes/base/iris/iris-befe.yaml`: Add ConfigMap/Secret environment bindings for OIDC configuration with secure defaults.
- **Application Seam & Tests**:
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java`: Keep consuming `SecurityContext.getUserPrincipal()` -> `ThemisPrincipal(sub, PrincipalType.HUMAN, "harmonia-clinical", Map.of())`; defense-in-depth 401 `OperationOutcome` if an unauthenticated request reaches JAX-RS.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/security/OidcTestTokenHelper.java`: Add lightweight test utility using standard `java.security` to generate deterministic RSA keys and signed test JWTs (valid, expired, wrong issuer/audience, bad signature) for test verification.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/ThemisClinicalAuthorizationFilterTest.java`: Maintain and expand anti-spoofing tests, verifying header immunity, principal extraction, and default-deny 403 behavior.

### Nuances, Risks & Corners
- **Container vs Filter 401 Boundary**: Container bearer-only rejection returns HTTP 401 (with `WWW-Authenticate: Bearer`) before JAX-RS executes. The filter's `abortWithOutcome` handles JAX-RS level defense-in-depth and 403 Forbidden outcomes. Test assertions must reflect this lifecycle distinction.
- **Fail-Closed on Missing Configuration**: When `OIDC_ISSUER_URL` or provider trust is missing/unreachable at runtime, WildFly's OIDC subsystem fails closed, rejecting incoming bearer tokens.
- **Operations & Non-Clinical Isolation**: Port 8090 operations endpoints and non-clinical paths (`/operations/*`, `/iris/*`) remain unaffected by clinical security constraints.

# Testing

- **Missing Bearer Token**: Unauthenticated request to `/api/fhir/Person` is rejected by the container with HTTP 401 Unauthorized (`WWW-Authenticate: Bearer`).
- **Malformed / Arbitrary Bearer Text**: Request with `Authorization: Bearer invalid-text` fails container authentication with HTTP 401.
- **Invalid Signature / Untrusted Key**: Bearer token signed with untrusted key is rejected by OIDC validation with HTTP 401.
- **Expired Token**: Bearer token with expired timestamp (`exp` in the past) fails container authentication with HTTP 401.
- **Wrong Issuer / Audience**: Token with mismatched `iss` or `aud` claims fails container authentication with HTTP 401.
- **Valid Authenticated Token -> 403 Forbidden**: Valid signed token establishes container principal (`sub`), passes container auth, reaches `ThemisClinicalAuthorizationFilter`, and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (proving authentication succeeds while authorization remains default-deny).
- **Anti-Spoofing Immunity**: Requests with spoofed `X-Harmonia-User`, `X-Harmonia-Role`, or `X-Principal-Id` headers cannot establish identity or elevate permissions.
- **Architecture Regressions**: All ArchUnit tests in `paradeigma-test` and existing BEFE test suites pass without regressions.

# Assumptions & Open Questions

- **Significant Assumption (Packaging Standard on WAR-on-WildFly)**: We standardize on the WAR deployment deployed to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` rather than maintaining dual packaging with Galleon bootable JARs.
  - *Rationale*: Aligns Dockerfile, Docker Compose, and Kubernetes deployments with the WildFly 31 Maven target and avoids inert bootable-JAR configuration.
  - *Alternative*: Changing Dockerfile to execute `iris-befe-bootable.jar` (rejected as a larger operational runtime change).
  - *Impact*: Subsystem configuration is managed via standard WAR descriptors (`web.xml`, `oidc.json`).
- **Significant Assumption (Principal Type HUMAN Mapping)**: All validated OIDC bearer tokens for clinical endpoints map to `PrincipalType.HUMAN`.
  - *Rationale*: Explicitly instructed by the architecture decision; machine-to-machine workload identity (mTLS / SPIFFE) is deferred to a future task.
  - *Alternative*: Inferring service identity from claims or network origin (explicitly rejected).
- **Significant Assumption (In-Repo Integration Test Scope)**: Embedded `resteasy-undertow` test runtime does not execute the WildFly `elytron-oidc-client` subsystem.
  - *Rationale*: Standard in-process RESTEasy Undertow tests cannot bootstrap WildFly subsystems without full Arquillian/container infrastructure. We provide deterministic test token generators and unit/filter verifications, and document the container runtime verification contract honestly.
  - *Alternative*: Implementing a custom JWT filter solely for unit tests (strictly prohibited by parent task rules).

# Delivery Steps

### ✓ Step 1: Configure WildFly Container OIDC Authentication & Authoritative Packaging
Goal: Establish container-managed OIDC bearer authentication descriptors and pin the authoritative WildFly WAR runtime packaging.
Scope: `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`, `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`, `iris/iris-befe/Dockerfile`, `docker-compose.yml`, `deployment/kubernetes/base/iris/iris-befe.yaml`.
Acceptance Criteria:
- [ ] `iris/iris-befe/Dockerfile` pins the base image to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
- [ ] `web.xml` declares `<login-config><auth-method>OIDC</auth-method></login-config>` and `<security-constraint>` with `<auth-constraint>` for `/api/fhir/*` and `/fhir/*`.
- [ ] `WEB-INF/oidc.json` is created with externalized environment properties for issuer, client ID, audience verification, `bearer-only: true`, and `principal-attribute: sub`.
- [ ] `docker-compose.yml` and `deployment/kubernetes/base/iris/iris-befe.yaml` configure externalized OIDC environment variables without hardcoded secrets.
- [ ] WAR package builds cleanly without compilation or descriptor errors.
Verification: `mvn clean package -pl iris/iris-befe -DskipTests` → green

### ✓ Step 2: Implement Test Token Harness, Security Filter Verification & Anti-Spoof Test Suite
Goal: Provide deterministic test token utilities, verify principal extraction and anti-spoofing invariants in `ThemisClinicalAuthorizationFilter`, and document container integration verification.
Scope: `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/security/OidcTestTokenHelper.java`, `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/ThemisClinicalAuthorizationFilterTest.java`, `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java`.
Acceptance Criteria:
- [ ] `OidcTestTokenHelper` generates cryptographically signed test JWTs (valid, expired, wrong issuer/audience, corrupt signature) using standard `java.security` for test use.
- [ ] `ThemisClinicalAuthorizationFilterTest` verifies that a valid container principal (`sub`) is correctly extracted into `ThemisPrincipal(sub, PrincipalType.HUMAN, "harmonia-clinical", Map.of())`.
- [ ] Tests verify that authenticated clinical requests return HTTP 403 Forbidden with a FHIR `OperationOutcome` (Task 01 default-deny).
- [ ] Tests verify that unauthenticated or missing container principals return HTTP 401 Unauthorized with a FHIR `OperationOutcome`.
- [ ] Anti-spoofing regression tests verify that caller-supplied headers (`X-Harmonia-*`, `X-Principal-Id`, arbitrary bearer text) are completely ignored.
- [ ] All unit tests in `iris-befe` and repository architecture tests pass.
Verification: `mvn test -pl iris/iris-befe,paradeigma/paradeigma-test -Dtest="*Test,*ArchitectureTest"` → green