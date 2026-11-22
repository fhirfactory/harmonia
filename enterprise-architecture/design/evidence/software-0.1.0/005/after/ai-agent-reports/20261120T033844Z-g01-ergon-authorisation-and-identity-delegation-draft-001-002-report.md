Report-Produced-At: 2026-11-20T03:38:44.192771Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — Calliope iteration 005 G01 Draft 001.002

The [follow-up Task][task] produced [Draft 001.002][draft] from [Draft 001.001][predecessor] against Architecture 1.1.26 and snapshot `Software-Version-0.1.0-Snapshot-004` at `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d` on `feature/BL-CALLIOPE-OO-01`. The [prior reconciliation report][prior-report] supplies the originating G01 review context.

## Changes

- **Identity delegation** · Decision 5 uses this term for propagation of the calling agent's ID alone. The serving operation's ordinary processing identity and applicable Passport context retain their role under decision 4.
- **Ergon.PEP** · Decision 2 assigns admission failure to the inbound PEP and release or handoff failure after processing to the outbound PEP. Each distinguishes policy denial from an unavailable determination.
- **Passport availability** · Decision 9 states the joint architectural obligation explicitly. Themis owns policy, Passport resolution and validity. Mneme provides governed operational access in time for enforcement.
- **Themis reconciliation** · The scope of ID-only delegation is the exchange between processing components. Themis continues to supply its distributed PEPs with PEP-Criteria through PEP-Context. The approved Passport-availability Principle permits a governed reference. FORWARD policy and provenance of both identities apply. [Themis enforcement context][pep-context] · [Themis forwarding wording][themis-delegation] · [Draft reconciliation][reconciliation]

The inspected Themis authority leaves forwarding representation and PDP interaction open. A requirement for the caller to convey PEP-Criteria or an authority-bearing context as identity delegation on the same G01 boundary would conflict with the ID-only decision and require Solution Architect adjudication. The Role and context contract correspondence, the A01 and A02 evidence obligations, and the detailed boundary failure outcomes remain open for review. [Draft review matters][review] · [005 findings][issues]

## Verification

- **PASS** · All 12 G01 decisions and all prior reference targets are present. The draft and Task resolved 48 local references and fragments. The tracked tree, index and HEAD matched the entry state, and the six pre-existing draft/evidence files matched their entry checksums. The working-tree additions are Draft 001.002 and its Task/Report.
- **PASS** · The existing ArchUnit suite passed 90 tests with zero failures, errors or skips in the 36-module reactor. Offline run with OpenJDK 21.0.13.1 and Maven 3.9.12, Maven elapsed 30.642 seconds. Command was `timeout --kill-after=10s 120s mvn -B -o -ntp test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`, with `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64` and its `bin` first in `PATH`. This run exercised existing architecture tests.
- **UNRESOLVED** · The follow-up instruction-entry timestamp was unavailable. Its Task records the unresolved event and filename prefix. The iteration-wide evidence check requires a corresponding HCRL and is deferred with HCRL.005.

[task]: ../../before/ai-agent-tasks/unresolved-g01-ergon-authorisation-and-identity-delegation-draft-001-002-task.md
[draft]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.002.md
[predecessor]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.001.md
[prior-report]: 20261120T033240Z-g01-ergon-authorisation-and-identity-delegation-reconciliation.md
[pep-context]: ../../../../../../continuum/application/application-component/899d34ec491948b68aad09e507dea6d1.md#pep-context
[themis-delegation]: ../../../../../../continuum/application/application-component/899d34ec491948b68aad09e507dea6d1.md#themis-availability-and-delegation
[reconciliation]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.002.md#identity-delegation-and-themis-enforcement-context
[review]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.002.md#9-matters-for-solution-architect-review
[issues]: ../Calliope-Architectural-Issue-Groups-005.md#g01--security-context-provenance-and-preservation-of-security-criteria
