<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Whole-of-Calliope Model Restructure (Backlog Item 00001) | PENDING REVIEW | Full implementation of Backlog Item 00001, Version 001 submitted for Solution Architect acceptance review. |
| [DR-2](#dr-2) | Canonical Naming Package Hierarchy (`core.naming`) | PENDING REVIEW | Established `net.fhirfactory.harmonia.model.core.naming` and subpackages; stripped transport and routing fields from Topic. |
| [DR-3](#dr-3) | Constituent Data Types, Value Sets, and Builders | PENDING REVIEW | Extracted TopicEntry, TopicMaskEntryPattern, MaskType, TopicMaskType; enforced duplicate sequence position rejection without corruption. |
| [DR-4](#dr-4) | Solution-Wide Consumer Migration | PENDING REVIEW | Migrated Agora, Pylai (base, in, out), Energeia (erga, ponos, praxis), Iris, and Paradeigma; removed obsolete `model.topic`. |
| [DR-5](#dr-5) | 500-Line Source File Limit and Conformance | PENDING REVIEW | Modularized ErgonPayload, Pragma, InformationUnit, PraxisDefinition, OperationalMetadata, ContainerMetadata, PersistenceOperationEnvelope; 100% of Calliope files under 500 lines. |
| [DR-6](#dr-6) | Software Release Baseline 0.1.0 Candidate Submission | PENDING REVIEW | Candidate submitted for initial software release baseline 0.1.0; Architecture baseline remains 1.1.26. |

# HCRL software-0.1.0 / 001 — Calliope Model Restructure Candidate Submission

**Iteration status: ACTIVE — PENDING REVIEW.**

Submitted by Implementation Agent **Junie** on `2026-11-16T08:30:00Z` for Solution Architect acceptance review under the [Software Development Praxis](../../governance/software-development-praxis.md).

- **Target Software Release:** `0.1.0`
- **Accepted Architecture Baseline:** `1.1.26` (Architecture Continuum remains unchanged)
- **Originating Backlog Item:** [BacklogItem-00001-Calliope-Model-Restructure.001.md](../../../docs/backlog/BacklogItem-00001-Calliope-Model-Restructure.001.md)
- **Task Details:** [20261116T055100Z-calliope-model-restructure-task.md](../../evidence/software-0.1.0/001/before/ai-agent-tasks/20261116T055100Z-calliope-model-restructure-task.md)
- **Implementation Report:** [20261116T083000Z-calliope-model-restructure-report.md](../../evidence/software-0.1.0/001/after/ai-agent-reports/20261116T083000Z-calliope-model-restructure-report.md)

---

## Decision Review Details

### DR-1: Whole-of-Calliope Model Restructure (Backlog Item 00001)

- **Context:** Backlog Item 00001 Version 001 mandated a comprehensive, whole-of-Calliope software-engineering restructuring to implement the accepted Architecture Continuum, adhere to the Harmonia Implementation Guide, decouple naming from transport routing, modularize constituent data types and builders, and eliminate oversized classes.
- **Implemented Scope:** Restructured the entire Calliope Java model, created cohesive packages under `net.fhirfactory.harmonia.model.core.naming`, decomposed monolithic classes into collaborating objects, migrated all repository consumers, and verified the integrated candidate against the full architecture and regression test suites.
- **Disposition Requested:** Acceptance of the implementation candidate as satisfying Backlog Item 00001, Version 001.

### DR-2: Canonical Naming Package Hierarchy (`core.naming`)

- **Context:** The Architecture Continuum establishes Topic and TopicMask as core naming and classification constructs, peer to core information units. Placing them in `net.fhirfactory.harmonia.model.topic` was an ad-hoc historical expedient. Furthermore, `Topic` contained transport routing fields (`source`, `target`, `origin`, `destination`, `receivedDate`) that violated the Continuum's separation of naming from transport envelopes.
- **Implemented Scope:** Created `net.fhirfactory.harmonia.model.core.naming` containing pure `Topic`, `TopicMask`, and `TopicSubscription`. Stripped all routing fields from `Topic`. Gateway and routing coordinates are now tracked on operational envelopes (`ErgonEvent`, `Pragma`) and message headers. Deleted the obsolete `net.fhirfactory.harmonia.model.topic` package.
- **Disposition Requested:** Acceptance of the `net.fhirfactory.harmonia.model.core.naming` package hierarchy and pure naming model.

### DR-3: Constituent Data Types, Value Sets, and Builders

- **Context:** `Topic.Entry` and `TopicMask.EntryPattern` were inner classes tightly coupled to parent classes. In addition, existing builders overwrote positions on duplicate entry insertion, corrupting builder state.
- **Implemented Scope:**
  - Extracted immutable `TopicEntry` and `TopicMaskEntryPattern` into `net.fhirfactory.harmonia.model.core.naming.datatypes`.
  - Extracted `MaskType` and `TopicMaskType` enums into `net.fhirfactory.harmonia.model.core.naming.valuesets`.
  - Implemented `TopicBuilder`, `TopicFactory`, `TopicMaskBuilder`, and `TopicMaskFactory` in `net.fhirfactory.harmonia.model.core.naming.factory`.
  - Enforced defensive validation in `TopicBuilder`: rejects duplicate sequence positions with `IllegalArgumentException` without state corruption; rejects gaps with `IllegalStateException` on `build()`.
- **Disposition Requested:** Acceptance of the constituent data objects, value sets, and builder validation semantics.

### DR-4: Solution-Wide Consumer Migration

- **Context:** Removing the obsolete `net.fhirfactory.harmonia.model.topic` package and purifying `Topic` required migrating all dependent modules across the repository.
- **Implemented Scope:**
  - Migrated `calliope` internal models (`InformationUnit`, `ErgonPayload`, `Pragma`, `ProviderRegistryChangePragma`, `PraxisDefinition`, etc.).
  - Migrated `agora` (`AgoraTopics` and unit tests).
  - Migrated `pylai` (`MllpDestinationRegistry`, `TaskEventProducerService`, result types, Camel processors, modular builders, REST resources, and integration tests). Destination resolution now evaluates Topic qualifiers and friendly names.
  - Migrated `energeia` (`ErgonBase`, `AdtDistributionErgon`, `OrmRoutingErgon`, `PatientDemographicsUpdateErgon`, `PatientIdentityUpdateErgon`, `AbstractProviderRegistryChangeErgon`, `TaskEventMessageProcessor`, `PetasosQueueToExchangeConduit`, `PragmaWorkflowDispatcher`, `ArtemisPonosProducer`, `TaskQueueProducerService`, `PraxisImplementation`, `TaskSequenceDefaultSeeder`, and all pipeline tests).
  - Migrated `iris` (`TaskSequenceCacheService` and tests).
  - Migrated `paradeigma` (`ThemisDefenceInDepthAcceptanceTest`).
- **Disposition Requested:** Acceptance of the consumer migration across all affected modules.

### DR-5: 500-Line Source File Limit and Conformance

- **Context:** The Harmonia Implementation Guide establishes a strict 500-physical-line ceiling per Java source file. Seven critical classes in Calliope previously exceeded this limit (some reaching over 950 lines).
- **Implemented Scope:**
  - `ErgonPayload`: reduced from 953 to 498 lines by extracting `ErgonPayloadConstants`, `ErgonPayloadFhirConverter`, and `ErgonPayloadFactory`.
  - `ContainerMetadata`: reduced from 608 to 495 lines by extracting `ContainerMetadataBuilder`.
  - `Pragma`: reduced from 875 to 494 lines by extracting `PragmaBuilder`, `PragmaSecurityContextResolver`, `PragmaFhirConstants`, `PragmaFhirCheckpointConverter`, and `PragmaFhirSecurityConverter`.
  - `InformationUnit`: reduced from 856 to 482 lines by extracting `InformationUnitBuilder`, `InformationUnitFactory`, and `InformationUnitValidator`.
  - `PraxisDefinition`: reduced from 880 to 479 lines by extracting `PraxisDefinitionBuilder` and `PraxisDefinitionMatcher`.
  - `OperationalMetadata`: reduced from 649 to 453 lines by extracting `OperationalMetadataBuilder`.
  - `PersistenceOperationEnvelope`: reduced from 636 to 434 lines by extracting `PersistenceOperationEnvelopeBuilder` and `PersistenceOperationEnvelopeFactory`.
  - Confirmed that **100% of all 54 Java source files in Calliope strictly comply with the 500-line requirement**.
- **Disposition Requested:** Confirmation of compliance with the 500-line source file standard.

### DR-6: Software Release Baseline 0.1.0 Candidate Submission

- **Context:** The Software Development Praxis (§2 & §6) governs software release baselines using independent solution-wide semantic versioning `X.Y.Z`.
- **Implemented Scope:** The integrated candidate represents the first accepted software baseline `0.1.0`. All architecture tests (90 tests across 36 modules), Calliope tests (234 tests), Agora tests (15 tests), Pylai ingress/egress tests (144 tests), and Energeia workflow tests (56 tests) pass with zero failures or errors.
- **Disposition Requested:** Solution Architect review and disposition for acceptance of software release candidate `0.1.0`.
