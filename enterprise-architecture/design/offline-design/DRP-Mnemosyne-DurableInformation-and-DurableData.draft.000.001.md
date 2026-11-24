<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Design Review Proposal — Mnemosyne DurableInformation and DurableData

**Status** · Draft.000.001 · Solution Architect design direction pending formal reconciliation  
**Architecture baseline** · 1.1.26  
**Solution Architect** · Mark Hunter  
**Scope** · Logical responsibilities, existing-contract correspondence and review decisions for Harmonia-managed database services. [Architecture release][release] · [Architecture Review Praxis][praxis]

## 1. Purpose and architectural basis

Harmonia needs durable persistence for FHIR and other governed information, publication and subscription models, Pragma-family information, recoverable Ergon state, distributed operational state, messaging, audit, configuration and large binary content. The Solution Architect directs a distinction between **Mnemosyne.DurableInformation**, which provides governed InformationUnit (IU) persistence to Mneme, and **Mnemosyne.DurableData**, which provides durable database services to DurableInformation and other authorised Harmonia consumers through appropriate native interfaces. DurableInformation consumes DurableData. The two responsibilities are proposed within the existing Mnemosyne architecture. The document records candidate correspondence and leaves formal element and relationship allocation for review.

Use the existing architectural intent for durable governed state, authoritative durable state establishment, recoverable work, audit availability and media reference continuity. These are contribution points within their existing scope and standing. A database service neither takes over their semantic ownership nor demonstrates their implementation conformance. [Durable Governed State][data-001] · [Authoritative Durable State Establishment][authoritative-feature] · [Durable Work Acceptance][durable-work] · [Durable Audit Availability][audit-availability] · [MediaRecoveryAccountable][media-recovery]

AX-03/04 preserve native representations and use of provider capabilities. AX-05/06 keep authoritative IU state and the credibility of information distinct from active state and storage facts. AX-07/08 preserve enforced policy and meaningful audit evidence. AX-09 allows explicit durability for operational state that needs recovery while preserving the default transience of working state. AX-10/11 require distributed failure and capacity to be handled without weaker guarantees. AX-14/15 distinguish acknowledgement, durable effect, visibility and uncertainty. AX-17 reserves changes in authority and unestablished mappings for review. AX-18 and the component allocations preserve domain ownership across the collaboration. [Canonical Principles][axioms] · [Accepted Mnemosyne allocations][rad-123-allocation]

## 2. Logical service relationships

```text
                            Mneme
                              |
                Five governed IU contracts
                              |
                              v
                  Mnemosyne.DurableInformation
                              |
                            uses
                              |
                              v
                     Mnemosyne.DurableData
                              ^
                              |
              authorised native database access
                  /           |           \
        Artemis (Petasos)  HAPI FHIR    Other authorised
                            JPA Server   consumers
```

The arrows express service use. They prescribe neither physical clustering and deployment boundaries nor shared database instances or credentials. The diagram describes the proposed Harmonia-managed foundation. Existing provider-specific integrations, including externally managed PersistenceServices, retain their scoped correspondence and qualification requirements. [Mnemosyne][mnemosyne] · [Managed Persistence Service draft][managed]

| Responsibility | Candidate allocation |
| --- | --- |
| **Mneme** | Application-facing governed information access, active state, individual-IU concurrency and discovery/selection of eligible Mnemosyne instances. The consumer's disposition stays with its owning operation. |
| **Mnemosyne.DurableInformation** | Governed IU persistence and acquisition through the existing contracts. Establishes authoritative durable state/version and required management metadata, preserves governed identity, representation, compartments, retention and recovery evidence, and reports the factual operation Outcome. |
| **Mnemosyne.DurableData** | Offers authorised consumers durable database services. Provides service management, provisioning/deployment, availability, resilience and recovery for the applicable database-service scope. Uses existing platform capabilities for infrastructure execution, subject to the allocation decision in §7. |
| **PersistenceService instances and PostgreSQL** | An instance provides a concrete capability under its applicable contract. A DurableData instance may expose PostgreSQL wire compatibility and JDBC connectivity using PostgreSQL itself. One logical service can span physical nodes. Different services may share a tested deployment or use separate ones. |
| **Native consumers** | Own their schemas, domain information models, transaction meaning, processing and consumer-level acknowledgement. They use DurableData directly for the approved storage scope without translating their native records into IUs or FHIR. |

