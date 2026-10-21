<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Whole rejection of 001 and revised application authority | Rejected 001 as a whole; establish and apply the revised strategy | Operative Task, 2026-10-21T09:27:25.083Z; resulting architecture awaits holistic SA/ARB review. |
| [DR-2](#dr-2) | Element representation, identity, standing and incorporation Praxis | Establishment and application authorised by Task | Applied representation and checks below; holistic acceptance pending. |
| [DR-3](#dr-3) | RAD ingestion, reconciliation and source retirement | Application authorised by Task | Preserve authority and qualified assertions; defer only affected matters requiring new architectural decisions. |
| [DR-4](#dr-4) | ArchiMate, publication and reproducible machinery | Bounded extensions authorised by Task | Preserve projection/workbench roles and actual verification limits. |
| [DR-5](#dr-5) | Unresolved architectural questions and result acceptance | Result review pending | Agent cannot supply missing architectural decisions or confirm closure. |
| [DR-6](#dr-6) | Bounded Minutiae and formal activity evidence | Not recorded | Supporting edits and formal evidence disclosed below. |

# HCRL — RAD-1.1.5 element-oriented Enterprise Architecture Continuum

**Standing:** authorised implementation applied; submitted for holistic review, iteration **1.1.5-002**, transition
**RAD-1.1.4 → RAD-1.1.5**. Establishment and application are explicitly authorised;
holistic review of the incorporated result remains with Mark Hunter, Solution
Architect, and the ARB. Generation and validation confer no architectural approval.

The [operative Task](../../evidence/1.1.5/002/before/ai-agent-tasks/20261021T092725Z-establish-and-apply-element-continuum.md)
retains the full issued instruction and its attributable event time. Entry HEAD
is `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`, branch `ArchitecturalGovernance`,
entry root release 1.1.4 / previous 1.1.3; resulting root release 1.1.5 / previous 1.1.4. Prior iteration 001 and its evidence were
untracked inputs at entry and are preserved explicitly.

<a id="dr-1"></a>
## DR-1 — Whole rejection and revised authority

The Solution Architect rejects [1.1.5-001](hcrl-1.1.5-001.pending.md) **as a whole**.
Its investigation is not rejected; its premise that historical RAD documents
should generally remain architectural carriers is rejected. The former DR-1
acceptance and unanswered DR-2–DR-13 remain decision history, not operative
migration directives. No individual adjudication of those proposals is performed.

The new unit of continuum representation is the individual architectural element,
classified by the governed native ArchiMate metamodel and Harmonia specialisations.
The Task authorises establishing a deterministic representation and machinery,
then applying it to real RAD content, preserving identity, standing, meaningful
provenance and explicit uncertainty. It authorises retirement of successfully
ingested source carriers and bounded supporting governance/machinery changes.

AX-03/AX-04 retain native semantics and architecture's authority over machinery.
AX-08/AX-09 distinguish durable transformation evidence from transient processing.
AX-14 preserves identity roles, assertion standing, source history and projection
versus approval. AX-15 preserves unestablished outcomes; AX-17 limits deferral to
affected items requiring missing architectural authority. The change supplies no
new runtime boundary or metamodel concept. Existing naming and containment rules
control; paths and labels confer neither identity nor semantic relationships.

<a id="dr-2"></a>
## DR-2 — Canonical element representation and Praxis

**Applied under the operative Task; result acceptance pending.** The continuum is
one resource-owned UUID32 and one Markdown file per architectural element. Its
native classification selects a deterministic directory from the governed
ArchiMate type catalogue; Feature specialises Capability and Topic explicitly
specialises DataObject. Home retains its directed Association meaning and is
represented as an assertion, not an invented element. Grouping and Location are
native types, not document-aggregation categories.

The [representation contract](../../../governance/continuum-representation.md),
[incorporation Praxis](../../../governance/continuum-incorporation-praxis.md),
[base template and type catalogue](../../../governance/continuum/), and JSON schema
implement identification → classification → representation → identity → content →
provenance/standing → placement → validation. The catalogue covers **60 native
types**, of which **26** occur in the migrated population. One common Markdown
representation with native/specialisation constraints avoids a parallel taxonomy.

There are **1,559 canonical elements**, **1,137 native relationships**, and **462
qualified source predicates** retained without coercion to native edges. All
1,559 elements have provenance. **1,394 canonical identities are preserved**;
**165 are newly allocated** through bounded authorised ingestion. Three erroneous
unaccepted Role-attribute allocations were withdrawn and are retained as non-reuse
evidence. They are not fabricated historical architectural elements. The 50
accepted evolved source records retain the same canonical identities.

Existing naming/identity rules control. The governed native Name = displayName
rule corrects **841** differing prior native Names, with prior values retained.
Naming does not infer identity or acceptance. **164 canonicalName values remain
explicitly unresolved** for missing formal vocabulary, and **223 source records
have no established original definition**; historical absence is not filled by
inventing meaning. Currentness is independent of semantic standing: 1,158 current,
69 pending adjudication, 300 historical-reference and 32 superseded records.
[Detailed validation and exact type/standing counts](../../evidence/1.1.5/002/after/continuum-validation.json)
retain the source's full qualified standing text.

<a id="dr-3"></a>
## DR-3 — Ingestion, reconciliation and source retirement

**Applied to the actual RAD corpus.** Exact canonicalId continuity reconciles the
maintained captures and accepted ingestion records; document position, name
similarity and co-location supply no instance correspondence. Additional bounded
native definitions include strategic constructs, Business Roles/Interactions/
Collaborations, application responsibilities, services/interfaces and logical
information. U18/U19 and other adjudicated responsibility/semantic contracts are
incorporated as qualified contributions to their explicitly identified subjects.
F/D/M rows remain source assertions, not automatically new Features or objects.

The migration accounts for **179 original tracked RAD files**. **855 selected
source ranges across 57 files** retire from the active architectural role;
**20 whole carriers** retire, including 11 historical generated baseline carriers.
The [source succession](../../evidence/1.1.5/002/after/source-succession.json) records
exact replacements and source-edition offsets; [preservation validation](../../evidence/1.1.5/002/after/source-preservation-validation.json)
checks unchanged original text. Other passages retain their actual governance,
review, navigation, evidence or unresolved-source roles. No old source document
is relocated into the continuum as a substitute for decomposition.

The [corpus accounting](../../evidence/1.1.5/002/after/corpus-accounting.json),
[definition reconciliation](../../evidence/1.1.5/002/after/definition-ingestion.json),
[section correspondence](../../evidence/1.1.5/002/after/section-correspondence.json)
and [exact source ranges](../../evidence/1.1.5/002/after/source-range-correspondence.json)
identify that boundary. The last source-completion pass adds 509 exact contributions
through 452 source ranges to existing resources without allocation. Accepted
retypings retain earlier conceptual contributions as provenance; no speculative
BusinessObject counterpart is manufactured. Source conflicts/qualifications
remain visible. The runtime-AI optional-adjunct contribution is assigned to the
explicit Ergon subject, not the separately bounded AI Assurance Ergon.

This is a real migrated continuum with an explicit residual boundary, not a claim
that every unretired source assertion has been reconciled. DR-5 identifies the
remaining matters requiring architectural authority. No blanket migration of
legacy-doco or unrelated historical processing disposal is performed.

<a id="dr-4"></a>
## DR-4 — Projection, publication and reproducibility

**Applied and exercised.** The [review entry point](../../../README.md) exposes the
full-content HTML browser, canonical index, Exchange model and derived PDF catalogue.
The [Archi workbench](../../archimate/README.md) retains the human modelling and
review role. Its presentation template carries 1,209 existing Views, property
vocabulary and navigation, with empty semantic collections. Canonical Markdown
supplies elements and outgoing native relationships. Publication allocates no IDs.

The current pipeline passes schema, reference, placement, identity, provenance,
qualified-predicate, containment and independent information-name checks. All 83
information elements with established naming inputs reproduce their names; 11
unclassified DataObjects remain explicitly outside Topic derivation. Eleven
mutation/workbench/publication tests pass, including exact-baseline and stale-review
rejection, prospective validation, identity retry safety and zero-allocation checks.

**Actual Archi 5.10 import and complete-model export PASS** for the retained control:
all 1,559 canonical values/owners, native types, names and documentation; all 1,137
relationships; and all 1,209 Views survive. The canonical no-edit review detects
zero changes. [Transport evidence](../../evidence/1.1.5/002/after/archimate-validation.json)
preserves raw differences: language attributes and organization/navigation
transformations mean whole-model byte fidelity is **not** established. Existing
presentation is not replaced with the return. Full interactive visual review and
architectural acceptance remain distinct.

[Isolated regeneration PASS](../../evidence/1.1.5/002/after/reproducibility.json):
XML, HTML, Markdown index and TeX are byte-identical from maintained inputs without
historical captures, processing workspaces, copied LaTeX architecture or local
interaction logs. The generated 255-page PDF catalogue compiles and repeats byte
for byte in the fixed recorded environment; it is not a second narrative source.
The browser passes a DOM-contract smoke test; live-browser visual review is not
claimed. Rendered local links pass. Retained source succession is repeatable from
its exact entry Git edition; initial UUID allocation is not rerun as publication.

The required Maven architecture suite passes. The historical framework suite
already had 10 authority-pin errors at entry; two historical fixture classes in
the focused shared suite encounter the same AGENTS.md pin. These suites are
reported **FAIL**, separately from 45 executed shared semantic tests and the
passing current pipeline. Historical pins are not rewritten to manufacture a
PASS. [Validation evidence](../../evidence/1.1.5/002/after/validation-summary.json)
records commands, outcomes and limits.

### Authorised follow-up — root continuum review manifest

The Solution Architect's [2026-10-21T10:16:02.745Z instruction](../../evidence/1.1.5/002/before/ai-agent-tasks/20261021T101602Z-generate-continuum-review-manifest.md)
authorises the generated [root manifest](../../../continuum/MANIFEST.md) and its
deterministic generation/validation. It is a navigation/review projection, with no
independent architectural authority. All **1,559 elements** appear exactly once,
organised by the existing catalogue's native layer/aspect and type, then by
displayName. Each row exposes native type, specialisation, independent semantic
standing/currentness, canonicalName or explicit unresolved status, canonicalId,
linked canonical path and source qualifications. The **164 unresolved names** and
all historical/qualified populations remain visible. The manifest also exposes
zero counts for supported but unpopulated types.

The exact `continuum/MANIFEST.md` path joins the existing publication subtree as
a derived-output exclusion from element loading. Arbitrary other Markdown still
fails element validation. Generation validates canonical inputs; `--check` detects
missing/stale output without writing. The current Makefile and isolated generation
check include the manifest. No classification, identity, standing or relationship
is inferred or altered for presentation.

[Follow-up verification](../../evidence/1.1.5/002/after/manifest-validation.json)
passes: 16 continuum tests (including five manifest checks), all 1,595 rendered
local links, current generation/staleness checks and 90 Maven architecture tests.
[Isolated regeneration](../../evidence/1.1.5/002/after/manifest-reproducibility.json)
reproduces the manifest and existing generated publication bytes. All 1,559
canonical files and six existing publication artifacts are byte-identical to
this follow-up's entry state. This adds review navigation without changing the
migrated architecture, original verification limits or pending holistic acceptance.

<a id="dr-5"></a>
## DR-5 — Unresolved questions and holistic review

**Solution Architect/ARB disposition requested on the resulting architecture as a
whole.** No agent architectural closure is asserted. These affected matters remain
explicit; they have not halted independently supported ingestion:

| Question | Preserved boundary / decision needed |
| --- | --- |
| Q-01 | Formal canonical-name vocabulary and contextual inputs for newly represented native types; 164 unresolved canonicalNames. Do not invent qualifiers or root completeness. |
| Q-02 | 214 historical Capability references have no accepted current RAD crosswalk. The broader historical population retains source standing; 223 original definitions remain absent rather than inferred. |
| Q-03 | External governing-body interest versus operational Actor facets: the source expressly distinguishes these from enterprise Stakeholders. Classification/identity requires that distinction to be adjudicated. |
| Q-04 | Candidate CORE/SEC/PROV/INT/APP/DIR catalogue handoffs versus existing historical coded resources, including a DIR range and a layering-pattern entry: explicit owner/instance correspondence is required; equal codes/titles do not alone approve identity continuity or standing. |
| Q-05 | Remaining G2 candidates, assemblies, reified Relationship and later accepted authored Topics: retain exact known correspondences and accepted retypings; decide any additional same-instance/cross-level mapping. |
| Q-06 | Strategic construct facets versus ApplicationComponents, notably Digital Twin: no identity merge follows from shared terminology. |
| Q-07 | Ergo pattern, Erga collection, Pathway/Segment/Traversal/Route/Horos/Terma and composite control/support bundles: resolve the expressly open native classification/identity questions before materialising additional elements. |
| Q-08 | Unallocated F/D/M proposals and unresolved carrier/control facets: mapped assertions are preserved on existing subjects; a table row does not itself establish another architectural element. |
| Q-09 | Class/Package working DesignNote explicitly defers native classification. Older working notes retain their U01–U19 source/adjudication roles. |
| Q-10 | Remaining ownership/Service-consumption and cross-level assertion matrices require explicit instance/endpoint correspondence. Generic modelling rules remain governing. Labels cannot supply a crosswalk. |
| Q-11 | 462 qualified predicates have no accepted native relationship mapping. Preserve their source owners, target identities, standing and original predicate meanings until adjudicated. |
| Q-12 | Holistic acceptance, residual-source treatment and any future retyping/lineage or automatic relationship/View application scope. The bounded workbench adapter defers unsupported automatic application explicitly. |

[Corpus accounting](../../evidence/1.1.5/002/after/corpus-accounting.json) names affected
sources; [continuum validation](../../evidence/1.1.5/002/after/continuum-validation.json)
names affected canonical IDs/properties. Source-level Proposed, Candidate,
historical, superseded and unresolved assertions remain available. Native graph
validity and tooling PASS are not architectural approval. The maintained result is
mechanically reproducible and reviewable; full semantic reconciliation/stability
is qualified by these questions and final Solution Architect confirmation.

<a id="dr-6"></a>
## DR-6 — Bounded Minutiae and formal evidence

Minutiae-Accepted: No

Comment: No resulting-group disposition has been recorded. The operative Task
already authorises the work. Supporting navigation points agents to the same
individual axioms, canonical continuum and current pipeline. Existing governance
contracts retain their roles; historical documentation gets bounded succession
not retrospective rewriting. The root release is incremented once to 1.1.5 with
previous_version 1.1.4. Maven versions and runtime code remain unchanged.

Formal Tasks preserve the original instruction plus the holistic-review,
continuation and transient-retry follow-ups at their actual instruction-entry
timestamps. The final Report records factual outcomes and correspondence under
this iteration's after/ai-agent-reports location. Purpose-selected migration,
validation and actual Archi-return evidence support this HCRL. New source changes
remain in the working tree for review; no commit, push or architectural closure
is claimed. Completed historical HCRLs and the rejected 001 input remain preserved.

Formal Report: [applied migration and verification](../../evidence/1.1.5/002/after/ai-agent-reports/20261021T100913Z-element-continuum-applied-migration.md). Report production event: `2026-10-21T10:09:13.320Z`.

Initial migration evidence-layout validation: PASS (four Tasks, one Report). The post-emission timestamp-field formatting correction is retained as subsequent Report provenance; original event times and filename prefixes are unchanged. The later manifest Task/Report records the separate authorised follow-up without rewriting that original Report.

Manifest follow-up Report: [generated review manifest](../../evidence/1.1.5/002/after/ai-agent-reports/20261021T102230Z-continuum-review-manifest.md). Report production event: `2026-10-21T10:22:30.214Z`. The follow-up adds the exact root projection exclusion and its checks under DR-4; it does not amend the original migration Report or allocate another release.

Follow-up iteration-evidence validation: PASS (five Tasks, two Reports; no errors
or unresolved event-time/prefix correspondence). This mechanical check supplies
no architectural acceptance or attribution-authenticity adjudication.
