# Harmonia — Design Review Proposal (DRP)

**DRP:** DRP-Mnemosyne-Authoritative-Persistence-Service-Model-001  
**Title:** Mnemosyne Authoritative Persistence Service Model — Concept and Governed Persistence  
**Status:** Consolidated adjudicated review candidate — **MP-01–MP-08 accepted in architectural dialogue; MP-08 incorporated following review of the preceding consolidated text**  
**Baseline:** `enterprise-architecture/design/offline-design/DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md`, 24 October 2026  
**Working branch:** `Mneme-and-Mnemosyne-Build`  
**Prepared for:** Solution Architect / Architecture Review  
**Prepared by:** ChatGPT, architecture peer  
**Date of consolidation:** 11 November 2026

> **Review standing.** This is the complete consolidated text for review. It preserves the baseline proposal, Feature/Requirement rationale, original questions and Continuum incorporation intent; incorporates **MP-01–MP-08 accepted Solution Architect decisions**; and explicitly separates agreed architecture from residual contract and canonical-reconciliation details. It is not an instruction to implement, publish, allocate new canonical identities or change the repository.

## 0. Authority, source succession and scope

### 0.1 Source and precedence

The **primary baseline** is `DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md`. Related information is taken from:

- `DRP-Information-Unit-Framework.Final.001.md`, accepted by the Solution Architect and incorporated as the 1.1.22 Information Unit change set (the complete repository state and any accepted-release status must be independently checked at incorporation time);
- `DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md`;
- `DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md`;
- the Topic/InformationCoordinates design package and accepted IU-01–IU-13 adjudications;
- relevant accepted Architecture Continuum axioms, particularly AX-05, and established Themis, Kleio, Dokimasia, information governance, resilience and release boundaries.

The accepted IU design governs IU semantics. Adjacent **drafts** are corroborating design evidence, not accepted authority over this DRP; their open points remain open. Historical names and Legacy RAD evidence are not automatically current canonical identities. Repository integration must reconcile actual native elements, standing, and capability/requirement mappings against the then-current Continuum. The previous 1.1.22 incorporation report identified a strictly failing Archi Exchange transport with preserved semantic graph but qualified presentation/state changes; that is architecture-governance context, **not** a new Mnemosyne design requirement or a claim that a new round trip has passed.

### 0.2 Architectural purpose

Define what must remain true when Mnemosyne provides **authoritative durable Information Unit persistence and acquisition**, while preserving freedom to implement a range of persistence engines, gateways and provider-specific capabilities. The architecturally important units are responsibility, governed meaning, capability, authority and interaction boundary—not a mandated storage technology, Java type, database schema or transport.

### 0.3 Decision standing

The foundational IU and cross-component decisions are accepted input. **MP-01–MP-08 have been adjudicated and accepted in the architecture dialogue.** GPF's five-tier placement, provider-asymmetric capabilities and the generic Calliope-owned Outcome semantics are now settled. Exact reason-enumeration members, provider-specific execution mechanics and native Continuum identity mappings remain matters for their owning contracts or later governed reconciliation, not open conceptual MP decisions.

---

## 1. Feature realisation and architectural justification

### 1.1 Historical Feature proposals and current mapping obligation

The baseline identified these **candidate** paths:

- `DataManagement.InformationUnits.DataSteadfastness.Recovery` — durable recoverability;
- `DataManagement.InformationUnits.DataAccessibility` — authoritative governed acquisition;
- `DataManagement.InformationUnits.DataThroughput` — persistence performance within distributed Harmonia;
- a proposed **Authoritative Persistence** Feature beneath `DataManagement.InformationUnits`, if not already present.

Those paths are **historical source labels**, not permission to create or reintroduce an obsolete capability hierarchy. Harmonia's accepted 1.1.20+ architecture expresses capabilities and Features within its current CT1 → CT2 → CT3 → Feature hierarchy. The Information Unit incorporation already established the appropriate **semantic-reuse** approach to historical Feature and Requirement mappings. Mnemosyne's contribution is to be mapped to existing authoritative persistence, durable acceptance, recoverability, accessibility, throughput, governed information and service-capability Features as applicable, potentially across more than one hierarchy location. No artificial singular Feature owner is assigned merely to fit the draft.

The **candidate Authoritative Persistence Feature** is retained as a recorded proposal, but it is not automatically created: its current accepted equivalent, coverage and actual native relationships require Continuum reconciliation. Persistence is an enabling capability; this proposal does not itself establish a new Business information entity.

### 1.2 Required characteristics and obligations

The baseline's architectural requirements are retained:

1. **Authoritative durable acceptance** is distinguishable from active-cache activity, generic storage acknowledgements, transport receipt and provider-native acknowledgements.
2. The Information Unit's **governed identity and governed state/version** are not displaced by provider-native IDs, revisions, transaction IDs or timestamps.
3. Applicable **information governance, security, provenance, audit, retention and preservation** obligations survive the persistence boundary.
4. **Persistence responsibility is not semantic ownership** of the underlying clinical, operational or business information.
5. Multiple **PersistenceServices** and persistence technologies may implement the architectural obligation without redefining it.
6. **Uncertain outcomes remain uncertain** until resolved by competent evidence; they are not silently converted into success, failure or absence.

These obligations must be grounded in current accepted Motivation elements; this DRP does not assign new Requirement IDs or claim that every wording above already exists as a separately allocated Requirement.

### 1.3 Strategy and Motivation context

AX-05's active/durable distinction is especially material. The architecture also relates to the accepted information-purpose/native-content, component-encapsulation, origin/domain authority, Themis authorisation, Kleio AuditEvent, Dokimasia assurance/provenance, distributed operation, resilience and governed-access axioms. Applicable security, retention and catastrophic-recovery obligations apply according to the owning requirements and contracts. The authoritative persistence model must preserve them without unilaterally redefining their ownership.

---

## 2. Proposal — Mnemosyne's five-tier authoritative persistence architecture

**MP-01 and MP-05 — ACCEPTED.** Mnemosyne is the Information System responsible for authoritative durable persistence and acquisition within its assigned scope. Its architectural decomposition is **five tiers**:

```text
                        Mnemosyne
                            |
            +----------------------------------+
            | 1. Mneme Adapter / Facade        |
            |    GovernedReader/Writer/Search  |
            +----------------------------------+
            | 2. InformationUnit Controller    |
            |    local Themis-enabled PEP/PDP  |
            +----------------------------------+
            | 3. InformationUnit               |
            |    Constructor / Disassembler   |
            +----------------------------------+
            | 4. GovernedPersistenceFramework  |
            |    persistence state/workflow    |
            +----------------------------------+
            | 5. Persistence Adapter           |
            |    native execution/verification |
            +----------------------------------+
                            |
                  Persistence engine(s)
```

The tiers are **architectural divisions of responsibility**, not necessarily five processes, deployable components, network hops, Java modules or compulsory synchronous method calls. **Each of multiple Mnemosyne deployment instances presents these same five responsibility tiers**; distributed capability registration/discovery, balancing and IU-type concurrency are specified in §7A (MP-08).

**1. Mneme Adapter / Façade:** Registers the IU-type capabilities supported by its Mnemosyne instance and participates in the Harmonia-owned `Interaction::Mneme-to-Mnemosyne`, maintaining the GovernedReader, GovernedWriter and GovernedSearch contract boundary. It conveys the requested information-management operation and returns Mnemosyne's persistence-related information and Outcomes without taking over Mneme's consumer-level decisions. Its registration responsibility is distinct from **Mneme's Mnemosyne Adapter (Store)**, which uses TopicMasks for capability discovery and instance selection (§7A).

