Task-Issued-At: UNRESOLVED

Event source: The attributable system timestamp of the operative implementation assignment entering the interaction was not exposed at the required precision. The Solution Architect disposition timestamps were also not supplied. No timestamp or filename prefix has been inferred from the current clock or unrelated repository metadata.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Mneme–Mnemosyne Governed Persistence V001

- **Assigned agent** · Junie — Claude Opus 5.5
- **Issuing authority** · Solution Architect, as identified in the operative V001 dispositions recorded below
- **Governed boundary** · `software-0.1.0/006`
- **Target software candidate** · `0.1.0`
- **Accepted Architecture baseline** · `1.1.26`, supplemented only by accepted offline designs
- **Backlog Item** · [00002, revision 1 — Mneme–Mnemosyne Governed Persistence V001](../../../../../../../docs/backlog/BacklogItem-0002-Mneme-Mnemosyne-Governed-Persistence-V001.001.md)
- **Entry branch / HEAD** · `feature/BL-CALLIOPE-OO-01` / `8af315134a1f2e03e75512f66513ef2f7eeb80b8`
- **Entry working tree** · Clean before this Task's Backlog Item was created; no pre-existing modifications were present.
- **Repository transition authority** · Implementation, testing and submission for review only. No commit, tag, push, deployment, Architecture release allocation or software acceptance authority.

## Solution Architect Dispositions

The following operative dispositions are reproduced verbatim from the issuing interaction. They are the task-specific authority for the Backlog Item and implementation scope and provenance boundary. The event timestamps were not exposed and remain unresolved.

### V001 Backlog Traceability disposition

````text
Solution Architect Disposition — V001 Backlog Traceability

The V001 implementation assignment is explicitly authorised by the Solution Architect.

The omission of a formal Backlog Item reference is an assignment-preparation deficiency, not a reason to abandon the implementation.

You are instructed to:

1. Inspect the existing Backlog Item conventions and identify the next appropriate available identifier.
2. Create a new Backlog Item, revision 1, for Mneme–Mnemosyne Governed Persistence — V001 in the established repository backlog location.
3. Use the issued V001 implementation assignment as the authoritative scope definition.
4. Record this Solution Architect disposition as the authority for the new Backlog Item and its implementation.
5. Proceed with the implementation under that Backlog Item without requiring another scope-approval cycle.

The authorised implementation scope remains unchanged:

- Mneme GovernedWriter and GovernedReader integration.
- Mnemosyne GovernedPersistenceFramework.
- Non-FHIR PersistenceService for InformationUnits and operational Status.
- PostgreSQL persistence.
- Governed write/read integration tests.
- Appropriate failure and indeterminate outcome handling.

The FHIR PersistenceService remains outside the primary V001 implementation scope.

Do not expand the backlog scope, alter accepted architectural decisions or interpret this disposition as a general waiver of the Software Development Praxis.

This authorisation applies specifically to V001.

Record the final Backlog Item ID, revision and repository path in the implementation evidence.

Continue with software implementation and testing.
````

### Mneme–Mnemosyne V001 iteration disposition

````text
Solution Architect Disposition — Mneme–Mnemosyne V001

The authorised implementation provenance boundary is:

- Software candidate: 0.1.0
- Implementation iteration: 006
- HCRL: hcrl-software-0.1.0-006.pending.md
- Evidence: enterprise-architecture/design/evidence/software-0.1.0/006/

This message constitutes the Solution Architect's explicit, task-specific authorisation to establish Iteration 006 for the Mneme–Mnemosyne V001 implementation.

Create the corresponding HCRL.006 in accordance with the Software Development Praxis, preserving the existing HCRL.004 and all outstanding review obligations.

Record this disposition verbatim in the V001 Task evidence, together with the authorised Backlog Item ID, revision and repository path.

The authorisation covers implementation, testing and submission for review. It does not constitute acceptance of the resulting software, approval of changes to architectural authority, or permission to commit, tag or push.

Proceed with the authorised V001 implementation.
````

## Operative Implementation Assignment

The original issued assignment is retained verbatim below. The Backlog Item adds no scope beyond it and the preceding Solution Architect dispositions.

````markdown
# Junie Implementation Assignment — Mneme and Mnemosyne V001

**Implementation Agent:** Junie — Claude Opus 5.5  
**Architecture Authority:** 1.1.26, supplemented by accepted offline designs  
**Software Candidate:** 0.1.0  
**Assignment Type:** Software implementation

## 1. Mission

Implement the first functional Mneme–Mnemosyne vertical slice.

**The expected outcome is working, tested software, not an implementation plan.**

Use the existing Harmonia codebase as the starting point.

Inspect existing implementations, identify reusable code, make appropriate engineering decisions and implement the required functionality.

Do not begin by producing a general implementation-gap analysis.

Where existing code conflicts with accepted architecture, correct or replace the relevant implementation within this assignment's scope.

If an architectural decision genuinely prevents implementation, document the exact blocker. Do not use uncertainty about ordinary implementation choices as a reason to stop.

