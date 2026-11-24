<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture Reconstruction dependency analysis

Date: 2026-10-20. Scope: Dependency Class 1 in the [current-to-historical dependency analysis](2026-10-20-current-to-historical-processing-dependency-analysis.md), against the current RAD-1.1.1 working repository. Status: read-only analysis for the Solution Architect; no architectural decision, migration authority or machinery change.

## Executive summary

**No architectural information required by the examined current consumers was found to exist only in historical processing material. Bucket B is empty.**

The four starting records, **B01 and B05–B07**, contain several different kinds of dependency:

- **B01 supplies a complete architectural model**, but the accepted architectural information already exists in the governed RAD-1.1.0 publication. The maintained accepted-ingestion capture also contains the bounded amendments. The publisher still renders from its pinned historical candidate rather than taking the resulting governed model as its complete input boundary.
- **B05 supplies an earlier complete tree and representation correspondence.** Its architectural records, assertions, definitions, standing and unresolved matters already exist in maintained RADS captures and catalogues; the governed publication retains their native representation. This is reconstruction through an older carrier, not discovery of otherwise missing current architecture.
- **B06 is principally recovery-boundary and continuity validation.** In the synchronisation call, the recovery loader's returned model is discarded. The separately read historical `model.json` is an equality operand: its entire semantic capture already exists in maintained `baseline/architecture.json`.
- **B07's direct legacy-source reads validate captured historical assertions or embed historical source documentation in Views.** Current Motivation is supplied by maintained RADS sections and `baseline/motivation.json`. The legacy files do not supply otherwise absent current Motivation definitions. Optional recovery and adoption checks have separate purposes.

The historical carrier is therefore **not semantically necessary for current architectural content** in any examined case. It remains operationally necessary to the existing loader, replay or validation contract in several cases. Those are different findings. This report does not establish operational independence or authorise removal of any carrier.

“Already present” includes maintained governed captures **and the applicable governed architecture representation**. It does not mean that `architecture.json` alone contains every element, View or accepted IA amendment, or that every accepted XML detail has a corresponding prose paragraph. Governed publication is an accepted representation under RADS authority, not an independent source of architectural authority.

## 1. Authority, scope and method

Authority was loaded from [root instructions](../../AGENTS.md), the [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md), [Architecture Change Provenance](../../docs/markdown/governance/architecture-change-provenance.md), the [release policy](../../docs/markdown/governance/architecture-release-policy.md), the [baseline source contract](../../docs/markdown/governance/baseline/README.md), and the [accepted RAD-1.1.0 promotion decision](../../docs/markdown/governance/baseline/release-1.1.0.json). The [publication index](../../docs/archimate/exchange/publish/README.md) identifies RAD-1.1.0 as the current governed model. The supplied [provenance design report](2026-10-20-architecture-provenance-and-evidence-design.md) and starting dependency report were navigation aids; their conclusions were checked against consumers and retained content.

AX-04 prevents a machinery input contract from defining architectural necessity. AX-08/09 distinguish architectural meaning from execution and recovery material. AX-14 preserves the distinctions between architectural content, identity proof, presentation and historical evidence. AX-17 prevents treating retained historical assertions as current authority or manufacturing a missing relationship. The provenance contract explicitly permits maintained governed releases as applicable inputs and requires separately authorised work to remove existing processing dependencies.

The investigation started with the four records assigned to Architecture Reconstruction in section 10 of the starting report. Adjacent records were followed only where necessary to terminate a chain or distinguish content from proof. Classes 2–5 were not independently inventoried or assessed for completeness.

Method:

1. Read maintained consumer implementations, their selectors, caller conditions and exact historical-location mappings.
2. Separate bytes merely hashed, source assertions merely verified, and objects actually used to construct architecture.
3. Compare original and maintained JSON data, XML resource populations, native types/endpoints, property values and documentation. Use resource keys and representation IDs, not names, to establish correspondence.
4. Follow the provenance of cached candidates/recovery models far enough to identify their ultimate architectural inputs; distinguish that provenance from calls actually executed by a current consumer.

Only ordinary read-only inspection scripts were executed, using Python `-B` and standard-library JSON/XML parsing. No repository loader, publisher, reconciliation renderer, generator, test suite or historical executable was run. No architecture or machinery-validation output was generated. Comparisons below are observations of retained content, not new publication or acceptance evidence.

### Inspected state

| Item | Observed state |
| --- | --- |
| Git HEAD | `8f3244b1af1481885b578f3749b35ebf0ffdc6a6` |
| Root release | `version=1.1.1`, `previous_version=1.1.0` |
| Applicable published architectural model | `docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml` |
| Published population | 1,394 elements; 1,137 relationships; 1,209 Views |
| Working tree | Existing governance installation changes and prior reports were present and preserved. This report is the only added file for this task. |

