<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Run-002 supplementary diagnostic — containment and workspace framework

**Standing: analysis for Solution Architect review; no architectural adjudication or machinery correction.**
This supplements the [Run-002 report](2026-10-20-information-architecture-machinery-run-002-workspace-organisation.md).
Only this report is added. Machinery, governance, tests, frozen inputs, existing
run evidence and candidates remain unchanged. Neither candidate was regenerated.

## Findings

Run-002 does **not** replace Home with Composition or relocate Home-governed
Business elements. It extends IA navigation, independently implements ancestry
already traversed for Run-001 naming, and rebuilds relationship navigation instead
of using the established projection helpers. Two concrete divergences require review:

1. Its model-wide diagnostic treats preserved historical Composition assertions
   as competing parents alongside current assertions. All **61** direct cases
   have exactly this explanation; the **109** findings include 48 descendants.
   These are not evidence of 61 unresolved competing *current* parents.
2. Its additional source-element folders change the established relationship-path
   formula and replace every Relations folder identifier. This retains actual
   source semantics but is an extension of the workspace contract, not merely
   reuse of the existing implementation.

The suspected explanation that these findings concern types outside the clarified
Composition rule is **not supported**: all 109 affected elements are Capability
elements, without Feature classification. The diagnostic is new and broader than
the IA projection; its relevant error is ignoring assertion currentness.

## Authority and historical boundaries

The relevant axioms are [AX-04, AX-14 and AX-17](../../docs/markdown/governance/architectural-axioms.md):
reuse suitable machinery, preserve semantic distinctions, and report uncertain or
conflicting authority without silently adjudicating it. RADS supplies authority;
historical reports and generated models supply implementation evidence only.

| Boundary | Evidence and behaviour |
| --- | --- |
| Pre-existing capability navigation | `generate_views.Model` originated in `8afd7f21` (2026-10-16); `publish_navigation.py` in `60175899` (2026-10-17), before their workspace move. Native Capability/Composition hierarchy traversal and source-based Relations navigation already existed. |
| Pre-existing Home and accepted-instance machinery | Commit `f40fad55` (2026-10-19) contains `home_relationships.py`, `home_context.py`, both Home test modules, accepted 1.0.8 replay and currentness-aware Capability containment View selection. |
| Pre-existing IA baseline | HEAD `6628a923` (2026-10-19) added the cross-layer Composition governance and initial IA population: 50 BusinessObjects, four DataObjects, zero Artifacts, flat collection placement and no new IA Composition. |
| Change Review before Run-001 | Uncommitted `element_correlation.py` / `change_review.py` and round-trip diagnostic integration were already present in Run-001's source fingerprints. They record native Composition and presentation separately; they are not Run-002 additions. |
| Run-001 | `information_naming.py` and its runner proposed naming for BusinessObject, explicitly specialised Topic and Artifact, traversing incoming Composition. Existing lexical/qualified-name machinery was reused; Artifact vocabulary and explicit Topic recognition were extended. Navigation was preserved. |
| Run-002 | The two additional tool fingerprints are `workspace_navigation.py` and `information_workspace_run.py`. They add IA element navigation, complete Relations reprojection and the non-IA diagnostic. All 35 Run-001 source fingerprints are identical in Run-002, including the naming, Home and old navigation tools. |

The working tree already contained the uncommitted runs and governance changes
when this investigation began. Attribution above uses Git history and the two
run manifests, not file modification dates. Run-001's governance archive preserves
the pre-naming-decision documents, with explicit before/after hashes in its
`governance/manifest.json`; those archived bytes are not the later decision text.

## Home: existing governance, serialization and implementation

