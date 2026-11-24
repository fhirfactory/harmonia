<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Archi round-trip service — assessment and least-step implementation plan

Date: 2026-10-18. Branch inspected: `Harmonia-GovernedAccess`.
Checkout: `d060702d6055bc42ef4e0b64917703636f466faa`.
Assessment baseline: accepted, committed Architecture Release **1.0.6**, inspected
by Mark in **Archi 5.10.0**. Final plan reconciliation after identity/naming
adjudication on 2026-10-18.
Status: **assessment and proposed plan; no implementation authority implied**.

## 1. Final recommendation and incorporated adjudications

Retain the comparison boundary: **the exact published Open Exchange that entered
Archi versus its incoming full-model Open Exchange export**. `canonicalId` establishes
architectural instance continuity. Existing Exchange IDs remain representation
bindings; View and visual IDs retain their governed presentation roles. Names,
ancestry, types, folders and layout never become identity matching keys.

Release **1.0.6 is accepted and checked in**. Mark inspected it in **Archi 5.10.0**
and accepted it as the current synchronised baseline. Acceptance does not establish
the fidelity of an unchanged export; no such fixture was found.

The final adjudications supersede the previous review's prefix/naming proposals:

| Decision | Consequence |
| --- | --- |
| The type-prefix requirement is withdrawn | Canonical ID remains type-independent. Native/Harmonia type changes retain identity and are reported independently. There is no outstanding prefix decision. |
| Canonical ID is an immutable, globally unique, non-reusable, semantically opaque UUID persisted as exactly 32 lowercase hex characters | Validate `[0-9a-f]{32}` with a full-string match, plus uniqueness, accepted bindings and history. No URN, separators, encoded type/name/path/lifecycle or hexadecimal encoding of a UUID's textual string. |
| Three governed names: `displayName`, `friendlyName`, `canonicalName` | Preserve and compare them independently: human presentation, readable qualified context, and formally governed qualified path. All may change without changing identity. |
| Historical field names do not constrain the new semantics | New terminology supersedes historical naming. Use an explicit versioned mapping, retaining old values/evidence; do not impose a new meaning by matching field spellings. |
| EC-01–EC-14 remain identifier aliases/provenance, with new UUID32 IDs for their elements | Do not convert the codes into UUIDs, grandfather them as active canonical IDs, or rewrite frozen binding evidence. Allocate new UUIDs only in separately authorised future work. |
| Naming provenance is retained, distinct from identifier aliases | Reuse existing alias/source/history machinery where adequate; resolve its missing name-form qualification before extending it. |
| Native `archimateType` and optional specialised `harmoniaType` are separate from identity | Reuse Feature → native Capability plus `feature` metadata; do not invent subtypes or use type as an allocation/matching input. |
| Governed View presentation is persistent round-trip state | Preserve compatible approved View/occurrence/layout/routing state during republication; report conflicts and retain the curated original. |
| Normalisation means proven representational equivalence | Equivalent Property-definition rekeying can be normalised. Known exporter-caused semantic/presentation loss still remains a finding. |

The baseline contains **14 legacy EC identifier Properties and 1,285 elements
without that historical Property**. The EC codes are now adjudicated as preserved
identifier aliases/provenance, not new canonicalId values. **All 1,299 elements
require new UUID32 canonicalId metadata**, including those 14; zero currently
conforms. This is a transition/readiness finding, not a retrospective rejection of
accepted 1.0.6. Preserve its original adoption proof.

Keep the three increments, with clarified scope: **(1) read-only identity/three-name
conformance diagnostics plus actual export-fidelity proof; (2) integrated Canonical-ID
comparison and Change Reconciliation Report; (3) approved presentation replay on
republication**. The new decisions change the contract checked in increment 1,
not this dependency order. Identity allocation and naming population
are separately reviewed prerequisites to full-model conformance, not hidden work in
intake. Directory migration remains independent.

The identity contract and EC treatment are now decided. Some naming derivation/alias
semantics remain architectural decisions before population. They do not
prevent a diagnostic increment 1 from exposing those gaps without resolving them.
Section 15 gives the final issue classification and authorisation boundary.

## 2. Authority and scope

The [release policy](../../docs/markdown/governance/architecture-release-policy.md),
[mapping profile](../../docs/markdown/governance/rad-archimate-model-mapping.md),
[navigation convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md),
[identity model](../../docs/markdown/governance/architecture-identity/model.md) and
[baseline authority](../../docs/markdown/governance/baseline/README.md) establish:

- Markdown RAD authors and governs architecture.
- ArchiMate visualises, navigates, supports review and proposes changes. Accepted
  presentation state may become subordinate governed projection metadata.
- An Archi export is change input; its existence, IDs, Properties and successful
  validation confer no approval.
- LaTeX is strictly downstream publication, outside this round-trip service.
- Semantic hierarchy, model-tree navigation and View navigation are independent.
  Folder nesting and visual containment cannot manufacture a semantic edge.

Material consistency with the [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md):

| Authority | Consequence for this plan |
| --- | --- |
| AX-04 | Reuse working machinery; Archi and generators do not determine architectural meaning. No new runtime subsystem, protocol or deployment boundary is proposed. |
| AX-14 | Preserve semantic/visual identity, assertion standing/currentness, semantic hierarchy/navigation and observed change/accepted change as distinct concepts. |
| AX-17 | Do not match architecture by names, complete missing crosswalks, promote candidates, infer deletion intent or resolve contradictory assertions. Record evidence and adjudication needs. |

Mark's post-review acceptance statement establishes the current status: Release
1.0.6 was inspected in Archi 5.10.0, accepted and committed. Pending-inspection or
uncommitted-candidate wording in the earlier generated README, guide, reports and
GTK logs describes their preparation stage. Those historical artifacts are not
rewritten. Their earlier GUI failure does not contradict the later human
inspection, and neither proves unchanged export fidelity.

The task adjudications govern the new semantic contract; existing field spellings
do not constrain it. In historical records, `canonical_id` qualified paths correspond
to the new `canonicalName`; old `canonical_name` short labels are legacy naming
data. Existing `canonical_architecture_id=EC-xx` records document the prior adoption;
those codes will be retained as identifier aliases/provenance, while the elements
receive new UUID32 canonicalIds. This is an adjudicated semantic/data-contract
transition, not a mechanical conversion of EC text into UUIDs, architectural
replacement, or authority to rewrite previous identity history.

Future authorised governance/data-contract work records these semantics, UUID
persistence, three-name source/derivation and aliases. Mapping-profile §7's
**“Views are disposable deterministic projections”** also needs reconciliation with
required preservation of approved presentation. Neither that older wording nor old
field naming reverses the new decisions. RADS and historical evidence are not edited
here; explicit versioned active mappings and pinned old proofs keep both intelligible.

Only this working assessment report is revised. The explicit task prohibition on
incrementing the release is followed: `architecture-release.json` remains 1.0.6.
Future accepted implementation change sets follow the patch policy; comparison
execution itself must never increment a release. This is publication-framework
planning, not a convergence/runtime milestone or additional semantic publication.

## 3. What Release 1.0.6 actually contains

The active model is
[synchronised-baseline/Harmonia-Architecture-Baseline.xml](../../docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml),
not the top-level historical `Harmonia-Capability-Architecture.xml`.

| Measurement | Inspected 1.0.6 publication |
| --- | ---: |
| Semantic elements | 1,299 |
| Relationships | 852 |
| Views | 1,196 |
| Visual objects, including annotation nodes | 3,915 |
| Element-referencing visual objects | 3,876 |
| Annotation/Label nodes | 39 |
| Visual connections | 1,449 |
| Property definitions | 55 |
| XML bytes | 19,806,014 (19.806014 decimal MB) |

Model ID: `id-80b065eaa4c550eba09c0dbd41b1d631`.
Published SHA-256:
`d57a5adeeef6b0906b30a02c3ea80cc6ff6308988775e7e3a619db01564ca52a`.

The semantic population is not 1,299 independently registered Architecture
Identities. It comprises the 946-record reconciled capture, 214 unbound historical
1.0.4 objects and 139 Motivation elements, including five reused Principles.
The common identity register contains **14** adopted EC bindings. Capture standing
remains 716 Canonical / 227 Proposed / 3 Candidate; currentness independently
remains 845 current / 69 pending / 32 superseded. These qualifications must accompany
change evidence rather than disappear into totals.

The 852 native relationships include 189 retained original edges, 634 recovered
source assertions and 29 Motivation assertions. A further **462 source assertions
are deliberately metadata/findings**, not native relationships. The report must
notice edits to that metadata without interpreting it as an authorised graph edit.

### Active publication and verification

`tools/synchronise_baseline.py` reads governed `baseline/architecture.json`,
`motivation.json`, `correspondence.json` and Documentation classification. It copies
the pinned recovery model/crosswalk and overlays the explicitly governed current
projection. It produces 12 core outputs, including Markdown baseline catalogues;
`report_baseline.py` adds the report and inspection guide, giving **14** generated
files. `verify_baseline_outputs.py` checks this exact set in independent processes.

The Makefile's active `all` target runs baseline generation, publication tests,
baseline checking, report checking and independent reproduction. `make all` writes
generated Markdown and XML and is **not an ingestion operation**. This assessment
used read-only checks and temporary reproduction instead.

Generation validates source hashes, source correspondence, identity preservation,
one canonical home, source-based relationship homes, View scopes, visual endpoints,
Feature associations, layouts, standing and offline schemas. These strict gates
remain appropriate for publication. Applying the entire baseline validator to an
incoming edited export would reject intended additions and changes before they
could be reported; ingestion needs structural checks separated from these
publication-preservation expectations.

The lower layers remain useful:

- `generate_views.py`: secure XML parser, namespace constants, local schema
  resolver, global identifier check, signatures/digests, serialization and existing
  UUIDv5 publication namespace.
- `publish_documentation.py`: exact 14-EC source binding, verbatim Documentation
  overlay and restoration of the original lock for preservation checks.
- `publish_navigation.py`: architecture-only preservation snapshot and
  `navigation_homes`, separate from Views and organisations.
- `feature_projection.py`: exact assertion-based navigation/layout, contextual
  landscape roles and distinct visual appearances for shared semantic behaviour.
- `report_baseline.py`: deterministic reporting and verified-evidence checks.
- `scripts/architecture_identity.py` and `architecture_release.py`: existing
  binding/history validation and read-only release loading.

## 4. Identity, naming and semantic-type coverage

### Architectural identity and representation identity

Use the existing `canonical_architecture_id` role for the persistent `canonicalId`,
as a lifetime-identity responsibility in the future active contract, without
requiring the historical field name to persist. Capture `canonical_id`
is a mutable structural responsibility-context path, corresponding conceptually to
the new `canonicalName`, not the UUID. Open Exchange identifiers, source keys,
HCM references and native definition codes retain their own roles; no matching or
allocation is inferred from their spelling.

The matrix separates current bindings from interchange preservation evidence.
Installed-plugin observations are static evidence, not an executed round-trip test.

