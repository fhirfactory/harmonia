<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Information Architecture machinery-validation run-002 — contained-leaf Element folders

## Result and inspection boundary

The governed **Harmonia ArchiMate Round-Trip Engineering Praxis** is established in
[RADS](../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md),
and Run-002 now regenerates the complete Element navigation population from the
shared state-filtered containment model and governed collection classifications.
The remaining Element projection defect is corrected: **every element
participating in resolved containment has its own context folder, including
contained leaves, with its own element reference inside**. Child folders nest
recursively. The complete Relationship navigation remains exactly unchanged.

The preceding Run-002 policy was: “Composition/Home parents with children receive
context folders containing themselves and direct leaves; deeper contexts nest
recursively.” That direct-leaf rule was the defect. It is superseded by the
folder-per-contained-element rule in the governed Praxis and navigation convention;
the shared containment resolver and collection classification are unchanged.

Earlier in Run-002, before the complete-population projection correction,
the runner regenerated only the 86 Information Architecture Elements;
it retained other Element placement and required incoming collection scaffolding.
Retained folders could not prove complete generation. The correction is in shared
framework capabilities, context and regressions, rather than an IA-specific fix.

The principal [candidate.xml](../../docs/archimate/processing/machinery-validation/information-architecture/run-002/candidate.xml)
is plain Open Exchange XML, ready for Solution Architect inspection in Archi.
It remains **PROPOSED; NOT ACCEPTED; NOT A RAD RELEASE**. Its exact hash, size and
final test results are recorded in the validation section below.

This remains **Run-002** against **RAD-1.0.10**, predecessor **1.0.9**. No Run-003,
RAD-1.1.0, final reconciliation, canonical-ID allocation, Documentation cleanup,
Archi import/round-trip or architectural acceptance was performed.

## Repository discovery, authority and checkpoints

