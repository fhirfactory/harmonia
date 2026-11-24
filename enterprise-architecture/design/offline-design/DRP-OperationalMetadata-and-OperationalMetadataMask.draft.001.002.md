<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — OperationalMetadata and OperationalMetadataMask

**Standing:** Draft 001-002 — expanded for Solution Architect review and explicit remapping approval; **not yet incorporated**  
**Baseline:** Accepted Harmonia Architecture Continuum 1.1.24; `DRP-Information-Unit-Framework.Final.001.md` (IU-01–IU-13); accepted Mneme–Mnemosyne Interaction and Mneme Cache Architecture decisions  
**Predecessor:** `DRP-OperationalMetadata-and-OperationalMetadataMask.draft.001.md`  
**Date:** 15 November 2026  
**Character:** Conceptual architectural refinement, allocation and migration analysis; **not** a Java class or physical-schema specification

## 0. Purpose, scope and authority

Harmonia needs to distinguish three *peer* metadata concerns:

1. **InformationMetadata** — characteristics and governance of the information itself;
2. **PetasosParcelMetadata** — characteristics of a particular conveyance or handoff;
3. **OperationalMetadata** — Harmonia middleware operations upon, and manifestations of, information, parcels and work.

The subject of this DRP is the third concern and its neutral selection construct, **OperationalMetadataMask**.

This revision expands Draft 001 by identifying the **existing IU metadata and lifecycle information that may be reassigned to OperationalMetadata**. The requested remapping is an *architectural proposal for the Solution Architect's approval*, not a claim that previously accepted IU-03, IU-09 or Mneme/Mnemosyne contracts have already been replaced.

**Explicit non-goals:** a generic untyped metadata bag; a second IU identity; a universal processing state machine; automatic changes to the four currently accepted InformationCoordinates; a new persistence service; a duplicate audit/provenance mechanism; a Java API, wire encoding, storage layout or subscription delivery implementation.

### 0.1 Existing sources and precedence

The accepted IU DRP presently defines:

```text
InformationUnit
├── Identity
├── Metadata
│   ├── ManagementContext
│   ├── InformationCoordinates
│   ├── SecurityContext
│   ├── LifecycleContext
│   └── [optional creator/updater-supplied Relationships]
└── InformationRepresentation [1]
    ├── ContainerMetadata
    ├── ContentMetadata
    └── Content
```

Its §5.1 gives Mneme's **operational Version**, **Active Generation**, cache references and transient/durable working characteristics to `ManagementContext`. Its §5.4 gives Mneme–Mnemosyne request/acceptance/commit/completion coordination to `LifecycleContext`; §§16–19 add temporal, persistence, recovery and visibility qualifications.

This Draft 002 must therefore be read **with**, not *instead of*, the full accepted IU DRP. Its proposed reallocation becomes authoritative only following explicit Solution Architect acceptance and governed Continuum incorporation. The accepted 1.1.24 interaction outcomes and cache architecture continue to govern behaviour irrespective of the future metadata location.

The pending IU companion `DRP-Information-Unit-Framework.revised.draft.002.md` describes the same conceptual refinement. Final reconciliation must keep both DRPs consistent; do not rewrite the original accepted `.Final.001.md` solely by citing this draft.

## 1. Conceptual separation

```text
Metadata framework (peer concerns, not one universal containment tree)
├── InformationMetadata
│   └── What the governed information is, where it logically resides,
│       its origin, its requirements, its relationships
├── PetasosParcelMetadata
│   └── What this conveyance is, from whom, to whom,
│       and its parcel-specific routing/handoff characteristics
└── OperationalMetadata
    └── What Harmonia is doing or has done with an associated subject:
        working revision, cache manifestation, processing, coordination,
        delivery progress, durability and recovery evidence
```

These are **semantic peers**, not mandatory children of a common physical object. They may be associated with an InformationUnit, a parcel, a Pragma, an Ergon execution, an Mneme cache manifestation, a Mnemosyne operation or other appropriate operational subject. No single central component automatically owns every operational property.

**A metadata association is not an ownership transfer.** The owning Feature retains its operational and evidence obligations. A property may be independently persisted, embedded in an operational structure, referenced, projected into an IU summary, or provided by an appropriate governed interface as its accepted contract requires.

### 1.1 Business state versus middleware-operational state

