<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia — Implementation Report: Calliope Model Restructure

- **Report Identifier:** `Report-Calliope-Model-Restructure-001`
- **Report-Produced-At:** `2026-11-16T08:30:00Z`
- **Task Identifier:** `Task-Calliope-Model-Restructure-001`
- **Task-Issued-At:** `2026-11-16T05:51:00Z`
- **Implementation Agent:** `Junie`
- **Target Software Release:** `0.1.0`
- **Accepted Architecture Baseline:** `1.1.26`
- **Backlog Item:** `BacklogItem-00001-Calliope-Model-Restructure.001.md`
- **Backlog Item ID:** `00001`
- **Backlog Item Version:** `001`

---

## 1. Implementation Summary

This engineering activity implements in full **Backlog Item 00001, Version 001 — Calliope Model Restructure**.

The Calliope subproject (`calliope`) is Harmonia's pure domain model and schema library, representing core information and naming models defined in the Architecture Continuum. Prior to this restructuring, Calliope suffered from several structural and architectural defects:
1. Misplaced naming and identity abstractions: `Topic`, `TopicMask`, `TopicSubscription`, and associated types were situated in an ad-hoc top-level package (`net.fhirfactory.harmonia.model.topic`) rather than alongside their peer `core.information` package under `net.fhirfactory.harmonia.model.core.naming`.
2. Misplaced transport and routing responsibilities: `Topic` contained transport routing fields (`source`, `target`, `origin`, `destination`, `receivedDate`) and convenience constructors conflating message egress coordinates with pure classification and identity.
3. Lack of modular constituent data objects and builders: `Topic.Entry` and `TopicMask.EntryPattern` were inner classes tightly coupled to their parent classes, and builders lacked validation to reject duplicate sequence positions without state corruption.
4. Monolithic source files exceeding the Harmonia Implementation Guide ceiling: `ErgonPayload` (953 lines), `Pragma` (875 lines), `InformationUnit` (856 lines), `PraxisDefinition` (880 lines), `OperationalMetadata` (649 lines), `ContainerMetadata` (608 lines), and `PersistenceOperationEnvelope` (636 lines) exceeded the mandatory 500-physical-line limit.

To resolve these defects:
- Created the canonical package hierarchy:
  - `net.fhirfactory.harmonia.model.core.naming` (pure `Topic`, `TopicMask`, `TopicSubscription`)
  - `net.fhirfactory.harmonia.model.core.naming.datatypes` (`TopicEntry`, `TopicMaskEntryPattern`)
  - `net.fhirfactory.harmonia.model.core.naming.valuesets` (`MaskType`, `TopicMaskType`)
  - `net.fhirfactory.harmonia.model.core.naming.factory` (`TopicBuilder`, `TopicFactory`, `TopicMaskBuilder`, `TopicMaskFactory`)
- Stripped transport and routing fields from `Topic`.
- Hardened `TopicBuilder` and `TopicMaskBuilder` to strictly validate zero-based continuous sequences and reject duplicate sequence positions without state corruption.
- Modularized large classes across Calliope into cohesive collaborating objects, builders, and converters, bringing **100% of all Java source files in Calliope strictly under the 500-line limit**.
- Migrated all consumers across Calliope, Agora, Pylai, Energeia, Iris, and Paradeigma to the canonical naming model.
- Removed the obsolete legacy package `net.fhirfactory.harmonia.model.topic`.

---

## 2. Object-Oriented Design

### 2.1 Package Hierarchy and Responsibilities

