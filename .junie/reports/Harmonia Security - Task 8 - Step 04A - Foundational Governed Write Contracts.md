<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->

# Harmonia Security - Task 8 - Step 04A - Foundational Governed Write Contracts

## Executive Summary & System Utility

To maximize platform reliability, clinical data safety, and developer efficiency across the Harmonia Health Integration Environment (HIE), Task 08 Step 08.04A establishes the pure Java foundational contract layer for Harmonia's governed-write architecture. By formalizing clear, type-safe, and misuse-resistant caller-facing abstractions prior to implementing runtime persistence coordination, this design eliminates race conditions, lost updates, and ambiguous failure states across all integrating components (Pylai gateways, Energeia workflow activities, Iris presentation services, and Hestia storage tiers).

The foundational contract layer strictly enforces the five core architectural invariants of the Strong Hybrid persistence model:
1. **Separation of Coordination vs Authority**: Opaque in-memory coordination tokens (`ActiveCoordinationToken`) represent transient cache state only and confer zero permission to bypass durable conditional checks (`ExpectedAuthoritativeVersion`).
2. **Monotonic Authoritative Progression**: Mnemosyne (PostgreSQL) is the sole authority for state versioning; updates require atomic verification of predecessor versions.
3. **Guarded Convergence**: Successful commits converge back into Mneme via CAS loops with newer-version preservation.
4. **Commit Visibility over Degraded Convergence**: An authoritative commit with degraded cache convergence is reported as a successful commit (`WriteResult.Committed` with `ConvergenceStatus.DEGRADED` and `isCommitted() == true`), preventing spurious duplicate writes.
5. **Strict UPDATE-Only Lifecycle (ADR-020)**: Physical DELETE methods are strictly prohibited across caller-facing interfaces.

---

## Package Placement & Dependency Rationale

### Placement in `calliope` (`net.fhirfactory.harmonia.model.governedwrite`)

The foundational contract types are housed in the `calliope` model library under package `net.fhirfactory.harmonia.model.governedwrite`:

```
net.fhirfactory.harmonia.model.governedwrite
├── ActiveCoordinationToken.java
├── ActiveStateConflict.java
├── AuthoritativeCommitOutcome.java
├── AuthoritativePreconditionConflict.java
├── AuthoritativeVersion.java
├── ConvergenceStatus.java
├── ExpectedAuthoritativeVersion.java
├── GovernedRead.java
├── GovernedWriter.java
├── PreconditionFailureReason.java
├── ResourceKey.java
└── WriteResult.java
```

### Architectural & Dependency Justification
- **Universal Availability**: `calliope` is Harmonia's canonical model library. All upstream callers—`pylai` gateways, `energeia` workflow engines, `iris` presentation adapters, and `hestia` persistence services—already depend directly on `calliope`.
- **Direct Context Reuse**: `calliope` depends on `themis-api`, enabling direct reuse of `ThemisSecurityContext` for authentication, authorization, correlation, and causation lineage without creating duplicate security models or circular dependencies.
- **Zero Framework Leakage**: The contract layer consists entirely of pure JDK records, final value objects, sealed interfaces, and enums. It maintains **zero** dependencies on Infinispan (`RemoteCache`), JPA/Hibernate (`jakarta.persistence..`, `org.hibernate..`, `ca.uhn.fhir.jpa..`), or HTTP/Servlet web frameworks (`org.springframework.web..`, `jakarta.servlet..`).
- **No Module Proliferation**: Placing the pure contract in `calliope` eliminates the need to introduce new Maven modules (e.g. `hestia-governance` or `hestia-api`), minimizing build overhead and dependency complexity.

---

## Implemented Contract Types & Semantics

### 1. Identity, Token & Read Models

| Contract Type | Kind | Responsibility & Semantics |
| :--- | :--- | :--- |
| `ResourceKey` | `record` | Immutable logical resource identifier `(String resourceType, String id)` with strict non-null and non-blank validation. Exposes `toQualifiedPath()`. |
| `ActiveCoordinationToken` | `final class` | Strongly opaque value object representing a real observed Mneme (Infinispan) cache token. Strictly enforces observed token semantics: contains no `none()` or `isPresent()` magic absence states, exposes no `asOpaqueString()` or numeric accessors (`longValue()`, `intValue()`), does not implement `Comparable`, and unconditionally masks raw internal representation in `toString()` (`ActiveCoordinationToken[opaque]`). |
| `AuthoritativeVersion` | `record` | Value object `(String value)` identifying durable persisted state in Mnemosyne. Provides `toExpected()` to transition into an expected predecessor. |
| `ExpectedAuthoritativeVersion` | `final class` | Strongly typed representation of an expected database predecessor version. Supports expected absence for CREATE via `none()` (`isNone() == true`) and expected predecessor version for UPDATE via `of(AuthoritativeVersion)` / `of(String)` / `of(long)`. |
| `GovernedRead<T>` | `record` | Immutable envelope bundling `ResourceKey`, payload `T`, `ActiveCoordinationToken`, and `AuthoritativeVersion`. Guarantees callers cannot assemble mismatched active and authoritative concurrency contexts. Exposes `expectedAuthoritativeVersion()`. |

