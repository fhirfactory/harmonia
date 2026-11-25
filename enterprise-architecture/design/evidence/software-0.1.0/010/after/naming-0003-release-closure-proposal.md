<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Backlog Item 0003 — Proposed software release-closure record

**Standing** · Prepared for Solution Architect review only. The standalone
Naming implementation and the Final.003 / Backlog reconciliations have
Solution Architect acceptance recorded in Software HCRL 010. This proposal is
not a completed HCRL, software release, Git transition or acceptance of any
unresolved qualification.

**Target** · `software-0.1.0` · branch
`feature/BL-0003-Calliope-Topic-and-TopicMask` · Maven
`0.1.0-SNAPSHOT`.

**Review record** · [Software HCRL 010](../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md)
remains in `review/`, not `register/`. [Closure Task](../before/ai-agent-tasks/20261125T101213Z-naming-0003-closure-task.md)
and [Implementation Report](ai-agent-reports/UNRESOLVED-naming-0003-closure-report.md)
preserve the current activity.

## Candidate identity

- **Base** · commit `d79b62add0fb3f85cccafc29972330dd033b7145`, tree
  `8b8dd180720ea36d36248d16bda7be2a7c73b4e7`.
- **Implementation patch** · `naming-0003-completion-candidate.patch`, SHA-256
  `c9efe68fe6d5a9ed1c8e6c9dccc1411570a550de6c583b85461029875caa0df8`; exact
  replay and six-file byte comparison against the base passed.
- **Accepted documentation and review source** · Final.003 SHA-256
  `d53dcfdc2cc13671dc4d349087d51d4f268f1a0770fbbacc28bb958de766c276`;
  Backlog Item 0003 revision 000.005 SHA-256
  `215bb159762d9b242b2b5daf7a200b9b569c4d8bf5bcd456c486f1453246f2b4`;
  HCRL 010 pending record SHA-256
  `12b7eeaf2aaa1c973f3953367a9e3c7555d3b509dd517b2a11892b7aa2a68ec8`;
  closure Task SHA-256
  `d0b56c4c0312654f4d901c2a92d9fbdb9616105ad3ada464dc85a6994ce4932b`;
  closure Implementation Report SHA-256
  `f5721ff4586508f10ac06a420b4b093adc98242a4a5c9efc99788e37a40279c2`.
- **Lineage distinction** · `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb` /
  tree `1846199b36bbfb07c87746c1367c059698b7120d` is the predecessor source
  reviewed in the original Codex Report, not this accepted successor. That
  report and its findings remain preserved unchanged.
- **Complete uncommitted candidate paths** · The exact proposed commit set is
  enumerated below; no aggregate patch or commit object has been created.

## Proposed commit candidate inventory

- `calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/NamingSerialization.java`
- `calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/canonical/CanonicalPathSyntax.java`
- `calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/canonical/MaskExpressionParser.java`
- `calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/canonical/TopicCanonicalParser.java`
- `calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/TopicCanonicalParserTest.java`
- `calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/canonical/CanonicalParserTest.java`
- `docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.005.md`
- `enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md`
- `enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-010.pending.md`
- `enterprise-architecture/design/evidence/software-0.1.0/010/before/ai-agent-tasks/20261125T091303Z-naming-0003-completion-task.md`
- `enterprise-architecture/design/evidence/software-0.1.0/010/after/ai-agent-reports/20261125T094849Z-naming-0003-completion-report.md`
- `enterprise-architecture/design/evidence/software-0.1.0/010/after/naming-0003-completion-candidate.patch`
- `enterprise-architecture/design/evidence/software-0.1.0/010/before/ai-agent-tasks/20261125T101213Z-naming-0003-closure-task.md`
- `enterprise-architecture/design/evidence/software-0.1.0/010/after/naming-0003-release-closure-proposal.md`
- `enterprise-architecture/design/evidence/software-0.1.0/010/after/ai-agent-reports/UNRESOLVED-naming-0003-closure-report.md`

