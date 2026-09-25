# Harmonia Architecture Decisions

This document records architecture choices that constrain how Harmonia
requirements are realised. Decisions are distinct from requirements: a
requirement states what Harmonia must achieve; a decision records the
architectural approach selected to achieve or protect that outcome.

## Current Decisions

### ADR-001 --- Petasos Owns Messaging Runtime

Petasos is the standalone Harmonia messaging capability and owns the
ActiveMQ Artemis runtime. Ponos consumes Petasos messaging services and
owns workflow/work execution; it does not own broker lifecycle.

### ADR-002 --- Pylai Owns External Interface and Protocol Behaviour

Pylai owns external protocol/interface behaviour, including HL7/FHIR
ingress and egress. Domain workflow logic remains outside the protocol
boundary.

### ADR-003 --- Mnemosyne Owns Durable Application State

Mnemosyne is the durable persistence capability. Mneme/Infinispan
provides operational/cache state and must not redefine the existence of
durable governed information.

### ADR-004 --- Calliope Owns Canonical Models and Semantic Governance

Calliope owns canonical model definitions, profiles, terminology
bindings, and semantic governance used by Harmonia processing.

### ADR-005 --- Themis Owns Security Policy Decisions

Themis is the common Harmonia authorisation/policy capability.
Authentication boundaries establish trusted identity; Themis evaluates
permissions.

### ADR-006 --- FHIR R5 Is the Clinical Interoperability Boundary

The durable clinical interoperability service is represented and exposed
using HL7 FHIR R5. Proprietary source models are translated into the
governed representation rather than propagated as the enterprise
integration contract.

### ADR-007 --- Harmonia Is an Interoperability Authority, Not Universal Clinical Truth

Source systems may remain authoritative for the activities they manage.
Harmonia maintains the governed, vendor-neutral interoperability
representation and preserves provenance and Information Authority rather
than claiming universal clinical source-of-truth status.

### ADR-008 --- Iris-Clinical Begins as a Longitudinal Record Viewer

The initial Iris-Clinical capability is read-oriented. The architecture
permits selective clinical authoring later, but authoring is introduced
only where terminology, validation, security, governance, and workflow
prerequisites are satisfied.

### ADR-009 --- Information Authority Is Cross-Cutting

AUTHORITATIVE, INFORMATIONAL, and ANECDOTAL classification is a core
Harmonia concept rather than an Iris or Clinical-only feature.

### ADR-010 --- Clinical Search Must Be Authoritative-Backed

Cache contents are not the definition of the clinical record. Clinical
read/search must remain correct from durable state after cache loss or
restart.

### ADR-011 --- Agora Uses Restricted Self-Hosted Matrix/Synapse

The initial Agora collaboration environment is self-hosted, restricted,
and non-federated. Patient collaboration rooms and membership are
governed by Harmonia rather than unmanaged public federation.

### ADR-012 --- Paradeigma Is an Isolated Exemplar/Test Subsystem

Paradeigma exists to exercise Harmonia interfaces and workflows with
deterministic synthetic clinical scenarios. It is not a production
clinical dependency and must remain isolated from production
architecture.

### ADR-013 --- Kleio Owns Audit Evidence and Provenance

Kleio is the Harmonia capability responsible for immutable audit
evidence and governed provenance. Themis, Pylai, Ponos, Ergon, Iris, and
other Harmonia subsystems may produce evidence, but do not independently
own the enterprise audit model or durable audit record. Accepted audit
evidence is append-only and shall not be updated, replaced, patched,
deleted, or resurrected through ordinary application persistence
mechanisms.

### ADR-014 --- Petasos Owns Durable Processing Transition Boundaries

Petasos governs the durable transfer of work between independently
recoverable Harmonia processing activities. Ponos owns execution and
orchestration of those activities; Petasos establishes the durable
boundary at which responsibility for work is transferred. ActiveMQ
Artemis is an implementation mechanism used by Petasos and does not
itself define Harmonia processing semantics.

### ADR-015 --- Replay Is Anchored to Explicit Durable Transitions

Replay is supported only from Petasos-governed durable transition points
explicitly designated as replayable. Harmonia does not provide arbitrary
replay from internal implementation states. A deliberate replay creates
a new processing attempt linked to the original transition through
correlation, causation, and provenance; it does not modify, replace, or
impersonate the original event or processing attempt. Replay,
deterministic reprocessing, transport redelivery, and redelivery of an
established result are distinct operations and shall not be treated as
equivalent.