An Ergon may perform *business* processing and create a **new** InformationUnit whose Content expresses the new business/domain state. This is **not** `OperationalMetadata`.

`OperationalMetadata` describes middleware operation *from the business perspective*: for example an Ergon's execution activity, Ponos scheduling, Artemis coordination, Petasos handoff/retry, Mneme active-cache management, and Mnemosyne persistence coordination. These concerns remain distinct even when the business perceives all Harmonia processing as middleware.

Avoid the unqualified name `ProcessingState` unless its subject and governing vocabulary are specified; it must not silently blend business Content state, execution state, parcel delivery state and persistence state.

## 2. OperationalMetadata conceptual model

```text
OperationalMetadata
├── subject / association                  : [governed subject reference(s)]
├── operationalContext                     : [responsible Feature/domain]
├── operationalRevision                    : [when Mneme-managed active IU]
├── activeManifestation                     : [cache generation / residency, where applicable]
├── lifecycleCoordination                  : [when Mneme–Mnemosyne handshaking]
├── executionCoordination                  : [when Ponos/Ergon/Artemis activity]
├── conveyanceOperations                   : [when Petasos delivery/retry activity]
├── temporalObservations                   : [meaningful activity/handshake timestamps]
└── evidenceReferences                     : [correlations to governed logs/audit/provenance]
```

**Every line is a conceptual grouping, not a mandatory property list**; a concrete instance need not possess all groups. In particular, no common state enum, counter type, null sentinel, timestamp catalogue, correlation UUID or polymorphic table is prescribed.

The **subject** matters: a cache-generation value is meaningful relative to an IU manifestation, while a delivery-attempt value is meaningful relative to a parcel. Two operational records may refer to the same IU yet report different, simultaneously valid facts because they concern different activities.

## 3. Detailed InformationUnit remapping — proposal for approval

This section is the material addition in Draft 002. It distinguishes **retained information semantics** from **relocated middleware-operational semantics**.

### 3.1 Proposed conceptual before and after

**Current accepted allocation:**

```text
InformationUnit
├── Identity / own governed IU identity
├── Metadata
│   ├── ManagementContext
│   │   ├── Mneme operational Version
│   │   ├── Active Generation
│   │   └── Cache/working references and operational characteristics
│   ├── InformationCoordinates
│   ├── SecurityContext
│   └── LifecycleContext
│       └── Mneme–Mnemosyne persistence coordination
├── [Relationships]
└── InformationRepresentation [1]
```

**Proposed conceptual allocation (pending approval):**

```text
InformationUnit
├── Identity                          : governed UUID and IU Topic/name
├── Version                           : authoritative persisted IU revision
├── InformationMetadata
│   ├── InformationCoordinates        : governed logical placement/resolution/owner
│   ├── Origin                        : information provenance/origin distinction
│   ├── Security                      : IU-side requirements
│   ├── InformationLifecycle          : if genuinely about governed information
│   └── Relationships                 : creator/updater supplied links
└── InformationRepresentation [1]
    ├── ContainerMetadata
    ├── ContentMetadata
    └── Content

OperationalMetadata                 : separate conceptual metadata concern
├── MnemeManagement                  : operational revision / active generations
├── PersistenceCoordination          : Mneme–Mnemosyne request/ack/commit/outcome
└── OtherOperationalContexts         : cache, work, execution, parcel handling
        ↕ associated with appropriate IU, version, manifestation,
          activity or parcel — no universal single owner
```

The diagrams illustrate **semantic allocation**, not removal of essential data from reader-facing contracts. The accepted logical class names, existing interfaces and actual persistence/evidence arrangements require separately governed reconciliation.

### 3.2 Property-by-property remapping register