The release policy explicitly makes the 1.1.1 installation governance-only. `publish_information_release.active()` nevertheless requires exactly `1.1.0 / 1.0.10`; that branch is not active under today's root value. The fallback retains earlier authority gates. Consequently, the chains below describe maintained capabilities and their conditions, not a claim that all commands currently complete. The release/applicability issue is neither solved nor used to infer missing architectural information.

## 2. Starting dependencies examined

Paths in this report are repository-relative. These abbreviations are exact prefixes:

| Abbreviation | Prefix |
| --- | --- |
| `B` | `docs/markdown/governance/baseline` |
| `F` | `docs/archimate/framework` |
| `P` | `docs/archimate/processing/publishing` |
| `I` | `docs/archimate/processing/ingestion` |
| `M` | `docs/archimate/processing/machinery-validation/information-architecture` |
| `X` | `docs/archimate/exchange` |
| `L` | `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft` |

| Original record | Maintained consumer | Historical carrier or exact population | Reduced finding |
| --- | --- | --- | --- |
| B01 | `publish_information_release.inputs/publish`; `reconciliation.information.render`; conditional `synchronise_baseline.publish` fast path; `test_information_release` setup | `M/run-002/candidate.xml` | Architectural model: **SEMANTICALLY EQUIVALENT** maintained model; accepted amendments have **EXACT** resource captures. Authored presentation and replay assertions are separated below. |
| B05 | `synchronise_baseline.load_inputs`, `Baseline.__init__/build/markdown/validate`; `diagnose_roundtrip.baseline_context`; baseline/metadata tests | `P/1.0.5/Harmonia-Architecture-1.0.5A.xml`, `representation-crosswalk.json`; the exact `B/correspondence.json.pinned_inputs` population | Architectural records/assertions: **EXACT** maintained captures; native representation: **SEMANTICALLY EQUIVALENT** current publication. Several pin-only and presentation portions are reclassified. |
| B06 | `synchronise_baseline.load_inputs → recover_v02.load_inputs`; separately `synchronise_baseline`'s snapshot equality check; `RecoveryTests`; explicit recovery targets | `P/1.0.5/source-manifest.json.files_sha256`'s 75 members; `L/model.json`, `L/identity-lock.json`; manifest-selected baseline exchange and maintained lock/mapping/registry inputs | No unique architectural content. Semantic model: **EXACT** maintained equivalent. Normal transitive reads are validation; explicit recovery/test uses belong outside Class 1. |
| B07 | `synchronise_baseline.load_inputs/build`; `recover_v02.Recovery.evidence/description`; historical adoption/identity checks | Manifest-selected `L` corpus and four exact `docs/latex-legacy` files listed in section 5 | Captured Motivation information: **EXACT** maintained equivalent. Direct legacy reads supply source verification or historical documentation; reclassified accordingly. |

`workspace_paths.historical_path` resolves only exact registered paths. In particular, frozen `docs/archimate/capability-architecture/recovery-v0.2/...` locators resolve to `P/1.0.5/...`; the old baseline exchange locator resolves to `X/publish/1.0.4/Harmonia-Capability-Architecture.xml`. A historical-looking locator is not necessarily a processing carrier after resolution.

## 3. Call chains and chained reconstruction

### 3.1 Accepted RAD-1.1.0 publication branch — B01

```text
synchronise_baseline.publish [only when exact 1.1.0 promotion is active]
  → publish_information_release.publish
    → inputs ← maintained B/release-1.1.0.json
      → M/run-002/candidate.xml
      → maintained B/accepted-ingestion-1.0.10.json
      → historical plan / authority / establishment operands
      → governed 1.0.10 baseline and original submission; actual control/export
    → reconciliation.information.render(candidate, maintained accepted source, scope)
      → copy complete candidate tree
      → replace the 82 accepted element records from source.elements[*].xml
      → verify the 60 accepted relationships and 12 View records against source
      → derive/check names and containment; project Element/Relationship navigation
    → compare with historical reconciled.xml [exact replay expectation]
    → change model release / candidate-standing metadata
    → governed RAD-1.1.0 representation
```

Code: [publisher](../../docs/archimate/framework/tools/publish_information_release.py), `inputs` at line 37 and `publish` at line 106; [reconciler](../../docs/archimate/framework/tools/reconciliation/information.py), `render` at line 179. The renderer's complete starting tree is the candidate. The accepted capture is a bounded amendment, not a complete replacement model.

The candidate's provenance is also explicit:

```text
Run-002 candidate
  ← information_workspace_run.build
    ← information_naming_run.load_inputs ← M/specimen.json
      ← governed RAD-1.0.10 XML [baseline]
      ← X/ingest/1.0.10/harmonia-architecture-1.0.10.xml [architect submission]
      ← actual no-edit export [control]
    ← names derived on the incoming submission; workspace projection
    ← Run-001 checks [prior output expectations, not another architectural source]
```