| Object | Existing identity/correspondence | Publication → Archi → export assessment |
| --- | --- | --- |
| Model | Retained Exchange model ID, publication hash, release evidence | Importer sets model ID; exporter writes it. Verify on the full-model unchanged fixture. Matching model ID alone does not identify a particular publication. |
| Original semantic elements/relationships | 228 element IDs and 189 Composition IDs in immutable original semantic lock; 14 exact EC bindings | Retain and compare these representation IDs verbatim as forensic evidence. The 14 EC objects have protected legacy code bindings, retained as identifier-alias/provenance evidence rather than active UUID identity. None yet has its new canonicalId. The 214 unbound objects lack that primary architectural identity and cannot be reported as fully identity-reconciled. |
| Reconciled capture elements | Recovery crosswalk binds 946 exact capture keys/reference IDs to XML IDs; current Markdown exposes those bindings | Most are persistent representation IDs, not adopted Canonical Architecture IDs. The recovery uses `visual_id(model, "latex-rad-recovery-v1", "element", reference_id)` except retained EC bindings. Retain actual IDs; do not replace them with the identity model's future-allocation algorithm. |
| Motivation elements | Explicit source keys/native IDs, published IDs and source Properties; five reused Principle IDs | New selections use the synchronised-baseline publication role/key; existing IDs take precedence. Use published IDs and exact source correspondence, not element labels or generator re-discovery by name. |
| Native relationships | Locked IDs; recovered predicate + endpoint-reference tuple; Motivation assertion keys | Importer/exporter preserve relationship IDs and semantic endpoint references. Retain relationship IDs and compare type, Canonical-ID endpoint bindings where available, attributes, Documentation and Properties. Same-ID endpoint/meaning change requires review, never permission to redefine the assertion. |
| Source relationship locators | Crosswalk `source_index`, snapshot pointer and exact assertion; Motivation relationship keys | A source index/pointer is a locator, not lifetime identity. Do not regenerate incoming IDs from indexes or endpoint tuples. Distinct parallel assertions lack automatic identity allocation; existing recovery deliberately rejects collisions. |
| Views | Existing retained IDs; deterministic context/role/projection keys for additions | Importer sets diagram ID; exporter writes it. Names, folders, coordinates and page contents are attributes, not matching keys. Publication role and page keys are not universal architectural identities. |
| Visual objects | Node IDs separate from `elementRef`; Label nodes also have IDs | Importer sets object ID; exporter writes it. Match by actual visual ID and owning View. Shared behaviour has multiple valid appearances; `elementRef` alone cannot identify an appearance. |
| Visual connections | Connection IDs, `relationshipRef`, visual source/target IDs | Importer sets connection ID; exporter writes it. Compare occurrence/endpoints separately from the one underlying semantic relationship. Losing a visual connection does not establish relationship deletion. |
| Documentation | Owner identity plus language/occurrence; no independent Documentation ID | Compare on the matched owner. Importer chooses a language variant and reads a single string; exporter emits its chosen language. Model/element/relationship/View Documentation uses non-normalizing text extraction in inspected code, but multilingual preservation is not demonstrated. |
| Property definitions | 55 unique published definition IDs, exact distinct names and string types | IDs do **not** survive as stable definition identity: import reduces definitions to ID → property-key text; export creates `propid-...` IDs from unique property keys. No independent property-instance ID exists. |
| Property values | Owner ID + definition reference + value language/multiplicity | Inspect exact referenced definition on each side. Match an unchanged exact unique property key as an interchange field key, retaining both XML definition IDs. This is not name-based architectural identity. Changed/duplicate keys or languages/multiplicity can be ambiguous. Import uses normalized text for property values. |
| Navigation folders/reference items | Deterministic publisher IDs; semantic-context/projection keys; referenced target IDs | Exporter omits organisation `item` IDs; importer builds folders from labels/context, not those IDs. Target `identifierRef` survives. Object-home paths can be compared; folder rename versus replacement, or move versus remove/add, cannot always be resolved. |
| Projection metadata | View/node/connection state in XML; in-memory scope/layout/association indexes; published crosswalk and preservation proofs | No general accepted-Archi-layout overlay reader/store exists. IDs identify observed state; they do not make an edited folder or layout authoritative. Existing generators would regenerate their own presentation. |

### UUID contract and minimum active-model extension

The persisted form is a **full-string lowercase `[0-9a-f]{32}` match**, e.g.
`550e8400e29b41d4a716446655440000`. It is the UUID's `.hex` representation, not
`id-…`, a UUID URN, dashed text, or the hex encoding of the characters in that text.
Lowercase is already the identity model's convention and is used by existing
publication `.hex` generation. Intake must report uppercase, separators, padding,
wrong length or non-hex values as conformance findings rather than silently
repairing/rekeying identity. Show possible lexical equivalence only as evidence.

The repository already recommends **random UUIDv4 allocation once**, enforced for
`origin=allocated` by `architecture_identity.validate_registry` using
`UUID.version == 4`. Python's version check applies to the RFC 4122 variant.
Keep that existing allocation policy and persist `.hex`; no new version, variant,
UUIDv7/timestamp or hash-from-name policy is needed. The target lexical contract and
allocation policy are separate checks. There are no allocated UUID records to
reserialise: all 14 historical records have `origin=retained`, and their EC codes
remain legacy identifier data, not UUID input. Do not retroactively treat publication
UUIDv5s as allocated architectural UUIDs. Any different allocation policy would need
its own decision, but is unnecessary for increment 1, which allocates nothing.

Keep existing publication/visual namespaces and identifiers. Canonical UUIDs are
Property strings, not XML `xs:ID` attributes; a leading digit is valid. Validate
canonical uniqueness across governed current and retired instances/history, and
representation-ID uniqueness separately across the XML model, Views and occurrences.
Do not strip `id-` to manufacture canonical identity or conflate these namespaces.

Extend the existing active publisher and shared validation, not historical recovery.
Reuse the Canonical Architecture ID Property carrier/`Baseline.property` pattern,
with an explicit current-qualified key where needed beside immutable `v0.2` fields.
The eventual active conformance gate checks **every `<elements>/<element>` in the
current publication**, including proposed/superseded/historical references: exactly
one valid UUID claim, unique accepted instance/binding/history, all three governed
names with source standing, and compatible native/Harmonia types. Incomplete
ancestry remains a naming/authority gap; it is never encoded in the UUID or filled
by inventing context. Increment 1 reports this target's violations without replacing
existing publication gates or requiring a migrated model.

`validate_registry` presently requires allocated UUIDv4 **URNs**, retained native
codes and the demonstrated EC locked crosswalk; its schema has only one `name`
field. `verify_bindings` invokes the exact 14-record/one-event historical adoption
proof, freezes register bytes and checks original semantic content. Its reserved-ID
scan uses the historical top-level model. These are useful mechanisms, not the
new active-model gate. Add a bounded read-only check against the synchronised XML
and accepted registry/history; keep historical validation against explicit pinned
inputs. Before any future population, version the active contract and extend
reviewed bindings/collision coverage and naming fields. Do not relax the old proof,
overwrite historical records as though they already contained the new roles, or
rederive existing Exchange IDs from the new UUID handles.

No relationship, View, node or connection currently carries the lifetime Canonical
ID Property. Retain their role-specific identifiers for comparison/preservation.
Additional canonical treatment is a later architectural question, not inferred
from the element UUID decision; see section 15.

### Canonical ID audit — complete coverage and gaps

A fresh parse of the accepted XML, Property definitions and identity register found:

| Population | Elements | Historical EC-ID Property present | Historical Property absent |
| --- | ---: | ---: | ---: |
| Reconciled capture | 946 | 14 | 932 |
| Unbound historical reference elements | 214 | 0 | 214 |
| Motivation, including five reused Principles | 139 | 0 | 139 |
| **Total** | **1,299** | **14** | **1,285** |

All 14 existing values are single-valued and distinct and match the common
register's exact locked Exchange bindings. There are no duplicate legacy-code
claims. None of these codes is a UUID32; under the final adjudication they remain
legacy identifiers/aliases/provenance, not malformed active UUID allocations. The
unchanged legacy binding list is:

| Historical adopted EC identifier | Exchange element ID |
| --- | --- |
| EC-01 | `id-d53727d908365757a293b0896b2c769b` |
| EC-02 | `id-06e038737c5355edbc0591e08c594ab0` |
| EC-03 | `id-5891936941f65d20afad86b0697879c7` |
| EC-04 | `id-9e79bc474aed516c8c6752ab81b63d6b` |
| EC-05 | `id-e11f02764fe55ff29c0782db21198040` |
| EC-06 | `id-a6f6d30d8fce5ac78f60f7aefa71b28e` |
| EC-07 | `id-d71457435a085e749ffb1e1a3b551228` |
| EC-08 | `id-dbd66e69788450649e184966aa724797` |
| EC-09 | `id-770ebb3e3bdc5f2bbe77b2bfd55bd74d` |
| EC-10 | `id-49d4f69ebeeb5ea782a52172fb3e8a7b` |
| EC-11 | `id-82cd1f6a3e2352639e22c542f5cb3a9d` |
| EC-12 | `id-9bbd142f818e50d39a3d0429f74b72b4` |
| EC-13 | `id-7d6f635ffb6f5c6181bde1d07b859662` |
| EC-14 | `id-4dbf578f52bd5b3b9301b9807ce60f30` |

All are native `Capability`. Their future treatment is **decided**: preserve EC
codes as identifier aliases/provenance and frozen binding evidence; allocate a new
UUID32 canonicalId for each of these elements just as for all other elements.
There is no active grandfathering option and no EC-text-to-UUID conversion.
The type-prefix proposal is withdrawn. No allocation or migration occurs here.

The implementation consequence is a versioned **active** register/alias/history
transition in the existing machinery, carrying exact old code → same instance → new
UUID → retained Exchange-ID evidence. Current validation rejects rekey/removal and
native-code alias claims, so it cannot be applied unchanged to that future active
contract. Preserve its historical rules and inputs rather than weakening them;
new active validation implements the adjudicated alias role and continuity.
No architectural retirement/replacement is implied. Preserve native EC definitions,
all existing Exchange/View/visual IDs and every prior provenance/event record.
Historical spelling is retained in historical evidence, not imposed on the new
canonicalId semantic role. The remaining allocation task covers **all 1,299**
represented elements, not only the previously reported 1,285 gaps.

The historical Property-absence set is exhaustive: **every semantic element except
the 14 listed above** in the pinned XML. Grouped by native type, it is 827 Capability,
160 BusinessFunction, 139 BusinessService, 20 BusinessProcess, 25 Principle,
74 Requirement, 10 Driver, 10 Stakeholder, 9 Goal, 5 Assessment, 3 Outcome and
3 Constraint. It is not a claim that all missing objects already have sufficient
authority for allocation.

For an exact inventory of those 1,285 historical-Property gaps without transcribing
rows, this XPath selects their existing `identifier`. The complete new UUID32 gap
is all 1,299 elements, including the 14 legacy-code owners; names are not selectors:

```text
namespace a = http://www.opengroup.org/xsd/archimate/3.0/
/a:model/a:elements/a:element[
  not(a:properties/a:property[
    @propertyDefinitionRef='recovery-canonical-architecture-id']/a:value)
]/@identifier
```

The 932 capture gaps resolve through existing crosswalk keys/Reference IDs; the
214 unbound objects have no proven RAD crosswalk. Motivation native AX/REQ/etc
codes are valuable adoption evidence, not automatically adopted lifetime IDs.
All 946 capture `canonical_id` qualified paths and projected **Structural Canonical
ID** Properties correspond semantically to `canonicalName`; none supplies a UUID32
canonicalId. Historical field names impose no new-contract constraint. Historical
identity-status Properties correctly expose unresolved identity and must not be
rewritten merely because the new rule exists.

Two snapshots establish present uniqueness, not lifetime non-reuse or actual
continuity of responsibility. Reuse existing registry events, append-only history,
tombstones and source/binding validation. Reuse of a retired ID, two owners of one
ID, or contradictory established bindings is an identity violation. Whether an
otherwise same-ID semantic rewrite represents another architectural instance can
require architectural judgement; a large text/type edit alone does not prove reuse.

### Existing short-name fields and aliases — not the new qualified names

Every XML element has a nonempty native Name. The old `canonical_name` field and
`v0.2 / Canonical Name` Property are **short governed/candidate labels**, not the
new formally qualified `canonicalName`. The earlier audit remains useful with
that explicit distinction:

- 719/946 capture elements have `canonical_name` projected as
  `v0.2 / Canonical Name`; 227 lack it. Of those 719, name standing is 696 Canonical,
  20 Proposed and 3 Candidate. Currentness and architectural standing are separate.
- 162 capture elements have `reviewed_name`; 44 carry existing `name_aliases`
  projected as `v0.2 / Name Aliases`. 430 carry historical **ID** aliases instead.
  These channels must remain distinct. Registry `aliases` means identifier aliases,
  not name aliases; its schema currently has no dedicated name-alias field.
- 34 display Names differ from the legacy Canonical Name Property: 14 have the explicitly
  retained EC display prefix and 20 retain reviewed/candidate naming distinctions.
  Twenty alias lists include the canonical-name value. Do not silently choose a
  winning name, remove aliases or label a pre-existing mismatch as an Archi rename.
- There are 580 elements without that historical short-name Property: 227 capture
  records, 214 unbound objects and 139 Motivation objects. This is a legacy-field
  census, not a count of missing new canonicalNames or evidence of missing RAD prose.
  New-role coverage must be checked independently.

