---
sessionId: session-260924-161546-euo2
---

# Requirements

### Goal / Outcome
Converge the Iris BEFE security boundary for FHIR `AuditEvent` so that `AuditEvent` endpoints (`/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}`) are governed under the `AUDIT` security domain instead of `CLINICAL`. Authorize `READ` and `SEARCH` for callers holding `audit.read` (or `system.admin` read privileges) via canonical `AuditReadPolicy`, while ensuring all external mutation actions (`CREATE`, `UPDATE`, `PATCH`, `DELETE`) fail closed through canonical Themis explicit-deny evaluation.

### Scope
- **In Scope:**
  - Resource security-domain classification in `ThemisClinicalAuthorizationFilter` (route `AuditEvent` to `AUDIT` domain/label while keeping other FHIR resources as `CLINICAL`).
  - Canonical explicit-deny policy `AuditImmutabilityDenyPolicy` in `themis-core` registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
  - Fail-closed external mutation denial for `AuditEvent` across all roles (including `system.admin`, clinical, operations, provider registry).
  - Authority and cross-domain isolation verification (`audit.read` permits READ/SEARCH; clinical/ops/unknown denied; unauthenticated returns 401).
  - Unit, boundary, and architectural tests in `themis-core`, `iris-befe`, and `paradeigma-test`.
- **Out of Scope:**
  - Step 04.4 `AuditEventResource` refactoring, method removal, or Kleio delegation.
  - Modifying `AuditService`, `DurableAuditService`, or Kleio persistence layer.
  - Step 04.5 Mnemosyne backend JPA/Infinispan immutability triggers.
  - Step 04.6 Iris UI modifications.
  - Introducing any new `audit.write` role or authority.

### Done When
- [ ] `/api/fhir/AuditEvent` and `/api/fhir/AuditEvent/{id}` requests construct `ThemisResource` and `ThemisSecurityContext` with domain `AUDIT` and security label `AUDIT`.
- [ ] `ThemisPolicy` explicit-deny policy `AuditImmutabilityDenyPolicy` denies any mutating action (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] Principal with `audit.read` is allowed `READ` and `SEARCH` on `AuditEvent`, but denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` with HTTP 403.
- [ ] Principal with `system.admin` is denied `CREATE`, `UPDATE`, `PATCH`, `DELETE` on `AuditEvent` via canonical explicit-deny precedence.
- [ ] Clinical-only, operations-only, and provider-registry authorities are denied all access (`READ`, `SEARCH`, `CREATE`, `UPDATE`, `DELETE`) to `AuditEvent`.
- [ ] Unauthenticated requests preserve HTTP 401 `OperationOutcome` boundary.
- [ ] Existing clinical FHIR resources (`Patient`, `Observation`, etc.) retain `CLINICAL` domain classification and behavior without regression.

---

# Technical Design

### Decisions
- **Chose canonical explicit-deny `AuditImmutabilityDenyPolicy` in `themis-core` / not ad-hoc BEFE filter checks or narrowing `SystemAdminPolicy` directly**: `DeterministicPolicyEvaluator` already implements explicit-deny evaluation (Step 2 before Step 3 ordered allow policies). An explicit-deny policy cleanly enforces non-repudiation invariants for `AUDIT` targets across all roles without mutating role semantics in `SystemAdminPolicy` or bypassing canonical Themis in Iris BEFE.
- **Chose selective domain resolution in `ThemisClinicalAuthorizationFilter` / not separate JAX-RS filter or URI rework**: The filter already parses `resourceType` from the URI subpath; mapping `AuditEvent` (case-insensitive) to `HarmoniaSecurityLabelEnum.AUDIT` and other FHIR resources to `HarmoniaSecurityLabelEnum.CLINICAL` establishes domain isolation with minimal diff surface.

### Approach & Touches
- **Themis Core (`themis/themis-core`)**:
  - Add `AuditImmutabilityDenyPolicy.java` in `net.fhirfactory.harmonia.themis.core.policy`:
    - Implements `ThemisPolicy` with `isExplicitDeny() == true`, `order = 10`, `domain = "AUDIT"`.
    - `appliesTo(request)`: returns true when `target.securityDomain == "AUDIT"` (or has label `AUDIT`) AND `action` is one of `CREATE`, `UPDATE`, `DELETE` (or not `READ`/`SEARCH`).
    - `evaluate(request)`: returns `ThemisAuthorizationDecision.deny(ThemisDecisionReason.ACTION_NOT_PERMITTED, POLICY_ID, corrId, "External mutation of AUDIT records is strictly prohibited")`.
  - Register `new AuditImmutabilityDenyPolicy()` in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
  - Reference analog: `AuditReadPolicy.java` for domain matching and `ClinicalAuthorizationPolicy.java` for explicit DELETE denial pattern.
- **Iris BEFE (`iris/iris-befe`)**:
  - Update `ThemisClinicalAuthorizationFilter.java`:
    - In `filter()` around line 158–174, determine target domain and security label based on `resourceType`: if `"AuditEvent".equalsIgnoreCase(resourceType)`, use `HarmoniaSecurityLabelEnum.AUDIT`, else `HarmoniaSecurityLabelEnum.CLINICAL`.
    - Apply the resolved label/domain consistently to both `ThemisResource` and `ThemisSecurityContext`.
    - Retain HTTP method-to-action mapping (`GET` id == null -> `SEARCH`, `GET` id != null -> `READ`, `POST` -> `CREATE`, `PUT`/`PATCH` -> `UPDATE`, `DELETE` -> `DELETE`).
- **Architecture & Security Guardrails (`paradeigma/paradeigma-test`)**:
  - Update `SecurityEnforcementArchitectureTest.java` to verify that `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator` and that BEFE relies on canonical Themis without local audit bypasses.

### Nuances, Risks & Corner Cases
- **PATCH Mapping Convention**: Themis action model has no `PATCH` action; filter maps `PATCH` -> `ThemisAction.UPDATE`. Immutability policy checks `UPDATE`, correctly denying both PUT and PATCH.
- **AuditReadPolicy Order**: `AuditReadPolicy` has order 100. Explicit-deny policies evaluate before Step 3 ordered policies, guaranteeing mutations are rejected before `SystemAdminPolicy` (order 10) or any allow policy is considered.
- **Resource Code Immutability**: `AuditEventResource.java` retains its existing endpoints during Step 04.3. No methods are deleted or refactored in this step; security is enforced strictly at the authorization filter boundary.

### Contracts
```java
// Policy Contract
public class AuditImmutabilityDenyPolicy implements ThemisPolicy {
    public static final String POLICY_ID = "audit-immutability-deny-policy";
    public static final String DOMAIN = "AUDIT";

