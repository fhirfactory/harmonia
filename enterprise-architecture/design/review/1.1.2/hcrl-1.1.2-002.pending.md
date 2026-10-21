<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Architectural decision authority and candidate judgement | Accept | Proposed incorporation faithfully represents the agreed architectural decision authority and candidate-judgement model. |
| [DR-2](#dr-2) | HCRL as the common Decision Review Log | Accept | Accepted. HCRL is the common Decision Review Log and authoritative semantic record for the governed RAD release transition. Specialised review mechanisms retain their specialised purpose and feed relevant review material into HCRL without creating competing final acceptance records. |
| [DR-3](#dr-3) | Repository structure and bounded migration | Accept |Accepted. The target Enterprise Architecture repository structure and its semantics are established. Migration into that structure must be based on a separately established bounded source-to-destination mapping that preserves standing, identity, correspondence and existing specialised semantics; acceptance of DR-3 does not itself authorise repository migration. |
| [DR-4](#dr-4) | Required transition evidence | Accepted | Accepted. Required transition evidence supports understanding, verification and preservation of the authorised architectural transition without duplicating source-control history or recreating the superseded mandatory archive package. Evidence selection and representation are governed by DR-4.01 and DR-4.02 respectively. |
| [DR-4.01](#dr-4-01) | Selection of before/after evidence | Accept | Accepted. Before/after evidence is selected where it materially contributes to understanding, verifying or preserving the authorised architectural transition. Evidence is not accumulated merely for historical completeness, and material already adequately preserved by source control need not be duplicated. |
| [DR-4.02](#dr-4-02) | Evidence representation and preservation |Accept with Clarification | Evidence SHALL preserve the architecturally relevant before/after state faithfully. Repository-controlled material may be identified and recovered through Git and does not require duplicate exact-byte archival or mandatory hashing. Where required evidence cannot be reliably recovered from source control, the retained evidence SHALL itself preserve the necessary state and correspondence. No universal hashing, archive format or exact-byte duplication requirement is established. |
| [DR-5](#dr-5) | Governance knowledge and the Enterprise Architecture Continuum | Accept | Accepted. Governance machinery, rules and context remain governance knowledge and do not require corresponding ArchiMate elements or continuum entries. ArchiMate classification and continuum placement apply where the knowledge is genuinely part of the Enterprise/Solution Architecture. |
| [DR-6](#dr-6) | Review of every change and implementation discoveries | Accept | Accepted. Every Harmonia change is subject to Architecture Review irrespective of its implementation classification. Review determines whether the change is architecturally material. Implementation may expose previously unidentified architectural implications, which must be surfaced for adjudication; implementation does not independently establish or authorise architectural intent. |
| [DR-7](#dr-7) | Identity and correspondence across the transition | Accept | Accepted. Preserve established architectural identity and record correspondence where required to maintain continuity across the transition. HCRL Decision Review references and governance/prose statements do not require canonical architectural identities. Correspondence SHALL reflect established architectural continuity and SHALL NOT be inferred merely from naming, location or tooling convenience. |
| [DR-8](#dr-8) | Completion, immutability and residual uncertainty |Accept | Accepted. HCRL completion requires confirmation that the authorised change and the resulting incorporated state correspond. Explicitly deferred or excluded matters do not prevent completion where their treatment is recorded. Completed HCRLs are immutable, subject to the deferred non-semantic erratum mechanism in DR-8.01 and the pending-iteration treatment established by DR-8.02. |
| [DR-8.01](#dr-8-01) | Attributable non-semantic erratum mechanics | Defer | The architectural rule is established: a completed HCRL is immutable; a genuinely non-semantic correction may be represented by an attributable erratum, while a material correction requires a subsequent governed HCRL transition. Detailed erratum mechanics SHALL be established when an actual requirement for an erratum arises rather than designing speculative machinery now. |
| [DR-8.02](#dr-8-02) | Pending-iteration checkpoints and retention | Accept with Clarification | Pending HCRL iterations are working review provenance and SHALL be retained while required to support the active review and its traceability. The completed HCRL is the durable authoritative record of the governed transition. No requirement is established to retain every superseded pending iteration indefinitely; disposal or retention may be determined pragmatically once the transition is complete, provided required evidence and attributable decision history are not lost. |
| [DR-9](#dr-9) | Prospective adoption and the proposed release boundary | Accepted with Clarification | Accepted. HCRL governance applies prospectively from this adoption, with the current proposed transition being RAD-1.1.1 → RAD-1.1.2 subject to predecessor verification at acceptance. Pending HCRL iterations do not consume releases. No retrospective HCRL reconstruction or historical reconciliation is required. The historical 1.0.10 → 1.1.0 pilot is not part of this transition and SHALL NOT be investigated, completed, cancelled or otherwise reconciled unless a future specific architectural need requires it. |
| [DR-10](#dr-10) | Bounded Minutiae for this iteration | Accept | Accepted as correctly classified bounded review and transition provenance for this HCRL iteration. |

# HCRL — Architecture Governance Decision Review Log

**Standing and scope.** Iteration **002**, prepared by Codex on 2026-10-21
(Australia/Sydney), revises [iteration 001][previous] for ARB review. The proposed
transition remains **RAD-1.1.1 → RAD-1.1.2**. Root
[architecture-release.json][release-json] still records `version=1.1.1`,
`previous_version=1.1.0`. No resulting accepted state or completed HCRL has been
established. This task authorises the review revision only; it does not authorise
governance installation, repository migration, model changes or release allocation.

**Authoritative inputs.** The user-supplied A01–A12 adjudications, whose full
transcription is preserved in [iteration 001's source record][adjudications],
remain authoritative inputs. Their substantive requirements are carried into the
applicable Decision Reviews below. The latest task also supplies the authoritative
Decision Review structure and the ARB correction quoted in [DR-5](#dr-5). These
inputs supersede conflicting candidate recommendations or interpretations in
iteration 001 and the [discovery report][discovery]. The discovery report remains
findings and candidate provenance, not a decision source. No independent ARB
meeting date, signature or peer concurrence has been supplied or invented.

**How to review this record.** The table above is the single Decision Review
table. DR references are this HCRL's operating decision structure and scoped
review references, not canonical architectural identities. Each top-level DR has
its corresponding section. Independently adjudicable subordinate matters use
one level only, as in DR-4.01; none is nested beneath a subordinate DR. Proposals,
uncertainties, consequences, evidence, dependencies, implementation implications
and provenance are contained within their applicable DR. Input labels A01–A12
identify source adjudications; they are not a second operating review structure.

The supplied decisions are settled inputs. **Not recorded** in the table means
that no Solution Architect disposition on this iteration's proposed incorporation
or remaining application detail has been supplied. It does not reopen or negate
those inputs, imply an issued On-Hold instruction, or grant application authority.
Material corrections and any further bounded instructions must remain attributable
to the Solution Architect. DR-10's table entry refers to the same bounded
Minutiae control in that section, not a separate approval mechanism.

**Revision provenance.** The inspected repository HEAD remains
`2cbd9ded6be9f38cb4f18fe2ec083a7375750945`, branch `ArchitecturalGovernance`.
Iteration 001's SHA-256 at this revision's entry is
`fd68c5bb4d0a622dae995a60891c2dd14b3a8d1f80dfd2b4fb00e7e7eaa43d4e`.
It is preserved unchanged, together with the existing discovery and
[initialisation report][initialisation]. This revision replaces the separate
matter, knowledge, action, uncertainty and minutiae catalogues with the DR
structure; removes the proposed ArchiMate classification of governance content
and the general governance-to-continuum correspondence requirement; and places
remaining application questions with the decision they affect. It adds no
architectural decision to fill an unestablished detail.

**Applicable architectural authority.** [Root instructions][agents], the
[Architectural Axioms][axioms] and [Architecture Change Provenance][provenance]
continue to govern this bounded review. AX-04 supports reusing review and
specialised machinery without allowing it to define authority. AX-08 and AX-09
distinguish meaningful transition evidence from source history and processing
detail. AX-14 preserves the distinctions between governance and Enterprise/
Solution Architecture, input adjudication and proposed incorporation, approval
and execution, and release and publication. AX-15 and AX-17 require explicit
uncertainty about effects and missing architectural decisions. This revision
proposes no axiom amendment or new runtime responsibility. Concrete conflicts
with installed contracts are described within the relevant DR rather than
resolved through implementation or new machinery.

<a id="dr-1"></a>
## DR-1 — Architectural decision authority and candidate judgement

**Context and settled basis.** A01 makes the ARB the collaborative architectural
review and adjudication forum. Mark Hunter, Solution Architect, holds final
architectural decision and acceptance authority. ChatGPT acts as architecture
peer/reviewer and may challenge, recommend, concur or dissent, but cannot
independently approve. Codex, Junie and other agents may submit, analyse and
implement authorised work, without approving architecture. Material peer
disagreement may be retained where useful. Consensus is desirable but not
required; there is no voting, quorum or majority mechanism. Delegation requires
a separate explicit governance decision.

A07 directs submitters to use architectural judgement, publish useful candidate
HCRLs and expose uncertainty rather than withhold a potentially material matter.
ARB adjudicates interpretation and classification; corrections inform future
judgement without delegating architectural authority.

**Proposed incorporation for review.** Express these roles and limits in the
common governance definition and retain **Solution Architect Disposition** as
the operative terminology. Candidate statements must make proposed meaning and
uncertainty assessable, with enough source context and consequences for ARB
review. Do not invent a compulsory knowledge schema, peer-concurrence gate or
delegation rule. Useful material dissent and attributable decision revisions may
be retained without implying that consensus is mandatory.

**Decision sought and implications.** Review whether this incorporation proposal
faithfully expresses A01/A07 and identify any wording or scope correction. The
[installed provenance contract][provenance] and [specialised review contract][review]
already reserve acceptance and disposition changes to the Solution Architect;
their reconciliation must preserve that boundary. Governance rules and context
belong in `enterprise-architecture/governance/` under DR-5. DR-2 applies the
authority model to the common log; DR-8 applies it to completion. No acceptance
or application instruction is generated by this submission.

<a id="dr-2"></a>
## DR-2 — HCRL as the common Decision Review Log

**Context and settled basis.** A02 makes HCRL the single authoritative semantic
change/review record for each governed RAD release transition. A completed HCRL
records the authorised semantic transition from the immediate governed
predecessor to the resulting release. It governs that release transition,
rather than every intermediate edit or processing event.

Specialised mechanisms retain authority for their specialised discovery,
analysis, validation and presentation purposes. They produce review material
into HCRL; they are not competing final acceptance records. Material findings
requiring adjudication belong in HCRL, with minor/supporting detail represented
as minutiae or evidence as appropriate.

The present task further directs that HCRL operates as a **Decision Review Log**:
one opening table with `Decision Review`, `Subject`, `Solution Architect
Disposition` and `Comment`; top-level DR numbering with corresponding sections;
and at most one subordinate level for independently adjudicable matters. A
matter needing deeper decomposition becomes another top-level DR. There are no
parallel decision taxonomies for knowledge, actions, uncertainties or minutiae.

**Proposed incorporation for review.** Reconcile the representation-neutral
provenance definition, specialised review/Praxis/template references and agent
entry guidance to produce this single final semantic record. Keep specialised
findings, source review/CR references, validation gates and material decision
history within or referenced from the applicable DR. Do not turn the ArchiMate
template's XML-specific assumptions into universal HCRL obligations.

**Decision sought and implications.** Review this common-record incorporation
and its relationship to specialised review, including whether the matters here
are decomposed at the appropriate decision granularity. Current
[Authoring][authoring] and [Round-Trip][roundtrip] Praxes still use
`change-review.md` checkpoints and final-record wording. Their future adaptation
must preserve specialised purpose and applicable human checkpoints, while
eliminating competing final acceptance records. DR-3 governs placement, DR-4
evidence, DR-8 closure and DR-10 bounded minor detail. Those adaptations have not
been applied.

<a id="dr-3"></a>
## DR-3 — Repository structure and bounded migration

**Context and settled basis.** A03 replaces `docs/markdown` with the following
target structure. Markdown is a representation format, not the architectural
taxonomy. The versioned paths identify the resulting architecture release.

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

`continuum/` contains governed Enterprise/Solution Architecture.
`continuum/documentation/published/` contains LaTeX publication material.
`design/` contains architecture design/change lifecycle material. The existing
ArchiMate strategy remains authoritative beneath `design/archimate/` and is not
redesigned merely as part of restructuring. Under the supplied correction,
governance machinery, rules and context belong in `governance/`; DR-5 states
that boundary explicitly.

Material not fully approved, including partially approved artefacts requiring
another iteration, belongs in `design/review/<version>/`. Approved architectural
artefacts/design records belong in `design/register/<version>/`. Partially
approved artefacts do not enter the register. Required evidence belongs in
`design/evidence/<version>/before/` and `after/`, subject to DR-4. Naming is
`hcrl-<target-version>-<iteration>.pending.md` and
`hcrl-<target-version>-<iteration>.complete.md`; the completed HCRL resides in
the register.

**Proposed incorporation for review.** Plan a bounded source-to-destination and
reference/consumer mapping before any application. Preserve source standing,
canonical identity where established, useful correspondence, unique content and
the existing legacy original-content preservation rules. Distinguish ArchiMate
design/change material from governance machinery and context in the mapping;
do not assume an unchanged wholesale directory move correctly applies both A03
and the correction. Preserve the specialised strategy and semantics.

**Remaining matter requiring adjudication.** The exact affected-file inventory,
destinations, rollout sequence, publication-source mapping and compatibility
actions have not been exhaustively established. A concrete bounded migration
proposal remains necessary before application. No migration of all `docs/`, all
legacy LaTeX or all historical processing is inferred. The target structure is
settled; this uncertainty concerns its application, not whether to adopt it.

**Existing conflict, dependencies and validation.** Root instructions and the
[current governance index][governance] define RADS as `/docs/markdown`; existing
links, Praxes and consumers use current paths. Reconcile those sources and
consumers coherently when authorised, using DR-4's evidence treatment, DR-5's
governance/architecture boundary, DR-7's correspondence and DR-9's adoption
boundary. Check standing, placement, links and affected consumers against the
reviewed mapping. Only the review location already established for iteration
001 is used now; no migration subtree is created by this revision.

<a id="dr-4"></a>
## DR-4 — Required transition evidence

**Context and settled basis.** A03 supersedes the mandatory `before.tar.gz`,
`after.tar.gz`, `change-review.md` package contract. Required transition evidence
belongs in the adjudicated before/after directories; the completed HCRL is the
semantic record in the register. Git remains source/version history. Evidence
should not duplicate Git merely for completeness. A05 likewise reserves source
history to Git and permits bounded code exemplars only where they materially
explain architectural meaning.

The [installed provenance contract][provenance] still mandates the exact
three-file package, full changed-file archives and related lifecycle rules.
Those obligations need explicit reconciliation with the supplied decisions.
Superseding the package is not authority to assume that no evidence is required,
discard unique content, rewrite historical records or delete recovery material.

**Matter requiring review.** Determine the required evidence treatment for the
authorised transition without reintroducing mandatory tar files or inventing a
new universal snapshot requirement. The following subordinate matters separate
selection from representation; no choice is made here to fill either gap.
Their treatment informs DR-3's application and DR-8's closure.

<a id="dr-4-01"></a>
### DR-4.01 — Selection of before/after evidence

**Decision needed.** A03 establishes where required evidence belongs but does
not fully specify the selection rule. Establish what before/after content is
needed to understand and verify this transition, distinguish it from source
history already served by Git, and identify any otherwise unavailable
architectural content or correspondence requiring preservation.

**Proposal for review and consequences.** Select evidence for its contribution
to interpreting and verifying the authorised transition. Do not copy unchanged
dependencies, implementation source or processing history merely for historical
completeness. A bounded architectural exemplar must have an explicit explanatory
purpose. Apply the reviewed selection to the actual changed-content inventory;
do not substitute an entire directory walk or Git commit tree for that judgement.
If selection remains uncertain, record the uncertainty rather than dispose of
the affected material or claim closure.

<a id="dr-4-02"></a>
### DR-4.02 — Evidence representation and preservation

**Decision needed.** The package's supersession does not fully establish which
whole-file/exact-byte preservation, hashing or immutable-source-reference rules
apply under the replacement before/after layout. Review their treatment for the
evidence selected under DR-4.01, including the role of Git locators and content
that is not recoverable from source history.

**Consequences and limits.** Retained hashes or references identify content but
do not replace missing required content. Preserve meaningful state and path
correspondence without inferring a new universal evidence format or automatically
carrying forward compulsory archives. Resolve the applicable representation and
verification instructions before evidence-changing application. This revision
creates no before/after evidence package and performs no historical conversion.

<a id="dr-5"></a>
## DR-5 — Governance knowledge and the Enterprise Architecture Continuum

**Authoritative correction carried into this iteration.** The user supplies the
following correction already made by the ARB during review of iteration 001:

> Governance machinery, governance rules and governance context belong in
> enterprise-architecture/governance/. A06 applies to knowledge that is genuinely
> part of the Enterprise/Solution Architecture; durable governance knowledge SHALL
> NOT be manufactured into ArchiMate architectural knowledge merely because it is
> durable. There is no general requirement for governance material to have a
> corresponding continuum entry.

This is settled input, not a proposed resolution awaiting invention. It corrects
iteration 001's proposed ArchiMate classification of the governance rules and its
general governance-source/continuum correspondence dependency. Those proposals
are not carried forward.

**A06 within its corrected scope.** For knowledge genuinely belonging to the
Enterprise/Solution Architecture, ArchiMate with governed Harmonia specialisations
provides the taxonomy. The Enterprise Architecture Continuum SHALL align with
the ArchiMate metamodel; specialisations reside within their governing native
concept's structure. Submitters propose applicable native-concept or governed-
specialisation tags. ARB may accept, reclassify or reject them; accepted
classification determines continuum placement. Agents SHALL NOT invent types or
specialisations to accommodate information. A new type/specialisation itself
requires adjudication. The continuum and Archi model should provide corresponding
navigation over that governed architecture.

**Proposed incorporation for review.** Keep governance machinery, rules and
context in `enterprise-architecture/governance/`. Do not turn the authority,
review, release, evidence, submission or closure rules in this HCRL into proposed
ArchiMate elements merely because those rules are durable. No general continuum
counterpart, architectural tag, new model element or new canonical identity is
required for them. This iteration submits no such governance-as-architecture
knowledge catalogue. The [modelling strategy][modelling] remains binding for
actual architectural modelling and native-compatible specialisations.

A08 is superseded by A03/A06. No separate architectural-knowledge navigation
mechanism or Domain13 master catalogue is required. Genuine architectural content
ultimately follows its architectural concept and corresponding Archi model;
governance follows the corrected governance location. Existing detailed catalogues
may retain their appropriate purpose. The [requirements catalogue][requirements]
contains prospective Domain13 statements that need reconciliation; their presence
does not override the supplied decisions or authorise blanket deletion.

**Decision sought, remaining detail and implications.** Review whether the proposed
incorporation faithfully applies the correction and the scoped A06/A08 decisions.
Precise native-concept/specialisation placement remains to be established only
for genuine architectural content included in an authorised migration. The prior
general governance-to-continuum question is resolved by the correction, not
retained as a prerequisite. DR-3's inventory must respect this boundary without
inventing new architectural relationships from folders or naming conventions.

<a id="dr-6"></a>
## DR-6 — Review of every change and implementation discoveries

**Context and settled basis.** A05 makes every Harmonia change subject to
Architecture Review: architecture changes, features, backlog items,
implementation changes, bugs, patches and refactoring. Review does not imply
material architectural effect. Submitters exercise judgement about materiality
versus minutiae/non-material implementation detail; ARB/SA adjudicates that
assessment. No change bypasses review because of its label.

A12 expects anyone authorised to implement Harmonia to recognise when the work
changes, contradicts, extends, clarifies or otherwise materially affects
architectural intent. Such discoveries SHALL be surfaced through HCRL for ARB
adjudication. Implementation is a legitimate discovery mechanism, not an
independent authorisation mechanism. Ordinary detail without architectural effect
requires normal HCRL/minutiae review, not separate material treatment.

Significant collaborative Task Details and Task Reports are useful transition
provenance and normally minutiae unless materially architectural. Git is
authoritative for source-code history. Do not reproduce implementation source
as architectural evidence merely for completeness; a bounded exemplar needs to
materially assist understanding of a decision, principle, pattern, constraint
or design.

**Proposed incorporation for review.** Align contribution, implementation and
agent guidance with this coverage. Surface a materially new issue with its
governing authority, evidence, architectural consequences and uncertainty in the
applicable DR. Distinguish a conformance defect from unestablished intent or a
proposed architectural change. Do not promote existing code into architectural
authority or invent a material delta for a transition containing only minor
implementation changes.

**Decision sought and implications.** Review this incorporation and its proposed
treatment of minor-only transitions through DR-10. The existing
[release policy][release] increments every accepted change set and remains
compatible with review of every change; no materiality-based exemption or second
allocator is supplied. Review coverage must be evident for ordinary fixes as well
as architecture changes. DR-4 governs source/evidence selection, DR-1 authority
and DR-9 release allocation. No implementation policy or production code is
changed in this revision.

<a id="dr-7"></a>
## DR-7 — Identity and correspondence across the transition

**Context and settled basis.** A09 preserves canonical identity for governed
architectural things where it exists. It does not allocate canonical identities
to every prose statement or HCRL entry. Use appropriate scoped sources/references
for knowledge without canonical element identity; record explicit correspondence
where continuity across releases needs it. Provenance machinery convenience is
not authority to manufacture identity.

**Proposed incorporation for review.** Carry established canonical identities
and applicable representation bindings through any approved moves. Map old/new
source paths and relevant sections for prose. Distinguish file relocation,
semantic replacement and representation succession. Preserve the
[identity contract's][identity] canonical/representation distinctions and
specialised identity rules. DR references remain local decision references.

**Decision sought and consequences.** Review whether the proposed correspondence
treatment is adequate for DR-3's eventual mapping and DR-8's closure. Exact
correspondence depends on the reviewed affected-content inventory; it is not
invented from matching names, folders or desired tooling output. No new identity
regime, allocation or model-population task is proposed or performed.

<a id="dr-8"></a>
## DR-8 — Completion, immutability and residual uncertainty

**Context and settled basis.** A10 makes an HCRL complete when the Solution
Architect confirms it accurately represents both the authorised change and the
result actually incorporated. Explicitly deferred/excluded matters do not block
completion when their treatment is recorded. A completed HCRL is immutable.
Genuinely non-semantic correction may use an attributable erratum; material
correction/change requires a subsequent governed HCRL transition. Agents may
report facts but cannot self-declare architectural closure.

**Proposed incorporation for review.** Before seeking completion, bind the
authorised meaning to the actual resulting state, source/path correspondence,
required evidence, relevant validation conclusions and limitations. Record exact
reviewed content, attribution, conditions, exclusions and deferrals. Unknown
effects or unfulfilled prerequisites must not become assumed success. Technical
PASS, a complete filename or a release number supplies no missing acceptance.

For the current proposed target the eventual completed record belongs at
`enterprise-architecture/design/register/1.1.2/hcrl-1.1.2-<final-iteration>.complete.md`.
The final iteration is not presumed to be 002. Nothing enters the register now.
Application, validation, evidence establishment and human closure remain
unperformed. DR-1 supplies authority, DR-3/DR-7 actual correspondence, DR-4
evidence and DR-9 the release boundary.

**Decision sought.** Review the incorporation of these settled completion rules
and record the treatment of any residual matters at the appropriate point.
The following application mechanics remain undecided; this candidate does not
invent a policy to make closure appear available.

<a id="dr-8-01"></a>
### DR-8.01 — Attributable non-semantic erratum mechanics

**Decision needed.** A10 permits an attributable erratum without changing the
immutable completed HCRL, but does not specify its carrier, location or detailed
attribution/correspondence procedure. Review those mechanics when needed for
application. They must distinguish a genuinely non-semantic correction from a
material change requiring a subsequent governed transition. No edit-in-place
exception or additional closure authority is proposed.

<a id="dr-8-02"></a>
### DR-8.02 — Pending-iteration checkpoints and retention

**Decision needed.** A03 supplies iteration naming and A10 completed-record
immutability. They do not fully define formal iteration checkpoints/allocation or
retention/disposal of earlier pending iterations. Review the applicable mechanics
or record a bounded deferral/exclusion. No general permanent-retention mandate or
deletion permission is inferred. This specifically authorised revision preserves
iteration 001 for current review provenance; that fact does not establish a
general pending-record lifecycle rule.

<a id="dr-9"></a>
## DR-9 — Prospective adoption and the proposed release boundary

**Context and settled basis.** A11 begins HCRL governance prospectively with
adoption. It forbids blanket historical reconstruction or retrospective HCRL
creation. A later specific need may justify bounded historical archaeology;
preserve gaps and uncertainty rather than inventing authority or correspondence.

The [current root authority][release-json] is 1.1.1 with predecessor 1.1.0.
Under the [existing patch policy][release], the proposed target remains **1.1.2**.
There is no supplied major/minor decision. Pending iterations do not each
allocate a release; generation does not increment one. At eventual acceptance,
the appropriate increment belongs in the same accepted change set, with the
actual immediate predecessor rechecked. Baseline drift requires reassessing
scope and approval applicability, not merely renaming the file.

**Proposed incorporation for review.** Treat the proposed 1.1.1 → 1.1.2
transition as prospective adoption, subject to actual acceptance. Record its
real adoption boundary at that point; the preparation date alone does not
establish it. No previous-release HCRLs are backfilled by this work.

**Remaining matter requiring adjudication.** The installed provenance adoption
still names a separately authorised 1.0.10 → 1.1.0 migration/pilot. Reconcile the
wording and its limited dependency purpose explicitly with prospective HCRL
adoption. It is not this first HCRL, and the revision neither executes nor invents
cancellation or completion of that separate historical activity. No frozen record
is rewritten, recovery dependency retired or processing material disposed of.
DR-3 and DR-8 must reflect the accepted boundary and any recorded exclusions.

<a id="dr-10"></a>
## DR-10 — Bounded Minutiae for this iteration

**Context and settled basis.** A04 requires one explicit Yes/No control for a
disclosed bounded Minutiae group. Submitters use judgement; ARB reviews the
classification. Yes accepts the disclosed group. No identifies required
correction, escalation or reclassification. Silence and agent classification
never supply acceptance. Subsequent iterations incorporate the adjudication;
learning improves judgement without conferring authority. Do not use
`ACCEPTED_WITHOUT_COMMENT` as a per-item disposition.

**Group submitted for this iteration.** The following unnumbered items are one
bounded group within this DR, with no independent minutiae hierarchy:

- The original bounded initialisation task and this review-revision task as Task
  Details/provenance. Their authoritative decisions and corrections remain
  material inputs to the applicable DRs, not accepted as minor by implication.
- The [discovery report][discovery] as findings/candidate provenance, with its
  conflicting recommendations superseded by the authoritative task decisions.
- Preserved [iteration 001][previous] and the [initialisation report][initialisation]
  as review/execution provenance, not competing current acceptance records. The
  correction to iteration 001's governance classification is material DR-5 input.
- Iteration 002's scoped references, links, formatting and factual preparation/
  verification details. These do not demote the substantive DR proposals or
  authorise migration. This revision creates only the new pending Markdown file
  in the existing review directory and leaves earlier artefacts intact.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

This is the operative bounded group control reflected by DR-10 in the opening
table, not another decision hierarchy. Its brackets mark an unanswered field,
not a third disposition. No acceptance is inferred and neither Yes nor No has
been selected by the submitter. The disclosed group ends with the items above;
no later undisclosed addition is covered by it. Any correction or material
reclassification belongs in the applicable DR of a subsequent iteration.

[previous]: hcrl-1.1.2-001.pending.md
[adjudications]: hcrl-1.1.2-001.pending.md#adjudication-record
[agents]: ../../../../AGENTS.md
[release-json]: ../../../../architecture-release.json
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