The 719 legacy short-name Property values include three duplicate-name pairs:
`Demographic Change Distribution` (`id-35f2e62266245c35ae0957b13f0899d7`,
`id-0672fb8a0d7a5f029d80931b0d3d2c60`), `Diagnostic Report Distribution`
(`id-6ffaf46cf2715eb28cd85d28c24c7d0a`, `id-d5b1d7f0358b5ce095b5a56df9c07714`)
and `Health Information Exchange` (`id-2134969d36665649a7d61fcea20bc15f`,
`id-0712aaf441dc5100a5ac79c4b7848477`). These are naming/standing findings,
not evidence the instances are identical. CM-R05's existing Capability naming
conflict is not cured by unique IDs or a qualified display label.

### Three-name support, derivation and provenance gaps

| Target concept | Existing source/representation evidence | Minimum treatment |
| --- | --- | --- |
| `displayName` | All 1,299 native XML Names; source `name`, `reviewed_name`, old short `canonical_name`; classified folder labels | Use native element `<name>` as its presentation carrier in the active projection. Establish the approved source value/qualification without treating folder labels as element names. No rename is made here. |
| `friendlyName` | No explicit field or Property in the capture, registry, active XML or naming code inspected | Add an explicit active field/Property after a derivation/authority rule is approved. Existing ancestry traversal can supply exact context, but no friendly-name tokenisation grammar is established. |
| `canonicalName` | 946 structural `canonical_id` values and `v0.2 / Structural Canonical ID` Properties; Business metamodel §8 grammar | Reuse the existing qualified-path grammar/evidence for its supported kinds. Of 946 paths, 190 have Established and 756 Proposed structural/ancestry standing. Do not promote proposed paths or relabel short `canonical_name` values as qualified names. |

All 12 current native types already carry Properties; the XSD supports string
metadata. No schema/type limitation prevents UUID and qualified-name persistence.
Use native `<name>` for displayName and separate current Properties for the two
qualified names. Alternate-language Names, Documentation and folder labels are not
substitutes. Repeating displayName in a Property is optional; if repeated, check
agreement. The existing `name`/`reviewed_name`/old short `canonical_name` values are
source evidence for the unqualified readable portion, **not automatically the final
displayName** when type/tier/context qualification is governed. Preserve discrepancies
and source standing; do not promote a reviewed candidate over an accepted assertion.

#### Persist, derive, or persist-and-validate qualified names

**Recommend persisted-and-validated for both `friendlyName` and `canonicalName`.**
This extends the actual machinery: the source already persists qualified structural
paths, the publisher emits them as Properties, and validators check grammar,
local-token spelling and ancestry. Store the governed/current values in the active
source contract and publish them in Exchange; validate against deterministic naming
rules and exact source context wherever established. A generated value can be
materialised only from approved inputs/rules. Do not substitute a newly derived
value for an incoming edit before reporting the raw change.

| Treatment | Repository consequence |
| --- | --- |
| Derived only | Requires a naming algorithm that currently does not exist for friendlyName; cannot recover authoritative context from Archi's lossy folders. Omitting the values from Exchange would make the workbench/report dependent on external reconstruction and obscure explicit name proposals. |
| Persisted only | Existing source and Property infrastructure supports it, but values can contradict current ancestry/tokens; the existing structural validator already guards this for canonical paths. |
| Persisted and validated | Reuses current capture/property/validation patterns, provides portable names for Archi and report evidence, and exposes drift/proposals rather than silently recomputing them away. No parallel naming repository is needed. |

For canonicalName, the agreed semantic correspondence to old qualified `canonical_id`
is settled; its spelling does not force retention of the old field label. Reuse the
qualified-path data and deterministic grammar/ancestry checks, keeping Proposed or
unresolved context visible. For friendlyName, **no approved deterministic derivation
exists yet**. Persist only an explicitly governed value until such a rule is agreed;
report missing/underived/conflicting state rather than inventing a token transform.
Whether those initial values may be authored independently of a derivation, and the
actual token/source grammar for validation, need Mark and ChatGPT's confirmation.
The persisted-and-validated recommendation and missing rule are presented for that
review; this task implements neither. Exact Property/field spellings are routine
versioned implementation choices once the semantic source/derivation rule is agreed.

The exact Person Identity example shows why field roles and standing matter:

```text
current XML/source name:   Person Identity Administration
old canonical_name:        Person Identity
reviewed_name:             Person Identity Administration
name_aliases:              [Person Identity]
structural canonical_id:   CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.
                          CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY
structural/ancestry state: Proposed
```

The path above is one uninterrupted string in the source. It matches the shape
of Mark's `canonicalName` example, but its current source standing remains Proposed.
`[CT3]` is currently added to **folder labels**, while semantic Names are retained
unchanged by the navigation convention. The example clarifies naming concepts and
the legacy-path correspondence; it does not silently approve a live rename,
tier/ancestry change or whole-model naming migration.

There is no governed function that turns this path into
`HealthcareEntityInformationEnablement.ClientAdministration.PersonIdentity`.
Approve how readable tokens are sourced, punctuation/acronyms/spacing and separators
are handled, whether codes or current/old labels provide tokens, and collisions are
reported. Removing hyphens or applying title/Pascal case is not a governed derivation.
Use exact semantic responsibility ancestry, not whichever folder/View appearance
happens to contain the element. Existing `chain`, owner paths and one-home evidence
can be reused once the rule is decided. Missing ancestry remains unresolved.

The Business qualified grammar covers Capability/Feature/Function/Service/Process;
it does not define all Motivation naming paths. The 353 non-capture elements (214
unbound historical Capabilities and 139 Motivation objects) have no projected
structural path. Reuse applicable native-definition/source evidence and report
missing grammar/context rather than invent a universal path, synthetic parents or
semantic specialisation. Every represented element remains in conformance coverage.

Existing aliases are useful but **not sufficient to distinguish all three forms**:
`name_aliases` is a list of unqualified strings; `id_aliases` stores former structural
paths; registry `aliases` is a globally protected identifier-alias namespace.
Search/rendering and validators assume those roles. Under the new terms a former
qualified path is a historical `canonicalName`, but must not be silently moved from
its historically governed identifier-alias field/namespace. The present schema has
no name-form discriminator or individual alias decision/source metadata. Existing
source/review records and registry events can supply provenance after an extension.

Small options for Mark and ChatGPT are (a) extend existing `name_aliases` with
form-qualified records and provenance/event references while retaining legacy
unclassified strings, or (b) use three form-specific alias lists in the same governed
source model with existing record/event provenance. Both reuse the existing alias
mechanism; neither needs an independent alias repository. Decide the treatment of
legacy structural `id_aliases` and namespace/search compatibility explicitly.
Until then report aliases as legacy/unclassified or structural-ID history; do not
infer a form, construct alias chains or resolve identity from any name alias.

Historical qualified names may encode an earlier tier/type/ancestry. Existing
`rads_business_architecture.py` checks ID-alias terminal tokens against the current
type; that cannot blindly govern three-name history after an allowed reclassification.
Validate accepted historical names against their applicable historical context/rules,
not by forcing the current type/path onto them. The current source/registry schema
must be versioned or adapted explicitly; a field-name swap would break provenance
and historical checks. CM-R05 currently enforces global Capability short-name
uniqueness. Mark and ChatGPT must clarify its application to the three naming forms;
qualified paths alone do not close the existing BC/BEC name conflict.

### Native type and Harmonia semantic type

`recover_v02.TYPES` and the existing `v0.2 / Construct Type` Property already
express two type levels on all 946 capture elements:

| Harmonia source type | Native ArchiMate type | Instances |
| --- | --- | ---: |
| capability | Capability | 421 |
| enterprise_capability | Capability | 28 |
| feature | Capability | 178 |
| function | BusinessFunction | 160 |
| service | BusinessService | 139 |
| process | BusinessProcess | 20 |

All **178 Features** retain `feature` in metadata and native `Capability`; 157 are
current and 21 pending adjudication. This agrees with the Business metamodel and
mapping profile: Feature inherits Capability semantics and is distinct from a
Capability tier, Requirement or application feature. This does **not** authorise
native Specialization or Composition edges for Feature framing.

Use the existing source-qualified semantic-type value to validate compatibility
with native `xsi:type`, preserving spelling/provenance rather than inventing new
subtypes. Each of the three name edits, native-type change and Harmonia-type change
is a separate delta on the same architectural identity. Type evolution does not
itself allocate/rekey that identity or recompute its representation ID.
Unknown/incompatible pairs require review; changing native Capability to Requirement
while retaining Feature must be visible.

The remaining 353 elements have no `recovery-construct-type` field. Motivation
already has governed native types and source evidence; absence of a specialisation
need not imply a new subtype. Define how that native-only case is validated, without
inferring a Harmonia subtype. Existing mixed C/F/D/M rows and Information headings
already have unresolved construct/type mapping in the mapping catalogue and identity
inventory; they are architectural-review issues, not opportunities to invent a
specialisation. No further architecture layers are published by this plan.

### Concrete workbench evidence and limits

The prior repository log identifies the installed Archi distribution. Read-only
`javap -c -p` inspection examined:

`/usr/vm/devel/Tools/Archi/plugins/org.opengroup.archimate.xmlexchange_5.10.0.202607081558.jar`

SHA-256: `7442b0a8ee987b26ea53c98ce040890cb7c18945771a5f364f470f03de295e1a`.

Relevant methods in `org.opengroup.archimate.xmlexchange`:

- `XMLModelImporter.parseRootElement`, `parseArchiMateElements`,
  `parseArchiMateRelations`, `parseViews`, `addNodes`, `addConnections` assign the
  exchanged IDs to native objects. Export methods use `checkID` to emit them.
- `parsePropertyDefinitions` retains the definition ID/name lookup, not the
  definition object/type. `addProperties` creates native key/value properties.
  `getAllUniquePropertyKeysForModel` and `writeModelPropertiesDefinitions` create
  new definition IDs and emit string definitions.
- `getSubFolder`/`createSubFolder` construct folders using label/documentation;
  `writeFolder` emits labels, hierarchy and `identifierRef` children without item
  IDs. A path string is therefore observed navigation evidence, not recovered
  persistent folder identity.
- `getChildElementText` selects locale language or the first available variant;
  with its normalization flag it calls JDOM `getTextNormalize`. Property values,
  names and folder labels use this route. Documentation handling differs by owner.

Consequently, arbitrary whitespace collapse is unsafe, especially for embedded
snapshot/assertion JSON and other multiline provenance. A probe found 1,669
published property-value strings differ under generic whitespace collapse; this
is a sensitivity measurement, **not** an observed Archi export result. No such
content is changed or suppressed by this assessment.

The inspected repository evidence contains no unchanged 1.0.6 Archi-export fixture.
Full empirical preservation, export options, style defaults, node stacking,
language handling, nested/implicit connections and annotation fidelity remain
verification work. The service must not depend at runtime on an installed plugin,
bytecode inspection, historical transcriber, GUI or name-based reconstruction.

### Export fidelity under Canonical ID

| Observed exporter/importer behaviour | Revised significance |
| --- | --- |
| Property-definition IDs regenerated as `propid-*` | Does not change architectural identity if the exact governed key and Canonical ID value survive uniquely on the same element. Compare resolved definitions/values; record old/new definition IDs. Exact equivalent rekeying is representation behaviour. |
| Definition datatypes recreated as string | Current 55 definitions are string; verify this profile. A changed/lost non-string type on other input is a content loss, not automatically equivalent. |
| Folder/reference-item IDs omitted | Does not remove element Canonical IDs, View IDs or reference targets. It loses folder/item identity and can make rename/move continuity ambiguous. Report the loss and observed hierarchy/membership differences. |
| Property whitespace or language variants normalised/selected | May alter Canonical ID, names, Documentation, semantic metadata or provenance content. A missing/changed/duplicate Canonical ID is an identity finding. Other information loss remains visible even if caused by Archi. |
| Element/relationship/View serialization order changed | Equivalent for uniquely indexed unordered collections; not inherently equivalent for visual stacking, routes, language variants or ordered content. |
| Geometry/style/annotation/View transformation | Presentation/workbench delta or information loss unless exact equivalence is demonstrated. GUI acceptance alone does not prove it. |

Canonical ID materially simplifies element matching despite Property-definition
rekeying or Exchange-ID churn. It does not recover a dropped Property, prove identity
non-reuse, recover anonymous folder identity, or restore lost semantic/presentation
content. The unchanged-export proof must measure **values and state**, not just IDs.

### Gaps that constrain the first service

