Task-Issued-At: UNRESOLVED

Event source: The interaction did not expose the attributable instruction-entry event timestamp. Solution Architect treatment is required before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Details — F-01 Topic/TopicMask Design Consolidation

- **Attribution:** User-issued instruction in the Codex interaction for the Harmonia workspace. The linked adjudication record supplies the Solution Architect decisions.
- **Evidence boundary:** `software-0.1.0/002` as explicitly assigned. The [software iteration 001 review](../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md) establishes prior candidate context.
- **Backlog correspondence:** [BL-CALLIOPE-OO-01, revision 1](../../../../../../docs/backlog/calliope-oo-01-topic-models.md), Git blob `3c93558550da6a140a8668e120638efb8e9a368d`. The [wider Calliope item 00001, revision 001](../../../../../../docs/backlog/BacklogItem-0001-Calliope-Model-Restructure.001.md), blob `88c21095fcabd2909fa4d42e661358b08147e0fe`, explains the prior candidate's wider scope.
- **Design input:** [Accepted F-01 adjudication record](../F-01-Topic-TopicMask-Adjudications.md).
- **Source state at entry:** Branch `feature/BL-CALLIOPE-OO-01`, HEAD `fd52408e3d8be433bb43eedd2a448354646d7518`, Architecture `1.1.26`. Evidence uses the adjudication record as supplied in the working tree.

## Operative instruction

The request text is preserved below inside the interaction's enclosing code fence.

```
# Harmonia — F-01 Topic/TopicMask Design Consolidation

**Repository:** `fhirfactory/harmonia`  
**Working branch:** `feature/BL-CALLIOPE-OO-01`  
**Software release:** `software-0.1.0`  
**Iteration:** `002`  
**Activity:** Design consolidation and implementation gap analysis  
**Status:** Authorised for documentation preparation only; no implementation authorised.

## 1. Objective

Consolidate the Solution Architect's accepted F-01 Topic/TopicMask adjudications into a revised Design Review Package (DRP).

The adjudication record is the authoritative input for this activity.

The existing Final.001 DRP is the historical predecessor and must remain unchanged.

## 2. Authoritative Inputs

### 2.1 Accepted adjudications

Read:

`enterprise-architecture/design/evidence/software-0.1.0/002/before/F-01-Topic-TopicMask-Adjudications.md`

This document records the Solution Architect's F-01.1–F-01.44 adjudications, including subsequent corrections, refinements and qualifications.

**Do not reconstruct decisions from earlier DRPs where they conflict with these adjudications.**

If a decision is incomplete, inconsistent or ambiguous, report the issue for Solution Architect adjudication rather than inventing a resolution.

### 2.2 Existing DRP

Read:

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.md`

Preserve this file unchanged.

Its purpose is to provide the predecessor design, architectural context and evidence for the decision-delta analysis.

### 2.3 Related architecture

Inspect relevant architectural sources only where necessary to identify dependencies or contradictions.

Particular attention should be given to:

- Topic identity and canonical naming.
- Information Unit Topic usage.
- TopicMask usage in Themis.
- TopicMask usage in subscriptions, routing and capability discovery.
- Existing Topic/TopicMask architectural identities.
- Harmonia logging architecture.

Use bounded excerpts and targeted searches. Do not load complete RAD documents unnecessarily.

## 3. Required DRP