**2. InformationUnit Controller:** Controls governed IU operations at Mnemosyne's boundary, including the local **execution of Themis-provided/enabled PDP and PEP functions** where applicable. Themis owns and provides policy/authorisation mechanisms; their evaluation and enforcement execute wherever they are needed. This is **not** a centralised PDP execution dependency and does not create a second independent Mnemosyne authorisation framework.

**3. InformationUnit Constructor / Disassembler:** Decomposes IU information for persistence and reconstitutes a conformant governed IU on acquisition. It handles the **IU's governed structure**, not provider-specific SQL/FHIR/protocol handling or consumer-owned clinical/business Content transformation. The IU retains **exactly one governed InformationRepresentation** even when physically decomposed.

**4. GovernedPersistenceFramework (GPF):** An accepted named architectural tier that **controls persistence-service state and workflow**, including delegated-operation coordination and any applicable watchdog, timeout or higher-level failure-processing logic. GPF is **not** the entirety of Mnemosyne's governance/behaviour, nor a mandate for a separate Java framework or deployment.

**5. Persistence Adapter:** Encapsulates the nuances of each selected underlying persistence engine or service. It performs the delegated technology-specific operation and obtains, interprets and, where necessary, supplements engine acknowledgements to establish the **operation's actual known result**. Provider-specific verification belongs here, including any out-of-band activity required as part of that operation's execution sequence.

**PersistenceService [1..*]** remains an architectural capability concept: an actual persistence service is **realised jointly by GPF-controlled state/workflow and Persistence Adapter execution**, and is **not a sixth architectural tier**. Neither tier on its own owns the whole persistence-service operation. Native provider capabilities may be different across instances.

The IU is the governed information Mnemosyne acts upon, not a prescribed physical provider contract. Persistence mappings may be physically different without producing another IU; a *new governed representation of information* remains a separate creator-owned IU under the accepted Information Unit architecture.

---

## 3. Architectural identity and responsibility allocation

### 3.1 Mnemosyne's allocation — MP-02 accepted

The terms **Responsible** and **Partially Responsible** below express actual architectural scope, not implementation exclusivity or an invitation to invent other co-owners. Other components' responsibilities follow existing accepted authority.

| Responsibility | Allocation | Scope and distinction |
|---|---|---|
| **Authoritative Persistence** | **Responsible** | Establish/preserve authoritative durable IU information through the governed persistence boundary. |
| **Authoritative Acquisition** | **Responsible** | Acquire the authoritative information needed for a governed retrieval. |
| **Governed Identity Preservation** | **Responsible** | Preserve IU UUID/Topic and governed identity independently of provider-native keys. |
| **Authoritative Version / State Management** | **Partially Responsible** | Own its persistence-related contribution to authoritative state/version; not all information-state semantics. |
| **Persistence Coordination** | **Responsible** | Coordinate persistence services, GPF workflows and adapter execution. |
| **InformationCoordinates Resolution** | **Partially Responsible** | Resolve/use coordinates as needed for persistence and acquisition; not owner of every coordinate use. |
| **Governance Enforcement** | **Responsible within persistence context** | Enforce applicable obligations when performing persistence operations; does not own all Harmonia governance. |
| **Outcome Determination** | **Responsible within persistence context** | Report what is known about persistence execution; not the consumer's business/workflow disposition. |
| **Persistence Recoverability** | **Partially Responsible** | Ordinary persistence preservation/recovery; **excludes catastrophic-failure recovery**. |

**Authoritative search — MP-02A accepted:** Mnemosyne is responsible for authoritative persistence-search execution/coordination under the established `GovernedSearch` boundary **where applicable persistence capabilities support the request**. Mneme retains the Harmonia-facing search intention, disclosure semantics and governed Search Result Set. Individual adapters may or may not expose native search. Mnemosyne need not implement a universal search engine or fabricate unavailable search support.

### 3.2 Five-tier responsibility allocation — MP-05 accepted

| Tier | Architectural contribution |
|---|---|
| **Mneme Adapter / Façade** | Entry/return contract to Mneme; stable GovernedReader/Writer/Search boundary. |
| **InformationUnit Controller** | IU-specific persistence-context governance and locally executed Themis-enabled PEP/PDP. |
| **IU Constructor / Disassembler** | Governed IU decomposition/reconstruction, integrity of the IU-level representation contract. |
| **GPF** | PersistenceService state/workflow control; delegation; supervision, watchdog/timeouts and applicable higher-level failure processing. |
| **Persistence Adapter** | Native provider mapping, operation execution, engine ACK, supplementary verification, technology-specific correlation and factual delegated-operation Outcome. |

GPF and Persistence Adapter **jointly fulfil** the persistence-engine interaction capability: GPF controls the state/workflow, the adapter handles native execution and verification. The architecture does not prescribe a rigid call stack for all operations.

### 3.3 Adjacent authority

| Concern | Architectural responsibility |
|---|---|
| Harmonia-facing active governed IU management, client semantics, operational Version and individual Active Generations | **Mneme**, represented in IU ManagementContext |
| Common Harmonia information, Outcome and domain-specific Reason models | **Calliope** — model ownership is the default unless a strong accepted exception exists |
| Themis policy, PDP/PEP provision and enablement | **Themis**; evaluation/enforcement distributed and executed wherever required |
| AuditEvent management | **Kleio** and event-producing components under their contracts |
| Assurance and applicable provenance roles | **Dokimasia**, existing Provenance and Provenance Client Library allocations, subject to canonical mapping |
| Clinical/business interpretation, source authority and governed Content transformation | **Originating/domain capability**, not assigned to Mnemosyne merely by storage |
| Catastrophic-failure recovery | **Outside Mnemosyne's MP-02 allocation**, governed by existing wider recovery architecture |

Themis's distributed execution is a **settled** architectural decision. Do not reinterpret the presence of a PDP in Mnemosyne's Controller as an independent local policy authority or as a requirement to make an external round-trip to a central PDP.

### 3.4 Authoritative persistence-state consistency

**Active/cache success is not authoritative durable success.** Provider receipt, acceptance, adapter execution, engine acknowledgement/verification, durable effect and remote/end-to-end completion may be distinct events and outcomes. The accepted-work durability requirements still apply where governed work is accepted. GPF must preserve the distinctions between stages it controls, while adapter-level execution/verification stays inside the adapter.

Provider-native identifiers, revisions, transaction IDs and timestamps are evidence and may support a mapping, but do not replace IU UUID/Topic, governed Version/Active Generation or Content structure/native versions. Mneme operational Version and per-cache Active Generation remain in `Metadata.ManagementContext`; Content Structure Version in `InformationRepresentation.ContentMetadata`; optional native payload instance version in `Content`. Mnemosyne's persistence-side authoritative state/version responsibilities are **partial** and must not be expanded into all semantic or clinical state management.

### 3.5 First-class governance aspects

Security, audit participation, provenance, retention, integrity, resilience and ordinary persistence recoverability are binding **where applicable to persistence**. They are not required to be embedded into native Content or provider records, nor to constitute new separate subframeworks owned by Mnemosyne. Provider-native mechanisms can fulfil parts of these requirements without assuming responsibility for Harmonia's governing policy or business semantics.

