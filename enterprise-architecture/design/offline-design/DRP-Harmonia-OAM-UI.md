<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal (DRP)
# Harmonia Operations, Administration and Management UI

**DRP:** DRP-Harmonia-OAM-UI  
**Status:** Draft 003 — Consolidated Architectural Review  
**Date:** 2026-11-12  
**Solution Architect:** Mark Hunter  
**Architecture authority:** `enterprise-architecture/continuum/`

**Principal solution elements:**
- **Iris-Console** — Vue 3/TypeScript OA&M Single Page Application.
- **Iris-IM** — Iris Information Manager.
- **Iris-Agent** — embedded observability for Harmonia components.
- **Iris-Observer-Agent** — external native-API observability adapter.
- **`IrisComponentObservabilityInterface`** — the common gRPC/Protocol Buffers contract.

**Existing implementation lineage:**
- Iris Console / Iris Monitor.
- Iris BEFE.
- Iris Administration (related but distinct scope).

**Architectural disposition:** Consolidation of accepted design decisions for review and governed incorporation. No Continuum modification or implementation authorised.

---

# 1. Feature Realisation

## 1.1 Purpose

Establish the Harmonia Operations, Administration and Management solution as an integrated operational workbench, supported by an operational information management component.

The solution comprises two logically distinct responsibilities:

**Iris-Console**

The existing browser-based OA&M application, implemented using Vue 3 and TypeScript.

**Iris-IM**

An application component responsible for acquiring, aggregating, correlating, caching and providing information about Harmonia's application components, runtime environments, platform configuration, infrastructure and operational activity.

Iris-IM provides the information services consumed by Iris-Console.

**Iris-Agent** is embedded within a Harmonia component and obtains observations through the component's own facilities. **Iris-Observer-Agent** obtains external platform, middleware and infrastructure observations through their native APIs/functions.

Both agent types expose exactly the same **`IrisComponentObservabilityInterface`** to Iris-IM, using the same gRPC over HTTP/2 and Protocol Buffers information and interaction contract. Native technology-specific integration is performed by Iris-Observer-Agent, **not** by Iris-IM.

Iris-IM maintains a lightweight, disposable, instance-local in-memory cache; independently operating Iris-IM instances provide failover without cache replication.

The solution supports inspection of:

1. Harmonia Component Topology.
2. Operational Logs.
3. Operational Metrics.
4. Praxis and associated operational processing.

The Praxis Viewer includes:

1. Publish/Subscribe.
2. Praxis Views.
3. Current-Task Status.

Read-only introspection also includes TaskQueue and execution-instance metadata, Digital Twin operational metadata, governed AuditEvent metadata, and Dokimasia assurance activity/state/outcome metadata.

Digital Twin interaction or control, Agentic AI collaboration and Matrix/Element user interaction remain excluded. No governed business/clinical information content, task input/output payload or unrestricted audit/assurance evidence is exposed by ordinary OA&M introspection.

## 1.2 Capability and Feature Alignment

The solution realises or supports existing operational, information-management and systems-administration capabilities.

Applicable Enterprise Capabilities and Features shall be identified through reconciliation with the accepted Capability Model.

The relevant capability concerns include:

- Operational topology discovery and inspection.
- Component and service health observation.
- Platform and infrastructure configuration visibility.
- Operational logging and diagnostics.
- Operational measurements and performance monitoring.
- Praxis configuration inspection.
- Publish/Subscribe visibility.
- Task processing observation.
- Operational information correlation.
- Read-only TaskQueue, execution and Digital Twin operational introspection.
- Governed audit metadata and Dokimasia assurance-status observation.
- Common embedded/external observability agents and metadata-only packet semantics.

UI presentation does not automatically establish a new Enterprise Capability.

Existing Features shall be reused wherever their semantic coverage is sufficient.

Canonical Feature identities remain subject to verification.

## 1.3 Associated Requirements

The following requirements are proposed for architectural reconciliation.

| ID | Requirement |
|---|---|
| OAM-UI-001 | Provide Iris-Console, the existing OA&M Single Page Application, implemented using Vue 3 and TypeScript. |
| OAM-UI-002 | Provide a logical Component Topology Viewer whose principal perspective is above the Technology/Infrastructure layer. |
| OAM-UI-003 | Provide an operational Log Browser. |
| OAM-UI-004 | Provide an operational Metrics Browser. |
| OAM-UI-005 | Provide a Praxis Viewer supporting Publish/Subscribe, Praxis Views and Current-Task Status. |
| IRIS-IM-001 | Acquire operational information from participating Harmonia Application Components and services. |
| IRIS-IM-002 | Acquire underlying platform and infrastructure information through Iris-Observer-Agent native management/observation interfaces. |
| IRIS-IM-003 | Aggregate and correlate application, platform and infrastructure operational information. |
| IRIS-IM-004 | Maintain a disposable, instance-local in-memory information cache with explicit freshness and source-identification semantics. |
| IRIS-IM-005 | Expose operational information retrieval and observation services. |
| IRIS-IM-006 | Distinguish defined architecture, configured/deployed state and observed operational state. |
| IRIS-IM-007 | Support continued operational inspection when individual information sources are unavailable, subject to available cached information. |
| IRIS-IM-008 | Preserve authoritative component ownership and governance boundaries. |
| IRIS-IM-009 | Support authenticated and authorised operational information access. |
| IRIS-IM-010 | Distinguish observation from configuration-changing and operational-control activities. |
| OAM-UI-006 | Provide read-only TaskQueue, Pragma, execution and Digital Twin operational-metadata inspection. |
| OAM-UI-007 | Provide governed AuditEvent metadata and Dokimasia assurance activity/state/outcome inspection. |
| OAM-UI-008 | Restrict OA&M introspection to authorised operational metadata, identities, names and relationships; exclude governed processing content. |
| IRIS-IM-011 | Support independent Iris-IM instances and failover without replication or transfer of caches. |
| IRIS-IM-012 | Reconstruct cache information by bounded, efficient acquisition without excessive external-API traffic. |
| IRIS-IM-013 | Retain no Iris-specific durable operational event history or event-replay infrastructure. |
| OBS-001 | Iris-Agent and Iris-Observer-Agent expose one common `IrisComponentObservabilityInterface`. |
| OBS-002 | Agent-to-Iris-IM transport uses gRPC over HTTP/2 with Protocol Buffers across Java, Python and other applicable languages. |
| OBS-003 | The core packet types are `ComponentCoordinatePacket`, `ComponentStatusPacket`, `ComponentActivityPacket`, `ComponentLogPacket`, `ComponentTaskPacket` and `ComponentAuditPacket`. |
| OBS-004 | The common interaction includes provider description, bounded initial snapshot, incremental observation and on-demand metadata query. |
| OBS-005 | The observed target, reporting agent and source must remain distinguishable, with source observation time and Iris-IM acquisition/freshness semantics. |
| OBS-006 | Agent observation and reconnection shall be bounded, security-governed and independent of host business processing. |

