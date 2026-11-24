<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — Core Information Unit Framework

**DRP identity:** `DRP-Information-Unit-Framework-001`  
**Document standing:** Consolidated review candidate; **not** a published Architecture Continuum change or an accepted implementation specification  
**Primary baseline:** `DRP-Information-Unit-Framework.draft.003.md` (27 October 2026)  
**Prepared for:** Solution Architect review and architectural adjudication  
**Repository:** `fhirfactory/harmonia`  
**Intended working branch:** `Mneme-and-Mnemosyne-Build`  
**Accessible source branch for this consolidation:** `Capability-Map-Consolidation` (see source qualification)  
**Date:** 11 November 2026

## 0. Source authority, scope and interpretation

This document consolidates the existing Information Unit DRP with subsequently available governed Information Unit architecture and relevant adjacent design proposals. Its object is the **Information Unit as Harmonia's fundamental governed information building block**, not an independent design of Mneme, Mnemosyne, Themis or an implementation framework.

The authoritative source for accepted architecture is the Markdown Architecture Continuum under `enterprise-architecture/continuum/`. The existing DRP is the baseline for consolidation; other DRPs are design evidence and may contain proposals rather than accepted decisions. The retained architectural propositions are presented as **DRP design assertions** unless independently confirmed as accepted in the Continuum. `SHALL` expresses a constraint proposed or inherited in this DRP; it does not claim every sentence has already been incorporated into the Continuum.

**Source qualification.** At the time of preparation, the remote GitHub interface did not expose `Mneme-and-Mnemosyne-Build` (`No commit found for the ref`), although that is the working branch identified by the Solution Architect. The available remote branch was `Capability-Map-Consolidation`. Accordingly, this consolidation cannot certify that it reflects every change in the new branch, nor that it exhaustively inspected the many thousands of Continuum resources. Explicitly verified Continuum identifiers are distinguished from DRP claims and subjects for follow-up reconciliation. There has been no repository modification, ADD, acceptance, commit or push.

### 0.1 Canonical anchors verified in the accessible Continuum

| Construct | Verified source identity | Established description / qualification |
|---|---|---|
| Business Information Unit | `business/business-object/10284034c8534b639284386b064f2c54.md` — `BO-InformationUnit` | Accepted conceptual governed management boundary, not a storage or transport schema |
| Logical InformationUnit Class | `application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md` — `model.core.information.InformationUnit` | Accepted bounded logical class (HCRL 1.1.15-001); governs the association of a Governed Representation with its Management Context |
| Search Result Set | `business/business-object/b683ae87c72a4c16aba948a4b8cb0998.md` — `BO-SearchResultSet` | Adjudicated managed metadata collection; not the full contents of every matched Information Unit |

The first two anchors are *different architectural-layer representations* of the Information Unit; they are not grounds to create two competing information units. Nor does the accepted logical Class by itself establish all attributes proposed in `draft.003`.

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

These names are retained as the **historical proposal inventory**, not silently promoted to current canonical Feature identities. The 1.1.20 Capability consolidation introduced a much larger and reorganised four-tier capability catalogue, so current CT1/CT2/CT3/Feature ancestry and equivalence must be established using canonical identity and semantic definition, not matching labels or the obsolete `DataManagement.InformationUnits.*` paths found in neighbouring DRPs. The IU itself remains foundational even if related Features are allocated across several Enterprise Capabilities.

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

## 2. Core proposal

The **Information Unit is the indivisible unit of information governance** within Harmonia. It has independent governed identity, management metadata and one governed representation of information.

The October design's conceptual form is retained:

```text
InformationUnit
├── Identity
├── Metadata
└── InformationRepresentation [1..1]
    ├── ContainerMetadata
    ├── ContentMetadata
    └── Content
```

This improves upon the earlier `Identity / Metadata / Content / Context` partition by separating IU governance, envelope or native-container semantics, description of represented content and the content itself. `InformationRepresentation` is the preferred DRP term over an undifferentiated `Payload`; the exact canonical vocabulary and composition remain items for architectural reconciliation (§22).

