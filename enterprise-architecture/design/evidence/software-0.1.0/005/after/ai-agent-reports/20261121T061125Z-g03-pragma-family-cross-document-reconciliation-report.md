Report-Produced-At: 2026-11-21T06:11:25.654157Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — G03 Pragma family cross-document reconciliation · Iteration 005

The [review Task][task] requested exception-based review of the five exact Pragma-family candidates and their originating evidence. The starting worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d)), with Architecture 1.1.26 and cumulative pending HCRL.004. The [reconciliation][evidence] identifies each reviewed revision and its SHA-256.

## Result and pending dispositions

- The five candidates describe a coherent core. They preserve work/undertaking identity, exact input cardinality and separately attributed outputs, metadata-only subscription selection, reference handoff of a durably established realisation, and governed WorkSpace access to Pragmaton. Execution, save, persistence, acceptance and delivery outcomes remain separate.
- The register records 12 scoped exceptions. The cross-candidate findings are the Pragmaton Twin execution-identity clause and inconsistent scope wording for the PublishingMask. The register identifies qualified historical publication, work identity and handoff differences, accepted cache constraints, remaining semantic contracts, and stale companion references and Pragmaton phrasing. Agreed WorkSpace access and settled terminology are carried forward as design agreement.
- The [readiness assessment][readiness] recommends collective Solution Architect adjudication with the register. It distinguishes a proposed correction or disposition before acceptance from the owning contract work needed before dependent incorporation or implementation. Software-level design choices remain outside the architecture exception set.
- Extended [DR-12][review] with the bounded review result and evidence. DR-1–DR-11 and previous DR-12 dispositions and obligations were preserved. All five drafts and prior evidence were verified unchanged. The draft set and DR-12 remain pending.

## Verification and limits

- **PASS** · Checked 259 local link targets or fragments before Report emission, the recorded hashes of all five candidates, all 12 exception records and the required review sections, and byte identity of DR-1–DR-11 and the prior DR-12 body. Reconciliation SHA-256 `682713e3768b2137ca5424bc7d1887a3abdb0fe2eee669f9a18eb08c7555ab34`.
- **PASS** · The bounded offline ArchUnit reactor with Java 21 completed in Maven-reported **29.370 s**, exit 0. All **90 tests** passed with zero failures, errors or skips. The result supplies existing-software evidence. Candidate runtime conformance remains future evidence.
- **FAIL** · `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 005` exited 1 with `No corresponding HCRL for software-0.1.0-005`. HCRL.004 records the cumulative pending review. Whole-iteration layout validation requires the reserved matching HCRL.005.
- **UNRESOLVED** · The system-recorded instruction-entry time was unavailable. The exact instruction, unresolved timestamp and filename prefix are preserved for Solution Architect treatment before closure.
- **PASS** · Git HEAD, index and refs match the entry state. The worktree delta is confined to the reconciliation, formal Task/Report and DR-12 extension. Accepted Architecture 1.1.26 and the earlier evidence are preserved.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-g03-pragma-family-cross-document-reconciliation-task.md
[evidence]: ../G03-Pragma-Family-Cross-Document-Reconciliation-005.md
[readiness]: ../G03-Pragma-Family-Cross-Document-Reconciliation-005.md#completeness-and-readiness
[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
