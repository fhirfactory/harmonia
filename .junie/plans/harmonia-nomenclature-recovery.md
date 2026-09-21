---
sessionId: session-260921-154043-u5qs
---

# Requirements

### Overview & Goals
A previous session working on the Harmonia/HIE nomenclature convergence was interrupted during `mvn test` due to extensive `com.tngtech.archunit.core.importer` logging. This plan establishes the recovery baseline, formalizes the completed and remaining nomenclature changes, and defines the verification steps.

### Scope
- **In Scope**:
  - Verification of working-tree changes across Docker Compose, Kubernetes manifests, Ansible playbooks, Iris UI SPAs, and backend modules (`calliope`, `energeia`, `hestia`, `petasos`, `pylai`).
  - Creation of test logging configuration (`logback-test.xml`) in `paradeigma/paradeigma-test` to suppress verbose ArchUnit bytecode importer trace logging.
  - Execution and validation of all 25 ArchUnit architecture tests and the 30-module Maven test suite.
- **Out of Scope (Preserved)**:
  - Preserving `@Table(name = "hie_operations_resources")` in `OperationResourceEntity.java` to prevent database schema mismatch.
  - Preserving legitimate domain usages of "Health Information Exchange (HIE)" / "Health Integration Environment (HIE)".
  - Any weakening or disabling of ArchUnit architecture rules.

### Completed Changes
1. **Docker Compose & Deployment Identities**:
   - `docker-compose.yml`: services `hie-operations-jpa-server-1/2` renamed to `operations-1/2`; network `hie-network` renamed to `harmonia-network`; all container names unified as `harmonia-*`.
   - `infinispan-1/2`: internal endpoints updated to `http://operations-1:8080/api/operations` and `http://operations-2:8080/api/operations`.
   - Kubernetes manifests and Ansible `verify.yml` updated to match `operations-1/2`.
2. **Module and Application Class Renames**:
   - `hestia/hie-operations-cli` renamed to `hestia/mnemosyne-operations-cli`.
   - `HieOperationsJpaApplication.java` renamed to `MnemosyneOperationsJpaApplication.java`.
   - `HieWorkflowCliMain.java` renamed to `WorkflowCliMain.java`.
3. **Iris UI & Frontend Branding**:
   - npm packages renamed to `@harmonia/iris-administration`, `@harmonia/iris-clinical`, `@harmonia/iris-console`.
   - HTML titles and component branding updated to Harmonia terminology.
4. **Camel Exchange Headers & FHIR System URIs**:
   - `ErgonBase.java`: `HIE_*` constants migrated to `HARMONIA_*` with backwards-compatible fallback resolution.
   - `calliope` and `pylai`: URIs migrated to `http://fhirfactory.net/harmonia/*` with backwards-compatible alias handling in `ErgonReasonEnum`.

### Acceptance Criteria
- All 25 architecture tests in `paradeigma-test` pass cleanly.
- Full Maven reactor build (`mvn test`) passes cleanly across all 30 submodules without hangs or verbose TRACE spam.
- Docker Compose, Kubernetes, and Ansible definitions cleanly reference `operations-1`/`operations-2` and `harmonia-network`.
- Volume persistence is preserved across all database and messaging containers.

# Technical Design

### Current Implementation & Recovery State
The working tree contains 38 modified files, 17 deleted files (superseded legacy classes/modules), and 7 untracked files/directories representing new implementations and integration tests.

### Root Cause of Test Runner Slowdown
- During `mvn test` in `paradeigma-test`, ArchUnit imports bytecode for the entire repository to execute architecture assertions.
- Because `paradeigma/paradeigma-test` lacked a local `logback-test.xml`, Logback initialized with default unconstrained console logging.
- `com.tngtech.archunit.core.importer.ClassFileImporter` emitted millions of characters of DEBUG/TRACE logging directly to `stdout`, causing severe terminal and test-runner I/O contention.
- The execution was not hung (no deadlock or infinite loop).

