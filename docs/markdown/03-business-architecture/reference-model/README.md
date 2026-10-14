<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Business Architecture reference candidate model

This is a machine-readable **reference candidate capture** of the Strategy and
Business Architecture documented on `Harmonia-GovernedAccess`, with the Business
Capability side reconciled on 2026-10-14. It
supports navigation, validation, contextual diagrams and change-impact queries.
Inclusion supplies no architectural approval or missing decomposition decision.
The [Business Architecture Metamodel](../metamodel/business-architecture-metamodel.md),
especially §§1–3 and 8, governs meaning; the cited source documents govern each
captured assertion. AX-14, AX-15 and AX-17 preserve distinctions and uncertainty.

The existing RADS has Markdown catalogues, explicit source references and
embedded diagrams, with no machine-readable catalogue/schema or RADS generator.
This representation stays within Domain03, uses those sources and adds only the
required representation contract and a dependency-free validation/navigation
tool. It introduces no application domain model, runtime catalogue, module or
deployment boundary. It does not generate architecture from Markdown structure.

## Representation and authority

[business-architecture.json](business-architecture.json) holds the capture;
[business-architecture.schema.json](business-architecture.schema.json) is its
JSON Schema 2020-12 representation contract. The root `capture_status: candidate`
is independent of an element's `status: established`: the latter records an
existing documented identity/responsibility, not approval conferred by this
capture. These terms follow the existing RADS established/candidate/unresolved
distinctions; they introduce no new approval process.

| Collection/property | Meaning |
| :--- | :--- |
| `elements` | Independently established elements and explicitly identified candidate BC concerns with unresolved placement. `status` distinguishes them. `category` distinguishes Business from Business Enabling Capabilities; it is not a tier. |
| `key` | A stable internal record reference, such as `capability:person-identity`. It is neither an allocated Canonical ID nor an ID Alias. Preserve it across renames/relocations. |
| `name`, `type`, `status` | Current documented architectural name, semantic type and standing of the captured element assertion. |
| `tier` | `CT1`, `CT2`, `CT3` for a Capability when independently established; `FT` for a Feature. Explicit `null` on a Capability means unresolved. Behaviour carries no tier. |
| `canonical_id` | Complete structural Canonical ID, or explicit `null` with `canonical_id` in `unresolved`. Never populate it from display numbers or an internal record key. |
| `local_id` | An already-established local token, preserving spelling such as `FEAT-EM-01`. It does not prove an allocated complete Canonical ID. |
| `name_aliases`, `id_aliases` | Retained names or historical structural IDs for the same element. Each alias is attached directly to its current element; there are no alias chains. Omission means no retained alias. |
| `owner` | Mandatory established Capability responsibility for each Feature, and Capability or Feature responsibility for each Function, Service and Process. Flat storage retains ownership. |
| `capability_parent` | Only an independently evidenced Capability decomposition/partial parent. Absence while ancestry is unresolved does not declare a root. |
| `ancestry_status`, `unresolved` | Whether complete structural ancestry is established, plus separately recorded unestablished dimensions. Definitions are in `unresolved_meanings`. A known parent does not establish complete ancestry or its tier. |
| `views` | Membership references to Business regions or Business Enabling contextual views. Membership is separate from ownership and Capability ancestry. |
| `catalogue_contexts` | Named decomposition/responsibility descriptions preserved as documented. Their `container` records catalogue presentation, not architectural ancestry. `element: null` and `element_status: unresolved` preserve unestablished standalone Capability identity. |
| `catalogue_context` | A Feature's source decomposition description. Its `owner` remains the independently established Capability context; an unresolved description never becomes an invented intermediate owner. |
| `relationships` | Typed, source-evidenced relationships; `status: unresolved` records a specifically unestablished association with a reason. It is not an approved edge. |
| `sources` | RADS paths and SHA-256 fingerprints, with exact line/text and heading-anchor locators on each record/relationship. These locate evidence and reveal source drift. |
| `summary` | A bounded source description when available. It does not replace source definitions, qualifications, authority boundaries or illustrative/non-normative distinctions. Absence does not authorise an inferred definition. |

An element's existence, numerical Capability Tier and complete Canonical ID are
independent assertions. The capture currently contains **190 established Business
Capabilities** (**18 CT1**, **39 CT2**, **133 CT3**) and **three candidate BC concerns
with unresolved tier/placement**: Health Service Operational Readiness, Clinical
Device Maintenance Management and Health Information Sharing Programme Management.
The [canonical BC catalogue](../../02-strategy/capabilities/business-capabilities.md)
governs their distinctions and the preserved uncertainties. It also contains
**76 established Business Enabling Capability contexts**, **137 FT
Features**, **147 Functions**, **133 Services** and **19 Processes**. Seventy-three
named enabling decomposition contexts are preserved separately: four link to
independently established Capabilities and **69 have unresolved standalone
Architectural Element identity**. Counting those 69 as approved Capabilities
would contradict [K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing).

