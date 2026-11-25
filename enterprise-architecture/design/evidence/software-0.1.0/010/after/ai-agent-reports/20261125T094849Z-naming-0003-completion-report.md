<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Calliope Naming Completion Report — Backlog Item 0003

Report-Produced-At: 2026-11-25T09:48:49Z (UTC system time at completion
  of Report emission; no earlier composition time is asserted).
- **Report-Amended-At** · 2026-11-25T09:50:03Z (relative-link correction);
  2026-11-25T09:50:44Z (implementation-guide sequencing note);
  2026-11-25T09:51:33Z (header whitespace correction);
  2026-11-25T09:53:37Z (post-emission test outcomes and HCRL mapping note);
  2026-11-25T09:57:53Z (Javadoc archive and full reactor verification results).
  The original Report-Produced-At is unchanged.
Task-Issued-At: 2026-11-25T09:13:03.379Z
- **Agent / role** · Junie · bounded implementation, documentation and verification.
- **Software target** · `0.1.0-SNAPSHOT` · review target `software-0.1.0-010`.
- **Branch** · `feature/BL-0003-Calliope-Topic-and-TopicMask`.

**Disposition** · A corrected implementation candidate and proposed
documentation reconciliation are presented for Solution Architect review. This
Report does not accept the implementation, advance or complete Software HCRL
010, establish a software release, or authorize a repository transition.

## 1. Authority, scope and architecture review

The operative Task references accepted Backlog Item 0003 revision `000.004`
and `DRP-Topic-and-TopicMask.Final.003.md`. Backlog revision `000.005` is a
separate proposed reconciliation and remains **Proposed — awaiting Solution
Architect acceptance**; revision `000.004` remains the accepted specification.
[Task Detail][task] · [Accepted Backlog Item][accepted] · [Proposed revision][proposed]
· [Final.003][design]

The source correction retains Topic rejection and distinguishes structural
canonical syntax from invalid empty decoded fields. It is consistent with
Final.003 §§3 and 8 and the canonical AX-14 requirement to preserve semantic
distinctions. No module, dependency, public API, persistence, security,
deployment or release boundary changed. AX-04 and AX-12 remain satisfied; AX-17
requires the unrecorded HCRL decisions and implementation-structure claims to
remain explicitly unresolved. The Final.003 addendum and Backlog revision
000.005 are proposals only and do not alter accepted Final.003 §§1–10.
[AX-04][ax04] · [AX-12][ax12] · [AX-14][ax14] · [AX-17][ax17] ·
[Architecture Review Praxis][review-praxis]

## 2. Changes and candidate correspondence

- `CanonicalPathSyntax.splitFields` now preserves empty raw slices after
  structural delimiter validation. `TopicCanonicalParser` supplies the existing
  field-specific checks, retaining `MALFORMED_TEXT` with `QUALIFIER`, `VALUE` or
  `VERSION` and the known entry index. Duplicate/missing equals and misplaced
  version separators remain structural failures. `MaskExpressionParser` still
  accepts empty pattern fields.
- `CanonicalParserTest` and `TopicCanonicalParserTest` cover `=LAB`, `Role=`,
  `Role:1=`, a later-entry empty value, `:1=LAB`, `Role:=LAB`, category,
  operation, field and index metadata, plus mask `=` and `Role=` preservation.
- `NamingSerialization.java` no longer contains the stray `what` in its Javadoc.
- Final.003 received a clearly **proposed** addendum recording the XML 1.0
  boundary, optional declaration, current facade/interchange arrangement,
  serialization proxies/builders, depth enforcement, field-specific diagnostics
  and preservation of independent Findings 2 and 3. Existing accepted §§1–10
  were not rewritten.
- Backlog Item 0003 revision `000.005` is a proposed copy/reconciliation of
  revision `000.004`. It updates AC-6, AC-8 and AC-11 for the XML, diagnostics
  and package-boundary evidence. It does not change `000.004`'s standing.

The repository's XML parser and existing XML codec tests confirm input is
restricted to XML 1.0, an XML declaration is optional, XML 1.1 is rejected, and
the writer emits XML 1.0 UTF-8. The task context identifies implementation
decisions at 07:02Z and 07:26Z, but Software HCRL 010 DR-3 is `Not recorded`;
this Report does not represent those references as attributable HCRL
dispositions. The proposed documentation explicitly preserves that uncertainty
for Solution Architect treatment.

The retained patch
[`naming-0003-completion-candidate.patch`][candidate-patch] contains the six
Calliope source/test paths only; the two documentation candidates are identified
separately above. Patch SHA-256:
`c9efe68fe6d5a9ed1c8e6c9dccc1411570a550de6c583b85461029875caa0df8`.
Replaying it with `git apply --unidiff-zero` against base commit
`d79b62add0fb3f85cccafc29972330dd033b7145` and comparing all six resulting
files with `cmp` returned **PASS**. The resulting Calliope files match the
working candidate byte-for-byte.

Documentation SHA-256 values at Report emission:

- Final.003: `d2862743969f27fefd691c42c92b41f72123086d7bab239e98b658837048779d`
- Proposed Backlog revision 000.005:
  `b9540ac797ccb14f8e37fba411f6935122a1ce491da75ed9ebfa9211867c07df`

## 3. Verification

Environment: OpenJDK `21.0.13.1`, Maven `3.8.7`, Linux, UTF-8.

