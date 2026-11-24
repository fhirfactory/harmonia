# Design Review Proposal (DRP)

**DRP:** DRP-Information-Unit-Framework-001\
**Title:** Core Information Unit Topic and Logical Model\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Formalise the Harmonia **Information Unit** as a foundational reusable
model under the Calliope model namespace:

`model.core`

The proposal takes the already-established Information Architecture
definition of Information Unit and gives it explicit Topic, Package and
logical Class structure suitable for reuse by Harmonia model domains
including work packages, events, audit events, messaging,
workflow/execution constructs, managed information and system
interactions.

The implicit implementation namespace root `net.fhirfactory.harmonia` is
omitted from all architectural package names.

This DRP does not redefine Information Unit as a Mneme or Mnemosyne
construct. Mneme may manage Information Units and Mnemosyne may persist
authoritative representations of them, but neither owns the foundational
Information Unit model.

## 2. Existing Architectural Definition

The current governed architecture establishes:

> **An Information Unit is the governed boundary within which Harmonia
> associates a Governed Representation with its Management Context.**

An Information Unit is bounded and governed but is not necessarily
atomic. It may contain structured, composite or related information
according to its defined Information Unit structure.

The established conceptual partition is:

``` text
Information Unit
├── Identity
├── Metadata
├── Content
└── Context
```

These are conceptual partitions and do not require four implementation
objects, classes, database structures or serialised elements.

The architecture also establishes that:

-   Information Unit identity, represented-information identity and
    represented-entity identity are potentially distinct;
-   Unit Structure and Content Structure are distinct;
-   Content Structure and Content Format are distinct;
-   Context includes management concerns such as provenance, authority,
    version, lifecycle, temporal context, relationships and active
    generation;
-   Version and Active Generation are distinct semantic dimensions;
-   management acceptance does not transfer originating information
    authority;
-   security context is operational context and is not automatically
    persisted as Information Unit content;
-   the model is release-bound and evolves through explicit architecture
    rather than uncontrolled schema drift.

This DRP preserves those semantics.

## 3. Architectural Position

### 3.1 Package Root

The proposed package root is:

``` text
model
└── core
```

`model.core` contains only foundational model constructs intentionally
reusable by multiple otherwise independent Harmonia model domains.

Information Unit qualifies because it is a fundamental building block
used by higher-order constructs rather than belonging to one functional
subsystem.

Examples of downstream constructs expected to depend upon the core
Information Unit semantics include:

``` text
WorkPackage
AuditEvent
Event
Task
Message
SearchResultSet
ManagedInformationUnit
...
```

This list is illustrative and does not establish inheritance or
containment relationships.

### 3.2 Package Semantics

`Package` is the Harmonia specialisation of ArchiMate `Grouping`.

Containment is expressed by Composition:

``` text
Package «Grouping»
    ├── Composition → Package
    └── Composition → DataObject
                         ├── «Topic»
                         └── «Class»
```

Package containment establishes architectural organisation and qualified
model namespace. It does not by itself establish inheritance,
realisation, aggregation or semantic ownership between contained
DataObjects.

## 4. Proposed Package Structure

Initial proposed structure:

``` text
model
└── core
    └── information
        ├── Topic::InformationUnit
        ├── Topic::InformationUnitIdentity
        ├── Topic::InformationUnitMetadata
        ├── Topic::InformationUnitContent
        ├── Topic::InformationUnitContext
        │
        └── Class::InformationUnit
```

The extra `information` package beneath `core` is proposed to keep
`model.core` from becoming an undifferentiated collection while
retaining Information Unit as foundational architecture.

Whether the final qualified location should be
`model.core.InformationUnit` or `model.core.information.InformationUnit`
is a review matter. The semantic decision that Information Unit belongs
to the foundational `model.core` neighbourhood is the important part.

No deeper package hierarchy is proposed merely to mirror the four
conceptual partitions.

## 5. Topic Model

### 5.1 InformationUnit

**Topic:** `InformationUnit`

The governed boundary associating a Governed Representation with its
Management Context.

InformationUnit is the parent semantic construct. It is not a synonym
for:

-   the represented business/domain information;
-   a FHIR Resource;
-   a Java object;
-   a persistence entity;
-   a database record;
-   an API payload;
-   a cache entry;
-   a message envelope.

### 5.2 InformationUnitIdentity

**Topic:** `InformationUnitIdentity`

Identity of the Information Unit itself.

It SHALL remain distinguishable from:

1.  identity of the represented information;
2.  identity of any real-world entity represented by that information.

No identifier syntax, datatype or generation scheme is prescribed by
this Topic.

### 5.3 InformationUnitMetadata

**Topic:** `InformationUnitMetadata`

Characteristics required for Harmonia to recognise and manage the
Information Unit.

Metadata includes the semantic ability to identify the structure/type to
which the Unit conforms.

