Task-Issued-At: 2026-11-03T03:24:56.132Z

Event source: /home/mhunter/.codex/sessions/2026/11/03/rollout-2026-11-03T14-18-31-01a19f6a-5f8b-7e22-bb83-3e89871dff24.jsonl; response_item role=user, line 96, operative command-definition instruction-entry event

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Establish HCRL lifecycle commands — Task

Issuer: Mark Hunter, Solution Architect; operative repository user instruction.

Provenance boundary: existing HCRL 1.1.19/001, DR-20 command-orchestration context.
This task defines commands; it does not execute them or adjudicate DR-20.
No new HCRL/DR, iteration, release, accepted publication, commit or push is authorised.
Existing working-tree changes predate this task and remain outside its implementation scope.

Operative instruction retained verbatim:

```
# Harmonia — Establish HCRL Lifecycle Commands

## 1. Goal

Establish two repository-recognised, plain-English governance commands:

- **HCRL Updated**
- **HCRL Finalised**

These commands SHALL orchestrate existing Harmonia governance machinery rather than introduce a new governance framework.

## 2. Expected Outcome

Codex and Junie can reliably interpret either command and invoke the appropriate existing governance processes.

The command definitions must distinguish iteration processing from terminal HCRL finalisation and architecture release.

## 3. Context

The existing repository already supports:

- HCRL preparation, review and adjudication.
- Iteration-specific candidate publication and evidence.
- Architecture validation and reconciliation.
- Release allocation and accepted-baseline publication.
- HCRL completion and repository transitions.

However, the two intended commands are not explicitly defined, and their orchestration is incomplete.

This contributed to DR-20 in HCRL 1.1.19-001, where terminal publication was attempted against an existing immutable release.

### Command A — HCRL Updated

An explicit Solution Architect instruction of **"HCRL Updated"** SHALL initiate governed processing of the current iteration.

The operation SHALL:

1. Apply applicable iteration governance and verification requirements.
2. Preserve the current iteration's candidate architecture, evidence, decisions and qualifications.
3. Record the disposition of changes incorporated during the iteration.
4. Carry forward unresolved decisions, obligations and authorised deferrals.
5. Advance the iteration identifier from `00x` to `00x+1`.
6. Establish the next iteration from the resulting governed architectural state.

Not every outstanding HCRL decision must be resolved before an iteration can advance.

The operation SHALL NOT automatically:

- Finalise the HCRL.
- Advance the architecture release version.
- Publish or replace an accepted reference baseline.

### Command B — HCRL Finalised

An explicit Solution Architect instruction of **"HCRL Finalised"** SHALL initiate terminal HCRL processing.

The operation SHALL:

1. Verify the complete HCRL decision history and incorporated architectural state.
2. Confirm mandatory closure obligations are satisfied or explicitly dispositioned.
3. Preserve final candidate and governance evidence.
4. Complete HCRL closure.
5. Allocate the appropriate architecture release identity under existing release governance.
6. Publish the accepted reference architecture using existing publication machinery.
7. Complete the authorised repository transition.

Existing release immutability and validation controls SHALL remain effective.

Explicitly authorised unresolved matters and deferrals may remain recorded without preventing closure where permitted by governance.

## 4. Constraints

- Reuse existing governance machinery wherever possible.
- Prefer repository-level agent command definitions and orchestration over new tooling.
- Preserve canonical Markdown authority and existing architecture representation rules.
- Preserve existing HCRL identities, decisions, evidence and history.
- Do not weaken validation, publication or release controls.
- Do not introduce unrelated architectural or governance changes.
- Do not silently reinterpret existing accepted releases.
- Both Codex and Junie must be able to discover and interpret the commands consistently.

**Do not execute either lifecycle command against an existing HCRL as part of this task.**

## 5. Authorised Activity / Scope

Codex is authorised to:

1. Inspect existing governance instructions, agent rules and lifecycle entry points.
2. Establish the two commands in the appropriate repository-level instruction authority.
3. Define their required inputs, processing boundaries, outcomes and stop conditions.
4. Connect each command to existing governance operations.
5. Make only the minimal supporting changes necessary for consistent interpretation.
6. Perform bounded, non-mutating verification of command recognition and lifecycle semantics.

If implementing either command requires substantial new automation, a governance redesign or changes to release identity semantics, report the gap rather than expanding scope.

### DR-20 Boundary

DR-20 remains unresolved.

This task establishes command semantics and orchestration only.

It does not authorise:

- Replacing the existing 1.1.19 publication.
- Advancing to 1.1.20.
- Closing HCRL 1.1.19-001.
- Transitioning to iteration 002.
- Publishing a new accepted baseline.

## 6. Report

Provide a concise report identifying:

1. Where the two commands are defined.
2. How Codex and Junie discover them.
3. Which existing operations each command invokes.
4. Whether any orchestration gaps remain.
5. What verification was performed.
6. Which files changed.

Record the working report under `.junie/reports`, consistent with existing agent-report conventions.

Do not create a new HCRL or DR merely to document this bounded command-definition activity.

## 7. Execution Boundary

The authorised outcome is **usable command definitions**, not execution of an HCRL lifecycle transition.

Do not publish, finalise, advance an iteration, allocate a release, commit or push.

Return any unresolved governance or implementation conflict for Solution Architect adjudication.
```