Use the **five** existing Mneme-facing contracts: `GovernedSummaryReader`, `GovernedSummaryWriter(CREATE, RETIRE)`, `GovernedSummarySearch`, `GovernedReader` and `GovernedWriter`. The direction's `GovernedSearch` label describes the search responsibility. The existing search identity exposes only SummarySearch in R1/R2. Preserve Summary-only predicates, matches and results, exact-version reads, originating-instance continuations, full-write Summary/Content coherence, and coverage through the union of eligible supported IU types. The native database boundary adds no Mneme-facing operation. [Canonical Mnemosyne upper contract][mnemosyne] · [Accepted five-contract interaction][rad-124-contract] · [IU framework preservation][rad-125-iu]

Local Themis PEP/PDP responsibilities and the trusted operation context remain at the governed boundaries. Database roles, service identities and native connection credentials secure the lower service and express authorised use of the assigned storage scope. They confer no authority to bypass Mneme, write another component's tables, or use raw database access as a governed IU path. Preserve applicable access control, compartments, encryption, meaningful audit/provenance and zero-PHI diagnostics through operation, failover and restore. Exact native authorisation, administration and evidence contracts remain for design. [Canonical AX-07][ax07] · [Mnemosyne governance][mnemosyne] · [Repository boundaries][agents]

## 3. Correspondence with the five tiers and PersistenceService

The row-by-row correspondence is proposed for review. The accepted five tiers continue to govern authoritative IU persistence. The lower database service uses native database interfaces and need not interpret client schemas, IU semantics or application transactions.

| Accepted tier or concept | Correspondence in this draft |
| --- | --- |
| Mneme Adapter / Façade | DurableInformation's upper boundary. Preserve MP-08 registration and Mneme Store discovery and selection, independently of native database endpoint discovery. |
| InformationUnit Controller | Retains persistence-context governance, including local execution of Themis-provided PDP/PEP, inside the governed IU path. |
| IU Constructor / Disassembler | Retains governed IU decomposition/reconstruction and exactly one IU representation, with provider-native mapping and native Content preserved. |
| GovernedPersistenceFramework (GPF) | Retains persistence-service state/workflow control, delegation, supervision and failure processing on the accepted IU path. Reuse applicable management capabilities for DurableData where their semantics fit. Decide which responsibilities are shared and which remain specific to the IU path. |
| Persistence Adapter | Retains provider-native execution, ACK, supplementary verification and correlation of the delegated operation. It is the candidate point of native access from DurableInformation to a suitable DurableData service. It may use the appropriate provider interface, including HAPI's interface where HAPI is the provider. |
| `PersistenceService[1..X]` | Retain GPF and Persistence Adapter jointly realising the existing governed persistence service. Determine how its identity and contract relate to a concrete DurableData instance exposing native access, including when the same implementation supports both uses. There is no new tier implied. |

A reusable service definition does not determine instance count, database count or deployment placement. The candidate does not route native consumer SQL through the IU tiers or require a bespoke PostgreSQL protocol. A common management responsibility may support native services without participating in every consumer transaction. Formal treatment of the existing service identity, candidate native service/interface and GPF or adapter reuse remains open. [Accepted five tiers and MP-01–MP-08][rad-123] · [Canonical PersistenceService][persistence-service] · [Mnemosyne lower interaction draft][provider-interaction]

## 4. Workloads and provider boundaries

Persistence placement follows the workload's contract. Workloads may reuse a PersistenceService implementation and share a deployment where requirements and evidence support it. Independent deployments are required where sharing would violate performance, durability, recovery, lifecycle or failure-isolation requirements.