| Required part | Established location and behaviour |
| --- | --- |
| Praxis definition | [archimate-round-trip-engineering-praxis.md](../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md) defines purpose, actors, authority, stages, inputs/outputs, checkpoints, stop/recovery conditions and release management. It composes existing contracts instead of reproducing a procedural script. |
| Agent discovery | Root [AGENTS.md](../../AGENTS.md#archimate-round-trip-task-entry), [governance index](../../docs/markdown/governance/README.md), [architect entry](../../docs/archimate/README.md#round-trip-engineering-requests), existing authoring Praxis and framework README link to the maintained RADS contract. No conversation or Junie report is an architectural prerequisite. |
| Review trigger | `Model Updated. Please review and generate change-review.md.` The agent discovers exact baseline/export provenance, validates, observes and prepares independently adjudicable CRs under the existing template. Explicit locators resolve ambiguous inputs; timestamps and backup filenames do not supply authority. |
| Change Review checkpoint | Human `change-review.md`: changed/unchanged architecture, new/deleted/evolved elements, preserved/rebound identities, relationships, derived consequences, metadata/navigation, Views, validation, ambiguities, adjudication and proposed deterministic directives. Default On-Hold/Unresolved and Not authorised; detection applies nothing. |
| Directive/application trigger | `change-review.md reviewed and updated. Apply directives.` The reviewed document supplies human adjudication, subject to exact input applicability, decision-linked instructions, prerequisites, resulting state and higher authority. Ambiguous/inconsistent directives stop affected execution. |
| Application checkpoint | `roundtrip_praxis.inspect_review` pins the reviewed bytes to the baseline/export hashes. The authorised agent must then validate the actual CR instructions, dependencies and expected state under the existing Change Review contract. Hash applicability alone never authorises execution. |
| Release boundary | The governed release policy's existing RAD-1.0.10 exception includes supporting Praxis/governance changes. Processing attempts consume no RAD versions. Actual Archi proof and Solution Architect acceptance precede the separately governed publication/release transition. |

AX-04, AX-14, AX-15 and AX-17 materially govern this work: reuse suitable
machinery; preserve semantics/representation/projection/presentation and
execution-result distinctions; preserve uncertain effects; and reserve
architectural adjudication to the Solution Architect. The current 2026-10-20
clarification is recorded in RADS, with explicit precedence over earlier broad
Composition wording and earlier preservation of unresolved navigation locations.
No legacy architectural description was promoted into RADS without review.

The linked historical `ingestion/1.0.8/run-002/change-review.md` remains a different
review transaction. It was read for context and not modified or applied to this
Information Architecture specimen. A reviewed filename from another transaction
cannot confer applicability to the RAD-1.0.10 input pair.

## Shared framework and current interpretation

| Component | Reused or extended responsibility |
| --- | --- |
| `roundtrip_praxis.py` | Reusable unique discovery, comprehensive read-only comparator/intake orchestration and reviewed-document applicability checkpoint; identifies the review contract, template and required evidence. It does not guess CR granularity or parse prose into architectural authority. |
| `diagnose_roundtrip.py`, `change_review.py`, `element_correlation.py` | Existing offline XML/reference checks, exhaustive representation/presentation comparison, canonical-first continuity and rebinding evidence. Native Composition observations remain distinct from effective current containment. |
| `relationship_state.py`, `containment.py` | Existing shared standing selection, type-selected Home/Composition resolution and single ancestry engine. Run-002 resolves once and passes that model to its consumers. |
| `governed_metadata.py`, `information_naming.py` | Existing lexical/qualified-name machinery and authored native type/Name/specialisation interpretation. Submitted derived claims remain evidence; they do not override semantics. Run-001 proposed metadata is still reproduced byte for byte. |
| `workspace_policy.py` | Collection classification from resource-owned RADS records by canonical identity. Current semantic ancestors take precedence over stale recorded category/owner context. Recorded context may recover a collection category only; it never inserts an absent current parent. New unmapped classifications stop explicitly. |
| `workspace_navigation.py` | Shared projector now gives all resolved containment participants their own context, including 691 contained leaves. Existing deterministic identity keys, collection/role placement and standalone policy are retained. Relationship source paths and projection identities are preserved exactly. |
| `run_evidence.py` | Common create/check/guarded-correction writer. Checks archived manifest and every original generated output before any correction write, rejects stale archives/path escapes and performs no release allocation. The former one-time `--correct-run-002` branch is replaced by reusable `--before-archive`. |
| `governance_history.py`, `governance-transitions.json` | Exact, registered before/after governance transitions with hash-verified snapshots; frozen source pins remain unchanged. Historical replay accepts only the recorded chain and rejects arbitrary edits or snapshot drift. |
| Existing resource/reconciliation/publication tools | Retain their bounded accepted scopes. They are adapters available to the governed agent, not a universal unattended reconciliation engine. |

**Home is the default**, explicitly directed native Association specialised as
Home, contained element → home. **Composition exceptions are selected by the
contained element's native type:** Capability (including Feature), BusinessObject,
DataObject (including Topic), Artifact. Feature inherits Capability; explicit Topic
specialisation inherits DataObject. Bracketed labels, stale derived Properties,
ordinary Association, Realization, folders and Views establish no containment.
Current/historical relationship standing is resolved before ancestry.

The specimen still selects **212 Home** and **632 Composition** relationships,
excludes **121 preserved historical assertions** and has **zero current
containment findings**. All assertions remain in XML. The old 61 direct / 109
propagated findings remain resolved by historical filtering; no parent is chosen
from competing current assertions. Conditional proposals are not accepted facts.

## Generated Element and source-organised Relationship navigation

The complete **1,394-element** population is generated. The immutable export's
Element or Relationship folders supply neither ancestry nor a generation oracle.
Collections use governed native/category/standing roles. Every containment
participant receives its own same-named folder containing its own reference;
contained children receive nested folders, recursively, including leaves.
Business Home projections retain the Business
context and Functions/Processes/Services roles. A missing current Feature parent
or Business Home is exposed under `Unresolved Feature Ancestry` or `Unestablished
Home`, respectively. These navigation labels assert no semantic parent or root.
Existing names and relationship standing remain unchanged by that projection.

In particular, the existing 173 Feature elements without native Composition are
not silently attached through their recorded owners. The 111 Business behaviours
without explicit Home are not given inferred Homes or current owner containment.
Their prior source-qualified names remain unchanged historical naming evidence.
Collection classification and actual current containment remain separate.

| Generated measure | Result |
| --- | ---: |
| Element navigation references / retained Element references | 1,394 / 0 |
| Elements with selected current containment | 844 |
| Containment roots with children / uncontained standalone Elements | 149 / 401 |
| Maximum selected containment depth, edges | 4 |
| Containment participants with own contexts | 993 |
| Contained leaves with own folders, newly supplied | 691 |
| Element context/role folders, including collection-context mirrors | 1,317 (previously 626) |
| Total Element navigation folders, including 32 collection/scaffold folders | 1,349 (previously 658) |
| Relationship-source contexts | 493 |
| Relationship mirror folders | 538 |
| Relationship references, exactly once | 1,137 |
| Existing Views, preserved | 1,209 |

Legacy JSON summary keys named `composition_*` now describe the shared selected
containment paths; they are retained for evidence compatibility and do not imply
Composition for Home-governed elements or semantic root completeness.

The 86-element IA subset retains its intended Composition hierarchy, including
the Individual parent/self and Authority, Contact, Demographics, Identity,
Patient, Person and Relationship children. CommunicationAddress follows Workforce
Composition; Equipment Aggregation establishes no parent. The collection anchors
remain Business/BusinessObjectMap, Application/InformationMap and
Technology & Physical/DataMap. There is no Artifact population.

Relationships remain source-organised for all 1,137 native records, including
historical ones. Their paths mirror the actual source's generated Element location;
source contexts are reused and leaf sources retain their existing source-folder
identities. Type and target choose no home. The complete serialized Relations
subtree and all relationship-plan entries exactly match the preceding Run-002
candidate. Its XML-content signature SHA-256 is
`8d56e03718583b7590293f0357f209d365d676f8f379c150a72ff5d9fc3b398b`.
Projection IDs remain separate from
canonical architectural identity and allocate none.

## Proof of generation, semantics and Views

The reusable regression suite now asserts **final serialized XML**, independently
of the placement plan. Its explicit Care → Encounter assertion requires:

```text
Application/InformationMap/
  [Topic] Care/                    workspace-04179944a6c610d6a759c58485418bdd
    [Topic] Care                   id-395f82a0d8664955b6f542d80de3f5b5
    [Topic] Encounter/             workspace-f4d00a368ac3353024faed824799b563
      [Topic] Encounter            id-bcfd7137c33f4255ad3780826a64b4c5
```

Care's immediate references contain **only Care**; Encounter's folder contains
**only Encounter** as its immediate reference. Encounter is not a bare reference
in Care. The extracted final XML is retained as
[care-context.xml](../../docs/archimate/processing/machinery-validation/information-architecture/run-002/validation/contained-leaf-correction/care-context.xml).
The independent read-only
[verify_candidate.py](../../docs/archimate/processing/machinery-validation/information-architecture/run-002/validation/contained-leaf-correction/verify_candidate.py)
asserts this directly against the on-disk candidate and checks all 993 containment
participants for their own named folder and direct self-reference.

Synthetic final-serialization regressions cover Care/Encounter as a contained
leaf and then as a contained non-leaf with Child, plus a sibling Episode; an
eight-level Capability hierarchy; Home-governed leaf and non-leaf recursion;
governed Business Home roles; standalone policy; absent/misleading folders;
unchanged Relationship navigation; and preserved Views. No IA-specific branch
or second containment resolver was added. `containment.py`, `relationship_state.py`
and `workspace_policy.py` are byte-identical to the preceding state.

The durable rule is in
[the Praxis](../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md#common-interpretation-and-projection-rules)
and [navigation convention](../../docs/markdown/governance/archi-navigation-and-view-convention.md#current-round-trip-projection--2026-10-20),
with a framework README discovery link and a hash-verified governance transition
under `run-002/governance/contained-leaf-correction/`. This report supplies evidence,
not enduring architectural authority.

[Element generation proof](../../docs/archimate/processing/machinery-validation/information-architecture/run-002/element-generation-proof.json)
records two independent in-memory input variants:

1. Remove every Element and Relationship folder, collection anchor and navigation
   reference; keep the authored semantic architecture and Views.
2. Replace Element navigation with one deliberately misleading flat folder and
   omit Relationship navigation.

Both regenerate the **exact same complete organisation** as the principal
candidate, with 1,394 generated Element references. Protected content is unchanged.
This establishes generation from semantics/configuration; it does not infer
generation from structures already present in the submission. Regression tests
also change an actual relationship while retaining old folders and require the
new semantic hierarchy to change the generated output. The submission file is
never edited for these counterfactuals.

All **1,209 Views** survive with exact XML-content signatures, including identities,
membership, nodes/nesting, connections, order, layout/geometry, styles, annotations,
Documentation and residual governed content. The **complete Views navigation
subtree** is also unchanged. View signature SHA-256:
`7884117cd23076e1c21e69a886287e642e6ab76675a0767619e21488d05c146b`.
View-navigation signature SHA-256:
`863be1b0b47f7e273c1d5b9a9353c180eea6f1340748cb5b94875313531670ca`.
The independent final audit also establishes byte-identical serialized Views and
Views navigation subtrees against the preceding candidate, with SHA-256
`ddca4418ecb85bb73c67a4b1442ddecf17c43f9bcafe73a0edd489a225e28dc7`
and `5edd5a52016d3aafa7fccad5dd2c7d844b43f73839c1d5606d8912c019bedbf2`,
respectively. The former signature hashes exclude indentation and namespace-prefix
differences; the latter hashes cover the exact serialized subtrees.

The naming allowlist verifier restores only allowed metadata/Names and the
candidate marker to compare protected authored content. Restoring only the
original organisations then invokes that verifier against the immutable architect
submission. No semantic element, relationship, endpoint, direction, specialisation,
canonical identity, Documentation or View is changed by navigation generation.

The already-passing behaviours are retained: `[BusinessObject]` and `[Topic]`
naming; explicit Topic/native DataObject; **38 representation rebindings** with
canonical continuity; **1,362 canonical matches**; **32 absent new canonical IDs**
without allocation; four explicitly unresolved legacy DataObject classifications;
shared state-filtered containment; and actual-source relationship navigation.
Both Run-001 proposed metadata and rebinding evidence remain byte-identical.

## History and reproduction

Immediately before this contained-leaf correction, **180 files**, including every
generated output, its manifest, live machinery/governance, the preceding report
and validation manifest, were snapshotted under
[pre-contained-leaf-correction](../../docs/archimate/processing/machinery-validation/information-architecture/run-002/validation/pre-contained-leaf-correction/manifest.json).
The shared guarded writer verified every original generated output against that
archive before replacing Run-002 outputs. Earlier archives remain unchanged.
The intermediate corrected candidate and machinery are additionally retained as
97 files in `validation/pre-contained-leaf-regression-completion/`, before aligning
the inherited Home regression's expected folder path. The final guarded regeneration
updates that test snapshot without changing candidate XML.

The previous report and machinery/output state are preserved unchanged in
[pre-element-projection-correction](../../docs/archimate/processing/machinery-validation/information-architecture/run-002/validation/pre-element-projection-correction/manifest.json).
The earlier pre-containment archive and diagnostic are retained as history.
The first generated Element correction is additionally archived under
`validation/pre-context-completion/` before completing navigation-governance context
and the current-ancestor classification regression. `validation/pre-view-navigation-guard/`
also retains that generated state before adding the fail-closed guard against
discarding unestablished View navigation. All attempts remain Run-002.
No historical pin, input or Run-001 result was rewritten to make a check pass.

The old `validation/verify_containment_correction.py` and its logs describe the
previous byte-identical-candidate correction; they are historical, not the current
Element-generation acceptance check. Current verification uses `--check`, the
updated isolated replay, counterfactual generation proof and the regression suite.
Archived machinery includes tools, schemas, tests, governing context and collection
source snapshots. Isolated replay restores those sources/configuration without
importing live processing modules and compares candidate, metadata, containment,
navigation, rebinding identity fields and generation proof.

## Validation results

Candidate SHA-256: `541219c023d2cd507d591e81df9a0a82fbf1c8f4d324448a02bc476e00c3e59a`.
Size: **22,336,985 bytes**. Only Element navigation changes relative to the
preceding candidate (`a451a6358e9c28ee95a2f14e339c01024ed7ea955d978d9fedaf8619be04c73f`);
the Relations and Views subtrees remain exactly unchanged.

Potentially blocking verification commands used explicit TERM/KILL bounds from
the repository root. The final full suite loaded all final framework/test changes.

| Check | Result |
| --- | --- |
| `timeout --signal=TERM --kill-after=5s 600s python3 -B -m unittest discover -s docs/archimate/framework/tests -v` | **PASS — 317 tests, 345.246 seconds**, no failures/errors. |
| Final workspace, Home/Composition, standing, identity/naming, View preservation, governance-history and guarded-writer regressions | **PASS**, included in that complete final suite. |
| `timeout --signal=TERM --kill-after=5s 300s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` | **PASS — 90 tests**, no failures/errors/skips; Maven 18.289 seconds. |
| Guarded final Run-002 regeneration, 120-second bound | **PASS** — all archived output preconditions verified before replacement; the final inherited-test correction changes machinery evidence only, leaving candidate XML byte-identical. |
| Live `information_workspace_run.py --check`, 120-second bound | **PASS** — every generated byte and live machinery fingerprint reproduced. |
| Isolated `validation/reproduce_candidate.py`, 120-second bound | **PASS** — archived sources/configuration reproduce candidate, metadata, navigation, containment and counterfactual proof byte for byte; 38 rebinding identities preserved. |
| Final on-disk `validation/contained-leaf-correction/verify_candidate.py`, 30-second bound | **PASS** — direct Care/self → Encounter-folder/self XML assertion; own named folder and direct reference for all 993 containment participants. |
| Missing/misleading input-folder counterfactuals | **PASS** — both regenerate the exact same complete organisation with 1,394 Element references; immutable input untouched. |
| Relationship navigation | **PASS** — complete serialized Relations subtree, projection identities and all relationship-plan entries exactly unchanged. |
| Naming, type/specialisation, identity, rebinding, metadata and containment | **PASS** — protected content unchanged; metadata, resolved-containment and rebinding evidence byte-identical to the preceding candidate; Run-001 evidence unchanged. |
| Views and View navigation | **PASS** — all 1,209 Views and complete View navigation preserve exact XML-content signatures; serialized subtrees are byte-identical to the preceding candidate. |
| Offline XSD, identifiers/references, unique navigation and naming allowlist | **PASS**, in generation and replay. |
| Frozen inputs, all before-archives and machinery/output integrity | **PASS** — 28 protected file hashes, 95 generated outputs and 88 live/archived sources; all six before-archives verified (180, 97, 130, 92, 120 and 93 files). |
| Local report links and `git diff --check` | **PASS**. |
| Actual Archi import/rendering, complete-model transport and architectural acceptance | **PENDING SOLUTION ARCHITECT** — not performed or claimed. |

Two development invocations required regression-assertion corrections:
the first focused workspace run executed 36 tests in 35.765 seconds with one
failure in an updated assertion that incorrectly treated standalone elements as containment
participants; the first complete run executed 317 tests in 350.903 seconds with
one failure in an inherited Home test expecting a bare leaf. Their logs are
retained separately. Those assertions were aligned with the authorised policy,
then the complete final suite above passed. No production code was changed to
satisfy either assertion correction. No timeout or stall occurred.

Current logs, the extracted Care XML and the independent audit are under
[validation/contained-leaf-correction](../../docs/archimate/processing/machinery-validation/information-architecture/run-002/validation/contained-leaf-correction/).
The [current validation manifest](../../docs/archimate/processing/machinery-validation/information-architecture/run-002/validation/manifest.json)
pins the final candidate, report, checks and evidence. The
[final audit](../../docs/archimate/processing/machinery-validation/information-architecture/run-002/validation/contained-leaf-correction/final-candidate-audit.json)
records exact folder/reference counts, Care/Encounter folder and representation
identifiers, preservation hashes, counterfactual results and source/frozen/archive
integrity. Earlier `validation/praxis-correction/` results remain historical evidence
for the preceding candidate, preserved unchanged rather than represented as current
validation.

## Remaining two-trigger proof and stop point

The two triggers are now discoverable as a governed **agent-operated** workflow.
Discovery/intake, shared derivation, navigation generation and review applicability
have reusable tools and regression proof. The agent still decomposes architectural
CRs and checks reviewed prose under the governed contract; the helper deliberately
does not infer authority or implement a universal prose-to-reconciliation engine.
Existing reconciliation adapters are scope-specific. Future approved changes
outside their coverage require shared implementation/regressions or explicit
adjudication, not hidden run scripts or invented architectural meaning.

End-to-end directive application/publication for this Information Architecture
submission remains **unproven and unauthorised in this task**. The four legacy
DataObject classifications remain uncertain; 32 new canonical identities remain
unallocated. The Solution Architect must inspect the corrected candidate in Archi;
a later actual complete-model export must establish transport fidelity and the
required review, directive, validation and acceptance gates must complete before
final reconciliation/publication or RAD-1.1.0. No input modification or manual
repair of derived metadata is requested of the Solution Architect.

Stop here with the corrected Run-002 candidate ready for architectural inspection.
