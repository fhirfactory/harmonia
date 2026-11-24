<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Initial Information Architecture element projection

## Authority and bounded decision

The Solution Architect's **Establish Initial Information Architecture Element
Population** task (2026-10-19) authorises this projection from current RAD-1.0.9
and its existing ArchiMate model. The normal patch change set is RAD-1.0.10.
This decision establishes representation and navigation, not new information
meaning. The repository's existing definitions remain authoritative. Discovery
reports, implementation and legacy persistence structures establish no new objects.

AX-01/04 preserve information meaning independently of machinery; AX-14 preserves
conceptual/logical/physical distinctions, source standing and independent authority;
AX-17 requires unresolved identity, names, representations and context to remain
explicit. AX-05/06/08/09/13/18 additionally preserve the source-defined active/durable,
information authority, evidence, transience and external-domain boundaries. No
axiom conflict was identified in the selected element-only projection.

## Native representation and eligibility

An independently meaningful established conceptual information object maps to
native `BusinessObject`; an established logical/application information object
maps to native `DataObject`. No Harmonia specialisation is introduced. This
increment creates no native `Artifact` and makes no physical-information allocation.
No corresponding object at another abstraction level is required.

Each resource in [the retained capture](baseline/information-architecture.json)
records its exact selected RADS section, assertion, fingerprint and qualified
standing. Headings locate reviewed definitions; they do not automatically type or
admit elements. The capture is a deterministic projection selection and metadata
source, not an independent redesign of Information Architecture. Residual source
questions remain unresolved. G2 decisions admit only their explicitly approved
meanings; they do not approve the candidate families or package. Bounded adjudicated
Domain05 logical meaning retains the candidate Step-B baseline standing and does
not become final Solution Architecture or an implementation schema.

## Identity and names

New resource-owned immutable `canonicalId` values follow the existing
[identity contract](canonical-identity-and-naming.md): random UUIDv4, persisted
once as UUID32. The existing identity machinery derives and binds the separate
Exchange representation ID. Allocation history is retained under
`docs/archimate/processing/publishing/1.0.10/information-population/allocation/`.
Publication allocates zero identities. All prior element, relationship, View,
visual-object and organisation identities remain unchanged.

Governed native Name/displayName use `<Base Name> BO` for conceptual objects and
`<Base Name> Topic` for logical objects. `<Base Name> Artefact` is the physical
naming convention only; no such element is created here. Information-object Names
must be model-wide unique. A collision stops both dependent interpretations for
architect adjudication; names are not invented and concepts are not merged.
Shared lexical bases establish no equivalence or cross-level traceability.

The existing local PascalCase lexical rule supplies unqualified `friendlyName`
from the governed suffixed Name. This uses the contract's local form in the absence
of established semantic qualification; it asserts no IA ancestry. Folder names,
family names, responsibility and Business Home do not supply IA semantic context.
The initial canonicalName vocabulary stop on all 54 resources is resolved by the
Solution Architect's **IA canonical-name qualifiers** adjudication (2026-10-19).
The [governed vocabulary](canonical-identity-and-naming.md#information-object-canonical-name-vocabulary)
now supplies `BO` for the conceptual BusinessObjects and `TOPIC` for logical
DataObjects in InformationMap. The unchanged local qualification rule prefixes
the existing friendlyName component: `BO-PractitionerBo`, `TOPIC-ContextTopic`.
The source-owned records persist and validate all three names. Original allocation
history and the pre-adjudication candidate remain preserved; no identity is
allocated or changed by completion. No Artifact/ARTEFACT qualifier is authorised.
RAD-1.0.10 remains the same unsigned candidate under the explicit release decision.
The later [cross-layer Composition invariant](architecture-modelling-strategy.md#cross-layer-semantic-containment-and-contextual-naming)
governs contextual names. The unchanged local IA forms assert no semantic root or
parent. Where a correct contextual name requires ancestry yet to be authored in
Archi, preserve the dependent property unresolved until containment is ingested;
never manufacture a path from the flat population, folders, Views or family names.
This increment creates no Composition and does not migrate prior Business names.

## Navigation and explicit exclusions

The agreed View-tree locations are:

```text
2-BusinessLayer
    1-BusinessCapabilities
    2-BusinessEnablingCapabilities
    3-BusinessObjectMap
3-ApplicationLayer
    1-InformationMap
    2-EnterpriseCapabilities
    3-ApplicationMap
    4-MiddlewareMap
4-TechnologyLayer
    1-DataMap
```

Archi's native resource roots remain Business, Application and Technology & Physical, under
the existing importer convention. New element references are flat in
Business → BusinessObjectMap and Application → InformationMap. Technology & Physical →
DataMap remains empty. Only the existing empty Information Map resource folder and
three immediate View-category labels change spelling to match the agreed structure;
their identities are retained. No existing resource or diagram is relocated.
The new BusinessObjectMap and DataMap View folders are empty workbench locations.
No IA diagram Views, visual nodes, lines, grouping elements, family hierarchy,
ownership, stewardship or Home assertion is created.

There are **no new semantic relationships**, including source-evidenced relationships:
Composition, Aggregation, Association, Realization, Access, Assignment and all other
edges are excluded from this task. Reified relationship *information objects*,
where independently defined, remain BusinessObjects with no edges.

Endpoint BO remains unresolved between the Practitioner and Device definitions.
Candidate resource inventories and qualified/unresolved representations do not
acquire approval through projection. The [task report](../../../.junie/reports/2026-10-19-initial-information-architecture-population.md)
records the complete admitted population, exclusions, evidence and remaining decisions.

## Stop boundary

The Solution Architect will author IA organisation, relationships and Views in
Archi. That model must supply future IA ingestion, reconciliation, publication and
round-trip evidence. This increment proves only deterministic element publication
and applicable no-edit transport; it does not establish an IA ingestion/reconciliation
contract or claim human rendering acceptance. Unrelated architecture/presentation
findings remain unchanged.
