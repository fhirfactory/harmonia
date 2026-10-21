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

# HCRL — Architecture Governance completed transition record

**Standing.** Iteration **003**, completed on 2026-10-21 (Australia/Sydney),
is the authoritative semantic record of **RAD-1.1.1 → RAD-1.1.2**. It records
application of the Solution Architect dispositions in [reviewed iteration 002][reviewed],
the subsequent **Apply** instruction and final confirmation of the incorporated
result. Codex prepared this record from [pending iteration 003][pending] under
that authority. The opening table reproduces the issued dispositions/comments
exactly, including their original wording and scope; it issues no new dispositions.
Root [architecture-release.json][release-json] records `version=1.1.2`,
`previous_version=1.1.1`. Completion belongs to the same accepted change set and
consumes no additional release.

**Final confirmation.** Mark Hunter, Solution Architect, confirmed the result
on 2026-10-21 with the explicit task instruction **“result confirmed - complete”**,
referencing pending iteration 003. That confirmation establishes that this HCRL
accurately represents both the authorised change and the result actually
incorporated. This completed HCRL resides in the register and is immutable.
Codex records the Solution Architect's confirmation without issuing independent
architectural acceptance. Deferred erratum mechanics, excluded migration and
excluded historical work remain as recorded below.

**Exact authority and state.** The originating repository locator is
`2cbd9ded6be9f38cb4f18fe2ec083a7375750945`, verified before application with
root release 1.1.1. Reviewed iteration 002 is preserved unchanged; its SHA-256
is `d578a1e28fa0f89e10585664b7fc33d248718bc00151c00a6685aa082be0320d`.
Iteration 001 and the existing discovery/initialisation reports are also
preserved. Their supplied A01–A12 decisions and the ARB governance-location
correction remain source authority; iteration 002's explicit comments settle
the application details. Its earlier preparation-only narrative and unanswered
Minutiae placeholder record the pre-adjudication drafting state; the subsequently
entered decisions and Apply instruction govern this application.

The resulting files are present at the paths recorded below and in the
[application report][report], including its completion addendum. They remain
uncommitted workspace changes; no immutable after-commit locator is claimed.
Solution Architect confirmation is recorded above. The retained files themselves
preserve the resulting state pending an accepted-change-set commit. Review inputs
remain unchanged; no disposal accompanies completion. Optional source hashes here
identify the reviewed input; they establish no universal hash rule.

**Axiom consistency.** AX-04 is preserved by reusing the specialised review
mechanisms and existing release helper. AX-08/AX-09 guide meaningful evidence and
bounded processing retention. AX-14 preserves governance versus architecture,
source decision versus application result, review versus publication and
pending versus complete. AX-15/AX-17 preserve unknown effects and unestablished
authority. No axiom, model identity, runtime responsibility or native modelling
semantics is changed. The source authority remains repository-held rules and
the explicitly supplied adjudications, rather than implementation or prior
session interpretations.

<a id="dr-1"></a>
## DR-1 — Architectural decision authority and candidate judgement

**Authorised meaning.** ARB is the collaborative architectural review and
adjudication forum; Mark Hunter, Solution Architect, retains final decision and
acceptance authority. ChatGPT may challenge, recommend, concur or dissent without
independent approval. Agents submit, analyse and implement authorised work without
approving architecture. Consensus is desirable, not required; no vote, quorum,
majority mechanism or inferred delegation is introduced. Material disagreement
may be retained. Candidate judgement exposes uncertainty and improves through
ARB correction without conferring authority.

**Application.** Installed these rules in
[Architecture Governance][governance-contract], including Solution Architect
Disposition terminology, judgement-led submission and implementation-discovery
responsibility. Updated [root instructions][agents] and [docs instructions][docs-agents]
to load the current common governance/provenance contracts and apply the exact
reviewed scope. Missing decisions and architectural closure remain human authority.
These are governance rules, not newly classified ArchiMate elements.

