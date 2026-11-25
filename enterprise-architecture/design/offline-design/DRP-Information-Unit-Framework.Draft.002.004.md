<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — Information Unit Framework (Reconciled Conceptual Refinement)

**Standing:** Draft 002.004 for Solution Architect review; **not** an accepted replacement for `DRP-Information-Unit-Framework.Final.001.md`  
**Baseline:** Accepted Information Unit Framework (IU-01–IU-13), accepted Continuum 1.1.24 and Mneme–Mnemosyne Interaction 1.1.24  
**Date:** 26 November 2026  
**Change character:** Architectural conceptual refinement and explicit reconciliation; includes an agreed target logical model for Backlog Item 0005, not implementation authorisation

## 0.1 Draft 002.004 — agreed InformationUnit model for Calliope

This section records the Solution Architect's subsequent design discussion and **takes precedence over conflicting candidate structures, terminology and allocations elsewhere in this draft**. Earlier sections are retained as historical reconciliation context, not as competing instructions for Backlog Item 0005. The accepted architecture remains authoritative outside the explicitly stated proposed refinements; discrepancies with accepted DRPs must be reported for adjudication, not silently rewritten.

```text
InformationUnit
├── id : UUID                              [immutable]
├── informationConcept : Topic             [immutable]
├── description : String
├── InformationMetadata                    [mandatory, initialised container]
│   ├── PersistenceLifecycleMetadata       [mutable; Mnemosyne]
│   ├── CacheLifecycleMetadata             [mutable; Mneme]
│   ├── OriginMetadata                     [immutable]
│   ├── InformationCoordinates             [immutable]
│   ├── SecurityMetadata                   [mutable; Themis-governed]
│   └── Relationships                      [mutable]
└── InformationRepresentation              [one; immutable per governed revision]
    ├── ContainerMetadata
    │   ├── containerConcept : Topic
    │   └── containerSchema : Topic
    ├── ContentMetadata
    │   ├── contentConcept : Topic
    │   ├── contentSchema : Topic
    │   └── ContentDetail
    │       ├── size
    │       ├── language
    │       ├── height
    │       ├── width
    │       ├── frames
    │       ├── duration
    │       ├── pages
    │       ├── checksum
    │       └── checksumAlgorithm
    └── Content
        ├── reference
        └── data
```

### Identity and representation

- `id` is the InformationUnit UUID. `informationConcept` is the InformationUnit's classification Topic. Both are immutable.
- `InformationUnit.getName()` is `informationConcept.getName() + id.toString()` (**no inserted separator**). This supersedes the earlier proposed `Identity.name : Topic` and the `harmonia.mneme.iu.<compressed-uuid>` derivation in §2.1 for the target Calliope model.
- `description` is a direct `String` property.
- All defined metadata containers must be instantiated, with appropriate defaults; absent details must not require a null container.
- Exactly one governed `InformationRepresentation` exists per InformationUnit. Its representation is immutable **within a governed revision**. A governed revision may replace it; a transformation creating different information creates a **new InformationUnit** with provenance. These are distinct operations.
- `Content.reference` and `Content.data` are mutually exclusive: **exactly one** is populated. Referenced content and inline content are alternative ways to carry the one representation.

### Metadata responsibilities and change boundaries

- `PersistenceLifecycleMetadata` is Mnemosyne-governed authoritative persistence/disposition metadata; `CacheLifecycleMetadata` is Mneme-governed cache/active-generation/operational metadata. This is the **target logical allocation**, superseding §4's candidate placement of all operational concerns in a separate peer `OperationalMetadata`. Other operational metadata subjects may still exist independently.
- `OriginMetadata` records original source system (`Topic`), first Harmonia entry component (`Topic`), and entry `Instant`; these are immutable, including for internally generated information. It is not the original source's creation time, nor an alternative to derivation relationships.
- `InformationCoordinates` are immutable for the lifetime of the InformationUnit. Their detailed property inventory must be reconciled with accepted contracts rather than inferred from the older four-versus-seven candidate discussion.
- `SecurityMetadata` is mutable only under Themis governance and **must never reduce effective security restrictions**.
- `Relationships` are mutable IU relationships, preserving the applicable local/remote reference validation boundaries.
- `InformationItinerary` is **not part of the target InformationUnit metadata**. Publication/subscription and routing concern Pragma/Pragmatosis; routing decisions belong to `Ponos.Praxis.Router`, which requests Mneme to apply appropriate changes. Confirm any affected accepted interfaces separately.

