# Ponos Execution & Dispatch Security

## 1. Overview

**Ponos** coordinates the dispatching of incoming `Pragma` tasks into Camel route processing pipelines.

Prior to route execution, `PragmaWorkflowDispatcher` acts as a mandatory Themis security gate, evaluating two distinct authorization claims:
1. **Originating Requester Authority**: Confirms the caller had valid submission authority (`provider.change.submit`).
2. **Ergon Execution Authority**: Confirms the executing activity possesses `provider.change.process`.

---

## 2. Dispatch Decision Matrix

```
Pragma Received
  │
  ├── 1. Check Originating Claims (SUBMIT_UPDATE / PROVIDER_REGISTRY)
  │     ├── DENY ──► Mark Pragma FAILED (THEMIS_EXECUTION_GATE) & Terminate
  │     └── ALLOW
  │           ▼
  └── 2. Check Ergon Execution Claims (PROCESS / PROVIDER_REGISTRY)
        ├── DENY ──► Mark Pragma FAILED (THEMIS_EXECUTION_GATE) & Terminate
        └── ALLOW ──► Dispatch to Camel Route
```

---

## 3. Failure Behaviour

If authorization is denied at the Ponos execution gate:
- Execution immediately halts; no downstream Camel routes or database operations are executed.
- The `Pragma` status is transitioned to `FAILED`.
- A checkpoint `THEMIS_EXECUTION_GATE` is recorded in the task audit log detailing the exact reason code (`AUTHORITY_MISSING` or `EXECUTION_AUTHORITY_MISSING`).
- Non-transient security failures are **not** retried.