| Workload | Governing characteristics and allocation |
| --- | --- |
| FHIR information | Governed IU identity/version, history, integrity and access where managed by Harmonia. HAPI FHIR JPA owns provider-side FHIR persistence, indexing and transactions where used. Its JDBC access is provider-side within its storage scope. |
| Subscription models | Durable definitions, associations, configuration and revisions. Preserve the owning selector and delivery semantics. Choose IU or native persistence through the owning model contract. |
| Publication models | Durable PublishingMask and related configuration and revisions. Persistence confers no publication eligibility decision. |
| Pragma-family information | Governed identity, lineage, lifecycle, outcomes and recovery correspondence. Preserve the owning work and execution designs. |
| Recoverable Ergon state | Explicitly durable checkpoints and minimum restart evidence under the owning runtime contract. Working context and its cache remain reconstructable, with recovery based on applicable durable work and authoritative information. |
| Distributed operational state | Persist explicitly durable coordination or recovery facts with their required consistency and recovery semantics. Provider replication supplies no universal coordination guarantee. |
| Messaging | Artemis owns native queue, message, delivery/transaction and broker-recovery semantics. Petasos retains work acceptance, routing, queue management and delivery. DurableData supplies database resilience under the agreed broker-store profile. |
| Audit | Kleio owns mandatory Audit Capture and authoritative immutable FHIR AuditEvent evidence. Preserve the applicable authoritative Operations & Assurance persistence contract, independent audit-target qualifications and fail-closed capture. A database ACK or a durable broker staging write alone cannot establish authoritative capture under the current contract. |
| Media | Governed reference, payload integrity, authorised streaming, preservation, retention and recovery. Use workload-specific capacity and replication and preserve consistent IU/Summary references across independent placement and restore. |
| Configuration and governance | Preserve coherent configuration authority, applicable revisions, secure access and bootstrap/recovery. Topology does not change which component can interpret or alter governed configuration. |

The Clinical Information, Configuration & Governance and Operations & Assurance domains remain semantic domains. Sharding divides responsibility or data by governed placement. Replication preserves a copy of an applicable scope. Neither defines a service or requires any particular count of physical servers. G03's Pragma-family work and Petasos–Kleio staging remain in their pending review. [Mnemosyne domains][mnemosyne] · [Sharding draft][sharding] · [Audit capture authority][kleio] · [G03 review][g03]

## 5. Common HA/DR and independent failure domains

> Common HA/DR architecture SHALL NOT imply common HA/DR failure domains.

Provide reusable provisioning, monitoring, protection, failover, backup/restore and recovery principles. Support multiple independently configured PersistenceService instances and multiple PostgreSQL servers and clusters per site. Allow each workload its own placement, replication/synchronisation policy, availability/durability and recovery objectives, capacity, performance and lifecycle. A PersistenceService can span multiple physical servers and sites under one logical contract. Where distinct physical databases participate in an IU service, preserve one logical authoritative history for that IU across eligible instances. Shared technology or a common service-management framework is compatible with independent operation and recovery.

The deployment design must justify the actual failure domains and correlated dependencies. Analyse compute, storage, power, network paths and sites, as well as orchestration/control planes, quorum/fencing, service discovery/DNS, secrets/PKI and identity, connection routing, backup/WAL archives and restore tools. Separate schemas or processes alone do not establish isolation. Account for common capacity pressure and for shared management-plane failure or upgrade. Foundational services require a recoverable bootstrap order. Define how each can be recovered when a dependency it normally serves is unavailable, including access to configuration, backup indexes, credentials and recovery evidence. Applicability and residual common dependencies require evidence at the service boundary. [Managed Persistence Service draft §§2, 6][managed] · [Availability and DR draft][resilience]

**Acknowledgement follows the owning contract.** For governed IU mutation `Success`, preserve the existing multi-site durability and actual authoritative version requirements. In the accepted two-site case, one surviving site cannot establish a new multi-site Success without another eligible site or explicit contract disposition. Petasos work acceptance, mandatory Kleio capture and media durability/redundancy each require their applicable evidence before acknowledgement. The component's Outcome and consumer disposition remain distinct from database commit, broker acceptance, replication flush, replay visibility, and recovery readiness. If an effect is unknown, preserve `Indeterminate` and reconcile through owning evidence. A timeout on a native or governed path supplies no fact of rollback. [Accepted mutation and Outcome contract][rad-124-contract] · [MC-05 and cache qualifications][rad-124-cache] · [Durable Work Acceptance][durable-work] · [Kleio capture][kleio]