### Content and container semantics

- `containerConcept`, `containerSchema`, `contentConcept` and `contentSchema` are `Topic` properties. Their values are not required to equal `InformationUnit.informationConcept`.
- `contentConcept` identifies **what the content represents** (e.g. `FHIR.R5.Patient`); `contentSchema` identifies **how it is represented** (e.g. `application/fhir+json`). Thus no additional `mimeType` field is required.
- `containerSchema` uses the predefined system `RAW` Topic where there is no distinct container schema. Every container has a concept, even for simple content.
- Concepts/schemas and related value sets are governed by Calliope **release-defined builders**, while individual Topic instances can be created ad hoc.
- `ContentDetail.size` denotes **content size in bytes**. Other media-specific details apply where meaningful; do not invent universal values.
- `ContentDetail.checksum` is a digest of the actual content bytes, inline or externally referenced. `checksumAlgorithm` explicitly identifies the algorithm; **default `SHA-256`**. A checksum for referenced content must describe the referenced bytes, not the reference string.
- Compression and encoding, if needed, require an explicit responsibility decision; do not silently introduce new mandatory fields.

### Implementation/reconciliation boundary

This section establishes the agreed **containers, mandatory named properties, ownership and invariants**. Codex/Junie may propose further ordinary properties *inside those responsibility boundaries* as needed, but must return any **new architectural semantics, contradictory accepted contracts or ownership changes** for Solution Architect adjudication. This draft does **not** authorise implementation, changes to other consumers, or modification of accepted baselines. The exact Java property types and applicability of the remaining `ContentDetail` media descriptors are implementation-design matters unless they create an architectural decision.

## 0. Authority and scope

The definitive construct remains **`InformationUnit`**, not `ManagedIU`. Historical use of `ManagedIU` in dialogue meant “an InformationUnit being managed” and does not authorise a new type. `BO-InformationUnit` and `model.core.information.InformationUnit` are existing conceptual/logical views of the same governed information unit, not two entities.

This draft should be reviewed **together with the complete** `enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md`; **every IU-01–IU-13 decision and all accepted contracts in that source remain effective unless a specific change is expressly accepted**. In particular, this draft does not reprint or delete the source's full Capability/Requirement mapping, contract conformance register, retention/disposition requirements, search-result contract, audit/provenance distinctions, and other source sections. It is a consolidated *proposed amendment*, **not a claim that an accepted source has already been changed**.

**Related decisions:** `DRP-Topic-and-TopicMask.draft.001.md` was approved by the Solution Architect and is intended to become `DRP-Topic-and-TopicMask.Final.001.000.md` in `offline-design`; repository placement is not presumed complete. `DRP-OperationalMetadata-and-OperationalMetadataMask.draft.002.md` has received explicit Solution Architect approval of **OM-06–OM-10**, supplementing the previously agreed OM-01–OM-05; the filename/finalisation and governed ADD remain separate steps. `DRP-PetasosParcel-and-PetasosParcelMask.draft.001.md` remains **on hold** pending further conceptual design.

**Change relative to Draft 002:** the former *candidate* remapping of IU management and middleware lifecycle concerns is now **approved at conceptual level** and captured as a detailed OM-R01–OM-R16 crosswalk below. This is **not** a retrospective claim that the original four-context IU logical model or its interfaces have already been migrated. The accepted Mneme–Mnemosyne five-contract interaction, Mneme Cache Architecture, and all IU-01–IU-13 invariants remain controlling.

## 1. InformationUnit — conceptual subject and structure

An InformationUnit is Harmonia's logically **atomic, individually governed unit of information**, irrespective of implementation language, physical storage mechanism or conveyance. It is not a Java object, message, parcel, FHIR Resource, database row, cache copy, transformation engine or universal workflow instruction.