| ID | Existing IU allocation / concept | Proposed conceptual disposition | Required invariant or qualification |
|---|---|---|---|
| **OM-R01** | `InformationUnit.Identity.id` — IU UUID | **RETAIN in InformationUnit** | Persistent governed IU identity; never replaced by an operational record ID |
| **OM-R02** | `InformationUnit.Identity.name` — IU Topic | **RETAIN in InformationUnit** | IU identity/naming; distinct from parcel/operational classification Topics |
| **OM-R03** | `InformationUnit.version` — authoritative persisted IU revision (recently clarified) | **RETAIN in InformationUnit** | Revision established/returned by Mnemosyne; do not confuse with `ManagementContext.version` |
| **OM-R04** | `Metadata.ManagementContext.version` — Mneme operational Version | **REMAP to OperationalMetadata / Mneme-management concern** | Preserve cache coherence, stale-write/conflict and concurrency semantics; preserve read/write visibility required by façades |
| **OM-R05** | `Metadata.ManagementContext.activeGeneration` — individual active cache manifestation | **REMAP to OperationalMetadata / cache-manifestation concern** | Several manifestations may share one operational revision; generation ≠ IU identity, IU version or new representation |
| **OM-R06** | `Metadata.ManagementContext` embedded/transient/durable cache working data and cache references | **REMAP selectively to OperationalMetadata** | Working state may be transient *or* durable; retain local/remote reference semantics, residency, recovery and client visibility where required |
| **OM-R07** | `Metadata.LifecycleContext` request/acceptance/commit/completion handshaking | **REMAP middleware persistence coordination to OperationalMetadata** | Preserve accepted factual outcomes, durability stages, read-after-write behaviour, multi-site completion and error/indeterminate cases |
| **OM-R08** | `Metadata.LifecycleContext` operational timestamps, stage/correlation references | **REMAP where about operations** | Timestamp and correlation semantics must remain attributable; evidence cannot silently disappear with cache eviction |
| **OM-R09** | IU information lifecycle (current/superseded/retained/archived/retired etc.) | **RETAIN with information governance where it describes IU disposition** | Distinguish governed retention/RETIRE/hold from temporary persistence workflow stages; no universal state enum invented |
| **OM-R10** | `Metadata.InformationCoordinates` — four named Topic references | **RETAIN in InformationMetadata** | Gateway, endpoint, logical location, owner independent of security; seven-coordinate idea remains a separate candidate |
| **OM-R11** | `Metadata.SecurityContext` — action/compartment/other IU-required criteria | **RETAIN in InformationMetadata** | Themis Passport/PEP/PDP remain distinct; relocation does not turn routing or operational status into permission |
| **OM-R12** | Optional IU relationships/reference semantics | **RETAIN in InformationUnit / InformationMetadata** | Creator/updater-supplied, local validation as applicable, no mandatory remote target validation or reciprocal links |
| **OM-R13** | `InformationRepresentation.ContainerMetadata` | **RETAIN with InformationRepresentation** | Container type/characteristics remain descriptive of information envelope, not Petasos operations |
| **OM-R14** | `InformationRepresentation.ContentMetadata` — Content Structure/Version, Format, references | **RETAIN with InformationRepresentation** | Do not equate structure version with Mneme revision or authoritative IU version |
| **OM-R15** | `InformationRepresentation.Content` and optional native content instance version | **RETAIN with InformationRepresentation** | Business state belongs in resulting Content; no mandatory native version or extra nested IU |
| **OM-R16** | Provenance, Kleio AuditEvents, Dokimasia assurance evidence | **RETAIN in their established governance concerns** | OperationalMetadata may correlate/reference evidence; it does not replace event records or provenance |

**Requested decision:** approve the *semantic reallocation direction* for OM-R04–OM-R08, subject to the safeguards in §§4–6. All **RETAIN** rows constrain the reallocation and are not approvals to create duplicate fields.

### 3.3 Clarifying the existing ManagementContext

The accepted `ManagementContext` presently gathers working information maintained principally by Mneme. Under the proposal:

- Mneme's active working **operational Version** is metadata about Mneme's management of an IU and fits **OperationalMetadata**.
- **Active Generation** identifies a particular cache manifestation, not a revision of the governed IU, and fits the operational/cache context.
- Cache references, residency, stale-update indicators and associated observations fit operational metadata **when they describe management of information rather than the governed information itself**.
- The accepted rules for embedding, referencing, hybrid population and transient/durable characteristics **carry forward unchanged** until replaced by an explicitly reviewed contract.

Importantly, **moving the conceptual home is not permission to strip data from `InformationUnit` API views** where current operations depend on it. It may remain visible as an associated/projection view, even if semantically owned outside InformationMetadata.

### 3.4 Clarifying the existing LifecycleContext

The name `LifecycleContext` currently covers *persistence coordination*, but "lifecycle" can also denote an IU's governed information disposition. They must not be conflated.

