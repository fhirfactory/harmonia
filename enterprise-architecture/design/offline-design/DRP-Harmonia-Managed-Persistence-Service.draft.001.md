<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0 -->
# Harmonia — Design Review Proposal (DRP)

**DRP:** `DRP-Harmonia-Managed-Persistence-Service-001`  
**Title:** Harmonia-Managed Persistence Services — PostgreSQL, Site Resilience and R1/R2 Deployment Candidates  
**Status:** Architectural review candidate; established decisions distinguished from deployment proposals and unresolved matters  
**Prepared for:** Solution Architect / Architecture Review  
**Prepared by:** ChatGPT (architecture peer)  
**Date:** 13 November 2026  
**Working architectural context:** Harmonia 1.1.24, `Mneme-and-Mnemosyne-Build`  
**Intended repository location:** `enterprise-architecture/design/offline-design/DRP-Harmonia-Managed-Persistence-Service.draft.001.md`

> **Standing and limits.** This DRP consolidates the explicit decisions made in the current Harmonia architectural discussion concerning *where site-level authoritative-information redundancy is managed* and the choice of PostgreSQL for **Harmonia-managed** PersistenceServices. It also reconciles, without silently accepting open questions, the earlier Mnemosyne availability/resilience/recovery draft. R1/R2 deployment patterns, Patroni/CloudNativePG use, configuration details and numerical service objectives are **candidates for review**, not already-adjudicated decisions. No repository/Architecture Continuum changes are authorised merely by drafting this document.

---

## 0. Authority, boundaries and source succession

### 0.1 Governing accepted architecture

This DRP is subordinate to, and does not reauthor:

1. `DRP-Information-Unit-Framework.Final.001.md` — accepted IU identity, representation, InformationCoordinates, LifecycleContext, version and governed-state meaning (incorporated at 1.1.22).
2. `DRP-Mnemosyne-Authoritative-Persistence-Service-Model.Final.001.md` — Mnemosyne's five tiers, authoritative persistence/acquisition, GPF and Persistence Adapter responsibilities, provider-capability asymmetry, authoritative Outcomes and distributed Mnemosyne registration/selection (incorporated at 1.1.23).
3. The accepted Architecture Continuum at `enterprise-architecture/continuum/`, including applicable Information Unit, Data Steadfastness, accepted-work durability, security, retention, assurance, operational continuity and non-functional requirements.

Adjacent designs requiring separate review remain:

- `DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md` and the 1.1.24 interaction-focused review — the upper GovernedReader/Writer/Search boundary, effective transaction identity context, Outcome and availability semantics.
- `DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md` — lower interaction, provider-specific representation and capability freedom.
- `DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md` — **partitioning/sharding**, not the same as physical redundancy or replication; remains a distinct unadjudicated DRP.

### 0.2 Availability / recovery source succession

`DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md` provides direct source input. Its substantive constraints are accounted for here and by the accepted Mnemosyne model: authoritative IU recoverability, operational availability versus disaster recovery, no cache-only authoritative recovery, correct failure/degradation reporting and technology-independent native recovery options. **Its outstanding review questions are preserved in §11, not declared answered.** After review, it may be explicitly superseded if reconciliation confirms full coverage.

### 0.3 Scope

This DRP answers: **What manages authoritative persistence redundancy across geographically separated sites when a PersistenceService is operated by Harmonia?** It then explores suitable PostgreSQL-based deployment architectures for R1 and R2.

It does **not** redefine Mneme's distributed IU cache, impose a Mnemosyne peer-synchronisation protocol, define a complete PostgreSQL administration runbook, solve Alpha/Beta managed release coordination, or adjudicate sharding.

---

## 1. Architectural intent and established decisions

### 1.1 The central Mneme concept — retained

Mneme's architectural centre is its **single logical distributed IU cache and management domain**, not a collection of unrelated site-local IU caches. Mneme manages operational IU state and **concurrency for an individual IU** across participating cache nodes. Its distributed management domain may span geographically separated sites.

R3+ may introduce separate domains, e.g. `Mneme-Distributed-Cache[Alpha]` and `[Beta]`, for higher-level managed release (RED/BLUE-style) activities. These domains do **not** require cache-to-cache synchronisation. The managed release programme, not this DRP, coordinates permitted write activity and changeover between them. Authoritative persistence consistency remains the responsibility of the persistence tier. R3+ Alpha/Beta specifics are **out of scope** for R1/R2 deployment selection.

