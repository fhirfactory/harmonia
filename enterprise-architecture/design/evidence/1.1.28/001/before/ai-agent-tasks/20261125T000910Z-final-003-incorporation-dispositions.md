Task-Issued-At: 2026-11-25T00:09:10.933Z

Event source: Codex interaction system rollout 01a20f52-81bf-7600-a71d-90e590b3bbec, timestamp of the user response_item carrying the operative Solution Architect dispositions.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Record Final.003 incorporation dispositions

**Agent** · Codex, Architecture governance and incorporation.

**Review boundary** · [Architecture 1.1.28/001][hcrl]. Current Architecture
1.1.26 at Git `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`, branch
`feature/BL-0003-Calliope-Topic-and-TopicMask`. Software `0.1.0-SNAPSHOT`.
The [reviewed candidate Report][reviewed] establishes the prior isolated
comparison. The [HCRL preimage][preimage] preserves the exact record before
the following Solution Architect dispositions.

## Operative instruction

```
# Harmonia — Architecture 1.1.28/001
## Final.003 Incorporation and Solution Architect Dispositions

**Role:** Architecture governance and incorporation agent  
**Current incorporated Architecture baseline:** 1.1.26  
**Accepted design:** `DRP-Topic-and-TopicMask.Final.003.md`  
**Target Architecture release:** 1.1.28, subject to release prerequisites  
**Software target:** 0.1.0-SNAPSHOT  
**Branch:** `feature/BL-0003-Calliope-Topic-and-TopicMask`

## Objective

Record the Solution Architect's dispositions for DR-8, DR-9 and DR-10 in:

`enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md`

Prepare and, where permitted by existing governance and predecessor state, apply the accepted Final.003 architectural incorporation.

The architectural design and incorporation content have been reviewed.

**Do not reopen the accepted Topic and TopicMask design or initiate another broad architectural investigation.**

This assignment concerns exact incorporation, release sequencing, validation and HCRL completion.

---

## 1. Solution Architect disposition — DR-8

**Decision: ACCEPTED**

Accept the Final.003 replacement Architecture Continuum ADD and its scoped retirement of superseded architectural content.

The accepted candidate is:

`enterprise-architecture/design/evidence/1.1.28/001/after/final-003-incorporation.proposed.patch`

Use the exact reviewed candidate identified by SHA-256:

`e92f13ff2a2945350f376a5854e9b225534ebe40b78e3ce21ccbd6d186034cc7`

### Accepted scope

The change refines seven existing architectural elements:

1. Topic.
2. TopicMask.
3. Calliope-Models.
4. Logical InformationUnit.
5. Conceptual Information Unit.
6. TopicIdentityStable.
7. Mnemosyne Adapter (Store).

It also updates two existing association descriptions.

Preserve:

- Architectural element identities.
- Relationship identities, endpoints and directions.
- Existing interfaces and dependencies.
- Architecture navigation.
- Authored Views.
- Independently governed architectural responsibilities.

Do not create replacement elements where existing elements are being refined.

### Superseded material

Accept the eight scoped retirements documented in:

`final-003-retired-source-record.md`

The retirement removes obsolete or duplicated current architectural content.

Preserve the continuing authoritative semantics identified in the retirement record, including:

- InformationUnit UUID identity.
- Governed InformationUnit indivisibility.
- The distinction between governed indivisibility and transactional atomicity.
- Applicable IU §§6.5.1–6.5.9 guardrails.
- Existing IU metadata and representation responsibilities.
- Independently governed registry and reference responsibilities.
- Existing Store adapter interfaces and capability obligations.

Historical committed versions remain retrievable through Git.

Do not introduce compatibility copies, duplicate authoritative content or obsolete software APIs.

### Relationship to previous dispositions

DR-8 supersedes the implementation implications of earlier DR-1–DR-4 statements wherever they conflict with accepted Final.003.

In particular, superseded RE2/J and runtime Topic UUID requirements do not remain implementation authority.

Preserve the historical text of previously recorded dispositions as evidence of those decisions at the time.

Add a clear statement identifying Final.003 as the governing successor.

### DR-8 acceptance qualification

The architectural content and exact replacement patch are accepted.

The actual incorporated result must still pass applicable Architecture release verification.

The disposition does not independently declare Architecture 1.1.28 complete.

---

## 2. Solution Architect disposition — DR-9

**Decision: ACCEPTED**

Accept the pre-1.0.0 Software Development Praxis amendment.

The reviewed candidate is:

`enterprise-architecture/design/evidence/1.1.28/001/after/software-development-praxis-pre-1-0-0.proposed.patch`

Expected SHA-256:

`7909b8d2c294b1d8e50b82f31a67682d651cb3ccff373077228b3d7bf4b203ca`

### Accepted principle

Before Harmonia Software 1.0.0, development proceeds as one coherent implementation conforming to the current accepted architecture.

When a contract changes:

- Identify all affected components and consumers.
- Refactor direct and indirect consumers.
- Update affected interfaces, adapters and representations.
- Update applicable configuration, integrations and build dependencies.
- Update tests, fixtures and documentation.
- Verify the resulting behaviour through appropriate regression and integration testing.

Superseded pre-1.0.0 APIs and implementations carry no backward-compatibility obligation.

**Supporting already implemented components means refactoring them to the accepted contract, not preserving obsolete contracts.**

Successful compilation alone does not establish implementation coherence.

### Governance

The amendment uses the existing Software Development Praxis mechanisms for:

- Backlog Item scoping.
- Implementation assignments.
- Dependency analysis.
- Implementation Reports.
- Software HCRL review.
- Regression and integration verification.

Do not introduce new administrative gates, compatibility frameworks or migration processes.

### Incorporation scope

Authorise the Praxis amendment as a separately recorded governance decision within the same Architecture 1.1.28 incorporation change set as DR-8.

Preserve its distinct DR-9 authority and verification.

Do not create a separate Architecture release solely for this amendment unless an existing mandatory governance rule prevents incorporation in 1.1.28.

If such a rule exists, report the precise constraint before changing the authorised scope.

---

## 3. Solution Architect disposition — DR-10

**Decision: ACCEPTED WITH QUALIFICATION**

Accept the proposed Architecture transition sequence:

**1.1.26 → 1.1.27 → 1.1.28**

### Architecture 1.1.27

Preserve the separately governed:

`hcrl-1.1.27-002.pending.md`

Complete its applicable review and verification before using 1.1.27 as the accepted predecessor for 1.1.28.

Do not silently incorporate 1.1.27/002 into the Final.003 change set.

### DR-5 locator correction

The existing DR-5 disposition has already accepted the exact two-path retained-input locator correction.

Use:

`01-retained-topic-input-locator.patch`

Expected SHA-256:

`f29779bd8b8b0000442d5413293b157c84de4446239b5031980364f534a49418`

Determine the earliest appropriate Architecture transition in which the correction is required.

Prefer inclusion in 1.1.27 where necessary to satisfy its verification gates.

Otherwise incorporate it with the Final.003 transition, subject to explicit treatment of any earlier dependent verification.

**Apply the correction exactly once.**

Do not duplicate source bindings, change accepted source identities or rewrite historical evidence.

### Architecture 1.1.28

Use the actual accepted 1.1.27 result as the immediate predecessor.

Incorporate:

- The accepted DR-8 Final.003 replacement ADD.
- The accepted DR-9 Software Development Praxis amendment.
- The accepted DR-5 correction, if not already incorporated.

Reconcile the reviewed patch against the exact accepted predecessor.

If the accepted predecessor differs from the isolated review baseline, perform the minimum necessary reconciliation and record the differences.

Do not blindly apply a patch prepared against Architecture 1.1.26.

Any substantive difference from the accepted DR-8 or DR-9 scope requires renewed Solution Architect disposition.

### Release verification

Before declaring Architecture 1.1.28 complete, verify the actual resulting Continuum.

Required verification includes:

1. Exact source and predecessor identity.
2. Canonical Markdown validation.
3. Element and relationship identity preservation.
4. Relationship endpoint and direction consistency.
5. Source binding and retained-input integrity.
6. Applicable provenance validation.
7. ArchiMate Open Exchange XML generation.
8. Exchange schema and reference validation.
9. Deterministic projection.
10. Preservation of authored Views and navigation.
11. Architecture correspondence with Final.003.
12. Incorporation of the DR-9 Praxis amendment.
13. Applicable architecture test and structural-conformance checks.
14. Actual Archi transport verification where required by the release gates.
15. Review of the complete resulting architectural change.

Do not substitute successful isolated-candidate validation for verification of the actual incorporated release.

### Existing verification qualifications

Explicitly retain and disposition the following:

- The unavailable named ArchUnit source suite.
- Actual Archi import/export verification not yet performed.
- Four historical unresolved Task issuance timestamps and filename prefixes.
- Any dependency on the DR-5 locator repair.
- Any outstanding 1.1.27 predecessor or release qualification.

Do not invent missing historical timestamps.

Do not silently waive required checks.

Where a check cannot be completed, identify the owning governance review, the applicable release gate and the exact consequence.

### Release completion

The accepted DR-10 sequence authorises preparation and controlled incorporation.

**It does not authorise bypassing incomplete Architecture release gates.**

If prerequisites remain unresolved, keep the applicable HCRL or release pending and report the precise blocker.

---

## 4. Update HCRL 1.1.28/001

Update the pending HCRL to record:

| Decision | Solution Architect disposition |
|---|---|
| DR-8 | ACCEPTED |
| DR-9 | ACCEPTED |
| DR-10 | ACCEPTED WITH QUALIFICATION |

Preserve DR-1–DR-7 and their historical dispositions.

Record the new dispositions with sufficient detail to establish:

- Accepted source identity.
- Exact reviewed patch identity.
- Architectural scope.
- Superseded implementation semantics.
- Continuing architectural obligations.
- Required incorporation sequence.
- Outstanding verification qualifications.

Do not reinterpret the historical dispositions as authority to implement superseded software behaviour.

Use the existing HCRL structure and evidence conventions.

---

## 5. Prepare and execute the authorised incorporation sequence

First establish the actual state of Architecture 1.1.27.

If its accepted result is available and its required gates have been satisfied:

1. Establish the exact accepted predecessor.
2. Reconcile the accepted patches against that predecessor.
3. Apply the authorised changes through the existing Continuum incorporation process.
4. Run the applicable validation and projection checks.
5. Prepare the resulting Architecture release artefacts.
6. Record the exact result and outstanding release qualifications.

If Architecture 1.1.27 is not yet accepted:

1. Record the DR-8–DR-10 dispositions.
2. Prepare the exact incorporation sequence.
3. Validate the proposed candidate against the best available accepted baseline.
4. Preserve all prepared changes and evidence.
5. Leave dependent incorporation and release completion pending.

Do not manufacture a predecessor release or declare acceptance based solely on an isolated projection.

---

## 6. Software boundary

Do not modify:

- Calliope production Java.
- JUnit tests.
- XML fixtures.
- Maven POMs.
- Backlog Item 0003.
- Software HCRL 010.

Final.003 remains the accepted design authority for the next Naming implementation assignment.

Architecture DR-6 already permits standalone Naming implementation before completion of Architecture 1.1.27 and 1.1.28, provided its five recorded conditions are met.

Do not impose an additional requirement that Architecture 1.1.28 be released before preparing the next Backlog Item 0003 revision.

Dependent software acceptance remains subject to applicable architectural incorporation and conformance gates.

---

## 7. Verification and reporting

Produce a concise report covering:

- The recorded DR-8–DR-10 dispositions.
- The exact predecessor and Architecture release state.
- The status and placement of DR-5.
- The incorporated or prepared DR-8 ADD.
- The incorporated or prepared DR-9 amendment.
- Files changed.
- Validation results.
- Outstanding qualifications.
- Any deviations from the accepted source patches.
- Required actions for Architecture release completion.

Use the established evidence and provenance conventions.

Keep the report focused on the actual incorporation work.

Do not initiate unrelated model restructuring or historical-document reconciliation.

---

## 8. Deliverables

Provide:

1. Updated `hcrl-1.1.28-001.pending.md` with DR-8–DR-10 dispositions.
2. Exact incorporation status and resulting candidate, where authorised.
3. DR-5 application status and release placement.
4. DR-9 governance amendment incorporation status.
5. Architecture validation and projection results.
6. Remaining release-gate exceptions.
7. Concise final incorporation report.
8. Confirmation of the software implementation boundary.

Do not commit, push, merge, tag or publish.

**Stop after recording the dispositions and completing the authorised, currently feasible incorporation work. Do not declare Architecture 1.1.28 complete while required prerequisites remain unresolved.**
```

[hcrl]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
[reviewed]: ../../after/ai-agent-reports/20261124T235126Z-final-003-incorporation-review.md
[preimage]: ../review-disposition/hcrl.before-final-003-dispositions.md.gz
