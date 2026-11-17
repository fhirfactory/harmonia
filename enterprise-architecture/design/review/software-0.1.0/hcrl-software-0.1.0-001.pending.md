<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Whole-of-Calliope Model Restructure (Backlog Item 00001 & Design Note) | PENDING REVIEW | Full implementation of Backlog Item 00001, Version 001 conforming to DesignNote-Calliope-Initial-Model-Architecture.md submitted for Solution Architect acceptance review. |
| [DR-2](#dr-2) | Canonical 13-Package Functional Hierarchy (`model.core.*`) | PENDING REVIEW | Established the 13 functional packages specified in DesignNote-Calliope-Initial-Model-Architecture.md; eliminated all historical and ad-hoc packages without shims. |
| [DR-3](#dr-3) | Constituent Data Types, Value Sets, and Builders | PENDING REVIEW | Extracted TopicEntry, TopicMaskEntryPattern, MaskType, TopicMaskType; enforced duplicate sequence position rejection without corruption. |
| [DR-4](#dr-4) | Solution-Wide Consumer Migration & ArchUnit Synchronization | PENDING REVIEW | Migrated Agora, Pylai, Energeia, Hestia, Iris, and Paradeigma; synchronized ArchUnit tests while preserving all underlying architectural invariants. |
| [DR-5](#dr-5) | 500-Line Source File Limit and Conformance | PENDING REVIEW | Modularized ErgonPayload, Pragma, InformationUnit, PraxisDefinition, OperationalMetadata, ContainerMetadata, PersistenceOperationEnvelope; 100% of all 72 Calliope files under 500 lines. |
| [DR-6](#dr-6) | Software Release Baseline 0.1.0 Candidate Submission | PENDING REVIEW | Candidate submitted for initial software release baseline 0.1.0; Architecture baseline remains 1.1.26. |

# HCRL software-0.1.0 / 001 — Calliope Model Restructure Candidate Submission

**Iteration status: ACTIVE — PENDING REVIEW.**

Submitted by Implementation Agent **Junie** on `2026-11-16T08:30:00Z` (amended `2026-11-17T07:45:00Z`) for Solution Architect acceptance review under the [Software Development Praxis](../../governance/software-development-praxis.md).

- **Target Software Release:** `0.1.0`
- **Accepted Architecture Baseline:** `1.1.26` (Architecture Continuum remains unchanged)
- **Originating Backlog Item:** [BacklogItem-00001-Calliope-Model-Restructure.001.md](../../../docs/backlog/BacklogItem-00001-Calliope-Model-Restructure.001.md)
- **Authoritative Design Note:** [DesignNote-Calliope-Initial-Model-Architecture.md](../../../docs/design/DesignNote-Calliope-Initial-Model-Architecture.md)
- **Task Details:** [20261116T055100Z-calliope-model-restructure-task.md](../../evidence/software-0.1.0/001/before/ai-agent-tasks/20261116T055100Z-calliope-model-restructure-task.md)
- **Implementation Report:** [20261116T083000Z-calliope-model-restructure-report.md](../../evidence/software-0.1.0/001/after/ai-agent-reports/20261116T083000Z-calliope-model-restructure-report.md)

---

## Decision Review Details

### DR-1: Whole-of-Calliope Model Restructure (Backlog Item 00001 & Design Note)

- **Context:** Backlog Item 00001 Version 001 mandated a comprehensive, whole-of-Calliope software-engineering restructuring to implement the accepted Architecture Continuum, adhere to the Harmonia Implementation Guide, decouple naming from transport routing, modularize constituent data types and builders, and eliminate oversized classes. The Architecture Team subsequently established the authoritative package hierarchy in `docs/design/DesignNote-Calliope-Initial-Model-Architecture.md`.
- **Implemented Scope:** Restructured the entire Calliope Java model into the 13 canonical packages specified by the Design Note, eliminated all historical and compatibility packages, decomposed monolithic classes into collaborating objects, migrated all repository consumers, synchronized ArchUnit test suites, and verified the integrated candidate against the full architecture and regression test suites.
- **Disposition Requested:** Acceptance of the implementation candidate as satisfying Backlog Item 00001, Version 001 and `DesignNote-Calliope-Initial-Model-Architecture.md`.

### DR-2: Canonical 13-Package Functional Hierarchy (`model.core.*`)

- **Context:** Historical Calliope implementations packaged classes ad-hoc or by consuming component names (e.g. `model.pragma`, `model.ergon`, `model.praxis`, `model.petasos`, `model.governedwrite`, `model.topic`), violating separation of concerns and leaking runtime component names into Calliope.
- **Implemented Scope:** Implemented the 13 domain/functional packages established by the Design Note:
  1. `net.fhirfactory.harmonia.model.core.naming`: pure `Topic`, `TopicMask`, `TopicSubscription` (transport fields stripped).
  2. `net.fhirfactory.harmonia.model.core.information.access`: unified point access ports (`GovernedAccess`, `GovernedReader`, `GovernedWriter`, `WriteResult`, `ActiveStateCoordinator`, `ActiveStateToken`, `ActiveStateConvergencePort`).
  3. `net.fhirfactory.harmonia.model.core.information.entity`: durable Information Unit entities (`InformationUnit`, `InformationRepresentation`, `InformationMetadata`, `ContainerMetadata`, `OperationalMetadata`, `Content`, `ContentMetadata`, `OperationalRevision`).
  4. `net.fhirfactory.harmonia.model.core.information.operations`: working cache operational context (`ManagementContext`).
  5. `net.fhirfactory.harmonia.model.core.information.persistence`: persistence command envelopes (`PersistenceOperationEnvelope`, `PersistenceOperationType`).
  6. `net.fhirfactory.harmonia.model.core.workflow.orchestration`: pathway definitions (`PraxisDefinition`, `PraxisDefinitionBuilder`, `PraxisDefinitionMatcher`).
  7. `net.fhirfactory.harmonia.model.core.workflow.tasks`: discrete task instances and activity models (`Pragma`, `ErgonPayload`, `ErgonReasonEnum`, `PragmaCheckpoint`).
  8. `net.fhirfactory.harmonia.model.core.domain.fhir`: FHIR R5 converters and canonical resource alignment ports (`ErgonPayloadFhirConverter`, `PragmaFhirConverter`, `ResourceAlignmentPort`, `AlignmentAssessment`).
  9. `net.fhirfactory.harmonia.model.core.message.entity`: message lifecycle events (`ErgonEvent`).
  10. `net.fhirfactory.harmonia.model.core.message.routing`: messaging infrastructure definitions (`PetasosQueueDefinition`).
  11. `net.fhirfactory.harmonia.model.core.security`: model security labels and tags (`FhirSecurityTagManager`, `HarmoniaAuthorityEnum`, `HarmoniaRoleEnum`, `ErgonSecurityDefinition`).
  12. `net.fhirfactory.harmonia.model.core.observability`: module status models (`ModuleStatus`).
  13. `net.fhirfactory.harmonia.logging`: Invariant 7 diagnostic logging (`PhiLogger`).
  Deleted all historical packages: `model.topic`, `model.information`, `model.governedwrite`, `model.persistence`, `model.petasos`, `model.praxis`, `model.pragma`, `model.ergon`, `model.alignment`.
- **Disposition Requested:** Acceptance of the canonical 13-package functional hierarchy.

### DR-3: Constituent Data Types, Value Sets, and Builders

- **Context:** `Topic.Entry` and `TopicMask.EntryPattern` were inner classes tightly coupled to parent classes. In addition, existing builders overwrote positions on duplicate entry insertion, corrupting builder state.
- **Implemented Scope:**
  - Extracted immutable `TopicEntry` and `TopicMaskEntryPattern` into `net.fhirfactory.harmonia.model.core.naming.datatypes`.
  - Extracted `MaskType` and `TopicMaskType` enums into `net.fhirfactory.harmonia.model.core.naming.valuesets`.
  - Implemented `TopicBuilder`, `TopicFactory`, `TopicMaskBuilder`, and `TopicMaskFactory` in `net.fhirfactory.harmonia.model.core.naming.factory`.
  - Enforced defensive validation in `TopicBuilder`: rejects duplicate sequence positions with `IllegalArgumentException` without state corruption; rejects gaps with `IllegalStateException` on `build()`.
- **Disposition Requested:** Acceptance of the constituent data objects, value sets, and builder validation semantics.

### DR-4: Solution-Wide Consumer Migration & ArchUnit Synchronization

- **Context:** Restructuring Calliope into the new functional hierarchy and removing obsolete packages required migrating all dependent modules across the repository and updating ArchUnit test suites that previously encoded obsolete package paths.
- **Implemented Scope:**
  - Migrated `calliope` internal models and test suites.
  - Migrated `agora` (`AgoraTopics`, `AgoraSecurityUtils`, and unit tests).
  - Migrated `pylai` (`MllpDestinationRegistry`, `TaskEventProducerService`, result types, Camel processors, modular builders, REST resources, and integration tests).
  - Migrated `energeia` (`ErgonBase`, `AdtDistributionErgon`, `OrmRoutingErgon`, `PatientDemographicsUpdateErgon`, `PatientIdentityUpdateErgon`, `AbstractProviderRegistryChangeErgon`, `TaskEventMessageProcessor`, `PetasosQueueToExchangeConduit`, `PragmaWorkflowDispatcher`, `ArtemisPonosProducer`, `TaskQueueProducerService`, `PraxisImplementation`, `TaskSequenceDefaultSeeder`, `TaskSequenceLoader`, `PraxisService`, and all pipeline tests).
  - Migrated `hestia` (`ClinicalResourceWriter`, `ClinicalResourceReader`, `ActiveStateCoordinator`, `WriteResult`, and coordination tests).
  - Migrated `iris` (`TaskSequenceCacheService` and tests).
  - Migrated `paradeigma` (`ThemisDefenceInDepthAcceptanceTest`).
  - Synchronized ArchUnit architecture tests (`GovernedWriteContractArchitectureTest`, `GovernedWriteCompositionArchitectureTest`, `SecurityEnforcementArchitectureTest`) to assert the new package contracts under `model.core.*` while preserving all underlying architectural invariants.
- **Disposition Requested:** Acceptance of the solution-wide consumer migration and ArchUnit synchronization.

### DR-5: 500-Line Source File Limit and Conformance

- **Context:** The Harmonia Implementation Guide establishes a strict 500-physical-line ceiling per Java source file. Seven critical classes in Calliope previously exceeded this limit (some reaching over 950 lines).
- **Implemented Scope:**
  - `ErgonPayload`: reduced from 953 to 498 lines by extracting `ErgonPayloadConstants`, `ErgonPayloadFhirConverter`, and `ErgonPayloadFactory`.
  - `ContainerMetadata`: reduced from 608 to 495 lines by extracting `ContainerMetadataBuilder`.
  - `Pragma`: reduced from 875 to 494 lines by extracting `PragmaBuilder`, `PragmaSecurityContextResolver`, `PragmaFhirConstants`, `PragmaFhirCheckpointConverter`, and `PragmaFhirSecurityConverter`.
  - `InformationUnit`: reduced from 856 to 483 lines by extracting `InformationUnitBuilder`, `InformationUnitFactory`, and `InformationUnitValidator`.
  - `PraxisDefinition`: reduced from 880 to 478 lines by extracting `PraxisDefinitionBuilder` and `PraxisDefinitionMatcher`.
  - `OperationalMetadata`: reduced from 649 to 453 lines by extracting `OperationalMetadataBuilder`.
  - `PersistenceOperationEnvelope`: reduced from 636 to 433 lines by extracting `PersistenceOperationEnvelopeBuilder` and `PersistenceOperationEnvelopeFactory`.
  - Confirmed that **100% of all 72 Java source files in Calliope strictly comply with the 500-line requirement (maximum: 498 lines)**.
- **Disposition Requested:** Confirmation of compliance with the 500-line source file standard.

### DR-6: Software Release Baseline 0.1.0 Candidate Submission

- **Context:** The Software Development Praxis (§2 & §6) governs software release baselines using independent solution-wide semantic versioning `X.Y.Z`.
- **Implemented Scope:** The integrated candidate represents the initial software baseline `0.1.0`. All architecture tests (90 tests across 36 modules), Calliope tests (234 tests), Agora tests (15 tests), Pylai ingress/egress tests (144 tests), Energeia workflow tests (84 tests), Hestia tests (89 tests), Iris BEFE tests (173 tests), and full reactor compilation (50 modules) pass with zero failures or errors.
- **Disposition Requested:** Solution Architect review and disposition for acceptance of software release candidate `0.1.0`.