**Move candidate:** Mneme–Mnemosyne operational handshake state, persistence-stage results, operational activity timestamps, retries and coordination references — allocated to **OperationalMetadata**, while remaining governed by their existing owners and interaction contracts.

**Retain as information concern:** IU retention, preservation, archive, hold, authoritative version history, retirement/disposition state where genuinely descriptive of governed information and its regulatory/semantic lifecycle.

**Do not confuse** successful initial acceptance with durable multi-site authoritative completion; the outcome vocabulary and commitments from the accepted 1.1.24 interaction remain controlling. The proposal creates no new universal state machine.

### 3.5 What must not be remapped

The separation must not move IU identity, immutable governed information, Topic/TopicMask naming, information owner/location, the IU's required SecurityContext, optional creator-supplied relationships, Content Structure/Version, native Content, or Content/native identifiers into OperationalMetadata for convenience.

`InformationUnit.InformationCoordinates` answers where the information is logically located/resolved; `PetasosParcelMetadata` answers how a particular parcel is conveyed; `OperationalMetadata` answers what the middleware did. Equality of some real-world values does not collapse their semantics.

## 4. Compatibility, authoritative access and durability conditions

A semantic reallocation is acceptable only if it preserves all of the following:

1. **Existing IU identity and atomic representation.** `BO-InformationUnit` and `model.core.information.InformationUnit` remain views of the same IU; one governed identity and one governed InformationRepresentation are maintained.
2. **Authoritative persisted IU Version.** Mnemosyne's persisted version remains attributable to the IU and stable across the governed read/write contracts. It is distinct from Mneme's operational revision, cache generation, Content Structure Version and any optional native Content instance version.
3. **Concurrency protection.** Every existing stale-update, optimistic concurrency, serialisation and conflict-detection obligation remains effective after relocating operational revision.
4. **Read/write contract compatibility.** Mneme's façades and Mnemosyne's accepted five logical contracts must still expose the operational/persistence facts needed for their consumers, perhaps by an associated/projection view. A consumer must not silently lose an accepted field or assurance.
5. **Durable work and truthful acknowledgements.** Persist-before-ack where applicable, no loss of governed work, configured multi-site authoritative durability and truthful Success/Failure/Indeterminate outcomes remain mandatory. Moving a field must not create an acknowledgement-before-durability loophole.
6. **Recovery and retention.** Operational facts required to resume, replay, reconcile uncertainty or demonstrate completed work are not disposed of merely because a cache entry or parcel disappears. Explicit retention and evidence obligations control their lifecycle.
7. **Security and disclosure.** Operational data must be disclosed only through suitable authorised contexts. Moving it out of IU SecurityContext does not remove applicable Themis enforcement or disclosure safeguards.
8. **Observability and evidence.** Iris observability, logging, Kleio AuditEvents, provenance and assurance remain distinct concerns; operational metadata may link to events without becoming an alternative audit ledger.
9. **No assumed physical co-location.** Atomic IU governance does not imply every operational observation must be written in the same persistence transaction or PostgreSQL schema. Required correctness/causality and durability must nevertheless be proven by the responsible contract.
10. **No new external source of truth by accident.** An OperationalMetadata projection or cache may not supersede Mnemosyne's authoritative persisted IU state, nor may operational observation be reported as a confirmed authoritative result.

These are **conditions for an approved allocation**, not a newly authorised migration design.

## 5. Additional operational subjects beyond InformationUnit

OperationalMetadata is a peer metadata concern usable beyond IU management:

| Operational subject | Illustrative operational facts | Semantic owner to preserve |
|---|---|---|
| Mneme active IU/cache manifestation | Operational revision, generation, cache presence, loading/staleness | Mneme and relevant cache contracts |
| Mneme–Mnemosyne governed operation | Request, acceptance, durable completion, uncertainty, recovery stage | Accepted interaction/Mnemosyne contracts |
| Petasos parcel/handoff | Delivery attempt, queue/retry/checkpoint/acknowledgement status | Petasos and its established orchestration |
| Ponos/Ergon execution | Scheduled/active/completed middleware work, execution and checkpoint metadata | Ponos and applicable Ergon framework |
| Artemis-related coordination | Operational coordination/assurance or work-management observations where governed | Existing Artemis/related Feature; no unverified vocabulary |
| Iris component observation | Current activity/health/observation | Existing Iris observation contracts; not automatically a durable IU fact |

