<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

Task-Issued-At: UNRESOLVED

Event source: The operative user instruction is available in this Codex interaction,
but its attributable system instruction-entry timestamp is not exposed. The first
clock observation, 2026-11-15T23:27:46Z, is later investigation time, not issuance.
The original event and required UTC filename prefix remain unresolved for Solution
Architect treatment before closure; neither is inferred from that observation.

# Task Details — Software Development Praxis proposal

Issuer: user instructing Codex for repository `fhirfactory/harmonia`.
Activity: governance investigation, proposal and pending-HCRL preparation only.
Boundary: proposed Architecture transition 1.1.25 → 1.1.26, iteration 001;
this conventional next-patch review identity allocates or reserves no release.
Entry branch: `Mneme-and-Mnemosyne-Build`.
Entry HEAD: `441ec989a3853db4ea4dad253e14b8464181f293`.
Entry worktree: clean. Root release: 1.1.25, previous 1.1.24.

The repository root AGENTS.md and applicable current governance are binding.
No adoption, normative-source amendment, software development, release allocation,
publication, HCRL completion, commit or push is authorised by this assignment.

## Operative issued wording

The following preserves the user assignment verbatim, omitting only its outer
Markdown code fence. Its recommendations remain proposals until adjudicated.

````text
# Harmonia — Software Development Praxis
## Codex Governance Assignment

### Goal

Develop a concise, practical Software Development Praxis for Harmonia, extending the existing architectural governance framework to support autonomous software implementation, testing, review, acceptance and release.

The objective is to establish a repeatable development lifecycle comparable to the DevOps backlog → development → testing → pull request → review → acceptance → release model, using Harmonia's existing HCRL and provenance mechanisms wherever appropriate.

### Expected Outcome

A reviewable governance proposal that:

1. Defines explicit software-development milestones and acceptance boundaries.
2. Supports autonomous implementation within an authorised development assignment.
3. Distinguishes software implementation releases from Architecture Continuum releases.
4. Uses existing HCRL and evidence mechanisms without introducing competing governance.
5. Covers review-driven corrections, rejected candidates, rollback and accepted software releases.
6. Identifies the minimum repository governance and tooling changes necessary to make the process operational.

**This assignment authorises development of the governance proposal, not its automatic adoption.**

### Context

Repository: `fhirfactory/harmonia`

Last-known working branch: `Mneme-and-Mnemosyne-Build`

Last-known accepted Architecture Continuum baseline: `1.1.25`

Verify repository state, applicable release identities and current governance before proceeding.

Relevant authority includes:

- `AGENTS.md`
- `enterprise-architecture/governance/architectural-axioms.md`
- `enterprise-architecture/governance/architecture-governance.md`
- `enterprise-architecture/governance/architecture-review-praxis.md`
- `enterprise-architecture/governance/architecture-change-provenance.md`
- `enterprise-architecture/governance/architecture-release-policy.md`
- `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`

The existing Architecture Continuum, governance requirements, HCRL semantics and Solution Architect authority remain binding.

### Constraints

**Preserve existing governance.**

Extend existing mechanisms rather than constructing a separate governance framework.

**Preserve implementation autonomy.**

A properly authorised implementation assignment delegates ordinary engineering decisions, development, testing, documentation and associated evidence production to the implementation agent.

Escalation is appropriate for genuine architectural decisions, material scope changes or unresolved authority conflicts, not routine engineering choices.

**Preserve acceptance authority.**

An implementation agent may prepare a candidate release and supporting HCRL, but cannot independently accept its own implementation.

**Separate release identities.**

An Architecture Continuum release identifies accepted architectural authority.

A software release identifies an accepted implementation baseline and its architectural conformance.

Ordinary source-code changes must not automatically require incrementing the Architecture Continuum release.

**Preserve implementation standards.**

The existing Harmonia Implementation Guide remains applicable to substantial implementation work, including defensive programming, meaningful unhappy-path handling, educational code documentation and implementation conformance evidence.

**Avoid unnecessary process overhead.**

Do not introduce speculative approval gates, duplicated provenance records, unnecessary agent interactions or a broad rewrite of existing governance.

### Authorised Activity / Scope

1. Examine the existing governance, HCRL, release-policy and provenance mechanisms.

2. Determine how they can support the following target software-development lifecycle:

   - Accepted software baseline.
   - Bounded development assignment.
   - Autonomous implementation and testing.
   - HCRL submission requesting implementation acceptance.
   - Solution Architect review and corrective iteration.
   - Accepted release closure, Git verification and establishment of the next baseline.

3. Propose how formal AI-agent Task Details, execution Reports, test evidence and implementation-conformance evidence should be retained using established repository conventions.