[Run-002 construction](../../docs/archimate/framework/tools/information_workspace_run.py), lines 40–84, starts the model from the **incoming architect submission**, not from Run-001's candidate or an archived `architecture.json`. Run-001 is used to check behaviour/preservation. The publication adapter reads the cached candidate; it does not invoke that construction or regenerate the older baseline.

The ultimate Class 1 requirements are the unchanged baseline architecture plus the accepted submission amendments. Both are represented in the governed RAD-1.1.0 result. The original submission, control and pre-promotion bytes may still be required for separate authority, transport or replay checks; they are not missing architectural source.

### 3.2 Earlier baseline reconstruction and source-context validation — B05–B07

```text
Makefile baseline / baseline-check / independent output comparison
  → synchronise_baseline.publish [fallback branch]
    → load_inputs
      → B/correspondence.json → pins / source-era validation
      → recover_v02.load_inputs [validate entire recovery boundary; return discarded]
      → maintained B/architecture.json [actual semantic source]
      → L/model.json [must equal semantic source; not selected over it]
      → maintained B/motivation.json + selected current RADS sections
      → P/1.0.5 recovered XML + representation-crosswalk.json
    → Baseline copies recovered XML; binds maintained records to retained nodes
    → rebuild documentation from maintained source; preserve retained content
    → later maintained ingestion / Home / presentation / initial-IA layers

diagnose_roundtrip.baseline_context
  → Baseline(*synchronise_baseline.load_inputs()) [same input boundary]
```

Code: [synchronisation](../../docs/archimate/framework/tools/synchronise_baseline.py), lines 167–208, 211–236, 383–490 and 903–965; [source-context caller](../../docs/archimate/framework/tools/diagnose_roundtrip.py), lines 185–189. This is a genuine current machinery dependency on a historical complete tree. The fact that the semantic catalogue is separately loaded from maintained source is material.

The provenance of that earlier tree is:

```text
P/1.0.5/Harmonia-Architecture-1.0.5A.xml [cached recovery]
  ← recover_v02.publish / Recovery.build [explicit historical recovery operation]
    ← L/model.json [946 records; 1,096 relationship assertions]
    ← governed X/publish/1.0.4/Harmonia-Capability-Architecture.xml
       [228 elements; 189 relationships; 219 Views; 14 retained EC bindings]
    ← actual included L TeX corpus [historical evidence documentation]
    ← selected five-axiom appendix [snapshot Principle representations]
```

[Recovery code](../../docs/archimate/framework/tools/recover_v02.py), `load_inputs` at line 89, `Recovery.build` at line 590 and `publish` at line 696, establishes this chain. Normal synchronisation reads the cached recovery; **it does not call `recover_v02.publish` or `Recovery.build`**. Its separate call to `recover_v02.load_inputs()` validates the frozen boundary and discards the returned tuple. Calling this a fresh reconstruction from TeX on every publication would be incorrect.

At the leaf of the chain:

- `L/model.json` is exactly the semantic portion of maintained `B/architecture.json`.
- The 1.0.4 baseline is already a retained governed exchange representation outside processing. Its 14 EC documentation prefixes survive exactly in RAD-1.1.0; its other objects are retained with explicit historical-reference standing.
- Raw included TeX is historical source evidence, not a source of additional current semantic allocations.

### 3.3 Archived `architecture.json` and still-earlier states

The nearby metadata path is [governed_metadata.load](../../docs/archimate/framework/tools/governed_metadata.py), lines 293–311:

```text
current metadata validation
  → maintained B/architecture.json + B/motivation.json → current descriptors
  → sourceHistory / identity-establishment-1.0.7
    → manifest + archived architecture.json + motivation.json
      → descriptors → previous canonicalId ownership → compare current IDs
    → prior-release/* [manifest byte checks]
    → governed 1.0.6 XML [accepted-publication hash]
```

The two archived captures are **byte-identical to the maintained captures**. Their fields are used to construct a prior identity map for non-deletion/non-reallocation checking. The `prior-release/architecture.json` population is hashed, not replayed as an architectural source by this function. `release()` separately reads the archived predecessor release for historical comparison metadata. There is no hidden runtime chain that reconstructs the current catalogue by loading those prior-release JSON records.

This terminates the apparent “current machinery → historical architecture.json → earlier baseline” dependency: the backward edge proves continuity, while the architectural information is already maintained. It is a Class 2 boundary exclusion, not an additional Class 1 missing-source finding. The broader identity/authority contract is outside this analysis.

## 4. Exact architectural information consumed and maintained equivalents