These are provisional DRP-local identifiers, not newly allocated canonical architecture identifiers.

## 1.4 Strategy and Motivation

The proposal supports Harmonia's established architectural direction:

- FOSS-only technology selection.
- Governed information access.
- Explicit separation of architectural responsibility.
- Distributed operation and resilience.
- Operational observability and diagnosability.
- Reuse of established middleware and frameworks.
- Explicit information ownership.
- Separation between operational information and authoritative execution.
- Appropriate security and auditing of management activities.
- Simplicity of self-management: avoid introducing distributed operational caches, redundant persistence engines or an Iris-specific management framework.
- Resilience through independent reconstruction, rather than state replication.
- The defensive-programming, educational documentation and unhappy-path expectations of `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`.

---

# 2. Proposal

## 2.1 Architectural Proposition

Establish **Iris-IM** as Harmonia's operational information integration and management component.

Iris-IM shall provide a coherent operational information perspective across:

1. Harmonia Application Components and services.
2. Application deployment and runtime instances.
3. Platform services and configuration.
4. Infrastructure configuration and operational state.
5. Operational logging and metrics.
6. Praxis, Publish/Subscribe and task processing.

Iris-IM shall maintain sufficient **transient** information independently of the originating services to support efficient observation, correlation and diagnosis.

Iris-Agent and Iris-Observer-Agent provide that information through the single common northbound `IrisComponentObservabilityInterface`. Iris-IM shall not select different protocols according to agent type or implement source-specific native management adapters.

The operational scope includes metadata about TaskQueues, execution and Digital Twin instances, authorised audit information and Dokimasia assurance activity/status/outcome, without exposing governed content or assuming execution/assurance authority.

It shall not become the authoritative owner of information or execution responsibilities belonging to other Harmonia components.

## 2.2 Solution Structure

```text
                         Iris-Console
                        Vue / TypeScript
                               |
                     Governed UI information API
                               |
                               v
                            Iris-IM
               Aggregation / Correlation / Query
                   Disposable local-memory cache
                               |
                    Common gRPC / Protobuf:
             IrisComponentObservabilityInterface
                               |
                 +-------------+-------------+
                 |                           |
                 v                           v
             Iris-Agent             Iris-Observer-Agent
             (embedded)                 (external)
                 |                           |
         Host-component facilities    Native management APIs
                 |                           |
          Mneme / Ponos / ...        Synapse / PostgreSQL /
          Petasos / Dokimasia       Artemis / Kubernetes / ...
```

This is a logical responsibility model rather than a prescribed deployment topology. An external observer may inspect multiple runtime instances and need not have a one-to-one deployment relationship with its observed targets.

**Iris-IM sees one common service contract.** The distinction between embedded and external observers is entirely southbound of that interface.

The identity of the reporting agent, its observed component/instance and the originating information source shall remain distinguishable.

## 2.3 Technology

**Iris-Console**

- Vue 3 / TypeScript Single Page Application, retaining the existing `iris/iris-console` module.
- Modular presentation, typed API clients and appropriate reuse of existing Iris UI assets.
- Browser-facing information services provided by Iris-IM through an appropriate HTTP API; the browser does not implement the agent-facing gRPC contract.

**Iris-IM**

- Java/Jakarta EE is the preferred implementation technology, consistent with Harmonia's established stack.
- Existing Iris BEFE capabilities shall be evaluated for reuse.
- In-process memory cache, no dedicated persistence or distributed cache required for reconstructable observations.
- One common northbound gRPC/Protocol Buffers client implementation; no platform-specific management API integration inside Iris-IM.

**Agent communication**

- **Accepted:** gRPC over HTTP/2 and Protocol Buffers for `IrisComponentObservabilityInterface`.
- Both Iris-Agent and Iris-Observer-Agent expose precisely the same contract and interaction semantics.
- Java, Python and other applicable implementation bindings use the same wire schema and protocol.
- Mutual TLS protects/authenticates communication; authorisation remains subject to Themis and local policy enforcement.

The detailed RPC signatures and packet field definitions remain detailed-design work; the transport itself is no longer undecided.

The existing Iris BEFE boundary may be incorporated into Iris-IM responsibilities; no additional BEFE component is inherently required.