```text
InformationUnit
├── Identity
│   ├── id                           : UUID
│   └── name                         : Topic
├── version                          : String
├── InformationMetadata              : [conceptual information-related metadata]
│   ├── InformationCoordinates       : [where/how information is logically resolved]
│   ├── Origin                       : [where the information came from]
│   ├── Security                     : [compartment, label, other requirements]
│   ├── InformationLifecycle         : [governed information disposition, as applicable]
│   └── Relationships                : [creator/updater-supplied references]
└── InformationRepresentation [1]
    ├── ContainerMetadata
    │   ├── type                     : Topic
    │   └── ...                      : envelope/assembly characteristics
    ├── ContentMetadata
    │   ├── type                     : Topic
    │   ├── structureVersion         : String
    │   └── ...                      : format, reference, structure/technical descriptors
    └── Content                     : [portable representation or governed reference]
```

This is **conceptual**: `InformationMetadata`, `Origin` and `InformationLifecycle` are conceptual groupings whose exact logical representation remains subject to reconciliation. It is **not** a directive to create new Java fields/classes, rename currently accepted `Metadata` wholesale, or silently erase the present four metadata contexts. The currently accepted logical Class decomposes `Metadata` into `ManagementContext`, `InformationCoordinates`, `SecurityContext`, `LifecycleContext`, with optional relationships. **Approved OM-06–OM-10 change the proposed conceptual allocation, not the pre-incorporation logical state.** See §§3–4.

## 2. Identity and distinct revisions

### 2.1 Identity

`Identity.id : UUID` is the unique governed IU identifier. `Identity.name : Topic` is its named topic/classification, conceptually derived from its UUID under the accepted naming pattern `harmonia.mneme.iu.<hex-string-of-compressed-uuid>`. The Topic's **own** UUID is distinct from the IU UUID. Never conflate those or treat a topic name as authoritative IU identity.

### 2.2 Version semantics — proposed reconciliation

The Solution Architect's discussion and OM-06–OM-10 approval clarified distinct roles:

| Value | Intended meaning | Status |
|---|---|---|
| `InformationUnit.version : String` | **Authoritative persisted IU revision** established/returned by Mnemosyne; analogous in role to a FHIR `meta.versionId`, but not restricted to FHIR | Agreed recent conceptual direction; location/contract change requires reconciliation |
| Mneme operational revision (`ManagementContext.version` in the accepted IU model) | Active-information/cache/concurrency revision used by Mneme | Accepted semantic distinction; **approved conceptual relocation** to OperationalMetadata under OM-06 |
| `ContentMetadata.structureVersion` | Version of the Content Structure/model/profile | Accepted unchanged |
| Content-native version, **where supplied** | Version under the Content's own native information model | Accepted unchanged; no fabricated native version |
| Active Generation | Identity of an active cache manifestation, not a revision of information | Accepted unchanged; **approved conceptual relocation** under OM-07 |

**Explicit conflict to adjudicate:** the accepted 1.1.22 IU Framework (§§9, 16) states that native Content instance version resides in Content and documents three version concepts. The later clarification describes a **Mnemosyne-governed authoritative persisted IU revision at `InformationUnit.version`**, in addition to Mneme operational revision, and analogises it to `Patient.meta.versionId`. The Solution Architect has said this is paraphrasing existing intended distinctions, not inventing a gratuitous duplicate; nevertheless **the written accepted baseline must be reconciled carefully** so the authoritative persisted revision and native payload version are not silently conflated or made duplicative. Specifically, preserve the accepted Mneme–Mnemosyne rule that GovernedWriter may return the incremented authoritative IU version and authoritative read cannot expose pre-durable versions. Do not infer any implementation counter, native payload mutation or guaranteed equivalence to FHIR meta.versionId without an explicit contract.

### 2.3 Container and Content identities

Do **not** introduce mandatory `Container.id` or `Content.id` UUIDs. Where a represented object or external content has a native identity or persistent reference, preserve it in native Content or applicable ContentMetadata / ContainerMetadata under the corresponding contract. This does not create nested independently governed IUs.

## 3. Information-related metadata (InformationMetadata)

`InformationMetadata` is the **conceptual peer** of `PetasosParcelMetadata` and `OperationalMetadata`, not necessarily a mandatory new wrapper around the current IU `Metadata` Class. Its concerns are:

| Concern | Semantic responsibility | Relationship to current accepted IU |
|---|---|---|
| InformationCoordinates | Where information is stored/resolved, logical owner, gateway, endpoint/location | Accepted four named Topics: PersistenceGateway, GatewayEndpoint, InformationLocation, InformationOwner |
| Origin | Where the **information** came from | Distinct from Petasos parcel source; precise class/field and provenance relation not final |
| Security | IU-required compartment, label, action/other criteria | Corresponds to current IU-side SecurityContext; separate from Themis Passport permissions |
| Information-related State | State of governed information *where distinct from operational execution state* | Must reconcile with current LifecycleContext; not a universal native business state machine |
| Relationships | Explicit relatesTo/partOf/basedOn-like references provided by creator/updater | Preserve optionality, local validation, no mandatory remote validation, no automatic reciprocal relations |

**InformationCoordinates:** the existing accepted four Topics remain the baseline until an authorised change; later discussion considered seven dimensions (`SourceEntryComponent`, `SourceComponent`, `TargetComponent`, `TargetExitComponent`, `Origin`, `Destination`, `Owner`). That seven-part candidate is **not yet approved as a replacement**, nor may `PersistenceGateway` be automatically equated to `Origin` or `TargetExitComponent`. Different activities may give the same gateway different interaction roles. Coordinates do **not** establish security permissions.

**Origin:** Information origin must be conceptually distinct from InformationCoordinates (where stored/resolved), even if some concrete Topics happen to be equal. It must also be distinct from `PetasosParcel.Source` for a given re-publication. Define origin/provenance representation and preservation separately; don't duplicate Kleio audit evidence or IU relationships as a substitute.

**Security:** this is IU-side requirements, not the requester's permissions. Themis Passport/PEP/PDP semantics, action/compartment/other masks, positive permission rules and non-disclosure remain governed by their own contracts. A Topic/TopicMask match alone grants nothing.

**Business-level states** created through Ergon processing belong in new resulting IU Content, if they form part of represented business information; they are not generic middleware processing states. An Ergon's transformation generates a separate governed IU with appropriate provenance, preserving the one-representation invariant.

## 4. OperationalMetadata: a peer, not an automatic IU child

The new conceptual split is:

```text
Metadata (peer concerns; no mandatory shared physical owner)
├── InformationMetadata       — characteristics of governed information
├── PetasosParcelMetadata     — conveyance/routing characteristics
└── OperationalMetadata       — Harmonia middleware activity, state, execution
```

### 4.1 Approved conceptual reallocation

**OM-06, OM-07 and OM-08 are APPROVED by the Solution Architect.** Their intent is to put operational facts in an **associated peer OperationalMetadata concern**, instead of treating them as intrinsic governed InformationMetadata. **OM-09 and OM-10 are also approved** and protect the IU's authoritative properties and its information lifecycle.

```text
Accepted IU logical model (before governed incorporation)
InformationUnit.Metadata
├── ManagementContext
│   ├── Mneme operational version
│   ├── Active Generation
│   └── Cache / working context
├── InformationCoordinates
├── SecurityContext
└── LifecycleContext
    └── Mneme–Mnemosyne persistence coordination

Approved conceptual allocation (target; not yet migrated)
InformationUnit
├── Identity and authoritative persisted Version
├── InformationMetadata
│   ├── InformationCoordinates
│   ├── Origin / provenance association
│   ├── Security requirements
│   ├── Governed information lifecycle / disposition
│   └── Optional Relationships
└── InformationRepresentation [1]
    ├── ContainerMetadata
    ├── ContentMetadata
    └── Content

OperationalMetadata (peer concern, associated with relevant subjects)
├── Mneme operational revision
├── Active Generation / cache manifestation / working references
├── Mneme–Mnemosyne persistence handshaking and activity
└── Other middleware-operational observations as governed
```

The target diagram is **conceptual**. It does not require a wholesale rename of the existing `Metadata` class or automatic deletion of `ManagementContext` and `LifecycleContext`; Codex must reconcile current canonical identities, interface contracts and required projections in a later, separately authorised operation.

### 4.2 IU-to-OperationalMetadata mapping — accepted decisions

