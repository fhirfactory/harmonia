<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Governed Architecture Identity

## Purpose, authority and release boundary

Identity belongs to the architectural construct, not its document, catalogue
record, diagram, View, generator or implementation representation. One construct
has one persistent identity and may have many descriptions and representations.
This model applies across the RAD, not just the ArchiMate publisher.

The [Architectural Axioms](../architectural-axioms.md), especially AX-04, AX-14 and
AX-17, govern this model. Engine identity cannot redefine architecture; source,
navigation, implementation and architectural identity remain distinct; absent
identity and relationships remain unresolved. This release defines identity
governance and validation, without changing architectural responsibilities.
The [release authority](../../../../architecture-release.json) was incremented
using the established mechanism from 1.0.1 to 1.0.2. Maven is independent.

The [inventory and collision report](inventory.md) was produced before allocation.
It is evidence for review, not a new semantic catalogue. **This release allocates
and migrates zero construct identities.** [registry.json](registry.json) deliberately
starts empty; its namespace is a publication algorithm constant, not a construct
identity. No additional RAD domain, semantic element, relationship or View is
published. The owner confirms Release 1.0.1 was committed and visually verified,
including Documentation on the 14 EC elements. Those results are retained.

## Identity roles

| Role | Meaning and governing treatment |
| --- | --- |
| Canonical Architecture ID | Persistent governed handle for the construct's lifetime. Independent of classification, tier, owner/path, document and release. Exact pre-existing governed IDs may be retained through reviewed adoption. |
| Domain03 structural Canonical ID | Existing typed responsibility-context identifier under [metamodel §8](../../03-business-architecture/metamodel/business-architecture-metamodel.md#8-architectural-element-identity-and-canonical-identification). It changes on approved relocation and retains historical IDs as aliases. It is not the new lifetime handle. |
| Source/Capture ID | Existing capture record `key`, local Feature token such as `FEAT-EM-01`, source assertion key or JSON pointer. Namespace/scope is explicit. No automatic promotion to canonical identity. |
| Navigation/Register ID | C/F/D/M/CDG and document/review keys where sources define navigation semantics. F46 is a collision in one such register. A key may describe several constructs or none. |
| ArchiMate Semantic ID | Open Exchange identifier bound to canonical identity by a reviewed lock or deterministic derivation. It exists once across the common model and all Views. It does not itself establish a RAD crosswalk. |
| ArchiMate Visual ID | Separate deterministic identity of a View, node or connection appearance. View-local endpoints refer to visual nodes; elementRef/relationshipRef refer to semantic IDs. |
| Implementation ID | Code class/package/module, endpoint, API operation, database/runtime/resource or FHIR identifier. It identifies its implementation context and never automatically establishes architectural identity. |

Names are governed attributes, not identity allocation inputs. **CM-R05 Global
Capability Name Uniqueness remains mandatory.** Its existing BC/BEC Health
Information Exchange conflict remains explicit; UUIDs or namespace qualification
do not fix or waive it. Name similarity, even exact equality, confers no mapping
authority. A rename retains identity and may retain the previous name for search;
name aliases are not identifier aliases.

## Syntax decision and compatibility

**Recommendation for review: opaque persistent IDs for future allocations, with
exact retention of genuinely governed existing IDs.** New IDs use the lowercase
UUIDv4 URN form `urn:uuid:<uuid>`. Allocate a random value once under an explicit
decision and retain it; never regenerate it on publication. No domain/type/tier,
name, path, release or implementation string is encoded. Such properties can
change while the construct persists; encoding them would force misleading IDs
or identity churn. The register's `construct_type` is an identity classification,
not an allocation of an ArchiMate type or a new domain metamodel.

Existing EC-01–EC-14, AX-01–AX-18, REQ-FND, CST-EXT, COA, SR and VS definition IDs
already identify governed definitions. Preserve their spelling and meaning. They
are retained native identities, not renamed to UUIDs. Exact adoption into the
common register is a later reviewed migration. Candidate requirement IDs in a
navigation table, ordinals such as BC-01, local FEAT tokens, C keys, HCM/LaTeX
publication IDs and XML UUIDs are not assumed to be such retained identities.

The Business metamodel's typed grammar is **preserved**, including CT1 → CT2 →
CT3 → Feature, ancestry validation, structural non-reuse and relocation aliases.
Its existing `canonical_id` field continues to mean full structural Canonical ID;
do not place a UUID or capture key in it. The persistent field is explicitly named
`canonical_architecture_id` in the register. These are two roles of one construct,
not competing architecture models. Adding a lifetime handle does not establish
missing tier, ancestry, root standing or structural code allocation. It does not
clear any of the 2,467 unresolved RAD dimensions. A future approved crosswalk must
bind the exact source record and both identifiers where available.

## Allocation, lifecycle and migration

The architecture owner, or an explicitly delegated architectural authority, approves
allocation/adoption and lifecycle decisions in RADS. Maintainers/tools may prepare
evidence and validate it; a commit author, generated fingerprint or successful test
does not independently approve an architectural identity decision.

1. Establish the bounded construct, source authority, name, responsibility and
   architectural standing. Decide whether it is architecture, a type definition,
   a documentary bundle or an unresolved proposal. Resolve identity ambiguity
   before allocation; do not allocate just to improve coverage. Unestablished
   ancestry remains a separate finding, not a source of manufactured context.
2. Search existing canonical IDs, historical IDs, source mappings, native
   definitions and semantic locks. Produce collision evidence. An exact unique
   governed ID permits a proposed adoption; a name does not. Approve the migration
   or allocation explicitly before adding a registry record.
3. Retain the native ID on adoption; otherwise allocate one opaque ID. Record the
   decision, source document/section/fingerprint, acceptance release, type, name,
   standing and qualified source identifiers. Sources may be candidate only when
   their candidate standing is explicitly accepted and retained; allocation does
   not promote an architecture proposal into final architecture.
4. Enforce global uniqueness across canonical handles and historical identifier
   aliases. No reuse after retirement. Scoped source identifiers are not global
   aliases. A C/F bundle can contribute evidence to several constructs only with
   explicitly adjudicated facets; a collision cannot be hidden by inventing facets.
5. Renaming or reclassification retains the handle. Responsibility relocation
   retains it while the existing structural path and direct historical aliases
   change under Domain03 rules. Retain capture keys and previous provenance.
6. Deprecation marks discouraged future use; retirement retains a tombstone,
   provenance, aliases and bindings. Retired identities cannot be reactivated or
   reused for another construct. Corrections to erroneous retirement require an
   explicit governance/model revision; they are not routine tool operations.
7. Semantic replacement creates a separately authorised identity. Retire or
   deprecate the predecessor and record successors; do not alias its ID to a
   different construct. Merge/split decisions specify whether one original
   construct genuinely survives or all successors are new. Preserve every
   predecessor as a historical object, state exact continuity and consequences,
   and approve relationship reassessment. Never reuse a predecessor ID merely
   to preserve diagram geometry. Successor links are lineage, not alias chains.
8. An identifier alias resolves directly to the same current record. No alias
   chains, redirects through another canonical record, similar-name aliases or
   automatic import of local/navigation keys. Historical structural aliases stay
   in their governed namespace and retain their grammar/non-reuse rules.
9. Migrate in small reviewed batches. Dry-run the exact crosswalk and history
   validation, then update sources/capture/publication bindings as explicitly
   authorised. Capture field meanings and existing names/IDs must not silently
   change. Preserve provenance and prove continuity against the prior accepted
   register and existing semantic lock. This release performs none of these moves.

## Coverage across architectural constructs

| Construct | Identity boundary and special gate |
| --- | --- |
| Capability | Identity independent of BC/BEC/EC classification and CT tier. Preserve existing EC IDs and the 228 locked Exchange objects. 214 RAD crosswalks remain unresolved. |
| Feature | Distinct construct with Capability semantics and FT classification. Preserve local FEAT tokens and full CT3 framing requirements; do not infer full structural ID. |
| Function, Service, Process/Behaviour | Identity belongs to the bounded behaviour/exposure/progression. Ownership/path changes do not allocate another construct. Do not force one-to-one Function/Service/Process mappings. |
| Actor/Role, Interaction/Collaboration | Identity only for an established construct. Actor categories, qualified references and examples are not inferred participant instances. Role eligibility or participation is independently governed. |
| Information/Data | Distinguish conceptual meaning, governed model, representation, instance and persistence responsibility. FHIR resource IDs/classes and model versions do not identify architecture constructs. Mixed headings need adjudication. |
| Application | Preserve component responsibilities and candidate boundaries. C keys are navigation; library/runtime facets and mixed bundles need exact source adjudication. No module=component inference. |
| Technology/Platform | Identity for governed services/nodes/environments/artifacts, independent of mutable deployment/resource names. No accepted topology catalogue is established by directory or legacy runtime files. |
| Motivation/Strategy | Preserve governed axiom, requirement, constraint, EC, COA, resource and stream IDs. A numbered concern/heading is not identity; decision documents are not automatically Requirements. |
| Implementation/Migration | Allocate only for an actual governed WorkPackage, Deliverable, Plateau, Gap or transition. Patch releases, CDG navigation entries and plans do not automatically become those constructs. |
| Governed traceability | An independently governed assertion can receive its own persistent handle after predicate, direction, standing and exact endpoints are established. A review record/provenance link needs no semantic relationship identity merely to be referenced. |

An assertion's identity is independent of the diagrams that show it. Its governed
predicate and endpoints are locked once published; a materially different
assertion requires a separately reviewed replacement identity. Repeated evidence
for one assertion is additional provenance, not another relationship. Unresolved
assertions remain findings, not edges.

## ArchiMate identity contract

Existing semantic IDs take precedence over derivation. Preserve the immutable
baseline lock and the explicit 14 EC UUID/Reference-ID matches. Do not recompute
those UUIDs from EC strings. The other 214 capabilities and all 189 relationships
retain their locked IDs while their RAD crosswalks remain unresolved. No canonical
identity is allocated from a similar label, historical HCM ID or implementation ID.

For a future approved, previously unpublished construct, derive its semantic ID:

```text
namespace = UUIDv5(NAMESPACE_URL, "urn:harmonia:architecture:semantic:v1")
key       = compact UTF-8 JSON ["HarmoniaArchitecture", canonical_architecture_id]
id        = "id-" + UUIDv5(namespace, key).hex
```

The namespace is pinned in the register and validator. Do not include type, label,
source path, release or View. Register the result once; check all semantic/visual
identifier collisions against the common model before publication. A collision
stops the dependent publication, without salt/retry/reallocation. Model identity
remains the existing model ID. Future type projections requiring several semantic
facets need an explicit architectural identity decision, not a generator suffix.

Visual identity retains the existing `generate_views.py` UUIDv5 namespace and
model/View/kind/semantic-object keys, including visual endpoints for connections.
Nodes use elementRef and connections use relationshipRef with View-local node
IDs. A canonical registry record does not allocate a new appearance in every View.

## Register, provenance and validation

[registry.json](registry.json) is the single common-model binding register once
approved records are adopted. It complements authoritative RAD definitions and
the immutable Exchange lock; it does not replace either or reconcile them by
existence. [registry.schema.json](registry.schema.json) and the
[validator](../../../../scripts/architecture_identity.py) enforce structure and
cross-record/history rules. Sources retain definitions; the register retains
handles, direct aliases, scoped source IDs, decisions and semantic bindings.
The schema version is a data-contract version, not another architecture release.

Validate against the previously accepted register, not merely against itself:

```bash
python3 scripts/architecture_identity.py inventory
python3 scripts/architecture_identity.py inventory --check
python3 scripts/architecture_identity.py validate
```

The default history check reads the register from Git HEAD when present;
`--previous-registry` supplies an explicit prior fixture for review/testing. An
initial empty register permits no silent allocation: every future record requires
an allocation/adoption event and authoritative evidence. Fingerprints are drift
guards; they do not approve the assertion. Preserve original provenance snapshots
and explicitly add reviewed current evidence rather than rewriting history.
Previously accepted fingerprints remain historical provenance if a source moves;
new evidence must resolve to a current governed source and match its section/hash.
This distinction does not turn source drift into approval of changed semantics.

Validation rejects duplicate/reused handles and aliases, navigation/capture IDs
masquerading as canonical IDs, ambiguous qualified source bindings, missing
provenance, changed semantic bindings/namespaces/endpoints, invalid derivations,
unresolved endpoints, removed tombstones, rewritten events and missing decisions.
Source and semantic type legality, lifecycle meaning, actual merge/split continuity
and approval authenticity still require architectural review. An empty-register
PASS establishes the mechanism, not RAD identity completeness. Existing structural
grammar, CM-R05 and publication reference/XSD checks remain separate required gates.

## Review stop

Review syntax and coexistence with Domain03 structural identification, the
inventory, unresolved cases and lifecycle rules before authorising migration.
Start with the 14 exact EC adoptions if approved. No bulk allocation, naming
adjudication, F46 repair or additional-domain publication occurs in this release.
