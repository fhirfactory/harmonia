<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Presentation configuration dependency analysis — Dependency Class 3

Date: 2026-10-20. Status: bounded analysis of the current working repository; not architectural adjudication, a new presentation contract, implementation direction or disposal authority.

## 1. Executive summary

**No architect-authored presentation information required by the examined current workflow was found only in historical processing.** The governed RAD-1.1.0 publication already carries the complete presentation that the release publisher obtains from the historical Run-002 candidate. Direct inspection established an exact serialized match for the entire `views` subtree and the entire `organizations/item[label='Views']` subtree, including all 1,209 Views, 4,005 visual objects, 1,804 connections, 2,908 bendpoints, styles, documentation, properties, ordering and nesting. This finding was checked afresh against actual files and preservation requirements; it is not inferred from the earlier round-trip PASS.

**The machinery nevertheless still depends on historical processing for presentation.** The RAD-1.1.0 adapter reads the complete candidate there; the fallback publisher starts from the recovered model/crosswalk and reads historical selection files and a semantic census. Those are real dependencies even where their contents are duplicated or their role is to replay an earlier generation algorithm. Existing release-applicability and authority-pin limitations remain separate and were not changed or solved here.

The Class 3 starting records need finer classification:

- **B16** is primarily an earlier Home-completion delta check. It reads earlier Views and navigation as comparison operands; it does not restore missing presentation into the result.
- **B17** mixes generated-View replay scope, protection of four accepted Views, computed membership expectations and historical diagnostic labels. It contains no coordinates, dimensions, bendpoints, styles or complete authored diagrams. The resulting presentation is already in RAD-1.1.0. Exact generation-era labels and expected lists are not all restated there, but their absence is not missing architect-authored presentation.
- **B01/B05/B07**, inherited from Classes 1/2, expose the actual complete View carriers, older View-routing descriptors and historical source-documentation Views. Their necessary current presentation is preserved in maintained state; old generation descriptors and exact intermediate states have separate roles.
- Element/Relationship folder hierarchies remain **derived navigation**. Their construction and regression expectations must not be counted as architect-authored View authority.

**Bucket B is empty. The smallest set of genuinely missing governed presentation information is the empty set.** This is a content-availability finding within the examined dependencies, not proof that today's loaders can operate without processing or permission to discard any carrier.

## 2. Authority, scope and method

Authority loaded independently of previous reports:

- Root [AGENTS.md](../../AGENTS.md), [documentation instructions](../../docs/AGENTS.md), and the [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md), particularly AX-04, AX-08/09, AX-14, AX-15 and AX-17: machinery does not define architectural intent; meaningful presentation, generated structures, evidence and historical execution must remain distinct; uncertainty must remain explicit.
- [Architecture Change Provenance](../../docs/markdown/governance/architecture-change-provenance.md): current-operation independence is a required end state, with live dependencies retained until separately authorised work resolves them. Installation supplies neither independence nor disposal authority.
- [Round-Trip Engineering Praxis](../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md#common-interpretation-and-projection-rules), [publication semantics](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md#views-as-projections-and-preservation-of-authored-views), and the [navigation/View convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md#current-round-trip-projection--2026-10-20). All existing round-trip Views and their navigation are preserved; older generated-View replay does not authorise regenerating submitted Views. Element/Relationship folders are derived from resolved containment and governed collection roles.
- The maintained [RAD-1.1.0 release decision](../../docs/markdown/governance/baseline/release-1.1.0.json), [published-package index](../../docs/archimate/exchange/publish/README.md) and [release policy](../../docs/markdown/governance/architecture-release-policy.md), to identify the actual governed representation and permitted change scope.

The [original dependency analysis](2026-10-20-current-to-historical-processing-dependency-analysis.md), [Class 1 analysis](2026-10-20-architecture-reconstruction-dependency-analysis.md), [Class 2 analysis](2026-10-20-identity-authority-continuity-dependency-analysis.md), and supplied [provenance design](2026-10-20-architecture-provenance-and-evidence-design.md) supplied investigation leads, not architectural authority.

Scope starts with B16/B17, follows the presentation portions referred from Classes 1/2, and identifies adjacent fixture/recovery reads only to reclassify them. No fixture redesign, historical replay/audit, identity-continuity solution, release-applicability correction or machinery change was performed. No new authoring submission was selected from the pre-existing untracked ingest directory.

Observed boundary:

| Item | Inspected value |
| --- | --- |
| Git HEAD | `8f3244b1af1481885b578f3749b35ebf0ffdc6a6` |
| Root release | `version=1.1.1`, `previous_version=1.1.0` |
| Root release SHA-256 | `6423e6c3f4ce3392c219a657002a963c8d0c847819490fa0981f00e73cc06465` |
| Applicable governed publication examined | `docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml` |
| Publication SHA-256 | `4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1` |
| Historical complete candidate | `docs/archimate/processing/machinery-validation/information-architecture/run-002/candidate.xml` |
| Candidate SHA-256 | `541219c023d2cd507d591e81df9a0a82fbf1c8f4d324448a02bc476e00c3e59a` |

Method: inspect maintained call sites and the exact JSON fields read; parse XML securely without network/entity resolution; compare full ordered subtrees, attributes, non-indentation text, and separately serialized subtrees; enumerate presentation objects and references; inspect selected compressed before states without extraction or mutation. No publication, candidate or layout was regenerated, including in memory. Exact subtree serialization below means `lxml.etree.tostring` of the parsed subtree with its tail excluded, not whole-file byte equality.

Aliases below are exact repository-relative prefixes:

| Alias | Prefix |
| --- | --- |
| `F` | `docs/archimate/framework` |
| `B` | `docs/markdown/governance/baseline` |
| `P` | `docs/archimate/processing/publishing` |
| `I` | `docs/archimate/processing/ingestion` |
| `M` | `docs/archimate/processing/machinery-validation/information-architecture` |
| `X` | `docs/archimate/exchange` |
| `L` | `docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft` |

`Q` means `I/1.0.10/run-002/reconciliation/execution-001`; `CURRENT` means `X/publish/1.1.0/Harmonia-Architecture-1.1.0.xml`. Equivalent classifications concern information, not acceptance of a replacement pathname by the existing hash-pinned loader. An **EXACT** maintained equivalent does not imply that changing a pinned input is authorised.

## 3. Dependency catalogue: consumer → carrier → information → purpose → equivalent

Composite records are split by consumed information so that a historical expected result is not mistaken for missing presentation.

| Record/portion | Maintained consumer | Exact historical carrier/population | Presentation information actually consumed | Why processing reads it | Significance; maintained equivalent; reduction |
| --- | --- | --- | --- | --- | --- |
| B01: complete accepted presentation | `publish_information_release.inputs/publish → reconciliation.information.render`; conditional `synchronise_baseline.publish` fast path; `test_information_release` | `M/run-002/candidate.xml`, selected by `B/release-1.1.0.json.inputs.candidate` and the reconciliation plan | Every View identifier, name/type, visual object and semantic reference; local connection identity/endpoints; node geometry/nesting; bendpoints; style/font/colour; labels; documentation/properties; ordered View contents; complete Views navigation | `render` copies the candidate's complete tree, overlays accepted **elements**, and reprojects Element/Relationship folders. Complete existing Views come from that tree; the bounded accepted capture is not a complete-model replacement. | **GOVERNED PRESENTATION; EXACT** in CURRENT's complete `views` and Views organisation subtrees. **A**. |
| B01/B04: exact intermediate presentation operand | Same publisher's exact `reconciled.xml` replay comparison; release regression | `Q/artifacts/reconciled.xml` | Complete intermediate model, including its Views/tree | Require the replay to reproduce the accepted intermediate bytes before model-level release metadata changes | Presentation portion **EXACT** in CURRENT; the demand for the whole historical intermediate file is **TEST EXPECTATION / HISTORICAL/RECOVERY INFORMATION**, **D**. No separate presentation contribution. |
| B05: recovered View state | `synchronise_baseline.load_inputs → Baseline.__init__/build`; `feature_projection`; dependent baseline/presentation tests | `P/1.0.5/Harmonia-Architecture-1.0.5A.xml` | Starting 984 Views, existing node/connection IDs and dimensions/style/documentation; old View navigation paths read through `navigation_homes`; existing diagram contents used before later presentation layers | The fallback builds from a recovered complete tree, moves retained View entries, reuses existing Views and applies historical generated layouts before accepted later overlays | **GOVERNED PRESENTATION** for the retained resulting state; **SEMANTICALLY EQUIVALENT**, evolved under the governed presentation changes, in CURRENT. All 984 View IDs and every recovered node/connection ID survive. Old layouts are not claimed exact current layouts. **A** for current state; exact pre-migration state is **D**. See sections 5/6. |
| B05: generated View descriptors | `Baseline.view_path`; `feature_projection.landscapes/business_layout/validate` | `P/1.0.5/representation-crosswalk.json.views`, 765 entries | Generated `role`→View bindings; `keys` identifying the context, Feature or behaviour-page membership; `assertion:*` target key for routing; context/features roles for View reuse; behaviour roles/keys for layout selection | Recreate earlier View placement, landscapes and Feature-column layouts; validate those earlier projections | **DERIVED PRESENTATION/NAVIGATION** generation descriptors. **PARTIAL** as an exact descriptor equivalent: maintained governance/framework retain the rules and CURRENT retains complete resulting Views/membership/tree, but no independent maintained copy of the exact 765-entry role/key registry was identified. Historical-only part is the generation-era descriptor registry, not missing authored geometry, View identity or current navigation. **C**. |
| B05: Element/Relationship paths | `Baseline.build/organizations`; historical projection validation | Recovered XML `organizations`, alongside maintained source-derived paths. Crosswalk element `navigation_path` values are recorded outputs, not read by this baseline reconstruction chain. | Old resource/reference locations, folder labels/IDs/order; the fallback retains some unbound historical resource paths and constructs source-based Relations | Rebuild the old workspace and check its then-current organisation; not architectural containment authority | **DERIVED PRESENTATION/NAVIGATION**; current algorithm/rules/classification inputs are maintained, and CURRENT contains the resulting workspace. **SEMANTICALLY EQUIVALENT** for necessary current navigation; **PARTIAL** for exact old pre-migration scaffolding, which is historical output rather than a current presentation requirement. **C**, with old expected bytes **D**. |
| B07: source-evidence Views | `Baseline.build` and explicit `recover_v02.Recovery.evidence`; recovery tests | Four `docs/latex-legacy/` paths in `B/correspondence.json.pinned_inputs`; recovery's 30 included corpus files under `L`, selected from its TeX include graph. Their exact population is `P/1.0.5/representation-crosswalk.json.views` entries with `role=evidence:*`, resolving the suffix after `evidence:` relative to `L`. | Original text embedded in View Documentation; source path/hash properties; evidence-View identity, label/canvas and navigation | Preserve/display historical source material and reconstruct those evidence pages; not discover current architectural intent | Existing View presentation is **GOVERNED PRESENTATION**, **EXACT** in CURRENT relative to the accepted candidate. Historical documentation text is **SEMANTICALLY EQUIVALENT** after whitespace normalization in CURRENT and **EXACT** in governed `X/publish/1.0.6/Harmonia-Architecture-Baseline.xml`. **A** for retained pages, **D** for original corpus/recovery checks. |
| B16: pre-Home presentation comparison | `home_context.audit/before_bytes`; transitive fallback/presentation tests | `P/1.0.9/home-context-completion/before.json`, selecting `before/docs/archimate/exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml.gz` under that directory | Earlier complete View contents and resource/relationship/View navigation locations; expected unchanged View signatures and unrelated paths | Bound the Home naming/navigation delta against its actual pre-completion state. If later presentation was applied, the audit substitutes the already-held pre-presentation Views in a **comparison copy**, then compares with the old model | **TEST EXPECTATION / HISTORICAL/RECOVERY INFORMATION**, **D**. **NONE identified outside processing for this exact complete pre-Home comparison state**; CURRENT has the evolved presentation, not its predecessor's exact generated routes/organisation. This historical-only comparison state supplies no missing current authored View. |
| B17: saved generation scope | `view_presentation.publish/selection/apply/audit`; `test_view_presentation` | `P/1.0.9/containment-scope-completion/selection.json`; selected fields of `P/1.0.9/view-presentation-completion/selection.json` | Exact field breakdown in section 4: 231 generated containment specs, 61 business View keys, protected IDs, historical/oversized lists and stale-route labels | Reproduce and constrain the earlier generated-View migration, including selection equality and fixed population checks | Mixed **DERIVED PRESENTATION/NAVIGATION**, **TEST EXPECTATION**, and pointers to **GOVERNED PRESENTATION**. Required resulting View state **EXACT** in CURRENT. Generation-era classifications have **PARTIAL/NONE** independent maintained equivalents, detailed below; they are **C/D**, not missing authored state. |
| B17: saved semantic census | `view_presentation.selection` | `P/1.0.9/containment-scope-completion/semantic-census-before.json`, only `eligible` keys and each row's `source/children/relationships` | Expected View→source/direct-child/native-edge mapping for 231 Views | Assert recomputed eligibility/membership equals the investigated historical population | **TEST EXPECTATION** for derived generation; **EXACT** membership/edge outcomes already in CURRENT and `selection.json`, but **NONE identified as a separate maintained census file**. Extra census `path/n/additional_views/oversized/protected_authored` fields are not read by this production check. **D** for frozen expected population, **C** for its derived content. |

Source call sites: [release publisher](../../docs/archimate/framework/tools/publish_information_release.py), [reconciler](../../docs/archimate/framework/tools/reconciliation/information.py), [baseline publisher](../../docs/archimate/framework/tools/synchronise_baseline.py), [Feature projection](../../docs/archimate/framework/tools/feature_projection.py), [Home audit](../../docs/archimate/framework/tools/home_context.py), [presentation replay](../../docs/archimate/framework/tools/view_presentation.py), and [recovery](../../docs/archimate/framework/tools/recover_v02.py). No run-local executable was invoked.

The four B07 legacy paths are `docs/latex-legacy/diagrams/fig-motivation-map.tex` and `docs/latex-legacy/chapters/{01-motivation-strategy.tex,01-motivation-strategy-old.tex,appendix-requirements.tex}`. For B05, `views[*].role/keys` are the operative stored descriptors; its recorded `views[*].path/relationships` are not read by the inspected fallback placement/layout functions. Actual old View paths come from recovered XML organisation. Stored fields were not counted as active presentation inputs merely because their file is opened.

## 4. B17 field-level findings

The two selection files are [completed selection](../../docs/archimate/processing/publishing/1.0.9/containment-scope-completion/selection.json) and [earlier selection](../../docs/archimate/processing/publishing/1.0.9/view-presentation-completion/selection.json). The second is not consumed wholesale as generation configuration: `selection()` reads its `historical_candidates` and `oversized` fields. A test separately reads its earlier `containment` keys to count the scope correction.

| Field | Exact use | Maintained equivalent and classification |
| --- | --- | --- |
| `authority`, `architecture_release` | Authority link and originating migration release participate in whole-scope equality; the release comes from the completed selection | Maintained publication-semantics rule and governed 1.0.9 release supply **EXACT** rule/release facts. Originating migration context is **HISTORICAL/RECOVERY INFORMATION**, not an additional View characteristic. **D**. |
| `containment[View ID].{source,children,relationships}` plus `purpose/standing_filter` | Recomputed from declared landscape purposes and state-filtered native Composition; checked against saved selection/census. `apply` uses the validated saved spec to add missing visual references and lay out those Views | All **231** current Views contain exactly `{source, children}` and the selected native-edge references. **EXACT** resulting membership; the generic purpose/rule is maintained. Raw generation scope is **DERIVED PRESENTATION/NAVIGATION**, and fixed equality is a **TEST EXPECTATION**. **C/D**; resulting View state **A**. |
| `protected_authored` — four IDs | Independently obtained from maintained `B/accepted-ingestion-1.0.8.json.views[*].representationId`; excluded from generated selection; unselected XML must match exactly | **EXACT**, both the four IDs/full View records in the maintained capture and full presentation in CURRENT. These are two Person Identity landscapes and two Feature-realisation Views. Current round-trip governance protects **all** existing Views, not only these four. Protection carries governed significance, but history contributes no unique protection list or presentation. **A**. |
| `business` — 61 View IDs with connection-ID lists | Recomputed from `b.layout_groups` and reconstructed Views. `apply` iterates the **View keys**, then reroutes every connection in each View from its actual rectangles. The saved connection lists constrain equality; they are not stored routes | **EXACT** View and connection-ID sets in CURRENT for all 61. Nine saved lists have a different connection sibling order from CURRENT, while their sets agree. Exact old list order is **TEST EXPECTATION / TOOL REPRESENTATION DETAIL** for replay, not an extra route or a demonstrated current ordering requirement. **C/D**. Current ordered View subtrees are nevertheless preserved exactly from the accepted candidate. |
| `historical_candidates` — 62 IDs | Copied from the earlier selection, sorted, counted and reported. This list is **not passed as an exclusion filter** to `containment_candidates`; that function uses declared purpose, currentness, direct Composition, protected IDs and child-count limits | All 62 Views survive in CURRENT: **EXACT** presentation. **NONE identified** for a separate maintained copy of this exact migration-era candidate label list. The historical label roster is the historical-only information; it is not missing presentation or new current selection authority. **D**. |
| `oversized` — three View/count records, each 21 children | Merge the earlier two records with recomputed oversized cases; count/report the result. Candidate generation independently skips a direct-child population above 20 | All three Views survive in CURRENT; the >20 unresolved rule is maintained in publication semantics: **EXACT** state/rule. **PARTIAL** for the exact historical migration roster outside processing. The roster supplies historical case selection, not layout geometry or an authorised alternative layout. **C/D**. |
| `known_stale_connections` — 29 IDs | Recomputed by finding existing bends in the pre-migration reconstructed Business Views; used to annotate/count changed connections. It does not supply bendpoints or determine the new route | All 29 connections survive in CURRENT: **EXACT** identities and final routes. **NONE identified** for their old “known stale” classification as maintained current data. This temporal diagnosis is **HISTORICAL/RECOVERY INFORMATION / TEST EXPECTATION**, **D**. |
| `uncertain_diagnostics` — one ID | Hard-coded in maintained `selection()` and compared with the saved scope; not used to generate a diagnostic replacement | **EXACT** ID in maintained code and diagnostic View in CURRENT. Existing unresolved presentation is preserved, not adjudicated. **A** for retained presentation; migration label **D**. |

The completed census's 231 `source/children/relationships` records exactly equal the corresponding selection records. `selection()` independently computes them before comparing. Geometry comes from existing rectangles and maintained layout/routing functions (`containment_layout`, `business_route`); the census contains no authored coordinates, bends or styles.

**Necessary distinction:** the migrated diagrams are now existing preserved Views under current governance, even when earlier machinery originally generated them. Their historic generation origins do not make today's complete View state disposable. Conversely, a historical selection roster does not become architect-authored presentation merely because it was used to generate those Views.

## 5. View preservation findings

### Complete required boundary

The Praxis requires preservation of identity, membership, connections, nested nodes, order, layout, geometry, style, documentation and View navigation. The release renderer copies the candidate tree; its accepted View capture is a validation operand rather than a source from which it recreates all Views. After reprojection, `reconciliation.information.render` asserts both signature and serialized equality for `views` and the Views organisation. `publish_information_release.publish` checks their serialized equality again against the candidate after changing model release/standing metadata.

Direct comparisons found the following in CURRENT versus **both** the historical Run-002 candidate and `Q/artifacts/reconciled.xml`:

| Required characteristic | Observation |
| --- | --- |
| View population, identity, names/types | All **1,209** IDs and full ordered View subtrees match exactly. No historical-only View or View identity. |
| Membership and visual objects | All **4,005** objects match, including **3,967** element-reference nodes and **38** labels. No historical-only visual object or reference. |
| Connections | All **1,804** connection IDs, relationship references, local source/target IDs and complete ordered contents match. No historical-only connection or endpoint. |
| Geometry | Every node's `x/y/w/h` matches; all **2,908** bendpoints match, including sequence. No lost historic geometry or route. |
| Style | All **5,809** style subtrees match, including node fills, line colours, fonts and font colours. Any other attributes/children would also have been covered by whole-subtree equality. |
| Documentation/properties | All **1,196** View documentation nodes and **68** properties in **34** property containers match. All four referenced property-definition IDs exist and their definitions match the candidate. Names, labels and text are included, not only IDs. |
| Nesting/order | Whole-tree comparison preserves child sequence and parentage, including **60** nested node-to-node occurrences. No sibling sorting or flattening was used in this comparison. |
| View navigation | Complete ordered Views subtree matches, including **1,412** folders counting its root and exactly one reference to each of the 1,209 Views. Names, nested paths, reference/folder IDs and ordering are present in maintained state. |
| External references needed to use the diagrams | Every visual `elementRef` and `relationshipRef` resolves in CURRENT. Referenced View property definitions are present, not dangling historical dependencies. |

All four accepted 1.0.8 View records and all twelve accepted 1.0.10 View records also match CURRENT structurally without text normalization. The latter capture contains eleven new Topic Views and the revised InformationMap. These captures provide additional maintained records of the accepted bounded changes; neither is falsely treated as the entire presentation model.

### Earlier recovered state and historical documentation

The recovered 1.0.5A tree contains 984 Views, 2,883 visual objects and 1,320 connections. Every one of those View IDs and node/connection IDs survives in CURRENT, and every recovered node's width/height survives. All 984 View documentation bodies agree after whitespace normalization. All 1,320 recovered connection styles match structurally; all 2,883 recovered node styles match after disregarding only explicit `a="100"` colour attributes versus their absence. This describes the observed serialization difference without assuming arbitrary style differences are harmless. Current coordinates are not universally the recovered coordinates: **816** recovered nodes have changed `x/y` in the subsequent governed presentation evolution. The navigation/View convention's synchronised-baseline correction and later containment/routing publication rules explicitly describe those presentation changes. The old tree is a starting state for replay, not the specification of the current layout.

The recovered crosswalk's 765 View descriptors were emitted by `Recovery.view` as `role/keys/relationships/path` records. They describe generated recovery purposes, pages and routing. They are not a separate collection of architect-authored hidden coordinates, styles or bendpoints. Current View state and navigation no longer require reconstructing their original generation decisions to know what must be preserved.

Historical source-evidence documentation needs a separate precision statement. All **30** recovery evidence bodies and the **four** additional legacy Motivation source-evidence bodies survive exactly in the governed 1.0.6 publication. Their corresponding current documentation is whitespace-normalized but textually equivalent; original source formatting is not asserted byte-identical in CURRENT. Current accepted candidate→publication preservation is exact, including these pages. Exact original TeX formatting is a historical-source/recovery matter, not newly discovered missing authored View presentation. No current architectural authority is conferred on those historical texts.

A comparison with the earlier governed 1.0.10 publication also encounters the old InformationMap placeholder label `id-e61da167f9f25082b9794aa8ebe715b0`, which the accepted revised InformationMap replaces. Its earlier state remains in that governed publication. It is not a missing node from the accepted 1.1.0 candidate. Likewise, explicit opaque colour-alpha attributes and normalized text in older states cannot be equated with lost current presentation simply because the XML is not byte-identical. The complete accepted candidate's styles and text, including its explicit alpha values, are preserved exactly in CURRENT.

**Conclusion for subsequent processing:** the existing governed publication contains the complete accepted View state these consumers preserve. No additional historical View, membership, visual object/connection, geometry, bendpoint, style, documentation, governed order/nesting, View-tree property or other consumed presentation characteristic was found necessary to supplement it. This establishes information sufficiency, not a new source-selection contract or proof that an unmodified historical adapter accepts CURRENT as its input.

## 6. Navigation/folder findings

Three structures must stay separate:

| Structure/read | Actual role and classification |
| --- | --- |
| Complete Views organisation from B01 | Preserved navigation of existing governed Views. **GOVERNED PRESENTATION**, already **EXACT** in CURRENT. This includes View-specific ordering/nesting; it is not discarded with resource folders. |
| Recovered resource paths and crosswalk/landscape/page descriptors in B05 | Inputs to the **earlier** navigation/layout algorithm. Baseline code reads old View paths, `assertion` target keys, generated roles and behaviour-page keys. It also retains some historical resource locations. Classify the role descriptors as **DERIVED PRESENTATION/NAVIGATION**, with exact old-state comparison **TEST EXPECTATION**, not hidden architect-authored View state. |
| Current complete Element/Relationship projection | [Workspace policy](../../docs/archimate/framework/tools/workspace_policy.py) reads maintained `B/architecture.json`, `motivation.json`, `accepted-ingestion-1.0.8.json`; native information anchors/rules are maintained code. [Workspace navigation](../../docs/archimate/framework/tools/workspace_navigation.py) uses shared resolved containment and explicit placements. For complete element coverage it removes **all non-Views top-level organisation branches**, rebuilds Element folders and source-based Relations, and retains the complete Views branch. Old Element/Relationship folder ancestry supplies no required presentation authority. |
| Saved `workspace-projection.json`, folder generation proofs, earlier before projections and navigation report counts | Derived algorithm outputs or regression expectations. Their exact historical bytes and old folder IDs are not architect-authored Views. **DERIVED PRESENTATION/NAVIGATION / TEST EXPECTATION**. |

The complete current projector gives every resolved containment participant its own same-named folder, including contained leaves; it places one element reference there and nests child folders. Relationships follow their actual source's resulting path. Its generation proof exercises absent and misleading incoming resource folders. These maintained requirements and classification inputs, rather than historical folder trees, explain necessary navigation. This task inspected those mechanics; it did not regenerate a proof or candidate.

With partial declared coverage the reusable projector can retain unprojected homes/scaffolding. That generic interface does not change the inspected release path: `workspace_policy.placements` covers every supported element, and reconciliation supplies that complete map. An arbitrary future partial caller is not evidence that the complete current model has missing presentation.

Regenerated navigation-item IDs, historical UUID keys and sibling order in derived resource branches are **TOOL REPRESENTATION DETAIL** or deterministic workspace behaviour unless a specific governed requirement says otherwise. They do not become governed authored presentation through visibility in Archi. View-tree order and in-View order are independently preserved in the exact current comparisons above; no assumption that all XML order is harmless was used.

## 7. Chained sourcing: what history actually adds

The accepted release path is:

```text
historical complete Run-002 candidate
  + maintained accepted resource capture and maintained collection classification
  + pinned plan/decision/control/result used to validate scope and replay
        ↓
element amendments; derived Element/Relationship folders; release metadata
        ↓
CURRENT — all candidate Views and Views navigation preserved exactly
```

CURRENT is the maintained **output** of this path, not its complete input. The historical candidate supplies complete presentation to the implementation, but contributes no presentation absent from that maintained output. The twelve accepted View records are checked against existing candidate Views, not combined with CURRENT to recover the other 1,197 Views.

The older path is:

```text
historical recovered complete tree + generated View-role/key registry
  + maintained semantic/Motivation captures and presentation rules
  + maintained accepted 1.0.8 View capture
  + historical generated-View selection/census
        ↓
earlier replayed navigation, generated layouts/routes and preservation assertions
```

The old selection/census constrains **which earlier generation is reproduced**, not what hidden geometry should be restored to the current governed publication. Existing widths/styles are read from the recovered/reconstructed Views; layouts/routes are calculated by maintained functions. CURRENT already contains the resulting accepted View state. `home_context.audit` adds an earlier comparison boundary, not a missing presentation overlay.

No examined normal consumer was found taking CURRENT and restoring a missing architect-authored characteristic from a historical capture. The actual chains either start from a complete historical tree, reproduce an earlier generated presentation, or compare against an earlier state. Changing those chains is outside this report.

## 8. Presentation-related reclassifications and exclusions

| Original record/lead | Result of Class 3 examination |
| --- | --- |
| Class 1 B01 layout/style/View navigation | Actual governed presentation, **A**, already exact in CURRENT. Candidate pin/plan/review authority remain Class 2; exact whole intermediate reproduction is Class 4/5. |
| Class 1 B05 crosswalk View paths/roles/keys and recovered organisation | Split into preserved resulting View state **A**, generated replay descriptors/resource paths **C**, and exact old-state expectations **D**. Neither a generated role nor an organisation folder establishes semantic containment. |
| Class 1 B07 full-source documentation/evidence Views | Retained governed View state **A**; original source corpus and exact recovery are **D**, historical/recovery. No new architectural semantics adjudicated. |
| Class 2 C05 / B03 protected View identity/content | Native View identity and authorised-scope continuity stay Class 2. The presentation being protected is already exact in CURRENT; requiring a prior comparison operand is not a presentation-content gap. |
| Class 2 C14/C15 frozen visual hashes, navigation-baseline hashes and old deterministic census | **D**, frozen integrity/fixture/recovery expectations; no additional authored presentation source identified. Class 2 introduced no separate historical-only authored layout carrier. |
| B16 itself | Reclassify from general “presentation configuration” to a historical delta-validation boundary, **D**. Its separate source-era naming-authority read through `reconcile_108.evidence_bytes` remains Class 2 and supplies no View content. |
| B18 `view_presentation.main(audit)/before_bytes` | **D**, historical completion audit. Its `containment-scope-completion/before.json` has 647 rows, with eight compressed snapshots; the audit enumerates all rows, not merely View XML. It is not used by normal `view_presentation.publish` to supply final View state. |
| B15, presentation portions of B20/B24/B25/B28 | **D**, regression/preservation expectations: starting View hashes, saved validation outputs/candidates, selected prior projections and before-correction states. Their purpose is testing assertions about earlier inputs/results. No fixture population was redesigned or independently audited here. |
| B19 presentation handoff/report reads; B30–B39 historical preservation/transport/replay references | **D**, historical execution/report/recovery rather than missing current authored state. Saved logs, original transport specimens and intermediate serialization are distinct from maintained governed presentation. No optional replay/audit was executed or analysed further. |

## 9. A/B/C/D reduction

The unit of classification is the information consumed, not an entire directory or a composite file.

| Bucket | Findings |
| --- | --- |
| **A. Governed presentation already exists in maintained state** | B01 complete accepted Views and navigation; B05 retained/evolved current diagrams; all four accepted 1.0.8 and twelve accepted 1.0.10 View records; protected/oversized/historical/diagnostic diagrams; retained evidence-View state. CURRENT contains the complete accepted presentation. Historical carriers contribute no unique required current presentation. |
| **B. Governed presentation exists only or partially historically** | **No instances identified within scope.** No missing current View, identity, membership, object, connection, rectangle, route, style, documentation, governed order/nesting or navigation characteristic. |
| **C. Derived/tool presentation dependency** | Earlier generated View-role/key descriptors; containment selection and calculated layout/routing inputs; generated Element/Relationship folder structures, collection/source projection and their tool-specific IDs. Some exact old descriptor records remain only in processing, but they are not architect-authored presentation absent from maintained state. |
| **D. Not actually Class 3** | Exact pre-Home/intermediate states and their preservation checks; semantic-census expected population; generation-era “historical”, “oversized” and “stale” labels as historical facts; saved regression results; source/authority pins; original-source/recovery/transport/execution assertions. These are fixtures, recovery, historical validation or continuity, as specified above. |

These classifications neither retire an existing check nor authorise replacing its operands. Content sufficiency and existing operational dependence are separate findings.

## 10. Smallest genuinely missing governed presentation set

**Empty set.** There is no identified governed presentation item requiring preservation **outside the existing maintained state** to support the examined current architecture workflow.

For clarity, information not independently duplicated as maintained current configuration includes the exact 765-entry recovery role/key registry, the 62-case historical migration label roster, old oversized/stale classifications, the standalone frozen census, and the exact complete pre-Home comparison state. Those are precisely bounded generation/history/expectation gaps, not Bucket B presentation gaps. They do not justify either creating new presentation configuration or assuming the current loaders are independent.

## 11. Uncertainties requiring Solution Architect adjudication

| Uncertainty | Boundary preserved by this analysis |
| --- | --- |
| Continuing standing of earlier generated-View selection | The source inventory described saved selections as effective configuration. Inspection shows a narrower split between generation scope, expected results and historical labels. Any claim that these migration-era rosters impose continuing architect-authored obligations beyond preserving all existing Views requires explicit authority. No such extra obligation was established here. |
| More-than-20-child and diagnostic presentation | Maintained governance still leaves the >20-child archetype unresolved; the diagnostic remains preserved. This task makes no new layout decision. Their actual existing presentation is already maintained, so these unresolved future decisions do not populate Bucket B. |
| Historical exactness versus current presentation | Reproducing pre-migration coordinates, original whitespace, original export order or an exact old workspace is a distinct historical requirement. No implicit perpetual current-presentation requirement was inferred. Any additional requirement must identify its governed meaning and scope. |

There is no unresolved **observed missing current presentation item** to submit for adjudication. The operational source contract and release applicability remain outside scope; this report neither selects a new source nor proposes a framework or cleanup design.

## 12. Verification and unchanged-state boundary

| Check | Result and limitation |
| --- | --- |
| Static consumer/field trace | Completed for B16/B17 and inherited presentation portions, including generated descriptor creation/consumption and complete versus partial navigation projection. Static reachability is not a successful invocation of release-qualified commands. |
| Read-only XML/JSON comparison | Completed with fresh counts, full ordered subtree comparisons, serialized subtree comparisons, reference/property-definition checks and selected gzip reads. No regenerated presentation or new evidence result was written into the repository. |
| Older-source comparison | Confirmed all recovered View/node/connection IDs remain, all recovered node dimensions remain, documentation equivalence is precisely qualified, and historical source bodies have exact governed 1.0.6 counterparts. Superseded coordinates and the revised InformationMap placeholder were not misreported as missing current intent. |
| Required repository architecture suite | **PASS** — `timeout --signal=TERM --kill-after=10s 600s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`; BUILD SUCCESS; **90 tests, zero failures/errors/skips**, all 36 selected reactor modules successful; Maven elapsed **17.618 seconds**, finished **2026-10-20 16:17:40 Australia/Sydney**. This checks JVM invariants, not historical-processing independence. |
| Architecture machinery execution | No publisher, layout generator, candidate generation, historical verifier, optional replay or framework regression suite was run. No fresh Archi round-trip or human rendering acceptance is claimed. |
| Mutation boundary | The only authored repository file is this report. Existing governance/release changes and untracked reports/submission were preserved. Framework, tests, governance, release, exchange XML and historical processing/evidence remain unchanged. A final hash comparison covers 1,858 input/boundary files and the pre-existing tracked diff. Maven produced ordinary build/test artefacts only. |

No implementation, migration, presentation regeneration, evidence packaging or cleanup is recommended or authorised here. Analysis stops with this report.

## 13. Separate answers

**Does current Harmonia operation require architect-authored presentation information that exists only in historical processing material?**

**No, within the dependencies examined.** The complete accepted presentation is already in governed RAD-1.1.0 and bounded maintained View captures. Historical-only generation descriptors and earlier comparison states are not missing current architect-authored presentation.

**Does current Harmonia machinery still depend on historical processing material to preserve or reconstruct presentation?**

**Yes.** Its maintained release adapter still sources complete Views/navigation from a historical candidate; the fallback still reads the recovered model/crosswalk and historical selection/census; historical delta/regression checks still read earlier presentation operands. The information's presence in maintained state has not changed those dependencies. Existing release/authority applicability limits remain separate, and no unmodified-command independence is claimed.