**Important companion model:** the governed logical Class describes an association between a **Governed Representation** and its **Management Context**. The revised IU decomposition must preserve that accepted meaning. `InformationCoordinates`, security criteria and policy context cannot simply be collapsed into the undifferentiated `Metadata` box without establishing their distinct semantics.

## 3. Architectural definition and indivisibility

An IU is the boundary by which information receives independent governed standing. Internal constituents can be structured, inspected and transformed by authorised operations without themselves becoming independently governed IUs. The unit of governance is not necessarily the unit of database storage, transport or computation.

An IU is not synonymous with its Content, Information Representation, FHIR Resource, message, Java instance, persistence entity, database row, HTTP request, broker parcel or cache entry. An implementation may map an IU into many storage records and may reconstruct an IU from references or provider-native representations without changing its governance identity.

**One representation per IU.** The proposed invariant `InformationRepresentation [1..1]` prevents multiple independently governed representations from being embedded under a single IU identity. Transformation, extraction, repackaging and independently governed alternates may yield other IUs connected by governed relations and provenance. It does **not**, by itself, require that all content bytes be inline, locally stored or physically indivisible.

## 4. Information Unit identity

`InformationUnit.Identity` identifies the **governed Harmonia information element**. It must be distinguishable from:

- the identity of the information or document represented;
- the identity of a patient, practitioner, device, organisation or other represented entity;
- external-source identifiers and aliases;
- provider-native database keys or object references;
- transport/message identifiers; and
- interaction, transaction and audit correlation identifiers.

IU identity persists through the operations and lifecycle changes for which the IU retains the same governed standing. A decision that content has become a *new* governed unit entails new IU identity and a relationship to the predecessor, not accidental reuse of an external record ID.

The baseline deliberately does not mandate UUID type, identifier syntax, generation algorithm or serialisation. Topic identity (`TopicID`) must not be equated with IU identity without an explicit canonical identity rule; a Topic reference ordinarily identifies a Topic **about** an IU or a dimension, not automatically the IU instance itself.

## 5. Information Unit Metadata and Management Context

IU Metadata is Harmonia-aware information needed to recognise, govern and process the IU, distinct from Container Metadata and Content Metadata. Depending on applicable Features, it can express or link to:

- IU type/Unit Structure, identity and version;
- governed state and active generation;
- valid/observed/recorded/changed temporal coordinates;
- stewardship, source, owner, custody and location;
- governed relationship references and provenance linkage;
- classification, compartments and other information-protection attributes;
- processing and correlation context; and
- native representation references or integrity characteristics necessary for the task.

These are **semantic domains**, not an approved list of Java fields. Appropriate information-management operations should ordinarily be possible using governed identity and management metadata without interpreting Content. Some specialised operations will also require Container or Content Metadata.

`Management Context` is an accepted term in the governed Class description. The obsolete or overlapping `InformationUnitContext` formulation must be reconciled into IU Metadata, InformationCoordinates, retained external governance reference or another established construct **without losing its meaning**. A context used only for an execution/session is not necessarily durable IU state.

### 5.1 InformationCoordinates

The Topic/InformationCoordinates DRP explicitly proposes **InformationCoordinates** as a structured set of *named Topic references* identifying information-management dimensions, initially:

- `informationOwner: TopicID` — owner of the information;
- `informationLocation: TopicID` — information's location.

These properties are not an interchangeable unordered Topic bag; ownership and location may follow unrelated Topic classification/containment hierarchies. The proposal's representative structures such as `Patient::EMPI=...` and `Observation::Enterprise=...` are examples, **not prescribed Topic namespaces**.

The coordinate construct complements the IU management context. Its precise containment, representation and accepted canonical Topic/Class status require review. A location coordinate does not automatically confer physical storage authority, and an ownership coordinate does not itself establish access rights.

## 6. Information Representation

The IU's one `InformationRepresentation` comprises distinguishable **ContainerMetadata**, **ContentMetadata** and **Content**. It is neither equivalent to an HTTP payload nor reduced to a transport envelope. The representation captures information's relevant native carrier/envelope semantics alongside its represented content.

