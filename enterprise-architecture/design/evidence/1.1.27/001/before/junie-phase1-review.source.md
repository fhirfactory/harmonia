### Phase 1 — Comprehensive Object-Oriented Engineering Quality Review and Refactoring Plan

#### Executive Summary & Scope

In accordance with the Harmonia Implementation Guide (`docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`), repository `AGENTS.md`, and the accepted Architecture Continuum (Baseline 1.1.25/1.1.24), this review presents an evidence-based assessment of the `Calliope` production Java codebase. 

Per the task mandate, **no source code has been modified during Phase 1**. This review documents structural deficiencies, identifies classes exceeding the mandatory 500-line ceiling, highlights package and encapsulation issues, maps divergences against the Architecture Continuum, and presents a proposed refactoring plan for Solution Architect disposition prior to Phase 2 implementation.

---

### 1. Applicable Authoritative Architectural Baseline & DRP Inventory

Calliope (`calliope`) serves as Harmonia's shared canonical model and schema library. The applicable authoritative baselines and design proposals governing this review are:

1. **Harmonia Architectural Axioms (`enterprise-architecture/governance/architectural-axioms.md`)**:
   - `AX-01` to `AX-18`, particularly `AX-05` (Mneme active state vs Mnemosyne authoritative durable truth), `AX-06` (Default-deny security governance), `AX-13` (Interoperability publication boundaries), `AX-15` (Explicit uncertainty / three-valued outcome semantics), and `AX-17` (Discrepancy and conflict preservation).
2. **Repository Guardrails (`AGENTS.md`)**:
   - Section 2 System Taxonomy: Calliope models are pure domain models with zero dependencies on higher layers (`Themis`, `Hestia`, `Petasos`, `Energeia`, `Pylai`, `Iris`, `Paradeigma`, `Agora`). Calliope-Models (build-time library) is strictly distinguished from Calliope-DataSets (runtime managed information access).
   - Invariants 1–10: Especially Invariant 7 (Zero-PHI Diagnostic Logging via `PhiLogger`).
3. **Harmonia Implementation Guide (`docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`)**:
   - §4: Hard ceiling of 500 lines of code per class (excluding license and imports).
   - §5: Defensive programming and 95/5 engineering philosophy (null checks, defensive copies, unmodifiable collections, fail-fast validations).
   - §6: Comprehensive educational documentation explaining responsibilities, contracts, and failure modes.
   - §7: Clean OO design, Single Responsibility Principle (SRP), standard package structure (`datatypes`, `valuesets`, `factory`, `base`), and strict separation of information models from consuming component hierarchies.
4. **Final Architecture Review Proposals (DRPs)**:
   - `DRP-Topic-and-TopicMask.Final.001.md`: Establishes immutable, independently identifiable `Topic` (UUID, explicitly sequenced `canonicalName` root-to-leaf mapping, derived names, no `Topic.version`) and `TopicMask` (UUID, single matching basis: `TopicNameBased` or `TopicCanonicalNameBased`, `Exact` vs `Inclusive` containment depth, neutral element-wise regex matching).
   - `DRP-Information-Unit-Framework.Final.002.md`: Consolidated reconciliation of `IU-01` to `IU-13`. Establishes the peer metadata model (`InformationMetadata`, `PetasosParcelMetadata`, `OperationalMetadata`), singular governed `InformationRepresentation`, distinct versioning dimensions (authoritative persisted IU revision vs Mneme operational revision vs Content Structure Version), and four immutable `InformationCoordinates`.
   - `DRP-OperationalMetadata-and-OperationalMetadataMask.Final.001.md`: Approves `OM-01`–`OM-10`, relocating active cache manifestations, operational revisions, and persistence handshakes from `InformationUnit` metadata to peer `OperationalMetadata`.
   - `DRP-Mnemosyne-Authoritative-Persistence-Service-Model.Final.001.md`: Establishes governed point access contracts (`GovernedAccess`, `GovernedReader`, `GovernedWriter`, `AuthoritativeVersion`, `ResourceKey`).

---

### 2. Complete Calliope Source Tree Review & Metrics

