Optional spending limit; leave empty for no limit: 40
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Inspect the current repository to determine the exact WildFly runtime/version and packaging used by `iris-befe`, and identify the supported smallest container-managed OIDC/OAuth2 bearer authentication mechanism that can populate JAX-RS/Jakarta `SecurityContext` for this runtime.

Please focus on:
- `iris/iris-befe` build/runtime packaging (bootable jar, WildFly layers, version pins, Docker base image, plugins)
- any existing WildFly/Elytron/OIDC capability or extension already present
- whether implementing container-managed bearer auth in this packaging is reasonably contained within this task or would require a material runtime/platform change
- likely config files and modules that would need changes if feasible

Return:
1. exact runtime/version evidence,
2. recommended WildFly mechanism and why,
3. any blockers/risks,
4. implementation surface with file paths/line numbers.



Fetch URL

Fetch URL

Fetch URL

Fetch URL

Fetch URL

Fetch URL

Fetch URL

Fetch URL

Fetch URL

Exploration is Ready

**Findings**

**1. Exact runtime/version and packaging evidence**

The repository has two conflicting WildFly packaging paths:

- **Declared Maven runtime:** root `pom.xml:57-72` targets Java 21 and declares `<wildfly.version>31.0.1.Final</wildfly.version>`.
- **BEFE Maven module:** `iris/iris-befe/pom.xml:31-35` is a WAR module. `iris/iris-befe/pom.xml:159-180` also configures `org.wildfly.plugins:wildfly-jar-maven-plugin:11.0.1.Final` with feature pack `wildfly@maven(org.jboss.universe:community-universe)#31.0.1.Final` and layers `jaxrs`, `cdi`, `jsonb`, `transactions`, and `management`.
- **Generated build output:** `iris/iris-befe/target/bootable-jar-build-artifacts/jar-content/provisioning.xml:3-18` confirms the generated bootable runtime uses WildFly `31.0.1.Final` and those layers. `target/iris-befe-bootable.jar` is present.
- **Actual Docker image path:** `iris/iris-befe/Dockerfile:16-22` does not run the generated bootable JAR. It uses the floating image `quay.io/wildfly/wildfly:latest-jdk21`, copies `target/iris-befe.war` to `standalone/deployments/ROOT.war`, and starts `standalone.sh`.
- **Local/Kubernetes deployment:** `docker-compose.yml:292-305` builds that Dockerfile, and `deployment/kubernetes/base/iris/iris-befe.yaml:46-66` deploys the resulting image `localhost:32000/harmonia/iris-befe:1.0.0-SNAPSHOT`.
- **Documentation expectation:** `docs/middleware/wildfly.md:1-3` and `deployment/README.md:43-46` describe the service as WildFly 31.0.1/WildFly 31, but the Docker base image is not pinned to that version.

Therefore, the **build intent is WildFly 31.0.1.Final**, but the **runtime actually used by Docker Compose/Kubernetes is not deterministically WildFly 31.0.1.Final**. It is whatever version is currently behind `latest-jdk21`. This is the primary packaging/runtime discrepancy.

**2. Existing WildFly security capability**

The generated bootable runtime contains base Elytron support: `iris/iris-befe/target/bootable-jar-build-artifacts/wildfly/standalone/configuration/standalone.xml:4-16` includes `org.wildfly.extension.elytron`.

However, the current generated provisioning configuration does not include an OIDC layer or OIDC deployment configuration. Targeted searches found no `org.wildfly.extension.elytron-oidc-client`, `elytron-oidc-client` subsystem configuration, `oidc.json`, or MicroProfile JWT configuration in the source or generated BEFE runtime.

`iris/iris-befe/src/main/webapp/WEB-INF/web.xml:18-29` contains only the web-app declaration, display name, and context listener. It has no `login-config`, `security-constraint`, OIDC auth method, or security roles.

The BEFE POM has no OIDC, MicroProfile JWT, or JWT validation dependency (`iris/iris-befe/pom.xml:37-146`). No application Java code performs JWT parsing or token validation.

**3. Recommended WildFly mechanism**

The smallest repository-compatible mechanism is the **native WildFly `elytron-oidc-client` subsystem**, using its bearer-only deployment mode.

WildFly 31’s official model/documentation exposes `subsystem=elytron-oidc-client`, `secure-deployment`, provider configuration, `bearer-only`, and `verify-token-audience`. The mechanism is designed to validate OIDC/OAuth bearer JWTs at the container boundary and to expose the authenticated web identity to the application. It is identity-provider-neutral: the provider URL/discovery document and trusted signing keys come from the configured OIDC issuer rather than from a Keycloak/Entra-specific application SDK.

For the BEFE clinical REST API, the expected configuration shape is:

- OIDC provider/discovery URL (issuer);
- client ID/audience;
- bearer-only mode, so unauthenticated API requests receive 401 rather than browser redirects;
- token-audience verification enabled;
- principal attribute set to `sub`;
- TLS certificate verification enabled for provider communication;
- externalized provider URL/client ID/trust configuration with no committed secrets.

The deployment can be configured through either of the WildFly-supported forms:

1. deployment configuration using `WEB-INF/web.xml` with `<login-config><auth-method>OIDC</auth-method></login-config>` plus `WEB-INF/oidc.json`; or
2. the `elytron-oidc-client` subsystem’s `secure-deployment`/provider configuration in the server runtime.

For this repository, the first form is likely the smallest if the current WAR-on-WildFly Docker path is retained. A server-level configuration is preferable if operations require all OIDC settings to remain outside the application archive, but no standalone XML/CLI configuration management currently exists in the repository.

**4. Why not MicroProfile JWT**

MicroProfile JWT is not currently present in BEFE’s dependencies, configuration, or generated layer set. Selecting it would require adding the appropriate WildFly MicroProfile JWT runtime layer/configuration and establishing `mp.jwt.*` trust properties. It is a possible standards-based alternative, but it is not an already-used mechanism and is not smaller than using WildFly 31’s native OIDC client for this WAR deployment.

The native Elytron OIDC client is preferable because:

- it is a WildFly 31 container capability explicitly intended for OIDC-secured deployments;
- it avoids adding application-level JWT parsing or a vendor SDK;
- it can be configured for bearer-only REST authentication;
- it allows the existing Task 01 filter to remain unchanged and consume `SecurityContext.getUserPrincipal()`.

**5. Compatibility with the existing Task 01 boundary**

The existing application path already has the correct consumer seam:

- `ThemisClinicalAuthorizationFilter.java:115-123` reads `requestContext.getSecurityContext().getUserPrincipal()` and returns 401 when the container has no usable principal.
- `ThemisClinicalAuthorizationFilter.java:126-127` obtains roles only from `SecurityContext.isUserInRole(...)`.
- `ThemisClinicalAuthorizationFilter.java:245-250` converts the trusted container principal name into `ThemisPrincipal` with `PrincipalType.HUMAN`.

No Java-side authentication implementation should be added to the filter or REST resources. With the OIDC configuration setting the principal attribute to `sub`, the existing filter can receive the validated stable subject and construct the current human principal without changing authorization policy.

**6. Is implementation reasonably contained?**

**The authentication mechanism itself is reasonably contained and does not require a new identity product.** It belongs in WildFly deployment/runtime configuration, not in BEFE business code.

There is nevertheless a required packaging decision before implementation can be safely verified:

- **Option A — retain the deployed WAR path:** pin `iris/iris-befe/Dockerfile` to a known WildFly 31.0.1.Final JDK 21 image (or otherwise guarantee the supported runtime), add the OIDC deployment configuration, and externalize provider settings through Compose/Kubernetes configuration. This is the smallest change to the currently deployed path.
- **Option B — make the bootable JAR authoritative:** add the `elytron-oidc-client` Galleon layer to `iris/iris-befe/pom.xml`, add OIDC configuration to that bootable runtime, and change the Dockerfile to run `target/iris-befe-bootable.jar`. This removes the current WAR/full-image divergence but is a larger packaging change.

Without choosing one of these paths, an integration test cannot establish which WildFly version and subsystem schema is actually under test. The floating `latest-jdk21` image is an operational risk because it can change independently of the Maven-pinned 31.0.1 feature pack.

**7. Likely implementation/configuration surface**

If Option A is selected:

- `iris/iris-befe/src/main/webapp/WEB-INF/web.xml:18-29` — add the standard OIDC login configuration.
- `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json` — add provider URL, client/audience, bearer-only, audience verification, and `sub` principal configuration, using runtime/environment substitution and no secrets.
- `iris/iris-befe/Dockerfile:16-22` — pin the WildFly image and ensure the image includes the WildFly 31 OIDC subsystem.
- `docker-compose.yml:292-305` — provide explicitly test-scoped issuer/audience/trust configuration; do not add an auth bypass.
- `deployment/kubernetes/base/iris/iris-befe.yaml:46-66` — add environment/config references and Kubernetes Secret/ConfigMap wiring for issuer, audience, and trust configuration. No secret values should be committed.
- `deployment/kubernetes/base/ingress.yaml:1-90` — no change is required for container authentication itself if bearer validation occurs in WildFly; however, production bearer tokens still require HTTPS, and this manifest currently has no TLS/auth configuration.

If Option B is selected:

- `iris/iris-befe/pom.xml:159-180` — add the supported `elytron-oidc-client` Galleon layer to the bootable runtime.
- The bootable runtime/server configuration — add the OIDC subsystem/provider or deployment configuration.
- `iris/iris-befe/Dockerfile:18-22` — run the bootable JAR rather than copying the WAR into an unrelated floating full image.
- Compose/Kubernetes — pass only externalized provider configuration.

No new BEFE Java class, Themis policy, or clinical role vocabulary is necessary for either option.

**8. Verification limitation and risks**

The current BEFE tests do not exercise a real WildFly authentication boundary:

- `ThemisClinicalAuthorizationFilterTest.java:55-85` mocks a JAX-RS `SecurityContext` and injects a synthetic `Principal`.
- Existing RESTEasy tests do not provision an OIDC issuer, JWKS, or WildFly container authentication runtime.

A meaningful Task 02 integration test will require a pinned WildFly runtime plus a test OIDC issuer/JWKS that signs deterministic JWTs. It must verify signed token → WildFly authentication → `SecurityContext.getUserPrincipal()` → Task 01 filter. Mocking `SecurityContext` alone cannot prove this flow.

Additional risks:

- `latest-jdk21` makes the actual WildFly version and OIDC configuration schema non-reproducible.
- The generated bootable runtime currently omits the OIDC layer, while the Dockerfile does not use that runtime anyway.
- `deployment/kubernetes/base/ingress.yaml:14-90` routes HTTP traffic and has no TLS/auth annotations; this is not a reason to move validation into the ingress, but bearer-token production transport must ultimately be HTTPS.
- The repository contains no existing approved OIDC issuer, JWKS URL, truststore, or test issuer.

**Recommendation**

Use WildFly 31 native `elytron-oidc-client` in bearer-only mode, with the validated `sub` claim mapped to the container principal. Do not implement JWT parsing in Iris BEFE and do not introduce Keycloak, Entra ID, MicroProfile JWT, or a custom authentication filter.

Before coding, resolve the repository’s packaging inconsistency by making the WAR/full-WildFly path or the bootable-JAR path authoritative and pinning the runtime version. If retaining the currently deployed WAR path, the change is otherwise a contained deployment configuration change; if making the bootable JAR authoritative, adding the OIDC Galleon layer and changing the Docker entrypoint is a material packaging change but still does not require a new identity-product decision.

No repository files were modified and no tests/builds were run during this read-only exploration.

**Discoveries for future steps**

- Runtime evidence: Maven provisions WildFly `31.0.1.Final`, but Docker executes `quay.io/wildfly/wildfly:latest-jdk21`; pinning/alignment is required for reproducible OIDC integration testing.
- The current generated bootable layer set has base Elytron but no `elytron-oidc-client`; adding that layer is only relevant if the bootable-JAR path becomes authoritative.
- The smallest current-deployment seam is `WEB-INF/web.xml` plus WildFly OIDC deployment configuration; Task 01’s `SecurityContext` consumer should not be changed.
- Genuine validation requires a real/pinned WildFly runtime and test OIDC/JWKS issuer; existing Mockito/RESTEasy tests cannot establish container-level authentication.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Feedback**  
Emphasize a delivery plan for implementing Task 02 only, using the approved architecture discovered so far:
- WildFly/container-managed OIDC/OAuth2 bearer authentication for Iris BEFE clinical APIs
- no application-level JWT parsing or validation
- preserve Task 01 clinical authorization boundary and anti-spoofing behavior
- current key risk: repository packaging/runtime mismatch (Maven provisions WildFly 31.0.1.Final bootable runtime, but Docker currently runs a floating `quay.io/wildfly/wildfly:latest-jdk21` WAR deployment)
- determine the smallest contained implementation path that is actually compatible with the current runtime/packaging and provide verification steps, including at least one meaningful container-level integration test if feasible
- do not broaden into operations auth, Pylai auth, Task 03 authorization policy, CORS, audit changes, or ingress/TLS redesign



