Task-Issued-At: UNRESOLVED

Event source: The Codex interaction exposes the operative instruction and its attribution without a system-recorded instruction-entry time. Solution Architect treatment is required before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — G03 Pragma family cross-document reconciliation

The Solution Architect requested an exception-based review of the exact five Pragma-family candidates in the instruction below. The originating worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with Architecture 1.1.26 and an existing cumulative [DR-12][review].

## Operative instruction

The following preserves the issued wording inside the enclosing message fence.

~~~
# Harmonia — Iteration 005 / G03
## Pragma Family — Cross-Document Consistency and Completeness Review

### Objective

Perform a comprehensive, exception-based architectural reconciliation of the five revised Pragma-family DRPs.

The objective is to establish whether they collectively describe one coherent, complete architectural model suitable for final Solution Architect adjudication.

**Do not revise the DRPs during this activity.**

Identify discrepancies and propose bounded corrections for subsequent authorisation.

### 1. Review scope

Review the following exact candidate revisions:

1. `DRP-Mneme-Pragma-Model.draft.000.003.md`
2. `DRP-Mneme-Pragmaton-Model.draft.000.003.md`
3. `DRP-Pragma-Publication-and-Subscription.draft.000.002.md`
4. `DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.003.md`
5. `DRP-Interaction-Petasos-to-Mneme.draft.000.003.md`

Review their associated Iteration 005 reconciliation evidence.

Consult relevant accepted Continuum elements, existing DRPs and Design Notes only where necessary to verify architectural correspondence.

Use bounded excerpts. Avoid unnecessary examination of large RAD documents.

### 2. Architectural consistency

Verify that the five DRPs consistently describe:

**Work information and identity**
- Reusable work definitions.
- Originating archetypal Pragma.
- Subscriber-specific Pragmatosis.
- Stimulus and Noesis specialisation.
- Independent identity, progression, outcome and lineage.
- Exactly one input InformationUnit and zero or more generated outputs.
- Immutable, non-authoritative InformationUnit summaries.

**Publication and subscription**
- InformationUnit eligibility through PublishingMask.
- Pragma-only subscription framework.
- Subscription matching against Pragma and input IU metadata.
- Mneme Router/DecisionEngine responsibilities.
- Subscriber-specific realisation through PragmaFactory.
- Fan-out, filtering and zero-match outcomes.

**Execution information**
- Pragmaton as the Mneme-managed execution-information environment.
- Ergon.WorkSpace as its execution-facing abstraction.
- JavaSpaces-inspired associative access.
- ContextLoader and ProcessingEngine responsibilities.
- Ergon-InformationAdapter mediation.
- Workflow Cache and Standard Cache separation.
- Themis-governed access and lazy resolution.

**Transport and execution**
- Authoritative realisation before acknowledged durable handoff.
- PetasosParcel referencing the existing realisation.
- Receiver resolution rather than creation of the undertaking.
- Independent delivery, execution, save and persistence outcomes.
- Duplicate delivery, retry, recovery and `DoNotBefore`.
- No internal resumption of a failed Ergon.

**Assurance**
- Kleio audit, provenance and OA&M responsibilities.
- Dokimasia assurance against defined Praxis.
- Correlation across publication, subscription, handoff and execution.

### 3. Architectural reconciliation

Identify contradictions involving:

- The accepted Continuum.
- Qualified Domain05 work/task definitions.
- PetasosParcel reference and handoff semantics.
- Historical ActionableTask/FulfillmentTask terminology.
- Pragma versus Pragmatosis identity.
- Pragmaton lifecycle and WorkSpace correspondence.
- InformationUnit revision and authority.
- Existing Topic/TopicMask contracts.
- Themis and trusted security context.
- Existing scheduling and recovery responsibilities.

Distinguish:

1. **Actual contradictions between the five candidates.**
2. **Candidate-to-Continuum differences requiring incorporation.**
3. **Missing architectural contracts requiring Solution Architect adjudication.**
4. **Software implementation decisions that do not require further architecture.**
5. **Editorial or cross-reference corrections.**

Do not report an already-agreed conceptual relationship as unresolved merely because incorporation remains pending.

### 4. Exception-based review

For each genuine exception, record:

| Field | Requirement |
|---|---|
| Exception ID | Stable identifier |
| Subject | Concise description |
| Affected DRPs | Exact revisions and sections |
| Existing authority | Applicable accepted architectural source |
| Discrepancy | Specific contradiction, gap or qualification |
| Recommended treatment | Minimal correction or decision required |
| Impact | Architectural, contractual, implementation or editorial |
| Disposition | Pending Solution Architect adjudication |

**Do not manufacture exceptions merely to demonstrate review coverage.**

If an area is consistent, record that conclusion in a concise coverage summary.

### 5. Completeness assessment

Determine whether the five DRPs collectively provide sufficient architectural definition to support:

- Governed Continuum incorporation.
- Interaction contract refinement.
- Implementation backlog derivation.
- Verification and acceptance criteria.

Identify missing information only where its absence prevents a meaningful architectural decision or implementation contract.

Do not expand the architecture into unnecessary implementation detail.

### 6. Deliverables

Create:

`G03-Pragma-Family-Cross-Document-Reconciliation-005.md`

Include:

1. Executive reconciliation result.
2. Review coverage matrix.
3. Exception register.
4. Candidate-to-Continuum differences.
5. Proposed bounded corrections.
6. Remaining Solution Architect decisions.
7. Recommendation concerning readiness for collective DRP adjudication.

Update **DR-12** in:

`hcrl-software-0.1.0-004.pending.md`

Record the review result and its evidence reference.

Preserve all previous HCRL issues, dispositions and carry-forward obligations.

### 7. Constraints

- No changes to the five DRPs.
- No application-code changes.
- No accepted Continuum modifications.
- No architectural incorporation.
- No commit, tag or push.
- Preserve all historical evidence.
- Maintain Iteration 005 evidence conventions.
- Do not claim formal acceptance or close DR-12.

### Expected result

A concise, evidence-backed assessment establishing whether the five Pragma-family DRPs are mutually consistent and identifying the smallest set of corrections or decisions necessary before collective acceptance.

**Review by exception. Preserve established decisions. Do not reinvent the architecture.**
~~~

[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