### ADR-016 --- Audit Is Anchored to Architecturally Significant Transitions

Kleio AuditEvents are generated for architecturally significant actions
and transitions, including acceptance or rejection of work, significant
commitment or authority transitions, external dispatch, and deliberate
replay where policy requires evidence. Internal implementation steps
such as parsing, mapping helpers, individual validation rules, and
ordinary method execution are not independently audited merely because
they occur. Audit, provenance, and replay remain distinct concepts but
may share a durable Petasos transition as their architectural anchor.

### ADR-017 --- Processing Pressure Is Expressed as Durable Backlog

Where processing demand exceeds downstream capacity, Harmonia shall
preferentially express pressure as bounded, observable, durable
Petasos/Artemis backlog rather than unbounded growth in JVM heap, worker
threads, database connections, or volatile work state. Queue depth,
oldest-transition age, processing rate, retry/replay count, dead-letter
count, and backlog drain rate form part of the operational model for
transition processing.

### ADR-018 --- Mnemosyne Defines the Authoritative Durable State Boundary

**Status:** Accepted\
**Date:** 24 September 2026

#### Context

Harmonia requires application state to remain available and recoverable
across process, node and infrastructure failures while supporting high
throughput, horizontal scaling and deployment across multiple failure
domains.

The original Mneme architecture considered using Infinispan as more than
a conventional cache. Under this model, application state would be
synchronously replicated across multiple Infinispan instances,
potentially distributed across independent sites or data centres. Each
site could then independently persist replicated state through its local
cache-store mechanism into a local Mnemosyne persistence service.

The intended benefits were:

-   high availability at the application and middleware tier rather than
    relying primarily upon database or storage-tier high availability;
-   low write latency by allowing application processing to complete
    following distributed cache acceptance rather than waiting for
    physical database persistence;
-   resilience to failure of an individual persistence store;
-   independent persistence of replicated state at multiple sites;
-   reduced dependence on expensive database replication and highly
    available storage infrastructure; and
-   eventual reconciliation of persistence stores following temporary
    database, site or network failure.

Conceptually, the distributed Mneme fabric would therefore have formed
the immediate acceptance boundary, with Mnemosyne persistence occurring
asynchronously behind it.

This approach is technically feasible. However, for replicated cache
state to constitute authoritative durable state, Harmonia would also
need to guarantee recovery following cache, process, site and network
failures. This would require Harmonia to own mechanisms for durable
outstanding-write tracking, persistence retry, restart recovery,
cross-site reconciliation, version consistency, conflict resolution,
partition handling and eventual convergence of independent persistence
stores.

The resulting capability would effectively introduce a distributed
persistence and consistency layer above the existing durable persistence
technologies.

Harmonia already provides distinct technologies appropriate to the
principal resilience requirements:

-   **Mnemosyne/PostgreSQL** provides durable application-state
    persistence;
-   **Petasos/Artemis** provides durable transfer of work between
    independently recoverable processing activities; and
-   **Mneme/Infinispan** provides distributed resource availability, coordination,
    concurrency/version management and efficient read-dominant access to
    reconstructable active state.

The additional complexity required to make Mneme itself an authoritative
persistence boundary is therefore not justified by the expected
performance, availability or infrastructure-cost benefits.

#### Decision

**Mnemosyne defines the authoritative durable application-state boundary
for Harmonia.**

Application state that requires durable persistence is not considered
durably accepted solely because it has been written to, replicated by,
or is currently available from Mneme/Infinispan.

A successful Mneme cache operation does not constitute durable
application-state acceptance.

State requiring durable persistence is considered accepted when:

1.  it has been successfully committed through Mnemosyne to its
    authoritative durable persistence mechanism; or
2.  responsibility for further processing has been successfully
    transferred across an explicitly defined durable Petasos transition,
    where the state necessary to complete or reconstruct that processing
    is durably recoverable.

Petasos/Artemis may therefore provide a durable **work acceptance**
boundary, but does not replace Mnemosyne as the authoritative repository
for committed application state.

Mneme/Infinispan will remain a distributed caching capability. State
held by Mneme must be reconstructable from authoritative durable state
or from an explicitly defined durable processing transition where
reconstruction is part of that transition's contract.

Volatile or cached state must never be the sole recoverable
representation of application state or work that Harmonia has
acknowledged as durably accepted.

#### Architectural Responsibilities

  ----------------------------------------------------------------------------
Subsystem         Responsibility      Primary           Durability Role
Technology
  ----------------- ------------------- ----------------- --------------------
