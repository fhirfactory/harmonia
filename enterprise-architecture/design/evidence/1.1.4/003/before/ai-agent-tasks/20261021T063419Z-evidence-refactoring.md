<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Detail — HCRL 1.1.4-003

**Task-Issued-At: 2026-10-21T06:34:19.881Z.** Event: the operative instruction entered the agent interaction.
Attributable source: system-recorded `response_item` / `message`,
line 9 of `/home/hunterm/.codex/sessions/2026/10/21/rollout-2026-10-21T17-34-16-01a15d2a-eb4c-7f13-9fba-561a55ea356d.jsonl`; role `user`.
The event is retained at its established millisecond precision; the UTC filename
prefix derives from the same event to seconds. No filesystem, commit, current-clock
or inferred-sequence time is substituted.

Retained during this 003 activity from the operative user message.

Correspondence: [pending HCRL 003](../../../../../review/1.1.4/hcrl-1.1.4-003.pending.md);
its corresponding new Report is retained under `003/after/ai-agent-reports/`.
The complete issued instruction below expressly authorises this iteration-scoped
repository/evidence refactoring and reserves Praxis implementation for 004 and
migration adjudication for 005. The 1.1.3 execution-only rule remains unchanged
in the governed documents. No release increment or closure is authorised here.

The delimited original text below is the complete message text, unchanged.
Its SHA-256 (UTF-8, without an added terminal newline) is
`89adc243c453824f0a0198b49cebd056b5ac9ee9f709ee57a9b8110c727159f5`. Original absolute/file links and their original link
bases remain historical source wording; current navigation is supplied above.
The local session is a recovery source, not a continuing repository dependency.

<!-- ORIGINAL MESSAGE START -->
For 003, I would make the instruction deliberately explicit because we are asking Codex to repair the repository representation of the process, not yet change the Praxis itself.
Goal
Create HCRL 1.1.4-003 as a refactoring of the existing RAD-1.1.4 review and evidence artefacts so that evidence is scoped by HCRL iteration, while preserving and carrying forward the substantive architectural decisions and migration analysis established in 1.1.4-002.
This task is a repository/evidence refactoring exercise only. It does not implement the corrected Architecture Review Praxis and does not proceed with repository content migration.
Expected Outcome
Refactor the RAD-1.1.4 material to establish this structure:
enterprise-architecture/design/evidence/1.1.4/
├── 001/
│   ├── before/
│   └── after/
├── 002/
│   ├── before/
│   └── after/
└── 003/
    ├── before/
    └── after/

The governing semantic is:
evidence/<RAD-version>/<HCRL-iteration>/before|after

Each HCRL iteration identifies a governed activity. before/ contains retained inputs/provenance for that activity and after/ contains retained outputs/provenance produced by that activity.
For substantive AI-agent activity, this includes the formal Task in before/ and the corresponding Report in after/.
Existing durable supporting artefacts produced by Codex for 1.1.4-001 and 1.1.4-002 are to be classified and relocated to the appropriate iteration evidence rather than retained as review-working material.
Create:
enterprise-architecture/design/review/1.1.4/
    hcrl-1.1.4-003.pending.md

003 SHALL carry forward all still-applicable decisions from 002. It SHALL additionally record the architectural decision to refactor evidence around the HCRL-iteration boundary.
Context
Use the current ArchitecturalGovernance branch and current repository state as authority.
The RAD-1.1.4 review exposed a defect in our repository representation.
The following concepts are now distinguished:
- Working material — transient material used while performing work; not retained merely because it existed.
- Evidence — durable material retained because it supports, explains, reproduces or verifies a governed activity.
- HCRL — the governed semantic change/decision record. It is not the working area in which Architecture Review analysis is performed.
The existing 1.1.4-002 review produced supporting artefacts including:
hcrl-1.1.4-002.mapping.csv
hcrl-1.1.4-002.references.csv.gz
hcrl-1.1.4-002.checks.json

These are evidence supporting the 002 activity, not HCRL content and not merely transient working files.
The previous RAD-1.1.3 distinction whereby formal AI-agent Task/Report evidence was retained only for execution of an authorised change has proved too narrow. The iteration itself provides the provenance boundary: substantive AI-agent activity associated with an HCRL iteration requires its Task and Report to be retained as evidence for that iteration.
The corrected Praxis will be implemented separately in 1.1.4-004. Do not perform that work in this task.
Constraints
Preserve historical truth.
Do not rewrite 001 or 002 to make it appear that the corrected structure or Praxis existed when those activities occurred.
Do not change the substantive migration findings, mappings, classifications or architectural decisions established by 002.
Do not resume or execute the documentation/repository migration.
Do not modify the governed Architecture Review Praxis or governance documents to implement the new rule; that is explicitly reserved for 1.1.4-004.
Do not manufacture historical Task or Report content, timestamps, instructions or agent outputs that cannot be established from repository or attributable interaction evidence.
Where the original Task or Report for 001 or 002 can be faithfully recovered from authoritative evidence, preserve it with its attributable event provenance. Where it cannot be recovered faithfully, record that absence explicitly rather than synthesising a replacement.
Do not preserve transient working material merely for completeness.
Preserve all currently valid 002 decisions in 003. Any necessary change caused specifically by this refactoring must be explicit in 003; do not silently alter a prior decision.
The HCRL itself must not become the container for detailed evidence that properly belongs under design/evidence/1.1.4/<iteration>/.
Authorised Activity / Scope
You are authorised to:
1. Inspect the existing 1.1.4-001 and 1.1.4-002 review artefacts and existing RAD-1.1.4 evidence.
2. Determine which existing files are durable evidence versus transient working material.
3. Establish design/evidence/1.1.4/001/, 002/, and 003/ with appropriate before/ and after/ structure.
4. Relocate existing durable evidence to the iteration to which it actually belongs.
5. Recover and retain the formal AI-agent Task and Report for 001 and 002 only where they can be faithfully established from authoritative evidence.
6. Retain this 003 Task itself as the formal AI-agent Task evidence for iteration 003.
7. Produce the corresponding 003 Report as after-evidence.
8. Create hcrl-1.1.4-003.pending.md, carrying forward the applicable 002 decisions and recording the decisions associated with this refactoring.
9. Correct references affected solely by relocated evidence so that they resolve to the new locations.
10. Perform integrity checks necessary to demonstrate that the refactoring has not altered the substantive 002 migration analysis or decisions.
Do not perform the 004 Praxis implementation or the 005 migration adjudication.
Report
Produce the formal Task and Report with durable attributable timestamps under:
enterprise-architecture/design/evidence/1.1.4/003/before/
enterprise-architecture/design/evidence/1.1.4/003/after/

The Report must identify:
- files/directories created, moved, renamed or removed;
- classification of existing 001/002 material as retained evidence versus transient working material;
- recovered 001/002 Task/Report provenance, and any provenance that could not be faithfully recovered;
- reference changes made solely as a consequence of relocation;
- confirmation that 003 carries forward the applicable 002 decisions;
- integrity checks performed;
- any ambiguity requiring Solution Architect adjudication; and
- explicit confirmation that neither the Architecture Review Praxis nor the substantive repository migration was implemented.
Stop after producing hcrl-1.1.4-003.pending.md and the supporting 003 evidence/report. Do not apply any further architectural or migration change without Solution Architect adjudication.
[@hcrl-1.1.4-001.pending.md](file:///home/hunterm/Development/Code/harmonia/enterprise-architecture/design/review/1.1.4/hcrl-1.1.4-001.pending.md)
<!-- ORIGINAL MESSAGE FINISH -->
