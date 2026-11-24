<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — Information Unit Framework (Conceptual Refinement)

**Standing:** Revised draft 002-002 for Solution Architect review; **not** an accepted replacement for `DRP-Information-Unit-Framework.Final.001.md`  
**Baseline:** Accepted Information Unit Framework (IU-01–IU-13), accepted Continuum 1.1.24 and Mneme–Mnemosyne Interaction 1.1.24  
**Date:** 15 November 2026  
**Change character:** Architectural conceptual refinement and explicit reconciliation, not an implementation schema

## 0. Authority and scope

The definitive construct remains **`InformationUnit`**, not `ManagedIU`. Historical use of `ManagedIU` in dialogue meant “an InformationUnit being managed” and does not authorise a new type. `BO-InformationUnit` and `model.core.information.InformationUnit` are existing conceptual/logical views of the same governed information unit, not two entities.

This draft should be reviewed **together with the complete** `enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md`; **every IU-01–IU-13 decision and all accepted contracts in that source remain effective unless a specific change is expressly accepted**. In particular, this draft does not reprint or delete the source's full Capability/Requirement mapping, contract conformance register, retention/disposition requirements, search-result contract, audit/provenance distinctions, and other source sections. It is a consolidated *proposed amendment*, **not a claim that an accepted source has already been changed**.

Adjacent drafts: `DRP-Topic-and-TopicMask.draft.001.md`, `DRP-PetasosParcel-and-PetasosParcelMask.draft.001.md`, `DRP-OperationalMetadata-and-OperationalMetadataMask.draft.001.md`. This proposed IU delta must be reconciled with the accepted Mneme–Mnemosyne five-contract interface, Mneme Cache Architecture, and IU identity/naming model.

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
│   ├── State                        : [information/lifecycle meaning to reconcile]
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

This is **conceptual**: `InformationMetadata`, `Origin` and `State` are candidate conceptual groupings. It is **not** a directive to create new Java fields/classes, rename currently accepted `Metadata` wholesale, or silently erase the present four metadata contexts. The accepted Class currently decomposes `Metadata` into `ManagementContext`, `InformationCoordinates`, `SecurityContext`, `LifecycleContext`, with optional relationships. See §§3–4 below.

## 2. Identity and distinct revisions

### 2.1 Identity

`Identity.id : UUID` is the unique governed IU identifier. `Identity.name : Topic` is its named topic/classification, conceptually derived from its UUID under the accepted naming pattern `harmonia.mneme.iu.<hex-string-of-compressed-uuid>`. The Topic's **own** UUID is distinct from the IU UUID. Never conflate those or treat a topic name as authoritative IU identity.

### 2.2 Version semantics — proposed reconciliation

The recent Solution Architect discussion clarified two distinct roles:

| Value | Intended meaning | Status |
|---|---|---|
| `InformationUnit.version : String` | **Authoritative persisted IU revision** established/returned by Mnemosyne; analogous in role to a FHIR `meta.versionId`, but not restricted to FHIR | Agreed recent conceptual direction; location/contract change requires reconciliation |
| Mneme operational revision (`ManagementContext.version` in the accepted IU model) | Active-information/cache/concurrency revision used by Mneme | Accepted semantic distinction; candidate relocation to OperationalMetadata, **not yet authorised** |
| `ContentMetadata.structureVersion` | Version of the Content Structure/model/profile | Accepted unchanged |
| Content-native version, **where supplied** | Version under the Content's own native information model | Accepted unchanged; no fabricated native version |
| Active Generation | Identity of an active cache manifestation, not a revision of information | Accepted unchanged; candidate operational relocation |

