<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Canonical Enterprise Architecture Continuum representation

## Authority, scope and architectural unit

The Solution Architect's [RAD-1.1.5 Task](../design/evidence/1.1.5/002/before/ai-agent-tasks/20261021T092725Z-establish-and-apply-element-continuum.md)
authorises establishment and application of this representation. [HCRL 002](../design/review/1.1.5/hcrl-1.1.5-002.pending.md)
records the incorporated result and unresolved questions for holistic review.
The rejected 001 document-carrier strategy supplies no application authority.

The continuum's architectural unit is **one architectural element with one
resource-owned canonicalId and one canonical Markdown file**. The native ArchiMate
metamodel supplies its classification. Harmonia Feature specialises Capability;
Topic specialises DataObject; the ARB follow-up below establishes
ImplementationGuideline as a CourseOfAction specialisation. A source document, heading, software module, diagram
folder, information family or contextual bundle does not establish an element.
There is no miscellaneous or multi-concept document category in the continuum.

AX-03/04 require native semantics and architecture-led machinery. AX-08/09 require
meaningful provenance. AX-14 preserves semantic standing, currentness, placement,
identity, representation and approval as different matters. AX-15/17 require
explicit uncertainty without blocking independent authorised work. This mechanism
introduces no runtime architecture. The initial migration introduced no new
specialisation; the subsequent ARB decision explicitly adds ImplementationGuideline.

The [canonical identity/naming contract](../../docs/markdown/governance/canonical-identity-and-naming.md),
[modelling strategy](../../docs/markdown/governance/architecture-modelling-strategy.md),
[Round-Trip Praxis](../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md),
[Architecture Governance](architecture-governance.md) and
[Change Provenance](architecture-change-provenance.md) remain controlling in their
roles. An unresolved native classification stays in the originating review/source;
its source is preserved and its decision belongs in the applicable HCRL DR.

## Deterministic placement and template selection

[type-catalogue.json](continuum/type-catalogue.json) is the executable representation
profile. Its 60 concrete native element types are checked against the repository's
bundled ArchiMate schema. Type directories follow the native layers/aspects; physical
and implementation/migration concepts retain their native classification. Grouping
and Location are native concepts, not fallback document classifications. Empty
directories need not be committed: the profile determines their location before
the first element is authored. The complete supported type list is in that profile.

Examples:

```text
enterprise-architecture/continuum/
  motivation/principle/<canonicalId>.md
  motivation/requirement/<canonicalId>.md
  strategy/capability/<canonicalId>.md
  strategy/capability/feature/<canonicalId>.md
  strategy/course-of-action/<canonicalId>.md
  business/business-object/<canonicalId>.md
  business/business-role/<canonicalId>.md
  application/application-component/<canonicalId>.md
  application/data-object/<canonicalId>.md
  application/data-object/topic/<canonicalId>.md
  technology/artifact/<canonicalId>.md
```

The UUID filename is a locator derived from an already allocated identity. It is
never the means by which identity is inferred or allocated. Renaming an element
does not rename its identity. Retyping requires architectural authority; changing
the directory cannot supply that decision. No parent/child relationship follows
from directory placement. Specialisations occupy their native concept's directory.

Use one [base representation](continuum/element.md), supplemented by the native
type and specialisation constraints in the profile. This avoids sixty divergent
schemas while retaining exact native typing. The template command materialises
the applicable profile:

```bash
python3 scripts/architecture_continuum.py template --type Principle
python3 scripts/architecture_continuum.py template --type Capability --specialization Feature
python3 scripts/architecture_continuum.py template --type DataObject --specialization Topic
```

This is representation selection, not authority to create a construct. A Feature
needs an established Capability-compatible responsibility and its actual governed
framing/standing. Topic requires explicit Topic classification; neither a bracketed
label nor an ordinary DataObject is enough. BusinessObject and Artifact are native
types; their display prefixes do not create Harmonia specialisations. Home is a
directed Association specialisation and is stored as an assertion, not an element.

## Resource fields and Markdown content

A file starts with JSON front matter between `---` lines. JSON is used as a strict
subset of YAML so ordinary Markdown consumers and standard-library machinery can
handle the same representation. Duplicate keys are rejected. `schemaVersion`
versions this serialization only; it creates no architecture release or identity.

Required fields are:

| Field | Meaning |
| --- | --- |
| `canonicalId` | Existing resource-owned UUID32, or one newly authorised UUIDv4 allocation. |
| `archimateType` | Exact native type in the reference profile. |
| `specialization` | `Feature`, `Topic`, or null; explicit classification evidence is required. |
| `displayName`, `friendlyName`, `canonicalName` | Governed names, independent of identity. A genuinely unresolved dependent property is null with an explicit reason. |
| `standing` | Source semantic standing and currentness independently; placement, structural/name status and other qualifications where applicable. |
| `identity` | Preservation/allocation authority, relevant continuity and migration decision. |
| `naming` | Governing derivation/retained-source evidence and contextual limits. |
| `provenance` | Recoverable source editions, exact selectors and the role/standing of each contribution. |
| `sourceBindings` | Explicit source-to-instance correspondence, never a global architectural identifier or lexical-match rule. |
| `exchange` | Separate existing or governed derived representation identity and qualified transport properties. |
| `relationships` | Explicit outgoing native assertions with separate identity, direction, endpoints, standing and provenance. |
| `unresolvedProperties` | Specific dependent properties for which authority/inputs are insufficient, with HCRL traceability. |