# 3. Design Context

## 3.1 Existing Iris Implementation

The existing repository contains:

- `iris/iris-console`
- `iris/iris-administration`
- `iris/iris-befe`

The existing Iris Console already provides operational monitoring interfaces.

The existing BEFE contains operational REST resources, aggregation services, component health providers and interfaces to operational caches.

These are implementation assets for consideration.

However, existing implementation behaviour is not automatically authoritative architecture.

Particular attention shall be paid to existing direct dependencies on Infinispan caches and historical task-sequence representations.

These must be reconciled with the accepted information governance, persistence and Praxis architectures.

## 3.2 Naming and Architectural Identity

The accepted architectural names are:

- **Iris-Console** (the existing Harmonia OA&M UI).
- **Iris-IM** (Iris Information Manager).
- **Iris-Agent** (embedded observability).
- **Iris-Observer-Agent** (external/native-interface observability).

The shared logical/software interface is **`IrisComponentObservabilityInterface`**. The six `Component*Packet` types use PascalCase. Architectural component names are hyphen-separated.

The DRP identifier/filename remains `DRP-Harmonia-OAM-UI`; this does **not** establish a separate replacement component named after the DRP.

Legacy Iris Console/Monitor and Iris BEFE implementation identities shall be reconciled with governed canonical identities; Iris Administration remains a separate scope and is not silently incorporated.

# 4. Architectural Definition

## 4.1 Core Architectural Principle

Iris-IM provides a unified operational information perspective connecting:

**Defined Architecture → Deployed and Configured Environment → Observed Operational Behaviour**

These perspectives are related but distinct.

### Defined Architecture

Represents Harmonia's governed logical structure, including Application Components, services and their architectural relationships.

### Deployed and Configured Environment

Represents the actual deployment and configuration of Harmonia components and supporting infrastructure.

This may include:

- Application runtime instances.
- Container and virtual-machine instances.
- Platform services.
- Service registrations.
- Deployment configuration.
- Network and service endpoints.
- Relevant configuration relationships.

### Observed Operational Behaviour

Represents information collected or reported about current and historical operation.

This may include:

- Component health.
- Availability.
- Metrics.
- Logs.
- Processing activity.
- Current task state.
- Operational exceptions.

Iris-IM shall preserve the distinction between these perspectives while enabling their correlation.

## 4.2 Information Acquisition

Iris-IM acquires operational information from both Harmonia components and external platforms exclusively through the **common agent-facing `IrisComponentObservabilityInterface`**.

The accepted northbound transport is **gRPC over HTTP/2**, using Protocol Buffers, regardless of whether the agent is embedded or external.

The logical interactions are:

- **Describe:** identify the agent, the observed target(s) and available packet categories.
- **Snapshot:** acquire a bounded initial operational representation for startup/reconstruction.
- **Observe:** receive incremental operational updates where supported.
- **Query:** retrieve authorised, bounded on-demand metadata for more detailed inspection.

These are logical operations, not final gRPC method signatures.

The agent's *southbound* acquisition mechanisms may vary by source: host instrumentation, native management queries, configuration inspection, change notifications or periodic measurement. Those choices shall not alter Iris-IM's single common northbound interaction.

Each Iris-IM instance must be able to obtain a sufficiently complete view independently; incremental delivery must not inadvertently divide necessary observations among competing consumers.

The contract shall preserve source/observer identity, timestamps, completeness, availability and appropriate error information. Connection recovery and retries must be bounded and source-sensitive.

## 4.3 Platform, Middleware and Infrastructure Observation

Iris-IM is responsible for obtaining a coherent view of platform, middleware and infrastructure configuration and operation. It obtains that information **through Iris-Observer-Agent**, whose southbound interfaces directly use appropriate native management and monitoring facilities.

The observed technologies may include:

- Ubuntu and virtualised environments.
- Containers and application servers.
- Kubernetes/OpenShift where deployed.
- PostgreSQL, Apache ActiveMQ Artemis and Infinispan.
- Matrix/Synapse and other Python or non-Java systems.
- Platform service configuration, runtime discovery and deployment information.

No assumption is made that Kubernetes is deployed in every release. One observer implementation may observe multiple instances; an observer-per-instance deployment is not mandatory.

Native management queries must be appropriately scoped and must not bypass the relevant information-governance boundaries to retrieve application business content.

### 4.3.1 Observation and Management

**Observation** retrieves current/configured state, relevant metadata, measurements and health information.

**Management** changes configuration or runtime state, such as restarting a service or modifying a deployment.

This DRP establishes **read-only operational observation**, not management authority. Future management functionality, if authorised, requires its own contracts, ownership, security and audit obligations.

The use of native management facilities by an Iris-Observer-Agent does not imply that Iris-IM or Iris-Console may issue arbitrary configuration-changing commands.

## 4.4 Information Aggregation

Iris-IM shall aggregate operational information obtained from multiple sources.

Aggregation responsibilities include:

- Combining component information from different sources.
- Relating logical components to runtime instances.
- Relating components to their operational metrics.
- Relating components and activities to associated logs.
- Combining relevant Praxis and processing observations.
- Producing coherent operational summaries.

Aggregation shall preserve the identity and source of contributing information.

Aggregation must not silently replace an authoritative source's meaning with an independently inferred state.

Where observations conflict, Iris-IM should expose or qualify the discrepancy rather than inventing agreement.

## 4.5 Information Correlation

Iris-IM shall correlate information across the application, platform and infrastructure perspectives.

Examples include:

```text
Application Component
        |
        +-- Runtime Instances
        |
        +-- Deployment Configuration
        |
        +-- Operational Health
        |
        +-- Metrics
        |
        +-- Logs
        |
        +-- Relevant Processing Activity
```

For processing information:

```text
Praxis
   |
   +-- Associated Topics
   |
   +-- Relevant Publications / Subscriptions
   |
   +-- Related Processing Activity
   |
   +-- Associated Tasks
   |
   +-- Correlated Logs and Metrics
```

Correlation shall use authoritative identities and relationships where available.

Heuristic correlation may be presented as qualified evidence but shall not silently establish an authoritative architectural relationship.

## 4.6 Information Caching

Iris-IM shall maintain a **lightweight, instance-local, in-memory cache** of operational observations and correlated representations.

The cache supports efficient operational queries, recent/current-state presentation, source correlation and continued inspection of previously acquired information when a source is unavailable.

**The cache is disposable and reconstructable.** Iris-IM shall not require a distributed cache, cache replication, separate persistent information store or new durable event history merely to perform OA&M.

### 4.6.1 Cache Authority

Iris-IM is not authoritative for Praxis definitions, task execution, logging, audit, assurance, infrastructure configuration or other source-owned information. The appropriate owning services remain authoritative.

Operational event preservation remains the responsibility of the established logging architecture, not Iris-IM. AuditEvent and assurance evidence remain within their respective existing governance boundaries.

### 4.6.2 Cache Freshness

Cached information shall distinguish the observed target, reporting agent, originating source, source observation time, Iris-IM acquisition time, completeness and freshness.

A missing or unavailable observation shall not be silently interpreted as healthy. Agent/source unavailability does not necessarily imply that the observed component itself has failed.

### 4.6.3 Cache Persistence and Recovery

Independent Iris-IM instances maintain **independent local caches**. If one instance fails, another continues to serve its own observations. There is no transfer or replication of the failed cache.

A restarting instance reconstructs its cache as an ordinary startup activity by using bounded snapshots, incremental observations and authorised source queries. It does not replay complete operational log/audit histories to recreate the cache.

Iris-IM may serve partial information before every information domain is available. Historical observations not reconstructable from current sources remain available only through their proper retained-information services.

### 4.6.4 Efficient Acquisition and Source Protection

Bootstrap and reconciliation shall avoid hammering external APIs. Agents shall favour initial bulk snapshots, native change notifications, incremental observation and version-aware retrieval where available.

Requests and reconnection attempts must be bounded, with suitable backoff, jitter, rate limits and request coalescing. Simultaneous restart of several Iris-IM instances must not create an uncontrolled management-API surge.

No additional distributed coordination/persistence service is mandated merely to achieve bounded refresh.

The guiding principle is **resilience through independence rather than synchronisation**.

## 4.7 Information Services

Iris-IM shall expose operational information services suitable for Iris-Console and other appropriately authorised consumers.

The logical service groups are:

| Service group | Principal responsibility |
|---|---|
| Component Information | Logical component identities and relationships |
| Deployment Information | Runtime instances and deployment relationships |
| Configuration Information | Relevant platform and infrastructure configuration |
| Health Information | Component and service operational health |
| Logging Information | Operational log retrieval and correlation |
| Metrics Information | Operational measurement retrieval and aggregation |
| Praxis Information | Governed Praxis definitions and related operational views |
| Processing Information | Pub/Sub observations and current-task information |
| TaskQueue / Execution Information | Queue and Pragma identity, status, retry and execution metadata |
| Digital Twin Observation | Read-only Twin identity, lifecycle, queue and execution metadata |
| Audit Metadata | Governed AuditEvent identity, outcome and relevant references |
| Assurance Information | Dokimasia activity, state and outcome metadata |

These are logical service responsibilities.

They do not prescribe individual REST resources, Java interfaces or separately deployed services.

Iris-IM shall provide appropriately governed access and shall not expose unrestricted raw access to underlying management interfaces, business/clinical payloads, task input/output, audit evidence or assurance evidence. Iris-Console shall not need to understand the native observed technology or the embedded/external agent type.

## 4.8 Component Topology Viewer

Iris-Console shall provide a Component Topology Viewer.

Its primary perspective shall represent Harmonia's logical Application Component organisation.

The viewer shall support inspection of:

- Architectural components.
- Component relationships.
- Available runtime instances.
- Deployment relationships.
- Relevant configuration.
- Health and operational state.
- Associated logs and metrics.

Infrastructure information shall be available through contextual inspection and drill-down.

The existence or multiplicity of deployed instances shall not redefine the canonical Application Component structure.

## 4.9 Log Browser

The Iris-Console shall provide a Log Browser using the information services exposed by Iris-IM.

The browser shall support:

- Temporal retrieval.
- Source and component filtering.
- Severity filtering.
- Structured and textual inspection.
- LogEvent detail.
- Correlation with related components and operational activity.

Iris-IM may maintain a bounded cache of recent log information and query results.

It shall not automatically duplicate the complete operational logging repository.

The existing Log-Satellite, Log-Server and Log-Persistence responsibilities remain separate.

The Log Browser shall preserve the distinction between operational LogEvents, AuditEvents and Provenance.

## 4.10 Metrics Browser

The Iris-Console shall provide a Metrics Browser.

Iris-IM shall aggregate and expose relevant operational measurement information.

The browser shall support:

- Metric identification.
- Component and service filtering.
- Current measurements.
- Historical measurement windows.
- Time-series presentation.
- Comparison of compatible measurements.
- Correlation with other operational information.

Metric semantics, including units, sampling intervals and measurement type, must be preserved.

The selection of metrics collection and long-term persistence technologies is not determined by this DRP.

