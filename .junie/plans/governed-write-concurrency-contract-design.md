---
sessionId: session-260925-151930-rag7
---

# Requirements

### Goal / Outcome
Author the authoritative design specification for Harmonia's governed write and concurrency contract (`docs/design/governed-write-concurrency-contract.md`), establishing the Strong Hybrid architecture contract between callers/workflows and Mneme/Mnemosyne storage without implementing production code.

### Scope
- **In Scope**:
  - Creation of `docs/design/governed-write-concurrency-contract.md` with all 19 required specification sections.
  - Incorporation of all Task 08.02B architectural review corrections (opaque coordination tokens, Mneme non-authoritative boundary, CAS-based convergence loop, deferral of clinical consistency tiers).
  - Conceptual Java contract interface shapes, version domain definitions, conflict models, and test plan specifications.
  - Updating `docs/README.md` information architecture index to register the new `docs/design/` directory and document.
- **Out of Scope**:
  - Implementing production Java interfaces, services, or adapters.
  - Modifying Infinispan XML or cache configuration.
  - Modifying database schemas, JPA entities, or `@Version` annotations.
  - Implementing conditional REST/JPA write methods.
  - Removing legacy DELETE endpoints or production code in this step.

### Done When
- `docs/design/governed-write-concurrency-contract.md` exists, is completely populated across sections 1–19, and satisfies all Task 08.02B corrections.
- `docs/README.md` reflects the `docs/design/` document hierarchy and links to the new contract.
- No production source code, tests, schemas, or configurations are modified.
- Existing architecture tests (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) remain completely green.

# Technical Design

### Decisions
- **chose `docs/design/governed-write-concurrency-contract.md` / not `docs/architecture.md` or `docs/architecture/`** — `docs/architecture.md` is specifically Petasos documentation, and creating `docs/design/` provides a dedicated location for formal API and concurrency contracts across subprojects.
- **chose opaque Infinispan entry token for active coordination / not application-level arithmetic counter (G17 -> G18)** — Infinispan CAS (`replaceWithVersion`) already provides atomic progression safety; assigning arithmetic meaning to cache tokens introduces fragile state coupling.
- **chose explicit 4-domain version model / not conflating cache and FHIR versions** — Mneme entry tokens, Mnemosyne persistence versions, FHIR `meta.versionId`, and HTTP ETags have distinct owners, lifetimes, and comparison semantics.
- **chose reporting authoritative commit success with degraded cache convergence flag / not reporting failure when commit succeeds** — reporting failure after an authoritative database commit encourages callers to retry operations that already committed, causing spurious duplicate conflicts or double-mutations.
- **chose strict UPDATE-only lifecycle semantics / no governed DELETE (ADR-020)** — soft/hard deletes are rejected at the governed contract layer; deactivations and status updates are modeled purely as conditional UPDATEs.

### Approach & Touches
- **Target document**: `docs/design/governed-write-concurrency-contract.md`
  - Section 1: Purpose
  - Section 2: Architectural Context (Strong Hybrid model, ADR-018/019/020)
  - Section 3: Invariants (Separation of coordination vs authoritative commit)
  - Section 4: Governed Read Model (`GovernedRead<T>`)
  - Section 5: CREATE Contract (Strict uniqueness, no upsert)
  - Section 6: UPDATE Contract (Complete end-to-end sequence)
  - Section 7: Active Coordination Contract (Mneme token CAS, failure modes)
  - Section 8: Authoritative Persistence Contract (Mnemosyne conditional update)
  - Section 9: Conflict Model (`ActiveStateConflict` vs `AuthoritativeStateConflict`)
  - Section 10: Result Model (`WriteResult<T>`, committed version, convergence status)
  - Section 11: Version Model (4 domains matrix & comparison rules)
  - Section 12: Guarded Mneme Convergence (CAS loop & newer-version invariant)
  - Section 13: Commit Outcome Semantics (Handling timeout/unknown outcomes)
  - Section 14: Security / Provenance Context (Reusing Themis & Calliope models)
  - Section 15: API Placement (Service boundaries in Hestia/Calliope/Energeia)
  - Section 16: Bypass Prevention (ArchUnit rules, encapsulation, restricted APIs)
  - Section 17: Developer Usage Examples (Idiomatic developer experience)
  - Section 18: Contract Test Plan (16 mandatory verification scenarios)
  - Section 19: Deferred Questions (Semantic conflicts, multi-resource sagas)