**Mneme**         Distributed caching Infinispan        Non-authoritative,
and efficient state                   reconstructable
access                                state

**Mnemosyne**     Durable             PostgreSQL        Authoritative
application-state                     durable application
persistence                           state

**Petasos**       Transfer of work    ActiveMQ Artemis  Durable processing
between                               transitions
independently                         
recoverable                           
processing                            
activities

**Kleio**         Immutable audit and PostgreSQL        Durable immutable
provenance evidence                   evidence
  ----------------------------------------------------------------------------

These responsibilities are deliberately distinct. Harmonia must not
introduce implicit overlap between cache availability, processing
durability and authoritative persistence.

#### Consequences

The decision simplifies Harmonia's failure and recovery semantics.

##### Positive consequences

-   Harmonia does not need to implement its own distributed database
    consistency and convergence mechanisms.
-   Persistence failure can be represented explicitly rather than hidden
    behind successful cache operations.
-   Database recovery and replication can use established PostgreSQL
    capabilities appropriate to each deployment topology.
-   Petasos/Artemis can absorb processing pressure and temporary
    downstream outages as durable backlog rather than requiring volatile
    cache state to act as a persistence substitute.
-   Mneme remains independently scalable and distributable without
    carrying an authoritative durability obligation.
-   Cache contents may safely be evicted, restarted or reconstructed
    without causing loss of acknowledged authoritative state.
-   Different deployment profiles may provide different levels of
    PostgreSQL, Artemis and Infinispan redundancy without changing
    Harmonia's logical durability semantics.
-   Failure behaviour becomes easier to test and reason about.

##### Negative consequences

-   authoritative synchronous writes may incur greater latency than
    memory/cache acceptance;
-   PostgreSQL remains part of the critical path for operations
    requiring immediate durable application-state commitment;
-   deployments requiring high availability of authoritative state must
    provide an appropriate PostgreSQL HA/DR topology;
-   Infinispan replication cannot by itself be used to mask loss of the
    authoritative persistence service; and
-   some existing Harmonia persistence and cache interactions must be
    changed because they currently allow cache or JVM-local state to be
    treated as successfully persisted state.

These costs are accepted in preference to Harmonia assuming
responsibility for distributed persistence, reconciliation and
consistency.

##### Failure Semantics

The following invariant applies:

> **Accepted state must be durably recoverable.**

This does **not** imply:

> Accepted state must always have already been committed to PostgreSQL.

A durable Petasos transition may constitute acceptance of work where the
information necessary to continue processing is durably recoverable from
that transition.

However:

> **Cache presence, cache replication, JVM-local state or an
> asynchronous persistence attempt does not by itself constitute durable
> acceptance.**

Consequently:

-   failure to persist required authoritative state must be propagated
    as failure;
-   a local in-memory fallback must not convert unavailable durable
    persistence into apparent success;
-   asynchronous processing may return acceptance only after the
    corresponding durable work boundary has been established;
-   failure after a durable Petasos transition must result in
    recoverable backlog, redelivery or controlled failure rather than
    silent loss;
-   cache loss must be recoverable from Mnemosyne or another explicitly
    defined durable source; and
-   transient processing state such as a Pragma may remain volatile
    where it can be reconstructed without loss of acknowledged work.

#### Rejected Alternative --- Mneme as a Distributed Persistence Fabric

The rejected architecture would have treated replicated Infinispan state
as Harmonia's primary immediate persistence/availability boundary.

Multiple independent Mneme instances or sites would replicate state
between them. Each site would independently write replicated state
through its cache-store implementation to a local Mnemosyne persistence
service. Failure of an individual database or persistence site would not
prevent application acceptance, with failed persistence operations
subsequently retried and stores gradually reconciled.

This model was rejected **not because it is technically invalid**, but
because completing it safely would require Harmonia to provide
substantial additional distributed-systems capability, including:

-   durable tracking of outstanding persistence operations;
-   recovery of pending writes following process or site restart;
-   persistence retry and backoff;
-   store convergence monitoring;
-   version and ordering semantics;
-   duplicate and idempotency handling;
-   conflict detection and resolution;
-   network-partition behaviour;
-   reconciliation of independently persisted copies; and
-   operational tooling for detecting and repairing divergence.

These responsibilities would duplicate capabilities already available
through established database and durable-messaging technologies while
materially increasing implementation, testing and operational
complexity.

The likely reduction in persistence latency and infrastructure cost does
not justify that complexity for Harmonia's expected workload and
availability requirements.

#### Relationship to Other Decisions

This decision reinforces:

-   **ADR-003 --- Mnemosyne Owns Durable Application State**
-   **ADR-010 --- Clinical Search Must Be Authoritative-Backed**
-   **ADR-014 --- Petasos Owns Durable Processing Transition
    Boundaries**
-   **ADR-015 --- Replay Is Anchored to Explicit Durable Transitions**
-   **ADR-017 --- Processing Pressure Is Expressed as Durable Backlog**

It also establishes the architectural basis for the current
implementation sequence:

``` text
Task 07   Remove volatile persistence fallback
             |
Task 08   Establish authoritative Clinical writes
             |
Task 09   Establish cache-aside point reads
             |
Task 10   Establish authoritative Clinical search
```

Task 07 must remove cases where cache or JVM-local state is incorrectly
treated as durable acceptance without prematurely redesigning the
authoritative Clinical write path that is addressed by Task 08.

In alignment with ADR-020, Task 08 establishes authoritative CREATE and UPDATE
write semantics, treating domain-appropriate lifecycle changes as conditional
updates, without providing a physical authoritative DELETE path.

#### Architectural Principle

> **Petasos makes work recoverable.**\
> **Mnemosyne makes application state durable.**\
> **Mneme makes active state available, coordinated and fast.**\
> **Kleio makes evidence permanent.**

Harmonia will prefer these explicit responsibilities over introducing a
distributed persistence protocol spanning them.

### ADR-019 — Mneme Owns Distributed Resource Access and Coordination

**Status:** Accepted  
**Date:** 25 September 2026

#### Context

Harmonia processes resources and data objects through validation, transformation,
workflow, integration and persistence activities. Access to these objects is
expected to be uneven, highly repetitive and strongly read-dominant.

At any point in time, only a comparatively small subset of the total persisted
resource population is expected to be actively involved in processing. That
active subset may be read repeatedly by different Harmonia components while it
is validated, referenced, transformed, enriched, coordinated or progressed
through workflows. A useful design expectation is approximately a 10:1
read-to-write ratio for this active working set; the ratio is indicative rather
than a contractual limit.

Repeated authoritative retrieval from Mnemosyne for every such interaction
would unnecessarily increase persistence traffic, database connection
utilisation and processing latency.

Mneme was introduced to provide more than a convenient local cache. Using
Infinispan and its distributed access mechanisms, Mneme is intended to provide
a common low-latency resource-access and coordination capability across the
Harmonia ecosystem.

The principal intended capabilities are:

1. **Distributed resource availability** — supported active resources and data
   objects can be accessed by Harmonia components regardless of which runtime
   node originally obtained, created or cached their working representation.
2. **Distributed concurrency and version coordination** — concurrent access to
   a given active resource or data object is coordinated through Mneme rather
   than independently by each client or JVM. Client code should not need to
   recreate distributed concurrency/version-management machinery.
3. **Read-dominant performance** — frequently read active state can be accessed
   at low latency without repeated database retrieval, while the comparatively
   smaller number of authoritative writes are committed through Mnemosyne.
4. **Efficient internal distribution** — native distributed-cache access and
   replication can be used within Harmonia without unnecessarily introducing
   HTTP network boundaries between internal components.

The original Mneme design also contemplated **distributed durability**:
replicated Infinispan state could act as an immediate durable acceptance fabric
with persistence occurring behind it. ADR-018 deliberately rejects that
responsibility because the required recovery, reconciliation, partition,
convergence and durable outstanding-write semantics are not justified by the
expected benefit.

Rejecting distributed durability does not reduce Mneme to an optional
best-effort cache. Its distributed availability, coordination and performance
semantics remain deliberate architectural responsibilities.

#### Decision

> **Mneme owns Harmonia's distributed resource access and coordination
> capability for active application state.**

Mneme will use Infinispan to expose a distributed, low-latency and
reconstructable representation of selected resources and data objects being
actively used by Harmonia.

Mneme provides four defining characteristics:

- **distributed availability** — supported active state is accessible across
  Harmonia runtime instances through the distributed Mneme capability;
- **distributed coordination** — Mneme provides the common coordination point
  for concurrent access and working versions of an individual active resource
  or data object;
- **read-dominant performance** — Mneme absorbs high-volume, repetitive and
  potentially jittery reads against the active working set, reducing repeated
  access to authoritative persistence; and
- **reconstructability** — Mneme state is non-authoritative and may be rebuilt
  from authoritative durable state or another explicitly defined durable
  source.

Mneme does **not** own distributed durability.

