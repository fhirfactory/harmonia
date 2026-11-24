---
sessionId: session-261001-154749-oblz
---

# Requirements

### Overview & Goals

The architectural decision for **Milestone M2.3 (Deployment-Level Transport Authentication)** is confirmed: **Mneme → Mnemosyne transport authentication SHALL use mutual TLS (mTLS)**.

The intended responsibility and trust model strictly separates transport authentication from authorization:

```
TLS/container infrastructure
    authenticates the calling workload
            |
            v
trusted certificate identity (URI SAN: urn:harmonia:service:mneme)
            |
            v
bounded service-identity mapping (CertificateServiceIdentityMapper)
            |
            v
service:mneme
            |
            v
HttpServletRequest.getUserPrincipal()
            |
            v
AuthoritativeSecurityInterceptor
            |
            v
HarmoniaServiceIdentities.PRINCIPAL_MNEME
            |
            v
Themis authorization
```

Transport authentication establishes machine/service identity only. Themis remains solely and exclusively responsible for policy evaluation and authorization.

### Scope

- **In Scope (M2.3)**:
  - Exact Spring Boot 3 / embedded Tomcat X.509 client certificate authentication path populating `HttpServletRequest.getUserPrincipal()`.
  - Deterministic mapping adapter (`CertificateServiceIdentityMapper`) mapping authenticated X.509 URI SAN (`urn:harmonia:service:mneme`) to canonical `service:mneme` identity.
  - Development PKI infrastructure (Dev CA, server keystore/truststore, client keystore/truststore with SAN URI extensions) for Docker Compose and test environments.
  - Client-side SSLContext configuration in `MnemeAuthoritativeHttpClient` and `MnemeAuthoritativeClientConfig`.
  - Precise transport failure classification in `HttpTransportFailureClassifier` distinguishing pre-transmission TLS handshake failures (`NotCommitted`) from indeterminate transport/stream failures (`OutcomeUnknown`).
  - Server-side Spring Boot TLS configuration (`server.ssl.*`) and high-precedence authentication filter in `mnemosyne-clinical`.
  - Verification of fail-closed security enforcement across all failure permutations (no cert, untrusted cert, wrong service identity, missing/malformed SAN URI, unauthorized action).
  - Preservation of `AuthoritativeSecurityInterceptor`, `HarmoniaServiceIdentities`, and Themis policies.

- **Out of Scope (Explicit Exclusions)**:
  - **M2.4**: Authenticated distributed semantic operations (CREATE/READ/UPDATE/conflicts over HTTP) across Docker network (deferred to M2.4).
  - Production enterprise PKI, automated certificate issuance infrastructure (ACME/Vault), or online rotation machinery.
  - Modifying `clinical.delete` or altering Themis service authority scopes.
  - Modifying public `/fhir/*` (`JpaRestfulServer`).
  - M3 Governed Access integration (`DefaultGovernedReader`, `DefaultGovernedWriter`).

### Architectural Distinctions & Invariants

1. **Certificate Identity != Harmonia Service Identity != Themis Authorization**:
   - **Certificate Identity**: The cryptographic credential presented in the authenticated X.509 certificate. The **normative** service identity source is the **URI Subject Alternative Name (SAN)**: `urn:harmonia:service:mneme`. The certificate Subject CN (`CN=service:mneme`) is descriptive/debugging only and MUST NOT independently establish the service identity.
   - **Harmonia Service Identity**: The canonical, internal service principal identifier defined in `themis-core` (`HarmoniaServiceIdentities.ID_MNEME` = `"service:mneme"`).
   - **Themis Authorization**: The deterministic evaluation by `DeterministicPolicyEvaluator` and `ClinicalAuthorizationPolicy` determining whether the principal possesses the authority (`clinical.read`, `clinical.create`, `clinical.update`) for the requested FHIR action.
2. **Fail-Closed Transport Authentication**:
   - Requests without a valid client certificate fail at TLS handshake (or HTTP 401 if TLS validation is bypassed).
   - Requests with an untrusted certificate fail at TLS handshake.
   - Requests with a valid certificate lacking a trusted URI SAN (e.g. unknown service URI, missing URI SAN, conflicting SANs) fail closed at `CertificateServiceIdentityMapper` / `AuthoritativeSecurityInterceptor` with HTTP 401.
   - Requests with authenticated `service:mneme` attempting unauthorized operations fail closed at `ThemisAuthorizer` with HTTP 403.