```text
calliope/src/main/java/net/fhirfactory/harmonia/model/
├── core/
│   ├── naming/
│   │   ├── Topic.java                          (Pure naming & identity, 385 lines)
│   │   ├── TopicMask.java                      (Selector & pattern filter, 287 lines)
│   │   ├── TopicSubscription.java              (Topic filtering criteria, 322 lines)
│   │   ├── datatypes/
│   │   │   ├── TopicEntry.java                 (Immutable canonical name entry, 138 lines)
│   │   │   └── TopicMaskEntryPattern.java      (Sequenced entry pattern, 150 lines)
│   │   ├── valuesets/
│   │   │   ├── MaskType.java                   (Exact vs Inclusive depth, 41 lines)
│   │   │   └── TopicMaskType.java              (Name-based vs Canonical-based, 42 lines)
│   │   └── factory/
│   │       ├── TopicBuilder.java               (Strictly validating Topic builder, 160 lines)
│   │       ├── TopicFactory.java               (Standard domain topic creator, 177 lines)
│   │       ├── TopicMaskBuilder.java           (Validating TopicMask builder, 134 lines)
│   │       └── TopicMaskFactory.java           (Standard mask pattern creator, 109 lines)
│   └── information/
│       ├── InformationUnit.java                (Core managed information unit, 482 lines)
│       ├── ContainerMetadata.java              (Container metadata envelope, 495 lines)
│       ├── OperationalMetadata.java            (Operational & audit metadata, 453 lines)
│       └── factory/
│           ├── InformationUnitBuilder.java     (Decomposed builder, 229 lines)
│           ├── InformationUnitFactory.java     (Creation helpers, 57 lines)
│           ├── InformationUnitValidator.java   (Invariant validation, 53 lines)
│           ├── ContainerMetadataBuilder.java   (Decomposed builder, 137 lines)
│           └── OperationalMetadataBuilder.java (Decomposed builder, 265 lines)
├── ergon/
│   ├── ErgonPayload.java                       (Encapsulated payload, 498 lines)
│   ├── ErgonPayloadConstants.java              (Extension URLs & type systems, 43 lines)
│   ├── ErgonPayloadFhirConverter.java          (Bi-directional FHIR Task converter, 384 lines)
│   └── ErgonPayloadFactory.java                (Creation helpers, 104 lines)
├── pragma/
│   ├── Pragma.java                             (Workflow execution envelope, 494 lines)
│   ├── PragmaBuilder.java                      (Decomposed builder, 279 lines)
│   ├── PragmaSecurityContextResolver.java      (Themis security context resolver, 70 lines)
│   ├── PragmaFhirConstants.java                (FHIR mapping constants & URLs, 57 lines)
│   ├── PragmaFhirCheckpointConverter.java      (Checkpoint conversion helper, 113 lines)
│   └── PragmaFhirSecurityConverter.java        (Security extension converter, 174 lines)
├── persistence/
│   ├── PersistenceOperationEnvelope.java       (Persistence command envelope, 434 lines)
│   ├── PersistenceOperationEnvelopeBuilder.java(Decomposed builder, 199 lines)
│   └── PersistenceOperationEnvelopeFactory.java(Security bridging factory, 131 lines)
└── praxis/
    ├── PraxisDefinition.java                   (Task sequence definition, 479 lines)
    ├── PraxisDefinitionBuilder.java            (Decomposed builder, 166 lines)
    └── PraxisDefinitionMatcher.java            (Topic subscription & trigger matcher, 103 lines)
```

### 2.2 Significant Design Decisions

1. **Constituent Data Objects (`TopicEntry`, `TopicMaskEntryPattern`):**
   Extracted as first-class immutable types in `datatypes`. Each encapsulates its own validation logic (e.g. non-blank strings, regex syntax compilation) and serialization annotations.
2. **Defensive Builder Invariants (`TopicBuilder`):**
   `TopicBuilder` verifies non-negative sequence positions and rejects duplicate insertions immediately (`IllegalArgumentException`), preserving the existing map state intact without corruption. Upon `build()`, it validates that entries start at zero and form a continuous sequence with no gaps (`IllegalStateException`).
3. **Decoupling Transport Routing from Naming:**
   Removed all routing fields (`source`, `target`, `origin`, `destination`, `receivedDate`) from `Topic`. Inbound gateways and messaging services (`IncomingAdtMessageProcessor`, `TaskEventProducerService`) now track gateway instances and facilities directly on `ErgonEvent` or message headers.
4. **Single Responsibility Decomposition:**
   Decomposed large monolithic classes by extracting FHIR Task component converters, security context resolvers, and checkpoint annotators into cohesive collaborating classes.

---

## 3. Architecture Conformance

- **Architecture Continuum Alignment:** Conforms to the accepted Continuum naming model (`Harmonia-Information-Topic-Classification`) where Topics represent pure hierarchical classification independent of transport or messaging envelopes.
- **Paradeigma Production Isolation (Invariant 1):** Conforms; zero dependencies or imports of Paradeigma in production code.
- **Petasos API Abstraction (Invariant 2):** Conforms; pure Java abstractions preserved with zero JMS leakage into `petasos-api`.
- **Default-Deny Security (Invariant 6):** Conforms; all security context propagation uses immutable `ThemisSecurityContext` structures.
- **Mneme / Mnemosyne State Separation (Invariant 8):** Conforms; no raw cache or persistence exposure.
- **External Publication Boundary (Invariant 9):** Conforms; Pylai projection remains non-destructive.
- **Architecture Baseline Standing:** Architecture release remains accepted baseline `1.1.26`. This assignment does not modify the Architecture Continuum.

