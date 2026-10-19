<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Architecture Modelling Strategy

## Authority and semantic compatibility

**Accepted Solution Architect decision — 2026-10-19, governance release 1.0.14.**
The [Architectural Axioms](architectural-axioms.md) remain controlling. AX-04
separates architectural semantics from machinery; AX-14 preserves meaningful
distinctions; AX-17 preserves architectural authority and explicit uncertainty.
This decision is consistent with those axioms: it makes an existing kind of
architectural assertion explicit without deriving authority from a tool.

Harmonia deliberately constrains the flexibility available in ArchiMate so that
architectural meaning remains consistent and machine-processable. A Harmonia
specialisation of an ArchiMate element or relationship SHALL remain semantically
compatible with its underlying native ArchiMate concept. Specialisation MAY
narrow or qualify native meaning; it SHALL NOT contradict or repurpose it.
Native concepts retain their native semantics. Where a Harmonia definition has
no faithful native representation, its representation requires an explicit
modelling decision rather than a reinterpretation of a native concept.

## Explicit architectural Home

Harmonia SHALL use a **directed ArchiMate Association specialised as `Home`** to
explicitly identify the logical architectural home of an element where that
home cannot be faithfully represented by a native ArchiMate structural
relationship. The direction is:

```text
element -- directed Association «Home» --> architectural home
```

For example, the Solution Architect explicitly authored:

```text
[Business Process] Identifier Verification
    -- directed Association «Home» --> [Feature] Identifier Capture
```

Association supplies an associated relationship between the endpoints; `Home`
qualifies that association with the Harmonia logical-belonging predicate. It
does not make Association a structural relationship. `Home` is the **Archi
relationship specialisation**, not an arbitrary relationship Name. A relationship
Name SHOULD remain absent unless it has a separately meaningful architectural
purpose.

Home SHALL NOT mean Composition or structural containment, Realization, Serving,
ownership or governance authority, or dependency. It SHALL NOT be inferred as a
consequence of any of them. Home is an additional, orthogonal architectural
assertion; it replaces no valid native semantic relationship.

### Independence from Realization and presentation

Realization retains its native ArchiMate semantics: it states what architectural
element or elements the source realises. An element may realise several
Features. Native Realization SHALL NOT, by itself, establish **Home, logical
containment, parentage, ownership, naming context or navigation context**.
An explicit Home and a Realization between the same endpoints assert different
things and may coexist.

Folder/model-tree placement and View placement are not substitutes for an
explicit Home. Realization, Serving, Flow, Composition, folders, Views, names,
graphical proximity and other contextual heuristics SHALL NOT generate Home.
Having only one Realization does not establish Home. Prospective Home assertions
SHALL be explicitly authored and reviewed under the
[Change Review contract](archimate-ingestion-change-review.md).

### Interchange evidence

The Solution Architect's current
[1.0.8 submission](../../archimate/exchange/ingest/1.0.8/HarmoniaArchitecture-1.0.8.xml)
contains four actual authored examples, independently of the earlier experiment.
Open Exchange preserves native `Association`, source, target, direction through
`isDirected="true"`, and the Archi specialisation through the relationship
property `specialization = Home`. In this export the property's definition ID
is `specialization` and its displayed definition name is `Specialization`.
Property-definition references must be resolved rather than treated as a
universal fixed identifier. This is empirical interchange evidence, **not the
architecture definition of Home**. Other serializations must preserve the same
architectural assertion.

## One-time Business baseline normalisation

This accepted task authorises a one-time materialisation of deterministically
established logical Feature homes for **existing governed** Business Functions,
Business Processes and Business Services. It authorises no new architectural
meaning and no additional Home for new 1.0.8 elements inferred from Realization.
The Solution Architect's four authored relationships remain unchanged.

The [fixed Business Home register](baseline/business-homes.json) records every
existing Business behaviour assessment, exact governed identities, source
statements, standing, and unresolved or competing candidate contexts. Its
accepted rows use explicit Business catalogue **Functions & Exposed Services /
Feature responsibility allocations**, corroborated by the reconciled baseline's
exact source keys and Feature bindings. A Process requires its own explicit
`Governed Process` allocation in that responsibility statement; it does not
inherit Home through a Function. A sole Realization, association edge, name,
heading or navigation path supplies no migration authority. Conflicting or
incomplete evidence remains for Solution Architect adjudication. Proposed
Feature ancestry or other retained standing is not promoted by normalisation.

This register is the accepted migration result, not a prospective inference
rule. Publication replays only its literal accepted relationships; it does not
discover additional homes from changed catalogues or relationships. New or
changed Home requires an explicitly reviewed assertion. The original capture,
all native relationships, and historical publications remain preserved.

## Home-derived naming and navigation