The Calliope production Java source tree (`calliope/src/main/java`) comprises **47 source files across 13 packages**, totaling approximately 16,400 lines of code.

#### Current Package Breakdown:
- `net.fhirfactory.harmonia.logging` (4 files): `DefaultPhiLogger`, `PhiLogger`, `PhiLoggerFactory`, `PhiLoggingConfig`.
- `net.fhirfactory.harmonia.model.alignment` (2 files): `AlignmentAssessment`, `ResourceAlignmentPort`.
- `net.fhirfactory.harmonia.model.core.information` (18 files): `ContainerMetadata`, `Content`, `ContentMetadata`, `InformationCoordinates`, `InformationItinerary`, `InformationMetadata`, `InformationRepresentation`, `InformationSecurityContext`, `InformationUnit`, `InformationUnitIdentifier`, `InformationUnitMask`, `InformationUnitMetadata`, `InformationUnitRelationship`, `LifecycleContext`, `ManagementContext`, `OperationalMetadata`, `OperationalMetadataMask`, `OperationalRevision`.
- `net.fhirfactory.harmonia.model.ergon` (3 files): `ErgonEvent`, `ErgonPayload`, `ErgonReasonEnum`.
- `net.fhirfactory.harmonia.model.governedwrite` (19 files): `ActiveStateConflict`, `ActiveStateConvergencePort`, `ActiveStateCoordinationResult`, `ActiveStateCoordinator`, `ActiveStateToken`, `ActiveStateTokenBridge`, `AuthoritativeCommitOutcome`, `AuthoritativePreconditionConflict`, `AuthoritativeVersion`, `ConvergenceStatus`, `ExpectedAuthoritativeVersion`, `GovernedAccess`, `GovernedBoundaryValidator`, `GovernedRead`, `GovernedReader`, `GovernedWriter`, `PreconditionFailureReason`, `ResourceKey`, `WriteResult`.
- `net.fhirfactory.harmonia.model.persistence` (2 files): `PersistenceOperationEnvelope`, `PersistenceOperationType`.
- `net.fhirfactory.harmonia.model.petasos` (1 file): `PetasosQueueDefinition`.
- `net.fhirfactory.harmonia.model.pragma` (5 files): `Pragma`, `PragmaCheckpoint`, `PragmaFhirConverter`, `PragmaStatus`, `ProviderRegistryChangePragma`.
- `net.fhirfactory.harmonia.model.praxis` (1 file): `PraxisDefinition`.
- `net.fhirfactory.harmonia.model.registry` (1 file): `ProviderRegistryConstants`.
- `net.fhirfactory.harmonia.model.security` (7 files): `ErgonSecurityDefinition`, `FhirConfidentialityEnum`, `FhirSecurityTagManager`, `HarmoniaAuthorityEnum`, `HarmoniaRoleEnum`, `HarmoniaSecurityCodeSystem`, `HarmoniaSecurityLabelEnum`.
- `net.fhirfactory.harmonia.model.status` (1 file): `ModuleStatus`.
- `net.fhirfactory.harmonia.model.topic` (5 files): `MaskType`, `Topic`, `TopicMask`, `TopicMaskType`, `TopicSubscription`.

---

### 3. Comprehensive Identification of Engineering Deficiencies

#### 3.1 Classes Violating the 500-Line Ceiling Rule
Exactly **10 production classes** exceed the mandatory 500-line ceiling:

| Class | Current Lines | Primary Contributing Factors | Recommended Refactoring Strategy |
|---|---|---|---|
| `ErgonPayload.java` | **953** | Conflates domain payload model with FHIR `TaskInputComponent` / `TaskOutputComponent` mapping, JSON serialization, and legacy extension parsing. | Extract `ErgonPayloadFhirConverter`, extract Jackson serializer helpers, retain pure immutable payload model (~250 lines). |
| `PraxisDefinition.java` | **880** | Combines workflow sequence model with Camel endpoint URI synthesis, route ID generation, legacy 5-tuple topic matching, and dozens of duplicate getter/setter aliases. | Extract route/endpoint string synthesis to a factory/builder; extract topic matching to a validator/strategy; remove redundant aliases (~300 lines). |
| `Pragma.java` | **875** | Houses 200-line inline static `Builder`, extensive query convenience filters (`findInput`, `filterOutputs`), and defensive collection copiers. | Extract `PragmaBuilder` to `factory` subpackage; retain core task instance model and checkpoint state management (~350 lines). |
| `InformationUnit.java` | **855** | Contains 200-line inline static `Builder`, dozens of deep forwarding methods into `metadata` and `representation` (violating Law of Demeter), and 20+ `with...` derivation methods. | Extract `InformationUnitBuilder` to `factory`; eliminate deep forwarding delegation; keep canonical aggregate root focused on identity, version, metadata, and representation (~320 lines). |
| `Topic.java` | **737** | Contains nested `Topic.Entry` (85 lines), nested `Topic.Builder` (60 lines), legacy 5-tuple constructors and factory methods (`fromHl7`, `fromFhir`, `forEgress`), and legacy element accessors. | Extract `TopicEntry` to `datatypes`; extract `TopicBuilder` and legacy HL7/FHIR creation methods to `TopicFactory` in `factory`; retain canonical immutable `Topic` (~240 lines). |
| `PersistenceOperationEnvelope.java` | **702** | Houses 205-line inline static `Builder` and 130 lines of security context extraction factory methods (`ofCreate`, `ofUpdate`, `ofDelete`, `fromSecurityContext`). | Extract `PersistenceOperationEnvelopeBuilder` and `PersistenceOperationEnvelopeFactory` to `factory` subpackage (~280 lines). |
| `TopicMask.java` | **673** | Contains nested `EntryPattern` (74 lines), nested `Builder` (87 lines), string parsing, and regex compilation caching logic. | Extract `TopicEntryPattern` to `datatypes`; extract `TopicMaskBuilder` to `factory`; streamline pattern compilation (~280 lines). |
| `OperationalMetadata.java` | **648** | Contains 200-line inline static `Builder` and dual getter methods (`name()` vs `getName()`). | Extract `OperationalMetadataBuilder` to `factory` subpackage (~260 lines). |
| `ContainerMetadata.java` | **607** | Houses 126-line inline static `Builder`, dozens of overloaded `with...` methods (both `Topic` and `String` overloads), and dual accessors. | Extract `ContainerMetadataBuilder` to `factory`; consolidate string-to-topic parsing overloads (~250 lines). |
| `PragmaFhirConverter.java` | **566** | Conflates core `Task` conversion with checkpoint extension extraction and `ThemisSecurityContext` extension mapping. | Extract `PragmaSecurityFhirConverter` and `PragmaCheckpointFhirConverter`; coordinate conversions cleanly through `PragmaFhirConverter` (~220 lines). |

*Note: Classes near the 500-line boundary (`InformationSecurityContext` at 465, `LifecycleContext` at 454, `ContentMetadata` at 450, `InformationRepresentation` at 448, `TopicSubscription` at 417, `InformationUnitMask` at 417, `Content` at 406, `PragmaCheckpoint` at 402) will also be reviewed to prevent future line-ceiling overflow.*

#### 3.2 Package-Organisation Deficiencies
1. **Replication of Consuming Component Hierarchy (§7.3 Violation)**:
   - Current packages are named directly after downstream consuming components: `model.ergon` (Ponos/Erga activity execution), `model.pragma` (Energeia execution envelope), `model.praxis` (Energeia pathway fulfilment), and `model.petasos` (Petasos messaging).
   - This directly contradicts Implementation Guide §7.3: *"Calliope MUST NOT be organised as a replication of Harmonia's implementation-component hierarchy. An information object used by Ponos, Petasos, Mneme or another Harmonia component does not automatically belong in a package named after that consuming component."*
2. **Absence of Standard Supporting Subpackages (§7.2 Violation)**:
   - Within `model.core.information`, `model.governedwrite`, `model.topic`, and `model.security`, domain entities, low-level data types, value sets (enums), builders, and conversion utilities are dumped flat into the same package root.
   - The required subpackages (`datatypes`, `valuesets`, `factory`) are not used anywhere in Calliope.
