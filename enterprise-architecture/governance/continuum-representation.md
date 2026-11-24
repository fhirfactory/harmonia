<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Canonical Enterprise Architecture Continuum representation

## Authority, scope and architectural unit

The Solution Architect's [RAD-1.1.5 Task](../design/evidence/1.1.5/002/before/ai-agent-tasks/20261021T092725Z-establish-and-apply-element-continuum.md)
authorises establishment and application of this representation. [HCRL 002](../design/review/1.1.5/hcrl-1.1.5-002.pending.md)
records the incorporated result and unresolved questions for holistic review.
The rejected 001 document-carrier strategy supplies no application authority.

The Solution Architect's [prospective RAD-1.1.7 acceptance](../design/register/1.1.7/hcrl-1.1.7-001.complete.md)
accepts the reconciled Continuum as the canonical architectural baseline and the
bounded supporting group from HCRL 002. This completes its holistic review without
altering individual element/assertion standing, residual questions or released
1.1.5/1.1.6 history. The original application/review account remains provenance.

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

The [canonical identity/naming contract](canonical-identity-and-naming.md),
[modelling strategy](architecture-modelling-strategy.md),
[Round-Trip Praxis](archimate-round-trip-engineering-praxis.md),
[Architecture Governance](architecture-governance.md) and
[Change Provenance](architecture-change-provenance.md) remain controlling in their
roles. An unresolved native classification stays in the originating review/source;
its source is preserved and its decision belongs in the applicable HCRL DR.

<a id="architectural-organisation-and-native-storage"></a>
## Architectural organisation and native storage — RAD-1.1.10

The Solution Architect's [1.1.10 Task](../design/evidence/1.1.10/001/before/ai-agent-tasks/unresolved-continuum-taxonomy-remap.md)
authorises the bounded organisational remap accepted in the [completed HCRL 001](../design/register/1.1.10/hcrl-1.1.10-001.complete.md).
The canonical [navigation.json](../continuum/navigation.json) supplies the ordered
Harmonia architectural taxonomy. It is organisation metadata of this Continuum,
not a separate element registry, architecture master index, ArchiMate type system
or publication-owned architecture. Its references use existing canonical IDs.
An element's name, definition, native type and independent standing remain owned
by its canonical Markdown file. The native-type directories below remain stable
storage locators; they no longer determine architectural reading order.

Governance uses `G`; Motivation, Strategy, Business, Application, Technology and
Implementation & Migration use `I`–`VI`. Every exposed descendant appends its
one-based sibling position to its parent's coordinate. Numbering is derived by
one shared traversal in `tooling/continuum_navigation.py`, never persisted as
identity or prefixed onto canonical names. Reordering or moving navigation may
change coordinates without changing IDs, names, native types or relationships.
All elements occur once; context/cross-reference links do not create another
canonical representation. Every structural node must have actual content,
children or an existing governed source. The explicitly authorised domain roots
may state absent population without inventing subsections.

Taxonomy is architectural navigation, not native semantic containment. No edge
is inferred from organisation. Standing is orthogonal: proposed, pending,
historical and superseded elements remain qualified in their architectural
contexts. `Unclassified <Domain>` preserves genuinely unestablished placement.
Strategy retains existing capability decomposition and qualifications. Business
uses populated capability contexts without duplicating the full Strategy tree.
Stakeholders are cross-cutting Motivation profiles, not a peer type-bucket chapter.
Application preserves logical Topic semantics and independently scoped information
structures; a name containing “Topic” does not establish that specialisation, nor
does a DataObject establish a Class. Middleware architecture belongs within the
authorised Application/System-of-Systems context. Missing integration, class,
Technology or release-family architecture is not invented to complete headings.

Element location properties, the Manifest/browser and narrative RAD consume
this same tree and coordinate traversal. From RAD-1.1.17 Iteration 002, Exchange
Model Tree folders use the independent native catalogue convention below;
reading coordinates remain properties, never native Name prefixes. Native Name
remains displayName. Number-bearing XML organisation identifiers are mutable
transport locators only. Archi may restructure its native folders; the location
properties preserve the canonical coordinates independently of that limitation.
Existing authored Views and their separate View-folder organisation are preserved.

