Task-Issued-At: UNRESOLVED

Event source: The Solution Architect assignment and amendment entered the agent interaction together. Their exact attributable system entry timestamp is unavailable, and the filename prefix requires Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Topic/TopicMask design acceptance and finalisation

- **Authority** · Mark Hunter, Solution Architect. The issued assignment records the disposition, and its amendment places the acceptance after the establishment of Iteration 006. The exact time of acceptance is unavailable.
- **Agent and scope** · Codex. DR-10 documentation finalisation for `software-0.1.0` in [Iteration 007][boundary].
- **Review basis** · [HCRL.004 / DR-10][review] and [Draft.001.003][source]. The exact reviewed draft at entry has SHA-256 `97a025db78fc6ff746a47df5b98ab68c926b3d89710ea1cff51009b0c7c22259` and is present at entry HEAD `8af315134a1f2e03e75512f66513ef2f7eeb80b8` on `feature/BL-CALLIOPE-OO-01`.
- **Preceding boundary** · [Iteration 006][v001] was already established for Mneme–Mnemosyne V001. Its pending implementation and evidence remain outside this assignment. The Architecture baseline used for scope correspondence is `1.1.26`. The software predecessor remains open in HCRL.004 / DR-6.

## Operative instruction and amendment

~~~markdown
# Codex Assignment — Topic/TopicMask Design Acceptance

## Authority

The Solution Architect has reviewed and accepted:

`DRP-Topic-and-TopicMask.draft.001.003.md`

**Disposition: Accepted with qualifications.**

This acceptance covers the runtime Topic/TopicMask model, matching semantics, construction, canonical representations and proposed Java API.

## 1. Finalise the DRP

Create:

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md`

Use Draft.001.003 as the accepted source.

Preserve its architectural content and all qualifications.

Update status, acceptance metadata and references as necessary.

Preserve Draft.001.003 unchanged.

## 2. Record outstanding qualifications

Retain the following obligations:

1. Historical Topic UUID references, registry compatibility and persisted selector migration.
2. Legacy selector equivalence, including security-sensitive matching.
3. Fully qualified IU Topic representation and canonical qualifier vocabulary.
4. Release-specific limits, RE2/J compatibility and resource safeguards.
5. Logging integration, performance and reliability evidence.
6. Actual Java implementation and consumer conformance.
7. Any required Architecture Continuum reconciliation and separately authorised incorporation.

Accept the proposed Java API choices in §8, including `Optional<String>`, deterministic `String.compareTo` ordering and the proposed exception contracts.

Do not reopen settled F-01 decisions.

## 3. Update HCRL.004

Under DR-10:

- Record the Solution Architect's qualified acceptance.
- Identify Final.001.001 as the accepted Topic/TopicMask design.
- Preserve the F-01 decision lineage.
- Carry forward the outstanding obligations.
- Preserve all unrelated decisions and review states.

Do not close HCRL.004 or the collective G03 review.

## 4. Evidence

Create the applicable Task and Report evidence using existing repository conventions.

Verify the final document against Draft.001.003 and confirm that no substantive architectural changes have been introduced.

Report any existing evidence-layout or validation failures separately.

## 5. Constraints

Do not:

- Modify software.
- Modify the Architecture Continuum.
- Modify other accepted DRPs.
- Create or revise implementation backlog items.
- Make additional architectural decisions.
- Commit, tag or push.

## 6. Deliverables

Return:

1. `DRP-Topic-and-TopicMask.Final.001.001.md`
2. Updated `hcrl-software-0.1.0-004.pending.md`
3. Concise finalisation report.

**Objective:** Formalise the qualified acceptance of Topic/TopicMask Draft.001.003 so the accepted design can govern subsequent Calliope implementation.



**Amendment — Topic/TopicMask Final.001.001 Acceptance**

The original finalisation assignment remains authorised, subject to the following clarification.

**1. Original review authority**

HCRL.004 / DR-10 remains the review record for `DRP-Topic-and-TopicMask.draft.001.003.md`.

Record the Solution Architect's qualified acceptance there, preserving the original F-01 review history and outstanding obligations.

**2. Acceptance provenance**

The Solution Architect's acceptance occurred after Iteration 006 had already been established.

Do not retrospectively attribute the acceptance event to Iteration 004.

Preserve the actual acceptance provenance through an attributable Task/Report record in a new, explicitly identified evidence boundary.

Use **Iteration 007**, provided it is unused and its allocation is consistent with the existing governance conventions.

If Iteration 007 is already allocated, report the collision rather than overwriting existing records.

**3. Iteration 006**

Iteration 006 belongs to Mneme–Mnemosyne V001.

Do not modify its HCRL, implementation evidence or acceptance standing.

Its implementation remains pending review and subsequent Calliope dependency reconciliation.

**4. Implementation**

Iteration 007 is intended to support the return to Calliope development.

This assignment authorises **DR-10 documentation finalisation only**.

Do not implement Calliope software, create an implementation candidate or treat the future implementation backlog as accepted.

The implementation assignment will be separately authorised after the design finalisation.

**5. Remaining instructions**

Preserve the original deliverables and constraints:

- Topic/TopicMask Final.001.001.
- Updated HCRL.004 / DR-10.
- Task and Report evidence.
- No Architecture Continuum changes.
- No software modifications.
- No commit, tag or push.

**The objective remains formalisation of the already accepted design, not another architectural review.**
~~~

[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[boundary]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-007.pending.md
[v001]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-006.pending.md
[source]: ../../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md
