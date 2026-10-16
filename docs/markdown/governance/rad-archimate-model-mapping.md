<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Harmonia RAD → ArchiMate Model Mapping

**Governed publication profile installed in the authorised release bundle, 2026-10-16.** The [release authority](../../../architecture-release.json) and [release policy](architecture-release-policy.md) govern this change set. Direct mappings below describe representation, not new architecture. Rows requiring adjudication grant no generation authority; installation does not resolve their architectural findings.

## 1. Purpose and authority

Harmonia has one common semantic architecture model. ArchiMate provides its visual, navigable and machine-processable representation; the RAD remains the authoritative explanatory and governed documentation. A semantic element exists once, independently of the Views in which it appears. The model should progressively explain Strategy → Capability → Behaviour → Information → Application → Technology, including the motivation and governance of those constructs. Missing traceability remains a finding.

The [Architectural Axioms](architectural-axioms.md), repository [AGENTS.md](../../../AGENTS.md), applicable accepted decisions and source definitions govern. The [Architecture Completion Plan](../architecture-completion-plan.md) defines the current domain standing and completion gates. Legacy documentation, implementation, historical reports and generated publications cannot establish missing architecture or override RADS. Reconciliation into RADS requires the existing review and section-preservation rules; this profile does not authorise that migration.

The user's decision establishes the model's publication purpose. It does not approve all possible ArchiMate interpretations or resolve existing source conflicts. The [mapping catalogue](rad-archimate-mapping-catalogue.md), [coverage assessment](rad-archimate-coverage-assessment.md) and [publication plan](rad-archimate-publication-plan.md) qualify this profile. Proposed review keys in those documents are document navigation labels, never semantic identities.

### Material axiom consistency

| Authority | Consequence for publication |
| --- | --- |
| AX-01–04 | Preserve health-information meaning, native standards representations and responsibility independent of technology machinery. No FHIR/class-to-concept identity inference. |
| AX-05/11/12 | Keep Mneme managed active access and Mnemosyne authoritative durable establishment distinct; retain developer-facing semantics without exposing machinery as architecture. |
| AX-06/07 | Information authority, custody, authentication, authorisation and policy enforcement remain distinct. Security context is not automatically persisted resource content. |
| AX-08/09 | Provenance, audit, assurance and telemetry retain their separate meanings and retention boundaries. A source link in this model is publication provenance, not runtime audit evidence. |
| AX-10/14/15 | Preserve failure, degradation, uncertain outcome and active/durable version distinctions; report unresolved publication outcomes explicitly. |
| AX-13 | Do not infer internal authority from external exchange or extend management beyond egress. ArchiMate publication itself introduces no new runtime authority. |
| AX-16/18 | Coordination and information management do not confer clinical/domain authority. Digital Twin information, coordination and execution projections retain their domain distinctions. |
| AX-17 | Missing IDs, ancestry, relationships, definitions and adjudications are explicit. No similar-name merges, diagram-driven edges or conventional architecture completion. |

## 2. Source and assertion contract

Publication must use reviewed assertions, not derive semantics from directory names, headings, ordinals, indentation, Mermaid arrow syntax, implementation classes or catalogue juxtaposition. A source document can contain approved definitions, candidates, examples, historical content and unresolved assertions together. Standing attaches to each assertion and relationship, not merely to the file.

Each proposed publication record must identify its RAD source document, stable source section/explicit anchor, assertion text or selected definition, source fingerprint and architectural standing. Source fingerprints detect drift; they do not confer approval. Snapshot/line numbers assist inspection but are not identity. Repeated descriptions of the same identity contribute provenance without generating duplicate elements.

The source must establish the construct's name, semantic kind and bounded responsibility. A direct type mapping does not establish an ID, description, owner, relationship or R1/R2 inclusion. Generation must check these independently. Candidate Domain05 content may later be represented with its accepted candidate standing clearly retained; it must not appear as the final Solution Architecture or MVP baseline.

## 3. Mapping and identity rules

Use native ArchiMate concepts only where they preserve Harmonia meaning. The catalogue supplies the proposed type and relationship rules. Each mapping is Direct, Derived but deterministic, Property/metadata, Documentation only, Requires architectural adjudication, or Not represented. Every non-direct row explains the representation decision or the missing decision. Unsupported native types must not be approximated silently.

Existing governed identifiers and retained aliases must be preserved verbatim. Canonical IDs, approved local IDs, documentary navigation keys and Exchange XML identifiers occupy different roles. In particular:

- Preserve all 228 Capability XML identifiers and all 189 Composition identifiers in the proven Exchange baseline. Preserve its labels, properties, hierarchy, model identifier and existing Views. These identifiers are established publication identities, not evidence of a repository crosswalk.
- Exact EC-01–EC-14 Reference IDs resolve to the authoritative Enterprise catalogue. Labels validate source drift; they are not mapping authority. The other 214 baseline elements have no demonstrated RAD identity crosswalk and must remain unresolved.
- Domain03 §8 continues to govern typed structural Canonical IDs, direct ID aliases, relocation, non-reuse and Feature ancestry. The [Governed Architecture Identity model](architecture-identity/model.md) distinguishes those responsibility-context identifiers from a persistent Canonical Architecture ID. An approved relocation changes the structural path while the lifetime handle and semantic model object persist. The capture's `canonical_id` field keeps its existing meaning; no UUID is placed there.
- The [common identity register](architecture-identity/registry.json) and its [schema](architecture-identity/registry.schema.json) define exact binding, provenance and lifecycle rules. Established empty in Release 1.0.2, it adopts only EC-01–EC-14 under the explicit [Release 1.0.3 decision](architecture-identity/adoption-1.0.3.md), with existing locked semantic IDs and no new allocation. Existing EC/AX/REQ-FND/CST-EXT/COA/SR/VS definition IDs are retained native identities, not renamed from labels. Future allocation is recommended to use opaque persistent UUIDv4 URNs after explicit authorisation.
- Existing ArchiMate semantic IDs remain locked. For a future approved identity without an existing binding, the identity model pins UUIDv5 namespace/key derivation independent of type, tier, label, document and View. Bind that result once and validate it against accepted history. Never recompute an existing locked UUID from an EC ID or structural path. Collisions block dependent publication; generators do not salt or retry identity.
- Existing stable navigation keys may locate evidence but must not become Canonical IDs or implicit aliases. Missing canonical identity does not invalidate established architecture; it prevents automatic semantic allocation until an explicit publication crosswalk is authorised.
- Name changes, another View, changed coordinates, section movement and release increments never create a new semantic object. Aliases resolve directly to the current object and cannot be reused for another identity.

Relationship identity must use an established relationship ID where available. Future allocations require a reviewed assertion/crosswalk key, relationship type, endpoint identities and distinct assertion discriminator where material; no label-based keys. Repeated evidence of one relationship must not create duplicate edges. Changes of meaning or endpoints require review and explicit identity history.

Canonical Architecture ID, Domain03 structural Canonical ID, Source/Capture ID,
Navigation/Register ID, ArchiMate Semantic ID, ArchiMate Visual ID and Implementation
ID retain their separate roles under the identity model. C/F/D/M/CDG navigation,
local FEAT tokens and HCM/LaTeX locks are not automatic canonical crosswalks.
CM-R05 remains mandatory; unique IDs do not waive Capability name uniqueness.
See the [identity inventory and collision report](architecture-identity/inventory.md)
for coverage, unresolved cases and proposed migration candidates before allocation.

Visual object identity is separate: retain the existing capability publisher's UUIDv5 namespace and model/View/semantic-object keys. Visual connections additionally use their visual endpoints. Generated visual IDs never substitute for semantic element/relationship IDs. Every capability node uses `elementRef`; every connection uses `relationshipRef` and View-local visual source/target IDs.

## 4. Documentation and properties

**Name = governed name. Documentation = governed definition/description. Properties = structured metadata with source authority.** Preserve legacy baseline labels even where EC prefixes differ from canonical names; the current slice does not rename them.

Select the authoritative definition with its material qualifications, exclusions and authority boundaries. Do not generate a description from a heading, summary count, related component name, LLM interpretation or incomplete navigation summary. If there is no description, report the gap and leave Documentation absent. A missing summary in the JSON capture is a review lead, not proof that RAD prose lacks a definition.

For the first slice, copy the entire EC section body verbatim, including EC-12/14 boundary qualifications and EC-14 non-allocation/behavioural-sequence qualifications. Preserve Markdown as text rather than paraphrasing it. The extraction boundary is the next EC heading or level-two catalogue section. Publication provenance is recorded in the explicit documentation manifest and validation report; this slice changes no existing semantic properties. Rich-text rendering/relative links in Archi require visual review.

Where established and applicable, future structured properties may carry existing RAD identifier/aliases, construct kind, architecture domain/category, Capability Tier or FT classification, status, release scope, source document/section and architectural standing. Include only source-governed values. A content hash is publication evidence, not invented architectural metadata. Unknown tier, release scope or status must not be supplied by default or from a directory. Publication release, architecture release scope, Maven version, native standards version, authoritative information version and active-state generation remain distinct.

## 5. Relationship profile and direction

Every relationship needs source evidence, typed endpoints, bounded meaning, direction, standing and identity. Validate both native ArchiMate legality and RAD meaning; XSD validation alone cannot establish either. The following are translation rules, not authority for new instances.

