<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Software Development Praxis

## 1. Authority and purpose

This Praxis specialises the common [Architecture Governance](architecture-governance.md),
[Review Praxis](architecture-review-praxis.md) and
[Change Provenance](architecture-change-provenance.md)
for software development. Every software change still receives proportionate
Architecture Review. HCRL remains the single semantic review/decision record;
Backlog Items specify work; pull requests, CI and release tags supply coordination
and evidence. Their roles do not confer equivalent architectural authority.
Mark Hunter, Solution Architect, retains final acceptance authority. An
implementation agent cannot accept its own work or declare architectural closure.

The [Architectural Axioms](architectural-axioms.md), accepted
contracts, repository guardrails and [Implementation Guide](../../docs/implementation/Harmonia-Implementation-Guide-Philosophy.md)
remain binding. Applicable implementation-sequencing constraints remain in force.
This Praxis creates no runtime component, architectural element or parallel board.

## 2. Baselines and release identities

An **Architecture release** identifies accepted architectural/governance authority,
under `architecture-release.json` and the amended common release policy. A
**software release** identifies an accepted implementation baseline, its exact
source state, tested scope and architectural conformance, including qualifications.
Neither identity implies complete implementation of the other.

Use a solution-wide, repository-wide software `X.Y.Z` identity, independent of
Architecture versions. Components may retain their Maven artefact versions or
build identities; these do not establish independently accepted Harmonia software
baselines. Patch denotes compatible corrections; minor denotes compatible added
functionality; major denotes incompatible supported contracts. The assignment
proposes the target; Solution Architect acceptance confirms it. These software
rules do not change the separate authority required for Architecture major/minor
increments. Optional `rc.N` labels identify candidates, never accepted baselines.
HCRL iteration numbers identify review iterations, not software versions or builds.

Ordinary implementation that conforms to unchanged architectural authority
increments only the software release on acceptance. Accepted architectural or
normative governance changes receive the existing Architecture patch allocation.
A change set affecting both identifies both transitions and their dependency;
each identity is allocated once, with no increment for corrective iterations,
rejected candidates, provenance maintenance or HCRL completion. Adoption of this
governance proposal is an Architecture change, not a software release.

The completed software HCRL is the semantic acceptance authority. An immutable
`software/vX.Y.Z` Git tag locates the accepted implementation commit; the HCRL
records that tag, commit, Architecture release/source locator, artefacts where
applicable and qualifications. No second software JSON release register is needed.
Record Maven/package versions explicitly. Publishable binaries require immutable,
unambiguous versions; prepare any release-version edits before final candidate
testing and review. A mutable `SNAPSHOT` label alone never identifies accepted bytes.

Establish the actual accepted software predecessor from its completed HCRL and
source locator. Branch names, passing tests, Maven versions and existing commits
do not establish acceptance. If none exists, the Solution Architect must explicitly
establish the first baseline or accept a first release with no asserted predecessor.
There is no retrospective acceptance of current source through adoption of this Praxis.

## 3. Backlog Items, assignments and traceability

`docs/backlog/` SHALL be the single authoritative location for Harmonia Backlog
Items authored by humans or agents. Backlog Items are work-management artefacts;
they are not Continuum elements and neither replace nor duplicate Motivation
elements. They may reference Requirements, Constraints, Goals, Outcomes, Journeys,
Use Cases, Capabilities, Features or other elements; propose changes or new elements;
define bounded realisation of accepted requirements; or capture justified engineering
corrections, refactoring, remediation and testing. Resulting architectural change
remains subject to established governance, ADD and HCRL controls. A Backlog Item
itself neither authorises implementation nor authorises or enacts architecture.

Every Backlog Item SHALL contain, at minimum:

- **Overview**, with **High-Level Synopsis of Effort**, **High-Level Synopsis of
  Activities**, **High-Level Synopsis of Outcome** and **High-Level Synopsis of
  Acceptance Criteria** as subsections.
- **Context**, including scope, constraints, exclusions and relevant starting facts.
- **Associated Requirements**, identifying applicable architectural references and
  their actual standing; justify engineering work with no applicable requirement
  rather than inventing one. Identify proposed or missing architecture explicitly.
- **Expected / Associated Activity**.
- **Expected Outcomes**.
- **Acceptance Criteria**, with observable results and how they will be verified.