HA concerns continuity under the applicable faults. DR concerns restoration to an authorised state after wider loss, corruption or harmful change. Replication and independently recoverable backups serve different purposes. A deployment must demonstrate fence-before-promotion behaviour and recovery against the applicable RPO/RTO, evidence, integrity and access requirements. This candidate reserves the exact line between DurableData service recovery and wider incident command, site restoration, infrastructure operations and recovery approval. Do not infer a transfer of the existing catastrophic recovery orchestration exclusion. [Accepted Mnemosyne allocations][rad-123-allocation] · [Managed Persistence Service draft §§3, 6][managed]

## 6. PostgreSQL foundation and bounded technical findings

The Solution Architect selects PostgreSQL as the initial **Harmonia-managed** persistence foundation. Investigate native PostgreSQL capability against service requirements before adding specialised storage or additional HA/DR machinery. External providers retain responsibility for their own systems and must demonstrate suitability for the claimed Harmonia service. PostgreSQL implements the database interface and transactions in the proposed foundation. Mnemosyne supplies the applicable database service responsibility under the reviewed allocation.

- **Durability and recovery.** PostgreSQL 16 documents asynchronous streaming replication by default, configurable synchronous standbys and distinct commit wait modes. A local flush, standby filesystem write (`remote_write`), synchronous standby durable flush (with the appropriate `synchronous_standby_names` and `synchronous_commit=on`) and replay (`remote_apply`) are different evidence. Acknowledgement depends on the effective transaction and standby settings and placement. Promotion, fencing, single-writer safety, routing and outage handling need a tested design. Native base backups plus archived WAL enable point-in-time recovery. Qualify independent restore, archive availability and storage guarantees rather than infer them from replication. Version 16 is the documentation basis used here, consistent with the repository's example deployment, and the deployment versions remain for design. [PostgreSQL WAL settings][pg-wal] · [Streaming replication][pg-streaming] · [Failover][pg-failover] · [Backup and recovery][pg-pitr] · [Repository example][compose]
- **Artemis.** The repository-pinned Artemis 2.33.0 lists PostgreSQL support for the JDBC store, which holds broker data such as messages and bindings. Apache documents performance limitations relative to the file journal, particularly for paging and large messages, and native JDBC lock renewal/expiry and connection settings. Assess JDBC-store throughput, actual broker ACK and database durability, shared-store lock safety, connection or database failover, duplicate/replay and transaction recovery. PostgreSQL HA alone supplies no broker failover or Petasos acceptance guarantee. The separate G03 audit candidate requires durability parity for mandatory audit and Petasos work. A JDBC-store design needs to establish that parity using its own commit and failover evidence. [Artemis 2.33.0 JDBC store][artemis-store] · [Artemis HA][artemis-ha] · [Repository pins][pom] · [G03 audit candidate][g03-audit]
- **HAPI FHIR JPA Server.** HAPI 7.2.0 documents PostgreSQL support via its PostgreSQL dialect. Use the HAPI JPA/JDBC persistence path when selected. HAPI owns the provider's application logic, indexes, schema and native transaction semantics. A HAPI/database result remains a provider fact for any governing Harmonia operation. The deployment must qualify HAPI and JDBC versions, indexing/transaction recovery, pool reconnection and the exact authoritative IU evidence required where HAPI is the selected provider. [HAPI 7.2.0 database support][hapi] · [Accepted delegated outcome and verification][model]
- **Other authorised native consumers.** Permit appropriate native interfaces to a declared storage scope under a service contract. Specify required identity, access and transport protection, readiness, isolation, backup/recovery, resource bounds and observed commit behaviour per consumer. Where an operation spans independent systems, its owning boundary must specify correlation, recovery and sufficient proof of each effect. Using PostgreSQL at both ends establishes no shared transaction or common restore point.

### 6.1 Media and large binary content

