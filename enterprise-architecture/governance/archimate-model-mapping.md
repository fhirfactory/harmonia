<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Current ArchiMate mapping profile

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

The [representation contract](continuum-representation.md) governs native element placement, persisted UUID32 identity, explicit specialisations and direct projection from canonical Markdown. The [identity/naming contract](canonical-identity-and-naming.md) and [Round-Trip Praxis](archimate-round-trip-engineering-praxis.md) retain their existing precedence. Offline native schemas and maintained endpoint applicability are under current EA tooling/governance. The following current assertion and relationship rules are promoted from the original profile; its RAD capture/adoption/publication subsystem is historical.

## 2. Source and assertion contract

Publication must use reviewed assertions, not derive semantics from directory names, headings, ordinals, indentation, Mermaid arrow syntax, implementation classes or catalogue juxtaposition. A source document can contain approved definitions, candidates, examples, historical content and unresolved assertions together. Standing attaches to each assertion and relationship, not merely to the file.

Each proposed publication record must identify its RAD source document, stable source section/explicit anchor, assertion text or selected definition, source fingerprint and architectural standing. Source fingerprints detect drift; they do not confer approval. Snapshot/line numbers assist inspection but are not identity. Repeated descriptions of the same identity contribute provenance without generating duplicate elements.

The source must establish the construct's name, semantic kind and bounded responsibility. A direct type mapping does not establish an ID, description, owner, relationship or R1/R2 inclusion. Generation must check these independently. Candidate Domain05 content may later be represented with its accepted candidate standing clearly retained; it must not appear as the final Solution Architecture or MVP baseline.

## 5. Relationship profile and direction

