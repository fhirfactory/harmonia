<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — Information Unit Framework (Consolidated Reconciliation)

**Standing:** Final 002 — Solution Architect-approved conceptual reconciliation; pending governed incorporation into the Architecture Continuum  
**Baseline:** Accepted Information Unit Framework (IU-01–IU-13), accepted Continuum 1.1.24 and Mneme–Mnemosyne Interaction 1.1.24  
**Date:** 15 November 2026  
**Change character:** Accepted architectural semantic reconciliation and bounded incorporation input; not an implementation schema

## 0. Authority and scope

The definitive construct remains **`InformationUnit`**, not `ManagedIU`. Historical use of `ManagedIU` in dialogue meant “an InformationUnit being managed” and does not authorise a new type. `BO-InformationUnit` and `model.core.information.InformationUnit` are existing conceptual/logical views of the same governed information unit, not two entities.

This consolidated reconciliation is to be read **together with the complete** `enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Final.001.md`; **every IU-01–IU-13 decision and all accepted contracts in that source remain effective unless a specific change is expressly accepted**. In particular, this draft does not reprint or delete the source's full Capability/Requirement mapping, contract conformance register, retention/disposition requirements, search-result contract, audit/provenance distinctions, and other source sections. It is a consolidated *proposed amendment*, **not a claim that an accepted source has already been changed**.

**Related decisions:** `DRP-Topic-and-TopicMask.draft.001.md` was approved by the Solution Architect and is intended to become `DRP-Topic-and-TopicMask.Final.001.000.md` in `offline-design`; verify the actual repository filename rather than presuming placement. `DRP-OperationalMetadata-and-OperationalMetadataMask.draft.002.md` received explicit Solution Architect approval of **OM-06–OM-10**, supplementing OM-01–OM-05; verify its final repository path and status independently before incorporation. `DRP-PetasosParcel-and-PetasosParcelMask.draft.001.md` remains **on hold** pending further conceptual design.

**Accepted reconciliation from previous drafts:** the former *candidate* remapping of IU management and middleware lifecycle concerns is now **approved at conceptual level** and captured as a detailed OM-R01–OM-R16 crosswalk below. This is **not** a retrospective claim that the original four-context IU logical model or its interfaces have already been migrated. The accepted Mneme–Mnemosyne five-contract interaction, Mneme Cache Architecture, and all IU-01–IU-13 invariants remain controlling.

## 1. InformationUnit — conceptual subject and structure

An InformationUnit is Harmonia's logically **atomic, individually governed unit of information**, irrespective of implementation language, physical storage mechanism or conveyance. It is not a Java object, message, parcel, FHIR Resource, database row, cache copy, transformation engine or universal workflow instruction.