A successful Mneme operation, distributed replication, concurrency decision or
working version does not by itself constitute authoritative durable acceptance.
Authoritative application-state persistence and the final persisted version
remain the responsibility of Mnemosyne.

This creates a deliberate distinction:

> **Mneme coordinates active distributed state; Mnemosyne commits authoritative
> durable state.**

The exact write-boundary protocol between Mneme coordination and Mnemosyne
authoritative persistence is governed by the relevant write architecture and
must preserve authoritative concurrency/version correctness at commit.

#### Distributed Resource Availability

Where a resource or data object is supported by Mneme, client code should be
able to obtain its active representation through the Mneme capability without
depending on the JVM or processing node that originally loaded or produced it.

Mneme therefore represents a system-wide resource-access capability, not a
collection of unrelated per-process caches.

The logical expectation is:

```text
 Pylai      Ponos      Ergon      Praxis      other Harmonia components
   \          |          |          |                    /
    \         |          |          |                   /
     +--------+----------+----------+------------------+
                              |
                              v
                         +---------+
                         |  Mneme  |
                         |Infinispan|
                         +----+----+
                              |
                    authoritative state
                              |
                              v
                         +---------+
                         |Mnemosyne|
                         +---------+
```

#### Distributed Concurrency and Version Coordination

Mneme is the distributed coordination point for concurrent access to an active
resource or data object.

Where multiple Harmonia components or runtime nodes operate on the same active
object, they must not independently establish incompatible local working
versions merely because they execute in different JVMs.

Mneme should use appropriate Infinispan concurrency facilities so that the
coordination mechanism is substantially transparent to ordinary client code.
The implementation may use versioned or conditional operations, atomic
replacement, locking or other appropriate Infinispan mechanisms, but those
mechanisms are implementation choices beneath the Mneme contract.

This responsibility does not make Mneme the final authority for a durable
write. Mnemosyne must still enforce the authoritative persistence transaction
and persisted-version semantics.

#### Read-Dominant Active Working Set

Mneme is intentionally optimised around a comparatively small active subset of
the total persisted resource population.

The expected access pattern is strongly read-dominant, with approximately ten
reads for each write being a useful design assumption rather than a fixed
service-level guarantee.

Typical processing may therefore involve repeated reads, validation, reference
lookups, workflow evaluation and transformation against Mneme before a smaller
number of create or update operations require authoritative persistence.

Mneme exists to prevent these repeated reads from degenerating into continuous
database lookups while still allowing Mnemosyne to remain the authoritative
durability boundary.

#### Process-Local Fallback

Where a component requires Mneme semantics:

> **Loss of Mneme must be visible as loss of distributed resource availability
> or coordination. It must not silently degrade into process-local application
> state.**

Production services must therefore not use `ConcurrentHashMap`, local
collections or equivalent process-memory structures as transparent substitutes
for Mneme-backed application state.

Such a fallback would lose not only distributed caching, but also Mneme's
resource-availability and concurrency/version-coordination semantics.

A component may respond to Mneme unavailability by failing the operation, or by
using an explicitly designed degraded path where the architecture permits one.
A degraded path must be deliberate and visible; it must not establish an
alternative JVM-local Mneme.

This decision does **not** prohibit ordinary process-local memory. Local
collections remain appropriate for temporary computation, immutable
configuration, logger caches, local registrations, implementation metadata and
other genuinely process-local concerns.

> **Process-local state is permitted. Process-local substitution for Mneme is
> not.**

#### Relationship Between Mneme and Mnemosyne

For read-dominant access, the intended model is:

```text
Request
   |
   v
 Mneme ---- HIT ----------------------> Return
   |
  MISS
   |
   v
Mnemosyne
   |
   +----> Populate/refresh Mneme
   |
   +----> Return
```

For authoritative writes:

```text
active distributed state / coordination
                 |
                 v
               Mneme
                 |
          authoritative write
                 |
                 v
             Mnemosyne
                 |
          durable commit/version
```

The precise authoritative Clinical write path is addressed separately and must
not be inferred solely from this diagram.

There is deliberately no transparent substitution of the form:

```text
Mneme unavailable
       |
       v
ConcurrentHashMap
       |
       v
Pretend distributed semantics still exist
```

#### Consequences

##### Positive Consequences

- supported active resources are available through a common distributed access
  capability across Harmonia runtime nodes;
- concurrency and working-version coordination are not independently reinvented
  by each client or process;
- high-volume and jittery reads can be absorbed without continuously querying
  Mnemosyne;
