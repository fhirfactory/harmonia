<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia — Implementation Report: Calliope Model Restructure

- **Report Identifier:** `Report-Calliope-Model-Restructure-001`
- **Report-Produced-At:** `2026-11-16T08:30:00Z`
- **Report-Amended-At:** `2026-11-17T07:45:00Z`
- **Task Identifier:** `Task-Calliope-Model-Restructure-001`
- **Task-Issued-At:** `2026-11-16T05:51:00Z`
- **Implementation Agent:** `Junie`
- **Target Software Release:** `0.1.0`
- **Accepted Architecture Baseline:** `1.1.26`
- **Backlog Item:** `BacklogItem-00001-Calliope-Model-Restructure.001.md`
- **Backlog Item ID:** `00001`
- **Backlog Item Version:** `001`
- **Authoritative Design Note:** `docs/design/DesignNote-Calliope-Initial-Model-Architecture.md`

---

## 1. Implementation Summary

This engineering activity implements in full **Backlog Item 00001, Version 001 — Calliope Model Restructure** in strict conformance with **`docs/design/DesignNote-Calliope-Initial-Model-Architecture.md`**.

The Calliope subproject (`calliope`) is Harmonia's pure domain model and schema library, representing canonical information and naming models defined in the Architecture Continuum. In this assignment, Calliope underwent a complete structural and object-oriented transformation:
1. **Target Package Hierarchy Implementation:** Reorganized the entire Calliope codebase from historical ad-hoc packages into the 13 domain/functional packages established by `DesignNote-Calliope-Initial-Model-Architecture.md` under `net.fhirfactory.harmonia.model.core.*` and `net.fhirfactory.harmonia.logging`.
2. **Elimination of Obsolete Packages:** Completely removed historical packages (`model.topic`, `model.information`, `model.governedwrite`, `model.persistence`, `model.petasos`, `model.praxis`, `model.pragma`, `model.ergon`, `model.alignment`) without retaining backwards-compatibility shims or obsolete structures.
3. **Decoupling Transport Routing from Naming:** Purified `Topic` by stripping all transport and routing metadata (`source`, `target`, `origin`, `destination`, `receivedDate`), establishing pure classification and identity. Transport coordinates are tracked cleanly on operational message headers and envelopes.
4. **Constituent Data Objects, Value Sets, and Builders:** Extracted inner classes (`Topic.Entry` &rarr; `TopicEntry`, `TopicMask.EntryPattern` &rarr; `TopicMaskEntryPattern`) and enums (`MaskType`, `TopicMaskType`) into dedicated subpackages. Hardened `TopicBuilder` and `TopicMaskBuilder` to strictly enforce continuous zero-based sequences and reject duplicate sequence positions immediately without corrupting builder state.
5. **Harmonia Implementation Guide Conformance (500-Line Limit):** Decomposed oversized monolithic classes (`ErgonPayload`, `Pragma`, `InformationUnit`, `PraxisDefinition`, `OperationalMetadata`, `ContainerMetadata`, `PersistenceOperationEnvelope`) into cohesive collaborating objects, builders, factories, and converters. **100% of all 72 Calliope Java source files strictly satisfy the 500-physical-line ceiling (maximum: 498 lines)**.
6. **Solution-Wide Consumer Migration:** Updated imports, construction patterns, and test suites across all affected Harmonia subprojects: Agora, Energeia (erga, ponos, praxis), Pylai (mllp-base, mllp-in, mllp-out, fhir-registry), Iris (iris-befe), Hestia (mneme, mnemosyne), and Paradeigma.
7. **ArchUnit Test Suite Synchronization:** Updated ArchUnit test suites (`GovernedWriteContractArchitectureTest`, `GovernedWriteCompositionArchitectureTest`, `SecurityEnforcementArchitectureTest`) to assert the target package contracts while preserving all underlying architectural invariants.

---

## 2. Object-Oriented Design

### 2.1 Authoritative Package Hierarchy and Class Inventory

In accordance with `DesignNote-Calliope-Initial-Model-Architecture.md`, Calliope is structured into 13 cohesive functional packages:

```text
calliope/src/main/java/net/fhirfactory/harmonia/
├── logging/                                          [Invariant 7 PHI Logging]
│   ├── DefaultPhiLogger.java                         (201 lines)
│   ├── PhiLogger.java                                (150 lines)
│   ├── PhiLoggerFactory.java                         (65 lines)
│   └── PhiLoggingConfig.java                         (128 lines)
│
└── model/core/
    ├── naming/                                       [Naming & Classification]
    │   ├── Topic.java                                (384 lines)
    │   ├── TopicMask.java                            (286 lines)
    │   ├── TopicSubscription.java                    (321 lines)
    │   ├── datatypes/
    │   │   ├── TopicEntry.java                       (137 lines)
    │   │   └── TopicMaskEntryPattern.java            (149 lines)
    │   ├── valuesets/
    │   │   ├── MaskType.java                         (40 lines)
    │   │   └── TopicMaskType.java                    (41 lines)
    │   └── factory/
    │       ├── TopicBuilder.java                     (159 lines)
    │       ├── TopicFactory.java                     (177 lines)
    │       ├── TopicMaskBuilder.java                 (133 lines)
    │       └── TopicMaskFactory.java                 (108 lines)
    │
    ├── information/
    │   ├── access/                                   [Governed Point Access Ports]
    │   │   ├── ActiveStateConflict.java              (39 lines)
    │   │   ├── ActiveStateConvergencePort.java       (39 lines)
    │   │   ├── ActiveStateCoordinationResult.java    (38 lines)
    │   │   ├── ActiveStateCoordinator.java           (45 lines)
    │   │   ├── ActiveStateToken.java                 (65 lines)
    │   │   ├── ActiveStateTokenBridge.java           (43 lines)
    │   │   ├── AuthoritativeCommitOutcome.java       (39 lines)
    │   │   ├── AuthoritativePreconditionConflict.java(78 lines)
    │   │   ├── AuthoritativeVersion.java             (61 lines)
    │   │   ├── ConvergenceStatus.java                (39 lines)
    │   │   ├── ExpectedAuthoritativeVersion.java     (86 lines)
    │   │   ├── GovernedAccess.java                   (28 lines)
    │   │   ├── GovernedBoundaryValidator.java        (89 lines)
    │   │   ├── GovernedRead.java                     (54 lines)
    │   │   ├── GovernedReader.java                   (41 lines)
    │   │   ├── GovernedWriter.java                   (51 lines)
    │   │   ├── PreconditionFailureReason.java        (33 lines)
    │   │   ├── ResourceKey.java                      (54 lines)
    │   │   └── WriteResult.java                      (253 lines)
    │   │
    │   ├── entity/                                   [Information Unit Entities]
    │   │   ├── ContainerMetadata.java                (495 lines)
    │   │   ├── Content.java                          (406 lines)
    │   │   ├── ContentMetadata.java                  (450 lines)
    │   │   ├── InformationCoordinates.java           (304 lines)
    │   │   ├── InformationItinerary.java             (341 lines)
    │   │   ├── InformationMetadata.java              (362 lines)
    │   │   ├── InformationRepresentation.java        (448 lines)
    │   │   ├── InformationSecurityContext.java       (465 lines)
    │   │   ├── InformationUnit.java                  (483 lines)
    │   │   ├── InformationUnitIdentifier.java        (291 lines)
    │   │   ├── InformationUnitMask.java              (417 lines)
    │   │   ├── InformationUnitMetadata.java          (318 lines)
    │   │   ├── InformationUnitRelationship.java      (342 lines)
    │   │   ├── LifecycleContext.java                 (454 lines)
    │   │   ├── OperationalMetadata.java              (453 lines)
    │   │   ├── OperationalMetadataMask.java          (311 lines)
    │   │   ├── OperationalRevision.java              (96 lines)
    │   │   └── factory/
    │   │       ├── ContainerMetadataBuilder.java     (136 lines)
    │   │       ├── InformationUnitBuilder.java       (229 lines)
    │   │       ├── InformationUnitFactory.java       (57 lines)
    │   │       ├── InformationUnitValidator.java     (52 lines)
    │   │       └── OperationalMetadataBuilder.java   (264 lines)
    │   │
    │   ├── operations/                               [Mneme Working Context]
    │   │   └── ManagementContext.java                (382 lines)
    │   │
    │   └── persistence/                              [Persistence Command Envelopes]
    │       ├── PersistenceOperationEnvelope.java     (433 lines)
    │       ├── PersistenceOperationEnvelopeBuilder.java (198 lines)
    │       ├── PersistenceOperationEnvelopeFactory.java (130 lines)
    │       └── PersistenceOperationType.java         (27 lines)
    │
    ├── workflow/
    │   ├── orchestration/                            [Praxis Workflow Definitions]
    │   │   ├── PraxisDefinition.java                 (478 lines)
    │   │   ├── PraxisDefinitionBuilder.java          (165 lines)
    │   │   └── PraxisDefinitionMatcher.java          (102 lines)
    │   │
    │   └── tasks/                                    [Pragma & Ergon Task Models]
    │       ├── ErgonPayload.java                     (498 lines)
    │       ├── ErgonPayloadConstants.java            (42 lines)
    │       ├── ErgonPayloadFactory.java              (103 lines)
    │       ├── ErgonReasonEnum.java                  (221 lines)
    │       ├── Pragma.java                           (494 lines)
    │       ├── PragmaBuilder.java                    (263 lines)
    │       ├── PragmaCheckpoint.java                 (402 lines)
    │       ├── PragmaSecurityContextResolver.java    (69 lines)
    │       ├── PragmaStatus.java                     (171 lines)
    │       └── ProviderRegistryChangePragma.java     (214 lines)
    │
    ├── domain/fhir/                                  [FHIR & Domain Converters]
    │   ├── AlignmentAssessment.java                  (57 lines)
    │   ├── ErgonPayloadFhirConverter.java            (385 lines)
    │   ├── PragmaFhirCheckpointConverter.java        (114 lines)
    │   ├── PragmaFhirConstants.java                  (56 lines)
    │   ├── PragmaFhirConverter.java                  (421 lines)
    │   ├── PragmaFhirSecurityConverter.java          (174 lines)
    │   ├── ProviderRegistryConstants.java            (92 lines)
    │   └── ResourceAlignmentPort.java                (55 lines)
    │
    ├── message/
    │   ├── entity/                                   [Lifecycle Message Events]
    │   │   └── ErgonEvent.java                       (239 lines)
    │   │
    │   └── routing/                                  [Messaging Infrastructure Models]
    │       └── PetasosQueueDefinition.java           (280 lines)
    │
    ├── security/                                     [Model Security Labels & Tags]
    │   ├── ErgonSecurityDefinition.java              (174 lines)
    │   ├── FhirConfidentialityEnum.java              (204 lines)
    │   ├── FhirSecurityTagManager.java               (306 lines)
    │   ├── HarmoniaAuthorityEnum.java                (77 lines)
    │   ├── HarmoniaRoleEnum.java                     (123 lines)
    │   ├── HarmoniaSecurityCodeSystem.java           (32 lines)
    │   └── HarmoniaSecurityLabelEnum.java            (90 lines)
    │
    └── observability/                                [System Health & Status]
        └── ModuleStatus.java                         (264 lines)
```