```text
InformationUnit
├── Identity
│   ├── id                           : UUID
│   └── name                         : Topic
├── version                          : String
├── InformationMetadata              : [conceptual information-related metadata]
│   ├── InformationCoordinates       : [four stable location/ownership references]
│   ├── InformationItinerary         : [participation/path of this information]
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

This is **conceptual**: `InformationMetadata`, `InformationItinerary`, `Origin` and `InformationLifecycle` are conceptual groupings whose exact logical representation remains subject to reconciliation. It is **not** a directive to create new Java fields/classes, rename currently accepted `Metadata` wholesale, or silently erase the present four metadata contexts. The predecessor accepted logical Class decomposes `Metadata` into `ManagementContext`, `InformationCoordinates`, `SecurityContext`, `LifecycleContext`, with optional relationships. **Approved OM-06–OM-10 establish the target conceptual allocation; the pre-incorporation logical state remains as published until Codex applies a governed change.** See §§3–4.

## 2. Identity and distinct revisions

### 2.1 Identity

`Identity.id : UUID` is the unique governed IU identifier. `Identity.name : Topic` is its named topic/classification, conceptually derived from its UUID under the accepted naming pattern `harmonia.mneme.iu.<hex-string-of-compressed-uuid>`. The Topic's **own** UUID is distinct from the IU UUID. Never conflate those or treat a topic name as authoritative IU identity.

### 2.2 Version semantics — accepted conceptual separation and source reconciliation

The Solution Architect's discussion and OM-06–OM-10 approval clarified distinct roles:

| Value | Intended meaning | Status |
|---|---|---|
| `InformationUnit.version : String` | **Authoritative persisted IU revision** established/returned by Mnemosyne; analogous in role to a FHIR `meta.versionId`, but not restricted to FHIR | Agreed recent conceptual direction; location/contract change requires reconciliation |
| Mneme operational revision (`ManagementContext.version` in the accepted IU model) | Active-information/cache/concurrency revision used by Mneme | Accepted semantic distinction; **approved conceptual relocation** to OperationalMetadata under OM-06 |
| `ContentMetadata.structureVersion` | Version of the Content Structure/model/profile | Accepted unchanged |
| Content-native version, **where supplied** | Version under the Content's own native information model | Accepted unchanged; no fabricated native version |
| Active Generation | Identity of an active cache manifestation, not a revision of information | Accepted unchanged; **approved conceptual relocation** under OM-07 |

**Source wording requiring faithful incorporation reconciliation (not an instruction to reopen approved semantics):** the accepted 1.1.22 IU Framework (§§9, 16) states that native Content instance version resides in Content and documents three version concepts. The later clarification describes a **Mnemosyne-governed authoritative persisted IU revision at `InformationUnit.version`**, in addition to Mneme operational revision, and analogises it to `Patient.meta.versionId`. The Solution Architect explicitly clarified that this paraphrases intended distinctions rather than introducing a gratuitous duplicate; nevertheless **the written accepted baseline must be reconciled carefully** so the authoritative persisted revision and native payload version are not silently conflated or made duplicative. Specifically, preserve the accepted Mneme–Mnemosyne rule that GovernedWriter may return the incremented authoritative IU version and authoritative read cannot expose pre-durable versions. Do not infer any implementation counter, native payload mutation or guaranteed equivalence to FHIR meta.versionId without an explicit contract.

### 2.3 Container and Content identities

Do **not** introduce mandatory `Container.id` or `Content.id` UUIDs. Where a represented object or external content has a native identity or persistent reference, preserve it in native Content or applicable ContentMetadata / ContainerMetadata under the corresponding contract. This does not create nested independently governed IUs.

## 3. Information-related metadata (InformationMetadata)

`InformationMetadata` is the **conceptual peer** of `PetasosParcelMetadata` and `OperationalMetadata`, not necessarily a mandatory new wrapper around the current IU `Metadata` Class. Its concerns are:

| Concern | Semantic responsibility | Relationship to current accepted IU |
|---|---|---|
| InformationCoordinates | Where information is stored/resolved, logical owner, gateway, endpoint/location | **Unchanged** four named Topics: PersistenceGateway, GatewayEndpoint, InformationLocation, InformationOwner |
| InformationItinerary | Participating components and actual/intended information progression | **Approved as a distinct conceptual concern**; detailed structure deferred |
| Origin | Where the **information** came from | Distinct from Petasos parcel source; precise class/field and provenance relation not final |
| Security | IU-required compartment, label, action/other criteria | Corresponds to current IU-side SecurityContext; separate from Themis Passport permissions |
| Information-related State | State of governed information *where distinct from operational execution state* | Must reconcile with current LifecycleContext; not a universal native business state machine |
| Relationships | Explicit relatesTo/partOf/basedOn-like references provided by creator/updater | Preserve optionality, local validation, no mandatory remote validation, no automatic reciprocal relations |

**InformationCoordinates:** the four accepted Topic references **remain unchanged**:

```text
InformationCoordinates
├── PersistenceGateway  : TopicID
├── GatewayEndpoint     : TopicID
├── InformationLocation : TopicID
└── InformationOwner    : TopicID
```

They answer where/how information is logically located, resolved and owned. They neither prescribe a movement path nor grant information access. The prior discussion of a seven-part coordinate replacement is **resolved by separating itinerary semantics**, not by introducing seven alternative coordinates. Do not equate `PersistenceGateway` universally with `Origin` or `TargetExitComponent`.

### 3.1 InformationItinerary — a distinct information concern

**Approved conceptual distinction:** `InformationItinerary` is an adjunct to information progression/Journey and provenance, describing components participating in the information's actual or intended passage. It does not replace `InformationCoordinates`, a business Journey, a provenance evidence record, or an individual Petasos handoff. Its candidate conceptual vocabulary is:

```text
InformationItinerary
├── SourceEntryComponent : Topic
├── CreationComponent    : Topic
├── PreviousComponent    : Topic
├── NextComponent        : Topic
├── TargetExitComponent  : Topic
└── ...                  : other participating/path characteristics
```

These names are **illustrative conceptual participants**, **not** an authorised mandatory list of Java fields or ArchiMate properties. Historical participation and prospective intended routing must be distinguishable; neither the complete path nor every component is guaranteed to be known. InformationItinerary describes an information journey, not necessarily every delivery attempt or a proven chain of custody. Provenance has independent evidence requirements.

**Approved common semantics with Petasos:** PetasosParcel will carry **its own complete instance** of InformationItinerary, appropriate to that parcel/handoff, rather than a mere pointer to a single mutable shared itinerary. 'Complete' means the parcel carries a self-contained itinerary structure, **not** that every possible itinerary value is populated. Earlier parcels' historical routing context must not be changed retroactively when later handoffs occur.

This **does not approve** the remaining PetasosParcel model, its publication/subscription mechanics, operational state, or parcel-specific reconciliation. The detailed PetasosParcel and Petasos framework DRP is explicitly **deferred until after Mneme/Mnemosyne reaches an operational 0.5 build-quality milestone**. There is no need to design the full itinerary update protocol or route-selection algorithm for this IU incorporation.

`InformationItineraryMask` may eventually provide common discrimination across IU- and parcel-carried itineraries. Its presence, field catalogue, cardinality, evaluation/absence semantics and implementation are **not** authorised by this IU DRP. The IU's information-oriented criteria may account for itinerary once its matching contract is governed.


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
│   ├── InformationItinerary
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
| **OM-R10 / OM-09** | Four InformationCoordinates | **Remain with InformationMetadata unchanged** | Logical placement/owner; independent of security |
| **IU-IT01** | Information movement/participating components | **InformationItinerary — separate InformationMetadata concern** | Do not create or overwrite coordinate dimensions; detailed fields remain conceptual |
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

Petasos queue/delivery/retry progress and Ponos/Ergon/Artemis execution state are also middleware-operational *candidates* associated with their own subjects. Their detailed structures remain governed separately; **PetasosParcel/Framework detailed design is deferred until after the Mneme/Mnemosyne operational 0.5 milestone**. The separate `OperationalMetadataMask` discriminates operational properties where governed, not native business state.

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
├── [future itinerary criteria]           -> InformationMetadata.InformationItinerary
├── information-security criteria         -> InformationMetadata.Security (where applicable)
├── container-type criterion              -> InformationRepresentation.ContainerMetadata.type
├── content-structure-type criterion      -> InformationRepresentation.ContentMetadata.type
└── [other governed IU characteristic criteria, if specified]
```