### 1.2 Mnemosyne instances and persistence paths

Multiple Mnemosyne instances may be simultaneously available and registered for overlapping IU types. Mneme discovers capability registrations and selects eligible instances. The persistence paths exposed through different Mnemosyne instances may terminate at **different PostgreSQL nodes/sites and different physical databases** that form one governed *logical authoritative persistence service* for the relevant IU.

**Clarification of accepted 1.1.23 terminology:** “same logical authoritative persistence engine/destination” is a statement of **one logical authoritative information history**, not a requirement that all Mnemosyne instances connect to the same physical database process. PostgreSQL primary/standby replicas and controlled failover are examples of physical realisations under that logical authority.

IU identity is a governed UUID/Topic identity and is **not constructed from PostgreSQL row IDs, database coordinates, site identity or database-native transaction IDs**. Provider-native representation is separate from governed identity and version semantics.

### 1.3 Site redundancy — accepted ownership allocation

| Responsibility | Architectural allocation |
|---|---|
| Distributed IU active-state management and individual-IU concurrency | **Mneme** (within its logical cache domain) |
| Eligibility discovery, Mnemosyne instance selection/changeover | **Mneme's Mnemosyne Adapter (Store)** |
| Authoritative IU persistence/acquisition and factual Outcome reporting | **Mnemosyne**, through its accepted five-tier architecture |
| Persistence workflow and supervision | **Mnemosyne GPF** |
| Provider-native execution, acknowledgement and verification | **Mnemosyne Persistence Adapter** |
| **Site-level authoritative-data replication/synchronisation and consistency** | **Applicable PersistenceService / persistence infrastructure** |
| Catastrophic recovery orchestration, site restoration and managed recovery approval | **Wider operational/DR capability**, not Mnemosyne acting alone |
| R3+ Alpha/Beta release switchover/write coordination | **Managed release programme** |

Consequently, **no new Mneme-to-Mneme, Store-to-Store or Mnemosyne-to-Mnemosyne IU replication or synchronisation mechanism is required** for site redundancy. Such a mechanism is not created as a fallback merely because a provider's own resilience is inadequate.

### 1.4 Harmonia-managed versus externally managed services — accepted

- **Harmonia-managed PersistenceService:** PostgreSQL is the selected default persistence engine. PostgreSQL's native WAL/replication features form the data-synchronisation basis where multi-site persistence is required. Suitable FOSS HA/DR orchestration may supplement PostgreSQL; the choice and deployment of orchestration remain to be adjudicated. Separate PostgreSQL instances/clusters can be used for different problem domains; **one global database is not required**.
- **Externally/non-Harmonia-managed PersistenceService:** the provider manages its own persistence technology, synchronisation and availability — **SEP**. Harmonia still establishes deployment-specific performance, availability, integrity, durability and recovery requirements and evaluates provider suitability. SEP is not an exemption from a governed service contract.
- Mnemosyne must not assert a persistence guarantee that the underlying service cannot substantiate. External providers may expose different additive capabilities; the accepted Mnemosyne architecture does not demand an artificial universal native feature set.

### 1.5 Invariants already established

1. Acknowledged *active-cache* success is not proof of *authoritative durable persistence*.
2. Request receipt, accepted durable work, verified provider execution, authoritative persisted effect and remote replication/availability are distinct facts.
3. Mnemosyne reports factual `Outcome` (`Success | Failure | Indeterminate`) against the specified operation/stage; Mneme determines consumer-level disposition. A timeout alone proves neither success nor failure.
4. Replica identity or version inconsistency must never be silently normalised into a new authoritative IU history.
5. Site unavailability must not silently substitute a stale replica read for a current authoritative state when the operation requires currency.
6. Site/network/storage failure is an anticipated condition; authoritative persisted state cannot depend solely on a volatile cache for recovery.
7. RETIRE is an information lifecycle/disposition concern, not a synonym for “replicated”, “unavailable” or native physical deletion.
8. The applicable Themis, audit, provenance, integrity, retention and preservation obligations remain effective during failover and recovery.

