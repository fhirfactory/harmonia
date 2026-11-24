------- Legacy Content - Superseded ------- Start ------
Superseded by:
[archimate-round-trip-engineering-praxis.md](../../../enterprise-architecture/governance/archimate-round-trip-engineering-praxis.md)
Current EA governance is maintained at the successor; original content below is unchanged historical material.
---------------------------------------------------------

> **RAD-1.1.5 canonical-source adapter:** The [continuum workbench procedure](../../../enterprise-architecture/design/archimate/README.md) implements the exact-baseline, review and application gates below for individual canonical Markdown resources. Existing standing, identity and native interpretation rules remain controlling. Historical source-adapter instructions remain applicable only to their originating editions.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia ArchiMate Round-Trip Engineering Praxis

## Purpose and authority

This Praxis is the reusable governed mechanism for reviewing, adjudicating,
reconciling and publishing Solution Architect changes made in Archi. It supplies
the context, expected results and checkpoints for authorised agents; framework
tools execute its stages. It introduces no runtime Energeia/Ponos component.
The [authoring lifecycle](architecture-authoring-praxis.md) still governs the
serial baseline → authoring → submission → next-publication lineage.

The Solution Architect's 2026-10-20 machinery-validation instruction establishes
this Praxis and the containment clarification below. Repository-held authority,
not conversation or a run report, governs subsequent sessions. AX-04 requires
reuse of suitable machinery; AX-14 separates semantics, representation,
projection and presentation; AX-15 preserves uncertain execution effects; AX-17
reserves architectural adjudication to the Solution Architect. The
[Change Review contract](archimate-ingestion-change-review.md),
[identity/naming contract](canonical-identity-and-naming.md),
[publication semantics](archimate-ingestion-publication-semantics.md) and
[release policy](architecture-release-policy.md) retain their respective roles.

[Architecture Change Provenance](../../../enterprise-architecture/governance/architecture-change-provenance.md) defines
durable transition evidence for this Praxis, as for Markdown/RADS and future
representations. Its adoption decision supersedes the permanent retention
interpretation of run snapshots and complete execution evidence. Detailed input
pins, previous reviews, machinery/configuration pins and before snapshots serve
active processing/recovery, validation and unresolved dependencies. Final closure
preserves the authorised change and decision under that contract, without
requiring the processing workspace to interpret them.

## Common HCRL integration — RAD-1.1.2

[Architecture Governance](../../../enterprise-architecture/governance/architecture-governance.md)
defines the release-level Decision Review Log and final semantic acceptance
record. This Praxis retains its specialised discovery, analysis, validation,
presentation and governed application checkpoints. Material findings requiring
adjudication feed the applicable HCRL DR with source review/CR correspondence;
supporting detail may be minutiae/evidence. A specialised `change-review.md` is
not a competing final acceptance record. Existing trigger vocabulary, exact-input
applicability, native semantics, identity, containment, navigation and View
preservation remain binding. No current model, input or processing path is moved.
Required transition evidence follows current provenance rather than a mandatory
archive package. Common completion requires Solution Architect confirmation of
the authorised and actually incorporated result.

## Actors and trigger vocabulary

| Actor | Authority and responsibility |
| --- | --- |
| Solution Architect | Authors architectural intent and Views; edits and adjudicates Change Requests and directives; supplies architectural acceptance and Archi inspection. |
| Codex, Junie or another authorised processing agent | Discovers exact inputs and governing contracts, assembles evidence, proposes deterministic consequences, executes reviewed directives within scope, validates and prepares publication. It cannot issue an architectural disposition. |
| Archi and repository framework | Workbench, interchange and repeatable processing machinery; successful execution establishes no architectural authority. |

The following short requests activate this Praxis without a restatement of its
mechanics. Export/review locators are optional when uniquely discoverable:

> Model Updated. Please review and generate change-review.md.

> change-review.md reviewed and updated. Apply directives.

The first trigger stops at the **Change Review checkpoint**. The second consumes
the reviewed document as human adjudication, checks its applicability and stops
at any missing/contradictory directive or unmet gate. An old reviewed document,
an On-Hold default, a generated proposal or an Outcome of Resolved is not a new
Accept decision. A filename alone is insufficient to select among multiple runs.

## Inputs, outputs and governed stages

