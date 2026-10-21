<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Identity and Authority Continuity dependency analysis

Date: 2026-10-20. Scope: Dependency Class 2 in the [current-to-historical processing analysis](2026-10-20-current-to-historical-processing-dependency-analysis.md), including the relevant reclassifications in the [completed Class 1 analysis](2026-10-20-architecture-reconstruction-dependency-analysis.md). Status: analysis for the Solution Architect; no architectural decision, implementation proposal, migration instruction or disposal authority.

## 1. Executive summary

**Harmonia needs historical facts to establish identity and authority continuity. It has no intrinsic requirement to obtain those facts from the existing historical processing directories. Its current machinery nevertheless has live dependencies on those directories.**

The substantive backward-looking questions are:

1. Does the same architectural instance still own its originally established Canonical ID, including retained historical instances, and have additions avoided existing identities?
2. Where an instance changes name, native type or Exchange identifier, is it demonstrably the same instance, with an authorised change rather than an unrecorded replacement?
3. Is the accepted resource capture still the exact capture established under the recorded instructions and authority, rather than a silently substituted allocation or decision?
4. Does the governed result incorporate exactly the accepted changes, preserve held matters and protected relationships/Views, and descend from the identified predecessor?
5. Has a governance source remained at its accepted version, or moved through specifically recognised changes while preserving the original authority boundary?
6. Do the adopted legacy bindings and source assertions still correspond to the explicitly identified original records, without promoting unresolved legacy material into current authority?

Several reads establish narrower facts: equality with a duplicated capture, restoration of an old release Property for comparison, or integrity of every file in a frozen processing population. These are not all independent architectural continuity obligations. A saved hash proves correspondence to pinned bytes; it does not establish who authorised those bytes or whether the content was architecturally accepted.

The inspected identity populations are concrete:

| Boundary | Fact retained or compared |
| --- | --- |
| RAD-1.0.7 establishment | 1,299 resource-owned Canonical IDs: 946 captured catalogue elements, 214 historical-reference elements and 139 Motivation elements. The two maintained captures are byte-identical to their post-establishment historical copies. |
| Accepted 1.0.8 ingestion | Nine additional element identities, 17 accepted relationships and four View captures. Maintained and execution-era accepted-source files are byte-identical. |
| Initial RAD-1.0.10 IA population | 54 identities, initially 50 BusinessObjects and four DataObjects. Naming completion must preserve their allocations, bindings, meanings and population. |
| RAD-1.0.10 → RAD-1.1.0 | All 1,362 predecessor Canonical IDs survive; 38 move to different Exchange identifiers. Accepted source contains 38 evolutions, 12 renames and 32 additions. Final population is 1,394 identities. |
| Older EC adoption | Fourteen exact EC-definition-to-Exchange bindings remain a bounded legacy contract. EC strings and legacy structural `canonical_id` values are not the resource-owned UUID32 `canonicalId`. |

The approved three-file provenance contract is conceptually sufficient for the meaningful **governed transition** facts, with the identified governed baseline and maintained source/governance providing unchanged context. It is not a complete record of intermediate candidates, original transport experiments, every frozen input, or every historical machinery execution. The distinction matters particularly for the IA allocation-before-naming-completion snapshot, whose exact intermediate bytes were not found in ordinary Git history at its maintained source path.

No blanket **REDUNDANT CARRIER** conclusion is made for a directory, manifest or evidence population. Narrow duplicate content is identified below. No disposal follows from that finding.

## 2. Authority, inspected state and method

Authority was loaded from [root instructions](../../AGENTS.md), [documentation instructions](../../docs/AGENTS.md), the [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md), [Architecture Change Provenance](../../docs/markdown/governance/architecture-change-provenance.md), the [release policy](../../docs/markdown/governance/architecture-release-policy.md), [Canonical Identity and Governed Naming](../../docs/markdown/governance/canonical-identity-and-naming.md), the [baseline source contract](../../docs/markdown/governance/baseline/README.md), the maintained [RAD-1.1.0 promotion decision](../../docs/markdown/governance/baseline/release-1.1.0.json), and the bounded [EC adoption decision](../../docs/markdown/governance/architecture-identity/adoption-1.0.3.md). The older [identity model](../../docs/markdown/governance/architecture-identity/model.md) was read in its qualified historical role. The current canonical contract expressly supersedes its earlier lifetime-handle/UUID-URN recommendations for the new identity Properties while preserving frozen registers and provenance.

AX-04 separates architectural meaning from machinery; AX-08/09 separate meaningful evidence from execution and temporary state; AX-14 preserves identity, representation, source, integrity and authority distinctions; AX-15/17 preserve uncertainty and require actual architectural authority. These support analysing current checks without treating their implementation as a statement that every operand must be preserved permanently. No architectural change is proposed.

The supplied [provenance design report](2026-10-20-architecture-provenance-and-evidence-design.md) and the two starting analyses supplied investigation leads. The installed provenance contract, maintained source and actual consumers supplied the governing interpretation and implementation evidence.

| Inspected item | State |
| --- | --- |
| Git HEAD | `8f3244b1af1481885b578f3749b35ebf0ffdc6a6` |
| Root release | `version=1.1.1`, `previous_version=1.1.0` |
| Applicable published architecture | `docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml` |
| Published model SHA-256 | `4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1` |
| Working tree | Existing governance-installation edits and supplied reports were present. They were preserved. |

Method: inspected maintained call sites, exact manifest selectors, historical decision/source operands and tests; performed read-only JSON/XML, gzip, byte-equality and Git-history inspections using standard-library scripts. No publisher, reconciliation renderer, repository loader, bootstrap, historical executable, framework test suite or architecture generator was invoked. The required JVM architecture-suite result is recorded in section 13; that suite does not establish ArchiMate continuity.

These are source-derived assertions and observations of retained records, not a claim that every current CLI path passes. The exact RAD-1.1.0 publication branch requires root authority `1.1.0 / 1.0.10`; initial-IA applicability also has exact-release guards. The RAD-1.1.1 versus published RAD-1.1.0 applicability issue remains outside scope. No conclusion about a failed authority pin is converted into a finding that an authorised governance installation lacks authority.

### Classification conventions

The final buckets classify **assertions**. Carrier classifications classify a carrier's contribution to a specified assertion. They are not interchangeable:

- **ESSENTIAL TO THE ASSERTION:** original/prior-state or transition information is an intrinsic operand. This does not make its present directory, compression or filename essential.
- **INCIDENTAL CARRIER:** the assertion is valid, but the historical processing file is an implementation location or support representation of the required fact.
- **REDUNDANT CARRIER:** for the explicitly bounded fact, another retained authoritative source already supplies the fact/proof and the copy supplies no unique evidence. This does not extend to its historical placement, establishment event, surrounding manifest or every other use.
- **UNCLEAR:** authority or evidence is insufficient to establish the stronger claim safely.

Thus a historical fact can belong in Bucket A while a duplicate file carrying that fact is incidental or redundant. A prior-state comparison can also belong in Bucket C when it protects only a processing experiment. Temporal comparison alone does not make something architectural evidence.

Provenance ratings assess the approved contract **conceptually**, not the existence or adequacy of a package today. **FULLY** permits the identified governed baseline, unchanged maintained dependencies and ordinary Git history in their proper roles; the delta archives are not full-state backups. **PARTIALLY** identifies an additional execution/intermediate-state fact that the contract does not preserve. **NO** means the assertion as written needs excluded processing history. **NOT APPLICABLE** denotes a current-state or machinery-only property rather than a governed architectural transition. No assertion was rated adequate by imagining an extra provenance artefact.

## 3. Dependencies examined and Class 1 inheritance

All paths below are repository-relative. Exact prefixes:

| Alias | Prefix |
| --- | --- |
| `B` | `docs/markdown/governance/baseline` |
| `F` | `docs/archimate/framework` |
| `P` | `docs/archimate/processing/publishing` |
| `I` | `docs/archimate/processing/ingestion` |
| `M` | `docs/archimate/processing/machinery-validation/information-architecture` |
| `X` | `docs/archimate/exchange` |
| `L` | `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft` |
| `D` | `docs/markdown/governance/architecture-identity` |

`Q` below denotes `I/1.0.10/run-002/reconciliation/execution-001`. Exact historical aliases are resolved through `F/historical-locations.json` and `workspace_paths.historical_path`; an old locator is not necessarily a processing file after resolution.