### 2. Caller-Facing Writer Interface

```java
package net.fhirfactory.harmonia.model.governedwrite;

import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

public interface GovernedWriter {
    <T> WriteResult<T> create(ResourceKey key, T resource, ThemisSecurityContext securityContext);
    <T> WriteResult<T> update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext);
}
```

- **Operations**: Exposes only `create` and `update`. Physical `delete` or `remove` operations are absent in strict adherence to ADR-020.
- **Precondition Binding**: `update` requires a previously obtained `GovernedRead<T>`, guaranteeing that in-flight active tokens and expected authoritative versions are bound to observed state.
- **Context Integration**: Reuses `ThemisSecurityContext` directly for caller identity, security domain, granted authorities, `correlationId`, and `causationId`.

### 3. Conflict Taxonomy & Outcome Model

| Taxonomy Type | Kind | Responsibility & Semantics |
| :--- | :--- | :--- |
| `AuthoritativeCommitOutcome` | `enum` | Three-state commit outcome: `COMMITTED`, `NOT_COMMITTED`, and `UNKNOWN`. Prevents ambiguous network timeouts from being collapsed into false negatives. |
| `ConvergenceStatus` | `enum` | State of cache convergence following durable commit: `CONVERGED`, `DEGRADED`, and `NOT_APPLICABLE`. |
| `PreconditionFailureReason` | `enum` | Specific database precondition failure reason: `RESOURCE_ALREADY_EXISTS` (CREATE conflict) or `EXPECTED_VERSION_MISMATCH` (UPDATE conflict). |
| `ActiveStateConflict` | `record` | Encapsulates transient in-flight cache contention `(ResourceKey key, String message)`. Fast retry is recommended. |
| `AuthoritativePreconditionConflict` | `record` | Encapsulates durable database precondition mismatch `(ResourceKey key, PreconditionFailureReason reason, ExpectedAuthoritativeVersion expectedVersion, AuthoritativeVersion currentVersion, String message)`. |

### 4. Sealed Result Hierarchy (`WriteResult<T>`)

```java
public sealed interface WriteResult<T> extends Serializable permits
    WriteResult.Committed,
    WriteResult.ActiveConflict,
    WriteResult.AuthoritativeConflict,
    WriteResult.OutcomeUnknown,
    WriteResult.NotCommitted
```

- **`WriteResult.Committed<T>`**: `(ResourceKey key, T resource, AuthoritativeVersion version, ConvergenceStatus convergenceStatus, String degradationMessage)`. Enforces that `commitOutcome() == COMMITTED`, `isCommitted() == true`, and `convergenceStatus` cannot be `NOT_APPLICABLE`. Supports degraded cache convergence (detected via `degradationReason().isPresent()` or `convergenceStatus() == ConvergenceStatus.DEGRADED`).
- **`WriteResult.ActiveConflict<T>`**: `(ResourceKey key, ActiveStateConflict conflict)`. Enforces `commitOutcome() == NOT_COMMITTED` and `convergenceStatus() == NOT_APPLICABLE`.
- **`WriteResult.AuthoritativeConflict<T>`**: `(ResourceKey key, AuthoritativePreconditionConflict conflict)`. Enforces `commitOutcome() == NOT_COMMITTED` and `convergenceStatus() == NOT_APPLICABLE`.
- **`WriteResult.OutcomeUnknown<T>`**: `(ResourceKey key, String message)`. Enforces `commitOutcome() == UNKNOWN`, `isOutcomeUnknown() == true`, and `convergenceStatus() == NOT_APPLICABLE`.
- **`WriteResult.NotCommitted<T>`**: `(ResourceKey key, String reason)`. General uncommitted failure variant with `commitOutcome() == NOT_COMMITTED`.

---

## File Modification & Addition Catalog

