<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Note — Calliope Initial Model Architecture

## 1. Purpose

Define the target Java package hierarchy for Harmonia's shared core information models and interfaces. The hierarchy provides a reference for organising model objects; detailed object definitions and contracts are developed separately.

## 2. Design Principles

Establish a standardised **function- and domain-oriented package hierarchy**. Packages group related information models or interfaces by cohesive functional responsibilities or domain associations, rather than by historical implementation locations or the names of consuming components.

## 3. Provenance

This reference model was developed by the Harmonia Architecture Team, drawing on its collective software engineering and architectural experience.

## 4. Target Information Model Package Hierarchy

**Base package:** `net.fhirfactory.harmonia.model.core`

All package paths in Section 4 are relative to this base.

### 4.1 Overview

The consolidated hierarchy, with the currently identified principal objects, is:

```text
net.fhirfactory.harmonia.model.core
├── naming
│   ├── Topic
│   ├── HumanName
│   └── OrganisationName
├── information
│   ├── entity
│   │   ├── InformationUnit
│   │   ├── InformationUnitSummary  [TBD: separate object?]
│   │   └── LifecycleContext
│   ├── access
│   ├── persistence
│   └── operations
│       └── ManagementContext
├── workflow
│   ├── tasks
│   │   ├── Pragma
│   │   └── Behaviour
│   ├── engines
│   ├── orchestration
│   │   └── PraxisDefinition
│   └── data
│       └── PackedPragma           [candidate; semantics TBD]
├── message
│   ├── routing
│   │   ├── PetasosRoute
│   │   ├── PetasosConsumer
│   │   └── PetasosProvider
│   └── entity
│       └── PetasosParcel
├── security
├── audit
├── logging
├── collaboration
│   ├── space
│   │   ├── Ward
│   │   ├── Campus
│   │   ├── Department
│   │   └── Specialty
│   ├── role
│   │   ├── PractitionerRole
│   │   └── Wardsperson
│   ├── twin
│   │   ├── Silhouette
│   │   ├── Agent
│   │   ├── TaskRequest
│   │   └── TaskResponse
│   └── team
│       ├── CareTeam
│       ├── OnCall
│       └── OnDuty
├── domain
│   ├── fhir
│   ├── hl7v2
│   └── matrix
└── observability
    ├── ComponentCoordinate
    ├── ComponentStatus
    ├── ComponentActivity
    ├── ComponentLog
    ├── ComponentTask
    └── ComponentAudit
```

The hierarchy is the target organisational reference; named objects are initial allocations. The list does not imply that supporting types, factories, builders or value sets are exhaustive. `datatypes`, `valuesets`, `factory` and other subpackages may be used where responsibilities warrant them.

### 4.2 Core Hierarchy Branches

#### 4.2.1 `naming`

Principal objects: `Topic`, `HumanName`, `OrganisationName`.

Existing related objects requiring allocation during detailed design: `TopicMask`, `TopicSubscription` and associated supporting types.

#### 4.2.2 `information.entity`

Principal objects: `InformationUnit`, `LifecycleContext`.

`InformationUnitSummary` is **TBD** as a separate Java object. Summary operations are expected to expose identity, names, references and metadata without Content; this does not yet prescribe a distinct summary class.

#### 4.2.3 `information.access`

Shared models associated with governed information access, including read, write and search requests, criteria and outcomes. Java service interfaces belong in the interface hierarchy where applicable.

#### 4.2.4 `information.persistence`

Shared persistence models, including persistence operations, authoritative versioning, concurrency and durability outcomes. Candidate existing objects include `PersistenceOperationEnvelope` and `PersistenceOperationType`.

#### 4.2.5 `workflow.tasks`

Principal task models: `Pragma`, `Behaviour`. Other task forms and associated status/checkpoint types remain to be allocated.

#### 4.2.6 `workflow.engines`

Shared models for processing-engine **configuration, status, reporting and state management that must be visible to other components or systems**. Applicable across general, Digital Twin and assurance processing engines.

This package does not contain processing-engine implementations or private execution logic.

#### 4.2.7 `workflow.orchestration`

Models for workflow coordination, definitions and sequencing. Initial object: `PraxisDefinition`.

#### 4.2.8 `message.routing`

Principal objects: `PetasosRoute`, `PetasosConsumer`, `PetasosProvider`.

#### 4.2.9 `message.entity`

Principal object: `PetasosParcel`.

`ErgonEvent` is an existing implementation object whose purpose and place in the target model are **unresolved**; its continued existence is not assumed.

#### 4.2.10 `security`

Shared security information models and value sets. Precise object allocations remain to be established.

#### 4.2.11 `audit`

Shared audit information models, including AuditEvent-related representations as applicable.

#### 4.2.12 `logging`

Shared logging information models, including LogEvent-related representations as applicable.

#### 4.2.13 `collaboration.space`

Principal objects: `Ward`, `Campus`, `Department`, `Specialty`.

#### 4.2.14 `collaboration.role`

Principal objects: `PractitionerRole`, `Wardsperson`; additional roles TBD.

#### 4.2.15 `collaboration.twin`

Principal objects: `Silhouette`, `Agent`, `TaskRequest`, `TaskResponse`.

#### 4.2.16 `collaboration.team`

Principal objects: `CareTeam`, `OnCall`, `OnDuty`.

#### 4.2.17 `domain.fhir`

Primarily factories, builders and conversion models **to and from** external FHIR representations and Harmonia's canonical models. Does not replicate the external domain model or implement FHIR endpoints.

#### 4.2.18 `domain.hl7v2`

Primarily factories, builders and conversion models **to and from** HL7 v2 representations and Harmonia's canonical models. Does not implement transport endpoints.

#### 4.2.19 `domain.matrix`

Primarily factories, builders and conversion models **to and from** Matrix representations and Harmonia's canonical models. Does not implement Matrix communication services.

#### 4.2.20 `workflow.data`

Shared information associated with workflow processing and results.

Candidate object: `PackedPragma` (**name and detailed semantics provisional**). No assumptions are made here about its fields or relationships.

#### 4.2.21 `observability`

Principal objects: `ComponentCoordinate`, `ComponentStatus`, `ComponentActivity`, `ComponentLog`, `ComponentTask`, `ComponentAudit`.

These are shared observability information models, not observation-agent implementations.

#### 4.2.22 `information.operations`

Initial object: `ManagementContext`.

Operational management information relating to Information Units; detailed transient/durable semantics and placement within an IU remain to be developed.

#### 4.2.23 Additional branches

To be identified as the model is developed.

## 5. Target Interface Package Hierarchy

**Base package:** `net.fhirfactory.harmonia.interfaces.core`

### 5.1 Overview

```text
net.fhirfactory.harmonia.interfaces.core
├── audit
│   ├── AuditClient
│   └── AuditQuery
├── logger
│   ├── LoggerClient
│   └── LoggerQuery
├── endpoint
│   └── RegisterEndpoint
├── routing
│   ├── InformationUnitSubscription
│   └── InformationUnitPublish
└── observability
    └── IrisComponentObservabilityInterface
```

### 5.2 Core Hierarchy Branches

#### 5.2.1 `audit`

Interfaces: `AuditClient`, `AuditQuery`.

#### 5.2.2 `logger`

Interfaces: `LoggerClient`, `LoggerQuery`.

#### 5.2.3 `endpoint`

Interface: `RegisterEndpoint`.

#### 5.2.4 `routing`

Interfaces: `InformationUnitSubscription`, `InformationUnitPublish`.

#### 5.2.5 `observability`

Interface: `IrisComponentObservabilityInterface`.

Detailed interface contracts are defined separately from this target package model.