---

## 4. Consumer Migration

All consumers across Harmonia were migrated to `net.fhirfactory.harmonia.model.core.naming.Topic`:

1. **`calliope`:**
   Updated all core information metadata models (`ContentMetadata`, `ContainerMetadata`, `InformationCoordinates`, `InformationItinerary`, `InformationMetadata`, `InformationSecurityContext`, `InformationUnit`, `InformationUnitIdentifier`, `InformationUnitMask`, `InformationUnitMetadata`, `InformationUnitRelationship`, `LifecycleContext`, `OperationalMetadata`, `OperationalMetadataMask`, `ErgonEvent`, `ErgonPayload`, `Pragma`, `ProviderRegistryChangePragma`, `PraxisDefinition`).
2. **`agora` (`agora-api`):**
   Migrated `AgoraTopics` and `AgoraTopicConstantsTest` to construct pure Topics without transport routing metadata.
3. **`pylai` (`pylai-mllp-base`, `pylai-mllp-in`, `pylai-mllp-out`):**
   Migrated `MllpDestinationRegistry`, `TaskEventProducerService`, `AdtProcessingResult`, `MfnProcessingResult`, `OrmProcessingResult`, `OruProcessingResult`, `OutboundMllpRequest`, modular resource builders, and all Camel route processors. Destination resolution now evaluates Topic qualifiers and friendly names.
4. **`energeia` (`energeia-erga`, `energeia-ponos`, `energeia-praxis`):**
   Migrated `ErgonBase`, `AdtDistributionErgon`, `OrmRoutingErgon`, `PatientDemographicsUpdateErgon`, `PatientIdentityUpdateErgon`, `AbstractProviderRegistryChangeErgon`, `TaskEventMessageProcessor`, `PetasosQueueToExchangeConduit`, `PragmaWorkflowDispatcher`, `ArtemisPonosProducer`, `TaskQueueProducerService`, `PraxisImplementation`, and `TaskSequenceDefaultSeeder`.
5. **`iris` (`iris-befe`):**
   Migrated `TaskSequenceCacheService` and its test suite.
6. **`paradeigma` (`paradeigma-test`):**
   Migrated `ThemisDefenceInDepthAcceptanceTest`.

---

## 5. Verification Results

A comprehensive verification programme was executed against the integrated solution:

1. **Paradeigma Architecture Tests (AGENTS.md §4 & §5):**
   - Command: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`
   - Scope: 36 reactor modules, 11 architecture test classes, 90 tests.
   - Result: **PASS** (90 tests run, 0 failures, 0 errors, 0 skipped).
2. **Calliope Component & Naming Unit Tests:**
   - Command: `mvn test -pl calliope`
   - Scope: All naming, information, ergon, pragma, persistence, and praxis unit tests.
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
   - Command: `mvn test -pl energeia/ponos,energeia/praxis`
   - Scope: End-to-end message flow, pipeline dispatch, and task sequence caching.
   - Result: **PASS** (56 tests run, 0 failures, 0 errors, 0 skipped).

---

## 6. Implementation Guide Conformance

- **500-Line Physical Limit:** Confirmed. Maximum file size in Calliope is 498 lines (`ErgonPayload.java`). All 54 Java files strictly comply.
- **Defensive Programming (95/5 Philosophy):** Comprehensive null-checks, blank-string guards, immutable map copying (`Collections.unmodifiableSortedMap`), and continuous sequence validation ensure robust unhappy-path handling.
- **Educational Documentation:** Full Javadoc on all public classes, methods, and builders detailing architectural responsibilities and error behaviors.

---

## 7. Outstanding Matters

None. No unresolved defects, exceptions, or architecture conflicts remain.

---

## 8. Candidate Submission

- **Candidate Git State:** Clean unstaged/staged candidate on working branch.
- **Target Software Release:** `0.1.0`
- **Applicable HCRL:** `enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-001.pending.md`
- **Submission Status:** Ready for Solution Architect review and acceptance under the Software Development Praxis.