<a id="dr-2"></a>
## DR-2 — HCRL as the common Decision Review Log

**Authorised meaning.** One HCRL is the authoritative semantic record for each
immediate governed release transition, not each edit or processing event.
Specialised mechanisms retain their discovery, analysis, validation and
presentation purposes and feed material findings into the applicable DR.
They are not competing final acceptance records.

**Application.** The common governance contract and
[common HCRL template][hcrl-template] provide the single opening table with
Decision Review, Subject, Solution Architect Disposition and Comment; consecutive
top-level DR sections; and one permitted subordinate level. Proposals, uncertainty,
evidence, consequences, application details and provenance belong in the applicable
DR, without parallel decision taxonomies. This record retains the existing ten
top-level and four subordinate reviews.

Reconciled these existing sources **in place**:
[ArchiMate Change Review][specialised-review],
[Authoring Praxis][authoring], [Round-Trip Praxis][roundtrip] and
[ArchiMate review template][specialised-template]. Final material decision facts
and validation conclusions now feed HCRL. The specialised four-CR-disposition/
separate-outcome semantics, exact-input checkpoints, trigger vocabulary,
native-type/identity/containment/navigation rules, View preservation and
applicable validation/publication gates remain intact. No intake, reconciler,
model or machinery code was executed or migrated.

<a id="dr-3"></a>
## DR-3 — Repository structure and bounded migration

**Authorised meaning and exclusion.** The target structure and standing rules are
established, but acceptance explicitly does **not** authorise repository migration.
Migration requires a separately established bounded source-to-destination mapping
preserving standing, identity, correspondence and specialised semantics.

**Application.** Installed the target layout, version/naming rules and placement
semantics in the common governance contract. New maintained governance sources
were added under `enterprise-architecture/governance/`:
`README.md`, `architecture-governance.md`,
`architecture-change-provenance.md` and
`templates/hcrl-template.pending.md`. These are the adopted common governance
installation, not relocation of the existing corpus.

Existing source correspondence is **same path → same path**, with in-place
amendments to root/docs instructions, the existing governance index, release
policy, provenance entry, specialised review/Praxes/template and requirements
navigation catalogue. DR-2 records the specialised amendments; DR-4 the
provenance replacement; DR-5 the navigation qualification; DR-9 the release
increment. The exact changed-path inventory is in the factual application report.

No existing file is renamed or removed. The continuum, publications, ArchiMate
strategy/machinery and processing material remain at their existing paths.
The application installation created no register, continuum, ArchiMate or
evidence subtree. Following Solution Architect confirmation, completion adds
only this record at `design/register/1.1.2/hcrl-1.1.2-003.complete.md` and updates
current-standing references in the governance entry, both common contracts,
release policy and supporting report. This is authorised HCRL closure, with the
pending inputs retained; no existing architecture is migrated or partially
approved artefact promoted. A future migration mapping remains outside this
transition, not an unresolved prerequisite silently satisfied by directory moves.

<a id="dr-4"></a>
## DR-4 — Required transition evidence

**Authorised meaning.** Evidence supports understanding, verification and
preservation of the authorised transition without duplicating source history or
recreating the mandatory archive package.

**Application.** Installed [current Architecture Change Provenance][provenance-contract]
as the single representation-neutral contract. The former contract remains at its
[existing path][old-provenance], with the complete original text unchanged inside
an explicit supersession wrapper referencing the current contracts. Existing
historical anchors remain available; the preserved text is not competing current
authority. This is in-place reconciliation, not investigation or rewriting of
historical releases.

Updated specialised closure guidance/template fields to use HCRL contributions,
adequate Git recovery or necessary retained evidence, rather than mandatory
`before.tar.gz`, `after.tar.gz`, `change-review.md` and archive-hash fields.

<a id="dr-4-01"></a>
### DR-4.01 — Selection of before/after evidence

**Authorised meaning.** Select evidence where it materially assists understanding,
verification or preservation; do not accumulate it for completeness or duplicate
material adequately preserved by source control.