---

## 2. Conceptual deployment and failure domains

The intended multi-site arrangement can be represented without peer-to-peer Mnemosyne synchronisation:

```text
                  Mneme-Distributed-Cache[Alpha]
               (one logical distributed IU domain)
                       /             \
                  Site A              Site B
                    |                   |
               Mnemosyne-A         Mnemosyne-B
                    |                   |
            Persistence Adapter  Persistence Adapter
                    |                   |
            PostgreSQL path A   PostgreSQL path B
                    |                   |
               PostgreSQL A  <-- WAL replication --> PostgreSQL B
                    \________ one logical authoritative ________/
                              information history
```

The diagram is **conceptual**: PostgreSQL's conventional physical replication topology is a *single writable primary* with one or more standbys, not simultaneous independent writes to both databases. Mnemosyne paths must route writes to the current legitimate primary, even when a local site's PostgreSQL server is a read-only standby. The diagram does not assert that either site can continue writing independently while the inter-site connection is partitioned.

Geographic separation in excess of **100 km** is a resilience *design objective discussed for candidate sites* (regional power, seismic and network disruption). It is not a claim that distance alone ensures fault independence. Shared carriers, identity/PKI services, DNS, quorum placement, management control planes and backup locations must also be considered.

**Two-site quorum caution:** two physical sites alone do not automatically supply an unambiguous majority or safe automated failover during network partition. An independent third failure domain/witness or a deliberately conservative failover policy is a candidate measure; exact quorum topology is a deployment decision.

---

## 3. PostgreSQL — chosen persistence engine and demonstrated mechanisms

### 3.1 Capabilities relevant to Harmonia

PostgreSQL supplies the following *native mechanisms*:

- **Physical WAL streaming replication** — one primary replicates database changes to standby servers; suitable for replication of a coherent PostgreSQL cluster/database system.
- **Asynchronous replication** — the primary may acknowledge a commit before a standby receives the corresponding WAL; non-zero lost-write risk exists if the primary site catastrophically fails before propagation.
- **Synchronous replication** — with an appropriate `synchronous_standby_names` policy and `synchronous_commit=on`, transaction completion can wait until required standby WAL has been flushed to durable storage.
- **`synchronous_commit=remote_apply`** — the requested synchronous standby has replayed the transaction and can make it visible to queries, subject to proper read routing.
- **Standby promotion and recovery primitives** — usable by an HA controller but not themselves a complete automated cross-site orchestration solution.
- **Physical backups, continuous WAL archiving and point-in-time recovery** — needed for recoverability from error, corruption or undesired change in addition to replication.

PostgreSQL physical replication **does not inherently provide multi-primary writable consistency**. Logical replication is a different capability; introducing it for bidirectional writes would require a separately reviewed conflict/identity/consistency design and is **not** assumed here.

### 3.2 Durability, visibility and Outcome semantics

| Conceptual fact | Candidate PostgreSQL evidence | Architectural caution |
|---|---|---|
| Locally persisted | Primary's committed/flushed WAL and provider verification | Does not prove remote durability |
| Remotely durably propagated | Required synchronous standby has acknowledged durable WAL flush | Does not necessarily prove remote query visibility |
| Remotely available for retrieval | Required standby has replayed WAL (`remote_apply`) and safe routing is possible | Is not a universal guarantee for every standby or every query routing path |
| Asynchronous distribution in progress | Replication state/LSN/lag evidence | A target or expected lag is not a proven completed state |
| Safe promotion/recovery | Appropriate leader/standby qualification, fencing and verification | Merely having a replica does not prove safe failover |

The earlier discussion recalled **local-available-after-write** and **distributed-available-after-write** concepts. These remain useful candidate *completion/availability semantics*, but their **exact names, availability scope, evidence threshold, lifecycle encoding and required usage have not yet been adjudicated** in the interaction DRP. This DRP does not add Outcome statuses or an IU lifecycle state machine.

### 3.3 Provider capability verification

Mnemosyne reports the factual operation Outcome based on evidence made available by its selected PersistenceService through the Persistence Adapter. It is **not** responsible for re-implementing PostgreSQL's replication protocol. Its existing GPF coordinates delegated workflow and supervision; native PostgreSQL acknowledgement/verification remains with the adapter. A “write acknowledged locally” fact must not become a “remote-available” assertion by inference.