Each item's title SHALL identify the item, with explicit **Backlog Item ID** and
**Backlog Item Revision** header metadata. Retain established identifiers; no new
allocation scheme or revision format is introduced. Task Details, Implementation
Reports and HCRLs reference both values and the corresponding source/revision
locator. The item revision identifies its work specification, independently of
Architecture/software releases and HCRL iteration numbers.

The Overview summarises the detailed sections; it is not a second specification.
Use the [repository Backlog Item template](../../docs/backlog/backlog-item-template.md), stable
item identifiers and exact source/version references. The existing Task 9 sample is
historical work material, not the desired structure/content for future items.
No historical item is rewritten or retrospectively declared compliant by this Praxis.

The Backlog Item specifies the objective, intended outcome, requirements, scope,
constraints and acceptance criteria. The formal AI-agent Task Detail SHALL reference
the applicable item(s) and reviewed revision/source instead of duplicating their
complete specifications. It records the specific authorised assignment, agent,
baseline/authority, execution instructions, exclusions and additional evidence or
verification directions. Preserve actual issued wording under the common provenance
contract; do not discard instructions to create an artificially shorter record.
Specification, delegated execution and implementation acceptance remain distinct.

Maintain **Architecture Continuum ↔ Backlog Item → AI-Agent Task → Implementation/Report
→ HCRL → Accepted Software Release** correspondence. Use appropriate many-to-many
links, not mandatory one-to-one mappings. The Continuum/Backlog correspondence
represents information and proposals; it does not confer equal authority. Existing
element references, item IDs, source locators, Task/Report links and HCRL/release
references suffice. Record reverse correspondence in the Backlog/HCRL where useful;
traceability alone does not require adding backlinks to each Continuum element.
Changing a Backlog Item must not silently change the criteria of an already reviewed
assignment or candidate; preserve the referenced revision and material changes.

## 4. Milestones and acceptance boundaries

| Milestone | Required result and boundary |
| --- | --- |
| Accepted baseline | Establish accepted software predecessor and relevant accepted Architecture release, exact sources, qualifications and existing work. |
| Bounded assignment | Reference the originating Backlog Item(s) and their revision; retain issued execution authority, baseline, exclusions, additional verification and permitted repository/release actions. This authorises engineering work, not acceptance. An item's existence supplies no implementation authority. |
| Implementation and testing | Agent performs ordinary design, code, tests, documentation and evidence work autonomously within the assignment. Internal failures/corrections are normal development. |
| Candidate submitted | Preserve an exact integrated Harmonia solution candidate, originating Backlog Item references, Report, required solution-wide verification/conformance evidence and pending HCRL requesting implementation acceptance. Any software PR/HCRL submission is always solution-wide. A PR is optional transport where repository interaction is authorised, not another acceptance gate. |
| Review and correction | Solution Architect records acceptance, qualifications, changes requested or rejection against the exact candidate. Corrections within delegated scope proceed under the assignment and attributable review directions; resubmit changed candidate and affected verification. |
| Accepted release closure | After exact-result confirmation, complete the software HCRL, perform authorised Git/release operations, verify correspondence and establish the next software baseline. Report partial closure if any mandatory step remains incomplete. |

An assignment should identify the software predecessor/target and Architecture
baseline, reference its Backlog Item(s), and state execution-specific constraints,
required checks/evidence and commit/branch/merge/tag/push/publication/deployment
authority where relevant. Work outcomes, scope and criteria remain in the referenced
items; do not duplicate their complete specifications. Repository defaults need
not be restated.
Clarify missing authority only when the affected action requires it. Preparation
may continue without permission to merge, publish or deploy.

## 5. Autonomous implementation and escalation

Within the authorised scope, the agent SHALL make ordinary implementation choices,
decompose cohesive classes, organise packages consistently with accepted ownership,
write and correct tests, handle realistic failures, update documentation, diagnose
build problems and produce evidence without repeated engineering approval.
The Implementation Guide's defensive programming, 95/5 philosophy, object design,
educational documentation and exception rules remain applicable. Its 500-line Java
exception requires the specified Solution Architect disposition; this Praxis
does not convert explicit existing exceptions into routine delegated choices.