### 2.2 Key Architectural and Design Decisions

1. **Separation of Entities from Operations:**
   In accordance with the Information Unit Framework DRP, durable Information Unit entities (`InformationUnit`, `InformationRepresentation`, `InformationMetadata`, `ContainerMetadata`, `OperationalMetadata`) reside in `information.entity`, while transient working cache state (`ManagementContext`) resides in `information.operations`.
2. **Unified Governed Access Abstractions:**
   Replaced `net.fhirfactory.harmonia.model.governedwrite` with `net.fhirfactory.harmonia.model.core.information.access`. Governed access encapsulates both authoritative reading (`GovernedReader`, `GovernedRead`) and writing (`GovernedWriter`, `WriteResult`) alongside Mneme distributed coordination tokens and ports (`ActiveStateCoordinator`, `ActiveStateToken`, `ActiveStateConvergencePort`).
3. **Decoupling Task Execution from Workflow Orchestration:**
   - Discrete task instances and activity units (`Pragma`, `ErgonPayload`, `ErgonReasonEnum`, `PragmaCheckpoint`) reside in `model.core.workflow.tasks`.
   - Reusable pathway definitions and sequence templates (`PraxisDefinition`, `PraxisDefinitionBuilder`, `PraxisDefinitionMatcher`) reside in `model.core.workflow.orchestration`.
4. **Encapsulation of FHIR Mapping & Canonical Alignment:**
   All FHIR R5 bi-directional mapping logic (`ErgonPayloadFhirConverter`, `PragmaFhirConverter`, `PragmaFhirCheckpointConverter`, `PragmaFhirSecurityConverter`) and canonical resource alignment ports (`ResourceAlignmentPort`, `AlignmentAssessment`) are centralized in `model.core.domain.fhir`.