No complete structural Canonical ID or new local code is allocated. BC tiers,
root standing and parents come from **explicit canonical assertions**, independently
of ordinals, nesting and indentation. The six Individual Care Delivery CT2
identities retain their record keys and definitions; their adjudicated assessment,
monitoring and review CT3s are now captured. The other three clinical CT2s retain
their unestablished/provisional CT3 detail. The three candidate concerns have no
parent or region assigned, and no compounding/preparation Capability is created.
All 137 Feature names, local identifiers, FT classification and ownership remain
unchanged. All BEC tiers and complete ancestry remain unresolved.

The [approved fixed-decomposition decision](../../02-strategy/reviews/fixed-capability-decomposition-decision.md)
as clarified by the metamodel requires **BEC CT1 → CT2 → CT3 → FT** for a fully reconciled Feature: Business Enabling CT3 is the
mandatory immediate Capability frame, with CT2 and CT1 ancestry. No Feature may
compensate for missing decomposition, and no tier may be skipped. The capture's
existing `owner` and partial-parent assertions preserve established responsibility
evidence; they do not assert that an owner is CT3 or that the Feature is
structurally reconciled. All 137 Features' required Capability frames/complete
ancestry remain unresolved pending separate architectural reconciliation. The
69 unresolved enabling descriptions remain unresolved descriptions, not newly
established Capabilities. BCs do not decompose into Harmonia Features and no new
BC↔BEC support edge is established. Existing B2.1 and assurance support assertions
are retained without extension to the new descendants. BC decomposition adds
**166 established parent relationships** to the six previously captured BC parent
relationships, for **172**. The representation schema already supports these
distinct assertions and requires no change.

**Health Information Exchange** now identifies both an adjudicated BC CT2 and an
existing BEC. The records retain separate keys, categories and meanings with
explicit `name_uniqueness` uncertainty. This is a reported conflict with global
name uniqueness and the BC/BEC naming boundary, not an identity merger, alias,
support mapping or approved uniqueness exception. Bare-name lookup is ambiguous;
use a record key. The naming point requires adjudication in the later BEC phase.
The defined Maintenance concern omitted from DN-BC-015's seven-child set remains
candidate/unplaced. Neither unresolved point is declared reconciled.

Canonical BC evidence/context/consumption relationships are preserved in Markdown.
The current governed relationship vocabulary does not encode those precise
semantics; `semantic_relationship_representation` records that unresolved capture
dimension rather than substituting a structural, runtime or Service edge. No new
relationship type or downstream behavioural element is invented.

## Responsibility, contextual views and behaviour

The five Business Enabling views and five natural Business regions are `views`
records, never Capabilities or identifier ancestors. The three assurance enabling
Capabilities have no assigned view. The separate Domain03 operational
`Credential Management` context is retained with explicit unresolved Strategy
catalogue correspondence; it remains distinct from `Clinical Qualification
Management` and its allowed historical Name Alias.

Reported source conflicts remain explicitly flagged as unresolved. In particular,
the existing `Coordinate Care-Team Collaboration` description mentions managing
clinical handovers while metamodel §3.7 excludes clinical-handover management.
Both source assertions are preserved for review; the capture makes no correction
or authority allocation.

`Client Administration → Person Identity → Identifier Resolution [FT /
FEAT-EM-01]` is represented through the explicit partial Capability parent and
the Feature's owner. Both Capability tiers, root status and full structural IDs
remain unresolved. Other named enabling decomposition descriptions are retained
without assuming they establish independent CT2/CT3 elements. For example,
`Health Information Communication → Boundary Protocol Mediation` remains the
documented catalogue context, without a semantic correction or a new Capability.

Functions and Services remain owned by the explicitly declared Domain03
Capability; their established Feature associations are separate typed edges.
This preserves broader compound behaviour and explicitly rejected Feature
associations. A Function need not have a Feature association to be valid.
Contribution relationships (such as Ward patient flow's contribution to Ward
Capacity) remain distinct from whole-Feature realisation. Proposed names, stale
abbreviations and similar terminology do not become aliases.

