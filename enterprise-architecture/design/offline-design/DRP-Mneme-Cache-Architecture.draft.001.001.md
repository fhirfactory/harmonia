<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — Mneme Cache Architecture

**DRP identity:** `DRP-Mneme-Cache-Architecture-001`  
**Document:** `DRP-Mneme-Cache-Architecture.draft.001.md`  
**Architectural increment:** Harmonia 1.1.24  
**Document standing:** Consolidated design proposal recording Solution Architect decisions reached in the Mneme–Mnemosyne Interaction Review II; **the DRP as a whole awaits review and governed incorporation**  
**Prepared for:** Solution Architect architectural review  
**Prepared by:** ChatGPT, architectural reasoning peer  
**Date:** 13 November 2026  
**Repository / working branch:** `fhirfactory/harmonia` / `Mneme-and-Mnemosyne-Build`  
**Architecture authority:** `enterprise-architecture/continuum/`

## 0. Standing, authority and boundaries

This DRP defines the **two-cache architecture within Mneme**. It consolidates the expressly agreed distinction between lightweight reference-based IU management and locally materialised IU processing, including the agreed Infinispan deployment direction. It is **not** a redesign of the Information Unit, the Mneme–Mnemosyne governed interaction, Ergon, Ponos, DigitalTwin, Dokimasia, Themis, Petasos or the persistence tier.

The authoritative architecture remains governed Markdown in `enterprise-architecture/continuum/`. Existing accepted IU and component semantics take precedence over historical drafts. Statements identified below as **agreed in review** record the Solution Architect's design decisions in the present discussion; they do not claim that this new DRP has already been incorporated into a published Continuum release. Technical configuration details explicitly left for verification are not silently promoted into accepted guarantees.

For this DRP, **Information Unit (IU)** means Harmonia's established governed IU, regardless of whether its Content is currently represented by reference or materialised by value. A cache entry, Java object, working manifestation, Active Generation or processing copy is not automatically a new governed IU or a new authoritative content version.

## 1. Feature realisation

### 1.1 Feature and capability context

This proposal provides an architectural realisation for:

- Governed Information Unit accessibility and active working-state management, including `DataManagement.InformationUnits.DataAccessibility` in the earlier design catalogue.
- Distributed active information availability and reconstructability, corresponding to `DataManagement.InformationUnits.DataSteadfastness.Availability` and `.Recovery` in the earlier DRPs.
- Mneme-governed contextualisation supporting common Ergon execution, with Ponos, DigitalTwin/Behaviour and Dokimasia/Dokimasia Ergon as consumers.

The precise current CT1→CT2→CT3→Feature identifiers and mapping in the 1.1.24 Continuum are **to be reconciled against the governed capability catalogue**; this DRP does not invent replacement Feature identities or assume older names remain unchanged.

### 1.2 Associated architectural requirements

The accepted operational and design requirements relevant here are:

1. Harmonia targets **active/active/… operation across N+1 geographical sites, N > 0**, with acceptable, bounded performance degradation on the loss of a site, subject to the governed NFRs.
2. The durable acceptance contract for `GovernedWriter → Outcome.Success` means **authoritative persistence across multiple physical sites**; cache population or cache replication cannot fulfil that contract on its own.
3. Received information is normally durably written **once on ingress**; its references then flow through message-bus and workflow scaffolding. Subsequent processing dereferences as required rather than re-persisting the same incoming information at each handoff.
4. The ordinary working representation of an IU is comparatively small where its payload can remain a reference to persisted content. Processing may require a larger resolved representation.
5. The input supplied to concurrently executing Ergons through `.input()` is **immutable**. Independent executions may materialise independent copies without needing shared mutable input instances.
6. Loss of a cache, execution JVM or site must not itself destroy acknowledged authoritative information or accepted governed work. Recovery uses the already-established Ergon lifecycle, checkpointing and context-loading responsibilities.
7. Information access, authorisation, concurrency, operational versions, generations, provenance and audit remain governed by their existing owning components and contracts.

### 1.3 Motivation

The architectural problem is to support frequent, geographically distributed coordination **without requiring a large, resolved working-content cache at every site or every execution JVM**. The design seeks lower inter-site traffic, memory pressure and cost while retaining governed active-IU access and recoverable execution. Existing IU primacy, pass-by-reference, persistence authority, resilience and shared execution-framework principles remain controlling.

## 2. Proposal