Create:

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.draft.002.md`

The document must be a **complete, self-contained design**, not merely a list of changes to Final.001.

It must incorporate the accepted adjudications concerning:

1. Topic identity, equality, hashing and natural ordering.
2. TopicEntry structure, sequencing and immutability.
3. Canonical, versioned, relative, friendly and display names.
4. Optional TopicEntry versions.
5. Generated `N0`, `N1`, etc. values.
6. Topic hierarchy, ancestry and containment.
7. TopicMask structure, equality and construction.
8. Positional qualifier, value and version RegEx matching.
9. `includeContainedElements`.
10. Explicit universal masks.
11. Literal and RegEx helper methods.
12. RE2/J matching semantics.
13. Canonical-expression escaping and parsing.
14. Logical serialization requirements.
15. Release-defined validation and resource limits.
16. Thread safety and performance constraints.
17. Fail-closed matching behaviour.
18. Diagnostic logging and Harmonia logging integration.
19. Mandatory behavioural acceptance tests.

Include proposed public Java API signatures where appropriate.

Clearly distinguish accepted architectural requirements from implementation recommendations.

## 4. Decision-Delta Reconciliation

Produce an explicit comparison between Final.001 and Draft.002.

At minimum, reconcile:

| Subject | Final.001 | Accepted F-01 decision |
|---|---|---|
| Topic identity | UUID | Non-versioned canonical name |
| TopicMask identity | UUID | Structural equality |
| Topic versions | Not supported | Optional per-entry String |
| Missing qualifier/value | Generated qualifier | Generated value |
| Ordering | Explicit sequence numbers | Ordered entries; sequence keys are implementation details |
| Containment | Exact / Inclusive | `includeContainedElements` |
| Mask matching | Separate name/canonical-name bases | Independent qualifier/value/version expressions |
| Universal mask | Default unrestricted | Explicit `TopicMask.all()` |
| RegEx engine | Undecided | RE2/J |
| Logging | General diagnostic helpers | Harmonia logging integration |

Do not silently resolve conflicts with other accepted architectural material.

Record such conflicts as reconciliation findings.

## 5. Implementation Gap Analysis

Inspect the existing Calliope Java implementation using targeted file discovery and bounded source inspection.

Identify differences between the current code and the accepted design.

Include:

- Relevant classes and packages.
- Existing public API contracts.
- Missing functionality.
- Incompatible behaviour.
- Dependencies requiring modification.
- Existing tests and missing behavioural coverage.

Produce an **exception-based report**, prioritising material deviations rather than repeating conformant implementation details.

Do not modify source code or tests.

## 6. Evidence Requirements

Use the established iteration evidence structure:

`enterprise-architecture/design/evidence/software-0.1.0/002/`

### `before/`

Contains the Solution Architect's accepted adjudication record.

Preserve it unchanged.

### `after/`

Create the execution evidence here.

Recommended deliverables:

- `F-01-Decision-Delta.md`
- `F-01-Architecture-Reconciliation.md`
- `F-01-Implementation-Gap-Analysis.md`
- `F-01-Execution-Report.md`

These files must distinguish:

- Accepted decisions.
- Incorporated design content.
- Unresolved architectural conflicts.
- Implementation gaps.
- Outstanding qualifications.

Do not create execution reports in `.junie/`.

## 7. Governance Constraints

This assignment authorises documentation consolidation and analysis only.

**Do not:**

- Modify Final.001.
- Modify the accepted adjudication record.
- Change the Architecture Continuum.
- Initiate or execute ADD.
- Modify Java source code.
- Modify tests.
- Initiate implementation.
- Publish an architecture or software release.
- Commit or push changes without explicit authorisation.

Draft.002 must remain pending Solution Architect review.

The adjudication record is authoritative for this task, but the revised DRP does not become an accepted architectural baseline merely because Codex has generated it.

## 8. Required Deliverables

Return:

1. The complete `DRP-Topic-and-TopicMask.draft.002.md`.
2. Decision-delta evidence against Final.001.
3. Architecture reconciliation findings.
4. Exception-based implementation gap analysis.
5. Execution report identifying completed work, limitations and outstanding qualifications.

## 9. Completion Criteria

The assignment is complete when:

- Every accepted F-01 adjudication is accounted for.
- Draft.002 is internally consistent.
- Differences from Final.001 are explicitly recorded.
- Conflicts with existing architecture are identified without unauthorised resolution.
- Implementation gaps are documented.
- All evidence is stored in the correct iteration directory.
- No implementation or architecture publication has occurred.

**Return the results for Solution Architect review. Do not proceed beyond this boundary.**
```