| RAD assertion | Proposed representation and direction | Boundary |
| --- | --- | --- |
| Explicit same-category Capability decomposition | Composition: parent → child | Existing Exchange edges preserved. New edges require exact reconciled endpoints and complete source evidence. |
| BEC CT3 frames Feature | Capability-typed Feature; source framing retained pending native edge adjudication | Never CT1/CT2 → Feature; FT is not CT4. Do not equate framing automatically to Composition or native Specialization. |
| Behaviour materially realises Capability/Feature | Realization: behaviour → Capability/Feature, when native endpoint rules permit | Ownership or shared subject matter alone does not establish realisation. |
| Capability/Feature owns behaviour or information responsibility | Source-qualified Association for responsibility; structured owner reference where supported | Capability is not an active structural actor. Do not use Assignment or Composition merely to render ownership. Approval of the precise representation is required before generating new responsibility edges. |
| Explicit performer fulfils Role / performs behaviour | Assignment: Actor → Role; Role/Actor → behaviour where supported and established | Qualified references are references, not new actors. No exhaustive participant matrix. |
| Function/Process fulfils exposed Service | Realization: behaviour → Service | Never one Service per Function. Exposure and consumption must be established. |
| Exposed Service used by consumer | Serving: provider Service → consuming behaviour/actor/component as permitted | No inference of ownership transfer. Native legality checked for the actual types. |
| BEC materially enables BC (including B2.1) | Directed Association: BEC → BC, labelled material enablement; source records its inverse `enabled-by` | Proposed conservative encoding avoids mandatory runtime Serving semantics. No automatic application dependency. Requires profile review before new edges. |
| Requires / Consumes / Produces information | Initially distinct directed Associations: responsible Function → information, preserving those three source predicates | `Consumes` is semantic action, not deletion; Requires is not necessarily read. A later Access mapping needs separately established Read/Write semantics. Do not collapse Requires and Consumes into Read. |
| Established read/write of passive information | Access: active/behaviour → passive object with explicit accessType | Do not infer Write from information custody or Produces without the established representation contract. |
| Explicit transfer or causal progression | Flow: sender → receiver for actual transfer; Triggering: antecedent → consequent for established causality/order | Consumption, dependency and proximity do not imply transfer or causality. |
| Pragma / Pylai.Diadromí realises Metabolē | Realization: concrete DataObject → conceptual BusinessObject | Genuine architectural Realisation per Domain04; not type inheritance/shared manager/persistence. Element identity must still be established first. |
| Actual whole/part or subtype | Composition/Aggregation: whole → part; Specialization: specialised → general | Use only explicit meanings. System-map indentation, collection membership and application grouping are insufficient. |
| Motivation contributes to intent | Influence: source → affected motivation/strategy element, only where causal/contribution meaning and native endpoint legality fit | Co-governance and textual contribution may require labelled Association. No invented strength, Goal, Outcome or causal path; AX-07/08 have no causal outcome edges in the orientation source. |
| Decision governs construct | Source/provenance properties and governed decision documentation; Motivation representation only if it actually is a Principle/Requirement/Constraint | No native ADR element; no automatic decision-to-Requirement conversion. |

A labelled Association is proposed only for an explicitly established association whose specific Harmonia predicate must be retained. It is never a fallback for missing relationships or a way to bypass adjudication. New specialised association profiles above require review before their generation slice. Unresolved relations remain report entries, never ordinary graph edges labelled as fact.

## 6. Traceability and navigation

Apply [Domain03→04 traceability](../04-information-architecture/traceability/domain03-traceability.md) and [Domain05 Step B](../05-information-systems-architecture/traceability/step-b-method.md): use the Business and Business Enabling dual anchors; preserve Function-level Requires/Consumes/Produces; distinguish information responsibility, management, custody and originating authority. Not every trace must visit every node. A direct derived Capability→Component navigation summary must retain its underlying established path and derivation provenance and must not replace that reasoning.

A graph browser should eventually traverse incoming and outgoing semantic relationships from Ponos, Mneme, Calliope or Iris and reach their motivation, capability support, behaviour, information, collaborators and supporting technology. It must also show the boundary of established knowledge. The current B2.1 result establishes seven capability-enablement edges and no software allocation. The six worked-example Functions do not allocate themselves to six components. EC-14 has no component allocation. Do not fabricate these links for a complete-looking component View.

## 7. View profile

The explicitly authorised Release 1.0.4 applies the
[Harmonia Archi Navigation & View Convention](archi-navigation-and-view-convention.md)
to this existing capability publication. It changes model-tree organisations,
moves existing View navigation entries and adds context/immediate-child Views.
All semantic records, 14 EC bindings, Documentation and original View contents
remain unchanged. The original semantic lock remains immutable; navigation is
validated separately from semantic architecture. This bounded presentation
change authorises no additional RAD constructs or identity reconciliation.

