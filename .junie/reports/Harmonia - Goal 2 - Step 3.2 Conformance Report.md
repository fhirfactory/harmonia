# Goal 2 Step 3.2 Conformance Report

**Status:** COMPLETE  
**Verification date:** 2026-09-29  
**Scope:** Recovered Mnemosyne Clinical authoritative-persistence baseline, PostgreSQL history/concurrency evidence, and architecture closure. No Step 3.3 work was commenced.

## 1. Executive Summary

The recovered Step 3.2 implementation and tests are present and operational. The accepted HAPI FHIR JPA persistence mechanisms were reverified against genuine PostgreSQL Testcontainers, and the full Mnemosyne Clinical and ArchUnit architecture suites passed.

The earlier verification report had doubled its test totals because the reusable runner summed duplicate Maven console summaries. `scripts/test_runner.py` now uses unique Surefire XML reports written during the current invocation as the authoritative source for totals and retains XML-derived failed-test names and messages. The corrected totals in this report are therefore the actual Surefire counts.

No persistence behavior was redesigned, no application-local locking was introduced, and no Step 3.3 transport work was started.

## 2. Recovered Baseline Files Verified

The following files were confirmed present and intact in `hestia/mnemosyne-clinical`:

1. `src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java`
   - Exposes point `read(ResourceKey)`, `create(ResourceKey, T)`, and conditional `update(ResourceKey, T, ExpectedAuthoritativeVersion)` operations.
   - Does not expose JPA/Hibernate entities or physical delete/remove operations.
2. `src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java`
   - Preserves the legacy persistence implementation and point-read behavior without dual-writing.
3. `src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/HapiJpaAuthoritativePersistenceAdapter.java`
   - Implements the accepted authoritative boundary using HAPI `DaoRegistry` and `IFhirResourceDao<?>`.
4. `src/test/java/net/fhirfactory/harmonia/hapifhir/persistence/HapiJpaAuthoritativePersistencePostgreSqlConcurrencyTest.java`
   - Exercises PostgreSQL-backed create collision, update collision, sequential history, point-read, and failure/precondition scenarios.

## 3. Accepted HAPI JPA Operations and Semantics

- **Point read:** `dao.read(new IdType(key.resourceType(), key.id()), requestDetails)`; absent/gone resources map to `NotCommitted`.
- **Create-if-absent:** the proposed resource receives client-assigned ID `new IdType(key.resourceType(), key.id())`; `dao.update(proposedState, requestDetails)` is invoked with `If-None-Match: *`. One concurrent winner establishes authoritative version 1; existing-resource/collision outcomes map to `Conflict(RESOURCE_ALREADY_EXISTS)`.
- **Update-if-expected-predecessor:** the proposed resource receives versioned ID `new IdType(key.resourceType(), key.id(), expectedVerStr)`; `dao.update(proposedState, requestDetails)` is invoked with `If-Match: W/"<expected-version>"`. One concurrent winner advances version 1 to version 2; optimistic/precondition conflicts map to `Conflict(EXPECTED_VERSION_MISMATCH)`.
- **Version mapping:** HAPI's persisted `IdType.getVersionIdPart()` is explicitly parsed into bounded Harmonia `AuthoritativeVersion` values. No identity is established between `AuthoritativeVersion`, FHIR `meta.versionId`, HTTP ETag, or Mneme `ActiveStateToken`.
- **Locking and governance:** the adapter contains no JVM-local lock and does not inject default security labels or other governance metadata.

## 4. PostgreSQL History and Concurrency Evidence

`HapiJpaAuthoritativePersistencePostgreSqlConcurrencyTest` uses Testcontainers with genuine PostgreSQL (`postgres:16-alpine`) and ten concurrent workers for the collision scenarios.

