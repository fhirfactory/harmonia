# Concept: Themis `[IMPLEMENTED]`

Themis is Harmonia's centralized default-deny authorization engine, security policy evaluation framework, and non-PHI audit logging subsystem, enforcing strict zero-trust governance across all platform operations.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Θέμις* (Themis)
- **Etymology**: Ancient Greek feminine noun meaning divine law, customary right, or that which is established by sacred order (derived from *τίθημι* - to put, to place, to establish).
- **Mythological Context**: Themis is the Titaness of divine order, law, fairness, and custom. She represents the inherent, unwritten cosmic order that governs both gods and mortals. Depicted holding scales and sitting beside Zeus on Mount Olympus, Themis presided over the assembly of gods, counselled rulers, and ensured that laws were upheld without bias or passion.
- **Architectural Rationale**: Healthcare integration engines operate in an environment where privacy, confidentiality, and data governance are matters of legal and moral imperative (HIPAA, GDPR). Traditional perimeter security ("implicit trust once inside the firewall") is catastrophically inadequate. Themis brings unwavering divine law to the architecture: every action is evaluated against immutable policies, access is denied by default, and every decision is impartially weighed on the scales of justice.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Themis enforces a 4-gate security model spanning every tier of the platform:

```
+---------------------------------------------------------------------------------------+
|                                    THEMIS SUBSYSTEM                                   |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                        THEMIS 4-GATE EVALUATION MODEL                          |  |
|   |                                                                                |  |
|   |   Gate 1: Perimeter Ingress Gate (Pylai)                                       |  |
|   |           -> Evaluates external caller identity & TLS client certificate      |  |
|   |                                                                                |  |
|   |   Gate 2: Message Dispatch Gate (Petasos)                                      |  |
|   |           -> Asserts publishing authority before enqueuing to broker queues    |  |
|   |                                                                                |  |
|   |   Gate 3: Activity Execution Gate (Energeia / Ponos)                           |  |
|   |           -> Verifies worker role & required authorities before Ergon run     |  |
|   |                                                                                |  |
|   |   Gate 4: Storage Persistence Gate (Hestia / Mnemosyne)                        |  |
|   |           -> Governs read/write queries to clinical & operational databases    |  |
|   +--------------------------------------------------------------------------------+  |
|                                         |                                             |
|                                         v                                             |
|   +--------------------------------------------------------------------------------+  |
|   |                     DETERMINISTIC EVALUATION PIPELINE                          |  |
|   |   Request -> Explicit Deny -> Policy Matching -> Authority Check -> Default Deny|
|   +--------------------------------------------------------------------------------+  |
|                                         |                                             |
|                                         v                                             |
|   +--------------------------------------------------------------------------------+  |
|   |                         themis-audit (Non-PHI Stream)                          |  |
|   |   - ThemisAuditService dispatches structured ThemisAuditEvent records          |  |
|   |   - Omits patient identifiers; captures principal, action, decision & reason  |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Themis Owns
- Security principal representations (`ThemisPrincipal`) and service accounts (`HarmoniaServiceIdentities`).
- Mnemonic role definitions (`HarmoniaRoleEnum`) and granular machine authorities (`HarmoniaAuthorityEnum`).
- Security classification tags (`HarmoniaSecurityLabelEnum`).
- Deterministic default-deny policy evaluation engine (`ThemisService`, `ThemisPolicy`).
- Built-in server-side domain policies (`ProviderRegistryReadPolicy`, `ProviderRegistrySubmitPolicy`, `SystemAdminPolicy`).
- Non-PHI security audit event generation and dispatch (`ThemisAuditService`, `ThemisAuditEvent`).

### What Themis Explicitly Does NOT Own (Anti-Responsibilities)
- Direct network socket termination or MLLP wire framing (owned by Pylai).
- Relational schema tables or JPA persistence (owned by Mnemosyne).
- Browser session state or UI rendering (owned by Iris).
- Background queue consumer thread management (owned by Energeia).

---

## 4. Key Security Invariants `[IMPLEMENTED]`

### Default-Deny Authorization `[IMPLEMENTED]`
- Any operation evaluated by `ThemisService` that lacks an explicit, matching policy granting permission evaluates strictly to `ThemisDecision.DENY`.
- Unauthenticated callers or requests with empty authority sets are immediately rejected.

### Zero-PHI Audit Invariant `[IMPLEMENTED]`
- Security audit records emitted via `ThemisAuditService` capture caller principal, action type, target resource type, evaluation outcome, and reason codes.
- Audit records **never** contain patient names, dates of birth, social security numbers, or clinical observations.

---

## 5. Key Submodules & Classes `[IMPLEMENTED]`

| Component | Module Name | Key Technology | Key Classes | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Security Contracts** | `themis-api` | Pure Java 21 | `ThemisPrincipal`, `ThemisRole`, `ThemisAuthority`, `ThemisPolicy` | `[IMPLEMENTED]` |
| **Policy Engine** | `themis-core` | Java 21, Spring/CDI | `ThemisService`, `ThemisDecision`, `ProviderRegistryPolicies` | `[IMPLEMENTED]` |
| **Security Audit** | `themis-audit` | Java 21, Slf4j | `ThemisAuditService`, `ThemisAuditEvent`, `AuditDecisionEnum` | `[IMPLEMENTED]` |

### Built-in Policy Portfolio `[IMPLEMENTED]`
- `ProviderRegistryReadPolicy`: Requires `PRV_READ` or `PRV_SEARCH` authorities.
- `ProviderRegistrySubmitPolicy`: Requires `PRV_SUBMIT` authority.
- `ProviderRegistryProcessPolicy`: Requires `PRV_PROCESS` or `PRV_TRANSFORM` authorities.
- `ProviderRegistryPersistPolicy`: Requires `PRV_WRITE` or `SYS_ADMIN` authorities.
- `SystemAdminPolicy`: Grants wildcard access to users with `SYS_ADMIN`.
