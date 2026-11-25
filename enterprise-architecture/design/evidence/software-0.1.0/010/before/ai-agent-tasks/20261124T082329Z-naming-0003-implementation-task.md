<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Junie implementation Task — Backlog Item 0003

**Standing** · Issued to Junie under the Software Development Praxis.

Task-Issued-At: 2026-11-24T08:23:29.194Z

**Issuance event** · The attributable system instruction-entry event is
`UserPromptEvent` timestamp `1795508609194`, prompt
`prompt-261124-192329-16sc`, session `session-261124-192313-192s`. This records
the instruction entering the agent interaction, not an earlier composition
time. The approved handoff had not previously entered a Junie session; this
Task is issued by the current interaction. No external ACP delivery is claimed.

**Issuing authority** · Mark Hunter, Solution Architect.

**Assignee** · Junie.

**Repository** · `fhirfactory/harmonia`.

**Accepted work specification** · Backlog Item 0003, revision `000.003`,
[`docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md`](../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md),
SHA-256 `6c5b7bf1612105d8043e62becadb8923d66d74cbf9151130567dd030dba5947b`.
It is accepted with qualifications by the Solution Architect. The operative
approved Task source is
[`naming-0003-implementation-task.proposed.md`](../../../../../../../.junie/plans/naming-0003-implementation-task.proposed.md),
SHA-256 `af6fb43230877c0ff49a2fd1cb969c9c060759807e88ebbbe435becc9a8d1578`.

## Issued instruction

The following user instruction is preserved verbatim:

> The Junie implementation Task for Backlog Item 0003, revision 000.003, is approved.
>
> Issue the Task to Junie under the existing Software Development Praxis.
>
> Record the actual issuance event, source commit, working-tree state and accepted backlog revision.
>
> Preserve the two accepted implementation qualifications and all existing architectural and verification boundaries.
>
> Do not expand the assignment or initiate additional architecture reviews.
>
> Junie is authorised to perform the bounded Naming implementation and associated tests once the Task is issued.
>
> Commit, push, merge, tag, deployment and publication remain separately governed.

The subsequent implementation instruction is preserved verbatim:

> ### IMPLEMENT FROM CREATE_PLAN
>
> The plan above is the source of truth for this task.
>
> DO:
> - Execute the plan step by step (touch points → approach → tests).
> - Reuse plan facts; open/search code only for a concrete gap or failed assumption.
> - If the plan lists created artifacts (repro/scripts), reuse those paths — do not recreate.
> - On a real blocker (missing symbol, red expected test, conflict with guidelines/issue): fix locally or do a targeted lookup. Stay on the same plan.
>
> DO NOT:
> - Re-map / broadly re-explore the repo "from scratch".
> - Invent a competing private plan that replaces the user's plan.
> - Call create_plan again for this task.
> - Deviate from the plan without a concrete blocker.
>
> Next action: start implementing from the plan (edits/tests as it says).

## Starting authority and source state

- Accepted Architecture baseline: `1.1.26`.
- Software development identity: `0.1.0-SNAPSHOT`; governed software target:
  `software-0.1.0`.
- Previous completed software iteration: `009`,
  [`hcrl-software-0.1.0-009.complete.md`](../../../../../register/software-0.1.0/hcrl-software-0.1.0-009.complete.md).
- Accepted design: [`DRP-Topic-and-TopicMask.Final.001.001.md`](../../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md),
  accepted with qualifications under Software HCRL `004`, DR-10.
- Standalone prerequisite authority: Architecture HCRL `1.1.28/001`, DR-6,
  which permits this bounded implementation before Architecture 1.1.27 and
  1.1.28 completion when its five conditions are observed. Architectural
  incorporation and verification remain prerequisites to dependent acceptance.
- Branch at source inspection: `feature/BL-0003-Calliope-Topic-and-TopicMask`,
  tracking `origin/feature/BL-0003-Calliope-Topic-and-TopicMask`.
- Source commit: `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`.
- Source tree: `1846199b36bbfb07c87746c1367c059698b7120d`.
- The source tree matches the accepted development foundation tree at
  `7d5c195ad7782ab0b39032308a2eb8f72c678da5`.
- Working-tree status was captured at `2026-11-24T08:24:42Z`, before any Task
  implementation edits. The index was clean. The following modified/untracked
  paths were already present and are outside this Task unless specifically
  named by the approved assignment:

```text
 M docs/implementation/harmonia-convergence-runtime-integration-plan.md
?? .junie/plans/naming-0003-implementation-task.proposed.md
?? .junie/plans/naming-0003-junie-handoff.md
?? docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md
?? docs/backlog/evidence/0003/000.003-reviewed.md.gz
?? enterprise-architecture/design/evidence/1.1.28/001/after/01-retained-topic-input-locator.patch
?? enterprise-architecture/design/evidence/1.1.28/001/after/02-topic-topicmask-continuum.patch
?? enterprise-architecture/design/evidence/1.1.28/001/after/ai-agent-reports/20261124T054105Z-topic-topicmask-continuum-reconciliation.md
?? enterprise-architecture/design/evidence/1.1.28/001/after/ai-agent-reports/20261124T055110Z-revised-dr-6-disposition.md
?? enterprise-architecture/design/evidence/1.1.28/001/after/ai-agent-reports/20261124T062555Z-complete-disposition-record.md
?? enterprise-architecture/design/evidence/1.1.28/001/after/ai-agent-reports/20261124T062949Z-final-governance-readiness.md
?? enterprise-architecture/design/evidence/1.1.28/001/after/verification.json
?? enterprise-architecture/design/evidence/1.1.28/001/before/ai-agent-tasks/unresolved-complete-disposition-record.md
?? enterprise-architecture/design/evidence/1.1.28/001/before/ai-agent-tasks/unresolved-final-governance-readiness.md
?? enterprise-architecture/design/evidence/1.1.28/001/before/ai-agent-tasks/unresolved-revised-dr-6-disposition.md
?? enterprise-architecture/design/evidence/1.1.28/001/before/ai-agent-tasks/unresolved-topic-topicmask-continuum-reconciliation.md
?? enterprise-architecture/design/evidence/1.1.28/001/before/review-disposition/hcrl.before-disposition-completion.md
?? enterprise-architecture/design/evidence/1.1.28/001/before/review-disposition/hcrl.before-dr-6-revision.md
?? enterprise-architecture/design/review/1.1.28/hcrl-1.1.28-001.pending.md
```

## Assignment

Implement only the accepted standalone Naming increment in
`calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/`, with
corresponding tests under the matching Calliope test package and XML fixtures
under Calliope test resources. Follow the accepted public API and behaviours in
Final.001.001 §§1–8 and the acceptance criteria AC-1–AC-13 in Backlog revision
`000.003`.

Only these Calliope dependency declarations are authorised:

- Direct `com.google.re2j:re2j:1.8` for matching.
- Test-scoped `org.junit.jupiter:junit-jupiter:5.10.2` for Naming tests.

Keep the inherited Java 21 compiler and Surefire configuration. Do not change
the parent POM, module graph, plugins or logging dependencies.

Apply the approved bounds: depth 16; decoded Topic field lengths 64/256/64
Unicode code points; each decoded mask expression at most 512 code points;
canonical Topic text 32 KiB and mask expression 128 KiB UTF-8; XML record 256
KiB UTF-8; pre-compilation guards of 16 nested groups, repetition bound 256 and
4,096 estimated expansion units; and a native RE2/J `programSize()` maximum of
1,024 instructions. Enforce bounds before unnecessary allocation, decoding or
compilation; compile expressions once at immutable entry construction; keep
matchers invocation-local and matching bounded to 48 full-field checks.

### Accepted implementation qualifications

1. **RE2/J safeguards** — Prefer bounded inputs and native compiled-program
   checks. Additional pre-compilation checks must remain simple, justified and
   independently testable. Do not create a second regex parser, expression
   tree, expanded pattern estimator or Java RegEx fallback.
2. **Diagnostics** — Keep diagnostics synchronous, bounded and local to Naming;
   preserve safe categorized validation failures and fail-closed Boolean
   matching. Do not add global mutable state, asynchronous processing, queues,
   buffering, application logging infrastructure or delivery guarantees.

The separate accepted interim verification qualification also remains in force:
structural JUnit checks, production/test diff review and dependency-boundary
verification are accepted for this standalone increment while the exploratory
ArchUnit suite is absent. Report that suite as unexecuted and preserve its
separate Architecture 1.1.27/002 amendment standing. Do not revive the obsolete
suite.

Preserve the current Architecture 1.1.26 baseline and the separately governed
1.1.27/002 amendment and 1.1.28/001 reconciliation. Do not modify Continuum,
review or release records, the convergence plan, this approved Task source, or
the pre-existing working-tree changes. Keep IU qualifier vocabulary, specialised
reference mappings, downstream integration, compatibility/migration work,
consumer policy, Themis changes and operational logging integration outside this
assignment. Claim architectural conformance only to the extent demonstrated by
the evidence.

## Verification and handoff

Add focused functional and structural JUnit evidence for AC-1–AC-13, including
malformed/adverse inputs, applicable limits, real XML reader/writer round trips,
diagnostic and evaluation failure, builder recovery, immutable snapshots,
concurrent reuse and deterministic bounded-workload measurements. Keep each
production Java file within 500 physical lines and document classes and
significant methods for newcomers.

Run the specified focused Calliope reactor test and full Maven reactor
verification on JDK 21, using bounded waits. Report exact commands, environment,
results, test counts, test scope and limitations. The report must identify
solution-wide regression, integration and conformance coverage actually
exercised, and disclose required missing coverage for later Solution Architect
treatment. Preserve the accepted standalone diagnostics and ArchUnit
qualifications.

Produce the Implementation Report with AC correspondence, structural and
dependency evidence, observed workload timings, exact candidate source paths and
the starting Git tree. An independent Codex implementation review is required
before implementation acceptance; it is not initiated by this Task. Do not
initiate additional Architecture reviews or claim acceptance/closure.

This Task authorises local in-scope edits and tests only. Commit, push, merge,
tag, publication and deployment remain separately governed and are not
authorised.