# Harmonia Convergence and Runtime Integration Plan

**Status:** Active\
**Purpose:** Master implementation plan\
**Target location:** `harmonia/docs/implementation/`

------------------------------------------------------------------------

## 1. Purpose

This document is the master implementation plan for the Harmonia
convergence and runtime-integration activities.

It combines the previously separate architecture/conformance convergence
stream and Docker/runtime/deployment stream into a **single ordered
programme of work**.

The working rule is:

> **One plan. One current milestone. One next step.**

Each milestone may contain multiple bounded implementation steps, but
milestones and steps should be undertaken in order unless an explicit
architectural decision records why the sequence must change.

Junie plans and implementation tasks should reference this document and
identify the milestone and step being performed.

The target outcome is:

> **Harmonia's governed information path operates end-to-end through the
> intended subsystem boundaries, first as reproducible Docker containers
> and then under MicroK8s, with applications unable to bypass Mneme and
> Mnemosyne's authoritative-state boundary.**

------------------------------------------------------------------------

## 2. Architectural Baseline

### 2.1 Responsibilities

-   **Themis** controls who may act.
-   **Calliope** governs what information means and information
    authority.
-   **Mneme** owns application-facing access to managed information and
    the distributed active-state representation, observation,
    coordination and convergence required to use it safely.
-   **Mnemosyne** owns the authoritative durable representation of
    managed information and establishes authoritative version
    progression.
-   **Petasos** makes distributed work recoverable.
-   **Kleio** preserves evidence.
-   **Pylai** makes health information interoperable.

The core rule is:

> **Mneme manages active use; Mnemosyne establishes durable truth. Mneme
> may reject a progression before persistence, but only Mnemosyne can
> establish a new authoritative state. After authoritative commit, Mneme
> converges its active representation to that state.**

### 2.2 Machinery

Harmonia owns the information-management architecture. HAPI FHIR,
Infinispan, PostgreSQL, Artemis, Docker and Kubernetes/MicroK8s are
machinery used to implement it.

> **Use the machinery. Own the semantics.**

The preferred runtime principle remains:

> **Many modules; few processes. Many explicit boundaries; few network
> boundaries. Rich domain model; boring infrastructure.**

### 2.3 Version domains

Keep these distinct:

1.  FHIR `meta.versionId`
2.  HTTP `ETag`
3.  Mneme/Infinispan active-state token
4.  Mnemosyne authoritative version

No implementation should assume equivalence without an explicit adapter
mapping.

### 2.4 Lifecycle

Normal physical deletion is not an information lifecycle mechanism.
Lifecycle transitions are authoritative updates where governed domain
semantics define them.

Mneme cache eviction is not authoritative deletion.

------------------------------------------------------------------------

## 3. Accepted Starting Baseline

The following work predates this consolidated plan and is accepted
baseline.

### 3.1 Governed contracts --- COMPLETE

Established:

-   `GovernedReader`
-   `GovernedWriter`
-   `GovernedRead`
-   `GovernedAccess`
-   `ResourceKey`
-   associated Calliope boundary validation

The governed contract exposes no physical DELETE/REMOVE/PURGE operation.

### 3.2 Mnemosyne authoritative persistence --- COMPLETE / CONFORMANT

Completed:

-   HAPI FHIR JPA runtime activation
-   `AuthoritativePersistencePort`
-   `HapiJpaAuthoritativePersistenceAdapter`
-   authoritative point READ
-   CREATE-if-absent
-   UPDATE-if-expected-authoritative-predecessor
-   PostgreSQL concurrency verification
-   authoritative version progression verification
-   architecture tests protecting the persistence boundary

CREATE concurrency permits exactly one authoritative creator.

UPDATE concurrency permits exactly one progression from predecessor `N`
to `N+1`.

### 3.3 Mnemosyne authoritative HTTP boundary --- COMPLETE / CONFORMANT

Implemented internal synchronous authoritative-state operations for:

-   READ
-   CREATE-if-absent
-   UPDATE-if-expected-predecessor

The boundary preserves:

-   `Committed`
-   `Conflict`
-   `NotCommitted`
-   `OutcomeUnknown`

It exposes neither DELETE nor SEARCH.

Transport identity validation is strict. HTTP conditional/version
representation is explicitly mapped to Harmonia authoritative-version
semantics.

The endpoint remains fail-closed until deployment/runtime infrastructure
supplies trustworthy authenticated service identity. Themis
authorization occurs only after trusted identity has been established.

### 3.4 Runtime work --- IN PROGRESS

Mneme is operating within the Docker work on `harmonia-srv`.

That work is now part of this master plan rather than a separate
activity stream.

------------------------------------------------------------------------

# 4. Master Sequence

``` text
M1  STABLE DOCKER BASELINE
 |
 v
M2  DISTRIBUTED AUTHORITATIVE PATH
 |
 v
M3  GOVERNED ACCESS
 |
 v
M4  APPLICATION MIGRATION
 |
 v
M5  AUTHORITATIVE SEARCH
 |
 v
M6  MICROK8S
 |
 v
M7  REMAINING CONVERGENCE
 |
 v
M8  CONVERGENCE CLOSURE
```

A later milestone should not be used to bypass an unresolved
prerequisite in an earlier milestone.

------------------------------------------------------------------------

# 5. M1 --- Stable Docker Runtime Baseline

## Objective

Finish and consolidate the Docker/server work already underway before
introducing another orchestration layer.

## Steps

### M1.1 Complete current Docker fixes

Complete outstanding Docker/server corrections on `harmonia-srv`.

### M1.2 Verify Mneme runtime

Verify image construction, configuration, startup/shutdown, health,
networking and restart behaviour.

### M1.3 Verify Infinispan integration

Verify Mneme connectivity and intended active-state behaviour.
Infinispan remains active-state machinery, not authoritative
persistence.

### M1.4 Establish/verify local image registry

Provide a predictable image build/tag/publish/use workflow suitable for
Docker and later MicroK8s.

### M1.5 Stabilise networking and configuration

Establish predictable service naming, DNS/networking, ports,
configuration injection and environment separation. Remove inappropriate
developer-workstation or `localhost` assumptions.

### M1.6 Verify persistent infrastructure storage

Verify volumes for infrastructure requiring durable storage.

### M1.7 Establish Docker Compose topology

Maintain a reproducible Docker Compose representation of the
development/runtime topology. Compose remains the simpler development
and diagnostic environment after MicroK8s is introduced.

### M1.8 Document baseline

Document topology, images, networks, volumes, configuration,
startup/shutdown and verification commands.

## Exit criterion

> **Mneme and its required infrastructure start reproducibly under
> Docker, use known networking/configuration conventions, and recover
> predictably after restart.**

------------------------------------------------------------------------

# 6. M2 --- Distributed Authoritative Path

## Objective

Connect Mneme to the already-implemented Mnemosyne authoritative
boundary across a genuine process/network boundary.

The completed former convergence increments 3.1--3.3 are baseline and
should not be redesigned without evidence of a defect.

## Steps

### M2.1 Mneme authoritative HTTP client

Implement the Mneme-side client for:

-   READ
-   CREATE-if-absent
-   UPDATE-if-expected-predecessor

Preserve `Committed`, `Conflict`, `NotCommitted` and `OutcomeUnknown`.

Failure classification:

``` text
failure known to occur before request transmission
    -> NotCommitted

request may have reached Mnemosyne but the authoritative
response cannot be established
    -> OutcomeUnknown
```

`OutcomeUnknown` must not be blindly retried.

Do not invent service authentication inside the client.

### M2.2 Containerise/deploy Mnemosyne

Prove:

``` text
Mneme container
      |
      | internal HTTP
      v
Mnemosyne container
      |
      v
HAPI FHIR JPA
      |
      v
PostgreSQL
```

No shared-JVM or `localhost` shortcut should defeat the intended runtime
boundary.

### M2.3 Establish service identity

Resolve the deployment-level identity mechanism for Mneme -\> Mnemosyne:

``` text
transport authentication
        |
        v
trusted service identity
        |
        v
ThemisPrincipal
        |
        v
Themis authorization
```

Record the selected mechanism explicitly as an architecture/deployment
decision.

### M2.4 Distributed authoritative-path verification

Verify actual Docker-network behaviour for CREATE, READ, UPDATE, CREATE
collision, stale UPDATE, Mnemosyne unavailability, ambiguous outcome
where practical, and fail-closed security.

## Exit criterion

> **Mneme securely invokes Mnemosyne's authoritative point-access
> boundary across a genuine process/network boundary with correct result
> and failure semantics.**

------------------------------------------------------------------------

# 7. M3 --- Governed Access

## Objective

Join the distributed authoritative path to the governed contracts so
`GovernedAccess` becomes the production information-access path.

## Steps

### M3.1 `DefaultGovernedReader`

Implement the production point-read path through Mneme:

``` text
Application
     |
     v
GovernedReader
     |
     +---- active-state context
     |
     +---- authoritative point access
                 |
                 v
              Mnemosyne
```

`GovernedRead` encapsulates active-state and authoritative-version
context without destructive mutation of the FHIR resource.

### M3.2 `DefaultGovernedWriter`

Place governed-write orchestration in Mneme:

``` text
proposed change
      |
      v
Calliope validation/governance
      |
      v
Mneme active-state coordination
      |
      v
Mnemosyne authoritative commit
      |
      v
authoritative version N+1
      |
      v
Mneme active-state convergence
```

Mneme may reject before persistence. Only Mnemosyne establishes
authoritative state. After commit, Mneme converges active state.

### M3.3 Failure/recovery verification

Verify active-state conflict, authoritative conflict, Mnemosyne
unavailable, ambiguous outcome, successful commit/convergence,
active-state reconstruction, and that Mneme never becomes authoritative
during Mnemosyne failure.

## Exit criterion

> **Governed READ/CREATE/UPDATE execute through Mneme using
> authoritative Mnemosyne persistence and active-state convergence.**

------------------------------------------------------------------------

# 8. M4 --- Application Migration

## Objective

Move application consumers onto governed access and remove the legacy
paths responsible for MAT-01.

## Steps

### M4.1 Provider Registry / Iris migration

Replace application use of `FhirCacheService`, raw `RemoteCache`, direct
cache mutation, local authoritative-version manufacture and persistence
assumptions with `GovernedAccess`.

### M4.2 Lifecycle migration

Remove physical-delete information semantics. Domain lifecycle changes
become governed authoritative UPDATE operations. Cache eviction remains
operational only.

### M4.3 Remove obsolete paths

After migration, remove or explicitly deprecate obsolete raw cache
mutation, `FhirRestCacheStore`, local-version generation,
physical-delete paths and persistence bypasses as applicable.

### M4.4 Mechanical enforcement

Prevent:

``` text
Application --X--> RemoteCache
Application --X--> Mnemosyne persistence implementation
Application --X--> PostgreSQL
Application --X--> HAPI DAO
```

Require:

``` text
Application
     |
     v
   Mneme
     |
     v
 Mnemosyne
```

## Exit criterion

> **Application managed-information access uses Mneme governed access
> and the legacy cache-as-persistence path can no longer be used.**

**Expected result:** MAT-01 CLOSED / CONFORMANT.

------------------------------------------------------------------------

# 9. M5 --- Authoritative Search

## Objective

Replace cache-value scanning with authoritative-backed search while
retaining Mneme as the application-facing managed-information boundary.

## Constraints

Harmonia is not a big-data provider:

-   search results are bounded;
-   sufficiently constrained multi-parameter search is preferred;
-   result scope should optimise cache/replication/system behaviour;
-   security overlays may restrict result scope; and
-   meaningful restrictions should be representable as reasoning
    associated with results.

Mneme does not reimplement a FHIR database/search engine. HAPI native
search/index machinery is used beneath Mnemosyne.

## Steps

### M5.1 Define authoritative search contract