**Application and selection for this transition.** The pre-change versions of
the eleven modified tracked paths are recoverable at the originating Git commit.
Their resulting versions and the six files added at application handoff are
retained directly in the working change set. Completion adds this register record
as the seventh new file, with current-standing reference updates recorded under
DR-3 and in the report's completion addendum. The reviewed, previously
untracked HCRL inputs and existing reports remain at their actual paths; no claim
is made that the originating commit contains them. No extra architecture-source
copies, implementation-source exemplars, before/after archive trees or historical
processing snapshots are selected merely for completeness.

The relevant evidence is the issued decisions, scoped source/result
correspondence, actual retained source files and validation conclusions. Detailed
engineering logs/recovery captures remain temporary working material, not a
fourth architectural-evidence component or permanent acceptance record.

<a id="dr-4-02"></a>
### DR-4.02 — Evidence representation and preservation

**Authorised clarification.** Evidence faithfully preserves architecturally relevant
state. Git may identify/recover repository-controlled material, without duplicate
exact-byte archives or mandatory hashes. Where source control cannot reliably
recover required evidence, retained evidence itself preserves the necessary state
and correspondence. No universal format, hash or exact-byte duplication rule is
established.

**Application.** The current contract implements that clarification. Original
tracked sources can be read with `git show <originating-commit>:<path>`;
new/current uncommitted sources are retained as files pending accepted-change-set
commit. Solution Architect confirmation is now recorded; no after-commit locator
is invented. Optional engineering comparisons verify
preservation; they are not new governance obligations. The superseded contract's
original content remains intact, reviewed iteration 002 is unchanged, and absence
before addition remains distinct from an empty file. No permanent auxiliary
manifest, duplicate archive or self-hash machinery is installed.

<a id="dr-5"></a>
## DR-5 — Governance knowledge and the Enterprise Architecture Continuum

**Authorised meaning.** Governance machinery, rules and context remain governance
knowledge under `enterprise-architecture/governance/`. Durability does not require
manufacturing ArchiMate elements or continuum entries. ArchiMate/native-concept
and governed-specialisation classification/placement apply to genuine
Enterprise/Solution Architecture. New types/specialisations need adjudication;
navigation is not independent architectural authority.

**Application.** The common governance contract states that boundary and the
scoped A06 rules explicitly. It establishes no new architecture elements,
specialisations, canonical IDs, knowledge taxonomy or continuum counterparts for
governance content. The target layout and common-governance entry clarify that
Domain13 is not an EA master catalogue.

Amended the [requirements navigation catalogue][requirements] only to qualify
prospective Domain13 governance/navigation claims and link the current rule.
Existing requirement statuses/classifications, domain refreezing and runtime
allocations are unchanged. Broader concept placement remains part of a future
authorised migration, not this installation.

<a id="dr-6"></a>
## DR-6 — Review of every change and implementation discoveries

**Authorised meaning.** Every Harmonia change receives Architecture Review,
irrespective of implementation classification. Review determines materiality.
Implementation can expose previously unrecognised architectural implications;
those return for adjudication and cannot independently establish intent.
Ordinary detail receives appropriate HCRL/minutiae review. Significant Task
Details/Reports normally supply minor transition provenance, while Git retains
source history and code exemplars need a bounded architectural purpose.

**Application.** Installed these rules in common governance and root/docs agent
guidance. The release policy explicitly covers reviewed minor-only transitions
without inventing a material delta or changing the patch mechanism. No production
implementation or new runtime review/assurance service is introduced.

<a id="dr-7"></a>
## DR-7 — Identity and correspondence across the transition

**Authorised meaning.** Preserve established identity and correspondence needed for
continuity. Governance/prose and DR references require no canonical architectural
IDs. Continuity cannot be inferred from naming, location or tooling convenience.