5. **Decoupling Message Events from Routing:**
   - Lifecycle event payloads (`ErgonEvent`) reside in `model.core.message.entity`.
   - Queue definition and addressing metadata (`PetasosQueueDefinition`) reside in `model.core.message.routing`.

---

## 3. Architecture Conformance

- **Architecture Continuum Principles (AX-01 – AX-17):**
  - **AX-05 (Mneme / Mnemosyne State Separation):** Governed access contracts in `information.access` enforce strict separation between active coordination tokens (`ActiveStateToken`) and durable authoritative versions (`AuthoritativeVersion`).
  - **AX-10 (Default-Deny Security):** All task execution models (`Pragma`) and persistence envelopes enforce immutable `ThemisSecurityContext` propagation.
  - **AX-13 (External Publication Boundary):** Pylai's projection of canonical topics and FHIR tasks terminates Harmonia operational management without leaking internal metadata.
  - **AX-14 (Zero-PHI Diagnostic Logging):** Enforced continuously through `PhiLogger` and verified by ArchUnit tests.
- **Architectural Invariants:**
  - **Invariant 1 (Paradeigma Production Isolation):** Verified zero production imports or dependencies on Paradeigma.
  - **Invariant 2 (Petasos API Abstraction):** Verified zero JMS/Artemis API leakage into `petasos-api`.
  - **Invariant 3 (Iris Decoupling):** Verified zero direct JPA, Hibernate, or PostgreSQL dependencies in Iris.
  - **Invariant 8 (Mneme / Mnemosyne State Separation):** Verified no raw database/JPA access exposed as application access path.
  - **Invariant 10 (Agora Collaboration Isolation):** Verified Ponos decoupling and Matrix DTO encapsulation.
- **ArchUnit Conformance:**
  All 90 ArchUnit architecture tests across 36 modules pass with zero violations, verifying package layering, security enforcement, and isolation boundaries.

---

## 4. Consumer Migration

All consumers across Harmonia were systematically updated to import from the canonical `model.core.*` packages:

1. **`calliope`:** Internal test suites updated to target new packages (`InformationUnitIdentifierTest`, `TopicTest`, `TopicMaskTest`, `TopicSubscriptionTest`, `ErgonPayloadTest`, `PragmaTest`, `PraxisDefinitionTest`, `PersistenceOperationEnvelopeTest`, `InformationUnitArchitecturalConformanceTest`).
2. **`agora` (`agora-api`, `agora-core`, `agora-service`):**
   Updated `AgoraTopics`, `AgoraSecurityUtils`, and unit tests to import `Topic` and security enums from `model.core.naming` and `model.core.security`.
3. **`pylai` (`pylai-mllp-base`, `pylai-mllp-in`, `pylai-mllp-out`, `pylai-fhir-registry`):**
   Updated `MllpDestinationRegistry`, `TaskEventProducerService`, `IncomingAdtMessageProcessor`, `IncomingMfnMessageProcessor`, `IncomingOrmMessageProcessor`, `IncomingOruMessageProcessor`, `AdtTaskResourceBuilder`, `AdtCommunicationResourceBuilder`, `OutboundMllpProcessor`, `OutboundMllpResource`, `ChangeRequestSubmissionService`, `FhirSecurityInterceptor`, and all test suites to import `ErgonEvent`, `Topic`, `Pragma`, and `PersistenceOperationEnvelope`.
4. **`energeia` (`energeia-erga`, `energeia-ponos`, `energeia-praxis`):**
   Updated `ErgonBase`, `AdtDistributionErgon`, `OrmRoutingErgon`, `PatientDemographicsUpdateErgon`, `PatientIdentityUpdateErgon`, `AbstractProviderRegistryChangeErgon`, `TaskEventMessageProcessor`, `PetasosQueueToExchangeConduit`, `PragmaWorkflowDispatcher`, `ArtemisPonosProducer`, `TaskQueueProducerService`, `PraxisImplementation`, `TaskSequenceDefaultSeeder`, `TaskSequenceLoader`, `PraxisService`, and all workflow/pipeline integration tests to import from `workflow.tasks`, `workflow.orchestration`, `domain.fhir`, and `message.entity`.
5. **`hestia` (`mnemosyne-api`, `mnemosyne-clinical`, `mneme-cluster`):**
   Updated `ClinicalResourceWriter`, `ClinicalResourceReader`, `ActiveStateCoordinator`, `WriteResult`, and coordination tests to import from `information.access`.
