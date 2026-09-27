```{=html}
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
```
# Harmonia Architectural Guardrails & Rules for AI Agents (AGENTS.md)

This document establishes the authoritative repository-wide
architectural rules, boundaries, and development constraints for
autonomous and co-executor agents working on the Harmonia Health
Integration Environment (HIE) codebase.

------------------------------------------------------------------------

## 1. Architectural Authority

All architecture, design, implementation and automated-agent activity
within Harmonia is governed by the Harmonia Architectural Axioms defined
in:

    docs/architectural-axioms.md

The Architectural Axioms are the highest-level design authority within
the Harmonia repository.

Architecture decisions, architectural invariants, design contracts,
implementation patterns and existing source code MUST be interpreted
consistently with those axioms.

Where an existing implementation, architectural decision, requirement,
documentation statement or requested change appears to conflict with an
Architectural Axiom, the conflict MUST be reported rather than silently
resolved in favour of the existing implementation.

Existing implementation is not, by itself, evidence of architectural
intent.

Before proposing or implementing a material architectural change, an
agent MUST identify the Architectural Axioms materially relevant to that
change and demonstrate that the proposed approach is consistent with
them.

## 2. System Taxonomy & Module Hierarchy

Harmonia enforces strict separation of concerns across its 9 core
subprojects:

  -------------------------------------------------------------------------------
  Subproject        Domain /              Allowed           Invariants &
                    Responsibility        Dependencies      Constraints
  ----------------- --------------------- ----------------- ---------------------
  **Calliope**      Canonical Schemas,    JDK, HAPI FHIR    Pure domain models;
  (`calliope`)      Common Models,        Structures        zero dependencies on
                    HL7/FHIR converters,                    higher layers
                    Topic definitions                       (Themis, Hestia,
                                                            Petasos, Energeia,
                                                            Pylai, Iris,
                                                            Paradeigma, Agora).

  **Themis**        Policy evaluation,    JDK, Calliope,    Default-deny security
  (`themis/`)       RBAC/ABAC             HAPI FHIR         engine; `themis-api`
                    authorization,                          contains pure
                    non-PHI security                        contracts without
                    auditing                                engine or storage
                                                            dependencies.

  **Hestia**        Mneme:                Calliope, Themis, Mneme state is active
  (`hestia/`)       application-facing    Infinispan, HAPI  and reconstructable,
                    managed-information   FHIR, PostgreSQL  never authoritative.
                    access, distributed                     Mnemosyne establishes
                    active state,                           durable truth and
                    observation and                         does not expose its
                    coordination.                           persistence
                    Mnemosyne:                              implementation as an
                    authoritative durable                   application-access
                    managed-information                     mechanism.
                    state and                               
                    authoritative state                     
                    progression.                            

  **Petasos**       Resilient messaging   JDK, Calliope,    `petasos-api` is
  (`petasos/`)      abstraction and       Themis API        strictly free of JMS
                    ActiveMQ Artemis                        or Artemis
                    broker adapters                         dependencies. Artemis
                                                            client code is
                                                            isolated to
                                                            `petasos-artemis`.

  **Energeia**      Task processing       Calliope, Themis, Activity execution
  (`energeia/`)     (Ponos), Ergon        Hestia, Petasos   handles `Pragma`
                    activity units                          envelopes and FHIR
                    (Erga), Workflow                        `Task` lifecycle.
                    orchestration                           
                    (Praxis)                                

  **Pylai**         Interoperability      Calliope, Themis, Establishes
  (`pylai/`)        membrane and          Petasos, Hestia   standards-compliant
                    inbound/outbound                        ingress/egress
                    protocol gateways                       boundaries. External
                    (including MLLP and                     contracts must not
                    FHIR REST)                              expose
                                                            Harmonia-private
                                                            operational
                                                            semantics; egress
                                                            terminates Harmonia
                                                            management of the
                                                            emitted
                                                            representation.

  **Iris**          Presentation services Calliope, Themis  Strictly decoupled
  (`iris/`)         (iris-befe WildFly    API, Mneme        from authoritative
                    gateway,              client-facing     persistence/JPA.
                    iris-clinical,        contracts         Managed-information
                    iris-console,                           access must use
                    iris-administration                     defined
                    SPAs)                                   application-facing
                                                            interfaces; Iris must
                                                            not treat raw cache
                                                            or database access as
                                                            an alternative
                                                            persistence path.

  **Agora**         Matrix/Synapse        Calliope, Themis  Zero Ponos
  (`agora/`)        collaboration         API, Petasos API, dependencies; Matrix
                    projection, AS        Mnemosyne         DTO encapsulation;
                    transaction           Operations        Themis default-deny
                    ingestion, room/space                   governance; zero-PHI
                    lifecycle                               metadata.

  **Paradeigma**    Synthetic clinical    Production APIs   **Leaf / Simulation
  (`paradeigma/`)   simulation (EMR, LMS, (MLLP, FHIR REST, Only**. Production
                    PAS, RIS-PAC          Petasos API)      modules MUST NEVER
                    simulators, scenario                    depend on or import
                    engine)                                 Paradeigma.
  -------------------------------------------------------------------------------