4. Define the treatment of:
   - Accepted implementations.
   - Qualified acceptance.
   - Rejected candidates.
   - Remediation within the same development increment.
   - Rollback or abandonment.
   - Architectural discoveries requiring escalation.

5. Identify the smallest required additions or amendments to existing governance documents, `AGENTS.md` and any supporting tooling.

6. Prepare the proposed Software Development Praxis and any necessary supporting amendments for Solution Architect review.

7. Follow existing governance for preparing and presenting the proposal itself.

Do not apply the proposed normative governance changes before Solution Architect review and authorisation.

Do not initiate Calliope refactoring or unrelated implementation changes.

### Report

Provide:

1. Current governance mechanisms examined and their applicability.
2. The proposed Software Development Praxis.
3. Specific governance gaps or conflicts, distinguishing genuine gaps from matters already covered.
4. Proposed repository changes, including exact affected documents.
5. Software-release identity and HCRL lifecycle recommendations.
6. Any unresolved decisions requiring Solution Architect adjudication.
7. The formal governed Task, Report and supporting evidence required by the current repository process.
8. The proposed HCRL decision points for review.

Prefer a concise, implementable outcome over extensive theoretical process documentation.

**Stop at the proposal/review boundary. Do not self-accept, apply normative changes, commit or push without the applicable authorisation.**
````

## Solution Architect review directions — follow-up within this assignment

Follow-up-Issued-At: UNRESOLVED

Attribution: Mark Hunter, Solution Architect, through the current user message
explicitly presenting HCRL 1.1.26/001 review directions. The instruction-entry
system timestamp is not exposed; no replacement timestamp is inferred.
This retains the material follow-up under the original Task and iteration,
not a new assignment, HCRL iteration or release. The original Task event remains
unchanged. DR-6 qualifies the original missing event; this follow-up has the
separately disclosed same observation limit. No full temporal compliance is claimed.

The following is the issued follow-up verbatim, omitting only its outer fence.

````text
# Harmonia — Software Development Praxis
## HCRL 1.1.26/001 — Solution Architect Review Directions

### Goal

Revise the proposed Software Development Praxis to incorporate the Solution Architect's decisions concerning Backlog Items, solution-wide verification and software release identity.

This is a **review-driven correction of the existing proposal**, not a new governance-development assignment.

Preserve the existing HCRL, proposal, analysis, evidence and accepted architectural authority. Do not restart the activity or introduce an independent governance framework.

### Expected Outcome

An updated Software Development Praxis and corresponding amendments, presented through the existing HCRL for Solution Architect acceptance.

The revised proposal must incorporate the following decisions.

### DR-1 — Backlog Items and Development Lifecycle

**Disposition: Changes Requested**

#### 1. Authoritative Backlog Repository

`docs/backlog/` SHALL be the single authoritative repository location for Harmonia Backlog Items, whether generated by humans or AI agents.

Use the existing backlog organisation and conventions where suitable. Inspect these before proposing amendments.

Do not introduce separate AI and human backlogs.

#### 2. Architectural Semantics

Backlog Items are **work-management artefacts**, not Architecture Continuum elements.

They do not replace, duplicate or constitute Motivation elements.

A Backlog Item may:

- Reference existing Requirements, Constraints, Goals, Outcomes, Journeys, Use Cases, Capabilities, Features or other architectural elements.
- Identify or propose changes to existing Motivation or other Continuum elements.
- Initiate the creation of new architectural elements through established governance.
- Define bounded implementation work that realises part or all of an accepted architectural requirement.
- Capture engineering corrections, refactoring, remediation, testing or other justified technical work.

Backlog Items can and will result in updates to the Architecture Continuum.

However, **the Backlog Item does not itself authorise or enact an architectural change**. Resulting changes remain subject to established architectural governance, ADD and HCRL controls.

Do not introduce a mandatory one-to-one mapping between Backlog Items and architectural elements.

#### 3. Backlog Item versus AI-Agent Task Detail

The Backlog Item establishes the development objective, intended outcome, applicable architectural references, scope, constraints and acceptance criteria.

The AI-Agent Task Detail records the specific authorised assignment.

For software-development assignments, the formal Task Detail SHALL reference the applicable Backlog Item(s) rather than duplicate their complete specifications.

The Task Detail may include additional execution-specific instructions, such as:

- Assigned agent and authorised activity.
- Applicable baseline and architectural authority.
- Execution constraints and exclusions.
- Required evidence or verification.
- Additional instructions from the Solution Architect.

A Backlog Item's existence does not independently authorise implementation.

Preserve the distinction between work specification, delegated execution and implementation acceptance.

The existing formal Task/Report provenance machinery remains applicable.

#### 4. Traceability

Maintain traceability across:

**Architecture Continuum ↔ Backlog Item → AI-Agent Task → Implementation/Report → HCRL → Accepted Software Release**

The bidirectional relationship between the Continuum and Backlog represents information and change proposals, not equivalent architectural authority.

Support appropriate many-to-many relationships without imposing artificial cardinality constraints.

### DR-2 — Software Release Identity and Verification

**Disposition: Accepted with refinement**

#### 1. Solution-wide release identity

Accept the proposed independent, repository-wide software `X.Y.Z` release identity.

Harmonia software releases are **solution-wide**, not independently accepted component releases.

Individual components may have their own Maven artefact versions or build identities, but these do not establish separate accepted Harmonia software baselines.

Preserve the separation between Architecture Continuum releases and software releases.

#### 2. Solution-wide Pull Request

A software Pull Request/HCRL submission is ALWAYS solution-wide.

The originating Backlog Item and implementation assignment may concern one or several modules, but the acceptance candidate represents the integrated Harmonia solution.

Before submitting the candidate for HCRL/Pull Request acceptance review, the implementation agent SHALL perform:

- Applicable component/unit testing.
- Solution-wide regression testing.
- Solution-wide integration testing.
- Relevant architectural and implementation-conformance validation.

Record actual commands, scope, environment, outcomes and material failures.

A module passing its own tests is insufficient evidence of solution-wide readiness.

Failures, unavailable environments, unexecuted tests and unresolved dependencies must be explicitly reported. They must not be represented as successful verification.

Where required testing cannot be completed, the candidate must identify the exception and obtain an explicit Solution Architect disposition rather than silently treating the prerequisite as satisfied.

Do not confuse solution-wide verification with requiring changes to every module.

Preserve the proposed immutable release identification and exact Git-state verification mechanisms.

### DR-3 — Evidence, Review and Corrective Iterations

**Disposition: Accepted with refinement**

Preserve Codex's existing proposed treatment of:

- Formal Task and Report provenance.
- Autonomous implementation and testing.
- Implementation Guide conformance.
- HCRL review and acceptance.
- Review-driven remediation.
- Qualified acceptance.
- Rejected and abandoned candidates.
- Rollback.
- Exact candidate/source correspondence.
- Release closure and verification.

Explicitly require the Implementation Report and HCRL to identify the originating Backlog Item(s).

Implementation evidence must demonstrate satisfaction of applicable Backlog Item acceptance criteria, architectural conformance and solution-wide regression/integration results.

Review-driven corrections should remain within the existing development increment and proposed software release where authorised. Perform appropriate retesting and preserve candidate correspondence.

Do not introduce a new approval requirement for ordinary corrections within the authorised scope.

### DR-4 — Supporting Governance Amendments

**Disposition: Revise and resubmit**

Update the proposed Software Development Praxis and exact supporting amendments to incorporate DR-1 through DR-3.

Inspect the existing `docs/backlog/` conventions and determine the minimum changes required to support authoritative Backlog Items and their traceability.

Identify any genuinely necessary updates to existing repository governance, templates, agent instructions and tooling.

Avoid duplicating specifications between Backlog Items, Task Details, Reports and HCRLs.

Preserve established HCRL, provenance and architectural release semantics except where the accepted decisions require explicit amendment.

Do not introduce new governance machinery where existing mechanisms suffice.

### DR-5 — Initial Software Baseline

**Disposition: Accepted**

Accept Codex's recommendation to defer establishment of the first accepted software baseline and initial software release version until the first appropriately authorised software-development assignment.

Do not retrospectively accept existing software merely because it is present in Git.

Preserve existing architectural qualifications and their actual applicability.

### DR-6 — Minutiae and Provenance

**Disposition: Accepted with qualification**

Preserve the recorded uncertainty concerning the original AI-agent Task issuance timestamp and filename prefix.

Do not manufacture historical timestamps or substitute unrelated events.

Record the provenance exception explicitly for the HCRL and retain the existing evidence.

Preserve the existing review and acceptance authority boundaries.

### Authorised Activity

1. Incorporate these review directions into the existing pending HCRL.
2. Revise the Software Development Praxis proposal.
3. Reconcile the exact proposed governance amendments and necessary tooling changes.
4. Inspect and account for existing `docs/backlog/` conventions.
5. Update the relevant formal Report and supporting evidence.
6. Perform applicable proposal consistency and validation checks.
7. Present the revised candidate and any remaining genuine decision points for Solution Architect acceptance.

This remains a proposal-stage activity.

**Do not apply normative governance changes, finalise the HCRL, allocate a release, commit, push or commence Calliope refactoring.**

### Report

Provide a concise summary of:

- Changes made to the proposal.
- Backlog conventions examined and any recommended amendments.
- Solution-wide testing and acceptance semantics incorporated.
- Updated governance/tooling amendment scope.
- Validation results.
- Any remaining material conflicts or decisions requiring Solution Architect adjudication.

Preserve the existing HCRL history and provenance.

Do not create another independent review cycle or repeat the original governance investigation unnecessarily.
````

## Subsequent Solution Architect clarifications — same review correction

Follow-up-Issued-At: UNRESOLVED

Attribution: the user steering this same Solution Architect review correction.
Instruction-entry timestamps are not exposed. These clarifications supersede any
assumption that the inspected sample defines future structure. They do not restart
the assignment or authorise normative application. Issued wording follows verbatim.

```text
the existing sample file in docs/backlog is not reflective of the desired structure or content of future backlog items.
```

```text
a backlog should, as a minimum, contain:

--> Overview
--> Overview.High-Level-Synopsis of Effort
--> Overview.High-Level-Synopsis of Activities
--> Overview.High-Level-Synopsis of Outcome
--> Overview.High-Level-Synopsis of Acceptance Critera
--> Context
--> Associated Requirements 
--> Expected / Associated Activity
--> Expected Outcomes
--> Acceptance Criteria
```

Representation note: use nested headings for the four Overview synopses, and
normalise the heading spelling to “Acceptance Criteria” without changing meaning.
The existing sample remains unchanged historical work material, not a template.

## Final Backlog refinements — same assignment and iteration

Follow-up-Issued-At: UNRESOLVED

Attribution: Solution Architect final accepted refinement directions in the current
user message. The instruction-entry timestamp is not exposed and is not inferred.
The original event/filename and prior directions/dispositions remain unchanged.
The following issued wording is retained verbatim; this is not a new assignment.

````text
# Harmonia — Software Development Praxis
## HCRL 1.1.26/001 — Final Backlog Refinements

### Goal

Incorporate the Solution Architect's final accepted refinements to the proposed Backlog Item template and Backlog README within the existing Software Development Praxis proposal.

These are minor corrections. The substantive Backlog Item semantics, software-development lifecycle, solution-wide verification and release model have already been reviewed and accepted in principle.

### Expected Outcome

An internally consistent, review-ready proposal incorporating the following two refinements, with the existing HCRL and supporting amendments updated accordingly.

### Context

Continue within the existing HCRL 1.1.26/001 review and its established evidence/provenance structure.

Relevant proposed artefacts:

- `docs/backlog/backlog-item-template.md`
- `docs/backlog/README.md`
- `enterprise-architecture/governance/software-development-praxis.md`

Use the actual current repository paths for the proposal-stage versions.

Preserve the existing Solution Architect dispositions, accepted decisions and review history.

### Authorised Activity

**1. Backlog Item identity and revision**

Update the proposed Backlog Item template to include explicit header metadata identifying:

- Backlog Item ID.
- Backlog Item Revision.

The document title should also identify the Backlog Item.

Do not invent a new identifier allocation scheme. Reconcile with any existing repository conventions.

Ensure that the formal AI-agent Task Detail can reference the specific Backlog Item and revision used to authorise the implementation.

Preserve the previously accepted minimum Backlog Item structure, including all four Overview synopses.

**2. Backlog README links**

Verify that the proposed maintained `docs/backlog/README.md` references the actual maintained locations:

- `docs/backlog/backlog-item-template.md`
- `enterprise-architecture/governance/software-development-praxis.md`

Ensure that the links resolve correctly after incorporation.

Remove proposal-stage link references from the maintained versions where they would be invalid.

**3. Proposal reconciliation**

Update the existing proposal, prospective patch, supporting amendments and HCRL evidence only where required to reflect these corrections.

Check that the Backlog Item ID/revision semantics remain consistent across the Praxis, Task Details, Reports and HCRL references.

Do not duplicate complete Backlog Item specifications in execution records.

**4. Validation**

Perform applicable bounded checks for:

- Required Backlog Item sections.
- Explicit identity/revision metadata.
- Maintained-document link integrity.
- Proposal/patch consistency.
- Preservation of prior dispositions and review history.

Record the actual results and any unresolved exceptions.

### Constraints

This is a correction to the existing proposal, not a new governance-design activity.

Do not reopen previously accepted architectural decisions, introduce new mandatory Backlog sections, create a separate backlog management mechanism or expand the proposed governance framework.

Preserve the distinction between Backlog Items, architectural Motivation elements, delegated Tasks and implementation acceptance.

Do not apply normative amendments, finalise HCRL 1.1.26/001, allocate a release, commit or push.

### Report

Return a concise confirmation of:

1. Corrections incorporated.
2. Resulting affected files.
3. Validation results.
4. Any remaining material acceptance concerns.

**Present the corrected candidate for final Solution Architect review.**
````