### Key Decisions
1. **Targeted Test Logging Configuration**:
   - Add `paradeigma/paradeigma-test/src/test/resources/logback-test.xml` with root level `INFO` and `com.tngtech.archunit` set to `INFO`.
   - *Rationale*: Solves test slowdown at the source without altering production code, build dependencies, or architecture rules.
2. **Preserve Relational Table Name**:
   - Keep `@Table(name = "hie_operations_resources")` in `OperationResourceEntity.java`.
   - *Rationale*: Avoids breaking active database instances without requiring schema migration scripts.
3. **Preserve Volume Safety**:
   - Keep named volumes (`postgres_data_1/2`, `postgres_ops_data_1/2`, `petasos_data`).
   - *Rationale*: Guarantees data persistence across restarts and service renames.

### Architecture Invariant Compliance
- **Paradeigma Isolation**: Zero production dependencies or imports of `paradeigma` (asserted by `ParadeigmaIsolationArchitectureTest`).
- **Petasos API Encapsulation**: Pure Java abstractions with zero JMS/ActiveMQ leakage (asserted by `PetasosApiIsolationArchitectureTest`).
- **Iris Presentation Decoupling**: Zero JPA/PostgreSQL imports in `iris` (asserted by `IrisDecouplingArchitectureTest`).
- **Default-Deny Security**: Zero-PHI logging and Themis authorization contracts preserved.

```mermaid
graph LR
    subgraph Infrastructure [Deployment & Persistence]
        DB[harmonia-postgres-ops-1/2] --> OPS[operations-1/2]
        VOL[(postgres_ops_data_1/2)] -.-> DB
    end
    subgraph Caching [Mneme In-Memory Grid]
        OPS -->|REST Store| INF[infinispan-1/2]
    end
    subgraph Presentation [Iris Tier]
        INF --> BEFE[harmonia-befe]
        BEFE --> SPAS[@harmonia/iris-*]
    end
```

# Testing

### Validation Approach
Verification proceeds in two focused stages:

1. **Architecture Test Suite Verification**:
   - Verify that all ArchUnit rules execute quickly and pass with suppressed TRACE logging.
   - Command:
     ```bash
     mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
     ```
   - Target checks:
     - `ParadeigmaIsolationArchitectureTest` (4 checks)
     - `PetasosApiIsolationArchitectureTest` (3 checks)
     - `PackageLayeringArchitectureTest` (3 checks)
     - `SecurityEnforcementArchitectureTest` (2 checks)
     - `AgoraIsolationArchitectureTest` (7 checks)
     - `ProviderRegistryArchitectureTest` (2 checks)
     - `IrisDecouplingArchitectureTest` (4 checks)

2. **Full Repository Reactor Test**:
   - Verify compilation, unit tests, and integration tests across all 30 submodules.
   - Command:
     ```bash
     mvn test
     ```
   - Expected outcome: `BUILD SUCCESS` across all 30 reactor projects.

# Delivery Steps

### ✓ Step 1: Configure ArchUnit Test Logging
Add `src/test/resources/logback-test.xml` in `paradeigma/paradeigma-test` to configure Logback and suppress verbose `com.tngtech.archunit` importer trace logging.

- Create `paradeigma/paradeigma-test/src/test/resources/logback-test.xml`.
- Configure a standard `CONSOLE` appender with root level set to `INFO`.
- Explicitly configure `<logger name="com.tngtech.archunit" level="INFO" />` to prevent class-importer traversal logs from saturating console output.
- Retain all existing architecture rules, tests, and production code without modification.

### ✓ Step 2: Execute Architecture Tests & Full Reactor Verification
Run the automated ArchUnit architecture tests and the full Maven reactor test suite to verify end-to-end convergence and build health.

- Run architecture test suite: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.
- Verify all 25 architecture rules pass (Paradeigma isolation, Petasos API isolation, Agora isolation, Package layering, Security enforcement, Provider Registry, Iris decoupling).
- Run full reactor build: `mvn test`.
- Confirm all 30 modules compile and test cleanly with zero failures and zero uncommitted unintended changes.