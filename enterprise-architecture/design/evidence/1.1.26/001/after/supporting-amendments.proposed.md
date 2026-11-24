<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Supporting amendments — proposed, not applied

The [Praxis proposal](../../../../review/1.1.26/software-development-praxis.proposed.md)
and [pending HCRL](../../../../review/1.1.26/hcrl-1.1.26-001.pending.md)
are the review entry points. The [exact prospective diff](governance-amendments.proposed.patch)
is based on HEAD `441ec989a3853db4ea4dad253e14b8464181f293`.
It changes seven existing documents and adds the Praxis, backlog README and Backlog
Item template. No diff target was edited. This is the revised candidate under the
Solution Architect's six dispositions and subsequent minimum-structure clarification,
retained in the existing Task. The [reviewed-input correspondence](../before/solution-architect-review/source-correspondence.json)
preserves the prior candidate. Added documents retain the substantive review text
with review wrappers removed and links rebased for their maintained locations.

## Proposed normative/documentation changes

| Exact affected path | Bounded amendment |
| --- | --- |
| `enterprise-architecture/governance/software-development-praxis.md` | Add the software specialisation, authoritative Backlog work specification and minimum structure, many-to-many traceability, solution-wide candidate/testing/acceptance rules and existing corrective/closure treatment. |
| `enterprise-architecture/governance/architecture-governance.md` | Extend common HCRL coverage/target naming to software, retain Solution Architect authority, identify Backlog work-management and integrated-solution boundaries, specialise software closure and remove the unconditional Architecture increment for software-only changes. |
| `enterprise-architecture/governance/architecture-review-praxis.md` | Reference software specialisation, originating items, solution-wide pre-submission tests and exception treatment; generalise paths and condition Architecture allocation on actual effect. Preserve common review/confirmation. |
| `enterprise-architecture/governance/architecture-change-provenance.md` | Extend the target key to `software-X.Y.Z`; software Tasks reference item revisions instead of duplicating specifications; Reports/HCRLs identify items and criteria/conformance/regression/integration evidence. Preserve existing record/event/iteration model. |
| `enterprise-architecture/governance/architecture-release-policy.md` | Explicitly supersede the rule that every accepted source change increments Architecture; retain Architecture allocation for accepted architectural/normative governance change and cross-reference software allocation. Historical decisions stay unchanged. |
| `AGENTS.md` | Align §1.3, evidence target and implementation instructions; require single Backlog location, referenced specifications and solution-wide testing/reporting; route existing HCRL commands by target kind. Preserve fail-closed/Git limits. |
| `docs/AGENTS.md` | Align the inherited evidence target placeholder and explain software qualification, avoiding contradictory RAD-only placement instructions. |
| `enterprise-architecture/governance/templates/hcrl-template.pending.md` | Add target/source, originating Backlog Item revision, integrated candidate, criteria and solution-wide verification/exception fields, retaining the existing DR/Minutiae structure. |
| `docs/backlog/README.md` | Add a short entry point identifying the single human/agent work-management location, template, authority boundaries and references. Existing sample remains historical. |
| `docs/backlog/backlog-item-template.md` | Add the Solution Architect's minimum sections and four Overview synopses, with explicit Backlog Item ID/Revision header fields, the item identified in the title, scope/constraints/source/traceability in Context and observable criteria. No complete specification is copied into Tasks/Reports/HCRLs. |

## Backlog inspection and clarification

Inspection found only `docs/backlog/Harmonia - Task 9 - Backlog.md`: a six-line
grouped sample with `Mneme`, `BL-09-01` and brief objective prose; no directory README
or template. The Solution Architect subsequently clarified that this sample is
not representative of future structure/content and supplied the required minimum.
The proposed [template](../../../../review/1.1.26/backlog-item-template.proposed.md)
and [README](../../../../review/1.1.26/backlog-readme.proposed.md) follow those directions,
not inferred sample conventions. Existing sample bytes, identifier and historical
assertions remain unchanged; no migration or retrospective compliance is proposed.

The minimum is Overview with high-level synopses of Effort, Activities, Outcome
and Acceptance Criteria; Context; Associated Requirements; Expected / Associated
Activity; Expected Outcomes; Acceptance Criteria. Item/architecture associations
are many-to-many. An item may propose changes under ADD/HCRL, but is neither a
Motivation element nor authority to implement or change architecture. Task Details
reference the item/revision and add execution instructions. Reports/HCRLs identify
originating items and evidence against criteria rather than repeat specifications.

The current Implementation Guide is unchanged. Current AGENTS.md already makes
its standards applicable; the user's assignment reinforces that obligation.
Its Draft/Pending header and closing statement remain an observed standing mismatch,
not an exemption or evidence of separate acceptance. Header/standing reconciliation
may be explicitly addressed later; adoption of this Praxis does not retrospectively
approve the guide or any existing implementation. No legacy architectural content
is migrated into the Continuum or rewritten to agree with it.

## Minimum supporting tooling work, if adoption/application is authorised

Only the existing evidence helper needs functional extension. These are proposed
implementation requirements, not current executable behaviour or tested code:

| Exact affected path | Required change and verification |
| --- | --- |
| `enterprise-architecture/tooling/architecture_review_evidence.py` | In `boundary_paths` and `check_iteration`, validate either the existing numeric Architecture target or exactly `software-X.Y.Z` using the existing strict numeric version grammar on its suffix. Preserve traversal rejection, iteration grammar, exclusive emission, before/after roles and event-prefix checks. `emit_record` then uses those existing paths. Keep ArchiMate `--prepare` Architecture-only with an explicit target-kind rejection for software, rather than producing an architectural-delta scaffold for software acceptance. No allocator or acceptance inference. |
| `enterprise-architecture/tooling/tests/test_architecture_review_evidence.py` | Extend the existing suite for separate numeric/software targets sharing the same numeric version; software Task/Report emission and read-only checks; pending/completed discovery; timestamp mismatch/unresolved events; malformed prefix, prerelease target, path traversal and wrong-role rejection; software `--prepare` rejection; no mutation of root Architecture release or existing numeric evidence. Existing Architecture cases must continue to pass. |
| `enterprise-architecture/tooling/README.md` | Document explicitly selected software-target emission/check examples, common HCRL dispatch and the fact that Architecture publication targets are not software-release commands. |

No change to `architecture_release.py`, the Architecture release JSON schema,
Continuum projectors/publishers, runtime code or Maven dependency graph is needed.
Git supplies ordinary commit/tag/diff mechanics; the governed process supplies
authority. No new lifecycle CLI, approval service, release allocator, software JSON
register, evidence taxonomy, mandatory PR workflow or reviewer-agent loop is proposed.

The solution-wide software identity is explicitly accepted with refinement. Every
software PR/HCRL candidate is the integrated solution, even for narrow module work.
Before submission, component/unit, solution-wide regression/integration and relevant
conformance verification are required. Actual commands, source, scope, environment,
results and unavailable/unexecuted/failed conditions must be recorded; incomplete
required testing requires an explicit exception disposition. Optional PR transport
does not make the integrated-solution acceptance scope optional. No universal
regression/integration command or readiness proof is inferred from `mvn test` or
the architecture suite. The software assignment must identify actual applicable
suites/environments; missing coverage remains disclosed, not manufactured by this
proposal. No additional CI platform or test harness is installed here.

`--version software-X.Y.Z` is a proposed interface extension; the current helper
correctly rejects it. Do not execute it as if already adopted. Tool checks validate
layout/events, not authentic issuance, semantic conformance or acceptance.

## Adoption/application sequence for review

1. Review the corrected candidate against the recorded dispositions in this same
   HCRL. DR-1 requested changes, DR-2/DR-3 were accepted with refinements, DR-4 requires
   resubmission, DR-5 accepts first-baseline deferral and DR-6 accepts the original
   temporal qualification. Do not reopen those decisions as unanswered; current
   normative policy remains in force until explicit adoption/application.
2. If authorised, apply only the reviewed governance amendments and bounded helper
   extension. Preserve adoption attribution and recheck the accepted Architecture
   predecessor. Run the existing helper suite plus the specified new namespace
   cases, link checks, required architecture checks and applicable common gates.
   Keep the change pending until required implementation/verification succeeds or
   receives explicit applicable treatment; do not activate incomplete machinery.
3. This normative adoption receives one Architecture patch under current policy.
   `1.1.25 → 1.1.26` is only today's provisional review mapping. Recheck allocation,
   predecessor and release correspondence before application/closure; baseline
   drift must be reviewed, not hidden by renaming. No software release is allocated
   by adoption and unchanged frozen architecture publications need not regenerate.
4. Report actual application and obtain exact-result Solution Architect confirmation
   under Review Praxis §6. Complete/register and perform Git actions only with
   applicable authority. This assignment supplies none of those future actions.
5. As accepted under DR-5, defer to the first appropriately authorised software
   assignment to establish its
   accepted predecessor or explicitly uninitialised starting state, proposed first
   software version and repository/release authority. This may be part of that
   assignment; it is not another standing approval gate for later routine work.

An initial software-baseline/version decision is deferred by the recorded decision;
no existing software is retrospectively accepted. A proposal is not operational merely because
its illustrative paths are mechanically plausible or this prospective diff applies.

## Final accepted Backlog refinements — same 1.1.26/001 proposal

The latest Solution Architect directions confirm the substantive proposal is
accepted in principle and request only explicit Backlog Item identity/revision
headers and correct maintained README links. The template now records **Backlog
Item ID** and **Backlog Item Revision** beneath its identifying title, without
adding sections or an ID allocator. The observed `BL-09-01` identifier remains
unchanged; no existing revision convention was established from that sample.
This introduces no new identifier or revision format. Task Details, Reports and
HCRLs reference the item ID/revision and source, without copying specifications.

The prospective maintained README links are exactly `backlog-item-template.md`
and `../../enterprise-architecture/governance/software-development-praxis.md`,
resolved from `docs/backlog/README.md`. Review-copy links remain usable for review;
incorporation text contains no `.proposed.md` link destinations. The patch-generation
rebase already supplied these correct destinations and is verified, not redesigned.
No additional target documents or tooling changes are required.

[Pre-refinement input correspondence](../before/final-backlog-refinements/source-correspondence.json)
preserves the reviewed candidate and formal record states. Final bounded checks
are recorded in [refinement verification](final-backlog-refinements/verification.json).
All prior dispositions and acceptance boundaries remain in force; this is final
exact-candidate review, not a new governance-design or HCRL cycle.
