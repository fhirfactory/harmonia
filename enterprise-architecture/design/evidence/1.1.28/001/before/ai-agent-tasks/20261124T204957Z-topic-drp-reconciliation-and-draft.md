Task-Issued-At: 2026-11-24T20:49:57.891Z

Event source: Codex interaction system rollout 01a20f52-81bf-7600-a71d-90e590b3bbec, timestamp of the user response_item carrying the operative instruction.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Topic/TopicMask DRP filename reconciliation and design draft

**Agent** · Codex, repository and architecture governance preparation.

**Starting state** · Architecture 1.1.26, software 0.1.0-SNAPSHOT,
`feature/BL-0003-Calliope-Topic-and-TopicMask`, HEAD
`f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`, clean index with pre-existing
working and untracked changes including the reviewed software candidate.

**Evidence association** · [Architecture 1.1.28/001][architecture] holds the
exact prior R1/R2 reconciliation and its accepted dispositions awaiting
incorporation. This Task and its Report document follow-on investigation and
proposal preparation associated with that source review. The task establishes
no resulting Architecture release or change to the reviewed 1.1.28 scope.
[Software 010][software] holds the originating software review.

## Operative instruction

```
# Harmonia — Topic/TopicMask DRP Version Reconciliation and New Design Draft

**Role:** Repository and architectural governance agent  
**Architecture baseline:** 1.1.26  
**Software:** 0.1.0-SNAPSHOT  
**Feature branch:** `feature/BL-0003-Calliope-Topic-and-TopicMask`

## Objective

Perform a bounded governance reconciliation of the Topic/TopicMask Design Review Package (DRP) naming convention and prepare the next architectural design draft.

The Solution Architect has clarified the authoritative DRP versioning convention.

The existing documents were incorrectly named because draft revision identifiers were carried into accepted Final filenames.

Correct this without losing accepted content, historical provenance, evidence or reference integrity.

Separately, prepare the next Topic/TopicMask DRP draft to incorporate the proposed simplified R1/R2 matching model.

**Do not modify production Java, tests, Maven configuration or the existing implementation candidate.**

## 1. Authoritative DRP naming convention

DRP filenames follow this lifecycle:

| Filename suffix | Meaning |
|---|---|
| `Draft.000.001` | First draft before the first accepted release |
| `Draft.000.002` | Second draft before the first accepted release |
| `Final.001` | First accepted release |
| `Draft.001.001` | First draft based on accepted release 001 |
| `Draft.001.002` | Second draft based on accepted release 001 |
| `Final.002` | Second accepted release |
| `Draft.002.001` | First draft based on accepted release 002 |
| `Draft.002.002` | Second draft based on accepted release 002 |
| `Final.003` | Third accepted release |

The first numeric component of a Draft identifies the **preceding accepted release**.

The second numeric component identifies the **draft iteration**.

A Final document identifies the resulting accepted release and has only one numeric component.

**Do not carry draft iteration numbers into Final filenames.**

Preserve the repository's established case-sensitive `Draft` and `Final` spelling.

## 2. Topic/TopicMask historical reconciliation

The following document identities require reconciliation:

| Existing filename | Correct filename |
|---|---|
| `DRP-Topic-and-TopicMask.Final.001.000.md` | `DRP-Topic-and-TopicMask.Final.001.md` |
| `DRP-Topic-and-TopicMask.Final.001.001.md` | `DRP-Topic-and-TopicMask.Final.002.md` |

Before changing anything:

1. Verify both documents exist.
2. Establish their accepted standing and provenance.
3. Identify all references to the existing filenames.
4. Identify any version references in document headers or other governed metadata.
5. Record the original file hashes and Git state.

Perform the reconciliation without changing accepted architectural semantics.

Preserve historical evidence referring to the original filenames. Do not rewrite historical reports merely to make their references appear current.

Where existing relative links must continue to resolve, determine whether references should be updated or compatibility locators retained.

Do not silently alter accepted evidence.

Record the reconciliation and its provenance through the applicable governance process.

## 3. Establish the naming convention

Locate the authoritative documentation-governance rules.

Prepare the smallest appropriate governance amendment defining the DRP naming convention in Section 1.

The convention must distinguish:

- Draft baseline version.
- Draft iteration.
- Accepted Final version.
- Draft-to-Final transition.
- Historical filename reconciliation.

Inspect existing validation mechanisms and determine how to detect incorrect DRP filenames automatically.

Prefer extending an existing validator over introducing a new validation framework.

Do not perform an unrestricted repository-wide renaming exercise.

Identify other incorrectly named DRPs as a separate bounded inventory for later disposition.

## 4. Create the new Topic/TopicMask design draft

Create:

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Draft.002.001.md`

Use the accepted content of the reconciled `Final.002` as the design baseline.

This is the **first draft based on accepted release 002**, proposing the changes that may eventually become `Final.003`.

The new draft is not accepted architecture.

### 4.1 Preserve existing Topic semantics

Preserve the accepted:

- Ordered TopicEntry structure.
- Qualified Topic identity.
- Version-independent Topic equality and ordering.
- Optional version data.
- Immutable Topic and TopicMask objects.
- Builder semantics.
- Strict canonical representations and parsing.
- Navigation and ancestry.
- Explicit universal TopicMask.
- Ordinary positional matching.
- Exact versus inclusive containment.
- Strict, lossless XML interchange.
- Bounded validation and safe failure handling.

Do not redesign these aspects.

