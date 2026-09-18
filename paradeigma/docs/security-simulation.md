# Harmonia Paradeigma — Themis Security Simulation Guide

### 1. Overview
Harmonia security is governed by **Themis**, providing declarative RBAC, fine-grained ABAC, and dual-authority asynchronous execution checks. Paradeigma provides standardized actor fixtures and context factories representing canonical roles without reproducing security evaluation logic.

---

### 2. Pre-configured Security Actors

The `ParadeigmaSecurityActors` catalog defines standardized principals and contexts in `net.fhirfactory.harmonia.paradeigma.common.security`:

| Actor | Canonical Role | Granted Authorities | Permitted Actions |
|-------|----------------|---------------------|-------------------|
| **Provider Steward** | `PRV_ADM`, `PRV_SUB`, `PRV_APR`, `PRV_RDR` | `provider.change.submit`, `provider.change.approve`, `provider.read`, `provider.search`, `provider.admin` | Full read, change request submission, and change approval across Provider Registry |
| **Clinician** | `PRV_RDR` | `provider.read`, `provider.search` | Synchronous read and search operations only. Write attempts are rejected with 403 Forbidden |
| **System Administrator** | `SYS_ADM`, `PRV_ADM` | `system.admin`, `provider.admin` | Infrastructure administration and configuration |
| **Integration Service** | `SYS_INT`, `PRV_PROC` | `system.integration`, `provider.change.process` | Background queue ingestion and Ponos Ergon asynchronous execution |
| **Read-Only User** | `PRV_RDR` | `provider.read` | Resource inspection only |
| **Unauthorized User** | (None) | (None) | Unprivileged caller; all protected operations evaluate to `DENY` |
| **Unauthenticated** | (None) | (None) | Anonymous context (`ThemisSecurityContext.anonymous()`) |

---

### 3. Usage in Scenarios

#### Creating an Actor Context
```java
// Create Provider Steward context with auto-generated correlation ID
SecurityScenarioContext steward = SecurityScenarioContext.providerSteward();

// Create Clinician context with explicit correlation ID
SecurityScenarioContext clinician = SecurityScenarioContext.clinician("corr-12345");
```

#### Evaluating Policies with Themis
```java
DeterministicPolicyEvaluator evaluator = DeterministicPolicyEvaluator.withDefaultPolicies();

ThemisResource resource = ThemisResource.of(
    "Practitioner",
    "pract-101",
    "PROVIDER_REGISTRY",
    Set.of(ThemisSecurityLabel.of("PROVIDER_REGISTRY"))
);

ThemisAuthorizationRequest request = ThemisAuthorizationRequest.builder()
    .principal(steward.principal())
    .authorities(steward.grantedAuthorities())
    .action(ThemisAction.CREATE)
    .target(resource)
    .context(steward.securityContext())
    .build();

ThemisAuthorizationDecision decision = evaluator.authorize(request);
assertThat(decision.isAllowed()).isTrue();
```

---

### 4. Simulation Test Seams for Negative & Failure Testing

Paradeigma provides context factories to simulate specific authorization failure modes:

1. **Expired Context**:
   ```java
   ThemisSecurityContext expired = ParadeigmaSecurityActors.expiredContext();
   ```

2. **Revoked Authority**:
   ```java
   ThemisSecurityContext revoked = ParadeigmaSecurityActors.revokedAuthorityContext(
       ParadeigmaSecurityActors.PROVIDER_STEWARD_PRINCIPAL,
       HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT
   );
   ```

3. **Missing Role**:
   ```java
   ThemisSecurityContext missingRole = ParadeigmaSecurityActors.missingRoleContext(
       ParadeigmaSecurityActors.PROVIDER_STEWARD_PRINCIPAL,
       HarmoniaRoleEnum.PRV_SUB
   );
   ```