## 4.11 Praxis Viewer

The Iris-Console shall provide a Praxis Viewer with three principal perspectives.

### 4.11.1 Publish/Subscribe

Provide visibility into:

- Topics.
- Publishers.
- Subscribers.
- Subscription relationships.
- Publication and delivery activity.
- Relevant processing outcomes.

The viewer shall distinguish logical Publish/Subscribe arrangements from observed runtime activity.

It shall preserve the established separation between publication selection, Praxis continuation, Petasos handoff and Ponos execution.

### 4.11.2 Praxis Views

Provide inspection of governed Praxis information.

This includes, where available:

- Praxis identification and version.
- Applicable configuration.
- Associated Pathway, if any.
- Relevant Topic relationships.
- Operational constraints.
- Related observed processing activity.

The viewer shall distinguish the normative Praxis definition from its observed operational use.

It shall not redefine Praxis as a task sequence, execution engine or Pub/Sub graph.

Detailed Praxis View semantics require reconciliation with the accepted Praxis architecture.

### 4.11.3 Current-Task Status

Provide operational observation of accepted work and execution state.

This includes:

- Task identification.
- Current authoritative or last-observed processing state.
- Relevant timestamps.
- Processing context.
- Related Praxis and Ergon.
- Correlation identifiers.
- Associated operational events, logs and metrics.

Task acceptance, execution commencement and task completion remain distinct events.

Iris-IM shall not derive authoritative task state merely from queue observations or inferred processing outcomes.

Digital Twin-specific task **interaction or control** remains outside this proposal; read-only Twin and task/execution **metadata** observation is included.

## 4.12 Security and Governance

Iris-IM shall observe Harmonia's established Themis security architecture.

Agent-to-Iris-IM communication uses **mutual TLS**, with applicable Themis/local-policy authorisation for observation categories and queries. Transport authentication is not a substitute for information-disclosure authorisation.

The metadata-only boundary applies at the originating service/agent and Iris-IM, not merely to browser-side field visibility. Names, identifiers and references may themselves be sensitive.

The following distinctions shall be maintained:

**Application information access**

Uses the applicable governed application interfaces and local enforcement responsibilities.

**Platform and infrastructure information access**

Uses appropriately authorised management interfaces and service identities.

Application-level authority does not automatically grant unrestricted infrastructure management authority.

Infrastructure credentials and management permissions shall not be exposed to the browser.

Iris-IM shall provide appropriately restricted information services to its consumers.

Sensitive configuration properties, credentials and operationally sensitive information shall be protected from inappropriate disclosure.

Any authorised configuration-changing operation must be subject to the applicable security, logging and audit requirements.

## 4.13 Resilience and Operational Independence

Iris-IM shall remain useful when individual Harmonia components, source systems, observers or infrastructure services become unavailable.

It supports continued diagnosis from available cached observations, honestly distinguishing stale, unavailable, unknown, partial and error conditions rather than implying health. It shall report its own acquisition failures, freshness and observation coverage.

Multiple independent Iris-IM instances may operate active/active behind a common service endpoint, each with its own disposable in-memory cache and agent subscriptions. Loss of one instance does not require cache synchronisation or transfer.

Iris-Agent failure must not prevent its host component's primary processing. A failed Iris-Observer-Agent must not force unrelated observation providers to fail. Source retries and reacquisition shall be bounded.

## 4.14 Shared Observability Contract and Packets

The accepted **`IrisComponentObservabilityInterface`** provides the same gRPC/HTTP2 and Protocol Buffers northbound service to Iris-IM, irrespective of agent type or implementation language.

Its six core information categories are:

| Packet | Principal metadata semantics |
|---|---|
| `ComponentCoordinatePacket` | Observed component identity, architectural/deployment coordinates and relationships |
| `ComponentStatusPacket` | Health, operational status, availability and lifecycle |
| `ComponentActivityPacket` | Operational activity, measurements and processing status |
| `ComponentLogPacket` | Log-service and LogEvent metadata, identifiers and references |
| `ComponentTaskPacket` | TaskQueue, Pragma, execution and Digital Twin task metadata |
| `ComponentAuditPacket` | Governed AuditEvent metadata, identities and references |

These packet names/categories are accepted; their exact fields, schema evolution and RPC signatures remain to be designed. A provider only needs to support applicable categories, declaring its capabilities through the common contract.

The contract supports provider description, efficient initial snapshots, incremental observation, and bounded/authorised on-demand queries. Each observation must preserve the observed entity, reporting agent, original source and observation timestamp; Iris-IM adds acquisition/freshness semantics.

## 4.15 Embedded Iris-Agent and External Iris-Observer-Agent

**Iris-Agent** resides within a Harmonia component. It acquires observations through local component facilities, publishes relevant metadata and does not become a dependency of the host's core processing.

**Iris-Observer-Agent** is the external observability adapter. Southbound, it may interact with native APIs/functions of Matrix/Synapse (Python), PostgreSQL, Artemis, Infinispan, application servers, Kubernetes or operating systems. Northbound, it exposes exactly the same contract as Iris-Agent.

Iris-Observer-Agent is not necessarily a separate process per observed instance; one implementation may observe several instances. The common wire/service contract shall be implemented consistently in Java, Python and other applicable languages.

Neither agent type owns system-wide correlation, Iris-IM cache lifecycle, authoritative persistence or general management/control.

## 4.16 Operational Metadata Boundary

Iris-IM and Iris-Console expose **operational metadata**, subject to appropriate Themis governance, not governed processing content.

