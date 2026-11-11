<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — Core Information Unit Framework

**DRP identity:** `DRP-Information-Unit-Framework-001`  
**Document standing:** Consolidated DRP for Solution Architect review; IU-01–IU-13 adjudications incorporated; **not** a published Architecture Continuum change  
**Primary baseline:** `DRP-Information-Unit-Framework.draft.003.md` (27 October 2026)  
**Prepared for:** Solution Architect review and architectural adjudication  
**Repository:** `fhirfactory/harmonia`  
**Working branch:** `Mneme-and-Mnemosyne-Build` (verified remote HEAD `cb21ca0c5291f726f4babc7e59dabeba1a6c302c`)  
**Initial source comparison:** `Capability-Map-Consolidation` at the same commit  
**Date:** 11 November 2026

## 0. Source authority, scope and interpretation

This document consolidates the existing Information Unit DRP with subsequently available governed Information Unit architecture and relevant adjacent design proposals. Its object is the **Information Unit as Harmonia's fundamental governed information building block**, not an independent design of Mneme, Mnemosyne, Themis or an implementation framework.

The authoritative source for accepted architecture is the Markdown Architecture Continuum under `enterprise-architecture/continuum/`. The existing DRP is the baseline for consolidation; other DRPs are design evidence and may contain proposals rather than accepted decisions. The retained architectural propositions are presented as **DRP design assertions** unless independently confirmed as accepted in the Continuum. `SHALL` expresses a constraint proposed or inherited in this DRP; it does not claim every sentence has already been incorporated into the Continuum.

**Source qualification.** The remote `Mneme-and-Mnemosyne-Build` branch was subsequently verified at commit `cb21ca0c5291f726f4babc7e59dabeba1a6c302c`, the same commit used during the earlier `Capability-Map-Consolidation` inspection. This resolves IU-13's branch-access qualification. The Continuum is large: the explicitly inspected canonical anchors and adjacent DRPs are documented below; a complete element-by-element Continuum reconciliation, including all current capability and Requirement IDs, remains a bounded incorporation obligation. No repository modification, ADD, commit or push has been performed.

### 0.1 Canonical anchors verified in the accessible Continuum

| Construct | Verified source identity | Established description / qualification |
|---|---|---|
| Business Information Unit | `business/business-object/10284034c8534b639284386b064f2c54.md` — `BO-InformationUnit` | Accepted conceptual governed management boundary, not a storage or transport schema |
| Logical InformationUnit Class | `application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md` — `model.core.information.InformationUnit` | Accepted bounded logical class (HCRL 1.1.15-001); governs the association of a Governed Representation with its Management Context |
| Search Result Set | `business/business-object/b683ae87c72a4c16aba948a4b8cb0998.md` — `BO-SearchResultSet` | Adjudicated managed metadata collection; not the full contents of every matched Information Unit |

The first two anchors are **distinct conceptual and logical representations of the same Information Unit**, as accepted under IU-01. They are not competing definitions or independently governed entities. The logical Class's 'Governed Representation' and 'Management Context' wording is interpreted through the adjudicated IU composition below, without inventing a second independently governed management object.

### 0.2 Related design sources examined

- `DRP-Information-Unit-Framework.draft.003.md` — primary source, including its 25 numbered sections and design-provenance appendix.
- `DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md` — Topic references, ownership/location coordinates, hierarchy and Themis boundary.
- `DRP-Themis-Policy-Passport-Enforcement.draft.001.md` — subject-owned required criteria, Passport and PEP separation.
- `DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md` — governed reader/writer/search and exchanged Information Units.
- `DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md` and `DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md` — authoritative persistence and provider representation freedom.
- `DRP-Mneme-Security.draft.002.md` — access control and security-context durability boundary.

Other related DRPs, including the Pragma/Pragmaton, audit, resilience, sharding and logging/provenance drafts, remain relevant neighbours; the consolidation does not assert full line-by-line reconciliation of those documents.

## 1. Feature realisation and architectural position

Harmonia shall manage information through independently identifiable, governed **Information Units (IUs)**. The IU is the fundamental managed information element across information management, persistence, integration, workflow, routing, discovery, publication/subscription, information protection, audit and provenance. The IU is not owned by Mneme, Mnemosyne, Themis, an integration engine, a database or a transport.

The IU is foundational information architecture and not exclusively the property of a single Enterprise Capability. Applicable Features and Requirements *use* IU semantics; their ownership of specialised behaviour does not redefine IU identity or composition.

### 1.1 Capability and Feature context

The October DRP proposed an Enterprise Capability **Information Content and State Management**, with the following Feature candidates:

1. Durable Information Persistence;
2. Information State Verification;
3. Information Content Identification and Protection;
4. Information Discovery and Distribution;
5. Temporal Information Management;
6. Information Provenance and Accountability;
7. Information Lifecycle Management.

These names are retained as the **historical proposal inventory**, not silently promoted to current canonical Feature identities. **IU-10 resolves the reconciliation method:** reuse existing canonical Features and Requirements wherever semantically equivalent; introduce no historical Feature or RequirementGroup automatically. The 1.1.20 Capability consolidation introduced a much larger and reorganised four-tier capability catalogue, so current CT1/CT2/CT3/Feature ancestry and equivalence must be established using canonical identity and semantic definition, not matching labels or the obsolete `DataManagement.InformationUnits.*` paths found in neighbouring DRPs. The IU itself remains foundational even if related Features are allocated across several Enterprise Capabilities.

### 1.2 Requirement context

The October candidate RequirementGroups were **GovernedInformationState** and **InformationPersistence**. Their original requirements remain part of the DRP's design coverage:

- technology-neutral governed IU identity;
- separation of IU identity from represented-information and represented-entity identities;
- one governed Information Representation per IU;
- preservation of Unit, Container, Content Metadata and Content distinctions;
- filtering and enforcement without exposing internal metadata layout;
- independent Unit Structure, Content Structure and Content Format;
- Version distinct from Active Generation;
- provenance, authority, lifecycle, temporal and IU relationship semantics;
- Content-aware and Content-neutral service participation; and
- separately governed representations as related IUs.

These are not asserted here to have acquired newly verified Requirement IDs. Reconcile with the accepted NFR and Motivation architecture, reusing existing RequirementGroups and Requirements where applicable.

### 1.3 Motivation and axiom context

Information Unit concerns span authorisation, audit, authoritative and active information, information ownership, custody, preservation, resilience, integration, distributed operation and execution. Existing Principles, Axioms and Requirements are the reference authority. A historical DRP citation such as `AX-05` remains a cross-reference requiring canonical confirmation, not independent evidence of a present-day axiom meaning.

## 2. Core proposal — an atomic Information Unit

The **Information Unit is Harmonia's indivisible unit of governed information**. Its independence is logical and governed: it is not a promise of a single physical write, row, object, file or network operation. Persistence may be decomposed or referenced so long as the accepted Information Unit's integrity and consistency are preserved.

The accepted conceptual structure is:

```text
InformationUnit
├── Identity
│   ├── UUID
│   └── Topic : harmonia.mneme.iu.<hex-string-of-compressed-uuid>
├── Metadata
│   ├── ManagementContext
│   ├── InformationCoordinates
│   ├── SecurityContext
│   ├── LifecycleContext
│   └── Relationships [0..*] (optional creator-supplied references)
└── InformationRepresentation [1..1]
    ├── ContainerMetadata
    ├── ContentMetadata
    │   ├── ContentStructureTopic
    │   └── ContentStructureVersion
    └── Content
```

This replaces the old `Identity / Metadata / Content / Context` partition, and it reconciles the accepted logical Class's association between Governed Representation and Management Context without creating a separate governance entity. The shown field labels are semantic names unless independently verified as canonical Class attributes.

**IU-02:** an IU contains **exactly one** governed InformationRepresentation. It SHALL NOT maintain alternative governed representations, `otherRepresentations()` collections, synchronisation dependencies or reverse transformation rules. A consumer requiring a differently structured or formatted governed representation transforms the information and creates a **new IU**, with appropriate provenance and any expressly supplied relationships. The transforming consumer owns transformation fidelity and any attempted reverse transformation; an update to a source IU must still obey governed write, concurrency and authorisation rules.

Mnemosyne/PersistenceServices may persist an IU using a provider-native physical form but MUST fulfil the IU's governed representation contract when reconstructing or delivering it. Different physical forms are **not** alternate governed representations within an IU. An accessor SHALL NOT become a universal business-logic transformation engine.

## 3. IU identity, atomicity and management boundary

An IU has its own persistent unique identity and a corresponding **Topic**, with an agreed canonical naming pattern:

```text
harmonia.mneme.iu.<hex-string-of-compressed-uuid>
```

The hex compression is a deterministic and reversible representation of the UUID; its exact encoding remains a logical/implementation contract detail. The namespace does not transfer ownership of the foundational IU model to Mneme.

**IU-01:** `BO-InformationUnit` and `model.core.information.InformationUnit` are respectively the accepted conceptual Business Object and logical Class for the **same** IU. They do not imply two entities.