---

## 4. Information Unit contract at the persistence boundary

### 4.1 Canonical IU semantics retained

Mnemosyne must recognise and preserve the accepted IU contract:

```text
InformationUnit
├── Identity (UUID; own Topic name harmonia.mneme.iu.<hex-string-of-compressed-uuid>)
├── Metadata
│   ├── ManagementContext
│   ├── InformationCoordinates
│   ├── SecurityContext
│   └── LifecycleContext
└── InformationRepresentation [exactly 1]
    ├── ContainerMetadata
    ├── ContentMetadata
    └── Content
```

An IU is **one governed atomic identity and one governed InformationRepresentation**; it may be physically disassembled or represented in a provider-native schema only if its required governed state and representation can be reconstructed. No nested independently governed representations, representation registry or implicit transformation synchroniser is introduced by persistence.

The role of `LifecycleContext` is Mneme–Mnemosyne coordination and handshaking. `ManagementContext` remains a Mneme operational workspace and may contain transient references; *not every ManagementContext field must be durably persisted merely because the IU is being persisted*. The persistence contract must distinguish preserved governed attributes from invalidated/transient operational references.

### 4.2 InformationCoordinates for provider resolution

`Metadata.InformationCoordinates` has **four distinct Topic references**:

```text
PersistenceGateway  : TopicID
GatewayEndpoint     : TopicID
InformationLocation : TopicID
InformationOwner    : TopicID
```

They identify governed logical gateway capability, endpoint context, information location and operational owner—not physical URLs, credentials, JDBC properties, or security authority. Mnemosyne interprets/resolves these named references into the applicable PersistenceService, connection context and logical information target. Provider selection and resolution can require communication with remote systems, which may be costly or unavailable; this does not erase the distinction between logical Topic reference and its resolved infrastructure. The exact coordinate population, routing fallback and resolution/cache mechanisms remain to be designed under the applicable interaction/solution contracts.

**InformationCoordinates and SecurityContext are independent**: a location or owner Topic does not by itself authorise an operation.

### 4.3 Native standards and Content-aware/content-neutral participation

A **Content-aware** PersistenceService may expose native indexing, schema-sensitive search, provider-native version checks and content-dependent functions. A **Content-neutral** service may preserve opaque Content or references without interpreting domain structure. Both may contribute to authoritative persistence, subject to the same governed boundary; capability richness is additive rather than universal.

The IU's native Content structure/format and any applicable native instance version remain meaningful. Mnemosyne may perform the necessary provider mapping, assembly and governed reconstruction, but does not thereby take over domain-owned clinical/business transformations or generate another IU each time storage encoding changes.

For large content, existing pass-by-reference/large-binary boundaries apply; this proposal **does not require large binary objects to be stored inside PostgreSQL** or any particular relational engine. PostgreSQL remains a plausible/default persistence technology for relevant domains under existing technology direction, with multiple domain-scoped instances possible, but its use is not elevated here into a new provider contract or an exclusive universal store.

### 4.4 IU relationships and validation

Optional relationships are supplied by the IU creator/authorised updater; Mneme validates applicable **local** Harmonia references and does not assert correctness/existence of **remote** references. Mnemosyne preserves the relevant accepted information and any applicable persistence integrity constraints. It does not infer reciprocal links, construct universal graph maintenance, require cross-provider referential transactions, or turn optional relationships into automatic provenance.

---

## 5. Mnemosyne's two adjacent interaction boundaries — MP-07 accepted

### 5.1 Interaction::Mneme-to-Mnemosyne

The **Harmonia-owned semantic boundary** is preserved through the three existing contracts:

- **GovernedReader:** Authoritative acquisition of governed IU information, distinguishing an established absence from failure or uncertainty where appropriate.
- **GovernedWriter:** CREATE, UPDATE and DELETE authoritative persistence requests and their relevant state/version and Outcome results.
- **GovernedSearch:** Authoritative persistence-search execution; Mneme retains consumer-facing search semantics and Search Result Set management.

The **Mneme Adapter / Façade** (tier 1) is Mnemosyne's participant and its **IU-type capability registrant**; **Mneme's Mnemosyne Adapter (Store)** discovers such registrations by TopicMask and selects an eligible instance (§7A). The architecture remains declaratively grounded in `Interaction → Service/Contract → Information Unit → Topic`, with relevant enforcement and evidence where they apply. Search authorisation and disclosure of individual IU Content remain distinguishable.

**Mnemosyne reports persistence Outcomes; Mneme decides its own consumer-level disposition**, including whether an `Indeterminate` persistence result constitutes a failure for Mneme's operation. The two statuses are not the same fact.

### 5.2 Internal construction and authorisation boundaries

The **InformationUnit Controller** performs Mnemosyne's applicable persistence-context governance and **executes Themis-provided PDP/PEP locally**, with other local enforcement points possible under existing architecture. Policy provision is Themis's responsibility; distributed execution is the accepted design.

The **IU Constructor / Disassembler** maps between the IU's governed structure and the logical pieces required for persistence. It SHALL preserve the one-InformationRepresentation invariant and reconstruct appropriately governed IU identity/Metadata/Content. It is **not** the owner of provider-native encoding/protocol decisions, nor a general Content transformation service.

### 5.3 GPF ↔ Persistence Adapter / PersistenceService

The lower interaction proposed by `DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md` is reconciled with **GPF control plus Persistence Adapter execution**, not a sixth service layer.

- **GPF:** Controls service state/workflow, delegated-operation sequencing, supervision and applicable watchdog, timeout and higher-level failure handling.
- **Persistence Adapter:** Performs delegated CRUD/search or other supported operations in the provider's native form, obtains engine acknowledgements, performs any necessary technology-specific out-of-band verification **within the delegated operation sequence**, and reports the best established factual outcome.
- **PersistenceService:** The capability resulting from that joint contribution, potentially backed by PostgreSQL, native FHIR, a legacy gateway, object storage or another appropriate persistence technology.

**Representation freedom:** An adapter is not required to accept or produce a physical `InformationUnit` object. It must support the governed reconstruction obligation through its own agreed native contribution and evidence. Provider-specific mapping for storage is not creation of a second governed IU.

**Capability asymmetry:** Native search, indexing, bulk operations, transactions, version checks and traversal may be additive. No provider must fabricate a capability solely to match another provider's interface.

### 5.4 Separation of concerns

| Concern | Mneme | Mnemosyne tier(s) / collaborating component |
|---|---|---|
| Consumer-facing read/write/search intent and post-outcome disposition | **Mneme** | Tier 1 reports persistence result under the governed contract |
| Persistence-context access enforcement | Applicable local PEP/PDP | Tier 2 executes Themis-enabled functions |
| IU structure and reconstruction | IU model consumer | Tier 3 establishes conformant assembly/disassembly |
| Persistence workflow, service state, supervision | Receives reported outcome | **Tier 4 GPF** |
| Native CRUD/ACK/verification and technical correlation | Does not own | **Tier 5 Persistence Adapter** |
| Shared IU/Outcome/Reason models | Consumes | **Calliope owns models**; Mnemosyne and others use |
| Persistence-service capability | Requests through governed boundary | **GPF + Persistence Adapter jointly** |

The two neighbouring interaction DRPs remain separate designs requiring their own governed review. MP-07 establishes the reconciliation constraints **without authorising modifications to either draft**.

---

## 6. Outcome model, authority, acknowledgement and uncertainty — MP-04/MP-06 accepted

