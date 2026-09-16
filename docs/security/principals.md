# Themis Principals & Identity Model

## 1. Concept

A **Principal** represents an identity requesting or executing work across Harmonia.

Themis does not assume every Principal is a human user. Supported principal types include:

```java
public enum PrincipalType {
    HUMAN,    // End-users, clinicians, administrative staff
    SYSTEM,   // External healthcare systems, EMRs, PAS, LIMS
    SERVICE,  // Internal Harmonia subsystem microservices
    PROCESS   // Asynchronous background task runners and Ergon workers
}
```

---

## 2. Model: `ThemisPrincipal`

```java
public record ThemisPrincipal(
    String principalId,
    PrincipalType principalType,
    String sourceDomain,
    Map<String, String> attributes
) implements Serializable {}
```

### Identity Formatting Conventions
Opaque, stable identifiers are strictly preferred over mutable display names:
- Human users: `user:dr-smith`, `user:nurse-jackie`
- External systems: `system:epic-emr-east`, `system:cerner-pas-main`
- Internal services: `service:pylai`, `service:ponos`, `service:provider-registry`
- Background processes: `process:provider-registry-change-processor`

---

## 3. Credential Hygiene
- Authentication context preserves `principalId`, `principalType`, `sourceDomain`, and contextual metadata (`X-Correlation-Id`).
- **Never persisted in Pragma, logs, or state**: passwords, bearer tokens, OAuth refresh tokens, or client secrets.