The requested equivalence labels concern **architectural information**, not interchangeable pathnames or byte-compatible inputs to today's functions. `EXACT` identifies the same data/record; `SEMANTICALLY EQUIVALENT` identifies the same architectural assertions despite representation or governed metadata differences. None of the following content dependencies requires `PARTIAL`, `NONE` or `UNCLEAR` on the inspected evidence.

### 4.1 B01 — complete candidate content

| Information obtained from the historical candidate | Current use / reason for the historical read | Precise maintained equivalent | Classification and consequence |
| --- | --- | --- | --- |
| All 1,394 element subjects: native types, names, documentation, properties, existing canonical identities, BO/Topic specialisation and the four explicitly unresolved DataObjects | `render` copies the full candidate, then overlays only accepted element XML. The adapter is written as replay of that exact accepted proposal boundary. | [RAD-1.1.0 XML](../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml), `elements/element` by `identifier`; [accepted IA capture](../../docs/markdown/governance/baseline/accepted-ingestion-1.0.10.json), `elements[*]` by `representationId`/`canonicalId`, including full `xml`, `previous`, type and governed-name fields | **SEMANTICALLY EQUIVALENT** full model; **EXACT** accepted record content. The final publication has the same subjects and architectural assertions. The candidate is not their only carrier. |
| All 1,137 relationships: identifiers, native types, endpoints, names, documentation, direction and specialisation properties, including Composition and Aggregation | Preserve complete semantic graph; validate containment and accepted scope, without rebuilding untouched edges from the bounded capture | RAD-1.1.0 XML, `relationships/relationship`; IA capture `relationships[*].xml` contains the 60 accepted additions, 59 Compositions and one Aggregation | **EXACT** relationship subtrees, ignoring surrounding formatting whitespace. Every candidate relationship is present and unchanged. |
| The 82 changed IA elements: 32 added Topics, 38 BO→Topic evolutions and 12 BO name migrations | Candidate supplies authored semantics against which captured element replacement and name derivation are checked | IA capture `elements[*]`, including complete accepted XML; RAD-1.1.0 `elements/element` | **EXACT** accepted resource capture; **SEMANTICALLY EQUIVALENT** candidate-to-publication architectural content. Publication persists the 32 IDs already allocated in the capture. Their absence in the earlier candidate is not missing architecture. |
| CR-155–158's Context Topic, Pragma Topic, PetasosParcel Topic and Pylai.Diadromí Topic: native DataObject state without an inferred specialisation | Verify that held classifications were not changed | RAD-1.1.0 elements; `B/accepted-ingestion-1.0.10.json.held`; `B/release-1.1.0.json.held_crs` and scope exclusions | **EXACT** retained element content. Their unresolved classification is explicitly preserved in maintained architecture; a missing decision must not be mistaken for lost source information. |
| View membership/connections where they repeat architectural subjects and relationships | Carry the complete authored workbench and check references | RAD-1.1.0 `views/diagrams/view`; all 1,209 View subtrees match the candidate. The 11 new and one revised IA Views are also captured in `B/accepted-ingestion-1.0.10.json.views[*].xml`. | **EXACT** retained View content. Coordinates, style, folder organisation and exact View navigation are presentation, excluded from Class 1 below. A diagram reference does not create an additional semantic edge. |

Direct comparison found 1,312 element subtrees structurally identical and 82 with serialised differences. After removing **only** the `canonicalId` property from both sides, all 1,394 element subtrees match, preserving order of all remaining children. Of the 82 differences, 32 add a previously absent `canonicalId`; the other 50 have equal property values and differ in its position. All 1,137 relationship subtrees and all 1,209 View subtrees match without this adjustment. Model release/candidate-standing metadata and generated Element/Relationship navigation are outside this element comparison and are explicitly handled by the publisher.

Thus the complete accepted architectural content exists. The fact that the bounded JSON capture alone is not a complete model does not establish a missing maintained equivalent when the governed complete representation is present.

### 4.2 B05 — recovered architecture and representation crosswalk

