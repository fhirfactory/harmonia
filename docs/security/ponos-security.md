# Ponos Execution & Dispatch Security

## 1. Overview

**Ponos** coordinates the ingestion of incoming Petasos queue messages and the dispatching of `Pragma` tasks into Camel route processing pipelines.

Prior to route execution, Ponos enforces security boundaries at two key integration points:
1. **Conduit Deserialization (`PetasosQueueToExchangeConduit`)**: Preserves deserialized `Pragma` domain context from the payload body without injecting synthetic service identities or default authorities on missing context.
2. **Workflow Dispatcher (`PragmaWorkflowDispatcher`)**: Acts as a mandatory Themis security gate, evaluating both originating requester claims and executing activity claims before permitting route execution.

---

## 2. Ingress & Dispatch Security Flow

### Conduit Context Primacy & Missing-Context Hardening
When `PetasosQueueToExchangeConduit` receives a `PetasosMessage`:
- If the payload is a serialized `Pragma` (or FHIR `Task`), the deserialized security context (`originatingPrincipal`, `originatingAuthorities`, `originatingSecurityContext`, `securityDomain`) is authoritatively preserved.
- Non-authoritative transport metadata (e.g., JMS properties) is NOT allowed to override domain security fields.
- If a message lacks security context (e.g. unauthenticated or non-Pragma messages), the conduit does NOT synthesize default identities (such as `service:petasos`) or default submit authorities (`provider.change.submit`). Missing context remains unauthenticated and is forwarded as-is to evaluate against default-deny policy.

### Dispatch Authorization
`PragmaWorkflowDispatcher` evaluates two distinct authorization claims:
1. **Originating Requester Authority**: Confirms the caller had valid submission authority (`provider.change.submit`). If `pragma.getOriginatingPrincipal()` is `null` / unauthenticated, the dispatcher evaluates with missing context without synthesizing fallback identities (`service:internal`) and fails closed (`DENY`).
2. **Ergon Execution Authority**: Confirms the executing activity possesses `provider.change.process`.

---

## 3. Dispatch Decision Matrix

```
Petasos Message Received
  │
  ├── 1. Conduit Deserialization (PetasosQueueToExchangeConduit)
  │     ├── Deserializes Pragma / Task JSON body
  │     └── Preserves domain context (No synthetic service:petasos fallback)
  │
  ├── 2. Check Originating Claims (SUBMIT_UPDATE / PROVIDER_REGISTRY)
  │     ├── Missing / Null Principal ──► Evaluate Missing Context ──► DENY
  │     ├── Insufficient Authority   ──► DENY ──► Mark Pragma FAILED (THEMIS_EXECUTION_GATE)
  │     └── ALLOW
  │           ▼
  └── 3. Check Ergon Execution Claims (PROCESS / PROVIDER_REGISTRY)
        ├── DENY ──► Mark Pragma FAILED (THEMIS_EXECUTION_GATE) & Terminate
        └── ALLOW ──► Dispatch to Camel Route
```

---

## 4. Failure Behaviour

If authorization is denied at the Ponos execution gate:
- Execution immediately halts; no downstream Camel routes or database operations are executed.
- The `Pragma` status is transitioned to `FAILED`.
- A checkpoint `THEMIS_EXECUTION_GATE` is recorded in the task audit log detailing the exact reason code (`AUTHORITY_MISSING` or `EXECUTION_AUTHORITY_MISSING`).
- Non-transient security failures are **not** retried.

---

## 5. Non-Canonical Legacy Paths

The `TaskQueueProducerService` direct-JMS fallback path bypasses Petasos abstraction and operates without `ThemisSecurityContext` or Petasos deduplication guarantees. It is classified as a non-canonical legacy transport and must not be used for security-bearing domain workflows without explicit boundary authentication.