------------------------------------------------------------------------

## 3. Derived Architectural Invariants and Guardrails

The following rules implement or protect the Architectural Axioms. They
are mandatory but subordinate to the axioms from which Harmonia's
architecture is derived.

### Invariant 1: Paradeigma Production Isolation

-   **Rule**: `Production Code -> Paradeigma` is strictly **forbidden**.
-   **Enforcement**:
    1.  No production POM may declare a `<dependency>` on any
        `net.fhirfactory.harmonia:paradeigma*` artifact.
    2.  No production Java class may import
        `net.fhirfactory.harmonia.paradeigma.*`.
    3.  No production class may include simulation flags
        (e.g. `paradeigmaMode`, `simulationMode`, `syntheticRequest`,
        `isParadeigmaGenerated`).
    4.  Verified continuously by `ParadeigmaIsolationArchitectureTest`.

### Invariant 2: Petasos API Abstraction & Encapsulation

-   **Rule**: `petasos-api` contains pure Java abstractions (`Petasos`,
    `PetasosProducer`, `PetasosConsumer`, `PetasosMessage`,
    `PetasosDestination`).
-   **Enforcement**:
    1.  Zero `org.apache.activemq..`, `jakarta.jms..`, or `javax.jms..`
        classes may be exposed in or imported by `petasos-api`.
    2.  Message payloads in Petasos are treated as opaque binary/text
        streams (`byte[]` / `String`). Petasos never parses HL7 or FHIR
        business content.
    3.  Verified continuously by `PetasosApiIsolationArchitectureTest`.

### Invariant 3: Iris Presentation Decoupling

-   **Rule**: The Iris presentation tier (`iris-befe` and Vue 3 SPAs)
    must remain presentation-only and decoupled from internal databases.
-   **Enforcement**:
    1.  Iris modules must not depend on or import JPA/Hibernate
        (`jakarta.persistence..`, `org.hibernate..`), PostgreSQL drivers
        (`org.postgresql..`), or server-side JPA (`ca.uhn.fhir.jpa..`).
    2.  `iris-administration` is a Vue 3 SPA consuming FHIR REST
        endpoints (`/fhir/r5/Practitioner`, etc.). It must not implement
        Provider Registry database storage, authoritative validation
        state machines, or referential integrity rules.
    3.  Server-side Provider Registry governance is owned exclusively by
        `mnemosyne-clinical`, `energeia-erga`, and `themis-core`.
    4.  Verified continuously by `IrisDecouplingArchitectureTest` and
        `ProviderRegistryArchitectureTest`.