| Starting record | Current consumer and exact historical read contract | Assertions below |
| --- | --- | --- |
| B03 | `publish_information_release.inputs` reads every `B/release-1.1.0.json.inputs.*.path`; expands `Q/plan.json.authority.{decision,review}` and `.inputs.*`; validates `Q/artifacts/source-manifest.json`. Reviewed files include `I/1.0.10/run-002/change-review.md`, `Q/decision.json`, `Q/reviewed-instructions.md`, plan and establishment record. | C03–C05, C14 |
| B08 | `governed_metadata.load/archive` resolves `B/architecture.json.governedMetadata.sourceHistory` to `P/1.0.7/identity-establishment-1.0.7`; checks its manifest and all 15 `files_sha256` members, then builds the old owner map from `architecture.json` and `motivation.json`. | C01–C02, C14 |
| B09 | `governed_metadata.release/historical_bytes/remove_properties` reads that archive's `prior-release/architecture-release.json`. | C13 |
| B10 | `reconcile_108.read` reads `B/correspondence.json.accepted_ingestion.source_history`, namely `I/1.0.8/run-001/reconciliation/execution-001/accepted-source.json`. | C06 |
| B11 | Same reader checks all five `B/accepted-ingestion-1.0.8.json.evidence_sha256` entries: two submissions, two `change-review-before.md` files, and source-era naming governance. For the latter it reads `P/1.0.9/home-context-completion/before.json` and its selected naming snapshot. | C07 |
| B12 | `information_population.preserved_authority`, called on baseline authority mismatch, reads `P/1.0.10/information-population/canonical-name-completion/manifest.json.governed_authority_amendments` and the requested `before/<basename>.gz`. Only the six allowlisted authority sources qualify. | C08 |
| B13 | `governance_history.follows`, through `F/governance-transitions.json`, reads all three registered manifests and the requested source's matching before snapshots. Twelve rows across the manifests; not twelve unconditional snapshot reads per call. | C08, C14 |
| B14 | `information_population.load` reads `P/1.0.10/information-population/allocation/{manifest.json,prior-release.json,information-architecture.json.gz}` and `canonical-name-completion/{manifest.json,information-architecture.json.gz}`; uses governed `X/publish/1.0.9` as originating model. | C02, C09, C13–C14 |
| Class 1: B01 pins and rebinding provenance | Same release publisher: historical candidate, scope/decision/instruction/establishment bindings and `M/run-002/representation-rebindings.json`. Followed through actual correlation and protected-content checks, not only pin comparison. | C03–C05, C14 |
| Class 1: B05 pinned boundary and exact capture-to-ID establishment | `synchronise_baseline.load_inputs/Baseline/validate`: `B/correspondence.json.pinned_inputs`, recovered XML/crosswalk, source-manifest/findings/release hashes. Crosswalk's 946 `elements[*]` capture-key bindings and retained semantic IDs are relevant; View routing is excluded. | C10–C11, C13–C14 |
| Class 1: B06 normal validation | `synchronise_baseline.load_inputs → recover_v02.load_inputs`; all 75 recovery-manifest members, legacy model and identity lock; separate maintained-semantic-capture versus `L/model.json` equality check. Recovery-loader return values are discarded by this synchronisation caller. | C10–C11, C14 |
| Class 1: B07 historical-source verification | Synchronisation checks captured historical Motivation assertions and source hashes against four pinned legacy TeX files. Direct historical adoption/identity checks in `scripts/architecture_identity.py` were also followed. | C11–C12, C15 |

The four exact B07 TeX carriers are `docs/latex-legacy/diagrams/fig-motivation-map.tex`, `chapters/01-motivation-strategy.tex`, `chapters/01-motivation-strategy-old.tex` and `chapters/appendix-requirements.tex`. The `-old` file is a pin operand, not an additional selected typed Motivation source.

Tests are maintained consumers where they exercise these same paths: `test_governed_metadata`, `test_information_release`, `test_information_population`, `test_information_naming`, `test_reconciled_ingestion`, baseline/Home/round-trip source-context tests, `test_recovery`, and `scripts/tests/test_architecture_identity.py`. Their synthetic failure cases establish the intended contract in source; their real-history setup does not create independent architectural authority. No fixture redesign or execution replay was undertaken.

B04's exact `reconciled.xml` replay expectation is described only to distinguish it from actual identity correlation. B15's broad preservation population, presentation selection, transport specimens, saved regression results and optional historical audits are not reopened as Class 2 work.

## 4. Current call/check chains

### 4.1 Established-source validation and fallback publication

```text
synchronise_baseline.load_inputs
  → correspondence.pinned_inputs → recovered XML / crosswalk / manifest / findings / release / legacy pins
  → correspondence.current_authority_sha256
      → live hash equality, otherwise information_population.preserved_authority
          → original governance snapshot + explicit completion amendment
          → governance_history.follows if necessary
      → otherwise governance_history.follows directly
  → recover_v02.load_inputs
      → frozen 75-file boundary
      → model ↔ identity-lock fields
      → retained EC registry ↔ exact documentation-map bindings
  → maintained architecture.json, minus only governed metadata fields
      ↔ L/model.json [whole semantic-capture equality]
  → maintained motivation.json ↔ capture hash + recorded source assertions
  → Baseline uses recovered XML and crosswalk for retained representation bindings

governed_metadata.load(Baseline)
  → sourceHistory + correspondence-pinned establishment manifest
  → 15 member hashes + accepted 1.0.6 publication hash
  → old descriptors → representation-ID-to-original-canonicalId map
  → validate_records(current index, previous map)
      → every old resource remains; its Canonical ID is unchanged
      → current IDs valid and unique; expected names agree
  → validate_publication / overlay transport exactly those source-owned values

reconcile_108.read / extend_index
  → accepted source + execution-era copy equal the maintained correspondence pin
  → five evidence pins, including source-era naming bytes
  → current descriptors; collision check and combined identity validation

information_population.load
  → allocation source / prior release integrity
  → completed source integrity and exact completed-copy equality
  → current source == complete_names(original allocation source)
  → original 1.0.9 model pin; 54 resource IDs/types/definitions validated
```

The fallback's retained-element, relationship and View signature checks then protect content at `synchronise_baseline.validate`, lines 743–763. This is not a fresh TeX reconstruction on each call. `recover_v02.load_inputs` validates the old recovery inputs; normal synchronisation consumes the already recovered XML separately.

### 4.2 Accepted Information Architecture reconciliation and promotion

```text
publish_information_release.inputs
  → maintained release decision: promotion standing, predecessor, accepted/held CRs
  → pinned accepted capture, candidate, export, current review, plan, establishment,
    rebinding record and exact historical reconciled result
  → establishment binds source bytes ↔ plan bytes ↔ instruction pins ↔ release authority
  → plan authority files are hash-checked
  → baseline/control/candidate/export/original inputs are pinned and parsed
  → reconciliation.information.validate_scope
      → canonical correlation baseline ↔ candidate; no deletion
      → exact approved additions and existing-instance baseline bindings
      → protected elements, retained relationships and unauthorised View changes checked
      → accepted and held CR populations equal the maintained promotion decision

publish_information_release.publish [exact release authority required]
  → reconciliation.information.render from persisted accepted resource fields
      → source CR/operation/representation bindings; every existing Canonical ID retained
      → all resulting identities valid; authored semantics preserved
      → held records and content outside the accepted amendments preserved
      → accepted relationship/View captures preserved; authored Views/navigation exact
      → no allocation call
  → exact historical reconciled.xml equality [replay expectation]
  → only release/standing metadata changed
  → canonical correlation baseline ↔ published result; no identity lost
  → candidate Views exact; held DataObjects unchanged
```

Important caller distinction: the publisher calls `inputs` and `validate_scope`; it does **not** call `reconciliation.information.load_plan`. The latter, used by the explicitly invoked reconciliation CLI, additionally parses the decision's accepted CR set and checks instruction revision 2 / Accept markers for each approved CR. The publisher instead pins those authority files and trusts the maintained promotion decision and bound plan. It must not be credited with re-adjudicating the review text.

### 4.3 Explicit governance history and older EC checks