Mneme provides **two distinct logical cache capabilities**, realised using two different Infinispan access and deployment models:

1. **`Mneme-Standard-Cache`** — the lightweight, **reference-based** IU representation and distributed Mneme operational/coordination state, implemented through a **multi-site Infinispan Server** cache accessible via **Hot Rod clients** where applicable throughout Harmonia. The selected direction is **synchronous distribution**, subject to the multi-site topology and concurrency verification in §9.
2. **`Mneme-Workflow-Cache`** — the **resolved, by-value** IU representation used during execution, implemented as an **embedded Infinispan LOCAL cache** in each relevant execution JVM. It is not synchronised or replicated with Workflow-Cache instances in other JVMs or sites.

These are **two cache capabilities, not two types of IU**. Their architectural difference is representation, residency, purpose and lifecycle; neither changes authoritative IU identity.

## 3. Design context and terminology

### 3.1 Normal information flow

```text
Receive information
       |
       v
Mneme → GovernedWriter → Mnemosyne → PersistenceService
                                  |
                         Multi-site durable IU
       |
       v
Message bus / workflow / Pragma: carry IU references
       |
       v
Ergon context loading when processing actually needs content
       |
       v
Resolve IU Content (except content that must remain referenced)
       |
       v
Local Mneme-Workflow-Cache → Ergon / Behaviour / Dokimasia Ergon
```

This is a **conceptual flow**, not a prescribed call sequence or new API. The existing processing, transport, identity and lifecycle controls continue to apply.

### 3.2 IU representation

- **Reference-based representation:** An IU whose Content/payload is, where possible, a reference to its durably persisted information; hybrid/direct values remain permitted by the established IU framework. The IU's identity, Metadata, InformationCoordinates and governance semantics remain intact.
- **Resolved representation:** The **same IU**, with relevant referenced Content resolved/materialised during context loading for execution. Very large binary/media content remains referenced as required by existing information/media boundaries.
- **Resolution is not transformation:** Replacing a reference with the referenced content in an active manifestation does **not by itself** create another IU, a new authoritative content version or a new independent information identity. A genuine content transformation that produces a new governed representation remains subject to the accepted IU transformation rules.
- **Operational Version versus Active Generation:** Mneme's operational Version concerns coherence/concurrency; an Active Generation identifies an active cache manifestation. Several manifestations may represent the same IU operational Version. Neither cache configuration authorises new version or identity semantics.

### 3.3 Cache residency versus information authority

The caches are **non-authoritative active/working facilities**. Mnemosyne and its applicable PersistenceService establish authoritative persistence. Local cache loss is not information loss; but neither a cache hit nor a synchronously acknowledged cache update is, by itself, proof of `GovernedWriter` success.

## 4. Architectural definition

### 4.1 `Mneme-Standard-Cache` — distributed, reference-based

**Purpose.** Maintain lightweight IU manifestations and Mneme-managed operational information needed for common distributed access, identity/version awareness and state coordination across active sites. It is not a bulk resolved-content repository.

**Agreed architecture:**

- A **multi-site, synchronously distributed Infinispan Server cache** is the selected technology/deployment direction.
- Appropriate Mneme access components across Harmonia have **Hot Rod clients**; use of a client does not grant other components permission to bypass the Mneme governed façade or Themis security obligations.
- Information stored by default is a **reference-based IU representation**, rather than the large dereferenced processing representation.
- Cache distribution/owner placement shall support the N+1 availability target as qualified by the existing Requirements. The number of owners, topology-awareness strategy, network topology, partition response and exact failure guarantees remain to be technically validated.
- The Standard Cache is logically available to appropriately authorised consumers through Mneme across the logical distributed-cache domain; a consumer does not require an individual permanent IU-to-site affinity.
- Cache access/replication **does not itself linearise** every concurrent state-machine transition; Mneme retains its established IU-instance concurrency and lifecycle authority.
- The Standard Cache is bounded and governed; it is not required to hold every IU ever ingested or every large resolved payload.

**Important distinction.** “Synchronous distribution” does **not** mean “a complete copy on every node”. Infinispan distributed mode uses configured owners for each entry. Whether selected cache data must be present at every geographical site, and how site placement is enforced, must be established against the NFRs rather than inferred from the word *synchronous*.

### 4.2 `Mneme-Workflow-Cache` — embedded, local, resolved