    @Override
    public String getPolicyId() { return POLICY_ID; }
    @Override
    public int getOrder() { return 10; }
    @Override
    public boolean isExplicitDeny() { return true; }
    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request);
    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request);
}
```

---

# Testing

### Checklist
- **Audit Read/Search Authorization**: Authenticated principal with `audit.read` (or `AUD_RDR` role) successfully executes `GET /api/fhir/AuditEvent` (SEARCH) and `GET /api/fhir/AuditEvent/123` (READ) -> 200/green.
- **Audit Mutation Denial**: Authenticated principal with `audit.read` receives 403 Forbidden on `POST`, `PUT`, `PATCH`, `DELETE` to `/api/fhir/AuditEvent` -> `OperationOutcome` with `ACTION_NOT_PERMITTED`.
- **System Admin Audit Immutability**: Principal with `system.admin` / `SYS_ADM` role is allowed `READ`/`SEARCH` but denied `POST`, `PUT`, `PATCH`, `DELETE` on `AuditEvent` -> 403 Forbidden.
- **Cross-Domain Isolation**: Principal with Clinical (`CLINICAL_WRITE`, `CLINICAL_ADMIN`), Operations (`OPS_ADM`, `OPS_VIEWER`), or Provider Registry roles is denied all actions on `AuditEvent` -> 403 Forbidden.
- **Clinical Resource Isolation**: Standard clinical resources (`Patient`, `Observation`) retain `CLINICAL` domain classification and allow clinical read/write roles as before.
- **Unauthenticated / Missing Principal**: Anonymous or missing principal returns 401 Unauthorized (`PRINCIPAL_MISSING`).
- **Anti-Spoofing & Header Isolation**: Injected `X-Harmonia-*` or caller-controlled headers do not alter SecurityContext identity or domain classification.

---

# Delivery Steps

### ✓ Step 1: Canonical Themis Audit Immutability Explicit-Deny Policy
Goal: Enforce fail-closed immutability for the `AUDIT` security domain within `themis-core` using the existing explicit-deny engine.
Scope: `themis/themis-core`
Acceptance Criteria:
- [ ] `AuditImmutabilityDenyPolicy` is implemented in `net.fhirfactory.harmonia.themis.core.policy` with `isExplicitDeny() == true` matching mutating actions (`CREATE`, `UPDATE`, `DELETE`) on `AUDIT` target resources.
- [ ] `AuditImmutabilityDenyPolicy` is registered in `DeterministicPolicyEvaluator.withDefaultPolicies()`.
- [ ] Unit tests in `AuditImmutabilityDenyPolicyTest` verify explicit deny on mutating actions for any caller role (including `system.admin`).
- [ ] Evaluator tests in `DeterministicPolicyEvaluatorTest` confirm explicit-deny precedence over `SystemAdminPolicy` for AUDIT mutation actions while keeping AUDIT READ/SEARCH allowed.
Verification: `mvn test -pl themis/themis-core -Dtest="AuditImmutabilityDenyPolicyTest,DeterministicPolicyEvaluatorTest,ClinicalAuthorizationPolicyTest,OperationsAuthorizationPolicyTest"` → green

### ✓ Step 2: Iris BEFE AuditEvent Domain Convergence and Guardrails
Goal: Route BEFE `AuditEvent` requests into the `AUDIT` security domain, establish boundary tests, and enforce architecture guardrails.
Scope: `iris/iris-befe`, `paradeigma/paradeigma-test`
Acceptance Criteria:
- [ ] `ThemisClinicalAuthorizationFilter` dynamically classifies `AuditEvent` requests as `HarmoniaSecurityLabelEnum.AUDIT` and non-AuditEvent FHIR requests as `HarmoniaSecurityLabelEnum.CLINICAL` for both `ThemisResource` and `ThemisSecurityContext`.
- [ ] `ThemisClinicalAuthorizationFilterTest` validates all 15 required boundary scenarios (AuditEvent AUDIT classification, `audit.read` READ/SEARCH allow, mutation deny for all roles including `system.admin`, cross-domain isolation, anti-spoofing, and clinical regression prevention).
- [ ] `SecurityEnforcementArchitectureTest` asserts canonical Themis governance of `AuditEvent`, presence of `AuditImmutabilityDenyPolicy`, and absence of ad-hoc Iris audit authorization bypasses.
Verification: `mvn test -pl iris/iris-befe,paradeigma/paradeigma-test -Dtest="ThemisClinicalAuthorizationFilterTest,SecurityEnforcementArchitectureTest"` → green