<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Business Architecture reference candidate model

This is a machine-readable **reference candidate capture** of the Strategy and
Business Architecture documented on `Harmonia-GovernedAccess`, 2026-10-13. It
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
| `elements` | Independently established Capabilities, Features, Functions, Services and Processes. `category` distinguishes Business from Business Enabling Capabilities; it is not a tier. |
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
independent assertions. The capture currently contains **24 Business
Capabilities** (18 catalogue entries and the six Individual Care Delivery
children), **76 established Business Enabling Capability contexts**, **137 FT
Features**, **147 Functions**, **133 Services** and **19 Processes**. Seventy-three
named enabling decomposition contexts are preserved separately: four link to
independently established Capabilities and **69 have unresolved standalone
Architectural Element identity**. Counting those 69 as approved Capabilities
would contradict [K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing).

No complete structural Canonical ID or CT1/CT2/CT3 allocation is asserted in this
capture. All 137 local Feature identifiers and their FT classification remain
established. The Business catalogue's explicit L1/L2 statements are retained as
`documented_tier_label`, distinct from allocated `tier`; the approved catalogue
presentation does not supply CT ancestry/root allocation. The six **agreed
Capability decomposition** relationships from Individual Care Delivery are
preserved, including their definitions and L2 display labels. No CT3 detail or
care-setting Capability is created. This conservative capture leaves the
deprecated labels/current CT reconciliation visible for subsequent adjudication.

The [approved fixed-decomposition decision](../../02-strategy/reviews/fixed-capability-decomposition-decision.md)
requires **CT1 → CT2 → CT3 → FT** for a fully reconciled Feature: CT3 is the
mandatory immediate Capability frame, with CT2 and CT1 ancestry. No Feature may
compensate for missing decomposition, and no tier may be skipped. The capture's
existing `owner` and partial-parent assertions preserve established responsibility
evidence; they do not assert that an owner is CT3 or that the Feature is
structurally reconciled. All 137 Features' required Capability frames/complete
ancestry remain unresolved pending separate architectural reconciliation. The
69 unresolved enabling descriptions remain unresolved descriptions, not newly
established Capabilities. The JSON capture, schema, local IDs and existing
relationships are unchanged by this metamodel/documentation decision.

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

**Enforcement and source-reconciliation limits:** the existing tool and schema
predate the fixed-decomposition decision. Their permissive Feature path handling
still accepts Features directly under resolved CT1 or CT2; the validator test
`test_complete_typed_ancestry_and_direct_behaviour_anchoring` explicitly exercises
that former permission. A representation PASS therefore does not establish
conformance to the new mandatory CT3 Feature frame. Updating that enforcement
requires separate tooling work; it does not authorise allocating tiers to the
capture. Documentation changes also invalidate stored source fingerprints and
may invalidate line/text/anchor locators. Resulting source-drift diagnostics
record a capture-reconciliation need, not a semantic failure of the new rule.
The capture SHALL NOT be mechanically refreshed to conceal these consequences.

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