- native distributed-cache access can minimise unnecessary internal HTTP
  latency and protocol overhead;
- Mneme can be scaled and distributed independently of authoritative
  persistence;
- cache contents remain reconstructable and may be evicted or restarted without
  redefining durable truth; and
- the distinction between distributed coordination and durable authority is
  explicit.

##### Negative Consequences

- Mneme/Infinispan remains an important runtime dependency even though it is not
  a durability boundary;
- loss of Mneme may make distributed resource access or coordination
  unavailable until restored or an explicitly designed degraded path is used;
- concurrency/version behaviour must be designed and tested deliberately rather
  than assumed from cache replication alone;
- authoritative write paths must correctly bridge Mneme coordination and
  Mnemosyne persisted-version semantics; and
- appropriate monitoring of Mneme availability, topology and concurrency
  behaviour is operationally important.

These costs are accepted because distributed availability, coordination and
read-dominant access are intentional Harmonia capabilities rather than
incidental cache optimisations.

#### Rejected Alternative — Mneme as Distributed Durability

ADR-018 records the rejected distributed-durability model. The idea remains
technically attractive: replicated Infinispan state could form an immediate
acceptance fabric and allow persistence stores to converge behind it.

It is rejected for Harmonia because doing it safely would require durable
outstanding-write tracking, restart recovery, partition semantics,
reconciliation, conflict resolution, convergence monitoring and associated
operational tooling. Those costs are not justified by the expected workload and
availability benefit.

This rejection applies specifically to **durability**. It does not reject
Mneme's distributed availability, coordination, version-management or
read-performance responsibilities.

#### Rejected Alternative — Process-Local Cache Fallback

A JVM-local fallback may make an individual process appear available while
silently destroying Mneme's distributed semantics.

For example:

```text
Node A                 Node B                 Node C

Person/123 v7          Person/123 v6          Person/123 absent
Task/456 PROCESSING    Task/456 RECEIVED      Task/456 absent
```

None of these copies need be authoritative for the situation to be incorrect.
The nodes no longer share the resource availability or coordination model that
Mneme exists to provide.

The apparent availability benefit therefore does not justify process-local
substitution.

#### Relationship to Other Decisions

This decision complements:

- **ADR-003 — Mnemosyne Owns Durable Application State**
- **ADR-010 — Clinical Search Must Be Authoritative-Backed**
- **ADR-014 — Petasos Owns Durable Processing Transition Boundaries**
- **ADR-017 — Processing Pressure Is Expressed as Durable Backlog**
- **ADR-018 — Mnemosyne Defines the Authoritative Durable State Boundary**

Together:

| Subsystem | Question it answers |
|---|---|
| **Mnemosyne** | Where does authoritative application state live and what version was durably committed? |
| **Petasos** | Will accepted work survive until processing can continue? |
| **Mneme** | How is active state made available, coordinated and read efficiently across Harmonia? |
| **Kleio** | What durable evidence exists of significant actions and transitions? |

#### Architectural Principle

> **Mneme makes active state available, coordinated and fast across Harmonia —
> reconstructable, never authoritative, and never secretly local.**

### ADR-020 — Governed Information Uses Lifecycle State Rather Than Physical Deletion

**Status:** Accepted  
**Date:** 25 September 2026

#### Context

In healthcare integration and interoperability environments, persisted resources
(including patients, encounters, practitioners, clinical observations, documents,
and processing tasks) represent clinical, legal, regulatory, and evidentiary
facts.

Traditional persistence architectures often treat physical deletion (`DELETE` in
SQL or HTTP `DELETE` in REST APIs) as a standard CRUD primitive that permanently
removes records from durable storage. In a distributed health integration
platform such as Harmonia, physical deletion introduces severe operational,
clinical, and governance failures:

1. **Loss of Clinical and Legal History** — destroying persisted records erases
   the longitudinal record of care, compromises traceability, and violates
   healthcare data retention and evidentiary standards.
2. **Referential Integrity Destruction** — physical deletion of an entity breaks
   references in downstream systems, clinical documents, immutable audit records,
   and related resources across the enterprise.
3. **Audit and Provenance Invalidation** — accepted audit evidence governed by
   Kleio (ADR-013) must point to verifiable historical and provenance contexts;
   deleting the underlying entities destroys the audit graph.
4. **Distributed State Ambiguity** — in an ecosystem combining distributed
   caching (Mneme) and durable persistence (Mnemosyne), physical deletion creates
   unsolvable race conditions, stale read anomalies, and ambiguity regarding
   whether an absent cache record was deleted, evicted, or never created.