This is a **conceptual matching subject** rather than a settled class, field catalogue or permission model. **Middleware-operational state moved under OM-06–OM-08 is not an intrinsic IU criterion:** select it through `OperationalMetadataMask`, when appropriate, rather than silently reintroducing it through `InformationUnitMask`. An InformationUnitMask may contain/reference TopicMasks for Topic-valued fields; other field types require their own properly specified comparison semantics. Criteria referring to information-security requirements must not accidentally disclose restricted Content or turn a topic-selection match into Themis authorisation. Whether each criterion is mandatory, optional or repeatable is deferred.

A `SubscriptionStatement` is conceptually composed, rather than inherited, as; **this composition is not authorisation to implement or close the Petasos design**:

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
| IU-R10 | Four InformationCoordinates vs itinerary-style component dimensions | **RESOLVED conceptually:** retain the four existing coordinates unchanged; move participating/path concepts to distinct `InformationItinerary` |
| IU-R11 | `Origin` relation to provenance, itinerary and location | Distinct concerns agreed; allocation of overlapping origin facts and evidence representation remains a bounded detailed-design decision |
| IU-R12 | `InformationMetadata` vs accepted `Metadata` / four contexts | Conceptual grouping agreed; **OM-06–OM-10 approved**; canonical identity, compatibility projection and exact class allocation remain incorporation work |
| IU-R13 | Original IU-01–IU-13 mapping, full acceptance/conformance obligations | Preserved by reference to accepted complete DRP; no silent reopening |
| IU-R14 | Approved OM-09 IU identity/authority/security/representation safeguards | **APPROVED**; no migration of information properties into operational metadata |
| IU-R15 | Approved OM-10 governed information lifecycle versus middleware state | **APPROVED**; distinguish disposition from persistence handshaking |
| IU-R16 | OperationalMetadataMask and optional subscription operational criterion | Agreed concept; details governed by separate OM DRP; no operational criteria smuggled into IU mask |
| IU-R17 | PetasosParcel detailed model | **DEFERRED** until after operational Mneme/Mnemosyne 0.5 milestone; common itinerary structure/parcel-owned complete instance agreed only |
| IU-R18 | InformationItinerary | **APPROVED** as separate from four InformationCoordinates; adjunct to information progression/Journey and provenance; conceptual component vocabulary only |
| IU-R19 | Parcel-itinerary relationship | **APPROVED**: every relevant PetasosParcel carries its own complete itinerary instance/snapshot, with no assumption of complete field population or mutable shared reference |

