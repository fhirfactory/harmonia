<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | RE2/J dialect and bounded construction | ACCEPTED | Accept the exact successor Naming candidate; retain predecessor findings as historical evidence. |
| [DR-2](#dr-2) | Strict interchange and safe validation context | ACCEPTED | Accept the candidate and reported verification; retain original Codex findings unchanged. |
| [DR-3](#dr-3) | Supported Naming API and package containment | ACCEPTED | Accept the implementation reconciliation; public visibility does not expand the supported API. |
| [DR-4](#dr-4) | Integrated verification and implementation acceptance prerequisites | ACCEPTED WITH QUALIFICATION | Standalone Naming accepted; Architecture incorporation, ArchUnit and integration/runtime coverage remain deferred to governed scopes. |
| [DR-5](#dr-5) | Bounded Minutiae | ACCEPTED WITH QUALIFICATION | `Minutiae-Accepted: Yes`; accept documented provenance limitations without inventing timestamps or production events. |
| [DR-6](#dr-6) | Design and Backlog reconciliation | ACCEPTED | Accept Final.003 implementation reconciliation and Backlog 0003 revision 000.005; no further Codex review required. |

# HCRL software-0.1.0 / 010 — Naming implementation acceptance

**Status** · COMPLETE — ACCEPTED STANDALONE NAMING DEVELOPMENT CANDIDATE.
This registration records Solution Architect acceptance of the exact candidate
and its explicit qualifications. It does not allocate or establish an accepted
software release or terminal tagged baseline.

**Software identity** · Maven `0.1.0-SNAPSHOT` remains the development identity.
No software release version or tag is allocated or authorized.

**Backlog lineage** · [Revision 000.003][backlog-003] retains its historical
acceptance; [revision 000.004][backlog-004] remains intact for its original
assignment; [revision 000.005][backlog-005] is accepted and supersedes 000.004
for the reconciled Naming scope and subsequent assignments.

**Origin and candidate** · Accepted Architecture `1.1.26` remains the
incorporated baseline. The candidate is on branch
`feature/BL-0003-Calliope-Topic-and-TopicMask`, based on commit
`d79b62add0fb3f85cccafc29972330dd033b7145`, tree
`8b8dd180720ea36d36248d16bda7be2a7c73b4e7`. The six-file
[Calliope source/test patch][candidate-patch] has SHA-256
`c9efe68fe6d5a9ed1c8e6c9dccc1411570a550de6c583b85461029875caa0df8`; exact-base
replay and byte comparison passed. The older `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`
/ tree `1846199b36bbfb07c87746c1367c059698b7120d` is the predecessor examined by
the original Codex review, not this accepted successor. That review and its
findings remain unchanged.

**Evidence** · The originating [implementation Task][implementation-task],
[completion Report][completion-report], [closure Task][closure-task], and
[closure Report][closure-report] remain at their originating locations. This
registration and the [final closure Task][final-task] preserve the current
Solution Architect dispositions and Git boundary. The [final closure Report][final-report]
records actual checks and the repository state.

<a id="dr-1"></a>
## DR-1 — RE2/J dialect and bounded construction

The original review conclusion and proposed disposition remain in the
[pending review record][review-dr1]; the original independent findings are
preserved in the [Codex Report][codex-review]. **Solution Architect disposition
· ACCEPTED.** Accept the exact successor candidate identified above, subject to
the reported verification and DR-4 qualifications. The original findings
describe the predecessor reviewed source; they are not rewritten or treated as
findings against this accepted successor.

<a id="dr-2"></a>
## DR-2 — Strict interchange and safe validation context

The original review conclusion and proposed disposition remain in the
[pending review record][review-dr2], with the Codex findings preserved
unchanged. **Solution Architect disposition · ACCEPTED.** Accept the current
Naming candidate and reported verification subject to DR-4. The accepted
successor result is recorded separately from the predecessor review.

<a id="dr-3"></a>
## DR-3 — Supported Naming API and package containment

The original review conclusion and proposed disposition remain in the
[pending review record][review-dr3]. **Solution Architect disposition ·
ACCEPTED.** The [Final.003 implementation reconciliation][reconciliation]
governs the supported surface: root model and builder types plus the supported
codec facade; public visibility for cross-package implementation helpers does
not expand the supported API, and no JPMS export contract is asserted. Accept
the exact candidate subject to DR-4.

<a id="dr-4"></a>
## DR-4 — Integrated verification and implementation acceptance prerequisites

The original review conclusion and proposed disposition remain in the
[pending review record][review-dr4]. The candidate's fresh
`mvn -B -ntp -pl calliope -am test` and `mvn -B -ntp verify` results are PASS:
Calliope ran 91 tests with no failures, errors or skips; the 50-module reactor
verify succeeded. The other modules mostly had no tests or sources to compile;
the Maven phase does not establish solution-wide integration/runtime coverage.
The Architecture selector remains UNEXECUTED, not PASS. See the [completion
Report][completion-report] for the actual commands, scopes and environments.

**Solution Architect disposition · ACCEPTED WITH QUALIFICATION.** Standalone
Naming is accepted subject to those results and the following continuing
qualifications, which do not block this standalone acceptance:

- Architecture Continuum incorporation and dependent verification remain in
  Architecture 1.1.28/001 DR-6/DR-10. The missing retained-input locator remains
  an Architecture-governed matter, not resolved by this software acceptance.
- The ArchUnit suite remains unexecuted and separately governed by Architecture
  1.1.27/002; its selector outcome is **UNEXECUTED**, not PASS.
- Solution-wide integration/runtime coverage was not executed. It remains due
  in its existing integration/convergence scopes.
- Shared operational logging delivery remains deferred under the accepted
  standalone diagnostics qualification.
- No software release identity, tag or Git transition is authorized here.
  `0.1.0-SNAPSHOT` remains a development identity.

<a id="dr-5"></a>
## DR-5 — Bounded Minutiae

The original review-time request and its evidence are preserved in the
[pending review record][review-dr5]. **Solution Architect disposition ·
ACCEPTED WITH QUALIFICATION.**

```text
Minutiae-Accepted: Yes
Comment: The Solution Architect accepts the documented provenance limitations. Preserve original evidence and correct formatting only where independently verifiable. Do not invent missing timestamps or production events.
```

The initial evidence check reported **FAIL** on eleven historical Task/Report
metadata fields because their labels used decorated syntax. Those fields were
normalized to conventional `Label: value` form, preserving timestamps,
`UNRESOLVED` values, filenames and substantive content; related embedded Task
labels in Reports received the same formatting-only correction. The
post-correction evidence check is **UNRESOLVED**, with zero format errors and
six original timestamp/filename-prefix events still unresolved: five Reports
(the closure, Codex-corrections, function-package, implementation and
package-structure Reports) and the Codex-corrections Task. These limitations
are explicitly accepted and no event was inferred or replaced.

The completion Task's recorded `2026-11-25T09:13:03.379Z` differs by 2 ms from
the attributable system task record's `2026-11-25T09:13:03.377Z`; the original
evidence is preserved without changing the recorded value. The completion
Report's production event is retained as recorded but is not independently
corroborated. The closure Task's `2026-11-25T10:12:13.284Z` is independently
corroborated and its field formatting alone was normalized. The current final
Task uses its attributable `2026-11-25T10:33:21.684Z` event. No missing
timestamp or production event is created by this registration.

<a id="dr-6"></a>
## DR-6 — Design and Backlog reconciliation

**Solution Architect disposition · ACCEPTED.** Accept the implementation
reconciliation addendum to [Final.003][reconciliation] without changing its
accepted §§1–10 or incorporating it into the Architecture Continuum. Accept
[Backlog Item 0003 revision 000.005][backlog-005] as superseding 000.004 for
the reconciled scope and subsequent assignments; preserve 000.004 and its
original assignment history. Codex review is complete; no further independent
review is required. Architecture incorporation, ArchUnit and integration
qualifications remain as recorded in DR-4.

## Final governance closure

HCRL 010 is complete for the accepted standalone Naming development candidate.
Architecture remains `1.1.26`; no Architecture increment or Continuum
publication was made. Software remains `0.1.0-SNAPSHOT`; no release or tag was
allocated or authorized. This record does not assert an accepted software
release or terminal baseline.

The current branch and base HEAD are recorded above. The accepted code
candidate's exact patch is retained and verified; the closure Task/Report and
this HCRL preserve the final candidate and repository state. No commit, push,
merge or branch change was performed by Junie. The Solution Architect retains
the Git operations; the repository remains ready for that review and transition.

[backlog-003]: ../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md
[backlog-004]: ../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.004.md
[backlog-005]: ../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.005.md
[candidate-patch]: ../../evidence/software-0.1.0/010/after/naming-0003-completion-candidate.patch
[implementation-task]: ../../evidence/software-0.1.0/010/before/ai-agent-tasks/20261125T091303Z-naming-0003-completion-task.md
[completion-report]: ../../evidence/software-0.1.0/010/after/ai-agent-reports/20261125T094849Z-naming-0003-completion-report.md
[closure-task]: ../../evidence/software-0.1.0/010/before/ai-agent-tasks/20261125T101213Z-naming-0003-closure-task.md
[closure-report]: ../../evidence/software-0.1.0/010/after/ai-agent-reports/UNRESOLVED-naming-0003-closure-report.md
[final-task]: ../../evidence/software-0.1.0/010/before/ai-agent-tasks/20261125T103321Z-naming-0003-final-closure-task.md
[final-report]: ../../evidence/software-0.1.0/010/after/ai-agent-reports/20261125T105242Z-naming-0003-final-closure-report.md
[review-dr1]: ../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md#dr-1
[review-dr2]: ../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md#dr-2
[review-dr3]: ../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md#dr-3
[review-dr4]: ../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md#dr-4
[review-dr5]: ../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md#dr-5
[codex-review]: ../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md
[reconciliation]: ../../offline-design/DRP-Topic-and-TopicMask.Final.003.md#implementation-reconciliation-addendum