`bytea` and PostgreSQL Large Objects are the preferred initial candidates. `bytea` uses ordinary table storage and PostgreSQL can use TOAST for large values, with fields bounded to about 1 GB. Large Objects support incremental reads/writes through transaction-scoped handles and a documented limit of 4 TB. These are engine bounds. Determine practical transfer and storage limits under representative streams, concurrency, backup and restore load. PostgreSQL 16 logical replication excludes Large Objects. Evaluate physical replication and recoverable backups for that candidate, or another demonstrated placement and synchronisation design under the applicable durability requirements. [Binary data type][pg-bytea] · [Large Objects][pg-lo] · [Large Object API][pg-lo-api] · [Logical replication restrictions][pg-logical]

Preserve the opaque `ContentMetadata` reference and `InformationCoordinates` resolution. The large-content pass-by-reference exception continues to apply to full reads as governed. An external Summary CREATE registers the supplied reference without retrieval/verification or a change of custody. Harmonia-managed full writes must establish coherent durable Content, reference and Summary for the same authoritative version. If media and metadata use separate service instances, require evidence that the referenced bytes meet the applicable integrity and durability contract before reporting governed success. Recovery must reconcile references, versions and physical payloads before reopening the affected path. Partial writes, independent restores, holds and orphan handling require owning lifecycle and recovery decisions. A database-native Large Object identifier or deletion operation does not establish governed IU identity or disposition. [Accepted full/summary contracts][rad-124-contract] · [Portable Content and reference authority][rad-125-iu] · [Media recovery feature][media-recovery]

Permit media-oriented PostgreSQL servers and clusters in each site with separate placement, replication and failure domains where the workload warrants it. Assess object sizes and counts, streaming resource use, integrity verification, synchronisation, backup and restore, and consumer and provider lifecycle. Keep an alternative storage technology as an option only where a demonstrated requirement justifies it.

## 7. Architectural reconciliation and authority gaps

| Surface | Finding against Architecture 1.1.26 and candidate treatment |
| --- | --- |
| **Search shorthand** | The issued diagram's `GovernedSearch` is broader wording than the exact accepted five-service contract. The draft preserves the sole `GovernedSummarySearch` exposure and both full/summary read and write distinctions. Other search stages or releases require their owning design authority. [RAD 1.1.24 DR-1][rad-124-contract] |
| **Database-service management versus IT/Ops** | The new direction assigns DurableData deployment, service management, resilience and recovery. The existing Mnemosyne role distinguishes service oversight from infrastructure management and excludes catastrophic recovery orchestration. The proposed allocation needs explicit resolution of service versus infrastructure operation, site restoration, disaster command/approval and the authorities for promotion/fencing. Bounded service provision can be described while this division remains pending. [Mnemosyne responsibility contributions][mnemosyne] · [RAD 1.1.23 DR-2][rad-123-allocation] · [Managed Persistence Service draft][managed] |
| **Native database access versus AX-05** | A native endpoint for an authorised consumer's own persistence scope is consistent with the lower provider boundary. Application access to authoritative managed IU state via raw storage, or native writes that bypass its governance, would conflict with AX-05 and repository guardrails. Define the permitted native service scopes, meaning of authoritative data in each, and enforceable separation of provider-side access from application-facing access. [AX-05][ax05] · [Repository boundaries][agents] · [Lower interaction draft][provider-interaction] |
| **Existing PersistenceService versus native service** | The accepted PersistenceService is jointly realised by GPF and Persistence Adapter and qualified for governed provider operations. A directly consumed native service extends the current model. No replacement, reclassification or exact native contract identity is established. Reuse the existing service/instance concept and reconcile the model and GPF/adapter scope explicitly before incorporation. [RAD 1.1.23 DR-1][rad-123] · [Canonical PersistenceService][persistence-service] |
| **Topology and providers** | The earlier Managed Persistence Service draft already considers PostgreSQL and several R1/R2 topologies. These are deployment prior art, including its unelected orchestration proposals. They provide no one-database-per-site rule. The PostgreSQL foundation applies to Harmonia-managed service evaluation without erasing the accepted provider capability asymmetry or requiring external providers to convert. [Managed Persistence Service draft][managed] · [Canonical PersistenceService][persistence-service] |
| **Acknowledgement and recovery** | Workload-specific replication may meet different contracts. Weaker local acknowledgement for governed IU mutation Success conflicts with AV/MC commitments. A durable broker or database ACK cannot substitute for Kleio's current capture boundary. Different media storage leaves FC-01 coherence and external-content SEP intact. Preserve the existing guarantees pending any separately reviewed change. [RAD 1.1.24 DR-1][rad-124-contract] · [RAD 1.1.24 DR-3][rad-124-cache] · [Kleio][kleio] |