### 6.1 Common Calliope-owned Outcome model

**Calliope owns Harmonia's models by default**; an exception requires a strong architectural reason accepted by the Solution Architect. The **common** (not Mnemosyne-private) conceptual model is:

```text
Outcome
├── status : Status
│   ├── Success
│   ├── Failure
│   └── Indeterminate
└── reason : Reason
```

`Status` is a **closed enumeration** with the exact accepted spellings `Success`, `Failure` and **`Indeterminate`**.

`Reason` uses **(B) an agreed common closed vocabulary plus governed, closed domain-specific extensions**, including persistence reasons where required. The common/domain extension arrangement is accepted; the **specific reason names/members are not yet adjudicated**. Labels such as `NotFound`, `VersionConflict`, `UnsupportedOperation`, `Timeout` or `ConfirmationUnavailable` are informative candidates, **not accepted enum identifiers**. The implementation/model contract determines how the closed vocabularies are represented without introducing an unrestricted free-text reason.

### 6.2 Meaning of status

| Status | Contractual meaning |
|---|---|
| **Success** | The reporting component knows that the specified operation succeeded according to **that operation's** contract. |
| **Failure** | The reporting component knows that the specified operation failed according to **that operation's** contract. |
| **Indeterminate** | **“I don't know at this point.”** The definitive outcome cannot currently be established from the reporting component's available evidence. |

`Indeterminate` is a **first-class epistemic/operational status**, not a euphemism for provider inadequacy, timeout, failure or likely success. A sound persistence engine with proper acknowledgement may nevertheless leave the outcome unknown **at the particular reporting instant**. The status does not require asynchronous resolution in every case nor imply that a later outcome will always be available.

A reason refines the result **within its operation context**; a reason need not map to only one status. For instance, a lookup successfully establishing absence may be `Success` with an applicable *not-found* reason, where that read contract defines the lookup itself as successfully completed. An unauthorised mutation, conflict or unsupported operation is not silently converted to ordinary absence.

### 6.3 Stage-specific meaning

```text
Request received
  ≠ Governed work accepted and durably preserved
  ≠ Specific adapter execution verified
  ≠ Authoritative persisted effect established
  ≠ Remote propagation / end-to-end business completion established
```

The same generic Outcome model can describe different operations or reporting stages. `Success` for accepting a work item does **not** imply successful authoritative persistence; a native transport ACK does **not** necessarily establish persisted effect. Contracts shall state what each reported Outcome actually covers.

### 6.4 Persistence Adapter owns execution, ACK and verification

A Persistence Adapter **MUST have an appropriate means to receive/interpret its engine's acknowledgement or otherwise verify the result** for the operations it claims to support. It is **the adapter's job** to execute a delegated operation and establish the operation's known result.

If the selected persistence technology requires extra reads, checks or out-of-band activities to validate a WRITE or UPDATE, the **adapter performs those activities as part of the sequence delegated by GPF**. The work is not pushed into GPF simply because it requires more than one native operation.

A provider without credible basic CRUD verification should be treated with serious suitability concern; such deficiency is **not what `Indeterminate` is intended to excuse**. Appropriate adapter capability requirements do not imply universal support for every operation or every enhanced native feature.

### 6.5 GPF owns persistence workflow and supervision

GPF controls broader persistence state and workflow, including dispatching/delegating operations and any applicable watchdog, timeout or failure-processing logic. It receives the adapter's factual result and decides the next **persistence-workflow** step. GPF does **not** own technology-specific verification or redefine the factual result merely because a watchdog expires.

For example, when GPF's watchdog expires before an adapter can establish a provider's result, GPF can report the operation as currently `Indeterminate` and exercise the appropriate failure-processing or supervision logic. That does not prove the underlying write failed or succeeded.

### 6.6 Correlation and non-blind recovery

Correlation is split according to actual expertise:

- **Persistence Adapter:** correlates native requests, acknowledgements, transaction IDs and any supplementary verification needed to establish the delegated operation's known result.
- **GPF:** correlates delegated operations with service state and broader persistence workflow.
- **Mnemosyne's governed boundary:** preserves the relationship to the relevant IU identity, applicable versions and operation outcome without substituting provider identity for IU identity.

When an operation is indeterminate, a blind repeat may produce an unintended duplicate or state change. The appropriate operation/adapter/workflow contract must protect against such risk; this DRP does not prescribe a universal idempotency algorithm or provider-native correlation envelope.

### 6.7 Persistence Outcome versus Mneme disposition

**Mnemosyne reports the persistence Outcome. Mneme decides its response to that information.**

Mneme may elect to regard an `Indeterminate` persistence Outcome as failure **of Mneme's own requested operation**, or it may wait, defer, reconcile or respond differently under its contract. Such a consumer-level disposition does **not** retrospectively change the reported factual persistence status to `Failure`. This separation of observed Outcome and consumer disposition is mandatory.

### 6.8 Deferred exact model details

The following **remain open to their owning detailed contracts**, without reopening MP-04/MP-06: exact common and persistence Reason enum members, stage naming, provider completion-proof thresholds, correlation-envelope shape, exact wire/Java models, timeout values, retry mechanisms, idempotency support and any particular GPF state machine.

---

## 7. Intrinsic and additive capabilities — MP-02/MP-03 accepted

### 7.1 Mnemosyne-level responsibilities versus provider capabilities

The nine responsibility allocations in §3.1 are **not** requirements for every individual Persistence Adapter to replicate all capabilities independently. Mnemosyne has its assigned responsibilities; the GPF and adapters jointly provide concrete persistence service capability.

Native search, transactions, indexing, bulk operations, history, subscriptions/notifications, traversal, version checks and other provider-specific functions may be available on some engines and absent on others. A provider can contribute **additive capabilities**, potentially richer than the minimum required for a particular operation.

The fact that a provider has a native feature does not automatically confer broader architectural responsibility upon it. Conversely, Mnemosyne cannot claim a governed outcome the selected provider/adapter cannot substantiate.

### 7.2 Represent capability asymmetry without universal parity

**MP-03 — accepted:** Represent common persistence obligations at the governed Mnemosyne/GPF level and **associate additive capabilities with the particular PersistenceServices that actually provide them**. Do not invent a new standard interface with fake implementations for features unavailable in other technologies.

Provider suitability is evaluated against the **particular IU, operation and governing requirements**. Capability limitations are reported accurately: inability to support a requested operation is not success. The existence of a `GovernedSearch` interaction does not require every adapter to support native search or require GPF to build a universal cross-provider search engine.

A capability may be statically known, configured or otherwise established; no dynamic-discovery protocol, universal capability schema, inheritance hierarchy or service registry is mandated by this DRP.

### 7.3 Architecture promotion threshold

Create or specialise an architectural Interaction for a provider capability **only if its responsibilities, boundary or governed semantics are materially distinct**. Do not create one new Interaction per CRUD verb, JDBC method, index option or other technology-specific operation. The existing `Interaction::Mnemosyne-to-PersistenceService` draft is reconciled to the five tiers and may be refined in its own subsequent review.

### 7.4 Historical capability provenance

The source candidate `DataManagement.InformationUnits.*` names, proposed Authoritative Persistence Feature and historical solution requirements remain traceable inputs, **not** automatically accepted current Features. Reconcile their semantic contribution against the actual 1.1.20+ `CT1 → CT2 → CT3 → Feature` model, retaining genuine one-to-many/partial matches and the distinction between capability ownership and capability support.