| Crosswalk | Existing IU subject | Approved allocation | Preservation obligation |
|---|---|---|---|
| **OM-R01** | IU UUID | **Remain with IU** | Unique persistent governed identity |
| **OM-R02** | IU Topic / name | **Remain with IU** | Distinct from Topic's own UUID |
| **OM-R03** | Authoritative persisted IU Version | **Remain with IU** | Mnemosyne-established/returned version; authoritative-read durability |
| **OM-R04 / OM-06** | `ManagementContext.version` (Mneme operational revision) | **OperationalMetadata** | Staleness/conflict/concurrency behaviour and required visibility |
| **OM-R05 / OM-07** | Active Generation | **OperationalMetadata** | Per-manifestation identity; no extra IU or representation |
| **OM-R06 / OM-07** | Working/cache context, refs, residency | **OperationalMetadata where operational** | Transient/durable distinctions and cache recovery |
| **OM-R07 / OM-08** | `LifecycleContext` handshake stages | **OperationalMetadata** | Request/accept/commit/completion, outcomes and durability |
| **OM-R08 / OM-08** | Operational timestamps / coordination references | **OperationalMetadata where operational** | Traceability and retained evidence |
| **OM-R09 / OM-10** | IU retention, archive, hold, retirement and information disposition | **Remain with governed information** | Distinct from persistence/processing stages |
| **OM-R10 / OM-09** | Four InformationCoordinates | **Remain with InformationMetadata** | Logical placement/owner; independent of security |
| **OM-R11 / OM-09** | SecurityContext | **Remain with InformationMetadata** | IU required criteria; Themis permissions independent |
| **OM-R12 / OM-09** | IU relationships | **Remain with IU / InformationMetadata** | Optional, author-supplied local/remote references |
| **OM-R13 / OM-09** | ContainerMetadata | **Remain with InformationRepresentation** | Container/envelope characteristics |
| **OM-R14 / OM-09** | ContentMetadata | **Remain with InformationRepresentation** | Content Structure Topic/Version, format, references |
| **OM-R15 / OM-09** | Content | **Remain with InformationRepresentation** | Native content/domain state, single governed representation |
| **OM-R16** | Audit, provenance, assurance | **Retain specialist responsibilities** | Kleio, appropriate provenance owner, Dokimasia and logs are distinct |

The mapping **does not reassign owning Features**. For example, Mneme remains responsible for its concurrency/active-cache logic, and Mnemosyne remains responsible for its authoritative persistence commitments, even when their operational data is associated through a common metadata concept.

### 4.3 Information lifecycle is not an operational handshake

An IU may have information-specific preservation, retention, legal hold, archive, supersession or RETIRE semantics. Those are **governed information concerns** and stay associated with the IU. They do not become merely transient operational state.

A persistence request's acceptance, processing, durability confirmation or indeterminate result describes a **middleware operation** and belongs conceptually to OperationalMetadata.

Neither category should be conflated with **business state**: an Ergon's changed business/domain state is expressed in the Content of its resulting new IU.

### 4.4 Mandatory compatibility and accountability

Approved semantic relocation **does not authorise breaking**:

- `GovernedReader`, `GovernedWriter`, `GovernedSummaryReader`, `GovernedSummaryWriter`, `GovernedSummarySearch`, cache/reacquisition or continuation contracts;
- Mneme optimistic concurrency and stale-operation detection;
- Mnemosyne authoritative Version selection/return, multi-site durability, truthful outcomes and prevention of pre-durable reads;
- no-loss work/retry/recovery, preservation, retention and audit/assurance commitments;
- authorisation, compartment protection, safe summaries and least disclosure;
- operational visibility where an established consumer needs an IU-facing or summary-facing projection.

An operational property may be **associated with** an IU and exposed through a governed projection without becoming intrinsic `InformationMetadata` or requiring co-persistence in the same physical object. Conversely, removal from an IU-facing contract requires specific approval and migration evidence; this DRP alone does not approve that removal.

Petasos queue/delivery/retry progress and Ponos/Ergon/Artemis execution state are also middleware-operational *candidates* associated with their own subjects. Their detailed structures remain governed separately; **PetasosParcel design is on hold**. The separate `OperationalMetadataMask` discriminates operational properties where governed, not native business state.

## 5. InformationRepresentation, ContainerMetadata, ContentMetadata and Content

Each IU has **exactly one** governed InformationRepresentation, with exactly one ContainerMetadata, ContentMetadata and Content as applicable to its accepted contract. A different *governed* transformation produces a **new IU**, never an additional alternative governed representation inside the original IU.