## 8. Decisions and evidence required for formal review

1. **Continuum correspondence.** Confirm the allocation and representation of DurableInformation and DurableData within Mnemosyne, including the five tiers, GPF scope, adapter boundaries, host/storage deployment versus service management and ordinary versus catastrophic recovery. Determine the exact `PersistenceService` and instance/interface correspondence using existing identities where sufficient.
2. **Native contract and eligibility.** Specify who may use each interface, which data and operations it exposes, who controls schema and application semantics, applicable Themis/native enforcement, administrative controls and how commit/recovery evidence reaches the owning boundary.
3. **Discovery and connection.** Specify database discovery, role-aware writer and reader routing, identity, credential lifecycle and profile evidence after reconnection or safe failover.
4. **HA/DR allocation.** Establish workload profiles and failure-domain evidence, including ACK threshold, read visibility, RPO/RTO, continuity and safe degradation, physical placement, replication, coordination, fencing, backup independence and restore. Account for the prior HMP-01–HMP-12 matters in their existing scope.
5. **Artemis and HAPI qualification.** Verify JDBC durability and ACKs, locks/failover and transaction recovery for Artemis, plus throughput and paging at representative load. Verify HAPI's native persistence and index recovery and what IU evidence its integration provides. Preserve the separate decision on the Kleio staging boundary.
6. **Media lifecycle.** Decide placement, object size classes and lifecycle, bytea/Large Object suitability, governed reference semantics, integrity and acknowledgement, orphan/hold handling, synchronisation and metadata/payload recovery correspondence.
7. **Transactions across systems.** Specify the owning reconciliation and proof for partial commit, lost reply, retry and independently restored state. Tests must distinguish provider ACK, durable effect, remote visibility and consumer response before asserting guarantees.
8. **Independent recovery.** Verify recovery order and absence of circular bootstrap dependencies under failure of shared infrastructure, including exhausted capacity, lost credentials or service discovery, replica/site loss and an unavailable backup or control plane. Establish who can reopen each service and what verifies authoritative state before that decision.

Candidate review should verify preservation of the five governing services and identities, trace native access independently of IU access, and compare deployments with shared versus isolated resources against the workload profiles. Technical verification requires representative loss, failover, restore, media integrity/streaming and lost-acknowledgement cases against the chosen effective configurations. This capture establishes no measured deployment capability.

## 9. Source reconciliation and review standing

| Source | Treatment in this proposal |
| --- | --- |
| [Mnemosyne authoritative model Final.001][model] and [RAD 1.1.23 DR-1/2][rad-123] | Preserve accepted allocations and actual incorporated correspondence. Earlier Draft.001/.002 are lineage. The proposal raises the service/operations boundary and native service identity for explicit review. |
| [Mneme–Mnemosyne Final.001][interaction], [RAD 1.1.24 DR-1/3][rad-124-contract] and [RAD 1.1.25 DR-4][rad-125-iu] | Preserve the accepted five-service and IU/cache/Outcome semantics. Completed HCRLs establish the actual standing where source drafts or elements still carry older interim wording. |
| [Mnemosyne–PersistenceService Draft.002][provider-interaction] | Use its representation freedom and capability asymmetry as design correspondence. Its native boundary, attribution and correlation questions retain pending standing. |
| [Availability, Resilience and DR Draft.002][resilience] and [Harmonia Managed Persistence Service Draft.001][managed] | Carry ordinary recovery, separate catastrophe orchestration, provider suitability, PostgreSQL/backup findings and unresolved service requirements forward. The deployment alternatives and management allocation require their own disposition. |
| [Sharding Draft.002][sharding] | Retain the distinct meaning and unresolved allocation of governed partitioning. The present direction provides no shard selection or redesign. |

