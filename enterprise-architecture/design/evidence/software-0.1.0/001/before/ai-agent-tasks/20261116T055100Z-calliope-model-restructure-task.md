<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia — Software Implementation Task Details

- **Task Identifier:** `Task-Calliope-Model-Restructure-001`
- **Task-Issued-At:** `2026-11-16T05:51:00Z`
- **Implementation Agent:** `Junie`
- **Target Software Release:** `0.1.0`
- **Accepted Architecture Baseline:** `1.1.26`
- **Backlog Item:** `BacklogItem-00001-Calliope-Model-Restructure.001.md`
- **Backlog Item ID:** `00001`
- **Backlog Item Version:** `001`
- **Backlog Item Path:** `docs/backlog/BacklogItem-00001-Calliope-Model-Restructure.001.md`
- **Target Provenance Location:** `enterprise-architecture/design/evidence/software-0.1.0/001/before/ai-agent-tasks/`

---

## 1. Operative Task Assignment

```text
# Harmonia — Software Implementation Task

## Assignment

**Backlog Item:** BacklogItem-00001-Calliope-Model-Restructure.001.md
**Backlog Item ID:** 00001
**Backlog Item Version:** 001
**Implementation Agent:** Junie
**Target Software Release:** 0.1.0
**Accepted Architecture Baseline:** 1.1.26

## 1. Goal

Implement Backlog Item 00001, Version 001 — Calliope Model Restructure.

This is a whole-of-Calliope software-engineering restructuring activity.

The accepted Backlog Item defines the engineering problem, required activities, expected outcomes and acceptance criteria.

Implement the Backlog Item in full.

## 2. Expected Outcome

A restructured Calliope Java model that:
- Implements the accepted Architecture Continuum.
- Follows the object-oriented design and package organisation principles established in the Harmonia Implementation Guide.
- Contains cohesive, appropriately encapsulated and documented collaborating objects.
- Organises model objects, data elements, value sets, factories and supporting responsibilities consistently.
- Corrects identified structural and implementation deficiencies.
- Updates affected Harmonia consumers.
- Passes applicable component, integration, regression and architecture verification.

The implementation must address the entire Calliope model, not merely the examples identified in the Backlog Item.

## 3. Context

Read and apply:
1. The authoritative Backlog Item: docs/backlog/BacklogItem-00001-Calliope-Model-Restructure.001.md
2. Harmonia Implementation Guide: docs/implementation/Harmonia-Implementation-Guide-Philosophy.md
3. Accepted Architecture Continuum: enterprise-architecture/continuum/
4. Software Development Praxis: enterprise-architecture/governance/software-development-praxis.md
5. Applicable repository instructions, including AGENTS.md.

The Backlog Item contains our established engineering findings and decisions.
Do not treat these findings as hypotheses requiring a separate investigation before implementation.
Examine the existing model to establish the detailed implementation changes needed, including additional deficiencies not explicitly identified in the Backlog Item.

## 4. Authorised Engineering Activity

You are authorised to perform the software-engineering work necessary to satisfy Backlog Item 00001, Version 001.
This includes:
- Restructuring the Calliope package hierarchy.
- Moving, renaming, decomposing, replacing and creating Java classes.
- Extracting and reorganising data elements, value sets, factories and supporting objects.
- Correcting misplaced responsibilities and known implementation defects.
- Updating dependent classes and modules throughout Harmonia.
- Updating relevant tests, imports and implementation documentation.
- Performing the necessary builds, regression tests, integration tests and conformance verification.

Exercise normal software-engineering judgement in determining the detailed object design and implementation sequence.
You may organise the work into manageable internal stages. These do not reduce the scope or completion requirements of the Backlog Item.
Do not retain historical Java structures or compatibility interfaces merely because they exist.
Preserve accepted architectural and information contracts. Where a genuine architecture conflict or material change to an accepted architectural contract is required, identify it for Solution Architect disposition rather than silently changing architectural authority.
The accepted Architecture Continuum must not be modified under this assignment.

## 5. Implementation Standards

Follow the Harmonia Implementation Guide, particularly:
- Object-oriented responsibility allocation and encapsulation.
- Cohesive Java package organisation.
- Appropriate use of constituent data objects, value sets and factories.
- The 500-physical-line source-file requirement.
- Educational source-code documentation.
- Defensive programming and meaningful unhappy-path handling.
- The 95/5 engineering philosophy.

Existing code is implementation material to be improved, not architectural authority.
Use any applicable Solution Architect-authored exemplars to understand the intended engineering quality.

## 6. Verification

Establish and execute a suitable verification programme for the resulting integrated Harmonia candidate.
Include relevant:
- Calliope component and unit tests.
- Tests of affected consumers.
- Integration and regression tests.
- Architecture and implementation-conformance tests.
- Negative, boundary and defensive-behaviour tests.

Report the actual commands, execution environment, results and limitations.
Do not represent incomplete testing as successful verification.

## 7. Implementation Report

Produce a formal Implementation Report demonstrating conformance with Backlog Item 00001, Version 001.
Include:
1. Implementation Summary — what was changed and why.
2. Object-Oriented Design — resulting package hierarchy, class responsibilities and significant design decisions.
3. Architecture Conformance — relationship to the accepted model and any unresolved discrepancies.
4. Consumer Migration — affected modules, contract changes and migration outcomes.
5. Verification Results — commands, tests, outcomes and limitations.
6. Implementation Guide Conformance — OO design, documentation, defensive programming and source-file sizes.
7. Outstanding Matters — unresolved defects, exceptions or matters requiring Solution Architect disposition.
8. Candidate Submission — exact source identity and evidence supporting acceptance criteria.

Retain the Task, Implementation Report and supporting evidence in the locations required by the Software Development Praxis.
Record actual task-issuance and report-production timestamps.
Submit the implementation candidate for Solution Architect review through the applicable software HCRL.

## 8. Execution Authority

This assignment authorises implementation, testing and preparation of the implementation evidence.
It does not independently authorise:
- Changes to the accepted Architecture Continuum.
- Acceptance of the resulting software.
- Merge into the integration branch.
- Software release tagging or deployment.

Commit and push operations require their applicable repository authority.
Proceed with implementation of the complete Backlog Item.
```

---

## 2. Delegated Scope and Governing Constraints

1. **Target Software Release:** `0.1.0` (First accepted software release candidate under Software Development Praxis).
2. **Accepted Architecture Baseline:** `1.1.26` (Architecture Continuum remains unchanged; pure software implementation).
3. **Primary Authority:** `docs/backlog/BacklogItem-00001-Calliope-Model-Restructure.001.md`.
4. **Governing Praxis:** `enterprise-architecture/governance/software-development-praxis.md` and `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`.
5. **No Historical Retention:** Obsolete historical packages (`net.fhirfactory.harmonia.model.topic`) and compatibility wrappers are not preserved once migration is verified.
6. **Strict Source File Limit:** Every Java source file in Calliope must strictly satisfy the 500-physical-line ceiling.