**Application.** The current provenance contract records these limits and keeps
specialised identity semantics binding. Changed maintained sources are modified
at the same paths; new common contracts are additions. The old provenance source
is retained with explicit semantic supersession to the new contract, not an
invented file rename or a new canonical element. No model resource, binding,
UUID, relationship or View is changed. Git diff and preservation checks bound
that conclusion to this change set, not an audit of all historical identity.

<a id="dr-8"></a>
## DR-8 — Completion, immutability and residual uncertainty

**Authorised meaning.** Completion requires Solution Architect confirmation that
authorised change and actually incorporated state correspond. Explicit recorded
deferrals/exclusions may remain. Completed HCRLs are immutable, subject to the
established non-semantic-erratum principle and subsequent transitions for material
changes. Agents report facts, not architectural closure.

**Application and final confirmation.** Installed those rules. Application
facts are recorded here and in the supporting report. Source correspondence and
preservation, release metadata and documentation/conformance validation were
verified before handoff; their conclusions and limits remain recorded under
DR-10 and in the report. Mark Hunter, Solution Architect, subsequently instructed
**“result confirmed - complete”** on 2026-10-21, referencing pending iteration 003.
This supplies the required confirmation of authorised meaning and incorporated
result. This record is therefore complete in the register; the pending application
record remains unchanged as review provenance. No after-commit locator is supplied.
Completion follows the explicit human confirmation; validation alone supplies no
architectural acceptance.

DR-8.01's deferral and the migration/historical-work exclusions in DR-3/DR-9 remain
part of the authorised result and do not prevent completion. The completed record
is immutable; material correction requires a subsequent governed transition,
with genuinely non-semantic correction subject to the established attributable
erratum principle and deferred mechanics.

<a id="dr-8-01"></a>
### DR-8.01 — Attributable non-semantic erratum mechanics

**Disposition applied: Defer.** Preserved the architectural rule: immutable
completed HCRL; attributable erratum for a genuinely non-semantic correction;
subsequent governed transition for a material correction. Detailed mechanics
will be established when an actual requirement arises. No erratum carrier,
implementation or speculative procedure is designed now. This explicit deferral
is part of the authorised result, not an incomplete implementation of accepted
erratum machinery.

<a id="dr-8-02"></a>
### DR-8.02 — Pending-iteration checkpoints and retention

**Authorised clarification.** Retain pending iterations while active review and
traceability require them. The completed HCRL is durable authority. Superseded
pending iterations need not all be retained indefinitely; pragmatic retention/
disposal after completion must preserve required evidence and attributable history.

**Application.** Installed that clarification without inventing a fixed checkpoint
schedule or permanent retention mandate. Pending iterations 001, reviewed 002
and 003 remain unchanged and available as review provenance at completion.
This completed record supplies durable transition authority. No disposal or
processing cleanup is performed, and no permanent retention obligation for every
pending iteration is added.

<a id="dr-9"></a>
## DR-9 — Prospective adoption and the proposed release boundary

**Authorised clarification.** Adoption is prospective for 1.1.1 → 1.1.2, subject to
predecessor verification. Iterations consume no releases; no retrospective HCRL
reconstruction or historical reconciliation is required. The historical pilot
is outside this transition and must not be investigated, completed, cancelled
or otherwise reconciled unless a future specific architectural need requires it.

**Application.** Verified the root authority at 1.1.1/previous 1.1.0 before mutation,
then used the existing expected-current helper once to prepare
`version=1.1.2`, `previous_version=1.1.1`. The release policy records this
governance-only increment, accepted adoption and final Solution Architect
confirmation. Completion consumes no further release. No minor/major increment
or second allocator is introduced. Architecture/model
publications are not regenerated for this governance-only change. No pilot
artefact, WIP ingest, historical run or retrospective transition was investigated
or modified; its existing references remain preserved outside current work.

<a id="dr-10"></a>
## DR-10 — Bounded Minutiae for this iteration

**Recorded source decision.** Reviewed iteration 002 DR-10 explicitly accepts its
disclosed bounded group as correctly classified review/transition provenance.
The source table is an unambiguous human decision despite the earlier unanswered
placeholder in that file; the following control reflects that decision only.

