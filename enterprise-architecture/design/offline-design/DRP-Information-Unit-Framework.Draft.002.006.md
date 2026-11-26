<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Design Review Proposal — Information Unit Framework

**Standing:** Draft 002.006 for Solution Architect review. This successor preserves Drafts 002.004 and 002.005 and incorporates the accepted BL-0005 final corrections recorded in software HCRL 0.1.0/011; §1.6 is clarified per the accepted DR-11 interpretation and recorded in DR-12. It does not itself establish Continuum incorporation or software implementation acceptance.

**Date:** 26 November 2026

**Baseline:** [Continuum 1.1.26][release], [IU Framework Final.001 (IU-01–IU-13)][iu-final-001], [IU Framework Final.002][iu-final-002], and the [Mneme–Mnemosyne Interaction][interaction]. These sources establish the accepted baseline against which the proposed design is reviewed. [Architectural Axioms][axioms]

An InformationUnit (IU) is Harmonia's atomic, individually governed unit of information. The business and logical definitions describe the same unit. The proposal specifies the Calliope logical model. The accepted contracts continue to govern access, search, disclosure, persistence, retention and recovery. [Business Information Unit][iu-business] · [Logical InformationUnit][iu-class]

## 1 Draft 002.006 — InformationUnit model for Calliope

```text
InformationUnit
├── id : UUID
├── informationConcept : Topic
├── description : String
├── InformationQuality
│   ├── owner : String
│   ├── grade : {Anecdotal, Informational, Canonical}
│   └── certifications : Map<String, String>
├── InformationMetadata
│   ├── PersistenceLifecycleMetadata
│   ├── CacheLifecycleMetadata
│   ├── OriginMetadata
│   ├── InformationCoordinates
│   ├── SecurityMetadata
│   ├── informationAuthority : Topic
│   └── Relationships
│       └── relationships : List<InformationUnitRelationship>
└── InformationRepresentation [1 per IU]
    ├── ContainerMetadata
    │   ├── containerConcept : Topic
    │   ├── containerSchema : Topic
    │   └── containerMediaType : String
    ├── ContentMetadata
    │   ├── contentConcept : Topic
    │   ├── contentSchema : Topic
    │   ├── contentMediaType : String
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
    └── Content [one of]
        ├── reference
        └── data
```

Initialise all defined metadata containers, including when individual values are unknown or inapplicable. The diagram describes logical composition. The Calliope design notes guide package placement, class granularity, immutability and format selection. [Initial Model Architecture][package] · [Object and Class Granularity][granularity] · [Object Immutability and Modification Guidelines][immutability] · [Serialization and Format Selection][serialization]

### 1.1 Identity and naming

The immutable `id : UUID` is the IU's sole instance identifier. The immutable `informationConcept : Topic` replaces `ConceptFDN` and `informationProfile` as the representation of the IU's classification. `description` is a direct `String` property.

`InformationUnit.getName()` returns `informationConcept.getName() + id.toString()` with no inserted separator. `Topic.getName()` joins the Topic's raw values with periods. The string name is the current designation, superseding the previous designation without a legacy compatibility binding. The Topic's own value identity remains the ordered qualified path, independent of entry versions. [G02 A04][identity] · [Topic and TopicMask Final.003][naming]

For MI-05, the single `informationConcept` field provides the concrete qualified Topic. The Store matches it against the selected instance's supported TopicMasks. It preserves the applicable required-criterion match and complete coverage across eligible, available instances. [Store contract][store] · [Mneme–Mnemosyne Interaction][interaction]

### 1.2 Lifecycle metadata

`PersistenceLifecycleMetadata` and `CacheLifecycleMetadata` are direct constituents of `InformationMetadata` and supersede the previous allocations of these properties. Mnemosyne governs the authoritative Managed Revision, disposition and legal hold in `PersistenceLifecycleMetadata`. Mneme governs cache state and Active Generation in `CacheLifecycleMetadata`.

Preserve the behavioural responsibilities of Mneme's operational revision and the persistence handshake. Active Generation identifies a cache manifestation. The Managed Revision remains distinct from the operational revision, Active Generation, Data Version, schema Topic version and any native payload instance version. Preserve exact-revision reads, immutable authoritative history and the actual durably established revision on successful writes. [Information Unit Metadata][metadata] · [G06 A15][revision] · [Mneme–Mnemosyne Interaction][interaction]

A local modification retains the originating Managed Revision as the expected revision for a subsequent governed write. Following successful persistence, Mnemosyne establishes the successor Managed Revision. Calliope does not clear or allocate the Managed Revision while constructing a local revision candidate.

### 1.3 Coordinates, origin and itinerary

`InformationCoordinates` retains `PersistenceGateway`, `GatewayEndpoint`, `InformationLocation` and `InformationOwner`. Coordinates are immutable within each governed revision. Authorised relocation or ownership change establishes a new revision under the same IU UUID, preserving the earlier coordinates and originating Managed Revision as the expected revision for a subsequent write. Coordinates describe logical resolution and ownership independently of security requirements.