3. **No Caller-Controlled Header Trust**:
   - Principal identity must be established exclusively from verified TLS transport certificates via URI SAN, never from caller-supplied HTTP headers (e.g. `X-Principal`).
4. **Failure Classification Discipline (AX-01, AX-05)**:
   - Known failures occurring demonstrably before authoritative HTTP transmission (e.g. TLS handshake rejection, cert path building failure, DNS failure) MUST return `NotCommitted`.
   - Failures occurring after handshake or during/after request byte transmission where processing state cannot be established MUST return `OutcomeUnknown`.

# Technical Design

### Verified X.509 Principal Path (Exact Runtime Mechanism)

In Spring Boot 3 with embedded Tomcat, `server.ssl.client-auth=need` enforces TLS client certificate validation against the truststore at the JSSE layer. However, embedded Tomcat does not automatically populate `HttpServletRequest.getUserPrincipal()` without a container authenticator valve or a Servlet filter.

The verified runtime path operates as follows:

```mermaid
graph TD
    subgraph Client [hestia/mneme-persistence]
        MAC[MnemeAuthoritativeHttpClient<br/>JDK HttpClient with SSLContext<br/>Client Keystore: SAN URI=urn:harmonia:service:mneme]
    end

    subgraph TLS Handshake [Tomcat JSSE Boundary]
        TS[Server Truststore<br/>Validates Client Cert against Dev CA]
        ATTR[Sets request attribute<br/>jakarta.servlet.request.X509Certificate]
    end

    subgraph Authentication Filter [mnemosyne-clinical]
        FILTER[X509CertificateAuthenticationFilter<br/>Ordered.HIGHEST_PRECEDENCE]
        MAPPER[CertificateServiceIdentityMapper<br/>URI SAN urn:harmonia:service:mneme -> 'service:mneme']
        WRAP[HttpServletRequestWrapper<br/>getUserPrincipal returns Principal('service:mneme')]
    end

    subgraph Interceptor & Authorization [mnemosyne-clinical & themis-core]
        ASI[AuthoritativeSecurityInterceptor<br/>request.getUserPrincipal]
        HSI[HarmoniaServiceIdentities<br/>Resolves PRINCIPAL_MNEME & Authorities]
        THEMIS[ThemisAuthorizer / ClinicalAuthorizationPolicy<br/>Evaluates Action]
        AFRC[AuthoritativeFhirResourceController<br/>/api/authoritative/fhir/*]
    end

    MAC -->|TLS 1.3 / mTLS Handshake| TS
    TS -->|Valid Cert Chain| ATTR
    ATTR --> FILTER
    FILTER --> MAPPER
    MAPPER --> WRAP
    WRAP --> ASI
    ASI --> HSI
    HSI --> THEMIS
    THEMIS -->|ALLOW| AFRC
```

#### Detailed Execution Steps:
1. **mTLS Handshake**: Mneme connects over HTTPS. Tomcat JSSE validates Mneme's client certificate against `server.ssl.trust-store`.
2. **Attribute Injection**: Tomcat places the validated peer certificate array in `jakarta.servlet.request.X509Certificate`.
3. **Filter Interception**: `X509CertificateAuthenticationFilter` (a high-precedence Spring Web filter registered before Spring MVC) intercepts the request:
   - Reads `X509Certificate[] certs = (X509Certificate[]) request.getAttribute("jakarta.servlet.request.X509Certificate")`.
   - If `certs == null || certs.length == 0`, passes request through unwrapped (resulting in `request.getUserPrincipal() == null`).
   - If present, extracts peer certificate `certs[0]`.
4. **Deterministic SAN URI Identity Mapping**:
   - Calls `CertificateServiceIdentityMapper.mapToServiceIdentity(certs[0])`.
   - Extracts URI Subject Alternative Names (`cert.getSubjectAlternativeNames()`, type 6 = `uniformResourceIdentifier`).
   - Validates that exactly one normative Harmonia service URI SAN is present matching the explicit whitelist (`urn:harmonia:service:mneme`).
   - Rejects certificates with missing, malformed, unknown, or conflicting URI SANs (returns `Optional.empty()`).
   - Does NOT rely on Subject CN to establish service identity.