**IU-06:** the IU UUID and its own Topic have related but distinct roles. The UUID identifies the IU; the IU Topic provides its hierarchical canonical name. Neither is interchangeable with Topics in InformationCoordinates, ContentMetadata or SecurityContext. IU identity also remains distinct from native Content identity/version, represented-entity identity, persistence-provider IDs, transport identifiers and correlation identifiers. The older `TopicID id` example must be reconciled with this accepted UUID-plus-Topic contract rather than adopted literally.

The IU is not a FHIR Resource, HTTP payload, message, Java object, database row, cache manifestation or physical persistence fragment. IU atomicity concerns its **governed identity, singular representation and accepted-state consistency**; it does not prohibit pass-by-reference, physical decomposition or multiple cache instances.

## 4. Identity and Topic designation

The IU's unique UUID remains independent of its Topic's naming and registration mechanics. The IU Topic's canonical name is derived from the UUID and has the fixed conceptual prefix `harmonia.mneme.iu`.

Topics identify governed named concepts through their canonical naming and containment system. They may describe a gateway, endpoint, location, owner, content structure, compartment, action or another named entity. A Topic is **not** the implementation or configuration of its referent.

The IU's own Topic is different from every coordinate or criterion Topic: neither location, ownership nor compartment is itself the IU identity. A TopicID identifies a Topic and may appear as a referenced value in IU metadata, but the mere use of TopicIDs does not turn every IU attribute into its own independently governed IU.

## 5. Information Unit Metadata and its four contexts

The accepted `InformationUnit.Metadata` decomposition is:

```text
Metadata
├── ManagementContext
├── InformationCoordinates
├── SecurityContext
└── LifecycleContext
```

These contexts are distinct **concerns within one IU**, not independently governed nested IUs. Their contents may be materialised or referenced through suitable contracts. InformationUnitContext and the accepted Class's historical Management Context language are reconciled against this explicit decomposition; they do not authorise a second undifferentiated context store.

### 5.1 ManagementContext — Mneme operational working information

`InformationUnit.Metadata.ManagementContext` provides a logical home for operational information maintained principally by **Mneme** and exposed to appropriate clients. It is for data that does **not** belong to the IU's identity or represented Content.

Its content may be embedded; be only a reference to a Mneme cache table; or be a hybrid of direct attributes and references. Operational properties may be expressly **transient**; presence inside ManagementContext does not automatically create a durable persistence obligation. A referenced transient entry may cease to be resolvable after cache loss, migration or restart and must never be mistaken for authoritative durable information. Visibility to a client does not automatically grant that client write authority.

Mneme's **operational Version**, used for cache conflict detection, optimistic concurrency and stale-update handling, belongs here. An **Active Generation** identifies an individual active manifestation of an IU in a particular distributed Mneme cache environment. Multiple generations may express the same operational Version. A generation is **not** another IU, another governed Content version or another independently governed representation. Neither a globally coordinated generation counter nor a specific cache-coherence algorithm is prescribed.

### 5.2 InformationCoordinates — operational resolution and ownership

`InformationCoordinates` comprises **four semantically distinct named Topic references**:

```text
InformationCoordinates
├── PersistenceGateway  : TopicID
├── GatewayEndpoint     : TopicID
├── InformationLocation : TopicID
└── InformationOwner    : TopicID
```

- **PersistenceGateway** identifies/describes a named PersistenceService gateway or capability appropriate to Mnemosyne.
- **GatewayEndpoint** identifies/describes a named logical endpoint or its parameter context.
- **InformationLocation** identifies/describes the logical location or node where the information lives.
- **InformationOwner** identifies/describes the operational owner, potentially an organisation or other operational entity.

These are Topic names/identities within their governed containment hierarchies, **not** inline gateway implementations, URLs, credentials, connection parameters or physical storage addresses. Mnemosyne interprets the coordinates to select/resolve a PersistenceService and access context, including potentially expensive third-party GET, UPDATE and other operations. Physical configuration may change without changing the IU's coordinate Topic reference. An IU may carry operationally useful references without encoding deployment topology as its identity.

**InformationCoordinates is 100% independent of SecurityContext.** Operational ownership/location does not itself establish a compartment, clearance, right to read or authorisation. Any security policy separately consulting a location value does not make the location coordinate itself a security rule. Precise per-coordinate presence and population rules belong to the applicable Mnemosyne and IU lifecycle contracts, not an invented universal mandatory-field rule here.

### 5.3 SecurityContext — IU-required criteria

`SecurityContext` expresses criteria and compartments applicable to this IU using **explicit Topic references or TopicMasks**. These are IU-side requirements, not a user's permissions and not Themis's Passport. The matching mechanism is shared across Harmonia, not bespoke to security; see §§12–13.

### 5.4 LifecycleContext — operational handshaking