### 3.4 HA orchestration: PostgreSQL plus Patroni is a strong candidate, not an accepted product decision

**Patroni** is a FOSS (MIT-licensed) PostgreSQL HA coordinator. It can supervise PostgreSQL nodes, use a distributed configuration store (for example etcd/Consul or Kubernetes API integration), coordinate leadership, manage synchronous-standby selection and facilitate failover. **Patroni is not the database replication engine**: PostgreSQL performs the WAL replication.

For a design prioritising acknowledged-write preservation, Patroni's synchronous/quorum and strict behaviours warrant explicit investigation. **Synchronous mode alone must not be mistaken for an unconditional remote-durability guarantee**: some modes can fall back to primary-only writes when no suitable synchronous standby is available. Strict mode may instead suspend writes. Client transaction-level `synchronous_commit` overrides, failure/forced promotion, cancelled acknowledgements and standby selection must be evaluated against the actual service obligation.

Candidate supporting components include a safely placed DCS quorum, role-aware client routing/proxying (e.g. HAProxy/PgBouncer where appropriate), monitoring, backups, WAL archives, fencing and operational runbooks. None is automatically a mandatory new Harmonia Information System or named architectural component.

### 3.5 Backup is independent of replica availability

Replication faithfully propagates some undesirable changes as well as legitimate ones. Database replicas therefore do **not** replace independent recoverable backups, WAL archiving, retention and tested point-in-time restores. Backups must survive loss of the primary site and meet applicable security and governance requirements.

### 3.6 Technology and data-management scope

- PostgreSQL may run as multiple independent **domain-oriented clusters/instances**; this DRP does not conflate Clinical, Configuration/Governance, and Operations/Assurance responsibilities into one physical database.
- Native large binary content need not be stored as PostgreSQL BLOBs. Harmonia's existing large-content/pass-by-reference and InformationRepresentation boundaries continue to apply.
- Physical replication does not itself establish cross-cluster consistency between *different* PostgreSQL clusters/domains; each logical authoritative data domain requires an explicit consistency/recovery scope.
- Security for database replication/control/backups requires authenticated, encrypted connections and appropriate least-privilege infrastructure identities, independent of transaction-level Themis governance.

---

## 4. R1 candidate deployment architectures (Ubuntu Server: VMs and containers)

The accepted R1 platform direction is **Ubuntu Server, with VM and container deployment options**. R1 must retain the accepted ordinary single-node-failure resilience and accepted-work durability objectives. Site-level disaster recovery expectations and numeric recovery objectives must be assigned explicitly rather than inferred from platform choice.

### R1-A — PostgreSQL on Ubuntu VMs; Patroni-managed primary/standby; site-separated replica (**preferred evaluation baseline**)

```text
  Site A:  Mneme nodes + Mnemosyne A       Site B: Mneme nodes + Mnemosyne B
           PostgreSQL Primary <== WAL ==> PostgreSQL Standby
           Patroni agent                  Patroni agent
                  \_______________________/
                    DCS quorum / qualified witness placement
  Separate: backup+WAL archive, role-aware routing, monitoring, recovery control
```

**Candidate:** PostgreSQL on independently failed VM hosts/storage; Patroni agents with a resilient DCS; role-aware route to current primary; remote standby; site-independent backup/WAL archive. An independent voting failure domain or conservative site-failure runbook must avoid unsafe two-site promotion.

**Strengths:** concrete, mature FOSS building blocks; independent database lifecycle from Mneme/Mnemosyne containers; comparatively simple operational control for R1; portable into R2 if desired.

**Risks/trade-offs:** inter-site RTT increases synchronous commit latency; loss of remote sync standby may suspend required durable writes; DCS quorum and fencing are critical; remote read safety depends on replication/apply and routing; VM/database operations require disciplined automation.

**Evaluation:** compare asynchronous replication for non-zero-RPO classes against synchronous/strict policies for classes requiring stronger durability. Do not choose a uniform policy without information-classification and measured NFR evidence.

### R1-B — Containerised PostgreSQL nodes managed with host-level HA tooling (**alternative / proof candidate**)

