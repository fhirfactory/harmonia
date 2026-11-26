<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Calliope Naming

This document describes the supported software contract implemented under the
accepted [Backlog Item 0003 revision 000.004](../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.004.md)
and [Final.003 design](../enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md).
Those governed sources remain authoritative; this README is implementation
documentation, not an independent design decision.

## Package organization

Naming is organised by function under `net.fhirfactory.harmonia.model.core.naming`.
Only the first two packages form the supported API:

| Package | Responsibility | Types |
| --- | --- | --- |
| `naming` | Supported models and boundaries | Principal values `Topic` and `TopicMask`; dependent values `TopicEntry` and `TopicMaskEntry`; builders `TopicBuilder` and `TopicMaskBuilder`; the `NamingXmlCodec` facade; `NamingValidationException`; package-private `NamingBoundary` |
| `naming.valuesets` | Supported validation vocabulary | `NamingFailureCategory`, `NamingOperation`, `NamingFieldKind` |
| `naming.validation` | Field rules and bounds | `TopicFieldRules`, `NamingLimits`, `NamingRuleViolation` |
| `naming.canonical` | Canonical-text scanning and decoding | `CanonicalPathSyntax`, `TopicCanonicalParser`, `MaskExpressionParser`, `CanonicalEntryFields` |
| `naming.matching` | Pattern/version descriptions, evaluation, diagnostics | `FieldPattern`, `VersionConstraint`, `EntryCriterion`, `MatchCandidate`, `MaskEvaluator`, `MatchProbe`, `MatchDiagnosticSink`, `NamingDiagnostic`, `DiagnosticSeverity`, `MatchExitCategory`; package-private `NumericVersion` |
| `naming.interchange` | XML record format | `NamingXmlFormat`, `XmlEntryFields`, `XmlMaskRecord`; package-private `XmlRecordParser` |

Dependencies point one way. The four implementation packages depend only on
`validation` and `valuesets`, never on the models: parsers return decoded
fields, the XML format works on neutral records and the evaluator reads a
read-only candidate view. The models alone assemble completed entries, so
builders, canonical text and XML reconstruction all apply the same strict
validation with the actual operation and entry index. Implementation code
reports rejections as `NamingRuleViolation`; the package-private
`NamingBoundary` translates each one into `NamingValidationException`, so
callers only ever see the supported exception.

Java requires types used across packages to be public. The implementation
packages therefore contain public types that are **not** supported API: each
is documented as such, and `NamingStructureTest` enforces the per-package type
lists, the dependency direction and that no supported signature exposes an
implementation type. No JPMS module descriptor is used. Callers must depend
only on `naming` and `naming.valuesets`.

Calliope's `Topic` is an immutable ordered path of completed
`TopicEntry(qualifier, value, optional version)` values. A Topic's identity,
hash code and natural order use only the case-sensitive ordered qualifier/value
pairs; optional versions are opaque entry data and do not affect identity.
`TopicEntry.equals` compares the complete entry, including version presence and
spelling. Parent and ancestor projections retain the versions of entries they
contain. No runtime Topic identifier is generated.

## Construction and projections

`Topic.builder()` provides two construction modes. Sequential `addEntry` calls
append in order; indexed `addEntry(index, ...)` calls target final zero-based
positions and may arrive out of order. The first successful addition selects
the mode. Failed additions do not select a mode or mutate stored input. Indexed
`build()` requires contiguous positions starting at zero; a valid replacement
does not change depth. Missing/empty qualifiers supplied during Topic assembly
receive the deterministic `N<index>` qualifier. Strict canonical/XML
reconstruction never applies that default. Builders are caller-confined;
completed Topics and their entry lists are immutable snapshots.

The identity projection is `qualifier=value` entries joined by periods. The
version-capable projection is `qualifier[:version]=value`. Canonical parsing
requires completed, nonempty fields and accepts only the documented outer
escapes (`.`, `=`, `:`, and backslash). It strips exactly one outer escaping
layer. `getName` joins raw values with periods, `getFriendlyName` returns the
final raw value, and `getDisplayName` uses the final two values separated by a
hyphen (or just the sole value for a root). `getRelativeName` is the escaped
qualified representation of the final entry. Valid Unicode is preserved
without normalization.

## TopicMask selection