**Requirements**

Establish a trusted, identity-provider-neutral authentication boundary for Iris BEFE clinical APIs (`/api/fhir/*`) at the WildFly container layer, ensuring that authenticated callers are exposed via Jakarta/JAX-RS `SecurityContext.getUserPrincipal()` to `ThemisClinicalAuthorizationFilter` as `PrincipalType.HUMAN` using the validated `sub` claim.

**Scope**
- **In Scope:**
    - WildFly container-managed OpenID Connect / OAuth 2.0 bearer-only authentication configuration (`web.xml`, `oidc.json`, Galleon bootable layers, Docker base image pin).
    - Externalized deployment environment configuration in `docker-compose.yml` and Kubernetes manifests (`iris-befe.yaml`) using standard OIDC variables (`OIDC_ISSUER_URL`, `OIDC_CLIENT_ID`, etc.) with fail-closed defaults.
    - Test-scoped deterministic JWT signing and verification harness for container/runtime integration testing without vendor SDK dependencies or authentication bypasses.
    - Comprehensive unit and integration test coverage for missing, malformed, expired, incorrectly signed, and valid bearer tokens, plus anti-spoofing regression tests.
- **Out of Scope:**
    - Clinical authorization policies or RBAC roles (`CLINICAL_ADMIN`, `CLINICAL_DOCTOR`, etc.) — Task 03 scope; valid authenticated clinical callers receive 403 Forbidden under current default-deny rules.
    - Application-level or filter-level JWT parsing/validation logic.
    - Operations API (`ThemisOperationsAuthorizer`) and Pylai FHIR gateway (`FhirSecurityInterceptor`) auth remediation (logged as discrepancies).
    - Ingress TLS termination architecture or non-clinical endpoint changes.

**Done When**
- Iris BEFE authenticates clinical API requests exclusively at the WildFly container boundary using standard OIDC bearer tokens.
- Missing, malformed, expired, invalidly signed, or wrong-audience tokens return HTTP 401 Unauthorized with a FHIR `OperationOutcome`.
- Valid cryptographically signed tokens establish a Jakarta `SecurityContext` principal with the validated `sub` claim, which `ThemisClinicalAuthorizationFilter` consumes as `PrincipalType.HUMAN`.
- Valid authenticated clinical requests reach Themis and return HTTP 403 Forbidden due to absent clinical authorization policy (Task 03).
- Caller-controlled headers (`X-Harmonia-User`, `X-Harmonia-Role`, etc.) and arbitrary bearer text cannot establish or elevate identity.
- All architecture tests (`*ArchitectureTest`) and BEFE test suites pass cleanly.

**Technical Design**

**Decisions**
- **Chose WildFly native `elytron-oidc-client` bearer-only deployment configuration / not custom JAX-RS JWT filter or vendor SDK (Keycloak/Entra/Auth0 SDK)**: WildFly 31 natively supports standard OIDC token validation at the container level via `WEB-INF/web.xml` and `WEB-INF/oidc.json`, ensuring application business logic and `ThemisClinicalAuthorizationFilter` remain decoupled from token parsing and vendor lock-in.
- **Chose `sub` claim as default stable principal identifier / not email or display name**: OIDC RFC 7519 defines `sub` as the unique, immutable subject identifier; avoiding email and display names prevents identity ambiguity and protects privacy.
- **Chose pinned Docker base image `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` and Galleon layer `elytron-oidc-client` / not unpinned floating `latest-jdk21`**: Guarantees runtime parity between Maven bootable JAR build (`31.0.1.Final`) and container deployments, avoiding subsystem schema divergence.
- **Chose deterministic test-scoped RSA key-pair & JWT signer / not `AUTH_DISABLED=true` or localhost bypass**: Adheres strictly to security guardrails by testing cryptographic validation paths end-to-end without introducing security backdoors into production code.

**Approach & Touches**
- **Container Descriptors & Runtime Configuration**:
    - `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`: Add `<login-config><auth-method>OIDC</auth-method></login-config>` and `<security-constraint>` protecting `/api/fhir/*` and `/fhir/*`.
    - `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`: Externalize provider URL (`${env.OIDC_ISSUER_URL}`), client ID (`${env.OIDC_CLIENT_ID}`), `bearer-only: true`, `principal-attribute: sub`, `verify-token-audience: true`, and fail-closed defaults.
    - `iris/iris-befe/pom.xml`: Add `<layer>elytron-oidc-client</layer>` to `wildfly-jar-maven-plugin` layers.
    - `iris/iris-befe/Dockerfile`: Pin base image to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
- **Deployment Manifests**:
    - `docker-compose.yml`: Wire externalized OIDC environment variables for `befe` service.
    - `deployment/kubernetes/base/iris/iris-befe.yaml`: Add ConfigMap/Secret references for OIDC parameters with secure defaults.
- **Application Integration & Tests**:
    - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java`: Retain consumption of `SecurityContext.getUserPrincipal()` -> `ThemisPrincipal(sub, PrincipalType.HUMAN, "harmonia-clinical", Map.of())`.
    - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/ThemisClinicalAuthorizationFilterTest.java`: Expand tests for container-authenticated principals, anti-spoofing invariants, and 401 vs 403 behavior.
    - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/OidcBearerAuthenticationIntegrationTest.java`: Add integration test verifying signed token -> container auth / mock OIDC validation -> `SecurityContext` -> Themis filter -> 403 default-deny.

**Nuances, Risks & Corner Cases**
- **Fail-Closed on Missing Configuration**: When `OIDC_ISSUER_URL` or trust material is unconfigured, the container must reject all incoming clinical bearer tokens with HTTP 401, never defaulting to an unauthenticated pass-through.
- **Anti-Spoof Invariant Preservation**: Ensure `ThemisClinicalAuthorizationFilter` continues to discard caller-supplied `X-Harmonia-*` or `Authorization: Bearer <arbitrary>` headers when no genuine container principal exists.
- **Operations & Non-Clinical Isolation**: Port 8090 operations endpoints and non-clinical paths must remain isolated from clinical OIDC filter rules.

**Testing**

- **Missing Credentials**: Request without `Authorization` header to `/api/fhir/Person` returns HTTP 401 with `OperationOutcome` (`IssueType.SECURITY`).
- **Malformed / Arbitrary Bearer Text**: `Authorization: Bearer not-a-jwt` returns HTTP 401.
- **Invalid Signature**: Bearer token signed with untrusted/unknown private key returns HTTP 401.
- **Expired Token**: Bearer token with `exp` in the past returns HTTP 401.
- **Mismatched Issuer / Audience**: Token with invalid `iss` or `aud` claims returns HTTP 401.
- **Valid Signed Token -> 403 Forbidden**: Token with valid signature, issuer, audience, and future expiry establishes container principal with `sub`, passes authentication, reaches `ThemisClinicalAuthorizationFilter`, and returns HTTP 403 Forbidden (due to absent clinical authorization policy).
- **Anti-Spoofing Non-Elevation**: Caller sending spoofed `X-Harmonia-User` / `X-Harmonia-Role` headers alongside invalid or missing tokens cannot bypass 401 or elevate permissions.
- **Architecture & Regression Guardrails**: All ArchUnit tests in `paradeigma-test` and existing BEFE test suites pass.

**Assumptions & Open Questions**

- **Significant Assumption (OIDC Runtime Engine)**: We assume WildFly 31.0.1.Final's native `elytron-oidc-client` subsystem is the approved container-managed mechanism for WAR deployments.
    - *Rationale*: Avoids introducing vendor SDKs or custom JWT filters while fulfilling the container-managed authentication requirement.
    - *Alternative*: SmallRye MicroProfile JWT feature pack.
    - *Impact*: Standardized deployment descriptor (`oidc.json` / `web.xml`) without application-level token parsing.
- **Significant Assumption (Service/Workload Identity Deferral)**: All OIDC authenticated tokens for BEFE clinical endpoints are treated as `PrincipalType.HUMAN`.
    - *Rationale*: Explicitly instructed by architecture decision; machine-to-machine workload identity (mTLS / SPIFFE) is deferred to a future task.
    - *Alternative*: Inspecting custom claims for service types (rejected to maintain identity-provider neutrality).

**Delivery Steps**

**Step 1: Configure WildFly Container OIDC Authentication & Packaging**  
Goal: Establish container-managed OIDC bearer authentication descriptors and resolve runtime packaging alignment for Iris BEFE.  
Scope: `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`, `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`, `iris/iris-befe/pom.xml`, `iris/iris-befe/Dockerfile`, `docker-compose.yml`, `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `web.xml` declares standard OIDC `<login-config>` with `<auth-method>OIDC</auth-method>` and security constraints for `/api/fhir/*` and `/fhir/*`.
- [ ] `WEB-INF/oidc.json` is created with externalized environment properties for issuer, client ID, audience verification, `bearer-only` mode, and `sub` principal attribute.
- [ ] `iris/iris-befe/pom.xml` includes `elytron-oidc-client` in Galleon bootable JAR layers.
- [ ] `iris/iris-befe/Dockerfile` pins the base image to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
- [ ] `docker-compose.yml` and `deployment/kubernetes/base/iris/iris-befe.yaml` configure externalized OIDC environment variables without committed secrets.
  Verification: `mvn clean package -pl iris/iris-befe -DskipTests` → green

**Step 2: Implement Authentication Integration Verification & Anti-Spoof Test Suite**  
Goal: Verify trusted container principal establishment, validate fail-closed token behaviors, and prove end-to-end integration through `ThemisClinicalAuthorizationFilter`.  
Scope: `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/ThemisClinicalAuthorizationFilterTest.java`, `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/OidcBearerAuthenticationIntegrationTest.java`, `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java`.  
Acceptance Criteria:
- [ ] Unit and integration tests verify rejection (401 Unauthorized + `OperationOutcome`) for missing credentials, arbitrary bearer text, malformed JWTs, invalid signatures, expired tokens, and mismatched issuer/audience.
- [ ] Tests verify that a valid cryptographically signed token populates `SecurityContext.getUserPrincipal()` with the validated `sub` claim and maps to `ThemisPrincipal(sub, PrincipalType.HUMAN, "harmonia-clinical", ...)`.
- [ ] Tests verify that an authenticated clinical request returns HTTP 403 Forbidden (default-deny) when evaluated by Themis, proving separation of authentication from authorization.
- [ ] Anti-spoofing regression tests verify that caller-controlled HTTP headers (`X-Harmonia-*`) cannot establish or elevate identity.
- [ ] Full module test suite and architecture tests pass without regressions.
  Verification: `mvn test -pl iris/iris-befe,paradeigma/paradeigma-test -Dtest="*Test,*ArchitectureTest"` → green



REVIEW THE PLAN

**Task description**  
Review the proposed delivery plan for Task 02.

**Feedback**  
Focus on whether the plan:
- properly handles the WildFly runtime/packaging mismatch as a first-class risk
- is narrow enough to stay within Task 02 scope
- includes meaningful container-level authentication verification, not only mocked SecurityContext tests
- preserves Task 01 authorization semantics and anti-spoofing invariants
- avoids smuggling in Task 03 authorization or unrelated security remediations



Changes Requested

**Verification**
- Confirmed `ThemisClinicalAuthorizationFilter` (`iris/iris-befe/.../security/ThemisClinicalAuthorizationFilter.java:116-124,245-253`) consumes `SecurityContext.getUserPrincipal()`, maps to `ThemisPrincipal(name, HUMAN, "harmonia-clinical", Map.of())`, and returns 401/403 with a FHIR `OperationOutcome`. The plan's principal/`sub` mapping and Task 01 consumer seam are accurate.
- Confirmed WildFly bootable-jar plugin targets `31.0.1.Final` with layers `jaxrs,cdi,jsonb,transactions,management` and no `elytron-oidc-client` (`iris/iris-befe/pom.xml:159-189`) — plan's proposed layer addition is factually the missing piece.
- Confirmed the packaging mismatch is real: `Dockerfile:16-19` deploys `target/iris-befe.war` onto the floating full image `quay.io/wildfly/wildfly:latest-jdk21`, NOT the bootable jar.
- Confirmed the test infrastructure is Mockito-only: `ThemisClinicalAuthorizationFilterTest` mocks `SecurityContext`; `FhirRestResourceIntegrationTest` directly instantiates resources with reflection injection; the only container-ish test deps are `resteasy-core/undertow/client` (`iris/iris-befe/pom.xml:127-145`). No Arquillian, embedded WildFly, Testcontainers, or JWKS/mock-issuer dependency exists.
- Scope discipline is otherwise good: the plan correctly excludes Operations/Pylai auth, Task 03 policy, CORS, PHI logging, and ingress/TLS redesign, and correctly expects authenticated clinical callers to receive 403.