The accepted [Architecture Modelling Strategy](architecture-modelling-strategy.md)
governs semantic compatibility of specialisations and explicit architectural
Home. Home is a directed native Association, specialised as `Home`, from an
element to its logical architectural home. It is independent of Realization,
structural containment and ownership. The accepted
[Home-derived naming/navigation decision](architecture-modelling-strategy.md#home-derived-naming-and-navigation)
records the earlier candidate projection. The later
[cross-layer Composition invariant](architecture-modelling-strategy.md#cross-layer-semantic-containment-and-contextual-naming)
makes Composition ancestry authoritative for contextual naming across all layers;
Home does not create or override that ancestry. Existing candidate navigation and
names remain preserved in this bounded IA completion. No relationship Name
is required to encode the specialisation. Publication SHALL replay explicit
reviewed Home assertions only; neither native edges nor presentation infer it.

Every relationship needs source evidence, typed endpoints, bounded meaning, direction, standing and identity. Validate both native ArchiMate legality and RAD meaning; XSD validation alone cannot establish either. The following are translation rules, not authority for new instances.

| RAD assertion | Proposed representation and direction | Boundary |
| --- | --- | --- |
| Explicit same-category Capability decomposition | Composition: parent → child | Existing Exchange edges preserved. New edges require exact reconciled endpoints and complete source evidence. |
| BEC CT3 frames Feature | Native Composition: CT3 Capability → Feature Capability; Feature retains Harmonia classification `Feature` | The [2026-10-19 metamodel rule](capability-feature-profile.md#bec-composition-hierarchy) defines accepted Composition as CT3 framing/semantic containment. Never CT1/CT2 → Feature; FT is not CT4. Instance acceptance, standing and complete ancestry remain separately governed. |
| Behaviour materially realises Capability/Feature | Realization: behaviour → Capability/Feature, when native endpoint rules permit | Ownership or shared subject matter alone does not establish realisation. |
| Capability/Feature owns behaviour or information responsibility | Source-qualified Association for responsibility; structured owner reference where supported | Capability is not an active structural actor. Do not use Assignment or Composition merely to render ownership. Approval of the precise representation is required before generating new responsibility edges. |
| Explicit performer fulfils Role / performs behaviour | Assignment: Actor → Role; Role/Actor → behaviour where supported and established | Qualified references are references, not new actors. No exhaustive participant matrix. |
| Function/Process fulfils exposed Service | Realization: behaviour → Service | Never one Service per Function. Exposure and consumption must be established. |
| Exposed Service used by consumer | Serving: provider Service → consuming behaviour/actor/component as permitted | No inference of ownership transfer. Native legality checked for the actual types. |
| BEC/EC materially enables a consumer (including B2.1) | Serving: enabling provider → enabled consumer, reversing `enabled_by` / `enterprise_enabled_by`, subject to native endpoint validity | [HCRL 1.1.5-002 ARB adjudication](../design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation) supersedes the earlier proposed Association default. Preserve source standing and predicate; no automatic runtime dependency or ownership transfer. |
| Requires / Consumes / Produces information | Initially distinct directed Associations: responsible Function → information, preserving those three source predicates | `Consumes` is semantic action, not deletion; Requires is not necessarily read. A later Access mapping needs separately established Read/Write semantics. Do not collapse Requires and Consumes into Read. |
| Established read/write of passive information | Access: active/behaviour → passive object with explicit accessType | Do not infer Write from information custody or Produces without the established representation contract. |
| Explicit transfer or causal progression | Flow: sender → receiver for actual transfer; Triggering: antecedent → consequent for established causality/order | Consumption, dependency and proximity do not imply transfer or causality. |
| Pragma / Pylai.Diadromí realises Metabolē | Realization: concrete DataObject → conceptual BusinessObject | Genuine architectural Realisation per Domain04; not type inheritance/shared manager/persistence. Element identity must still be established first. |
| Actual whole/part or subtype | Composition/Aggregation: whole → part; Specialization: specialised → general | Use only explicit meanings. System-map indentation, collection membership and application grouping are insufficient. |
| Motivation contributes to intent | Influence: source → affected motivation/strategy element, only where causal/contribution meaning and native endpoint legality fit | Co-governance and textual contribution may require labelled Association. No invented strength, Goal, Outcome or causal path; AX-07/08 have no causal outcome edges in the orientation source. |
| Decision governs construct | Source/provenance properties and governed decision documentation; Motivation representation only if it actually is a Principle/Requirement/Constraint | No native ADR element; no automatic decision-to-Requirement conversion. |

A labelled Association represents an established architectural relationship whose
stronger native meaning is not justified, under the
[ARB reconciliation rules](../design/review/1.1.5/hcrl-1.1.5-002.pending.md#arb-semantic-reconciliation).
This authorises a fallback for known relationship existence; an explicitly
unestablished relationship remains a finding. The original predicate and standing
remain available. Source status is not promoted by graph materialisation. Other
new specialised profiles still require their actual review authority.

## 9. Documentary content and ambiguity

Contents pages, reading paths, domain folders, metamodel class headings, examples, references, historical migration wrappers, review checklists, source keys, gap navigation IDs, counts, status dashboards and implementation module lists do not automatically become architecture elements. Metamodel categories describe how to model instances; they are not themselves all architectural instances. Keep suitable content as documentation, metadata, View annotation or a report. Exclude deliberately rejected/retired meanings from current architecture while retaining source history.

Escalate ambiguous type, identity, source standing, definition, endpoint, relation predicate, Feature ancestry, ID conflict or contradictory source. Report both assertions with evidence; do not choose by similar names, latest file date, structural symmetry or implementation convenience. Block only dependent publication records/slices, preserving the rest where independently authorised.

<a id="8-deterministic-publication-and-verification"></a>
## Deterministic publication and verification

Use the [current build and verification entry points](../tooling/README.md). Preserve canonical identities, native types, standing, relationships and authored Views; validate schemas and references, independently check naming and containment, and compare deterministic regeneration. Actual Archi transport, visual inspection and Solution Architect acceptance retain their distinct gates under the Round-Trip Praxis.
