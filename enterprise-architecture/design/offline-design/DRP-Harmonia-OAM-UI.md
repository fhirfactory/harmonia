<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal (DRP)
# Harmonia Operations, Administration and Management UI

**DRP:** DRP-Harmonia-OAM-UI  
**Status:** Draft 002 — Architectural Review  
**Date:** 2026-11-11  
**Solution Architect:** Mark Hunter  
**Architecture authority:** `enterprise-architecture/continuum/`

**Principal solution elements:**
- Harmonia-OAM-UI — Vue 3 / TypeScript Single Page Application.
- Iris-IM — Iris Information Manager.

**Existing implementation lineage:**
- Iris Console / Iris Monitor.
- Iris BEFE.
- Iris Administration (related but distinct scope).

**Architectural disposition:** Proposed. No Continuum modification or implementation authorised.

---

# 1. Feature Realisation

## 1.1 Purpose

Establish the Harmonia Operations, Administration and Management solution as an integrated operational workbench, supported by an operational information management component.

The solution comprises two logically distinct responsibilities:

**Harmonia-OAM-UI**

A browser-based presentation application implemented using Vue 3 and TypeScript.

**Iris-IM**

An application component responsible for acquiring, aggregating, correlating, caching and providing information about Harmonia's application components, runtime environments, platform configuration, infrastructure and operational activity.

Iris-IM provides the information services consumed by Harmonia-OAM-UI.

Iris-IM is not limited to consuming Harmonia Application Services. It also interacts directly with appropriate underlying platform and infrastructure management interfaces.

The solution supports inspection of:

1. Harmonia Component Topology.
2. Operational Logs.
3. Operational Metrics.
4. Praxis and associated operational processing.

The Praxis Viewer includes:

1. Publish/Subscribe.
2. Praxis Views.
3. Current-Task Status.

Digital Twin interaction, Agentic AI interaction and Matrix/Element integration are excluded from this proposal.

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

UI presentation does not automatically establish a new Enterprise Capability.

Existing Features shall be reused wherever their semantic coverage is sufficient.

Canonical Feature identities remain subject to verification.

## 1.3 Associated Requirements

The following requirements are proposed for architectural reconciliation.

| ID | Requirement |
|---|---|
| OAM-UI-001 | Provide an OA&M Single Page Application implemented using Vue 3 and TypeScript. |
| OAM-UI-002 | Provide a logical Component Topology Viewer whose principal perspective is above the Technology/Infrastructure layer. |
| OAM-UI-003 | Provide an operational Log Browser. |
| OAM-UI-004 | Provide an operational Metrics Browser. |
| OAM-UI-005 | Provide a Praxis Viewer supporting Publish/Subscribe, Praxis Views and Current-Task Status. |
| IRIS-IM-001 | Acquire operational information from participating Harmonia Application Components and services. |
| IRIS-IM-002 | Interact directly with appropriate underlying platform and infrastructure management interfaces. |
| IRIS-IM-003 | Aggregate and correlate application, platform and infrastructure operational information. |
| IRIS-IM-004 | Maintain operational information caches with explicit freshness and source-identification semantics. |
| IRIS-IM-005 | Expose operational information retrieval and observation services. |
| IRIS-IM-006 | Distinguish defined architecture, configured/deployed state and observed operational state. |
| IRIS-IM-007 | Support continued operational inspection when individual information sources are unavailable, subject to available cached information. |
| IRIS-IM-008 | Preserve authoritative component ownership and governance boundaries. |
| IRIS-IM-009 | Support authenticated and authorised operational information access. |
| IRIS-IM-010 | Distinguish observation from configuration-changing and operational-control activities. |

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

Iris-IM shall maintain sufficient information independently of the originating services to support efficient observation, correlation and diagnosis.

It shall not become the authoritative owner of information or execution responsibilities belonging to other Harmonia components.

## 2.2 Solution Structure

