<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Backlog Item 0004 — Software Foundation Reset

**Backlog Item ID:** 0004

**Backlog Item Revision:** 001

## Overview

### High-Level Synopsis of Effort

Reset the exploratory implementation and establish an empty Maven foundation for incremental development.

### High-Level Synopsis of Activities

Verify the pushed checkpoint and apply the approved manifest. Reconcile the Maven lifecycle, record historical deployment and implementation evidence, and prepare the ArchUnit governance amendment.

### High-Level Synopsis of Outcome

All 50 Maven projects are available at `0.1.0-SNAPSHOT`. Accepted architecture and historical review inputs remain retrievable.

### High-Level Synopsis of Acceptance Criteria

Verify exact source scope, the full and targeted reactor on JDK 21, empty application archives and governed review correspondence.

## Context

The approved reset uses `718fc278be5b89b6138d4341feddefce1269eaa6` on `feature/BL-CALLIOPE-OO-01` and accepted Architecture 1.1.26. This item records the adjudicated specification used in the [issued assignment](../../enterprise-architecture/design/evidence/software-0.1.0/009/before/ai-agent-tasks/unresolved-software-foundation-reset.md). The Solution Architect's instruction supplies the execution authority. The software development target is `0.1.0-SNAPSHOT`. Feature implementation, release acceptance and external deployment operations are outside scope.

## Associated Requirements

- [Architectural Axioms](../../enterprise-architecture/governance/architectural-axioms.md), particularly AX-05, AX-08, AX-14 and AX-17, govern responsibility boundaries, evidence and preservation of decisions.
- [Software Development Praxis](../../enterprise-architecture/governance/software-development-praxis.md) governs candidate review and supersession. Retirement of the standing ArchUnit requirement is a prospective Architecture amendment.

## Expected / Associated Activity

1. Check the current branch, pushed Git origin, clean state and [approved deletion manifest](../../enterprise-architecture/design/evidence/software-0.1.0/009/before/approved-delete-paths.txt).
2. Remove the specified implementation and retire its build-time dependencies and plugin executions. Set the Maven project and parent versions uniformly.
3. Preserve architecture, documentation and evidence. Record implementation candidate supersession and historical deployment observations, and prepare the Architecture amendment.
4. Verify exact Git scope, all 50 Maven projects, full and targeted builds and archive contents. Submit the [reset HCRL](../../enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-009.pending.md) for review.

## Expected Outcomes

A coherent empty reactor ready for a separate Naming assignment, with documented pre-reset recovery and traceable review records.

## Acceptance Criteria

| Criterion | Observable result | Verification |
| --- | --- | --- |
| AC-1 | Exactly 1,326 approved tracked deletions; 12,837 tracked paths remain | Compare Git paths to the pinned manifest and check protected content |
| AC-2 | All 50 POMs retain the module graph and use `0.1.0-SNAPSHOT` | Parse project and parent identities and compare hierarchy |
| AC-3 | Full and targeted builds succeed on JDK 21 | Record actual commands, results and empty JAR/WAR inspection |
| AC-4 | No exploratory code, frontend implementation or placeholder source remains | Inspect module source trees and archive payloads |
| AC-5 | Deployment evidence and supersessions are traceable, with Architecture 1.1.26 still accepted | Review linked records and pending amendment |