An engineering choice implements established contracts within delegated scope.
Changing authority, responsibility, public semantics, invariants, security,
durability, deployment boundaries or an accepted design decision may require
architectural adjudication. An apparent architecture/code conflict, material scope
change or missing architectural authority SHALL be raised in the applicable HCRL
DR under AX-17 before dependent implementation. Record proposed resolution and
evidence; do not derive authority from existing code. Continue independent
authorised work where it cannot prejudge the decision.

Required architecture changes use an Architecture-target HCRL under the common
Praxis, cross-linked from the software HCRL. Each records its own transition once;
do not copy decisions or treat software acceptance as architecture adoption.
The software candidate identifies the resulting accepted architecture before its
dependent release closure. Narrow pre-existing exceptions require exact attribution;
an Architecture-release qualification is not automatically a software-test waiver.

## 6. Solution-wide candidate submission and evidence

Every software PR/HCRL acceptance candidate SHALL represent the integrated Harmonia
solution, even when its Backlog Items and assignment change only one or several
modules. This requires solution-wide verification, not changes to every module.
Before submitting a candidate for acceptance review the implementation agent SHALL
perform applicable component/unit testing, solution-wide regression testing,
solution-wide integration testing, and relevant architectural and
implementation-conformance validation against the exact candidate.

Record actual commands, scope, source state, environment, outcomes and material
failures, including unavailable environments, unexecuted tests and unresolved
dependencies. A module passing its tests is insufficient solution-wide readiness;
neither an architecture suite nor a Maven phase name alone proves regression or
integration coverage. Identify the actual solution scope exercised. Where required
testing cannot complete, disclose the exception and request an explicit Solution
Architect disposition in the candidate HCRL before treating the prerequisite as
satisfied. Submission of an exception for adjudication is not a readiness claim;
pending disposition and failed/unexecuted/uncertain results remain visible.

Use the common `design/` structure with a qualified software target key:

```text
enterprise-architecture/design/
  review/software-X.Y.Z/hcrl-software-X.Y.Z-001.pending.md
  register/software-X.Y.Z/hcrl-software-X.Y.Z-NNN.complete.md
  evidence/software-X.Y.Z/001/
    before/ai-agent-tasks/<issued-UTC>-<assignment>.md
    after/ai-agent-reports/<produced-UTC>-<report>.md
```

This extends existing HCRL/evidence target naming, not governance semantics.
Architecture paths retain their existing numeric keys. The HCRL iteration remains
the provenance boundary. Retain the operative Task and substantive follow-ups,
corresponding Reports and purpose-selected evidence using the common provenance
contract, irrespective of architectural materiality. Do not issue a Task for
every command, test, review comment or mechanical evidence update. Retain material
correction instructions and resulting activity correspondence within that model.
Later iterations reference earlier evidence at its original location.

A submission SHALL establish, without copying the Backlog specifications:

- Exact accepted predecessor and candidate source: commit/tree and bounded diff,
  or a retained patch plus exact base and necessary new files while uncommitted.
  Preserve reviewed candidate correspondence through later corrections; Git is
  the source-history authority. A hash cannot replace missing source bytes.
- Required tests actually executed: command, source state, relevant environment,
  scenario/suite scope, outcomes and material failure diagnostics. Retain useful
  summaries/results; retain detailed logs only where needed for review/recovery.
  Apply bounded execution and distinguish PASS, FAIL, TIMEOUT, STALLED and UNRESOLVED;
  identify unperformed checks and reasons separately. Passing unit tests do not
  imply integration, operational or deployment verification.
- Implementation conformance: a concise contract → implementation → evidence
  mapping covering functional correctness, defensive/unhappy paths, educational
  documentation and object/package quality, architectural invariants, known
  limitations and accepted exceptions. Include security, concurrency, retry,
  partial failure and recovery where relevant. Identify originating Backlog Item(s)
  in both the Implementation Report and HCRL, and map applicable acceptance criteria
  to implementation/results, architectural conformance and solution-wide
  regression/integration evidence. Compilation/coverage alone is insufficient.
- A pending HCRL with one opening DR table, independently adjudicable material
  matters, actual candidate conclusions and a bounded Minutiae group. Record the
  implementation-acceptance request and exact acceptance scope explicitly even
  when Architecture Review finds no material architectural effect.

