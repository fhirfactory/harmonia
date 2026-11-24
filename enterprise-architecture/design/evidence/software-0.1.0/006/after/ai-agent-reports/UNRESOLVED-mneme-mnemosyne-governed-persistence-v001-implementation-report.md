Report-Produced-At: UNRESOLVED

Event source: The attributable system timestamp of completed Report emission was not exposed at the required precision. The filename prefix and field remain explicitly unresolved; neither current time nor filesystem/commit metadata has been substituted.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Implementation Report — Mneme–Mnemosyne Governed Persistence V001

- **Target:** Software candidate `0.1.0`; implementation iteration `006`
- **Task:** [V001 implementation Task](../../before/ai-agent-tasks/UNRESOLVED-mneme-mnemosyne-governed-persistence-v001-task.md)
- **Backlog Item:** [00002, revision 1 — Mneme–Mnemosyne Governed Persistence V001](../../../../../../../docs/backlog/BacklogItem-0002-Mneme-Mnemosyne-Governed-Persistence-V001.001.md)
- **HCRL:** [hcrl-software-0.1.0-006.pending.md](../../../../../review/software-0.1.0/hcrl-software-0.1.0-006.pending.md)
- **Entry branch / HEAD:** `feature/BL-CALLIOPE-OO-01` / `8af315134a1f2e03e75512f66513ef2f7eeb80b8`
- **Entry working tree:** clean before this assignment's authorized Backlog Item was created
- **Resulting repository state:** uncommitted candidate worktree; no commit, merge, tag, push, deployment, release allocation or acceptance was performed or inferred
- **Architecture:** baseline `1.1.26`; no Continuum change or new architecture decision was made

## Result

Implemented the V001 governed Non-FHIR vertical slice through Mneme, Mnemosyne's Themis PEP/controllers, the GovernedPersistenceFramework (GPF), and a PostgreSQL-backed Non-FHIR PersistenceService. The end-to-end Testcontainers PostgreSQL tests establish IU create/read, version progression, duplicate replay, Status persistence/read, authorization rejection, database unavailability handling and fresh-service-stack reconnection.

The existing FHIR persistence implementation was not rewritten. A small GPF binding now routes the existing `AuthoritativePersistencePort<IBaseResource>` through the GPF, with a PostgreSQL-backed Clinical integration test for FHIR create/update/read.

## Implementation decisions and boundaries

- Reused Calliope `InformationUnit`, `ResourceKey`, `ExpectedAuthoritativeVersion`, `AuthoritativeVersion`, `GovernedReader`, `GovernedWriter`, `GovernedRead`, `WriteResult`, active-state coordination, and the existing Themis API/evaluator contracts.
- Added Mnemosyne-facing IU and operational Status ports in `mnemosyne-api`; extended committed persistence results with explicit `DurabilityAssurance`. Existing two-argument FHIR `Committed` construction remains source compatible and defaults to `UNQUALIFIED`; unknown results retain their observed lower-level assurance when a required profile is unmet.
- Added Themis-enforced IU and Status controllers and a GPF that routes Non-FHIR operations to the engine-neutral service. Both boundaries fail closed; the integration test uses a narrow test-only Themis policy. No production IU/Status policy was invented or added.
- Added PostgreSQL append-only IU version history with UUID/canonical topic identity, canonical JSON representation and SHA-256 integrity verification. The disassembler excludes the IU's optional Mneme `ManagementContext` from authoritative durable JSON and clears caller-assigned versions; Mnemosyne assigns the persisted version. Identical create replay returns the existing version; a conflicting duplicate and stale expected version are conflicts. No physical-delete lifecycle was added.
- Added owner/subject/type keyed opaque Status history with expected-version updates. Status payload semantics remain with its owner; no Status is converted into an InformationUnit.
- Added explicit `DEVELOPMENT_LOCAL_TRANSACTION` and `PRODUCTION_CROSS_SITE` assurance profiles. A local PostgreSQL commit proves local transactional persistence only. When the production profile sees only local assurance, the result is `OutcomeUnknown` with observed local assurance retained. The development schema bootstrap is explicit and idempotent; production migrations remain a deployment responsibility.
- Reused the existing Clinical FHIR persistence port and transaction implementation through `FhirPersistenceServiceBinding`. Its tested assurance is local transaction only; FHIR changes remain secondary and no wholesale rewrite was performed.

## Files created or modified

- `docs/backlog/BacklogItem-0002-Mneme-Mnemosyne-Governed-Persistence-V001.001.md`
- `enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-006.pending.md`
- `enterprise-architecture/design/evidence/software-0.1.0/006/before/ai-agent-tasks/UNRESOLVED-mneme-mnemosyne-governed-persistence-v001-task.md`
- `enterprise-architecture/design/evidence/software-0.1.0/006/after/ai-agent-reports/UNRESOLVED-mneme-mnemosyne-governed-persistence-v001-implementation-report.md`
- `hestia/mnemosyne-api/pom.xml`; `AuthoritativePersistenceResult.java`; new `DurabilityAssurance.java`; new `GovernedInformationUnitPersistence.java`, `GovernedOperationalStatusPersistence.java`, `OperationalStatusKey.java`, and `OperationalStatusRecord.java`; new `DurabilityAssuranceTest.java` and `OperationalStatusKeyTest.java`.
- `hestia/mnemosyne-operations/pom.xml`; new GPF, FHIR binding and durability profile; Themis authorization gate; IU disassembler/constructor; Non-FHIR contract, PostgreSQL service/stores/failure classifier/schema initializer; `src/main/resources/db/mnemosyne/nonfhir/V001__information_unit_and_status.sql`; and unit/controller tests for conversion/integrity, durability, SQLSTATE mapping and authorization.
- `hestia/mneme-cluster/pom.xml`; new `InformationUnitGovernedWriter.java`, `InformationUnitGovernedReader.java`, and `OperationalStatusGovernedAccess.java`; `MnemeMnemosynePostgresIntegrationTest.java`; test-scoped `src/test/resources/docker-java.properties` setting Docker API `1.44` for this environment's Testcontainers client.
- `hestia/mnemosyne-clinical/pom.xml`; updated `AuthoritativePersistencePostgreSqlConcurrencyTest.java` with the GPF-to-existing-FHIR PostgreSQL create/update/read case.

