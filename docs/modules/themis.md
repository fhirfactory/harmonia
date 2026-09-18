# Themis Subproject Reference: Policy & Authorization Governance `[IMPLEMENTED]`

Themis is Harmonia's centralized default-deny authorization engine, policy evaluation framework, and non-PHI security auditing subsystem.

---

## 1. Subproject Architecture & Leaf Modules `[IMPLEMENTED]`

```
themis/
├── themis-api/    # Pure security contracts, models (Principal, Role, Authority, Policy)
├── themis-core/   # Deterministic default-deny policy evaluator & built-in domain policies
└── themis-audit/  # Structured non-PHI audit event dispatcher & correlation logger
```

### Subproject Maven Coordinates `[CONFIGURED]`
- **Parent GroupId**: `net.fhirfactory.harmonia`
- **ArtifactId**: `themis`
- **Version**: `1.0.0-SNAPSHOT`
- **Packaging**: `pom`

---

## 2. Leaf Module Deep-Dives `[IMPLEMENTED]`

### 2.1 `themis-api` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:themis-api:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.themis.api`
  - `net.fhirfactory.harmonia.themis.api.model`
  - `net.fhirfactory.harmonia.themis.api.policy`
- **Key Classes & Interfaces**:
  - `ThemisAuthorizer` / `ThemisService`: Core evaluation contract accepting a `ThemisAuthorizationRequest` and returning a `ThemisAuthorizationDecision`.
  - `ThemisPrincipal`: Caller identity representation carrying principal name, type (`USER`, `SERVICE`, `EXTERNAL_SYSTEM`), assigned roles, and granular authorities.
  - `ThemisAction`: Requested action verb (`READ`, `WRITE`, `SUBMIT`, `PROCESS`, `SEARCH`, `ADMIN`).
  - `ThemisResource`: Target resource identifier and classification label.
  - `ThemisDecision`: Decision outcome enum (`PERMIT`, `DENY`).
  - `ThemisDecisionReason`: Diagnostic reason code explaining why access was permitted or denied.
  - `ThemisPolicy`: Interface for evaluating access requests against deterministic rules.
- **Runtime Dependencies**: Pure Java 21 library. Depends on `calliope` and Slf4j. Strictly zero database, JPA, or messaging broker dependencies.

### 2.2 `themis-core` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:themis-core:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.themis.core.constants`
  - `net.fhirfactory.harmonia.themis.core.evaluator`
  - `net.fhirfactory.harmonia.themis.core.identities`
  - `net.fhirfactory.harmonia.themis.core.policy`
- **Key Classes & Policies**:
  - `DeterministicPolicyEvaluator`: Primary evaluation engine implementing the default-deny algorithm.
  - `HarmoniaServiceIdentities`: Managed service account catalogue for platform daemons (Pylai, Ponos, Mnemosyne, Agora).
  - Built-in Policy Portfolio:
    - `ProviderRegistryReadPolicy`: Authorizes read/search operations for callers with `PRV_READ` or `PRV_SEARCH`.
    - `ProviderRegistrySubmitPolicy`: Authorizes submissions for callers with `PRV_SUBMIT`.
    - `ProviderRegistryProcessPolicy`: Authorizes pipeline transformations for workers with `PRV_PROCESS` or `PRV_TRANSFORM`.
    - `ProviderRegistryPersistPolicy`: Authorizes relational persistence for services with `PRV_WRITE` or `SYS_ADMIN`.
    - `AuditReadPolicy`: Restricts access to security audit streams to auditors with `AUDIT_READ`.
    - `SystemAdminPolicy`: Blanket administrative bypass for principals holding `SYS_ADMIN`.
- **Runtime Dependencies**: `themis-api`, `calliope`, Slf4j.

### 2.3 `themis-audit` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:themis-audit:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.themis.audit.model`
  - `net.fhirfactory.harmonia.themis.audit.service`
- **Key Classes & Interfaces**:
  - `ThemisAuditEvent`: Immutable structured security audit record capturing timestamp, caller principal, requested action, target resource type, evaluation decision, and reason code.
  - `ThemisAuditService`: Audit dispatcher interface with asynchronous event emitting guarantees.
  - `InMemoryThemisAuditService`: High-performance circular buffer audit dispatcher used for testing and local deployments.
- **Zero-PHI Guarantee**: The audit record model deliberately lacks fields for patient clinical data, ensuring that audit events never leak PHI to system log sinks.
- **Runtime Dependencies**: `themis-api`, Slf4j.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Themis Owns
- The definition and evaluation of security policies, principals, roles, and authorities.
- The default-deny policy evaluation engine (`DeterministicPolicyEvaluator`).
- Built-in domain security policies for Provider Registry and platform services.
- Controlled platform service identities (`HarmoniaServiceIdentities`).
- Non-PHI security audit logging stream (`ThemisAuditService`).

### What Themis Explicitly Does NOT Own (Anti-Responsibilities)
- Network socket listening or TLS handshake termination (owned by Pylai / Ingress).
- User credential database schemas or password storage (delegated to enterprise identity provider / LDAP).
- Database table definitions or relational storage (owned by Mnemosyne).
- Web browser session state or UI rendering (owned by Iris).

---

## 4. Verification & Testing `[IMPLEMENTED]`

- **Execute All Themis Tests**:
  ```bash
  mvn test -pl themis/themis-api,themis/themis-core,themis/themis-audit -am
  ```
- **Run Security Enforcement Architecture Tests**:
  ```bash
  mvn test -pl paradeigma/paradeigma-test -am -Dtest="SecurityEnforcementArchitectureTest"
  ```
- **Key Tests**: `DeterministicPolicyEvaluatorTest`, `ProviderRegistryPoliciesTest`, `ThemisAuditServiceTest`.