In clinical and administrative domains, when an entity is retired, deactivated,
cancelled, completed, entered in error, or otherwise logically removed, this
action constitutes a domain-significant lifecycle transition rather than data
destruction.

Furthermore, lower-level distributed cache capabilities (such as Infinispan's
version-aware conditional removal primitive `removeWithVersion()` characterized in
laboratory evaluations) must not be conflated with governed resource deletion.
Similarly, long-term data archival and regulatory purge have sometimes been
improperly assumed to be standard in-band application persistence features.

A definitive architectural decision is required to establish Harmonia's
platform invariant regarding governed persisted information, the write semantics
for authoritative Clinical operations (Task 08), the boundary between cache
eviction and resource state, and the formal scope of archival and purge.

#### Decision

> **Harmonia does not provide physical DELETE semantics for governed persisted
> information. Logical deletion is a domain-appropriate lifecycle transition and
> therefore an authoritative UPDATE.**

Harmonia establishes the following normative platform guardrail:

> **Harmonia SHALL NOT expose physical deletion as a normal operation for
> governed persisted information. Logical deletion SHALL be represented as a
> domain-appropriate lifecycle transition and processed as a concurrency-controlled
> authoritative update. Mneme cache eviction SHALL NOT be interpreted as
> deletion of authoritative state. Archival, retention-based purge and physical
> disposal are outside the current Harmonia framework scope.**

#### Four Core Distinctions

To ensure conceptual clarity across architecture, design, and implementation,
Harmonia strictly distinguishes four operational concepts:

1. **Resource Lifecycle Transition**:
   An authoritative state change in Mnemosyne reflecting domain progression
   (illustrative examples include transitioning to `inactive`, `entered-in-error`,
   `cancelled`, `completed`, or `superseded`). A lifecycle transition is committed
   to Mnemosyne as a concurrency-controlled authoritative `UPDATE`, subject to
   version advancement, provenance capture, and Kleio audit logging.
2. **Mneme Eviction**:
   The removal, invalidation, or expiration of a reconstructable active-state entry
   from the distributed cache (Mneme/Infinispan) according to cache capacity,
   TTL/idle policies, explicit invalidation, or cache-clearing operations. Cache
   eviction affects only the non-authoritative active cache working set; it does
   NOT alter, mutate, or delete authoritative durable state in Mnemosyne.
3. **Archival**:
   The long-term migration, cold-storage management, or offline tiering of
   historical records. Archival is formally designated as outside the scope of
   the current Harmonia framework and must be managed by external operational
   and database lifecycle procedures.
4. **Purge / Physical Disposal**:
   The physical destruction, cryptographic erasure, or permanent scrubbing of
   persisted records under statutory data-retention schedules or legal mandates.
   Retention-based purge and physical disposal are formally designated as outside
   the scope of the current Harmonia framework.

#### Write Semantics and Task 08 Consequence

Harmonia categorizes authoritative write operations into distinct primitives:

- **CREATE**: Establishes a new authoritative resource in Mnemosyne, establishes
  initial durable state and provenance, and participates in the version and
  coordination protocol defined by Task 08.
- **UPDATE**: Modifies existing authoritative resource attributes or lifecycle
  state in Mnemosyne, advances authoritative state under the version and
  concurrency control protocol defined by Task 08, and coordinates state with
  Mneme.
- **LIFECYCLE TRANSITION**: A semantic specialization of **UPDATE** that alters
  lifecycle attributes (illustrative examples include domain-appropriate fields
  such as FHIR `status`, `active`, or `verificationStatus`). It executes
  strictly through the authoritative `UPDATE` write pipeline.
- **DELETE (Physical)**: **Not supported.** Harmonia does not provide an
  authoritative physical delete path or database `DELETE` protocol for governed
  persisted information.

Implementation sequence Task 08 ("Establish authoritative Clinical writes") is
consequently scoped to design and implement authoritative `CREATE` and `UPDATE`
write protocols between Mneme coordination and Mnemosyne persistence. Task 08
does not design, implement, or expose a physical authoritative `DELETE` path.

#### Concurrency and Distributed Cache Semantics

All lifecycle transitions execute through concurrency-controlled `UPDATE`
protocols in Mnemosyne:

- A lifecycle transition participates in the same concurrency-control and
  conflict-rejection mechanisms as any other authoritative update in Mnemosyne.
  Stale updates that conflict with committed durable state are rejected
  according to the concurrency protocol established in Task 08.
