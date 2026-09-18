# Harmonia Provider Registry — Failure & Restart Recovery

### 1. Durability & Crash Resilience Guarantees

The Provider Registry change processing architecture is designed to withstand broker terminations, container restarts, and runtime failures without loss of change requests or data corruption:

1. **Dual State Persistence**:
   - Change requests are stored durably in Apache ActiveMQ Artemis message journals before `202 Accepted` is returned.
   - Task progression is persisted in the distributed Infinispan `PragmaCacheService` and HIE database.
2. **Idempotent Resumption**:
   - If Ponos terminates during in-flight execution, unacknowledged Artemis messages redeliver to the queue upon restart.
   - `PragmaWorkflowDispatcher` inspects the latest `PragmaCheckpoint` on the task instance and resumes execution from the last uncompleted Ergon activity.
   - If a change is already marked `COMPLETED`, replaying the event is a no-op that does not create duplicate database records.
3. **Optimistic Concurrency Protection**:
   - Stale concurrent update requests attempting to overwrite a resource based on an outdated version are rejected (`PR-VAL-006`) without silent lost updates.

### 2. Failure Handling Matrix

| Failure Mode | Impact | Recovery Mechanism |
| :--- | :--- | :--- |
| Broker Crash before Ergon Commit | Message remains in Artemis durable journal. | WorkEngine restarts, consumes message, executes Ergon pipeline to `COMPLETED`. |
| Validation Failure (Missing Ref) | Task cannot be applied. | State transitions to `REJECTED`, `OperationOutcome` recorded in `Task.output`, no database mutation. |
| Version Conflict on `PUT` (`If-Match`) | Request based on stale data. | State transitions to `FAILED`, `PR-VAL-006` recorded in `Task.output`, stored version untouched. |
| Node Termination during Ergon Processing | Task partially completed. | Checkpoint manager records intermediate stage; restarted node resumes from latest checkpoint. |