Use the common timestamp/prefix requirements and unresolved-event treatment.
One substantive activity may have adequate many-to-many Task/Report correspondence;
PR descriptions, CI logs and HCRL shall link formal evidence rather than duplicate it.
Keep evidence free of PHI and respect existing security/logging boundaries.

## 7. Review outcomes and corrective iterations

| Outcome | Treatment |
| --- | --- |
| Accepted | Attributable Solution Architect confirmation binds the exact source candidate, incorporated result, conformance evidence and release scope. Proceed only to authorised closure actions. |
| Qualified acceptance | Record each limitation/failed or unperformed check, accepted risk, affected usage/release restrictions, remediation responsibility and trigger or due point. Distinguish a condition required before closure from an explicitly permitted post-release obligation. Unmet preconditions prevent closure; agents cannot waive mandatory gates. |
| Changes requested | Retain the reviewed candidate and disposition; correct within the same development increment and proposed software release where authorised. Retest affected behaviour and required component, solution-wide regression/integration and conformance checks; identify superseded results and re-establish integrated readiness for resubmission. No fresh approval is needed for ordinary in-scope corrections. |
| Rejected candidate | Preserve the rejection, reason and sufficient candidate/evidence correspondence. It establishes no accepted baseline, consumes no software or Architecture release and does not erase the previous baseline. Resume remediation only within continuing assignment/review authority; rejection alone does not authorise expanded scope. |
| Abandoned increment | Record the attributable cancellation/disposition and actual remaining Git state in the pending HCRL. Mark review closed without acceptance; retain it in `review/`, not the completed register. Allocate no release. Preserve required evidence and isolate/revert only the authorised work. |
| Rollback | Before acceptance, reversible local corrections within the assignment are engineering work. After acceptance, source reversion is a new bounded software transition with its own HCRL/release; preserve prior tags and completed HCRLs. An operational redeployment of a previously accepted artefact follows explicit existing rollback authority and records the actual deployed version, rather than inventing a new source release. |

Uncertain rollback, migration or distributed effects remain UNRESOLVED under
AX-15 until established by authoritative observation. Never assume that reverting
Git undoes durable state or that a timeout proves failure. Deployment and database
operations require their actual authority; source acceptance alone supplies none.

Preserve attributable decisions and candidate changes in the same HCRL history.
`HCRL Updated` processes and advances exactly one reviewed iteration under common
controls, using software-qualified paths. It neither accepts nor allocates a release.
Ordinary development/test retries do not each create a review iteration. A
successor carries forward decisions, unresolved matters, obligations and links.
An agent cannot overwrite reviewed inputs or convert a recorded disposition into
an agent status. Reassessment is required when the baseline or candidate changes
in a way that invalidates reviewed evidence or acceptance.

## 8. Closure, Git verification and next baseline

`HCRL Finalised` dispatches by the explicitly established target kind. For software,
it initiates exact-result confirmation/closure under Review Praxis §6; it supplies
no missing acceptance, test waiver, architecture adoption or Git/deployment authority.
No Architecture bump or Continuum publication occurs for software-only closure.

Recheck predecessor, candidate and concurrent baseline drift. Verify mandatory
checks have succeeded or received an applicable explicit disposition. Identify
accepted implementation source separately from the commit containing closure
records; no record must contain its own final commit hash. Verify the authorised
merge's implementation tree against the accepted candidate; code changes require
affected revalidation and renewed exact-candidate confirmation. Mechanical HCRL
completion/evidence updates remain provenance maintenance.

Register only the confirmed accepted solution-wide result, preserving originating
Backlog Item links, conditions and original history. Perform only authorised
commit/merge/tag/push/artefact publication actions.
An existing identical tag/artefact can be verified; differing immutable content
must fail closed. Record the actual branch, implementation commit, release tag,
closure-record source locator, clean/dirty state and remote/published correspondence
when performed. If repository authority is absent or an operation fails, report
accepted-but-closure-pending with the precise remaining obligation; do not claim a
terminal baseline or overwrite acceptance history. Retry from verified reached state.

The next assignment starts from the release whose required closure correspondence
has been verified, carrying all continuing qualifications and remediation obligations.
Acceptance, Git release, artefact publication and deployment remain distinct facts.