| File Path | Subproject / Module | Action | Description |
| :--- | :--- | :--- | :--- |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ResourceKey.java` | `calliope` | Created | Logical resource key record with non-blank validation. |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ActiveCoordinationToken.java` | `calliope` | Created | Strongly opaque token value object representing real observed tokens. |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/AuthoritativeVersion.java` | `calliope` | Created | Authoritative database version record. |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ExpectedAuthoritativeVersion.java` | `calliope` | Created | Predecessor version value object (`none()` vs `of(...)`). |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/GovernedRead.java` | `calliope` | Created | Context envelope bundling payload, active token, and authoritative version. |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/GovernedWriter.java` | `calliope` | Created | Public caller-facing contract declaring `create` and `update`. |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/WriteResult.java` | `calliope` | Created | Sealed result interface with permitted variants and factory methods. |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ActiveStateConflict.java` | `calliope` | Created | Active cache contention conflict record. |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/AuthoritativePreconditionConflict.java` | `calliope` | Created | Authoritative database precondition conflict record. |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/AuthoritativeCommitOutcome.java` | `calliope` | Created | Three-state commit outcome enum (`COMMITTED`, `NOT_COMMITTED`, `UNKNOWN`). |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ConvergenceStatus.java` | `calliope` | Created | Convergence status enum (`CONVERGED`, `DEGRADED`, `NOT_APPLICABLE`). |
| `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/PreconditionFailureReason.java` | `calliope` | Created | Precondition failure reason enum (`RESOURCE_ALREADY_EXISTS`, `EXPECTED_VERSION_MISMATCH`). |
| `calliope/src/test/java/net/fhirfactory/harmonia/model/governedwrite/GovernedWriteContractTest.java` | `calliope` | Created | Unit test suite exercising all contract semantics, token opacity, and invariants. |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteContractArchitectureTest.java` | `paradeigma-test` | Created | ArchUnit guardrail tests enforcing zero Infinispan, JPA, or HTTP dependencies. |
| `docs/design/governed-write-concurrency-contract.md` | `docs` | Updated | Aligned design documentation with implemented types, sealed result hierarchy, and package placement. |

---

## Test Execution & Verification Results

### 1. Developer Experience Unit Tests (`GovernedWriteContractTest`)
- **Location**: `calliope/src/test/java/net/fhirfactory/harmonia/model/governedwrite/GovernedWriteContractTest.java`
- **Command**: `mvn test -pl calliope -Dtest=GovernedWriteContractTest`
- **Results**: `Tests run: 9, Failures: 0, Errors: 0, Skipped: 0` (Execution time: ~0.057s).
- **Coverage**:
  1. `scenario1_contextRetentionInGovernedRead`: Asserts `GovernedRead` bundles key, payload, active token, and authoritative version.
  2. `scenario2_tokenOpacityAndRealTokenSemantics`: Asserts `ActiveCoordinationToken` rejects blank values, masks in `toString()`, is not `Comparable`, and reflectively asserts absence of `none`, `isPresent`, `asOpaqueString`, `increment`, `add`, `plus`, `next`, `compareTo`, `getVersion`, `longValue`, `intValue`.
  3. `scenario3_createConflictSemantics`: Asserts CREATE duplicate yields `AuthoritativeConflict` with `RESOURCE_ALREADY_EXISTS` and `NOT_COMMITTED`.
  4. `scenario4_updateConflictSemantics`: Asserts UPDATE mismatch yields `AuthoritativeConflict` with `EXPECTED_VERSION_MISMATCH` preserving versions.
  5. `scenario5_activeVsAuthoritativeConflictDistinction`: Asserts `ActiveConflict` and `AuthoritativeConflict` are mutually distinct types.
  6. `scenario6_threeStateCommitOutcomes`: Asserts `COMMITTED`, `NOT_COMMITTED`, and `UNKNOWN` are distinct non-overlapping states.
  7. `scenario7_degradedConvergenceRepresentation`: Asserts degraded convergence yields `WriteResult.Committed` with `isCommitted() == true`.
  8. `scenario8_absenceOfGovernedDelete`: Reflectively asserts `GovernedWriter` declares exactly `create` and `update` with zero delete/remove methods.
  9. `scenario9_invalidCombinationRejection`: Asserts constructor null/blank rejections and rejection of `NOT_APPLICABLE` convergence in `Committed`.

### 2. ArchUnit Architecture Guardrail Tests (`GovernedWriteContractArchitectureTest`)
- **Location**: `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteContractArchitectureTest.java`
- **Command**: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="GovernedWriteContractArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`
- **Results**: `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0` (Execution time: ~0.55s).
- **Rules Verified**:
  1. `contractPackage_mustNotDependOnInfinispan`: Asserts zero dependencies on `org.infinispan..`, `org.infinispan.client.hotrod..`, `org.infinispan.commons..`.
  2. `contractPackage_mustNotDependOnPersistenceFrameworks`: Asserts zero dependencies on `jakarta.persistence..`, `javax.persistence..`, `org.hibernate..`, `org.postgresql..`, `ca.uhn.fhir.jpa..`.
  3. `contractPackage_mustNotDependOnHttpWebFrameworks`: Asserts zero dependencies on `org.springframework.web..`, `jakarta.ws.rs..`, `jakarta.servlet..`, `org.apache.http..`, `org.apache.hc..`.
  4. `governedWriter_mustNotDeclareDeleteOrRemoveMethods`: Asserts no methods named `*delete*` or `*remove*` exist in the contract package.
  5. `contractPackage_mustContainZeroForbiddenImportsInSource`: Performs static filesystem analysis verifying no forbidden imports exist in source files.