| Stage | Required inputs and reusable machinery | Result and checkpoint |
| --- | --- | --- |
| Discover and pin | Root release authority; applicable published-package provenance; incoming landing area; submission/run manifests and any explicit trigger locator. `workspace_paths.py`, `diagnose_roundtrip.Exchange` and framework input hashing. | One exact baseline and immutable architect export, byte/XML hashes, scope and processing identity. Choose neither the newest timestamp nor a `.bak` by guesswork. Missing/competing provenance returns for adjudication. |
| Validate and observe | Secure XML parsing, bundled offline XSDs, identifier/reference checks, `diagnose_roundtrip.py`, `element_correlation.py`, `change_review.py`, `relationship_state.py`, `containment.py`. A no-edit control, where available, belongs to this exact baseline. | Raw representation findings and candidate architectural interpretation kept separately; canonical continuity and Exchange rebinding evidence; current/history selection before ancestry. Invalid input is reported, never repaired in place. |
| Prepare Change Review | Exact observations, governed contracts and [review template](templates/archimate-ingestion-change-review-template.md); `roundtrip_praxis.py` supplies reusable intake evidence and the checkpoint brief. Agent decomposes independently adjudicable changes under the existing CR contract. | **`change-review.md` is the human architectural checkpoint.** Include the contents below, stable CR IDs, default On-Hold/Unresolved and Not authorised. Detection creates no acceptance or reconciliation. |
| Read adjudication | Reviewed `change-review.md`, preserved previous review, pinned input hashes, decision-linked instructions, dependencies and target state. | **Directive/application checkpoint:** exact authorised scope and expected resulting state, or an explicit adjudication question before dependent mutation. The agent interprets the reviewed prose under the CR contract; no automatic prose-to-authority inference. |
| Apply and derive | Only approved directives; existing governed resource/identity machinery and scope-specific reconciler adapters; shared naming, containment and workspace projectors. | Proposed reconciled architecture, derived metadata and navigation, preservation evidence and per-CR processing outcomes. No run-specific semantic walker or silent identity allocation. Unsupported adapters require implementation and regression proof within authorised scope. |
| Validate and prove | Schema/references; independent semantic/identity/naming assertions; deterministic reprojection; `diagnose_roundtrip.py` and actual Archi import/export evidence. | Automated validation, preserved Views and a real complete-model Archi round trip with classified residuals. Import readiness, deterministic replay, actual transport and architectural acceptance are separate results. |
| Accept and publish | Successful applicable gates, Solution Architect acceptance and [release policy](architecture-release-policy.md). Existing publication/resource tooling and `scripts/architecture_release.py`. | Coherent governed RADS and exchange publication, accepted release allocation and retained evidence. Generation never increments a release. |

The review states what changed **and what remained unchanged**, preserved/rebound
identities, new/deleted/evolved architecture, relationship changes, derived
consequences, metadata/navigation consequences, View changes/preservation,
validation results, ambiguities and matters requiring adjudication. Propose
directives where existing authority makes the action deterministic, while retaining
the independent CR decisions and dependencies. Raw XML counts are not CR counts.
Where there is no transport control, transport origin remains unknown.

## Common interpretation and projection rules

**Current containment clarification — 2026-10-20.** For contextual resolution,
Home is the default. Home is the Harmonia specialisation of native ArchiMate
Association: explicitly directed **contained element → home**, with an explicit
`Specialization=Home` Property resolved by definition name. Ordinary Association,
its Name, Realization, folder placement or View nesting does not assert Home.

The **contained element's native type** selects the exceptions:

| Contained native type | Governing mechanism |
| --- | --- |
| Capability, including Feature | Composition, parent → child |
| BusinessObject | Composition, parent → child |
| DataObject, including Topic | Composition, parent → child |
| Artifact | Composition, parent → child |
| Other applicable native types | Home, contained element → home |

Feature inherits Capability's rule. Topic is native DataObject with explicit
Topic specialisation and inherits DataObject's rule. Derived `harmoniaType`, a
bracketed Name or a folder cannot override native type. This clarification narrows
the earlier cross-layer Composition wording; it does not convert, delete or
accept any relationship instance. Previously persisted names and source-qualified
context remain historical replay evidence, not substitute current containment.

Resolve current versus historical **relationship standing before ancestry**.
Preserved historical assertions stay in the model but cannot supply a current
parent. Unclassified submissions may support explicitly conditional previews;
currentness is not approval. Competing/duplicate parents, cycles, missing endpoints
and insufficient required ancestry remain findings, with no selected winner.
Absence of a parent proves neither completeness nor semantic root status.