1. Zero active UUID32 values: the EC treatment is now adjudicated as legacy
   identifier aliases/provenance plus new UUID allocation. All 1,299 elements need
   UUID32 metadata; the 14 historical codes are not to be converted. These findings
   block full-model conformance, not a read-only census/transfer diagnostic.
2. The three naming concepts are decided, but `friendlyName` grammar, qualified
   naming coverage/authority and form-specific alias provenance are incomplete.
   The semantic mapping of qualified `canonical_id` to canonicalName is decided;
   short legacy `canonical_name` values still need source/qualification treatment.
3. No unchanged real Archi export fixture proves supported transfer fidelity.
   Because the accepted model lacks the new UUID/three-name fields, a small synthetic
   contract specimen must also prove those carriers through Archi; legacy-only
   export evidence cannot certify absent fields. This does not alter 1.0.6 or allocate
   real architectural identities.
4. No approved-presentation replay hook exists. Reporting can be delivered first,
   but accepted republication must preserve compatible curated state or surface
   conflicts. This remains required, not optional cleanup.

Issue classifications and exact increment-1 prerequisites are in section 15.

## 5. Comparison boundary and minimum mechanism

```text
Exact originating Published Open Exchange ─┐
                                         ├─ parse, validate, index and compare
Incoming full-model Archi Open Exchange ──┘                 │
                                                           v
                                      Change Reconciliation Report
                                                           │
                                      Human review/adjudication
                                               ┌───────────┴───────────┐
                                               v                       v
                                    Governed Markdown RAD    Governed projection state
                                      semantic changes        accepted presentation
                                               └───────────┬───────────┘
                                                           v
                                                Validated republication
```

### Provenance and admission

Pin the originating bytes/hash, model ID, release, source revision, registry and
correspondence evidence. Keep that artifact when releases advance. Do not choose a
base by filename, model Name, element names or current root release. Record incoming
hash, exporter version/options/locale and full-model scope evidence. The identity
register at publication time enriches the base; current accepted history adds
retirement/reuse checks and RAD drift diagnostics without replacing that base.

Reuse secure parsing, bundled XSDs, global representation-ID uniqueness and generic
reference/endpoint checks. Separate these from strict baseline-output expectations:
`synchronise_baseline.validate` deliberately rejects edits and is unsuitable as the
incoming acceptance gate. XSD PASS proves neither native semantic legality nor
architectural acceptance. Partial/invalid/unsupported input receives diagnostics
and incomplete/UNRESOLVED coverage, never a clean exhaustive delta.

### Identity-aware comparison

1. Resolve each document's Property references independently. Use the declared
   active-field contract for `canonicalId`, `displayName`, `friendlyName`,
   `canonicalName`, aliases and `harmoniaType`; resolve native `archimateType` from
   `xsi:type`. Retain version-qualified legacy fields independently. Duplicate,
   conflicting or changed keys/values are evidence, not a first-value-wins mapping.
2. Separate known legacy EC-code metadata from active canonicalId claims; the 14
   adjudicated legacy values are preserved aliases/provenance, not candidate UUIDs.
   Validate the exact 32-lowercase-hex UUID representation and build a unique
   Canonical ID → element index, plus separate Exchange ID → canonical/source maps.
   Missing, malformed, duplicate or multiple claims are conformance/identity findings.
   Do not trim/reformat active UUIDs, convert EC codes, derive UUIDs from paths or follow
   name aliases to complete identity. Current 1.0.6 has no format-conformant pairings;
   its old EC bindings remain preserved identifier-alias/provenance evidence.
3. For a valid same-ID pair, compare **each name independently**, both types,
   Documentation/languages, semantic Properties, ancestry/context and other content.
   Changes are candidates on the same instance. A qualified-name change can reflect
   a relocation; it does not prove deletion/new identity. Same canonical UUID with
   different Exchange ID is representation-binding drift, not two instances; retain
   both IDs and report any locked-binding conflict without silently rebinding it.
4. A valid UUID only in incoming means candidate new instance; only in the base means
   candidate deletion/retirement with adequate full-export scope evidence. Missing
   canonical metadata on a known representation ID is identity loss, not invented
   deletion/addition. Same Exchange ID with changed UUID is identity mutation requiring
   review. A new well-formed incoming UUID is a claim, not architectural approval or
   permission to register it.
5. Check accepted registry/history for global uniqueness, immutable ownership,
   tombstones/non-reuse and crosswalks. Two snapshots cannot prove lifetime non-reuse.
   Proven reuse is a violation; an otherwise same-ID responsibility rewrite with
   uncertain continuity requires adjudication. A name/type/path change alone does
   not prove reuse. For baseline self-comparison, unchanged legacy violations stay
   in conformance diagnostics even when the change set is empty.
6. Retain relationship/assertion IDs and compare type/predicate/direction/endpoints,
   Documentation, Properties and type-specific attributes. Resolve element endpoints
   through valid canonical bindings where possible, retaining raw Exchange references.
   Missing canonical endpoints limit architectural comparison. Parallel assertions
   stay distinct; do not invent relationship canonical IDs or match by labels/tuples.
7. Retain View IDs and visual occurrence/connection IDs with their owning View.
   Compare references through canonical bindings while preserving raw reference,
   parent/nesting, endpoint, layout/style and routing differences. Repeated element
   appearances are separate occurrences. Canonical element identity cannot repair
   lost/churned View or occurrence identity or infer a merge.
8. Compare owner Documentation/Properties by identity + field/language/multiplicity.
   Report navigation memberships, anonymous subtrees, empty contexts, item text,
   unsupported attributes and residual subtrees independently. Folder paths and
   visual nesting never establish architectural ancestry or qualified names.

Representation-ID comparison remains useful across **all 1,299 legacy objects** as
explicit diagnostic coverage, including the 14 protected EC bindings. It cannot
be described as compliant UUID reconciliation, silently infer renames for missing
IDs, or bless legacy codes as UUIDs merely to make a no-edit report clean.
A reviewed future identity-enabled cohort can be compared within the same coherent
model while incomplete whole-model readiness remains visible.

One bounded local command suffices. No server, database, queue, native `.archimate`
parser, historical transcriber, name matcher or three-way merge engine is required.

### Minimum presentation preservation

Retain immutable published and incoming Exchange inputs from the first report.
The least new accepted-state store is a **small approval manifest referencing a
hash-pinned Exchange snapshot and selected View IDs**, with baseline/registry
bindings and approved navigation/presentation scope. Reuse full `<view>` subtrees,
not a parallel coordinate/layout schema. The immutable snapshot can be the retained
export; it need not be duplicated per View. Selection is explicit human approval,
never an ingestion side effect. The snapshot is subordinate projection data, not
another semantic repository or automatic adoption of its element collection.

Before republication, resolve referenced elements/relationships against the reconciled
semantic model, retaining existing publication IDs. Reuse compatible accepted View
subtrees after generated defaults and before final validation, with required Property
references resolved through the known definition table. Include View structure,
nesting, annotations, style, positions/sizes, occurrences, endpoints and ordered
routing/bendpoints. Preserve approved model-tree/navigation state within the existing
one-home/convention rules; folder paths remain observed state rather than invented
folder identity. Do not accept a whole incoming semantic model just to retain Views.

For the first replay hook, preserve an approved View intact when its references,
semantic endpoint bindings, allowed types and governed scope remain compatible.
Name/Documentation edits normally do not invalidate coordinates; show overflow or
other actual conflicts when detected. Do not require fresh generated-layout equality
for a curated View. Existing exact layout validators remain for generated/default
and frozen outputs; add narrowly scoped validation for explicitly approved state,
retaining XSD, reference, endpoint, semantic-preservation and identity gates.

If deletion, relationship endpoint/type change, missing binding, changed scope or
unsupported content makes safe reuse unproven, **stop the affected publication with
a presentation-conflict finding**. Retain both snapshots and the curated View; show
View/occurrence IDs, dependency, old/new semantics and layout impact. Human review
can amend the curated View or authorise a separately identified replacement generated
View. Do not silently omit the old View, overwrite it, insert a new node, reroute a
connection or transfer geometry to a different instance. Automatic local repair,
replacement-View identity policy and complex layout merging are unnecessary for the
first hook and remain deferred. A retained historical snapshot need not be inserted
into an invalid current model merely to demonstrate preservation.

## 6. Normalisation: extend comparison, preserve publication locks

`generate_views.signature` already expands element/attribute namespace names,
sorts attributes, excludes whitespace-only element text and ignores indentation
tails. It preserves child order and nonempty text. `semantic_snapshot` additionally
hashes the protected model tree, while `architecture_snapshot` excludes Views and
organisations. These are useful preservation primitives, not a complete round-trip
normal form. A read-only reorder probe confirmed that moving an element within
the elements collection changes the protected-root signature while its per-ID
element hashes remain equal. XML canonicalization alone also leaves collection
ordering and workbench-specific transformations unresolved.

Do not weaken or replace those locks. Add a separate comparison profile:

| Treatment | Minimum rule |
| --- | --- |
| XML syntax | Ignore declaration/encoding, indentation, attribute order, namespace-prefix spelling and equivalent escapes after parsing. Resolve QName-valued `xsi:type`, not just XML tag names. Preserve raw hashes. |
| Unordered record collections | Build representation-ID inventories and Canonical-ID element bindings; use the object-specific identity roles from section 5. Ignore order only for unordered collections. Preserve duplicate diagnostics rather than overwrite records. |
| Text and language | Preserve all three name forms, Documentation, Markdown formatting, meaningful whitespace and language variants. XML-defined line-ending decoding is distinct from text rewriting. Do not globally trim, case-fold, paraphrase or strip Markdown. |
| Property IDs | Resolve references in each document and use exact unique field keys when IDs were recreated by the verified workbench. Retain both IDs and definition type/name evidence. Key renames/merges and duplicate keys remain ambiguous. |
| Property values | Preserve raw before/after strings; never normalise malformed Canonical IDs into conformity. Known importer whitespace/language effects are labelled transfer transformations/losses; causal evidence does not establish equivalence or justify hiding them. Parse only explicitly known JSON-valued fields for supplementary diagnostics, never as replacement graph authority. |
| Defaults and numbers | Normalize only schema-defined equivalent typed values or demonstrated semantically equivalent workbench defaults, with named/versioned rules. A default style/font/language change is not automatically cosmetic noise. |
| Navigation | Record missing item IDs as identity information loss; compare actual paths/membership/empty folders without treating paths as identity. Separate serialized sibling order from labels/placement and from Archi's documented name-based tree sorting. |
| Diagrams | Preserve nesting, geometry, labels, styles, viewpoint, View references, connection endpoints and bendpoint order. Node/connection order may affect stacking; classify such order changes as presentation or unresolved until interchange evidence establishes otherwise. |
| Other content | Preserve/report extension attributes, metadata and unsupported subtrees as residual differences. Never silently discard content because the current generator does not emit it. |

Normalisation means **proven representational equivalence**, not merely recurring
Archi behaviour. An unchanged Archi fixture calibrates these rules; it is **not** a
blanket diff to subtract from all future imports. Each applied rule needs a recorded
reason and tests demonstrating that nearby meaningful changes remain visible. Unavoidable
interchange losses remain visible in the first report, even when no human edit
occurred. No cleanup of Archi Properties or provenance is included.

## 7. Minimum useful change classifications

Keep detection (object/action/field) separate from interpretation. The same identity
can have semantic, presentation and transfer findings together.