### Invariant 4: Ingress Dual-Write Safety (REC-001)

-   **Rule**: Inbound gateways (`pylai-mllp-in`) must guarantee
    end-to-end downstream message acceptance before returning an `AA`
    (Application Accept) ACK to the upstream sender.
-   **Enforcement**:
    1.  If downstream Petasos queue publishing
        (`taskEventProducerService.sendTaskEvent(...)`) or cache write
        fails, the exception must NOT be swallowed.
    2.  The failure MUST trigger an `AE` (Application Error) NACK
        response back over MLLP to prompt upstream sender retry.

### Invariant 5: Destination Fan-Out State Tracking (REC-002)

-   **Rule**: Parent workflow tasks in Mnemosyne and Pragma envelopes
    must track granular sub-status per fan-out destination.
-   **Enforcement**:
    1.  `AdtDistributionErgon` records destination checkpoints
        (`FANOUT_DISPATCH_INITIATED`, `destinationQueue`,
        `status=QUEUED`) in the `Pragma`.
    2.  `OutboundTaskResourceBuilder` updates `Task.output` with
        structured `http://example.org/hie/destination-delivery-status`
        extensions capturing `destinationId`, `status`, `ackCode`, and
        timestamps upon delivery.

### Invariant 6: Default-Deny Security Governance (Themis)

-   **Rule**: All governed ingress, processing and state-changing
    operations must execute within an established security context and
    evaluate authorization through Themis.
-   **Enforcement**:
    1.  Unauthenticated or unauthorized requests must default to `DENY`
        (`ThemisDecision.DENY`).
    2.  Security context propagation must use the canonical immutable
        `ThemisSecurityContext` (or an explicitly defined transport
        representation of that context); callers must not establish
        authority through caller-controlled resource attributes or FHIR
        tags.
    3.  Security context is operational context and must not
        automatically become persisted resource content.
    4.  Security-significant evidence must be recorded through the
        Harmonia audit/evidence boundary (Kleio) according to policy,
        without logging unmasked PHI.

### Invariant 7: Zero-PHI Diagnostic Logging

-   **Rule**: Protected Health Information (PHI) must never be emitted
    into non-clinical log streams.
-   **Enforcement**:
    1.  Log identifiers (MRN, control IDs) only; mask or omit patient
        names, addresses, and clinical observation values in standard
        log statements.
    2.  Use `FhirSanitizer` or `PhiLogRouting` for diagnostic message
        logging.

### Invariant 8: Mneme / Mnemosyne State Separation

-   **Rule**: Mneme manages active distributed use of managed
    information; Mnemosyne alone establishes authoritative durable
    state.
-   **Enforcement**:
    1.  Application and presentation code must not use Mnemosyne
        database/JPA access as an application-facing information-access
        mechanism.
    2.  Loss of Mneme must not silently promote process-local or cached
        state to authoritative state.
    3.  Loss of Mnemosyne must not cause Mneme to treat active cache
        state as authoritative durable state.
    4.  Ungoverned raw cache mutation must not be exposed as an
        alternative application path for Harmonia-managed information.

### Invariant 9: External Interoperability Boundary

-   **Rule**: Standards govern Harmonia's external contracts;
    Harmonia-private operational semantics remain internal.
-   **Enforcement**:
    1.  Pylai must construct externally publishable representations
        without destructively altering the internally managed
        representation.
    2.  External publication must be fail-closed: only information and
        extensions permitted by the applicable interoperability contract
        may be emitted.
    3.  Harmonia-private cache, persistence, authority, governance,
        security-context and distributed-concurrency metadata must not
        be exposed merely because it exists internally.
    4.  Egress terminates Harmonia management of the emitted
        representation; retained provenance or audit evidence does not
        extend operational control beyond the boundary.

### Invariant 10: Agora Collaboration & Matrix Isolation

-   **Rule**: Agora makes Matrix a Harmonia collaboration capability; it
    does NOT make Matrix the Harmonia architecture.