## 2. Architecture

Mnemosyne provides two logical PersistenceServices:

1. **Non-FHIR PersistenceService** — InformationUnit and operational Status persistence.
2. **FHIR PersistenceService** — FHIR resource persistence.

Both operate through the GovernedPersistenceFramework.

The architectural interaction is:

```text
                  Mneme
                    |
         GovernedReader / Writer / Search
                    |
                    v
                Mnemosyne
                    |
               IU Controller
                 (PEP/PDP)
                    |
            IU Constructor /
               Disassembler
                    |
                    v
        GovernedPersistenceFramework
                    |
          +---------+---------+
          |                   |
          v                   v
       Non-FHIR              FHIR
    PersistenceService  PersistenceService
          |                   |
          v                   v
      PostgreSQL          PostgreSQL
```

This is a logical responsibility model, not a prescribed deployment or Java package structure.

### Important boundaries

- Mneme owns governed access, operational information management and applicable cache behaviour.
- Mnemosyne owns governed persistence execution.
- The GovernedPersistenceFramework coordinates persistence operations.
- PersistenceServices implement the appropriate information-storage behaviour.
- Themis supplies the relevant authorisation decisions.
- Mnemosyne must not fabricate authorisation or bypass the governing security contract.

## 3. Implementation scope — V001

### Primary target: Non-FHIR persistence

Implement the following end-to-end functionality:

**A. GovernedWriter**

Accept an InformationUnit through Mneme and establish it authoritatively through Mnemosyne's Non-FHIR PersistenceService.

**B. GovernedReader**

Retrieve the previously persisted InformationUnit through Mneme, using the governed Mnemosyne read path.

**C. InformationUnit integrity**

Preserve the required IU identity, representation, metadata, version and references.

**D. Status persistence**

Provide a basic governed mechanism for persisting and retrieving operational Status information through the Non-FHIR PersistenceService.

Status information must not automatically be converted into an InformationUnit.

Preserve the distinction between the semantic owner of Status and Mneme/Mnemosyne's responsibility for managing its availability and persistence.

### Secondary target: FHIR PersistenceService

Inspect the existing FHIR persistence implementation and establish the appropriate integration boundary with the GPF.

Reuse its existing implementation wherever suitable.

Do not undertake a wholesale FHIR persistence rewrite.

If feasible within the bounded V001 changes, provide a basic GPF-to-FHIR PersistenceService integration test.

Otherwise, retain it as the next implementation backlog item.

## 4. Existing code

Begin with the existing modules:

```text
hestia/
    mneme-cluster/
    mneme-persistence/
    mnemosyne-api/
    mnemosyne-clinical/
    mnemosyne-operations/
```

Also inspect the necessary Calliope models, Themis interfaces and existing integration tests.

Reuse existing functionality where architecturally appropriate.

Avoid unnecessary new frameworks, abstractions or replacement implementations.

**Existing module names do not establish architectural authority.**

Follow the Harmonia Implementation Guide, including its object-oriented design, documentation and source-size requirements.

## 5. Authoritative persistence

The existing asynchronous write-behind implementation must not be assumed to satisfy the accepted persistence contract.

A successful GovernedWriter operation requires confirmation of the applicable authoritative durability.

Distinguish:

- Cache acceptance.
- Local database persistence.
- Authoritative persistence.
- Required cross-site durability.
- Failed or indeterminate outcomes.

Do not report successful authoritative persistence merely because information has been accepted into an Infinispan cache.

For V001, use an explicitly identified development persistence configuration where necessary.

A successful local PostgreSQL transaction is evidence of local transactional persistence, **not proof of production cross-site durability**.

Production conformance must remain appropriately qualified until the required infrastructure and tests are available.

Do not implement an entire PostgreSQL HA/DR architecture in this assignment.

## 6. Implementation requirements

### 6.1 Non-FHIR PersistenceService

Implement or adapt the necessary persistence service for InformationUnits and operational Status.

Use PostgreSQL as the primary database target.

Preserve existing architectural requirements for:

- IU identity and version.
- IU metadata and information representation.
- Information references.
- Appropriate immutable historical information.
- Governed lifecycle operations.
- Accurate persistence outcomes.

Do not implement physical deletion as ordinary managed-information lifecycle behaviour.

### 6.2 GovernedPersistenceFramework

Implement the minimum GPF functionality necessary to support the V001 operations.

The GPF must coordinate the appropriate persistence operation and delegate to the Non-FHIR PersistenceService.

Keep persistence-engine details out of the common orchestration contracts.

### 6.3 Mnemosyne governance

Implement the required governance boundary for the supported operations.

Use the accepted Themis integration model.

Do not introduce a second independent security framework.

Where a required integration interface is incomplete, make the smallest compatible implementation needed to support the authorised vertical slice.

Do not weaken an existing authorisation boundary merely to make the integration test pass.

### 6.4 Mneme integration

Implement the corresponding Mneme-facing operations.

