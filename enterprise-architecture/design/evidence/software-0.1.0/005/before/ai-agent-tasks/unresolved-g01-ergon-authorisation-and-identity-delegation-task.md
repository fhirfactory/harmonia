Task-Issued-At: UNRESOLVED

Event source: The operative instruction is preserved from the Codex interaction. Its attributable instruction-entry timestamp was unavailable. The filename prefix and timestamp require Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Calliope iteration 005 G01 DRP and reconciliation

**Attribution** · The user issued the instruction below in the Codex interaction as the Solution Architect's G01 adjudication for documentation. The proposal is for Solution Architect review. The activity belongs to the existing review-evidence boundary `software-0.1.0/005`. [G01 review evidence](../../after/Calliope-Architectural-Issue-Groups-005.md#g01--security-context-provenance-and-preservation-of-security-criteria)

**Originating state** · HEAD `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d` on the named branch and [Architecture release 1.1.26](../../../../../../../architecture-release.json). The existing 005 findings and consolidation Task/Report are working-tree evidence. The [HCRL.004](../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md) locates historical BL-CALLIOPE-OO-01 revision 1 and the assignment-specific whole-of-Calliope authority. This instruction concerns the G01 proposal.

## Operative instruction

~~~markdown
# Harmonia — Calliope Review Iteration 005
## G01 — Ergon Authorisation and Identity Delegation

Repository: `fhirfactory/harmonia`  
Branch: `feature/BL-CALLIOPE-OO-01`  
Architecture baseline: `1.1.26`  
Software snapshot: `Software-Version-0.1.0-Snapshot-004`

### Objective

Document the Solution Architect's G01 adjudication in a new architectural Decision Record Proposal (DRP).

### Deliverable

Create:

`DRP-Ergon-Authorisation-and-Identity-Delegation.draft.001.001.md`

Place it in the established offline-design directory.

### Architectural decisions to record

1. Security context is determined through:
   - IU.SecurityMetadata — information security requirements.
   - Ergon.Process.Role — processing identity and authorisation profile.
   - Themis.PRP — applicable rules.

2. The common Ergon framework incorporates distinct inbound and outbound PEP enforcement responsibilities:

   `Ergon.In → Ergon.PEP.In → Ergon.Process → Ergon.PEP.Out → Ergon.Out`

   Inbound and outbound enforcement have different failure-handling requirements.

3. An Ergon.Process operates under its own Role and applicable Passport.

4. For service calls initiated by Ergon.Process, its processing identity is provided to the serving component within the applicable Passport context.

5. Credential delegation propagates only the calling agent's ID, never the agent's Passport or authorisation profile.

6. IU.InformationRepresentation determines whether the content includes a delegated identity requiring consideration.

7. At invocation, Themis resolves the applicable Passport for each required identity.

8. The effective authorisation profile combines the processing Ergon's authority with validated, permitted delegated authority using the agreed OR semantics.

9. Themis and Mneme jointly ensure timely availability of Passports for supplied agent identities.

10. The IU's SecurityMetadata and security requirements remain unchanged by processing, access or delegation.

11. All IUs are encrypted in transit and at rest.

12. Petasos.Queue and Petasos.Router do not determine whether an Ergon.Process is authorised to process an IU.

### Review and reconciliation

Reference the G01 findings A01 and A02 from `Calliope-Architectural-Issue-Groups-005.md`.

Identify relevant existing architectural authority and any potential conflicts.

Preserve established Themis, Mneme, IU and Ergon responsibilities unless explicitly changed by the decisions above.

Distinguish architectural decisions from implementation obligations, including representation fidelity, Passport resolution and authorisation testing.

### Constraints

- Do not modify implementation code.
- Do not modify the accepted Architecture Continuum.
- Do not change existing accepted DRPs.
- Do not modify HCRL.004.
- Do not create or update HCRL.005 yet.
- Do not commit or push.
- Do not introduce additional architectural decisions without Solution Architect adjudication.

### Completion

Provide the draft DRP and a concise reconciliation report.

Stop for Solution Architect review.
~~~
