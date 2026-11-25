Task-Issued-At: 2026-11-24T23:42:29.560Z

Event source: Codex interaction system rollout 01a20f52-81bf-7600-a71d-90e590b3bbec, timestamp of the user response_item carrying the operative instruction.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Prepare the Final.003 Architecture incorporation candidate

**Agent** · Codex, Architecture incorporation and governance.
**Source** · Incorporated Architecture 1.1.26, Software 0.1.0-SNAPSHOT,
`feature/BL-0003-Calliope-Topic-and-TopicMask`, HEAD
`f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`.
[Accepted Final.003][final] SHA-256
`3902908dbffec9b35c15f3d31bf05b9b3a3e885dabecc71780c51fe4d9886043`.

**Evidence association** · [Architecture 1.1.28/001][hcrl], the existing
R1/R2 review. Start with the [prior Final.003 proposal][proposal], its companion
ADD, DR-5 patch and Praxis amendment. The Solution Architect approves the
architectural direction and pre-1.0.0 principle for this preparation. The
exact resulting candidate, transition sequence, application, verification
qualifications and closure retain their separate dispositions.

## Operative instruction

```
# Harmonia — Topic and TopicMask Final.003 Architecture Incorporation

**Role:** Architecture incorporation and governance agent  
**Accepted design:** `DRP-Topic-and-TopicMask.Final.003.md`  
**Current incorporated Architecture baseline:** 1.1.26  
**Software:** 0.1.0-SNAPSHOT  
**Branch:** `feature/BL-0003-Calliope-Topic-and-TopicMask`

## Objective

Prepare a complete, validated Architecture Continuum incorporation candidate for the accepted Topic and TopicMask Final.003 design.

Use the existing:

- `final-003-incorporation-proposal.md`
- `final-003-continuum-add.proposed.patch`
- `01-retained-topic-input-locator.patch`
- `software-development-praxis-pre-1-0-0.proposed.patch`

as the starting evidence and proposed changes.

**Do not restart the architectural analysis or regenerate the model unnecessarily.**

The Solution Architect has reviewed the Final.003 incorporation proposal and approved its architectural direction.

This assignment addresses incorporation completeness, removal of superseded content from current definitions, metadata consistency, governance and validation.

## 1. Preserve the accepted design

`DRP-Topic-and-TopicMask.Final.003.md` is accepted.

Do not reopen or alter its technical decisions.

The accepted design includes:

- Immutable Topic and TopicEntry.
- Ordered, qualified Topic identity without a runtime UUID.
- Version-independent Topic equality, hashing and natural ordering.
- Opaque optional Topic versions.
- Strict canonical naming and reconstruction.
- Ordinary and explicitly universal TopicMasks.
- Six supported qualifier/value wildcard forms.
- Exact-text and numeric version constraints.
- Overflow-safe numeric comparisons.
- Endpoint-only range validation.
- Strict XML interchange.
- Bounded resource consumption.
- Minimal supported Java API.
- Package-private implementation machinery.
- The accepted invocation-local diagnostic-handler boundary.
- Pre-1.0.0 implementation coherence.

Do not reintroduce regex engines, legacy selector support, compatibility layers or runtime migration machinery.

## 2. Use the existing ADD candidate

Review the existing Final.003 ADD candidate against the accepted DRP.

The current proposal identifies:

- Seven existing architectural elements.
- Two association descriptions.
- Existing architectural interfaces and dependencies.

Preserve the established element identities, relationship identities, relationship directions and endpoints.

Do not create replacement architectural elements where an existing element is being refined.

Do not alter unrelated architectural definitions, relationships, navigation or Views.

Use the existing approved Markdown Continuum representation and incorporation mechanisms.

Do not edit ArchiMate Exchange XML as an architectural source.

Do not use Archi as the source of architectural authority.

The Markdown Architecture Continuum remains authoritative.

## 3. Clean superseded architectural content

Review the affected seven architectural definitions and two association descriptions.

Where an element contains a *Legacy Content — Superseded* section or equivalent historical material:

1. Identify whether the content retains current architectural authority.
2. Preserve any still-applicable requirements by incorporating them into the current authoritative definition.
3. Remove obsolete explanations, superseded API descriptions, historical UUID assumptions and regex-selection semantics from the current definition.
4. Do not retain duplicate authoritative and superseded descriptions merely for historical reference.
5. Do not introduce replacement compatibility sections.

Historical committed versions remain available through Git.

Existing review evidence must not be rewritten.

This cleanup is restricted to the elements and relationships affected by Final.003.

Do not perform a general Continuum cleanup.

## 4. Reconcile incorporation metadata

Review metadata introduced by the existing ADD candidate, particularly:

`namingReconciliation`

The candidate currently describes Final.003 incorporation as awaiting review.

Distinguish clearly between:

- The standing of an unaccepted incorporation candidate.
- The standing of an accepted and incorporated Architecture release.

Prepare metadata appropriate to the resulting incorporated architecture.

Do not prematurely assert that an Architecture release has been accepted.

Prefer using existing architectural governance metadata and provenance conventions rather than introducing a new status mechanism.

If the current metadata should only exist during review, keep it in review evidence rather than the authoritative element definition.

The final incorporated Continuum must not continue describing its own accepted semantics as pending incorporation.

## 5. Incorporate the DR-5 locator correction

The existing live Continuum validation failure concerns the missing retained-input locator associated with Architecture 1.1.25.

Use:

`01-retained-topic-input-locator.patch`

as the reviewed candidate for correction.

Verify that the repair:

- Identifies the intended retained input.
- Preserves the original source identity and accepted decision history.
- Does not change architectural semantics.
- Does not introduce duplicate source bindings.
- Restores canonical validation against the appropriate baseline.

Apply the correction to the incorporation candidate in the appropriate order.

Do not silently modify historical evidence or previously accepted releases.

Record the correction explicitly in the applicable Architecture HCRL disposition.

## 6. Incorporate the Software Development Praxis amendment

Review:

`software-development-praxis-pre-1-0-0.proposed.patch`

against the accepted Software Development Praxis and its governing Architecture baseline.

The Solution Architect has accepted the following principle:

> Before Harmonia Software 1.0.0, the implementation evolves as one coherent system conforming to the current accepted architecture.
>
> When a contract changes, every affected component and consumer must be refactored to the new contract.
>
> This includes direct and indirect dependencies, interfaces, adapters, representations, configuration, tests, documentation and integrations.
>
> Superseded pre-1.0.0 APIs and implementations carry no backward-compatibility obligation.
>
> The work is complete only when the affected system has passed applicable regression and integration verification.

Preserve this principle exactly in substance.

In particular:

**Supporting already implemented components means refactoring them to the accepted API, not preserving obsolete APIs for their use.**

Confirm that the proposed amendment follows existing governance and revision conventions.

Include it in the same proposed Architecture incorporation where permitted.

If existing governance requires a separate architectural disposition, prepare that disposition explicitly rather than silently broadening the current one.

Do not introduce a legacy-support process, compatibility framework or additional administrative workflow.

## 7. Reconcile Architecture Review 1.1.28/001

Review the existing:

`hcrl-1.1.28-001.pending.md`

and applicable predecessor decisions.

Determine the correct relationship between:

- The previously reviewed Topic/TopicMask ADD.
- The DR-5 retained-input locator correction.
- The accepted Final.003 design.
- The new replacement ADD.
- The Software Development Praxis amendment.
- The separately governed 1.1.27/002 matter.

Preserve the distinction between previously reviewed evidence and the newly proposed incorporation.

Do not silently treat an earlier RE2/J-oriented ADD candidate as the accepted successor.

Prepare the appropriate HCRL dispositions and exact review references.

If the correct incorporation transition cannot be established from current authority, record the outstanding decision and recommend the smallest appropriate resolution.

Do not invent acceptance history.

## 8. Verify the resulting Continuum

Construct an isolated proposed incorporated state using the exact accepted predecessor and the reviewed corrections.

Perform applicable verification, including:

1. Markdown source integrity.
2. Source and retained-input references.
3. Canonical identity preservation.
4. Element and relationship identity preservation.
5. Relationship endpoints and direction.
6. Source binding and provenance validity.
7. Canonical validation.
8. ArchiMate Open Exchange XML generation and validation.
9. View and navigation preservation.
10. Relevant architectural axioms and design correspondence.
11. Software Development Praxis consistency.
12. Absence of superseded regex and UUID semantics from the affected current definitions.

Verify that unrelated architectural elements and relationships remain unchanged.

Use the existing validation tooling and established architecture gates.

Where a required check is unavailable, report the actual limitation and its acceptance implications.

Do not substitute an invented check or claim a pass.

### Existing qualifications

The previous report identified:

- A missing retained-input locator in the live Continuum.
- Historical provenance timing qualifications.
- An unavailable named ArchUnit source suite.
- Outstanding actual Archi transport and human review.

Confirm the status of each applicable qualification.

Distinguish previously known issues from any new issues introduced by this candidate.

Do not conceal, automatically waive or expand the scope of those qualifications.

## 9. Prepare incorporation and HCRL artefacts

Prepare the resulting proposed incorporation artefacts using the repository's established conventions.

These should include:

- A final proposed ADD patch or equivalent governed change.
- The necessary DR-5 correction.
- The Software Development Praxis amendment or separately scoped proposal.
- An updated Architecture HCRL ready for Solution Architect adjudication.
- A concise incorporation summary.
- A verification and exception report.

Include an exact inventory of changed architectural elements, relationships, governance documents and evidence files.

Clearly distinguish:

- Accepted design decisions.
- Prepared incorporation changes.
- Pending HCRL dispositions.
- Unresolved verification exceptions.
- Future software implementation obligations.

Do not create duplicate accepted DRPs or compatibility copies.

## 10. Software implementation remains gated

Do not modify:

- Production Java.
- JUnit tests.
- XML fixtures.
- Maven POMs.
- Backlog Item 0003.
- Software HCRL 010.

The accepted Final.003 design is the architectural basis for the next Backlog Item 0003 revision.

The future implementation assignment must replace the unaccepted RE2/J design with the accepted wildcard and numeric-version semantics.

The existing independent review findings remain applicable to their original candidate.

Do not conflate acceptance of Final.003 with acceptance of its software implementation.

## 11. Required deliverables

Provide:

1. **Final.003 incorporation candidate** — with exact modified architectural elements and associations.
2. **DR-5 locator correction** — validated against the appropriate predecessor.
3. **Software Development Praxis amendment** — prepared for the applicable incorporation.
4. **Architecture HCRL update** — ready for Solution Architect review and disposition.
5. **Validation report** — including all passes, failures, unresolved checks and qualifications.
6. **Change inventory** — identifying every changed file.
7. **Recommended acceptance sequence** — identifying which actions are required to complete the Architecture incorporation and enable Backlog Item 0003 revision.

Use the existing Architecture evidence directory and established report conventions.

Preserve accepted documents, prior review evidence and existing implementation changes.

Do not commit, push, merge, tag, release or publish.

**Stop after preparing the complete incorporation candidate and HCRL for Solution Architect review.**
```

[final]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.003.md
[hcrl]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
[proposal]: ../../after/final-003-incorporation-proposal.md