- **Concurrent CREATE:** exactly 1 of 10 writers committed version 1; the other 9 returned `RESOURCE_ALREADY_EXISTS`. SQL assertions found exactly 1 matching `HFJ_RESOURCE` row, current `res_ver = 1`, and `HFJ_RES_VER` history exactly `[1]`.
- **Concurrent UPDATE from predecessor version 1:** exactly 1 of 10 writers committed version 2; the other 9 returned `EXPECTED_VERSION_MISMATCH`. SQL assertions found current `res_ver = 2` and `HFJ_RES_VER` history exactly `[1, 2]`; no version 3 or phantom history record was created.
- **Sequential progression:** committed updates advanced from version 1 through version 5, and SQL history was asserted exactly as `[1, 2, 3, 4, 5]`.
- **Point read and diagnostics:** the winner payload and explicit authoritative version were read back; absent resources and invalid/precondition cases retained the established result algebra.

## 5. Architecture Compliance

The full architecture suite passed all 11 architecture test classes and 84 tests. In particular:

- `MnemosyneAuthoritativePersistenceArchitectureTest` passed all 6 tests, enforcing zero Infinispan/Mneme active-state coupling in the authoritative persistence package, no public port delete/remove exposure, and no JPA/Hibernate entity leakage in the port contract.
- `GovernedWriteCompositionArchitectureTest` passed all 6 tests, preserving governed-write placement/composition and separation from persistence implementation details.
- The remaining architecture classes covering Agora, Iris, Paradeigma, Petasos, Provider Registry, Pylai, package layering, governed-write contracts, and security enforcement also passed.

These results are consistent with the applicable Harmonia guardrails for Mneme/Mnemosyne state separation, ADR-020 physical-delete prohibition, persistence-contract encapsulation, and production isolation. No architectural-axiom conflict was identified.

## 6. Test Execution Record

All counts below are taken from Surefire XML reports generated by the final runner/wrapper executions, not by adding Maven console summaries.

| Verification | Exact command or wrapper phase | Tests | Failures | Errors | Skipped | Duration |
|---|---|---:|---:|---:|---:|---:|
| PostgreSQL concurrency class | `python3 scripts/test_runner.py --process-timeout=180 --stall-timeout=60 test -pl hestia/mnemosyne-clinical -Dtest=HapiJpaAuthoritativePersistencePostgreSqlConcurrencyTest` | 5 | 0 | 0 | 0 | 46s |
| Full Mnemosyne Clinical suite | `python3 scripts/test_runner.py --process-timeout=180 --stall-timeout=60 test -pl hestia/mnemosyne-clinical` (also run as Phase 1 of the final wrapper) | 81 | 0 | 0 | 0 | 1m17s (final wrapper phase) |
| Full architecture suite | `python3 scripts/test_runner.py --process-timeout=180 --stall-timeout=60 test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` (also run as Phase 2 of the final wrapper) | 84 | 0 | 0 | 0 | 28s (final wrapper phase) |
| Final reusable wrapper | `./scripts/run-step3-verification.sh` | 81 + 84 unique tests | 0 | 0 | 0 | approximately 1m45s |

The 5-test concurrency class is included in the 81-test Mnemosyne Clinical total and is not added again to the 165 unique tests represented by the two full-suite phases.

## 7. Verification Tooling Correction

`scripts/test_runner.py` was corrected so that it:

- snapshots existing `target/surefire-reports/TEST-*.xml` mtimes before Maven starts;
- aggregates only reports newly written by the current invocation;
- uses each unique Surefire XML report's `tests`, `failures`, `errors`, and `skipped` attributes as totals; and
- continues to extract and deduplicate failed test names and error messages from the XML reports.

This prevents duplicate Maven summary lines from inflating the conformance evidence.

## 8. Closure

- Goal 2 Step 3.2 is **COMPLETE**.
- The accepted HAPI JPA/PostgreSQL authoritative persistence baseline is verified without persistence redesign.
- Native HAPI history progression and zero-phantom-history assertions are present and passing.
- Full Mnemosyne Clinical and architecture suites pass with the corrected counts: **81** and **84**, respectively; targeted concurrency class: **5**.
- No physical DELETE operation, Petasos/Artemis messaging, Pylai gateway change, application-local lock, security-label mutation, or Step 3.3 internal HTTP authoritative transport was introduced.