| Information obtained from the recovery/carrier | Current use / reason for the historical read | Precise maintained equivalent | Classification and consequence |
| --- | --- | --- | --- |
| The 946 catalogue subjects: keys/reference IDs, construct type, Capability category/tier, parent and owner assertions, definitions, explanatory material, aliases, placement and governance standing, currentness, release scope, provenance and unresolved dimensions | `Baseline` starts with recovered nodes, binds maintained source records to them, rebuilds documentation and retains structured snapshot properties | [B/architecture.json](../../docs/markdown/governance/baseline/architecture.json), `elements[*]`, `placement_assertions`, `reviewed_bec_reconciliation`, `relationship_constraints` and other capture fields; the relevant maintained baseline catalogues; RAD-1.1.0 elements carrying `v0.2 / Source Capture Key` and `v0.2 / Snapshot Reference ID` | **EXACT** semantic capture. All 946 crosswalk keys bind to a current published element with the same source-key/reference properties and the canonicalId owned by the maintained record. Historical recovery supplies no additional catalogue assertion. |
| The 634 native source relationships and their source-assertion bindings: native mapping, identifiers, source/target and independent assertion standing | Retain graph edges; use `crosswalk.relationships[*].source_index` to label source assertions' native representations in generated Markdown | `B/architecture.json.relationships` and `relationship_currentness`; [B/relationships.md](../../docs/markdown/governance/baseline/relationships.md), native type/ID per source assertion; RAD-1.1.0 `relationships/relationship` | **EXACT** source assertions and maintained catalogue bindings; **SEMANTICALLY EQUIVALENT** current native representation with retained properties. All 634 IDs/types exist in the current publication and all IDs occur in the maintained relationship catalogue. No native-mapping information was found only in the crosswalk. |
| The 462 assertions deliberately not represented as native relationships: predicate, endpoints, status, currentness, qualifications and source excerpts | Preserve absence of unsupported edges and explain unresolved mapping, rather than silently filling the graph | `B/architecture.json.relationships`, `relationship_currentness` and `relationship_constraints`; metadata-only rows of `B/relationships.md`; version-qualified element properties in the governed representation | **EXACT** source assertions. Every `recovery-findings.json.deferred_relationships[*].source_assertion` equals the indexed maintained assertion. The findings file itself is pin-checked in the normal loader, not ingested to supply new architectural decisions. |
| Retained original EC-01–EC-14 documentation before the recovery appendix | `Baseline.build` explicitly takes the pre-recovery documentation prefix from those existing elements; this is a real architectural-text read from the recovered tree | RAD-1.1.0 documentation for the same 14 representation IDs retains every original 1.0.4 documentation prefix exactly; the originating governed 1.0.4 XML and 1.0.6 XML also retain it. The maintained `enterprise-capabilities.md` supplies the corresponding baseline descriptions. | **EXACT** original documentation prefixes in the applicable current governed representation. Literal identity was checked for all 14. This content is not uniquely dependent on the recovery XML even where JSON does not separately reproduce that prefix as a dedicated field. |
| The 214 unreconciled 1.0.4 Capability references and retained 189 original relationships, with their qualified historical standing | Preserve reference objects/edges without inventing a v0.2 crosswalk or treating them as a competing current catalogue | `B/architecture.json.historical_reference_elements` records the 214 resources and governed names/IDs; RAD-1.1.0 retains the full nodes/edges and historical qualification; `X/publish/1.0.4/Harmonia-Capability-Architecture.xml` retains the originating governed representation | **SEMANTICALLY EQUIVALENT** maintained historical architectural reference content. All 228 original elements, including the 14 EC bindings, and all 189 original relationship IDs remain in RAD-1.1.0. “No proven crosswalk” remains explicit; availability does not establish reconciliation. |
| Five snapshot Principle IDs and their existing nodes | Reuse retained Principle representation IDs while replacing documentation with the explicit maintained Motivation selection | `B/motivation.json.elements` with native axiom IDs; maintained axiom sections; RAD-1.1.0 Principle elements with those IDs | **SEMANTICALLY EQUIVALENT** current representation; current definition text is supplied by maintained sources. The snapshot axiom text does not override the current axiom register. |

The crosswalk is not uniformly architectural source. Its `elements` capture-key binding and `relationships` native mapping have the equivalents above. The historical allocation/binding event is continuity proof. `views[*].keys`, navigation paths, roles and retained layout scopes are used to place or organise Views: those portions are presentation, not additional architecture.

The recovered XML itself has 1,165 elements, 823 relationships and 984 Views. All its element, relationship and View IDs survive in RAD-1.1.0. That does not imply byte-identical subtrees: documentation is deliberately reconciled, governed metadata is added and later Archi transport normalises whitespace. This analysis does not claim that the old XML is a byte-interchangeable input for the new one.

### 4.3 B06 — the older semantic model and boundary validation

The exact content comparison is:

```text
B/architecture.json
  minus governedMetadata
  minus historical_reference_elements
  minus each element's canonicalId / displayName / friendlyName / canonicalName /
       nameComponent / nameComponentAuthority
  = L/model.json
```

These are exactly the removals defined by `governed_metadata.semantic_capture`, not an inferred or broader semantic filter. Equality holds for the **entire resulting JSON object**, including all 946 records, all 1,096 assertions, catalogue contexts, placement/currentness records, definitions, source excerpts, original qualifications and unresolved matters. This establishes **EXACT** maintained equivalence of the old semantic input.

At synchronisation lines 187–189, the maintained capture is assigned to `source`; the old model is assigned to `snapshot` and compared for equality. The returned semantic source is the maintained one. This read adds the fact “the adopted capture still equals its frozen predecessor”, rather than providing a missing definition or relationship.