**Purpose.** Retain the materialised, potentially large IU working information required by a particular execution JVM without paying the cost of routine inter-site duplication.

**Agreed architecture:**

- Implemented as an **embedded Infinispan LOCAL cache** in the JVM hosting the execution capability.
- Accessed locally by the common Ergon context-loading and processing facilities; no independent Infinispan Server or Hot Rod round trip is needed for this local cache.
- No Workflow-Cache inter-JVM replication, global ownership, distributed key affinity, cross-site L1 or near-cache coherence is required.
- Entries represent IUs resolved in the course of governed context loading. They are **working manifestations of the same governed IUs** referenced through the Standard Cache and authoritative storage.
- Entries are transient and bounded. Their retention, reuse, invalidation and eviction must not breach Ergon context validity, IU security or Mneme's operational version/generation semantics.
- A Workflow-Cache is **local to its execution JVM**, not a single physical cache shared by all executors at a site. Multiple JVMs at one site can each have a distinct Workflow-Cache instance.
- Concurrent processors can legitimately resolve the same **immutable `.input()`** into different local Workflow-Caches. Their outputs and the processing outcome remain governed by the existing Pragma/Praxis/Ergon orchestration, not by synchronising those cache copies.

**No on-demand cache replication.** A request at another JVM does not pull a resolved entry into that JVM by using Infinispan's cluster ownership or L1 mechanism. It can, when legitimately required, **resolve the underlying governed information independently** through existing Mneme/Mnemosyne access. This avoids introducing a misleading “local cache with distributed L1” hybrid.

### 4.3 Agreed execution deployments

At every site hosting the corresponding execution capability, provide a Workflow-Cache **inside the execution JVM**:

| Execution JVM / component | Embedded cache | Work supported |
|---|---|---|
| **Ponos-JVM** | `Mneme-Workflow-Cache` (LOCAL) | General Ergon execution and context |
| **DigitalTwin-JVM** | `Mneme-Workflow-Cache` (LOCAL) | DigitalTwin execution of specialised **Behaviour** Ergon |
| **Dokimasia-JVM** | `Mneme-Workflow-Cache` (LOCAL) | Assurance execution of **Dokimasia Ergon**, including specialised AI Assurance Ergon where selected |

Ponos, DigitalTwin and Dokimasia are the established execution components. **Behaviour** and **Dokimasia Ergon** are Ergon specialisations; they do **not** require new, independent context-loading or cache-recovery frameworks.

The cache capability is common, though per-JVM resource configuration may vary by workload. The architectural statement does not prescribe one JVM or one executor per site, container image layout, WildFly domain configuration or a mandatory library-versus-WildFly-subsystem integration technique.

### 4.4 Conceptual deployment view

```text
                         Mneme-Standard-Cache
              distributed Infinispan Server infrastructure
                    synchronous distributed state
                                  |
                   Hot Rod clients wherever needed
                                  |
              +-------------------+-------------------+
              |                   |                   |
            Site A              Site B              Site C     ... N+1
              |                   |                   |
      +----------------+  +----------------+  +----------------+
      | Ponos JVM      |  | Ponos JVM      |  | Ponos JVM      |
      | Workflow LOCAL |  | Workflow LOCAL |  | Workflow LOCAL |
      +----------------+  +----------------+  +----------------+
      | DigitalTwin JVM|  | DigitalTwin JVM|  | DigitalTwin JVM|
      | Workflow LOCAL |  | Workflow LOCAL |  | Workflow LOCAL |
      +----------------+  +----------------+  +----------------+
      | Dokimasia JVM  |  | Dokimasia JVM  |  | Dokimasia JVM  |
      | Workflow LOCAL |  | Workflow LOCAL |  | Workflow LOCAL |
      +----------------+  +----------------+  +----------------+
              |                   |                   |
          Governed IU acquisition and durable processing/recovery
                                  |
                        Mneme → Mnemosyne
                                  |
                    Multi-site PersistenceService
```

The diagram illustrates **co-location and logical access**, not an assertion that Standard-Cache server owners or persistence primaries exist at every displayed JVM. Site count, exact topology and container allocation are deployment matters under the established NFRs.

### 4.5 Common Ergon context-loading, checkpoint and recovery contract