**Candidate:** PostgreSQL and Patroni run in containers on Ubuntu hosts/VMs; durable PostgreSQL volumes and WAL/archive lifecycle remain explicit; HA coordination is outside the containers or uses appropriately deployed Patroni/DCS.

**Strengths:** consistent packaging and reproducible deployment; closer to a container-first operations model.

**Risks/trade-offs:** the presence of containers does not supply database HA or cross-site failover by itself; restart/routing/volume/fencing correctness must be proven. Avoid creating an ad-hoc container scheduler for database HA simply to avoid host-based PostgreSQL deployment.

**R1 candidate conclusion:** R1-A is the **recommended implementation assessment starting point**, not yet an accepted deployment. R1-B remains valid when operational evidence supports it. Existing tooling choices support **Maven/Jenkins** for build/release workflows and **Ansible where needed** for repeatable infrastructure provisioning, configuration and recovery preparation.

---

## 5. R2 candidate deployment architectures (Ubuntu + OpenShift preferred)

The R2 direction includes **OpenShift preferred** for container orchestration. PostgreSQL's underlying authoritative and replication semantics remain unchanged. Kubernetes/OpenShift does not, by its mere presence, solve cross-site database leader selection or DR.

### R2-A — OpenShift-managed Harmonia application services; PostgreSQL HA remains on Ubuntu VMs (**continuity candidate**)

**Candidate:** Mneme/Mnemosyne services operate in OpenShift; PostgreSQL remains a Patroni-managed VM-based cluster and remote site replica. OpenShift services connect through a stable, secured, role-aware database endpoint.

**Strengths:** reuses the established R1 database operational model; independently upgrades application platform and database infrastructure; avoids an unnecessary database migration at the R2 transition.

**Risks/trade-offs:** two operational platforms; network and identity boundaries; care required around OpenShift-to-VM connectivity, service discovery, locality and database connection management.

### R2-B — PostgreSQL managed by a FOSS Kubernetes operator, with site-level replica topology (**cloud-native candidate**)

**Candidate:** PostgreSQL runs as managed stateful workloads under an operator such as **CloudNativePG** (Apache-2.0) on OpenShift, with separately placed replicas/replica clusters as appropriate. Operator-managed backups/WAL archiving and declarative configuration complement rather than replace PostgreSQL replication.

**Strengths:** native Kubernetes reconciliation, service handling, backups and operational automation; plausible full OpenShift operating model.

**Risks/trade-offs:** independent site / Kubernetes-cluster failure is not the same as pod failure. In particular **CloudNativePG does not natively automate promotion across separate Kubernetes clusters**; this requires an appropriate external/multi-cluster decision-maker or managed DR procedure. OpenShift compatibility (SCC/permissions, storage, supported versions), location of consensus, fencing and RTO/RPO remain to be qualified. Do not assume arbitrary stretched-cluster WAN latency is acceptable.

### R2-C — Hybrid: operator-managed local HA, separately governed multi-site DR (**evolution candidate**)

**Candidate:** Within each site/cluster, a selected operator maintains local PostgreSQL availability. A remote PostgreSQL replica cluster is maintained for disaster recovery, with an explicit authority-controlled promotion/switchover and route-redirection process. This can be combined with B or staged from A.

**Strengths:** separates ordinary site-local automatic HA from regional disaster recovery; avoids falsely claiming automated cross-cluster failover; supports a conservative initial R2 operational model.

**Risks/trade-offs:** may have longer cross-site RTO, requires recovery exercises and clear restoration authority; async DR may entail non-zero RPO; sync cross-site choices may reduce write availability.

**R2 candidate conclusion:** Evaluate **R2-A for low-risk continuity** and **R2-B/C for cloud-native operation**. A single choice is **not yet accepted**. The chosen R2 arrangement must demonstrate the same governed persistence semantics as R1; a different implementation must not redefine IU or Outcome semantics.

---

## 6. Service guarantees and deployment-time objectives

### 6.1 Specification principle

Architectural obligations must be independently measurable. **Numerical values are intentionally not invented here.** Different information classes/domains and externally managed providers may require different qualified profiles. A performance target cannot be substituted for a correctness assertion.

### 6.2 Candidate measurable service parameters

