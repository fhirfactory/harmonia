Report-Produced-At: 2026-11-20T03:52:19.420854Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — Calliope iteration 005 G02 architectural reconciliation

The [operative task][task] commissioned A03–A05 evidence at `software-0.1.0/005`. The resulting [reconciliation][reconciliation] is ready for Solution Architect adjudication. Source was `feature/BL-CALLIOPE-OO-01`, HEAD `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`, `Software-Version-0.1.0-Snapshot-004`, and accepted Architecture Continuum 1.1.26. HCRL.004 DR-1 identifies the associated historical BL-CALLIOPE-OO-01 revision 1 and the reviewed candidate. No Java, XML or SQL changes were present between that implementation commit and the snapshot. [Release][release] · [HCRL.004][hcrl] · [Backlog Item][backlog] · [G02 assignment scope][groups]

## Outcome and unresolved matters

- **A03** · The accepted UUID, numbered exchange and registry contract requires explicit correspondence to F-01's settled structural model. Authoritative registry partitions, deployed UUIDs, alias bindings and historical replay corpus remain unverified. The document identifies observed consumers and options for migration or continued resolution.
- **A04** · The unqualified designation and independent IU UUID are settled for the redesign. Exact qualified vocabulary, binding-specific acceptance and legacy Topic UUID resolution remain open. The FDN note's canonical-name terminology and distinct Topic UUID coverage need governed alignment.
- **A05** · Shared definition, dataset content, native codes and consuming authority have distinct owners. The candidate pairings of relationship codes and Topics, lifecycle dispositions and operational views require bounded correspondence. Existing loss of stage fidelity and suppression of commit uncertainty are implementation conformance findings. G06 supplies the governed lifecycle facts and G03/G04 consume the resulting mappings.

## Verification and resulting state

- **PASS** · Checked the requested finding coverage and traceability, 72 source links/fragments in the document and four in the Task. Both accepted Topic source files matched the recorded SHA-256. The canonical retained locator mismatch previously reported by iteration 004 remains observable at HEAD. [Iteration 004 evidence][prior]
- **PASS** · Seven targeted observations were reproduced with local synthetic inputs and the snapshot's Java 21 Surefire classpath. Two calls to `InformationUnitIdentifier.toTopic()` returned equal objects with different UUIDs. The Topic reader accepted unrelated qualifiers plus a trailing entry, and compressed input accepted a leading plus in a 16-character half. `ErgonPayloadFhirConverter.deserializeTopic()` silently kept the second of two JSON entries at sequence key `0`. A Topic stage round trip repeated its prefix, while `LifecycleContext` reported committed on `COMMITTED` stage with `UNKNOWN` outcome and permitted an Archived disposition with a false archive flag. These probes establish the observed behaviours in the snapshot. Runtime deployment and replay remain unverified.
- **PASS** · ArchUnit reported 90 tests with zero failures, errors or skips in the 36-module dependency reactor with Maven 3.9.12 and OpenJDK 21.0.13.1. Maven reported 30.463 seconds. The initial environment's Java 25 run completed but ArchUnit emitted major-version-69 import warnings. The Java 21 result supplies the verification evidence.
- **UNRESOLVED** · The attributable Task instruction-entry time was unavailable. The Task has an unresolved timestamp and filename prefix for Solution Architect treatment before closure. Whole-iteration checking requires a corresponding HCRL.005, which this assignment reserves. The formal records are associated with the established Iteration 005 evidence directory.
- **PASS** · Verified HEAD, index and tracked paths against the originating Git state, preservation of 12 pre-existing untracked files, and absence of HCRL.005. The new G02 document and its formal Task/Report are the resulting working-tree additions.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 300s mvn -o -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-calliope-g02-architectural-reconciliation-task.md
[reconciliation]: ../Calliope-G02-Architectural-Reconciliation-005.md
[groups]: ../Calliope-Architectural-Issue-Groups-005.md#g02--governed-topic-identity-reference-migration-and-classification-vocabulary
[release]: ../../../../../../../architecture-release.json
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-1-whole-of-calliope-scope-and-candidate
[backlog]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[prior]: ../../../004/after/F-01-Architecture-Reconciliation.md#existing-evidence-locator-exception