An Information Representation may be physically represented through references, metadata and native content held separately. Multiple *physical realisations* or copies of the same governed representation must not be confused with independently governed alternate representations.

## 7. Container Metadata

Container Metadata describes the container, envelope, assembly or carriage associated with the IU's representation. Potential dimensions include container type, source/destination, author/recipient, assembly context, interaction mechanism, envelope structure and related descriptive characteristics.

The same Content delivered through a FHIR Bundle, document, event, broker message or HTTP operation may convey different native context and integration significance. Relevant native envelope semantics must be preserved when governed by the information-management contract; they are neither invariably disposable transport state nor automatically identical to IU Metadata.

Content-aware integration functionality may inspect and transform Container Metadata under its own Feature responsibilities. This does not transfer integration responsibility to Mnemosyne.

## 8. Content Metadata

Content Metadata describes the represented Content without requiring every IU consumer to understand the Content's inner semantics. It may capture content type, semantic structure/schema/profile/model identity, language, concrete encoding, size, hash/digest, referenced location and integrity information.

The established separation is:

```text
Unit Structure ≠ Content Structure ≠ Content Format
```

- **Unit Structure**: structure/type of the governed IU.
- **Content Structure**: semantic model/schema of its Content.
- **Content Format**: concrete syntax/encoding or representation.

A system may happen to use the same artefact for two domains, but this does not erase their semantic distinction. Changes to encoding need not entail changes to the Content's semantic structure; whether they require a new IU Version or a separate IU follows governed change semantics rather than an automatic transport rule.

## 9. Content

Content may be structured, composite, textual, binary, externally defined, inline, referenced, domain-specific or opaque to particular consumers. The IU model is **FHIR-independent**, though FHIR-oriented and other native-domain services may interpret Content deeply when their responsibility calls for it. The HAPI JPA example in the original DRP illustrates this permitted specialisation; it is not an architectural product prescription.

For large information, established Harmonia pass-by-reference and media-management boundaries apply; IU governance must remain effective whether Content is inline or externally managed. This DRP does not make a relational database the default store of large binaries or mandate a binary size threshold in its model.

## 10. Encapsulation and governed behaviour

A consumer shall not be forced to couple to deep internal paths such as `InformationUnit.InformationRepresentation.ContentMetadata.*` solely to filter, route, authorise or publish information. Stable, Feature-appropriate operations should expose the relevant governed characteristic.

The original DRP discussed candidate `TopicMask`, `SecurityMask` and other Masks, along with conceptual `get(...)` / `test(...)` behaviour. These are **design patterns**, not accepted universal APIs, physical mask encodings or required Java signatures. Internal metadata may contribute to derived criteria without being exposed wholesale.

Encapsulation is not absolute opacity: authorised domain services can access Content or specific metadata where their contractual responsibilities require it. The principle is to expose stable *semantic contracts* rather than permit uncontrolled dependence on an implementation layout.

## 11. Discovery, filtering, routing and publication/subscription

These behaviours may depend on characteristics drawn from IU Metadata, Container Metadata and Content Metadata. They must not flatten the three metadata domains into an indistinguishable bag merely to simplify indexing.

- Topic-aware filtering may use governed Topic classification and hierarchy.
- Discovery may identify IUs by metadata and content-oriented criteria, under disclosure policy.
- Routing may use IU identity and relevant container/interaction context.
- Publication/subscription may use Topic-oriented criteria.
- Information protection may use derived RequiredCriteria and effective authorisation.

The governed **Search Result Set** is a managed metadata collection containing references to IU Versions, not a presumption that every matching IU's content is disclosed. Search permission and retrieval permission can be separately evaluated. Search results used in work execution can be persisted in an Ergon context, without changing IU identity or granting access by inclusion.

## 12. Topic identity, hierarchy and matching

A **Topic** has its own governed canonical identity; a Topic reference (`TopicID`) identifies that Topic, not merely a mutable distinguished-name string. Topic classification and containment hierarchies support discrimination and hierarchical matching. Design-time naming or classification updates and runtime identity stability must be handled as separately governed matters.