| Parameter | What deployment must define/verify |
|---|---|
| **Authoritative write acknowledgement** | Which event suffices: local durable commit, remote durable flush, remote replay or another explicitly agreed guarantee |
| **Local read-after-write** | Earliest safe observable retrieval through local authorised path, including read-your-writes evidence |
| **Distributed read-after-write** | Earliest guaranteed retrieval through required alternate site/path, including currency/version checks |
| **Replication lag** | Time and/or WAL/LSN distance, permitted thresholds, distribution and sampling method |
| **Distributed visibility latency** | End-to-end latency from specified committed write to remote retrieval availability; percentile/maximum and test method |
| **RPO** | Maximum allowable authoritative information loss for each defined failure/disaster class |
| **RTO** | Time to restore applicable authoritative service for each defined fault class |
| **Availability** | Scope (instance, node, site, regional), measurement window, degradation and allowable read/write restrictions |
| **Commit latency and throughput** | Percentiles under representative workload, including cross-site network conditions; no invented per-adapter benchmark |
| **Failure/failover qualification** | Time to detect failures, qualify candidate, fence former primary, switch routes and resume safely |
| **Integrity/identity/version** | Demonstrable preservation of IU identity, version/history, no silent stale reads or divergent committed histories |
| **Backup and recovery** | Backup/WAL freshness, successful restore/PITR drills, recovery evidence and retention tests |
| **Observability/evidence** | Source of assertions, timestamp/correlation, audit-versus-operational evidence, missing/unknown state reporting |

The accepted wider event-load objective (100,000 incoming events in eight hours, completion within 24 hours) remains a **whole-system requirement**, not a standalone PostgreSQL node throughput figure.

### 6.3 Requirements by failure mode

At minimum, verification should separately examine: PostgreSQL process crash; VM/node failure; standby failure; network interruption; inter-site partition; primary site destruction; simultaneous or correlated site/control-plane failures; WAL archive loss; corrupted replica/storage; stale replica read; failed promotion; return/rejoin of an old primary; and recovery of a previously acknowledged write. For each, distinguish **service availability**, **authoritative-state correctness**, **durability** and **recovery evidence**.

**Conservative failure behaviour:** when a complete authoritative view cannot be established, a component must not manufacture `Success`, silently downgrade a requested durability level or return an older version as conclusively latest. The service can report supported failure/uncertainty under the owning contract.

### 6.4 Safe degradation

A deployment may provide different operation classes (e.g., read-only on a standby during partition), subject to authorisation and freshness requirements. It must not silently switch from an agreed distributed durable-write guarantee to a local-only write and present the result as equivalent. Degraded operation needs defined acceptance/reporting semantics and operational authority.

---

## 7. External/non-Harmonia-managed PersistenceServices

External persistence implementations retain their own technology, internal replication, availability and operational responsibility. Harmonia's architecture requires **observable conformance**, not infrastructure ownership.

For a deployment, establish service-specific suitability and evidence: governed read/write/search functions claimed, native or translated IU semantics, authoritative version/currency, identity preservation, security/audit controls, error and uncertainty reporting, provider acceptance/commit evidence, available read-after-write behaviour, availability/latency, replication/DR characteristics where exposed, and contractual recovery limits.

Where a provider cannot substantiate a requested distributed availability guarantee, Mnemosyne shall report only what is established. A lower resilience class may be acceptable for some assigned information **only if** that assignment is explicitly permitted by governance/deployment requirements. Mneme and Mnemosyne are not implicitly tasked with creating a substitute cross-site replication layer for such a provider.

---

## 8. Relationship to the two Interaction DRPs

### 8.1 Mneme → Mnemosyne (under separate 1.1.24 review)

The upper boundary remains the accepted logical `GovernedReader`, `GovernedWriter` and `GovernedSearch` services, with R1/R2 synchronous request/response and the Calliope Outcome model. Transaction identity context is propagated independently of the operationally elevated component profiles; Themis governs authorisation. Mneme may select a new Mnemosyne instance, but only a compatible authoritative information view—not mere IU-type registration—can justify an authoritative result.

A request for a particular durability/availability guarantee must have an unambiguous agreed meaning. The exact names/shape of write/update availability conditions remain for the owning interaction adjudication. **No new asynchronous Mneme–Mnemosyne callback or replication protocol is introduced by this DRP.**