| Class | Deterministic finding and review boundary |
| --- | --- |
| Identity violation / conformance unresolved | Missing or malformed 32-hex UUID, duplicated/mutated/conflicting canonical claim, proven retired-ID reuse or changed locked binding. State evidence and affected coverage. Apparent responsibility reuse without proof requires adjudication. |
| Candidate naming change | Same valid UUID with independent `displayName`, `friendlyName`, `canonicalName` or name-alias deltas. Show name-form/source standing and ancestry effects; never delete/create from a rename/relocation. |
| Candidate content or metadata change | Same ID, changed Documentation, language, Properties, alias list, semantic standing or provenance. Editable provenance is a claim, not authority. |
| Candidate semantic/type change | Same UUID, changed `archimateType` or `harmoniaType`; identity continues. Validate compatibility and presentation impact separately, with no implicit subtype or automatic replacement. |
| Candidate new element / deletion or retirement | Canonical ID only on one side, with identity and export-scope qualifications. Missing IDs, filtered exports, replacement and accepted lifecycle decisions remain distinct. |
| Candidate relationship / hierarchy change | Exact relationship ID plus changed type/direction/endpoints/Properties; additions/absence; explicit semantic decomposition. Distinguish structural-ID/owner/ancestry metadata and 462 metadata-only assertions from graph edges. |
| Governed projection / presentation reconciliation | View/navigation additions/absence/edits; occurrence/connection changes; nesting, geometry, style, annotation, stacking or routing. Preserve accepted compatible state; no Markdown semantic edit is implied. |
| Presentation conflict | Proposed semantics cannot safely coexist with retained curated state, or compatibility is unproven. Identify exact dependencies and retain the View for review. |
| Non-semantic representation equivalence | Only proven serialization/rekey/default equivalence, with applied rule and raw evidence. Canonical IDs and meaningful content must remain equal. |
| Transfer information loss / ambiguous change | Dropped folder identity, text/language/type/unsupported state, anonymous folder continuity, selective absence, inconsistent aliases/types, unclear annotation intent. A known exporter cause does not remove the finding. |

Separate five forms of hierarchy: semantic Composition/other explicit edges,
owner/framing/ancestry metadata, model-tree homes, View navigation, and nested visual
objects. No form authorises another. These are reconciliation classifications;
Mark and ChatGPT adjudicate identity continuity, semantic acceptance and destinations.

## 8. Minimum Change Reconciliation Report

Deliver deterministic Markdown and a JSON evidence file from **one comparison
result**, in the same first useful service increment. A small versioned result
contract suffices; separate renderer/core implementation increments add no value.

1. **Input provenance:** originating/incoming hashes, model IDs, release/source
   revision, registry/history/correspondence hashes, exporter version/options/locale,
   full-model scope, parser/schema/comparison-profile versions and validation results.
   Distinguish current RAD drift from changes to the originating pair.
2. **Readiness and coverage:** counts before/after, Canonical-ID present/missing/
   duplicate/noncompliant counts, unmatched claims, representation-only coverage,
   three-name/type conformance, unsupported content and completeness. A result can be
   useful with gaps, but cannot say “no architectural change” when coverage is unsafe.
3. **Architectural findings:** Canonical ID as primary key; both Exchange IDs;
   separate `displayName`, `friendlyName`, `canonicalName`, their name-form aliases
   and provenance; `archimateType` and `harmoniaType`; ancestry/context; exact baseline
   source/capture binding and standing/currentness; action/classification, field
   locator, before/after values and unresolved identity/authority questions.
4. **Relationship/impact evidence:** relationship ID, predicate/type/direction,
   old/new endpoint Canonical and Exchange IDs (including missing bindings),
   Documentation/Properties and affected Views/occurrences. Ten missing appearances
   do not equal ten semantic deletions. Metadata-only assertions remain metadata.
5. **Presentation findings and preservation status:** View and occurrence/connection
   IDs, semantic bindings, navigation homes/subtree evidence, structure, geometry,
   sizing/styles, endpoints and ordered routing. Identify the retained snapshot and
   whether state is pending approval, compatible, conflicted or unverified; expose
   replacement-View proposals without overwriting the curated original.
6. **Equivalence and information-loss evidence:** each applied normalisation rule,
   affected object/field, raw evidence and proof of equivalence. Separately list
   unexplained and known-but-lossy transformations, including unchanged-export losses.
7. **Review disposition:** initial `PENDING HUMAN REVIEW`, questions, source-grounded
   proposed destination (RAD, projection state, both or unresolved), and evidence
   locators. Human accept/reject/defer/rationale belongs in a separately maintained
   review record so rerunning the report cannot overwrite adjudication.

Retain full changed Documentation/Property/subtree evidence in JSON or a hash-pinned
source locator; Markdown can show focused diffs. Stable ordering is by class/object
identity/field, with representation IDs for unresolved records. Report-local item
numbers are evidence locators, not architectural IDs. Do not hide loss/deletions to
keep the report short.

Input admission and report generation are strictly read-only: no RAD, model,
registry, identity, relationship, projection-state or release mutation. Outputs are
explicitly selected report/evidence files only, with resolved-path protections
against input aliasing and governed destinations. Retaining input snapshots for
review does not mark their contents accepted. The later approved-presentation
publication hook is a separate explicit operation, not a report side effect.

## 9. Reuse and genuinely new work

| Reuse | Boundary |
| --- | --- |
| `generate_views.parse`, namespaces, `load_schema`, `validate_identifiers`, digest/signature primitives | Keep parser protections and local schemas unchanged; signatures are useful raw evidence, not the sole comparison algorithm. |
| `publish_navigation.navigation_homes`, architecture/presentation separation | Reuse target-to-path traversal; add anonymous subtree/empty-folder/order evidence where its returned paths are insufficient. |
| Original locks, documentation map, registry schema/history and binding validators | Verify base evidence, names, immutable bindings, aliases and tombstones; extend coverage checks without allocating IDs. Do not impose exact original populations on edited input. |
| Existing short labels, structural paths, aliases, Construct Type and provenance | Reuse source/grammar evidence with explicit three-name mapping and standing. Extend alias qualification only after decision; no parallel alias repository or subtype machinery. |
| Recovery crosswalk: exact keys, source assertions, relationship IDs and View scopes | Read as established baseline evidence. Do not execute historical recovery or LaTeX reconciliation to interpret an incoming export. |
| Current correspondence, textual baseline references, Motivation keys and Documentation classification | Enrich known changes with exact source/standing/history. Missing mappings remain absent. |
| Existing report conventions and tests | Reuse deterministic evidence/report rendering patterns and rejection fixtures; add tests for actual changed exports rather than recasting all changes as invalid publication. |
| Current publication/independent verification | Prove additions have not disturbed all 14 outputs or historical replay. Keep checks separate from ingestion execution. |

Genuinely new work is limited to:

- An identity/three-name/type diagnostic preflight and explicit active-field contract,
  extending existing registry/binding validation. Project only reviewed identity
  records after separately authorised allocation/migration; allocation/adoption and naming
  population are not ingestion features.
- A real unchanged-export fixture and transfer evidence/profile. Reuse existing
  parser, XSDs and preservation primitives; prove equivalence before normalising.
- One Exchange comparator plus Markdown/JSON renderer and bounded opt-in command,
  including semantic, presentation, loss and residual-content findings.
- A small approved-presentation manifest and publication replay/compatibility hook.
  Preserve referenced Exchange View state; do not build a layout language or merger.
- Optional diagnostic measurements at the existing publication phase boundaries.

Historical recovery is valuable for exact crosswalks, before/after preservation,
typed endpoints and independent replay. Its source-specific `DEFERRED` mappings,
LaTeX decoding, corpus-drift judgments and task labels are not an ongoing
reconciliation policy. In particular, frozen v0.2 `tools/reconcile.py` contains
normalized-name lookup plus explicit historical disposition tables. Those are
bounded historical decisions, **not** a reusable identity matcher for Archi input.
The frozen `baseline.py` adapts a prior transcriber in memory; it is not an Exchange
normalizer. Neither should be imported into the intake command.

## 10. Final least-step implementation sequence

Retain the three increments. UUID format and three-name semantics change the
increment-1 preflight and transfer specimens; they do not create a reason to move
comparison ahead of transfer proof or preservation ahead of explicit review.
No increment is implemented or authorised by this report. See section 15 for
remaining issues and their exact prerequisite boundaries.

### Increment 1 — diagnostic conformance and real transfer proof

**Smallest first authorisation:** one opt-in, read-only diagnostic preflight using
existing parser/identity/validation code, an explicit role/field contract, and
recorded Archi 5.10.0 transfer evidence. Do not add a failing new conformance gate
to the existing publication `all` target. Do not implement a general reconciliation
comparator/report, allocate UUIDs, transition EC identities, populate three names,
change aliases or alter accepted presentation.

The preflight must:

- Inventory every semantic element, exact canonical Property claim, three naming
  roles, native/Harmonia type, source/binding and standing; do not filter out
  historical, proposed, superseded or candidate elements present in the model.
- Validate the new exact 32-lowercase-hex UUID format, multiplicity, global ownership
  and known history; reuse existing UUIDv4 policy only when evaluating allocated
  records. Distinguish a completed audit from **NONCONFORMANT** model readiness.
  Existing 14-record validator PASS must not imply new-rule PASS.
- Separate legacy code/short-name/structural-path observations from actual active
  UUID/three-name fields. Identify missing/unresolved roles rather than populate
  them from similar field names, display folders or guessed string transforms.
- Preserve the old adoption/binding proof and use a distinct active-model diagnostic
  path. Record the planned unambiguous current Property roles under the existing
  `Baseline.property` convention; coding exact non-conflicting field keys is routine,
  but field semantics, naming grammar and legacy transition are not inferred.

**Transfer deliverables:**

1. A full-model unchanged import/export specimen tied to the exact 1.0.6 hash,
   exporter/plugin/options/locale and scope; retain raw inputs and all observed
   transformations/losses. The accepted working copy is evidence only if absence
   of edits and originating bytes can be established. GUI acceptance alone is not
   that evidence. Store under existing tests/fixtures or a hash-pinned evidence
   location without moving or duplicating the base unnecessarily.
2. A compact **synthetic contract specimen**, kept distinct from architecture,
   carrying fixed test UUIDs (including leading-zero/digit cases), native Name as
   displayName, separate friendly/canonical qualified-name Properties and existing
   type/alias metadata. Use supplied literal examples, not a new derivation grammar
   or allocated Harmonia identities. Exercise it through the real workbench and
   record key/value/owner/language fidelity. It proves new carriers that 1.0.6 lacks;
   it does not implement/populate the three-name model. A mocked XML export alone
   cannot establish Archi fidelity.

Use existing parsing/snapshots and targeted assertions/probes to record the transfer
profile; a full general comparison algorithm belongs in increment 2. Verify model,
semantic, relationship, View, occurrence and connection IDs; all legacy EC key/value
bindings; names, Properties, Documentation/languages; relationship endpoints; View
structure, geometry, sizing/style, annotations and ordered routing. Enumerate
Property-definition rekeying and folder/item-ID loss. Do not subtract unexplained
no-edit differences or claim losslessness merely because the exporter causes them.

**Expected validation:** the current model is audited as 1,299 elements, 14 preserved
legacy EC-code fields, 1,285 absent historical fields, **0 active UUID32 values and
1,299 required allocations**, with no duplicate legacy claims. Malformed active UUID
claims are a separate case from known historical alias/provenance data. Native Names
exist for all; no explicit friendlyName exists; legacy short names and the
190 Established/756 Proposed structural paths retain their roles and standing.
This is a successful diagnostic, **not** successful model conformance.
Targeted tests cover malformed formats/case/whitespace, duplicates and mutation,
separate same-ID naming/type changes, absent/ambiguous role fields, namespace
separation, Feature compatibility and no writes. Every unexpected transfer/loss
finding remains visible. Do not require the accepted model to pass the new contract
by altering it. Existing publication/identity/architecture tests and pinned replay
remain separate required checks.

**Acceptance gate:** raw export evidence and the supported transfer/loss profile
must be reviewable. If real workbench export is unavailable, fidelity remains
UNRESOLVED and this deliverable is incomplete; diagnostic work can still finish.
No extra architectural decision is needed to begin this diagnostic scope after
explicit implementation authorisation. The EC alias/new-UUID decision is already
settled. A friendly-name algorithm is not needed to report its absence honestly.

**Readiness work before full-service conformance:** separately authorise new UUID
allocation for all 1,299 represented elements and the active alias/provenance
migration already adjudicated for the EC elements; confirm qualified-name/friendly-name/
alias rules and bounded source-based instance bindings. Then version the active
source/register/schema contract and extend reviewed bindings/current publisher,
retaining old proof against pinned fixtures and all existing representation IDs.
Do not reinterpret existing qualified-path values as UUIDs or existing short labels
as complete qualified/display names. Use explicit versioned role mappings; old
field spellings do not constrain the active contract. Do not invent ancestry or
allocate IDs just to clear diagnostics. This is not an automatic or bulk migration
inside any intake command. A useful reviewed cohort may precede whole-model
conformance within one coherent semantic model.

**Stop:** increment 1 alone authorises neither model/naming/alias migration nor the
comparison service, presentation acceptance or directory movement. Its fixtures
and findings make the next bounded authorisation concrete.

### Increment 2 — deliver one conservative comparison-and-report command