`LifecycleContext` provides information needed for **Mneme–Mnemosyne lifecycle and persistence coordination**, including distinguishing request/acceptance/commit/completion as required by the appropriate governed contracts. It is not itself a universal state machine, nor does it own the semantic lifecycle of Content. Its operational fields may have different preservation requirements; specific state/outcome vocabulary belongs to the Mnemosyne interaction DRPs and governed requirements.

## 6. InformationRepresentation — one governed Content representation

Each IU contains **one and only one** `InformationRepresentation`, comprising `ContainerMetadata`, `ContentMetadata` and `Content`.

The representation is the governed presentation of the IU's Content, not an automatic registry of alternative transformations, encodings or serialisations. Transforming HL7 v2 ADT content into a FHIR Patient, converting JSON into XML for a distinct governed use, or otherwise creating a different governed expression is the transforming client's business logic. When the result is to be governed, it receives its **own IU identity and single representation**, with appropriate provenance.

A derived IU does not force its source IU to keep a backlink, update its Content, synchronise revisions or maintain a dependency graph. Transformations may be lossy, irreversible or only conditionally valid. Any attempted reverse update must be explicit and governed; the IU accessor does not promise reversibility.

Mnemosyne's physical representation—relational tables, native FHIR storage, document store, referenced binary or gateway state—may be different from the one represented in the IU contract. That difference is a **persistence implementation concern**, not another IU InformationRepresentation.

## 7. Container Metadata

Container Metadata describes the container, envelope, assembly or carriage associated with the IU's representation. Potential dimensions include container type, source/destination, author/recipient, assembly context, interaction mechanism, envelope structure and related descriptive characteristics.

The same Content delivered through a FHIR Bundle, document, event, broker message or HTTP operation may convey different native context and integration significance. Relevant native envelope semantics must be preserved when governed by the information-management contract; they are neither invariably disposable transport state nor automatically identical to IU Metadata.

Content-aware integration functionality may inspect and transform Container Metadata under its own Feature responsibilities. This does not transfer integration responsibility to Mnemosyne.

## 8. ContentMetadata — Content Structure Topic and version

`InformationRepresentation.ContentMetadata` identifies and describes the Content independently of the Content's own data values. It includes an applicable **Topic describing the Content Structure and a Content Structure Version**. Other relevant characteristics may include content type, structure/profile/model, concrete format or encoding, language, size, hash/digest and content references where governed by the relevant contract.

The distinction remains:

```text
Unit Structure ≠ Content Structure ≠ Content Format
```

- **Unit Structure** characterises the governed IU form.
- **Content Structure** is identified through its Topic and **structure version** (for example a FHIR release/profile variant).
- **Content Format** describes the concrete encoding/serialisation.

`ContentMetadata.Version` means the **Content Structure Version**. It does not represent Mneme's cache version and it does not duplicate a payload-native instance version. The two latter cases belong elsewhere (§§5.1 and 9).

## 9. Content — native information and native versioning

`InformationRepresentation.Content` is the actual represented information: structured, textual, binary, composite, opaque, externally defined, inline or referenced. The IU is FHIR-independent, although appropriate Content-aware services may process FHIR, HL7 v2, proprietary or other native models.

**The Content's instance version, if any, belongs inside the Content itself.** A FHIR Resource may convey `meta.versionId`; another payload may have a different mechanism or **no native instance version at all**. The IU framework SHALL NOT fabricate or mandate payload-native version support. Mneme can still version its management of that IU independently. `ContentMetadata` records the version of the Content *structure*, not the instance.

Content-native integrity and preservation requirements, including byte fidelity where it is a governed property, remain applicable. The IU's atomic logical boundary permits physically separate large binaries and pass-by-reference; it does not mandate large binary storage inside PostgreSQL or one relational row.

## 10. Encapsulation, Feature-facing operations and TopicMasks

The IU should expose its characteristics through stable, governed interfaces rather than force every consumer to traverse internal structures. Authorised domain-specific functions may access Content or metadata as required by their Features.

**IU-08 is resolved:** do not invent a mandatory universal catalogue of InformationUnit `get()`/`test()` methods, `SecurityMask` classes or Feature-specific Mask wrappers merely to complete this DRP. Derive operational contracts from their owning Features.

**TopicMask is a general-purpose Harmonia construct** usable by publication/subscription, routing, discovery, security and other Topic-discriminating Features. Its definition and matching semantics belong in the common Topic/Mask model; IU SecurityContext is one consumer, not its owner.

## 11. Filtering, publication/subscription, discovery and routing

IU management, pub/sub, discovery, routing and security may consult IU Metadata, ContainerMetadata and ContentMetadata as their respective contracts require, without flattening the different metadata domains or turning the IU into a transformation engine.