Element and Relationship folders are derived navigation projections. Rebuild
declared populations from this shared resolution and governed collection/role
configuration, independently of submitted folder ancestry. **Every element
participating in resolved semantic containment receives its own same-named
Element navigation folder, including contained leaves.** Put the element's
single reference inside its corresponding folder and nest contained children's
folders recursively beneath the parent context, retaining governed collection
and role placement. Leaf status never determines folder eligibility. Elements
outside the resolved hierarchy retain their governed collection/standalone
policy; do not manufacture containment to obtain folders. For example, Topic
Care → Topic Encounter projects as `Care/Care` and `Care/Encounter/Encounter`
(folders followed by the element reference), never two bare references in Care.
Relationship navigation follows its actual
source's resulting path, reuses a source context where present and supplies a
source-element context otherwise. Relationship type/target does not select its
home. Each semantic element/relationship has one navigation reference. Unmapped
populations must be reported explicitly, never described as regenerated merely
because their incoming folders were retained. Collection anchors are presentation
configuration; they confer no architectural containment.

**All existing Views are preserved architect-authored presentation artefacts in
this round-trip process.** Preserve identity, membership, connections, nested
nodes, order, layout, geometry, style, Documentation and their navigation tree.
They are not disposable Element/Relationship folders. Earlier generated-View
publishers may replay frozen publications; they do not authorise regenerating an
existing submitted View. A separately reviewed directive is required to change one.

Canonical architectural identity and Exchange representation identity are distinct.
Unique established canonical identity preserves continuity across representation
rebinding; report old/new bindings and evidence. Allocate no identity merely to
make correlation or a projection succeed. Projection IDs occupy a separate
namespace. Naming derives from authored native type, Name, explicit specialisation
and governed context using the naming contract; stale submitted derived claims
remain evidence and do not override authored semantics. The architect is not
required to maintain machine metadata or alter an immutable submission for tooling.

## Stop, recovery and evidence

Stop dependent work for uncertain baseline/export/review applicability, ambiguous
identity, unestablished semantic interpretation, conflicting directives, unmet or
circular dependencies, required new naming/collection rules, destructive View
changes without authority, unresolved effects or missing proof. Record the exact
question and unaffected results. Do not change a Solution Architect disposition.
Use bounded commands and distinct PASS, FAIL, TIMEOUT, STALLED and UNRESOLVED
outcomes under repository instructions.

Machinery corrections belong in governed context, shared implementation and
regression/acceptance tests. Fixtures must distinguish preserved input, authored
semantics, generated metadata/navigation and preserved presentation. Demonstrate
folder generation with absent and misleading input folders, semantic-change
sensitivity and unchanged semantic/View signatures. Assert the final serialized
XML's parent/self and child-folder/child-reference structure, including contained
leaves, non-leaves, multiple levels and both Home and Composition; output presence
or arbitrary generated locations alone are insufficient. Secure before bytes
before explicitly authorised in-run correction and pin machinery/configuration
as needed for recovery and bounded read-only validation. These safeguards do not
require permanent machinery snapshots, logs or reproduction of every historical
execution. Record material validation conclusions and limits in the applicable HCRL DR
under Architecture Change Provenance. Missing originals or uncertain effects
block closure; a successful replay does not establish architectural acceptance.

## Runs and RAD releases

Processing identities (`run-001`, `run-002`, …) identify attempts, not releases
or necessarily architectural transitions. The following RAD-1.0.10/1.1.0
conditions describe the bounded historical proving/promotion cycle; they are
not the current release allocator or a new processing instruction.
The [bounded RAD-1.0.10 validation decision](architecture-release-policy.md#machinery-validation-runs--2026-10-20)
keeps baseline and architect submission immutable and the release unchanged,
including supporting Praxis/governance corrections. An explicitly authorised
Run-002 correction retains its before evidence; it does not start Run-003.
RAD-1.1.0 requires faithful processing, real Archi round-trip proof and the governed
acceptance/publication transition. Establishing this Praxis creates no release,
final reconciliation or acceptance. General future releases follow the existing
patch policy and explicit minor/major decisions.

Current operation must become independent of historical processing under
Architecture Change Provenance. Existing dependencies and recovery requirements
remain until separately authorised migration and verification resolve them.
Installing the contract neither demonstrates independence nor permits cleanup.

------- Legacy Content - Superseded ------- Finish ----
