<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Change Review Log — Architecture Governance

| Matter | Proposed governed transition | Supplied ARB adjudication | Application |
| --- | --- | --- | --- |
| [M01](#m01) | Clarify architectural acceptance authority | A01 — Accept with clarification | Not performed |
| [M02](#m02) | Establish the release-level HCRL | A02 — Accept with clarification | Not performed |
| [M03](#m03) | Replace repository and evidence structure | A03 — Accept with replacement design | Not performed |
| [M04](#m04) | Review a bounded Minutiae group explicitly | A04 — Accept with replacement design | Not performed |
| [M05](#m05) | Review every Harmonia change | A05 — Accept with replacement design | Not performed |
| [M06](#m06) | Align durable knowledge with ArchiMate | A06 — Accept with replacement design | Not performed |
| [M07](#m07) | Submit candidates using architectural judgement | A07 — Accept with replacement design | Not performed |
| [M08](#m08) | Derive navigation from structure and metamodel | A08 — Superseded by A03 and A06 | Not performed |
| [M09](#m09) | Preserve identity and explicit correspondence | A09 — Accept | Not performed |
| [M10](#m10) | Bind completion to authorised actual results | A10 — Accept with clarification | Not performed |
| [M11](#m11) | Adopt prospectively | A11 — Accept with replacement design | Not performed |
| [M12](#m12) | Surface implementation discoveries for adjudication | A12 — Accept with replacement design | Not performed |

## 1. Transition, standing and input authority

| Field | Recorded value |
| --- | --- |
| Prepared | 2026-10-21, Australia/Sydney; Codex, submitter |
| Record standing | Initial candidate HCRL; pending review of this translation and its proposed application |
| Originating release | **RAD-1.1.1**, as recorded in root `architecture-release.json`; its predecessor is RAD-1.1.0 |
| Proposed resulting release | **RAD-1.1.2**, the next patch under the current release policy |
| Inspected repository state | Commit `2cbd9ded6be9f38cb4f18fe2ec083a7375750945`, branch `ArchitecturalGovernance`; tracked workspace clean at entry |
| Additional supplied repository input | Existing untracked discovery report, separately identified below; not part of the committed baseline |
| Actual resulting governed state | Not established; no adoption, implementation, acceptance or release allocation performed |
| Iteration | `001`; a review iteration, independent of a release increment or processing run |
| Supplied decision authority | The user's explicit A01–A12 ARB adjudications, transcribed in [Appendix A](#adjudication-record), are authoritative task input |
| Final architectural authority | Mark Hunter, Solution Architect; agents cannot accept this translation, authorise its application or confirm architectural closure |
| Solution Architect Disposition on this translation | Not recorded; source adjudications are retained separately and have not been converted into application permission |
| Application authority in this task | Create this pending review artefact and the requested task report only; do not implement proposed governance or migration |
| Final closure confirmation | Not supplied; no completed HCRL exists for this proposed transition |

The supplied adjudications settle A01–A12. They supersede conflicting candidate
recommendations in the discovery report. This HCRL makes those decisions durable
as explicit input; it does not imply an independently observed ARB meeting,
ChatGPT concurrence or an additional Solution Architect signature. The decision
meeting/date was not separately supplied; 2026-10-21 is the task/transcription date.

The [discovery report][discovery] supplies findings, source leads and candidates,
not subsequent decision authority. Its SHA-256 at entry is
`8895c7696c423b715c6a9d50cd0b4a946d17b0f95d9ee00a70404ef1235a07b7`.
The root release file's SHA-256 is
`6423e6c3f4ce3392c219a657002a963c8d0c847819490fa0981f00e73cc06465`.
The commit identifies inspected content; it is not independent proof of human
acceptance of every assertion in that content.

The governing sources inspected for this bounded task are [root instructions][agents],
the [relevant axioms][axioms], [Architecture Change Provenance][provenance],
[release policy][release], [modelling strategy][modelling], the specialised
[Change Review contract][review], [Authoring Praxis][authoring],
[Round-Trip Praxis][roundtrip] and [identity contract][identity]. Current
governance remains installed at its existing paths. This new review location is
expressly authorised for this candidate; its creation does not migrate RADS or
make its contents approved architecture by location.

The existing patch policy supplies **1.1.2**, without inferring a minor/major
increment from the significance of the proposal. Root metadata stays at 1.1.1
during preparation. Before any eventual acceptance, recheck the immediate
predecessor, the exact source state and the target under that policy. If another
accepted change intervenes, reassess this transition and its approval applicability;
do not treat a filename change as renewed authorisation.

## 2. Bounded proposed transition and axiom consistency

The proposed release adopts the supplied Architecture Governance model: one
release-level semantic HCRL, explicit Solution Architect authority within ARB
review, review of every change, a bounded Minutiae control, the adjudicated
repository structure, ArchiMate-aligned durable knowledge, identity continuity,
human-confirmed immutable completion and prospective adoption. It records the
necessary reconciliation and application work below for review.

This iteration performs none of that application. It does not move existing
architecture, governance, ArchiMate or LaTeX material; revise contracts, agent
instructions, tooling or models; allocate identities; generate publications or
evidence archives; reconstruct history; or delete processing material. It creates
only this review path and the associated execution report. The separate current
Information Architecture WIP ingest is not an input and has not been inspected.

| Material axiom | Consistency of the proposed approach |
| --- | --- |
| AX-04 — Harmonia owns semantics; engines provide machinery | Reuse the existing review and provenance foundations and specialised ArchiMate mechanisms. File formats, folder layout and tooling do not establish acceptance. No new runtime service or HCRL engine is inferred. |
| AX-08 — Evidence records meaning | Retain the authorised semantic transition and required evidence; Git supplies source history. Do not preserve every processing event or implementation source merely because it participated. |
| AX-09 — Transient operational state | Distinguish working reviews and operational recovery needs from permanent architectural evidence. This proposal gives no disposal authority. |
| AX-14 — Preserve semantic distinctions | Keep source adjudication, candidate interpretation, classification, application permission, actual result, validation, release and publication distinct. Partial approval does not admit an artefact to the register. |
| AX-15 — Preserve uncertain effects | Unknown application effects cannot be reported as a successful transition. No application occurred in this task; future uncertainties require evidence or recorded treatment. |
| AX-17 — Authority and architectural uncertainty | Embed supplied decisions; expose translation/application gaps rather than inventing policy. Existing authority and preservation duties remain visible until their approved replacement is applied. |

No axiom amendment or runtime responsibility change is proposed. The existing
review/provenance capabilities can support the semantic model; adaptations need
bounded authorisation. Future checks can enforce placement, references, lineage
and immutability mechanically, but cannot supply architectural acceptance or
replace judgement about materiality. The concrete existing-governance conflicts
are enumerated in section 6; no code-derived architectural conflict is asserted.

## 3. Material matters and proposed application

The M-identifiers are scoped references within this HCRL, not canonical element
identities. For every matter below, **the source adjudication is supplied and
settled; the proposed translation/application has not been approved or executed**.
The common outcome reason is that this task authorises preparation only. Each
matter identifies its own application implications and verification expectations;
later Solution Architect dispositions must identify any accepted wording, scope,
conditions, exclusions and application permission explicitly.

<a id="m01"></a>
### M01 — Acceptance authority (A01)

**Proposed meaning:** ARB is the collaborative review/adjudication forum. Mark
Hunter, Solution Architect, holds final architectural decision and acceptance
authority. ChatGPT may challenge, recommend, concur or dissent, without independent
approval authority. Codex, Junie and other agents submit, analyse and implement
authorised work without approving architecture. Material peer disagreement may
be retained; consensus is desirable, not required. There is no vote, quorum or
majority mechanism; delegation needs a separate explicit governance decision.

**Application proposal:** Use **Solution Architect Disposition** in the common
record and align references in existing review/Praxis/agent guidance. Preserve
attributable decisions and useful material dissent. Do not introduce independent
Board approval, mandatory peer concurrence or implied delegation.

**Dependencies/check:** M02 and M10 carry these authority distinctions through
review and closure. Check every operative acceptance/closure statement against A01.

<a id="m02"></a>
### M02 — Single release-level semantic record (A02)

**Proposed meaning:** HCRL is the single authoritative semantic change/review
record for each governed RAD release transition, from its immediate governed
predecessor to the resulting release. Specialised reviews retain their authority
for discovery, analysis, validation and presentation, and feed material findings
requiring adjudication into HCRL. Supporting detail may be minutiae/evidence.
HCRL governs the release transition, not every edit or processing event.

**Application proposal:** Reconcile the existing representation-neutral
provenance contract and specialised review/Praxis/template references so they
produce one final semantic acceptance record. Preserve specialised findings,
review/CR references, validation gates and decision history without competing
final acceptance records. This is not permission to rename the ArchiMate template
and infer that all XML-specific semantics apply to every HCRL.

**Dependencies/check:** M01, M03, M04, M05 and M10. Trace each material producer
finding to its HCRL treatment and preserve the distinction between specialised
validation and final architectural acceptance.

<a id="m03"></a>
### M03 — Repository, review, register and evidence structure (A03)

**Proposed meaning:** Replace `docs/markdown` with `enterprise-architecture`,
using the exact structure in [A03](#a03). `continuum/` holds governed Enterprise/
Solution Architecture, represented in Markdown; its structure follows architectural
concepts. `continuum/documentation/published/` holds LaTeX publication material.
`governance/` governs the architecture itself. `design/` holds the design/change
lifecycle, with the existing ArchiMate strategy preserved beneath `design/archimate/`.

Unapproved and partially approved material remains in `design/review/<version>/`.
Only approved architectural artefacts/design records enter
`design/register/<version>/`; a partially approved artefact does not enter the
register. Required transition evidence belongs in
`design/evidence/<version>/before/` and `after/`. The version is the resulting
architecture release. Use `hcrl-<target-version>-<iteration>.pending.md` and
`hcrl-<target-version>-<iteration>.complete.md`; the completed HCRL belongs in the
register. The old mandatory `before.tar.gz`, `after.tar.gz`, `change-review.md`
package is superseded. Git remains source/version history; do not duplicate it
as architectural evidence merely for completeness.

**Application proposal:** Produce an explicit source-to-destination and reference/
consumer inventory before authorised migration. Preserve identity, standing,
specialised semantics and original legacy content/preservation requirements.
Update governed contracts and their consumers coherently. This review file's
location is the only part of the target structure established now; no register,
evidence, continuum, governance or ArchiMate subtree is created by this task.

**Dependencies/check:** M02, M06, M08, M09 and M11; U01–U03 below identify
undecided application details. Validate approved placement, before/after
correspondence and references. Supersession of the package contract does not
authorise historical conversion, loss of unique content or disposal.

<a id="m04"></a>
### M04 — Bounded Minutiae control (A04)

**Proposed meaning:** Submitters classify candidate supporting/minor matters using
judgement; ARB reviews that judgement through one bounded Minutiae group control:
`Minutiae-Accepted: Yes | No` and a Solution Architect / ARB `Comment`. Yes accepts
the disclosed group; No identifies correction, escalation or reclassification.
Silence supplies no acceptance, and agent classification has no approval authority.
Subsequent iterations incorporate adjudication; previous classifications inform
better judgement. Do not use `ACCEPTED_WITHOUT_COMMENT` as a per-item disposition.

**Application proposal:** Integrate this group control and bounded scope into
HCRL guidance, keeping material dispositions and application authority distinct.
The currently governed specialised four-disposition vocabulary is not silently
extended or historically rewritten.

**Dependencies/check:** M01, M02, M05 and M07. Verify that a recorded Yes applies
to an identifiable disclosed group and reviewed content, not later undisclosed
additions. Section 7 leaves the control unanswered for human review.

<a id="m05"></a>
### M05 — Architecture Review for every change (A05)

**Proposed meaning:** Every Harmonia change receives Architecture Review:
architecture, features, backlog, implementation, bugs, patches and refactoring.
Review does not imply material architectural effect. Submitters assess material
matters versus minutiae/non-material implementation change; ARB/SA adjudicates.
New or changed architectural meaning exposed in implementation is material HCRL
content, regardless of the work's label.

Task Details and Task Reports for significant collaborative work are useful
transition provenance, normally minutiae unless materially architectural. Git
remains authoritative for source-code history. Retain code as architectural
evidence only as a bounded exemplar materially explaining a decision, principle,
pattern, constraint or design.

**Application proposal:** Reconcile entry guidance and release/HCRL coverage for
all changes, including transitions containing only approved minutiae. Keep the
existing every-accepted-change patch rule; no release exemption, second allocator
or requirement to invent a material architectural delta is supplied.

**Dependencies/check:** M02, M04, M10–M12. Test the review model against a feature,
ordinary bug fix, governance change and newly discovered architectural issue;
each must have review treatment appropriate to its meaning.

<a id="m06"></a>
### M06 — ArchiMate-aligned durable knowledge (A06)

**Proposed meaning:** ArchiMate and governed Harmonia specialisations provide the
durable-knowledge taxonomy. The Enterprise Architecture Continuum SHALL align
with the ArchiMate metamodel. Specialisations reside within their governing native
concept's structure; there is no independent documentation taxonomy. Candidate
knowledge carries its proposed native-concept or governed-specialisation tag,
subject to ARB acceptance, reclassification or rejection. Accepted classification
determines its place in the continuum. New types/specialisations require explicit
adjudication; agents cannot create them to accommodate content. The continuum
and Archi model provide corresponding navigation over the same architecture.

**Application proposal:** Classify the supplied governance knowledge provisionally
using native concepts in section 4. Before moving existing architecture, establish
its reviewed concept/specialisation mapping and preserve unresolved classification.
The illustrative `<Axiom>`, `<DesignPattern>` and `<ModellingPattern>` tags in
A06 are not treated here as proof of an installed specialisation definition or
authority to create one. Existing specialisation/native semantics remain binding.

**Dependencies/check:** M03, M07–M09 and U02. Check native compatibility,
governed-specialisation authority and correspondence; do not infer Home,
Composition, ownership or new element identities from directories.

<a id="m07"></a>
### M07 — Judgement-led candidate submission (A07)

**Proposed meaning:** Publish candidate HCRLs using architectural judgement and
state uncertainty, including potentially material matters with uncertain
classification or interpretation. ARB adjudicates and corrections inform future
submissions. Learning never delegates architectural authority.

**Application proposal:** Retain enough exact proposed meaning, source comparison,
classification, effects and uncertainty to support adjudication, without inventing
a compulsory new knowledge schema or refusing useful submissions because their
classification is incomplete. This initial candidate follows that approach.

**Dependencies/check:** M01, M04 and M06. Ensure candidate wording and proposed
tags are distinguishable from accepted knowledge and later human corrections.

<a id="m08"></a>
### M08 — Navigation and Domain13 (A08, superseded by A03/A06)

**Proposed meaning:** A08 supplies no separate navigation mechanism. Navigation
follows the ArchiMate-aligned continuum and corresponding Archi model. Domain13
is not the Enterprise Architecture master catalogue; material ultimately belongs
according to its architectural concept, not historical documentation convenience.

**Application proposal:** Reconcile prospective Domain13/navigation statements
against A03/A06. In particular, the current [requirements catalogue][requirements]
assigns formal guardrails/ADRs/compliance to Domain13, while the [governance
index][governance] defines cross-domain governance. Preserve useful detailed
catalogues where appropriate; do not implement the discovery report's separate
knowledge-guide/master-index proposal or interpret supersession as blanket deletion.

**Dependencies/check:** M03, M06 and M09. Verify that navigation reflects reviewed
concepts and their model correspondence, without a new taxonomy or authority claim.

<a id="m09"></a>
### M09 — Identity and correspondence (A09)

**Proposed meaning:** Preserve existing canonical identities of governed
architectural things. Do not allocate canonical identities to every prose
statement or HCRL entry. Use scoped source/reference mechanisms where there is
no canonical element identity, and explicit correspondence where release
continuity needs it. Machinery convenience cannot justify manufactured identity.

**Application proposal:** Carry existing canonical identity and applicable
representation bindings through approved moves. Map old/new source paths and
specific sections for prose; distinguish relocation from semantic replacement.
Keep the existing identity contract's native/representation distinctions. No
IDs are allocated by this candidate, including to proposed durable statements.

**Dependencies/check:** M03, M06, M08 and M10. Verify unchanged canonical IDs
and reviewed continuity; names, folders and local M/K references are not IDs.

<a id="m10"></a>
### M10 — Completion, immutability and residual uncertainty (A10)

**Proposed meaning:** HCRL becomes complete when the Solution Architect confirms
it accurately represents both the authorised change and the result actually
incorporated. Explicit deferrals/exclusions do not prevent completion when their
treatment is recorded. A completed HCRL is immutable. A genuinely non-semantic
correction may use an attributable erratum; material correction/change requires
a subsequent governed HCRL transition. Agents report facts, not architectural closure.

**Application proposal:** Bind final decisions, actual source/result correspondence,
evidence and validation limits before requesting closure. Record attribution and
exact reviewed content. Proposed completion path for this target is
`enterprise-architecture/design/register/1.1.2/hcrl-1.1.2-<final-iteration>.complete.md`;
the final iteration is not assumed to be `001`. Do not create it now. The erratum
carrier and pending-iteration retention details remain U04, without weakening
immutability or inventing an additional closure authority.

**Dependencies/check:** M01–M03, M05, M09 and M11. Confirm actual results and
explicit treatment of residual matters; successful checks or a filename never
self-complete this HCRL.

<a id="m11"></a>
### M11 — Prospective adoption (A11)

**Proposed meaning:** HCRL governance begins prospectively with adoption of this
model. There is no blanket historical reconstruction or retrospective HCRL creation.
Specific later needs may justify bounded historical archaeology; preserve gaps
and uncertainty rather than inventing historical authority or correspondence.

**Application proposal:** Treat 1.1.1 → proposed 1.1.2 as the initial adoption
transition, subject to acceptance and predecessor recheck. Explicitly reconcile
the older provenance contract's intended separately authorised
1.0.10 → 1.1.0 migration/pilot reference with prospective adoption: it is not this
first HCRL, and no historical pilot/reconstruction is executed here. Do not alter
frozen records, retire dependencies or infer cleanup authority.

**Dependencies/check:** M02, M03, M10 and U05. The adoption event must be explicit
at acceptance; the preparation date alone is not the effective adoption date.

<a id="m12"></a>
### M12 — Implementation discoveries (A12)

**Proposed meaning:** Anyone authorised to implement Harmonia is expected to
recognise changes, contradictions, extensions, clarifications or other material
effects on architectural intent. Surface them through HCRL for ARB adjudication;
implementation discovers issues but cannot authorise architecture. Ordinary
detail without architectural effect needs normal HCRL/minutiae review, with no
separate material architectural treatment.

**Application proposal:** Align implementation/agent guidance with this discovery
responsibility and M05. A finding should distinguish a conformance defect,
unestablished meaning and a proposed change, using existing authority and explicit
uncertainty. Do not promote an implementation pattern or source-code snapshot
into accepted architecture merely because it exists.

**Dependencies/check:** M01, M02, M04, M05 and M07. Check that materially new
meaning returns for adjudication, while ordinary detail remains bounded minutiae.

## 4. Proposed durable architectural knowledge

These are proposed classifications of the supplied meaning, not new decisions or
canonical elements. Native `<Constraint>` tags describe normative limits;
`<Principle>` describes guidance for judgement. ARB may reclassify them. A record
form such as HCRL, a decision event or a document heading is not made into a new
ArchiMate type. No new Harmonia specialisation is proposed.

| Scoped reference | Proposed tag | Durable statement / owning matter | Proposed application home |
| --- | --- | --- | --- |
| K01 | `<Constraint>` | Final acceptance belongs to the Solution Architect; collaborative/agent participation supplies no independent approval (M01). | Architecture governance: common review/authority definition |
| K02 | `<Constraint>` | One HCRL records each authorised immediate governed release transition; specialised reviews feed it (M02). | Architecture governance: common review/provenance contract |
| K03 | `<Constraint>` | Apply the adjudicated repository structure, standing/placement rules, naming and evidence separation (M03). | Architecture governance: repository/provenance contract |
| K04 | `<Constraint>` | Accept only the explicitly disclosed bounded Minutiae group through the Yes/No control (M04). | Architecture governance: HCRL review control |
| K05 | `<Constraint>` | Every change receives review; materiality is adjudicated and Git retains code history (M05). | Architecture governance: review/release and contribution guidance |
| K06 | `<Constraint>` | Durable knowledge and navigation align with native ArchiMate concepts and governed specialisations; no invented types (M06/M08). | Architecture governance: modelling/continuum organisation contract |
| K07 | `<Principle>` | Exercise architectural judgement, expose uncertainty and improve through review without acquiring authority (M07). | Architecture governance: submission guidance |
| K08 | `<Constraint>` | Preserve canonical identities where established and use scoped references/correspondence elsewhere (M09). | Architecture governance: existing identity/provenance responsibilities |
| K09 | `<Constraint>` | Solution Architect confirmation binds authorised meaning and incorporated result; completed HCRLs are immutable (M10). | Architecture governance: closure/correction contract |
| K10 | `<Constraint>` | Adopt prospectively; specific historical investigation needs its own bounded authority (M11). | Architecture governance: adoption/provenance contract |
| K11 | `<Constraint>` | Surface implementation-discovered material intent through HCRL; implementation does not authorise it (M12). | Architecture governance: implementation/contribution guidance |

Under A03 these normative governance sources belong in
`enterprise-architecture/governance/`. Under A06 accepted knowledge must have its appropriate place and
corresponding navigation in the metamodel-aligned continuum/Archi model. The
precise relationship between those governance sources and concept-organised
continuum entries is **U02**, not an inferred duplication policy or an independent
governance taxonomy. No exact continuum folder scheme, new model element,
specialisation, UUID or assertion-level reclassification is installed here.

## 5. Proposed implementation/application actions and dependencies

These actions are proposed for review and later bounded authorisation; none is
an executable directive in this task. A01–A12 alone do not remove the explicit
instruction not to implement yet.

| Action | Proposed work and expected evidence of result | Prerequisites |
| --- | --- | --- |
| P01 — Review translation | Solution Architect reviews M01–M12, K01–K11, minutiae, unresolved details and the bounded application scope; record any corrections in the next pending iteration. | Supplied Appendix A; no invented acceptance |
| P02 — Establish migration mapping | Inventory affected governed sources, approved/candidate standing, native concepts/specialisations, exact destinations, references and tooling consumers. Identify original legacy sections needing preservation. | P01; M03/M06/M08/M09; U02/U03 |
| P03 — Reconcile common governance | Amend the maintained provenance/review/release/agent responsibilities to express the accepted model once; align specialised review/template/Praxis references. Preserve specialised semantics and historical issued decisions. | P01; M01–M05/M10–M12; U01/U04/U05 as applicable |
| P04 — Apply authorised structure | Move only the explicitly mapped content and update governed references/consumers; preserve the existing ArchiMate strategy beneath `design/archimate/`. Keep partially approved artefacts out of the register. | P02/P03; exact scope, before-state evidence and application permission |
| P05 — Adapt consumers and validate | Bound any path/tool/template adaptations; check links, lineage, classification, canonical identity, model/navigation correspondence, placement and applicable publication gates. Preserve unknowns and validation limitations. | P03/P04; explicitly authorised code/tool scope |
| P06 — Confirm and close | Reconcile actual changed-content/source correspondence and required evidence; record exclusions/deferrals; recheck release drift; obtain Solution Architect confirmation before completion/register placement and accepted patch update. | P05; M01/M10; required U-details resolved or explicitly excluded/deferred |

At eventual acceptance the normal release policy requires the patch increment
in the same accepted change set; publication generation never allocates it. The
pending artefact does not require a patch bump on every edit. No claim is made
that this review is ready for P04, or that all tooling adaptation can safely be
executed without the concrete mapping and bounded application scope.

## 6. Existing-governance reconciliation and unresolved application details

### 6.1 Known conflicts or required reconciliation

| Current source / rule | Supplied controlling decision and proposed treatment |
| --- | --- |
| Root AGENTS §1.2 and current governance index define RADS as `/docs/markdown`; current navigation, consumers and Praxes use that layout. | A03 replaces the structure; A06 determines the continuum organisation. Report the mismatch and migrate only through reviewed application. These sources are unchanged now. |
| Provenance **Governing rule**, **Definitions**, **Archive contents**, **change-review.md and authorisation** and lifecycle mandate the exact three-file package, full changed-file archives and `change-review.md`. | A02/A03 replace the final semantic record and package contract. Preserve meaningful before/after transition evidence under the new directories, without silently carrying forward compulsory tar files or inferring that evidence is unnecessary. U01 governs undecided selection/representation details. |
| Specialised review/Authoring/Round-Trip contracts and template use `change-review.md` as checkpoint and final record. | A02 retains specialised authority for its purposes but HCRL is the single final semantic review; A03 changes final placement/naming. Keep existing triggers/mechanics operational until an authorised compatible adaptation, without redesigning the ArchiMate strategy. |
| Specialised review uses four per-CR dispositions; discovery proposed per-item `ACCEPTED_WITHOUT_COMMENT`. | A04 specifies a single bounded Minutiae group control, not that extra per-item disposition. Preserve historical specialised decisions and do not manufacture new dispositions. |
| Discovery proposes an independent knowledge taxonomy and knowledge guide, preserving existing homes rather than restructuring. | A03/A06 replace that design; A08 is superseded. Do not implement those rejected candidate recommendations. |
| Requirements catalogue contains prospective Domain13 ownership/navigation statements. | Reconcile under A03/A06/A08; Domain13 does not become the EA master catalogue. Exact content placement follows reviewed architectural classification. |
| Provenance adoption identifies an intended separately authorised 1.0.10 → 1.1.0 historical migration/pilot. | A11 starts HCRL prospectively. Do not backfill or claim that historical transition is the first HCRL; retain/reconcile its separate limited dependency purpose explicitly (U05). |
| Release policy increments every accepted change and allows governance-only increments without regenerating frozen publications. | Compatible with A02/A05: every transition receives appropriate review, even if only minutiae. Keep the patch rule; no materiality-based release exception is supplied. |
| Existing legacy preservation, unique-content/recovery safeguards and specialised identity/native semantics. | No blanket override is supplied. Preserve these obligations; structure/package supersession does not itself authorise deletion, retrospective rewriting or new identities. |

### 6.2 Open details for review; no additional decisions inferred

| Reference | Remaining uncertainty / proposed handling | Dependency or completion effect |
| --- | --- | --- |
| U01 — Required evidence | A03 fixes evidence directories and supersedes the package. It does not specify the selection threshold, whole-file versus other representation, byte-preservation/hashing obligations or use of immutable Git locators for particular changed architectural content. Propose selecting only evidence needed to understand/verify the transition and preserve otherwise unavailable meaning; exact obligations need disposition before evidence-changing application. | M03/P03/P04/P06; no old tar mandate, automatic evidence waiver or new universal snapshot policy inferred |
| U02 — Concept mapping and governance relationship | A03 fixes governance's location; A06 fixes the metamodel taxonomy. Exact native-concept folder conventions, existing specialisation definitions applicable to all content and governance-source/continuum correspondence have not been adjudicated in detail. Section 4 tags are proposed, not an approved complete migration map. | M03/M06/M08; settle the affected mapping before moving or modelling those items |
| U03 — Migration extent and compatibility | Existing source, LaTeX/publication and ArchiMate consumer mappings have not been exhaustively inventoried. Exact source-to-target correspondence, rollout sequence and compatibility actions require a concrete bounded proposal; no migration of all `docs/` or all legacy LaTeX is assumed. | P02/P04/P05; does not reopen the A03 target structure |
| U04 — Iteration retention and erratum mechanics | A03/A10 establish naming, completion placement, immutability and an attributable non-semantic erratum. They do not define pending-iteration retention/disposal, iteration allocation checkpoints or the erratum carrier/location. Do not invent a retention mandate, deletion permission or edit-in-place exception. | M10/P03/P06; settle applicable mechanics or record an explicit bounded exclusion |
| U05 — Adoption and older pilot reference | Record the actual adoption/acceptance boundary and explicit treatment of the older separately authorised migration/pilot wording. The candidate target is prospective; no ARB meeting date or historical authority/correspondence is fabricated. | M11/P03/P06; historical work stays outside this transition unless separately authorised |

None of U01–U05 reopens the final-acceptance authority, target tree, taxonomy,
grouped Minutiae rule, review coverage or other settled adjudications. Explicit
deferrals/exclusions may permit completion under A10 when they accurately bound
the incorporated result; this agent makes no closure decision.

## 7. Minutiae — bounded candidate group for this iteration

The following group is submitted for review as supporting/minor provenance.
Classification is Codex's judgement and remains subject to ARB/SA adjudication.
The material governance meanings of these artefacts are already in M01–M12;
classifying their execution/provenance detail here does not demote those meanings.

| Scoped reference | Disclosed candidate minutia | Boundary |
| --- | --- | --- |
| N01 | Task Details: the user's bounded request to initialise this pending HCRL and report. | Preparation only; A01–A12 are material authoritative inputs, reproduced below, not accepted as minutiae by implication. |
| N02 | Existing discovery/design report, identified in section 1. | Findings/candidate provenance; its conflicting recommendations are superseded by Appendix A, not accepted by reference. |
| N03 | This pending submission's local identifiers, links, transcription, formatting and minimal review-directory scaffolding. | Does not classify M01–M12/K01–K11 as minor or authorise wider structure creation. |
| N04 | [Initialisation task report][initialisation], including factual validation and scope checks. | Working execution provenance; neither architectural authority nor an extra final acceptance record. |

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

The brackets are an unanswered control, not a third disposition. No Yes or No
has been supplied, and silence accepts nothing. A No identifies required
correction/escalation/reclassification through the human comment. Subsequent
iterations incorporate that adjudication.
The disclosed group is N01–N04 in this iteration; there is no open-ended group
or per-item `ACCEPTED_WITHOUT_COMMENT` disposition.

## 8. Actual preparation, evidence and closure state

| Content / activity | Before | Current preparation result |
| --- | --- | --- |
| This pending HCRL path | ABSENT | Added candidate review artefact only |
| `.junie/reports/2026-10-21-architecture-governance-hcrl-initialisation.md` | ABSENT | Added working task report |
| `architecture-release.json` | 1.1.1, previous 1.1.0 | Unchanged; 1.1.2 is proposed |
| Existing governed architecture/governance, model, publication and discovery report | Existing state at entry | Unchanged |
| Review/register/evidence/continuum migration | Not applied | Not applied; only ancestors necessary for this review path created |
| Resulting accepted changed-content inventory and before/after evidence | Not established | Pending future authorised application; proposed impact areas above are not a verified final inventory |
| Solution Architect acceptance, Minutiae acceptance and final completion | Not supplied for this translation | Not supplied; agent has not self-approved |

Preparation checks and their factual results are recorded in the linked task
report. They verify this bounded submission and existing conformance where
applicable; they do not prove adoption, implementation readiness, acceptance or
closure. No before/after package, model publication, register entry, historical
HCRL or implementation source copy is created. Required evidence for an actual
governed transition remains to be selected and established under the reviewed
application scope. The proposed transition is **pending**.

<a id="adjudication-record"></a>
## Appendix A. Supplied ARB adjudication record — authoritative task input

The following transcribes the supplied A01–A12 decisions; only Markdown layout
has been adapted. Their decision labels and terms are retained. They are the
input to this HCRL, not conclusions invented from the discovery report or session
memory. Authority and attribution limits are recorded in section 1.

<a id="a01"></a>
### A01 — Acceptance Authority

**ACCEPT WITH CLARIFICATION**

- The ARB is the collaborative architectural review and adjudication forum.
- Mark Hunter, as Solution Architect, holds final architectural decision and acceptance authority.
- ChatGPT acts as architecture peer/reviewer and may challenge, recommend, concur or dissent, but cannot independently approve.
- Codex, Junie and other agents may submit, analyse and implement authorised work but cannot approve architecture.
- Material peer disagreement may be retained where useful.
- Consensus is desirable but not required.
- There is no voting, quorum or majority mechanism.
- Delegation of Solution Architect authority requires a separate explicit governance decision.
- Use terminology such as Solution Architect Disposition, rather than implying independent Board approval authority.

<a id="a02"></a>
### A02 — Common Review / HCRL Authority

**ACCEPT WITH CLARIFICATION**

- HCRL is the single authoritative semantic change/review record for each governed RAD release transition.
- Specialised review mechanisms remain authoritative for their specialised discovery, analysis, validation and presentation purposes.
- Those mechanisms produce review material into HCRL; they are not competing final acceptance records.
- Material specialised-review findings requiring adjudication are represented in HCRL.
- Supporting/minor detail may be represented as minutiae/supporting evidence.
- A completed HCRL records the authorised semantic transition from the immediate governed predecessor to the resulting governed RAD release.
- HCRL governs the governed release transition, not every intermediate edit or processing event.

<a id="a03"></a>
### A03 — Repository, Review, Register and Evidence Structure

**ACCEPT WITH REPLACEMENT DESIGN**

Target structure:

```text
enterprise-architecture/
├── continuum/
│   ├── <Enterprise Architecture content>
│   └── documentation/
│       └── published/
│           └── <LaTeX publication>
├── design/
│   ├── archimate/
│   ├── review/
│   │   └── <version>/
│   ├── register/
│   │   └── <version>/
│   └── evidence/
│       └── <version>/
│           ├── before/
│           └── after/
└── governance/
```

- Replace `docs/markdown` with `enterprise-architecture`.
- `continuum/` contains the actual governed Enterprise/Solution Architecture.
- Markdown is the representation format but does not define the architectural structure.
- `continuum/documentation/published/` contains LaTeX publication material.
- `governance/` contains governance of the architecture itself.
- `design/` contains architecture design/change lifecycle material.
- Existing ArchiMate strategy remains authoritative beneath `design/archimate/`; do not redesign it merely as part of this restructuring.
- `design/review/<version>/` contains material not yet fully approved, including partially approved material requiring another iteration.
- `design/register/<version>/` contains approved architectural artefacts/design records for that version.
- Partially approved artefacts do not enter the register.
- `design/evidence/<version>/before/` and `after/` contain required transition evidence.
- `<version>` is the resulting architecture release.

HCRL naming:

```text
hcrl-<target-version>-<iteration>.pending.md
hcrl-<target-version>-<iteration>.complete.md
```

- The completed HCRL resides in the register.
- The old mandatory `before.tar.gz`, `after.tar.gz`, `change-review.md` package contract is superseded.
- Git remains source/version history; architectural evidence should not duplicate Git merely for completeness.

<a id="a04"></a>
### A04 — Minutiae

**ACCEPT WITH REPLACEMENT DESIGN**

Supporting/minor matters are presented as a bounded Minutiae section.
Use one ARB control:

```text
Minutiae-Accepted: Yes | No
Comment: <Solution Architect / ARB comment>
```

- Submitters use judgement to classify candidate minutiae.
- ARB reviews that judgement.
- Yes accepts the disclosed bounded group.
- No identifies required correction/escalation/reclassification.
- Silence never means acceptance.
- Agent classification never constitutes architectural authority.
- Subsequent HCRL iterations incorporate the adjudication.
- Agents should learn from previous classifications and improve future judgement.
- Do not use `ACCEPTED_WITHOUT_COMMENT` as a per-item disposition.

<a id="a05"></a>
### A05 — All Changes Receive Architecture Review

**ACCEPT WITH REPLACEMENT DESIGN**

- Every Harmonia change is subject to Architecture Review, including architecture changes, features, backlog items, implementation changes, bugs, patches and refactoring.
- Architecture Review does not imply every change has material architectural effect.
- Submitters use judgement to distinguish material architecture matters from minutiae/non-material implementation change.
- ARB/SA adjudicates that assessment.
- If implementation exposes new or changed architectural meaning, that becomes a material HCRL matter.
- No change bypasses Architecture Review merely because it is called a bug, patch, backlog item or implementation change.
- Task Details and Task Reports associated with significant collaborative work form useful transition provenance and normally appear as minutiae unless they contain material architectural meaning.
- Git is authoritative for source-code history.
- Do not reproduce implementation source in architectural evidence merely for historical completeness.
- Code may be retained as a bounded architectural exemplar only where it materially assists understanding of a decision, principle, pattern, constraint or design.

<a id="a06"></a>
### A06 — Durable Architectural Knowledge

**ACCEPT WITH REPLACEMENT DESIGN**

- ArchiMate together with governed Harmonia specialisations is the taxonomy for durable architectural knowledge.
- The Enterprise Architecture Continuum SHALL be organised in alignment with the ArchiMate metamodel.
- Harmonia specialisations reside within the structure of their governing ArchiMate concept.
- Do not invent an independent documentation taxonomy.
- Candidate durable knowledge is tagged using the applicable native ArchiMate concept or governed Harmonia specialisation, for example `<Constraint>`, `<Principle>`, `<Axiom>`, `<DesignPattern>`, `<ModellingPattern>`.
- The tag represents the submitter's proposed architectural classification.
- ARB may accept, reclassify or reject it.
- Once accepted, architectural classification determines its appropriate place in the ArchiMate-aligned continuum.
- Agents SHALL NOT invent new architectural types or Harmonia specialisations merely to accommodate information. A new type/specialisation is itself an architectural decision requiring adjudication.
- The continuum and Archi model should provide corresponding navigational structures over the same governed architecture.

<a id="a07"></a>
### A07 — Candidate Submission

**ACCEPT WITH REPLACEMENT DESIGN**

- Use architectural judgement.
- Publish the candidate HCRL and allow the ARB to adjudicate whether the submitter has classified or interpreted matters correctly.
- State uncertainty rather than withholding a potentially material matter.
- ARB corrections become precedent informing future agent judgement.
- Learning improves submission quality; it never delegates architectural authority.

<a id="a08"></a>
### A08 — Navigation / Domain13

**SUPERSEDED BY A03 AND A06**

- No separate architectural-knowledge navigation mechanism is required.
- Navigation follows the ArchiMate-aligned continuum and corresponding Archi model.
- Domain13 does not become the master index/catalogue for the Enterprise Architecture.
- Existing material should ultimately be located according to what architectural thing it is, not historical documentation convenience.

<a id="a09"></a>
### A09 — Identity and Correspondence

**ACCEPT**

- Preserve canonical identity for governed architectural things where it exists.
- Do not allocate canonical identities to every prose statement or HCRL entry.
- Use appropriate scoped source/reference mechanisms for knowledge without canonical element identity.
- Record explicit correspondence where continuity across releases requires it.
- Do not manufacture identity merely to simplify provenance machinery.

<a id="a10"></a>
### A10 — Completion and Residual Uncertainty

**ACCEPT WITH CLARIFICATION**

- An HCRL becomes complete when the Solution Architect confirms that it accurately represents both the authorised change and the result actually incorporated.
- Explicitly deferred or excluded matters do not prevent completion when their treatment is recorded.
- A completed HCRL is immutable.
- A genuinely non-semantic correction may be represented through an attributable erratum.
- A material correction/change requires a subsequent governed HCRL transition.
- Agents may report implementation/validation facts but cannot self-declare architectural closure.

<a id="a11"></a>
### A11 — Prospective Adoption

**ACCEPT WITH REPLACEMENT DESIGN**

- HCRL governance begins prospectively with adoption of this governance model.
- Do not perform blanket historical reconstruction or retrospective HCRL creation.
- Historical architecture/provenance may be investigated later when a specific need requires bounded historical archaeology.
- Preserve uncertainty and gaps rather than inventing retrospective authority or correspondence.

<a id="a12"></a>
### A12 — Implementation Discovery

**ACCEPT WITH REPLACEMENT DESIGN**

- Anyone authorised to implement Harmonia is expected to recognise when implementation changes, contradicts, extends, clarifies or otherwise materially affects architectural intent.
- Such discoveries SHALL be surfaced through HCRL for ARB adjudication rather than silently established through implementation.
- Ordinary implementation detail that does not affect architectural intent requires no separate material architectural treatment beyond normal HCRL/minutiae review.
- Implementation is a legitimate mechanism for discovering architectural issues; it is not an independent mechanism for authorising architecture.

## Source references

[agents]: ../../../../AGENTS.md
[axioms]: ../../../../docs/markdown/governance/architectural-axioms.md
[provenance]: ../../../../docs/markdown/governance/architecture-change-provenance.md
[release]: ../../../../docs/markdown/governance/architecture-release-policy.md
[modelling]: ../../../../docs/markdown/governance/architecture-modelling-strategy.md
[review]: ../../../../docs/markdown/governance/archimate-ingestion-change-review.md
[authoring]: ../../../../docs/markdown/governance/architecture-authoring-praxis.md
[roundtrip]: ../../../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md
[identity]: ../../../../docs/markdown/governance/canonical-identity-and-naming.md
[governance]: ../../../../docs/markdown/governance/README.md
[requirements]: ../../../../docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md
[discovery]: ../../../../.junie/reports/2026-10-21-architecture-governance-hcrl-discovery-and-design.md
[initialisation]: ../../../../.junie/reports/2026-10-21-architecture-governance-hcrl-initialisation.md
