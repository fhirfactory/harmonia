Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Iteration 008 corrective implementation verification task

The issuing context is the operative user message in the Codex interaction for the
Harmonia repository. The instruction-entry event timestamp is unavailable in the
interaction metadata exposed to this review. The following preserves the issued
assignment and its appended deserialization instruction.

```
# Codex Assignment — Iteration 008 Corrective Implementation Verification

## Authority

**Architecture baseline:** 1.1.26  
**Software candidate:** 0.1.0  
**Implementation iteration:** 008

**Accepted Backlog Item:**

`docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md`

**Accepted design:**

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md`

**Corrective Implementation Report:**

`enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/20261124T031123Z-calliope-topic-topicmask-corrective-implementation.md`

**Original independent review:**

`enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/20261124T005629Z-calliope-topic-topicmask-independent-review.md`

**Review record:**

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-008.pending.md`

## Objective

Independently verify Junie's corrected implementation against the original findings and the accepted Backlog Item.

This is a focused corrective verification, not a new architectural design review.

Inspect the actual corrected source and tests. Do not rely solely on the Implementation Report.

## 1. Verify A-01–A-06

For each original finding:

- Reproduce the original failure vector against the corrected implementation.
- Determine whether the defect is resolved.
- Inspect the relevant implementation for unintended changes.
- Verify the associated regression tests.
- Identify any remaining defects or unsupported assertions.

Pay particular attention to:

**A-01:** Malformed or ambiguous JSON must not produce a universal TopicMask.

**A-02:** Typed security action masks must survive serialization and retain their selection semantics.

**A-03:** Historical operational subject qualifiers must not be silently reinterpreted.

**A-04:** Supported historical Ergon Topic formats must be read without identity loss or ambiguous fallback.

**A-05:** Empty ordinary criteria must not be treated as unrestricted.

**A-06:** Documentation must satisfy the Implementation Guide, including the 500-line production-file limit.

## 2. Verify C-05 completion

Check the newly added tests against the missing mandatory acceptance vectors.

Confirm that the tests exercise the actual relevant implementations and configured serialization boundaries.

Do not accept tests that merely reproduce the implementation's own assumptions.

## 3. Historical compatibility review

Examine the three outstanding compatibility matters:

**Historical Topic UUIDs**

Determine whether the corrected interchange preserves the necessary information to maintain applicable historical references.

Do not require restoration of UUID-based Topic identity.

Identify any actual affected consumer or persisted-reference boundary that requires additional treatment.

**Java serialization**

Determine whether old native Java serialization compatibility is an applicable requirement at any identified supported boundary.

If no such boundary is established, classify this as a qualified compatibility obligation rather than automatically requiring a migration implementation.

**Legacy Topic names and selectors**

Examine the newly rejected historical inputs, including backslashes, whitespace, empty segments and selector syntax.

Distinguish:

- Demonstrably malformed or ambiguous input.
- Previously valid supported input.
- Inputs whose historical applicability is unknown.

Identify any unintended changes to security-sensitive or routing selection.

Do not invent unsupported historical compatibility requirements.

## 4. Verification evidence

Independently reconcile the reported results:

- Calliope: 292 tests, zero failures.
- Architecture: 90 tests, zero failures.
- Full reactor: 50 modules, 1,786 test instances, zero failures, ten skipped.

Confirm what the retained evidence actually establishes.

Review the exact source-state correspondence and the handling of unresolved timestamps and pre-edit provenance.

Do not classify skipped infrastructure tests as PASS.

## 5. Architectural and release qualifications

Preserve the distinction between:

1. Topic/TopicMask component implementation conformance.
2. Historical boundary compatibility.
3. Architecture Continuum reconciliation.
4. Integrated Harmonia software-release acceptance.

Review the outstanding B-02 and C-series qualifications only to determine whether the corrective implementation has changed their status.

Do not reopen settled Topic/TopicMask design decisions.

Do not require implementation of unrelated infrastructure.

## 6. Deliverables

Produce a focused independent verification report in the existing Iteration 008 evidence structure.

Include:

1. A-01–A-06 disposition: RESOLVED / PARTIALLY RESOLVED / UNRESOLVED.
2. C-05 verification outcome.
3. Any newly discovered corrective defects.
4. Historical compatibility findings requiring Solution Architect adjudication.
5. Remaining component acceptance blockers.
6. Remaining integrated software-release qualifications.
7. A recommendation on whether the Topic/TopicMask component implementation can be accepted, including any necessary qualifications.

## Constraints

- Review-only: do not modify production code or tests.
- Do not modify the accepted Backlog Item or design.
- Preserve Mneme/Mnemosyne V001 and its evidence.
- Do not change existing Solution Architect dispositions.
- Do not commit, tag, push or establish a release.
- Preserve earlier Task, Report and review evidence.
- Do not declare the candidate accepted.

**Stop after submitting the independent verification report for Solution Architect review.**
```

Also:

Does the strict TopicMask JSON decoder reject malformed input consistently through every supported deserialization path, including nested consumers?

In particular, verify that Jackson configuration or alternative construction paths cannot bypass the strict decoder and silently create a broader mask.