Permitted where authorised: identities, names, component relationships, configured/deployed location, status, lifecycle, metrics, queue/task/execution state, timestamps, failures/retries, log/audit references and assurance status/outcome.

Outside ordinary OA&M introspection: InformationUnit payloads, clinical/business content, task inputs/outputs, Matrix conversation content, sensitive AuditEvent contents, and assurance evidence. Classification as metadata does not make a name or identifier unrestricted.

Operational diagnostic LogEvent messages remain subject to established logging/security policy; this does not authorise arbitrary payload retrieval.

## 4.17 TaskQueue, Execution, Digital Twins and Dokimasia

Iris-Console shall support read-only TaskQueue summaries, on-demand bounded queue metadata, Pragma identities, processing state, retries and associated execution relationships. Petasos message-transport queues must remain distinguishable from work/task processing queues.

Digital Twin instance identity/type, lifecycle, associated queue and current/recent execution metadata may be inspected. Twin interaction/control, task fulfilment and Matrix/Element user interaction are excluded.

Governed audit metadata may be inspected through appropriate audited information services.

Dokimasia assurance activity identity, lifecycle, status, outcome and relevant component/task relationships are in scope. Dokimasia remains authoritative for assessments, conclusions and evidence; Iris-IM does not reproduce its evaluation logic.

**Operational health and assurance outcome are independent dimensions.**

## 4.18 Log-Persistence and Audit-Persistence Specialisation

Agents associated with Log-Persistence and Audit-Persistence serve two distinct roles:

1. Observe/report the operational condition and capacity of the persistence service itself.
2. Expose authorised, bounded metadata-query access to relevant information retained by the owning service.

Neither agent routinely transfers an entire durable history into Iris-IM. Detailed retained LogEvent, AuditEvent and Provenance ownership remains with existing governed facilities.

`ComponentLogPacket` and `ComponentAuditPacket` do not imply bulk transfer of raw historical content. Query/filter/pagination semantics remain detailed-design subjects.

The design must avoid unbounded observation or audit-query recursion, especially where querying audit information itself produces an AuditEvent.

## 4.19 Iris-Console Implementation Perspective

The existing Vue 3/TypeScript Iris-Console remains the frontend. The Component Topology Viewer is a suitable first educational implementation exercise, with subsequent logs, metrics, Praxis and contextual introspection.

Vue Single-File Components, Composition API, composables, router, typed API clients and appropriately scoped state management are relevant design techniques; the DRP does not prescribe a particular directory/class decomposition.

The browser shall never fabricate a healthy operational status or silently substitute configured information for observed information. It shall present loading, stale, partial, empty, unavailable and error conditions faithfully.

The Iris-Console/API boundary is distinct from the common agent gRPC contract.

# 5. Explicitly Not Prescribed

The following remain implementation-design decisions:

- Internal Iris-IM Java/Jakarta EE component structure and existing BEFE code reuse.
- Final Iris-Console Vue component layout, routing, composables, charting and shared UI state.
- Browser-facing information-service endpoint and browser streaming implementation.
- In-memory cache collection data structures, expiry and bounded-memory policy.
- Detailed Protocol Buffers envelopes/fields and gRPC RPC signatures.
- Agent discovery/registration, incremental sequencing, reconnection and versioning.
- Southbound observer libraries, native polling/change-notification mechanics and source-specific rate budgets.
- Concrete deployment, packaging and language-specific generated bindings.
- Source-specific metadata-query, filtering and pagination semantics.

**Not open for re-selection:** the shared northbound gRPC/Protocol Buffers service, six packet type names, metadata-only boundary, common agent interface, independent Iris-IM local-memory caches and non-authoritative role.

This DRP does not authorise an Iris distributed cache, dedicated Iris-IM persistence engine, full event-replay framework or native management adapters within Iris-IM.

# 6. Evidence and Rationale

The proposal is based on:

1. Existing Iris Console and BEFE implementation evidence.
2. Harmonia's accepted Application Architecture.
3. Existing information governance and service ownership semantics.
4. The operational Logging DRPs.
5. Accepted Praxis, Pragma, Ponos and Petasos responsibilities.
6. Harmonia's platform and infrastructure deployment direction.
7. The Solution Architect's explicit direction to use Vue 3/TypeScript.
8. The Solution Architect's decision to establish Iris-IM as an operational information aggregator and cache.
9. The Solution Architect's explicit requirement for direct platform and infrastructure configuration **observation**, implemented southbound by Iris-Observer-Agent.
10. Accepted Iris-Agent and Iris-Observer-Agent architecture, six packet types and single `IrisComponentObservabilityInterface`.
11. Accepted gRPC over HTTP/2 and Protocol Buffers transport, mutual TLS, bounded snapshots, incremental observation and on-demand metadata queries.
12. Accepted disposable local-memory caching and independent-instance failover without operational event preservation.
13. Accepted metadata-only inspection of TaskQueues, execution/Digital Twins, governed audit metadata and Dokimasia assurance metadata.
14. The repository-wide `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`.

The central architectural rationale is that an operational management interface must correlate information spanning multiple architectural layers.

Without an intermediate information-management responsibility, the SPA would either acquire substantial integration complexity or depend upon numerous source-specific interfaces.

Iris-IM centralises that information-integration responsibility while preserving underlying authority and service ownership.

---

# 7. Relationship to Current Architecture

## 7.1 Existing Iris BEFE

The existing Iris BEFE is an implementation asset for Iris-IM.

Its operational information acquisition, aggregation, REST interfaces and security capabilities shall be examined for reuse.

Its existing direct cache dependencies and historical processing models require reconciliation against the current architecture.