---

## 7A. Distributed Mnemosyne instances, capability subscriptions and work distribution — MP-08 accepted

### 7A.1 Required distributed deployment semantics

Mnemosyne **WILL be deployed with multiple simultaneously operating instances**. Each may handle the same, an overlapping, or a different/disjoint **subset of IU types**. An IU *type* identifies a class of information capability for registration and selection; an IU *instance* is an individually identified governed Information Unit. The distinction is fundamental.

The distributed architecture must fulfil three requirements:

1. **High availability and changeover:** loss or unavailability of one Mnemosyne instance must permit selection of another eligible instance, under the applicable operation/outcome contract.
2. **Concurrency at IU-type scope:** multiple Mnemosyne instances can process **different IU instances of the same registered IU type concurrently**.
3. **Load balancing:** Mneme can distribute eligible operations across multiple registered Mnemosyne instances. Round-robin is an illustrative strategy, not a prescribed algorithm.

**Concurrency for a particular IU instance belongs to Mneme.** This architectural requirement does **not** commission a new concurrent-write or independently-authoritative multi-engine coordination design. Infinispan may provide concurrent-write features, but the foreseeable release strategy does not depend on using those features for concurrent mutation of the same IU.

### 7A.2 Registration, discovery and selection

The two ends of the existing Mneme–Mnemosyne relationship have distinct adapter responsibilities:

- **Mnemosyne's tier-1 Mneme Adapter / Façade REGISTERS** the IU-type capabilities offered by **that Mnemosyne instance**, using the established Topic/subscription vocabulary.
- **Mneme's Mnemosyne Adapter (Store) DISCOVERS** the registered capabilities **using TopicMasks**. It establishes the eligible Mnemosyne instances for the requested IU type and supports load balancing and availability-related selection/changeover.

Multiple Mnemosyne instances **may register the same IU type**, and a registered instance may support multiple types. Registration is **capability declaration**, not assignment of IU-instance authoritative ownership; TopicMask matching is eligibility discrimination, **not** security authorisation. The matching semantics are those of the accepted shared TopicMask primitive (element-wise regex patterns against dot-separated canonical-name elements), not a new Mnemosyne-only mask language.

Illustrative operation flow (responsibilities, **not** mandated network topology or a separately deployed capability registry):

```text
                 Mneme (Infinispan / IU-instance concurrency)
                                |
                    Mnemosyne Adapter (Store)
                    TopicMask capability lookup
                    eligible-instance selection
                                |
          +---------------------+---------------------+
          |                     |                     |
    Mnemosyne A            Mnemosyne B            Mnemosyne C
    Mneme Adapter          Mneme Adapter          Mneme Adapter
    registers IU types     registers IU types     registers IU types
          |                     |                     |
       IU Controller / Constructor / GPF / Persistence Adapter
          |                     |                     |
          +---------- Same logical engine for a given IU --------+
```

The diagram does not impose a one-to-one mapping of Mneme Store objects to Mnemosyne instances: a Mneme-side Store seeks **eligible registered capabilities**, and the selected instance services the request. It also does not prescribe whether a particular provider is deployed as a cluster or single server.

### 7A.3 Logical persistence authority and IU-specific consistency

**For any given IU instance, different eligible Mnemosyne instances resolve to the same logical authoritative persistence engine.** That logical engine may itself be realised through multiple physical nodes, replicas or failover members. This is **one logical authoritative target**, not permission for separate Mnemosyne instances to maintain independent authoritative histories for the same IU.

The shared logical destination does not transfer IU-instance concurrency ownership from Mneme to Mnemosyne. Mnemosyne instances must still preserve applicable authoritative persistence/version/state and engine-level constraints; the Persistence Adapter continues to execute and verify native operations under GPF workflow control.

An IU's **InformationCoordinates** remain governed Topic references and are not replaced by the Mnemosyne instance selected for this request. Instance selection and logical persistence destination are different decisions; a new eligible instance for an IU must still reach the IU's appropriate logical engine.

### 7A.4 Availability and outcome correctness

**Capability support** and **current instance availability** are different facts. The mechanism by which availability is detected, reflected in registrations and used during selection/changeover is a **solution-design problem for Junie**. The Solution Architect has previously explored several mechanisms without adopting one. This DRP intentionally specifies requirements, **not a fourth invented algorithm**.

Changeover does **not** establish that a prior in-flight write failed. GPF/adapter operation evidence and the Calliope `Outcome.status = Indeterminate` meaning remain intact. Mneme may treat uncertainty as a failure of its consumer-level operation, but must not overwrite the factual persistence result. A load-balancing or retry decision must preserve IU-instance concurrency control and avoid unintended duplicated mutations.

### 7A.5 Architectural responsibility summary

| Concern | Responsible architectural location |
|---|---|
| IU-type capability declaration/registration | **Mnemosyne — Mneme Adapter / Façade** in each instance |
| IU-type capability discovery/matching | **Mneme — Mnemosyne Adapter (Store)** using TopicMasks |
| Mnemosyne instance selection, load balancing and changeover | **Mneme — Mnemosyne Adapter (Store)** |
| Concurrency for one IU instance | **Mneme** |
| Concurrent execution for distinct IU instances of the same IU type | **Multiple Mnemosyne instances** |
| Persistence workflow and operation supervision | **Each selected Mnemosyne instance's GPF** |
| Native operation, acknowledgement and verification | **Each selected Mnemosyne instance's Persistence Adapter** |
| Authoritative destination for the same IU, independent of instance selected | **Same logical persistence engine** |

### 7A.6 Explicit mechanism deferral and implementation evidence

The accepted distributed requirements do **not** prescribe heartbeat/lease algorithms, centralised registries, Infinispan Store implementation classes, request-routing protocols, leader election, load-balancing policy, fencing, clustering or automated health-check implementation. They are not an invitation to reintroduce distributed authoritative concurrent-write functionality for an individual IU.

Junie's solution design must establish workable **high-availability changeover, IU-type concurrency and load balancing**, and implementation validation should exercise instance failure, overlapping IU-type registration and distribution across eligible instances. The DRP sets their architectural obligations; an implementation algorithm is neither proposed nor pre-approved here.

---

## 8. Resilience, availability, durability and operational behaviour

Harmonia's accepted applicable NFRs remain binding, including durable capture of accepted governed work, authoritative information integrity, preserved versions, encryption in transit/at rest, resilience to ordinary node/service failures and applicable recoverability. **Mnemosyne is only partially responsible for Persistence Recoverability and is explicitly not responsible for catastrophic-failure recovery orchestration** (MP-02). This does not extinguish Mnemosyne's obligations to preserve information, evidence and correct state needed by a wider recovery capability.

**GPF** may use service-state management, supervision, watchdogs, timeouts and failure-processing workflows. **Persistence Adapters** own native failure handling, engine ACK interpretation and any technical follow-up required to verify delegated operations. The existence of a timeout does not establish a committed operation's failure. An `Indeterminate` result describes what is currently known.

The distributed deployment **MUST support high availability/changeover, concurrency for distinct IUs of a shared registered type, and load balancing**, with IU-instance concurrency owned by **Mneme** (§7A). The wider throughput objective of **100,000 incoming events in eight hours and completion within 24 hours** is not a mandated isolated per-adapter benchmark. Wider concurrency, RED/BLUE availability or redundancy and recovery requirements are not promises that every remote technology independently implements them. A selected provider must nevertheless be suitable for the operation's applicable obligations.