The Markdown heading equals displayName. `## Architectural content` introduces the
definition, responsibility, exclusions, qualified assertions and unresolved matters.
Source-qualified additional contributions retain their source standing. Existing
source history remains visibly historical. A component definition can describe
its relationships and boundaries with other elements; this does not make the
file a second canonical representation of those elements. References resolve to
their own canonical files where established.

Architectural explanation belongs in the Markdown body. Projection metadata is
subordinate; copied source JSON and old model properties are provenance, not a
second authoring mechanism. Qualified relationship assertions follow their actual
adjudication. The ARB follow-up below supplies native mapping authority for known
predicates; explicitly unestablished relationship existence remains a finding.

## Identity, names and standing

Preserve existing canonicalId before allocating anything. The former registry's
EC bindings, historical aliases, source keys, structural IDs and Exchange IDs
keep their original roles. UUIDs are never derived from names, types or paths.
The initial migration has an exact identity boundary and explicitly reconciles
the 50 accepted evolved records by their shared canonicalId. Historical reference
populations retain their separate identities and unestablished RAD correspondence.

Only an explicitly authorised creation/ingestion operation allocates UUIDv4 once.
Validation and publication allocate zero IDs. Retain tombstones for genuinely
retired architectural elements. IDs selected by a defective, unaccepted import
for text that is not an element are preserved in withdrawal/non-reuse evidence;
they are not fabricated historical architectural elements. The three withdrawn
Role-attribute selections discovered during application follow that rule.

The native ArchiMate Name must equal displayName. A prior differing native Name
is retained as representation history. This deterministic projection correction
does not change the resource's governed displayName or existing authored Views.

