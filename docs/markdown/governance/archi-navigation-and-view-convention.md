<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Harmonia Archi Navigation & View Convention

## Current round-trip projection — 2026-10-20

The [Round-Trip Engineering Praxis](archimate-round-trip-engineering-praxis.md#common-interpretation-and-projection-rules)
governs current round-trip regeneration. Element and Relationship folders are
derived from shared state-filtered Home/type-selected Composition containment and
governed collection roles, including when submitted folders are absent or wrong.
It supersedes earlier preservation of unestablished Home locations for this
projection: retain names and semantic uncertainty, and display unresolved Home or
Feature ancestry explicitly in derived navigation rather than replaying submitted
paths. Recorded source context may classify a collection; it supplies no absent
current containment. Every element participating in resolved containment has its
own same-named Element folder containing its single element reference, including
contained leaves; child folders nest recursively under their parent context with
governed collection/role placement. Standalone collection policy remains in force
outside that hierarchy. Relationship folders mirror the actual source, reusing a
source context or adding one where needed. All existing Views, including their
navigation, are preserved presentation artefacts; historical generated-View
replay descriptions below grant no authority to replace submitted Views.

## Authority and purpose

The architecture owner's Release 1.0.4 task authorises this reusable publication
convention and its application to the existing capability publication. The
[release authority](../../../architecture-release.json) and
[release policy](architecture-release-policy.md) govern the increment. This is a
navigation and presentation change with **zero semantic architecture changes**.
Future-domain conventions below establish presentation rules only; they do not
authorise those domains' publication, identity allocation or semantic derivation.

Under [AX-04, AX-14 and AX-17](architectural-axioms.md), publication machinery must
preserve Harmonia meaning, distinguish navigation from semantics and retain
unresolved architecture. This convention is consistent with the
[mapping profile](rad-archimate-model-mapping.md),
[identity model](architecture-identity/model.md) and
[truthful traceability rule](../04-information-architecture/traceability/domain03-traceability.md#11-traceability-principles).
Folder placement supplies no missing architecture.

The later [Increment 2B ingestion/publication contract](archimate-ingestion-publication-semantics.md)
adds submission recognition, accepted authored-View preservation and a distinct
[deterministic Containment View](archimate-ingestion-publication-semantics.md#deterministic-containment-view)
archetype for 1–20 direct Composition children. Its universal
`Name == displayName` rule supersedes historical baseline instructions below to
leave a different native Name unchanged in future governed results. Historical
navigation/layout examples and artefacts remain intact; their migration is not
authorised by 2B. Generated Views and accepted authored Views retain separate
preservation obligations; deterministic publication does not discard the latter.

## Three independent structures

The [explicit Home modelling decision](architecture-modelling-strategy.md#explicit-architectural-home)
distinguishes logical architectural Home from the **navigation home** terminology
used below. A single model-tree location is a presentation rule, not mandatory
architectural Home cardinality. Folder and View placement establish no Home;
the Home decision establishes no automatic folder or View placement rule.

| Structure | Meaning | Source of authority |
| --- | --- | --- |
| Semantic architecture | Elements and typed relationships, including actual decomposition | Governed definitions, assertions and exact publication bindings |
| Model-tree navigation | The canonical location of an element or relationship in Archi folders | This convention projected from the relevant established semantic hierarchy |
| View navigation | Folders of diagrams that progressively answer architectural questions | A declared View scope projected from the semantic model |

An architectural element exists semantically once, has exactly one canonical
model-tree home, and may have many visual appearances. A relationship likewise
exists once and has exactly one navigation home. A folder and its contained
element may intentionally have the same name: the folder supplies context, while
the element is architecture. Folder labels, numbering and identifiers never
allocate Architecture Identities or establish semantic aliases.

The semantic hierarchy must be independently reconstructable from established
relationships without reading folders. Generate semantics before navigation;
folder nesting must never generate Composition or any other relationship.
Moving or renaming folders cannot change element/relationship identities.
Missing hierarchy remains missing; do not manufacture ancestors, tiers, natural
view membership or relationships to make a navigation tree look complete.

## Canonical model-tree homes

The [Canonical Identity and Governed Naming Contract](canonical-identity-and-naming.md)
governs resource metadata. A canonical navigation home is a presentation location;
folders and View placement create no `friendlyName` or `canonicalName` qualification
and own no `canonicalId`. Existing navigation and presentation remain preserved.


### Synchronised baseline convention — 2026-10-17

The architecture owner's baseline-synchronisation task establishes the following
current navigation. The later sections' 1.0.4 examples remain reference-publication
conventions. They do not constrain the recovered baseline to the old population.
No navigation change authorises additional architecture or changes element identity.

Archi **5.10.0** is the supported human architecture workbench for the current
uncommitted **1.0.6** candidate. Its native resource categories are the primary
resource organisation. Exchange category markers use the exact native labels
`Motivation`, `Strategy`, `Business` and `Application`; the importer consumes
these markers rather than creating a redundant folder below each native root.
Resource folders use human-readable labels without global layer numbering.
There is no demonstrated sibling-order requirement requiring resource prefixes.

```text
Motivation/
Strategy/
  Business Layer/
    Business Capabilities/                 <governed BC hierarchy>
    Business Enabling Capabilities/         <governed BEC hierarchy>
  Application Layer/
    Enterprise Capabilities/               <governed EC hierarchy>
    Application Capabilities/               <no invented population>
  Unreconciled 1.0.4 Reference/
Business/
  Business Capabilities/                    <BC context through finest governed level>
  Business Enabling Capabilities/           <BEC context through Feature where governed>
Application/
  Information Map/
  Enterprise Capabilities/
  Application Map/
  Middleware Map/
Relations/                                  <unnumbered source-resource paths>
Views/
  0-Motivation/
  1-Strategy/
    Capabilities/
      1-Business Capabilities/
      2-Business Enabling Capabilities/
      3-Enterprise Capabilities/
    Unreconciled 1.0.4 Reference/
  2-BusinessLayer/
    1-Business Capabilities/
    2-Business Enabling Capabilities/
  3-ApplicationLayer/
    1-InformationMap/
    2-Enterprise Capabilities/
    3-ApplicationMap/
    4-MiddlewareMap/
  4-TechnologyLayer/
  5-Implementation/
```

The View tree retains its accepted global numbering because Views share one
navigation namespace. Preserve its existing numbered immediate sibling categories
as shown above. This ordering does not apply below native resource roots or in
mirrored Relations paths. Numbering supplies no architecture code or relationship.

Capability contexts display `[CT1]`, `[CT2]` and `[CT3]` from exact governed
classification. Features display `[Feature]` as the next first-class decomposition
level where complete governed CT3 framing exists. The same context is exposed in
Strategy resources, Business resources, Strategy Views and Business Views:

```text
[CT1] Entity Information Enablement
  [CT2] Provider Administration
    [CT3] Practitioner Role Administration
      [Feature] Electronic Endpoint Binding
```

Folders carry these display labels; semantic architectural Names, IDs, types and
existing diagram identities remain unchanged. A classified leaf folder may contain
the existing semantic reference under its unmodified Name. That single context
folder is retained to expose classification without renaming architecture. There
is no intermediate `Features (framing only)` folder. Business context folders do
not retype Features or duplicate their Strategy semantic homes. Empty context
folders remain legitimate navigation even where no Business behaviour or View is
associated. Final human import/rendering acceptance remains separate from Exchange
structure validation.

Use the exact governed ancestry, independently for BC and BEC, and terminate at
the finest recorded level. Missing tiers are never invented. A current Feature
without complete current CT1 -> CT2 -> CT3 ancestry is retained in an explicitly
`Unresolved Feature Ancestry` context, preserving its responsibility evidence and
standing. Proposed placement remains Proposed. The current governed baseline has
157 current Features, all in BEC with complete recorded ancestry; no BC Features
exist. This projection rule creates no BC Features or amendment to the metamodel.
Other pending, superseded, identity and ancestry uncertainties remain explicit.

### Final human-inspection projection correction — same 1.0.6 candidate

Business resource homes use the exact current `realises_feature`,
`associated_feature` and `contributes_feature` assertion endpoints where the
assertion is not unresolved. Proposed assertions retain Proposed standing.
An explicit `governed_process` assertion provides the Process navigation path
through that Function's recorded Feature context. This is a documentary path,
not a new Process/Feature semantic relationship. No traversal through shared
ownership, `uses_function` or `participating_function` supplies a Feature home.

A single recorded Feature association supplies the Business resource context.
Multiple recorded Feature associations do not establish a preferred canonical
home: retain the existing Capability home and report the shared cases. This
single-home navigation constraint does not remove any association: shared
behaviour has separate visual appearances beneath each associated Feature in
Business diagrams, all referring to the same semantic element. Unresolved
associations and behaviour without an association stay at their established
Capability context. Their absence is not silently repaired.

A realisation View belongs beneath the exact Feature endpoint of its recorded
assertion, including separately qualified historical contexts. A Capability-wide
Behaviour View remains at that Capability. No generic CT3 Feature Realisations
bucket remains. Strategy continues to contain only capability/Feature framing.

Each applicable capability View context has an immediate-child landscape as its
first serialized View reference. Landscape View Names use the local `0-`
prefix because Archi 5.10.0 sorts Views by display Name rather than Exchange
item order. This demonstrated View-only ordering requirement introduces no
resource prefix or semantic element rename. Taxonomy roots show only CT1; CT1 shows context
and CT2; CT2 shows context and CT3; CT3 shows context and Features where governed.
Reuse existing immediate-child Views and IDs; allocate missing landscape View IDs
from stable context identity and projection role. Terminal anchor Views remain.
No generic Landscapes folder or invented Business Capability Features are used.
Validation checks both serialized order and the landscape Name sorting first.
The installed Archi 5.10.0 tree comparator confirms name sorting; native GUI
rendering remains a final human inspection item.

Business diagrams use deterministic Feature columns beneath the Capability
context. Associated Functions, Processes and Services appear within their
Feature's column; unassociated behaviour appears in a separate lower band.
Paging includes missing governed association endpoints. Shared behaviour may
have multiple visual nodes, with deterministic appearance IDs and one semantic
ID. Draw only existing native relationships; do not add edges for contextual
paths, folder containment, placement or inferred Function/Process/Service chains.

Information, application-component and middleware projections remain independent.
Capability elements exist once in Strategy; other layers mirror context through
folders. Relations follow their source's actual resource home. Navigation nesting
never creates semantic relationships. Folder identity keys retain stable source
identity and projection role; changed Feature scaffolding is navigation only.

Keep progressive context/immediate-child Views and deliberate leaf/anchor Views.
Place Motivation subject Views directly under `0-Motivation` unless several Views
warrant a real substructure. Empty top-level locations and clearly labelled
documentation-only coverage Views expose representation boundaries without
inventing semantics. Preserve all existing semantic, relationship, View, node and
connection identities when reorganising. Folder/reference presentation identities
may be reprojected; semantic context folder keys use stable source identity and
projection role, excluding mutable labels and ancestry.

Documentation starts with the concise source-justified definition, followed by
existing Overview, Intent, Boundary, Responsibility or equivalent explanation
where reconciled evidence establishes continued current applicability. Preserve
that text verbatim; historical provenance alone does not imply historical meaning.
Use Markdown emphasis for explanatory labels. Genuinely historical narrative or
material with uncertain applicability follows a Markdown `---` rule and `## History`.
Omit the separator when no history exists. Predecessor names, superseded wording,
relocation and reconciliation explanations belong in History when useful as
narrative. Uncertain source explanation remains historical with its provenance
and standing preserved under AX-17. The baseline's exact-key, field-level
[classification manifest](baseline/documentation-classification.json) records text
hashes and applicability evidence; it authorises no semantic rewriting.

Existing source-era Properties remain intact and version-qualified; current-baseline
Properties are separately qualified. Identity, status, currentness, tier, source,
provenance and review state belong in Properties. Do not dump those fields into
historical prose. A historical element's source definition remains readable first;
its Properties and View context make its historical standing explicit.

Markdown RAD authors and governs; ArchiMate visualises, navigates, supports review
and proposes change; LaTeX RAD renders and publishes. An Archi export is change
input for governed reconciliation, not a replacement architecture repository.
Accepted Archi presentation state may be subordinate governed projection metadata.
Publication flows strictly `Markdown RAD -> LaTeX RAD -> publication output`;
there is no normal reverse-authoring path. See the
[documented collaboration lifecycle](../../archimate/README.md).

[The synchronised baseline](baseline/README.md) supplies the current textual
projection, exact correspondence, unresolved items and coverage boundaries.

Use the subject's established decomposition, preserving its standing and
qualifications. A decomposing subject has a same-named folder containing its own
element reference followed by its children's folders. Apply the same pattern to
leaves for a predictable home. Folder names use existing semantic labels;
alphabetical ordering is a navigation convenience. Do not invent numeric
architecture codes. A separately governed ordering may be projected when present.

For the current Strategy capability publication:

```text
Strategy
├── Business Capabilities
│   └── Individual Care Delivery                 [folder]
│       ├── Individual Care Delivery             [existing CT1 element]
│       └── <existing CT2 capability>             [folder]
│           ├── <same CT2 capability>             [existing element]
│           └── <existing CT3 capability>         [folder]
│               └── <same CT3 capability>         [existing element]
├── Business Enabling Capabilities
│   └── <same pattern from its own Composition hierarchy>
└── Enterprise Capabilities
    ├── EC-01 Managed Entity & Relationship
    └── ... EC-14 Service Guardian
```

BC and BEC remain separate structures. EC remains an orthogonal catalogue; no EC
containment is inferred. The EC native identities and locked semantic bindings
from [Release 1.0.3](architecture-identity/adoption-1.0.3.md) are unchanged.

An ArchiMate layer may contain several independent subject navigation structures.
Sharing a layer does not imply sharing a decomposition or a navigation parent.

## Explicit Home projection

**Solution Architect decision — 2026-10-19, unsigned RAD-1.0.9 completion.**
For the applicable Business Functions, Processes and Services with one established
explicit Home to a Feature, principal navigation SHALL use that Feature's governed
semantic ancestry and the existing Business context projection above, followed by
the existing `Functions`, `Processes` or `Services` leaf. This is the same Home
context used by both [qualified names](canonical-identity-and-naming.md).
Explicit Home takes precedence over responsibility-derived placement while
responsibility retains its independent meaning and standing.

Architecture → explicit Home → navigation is the only direction of authority.
Folder placement supplies no Home, Composition, ownership, responsibility or
Realization assertion. Reuse existing context folders and stable identity-based
folder conventions; retain one principal reference per element. Relationship
references follow their source's resulting location under the existing Relations
projection. No View presentation change follows from this decision.

The [modelling decision](architecture-modelling-strategy.md#home-derived-naming-and-navigation)
defines applicability and stop conditions. The 93 unestablished and 18 ambiguous
Home cases retain their existing names and locations pending adjudication.

## Relationship homes follow the source

Place each relationship under **Relations**, then its subject structure and the
canonical decomposition path of its **source semantic element**. Relationship
type, target, diagram appearance or discovery order must not change that home.
Do not repeat a relationship under its target. Mirror source paths even when
some folders contain no outgoing relationships; empty branches are presentation
context and may be omitted by an importer without changing architecture.

```text
Relations
└── Capabilities
    ├── Business Capabilities
    │   └── Individual Care Delivery
    │       ├── <relationships sourced by this CT1>
    │       └── <CT2>
    │           ├── <relationships sourced by this CT2>
    │           └── <CT3>
    ├── Business Enabling Capabilities
    │   └── <source paths in the BEC hierarchy>
    └── Enterprise Capabilities
        └── <independent source catalogue; currently no outgoing relationships>
```

## Progressive Views

Keep a high-level capability landscape. Provide BC and BEC overviews limited to
existing CT1, CT2 and CT3 capabilities, with no deeper constructs. EC has its own
catalogue overview. Normal detailed Views contain the **current context and its
immediate decomposition only**. A leaf overview shows that existing element;
it does not invent children. Overview diagrams may span multiple levels when
their scope says so. Do not repeat a complete descendant tree in every diagram.

```text
Views
└── Capabilities
    ├── Harmonia Capability Landscape
    ├── Business Capabilities
    │   ├── Business Capability Model             [CT1 + CT2 + CT3 overview]
    │   └── Individual Care Delivery
    │       ├── Individual Care Delivery Overview [context + immediate CT2]
    │       └── <CT2>
    │           ├── <CT2> Overview                [context + immediate CT3]
    │           └── <CT3>
    │               └── <CT3> Overview            [leaf context]
    ├── Business Enabling Capabilities
    │   ├── Business Enabling Capability Model    [CT1 + CT2 + CT3 overview]
    │   └── <same progressive pattern>
    └── Enterprise Capabilities
        └── Enterprise Capability Model          [orthogonal EC overview]
```

The existing overview names, IDs, node/connection IDs, coordinates, labels and
Documentation are retained when only moving their navigation entries. The names
`Business Capability Model`, `Business Enabling Capability Model` and `Enterprise
Capability Model` designate the three retained overview diagrams. A controlled
replacement needs an explicit scope decision and a report of retired/replacement
View IDs; there are **no View replacements in 1.0.4**. The existing individual
assessment import diagnostic remains intact at its original Views navigation home.

New detailed Views use a highlighted context above a wrapped grid of immediate
children, readable 12-point text, and only existing Composition edges. The matching
child folders in the Views tree provide the drill-down route. Keep the visible
capability nodes as representations of the same semantic elements; do not replace
them with diagram-reference nodes to obtain a navigation shortcut.

## Future subject conventions — no additional publication in 1.0.4

| Layer / subject | Model-tree and View convention | Semantic boundary |
| --- | --- | --- |
| Strategy | Separate BC, BEC and orthogonal EC structures as above | No missing capability crosswalk or hierarchy is inferred |
| Business | Replicate the relevant BC/BEC hierarchy as navigation context for associated Business-layer resources and Views | Strategy Capability elements remain in Strategy; do not duplicate them into Business or infer allocations |
| Application / Data Objects Model | Independent `Data Objects Model` navigation and View hierarchy using governed Data Object decomposition | No one-object-per-register-row or implementation-class inference |
| Application / Software Components | Independent `Software Components` navigation and View hierarchy using governed SoftwareComponent containment | Do not infer containment from packages, POMs, deployment or shared layer membership |
| Relationships | Follow the source's appropriate natural hierarchy | Source placement adds no edge, ownership or target containment |
| Views | Normally mirror the relevant subject hierarchy and progressively narrow scope | Views remain projections and cannot establish missing semantics |

```text
Application
├── Data Objects Model
│   └── <governed Data Object hierarchy>
└── Software Components
    └── <governed SoftwareComponent containment hierarchy>
```

## Determinism and verification

Existing semantic IDs and the identity register are immutable in this release.
Folder/View/visual IDs use deterministic UUIDv5 keys in the existing publication
namespace. Keys include model, representation role and semantic subject or stable
subject/View key. They exclude mutable labels, coordinates, release and folder
ancestry. Reference items have separate deterministic IDs. They are publication
identifiers, never persistent Architecture Identities.

Retain the original semantic lock unchanged. A navigation-aware architecture
comparison excludes organisations and Views, while independent navigation and
View validators check canonical homes, source paths, scopes, deterministic IDs,
references, geometry and original View preservation. Replay the original publisher
against its unchanged lock to establish the pre-navigation semantic baseline.
Moving folders must leave that architecture comparison unchanged; incorrect folder
placement must fail navigation validation independently.

The 1.0.4 gates require 228 capabilities, 189 Composition relationships, all
original semantic records/endpoints/Documentation, 14 unchanged EC register
bindings, exactly one home per semantic object, BC/BEC path agreement, independent
EC navigation, exact detailed View scope, unchanged source assertions/findings,
byte-identical repeated generation and offline Archi 5.9 XSD validation. Test
rejections for duplicate homes, target-based relationship placement, invented
folder-driven Composition, semantic mutation and incorrect View scope.

The existing 214 unresolved capability crosswalks, F46 collision, C navigation
keys, incomplete Canonical IDs, unresolved assertions, Domain05 deferrals and
2,467 unresolved RAD dimensions/relationships remain explicit. The locked
publication hierarchy is the authorised input for this navigation slice; it
does not establish the missing RAD identity crosswalks or reconcile current RAD
architecture by folder placement.

Human acceptance requires inspection in Archi: expand the BC/BEC model trees,
open the retained three-tier overviews, follow source-based Relations folders,
and open successive context/immediate-child Views. The release report identifies
the exact model and paths. Automated validation establishes the structure and
importability; human visual acceptance remains a separate review outcome.