### 8.2 Mnemosyne → PersistenceService (under separate review)

The lower boundary exposes enough native evidence/capability for Mnemosyne to report the correct Outcome, **without prescribing a universal `InformationUnit`-shaped provider contract**. PostgreSQL-specific replication/status interrogation belongs to its selected provider adapter and operational instrumentation, not to the public governed Interaction. Different external services may legitimately provide different measurable guarantees.

---

## 9. Distinguish replication from sharding

`DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md` remains separate and open. Sharding divides an authoritative information population across partitions; replication makes copies of the relevant authoritative state within a logical domain. A shard may itself be replicated. Neither feature automatically supplies the other.

The sharding draft's unanswered questions remain: who authorises semantic partitionability; whether shard membership is governed durable metadata; re-sharding invariants; and whether any domains must prohibit divergent partitioning. This DRP **does not adjudicate or supersede** those questions, create a new shard-management mechanism, or allocate shard ownership on the strength of PostgreSQL replication alone.

---

## 10. Feature realisation, governance and architectural incorporation

### 10.1 Business/enterprise capability contribution

This design contributes to the accepted 1.1.20+ capability model's established authoritative persistence, Information Unit Data Steadfastness (availability/recoverability), integrity, distributed information access, resilient operations and audit/assurance Features as applicable. The prior drafts' `DataManagement.InformationUnits.*` paths are **historical**, not automatic canonical identifiers.

### 10.2 Motivation and deployment evidence

Reconcile against actual existing Requirements, NFRs and relevant Axioms in the Architecture Continuum before allocating any new Motivation/Capability elements. Distinguish architecture obligation from a deployment's chosen numeric targets and implementation technology. Avoid creating duplicate Features or an architecture-specific PostgreSQL replication information model unless justified by a stable cross-solution contract.

### 10.3 Security, continuity and operational responsibilities

- Component credentials used to connect to the DB are infrastructure credentials, not substitutions for propagated transaction identity/authority.
- Themis-governed disclosure, Kleio AuditEvents, applicable Dokimasia assurance/provenance and data preservation/retention remain binding during failover.
- Restoration, catastrophic incident command, controlled promotion and verification authority belong to the wider operational/DR model; Mnemosyne supports evidence and operations within its scope but does not become the whole enterprise DR orchestrator.
- Service-health and replication signals are candidates for the existing Harmonia observability architecture; this DRP does not create a new observability agent or management interface.

### 10.4 Proposed incorporation disposition

After Solution Architect review, incorporate the **accepted responsibility allocation and invariants**, link the applicable Features/Requirements/Interactions, record selected R1/R2 reference deployment(s) and their target guarantee profiles as deployment design evidence, and preserve deferred matters. **Do not use this draft as an instruction to have Codex or Junie invent new architecture.**

---

## 11. Matters explicitly unresolved for Solution Architect review

These remain open, irrespective of whether the availability/recovery draft is eventually superseded:

| ID | Question | Owning context |
|---|---|---|
| HMP-01 | Which information classes/domains require local durability, remote durable flush or remote replay before a governed WRITE can report the applicable completion guarantee? | IU/Mneme–Mnemosyne contract + NFR |
| HMP-02 | Exact semantics/names for local-available-after-write, distributed-available-after-write and any related write/update state/Outcome information | Interaction + Calliope models |
| HMP-03 | Recovery point/time objectives and replication/commit performance thresholds by information class, site failure mode and release | NFR/deployment |
| HMP-04 | Which R1 candidate topology and what HA coordinator, quorum/fencing, client routing and backup architecture are selected? | R1 solution/deployment |
| HMP-05 | Which R2 topology is selected; whether PostgreSQL remains outside OpenShift or moves under a qualifying operator; cross-cluster DR ownership? | R2 solution/deployment |
| HMP-06 | How will recovered authoritative currency, IU identity, version, audit/provenance and integrity be verified before reopening service? | Recovery assurance |
| HMP-07 | Which recovery/replication signals are mandatory operational evidence, and which events must become governed AuditEvents/provenance? | Kleio/Dokimasia/OA&M |
| HMP-08 | When may a degraded local-only service continue versus suspend governed writes; who authorises any reduced durability profile? | Continuity/governance |
| HMP-09 | What criteria qualify externally managed providers for particular IU types/information domains and failure classes? | Provider integration/deployment |
| HMP-10 | What guarantees can be made about freshness and next-page SearchResult continuity across site failover? | Mneme–Mnemosyne interaction |
| HMP-11 | How do expected site distance, WAN latency/bandwidth, control-plane quorum, physical failure correlation and R1/R2 infrastructure affect the feasible guarantees? | Infrastructure/deployment |
| HMP-12 | Do any accepted NFRs already establish numeric RPO/RTO targets that must be preserved verbatim? | Architecture Continuum reconciliation |

