<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Canonical Identity and Governed Naming Contract

This contract records the explicitly authorised establishment following the
accepted Architecture Release 1.0.6 and Increment 1 transport proof. It supersedes
earlier UUID-URN recommendations, EC-as-lifetime-handle terminology and structural
path terminology **for the new `canonicalId` and three naming Properties**. Frozen
registers, captured legacy fields and historical publication evidence retain their
original meaning and values. AX-04, AX-14 and AX-17 apply: reuse existing machinery,
preserve distinct semantic roles and retain missing authority explicitly.

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
| `displayName` | Human-facing presentation name, with established formal qualification where applicable. Capability display names carry `[CT1]`, `[CT2]` or `[CT3]`; Features carry `[Feature]`. Every governed element's native ArchiMate `Name` SHALL equal this value. |
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
in [resource amendments](baseline/home-derived-metadata.json), while the original
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
No additional Harmonia semantic type is established by this contract.

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