5. **Principal Request Wrapping**:
   - If mapped, wraps `HttpServletRequest` in an `AuthenticatedServiceRequestWrapper` whose `getUserPrincipal()` returns a `Principal` with name `"service:mneme"`.
6. **Interceptor Verification & Themis Authorization**:
   - `AuthoritativeSecurityInterceptor` calls `request.getUserPrincipal()`, retrieves `"service:mneme"`, resolves canonical `HarmoniaServiceIdentities.PRINCIPAL_MNEME`, and delegates to `ThemisAuthorizer`.

---

### Certificate → Harmonia Identity Mapping (Normative SAN URI Rule)

The mapping from X.509 certificate identity to Harmonia service identity is bounded, deterministic, and fail-closed:

```
authenticated X.509 certificate
      |
      | URI SAN (type 6)
      v
urn:harmonia:service:mneme
      |
      | explicit bounded mapping (whitelist only)
      v
service:mneme
```

#### Trust and Mapping Rules:
- **Normative Source**: URI Subject Alternative Name (SAN type 6) `urn:harmonia:service:mneme`.
- **Descriptive CN**: Subject CN `CN=service:mneme` is allowed for logging/diagnostics, but MUST NOT independently establish the service identity.
- **Fail-Closed on Unknown/Malformed/Conflicting SANs**: Generic pattern conversion (e.g. `urn:harmonia:service:*` -> `service:*`) is strictly forbidden. Only explicitly registered identities in the mapping table are trusted.
- If multiple conflicting Harmonia service URI SANs are present, the mapper MUST fail closed and return `Optional.empty()`.

| Certificate URI SAN (type 6) | Subject CN (Descriptive) | Mapped Harmonia Service Identity | Canonical Themis Principal | Assigned Authorities |
| :--- | :--- | :--- | :--- | :--- |
| `urn:harmonia:service:mneme` | `CN=service:mneme` | `"service:mneme"` | `HarmoniaServiceIdentities.PRINCIPAL_MNEME` | `clinical.read`, `clinical.create`, `clinical.update`, `clinical.delete`, `system.integration` |
| `urn:harmonia:service:unknown` | Any CN | `Optional.empty()` (No mapping) | `null` | None (Fails closed: HTTP 401) |
| Missing URI SAN | `CN=service:mneme` | `Optional.empty()` (No mapping) | `null` | None (Fails closed: HTTP 401) |
| Malformed / Multiple Conflicting URI SANs | Any CN | `Optional.empty()` (No mapping) | `null` | None (Fails closed: HTTP 401) |

---

### Precise Transport & TLS Failure Classification

In accordance with Harmonia Architectural Axioms (AX-01, AX-05) and the established semantic rule:
- **`NotCommitted`**: Known failure occurring demonstrably before authoritative HTTP transmission (zero possibility of server-side mutation).
- **`OutcomeUnknown`**: Failure where the request may have reached authoritative processing but the final outcome cannot be established.

#### Classification Analysis by Failure Scenario:

| Failure Scenario | Exact Runtime Mechanism / JSSE Exception | Point in Lifecycle | Failure Classification | Rationale |
| :--- | :--- | :--- | :--- | :--- |
| **Missing client certificate** (client connects without cert when `client-auth=need`) | `SSLHandshakeException: Received fatal alert: certificate_required` / `bad_certificate` | TLS Handshake (Pre-network/Pre-HTTP) | **`NotCommitted`** | Handshake aborted before connection established; zero HTTP request bytes transmitted. |
| **Rejected / untrusted client cert** (client cert signed by untrusted CA) | `SSLHandshakeException: Received fatal alert: unknown_ca` / `handshake_failure` | TLS Handshake (Pre-network/Pre-HTTP) | **`NotCommitted`** | Server closes TLS handshake; zero HTTP request bytes transmitted. |
| **Server certificate validation failure** (client truststore rejects server cert) | `SSLHandshakeException: PKIX path building failed: ... unable to find valid certification path` / `SSLPeerUnverifiedException` | TLS Handshake (Pre-network/Pre-HTTP) | **`NotCommitted`** | Client terminates TLS handshake before establishing session; zero HTTP request bytes transmitted. |
| **TLS connection / handshake timeout** | `HttpConnectTimeoutException` | TCP/TLS connection phase (Pre-HTTP) | **`NotCommitted`** | Connection setup did not complete; request was not transmitted. |
| **TLS connection reset / failure after successful handshake / during transmission** | `SSLException: Connection reset` / `SocketException` / `HttpTimeoutException` (on read) / `IOException` | Post-handshake / In-flight HTTP transmission | **`OutcomeUnknown`** | Request bytes may have reached or been partially processed by Mnemosyne; state cannot be determined safely. |