---

## 12. Review decision and source disposition

**Decision:** Pending Solution Architect review of this consolidated DRP.  
**Already accepted in architectural dialogue:** PostgreSQL for Harmonia-managed persistence; externally managed provider implementation as SEP with contractual qualification; persistence tier owns site-level authoritative-data synchronisation; no new Mneme/Mnemosyne peer synchronisation; Mnemosyne's truthful Outcome responsibility; Mneme's distributed cache/IU-instance concurrency responsibilities.  
**Proposed, not yet accepted:** Patroni and associated HA control-plane design; R1-A/B and R2-A/B/C deployments; exact replication modes; numerical NFR profiles; formal supersession of the earlier Availability/Resilience/Disaster-Recovery draft.  
**Not superseded:** `DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md`.  
**Potentially superseded *after explicit reconciliation and approval*:** `DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md`.

---

## Appendix A — Reconciliation against the two earlier DRPs

| Earlier DRP subject | Coverage here or in accepted 1.1.23 | Disposition |
|---|---|---|
| Availability and recovery distinct | §§1, 2, 6; accepted Mnemosyne §§3, 8 | Substantive concern retained |
| Durable recoverability of acknowledged IUs | §§1.5, 3, 6 | Retained; exact failure guarantee open |
| No cache-only authoritative recovery | §§1.1, 1.5 | Retained |
| Native replication, clustering, backup, failover allowed | §§3–5 | Extended with PostgreSQL/FOSS candidates |
| Mneme active-state versus Mnemosyne authoritative persistence | §§1, 8 | Retained |
| Petasos durable work handoff not a substitute for committed IU recovery | Accepted Mnemosyne and other owning architecture | Retained by reference; not duplicated |
| Differential RTO/RPO and recovery verification | §§6, 11 | Open — must be adjudicated |
| Recovery audit versus operational evidence | §§6, 10, 11 | Open — must be adjudicated |
| Shard semantics and persistence-partition separation | §9 / original sharding draft | Distinct DRP retained |
| Semantically safe sharding; sharding governance and rebalancing | Original sharding draft | **Not covered, not accepted** |

## Appendix B — Technical references (informative, checked 13 November 2026)

1. PostgreSQL documentation, replication configuration and synchronous standbys: <https://www.postgresql.org/docs/current/runtime-config-replication.html>.
2. PostgreSQL documentation, WAL and `synchronous_commit`: <https://www.postgresql.org/docs/current/runtime-config-wal.html>.
3. PostgreSQL documentation, log-shipping standby, hot standby, synchronous replication and promotion: <https://www.postgresql.org/docs/current/warm-standby.html>.
4. PostgreSQL documentation, continuous archiving, base backup and PITR: <https://www.postgresql.org/docs/current/continuous-archiving.html>.
5. Patroni project and MIT licence: <https://github.com/patroni/patroni> and <https://github.com/patroni/patroni/blob/master/LICENSE>.
6. Patroni replication and failover modes: <https://patroni.readthedocs.io/en/latest/replication_modes.html>.
7. Patroni dynamic configuration: <https://patroni.readthedocs.io/en/latest/dynamic_configuration.html>.
8. CloudNativePG project and Apache-2.0 licence: <https://github.com/cloudnative-pg/cloudnative-pg>.
9. CloudNativePG distributed topology / cross-cluster limitations: <https://cloudnative-pg.io/docs/current/architecture/>.

*External product documentation substantiates implementation feasibility, not acceptance of new Harmonia architectural decisions. Confirm versions, licence/deployment compatibility and operational support at release engineering time.*