---

## Compact Developer Usage Example

```java
package com.example.integration;

import net.fhirfactory.harmonia.model.governedwrite.*;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.hl7.fhir.r5.model.Practitioner;

public class PractitionerRegistrationService {

    private final GovernedWriter governedWriter;
    // GovernedReader is a conceptual interface for reading governed state,
    // out-of-scope for the foundational 08.04A contract.
    private final GovernedReader governedReader; 

    public PractitionerRegistrationService(GovernedWriter governedWriter, GovernedReader governedReader) {
        this.governedWriter = governedWriter;
        this.governedReader = governedReader;
    }

    /**
     * Governed CREATE example: Strict uniqueness enforcement.
     */
    public WriteResult<Practitioner> registerNewPractitioner(
            Practitioner practitioner,
            ThemisSecurityContext securityContext) {
        
        ResourceKey key = ResourceKey.of("Practitioner", practitioner.getIdElement().getIdPart());
        WriteResult<Practitioner> result = governedWriter.create(key, practitioner, securityContext);

        if (result instanceof WriteResult.Committed<Practitioner> committed) {
            System.out.println("Created practitioner version: " + committed.version().value());
            return committed;
        } else if (result instanceof WriteResult.AuthoritativeConflict<Practitioner> conflict) {
            System.err.println("Duplicate practitioner rejected: " + conflict.conflict().reason());
            return conflict;
        } else if (result instanceof WriteResult.OutcomeUnknown<Practitioner> unknown) {
            System.err.println("Commit unconfirmed, trigger reconciliation: " + unknown.message());
            return unknown;
        }
        return result;
    }

    /**
     * Governed UPDATE example: Read-modify-write with active contention retry.
     */
    public WriteResult<Practitioner> updatePractitionerTelecom(
            ResourceKey key,
            String phone,
            ThemisSecurityContext securityContext) {

        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            // 1. Obtain current state with active token and authoritative version
            GovernedRead<Practitioner> current = governedReader.read(key)
                    .orElseThrow(() -> new IllegalStateException("Practitioner not found: " + key));

            // 2. Prepare proposed mutation
            Practitioner proposed = current.resource().copy();
            proposed.addTelecom().setValue(phone);

            // 3. Submit governed update with predecessor context bound in GovernedRead
            WriteResult<Practitioner> result = governedWriter.update(current, proposed, securityContext);

            if (result instanceof WriteResult.Committed<Practitioner> committed) {
                if (committed.convergenceStatus() == ConvergenceStatus.DEGRADED) {
                    System.out.println("Committed with degraded cache convergence: " + committed.degradationReason().orElse("None"));
                }
                return committed;
            } else if (result instanceof WriteResult.ActiveConflict<Practitioner>) {
                System.out.println("Cache race contention on attempt " + attempt + ", retrying...");
                continue; // Transparent retry on transient active cache race
            } else if (result instanceof WriteResult.AuthoritativeConflict<Practitioner> authConflict) {
                System.err.println("Precondition mismatch in database: " + authConflict.conflict().reason());
                return authConflict; // Permanent database precondition mismatch
            } else {
                return result;
            }
        }
        return WriteResult.notCommitted(key, "Exceeded retry attempts due to cache contention");
    }
}
```

---

## Explicit Boundaries & Scope Confirmations

To ensure zero ambiguity and preserve project boundaries:
1. **Zero Write Path Migration Confirmed**: No existing production write paths (`FhirCacheService`, `TaskCacheService`, `FhirRestCacheStore`, `FhirStorageService`, or HAPI Resource Providers) were migrated or modified during Step 08.04A. Existing production paths remain untouched until subsequent migration tasks.
2. **Zero Subsystem/Module Introduction Confirmed**: No new Maven modules or deployable subsystems were created. The contract layer resides entirely within `calliope`.
3. **Zero Database Schema or Configuration Changes Confirmed**: No database schemas, Infinispan configuration files, or JPA entity annotations were modified.
4. **Runtime Coordination Deferral Confirmed**: Runtime implementations of Mneme Hot Rod CAS coordination loops and Mnemosyne conditional SQL persistence adapters remain deferred to subsequent runtime implementation tasks.
5. **No Unresolved Questions**: All core contract semantics, token opacity constraints, sealed hierarchies, and architecture rules are fully established, verified, and passing across all suites.