`recover_v02.load_inputs` also parses that old model to check uniqueness, supported types, identity-lock fields and relationship-currentness coverage. It parses the manifest-selected governed 1.0.4 exchange and checks retained EC bindings against maintained mapping/registry data. Its normal synchronisation caller does not consume those return values. The 75-member manifest loop checks **every member's hash**, including old machinery, source files and other recovery operands; it does not execute or semantically ingest all 75 files.

Accordingly, **B06's normal backward dependency is reclassified to continuity/recovery validation**. Direct `RecoveryTests` construction and explicit `recovery*` targets exercise frozen recovery and belong to regression fixtures or optional recovery. Their required historical byte boundary is not a missing current architectural source.

### 4.4 B07 — Motivation content versus historical TeX

The four direct legacy carriers pinned by correspondence are:

```text
docs/latex-legacy/diagrams/fig-motivation-map.tex
docs/latex-legacy/chapters/01-motivation-strategy.tex
docs/latex-legacy/chapters/01-motivation-strategy-old.tex
docs/latex-legacy/chapters/appendix-requirements.tex
```

Maintained [B/motivation.json](../../docs/markdown/governance/baseline/motivation.json) already records **139 typed elements and 29 relationships**:

- **53 current elements**: 18 Principles from the axiom register, seven Drivers, five Assessments, six Goals, three Outcomes, five Requirements, three Constraints and six Stakeholders. Their documentation comes from complete sections of the maintained RADS sources listed below.
- **86 historical-reference elements**: 14 from the legacy diagram, five from the Motivation/Strategy chapter and 67 from the requirements appendix. Their keys, types, names, documentation, status, currentness, source locators and source assertions are captured explicitly; they are not promoted to current architectural intent.
- **18 current and 11 historical-reference relationships**, including their explicit predicates, endpoints and exact source assertions. No relationship is inferred from diagram proximity.

The current source set is precise:

| Source | Selection |
| --- | --- |
| `docs/markdown/governance/architectural-axioms.md` | The 18 native axiom sections identified by each capture record's heading/level |
| `docs/markdown/01-motivation/drivers-assessments/drivers.md` | Seven selected Driver sections |
| `docs/markdown/01-motivation/drivers-assessments/assessments.md` | Five selected Assessment sections |
| `docs/markdown/01-motivation/goals-outcomes/strategic-goals.md` | Six selected Goal sections |
| `docs/markdown/01-motivation/goals-outcomes/business-outcomes.md` | Three selected Outcome sections |
| `docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md` | Five selected Requirement sections |
| `docs/markdown/01-motivation/requirements-constraints/external-constraints.md` | Three selected Constraint sections |
| `docs/markdown/01-motivation/stakeholders/enterprise-stakeholders.md` | Six selected Stakeholder sections |

The record selectors are `elements[*].{source,heading,heading_level}` and `relationships[*].{source,assertion}`. `Baseline.build` renders `documentation`, `history`, type, status and currentness from this **maintained capture**. `load_inputs` reads a legacy file's text only to confirm a captured source assertion exists; hashes prove original-source correspondence. It does not parse that file to discover additional current elements, relations or definitions. The `-old.tex` file supplies no additional typed Motivation selection in the capture.

**Classification: EXACT** for the typed architectural information already in `B/motivation.json`. The historical validation read is Class 2; embedding the four complete legacy files into “Historical Source Evidence” Views is historical documentation/recovery content, with View presentation in Class 3. Neither supplies missing current Motivation architecture.

The separate `Recovery.evidence` follows the actual master TeX include graph and embeds 30 included section files as View documentation. It also transcribes five explicitly selected snapshot axioms. Those are explicit historical recovery operations, not the normal synchronisation semantic source. All 30 evidence documentation bodies are exactly retained in the governed 1.0.6 XML. The latest publication retains corresponding evidence Views, with whitespace-normalised text. This observation establishes retained carriers, not current architectural authority for every sentence in the original corpus.

## 5. Reclassifications and exclusions

These are limited to portions reached from the four starting records. They do not extend the analysis into the other classes.