`InformationUnitMetadata` does not mean arbitrary technical metadata and
is not a dumping ground for infrastructure attributes.

### 5.4 InformationUnitContent

**Topic:** `InformationUnitContent`

The information represented by the Information Unit together with
representation characteristics necessary to interpret it.

Content semantics distinguish:

``` text
Content.Data
Content.Structure
Content.Format
```

`Content.Structure` identifies the semantic/structural definition of
represented content.

`Content.Format` identifies its concrete representation/encoding.

Structure and Format SHALL NOT be collapsed.

### 5.5 InformationUnitContext

**Topic:** `InformationUnitContext`

The management context governing an Information Unit over time.

The established architectural concerns include, where applicable:

-   provenance;
-   information authority;
-   version;
-   lifecycle;
-   temporal context;
-   relationships;
-   active generation.

These concerns are not asserted to be scalar fields or mandatory
embedded objects.

InformationUnitContext does not automatically include transient
operational security context, cache coordination state or implementation
plumbing.

## 6. Information Unit Structure

The four conceptual partitions are represented semantically as
composition of the Information Unit concept:

``` text
InformationUnit
├── InformationUnitIdentity
├── InformationUnitMetadata
├── InformationUnitContent
└── InformationUnitContext
```

This is architectural composition of meaning.

It SHALL NOT be interpreted as requiring the logical Class realisation
to contain four concrete child objects. The logical representation may
realise the same semantics differently provided the distinctions remain
observable and enforceable.

## 7. Metadata and Structural Identity

The current architecture distinguishes at least two structural
identities:

``` text
InformationUnit.Metadata.Structure
InformationUnit.Content.Structure
```

They answer different questions.

**Unit Structure** identifies the governed Information Unit
structure/type to which the Unit conforms.

**Content Structure** identifies the semantic/structural definition
governing the represented information.

Therefore:

``` text
UnitStructure ≠ ContentStructure
```

even where a particular implementation happens to use the same
identifier or model artefact for both.

The DRP proposes that these become explicit reusable Topics if the
existing Topic registry does not already contain semantically equivalent
constructs:

``` text
Topic::InformationUnitStructure
Topic::InformationContentStructure
Topic::InformationContentFormat
```

Their exact package placement is subject to registry reconciliation.

## 8. Version and Active Generation

The existing architecture establishes:

> **Version identifies a governed progression of an Information Unit's
> representation.**

> **Active Generation identifies a particular valid manifestation of an
> Information Unit established for active management or use.**

Therefore:

``` text
Version change
    = governed information progression

Active Generation change
    = different valid active manifestation
    ≠ necessarily information progression
```

Multiple Active Generations may derive from the same Version.

The DRP proposes explicit reusable Topics where not already present:

``` text
Topic::InformationUnitVersion
Topic::InformationUnitActiveGeneration
```

These are semantic concepts, not counters, timestamps, cache versions or
persistence implementation tokens.

## 9. Provenance, Authority, Security and Audit Boundaries

Information Unit must be able to carry or reference applicable
management semantics without absorbing ownership of those frameworks.

Accordingly:

``` text
InformationUnitContext
    → references applicable Provenance semantics
    → references applicable Information Authority semantics
    → references applicable lifecycle/temporal semantics
```

The Information Unit framework SHALL NOT redefine those external
concepts.

Similarly, security classification, compartmentalisation and information
labels may govern an Information Unit and/or its represented
information, but their semantics belong to the
Authentication/Authorisation and information-control architecture.

Audit evidence concerning an Information Unit belongs to the Audit
framework; it is not automatically InformationUnitContext.

This separation is deliberate:

``` text
Provenance ≠ Audit Evidence ≠ Security Context
```

A later DRP may formalise each referenced framework and this DRP should
then be updated to reference its canonical Topics.

## 10. Proposed Logical Class Model

The minimum foundational logical realisation is:

``` text
model.core.information.InformationUnit
```

The Class realises the governed Information Unit abstraction without
exposing persistence, transport or execution machinery.

Conceptually it must preserve access to the semantics of:

``` text
InformationUnit
    Identity
    Metadata
    Content
    Context
```

but this DRP does not prescribe four concrete fields/classes.

Candidate supporting logical value types may be introduced where they
materially preserve architectural distinctions, for example:

``` text
model.core.information.InformationUnitId
model.core.information.InformationUnitStructureId
model.core.information.ContentStructureId
model.core.information.ContentFormat
model.core.information.InformationUnitVersion
model.core.information.ActiveGeneration
```

These are **candidate logical Classes/value types**, not approved
implementation attributes.

The logical design should favour semantic types where confusing two
values would violate an architectural distinction---for example Unit
Structure versus Content Structure, or Version versus Active Generation.

## 11. Genericity and Content