- The **common Ergon framework** owns context loading/unloading, checkpoints and execution recovery. Its specialisations reuse those facilities.
- The Workflow-Cache is a **transient processing aid**, not a checkpoint repository, durable message queue or substitute for authoritative IU storage.
- On failure of a JVM, cache instance or site, the lost Workflow-Cache contents need not be recovered as cache entries. The surviving/restarted Ergon framework uses its governed checkpoint, work-transition and context-loading facilities to **reconstruct what processing needs** from authoritative information and durable work state.
- Ergon recovery respects the existing rule: **do not resume inside a failed Ergon**; recover/retry from the appropriate existing execution boundary.
- Cache eviction, reloading or re-execution must not make previously acknowledged durable information or accepted work disappear, nor manufacture successful persistence or processing Outcomes.
- Any changes made during execution are handled through existing context-save, Mneme governed updates and persistence contracts; immutability of `.input()` is not a claim that every output or working value is immutable.

**Allocation is deliberate:** Infinispan handles local caching; Mneme governs IUs and their active state; Ergon handles processing-context and checkpoint recovery; Mnemosyne/PersistenceService handle authoritative state. **No new Workflow-Cache recovery subsystem is introduced.**

### 4.6 Interaction and security boundaries

- The logical `Mneme ↔ Mnemosyne` boundary retains `GovernedReader`, `GovernedWriter`, `GovernedSearch`, factual Outcomes and applicable transaction-identity propagation.
- `GovernedWriter → Success` requires **confirmed multi-site durable persistence**. Distributed reference-cache propagation is a separate activity and not evidence of this durability.
- Work/message envelopes ordinarily pass **references** to ingressed IUs; they do not require repeated content writes or cross-site distribution of resolved IU copies.
- Context loading and ad hoc information acquisition remain governed, including Themis authorisation, disclosure and relevant audit/provenance obligations. Embedding a cache inside an execution JVM does not bypass governance.
- Operational logs, AuditEvents and Provenance may traverse Mneme-related logical interfaces where their contract requires, but are **not ordinarily to be populated into Mneme's active Standard/Workflow caches as generic streamed workload**. Their proper owning services and governed retention/export contracts remain unchanged.
- No cache representation shall reveal information outside the requester's authorised context; co-residency in an execution JVM does not expand transaction authority.

### 4.7 Behaviour under normal operation and failures

| Situation | Standard Cache | Workflow Cache | Governed responsibility |
|---|---|---|---|
| Normal ingress | Maintain/reference lightweight IU and active management information as needed | Not populated merely because ingress occurred | GovernedWriter establishes authoritative durability |
| Reference routed through workflow | Lightweight IU/ref available under Mneme | No automatic large materialisation | Petasos/Pragma/Praxis carry governed references |
| Ergon context load | Resolve IU version/visibility as required | Materialise and retain resolved working IU locally | Mneme governed context access; Ergon framework coordinates load |
| Two Ergons consume same immutable input | Common IU identity/reference remains available | Two independent resolved manifestations are permitted | Existing Pragma processing and outcome semantics |
| Local Workflow-Cache eviction | No special effect on authoritative data | Reload when context legitimately needs it | Ergon context validity and Mneme access |
| Execution JVM failure | Unaffected unless shared client/service also fails | Local entries lost | Existing checkpoint/work recovery and fresh context loading |
| Complete site loss | Surviving distribution subject to verified topology/failure policy | Local entries at failed site lost; surviving sites reconstruct on demand | N+1 continuity, Ergon recovery, Mnemosyne authoritative access |
| Stale/unavailable authoritative data on recovery | Reference state is not proof of latest readable version | Must not silently treat an outdated reconstruction as current | Mneme–Mnemosyne retrieval/Outcome and version-currency contract |

This table states **responsibility and intended behaviour**, not a promise that any unverified Infinispan topology or PersistenceService failover already implements it.

## 5. Explicitly not prescribed

This DRP does **not** require:

- A full replicated cache on every site, or distribution of every resolved IU between sites.
- An IU-to-node affinity registry, `KeyAffinityService`, IU key redesign, per-IU routing service or custom cross-site Workflow-Cache protocol.
- Shared mutable copies of immutable `.input()` across Ergons or automatic merger of their in-memory representations.
- A new persistence model, cache write-behind acceptance mechanism, checkpoint service, Ergon recovery subsystem, or replacement for governed services.
- Universal caching of log streams, AuditEvents, Provenance records, Search Result Sets or every persisted IU.
- A prescribed Infinispan owner count, cluster-versus-cross-site backup topology, node-count per site, Hot Rod near-cache policy, cache size/TTL, eviction algorithm, marshalling format, JVM/container layout or WildFly integration technique.
- New Java API signatures, checkpoint data models, state-machine synchronisation algorithms, distributed locks or alternative Workflow-Cache storage products.