| Reached read or portion | What it actually contributes | Reclassification |
| --- | --- | --- |
| B01 candidate layout, node positions, styles and View navigation | Authored presentation; all 1,209 resulting Views already survive in the current publication | **Class 3 — presentation configuration/content**. View semantic references are accounted for in section 4.1. |
| B01 candidate pin, plan/decision/review/source-manifest pins, and original rebinding provenance | Exact accepted boundary, permission and lineage, not extra element definitions | **Class 2 — identity and authority continuity**, only as needed to exclude them from Class 1. |
| B01 exact historical `reconciled.xml` comparison and Run-001 output comparisons | Expected results for replay/preservation | **Class 4 — regression expectations**; explicit execution reproduction is Class 5. |
| B05 `source-manifest.json`, `recovery-findings.json`, `architecture-release.json` within correspondence pins | Hash-only read in this normal loader; predecessor release is separately used for historical comparison metadata | **Class 2 continuity / Class 5 recovery**. Architectural assertions contained in findings are already exactly in maintained `architecture.json`; the file is not parsed here to supply missing facts. |
| B05 crosswalk View paths/roles/keys and recovered organisation hierarchy | View routing and presentation context, not a semantic hierarchy | **Class 3 — presentation**. Exact historic capture-to-ID establishment is Class 2; current bindings are accounted for above. |
| B06 manifest's complete 75-member boundary, identity lock and source equality | Integrity of the frozen recovery input population and non-drift of adopted source | **Class 2 / Class 5** in normal validation; direct recovery regressions are Class 4. No independent architectural reconstruction requirement remains in this record. |
| B07 assertion-in-original-source and source hash checks | Prove that maintained captures came from their recorded historical inputs | **Class 2 — source/authority continuity**. Captured architectural content is already maintained. |
| B07 full TeX copied into source-evidence View documentation; original included corpus | Display/preserve historical source evidence and support explicit recovery, without accepting its architectural meaning | **Class 5 — historical recovery/evidence documentation**, with View organisation/presentation in Class 3. Not a current architectural-source gap. |
| Archived identity-establishment `architecture.json` and `motivation.json`; older prior-release hash operands | Original ID ownership and preserved predecessor boundary | **Class 2**, not a new Class 1 source. Both named post-establishment captures are byte-identical to maintained source. |
| Historical adoption verification and legacy lock/history inventory | Prove a bounded historical identity event or report historical mechanisms | **Class 2 / Class 5**, not additional current architecture. No broader audit of those functions is claimed. |

This corrects the granularity of the earlier classification. B01 and B05 retain architectural-content components, all in Bucket A. B06's normal read is validation rather than reconstruction; B07's direct legacy reads similarly do not discover new current architecture. Their underlying captured architectural content is accounted for in A, while the read contracts belong in C.

## 6. Content checks and limits

| Read-only comparison | Result |
| --- | --- |
| Candidate versus RAD-1.1.0 populations | Identical resource-ID sets: 1,394 elements, 1,137 relationships, 1,209 Views |
| Candidate versus RAD-1.1.0 relationships / Views | All subtrees structurally equal, ignoring indentation-only XML text and tails |
| Candidate versus RAD-1.1.0 elements | All structurally equal after removing only `canonicalId` properties; 32 added IDs, 50 property-order differences, 1,312 originally identical subtrees |
| `L/model.json` versus maintained semantic capture | Entire JSON objects equal after only the declared metadata removals |
| Recovery crosswalk's 946 element bindings | All match published source-capture key/reference ID and maintained canonicalId ownership |
| Crosswalk's 634 native relationship source assertions | All equal maintained assertions at their exact source indices; all native IDs/types and exact source/target bindings present in RAD-1.1.0; all IDs recorded in maintained `relationships.md` |
| Recovery findings' 462 deferred assertions | All equal maintained assertions at their exact indices |
| Retained EC documentation | All 14 original governed 1.0.4 documentation prefixes present exactly in current RAD-1.1.0 element documentation |
| Original governed 1.0.4 resources | All 228 element and 189 relationship IDs retained in RAD-1.1.0 |
| Recovery structured properties | Governed 1.0.6 retains all properties on all 1,165 elements and 823 relationships exactly. In RAD-1.1.0, differences in retained property values reduce to whitespace in embedded excerpts/JSON strings; the exact semantic capture remains maintained independently. |
| Identity-establishment copies | `architecture.json` and `motivation.json` byte-identical to maintained files of those names |
| Current Motivation documentation | All 53 captured current documentation bodies equal their selected complete live RADS sections |
| Source-evidence corpus documentation | All 30 recovery evidence bodies exactly retained in governed 1.0.6; corresponding latest bodies differ in whitespace. Raw historical TeX is not treated as current architectural authority. |

Recorded byte identities disambiguate the inspected carriers:

| File | SHA-256 |
| --- | --- |
| `B/architecture.json` and identity-establishment copy | `0f7dc7b43da74dddffcb41b169031f0bd771c51509002851bb07c1f1a7eeee0b` |
| `B/motivation.json` and identity-establishment copy | `a6278adf4979b88d43e664a72833b96d3632dfb7f87d40e0e0a4b60833f1d56f` |
| `P/1.0.5/Harmonia-Architecture-1.0.5A.xml` | `f85be36ff6edb3c6491dd526c42d4207db9214154da437941b709de34dc106a1` |
| `M/run-002/candidate.xml` | `541219c023d2cd507d591e81df9a0a82fbf1c8f4d324448a02bc476e00c3e59a` |
| `X/publish/1.1.0/Harmonia-Architecture-1.1.0.xml` | `4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1` |
| Root `architecture-release.json` | `6423e6c3f4ce3392c219a657002a963c8d0c847819490fa0981f00e73cc06465` |