## 9. Governed ADD incorporation scope

Following Solution Architect acceptance of this reconciliation, Codex may be commissioned for the separately governed ADD of its bounded semantics: use the existing Continuum and canonical Calliope identities, reconcile Topics and named metadata contexts, introduce connected changes supported by the adjudicated delta, and carry the original IU-01–IU-13 evidence and qualifications forward. Reconcile Themis and Mneme/Mnemosyne interfaces where actually affected. **Do not implement, redesign or incorporate the deferred PetasosParcel/Framework beyond preserving the approved conceptual itinerary boundary and necessary cross-references.** Ponos and Artemis remain reference-only unless a demonstrable IU dependency needs explicit architecture review. Do not manufacture new capabilities, IU variations or duplicate metadata classes; do not regard this draft as authorisation to alter existing immutable published releases.

## 10. Solution Architect disposition and implementation gate

**Accepted conceptual disposition for Codex incorporation** based on Solution Architect acceptance of the IU reconciliation and explicit OM-06–OM-10, Topic/TopicMask and InformationItinerary discussions. This document is named **Final.002** to preserve the preceding accepted **Final.001** as an immutable predecessor, not to overwrite its provenance.

**Scope of acceptance:** four unchanged InformationCoordinates; new distinct InformationItinerary concept; Petasos parcels each owning a complete itinerary instance; approved operational metadata remapping; IU identity/version/metadata/representation boundaries and invariants; shared mask responsibility boundaries.

**Not a finding that every logical detail is resolved:** authoritative IU version versus historical Content-native version vocabulary must be semantically reconciled; Origin/itinerary/provenance evidential relationships, exact new Class/association allocations, mask operators/cardinalities, interchange formats and safe interface projections remain bounded implementation/ADD design questions. Codex must preserve accepted contracts and raise architectural conflicts for adjudication, not silently invent answers.