**Deliverable:** one general Exchange comparison result and deterministic
Markdown/JSON Change Reconciliation Report, following sections 5–8, exposed through
one explicit local command and opt-in Make target. Illustrative invocation:

```text
reconcile_exchange.py --published <exact-base.xml> --incoming <archi-export.xml>
                     --published-evidence <pinned-release-and-identity-evidence>
                     --output-directory <explicit-review-output>
```

Keep a small module/entry point under existing `tools/`; a separate core/renderer
release is unnecessary. Use the verified equivalence profile, generic admission
checks, Canonical-ID matching and exact source enrichment from the base. Report
legacy gaps rather than requiring invented IDs to produce any evidence. Retain
both input snapshots and their presentation state. Report intake **never** updates
RAD, identity, projection state or release. Do not add ingestion to `make all`.

**Review evidence:** self-comparison has no deltas while retaining pre-existing
conformance findings. Unchanged real export has zero unexplained edits only if
proven, with all known losses still disclosed. Targeted
fixtures cover a valid same UUID plus independent display/friendly/canonical-name,
content/Property/alias/ancestry/native-type/Harmonia-type changes,
same-ID different representation ID, changed/missing/duplicate canonical claims,
new/absent IDs, retirement/reuse evidence, two same-name distinct identities and
forged source Properties. A missing-ID base must never receive a full-coverage
clean result. Non-reuse claims are qualified by history coverage.

Also cover relationship type/direction/endpoints and parallel assertions;
semantic hierarchy versus structural metadata/folders; View/occurrence/connection
additions/absence, shared appearances, visual nesting, layout/style/stacking/routes,
annotations and conflicts with proposed semantic deletion/type/endpoint changes.
Loss of languages, unknown content, folder IDs or Property keys remains visible.
Nearby substantive edits must survive every equivalence rule. Selective exports,
DTD/invalid/duplicate/dangling/cross-View references and unsafe output paths produce
bounded diagnostics. Same inputs reproduce Markdown and JSON in independent
processes; report items locate exact before/after evidence. Input/registry/projection/
release hashes stay unchanged. Existing relevant checks still pass.

**Stop:** this is the minimum useful **read-only first service**, with readiness
limits stated prominently. Reviewers can adjudicate a bounded change set. Do not
republish over accepted manual presentation until increment 3 is in place; do not
claim that a report alone completes the preservation lifecycle.

### Increment 3 — preserve approved View state during republication

**Deliverable:** the small snapshot-reference/approval manifest and compatible-View
replay hook described in section 5. A separately authorised approval action selects
presentation scopes; intake is still read-only. Reuse existing View/occurrence IDs
and Exchange state, exact reference validation and semantic-preservation checks.
Apply approved state after default layout and before final publication validation.
Generated-only scopes continue using working generators. No automatic View merger
or automatic semantic Markdown write-back is required.

**Review evidence:** accept an edited View in a fixture through an explicit review
record; republish compatible renamed/documented/metadata-edited semantic elements
and show that View structure, occurrence identity, geometry, sizes, styles and
routing remain preserved. Verify approved homes/contexts under the navigation
contract. Unapproved incoming presentation must not be applied. Conflicting semantic
deletion, edge retyping/endpoint change, unsafe remap, missing Property definition
or unsupported state must produce exact conflicts and leave the stored curated
snapshot unchanged, rather than silently overwrite/omit it. A replacement View
requires explicit authorisation and retains original evidence.

Fixed approved input/state produces deterministic output. Unrelated generated
Views, semantic identities/relationships and historical replay remain unchanged
except explicitly accepted semantic/release changes. A bounded end-to-end evidence
case can now show human-adjudicated RAD edits, separately approved presentation,
validated republication and re-import. Do not implement automated RAD modification.
The semantic edits themselves remain a separately authorised architectural change.

**Stop:** compatible presentation retention is established. Defer sophisticated
merge/repair, non-element canonicalisation and broad identity migration unless
separately authorised; none is needed merely to report a proposed change.

### Release acceptance is not a byte-preservation exception

Every separately accepted increment still receives the required patch. The current
generator reads root release state directly, and `report_baseline.py` checks current
release/evidence equality while rendering literal 1.0.6 task prose and a fixed
14-file output set. It is a release-specific completion renderer, not yet a
release-neutral report. Consequently, a future patch cannot both leave all current
published release metadata unchanged and pass the same current-release gates.

The least disruptive preservation mechanism is to retain the exact 1.0.6 artifact
and replay the accepted source revision recorded above in an isolated temporary
checkout/extraction. Do not temporarily change the live root release to run replay.
When accepting an increment, refresh the affected **new-release** evidence and any
deliberately republished release metadata under the existing policy. If the default
publication is advanced, narrowly adapt its current completion-report/guide handling
to the new change set; do not relabel the historical 1.0.6 correction narrative as
a new correction or overwrite historical evidence. A root-only release bump with
stale current publication checks is not a completed increment.

This release integration is required housekeeping in increment 1, not authority
for identity migration, directory redesign or semantic change beyond an explicitly
reviewed batch. Validation evidence above means unchanged-content guarantees
**within a fixed release/input set**, plus explicit accounting for release metadata
when that set advances. Timing instrumentation itself must leave fixed-input
deterministic bytes unchanged.

## 11. Directory direction and migration dependencies

`docs/archimate/framework/` plus `exchange/publish/` and `exchange/ingest/` is an
appropriate eventual separation of reusable tooling and directional artifacts.
Published files remain subordinate projections; ingested files remain proposals.
This layout should not imply that every file outside those directories is current
architecture, nor that historical recovery is an active ingestion channel.

It is **not a prerequisite** to the revised increments. Moving files now would
introduce more work and threaten pinned replay without fixing identity coverage.
Keep new modules/tests beside their current equivalents until the first report
and input/approved-presentation artifact contracts have been reviewed. Give the
approved presentation manifest/snapshot references an explicit publish-side governed
state home, distinct from unreviewed `exchange/ingest/` exports. For the first hook,
that can be beside current projection metadata; no new directory hierarchy is
required. Preserve snapshot-relative references and hashes in any later move.
If restructuring is then justified:

1. Establish explicit repository/base/schema/input/output path parameters or a
   shared path resolver while preserving existing defaults. Add compatibility
   entry points and a testable old/new invocation contract before moving active
   files. Do not turn this into a general publisher rewrite.
2. Move reusable active code/schemas/tests to `framework/` and active publication/
   intake artifacts to directional `exchange/` subdirectories in one separately
   reviewed migration. Preserve the 1.0.6 publication as a release-qualified
   reproducible artifact, using exact bytes/identities and its recorded evidence.
   Keep frozen original/recovery paths in place; archive/history naming is a
   separate choice, not a reason to rewrite them.
3. Retain compatibility launchers/paths for frozen consumers or use an explicitly
   versioned replay checkout/adapter. Verify original 14-file reproduction and
   recovery replay independently of the new defaults. Update current links and
   manifests deliberately; do not rewrite historical manifests/hashes/reports to
   pretend they originated under the new layout. Retire compatibility only after
   all governed and frozen consumers are accounted for.

Concrete path dependencies requiring preservation or explicit migration:

| Dependency | Observed coupling |
| --- | --- |
| Base/root discovery | `generate_views.BASE = __file__.parents[1]`; recovery `ROOT = BASE.parents[2]`; synchronisation inherits both. Moving tools changes source roots and outputs. |
| Imports | Sibling absolute Python imports among generation, recovery, navigation, feature and report modules; tests inject `BASE/tools`; `scripts/architecture_identity.py` injects the exact existing tools path. |
| Defaults/Make | Relative baseline/schema/map/output/report variables and `REPOSITORY=../../..`; active synchronisation CLI has fixed RAD/DEST and only a `/tmp` reproduction override, unlike configurable legacy publishers. |
| Independent verification | `verify_baseline_outputs.py` fixes 14 outputs, imports report paths, executes tools at `BASE/tools` and compares repository-relative mirrors under `/tmp`. |
| Release-specific reporting | Synchronisation loads the root release without a release-file CLI option; `report_baseline.py` combines current release/hash gates with literal 1.0.6 prose. Preserve the original replay and deliberately maintain future current-release evidence instead of assuming the renderer is generic. |
| Schemas | Diagram → View → Model schema links and XML/DC schema dependencies need sibling files and unchanged offline resolution. Provenance hashes pin all five XSDs. |
| Governed correspondence | `baseline/correspondence.json` pins recovery model/crosswalk/manifests and authority/source hashes; `documentation-classification.json` pins capture text/classifications. A governed path change is an explicit correspondence change. |
| Documentation manifest/identity | `documentation-map.json` resolves its source relative to itself; registry provenance/adoption records and identity scripts/tests reference the old Exchange/map/lock locations. Accepted identity history cannot be rewritten for convenience. |
| Frozen recovery | `recovery-v0.2/source-manifest.json` pins 75 source/tool/baseline files and exact paths; the recovery generator itself is pinned by current verification evidence. Snapshot `baseline.py` relies on sibling frozen versions. |
| Embedded references | Generated XML Properties contain RAD/source paths; Markdown uses relative links/anchors; current README, guide and governance links identify the current area. Moving published XML need not alter its historical embedded provenance. |
| Evidence/reports | Navigation and recovery reports validate pinned command logs and file hashes; synchronised preservation proofs and `verification.json` pin code, outputs and logs. Refresh current evidence deliberately; retain historical evidence unchanged. |
| Historical top-level publication | `Harmonia-Capability-Architecture.xml`, original fixture/lock and legacy report tools remain replay inputs. `legacy-all` needs its historical release fixtures; it is not equivalent to current `all`. |

Do not globally substitute paths in historical XML/JSON, silently rehash evidence,
or rely solely on symlinks: `.resolve()` and parent-based root discovery can alter
behaviour. Where a future default publisher legitimately changes, retain a pinned
1.0.6 replay path rather than claiming the new default's bytes are the old release.

## 12. Model and View scalability measurements

Preserve **one coherent semantic model**. Bounded contextual Views remain the
primary usability mechanism. Current broad overview Views provide empirical
measurement subjects, not justification to split the model or impose limits.

Add measurement at the **existing publication generation and serialized-validation
boundaries**, as soon as those hooks are touched for the identity projection
contract (after increment-1 decisions), or no later than presentation replay in
increment 3. It can be a small adjunct change authorised independently; it does not
block transfer proof or the first report. Count the final serialized/reparsed output
including approved presentation, and time generation and validation separately.
Reuse `synchronise_baseline.counts` and the actual View trees; include all nested
node/connection appearances. Report distinct referenced semantic elements separately
where useful, because one element may appear several times.
Use a deterministic tie-break when several Views share the largest count.

Existing validation reports already expose semantic/relationship/View totals and
some per-View data in older publishers. Missing consolidated measurements are total
visual objects/connections, largest-View details, bytes and phase timings. Add an
**optional diagnostics sidecar/stdout result**, observed through the existing
publisher, rather than a parallel generator. Keep elapsed times, machine context
and invocation time outside deterministic validation/report bytes and outside
the 14-file identity/reproduction set. Metrics output must not overwrite inputs
or become an authority source. Stable input hashes tie a measurement to a model.

An assessment-only, one-pass in-memory measurement produced:

```text
ArchiMate Model Size — Release 1.0.6

Semantic Elements:       1,299
Relationships:            852
Views:                  1,196
Visual Objects:         3,915   (3,876 Element; 39 Label)
Visual Connections:     1,449

Largest View:
  View:                  Business Capability Model — v0.2
  ID:                    id-9ac38e89ff405925aaa995f4f9ae79e8
  Elements:              200    (200 element appearances; 200 visual objects)
  Connections:           182

Generated XML size:      19.806014 MB
Generation time:          1.485 sec
Validation time:          2.513 sec
```

Generation above means `load_inputs + Baseline.build + serialize`, once.
Validation means separate serialized reparse plus `synchronise_baseline.validate`,
once. It excludes Markdown/report rendering, the second determinism generation,
independent reproduction and native Archi import/render performance. These are
one local run's diagnostics, not a benchmark or threshold. The measured generated
SHA matches the accepted published bytes. No metrics implementation was added.
A future wrapper should state phase scope and can separately measure the whole
publication invocation. Native workbench responsiveness needs separate empirical
observation; XML size or generation time does not establish GUI performance.

## 13. Risks and deliberately deferred decisions