-   **Enforcement**:
    1.  **Ponos Decoupling**: Direct dependency from Agora to Ponos
        (`net.fhirfactory.harmonia.energeia.ponos..`) is strictly
        forbidden. Agora coordinates with workflows exclusively via
        Petasos queues (`petasos.queue.agora.*`).
    2.  **Matrix DTO Encapsulation**: Matrix protocol structures
        (Client-Server and Synapse Admin DTOs) must be encapsulated in
        `agora-matrix`. Raw Matrix types must never leak into other
        Harmonia modules.
    3.  **Themis Default-Deny Authorization**: Every room creation,
        invite, join, or kick operation must be gated by Themis policy
        evaluation (`themisAuthorizer.evaluate(...)`).
    4.  **Zero-PHI Room Metadata**: Room aliases, Space names, and
        topics must never include patient names, DOB, MRN, or clinical
        details (use opaque UUIDs).

------------------------------------------------------------------------

## 4. Automated Architecture Test Suite

All agents making modifications to the Harmonia repository must verify
changes against the ArchUnit architecture suite located in
`paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/`:

-   `ParadeigmaIsolationArchitectureTest`: Asserts zero production
    dependencies, imports, or simulation flags for Paradeigma across all
    modules (including `agora`).
-   `AgoraIsolationArchitectureTest`: Asserts Ponos decoupling, Matrix
    DTO encapsulation, and Paradeigma isolation for Agora.
-   `PetasosApiIsolationArchitectureTest`: Asserts zero JMS or ActiveMQ
    Artemis API leakage into `petasos-api`.
-   `IrisDecouplingArchitectureTest`: Asserts zero direct JPA,
    Hibernate, or PostgreSQL database dependencies in Iris.
-   `ProviderRegistryArchitectureTest`: Asserts `iris-administration`
    separation from server-side Provider Registry governance.
-   `PackageLayeringArchitectureTest`: Asserts strict unidirectional
    dependency layering across all subproject packages.
-   `SecurityEnforcementArchitectureTest`: Asserts Themis policy
    contracts and security context structures.
-   Architecture and integration tests SHOULD enforce Mneme/Mnemosyne
    separation and the Pylai external-publication boundary as concrete
    testable consequences of AX-05 and AX-13. New test classes should be
    named for the invariant they enforce rather than for a transient
    implementation mechanism.

------------------------------------------------------------------------

## 5. Execution & Build Commands

-   **Run Architecture Tests**:

    ``` bash
    mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
    ```

-   **Run Agora Subsystem Tests**:

    ``` bash
    mvn test -pl agora/agora-service -am
    ```

-   **Run Inbound MLLP Gateway Tests (REC-001)**:

    ``` bash
    mvn test -pl pylai/pylai-mllp-in -am
    ```

-   **Run Ergon Activity & Outbound Gateway Tests (REC-002)**:

    ``` bash
    mvn test -pl energeia/erga,pylai/pylai-mllp-out -am
    ```

-   **Run Full Repository Test Suite**:

    ``` bash
    mvn test
    ```

## 6. Junie Plans and Reports

Files under `.junie/plans/` and `.junie/reports/` are working and historical
execution artefacts. They are NOT sources of architectural authority.

A Junie plan MUST be interpreted against, in order of authority:

1. `docs/architectural-axioms.md`
2. this `AGENTS.md`
3. applicable accepted Architecture Decision Records
4. applicable requirements and design contracts
5. the task-specific plan

Where an existing Junie plan or report conflicts with a higher-authority
source, the higher-authority source prevails.

Historical plans and reports MUST NOT be used as evidence that an
architectural pattern remains valid merely because it was previously
implemented, proposed, or approved.

Before executing a material architectural plan, Junie MUST identify the
applicable Architectural Axioms and report any conflict between the proposed
work, the existing implementation, and those axioms.