Task-Issued-At: UNRESOLVED

Event source: The operative Solution Architect implementation assignment entered the agent interaction. Its exact attributable system entry timestamp is unavailable; the filename prefix and event time remain unresolved for Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Junie Implementation Assignment
## Calliope Topic/TopicMask — Software 0.1.0 / Iteration 008

**Implementation Agent:** Junie — Claude Opus 5.5  
**Assignment:** Implement the accepted Topic/TopicMask model  
**Architecture Baseline:** 1.1.26  
**Software Candidate:** 0.1.0

### 1. Authorisation and traceability

The Solution Architect authorises implementation of the accepted Topic/TopicMask design.

**Backlog authority:**
- ID: `BL-CALLIOPE-OO-01`
- Revision: `1`
- Path: `docs/backlog/calliope-oo-01-topic-models.md`

**Accepted design authority:**

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md`

**Design acceptance:**
- HCRL.004 / DR-10 — Accepted with qualifications.
- HCRL.007 — Acceptance provenance and finalisation.

**Implementation provenance:**
- Iteration: `008`
- HCRL: `hcrl-software-0.1.0-008.pending.md`
- Evidence: `enterprise-architecture/design/evidence/software-0.1.0/008/`

The Solution Architect explicitly authorises establishment of Iteration 008 for this implementation, provided that iteration is not already allocated.

If a collision exists, report it rather than overwriting existing evidence.

This authorisation permits implementation and testing, not software acceptance, release or commit/push.

### 2. Objective

**Implement the complete accepted Topic/TopicMask model in Calliope.**

The deliverable is working Java software, integrated with its existing consumers and supported by executable tests.

Do not substitute an implementation plan, gap analysis or new architectural design for implementation.

### 3. Implementation scope

Implement the contracts established in Final.001.001, including:

**Topic and TopicEntry**
- Immutable typed Topic entries.
- Ordered qualifier/value/version representation.
- Canonical identity, equality and hashing.
- Deterministic natural ordering.
- Strict character and structural validation.
- Canonical parsing and formatting.
- Versioned and non-versioned representations.
- Hierarchical navigation.

**TopicBuilder**
- Sequential and indexed construction.
- Generated qualifiers.
- Continuity validation.
- Reusable builder semantics.
- Immutable resulting snapshots.

**TopicMask and TopicMaskEntry**
- Ordinary and universal masks.
- Structural, positional matching.
- Exact-depth and contained-element matching.
- Independent qualifier, value and version criteria.
- RE2/J expression compilation and matching.
- Literal helpers.
- Canonical mask expressions.
- Strict parsing and logical reconstruction.

**TopicMaskBuilder**
- Fluent construction.
- Validated operations.
- Reusable builder semantics.
- Immutable results.

Implement the accepted Java API in Final.001.001 §8.

Do not independently revise accepted method signatures or semantics.

### 4. Existing implementation

Inspect the existing Calliope classes and their consumers.

Reuse appropriate implementations.

Replace obsolete Topic/TopicMask behaviour where necessary.

The accepted design supersedes conflicting candidate software behaviour within the authorised scope, subject to its explicit compatibility and migration qualifications.

Preserve unrelated Calliope functionality.

Follow the Implementation Guide, including the 500-line source-file limit and object-oriented responsibility boundaries.

### 5. Consumer compatibility

Identify and update directly affected consumers as necessary.

Pay particular attention to:

- Topic identity and former UUID references.
- Topic and mask serialization.
- Existing selector implementations.
- InformationUnit Topic designation.
- Security-sensitive selection.
- Publication, subscription and routing consumers.

**Do not silently broaden selector matching.**

Invalid or ambiguous legacy selectors must fail closed unless an explicitly approved migration establishes equivalent meaning.

The fully qualified IU Topic qualifier vocabulary remains a separate architectural decision.

Do not invent that vocabulary.

Preserve the established unqualified IU Topic designation and independent IU UUID.

Where an exact mapping cannot be completed without the outstanding decision, isolate and report that dependency without abandoning independent implementation work.

### 6. Dependencies and architecture

Declare RE2/J directly in the implementing Maven module.

Use the existing Harmonia logging infrastructure.

Do not introduce a new logging, caching, authorisation or service-discovery framework.

TopicMask is a neutral selector. A successful match does not grant authority to access, publish, disclose, route or persist information.

Preserve Themis's independent authorisation responsibilities.

Release-specific resource limits must be established and tested. Where the accepted design requires an additional release decision, identify the issue and use a clearly qualified implementation boundary rather than inventing architectural authority.

### 7. Mandatory testing

Implement the behavioural acceptance criteria in Final.001.001 §10.

This includes:

- Identity and ordering.
- Qualifier generation.
- Character validation.
- Builder operations and immutability.
- Canonical representation round trips.
- Universal mask semantics.
- RE2/J expression matching.
- Version-presence matching.
- Expression parsing and reconstruction.
- Legacy compatibility.
- Concurrent evaluation.
- Fail-closed behaviour.
- Resource limits.
- Logging and diagnostic behaviour.

Execute relevant Calliope tests, consumer tests, architecture tests and the full Maven reactor.

Report actual coverage and any acceptance criterion that cannot yet be demonstrated.

Do not claim full conformance based only on successful compilation or existing tests.

### 8. Repository safety

Before implementation:

1. Verify branch, HEAD and working-tree state.
2. Confirm Iteration 008 is available.
3. Verify the accepted Backlog Item and Final.001.001.
4. Identify existing uncommitted changes.

**Important:** Mneme/Mnemosyne V001 has an uncommitted implementation candidate under Iteration 006.

Preserve those changes.

Use an isolated working tree or another demonstrably safe approach if necessary. Do not overwrite or unintentionally include V001 changes in the Calliope implementation candidate.

If safe isolation cannot be established, report the precise obstacle rather than modifying unrelated work.

### 9. Failure handling

Continue implementation when ordinary engineering difficulties arise.

Fix defects, adapt consumers and rerun tests.

Escalate only genuine architectural ambiguities or constraints that cannot safely be resolved within the assignment.

If an issue exposes a missing implementation guardrail, record the proposed guardrail in the Implementation Report.

Do not independently modify the Software Development Praxis or architectural authority.

### 10. Deliverables

Provide:

1. Implemented Calliope Topic/TopicMask software.
2. Required consumer adaptations.
3. Automated tests and execution evidence.
4. HCRL.008 pending review.
5. Implementation Task and Report.
6. Explicit outstanding compatibility, migration and conformance obligations.

Do not commit, tag, push or declare a release.

### 11. Completion

The assignment is ready for review when the accepted Topic/TopicMask functionality has been implemented, the applicable tests have been executed, and remaining qualifications are accurately documented.

The next step is an independent Codex implementation review followed by Solution Architect adjudication.

**Begin implementation. Do not stop at analysis or planning.**

## Entry state record and verification limitation

- Branch: `feature/BL-CALLIOPE-OO-01`
- HEAD: `8af315134a1f2e03e75512f66513ef2f7eeb80b8`
- Entry reference: the immediately preceding [Iteration 007 Finalisation Report](../../../../007/after/ai-agent-reports/20261123T204533Z-topic-topicmask-finalisation-report.md) records the same branch and HEAD and states that its 42 pre-existing modified/untracked files matched its entry state.
- Task-specific pre-edit status capture: not separately recorded by this agent before source edits. The branch/HEAD and pre-existing-worktree baseline above were taken from repository-held Iteration 007 evidence; this limitation is disclosed in the Iteration 008 Report.
- Iteration 008 HCRL and evidence paths: no pre-existing Iteration 008 HCRL or Task/Report file was found before the Task artifact was created.
- Session edit scope: source and test edits were confined to `calliope/`; provenance additions were confined to this Iteration 008 evidence boundary. No Hestia/V001 path was edited, and no Git operation changed the branch or index.
- Backlog authority: `BL-CALLIOPE-OO-01`, revision `1`.
- Accepted design: Final.001.001; HCRL.004 / DR-10 and HCRL.007 provenance.