- **Documentation index**: `docs/README.md` (register `docs/design/` in Information Architecture).

### Nuances / Risks / Corners
- **Doc-First Guardrail**: Ensure no production `.java` files are touched. Conceptual code in the design document must be presented purely as Markdown code blocks.
- **Idempotency & Unknown Outcomes**: The contract must explicitly define how ambiguous network failures between caller and Mnemosyne are classified (`CommitOutcomeUnknown`) without guessing success or failure.
- **Existing Bypass Locations**: The document must explicitly catalog existing bypass risks (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`) as targets for future migration.

### Contracts (Conceptual Interface Shapes in Design Doc)
```java
public record GovernedRead<T>(
    ResourceKey key,
    T resource,
    ActiveCoordinationToken activeToken,
    ExpectedAuthoritativeVersion authoritativeVersion
) {}

public record GovernedWriteContext(
    ThemisSecurityContext securityContext,
    String correlationId,
    String causationId,
    String sourceSystem
) {}

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, GovernedWriteContext context);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, GovernedWriteContext context);
}
```

# Testing

### Checklist
- [ ] Verify that `docs/design/governed-write-concurrency-contract.md` includes all 19 numbered sections with complete, robust content.
- [ ] Verify that all Task 08.02B review corrections (opaque token CAS, Mneme non-authority, CAS convergence loop, consistency tier deferral) are explicitly incorporated.
- [ ] Verify that all 16 contract test scenarios from Task Part 17 are detailed with expected outcomes.
- [ ] Verify that `docs/README.md` correctly references `docs/design/` in its documentation tree.
- [ ] Verify git status shows zero modified/added production Java files or configuration artifacts.
- [ ] Regression check: ArchUnit test suite runs and passes cleanly.

# Assumptions & Open Questions

- **Documentation Root**: `docs/design/` will be established as the canonical directory for platform API and concurrency design contracts.
- **Security & Provenance Model Reuse**: Reuses `ThemisSecurityContext` and `PersistenceOperationEnvelope` context fields instead of inventing duplicate security records.

# Delivery Steps

### ✓ Step 1: Author Governed Write and Concurrency Contract Design Document and Update Doc Index
Goal: Create `docs/design/governed-write-concurrency-contract.md` containing the complete 19-section architectural contract for governed writes, and register `docs/design/` in `docs/README.md`.
Scope: `docs/design/governed-write-concurrency-contract.md`, `docs/README.md`
Acceptance Criteria:
- [ ] `docs/design/governed-write-concurrency-contract.md` is authored containing sections 1 through 19.
- [ ] All 4 corrections from Task 08.02B are addressed: opaque coordination tokens without arithmetic meaning, Mneme token consumption without durable update authority, CAS loop for guarded convergence, and deferral of resource-specific clinical consistency tiers.
- [ ] The four version domains (Mneme entry token, Mnemosyne DB version, FHIR meta.versionId, HTTP ETag) are formally distinguished with ownership, lifetime, and comparison invariants.
- [ ] Conflict taxonomy (`ActiveStateConflict`, `AuthoritativeStateConflict`) and result semantics (`CommitOutcomeUnknown`, commit success with degraded convergence) are fully specified.
- [ ] Conceptual Java signatures for `GovernedRead`, `GovernedWriteContext`, `GovernedWriter`, `WriteResult`, and token records are documented.
- [ ] Bypass prevention strategies (ArchUnit rules, restricted interfaces) and all 16 verification scenarios in the test plan are enumerated.
- [ ] `docs/README.md` is updated to include `docs/design/` in its documentation index.
- [ ] No production source code, tests, build files, or configurations are modified.
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green