3. **Misleading Package Naming (`model.governedwrite`)**:
   - The package is named `model.governedwrite`, yet it houses `GovernedReader`, `GovernedRead`, and `GovernedAccess` (which explicitly unifies reading and writing). Calling the entire package `governedwrite` is misleading and unaligned with the Architecture Continuum concept of *Governed Point Access*.
4. **Cross-Cutting Logging in Calliope Root**:
   - `net.fhirfactory.harmonia.logging` resides in the Calliope module. While `PhiLogger` is a repository-wide standard enforcing Invariant 7, Calliope is defined as the *Canonical Model & Schema Library*. Its placement should be clearly categorized under core infrastructure or logging contracts.

#### 3.3 Inappropriate Inheritance and Coupling Anti-Patterns
1. **`InformationUnitMetadata` vs `InformationMetadata` Parallel Hierarchy**:
   - `InformationUnit` maintains an awkward dual metadata representation with type-checks (`if (metadata instanceof InformationUnitMetadata ium)`), creating runtime coupling between the newer DRP `InformationMetadata` interface and the legacy `InformationUnitMetadata` class.
2. **`PraxisDefinition` Conflation**:
   - `PraxisDefinition` contains Apache Camel routing logic (generating Camel route IDs, input/output/error endpoint URI validation) within a Calliope domain class. Camel routing concepts belong in Energeia/Praxis, not in Calliope canonical schemas.
3. **`ErgonSecurityDefinition` in `model.security`**:
   - `ErgonSecurityDefinition` couples Ergon activity execution to Themis authorities and is placed in `model.security` alongside generic FHIR security tagging utilities (`FhirSecurityTagManager`) and security enums.

#### 3.4 Inadequate Documentation Quality
- Several classes and enums (e.g. `HarmoniaSecurityCodeSystem`, `ConvergenceStatus`, `AuthoritativeCommitOutcome`, `PreconditionFailureReason`, `PetasosQueueDefinition`) lack comprehensive educational documentation describing their 95/5 failure semantics, architectural contracts, and newcomer context, failing the educational mandate established in the Implementation Guide.

---

### 4. Proposed Coherent Object-Oriented Package Organisation

To satisfy the Harmonia Implementation Guide (§7.2 and §7.3) while preserving domain cohesion and clean layering, the following package structure is proposed:

```text
net.fhirfactory.harmonia.model.core.naming
├── Topic                                            [Root Domain Aggregate]
├── TopicMask                                        [Root Domain Aggregate]
├── TopicSubscription                                [Subscription Criteria]
├── datatypes/
│   ├── TopicEntry                                   [Extracted from Topic.Entry]
│   └── TopicEntryPattern                            [Extracted from TopicMask.EntryPattern]
├── valuesets/
│   ├── MaskType
│   └── TopicMaskType
└── factory/
    ├── TopicBuilder                                 [Extracted Builder]
    ├── TopicFactory                                 [HL7 / FHIR creation helpers]
    └── TopicMaskBuilder                             [Extracted Builder]

net.fhirfactory.harmonia.model.core.information
├── InformationUnit                                  [Root Domain Aggregate]
├── InformationRepresentation                        [Singular Governed Representation]
├── InformationMetadata                              [Peer Information Metadata Interface]
├── OperationalMetadata                              [Peer Operational Metadata Aggregate]
├── InformationUnitIdentifier                        [IU Persistent Identity]
├── InformationUnitRelationship                      [Governed Relationship]
├── InformationUnitMask                              [Composite IU Selector]
├── OperationalMetadataMask                          [Operational Selector]
├── datatypes/
│   ├── ContainerMetadata                            [Envelope / Assembly Descriptor]
│   ├── ContentMetadata                              [Content Specification Descriptor]
│   ├── Content                                      [Governed Portable Content]
│   ├── InformationCoordinates                       [Four Stable Location Topics]
│   ├── InformationItinerary                         [Participating Path Topics]
│   ├── InformationSecurityContext                   [IU Security Criteria]
│   ├── LifecycleContext                             [Governed Disposition State]
│   ├── ManagementContext                            [Active Cache Residency Context]
│   └── OperationalRevision                          [Mneme Concurrency Revision]
└── factory/
    ├── InformationUnitBuilder                       [Extracted Builder]
    ├── ContainerMetadataBuilder                     [Extracted Builder]
    ├── OperationalMetadataBuilder                   [Extracted Builder]
    └── InformationRepresentationBuilder             [Extracted Builder]

net.fhirfactory.harmonia.model.core.governedaccess     [Replaces model.governedwrite]
├── GovernedAccess                                   [Unified Read/Write Port]
├── GovernedReader                                   [Read Port]
├── GovernedWriter                                   [Write Port]
├── ResourceKey                                      [Logical Resource Key]
├── AuthoritativeVersion                             [Authoritative Revision]
├── ExpectedAuthoritativeVersion                     [Optimistic Concurrency Key]
├── WriteResult                                      [Authoritative Commit Result]
├── ActiveStateCoordinator                           [Mneme Coordination Port]
├── ActiveStateToken                                 [Active Lock Token]
├── ActiveStateTokenBridge                           [Token Bridge Utility]
├── ActiveStateCoordinationResult                    [Coordination Outcome]
├── ActiveStateConvergencePort                       [Multi-Node Convergence Port]
├── GovernedBoundaryValidator                        [Precondition Validator]
├── valuesets/
│   ├── AuthoritativeCommitOutcome
│   ├── PreconditionFailureReason
│   └── ConvergenceStatus
└── datatypes/
    ├── ActiveStateConflict
    └── AuthoritativePreconditionConflict

net.fhirfactory.harmonia.model.core.task              [Replaces model.pragma & model.ergon]
├── Pragma                                           [Discrete Task Execution Instance]
├── ErgonPayload                                     [Task Input/Output Payload Unit]
├── ErgonEvent                                       [Task Lifecycle Event]
├── PragmaCheckpoint                                 [Immutable Execution Audit Record]
├── valuesets/
│   ├── PragmaStatus
│   └── ErgonReasonEnum
├── datatypes/
│   └── ProviderRegistryChangePragma
└── factory/
    ├── PragmaBuilder                                [Extracted Builder]
    ├── ErgonPayloadBuilder                          [Extracted Builder]
    ├── PragmaFhirConverter                          [Converter Coordinator]
    ├── PragmaSecurityFhirConverter                  [Extracted Security Converter]
    ├── PragmaCheckpointFhirConverter                [Extracted Checkpoint Converter]
    └── ErgonPayloadFhirConverter                    [Extracted Payload FHIR Converter]

net.fhirfactory.harmonia.model.core.workflow          [Replaces model.praxis]
├── PraxisDefinition                                 [Task Sequence Definition Model]
└── factory/
    ├── PraxisDefinitionBuilder                      [Extracted Builder]
    └── PraxisRouteIdGenerator                       [Extracted Camel/Route String Helper]

net.fhirfactory.harmonia.model.core.persistence       [Persistence Envelopes]
├── PersistenceOperationEnvelope                     [Command Envelope Model]
├── valuesets/
│   └── PersistenceOperationType
└── factory/
    ├── PersistenceOperationEnvelopeBuilder          [Extracted Builder]
    └── PersistenceOperationEnvelopeFactory          [Extracted Factory Helpers]

net.fhirfactory.harmonia.model.core.security          [Model Security & Tagging]
├── valuesets/
│   ├── HarmoniaAuthorityEnum
│   ├── HarmoniaRoleEnum
│   ├── HarmoniaSecurityCodeSystem
│   ├── HarmoniaSecurityLabelEnum
│   └── FhirConfidentialityEnum
├── datatypes/
│   └── ErgonSecurityDefinition
└── factory/
    └── FhirSecurityTagManager                       [FHIR Tag Utility]

net.fhirfactory.harmonia.model.core.alignment         [Canonical Model Alignment]
├── ResourceAlignmentPort                            [Alignment Boundary Port]
└── datatypes/
    └── AlignmentAssessment

net.fhirfactory.harmonia.model.core.messaging         [Replaces model.petasos]
└── datatypes/
    └── PetasosQueueDefinition

net.fhirfactory.harmonia.logging                     [Invariant 7 Logging]
├── PhiLogger
├── DefaultPhiLogger
├── PhiLoggerFactory
└── PhiLoggingConfig
```