IU identity, IU type/topic, owner Topic, location Topic, content Topic and security attributes must remain distinguishable. `InformationCoordinates` provides named references for dimensions, not a replacement for the identity or security model. Hierarchical matching does not itself provide a positive authorisation decision.

## 13. Information protection and Themis

An IU is a **protected subject**. It can own/expose the criteria required to perform an operation against it, but it does not own interpretation of Themis policy or construction/lifecycle of an Identity's Passport.

The neighbouring Themis DRP distinguishes:

- **RequiredCriteria** — the protected subject's conditions;
- **AuthorisationMap / Passport** — the authenticated Identity's effective authorisations;
- **PEP** — local enforcement using the appropriate effective authorisation context;
- **Themis policy functions** — authoritative policy definition/management and related distribution/decisions.

For IUs, the Themis draft proposes candidate, not yet canonically confirmed, groupings:

| Candidate criterion domain | Examples |
|---|---|
| `unitCriteria` | IU status, temporal context and Harmonia source |
| `containerCriteria` | Container Topic/status and carrier context |
| `contentCriteria` | Content Topic/status, origin, classification, compartment tags |

IU-owned metadata can supply these criteria through encapsulated behaviour. The precise grouping and masks remain subject to accepted Themis model reconciliation. Authorisation evaluation also considers the requested **operation/action**, not merely a static data label. The effective Passport and context must be current enough for enforcement; permission cannot be inferred merely from possession of a reference, successful search or the right to execute a Pragma/Ergon.

Security context used for enforcement does not automatically become durable IU Content or metadata. Intrinsic information classification and compartmentalisation remain governed attributes; operational Passport snapshots, session context and PEP state have distinct lifecycles.

## 14. IU relationships, derivation and identity continuity

Independent IUs may be related through governed relationship semantics. The October candidate vocabulary included `relatesTo`, `partOf` and `basedOn`; these remain **examples pending canonical relationship adjudication**, not authorisation to create new canonical Topics/classes.

Relationships may express association, containment, source or derivation, subject to their governed meanings. They do not replace provenance. Conversely, provenance cannot substitute for every semantic relation between IUs. Derived or transformed material that acquires independently governed standing should receive independent IU identity and an appropriate lineage relationship, rather than silently overwrite its source.

## 15. Provenance, audit and accountability

Preserve the following distinctions:

```text
Provenance ≠ Audit Evidence ≠ IU Relationship ≠ Security Context
```

- **Provenance** concerns origin, derivation and lineage of information and state.
- **Audit** records relevant governed actions, including attempted access and disclosure according to applicable requirements.
- **Accountability** identifies accountable actor, mechanism, authority or cause as supported by the relevant governance contract.
- **Relationships** express meaning between separate IUs.
- **Security context** determines and evidences permissible use without becoming provenance by default.

Kleio is associated with AuditEvent management; Dokimasia with assurance/provenance-related concerns subject to the current governed allocation. Operational logging via rsyslog is distinct from authoritative AuditEvent and provenance records. Where these frameworks use IUs, IU primacy does not mean the IU itself must embed every event or proof record.

## 16. Version, Active Generation and temporal governance

**Version** describes governed progression of represented information; **Active Generation** describes a valid active manifestation established for management or use and is not necessarily a new Version. Preserve version-specific retrieval and version-specification integrity as separate concerns.

Relevant time semantics may include observed time, applicable/valid time, recorded/awareness time, governance-state change time, representation establishment and active generation time. Do not equate all of these with database timestamps. Temporal governance must remain meaningful across persistence, replication and migration.

The rules governing when content changes yield a new Version, new generation or new IU require canonical specification; do not invent a universal rule from the presence of a timestamp or digest.

## 17. Information lifecycle and retention

Information lifecycle is not identical to persistence lifecycle or the state of a particular database record. Information may be established, active/current, superseded, withdrawn, held, archived, retired or disposed according to **concept-specific governed transitions**; the Continuum expressly rejects a universal lifecycle imposed on all information concepts.