```text
Minutiae-Accepted: Yes
Comment: Solution Architect's reviewed iteration 002, DR-10: "Accepted as correctly classified bounded review and transition provenance for this HCRL iteration." Scope: the group disclosed in iteration 002 only.
```

That group comprises the original/revision Task Details, discovery-report
provenance, preserved iteration 001/initialisation-report provenance, and
iteration 002's local references/formatting/preparation facts, with the material
decisions/correction kept outside minor classification. It is not enlarged by
the agent. The Solution Architect separately confirmed the incorporated result
through the completion instruction recorded under DR-8.

**Application and completion provenance.** The Apply task and
[application report][report] supplied factual execution/verification detail for
the Solution Architect's result confirmation, now received. The completion task
and report addendum record that confirmation and register placement. They do not
supply a new architectural decision or independent acceptance record. The report's
source inventory and check conclusions are supporting facts; material meaning
remains in the applicable DRs above. The accepted bounded Minutiae group is unchanged.

**Application-stage verification.** Required architecture suite **PASS**: 90 tests in 11 classes,
zero failures/errors/skips; Maven BUILD SUCCESS in 17.199 seconds, bounded to
240 seconds with five-second termination grace. This verifies existing
implementation conformance, not architectural closure. Release validation
**PASS**: 1.1.2, previous 1.1.1. Documentation/scope checks **PASS**: 78 new/changed
local links/anchors, parsing/rendering, exact issued table, ten top-level/four
subordinate DRs, scoped Minutiae control, unchanged reviewed inputs/predecessor
content, recoverable tracked pre-images and the exact eleven-modified/six-added
path boundary with no moves/removals. No new whitespace issues were introduced;
unchanged pre-existing whitespace was preserved. Specialised projection-rule
text is unchanged. Actual model transport/publication and migration were not
performed. These are the application-stage results; the scope count predates
addition of this completed register record. Final architectural confirmation has
now been supplied by the Solution Architect as recorded under DR-8.

**Completion verification.** Documentation and scope checks **PASS**: 64 local
links/anchors across the six closure documents; parsing/rendering, references,
fenced blocks and whitespace; exact issued table and DR structure; unchanged
source-scoped Minutiae control. The closure scope is this register record plus
five current-standing reference updates. All other workspace files, including
pending review inputs and release metadata, remain unchanged from the confirmation
handoff. No existing file is moved or removed; no migration, historical work or
additional release increment is performed. The existing architecture-suite result
above remains applicable to the unchanged implementation.

[reviewed]: ../../review/1.1.2/hcrl-1.1.2-002.pending.md
[pending]: ../../review/1.1.2/hcrl-1.1.2-003.pending.md
[report]: ../../../../.junie/reports/2026-10-21-architecture-governance-hcrl-application.md
[initialisation]: ../../../../.junie/reports/2026-10-21-architecture-governance-hcrl-initialisation.md
[release-json]: ../../../../architecture-release.json
[agents]: ../../../../AGENTS.md
[docs-agents]: ../../../../docs/AGENTS.md
[governance-contract]: ../../../governance/architecture-governance.md
[provenance-contract]: ../../../governance/architecture-change-provenance.md
[hcrl-template]: ../../../governance/templates/hcrl-template.pending.md
[old-provenance]: ../../../../docs/markdown/governance/architecture-change-provenance.md
[specialised-review]: ../../../../docs/markdown/governance/archimate-ingestion-change-review.md
[authoring]: ../../../../docs/markdown/governance/architecture-authoring-praxis.md
[roundtrip]: ../../../../docs/markdown/governance/archimate-round-trip-engineering-praxis.md
[specialised-template]: ../../../../docs/markdown/governance/templates/archimate-ingestion-change-review-template.md
[requirements]: ../../../../docs/markdown/01-motivation/requirements-constraints/master-requirements-catalogue.md