Views are disposable deterministic projections of the shared model, never architectural sources. Preserve the four capability Views and original import diagnostic. Retain their semantic/visual IDs. New Views need a bounded question, selected semantic identities, standing filter, relationship subset and deterministic layout. Each source/target visual node must represent the semantic relationship's actual endpoints. Do not add semantic relationships for geometry.

| Expected View | Intended question / boundary |
| --- | --- |
| Harmonia Architecture Landscape | What governed capabilities, responsibilities and layers exist, and where is coverage incomplete? Entry point to the common model. |
| Strategy/Motivation | What stakeholder concerns, intent, principles, requirements and strategic responses are explicitly related? |
| Existing four Capability Views | Entry landscape and three baseline category projections; no Feature or inferred Enterprise hierarchy. |
| Business Behaviour | What bounded functions, services, processes and participants discharge a selected responsibility? |
| Information Architecture | What information meanings and established relationships are governed, with whose responsibility/authority? |
| Application Architecture | What software responsibilities and explicit structural/behavioural boundaries exist, with candidate standing retained? |
| Application Integration | How do established internal/external interactions cross boundaries, without conflating durable handoff with transient Diadromí? |
| Technology/Platform | Which accepted runtime/services/artifacts support an application responsibility? Pending governed topology. |
| Security | How do existing principles, constraints, Themis services/enforcement and audit responsibilities constrain operations? No parallel security metamodel. |
| Implementation/Migration | Which accepted changes/deliverables address established architecture states/gaps? No invented plateaus. |
| Traceability / component-specific | Why does this component exist, what does it support/use, and where do established traces stop? |
| Release-specific | Which existing assertions belong to a source-governed scope? Unknown scope stays visible as unknown. |

Use nested Views organisation items, bounded tier/grid layouts, readable labels and wrapped/vertical siblings. Natural Business regions, BEC contextual groups and strategy derivation stages may organise presentation; they are not new semantic capabilities, tiers or containers. Information Views/Assemblies are architectural information constructs, distinct from ArchiMate diagram Views.

## 8. Deterministic publication and verification

Inputs must be explicit/configurable: immutable semantic Exchange baseline, lock, reviewed mapping/identity registry, authoritative source snapshots and local Archi 5.9 schemas. Never depend permanently on Downloads. The existing baseline fixture and lock remain evidence. Enrichment is an allowlisted overlay; it never rewrites the fixture or weakens the baseline lock.

Use stable ordering and fixed namespace/identity keys. Exclude clock time, absolute machine paths, parser order and display labels from IDs and output determinism. Preserve unrelated Views/navigation. Repeated generation and regeneration from the augmented model must preserve byte-identical Views and semantic identity. Publication report hashes identify exact input/output content.

Required gates per increment: source drift and authority checks; identity/alias uniqueness and non-reuse; baseline preservation except explicitly allowed changes; no duplicate XML identifiers; all elementRef/relationshipRef/org references resolve; connections use View-local visual IDs; semantic endpoints match represented elements; each generated View is organised; native relationship legality and source predicate/direction agree; local offline Archi 5.9 Diagram/Model/View XSD validation of serialized XML; repeat generation/idempotence; bounded relevant tests and required repository architecture suite; change/coverage report; visual import/inspection in Archi before the next increment. XSD PASS does not claim visual import or semantic approval.

## 9. Documentary content and ambiguity

Contents pages, reading paths, domain folders, metamodel class headings, examples, references, historical migration wrappers, review checklists, source keys, gap navigation IDs, counts, status dashboards and implementation module lists do not automatically become architecture elements. Metamodel categories describe how to model instances; they are not themselves all architectural instances. Keep suitable content as documentation, metadata, View annotation or a report. Exclude deliberately rejected/retired meanings from current architecture while retaining source history.

Escalate ambiguous type, identity, source standing, definition, endpoint, relation predicate, Feature ancestry, ID conflict or contradictory source. Report both assertions with evidence; do not choose by similar names, latest file date, structural symmetry or implementation convenience. Block only dependent publication records/slices, preserving the rest where independently authorised.

## 10. Release rules and stop point

Every accepted repository change increments the RAD / Architecture patch release under the [release policy](architecture-release-policy.md), regardless of author/tool. The repository-root [architecture-release.json](../../../architecture-release.json) is the single current-release authority; publication and assessment reports read it through the shared helper. The owner established the prior baseline and authorised this bundle's increment. Generation never increments a release. Minor/major increments require an explicit governance decision. Maven, baseline `v2.1a`, R1/R2 scope and branch names retain their separate meanings.

This increment is limited to this specification, catalogue, coverage assessment, incremental plan and exact-ID capability Documentation. No wholesale conversion, new cross-layer semantics or additional diagrams are authorised by this release bundle. All subsequent slices require review and Archi visual inspection as specified in the plan.