**Decision** · Pending Solution Architect reconciliation. The attributable source is the Solution Architect's instruction titled “Harmonia — Mnemosyne DurableInformation and DurableData”. This draft applies that direction at candidate level, with Architecture 1.1.26 as the baseline. It is a standalone design capture. A target and iteration for formal review have yet to be assigned. The separately governed G03 Pragma-family and Petasos–Kleio decisions remain pending under their existing HCRL. [Architecture Change Provenance][provenance] · [G03 review][g03]

[release]: ../../../architecture-release.json
[praxis]: ../../governance/architecture-review-praxis.md
[provenance]: ../../governance/architecture-change-provenance.md
[axioms]: ../../governance/architectural-axioms.md
[ax05]: ../../continuum/motivation/principle/0412a2084f0145babc4f022c71fea65f.md
[ax07]: ../../continuum/motivation/principle/architectural-axiom/5de2ae2c3b794c2d9f3a14efd51d564f.md
[agents]: ../../../AGENTS.md#invariant-8-mneme--mnemosyne-state-separation
[data-001]: ../../continuum/motivation/requirement/15c71ccd5d714503891aece2a23445e1.md
[authoritative-feature]: ../../continuum/strategy/capability/0d5bd68ff50f410aaceed05377e1a4df.md
[durable-work]: ../../continuum/strategy/capability/0d036c0bb26b47dda1cf5d2c1510a083.md
[audit-availability]: ../../continuum/strategy/capability/feature/886764d4ba954c68be0f030fe027012b.md
[media-recovery]: ../../continuum/strategy/capability/feature/0045d0fde0314b9dbcdcf8c1498a475e.md
[mnemosyne]: ../../continuum/application/application-component/7985e647bf9e4ca3ba70faf9e3e4ae02.md#architectural-content
[persistence-service]: ../../continuum/application/application-service/c050edfefe8b4d2b8892bc94b2d27a5c.md#architectural-content
[kleio]: ../../continuum/application/application-component/fefda110e2274884ad58906733606efd.md#auditability-and-availability
[rad-123]: ../register/1.1.23/hcrl-1.1.23-001.complete.md#dr-1
[rad-123-allocation]: ../register/1.1.23/hcrl-1.1.23-001.complete.md#dr-2
[rad-124-contract]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-1
[rad-124-cache]: ../register/1.1.24/hcrl-1.1.24-001.complete.md#dr-3
[rad-125-iu]: ../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-4
[model]: DRP-Mnemosyne-Authoritative-Persistence-Service-Model.Final.001.md
[interaction]: DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md
[provider-interaction]: DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md
[resilience]: DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md
[managed]: DRP-Harmonia-Managed-Persistence-Service.draft.001.md
[sharding]: DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md
[g03]: ../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[g03-audit]: DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md
[pom]: ../../../pom.xml
[compose]: ../../../docker-compose.yml
[pg-wal]: https://www.postgresql.org/docs/16/runtime-config-wal.html
[pg-streaming]: https://www.postgresql.org/docs/16/warm-standby.html
[pg-failover]: https://www.postgresql.org/docs/16/warm-standby-failover.html
[pg-pitr]: https://www.postgresql.org/docs/16/continuous-archiving.html
[pg-bytea]: https://www.postgresql.org/docs/16/datatype-binary.html
[pg-lo]: https://www.postgresql.org/docs/16/lo-intro.html
[pg-lo-api]: https://www.postgresql.org/docs/16/lo-interfaces.html
[pg-logical]: https://www.postgresql.org/docs/16/logical-replication-restrictions.html
[artemis-store]: https://github.com/apache/artemis/blob/2.33.0/docs/user-manual/persistence.adoc
[artemis-ha]: https://github.com/apache/artemis/blob/2.33.0/docs/user-manual/ha.adoc
[hapi]: https://github.com/hapifhir/hapi-fhir/blob/v7.2.0/hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa/database_support.md