#### Implementation in `HttpTransportFailureClassifier`:
- Check for provably pre-transmission exceptions:
  - `UnknownHostException` / `UnresolvedAddressException` -> `NotCommitted` (DNS resolution failure).
  - `SSLHandshakeException` / `SSLPeerUnverifiedException` / `CertificateException` -> `NotCommitted` (TLS handshake rejected before transmission).
  - `HttpConnectTimeoutException` -> `NotCommitted` (Connection/handshake timeout before session establishment).
  - `IllegalArgumentException` / `NullPointerException` / `IllegalStateException` -> `NotCommitted` (Local client validation).
- All generic `SSLException` (not caused by handshake), `SocketTimeoutException`, `HttpTimeoutException` (response read timeout), `SocketException`, and general `IOException` remain classified conservatively as **`OutcomeUnknown`**.

---

### Development PKI Topology

The minimum reproducible Docker-development PKI arrangement consists of:

```
[ Harmonia Dev Root CA ] (ca.crt / ca.key)
       |
       +---> [ Mnemosyne Server Cert ] (CN=mnemosyne-clinical, SAN: DNS:mnemosyne-clinical, DNS:localhost, IP:127.0.0.1)
       |        - Keystore: mnemosyne-keystore.p12 (server cert + key)
       |        - Truststore: mnemosyne-truststore.p12 (contains ca.crt)
       |
       +---> [ Mneme Client Cert ] (CN=service:mneme, SAN: URI:urn:harmonia:service:mneme)
                - Keystore: mneme-keystore.p12 (client cert + key)
                - Truststore: mneme-truststore.p12 (contains ca.crt)
```

1. **Development Root CA**:
   - `ca.key` (2048-bit RSA or EC P-256) + `ca.crt` (Self-signed Root CA, `CN=Harmonia Development CA`).
2. **Mnemosyne Server Certificate**:
   - Subject: `CN=mnemosyne-clinical`.
   - Subject Alternative Names (SAN): `DNS:mnemosyne-clinical`, `DNS:hapi-fhir-jpa-server-1`, `DNS:localhost`, `IP:127.0.0.1`.
   - Keystore: `mnemosyne-keystore.p12` containing server private key and certificate chain.
   - Truststore: `mnemosyne-truststore.p12` containing `ca.crt`.
3. **Mneme Client Certificate**:
   - Subject: `CN=service:mneme` (descriptive).
   - Subject Alternative Names (SAN): `URI:urn:harmonia:service:mneme` (normative service identity).
   - Keystore: `mneme-keystore.p12` containing client private key and certificate chain.
   - Truststore: `mneme-truststore.p12` containing `ca.crt`.
4. **Reproducible Generation Script**:
   - `scripts/pki/generate-dev-certs.sh`: Shell script using OpenSSL / `keytool` generating keystores into `.pki/` or test resources.
5. **Docker Compose & Configuration**:
   - Keystores mounted read-only into `hapi-fhir-jpa-server-1` at `/etc/harmonia/tls/`.
   - Port `8443` configured for HTTPS with `server.ssl.client-auth=need`.

---

### Fail-Closed Cases & Expected Responses