All newly added production Java files are below the 500-line source limit; the largest is `PostgresInformationUnitStore.java` at 320 lines.

## Acceptance criteria evidence

| Criterion | Result | Evidence |
| --- | --- | --- |
| Governed IU write/read and integrity | PASS | Testcontainers PostgreSQL 16 acceptance test verifies UUID/canonical topic identity, metadata/relationships, representation, version, digest reconstruction and fresh service-stack reconnection. |
| Versioning, duplicate/repeat requests | PASS | Identical create replay retains version 1 without a second history row; update appends version 2; stale version 1 is rejected; append-only history contains two rows. |
| Owner-defined Status round trip | PASS | Status create/read/update reaches version 2 and verifies no IU identity row was created. |
| Themis authorization | PASS | No matching policy denies at both Mneme and Mnemosyne boundaries before database mutation. The allow policy is test-only. |
| Failed/indeterminate/durability results | PASS with qualification | Database connection refusal before commit returns `NotCommitted`; SQLSTATE 08006 during a synthetic commit-phase classifier test returns `OutcomeUnknown`; a production-profile local commit is returned as `OutcomeUnknown` with `LOCAL_TRANSACTION_COMMITTED` observed. Real connection loss during COMMIT was not induced. |
| Restart/reconnection | PASS | A fresh service/controller/reader stack reads the previously committed IU from the same PostgreSQL database. |
| FHIR boundary | PASS (secondary target) | Existing Clinical PostgreSQL concurrency test now exercises GPF-bound FHIR create/update/read; existing FHIR implementation remains unchanged. |
| Production conformance | QUALIFIED / NOT ESTABLISHED | No production cross-site PostgreSQL durability, HA/DR, or production Themis IU/Status policy was available or claimed. |

## Verification actually executed

All Maven invocations used OpenJDK `21.0.13.1` from `/usr/lib/jvm/java-21-openjdk-amd64`.

1. `mvn -B -ntp test -pl hestia/mnemosyne-clinical -am -Dtest=AuthoritativePersistencePostgreSqlConcurrencyTest -Dsurefire.failIfNoSpecifiedTests=false` — PASS; 4 PostgreSQL Testcontainers tests, including the GPF-to-FHIR binding case.
2. `mvn -B -ntp test -pl hestia/mneme-cluster,hestia/mneme-persistence,hestia/mnemosyne-api,hestia/mnemosyne-clinical,hestia/mnemosyne-operations -am` — PASS; all 11 reactor modules succeeded. Maven's final Surefire result line reported 93 tests for its final module; no reactor-wide aggregate count was printed.
3. `mvn -B -ntp test -pl paradeigma/paradeigma-test -am -Dtest='*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` — PASS; 37 reactor modules, 90 architecture tests, zero failures/errors/skips.
4. `mvn -B -ntp test` — PASS; all 50 reactor modules succeeded in 7:14. Maven's final Surefire result line reported 158 tests for the final module with zero failures/errors/skips; no reactor-wide aggregate count was printed.
5. `git diff --check` — PASS. Source-size review — PASS; all new production Java files are below 500 lines.

During development, a test assertion initially compared an `Optional` message for equality instead of testing its contents; that assertion was corrected, and all final suites passed. No implementation/test failure remains known. No application server was launched: the new components are plain library code as scoped by the assignment and were composed end-to-end in PostgreSQL-backed integration tests.

## Limitations and next increment

- The integration stack runs in-process and uses a test-only `ActiveStateCoordinator`; it does not implement full distributed active-cache materialization or convergence. Successful Mneme writes are marked degraded to communicate that limit.
- The Themis API is enforced at both boundaries, but a production IU/Status policy and its deployment remain outstanding; the test policy does not supply production authorization.
- Only a local PostgreSQL transaction is verified. Required cross-site durability, HA/DR, failover behavior and a real uncertain-COMMIT event remain unverified. A real production deployment must use governed migrations; the bundled schema initializer is development-only.
- Task-issued, Solution Architect disposition and report-production timestamps were not exposed. Their timestamps and filename prefixes remain explicitly unresolved in the Task and Report; HCRL.004's outstanding review obligations and the missing iteration-005 HCRL/layout-check matter remain open under HCRL.006 DR-11.
- Proposed next increment: establish and test production Themis policies and the deployment-owned database migration/durability profile, then implement the separately governed active-cache convergence work. Do not treat this report or HCRL.006 as Solution Architect acceptance.