---

### 5. Architectural Divergence Analysis: Proposed Organisation vs Architecture Continuum

In accordance with requirement 6 of Phase 1, the following divergences between the proposed package organisation and the accepted Architecture Continuum are identified:

#### 5.1 Placement of `Topic` and `TopicMask`
- **Architecture Continuum Position**:
  In the canonical Continuum repository (`enterprise-architecture/continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md` and `bb489157a6bf48be926c5a601bb0a6c3.md`), both elements are formally registered under:
  - `displayName: Topic`, `friendlyName: model.core.information.Topic`
  - `displayName: TopicMask`, `friendlyName: model.core.information.TopicMask`
- **Harmonia Implementation Guide §7.2 Position**:
  The Implementation Guide explicitly proposes:
  ```text
  net.fhirfactory.harmonia.model.core.naming
      Topic
      TopicMask
  ```
- **Current Codebase Position**:
  Both classes reside in:
  ```text
  net.fhirfactory.harmonia.model.topic
  ```
- **Architectural Analysis & Divergence**:
  `DRP-Topic-and-TopicMask.Final.001.md` (§1 and §5.3) establishes that `Topic` and `TopicMask` are foundational naming, classification, and neutral matching constructs that are independent of Information Units, subscriptions, or persistence. Placing them in `net.fhirfactory.harmonia.model.core.naming` provides strong conceptual cohesion and prevents circular coupling between core naming primitives and composite information units.
  However, this diverges from the Continuum `friendlyName` (`model.core.information.*`).

  **Proposed Disposition Options for Solution Architect Adjudication**:
  - **Option 1 (Recommended)**: Place `Topic` and `TopicMask` in `net.fhirfactory.harmonia.model.core.naming` per Implementation Guide §7.2. Flag the Architecture Continuum data-object metadata (`friendlyName`) for subsequent alignment during the next governed release cycle.
  - **Option 2 (Strict Continuum Matching)**: Place `Topic` and `TopicMask` directly in `net.fhirfactory.harmonia.model.core.information` matching the current Continuum `friendlyName`, maintaining subpackages `datatypes` and `factory`.
  - **Option 3 (Conservative Transition)**: Retain the existing package `net.fhirfactory.harmonia.model.topic`, decomposing its internal structures into `datatypes`, `valuesets`, and `factory` without moving `Topic.java` or `TopicMask.java`.

#### 5.2 Renaming `model.governedwrite` to `model.core.governedaccess`
- **Continuum Position**: The Architecture Continuum baseline and ADR-020 define *Governed Point Access*, comprising unified read and write operations (`GovernedAccess`, `GovernedReader`, `GovernedWriter`).
- **Divergence**: The existing package name `model.governedwrite` misrepresents the package contents (which includes `GovernedReader` and `GovernedAccess`). Renaming to `model.core.governedaccess` resolves this divergence and accurately reflects the Continuum contract.

#### 5.3 Unification of `model.pragma` and `model.ergon` into `model.core.task`
- **Continuum Position**: Pragma represents the discrete task execution instance, and Ergon represents the activity execution envelope.
- **Divergence**: The current Java packages replicate runtime component names (Energeia/Ponos/Erga). Reorganizing them into `model.core.task` groups task models cohesively by domain concept rather than consuming component hierarchy, complying with Implementation Guide §7.3.

---

### 6. Downstream Impacts, Maven Reactor Scope, and Migration Strategy

#### 6.1 Downstream Modules Affected
Calliope is Harmonia's foundational canonical model library. Changes to its package paths directly impact:
1. **`energeia`** (`energeia-erga`, `energeia-ponos`, `energeia-praxis`):
   - Extensive usage of `Pragma`, `ErgonPayload`, `ErgonEvent`, `Topic`, `PraxisDefinition`, `PhiLogger`.
2. **`hestia`** (`mneme-cache`, `mneme-persistence`, `mneme-rest`, `mnemosyne-clinical`):
   - Consumes `GovernedAccess`, `ResourceKey`, `AuthoritativeVersion`, `InformationUnit`, `PersistenceOperationEnvelope`.
