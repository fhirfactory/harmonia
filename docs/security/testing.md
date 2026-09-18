# Themis Testing Strategy & Verification Guide

## 1. Utilitarian Testing Philosophy

Security verification follows a strict utilitarian testing strategy to prove **defence in depth**:
> Every security boundary must be independently testable, and removing authority at any single checkpoint must prevent state mutation.

---

## 2. Key Acceptance Scenarios

| Scenario | Scope | Expected Behaviour |
| :--- | :--- | :--- |
| **Scenario A** | Read & Search | Principal with `PRV_RDR` retrieves and searches Provider Registry resources successfully. |
| **Scenario B** | Unauthorized Write | Principal with `PRV_RDR` attempts PUT/POST; rejected by Pylai with HTTP 403; no `Pragma` is created. |
| **Scenario C** | Governed Write | `PRV_SUB` caller submits -> Pylai `ALLOW` -> Ponos `ALLOW` -> Ergon & Storage `ALLOW` -> Change committed. |
| **Scenario D** | Execution Privilege Failure | `PRV_SUB` Pragma accepted, but Ergon execution authority `PRV_PROC` is revoked -> Ponos halts dispatch; database untouched. |
| **Scenario E** | Persistence Privilege Failure | Ingress and Ponos succeed, but storage authority `provider.resource.update` is revoked -> Ergon persistence gate denies write; database untouched. |

---

## 3. Negative & Tampering Test Suites
- **Context Tampering**: Serializing and reconstructing `Pragma` claims to verify immutable fidelity and structure.
- **Direct Persistence Bypass**: Calling `FhirStorageService` without required authorities raises `ForbiddenOperationException`.
- **Default Deny Safety**: Invocations with missing roles, unmapped domains, or invalid actions deterministically return `DENY`.