Define the managed search contract exposed through Mneme without
unnecessary HAPI implementation leakage.

### M5.2 Implement Mnemosyne search adapter

Use HAPI FHIR native search and authoritative indexes.

### M5.3 Implement Mneme search orchestration

``` text
Application / Pylai
        |
        v
      Mneme
        |
        v
authoritative search
        |
        v
    Mnemosyne
        |
        v
 HAPI FHIR search
        |
        v
 PostgreSQL indexes
```

### M5.4 Remove cache-scan search

Eliminate `remoteCache.values()` or equivalent whole-cache scanning as
an authoritative search mechanism.

### M5.5 Distributed verification

Verify search after Mneme restart, with empty/partial active cache, and
with result/security constraints.

## Exit criterion

> **Search operates against authoritative state through Mneme/Mnemosyne
> and does not depend on cache population.**

**Expected result:** MAT-02 CLOSED / CONFORMANT.

------------------------------------------------------------------------

# 10. M6 --- MicroK8s Runtime

## Objective

Introduce orchestration after the component/container topology has been
proven.

MicroK8s becomes the distributed integration/deployment environment on
`harmonia-srv`. Docker Compose remains the simpler
development/diagnostic environment.

## Steps

### M6.1 Establish MicroK8s baseline

Configure MicroK8s, registry integration, namespaces, DNS, persistent
storage, ingress where required, health/readiness and basic operational
visibility.

### M6.2 Deploy infrastructure workloads

Evaluate and deploy PostgreSQL, Infinispan and Artemis where
appropriate. Kubernetes does not imply every infrastructure component
must run inside Kubernetes; record deliberate exceptions.

### M6.3 Deploy Harmonia workloads

Deploy Mneme and Mnemosyne, followed by application runtimes required by
the integration topology.

### M6.4 Externalise runtime configuration and service identity

Move deployment-specific configuration to appropriate orchestration
mechanisms and realise the service-identity decision from M2.3 using
production-shaped deployment machinery.

### M6.5 Repeat distributed integration verification

Repeat governed-access and authoritative-state integration tests across
independently scheduled workloads.

## Exit criterion

> **The converged Mneme -\> Mnemosyne governed information path operates
> correctly across independently deployed MicroK8s workloads.**

------------------------------------------------------------------------

# 11. M7 --- Remaining Convergence Findings

## Objective

Resolve material original conformance findings outside the central
Mneme/Mnemosyne point-access and search path.

## Steps

### M7.1 MAT-05 --- Iris/Kleio persistence coupling

Remove direct PostgreSQL/Kleio persistence coupling from
Iris/presentation-tier code.

### M7.2 MAT-07a --- Silent process-local fallback

Remove Petasos/MLLP behaviour that silently changes distributed
communication into process-local execution. Failure must be explicit or
recoverable rather than invisibly changing architectural semantics.

### M7.3 MAT-09 --- Provenance and durable evidence

Distinguish FHIR `Provenance` information/protocol content from accepted
Harmonia evidence requiring durable preservation. Durable evidence
belongs through Kleio/evidence mechanisms, not mutable/evictable cache
state.

### M7.4 MAT-10 --- Broader mechanical enforcement

Expand architecture/conformance tests around the established subsystem
boundaries. Prefer mechanical constraints over developer discipline
where practical.

## Exit criterion

> **All remaining material original convergence findings are CONFORMANT
> or explicitly documented external/deployment constraints with
> supporting evidence.**

------------------------------------------------------------------------

# 12. M8 --- Convergence Closure

## Objective

Prove the implemented system conforms as a whole and reconcile
documentation with implemented reality.

## Steps

### M8.1 Full conformance assessment

Re-evaluate MAT-01, MAT-02, MAT-03, MAT-04, MAT-05, MAT-06, MAT-07a,
MAT-07b, MAT-08, MAT-09 and MAT-10 using running implementation and
mechanical evidence.

### M8.2 Runtime resilience verification