| Failure Mode | Point of Enforcement | Technical Mechanism | Observed Outcome | Persistence Result |
| :--- | :--- | :--- | :--- | :--- |
| **1. No Certificate** (Plain HTTP or TLS without client cert) | Tomcat JSSE / Network Layer | TLS Handshake rejects connection (`certificate_required` / `bad_certificate`) | `SSLHandshakeException` | **`NotCommitted`** |
| **2. Untrusted Certificate** (Cert signed by unknown/untrusted CA) | Tomcat JSSE / Network Layer | TLS Handshake rejects connection (`unknown_ca` / `PKIX path building failed`) | `SSLHandshakeException` | **`NotCommitted`** |
| **3. Untrusted Server Cert** (Client rejects Mnemosyne cert) | Client JSSE Layer | TLS Handshake aborts locally (`PKIX path building failed`) | `SSLHandshakeException` | **`NotCommitted`** |
| **4. Valid Cert, Missing / Wrong URI SAN** (Valid CA cert, but no `urn:harmonia:service:mneme` SAN) | `CertificateServiceIdentityMapper` + `AuthoritativeSecurityInterceptor` | Mapper returns `Optional.empty()`; `getUserPrincipal()` is null | HTTP `401 Unauthorized` (`"Missing or unauthenticated principal"`) | **`NotCommitted`** |
| **5. Authenticated but Unauthorized** (`service:mneme` performing unauthorized action) | `ThemisAuthorizer` (`ClinicalAuthorizationPolicy`) | Policy denies request (e.g. DELETE action) | HTTP `403 Forbidden` (`"Access denied by Themis"`) | **`NotCommitted`** |
| **6. Post-Handshake Reset / Stream Failure** | Network Transport / Socket Layer | Mid-stream socket reset during HTTP exchange | `IOException` / `SocketException` | **`OutcomeUnknown`** |

---

### Expected File Changes

| File / Component | Module | Action | Description |
| :--- | :--- | :--- | :--- |
| `CertificateServiceIdentityMapper.java` | `hestia/mnemosyne-clinical` | **New** | Deterministic mapper extracting URI SAN (`urn:harmonia:service:mneme`) and mapping to `"service:mneme"`. Fails closed on missing/unknown/conflicting SANs. |
| `X509CertificateAuthenticationFilter.java` | `hestia/mnemosyne-clinical` | **New** | High-precedence Servlet Filter extracting `jakarta.servlet.request.X509Certificate`, resolving identity via `CertificateServiceIdentityMapper`, and wrapping request with authenticated `Principal`. |
| `AuthoritativeWebMvcConfig.java` | `hestia/mnemosyne-clinical` | **Modify** | Register filter with `FilterRegistrationBean` / high precedence. |
| `application-mtls.yml` / `application.yml` | `hestia/mnemosyne-clinical` | **Modify** | Add `server.ssl.*` configuration for keystore, truststore, and `client-auth=need`. |
| `HttpTransportFailureClassifier.java` | `hestia/mneme-persistence` | **Modify** | Classify pre-transmission `SSLHandshakeException`, `SSLPeerUnverifiedException`, `CertificateException`, and `HttpConnectTimeoutException` as `NotCommitted`. |
| `MnemeAuthoritativeClientConfig.java` | `hestia/mneme-persistence` | **Modify** | Add properties/env for keystore path/password, truststore path/password, and HTTPS URL default. |
| `MnemeAuthoritativeHttpClient.java` | `hestia/mneme-persistence` | **Modify** | Configure `HttpClient.newBuilder().sslContext(...)` with client keystore and truststore. |
| `SslContextFactory.java` | `hestia/mneme-persistence` | **New** | Helper utility to construct `SSLContext` from PKCS12/JKS keystores and truststores. |
| `scripts/pki/generate-dev-certs.sh` | Repository root | **New** | Deterministic script generating Dev CA, server certs, and client certs (with URI SAN `urn:harmonia:service:mneme`) for tests and Docker. |
| `docker-compose.yml` | Repository root | **Modify** | Configure TLS port `8443:8443`, volume mounts for `/etc/harmonia/tls/`, and environment variables. |
| `DistributedAuthoritativeDockerPathTest.java` | `hestia/mneme-persistence` | **Modify** | Add mTLS test scenarios verifying authenticated transport and fail-closed rejections. |
| `harmonia-convergence-runtime-integration-plan.md` | `docs/implementation` | **Modify** | Update M2.3 milestone status and documentation. |

---

### M2.3 Implementation Plan (Consolidated Sequence)

### ✓ Step 1: Development PKI and Certificate Generation
- Create `scripts/pki/generate-dev-certs.sh` to generate the Dev Root CA, Mnemosyne server certificate (with DNS SANs), and Mneme client certificate (with normative `URI:urn:harmonia:service:mneme` SAN and descriptive `CN=service:mneme`).
- Place generated test keystores/truststores in test resources and `.pki/` for reproducible execution.