```text
governance_history.follows(relative, old hash, live hash, maintained register)
  → verify hashes of all registered manifests
  → for each matching source row, verify decompressed before bytes
  → follow one unambiguous before_sha256 → after_sha256 edge at each step
  → reject missing/ambiguous chain or cycle

scripts/architecture_identity.py validate
  → current registry ↔ explicit --previous-registry OR git show HEAD:<registry path>
  → append-only events / immutable bindings / retained aliases and lifecycle constraints

scripts/architecture_identity.py adoption / bindings [bounded legacy capability]
  → maintained adoption-baseline-1.0.2 + adoption-1.0.3 decision
  → exact documentation map and 14 EC native definitions / locked Exchange bindings
  → historical semantic/visual/preservation fingerprints and deterministic replay
```

`recover_v02.load_inputs` calls neither `verify_adoption` nor `verify_bindings`. It uses the registry's schema, namespace and exact 14 retained/locked binding map, with the registry bytes already covered by the recovery boundary. The stronger adoption/replay checks belong to the explicit script/tests. This prevents attributing their whole historical replay population to ordinary recovery-derived source validation.

### 4.4 Consumer source references

Line numbers identify the inspected working source; the links open the maintained file.

| Maintained source | Relevant checks |
| --- | --- |
| [synchronise_baseline.py](../../docs/archimate/framework/tools/synchronise_baseline.py) | `load_inputs` 167–208; crosswalk ownership 231–232; retained content validation 743–763 |
| [governed_metadata.py](../../docs/archimate/framework/tools/governed_metadata.py) | fallback guard 52–58; semantic capture 61–69; historical release 72–92; descriptors 198–252; owner/uniqueness checks 255–274; history loader 293–311; publication validation 337–369; once-only bootstrap 445–494 |
| [publish_information_release.py](../../docs/archimate/framework/tools/publish_information_release.py) | pin/input/authority chain 32–59; accepted metadata overlay 82–93; scoped promotion and canonical correlation 96–162 |
| [reconciliation/information.py](../../docs/archimate/framework/tools/reconciliation/information.py) | scope 64–130; allocation admission/capture 133–176; no-allocation replay 179–263; explicit instruction checks 266–289; source establishment 292–297 |
| [element_correlation.py](../../docs/archimate/framework/tools/element_correlation.py) | `canonical_index` and `correlate`; canonical precedence, claim conflicts, rebinding and unresolved outcomes |
| [reconcile_108.py](../../docs/archimate/framework/tools/reconcile_108.py) | source-era evidence/capture integrity 21–44; combined identity descriptors 47–67 |
| [information_population.py](../../docs/archimate/framework/tools/information_population.py) | authority allowlist/transition 42–85; permitted naming transform 102–112; allocation/completion history and source validation 115–158; addition collision check 194–203 |
| [governance_history.py](../../docs/archimate/framework/tools/governance_history.py) and [transition register](../../docs/archimate/framework/governance-transitions.json) | full `follows` implementation and exact three registered manifests |
| [recover_v02.py](../../docs/archimate/framework/tools/recover_v02.py) | complete boundary/identity lock/retained binding validation 89–125 |
| [architecture_identity.py](../../scripts/architecture_identity.py) | Git prior registry 54–65; registry history 207–249; bounded adoption/bindings 252–396; inventory's legacy lock/history reads 531–537 |


## 5. Assertion catalogue: facts, significance and minimum operands

### C01 — Established Canonical ID ownership persists

**Proposition:** Every resource present in the 1.0.7 establishment still exists in the indexed source and retains its originally assigned `canonicalId` under the established representation binding. A lost identity, changed value or physical removal cannot silently become a new allocation.

Consumer: `governed_metadata.load`, lines 293–311, and `validate_records`, lines 255–274, through metadata validation/overlay and dependent tests. Historical material: the two establishment captures, their manifest, and the accepted predecessor publication pin (B08).

This protects identity persistence and retained non-reuse evidence. Minimum facts are the original resource-to-ID association, its established source/representation binding and standing, and the corresponding current association. The comparison is not name matching. `previous` is a representation-ID-keyed map; the separately pinned recovery crosswalk and unchanged capture establish its source-record ownership context.

**Limit:** this is a comparison against the establishment population, not an exhaustive proof that no ID was ever used by any unrecorded or physically removed instance. The governed non-reuse rule is broader than any one snapshot. The validator rejects removal rather than proving an undocumented deletion safe.

### C02 — The current identity population is valid and collisions are absent

**Proposition:** Each indexed current element owns one syntactically valid UUID32 `canonicalId`, no two indexed owners share it, and published metadata matches the owning source. Initial IA additions must not collide with existing representation IDs, Canonical IDs or governed names.

Consumers: `governed_metadata.read/validate_records/validate_publication`, `reconcile_108.extend_index`, `information_population.load/project`, and `element_correlation.canonical_index`. The historical loaders are on the surrounding path, but uniqueness and source-to-publication consistency themselves use the selected present population. `information_population.project` compares additions to the originating model's existing population.

Minimum facts: the complete applicable owner population, values/claims, valid syntax, source-to-model bindings and, for additions, the existing model population. These protect uniqueness and faithful projection. A valid UUID32 is not proof that Python's random allocator was executed exactly once; manifests and allocation declarations record that event, while persisted values and no-allocation replay preserve its result. Publication source and tests explicitly forbid allocation on ordinary replay.

### C03 — The accepted source and result remain bound to the authorised scope

**Proposition:** The maintained accepted IA capture is the source established for the exact reviewed plan/instructions under the originating 1.0.10 authority, and promotion accepts exactly CR-001–154 while holding CR-155–158. Reconciliation acceptance alone does not authorise a release; the separate promotion decision does.

Consumers: `publish_information_release.inputs`, lines 38–59; `reconciliation.information.validate_establishment`, lines 292–297; explicit CLI `load_plan`, lines 266–289; renderer scope/CR checks. Historical carriers are B03's review, decision, instructions, plan, candidate/export and source-manifest pins.

This protects authorisation attribution, scope integrity, source establishment and release lineage. Minimum facts: attributable Solution Architect decision and date, exact proposed content and permitted derivations, originating/resulting governed-state identities, accepted/held CRs, exclusions/conditions, and persisted resulting source identities. File hashes bind operands to those decisions; they do not supply the decision.

The historical decision explicitly says reconciliation only, no RAD release; `B/release-1.1.0.json` supplies the separate release promotion. The machinery's reported unpublished standing in the accepted source remains distinguishable from acceptance of its later publication.

### C04 — Canonical identity survives authorised retyping, renaming and Exchange rebinding

**Proposition:** Every predecessor architectural instance remains identifiable by its Canonical ID after the accepted change. The 38 evolved instances retain their Canonical IDs despite different Exchange identifiers; 12 renames retain their representation IDs; the 32 additions use the already persisted new identities.

Consumers: `reconciliation.information.validate_scope/render`, `element_correlation.correlate`, `publish_information_release.publish`, lines 119–130, and `test_information_release`. The original rebinding JSON is additionally hash-pinned and referenced in emitted provenance; it is not what `correlate` uses to discover or validate the correspondence.

Minimum facts: complete before/after canonical ownership maps, each relevant before/after Exchange binding, the authorised semantic operation and its CR, and the accepted addition map. This protects continuity while permitting approved semantic evolution. Names/types/folders are excluded from correlation keys. Shared unique valid Canonical IDs establish correspondence; a representation-ID fallback is allowed only when neither side claims canonical identity. Contradictory/lost/duplicate claims yield uncertainty rather than guessed matching.

**Concrete example:** Canonical ID `2c884cdd6a4b41a184818e3a7e540897` persists from `Organisational Unit BO`, native BusinessObject, Exchange `id-bf12d24b62bb595eaa2325a6a7bea26e`, to `[Topic] OrganisationalUnit`, native DataObject, Exchange `id-0235e0eaeafd42b683a53bacd2e40939` (CR-051). The accepted capture contains both associations. Identity persistence does not imply unchanged classification or meaning; authorisation of that change is C03.

### C05 — Retained relationships, Views and excluded content are not silently repurposed

**Proposition:** Retained relationship identifiers keep their protected typed assertions/endpoints; protected View identifiers keep their authorised content/references; held instances remain unchanged; changes outside accepted scope do not appear as a side effect of metadata reconciliation or promotion.

Consumers: `reconciliation.information.validate_scope`, lines 110–122, `render`, lines 225–254; `publish_information_release.publish`, lines 109–118 and 131–139; older `synchronise_baseline.validate`, lines 743–763. Historical operands are the identified baseline/control/candidate and recovered original XML, with scope and accepted relationship/View captures.