`TopicMask` is a neutral Boolean selector, not an authorization grant. Ordinary
masks evaluate ordered positions and by default require exact depth. Setting
`includeContainedElements(true)` accepts additional trailing Topic entries.
An explicit `TopicMask.all()` is a separate universal kind; ordinary `*` text
does not create that kind. Universal-only restrictions on expression and
containment accessors are documented on `TopicMask`.

Qualifier and value patterns support only these whole-field forms:

| Pattern | Meaning |
| --- | --- |
| `Patient` | Exact |
| `Patient*` | Prefix |
| `*Patient` | Suffix |
| `*Patient*` | Contains |
| `Patient*Record` | Prefix and suffix, without overlap |
| `*` | Any valid field |

Backslash escapes a literal asterisk or backslash. Before any other character,
or at the end, backslash remains literal. Other characters (including regex
punctuation) have no special meaning. Unsupported wildcard arrangements are
rejected while the entry is built. Patterns are classified once and matched
with bounded equality, prefix/suffix operations or substring search.

`TopicMaskEntry` factories accept raw patterns; `ofLiterals` and the literal
helpers first apply the completed Topic field rules and escape pattern
metacharacters. `TopicMaskBuilder` offers the same patterns/literals choices,
validates before mutation and returns immutable independent snapshots. It does
not generate Topic qualifiers. As with Topic builders, callers confine builder
use to one thread.

## Version constraints

Topic versions are opaque printable-ASCII text. A mask's omitted constraint
matches either an absent or present Topic version; a present-empty constraint
never matches. A nonempty constraint is either exact case-sensitive text or an
operator-led numeric expression. Numeric forms are `=v`, `<v`, `<=v`, `>v`,
`>=v`, or a lower/upper range such as `>=1.2,<2.0`. Ranges require a lower
comparison followed by an upper comparison and ordered endpoints; equal
endpoints are valid only when both sides are inclusive.

An initial backslash escapes one leading `=`, `<`, `>`, or backslash to select
exact text. Other leading uses are invalid. Exact comparisons preserve spelling;
numeric comparisons operate component-wise on bounded ASCII decimal spans,
without numeric conversion. Leading zeroes and absent trailing zero components
compare equivalently, while the stored Topic version and raw mask constraint
remain unchanged. Numeric syntax has no semantic-version, prerelease or
compatibility interpretation.

## XML interchange and validation

The only supported XML boundary is the stateless `NamingXmlCodec` in this
package: `readTopic(byte[])`, `writeTopic(Topic)`, `readTopicMask(byte[])`, and
`writeTopicMask(TopicMask)`. Topic entries and ordinary mask criteria retain
order. Mask records explicitly identify ordinary/universal kind and ordinary
containment. Raw qualifier/value patterns and optional version constraints use
canonical padded UTF-8 Base64; omitted and present-empty constraints remain
distinct. Output is deterministic UTF-8. Input is strict UTF-8, structurally
ordered and bounded; DTDs, external entities, duplicate/unexpected content,
unsupported encodings and malformed Base64 are rejected. This candidate also
accepts XML 1.0 only: an input declaration may be absent or declare version
`1.0`, while version `1.1` is rejected before XML parsing. This restriction is
not stated in accepted Final.003 or Backlog Item 0003 v000.004 and remains
subject to their governed reconciliation; this note does not amend either
authority.

Validation failures use `NamingValidationException` with the public
`NamingFailureCategory`, `NamingOperation`, `NamingFieldKind` and an
`OptionalInt` for a valid known entry index. Messages contain only safe
metadata, never raw values, patterns, versions, paths or payloads. Failed
builder operations preserve prior state; strict reconstruction does not publish
partial models.

The fixed limits are depth 16; qualifier 64 code points; Topic value and
version 256 and 64 code points respectively; each qualifier/value pattern 512
code points; Topic and ordinary-mask canonical text 32 KiB and 128 KiB UTF-8;
XML records 256 KiB UTF-8; numeric operands 64 ASCII characters and 32
components; and raw numeric ranges 133 ASCII characters. Completed models and
their compiled mask criteria are immutable and safe for concurrent reuse.

## Java serialization

