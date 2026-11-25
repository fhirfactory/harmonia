Task-Issued-At: 2026-11-24T22:26:14.185Z

Event source: Codex interaction system rollout 01a20f52-81bf-7600-a71d-90e590b3bbec, timestamp of the user response_item carrying the operative instruction. This is instruction entry, not an inferred prior review time.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Topic and TopicMask Final.003 acceptance and incorporation preparation

**Agent** · Codex, architecture governance and documentation.
**Source** · Architecture 1.1.26, Software 0.1.0-SNAPSHOT,
`feature/BL-0003-Calliope-Topic-and-TopicMask`, HEAD
`f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`. The identified
[accepted Draft.002.004][draft] has SHA-256
`eee8a8f8eadb159e1a116b2f0e4e4411509beb78f77b9b69f5c6c2839cfb02a3`.
The previously accepted DRP release 002 is carried at its original
[Final.001.001 path][prior-final].

**Evidence association** · [Architecture 1.1.28/001][architecture], the
originating R1/R2 reconciliation and evidence boundary for the
[preceding design work][prior]. The operative Solution Architect
disposition accepts the design, supported API and diagnostic-handler
recommendations for this increment. The task supplies authority to
promote the DRP and prepare the ADD and software follow-on recommendations.
The Architecture transition, exact incorporation and [Software 010][software]
candidate disposition retain their separate review gates.

## Operative instruction

# Harmonia — Topic and TopicMask Final.003 Acceptance

**Role:** Architecture governance and documentation agent  
**Architecture baseline:** 1.1.26  
**Software:** 0.1.0-SNAPSHOT  
**Branch:** `feature/BL-0003-Calliope-Topic-and-TopicMask`

## Solution Architect disposition

The Solution Architect has reviewed and **ACCEPTED**:

`DRP-Topic-and-TopicMask.Draft.002.004.md`

Promote it to:

`DRP-Topic-and-TopicMask.Final.003.md`

Final.003 is the third accepted version of this DRP, succeeding Final.002.

## 1. Finalise the DRP

Create `Final.003.md` from `Draft.002.004.md`.

Preserve the accepted technical design.

Update:

- Document standing to `Final.003 — Accepted`.
- Design lineage and predecessor references.
- Statements describing accepted decisions as merely proposed.
- Decision-register entries that are resolved by this acceptance.
- References necessary for internal document consistency.

Do not reopen the accepted matching, versioning, range-validation, XML or resource-limit decisions.

The supported API and diagnostic-handler recommendations documented in Draft.002.004 are accepted for this increment. Retain their specified boundaries.

Remove obsolete draft-review language while preserving genuinely outstanding work concerning architecture incorporation, backlog revision, independent verification and release governance.

## 2. Preserve the development principle

The accepted design requires coherent refactoring of affected Harmonia components before Software 1.0.0.

There is no backward-compatibility requirement for superseded pre-1.0.0 implementations.

Retain the corresponding Software Development Praxis amendment for governance incorporation.

Do not introduce legacy software support.

## 3. Architecture incorporation

Prepare the necessary Architecture Continuum changes corresponding to Final.003.

Reconcile existing Topic and TopicMask architectural definitions, interfaces, relationships and dependencies with the accepted design.

Maintain architectural identity where the same governed architectural element continues to exist.

Do not retain superseded semantics merely for compatibility.

Use the established ADD/HCRL process.

**Do not declare an Architecture release complete or incorporated until its required reviews and verification have succeeded.**

## 4. Software boundary

The accepted DRP establishes design authority but does not independently authorise implementation.

Prepare the necessary follow-on disposition for Backlog Item 0003 and Software HCRL 010.

The next implementation assignment must use Final.003 and refactor affected code and tests to its contracts.

Do not modify production software or tests in this task.

## 5. Verification

Verify:

- Final.003 accurately reflects Draft.002.004.
- Accepted technical decisions are unchanged.
- No obsolete proposed status remains for accepted decisions.
- Remaining governance obligations are correctly distinguished.
- Links and references resolve within the applicable current document scope.
- Prior accepted releases and review evidence are not rewritten.
- No production implementation is modified.

Record existing validation failures separately from failures introduced by this change.

## 6. Deliverables

1. `DRP-Topic-and-TopicMask.Final.003.md`
2. Acceptance and promotion record.
3. Architecture incorporation proposal or applicable HCRL update.
4. Follow-on software backlog/HCRL recommendations.
5. Validation evidence.

Do not commit, push, merge, tag or publish.

**Stop after preparing the acceptance and incorporation artefacts for Solution Architect verification.**

[draft]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Draft.002.004.md
[prior-final]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[architecture]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
[prior]: ../../after/ai-agent-reports/20261124T222056Z-topic-drp-draft-002-004.md
[software]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