Minimum facts: the prior identifier and typed assertion/endpoints for each protected relationship; prior View identity/content/reference association; exact approved amendments and exclusions; resulting association/content. Scope allows 60 new relationships and 12 reviewed View changes in the 1.1.0 reconciliation. Existing relationships are checked against control content; unauthorised View changes are rejected; final authored Views are preserved from the accepted candidate.

These are native representation identity and scope-integrity checks, not Canonical ID allocation to relationships or Views. The identity portion is in scope here; choices about layout, routing, View generation and presentation configuration are not analysed or redesigned.

### C06 — The maintained 1.0.8 accepted capture is the exact established capture

**Proposition:** `B/accepted-ingestion-1.0.8.json` still has the accepted source hash recorded in correspondence, and the execution-era source copy has that same hash.

Consumer: `reconcile_108.read`, lines 34–44 (B10). Minimum facts are the accepted capture identity/hash and current source bytes. This protects against silent alteration of the nine allocated identities, accepted authored XML, scope and evidence associations. Current and historical copies are byte-identical.

The maintained correspondence pin independently detects current drift. Checking an additional identical file supplies no additional allocation or authorisation fact for this equality assertion. Historical source location is not itself approval. Proof that this was the accepted capture still needs the authority/transition context in C07; two equal files cannot establish that context.

### C07 — Accepted 1.0.8 records retain their original input and decision context

**Proposition actually checked:** The recorded submission/review/naming operands are still exactly the operands whose hashes the accepted capture records. **Governed significance:** the accepted records should remain traceable to the submitted content, issued decisions and applicable naming authority, without silently interpreting old acceptance through today's different policy.

Consumer: `reconcile_108.read/evidence_bytes`, lines 21–44 (B11). It hashes the five operands; it does not parse those reviews' dispositions, re-derive authorisation or prove the current capture from the submissions.

Minimum architectural facts are the original accepted changes and identities, originating/resulting states, the attributable material instructions/dispositions and qualifications, and the applicable source-era naming decision. Exact incoming transport bytes and every proposal in the submitted model are additional operands of this implementation, not automatically facts required for authorisation.

Authority limitation: the pinned run-002 `change-review-before.md` has Accept entries in its summary table while its detailed CR bodies retain On-Hold and pending-execution wording. The loader proves that this mixed historical document is unchanged, not which part constitutes final authority. Maintained correspondence/capture and later governed release decisions explicitly record acceptance, but a faithful consolidated account of the original decision revisions cannot be inferred from byte equality alone (section 12).

### C08 — Live governance has a specifically recognised relationship to the original authority

**Proposition actually checked:** The old pinned authority bytes remain retrievable, and the live governance file either matches the specifically recorded amended version or is reachable through a unique registered before-hash/after-hash chain. An arbitrary live edit cannot be accepted by refreshing the old pin.

Consumers: `synchronise_baseline.load_inputs`, lines 173–184; `information_population.preserved_authority`, lines 66–85; `governance_history.follows` (B12–B13). Completion allowlist: `canonical-identity-and-naming.md`, `architecture-release-policy.md`, `architecture-modelling-strategy.md`, `archimate-ingestion-publication-semantics.md`, `rad-archimate-model-mapping.md`, `rad-archimate-mapping-catalogue.md`, all under `docs/markdown/governance/`.

Minimum continuity facts are the applicable original authority identity/content, explicitly recognised resulting authority identity/content, the material authorised amendment and its scope/actor/conditions, and the path between them. The current chain implementation validates manifest pins, original snapshots and hash edges. It reads all three registered manifests even when some have no row for the requested source; only matching rows trigger snapshot reads. It rejects cycles and non-unique next edges.

**Authority limit:** manifests have `standing` claims and hashes, but `follows` does not inspect authorisation, decision text, actor/date or semantic permitted scope. It does not verify every intermediate after-file's content. Registration and the maintained pinned chain are its trust boundary. Architectural authorisation is a distinct governed fact, not a theorem supplied by the hash graph. The authority-continuity intention is meaningful; whether every registered transition has sufficient attributable historical authorisation is not established by this function.

### C09 — IA naming completion preserves the original allocations and all non-allowed content

**Proposition:** The maintained 54-resource initial IA capture is exactly the original allocated source with the expressly permitted naming completion: unchanged Canonical IDs, representation bindings, population, definitions and other metadata; completed `canonicalName`/`metadata_stops` and the declared top-level naming/adjudication fields only.

Consumer: `information_population.load`, lines 115–158, and `complete_names`, lines 102–112 (B14). It checks original/completed source hashes, exact completed-copy equality, and `data == complete_names(original)`. The manifest's `allowed_resource_changes` is descriptive; the implemented transform/equality enforces the permitted delta. The prior release snapshot is hash-checked, not semantically parsed by `load`.

This protects identity and semantic continuity across naming completion, rather than merely matching today's names to today's derivation. Minimum facts: the authorised 54 original allocations and definitions/bindings, naming-completion permission and exclusions, and resulting source. The code also checks native types, local derivation, governed source sections and the pinned originating 1.0.9 model.

Boundary qualification: completion occurred within the same unsigned candidate, not a separately established RAD transition. The full original incomplete capture is an intermediate state. Its exact prior bytes, unresolved naming-stop fields and execution sequence are more than the final governed transition's identity facts. Do not manufacture a governed before-state or label allocation → naming completion a new RAD release.

### C10 — The adopted semantic capture has not drifted from its explicitly chosen predecessor

**Proposition:** After removing only the canonical-establishment contract's added fields, the maintained architecture capture is exactly the reconciled v0.2 source selected for the baseline; the Motivation capture retains its correspondence hash and source-qualified records.

Consumer: `synchronise_baseline.load_inputs`, lines 187–207; `governed_metadata.semantic_capture`, lines 61–69 (inherited B06/B07). The comparison includes the whole source object: 946 records, 1,096 relationship assertions, standing, currentness, unresolved matters and source qualifications. The historical model is an equality operand, not preferred current source.

Minimum facts: identified adopted predecessor capture and authorised adoption boundary, exact semantic content/qualifications, the explicit metadata-only difference, and the current capture. This protects truthful adoption, semantic continuity and unresolved standing. The source identity and authorised adoption cannot be inferred merely because a current file equals a historical file. Current architecture content is already present; the additional proposition is non-drift from that identified adoption.

### C11 — Legacy record and representation bindings have not been silently reassigned

**Proposition:** Each frozen legacy record retains its recorded reference/capture/structural identity fields; the recovered representation retains its exact capture-key-to-Exchange binding; the 14 explicitly retained EC bindings agree with the fixed documentation map and retained/locked registry entries.

Consumers: `recover_v02.load_inputs`, lines 89–125; `synchronise_baseline.load_inputs/Baseline`, lines 167–208 and 231–232; retained-element/relationship validation. Operands: `L/model.json`, `L/identity-lock.json`, recovery crosswalk/XML and manifest-selected framework lock/map and `D/registry.json`.

Minimum facts: qualified record keys/reference IDs, the established legacy aliases/structural values, exact native representation bindings and their retained/adopted standing, and the current use of those bindings. Recovery checks lock fields `key`, `latex_label`, `local_id`, `canonical_id`, `id_aliases`; it validates the registry schema/namespace and exact retained/locked EC map. These checks protect provenance and non-guessing of identity correspondence. They do not turn a structural ID into UUID32 canonical identity or resolve the other 214 legacy objects' absent RAD crosswalks.

### C12 — Captured historical assertions really occur in their recorded original source

**Proposition:** A historical Motivation element/relationship attributed to a specific original source is supported by that source's recorded exact bytes and literal assertion, with valid captured endpoints. It is not a newly invented assertion retrospectively attributed to the legacy document.

Consumer: `synchronise_baseline.load_inputs`, lines 193–207, plus correspondence's TeX pins (inherited B07). Minimum facts: original source identity/version and relevant assertion, captured source locator/type/endpoints/standing, and the current captured assertion. Both source hash and assertion presence are checked. The original source's historical standing remains explicit.

This protects truthful source attribution. It does not establish current architectural approval of the historical sentence. The capture already includes the selected assertion/documentation; the comparison establishes its provenance. Full TeX embedded in historical evidence Views is a different use, outside this continuity assertion.

### C13 — Historical release context is correctly restored for comparison

**Proposition:** When the identity metadata overlay is removed for a historical content comparison, the model's release Property is restored to the original publication release rather than today's root release. The IA allocation's prior-release snapshot also remains at its pinned bytes.

