# Failure Behaviour & Fail-Safe Defaults

## 1. Fail-Closed Security Policy

In accordance with the foundational `DEFAULT DENY` principle, any security anomaly, service outage, context corruption, or unhandled exception deterministically evaluates to `DENY`:

```
Security Engine Error / Malformed Context
                  │
                  ▼
                DENY
```

---

## 2. Decision Reason Codes (`ThemisDecisionReason`)

| Reason Code | Category | Explanation |
| :--- | :--- | :--- |
| `ALLOWED_BY_POLICY` | Positive | Active policy explicitly authorized the operation |
| `EXPLICIT_DENY` | Rejection | Explicit denial rule triggered |
| `DEFAULT_DENY` | Rejection | Fallback default when no matching allow rule was found |
| `POLICY_NOT_FOUND` | Rejection | No active policy registered for the target domain/action |
| `PRINCIPAL_MISSING` | Rejection | Authorization request lacked an authenticated principal |
| `AUTHORITY_MISSING` | Rejection | Principal lacked required granular authority token |
| `EXECUTION_AUTHORITY_MISSING` | Rejection | Ergon or Ponos activity lacked processor authority |
| `PERSISTENCE_AUTHORITY_MISSING` | Rejection | Persistence gate denied storage mutation |
| `ACTION_NOT_PERMITTED` | Rejection | Action is not permitted on the target resource |
| `RESOURCE_TYPE_NOT_PERMITTED` | Rejection | Resource type is not supported by the target domain |
| `SECURITY_LABEL_NOT_PERMITTED` | Rejection | Security label on the data prevents access |
| `CONTEXT_MALFORMED` | Rejection | Corrupted or unparseable security context |
| `SECURITY_SERVICE_ERROR` | Rejection | Engine failure or unexpected exception |