The architectural identity purpose, type independence, opacity, exact persisted
format and three naming purposes are decided. No prefix policy remains open.
Concrete remaining architectural questions and implementation constraints are
classified, with their prerequisites, in the final section 15 rather than treated
as a reason to redesign working publication.

| Risk | Minimum containment |
| --- | --- |
| Legacy EC codes treated as conformant UUIDs, or converted | Keep codes as identifier aliases/provenance with frozen proof; separately allocate new UUIDs under the adjudicated role transition. |
| Old `canonical_name`/`canonical_id` semantics silently swapped | Explicit versioned active mapping; retain snapshots; distinguish old short labels, qualified names and immutable UUID. |
| Names/types/context drive identity allocation or continuity | Canonical-ID match first; independent field deltas; no name matcher, structural derivation or publication-ID promotion. |
| Missing identity/ancestry hidden by an all-green model result | Count every represented semantic element; separate audit completion, conformance and architectural approval. |
| Name aliases confused with identifiers or historical context | Preserve existing namespaces/provenance; qualify name forms by approved extension; do not validate history as current ancestry/type. |
| Unknown export or partial scope treated as architectural edits/deletions | Pin originating artifact/scope, record real unchanged and synthetic contract transfer evidence, qualify absence. |
| Real content/presentation loss normalised away | Proven equivalence per rule; raw evidence plus explicit transfer/residual findings. |
| Curated Views overwritten during publication | Retain snapshots at intake; explicit accepted-state replay and compatibility checks before republication, with visible conflicts. |
| Active conformance changes invalidate frozen proof/current release evidence | Separate pinned historical fixtures/checks and versioned current validation; refresh only new-release evidence under policy. |
| Directory/timing work destabilises evidence | Defer moves; optional metrics outside deterministic output bytes. |

Deliberately deferred: automatic Markdown write-back/acceptance; ID allocation or
alias/naming population by ingestion; bulk migration; resolving responsibility
continuity or replacement/merge/split by code; additional canonical identities for
non-elements; automatic View merge/repair/replacement; Properties/provenance cleanup;
native Archi file parsing/unattended export; physical directory migration; schema
upgrades unrelated to the active contract; new specialisations/architecture layers;
LaTeX recovery/recomposition; model partitioning/arbitrary size limits.

Approved-presentation preservation remains a required later increment. Identity
allocation and remaining naming rules require reviewed work; they do not prevent
the diagnostic first increment from exposing nonconformance.

## 14. Inspection and verification record

Direct inspection covered the following files or, for large structured/model
files, their relevant contracts, records, indexes and parsed contents. Historical
evidence was used as evidence of machinery, not authority to change RADS.

**Governance and current source correspondence**

- Root `AGENTS.md`, `docs/AGENTS.md`, `architecture-release.json`.
- `docs/markdown/governance/architecture-release-policy.md`,
  `architectural-axioms.md` (especially AX-04/14/15/17),
  `rad-archimate-model-mapping.md`, `archi-navigation-and-view-convention.md`;
  relevant catalogue rows and publication-slice/gate sections of
  `rad-archimate-mapping-catalogue.md` and `rad-archimate-publication-plan.md`.
- `docs/markdown/governance/architecture-identity/model.md`, `registry.json`,
  `registry.schema.json`, `adoption-1.0.3.md`, relevant coverage/mapping findings in
  `inventory.md`, and Business metamodel §8 and Feature definitions;
  binding/lock validation in `scripts/architecture_identity.py`, corresponding
  `scripts/tests/test_architecture_identity.py`, and shared release loading.
- `docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md`,
  `reference-model/README.md`, `reference-model/business-architecture.schema.json`
  and `reference-model/business-architecture.json` beneath that Business directory;
  `scripts/rads_business_architecture.py` for qualified-path/ancestry validation,
  name/ID-alias roles and search. The synchronised 946-record source remains
  `docs/markdown/governance/baseline/architecture.json`; the earlier reference
  capture is supporting machinery/evidence, not a replacement current population.
- `docs/markdown/governance/baseline/README.md`, `architecture.json`,
  `correspondence.json`, `motivation.json`, `documentation-classification.json`;
  the generation/check process also validates the source presentation overlays
  and exact referenced/pinned source material. Generated catalogue correspondence
  and discrepancies were checked through current publication verification.

**Publication framework and models**

- `docs/archimate/capability-architecture/README.md`, `Makefile`, `requirements.txt`,
  `documentation-map.json`, original `baseline/semantic-lock.json`,
  `baseline/navigation-baseline-1.0.3.json` and original baseline Exchange model.
- Active `synchronised-baseline/Harmonia-Architecture-Baseline.xml`,
  `validation.json`, `verification.json`, `archi-inspection-guide.md`, current
  `2026-10-17-archimate-markdown-baseline-synchronisation.md` and preservation
  checks invoked by the generator/report; historical native CLI/display logs under
  `verification/projection/`.
- `tools/generate_views.py`, `publish_documentation.py`, `publish_navigation.py`,
  `synchronise_baseline.py`, `feature_projection.py`, `verify_baseline_outputs.py`,
  `report_baseline.py`; relevant report/preservation entry points in
  `report_recovery.py`, and dependencies on legacy report/assessment tools.
- Tests `test_baseline.py`, `test_views.py`, `test_documentation.py`,
  `test_navigation.py`, `test_recovery.py`, `test_publication_metadata.py`;
  all were executed in the 102-test publication suite.
- `schemas/archimate3_Model.xsd`, `archimate3_View.xsd`, `archimate3_Diagram.xsd`
  (identifier, reference, property, Documentation, organisation and diagram
  contracts), `provenance.json`; the bundled schema set including `xml.xsd` and
  `dc.xsd` was exercised by offline validation/provenance tests. It is Archi 5.9's
  Exchange 3.0 namespace / 3.1 diagram schema, unchanged for supported Archi 5.10.0.

**Historical recovery and additional implementation evidence**

- `tools/recover_v02.py`, `recovery-v0.2/source-manifest.json`,
  `representation-crosswalk.json`, `architecture-release.json`,
  `Harmonia-Architecture-1.0.5A.xml`, `1.0.5A-recovery-report.md` and replay validation.
- Frozen `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools/reconcile.py`
  (lookup/disposition mechanism), `tools/baseline.py`, identity-lock and reviewed
  identity-history structures; no frozen evidence was modified.
- Installed plugin classes `XMLModelImporter`, `XMLModelExporter`,
  `XMLExchangeUtils`, read through bounded static `javap`; plugin provenance and
  decisive methods are recorded in section 4. This is supplementary machinery
  evidence, not repository-held architectural authority.

### Original assessment checks retained as historical evidence

All commands below used bounded `timeout --signal=TERM --kill-after=5s` execution
and `PYTHONDONTWRITEBYTECODE=1` for Python. Temporary logs/reproduction were under
`/tmp/harmonia-roundtrip-assessment-20261018/` or the verifier's temporary directory.
No `make all`, normal publication write, release bump or round-trip implementation
was run.

| Check | Bound | Actual result |
| --- | ---: | --- |
| `python3 docs/archimate/capability-architecture/tools/synchronise_baseline.py --check` | 90 s | PASS; current bytes/hash and deterministic regeneration agree. |
| `python3 -m unittest discover -s docs/archimate/capability-architecture/tests -v` | 120 s | PASS; 102 tests, 64.224 s, no failures/errors. |
| `python3 docs/archimate/capability-architecture/tools/report_baseline.py --check` | 30 s | PASS; current report/inspection-guide evidence agrees. |
| `python3 docs/archimate/capability-architecture/tools/verify_baseline_outputs.py` | 120 s | PASS; all 14 outputs independently reproduce byte-for-byte; release unchanged. |
| `python3 scripts/architecture_identity.py validate` | 60 s | PASS; 14 registered records, no allocation. |
| `python3 scripts/architecture_identity.py bindings --check` | 60 s | PASS; 14 retained exact bindings. |
| `python3 docs/archimate/capability-architecture/tools/recover_v02.py --check` | 90 s | PASS; pinned 1.0.5 fixture, 1,165 elements / 823 relationships / 984 Views, historical XML hash unchanged. |
| `python3 scripts/architecture_release.py check` | 30 s | PASS; 1.0.6 / previous 1.0.5. |
| `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` | 180 s | PASS; 90 tests, zero failures/errors/skips; BUILD SUCCESS, 27.380 s. Offline mode used cached dependencies. |
| Assessment-only model count/timing/signature probes and static plugin inspection | 20–45 s per operation | PASS after correcting a temporary timing-probe heredoc invocation; no production/test failure. Values and limits are recorded above. |
| Native Archi unchanged import/export | Not executed | UNRESOLVED empirical fidelity; historical GTK failure was not rerun unchanged. |

### Checks retained from the preceding post-review revision

Fresh non-mutating checks used the same timeout convention and Python bytecode
suppression. Temporary probes/logs remained under `/tmp/`; no service code, fixture,
model, RAD or governed metadata was written.

| Check | Bound | Revision result |
| --- | ---: | --- |
| XML/registry/capture Property audit with secure `lxml` parser | 25 s | PASS as a census: 1,299 elements, 14 unique single-valued legacy EC values, 1,285 without that historical Property; exact registry bindings agree. The final adjudication preserves EC codes as identifier aliases/provenance, not UUID allocations. All 1,299 lack active UUID32 canonicalIds. Name/alias/type findings are in section 4. This does not claim new-rule conformance. |
| `synchronise_baseline.py --check` | 90 s | PASS; deterministic generation and accepted XML SHA agree. |
| `architecture_identity.py validate` | 60 s | PASS; 14 records under the existing policy. Does not validate new-rule coverage or the new UUID format. |
| `architecture_identity.py bindings --check` | 60 s | PASS; 14 exact retained bindings. |
| `report_baseline.py --check` | 30 s | PASS; existing generated report/guide unchanged. |
| `verify_baseline_outputs.py` | 120 s | PASS; all 14 outputs independently reproduce byte-for-byte, release unchanged. |
| `architecture_release.py check` | 30 s | PASS; 1.0.6 / previous 1.0.5. |
| Required offline JDK 21 Maven architecture suite, command as above | 180 s | PASS; 90 tests, zero failures/errors/skips; BUILD SUCCESS, 28.914 s. |
| Report links, complete missing-ID selector, whitespace and workspace preservation | 30 s each | PASS; all 7 local links resolve, XPath selects exactly 1,285 missing elements, fences balance and no trailing whitespace is present. Only the requested report differs; model/registry/release hashes and all 14 generated outputs remain unchanged. |
| Unchanged real Archi export | Not executed | UNRESOLVED fidelity proof; later GUI acceptance is established by Mark, not an export fixture. |

The original 102-test publication suite, recovery replay, timing probes and static
plugin analysis were not rerun merely to revise prose; their earlier results and
limits are retained above. No new implementation tests were added.

### Final reconciliation validation

Fresh read-only inspection additionally covered the three-name/source contracts,
`business-architecture.schema.json`, `scripts/rads_business_architecture.py`
(structural/name/alias validation), all native type/Property populations, registry
origins/history/version checks, and the importer Property path in the previously
captured static Archi inspection. Exact searches found no implemented friendlyName
or three-role naming derivation in the inspected governance/capture/publisher code.
The UUID and naming conclusions in section 4 are this task's fresh census, not
inferences from historical report wording.

| Check | Bound | Final-reconciliation result |
| --- | ---: | --- |
| Secure XML/source/registry census and literal UUID probe | 25 s | PASS as inspection: 1,299 elements; 14 unique legacy EC values, none a UUID32; 1,285 without that historical Property; all 1,299 require new active UUID32 metadata. Known EC aliases/provenance are not malformed active allocations. All 12 native types already carry Properties. No display/friendly fields in the capture, 946 native/source names, 719 old short canonical names, 946 structural paths (190 Established/756 Proposed), 44 name-alias and 430 ID-alias lists. User's UUID example parses as lowercase RFC 4122 UUIDv4 hex. |
| `synchronise_baseline.py --check` | 90 s | PASS; deterministic generation and unchanged accepted SHA. |
| `architecture_identity.py validate` | 60 s | PASS under the old contract for 14 retained records; no claim of new conformance. |
| `verify_baseline_outputs.py` | 120 s | PASS; all 14 outputs independently reproduce byte-for-byte, release unchanged. |
| Required offline JDK 21 Maven architecture suite, command above | 180 s | PASS; 90 tests, no failures/errors/skips; BUILD SUCCESS, 28.459 s. |
| Final report checks and protected-artifact hashes | 30 s | PASS; 7 local links resolve, 5 fenced blocks balance, 15 sections and table widths agree, no trailing whitespace, staged/unstaged diff checks pass. Historical-Property XPath selects exactly 1,285; all 1,299 lack active UUID32 metadata. Model/registry/release/adoption/verification/recovery bytes match HEAD, accepted model SHA unchanged. Only the requested report differs; staging is preserved. |
| Real unchanged Archi export and synthetic contract transfer | Not executed | UNRESOLVED empirical fidelity; these are increment-1 deliverables, not established by GUI acceptance or string parsing. |