**Explicit conflict to adjudicate:** the accepted 1.1.22 IU Framework (§§9, 16) states that native Content instance version resides in Content and documents three version concepts. The later clarification describes a **Mnemosyne-governed authoritative persisted IU revision at `InformationUnit.version`**, in addition to Mneme operational revision, and analogises it to `Patient.meta.versionId`. The Solution Architect has said this is paraphrasing existing intended distinctions, not inventing a gratuitous duplicate; nevertheless **the written accepted baseline must be reconciled carefully** so the authoritative persisted revision and native payload version are not silently conflated or made duplicative. Specifically, preserve the accepted Mneme–Mnemosyne rule that GovernedWriter may return the incremented authoritative IU version and authoritative read cannot expose pre-durable versions. Do not infer any implementation counter, native payload mutation or guaranteed equivalence to FHIR meta.versionId without an explicit contract.

### 2.3 Container and Content identities

Do **not** introduce mandatory `Container.id` or `Content.id` UUIDs. Where a represented object or external content has a native identity or persistent reference, preserve it in native Content or applicable ContentMetadata / ContainerMetadata under the corresponding contract. This does not create nested independently governed IUs.

## 3. Information-related metadata (InformationMetadata)

`InformationMetadata` is the **conceptual peer** of `PetasosParcelMetadata` and `OperationalMetadata`, not necessarily a mandatory new wrapper around the current IU `Metadata` Class. Candidate concerns are:

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

Some properties **currently inside** `InformationUnit.Metadata.ManagementContext` and `InformationUnit.Metadata.LifecycleContext` are candidates to be **associated with** `OperationalMetadata` instead. They include Mneme operational revision, active cache generation, cache references, and Mneme–Mnemosyne handshaking and persistence coordination. This is a **candidate redistribution, not an approved migration**. A move must preserve historical compatibility, authoritative outcomes, recovery durability, concurrency/staleness detection, observability and access/authorisation; different operational properties may be transient or durable. The accepted IU currently has four contexts; do not delete them before a specific adjudication.

Petasos queue/delivery/retry progress and Ponos/Ergon/Artemis execution state are likewise middleware-operational concerns but may be associated with their **own subjects**, without assuming OperationalMetadata is owned by InformationUnit, Petasos, or a central framework service.

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

This is a **conceptual matching subject** rather than a settled class, field catalogue or permission model. An InformationUnitMask may contain/reference TopicMasks for Topic-valued fields; other field types require their own properly specified comparison semantics. Criteria referring to information-security requirements must not accidentally disclose restricted Content or turn a topic-selection match into Themis authorisation. Whether each criterion is mandatory, optional or repeatable is deferred.

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
| IU-R09 | Operational-data candidate relocation | **Not yet approved**; property-by-property impact review required |
| IU-R10 | Four accepted InformationCoordinates vs seven proposed | **Not yet resolved**; do not substitute seven automatically |
| IU-R11 | `Origin` relation to provenance and location | Conceptual distinction agreed; concrete representation open |
| IU-R12 | `InformationMetadata` vs accepted `Metadata` / 4 contexts | Conceptual grouping agreed; canonical model mapping open |
| IU-R13 | Original IU-01–IU-13 mapping, full acceptance/conformance obligations | Preserved by reference to accepted complete DRP; no silent reopening |

## 9. Candidate ADD scope after acceptance

Only following full Solution Architect acceptance: use the existing Continuum and canonical Calliope identities, reconcile Topics and named metadata contexts, introduce connected changes supported by the adjudicated delta, and carry the original IU-01–IU-13 evidence and qualifications forward. Reconcile Themis, Mneme/Mnemosyne, Petasos, Ponos and Artemis interfaces where actually affected. Do not manufacture new capabilities, IU variations or duplicate metadata classes; do not regard this draft as authorisation to alter existing immutable published releases.

## 10. Solution Architect disposition

**Pending review.** This draft is intentionally a proposed revision/companion to the **complete accepted** Information Unit Framework DRP. The accepted source remains unchanged. Proposed conceptual grouping, candidate metadata relocation and the precise version reconciliation must be reviewed and adjudicated explicitly before Codex is instructed to perform ADD.
