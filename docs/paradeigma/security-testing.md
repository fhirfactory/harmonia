# Paradeigma Themis Security Testing & Actor Fixtures `[IMPLEMENTED]`

This document describes how Paradeigma exercises and validates Harmonia's **Themis** security subsystem, providing pre-configured actor fixtures, authorization failure seams, and defense-in-depth acceptance suites.

---

## 1. Security Architecture & Defense-in-Depth `[IMPLEMENTED]`

Harmonia enforces default-deny security across every layer:
- **Perimeter Ingress**: Pylai gates incoming MLLP and REST payloads, requiring authenticated service principals and valid security contexts (`ThemisAction.CREATE` / `ThemisAction.READ`).
- **Internal Messaging**: Petasos wraps tasks with immutable `PragmaSecurityContext` stamps.
- **Asynchronous Execution**: Energeia Ponos workers re-verify submitter privileges and worker execution grants before executing Erga activities (`ThemisAction.EXECUTE`).
- **Storage Mutations**: Mnemosyne commits require explicit write authorization.

Paradeigma provides realistic security context fixtures without reimplementing Themis evaluation logic.

---

## 2. Pre-Configured Security Actors (`ParadeigmaSecurityActors`) `[IMPLEMENTED]`

The `ParadeigmaSecurityActors` catalog in `paradeigma-common` defines standardized test principals:

| Actor Fixture | Harmonia Roles | Granted Authorities | Permitted Actions & Boundaries |
| :--- | :--- | :--- | :--- |
| **Provider Steward** | `PRV_ADM`, `PRV_SUB`, `PRV_APR`, `PRV_RDR` | `provider.change.submit`, `provider.change.approve`, `provider.read`, `provider.search`, `provider.admin` | Full change request submission, approval, update, and administrative governance across the Provider Registry. |
| **Clinician** | `PRV_RDR` | `provider.read`, `provider.search` | Read-only resource queries and search operations. Write attempts evaluate to `DENY` (HTTP 403 Forbidden). |
| **System Administrator**| `SYS_ADM`, `PRV_ADM` | `system.admin`, `provider.admin`, `*` | Platform infrastructure management, configuration updates, and emergency overrides. |
| **Integration Service** | `SYS_INT`, `PRV_PROC` | `system.integration`, `provider.change.process` | Machine-to-machine background queue processing, Camel route dispatch, and Ponos Ergon asynchronous execution. |
| **Read-Only User** | `PRV_RDR` | `provider.read` | Resource inspection only; search parameters restricted. |
| **Unauthorized User** | *(None)* | *(None)* | Authenticated user lacking any granted clinical or administrative authorities; all operations evaluate to `DENY`. |
| **Unauthenticated User**| *(None)* | *(None)* | Anonymous context (`ThemisSecurityContext.anonymous()`); rejected at perimeter gate with HTTP 401 Unauthorized. |

---

## 3. Creating Actor Contexts in Scenarios `[IMPLEMENTED]`

```java
// 1. Create a Provider Steward context with auto-generated correlation ID
SecurityScenarioContext steward = SecurityScenarioContext.providerSteward();

// 2. Create a Clinician context with explicit clinical correlation ID
SecurityScenarioContext clinician = SecurityScenarioContext.clinician("corr-journey-101");

// 3. Create an Integration Service machine principal
SecurityScenarioContext systemService = SecurityScenarioContext.integrationService();
```

---

## 4. Test Seams for Negative & Security Failure Testing `[IMPLEMENTED]`

Paradeigma provides factory methods to simulate precise authorization edge cases and security defects:

### 4.1 Expired Security Context
Simulates a security token that has expired in transit between ingress and background execution:
```java
ThemisSecurityContext expiredContext = ParadeigmaSecurityActors.expiredContext();
// Evaluating against Themis yields ThemisDecisionReason.CONTEXT_EXPIRED
```

### 4.2 Revoked Authority
Simulates a user who submitted a change request but had their submission privileges revoked before background Ergon execution:
```java
ThemisSecurityContext revokedContext = ParadeigmaSecurityActors.revokedAuthorityContext(
    ParadeigmaSecurityActors.PROVIDER_STEWARD_PRINCIPAL,
    HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT
);
```

### 4.3 Missing Role
Simulates a principal attempting an administrative action without holding the prerequisite enterprise role:
```java
ThemisSecurityContext missingRoleContext = ParadeigmaSecurityActors.missingRoleContext(
    ParadeigmaSecurityActors.PROVIDER_STEWARD_PRINCIPAL,
    HarmoniaRoleEnum.PRV_ADM
);
```

---

## 5. Defense-in-Depth Acceptance Testing `[IMPLEMENTED]`

The `ThemisDefenceInDepthAcceptanceTest` suite in `paradeigma-test` validates multi-gate enforcement:

```java
@Test
@DisplayName("Security Gate: Ingress rejects unprivileged write attempts")
void ingressRejectsUnprivilegedWrites() {
    SecurityScenarioContext clinician = SecurityScenarioContext.clinician();
    
    ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
            .principal(clinician.principal())
            .authorities(clinician.grantedAuthorities())
            .action(ThemisAction.CREATE)
            .target(ThemisResource.of("Practitioner", "pract-test-1"))
            .context(clinician.securityContext())
            .build();

    ThemisAuthorizationDecision decision = themisAuthorizer.authorize(request);
    assertThat(decision.isDenied()).isTrue();
    assertThat(decision.reason()).isEqualTo(ThemisDecisionReason.AUTHORITY_MISSING);
}
```

- **Dual-Authority Verification**: Asynchronous tasks must satisfy both the submitter's identity context and the executing worker's service authority.
- **Zero Simulation Bypasses**: Themis security rules run identically whether called from automated JUnit tests or live Kubernetes clusters.