**Subsequent cross-layer adjudication:** the naming-context aspect of this earlier
Business Home decision is subject to the [Composition invariant below](#cross-layer-semantic-containment-and-contextual-naming).
Home remains a distinct Business assertion and navigation mechanism. It does not
create Composition ancestry or override established semantic containment. The
earlier candidate's persisted names/navigation remain preserved in this bounded
IA completion; no repository-wide naming migration is authorised here.

**Solution Architect decision — 2026-10-19, completion of unsigned RAD-1.0.9.**
For a governed Business Function, Business Process or Business Service with one
established explicit Home to a Feature, that Home and its governed semantic
ancestry SHALL supply the authoritative context for `friendlyName`,
`canonicalName` and the principal model-tree navigation location. Explicit Home
takes precedence over a differing responsibility/owner-derived context for these
projections. Responsibility and Home remain independent assertions: this
precedence changes neither responsibility nor ownership, Realization, Composition,
architectural standing or identity.

Both qualified names SHALL use the same path: governed semantic ancestry of Home
→ Home Feature → local element. The [naming contract](canonical-identity-and-naming.md)
supplies the existing lexical rules and formal qualifiers; no individual name or
new lexical rule is prescribed. `displayName` is unaffected and `canonicalId`
is immutable. Proposed ancestry remains Proposed; projection does not approve it.

The [navigation convention](archi-navigation-and-view-convention.md#explicit-home-projection)
supplies the existing Business context and Functions/Processes/Services folders.
The authority direction is architecture → explicit Home → navigation projection.
Folders do not establish Home, Composition, ownership, responsibility or Realization.
Relationship navigation follows the resulting established location of its source.
This decision changes no generated View presentation.

Apply only a single established explicit Home; never infer one. Multiple explicit
Homes, invalid targets, missing/cyclic/ambiguous semantic ancestry, required
promotion of an ancestor, or a requirement for new containment or lexical rules
require stopping that case and reporting it for adjudication. Preserve the existing
context of all other elements. The 93 unestablished and 18 ambiguous baseline
Home cases remain unresolved, non-blocking and untouched.

The frozen baseline and accepted-ingestion captures, reconciliation records and
audit evidence remain unchanged. [Current resource naming amendments](baseline/home-derived-metadata.json)
persist the derived fields against each existing resource key and canonical identity,
with its prior names, explicit Home relationship and semantic context. They are
amendments to those resources, not an identity register or new Home assertions.
Ordinary publication validates these fields without repair or identity allocation.
The [completion evidence](../../archimate/processing/publishing/1.0.9/home-context-completion/README.md)
retains the pre-decision candidate and verification of the complete population.
This completes RAD-1.0.9; it does not create RAD-1.0.10 or grant final sign-off.

## Cross-layer semantic containment and contextual naming

**Solution Architect adjudication — 2026-10-19, RAD-1.0.10 candidate completion.**
Harmonia uses native ArchiMate **Composition** as its governed semantic
containment relationship across all architectural layers. Directed Composition
from parent to child establishes the child's governed semantic containment
context. The resulting Composition ancestry is the authoritative contextual
path for friendlyName and canonicalName, using the existing naming derivation
and the applicable element-class qualifier at each component. This applies
equally to Business, Information/Application and Technology architecture; it
is not an IA specialisation.

Folders, View membership, View nesting/geometry, lexical similarity and proximity
SHALL NOT establish or infer containment. Home, responsibility, Realization and
other relationships SHALL NOT be silently converted to Composition. Conflicting
or incomplete ancestry required for a name remains unresolved; tooling SHALL NOT
choose or manufacture a context to satisfy a naming diagnostic.

No Composition is created for the 54 initial IA resources by this adjudication.
The Solution Architect will author containment in Archi. Where a resource needs
that semantic ancestry before its contextual names can be determined, retain
the unresolved property until the authored containment is accepted and ingested.
Existing local-name rules apply only where their semantic inputs are sufficient;
absence of an authored parent must not be treated as proof of a semantic root.
The candidate's other identities, elements, relationships, Views, navigation and
presentation are preserved. This decision authorises neither physical population
nor an Artifact/ARTEFACT naming qualifier.

**Solution Architect adjudication — existing Business modelling debt:** preserve
the existing contextual names of the 323 Business behaviour elements without
direct Composition containment unchanged. This is acknowledged modelling debt,
accepted as non-blocking for the current task. Do not infer, create or migrate
Composition relationships for them. The Solution Architect will progressively
establish their containment hierarchy during architectural modelling. Once
explicit Composition containment exists, normal contextual naming SHALL apply
from that governed ancestry. This acceptance does not declare their absent
containment complete or waive the cross-layer invariant.

## Deliberately deferred decisions

This decision establishes no general mandatory cardinality or requirement that
every applicable element have a Home; no exhaustive source/target type lists;
no Home inheritance; no consequences beyond the bounded naming/navigation
decision above, and no View consequences; and no
Information Architecture or Application Architecture Home rules. The current
Business examples target Features, native `Capability`; they do not establish
an exhaustive target restriction. General rules require a separate Solution
Architect decision. The existing CT3 → Feature Composition rule remains distinct.