Exercise Mneme restart, Mnemosyne restart, Infinispan restart/failure,
temporary PostgreSQL unavailability, network separation, ambiguous write
outcome, active-state reconstruction and Petasos backlog/recovery where
applicable.

### M8.3 Documentation reconciliation

Reconcile architectural axioms, ADRs, architecture/design documentation,
implementation documentation, deployment documentation and conformance
tests with the implemented architecture.

### M8.4 Final convergence report

Demonstrate for each material concern:

``` text
Axiom
  |
  v
Architectural invariant
  |
  v
Implementation mechanism
  |
  v
Mechanical enforcement
  |
  v
Runtime evidence
```

## Exit criterion

> **Harmonia's material architectural convergence findings are resolved,
> the intended distributed runtime is demonstrated, and architecture,
> implementation, enforcement and runtime evidence are mutually
> consistent.**

**Programme status:** CONVERGENCE COMPLETE.

------------------------------------------------------------------------

# 13. Current Position

## Completed foundation

-   Governed access contracts --- COMPLETE.
-   ADR-021 Mneme/Mnemosyne semantic ownership --- ACCEPTED.
-   ADR-022 Mnemosyne/HAPI JPA persistence architecture --- ACCEPTED.
-   HAPI JPA runtime activation --- COMPLETE.
-   Authoritative HAPI JPA persistence adapter --- COMPLETE.
-   PostgreSQL concurrency proof --- COMPLETE / CONFORMANT.
-   Mnemosyne internal authoritative HTTP server --- COMPLETE /
    CONFORMANT.
-   Server-side authoritative HTTP/security/conformance tests ---
    COMPLETE.
-   MAT-03 --- RESOLVED / CONFORMANT.
-   MAT-04 --- CONFORMANT.
-   MAT-06 --- CLOSED / CONFORMANT with implementation evidence.
-   MAT-07b --- CONFORMANT.
-   MAT-08 application boundary --- RESOLVED; deployment authentication
    remains an operational prerequisite.

## Current activity

Mneme is operating within the Docker work on `harmonia-srv`.

**Current milestone:** M1 --- Stable Docker Runtime Baseline.

**Next milestone after M1:** M2 --- Distributed Authoritative Path.

**Next convergence implementation step:** M2.1 --- Mneme authoritative
HTTP client.

------------------------------------------------------------------------

# 14. Working Rules for Junie

Junie should use this document as the implementation-sequencing
reference for convergence work.

For every task:

1.  identify the current milestone and step;
2.  inspect existing implementation evidence before designing;
3.  preserve accepted earlier milestone behaviour;
4.  make the smallest change necessary for the current step;
5.  add focused mechanical/conformance evidence where appropriate;
6.  report a discovered prerequisite rather than silently inventing
    architecture;
7.  stop at the defined step boundary; and
8.  do not commence the next step without review/approval.

The normal work cycle is:

``` text
Axiom / accepted architecture
        |
        v
Current milestone invariant
        |
        v
Observed implementation gap
        |
        v
Small bounded implementation
        |
        v
Focused verification
        |
        v
Conformance evidence/report
```

In particular, Junie should not:

-   introduce MicroK8s concerns before the Docker/component topology is
    stable;
-   implement authoritative search while completing point access;
-   migrate applications before governed access is production-ready;
-   invent transport authentication inside application code;
-   treat active-state cache content as authoritative persistence;
-   expose Mnemosyne persistence implementation directly to
    applications;
-   introduce physical DELETE as managed-information lifecycle
    semantics; or
-   create new runtime services/network boundaries without explicit
    architectural justification.

------------------------------------------------------------------------

# 15. Plan Maintenance

This is a living implementation plan.

When a step completes:

1.  record its status and material outcome;
2.  record any accepted architectural decision arising from it;
3.  update **Current Position**;
4.  identify the next step; and
5.  retain historical milestone intent rather than rewriting history to
    match later implementation details.

If implementation evidence requires a material plan change, make the
change explicitly and review it before subsequent implementation
proceeds.

The purpose of this plan is to prevent independent implementation
streams drifting ahead of one another.

> **One plan. One current milestone. One next step.**
