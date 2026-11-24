Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Preparation task — BL-CALLIOPE-OO-01, revision 2

- **Assignment** · Solution Architect instruction to Codex, reproduced below.
- **Review context** · [Software 0.1.0 / Iteration 008][hcrl], Architecture 1.1.26.
- **Input** · [BL-CALLIOPE-OO-01, revision 1][rev1], [accepted design][design] and [independent implementation review][review].
- **State at entry** · `feature/BL-CALLIOPE-OO-01` at `8af315134a1f2e03e75512f66513ef2f7eeb80b8`, with the uncommitted candidate and other pre-existing changes. Scope is proposal preparation and evidence.
- **Current work specification** · [Backlog Item 0003, revision 000.002][current-backlog] under the later [identity correction][identity-correction].

## Operative instruction

~~~text
# Codex Assignment — Calliope Backlog Revision 2

## 1. Authority and objective

**Solution Architect instruction:** Prepare revision 2 of the existing Calliope Topic/TopicMask Backlog Item.

**Existing Backlog Item:**
- ID: `BL-CALLIOPE-OO-01`
- Revision: `1`
- Path: `docs/backlog/calliope-oo-01-topic-models.md`

**Accepted design:**

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md`

**Review evidence:**

`enterprise-architecture/design/evidence/software-0.1.0/008/after/ai-agent-reports/20261124T005629Z-calliope-topic-topicmask-independent-review.md`

The objective is to reconcile the Backlog Item with the accepted Topic/TopicMask design and the findings of the independent implementation review.

Do not reopen accepted architectural decisions.

## 2. Prepare revision 2

Inspect revision 1 in full, including its scope, implementation expectations and acceptance criteria.

Create a separate revision 2 file using the repository's established backlog versioning convention.

Preserve revision 1 unchanged.

Maintain the existing Backlog Item identity and traceability.

### AC-2 — Compatibility

Replace obsolete requirements to retain the previous runtime Topic/TopicMask UUID identity, canonical-map API and incompatible public serialization contracts.

Require implementation of the accepted Java API and logical information model.

Retain explicit obligations to identify and reconcile applicable historical:

- Topic and TopicMask identifiers and references.
- Registry uniqueness and alias rules.
- Public APIs and consumer contracts.
- Persisted selectors.
- JSON, XML and Java serialization boundaries where applicable.
- Security-sensitive selection and routing behaviour.

Historical compatibility must be demonstrated or addressed through an explicitly authorised migration or qualification.

Do not silently waive historical obligations.

### AC-3 — TopicBuilder indexed construction

Align with Final.001.001 §2.

A successful indexed addition to an occupied position replaces its previous value without increasing depth.

Preserve requirements for:

- Exclusive sequential/indexed construction modes.
- Nonnegative indices.
- Contiguous final ordering.
- Validation before mutation.
- Failed operations preserving builder state.
- Reusable builders.
- Immutable completed Topic snapshots.

Remove conflicting duplicate-index rejection requirements, including those outside AC-3.

### AC-4 — TopicMask semantics

Replace the historical UUID, dual-basis and unrestricted-default assumptions with the accepted design:

- Explicit ordinary and universal mask kinds.
- `TopicMask.all()` for universality.
- Ordered positional qualifier/value/version criteria.
- Exact-depth and contained-element matching.
- RE2/J full-field expressions.
- Explicit version-presence semantics.
- Canonical expression reconstruction.
- Fail-closed handling of invalid or ambiguous selectors.

Preserve applicable positive, negative, null, concurrency and consumer-conformance tests.

### Other backlog content

Reconcile any additional wording inconsistent with Final.001.001.

Preserve the original backlog's legitimate objectives and requirements that remain applicable.

Do not expand the backlog to encompass unrelated Calliope, Mneme, Mnemosyne or G03 functionality.

## 3. Existing implementation

Iteration 008 has already produced an implementation candidate.

Revision 2 must provide a coherent acceptance basis for that candidate and its corrections.

In particular, preserve traceability to the independent review findings:

- A-01 through A-06.
- B-01 and B-02.
- Applicable C-series evidence and compatibility obligations.

Do not turn optional improvement D-01 into a mandatory requirement.

The revised backlog must not imply that the existing candidate is accepted.

## 4. Governance and provenance

Use Iteration 008 as the existing review context.

Record the preparation of revision 2 in the appropriate Task/Report evidence, following repository conventions.

Do not retrospectively rewrite earlier implementation assignments or review outcomes.

Do not mark revision 2 accepted.

Do not close HCRL.008 or change its existing Solution Architect dispositions.

Any proposed HCRL amendment must be presented separately for approval.

## 5. Verification

Check that:

1. Every revision 1 acceptance criterion is retained, superseded or explicitly dispositioned.
2. Revision 2 is consistent with Final.001.001.
3. The independent review's backlog findings are addressed.
4. Historical compatibility obligations remain visible.
5. All links and references resolve.
6. Revision 1 remains unchanged.

Provide a concise revision 1 → revision 2 reconciliation identifying substantive changes.

## 6. Constraints

Do not modify:

- Production software or tests.
- Accepted design documents.
- Architecture Continuum.
- Existing accepted backlog revisions.
- Mneme/Mnemosyne V001.
- Existing HCRL decisions.

Do not commit, tag, push or allocate a software release.

## 7. Deliverables

Return:

1. Proposed `BL-CALLIOPE-OO-01`, revision 2.
2. Revision 1 → revision 2 reconciliation.
3. Task/Report evidence.
4. Any genuine unresolved backlog decisions requiring Solution Architect adjudication.

**Stop at submission for Solution Architect review.**

The next step will be acceptance of revision 2, followed by a bounded Junie correction assignment under the existing Calliope Iteration 008 review.
~~~

[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-008.pending.md
[rev1]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[review]: ../../after/ai-agent-reports/20261124T005629Z-calliope-topic-topicmask-independent-review.md
[current-backlog]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md
[identity-correction]: unresolved-calliope-backlog-identity-correction.md
