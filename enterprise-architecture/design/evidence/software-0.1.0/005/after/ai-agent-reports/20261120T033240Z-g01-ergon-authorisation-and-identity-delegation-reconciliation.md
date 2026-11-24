Report-Produced-At: 2026-11-20T03:32:40.686935Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — Calliope iteration 005 G01 reconciliation

Codex prepared the [Ergon Authorisation and Identity Delegation draft][draft] from the [operative Solution Architect instruction][task]. The source was `feature/BL-CALLIOPE-OO-01` at snapshot `Software-Version-0.1.0-Snapshot-004` (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`) with Architecture 1.1.26 and the existing [iteration 005 review evidence][issues].

## Reconciliation

- **A01** · G01 establishes identity-only delegation, Themis resolution at invocation and enforcement at the receiving operation. Trusted transport and operation binding still need evidence for each supported Pragma FHIR Task path. DR-7 remains pending. [005 findings][issues] · [HCRL.004 DR-7][dr7]
- **A02** · G01 preserves the IU's security requirements through processing, access and delegation. The typed action-mask loss is a demonstrated software defect. The supported typed and legacy bindings, literal or regular expression semantics, and resulting authorisation behaviour require separate fidelity and conformance evidence. G03 remains relevant where selector migration is required. [005 findings][issues] · [IU metadata][iu-metadata] · [Draft conformance expectations][conformance]
- **Potential conflict** · The source-qualified Themis design describes Identity / PEP-Criteria pass-off. G01 gives Ergon a narrower ID-only forwarding rule and resolves authority with Themis at invocation. The approved Passport-availability Principle permits governed references, and FORWARD policy and provenance still apply. The draft maps the OR rule to complete eligible grants without changing conjunction within a Criterion. The bounded wording treatment and the exact Role/Passport-to-PEP contract correspondence await Solution Architect review. [Themis delegation][themis] · [Passport Propagation / Availability][passport] · [Draft review matters][review]
- **Responsibility boundaries** · Ergon hosts inbound and outbound enforcement for its own operation. Themis supplies policy and enforcement semantics, Mneme supports timely operational Passport availability, and the IU retains its own requirements. Petasos retains queue and routing operations and the applicable Themis check for FORWARD on those operations. The draft records encryption of all IUs in transit and at rest, with the established external boundary governing egress. The gate-specific failure outcomes still need review. [Draft architectural definition][decisions]

## Verification and resulting state

- **PASS** · All 12 decisions were checked against the issued wording. The draft and Task resolved 43 local references and fragments. HEAD, index and tracked files matched the originating state, and the three pre-existing iteration 005 evidence files matched their entry checksums.
- **PASS** · The existing ArchUnit suite ran offline with OpenJDK 21.0.13.1 and Maven 3.9.12. All 90 tests passed with zero failures, errors or skips across the 36-module dependency reactor in 29.346 seconds. This is baseline software evidence. The A01 transport and A02 fidelity obligations were outside this test exercise, and canonical validation was outside the draft-only scope.
- **UNRESOLVED** · The instruction-entry timestamp was unavailable. The Task retains the issued wording and unresolved filename prefix for Solution Architect treatment. The whole-iteration checker requires a corresponding HCRL. HCRL.004 is the current pending record, and preparation of HCRL.005 is deferred as instructed.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:$PATH \
timeout --kill-after=10s 120s mvn -B -o -ntp test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

The draft and its formal Task/Report evidence are working-tree additions for Solution Architect review.

[draft]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.001.md
[task]: ../../before/ai-agent-tasks/unresolved-g01-ergon-authorisation-and-identity-delegation-task.md
[issues]: ../Calliope-Architectural-Issue-Groups-005.md#g01--security-context-provenance-and-preservation-of-security-criteria
[dr7]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-7-pragma-fhir-security-context-authority
[iu-metadata]: ../../../../../../continuum/application/data-object/c05b7e37278141488ecc90b9a9ef96eb.md#current-information-and-operational-allocation
[conformance]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.001.md#8-conformance-expectations
[themis]: ../../../../../../continuum/application/application-component/899d34ec491948b68aad09e507dea6d1.md#themis-availability-and-delegation
[passport]: ../../../../../../continuum/motivation/principle/architectural-axiom/284b53f4a3c54acdba9e707124e997b8.md#architectural-content
[review]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.001.md#9-matters-for-solution-architect-review
[decisions]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.001.md#4-architectural-definition