The earlier publication suite, frozen recovery replay and static plugin evidence
remain retained with their original dates/scope; no repeated service tests or
fixtures were introduced. Only this report is revised. RADS, Release 1.0.6, model,
identity register, generated outputs, historical evidence and directory layout are
preserved. No identities/names/aliases were allocated, migrated or implemented.

## 15. Implementation Readiness and Remaining Issues

### Readiness conclusion

**The architectural identity contract is unambiguous:** immutable instance
identity, type-independent and semantically opaque, with a 32-lowercase-hex UUID
Property. All three names and both types are independent mutable governed attributes.
The native-type-prefix proposal is withdrawn, not an outstanding question. The
legacy EC treatment is also settled: preserve the codes as identifier aliases/
provenance, and allocate new UUID32 identities to those 14 elements like every other
element. Existing qualified `canonical_id` paths correspond to canonicalName;
historical field spellings impose no constraint on the new semantic roles.

**It can be implemented using the existing framework, but the accepted model does
not yet conform.** Zero elements satisfy the new UUID carrier contract, and no full
three-name contract exists in current source/Properties. All 1,299 elements need
future UUID allocation. Allocation/migration and name derivation cannot be smuggled
into a validator. A two-snapshot comparator also cannot by itself establish global
lifetime uniqueness/non-reuse; registry/history remains required.

**There is no evidenced BLOCKER to starting the diagnostic increment 1 defined in
section 10**, once explicitly authorised. It intentionally observes nonconformance
and preserves unresolved decisions. A real workbench export is needed to **complete**
its fidelity deliverable. It must not be accepted as a lossless or fully conformant
model result while that evidence or metadata is absent. If increment 1 were expanded
to migrate identities, populate names or enforce full conformance, the decisions
below would first have to be resolved; that is outside the bounded proposal.

### Classified issue register

“Before increment 1?” means before starting the diagnostic increment as defined,
not permission to disregard the issue at a later acceptance/migration gate. Each
row gives a primary classification from the requested categories.

| Issue | Classification | Supporting evidence and consequence | Before increment 1? |
| --- | --- | --- | --- |
| R1 — EC alias/provenance and future UUID treatment | IMPLEMENTATION CONSTRAINT | **Adjudicated:** preserve EC-01–EC-14 as identifier aliases/provenance, allocate new UUID32 values for the same 14 instances, and retain their exact Exchange bindings/frozen proof. No conversion or active grandfathering remains under consideration. Current history rejects rekey/removal and native-code aliases, so use a versioned active register/history mapping with old-code → same-instance → new-UUID evidence; keep historical validation pinned and unchanged. | No; diagnosis records the settled treatment. Execution requires separate allocation/migration authorisation, not another EC architectural decision. |
| R2 — UUID coverage and source-based instance continuity | ARCHITECTURAL ADJUDICATION REQUIRED | All 1,299 need new UUIDs: 946 capture (including the 14 EC elements), 214 unbound historical and 139 Motivation elements. Universal coverage is decided, irrespective of standing. The 214 have no proven RAD crosswalk; where instance continuity/overlap is unresolved, do not fabricate distinct instances to avoid duplicates or merge by labels. Adjudicate those bindings before allocation of that cohort. | No; source/instance ambiguity must be resolved before affected adoption/allocation and full-model conformance, not before a census. |
| R3 — UUID casing/version/variant and generation policy | NON-ISSUE / ALREADY COVERED | Identity model specifies lowercase, random UUIDv4 once; validator enforces v4 for allocated records. New persistence is `.hex`, with a full-string 32-character check. No UUIDv7/time/name/type allocation policy is needed. EC codes are preserved aliases under R1, not a UUID encoding variation. | No additional policy prerequisite; increment 1 implements diagnostics and allocates nothing. |
| R4 — Active versus frozen identity validation | IMPLEMENTATION CONSTRAINT | `verify_adoption`/`verify_bindings` freeze 14 records, one event, register bytes and original content; `validate_registry` expects URNs/native retained codes. Add a distinct opt-in active check using current XML/accepted history. Preserve frozen fixture/validator versions; do not loosen history gates. Future active schema/transition must be versioned in the existing mechanism. | No; this separation is required in increment 1. |
| R5 — UUID ownership/collisions/tombstones | IMPLEMENTATION CONSTRAINT | Existing validator has ownership, alias, semantic-binding, append-only history, retirement and tombstone checks. Extend the current-model population beyond the historical top-level reserved IDs. Canonical UUID and XML-ID namespaces are separate; metadata uniqueness cannot be proved just by `validate_identifiers` or two snapshots. | No; reuse/check known history in increment 1, report coverage limits. No automatic collision retry/reallocation. |
| R6 — No native Property carrier limitation | NON-ISSUE / ALREADY COVERED | All 1,299 elements across 12 native types already carry Properties. Bundled Concept/Element XSD accepts string values; numeric-leading UUID Property values are legal. Existing publisher/importer handles key/value metadata. Canonical UUID is not an XML identifier. | No; no schema/type redesign is justified. |
| R7 — Qualified-name persistence and friendlyName derivation | ARCHITECTURAL ADJUDICATION REQUIRED | Recommend **persisted-and-validated** friendlyName/canonicalName in the active source and Exchange Properties, reusing existing structural paths and validators. Confirm that choice and the friendly-name source/token grammar (including whether initially authored governed values are permitted). No friendly-name deriver exists; `chain`/owner traversal supplies context but not token/punctuation/acronym/separator/collision or naming-standing rules. Folder paths cannot supply authority. | No for absence diagnostics/literal transfer specimens; yes before deriving/populating friendlyName or claiming deterministic conformance. |
| R8 — canonicalName grammar coverage and source standing | ARCHITECTURAL ADJUDICATION REQUIRED | Qualified `canonical_id` → canonicalName is **decided**; field renaming is not an architectural obstacle. Existing paths cover 946 capture objects, 190 Established and 756 Proposed. The other 353 lack projected structural paths; no universal Motivation grammar is established. Person Identity's example path is currently Proposed. Resolve missing grammar/context and affected source standing without inventing ancestry or promoting all paths. | No for diagnosis or semantic role mapping; yes before approving/populating unsupported qualified names or claiming full naming conformance. |
| R9 — Legacy short-label source versus displayName | IMPLEMENTATION CONSTRAINT | `name`, `reviewed_name` and short `canonical_name` supply naming/source-standing evidence, not interchangeable final displayNames. XML Name can carry displayName, but `[CTn]` currently qualifies folders only. Use the approved source/qualification per cohort, retaining conflicts for review. After R7's persistence decision, version the active source/schema and separate qualified-name Property roles. No alternate-language overload or generic `name` collapse. | No; inventory and literal carrier tests are increment-1 work. Any unresolved concrete rename/qualification is reviewed before population. |
| R10 — Name-form aliases, historical structural aliases and reclassification | ARCHITECTURAL ADJUDICATION REQUIRED | `name_aliases` is untyped strings; `id_aliases` is structurally validated/non-reusable ID history; registry `aliases` is identifier-only. Old terminal-type checks reject an alias of a formerly different type. Select a small extension in the same model (tagged records or form-specific lists), preserving legacy namespace/provenance and historical-context validation. | No for inventory/preservation; yes before alias extension/migration or automatic name-history population. |
| R11 — CM-R05 under the three-name model | ARCHITECTURAL ADJUDICATION REQUIRED | Business catalogue says global Capability name uniqueness; `rads_business_architecture` and registry checks compare short `name`. The qualified canonicalName is a different concept. Decide rule application without claiming the existing BC/BEC HIE conflict is solved by UUIDs or qualified names. | No; preserve the existing finding. Required before changing the naming rule or its conformance claims. |
| R12 — Real unchanged export is not yet proven | IMPLEMENTATION CONSTRAINT | GUI acceptance and static ID/key preservation paths are not full empirical evidence. No real unchanged fixture found; record exact originating hash, scope/options/locale, all values and presentation. If native execution/input remains unavailable, fidelity stays UNRESOLVED. | No to start; evidence is required to complete increment 1's fidelity deliverable. |
| R13 — Legacy-only export cannot prove new UUID/three-name fields | IMPLEMENTATION CONSTRAINT | Baseline has zero UUID values and no friendlyName field. Add a separate fixed synthetic contract specimen and real import/export proof for new carriers, using literal test values rather than allocating real architecture IDs or deriving names. | No; an explicit increment-1 deliverable alongside the real full baseline. |
| R14 — Property rekeying, text/language loss and navigation identity loss | IMPLEMENTATION CONSTRAINT | Inspected Archi regenerates definition IDs, omits organisation item IDs, selects languages and normalises Property text. Exact unique key/value equivalence can preserve UUIDs; loss/conflicting keys/malformed IDs remains visible. Folder paths cannot recover persistent folder continuity or define qualified names. | No; characterize and disclose in increment 1. Acceptability of any actual semantic loss requires later architectural review. |
| R15 — Native/Harmonia type evolution and Feature semantics | IMPLEMENTATION CONSTRAINT | UUID stays fixed on either type change. Existing Feature → Capability plus `feature` Property is adequate. Current retained-native type/adoption checks cannot act as an unrestricted active reclassification policy. Reuse mapping evidence and keep unknown/incompatible pairs as findings, without new subtypes. | No; preflight covers known compatibility; actual semantic acceptance is reviewed in increment 2. |
| R16 — Relationship/View/occurrence identity and structure | LATER ROUND-TRIP CONCERN | Current relationship/View/node/connection IDs and reference rules are available; no lifetime Property is present on these objects. Keep the roles and parallel/repeated appearances distinct. Additional canonicalisation, replacement-View lineage and ambiguous folder lifecycle are not implied by the element rule. | No; retain/test transfer IDs now, handle deltas in increment 2 and any additional identity policy only by adjudication. |
| R17 — Compatible curated presentation and conflicts | LATER ROUND-TRIP CONCERN | Current build regenerates folders/layouts and validates exact generated Views; no accepted-state replay hook exists. Required snapshot/approval-manifest replay in increment 3 retains compatible View subtrees and conflicts safely. The mapping's disposable-View wording needs future governed reconciliation. | No; preserve raw snapshots now. Required before accepted curated-state republication. |
| R18 — Deterministic release verification and source/hash pins | IMPLEMENTATION CONSTRAINT | Current renderer has literal 1.0.6 prose/current-root release gates; 14-output verifier and correspondence pin outputs/tools/evidence. Future accepted implementation receives a patch and deliberately refreshed new-release evidence; replay 1.0.6 at its pinned revision. Current checks need not be weakened or historical evidence relabelled. | No; account for this when accepting increment 1, using section 10's release integration. |
| R19 — Directory migration, measurements and scalability | NON-ISSUE / ALREADY COVERED | Existing paths suffice. One semantic model, bounded contextual Views and optional publication metrics remain appropriate; timing stays outside deterministic bytes. New approved-state artifacts need separation from unreviewed ingest, not an immediate directory move. | No; retain sections 11–12 and defer physical migration. |

No row authorises Codex to choose a missing naming grammar or instance binding.
The EC alias/new-UUID treatment and qualified-path semantic mapping are already
decided; their future execution is an implementation constraint. After Mark and
ChatGPT review these findings, **explicit authorisation of the section-10 diagnostic
scope is sufficient to proceed directly to increment 1**; no further broad
architectural programme or directory move is needed. R2/R7/R8/R10/R11 can remain
recorded findings during that increment, but must be resolved for the affected
allocation, derivation, alias changes and conformance claims. No identified issue
is classified BLOCKER for the bounded diagnostic scope. Full-model service readiness
and completion of real export fidelity remain separate from readiness to start.
This assessment ends here; increment 1 is not executed.