`Topic`, `TopicEntry`, `TopicMask`, `TopicMaskEntry` and
`NamingValidationException` implement `java.io.Serializable`. The four models
use the serialization-proxy pattern: each writes a private proxy holding only
its raw text fields (and, for masks, the explicit kind and containment flag).
On deserialization the proxy rebuilds the model through the same strict
construction path as the builders and the canonical and XML readers, so a
crafted stream cannot produce an invalid model, compiled matching criteria are
recomputed rather than trusted, and `TopicMask.all()` deserializes to the
shared universal instance. A stream that bypasses a proxy, or a proxy with
invalid content, is rejected with an `InvalidObjectException` that names only
the type. Builders are caller-confined mutable helpers and are not
serializable. Java serialization does not replace the canonical text or XML
interchange contracts; callers that deserialize untrusted input remain
responsible for applying JDK deserialization filters.

## Diagnostics

Public `TopicMask.matches(Topic)` has no diagnostic-handler parameter. The
optional synchronous diagnostic sink and its bounded records belong to the
unsupported `naming.matching` package, are reachable only through
`TopicMask`'s package-private overloads, are invocation local, and are absent
on the public matching path. A match
returns only a Boolean and does not perform authorization, routing, persistence,
audit capture or logging; those responsibilities remain with their owning
Harmonia components.

# Calliope InformationUnit

This section describes the InformationUnit (IU) development candidate that
implements [Backlog Item 0005 revision 000.004](../docs/backlog/BacklogItem-0005-Calliope-InformationUnit.v000.004.md)
and the reconciled model in [Draft 002.007 §1](../enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.Draft.002.007.md).
The Solution Architect dispositions it applies are recorded in
[software HCRL 0.1.0/011](../enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-011.pending.md).
Those sources are authoritative; this README is implementation documentation.

## Packages

| Package | Responsibility | Principal types |
| --- | --- | --- |
| `information.entity` | The IU and its builders | `InformationUnit`, `InformationUnitBuilder`, `MinimumMetadataSuccessorBuilder` |
| `information.entity.datatypes` | Immutable constituents | `InformationQuality`; `InformationMetadata` with `PersistenceLifecycleMetadata`, `CacheLifecycleMetadata`, `OriginMetadata`, `InformationCoordinates`, `SecurityMetadata`, `Relationships`, `InformationUnitRelationship`; `InformationRepresentation` with `ContainerMetadata`, `ContentMetadata`, `ContentDetail`(`Builder`) and `Content`; `InformationModelException`; helper `ModelChecks` |
| `information.entity.valuesets` | Closed value sets | `RelationshipType`, `RelationshipRole`, `QualityGrade`, `InformationFailureCategory`, `FhirR4ResourceType`, `MediaTopLevelType` |
| `information.entity.factory` | Governed vocabulary builders | `InformationVocabulary` |

Dependencies point one way (`valuesets` <- `datatypes` <- `factory` <- root)
and use only the JDK and Calliope Naming. `InformationStructureTest` enforces
this, together with file size, licence headers, the absence of logging,
simulation, persistence and `Serializable` model types.

## Identity, name and classification

`id : UUID` is the sole instance identifier. `informationConcept : Topic`
replaces `ConceptFDN`/`informationProfile` and is the qualified value a Store
matches against its TopicMasks (MI-05). `getName()` is exactly
`informationConcept.getName() + id` with no separator, so qualifiers and entry
versions never appear in it. The name is a designation, never a relationship
target.

## Snapshots and controlled change

An `InformationUnit` object is an immutable snapshot of one governed revision.
Every change returns a new snapshot; the original is untouched.

- `reviseCoordinates` is an authorised relocation or ownership change: same
  UUID, new coordinates and a preserved earlier snapshot. The originating
  Managed Revision remains the expected revision for the next governed write;
  Mnemosyne establishes the successor after persistence succeeds. Disposition,
  legal hold and every other constituent carry forward.
- `reviseInformationAuthority` creates a governed revision under the same UUID
  while preserving the previous authority and snapshot. It retains the
  originating Managed Revision as the expected revision for the next write.
  Calliope carries the proposed authority; applicable governance authorizes it.
- `withPersistenceLifecycle` rejects a Managed Revision that regresses or
  disappears. `withCacheLifecycle` records Mneme's cache state and opaque
  Active Generation, which are independent of the Managed Revision.
- `withOrigin` and the `OriginMetadata.establish...` methods allow each unknown
  origin fact to be set once. A re-statement is idempotent only when it is
  identical, with Topics compared by their versioned canonical form.
- `withSecurity` accepts a proposed immutable value. Themis-Krites adjudicates
  every proposed security change, including strengthening, weakening and
  replacement. Calliope makes no security-policy decision and does not
  implement the Krites service.