Consumers: `governed_metadata.release/historical_bytes/remove_properties` (B09), initial-IA prior snapshot integrity (B14), and B05's recovery release hash. Minimum facts are the comparison boundary's original release/version and, where actually required, original release record bytes.

The predecessor label matters to interpreting a historical comparison. This mechanism is not independently proving that today's release was authorised or genuinely descended from its predecessor. The root release record remains the current authority; an old copied release JSON is not a competing allocator. Normalisation and a frozen recovery-release hash belong with historical comparison/recovery machinery, not as an additional identity/authority continuity obligation.

### C14 — Frozen processing operands retain their pinned bytes

**Proposition:** Every file enumerated by a chosen historical pin population still matches the saved digest, even if it is not parsed for identity, authority or architecture.

Consumers/populations: B08's 15 establishment members; inherited B06's 75 recovery members; B05's pin-only findings/manifest/release/legacy files; B03/B01 input pins; B13's unrelated registered manifests. The 15 include two post-establishment captures plus 13 prior-release source/release/Markdown/discrepancy files. The 75 include frozen tools and reports as well as actual source/identity inputs. These loops do not execute archived code.

Minimum facts for this exact assertion are the selected member inventory, historical digest and actual member bytes. It protects file/population integrity. Its architecture-relevant subset contributes to C01/C03/C08/C10/C11/C12; the remainder does not acquire an architectural property through co-membership. A missing unrelated Markdown/report/tool byte can stop a loader without establishing an architectural identity or authority failure.

The release publisher's exact `reconciled.xml` equality is likewise a replay-result assertion. Its meaningful identity and scope properties are independently expressed by C03–C05. The original rebinding JSON is pinned and emitted as a provenance reference; correlation instead uses the actual before/after canonical maps.

### C15 — The bounded EC adoption retained existing objects under the recorded decision

**Proposition:** The authorised 1.0.3 event adopted exactly EC-01–EC-14 from the identified earlier governed definitions into the frozen register, kept their existing locked Exchange objects, allocated no new identities and changed no protected semantic/visual content. Later bounded binding validation preserves the adopted semantic association and registry.

Consumers: `scripts/architecture_identity.py.verify_adoption/verify_bindings`, explicit CLI actions and corresponding tests (inherited B07). The adoption check compares the empty earlier register, exact event/decision fingerprint, source definition locators and bindings, original semantic/visual hashes and preserved file pins. `verify_bindings` additionally compares semantic/Documentation content and the register's navigation-baseline hash. Its replay/determinism/census components are separable historical checks.

Minimum architectural facts: adopted definition IDs, original representation bindings and definitions, attributable bounded adoption decision, prior/resulting registry state and protected-content exclusions. No historical processing directory is intrinsic: the decision, baselines, registry, exact mapping and governed publication already live in maintained/governed locations, with Git providing ordinary source history.

The separate `validate_registry(previous)` path enforces retained records/events, immutable established bindings/assertions, aliases, retirement and decision-kind constraints using an explicit prior register or `git show HEAD`. It demonstrates an existing use of ordinary history for a bounded validation purpose. Synthetic tests include relationship-record lifecycle rules; the actual frozen adoption population is 14 Capabilities, so those tests are not evidence of allocated Canonical IDs for today's native relationships or Views.

The `inventory` read of `L/identity-lock.json` and `reviewed-identity-history.json` only reports their presence/shape and explicitly labels them legacy publication history, not RAD allocation authority. That inventory read asserts no identity continuity: it belongs in Bucket C, with provenance **NOT APPLICABLE**. Recovery's actual lock-field comparison remains C11.

## 6. Where the necessary facts currently exist

This is a fact-location catalogue, not a search-hit list or proposed storage design. Selectors identify complete consumed populations. A generated report or manifest can attest an observation; it does not independently confer authority. Git availability below refers only to inspected local history, not an assurance about an external recovery service.