### ✓ Step 2: Implement Normative SAN URI Identity Mapper and Authentication Filter
- Implement `CertificateServiceIdentityMapper` in `mnemosyne-clinical` to extract URI SANs (type 6) and deterministically map `urn:harmonia:service:mneme` to `"service:mneme"`, failing closed on missing, unknown, malformed, or multiple conflicting SANs.
- Implement `X509CertificateAuthenticationFilter` in `mnemosyne-clinical` wrapping `HttpServletRequest` with `Principal("service:mneme")`.
- Register the filter with high precedence in `AuthoritativeWebMvcConfig`.
- Add unit tests verifying URI SAN extraction, CN non-reliance, and fail-closed behavior on unauthorized certificates.

### ✓ Step 3: Server-side TLS Configuration & Client mTLS SSLContext Factory
- Configure Spring Boot TLS in `mnemosyne-clinical` (`server.ssl.key-store`, `server.ssl.trust-store`, `server.ssl.client-auth=need`).
- Implement `SslContextFactory` in `mneme-persistence` to construct JDK `SSLContext` from keystores and truststores.
- Update `MnemeAuthoritativeClientConfig` with TLS properties (`mnemosyne.authoritative.tls.keystore.path`, `truststore.path`, passwords).
- Update `MnemeAuthoritativeHttpClient` to initialize JDK `HttpClient` with configured `SSLContext`.

### ✓ Step 4: Precise Failure Classifier & Wire/Container Verification
- Update `HttpTransportFailureClassifier` in `mneme-persistence` to classify pre-transmission `SSLHandshakeException`, `SSLPeerUnverifiedException`, `CertificateException`, and `HttpConnectTimeoutException` as `NotCommitted`, while keeping post-handshake transport IO failures as `OutcomeUnknown`.
- Add unit/WireMock tests verifying failure classification across all handshake failure modes (no cert, untrusted cert, rejected server cert, connect timeout).

### ✓ Step 5: Distributed Cross-Container Deployment Boundary Proof & Conformance
- Update `docker-compose.yml` to mount TLS certificates into `hapi-fhir-jpa-server-1` and configure HTTPS port `8443`.
- Execute distributed cross-container mTLS verification from a container attached to `harmonia-network`:
  - Verify Docker DNS resolution of `mnemosyne-clinical` to `172.18.0.3` (direct container port 8443, not host-published port).
  - Prove authenticated `service:mneme` caller using `MnemeAuthoritativeHttpClient` (with URI SAN `urn:harmonia:service:mneme`) successfully negotiates TLS 1.3 mTLS, reaches `X509CertificateAuthenticationFilter`, maps to `Principal("service:mneme")`, passes `AuthoritativeSecurityInterceptor`, receives Themis `ALLOW` for READ, and reaches `AuthoritativeFhirResourceController` (HTTP 404).
  - Prove unauthenticated / untrusted / wrong-SAN callers fail closed (`NotCommitted` or HTTP 401).
- Run ArchUnit architecture tests ensuring zero layer violations.
- Update master integration plan `docs/implementation/harmonia-convergence-runtime-integration-plan.md`.

---

### M2.3 Exit Criterion

**Milestone M2.3 is complete when:**
1. Mnemosyne enforces mTLS on its authoritative port (`8443`) with `server.ssl.client-auth=need`.
2. `CertificateServiceIdentityMapper` deterministically extracts URI SAN `urn:harmonia:service:mneme` from authenticated X.509 client certificates and binds it to `service:mneme` on `HttpServletRequest.getUserPrincipal()`, failing closed if the SAN URI is missing, unknown, malformed, or conflicting.
3. `AuthoritativeSecurityInterceptor` ingests `service:mneme` from `request.getUserPrincipal()`, resolves `HarmoniaServiceIdentities.PRINCIPAL_MNEME`, and executes Themis policy authorization without caller-controlled headers or security bypasses.
4. `MnemeAuthoritativeHttpClient` and `HttpTransportFailureClassifier` precisely classify pre-transmission TLS handshake rejections (no cert, untrusted client cert, untrusted server cert) as `NotCommitted` and post-handshake IO failures as `OutcomeUnknown`.
5. All fail-closed paths (no cert, untrusted cert, invalid SAN, unauthorized action) are verified and all ArchUnit architecture tests pass with zero violations.
6. Distributed cross-container mTLS is proven from a client container attached to `harmonia-network` resolving `mnemosyne-clinical:8443` over Docker DNS without traversing the host-published port.