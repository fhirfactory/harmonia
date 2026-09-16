# Themis Security Decision Auditing

## 1. Overview

Every security-sensitive authorization decision rendered by Themis (both `ALLOW` and `DENY`) is recorded as a structured, non-PHI `ThemisAuditEvent` via `ThemisAuditService`.

---

## 2. Event Structure

```java
public record ThemisAuditEvent(
    String eventId,
    String decisionId,
    ThemisDecision decision,       // ALLOW, DENY
    ThemisDecisionReason reason,   // ALLOWED_BY_POLICY, AUTHORITY_MISSING, etc.
    String policyId,               // provider-registry-submit-policy
    String principalId,            // user:dr-smith, service:pylai
    PrincipalType principalType,   // HUMAN, SYSTEM, SERVICE, PROCESS
    String sourceDomain,           // hospital-west
    ThemisAction action,           // SUBMIT_UPDATE, READ, PROCESS, UPDATE
    String resourceType,           // Practitioner
    String resourceId,             // PR-100
    String securityDomain,         // PROVIDER_REGISTRY
    Set<ThemisSecurityLabel> securityLabels,
    String correlationId,          // corr-12345
    String causationId,
    Instant timestamp,
    String classification          // AUDIT
) {}
```

---

## 3. Privacy & Sanitization Rules
- **No PHI or Payload Bodies**: Audit records strictly record metadata (resource type, identifier, action, reason) without serializing clinical payload bodies or patient attributes.
- **No Authentication Secrets**: Passwords, bearer tokens, and session cookies are never written to audit streams.
- **Classification**: Audit event streams and repository entries carry the authoritative data security label `AUDIT` and require `audit.read` / `AUD_RDR` to view.