**Implementation Guide sequencing note** · The Harmonia Implementation Guide
was consulted after the source edit rather than before it. This is a procedural
sequencing deviation, not a waiver. The production change reused the existing
Topic field validations and added no class or significant method; the changed
scanner method's Javadoc was clarified, and the Javadoc JAR goal completed.

| Command / check | Result |
| --- | --- |
| Focused pre-fix selector: `mvn -B -ntp -pl calliope -am -Dtest=CanonicalParserTest,TopicCanonicalParserTest -Dsurefire.failIfNoSpecifiedTests=false test` | **FAIL — expected reproduction.** Finished `2026-11-25T09:52:52Z`; 10 tests ran; the two new regression tests failed on `=LAB`, expecting `QUALIFIER` but receiving `CANONICAL_TEXT`. |
| Same focused selector after fix | **PASS.** Finished `2026-11-25T09:53:15Z`; 10 tests, 0 failures, 0 errors, 0 skipped. |
| `mvn -B -ntp -pl calliope -am test` | **PASS.** Finished `2026-11-25T09:56:40Z`; 91 tests, 0 failures, 0 errors, 0 skipped. |
| `mvn -B -ntp verify` | **PASS.** Finished `2026-11-25T09:57:23Z`; all 50 reactor modules succeeded and Calliope ran 91 tests. Other modules reported no tests to run; some empty-module JAR warnings were informational. |
| `mvn -B -ntp -pl calliope -am javadoc:javadoc` | **PASS.** Finished `2026-11-25T09:56:50Z`; goal exited successfully. |
| `mvn -B -ntp -pl calliope -am javadoc:jar` | **PASS.** Finished `2026-11-25T09:57:00Z`; built `calliope/target/calliope-0.1.0-SNAPSHOT-javadoc.jar`, whose contents include `index.html`. |
| Architecture selector: `mvn -B -ntp -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false test` | **UNEXECUTED.** Finished `2026-11-25T09:57:11Z`; the module has no test sources and Maven reported `No tests to run`. This is not a pass. |
| `git diff --check` and no-index checks of the new Task, Backlog proposal and patch | **PASS.** No whitespace errors reported. |
| Candidate patch replay from exact base and six-file `cmp` | **PASS.** |

The full Maven `verify` establishes the requested reactor build and the tests
that it actually ran. It does not establish solution-wide integration coverage:
the other reactor modules reported no tests to run, the architecture selector
ran no tests, and no separate runtime integration environment was exercised.

## 4. HCRL, acceptance and release status

Software HCRL 010 remains unchanged and pending. DR-1 through DR-5 are each
marked `Not recorded`; the current HCRL source locator is commit
`f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb` / tree
`1846199b36bbfb07c87746c1367c059698b7120d`, whereas this task's source base is
HEAD `d79b62add0fb3f85cccafc29972330dd033b7145` / tree
`8b8dd180720ea36d36248d16bda7be2a7c73b4e7`. HCRL 010 also references Backlog
Item 0003 revision `000.003`, while this Task references accepted revision
`000.004`. No HCRL 011 exists in the review directory. The missing dispositions
and changed exact source/backlog mapping prevent iteration advancement under
the governed lifecycle; the task request does not itself supply those
dispositions. No successor HCRL was created, and no decision was inferred from
the passing tests.

The Task/Report evidence is retained in the originating `software-0.1.0/010`
iteration. The Task-entry worktree/index status was not captured before edits
and remains **UNRESOLVED**; the exact starting commit/tree and the current
candidate patch are recorded above. The Report does not claim that Software
HCRL 010 has been updated to reference these results.

No Architecture or software release was allocated. No commit, push, merge, tag,
publication, deployment or acceptance was performed.

## 5. Solution Architect disposition requested

1. Review the exact candidate and decide the pending dispositions for Software
   HCRL 010 DR-1 through DR-5, especially DR-3's supported API/package boundary
   and DR-4's acceptance prerequisites. Confirm how the task-context decisions
   at 07:02Z and 07:26Z are to be attributed and represented.
2. Decide whether the proposed Final.003 addendum and Backlog Item 0003 revision
   000.005 accurately reconcile the XML 1.0 and implementation decisions;
   revision 000.004 remains the accepted item until then.
3. Determine required treatment for unexecuted architecture and solution-wide
   integration/regression coverage before any implementation acceptance.
4. Provide explicit authority for any HCRL iteration advancement or later
   release-closure activity. This Report supplies none.

The result is a corrected, tested implementation candidate with proposed
documentation changes, awaiting Solution Architect review. It is **not
accepted** and is **not release-ready evidence of completed closure**.

[task]: ../../before/ai-agent-tasks/20261125T091303Z-naming-0003-completion-task.md
[accepted]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.004.md
[proposed]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.005.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.003.md
[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
[candidate-patch]: ../naming-0003-completion-candidate.patch
[review-praxis]: ../../../../../../governance/architecture-review-praxis.md
[ax04]: ../../../../../../continuum/motivation/principle/54508cda80484a6caf95b901780eaf7d.md
[ax12]: ../../../../../../continuum/motivation/principle/ce85889bd9b34f79a53d4c907ceb19d4.md
[ax14]: ../../../../../../continuum/motivation/principle/d3fc6025dc7646d1b2020b51f7a44bfa.md
[ax17]: ../../../../../../continuum/motivation/principle/architectural-axiom/8882573df7ce43c6bc0f66da0a676139.md