`governed_process`, `participating_function`, `uses_process` and `exposes_process`
record only explicit declarations. Service/Function juxtaposition does not
generate an internal mapping. Assurance Process Design's participating criteria
Function keeps its own Capability owner. Service, Function and Process remain a
graph; there is no mandatory Service → Process → Function tree. No exhaustive
traceability or downstream implementation allocation is generated.

## Validation and navigation

Run from the repository root:

```bash
python3 scripts/rads_business_architecture.py validate
python3 scripts/rads_business_architecture.py summary
python3 scripts/rads_business_architecture.py list --category business
python3 scripts/rads_business_architecture.py list --type capability --view "Entity Management"
python3 scripts/rads_business_architecture.py contexts --view "Entity Management"
python3 scripts/rads_business_architecture.py show FEAT-EM-01
python3 scripts/rads_business_architecture.py impact "Person Identity"
python3 scripts/rads_business_architecture.py unresolved
python3 scripts/rads_business_architecture.py diagram "Service Delivery" > /tmp/service-delivery.mmd
timeout 60s python3 -m unittest discover -s scripts/tests -p 'test_rads_business_architecture.py' -v
```

The [tool](../../../../scripts/rads_business_architecture.py) validates the
published schema using its explicitly supported vocabulary and standard Python
only. Unknown schema keywords fail explicitly. It also validates Canonical ID
and ID Alias uniqueness/collisions, token grammar, ancestor existence, complete
typed ancestry when claimed, ownership, Feature type, contextual references,
relationship endpoint types, cycles, direct alias resolution, source locators,
fingerprints and scoped name uniqueness where the CT1 root is known. Historical
ID Alias paths need not identify *current* ancestors; they resolve directly to
the current element. Local tokens are distinct from the globally unique complete
Canonical ID/ID Alias namespace. Ambiguous name/local-token lookups require a
record key or complete Canonical ID.

`validate` returns **PASS** (exit 0) when representation constraints hold,
reporting `unresolved_by_architectural_decision` separately. This count is of
unresolved dimensions/relationships, not unique elements. Invalid structure,
an orphan, an unresolvable model reference, or contradictory identity assertion
returns **INVALID** (exit 1). An architecturally unestablished association is
valid only with existing referenced records and an explicit uncertainty reason;
a misspelled reference is still invalid. Source drift means the capture evidence
requires reconciliation, not that the architecture itself has been rejected.
Unknown CT1 Feature-name scope is reported unresolved rather than given an
invented root. JSON Schema validation alone cannot perform the graph checks.

**Enforcement and source-reconciliation limits:** the validator now rejects
complete CT1→Feature and CT2→Feature paths, including behaviour beneath those
invalid paths, and checks the immediate established BEC CT3 owner even before
IDs are allocated. Capability decomposition cannot cross BC/BEC categories.
Direct Function/Service/Process anchoring remains valid at any established
Capability tier. Historical BEC responsibility with unresolved Feature ancestry
remains valid evidence; PASS does not frame the 137 Features or complete BEC
reconciliation. An explicitly reported cross-category name collision remains
unresolved only when both records preserve the conflict; unreported duplicate
names still fail. PASS with uncertainty does not establish CM-R05 conformance.

Sources were reconciled semantically before fingerprints and locators were
updated. Incoming drift from the prior fixed-decomposition clarification was
reviewed as well: unchanged evidence assertions/owners remain intact while their
positions move. K9's supersession and B2.1's unchanged bounded support do not
allocate BEC ancestry or extend traceability. Later source drift still requires
semantic reconciliation and SHALL NOT be concealed by mechanical hash refresh.

`impact` follows recorded ownership, partial parents and confirmed dependants to
show a conservative impact set, returning unresolved mentions separately. It is
bounded by captured evidence, not a claim of exhaustive dependency coverage.
Contextual diagrams label missing identity/tier and draw unestablished
associations as dashed, explicitly **UNRESOLVED** edges. Catalogue descriptions
are not drawn as Capability ancestors. These views can be regenerated from the
JSON; the JSON itself must be reconciled against architectural authority.

## Subsequent maintenance

Review source changes semantically before updating records and fingerprints;
do not merely refresh hashes to make validation pass. Preserve source evidence
for each identity, owner and relationship. When tier/ancestry/code allocation is
approved, update the distinct assertions, retain the existing local spelling,
and supply every established ancestor required by the metamodel grammar. A
rename retains `key` and identity; a responsibility relocation updates its
complete Canonical ID and retains historical IDs directly as aliases, including
affected descendants. New decisions must remain distinguishable from this
reference candidate. The capture is neither a Capability Model redesign nor a
declaration of Domain03/programme completion.