The proposal does not require a separately deployed BEFE in addition to Iris-IM.

## 7.2 Mneme and Mnemosyne

Iris-IM obtains ordinary operational metadata through the common observability interface. Iris-Agents use applicable governed/local component interfaces when observing information owned by Mneme or Mnemosyne.

Direct native access to platform and infrastructure management services belongs to Iris-Observer-Agent and need not be routed through Mneme.

Iris-IM shall not bypass governed information access merely because information is being presented for operational purposes.

## 7.3 Logging Services

The existing operational logging architecture remains authoritative for log collection, delivery and persistence.

Iris-IM provides retrieval, aggregation, correlation and caching appropriate to OA&M use.

## 7.4 Praxis and Execution Services

Existing Praxis, Ponos, Petasos and Pragma responsibilities remain unchanged.

Iris-IM observes their definitions, relationships and operational state.

It does not acquire task scheduling, routing or execution authority.

## 7.5 Technology and Infrastructure

Platform and infrastructure observation is explicitly supported through Iris-Observer-Agent southbound native management and monitoring facilities.

Iris-IM retains only the common northbound gRPC/Protocol Buffers observability interface, avoiding technology-specific dependencies. External observation does not establish ownership of the underlying infrastructure or its authoritative configuration.

Non-Java systems, including Matrix/Synapse in Python, may be externally observed without altering their code; agents written in Python or other languages must still satisfy the same northbound contract.

# 8. Conformance Expectations

A conforming implementation shall demonstrate:

1. Retention of the existing Iris-Console Vue 3/TypeScript SPA.
2. A distinct and simple Iris-IM information-management responsibility.
3. Both Iris-Agent and Iris-Observer-Agent implementing **the identical** `IrisComponentObservabilityInterface`.
4. Common northbound gRPC over HTTP/2, Protocol Buffers and applicable Java/Python compatibility.
5. The six accepted packet types with source, observer and observed-target identity preserved.
6. Efficient snapshots, incremental updates and bounded on-demand metadata queries.
7. Native platform/middleware/third-party access performed only southbound of Iris-Observer-Agent.
8. Local disposable in-memory caching, independent Iris-IM failover and efficient/restrained reconstruction.
9. No new Iris-IM durable operational LogEvent/AuditEvent store or event-replay framework.
10. Explicit correlation of architecture, deployment, operation and observation freshness.
11. Original four viewers plus TaskQueue/execution/Digital Twin operational metadata, governed audit metadata and Dokimasia assurance metadata.
12. Metadata-only information protection at the source/provider and Iris-IM boundaries, not only in the browser.
13. Separation of read-only observation and control/management authority.
14. Themis/local authorisation and mutual TLS for the agent-facing interface.
15. Preservation of existing logging, audit, provenance, assurance, information governance and execution ownership.
16. Honest representation of unavailable, stale, incomplete and failed observations.
17. Defensive/unhappy-path tests, code comprehensibility and relevant documentation under `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`.

This DRP does not introduce Digital Twin interaction or control functionality.

# 9. Open Questions / Matters for Review

The original Draft 002 review identifiers are preserved. Architectural directions now resolved are distinguished from remaining detailed design.

| ID | Matter | Draft 003 standing |
|---|---|---|
| OAM-DR-01 | Reconcile existing Iris architectural identities with Iris-IM and Iris-Console | Names accepted (Iris-Console/Iris-IM); canonical reconciliation open |
| OAM-DR-02 | Determine reuse and disposition of existing Iris BEFE implementation | Open — implementation reconciliation |
| OAM-DR-03 | Define logical topology acquisition and architectural source alignment | Direction accepted; exact source/version acquisition open |
| OAM-DR-04 | Define platform-specific infrastructure information-acquisition boundaries | Agent responsibility accepted; source-specific mappings open |
| OAM-DR-05 | Establish cache durability, retention and recovery requirements | Decided: disposable local memory; details of bounded acquisition open |
| OAM-DR-06 | Reconcile authoritative metrics collection and historical retrieval services | Open |
| OAM-DR-07 | Establish precise Praxis View information contracts | Open |
| OAM-DR-08 | Establish current-task observation contracts | Open |
| OAM-DR-09 | Define boundary for authorised operational-control/configuration-changing requests | Decided: observation only; control excluded |
| OAM-DR-10 | Determine push, pull and streaming acquisition patterns by information type | Northbound gRPC snapshot/stream/query accepted; source-side details open |
| OAM-DR-11 | Reconcile Enterprise Capability and Feature mappings | Open |
| OAM-DR-12 | Define source freshness, availability and completeness semantics | Direction accepted; exact contract fields/thresholds open |
| OAM-DR-13 | Finalise Protocol Buffers packet fields, envelopes, versioning and gRPC RPCs | Open |
| OAM-DR-14 | Define provider discovery, registration and source reconciliation | Open |
| OAM-DR-15 | Define incremental observation sequencing, resumption and snapshot completeness | Open |
| OAM-DR-16 | Define Log-Persistence/Audit-Persistence bounded metadata query/filter/pagination | Open |
| OAM-DR-17 | Reconcile source-specific observability, including Digital Twins and Dokimasia | Open — detailed information mapping |
| OAM-DR-18 | Establish acquisition budgets, reconnection strategy and security identity/mTLS lifecycle | Open |
| OAM-DR-19 | Reconcile existing Iris-Console APIs and Vue architecture to Iris-IM information services | Open |

These are genuine detailed-design and Continuum-reconciliation questions. They do not reopen the accepted common protocol, packet names, read-only information boundary or local cache architecture.

# 10. Continuum Update