These are content comparisons, not proof that a consumer accepts a different path or can skip its history validation. No counterfactual publication, machinery-validation run, recovery, test execution or evidence-package construction was performed. In particular, no current architecture was reverse-derived or re-established from old implementation.

## 7. Final reduction

### Bucket A — already present in maintained architecture

| Architectural requirement | Maintained carrier(s) | Equivalence |
| --- | --- | --- |
| Complete accepted model underlying B01 | Governed RAD-1.1.0 XML; accepted IA element/relationship/View records in `B/accepted-ingestion-1.0.10.json` | **SEMANTICALLY EQUIVALENT** complete architectural model; **EXACT** relationships, Views and accepted captures |
| Original catalogue definitions, relationships, source qualifications and unresolved dimensions underlying B05/B06 | `B/architecture.json`, its baseline catalogues and `B/relationships.md`; corresponding current published nodes/edges | **EXACT** capture; **SEMANTICALLY EQUIVALENT** governed representation |
| Current representation correspondence for 946 records and 634 native assertions | Published key/reference/identity properties; maintained resource fields and relationship catalogue | **EXACT** current bindings/assertions; old establishment event separately excluded |
| Retained EC architectural documentation and historical reference population | Current governed XML; maintained historical-reference records; earlier governed representations retain original qualified content | **EXACT** EC prefixes; **SEMANTICALLY EQUIVALENT** qualified reference population |
| Explicit current and historical Motivation selections | `B/motivation.json` plus its selected maintained RADS sources; current published Motivation nodes/edges | **EXACT** captured typed information |

For each row the historical carrier is unnecessary **as the unique source of architectural meaning**. This says nothing about bypassing its present validation role.

### Bucket B — genuinely missing from maintained architecture

**Empty. No required architectural element, relationship, definition, property meaning, Motivation assertion, standing qualification or unresolved architectural matter was identified only in a historical processing carrier.**

The smallest set of genuinely missing maintained architectural information is therefore **the empty set**. There is no `PARTIAL` or `NONE` finding to specify or design a replacement for.

### Bucket C — not actually Architecture Reconstruction

The excluded portions are the exact source/identity/authority continuity checks, historical boundary hashes, predecessor release restoration, View presentation/routing, historical source-evidence documentation, regression expectations and explicitly invoked recovery/adoption operations in section 5. Their existence explains why current machinery can still fail without history even though Bucket B is empty.

No finding about those dependencies' full preservation requirements, acceptability or replacement is made here.

## 8. Uncertainties and Solution Architect boundary

No unresolved **Class 1 content-existence** question remains from the four examined records. The following boundaries remain explicit:

1. **Complete operational source boundary:** maintained captures are layered; the accepted complete model also exists as a governed representation. This report establishes content availability. It does not decide which combination a future publication/source contract should consume, or grant authority to extract architecture from a historical carrier into RADS. That later source-boundary decision belongs to the Solution Architect.
2. **Authority is qualified, not inferred:** the 214 unreconciled references, 462 metadata-only assertions, held IA classifications and other recorded gaps retain their existing standing. Their presence in a governed file does not turn them into approved current facts. This analysis does not close those architectural questions.
3. **Historical material outside the consumed scope:** no conclusion is drawn about unused legacy prose, unrelated processing populations, native backup-only content or original before-state recovery. A broader claim that every historical architectural sentence is reconciled would require different authorised work.
4. **Release applicability:** root RAD-1.1.1 and published RAD-1.1.0 have the recorded distinct roles. The resulting machinery guards remain a separate issue. They provide no evidence of missing architectural content and are not adjudicated here.

The report introduces no architectural proposal and requires no missing-content adjudication to support its bounded conclusion. It supplies no disposal authority and no assurance that Classes 2–5 are independent of processing.

## 9. Task boundary and plain answers

Only this report was created. Architecture, framework/tooling, loaders, tests, governance, release metadata, existing evidence and historical material were not changed, generated, moved, copied, renamed or deleted. Existing working changes were preserved. No architecture suite was run because this was the expressly read-only analysis/report task; no production or architectural implementation was changed.

**Does current Harmonia operation require any architectural information that exists only in historical processing material?**

**No, within the Class 1 dependencies examined.** Machinery still reads historical architecture carriers, but the architectural information it consumes already exists in maintained governed captures and/or the applicable governed publication. Other reached reads are continuity, presentation, regression or recovery requirements.

**Exactly what architectural information is missing from the maintained governed architecture?**

**None identified. The minimal missing-information list is empty.** This is a content finding, not a claim that current machinery already operates independently of historical processing. Work stops at this analysis and report.