`OriginMetadata` records original source system (`Topic`), first Harmonia entry component (`Topic`) and entry instant (`Instant`), including for internally generated information. Each value may initially be unknown and may be populated once established. Established values are immutable. Harmonia entry, source creation, logical location and derivation are separate facts.

`InformationMetadata.informationAuthority : Topic` is required. An authorized authority change establishes a new governed IU revision under the same UUID; the prior snapshot and its authority remain unchanged. The originating Managed Revision remains the expected revision for the subsequent governed write. `InformationQuality` is unaffected and does not contain the authority reference.

Routing and itinerary belong to Pragma/Pragmatosis. [Information Unit Metadata][metadata]

### 1.4 Security and relationships

`SecurityMetadata` is an immutable value carrying the IU's security requirements. Themis-Krites is the authority responsible for adjudicating every proposed change, including strengthening, weakening and replacement. Calliope stores and carries a proposed value but does not classify requirements, make security-policy decisions or enforce the Krites disposition. This proposal establishes the Themis-Krites architectural boundary only; implementation of a Krites service is outside BL-0005. The existing Themis default-deny and enforcement obligations remain in force. [G01 A02][security]

`InformationMetadata.Relationships` holds the optional internal and external semantic links in `relationships : List<InformationUnitRelationship>`. Each entry has this shape:

```text
InformationUnitRelationship
├── type : RelationshipType
├── role : RelationshipRole
└── target : URI
```

`RelationshipType` has values `EXTERNAL_SYSTEM`, `EXTERNAL_ORGANISATION`, `INTERNAL_IU` and `INTERNAL_OTHER`. `RelationshipRole` has values `BASED_ON`, `ALTERNATE_REPRESENTATION`, `PART_OF` and `DERIVED_FROM`. The target URI supports URN-based persistent identities and a string representation. An `INTERNAL_IU` target identifies the IU by its UUID (for example, in a UUID URN), independently of its classification-derived name.

The creator or an authorised updater may supply or change these links. Mneme validates local references as applicable and preserves supplied remote references without guaranteeing the target's existence, availability or correctness. Any reciprocal link must be supplied explicitly. `Content.reference` separately locates externally persisted Content. Provenance records the derivation of a transformed or validated IU. [Information Unit Metadata][metadata] · [IU Framework Final.001 §§14–15][iu-final-001]

### 1.5 InformationQuality

`InformationQuality` is a distinct constituent of the IU and is unchanged by this revision. Its `owner : String` identifies the party responsible for the quality assessment. `InformationCoordinates.InformationOwner` describes governed logical ownership and can change through a new revision. The `grade` is `Anecdotal`, `Informational` or `Canonical`. Each entry in `certifications : Map<String, String>` maps a certifier to a certification.

The Integration Architect defines grading criteria for each Harmonia software release. Each IU receives a grade according to its origin and assurance under the applicable release criteria. All InformationQuality properties and certification entries are immutable for the IU's lifetime. Subsequent validation produces a new IU with a new UUID and provenance linking it to the source IU. `Canonical` is an information-quality grade independent of authoritative standing. The IU retains an explicit reference to the applicable governed information authority under AX-06. Grade and certifications remain independent of security criteria, integrity checks, cache state and durable persistence. [AX-06][authority] · [IU Framework Final.001 §15][iu-final-001]

### 1.6 Content and container

Each IU has one governed `InformationRepresentation`. Any transformation that produces a new representation creates a new IU with a new UUID and provenance, even when the representations are semantically equivalent. Governed revisions preserve the IU's representation.

`containerConcept` and `contentConcept` describe what the container and content represent. `containerSchema` identifies the applicable container schema. Every container has a concept. Required `containerMediaType` and `contentMediaType` string fields carry IANA media type identifiers separately from concepts and schemas; media types do not substitute for either concept or schema.

`contentSchema` identifies the Content structure, schema or profile. Applicable Topic entry versions carry its version semantics. Format and encoding retain their meanings under the applicable representation or interchange contract. The native payload instance version stays with the native Content where supplied. A media type may describe a concrete encoding such as JSON or XML without changing the schema Topic.

Concept and schema Topics are independent of the IU's classification. Use release-defined builders for governed entries and the existing Naming API to construct individual Topics. When Calliope imposes no additional container structure, use the exact qualified `RAW` path `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW`.

The FHIR R4 resource vocabulary uses the containment path `Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4.0.1=R4.Resource=<resource-name>`. `R4` is the recognised release designation and `4.0.1` its specific version; resource definitions have no independent versions. The common hierarchy is `Domain → Owner → Standard`; each governed standard defines its own subordinate containment structure and the level at which versioning applies. Containment is represented by the qualified Topic path, not a separate relationship model.