**Sequence / gate:** (1) incorporate approved IU and supporting Topic/OperationalMetadata architecture through governed ADD, including original source traceability; (2) return to the Mneme/Mnemosyne build aiming for **operational 0.5 quality**; (3) only then reopen the deferred PetasosParcel/Petasos framework design. There is **no** authorisation for Java implementation, framework deployment, historical release rewriting or Petasos functional completion from this DRP alone.


## Appendix A — Accepted IU-01–IU-13 succession and preservation record

This revision **supplements** the complete accepted `DRP-Information-Unit-Framework.Final.001.md` and does not shorten or supersede its normative conformance requirements. That predecessor is incorporated **by explicit governed reference** because it remains an independently accepted, immutable design record. The original sections on capability/Feature derivation, information security, provenance, audit, retention, persistence, search results, native models, transformation, Mneme/Mnemosyne interaction, implementation freedoms, and original decision evidence remain fully operative except for the **specifically approved semantic relocations** in §§2–4 and the resolution of IU-R10.

| Predecessor decision | Treatment in Final.002 |
|---|---|
| IU-01 IU identity, business/logical views | PRESERVED — same `BO-InformationUnit`/logical `InformationUnit` |
| IU-02 atomicity and one InformationRepresentation | PRESERVED, including new IU per governed transformation |
| IU-03 four metadata contexts | RECONCILED — semantic operational allocation to peer OperationalMetadata; preserve contracts/projections while evolving canonical model |
| IU-04 four InformationCoordinates | PRESERVED **unchanged**; itinerary is separate |
| IU-05 IU SecurityContext and element-wise TopicMask | PRESERVED; approved standalone Topic/TopicMask DRP refines exact/qualifier behaviour |
| IU-06 IU UUID versus IU Topic | PRESERVED — never equate IU identity and Topic UUID |
| IU-07 optional relationships, local/remote validation | PRESERVED |
| IU-08 Feature-specific operations/masks | PRESERVED — no invented universal IU method catalogue |
| IU-09 operational version and Active Generation | RECONCILED — now conceptually associated through OperationalMetadata, with earlier operational meaning and visibility unchanged; other versions protected |
| IU-10 feature/capability reconciliation | PRESERVED — reuse existing canonical capability hierarchy |
| IU-11 outcomes/persistence boundary | PRESERVED — accepted five-contract and authoritative durability requirements control |
| IU-12 audit/provenance/responsibility boundaries | PRESERVED — specialized governance not absorbed by IU or OperationalMetadata |
| IU-13 branch/historical adjudication evidence | PRESERVED as source provenance; do not reinterpret as current branch state |

## Appendix B — Bounded Codex incorporation handoff

**Inputs**: this accepted IU Final.002; accepted predecessor IU Final.001; approved Topic/TopicMask Final.001 (verify exact path); approved OperationalMetadata design (verify final status and approved OM-06–OM-10); accepted 1.1.24 Mneme–Mnemosyne Interaction/Cache DRPs; canonical Continuum and current HCRL governance.

**Requested operation**: first inspect canonical elements and relationships; reconcile semantic deltas against pre-existing Class, Topic, Information Object, Data Object, Feature and Service identities; propose an evidence-backed, bounded ADD; preserve required invariants, all accepted prior qualifiers and source identifiers; obtain Solution Architect review at applicable gates. Do not casually map one conceptual box to one new ArchiMate/Java Class.

**Special checks**: four InformationCoordinates unchanged; InformationItinerary separate, with optional/actual-intended semantics unprescribed; parcel-owned complete itinerary instance acknowledged **only as conceptual cross-reference**; Mneme operational revision / cache generations and Mneme–Mnemosyne handshaking conceptually reallocated to OperationalMetadata, preserving externally required projections; Mnemosyne authoritative IU revision remains on IU; Topic Version remains absent; no security-policy conflation; no auto-application to Petasos.

**Explicit exclusion**: PetasosParcel/Framework modelling, implementation, queue/retry protocol or route selection; separate Java implementation, replacement of previously published releases and unrelated architecture redesign. Petasos detailed work resumes **after operational Mneme/Mnemosyne 0.5 milestone**.