- Invalid lifecycle state transitions (such as attempting an illegal transition
  on an already terminal resource) are rejected by domain validation rules.
- In Mneme, Infinispan's `removeWithVersion()` is a version-aware conditional
  removal capability that Harmonia may use for reconstructable cache management
  and cluster coordination. It operates strictly within the non-authoritative
  active cache working set and does not define or execute governed resource
  deletion semantics.

#### FHIR and Domain Lifecycle Semantics

At the protocol and API surface:

- External protocol interactions conceptually mapped to deletion (such as HTTP
  `DELETE` in FHIR REST APIs) do not execute physical database deletions in
  Mnemosyne.
- Instead, inbound protocol adapters and workflow pipelines translate such
  interactions into domain-appropriate lifecycle updates (illustrative examples
  include updating `active = false` or setting `status = 'entered-in-error'`),
  committing an authoritative update with associated provenance and audit
  evidence.
- Where domain or protocol specifications dictate specific responses (such as
  HTTP `410 Gone`) for retired or entered-in-error resources, the response is
  determined by evaluating governed lifecycle attributes in authoritative
  persistence, not by the physical absence of a record.

#### Audit and Provenance

In conformance with ADR-013 (Kleio Owns Audit Evidence and Provenance):

- Accepted audit evidence in Kleio is append-only, permanent, and immutable.
- Every lifecycle transition produces immutable Kleio `AuditEvent` records
  capturing the actor, authorization context, transition timestamp, prior state,
  and resulting state.
- Because persisted entities remain permanently in Mnemosyne, audit trails and
  provenance graphs remain intact and verifiable throughout the system's life.

#### Consequences

##### Positive Consequences

- **Information Preservation**: Clinical and operational history is preserved
  without loss of longitudinal record integrity.
- **Referential Integrity**: Cross-resource references, patient links, and
  downstream integrations remain structurally sound without dangling foreign keys.
- **Audit Compliance**: Kleio audit evidence and provenance graphs remain
  anchored to permanent, verifiable historical records.
- **Concurrency Safety**: Lifecycle transitions are protected by concurrency
  control and version checks, preventing lost updates and race conditions.
- **Clear Architectural Boundaries**: Distinguishes non-authoritative Mneme
  cache eviction from authoritative Mnemosyne state transitions, and excludes
  complex archival/purge engines from platform scope.

##### Negative Consequences

- **Monotonic Storage Growth**: Persistent database tables grow monotonically,
  requiring operational database capacity monitoring and storage planning.
- **Query Filtering Discipline**: Search queries and read pipelines must
  consistently filter on domain lifecycle state (e.g. `active = true`,
  `status != 'entered-in-error'`) to prevent retired records from appearing in
  active clinical queries.
- **API Mapping Rigor**: Inbound protocol gateways must carefully map incoming
  deletion requests to domain-specific lifecycle state changes.

#### Rejected Alternatives

- **Physical DELETE as Persistence Primitive**: Rejected because physical
  deletion destroys clinical history, invalidates foreign keys, breaks audit
  chains, and creates severe synchronization failures across distributed nodes.
- **Universal Generic Soft-Delete Flag (`is_deleted = true`)**: Rejected as the
  primary mechanism for domain lifecycle governance in favor of domain-native
  lifecycle attributes (e.g., FHIR `status`, `active`, `verificationStatus`,
  `period.end`). Generic boolean flags obscure domain semantics, fail to capture
  clinical context or reason for retirement, and complicate state validation.
  Any database-level columns (such as `is_deleted`) are implementation artefacts
  and do not replace domain-level lifecycle representation.
- **In-Band Platform Purge and Archival Subsystem**: Rejected for the current
  Harmonia framework. Implementing compliant multi-jurisdictional data retention,
  cold-storage migration, and tombstone synchronization would add significant
  operational complexity not justified by the core integration mission.

#### Relationship to Other Decisions

This decision complements and reinforces:

- **ADR-003 — Mnemosyne Owns Durable Application State**
- **ADR-006 — FHIR R5 Is the Clinical Interoperability Boundary**
- **ADR-010 — Clinical Search Must Be Authoritative-Backed**
- **ADR-013 — Kleio Owns Audit Evidence and Provenance**
- **ADR-018 — Mnemosyne Defines the Authoritative Durable State Boundary**
- **ADR-019 — Mneme Owns Distributed Resource Access and Coordination**

#### Architectural Principle

> **Governed information progresses through lifecycle states; it is never erased.**\
> **State changes are authoritative updates; cache evictions are not deletions; archival and purge belong to external governance.**