### Reader-facing navigation correction — RAD-1.1.11

The Solution Architect's [remediation Task](../design/evidence/1.1.11/002/before/ai-agent-tasks/unresolved-continuum-navigation-remediation.md)
authorises the following bounded refinements to the accepted 1.1.10 organisation.
The canonical navigation serialization is version 2. G and Parts I–VI retain
their domains; **R — Cross-Cutting References** is a separate reference area,
outside architectural decomposition. Its Stakeholder Index exposes every canonical
Stakeholder, including those without a represented Motivation association. Its
Historical Capability Identities index preserves reserved predecessors identified
by governed `identity.correspondence`, with links in both directions. Names never
establish correspondence. Other superseded source hierarchies and unresolved
descendants retain their existing allocation and visibly qualified standing.

Every canonical ID still has exactly one canonical navigation location, including
index/historical locations. `kind=context`, `contextRole` and `contextCanonicalId`
identify a contextual occurrence without another element identity. Business
Behaviour includes only represented behaviour and its complete recorded Strategy
Capability ancestry, retaining direct CT2 and Feature allocations. Canonical
Strategy locations, identities and standing are resolved at generation time for
every context; coordinates are never copied into canonical source metadata.
Stakeholder contextual occurrences under associated Motivation material cite
exact existing Association IDs through `relationshipIds`; index discovery does
not assert an association. No semantic relationship follows from either use.