**Petasos design remains on hold.** This DRP does not pre-empt unresolved PetasosParcel architecture or assign Petasos state fields/classes. The table records potential association only.

## 6. OperationalMetadataMask — conceptual selection model

```text
OperationalMetadataMask
└── governed operational criteria
    ├── subject/context discrimination
    ├── Mneme/cache/persistence characteristics [where available]
    ├── execution/work characteristics [where available]
    ├── conveyance-operation characteristics [where available]
    └── other explicitly governed dimensions
        └── TopicMask where the target field is a Topic
```

`OperationalMetadataMask` is **not** a subclass of TopicMask. It may **contain or reference** TopicMasks for Topic-valued operational dimensions, together with other criterion types only after their semantics are governed.

It is **neutral matching**, not an authorisation grant, processing instruction, event stream, operational snapshot guarantee or persistence command.

### 6.1 Subscription composition

```text
SubscriptionStatement
├── contentMask     : InformationUnitMask
├── routingMask     : PetasosParcelMask
└── operationalMask : OperationalMetadataMask [0..1]
```

The three discriminate respectively against information, conveyance and middleware operation. A matching subscription is conceptually a **conjunction** of its applicable criteria. Absence of the optional operational mask imposes no operational restriction; it does **not** imply a specific state or unrestricted information permission.

An operational criterion must declare the **subject and observation context** against which it is evaluated. Parcel state, Ergon execution state and Mneme cache state are not interchangeable merely because they are all OperationalMetadata. Some may be time-varying and not suitable for general-purpose durable subscriptions without explicit event/snapshot semantics.

### 6.2 Matching and absence

The following remain **unsettled** and must not be guessed in the ADD:

- OperationalMetadataMask UUID/identity, immutability, lifecycle and reference form;
- precise property/mask inventory, predicates, expression language and composition beyond agreed cross-mask AND;
- meaning of absent operational properties versus a wildcard match;
- handling of multiple matching operational records for the same IU or parcel;
- evaluation time, snapshot stability, change notification and replay semantics;
- which operational properties may be published, subscribed to or inspected, and by whom.

The already approved `Topic`/`TopicMask` design governs any Topic-valued member criteria, without the operational mask inventing alternate Topic matching rules.

## 7. Interface, migration and reconciliation approach

After Solution Architect approval, Codex should conduct a governed, **non-destructive** reconciliation, not a mechanical deletion of two IU context classes.

The required architectural mapping should cover:

1. **Canonical inventory:** identify current IU `ManagementContext`/`LifecycleContext` attributes and associations, Mneme active/cache responsibilities, Mnemosyne handshake/results, and current Petasos/Ponos/Artemis operational structures.
2. **Semantic allocation:** mark each attribute as **information**, **conveyance**, **operational**, or **unresolved**. Retain the responsible Feature, authority, security, version, preservation and recovery obligations.
3. **Compatibility surfaces:** establish whether each existing IU-facing field remains a projection, an associated reference, a separate OperationalMetadata view, or an unresolved contract. Avoid undeclared changes to `GovernedReader`, `GovernedWriter`, Summary operations, `SearchResult`, cache and continuation semantics.
4. **Causal/evidence relationships:** preserve relevant IU UUID, persisted IU version, operation/manifestation identity, and auditable correlation — without mandating new universal UUIDs for every operational datum.
5. **Logical and ArchiMate reconciliation:** reuse current canonical elements/relationships and existing capabilities/Features; create new elements only for truly absent approved semantics, consistent with Markdown authority and ArchiMate projection.
6. **Validation and governance:** record every resolved remapping and qualification with original sources/identifiers; preserve the immutable 1.1.24 release and follow existing HCRL/ADD acceptance processes.

No agent implementation, bulk schema migration or repository mutation is authorised by this review draft.

## 8. Adjudication register — explicit approval requested