TopicMask matching returns a discrimination/match result. It does **not** by itself grant access, require a publication, schedule delivery or select a storage provider. The relevant consuming Feature decides what the match means.

A **Search Result Set** remains the governed metadata collection of references to IU Versions, not an implicit disclosure of all matched Content. Search authorisation and subsequent individual IU read authorisation may differ. Its persistence in an Ergon context does not change the underlying IU identity.

## 12. Topics and element-wise TopicMask matching

A Topic is Harmonia's governed, hierarchically named identification/classification construct. It has a canonical identity and a **canonical name made up of ordered dot-separated elements**. Its meaning comes from governed naming, containment and the relevant hierarchy; it is not merely the external object it names.

**TopicMask semantics (accepted under IU-05):** a mask comprises **regular-expression patterns applied separately to the corresponding individual canonical-name elements**. It is **not** one whole-name regex and not a precomputed enumeration/set of TopicIDs. A candidate matches when the applicable individual element expressions match under governed syntax/anchoring rules.

Illustrative Topic:

```text
<BusinessDomain>.<StandardsBodyName>.InformationCompartment.<CompartmentName>
```

Illustrative mask intent:

```text
*.*.InformationCompartment.PersonalInformation*
```

The `*` notation is illustrative shorthand; the canonical pattern syntax is **element-wise regex** (so an unconstrained element might be written `.*`). Literal dot-separated boundaries remain boundaries for matching. The actual match contract must specify anchoring, escaping, missing/excess name elements and variable hierarchy depth without letting an element-level wildcard silently span adjacent elements.

A TopicMask may match a newly introduced Topic whose canonical elements satisfy the pattern without editing an enumerated identity list. Canonical renaming may change matching consequences even if the TopicID is stable; any such rename requires appropriate governance. Different consumer capabilities may apply different consequences to the same match, but the matching semantics themselves remain shared.

## 13. SecurityContext and Themis authorisation

An IU is a protected subject. Its `Metadata.SecurityContext` expresses **its requirements**, including applicable action, compartment and other criteria, through explicit Topics and/or TopicMasks. These are not the requesting Identity's permissions, and neither IU nor SecurityContext owns Themis policy interpretation.

Themis's effective AuthorisationMap/Passport describes what the authenticated Identity is authorised to do. A local PEP evaluates the IU's requirements against the applicable Passport/context and the requested operation. **TopicMask matches are necessary discriminations where the contract calls for them, not standalone authorisation grants.**

The neighbouring Themis DRP describes candidate `unitCriteria`, `containerCriteria` and `contentCriteria`, potentially derived from IU, Container and Content properties. They may be reconciled as IU-required criteria **without creating parallel taxonomies** or forcing clients to traverse the IU's internal metadata layout. Existing Themis requirement masks (`actionMask`, `compartmentMask` and other agreed criteria) should be reused according to their accepted contracts.

SecurityContext is independent from `InformationCoordinates`; operational owner, gateway and location do not inherently define security. Request/session Passports and enforcement snapshots do not automatically become durable IU Content or metadata. IU-required criteria may themselves be governed IU characteristics, with preservation rules established by the appropriate contract.

## 14. IU relationships, local and remote references

Optional IU-to-IU or IU-to-external-resource relationships are **attributes or references carried within the IU when supplied by its creator or authorised updater**. The IU is not a graph coordinator; it does not automatically track derived copies, generate reciprocal links or synchronise referents.

The historical names `relatesTo`, `partOf` and `basedOn` remain **illustrative**, not a universal prescribed vocabulary. The creator supplies the intended relationship semantics. They are not synonyms for provenance; provenance may separately record origin and transformation even when no explicit `basedOn` attribute exists.

**Local reference:** Mneme SHALL validate acceptability against the applicable governed local-reference contract (including suitable reference identity and any authorised reference constraints). Validating a reference is not a guarantee of clinical truth, an access grant to its target or an obligation to repair it if the target is later retired.

**Remote reference:** Mneme preserves the creator-supplied reference but does **not** assume responsibility for validating the external resource's existence, availability or correctness.

A reference does not imply shared IU identity, common lifecycle, reciprocal maintenance, distributed transactions or update propagation. Relationships may be represented in IU metadata where relevant, but the precise attribute structure remains a logical contract question, not permission to add a new universal relationship database.

## 15. Provenance, AuditEvent, accountability and assurance

The following are distinct:

```text
Provenance ≠ Audit Evidence ≠ IU Relationship ≠ Security Context
```

