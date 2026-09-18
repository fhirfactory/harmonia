# Threat Model & Risk Analysis

## 1. System Threat Vectors & Themis Mitigations

| Threat Vector | Description | Themis Mitigation | Residual Risk |
| :--- | :--- | :--- | :--- |
| **API Gateway Compromise** | An edge ingress node is breached, allowing attacker to forge arbitrary internal messages. | **Defence in Depth**: Internal workflow engines (Ponos) and persistence services (Mnemosyne) independently evaluate authorization policies. Direct writes require storage authorities. | Low |
| **Privilege Escalation via Background Task** | A low-privileged user submits a task hoping the background processor executes it with administrative powers. | **No Privilege Amplification**: Dual-authority evaluation requires both valid originating submit authority (`PRV_SUB`) and Ergon execution authority (`PRV_PROC`). | Low |
| **Pragma Security Context Tampering** | An attacker modifies message attributes or claims while in transit on message queues. | **Context Integrity**: Themis derives claims only from authenticated gateway context and evaluates structured immutable types. Malformed claims fail closed. | Low |
| **Direct Persistence Bypass** | An unauthenticated internal component invokes `FhirStorageService` directly. | **Default Deny Persistence Gate**: `FhirStorageService` enforces Themis authorization prior to performing SQL inserts or updates. | Low |
| **Stale Privilege Retention** | A user's role is revoked while an asynchronous change task is queued. | **Re-evaluation at Execution**: Policies re-evaluate active context and claims at the execution boundary before committing mutations. | Low |
| **Audit Log PHI Leakage** | Authorization logs capture patient names or sensitive clinical payloads. | **Explicit Metadata Sanitization**: Audit records strictly capture metadata (`principalId`, `action`, `resourceId`, `correlationId`), excluding payload bodies. | Very Low |
| **Security Service Outage** | Themis policy service crashes or becomes unreachable. | **Fail-Safe Operation**: All boundary enforcers fail closed to `DENY` upon service failure or exception. | Low (Availability impact only) |
