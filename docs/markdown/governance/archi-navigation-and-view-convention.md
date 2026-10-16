<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Harmonia Archi Navigation & View Convention

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

## Three independent structures

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
