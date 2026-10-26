> **Current EA operational succession (RAD-1.1.6):** This maintained contract
> is promoted under the Solution Architect's instruction to sever Legacy-RAD
> operational dependencies. Current sources and procedures are the
> [Continuum Representation](continuum-representation.md),
> [Incorporation Praxis](continuum-incorporation-praxis.md),
> [Archi workbench](../design/archimate/README.md) and
> [EA tooling](../tooling/README.md). Release-specific migration/adoption
> accounts and links labelled “Historical source” preserve decision provenance;
> they supply no current configuration, schema, source capture or authority.
> The promoted rules retain their existing precedence, qualifications and
> unresolved matters. Historical identity registers, RAD source adapters,
> publication slices and processing layouts are not current procedures.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Canonical Identity and Governed Naming Contract

This contract records the explicitly authorised establishment following the
accepted Architecture Release 1.0.6 and Increment 1 transport proof. It supersedes
earlier UUID-URN recommendations, EC-as-lifetime-handle terminology and structural
path terminology **for the new `canonicalId` and three naming Properties**. Frozen
registers, captured legacy fields and historical publication evidence retain their
original meaning and values. AX-04, AX-14 and AX-17 apply: reuse existing machinery,
preserve distinct semantic roles and retain missing authority explicitly.

## Current containment interpretation — 2026-10-20

