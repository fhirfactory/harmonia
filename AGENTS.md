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

    docs/markdown/governance/architectural-axioms.md

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

### 1.1 AI Context and Architecture Authority

Start substantive architecture and implementation tasks assuming no prior
AI-session context. Fresh AI context is the preferred default for a new
bounded task; clarification, mechanical correction, review before closure
and other tightly coupled work within that same authorised task do not
require a session restart.

Agents MUST use repository-held architectural authority and explicitly
authorised task decisions rather than remembered, summarised or
previous-session interpretations. Load the minimum authoritative context
sufficient for the bounded task, then progressively expand it by following
architectural dependencies, references and traceability where required.
This does not impose a fixed reading sequence or arbitrary context budget,
or waive required task inputs and repository instructions.

Prior conversation, model memory, generated or compacted summaries,
previous-session explanations, `.junie/plans` and `.junie/reports` MAY assist
navigation or investigation but do not constitute architectural authority.
Navigation aids identify sources; they do not replace authoritative material.
If sufficient authority cannot be established, or authority is ambiguous or
contradictory, agents MUST preserve and raise the uncertainty under AX-17
rather than resolve it through memory, convention or inference.

The [Architecture Completion Plan's context-loading guardrail](docs/markdown/architecture-completion-plan.md#81-ai-context-independence-and-progressive-authority-loading)
provides the programme guidance. The existing authority rules remain in force.

### 1.2 RADS and Legacy Documentation Governance

These standing rules apply to human and AI documentation work throughout the
repository.

#### Definitions and Authority

`/docs/markdown` (relative to the repository root) is the **Reference
Architecture Documentation Set (RADS)**. RADS is authoritative for the current
Harmonia architecture, with the Architectural Axioms remaining the highest-level
design authority under section 1. Unresolved matters, candidates and proposals
within RADS MUST remain distinguishable from approved architecture under AX-17.

**Legacy documentation (legacy-doco)** comprises architectural, design,
execution, module and other documentation elsewhere in the repository, outside
`/docs/markdown`. Its location, historical status or correspondence to existing
implementation MUST NOT, by itself, establish current architectural authority
or permit it to override RADS. Repository governance instructions and explicitly
assigned implementation-sequencing authority remain in force in their defined
roles; they do not make legacy architectural descriptions authoritative over
RADS.

#### Reconciliation Before Incorporation

Architectural information discovered in legacy-doco MUST NOT simply be copied
into RADS. It MUST first be reviewed and reconciled against the current
architecture and, where necessary, explicitly architecturally adjudicated
before incorporation into the appropriate RADS material. The direction is:

```text
legacy-doco -> architectural review/adjudication -> RADS
```

RADS MUST NOT be reverse-derived from legacy implementation merely because that
implementation already exists. Conflicts, missing authority and unresolved
architectural dependencies on legacy-doco MUST be reported and preserved under
AX-17 rather than silently resolved through copying or inference. A reference
to legacy-doco does not establish that its architectural meaning has been
reconciled or incorporated into RADS. Discovery and these standing rules do not
authorise migration beyond the explicitly authorised task scope.

#### Preservation and Section Supersession

When information from a specific legacy section has been incorporated,
reconciled, superseded or otherwise replaced by authoritative RADS content, the
original legacy content MUST be preserved unchanged. It MUST NOT be rewritten
merely to make it agree with RADS. Instead, the affected section MUST be
surrounded by an explicit supersession wrapper equivalent to:

```text
------- Legacy Content - Superseded ------- Start ------
Superseded by:
<RADS reference(s)>
---------------------------------------------------------

<ORIGINAL LEGACY CONTENT — UNCHANGED>

------- Legacy Content - Superseded ------- Finish ----
```

The wrapper MUST include RADS replacement references and SHOULD identify the
specific authoritative sections replacing or encapsulating the legacy
architectural meaning. Equivalent formatting appropriate to the source format
MAY be used while preserving the explicit boundaries, replacement references
and unchanged original content.

Enclosed legacy content MAY contradict RADS. Such contradictions are acceptable
as explicitly superseded historical architecture, design or implementation
information; they do not challenge the authority of the current RADS content.
Supersession MUST apply only to the relevant section or content. An entire
legacy document MUST NOT be marked superseded merely because one part has
migrated to RADS.

Legacy-doco MAY continue to contain useful implementation detail, historical
reasoning and execution information. Supersession of its architectural meaning
does not imply deletion of that information. This preservation rule governs
future reconciliation work; it does not authorise retrospective rewriting,
deletion or migration of existing legacy material.

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
-   `PylaiPublicationBoundaryArchitectureTest`: Asserts Pylai external
    publication encapsulation, non-destructive projection, and AX-13
    egress publication boundary rules.
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

### 5.1 Bounded Command, Test and Verification Execution

Commands, tests, builds, container operations, health checks and verification activities MUST NOT be allowed to wait indefinitely. An agent MUST use bounded waiting and MUST recover control when an operation ceases to make meaningful progress.

#### Execution rules

1. Before starting a potentially blocking operation, the agent SHOULD establish a reasonable expected completion or progress interval from the command type, prior runs, test configuration or current runtime context.
2. Ordinary unit tests, architecture tests and focused Maven test invocations SHOULD normally be treated as suspicious when they produce no meaningful progress for approximately **2 minutes**.
3. Integration tests, container startup, dependency resolution, image build/pull, distributed verification and full-repository builds MAY require longer. They MUST nevertheless have a bounded wait appropriate to the operation and SHOULD normally be investigated after approximately **5 minutes without meaningful progress**, unless repository evidence establishes that a longer interval is expected.
4. A single command or verification activity MUST NOT consume more than approximately **10 minutes without meaningful progress** unless the task explicitly requires a known long-running operation and the reason for continuing is reported.
5. Meaningful progress means observable evidence that the operation is advancing, such as new test completion, build phases, dependency activity, container state transition, application startup milestones, health-state changes or relevant log output. Repeated identical output, an unchanged spinner, an open process with no relevant output, or repeated polling with no state change is NOT meaningful progress.
6. When the applicable no-progress interval is exceeded, the agent MUST regain control: inspect available process/test/container state, capture relevant output, and terminate or time out the stalled operation where safe. The agent MUST NOT simply continue waiting.
7. If termination could destroy material state needed to diagnose an authoritative mutation, persistence operation or distributed outcome, the agent MUST preserve AX-15 uncertainty semantics: capture evidence and report the operation as unresolved rather than assuming success or failure.
8. A timed-out or stalled command MUST be reported as **TIMEOUT** or **STALLED**. It MUST NOT be reported as PASS or FAIL unless that semantic result was independently established.
9. The agent MUST distinguish an implementation/test failure from a harness, environment, dependency, deadlock, blocking-resource or command-invocation problem before changing production code.
10. A stalled command MUST NOT be repeatedly rerun unchanged. Before retrying, the agent MUST identify a concrete reason to expect a different outcome, such as correcting configuration, releasing a blocked resource, narrowing the test, increasing an evidenced insufficient timeout, or obtaining additional diagnostics.
11. Where practical, agents SHOULD prefer command-level or test-framework timeout mechanisms so that control returns automatically rather than relying solely on observation. Timeout values MUST remain appropriate to the operation and MUST NOT be inflated merely to avoid diagnosing a stall.
12. If a required verification cannot complete within a reasonable bounded period, the agent MUST stop the affected implementation step and report: the command, elapsed/no-progress interval, last meaningful progress, captured diagnostics, termination action, known state, unresolved uncertainty and recommended next diagnostic action.

#### Result semantics

The following outcomes are distinct and MUST NOT be collapsed:

- **PASS** — the required behaviour was positively established.
- **FAIL** — the required behaviour was executed and a failure was positively established.
- **TIMEOUT** — the allowed execution/wait period expired before completion was established.
- **STALLED** — the operation remained active but ceased to demonstrate meaningful progress.
- **UNRESOLVED** — available evidence is insufficient to determine the semantic outcome safely.

A timeout or stall is diagnostic evidence, not proof that the implementation is incorrect.

> **Wait long enough to establish expected behaviour; never wait indefinitely. Unknown or stalled is a result to investigate, not a reason to keep waiting.**

## 6. Junie Plans, Reports and Implementation Sequencing

Files under `.junie/plans/` and `.junie/reports/` are working and historical
execution artefacts. They are NOT sources of architectural authority.

### 6.1 Authority hierarchy

A Junie plan MUST be interpreted against, in order of authority:

1. `docs/markdown/governance/architectural-axioms.md`
2. this `AGENTS.md`
3. applicable accepted Architecture Decision Records
4. applicable requirements and design contracts
5. `docs/implementation/harmonia-convergence-runtime-integration-plan.md` for
   convergence/runtime implementation sequencing
6. the task-specific Junie plan

The convergence/runtime implementation plan is authoritative for the **order
and scope of implementation activities**, but it does not override the
Architectural Axioms, this `AGENTS.md`, accepted ADRs, or applicable design
contracts.

Where an existing Junie plan or report conflicts with a higher-authority
source, the higher-authority source prevails.

Historical plans and reports MUST NOT be used as evidence that an
architectural pattern remains valid merely because it was previously
implemented, proposed, or approved.

Before executing a material architectural plan, Junie MUST identify the
applicable Architectural Axioms and report any conflict between the proposed
work, the existing implementation, and those axioms.

### 6.2 Master convergence and runtime implementation plan

The repository-wide master implementation sequence for the current Harmonia
convergence and runtime-integration programme is:

    docs/implementation/harmonia-convergence-runtime-integration-plan.md

Junie MUST consult that document before planning or implementing work that
falls within the convergence/runtime programme.

The master plan combines the previously separate architecture-convergence and
Docker/runtime/deployment activity streams into one ordered programme:

1. Stable Docker Runtime Baseline
2. Distributed Authoritative Path
3. Governed Access
4. Application Migration
5. Authoritative Search
6. MicroK8s Runtime
7. Remaining Convergence Findings
8. Convergence Closure

For work governed by that plan, Junie MUST:

1. identify the current milestone and exact step before proposing changes;
2. inspect the repository for evidence of the current implementation state;
3. preserve accepted outcomes from completed milestones and steps;
4. implement only the smallest bounded change required by the current step;
5. add focused verification/conformance evidence appropriate to that step;
6. report discovered prerequisites, conflicts or missing architectural
   decisions rather than silently inventing a solution;
7. stop at the defined step boundary; and
8. NOT commence a later step or milestone without explicit instruction or
   approval.

The operating principle is:

> **One plan. One current milestone. One next step.**

### 6.3 Scope discipline

A future milestone MUST NOT be used to justify speculative implementation in
the current milestone.

In particular, unless the current approved step explicitly requires it, Junie
MUST NOT:

- introduce MicroK8s concerns before the Docker/component topology is stable;
- implement authoritative search while completing authoritative point access;
- migrate application consumers before governed access is production-ready;
- invent transport authentication inside application code;
- treat Mneme/Infinispan active-state content as authoritative persistence;
- expose Mnemosyne persistence implementation as an application-access path;
- introduce physical DELETE as managed-information lifecycle semantics;
- introduce a new runtime service, protocol, process or network boundary
  without explicit architectural justification; or
- opportunistically fix unrelated MAT findings while executing a bounded
  convergence step.

If work in the current step reveals a prerequisite belonging to another step,
Junie MUST report it and stop where necessary rather than silently broadening
scope.

### 6.4 Step completion and plan maintenance

A convergence/runtime step is not complete merely because code compiles.

The completion report SHOULD identify:

- milestone and step;
- implementation changes;
- architectural invariants affected;
- tests and verification actually executed;
- exact material test results;
- unresolved deployment or operational prerequisites;
- conformance impact, where applicable; and
- confirmation that later-step work was not commenced.

When a step completes, the master implementation plan SHOULD be updated to
record its status, material outcome, current milestone, and next step.

Material changes to the sequence or milestone intent MUST be explicit and
reviewed before subsequent implementation proceeds. Junie MUST NOT rewrite
historical milestone intent merely to make later implementation appear to have
followed the plan.