Preservation, retention hold, archive, migration, integrity verification and authorised disposal are distinct obligations. A retention hold protects information against an incompatible lifecycle/disposition action; policy must govern the action rather than rely on silent provider deletion. Immutable preserved versions remain distinct from an operational latest projection. The term **RETIRE**, where used for adjudicated retirement semantics, is not a synonym for physical destruction.

## 18. Relationship to Mneme

Mneme performs active governed information management, access, translation and contextualisation through its façades. Where Mneme consumes or supplies IUs, it must preserve their identities, permitted transformations, version semantics and information-protection obligations.

`GovernedReader`, `GovernedWriter` and `GovernedSearch` form the established Mneme–Mnemosyne interaction vocabulary. A search can identify IUs that may require further READ authorisation. Contextualisation and disclosure apply applicable access authority, and active work-context data is not presumed to be authoritative IU history.

The IU model is shared foundational architecture, not an internal Mneme data structure. Mneme may derive an operational projection, but that projection does not redefine the authoritative IU or its persistent identity.

## 19. Relationship to Mnemosyne and PersistenceService

Mnemosyne provides authoritative durable preservation and authoritative acquisition, logically acting upon governed IUs. It comprises `GovernedPersistenceFramework` with one or more `PersistenceService` implementations, while retaining a single governed persistence responsibility envelope.

Mnemosyne should commonly be able to decide persistence orchestration from IU identity and metadata, consulting Container/Content Metadata as necessary. It need not interpret native Content semantics itself. A PersistenceService may legitimately be deeply Content-aware (e.g. FHIR-native indexing) or treat Content as opaque.

**Intentional asymmetry:** the Mneme-to-Mnemosyne boundary preserves governed IU semantics; the Mnemosyne-to-PersistenceService boundary need **not** be physically `InformationUnit`-shaped. Providers may persist different physical representations and offer asymmetric native search/index capabilities. Provider convenience must not redefine authoritative IU identity, security, integrity, lifecycle or version semantics. Provider or sharding selection must preserve the authoritative logical identity across repartitioning and migration.

Accepted work durability, preservation before acknowledgement and recoverability are relevant NFR constraints; detailed recovery objectives, outcome vocabulary and provider capability contracts belong in the corresponding persistence DRPs and Continuum Requirements, not guessed here.

## 20. Relationship to integration, native standards and work execution

An IU may carry native-domain Content and meaningful Container Metadata. Harmonia preserves native standards semantics rather than forcing every source into a lossy canonical content schema. Integration functions can transform representations, retaining traceable source/derived IUs as required by governance.

Pragma is a task envelope and Pragmaton is associated with contextualised working information. Ergon execution, Petasos handoff, Artemis routing and Digital Twin processing may reference IUs or IU Versions. Those work-management constructs **are not alternate definitions of IU**. Durable work acceptance, retries, execution context and in-flight state remain separately governed; recovery must not silently continue inside a failed Ergon merely because its information state has been reconstructed.

## 21. Logical model, conformance and non-prescriptions

The accepted logical-model anchor is:

```text
model.core.information.InformationUnit
```

The conceptual decomposition in §2 is **not** a mandated one-to-one set of Java classes. Candidate logical names from the October baseline include `InformationUnitId`, `InformationUnitMetadata`, `InformationRepresentation`, `ContainerMetadata`, `ContentMetadata`, `Content`, `InformationUnitVersion` and `ActiveGeneration`. Their existence as canonical Classes or Topics must be individually checked; the accepted `InformationUnit` Class is not evidence that its proposed children are all accepted.

A conformant realisation should demonstrate:

1. independent IU identity and indivisible governed standing;
2. coherent handling of its one governed representation;
3. preserved distinctions among IU, Container and Content Metadata and Content;
4. independent Unit Structure, Content Structure and Content Format;
5. version-specific identity and active-generation distinctions;
6. security criteria exposed through governed behaviour and subject to current PEP decisions;
7. Content-aware and Content-neutral consumption where applicable;
8. independently governed derivative IUs and associated relationships/provenance;
9. logical IU governance despite different provider-native storage representations;
10. consistency with existing lifecycle, temporal, durability and disclosure rules; and
11. no dependence on FHIR, Java, SQL, messaging or one persistence product in the foundational IU definition.