| ID | Decision | Current standing | Proposed disposition |
|---|---|---|---|
| **OM-01** | Rename HarmoniaOperationalMetadata → **OperationalMetadata** | Agreed in dialogue | **ACCEPT** |
| **OM-02** | Three peer metadata concerns with subject-specific associations | Agreed in dialogue | **ACCEPT** |
| **OM-03** | Separate **OperationalMetadataMask** | Agreed conceptually | **ACCEPT** |
| **OM-04** | Optional `SubscriptionStatement.operationalMask` | Agreed conceptually | **ACCEPT** |
| **OM-05** | Business outcomes in new IU Content; operational states distinct | Agreed in dialogue | **ACCEPT** |
| **OM-06** | Mneme operational Version moves **conceptually** from IU ManagementContext to OperationalMetadata | **New detailed remapping** | **APPROVAL REQUESTED** (OM-R04) |
| **OM-07** | Active Generation and cache/working characteristics move conceptually to associated OperationalMetadata | **New detailed remapping** | **APPROVAL REQUESTED** (OM-R05–R06) |
| **OM-08** | Mneme–Mnemosyne operational handshake and stage/correlation data move conceptually from LifecycleContext to OperationalMetadata | **New detailed remapping** | **APPROVAL REQUESTED** (OM-R07–R08) |
| **OM-09** | Keep IU authoritative version, identity, information security, coordinates, relationships and representation with InformationUnit | Agreed semantics; explicit safeguard | **ACCEPT / PRESERVE** |
| **OM-10** | Keep governed information disposition/retention separate from middleware persistence-stage state | Existing architecture with new allocation distinction | **APPROVAL REQUESTED** (OM-R09) |
| **OM-11** | Retain all accepted concurrency, durability, five-contract and evidence outcomes across the remapping | Mandatory predecessor constraints | **PRESERVE WITHOUT CHANGE** |
| **OM-12** | OperationalMetadataMask exact attributes, matching syntax, absence, observation and snapshot rules | Unresolved | **DEFER; do not fabricate** |
| **OM-13** | Actual API projection, physical metadata housing, lifecycle storage and migration mechanics | Unresolved design | **DEFER to governed reconciliation** |
| **OM-14** | Petasos-specific state and parcel model | Separate DRP on hold | **DO NOT PRE-EMPT** |

**Intended acceptance scope:** Solution Architect acceptance of the *conceptual remapping* OM-06–OM-10, alongside the already discussed OM-01–OM-05, would authorise **architectural reconciliation planning**, not unilateral physical relocation, API breaking changes or data migration. Any rejected row remains anchored in the accepted IU source until separately resolved.

## 9. Connected Architecture Continuum delta after acceptance

If accepted, incorporate a bounded connected delta:

- **Business and application information:** distinguish governed InformationMetadata from OperationalMetadata and PetasosParcelMetadata; preserve original `BO-InformationUnit` / `model.core.information.InformationUnit` identities and exact-one-representation invariant.
- **Application data/behaviour:** allocate Mneme working/cache and Mneme–Mnemosyne handshake semantics to the OperationalMetadata concern, with explicit subject associations, existing owners and accepted interface-visible projections.
- **Strategy and motivation:** reuse existing governance, NFRs, Features and principles for resilience, logging, audit, concurrency, authorisation and provenance. Do not introduce a new generic operational-metadata capability merely because a conceptual grouping is named.
- **Cross-feature connections:** represent the shared informational associations needed by Mneme, Mnemosyne, Petasos, Ponos/Ergon/Artemis, Iris and Themis only where the approved semantics warrant them.
- **Reconciliation of documents:** adjust the proposed IU companion draft and, when separately authorised, the accepted IU framework's applicable sections without silently discarding IU-01–IU-13 decisions or predecessor evidence.

**Publication and incorporation remain governed by the Solution Architect and the Harmonia HCRL/ADD process.** This document does not alter an accepted release, grant information access, or initiate Junie work.

## 10. Solution Architect decision

**Status:** **PENDING REVIEW — DRAFT 002**.

Requested review emphasis: §§3.2–3.4 and the OM-06–OM-10 rows in §8. These capture the requested explicit IU-to-OperationalMetadata remapping.

The decisive architectural principle proposed for approval is:

> Operational characteristics of Harmonia's management and processing of an InformationUnit are associated with that IU through **OperationalMetadata**; they need not be intrinsic governed information metadata. Relocation must preserve all authoritative IU identity/version, access, concurrency, durability, recovery, evidence and interface obligations.

No repository file, canonical Continuum model, agent task or accepted baseline is modified by this draft.
