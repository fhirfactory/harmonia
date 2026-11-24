<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Backlog Item: BL-CALLIOPE-OO-01 — Cohesive Topic and TopicMask implementation

**Backlog Item ID:** BL-CALLIOPE-OO-01

**Backlog Item Revision:** 1

Proposed for Solution Architect review. This item specifies future engineering
work; it does not authorise implementation or establish an accepted software baseline.
The identifier names this item only and introduces no repository allocation scheme.

## Overview

### High-Level Synopsis of Effort

Make Calliope's Topic and TopicMask implementation easier to understand and maintain
through cohesive object collaboration, preserving accepted naming and matching
semantics and the existing consumer contracts. Correct the known duplicate-position
construction defect as part of this bounded increment.

### High-Level Synopsis of Activities

Characterise the existing public and serialisation contracts; separate construction,
compatibility conversion and matching support where responsibilities justify it;
strengthen invariant enforcement, documentation and tests; demonstrate integrated
Harmonia compatibility under the Software Development Praxis.

### High-Level Synopsis of Outcome

Small, documented collaborating objects support immutable Topic and TopicMask
facades. Existing callers continue to work, duplicate Topic positions fail explicitly,
and the implementation has traceable conformance and verification evidence.

### High-Level Synopsis of Acceptance Criteria

Review demonstrates cohesive responsibilities and compliant source-file size;
tests establish preserved contracts, accepted invariants and meaningful unhappy
paths. An exact solution candidate has component, solution-wide regression,
integration and conformance results, with explicit dispositions for required
verification that cannot be completed. Implementation acceptance remains with the
Solution Architect through HCRL.

## Context