**Issues**
1. **Integration verification is not grounded and risks the explicitly-prohibited approach (core requirement of the task).** Section 14 of the parent task demands a *meaningful container-level* test: signed token → WildFly OIDC → `SecurityContext.getUserPrincipal()` → filter, and explicitly forbids substituting application-level JWT validation. WildFly OIDC validation is performed by the `elytron-oidc-client` subsystem, which **cannot** be exercised by the available `resteasy-undertow` runtime or by Mockito. The plan's Step 2 `OidcBearerAuthenticationIntegrationTest` describes "signed token -> container auth / **mock OIDC validation** -> SecurityContext -> Themis filter". As written, this either mocks/injects the `SecurityContext` (i.e., just another mocked-SecurityContext test — the exact thing the reviewer feedback says is insufficient) or performs in-test JWT validation (the prohibited application-level substitute). The plan adds **no** test harness capable of real container OIDC verification (no Arquillian, no Testcontainers running the pinned WildFly image with a test JWKS issuer, no dependency additions in Step 1). Fix: either (a) specify a concrete real-container harness (e.g., Testcontainers/Arquillian with WildFly 31 `elytron-oidc-client` + a deterministic test JWKS issuer such as WireMock, including the required test-scope dependencies), or (b) per the task, honestly declare the infrastructure limitation and demonstrate the closest valid runtime verification — and NOT dress a mocked/JWT-mock test up as container integration.

2. **Container bearer-only 401 vs Task 01's filter-produced FHIR `OperationOutcome` 401 is unresolved.** A servlet `<security-constraint>` with `auth-constraint` + `bearer-only` OIDC causes the container to reject unauthenticated/invalid-token requests with its own 401 (WWW-Authenticate) *before* the `@PreMatching` JAX-RS `ThemisClinicalAuthorizationFilter` runs. In that case the filter never executes and does not emit the FHIR `OperationOutcome` body. Yet the plan's Testing/Done-When repeatedly assert "HTTP 401 with `OperationOutcome`" for missing/malformed/invalid/expired/wrong-issuer/wrong-audience tokens (all container-rejected cases). The plan does not state who produces the 401 body or how the container 401 is reconciled with Task 01's OperationOutcome contract (e.g., error-page mapping, or a security-constraint that authenticates-but-does-not-require so the filter still runs). Without resolving this, the acceptance criteria in both steps are self-contradictory. Fix: define the exact container-auth configuration and the resulting 401 response contract, and align the test expectations accordingly.

3. **Incoherent packaging decision — Option A and Option B are mixed, making the OIDC layer inert.** Step 1 adds `elytron-oidc-client` to the *bootable jar* Galleon layers (Option B) but keeps the *WAR-on-full-image* Docker path and only pins the base image (Option A). Because `Dockerfile` deploys the WAR to a full WildFly server (which already ships the OIDC subsystem), the Galleon layer addition to the bootable jar is never exercised by the actual runtime — dead work — and the prior research explicitly flagged Option A vs Option B as a decision to resolve. Fix: pick one authoritative packaging path. If retaining the WAR-on-full-image path, drop the bootable-jar layer change (or switch the Dockerfile to run the bootable jar); the Step 1 `mvn package` green-build verification does not detect this inconsistency.

**Notes**
- Step 1's `mvn clean package -DskipTests` proves only that provisioning/build succeeds; it does not verify that OIDC configuration is actually effective at runtime, so it is a weak done-signal for the authentication behaviour that is the point of the task.
- The `sub`-as-principal mapping requires the container to set the principal name to `sub` (`principal-attribute`), which the plan notes; confirm the chosen WildFly config actually surfaces `sub` (not preferred_username) to `getUserPrincipal().getName()`.

**Discoveries for future steps**
- Dockerfile runtime path is WAR-on-full-`quay.io/wildfly/wildfly` image, NOT the bootable jar — any runtime OIDC config must land where the full server reads it, not only in Galleon layers.
- No Arquillian/embedded-WildFly/Testcontainers test deps exist in `iris-befe`; genuine container-OIDC verification requires new test infrastructure or an honest limitation report.
- `@PreMatching` filter runs after servlet-container auth, so container bearer-only rejection pre-empts the filter's OperationOutcome 401 — this interaction must be designed explicitly.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Feedback**  
Revise the plan to address the review findings explicitly:
- Treat the WildFly runtime/packaging mismatch as a concrete decision to resolve inside the plan. Pick one authoritative packaging path for this task; do not mix bootable-jar and WAR-on-full-image changes.
- The plan must explicitly account for servlet/container auth running before the JAX-RS `@PreMatching` filter. Clarify how 401 behavior will work for missing/invalid tokens and how that interacts with Task 01's existing FHIR `OperationOutcome` behavior.
- Do not claim a mocked SecurityContext or mocked JWT validation as container-level integration verification. Either propose a real container-auth verification harness that is feasible in-repo (for example a pinned WildFly runtime under test plus a deterministic JWKS/test issuer) or explicitly plan to report the exact limitation while still providing the closest valid runtime verification.
- Keep scope limited to Task 02 only: container-managed OIDC bearer auth for Iris BEFE clinical APIs, preserving Task 01 authorization semantics and anti-spoofing, without Operations/Pylai auth, Task 03 policy, audit, CORS, PHI logging, or ingress/TLS redesign.



**Requirements**

Establish a trusted, identity-provider-neutral authentication boundary for Iris BEFE clinical APIs (`/api/fhir/*`) at the WildFly container layer, ensuring authenticated callers are exposed via Jakarta/JAX-RS `SecurityContext.getUserPrincipal()` to `ThemisClinicalAuthorizationFilter` as `PrincipalType.HUMAN` using the validated `sub` claim.

**Scope**
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

**Done When**
- Iris BEFE clinical API endpoints (`/api/fhir/*`) are secured at the WildFly container layer using `elytron-oidc-client` bearer-only authentication.
- Unauthenticated requests or requests with invalid/expired/untrusted tokens are rejected at the container boundary with HTTP 401 Unauthorized (RFC 6750 `WWW-Authenticate: Bearer`).
- Valid cryptographically signed tokens establish a container `SecurityContext` principal with the validated `sub` claim, which `ThemisClinicalAuthorizationFilter` consumes as `PrincipalType.HUMAN`.
- Valid authenticated clinical requests reach Themis and return HTTP 403 Forbidden with a FHIR `OperationOutcome` due to absent clinical authorization policy (Task 03).
- Caller-controlled headers (`X-Harmonia-User`, `X-Harmonia-Role`, etc.) and arbitrary bearer text cannot establish or elevate identity.
- Full repository architecture tests (`*ArchitectureTest`) and BEFE test suites pass cleanly.

**Technical Design**