- **Provenance:** origin, derivation, transformation and lineage evidence for governed information.
- **AuditEvent:** records relevant information activity and security/access operations.
- **Accountability:** records or establishes who/what is responsible under the applicable evidence model.
- **IU Relationships:** optional source- or updater-supplied semantic references.
- **SecurityContext:** requirements for authorised handling.

Kleio manages AuditEvent information; Dokimasia is the assurance framework, with its precise provenance responsibilities to be reconciled against existing Continuum authority. Mneme's active information and Mnemosyne's authoritative persistence concerns remain distinct. Operational rsyslog logging is not a substitute for AuditEvents or provenance evidence.

The IU may contain appropriate identifiers, correlation values and references but SHALL NOT implement audit, provenance, accountability or lifecycle orchestration. **IU-12 is closed at the IU level**; the exact cross-service canonical allocation remains a reconciliation obligation.

## 16. Operational Version, Active Generation and temporal semantics

The IU framework recognises **three separate version concepts** with different homes:

| Concept | Location | Meaning |
|---|---|---|
| IU operational Version | `Metadata.ManagementContext` | Mneme cache coherence, stale-write detection and concurrency control |
| Content Structure Version | `InformationRepresentation.ContentMetadata` | Version of the schema/structure described by a Content Structure Topic |
| Content-native instance Version, if supported | `InformationRepresentation.Content` | Native version of the payload under its own information model |

Mneme's operational Version does not need to match the Content-native instance version and the framework must not invent such a version if the payload does not have one.

**Active Generation** belongs in `ManagementContext` as the identity of an **individual active manifestation** of the same IU in a Mneme cache endpoint/server. Multiple clients or caches may hold different generations representing the same IU operational Version. Generation identification is different from Version (which detects staleness). A new manifestation can have a new generation without creating a new governed IU Version, IU identity or InformationRepresentation. The IU model does not mandate global generation counters, universal distributed locking or a specific cache invalidation protocol.

Temporal attributes belong with their meaning:

- business/clinical event and effective times remain in native Content;
- content-structure definition/version information belongs in ContentMetadata;
- working-cache timestamps, if needed, belong in ManagementContext;
- persistence/handshake timestamps, if needed, belong in LifecycleContext.

No universal mandatory `TemporalContext` is introduced. These semantics coexist with the governed temporal-information Requirements and preservation of information-history where applicable; the IU DRP does not prescribe a single native payload lifecycle or synthetic universal version history.

## 17. Information lifecycle, persistence and retention

An IU's information lifecycle is distinct from persistence lifecycle, operational cache lifecycle and the lifecycle of a native payload. The Continuum does not prescribe one universal state machine for all information concepts. Depending on its subject and governing obligations, information may be current, superseded, retained, held, archived, retired or disposed.

`Metadata.LifecycleContext` is the IU-level coordination surface for Mneme–Mnemosyne acceptance, persistence and related handshaking. It does not own the whole lifecycle implementation; nor is the existence of an accepted request proof of successful authoritative remote completion.

Preservation, retention holds, archival, integrity verification, immutable history, authorised disposition and catastrophic recovery retain their existing distinct governance. **RETIRE** is not equivalent to physical destruction. Any temporal/state obligations must be applied by the responsible component and governed contract rather than inferred solely from cache timestamps.

## 18. Relationship to Mneme

Mneme governs active IU information access, working-state management and contextualisation using shared Calliope information models. Its `ManagementContext` workspace can expose embedded, referenced, hybrid or explicitly transient operational data, including operational Version and Active Generation. Those are not themselves independent IUs.

Mneme's information-access façades include `GovernedReader`, `GovernedWriter` and `GovernedSearch` at the Mneme–Mnemosyne boundary. It validates creator-supplied local IU references under the relevant contract, does not validate remote reference targets, and does not automatically infer relations or manage transformation/reverse transformation of Content.

A client needing another governed representation creates another IU after its own transformation. Any update of the source by that client remains governed by Mneme's concurrency/access rules. Clients cannot assume direct write authority merely because ManagementContext fields are visible.

## 19. Relationship to Mnemosyne and PersistenceServices

Mnemosyne is the authoritative governed persistence/acquisition boundary, conceptually providing `GovernedPersistenceFramework` and one or more `PersistenceService` implementations. It operates on IU identity and the information's authoritative contract, while allowing each provider its own appropriate physical storage format, native indexing or external gateway mechanism.

Mnemosyne interprets `InformationCoordinates` Topic references to resolve the appropriate gateway/service, logical endpoint, information location and operational owner. These are logical, named entities rather than embedded network endpoint configurations. Resolution may avoid costly repeated third-party discovery and GET/UPDATE operations, subject to the detailed Mnemosyne cache/service contracts.