The [Round-Trip Engineering Praxis](archimate-round-trip-engineering-praxis.md#common-interpretation-and-projection-rules)
clarifies contextual resolution: Home by default; Composition for contained
native Capability/Feature, BusinessObject, DataObject/Topic and Artifact.
Current/historical relationship selection precedes ancestry. Earlier broad
Composition wording is subject to that clarification; the Information naming
rules below still use Composition because all their native types are exceptions.
Canonical continuity and observable Exchange rebinding remain distinct.

## Architectural identity

Every governed architectural **element/resource**, including retained historical,
superseded, deprecated and non-current resources, owns exactly one `canonicalId`.
It is element metadata in the governed RADS source capture, not an identifier owned
by a publisher or primarily maintained in a separate identity registry.

The value is an opaque UUID, persisted as exactly 32 lowercase hexadecimal
characters without separators. Establishment uses standard random UUIDv4 allocation
(`uuid.uuid4().hex`), once. Persistence provides stability; regeneration does not
allocate or derive UUIDs from names, types, paths or other semantics.

Canonical IDs SHALL be globally unique across elements, immutable for the lifetime
of the architectural instance and non-reusable. Equal Canonical IDs identify the
same instance; different Canonical IDs identify different instances, even if names
are equal. Rename, retyping, relocation and lifecycle changes retain the ID unless
architectural adjudication determines a genuinely different replacement instance.
Retyping an existing ID is a significant architectural change, never a new-ID
allocation inferred by tooling. This contract does not implement change reconciliation.

An element retained as history retains its ID. Retained source and accepted source
history provide non-reuse evidence. Physical removal that would lose that evidence
is a governance issue requiring adjudication; tooling must fail rather than invent
a replacement registry, recycle an ID or silently assume deletion is harmless.

Open Exchange `identifier` values remain separate representation identities for
elements, relationships, Views and visual objects. They remain unchanged by this
establishment. No Canonical IDs are allocated to relationships, Views, nodes or
visual connections. The 14 frozen EC bindings remain unchanged historical/external
identifier aliases and provenance; EC strings are never UUID32 Canonical IDs.

## Three governed names

Names are governed resource attributes; none establishes architectural identity.

| Property | Meaning |
| --- | --- |
| `displayName` | Human-facing presentation name, with established formal qualification where applicable. Capability display names carry `[CT1]`, `[CT2]` or `[CT3]`; Features carry `[Feature]`. Information elements use `[BusinessObject]`, `[Topic]` or `[Artifact]` as governed below. Every governed element's native ArchiMate `Name` SHALL equal this value. |
| `friendlyName` | Developer-friendly qualified name. Every component is whitespace-free UpperCamelCase/PascalCase and omits formal architectural type/tier qualifiers. Only genuine governed context supplies `.` qualification. |
| `canonicalName` | The same PascalCase components and context, with formal governed type/tier qualification on each component. |

The universal published-name invariant is `ArchiMate Name == displayName`;
qualification applies only where governed. This supersedes the earlier provision
for preserving a different native Name in the governed result. Historical Names,
diagram labels and submitted evidence remain preserved as evidence; existing
discrepancies require separately authorised migration, not silent repair.
[Increment 2B](archimate-ingestion-publication-semantics.md#recognition-classification-and-published-names)
governs authorised qualifier recognition, compatible native types and Change
Review for unknown or ambiguous qualifiers.

For example, the naming forms may be:

```text
displayName   = [CT3] Person Identity
friendlyName  = HealthcareEntityInformationEnablement.ClientAdministration.PersonIdentity
canonicalName = CT1-BEC-HealthcareEntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentity
```

With no genuine governed grouping, the forms are local:

```text
friendlyName  = ThingOne
canonicalName = MOT-ThingOne
```

These illustrations do not rename existing records. Current governed labels supply
readable components; retained older short labels remain provenance. Component spelling
splits whitespace, hyphens, slashes and other punctuation into words, spells `&` as
`And`, recognises existing case boundaries and capitalises each word with the
remaining letters lowercase (`FHIR REST` → `FhirRest`). Words and digits are retained.
Names beginning with digits or requiring a different semantic spelling need an
explicit source-local `nameComponent` decision and authority; a validator cannot
invent that spelling. The Solution Architect explicitly established
`24/7 Availability` → `TwentyFourSevenAvailability` (friendly) and
`MOT-TwentyFourSevenAvailability` (canonical), retained with source-local decision
provenance. This lexical operation supplies no architectural ancestry.

The [cross-layer Composition invariant](architecture-modelling-strategy.md#cross-layer-semantic-containment-and-contextual-naming)
is authoritative for contextual naming: native Composition parent → child
establishes semantic containment, and its ancestry supplies the contextual path
for both names across all layers. Folder/View structures, lexical similarity,
proximity, responsibility and Home SHALL NOT create that ancestry. Required
missing, ambiguous or conflicting ancestry stops the dependent property.
The local forms above require sufficient semantic inputs; an unauthored parent
is not proof of an independently established root.

The earlier candidate derivation below is retained as provenance for unchanged
Business names and navigation; it does not override the later cross-layer rule.
The existing Capability/Feature/behaviour capture's `parent_key` and `owner_key`
establish qualified context, preserving each assertion's standing, except where
the explicitly governed [Home precedence](architecture-modelling-strategy.md#home-derived-naming-and-navigation)
applies. A Business Function, Process or Service with one established explicit
Home to a Feature SHALL use the Home's recorded semantic ancestry, the Home
Feature, and the local element for both qualified names. Home overrides any
differing responsibility context for naming only; it does not change responsibility.
Both names use the existing lexical derivation below, with formal qualifiers only
in `canonicalName`. `displayName` and `canonicalId` remain unchanged. Missing or
ambiguous Home leaves the existing context untouched. The existing
formal vocabulary supplies `CT1`/`CT2`/`CT3`, the existing `BEC`/`EC` category
qualification where applicable, and `FEAT`, `FN`, `SV`, `PR` terminal qualification.
Business Capability tokens retain the existing unadorned `CTn` tier qualification.
The retained historical Capability population uses its explicit accepted native
Composition ancestry and semantic tier/category Properties; no RAD crosswalk is
thereby adopted. Motivation records without an established architectural grouping
use local names and `MOT-` qualification. Subject headings, native definition codes,
Archi folders, View placement and presentation containment do not create grouping.
Native definition codes remain identifier provenance, rather than readable-name
components. Proposed context remains Proposed; naming does not approve placement.

`friendlyName` and `canonicalName` SHALL be persisted in the resource source and
validated against deterministic derivation from its governed naming inputs and
context. A mismatch fails validation; validation does not repair or overwrite it.
Current Home-derived resource fields and their prior-name provenance are persisted
in [Historical source: resource amendments](../../docs/markdown/governance/baseline/home-derived-metadata.json), while the original
captures and reconciliation history remain unchanged. A mismatch with explicit
Home derivation fails validation; responsibility cannot override Home.
Renames must preserve useful historical naming through the existing name-alias and
source-provenance mechanisms. Identifier aliases and name aliases remain distinct.
Existing name uniqueness rules and unresolved findings remain applicable.

### Native Product canonical-name vocabulary

**Solution Architect decision — 2026-10-19, authorised 1.0.8 reconciliation toward
RAD-1.0.9:** native ArchiMate `Product` is an authorised canonical-name element
class and SHALL use the formal type qualifier `PRODUCT`. All other canonical-name
derivation rules above apply unchanged, including lexical spelling and qualification
only by genuine governed context. This decision establishes no Product-specific
naming algorithm, identity mechanism, hierarchy rule or architectural interpretation.
Product retains native type `Product`; no additional Harmonia subtype is introduced.

This resolves the Product-vocabulary execution stop affecting run-001 CR-002–005.
The stop and subsequent decision are retained in the ingestion reconciliation audit.

### Information-object canonical-name vocabulary

**Historical Solution Architect adjudication — 2026-10-19, completion of unsigned
RAD-1.0.10:** native ArchiMate `BusinessObject` representing a governed Harmonia
conceptual/business information object uses the formal qualifier `BO`. Native
ArchiMate `DataObject` representing governed Harmonia application/logical
information within the InformationMap uses `TOPIC`. This does not assign TOPIC
to every DataObject irrespective of its governed meaning or scope.

Existing lexical and qualification rules otherwise apply unchanged. The 54
initial IA resources have local names and no established semantic ancestry.
Apply the formal qualifier to the same existing PascalCase component used by
friendlyName: `Practitioner BO` → `PractitionerBo` → `BO-PractitionerBo`, and
`Context Topic` → `ContextTopic` → `TOPIC-ContextTopic`. The governed Name suffix
remains part of that lexical component; this adds no new spelling algorithm.
Folders, Views and Business Home supply no IA semantic qualification.

This adjudication authorises completion of canonicalName on the 54 existing
resource-owned records where their naming inputs are sufficient. If a contextual
name depends on Composition ancestry awaiting Solution Architect authoring,
preserve that unresolved property until containment is ingested; never manufacture
context. IDs, Names and architectural meaning remain unchanged. The original
absent-property stop and source allocation history
remain preserved in the [Historical source: completion evidence](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion).
At that completion no `Artifact` / `ARTEFACT` canonical-name qualifier was
authorised. The following decision supersedes its naming rules for subsequent
processing; the RAD-1.0.10 capture and publication retain their historical suffixes
and lexical components for exact baseline replay.

### Information-element naming — machinery-validation decision, 2026-10-20

The Solution Architect establishes these independent abstraction levels:

| Abstraction | Native ArchiMate type and authored classification | Display convention | Canonical component qualifier |
| --- | --- | --- | --- |
| Business / Conceptual | `BusinessObject` | `[BusinessObject] ElementName` | `BO` |
| Application / Logical | `DataObject` with explicit `Specialization=Topic` (Exchange key `Specialization` or `specialization`) | `[Topic] ElementName` | `TOPIC` |
| Application / Logical | `DataObject` with explicit `Specialization=Class` | `[Class] ElementName` | `CLASS` |
| Logical Packaging | `Grouping` with explicit `Specialization=Package` | `[Package] ElementName` | `PKG` |
| Technology / Physical | `Artifact` | `[Artifact] ElementName` | `ARTIFACT` |

`[BusinessObject]` and `[Artifact]` repeat native types and establish no additional
Harmonia subtype. Topic is an explicit Harmonia specialisation of DataObject;
regenerate `harmoniaType=Topic` from that authored specialisation. An unclassified
DataObject, folder, display prefix or stale `harmoniaType` alone SHALL NOT establish
Topic. Unknown/multiple/incompatible authored specialisations or display qualifiers
remain unresolved.

The architectural local name is the native Name with the matching display prefix
removed. For native BusinessObject only, remove the superseded terminal ` BO`:
`Practitioner BO` becomes `[BusinessObject] Practitioner`, `Practitioner` and
`BO-Practitioner` for displayName, local friendlyName and local canonicalName.
Otherwise retain the words of an unqualified architectural Name; do not strip
arbitrary suffixes or guess semantic spelling. Matching display qualifiers are
omitted from lexical components. Prefix/suffix ambiguity, empty names and missing
spelling authority stop dependent derivation. Existing PascalCase lexical rules
apply; no new identity or word-equivalence rule is introduced.

Only explicit native Composition parent → child supplies contextual ancestry.
Apply each ancestor's qualifier to its own canonicalName component; friendlyName
uses the same components without qualifiers. For example, `[Topic] Pragma` →
Composition → `[Topic] Payload` yields `Pragma.Payload` and
`TOPIC-Pragma.TOPIC-Payload`. Package Groupings (`Specialization=Package`) supply
naming ancestry via Composition to child Packages and logical Classes:
`model` → `core` → `information` → `InformationUnit` yields
`model.core.information.InformationUnit` and
`PKG-Model.PKG-Core.PKG-Information.CLASS-InformationUnit`.
No ancestor gives a local candidate, explicitly
without asserting root completeness. Competing parents, duplicate assertions,
cycles, missing endpoints or unestablished ancestor naming inputs stop dependent
contextual properties; independent determinate properties may still be proposed.
Aggregation, Specialization, Realization, Association, unspecialised Grouping, folders and Views
supply no naming ancestry.

No BusinessObject, Topic or Artifact requires counterparts at the other levels.
Artifact vocabulary authorises naming machinery, not physical population, DataMap
reconciliation or cross-level mappings.

Authored native type, Name, explicit specialisation and Composition take precedence
over stale derived Properties. In machinery-validation runs, record submitted
claims separately from proposed `displayName`, `friendlyName`, `canonicalName`,
`archimateType` and applicable `harmoniaType` (including proposed removal of an
inapplicable derived subtype). Preserve canonical identity claims and representation
rebinding evidence. Never allocate missing canonical IDs in a run. Regeneration
does not accept semantics, metadata or root status; accepted-source publication
continues validating persisted fields without repair. The
[run lifecycle](architecture-release-policy.md#machinery-validation-runs--2026-10-20)
governs candidate standing and the release boundary.

## Legacy fields and types

Legacy `canonical_id` is structural-path evidence corresponding semantically to
`canonicalName`; it is never repurposed as `canonicalId`. Its old spelling and
historical structural aliases remain preserved as source evidence. The new
`canonicalName` deliberately adopts the corrected PascalCase component spelling.
Legacy `canonical_name` is short-name evidence, not automatically `displayName`.
Reviewed names, current names and retained aliases keep their source standing and
provenance; no discarded field is silently reclassified into another alias channel.

`archimateType` records the native type independently of the opaque ID. Existing
Feature specialisation uses native `Capability` and `harmoniaType=Feature`.
Topic specialisation is governed by the information-element decision above.

The [RAD-1.1.5 ARB adjudication](../design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation)
additionally authorises ImplementationGuideline as a native CourseOfAction
specialisation. Its display prefix is `[ImplementationGuideline]`; this supplies
no new formal canonicalName qualifier. Preserve the affected name as unresolved
under Q-01 until vocabulary is explicitly established. The same adjudication
authorises semantic historical/current correspondence while reserving historical
UUIDs and provenance; the initial unresolved-crosswalk account below remains
establishment history, not a prohibition on the authorised reconciliation.

## Establishment, publication and history

The existing RADS resource captures own the new metadata. Historical-reference
resources are retained as resource records in that same capture, explicitly without
a proven RAD crosswalk. A separate `bootstrap` command validates all existing ID
claims and all expected names before allocation. It allocates only absent IDs,
refuses malformed/duplicate/multiple claims, preserves existing valid values and
refuses to rerun an established migration. Complete source snapshots preserve the
establishment history and the prior release; they are evidence, not a primary ID
register. Interrupted establishment must be inspected without reallocating IDs.

Ordinary publication reads and validates source fields and accepted source history,
rejects lost/mutated IDs, and transports the Properties into Open Exchange. It has
no allocation step. Existing source capture, ancestry, Property, schema, alias,
binding, recovery and deterministic publication checks continue to apply. The
architecture release policy governs the accepted patch transition; publication
does not increment release authority. The accepted 1.0.6 publication remains intact.

For newly authored Archi elements, absence of machine-governed metadata is not by
itself an architectural defect. After acceptance and resolution of semantic
inputs, authorised reconciliation may supply identity and naming metadata under
this contract. This is distinct from the completed one-time establishment above
and from loss or mutation of established metadata. Publication still allocates
no identity. [Increment 2B](archimate-ingestion-publication-semantics.md#author-supplied-information-and-machine-governed-metadata)
governs that boundary and semantic naming context, including elements without
containment; unresolved inputs cannot be supplied from folders or Views.

The established publication still requires a **real no-edit Archi 5.10 import and
complete-model export**, followed by the Increment 1 comparator. Canonical ID
values/owner associations, the three names, applicable Harmonia types and element/
relationship identity/endpoints must survive exactly. Known evidence-text,
organisation, language, newline, style and ordering transformations remain visible.
There is no claim of whole-model lossless fidelity or new comparator normalisation.
Reconciliation / Increment 2 and presentation replay remain outside establishment.