**Decisions**
- **Chose authoritative WAR-on-WildFly packaging with pinned base image / not bootable JAR or floating `latest` image**: The active deployment topology across Docker Compose and Kubernetes runs `iris-befe.war` on WildFly. Pinning `Dockerfile` to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` resolves runtime ambiguity, matches the project's WildFly 31 target, and leverages WildFly 31's native `elytron-oidc-client` subsystem without divergent Galleon bootable-JAR layering.
- **Chose WildFly container-managed OIDC bearer-only deployment configuration / not application-level JWT parsing or vendor SDKs**: Descriptors `WEB-INF/web.xml` and `WEB-INF/oidc.json` configure WildFly's native OIDC subsystem. Application code and `ThemisClinicalAuthorizationFilter` remain strictly decoupled from cryptographic validation and identity provider specifics (Keycloak, Entra, Auth0).
- **Chose container 401 for unauthenticated requests and filter 403 OperationOutcome for authenticated requests / not filter-level token parsing**: Servlet container authentication runs before JAX-RS `@PreMatching` filters. Missing or invalid bearer tokens are rejected by the container with HTTP 401. Requests passing container authentication reach `ThemisClinicalAuthorizationFilter`, where the trusted principal is evaluated by Themis and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (until Task 03).
- **Chose `sub` claim as default stable principal identifier / not email or display name**: OIDC RFC 7519 defines `sub` as the immutable, unique subject identifier. Using `sub` avoids identity instability, protects privacy, and satisfies Task 02 constraints.
- **Chose honest reporting of test runtime limitations / not synthetic in-filter JWT validators or disguised mocked tests**: The repository's test dependencies (`resteasy-undertow`) lack WildFly's Elytron OIDC subsystem. Unit and filter tests verify JAX-RS principal consumption and anti-spoofing; test JWT signing utilities provide deterministic tokens for container smoke testing; container integration limitations are documented transparently per parent task instructions.

**Approach & Touches**
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

**Nuances, Risks & Corners**
- **Container vs Filter 401 Boundary**: Container bearer-only rejection returns HTTP 401 (with `WWW-Authenticate: Bearer`) before JAX-RS executes. The filter's `abortWithOutcome` handles JAX-RS level defense-in-depth and 403 Forbidden outcomes. Test assertions must reflect this lifecycle distinction.
- **Fail-Closed on Missing Configuration**: When `OIDC_ISSUER_URL` or provider trust is missing/unreachable at runtime, WildFly's OIDC subsystem fails closed, rejecting incoming bearer tokens.
- **Operations & Non-Clinical Isolation**: Port 8090 operations endpoints and non-clinical paths (`/operations/*`, `/iris/*`) remain unaffected by clinical security constraints.

**Testing**

- **Missing Bearer Token**: Unauthenticated request to `/api/fhir/Person` is rejected by the container with HTTP 401 Unauthorized (`WWW-Authenticate: Bearer`).
- **Malformed / Arbitrary Bearer Text**: Request with `Authorization: Bearer invalid-text` fails container authentication with HTTP 401.
- **Invalid Signature / Untrusted Key**: Bearer token signed with untrusted key is rejected by OIDC validation with HTTP 401.
- **Expired Token**: Bearer token with expired timestamp (`exp` in the past) fails container authentication with HTTP 401.
- **Wrong Issuer / Audience**: Token with mismatched `iss` or `aud` claims fails container authentication with HTTP 401.
- **Valid Authenticated Token -> 403 Forbidden**: Valid signed token establishes container principal (`sub`), passes container auth, reaches `ThemisClinicalAuthorizationFilter`, and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (proving authentication succeeds while authorization remains default-deny).
- **Anti-Spoofing Immunity**: Requests with spoofed `X-Harmonia-User`, `X-Harmonia-Role`, or `X-Principal-Id` headers cannot establish identity or elevate permissions.
- **Architecture Regressions**: All ArchUnit tests in `paradeigma-test` and existing BEFE test suites pass without regressions.

**Assumptions & Open Questions**

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

**Delivery Steps**

**Step 1: Configure WildFly Container OIDC Authentication & Authoritative Packaging**  
Goal: Establish container-managed OIDC bearer authentication descriptors and pin the authoritative WildFly WAR runtime packaging.  
Scope: `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`, `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`, `iris/iris-befe/Dockerfile`, `docker-compose.yml`, `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `iris/iris-befe/Dockerfile` pins the base image to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
- [ ] `web.xml` declares `<login-config><auth-method>OIDC</auth-method></login-config>` and `<security-constraint>` with `<auth-constraint>` for `/api/fhir/*` and `/fhir/*`.
- [ ] `WEB-INF/oidc.json` is created with externalized environment properties for issuer, client ID, audience verification, `bearer-only: true`, and `principal-attribute: sub`.
- [ ] `docker-compose.yml` and `deployment/kubernetes/base/iris/iris-befe.yaml` configure externalized OIDC environment variables without hardcoded secrets.
- [ ] WAR package builds cleanly without compilation or descriptor errors.
  Verification: `mvn clean package -pl iris/iris-befe -DskipTests` → green

**Step 2: Implement Test Token Harness, Security Filter Verification & Anti-Spoof Test Suite**  
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



REVIEW THE PLAN

**Task description**  
Review the revised delivery plan for Task 02.

**Feedback**  
Focus on whether the revised plan now:
- picks a single authoritative runtime/packaging path,
- stays within Task 02 scope,
- correctly accounts for container authentication happening before the JAX-RS filter,
- has an honest verification strategy for container-level auth instead of a fake/mocked substitute.



Plan Approved

**Verification**
- **Packaging decision (prior Issue 3) resolved**: Confirmed `iris/iris-befe/Dockerfile:16-19` still deploys `target/iris-befe.war` as `ROOT.war` onto the floating full image `quay.io/wildfly/wildfly:latest-jdk21`. The revised plan now picks a single authoritative path — WAR-on-WildFly with the base image pinned to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` — and no longer adds the inert `elytron-oidc-client` Galleon layer to the bootable-jar plugin. The pom.xml/Galleon change was correctly dropped from Approach & Touches and Step 1. This is coherent: the full WildFly 31 server already ships the `elytron-oidc-client` subsystem, so `web.xml` + `oidc.json` descriptors in the WAR are the correct seam.
- **401 lifecycle (prior Issue 2) resolved**: Confirmed `ThemisClinicalAuthorizationFilter.java:116-124` has a defense-in-depth 401 `OperationOutcome` path when the container principal is missing, and 403 for authenticated-but-denied (lines 154-167). The revised plan now has an explicit Decision bullet and a "Container vs Filter 401 Boundary" nuance stating: container bearer-only rejection returns 401 (`WWW-Authenticate: Bearer`) before the `@PreMatching` filter runs; requests passing container auth reach the filter and get 403 `OperationOutcome`. Test expectations are aligned to this lifecycle (container 401 vs filter 401/403 OperationOutcome). Self-contradiction removed.
- **Integration verification (prior Issue 1) resolved**: The plan no longer dresses a mocked/JWT-mock test up as container integration. It explicitly documents that `resteasy-undertow` cannot bootstrap the Elytron OIDC subsystem, and honestly reports the limitation per parent task §14, while providing deterministic test token utilities and filter/anti-spoof unit verification. This matches the task's sanctioned fallback.
- **Scope discipline**: Correctly excludes Operations/Pylai auth, Task 03 policy, CORS, PHI logging, ingress/TLS redesign; correctly expects authenticated clinical callers to receive 403; preserves anti-spoofing and `sub`→`PrincipalType.HUMAN` mapping (matches filter lines 245-253 behavior).

**Issues**  
None blocking.

**Notes**
- `OidcTestTokenHelper` (Step 2) generates signed JWTs, but the only automated consumers described are filter unit tests that mock `SecurityContext` and therefore do not validate JWTs (validation is the container's job and prohibited in app code). The helper's automated value is thus ambiguous unless it feeds a documented docker-based smoke test, which is not operationalized as a verification step. This is a minor coherence/utility gap, not a core defect — the plan honestly frames tokens as being for documented container smoke testing.
- Step 1's `mvn clean package -DskipTests` only proves the WAR/descriptors build; it does not prove OIDC is effective at runtime. This is an inherently weak done-signal for authentication behaviour, but acceptable given the plan's transparent in-repo integration-test limitation and the task's explicit allowance to report that limitation.
- Confirm during implementation that the chosen WildFly OIDC config surfaces `sub` (via `principal-attribute`) to `getUserPrincipal().getName()` rather than `preferred_username`.

**Discoveries for future steps**
- Dockerfile runtime is WAR-on-full-`quay.io/wildfly/wildfly` image (not the bootable jar); OIDC descriptors belong in the WAR (`WEB-INF/web.xml` + `oidc.json`), read by the full server's built-in `elytron-oidc-client` subsystem.
- No Arquillian/embedded-WildFly/Testcontainers deps exist in `iris-befe`; genuine container-OIDC verification would require new test infra (e.g., Testcontainers with the pinned WildFly image + a WireMock JWKS issuer) — the plan opts for the sanctioned honest-limitation route instead.
- `@PreMatching` filter runs after servlet-container auth; with `<auth-constraint>` on `/api/fhir/*`, container 401 pre-empts the filter, so the filter's own 401 path is primarily a unit-test/defense-in-depth code path.



**Requirements**

Establish a trusted, identity-provider-neutral authentication boundary for Iris BEFE clinical APIs (`/api/fhir/*`) at the WildFly container layer, ensuring authenticated callers are exposed via Jakarta/JAX-RS `SecurityContext.getUserPrincipal()` to `ThemisClinicalAuthorizationFilter` as `PrincipalType.HUMAN` using the validated `sub` claim.

**Scope**
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

**Done When**
- Iris BEFE clinical API endpoints (`/api/fhir/*`) are secured at the WildFly container layer using `elytron-oidc-client` bearer-only authentication.
- Unauthenticated requests or requests with invalid/expired/untrusted tokens are rejected at the container boundary with HTTP 401 Unauthorized (RFC 6750 `WWW-Authenticate: Bearer`).
- Valid cryptographically signed tokens establish a container `SecurityContext` principal with the validated `sub` claim, which `ThemisClinicalAuthorizationFilter` consumes as `PrincipalType.HUMAN`.
- Valid authenticated clinical requests reach Themis and return HTTP 403 Forbidden with a FHIR `OperationOutcome` due to absent clinical authorization policy (Task 03).
- Caller-controlled headers (`X-Harmonia-User`, `X-Harmonia-Role`, etc.) and arbitrary bearer text cannot establish or elevate identity.
- Full repository architecture tests (`*ArchitectureTest`) and BEFE test suites pass cleanly.

**Technical Design**

**Decisions**
- **Chose authoritative WAR-on-WildFly packaging with pinned base image / not bootable JAR or floating `latest` image**: The active deployment topology across Docker Compose and Kubernetes runs `iris-befe.war` on WildFly. Pinning `Dockerfile` to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` resolves runtime ambiguity, matches the project's WildFly 31 target, and leverages WildFly 31's native `elytron-oidc-client` subsystem without divergent Galleon bootable-JAR layering.
- **Chose WildFly container-managed OIDC bearer-only deployment configuration / not application-level JWT parsing or vendor SDKs**: Descriptors `WEB-INF/web.xml` and `WEB-INF/oidc.json` configure WildFly's native OIDC subsystem. Application code and `ThemisClinicalAuthorizationFilter` remain strictly decoupled from cryptographic validation and identity provider specifics (Keycloak, Entra, Auth0).
- **Chose container 401 for unauthenticated requests and filter 403 OperationOutcome for authenticated requests / not filter-level token parsing**: Servlet container authentication runs before JAX-RS `@PreMatching` filters. Missing or invalid bearer tokens are rejected by the container with HTTP 401. Requests passing container authentication reach `ThemisClinicalAuthorizationFilter`, where the trusted principal is evaluated by Themis and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (until Task 03).
- **Chose `sub` claim as default stable principal identifier / not email or display name**: OIDC RFC 7519 defines `sub` as the immutable, unique subject identifier. Using `sub` avoids identity instability, protects privacy, and satisfies Task 02 constraints.
- **Chose honest reporting of test runtime limitations / not synthetic in-filter JWT validators or disguised mocked tests**: The repository's test dependencies (`resteasy-undertow`) lack WildFly's Elytron OIDC subsystem. Unit and filter tests verify JAX-RS principal consumption and anti-spoofing; test JWT signing utilities provide deterministic tokens for container smoke testing; container integration limitations are documented transparently per parent task instructions.

**Approach & Touches**
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

**Nuances, Risks & Corners**
- **Container vs Filter 401 Boundary**: Container bearer-only rejection returns HTTP 401 (with `WWW-Authenticate: Bearer`) before JAX-RS executes. The filter's `abortWithOutcome` handles JAX-RS level defense-in-depth and 403 Forbidden outcomes. Test assertions must reflect this lifecycle distinction.
- **Fail-Closed on Missing Configuration**: When `OIDC_ISSUER_URL` or provider trust is missing/unreachable at runtime, WildFly's OIDC subsystem fails closed, rejecting incoming bearer tokens.
- **Operations & Non-Clinical Isolation**: Port 8090 operations endpoints and non-clinical paths (`/operations/*`, `/iris/*`) remain unaffected by clinical security constraints.

**Testing**

- **Missing Bearer Token**: Unauthenticated request to `/api/fhir/Person` is rejected by the container with HTTP 401 Unauthorized (`WWW-Authenticate: Bearer`).
- **Malformed / Arbitrary Bearer Text**: Request with `Authorization: Bearer invalid-text` fails container authentication with HTTP 401.
- **Invalid Signature / Untrusted Key**: Bearer token signed with untrusted key is rejected by OIDC validation with HTTP 401.
- **Expired Token**: Bearer token with expired timestamp (`exp` in the past) fails container authentication with HTTP 401.
- **Wrong Issuer / Audience**: Token with mismatched `iss` or `aud` claims fails container authentication with HTTP 401.
- **Valid Authenticated Token -> 403 Forbidden**: Valid signed token establishes container principal (`sub`), passes container auth, reaches `ThemisClinicalAuthorizationFilter`, and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (proving authentication succeeds while authorization remains default-deny).
- **Anti-Spoofing Immunity**: Requests with spoofed `X-Harmonia-User`, `X-Harmonia-Role`, or `X-Principal-Id` headers cannot establish identity or elevate permissions.
- **Architecture Regressions**: All ArchUnit tests in `paradeigma-test` and existing BEFE test suites pass without regressions.

**Assumptions & Open Questions**

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

**Delivery Steps**

*** Step 1: Configure WildFly Container OIDC Authentication & Authoritative Packaging**  
Goal: Establish container-managed OIDC bearer authentication descriptors and pin the authoritative WildFly WAR runtime packaging.  
Scope: `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`, `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`, `iris/iris-befe/Dockerfile`, `docker-compose.yml`, `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `iris/iris-befe/Dockerfile` pins the base image to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
- [ ] `web.xml` declares `<login-config><auth-method>OIDC</auth-method></login-config>` and `<security-constraint>` with `<auth-constraint>` for `/api/fhir/*` and `/fhir/*`.
- [ ] `WEB-INF/oidc.json` is created with externalized environment properties for issuer, client ID, audience verification, `bearer-only: true`, and `principal-attribute: sub`.
- [ ] `docker-compose.yml` and `deployment/kubernetes/base/iris/iris-befe.yaml` configure externalized OIDC environment variables without hardcoded secrets.
- [ ] WAR package builds cleanly without compilation or descriptor errors.
  Verification: `mvn clean package -pl iris/iris-befe -DskipTests` → green

**Step 2: Implement Test Token Harness, Security Filter Verification & Anti-Spoof Test Suite**  
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



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Priorities:
- Implement Task 02 only, using WildFly/container-managed OIDC bearer authentication for Iris BEFE clinical APIs.
- Preserve Task 01 semantics; do not add app-level JWT parsing/validation and do not change Clinical authorization policy.
- Use the authoritative WAR-on-WildFly path for this task; pin the Docker runtime to WildFly 31.0.1.Final JDK 21 and configure OIDC via standard deployment descriptors/config.
- Container auth runs before the JAX-RS `@PreMatching` filter; missing/invalid bearer tokens should be rejected by the container, while authenticated users can still reach Task 01 and receive 403 until Task 03.
- Keep scope narrow: no Operations auth remediation, no Pylai auth remediation, no audit changes, no CORS/TLS redesign.
- Start with small command timeouts (60-120s). Use narrow/incremental commands only; avoid broad full-repo work unless necessary. Report exact commands run and outcomes.
- Known from previous steps: Docker currently deploys the WAR to `quay.io/wildfly/wildfly:latest-jdk21`; Maven pins WildFly 31.0.1.Final. Resolve that mismatch by pinning the Docker image, not by switching to bootable JAR.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Open file

Code is Ready

**Summary of Completed Work for Step 1**

**Overview**  
Configured WildFly container-managed OIDC bearer-only authentication descriptors and established authoritative WAR-on-WildFly container runtime packaging for Iris BEFE.

**Changes Made**
1. **Dockerfile Packaging (`iris/iris-befe/Dockerfile`)**:
    - Pinned the base container image from floating `quay.io/wildfly/wildfly:latest-jdk21` to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`, aligning with the Maven parent POM's WildFly 31.0.1.Final target.

2. **Web Application Descriptors (`iris/iris-befe/src/main/webapp/WEB-INF/web.xml`)**:
    - Declared `<login-config><auth-method>OIDC</auth-method></login-config>`.
    - Protected `/api/fhir/*` and `/fhir/*` endpoints via `<security-constraint>` with `<auth-constraint><role-name>*</role-name></auth-constraint>`.
    - Declared `<security-role><role-name>*</role-name></security-role>`.

3. **OIDC Client Configuration (`iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`)**:
    - Created deployment configuration for WildFly's `elytron-oidc-client` subsystem with:
        - `"client-id": "${env.OIDC_CLIENT_ID:iris-befe}"`
        - `"provider-url": "${env.OIDC_ISSUER_URL}"`
        - `"bearer-only": true`
        - `"ssl-required": "external"`
        - `"principal-attribute": "sub"`
        - `"verify-token-audience": true`

4. **Docker Compose (`docker-compose.yml`)**:
    - Configured `OIDC_ISSUER_URL`, `OIDC_CLIENT_ID`, and `OIDC_AUDIENCE` environment variables with default values for local development in the `befe` service definition.

5. **Kubernetes Deployment Manifests (`deployment/kubernetes/base/iris/iris-befe.yaml`)**:
    - Configured `OIDC_ISSUER_URL`, `OIDC_CLIENT_ID`, and `OIDC_AUDIENCE` environment variables under the `befe` container spec.

**Verification Performed**
- Reactor dependencies compiled and installed (`mvn install -pl calliope,themis/themis-api,themis -am -DskipTests`).
- Clean WAR packaging verified (`mvn clean package -pl iris/iris-befe -DskipTests` -> `BUILD SUCCESS`, generating `target/iris-befe.war`).
- Architecture test suite executed (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` -> 27 tests passed, 0 failures).
- Unit and integration tests for `iris-befe` executed (`mvn test -pl iris/iris-befe` -> 67 tests passed, 0 failures).

**Discoveries for future steps**
- **Execution commands & speed**: `mvn clean package -pl iris/iris-befe -DskipTests` completes in ~14.8s after installing upstream reactor modules (`calliope`, `themis-api`).
- **Reactor dependencies**: Running `mvn test -pl iris/iris-befe` runs very quickly (~4.5s) and executes all 67 unit and integration tests.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Please verify:
- the chosen implementation stays strictly at the WildFly/container boundary and does not introduce app-level JWT validation,
- the WAR-on-WildFly packaging path is coherent and the Docker pin aligns runtime with the repository's WildFly 31 target,
- `web.xml` and `WEB-INF/oidc.json` are valid for the selected WildFly mechanism,
- `/api/fhir/*` and `/fhir/*` are protected while non-clinical endpoints remain unaffected,
- config is externalized and secure-by-default with no committed secrets,
- no scope creep into Task 03, Operations/Pylai auth, audit, CORS, or TLS redesign,
- any silent misconfiguration risks or compatibility problems with WildFly 31 are clearly called out.



Implementation Approved

**Verification**
- **Dockerfile pin**: `iris/iris-befe/Dockerfile:16` is pinned to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`, aligning the runtime with the Maven WildFly 31.0.1.Final target (resolves the previous floating `latest-jdk21`). ✓
- **web.xml**: declares `<login-config><auth-method>OIDC</auth-method></login-config>` and a `<security-constraint>` with `<auth-constraint>` protecting `/api/fhir/*` and `/fhir/*`. ✓
- **oidc.json**: created with `bearer-only: true`, `principal-attribute: sub`, `verify-token-audience: true`, externalized `provider-url` (`${env.OIDC_ISSUER_URL}`, no default → fail-closed) and `client-id` (`${env.OIDC_CLIENT_ID:iris-befe}`), `ssl-required: external`. No app-level JWT parsing introduced. ✓
- **Endpoint scope**: Clinical resources resolve to `/api/fhir/*` (`@ApplicationPath("/api")` in `JaxRsActivator` + `@Path("/fhir/...")` on resources), which the constraint covers. Operations API is `/api/operations/*` on a separate embedded server (port 8090) and is NOT under `/api/fhir/*`, so it remains unaffected. ✓
- **Externalized config / no secrets**: `docker-compose.yml` (befe) and `deployment/kubernetes/base/iris/iris-befe.yaml` wire `OIDC_ISSUER_URL`/`OIDC_CLIENT_ID`/`OIDC_AUDIENCE`; no hardcoded credentials/secrets committed. ✓
- **Build**: `mvn clean package -pl iris/iris-befe -DskipTests` → BUILD SUCCESS (~ builds cleanly). Confirmed the WAR contains both `WEB-INF/web.xml` and `WEB-INF/oidc.json`. ✓
- **No scope creep**: `git status` shows changes confined to the five scoped files plus a benign plan doc (`.junie/plans/...`). `ThemisClinicalAuthorizationFilter` untouched (Step 2 scope). No Operations/Pylai/audit/CORS/TLS changes. ✓

**Issues**  
None blocking for Step 1 (all acceptance criteria met; verification command green). See Suggested alternative and Notes for a runtime-behavior risk that should be addressed before the feature is considered "done".

**Suggested alternative**  
In `web.xml` the `<auth-constraint>` uses `<role-name>*</role-name>` together with `<security-role><role-name>*</role-name></security-role>`. Per the Servlet spec, `*` in an auth-constraint means "any user in at least one of the roles declared in the deployment descriptor", whereas `**` means "any authenticated user regardless of role". Because WildFly's `elytron-oidc-client` only assigns roles it finds in the token's roles claim (and this deployment defines no concrete role→token mapping), the `*` form risks the container returning **403 at the container boundary** for authenticated-but-role-less callers — before the request reaches `ThemisClinicalAuthorizationFilter`. That would defeat the plan's core "Done When" behavior ("valid authenticated clinical requests reach Themis and return HTTP 403 with a FHIR OperationOutcome"), silently substituting a container 403 (no OperationOutcome) for the intended filter 403.
- **Better approach**: use `<role-name>**</role-name>` in the auth-constraint (and drop/adjust the `security-role *` declaration) so any successfully authenticated bearer principal passes container auth and reaches the filter, where Task 01 default-deny yields the 403 OperationOutcome.
- **Why better**: matches the documented intent ("authenticated users can still reach Task 01 and receive 403 until Task 03") and the standard WildFly OIDC bearer-only pattern for role-agnostic APIs.
- **Switching cost**: cheap (one/two-line descriptor edit).
- **Caveat**: This cannot be verified in-repo (no WildFly Elytron OIDC runtime in the `resteasy-undertow` test path), so it will not be caught by Step 2's JAX-RS-level tests either — hence flagging it now.

**Notes**
- **Unused `OIDC_AUDIENCE`**: `OIDC_AUDIENCE` is defined in `docker-compose.yml` and `iris-befe.yaml` but is never referenced by `oidc.json`. WildFly's `verify-token-audience: true` validates the token `aud` against the `client-id`, so audience is effectively bound to `OIDC_CLIENT_ID`. The extra env var is dead/misleading config (harmless because the default `iris-befe` matches the client-id). Consider either removing it or documenting that audience == client-id.
- **Container-runtime verification limitation**: The `elytron-oidc-client` subsystem is present in the stock WildFly 31 standalone.xml, so the `auth-method=OIDC` + `oidc.json` mechanism is valid for the selected runtime, but end-to-end token→SecurityContext behavior cannot be exercised by the in-repo test harness. This is consistent with the task's stated assumptions.

**Discoveries for future steps**
- Build speed: `mvn clean package -pl iris/iris-befe -DskipTests` completes fast (~15s) provided upstream reactor modules (`calliope`, `themis-api`) are already installed; otherwise run `mvn install -pl calliope,themis/themis-api,themis -am -DskipTests` first.
- Step 2 filter tests mock `SecurityContext` and run under `resteasy-undertow`; they will NOT exercise the container `web.xml`/`oidc.json` boundary, so the `*` vs `**` behavior above must be reasoned about manually, not asserted by unit tests.
- Clinical URL space is `/api/fhir/*` (ApplicationPath `/api` + resource `@Path("/fhir/...")`); Operations is `/api/operations/*` on the separate 8090 embedded server — relevant when reasoning about which paths the WildFly security-constraint governs.



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
The reviewer found one important runtime-behavior risk that should be fixed before we accept this step.

I agree with the reviewer: the current `web.xml` auth-constraint should allow any successfully authenticated bearer principal to pass container auth and reach `ThemisClinicalAuthorizationFilter`, rather than depending on container role mapping. Please adjust the descriptor accordingly so authenticated users are not at risk of a container-level 403 before Task 01 runs.

Concrete actions:
- Replace the current auth-constraint role usage with the servlet form that means "any authenticated user" rather than "authenticated user in one of the declared roles".
- Clean up any now-unnecessary `security-role` declaration tied to the old wildcard role approach.
- Re-check whether `OIDC_AUDIENCE` is dead config. If WildFly audience verification is driven by `client-id`, either remove the unused env var from compose/k8s or clearly align/document it so the configuration contract is not misleading.

Keep scope narrow:
- no Task 03 auth policy,
- no Operations/Pylai auth changes,
- no app-level JWT validation,
- no unrelated deployment redesign.

Execution discipline:
- use narrow commands with 60-120s timeouts,
- report exact commands run and results.

Known from previous steps:
- WAR-on-WildFly with Docker pinned to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` is the authoritative path for this task.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Summary of Completed Work for Step 1 (Feedback Addressed)**

Completed the configuration of WildFly container-managed OIDC bearer-only authentication for Iris BEFE. Addressed feedback by making the authentication constraint role-agnostic to ensure authenticated callers proceed to the application-level authorization filter.

**Changes Made**
1. **Web Application Descriptors (`iris/iris-befe/src/main/webapp/WEB-INF/web.xml`)**:
    - Updated `<auth-constraint>` to use `<role-name>**</role-name>`, allowing any successfully authenticated bearer token to pass container security and proceed to `ThemisClinicalAuthorizationFilter`.
    - Removed the now-unnecessary `<security-role>` declaration.

2. **Docker Compose (`docker-compose.yml`)**:
    - Removed the redundant/unused `OIDC_AUDIENCE` environment variable, relying on `client-id` for audience validation in `oidc.json`.

3. **Kubernetes Deployment Manifests (`deployment/kubernetes/base/iris/iris-befe.yaml`)**:
    - Removed the redundant `OIDC_AUDIENCE` environment variable.

**Verification Performed**
- Clean WAR packaging verified (`mvn clean package -pl iris/iris-befe -DskipTests` -> `BUILD SUCCESS`, confirming valid XML structure and build integrity).

**Discoveries for future steps**
- The `**` auth-constraint ensures that the container-level boundary delegates all downstream authorization decisions (including the default-deny 403) to the `ThemisClinicalAuthorizationFilter`, aligning with the intended behavior for clinical endpoints.
- The `OIDC_AUDIENCE` removal simplifies the configuration contract, as audience validation is now implicitly bound to the configured `client-id` in the WildFly OIDC subsystem.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Please specifically verify the retry changes:
- `web.xml` now allows any authenticated principal to pass container auth and reach `ThemisClinicalAuthorizationFilter`, rather than depending on container role mapping,
- removal/alignment of the unused `OIDC_AUDIENCE` config is correct and not misleading,
- step 1 still stays within scope and leaves non-clinical endpoints unaffected.



Implementation Approved

**Verification**
- **Retry fix — auth-constraint (`web.xml:35-37`)**: Now uses `<role-name>**</role-name>` (Servlet spec "any authenticated user"), replacing the prior `*` (role-scoped) form that risked a container-level 403 before `ThemisClinicalAuthorizationFilter`. The now-unnecessary `<security-role>` declaration was correctly removed. This matches the plan's "authenticated users reach Task 01 and receive filter 403" intent. ✓
- **Retry fix — `OIDC_AUDIENCE` removed**: `grep` confirms `OIDC_AUDIENCE` is gone from both `docker-compose.yml` (only `OIDC_ISSUER_URL`/`OIDC_CLIENT_ID` remain, lines 303-304) and `deployment/kubernetes/base/iris/iris-befe.yaml` (lines 60-63). No dead/misleading config; audience is bound to `client-id` via `verify-token-audience: true`. ✓
- **Dockerfile pin (`Dockerfile:16`)**: `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` aligns runtime with Maven WildFly 31.0.1.Final. ✓
- **oidc.json**: `bearer-only: true`, `principal-attribute: sub`, `verify-token-audience: true`, externalized `provider-url` (`${env.OIDC_ISSUER_URL}`, no default → fail-closed), `client-id` (`${env.OIDC_CLIENT_ID:iris-befe}`), `ssl-required: external`. No app-level JWT parsing. ✓
- **Endpoint scope**: constraint covers `/api/fhir/*` and `/fhir/*`; Operations API (`/api/operations/*`, port 8090) is not under the constraint and remains unaffected. ✓
- **Build**: `mvn clean package -pl iris/iris-befe -DskipTests` → BUILD SUCCESS (exit 0, ~fast). WAR contains `WEB-INF/web.xml` (1656 b) and `WEB-INF/oidc.json` (215 b). ✓
- **No scope creep**: `git status` shows only the five scoped files + new `oidc.json` + benign plan doc. `ThemisClinicalAuthorizationFilter` untouched (Step 2 scope); no Operations/Pylai/audit/CORS/TLS changes. ✓

**Issues**  
None. All Step 1 acceptance criteria are met, and the two prior concerns (`*`→`**` auth-constraint, `OIDC_AUDIENCE` cleanup) have been correctly resolved.

**Notes**
- **Container-runtime verification limitation** (unchanged from prior review, non-blocking): The `elytron-oidc-client` bearer flow (token→SecurityContext) cannot be exercised by the in-repo `resteasy-undertow` test harness. The `auth-method=OIDC` + `oidc.json` mechanism is valid for stock WildFly 31 standalone (subsystem present), consistent with the task's stated assumptions. The `**` semantics must be reasoned about manually — Step 2's JAX-RS-level tests won't assert it.

**Discoveries for future steps**
- Build: `mvn clean package -pl iris/iris-befe -DskipTests -q` completes in seconds when upstream reactor modules (`calliope`, `themis-api`) are already installed; otherwise run `mvn install -pl calliope,themis/themis-api,themis -am -DskipTests` first.
- WAR descriptor check: `unzip -l iris/iris-befe/target/iris-befe.war | grep WEB-INF` confirms `web.xml`/`oidc.json` packaging.



**Requirements**

Establish a trusted, identity-provider-neutral authentication boundary for Iris BEFE clinical APIs (`/api/fhir/*`) at the WildFly container layer, ensuring authenticated callers are exposed via Jakarta/JAX-RS `SecurityContext.getUserPrincipal()` to `ThemisClinicalAuthorizationFilter` as `PrincipalType.HUMAN` using the validated `sub` claim.

**Scope**
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

**Done When**
- Iris BEFE clinical API endpoints (`/api/fhir/*`) are secured at the WildFly container layer using `elytron-oidc-client` bearer-only authentication.
- Unauthenticated requests or requests with invalid/expired/untrusted tokens are rejected at the container boundary with HTTP 401 Unauthorized (RFC 6750 `WWW-Authenticate: Bearer`).
- Valid cryptographically signed tokens establish a container `SecurityContext` principal with the validated `sub` claim, which `ThemisClinicalAuthorizationFilter` consumes as `PrincipalType.HUMAN`.
- Valid authenticated clinical requests reach Themis and return HTTP 403 Forbidden with a FHIR `OperationOutcome` due to absent clinical authorization policy (Task 03).
- Caller-controlled headers (`X-Harmonia-User`, `X-Harmonia-Role`, etc.) and arbitrary bearer text cannot establish or elevate identity.
- Full repository architecture tests (`*ArchitectureTest`) and BEFE test suites pass cleanly.

**Technical Design**

**Decisions**
- **Chose authoritative WAR-on-WildFly packaging with pinned base image / not bootable JAR or floating `latest` image**: The active deployment topology across Docker Compose and Kubernetes runs `iris-befe.war` on WildFly. Pinning `Dockerfile` to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` resolves runtime ambiguity, matches the project's WildFly 31 target, and leverages WildFly 31's native `elytron-oidc-client` subsystem without divergent Galleon bootable-JAR layering.
- **Chose WildFly container-managed OIDC bearer-only deployment configuration / not application-level JWT parsing or vendor SDKs**: Descriptors `WEB-INF/web.xml` and `WEB-INF/oidc.json` configure WildFly's native OIDC subsystem. Application code and `ThemisClinicalAuthorizationFilter` remain strictly decoupled from cryptographic validation and identity provider specifics (Keycloak, Entra, Auth0).
- **Chose container 401 for unauthenticated requests and filter 403 OperationOutcome for authenticated requests / not filter-level token parsing**: Servlet container authentication runs before JAX-RS `@PreMatching` filters. Missing or invalid bearer tokens are rejected by the container with HTTP 401. Requests passing container authentication reach `ThemisClinicalAuthorizationFilter`, where the trusted principal is evaluated by Themis and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (until Task 03).
- **Chose `sub` claim as default stable principal identifier / not email or display name**: OIDC RFC 7519 defines `sub` as the immutable, unique subject identifier. Using `sub` avoids identity instability, protects privacy, and satisfies Task 02 constraints.
- **Chose honest reporting of test runtime limitations / not synthetic in-filter JWT validators or disguised mocked tests**: The repository's test dependencies (`resteasy-undertow`) lack WildFly's Elytron OIDC subsystem. Unit and filter tests verify JAX-RS principal consumption and anti-spoofing; test JWT signing utilities provide deterministic tokens for container smoke testing; container integration limitations are documented transparently per parent task instructions.

**Approach & Touches**
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

**Nuances, Risks & Corners**
- **Container vs Filter 401 Boundary**: Container bearer-only rejection returns HTTP 401 (with `WWW-Authenticate: Bearer`) before JAX-RS executes. The filter's `abortWithOutcome` handles JAX-RS level defense-in-depth and 403 Forbidden outcomes. Test assertions must reflect this lifecycle distinction.
- **Fail-Closed on Missing Configuration**: When `OIDC_ISSUER_URL` or provider trust is missing/unreachable at runtime, WildFly's OIDC subsystem fails closed, rejecting incoming bearer tokens.
- **Operations & Non-Clinical Isolation**: Port 8090 operations endpoints and non-clinical paths (`/operations/*`, `/iris/*`) remain unaffected by clinical security constraints.

**Testing**

- **Missing Bearer Token**: Unauthenticated request to `/api/fhir/Person` is rejected by the container with HTTP 401 Unauthorized (`WWW-Authenticate: Bearer`).
- **Malformed / Arbitrary Bearer Text**: Request with `Authorization: Bearer invalid-text` fails container authentication with HTTP 401.
- **Invalid Signature / Untrusted Key**: Bearer token signed with untrusted key is rejected by OIDC validation with HTTP 401.
- **Expired Token**: Bearer token with expired timestamp (`exp` in the past) fails container authentication with HTTP 401.
- **Wrong Issuer / Audience**: Token with mismatched `iss` or `aud` claims fails container authentication with HTTP 401.
- **Valid Authenticated Token -> 403 Forbidden**: Valid signed token establishes container principal (`sub`), passes container auth, reaches `ThemisClinicalAuthorizationFilter`, and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (proving authentication succeeds while authorization remains default-deny).
- **Anti-Spoofing Immunity**: Requests with spoofed `X-Harmonia-User`, `X-Harmonia-Role`, or `X-Principal-Id` headers cannot establish identity or elevate permissions.
- **Architecture Regressions**: All ArchUnit tests in `paradeigma-test` and existing BEFE test suites pass without regressions.

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Configure WildFly Container OIDC Authentication & Authoritative Packaging**  
Goal: Establish container-managed OIDC bearer authentication descriptors and pin the authoritative WildFly WAR runtime packaging.  
Scope: `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`, `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`, `iris/iris-befe/Dockerfile`, `docker-compose.yml`, `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `iris/iris-befe/Dockerfile` pins the base image to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
- [ ] `web.xml` declares `<login-config><auth-method>OIDC</auth-method></login-config>` and `<security-constraint>` with `<auth-constraint>` for `/api/fhir/*` and `/fhir/*`.
- [ ] `WEB-INF/oidc.json` is created with externalized environment properties for issuer, client ID, audience verification, `bearer-only: true`, and `principal-attribute: sub`.
- [ ] `docker-compose.yml` and `deployment/kubernetes/base/iris/iris-befe.yaml` configure externalized OIDC environment variables without hardcoded secrets.
- [ ] WAR package builds cleanly without compilation or descriptor errors.
  Verification: `mvn clean package -pl iris/iris-befe -DskipTests` → green

**Step 2: Implement Test Token Harness, Security Filter Verification & Anti-Spoof Test Suite**  
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



**Requirements**

Establish a trusted, identity-provider-neutral authentication boundary for Iris BEFE clinical APIs (`/api/fhir/*`) at the WildFly container layer, ensuring authenticated callers are exposed via Jakarta/JAX-RS `SecurityContext.getUserPrincipal()` to `ThemisClinicalAuthorizationFilter` as `PrincipalType.HUMAN` using the validated `sub` claim.

**Scope**
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

**Done When**
- Iris BEFE clinical API endpoints (`/api/fhir/*`) are secured at the WildFly container layer using `elytron-oidc-client` bearer-only authentication.
- Unauthenticated requests or requests with invalid/expired/untrusted tokens are rejected at the container boundary with HTTP 401 Unauthorized (RFC 6750 `WWW-Authenticate: Bearer`).
- Valid cryptographically signed tokens establish a container `SecurityContext` principal with the validated `sub` claim, which `ThemisClinicalAuthorizationFilter` consumes as `PrincipalType.HUMAN`.
- Valid authenticated clinical requests reach Themis and return HTTP 403 Forbidden with a FHIR `OperationOutcome` due to absent clinical authorization policy (Task 03).
- Caller-controlled headers (`X-Harmonia-User`, `X-Harmonia-Role`, etc.) and arbitrary bearer text cannot establish or elevate identity.
- Full repository architecture tests (`*ArchitectureTest`) and BEFE test suites pass cleanly.

**Technical Design**

**Decisions**
- **Chose authoritative WAR-on-WildFly packaging with pinned base image / not bootable JAR or floating `latest` image**: The active deployment topology across Docker Compose and Kubernetes runs `iris-befe.war` on WildFly. Pinning `Dockerfile` to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` resolves runtime ambiguity, matches the project's WildFly 31 target, and leverages WildFly 31's native `elytron-oidc-client` subsystem without divergent Galleon bootable-JAR layering.
- **Chose WildFly container-managed OIDC bearer-only deployment configuration / not application-level JWT parsing or vendor SDKs**: Descriptors `WEB-INF/web.xml` and `WEB-INF/oidc.json` configure WildFly's native OIDC subsystem. Application code and `ThemisClinicalAuthorizationFilter` remain strictly decoupled from cryptographic validation and identity provider specifics (Keycloak, Entra, Auth0).
- **Chose container 401 for unauthenticated requests and filter 403 OperationOutcome for authenticated requests / not filter-level token parsing**: Servlet container authentication runs before JAX-RS `@PreMatching` filters. Missing or invalid bearer tokens are rejected by the container with HTTP 401. Requests passing container authentication reach `ThemisClinicalAuthorizationFilter`, where the trusted principal is evaluated by Themis and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (until Task 03).
- **Chose `sub` claim as default stable principal identifier / not email or display name**: OIDC RFC 7519 defines `sub` as the immutable, unique subject identifier. Using `sub` avoids identity instability, protects privacy, and satisfies Task 02 constraints.
- **Chose honest reporting of test runtime limitations / not synthetic in-filter JWT validators or disguised mocked tests**: The repository's test dependencies (`resteasy-undertow`) lack WildFly's Elytron OIDC subsystem. Unit and filter tests verify JAX-RS principal consumption and anti-spoofing; test JWT signing utilities provide deterministic tokens for container smoke testing; container integration limitations are documented transparently per parent task instructions.

**Approach & Touches**
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

**Nuances, Risks & Corners**
- **Container vs Filter 401 Boundary**: Container bearer-only rejection returns HTTP 401 (with `WWW-Authenticate: Bearer`) before JAX-RS executes. The filter's `abortWithOutcome` handles JAX-RS level defense-in-depth and 403 Forbidden outcomes. Test assertions must reflect this lifecycle distinction.
- **Fail-Closed on Missing Configuration**: When `OIDC_ISSUER_URL` or provider trust is missing/unreachable at runtime, WildFly's OIDC subsystem fails closed, rejecting incoming bearer tokens.
- **Operations & Non-Clinical Isolation**: Port 8090 operations endpoints and non-clinical paths (`/operations/*`, `/iris/*`) remain unaffected by clinical security constraints.

**Testing**

- **Missing Bearer Token**: Unauthenticated request to `/api/fhir/Person` is rejected by the container with HTTP 401 Unauthorized (`WWW-Authenticate: Bearer`).
- **Malformed / Arbitrary Bearer Text**: Request with `Authorization: Bearer invalid-text` fails container authentication with HTTP 401.
- **Invalid Signature / Untrusted Key**: Bearer token signed with untrusted key is rejected by OIDC validation with HTTP 401.
- **Expired Token**: Bearer token with expired timestamp (`exp` in the past) fails container authentication with HTTP 401.
- **Wrong Issuer / Audience**: Token with mismatched `iss` or `aud` claims fails container authentication with HTTP 401.
- **Valid Authenticated Token -> 403 Forbidden**: Valid signed token establishes container principal (`sub`), passes container auth, reaches `ThemisClinicalAuthorizationFilter`, and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (proving authentication succeeds while authorization remains default-deny).
- **Anti-Spoofing Immunity**: Requests with spoofed `X-Harmonia-User`, `X-Harmonia-Role`, or `X-Principal-Id` headers cannot establish identity or elevate permissions.
- **Architecture Regressions**: All ArchUnit tests in `paradeigma-test` and existing BEFE test suites pass without regressions.

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Configure WildFly Container OIDC Authentication & Authoritative Packaging**  
Goal: Establish container-managed OIDC bearer authentication descriptors and pin the authoritative WildFly WAR runtime packaging.  
Scope: `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`, `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`, `iris/iris-befe/Dockerfile`, `docker-compose.yml`, `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `iris/iris-befe/Dockerfile` pins the base image to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
- [ ] `web.xml` declares `<login-config><auth-method>OIDC</auth-method></login-config>` and `<security-constraint>` with `<auth-constraint>` for `/api/fhir/*` and `/fhir/*`.
- [ ] `WEB-INF/oidc.json` is created with externalized environment properties for issuer, client ID, audience verification, `bearer-only: true`, and `principal-attribute: sub`.
- [ ] `docker-compose.yml` and `deployment/kubernetes/base/iris/iris-befe.yaml` configure externalized OIDC environment variables without hardcoded secrets.
- [ ] WAR package builds cleanly without compilation or descriptor errors.
  Verification: `mvn clean package -pl iris/iris-befe -DskipTests` → green

*** Step 2: Implement Test Token Harness, Security Filter Verification & Anti-Spoof Test Suite**  
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



IMPLEMENTATION

**Task description**  
Fully complete step 2.

**Feedback**  
Priorities:
- Add the focused Task 02 tests and any minimal supporting code/test utilities needed for them.
- Preserve the architecture boundary: no app-level JWT parsing/validation in production code, no Task 03 authorization policy, no operations/Pylai/audit/CORS/TLS work.
- The test harness may generate deterministic signed JWTs for test/documentation purposes, but do not build a fake in-app validator or pretend mocked SecurityContext tests are container-level OIDC verification.
- Verify `ThemisClinicalAuthorizationFilter` consumes the trusted principal as HUMAN using the stable authenticated subject identifier, and that authenticated users still get 403 from Task 01 default-deny.
- Keep the anti-spoofing invariants explicit: raw `X-Harmonia-*`, `X-Principal-Id`, arbitrary bearer text, etc. must not establish or elevate identity.
- If the repository test infrastructure cannot exercise real WildFly OIDC container auth, document that limitation precisely in code comments/test naming or adjacent documentation as appropriate, and provide the closest valid verification without crossing the architecture boundary.
- Start with narrow commands and 60-120s timeouts. Use targeted test runs first; parallelize if there are many tests. Report exact commands and outcomes.

Known from previous steps:
- Runtime path is WAR-on-WildFly 31.0.1.Final-jdk21 with `web.xml` + `WEB-INF/oidc.json`.
- Container auth happens before JAX-RS; container 401 behavior cannot be proven by mocked SecurityContext tests alone.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Summary of Completed Work for Step 2**

Completed Step 2: Implemented deterministic test token harness utilities, verified principal extraction and anti-spoofing invariants in `ThemisClinicalAuthorizationFilter`, and documented the container-level verification boundary.

**Key Changes Implemented:**

1. **Deterministic Test Token Utility (`iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/security/OidcTestTokenHelper.java`)**:
    - Implemented standard JDK-based (`java.security` / RSA 2048 / SHA256withRSA) RFC 7519 JWT signing helper without introducing third-party identity vendor SDKs or creating prohibited in-application JWT validators.
    - Provides deterministic generators for test token variants: valid tokens, expired tokens, tokens with mismatched issuer/audience, tokens signed with untrusted keys, and tokens with corrupted signatures.
    - Added unit test coverage in `OidcTestTokenHelperTest.java` (6 tests).

2. **Security Filter Verification & Anti-Spoofing Tests (`iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/ThemisClinicalAuthorizationFilterTest.java`)**:
    - Verified that a valid container principal (`sub` claim) is extracted into `ThemisPrincipal(sub, PrincipalType.HUMAN, "harmonia-clinical", Map.of())`.
    - Verified that invalid/blank/anonymous container principals are rejected with HTTP 401 Unauthorized (`ThemisDecisionReason.PRINCIPAL_MISSING`).
    - Verified that valid signed bearer tokens in request headers without container authentication fail closed with HTTP 401, demonstrating that the application filter does not perform un-sandboxed in-filter token parsing.
    - Verified that authenticated clinical callers evaluated by `DefaultThemisAuthorizer` receive HTTP 403 Forbidden with a FHIR `OperationOutcome` under Task 01 default-deny rules.
    - Expanded anti-spoofing regression tests to verify that caller-supplied headers (`X-Harmonia-*`, `X-Principal-Id`, `X-Requester`, `X-User-Roles`, `X-Security-Scopes`, arbitrary bearer tokens) are strictly ignored.

3. **Boundary Documentation (`ThemisClinicalAuthorizationFilter.java` & `ThemisClinicalAuthorizationFilterTest.java`)**:
    - Explicitly documented the boundary distinction: WildFly container-level OIDC authentication (`elytron-oidc-client`) rejects unauthenticated/invalid tokens with container 401, while JAX-RS `ThemisClinicalAuthorizationFilter` consumes the validated container principal (`sub`) and enforces Themis default-deny (403) for clinical operations until Task 03 adds clinical authorization policies.

**Verification:**
- `mvn test -pl iris/iris-befe` -> 78 tests passed, 0 failures.
- `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` -> 27 architecture tests passed, 0 failures.

**Discoveries for future steps**
- **Execution commands & speed**: `mvn test -pl iris/iris-befe` completes in ~5.1s running all 78 tests across REST resources, health providers, serialization, authorizers, and test token helpers.
- **Test boundary**: Unit tests verify JAX-RS principal consumption and anti-spoofing via mocked `SecurityContext`; actual cryptographic token rejection (container 401) is executed at the WildFly container layer prior to JAX-RS filters.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 2.

**Feedback**  
Please verify:
- tests stay within Task 02 scope and do not smuggle in app-level JWT validation,
- the new token helper is only a test utility and not a fake authentication mechanism,
- `ThemisClinicalAuthorizationFilter` still relies on container-established `SecurityContext` and now has good coverage for principal extraction, 401 unauthenticated, 403 authenticated-but-denied, and anti-spoofing invariants,
- test/documentation language is honest about the container-level verification limitation,
- check for any missed edge cases or misleading assertions around bearer tokens vs container auth.



Checking recent IDE activity

Implementation Approved

**Verification**
- **Production code decoupling**: `ThemisClinicalAuthorizationFilter.java` was NOT altered to add any JWT parsing. It still consumes identity exclusively via `requestContext.getSecurityContext().getUserPrincipal()` (line 127, `extractPrincipal` at 256-264) and maps the trusted subject to `ThemisPrincipal(name, PrincipalType.HUMAN, "harmonia-clinical", Map.of())`. Authorities come only from `SecurityContext.isUserInRole(...)`. The boundary is documented honestly in the class Javadoc (lines 51-60).
- **Token helper is test-only**: `OidcTestTokenHelper` lives under `src/test`, uses only standard `java.security` (RSA-2048/SHA256withRSA) + Jackson, generates deterministic valid/expired/not-before/wrong-issuer/wrong-audience/untrusted-key/corrupt-signature tokens. It contains no in-application validator; `verifySignature`/`parseClaimsUnsafe` are used only by its own unit test. A prominent Javadoc note states verification is performed by the WildFly container layer, not the app.
- **Filter test coverage**: verified principal extraction into `PrincipalType.HUMAN` with `sub` (N1), rejection of null/blank/whitespace/anonymous/system:anonymous (N2), 401 unauthenticated with SECURITY `OperationOutcome` (A, B1, B2, O, I1), 403 authenticated-but-denied with FORBIDDEN `OperationOutcome` under both mock and `DefaultThemisAuthorizer` (C1, C2, P, I2), allowed action mapping (D-H, M), non-clinical/OPTIONS pass-through (J1/J2/K), and comprehensive anti-spoofing (B1, L, Q) confirming `X-Harmonia-*`, `X-Principal-Id`, `X-Requester`, `X-User-Roles`, `X-Security-Scopes`, and arbitrary/valid bearer text never establish or elevate identity.
- **Honest bearer-vs-container distinction**: Test O ("Valid signed JWT in Authorization header without container SecurityContext fails closed with 401") correctly proves the filter ignores raw bearer text; Test P separately establishes the container principal — no misleading claim that mocked tests exercise real OIDC container validation.
- **Test execution**: `mvn test -pl iris/iris-befe` → 78 tests, 0 failures (~5s). `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"` → 27 tests, 0 failures. Both BUILD SUCCESS.

**Issues**  
None.

**Notes**
- The executor also created `.junie/plans/task-02-trusted-clinical-identity-establishment.md` (a copy of the plan document); it is outside the declared scope but harmless and not a code change.
- Container-level 401 for cryptographically invalid/expired/wrong-issuer tokens is not (and cannot be) exercised by these in-process mocked-`SecurityContext` tests — this limitation is acknowledged transparently in both the token helper and test Javadocs, consistent with the plan's stated assumption about `resteasy-undertow` lacking the Elytron OIDC subsystem.

**Discoveries for future steps**
- `mvn test -pl iris/iris-befe` runs all 78 tests in ~5s; architecture suite via `paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"` runs 27 tests in ~10-15s. Running the two modules separately works; a 300s timeout is comfortable for iris-befe and 600s for the architecture module (first `-am` build compiles dependencies).



**Requirements**

Establish a trusted, identity-provider-neutral authentication boundary for Iris BEFE clinical APIs (`/api/fhir/*`) at the WildFly container layer, ensuring authenticated callers are exposed via Jakarta/JAX-RS `SecurityContext.getUserPrincipal()` to `ThemisClinicalAuthorizationFilter` as `PrincipalType.HUMAN` using the validated `sub` claim.

**Scope**
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

**Done When**
- Iris BEFE clinical API endpoints (`/api/fhir/*`) are secured at the WildFly container layer using `elytron-oidc-client` bearer-only authentication.
- Unauthenticated requests or requests with invalid/expired/untrusted tokens are rejected at the container boundary with HTTP 401 Unauthorized (RFC 6750 `WWW-Authenticate: Bearer`).
- Valid cryptographically signed tokens establish a container `SecurityContext` principal with the validated `sub` claim, which `ThemisClinicalAuthorizationFilter` consumes as `PrincipalType.HUMAN`.
- Valid authenticated clinical requests reach Themis and return HTTP 403 Forbidden with a FHIR `OperationOutcome` due to absent clinical authorization policy (Task 03).
- Caller-controlled headers (`X-Harmonia-User`, `X-Harmonia-Role`, etc.) and arbitrary bearer text cannot establish or elevate identity.
- Full repository architecture tests (`*ArchitectureTest`) and BEFE test suites pass cleanly.

**Technical Design**

**Decisions**
- **Chose authoritative WAR-on-WildFly packaging with pinned base image / not bootable JAR or floating `latest` image**: The active deployment topology across Docker Compose and Kubernetes runs `iris-befe.war` on WildFly. Pinning `Dockerfile` to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` resolves runtime ambiguity, matches the project's WildFly 31 target, and leverages WildFly 31's native `elytron-oidc-client` subsystem without divergent Galleon bootable-JAR layering.
- **Chose WildFly container-managed OIDC bearer-only deployment configuration / not application-level JWT parsing or vendor SDKs**: Descriptors `WEB-INF/web.xml` and `WEB-INF/oidc.json` configure WildFly's native OIDC subsystem. Application code and `ThemisClinicalAuthorizationFilter` remain strictly decoupled from cryptographic validation and identity provider specifics (Keycloak, Entra, Auth0).
- **Chose container 401 for unauthenticated requests and filter 403 OperationOutcome for authenticated requests / not filter-level token parsing**: Servlet container authentication runs before JAX-RS `@PreMatching` filters. Missing or invalid bearer tokens are rejected by the container with HTTP 401. Requests passing container authentication reach `ThemisClinicalAuthorizationFilter`, where the trusted principal is evaluated by Themis and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (until Task 03).
- **Chose `sub` claim as default stable principal identifier / not email or display name**: OIDC RFC 7519 defines `sub` as the immutable, unique subject identifier. Using `sub` avoids identity instability, protects privacy, and satisfies Task 02 constraints.
- **Chose honest reporting of test runtime limitations / not synthetic in-filter JWT validators or disguised mocked tests**: The repository's test dependencies (`resteasy-undertow`) lack WildFly's Elytron OIDC subsystem. Unit and filter tests verify JAX-RS principal consumption and anti-spoofing; test JWT signing utilities provide deterministic tokens for container smoke testing; container integration limitations are documented transparently per parent task instructions.

**Approach & Touches**
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

**Nuances, Risks & Corners**
- **Container vs Filter 401 Boundary**: Container bearer-only rejection returns HTTP 401 (with `WWW-Authenticate: Bearer`) before JAX-RS executes. The filter's `abortWithOutcome` handles JAX-RS level defense-in-depth and 403 Forbidden outcomes. Test assertions must reflect this lifecycle distinction.
- **Fail-Closed on Missing Configuration**: When `OIDC_ISSUER_URL` or provider trust is missing/unreachable at runtime, WildFly's OIDC subsystem fails closed, rejecting incoming bearer tokens.
- **Operations & Non-Clinical Isolation**: Port 8090 operations endpoints and non-clinical paths (`/operations/*`, `/iris/*`) remain unaffected by clinical security constraints.

**Testing**

- **Missing Bearer Token**: Unauthenticated request to `/api/fhir/Person` is rejected by the container with HTTP 401 Unauthorized (`WWW-Authenticate: Bearer`).
- **Malformed / Arbitrary Bearer Text**: Request with `Authorization: Bearer invalid-text` fails container authentication with HTTP 401.
- **Invalid Signature / Untrusted Key**: Bearer token signed with untrusted key is rejected by OIDC validation with HTTP 401.
- **Expired Token**: Bearer token with expired timestamp (`exp` in the past) fails container authentication with HTTP 401.
- **Wrong Issuer / Audience**: Token with mismatched `iss` or `aud` claims fails container authentication with HTTP 401.
- **Valid Authenticated Token -> 403 Forbidden**: Valid signed token establishes container principal (`sub`), passes container auth, reaches `ThemisClinicalAuthorizationFilter`, and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (proving authentication succeeds while authorization remains default-deny).
- **Anti-Spoofing Immunity**: Requests with spoofed `X-Harmonia-User`, `X-Harmonia-Role`, or `X-Principal-Id` headers cannot establish identity or elevate permissions.
- **Architecture Regressions**: All ArchUnit tests in `paradeigma-test` and existing BEFE test suites pass without regressions.

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Configure WildFly Container OIDC Authentication & Authoritative Packaging**  
Goal: Establish container-managed OIDC bearer authentication descriptors and pin the authoritative WildFly WAR runtime packaging.  
Scope: `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`, `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`, `iris/iris-befe/Dockerfile`, `docker-compose.yml`, `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `iris/iris-befe/Dockerfile` pins the base image to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
- [ ] `web.xml` declares `<login-config><auth-method>OIDC</auth-method></login-config>` and `<security-constraint>` with `<auth-constraint>` for `/api/fhir/*` and `/fhir/*`.
- [ ] `WEB-INF/oidc.json` is created with externalized environment properties for issuer, client ID, audience verification, `bearer-only: true`, and `principal-attribute: sub`.
- [ ] `docker-compose.yml` and `deployment/kubernetes/base/iris/iris-befe.yaml` configure externalized OIDC environment variables without hardcoded secrets.
- [ ] WAR package builds cleanly without compilation or descriptor errors.
  Verification: `mvn clean package -pl iris/iris-befe -DskipTests` → green

**✓ Step 2: Implement Test Token Harness, Security Filter Verification & Anti-Spoof Test Suite**  
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



**Summary**
- Implemented Task 02 for Iris BEFE using WildFly **31.0.1.Final** container-managed OIDC bearer authentication (`elytron-oidc-client` via WAR descriptors), so trusted identity is established at the WildFly boundary and exposed to `SecurityContext.getUserPrincipal()` for Task 01.
- Preserved Task 01 semantics: unauthenticated/invalid bearer requests are handled at the container boundary, while valid authenticated clinical users still reach `ThemisClinicalAuthorizationFilter` and currently receive **403 Forbidden** under default-deny authorization.
  **Changes**
- Updated `iris/iris-befe/Dockerfile`, `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`, and added `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json` to pin the runtime to WildFly 31.0.1.Final-jdk21, secure `/api/fhir/*` and `/fhir/*`, run bearer-only OIDC, validate audience, and map principal identity from validated `sub`.
- Updated `docker-compose.yml` and `deployment/kubernetes/base/iris/iris-befe.yaml` to externalize the identity-provider-neutral OIDC contract (`OIDC_ISSUER_URL`, `OIDC_CLIENT_ID`) without secrets or insecure bypasses.
- Added focused test support and coverage in `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/security/OidcTestTokenHelper.java`, `OidcTestTokenHelperTest.java`, and expanded `ThemisClinicalAuthorizationFilterTest.java` to verify HUMAN principal mapping, anti-spoofing/header immunity, arbitrary bearer-text rejection, and authenticated-user→Task 01→403 behavior.
  **Verification**
- Passed `mvn clean package -pl iris/iris-befe -DskipTests`, `mvn test -pl iris/iris-befe`, and `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.
- Evidence achieved: valid trusted principal is consumed as `sub`/`PrincipalType.HUMAN` in filter tests; caller-controlled identity headers and arbitrary bearer text do not authenticate or elevate identity; authenticated clinical callers remain denied with 403 until Task 03 policy exists.
  **Notes**
- Limitation: the current in-repo test harness cannot boot the real WildFly Elytron OIDC subsystem, so full signed-token → WildFly container auth → `SecurityContext` integration was documented honestly rather than faked with app-level JWT validation; Operations/Pylai insecure auth patterns and ingress TLS discrepancy were left unchanged as out-of-scope backlog items.



Configured authentication: Managed by agent

**Requirements**

Establish a trusted, identity-provider-neutral authentication boundary for Iris BEFE clinical APIs (`/api/fhir/*`) at the WildFly container layer, ensuring authenticated callers are exposed via Jakarta/JAX-RS `SecurityContext.getUserPrincipal()` to `ThemisClinicalAuthorizationFilter` as `PrincipalType.HUMAN` using the validated `sub` claim.

**Scope**
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

**Done When**
- Iris BEFE clinical API endpoints (`/api/fhir/*`) are secured at the WildFly container layer using `elytron-oidc-client` bearer-only authentication.
- Unauthenticated requests or requests with invalid/expired/untrusted tokens are rejected at the container boundary with HTTP 401 Unauthorized (RFC 6750 `WWW-Authenticate: Bearer`).
- Valid cryptographically signed tokens establish a container `SecurityContext` principal with the validated `sub` claim, which `ThemisClinicalAuthorizationFilter` consumes as `PrincipalType.HUMAN`.
- Valid authenticated clinical requests reach Themis and return HTTP 403 Forbidden with a FHIR `OperationOutcome` due to absent clinical authorization policy (Task 03).
- Caller-controlled headers (`X-Harmonia-User`, `X-Harmonia-Role`, etc.) and arbitrary bearer text cannot establish or elevate identity.
- Full repository architecture tests (`*ArchitectureTest`) and BEFE test suites pass cleanly.

**Technical Design**

**Decisions**
- **Chose authoritative WAR-on-WildFly packaging with pinned base image / not bootable JAR or floating `latest` image**: The active deployment topology across Docker Compose and Kubernetes runs `iris-befe.war` on WildFly. Pinning `Dockerfile` to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21` resolves runtime ambiguity, matches the project's WildFly 31 target, and leverages WildFly 31's native `elytron-oidc-client` subsystem without divergent Galleon bootable-JAR layering.
- **Chose WildFly container-managed OIDC bearer-only deployment configuration / not application-level JWT parsing or vendor SDKs**: Descriptors `WEB-INF/web.xml` and `WEB-INF/oidc.json` configure WildFly's native OIDC subsystem. Application code and `ThemisClinicalAuthorizationFilter` remain strictly decoupled from cryptographic validation and identity provider specifics (Keycloak, Entra, Auth0).
- **Chose container 401 for unauthenticated requests and filter 403 OperationOutcome for authenticated requests / not filter-level token parsing**: Servlet container authentication runs before JAX-RS `@PreMatching` filters. Missing or invalid bearer tokens are rejected by the container with HTTP 401. Requests passing container authentication reach `ThemisClinicalAuthorizationFilter`, where the trusted principal is evaluated by Themis and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (until Task 03).
- **Chose `sub` claim as default stable principal identifier / not email or display name**: OIDC RFC 7519 defines `sub` as the immutable, unique subject identifier. Using `sub` avoids identity instability, protects privacy, and satisfies Task 02 constraints.
- **Chose honest reporting of test runtime limitations / not synthetic in-filter JWT validators or disguised mocked tests**: The repository's test dependencies (`resteasy-undertow`) lack WildFly's Elytron OIDC subsystem. Unit and filter tests verify JAX-RS principal consumption and anti-spoofing; test JWT signing utilities provide deterministic tokens for container smoke testing; container integration limitations are documented transparently per parent task instructions.

**Approach & Touches**
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

**Nuances, Risks & Corners**
- **Container vs Filter 401 Boundary**: Container bearer-only rejection returns HTTP 401 (with `WWW-Authenticate: Bearer`) before JAX-RS executes. The filter's `abortWithOutcome` handles JAX-RS level defense-in-depth and 403 Forbidden outcomes. Test assertions must reflect this lifecycle distinction.
- **Fail-Closed on Missing Configuration**: When `OIDC_ISSUER_URL` or provider trust is missing/unreachable at runtime, WildFly's OIDC subsystem fails closed, rejecting incoming bearer tokens.
- **Operations & Non-Clinical Isolation**: Port 8090 operations endpoints and non-clinical paths (`/operations/*`, `/iris/*`) remain unaffected by clinical security constraints.

**Testing**

- **Missing Bearer Token**: Unauthenticated request to `/api/fhir/Person` is rejected by the container with HTTP 401 Unauthorized (`WWW-Authenticate: Bearer`).
- **Malformed / Arbitrary Bearer Text**: Request with `Authorization: Bearer invalid-text` fails container authentication with HTTP 401.
- **Invalid Signature / Untrusted Key**: Bearer token signed with untrusted key is rejected by OIDC validation with HTTP 401.
- **Expired Token**: Bearer token with expired timestamp (`exp` in the past) fails container authentication with HTTP 401.
- **Wrong Issuer / Audience**: Token with mismatched `iss` or `aud` claims fails container authentication with HTTP 401.
- **Valid Authenticated Token -> 403 Forbidden**: Valid signed token establishes container principal (`sub`), passes container auth, reaches `ThemisClinicalAuthorizationFilter`, and returns HTTP 403 Forbidden with a FHIR `OperationOutcome` (proving authentication succeeds while authorization remains default-deny).
- **Anti-Spoofing Immunity**: Requests with spoofed `X-Harmonia-User`, `X-Harmonia-Role`, or `X-Principal-Id` headers cannot establish identity or elevate permissions.
- **Architecture Regressions**: All ArchUnit tests in `paradeigma-test` and existing BEFE test suites pass without regressions.

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Configure WildFly Container OIDC Authentication & Authoritative Packaging**  
Goal: Establish container-managed OIDC bearer authentication descriptors and pin the authoritative WildFly WAR runtime packaging.  
Scope: `iris/iris-befe/src/main/webapp/WEB-INF/web.xml`, `iris/iris-befe/src/main/webapp/WEB-INF/oidc.json`, `iris/iris-befe/Dockerfile`, `docker-compose.yml`, `deployment/kubernetes/base/iris/iris-befe.yaml`.  
Acceptance Criteria:
- [ ] `iris/iris-befe/Dockerfile` pins the base image to `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
- [ ] `web.xml` declares `<login-config><auth-method>OIDC</auth-method></login-config>` and `<security-constraint>` with `<auth-constraint>` for `/api/fhir/*` and `/fhir/*`.
- [ ] `WEB-INF/oidc.json` is created with externalized environment properties for issuer, client ID, audience verification, `bearer-only: true`, and `principal-attribute: sub`.
- [ ] `docker-compose.yml` and `deployment/kubernetes/base/iris/iris-befe.yaml` configure externalized OIDC environment variables without hardcoded secrets.
- [ ] WAR package builds cleanly without compilation or descriptor errors.
  Verification: `mvn clean package -pl iris/iris-befe -DskipTests` → green

**✓ Step 2: Implement Test Token Harness, Security Filter Verification & Anti-Spoof Test Suite**  
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