**ContainerMetadata:** information about the envelope, assembly or carriage of represented Content. Conceptual `ContainerMetadata.type : Topic` classifies that envelope. Other properties (source, destination, author, recipient, envelope structure, assembly context) may be native/container-specific rather than mandatory universal fields. There is **no separate `InformationUnit.Container.ContainerType`** required outside ContainerMetadata.

**ContentMetadata:** describes Content structure and technical properties independently of payload values. Conceptual `ContentMetadata.type : Topic` identifies Content Structure and `structureVersion` describes its specification/model/profile version; format, encoding, language, content reference, size and digest may be included as governed requirements. **Unit Structure ≠ Content Structure ≠ Content Format.** Native identifiers/references may appear where semantically appropriate. ContentMetadata does not substitute for native business state embedded in Content.

**Content:** governed portable information representation, textual/binary or a governed reference (including pass-by-reference for larger payloads). The architecture need not impose a JSON string on every payload and should not assume a Java Object graph is a portable authoritative representation. Native Java domain objects are valid *working manifestations* for Java processing but cannot be the only representation required by non-Java peers, archival persistence, audit or interoperable interchange. Serialisation/decoding is an explicit boundary contract with known content structure/format. Avoid uncontrolled Java native serialisation and uncontrolled whole-content logging. AuditEvents and LogEvents must have explicit evidence/redaction/retention semantics, not automatically embed full IU Content.

## 6. Selection and shared mask composition

A single InformationUnit can contain **multiple Topics describing different facets of the same information**. Therefore a higher-level `InformationUnitMask` is conceptually distinct from `TopicMask`.

```text
InformationUnitMask
├── identityName criterion                -> InformationUnit.Identity.name
├── information-coordinate criteria       -> InformationMetadata.InformationCoordinates
├── information-security criteria         -> InformationMetadata.Security (where applicable)
├── container-type criterion              -> InformationRepresentation.ContainerMetadata.type
├── content-structure-type criterion      -> InformationRepresentation.ContentMetadata.type
└── [other governed IU characteristic criteria, if specified]
```

This is a **conceptual matching subject** rather than a settled class, field catalogue or permission model. **Middleware-operational state moved under OM-06–OM-08 is not an intrinsic IU criterion:** select it through `OperationalMetadataMask`, when appropriate, rather than silently reintroducing it through `InformationUnitMask`. An InformationUnitMask may contain/reference TopicMasks for Topic-valued fields; other field types require their own properly specified comparison semantics. Criteria referring to information-security requirements must not accidentally disclose restricted Content or turn a topic-selection match into Themis authorisation. Whether each criterion is mandatory, optional or repeatable is deferred.

A `SubscriptionStatement` is composed, rather than inherited, as:

```text
SubscriptionStatement
├── contentMask     : InformationUnitMask
├── routingMask     : PetasosParcelMask
└── operationalMask : OperationalMetadataMask [optional]
```

These refer to different subjects: **governed information**, **a particular parcel/conveyance**, and **Harmonia operational activity**. A publication can match an IU through one parcel but not another, without the IU changing. Matching and disclosure-authorisation remain separate. `PublicationStatement` and `FilterStatement` may also contain/reuse appropriate masks. Do not make `Themis.Passport` a subclass of InformationUnitMask by default; it contains more than information-discrimination criteria, including identity permissions/actions.

No universally mandatory IU `get()`/`test()` catalogue is introduced. Consumers use the governed façade and selection contracts without leaking IU internal class layout.

## 7. Retained accepted IU invariants

The following decisions from accepted IU-01–IU-13 and the 1.1.24 Interaction are **retained** unless explicitly qualified above:

1. An IU is atomic logically, independent of physical decomposition and language.
2. The IU UUID, IU Topic, externally referenced Topics and native Content identity are different things.
3. Exactly one governed InformationRepresentation; client transformations create new IUs with provenance.
4. Optional author-supplied relationships, locally validated by Mneme as applicable; no universal remote-reference checking or automatic reciprocal relationships.
5. InformationCoordinates remain independent of SecurityContext; logistical ownership does not create authorisation rights.
6. SecurityContext describes IU requirements, Passport describes identity authority, PEP/PDP enforce; TopicMask matching alone is not disclosure authorisation.
7. Existing Mneme/Mnemosyne persistence/outcome, cross-site durability and authoritative-read contracts remain controlling.
8. Operational revision, active generation, authoritative persistence version, Content Structure Version, and optional native Content version must be disambiguated, not merged under a generic `version`.
9. AuditEvents (Kleio), assurance (Dokimasia), provenance and operational logs are distinct.
10. Information history, retention, archive, hold, immutable versions, RETIRE semantics and resilience remain governed requirements.
11. Search-result/summary operations do not automatically disclose full Content; authorised retrieval is separate.
12. No huge binary payloads mandated in PostgreSQL; preserve governed Content references and the existing pass-by-reference threshold.
13. No architecture assumption that every conceptual box is a separate Java class, runtime service or ArchiMate model element.

## 8. Reconciliation and review register

| ID | Question/decision | Present status / required treatment |
|---|---|---|
| IU-R01 | `InformationUnit` is definitive, not `ManagedIU` | Agreed; no new type |
| IU-R02 | IU conceptual metadata peers: Information / Parcel / Operational | Agreed concept; physical allocation not specified |
| IU-R03 | IU authoritative `version` vs Mneme operational revision | Agreed distinction; reconcile older §16/§9 prose and current authoritative writer/read contract |
| IU-R04 | Container/Content Type within respective metadata | Agreed; no duplicate external type properties |
| IU-R05 | No mandatory Container.Id / Content.Id UUIDs | Agreed |
| IU-R06 | Business/domain processing state in resulting IU Content | Agreed; distinct from middleware operational state |
| IU-R07 | Portable governed Content; Java objects are working manifestations | Agreed architectural direction; exact wire/storage format not prescribed |
| IU-R08 | `InformationUnitMask` selects across multiple IU characteristics | Agreed concept; precise fields/operators/cardinalities open |
| IU-R09 | OM-R04–OM-R08 operational reallocation | **APPROVED in OM-06–OM-08**; implement only after governed canonical/contract reconciliation; preserve interface projections and operational guarantees |
| IU-R10 | Four accepted InformationCoordinates vs seven proposed | **Not yet resolved**; do not substitute seven automatically |
| IU-R11 | `Origin` relation to provenance and location | Conceptual distinction agreed; concrete representation open |
| IU-R12 | `InformationMetadata` vs accepted `Metadata` / four contexts | Conceptual grouping agreed; **OM-06–OM-10 approved**; canonical identity, compatibility projection and exact class allocation remain incorporation work |
| IU-R13 | Original IU-01–IU-13 mapping, full acceptance/conformance obligations | Preserved by reference to accepted complete DRP; no silent reopening |
| IU-R14 | Approved OM-09 IU identity/authority/security/representation safeguards | **APPROVED**; no migration of information properties into operational metadata |
| IU-R15 | Approved OM-10 governed information lifecycle versus middleware state | **APPROVED**; distinguish disposition from persistence handshaking |
| IU-R16 | OperationalMetadataMask and optional subscription operational criterion | Agreed concept; details governed by separate OM DRP; no operational criteria smuggled into IU mask |
| IU-R17 | PetasosParcel detailed model | **ON HOLD**; do not imply acceptance through the IU companion |

## 9. Candidate ADD scope after acceptance

Only following full Solution Architect acceptance: use the existing Continuum and canonical Calliope identities, reconcile Topics and named metadata contexts, introduce connected changes supported by the adjudicated delta, and carry the original IU-01–IU-13 evidence and qualifications forward. Reconcile Themis, Mneme/Mnemosyne, Petasos, Ponos and Artemis interfaces where actually affected. Do not manufacture new capabilities, IU variations or duplicate metadata classes; do not regard this draft as authorisation to alter existing immutable published releases.

## 10. Solution Architect disposition

**Pending review.** This draft is intentionally a proposed revision/companion to the **complete accepted** Information Unit Framework DRP. The accepted source remains unchanged. The **OM-06–OM-10 conceptual metadata allocations are already approved** and incorporated accurately into this *review draft*. Remaining questions concern exact canonical/logical model reconciliation, the Version wording versus accepted older IU §9/§16, `Origin`, coordinate evolution, mask field inventory and interface projections. This document has **not** been signed off in full, and no Codex ADD or Java implementation is authorised by its preparation.