Use the existing PascalCase spelling, type vocabulary, Home/default and
type-selected Composition rules. Unestablished ancestry is not evidence of root
completeness. In particular, `Principle ABC` with sufficient local semantic inputs
can use `Abc` and `MOT-Abc`; Feature uses its existing FEAT and actual governed
context rules. Unknown context or a native type lacking an authorised formal-name
qualifier leaves only the affected property unresolved. The initial migration
does not invent Role, CourseOfAction or ApplicationComponent naming tokens.
[DR-5](../design/review/1.1.5/hcrl-1.1.5-002.pending.md#dr-5) records that vocabulary
question for the Solution Architect. It does not prevent identity/content ingestion.

Canonical, Established, Agreed, Accepted, Proposed, Candidate, historical,
superseded and source-specific qualifications retain their source meanings.
`currentness=current` never implies acceptance. Candidate elements are individually
represented in the continuum under the Task's explicit migration authority; the
candidate architecture's review and decision record remains under design/review.
This is not whole-candidate acceptance through directory placement.

## Source succession and governance

Successful ingestion replaces the source carrier's active architectural role for
the exact incorporated content. Preserve the original text/history and a bounded
supersession wrapper or explicit retired-source record with replacement references.
Do not call a source retired merely because one assertion was imported. Retain
unresolved sections with their original standing and source correspondence.
Governance/metamodel rules, navigation, review decisions and historical evidence
retain their actual roles; they need no invented architecture element to survive.

The source/element correspondence and corpus accounting are purpose-selected
migration evidence, not a competing architecture master index. Current authoring,
validation and publication read the canonical Markdown plus maintained configuration
and declared schemas/presentation inputs. They must operate without historical
processing workspaces, run-local code, local interaction logs or LaTeX model copies.

ArchiMate remains the architecture workbench and projection. Existing Views retain
their identities, contents, geometry and navigation. The maintained projection
template owns model properties, property definitions and presentation; it owns no
semantic elements or relationships. LaTeX publication consumes the continuum and
never supplies duplicate source elements. Historical releases remain historical.

Automated PASS demonstrates its stated checks only. Real Archi import/export,
visual review and Solution Architect acceptance remain distinct outcomes. The
resulting continuum is submitted for holistic review under HCRL 002; an agent
cannot confirm its own architectural closure.

## Applied validation and publication boundary

The root [MANIFEST.md](../continuum/MANIFEST.md) is a generated navigation/review
projection of every canonical element, authorised by the
[manifest follow-up Task](../design/evidence/1.1.5/002/before/ai-agent-tasks/20261021T101602Z-generate-continuum-review-manifest.md).
It owns no architectural meaning, identity or standing. Only this exact root file
and the existing `continuum/documentation/published/` subtree are excluded from
canonical element ingestion; arbitrary Markdown elsewhere still must validate as
an element. The manifest's presence or manual modification never alters projection
inputs. Its separate byte-for-byte staleness check detects missing or changed output.

`scripts/continuum_manifest.py` validates canonical inputs before generating the
manifest. Native layer/aspect groups and type order come from the governed type
catalogue. Within a type, rows sort by case-folded displayName, exact displayName
and canonicalId as the final tie-breaker; Feature/Topic remain under their native
types. All elements appear once, including historical and qualified populations.
The manifest exposes independent semantic standing/currentness, canonical names
(including unresolved values), identities, canonical paths, additional standing
qualifications and recorded unresolved properties. Literal source text is escaped
for Markdown tables. It allocates no identity and edits no canonical source.
It uses no wall-clock timestamp, historical capture or hidden input. The current
`make all` pipeline generates it; `continuum-check` verifies it without writing.

The base [element schema](continuum/element.schema.json) is constrained by the
[type catalogue](continuum/type-catalogue.json). Native type coverage is checked
against the maintained ArchiMate XSD. Validation checks UUID32 identity, exact
placement, required provenance, independent standing/currentness, unique source
bindings, representation identities, native endpoints, Home encoding, migration
continuity and withdrawn-allocation non-reuse. Qualified predicates check their
source owner, target identity, uniqueness and original qualification. They never
become native relationships merely because they validate.

Projection additionally uses the existing state-selected containment and information
naming machinery, offline XSD and graph/reference checks. Persisted legacy names
remain checked against their accepted expected values. Source-qualified Business
naming context is not retrospectively rewritten from current graph absence.
New `naming.inputs`, where supplied, use the existing `qualified_names` derivation;
local lexical inputs are checked with the shared spelling function. Missing formal
vocabulary remains an explicit unresolved property. Human review still establishes
that a semantic input or same-instance correspondence is architecturally correct.

The [workbench adapter](../design/archimate/README.md) makes reviewed amendment and
explicit deferral repeatable. The [reproducibility check](../../scripts/verify_continuum_reproducibility.py)
regenerates the root manifest, XML, HTML, versioned Markdown index and LaTeX catalogue in a temporary checkout
with no old RAD captures, processing workspaces, copied LaTeX architecture or local
interaction logs. Shared code retains historical command support, but importing
its Exchange reader no longer loads historical source captures.

The one-time source ingestion scripts are bounded migration adapters. Their
allocated identity results are persisted. Routine regeneration never reruns UUID
allocation or prose extraction. `complete_rad_continuum_sources.py` uses exact
entry-revision source locators to preserve additional contributions;
`continuum_source_succession.py` and `finalize_continuum_succession.py` apply the
recorded bounded retirement. They read the entry Git edition, preserve original
bytes and use keyed correspondence. Do not use these as future classifiers.

## ARB semantic reconciliation follow-up

The [reviewed HCRL semantic reconciliation](../design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation)
and subsequent Apply instruction authorise semantic correspondence independently
of migration UUID equality. Identity reconciliation considers meaning and scope,
tier and parentage, governed canonical context, provenance and accepted evolution;
names and catalogue codes alone never establish identity. `identity.correspondence`
reserves a superseded historical resource and names its existing current successor;
the successor records the inverse `predecessorCanonicalIds`. Historical identities,
original definitions (including their absence), representation bindings and Views
remain recoverable. This does not allocate replacement UUIDs or promote candidates.
Strategic Digital Twin and application DigitalTwin retain separate layer meanings.

`qualifiedAssertions[].nativeMapping` records the actual ARB disposition alongside
the unchanged original predicate. Enabled-by families use provider → consumer
Serving in the reverse direction; the actual endpoint types must permit it.
Other predicates use the strongest source-justified, endpoint-valid relationship,
with Association where existence is known but stronger semantics are not established.
Source status/currentness and original predicate provenance travel with the edge.
An expressly unestablished Feature association has no asserted relationship
existence: neither Realization nor Association may manufacture it. The maintained
`native-relationships.json` checks actual native endpoint applicability separately
from XSD validity. Runtime semantics and Service-consumption are never invented.

ImplementationGuideline is the explicitly authorised Harmonia specialisation of
native CourseOfAction, placed under
`strategy/course-of-action/implementation-guideline/`, with display prefix
`[ImplementationGuideline]`. It records an authorised architectural or implementation
direction without recasting a selected means as an enduring Requirement. Its
formal canonicalName qualifier remains unresolved under Q-01; no token is invented.
Constraints and Requirements retain their native Motivation classifications.

Domain05 DesignNotes are governed capture/provenance artefacts. They remain at
their governed source paths after processing. Every substantive normative assertion
requires an explicit release disposition: an appropriate canonical element or
relationship, represented by an existing canonical assertion, superseded/rejected/
excluded, or genuinely unresolved. Preserve assertion modality, scope and later
U01–U19 corrections. Rationale, examples and diagrams may remain provenance;
implementation menus and deferred mechanisms do not become Requirements. A note
is fully processed only when every normative assertion has a disposition, with
source selectors and canonical/decision correspondence adequate for review.
Neither its filename nor its paragraph count establishes architectural completeness.

The bounded application adapters consume retained explicit correspondence plans;
routine validation/publication reads maintained canonical resources and configuration
without those plans, original DesignNotes, old captures or interaction logs.