This inventory is a proposal, not commit authority. The Implementation Report
records actual validation and the test limitations.

## Verified results and qualifications

| Check | Result and boundary |
| --- | --- |
| `mvn -B -ntp -pl calliope -am test` | **PASS, fresh closure run** · OpenJDK `21.0.13.1`, Maven `3.8.7`, Linux/UTF-8; 91 tests, 0 failures, errors or skips. Finished `2026-11-25T21:26:28+11:00`. |
| `mvn -B -ntp verify` | **PASS, fresh closure run** · All 50 reactor modules succeeded; Calliope ran 91 tests, other modules mostly had no tests or sources to compile, and the Iris BEFE WAR packaged. Empty-JAR warnings were non-fatal. Exact finish time is unavailable in the retained command summary. |
| Calliope Javadoc goals / Javadoc JAR | **PASS** · Previously completed for this unchanged source candidate; JAR includes `index.html`. See the completion Report. |
| Architecture selector | **UNEXECUTED** · The selector found no tests in `paradeigma-test`; this is not a pass. ArchUnit remains deferred to Architecture 1.1.27/002. |
| Candidate patch replay / six-file `cmp` | **PASS** · Completed against the exact base; the code has not changed since that verification. |
| `git diff --check` | **PASS** · Tracked changes checked; the final documentation/evidence file checks are recorded in the closure Report. |
| Relative Markdown links | **PASS** · 109 local targets exist across the six current design, Backlog, HCRL, Task, proposal and Report Markdown files. |

The user accepted the Naming candidate subject to these reported results.
Architecture Continuum incorporation remains deferred to Architecture
1.1.28/001 DR-6/DR-10; integration/runtime coverage remains due in its
existing governed scope. Neither is represented as completed or as a test
failure. The retained-input locator and shared operational logging matters
remain governed qualifications. The original source candidate review and its
historical findings are not rewritten.

## Release and repository transition

- Architecture release remains `1.1.26` (`architecture-release.json`); no
  Architecture increment or Continuum publication is proposed for this
  standalone software acceptance.
- Software release identity: **UNALLOCATED**. Maven `0.1.0-SNAPSHOT` is a
  development identity, not an accepted baseline. Solution Architect must
  establish the first `software/X.Y.Z` baseline or explicitly accept the first
  release with no asserted predecessor, and confirm the immutable tag mapping.
- Proposed commit subject for later approval: `BL-0003: accept Calliope Naming implementation`.
  No commit, merge, tag, push, publication or deployment has been performed or
  is authorized by this proposal.
- No `hcrl-software-0.1.0-010.complete.md` register record is created. HCRL 010
  remains pending because DR-5 still lacks the required `Minutiae-Accepted`
  value/comment, including Solution Architect treatment of the preserved
  unresolved original Junie Report production event and filename prefix.
- The closure Report's exact `Report-Produced-At`, subsequent
  `Report-Amended-At` and UTC filename prefix are **UNRESOLVED**: the
  file-emission interface did not expose those event timestamps. No timestamp
  is inferred from a later clock read, filesystem metadata, or the user's
  task-entry time.

## Solution Architect decisions requested

1. Supply the DR-5 Minutiae acceptance value/comment and disposition the
   preserved unresolved original Report event and filename prefix.
2. Treat the unavailable exact closure-Report emission/amendment timestamps and
   `UNRESOLVED` filename prefix under the provenance contract.
3. Establish the first accepted software release identity/predecessor and
   immutable `software/vX.Y.Z` tag mapping, if release closure is desired.
4. Authorize the exact proposed commit and any separate merge, tag or push
   action. The current task expressly prohibits committing or pushing before
   that authorization.

The implementation is accepted for its standalone Naming scope; final HCRL and
software release closure remains pending these governance decisions. This
proposal supplies none of them.