Retention holds, immutable version history, archival, integrity verification, authorised disposition and catastrophic recovery remain distinguishable. **RETIRE** is not equivalent to destructive DELETE. A provider-native DELETE is evidence of a native operation, not automatic proof of Harmonia-compliant disposition. Large binaries remain subject to existing pass-by-reference management, not mandatory PostgreSQL BLOB storage.

Operational logs are not substitutes for Kleio AuditEvents or governed provenance evidence. Feature mapping may identify measurable durable acceptance, indeterminate outcome age, provider latency, ordinary recovery and integrity metrics without prescribing metric implementations here.

---

## 9. Security, audit, provenance, ownership and model governance

### 9.1 Security — Themis-provided, locally executed PDP/PEP

Themis provides/enables Harmonia's PDP/PEP mechanisms and governs policy/authorisation semantics. **Actual execution is distributed wherever needed**, including within Mnemosyne's **InformationUnit Controller**. The local Controller does not invent an independently authoritative Mnemosyne policy system; nor is it obliged to call a central PDP for every request. Additional local enforcement positions are permitted where applicable.

IU `Metadata.SecurityContext` describes subject-side requirements through Topics/TopicMasks; Identity Passport conveys effective permissions. IU InformationCoordinates (gateway, endpoint, location, operational owner) are **independent** of the IU SecurityContext. A successful coordinate match or native provider credential does not itself grant IU disclosure or modification authority.

Security remains effective through native search, storage, indexes, result metadata, logs and error handling. A prior Mneme-side decision does not automatically replace applicable persistence-context enforcement.

### 9.2 Audit and provenance

**Kleio** manages AuditEvent information. **Dokimasia** is the assurance framework, with specific provenance roles following existing Canonical Provenance and Provenance Client Library allocations. Mnemosyne and its tiers provide necessary evidence/correlation of attempted operations, actors/authority, IU and version, completion status and persistence state where applicable. A native engine audit log does not automatically replace Harmonia AuditEvent and provenance obligations.

### 9.3 Information ownership and Content semantics

Persistent storage/custody, semantic source ownership, clinical/business authorship, information operational owner and authority to act are separate concepts. Mnemosyne is **not** semantic owner of stored Content. `InformationOwner` is a logical operational coordinate Topic, not evidence of authorisation or necessarily the creator's identity.

The IU Constructor/Disassembler handles governed structure; the Persistence Adapter handles technology-specific mapping. Neither becomes a universal business-semantic transformation engine or creates an extra governed IU merely because native persistence representation differs.

### 9.4 Calliope as model owner

**Calliope owns Harmonia models in general.** Domain-specific model content does not migrate to Mnemosyne simply because Mnemosyne uses it or supplies persistence-specific meanings. `Outcome`, `Status`, the common closed `Reason` vocabulary, persistence-specific closed Reason extensions and IU models are **Calliope-owned**, unless a strong reason for an exception is expressly accepted. **Model ownership is distinct from behavioural responsibility**: the Persistence Adapter determines the delegated operation's known result, GPF controls workflow, and Mnemosyne reports the Outcome.

---

## 10. Conformance expectations and architectural invariants

A conformant Mnemosyne solution shall demonstrate, within its assigned scope:

1. The **five accepted tiers** are discernible as responsibility boundaries; they need not be five deployments or mandatory sequential calls.
2. `GovernedPersistenceFramework` is the named **state/workflow-control** tier, rather than the encompassing entirety of Mnemosyne.
3. PersistenceService capability is **jointly realised** by GPF-controlled workflow and Persistence Adapter native execution, **not a sixth tier**.
4. The **Mneme Adapter/Façade** preserves `GovernedReader`, `GovernedWriter` and `GovernedSearch` contracts and Mneme's right to determine its own response to persistence outcomes.
5. The **InformationUnit Controller** executes the applicable **Themis-provided PDP/PEP functions locally**, without inventing another authorisation authority.
6. The **IU Constructor/Disassembler** preserves IU identity, four metadata contexts and its **exactly one** governed InformationRepresentation while supporting different physical persistence forms.
7. Mnemosyne's nine **Responsible/Partially Responsible** allocations in §3.1 are respected, including the explicit catastrophic-failure recovery exclusion.
8. GPF controls the broader persistence workflow, with supervision, watchdogs/timeouts and failure processing as applicable; technology-specific CRUD verification is **not** delegated upward to GPF.
9. Persistence Adapters own delegated execution, engine acknowledgements, technology-specific correlation and any additional verification needed to report the operation's known result.
10. The **Calliope-owned** `Outcome.status` is exactly `Success | Failure | Indeterminate`; governed closed common and domain-specific Reason vocabularies are used.
11. `Indeterminate` represents **currently unknown fact**, not definitive failure or evidence that an engine fundamentally lacks CRUD verification.
12. Mnemosyne's persistence Outcome and **Mneme's consumer-level disposition** remain distinct; a consumer can regard uncertainty as failure of its own workflow without rewriting the persistence fact.
13. Provider-native identity, version, acknowledgements and physical representation do not supplant IU UUID/Topic, operational Version/Active Generation, Content Structure Version and applicable native Content version.
14. Four Topic-based InformationCoordinates are interpreted for persistence as necessary, **independent of SecurityContext**, without treating them as embedded connection configuration.
15. Authoritative search is a Mnemosyne responsibility where supported by appropriate available capabilities; native search is not universally required of adapters.
16. Additive native capabilities do not imply feature parity, universal rich-search contracts or invented fallback functionality.
17. Audit, provenance, integrity, security, retention, ordinary recovery and accepted-work durability obligations apply according to their existing owning architecture; catastrophic recovery orchestration is outside Mnemosyne.
18. Relevant state/stage distinctions are maintained: request receipt, accepted durable work, verified delegated operation, authoritative persisted effect and remote/end-to-end completion are not synonymous.
19. An engine lacking credible verification for claimed CRUD operations is a suitability concern and is not excused by the legitimate existence of `Indeterminate`.
20. **Multiple Mnemosyne instances** operate concurrently, with overlapping/disjoint IU-type capability registration by the Mnemosyne-side Mneme Adapter/Facade, TopicMask discovery and availability/load-balancing selection by the Mneme-side Mnemosyne Adapter (Store).
21. **High availability/changeover, IU-type concurrency and load balancing** are demonstrably supported without assigning IU-instance concurrency coordination to Mnemosyne.
22. For a given IU instance, any selected Mnemosyne instance reaches the **same logical authoritative persistence engine** and respects the IU-instance concurrency controls owned by Mneme.
23. No new implementation Class, provider-interface hierarchy, prescribed physical topology, health-check/routing algorithm or other mechanism is claimed mandated merely by the conceptual five-tier/distributed model.

A technology choice or successful native ACK, by itself, is not conformance evidence for the governed persistence contract. Detailed models and service contracts must faithfully realise these boundaries without inventing more authority.

---

## 11. Explicitly not prescribed

The accepted five tiers are **responsibility architecture**, not prescribed code/deployment segmentation. Unless separately accepted in an owning contract, this DRP does **not** prescribe:

- Java classes, interfaces, records, DTOs, package names or a code-oriented implementation for each tier;
- separate physical deployments, containers, microservices, processes, network hops or an exact universal call chain for the five tiers;
- a universal PersistenceService interface that mandates capability parity;
- PostgreSQL as the only engine, one global database, a mandatory relational large-binary store, a provider physical schema, ORM, driver or connection pool;
- native transport/protocol choices, provider addresses, credentials, a separately deployed provider-selection registry or technology-specific capability-negotiation mechanism; the **Topic-based registration and TopicMask discovery responsibilities are prescribed** (§7A);
- physical cache structure, exact UUID compression encoding, IU serialization or the internals of provider-native mapping;
- specific multi-instance health detection, registration expiry, leases, load-balancing algorithm, failover/changeover protocol, leader election or Infinispan concurrent-write configuration;
- GPF's specific state-machine vocabulary, watchdog duration, retry/backoff algorithm, reconciliation procedure or persistence-workflow implementation;
- persistence adapter's specific ACK protocol, verification read strategy, transaction algorithm or native correlation-token scheme;
- a mandatory second read after every successful native write, or native CRUD support beyond an adapter's claimed operations;
- a single reason enumeration closed across all specialised domains; **common plus governed closed domain-specific reasons** are accepted, exact members remain open;
- mandatory terminalisation of every Indeterminate outcome before returning to Mneme;
- Mnemosyne responsibility for catastrophic-failure recovery orchestration;
- new separately governed IUs to represent provider-physical encodings, universal semantic Content conversion or automatic relationship synchronization;
- automatic creation of new Capabilities, Features, RequirementGroups, Classes, Interactions, Topic instances or architectural service wrappers without verified architectural necessity.

Architecture **does** require credible operation verification, truthful status reporting, the specified tier responsibilities, IU integrity and applicable governance. Implementation independence does not waive those obligations.

---

## 12. Design provenance and rationale

The baseline was prepared on **24 October 2026** from Harmonia's architectural discussion, current Continuum, AX-05, ADR-018 and historical Mnemosyne persistence-management evidence. Its stated intent was **conceptual identity and durable governance without constraining solution choices below the Mnemosyne boundary**. Earlier legacy artefacts and current code may validate feasibility but do not independently authorise architecture.

The accepted 11 November IU framework supplies an authoritative common meaning of IU identity, metadata, four coordinate references, Content preservation, operational version/generation, local versus remote relationships, and the division of governance responsibilities. Adjacent interaction drafts further describe Mneme's GovernedReader/Writer/Search and the intentionally asymmetric provider boundary. Those drafts have their own review questions and are not silently made final by this consolidation.

The resulting rationale is to make **Mnemosyne authoritative over durable persistence semantics**, while allowing persistence implementation differences without letting those differences leak into governed information meaning or masquerade as authoritative state.

---

## 13. MP-01–MP-08 adjudication and contract-reconciliation register

**All eight MP subjects were accepted by the Solution Architect in architectural dialogue.** The MP identifiers are the stable **discussion/adjudication subjects** used for this consolidated review. Where the original pre-adjudication register grouped a related issue under a different tentative MP row, the original topic is still preserved below or allocated to a named neighbouring interaction contract; the settled MP identifiers govern this revision.

| ID | Accepted subject | Disposition | Bounded residual work |
|---|---|---|---|
| **MP-01** | `GovernedPersistenceFramework` name and architectural identity | **ACCEPTED** | GPF is an accepted Mnemosyne tier for persistence state/workflow control; not necessarily a separately deployed code framework. |
| **MP-02** | Intrinsic vs partial Mnemosyne responsibilities; authoritative search | **ACCEPTED** | Nine specific allocations in §3.1. Search execution responsibility accepted where capabilities support it; not a universal search engine. |
| **MP-03** | Additive PersistenceService capabilities | **ACCEPTED** | Capability asymmetry and suitability; model native capabilities against real services, not universal parity; promote new Interactions only for material semantic distinction. |
| **MP-04** | `Outcome` semantics and Reason vocabulary model | **ACCEPTED** | Calliope-owned generic `Outcome`, closed `Success/Failure/Indeterminate`, common closed Reason enumeration plus governed closed domain-specific extensions; exact members deferred. |
| **MP-05** | Five-tier decomposition and GPF/adapter/PersistenceService relationship | **ACCEPTED** | Façade, IU Controller, Constructor/Disassembler, GPF, Persistence Adapter. A PersistenceService is realised jointly through GPF control and adapter execution; no sixth tier. |
| **MP-06** | ACK, verification, correlation, watchdog and uncertainty | **ACCEPTED** | Adapter executes and verifies; GPF supervises/controls workflow; `Indeterminate` = unknown **at this point**; Mneme's disposition is separate. |
| **MP-07** | Cross-DRP responsibility and interaction boundaries | **ACCEPTED** | Six cross-boundary allocations in §5 and §9. Related interaction DRPs remain separate for their own consolidation; no alteration authorised by this DRP alone. |
| **MP-08** | Distributed Mnemosyne instance registration, IU-type concurrency, load balancing and high-availability changeover | **ACCEPTED** | Each Mnemosyne instance registers IU-type capability via its Mneme Adapter; Mneme-side Mnemosyne Adapter (Store) discovers via TopicMasks and selects eligible instances. Mneme owns IU-instance concurrency. Same IU uses the same logical engine. Junie designs availability/changeover/selection mechanics. |

### 13.1 Accepted nuance and exact vocabulary

- The accepted status identifier is **`Indeterminate`**.
- The accepted **Calliope model-ownership default** applies generally across Harmonia, including domain-specific models. Responsibility to execute an operation is not model ownership.
- GPF may have watchdog/timeout/failure-processing logic, but **technology-specific verification is the Persistence Adapter's job**.
- `Indeterminate` is a legitimate statement of knowledge at report time, not a blanket exception for an unsuitable persistence engine.
- An accepted Persistence Adapter cannot claim authoritative success on mere dispatch or an acknowledgement that does not satisfy the operation's required completion evidence.

### 13.2 Original baseline questions and adjacent draft dependencies

The baseline's four open questions (GPF name, intrinsic versus solution capability, additive provider capability, and authoritative Outcome vocabulary) are answered by **MP-01–MP-04**. The interaction DRPs' open questions remain allocated to their respective scopes: exact provider-native correlation envelope, when a new interaction materially warrants architectural standing, detailed Reason members, operation-specific proof thresholds, coordinate population and path-specific security/authorisation checks. These do **not** reopen the accepted MP-05–MP-08 architectural allocation. MP-08 supplies requirements for multi-instance availability/changeover, IU-type concurrency and load balancing while leaving the mechanism for Junie solution design.

### 13.3 Not an execution instruction

Adjudication acceptance does not itself register, publish, commit or alter canonical Continuum elements. The complete consolidated text still requires Solution Architect review and explicit authorisation for any governed ADD/reconciliation.

---

## 14. Architecture Continuum incorporation — connected delta (after acceptance)

### 14.1 Motivation

Reuse the accepted applicable Axioms, Mandates, Requirements, AssuranceRequirements and obligations concerning authoritative durable persistence, acquisition, privacy/security, integrity, state/version, recovery, work acceptance, audit and provenance. Preserve native standing and existing links. Do not create duplicate Motivation identities from historical draft formulations.

### 14.2 Strategy

Reconcile historical `DataManagement.InformationUnits.*` candidate Features into the accepted **CT1 → CT2 → CT3 → Feature** catalogue. Respect Mnemosyne's **nine accepted full/partial responsibility allocations** and provider asymmetry. Do not claim all current Features belong exclusively to Mnemosyne or are implemented by a passive IU Information Object.