- `withRelationships` and `withDescription` replace caller-supplied values.
- `deriveSuccessorAsClone(newId, role)` creates a complete new IU with a
  different UUID. It copies concept, description, quality, representation,
  authority, coordinates, origin, security and existing relationships, then
  appends an `INTERNAL_IU` provenance link to the source. Persistence and cache
  lifecycle facts are reinitialised for the new IU; all earlier snapshots stay
  unchanged. An inherited internal relationship may not target the new UUID.
- `deriveSuccessorWithMinimumMetadata(newId, role)` returns a
  `MinimumMetadataSuccessorBuilder` for a new IU. It inherits only origin and
  security, adds only the source-provenance relationship, and reinitialises
  persistence/cache lifecycle facts. The caller supplies concept, description,
  quality, authority and representation; coordinates default to unplaced and
  can be supplied. The builder does not expose setters for inherited origin,
  security or provenance, and a failed `build()` can be corrected and retried.

No operation replaces an IU's UUID, concept, quality or representation.

## Metadata, authority and quality

`InformationMetadata.initial(authority)` initialises every container.
`informationAuthority : Topic` is the explicit AX-06 authority reference and
is required. It is deliberately separate from `InformationQuality`, whose
`owner`, `grade` (`ANECDOTAL`, `INFORMATIONAL`, `CANONICAL`) and
`certifications` are immutable for the IU's lifetime. A `CANONICAL` grade is
not authoritative standing.

## Representation and Content

Each IU has exactly one `InformationRepresentation`. `ContentMetadata` keeps
three things apart. `contentConcept` says what the Content is. `contentSchema`
gives its structure, with versions carried as Topic entry versions.
`containerMediaType` and `contentMediaType` are required IANA media type
identifiers, stored separately from concepts and schemas. They do not create a
schema or content-concept Topic. `Content` is created with `inline`,
`inlineText` or `reference`, so both-carrier and neither-carrier states cannot
exist. Byte arrays are copied in and out. `ContentDetail` carries provider-supplied size,
language, dimensions, frames, duration, pages and a checksum. A checksum
always travels with its algorithm; `DEFAULT_CHECKSUM_ALGORITHM` is
`SHA-256`. Calliope never computes, verifies or fetches Content.

## Vocabulary

`InformationVocabulary` builds standard-defined qualified Topic paths. The
shared hierarchy is `Domain → Owner → Standard`; each standard defines its own
subordinate containment and versioning levels. Containment is represented by
the path itself, not a separate relationship model.

| Purpose | Path (unescaped) |
| --- | --- |
| Container schema when Calliope imposes no additional structure | `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW` |
| Content schema when no distinct schema is asserted | `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.ContentSchema=RAW` |
| FHIR R4 resource schema | `Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4.0.1=R4.Resource=<type>` for the 146 `FhirR4ResourceType` values |
| Generic profile schema | The caller's `Domain → Owner → Standard` path, any standard-defined intermediate entries, then `Profile[:version]=<name>` |

`R4` is the recognized FHIR release designation and `4.0.1` its specific
version; resource definitions have no independent versions. Media types are
stored only in their dedicated fields, never as content concepts or schemas.
`InformationVocabulary.governingStandard(domain, owner, standard, version)`
builds the common prefix, and `profileSchema(standardTopic, name, version)`
appends a generic profile. The governing owner and standard are supplied by the
profile's authority; profiles do not use the obsolete Harmonia data-schema path
or require a FHIR-specific owner. A standard may add its own intermediate
containment entries before the profile. The `Domain → Owner → Standard` prefix
and the rejection of `Resource` or `Profile` entries are restrictions of the
`InformationVocabulary.profileSchema` helper only; they do not constrain the
generic Topic model or other valid standard-defined hierarchies. A profile
Topic is independent of any underlying resource and carries no implicit
relationship to one. Versions remain on the entry whose semantics they
describe; Topic equality ignores versions while the canonical versioned form
preserves them.

## Failures and diagnostics

Rejections throw `InformationModelException`, which carries a
`InformationFailureCategory` and a structural field label, never the rejected
value. Each rejection happens before any state changes, and a failed
`InformationUnitBuilder.build()` keeps every input so the call can be retried.
`toString()` methods expose only the IU UUID, container and content
classification/schema Topics, enum values, counts or presence flags.
Payloads, locators, relationship targets, descriptions, owners, certifiers
and security labels never appear. The model does no logging, persistence,
caching, authorisation, reference resolution or serialisation.