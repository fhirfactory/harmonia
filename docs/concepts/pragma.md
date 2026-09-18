# Concept: Pragma `[IMPLEMENTED]`

A Pragma is Harmonia's canonical task execution state envelope, encapsulating the live runtime context, security assertions, correlation identifiers, and granular checkpoints of an ongoing clinical integration transaction.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Πρᾶγμα* (Pragma, plural: *πράγματα* - Pragmata)
- **Etymology**: Ancient Greek neuter noun derived from *πράσσω* (to do, achieve), meaning a thing done, a completed deed, a concrete affair, a matter of fact, or an active enterprise.
- **Philosophical Context**: In classical philosophy, whereas *praxis* refers to the ongoing practice or deliberate action, a *pragma* is the concrete reality, deed, or tangible business matter resulting from or undergoing that action. It is the objective fact of the transaction.
- **Architectural Rationale**: In distributed message-driven systems, an event cannot merely be a bag of uncontextualized clinical data. It requires an authoritative, objective state envelope that records *what has been done*, *who authorized it*, *which checkpoints were passed*, and *what remains to be accomplished*. The Pragma is that authoritative record.

---

## 2. Architectural Definition `[IMPLEMENTED]`

A Pragma is an immutable-friendly execution envelope that travels alongside the clinical payload through every stage of the Energeia execution pipeline:

```
+---------------------------------------------------------------------------------------+
|                                    PRAGMA ENVELOPE                                    |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                       CORRELATION & IDENTITY HEADERS                           |  |
|   |   - pragmaId: UUID (Unique identifier for this discrete execution state)       |  |
|   |   - taskSequenceId: UUID (Correlates all steps across the end-to-end workflow) |  |
|   |   - parentTaskId: Optional UUID (Identifies parent task in fan-out trees)      |  |
|   |   - creationTimestamp: ISO-8601 UTC Instant                                    |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                       PRAGMA SECURITY CONTEXT (Themis)                         |  |
|   |   - principal: ThemisPrincipal (Caller identity / service account)             |  |
|   |   - activeRoles: Set<HarmoniaRoleEnum> (e.g., CLINICAL_SYSTEM, GATEWAY_SERVICE)|  |
|   |   - authorities: Set<HarmoniaAuthorityEnum> (e.g., PRV_PROCESS, PRV_TRANSFORM) |  |
|   |   - securityLabels: Set<HarmoniaSecurityLabelEnum> (e.g., RESTRICTED, CONFIDENTIAL)|
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                       GRANULAR CHECKPOINT TRAIL (REC-002)                      |  |
|   |   - List<PragmaCheckpoint>:                                                    |  |
|   |       1. INGRESS_ACCEPTED (pylai-mllp-in, timestamp: T0)                       |  |
|   |       2. TRANSFORM_COMPLETED (Adt2FhirErgon, timestamp: T1)                    |  |
|   |       3. FANOUT_DISPATCH_INITIATED (AdtDistributionErgon, timestamp: T2)       |  |
|   |          * destination: HIS_NORTH (status: QUEUED)                             |  |
|   |          * destination: LIS_MAIN  (status: QUEUED)                             |  |
|   |          * destination: RIS_CENTRAL (status: QUEUED)                           |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                       OPAQUE PAYLOAD REFERENCE (Zero-PHI)                      |  |
|   |   - payloadUri: Reference to Mneme cache key or Mnemosyne resource ID          |  |
|   |   - payloadFormat: Content-Type (e.g., application/fhir+json, x-application/hl7)|
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Pragma Owns
- The definition and serialization of the execution state envelope (`Pragma`).
- Checkpoint record representation (`PragmaCheckpoint`) and state transition rules.
- Cryptographic and role security context encapsulation (`PragmaSecurityContext`).
- Granular sub-destination delivery tracking metadata (REC-002).
- Zero-PHI payload referencing (stores references, never raw unmasked clinical payloads in log streams).

### What Pragma Explicitly Does NOT Own (Anti-Responsibilities)
- Executing business rules or running activity steps (owned by Erga).
- Spawning worker threads or managing consumer loops (owned by Ponos).
- Defining workflow blueprint DAGs (owned by Praxis).
- Network socket wire transport (owned by Pylai / Petasos).

---

## 4. Key Classes & Interfaces `[IMPLEMENTED]`

| Class Name | Module Name | Architectural Role | Status |
| :--- | :--- | :--- | :--- |
| `Pragma` | `energeia-erga` / `calliope` | The canonical execution context envelope | `[IMPLEMENTED]` |
| `PragmaCheckpoint` | `energeia-erga` | Records an individual activity execution milestone | `[IMPLEMENTED]` |
| `PragmaSecurityContext` | `themis-api` | Immutable security assertions carried in the Pragma | `[IMPLEMENTED]` |
| `PragmaStatusEnum` | `energeia-erga` | Lifecycle states: `PENDING`, `IN_PROGRESS`, `COMPLETED`, `FAILED` | `[IMPLEMENTED]` |
| `DestinationStatus` | `energeia-erga` | Per-destination sub-status for fan-out dispatches (REC-002) | `[IMPLEMENTED]` |

---

## 5. Architectural Invariant: Zero-PHI Context Safety `[IMPLEMENTED]`

Under Harmonia's **Zero-PHI Logging Invariant**:
- A `Pragma` contains only correlation UUIDs, security assertions, timestamps, and payload keys.
- Clinical payload data (patient names, dates of birth, diagnoses) is stored in the backing cache or database, never embedded directly in the `Pragma` header fields.
- When `Pragma` state is emitted in standard operational logs (`INFO`/`WARN`), zero PHI is leaked to monitoring consoles.