### 14.3 Business information architecture

Reuse `BO-InformationUnit` and current governance, ownership and source authority concepts. Do not add a Business Object for storage technology, a new governed representation for a provider schema, or new Business processes simply because an adapter exists.

### 14.4 Application information and Calliope models

Preserve accepted IU and its `InformationRepresentation`, `Information Unit Metadata`, Topic/TopicMask and four coordinate references. Model `Outcome`/`Status`/common and domain-specific `Reason` under **Calliope**, reusing existing appropriate canonical types before ADD. Record the accepted three-value status enumeration. Do **not** invent exact Reason members or provider-specific schema/transaction/exception identities on behalf of the design.

### 14.5 Application behaviour and Feature-scoped realisation

Represent the accepted Mnemosyne **five-tier** responsibilities while exercising architectural judgement about native elements: the tiers must be discernible without insisting that each conceptual box becomes a prescribed implementation Class, runtime, endpoint or Interface. `GovernedPersistenceFramework` controls service state/workflow; the Persistence Adapter handles technology-specific execution, ACK and verification. PersistenceService is **jointly realised** by their collaboration, not a sixth tier.

Preserve existing `GovernedReader`, `GovernedWriter` and `GovernedSearch` interaction purpose, Mneme's consumer disposition, locally executed Themis-supplied PDP/PEP and the division of ordinary versus catastrophic recoverability. **Represent MP-08**: multiple Mnemosyne instances, Mnemosyne-side IU-type capability registration, Mneme-side TopicMask discovery, availability/changeover, IU-type concurrency, load balancing, Mneme-owned IU-instance concurrency and common logical authoritative destination per IU. Do not manufacture a specific coordination or health-check implementation.

### 14.6 Application structure, native relationships and cross-layer evidence

Reuse canonical Mnemosyne and current related components/Facets. Employ genuine native Assignment, Realization, Serving, Access, Composition or other relationships according to actual semantics. Link five-tier responsibilities, IU construction, current Features and Motivation to their correct owners without fabricating coverage. Provider-specific additive capabilities should attach to the **specific** relevant PersistenceService/contract, not to every service. Registering IU-type capability and matching a TopicMask are not additional proof of authorisation or independent persistence authority.

### 14.7 Interaction DRP succession

Reconcile this accepted parent DRP with `DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md` and `DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md` when each is separately reviewed. The parent DRP's acceptance does **not** authorise independently modifying, completing or publishing either draft. They must preserve MP-05/MP-06/MP-08 truth: five tiers, GPF workflow control, adapter-owned execution/verification, truthful Outcome and separate Mneme disposition.

### 14.8 Explicitly unaffected architecture

No change is authorised here to the individual IU concurrency architecture owned by Mneme, independent multi-engine authoritative concurrent writes, or native clinical/business Content semantics, centralisation of Themis execution, Kleio audit ownership, Dokimasia/provenance allocations, catastrophe-recovery orchestration, physical technology/product choice, implementation code or unrelated DRPs. Reuse immutable accepted publication baselines.

### 14.9 Governed method and evidence

Following the Solution Architect's **complete-document acceptance**, a separately authorised governed ADD/reconciliation shall identify the accepted predecessor/release, source and decision input, reused/created/modified canonical identities and relationships, semantic mapping, conformance and round-trip results, human review gates and all inherited/new qualifications under repository praxis. This document itself authorises **no repository write, commit, push, publication or deployment**.

---

## 15. Review decision

**Standing:** Complete consolidated and adjudicated Mnemosyne DRP for the Solution Architect's review.  
**MP-01–MP-08:** Accepted in architectural discussion and incorporated into this document.  
**Whole-document acceptance:** **Not yet recorded**; requires explicit review of this consolidated artefact.  
**Decision authority:** Solution Architect / applicable Architecture Review.  
**Accepted inputs:** Information Unit Framework Final.001 and its accepted architectural incorporation, plus established Themis, Calliope, Mneme, Kleio, Dokimasia and Continuum obligations.  
**Remaining details:** Exact Calliope Reason members, persistence-operation proof/correlation contracts, provider-specific capabilities and exact canonical mapping under future governed ADD/interaction reviews. These are bounded contract/integration obligations, **not open MP-01–MP-08 concepts**. The MP-08 availability, changeover and load-balancing mechanism is explicitly reserved for Junie solution design, subject to conformance evidence.

---

## 16. Post-decision actions

1. Review the **whole** consolidated text and record corrections, if any. MP-01–MP-08 conceptual decisions have already been adjudicated; do not reopen them merely because a neighbouring draft predates this consolidation.
2. If accepted, retain a stable final DRP in `enterprise-architecture/design/offline-design/` as the reviewed input for a **separately authorised** Continuum ADD/reconciliation.
3. Require Codex, within such an authorised operation, to reuse canonical elements, resolve real Feature/Motivation relationships, represent the five-tier responsibilities and MP-08 distributed capability registration/discovery appropriately, and preserve full acceptance/publication evidence.
4. Review the two adjacent interaction DRPs separately under the accepted parent boundaries, including stage-specific Outcomes, adapter ACK/verification, correlation and capability-specific contracts.
5. Following Continuum acceptance, allow implementation planning under the governing build process; this DRP is **not** implementation authorisation.

---

---

## Appendix A — Informative source succession

| Source | Status for this consolidation | Contribution |
|---|---|---|
| `DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md` | Primary baseline, draft | Mnemosyne/GPF/PersistenceService identity, authoritative state, capability asymmetry, outcome uncertainty, Feature/Requirement context and original four open questions |
| `DRP-Information-Unit-Framework.Final.001.md` | Accepted architectural input | Atomic IU, singular representation, UUID+Topic, four metadata contexts, coordinates, security, generation/version distinctions, lifecycle boundary and preservation |
| `DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md` | Neighbouring draft | GovernedReader/Writer/Search, Mneme versus Mnemosyne responsibility, search/disclosure and authoritative outcome distinctions |
| `DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md` | Neighbouring draft | Provider-native representation freedom, additive capabilities, governed correlation and intentional asymmetry |
| Topic/InformationCoordinates package | Historical design context, overridden where updated by accepted IU | Topic identity/naming; IU coordinate names and security separation reconciled with accepted four-coordinate model |
| Accepted Continuum and 1.1.22 IU incorporation evidence | Governing architecture and contextual verification | Native accepted concepts, cross-component responsibility, capability/motivation reuse and architectural governance |

## Appendix B — Source terminology retained for reconciliation

- **Mnemosyne** — authoritative persistence Information System.
- **GovernedPersistenceFramework / GPF** — accepted fourth Mnemosyne tier for persistence-service state/workflow control.
- **PersistenceService** — capability jointly realised by GPF control and Persistence Adapter execution, not a sixth tier.
- **Authoritative Persistence** — historical proposed Feature label requiring mapping, not automatic creation.
- **DataSteadfastness.Recovery, DataAccessibility, DataThroughput** — historical proposed Feature names retained as source correspondence.
- **Authoritative durable acceptance** — governed semantic event, not synonymous with generic provider acknowledgement.
- **Interaction::Mneme-to-Mnemosyne** — Harmonia-governed semantic boundary and Reader/Writer/Search contracts.
- **Interaction::Mnemosyne-to-PersistenceService** — intentionally asymmetric lower provider boundary.

**End of complete adjudicated consolidation review candidate.**
