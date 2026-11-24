Task-Issued-At: UNRESOLVED

Event source: Codex interaction. The attributable original instruction-entry timestamp is unavailable. The Task event and filename prefix require Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Calliope iteration 005 A04 InformationUnit identity clarification

**Attribution and scope** · The user issued the Solution Architect's A04 clarification in the Codex interaction, with authority to update the draft Design Note and Iteration 005 G02 evidence. [Originating G02 reconciliation][g02]

**Originating state** · Branch `feature/BL-CALLIOPE-OO-01`, HEAD `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`, commit subject `Software-Version-0.1.0-Snapshot-004`, and [Architecture 1.1.26][release]. The review context and historical [BL-CALLIOPE-OO-01 revision 1][backlog] are recorded in [HCRL.004 DR-1][hcrl]. The draft Design Note's original §7.2 is recoverable from HEAD.

## Operative instruction

~~~markdown
# Harmonia — Calliope Iteration 005
## A04 — InformationUnit Identity and ConceptFDN Reconciliation

**Repository:** `fhirfactory/harmonia`  
**Branch:** `feature/BL-CALLIOPE-OO-01`  
**Architecture baseline:** 1.1.26  
**Software snapshot:** Software-Version-0.1.0-Snapshot-004

### Objective

Update `docs/design/DesignNote-ConceptFDN-and-InstanceFDN.md` to reflect the Solution Architect's A04 clarification.

### Architectural decisions

1. An InformationUnit has an independently assigned UUID that establishes its durable identity.

2. An InformationUnit does **not** require an InstanceFDN.

3. The ConceptFDN identifying the InformationUnit concept is:

   `solution=harmonia.domain=internal.concept=InformationUnit`

4. A ConceptFDN describes what something is. It does not establish the identity of an individual IU.

5. The IU UUID remains independent of its classification, information representation, ownership, location, lifecycle and version.

6. The historical `harmonia.mneme.iu.<hex-uuid>` convention must not be presented as the current canonical IU identity model.

### Required changes

- Update §7.2 to reflect these decisions.
- Remove or qualify the outdated canonical IU Topic naming assertion.
- Preserve the distinction between IU concept classification and IU instance identity.
- Retain the established responsibilities of InformationRepresentation.
- Identify any discrepancy with the accepted Architecture Continuum 1.1.26 or existing IU DRPs.
- Record the correction and remaining reconciliation obligations in the Iteration 005 G02 evidence.

### Governance constraints

- Do not modify implementation code.
- Do not modify the accepted Architecture Continuum.
- Do not modify accepted DRPs.
- Do not reopen F-01.1–F-01.81.
- Do not modify HCRL.004 or create HCRL.005.
- Do not commit or push.
- Do not introduce an InstanceFDN for InformationUnit.
- Do not invent a replacement IU Topic identity or migration contract.

### Deliverable

Return the revised Design Note and a concise reconciliation report.

Identify any genuine conflicts requiring Solution Architect adjudication rather than independently resolving them.
~~~

[g02]: ../../after/Calliope-G02-Architectural-Reconciliation-005.md#a04--iu-topic-vocabulary-and-boundary-mappings
[release]: ../../../../../../../architecture-release.json
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-1-whole-of-calliope-scope-and-candidate
[backlog]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
