Task-Issued-At: UNRESOLVED

Event source: The follow-up instruction is preserved from the Codex interaction. Its attributable instruction-entry timestamp was unavailable. The filename prefix and timestamp require Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Calliope iteration 005 G01 Draft 001.002

**Attribution and scope** · Follow-up instruction from the user in the Codex interaction. It revises the offline G01 proposal within the existing `software-0.1.0/005` review-evidence boundary. [Original G01 Task][original-task] · [Draft 001.001][predecessor] · [005 findings][issues]

**Originating state** · Branch `feature/BL-CALLIOPE-OO-01`, HEAD `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`, Architecture 1.1.26. The predecessor draft is a working-tree artefact with SHA-256 `08d1a94235714380f42d91eda64f90ca1c1214fd8aa69b93f5671bbae6acba9a`. [Architecture release][release] · [HCRL.004][hcrl]

## Operative instruction

~~~markdown
Revise `DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.001.md` to produce `DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.002.md`.

Preserve the existing architectural decisions, references and traceability.

Apply four refinements:

1. **Identity delegation:** Use this terminology consistently. Only the calling agent's ID is propagated. No credentials, Passport or authorisation profile are transported as delegated authority.

2. **Ergon.PEP responsibilities:** Clarify the distinction between inbound enforcement (preventing unauthorised processing) and outbound enforcement (preventing unauthorised release or handoff after processing). Their failure-handling responsibilities are architecturally distinct.

3. **Passport availability:** Explicitly establish timely Passport availability as a joint architectural obligation of Themis and Mneme, while preserving their respective responsibilities.

4. **Themis reconciliation:** Reconcile ID-only delegation with the existing Themis Identity/PEP-Criteria pass-off terminology. Distinguish identity delegation between processing components from internal Themis context and criteria handling. Do not redesign Themis or silently override accepted architecture.

If a genuine architectural contradiction is identified, document it for Solution Architect adjudication rather than independently resolving it.

**Constraints:**
- No implementation changes.
- No Architecture Continuum changes.
- No changes to HCRL.004 or creation of HCRL.005.
- No additional architectural decisions.
- Do not commit or push.

Return Draft 001.002 and a concise description of the changes and any outstanding conflicts.
~~~

[original-task]: unresolved-g01-ergon-authorisation-and-identity-delegation-task.md
[predecessor]: ../../../../../offline-design/DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.001.md
[issues]: ../../after/Calliope-Architectural-Issue-Groups-005.md#g01--security-context-provenance-and-preservation-of-security-criteria
[release]: ../../../../../../../architecture-release.json
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