The **Mneme–Mnemosyne boundary** preserves governed IU semantics. The **Mnemosyne–PersistenceService boundary** need not exchange physically IU-shaped objects. Providers may be Content-aware or Content-neutral and differ in native capabilities. On retrieval Mnemosyne must deliver the required IU contract and preserve required identity, Content/Metadata integrity, security and persistence obligations.

**IU-11 is closed at the IU level:** Mnemosyne must communicate meaningful authoritative outcomes; acceptance of governed work is not necessarily remote authoritative completion. The exact response vocabulary, recovery contracts and provider capability asymmetry belong in the Mnemosyne DRPs, not a new IU provider interface.

## 20. Integration, transformations and work execution

Harmonia shall preserve relevant native standards semantics, including Container/Content distinctions. Integration or client business logic may transform one IU's Content to another structure or format, but a **new governed transformation result is a new IU**, with distinct identity and applicable provenance. Such logic remains with the transforming client/capability; reverse transformations are neither assumed nor guaranteed.

Pragma, Pragmaton, Ergon, Petasos, Artemis and Digital Twin interactions may use IUs or references to IU Versions. These do not redefine IU identity or make IU an execution task envelope. Search Result Sets remain governed metadata-reference collections. Operational work durability, retries and the rule against resuming inside a failed Ergon remain allocated to their existing execution architecture.

## 21. Logical model, conformance and intentionally unspecified details

The canonical logical Class is `model.core.information.InformationUnit` in Calliope. Its conceptual decomposition appears in §2; that diagram is not a mandatory one-to-one Java Class decomposition. Canonical individual Class/Topic identities should be reused after registry reconciliation rather than manufactured because a concept has a descriptive label.

A conformant implementation of the adjudicated IU architecture shall demonstrate:

1. one governed IU identity, with persistent UUID and deterministically derived IU Topic name;
2. **one** governed InformationRepresentation, not an array, registry or synchronisation manager;
3. four distinguishable IU metadata contexts and their agreed owners/semantics;
4. four named Topic-based InformationCoordinates independent of SecurityContext;
5. TopicMask element-wise regex discrimination consistently reused across pub/sub, security, routing and discovery;
6. IU-side security requirements separated from Passport and PEP evaluation;
7. Mneme operational Version and per-manifestation Active Generation separate from Content Structure Version and optional native payload instance version;
8. optional creator/updater-supplied IU relationships, with local reference validation and no mandatory remote reference validation;
9. provenance, AuditEvent and lifecycle orchestration owned by their existing specialised capabilities;
10. native Content Structure and Format preserved distinctly, with transformation owned by clients/capabilities that create new governed IUs;
11. physical persistence freedom with faithful reconstruction of the governed IU contract and consistency of accepted state;
12. appropriate disclosure, authorisation, immutable preservation and retention controls without another governance identity inside the IU.

This DRP does not prescribe the UUID compression algorithm, Java records/interfaces, database schema, physical cache layout, gateway URI/credentials, endpoint resolution algorithm, precise TopicMask regex syntax/anchoring, provider acknowledgement vocabulary, transaction mechanism, universal IU state machine or concrete native Content version technology. Those details are governed in their appropriate contracts. FHIR `Attachment`, `DocumentManifest`, `MessageHeader` and `Bundle` remain informative design precedents, not IU definitions.

## 22. IU-01–IU-13 adjudication and reconciliation register

