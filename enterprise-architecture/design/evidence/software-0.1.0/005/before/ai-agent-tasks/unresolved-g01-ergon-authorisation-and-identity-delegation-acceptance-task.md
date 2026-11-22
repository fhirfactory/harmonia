Task-Issued-At: UNRESOLVED

Event source: The Solution Architect instruction is preserved from the Codex interaction. Its attributable instruction-entry timestamp was unavailable. The filename prefix and timestamp require Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Calliope iteration 005 G01 architectural acceptance

**Attribution** · The instruction below records the Solution Architect's acceptance of [Draft 001.002][draft] and authorises recording that disposition in the offline design and [iteration 005 evidence][issues]. It belongs to the `software-0.1.0/005` review-evidence boundary and follows the [Draft 001.002 Task][prior-task].

**Originating state** · Branch `feature/BL-CALLIOPE-OO-01`, snapshot `Software-Version-0.1.0-Snapshot-004` at `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`, [Architecture 1.1.26][release] and [HCRL.004][hcrl]. Draft 001.002 and the findings are working-tree evidence. The accepted draft has SHA-256 `2606129c7e5177d290ea840082143b403ab7f178fff4fd285a3f321c39fe4ba2`. The issue groups had entry SHA-256 `c504e36b442f39e5f1371b55341c33ab97895c7b3eaef18db29662c3a525cd77`.

## Operative instruction and disposition

~~~markdown
# Harmonia — Calliope Iteration 005 — G01 Acceptance

The Solution Architect accepts:

`DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.002.md`

as the architectural resolution of G01.

## Required actions

1. Create `DRP-Ergon-Authorisation-and-Identity-Delegation.Final.001.md` from Draft 001.002.

2. Preserve all accepted architectural decisions without introducing additional semantics.

3. Record the following outstanding qualifications:
   - Reconcile existing Themis Identity/PEP-Criteria pass-off terminology with ID-only delegation.
   - Verify trusted Pragma/FHIR Task context boundaries.
   - Verify security-criteria representation fidelity.
   - Establish detailed inbound/outbound PEP failure contracts.
   - Verify timely Passport availability through Themis and Mneme.
   - Complete implementation conformance and verification.

4. Record G01's disposition in the Iteration 005 review evidence:
   - **G01:** Architecturally accepted.
   - **A01:** Architectural question resolved; implementation conformance outstanding.
   - **A02:** Architectural question resolved; representation fidelity and implementation conformance outstanding.

5. Preserve the remaining G02–G07 issues and all cumulative HCRL.004 obligations.

## Governance constraints

- Do not modify the accepted Architecture Continuum 1.1.26.
- Do not modify HCRL.004.
- Do not create HCRL.005 yet.
- Do not modify implementation code.
- Do not commit or push.
- Preserve architectural traceability and existing references.

Return the accepted DRP and updated Iteration 005 evidence for review.
~~~

[draft]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.002.md
[issues]: ../../after/Calliope-Architectural-Issue-Groups-005.md#g01--security-context-provenance-and-preservation-of-security-criteria
[prior-task]: unresolved-g01-ergon-authorisation-and-identity-delegation-draft-001-002-task.md
[release]: ../../../../../../../architecture-release.json
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