The [modelling strategy](../../docs/markdown/governance/architecture-modelling-strategy.md#explicit-architectural-home)
defines **element → directed Association «Home» → logical architectural home**.
Home is an explicit logical-belonging assertion, distinct from native structural
Composition, Realization, responsibility, ownership and governance authority.
Folders, Views, names and even a sole Realization cannot establish Home.

The observed Open Exchange representation is equivalent to:

```xml
<propertyDefinitions>
  <propertyDefinition identifier="specialization" type="string">
    <name>Specialization</name>
  </propertyDefinition>
</propertyDefinitions>
<!-- Under relationships; usual Exchange and xsi namespaces apply. -->
<relationship identifier="home-id" source="element-id" target="home-element-id"
              xsi:type="Association" isDirected="true">
  <properties>
    <property propertyDefinitionRef="specialization"><value>Home</value></property>
  </properties>
</relationship>
```

`specialization` is the observed definition **identifier**, not a universally fixed
key. [`home_relationships.assertion` / `authored_homes`](../../docs/archimate/framework/tools/home_relationships.py)
resolve `propertyDefinitionRef` to definition name `Specialization` or
`specialization`; require one `Home` claim, native Association and explicit
direction (`true` or `1`); and ignore relationship Name as specialisation evidence.
This is not a native `Specialization` relationship or `xsi:type="Home"`.
General detection preserves multiple Home assertions for review; it imposes no
universal endpoint list or cardinality rule.
The existing [Home interchange tests](../../docs/archimate/framework/tests/test_home_relationships.py)
and [Home context tests](../../docs/archimate/framework/tests/test_home_context.py)
cover these distinctions and the placement behaviour below.

The bounded Business migration replays **208** accepted literal Home rows from
[`business-homes.json`](../../docs/markdown/governance/baseline/business-homes.json),
leaving 93 unestablished and 18 ambiguous cases untouched. Four separately authored
accepted Homes produce **212** total: 104 Functions, 99 Services and nine Processes.

[`home_context.derive`](../../docs/archimate/framework/tools/home_context.py)
already implements Home-derived naming and principal workspace placement for
BusinessFunction, BusinessProcess and BusinessService with one Home to a governed
Feature, native Capability. It reads governed semantic ancestry (`naming_context`,
or the source-qualified `parent_key` / Feature `owner_key`), not folders or an
unfiltered scan of every historical Composition. Both qualified names share this
context. Home overrides differing responsibility-derived projection without
changing responsibility. Multiple Homes, invalid targets and missing/cyclic
ancestry stop dependent derivation. These are bounded projection restrictions,
not exhaustive architectural restrictions on Home itself.

`home_context.project` moves the existing reference using `relocate` →
[`reconcile_108.add_reference`](../../docs/archimate/framework/tools/reconcile_108.py),
reusing context folders and stable identity-based conventions. It moves outgoing
relationships under `Relations` plus the source's resulting folder path. Persisted
resource amendments and independent audit enforce this behaviour. General Home
inheritance, exhaustive endpoint rules, IA Home rules and mandatory cardinality
remain explicitly deferred in RADS; no all-types Home placement engine was found.

## Composition, types and existing workspace machinery

The [cross-layer rule](../../docs/markdown/governance/architecture-modelling-strategy.md#cross-layer-semantic-containment-and-contextual-naming)
and [ingestion contract](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md#semantic-containment-and-governed-naming-context)
predate both validation runs. They describe Composition across layers, qualified
by **valid native endpoints and established/accepted Harmonia meaning**. Incoming
relationships remain proposals. The pre-existing framework is type- and
population-aware in several bounded paths, rather than one universal resolver:

| Native type / specialisation | Established behaviour before Run-002 |
| --- | --- |
| Capability, including Feature | `generate_views.Model` accepts only Capability elements and Composition relationships, requires same recorded category/layer, unique parent and acyclic hierarchy. Its `parents`/`children` drive `publish_navigation.ancestry` and folder generation. This is a bounded baseline reader, not an all-model semantic validator. |
| Capability / Feature in the synchronised model | `Baseline.chain`, `capability_path`, `feature_ancestry`, `feature_path` and `context_paths` use exact governed records and preserve current/historical standing. Fully framed Features require same-category current CT1 → CT2 → CT3 context. Feature is native Capability with Harmonia classification `harmoniaType=Feature`, not CT4. The [Business metamodel](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md#bec-composition-hierarchy) establishes accepted BEC CT3 → Feature Composition as containment. |
| Capability containment Views | `view_presentation.containment_candidates` selects native Capability → Capability Composition using current governed relationship records or accepted ingestion, current/accepted endpoints, declared View purpose and authored-View protection. Historical relationships do not enter the current population. This selects View membership; it is not a complete element-folder builder. |
| BusinessObject | Initial IA publication uses Business/BusinessObjectMap, without derived containment. Run-001 adds proposed Composition-qualified naming and `[BusinessObject]` display; it does not add element-folder projection. |
| DataObject, including Topic | Initial publication uses Application/InformationMap. Its four specific DataObjects have governed historical TOPIC vocabulary, without a general DataObject-to-Topic inference rule. Run-001 requires explicit element `Specialization=Topic` for new Topic interpretation; an unclassified DataObject remains unresolved for Topic naming. Run-001 traverses Composition for naming only. |
| Artifact | No initial or submitted physical population. Run-001 adds `[Artifact]` / `ARTIFACT` naming vocabulary and synthetic coverage. No pre-Run-002 Artifact Composition workspace projector was found. |

No pre-existing central restriction enumerating **exactly Capability,
BusinessObject, DataObject and Artifact** was found in the inspected framework or
governance. The current written cross-layer wording is broader than that explicit
list; native legality/accepted Harmonia meaning and local implementation filters
must not be mistaken for the newly clarified exhaustive rule. The clarification
is the Solution Architect's explicit task direction. Its relationship to that
broader wording is raised for review under AX-17, without amending governance.
No existing framework behaviour is declared wrong on that basis.
The same pre-run section calls absent direct Composition on 323 Business
behaviour elements “modelling debt”. That statement also requires review against
the clarified type boundary; it was not introduced by Run-002. The machinery
preserves those Business elements and their established Home projections.

For folders, [`publish_navigation.organizations`](../../docs/archimate/framework/tools/publish_navigation.py)
already mirrors Capability context into Elements, Relations and Views. The later
[`Baseline.organizations`](../../docs/archimate/framework/tools/synchronise_baseline.py)
uses stable subject identities and independent projection roles.
`reconcile_108` preserves accepted authored XML/Views and adds references through
existing folders; `home_context` overlays explicit Home; `information_population`
adds the bounded flat IA collections while protecting existing navigation.

The established synchronised/reconciled relationship formula is:

```text
relationship folder path = [Relations] + source element's containing folder path
```

`synchronise_baseline.validate`, `reconcile_108.validate`, `home_context.audit` and
their tests enforce source-based placement. Relationship type and target do not
select a different home. These mechanisms already predate Run-002.

## Run-002 comparison and classification

[`workspace_navigation.py`](../../docs/archimate/framework/tools/workspace_navigation.py)
rebuilds Elements navigation only for declared native BusinessObject, DataObject
and Artifact populations. It supplies same-named folders for Composition parents,
keeps leaves as references, and mirrors resulting source locations into Relations.
The caller's anchor map restricts the projected population; it is not an enforced
global semantic-type policy. Cross-scope edges stop this projector. A synthetic
eight-level Capability test proves traversal depth, not permission to replace the
governed CT1/CT2/CT3/Feature metamodel with eight Capability tiers.

| Concern | Classification and evidence |
| --- | --- |
| Existing Home semantics and Business element placement | **Preserved, indirectly reused through the submission.** All 212 Home XML relationship records are unchanged. All 212 Home-source paths match persisted Home amendments in both candidates. Every non-IA element path is unchanged. Run-002 does not invoke the Home resolver; it trusts retained non-IA presentation. This is not new Home validation, but no actual Home placement bypass is demonstrated for this specimen. |
| IA Composition-derived folders | **Extension needed for a previously absent IA workspace projection.** Reusing Home for Topic containment would change semantics. The old Capability-only reader and Capability View selector are not drop-in IA projectors. |
| Composition ancestry calculation | **Duplicated traversal.** Run-002 `composition_paths` independently reconstructs the ancestry already traversed by Run-001 `information_naming.propose`; its verifier reconstructs it again. It also overlaps older Capability ancestry machinery. Reusing Run-001 proposed metadata is real reuse; reimplementing the navigation ancestry is a separate fact. |
| XML, identity and preservation utilities | **Correct direct reuse.** Existing secure parsing/serialization, schema and identifier checks, `g.folder`, canonical correlation, Run-001 naming, rebinding evidence and protected-content checks are used. Canonical identities are not allocated. |
| Relationship navigation | **Existing rule duplicated and extended; existing projection helpers bypassed.** Run-002 replaces Relations wholesale, implements its own `locations`/projection IDs and does not use `add_reference` or the established relationship-path checks. The new source rule remains source-based, but adds a source-name folder unless the source is an IA Composition context generated in this run. Even existing Capability contexts receive another source folder. |
| Multiple-parent diagnostic | **New generic diagnostic that bypasses existing standing distinctions.** `containment_findings` walks all out-of-scope elements and every native Composition without consulting currentness, accepted-instance records, type applicability or Home. This is broader than the scoped IA element projection. |

The immutable incoming model has **715 Relations folders**; Run-002 has **1,210**,
with **zero retained Relations folder identifiers**. All **1,137 relationship paths**
change; all **212 Home relationships** cease to satisfy the exact prior folder
formula. For example, relationship `id-a71687a2b74654a0b50b9fb3c46da93e` gains
`/Receive Care Plan` after its existing `/Functions` folder. Composition
`id-34e77a9793715ec5858a067ca3831952` gains `/Health Service Operations Enablement`
after the already established `/[CT1] Health Service Operations Enablement` folder.

This is an **actual implementation/test-contract divergence**, although the broader
governance principle “relationships follow their source” is still satisfied.
Run-002's verifier expects its new extra-folder formula and therefore does not
demonstrate compatibility with the earlier exact checks. A review is needed to
decide the intended workspace extension; this report does not infer that a
source-based folder refinement changes semantic containment or is inherently invalid.

## Explanation of the 61 / 109 findings

Independent XML inspection found **676 Capability → Capability Compositions** in
both baseline and submission, plus **59 DataObject → DataObject Compositions**
authored in the submission. The same 61 multiple-parent targets already occur in
the published baseline. Each pair has `v0.2 / Assertion Status=Proposed` on both
edges, with distinct `v0.2 / Record State` values:

| Target example | Historical parent | Current parent |
| --- | --- | --- |
| Ward Operations (`id-014da71d575756afa0db8502a0d1c904`) | Care Setting Operations Enablement; relationship `id-638a21bc4763512d87b1eb0f695d01ad`, `preserved historical assertion` | Health Service Operations Enablement; relationship `id-34e77a9793715ec5858a067ca3831952`, `current` |
| Emergency Care Progression (`id-036bd5a9fa1b5fca9c3245abf3016f44`) | Facility & Episodic Care Integration; relationship `id-06b8c61e03d25d5f8f5ad41dc47160b0`, `preserved historical assertion` | Emergency Department Operations; relationship `id-5b0756c95a9653959f0d14d04254e522`, `current` |

Excluding explicitly preserved historical assertions leaves **zero multiple direct
Composition parents** in the submission. This observation does not promote any
Proposed current assertion to Approved or adjudicate the Capability hierarchy.
The pre-existing `SemanticContainmentTests.test_historical_native_composition_does_not_enter_current_population`
explicitly tests this current/historical distinction. Run-002 retains the semantic
records and non-IA homes, so its diagnostic does not actually override their
placement. Its description as unresolved containment uncertainty is nevertheless
unsupported without first accounting for the preserved historical standing.

## Smallest corrections for review — not performed

1. **Correct the diagnostic interpretation first.** Classify the 61 pairs and 48
   propagated findings as current-plus-preserved-history observations. Any future
   machinery change should reuse established currentness/accepted-instance
   distinctions while retaining newly submitted IA edges as proposals. Neither
   deleting historical relationships nor accepting incoming architecture is needed.
2. **Resolve the relationship-layout extension.** The smallest compatibility change
   would remove the additional source-name folder and use the established
   source-path/reference helpers, retaining reusable folders/identifiers. If the
   additional folders are desired, treat that as an explicit framework workspace
   extension and review its preservation contract; do not merely relax old tests.
3. **Avoid a second semantic policy.** Keep the IA folder capability, but have its
   navigation consume the same eligible Composition context as naming, with
   declared type applicability and preserved standing. Broader consolidation is
   optional follow-up, not a prerequisite for recognising these two concrete
   divergences. Do not turn the historical Capability-only reader into an
   all-types model or extend bounded Home projection to ungoverned types by inference.

The clarified Composition type set and the older cross-layer wording should be
recorded as a review item before any broader policy implementation. No change to
governance, tests, source architecture or frozen models is proposed for execution
within this diagnostic task.

## Verification and stop boundary

Existing tests were executed unchanged, with bounded commands:

| Check | Result |
| --- | --- |
| From `docs/archimate/framework/tests`: `timeout --signal=TERM --kill-after=5s 120s python3 -B -m unittest test_home_relationships test_home_context test_view_presentation.SemanticContainmentTests -v` | **PASS**, 28 tests, 36.132 seconds. Publication fixtures are generated in memory; neither validation-run candidate is regenerated or written. |
| From repository root: `timeout --signal=TERM --kill-after=5s 300s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` | **PASS**, 90 tests, zero failures/errors/skips; Maven 20.393 seconds. |
| Read-only XML census, currentness/property inspection and organisation-path comparison of published baseline, submission and both existing candidates | Established the counts, Home preservation and exact path differences above; no generator invocation. |
| Read-only manifest SHA-256 checks | All six Run-001 and 54 Run-002 recorded output hashes match; all 28 Run-002 frozen-file hashes match. Run-001/002 shared source fingerprints match. |

These passing tests establish existing behaviour; they do not approve the new
relationship layout or convert raw multiple-parent observations into current
architectural conflicts. No Run-003, reconciliation, candidate regeneration,
test expectation change or RAD-1.1.0 was undertaken. RAD remains 1.0.10 / 1.0.9
under the existing machinery-validation exception. **Stop for Solution Architect review.**