Upon acceptance, a governed ADD shall reconcile the proposal against existing canonical architecture.

## 10.1 Motivation Updates

Identify and incorporate applicable operational information-management requirements and associated constraints.

## 10.2 Strategy Updates

Map the solution to established Enterprise Capabilities and Features.

## 10.3 Business Layer — Information Architecture

Reuse existing Business Objects and Topics where appropriate.

Do not introduce new Business Objects solely to represent UI data structures.

## 10.4 Business Layer — Behaviour

No new business execution framework is proposed.

## 10.5 Application Layer — Information Architecture

Reconcile the information contracts required to represent:

- Logical component structure.
- Deployment and runtime relationships.
- Platform configuration.
- Operational observations.
- Cached information and freshness.
- Logs and metrics.
- Praxis and task-processing observations.

Do not automatically turn presentation representations into canonical business information objects.

## 10.6 Application Layer — Capability Realisation

Identify the Application Functions, Services and Interactions required for Iris-IM and Iris-Console to realise the applicable Features.

## 10.7 Application Layer — Structure

Reconcile existing Iris components and define the appropriate structural representation of:

- Iris-IM.
- Iris-Console.
- Iris-Agent and Iris-Observer-Agent.
- `IrisComponentObservabilityInterface` and the associated packet information concepts.
- Supporting operational information interfaces.

Preserve existing canonical identities wherever semantic continuity is established.

## 10.8 Cross-Layer Relationships

Explicitly represent the relationships between Iris-IM and:

- Harmonia Application Components.
- Their operational information services.
- Platform and infrastructure management interfaces.
- Relevant operational information concepts.
- Associated Features and Requirements.

Iris-IM's cross-layer information-acquisition responsibilities shall not be mistaken for ownership of the underlying Technology/Infrastructure elements. Native technology-specific southbound relationships belong to Iris-Observer-Agent; its northbound service is the common gRPC/Protocol Buffers contract.

## 10.9 Unaffected Architecture

The proposal does not change:

- Digital Twin interaction architecture.
- Agentic AI interaction architecture.
- Matrix/Element integration.
- Authoritative task execution semantics.
- Mneme/Mnemosyne information-governance responsibilities.
- Existing logging transport and persistence responsibilities.
- Authoritative Themis policy ownership.
- Dokimasia assurance execution and evidence governance.
- Kleio/AuditEvent authoritative ownership.

Read-only operational observation of Digital Twin, execution, audit and assurance **metadata** does not constitute an architectural change to their execution, interaction or information-ownership responsibilities.

---

# 11. Review Decision

**Decision:** Accepted architectural direction consolidated; governed Continuum incorporation pending.  
**Decision authority:** Solution Architect / Architecture Review.

Established design inputs:

- The existing UI is **Iris-Console**, implemented in Vue 3/TypeScript.
- **Iris-IM** aggregates, correlates and exposes information held in a disposable local in-memory cache, with independent failover and bounded reconstruction.
- **Iris-Agent** provides embedded component observability; **Iris-Observer-Agent** adapts native platform/middleware/external interfaces.
- Both expose precisely one **`IrisComponentObservabilityInterface`**, using **gRPC over HTTP/2** and **Protocol Buffers**.
- The six accepted `Component*Packet` types form the core information categories.
- Read-only operational metadata/identities/names/references are in scope, subject to Themis; business/clinical/task payload content is not.
- Original UI viewing areas remain, extended with TaskQueue/execution/Digital Twin operational introspection, AuditEvent metadata and **Dokimasia** assurance metadata.
- Operational LogEvent preservation remains the existing logging architecture's responsibility; Iris-IM is not an event store.
- Digital Twin interaction/control, AI collaboration and Matrix/Element user interaction are excluded.

Remaining contract and mapping questions are recorded in Section 9. They do not reopen these accepted decisions.

No source-code implementation or Continuum ADD is authorised by this consolidation.

# 12. Post-Decision Actions

Following Solution Architect acceptance:

1. Reconcile canonical identities and capability mappings.
2. Incorporate the accepted architecture using the governed ADD process.
3. Define the common gRPC/Protocol Buffers IrisComponentObservabilityInterface, six packet contracts and bounded snapshot/stream/query interactions.
4. Reconcile existing Iris implementation assets.
5. Establish agent source mappings, bounded acquisition/security expectations and implementation acceptance criteria, including the Implementation Guide.
6. Prepare the appropriate Junie implementation assignment.

No source-code modification or Continuum incorporation is authorised by this draft.

---

# Appendix A — Design Provenance

**Architecture authority:**  
`enterprise-architecture/continuum/`

**Working design location:**  
`enterprise-architecture/design/offline-design/`

**Related DRPs:**
- `DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md`
- `DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md`
- Applicable Praxis and governed-information interaction DRPs.

**Existing implementation references:**
- `iris/iris-console`
- `iris/iris-administration`
- `iris/iris-befe`

**Design lineage:**

Draft 001 established the Vue/TypeScript OA&M SPA and its four principal functional areas.

Draft 002 introduced Iris-IM and direct underlying platform/infrastructure observation.

Draft 003 consolidates the accepted Iris-Console name, Iris-Agent/Iris-Observer-Agent architecture, common gRPC/Protocol Buffers `IrisComponentObservabilityInterface` and six packet types; metadata-only TaskQueue/execution/Digital Twin/AuditEvent/Dokimasia introspection; disposable instance-local caching and independently reconstructed active/active failover. Native platform/middleware interaction is explicitly assigned to Iris-Observer-Agent's southbound interfaces.

**Standing:** Consolidated Draft 003 — accepted design direction, pending governed incorporation.