| Assertion | Maintained/governed locations | Historical carriers and other retained locations |
| --- | --- | --- |
| C01–C02 | `B/architecture.json.elements[*]` and `.historical_reference_elements[*]`, `B/motivation.json.elements[*]`; canonical contract; combined metadata descriptors and later accepted captures. `X/publish/1.0.7` contains all 1,299 Canonical ID owners. Later `X/publish/{1.0.8,1.0.9,1.0.10,1.1.0}` retain/evolve the populations. | Establishment `architecture.json`, `motivation.json`, `manifest.json`; `B/correspondence.json.canonical_identity_establishment` pins the event boundary; establishment reports/validation attest allocation counts. The maintained post-establishment captures also exist at their normal paths in Git HEAD. |
| C03 | `B/release-1.1.0.json` records actor/date, accepted and held CRs, scope exclusions, originating/promoted releases and all input pins. `B/accepted-ingestion-1.0.10.json` records authority, 82 elements/60 relationships/12 Views/four held records, allocation count and previous associations. Both are present in Git HEAD. | `Q/{decision.json,reviewed-instructions.md,plan.json,artifacts/source-manifest.json}`, current `I/1.0.10/run-002/change-review.md`, candidate/export and `Q/artifacts/reconciled.xml`. The decision and instructions contain detailed original permission and no-release scope; the source manifest binds exact plan/source/authority hashes. |
| C04 | Governed `X/publish/1.0.10` and `X/publish/1.1.0` supply both complete owner maps; `B/accepted-ingestion-1.0.10.json.elements[*].previous` records all 50 existing-element prior associations, including 38 rebindings; current fields give new associations. | `M/run-002/representation-rebindings.json` has 38 detailed correspondences with original input locators/hashes/line numbers; original governed submission `X/ingest/1.0.10/harmonia-architecture-1.0.10.xml` and candidate retain incoming associations; `P/1.1.0/identity-provenance.json` repeats evolved/new associations. The JSON's line-number/input annotations exceed the minimum identity map. |
| C05 | Governed before/after XMLs retain relationship IDs/types/endpoints and View identities/content; `B/accepted-ingestion-1.0.10.json.relationships/views/held` and `B/release-1.1.0.json` record accepted scope/exclusions. Older accepted relationship/View captures are in `B/accepted-ingestion-1.0.8.json`; baseline relationship bindings are also rendered in `B/relationships.md`. | Candidate/control and reconciled XML; recovered XML and `P/1.0.5/representation-crosswalk.json.relationships[*]`; plan scope and original instructions. No separate relationship/View canonical-allocation history exists in these carriers. |
| C06 | `B/accepted-ingestion-1.0.8.json`; correspondence's `.accepted_ingestion.{source_sha256,authority,source_history}`; accepted capture and correspondence in Git HEAD. | `I/1.0.8/run-001/reconciliation/execution-001/accepted-source.json` is byte-identical to the maintained capture. |
| C07 | Accepted 1.0.8 capture contains originating/resulting release, nine element identities/authored XML, 17 relationship captures, four Views and five evidence hashes; correspondence records acceptance attribution; current canonical contract contains the Product qualifier decision. `X/publish/1.0.8` and `1.0.9` retain governed architectural states. | `I/1.0.8/run-001/detection/submission.xml`; `I/1.0.8/run-002/detection/submission.xml.gz`; both runs' `reconciliation/execution-001/change-review-before.md`; Home-completion `before.json` and selected source-era naming snapshot. Current run reviews/execution records and release policy provide later decision/result context, but the loader hashes the five original operands, not a consolidated authorisation account. |
| C08 | Live governance paths; `B/correspondence.json.current_authority_sha256`; `F/governance-transitions.json`; current canonical/naming decisions and provenance/release governance. Git records maintained governance/register history. | Completion `manifest.json.governed_authority_amendments` and six selected `before/*.gz`; `M/run-001/governance/manifest.json`, `M/run-002/governance/manifest.json`, `M/run-002/governance/contained-leaf-correction/manifest.json` and each matching `files[relative].snapshot`. These are exact transition edges/original bytes, not independent human-approval records. |
| C09 | `B/information-architecture.json` contains all 54 allocations/bindings/definitions and completed naming; current canonical contract's information-object vocabulary and release policy contain the completion permission; originating model path/hash in that capture resolves to governed `X/publish/1.0.9`. Completed source exists in Git HEAD. | Allocation manifest/original source/prior release; completion manifest/completed-source snapshot. Completed snapshot is byte-identical to maintained source. The exact original incomplete source was not found in any of the one source-path Git revisions inspected. It exists in the allocation snapshot, which itself is tracked in HEAD. |
| C10 | `B/architecture.json`, `B/motivation.json`, correspondence and source-classification records; source status/qualifications also rendered in baseline Markdown and governed releases. | `L/model.json`; pre-establishment `prior-release/{architecture.json,motivation.json}`; recovery model/crosswalk. Exact original pre-establishment captures also exist at maintained paths in commit `d060702d6055bc42ef4e0b64917703636f466faa`. |
| C11 | `F/documentation-map.json`, `F/baseline/{semantic-lock.json,Harmonia-Capability-Architecture-v2.1a.xml}`, `D/{registry.json,registry.schema.json,adoption-1.0.3.md}`, captured legacy fields in `B/architecture.json`, rendered catalogue/relationship/Motivation tables, governed original/latest XMLs. | `L/{model.json,identity-lock.json,reviewed-identity-history.json}` (last is inspected by inventory, not by recovery's lock validator); recovery manifest/crosswalk/XML; explicit older adoption baselines. Non-EC recovery bindings are preserved as representation correspondence, not inferred RAD identity. |
| C12 | `B/motivation.json.elements[*].{source,source_sha256,source_assertion,documentation}` and `.relationships[*].{source,source_sha256,assertion,from,to}`, plus correspondence pins; governed releases preserve historical evidence documentation. | Four exact legacy TeX files. The whole-file old source is independently checked; the copy of an excerpt in today's capture alone cannot establish that the original source contained it. Original source/capture files are also retained through local Git. |
| C13 | Establishment metadata says 1.0.7; correspondence and governed `X/publish/1.0.6` identify the predecessor. IA capture identifies originating 1.0.9. Root release remains current authority only. | Establishment `prior-release/architecture-release.json`; recovery `architecture-release.json`; IA `allocation/prior-release.json`. These supply original literal records/metadata to old comparisons. |
| C14 | Maintained pins/registers/selection code; ordinary Git history of machinery/configuration. Actual governed source/decision facts are catalogued in the other rows. | Every member of the explicitly selected 15/75/pin populations. Hash-only tools, reports, old Markdown and unused authority manifests add no architectural meaning just because their absence blocks a loop. |
| C15 | `D/{registry.json,adoption-1.0.3.md,adoption-baseline-1.0.2.json,adoption-validation.json,bindings-validation.json}`; `F/documentation-map.json` and baseline locks/navigation baseline; canonical EC definition sections; governed `X/publish/1.0.4`; registry's Git HEAD/explicit prior state. | Manifest-selected old-location aliases resolve to maintained framework and governed release files. Legacy lock/history inventory does not grant allocation authority. Historical replay/preservation expected hashes remain in the adoption/navigation baselines. |

These locations do not all have the same standing. Maintained source establishes current identities/content; applicable governed releases establish exact represented states; an attributable decision establishes permission; an immutable prior binding establishes the comparison boundary; manifests and saved reports identify/check their operands. A duplicate source file has no independent authorship merely because it was frozen.

## 7. Historical necessity and three-file provenance assessment

| Assertion | Carrier classification and historical necessity | Approved provenance relationship |
| --- | --- | --- |
| C01 | **ESSENTIAL TO THE ASSERTION:** an independently identified original allocation/owner association is needed. **REDUNDANT CARRIER, narrowly:** the two byte-identical post-establishment source copies add no unique owner-map content beyond maintained source plus the governed 1.0.7 publication. The event pin/manifest and predecessor evidence are separate roles, not declared redundant. | **FULLY** for allocation ownership and preservation across a governed transition: changed captures/model, attributable review and identified baseline establish the old/new map. No need to retain every pre-establishment generated Markdown file as identity evidence. |
| C02 | **INCIDENTAL CARRIER:** current validity/uniqueness uses the current complete population; additions require an existing authoritative population, which is already governed. History is needed for non-reuse across time, not to repeat syntax/duplicate checks. | **NOT APPLICABLE** for standalone present-state validity; **FULLY** for collision-free, authorised additions against an identified governed predecessor. |
| C03 | **ESSENTIAL TO THE ASSERTION:** original permission, exact source establishment and state lineage are historical decision/transition facts. **INCIDENTAL CARRIER:** separate plan/decision/instruction/source-manifest files are the current binding mechanism; not every machinery input is an authority fact. | **FULLY** for meaningful scope/authority/lineage under a compliant review and exact before/after changed content. **PARTIALLY** for the existing input-by-input machinery contract, which also demands proposals/control/replay operands outside governed delta evidence. |
| C04 | **ESSENTIAL TO THE ASSERTION:** prior and subsequent owner/binding maps are required. **INCIDENTAL CARRIER:** the rebinding JSON is not unique proof of those maps; governed states and maintained `previous` records retain them. Its input-line annotations are extra provenance, not necessary identity facts. | **FULLY** for governed rebinding/evolution and persisted additions. It does not prove an unobserved random-number-generator invocation or original transport execution. |
| C05 | **ESSENTIAL TO THE ASSERTION:** prior relationship/View association and protected-content scope are comparison operands. Candidate/control bytes used to isolate transport or prove exact serialization have additional fixture/replay roles. | **FULLY** for governed identity/content continuity and authorised exceptions. **PARTIALLY** for exact preservation relative to an intermediate candidate and actual no-edit control; those execution facts are not necessarily governed states or changed content. |
| C06 | **REDUNDANT CARRIER**, only for the execution-era duplicate's equality role: maintained accepted bytes and the maintained correspondence hash already establish the same non-drift. The pinned acceptance association is **ESSENTIAL TO THE ASSERTION**; its authority is not supplied by this duplicate. | **FULLY** for identifying/preserving the established accepted capture across its governed transition; ordinary Git also retains its file history. This does not retrospectively authorise the capture. |
| C07 | **ESSENTIAL TO THE ASSERTION:** applicable original decision/authority and source-state association are historical facts. **INCIDENTAL CARRIER:** complete submitted exports and hash-only review snapshots are the current mechanism. **UNCLEAR** for reconstructing fully attributable final decisions from these particular mixed-status historical reviews alone. | **FULLY** conceptually for the material authorisation, source/result identity and applicable authority facts. **PARTIALLY** for the check as implemented: exact incoming/review snapshot bytes and actual transport are not guaranteed delta-archive members. Existing authorisation ambiguity remains **UNCLEAR** for historical reconstruction, not a defect inferred in the approved contract. |
| C08 | **ESSENTIAL TO THE ASSERTION:** original authority, recognised successor and authorised amendment span states. **INCIDENTAL CARRIER:** run-local transition manifests/snapshots and all-manifest enumeration are the implementation. **UNCLEAR** whether the hash register alone establishes human authorisation; it does not inspect that fact. | **FULLY** for an authorised governed governance amendment: actual changed governance files before/after and reviewed attributable permission. **PARTIALLY** for historical machinery-only clarifications/corrections where no separate governed transition existed: Git and maintained governance have roles; a run is not made a RAD transition by its manifest. |
| C09 | **ESSENTIAL TO THE ASSERTION:** prior allocation-to-instance association is required for the continuity claim. **INCIDENTAL CARRIER:** completed-source duplicate. The original incomplete snapshot remains a distinct intermediate operand of the existing exact transform check; its required architectural event/standing cannot be reduced to today's equal IDs alone. | **FULLY** for the authorised resulting 54 allocations, their owner associations and final naming in the actual governed transition. **PARTIALLY** for exact allocation-state → completion replay inside one unsigned candidate. The missing operand is the original incomplete source, including then-unresolved metadata; it is not the governed 1.0.9 before-state or final 1.0.10 after-state. No extra provenance artefact is inferred. |
| C10 | **ESSENTIAL TO THE ASSERTION:** exact adopted predecessor content and adoption permission matter to non-drift. Particular legacy location is **INCIDENTAL CARRIER**; equivalent pre-establishment captures also exist at maintained paths in inspected Git. | **FULLY** for a governed adoption/content-preservation transition with identified baseline and changed-source bytes. Original unchanged dependencies are interpreted from that baseline/Git, not copied wholesale into delta archives. |
| C11 | **ESSENTIAL TO THE ASSERTION:** retained original binding/legacy identity history is needed to exclude reassignment. The recovery crosswalk/lock's historical processing location is **INCIDENTAL CARRIER**; equivalent governed bindings and maintained maps/records already exist for the inspected associations. | **FULLY** for meaningful governed binding adoption/continuity. Exact recovery execution remains separate. No proof is invented for the 214 unresolved source crosswalks. |
| C12 | **ESSENTIAL TO THE ASSERTION:** independent original-source content is necessary to prove the claimed attribution. The current capture's repetition of its own quotation is not independent provenance. | **FULLY** for truthful source/adoption facts using changed capture, attributable review and identified original source available through governed baseline/Git where appropriate. The archives need not include an unchanged whole TeX corpus. Hash-only checking of every unselected original file is C14. |
| C13 | **INCIDENTAL CARRIER:** the prior release label/record is a historical comparison parameter already identified elsewhere; byte-perfect old-record preservation is a recovery assertion. No additional architectural identity or authorisation proposition is established. | **NOT APPLICABLE** to comparison normalisation; **FULLY** for actual release predecessor/successor identities when relevant to C03/C08. |
| C14 | **INCIDENTAL CARRIER** for architecture purposes. Literal frozen-member integrity inherently needs those bytes, but that fact does not make the entire population architecture evidence. No unique architectural property was established for unrelated members. | **NO** for preserving/reproducing every frozen processing input, intermediate output and exact machinery result. **NOT APPLICABLE** as an architectural transition requirement. Machinery source history has its normal Git role. |
| C15 | **ESSENTIAL TO THE ASSERTION:** original adoption/bindings and prior registry state. Existing maintained/governed carriers already retain these; no processing directory is intrinsically required. Legacy inventory reads are **INCIDENTAL CARRIER** to current identity meaning. | **FULLY** for the bounded authorised adoption and protected identities. **PARTIALLY** for the composite historical check's repeated execution/census/byte reproduction assertions. Registry comparison against Git is ordinary history validation, not authorisation supplied by a commit. |

An archive of changed files is not sufficient in isolation to establish every unchanged identity in a large model: the contract expressly relies on its identified governed baseline and retrievable dependencies. Conversely, a package need not contain an unchanged framework, old control export or entire recovery population to establish an authorised change in identity and governance. This analysis neither creates packages nor declares any existing carrier disposable.

### Facts outside the approved transition contract

The examined architectural continuity assertions reveal no intrinsic need for a fourth provenance component. The following **additional facts demanded by existing mechanisms** are not assured by governed before/after content and a review:

| Extra fact | Why it is outside that contract; present availability |
| --- | --- |
| The exact incomplete IA allocation source before naming completion within the same unsigned candidate | It is an intermediate state, not the originating governed 1.0.9 source (where this source is absent) or resulting completed source. Only one maintained-source Git revision was found, with no byte match. The allocation `.gz` exists and is itself tracked. This is a concrete limit on assuming normal source-path Git recovery, not a new retention design. |
| Exact original submissions, actual no-edit control and candidate serialization | They establish what was submitted/exported or used in a processing comparison, not simply the authorised governed delta. Actual governed states cannot prove an independent transport execution. Relevant carriers remain, with fixture/replay purpose outside this task. |
| Every run manifest, frozen machinery input, saved intermediate result and replay byte | These prove execution/population integrity. Machinery Git history records code changes but does not by itself attest that an execution occurred with those bytes. The provenance contract deliberately excludes exhaustive execution history. |
| Attributable final authority absent or inconsistent in a historical record | No combination of equal files, hash edges, a commit or new generated report supplies missing Solution Architect authorisation. Existing accepted source/decisions may supply material authority; unresolved details must remain explicit. A compliant review can record established facts, not invent them retrospectively. |

No required architectural fact was shown to be intrinsically inexpressible using governed before/after state, an attributable review, maintained governance/source and ordinary Git in their defined roles. That is not a finding that a complete such record has already been assembled. In particular, Git can currently retrieve the tracked IA allocation snapshot **at its archive path**; the failed source-path search does not establish that those bytes are absent from Git altogether or that deleting a working carrier would satisfy today's loader.

## 8. Canonical-identity findings

1. **Identity persistence is asserted.** The establishment comparison retains owner/value pairs; accepted IA evolution is correlated by canonical ownership across native retyping and Exchange rebinding. A rename or new classifier does not entail a new instance.
2. **Uniqueness is asserted within complete selected populations.** Duplicate claims, malformed/multiple claims and source/model owner discrepancies fail. Historical copies are not inherently needed to perform present-population uniqueness checks.
3. **Non-reuse has bounded enforcement.** Retained historical resources keep IDs; establishment-history comparison refuses physical removal; additions are checked against current/existing claims. There is no demonstrated all-time consumed-ID proof across every hypothetical removed record. Do not claim that a 1,299-entry snapshot alone proves universal historical non-reuse.
4. **Representation-ID rebinding is explicitly recognised.** Thirty-eight accepted changes retain canonical identity while changing Exchange identifiers. The old rebinding JSON is a pinned record, but active correlation uses state maps. `B/accepted-ingestion-1.0.10.json` already retains explicit `previous` associations and CR bindings.
5. **Relationship identity continuity is asserted at the native representation/typed-assertion level.** Existing relationship identifiers, content and endpoints are protected; accepted new edges are enumerated. No `canonicalId` is allocated to native relationships by these consumers.
6. **View identity continuity is asserted through protected identifiers, signatures, content and references.** Final authored Views must match the reviewed candidate. This does not establish a separate canonical identity scheme for Views or justify retaining every presentation-generation input as identity evidence.
7. **Allocation authority is explicit and scoped.** Bootstrap is once-only; original establishment refuses rerun; authorised reconciliation may allocate only approved additions with missing metadata; replay/publication allocates zero. Historical manifests describe mechanism/counts; neither UUID syntax nor file equality independently proves the actor's permission or exact execution count.
8. **Canonical identity does not certify semantic equivalence.** Canonical correlation identifies the instance; accepted scope and typed/authored content checks govern its evolution. Retyping is not silently forbidden by the identity rule, nor accepted merely because the same Canonical ID remains.
9. **Legacy identity roles remain qualified.** The fixed 14 EC strings, source/capture keys, TeX labels and structural `canonical_id` survive as their existing provenance/binding roles. They cannot replace `canonicalId` or resolve unproven crosswalks by lexical similarity.

## 9. Authority findings

| What machinery establishes | What it does not establish by that check alone |
| --- | --- |
| A file still matches a pin associated with a maintained accepted record | Who authorised its architectural contents; whether every statement is current/accepted |
| Accepted source, plan and instruction pins are mutually bound | Solution Architect approval invented by the existence of a plan or source-manifest |
| A live governance hash has a unique path through registered edges | Human authorship/approval of each edge, or semantic permitted scope; `follows` does not read those facts |
| A canonical value and its owner association survive | Approval of a semantic reclassification, correctness of the definition, or all-time non-reuse |
| A result preserves held records/protected content and has the recorded predecessor | Completion of final visual inspection, release acceptance inferred from PASS, or relevance of the root release to every old adapter |
| Git retains file history and supplies a prior register | Architectural acceptance merely because a commit exists; recovery of bytes never committed at their source path |
| A reviewed attributable decision authorises bounded changes and derivations | An instruction to retain every tool, control, snapshot or execution file that helped implement it |

The meaningful authority facts are the attributable decision, exact bounded content/scope, applicable authority/state, conditions/exclusions and governed result. The installed three-file contract explicitly accommodates these. Its review records material decision revisions and distinguishes original authorisation from later completion hashes/outcomes. Requiring all previous processing documents to interpret the final permission is not an intrinsic authority requirement.

There is a specific distinction between **authority unchanged** and **authority legitimately changed**. Direct pins protect the former. Registered edges support the latter only within their declared trust boundary. Neither justifies replacing a pin with the current digest whenever governance changes. Equally, a missing recognised hash path does not disprove separately established Solution Architect authority.

## 10. Final Bucket A/B/C reduction

The reduction applies to decomposed facts, so a composite consumer can appear in more than one bucket without contradiction.

| Bucket | Actual assertions | Starting dependencies accounted for |
| --- | --- | --- |
| **A. Continuity genuinely requires historical comparison** | C01 original ID ownership/persistence and retained non-reuse associations; C03 exact authorised transition/source establishment/lineage; C04 canonical continuity and rebinding; C05 protected relationship/View association and excluded-scope continuity; C07 original decision/source-era authority association; C08 recognised governed authority succession; C09 preservation of prior allocations through permitted completion; C10 adopted-source non-drift; C11 original legacy bindings; C12 truthful attribution to original source; C15 bounded EC adoption. | Meaningful portions of B03, B08, B11–B14 and inherited B01/B05–B07. Prior-state/event facts are necessary; whole processing populations and current directory names are not. |
| **B. Valid assertion; historical processing is only the current carrier/mechanism** | C02 present validity/uniqueness and faithful source projection; C06 non-drift of an established accepted capture already pinned in maintained correspondence; duplicate source copies for C01/C09; rebinding record as a carrier of maps also present in governed states/maintained `previous`; current governance/identity facts obtained through incidental processing wrappers. | B08 duplicate captures, B10, B14 completed copy/current-population checks; inherited B01 rebinding carrier and B05 maintained binding facts. Validity of the assertion does not require that these checks survive in their current form. |
| **C. Not an architectural identity/authority property** | C13 release-Property restoration and old-record integrity for historical comparison; C14 unrelated all-member frozen-byte integrity; exact `reconciled.xml` replay result; exact intermediate transform execution beyond C09's identity/authorised-content fact; C15 historical deterministic-generation/census/whole-file replay components. | B09; hash-only portions of B03/B08/B12/B13/B14 and inherited B01/B05–B07. They belong to comparison normalisation, fixture/recovery or execution assurance. No solution for those other classes is proposed. |

Bucket A is not permission to preserve every historical mechanism. Bucket B is not a conclusion that historical comparisons never matter. Bucket C is not a deletion list. The completed Class 1 conclusion remains intact: no missing current architectural information was identified; Class 2 adds temporal and authority relationships between facts already represented, and some original decision/boundary evidence.

## 11. Smallest set of continuity facts that genuinely need preservation

The minimum is a set of **facts**, not a proposed registry, manifest, file format or implementation:

1. **Original and continuing instance ownership:** which architectural instance was assigned each canonical identity, including retained historical/non-current instances; subsequent lifecycle/replacement decisions where applicable; sufficient prior associations to prevent silent reassignment or reuse. Existing frozen EC/source aliases retain their separately qualified binding roles.
2. **Governed representation correspondence:** which before/after element, native relationship or View representation belongs to the retained instance/assertion/View; exact old/new identifiers where a binding changes; protected relationship type/endpoints and View references/content to the extent required by authorised scope.
3. **The attributable permission and its limits:** Solution Architect actor/date, originating authority/state, accepted instructions/CRs and material revisions, authorised allocations/retypes/rebindings, held/rejected matters, conditions and exclusions. Acceptance, publication, human inspection and validation outcomes remain distinct.
4. **Exact governed transition boundaries and realised content:** identified originating/resulting states and release lineage, actual changed content and its correspondence/absence, persisted resulting IDs, and enough protected before/after content to establish that only the authorised change occurred. Unchanged context remains identified and retrievable; a full processing trace is not one of these facts.
5. **Truthful source/adoption and authority succession:** which original source/binding/authority version was adopted or used, what assertion it actually supported, its standing/qualifications, and which specifically authorised amendment established any successor. Preserve unresolved or unestablished associations as such.

Current syntax, duplicate-ID detection and source/model equality need applicable complete current populations; they do not add a separate history-retention category. Runtime command order, all old log bytes, whole frozen framework copies, arbitrary run boundaries and layout-generation selections are not part of this minimum. The exceptional incomplete IA snapshot contains a distinct intermediate proof operand, but the exact old stop-fields/execution sequence are not automatically required durable architecture facts. Whether its stronger intermediate guarantee remains required is an adjudication question, not a proposed implementation choice here.

## 12. Uncertainties requiring Solution Architect adjudication

| Matter | Precisely unresolved fact or obligation |
| --- | --- |
| Original authority versus registered hash edges | `governance_history.follows` establishes correspondence to registered edges, not attributable permission. Which material original decisions support those edges must remain distinguishable from their `standing` text and pin integrity. No missing approval is inferred or fabricated. |
| Historical 1.0.8 decision revisions | The pinned run-002 review snapshot has summary Accept versus detailed On-Hold/pending wording. Later maintained source/correspondence record acceptance. Which material decisions/revisions form the complete attributable authorisation account must be established before any future reconstruction claims final consolidated authority. Current frozen records remain untouched. |
| Intermediate IA allocation guarantee | The existing exact original→completion transform check spans stages within one unsigned candidate. Does a continuing architectural obligation require proving that precise intermediate boundary, or only preserving the originally authorised identity assignments and final approved content? Its original incomplete source is not recoverable from the inspected maintained-source-path history, though the tracked allocation archive retains it. No choice is made here. |
| Breadth of non-reuse assurance | Governance requires non-reuse, while inspected enforcement retains known populations and refuses deletion. Any claim covering identities absent from all retained states/events would require evidence not supplied by these checks. This report does not assert such absent identities existed. |
| Composite preservation obligations | Which old all-file/visual/replay predicates remain separately required engineering assurances is outside the architectural minimum. Their current existence does not settle their future purpose, and their narrower purpose does not authorise disposal. |
| Recorded versus later inspection status | The maintained promotion record still says final published-release visual inspection pending. No hash/identity check proves a later human inspection occurred. Any later attestation needs its own attributable established fact; this analysis neither reopens acceptance nor resolves historical handoff status. |

These are bounded uncertainty findings. They do not reopen the approved provenance contract, propose identity allocation machinery, or adjudicate release applicability.

## 13. Verification and task boundary

| Inspection/check | Outcome and limit |
| --- | --- |
| Maintained consumer and test-source inspection | Completed for all starting Class 2 records and Class 1 reclassified portions above. Static caller conditions were preserved; no claim that guarded CLI branches execute successfully today. |
| Read-only source/copy equality | Confirmed both post-establishment captures and the 1.0.8 accepted-source pair are byte-identical. This establishes duplicate content, not duplicate authority. |
| Read-only XML ownership comparison | Confirmed 1.0.7 has all 1,299 captured Canonical IDs; all 946 core and 214 historical-reference representation bindings agree with the recorded map, and all 139 Motivation values are present. Confirmed 1,362 predecessor IDs survive into the 1,394-element result, with exactly 38 rebindings; all accepted capture current/previous bindings agree with the respective publications. |
| Read-only transition inspection | All three manifest hashes match their maintained register; all 12 decompressed before snapshots match their declared before hashes. This is integrity evidence, not a new acceptance/authorisation conclusion. |
| Bounded ordinary Git inspection | Confirmed maintained captures, accepted source, transition register, legacy model and allocation snapshot are present in HEAD. Found exact pre-establishment source bytes at `d060702d6055bc42ef4e0b64917703636f466faa`. No exact original incomplete IA source in the one maintained-source-path revision inspected. No global claim about every historical carrier's Git recoverability. |
| Required JVM architecture suite | **PASS**: `timeout --signal=TERM --kill-after=10s 600s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`. BUILD SUCCESS; 90 tests, zero failures/errors/skips; all 36 reactor modules successful; Maven elapsed 19.002 seconds; finished 2026-10-20 15:52:10 Australia/Sydney. It verifies application/JVM invariants, not the historical-proof contracts analysed here. |

Only this report is authored by this task. Existing working changes were preserved. No architecture, governance, release, framework, loader, validator, test, canonical identity or evidence was modified; no historical content was copied/moved/deleted, no architecture regenerated, and no evidence package or identity manifest/registry created. The express analysis-only instruction excludes a release increment for this task. JVM verification produces only ordinary build/test outputs. No implementation, migration or cleanup recommendation follows.

## 14. Plain answers

**Does Harmonia need historical information to prove identity and authority continuity?**

**Yes.** Proving persistence, non-reassignment, authorised semantic/representation change, original source adoption and legitimate authority succession requires prior ownership/state and attributable transition facts. Today's architecture alone cannot establish what was previously owned, approved or changed. Present-state validity checks are a separate matter.

**Does Harmonia need the existing historical processing directories to provide that information?**

**No intrinsic architectural requirement was established for those directories.** The necessary facts concern governed states, original associations and authorised transitions, which the approved provenance model can express with the identified baseline, maintained source/governance and ordinary Git in their proper roles. **Current machinery still requires specific processing carriers**, some material decision/intermediate facts still reside there, and no package/migration equivalence or disposal eligibility is claimed. Historical information is necessary; the existing processing-directory mechanism is not thereby mandated. Work stops at this analysis and report.