Generic profile-schema Topics are governed by the organisation and standard that define the profile. Begin with that authority's `Domain → Owner → Standard` path, preserve any additional standard-defined containment entries, and append `Profile[:version]=<profile-name>` directly to the resulting governing-standard Topic. For example, `Domain=Health.Owner=ExampleOrganisation.Standard:1.0=Clinical.Release:4=2026.Profile:2.1=ExampleProfile` uses a neutral illustrative owner and standard; it is not a release-defined vocabulary entry. Profile Topics are independent of any underlying FHIR or other resource and establish no relationship to one. The `InformationVocabulary.profileSchema` helper requires a `Domain → Owner → Standard` base and rejects additional `Resource` or `Profile` entries after that prefix; these restrictions apply only to that helper contract. They do not constrain the generic `Topic` model or prevent other valid standard-defined containment hierarchies from being constructed through the Naming API and used by the InformationUnit model. Do not restore the obsolete `System=Harmonia.Domain=Data...` schema path. Specific versions remain attached to the standard-defined entry they describe; Topic identity ignores versions, while the version-capable canonical rendering preserves them. `InformationVocabulary` supplies generic governing-standard and profile builders, and callers use the existing Naming API for other standard-defined intermediate entries.

Preserve native Content semantics and applicable native identifiers and versions. Content is portable as inline text, binary data or a reference. The logical model is independent of Java object graphs, interchange encoding and physical storage. [InformationRepresentation][representation] · [Serialization and Format Selection][serialization]

### 1.7 Content references and integrity

Calliope models `Content.reference` as the locator for externally persisted Content, opaque to Mneme. Exactly one of `Content.reference` or `Content.data` is populated. An IU can be constructed with inline `Content.data` alone, before a persistence reference exists. The Summary omits the `Content` constituent and projects the established locator, when available, from the single authoritative association for the IU and revision. The reference is absent from the Summary when no locator has been established.

The content provider supplies `size` in bytes, `checksum` and `checksumAlgorithm` for the actual Content bytes. The algorithm identifies how the digest was computed. The default digest algorithm is `SHA-256`. A digest for referenced Content describes the referenced bytes. Other `ContentDetail` values apply where meaningful. The PersistenceService determines how and when verification occurs under the applicable contract. Mneme and Mnemosyne impose no independent checksum verification requirement. If compression or encoding comes into scope, its ownership and the bytes covered by the integrity metadata require an explicit decision.

Authoritative persistence and applicable multi-site durability guarantees continue to apply. Summary registration persists the supplied reference without fetching or verifying external Content. Full reads retain the applicable Content materialisation obligation and the established pass-by-reference exception. [Mneme–Mnemosyne Interaction (FC-01–FC-05)][interaction] · [GovernedSummaryReader][summary]

## 2 Reconciliation with accepted contracts

Under FC-01 and FC-04, Mneme coordinates the governed persistence operation, the PersistenceService supplies the provider reference, and Mnemosyne establishes the authoritative association through `GovernedWriter`. A later persistence operation can establish that reference for an IU constructed with inline Content while preserving its governed information representation. `GovernedWriter` preserves the durable association among the reference, the Content and the Summary for the same IU revision. Materialising a referenced IU into a full view with `Content.data` leaves the association intact. FC-02 still requires the applicable materialisation attempt, with the existing pass-by-reference exception. FC-03 limits Summary search to the Summary's fields. FC-05 reference-only registration preserves supplied provider integrity details where available. [Mneme–Mnemosyne Interaction (FC-01–FC-05)][interaction] · [GovernedSummaryReader][summary]

[release]: ../../../architecture-release.json
[axioms]: ../../governance/architectural-axioms.md
[iu-final-001]: DRP-Information-Unit-Framework.Final.001.md
[iu-final-002]: ../evidence/1.1.25/001/before/accepted-input/DRP-Information-Unit-Framework.Final.002.md
[iu-class]: ../../continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md#architectural-content
[iu-business]: ../../continuum/business/business-object/10284034c8534b639284386b064f2c54.md#architectural-content
[identity]: DRP-Calliope-Identity-and-Classification-Framework.Final.001.000.md#2-a04--informationunit-identity-and-conceptfdn
[naming]: DRP-Topic-and-TopicMask.Final.003.md#7-consumer-boundaries
[interaction]: DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md
[store]: ../../continuum/application/application-function/9c680d56a6b943da8ff22f3264e19bc7.md#architectural-content
[metadata]: ../../continuum/application/data-object/c05b7e37278141488ecc90b9a9ef96eb.md#architectural-content
[representation]: ../../continuum/application/data-object/b9891b88150840ada3daefdec6f26c35.md#architectural-content
[revision]: DRP-Calliope-Lifecycle-Revision-and-Operational-State.Final.001.000.md#3-a15--data-version-and-managed-revision
[summary]: ../../continuum/application/application-service/2988247b75664896a7deb97b12b20c0a.md#architectural-content
[authority]: ../../continuum/motivation/principle/952c3b6148ce4fce94b69f99a0c2fec7.md#architectural-content
[security]: DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md#9-outstanding-qualifications
[package]: ../../../docs/design/DesignNote-Calliope-Initial-Model-Architecture.md#4-target-information-model-package-hierarchy
[granularity]: ../../../docs/design/DesignNote-Object-and-Class-Granularity.md
[immutability]: ../../../docs/design/DesignNote-Object-Immutability-and-Modification-Guidelines.md
[serialization]: ../../../docs/design/DesignNote-Serialization-and-Format-Selection.md
