<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate Ingestion and Publication Semantics — Increment 2B

## Authority and scope

The Solution Architect's Increment 2B task establishes these semantic and
presentation rules for the [Increment 2A Change Review](archimate-ingestion-change-review.md)
and subsequent authorised reconciliation/publication. It authorises no
reconciliation engine, blind-test acceptance, identity allocation to submitted
additions, model migration or layout implementation.

> Architect authors architecture. Harmonia machinery authors governance machinery.

The architect normally selects native ArchiMate types, supplies meaningful Names
and applicable authorised classification qualifiers, authors intended semantic
relationships and constructs useful Views. Governed processing supplies canonical
identity, derived names and metadata, relationship navigation, deterministic
publication Views and repository artefacts after the required acceptance.

The [Architectural Axioms](architectural-axioms.md) remain highest authority:

| Axiom | Consistency of this contract |
| --- | --- |
| [AX-04](architectural-axioms.md#ax-04) | Archi and publishing tools supply machinery; explicit governed architecture determines meaning. |
| [AX-14](architectural-axioms.md#ax-14) | Native type, Harmonia classification, identity, naming context, semantics, navigation and presentation remain distinct. |
| [AX-15](architectural-axioms.md#ax-15) | Validation and processing retain uncertain outcomes; publication does not imply an unproved reconciliation succeeded. |
| [AX-17](architectural-axioms.md#ax-17) | Missing context, incompatible classification and ungoverned interpretations require adjudication, rather than inference. |

Existing [canonical identity/naming](canonical-identity-and-naming.md),
[RAD/ArchiMate mapping](rad-archimate-model-mapping.md),
[navigation/View](archi-navigation-and-view-convention.md) and
[release](architecture-release-policy.md) contracts retain their respective
authority. This contract adds their ingestion/publication consequences and the
new Containment View archetype; it does not replace their identity syntax,
mapping profile, preserved history or unresolved metamodel decisions.

## Four distinct stages

| Stage | Governed responsibility |
| --- | --- |
| Ingestion | Establish what changed and what the submitted architecture proposes, preserving incoming evidence. |
| Adjudication | Establish what architectural meaning and change are authorised. |
| Reconciliation | Apply only the authorised change to governed Harmonia architecture. |
| Publication | Produce coherent governed representations, navigation and deterministic Views from the reconciled architecture, including the model returned to Archi. |

A publication rule SHALL NOT retrospectively change the observation of what was
submitted. An ingestion observation SHALL NOT automatically dictate publication
organisation. Incoming Names, folders, native/Exchange IDs, semantic records and
View evidence remain ascertainable independently of the governed result.

During detection, tooling MAY apply established rules deterministically to
recognise and compare proposals. Changes requiring architectural acceptance
become independently adjudicable CRs; ambiguity and missing authority become
questions/CRs under 2A. Deterministic recognition is not acceptance. After
adjudication, accepted CRs and their decision-linked executable instructions
supply reconciliation authority; this contract governs the resulting
representation. Accept, Reject, On-Hold and Manual Intervention are
[2A Dispositions/calls to action](archimate-ingestion-change-review.md#solution-architect-dispositions).
[Outcome and Outcome Reason](archimate-ingestion-change-review.md#outcome-and-outcome-reason)
are independent resulting-state evidence; resolution alone authorises no
reconciliation. Dependencies and processing evidence retain their 2A semantics.

## Recognition, classification and published Names

For **every governed architectural element**, its native ArchiMate `Name` SHALL
equal its governed `displayName`, as established in the
[naming contract](canonical-identity-and-naming.md#three-governed-names):

```text
ArchiMate Name == displayName
```

This invariant is universal; bracketed qualification is conditional on existing
classification rules. It requires neither a qualifier on every element nor a
new classification for every native type. An unaccepted incoming element's raw
Name remains submission evidence, not a value silently rewritten during ingestion.

An `[xxx]` Name qualifier MAY establish a Harmonia classification **only** where
`xxx` is explicitly governed and valid for the element's native ArchiMate type.
Recognition SHALL identify the governing rule and check native-type compatibility.
For example, existing identity/mapping governance establishes:

```text
native type  = Capability
Name         = [Feature] Identifier Capture
harmoniaType = Feature
archimateType = Capability
displayName  = [Feature] Identifier Capture
```

This illustrates recognition, not acceptance or identity allocation for the
blind-test Feature. The qualifier never changes the native type. Existing
`[CT1]`, `[CT2]` and `[CT3]` qualification retains its governed tier meaning;
brackets alone establish no additional `harmoniaType`.

Arbitrary bracketed text SHALL NOT become classification authority. Unknown,
incompatible, conflicting or ambiguous qualifiers, including conflicts with
submitted Properties, SHALL surface through Change Review without manufactured
classification. A qualifier that merely repeats a native type SHALL NOT create
an additional Harmonia subtype. Its presentation treatment follows applicable
naming authority, with unresolved treatment raised for review.

## Semantic containment and governed naming context

Model-tree folder placement SHALL NOT establish architectural containment.
View placement, nesting, adjacency and geometry SHALL NOT establish architectural
containment. Tooling SHALL NOT infer or create Composition from either structure.

An explicit, intentionally authored Composition establishes Harmonia semantic
containment **where that relationship is valid for the native endpoints and its
Harmonia meaning has been established and accepted**. A submitted Composition is
initially a proposal. Native legality alone does not resolve an outstanding
Harmonia metamodel decision. Whole/source and part/target direction is preserved.

```text
Folder hierarchy != semantic hierarchy
View hierarchy   != semantic hierarchy
Accepted valid Composition = explicit semantic containment
```

Semantic hierarchy SHALL remain reconstructable without folders or Views.
Governed naming MAY use explicit accepted containment as architectural context,
following the existing naming derivation rules. The governed Business Enabling
CT1 → Composition → CT2 → Composition → CT3 → Composition → Feature hierarchy
supplies semantic context under the [2026-10-19 metamodel rule](../03-business-architecture/metamodel/business-architecture-metamodel.md#bec-composition-hierarchy). An accepted
CT3 → Feature Composition establishes framing/semantic containment deterministically;
recognition of its meaning does not accept any incoming relationship, resolve
missing ancestry or promote standing.

Naming context SHALL derive from authoritative semantic architecture, including
other context explicitly established by existing governance or an accepted CR,
and SHALL NOT derive from arbitrary navigation. Existing source-qualified
`parent_key`/`owner_key` context retains its standing; this contract does not
convert those assertions into native Composition. Conflicting parents, ambiguous
context or insufficient semantic inputs needed for a name SHALL be raised through
Change Review. Tooling SHALL NOT choose ancestry by folder location or symmetry.

Not every element needs a Composition hierarchy. For an accepted element without
governed containment context, allocate identity under existing canonical identity
governance and derive only names established by the element and other applicable
authoritative rules. Existing local-name forms apply where governed. Do not invent
a semantic parent, additional containment or qualification to make names symmetric.
Semantic correctness takes precedence over naming symmetry.

## Author-supplied information and machine-governed metadata

Absence of machine-governed metadata on a **newly authored** Archi element is not
by itself an architectural defect. After architectural acceptance and resolution
of the required semantic inputs, authorised reconciliation MAY supply applicable
`canonicalId`, `displayName`, `friendlyName`, `canonicalName`, `archimateType`,
`harmoniaType` where established, and other already-required metadata. Apply the
[existing identity/naming syntax, persistence and history rules](canonical-identity-and-naming.md);
this contract introduces no alternative syntax or allocation mechanism.

Tooling SHALL NOT generate metadata whose semantic inputs remain unresolved.
Independent determinate metadata does not resolve a blocked interpretation.
Missing/new metadata is distinct from deletion, mutation or contradictory claims
against an established governed identity. Existing identity validation remains
strict. Ordinary publication reads and validates reconciled metadata; it SHALL
NOT allocate identity. The architect need not hand-build derived names or
governance Properties to submit a proposal.

## Explicit relationships, reuse and duplicates

The [Architecture Modelling Strategy](architecture-modelling-strategy.md#explicit-architectural-home)
establishes directed Association specialised as `Home` as a distinct authored
semantic relationship proposal. Detection and reconciliation SHALL preserve
native type, source, target, direction and specialisation, independently of
relationship Name. Resolve the Archi specialisation property through its
property definition; preserve its original representation as evidence.
Duplicate comparison must distinguish an ordinary Association from Home and
from other specialised assertions, and directed from undirected Associations.
No Realization, Serving, Flow, Composition, folder, View, name, proximity or
other contextual heuristic may infer Home. Replaying the fixed accepted
Business baseline Home register is not an ongoing inference mechanism.

Every explicitly authored semantic relationship SHALL be detected as an
architectural proposal. Preserve at least native relationship type, direction,
source, target and incoming representation identity/evidence, with locators to
the submitted records. Relationship folders, visual line placement, proximity
and model-tree organisation supply no relationship semantics. Native legality,
meaning, standing and acceptance follow the
[mapping relationship profile](rad-archimate-model-mapping.md#5-relationship-profile-and-direction)
and 2A review.

Ingestion SHALL distinguish:

| Observation | Governed treatment |
| --- | --- |
| New semantic relationship | Review the proposed edge against resolved endpoints and applicable authority. |
| New visual appearance of an existing semantic relationship | Reuse that governed relationship; review the View change as applicable, without adding another edge. |
| Attempted duplicate semantic relationship | Identify the duplicate against governed architecture; do not allocate another edge merely because the incoming representation ID differs. Preserve evidence and raise non-deterministic resolution through review. |

Once endpoint identities are resolved, semantic duplicate comparison SHALL
account for at least native relationship type, resolved governed source and
resolved governed target, preserving direction. Element `canonicalId` and
established governed bindings resolve endpoints; incoming Exchange IDs are
representation evidence, never architectural identity or duplicate authority.
Where endpoints cannot yet be resolved, duplicate status remains unresolved.

Preserve the mapping contract's reviewed assertion/crosswalk key and distinct
assertion discriminator where material; type/source/target equality alone must
not collapse separately governed meanings. Use an established governed
relationship identity/binding when available. This contract allocates no
relationship `canonicalId` and changes no existing relationship identity rule.

Resolve a View connection's `relationshipRef` and its View-local endpoints
separately from the semantic edge and its governed endpoints. Several
appearances, including in different Views, may refer to the same edge. Ambiguous
reuse, changed endpoints or competing meanings require Change Review rather
than a guessed merge.

## Relationship navigation and returned model organisation

The existing [source-based relationship home](archi-navigation-and-view-convention.md#relationship-homes-follow-the-source)
governs reconstruction: place a relationship in the Relations navigation
structure corresponding to the governed canonical home of its **source element**.
Incoming Relations folder placement is not semantic authority and need not be
replayed as publication organisation. Reproject navigation without changing
relationship meaning, direction, source, target or governed identity. Resolve an
unestablished source home through existing governance/review, rather than invent
semantic ancestry to obtain a path.

The coherent model returned to Archi SHALL use reconciled identities and
metadata, `Name == displayName`, source-based relationship homes, valid
references and separately governed View presentation. Publication does not turn
the returned Archi model into a replacement for RADS or authorise rejected or
unresolved incoming proposals.

## Views as projections and preservation of authored Views

A Harmonia View is a projection of governed architecture, not a source of
additional semantic architecture. Generation or reconstruction MAY add visual
nodes for governed elements and visual connections referencing existing governed
relationships. It SHALL reuse semantic relationships and SHALL NOT create an
edge because a diagram requires a line or infer containment from geometry.
If the desired relationship does not exist, its addition is a model/Change
Review matter, not a presentation-generation permission.

Authored/review Views are legitimate architectural presentation proposals.
Where a View change is accepted, preserve its authorised scope, identity and
presentation under [existing View governance](archi-navigation-and-view-convention.md),
including applicable subordinate projection metadata. Acceptance of presentation
does not accept or manufacture semantic architecture; dependencies on new edges
remain explicit under 2A.

Generated publication/navigation Views remain deterministic projections. The
mapping profile's description of Views as disposable deterministic projections
SHALL NOT be interpreted as permission to discard accepted authored Views.
Deterministic regeneration of generated Views and preservation/reconstruction of
accepted authored Views are distinct obligations. A controlled replacement
requires existing scope/identity-history authority.

## Deterministic Containment View

### Selection and semantic boundary

This archetype displays a selected containing/source element and its **direct**
governed Composition children relevant to the declared View purpose and standing
filter, plus the existing Composition relationships from that source to those
children. Selection inputs SHALL identify the source, purpose/filter and exact
expected children/relationships from governed architecture. Filtering is explicit;
it cannot hide an unsupported child count by silently dropping children.

For generated Capability hierarchy Views, eligibility follows the identifiable
source Capability, the declared purpose of presenting its immediate Capability
children, and accepted native Composition from that source. Existing visual
connections, legacy `context:`/`features:` roles, folders, ownership metadata and
layout SHALL neither establish nor prevent eligibility. A Harmonia Feature is a
native Capability: accepted CT3 → Feature Composition participates identically
to other Capability containment tiers, using the same archetype.

Projection SHALL determine complete direct-child membership from governed
Composition before creating visual references, placing nodes and routing
connections. Missing visual objects reference existing semantic identities;
presentation SHALL NOT create or infer Composition from membership, Home,
responsibility, Realization, folders or proximity. Accepted architect-authored
presentation takes precedence over generated migration eligibility. The
Solution Architect's RAD-1.0.9 scope correction applies this rule to the unsigned
candidate; historical-reference and uncertain diagnostic exclusions remain,
and more-than-20-child presentation remains unresolved.

Include the selected source exactly once and each selected direct child once.
Do not automatically include grandchildren, siblings, folder neighbours or other
relationships. A broader archetype requires separate governance. This View uses
accepted valid Composition only; it neither establishes CT3/Feature framing nor
creates, converts or duplicates semantic edges.

### Layout for 1–20 direct children

Place the source/container on the **left**, with children to its **right**,
arranged vertically. Render existing Composition relationships with the
conventional ArchiMate diamond at the whole/source side. Route clearly without
crossing unrelated node rectangles.

| Selected direct children | Required presentation |
| --- | --- |
| 1–10 | One vertical child column to the right of the source. |
| 11–20 | Two nonempty child columns; the second lies further right. Offset second-column node centres into routing corridors/gaps between first-column nodes, and route their connections through those corridors. |
| More than 20 | Unsupported/unresolved; report for presentation adjudication. Do not generate a third column, split Views, paginate or substitute another layout. |

For two columns, the partition and ordering SHALL be deterministic and provide
enough actual gaps for the second column; this contract imposes no ten-node cap
on the first column. Routing SHALL be calculated from actual node rectangles,
sizes and corridor clearance, rather than assumed fixed offsets. Second-column
paths SHALL NOT intersect first-column nodes. A renderer unable to satisfy the
geometry SHALL report nonconformance rather than silently publish obstructed
connections or omit children.

Use existing stable representation-identity conventions. Equivalent governed
selection and presentation inputs SHALL produce the same membership, visual
references, ordering, coordinates and routes on repeated generation. Detailed
node metrics and routing implementation are downstream work; no layout engine
is established here. Existing historical grids and accepted authored layouts
are not migrated merely by defining this distinct archetype.

### Conformance expectations

Later automated conformance SHALL establish at least:

- The expected source exists exactly once, all expected direct children exist
  once, and no unrelated or deeper nodes appear.
- Displayed Composition connections reference existing governed semantic
  relationships, with correct source/target and View-local visual endpoints;
  the displayed relationship set matches the declared selection.
- Generation creates no semantic relationship: compare governed semantic
  records/identities/endpoints before and after, independently of Views.
- Node rectangles do not overlap. Routed paths do not intersect unrelated node
  rectangles, including first-column nodes en route to second-column targets.
  Connections meet their own endpoints at the appropriate boundary.
- Source-left orientation, column count, vertical ordering, second-column
  offsets/corridors and source-side Composition diamond conform to the count
  and actual geometry.
- Repeated generation preserves deterministic output and valid references;
  more than 20 children yields an explicit unsupported/unresolved finding.

Schema/reference validation does not substitute for semantic or geometry checks.
Human Archi import/rendering acceptance remains a separate existing publication
gate. These expectations specify future tests, not tests of an implemented 2B
layout engine.

## Bounded unresolved architectural matters

The common CT3 → Feature native framing question was **resolved by the Solution
Architect on 2026-10-19** in the [Business metamodel rule](../03-business-architecture/metamodel/business-architecture-metamodel.md#bec-composition-hierarchy), reflected in
[mapping profile §5](rad-archimate-model-mapping.md#5-relationship-profile-and-direction).
Accepted native Composition establishes CT3 framing/semantic containment; the
blind-test relationship instances remain unadjudicated. This general rule does
not resolve the following matters.

| Matter | Current authority and required adjudication |
| --- | --- |
| Realization → Feature as logical context/containment | **Resolved by the Solution Architect's [explicit Home decision](architecture-modelling-strategy.md#explicit-architectural-home), 2026-10-19.** Realization SHALL NOT by itself establish Home, logical containment, parentage, ownership, naming context or navigation context. Home requires a separate explicit directed Association specialised as `Home`. |
| Containment View with more than 20 direct children | **Unresolved.** No presentation strategy is established; no third column, splitting, pagination or alternative is authorised. |

Retain native Realization when Home is independently authored or reconciled.
Neither conversion to Composition nor an additional Composition encodes Home.
The [2A evidence/interpretation distinction](archimate-ingestion-change-review.md#architectural-questions-and-separate-interpretations)
remains applicable. Home cardinality, exhaustive endpoint types, inheritance,
automatic naming/navigation and Information/Application rules remain deferred
by the modelling strategy.

## Adoption, conformance findings and stop boundary

The Name/displayName discrepancy exemplified by native `Identifier Resolution`
versus `displayName=[Feature] Identifier Resolution` is a publication conformance
finding. The universal invariant does not authorise rewriting historical
publications, accepted source architecture, blind-test XML or observed evidence.
Record the affected governed representations for a separately authorised
migration, retaining identity, naming history and unchanged submission evidence.
No existing release rule requires that model migration as part of this governance
adoption. A preservation-only reproduction of the old publisher is validation
evidence, not a claim of conformance to this new invariant or View archetype.

Apply the existing patch-release and validation gates to this governance change.
Report legacy publisher/conformance limitations separately from successful
identity, reference, source-preservation and deterministic-reproduction checks.
This increment ends after formalisation, release handling, bounded validation
and the implementation report. Do not reconcile or adjudicate current blind-test
changes, allocate their identities, implement layout or begin Increment 2C.
