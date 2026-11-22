Report-Produced-At: 2026-11-20T22:47:51.581478Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — Pragma Publication and Subscription Architecture · Iteration 005

The [issued Task][task] supplied PS-01–PS-16 for an offline design draft and review. The originating source was `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with Architecture 1.1.26 and existing local worktree changes. The [cumulative HCRL.004][hcrl] retains the existing software candidate, its backlog and the inherited obligations. The task scope is the design and reconciliation review.

## Result

- [Draft.000.001][draft] records PS-01–PS-16, the authorised Ergon processing environment, specialist context boundaries and the observable durability, retry, lineage and Assurance requirements. It awaits Solution Architect adjudication.
- [DR-12][review] adds the substantive review to the current cumulative HCRL.004. DR-11 adds supporting provenance, retaining the original Minutiae control. The original entries, dispositions and carry-forward obligations are preserved.
- The [Iteration 005 reconciliation][reconciliation] records bounded source correspondence and future conformance cases. The principal differences are the output-IU publication gate before Pragma creation and Mneme-side creation of a realisation before reference handoff. Existing work/undertaking identity and outcome policies, ParcelMask criteria, Pragmaton/WorkSpace/cache relationships, specialised naming and Dokimasia.Praxis derivation retain open decisions. The draft assigns Router/DecisionEngine and Ergon-InformationAdapter logical responsibilities for review, with their exact architectural correspondence unresolved.

## Verification and limits

- **PASS** · Inspected decision and requirement coverage for all 16 PS items. Checked 135 local link targets and fragments before Report emission and confirmed consecutive DR-1 through DR-12, a single Minutiae control, and an add-only HCRL diff. Draft SHA-256 `9d0cb43385363bd9332c806d45f325a9bec188961a53660c4ea0a0e006b69142`.
- **PASS** · The offline ArchUnit reactor with Java 21 completed in Maven-reported **27.526 s**, exit 0. All **90 tests** passed with zero failures, errors or skips. The run exercises architecturally relevant invariants in the existing software. The new draft's implementation, regression and integration conformance remain future evidence.
- **FAIL** · `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 005` exited 1 with `No corresponding HCRL for software-0.1.0-005`. This check requires a matching HCRL.005. The instruction directs entry in HCRL.004, with evidence in the originating 005 boundary. The whole-iteration check remains outstanding until the corresponding HCRL exists.
- **UNRESOLVED** · The system-recorded instruction-entry time was unavailable. The preserved Task wording, unresolved field and filename prefix require Solution Architect treatment before closure. [Task][task]
- **PASS** · Entry-state checks verified that all 39 pre-existing changed or untracked paths, Git index, HEAD and refs were preserved. Changes are confined to the Draft, pending HCRL.004 and the associated Iteration 005 Task, reconciliation and Report. Application code, the Continuum, Final DRPs and release allocation are outside this design activity. The HCRL remains pending.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-pragma-publication-and-subscription-task.md
[draft]: ../../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.001.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[reconciliation]: ../Pragma-Publication-and-Subscription-Reconciliation-005.md