These should use the accepted Mneme-to-Mnemosyne responsibilities and avoid direct consumer access to persistence implementations.

For V001, do not require completion of the full distributed cache architecture.

Implement only the cache behaviour necessary for correct operation, if any.

## 7. Testing

The primary acceptance test is:

```text
1. Construct valid InformationUnit
              |
              v
2. Mneme GovernedWriter
              |
              v
3. Mnemosyne
              |
              v
4. GovernedPersistenceFramework
              |
              v
5. Non-FHIR PersistenceService
              |
              v
6. PostgreSQL persistence
              |
              v
7. Confirm persistence outcome
              |
              v
8. Mneme GovernedReader
              |
              v
9. Retrieve persisted InformationUnit
              |
              v
10. Verify integrity
```

Provide automated tests demonstrating:

1. Successful IU creation and persistence.
2. Successful governed retrieval.
3. Preservation of IU identity, metadata, version and representation.
4. A basic Status persistence/read round trip.
5. Rejection of unauthorised operations.
6. Correct distinction between successful, failed and indeterminate outcomes where the applicable failure can be exercised.
7. Restart/reconnection behaviour where practical.
8. Correct handling of duplicate or repeated requests within the implemented contract.

Use JUnit and the existing Maven testing infrastructure.

Prefer Testcontainers with PostgreSQL for meaningful integration testing where the environment supports it.

Do not substitute H2-only test success for PostgreSQL integration evidence.

Do not claim tests were executed when they were not.

## 8. Engineering autonomy

Junie is authorised to:

- Inspect relevant source and tests.
- Add or modify Java classes.
- Refactor existing code necessary to implement the agreed functionality.
- Add or modify Maven dependencies.
- Add database migrations and configuration.
- Implement supporting test infrastructure.
- Execute builds and tests.
- Make reasonable engineering decisions within the accepted architecture.

Junie is **not** authorised to:

- Change architectural responsibilities.
- Modify accepted DRPs or the Architecture Continuum.
- Invent alternative persistence or security architectures.
- Change accepted architectural baselines.
- Implement unrelated functionality.
- Weaken governance or durability requirements.
- Commit, tag or push without separate authorisation.

## 9. Constraints and implementation guardrails

Follow existing repository instructions and the accepted Software Development Praxis.

Before modifying code:

1. Confirm the current branch and HEAD.
2. Inspect the existing working-tree state.
3. Preserve pre-existing modifications.
4. Identify the relevant existing code and tests.

Avoid large, speculative refactoring.

Prefer cohesive, independently testable implementation components.

Follow Harmonia's Implementation Guide and existing coding standards.

Do not create unnecessary generic frameworks.

Do not use the development environment's capabilities as evidence that production durability, resilience or availability requirements have been satisfied.

## 10. Failure and blocker handling

**The assignment is to implement software. Encountering a problem is not, by itself, a reason to stop.**

When an implementation issue occurs:

1. Diagnose the issue.
2. Determine whether it is an ordinary engineering problem or a genuine architectural ambiguity.
3. Resolve ordinary engineering problems within the assignment's scope.
4. Continue implementation and testing.
5. Escalate only genuine architectural blockers or constraints that cannot be satisfied safely.

If an implementation cannot be completed, preserve and report the useful work already performed.

For each unresolved issue, identify:

- The specific problem.
- The affected code or test.
- What was attempted.
- The observed result.
- Whether it represents an architectural blocker, environmental limitation or implementation defect.
- The smallest corrective action or additional guardrail required.

Do not turn implementation difficulties into a general architecture research exercise.

## 11. Evidence and report

Follow the established software-development evidence conventions.

Produce a concise implementation report identifying:

- Entry branch, HEAD and working-tree condition.
- Files created or modified.
- Implementation decisions.
- Reused versus replaced functionality.
- Build and test commands.
- Tests passed, failed or not executed.
- PostgreSQL integration evidence.
- Known limitations.
- Outstanding dependencies.
- Proposed next implementation increment.

**Evidence must distinguish implemented behaviour from intended behaviour.**

Do not mark the work accepted on behalf of the Solution Architect.

## 12. Completion criteria

V001 is ready for Solution Architect review when:

1. A governed IU write/read round trip works through Mneme and Mnemosyne.
2. The Non-FHIR PersistenceService persists the IU in PostgreSQL.
3. The GPF coordinates the relevant persistence operations.
4. Applicable governance is enforced.
5. IU identity, representation and version integrity are verified.
6. Basic operational Status persistence is demonstrated.
7. Relevant automated tests have been executed.
8. Known production-conformance gaps are explicitly disclosed.

If some criteria cannot be completed, report actual progress and precise blockers rather than claiming completion.

**The required deliverable is working, tested software.**

The subsequent activity will be a Codex implementation review and Solution Architect HCRL adjudication.

---

**Implementation instruction:** Begin engineering the V001 vertical slice immediately. Do not substitute an implementation-gap analysis, design proposal or broad architectural investigation for the requested implementation.
````