| ID | Subject | Disposition | Residual incorporation or contract work |
|---|---|---|---|
| **IU-01** | Conceptual/logical IU identity | **ACCEPTED** | Preserve `BO-InformationUnit` and `model.core.information.InformationUnit` as different views of the same IU |
| **IU-02** | Atomicity and one InformationRepresentation | **ACCEPTED** | Transforming clients produce separate IUs; Mnemosyne provider-native physical storage remains free; preserve IU consistency |
| **IU-03** | Metadata decomposition and ManagementContext | **ACCEPTED** | Four contexts; Mneme workspace may be embedded, referenced, hybrid, transient and appropriately visible |
| **IU-04** | InformationCoordinates | **ACCEPTED** | Four named Topic references for gateway, endpoint, logical location and operational owner; independent of security |
| **IU-05** | SecurityContext / TopicMask | **ACCEPTED** | IU-required Topics/TopicMasks; element-wise canonical-name regex; shared pub/sub, routing, discovery and security semantics |
| **IU-06** | UUID versus IU TopicID/name | **ACCEPTED** | UUID and `harmonia.mneme.iu.<hex-string-of-compressed-uuid>` IU Topic; distinguish all referenced Topics |
| **IU-07** | IU relationships/references | **ACCEPTED** | Optional creator/updater-supplied references; validate local, not remote; no reciprocal/synchronisation obligation |
| **IU-08** | Feature-facing Masks and Functions | **RESOLVED BY PRIOR DECISIONS** | Shared TopicMask semantics; no mandatory IU-specific universal operation catalogue |
| **IU-09** | Versions, Active Generation, temporal state | **ACCEPTED** | Mneme version + individual cache generation in ManagementContext; structure version in ContentMetadata; native version in Content if present; temporal data assigned by meaning |
| **IU-10** | Capability/Requirement mapping | **ACCEPTED — reconciliation method** | Semantically reuse current CT1→CT2→CT3→Feature and Motivation elements; do not manufacture seven historical Features or two RequirementGroups |
| **IU-11** | Persistence outcomes | **ACCEPTED — IU boundary** | Mnemosyne DRPs own detailed outcomes, provider asymmetry and acceptance versus remote completion |
| **IU-12** | Audit, provenance and lifecycle allocation | **ACCEPTED — IU boundary** | Kleio AuditEvent; Dokimasia assurance; canonical provenance allocation and Mneme/Mnemosyne lifecycle responsibilities to be reconciled |
| **IU-13** | Working branch | **VERIFIED / CLOSED** | `Mneme-and-Mnemosyne-Build` verified at `cb21ca0c5291f726f4babc7e59dabeba1a6c302c` |

All thirteen adjudication subjects are **closed as architectural discussion decisions**, but the expressly bounded canonical mappings, exact Class/Topic properties, provider contracts and related-DRP incorporation work are not thereby asserted complete. Where the text uses a proposed property name, canonical IDs remain the repository's authority.

## 23. Governed Continuum incorporation — connected architectural delta

Upon Solution Architect acceptance and authorisation, Codex should incorporate the **accepted semantic delta**, preserving existing IDs and governed relationships.

### 23.1 Motivation
Reuse the canonical Principles, Requirements, RequirementGroups and established NFRs for IU identity, data security, integrity, version/concurrency, resilience, preservation, retrieval, temporal semantics, audit, provenance and lifecycle. Reconcile the historical `GovernedInformationState` and `InformationPersistence` proposal names; do not create duplicates merely because the October draft called them candidates.

### 23.2 Strategy
Map the seven historical Capability/Feature proposals to their *semantic* equivalents in the accepted 1.1.20+ capability hierarchy. An IU is a foundational information element, not 'owned' by a single capability. No automatic new `Information Content and State Management` capability.

### 23.3 Business information architecture
Preserve canonical `BO-InformationUnit`. Express atomic identity and one governed information representation, creator-supplied optional IU references, separate provenance and IU naming/Topic reference semantics. Reconcile existing Topic names/classifications and relationships first.

### 23.4 Application information architecture
Preserve `model.core.information.InformationUnit` identity. Incorporate the four metadata contexts, four InformationCoordinates references, IU identity Topic, representation/content-structure metadata distinctions and accepted version/generation semantics under their correct logical model homes. Do not infer that every box in a conceptual diagram requires its own Java Class or new ArchiMate Topic.

### 23.5 Application behaviour and cross-layer relationships
Connect the existing feature contracts (Mneme/Mnemosyne, Themis, pub/sub, discovery, audit, provenance, workflows) to applicable IU information architecture using the strongest semantically appropriate relationships. Derive service behaviour from its governing Feature; neither multiply 'IU Functions' nor silently modify other subsystem design.

### 23.6 Explicit scope boundary
Acceptance of this DRP alone does not authorise ADD execution, a repository commit/push, Junie development or new PersistenceService, Themis or transformation implementations. Detailed contracts remain with their respective DRPs and governed architecture.

## 24. Review decision

**Standing:** Complete consolidated DRP for Solution Architect review, incorporating the recorded IU-01–IU-13 adjudications.  
**Architectural adjudications:** Accepted in dialogue, as individually recorded in §22.  
**Acceptance of this complete consolidated document:** Not yet recorded; requires review as a complete artefact.  
**Authority:** Solution Architect and current governed repository process.  
**Scope qualification:** Authoritative element/relationship mapping and outstanding cross-DRP reconciliation remain incorporation work rather than new IU design decisions.

## 25. Post-decision disposition

Following the Solution Architect's review and acceptance of the **whole consolidated DRP**, the text can be supplied as the accepted architectural input for a separately authorised Codex Continuum ADD/reconciliation. Codex should reuse existing canonical identities and relationships; incorporate only genuine deltas; reconcile related DRPs within authorised scope; and preserve required governance evidence. Junie implementation would then consume the accepted architecture and applicable detailed designs.

No repository file, generated model or application code has been changed by this consolidation.

