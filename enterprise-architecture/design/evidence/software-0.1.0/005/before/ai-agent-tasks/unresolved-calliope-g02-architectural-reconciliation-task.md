Task-Issued-At: UNRESOLVED

Event source: Codex interaction. The attributable original instruction-entry timestamp is unavailable. The Task event and filename prefix require Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Calliope iteration 005 G02 architectural reconciliation

**Attribution and scope** · The user issued the instruction below in the Codex interaction. It commissions read-only architecture and candidate analysis with a reconciliation document for Solution Architect adjudication. Existing review evidence establishes the `software-0.1.0/005` evidence boundary. [Issue inventory](../../after/Calliope-Architectural-Issue-Groups-005.md)

**Originating state** · Branch `feature/BL-CALLIOPE-OO-01`, HEAD `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`, commit subject `Software-Version-0.1.0-Snapshot-004`, and [Architecture release 1.1.26](../../../../../../../architecture-release.json). [HCRL.004 DR-1](../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-1-whole-of-calliope-scope-and-candidate) locates the reviewed implementation and the accepted historical [BL-CALLIOPE-OO-01 revision 1](../../../../../../../docs/backlog/calliope-oo-01-topic-models.md). This instruction specifically commissions A03–A05 analysis.

## Operative instruction

~~~markdown
# Harmonia — Calliope Review Iteration 005
## G02 — Governed Topic Identity, Reference Migration and Classification Vocabulary

### Working context

Repository: `fhirfactory/harmonia`  
Branch: `feature/BL-CALLIOPE-OO-01`  
Software snapshot: `Software-Version-0.1.0-Snapshot-004`  
Accepted Architecture Continuum: `1.1.26`

Current cumulative review:
`hcrl-software-0.1.0-004.pending.md`

Current architectural issue inventory:
`Calliope-Architectural-Issue-Groups-005.md`

### Objective

Investigate **G02 — Governed Topic Identity, Reference Migration and Classification Vocabulary**, covering findings A03, A04 and A05.

Prepare a focused architectural reconciliation document for Solution Architect adjudication.

This is an evidence-gathering and analysis assignment, not an architectural implementation or decision-making assignment.

### 1. Establish existing architectural authority

Examine the relevant accepted architectural definitions, including:

- Topic identity, naming, sequencing and registry semantics.
- TopicEntry structure and version semantics.
- Information Unit identity and its relationship to Topic identity.
- Canonical classification and vocabulary ownership.
- Applicable decisions from F-01.1–F-01.81.
- `DRP-Topic-and-TopicMask.draft.001.003.md`.

**Do not reopen the 81 previously settled Topic/TopicMask decisions.**

Where the current candidate differs from accepted Architecture Continuum definitions, identify the discrepancy precisely.

Distinguish an unresolved migration obligation from a genuine architectural contradiction.

### 2. Investigate A03 — Topic identity and reference migration

Determine:

- Which existing Topic UUIDs must be preserved.
- How legacy Topic references correspond to structural runtime identity.
- The governing scope of Topic registry uniqueness.
- How ordered TopicEntry sequences are preserved.
- How aliases and historical names are handled.
- Whether generated and explicitly supplied qualifiers can collide.
- Which existing consumers depend on legacy Topic identity or naming behaviour.

Identify unresolved architectural decisions separately from implementation-conformance requirements.

### 3. Investigate A04 — IU Topic vocabulary and boundary mappings

Examine the accepted IU Topic identity and naming conventions.

Identify the exact architectural questions concerning:

- Qualified versus unqualified IU Topic names.
- IU UUID versus Topic UUID.
- Canonical Topic representations.
- Qualifier vocabulary and ordering.
- Persistence, messaging, transport, matching and reference boundaries.
- Existing compressed UUID spelling and compatibility requirements.

Do not infer architectural authority from existing Java factories or positional constructor parameters.

Where existing design notes use terminology inconsistently with accepted F-01 decisions, identify the discrepancy without independently resolving it.

### 4. Investigate A05 — Classification vocabulary ownership

Identify existing classifications and vocabularies associated with:

- Topic.
- Information Unit.
- Relationships.
- Lifecycle.
- Other relevant Calliope models.

Distinguish:

- Governed architectural classifications.
- Closed base vocabularies.
- Open classifications.
- Native standards or codes.
- Runtime registries and their responsibilities.

Determine whether multiple representations express the same underlying classification or genuinely different concepts.

Do not consolidate distinct concepts merely because their values or names overlap.

### 5. Prepare the adjudication document

Create:

`Calliope-G02-Architectural-Reconciliation-005.md`

Place it in the established Iteration 005 review evidence directory.

For each finding, document:

1. The precise architectural question.
2. Accepted architectural authority.
3. Existing implementation or documentation evidence.
4. Confirmed discrepancies.
5. Matters already settled.
6. Outstanding decisions requiring Solution Architect adjudication.
7. Proposed alternatives, where justified.
8. Dependencies on G03, G04 or G06.
9. Implementation and verification obligations that do not require new architectural decisions.

Include an explicit traceability matrix connecting the findings to their source evidence.

### 6. Governance and scope

- Preserve all settled F-01 decisions.
- Preserve accepted architectural identities and relationships.
- Do not modify the Architecture Continuum.
- Do not revise the Topic/TopicMask DRP.
- Do not modify the accepted G01 DRP.
- Do not modify implementation code.
- Do not modify HCRL.004.
- Do not create HCRL.005 yet.
- Do not commit or push.

Use bounded repository excerpts and targeted source examination. Avoid unnecessarily loading large RAD documents.

### Deliverable

Return the completed G02 reconciliation document and a concise summary identifying:

- Architectural questions requiring adjudication.
- Matters already resolved by accepted authority.
- Actual conflicts, if any.
- Implementation-only concerns.
- Recommended adjudication sequence.

**Stop for Solution Architect review before undertaking any architectural changes.**
~~~