3. **`pylai`** (`pylai-mllp-in`, `pylai-mllp-out`, `pylai-fhir-rest`):
   - Consumes `Topic`, `PhiLogger`, `Pragma`, `ErgonPayload`.
4. **`themis`** (`themis-api`, `themis-core`):
   - Consumes security enums and interfaces.
5. **`iris`** (`iris-befe`):
   - Presentation gateway references to canonical models.
6. **`paradeigma`** (`paradeigma-test`, `paradeigma-common`):
   - Test probes and ArchUnit verification suites.

#### 6.2 ArchUnit Architectural Rule Conformance
- In `paradeigma/paradeigma-test/src/test/java/.../PackageLayeringArchitectureTest.java`:
  ```java
  noClasses().that().resideInAPackage("net.fhirfactory.harmonia.calliope..")
      .or().resideInAPackage("net.fhirfactory.harmonia.model..")
      .should().dependOnClassesThat().resideInAnyPackage(...)
  ```
- Because all proposed packages continue to reside under `net.fhirfactory.harmonia.model.*` or `net.fhirfactory.harmonia.logging.*`, the existing ArchUnit rules remain fully compatible.

#### 6.3 Phase 2 Execution & Validation Plan
Upon Solution Architect acceptance and disposition:
1. **Step 1: 500-Line Class Decomposition**:
   - Extract builders, converters, and nested entry patterns to their respective `factory` and `datatypes` subpackages.
   - Reduce all 10 oversized classes below 350 lines.
2. **Step 2: Package Reorganisation & Clean Imports**:
   - Apply accepted package restructuring (`model.core.naming`, `model.core.task`, `model.core.governedaccess`, etc.).
   - If backward compatibility is requested for external branches, temporary `@Deprecated` subclass wrappers can be retained in legacy packages; otherwise, clean atomic repository-wide import updates will be applied.
3. **Step 3: Verification & Conformance**:
   - Execute Calliope unit test suite: `mvn clean test -pl calliope`.
   - Execute downstream dependent test suites:
     - `mvn test -pl energeia/erga,energeia/ponos,energeia/praxis`
     - `mvn test -pl hestia/mneme-cache,hestia/mneme-persistence,hestia/mnemosyne-clinical`
     - `mvn test -pl pylai/pylai-mllp-in,pylai/pylai-mllp-out`
   - Execute ArchUnit architecture tests: `mvn test -pl paradeigma/paradeigma-test -Dtest="*ArchitectureTest"`.
   - Run full repository test suite: `mvn test`.
4. **Step 4: Evidence & Governance Retention**:
   - Record formal task and report records under `enterprise-architecture/design/evidence/`.

---

### 7. Explicit Decision Points for Solution Architect Disposition

Prior to beginning Phase 2 source-code modifications, explicit Solution Architect disposition is requested on the following three points:

1. **`Topic` and `TopicMask` Package Location**:
   - *Option 1*: `net.fhirfactory.harmonia.model.core.naming` (Implementation Guide §7.2 proposal; requires Continuum `friendlyName` update in next release).
   - *Option 2*: `net.fhirfactory.harmonia.model.core.information` (matches existing Continuum `friendlyName`).
   - *Option 3*: Retain `net.fhirfactory.harmonia.model.topic` while decomposing internals into `datatypes` and `factory`.
2. **Consuming Component Package Reorganisation**:
   - Authorisation to consolidate `model.pragma` and `model.ergon` into `net.fhirfactory.harmonia.model.core.task`.
   - Authorisation to rename `model.governedwrite` to `net.fhirfactory.harmonia.model.core.governedaccess`.
   - Authorisation to transition `model.praxis.PraxisDefinition` into `net.fhirfactory.harmonia.model.core.workflow` while decoupling Camel route-synthesis logic into a factory helper.
3. **Downstream Refactoring Scope**:
   - Whether to apply an atomic repository-wide package update across all modules simultaneously, or provide `@Deprecated` compatibility type aliases during the transition.

**Awaiting Solution Architect disposition to commence Phase 2 implementation.**