The selected **Infinispan Server/Hot Rod and embedded LOCAL** division is an explicit architectural design choice of this DRP, while details not agreed above remain subject to conformance evidence and deployment design.

## 6. Evidence and rationale

### 6.1 Architectural sources

- `DRP-Information-Unit-Framework.Final.001.md` — IU identity, ManagementContext, operational Version, Active Generation, Content/reference semantics, and distinction between IU working manifestations and independent versions.
- `DRP-Mnemosyne-Authoritative-Persistence-Service-Model.Final.001.md` — authoritative persistence boundary.
- `DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md` — reconstructability, safe failure/uncertainty, Ergon restart boundary and authoritative state versus active cache.
- `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md` — governed context establishment/use/return and specialisation-independent Ergon interaction.
- `DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md` and the 1.1.24 interaction-focused working revision — logical governed reader/writer/search interaction; latter's location must be verified before incorporation.
- Governed Continuum ApplicationComponent records for `Ponos`, `DigitalTwin`, `Behaviour`, `Dokimasia` and `Dokimasia Ergon` — specialised execution semantics and common Ergon facilities.
- Solution Architect's 13 November 2026 review decisions — the two named cache roles, Standard/Workflow distribution, Hot Rod and embedded locality, immutable input, and assignment of recovery to common Ergon.

### 6.2 Infinispan capability evidence (technology, not project authority)

Checked against the official Infinispan stable documentation:

- Cache configuration: https://infinispan.org/docs/stable/titles/configuring/configuring.html
- Embedded caches and WildFly integration: https://infinispan.org/docs/stable/titles/embedding/embedding.html
- Hot Rod clients: https://infinispan.org/docs/stable/titles/hotrod_java/hotrod_java.html
- Server/topology: https://infinispan.org/docs/stable/titles/server/server.html
- Cross-site replication: https://infinispan.org/docs/stable/titles/xsite/xsite.html

Relevant observed technology capabilities and limitations:

- An embedded **LOCAL** cache has no cluster replication and can be instantiated within a Java/WildFly application JVM; an Infinispan Server exposes remote cache access through Hot Rod.
- A Hot Rod **near cache** is a separate client-side optimisation populated by reads and invalidated by events; it is **not** the embedded Workflow-Cache selected here.
- A synchronously **distributed** cache normally stores an entry on the configured owner nodes, not universally on every cluster member.
- Infinispan synchronous distribution does **not automatically provide linearizable reads or universal state-machine serialisation**.
- Cross-site replication between independently active clusters has its own concurrency, failure and latency caveats; choosing a topology requires explicit verification.

## 7. Relationship to current architecture

| Existing architectural subject | Treatment by this DRP |
|---|---|
| Information Unit Framework | **Preserved.** Reference and resolved forms are active manifestations of an IU, not newly invented IU kinds. |
| Mneme distributed active-IU management | **Refined.** Split cache responsibilities by representation and residency. |
| Mneme–Mnemosyne Interaction | **Preserved.** Use governed authoritative access; do not embed this cache design into the interaction contract. |
| Mnemosyne/PersistenceService | **Unchanged.** Authoritative persistence/replication remains its responsibility. |
| Ergon-to-Mneme Pragmaton interaction | **Clarified.** Context loading materialises resolved IUs in the execution JVM's local cache. |
| Mneme Pragmaton availability/recovery | **Clarified.** Workflow-Cache loss is handled through existing execution recovery and context rebuild, not cache-state replication. |
| Ponos / DigitalTwin / Dokimasia | **Clarified deployment.** Each applicable execution JVM hosts a common embedded Workflow-Cache facility. |
| Petasos, Artemis, Pragma, Praxis | **Unchanged.** References and governed work envelopes flow through existing scaffolding. |
| Themis, Kleio, Dokimasia evidence/assurance, logging and provenance | **Unchanged.** Existing authority and evidence responsibilities remain. |

## 8. Conformance expectations

A conformant solution shall demonstrate that:

1. Standard Cache is distributed, reference-oriented and uses the selected remote Hot Rod access model, with no default replication of bulk resolved workflow payloads.
2. Workflow Cache is embedded/LOCAL within each applicable Ponos, DigitalTwin and Dokimasia execution JVM; it does not depend on distributed Workflow-Cache entry ownership.
3. IU identity, authorised disclosure, operational version and Active Generation remain correct across reference and resolved manifestations.
4. Concurrent Ergons can independently use the same immutable input without unsafe shared mutable copies or incorrect processing-outcome coupling.
5. Cache loss, eviction, JVM failure and site loss can trigger appropriate existing Ergon recovery/context loading without losing acknowledged work or treating stale information as authoritative.
6. Multi-site `GovernedWriter` success is established by the persistence contract, not by cached copies.
7. The actual Infinispan Standard-Cache topology and failure behaviour satisfy the governed site-loss, availability and performance constraints; this cannot be inferred from choosing `DIST_SYNC` alone.
8. The embedded cache arrangement is supported and operationally manageable in the selected WildFly/container configuration, including lifecycle and memory isolation as required.

No numeric cache capacity, latency, RTO/RPO or coverage threshold is invented here. Apply the already-governed targets and demonstrate them through the accepted solution validation process.

## 9. Open questions / qualifications — **not** accepted as additional decisions

The following require technical validation or reconciliation; they do not invalidate the agreed two-cache split.

**MC-01 — Standard Cache multi-site implementation and consistency.** Determine whether a single distributed cluster spanning sites or multiple site-local clusters with cross-site capability can meet **both** synchronous reference-state distribution **and** active/active concurrency/partition behaviour. Infinispan's cross-site synchronous replication is **not automatically safe for concurrent writes to the same keys in independent active/active clusters**. Specify only the necessary product topology once the behavioural evidence is available. Do not claim native Infinispan linearizability.

**MC-02 — Standard Cache recovery and state-machine guarantees.** Establish owner-count/site-placement and majority/partition/failover behaviour that allows continued governed work on losing any one site. Identify any Mneme-level concurrency obligations not guaranteed by cache operations themselves. Avoid inventing another distributed coordination mechanism unless required by evidence.

**MC-03 — Execution-local cache resource policy.** Determine per-JVM cache memory bounds, safe eviction timing, security isolation and lifetime relative to Ergon context load/unload/checkpoint. Reuse existing Ergon rules; do not introduce new context-lifecycle semantics through an eviction setting.

**MC-04 — Version freshness on reacquisition.** A cross-site durable write does not necessarily imply immediate *read visibility* on every replica. When a lost Workflow-Cache entry is reacquired, latest-version/read-after-write expectations, wait/fail/indeterminate outcomes and version-specific retrieval must be resolved within the **Mneme–Mnemosyne interaction** and PersistenceService guarantees, rather than implemented as a cache-specific affinity mechanism.

**MC-05 — N+1 site loss versus required multi-site write durability.** If the deployment starts with **only two physical sites**, losing one leaves no second site on which to establish *new* multi-site durable writes. The accepted `GovernedWriter → Success` contract therefore cannot be satisfied for new writes in that condition without an additional eligible durable site or an explicitly authorised change to service behaviour/contract. This is an **adjacent NFR/persistence availability reconciliation**, not authority to weaken `Success` or silently claim N+1 write continuity. Three or more sites may alleviate this particular constraint but do not, by themselves, prove quorum or persistence failover correctness.

**MC-06 — Feature mapping and as-built integration.** Reconcile the current 1.1.24 capability/Feature assignments and determine the bounded implementation allocation: standard remote clients, embedded LOCAL cache library/subsystem ownership, operational observation and deployment configuration. The architectural cache responsibilities above remain controlling.

## 10. Continuum update — connected architectural delta for subsequent authorised incorporation

The items below identify the **architectural meaning** to carry forward, not a Codex task or authorisation to execute.

### 10.1 Motivation

Relate the two-cache separation to current N+1 site availability, governed work durability, active data accessibility, recoverability, information integrity/security and efficiency/performance Requirements. Reconcile MC-05 in its owning availability/persistence architecture rather than silently relaxing an accepted guarantee.

### 10.2 Strategy and capabilities

Reuse and refine existing Information Unit accessibility/steadfastness, context management and execution-support Features. Resolve current Feature identities against the accepted 1.1.24 hierarchy; do not invent duplicate capabilities.

### 10.3 Business information architecture