The inspected source is commit `dc30cfbcc7ad4113b29e3710904453c023df4dc1` on
`Mneme-and-Mnemosyne-Build`. Accepted Architecture authority is **1.1.26**; the
relevant immutable Topic/TopicMask reconciliation was confirmed by
[HCRL 1.1.25/001, DR-3](../../enterprise-architecture/design/register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3).
Individual source qualifications remain in force. Existing Git snapshots and Maven
`1.0.0-SNAPSHOT` are not accepted software releases. Establishing the first accepted
software baseline/version remains deferred to an appropriately authorised
implementation assignment under
[HCRL 1.1.26/001, DR-5](../../enterprise-architecture/design/register/1.1.26/hcrl-1.1.26-001.complete.md#dr-5).

At this source state,
[Topic.java](../../calliope/src/main/java/net/fhirfactory/harmonia/model/topic/Topic.java)
has 736 physical lines and
[TopicMask.java](../../calliope/src/main/java/net/fhirfactory/harmonia/model/topic/TopicMask.java)
has 672, including documentation and supporting declarations. They combine core
immutable state with construction, compatibility factories, parsing, nested support
types and, for TopicMask, regex preparation/evaluation. This is a coherent first
increment with direct consumers and existing tests; it does not attempt to remedy
all ten oversized Calliope files at once.

`Topic.Builder.addEntry(int, ...)` currently uses map replacement, so a second entry
at the same position silently erases the first. The accepted Topic contract requires
duplicate-position rejection. This is an implementation discrepancy with established
authority, not a request for a new naming architecture. A Map already supplied to a
constructor cannot reveal entries overwritten before that call; enforce rejection
at the construction entry points that receive successive positions.

Junie's recovered Phase 1 review is an investigation input. The
[independent assessment](../../enterprise-architecture/design/evidence/1.1.27/001/after/engineering-assessment.md)
explains the narrower priority, corrections to its assumptions, consumer evidence
and separately disclosed authority questions. This item does not accept its proposed
whole-module package migration, task-model unification or estimated class sizes.

Scope is the two model classes, cohesive support objects warranted by their
decomposition, their tests and directly relevant documentation. Preserve the public
`net.fhirfactory.harmonia.model.topic` entry points, including `Topic.Entry`,
`Topic.Builder`, `TopicMask.EntryPattern`, `TopicMask.Builder`, the existing enums,
constructors and compatibility factories. Supporting class names and package detail
are ordinary engineering choices under the Guide. Existing consumers may supply
regression evidence; production changes outside this boundary require a material
scope review rather than an incidental import sweep.

Excluded: public package migration; removal of compatibility APIs; registry or UUID
allocation redesign; new Topic/version, regex or wire protocols; runtime subscription,
routing or security policy orchestration; Themis dependency reallocation; IU/peer
metadata redesign; Pragma/Ergon/Praxis, persistence or PetasosParcel refactoring;
and runtime convergence milestone changes. Any proposed change to accepted semantics,
responsibilities or boundaries must return for architectural adjudication before
dependent work. Unaffected engineering may continue within its future assignment.

Preparation correspondence: [Task Detail](../../enterprise-architecture/design/evidence/1.1.27/001/before/ai-agent-tasks/unresolved-calliope-oo-backlog-preparation.md)
and [pending HCRL 1.1.27/001](../../enterprise-architecture/design/review/1.1.27/hcrl-1.1.27-001.pending.md).
No implementation Task, implementation Report, software HCRL or accepted software
release exists for this item. Link those records when established; future Tasks,
Reports and HCRLs must identify **BL-CALLIOPE-OO-01, revision 1**, with the exact
reviewed source locator. A changed specification must preserve the assigned revision.

## Associated Requirements

| Authority / architectural reference | Applicable obligation and standing |
| --- | --- |
| [Calliope-Models](../../enterprise-architecture/continuum/application/application-component/307b3440a507403f8bc214d290bffb6a.md), canonical ID `307b3440a507403f8bc214d290bffb6a` | Shared model definition/library responsibility. Accepted IU/Topic incorporation does not transfer runtime ownership, policy evaluation or persistence to Models; source-qualified contributions retain their stated standing. |
| [Topic](../../enterprise-architecture/continuum/application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md), canonical ID `9e2ffa9b4a1d4422a33c2a4bbcfc86ef` | Preserve immutable UUID/definition, explicit root-to-leaf sequencing, rejection of gaps/duplicate positions, positional qualifier defaults and derived names; no Topic.Version. The common logical DataObject does not prescribe one Java class or its package. Final incorporated-result acceptance is recorded in 1.1.25/001 DR-3 despite retained pending-review wording in the source. |
| [TopicMask](../../enterprise-architecture/continuum/application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md), canonical ID `bb489157a6bf48be926c5a601bb0a6c3` | Preserve immutable definition/independent UUID, one matching basis, per-entry regex evaluation and Exact/Inclusive depth semantics. Neutral matching grants no operational authority. Wire grammar and cross-language regex decisions remain bounded detailed design. |
| [AX-04](../../enterprise-architecture/continuum/motivation/principle/54508cda80484a6caf95b901780eaf7d.md), [AX-14](../../enterprise-architecture/continuum/motivation/principle/d3fc6025dc7646d1b2020b51f7a44bfa.md), [AX-17](../../enterprise-architecture/continuum/motivation/principle/architectural-axiom/8882573df7ce43c6bc0f66da0a676139.md) | Preserve Harmonia semantics, distinguish identity from names/matches, and expose missing or conflicting authority rather than infer it from existing code or illustrative packages. |
| [Implementation Guide §§2–5, 7](../implementation/Harmonia-Implementation-Guide-Philosophy.md) and [agent implementation standards](../../AGENTS.md#harmonia-implementation-standards) | Cohesive OO collaboration, defensive programming, meaningful unhappy paths, educational documentation and the complete-file 500-line rule. The Guide's retained draft wording does not remove its applicability under AGENTS.md. |
| [Software Development Praxis §§3–8](../../enterprise-architecture/governance/software-development-praxis.md) | Separate work specification, execution authority and acceptance; retain item/revision correspondence and solution-wide verification/evidence. |

This is justified engineering realisation and conformance work against those existing
contracts. It creates no Motivation element, new architectural relationship or
one-to-one mapping between this item and the Continuum. Potential pre-existing
authority discrepancies are disclosed in the assessment and HCRL; resolving them is
outside this item's implementation scope.

## Expected / Associated Activity

1. Under a subsequent authorised assignment, verify the actual starting source,
   Architecture authority and qualifications. Characterise public signatures,
   identity/equality, compatibility factories and serialisation before extraction.
   Review real consumers such as TopicSubscription, IU/security selectors, MLLP
   destination selection and Praxis workflow subscriptions.
2. Decompose independently meaningful construction/validation, compatibility
   conversion and pattern preparation/evaluation responsibilities through cohesive
   objects and delegation. Keep immutable value state and invariant enforcement
   clear. Choose the smallest useful design; avoid unnecessary inheritance, static
   helper collections or a framework. Nested public compatibility types may delegate;
   they must not be deleted merely to meet a line count.
3. Reject duplicate Topic positions without replacing the original entry or
   corrupting subsequent builder use. Preserve valid-input behaviour and the
   other established failure contracts. Investigate additional ambiguity without
   silently expanding this increment's semantic changes.
4. Document every resulting class/interface and public/protected operation,
   explaining responsibility, collaborators, invariants, compatibility, failures,
   immutability and neutral matching. Include non-obvious private logic. Count full
   physical production source lines; do not strip useful explanation to pass.
5. Extend meaningful tests for the changed responsibilities and run existing
   component/consumer suites. Plan and perform solution-wide regression and
   integration verification for the exact integrated candidate, along with required
   ArchUnit and relevant implementation-conformance checks. Establish the actual
   reactor modules, profiles, services and environment; a Maven phase name alone
   does not establish integration coverage.
6. Submit a formal Implementation Report referencing this item/revision and AC
   results, with contract → implementation → evidence correspondence. Retain the
   Task, actual commands, scope/environment, outcomes, failures and exceptions in
   the applicable iteration. Request Solution Architect acceptance through the
   solution-wide software HCRL; perform authorised corrective iteration and retesting
   within that development increment. This backlog-preparation HCRL supplies no
   implementation or software-release authority.

## Expected Outcomes

- Cohesive, documented Topic/TopicMask objects and support collaborators with a
  reviewable responsibility mapping and bounded diff.
- Explicit rejection of duplicate Topic positions, with valid consumer and
  serialisation contracts preserved and demonstrated by tests.
- Complete-file size evidence and any specifically justified, explicitly accepted
  exception; the other oversized files remain separately visible work.
- A solution-wide candidate with attributable conformance and verification evidence,
  an implementation-acceptance request and explicit limits. Completion of this
  item is not independent component acceptance or retrospective acceptance of Git.

## Acceptance Criteria

| Criterion | Observable result | Verification / expected evidence |
| --- | --- | --- |
| AC-1 | Each changed/new production object has one coherent responsibility and justified collaborators. Topic and TopicMask no longer concentrate the extracted support responsibilities. Every affected production Java file is at most 500 physical lines, including documentation/declarations, unless the Solution Architect explicitly accepts a documented per-file exception. | Review the bounded diff, responsibility → class mapping and full-file counts. No documentation removal, artificial fragmentation or unsupported base class. An exception request is not an accepted exception. |
| AC-2 | Existing public packages, constructors, methods, nested type identities, enum values, valid-input factory behaviour, UUID derivation, equality/hash semantics and supported JSON/Java-serialisation behaviour remain compatible. | Compare the pre-change public surface and representative pre-change serialised fixtures with the candidate; exercise legacy constructors/factories, TopicSubscription and model/selector consumers. No import migration, removed alias or changed wire field is hidden in the extraction. |
| AC-3 | Topic construction preserves explicit contiguous sequencing from zero, derived names, positional qualifier defaults, immutable entries/maps and defensive Date copies. Repeated explicit positions, including repeated identical entries, are rejected with a documented IllegalArgumentException without overwriting prior builder state. | Tests include ordered/out-of-order contiguous input, gaps/negative positions, duplicate explicit calls through both overloads, mixed explicit/automatic calls, null/blank required values, input/output mutation attempts and builder reuse after rejection. A constructor is not claimed to detect history already lost in an input Map. |
| AC-4 | TopicMask retains one name/canonical matching basis, independent UUID, entry-local regex matching, Exact/Inclusive depth, unrestricted behaviour and the existing supported factory shorthand. Null candidates do not become matches; invalid regex fails as documented. | Positive/negative vectors for both bases, qualifier/name discrimination, shallower/equal/deeper candidates, explicit entries whose names contain dots, invalid regex, null candidates, immutability, serialisation and repeatable evaluation. Preserve existing compatibility defaults without defining a new universal regex/wire language. |
| AC-5 | Educational documentation explains every resulting class/interface and all public/protected operations, plus non-obvious private logic, including failure and compatibility contracts. | Documented review and a concise documentation/conformance inventory. Boilerplate comments, coverage percentages or compilation alone do not satisfy this criterion. |
| AC-6 | The exact integrated Harmonia candidate has applicable component/unit, solution-wide regression, solution-wide integration and architectural/conformance results. No result or required scope is silently omitted or represented as a pass. | Record actual bounded commands, source state, modules/scenarios, environment/dependencies, outcomes and material failures. Show consumer coverage, not just Calliope success. Required unavailable, failed, timed-out, stalled or unexecuted verification needs explicit Solution Architect disposition before being treated as satisfied. Historical Architecture qualifications are not software-test waivers. |
| AC-7 | The Implementation Report and pending software HCRL identify this item, revision and reviewed source, map AC-1–AC-6 to evidence, and preserve exact candidate correspondence and scope. No unapproved architectural change is included. | Formal Task/Report/evidence links under the assigned iteration, bounded diff and candidate locator; Solution Architect implementation acceptance/qualifications recorded through the common HCRL. First software version/baseline and subsequent release closure require their actual authority; this item allocates neither. |
