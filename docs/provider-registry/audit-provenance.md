# Harmonia Provider Registry — Audit & Provenance

### 1. Immutable Execution Lineage via Pragma Checkpoints

Every write operation creates an immutable execution history represented by `PragmaCheckpoint` audit records. Each checkpoint captures:
- `checkpointId`: Unique UUID for the checkpoint entry.
- `pragmaId`: Unique identifier of the enclosing change request.
- `ergonId`: Identifier of the executing Ergon processing activity (e.g. `practitioner-change-ergon`).
- `praxisId`: Workflow sequence definition (`seq-provider-registry-change-pipeline`).
- `stageName`: Canonical lifecycle stage (`INGEST`, `VALIDATE`, `APPROVE`, `COMMIT`, `COMPLETED`, `REJECTED`, `FAILED`).
- `timestamp`: UTC execution timestamp.
- `status`: Transitioned `PragmaStatus`.
- `statusMessage`: Diagnostic human-readable explanation of the step outcome.
- `stepIndex`: Ordered integer sequence index.

### 2. Audit Trail Structure

```
[Checkpoint 0: INGEST]
  - timestamp: 2026-09-16T09:30:00.100Z
  - statusMessage: "Change request received and queued for processing"

[Checkpoint 1: VALIDATE]
  - timestamp: 2026-09-16T09:30:00.250Z
  - statusMessage: "Referential and duplicate identifier validation passed"

[Checkpoint 2: APPROVE]
  - timestamp: 2026-09-16T09:30:00.300Z
  - statusMessage: "Automated first-iteration approval granted for Practitioner"

[Checkpoint 3: COMMIT]
  - timestamp: 2026-09-16T09:30:00.350Z
  - statusMessage: "Committing Practitioner to authoritative Mnemosyne persistence"

[Checkpoint 4: COMPLETED]
  - timestamp: 2026-09-16T09:30:00.410Z
  - statusMessage: "Practitioner/pract-dr-bowman-01 committed successfully at version 1"
```

### 3. Correlation & Causation Metadata

Every change `Pragma` records:
- `correlationId`: End-to-end trace ID passed from the client or generated at ingress.
- `requester`: Identity of the submitting user or system account.
- `sourceSystem`: Gateway or external system name (e.g. `pas-gw`, `pylai-fhir-registry`).
- `submittedAt`: ISO-8601 ingress timestamp.
- `resultingVersion`: Monotonically incremented version ID committed to Mnemosyne.