### 4.2 Proposed R1/R2 text matching

Replace the current RE2/J-based qualifier and value expression semantics with a deliberately small wildcard language.

| Pattern | Meaning |
|---|---|
| `Patient` | Exact match |
| `Patient*` | Prefix match |
| `*Patient` | Suffix match |
| `*Patient*` | Contains match |
| `Patient*Record` | Prefix and suffix match |
| `*` | Match any valid text |

Additional requirements:

- Case-sensitive matching.
- Literal treatment of all characters other than unescaped `*`.
- No regex operators, grouping, character classes, flags or backreferences.
- No RE2/J or Java regex evaluation.
- At most two wildcard operators per field.
- Define an unambiguous mechanism for representing a literal asterisk.
- Preserve original pattern text for canonical reconstruction.
- Preserve the current independent qualifier/value positional evaluation model.
- Matching is against the complete decoded field.

Do not implement wildcard matching by translating the patterns into regular expressions.

The proposed matching mechanism should be directly implementable through bounded string operations.

Where wildcard escaping interacts with canonical escaping, define the two representations and their decoding order explicitly.

### 4.3 Version matching

The Solution Architect has identified numeric comparisons and ranges as the preferred direction for R1/R2 version matching.

Candidate operators include:

- `=`
- `<`
- `<=`
- `>`
- `>=`
- Bounded ranges.

However, the existing accepted design does not prescribe a version-ordering scheme.

**Do not silently choose a numeric version model.**

In the draft, identify the decisions necessary to establish:

1. Whether versions are integers, dot-separated numeric components or another representation.
2. How numeric components are compared.
3. How nonnumeric existing versions are handled.
4. How absent versions interact with constraints.
5. Whether wildcard and exact-text version matching remain available.
6. How version constraints are represented canonically and in XML.
7. How empty versus absent version constraints are distinguished.

Record unresolved choices explicitly for Solution Architect adjudication.

Do not present an unapproved version-comparison scheme as accepted architecture.

### 4.4 R3 separation

Record that richer matching potentially required for future:

- `InformationUnit.InformationRepresentation.type`
- `InformationUnit.InformationRepresentation.content.data`

is outside the R1/R2 TopicMask matching requirements.

Do not introduce an R3 content-matching implementation into the Naming model.

Preserve the possibility of a separately governed matching abstraction if future requirements justify one.

### 4.5 Resource and dependency implications

Identify obsolete RE2/J-specific design requirements, including:

- RE2/J dependency.
- Regex-specific lexical safeguards.
- Regex group nesting limits.
- Counted-repetition limits.
- Regex expansion estimates.
- Compiled-program instruction limits.

Retain applicable bounds on:

- Topic depth.
- Field lengths.
- Pattern lengths.
- Canonical representations.
- XML records.
- Temporary allocations.
- Matching execution.

The simplified approach must remain deterministic, bounded and fail-closed.

### 4.6 API containment and diagnostics

Incorporate the architectural concerns raised during the independent implementation review.

In particular:

- Minimise public interfaces.
- Keep internal parsing and evaluation machinery encapsulated.
- Review package containment in conjunction with Java visibility.
- Avoid exposing implementation-only diagnostic types.
- Preserve typed, bounded validation outcomes.
- Keep ordinary diagnostics separate from mandatory security auditing.

Consider narrowly scoped diagnostic-handler injection with disabled/no-op behaviour, but do not introduce a logging framework into Calliope.

Mark any new diagnostic API decisions requiring explicit Solution Architect approval.

## 5. Reconcile the independent implementation review

Review:

`enterprise-architecture/design/evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md`

Distinguish:

1. Defects that remain applicable under simplified matching.
2. RE2/J-specific findings superseded by the proposed design.
3. Public API and package-containment findings that remain applicable.
4. Verification and evidence defects requiring correction.
5. Open Solution Architect decisions.

Do not discard the independent review findings.

Do not silently rewrite the original review report or Junie's Implementation Report.

The implementation candidate remains unaccepted and unchanged.

## 6. Backlog and HCRL treatment

Do not automatically revise the accepted Backlog Item 0003 in this assignment.

Instead, identify the precise changes required for its next revision after architectural adjudication.

Likewise, identify necessary updates to the pending Software HCRL 010.

Preserve the existing candidate's provenance, hashes and independent review evidence.

The new draft must not be treated as implementation authority before Solution Architect acceptance.

## 7. Verification

Verify:

- The historical DRP identity mapping.
- Preservation of accepted design content.
- Correct relative links and document references.
- Draft and Final naming convention compliance.
- Consistent document standing and provenance.
- No modifications to implementation files.
- No changes to the accepted software candidate.
- No loss or rewriting of historical evidence.

Report any references that cannot safely be reconciled.

Do not fabricate timestamps or acceptance events.

## 8. Deliverables

Return:

1. DRP filename-reconciliation inventory.
2. Proposed or completed bounded historical reconciliation, subject to repository governance authority.
3. Proposed documentation-governance amendment.
4. New `DRP-Topic-and-TopicMask.Draft.002.001.md`.
5. List of unresolved architectural decisions.
6. Required Backlog Item 0003 changes.
7. Required HCRL 010 dispositions.
8. Verification results and exceptions.

**Do not implement the new matching model.**

Do not commit, push, merge, tag or publish.

Stop after producing the governed draft and reconciliation evidence for Solution Architect review.
```

[architecture]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
[software]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