This DRP does **not** prescribe a Java record/class shape, constructor, JSON/XML layout, schema, identifier encoding, persistence mapping, cache layout, database engine, HTTP or broker protocol, Mask width, `get`/`test` method signature, provider selection algorithm, or universal lifecycle machine. Such details are derived from accepted Feature and solution contracts.

## 22. Consolidated outstanding decisions and reconciliation record

| Ref | Subject | Current treatment | Required review |
|---|---|---|---|
| IU-01 | IU conceptual and logical identity | **Partly resolved:** accepted Business Object and logical Class have been verified | Preserve their canonical identities and reconcile the DRP decomposition with the accepted Class's Governed Representation / Management Context language |
| IU-02 | `InformationRepresentation` and `[1..1]` | Retained substantive `draft.003` proposition | Verify adoption into current Continuum; clarify representation versus physical copies and alternate native encodings |
| IU-03 | `InformationUnitContext` / Management Context / Metadata | Overlapping terminology | Map established management context, coordinates and operation-only context without semantic loss |
| IU-04 | `InformationCoordinates` | Related DRP specifies named owner/location Topic references | Verify current canonical standing, representation and cardinality; avoid treating coordinate ownership as authorisation |
| IU-05 | Topic/Class vocabulary for metadata/content | Candidate names remain | Match canonical UUIDs before establishing new Topics, Classes or relationships |
| IU-06 | IU identity versus `TopicID` | Conceptually distinct, but proposal contains ambiguous `TopicID id` example | Determine exact identity/reference contract without conflation |
| IU-07 | IU relationship vocabulary | `relatesTo`, `partOf`, `basedOn` illustrative | Reuse established information relationships and provenance; settle precise allowed semantics |
| IU-08 | RequiredCriteria/Mask/function contract | Encapsulated subject-owned criteria supported by Themis draft | Reconcile candidate `unitCriteria`, `containerCriteria`, `contentCriteria` against accepted Themis requirements/masks and enforcement context |
| IU-09 | Version, generation, time and lifecycle | Distinctions preserved | Reconcile exact Topic/Requirement IDs, accepted state vocabulary and rules for changes |
| IU-10 | Capability/Feature and Requirement mapping | Old proposed `Information Content and State Management` group and `DataManagement.*` paths preserved only as provenance | Map semantically into the accepted 1.1.20+ CT1→CT2→CT3→Feature and Motivation hierarchies; avoid duplicate creation |
| IU-11 | Authoritative persistence outcomes | IU boundary known, provider design intentionally flexible | Align exact outcome and capability asymmetry with Mnemosyne DRPs rather than inventing provider contracts here |
| IU-12 | Audit/provenance/lifecycle allocation | Distinct responsibilities retained | Verify current canonical allocation to Kleio, Dokimasia and relevant services |
| IU-13 | Working branch state | New branch not visible remotely | Re-run source reconciliation against `Mneme-and-Mnemosyne-Build` when accessible, before treating this as current-state complete |

These are targeted architecture reconciliation matters, **not** a proposal to reopen accepted Decisions solely because an older draft used different words.

## 23. Continuum incorporation specification (subject to DRP acceptance)

The accepted DRP should be translated to a **connected architectural delta**, not an unrelated list of new elements.

### 23.1 Motivation

Link to the existing Principles, Axioms, RequirementGroups and Requirements for IU identity, information protection, provenance, versioning, lifecycle, temporal governance, resilience and exchange. Introduce only demonstrably missing Motivation elements, with reconciled identities and authority.

### 23.2 Strategy and capabilities

Associate IU-consuming Features using their actual accepted CT ancestry and identities. Preserve the historical seven Feature candidates only for semantic reconciliation. Do **not** create a redundant `Information Content and State Management` capability merely to make this DRP's historical proposal literal.

### 23.3 Business Information Architecture