**No new Business IU type** is required for reference versus resolved cache manifestations. The two-cache distinction is application/technology allocation, not a Business information distinction.

### 10.4 Business behaviour

No new Business Function, Service or Process merely to represent cache replication or embedded JVM residency.

### 10.5 Application information architecture

Preserve the existing `InformationUnit`, `InformationRepresentation`, Content/Metadata, `ManagementContext`, operational Version and Active Generation semantics. Expose only such cache residency/context status as has an actual shared architectural contract; do not introduce new IU fields by default.

### 10.6 Application functions, services, processes and interactions

Refine Mneme active information management and governed contextualisation with **Standard-Cache** and **Workflow-Cache** responsibilities; relate local materialisation to existing Ergon context load/unload, checkpoint and recovery. Preserve Mneme–Mnemosyne and Ergon–Mneme interaction boundaries rather than replicating their internals.

### 10.7 Application and technology structure

Model two logical Mneme cache capabilities and their **distinct deployment/access characteristics**, with Standard Cache served remotely and Workflow Cache embedded in the Ponos, DigitalTwin and Dokimasia execution JVMs. Reuse existing Application Components/Technology constructs where appropriate; distinguish logical components from multiple runtime instances.

### 10.8 Cross-layer and cross-feature relationships

Relate the cache responsibilities to the governing Requirements, Features and the existing Mneme, Mnemosyne, Ergon, Ponos, DigitalTwin, Behaviour, Dokimasia, Themis and Petasos architecture with semantically correct ArchiMate relationships. Preserve original identities and avoid representing logical access as false structural containment or authoritative custody.

### 10.9 Explicitly unaffected architecture

Authoritative persistence internals, IU identity, Themis policy, audit/provenance ownership, generic task orchestration, underlying execution recovery machinery and the detailed Mneme–Mnemosyne protocol remain outside the substantive change proposed here.

## 11. Review disposition

| Subject | Standing |
|---|---|
| Two logical Mneme cache concepts | **Agreed by Solution Architect in discussion; captured here** |
| `Mneme-Standard-Cache`: lightweight reference IUs, multi-site synchronous distribution, remote Hot Rod access | **Agreed design direction; concrete multi-site topology/guarantees qualified by MC-01/02** |
| `Mneme-Workflow-Cache`: local, embedded, resolved IUs within each applicable execution JVM | **Agreed by Solution Architect** |
| Ponos, DigitalTwin and Dokimasia JVM deployment | **Agreed by Solution Architect** |
| Common Ergon checkpoint/recovery; no separate Workflow-Cache recovery subsystem | **Agreed by Solution Architect** |
| Authoritative multi-site `GovernedWriter.Success` | **Previously accepted interaction decision; preserved** |
| Entire DRP and Continuum incorporation | **Pending review / separate governed authorisation** |

**Decision authority:** Solution Architect.  
**Decision on complete document:** Pending.  
**Repository action:** None authorised by creation of this DRP.

## 12. Post-decision actions

After review and explicit authorisation, the accepted architectural delta may be incorporated into the Continuum using existing governed ADD practice. Any required cross-references or targeted refinements to the IU, Mneme recovery, Ergon–Mneme and Mneme–Mnemosyne DRPs should preserve those documents' established scope and decisions. This section is not an agent task or approval to commit/push.

## Appendix A — Design provenance

**Repository material inspected (branch `Mneme-and-Mnemosyne-Build`):** `DRP-TEMPLATE.md`; Information Unit Framework Final.001; Mneme Pragmaton Availability/Resilience/Recovery draft.002; Ergon-to-Mneme Pragmaton draft.002; Mneme-to-Mnemosyne Interaction draft.002; Mneme Pragma Model draft.002; and the governed `Ponos`, `DigitalTwin`, `Behaviour`, `Dokimasia`, `Dokimasia Ergon` component definitions.

**Current-session adjudications:** N+1 active/active deployment goal; durable write success; ingress persist-once/reference-carrying workflow; resolved IU materialisation; immutable `.input()` across concurrent Ergons; separate Standard/Workflow caches; remote Hot Rod Standard Cache and embedded LOCAL Workflow Cache; and common Ergon checkpoint/context recovery.

**Qualification:** The 1.1.24 interaction-focused working revision was described in the session handoff but has not been treated here as an independently fetched, governed repository artefact. The named candidate technology configurations are supported by official Infinispan documentation, but deployment conformance remains to be demonstrated.