Resolved metadata distinguishes architectural, contextual, index, historical and
navigation occurrences. Every projection exposes typed canonical references.
Exchange navigation-only items carry reference documentation without semantic
edges. The projection also carries `Continuum / Navigation References` on native
elements: typed links, contextual occurrences, behaviour context paths and actual
Stakeholder association references as applicable. These navigation properties
preserve orientation through Archi's non-element folder limitations; they are
derived presentation metadata, not architectural assertions. Authored Views,
native names/types, canonical Markdown and semantic relationships are unchanged.
The [common Review Praxis](architecture-review-praxis.md#5-report-and-verify-the-incorporated-result)
requires Solution Architect structural review of materially changed generated
navigation before HCRL acceptance. Agent inspection supplies evidence, not acceptance.

<a id="archi-catalogue-organisation--rad-1117-iteration-002"></a>
## Archi catalogue organisation — RAD-1.1.17 Iteration 002

The Solution Architect's [authorised Task](../design/evidence/1.1.17/002/before/ai-agent-tasks/20261031T234829Z-archi-catalogue-and-display-names.md)
replaces inferred Model Tree organisation with a deterministic catalogue. This
changes projection and round-trip organisation only. The Continuum remains
architectural authority; its reader-facing navigation, native storage, names,
identities, classifications, standing, provenance and relationships are unchanged.
The [Iteration 002 HCRL](../design/review/1.1.17/hcrl-1.1.17-002.pending.md)
records implementation and outstanding result acceptance.

Each element has exactly one placement: native Archi category / `Catalogue` /
native ArchiMate type. Categories are Strategy, Business, Application, Technology
& Physical, Motivation, Implementation & Migration, and Other. Only represented
native types receive directories. ValueStream belongs to Strategy; Grouping and
Location belong to Other, including specialised Groupings. Feature remains under
Capability, Topic under DataObject and every Requirement specialisation under
Requirement. Specialisations remain native-element properties.

Each relationship has exactly one placement: `Relations/Catalogue/<Source Element>/`.
Resolve its explicit source Exchange binding to the governed canonical element;
never infer source from its storage owner, name, target or directory. Source
folders exist only for elements with outgoing relationships. Their readable
labels are `displayName [canonicalId]`, preventing collisions between equal names
and remaining stable if another equal name is introduced. All outgoing edges sit
directly in that folder, without relationship-type directories. Transport folder
IDs derive deterministically from native type or established canonical identity;
no architectural identity is allocated for a folder.

Composition, Aggregation, Specialization, Realization, Association, Home and
Grouping have no role in catalogue placement. Their explicit architectural
meaning, identities, direction and properties remain governed independently.
The projector retains containment/naming validation for semantic conformance;
it does not use those results to generate folders. Earlier containment/Home/
canonical-navigation folder-generation rules are retired for element and
relationship catalogues; their underlying architectural and naming rules remain.

Both native Name and element catalogue entry label equal governed `displayName`
verbatim across all native types. Do not prefix positional coordinates and do not
strip meaningful identifiers already present in a governed name. canonicalId,
canonicalName, friendlyName, source/release identifiers and evidence references
are unchanged. Reading coordinates may remain separate derived properties.

Views and their organisation are architect-maintained presentation. Preserve
identities, names, labels, properties, references, nested visual objects,
connections, order, geometry and all supported presentation, including empty
View directories and their metadata. Never generate or reorganise Views to
implement the catalogue. Mixed View/semantic branches requiring destructive
separation must stop for adjudication rather than discard presentation.

On Archi export, reconcile element/relationship details by established identity
and explicit endpoints through the existing review/application gates. Incoming
element/relationship folders confer no authority and are regenerated as these
catalogues. Detect View hierarchy changes separately, preserve the exact export,
and incorporate only adjudicated presentation into the existing projection
template before reprojection. The element-only apply adapter must reject applied
View/folder changes rather than silently ignore them. The maintained template
preserves presentation unsupported by transport; do not replace it with a lossy
no-edit return. Record format limitations and retain the native model/exact export
where required. A real supported-Archi transport and Solution Architect workbench
structural review remain separate from automated XML validation and acceptance.

## Deterministic native storage and template selection

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
python3 enterprise-architecture/tooling/architecture_continuum.py template --type Principle
python3 enterprise-architecture/tooling/architecture_continuum.py template --type Capability --specialization Feature
python3 enterprise-architecture/tooling/architecture_continuum.py template --type DataObject --specialization Topic
```

This is representation selection, not authority to create a construct. A Feature
needs an established Capability-compatible responsibility and its actual governed
framing/standing. Topic requires explicit Topic classification; neither a bracketed
label nor an ordinary DataObject is enough. BusinessObject and Artifact are native
types; their display prefixes do not create Harmonia specialisations. Home is a
directed Association specialisation and is stored as an assertion, not an element.

## Resource fields and Markdown content

### Stakeholder, Journey and independent requirements — 1.1.21 ADD

The [Stakeholder/Journey/requirements profile](stakeholder-journey-requirements-profile.md)
implements the issued 1.1.21 semantics and attributable native-relationship
disposition. Journey specialises ValueStream; UseCase and UserStory independently
specialise Requirement. A constituent Journey has the same element type as its
parent. Optional contextual participation and progression remain separate from
authorisation, accountability and runtime execution. The profile records naming,
projection, identity reconciliation and review limits; incorporation supplies no
final acceptance of the resulting candidate.

The bounded [Patient reconciliation](../design/review/1.1.21/hcrl-1.1.21-001.pending.md#dr-26)
uses the existing identity-correspondence contract to reserve a duplicate
Stakeholder allocation as history. Such a correspondence-backed historical
Stakeholder entry remains in the cross-cutting Stakeholder index, with a derived
successor reference; it does not enter the Capability history hierarchy or the
Motivation decomposition. Historical association standing is independent of the
reused element's current catalogue applicability.

### SolutionFacet classification — 1.1.19 candidate

The Solution Architect's [refactoring Task](../design/evidence/1.1.19/001/before/ai-agent-tasks/unresolved-solutionfacet-refactoring.md)
and [DR-17 clarification](../design/evidence/1.1.19/001/before/ai-agent-tasks/unresolved-solutionfacet-dr17-clarification.md)
authorise an identity-preserving UPDATE of the former Subject classification to
**SolutionFacet**, a Harmonia specialisation of native ArchiMate **Grouping**.
[HCRL DR-17](../design/review/1.1.19/hcrl-1.1.19-001.pending.md#dr-17)
records application and outstanding candidate review; this is not acceptance or
publication of a protected baseline. Earlier incorporation accounts below retain
their historical terminology and standing.

A SolutionFacet is a named aspect of a solution, expressed in accessible
terminology and organised within a hierarchical taxonomy to support the
classification, contextualisation and navigation of solution-related information.
It provides a plain-English organisation of solution information and may provide
context for architectural elements of different types. It does not represent an
architectural capability, imply implementation responsibility or ownership, or
inherently establish an architectural concern, requirement or constraint.

The Solution-Facet taxonomy asks **“What aspects of the solution are we describing?”**
The complementary Capability Map asks **“What must the solution be capable of doing?”**
The Capability Map may become more technical as the architecture develops;
the Solution-Facet taxonomy should remain accessible to a broad audience.

Preserve the existing G01–G18 identities and hierarchy. Existing parent-to-child
Composition expresses explicit classification containment. Separately established,
directed child-to-parent Home Associations retain their authoritative-location
meaning; Composition does not create Home. No relationship is inferred from
specialisation, presentation or navigation. This refactoring supplies no new
Capability, element identity, naming token or naming ancestry. Existing formal-name
qualifications remain unresolved where no naming vocabulary is established.

The Task's accessible `displayName` and explicit `friendlyName` fields are
retained separately. SolutionFacet supplies no required display prefix or formal
canonical-name qualifier. Projection continues to use `displayName` as native
Name; the authored workbench Name is retained as naming provenance where it
differs. Existing canonical Markdown storage paths remain under `grouping/`;
changing the specialisation does not require relocating these resources.

The later [DR-17 final naming dispositions](canonical-identity-and-naming.md#solutionfacet-naming-roles--dr-17-final-dispositions)
confirm Composition as the authority for the three affected SolutionFacets'
programmatic context, local displayName presentation and independent canonicalName
semantics. Earlier incorporation accounts supply no prohibition overriding those
explicitly verified naming dispositions and no authority to populate canonicalName.

The existing `subjectGroup` metadata key and its `sourceId` values remain source
correspondence identifiers for compatibility with the originating registers and
their tooling. The architectural concept they classify is now SolutionFacet;
the retained key neither creates another specialisation nor changes identity.

### Iteration 001 architectural statement specialisations — RAD-1.1.17

This incorporation account retains its original Subject terminology and navigation
labels as history. For the current candidate, the SolutionFacet section above
supersedes that classification terminology and its reader-facing labels only;
statement specialisations, source correspondence and relationship meanings remain
unchanged.

The Solution Architect-reviewed [Iteration 001 specification](../design/offline-design/Harmoni-NFR-Derived%20Architetural%20Elements%20-%20Iteration%20001.md)
and its [authorised ADD Task](../design/evidence/1.1.17/001/before/ai-agent-tasks/20261031T104506Z-nfr-derived-elements-add.md)
establish these specialisations in the executable type catalogue:

| Specialisation | Native base | Meaning |
| --- | --- | --- |
| Axiom | Principle | Prescriptive governing architectural rule; deviations require governance. |
| ArchitecturalAxiom | Principle | Prescriptive governing architectural direction for architecture and implementations. |
| ImplementationGuideline | CourseOfAction | Preferred implementation approach or strategy, not inherently a binding mandate. |
| Mandate | Constraint | Binding architectural constraint or obligation, including positive obligations. |
| FunctionalRequirement | Requirement | Required functionality or behaviour. |
| NonFunctionalRequirement | Requirement | Required quality or operational characteristic. |
| InformationRequirement | Requirement | Required information semantics, integrity, availability or lifecycle characteristics. |
| TransitionRequirement | Requirement | Required conditions when moving between architecture or solution states. |
| AssuranceRequirement | Requirement | Required demonstrable evidence or verification outcome. |

Native Principle expresses aspirational architectural direction; native Goal expresses
a desired outcome in this register. Axiom and ArchitecturalAxiom remain distinct;
no relationship between those specialisations is asserted. DerivedRequirement
denotes provenance, not another specialisation. Existing axiom definitions and their
highest authority remain unchanged; a new specialisation supplies no promotion over
those definitions. Source identifier prefixes supply no type information.

The new specialisations use the established `[Specialisation] Name` display convention
and native Motivation `MOT` naming qualifier. ImplementationGuideline retains its
existing unresolved formal qualifier. Unqualified Grouping also retains unresolved
formal naming; neither a token nor naming ancestry is inferred from subject organisation.
The earlier field-list examples below remain examples; the type catalogue and schema
contain the complete allowed specialisation population.

Iteration 001 subject groups/subgroups use native Grouping. Their explicit Composition
relationships represent containment within the register's subject classification,
including each statement's primary subject subgroup. They confer no component ownership,
implementation containment, logical Home or new naming ancestry. Groups live in
Cross-Cutting References / Architectural Subjects; subgroup navigation links to each
allocated statement's one canonical native-layer location. Existing RequirementGroup
hierarchies retain their accepted scope and identities. The cross-subject Information
Management grouping is distinct from the existing functional-requirements grouping.

The [pending HCRL](../design/review/1.1.17/hcrl-1.1.17-001.pending.md)
records application and remaining review checkpoints; incorporation supplies no final
Solution Architect confirmation.

A file starts with JSON front matter between `---` lines. JSON is used as a strict
subset of YAML so ordinary Markdown consumers and standard-library machinery can
handle the same representation. Duplicate keys are rejected. `schemaVersion`
versions this serialization only; it creates no architecture release or identity.

Required fields are:

| Field | Meaning |
| --- | --- |
| `canonicalId` | Existing resource-owned UUID32, or one newly authorised UUIDv4 allocation. |
| `archimateType` | Exact native type in the reference profile. |
| `specialization` | An explicit governed specialisation from the type catalogue, or null; classification evidence is required. |
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
their identities, contents, geometry and View navigation. Canonical organisation
comes from the navigation source above. The maintained projection
template owns model properties, property definitions and View presentation; it owns no
semantic elements or relationships. LaTeX publication consumes the continuum and
never supplies duplicate source elements. Historical releases remain historical.

Automated PASS demonstrates its stated checks only. Real Archi import/export,
visual review and Solution Architect acceptance remain distinct outcomes. The
resulting continuum was submitted for holistic review under HCRL 002; the
prospective 1.1.7 decision above supplies human acceptance. An agent cannot
confirm its own architectural closure.

## Applied validation and publication boundary

The root [MANIFEST.md](../continuum/MANIFEST.md) is a generated navigation/review
projection of every canonical element, authorised by the
[manifest follow-up Task](../design/evidence/1.1.5/002/before/ai-agent-tasks/20261021T101602Z-generate-continuum-review-manifest.md).
It owns no architectural meaning, identity or standing. Only this exact root file
is excluded from canonical element ingestion; arbitrary Markdown elsewhere in
`continuum/` still must validate as an element. RAD-1.1.8 relocates publications
outside canonical state to `documentation/continuum-manifest/` and establishes
the narrative LaTeX publication at `documentation/continuum-rad/`. Both are
derived documentation products; neither supplies canonical architectural content.
The manifest's presence or manual modification never alters projection
inputs. Its separate byte-for-byte staleness check detects missing or changed output.

`enterprise-architecture/tooling/continuum_manifest.py` validates canonical inputs before generating the
manifest. From 1.1.10, the canonical navigation tree supplies its hierarchy,
depth-first order and full coordinates; the previous native-layer/type catalogue
layout no longer supplies architectural navigation. Native types/specialisations
remain independently visible. All elements appear once, including historical and
qualified populations within their architectural contexts.
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
explicit deferral repeatable. The [reproducibility check](../tooling/verify_continuum_reproducibility.py)
regenerates the root manifest, XML, HTML, versioned Markdown index and LaTeX catalogue in a temporary checkout
with no old RAD captures, processing workspaces, copied LaTeX architecture or local
interaction logs. Current shared code lives under `enterprise-architecture/tooling/` and imports no historical command dispatcher, baseline processor or source capture.

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