Retain `BO-InformationUnit` and applicable existing business-information constructs. Represent independently governed IU identity, its relationships and governing representation semantics, preserving established Topic and search-result definitions. Reconcile the `Identity / Metadata / InformationRepresentation` conceptual shape with the governed Information Unit definition rather than replacing the Business Object.

### 23.4 Application information and behaviour

Retain the canonical `model.core.information.InformationUnit` Class. Evolve the Class/Topic model only where a semantic boundary is accepted, preserving ownership within foundational Calliope models. Feature-specific Functions, Services and Processes belong with the Feature architecture; do not fabricate a parallel catalogue of 'InformationUnit services' to mirror data fields.

### 23.5 Cross-layer relationships

Connect IU information constructs to the applicable behaviours and Features using the semantically strongest appropriate governed ArchiMate relationship. Do not use generic Association to avoid deciding known structural, access or realisation semantics. Preserve canonical identity and existing relations; do not infer a change in identity from renamed display terms.

### 23.6 Explicitly unaffected

No authorisation is conveyed to redesign Mneme, Mnemosyne, Themis, Petasos or integration architecture; select persistence vendors; choose data formats; generate Java interfaces; edit the Continuum; run an ADD; or perform code implementation.

## 24. Review decision

**Standing:** Consolidated candidate for Solution Architect review.  
**Decision:** Not yet recorded.  
**Acceptance authority:** Solution Architect under the repository's current architecture governance.  
**Key qualification:** the intended working branch was not accessible remotely during consolidation; canonical coverage is bounded by explicitly inspected sources.

## 25. Post-decision disposition

Once this document is accepted, its adjudicated architectural delta may be supplied as input to an authorised Codex operation under the *then-current* repository governance. The authorised operation should reconcile, rather than duplicate, canonical IDs and relationships; carry accepted distinctions into related DRPs where necessary; and preserve the evidence and review records required by the repository. Implementation by Junie should subsequently consume the accepted Continuum architecture, not treat unaccepted DRP proposals as instructions.

## Appendix A — Design lineage and references

The original Information Unit design drew on accepted Harmonia information governance and the generalised patterns of FHIR `Attachment`, `DocumentManifest`, `MessageHeader` and `Bundle`. Those FHIR resources remain informative design precedents, **not** Harmonia architectural authority or a requirement to shape IUs as FHIR resources.

The following source references use paths relative to the Harmonia repository:

- `enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md`.
- `enterprise-architecture/continuum/business/business-object/10284034c8534b639284386b064f2c54.md`.
- `enterprise-architecture/continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md`.
- `enterprise-architecture/continuum/business/business-object/b683ae87c72a4c16aba948a4b8cb0998.md`.
- `enterprise-architecture/design/offline-design/DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md`.
- `enterprise-architecture/design/offline-design/DRP-Themis-Policy-Passport-Enforcement.draft.001.md`.
- `enterprise-architecture/design/offline-design/DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md`.
- `enterprise-architecture/design/offline-design/DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md`.
- `enterprise-architecture/design/offline-design/DRP-Interaction-Mnemosyne-to-PersistenceService.draft.002.md`.
- `enterprise-architecture/design/offline-design/DRP-Mneme-Security.draft.002.md`.

## Appendix B — Carry-forward principles from draft.003

This consolidation expressly retains all of the baseline's principal propositions: IU primacy and indivisibility; a single representation; Unit, Container and Content Metadata separation; Contents as native and potentially opaque or content-aware; structure/format separation; stable Feature-facing encapsulation; filter/discovery/pub-sub use without flattening metadata; relations distinct from provenance; audit distinct from security; Version distinct from Active Generation; information lifecycle distinct from persistence; Mnemosyne as IU-authoritative but provider-representation-neutral; container-aware integration; foundational logical namespace; FHIR independence; implementation neutrality; and an accepted architecture delta rather than automatic code prescription.

The old open-question list is consolidated into §22 instead of being silently treated as answered by later vocabulary. The old capability/requirements proposal inventory is preserved in §1 for traceable semantic mapping, but its historical 'pending inclusion' statements are not incorrectly promoted to a current Continuum status assertion.
