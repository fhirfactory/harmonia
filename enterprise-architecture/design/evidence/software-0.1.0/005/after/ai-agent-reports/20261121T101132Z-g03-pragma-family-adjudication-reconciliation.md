Report-Produced-At: 2026-11-21T10:11:32.242328Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — G03 Pragma-family adjudication reconciliation · Iteration 005

The [operative Task][task] authorised a candidate-design reconciliation of the five drafts pinned by the [cross-document review][basis], and an investigation of the proposed Petasos–Kleio audit integration. Starting state was branch `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`) with pre-existing local changes, accepted Architecture 1.1.26 and cumulative pending software HCRL.004. The activity is recorded in [DR-12][hcrl].

## Outcomes

- Issued five sequential candidate revisions. They apply the adjudicated owner/worker allocation, PublishingMask scope/default, release-specific subscription matching, PragmaFactory responsibilities and undertaking-reference delivery. The revisions preserve the two-cache allocation and distinguish WorkSpace, Pragmaton and undertaking identity, plus actual Ergon-instance TaskOutcome and the independent dispositions. [Adjudications and exact candidates][register]
- Recorded eight partially resolved exceptions, three unresolved exceptions and one editorial correction. The register identifies the prior qualified egress-Pragma and Mneme selection allocations, the prior parcel referent and receiver-side creation, and the prior TaskOutcome Topic as explicit canonical correspondence matters. It also preserves the Twin, cache, selector, acknowledgement, identity/retry, access/save, trust and Assurance decisions still outstanding. [PF-01–PF-12][register]
- Prepared a separate [Petasos–Kleio investigation candidate][audit]. The existing Kleio Audit Capture client contract supplies the functional correspondence for a per-engine local binding, whose exact Audit.Agent/Audit.Satellite identity remains unestablished. The repository evidence includes the historical Audit Framework mandate and governed Kleio resilient capture material. A separately standing Audit Framework DRP was not located. The candidate distinguishes Petasos and Mneme occurrence contributions, Kleio authority, and the separate role of Logging, Provenance and Assurance.
- Apache's Artemis 2.33.0 manual supports a single broker-local commit for the two sends on a shared transactional session with persistent messages and established durable routes. The proposal preserves equivalent or stronger durability for mandatory audit evidence, with custody until the durable AuditEvent and required downstream acknowledgement are established. The local commit excludes Mneme/Mnemosyne state and downstream AuditEvent persistence. The proposed authoritative staging boundary, its outage/readiness conditions and equivalent guarantees, the local integration identity, and transaction authority and evidence contracts await explicit Solution Architect decision. Current AX-07 and Kleio capture rules govern. [Audit candidate][audit]
- Added the material results and evidence references to DR-12. HCRL.004 and DR-12 remain pending Solution Architect review. The HCRL preserves the previous review body and other DR rows. Changes are confined to candidate design, DR-12 and its originating evidence. Git HEAD and staged content match the starting state. The source, accepted Continuum, release allocation and publications were outside the change set.

## Verification and limits

- **PASS** · Java 21 offline ArchUnit reactor exited 0 in 27.989 seconds as reported by Maven. All **90** tests passed with zero failures, errors or skips. This is existing-software conformance evidence. Effective Artemis configuration, production deployment and candidate runtime behaviour were not exercised.
- **PASS** · Reconciled all 12 PF dispositions and retained PS-01–PS-16. Checked 284 local target and fragment references before Report emission, original five predecessor SHA-256 values, original cross-document review hash, prior HCRL body and other DR rows, and permitted worktree paths.
- **FAIL** · `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 005` exited 1 with `No corresponding HCRL for software-0.1.0-005`. The instruction places 005 evidence under the existing layout and directs the record update to cumulative HCRL.004. Whole-iteration tooling verification requires the matching HCRL or an explicit governance disposition.
- **UNRESOLVED** · The original instruction-entry system timestamp and filename prefix are unavailable. The Task retains the operative instruction and unresolved timestamp for Solution Architect treatment before closure.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

## Candidate and register SHA-256

```text
1c1991f84817c69d0a46a84a830803acb3c377cbf45ec0599991323bc41e4415  DRP-Mneme-Pragma-Model.draft.000.004.md
6d546cc237bc963bbc79366420a74cbbe6ab90278d0d40aed4086f9c454b3377  DRP-Mneme-Pragmaton-Model.draft.000.004.md
6b0e4e6325e886225ebc1c789be1f524bdb3c9bcdbfd97e89246f035f1ba36e4  DRP-Pragma-Publication-and-Subscription.draft.000.003.md
511b0eccaa6c20457245ea2faf1b49c326e5fc78646d1cd513b6c668453b5cec  DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.004.md
c505001961cd8970f6eea14bf005ffc5e9c8030e45d5b6a52081c93abd50eb8c  DRP-Interaction-Petasos-to-Mneme.draft.000.004.md
90954f8c6eb08c3f41356ee6fbf7a4c29ff49653eccbc138ed23f3841a03179d  DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md
d5055d0f871fbb4bdf3cd73b60b78cd43dd8e6239d979b4b5de3605d91feb5df  G03-Pragma-Family-Adjudications-005.md
```

[task]: ../../before/ai-agent-tasks/unresolved-g03-pragma-family-adjudication-reconciliation.md
[basis]: ../G03-Pragma-Family-Cross-Document-Reconciliation-005.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
[register]: ../G03-Pragma-Family-Adjudications-005.md
[audit]: ../../../../../offline-design/DRP-Interaction-Petasos-to-Kleio-Audit-Capture.draft.000.001.md