Information Unit is intended to encapsulate arbitrary governed
represented information without requiring the core model to understand
the domain meaning of that information.

A logical realisation may therefore be generic or otherwise
content-type-neutral.

This DRP does not prescribe Java generics, inheritance, interfaces,
records or serialization techniques.

The core invariant is:

> Infrastructure and framework code may manage an Information Unit
> without needing to infer or reinterpret the domain semantics of its
> Content.

Domain-aware behaviour remains the responsibility of the appropriate
domain model and business/application logic.

## 12. Relationship to Managed Information

`InformationUnit` and `ManagedInformationUnit` are not automatically
synonymous.

Information Unit is the foundational governed information boundary.

Managed Information concerns Harmonia's explicit responsibility to
maintain governed representation and management context over time.

The precise logical relationship between:

``` text
InformationUnit
ManagedInformationUnit
ManagedInformationState
```

should be formalised without allowing Mneme implementation behaviour to
redefine the core Information Unit model.

This is a principal review item for this DRP.

## 13. Explicitly Not Prescribed

This proposal does not prescribe:

-   Java record/class/interface form;
-   JSON/XML representation;
-   FHIR mapping;
-   persistence schema;
-   cache representation;
-   transport envelope;
-   database identity;
-   UUID or other identifier format;
-   version numbering scheme;
-   active-generation numbering scheme;
-   four physical objects corresponding to
    Identity/Metadata/Content/Context;
-   whether metadata is embedded or referenced;
-   whether every management-context concern is durable;
-   Mneme or Mnemosyne implementation behaviour.

## 14. Proposed Topic Registry Changes

Subject to Architecture Review and reconciliation with existing Topics:

**Retain / formalise existing concept** - `InformationUnit`

**Proposed explicit Topics** - `InformationUnitIdentity` -
`InformationUnitMetadata` - `InformationUnitContent` -
`InformationUnitContext` - `InformationUnitStructure` -
`InformationContentStructure` - `InformationContentFormat` -
`InformationUnitVersion` - `InformationUnitActiveGeneration`

**Referenced rather than redefined** - Provenance - Information
Authority - lifecycle semantics - temporal context - information
security/classification/compartment semantics - audit semantics

Existing equivalent Topics SHALL be reused rather than duplicated.

## 15. Proposed Class Registry Changes

Subject to Architecture Review:

**Package** - `model.core` - candidate child package:
`model.core.information`

**Required logical Class** - `InformationUnit`

**Candidate supporting logical Classes/value types** -
`InformationUnitId` - `InformationUnitStructureId` -
`ContentStructureId` - `ContentFormat` - `InformationUnitVersion` -
`ActiveGeneration`

The Class registry should record semantic realisation relationships to
the corresponding Topics rather than infer them solely from matching
names.

## 16. Conformance Expectations

A conformant logical realisation:

-   preserves Information Unit as a governed boundary around
    representation and management context;
-   preserves Unit identity separately from
    represented-information/entity identity;
-   preserves Unit Structure separately from Content Structure;
-   preserves Content Structure separately from Format;
-   preserves Version separately from Active Generation;
-   permits structured/composite Content;
-   does not require infrastructure to comprehend domain Content
    semantics;
-   does not turn transient operational context into durable Information
    Unit content by default;
-   does not make Mneme, Mnemosyne, FHIR, persistence or Java
    representation part of the foundational definition.

## 17. Open Questions / Matters for Review

1.  Final package location: `model.core` directly or
    `model.core.information`.
2.  Whether Identity, Metadata, Content and Context warrant distinct
    Topics, or whether some remain semantic partitions of
    `InformationUnit`.
3.  Which of Unit Structure, Content Structure, Content Format, Version
    and Active Generation already have equivalent canonical Topics.
4.  Exact relationship between `InformationUnit`,
    `ManagedInformationUnit` and `ManagedInformationState`.
5.  Which metadata/context concepts should be embedded, referenced or
    merely associated at logical-model level.
6.  Whether Information Unit identity requires a dedicated logical
    semantic type.
7.  Which provenance/authority/security/audit concepts are referenced
    after their respective framework DRPs are formalised.

## 18. Review Decision

**Decision:** Pending\
**Decision authority:** Solution Architect / Architecture Review\
**Decision notes:**

(To be completed during review.)

## 19. Post-Decision Actions

If accepted:

1.  reconcile proposed Topics against the existing Topic registry;
2.  establish the accepted `model.core` Package hierarchy using Package
    Composition;
3.  add/rehome the accepted Topic DataObjects within that hierarchy;
4.  add the accepted logical Class DataObjects and semantic realisation
    relationships;
5.  update dependent DRPs---Authentication/Authorisation, Audit and
    Mneme-to-Mnemosyne---to reference the canonical Information Unit
    model;
6.  only then use the resulting canonical model as an implementation
    constraint.