```text
                   Harmonia-OAM-UI
                    Vue/TypeScript
                          |
                          |
                   Information APIs
                          |
                          v
                 +------------------+
                 |                  |
                 |     Iris-IM      |
                 |                  |
                 | Acquisition      |
                 | Aggregation      |
                 | Correlation      |
                 | Cache Management |
                 | Query Services   |
                 |                  |
                 +--------+---------+
                          |
            +-------------+-------------+
            |                           |
            v                           v
     Harmonia Application        Platform / Infrastructure
           Services                 Management Interfaces
            |                           |
     +------+------+             +------+------+
     |      |      |             |      |      |
   Praxis  Logs  Metrics       Runtime Config Deployment
   Ponos   etc.  Health        Services       Information
   Petasos
   Mneme
```

This is a logical responsibility model.

It does not require every depicted responsibility to be implemented as a separate deployed service.

## 2.3 Technology

**Harmonia-OAM-UI**

- Vue 3.
- TypeScript.
- Single Page Application.
- Modular presentation architecture.

**Iris-IM**

- Java/Jakarta EE is the preferred implementation technology, consistent with Harmonia's established application stack.
- Reuse of existing Iris BEFE assets shall be examined.
- Internal component and service decomposition remains subject to implementation design.

Iris-IM is not required to expose a separate traditional BEFE simply because its principal consumer is a browser application.

The existing BEFE boundary may be incorporated into Iris-IM's information-service responsibilities.

---

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

The working solution names are:

- **Harmonia-OAM-UI**
- **Iris-IM**

Iris-IM replaces the working designation OAM-IM.

The existing Iris Console, Iris Monitor and Iris BEFE names shall be reconciled against their governed architectural identities before renaming or superseding canonical elements.

The existing Iris Administration scope shall not be silently incorporated into Harmonia-OAM-UI.

---

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

Iris-IM shall support acquiring information from both Harmonia applications and underlying infrastructure.

Acquisition mechanisms may include:

- Direct service queries.
- Platform management API queries.
- Configuration retrieval.
- Service and runtime discovery.
- Operational event subscriptions.
- Published telemetry.
- Periodic observation.
- On-demand information retrieval.

The acquisition mechanism may differ by information type and source.

Iris-IM shall not require every source to implement the same technical protocol.

The selected mechanisms must preserve relevant security, consistency, temporal and source-identification semantics.

## 4.3 Direct Platform and Infrastructure Interaction

Iris-IM explicitly owns responsibility for interacting with appropriate platform and infrastructure management interfaces.

These interactions may be independent of Mneme and the general Harmonia application information-access architecture.

Relevant environments may include:

- Ubuntu-based deployments.
- Virtualised runtime environments.
- Container runtime environments.
- OpenShift/Kubernetes deployments where applicable.
- Supporting middleware.
- Platform service configuration.
- Infrastructure management and discovery services.

No assumption is made that Kubernetes is present in every release or deployment.

Platform-specific access shall be isolated behind appropriate information-acquisition contracts.

The information supplied by platform interfaces remains distinguishable from Harmonia's governed Application Architecture.

### 4.3.1 Observation and Management

Infrastructure interaction has two distinct forms.

**Observation**

Retrieval of configuration and operational information.

Examples include inspecting deployed instances, configuration properties, resource utilisation and runtime health.

**Management**

An operation that changes configuration or runtime state.

Examples include modifying service configuration, restarting a runtime or initiating a deployment operation.

Iris-IM's information-acquisition responsibility does not automatically authorise every possible management action.

Where management functions are introduced, they require explicit service contracts, authorisation and ownership.

Iris-IM may provide management-facing interfaces without necessarily executing the underlying management operation itself.

Detailed command execution, delegation, lifecycle and approval semantics remain subject to further architectural definition.

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

Iris-IM shall provide an operational information cache.

The cache supports:

- Efficient operational queries.
- Reduced repeated retrieval from source services.
- Current-state presentations.
- Recent historical observations.
- Cross-source correlation.
- Continued inspection of previously acquired information when a source is unavailable.

### 4.6.1 Cache Authority

The Iris-IM cache is not authoritative for the underlying Harmonia operations.

For example:

- Praxis definitions remain governed by their owning services.
- Task execution state remains authoritative within the established execution architecture.
- Operational logs retain their established logging ownership.
- Infrastructure configuration remains authoritative within its relevant management systems.

Iris-IM maintains operational representations of this information.

### 4.6.2 Cache Freshness

Cached information shall retain sufficient metadata to distinguish:

- When the source observation occurred.
- When Iris-IM acquired the information.
- Whether the information is considered current.
- Whether the source is reachable.
- Whether the observation is complete.

An unavailable information source shall not automatically imply that its associated application component is unhealthy.

Similarly, the absence of recent observations shall not be represented as confirmation of health.

### 4.6.3 Cache Persistence and Recovery

The Iris-IM cache may contain reconstructable information, recent historical observations and operationally useful correlation records.

The requirements for cache durability and retention remain subject to architectural review.

Cache persistence shall not silently create another authoritative persistence service.

Where reconstructability is assumed, the implications of source unavailability, retention and recovery must be established.

## 4.7 Information Services

Iris-IM shall expose operational information services suitable for Harmonia-OAM-UI and other appropriately authorised consumers.

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

These are logical service responsibilities.

They do not prescribe individual REST resources, Java interfaces or separately deployed services.

Iris-IM shall provide appropriately governed access and shall not expose unrestricted raw access to underlying management interfaces.

## 4.8 Component Topology Viewer

The Harmonia-OAM-UI shall provide a Component Topology Viewer.

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

The Harmonia-OAM-UI shall provide a Log Browser using the information services exposed by Iris-IM.

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

The Harmonia-OAM-UI shall provide a Metrics Browser.

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

The Harmonia-OAM-UI shall provide a Praxis Viewer with three principal perspectives.

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

Digital Twin-specific task interaction remains outside this proposal.

## 4.12 Security and Governance

Iris-IM shall observe Harmonia's established Themis security architecture.

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

Iris-IM should remain useful when individual Harmonia components or infrastructure services are degraded or unavailable.

It shall support operational diagnosis using available current and historical observations.

A failure to refresh information shall not be presented as confirmation of operational health.

Iris-IM shall also expose information about its own operational condition, including information-acquisition failures and cache freshness.

The dependency and recovery requirements of Iris-IM's own cache require further definition.

---

# 5. Explicitly Not Prescribed

The following remain implementation-design decisions:

- Internal Java component decomposition.
- Detailed REST API structure.
- Vue component structure.
- Specific charting and topology-rendering libraries.
- Browser-side state management.
- Cache technology.
- Collection scheduling mechanisms.
- Polling versus streaming transport.
- Platform-specific client libraries.
- Detailed configuration-adapter implementations.
- Deployment packaging.
- Internal persistence implementation, if required.

These choices must conform to the architectural responsibilities and boundaries established by this DRP.

---

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
9. The Solution Architect's explicit requirement for direct platform and infrastructure configuration interaction.

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

Iris-IM shall use appropriate governed application interfaces when accessing information owned by the Harmonia information-management architecture.

Direct access to platform configuration and infrastructure management services is a distinct responsibility and need not be routed through Mneme.

Iris-IM shall not bypass governed information access merely because information is being presented for operational purposes.

## 7.3 Logging Services

The existing operational logging architecture remains authoritative for log collection, delivery and persistence.

Iris-IM provides retrieval, aggregation, correlation and caching appropriate to OA&M use.

## 7.4 Praxis and Execution Services

Existing Praxis, Ponos, Petasos and Pragma responsibilities remain unchanged.

Iris-IM observes their definitions, relationships and operational state.

It does not acquire task scheduling, routing or execution authority.

## 7.5 Technology and Infrastructure

Iris-IM may directly access underlying platform and infrastructure management interfaces.

This is an explicit part of its architectural responsibility.

Such access does not imply that Iris-IM owns the underlying infrastructure or its authoritative configuration.

---

# 8. Conformance Expectations

A conforming implementation shall demonstrate:

1. Vue 3/TypeScript SPA delivery.
2. A distinct Iris-IM information-management responsibility.
3. All four required UI functional areas.
4. Direct platform/infrastructure information acquisition.
5. Governed application-service integration.
6. Explicit correlation of architecture, deployment and observation.
7. Appropriate cache freshness and source identification.
8. Separation of information observation and operational-control authority.
9. Conformance with Themis security requirements.
10. Preservation of existing Harmonia component responsibilities.
11. Appropriate treatment of source unavailability and incomplete observations.
12. No introduction of Digital Twin interaction functionality through this DRP.

---

# 9. Open Questions / Matters for Review

| ID | Matter | Standing |
|---|---|---|
| OAM-DR-01 | Reconcile existing Iris architectural identities with Iris-IM and Harmonia-OAM-UI | Open |
| OAM-DR-02 | Determine reuse and disposition of existing Iris BEFE implementation | Open |
| OAM-DR-03 | Define logical topology acquisition and architectural source alignment | Open |
| OAM-DR-04 | Define platform-specific infrastructure information-acquisition boundaries | Open |
| OAM-DR-05 | Establish cache durability, retention and recovery requirements | Open |
| OAM-DR-06 | Reconcile authoritative metrics collection and historical retrieval services | Open |
| OAM-DR-07 | Establish the precise Praxis View information contracts | Open |
| OAM-DR-08 | Establish current-task observation contracts | Open |
| OAM-DR-09 | Define the boundary for authorised operational-control and configuration-changing requests | Open |
| OAM-DR-10 | Determine push, pull and streaming acquisition patterns by information type | Open |
| OAM-DR-11 | Reconcile Enterprise Capability and Feature mappings | Open |
| OAM-DR-12 | Define source freshness, availability and completeness semantics | Open |

These matters require architectural consideration where they affect system responsibilities or externally observable behaviour.

They do not require architecture to prescribe internal implementation detail unnecessarily.

---

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

Identify the Application Functions, Services and Interactions required for Iris-IM and Harmonia-OAM-UI to realise the applicable Features.

## 10.7 Application Layer — Structure

Reconcile existing Iris components and define the appropriate structural representation of:

- Iris-IM.
- Harmonia-OAM-UI.
- Supporting operational information interfaces.

Preserve existing canonical identities wherever semantic continuity is established.

## 10.8 Cross-Layer Relationships

Explicitly represent the relationships between Iris-IM and:

- Harmonia Application Components.
- Their operational information services.
- Platform and infrastructure management interfaces.
- Relevant operational information concepts.
- Associated Features and Requirements.

Iris-IM's cross-layer information-acquisition responsibilities shall not be mistaken for ownership of the underlying Technology/Infrastructure elements.

## 10.9 Unaffected Architecture

The proposal does not change:

- Digital Twin interaction architecture.
- Agentic AI interaction architecture.
- Matrix/Element integration.
- Authoritative task execution semantics.
- Mneme/Mnemosyne information-governance responsibilities.
- Existing logging transport and persistence responsibilities.
- Authoritative Themis policy ownership.

---

# 11. Review Decision

**Decision:** Pending  
**Decision authority:** Solution Architect / Architecture Review

Established design inputs:

- Harmonia-OAM-UI uses Vue 3 and TypeScript.
- Iris-IM is the proposed information-management component.
- Iris-IM aggregates, correlates and caches operational information.
- Iris-IM directly interacts with appropriate platform and infrastructure configuration interfaces.
- The UI provides the four agreed functional areas.
- Digital Twin interaction is excluded.

The remaining architectural questions are recorded in Section 9.

---

# 12. Post-Decision Actions

Following Solution Architect acceptance:

1. Reconcile canonical identities and capability mappings.
2. Incorporate the accepted architecture using the governed ADD process.
3. Establish the Iris-IM information-acquisition and service contracts.
4. Reconcile existing Iris implementation assets.
5. Establish implementation scope and acceptance criteria.
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

Draft 002 introduces Iris-IM as the operational information-management responsibility, consolidates the proposed BEFE role into that responsibility, and explicitly establishes direct interaction with underlying platform and infrastructure configuration.

**Standing:** Candidate — Solution Architect review required.