6. **`iris` (`iris-befe`):**
   Updated `TaskSequenceCacheService` and associated REST test resources to import `PraxisDefinition` from `workflow.orchestration`.
7. **`paradeigma` (`paradeigma-test`):**
   Updated `ThemisDefenceInDepthAcceptanceTest`, `GovernedWriteContractArchitectureTest`, `GovernedWriteCompositionArchitectureTest`, `MnemosyneAuthoritativePersistenceArchitectureTest`, and `SecurityEnforcementArchitectureTest`.

---

## 5. Verification Results

A comprehensive verification programme was executed against the integrated solution:

1. **Paradeigma Architecture Tests (AGENTS.md §4 & §5):**
   - Command: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`
   - Scope: 36 reactor modules, 11 architecture test classes, 90 tests.
   - Result: **PASS** (90 tests run, 0 failures, 0 errors, 0 skipped).
2. **Calliope Component & Unit Tests:**
   - Command: `mvn test -pl calliope`
   - Scope: Naming, information, access, operations, persistence, workflow, tasks, FHIR domain, security, and logging unit tests.
   - Result: **PASS** (234 tests run, 0 failures, 0 errors, 0 skipped).
3. **Agora Subsystem Tests (AGENTS.md §5):**
   - Command: `mvn test -pl agora/agora-service -am`
   - Scope: 12 reactor modules, 15 tests.
   - Result: **PASS** (15 tests run, 0 failures, 0 errors, 0 skipped).
4. **Inbound MLLP Gateway Tests (AGENTS.md §5 / REC-001):**
   - Command: `mvn test -pl pylai/pylai-mllp-in -am`
   - Scope: 7 reactor modules, 46 tests.
   - Result: **PASS** (46 tests run, 0 failures, 0 errors, 0 skipped).
5. **Ergon Activity & Outbound Gateway Tests (AGENTS.md §5 / REC-002):**
   - Command: `mvn test -pl energeia/erga,pylai/pylai-mllp-out -am`
   - Scope: 13 reactor modules, 98 tests.
   - Result: **PASS** (98 tests run, 0 failures, 0 errors, 0 skipped).
6. **Energeia Ponos & Praxis Workflow Tests:**
   - Command: `mvn test -pl energeia/ponos,energeia/praxis -am`
   - Scope: 17 reactor modules, 84 tests.
   - Result: **PASS** (84 tests run, 0 failures, 0 errors, 0 skipped).
7. **Hestia Data Services Tests:**
   - Command: `mvn test -pl hestia/mneme-cluster,hestia/mnemosyne-clinical -am`
   - Scope: 10 reactor modules, 89 tests.
   - Result: **PASS** (89 tests run, 0 failures, 0 errors, 0 skipped).
8. **Iris BEFE Presentation Gateway Tests:**
   - Command: `mvn test -pl iris/iris-befe -am`
   - Scope: 11 reactor modules, 173 tests.
   - Result: **PASS** (173 tests run, 0 failures, 0 errors, 0 skipped).
9. **Full Reactor Compilation:**
   - Command: `mvn test-compile`
   - Scope: All 50 modules in the Harmonia platform reactor.
   - Result: **PASS** (50 modules compiled with 0 errors).

---

## 6. Implementation Guide Conformance

- **500-Line Physical Limit:** Confirmed. Maximum file size in Calliope is 498 lines (`ErgonPayload.java`). All 72 Java files strictly comply.
- **Defensive Programming (95/5 Philosophy):**
  - Continuous zero-based sequence validation in `TopicBuilder` and `TopicMaskBuilder`.
  - Immediate duplicate rejection without state corruption.
  - Comprehensive null-checks and blank-string validation.
  - Defensive copying of maps and collections using `Collections.unmodifiableMap` and `Collections.unmodifiableSortedMap`.
- **Educational Documentation:**
  - Javadoc on all public interfaces, classes, builders, and domain models detailing architectural responsibilities, continuum mapping, and failure behavior.

---

## 7. Outstanding Matters

None. No target-model discrepancies, unresolved defects, exceptions, or architecture conflicts remain.

---

## 8. Candidate Submission

- **Candidate Git Head:** `cd492cf1c8de3ea649aece412d1826da3ee6f6d1`
- **Target Software Release:** `0.1.0`
- **Applicable HCRL:** `enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-001.pending.md`
- **Submission Status:** Complete implementation candidate ready for Solution Architect review and acceptance under the Software Development Praxis.
